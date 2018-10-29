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


package com.execue.platform.swi.operation.synchronization.impl;

import static com.execue.platform.swi.operation.AssetOperationHelper.transformColumn;
import static com.execue.platform.swi.operation.AssetOperationHelper.transformColumns;
import static com.execue.platform.swi.operation.AssetOperationHelper.transformMember;
import static com.execue.platform.swi.operation.AssetOperationHelper.transformMembers;
import static com.execue.platform.swi.operation.AssetOperationHelper.transformTable;
import static com.execue.platform.swi.operation.AssetOperationHelper.transformTables;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.AssetOperationInfo;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.HistoryAssetOperationInfo;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.bean.swi.AssetSyncInfo;
import com.execue.core.common.bean.swi.AssetSynchronizationContext;
import com.execue.core.common.bean.swi.ColumnSyncInfo;
import com.execue.core.common.bean.swi.ColumnUpdationInfo;
import com.execue.core.common.bean.swi.MemberSyncInfo;
import com.execue.core.common.bean.swi.MemberUpdationInfo;
import com.execue.core.common.bean.swi.TableSyncInfo;
import com.execue.core.common.bean.swi.TableUpdationInfo;
import com.execue.core.common.type.AssetOperationType;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.OperationEnum;
import com.execue.core.common.type.SyncRequestLevel;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.exception.AssetSynchronizationException;
import com.execue.platform.exception.SourceMetaDataException;
import com.execue.platform.swi.ISourceMetaDataService;
import com.execue.platform.swi.operation.AssetOperationHelper;
import com.execue.platform.swi.operation.synchronization.IAssetSyncPopulateService;
import com.execue.swi.exception.AssetOperationExceptionCodes;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXDeletionService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * This service populates the asset sync object by finding the differences with source
 * 
 * @author Vishay
 * @version 1.0
 * @since 20/08/09
 */
public class AssetSyncPopulateServiceImpl implements IAssetSyncPopulateService {

   private ISDXRetrievalService      sdxRetrievalService;
   private ISDXManagementService     sdxManagementService;
   private ISDXDeletionService       sdxDeletionService;
   private ISourceMetaDataService    sourceMetaDataService;
   private IMappingRetrievalService  mappingRetrievalService;
   private ICoreConfigurationService coreConfigurationService;
   private static final Logger       logger = Logger.getLogger(AssetSyncPopulateServiceImpl.class);

