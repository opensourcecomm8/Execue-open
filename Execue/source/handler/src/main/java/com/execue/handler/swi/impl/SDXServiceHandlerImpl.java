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


package com.execue.handler.swi.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.ac.AnswersCatalogManagementQueue;
import com.execue.core.common.bean.ac.AssetOperationDetail;
import com.execue.core.common.bean.batchMaintenance.MemberAbsorptionContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.AssetGrainInfo;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.DefaultMetric;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.bean.swi.AssetDeletionContext;
import com.execue.core.common.type.ACManagementOperationSourceType;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.core.common.type.AnswersCatalogOperationType;
import com.execue.core.common.type.AssetGrainType;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.DataSourceType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.PublishedFileType;
import com.execue.core.common.util.AssetGrainUtils;
import com.execue.core.common.util.ExeCueXMLUtils;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIAssetGrain;
import com.execue.handler.bean.UIAssetGrainInfo;
import com.execue.handler.bean.UIColumn;
import com.execue.handler.bean.UIMember;
import com.execue.handler.bean.UITable;
import com.execue.handler.comparator.UITableNameComparator;
import com.execue.handler.swi.ISDXServiceHandler;
import com.execue.handler.transformer.TableTransformer;
import com.execue.platform.IVirtualTableManagementService;
import com.execue.platform.helper.AnswersCatalogPlatformServiceHelper;
import com.execue.platform.swi.IAssetSourcePublisherMergeService;
import com.execue.platform.swi.IConstraintPopulationService;
import com.execue.platform.swi.ISDXDeletionWrapperService;
import com.execue.platform.swi.ISourceMetaDataService;
import com.execue.qdata.service.IAnswersCatalogManagementQueueService;
import com.execue.scheduler.service.IAssetDeletionJobService;
import com.execue.scheduler.service.IMemberAbsorptionJobService;
import com.execue.security.UserContextService;
import com.execue.swi.exception.BatchMaintenanceException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IAssetOperationDetailService;
import com.execue.swi.service.IBatchMaintenanceService;
import com.execue.swi.service.IDataSourceConnectionManagementService;
import com.execue.swi.service.IDataSourceSelectionService;
import com.execue.swi.service.IDefaultMetricService;
import com.execue.swi.service.IJoinService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingDeletionService;
import com.execue.swi.service.IMappingManagementService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXDeletionService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.util.cryptography.ICryptographyService;
import com.execue.util.cryptography.exception.CryptographyException;
import com.execue.util.cryptography.type.EncryptionAlgorithm;

public class SDXServiceHandlerImpl extends UserContextService implements ISDXServiceHandler {

   private ISDXRetrievalService                   sdxRetrievalService;
   private IKDXRetrievalService                   kdxRetrievalService;
   private IJoinService                           joinService;
   private ISDXManagementService                  sdxManagementService;
   private IDataSourceConnectionManagementService dataSourceConnectionManagementService;
   private IAssetDeletionJobService               assetDeletionJobService;
   private ISDXDeletionService                    sdxDeletionService;
   private IMemberAbsorptionJobService            memberAbsorptionJobService;
   private IBatchMaintenanceService               batchMaintenanceService;
   private IDataSourceSelectionService            dataSourceSelectionService;
   private IAssetSourcePublisherMergeService      assetSourcePublisherMergeService;
   private IDefaultMetricService                  defaultMetricService;
   private IVirtualTableManagementService         virtualTableManagementService;
   private IMappingRetrievalService               mappingRetrievalService;
   private IMappingManagementService              mappingManagementService;
   private IMappingDeletionService                mappingDeletionService;
   private ISourceMetaDataService                 sourceMetaDataService;
   private IConstraintPopulationService           constraintPopulationService;
   private ICryptographyService                   cryptographyService;
   private IAssetOperationDetailService           assetOperationDetailService;
   private ICoreConfigurationService              coreConfigurationService;
   private IAnswersCatalogManagementQueueService  answersCatalogManagementQueueService;
   private ISDXDeletionWrapperService             sdxDeletionWrapperService;

   private static String                          NOT_VALID_DATASOURCE = "NotValidDataSource";
   private static String                          PARENT_ASSET         = "Parent Dataset Collection";
   private static String                          CHILD_ASSETS         = "Child Dataset Collection(s)";

   private static final Logger                    logger               = Logger.getLogger(SDXServiceHandlerImpl.class);

   public Asset getAsset (Long applicationId, String assetName) throws ExeCueException {
      return getSdxRetrievalService().getAsset(applicationId, assetName);
   }

   public void updateAssetTables (Asset asset, List<TableInfo> tablesInfo) throws ExeCueException {
      getSdxManagementService().updateAssetTables(asset, tablesInfo);
      for (TableInfo tableInfo : tablesInfo) {
         defaultMetricService.handleAssetDefaultMetrics(asset, tableInfo.getTable(), tableInfo.getColumns(), true);
      }
   }

   public void updateColumn (Long assetId, Long tableId, Colum column) throws ExeCueException {
      getSdxManagementService().updateColumn(assetId, tableId, column);
   }

   public void createAssetTables (Asset asset, List<TableInfo> tablesInfo) throws ExeCueException {
      getSdxManagementService().createAssetTables(asset, tablesInfo);
      for (TableInfo tableInfo : tablesInfo) {
         defaultMetricService.handleAssetDefaultMetrics(asset, tableInfo.getTable(), tableInfo.getColumns(), false);
      }
   }

   public List<Asset> getAllAssets (Long applicationId) throws ExeCueException {
      return getSdxRetrievalService().getAllAssets(applicationId);
   }

