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


package com.execue.swi.service;

import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.AssetOperationInfo;
import com.execue.core.common.bean.entity.CannedReport;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Constraint;
import com.execue.core.common.bean.entity.ConstraintInfo;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Join;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.bean.querygen.ForeignKeyEntity;
import com.execue.core.common.bean.querygen.PrimaryKeyEntity;
import com.execue.core.common.bean.swi.PopularityAssetEntityDefinitionInfo;
import com.execue.core.common.type.AssetOperationType;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.DataSourceType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.StatType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.SDXException;

public interface ISDXRetrievalService {

   // *************************************** Methods Related to DataSource*****************************.

   public DataSource getDataSource (String dataSourceName) throws SDXException;

   public DataSource getDataSourceByAssetId (Long assetId) throws SDXException;

   public List<DataSource> getDisplayableDataSources (boolean loadUploadedConnections, Long userId) throws SDXException;

   public DataSource getDataSourceById (Long dataSourceId) throws SDXException;

   public List<DataSource> getDataSourcesByIdsList (List<Long> dataSourceIdsList) throws SDXException;

   public List<DataSource> getAllPublicDataSources () throws SDXException;

   public List<DataSource> getAllDataSourcesForPooling () throws SDXException;

   public List<DataSource> getDataSourcesByType (DataSourceType dataSourceType) throws SDXException;

   public List<DataSource> getContentAggregatorDataSourcesByAppId (Long appId) throws SDXException;

   public DataSource getUnstructuredWHDataSourceByAppId (Long appId) throws SDXException;

   // *******************************Methods Related to Asset*******************************************.

   public Asset getAssetById (Long assetId) throws SDXException;

   public Asset getAsset (Long applicationId, String assetName) throws SDXException;

   public Asset getAsset (Long id) throws SDXException;

   public List<Asset> getAssetsByApplicationId (Long applicationId) throws SDXException;

   public List<Asset> getAllAssets (Long applicationId) throws SDXException;

   public List<Asset> getAllParentAssets (Long applicationId) throws SDXException;

   public List<Asset> getAllChildAssets (Long baseAssetId) throws SDXException;

   public List<Asset> getLightAssets (List<Long> assetIds) throws SDXException;

   public List<Long> getAllAssetIds (Long applicationId) throws SDXException;

   public boolean isParentAsset (Long assetId, Long applicationId) throws SDXException;

   public List<Asset> getAllInactiveAssets (Long applicationId) throws SDXException;

   public Asset getAssetPopulatedByApplicationOwner (Long assetId) throws SDXException;

   public List<String> getAssetNamesUsingTableForUser (Long userId, String tableName) throws SDXException;

   public int getPubliclyAvailableAssetCount (Long applicationId) throws SDXException;

   // *************************************** Methods Related to Table**************************************.

   public List<Tabl> getAllTables (Asset asset) throws SDXException;

   public List<Tabl> getAllTablesExcludingVirtual (Long assetId) throws SDXException;

   public List<TableInfo> getAssetTables (Asset asset) throws SDXException;

   public List<TableInfo> getAssetTablesExcludingVirtual (Asset asset) throws SDXException;

   public TableInfo getAssetTable (Asset asset, Tabl table) throws SDXException;

   public Tabl getAssetTable (Long assetId, String tableName) throws SDXException;

   public Tabl getTableByParentTableName (Long assetId, String parentTableName) throws SDXException;

   public List<Tabl> getOrphanTables (Long assetId) throws SDXException;

   public List<Tabl> getPrimaryKeyTables (Long assetId) throws SDXException;

   public Tabl getTableById (Long tableId) throws SDXException;

   public List<Tabl> getTablesByLookupType (Long assetId, LookupType lookupType) throws SDXException;

   public List<TableInfo> getLookupTableInfos (Long assetId) throws SDXException;

   public List<Tabl> getAssetTables (Long assetId, List<String> tableNames) throws SDXException;

   public List<Tabl> getFactTablesEligibleForSystemDefaultMetrics (Long assetId) throws SDXException;

   public List<Long> getAllTableIdsForAsset (Long assetId, CheckType excludeVirtualTables) throws SDXException;

   public List<Long> getLookupTableIdsForAsset (Long assetId) throws SDXException;

   public List<Tabl> getPopulatedTables (List<Long> tableIds) throws SDXException;

   public List<Tabl> getLookupTables (Long assetId) throws SDXException;

   public Tabl getVirtualTableFromFactTableColumn (Long assetId, Long factTableId, String factTableDimensionColumn)
            throws SDXException;

   public List<Long> getLookupTableIdsWithMembers (Long assetId) throws SDXException;

   public List<Tabl> getFactTables (Long assetId) throws SDXException;

   // ****************************** Methods related to Column******************************************************.

   public List<Colum> getColumnsOfTable (Tabl table) throws SDXException;

   public Colum getAssetTableColum (Long assetId, String tableName, String columName) throws SDXException;

   public boolean isPartOfPrimaryKey (Long columId) throws SDXException;

   public boolean isForeignKeyColum (Long columId) throws SDXException;

   public Colum getColumnById (Long columnId) throws SDXException;

   public List<Long> getColumnsWithoutKDXDataType (Long assetId, Long tableId) throws SDXException;

   public List<Colum> getPopulatedColumns (List<Long> columnIds) throws SDXException;

   public List<Long> getAllColumnIdsForTable (Long assetId, Long tableId) throws SDXException;

   public List<Colum> getAssetTableColumnsByPage (Long assetId, Long tableId, Page page) throws SDXException;

   public List<String> getAssetTableColumnNamesByPage (Long assetId, Long tableId, Page metaPageDetail)
            throws SDXException;