   // This method will populate the asset sync object by comparing it with source. if we don't find any changes return
   // false, record has to go in the database with status as no change found .For each asset only the latest entry
   // should be there, so before insertion, move the old record to history if it exists
   public AssetSyncInfo populateAssetSyncInfo (AssetSynchronizationContext assetSynchronizationContext) throws AssetSynchronizationException {
      // for the asset, get the table info from swi
      // try to find all the tables
      // fill the sync object if some are deleted
      // try to find the difference between table at source and table at swi(table updation)
      // once tables are done, for each table, find the columns deleted, added or updated
      // once columns are done, for each table, for each column find the members deleted, added or updated
      // after that put the AssetSync Object in asset synchronization table, before that check if row exists for the
      // same asset id, move to history table and then insert a record
      
      Long assetId = assetSynchronizationContext.getAssetId();
      Long userId = assetSynchronizationContext.getUserId();
      
      AssetSyncInfo assetSyncInfo = new AssetSyncInfo();
      assetSyncInfo.setAssetId(assetId);
      Date startDate = new Date();
      boolean isSyncChangedFound = false;
      try {
         Asset asset = getSdxRetrievalService().getAsset(assetId);
         // TODO :-VG- virtual table need not be processed but any change in the parent table needs to be tracked and
         // then corresponding action needs to be taken
         List<TableInfo> swiAssetTablesInfo = getSdxRetrievalService().getAssetTablesExcludingVirtual(asset);
         List<Tabl> sourceAssetTables = sourceMetaDataService.getTablesFromSource(asset.getDataSource(), userId);
         List<TableInfo> sourceAssetTablesInfo = new ArrayList<TableInfo>();
         // as we are interested in only table deletion and updation, not addition of tables at source, so we will put
         // the details of only the tables which are existing in swi
         for (TableInfo swiTableInfo : swiAssetTablesInfo) {
            Tabl sourceMatchedTable = getMatchedTableByName(swiTableInfo.getTable(), sourceAssetTables);
            if (sourceMatchedTable != null) {
               sourceMatchedTable.setLookupType(swiTableInfo.getTable().getLookupType());
               sourceMatchedTable.setLookupValueColumn(swiTableInfo.getTable().getLookupValueColumn());
               sourceMatchedTable.setLookupDescColumn(swiTableInfo.getTable().getLookupDescColumn());
               sourceMatchedTable.setLowerLimitColumn(swiTableInfo.getTable().getLowerLimitColumn());
               sourceMatchedTable.setUpperLimitColumn(swiTableInfo.getTable().getUpperLimitColumn());
               try {
                  TableInfo sourceTableInfo = sourceMetaDataService.getTableFromSource(asset, sourceMatchedTable);
                  sourceAssetTablesInfo.add(sourceTableInfo);
               } catch (SourceMetaDataException s) {
                  // if lookup column is deleted, member retrieval will throw an exception, we will catch and continue
               }
            }
         }
         
         TableSyncInfo tableSyncInfo = null;
         List<ColumnSyncInfo> columnSyncInfo = null;
         List<MemberSyncInfo> memberSyncInfo = null;
         
         if (SyncRequestLevel.TABLE.equals(assetSynchronizationContext.getSyncRequestLevel())) {
            // populate table sync info
            tableSyncInfo = populateTableSyncInfo(swiAssetTablesInfo, sourceAssetTablesInfo);
         }
         if (SyncRequestLevel.COLUMN.equals(assetSynchronizationContext.getSyncRequestLevel())) {
            // populate column sync info
            columnSyncInfo = populateColumnSyncInfo(swiAssetTablesInfo, sourceAssetTablesInfo);
         }
         if (SyncRequestLevel.MEMBER.equals(assetSynchronizationContext.getSyncRequestLevel())) {
            // populate member sync info
            memberSyncInfo = populateMemberSyncInfo(swiAssetTablesInfo, sourceAssetTablesInfo);
         }

         Date completionDate = new Date();
         if (tableSyncInfo != null || ExecueCoreUtil.isCollectionNotEmpty(columnSyncInfo)
                  || ExecueCoreUtil.isCollectionNotEmpty(memberSyncInfo)) {
            if (tableSyncInfo != null) {
               assetSyncInfo.setTableSyncInfo(tableSyncInfo);
            }
            if (ExecueCoreUtil.isCollectionNotEmpty(columnSyncInfo)) {
               assetSyncInfo.setColumnSyncInfo(columnSyncInfo);
            }
            if (ExecueCoreUtil.isCollectionNotEmpty(memberSyncInfo)) {
               assetSyncInfo.setMemberSyncInfo(memberSyncInfo);
            }
            isSyncChangedFound = true;
         }
         
         assetSyncInfo.setSyncChangedFound(isSyncChangedFound);
         
         // TODO:-VG- need to see if service is called with job or interactively based on jobId.
         OperationEnum operation = OperationEnum.INTERACTIVE;
         String assetOperationData = AssetOperationHelper.convertAssetSyncInfoToXML(assetSyncInfo);
         AssetOperationInfo assetOperationInfo = AssetOperationHelper.populateAssetOperationInfo(assetId, startDate,
                  completionDate, assetOperationData, isSyncChangedFound, CheckType.NO, operation,
                  AssetOperationType.ASSET_SYNCHRONIZATION);

         AssetOperationInfo existingAssetOperation = null;
         try {
            existingAssetOperation = getSdxRetrievalService().getAssetOperationByAssetId(assetId,
                     AssetOperationType.ASSET_SYNCHRONIZATION);
         } catch (SDXException sdxException) {
            // There might not be an entry existing for the given asset in Sync Info Table
            if (AssetOperationExceptionCodes.ASSET_OPERATION_ENTRY_NOTFOUND_BY_ASSET_ID != sdxException.getCode()) {
               throw sdxException;
            }
         }

         if (existingAssetOperation != null) {
            // move the record to history table
            // delete the existing record
            HistoryAssetOperationInfo historyAssetOperationInfo = AssetOperationHelper
                     .populateHistoryAssetOperationInfo(existingAssetOperation);
            getSdxManagementService().createHistoryAssetOperation(historyAssetOperationInfo);
            getSdxDeletionService().deleteAssetOperation(existingAssetOperation);
         }
         getSdxManagementService().createAssetOperation(assetOperationInfo);
      } catch (SDXException e) {
         e.printStackTrace();
      } catch (SourceMetaDataException e) {
         e.printStackTrace();
      }
      return assetSyncInfo;
   }

