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


package com.execue.core.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.ApplicationContext;
import com.execue.core.common.bean.TypeConceptAssociationInfo;
import com.execue.core.common.bean.entity.AppDataSource;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityMaintenanceInfo;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.FromEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.bean.querygen.SQLIndex;
import com.execue.core.common.bean.querygen.SQLTable;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.CloudComponentSelectionType;
import com.execue.core.common.type.ComponentCategory;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.EntityType;
import com.execue.core.common.type.FromEntityType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.JobType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.OperationType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.QueryConditionOperandType;
import com.execue.core.common.type.QueryValueType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.type.StatType;
import com.execue.core.util.ExecueCoreUtil;

public class ExecueBeanManagementUtil {

   public static QueryTableColumn prepareQueryTableColumn (QueryColumn colum, QueryTable table) {
      QueryTableColumn queryTableColumn = new QueryTableColumn();
      queryTableColumn.setColumn(colum);
      queryTableColumn.setTable(table);
      return queryTableColumn;
   }

   public static QueryColumn prepareQueryColumn (String columnName, DataType dataType, int precision, int scale) {
      QueryColumn queryColumn = new QueryColumn();
      queryColumn.setColumnName(columnName);
      queryColumn.setDataType(dataType);
      queryColumn.setPrecision(precision);
      queryColumn.setScale(scale);
      return queryColumn;
   }

   public static QueryTableColumn prepareQueryTableColumn (String columName, DataType columnDataType,
            String tableAlias, String tableName, String tableActualName, CheckType virtual, String tableOwner,
            LookupType lookupType, boolean isDistinct) {
      QueryTableColumn queryTableColumn = new QueryTableColumn();
      QueryTable queryTable = new QueryTable();
      QueryColumn queryColumn = new QueryColumn();
      queryColumn.setColumnName(columName);
      queryColumn.setDataType(columnDataType);
      queryColumn.setDistinct(isDistinct);
      queryTable.setTableName(tableName);
      queryTable.setOwner(tableOwner);
      queryTable.setAlias(tableAlias);
      queryTable.setActualName(tableActualName);
      queryTable.setVirtual(virtual);
      queryTable.setTableType(lookupType);
      queryTableColumn.setColumn(queryColumn);
      queryTableColumn.setTable(queryTable);
      return queryTableColumn;
   }

   public static QueryTableColumn prepareQueryTableColumn (Colum colum, Tabl table, String tableAlias) {
      QueryTableColumn queryTableColumn = new QueryTableColumn();
      QueryTable queryTable = new QueryTable();
      QueryColumn queryColumn = prepareQueryColumn(colum);
      queryTable.setTableName(table.getName());
      queryTable.setOwner(table.getOwner());
      queryTable.setActualName(table.getActualName());
      queryTable.setVirtual(table.getVirtual());
      queryTable.setAlias(tableAlias);
      queryTableColumn.setColumn(queryColumn);
      queryTableColumn.setTable(queryTable);
      return queryTableColumn;
   }

   public QueryTableColumn prepareQueryTableColumn (String tableName, String alias, String columnName, DataType dataType) {
      QueryTableColumn queryTableColumn = new QueryTableColumn();
      queryTableColumn.setTable(prepareQueryTable(tableName, alias));
      queryTableColumn.setColumn(prepareQueryColumn(columnName, dataType));
      return queryTableColumn;
   }

   public static QueryTable prepareQueryTable (Tabl table) {
      QueryTable queryTable = new QueryTable();
      queryTable.setTableName(table.getName());
      queryTable.setOwner(table.getOwner());
      queryTable.setActualName(table.getActualName());
      queryTable.setVirtual(table.getVirtual());
      queryTable.setAlias(table.getAlias());
      queryTable.setTableType(table.getLookupType());
      return queryTable;
   }

   public static QueryTable prepareQueryTable (String tableName, String tableAlias) {
      QueryTable queryTable = new QueryTable();
      queryTable.setTableName(tableName);
      queryTable.setAlias(tableAlias);
      return queryTable;
   }

