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


package com.execue.platform.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.FromEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryRepresentation;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.QueryConditionOperandType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueStringUtil;
import com.execue.das.datatransfer.etl.bean.DataTransferQuery;
import com.execue.querygen.service.IQueryGenerationService;
import com.execue.querygen.service.QueryGenerationServiceFactory;
import com.execue.util.querygen.IQueryGenerationUtilService;
import com.execue.util.querygen.QueryGenerationUtilServiceFactory;

public class UnstructuredContentTransporterHelper {

   private static final String       EXISTING_TABLE_NAME = "source_content";
   private ICoreConfigurationService coreConfigurationService;

   private IQueryGenerationUtilService getQueryGenerationUtilService (AssetProviderType providerType) {
      return QueryGenerationUtilServiceFactory.getInstance().getQueryGenerationUtilService(providerType);
   }

   private IQueryGenerationService getQueryGenerationService (AssetProviderType providerType) {
      return QueryGenerationServiceFactory.getInstance().getQueryGenerationService(providerType);
   }

   /**
    * Constructs the temp table name as <target data source id>_<application id>_<source data source id>_content_temp
    * Trim down the temp table name to max allowed table name length using ExecueStringUtil.getNormalizedName and
    * CoreConfigurationConstants.MAX_DB_OBJECT_LENGTH
    * 
    * @param targetDataSourceId
    * @param applicationId
    * @param sourceDataSourceId
    * @return
    */
   public String constructSourceContentTempTableName (Long targetDataSourceId, Long applicationId,
            Long sourceDataSourceId) {
      StringBuilder sb = new StringBuilder();
      sb.append(targetDataSourceId);
      sb.append("_");
      sb.append(applicationId);
      sb.append("_");
      sb.append(sourceDataSourceId);
      sb.append("_");
      sb.append("content_temp");
      return ExecueStringUtil.getNormalizedName(sb.toString().trim(), getCoreConfigurationService()
               .getMaxDBObjectLength());
   }

   /**
    * Construct the create table statement with the provided table name and as structurally equivalent to source_content
    * table excluding data
    * 
    * @param contentTempTableName
    * @return
    */
   public String constructSourceContentTempTableCreationStatement (AssetProviderType providerType,
            QueryTable sourceContentTempQueryTable) {
      QueryTable sourceContentQueryTable = ExecueBeanManagementUtil.prepareQueryTable(EXISTING_TABLE_NAME, null,
               sourceContentTempQueryTable.getOwner());
      return getQueryGenerationUtilService(providerType).createTableUsingExistingTableWithoutData(
               sourceContentTempQueryTable, sourceContentQueryTable);
   }

   /**
    * Construct an alter statement on the table name provided to make the id column as primary key and auto increment
    * 
    * @param contentTempTable
    * @return
    */
   public String constructSourceContentTempTableIDAutoIncrementAlterStatement (AssetProviderType providerType,
            QueryTable contentTempTable) {
      String autoIncrementStmt = null;
      QueryColumn queryColumn = new QueryColumn();
      queryColumn.setColumnName("id");
      queryColumn.setDataType(DataType.LARGE_INTEGER);
      queryColumn.setPrecision(20);
      queryColumn.setNullable(false);
      queryColumn.setAutoIncrement(true);
      if (getQueryGenerationUtilService(providerType).isAutoIncrementClauseSupported()) {
         autoIncrementStmt = getQueryGenerationUtilService(providerType).createColumnAlterStatement(contentTempTable,
                  queryColumn);
      }
      // handle for other provider types
      return autoIncrementStmt;
   }

   /**
    * Construct truncate table statement for the table name provided
    * 
    * @param sourceContentTempQueryTable
    * @return
    */
   public String constructTruncateTableStatement (AssetProviderType providerType, QueryTable sourceContentTempQueryTable) {
      return getQueryGenerationUtilService(providerType).createTableTruncateStatement(sourceContentTempQueryTable);
   }

   /**
    * Provide the drop statement for temp content table
    * 
    * @param sourceContentTempQueryTable
    * @return
    */
   public String getTempTableDropStatement (AssetProviderType providerType, QueryTable sourceContentTempQueryTable) {
      return getQueryGenerationUtilService(providerType).createTableDropStatement(sourceContentTempQueryTable);
   }

   public DataTransferQuery getSelectInsertStmtForETL (DataSource dataSource, Long applicationId,
            Long maxSourceContentIdAtTarget, String tableName, Long sourceDataSourceId, AssetProviderType providerType) {
      Map<String, String> selectQueryAliasesMap = new HashMap<String, String>();
      selectQueryAliasesMap.put("source_item_id", "SOURCEITEMID");
      selectQueryAliasesMap.put("source", "FEEDSOURCE");
      selectQueryAliasesMap.put("added_date", "ADDEDDATE");
      selectQueryAliasesMap.put("url", "URL");
      selectQueryAliasesMap.put("title", "TITLE");
      selectQueryAliasesMap.put("description", "DESCRIPTION");

      String sourceSelectQuery = getQueryOnSourceForETLProcess(dataSource, applicationId, maxSourceContentIdAtTarget,
               selectQueryAliasesMap);
      String targetInsertStatement = getInsertOnTargetForETLProcess(tableName, applicationId, sourceDataSourceId,
               providerType, selectQueryAliasesMap);

      DataTransferQuery dataTransferQuery = new DataTransferQuery();
      dataTransferQuery.setSourceSelectQuery(sourceSelectQuery);
      dataTransferQuery.setTargetInsertStatement(targetInsertStatement);
      return dataTransferQuery;
   }