   private List<MemberSyncInfo> populateMemberSyncInfo (List<TableInfo> swiAssetTablesInfo,
            List<TableInfo> sourceAssetTablesInfo) {
      List<MemberSyncInfo> membersSyncInfo = new ArrayList<MemberSyncInfo>();
      for (TableInfo swiTabInfo : swiAssetTablesInfo) {
         if (swiTabInfo.getTable().getLookupType() != null) {
            TableInfo sourceMatchedTableInfo = getMatchedTableInfoByName(swiTabInfo, sourceAssetTablesInfo);
            if (sourceMatchedTableInfo != null) {
               List<Membr> swiMembers = swiTabInfo.getMembers();
               List<Membr> srcMembers = sourceMatchedTableInfo.getMembers();
               // find List of deleted members
               List<Membr> deletedMembers = new ArrayList<Membr>();
               for (Membr swiMembr : swiMembers) {
                  // if member is exist in swi but not in warehouse that means member got deleted from the warehouse
                  Membr matchedMembr = getMatchedMemberByLookupValue(swiMembr, srcMembers);
                  if (matchedMembr == null) {
                     logger.debug("Deleted Member " + swiMembr.getLookupValue());
                     deletedMembers.add(swiMembr);
                  }
               }
               // find List of new added members
               List<Membr> addedMembers = new ArrayList<Membr>();
               for (Membr srcMembr : srcMembers) {
                  // if member is not exist in swi but exist in warehouse that means new member got added
                  Membr matchedMembr = getMatchedMemberByLookupValue(srcMembr, swiMembers);
                  if (matchedMembr == null) {
                     logger.debug("Added Member " + srcMembr.getLookupValue());
                     addedMembers.add(srcMembr);
                  }
               }
               // find List of updated member
               List<MemberUpdationInfo> updatedMembers = new ArrayList<MemberUpdationInfo>();
               for (Membr swiMember : swiMembers) {
                  Membr matchedMember = getMatchedMemberByLookupValue(swiMember, srcMembers);
                  if (matchedMember != null) {
                     if (!areStringsSame(matchedMember.getLookupDescription(), swiMember.getOriginalDescription())) {
                        logger.debug("Updated Member swi " + swiMember.getLookupValue());
                        logger.debug("Updated Member source " + matchedMember.getLookupValue());
                        MemberUpdationInfo memberUpdationInfo = new MemberUpdationInfo();
                        memberUpdationInfo.setDescriptionChanged(true);
                        memberUpdationInfo.setSourceMember(transformMember(matchedMember));
                        memberUpdationInfo.setSwiMember(transformMember(swiMember));
                        updatedMembers.add(memberUpdationInfo);
                     }
                  }
               }
               // prepare object for updated member information
               if (ExecueCoreUtil.isCollectionNotEmpty(addedMembers)
                        || ExecueCoreUtil.isCollectionNotEmpty(deletedMembers)
                        || ExecueCoreUtil.isCollectionNotEmpty(updatedMembers)) {
                  MemberSyncInfo memSyncInfo = new MemberSyncInfo();
                  if (ExecueCoreUtil.isCollectionNotEmpty(addedMembers)) {
                     memSyncInfo.setAddedMembers(transformMembers(addedMembers));
                  }
                  if (ExecueCoreUtil.isCollectionNotEmpty(deletedMembers)) {
                     memSyncInfo.setDeletedMembers(transformMembers(deletedMembers));
                  }
                  if (ExecueCoreUtil.isCollectionNotEmpty(updatedMembers)) {
                     memSyncInfo.setUpdatedMembers(updatedMembers);
                  }
                  memSyncInfo.setColum(transformColumn(getMatchedLookupValueColumn(swiTabInfo, swiTabInfo.getTable()
                           .getLookupValueColumn())));
                  memSyncInfo.setTabl(transformTable(swiTabInfo.getTable()));
                  membersSyncInfo.add(memSyncInfo);
               }
            }
         }
      }
      return membersSyncInfo;
   }