   public static QueryTable prepareQueryTable (String tableName) {
      QueryTable queryTable = new QueryTable();
      queryTable.setTableName(tableName);
      return queryTable;
   }

   public static SelectEntity prepareSelectEntity (QueryTableColumn queryTableColumn, String entityAlias,
            StatType functionName) {
      SelectEntity selectEntity = new SelectEntity();
      selectEntity.setTableColumn(queryTableColumn);
      selectEntity.setType(SelectEntityType.TABLE_COLUMN);
      selectEntity.setAlias(entityAlias);
      selectEntity.setFunctionName(functionName);
      return selectEntity;
   }

   public static SelectEntity prepareDummySelectEntity (QueryTableColumn queryTableColumn) {
      SelectEntity selectEntity = new SelectEntity();
      selectEntity.setTableColumn(queryTableColumn);
      selectEntity.setType(SelectEntityType.TABLE_COLUMN);
      return selectEntity;
   }

   public static List<SelectEntity> prepareSelectEntities (QueryTableColumn queryTableColumn, String entityAlias,
            StatType functionName) {
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      selectEntities.add(prepareSelectEntity(queryTableColumn, entityAlias, functionName));
      return selectEntities;
   }

   /**
    * This method will create FromEntity Object from QueryTableColum object.
    * 
    * @param queryTableColumn
    * @return FromEntity object.
    */
   public static FromEntity createFromEntityObject (QueryTableColumn queryTableColumn) {
      FromEntity fromEntity = new FromEntity();
      QueryTable queryTable = new QueryTable();
      queryTable.setTableName(queryTableColumn.getTable().getTableName());
      queryTable.setAlias(queryTableColumn.getTable().getAlias());
      queryTable.setTableType(queryTableColumn.getTable().getTableType());
      queryTable.setOwner(queryTableColumn.getTable().getOwner());
      queryTable.setActualName(queryTableColumn.getTable().getActualName());
      queryTable.setVirtual(queryTableColumn.getTable().getVirtual());
      fromEntity.setType(FromEntityType.TABLE);
      fromEntity.setTable(queryTable);
      return fromEntity;
   }

   public static List<FromEntity> createFromEntities (QueryTable queryTable) {
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      fromEntities.add(createFromEntityObject(queryTable));
      return fromEntities;
   }

   public static FromEntity createFromEntityObject (QueryTable queryTable) {
      FromEntity fromEntity = new FromEntity();
      fromEntity.setType(FromEntityType.TABLE);
      fromEntity.setTable(queryTable);
      return fromEntity;
   }

   public static List<FromEntity> createFromEntities (Query query) {
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      fromEntities.add(createFromEntityObject(query));
      return fromEntities;
   }

   public static FromEntity createFromEntityObject (Query query) {
      FromEntity fromEntity = new FromEntity();
      fromEntity.setType(FromEntityType.SUB_QUERY);
      fromEntity.setSubQuery(query);
      return fromEntity;
   }

   public static FromEntity createFromEntityObject (String tableName, String tableAlias, CheckType virtual,
            LookupType lookupType) {
      FromEntity fromEntity = new FromEntity();
      QueryTable queryTable = new QueryTable();
      queryTable.setTableName(tableName);
      queryTable.setAlias(tableAlias);
      queryTable.setTableType(lookupType);
      queryTable.setVirtual(virtual);
      fromEntity.setType(FromEntityType.TABLE);
      fromEntity.setTable(queryTable);
      return fromEntity;
   }

   public static QueryTable createQueryTable (String tableName, String tableAlias, CheckType virtual,
            LookupType lookupType) {
      QueryTable queryTable = new QueryTable();
      queryTable.setTableName(tableName);
      queryTable.setAlias(tableAlias);
      queryTable.setTableType(lookupType);
      queryTable.setVirtual(virtual);
      return queryTable;
   }