   private String getInsertOnTargetForETLProcess (String tableName, Long applicationId, Long sourceDataSourceId,
            AssetProviderType providerType, Map<String, String> selectQueryAliasesMap) {
      List<QueryColumn> queryColumns = new ArrayList<QueryColumn>();
      queryColumns.add(ExecueBeanManagementUtil.prepareQueryColumn("context_id", DataType.NUMBER));
      queryColumns.add(ExecueBeanManagementUtil.prepareQueryColumn("source_server_id", DataType.NUMBER));
      queryColumns.add(ExecueBeanManagementUtil.prepareQueryColumn("source_item_id", DataType.NUMBER));
      queryColumns.add(ExecueBeanManagementUtil.prepareQueryColumn("source", DataType.STRING));
      queryColumns.add(ExecueBeanManagementUtil.prepareQueryColumn("added_date", DataType.DATE));
      queryColumns.add(ExecueBeanManagementUtil.prepareQueryColumn("url", DataType.STRING));
      queryColumns.add(ExecueBeanManagementUtil.prepareQueryColumn("title", DataType.STRING));
      queryColumns.add(ExecueBeanManagementUtil.prepareQueryColumn("description", DataType.STRING));
      // TODO:: NK: Should pass and set the owner when we use this method for different provider types like MSSQL, SAS, Teradata, etc 
      // where owner reference is mandatory
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tableName, null, null);
      Map<String, String> dataAliasesMap = new HashMap<String, String>();
      dataAliasesMap.put("context_id", applicationId.toString());
      dataAliasesMap.put("source_server_id", sourceDataSourceId.toString());
      return getQueryGenerationUtilService(providerType).createETLInsertStatement(queryTable, queryColumns,
               selectQueryAliasesMap, dataAliasesMap);
   }

   private String getQueryOnSourceForETLProcess (DataSource dataSource, Long applicationId,
            Long maxSourceContentIdAtTarget, Map<String, String> selectQueryAliasesMap) {
      QueryTable semantifiLinksQueryTable = ExecueBeanManagementUtil.prepareQueryTable("semantifi_links", "sl");
      QueryTable semantifiFeedsQueryTable = ExecueBeanManagementUtil.prepareQueryTable("semantifi_feeds", "sf");
      QueryTable semantifiCategoryQueryTable = ExecueBeanManagementUtil.prepareQueryTable("semantifi_cat", "sc");
      List<SelectEntity> selectEntities = prepareSelectEntities(semantifiLinksQueryTable, semantifiFeedsQueryTable,
               semantifiCategoryQueryTable, selectQueryAliasesMap);
      List<FromEntity> fromEntities = prepareFromEntities(semantifiLinksQueryTable, semantifiFeedsQueryTable,
               semantifiCategoryQueryTable);
      List<ConditionEntity> conditionEntities = prepareConditionEntities(semantifiLinksQueryTable,
               semantifiFeedsQueryTable, semantifiCategoryQueryTable, applicationId, maxSourceContentIdAtTarget);

      Query query = new Query();
      query.setSelectEntities(selectEntities);
      query.setFromEntities(fromEntities);
      query.setWhereEntities(conditionEntities);

      IQueryGenerationService queryGenerationService = getQueryGenerationService(dataSource.getProviderType());
      Asset asset = new Asset();
      asset.setDataSource(dataSource);
      QueryRepresentation queryRepresentation = queryGenerationService.extractQueryRepresentation(asset, query);
      return queryRepresentation.getQueryString();
   }

   private ConditionEntity prepareTableColumnConditionEntity (QueryTable lhsTable, String lhsColumnName,
            DataType lhsDataType, QueryTable rhsTable, String rhsColumnName, DataType rhsDataType) {
      QueryTableColumn lhsQueryTableColumn = ExecueBeanManagementUtil.prepareQueryTableColumn(ExecueBeanManagementUtil
               .prepareQueryColumn(lhsColumnName, lhsDataType), lhsTable);
      QueryTableColumn rhsQueryTableColumn = ExecueBeanManagementUtil.prepareQueryTableColumn(ExecueBeanManagementUtil
               .prepareQueryColumn(rhsColumnName, rhsDataType), rhsTable);
      ConditionEntity conditionEntity = new ConditionEntity();
      conditionEntity.setLhsTableColumn(lhsQueryTableColumn);
      conditionEntity.setOperandType(QueryConditionOperandType.TABLE_COLUMN);
      List<QueryTableColumn> rhsQueryTableColumns = new ArrayList<QueryTableColumn>();
      rhsQueryTableColumns.add(rhsQueryTableColumn);
      conditionEntity.setRhsTableColumns(rhsQueryTableColumns);
      conditionEntity.setOperator(OperatorType.EQUALS);
      return conditionEntity;
   }

   private ConditionEntity prepareValueConditionEntity (QueryTable lhsTable, String lhsColumnName,
            DataType lhsDataType, OperatorType operatorType, List<QueryValue> queryValues) {
      QueryTableColumn lhsQueryTableColumn = ExecueBeanManagementUtil.prepareQueryTableColumn(ExecueBeanManagementUtil
               .prepareQueryColumn(lhsColumnName, lhsDataType), lhsTable);
      ConditionEntity conditionEntity = new ConditionEntity();
      conditionEntity.setLhsTableColumn(lhsQueryTableColumn);
      conditionEntity.setOperandType(QueryConditionOperandType.VALUE);
      conditionEntity.setRhsValues(queryValues);
      conditionEntity.setOperator(operatorType);
      return conditionEntity;
   }

   private List<ConditionEntity> prepareConditionEntities (QueryTable semantifiLinksQueryTable,
            QueryTable semantifiFeedsQueryTable, QueryTable semantifiCategoryQueryTable, Long applicationId,
            Long maxSourceContentIdAtTarget) {
      List<ConditionEntity> conditionEntities = new ArrayList<ConditionEntity>();
      conditionEntities.add(prepareTableColumnConditionEntity(semantifiLinksQueryTable, "feed_id", DataType.NUMBER,
               semantifiFeedsQueryTable, "id", DataType.NUMBER));

      conditionEntities.add(prepareTableColumnConditionEntity(semantifiFeedsQueryTable, "cat_id", DataType.NUMBER,
               semantifiCategoryQueryTable, "id", DataType.NUMBER));

      conditionEntities.add(prepareValueConditionEntity(semantifiCategoryQueryTable, "name", DataType.STRING,
               OperatorType.EQUALS, ExecueBeanManagementUtil.createQueryValues(DataType.STRING, applicationId
                        .toString())));

      conditionEntities.add(prepareValueConditionEntity(semantifiLinksQueryTable, "id", DataType.NUMBER,
               OperatorType.GREATER_THAN, ExecueBeanManagementUtil.createQueryValues(DataType.NUMBER,
                        maxSourceContentIdAtTarget.toString())));

      return conditionEntities;
   }

   private SelectEntity prepareSelectEntity (QueryTable queryTable, String columnName, DataType dataType,
            String entityAlias) {
      QueryColumn queryColumn = ExecueBeanManagementUtil.prepareQueryColumn(columnName, dataType);
      QueryTableColumn queryTableColumn = ExecueBeanManagementUtil.prepareQueryTableColumn(queryColumn, queryTable);
      SelectEntity selectEntity = new SelectEntity();
      selectEntity.setTableColumn(queryTableColumn);
      selectEntity.setType(SelectEntityType.TABLE_COLUMN);
      selectEntity.setAlias(entityAlias);
      return selectEntity;
   }

   private List<SelectEntity> prepareSelectEntities (QueryTable semantifiLinksQueryTable,
            QueryTable semantifiFeedsQueryTable, QueryTable semantifiCategoryQueryTable,
            Map<String, String> selectQueryAliasesMap) {
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      selectEntities.add(prepareSelectEntity(semantifiLinksQueryTable, "id", DataType.NUMBER, selectQueryAliasesMap
               .get("source_item_id")));
      selectEntities.add(prepareSelectEntity(semantifiFeedsQueryTable, "name", DataType.STRING, selectQueryAliasesMap
               .get("source")));
      selectEntities.add(prepareSelectEntity(semantifiLinksQueryTable, "pubDate", DataType.DATE, selectQueryAliasesMap
               .get("added_date")));
      selectEntities.add(prepareSelectEntity(semantifiLinksQueryTable, "link", DataType.STRING, selectQueryAliasesMap
               .get("url")));
      selectEntities.add(prepareSelectEntity(semantifiLinksQueryTable, "title", DataType.STRING, selectQueryAliasesMap
               .get("title")));
      selectEntities.add(prepareSelectEntity(semantifiLinksQueryTable, "description", DataType.STRING,
               selectQueryAliasesMap.get("description")));
      return selectEntities;
   }

   private List<FromEntity> prepareFromEntities (QueryTable semantifiLinksQueryTable,
            QueryTable semantifiFeedsQueryTable, QueryTable semantifiCategoryQueryTable) {
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      fromEntities.add(ExecueBeanManagementUtil.createFromEntityObject(semantifiLinksQueryTable));
      fromEntities.add(ExecueBeanManagementUtil.createFromEntityObject(semantifiFeedsQueryTable));
      fromEntities.add(ExecueBeanManagementUtil.createFromEntityObject(semantifiCategoryQueryTable));
      return fromEntities;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService
    *           the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}