   private List<ColumnSyncInfo> populateColumnSyncInfo (List<TableInfo> swiAssetTablesInfo,
            List<TableInfo> sourceAssetTablesInfo) {
      List<ColumnSyncInfo> columnSyncInfo = new ArrayList<ColumnSyncInfo>();
      for (TableInfo swiTabInfo : swiAssetTablesInfo) {
         TableInfo matchedTableInfo = getMatchedTableInfo(swiTabInfo, sourceAssetTablesInfo);
         if (matchedTableInfo != null) {
            List<Colum> swiTableColumns = swiTabInfo.getColumns();
            List<Colum> srcTableColumns = matchedTableInfo.getColumns();
            // find list of deleted columns from swi
            List<Colum> deletedColumns = new ArrayList<Colum>();
            for (Colum swiTableColumn : swiTableColumns) {
               Colum existingMatchedColumn = getExistingMatchedColumn(swiTableColumn, srcTableColumns);
               if (existingMatchedColumn == null) {
                  logger.debug("deleted Column:" + swiTableColumn.getName());
                  deletedColumns.add(swiTableColumn);
               }
            }
            // get the list of new added columns in warehouse
            List<Colum> addedColumns = new ArrayList<Colum>();
            for (Colum srcTableColumn : srcTableColumns) {
               Colum existingMatchedColumn = getExistingMatchedColumn(srcTableColumn, swiTableColumns);
               if (existingMatchedColumn == null) {
                  logger.debug("Added Column:" + srcTableColumn.getName());
                  addedColumns.add(srcTableColumn);
               }
            }
            // get The list of updated columns
            List<ColumnUpdationInfo> updatedColumns = new ArrayList<ColumnUpdationInfo>();
            for (Colum swiTableColumn : swiTableColumns) {
               Colum existingMatchedColumn = getExistingMatchedColumn(swiTableColumn, srcTableColumns);
               if (existingMatchedColumn != null) {
                  ColumnUpdationInfo updatedColumn = getUpdatedColumnInfo(swiTableColumn, existingMatchedColumn);
                  if (updatedColumn != null) {
                     logger.debug("updated column");
                     logger.debug("swiColumn:" + updatedColumn.getSwiColumn().getName());
                     logger.debug("srcColumn:" + updatedColumn.getSourceColumn().getName());
                     logger.debug("DescriptionUpdated:" + updatedColumn.isDescriptionUpdated());
                     logger.debug("DataTypeUpdated:" + updatedColumn.isDataTypeUpdated());
                     logger.debug("PrecisionUpdated:" + updatedColumn.isPrecisionUpdated());
                     logger.debug("ScaleUpdated:" + updatedColumn.isScaleUpdated());
                     logger.debug("RequiredUpdated:" + updatedColumn.isRequiredUpdated());
                     logger.debug("DefaultValueUpdated:" + updatedColumn.isDefaultValueUpdated());
                     logger.debug("ConstraintUpdated:" + updatedColumn.isConstraintInfoUpdated());
                     updatedColumns.add(updatedColumn);
                  }
               }
            }
            if (ExecueCoreUtil.isCollectionNotEmpty(deletedColumns)
                     || ExecueCoreUtil.isCollectionNotEmpty(addedColumns)
                     || ExecueCoreUtil.isCollectionNotEmpty(updatedColumns)) {
               ColumnSyncInfo columnSyncInfoReference = new ColumnSyncInfo();
               if (ExecueCoreUtil.isCollectionNotEmpty(addedColumns)) {
                  columnSyncInfoReference.setAddedColumns(transformColumns(addedColumns));
               }
               if (ExecueCoreUtil.isCollectionNotEmpty(deletedColumns)) {
                  columnSyncInfoReference.setDeletedColumns(transformColumns(deletedColumns));
               }
               if (ExecueCoreUtil.isCollectionNotEmpty(updatedColumns)) {
                  columnSyncInfoReference.setUpdatedColumns(updatedColumns);
               }
               columnSyncInfoReference.setTabl(transformTable(swiTabInfo.getTable()));
               columnSyncInfo.add(columnSyncInfoReference);
            }
         }
      }
      return columnSyncInfo;
   }