   public static QueryValue createQueryValue (DataType dataType, String conditionValue) {
      QueryValue queryValue = new QueryValue();
      queryValue.setValueType(QueryValueType.STRING);
      queryValue.setValue(conditionValue);
      queryValue.setDataType(dataType);
      return queryValue;
   }

   public static List<QueryValue> createQueryValues (DataType dataType, String conditionValue) {
      List<QueryValue> queryValues = new ArrayList<QueryValue>();
      queryValues.add(createQueryValue(dataType, conditionValue));
      return queryValues;
   }

   public static ConditionEntity createConditionEntity (QueryTableColumn lhsTableColumn, OperatorType operator,
            List<QueryValue> rhsQueryValues) {
      ConditionEntity conditionEntity = new ConditionEntity();
      conditionEntity.setLhsTableColumn(lhsTableColumn);
      conditionEntity.setOperator(operator);
      conditionEntity.setOperandType(QueryConditionOperandType.VALUE);
      conditionEntity.setRhsValues(rhsQueryValues);
      return conditionEntity;
   }

   /**
    * This method is helper method to populateQueryGenerationInput() in order to create QueryTableColum object from
    * businessAssetTerm. It extracts the Column information from businessAssetTerm and creates a queryTableColum object
    * from it.
    * 
    * @param businessAssetTerm
    * @return queryTableColum
    */
   public static QueryTableColumn createQueryTableColum (BusinessAssetTerm businessAssetTerm) {
      Colum colum = (Colum) businessAssetTerm.getAssetEntityTerm().getAssetEntity();
      Tabl tabl = colum.getOwnerTable();
      QueryTableColumn queryTableColumn = new QueryTableColumn();
      QueryColumn queryColumn = new QueryColumn();
      queryColumn.setColumnName(colum.getName());
      queryColumn.setDataType(colum.getDataType());
      queryColumn.setFileDateFormat(colum.getFileDateFormat());
      queryColumn.setUnitType(colum.getConversionType());
      queryColumn.setDataFormat(colum.getDataFormat());
      QueryTable queryTable = new QueryTable();
      queryTable.setTableName(tabl.getName());
      queryTable.setTableType(tabl.getLookupType());
      queryTable.setAlias(tabl.getAlias());
      queryTable.setOwner(tabl.getOwner());
      queryTable.setActualName(tabl.getActualName());
      queryTable.setVirtual(tabl.getVirtual());
      queryTableColumn.setTable(queryTable);
      queryTableColumn.setColumn(queryColumn);
      return queryTableColumn;
   }

   public static List<QueryColumn> prepareQueryColumns (List<Colum> columns) {
      List<QueryColumn> queryColumns = new ArrayList<QueryColumn>();
      for (Colum colum : columns) {
         queryColumns.add(prepareQueryColumn(colum));
      }
      return queryColumns;
   }

   public static QueryColumn prepareQueryColumn (Colum colum) {
      QueryColumn queryColumn = new QueryColumn();
      queryColumn.setColumnName(colum.getName());
      queryColumn.setDataType(colum.getDataType());
      queryColumn.setDefaultValue(colum.getDefaultValue());
      queryColumn.setNullable(colum.isNullable());
      queryColumn.setPrecision(colum.getPrecision());
      queryColumn.setScale(colum.getScale());
      queryColumn.setDataFormat(colum.getDataFormat());
      return queryColumn;
   }

   public static QueryColumn prepareQueryColumn (String columnName, DataType dataType) {
      QueryColumn queryColumn = new QueryColumn();
      queryColumn.setColumnName(columnName);
      queryColumn.setDataType(dataType);
      return queryColumn;
   }

   public static List<String> prepareQueryColumnStrings (List<QueryColumn> queryColumns) {
      List<String> columnStrings = new ArrayList<String>();
      for (QueryColumn queryColumn : queryColumns) {
         columnStrings.add(queryColumn.getColumnName());
      }
      return columnStrings;
   }

   public static List<String> prepareColumnStrings (List<Colum> columns) {
      List<String> columnStrings = new ArrayList<String>();
      for (Colum column : columns) {
         columnStrings.add(column.getName());
      }
      return columnStrings;
   }

