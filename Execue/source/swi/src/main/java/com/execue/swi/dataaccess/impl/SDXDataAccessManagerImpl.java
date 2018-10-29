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


package com.execue.swi.dataaccess.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.bean.entity.AppDataSource;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.AssetOperationInfo;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Constraint;
import com.execue.core.common.bean.entity.ConstraintInfo;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.HistoryAssetOperationInfo;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.bean.governor.AssetEntityTerm;
import com.execue.core.common.bean.querygen.ForeignKeyEntity;
import com.execue.core.common.bean.querygen.PrimaryKeyEntity;
import com.execue.core.common.bean.swi.PopularityAssetEntityDefinitionInfo;
import com.execue.core.common.type.AssetEntityType;
import com.execue.core.common.type.AssetOperationType;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.DataSourceType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.StatType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.dataaccess.ISDXDataAccessManager;
import com.execue.swi.dataaccess.SDXDAOComponents;
import com.execue.swi.exception.AssetOperationExceptionCodes;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;

/**
 * @author Chandra
 */
public class SDXDataAccessManagerImpl extends SDXDAOComponents implements ISDXDataAccessManager {

   private final static int MAX_CONSTRAINT_NAME_SIZE = 30;

   public List<DataSource> getDataSourcesByType (List<DataSourceType> dataSourceTypes) throws SDXException {
      try {
         return getDataSourceDAO().getDataSourcesByType(dataSourceTypes);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<DataSource> getContentAggregatorDataSourcesByAppId (Long appId) throws SDXException {
      try {
         return getAppDataSourceDAO().getContentAggregatorDataSourcesByAppId(appId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public void deleteAppDataSourceMappings (Long appId) throws SDXException {
      try {
         getAppDataSourceDAO().deleteAppDataSourceMappings(appId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void createAppDataSourceMapping (AppDataSource appDataSource) throws SDXException {
      try {
         getAppDataSourceDAO().create(appDataSource);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void createAppDataSourceMappings (List<AppDataSource> appDataSources) throws SDXException {
      try {
         getAppDataSourceDAO().createAll(appDataSources);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public DataSource getUnstructuredWHDataSourceByAppId (Long appId) throws SDXException {
      try {
         return getAppDataSourceDAO().getUnstructuredWHDataSourceByAppId(appId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<DataSource> getDataSourcesAssociatedToUserByType (Long userId, DataSourceType dataSourceType)
            throws SDXException {
      try {
         return getDataSourceDAO().getDataSourcesAssociatedToUserByType(userId, dataSourceType);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<DataSource> getAllPublicDataSources () throws SDXException {
      try {
         return getDataSourceDAO().getAllPublicDataSources();
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<DataSource> getAllDataSourcesForPooling () throws SDXException {
      try {
         return getDataSourceDAO().getAllDataSourcesForPooling(false);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<DataSource> getDataSourcesByAppIdAndDataSourceType (Long appId, DataSourceType dataSourceType)
            throws SDXException {
      try {
         return getAppDataSourceDAO().getDataSourcesByAppIdAndDataSourceType(appId, dataSourceType);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public void updateDataSource (DataSource dataSource) throws SDXException {
      try {
         getAssetEntityDefinitionDAO().update(dataSource);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DATASOURCE_UPDATE_FAILED, dataAccessException);
      }

   }

   public <DomainObject extends Serializable> DomainObject getById (Long id, Class<DomainObject> clazz)
            throws SDXException {
      try {
         return getAssetEntityDefinitionDAO().getById(id, clazz);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<? extends Serializable> getByIds (List<Long> ids, Class<?> clazz) throws SDXException {
      try {
         return getAssetEntityDefinitionDAO().getByIds(ids, clazz);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   // DataSource is independent object, method impl to be changed
   // getDataSource part of AssetEntityDefinition?
   public DataSource getDataSource (String dataSourceName) throws SDXException {
      try {
         return getDataSourceDAO().getDataSource(dataSourceName);
      } catch (DataAccessException dae) {
         throw new SDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
   }

   public DataSource getDataSourceByAssetId (Long assetId) throws SDXException {
      try {
         return getDataSourceDAO().getDataSourceByAssetId(assetId);
      } catch (DataAccessException dae) {
         throw new SDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dae);
      }
   }

   public Asset getAsset (Long applicationId, String assetName) throws SDXException {
      try {
         return getAssetDAO().getByName(applicationId, assetName);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.ASSET_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Asset> getAssetsByApplicationId (Long applicationId) throws SDXException {
      try {
         return getAssetDAO().getAssetsByApplicationId(applicationId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.ASSET_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Asset> getAllAssets (Long applicationId) throws SDXException {
      try {
         return getAssetDAO().getAllAssets(applicationId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.ASSET_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Tabl> getAllTables (Asset asset) throws SDXException {
      try {
         return getAssetTableDAO().getTablesOfAsset(asset);
      } catch (DataAccessException dae) {
         throw new SDXException(SWIExceptionCodes.TABLE_INFO_RETRIEVAL_FAILED, dae);
      }
   }

   public List<TableInfo> getAssetTables (Asset asset) throws SDXException {
      List<TableInfo> assetTables = new ArrayList<TableInfo>();
      try {
         List<Tabl> tables = getAssetTableDAO().getTablesOfAsset(asset);
         assetTables = getTableInfo(tables);
      } catch (DataAccessException dae) {
         throw new SDXException(SWIExceptionCodes.TABLE_INFO_RETRIEVAL_FAILED, dae);
      }
      return assetTables;
   }

   private List<TableInfo> getTableInfo (List<Tabl> tables) throws DataAccessException {
      List<TableInfo> assetTables = new ArrayList<TableInfo>();
      for (Tabl tabl : tables) {
         TableInfo tableInfo = getTableInfo(tabl);
         assetTables.add(tableInfo);
      }
      return assetTables;
   }

   private TableInfo getTableInfo (Tabl tabl) throws DataAccessException {
      TableInfo tableInfo = new TableInfo();
      List<Colum> columns = getAssetTableColumnDAO().getColumnsOfTable(tabl);
      tableInfo.setTable(tabl);
      tableInfo.setColumns(columns);
      if (isLookupTable(tabl)) {
         tableInfo.setMembers(getAssetTableColumnMemberDAO().getMembersOfColumn(getLookupColumn(tabl, columns)));
      }
      return tableInfo;
   }

   public TableInfo getAssetTable (Asset asset, Tabl table) throws SDXException {
      TableInfo assetTable = new TableInfo();
      try {
         List<Tabl> tables = getAssetTableDAO().getTablesOfAsset(asset);
         for (Tabl tabl : tables) {
            // tablename is unique within an asset
            if (table.getName().equals(tabl.getName())) {
               List<Colum> columns = getAssetTableColumnDAO().getColumnsOfTable(tabl);
               assetTable.setTable(tabl);
               assetTable.setColumns(columns);
               if (isLookupTable(tabl)) {
                  assetTable.setMembers(getAssetTableColumnMemberDAO().getMembersOfColumn(
                           getLookupColumn(tabl, columns)));
               }
               return assetTable;
            }
         }
      } catch (DataAccessException dae) {
         throw new SDXException(SWIExceptionCodes.TABLE_INFO_RETRIEVAL_FAILED, dae);
      }
      return null;
   }

   // creates
   public void createAsset (Asset asset) throws SDXException {
      AssetEntityDefinition assetEntityDefinition = new AssetEntityDefinition();
      try {
         assetEntityDefinition.setAsset(asset);
         assetEntityDefinition.setEntityType(AssetEntityType.ASSET);
         assetEntityDefinition = getAssetEntityDefinitionDAO().create(assetEntityDefinition);
      } catch (DataAccessException dataAccessException) {
         dataAccessException.printStackTrace();
         throw new SDXException(SWIExceptionCodes.ASSET_CREATION_FAILED, dataAccessException);
      }
   }

   public void createDataSource (DataSource dataSource) throws SDXException {
      try {
         getAssetEntityDefinitionDAO().create(dataSource);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.ASSET_CREATION_FAILED, dataAccessException);
      }
   }

   // TODO - JP - need to make the method transactional
   public void createAssetTables (Asset asset, List<TableInfo> tablesInfo) throws SDXException {
      populateTableAliases(asset, tablesInfo);
      for (TableInfo tableInfo : tablesInfo) {
         Tabl table = tableInfo.getTable();
         List<Colum> columns = tableInfo.getColumns();
         createTable(asset, table);
         if (columns != null) {
            createColumns(asset, table, columns);
         }
         if (isLookupTable(table) && tableInfo.getMembers() != null) {
            createMembers(asset, table, getLookupColumn(table, columns), tableInfo.getMembers());
         }
         // if table is virtual lookup table, need to update the column from which this table came
         // to dimension
         if (CheckType.YES.equals(table.getVirtual())) {
            Tabl originalTable = getAssetTable(asset.getId(), table.getActualName());
            List<Colum> originalTableColumns = getColumnsOfTable(originalTable);
            Colum originalColumn = getColumnByNameFromList(originalTableColumns, table.getLookupValueColumn());
            originalColumn.setKdxDataType(ColumnType.DIMENSION);
            updateColumn(originalColumn);
         }
      }
   }

   private Colum getColumnByNameFromList (List<Colum> originalColumns, String columnName) {
      Colum matchedColum = null;
      for (Colum colum : originalColumns) {
         if (colum.getName().equalsIgnoreCase(columnName)) {
            matchedColum = colum;
            break;
         }
      }
      return matchedColum;
   }

   /**
    * This method populates the aliases for the tables belonging to the asset passed
    * 
    * @param tablesInfo
    * @throws ExeCueException
    */
   private void populateTableAliases (Asset asset, List<TableInfo> tablesInfo) throws SDXException {
      List<String> tableAliases = new ArrayList<String>();
      List<TableInfo> assetTables = getAssetTables(asset);
      for (TableInfo tableInfo : assetTables) {
         tableAliases.add(tableInfo.getTable().getAlias());
      }
      for (TableInfo tableInfo : tablesInfo) {
         String alias = ExecueCoreUtil.getAlias(tableAliases);
         tableInfo.getTable().setAlias(alias);
         tableAliases.add(alias);
      }
   }

   // updates
   public void updateAsset (Asset asset) throws SDXException {
      try {
         getAssetEntityDefinitionDAO().update(asset);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   // TODO - JP - need to make the method transactional
   public void updateAssetTables (Asset asset, List<TableInfo> tablesInfo) throws SDXException {
      for (TableInfo tableInfo : tablesInfo) {
         Tabl tabl = tableInfo.getTable();
         try {
            getAssetEntityDefinitionDAO().update(tabl);
            if (tableInfo.getColumns() != null) {
               getAssetEntityDefinitionDAO().updateAll(tableInfo.getColumns());
            }
            if (isLookupTable(tabl) && tableInfo.getMembers() != null) {
               getAssetEntityDefinitionDAO().updateAll(tableInfo.getMembers());
            }
         } catch (DataAccessException e) {
            throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
         }
      }
   }

   // TODO - JP - need to make the method transactional
   public void deleteAsset (Asset asset) throws SDXException {
      try {
         getAssetEntityDefinitionDAO().deleteAsset(asset);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }
   
   public void deleteMembersInBatches (Colum column, int batchSize) throws SDXException {
      List<Membr> membersBatch = getColumnMembersBatch(column, batchSize);
      while (membersBatch.size() > 0) {
         deleteMembers(membersBatch);
         membersBatch = getColumnMembersBatch(column, batchSize);
      }
   }

   public void deleteMembers (List<Membr> members) throws SDXException {
      try {
         getAssetEntityDefinitionDAO().deleteMembers(members);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.MEMBER_DELETION_FAILED, e);
      }
   }

   private void createTable (Asset asset, Tabl tabl) throws SDXException {
      AssetEntityDefinition assetEntityDefinition = new AssetEntityDefinition();
      assetEntityDefinition.setAsset(asset);
      assetEntityDefinition.setTabl(tabl);
      assetEntityDefinition.setEntityType(AssetEntityType.TABLE);
      try {
         getAssetEntityDefinitionDAO().create(assetEntityDefinition);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.TABLE_CREATION_FAILED, dataAccessException);
      }
   }

   public void createColumns (Asset asset, Tabl table, List<Colum> columns) throws SDXException {
      String tempColumnName = null;
      for (Colum colum : columns) {
         if (StringUtils.isBlank(colum.getDisplayName())) {
            tempColumnName = colum.getName();
            colum.setDisplayName(tempColumnName.replaceAll("_", " "));
         }
         AssetEntityDefinition assetEntityDefinition = new AssetEntityDefinition();
         assetEntityDefinition.setAsset(asset);
         assetEntityDefinition.setTabl(table);
         assetEntityDefinition.setColum(colum);
         assetEntityDefinition.setEntityType(AssetEntityType.COLUMN);
         try {
            getAssetEntityDefinitionDAO().create(assetEntityDefinition);
         } catch (DataAccessException dataAccessException) {
            throw new SDXException(SWIExceptionCodes.COLUMN_CREATION_FAILED, dataAccessException);
         }

      }
   }

   public void createMembers (Asset asset, Tabl table, List<Colum> columns, List<Membr> members) throws SDXException {
      Colum lookupColumn = getLookupColumn(table, columns);
      if (lookupColumn == null) {
         throw new SDXException(SWIExceptionCodes.MEMBERS_NOT_ALLOWED_ON_FACT_TABLE,
                  "Members not allowed on fact table [" + table.getName() + "] on asset [" + asset.getName() + "]");
      }
      createMembers(asset, table, lookupColumn, members);
   }

   private void createMembers (Asset asset, Tabl table, Colum column, List<Membr> members) throws SDXException {
      for (Membr membr : members) {
         AssetEntityDefinition assetEntityDefinition = new AssetEntityDefinition();
         assetEntityDefinition.setAsset(asset);
         assetEntityDefinition.setTabl(table);
         assetEntityDefinition.setColum(column);
         assetEntityDefinition.setMembr(membr);
         assetEntityDefinition.setEntityType(AssetEntityType.MEMBER);
         try {
            getAssetEntityDefinitionDAO().create(assetEntityDefinition);
         } catch (DataAccessException dataAccessException) {
            throw new SDXException(SWIExceptionCodes.MEMBER_CREATION_FAILED, dataAccessException);
         }
      }
   }

   private Colum getLookupColumn (Tabl table, List<Colum> columns) {
      if (!isLookupTable(table)) {
         return null;
      }
      String lookupValueColumn = table.getLookupValueColumn();
      for (Colum colum : columns) {
         if (colum.getName().equals(lookupValueColumn)) {
            // there will be only one lookup column in a lookup table
            return colum;
         }
      }
      return null;
   }

   private boolean isLookupTable (Tabl table) {
      if (table.getLookupType() == null) {
         return false;
      } else {
         return true;
      }
   }

   public List<Membr> getColumnMembers (Colum column) throws SDXException {
      try {
         return getAssetTableColumnMemberDAO().getMembersOfColumn(column);
      } catch (DataAccessException dae) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   public <DomainObject extends Serializable> DomainObject getByField (String fieldValue, String fieldName,
            Class<DomainObject> clazz) throws SWIException {
      try {
         return getAssetEntityDefinitionDAO().getByField(fieldValue, fieldName, clazz);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public Tabl getAssetTable (Long assetId, String tableName) throws SDXException {
      try {
         return getAssetTableDAO().getAssetTable(assetId, tableName);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public Colum getAssetTableColum (Long assetId, String tableName, String columName) throws SDXException {
      try {
         return getAssetTableColumnDAO().getAssetTableColum(assetId, tableName, columName);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Colum> getColumnsOfTable (Tabl table) throws SDXException {
      try {
         return getAssetTableColumnDAO().getColumnsOfTable(table);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public void deleteDataSource (DataSource dataSource) throws SDXException {
      try {
         getAssetEntityDefinitionDAO().delete(dataSource);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DATASOURCE_DELETE_FAILED, dataAccessException);
      }
   }

   public AssetEntityDefinition getAssetEntityDefinitionByIds (Asset asset, Tabl tabl, Colum colum, Membr membr) {
      return getAssetEntityDefinitionDAO().getAssetEntityDefinitionByIds(asset, tabl, colum, membr);
   }

   public List<AssetEntityDefinition> getAllMatchingAEDsByColumnName (Asset asset, Tabl table, Colum column)
            throws SDXException {
      try {
         return getAssetTableColumnDAO().getAllMatchingAEDsByColumnName(asset, table, column);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method returns the ForeignKeyEntity object which contains the information about parentTable,childTable,
    * parentColums, childColums. If parentColums size is 0, it means no foreign key exists for this table. If
    * parentColums size is greater than zero, then it is composite foreign key.This method needs to be called from
    * transactional context.
    * 
    * @param childTableId
    * @return foreignKeyEntity
    * @throws SDXException
    */
   public List<ForeignKeyEntity> getForeignKeys (Long childTableId) throws SDXException {
      try {
         return getAssetTableDAO().getForeignKeys(childTableId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   /**
    * This method returns the PrimaryKeyEntity object which contains the information about primary key colums. If Colums
    * size is 0, it means no primary key exists for this table. If Colums size is greater than zero, then it is
    * composite primary key. This method needs to be called from transactional context.
    * 
    * @param tablId
    * @return primaryKeyEntity
    * @throws SDXException
    */
   public PrimaryKeyEntity getPrimaryKey (Long tablId) throws SDXException {
      try {
         return getAssetTableDAO().getPrimaryKey(tablId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public boolean isForeignKeyColum (Long columId) throws SDXException {
      try {
         return getAssetTableColumnDAO().isForeignKeyColum(columId);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public boolean isPartOfPrimaryKey (Long columId) throws SDXException {
      try {
         return getAssetTableColumnDAO().isPartOfPrimaryKey(columId);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<AssetEntityDefinition> getForeignKeysForPrimaryKey (Asset asset, Tabl table, Colum column)
            throws SDXException {
      try {
         return getAssetEntityDefinitionDAO().getForeignKeysForPrimaryKey(asset, table, column);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Colum> getForeignKeysForPrimaryKey (Colum pkColumn) throws SDXException {
      try {
         return getAssetTableColumnDAO().getForeignKeysForPrimaryKey(pkColumn);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public AssetEntityDefinition getAssetEntityDefinitionByNames (String applicationName, String assetName,
            String tableName, String columnName, String memberName) {
      return getAssetEntityDefinitionDAO().getAssetEntityDefinitionByNames(applicationName, assetName, tableName,
               columnName, memberName);
   }

   public void updateColumns (List<Colum> colums) throws SDXException {
      try {
         getAssetEntityDefinitionDAO().updateAll(colums);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updateTables (List<Tabl> tables) throws SDXException {
      try {
         getAssetEntityDefinitionDAO().updateAll(tables);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updateColumn (Colum colum) throws SDXException {
      try {
         getAssetEntityDefinitionDAO().update(colum);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updateTable (Tabl table) throws SDXException {
      try {
         getAssetEntityDefinitionDAO().update(table);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updateMember (Membr member) throws SDXException {
      try {
         getAssetEntityDefinitionDAO().update(member);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updateMembers (List<Membr> members) throws SDXException {
      try {
         getAssetEntityDefinitionDAO().updateAll(members);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void createConstraints (List<Constraint> constraints) throws SDXException {
      try {
         for (Constraint constraint : constraints) {
            constraint.setName(ExecueStringUtil.trimStringToLength(constraint.getName(), MAX_CONSTRAINT_NAME_SIZE));
         }
         getAssetEntityDefinitionDAO().createAll(constraints);
         for (Constraint constraint : constraints) {
            for (Colum colum : constraint.getConstraintColums()) {
               colum.setIsConstraintColum(CheckType.YES);
               Set<Constraint> constraintSet = new HashSet<Constraint>();
               constraintSet.add(constraint);
               colum.setConstraints(constraintSet);
               updateColumn(colum);
            }
         }
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updateConstraint (Constraint constraint) throws SDXException {
      try {
         constraint.setName(ExecueStringUtil.trimStringToLength(constraint.getName(), MAX_CONSTRAINT_NAME_SIZE));
         getAssetEntityDefinitionDAO().update(constraint);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void deleteConstraint (Constraint constraint) throws SDXException {
      try {
         getAssetEntityDefinitionDAO().delete(constraint);
         for (Colum colum : constraint.getConstraintColums()) {
            colum.setIsConstraintColum(CheckType.NO);
            // updateColumn(colum);
         }
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void deleteColumns (List<Colum> columns) throws SDXException {
      try {
         getAssetEntityDefinitionDAO().deleteColumns(columns);
      } catch (DataAccessException e) {
         e.printStackTrace();
      }
   }

   public void deleteTables (List<Tabl> tables) throws SDXException {
      try {
         getAssetEntityDefinitionDAO().deleteTables(tables);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Constraint> getReferencedConstraints (Long columId) throws SDXException {
      try {
         return getAssetTableColumnConstraintDAO().getReferencedConstraints(columId);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Constraint> getConstraintsByConstraintID (Long constriantId) throws SDXException {
      try {
         return getAssetTableColumnConstraintDAO().getConstraintsByConstraintID(constriantId);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Tabl> getOrphanTables (Long assetId) throws SDXException {
      try {
         return getAssetTableDAO().getOrphanTables(assetId);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Tabl> getPrimaryKeyTables (Long assetId) throws SDXException {
      try {
         return getAssetTableDAO().getPrimaryKeyTables(assetId);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Membr> getMembersByLookupValues (Asset asset, Tabl table, Colum column, List<String> lookupValues)
            throws SDXException {
      try {
         return getAssetTableColumnMemberDAO().getMembersByLookupValues(asset, table, column, lookupValues);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public Membr getMembersByLookupValue (Asset asset, Tabl table, Colum column, String lookupValue) throws SDXException {
      try {
         return getAssetTableColumnMemberDAO().getMembersByLookupValue(asset, table, column, lookupValue);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Asset> getAllParentAssets (Long applicationId) throws SDXException {
      try {
         return getAssetDAO().getAllParentAssets(applicationId);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Asset> getAllChildAssets (Long baseAssetId) throws SDXException {
      try {
         return getAssetDAO().getAllChildAssets(baseAssetId);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<ConstraintInfo> getForiegnKeyConstraint (Long tablId) throws SDXException {
      try {
         return getAssetTableDAO().getForiegnKeyConstraint(tablId);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   public ConstraintInfo getPrimaryKeyConstraint (Long tablId) throws SDXException {
      try {
         return getAssetTableDAO().getPrimaryKeyConstraint(tablId);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<ConstraintInfo> getPrimaryKeyReferenceConstraints (Tabl table) throws SDXException {
      try {
         return getAssetTableDAO().getPrimaryKeyReferenceConstraints(table);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public Integer updateAssetEntitiesPopularity (List<AssetEntityTerm> assetEntityTerms) throws SDXException {
      Integer rowsUpdated = 0;
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(assetEntityTerms)) {
            List<Long> assetEntityTermIds = new ArrayList<Long>();
            for (AssetEntityTerm assetEntityTerm : assetEntityTerms) {
               assetEntityTermIds.add(assetEntityTerm.getAssetEntityDefinitionId());
            }
            rowsUpdated = getAssetEntityDefinitionDAO().updateAssetEntitiesPopularity(assetEntityTermIds);
         }
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return rowsUpdated;

   }

   public boolean isEntirePrimaryKey (Long tableId, Long columId) throws SDXException {
      try {
         return getAssetTableDAO().isEntirePrimaryKey(tableId, columId);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public String getAssetOperationData (Long assetOperationId) throws SDXException {
      try {
         return getAssetOperationDAO().getAssetOperationData(assetOperationId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(AssetOperationExceptionCodes.ASSET_OPERATION_DATA_NOTFOUND_BY_ID, dataAccessException);
      }
   }

   public String getAssetOperationDataByAssetId (Long assetId, AssetOperationType assetOperationType)
            throws SDXException {
      try {
         return getAssetOperationDAO().getAssetOperationDataByAssetId(assetId, assetOperationType);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(AssetOperationExceptionCodes.ASSET_OPERATION_DATA_NOTFOUND_BY_ASSET_ID,
                  dataAccessException);
      }
   }

   public AssetOperationInfo getAssetOperation (Long assetOperationId) throws SDXException {
      try {
         return getAssetOperationDAO().getById(assetOperationId, AssetOperationInfo.class);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(AssetOperationExceptionCodes.ASSET_OPERATION_ENTRY_NOTFOUND_BY_ID, dataAccessException);
      }
   }

   public AssetOperationInfo getAssetOperationByAssetId (Long assetId, AssetOperationType assetOperationType)
            throws SDXException {
      try {
         return getAssetOperationDAO().getAssetOperationByAssetId(assetId, assetOperationType);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(AssetOperationExceptionCodes.ASSET_OPERATION_ENTRY_NOTFOUND_BY_ASSET_ID,
                  dataAccessException);
      }
   }

   public void createAssetOperation (AssetOperationInfo assetOperationInfo) throws SDXException {
      try {
         getAssetOperationDAO().create(assetOperationInfo);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(AssetOperationExceptionCodes.ASSET_OPERATION_CREATION_FAILED, dataAccessException);
      }

   }

   public void createHistoryAssetOperation (HistoryAssetOperationInfo historyAssetOperationInfo) throws SDXException {
      try {
         getAssetOperationDAO().create(historyAssetOperationInfo);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(AssetOperationExceptionCodes.HISTORY_ASSET_OPERATION_CREATION_FAILED,
                  dataAccessException);
      }
   }

   public void deleteAssetOperation (AssetOperationInfo assetOperationInfo) throws SDXException {
      try {
         getAssetOperationDAO().delete(assetOperationInfo);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(AssetOperationExceptionCodes.ASSET_OPERATION_DELETION_FAILED, dataAccessException);
      }

   }

   public void deleteHistoryAssetOperation (HistoryAssetOperationInfo historyAssetOperationInfo) throws SDXException {
      try {
         getAssetOperationDAO().delete(historyAssetOperationInfo);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(AssetOperationExceptionCodes.HISTORY_ASSET_OPERATION_DELETION_FAILED,
                  dataAccessException);
      }

   }

   public void updateAssetOperation (AssetOperationInfo assetOperationInfo) throws SDXException {
      try {
         getAssetOperationDAO().update(assetOperationInfo);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(AssetOperationExceptionCodes.ASSET_OPERATION_UPDATION_FAILED, dataAccessException);
      }

   }

   public void updateHistoryAssetOperation (HistoryAssetOperationInfo historyAssetOperationInfo) throws SDXException {
      try {
         getAssetOperationDAO().update(historyAssetOperationInfo);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(AssetOperationExceptionCodes.HISTORY_ASSET_OPERATION_UPDATION_FAILED,
                  dataAccessException);
      }

   }

   public List<Colum> getConstraintColums (Tabl childTable) throws SDXException {
      try {
         return getAssetTableColumnDAO().getConstraintColums(childTable);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Tabl> getTablesByLookupType (Long assetId, LookupType lookupType) throws SDXException {
      try {
         return getAssetTableDAO().getTablesByLookupType(assetId, lookupType);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<TableInfo> getLookupTableInfos (Long assetId) throws SDXException {
      List<TableInfo> tableInfos = new ArrayList<TableInfo>();
      try {
         List<Tabl> lookupTables = getAssetTableDAO().getLookupTables(assetId);
         tableInfos = getTableInfo(lookupTables);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
      return tableInfos;
   }

   public List<Tabl> getVirtualTablesForTable (Long assetId, String tableName) throws SDXException {
      try {
         return getAssetTableDAO().getVirtualTablesForTable(assetId, tableName);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.ISDXDataAccessManager#getNotAlreadyConsideredVirtualTablesFromTables
    *      (java.lang.Long, java.util.List, java.util.List)
    */
   public List<Tabl> getNotAlreadyConsideredVirtualTablesFromTables (Long assetId, List<Long> consideredTableIds,
            List<String> consideredTableNames) throws SDXException {
      try {
         return getAssetTableDAO().getNotAlreadyConsideredVirtualTablesFromTables(assetId, consideredTableIds,
                  consideredTableNames);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public Tabl getTableByParentTableName (Long assetId, String parentTableName) throws SDXException {
      try {
         return getAssetTableDAO().getTableByParentTableName(assetId, parentTableName);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Asset> getLightAssets (List<Long> assetIds) throws SDXException {
      try {
         return getAssetDAO().getLightAssets(assetIds);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getAllAssetIds (Long applicationId) throws SDXException {
      try {
         return getAssetDAO().getAllAssetIds(applicationId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Membr> getColumnMembersBatch (Colum column, int batchSize) throws SDXException {
      try {
         return getAssetTableColumnMemberDAO().getColumnMembersBatch(column, batchSize);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void updateAssetEntityDefinition (AssetEntityDefinition assetEntityDefinition) throws SDXException {
      try {
         getAssetEntityDefinitionDAO().update(assetEntityDefinition);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<PopularityAssetEntityDefinitionInfo> getPopularityAssetEntityInfoForAssetId (Long assetId)
            throws SDXException {
      try {
         return getAssetDAO().getPopularityInfoForAsset(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public boolean isParentAsset (Long assetId, Long applicationId) throws SDXException {
      try {
         return getAssetDAO().isParentAsset(assetId, applicationId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Asset> getAllInactiveAssets (Long applicationId) throws SDXException {
      try {
         return getAssetDAO().getAllInactiveAssets(applicationId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public DataSource getLeastLoadedPublicDataSource (DataSourceType dataSourceType) throws SDXException {
      try {
         return getAssetDAO().getLeastLoadedPublicDataSource(dataSourceType);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public DataSource getLeastLoadedSystemDataSource (DataSourceType dataSourceType) throws SDXException {
      try {
         Long loadedSystemDataSource = getAssetDAO().getLeastLoadedSystemDataSource(dataSourceType);
         return getById(loadedSystemDataSource, DataSource.class);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Tabl> getAssetTables (Long assetId, List<String> tableNames) throws SDXException {
      try {
         return getAssetTableDAO().getAssetTables(assetId, tableNames);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Membr> getColumnMembersByPage (Colum column, Long pageNumber, Long pageSize) throws SDXException {
      try {
         return getAssetTableColumnMemberDAO().getMembersOfColumnByPage(column, pageNumber, pageSize);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Long getTotalMembersCountOfColumn (Colum column) throws SDXException {
      try {
         return getAssetTableColumnMemberDAO().getTotalMembersCountOfColumn(column);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Tabl> getFactTables (Long assetId) throws SDXException {
      try {
         return getAssetTableDAO().getFactTables(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }

   }

   public List<Tabl> getFactTablesEligibleForSystemDefaultMetrics (Long assetId) throws SDXException {
      try {
         return getAssetTableDAO().getFactTablesEligibleForSystemDefaultMetrics(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getColumnsWithoutKDXDataType (Long assetId, Long tableId) throws SDXException {
      try {
         return getAssetTableColumnDAO().getColumnsWithoutKDXDataType(assetId, tableId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Colum> getPopulatedColumns (List<Long> columnIds) throws SDXException {
      try {
         return getAssetTableColumnDAO().getPopulatedColumns(columnIds);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Membr> getPopulatedMembers (List<Long> memeberIds) throws SDXException {
      try {
         return getAssetTableColumnMemberDAO().getPopulatedMembers(memeberIds);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getAllTableIdsForAsset (Long assetId, CheckType excludeVirtualTables) throws SDXException {
      try {
         return getAssetTableDAO().getAllTableIdsForAsset(assetId, excludeVirtualTables);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getLookupTableIdsForAsset (Long assetId) throws SDXException {
      try {
         return getAssetTableDAO().getLookupTableIdsForAsset(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getLookupTableIdsWithMembers (Long assetId) throws SDXException {
      try {
         return getAssetTableDAO().getLookupTableIdsWithMembers(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getAllColumnIdsForTable (Long assetId, Long tableId) throws SDXException {
      try {
         return getAssetTableColumnDAO().getAllColumnIdsForTable(assetId, tableId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getAllMemberIds (Long assetId, Long tableId) throws SDXException {
      try {
         return getAssetTableColumnMemberDAO().getAllMemberIds(assetId, tableId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Tabl> getAllTablesExcludingVirtual (Long assetId) throws SDXException {
      try {
         return getAssetTableDAO().getAllTablesExcludingVirtual(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<TableInfo> getAssetTablesExcludingVirtual (Asset asset) throws SDXException {
      List<TableInfo> assetTables = new ArrayList<TableInfo>();
      try {
         List<Tabl> tables = getAssetTableDAO().getAllTablesExcludingVirtual(asset.getId());
         for (Tabl tabl : tables) {
            TableInfo tableInfo = new TableInfo();
            List<Colum> columns = getAssetTableColumnDAO().getColumnsOfTable(tabl);
            tableInfo.setTable(tabl);
            tableInfo.setColumns(columns);
            if (isLookupTable(tabl)) {
               tableInfo.setMembers(getAssetTableColumnMemberDAO().getMembersOfColumn(getLookupColumn(tabl, columns)));
            }
            assetTables.add(tableInfo);
         }
      } catch (DataAccessException dae) {
         throw new SDXException(SWIExceptionCodes.TABLE_INFO_RETRIEVAL_FAILED, dae);
      }
      return assetTables;
   }

   public List<String> getAssetNamesUsingTableForUser (Long userId, String tableName) throws SDXException {
      try {
         return getAssetDAO().getAssetNamesUsingTableForUser(userId, tableName);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.TABLE_INFO_RETRIEVAL_FAILED, e);
      }
   }

   public String getMinMaxMemberValue (Long assetId, Long tableId, Long columnId, StatType statType, AssetType assetType)
            throws SDXException {
      try {
         return getAssetTableColumnMemberDAO().getMinMaxMemberValue(assetId, tableId, columnId, statType, assetType);
      } catch (DataAccessException e) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public int getPubliclyAvailableAssetCount (Long applicationId) throws SDXException {
      try {
         return getAssetDAO().getPubliclyAvailableAssetCount(applicationId);
      } catch (DataAccessException e) {
         throw new SDXException(e.getCode(), e);
      }
   }

   public List<Colum> getAssetTableColumnsByPage (Long assetId, Long tableId, Page page) throws SDXException {
      try {
         return getAssetTableColumnDAO().getAssetTableColumnsByPage(assetId, tableId, page);
      } catch (DataAccessException e) {
         throw new SDXException(e.getCode(), e);
      }
   }

   public List<Membr> getAssetTableMembersByPage (Long assetId, Long tableId, Page page) throws SDXException {
      try {
         return getAssetTableColumnMemberDAO().getAssetTableMembersByPage(assetId, tableId, page);
      } catch (DataAccessException e) {
         throw new SDXException(e.getCode(), e);
      }
   }

   public List<String> getAssetTableColumnNamesByPage (Long assetId, Long tableId, Page page) throws SDXException {
      try {
         return getAssetTableColumnDAO().getAssetTableColumnNamesByPage(assetId, tableId, page);
      } catch (DataAccessException e) {
         throw new SDXException(e.getCode(), e);
      }
   }

   public List<Tabl> getLookupTables (Long assetId) throws SDXException {
      try {
         return getAssetTableDAO().getLookupTables(assetId);
      } catch (DataAccessException e) {
         throw new SDXException(e.getCode(), e);
      }
   }

   public Tabl getVirtualTableFromFactTableColumn (Long assetId, Long factTableId, String factTableDimensionColumn)
            throws SDXException {
      try {
         return getAssetTableDAO().getVirtualTableFromFactTableColumn(assetId, factTableId, factTableDimensionColumn);
      } catch (DataAccessException e) {
         throw new SDXException(e.getCode(), e);
      }
   }

   public Colum getRelatedVirtualLookupColumn (Long assetId, Long tableId, Long columnId) throws SDXException {
      try {
         return getAssetTableColumnDAO().getRelatedVirtualLookupColumn(assetId, tableId, columnId);
      } catch (DataAccessException e) {
         throw new SDXException(e.getCode(), e);
      }
   }

   public List<Long> getOtherAssetTableColumnIdsByDisplayName (Long assetId, Long tableId, Long columnId,
            String columnDisplayName) throws SDXException {
      try {
         return getAssetTableColumnDAO().getOtherAssetTableColumnIdsByDisplayName(assetId, tableId, columnId,
                  columnDisplayName);
      } catch (DataAccessException e) {
         throw new SDXException(e.getCode(), e);
      }
   }

   public List<String> getColumnDisplayNames (Long assetId, Long tableId) throws SDXException {
      try {
         return getAssetTableColumnDAO().getColumnDisplayNames(assetId, tableId);
      } catch (DataAccessException e) {
         throw new SDXException(e.getCode(), e);
      }
   }

   public List<Asset> getAssetsByTypeForApplication (Long applicationId, AssetType assetType) throws SDXException {
      try {
         return getAssetDAO().getAssetsByTypeForApplication(applicationId, assetType);
      } catch (DataAccessException e) {
         throw new SDXException(e.getCode(), e);
      }
   }

   public List<Long> getApplicationIdsConfiguredForUSWH (Long uswhDataSourceId) throws SDXException {
      try {
         return getAppDataSourceDAO().getApplicationIdsConfiguredForUSWH(uswhDataSourceId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   @Override
   public DataSource getSolrDataSourceByAppId (Long appId) throws SDXException {
      try {
         return getAppDataSourceDAO().getSolrDataSourceByAppId(appId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   @Override
   public void updateAssetStatus (Long assetId, StatusEnum status) throws SDXException {
      try {
         getAssetDAO().updateAssetStatus(assetId, status);
      } catch (DataAccessException e) {
         throw new SDXException(e.getCode(), e);
      }
   }

   @Override
   public Colum getLookupDescriptionColumnByLookupColumn (Long lookupColumnId) throws SDXException {
      try {
         return getAssetTableColumnDAO().getLookupDescriptionColumnByLookupColumn(lookupColumnId);
      } catch (DataAccessException e) {
         throw new SDXException(e.getCode(), e);
      }
   }

   @Override
   public Map<Long, Long> getColumnIDsByAEDIDs (List<Long> columnAEDIDs) throws SDXException {
      try {
         return getAssetEntityDefinitionDAO().getColumnIDsByAEDIDs(columnAEDIDs);
      } catch (DataAccessException e) {
         throw new SDXException(e.getCode(), e);
      }
   }
   
   @Override
   public Map<Long, Long> getMemberIDsByAEDIDs (List<Long> memberAEDIDs) throws SDXException {
      try {
         return getAssetEntityDefinitionDAO().getMemberIDsByAEDIDs(memberAEDIDs);
      } catch (DataAccessException e) {
         throw new SDXException(e.getCode(), e);
      }
   }
}