   private ColumnUpdationInfo getUpdatedColumnInfo (Colum swiColumnToBeMatched, Colum srcColumnToBeMatched) {
      ColumnUpdationInfo columnUpdationInfo = null;
      boolean isDescriptionUpdated = false;
      boolean isDataTypeUpdated = false;
      boolean isPrecisionUpdated = false;
      boolean isScaleUpdated = false;
      boolean isRequiredUpdated = false;
      boolean isDefaultValueUpdated = false;
      boolean isConstraintInfoUpdated = false;

      if (!areStringsSame(swiColumnToBeMatched.getDescription(), srcColumnToBeMatched.getDescription())) {
         isDescriptionUpdated = true;
      }
      if (!swiColumnToBeMatched.getDataType().equals(srcColumnToBeMatched.getDataType())) {
         isDataTypeUpdated = true;
      }
      if (swiColumnToBeMatched.getPrecision() != srcColumnToBeMatched.getPrecision()) {
         isPrecisionUpdated = true;
      }
      if (swiColumnToBeMatched.getScale() != srcColumnToBeMatched.getScale()) {
         isDataTypeUpdated = true;
      }
      if (!swiColumnToBeMatched.getRequired().equals(srcColumnToBeMatched.getRequired())) {
         isRequiredUpdated = true;
      }
      if (!areStringsSame(swiColumnToBeMatched.getDefaultValue(), srcColumnToBeMatched.getDefaultValue())) {
         isDefaultValueUpdated = true;
      }
      if (swiColumnToBeMatched.getConstraints() != null && srcColumnToBeMatched.getConstraints() != null
               && swiColumnToBeMatched.getConstraints().size() != srcColumnToBeMatched.getConstraints().size()) {
         // TODO: need to revisit this logic
         isConstraintInfoUpdated = true;
      }
      if (isDescriptionUpdated || isDataTypeUpdated || isPrecisionUpdated || isScaleUpdated || isRequiredUpdated
               || isDefaultValueUpdated || isConstraintInfoUpdated) {
         columnUpdationInfo = new ColumnUpdationInfo();
         columnUpdationInfo.setDescriptionUpdated(isDescriptionUpdated);
         columnUpdationInfo.setDataTypeUpdated(isDataTypeUpdated);
         columnUpdationInfo.setPrecisionUpdated(isPrecisionUpdated);
         columnUpdationInfo.setScaleUpdated(isScaleUpdated);
         columnUpdationInfo.setRequiredUpdated(isRequiredUpdated);
         columnUpdationInfo.setConstraintInfoUpdated(isConstraintInfoUpdated);
         columnUpdationInfo.setSourceColumn(transformColumn(srcColumnToBeMatched));
         columnUpdationInfo.setSwiColumn(transformColumn(swiColumnToBeMatched));
      }

      return columnUpdationInfo;
   }