   public static SQLTable prepareSQLTableFromQueryColumns (String tableName, List<QueryColumn> queryColumns) {
      SQLTable sqlTable = new SQLTable();
      sqlTable.setTableName(tableName);
      sqlTable.setColumns(queryColumns);
      return sqlTable;
   }

   public static SQLTable prepareSQLTableFromQueryColumn (String tableName, QueryColumn queryColumn) {
      SQLTable sqlTable = new SQLTable();
      sqlTable.setTableName(tableName);
      List<QueryColumn> queryColumns = new ArrayList<QueryColumn>();
      queryColumns.add(queryColumn);
      sqlTable.setColumns(queryColumns);
      return sqlTable;
   }

   public static SQLTable prepareSQLTableFromColumns (String tableName, List<Colum> columns) {
      SQLTable sqlTable = new SQLTable();
      sqlTable.setTableName(tableName);
      sqlTable.setColumns(ExecueBeanManagementUtil.prepareQueryColumns(columns));
      return sqlTable;
   }

   public static JobRequest modifyJobRequest (JobRequest jobRequest, JobStatus newJobStatus, Date completionDate) {
      jobRequest.setJobStatus(newJobStatus);
      jobRequest.setCompletionDate(completionDate);
      return jobRequest;
   }

   public static JobOperationalStatus prepareJobOperationalStatus (JobRequest jobRequest, String operationalStage,
            JobStatus jobStatus, String jobStatusDetail, Date startDate) {
      JobOperationalStatus jobOperationalStatus = new JobOperationalStatus();
      jobOperationalStatus.setJobRequest(jobRequest);
      jobOperationalStatus.setOperationalStage(operationalStage);
      jobOperationalStatus.setJobStatus(jobStatus);
      jobOperationalStatus.setStatusDetail(jobStatusDetail);
      jobOperationalStatus.setStartDate(startDate);
      jobOperationalStatus.setJobType(jobRequest.getJobType());
      jobOperationalStatus.setUserId(jobRequest.getUserId());
      return jobOperationalStatus;
   }

   public static JobOperationalStatus modifyJobOperationalStatus (JobOperationalStatus jobOperationalStatus,
            JobStatus jobStatus, String jobStatusDetail, Date endDate) {
      jobOperationalStatus.setJobStatus(jobStatus);
      jobOperationalStatus.setStatusDetail(jobStatusDetail);
      jobOperationalStatus.setEndDate(endDate);
      return jobOperationalStatus;
   }

   public static JobRequest prepareJobRequest (JobType jobType, String requestData, JobStatus jobStatus,
            Date requestedDate, long userId) {
      JobRequest jobRequest = new JobRequest();
      jobRequest.setJobType(jobType);
      jobRequest.setRequestData(requestData);
      jobRequest.setJobStatus(jobStatus);
      jobRequest.setRequestedDate(requestedDate);
      jobRequest.setUserId(userId);
      return jobRequest;
   }

   public static TypeConceptAssociationInfo populateTypeConceptInfo (BusinessEntityDefinition conceptBed,
            BusinessEntityDefinition bedType, Cloud cloud, Long modelId, List<BehaviorType> possibleBehaviors,
            Map<Long, List<EntityTripleDefinition>> attributePaths, BusinessEntityDefinition detailTypeBed,
            boolean attributesProvided, boolean behaviorProvided, boolean detailTypeProvided,
            List<BehaviorType> previousBehaviorTypes, List<Long> previousAttributePathDefinitionIds,
            boolean previousBehaviorTypesProvided, boolean previousAttributePathDefinitionsIdsProvided,
            boolean typeChangedFromLocationToNonLocation) {
      TypeConceptAssociationInfo typeConceptInfo = new TypeConceptAssociationInfo();
      typeConceptInfo.setSourceBed(conceptBed);
      typeConceptInfo.setBedType(bedType);
      typeConceptInfo.setCloud(cloud);
      typeConceptInfo.setModelId(modelId);
      typeConceptInfo.setBehaviorTypes(possibleBehaviors);
      typeConceptInfo.setAttributePaths(attributePaths);
      typeConceptInfo.setDetailTypeBed(detailTypeBed);
      typeConceptInfo.setBehaviorProvided(behaviorProvided);
      typeConceptInfo.setAttributesProvided(attributesProvided);
      typeConceptInfo.setDetailTypeProvided(detailTypeProvided);
      typeConceptInfo.setPreviousBehaviorTypes(previousBehaviorTypes);
      typeConceptInfo.setPreviousAttributePathDefinitionIds(previousAttributePathDefinitionIds);
      typeConceptInfo.setPreviousBehaviorTypesProvided(previousBehaviorTypesProvided);
      typeConceptInfo.setPreviousAttributePathDefinitionIdsProvided(previousAttributePathDefinitionsIdsProvided);
      typeConceptInfo.setTypeChangedFromLocationToNonLocation(typeChangedFromLocationToNonLocation);
      return typeConceptInfo;
   }

