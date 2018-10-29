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


package com.execue.swi.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.AssetOperationInfo;
import com.execue.core.common.bean.entity.CannedReport;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Constraint;
import com.execue.core.common.bean.entity.ConstraintInfo;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Join;
import com.execue.core.common.bean.entity.JoinDefinition;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.bean.querygen.ForeignKeyEntity;
import com.execue.core.common.bean.querygen.PrimaryKeyEntity;
import com.execue.core.common.bean.swi.PopularityAssetEntityDefinitionInfo;
import com.execue.core.common.type.AssetOperationType;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConstraintType;
import com.execue.core.common.type.DataSourceType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.StatType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.swi.dao.impl.ConstraintOrderComparator;
import com.execue.swi.dataaccess.ISDXDataAccessManager;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.ISDXRetrievalService;

public class SDXRetrievalServiceImpl implements ISDXRetrievalService {

   private ISDXDataAccessManager sdxDataAccessManager;

   // *******************Methods related to DataSource******************************
   public DataSource getDataSource (String dataSourceName) throws SDXException {
      return getSdxDataAccessManager().getDataSource(dataSourceName);
   }

   public DataSource getDataSourceByAssetId (Long assetId) throws SDXException {
      try {
         return getSdxDataAccessManager().getDataSourceByAssetId(assetId);
      } catch (SWIException swiException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, swiException);

      }
   }

   public List<DataSource> getDisplayableDataSources (boolean loadUploadedConnections, Long userId) throws SDXException {
      List<DataSource> dataSources = new ArrayList<DataSource>();
      if (loadUploadedConnections) {
         List<DataSource> uploadedDataSources = getSdxDataAccessManager().getDataSourcesAssociatedToUserByType(userId,
                  DataSourceType.UPLOADED);
         if (ExecueCoreUtil.isCollectionNotEmpty(uploadedDataSources)) {
            dataSources.addAll(uploadedDataSources);
         }
      }
      List<DataSource> regularDataSources = getSdxDataAccessManager().getDataSourcesAssociatedToUserByType(userId,
               DataSourceType.REGULAR);
      if (ExecueCoreUtil.isCollectionNotEmpty(regularDataSources)) {
         dataSources.addAll(regularDataSources);
      }
      return dataSources;
   }

   public DataSource getDataSourceById (Long dataSourceId) throws SDXException {
      try {
         DataSource dataSource = getSdxDataAccessManager().getById(dataSourceId, DataSource.class);
         dataSource.getAssets();
         return dataSource;
      } catch (SWIException swiException) {
         throw new SDXException(swiException.getCode(), swiException);
      }
   }

   public List<DataSource> getDataSourcesByIdsList (List<Long> dataSourceIdsList) throws SDXException {
      try {
         return (List<DataSource>) getSdxDataAccessManager().getByIds(dataSourceIdsList, DataSource.class);
      } catch (SWIException swiException) {
         throw new SDXException(swiException.getCode(), swiException);
      }
   }

   public List<DataSource> getAllPublicDataSources () throws SDXException {
      return getSdxDataAccessManager().getAllPublicDataSources();
   }

   public List<DataSource> getAllDataSourcesForPooling () throws SDXException {
      return getSdxDataAccessManager().getAllDataSourcesForPooling();
   }

   public List<DataSource> getContentAggregatorDataSourcesByAppId (Long appId) throws SDXException {
      return getSdxDataAccessManager().getContentAggregatorDataSourcesByAppId(appId);
   }

   public DataSource getUnstructuredWHDataSourceByAppId (Long appId) throws SDXException {
      return getSdxDataAccessManager().getUnstructuredWHDataSourceByAppId(appId);
   }

   public List<DataSource> getDataSourcesByType (DataSourceType dataSourceType) throws SDXException {
      List<DataSourceType> dataSourceTypes = new ArrayList<DataSourceType>();
      dataSourceTypes.add(dataSourceType);
      return getSdxDataAccessManager().getDataSourcesByType(dataSourceTypes);
   }

   // *******************Methods related to DataSource Ends******************************************

   // ********************Methods related to Asset****************************************************

   public Asset getAssetById (Long assetId) throws SDXException {
      try {
         return getSdxDataAccessManager().getById(assetId, Asset.class);
      } catch (SWIException swiException) {
         throw new SDXException(swiException.getCode(), swiException);
      }
   }

   public Asset getAsset (Long applicationId, String assetName) throws SDXException {
      Asset asset = getSdxDataAccessManager().getAsset(applicationId, assetName);
      // The asset may not exist; The client may be just checking
      if (asset != null)
         asset.getDataSource().getName();
      return asset;
   }

   public Asset getAsset (Long id) throws SDXException {
      try {
         Asset asset = getSdxDataAccessManager().getById(id, Asset.class);
         asset.getDataSource().getName();
         Application application = asset.getApplication();
         application.getName();
         return asset;
      } catch (SWIException swiException) {
         throw new SDXException(SWIExceptionCodes.ASSET_RETRIEVAL_FAILED, swiException);
      }
   }

   public List<Asset> getAssetsByApplicationId (Long applicationId) throws SDXException {
      return getSdxDataAccessManager().getAssetsByApplicationId(applicationId);
   }

   public List<Asset> getAllAssets (Long applicationId) throws SDXException {
      List<Asset> assets = getSdxDataAccessManager().getAllAssets(applicationId);
      for (Asset asset : assets) {
         asset.getDataSource().getName();
      }
      return assets;
   }

   public List<Asset> getAllParentAssets (Long applicationId) throws SDXException {
      List<Asset> assets = getSdxDataAccessManager().getAllParentAssets(applicationId);
      for (Asset asset : assets) {
         asset.getDataSource().getName();
      }
      return assets;
   }

   public List<Asset> getAllChildAssets (Long baseAssetId) throws SDXException {
      List<Asset> assets = getSdxDataAccessManager().getAllChildAssets(baseAssetId);
      for (Asset asset : assets) {
         asset.getDataSource().getName();
      }
      return assets;
   }

   public List<Asset> getLightAssets (List<Long> assetIds) throws SDXException {
      return getSdxDataAccessManager().getLightAssets(assetIds);
   }

   public List<Long> getAllAssetIds (Long applicationId) throws SDXException {
      return getSdxDataAccessManager().getAllAssetIds(applicationId);
   }

   public boolean isParentAsset (Long assetId, Long applicationId) throws SDXException {
      return getSdxDataAccessManager().isParentAsset(assetId, applicationId);
   }

   public List<Asset> getAllInactiveAssets (Long applicationId) throws SDXException {
      return getSdxDataAccessManager().getAllInactiveAssets(applicationId);
   }

   public Asset getAssetPopulatedByApplicationOwner (Long assetId) throws SDXException {
      Asset asset = getAssetById(assetId);
      asset.getApplication().getOwner().getFirstName();
      return asset;
   }

   public List<String> getAssetNamesUsingTableForUser (Long userId, String tableName) throws SDXException {
      return getSdxDataAccessManager().getAssetNamesUsingTableForUser(userId, tableName);
   }

   public int getPubliclyAvailableAssetCount (Long applicationId) throws SDXException {
      return getSdxDataAccessManager().getPubliclyAvailableAssetCount(applicationId);
   }

   // ********************Methods related to Asset Ends****************************************************

   // ********************Methods related to Table********************************************************

   public List<Tabl> getAllTables (Asset asset) throws SDXException {
      return getSdxDataAccessManager().getAllTables(asset);
   }

   public List<Tabl> getAllTablesExcludingVirtual (Long assetId) throws SDXException {
      return getSdxDataAccessManager().getAllTablesExcludingVirtual(assetId);
   }

   public List<TableInfo> getAssetTables (Asset asset) throws SDXException {
      return getSdxDataAccessManager().getAssetTables(asset);
   }

   public List<TableInfo> getAssetTablesExcludingVirtual (Asset asset) throws SDXException {
      return getSdxDataAccessManager().getAssetTablesExcludingVirtual(asset);
   }

   public TableInfo getAssetTable (Asset asset, Tabl table) throws SDXException {
      return getSdxDataAccessManager().getAssetTable(asset, table);
   }

   public Tabl getAssetTable (Long assetId, String tableName) throws SDXException {
      return getSdxDataAccessManager().getAssetTable(assetId, tableName);
   }

   public Tabl getTableByParentTableName (Long assetId, String parentTableName) throws SDXException {
      return getSdxDataAccessManager().getTableByParentTableName(assetId, parentTableName);
   }

   public List<Tabl> getOrphanTables (Long assetId) throws SDXException {
      return getSdxDataAccessManager().getOrphanTables(assetId);
   }

   public List<Tabl> getPrimaryKeyTables (Long assetId) throws SDXException {
      return getSdxDataAccessManager().getPrimaryKeyTables(assetId);
   }

   public Tabl getTableById (Long tableId) throws SDXException {
      try {
         return getSdxDataAccessManager().getById(tableId, Tabl.class);
      } catch (SWIException swiException) {
         throw new SDXException(swiException.getCode(), swiException);
      }
   }

   public List<Tabl> getTablesByLookupType (Long assetId, LookupType lookupType) throws SDXException {
      return getSdxDataAccessManager().getTablesByLookupType(assetId, lookupType);
   }

   public List<TableInfo> getLookupTableInfos (Long assetId) throws SDXException {
      return getSdxDataAccessManager().getLookupTableInfos(assetId);
   }

   public List<Tabl> getAssetTables (Long assetId, List<String> tableNames) throws SDXException {
      return getSdxDataAccessManager().getAssetTables(assetId, tableNames);
   }

   public List<Tabl> getFactTablesEligibleForSystemDefaultMetrics (Long assetId) throws SDXException {
      return getSdxDataAccessManager().getFactTablesEligibleForSystemDefaultMetrics(assetId);
   }

   public List<Long> getAllTableIdsForAsset (Long assetId, CheckType excludeVirtualTables) throws SDXException {
      return getSdxDataAccessManager().getAllTableIdsForAsset(assetId, excludeVirtualTables);
   }

   public List<Long> getLookupTableIdsForAsset (Long assetId) throws SDXException {
      return getSdxDataAccessManager().getLookupTableIdsForAsset(assetId);
   }

   public List<Tabl> getPopulatedTables (List<Long> tableIds) throws SDXException {
      List<Tabl> tables = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(tableIds)) {
         tables = new ArrayList<Tabl>();
         for (Long tableId : tableIds) {
            tables.add(getTableById(tableId));
         }
      }
      return tables;
   }

   public List<Tabl> getLookupTables (Long assetId) throws SDXException {
      return getSdxDataAccessManager().getLookupTables(assetId);
   }

   public Tabl getVirtualTableFromFactTableColumn (Long assetId, Long factTableId, String factTableDimensionColumn)
            throws SDXException {
      return getSdxDataAccessManager().getVirtualTableFromFactTableColumn(assetId, factTableId,
               factTableDimensionColumn);
   }

   public List<Long> getLookupTableIdsWithMembers (Long assetId) throws SDXException {
      return getSdxDataAccessManager().getLookupTableIdsWithMembers(assetId);
   }

   public List<Tabl> getFactTables (Long assetId) throws SDXException {
      return getSdxDataAccessManager().getFactTables(assetId);
   }

   // ********************Methods related to Table Ends********************************************************

   // ********************Methods related to Column Starts********************************************************

   public List<Colum> getColumnsOfTable (Tabl table) throws SDXException {
      return getSdxDataAccessManager().getColumnsOfTable(table);
   }

   public Colum getAssetTableColum (Long assetId, String tableName, String columName) throws SDXException {
      return getSdxDataAccessManager().getAssetTableColum(assetId, tableName, columName);
   }

   public boolean isPartOfPrimaryKey (Long columId) throws SDXException {
      return getSdxDataAccessManager().isPartOfPrimaryKey(columId);
   }

   public boolean isForeignKeyColum (Long columId) throws SDXException {
      return getSdxDataAccessManager().isForeignKeyColum(columId);
   }

   public Colum getColumnById (Long columnId) throws SDXException {
      try {
         return getSdxDataAccessManager().getById(columnId, Colum.class);
      } catch (SWIException swiException) {
         throw new SDXException(swiException.getCode(), swiException);
      }
   }

   public List<Long> getColumnsWithoutKDXDataType (Long assetId, Long tableId) throws SDXException {
      return getSdxDataAccessManager().getColumnsWithoutKDXDataType(assetId, tableId);
   }

   public List<Colum> getPopulatedColumns (List<Long> columnIds) throws SDXException {
      return getSdxDataAccessManager().getPopulatedColumns(columnIds);
   }

   public List<Long> getAllColumnIdsForTable (Long assetId, Long tableId) throws SDXException {
      return getSdxDataAccessManager().getAllColumnIdsForTable(assetId, tableId);
   }

   public List<Colum> getAssetTableColumnsByPage (Long assetId, Long tableId, Page page) throws SDXException {
      return getSdxDataAccessManager().getAssetTableColumnsByPage(assetId, tableId, page);
   }

   public List<String> getAssetTableColumnNamesByPage (Long assetId, Long tableId, Page metaPageDetail)
            throws SDXException {
      return getSdxDataAccessManager().getAssetTableColumnNamesByPage(assetId, tableId, metaPageDetail);
   }

   public List<String> getColumnDisplayNames (Long assetId, Long tableId) throws SDXException {
      return getSdxDataAccessManager().getColumnDisplayNames(assetId, tableId);
   }

   public Colum getRelatedVirtualLookupColumn (Long assetId, Long tableId, Long columnId) throws SDXException {
      return getSdxDataAccessManager().getRelatedVirtualLookupColumn(assetId, tableId, columnId);
   }

   public boolean isAssetTableColumnChangedNameUnique (Long assetId, Long tableId, Long columnId,
            String columnDisplayName) throws SDXException {
      boolean assetTableColumnChangedNameUnique = true;
      if (ExecueCoreUtil.isCollectionNotEmpty(getSdxDataAccessManager().getOtherAssetTableColumnIdsByDisplayName(
               assetId, tableId, columnId, columnDisplayName))) {
         assetTableColumnChangedNameUnique = false;
      }
      return assetTableColumnChangedNameUnique;
   }

   // ********************Methods related to Column Ends********************************************************

   // ********************Methods related to Members starts ********************************************************

   public List<Membr> getColumnMembers (Colum column) throws SDXException {
      return getSdxDataAccessManager().getColumnMembers(column);
   }

   public List<Membr> getMembersByLookupValues (Asset asset, Tabl table, Colum column, List<String> lookupValues)
            throws SDXException {
      List<Membr> members = getSdxDataAccessManager().getMembersByLookupValues(asset, table, column, lookupValues);
      if (ExecueCoreUtil.isCollectionNotEmpty(members)) {
         for (Membr member : members) {
            member.getLookupValue();
            member.getLookupDescription();
         }
      }
      return members;
   }

   public Membr getMemberByLookupValue (Asset asset, Tabl table, Colum column, String lookupValue) throws SDXException {
      return getSdxDataAccessManager().getMembersByLookupValue(asset, table, column, lookupValue);
   }

   public Membr getMemberById (Long memberId) throws SDXException {
      try {
         return getSdxDataAccessManager().getById(memberId, Membr.class);
      } catch (SWIException swiException) {
         throw new SDXException(swiException.getCode(), swiException);
      }
   }

   public Long getTotalMembersCountOfColumn (Colum column) throws SDXException {
      return getSdxDataAccessManager().getTotalMembersCountOfColumn(column);
   }

   public List<Membr> getPopulatedMembers (List<Long> memeberIds) throws SDXException {
      return getSdxDataAccessManager().getPopulatedMembers(memeberIds);
   }

   public List<Long> getAllMemberIds (Long assetId, Long tableId) throws SDXException {
      return getSdxDataAccessManager().getAllMemberIds(assetId, tableId);
   }

   public List<Membr> getAssetTableMembersByPage (Long assetId, Long tableId, Page page) throws SDXException {
      return getSdxDataAccessManager().getAssetTableMembersByPage(assetId, tableId, page);
   }

   public List<Membr> getColumnMembersByPage (Colum column, Long pageNumber, Long pageSize) throws SDXException {
      return getSdxDataAccessManager().getColumnMembersByPage(column, pageNumber, pageSize);
   }

   // ********************Methods related to Members starts ends***************************************************

   // ********************Methods related to Constraints starts ***************************************************

   public List<Constraint> getReferencedConstraints (Long columId) throws SDXException {
      return getSdxDataAccessManager().getReferencedConstraints(columId);
   }

   public List<Constraint> getConstraintsByConstraintID (Long constraintID) throws SDXException {
      return getSdxDataAccessManager().getConstraintsByConstraintID(constraintID);
   }

   public Constraint getConstraintByName (String constraintName) throws SDXException {
      try {
         return getSdxDataAccessManager().getByField(constraintName, "name", Constraint.class);
      } catch (SWIException swiException) {
         throw new SDXException(swiException.getCode(), swiException);
      }
   }

   public List<ConstraintInfo> getPrimaryKeyReferenceConstraints (Tabl table) throws SDXException {
      return getSdxDataAccessManager().getPrimaryKeyReferenceConstraints(table);
   }

   public ConstraintInfo getPrimaryKeyConstraint (Long tablId) throws SDXException {
      return getSdxDataAccessManager().getPrimaryKeyConstraint(tablId);
   }

   public List<ConstraintInfo> getForiegnKeyConstraint (Long tablId) throws SDXException {
      return getSdxDataAccessManager().getForiegnKeyConstraint(tablId);
   }

   // ********************Methods related to Constraints ends ********************************************************

   // ********************Methods related to AED'S starts*************************************************************
   public AssetEntityDefinition getAssetEntityDefinitionByIds (Asset asset, Tabl tabl, Colum colum, Membr membr) {
      AssetEntityDefinition assetEntityDefinition = getSdxDataAccessManager().getAssetEntityDefinitionByIds(asset,
               tabl, colum, membr);
      if (assetEntityDefinition != null) {
         asset = assetEntityDefinition.getAsset();
         if (asset != null) {
            asset.getId();
            tabl = assetEntityDefinition.getTabl();
            if (tabl != null) {
               tabl.getName();
               colum = assetEntityDefinition.getColum();
               if (colum != null) {
                  colum.getName();
                  membr = assetEntityDefinition.getMembr();
                  if (membr != null) {
                     membr.getLookupValue();
                  }
               }
            }
         }
      }
      return assetEntityDefinition;
   }

   public AssetEntityDefinition getAssetEntityDefinitionByNames (String applicationName, String assetName,
            String tableName, String columnName, String memberName) {
      AssetEntityDefinition assetEntityDefinition = getSdxDataAccessManager().getAssetEntityDefinitionByNames(
               applicationName, assetName, tableName, columnName, memberName);
      if (assetEntityDefinition != null) {
         Asset asset = assetEntityDefinition.getAsset();
         if (asset != null) {
            asset.getId();
            Tabl tabl = assetEntityDefinition.getTabl();
            if (tabl != null) {
               tabl.getName();
               Colum colum = assetEntityDefinition.getColum();
               if (colum != null) {
                  colum.getName();
                  Membr membr = assetEntityDefinition.getMembr();
                  if (membr != null) {
                     membr.getLookupValue();
                  }
               }
            }
         }
      }
      return assetEntityDefinition;
   }

   public AssetEntityDefinition getUnPopulatedAssetEntityDefinitionByNames (String applicationName, String assetName,
            String tableName, String columnName, String memberName) {
      return getSdxDataAccessManager().getAssetEntityDefinitionByNames(applicationName, assetName, tableName,
               columnName, memberName);
   }

   public AssetEntityDefinition getPopulatedAssetEntityDefinitionById (Long aedId) throws SDXException {
      AssetEntityDefinition aed = null;
      try {
         aed = getSdxDataAccessManager().getById(aedId, AssetEntityDefinition.class);
      } catch (SWIException swiException) {
         throw new SDXException(swiException.getCode(), swiException);
      }
      if (aed != null) {
         Tabl table = aed.getTabl();
         if (table != null) {
            table.getName();
         }
         Colum column = aed.getColum();
         if (column != null) {
            column.getName();
            column.setOwnerTable(table);
         }
         Membr member = aed.getMembr();
         if (member != null) {
            member.getLookupValue();
         }
      }
      return aed;
   }

   public AssetEntityDefinition getAssetEntityDefinitionById (Long aedId) throws SDXException {
      AssetEntityDefinition aed = null;
      try {
         aed = getSdxDataAccessManager().getById(aedId, AssetEntityDefinition.class);
      } catch (SWIException swiException) {
         throw new SDXException(swiException.getCode(), swiException);
      }
      if (aed != null) {
         Asset asset = aed.getAsset();
         asset.getName();
         Tabl table = aed.getTabl();
         if (table != null) {
            table.getName();
         }
         Colum column = aed.getColum();
         if (column != null) {
            column.getName();
         }
         Membr member = aed.getMembr();
         if (member != null) {
            member.getLookupValue();
         }
      }
      return aed;
   }

   public List<AssetEntityDefinition> getForeignKeysForPrimaryKey (Asset asset, Tabl table, Colum column)
            throws SDXException {
      // lazy loading
      List<AssetEntityDefinition> fkAEDList = getSdxDataAccessManager().getForeignKeysForPrimaryKey(asset, table,
               column);
      for (AssetEntityDefinition fkAED : fkAEDList) {
         Colum fkCol = fkAED.getColum();
         fkCol.getName();
         Tabl fkTab = fkAED.getTabl();
         fkTab.getName();
      }
      return fkAEDList;
   }

   public List<Colum> getForeignKeysForPrimaryKey (Colum pkColumn) throws SDXException {
      return getSdxDataAccessManager().getForeignKeysForPrimaryKey(pkColumn);
   }

   public AssetEntityDefinition pickCorrectAssetEntityDefinition (List<AssetEntityDefinition> assetEntityDefinitions)
            throws SDXException {
      // rules for picking the right aed are
      // 3. pick up the dimension type(id, sl, rl, shl,rhl,dimension)
      // 2. if column belongs to lookup table
      // 1. if column is entire pk
      // 4. if column is part of pk
      // if the list of AED's if greater than 0, then we pick up the first one in the list.
      // this will take care of situation if one and only one entry exists.
      // if more than one entry exits, then we will iterate and find the one which is primary key
      // or whose datatype is dimension.
      AssetEntityDefinition correctAssetEntityDefinition = assetEntityDefinitions.get(0);
      boolean correctEntityFound = false;
      if (assetEntityDefinitions.size() == 1) {
         correctEntityFound = true;
      } else {
         if (!correctEntityFound) {
            for (AssetEntityDefinition assetEntityDefinition : assetEntityDefinitions) {
               if (isEntirePrimaryKey(assetEntityDefinition.getTabl().getId(), assetEntityDefinition.getColum().getId())) {
                  correctEntityFound = true;
                  correctAssetEntityDefinition = assetEntityDefinition;
                  break;
               }
            }
         }
         if (!correctEntityFound) {
            for (AssetEntityDefinition assetEntityDefinition : assetEntityDefinitions) {
               if (isTableTypeLookupCategory(assetEntityDefinition.getTabl())) {
                  correctEntityFound = true;
                  correctAssetEntityDefinition = assetEntityDefinition;
                  break;
               }
            }
         }
         if (!correctEntityFound) {
            for (AssetEntityDefinition assetEntityDefinition : assetEntityDefinitions) {
               if (isKDXDataTypeDimensionCategory(assetEntityDefinition.getColum())) {
                  correctEntityFound = true;
                  correctAssetEntityDefinition = assetEntityDefinition;
                  break;
               }
            }
         }
         if (!correctEntityFound) {
            for (AssetEntityDefinition assetEntityDefinition : assetEntityDefinitions) {
               if (isPartOfPrimaryKey(assetEntityDefinition.getColum().getId())) {
                  correctEntityFound = true;
                  correctAssetEntityDefinition = assetEntityDefinition;
                  break;
               }
            }
         }
      }
      return correctAssetEntityDefinition;
   }

   public boolean isEntirePrimaryKey (Long tableId, Long columId) throws SDXException {
      return getSdxDataAccessManager().isEntirePrimaryKey(tableId, columId);
   }

   private boolean isTableTypeLookupCategory (Tabl tabl) {
      boolean isLookupTypeCategory = false;
      if (LookupType.SIMPLE_LOOKUP.equals(tabl.getLookupType()) || LookupType.RANGE_LOOKUP.equals(tabl.getLookupType())
               || LookupType.SIMPLEHIERARCHICAL_LOOKUP.equals(tabl.getLookupType())
               || LookupType.RANGEHIERARCHICAL_LOOKUP.equals(tabl.getLookupType())) {
         isLookupTypeCategory = true;
      }
      return isLookupTypeCategory;
   }

   private boolean isKDXDataTypeDimensionCategory (Colum colum) {
      boolean isDimensionCategory = false;
      if (ColumnType.ID.equals(colum.getKdxDataType()) || ColumnType.DIMENSION.equals(colum.getKdxDataType())
               || ColumnType.SIMPLE_LOOKUP.equals(colum.getKdxDataType())
               || ColumnType.RANGE_LOOKUP.equals(colum.getKdxDataType())
               || ColumnType.SIMPLE_HIERARCHY_LOOKUP.equals(colum.getKdxDataType())) {
         isDimensionCategory = true;
      }
      return isDimensionCategory;
   }

   public List<PopularityAssetEntityDefinitionInfo> getPopularityAssetEntityInfoForAssetId (Long assetId)
            throws SDXException {
      return getSdxDataAccessManager().getPopularityAssetEntityInfoForAssetId(assetId);

   }

   public List<AssetEntityDefinition> getAllMatchingAEDsByColumnName (Asset asset, Tabl table, Colum column)
            throws SDXException {
      return getSdxDataAccessManager().getAllMatchingAEDsByColumnName(asset, table, column);
   }

   // **************************Methods related to AssetOperation starts*******************************************

   public String getAssetOperationDataByAssetId (Long assetId, AssetOperationType assetOperationType)
            throws SDXException {
      return getSdxDataAccessManager().getAssetOperationDataByAssetId(assetId, assetOperationType);
   }

   public String getAssetOperationData (Long assetOperationId) throws SDXException {
      return getSdxDataAccessManager().getAssetOperationData(assetOperationId);
   }

   public AssetOperationInfo getAssetOperationByAssetId (Long assetId, AssetOperationType assetOperationType)
            throws SDXException {
      return getSdxDataAccessManager().getAssetOperationByAssetId(assetId, assetOperationType);
   }

   public AssetOperationInfo getAssetOperation (Long assetOperationId) throws SDXException {
      return getSdxDataAccessManager().getAssetOperation(assetOperationId);
   }

   // ***********************Methods related to AssetOperation ends*******************************************

   // *******************************************Miscellaneous methods starts*************************************

   public PrimaryKeyEntity getPrimaryKey (Long tablId) throws SDXException {
      return getSdxDataAccessManager().getPrimaryKey(tablId);
   }

   public List<ForeignKeyEntity> getForeignKeys (Long childTableId) throws SDXException {
      List<ForeignKeyEntity> foreignKeyEntities = new ArrayList<ForeignKeyEntity>();
      // find the table object from id
      Tabl childTable;
      try {
         childTable = getSdxDataAccessManager().getById(childTableId, Tabl.class);
      } catch (SWIException e) {
         throw new SDXException(e.code, e.getMessage(), e);
      }
      // get the table colums
      List<Colum> colums = getConstraintColumns(childTable);
      // for each colum, find the constraints associated with it. Then check if constraint is Foreign key, then add the
      // parentColum and parentTable.
      for (Colum colum : colums) {
         for (Constraint constraint : colum.getConstraints()) {
            if (ConstraintType.FOREIGN_KEY.equals(constraint.getType())) {
               Tabl parentTable = constraint.getReferenceTable();
               Colum parentColum = constraint.getReferenceColumn();
               // This is for lazy loading, because it might be needed later to access the names of table and colums.
               parentTable.getName();
               parentColum.getName();

               // check if this foreign key entity is already added to the list, if yes then get the old refernce
               // because it means it is a composite foreign key
               ForeignKeyEntity currForeignKeyEntity = null;
               boolean isExistsAlready = false;
               for (ForeignKeyEntity foreignKeyEntity : foreignKeyEntities) {
                  if (foreignKeyEntity.getParentTable().getName().equalsIgnoreCase(
                           constraint.getReferenceTable().getName())) {
                     isExistsAlready = true;
                     currForeignKeyEntity = foreignKeyEntity;
                     break;
                  }
               }
               // if no, then create new entity and add this to list.
               if (!isExistsAlready) {
                  currForeignKeyEntity = new ForeignKeyEntity();
                  foreignKeyEntities.add(currForeignKeyEntity);
               }
               // add the parent and child tables to the entity
               currForeignKeyEntity.setParentTable(parentTable);
               currForeignKeyEntity.setChildTable(childTable);

               // if parentcolums are null, we will create a list and add the current colum to list. if already
               // parentcolums are there, then get that list and add current colum into that list
               if (currForeignKeyEntity.getParentColums() != null) {
                  currForeignKeyEntity.getParentColums().add(parentColum);
               } else {
                  List<Colum> parentColums = new ArrayList<Colum>();
                  parentColums.add(parentColum);
                  currForeignKeyEntity.setParentColums(parentColums);
               }
               // if childcolums are null, we will create a list and add the current colum to list. if already
               // childcolums are there, then get that list and add current colum into that list
               if (currForeignKeyEntity.getChildColums() != null) {
                  currForeignKeyEntity.getChildColums().add(colum);
               } else {
                  List<Colum> childColums = new ArrayList<Colum>();
                  childColums.add(parentColum);
                  currForeignKeyEntity.setChildColums(childColums);
               }
            }
         }
      }

      // sort on basis of order all the parentcolums and child colums for all foreign key entities
      for (ForeignKeyEntity foreignKeyEntity : foreignKeyEntities) {
         Collections.sort(foreignKeyEntity.getParentColums(), new ConstraintOrderComparator());
         Collections.sort(foreignKeyEntity.getChildColums(), new ConstraintOrderComparator());
      }
      return foreignKeyEntities;
      // return getSdxDataAccessManager().getForeignKeys(childTableId);
   }

   public List<Colum> getConstraintColumns (Tabl childTable) throws SDXException {
      List<Colum> colums = getSdxDataAccessManager().getConstraintColums(childTable);
      for (Colum col : colums) {
         Set<Constraint> constraints = col.getConstraints();
         for (Constraint cons : constraints) {
            Tabl table = cons.getReferenceTable();
            if (table != null) {
               table.getName();
            }
            Colum refColum = cons.getReferenceColumn();
            if (refColum != null) {
               refColum.getName();
            }
         }
      }
      return colums;
   }

   public String getMinMaxMemberValue (Long assetId, Long tableId, Long columnId, StatType statType, AssetType assetType)
            throws SDXException {
      return getSdxDataAccessManager().getMinMaxMemberValue(assetId, tableId, columnId, statType, assetType);

   }

   // *******************************************Miscellaneous methods ends*************************************

   public List<Join> getMatchedJoins (List<Join> existingJoins, List<Colum> columns) {
      List<Join> matchedJoins = new ArrayList<Join>();
      for (Join join : existingJoins) {
         if (isJoinHasMatchedColumns(join, columns)) {
            matchedJoins.add(join);
         }
      }
      return matchedJoins;
   }

   private boolean isJoinHasMatchedColumns (Join join, List<Colum> columns) {
      for (Colum colum : columns) {
         for (JoinDefinition joinDefinition : join.getJoinDefinitions()) {
            if (joinDefinition.getSourceColumnName().equalsIgnoreCase(colum.getName())
                     || joinDefinition.getDestColumnName().equalsIgnoreCase(colum.getName())) {
               return true;
            }
         }
      }
      return false;
   }

   public List<Asset> getAssetsByTypeForApplication (Long applicationId, AssetType assetType) throws SDXException {
      return getSdxDataAccessManager().getAssetsByTypeForApplication(applicationId, assetType);
   }

   public Asset getAssetByNameForApplication (Long applicationId, String assetName) throws SDXException {
      return getSdxDataAccessManager().getAsset(applicationId, assetName);
   }

   public boolean isAssetExists (Long applicationId, String assetName) throws SDXException {
      boolean isAssetExists = false;
      Asset asset = getAssetByNameForApplication(applicationId, assetName);
      if (asset != null) {
         isAssetExists = true;
      }
      return isAssetExists;
   }

   @Override
   public CannedReport getCannedReportById (Long cannedReportId) throws SDXException {
      try {
         return getSdxDataAccessManager().getById(cannedReportId, CannedReport.class);
      } catch (SWIException e) {
         throw new SDXException(e.getCode(), e);
      }
   }

   @Override
   public Colum getLookupDescriptionColumnByLookupColumn (Long lookupColumnId) throws SDXException {
      return getSdxDataAccessManager().getLookupDescriptionColumnByLookupColumn(lookupColumnId);
   }

   @Override
   public Map<Long, Long> getColumnIDsByAEDIDs (List<Long> columnAEDIDs) throws SDXException {
      return getSdxDataAccessManager().getColumnIDsByAEDIDs(columnAEDIDs);
   }

   @Override
   public Map<Long, Long> getMemberIDsByAEDIDs (List<Long> memberAEDIDs) throws SDXException {
      return getSdxDataAccessManager().getMemberIDsByAEDIDs(memberAEDIDs);
   }

   @Override
   public List<Tabl> getNotAlreadyConsideredVirtualTablesFromTables (Long assetId, List<Long> consideredTableIds,
            List<String> consideredTableNames) throws SDXException {
      return getSdxDataAccessManager().getNotAlreadyConsideredVirtualTablesFromTables(assetId, consideredTableIds,
               consideredTableNames);
   }

   @Override
   public DataSource getSolrDataSourceByAppId (Long appId) throws SDXException {
      return getSdxDataAccessManager().getSolrDataSourceByAppId(appId);
   }

   public List<Long> getApplicationIdsConfiguredForUSWH (Long uswhDataSourceId) throws SDXException {
      return getSdxDataAccessManager().getApplicationIdsConfiguredForUSWH(uswhDataSourceId);
   }

   public DataSource getLeastLoadedPublicDataSource (DataSourceType dataSourceType) throws SDXException {
      return getSdxDataAccessManager().getLeastLoadedPublicDataSource(dataSourceType);
   }

   public DataSource getLeastLoadedSystemDataSource (DataSourceType dataSourceType) throws SDXException {
      return getSdxDataAccessManager().getLeastLoadedSystemDataSource(dataSourceType);
   }

   public ISDXDataAccessManager getSdxDataAccessManager () {
      return sdxDataAccessManager;
   }

   public void setSdxDataAccessManager (ISDXDataAccessManager sdxDataAccessManager) {
      this.sdxDataAccessManager = sdxDataAccessManager;
   }

}