   private boolean areStringsSame (String strToCompareOne, String strToCompareTwo) {
      boolean isStringSame = false;
      if (strToCompareOne == null && strToCompareTwo == null) {
         isStringSame = true;
      } else if (strToCompareOne != null && strToCompareTwo == null) {
         isStringSame = false;
      } else if (strToCompareOne == null && strToCompareTwo != null) {
         isStringSame = false;
      } else if (strToCompareOne != null && strToCompareTwo != null) {
         if (strToCompareOne.trim().equalsIgnoreCase(strToCompareTwo.trim())) {
            isStringSame = true;
         }
      }
      return isStringSame;
   }

   private TableInfo getMatchedTableInfo (TableInfo tableInfoToCompare, List<TableInfo> tablesInfoToCompare) {
      TableInfo tableFound = null;
      for (TableInfo tableInfoToComparePerInteration : tablesInfoToCompare) {
         if (tableInfoToComparePerInteration.getTable().getName().equals(tableInfoToCompare.getTable().getName())) {
            tableFound = tableInfoToComparePerInteration;
            break;
         }
      }
      return tableFound;
   }

   private Colum getExistingMatchedColumn (Colum columnToBeMatched, List<Colum> columnsToBeMatch) {
      Colum matchedColumn = null;
      for (Colum column : columnsToBeMatch) {
         if (columnToBeMatched.getName().equalsIgnoreCase(column.getName())) {
            matchedColumn = column;
            break;
         }
      }
      return matchedColumn;
   }