   public List<String> getColumnDisplayNames (Long assetId, Long tableId) throws SDXException;

   public Colum getRelatedVirtualLookupColumn (Long assetId, Long tableId, Long columnId) throws SDXException;

   public boolean isAssetTableColumnChangedNameUnique (Long assetId, Long tableId, Long columnId,
            String columnDisplayName) throws SDXException;

   // ******************************Methods related to Members****************************************************
   public List<Membr> getColumnMembers (Colum column) throws SDXException;

   public List<Membr> getMembersByLookupValues (Asset asset, Tabl table, Colum column, List<String> lookupValues)
            throws SDXException;

   public Membr getMemberByLookupValue (Asset asset, Tabl table, Colum column, String lookupValue) throws SDXException;

   public Membr getMemberById (Long memberId) throws SDXException;

   public Long getTotalMembersCountOfColumn (Colum column) throws SDXException;

   public List<Membr> getPopulatedMembers (List<Long> memeberIds) throws SDXException;

   public List<Long> getAllMemberIds (Long assetId, Long tableId) throws SDXException;

   public List<Membr> getAssetTableMembersByPage (Long assetId, Long tableId, Page page) throws SDXException;

   public List<Membr> getColumnMembersByPage (Colum column, Long pageNumber, Long pageSize) throws SDXException;

   // ******************************** Methods related to Constraints********************************************

   public List<Constraint> getReferencedConstraints (Long columId) throws SDXException;

   public List<Constraint> getConstraintsByConstraintID (Long constraintID) throws SDXException;

   public Constraint getConstraintByName (String constraintName) throws SDXException;

   public List<ConstraintInfo> getPrimaryKeyReferenceConstraints (Tabl table) throws SDXException;

   public ConstraintInfo getPrimaryKeyConstraint (Long tablId) throws SDXException;

   public List<ConstraintInfo> getForiegnKeyConstraint (Long tablId) throws SDXException;

   // ********************************Methods related to AssetEntityDefinitions****************************************

   public AssetEntityDefinition getAssetEntityDefinitionByIds (Asset asset, Tabl tabl, Colum colum, Membr membr);

   public AssetEntityDefinition getAssetEntityDefinitionByNames (String applicationName, String assetName,
            String tableName, String columnName, String memberName);

   public AssetEntityDefinition getUnPopulatedAssetEntityDefinitionByNames (String applicationName, String assetName,
            String tableName, String columnName, String memberName);

   public AssetEntityDefinition getPopulatedAssetEntityDefinitionById (Long aedId) throws SDXException;

   public AssetEntityDefinition getAssetEntityDefinitionById (Long aedId) throws SDXException;

   public List<AssetEntityDefinition> getForeignKeysForPrimaryKey (Asset asset, Tabl table, Colum column)
            throws SDXException;

   public List<Colum> getForeignKeysForPrimaryKey (Colum pkColumn) throws SDXException;

   public AssetEntityDefinition pickCorrectAssetEntityDefinition (List<AssetEntityDefinition> assetEntityDefinitions)
            throws SDXException;

   public List<PopularityAssetEntityDefinitionInfo> getPopularityAssetEntityInfoForAssetId (Long assetId)
            throws SDXException;

   public List<AssetEntityDefinition> getAllMatchingAEDsByColumnName (Asset asset, Tabl table, Colum column)
            throws SDXException;

   public List<Asset> getAssetsByTypeForApplication (Long applicationId, AssetType assetType) throws SDXException;

   // ************************************Methods related to AssetOperation*******************************************

   public String getAssetOperationDataByAssetId (Long assetId, AssetOperationType assetOperationType)
            throws SDXException;

   public String getAssetOperationData (Long assetOperationId) throws SDXException;

   public AssetOperationInfo getAssetOperationByAssetId (Long assetId, AssetOperationType assetOperationType)
            throws SDXException;

   public AssetOperationInfo getAssetOperation (Long assetOperationId) throws SDXException;

   // *******************************************Miscellaneous methods*************************************

   public PrimaryKeyEntity getPrimaryKey (Long tablId) throws SDXException;

   public List<ForeignKeyEntity> getForeignKeys (Long childTableId) throws SDXException;

   public String getMinMaxMemberValue (Long assetId, Long tableId, Long columnId, StatType statType, AssetType assetType)
            throws SDXException;

   public List<Join> getMatchedJoins (List<Join> existingJoins, List<Colum> columns);

   public boolean isAssetExists (Long applicationId, String assetName) throws SDXException;

   public DataSource getLeastLoadedPublicDataSource (DataSourceType dataSourceType) throws SDXException;

   public DataSource getLeastLoadedSystemDataSource (DataSourceType dataSourceType) throws SDXException;

   public List<Long> getApplicationIdsConfiguredForUSWH (Long uswhDataSourceId) throws SDXException;

   public CannedReport getCannedReportById (Long cannedReportId) throws SDXException;

   public DataSource getSolrDataSourceByAppId (Long appId) throws SDXException;

   public Colum getLookupDescriptionColumnByLookupColumn (Long lookupColumnId) throws SDXException;

   public Map<Long, Long> getColumnIDsByAEDIDs (List<Long> columnAEDIDs) throws SDXException;

   public Map<Long, Long> getMemberIDsByAEDIDs (List<Long> memberAEDIDs) throws SDXException;
   
   /**
    * Get the virtual tables of the provided asset, which are out of the provided tables and not already in the provided
    * tables
    * 
    * @param assetId
    * @param tableIds
    * @param tableNames
    * @return
    * @throws DataAccessException
    */
   public List<Tabl> getNotAlreadyConsideredVirtualTablesFromTables (Long assetId, List<Long> consideredTableIds,
            List<String> consideredTableNames) throws SDXException;
}
