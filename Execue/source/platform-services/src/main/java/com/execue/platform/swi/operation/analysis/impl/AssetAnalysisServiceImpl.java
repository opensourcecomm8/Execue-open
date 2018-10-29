/**
 * Licensed to the Execue Software Foundation (ESF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ESF licenses this file
 * to you under the Execue License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.execue.platform.swi.operation.analysis.impl;

import static com.execue.platform.swi.operation.AssetOperationHelper.convertAnalysisReportToXML;
import static com.execue.platform.swi.operation.AssetOperationHelper.convertXMLToAnalysisReportObject;
import static com.execue.platform.swi.operation.AssetOperationHelper.populateAssetOperationInfo;
import static com.execue.platform.swi.operation.AssetOperationHelper.populateHistoryAssetOperationInfo;
import static com.execue.platform.swi.operation.AssetOperationHelper.transformColumns;
import static com.execue.platform.swi.operation.AssetOperationHelper.transformMembers;
import static com.execue.platform.swi.operation.AssetOperationHelper.transformTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetOperationInfo;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DefaultMetric;
import com.execue.core.common.bean.entity.HistoryAssetOperationInfo;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.swi.AssetAnalysisReport;
import com.execue.core.common.bean.swi.AssetAnalysisReportInfo;
import com.execue.core.common.bean.swi.AssetAnalysisTableInfo;
import com.execue.core.common.type.AssetAnalysisOperationType;
import com.execue.core.common.type.AssetAnalysisThresholdType;
import com.execue.core.common.type.AssetOperationType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.OperationEnum;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.common.util.AssetGrainUtils;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.exception.AssetAnalysisException;
import com.execue.platform.swi.operation.AssetOperationHelper;
import com.execue.platform.swi.operation.analysis.IAssetAnalysisService;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.AssetAnalysisExceptionCodes;
import com.execue.swi.exception.AssetOperationExceptionCodes;
import com.execue.swi.exception.JoinException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IJoinService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXDeletionService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

public class AssetAnalysisServiceImpl implements IAssetAnalysisService {

   private ISDXRetrievalService     sdxRetrievalService;
   private ISDXManagementService    sdxManagementService;
   private ISDXDeletionService      sdxDeletionService;
   private IJoinService             joinService;
   private IMappingRetrievalService mappingRetrievalService;
   private ISWIConfigurationService swiConfigurationService;

   public AssetAnalysisReport fetchAssetAnalysisReport (Long assetId) throws AssetAnalysisException {
      AssetAnalysisReport assetAnalysisReport = null;
      try {
         String analysisReport = getSdxRetrievalService().getAssetOperationDataByAssetId(assetId,
                  AssetOperationType.ASSET_ANALYSIS);
         if (ExecueCoreUtil.isNotEmpty(analysisReport)) {
            assetAnalysisReport = convertXMLToAnalysisReportObject(analysisReport);
         }
      } catch (SDXException sdxException) {
         throw new AssetAnalysisException(sdxException.getCode(), "Asset Ananlysis Report fetch failed");
      }
      return assetAnalysisReport;
   }

   public boolean populateAssetAnalysisReport (Long assetId, PublishAssetMode publishAssetMode)
            throws AssetAnalysisException {
      Date startDate = new Date();
      boolean isAnalysisReportFound = false;
      try {
         Asset asset = getSdxRetrievalService().getAssetById(assetId);
         List<AssetAnalysisReportInfo> assetAnalysisReportInfoList = populateAssetAnalysisReport(asset);
         AssetAnalysisReport assetAnalysisReport = new AssetAnalysisReport();
         assetAnalysisReport.setOperationAsset(AssetOperationHelper.transformAsset(asset));
         assetAnalysisReport.setAssetAnalysisReportInfoList(assetAnalysisReportInfoList);
         assetAnalysisReport.setSelectedPublishAssetMode(publishAssetMode);
         Date completionDate = new Date();
         if (ExecueCoreUtil.isCollectionNotEmpty(assetAnalysisReportInfoList)) {
            isAnalysisReportFound = true;
         }
         // TODO:-VG- need to see if service is called with job or interactively based on jobId.
         OperationEnum operation = OperationEnum.INTERACTIVE;

         String assetOperationData = convertAnalysisReportToXML(assetAnalysisReport);
         AssetOperationInfo assetOperationInfo = populateAssetOperationInfo(assetId, startDate, completionDate,
                  assetOperationData, isAnalysisReportFound, CheckType.NO, operation, AssetOperationType.ASSET_ANALYSIS);

         AssetOperationInfo existingAssetOperation = null;
         try {
            existingAssetOperation = getSdxRetrievalService().getAssetOperationByAssetId(assetId,
                     AssetOperationType.ASSET_ANALYSIS);
         } catch (SDXException sdxException) {
            // There might not be an entry existing for the given asset in Sync Info Table
            if (AssetOperationExceptionCodes.ASSET_OPERATION_ENTRY_NOTFOUND_BY_ASSET_ID != sdxException.getCode()) {
               throw sdxException;
            }
         }

         if (existingAssetOperation != null) {
            // move the record to history table
            // delete the existing record
            HistoryAssetOperationInfo historyAssetOperationInfo = populateHistoryAssetOperationInfo(existingAssetOperation);
            getSdxManagementService().createHistoryAssetOperation(historyAssetOperationInfo);
            getSdxDeletionService().deleteAssetOperation(existingAssetOperation);
         }
         getSdxManagementService().createAssetOperation(assetOperationInfo);
      } catch (SDXException sdxException) {
         sdxException.printStackTrace();
         throw new AssetAnalysisException(sdxException.getCode(), "Asset Ananlysis Report Population failed");
      }
      return isAnalysisReportFound;

   }

   private List<AssetAnalysisReportInfo> populateAssetAnalysisReport (Asset asset) throws AssetAnalysisException {
      List<AssetAnalysisReportInfo> assetAssetAnalysisReportInfos = new ArrayList<AssetAnalysisReportInfo>();
      AssetAnalysisReportInfo lookupTablesWithoutMembers = analyseLookupTablesWithoutMembers(asset);
      if (lookupTablesWithoutMembers != null) {
         assetAssetAnalysisReportInfos.add(lookupTablesWithoutMembers);
      }
      AssetAnalysisReportInfo tablesWithoutJoins = analyseTablesWithoutJoins(asset);
      if (tablesWithoutJoins != null) {
         assetAssetAnalysisReportInfos.add(tablesWithoutJoins);
      }
      AssetAnalysisReportInfo unMappedColumnsOfTable = analyseUnmappedColumns(asset);
      if (unMappedColumnsOfTable != null) {
         assetAssetAnalysisReportInfos.add(unMappedColumnsOfTable);
      }
      AssetAnalysisReportInfo unMappedMembersOfTable = analyseUnmappedMembers(asset);
      if (unMappedMembersOfTable != null) {
         assetAssetAnalysisReportInfos.add(unMappedMembersOfTable);
      }
      AssetAnalysisReportInfo columnsWithoutColumnType = analyseColumnWithoutColumnType(asset);
      if (columnsWithoutColumnType != null) {
         assetAssetAnalysisReportInfos.add(columnsWithoutColumnType);
      }
      AssetAnalysisReportInfo tablesWithoutDefaultMetrics = analyseTablesWithoutDefaultMetrics(asset);
      if (tablesWithoutDefaultMetrics != null) {
         assetAssetAnalysisReportInfos.add(tablesWithoutDefaultMetrics);
      }
      AssetAnalysisReportInfo assetForGrain = analyseAssetForGrain(asset);
      if (assetForGrain != null) {
         assetAssetAnalysisReportInfos.add(assetForGrain);
      }
      return assetAssetAnalysisReportInfos;
   }

   public AssetAnalysisReportInfo analyseAssetForGrain (Asset asset) throws AssetAnalysisException {
      // 1. call getAssetGrain on MappingService
      // 2. call AssetGrainUtils to verify that DDC and DPC exists
      AssetAnalysisReportInfo assetAnalysisReportInfo = null;
      StringBuilder message = null;
      try {
         List<Mapping> assetGrain = getMappingRetrievalService().getAssetGrain(asset.getId());
         if (AssetGrainUtils.getDefaultDistributionConcept(assetGrain) == null) {
            message = new StringBuilder();
            message.append("Default Distribution Concept is not defined for Asset " + asset.getName());
         }
         if (AssetGrainUtils.getDefaultPopulationConcept(assetGrain) == null) {
            if (message == null)
               message = new StringBuilder();
            message.append("Default Population Concept is not defined for Asset " + asset.getName());
         }
         if (message != null) {
            assetAnalysisReportInfo = new AssetAnalysisReportInfo();
            assetAnalysisReportInfo.setAssetAnalysisOperationType(AssetAnalysisOperationType.ASSET_WITHOUT_GRAIN);
            assetAnalysisReportInfo.setThresholdMessage(message.toString());
            assetAnalysisReportInfo.setAssetAnalysisThresholdType(AssetAnalysisThresholdType.OVER_THRESHOLD);
         }

      } catch (MappingException mappingException) {
         throw new AssetAnalysisException(AssetAnalysisExceptionCodes.ASSET_GRAIN_CHECK_FAILED, mappingException);
      }
      return assetAnalysisReportInfo;
   }

   public AssetAnalysisReportInfo analyseColumnWithoutColumnType (Asset asset) throws AssetAnalysisException {
      // 1. Get all table objects (SDXService)
      // 3. get all columnIds of the table without kdxtype
      // 5. if(size of columns without kdxtype > treshold)
      // set the msg
      // else
      // get populate columnsWithoutKDXType(no kdxtype column ids list)
      AssetAnalysisReportInfo assetAnalysisReportInfo = null;
      try {
         List<Tabl> tables = getSdxRetrievalService().getAllTables(asset);
         List<AssetAnalysisTableInfo> assetAnalysisTableInfoList = new ArrayList<AssetAnalysisTableInfo>();
         for (Tabl table : tables) {
            List<Long> columnIds = getSdxRetrievalService().getColumnsWithoutKDXDataType(asset.getId(), table.getId());
            // TODO :-VG- need to have a better solution to avoid desc column of lookup from processing
            if (!LookupType.None.equals(table.getLookupType())) {
               Colum colum = getSdxRetrievalService().getAssetTableColum(asset.getId(), table.getName(),
                        table.getLookupDescColumn());
               columnIds.remove(colum.getId());
            }
            if (ExecueCoreUtil.isCollectionNotEmpty(columnIds)) {
               AssetAnalysisTableInfo assetAnalysisTableInfo = new AssetAnalysisTableInfo();
               assetAnalysisTableInfo.setOperationTable(transformTable(table));
               if (columnIds.size() > getSwiConfigurationService().getColumnWithoutKdxTypeLimit()) {
                  assetAnalysisTableInfo.setAssetAnalysisThresholdType(AssetAnalysisThresholdType.OVER_THRESHOLD);
                  assetAnalysisTableInfo.setThresholdMessage("There are " + columnIds.size()
                           + " columns without column type defined for table : " + table.getDisplayName());
               } else {
                  assetAnalysisTableInfo.setAssetAnalysisThresholdType(AssetAnalysisThresholdType.UNDER_THRESHOLD);
                  assetAnalysisTableInfo.setOperationColumns(transformColumns(getSdxRetrievalService()
                           .getPopulatedColumns(columnIds)));
               }
               assetAnalysisTableInfoList.add(assetAnalysisTableInfo);
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(assetAnalysisTableInfoList)) {
            assetAnalysisReportInfo = new AssetAnalysisReportInfo();
            assetAnalysisReportInfo
                     .setAssetAnalysisOperationType(AssetAnalysisOperationType.COLUMNS_WITH_MISSING_COLUMTYPE);
            assetAnalysisReportInfo.setAssetAnalysisTablesInfo(assetAnalysisTableInfoList);
         }
      } catch (SDXException sdxException) {
         throw new AssetAnalysisException(AssetAnalysisExceptionCodes.COLUMN_WITH_NO_KDXTYPE_FAILED, sdxException);
      }
      return assetAnalysisReportInfo;
   }

   public AssetAnalysisReportInfo analyseLookupTablesWithoutMembers (Asset asset) throws AssetAnalysisException {
      // 1. Get all lookup table ids(SDXService)
      // 2. Get all lookup table ids which have members(SDXService)
      // 3. Lookup tables without members size = AllLookup tables.size - memberLookupTables.size
      // 4. If (size > threshold)
      // set the message
      // else
      // get PopulateNonMemberLookupTables(nonMemberLookup tables list)(SDXService)
      // populate the structure
      AssetAnalysisReportInfo assetAnalysisReportInfo = null;
      try {
         List<Long> lookupTableIds = getSdxRetrievalService().getLookupTableIdsForAsset(asset.getId());
         if (ExecueCoreUtil.isCollectionNotEmpty(lookupTableIds)) {
            List<Long> memberLookupTableIds = getSdxRetrievalService().getLookupTableIdsWithMembers(asset.getId());
            lookupTableIds.removeAll(memberLookupTableIds);
            if (ExecueCoreUtil.isCollectionNotEmpty(lookupTableIds)) {
               assetAnalysisReportInfo = new AssetAnalysisReportInfo();
               assetAnalysisReportInfo
                        .setAssetAnalysisOperationType(AssetAnalysisOperationType.LOOKUP_TABLE_WITHOUT_MEMBERS);
               if (lookupTableIds.size() > getSwiConfigurationService().getLookupTablesWithoutMembersLimit()) {
                  assetAnalysisReportInfo.setAssetAnalysisThresholdType(AssetAnalysisThresholdType.OVER_THRESHOLD);
                  assetAnalysisReportInfo.setThresholdMessage("There are " + lookupTableIds.size()
                           + " tables which has been defined as lookup without members");

               } else {
                  assetAnalysisReportInfo.setAssetAnalysisThresholdType(AssetAnalysisThresholdType.UNDER_THRESHOLD);
                  List<Tabl> nonMemberLookupTables = getSdxRetrievalService().getPopulatedTables(lookupTableIds);
                  List<AssetAnalysisTableInfo> assetAnalysisTableInfoList = new ArrayList<AssetAnalysisTableInfo>();
                  for (Tabl nonMemberLookupTable : nonMemberLookupTables) {
                     AssetAnalysisTableInfo assetAnalysisTableInfo = new AssetAnalysisTableInfo();
                     assetAnalysisTableInfo.setOperationTable(transformTable(nonMemberLookupTable));
                     assetAnalysisTableInfoList.add(assetAnalysisTableInfo);
                  }
                  assetAnalysisReportInfo.setAssetAnalysisTablesInfo(assetAnalysisTableInfoList);
               }
            }
         }
      } catch (SDXException sdxException) {
         throw new AssetAnalysisException(AssetAnalysisExceptionCodes.NON_MEMBER_LOOKUP_ANALYSIS_FAILED, sdxException);
      }
      return assetAnalysisReportInfo;
   }

   public AssetAnalysisReportInfo analyseTablesWithoutDefaultMetrics (Asset asset) throws AssetAnalysisException {
      // TODO Auto-generated method stub
      // 1. get all tables for asset
      // 2. for each table, call getAllExistingDefaultMetrics on MappingService
      // 3. If nothing, then populate message, then no default metric
      // 4. else check whether invalid metrics exists,
      // 5. then use the aedId of the invalid metric to get the column object using getAEDById of SDxService
      // 6. populate the columns under the table.
      AssetAnalysisReportInfo assetAnalysisReportInfo = null;
      try {
         List<Tabl> tables = getSdxRetrievalService().getFactTables(asset.getId());
         List<AssetAnalysisTableInfo> assetAnalysisTableInfoList = new ArrayList<AssetAnalysisTableInfo>();
         for (Tabl tabl : tables) {
            AssetAnalysisTableInfo assetAnalysisTableInfo = new AssetAnalysisTableInfo();
            assetAnalysisTableInfo.setOperationTable(transformTable(tabl));
            List<DefaultMetric> defaultMetricList = getMappingRetrievalService().getAllExistingDefaultMetrics(
                     tabl.getId());
            if (ExecueCoreUtil.isCollectionNotEmpty(defaultMetricList)) {
               assetAnalysisTableInfo.setThresholdMessage("There is no default metric for the table :"
                        + tabl.getDisplayName());
               assetAnalysisTableInfoList.add(assetAnalysisTableInfo);
               assetAnalysisTableInfo.setAssetAnalysisThresholdType(AssetAnalysisThresholdType.OVER_THRESHOLD);
            } else {
               List<Colum> invalidDefaultMetricColumns = new ArrayList<Colum>();
               for (DefaultMetric defaultMetric : defaultMetricList) {
                  if (CheckType.NO.equals(defaultMetric.getValid())) {
                     invalidDefaultMetricColumns.add(getSdxRetrievalService().getAssetEntityDefinitionById(
                              defaultMetric.getAedId()).getColum());
                  }
               }
               if (ExecueCoreUtil.isCollectionNotEmpty(invalidDefaultMetricColumns)) {
                  assetAnalysisTableInfo.setAssetAnalysisThresholdType(AssetAnalysisThresholdType.UNDER_THRESHOLD);
                  assetAnalysisTableInfo.setOperationColumns(transformColumns(invalidDefaultMetricColumns));
                  assetAnalysisTableInfoList.add(assetAnalysisTableInfo);
               }
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(assetAnalysisTableInfoList)) {
            assetAnalysisReportInfo = new AssetAnalysisReportInfo();
            assetAnalysisReportInfo
                     .setAssetAnalysisOperationType(AssetAnalysisOperationType.TABLE_WITHOUT_DEFAULT_METRICS);
            assetAnalysisReportInfo.setAssetAnalysisTablesInfo(assetAnalysisTableInfoList);
         }
      } catch (SDXException sdxException) {
         throw new AssetAnalysisException(AssetAnalysisExceptionCodes.NON_MEMBER_LOOKUP_ANALYSIS_FAILED, sdxException);
      } catch (MappingException mappingException) {
         throw new AssetAnalysisException(AssetAnalysisExceptionCodes.NON_MEMBER_LOOKUP_ANALYSIS_FAILED,
                  mappingException);
      }
      return assetAnalysisReportInfo;
   }

   public AssetAnalysisReportInfo analyseTablesWithoutJoins (Asset asset) throws AssetAnalysisException {
      // 1. Get all table ids (SDXService)
      // 2. Get all table ids which have joins(JoinService)
      // 3. tables without joins size = All tables.size - joinTables.size
      // 4. If (size > treshold)
      // set the message
      // else
      // get PopulateNonJoinTables(non join tables list)(SDXService)
      AssetAnalysisReportInfo assetAnalysisReportInfo = null;
      try {
         List<Long> tableIds = getSdxRetrievalService().getAllTableIdsForAsset(asset.getId(), CheckType.YES);
         if (ExecueCoreUtil.isCollectionNotEmpty(tableIds)) {
            List<Long> joinTableIds = getJoinService().getTableIdsParticipatingInJoins(asset.getId());
            tableIds.removeAll(joinTableIds);
            if (ExecueCoreUtil.isCollectionNotEmpty(tableIds)) {
               assetAnalysisReportInfo = new AssetAnalysisReportInfo();
               assetAnalysisReportInfo.setAssetAnalysisOperationType(AssetAnalysisOperationType.TABLES_WITHOUT_JOINS);
               if (tableIds.size() > getSwiConfigurationService().getTablesWithoutJoinsLimit()) {
                  assetAnalysisReportInfo.setAssetAnalysisThresholdType(AssetAnalysisThresholdType.OVER_THRESHOLD);
                  assetAnalysisReportInfo.setThresholdMessage("There are " + tableIds.size()
                           + " tables on which joins has not been defined");
               } else {
                  assetAnalysisReportInfo.setAssetAnalysisThresholdType(AssetAnalysisThresholdType.UNDER_THRESHOLD);
                  List<Tabl> nonJoinTables = getSdxRetrievalService().getPopulatedTables(tableIds);
                  List<AssetAnalysisTableInfo> assetAnalysisTableInfoList = new ArrayList<AssetAnalysisTableInfo>();
                  for (Tabl nonJoinTable : nonJoinTables) {
                     AssetAnalysisTableInfo assetAnalysisTableInfo = new AssetAnalysisTableInfo();
                     assetAnalysisTableInfo.setOperationTable(transformTable(nonJoinTable));
                     assetAnalysisTableInfoList.add(assetAnalysisTableInfo);
                  }
                  assetAnalysisReportInfo.setAssetAnalysisTablesInfo(assetAnalysisTableInfoList);
               }
            }
         }

      } catch (SDXException sdxException) {
         throw new AssetAnalysisException(AssetAnalysisExceptionCodes.NON_JOIN_TABLES_FAILED, sdxException);
      } catch (JoinException joinException) {
         throw new AssetAnalysisException(AssetAnalysisExceptionCodes.NON_JOIN_TABLES_FAILED, joinException);
      }

      return assetAnalysisReportInfo;
   }

   public AssetAnalysisReportInfo analyseUnmappedColumns (Asset asset) throws AssetAnalysisException {
      // 1. get all tables of asset
      // 2. Get all column ids for the table
      // 3. get all mapped column ids for the table
      // 4. unmapped columns = all column ids.size - all mapped column ids.size
      // 5. If (size > threshold)
      // set the message
      // else
      // get PopulateColumnPerTable(non join tables list)(SDXService)'
      AssetAnalysisReportInfo assetAnalysisReportInfo = null;
      List<AssetAnalysisTableInfo> assetAnalysisTableInfoList = new ArrayList<AssetAnalysisTableInfo>();
      try {
         List<Tabl> tables = getSdxRetrievalService().getAllTables(asset);
         for (Tabl table : tables) {
            AssetAnalysisTableInfo assetAnalysisTableInfo = new AssetAnalysisTableInfo();
            assetAnalysisTableInfo.setOperationTable(transformTable(table));
            // TODO :-consider the case if table is lookup, no need to check for desc column
            List<Long> columnIds = getSdxRetrievalService().getAllColumnIdsForTable(asset.getId(), table.getId());
            if (ExecueCoreUtil.isCollectionNotEmpty(columnIds)) {
               List<Long> mappedColumnIds = getMappingRetrievalService().getMappedColumnIds(asset.getId(),
                        table.getId());
               columnIds.removeAll(mappedColumnIds);
               if (ExecueCoreUtil.isCollectionNotEmpty(columnIds)) {
                  if (columnIds.size() > getSwiConfigurationService().getTableWithUnmappedColumnsLimit()) {
                     assetAnalysisTableInfo.setAssetAnalysisThresholdType(AssetAnalysisThresholdType.OVER_THRESHOLD);
                     assetAnalysisTableInfo.setThresholdMessage("There are " + columnIds.size()
                              + " columns unmapped for this table : " + table.getDisplayName());
                  } else {
                     assetAnalysisTableInfo.setAssetAnalysisThresholdType(AssetAnalysisThresholdType.UNDER_THRESHOLD);
                     assetAnalysisTableInfo.setOperationColumns(transformColumns(getSdxRetrievalService()
                              .getPopulatedColumns(columnIds)));
                  }
                  assetAnalysisTableInfoList.add(assetAnalysisTableInfo);
               }
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(assetAnalysisTableInfoList)) {
            assetAnalysisReportInfo = new AssetAnalysisReportInfo();
            assetAnalysisReportInfo.setAssetAnalysisOperationType(AssetAnalysisOperationType.UNMAPPED_COLUMNS);
            assetAnalysisReportInfo.setAssetAnalysisTablesInfo(assetAnalysisTableInfoList);
         }
      } catch (SDXException e) {
         throw new AssetAnalysisException(AssetAnalysisExceptionCodes.UN_MAPPED_TABLES_COLUMNS_FAILED, e);
      } catch (MappingException e) {
         throw new AssetAnalysisException(AssetAnalysisExceptionCodes.UN_MAPPED_TABLES_COLUMNS_FAILED, e);
      }
      return assetAnalysisReportInfo;
   }

   public AssetAnalysisReportInfo analyseUnmappedMembers (Asset asset) throws AssetAnalysisException {
      // 1. Get lookup tables for the asset
      // 2. get all members ids for the table for the lookup value column
      // 3. get mapped member ids for the table
      // 4. unmapped member columns = all member ids.size - mapped member ids.size
      // 5 if(size > threshold)
      // set the msg
      // else
      // get populate unmapped memberforColumn(unmapped memberids list )
      AssetAnalysisReportInfo assetAnalysisReportInfo = null;
      List<AssetAnalysisTableInfo> assetAnalysisTableInfoList = new ArrayList<AssetAnalysisTableInfo>();
      try {
         List<Long> lookupTableIds = getSdxRetrievalService().getLookupTableIdsForAsset(asset.getId());
         for (Long lookupTableId : lookupTableIds) {
            Tabl table = getSdxRetrievalService().getTableById(lookupTableId);
            AssetAnalysisTableInfo assetAnalysisTableInfo = new AssetAnalysisTableInfo();
            assetAnalysisTableInfo.setOperationTable(transformTable(table));
            List<Long> memberIds = getSdxRetrievalService().getAllMemberIds(asset.getId(), lookupTableId);
            if (ExecueCoreUtil.isCollectionNotEmpty(memberIds)) {
               List<Long> mappedMemberIds = getMappingRetrievalService().getMappedMemberIds(asset.getId(),
                        lookupTableId);
               memberIds.removeAll(mappedMemberIds);
               if (ExecueCoreUtil.isCollectionNotEmpty(memberIds)) {
                  if (memberIds.size() > getSwiConfigurationService().getTableWithUnmappedMemebrsLimit()) {
                     assetAnalysisTableInfo.setAssetAnalysisThresholdType(AssetAnalysisThresholdType.OVER_THRESHOLD);
                     assetAnalysisTableInfo.setThresholdMessage("There are " + memberIds.size()
                              + " unmapped members for the table " + table.getDisplayName());
                  } else {
                     assetAnalysisTableInfo.setAssetAnalysisThresholdType(AssetAnalysisThresholdType.UNDER_THRESHOLD);
                     assetAnalysisTableInfo.setOperationMembers(transformMembers(getSdxRetrievalService()
                              .getPopulatedMembers(memberIds)));
                  }
                  assetAnalysisTableInfoList.add(assetAnalysisTableInfo);
               }
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(assetAnalysisTableInfoList)) {
            assetAnalysisReportInfo = new AssetAnalysisReportInfo();
            assetAnalysisReportInfo.setAssetAnalysisOperationType(AssetAnalysisOperationType.UNMAPPED_MEMBERS);
            assetAnalysisReportInfo.setAssetAnalysisTablesInfo(assetAnalysisTableInfoList);
         }
      } catch (SDXException e) {
         throw new AssetAnalysisException(AssetAnalysisExceptionCodes.UN_MAPPED_TABLES_COLUMNS_FAILED, e);
      } catch (MappingException e) {
         throw new AssetAnalysisException(AssetAnalysisExceptionCodes.UN_MAPPED_TABLES_COLUMNS_FAILED, e);
      }
      return assetAnalysisReportInfo;
   }

   public IJoinService getJoinService () {
      return joinService;
   }

   public void setJoinService (IJoinService joinService) {
      this.joinService = joinService;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public ISDXManagementService getSdxManagementService () {
      return sdxManagementService;
   }

   public void setSdxManagementService (ISDXManagementService sdxManagementService) {
      this.sdxManagementService = sdxManagementService;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   public ISDXDeletionService getSdxDeletionService () {
      return sdxDeletionService;
   }

   public void setSdxDeletionService (ISDXDeletionService sdxDeletionService) {
      this.sdxDeletionService = sdxDeletionService;
   }

}