   private TableSyncInfo populateTableSyncInfo (List<TableInfo> swiAssetTablesInfo,
            List<TableInfo> sourceAssetTablesInfo) {
      TableSyncInfo tableSyncInfo = null;
      // find deleted tables
      List<Tabl> deletedTables = new ArrayList<Tabl>();
      for (TableInfo swiTableInfo : swiAssetTablesInfo) {
         TableInfo sourceMatchedTableInfo = getMatchedTableInfoByName(swiTableInfo, sourceAssetTablesInfo);
         if (sourceMatchedTableInfo == null) {
            logger.debug("Deleted Table " + swiTableInfo.getTable().getName());
            deletedTables.add(swiTableInfo.getTable());
         }
      }
      // find updated Tables
      List<TableUpdationInfo> updatedTables = new ArrayList<TableUpdationInfo>();
      for (TableInfo swiTabInfo : swiAssetTablesInfo) {
         TableInfo matchedTableInfo = getMatchedTableInfoByName(swiTabInfo, sourceAssetTablesInfo);
         if (matchedTableInfo != null) {
            if (!areStringsSame(matchedTableInfo.getTable().getDescription(), swiTabInfo.getTable().getDescription())) {
               logger.debug("Updated Table swi " + swiTabInfo.getTable().getName());
               logger.debug("Updated Table source " + matchedTableInfo.getTable().getName());
               TableUpdationInfo tableUpdationInfo = new TableUpdationInfo();
               tableUpdationInfo.setDescriptionChanged(true);
               tableUpdationInfo.setSourceTable(transformTable(matchedTableInfo.getTable()));
               tableUpdationInfo.setSwiTable(transformTable(swiTabInfo.getTable()));
               updatedTables.add(tableUpdationInfo);
            }
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(deletedTables) || ExecueCoreUtil.isCollectionNotEmpty(updatedTables)) {
         tableSyncInfo = new TableSyncInfo();
         if (ExecueCoreUtil.isCollectionNotEmpty(deletedTables)) {
            tableSyncInfo.setDeletedTables(transformTables(deletedTables));
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(updatedTables)) {
            tableSyncInfo.setUpdatedTables(updatedTables);
         }
      }
      return tableSyncInfo;
   }

   private Tabl getMatchedTableByName (Tabl tableToBeMatched, List<Tabl> tablesToMatch) {
      Tabl matchedTable = null;
      for (Tabl tableToMatch : tablesToMatch) {
         String swiTable = tableToBeMatched.getName();
         if (CheckType.YES.equals(tableToBeMatched.getVirtual())) {
            swiTable = tableToBeMatched.getActualName();
         }
         if (swiTable.equalsIgnoreCase(tableToMatch.getName())) {
            matchedTable = tableToMatch;
            break;
         }
      }
      if (matchedTable != null) {
         matchedTable = ExecueBeanCloneUtil.cloneTable(matchedTable);
      }
      return matchedTable;
   }

   private TableInfo getMatchedTableInfoByName (TableInfo tableInfoToBeMatched, List<TableInfo> tablesInfosToMatch) {
      TableInfo matchedTableInfo = null;
      for (TableInfo tableInfoToMatch : tablesInfosToMatch) {
         Tabl tableToBeMatched = tableInfoToBeMatched.getTable();
         Tabl tableWithMatch = tableInfoToMatch.getTable();
         String tableNameToMatch = tableToBeMatched.getName();
         if (CheckType.YES.equals(tableToBeMatched.getVirtual())) {
            tableNameToMatch = tableToBeMatched.getActualName();
         }
         if (tableNameToMatch.equalsIgnoreCase(tableWithMatch.getName())) {
            matchedTableInfo = tableInfoToMatch;
            break;
         }
      }
      return matchedTableInfo;
   }

   private Membr getMatchedMemberByLookupValue (Membr memberToCompare, List<Membr> membrsToCompare) {
      Membr matchedMember = null;
      for (Membr memberToCompareIteration : membrsToCompare) {
         String lookUpValueMemberToCompare = memberToCompare.getLookupValue();
         String lookUpValueMemberToCompareIteration = memberToCompareIteration.getLookupValue();
         if (lookUpValueMemberToCompare == null) {
            if (lookUpValueMemberToCompareIteration == null) {
               matchedMember = memberToCompareIteration;
               break;
            }
         } else {
            if (lookUpValueMemberToCompare.trim().equalsIgnoreCase(lookUpValueMemberToCompareIteration.trim())) {
               matchedMember = memberToCompareIteration;
               break;
            }
         }
      }
      return matchedMember;
   }

   private Colum getMatchedLookupValueColumn (TableInfo swiTabInfo, String lookupValueColumn) {
      Colum valueColumn = null;
      for (Colum swiColum : swiTabInfo.getColumns()) {
         if (swiColum.getName().equalsIgnoreCase(lookupValueColumn)) {
            valueColumn = swiColum;
            break;
         }
      }
      return valueColumn;
   }

   @Override
   public List<Membr> getAddedMembers (Long parentAssetId, Long assetId, Long modelId, Long conceptId)
            throws AssetSynchronizationException {
      List<Membr> addedMembers = new ArrayList<Membr>();
      try {
         Mapping parentAssetMapping = getMappingRetrievalService().getPrimaryMapping(conceptId, modelId, parentAssetId);
         Mapping childAssetMapping = getMappingRetrievalService().getPrimaryMapping(conceptId, modelId, assetId);
         if (parentAssetMapping != null && childAssetMapping != null) {
            AssetEntityDefinition parentAssetAED = parentAssetMapping.getAssetEntityDefinition();
            List<Membr> parentAssetMembers = getSdxRetrievalService().getColumnMembers(parentAssetAED.getColum());
            if (ExecueCoreUtil.isCollectionNotEmpty(parentAssetMembers)) {
               AssetEntityDefinition childAssetAED = childAssetMapping.getAssetEntityDefinition();
               List<Membr> childAssetMembers = getSdxRetrievalService().getColumnMembers(childAssetAED.getColum());
               // if asset is of type cube, then remove cube all value member
               removeCubeAllValueMember(assetId, childAssetMembers);
               for (Membr parentAssetMember : parentAssetMembers) {
                  boolean isMemberAlreadyExists = false;
                  for (Membr childAssetMember : childAssetMembers) {
                     if (parentAssetMember.getLookupValue().equalsIgnoreCase(childAssetMember.getLookupValue())) {
                        isMemberAlreadyExists = true;
                        break;
                     }
                  }
                  if (!isMemberAlreadyExists) {
                     addedMembers.add(parentAssetMember);
                  }
               }
            }
         }
      } catch (MappingException e) {
         throw new AssetSynchronizationException(e.getCode(), e);
      } catch (SDXException e) {
         throw new AssetSynchronizationException(e.getCode(), e);
      }
      return addedMembers;
   }

   private void removeCubeAllValueMember (Long assetId, List<Membr> childAssetMembers) throws SDXException {
      Asset asset = getSdxRetrievalService().getAssetById(assetId);
      if (AssetType.Cube.equals(asset.getType()) && ExecueCoreUtil.isCollectionNotEmpty(childAssetMembers)) {
         Membr allValueMember = null;
         for (Membr membr : childAssetMembers) {
            String cubeAllValue = getCoreConfigurationService().getCubeAllValue();
            if (membr.getLookupValue().equalsIgnoreCase(cubeAllValue)) {
               allValueMember = membr;
               break;
            }
         }
         if (allValueMember != null) {
            childAssetMembers.remove(allValueMember);
         }
      }
   }

   @Override
   public List<Membr> getDeletedMembers (Long parentAssetId, Long assetId, Long modelId, Long conceptId)
            throws AssetSynchronizationException {
      List<Membr> deletedMembers = new ArrayList<Membr>();
      try {
         Mapping childAssetMapping = getMappingRetrievalService().getPrimaryMapping(conceptId, modelId, assetId);
         Mapping parentAssetMapping = getMappingRetrievalService().getPrimaryMapping(conceptId, modelId, parentAssetId);
         if (parentAssetMapping != null && childAssetMapping != null) {
            AssetEntityDefinition childAssetAED = childAssetMapping.getAssetEntityDefinition();
            List<Membr> childAssetMembers = getSdxRetrievalService().getColumnMembers(childAssetAED.getColum());
            if (ExecueCoreUtil.isCollectionNotEmpty(childAssetMembers)) {
               removeCubeAllValueMember(assetId, childAssetMembers);
               AssetEntityDefinition parentAssetAED = parentAssetMapping.getAssetEntityDefinition();
               List<Membr> parentAssetMembers = getSdxRetrievalService().getColumnMembers(parentAssetAED.getColum());
               for (Membr childAssetMember : childAssetMembers) {
                  boolean isMemberAlreadyExists = false;
                  for (Membr parentAssetMember : parentAssetMembers) {
                     if (childAssetMember.getLookupValue().equalsIgnoreCase(parentAssetMember.getLookupValue())) {
                        isMemberAlreadyExists = true;
                        break;
                     }
                  }
                  if (!isMemberAlreadyExists) {
                     deletedMembers.add(childAssetMember);
                  }
               }
            }
         }
      } catch (MappingException e) {
         throw new AssetSynchronizationException(e.getCode(), e);
      } catch (SDXException e) {
         throw new AssetSynchronizationException(e.getCode(), e);
      }
      return deletedMembers;
   }

   public ISourceMetaDataService getSourceMetaDataService () {
      return sourceMetaDataService;
   }

   public void setSourceMetaDataService (ISourceMetaDataService sourceMetaDataService) {
      this.sourceMetaDataService = sourceMetaDataService;
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

   public ISDXDeletionService getSdxDeletionService () {
      return sdxDeletionService;
   }

   public void setSdxDeletionService (ISDXDeletionService sdxDeletionService) {
      this.sdxDeletionService = sdxDeletionService;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}