   public void createDataSource (DataSource dataSource) throws ExeCueException {
      User user = getUserContext().getUser();
      dataSource.setName(dataSource.getDisplayName() + user.getId());
      dataSource.setPassword(getEncryptedPassword(dataSource.getPassword()));
      if (!getDataSourceConnectionManagementService().doesConnectionExist(dataSource)) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, NOT_VALID_DATASOURCE);
      }
      Set<User> users = new HashSet<User>();
      users.add(user);
      dataSource.setUsers(users);
      getSdxManagementService().createDataSource(dataSource);
      getDataSourceConnectionManagementService().setupDataSource(dataSource);
   }

   public List<DataSource> getDisplayableDataSources () throws ExeCueException {
      List<DataSource> dataSources = null;
      try {
         if ("ROLE_ADMIN".equalsIgnoreCase(getUserContext().getRole())) {
            dataSources = getSdxRetrievalService().getAllPublicDataSources();
         } else {
            dataSources = getSdxRetrievalService().getDisplayableDataSources(true, getUserContext().getUser().getId());
         }
      } catch (SDXException sdxException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, sdxException);
      }
      return dataSources;
   }

   public DataSource getDataSource (String dataSourceName) throws ExeCueException {
      return getSdxRetrievalService().getDataSource(dataSourceName);
   }

   public void updateDataSource (DataSource dataSource) throws ExeCueException {
      DataSource swiDataSource = getSdxRetrievalService().getDataSourceById(dataSource.getId());
      if (!"*****".equals(dataSource.getPassword()) && !swiDataSource.getPassword().equals(dataSource.getPassword())) {
         dataSource.setPassword(getEncryptedPassword(dataSource.getPassword()));
         dataSource.setPasswordEncrypted(CheckType.YES);
      } else {
         dataSource.setPassword(swiDataSource.getPassword());
      }
      if (!getDataSourceConnectionManagementService().doesConnectionExist(dataSource)) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, NOT_VALID_DATASOURCE);
      }

      User user = getUserContext().getUser();
      Set<User> users = new HashSet<User>();
      users.add(user);
      dataSource.setUsers(users);
      getSdxManagementService().updateDataSource(dataSource);
      getDataSourceConnectionManagementService().removeDataSource(swiDataSource);
      getDataSourceConnectionManagementService().setupDataSource(dataSource);
   }

   @Override
   public DataSource encryptDataSourceCredential (DataSource dataSource) throws ExeCueException {
      Long dataSourceId = dataSource.getId();
      dataSource = getSdxRetrievalService().getDataSourceById(dataSourceId);
      if (dataSource != null) {
         dataSource.setPassword(getEncryptedPassword(dataSource.getPassword()));
         dataSource.setPasswordEncrypted(CheckType.YES);
         getSdxManagementService().updateDataSource(dataSource);

      }
      return dataSource;
   }

   public List<TableInfo> getAssetTables (Asset asset) throws ExeCueException {
      return getSdxRetrievalService().getAssetTables(asset);
   }

   public void createAsset (Asset asset) throws ExeCueException {
      DataSource dataSource = getSdxRetrievalService().getDataSourceById(asset.getDataSource().getId());
      // TODO : Need to handle other file types as well

      if (DataSourceType.UPLOADED.equals(dataSource.getType())) {
         asset.setOriginType(PublishedFileType.CSV);
      }
      getSdxManagementService().createAsset(asset);
   }

   public void updateAsset (Asset asset) throws ExeCueException {
      getSdxManagementService().updateAsset(asset);
   }

   public void deleteDataSource (DataSource dataSource) throws ExeCueException {
      getSdxDeletionService().deleteDataSource(dataSource);
      getDataSourceConnectionManagementService().removeDataSource(dataSource);
   }

   public List<UITable> getAllAssetTables (Asset asset) throws ExeCueException {
      List<Tabl> assetTables = getSdxRetrievalService().getAllTables(asset);
      return getTransformedUITables(assetTables);
   }

   @Override
   public List<UIMember> getAssetTableMembersByPage (Long assetId, Long tableId, Page page) throws HandlerException {
      List<UIMember> membersByPage = new ArrayList<UIMember>();
      try {
         List<Membr> assetTableMembers = getSdxRetrievalService().getAssetTableMembersByPage(assetId, tableId, page);
         if (ExecueCoreUtil.isCollectionNotEmpty(assetTableMembers)) {
            for (Membr membr : assetTableMembers) {
               membersByPage.add(transformIntoUIMember(membr));
            }
         }
      } catch (SDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
      return membersByPage;
   }

   private UIMember transformIntoUIMember (Membr membr) {
      UIMember uiMembr = new UIMember();
      uiMembr.setId(membr.getId());
      uiMembr.setName(membr.getLookupValue());
      uiMembr.setDescription(membr.getLookupDescription());
      return uiMembr;

   }

   private void populateVirtualTableProperties (Long assetId, Tabl assetTable) throws SDXException {
      if (CheckType.YES.equals(assetTable.getVirtual())) {
         Tabl actualTable = getSdxRetrievalService().getAssetTable(assetId, assetTable.getActualName());
         assetTable.setActualTableDisplayName(actualTable.getDisplayName());
      }
   }

   public List<UITable> getAllAssetFactTables (Asset asset) throws ExeCueException {
      List<Tabl> assetTables = getSdxRetrievalService().getFactTables(asset.getId());
      return getTransformedUITables(assetTables);
   }

   public List<UIColumn> getUIColumnsOFTable (UITable uiTable) throws ExeCueException {
      TableTransformer tableTransformer = new TableTransformer();
      Tabl tabl = new Tabl();
      tableTransformer.transformToDomainObject(uiTable, tabl);
      List<Colum> columns = getSdxRetrievalService().getColumnsOfTable(tabl);
      List<UIColumn> uiColumns = getTransformedUIColumns(columns);
      return uiColumns;
   }

   public TableInfo getAssetTable (Asset asset, String tableName) throws ExeCueException {
      Tabl table = new Tabl();
      table.setName(tableName);
      TableInfo assetTable = getSdxRetrievalService().getAssetTable(asset, table);
      if (assetTable != null) {
         populateVirtualTableProperties(asset.getId(), assetTable.getTable());
         if (LookupType.None.equals(assetTable.getTable().getLookupType())) {
            updateColumsWithDefaultMetrics(asset, assetTable);
         }
      }
      return assetTable;
   }

   private void updateColumsWithDefaultMetrics (Asset asset, TableInfo assetTable) throws MappingException {
      List<DefaultMetric> allDefaultMetrics = getMappingRetrievalService().getAllExistingDefaultMetrics(
               assetTable.getTable().getId());
      if (ExecueCoreUtil.isCollectionNotEmpty(allDefaultMetrics)) {
         for (Colum colum : assetTable.getColumns()) {
            AssetEntityDefinition assetEntityDefinition = getSdxRetrievalService().getAssetEntityDefinitionByIds(asset,
                     assetTable.getTable(), colum, null);
            if (isColumExistingDefaultMetric(assetEntityDefinition, allDefaultMetrics)) {
               colum.setDefaultMetric(CheckType.YES);
            } else {
               colum.setDefaultMetric(CheckType.NO);
            }
         }
      }
   }

   private boolean isColumExistingDefaultMetric (AssetEntityDefinition columAED,
            List<DefaultMetric> existingDefaultMetrics) {
      boolean isExistingDefaultMetric = false;
      for (DefaultMetric defaultMetric : existingDefaultMetrics) {
         if (defaultMetric.getAedId().equals(columAED.getId())) {
            isExistingDefaultMetric = true;
            break;
         }
      }
      return isExistingDefaultMetric;
   }

   public TableInfo getTableInfo (Asset asset, String tableName, String description, String owner,
            boolean isVirtualTable) throws ExeCueException {
      TableInfo tableInfo = null;
      Tabl table = new Tabl();
      table.setName(tableName);
      table.setDescription(description);
      table.setOwner(owner);
      if (isVirtualTable) {
         tableInfo = getSourceTable(asset, table);
      } else {
         tableInfo = getAssetTable(asset, tableName);
         if (tableInfo == null) {
            tableInfo = getSourceTable(asset, table);
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(tableInfo.getColumns())) {
         tableInfo.setColumns(sortColumnsByKDXDataType(tableInfo.getColumns()));
      }
      return tableInfo;
   }

   private List<Colum> sortColumnsByKDXDataType (List<Colum> columns) {
      List<Colum> sortedColumns = new ArrayList<Colum>();
      List<Colum> nullColumns = getColumnsByKDXDataType(ColumnType.NULL, columns);
      List<Colum> idColumns = getColumnsByKDXDataType(ColumnType.ID, columns);
      List<Colum> dimensionColumns = getColumnsByKDXDataType(ColumnType.DIMENSION, columns);
      List<Colum> simpleLookupColumns = getColumnsByKDXDataType(ColumnType.SIMPLE_LOOKUP, columns);
      List<Colum> rangeLookupColumns = getColumnsByKDXDataType(ColumnType.RANGE_LOOKUP, columns);
      List<Colum> simpleHiearchyLookupColumns = getColumnsByKDXDataType(ColumnType.SIMPLE_HIERARCHY_LOOKUP, columns);
      List<Colum> mesureColumns = getColumnsByKDXDataType(ColumnType.MEASURE, columns);
      sortedColumns.addAll(nullColumns);
      sortedColumns.addAll(idColumns);
      sortedColumns.addAll(dimensionColumns);
      sortedColumns.addAll(simpleLookupColumns);
      sortedColumns.addAll(rangeLookupColumns);
      sortedColumns.addAll(simpleHiearchyLookupColumns);
      sortedColumns.addAll(mesureColumns);
      return sortedColumns;
   }

   private List<Colum> getColumnsByKDXDataType (ColumnType kdxDataType, List<Colum> columns) {
      List<Colum> columnsByKDXDataType = new ArrayList<Colum>();
      for (Colum colum : columns) {
         if (kdxDataType.equals(colum.getKdxDataType())) {
            columnsByKDXDataType.add(colum);
         }
      }
      return columnsByKDXDataType;
   }

   public TableInfo getSourceTable (Asset asset, Tabl table) throws ExeCueException {
      Long userId = getUserContext().getUser().getId();
      TableInfo sourceTableInfo = getSourceMetaDataService().getTableFromSource(asset, table);
      if (!asset.getOriginType().equals(PublishedFileType.RDBMS)) {
         Tabl sourceTable = sourceTableInfo.getTable();
         List<Tabl> tables = new ArrayList<Tabl>();
         tables.add(sourceTable);
         assetSourcePublisherMergeService.mergeSourcePublisherTables(tables, userId);
         assetSourcePublisherMergeService.mergeSourcePublisherColumns(asset, table, sourceTableInfo.getColumns(),
                  userId);
         sourceTableInfo.setTable(tables.get(0));
      } else {
         sourceTableInfo.getTable().setDisplayName(sourceTableInfo.getTable().getName());
      }
      return sourceTableInfo;
   }

   public List<UITable> getAllSourceTables (Asset asset) throws ExeCueException {
      Long userId = getUserContext().getUser().getId();
      List<Tabl> sourceTables = getSourceMetaDataService().getTablesFromSource(asset.getDataSource(), userId);
      if (!asset.getOriginType().equals(PublishedFileType.RDBMS)) {
         DataSource uploadedDataSource = getDataSourceSelectionService().getDataSourceForUploadedDatasets(
                  getUserContext().getUser().getId());
         List<Tabl> tablesFromSource = getSourceMetaDataService().getTablesFromSource(uploadedDataSource, userId);
         assetSourcePublisherMergeService.mergeSourcePublisherTables(tablesFromSource, userId);
         sourceTables.addAll(tablesFromSource);
      }
      return getTransformedUITables(sourceTables);
   }

   public List<Membr> getMembersFromSource (Asset asset, Tabl table) throws ExeCueException {
      return getSourceMetaDataService().getMembersFromSource(asset, table, null);
   }

   public Integer getMembersCountFromSource (Asset asset, Tabl table, Colum lookupColumn) throws ExeCueException {
      return getSourceMetaDataService().getMembersCount(asset, table, lookupColumn);
   }

   // Methods for getting objects from asset source - End

   private List<UITable> getTransformedUITables (List<Tabl> tables) {
      TableTransformer tableTransformer = new TableTransformer();
      List<UITable> sourceUITables = new ArrayList<UITable>();
      UITable uiTable = null;
      for (Tabl domainTable : tables) {
         uiTable = new UITable();
         tableTransformer.transformToUIObject(domainTable, uiTable);
         sourceUITables.add(uiTable);
      }
      Collections.sort(sourceUITables, new UITableNameComparator());
      return sourceUITables;
   }

   private List<UIColumn> getTransformedUIColumns (List<Colum> columns) {
      TableTransformer tableTransformer = new TableTransformer();
      List<UIColumn> uiColumns = new ArrayList<UIColumn>();
      UIColumn uiColum = null;
      for (Colum colum : columns) {
         uiColum = new UIColumn();
         tableTransformer.transformToUIObject(colum, uiColum);
         uiColumns.add(uiColum);
      }
      return uiColumns;
   }

   public Asset getAsset (Long assetId) throws ExeCueException {
      return getSdxRetrievalService().getAsset(assetId);
   }

   public void createMembers (Asset asset, Tabl table, List<Colum> columns, List<Membr> members) throws ExeCueException {
      getSdxManagementService().createMembers(asset, table, columns, members);
   }

   public void deleteMembers (List<Membr> members) throws ExeCueException {
      getSdxDeletionService().deleteMembers(members);
   }

   public List<String> deleteTables (Asset asset, List<String> tableNamesForDeletion) throws ExeCueException {
      List<Tabl> tables = new ArrayList<Tabl>();
      List<String> batchMaintenanceTables = null;
      for (String tableName : tableNamesForDeletion) {
         Tabl table = getSdxRetrievalService().getAssetTable(asset.getId(), tableName);
         tables.add(table);
      }
      batchMaintenanceTables = tablesExistsUnderBatchProcess(asset.getId(), tableNamesForDeletion);
      if (ExecueCoreUtil.isCollectionEmpty(batchMaintenanceTables)) {
         getSdxDeletionWrapperService().deleteAssetTables(asset, tables);
      }
      return batchMaintenanceTables;

   }

   private List<String> tablesExistsUnderBatchProcess (Long assetId, List<String> tableNamesForDeletion)
            throws ExeCueException {
      List<Long> batchMaintenaceTableIds;
      List<String> batchMaintenanceTableNames = new ArrayList<String>();
      try {
         batchMaintenaceTableIds = batchMaintenanceService.getTablesOfAssetUnderBatchProcess(assetId);
         for (String tableName : tableNamesForDeletion) {
            Tabl table = getSdxRetrievalService().getAssetTable(assetId, tableName);
            if (batchMaintenaceTableIds.contains(table.getId())) {
               batchMaintenanceTableNames.add(table.getName());
            }
         }
         return batchMaintenanceTableNames;
      } catch (BatchMaintenanceException e) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   @Override
   public Map<String, List<Asset>> validateAssetUnderMaintenance (Long applicationId, Asset asset)
            throws HandlerException {
      Map<String, List<Asset>> assetMap = new LinkedHashMap<String, List<Asset>>();
      try {
         Long assetId = asset.getId();
         asset = getSdxRetrievalService().getAssetById(assetId);
         if (getSdxRetrievalService().isParentAsset(assetId, applicationId)) {
            //1- Check if that parent asset is under process 
            //2- Get child assets and check for each child asset if they are under process
            if (getAssetOperationDetailService().isAssetUnderMaintenance(assetId)) {
               List<Asset> parentAssets = new ArrayList<Asset>();
               parentAssets.add(asset);
               assetMap.put(PARENT_ASSET, parentAssets);
            }
            List<AssetOperationDetail> assetOperationDetails = getAssetOperationDetailService()
                     .getUnderProcessAssetOperationDetailsByParentAssetId(assetId);
            if (ExecueCoreUtil.isCollectionNotEmpty(assetOperationDetails)) {
               List<Asset> childAssetsUnderProcess = new ArrayList<Asset>();
               for (AssetOperationDetail assetOperationDetail : assetOperationDetails) {
                  Asset childAsset = null;
                  if (assetOperationDetail.getAssetId() != null) {
                     childAsset = getSdxRetrievalService().getAssetById(assetOperationDetail.getAssetId());
                  } else {
                     //Dummy Asset that indicate that asset creation is under process
                     childAsset = new Asset();
                     childAsset.setDisplayName("An Asset is under creation process");
                  }
                  childAssetsUnderProcess.add(childAsset);
               }
               if (ExecueCoreUtil.isCollectionNotEmpty(childAssetsUnderProcess)) {
                  assetMap.put(CHILD_ASSETS, childAssetsUnderProcess);
               }
            }
         } else {
            //1- Get the parent asset and check if it is under process
            //2- Check if child asset itself  under process
            Long baseAssetId = getSdxRetrievalService().getAsset(assetId).getBaseAssetId();
            if (getAssetOperationDetailService().isAssetUnderMaintenance(baseAssetId)) {
               List<Asset> parentAssets = new ArrayList<Asset>();
               parentAssets.add(getSdxRetrievalService().getAssetById(baseAssetId));
               assetMap.put(PARENT_ASSET, parentAssets);
            }
            if (getAssetOperationDetailService().isAssetUnderMaintenance(assetId)) {
               List<Asset> childAssetsExistsInAcmq = new ArrayList<Asset>();
               childAssetsExistsInAcmq.add(asset);
               assetMap.put(CHILD_ASSETS, childAssetsExistsInAcmq);
            }

         }
      } catch (SDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
      return assetMap;
   }

   public Long deleteAsset (Long applicationId, Asset asset) throws ExeCueException {
      Long jobRequestId = null;
      try {
         Long assetId = asset.getId();
         Long baseAssetId = getSdxRetrievalService().getAssetById(assetId).getBaseAssetId();
         AssetDeletionContext assetDeletionContext = new AssetDeletionContext();
         assetDeletionContext.setAssetId(asset.getId());
         assetDeletionContext.setParentAssetId(baseAssetId);
         assetDeletionContext.setUserId(getUserContext().getUser().getId());

         AnswersCatalogOperationType answersCatalogOperationType = null;
         if (asset.getType() == AssetType.Cube) {
            answersCatalogOperationType = AnswersCatalogOperationType.CUBE_DELETION;
         } else if (asset.getType() == AssetType.Mart) {
            answersCatalogOperationType = AnswersCatalogOperationType.MART_DELETION;
         } else {
            answersCatalogOperationType = AnswersCatalogOperationType.ASSET_DELETION;
         }

         // populate ACMQ context and create an entry in ACMQ
         AnswersCatalogManagementQueue answersCatalogManagementQueue = AnswersCatalogPlatformServiceHelper
                  .prepareAnswersCatalogManagementQueue(baseAssetId, assetId, null, ExeCueXMLUtils
                           .getXMLStringFromObject(assetDeletionContext), null,
                           ACManagementOperationSourceType.USER_REQUEST, ACManagementOperationStatusType.INITIATED,
                           answersCatalogOperationType);

         getAnswersCatalogManagementQueueService().createAnswersCatalogManagementQueue(answersCatalogManagementQueue);

         //Set the acmq Id into the context
         assetDeletionContext.setAnswersCatalogManagementQueueId(answersCatalogManagementQueue.getId());

         // Prepare the list of asset ids to be invalidated
         List<Long> assetIds = new ArrayList<Long>();
         if (baseAssetId == null) {
            assetIds.add(assetId);
            List<Asset> childAssets = getSdxRetrievalService().getAllChildAssets(assetId);
            if (ExecueCoreUtil.isCollectionNotEmpty(childAssets)) {
               for (Asset childAsset : childAssets) {
                  assetIds.add(childAsset.getId());
               }
            }
         } else {
            assetIds.add(assetId);
         }

         // Invoke the invalidation of the assets
         getAnswersCatalogManagementQueueService().invalidateAnswersCatalogManagementQueues(assetIds);

         //schedule job
         jobRequestId = getAssetDeletionJobService().scheduleAssetDeletionJob(assetDeletionContext);
      } catch (SDXException sdxException) {
         throw new KDXException(sdxException.getCode(), sdxException);
      }
      return jobRequestId;
   }

   public void createConstraints (Asset asset) throws ExeCueException {
      getConstraintPopulationService().createConstraintsByRelations(asset.getId());
   }

   public void createDirectJoins (Asset asset) throws ExeCueException {
      getJoinService().suggestDirectJoinsByForeignKey(asset.getId());
   }

   public UIAssetGrainInfo getAssetGrainInfo (Long modelId, Long assetId) throws ExeCueException {
      UIAssetGrainInfo uAssetGrainInfo = new UIAssetGrainInfo();
      // get existing mapping
      List<Mapping> existingMappings = getMappingRetrievalService().getAssetGrain(assetId);
      // get eligible grains
      List<UIAssetGrain> eligibleGrain = getEligibleGrain(modelId, assetId);
      // prepare UI object list for the existing grains
      List<UIAssetGrain> existingGrain = new ArrayList<UIAssetGrain>();

      if (ExecueCoreUtil.isCollectionNotEmpty(existingMappings)) {
         for (Mapping mapping : existingMappings) {
            existingGrain.add(getUIAssetGrain(mapping));
         }
      }
      uAssetGrainInfo.setEligibleGrain(eligibleGrain);
      uAssetGrainInfo.setExistingAssetGrain(existingGrain);
      // get default distribution concept
      Mapping defaultDistribution = AssetGrainUtils.getDefaultDistributionConcept(existingMappings);
      Mapping defaultPopulation = AssetGrainUtils.getDefaultPopulationConcept(existingMappings);
      if (defaultDistribution != null) {
         String defaultDistributionValue = defaultDistribution.getAssetEntityDefinition().getColum().getDefaultValue();
         UIAssetGrain defaultDistributionGrain = getUIAssetGrain(defaultDistribution);
         defaultDistributionGrain.setGrainType(AssetGrainType.DEFAULT_DISTRIBUTION_CONCEPT);
         uAssetGrainInfo.setDefaultDistributionValue(defaultDistributionValue);
         uAssetGrainInfo.setDefaultDistributionGrain(defaultDistributionGrain);
      }
      if (defaultPopulation != null) {
         UIAssetGrain defaultPopulationGrain = getUIAssetGrain(defaultPopulation);
         defaultPopulationGrain.setGrainType(AssetGrainType.DEFAULT_POPULATION_CONCEPT);
         uAssetGrainInfo.setDefaultPopulationGrain(defaultPopulationGrain);
      }
      return uAssetGrainInfo;
   }

   private UIAssetGrain tranformUIAssetGrainInfo (AssetGrainInfo assetGrainInfo) {
      UIAssetGrain uiAssetGrain = new UIAssetGrain();
      uiAssetGrain.setMappingId(assetGrainInfo.getMappingId());
      uiAssetGrain.setConceptDisplayName(assetGrainInfo.getConceptDisplayName());
      uiAssetGrain.setGrainType(assetGrainInfo.getGrainType());
      return uiAssetGrain;
   }

   private List<UIAssetGrain> getEligibleGrain (Long modelId, Long assetId) throws MappingException {
      List<UIAssetGrain> eligibleAssetGrains = new ArrayList<UIAssetGrain>();
      List<AssetGrainInfo> grains = getMappingRetrievalService().getGrainConcepts(modelId, assetId);
      if (ExecueCoreUtil.isCollectionNotEmpty(grains)) {
         for (AssetGrainInfo assetGrainInfo : grains) {
            eligibleAssetGrains.add(tranformUIAssetGrainInfo(assetGrainInfo));
         }
      }
      return eligibleAssetGrains;
   }

   private UIAssetGrain getUIAssetGrain (Mapping mapping) {
      UIAssetGrain assetGrainDefinition = new UIAssetGrain();
      assetGrainDefinition.setMappingId(mapping.getId());
      assetGrainDefinition.setConceptDisplayName(mapping.getBusinessEntityDefinition().getConcept().getDisplayName());
      assetGrainDefinition.setGrainType(mapping.getAssetGrainType());
      return assetGrainDefinition;
   }

   public void saveAssetGrain (Long modelId, UIAssetGrainInfo assetGrainInfo, Long assetId) throws ExeCueException {
      List<Mapping> mappings = new ArrayList<Mapping>();
      List<UIAssetGrain> existingGrain = assetGrainInfo.getExistingAssetGrain();
      UIAssetGrain defaultDistributionGrain = assetGrainInfo.getDefaultDistributionGrain();
      UIAssetGrain defaultPopulationGrain = assetGrainInfo.getDefaultPopulationGrain();
      String defaultDistributionValue = assetGrainInfo.getDefaultDistributionValue();
      List<Mapping> previousGrain = getMappingRetrievalService().getAssetGrain(assetId);

      for (Mapping previousMapping : previousGrain) {
         if (AssetGrainType.DEFAULT_DISTRIBUTION_CONCEPT.equals(previousMapping.getAssetGrainType())) {
            previousMapping.getAssetEntityDefinition().getColum().setDefaultValue(null);
            Long tableId = previousMapping.getAssetEntityDefinition().getTabl().getId();
            getSdxManagementService().updateColumn(assetId, tableId,
                     previousMapping.getAssetEntityDefinition().getColum());
         }
         previousMapping.setAssetGrainType(null);
      }
      getMappingManagementService().updateMappings(previousGrain);

      for (UIAssetGrain assetGrain : existingGrain) {
         Mapping mapping = getMappingRetrievalService().getMapping(assetGrain.getMappingId());
         if (AssetGrainType.DEFAULT_POPULATION_CONCEPT.equals(assetGrain.getGrainType())) {
            // set incoming GrainType to POPULATION_CONCEPT if they are DEFAULT_POPULATION_CONCEPT
            // WE can not have more than one DEFAULT_POPULATION_CONCEPT for an assetId
            mapping.setAssetGrainType(AssetGrainType.POPULATION_CONCEPT);
         } else if (AssetGrainType.DEFAULT_DISTRIBUTION_CONCEPT.equals(assetGrain.getGrainType())) {
            // set incoming GrainType to DISTRIBUTION_CONCEPT if they are DEFAULT_DISTRIBUTION_CONCEPT
            // WE can not have more than one DEFAULT_DISTRIBUTION_CONCEPT for an assetId
            mapping.setAssetGrainType(AssetGrainType.DISTRIBUTION_CONCEPT);
         } else {
            mapping.setAssetGrainType(assetGrain.getGrainType());
         }
         if (defaultDistributionGrain != null && defaultDistributionGrain.getMappingId() != null
                  && mapping.getId() == defaultDistributionGrain.getMappingId().longValue()) {
            // If incoming mappingId matched to the defaultDistributionGrain then set the assetGrainType
            // DEFAULT_DISTRIBUTION_CONCEPT
            mapping.setAssetGrainType(AssetGrainType.DEFAULT_DISTRIBUTION_CONCEPT);
            Long tableId = mapping.getAssetEntityDefinition().getTabl().getId();
            Colum colum = mapping.getAssetEntityDefinition().getColum();
            colum.setDefaultValue(defaultDistributionValue);
            getSdxManagementService().updateColumn(assetId, tableId, colum);
         } else if (defaultPopulationGrain != null && defaultPopulationGrain.getMappingId() != null
                  && mapping.getId() == defaultPopulationGrain.getMappingId().longValue()) {
            // If incoming mappingId matched to the defaultPopulationGrain then set the assetGrainType
            // DEFAULT_POPULATION_CONCEPT
            mapping.setAssetGrainType(AssetGrainType.DEFAULT_POPULATION_CONCEPT);
         }
         mappings.add(mapping);
      }
      getMappingManagementService().updateMappings(mappings);
   }

   public Mapping getMatchedMapping (Long mappingId, List<Mapping> mappings) {
      Mapping matchedMapping = null;
      for (Mapping mapping : mappings) {
         if (mapping.getId().longValue() == mappingId.longValue()) {
            matchedMapping = mapping;
            break;
         }
      }
      return matchedMapping;
   }

   public Mapping getMatchedMappingByUIAssetGrain (Long mappingId, List<UIAssetGrain> mappings) throws MappingException {
      Mapping matchedMapping = null;
      for (UIAssetGrain uiAssetGrain : mappings) {
         if (uiAssetGrain.getMappingId().longValue() == mappingId.longValue()) {
            matchedMapping = getMappingRetrievalService().getMapping(uiAssetGrain.getMappingId());
            break;
         }
      }
      return matchedMapping;
   }

   public List<UITable> getSimpleHierarchicalLookupTables (Long assetId) throws ExeCueException {
      List<Tabl> tabls = new ArrayList<Tabl>();
      tabls = getSdxRetrievalService().getTablesByLookupType(assetId, LookupType.SIMPLEHIERARCHICAL_LOOKUP);
      return getTransformedUITables(tabls);
   }

   public List<UITable> getAllTables (Asset asset) throws ExeCueException {
      List<Tabl> swiTbls = new ArrayList<Tabl>();
      swiTbls = getSdxRetrievalService().getAllTables(asset);
      return getTransformedUITables(swiTbls);
   }

   public List<Asset> getAllParentAssets (Long applicationId) throws ExeCueException {
      return getSdxRetrievalService().getAllParentAssets(applicationId);
   }

   public void createMembersByJob (Asset asset, Tabl table, int totalMembers) throws ExeCueException {

      try {
         MemberAbsorptionContext memberAbsorptionContext = new MemberAbsorptionContext();
         memberAbsorptionContext.setAssetId(asset.getId());
         memberAbsorptionContext.setTableId(table.getId());
         memberAbsorptionContext.setTotalMembers(totalMembers);
         memberAbsorptionContext.setUserId(getUserContext().getUser().getId());
         Long jobRequestId = memberAbsorptionJobService.scheduleMemberAbsorptionJob(memberAbsorptionContext);
      } catch (BatchMaintenanceException batchMaintenanceException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, batchMaintenanceException);
      }
   }

   public boolean isAssetExistsUnderBatchProcess (Long assetId) throws ExeCueException {
      return batchMaintenanceService.isAssetUnderBatchProcess(assetId);
   }

   public boolean isColumnUnderBatchProcess (Long columnId) throws ExeCueException {
      return batchMaintenanceService.isColumnUnderBatchProcess(columnId);
   }

   public boolean isTableUnderBatchProcess (Long tableId) throws ExeCueException {
      return batchMaintenanceService.isTableUnderBatchProcess(tableId);
   }

   public boolean isParentAsset (Long assetId, Long applicationId) throws ExeCueException {
      return getSdxRetrievalService().isParentAsset(assetId, applicationId);
   }

   public List<Asset> getAllInactiveAssets (Long applicationId) throws ExeCueException {
      return getSdxRetrievalService().getAllInactiveAssets(applicationId);
   }

   public IMemberAbsorptionJobService getMemberAbsorptionJobService () {
      return memberAbsorptionJobService;
   }

   public void setMemberAbsorptionJobService (IMemberAbsorptionJobService memberAbsorptionJobService) {
      this.memberAbsorptionJobService = memberAbsorptionJobService;
   }

   public IBatchMaintenanceService getBatchMaintenanceService () {
      return batchMaintenanceService;
   }

   public void setBatchMaintenanceService (IBatchMaintenanceService batchMaintenanceService) {
      this.batchMaintenanceService = batchMaintenanceService;
   }

   public Asset getAssetPopulatedByApplicationOwner (Long assetId) throws ExeCueException {
      try {
         return getSdxRetrievalService().getAssetPopulatedByApplicationOwner(assetId);
      } catch (SDXException sdxException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, sdxException);
      }
   }

   public DataSource getDataSourceForUploadedDatasets () throws ExeCueException {
      try {
         return getDataSourceSelectionService().getDataSourceForUploadedDatasets(getUserContext().getUser().getId());
      } catch (SDXException sdxException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, sdxException);
      }
   }

   /**
    * @return the dataSourceSelectionService
    */
   public IDataSourceSelectionService getDataSourceSelectionService () {
      return dataSourceSelectionService;
   }

   /**
    * @param dataSourceSelectionService
    *           the dataSourceSelectionService to set
    */
   public void setDataSourceSelectionService (IDataSourceSelectionService dataSourceSelectionService) {
      this.dataSourceSelectionService = dataSourceSelectionService;
   }

   public void updateTableForSystemDefaultMetric (Long tableId, CheckType eligibleSystemDefaultMetric)
            throws ExeCueException {
      Tabl table = getSdxRetrievalService().getTableById(tableId);
      if (!eligibleSystemDefaultMetric.equals(table.getEligibleDefaultMetric())) {
         table.setEligibleDefaultMetric(eligibleSystemDefaultMetric);
         getSdxManagementService().updateTable(table);
      }
   }

   public TableInfo prepareVirtualTableInfo (Asset asset, Tabl virtualTable) throws ExeCueException {
      Tabl originalTable = getSdxRetrievalService().getAssetTable(asset.getId(), virtualTable.getActualName());
      return getVirtualTableManagementService().prepareVirtualTableInfo(asset, originalTable, virtualTable);
   }

   public Tabl getTableById (Long tableId) throws ExeCueException {
      return getSdxRetrievalService().getTableById(tableId);
   }

   public void saveUpdateDefaultMetrics (Long tableId, List<DefaultMetric> existingDefaultMetrics)
            throws ExeCueException {
      getMappingDeletionService().deleteDefaultMetrics(tableId);
      getMappingManagementService().saveUpdateDefaultMetrics(existingDefaultMetrics);
   }

   public List<Asset> getAssetsByApplicationId (Long applicationId) throws HandlerException {
      try {
         return getSdxRetrievalService().getAssetsByApplicationId(applicationId);
      } catch (SDXException sdxException) {
         throw new HandlerException(sdxException.getCode(), sdxException);
      }
   }

   public List<Colum> getAssetTableColumnsByPage (Long assetId, Long tableId, Page page) throws ExeCueException {
      return getSdxRetrievalService().getAssetTableColumnsByPage(assetId, tableId, page);
   }

   public boolean isAssetExists (Long applicationId, String assetName) throws ExeCueException {
      return getSdxRetrievalService().isAssetExists(applicationId, assetName);
   }

   public IAssetSourcePublisherMergeService getAssetSourcePublisherMergeService () {
      return assetSourcePublisherMergeService;
   }

   public void setAssetSourcePublisherMergeService (IAssetSourcePublisherMergeService assetSourcePublisherMergeService) {
      this.assetSourcePublisherMergeService = assetSourcePublisherMergeService;
   }

   public IDefaultMetricService getDefaultMetricService () {
      return defaultMetricService;
   }

   public void setDefaultMetricService (IDefaultMetricService defaultMetricService) {
      this.defaultMetricService = defaultMetricService;
   }

   public IVirtualTableManagementService getVirtualTableManagementService () {
      return virtualTableManagementService;
   }

   public void setVirtualTableManagementService (IVirtualTableManagementService virtualTableManagementService) {
      this.virtualTableManagementService = virtualTableManagementService;
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

   public IMappingManagementService getMappingManagementService () {
      return mappingManagementService;
   }

   public void setMappingManagementService (IMappingManagementService mappingManagementService) {
      this.mappingManagementService = mappingManagementService;
   }

   public IMappingDeletionService getMappingDeletionService () {
      return mappingDeletionService;
   }

   public void setMappingDeletionService (IMappingDeletionService mappingDeletionService) {
      this.mappingDeletionService = mappingDeletionService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IJoinService getJoinService () {
      return joinService;
   }

   public void setJoinService (IJoinService joinService) {
      this.joinService = joinService;
   }

   public ISourceMetaDataService getSourceMetaDataService () {
      return sourceMetaDataService;
   }

   public void setSourceMetaDataService (ISourceMetaDataService sourceMetaDataService) {
      this.sourceMetaDataService = sourceMetaDataService;
   }

   public IConstraintPopulationService getConstraintPopulationService () {
      return constraintPopulationService;
   }

   public void setConstraintPopulationService (IConstraintPopulationService constraintPopulationService) {
      this.constraintPopulationService = constraintPopulationService;
   }

   public IAssetDeletionJobService getAssetDeletionJobService () {
      return assetDeletionJobService;
   }

   public void setAssetDeletionJobService (IAssetDeletionJobService assetDeletionJobService) {
      this.assetDeletionJobService = assetDeletionJobService;
   }

   public IDataSourceConnectionManagementService getDataSourceConnectionManagementService () {
      return dataSourceConnectionManagementService;
   }

   public void setDataSourceConnectionManagementService (
            IDataSourceConnectionManagementService dataSourceConnectionManagementService) {
      this.dataSourceConnectionManagementService = dataSourceConnectionManagementService;
   }

   /**
    * @return the cryptographyService
    */
   public ICryptographyService getCryptographyService () {
      return cryptographyService;
   }

   /**
    * @param cryptographyService the cryptographyService to set
    */
   public void setCryptographyService (ICryptographyService cryptographyService) {
      this.cryptographyService = cryptographyService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   private String getEncryptedPassword (String password) throws HandlerException {
      String encryptPassoword = null;
      try {
         encryptPassoword = cryptographyService.encryptBase64(password, coreConfigurationService
                  .getExecueDataSourcePasswordEncyptionDescryptionKey(), EncryptionAlgorithm.TRIPLE_DES);
      } catch (CryptographyException cryptographyException) {
         cryptographyException.printStackTrace();
         throw new HandlerException(ExeCueExceptionCodes.ENCRPTION_PROCESS_FAILED, cryptographyException);
      }

      return encryptPassoword;
   }

   /**
    * @return the assetOperationDetailService
    */
   public IAssetOperationDetailService getAssetOperationDetailService () {
      return assetOperationDetailService;
   }

   /**
    * @param assetOperationDetailService the assetOperationDetailService to set
    */
   public void setAssetOperationDetailService (IAssetOperationDetailService assetOperationDetailService) {
      this.assetOperationDetailService = assetOperationDetailService;
   }

   /**
    * @return the answersCatalogManagementQueueService
    */
   public IAnswersCatalogManagementQueueService getAnswersCatalogManagementQueueService () {
      return answersCatalogManagementQueueService;
   }

   /**
    * @param answersCatalogManagementQueueService the answersCatalogManagementQueueService to set
    */
   public void setAnswersCatalogManagementQueueService (
            IAnswersCatalogManagementQueueService answersCatalogManagementQueueService) {
      this.answersCatalogManagementQueueService = answersCatalogManagementQueueService;
   }

   public ISDXDeletionWrapperService getSdxDeletionWrapperService () {
      return sdxDeletionWrapperService;
   }

   public void setSdxDeletionWrapperService (ISDXDeletionWrapperService sdxDeletionWrapperService) {
      this.sdxDeletionWrapperService = sdxDeletionWrapperService;
   }

}