   public static boolean isDataTypeDateFamily (Colum colum) {
      boolean isDataTypeDateFamily = false;
      if (DataType.TIME.equals(colum.getDataType()) || DataType.DATE.equals(colum.getDataType())
               || DataType.DATETIME.equals(colum.getDataType())) {
         isDataTypeDateFamily = true;
      }
      return isDataTypeDateFamily;
   }

   public static EntityTripleDefinition prepareEntityTripleDefinition (BusinessEntityDefinition source,
            BusinessEntityDefinition relation, BusinessEntityDefinition destination,
            EntityTripleDefinitionType entityTripleDefinitionType) {
      EntityTripleDefinition entityTripleDefinition = new EntityTripleDefinition();
      entityTripleDefinition.setSourceBusinessEntityDefinition(source);
      entityTripleDefinition.setRelation(relation);
      entityTripleDefinition.setDestinationBusinessEntityDefinition(destination);
      entityTripleDefinition.setTripleType(entityTripleDefinitionType);
      return entityTripleDefinition;
   }

   public static BusinessEntityMaintenanceInfo prepareBusinessEntityMaintenanceInfo (Long entityId,
            EntityType entityType, Long modelId, OperationType operationType, Long parentId) {
      BusinessEntityMaintenanceInfo businessEntityMaintenanceInfo = new BusinessEntityMaintenanceInfo();
      businessEntityMaintenanceInfo.setEntityId(entityId);
      businessEntityMaintenanceInfo.setEntityType(entityType);
      businessEntityMaintenanceInfo.setModelId(modelId);
      businessEntityMaintenanceInfo.setOperationType(operationType);
      businessEntityMaintenanceInfo.setParentId(parentId);
      return businessEntityMaintenanceInfo;
   }

   public static List<QueryColumn> prepareQueryColumnsFromColumnNames (List<String> columnNames) {
      List<QueryColumn> queryColumns = new ArrayList<QueryColumn>();
      for (String columName : columnNames) {
         QueryColumn queryColumn = new QueryColumn();
         queryColumn.setColumnName(columName);
         queryColumns.add(queryColumn);
      }
      return queryColumns;
   }

   public static QueryColumn prepareQueryColumnFromColumnName (String columnName) {
      QueryColumn queryColumn = new QueryColumn();
      queryColumn.setColumnName(columnName);
      return queryColumn;
   }

   public static SQLTable prepareSQLTableFromColumnNames (String tableName, List<String> columns) {
      SQLTable sqlTable = new SQLTable();
      sqlTable.setTableName(tableName);
      sqlTable.setColumns(prepareQueryColumnsFromColumnNames(columns));
      return sqlTable;
   }

   public static CloudComponent prepareCloudComponent (Cloud cloud, BusinessEntityDefinition bed,
            BusinessEntityDefinition typeBed, ComponentCategory componentCategory) {
      CloudComponent cloudComponent = new CloudComponent();
      cloudComponent.setCloud(cloud);
      cloudComponent.setComponentBed(bed);
      cloudComponent.setComponentTypeBed(typeBed);
      cloudComponent.setImportance(1.00);
      cloudComponent.setFrequency(1);
      cloudComponent.setRequired(CheckType.NO);
      cloudComponent.setComponentCategory(componentCategory);
      cloudComponent.setRepresentativeEntityType(bed.getEntityType());
      if (bed.getModelGroup().getId().equals(1L)) {
         cloudComponent.setCloudSelection(CloudComponentSelectionType.NOT_ENOUGH_FOR_CLOUD_SELECTION);
      }
      return cloudComponent;
   }

   public static ConditionEntity prepareConditionEntity (QueryTable queryTable, QueryColumn queryColumn,
            List<String> rhsValues, OperatorType operatorType) {
      QueryTableColumn queryTableColumn = ExecueBeanManagementUtil.prepareQueryTableColumn(queryColumn, queryTable);
      ConditionEntity conditionEntity = new ConditionEntity();
      conditionEntity.setLhsTableColumn(queryTableColumn);
      conditionEntity.setOperator(operatorType);
      List<QueryValue> queryValues = new ArrayList<QueryValue>();
      if (ExecueCoreUtil.isCollectionNotEmpty(rhsValues)) {
         for (String rhsValue : rhsValues) {
            queryValues.add(ExecueBeanManagementUtil.createQueryValue(queryColumn.getDataType(), rhsValue));
         }
      }
      conditionEntity.setRhsValues(queryValues);
      conditionEntity.setOperandType(QueryConditionOperandType.VALUE);
      return conditionEntity;
   }

   public static ConditionEntity prepareNotNullConditionEntity (QueryTableColumn queryTableColumn,
            OperatorType operatorType) {
      return prepareConditionEntity(queryTableColumn.getTable(), queryTableColumn.getColumn(), null, operatorType);
   }

   public static ApplicationContext prepareApplicationContext (Long modelId, Long appId, String applicationName,
            String assetName, Long assetId, AppSourceType appSourceType) {
      ApplicationContext applicationContext = new ApplicationContext();
      applicationContext.setModelId(modelId);
      applicationContext.setAppId(appId);
      applicationContext.setApplicationName(applicationName);
      applicationContext.setAssetName(assetName);
      applicationContext.setAssetId(assetId);
      applicationContext.setAppSourceType(appSourceType);
      return applicationContext;
   }

   public static AppDataSource buildAppDataSource (Long appId, Long dataSourceId) {
      AppDataSource appDataSource = new AppDataSource();
      appDataSource.setAppId(appId);
      appDataSource.setDataSourceId(dataSourceId);
      return appDataSource;
   }

   public static SQLIndex prepareSQLIndex (String tableName, String columnName) {
      List<String> columnNames = new ArrayList<String>();
      columnNames.add(columnName);
      return prepareSQLIndex(null, tableName, columnNames, false);
   }

   public static SQLIndex prepareSQLIndex (String tableName, String columnName, boolean unique) {
      List<String> columnNames = new ArrayList<String>();
      columnNames.add(columnName);
      return prepareSQLIndex(null, tableName, columnNames, unique);
   }

   public static SQLIndex prepareSQLIndex (String indexName, String tableName, String columnName, boolean unique) {
      List<String> columnNames = new ArrayList<String>();
      columnNames.add(columnName);
      return prepareSQLIndex(indexName, tableName, columnNames, unique);
   }

   public static SQLIndex prepareSQLIndex (String indexName, String tableName, List<String> columnNames, boolean unique) {
      SQLIndex index = new SQLIndex();

      index.setName(indexName);
      index.setTableName(tableName);
      index.setColumnNames(columnNames);
      index.setUnique(unique);

      return index;
   }

   public static QueryTable prepareQueryTable (String tableName, String tableAlias, String owner) {
      QueryTable prepareQueryTable = prepareQueryTable(tableName, tableAlias);
      prepareQueryTable.setOwner(owner);
      return prepareQueryTable;
   }

}
