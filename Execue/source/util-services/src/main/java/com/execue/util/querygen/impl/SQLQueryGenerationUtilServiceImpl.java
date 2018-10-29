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


package com.execue.util.querygen.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.LimitEntity;
import com.execue.core.common.bean.querygen.QueryClauseElement;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryStructure;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.bean.querygen.SQLIndex;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ConstraintType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.QueryConditionOperandType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.QueryFormatUtility;
import com.execue.core.constants.ISQLQueryConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.util.queryadaptor.ISQLAdaptor;
import com.execue.util.queryadaptor.SQLAdaptorFactory;
import com.execue.util.querygen.IQueryGenerationUtilService;

/**
 * @author Vishay
 */
public abstract class SQLQueryGenerationUtilServiceImpl implements IQueryGenerationUtilService, ISQLQueryConstants {

   private Integer providerTypeValue;

   public abstract String createIndexPingStatement (String indexName, QueryTable tableName);

   public abstract SelectQueryInfo createPingSelectStatement (QueryTable queryTable);

   public String createTableCreateStatement (QueryTable queryTable) {
      StringBuilder createStatement = new StringBuilder();
      createStatement.append(CREATE_TABLE_COMMAND);
      createStatement.append(SQL_SPACE_DELIMITER);
      createStatement.append(createTableRepresentation(queryTable));
      createStatement.append(SQL_SPACE_DELIMITER);
      return createStatement.toString();
   }

   public String createTableCreateStatement (QueryTable queryTable, List<QueryColumn> columns) {
      StringBuilder createStatement = new StringBuilder();
      createStatement.append(CREATE_TABLE_COMMAND);
      createStatement.append(SQL_SPACE_DELIMITER);
      createStatement.append(createTableRepresentation(queryTable));
      createStatement.append(SQL_SPACE_DELIMITER);
      createStatement.append(SQL_SUBQUERY_START_WRAPPER);
      createStatement.append(SQL_SPACE_DELIMITER);
      Iterator<QueryColumn> columnIterator = columns.iterator();
      while (columnIterator.hasNext()) {
         QueryColumn column = columnIterator.next();
         createStatement.append(createColumnDefinition(column));
         if (columnIterator.hasNext()) {
            createStatement.append(SQL_ENTITY_SEPERATOR);
            createStatement.append(SQL_SPACE_DELIMITER);
         }
      }
      createStatement.append(SQL_SUBQUERY_END_WRAPPER);
      return createStatement.toString();
   }

   public String createTableAlterStatement (QueryTable queryTable, QueryColumn queryColumn) {
      StringBuilder alterStatement = new StringBuilder();
      alterStatement.append(ALTER_TABLE_COMMAND);
      alterStatement.append(SQL_SPACE_DELIMITER);
      alterStatement.append(createTableRepresentation(queryTable));
      alterStatement.append(SQL_SPACE_DELIMITER);
      alterStatement.append(ADD_COLUMN_KEYWORD);
      alterStatement.append(SQL_SPACE_DELIMITER);
      alterStatement.append(createColumnDefinition(queryColumn));
      return alterStatement.toString();
   }

   public String createColumnAlterStatement (QueryTable queryTable, QueryColumn queryColumn) {
      StringBuilder alterStatement = new StringBuilder();
      alterStatement.append(ALTER_TABLE_COMMAND);
      alterStatement.append(SQL_SPACE_DELIMITER);
      alterStatement.append(createTableRepresentation(queryTable));
      alterStatement.append(SQL_SPACE_DELIMITER);
      alterStatement.append(CHANGE_COLUMN_KEYWORD);
      alterStatement.append(SQL_SPACE_DELIMITER);
      alterStatement.append(createColumnRepresentation(queryColumn));
      alterStatement.append(SQL_SPACE_DELIMITER);
      alterStatement.append(createColumnDefinition(queryColumn));
      return alterStatement.toString();
   }

   public String createTableDropStatement (QueryTable queryTable) {
      StringBuilder dropStatement = new StringBuilder();
      dropStatement.append(DROP_TABLE_COMMAND).append(SQL_SPACE_DELIMITER)
               .append(createTableRepresentation(queryTable));
      return dropStatement.toString();
   }

   public String createTableDeleteStatement (ConditionEntity conditionEntity) {
      StringBuilder deleteStatement = new StringBuilder();
      List<ConditionEntity> conditionEntities = new ArrayList<ConditionEntity>();
      conditionEntities.add(conditionEntity);
      List<String> conditions = buildWhereConditions(conditionEntities, false);
      QueryTable queryTable = conditionEntity.getLhsTableColumn().getTable();
      deleteStatement.append(DELETE_TABLE_COMMAND).append(SQL_SPACE_DELIMITER).append(
               createTableRepresentation(queryTable));
      deleteStatement.append(SQL_SPACE_DELIMITER);
      deleteStatement.append(SQL_WHERE_CLAUSE);
      deleteStatement.append(SQL_SPACE_DELIMITER);
      deleteStatement.append(ExecueCoreUtil.joinCollection(conditions));
      return deleteStatement.toString();
   }

   public String createTableTruncateStatement (QueryTable queryTable) {
      StringBuilder truncateStatement = new StringBuilder();
      truncateStatement.append(TRUNCATE_TABLE_COMMAND).append(SQL_SPACE_DELIMITER).append(
               createTableRepresentation(queryTable));
      return truncateStatement.toString();
   }

   public String createTableRenameStatement (QueryTable fromTable, QueryTable toTable) {
      StringBuilder renameStatement = new StringBuilder();
      renameStatement.append(RENAME_TABLE_COMMAND).append(SQL_SPACE_DELIMITER);
      renameStatement.append(createTableRepresentation(fromTable)).append(SQL_SPACE_DELIMITER).append(SQL_TO_KEYWORD)
               .append(SQL_SPACE_DELIMITER).append(createTableRepresentation(toTable));
      return renameStatement.toString();

   }

   public String createConstraintStatement (ConstraintType constraintType, String constraintName,
            QueryTable childTable, List<QueryColumn> childColumns, QueryTable parentTable,
            List<QueryColumn> parentColumns) {
      StringBuilder constraintStatement = new StringBuilder();
      constraintStatement.append(ALTER_TABLE_COMMAND);
      constraintStatement.append(SQL_SPACE_DELIMITER);
      constraintStatement.append(createTableRepresentation(childTable));
      constraintStatement.append(SQL_SPACE_DELIMITER);
      constraintStatement.append(ADD_CONSTRAINTS_KEYWORD);
      constraintStatement.append(SQL_SPACE_DELIMITER);
      constraintStatement.append(createObjectNameRepresentation(constraintName));
      constraintStatement.append(SQL_SPACE_DELIMITER);
      constraintStatement.append(constraintType.getValue() + SQL_SPACE_DELIMITER + CONSRAINT_KEY_CONSTANT);
      constraintStatement.append(SQL_SPACE_DELIMITER);
      constraintStatement.append(SQL_SUBQUERY_START_WRAPPER);
      constraintStatement.append(createColumnsCommaSeperatedRepresentation(childColumns));
      constraintStatement.append(SQL_SUBQUERY_END_WRAPPER);
      /*
       * For foreign key add references clause
       */
      if (ConstraintType.FOREIGN_KEY.equals(constraintType)) {
         constraintStatement.append(SQL_SPACE_DELIMITER);
         constraintStatement.append(REFERENCED_CONSTRAINTS_KEYWORD);
         constraintStatement.append(SQL_SPACE_DELIMITER);
         constraintStatement.append(createTableRepresentation(parentTable));
         constraintStatement.append(SQL_SPACE_DELIMITER);
         constraintStatement.append(SQL_SUBQUERY_START_WRAPPER);
         constraintStatement.append(createColumnsCommaSeperatedRepresentation(parentColumns));
         constraintStatement.append(SQL_SUBQUERY_END_WRAPPER);
      }
      return constraintStatement.toString();
   }

   public String createIndexStatement (SQLIndex sqlIndex, boolean isUnique) {
      StringBuilder indexStatement = new StringBuilder();
      indexStatement.append(CREATE_TABLE_KEYWORD);
      indexStatement.append(SQL_SPACE_DELIMITER);
      if (isUnique) {
         indexStatement.append(INDEX_UNIQUE_KEYWORD);
         indexStatement.append(SQL_SPACE_DELIMITER);
      }
      indexStatement.append(INDEX_KEYWORD);
      indexStatement.append(SQL_SPACE_DELIMITER);
      indexStatement.append(createObjectNameRepresentation(sqlIndex.getName()));
      indexStatement.append(SQL_SPACE_DELIMITER);
      indexStatement.append(JOIN_CONDITION_SEPERATOR);
      indexStatement.append(SQL_SPACE_DELIMITER);
      indexStatement.append(createTableRepresentation(sqlIndex.getTableName()));
      indexStatement.append(SQL_SPACE_DELIMITER);
      indexStatement.append(SQL_SUBQUERY_START_WRAPPER);
      indexStatement.append(createColumnsCommaSeperatedRepresentation(ExecueBeanManagementUtil
               .prepareQueryColumnsFromColumnNames(sqlIndex.getColumnNames())));
      indexStatement.append(SQL_SUBQUERY_END_WRAPPER);
      return indexStatement.toString();
   }

   @Override
   public List<String> createMultipleIndexStatement (List<SQLIndex> indexes) {
      List<String> multipleIndexStatements = new ArrayList<String>();
      String indexStatement = null;
      for (SQLIndex index : indexes) {
         indexStatement = createIndexStatement(index, index.isUnique());
         multipleIndexStatements.add(indexStatement);
      }
      return multipleIndexStatements;
   }

   public String createMultipleIndexsWithSingleStatement (List<SQLIndex> indexes) {
      return null;
   }

   public String createUpdateStatement (QueryTable targetTable, List<QueryTable> sourceTables,
            List<ConditionEntity> setConditions, List<ConditionEntity> whereConditions) {
      List<String> tablesSet = new ArrayList<String>();
      String targetTableClause = getTableClause(targetTable);
      tablesSet.add(targetTableClause);
      if (ExecueCoreUtil.isCollectionNotEmpty(sourceTables)) {
         for (QueryTable queryTable : sourceTables) {
            tablesSet.add(getTableClause(queryTable));
         }
      }
      List<String> setConditionsList = buildSetConditions(setConditions);
      List<String> whereConditionsList = buildWhereConditions(whereConditions);

      StringBuilder updateStatement = new StringBuilder();
      updateStatement.append(UPDATE_TABLE_KEYWORD);
      updateStatement.append(SQL_SPACE_DELIMITER);
      updateStatement.append(ExecueCoreUtil.joinCollection(tablesSet));
      updateStatement.append(SQL_SPACE_DELIMITER);
      updateStatement.append(SQL_SET_KEYWORD);
      updateStatement.append(SQL_SPACE_DELIMITER);
      updateStatement.append(ExecueCoreUtil.joinCollection(setConditionsList));

      if (ExecueCoreUtil.isCollectionNotEmpty(whereConditionsList)) {
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(SQL_WHERE_CLAUSE);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(ExecueCoreUtil.joinCollection(whereConditionsList, SQL_SPACE_DELIMITER
                  + WHERE_CLAUSE_SEPERATOR + SQL_SPACE_DELIMITER));
      }
      return updateStatement.toString();
   }

   public String createUpdateStatement (QueryTable targetTable, List<ConditionEntity> setConditions) {
      return createUpdateStatement(targetTable, null, setConditions, null);
   }

   public String createUpdateStatement (QueryTable targetTable, List<ConditionEntity> setConditions,
            List<ConditionEntity> whereConditions) {
      return createUpdateStatement(targetTable, null, setConditions, whereConditions);
   }

   public String createETLInsertStatement (QueryTable queryTable, List<QueryColumn> queryColumns,
            Map<String, String> selectQueryColumnAliases) {
      return createETLInsertStatement(queryTable, queryColumns, selectQueryColumnAliases, new HashMap<String, String>());
   }

   public String createETLInsertStatement (QueryTable queryTable, List<QueryColumn> queryColumns,
            Map<String, String> selectQueryColumnAliases, Map<String, String> dataColumnAliases) {
      StringBuilder insertStatement = new StringBuilder();
      insertStatement.append(INSERT_TABLE_COMMAND);
      insertStatement.append(SQL_SPACE_DELIMITER);
      insertStatement.append(createTableRepresentation(queryTable));
      insertStatement.append(SQL_SPACE_DELIMITER);
      insertStatement.append(SQL_SUBQUERY_START_WRAPPER);
      insertStatement.append(createColumnsCommaSeperatedRepresentation(queryColumns));
      insertStatement.append(SQL_SUBQUERY_END_WRAPPER);
      insertStatement.append(SQL_SPACE_DELIMITER);
      insertStatement.append(INSERT_VALUES_KEYWORD);
      insertStatement.append(SQL_SPACE_DELIMITER);
      insertStatement.append(SQL_SUBQUERY_START_WRAPPER);

      List<String> params = new ArrayList<String>(queryColumns.size());
      for (QueryColumn queryColumn : queryColumns) {
         String alias = dataColumnAliases.get(queryColumn.getColumnName());
         if (ExecueCoreUtil.isEmpty(alias)) {
            alias = "?" + selectQueryColumnAliases.get(queryColumn.getColumnName());
         }
         params.add(alias);
      }
      insertStatement.append(ExecueCoreUtil.joinCollection(params));
      insertStatement.append(SQL_SUBQUERY_END_WRAPPER);
      return insertStatement.toString();
   }

   public String createInsertStatement (QueryTable tableName) {
      StringBuilder insertStatement = new StringBuilder();
      insertStatement.append(INSERT_TABLE_COMMAND);
      insertStatement.append(SQL_SPACE_DELIMITER);
      insertStatement.append(createTableRepresentation(tableName));
      return insertStatement.toString();
   }

   public String createInsertStatement (QueryTable queryTable, List<QueryColumn> queryColumns, boolean isValuesSection) {
      StringBuilder insertStatement = new StringBuilder();
      insertStatement.append(INSERT_TABLE_COMMAND);
      insertStatement.append(SQL_SPACE_DELIMITER);
      insertStatement.append(createTableRepresentation(queryTable));
      insertStatement.append(SQL_SPACE_DELIMITER);
      insertStatement.append(SQL_SUBQUERY_START_WRAPPER);
      insertStatement.append(createColumnsCommaSeperatedRepresentation(queryColumns));
      insertStatement.append(SQL_SUBQUERY_END_WRAPPER);
      if (isValuesSection) {
         insertStatement.append(SQL_SPACE_DELIMITER);
         insertStatement.append(INSERT_VALUES_KEYWORD);
         insertStatement.append(SQL_SPACE_DELIMITER);
         insertStatement.append(SQL_SUBQUERY_START_WRAPPER);

         List<String> params = new ArrayList<String>(queryColumns.size());
         for (QueryColumn queryColumn : queryColumns) {
            params.add("?");
         }
         insertStatement.append(ExecueCoreUtil.joinCollection(params));
         insertStatement.append(SQL_SUBQUERY_END_WRAPPER);
      }
      return insertStatement.toString();
   }

   protected List<String> buildWhereConditions (List<ConditionEntity> conditionEntities, boolean userAlias) {
      boolean useAliasOnLeftSide = false;
      if (userAlias) {
         useAliasOnLeftSide = true;
      }
      return buildConditions(conditionEntities, userAlias, useAliasOnLeftSide);
   }

   protected List<String> buildWhereConditions (List<ConditionEntity> conditionEntities) {
      return buildConditions(conditionEntities, true, true);
   }

   protected List<String> buildSetConditions (List<ConditionEntity> conditionEntities) {
      return buildConditions(conditionEntities, true, true);
   }

   protected List<String> buildConditions (List<ConditionEntity> conditionEntities, boolean useAliasOnBothSides,
            boolean useAliasOnLeftSide) {
      List<String> conditions = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionNotEmpty(conditionEntities)) {
         for (ConditionEntity conditionEntity : conditionEntities) {
            StringBuilder columnSet = new StringBuilder();
            QueryTableColumn lhsTableColumn = conditionEntity.getLhsTableColumn();
            QueryColumn lhsQueryColumn = lhsTableColumn.getColumn();
            if (useAliasOnBothSides && useAliasOnLeftSide) {
               columnSet.append(lhsTableColumn.getTable().getAlias());
               columnSet.append(SQL_ALIAS_SEPERATOR);
            }
            columnSet.append(createColumnRepresentation(lhsTableColumn.getColumn()));
            if (QueryConditionOperandType.TABLE_COLUMN.equals(conditionEntity.getOperandType())) {
               columnSet.append(SQL_SPACE_DELIMITER);
               columnSet.append(JOIN_CONDITION_OPERATOR);
               columnSet.append(SQL_SPACE_DELIMITER);
               QueryTableColumn queryTableColumn = conditionEntity.getRhsTableColumns().get(0);
               if (useAliasOnBothSides) {
                  columnSet.append(queryTableColumn.getTable().getAlias());
                  columnSet.append(SQL_ALIAS_SEPERATOR);
               }
               columnSet.append(createColumnRepresentation(queryTableColumn.getColumn()));
            } else {
               columnSet.append(SQL_SPACE_DELIMITER);
               columnSet.append(conditionEntity.getOperator().getValue());
               columnSet.append(SQL_SPACE_DELIMITER);
               List<String> rhsValues = new ArrayList<String>();
               for (QueryValue queryValue : conditionEntity.getRhsValues()) {
                  rhsValues.add(getValueRepresentationByDataType(lhsQueryColumn.getDataType(), queryValue.getValue()));
               }
               // not all the operators are handled
               if (OperatorType.IN.equals(conditionEntity.getOperator())) {
                  columnSet.append(SQL_SUBQUERY_START_WRAPPER);
                  columnSet.append(ExecueCoreUtil.joinCollection(rhsValues));
                  columnSet.append(SQL_SUBQUERY_END_WRAPPER);
               } else {
                  columnSet.append(rhsValues.get(0));
               }
            }
            conditions.add(columnSet.toString());
         }
      }
      return conditions;
   }

   protected List<String> buildLhsConditions (List<ConditionEntity> conditionEntities) {
      List<String> lhsConditions = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionNotEmpty(conditionEntities)) {
         for (ConditionEntity conditionEntity : conditionEntities) {
            StringBuilder columnSet = new StringBuilder();
            QueryTableColumn lhsTableColumn = conditionEntity.getLhsTableColumn();
            columnSet.append(lhsTableColumn.getTable().getAlias());
            columnSet.append(SQL_ALIAS_SEPERATOR);
            columnSet.append(createColumnRepresentation(lhsTableColumn.getColumn()));
            lhsConditions.add(columnSet.toString());
         }
      }
      return lhsConditions;
   }

   protected List<String> buildRhsConditions (List<ConditionEntity> conditionEntities) {
      List<String> rhsConditions = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionNotEmpty(conditionEntities)) {
         for (ConditionEntity conditionEntity : conditionEntities) {
            StringBuilder columnSet = new StringBuilder();
            QueryTableColumn lhsTableColumn = conditionEntity.getLhsTableColumn();
            QueryColumn lhsQueryColumn = lhsTableColumn.getColumn();
            if (QueryConditionOperandType.TABLE_COLUMN.equals(conditionEntity.getOperandType())) {
               QueryTableColumn queryTableColumn = conditionEntity.getRhsTableColumns().get(0);
               columnSet.append(queryTableColumn.getTable().getAlias());
               columnSet.append(SQL_ALIAS_SEPERATOR);
               columnSet.append(createColumnRepresentation(queryTableColumn.getColumn()));
            } else {
               QueryValue queryValue = conditionEntity.getRhsValues().get(0);
               columnSet.append(getValueRepresentationByDataType(lhsQueryColumn.getDataType(), queryValue.getValue()));
            }
            rhsConditions.add(columnSet.toString());
         }
      }
      return rhsConditions;
   }

   protected String getTableClause (QueryTable queryTable) {
      return getTableClause(queryTable, true);
   }

   protected String getTableClause (QueryTable queryTable, boolean appendAlias) {
      StringBuilder tableClause = new StringBuilder();
      tableClause.append(createTableRepresentation(queryTable));
      if (appendAlias) {
         tableClause.append(SQL_SPACE_DELIMITER);
         tableClause.append(queryTable.getAlias());
      }
      return tableClause.toString();
   }

   public String getValueRepresentationByDataType (DataType dataType, String value) {
      String valueRepresenation = value;
      switch (dataType) {
         case NUMBER:
         case LARGE_INTEGER:
         case INT:
            valueRepresenation = value;
            break;
         case CHARACTER:
         case STRING:
         case TIME:
         case DATE:
         case DATETIME:
            valueRepresenation = QUOTE + value + QUOTE;
            break;
      }
      return valueRepresenation;
   }

   protected String getNullValueRepresentationByDataType (DataType dataType) {
      String valueRepresenation = NULL_VALUE_STRING_REPRESENTATION;
      switch (dataType) {
         case NUMBER:
         case LARGE_INTEGER:
         case INT:
            valueRepresenation = NULL_VALUE_NUMBER_REPRESENTATION;
            break;
         case CHARACTER:
         case STRING:
         case TIME:
         case DATE:
         case DATETIME:
            valueRepresenation = QUOTE + NULL_VALUE_STRING_REPRESENTATION + QUOTE;
            break;
      }
      return valueRepresenation;
   }

   public String createSelectStatement (QueryTable queryTable, List<QueryColumn> queryColumns, LimitEntity limitEntity) {
      QueryStructure queryStructure = createSelectQueryStructure(queryTable, queryColumns);
      queryStructure.setLimitElement(QueryFormatUtility.getSimpleQueryClauseElement(limitEntity.getStartingNumber()
               + SQL_ENTITY_SEPERATOR + limitEntity.getEndingNumber()));

      // 
      ISQLAdaptor sqlAdaptor = SQLAdaptorFactory.getInstance().getSQLAdaptor(providerTypeValue);
      queryStructure = sqlAdaptor.handleLimitClause(queryStructure);
      String limitSelectQuery = QueryFormatUtility.prepareQueryString(queryStructure);
      return limitSelectQuery;
   }

   private QueryStructure createSelectQueryStructure (QueryTable queryTable, List<QueryColumn> queryColumns) {
      return createSelectQueryStructure(queryTable, queryColumns, false);
   }

   private QueryStructure createSelectQueryStructure (QueryTable queryTable, List<QueryColumn> queryColumns,
            boolean distinct) {

      StringBuilder selectSimpleString = new StringBuilder();
      if (distinct) {
         selectSimpleString.append(SQL_DISTINCT_KEYWORD);
         selectSimpleString.append(SQL_SPACE_DELIMITER);
      }
      selectSimpleString.append(createColumnsCommaSeperatedRepresentation(queryColumns));

      QueryClauseElement simpleQueryClauseElement = QueryFormatUtility.getSimpleQueryClauseElement(selectSimpleString
               .toString());
      List<QueryClauseElement> selectElements = new ArrayList<QueryClauseElement>();
      selectElements.add(simpleQueryClauseElement);

      QueryClauseElement fromClauseElement = QueryFormatUtility
               .getSimpleQueryClauseElement(createTableRepresentation(queryTable.getTableName()));
      List<QueryClauseElement> fromElements = new ArrayList<QueryClauseElement>();
      fromElements.add(fromClauseElement);

      QueryStructure queryStructure = new QueryStructure();
      queryStructure.setSelectElements(selectElements);
      queryStructure.setFromElements(fromElements);
      return queryStructure;
   }

   public String createSelectStatement (QueryTable queryTable, List<QueryColumn> queryColumns) {
      return createSelectStatement(queryTable, queryColumns, false);
   }

   public String createSelectStatement (QueryTable queryTable, List<QueryColumn> queryColumns, boolean distinct) {
      StringBuilder selectStatement = new StringBuilder();
      selectStatement.append(SQL_SELECT_CLAUSE);
      selectStatement.append(SQL_SPACE_DELIMITER);
      if (distinct) {
         selectStatement.append(SQL_SPACE_DELIMITER);
         selectStatement.append(SQL_DISTINCT_KEYWORD);
         selectStatement.append(SQL_SPACE_DELIMITER);
      }
      selectStatement.append(createColumnsCommaSeperatedRepresentation(queryColumns));
      selectStatement.append(SQL_SPACE_DELIMITER);
      selectStatement.append(SQL_JOIN_FROM_CLAUSE);
      selectStatement.append(SQL_SPACE_DELIMITER);
      selectStatement.append(createTableRepresentation(queryTable));
      return selectStatement.toString();
   }

   public String createSelectStatement (QueryTable queryTable, boolean distinct) {
      StringBuilder selectStatement = new StringBuilder();
      selectStatement.append(SQL_SELECT_CLAUSE);
      selectStatement.append(SQL_SPACE_DELIMITER);
      if (distinct) {
         selectStatement.append(SQL_SPACE_DELIMITER);
         selectStatement.append(SQL_DISTINCT_KEYWORD);
         selectStatement.append(SQL_SPACE_DELIMITER);
      }
      selectStatement.append(SQL_WILD_CHAR);
      selectStatement.append(SQL_SPACE_DELIMITER);
      selectStatement.append(SQL_JOIN_FROM_CLAUSE);
      selectStatement.append(SQL_SPACE_DELIMITER);
      selectStatement.append(createTableRepresentation(queryTable));
      return selectStatement.toString();

   }

   public String createSelectStatementWithDateHandling (QueryTable queryTable, List<QueryColumn> sourceQueryColumns,
            List<QueryColumn> destinationQueryColumns) {
      StringBuilder selectStatment = new StringBuilder();
      selectStatment.append(SQL_SELECT_CLAUSE);
      selectStatment.append(SQL_SPACE_DELIMITER);
      selectStatment.append(createColumnsCommaSeperatedRepresentation(sourceQueryColumns, destinationQueryColumns));
      selectStatment.append(SQL_SPACE_DELIMITER);
      selectStatment.append(SQL_JOIN_FROM_CLAUSE);
      selectStatment.append(SQL_SPACE_DELIMITER);
      selectStatment.append(createTableRepresentation(queryTable));
      return selectStatment.toString();
   }

   public abstract SelectQueryInfo createCountNotNullStringToDateStatement (QueryTable queryTable,
            QueryColumn queryColumn, String dateFormat);

   public String createStatementForRandomRecords (QueryTable queryTable, QueryColumn queryColumn, int numRecords) {
      StringBuilder randomRecordsStatement = new StringBuilder();
      randomRecordsStatement.append(SQL_SELECT_CLAUSE);
      randomRecordsStatement.append(SQL_SPACE_DELIMITER);
      randomRecordsStatement.append(createColumnRepresentation(queryColumn));
      randomRecordsStatement.append(SQL_SPACE_DELIMITER);
      randomRecordsStatement.append(SQL_JOIN_FROM_CLAUSE);
      randomRecordsStatement.append(SQL_SPACE_DELIMITER);
      randomRecordsStatement.append(createTableRepresentation(queryTable));
      randomRecordsStatement.append(SQL_SPACE_DELIMITER);
      randomRecordsStatement.append(SQL_WHERE_CLAUSE);
      randomRecordsStatement.append(SQL_SPACE_DELIMITER);
      randomRecordsStatement.append(createColumnRepresentation(queryColumn));
      randomRecordsStatement.append(IS_NOT_NULL_KEYWORD);
      randomRecordsStatement.append(SQL_SPACE_DELIMITER);
      randomRecordsStatement.append(SQL_ORDER_BY_CLAUSE);
      randomRecordsStatement.append(SQL_SPACE_DELIMITER);
      randomRecordsStatement.append(getRandomFunction());
      randomRecordsStatement.append(SQL_SUBQUERY_START_WRAPPER);
      randomRecordsStatement.append(SQL_SUBQUERY_END_WRAPPER);
      randomRecordsStatement.append(SQL_SPACE_DELIMITER);
      randomRecordsStatement.append(SQL_LIMIT_CLAUSE);
      randomRecordsStatement.append(SQL_SPACE_DELIMITER);
      randomRecordsStatement.append(numRecords);
      return randomRecordsStatement.toString();
   }

   public SelectQueryInfo createDateRegexSizeNotNullStatement (QueryTable queryTable, QueryColumn queryColumn,
            String regex, int length) {
      StringBuilder countDateRegexSizeNotNullStatement = new StringBuilder();
      SelectQueryInfo countNotNullRegexStatement = createCountNotNullRegexStatement(queryTable, queryColumn, regex);
      countDateRegexSizeNotNullStatement.append(countNotNullRegexStatement.getSelectQuery());
      countDateRegexSizeNotNullStatement.append(SQL_SPACE_DELIMITER);
      countDateRegexSizeNotNullStatement.append(WHERE_CLAUSE_SEPERATOR);
      countDateRegexSizeNotNullStatement.append(SQL_SPACE_DELIMITER);
      countDateRegexSizeNotNullStatement.append(SQL_LENGTH_FUNCTION);
      countDateRegexSizeNotNullStatement.append(SQL_SUBQUERY_START_WRAPPER);
      countDateRegexSizeNotNullStatement.append(createColumnRepresentation(queryColumn));
      countDateRegexSizeNotNullStatement.append(SQL_SUBQUERY_END_WRAPPER);
      countDateRegexSizeNotNullStatement.append(SQL_SPACE_DELIMITER);
      countDateRegexSizeNotNullStatement.append(GREATER_THAN_EQUAL_OPERATOR);
      countDateRegexSizeNotNullStatement.append(SQL_SPACE_DELIMITER);
      countDateRegexSizeNotNullStatement.append(length);
      return new SelectQueryInfo(countDateRegexSizeNotNullStatement.toString(), countNotNullRegexStatement
               .getCountColumnIndexes());

   }

   public SelectQueryInfo createCountNotNullRegexStatement (QueryTable queryTable, QueryColumn queryColumn, String regex) {
      StringBuilder countSelectStatment = new StringBuilder();
      countSelectStatment.append(SQL_SELECT_CLAUSE);
      countSelectStatment.append(SQL_SPACE_DELIMITER);
      countSelectStatment.append(SQL_COUNT_FUNCTION);
      countSelectStatment.append(SQL_SUBQUERY_START_WRAPPER);
      countSelectStatment.append(createColumnRepresentation(queryColumn));
      countSelectStatment.append(SQL_SUBQUERY_END_WRAPPER);
      countSelectStatment.append(SQL_SPACE_DELIMITER);
      countSelectStatment.append(SQL_JOIN_FROM_CLAUSE);
      countSelectStatment.append(SQL_SPACE_DELIMITER);
      countSelectStatment.append(createTableRepresentation(queryTable));
      countSelectStatment.append(SQL_SPACE_DELIMITER);
      countSelectStatment.append(SQL_WHERE_CLAUSE);
      countSelectStatment.append(SQL_SPACE_DELIMITER);
      countSelectStatment.append(createColumnRepresentation(queryColumn));
      countSelectStatment.append(SQL_SPACE_DELIMITER);
      countSelectStatment.append(IS_NOT_NULL_KEYWORD);
      if (regex != null) {
         countSelectStatment.append(SQL_SPACE_DELIMITER);
         countSelectStatment.append(WHERE_CLAUSE_SEPERATOR);
         countSelectStatment.append(SQL_SPACE_DELIMITER);
         countSelectStatment.append(createColumnRepresentation(queryColumn));
         countSelectStatment.append(SQL_SPACE_DELIMITER);
         countSelectStatment.append(getRegexKeyword());
         countSelectStatment.append(SQL_SPACE_DELIMITER);
         countSelectStatment.append(QUOTE);
         countSelectStatment.append(regex);
         countSelectStatment.append(QUOTE);
      }
      List<Integer> countColumnIndexes = new ArrayList<Integer>();
      countColumnIndexes.add(0);
      return new SelectQueryInfo(countSelectStatment.toString(), countColumnIndexes);
   }

   protected String getRegexKeyword () {
      return SQL_REGEX_KEYWORD;
   }

   protected String getRandomFunction () {
      return SQL_RANDOM_FUNCTION;
   }

   public boolean isAutoIncrementClauseSupported () {
      return SQLAdaptorFactory.getInstance().getSQLAdaptor(providerTypeValue).isAutoIncrementClauseSupported();
   }

   public boolean isMultipleIndexesWithSingleStatementSupported () {
      return SQLAdaptorFactory.getInstance().getSQLAdaptor(providerTypeValue)
               .isMultipleIndexesWithSingleStatementSupported();
   }

   public SelectQueryInfo createStatBasedSelectStatement (QueryTable table, QueryColumn queryColumn, StatType statType) {
      StringBuilder sqlStatement = new StringBuilder();
      sqlStatement.append(SQL_SELECT_CLAUSE);
      sqlStatement.append(SQL_SPACE_DELIMITER);
      sqlStatement.append(statType.getValue());
      sqlStatement.append(SQL_SUBQUERY_START_WRAPPER);
      if (queryColumn.isDistinct() && StatType.COUNT.equals(statType)) {
         sqlStatement.append(SQL_DISTINCT_KEYWORD);
         sqlStatement.append(SQL_SPACE_DELIMITER);
      }
      if (queryColumn.getColumnName().equalsIgnoreCase(SQL_WILD_CHAR) && StatType.COUNT.equals(statType)) {
         sqlStatement.append(SQL_WILD_CHAR);
      } else {
         sqlStatement.append(createColumnRepresentation(queryColumn));
      }
      sqlStatement.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatement.append(SQL_SPACE_DELIMITER);
      sqlStatement.append(SQL_JOIN_FROM_CLAUSE);
      sqlStatement.append(SQL_SPACE_DELIMITER);
      sqlStatement.append(createTableRepresentation(table));
      List<Integer> countColumnIndexes = new ArrayList<Integer>();
      if (StatType.COUNT.equals(statType)) {
         countColumnIndexes.add(0);
      }
      return new SelectQueryInfo(sqlStatement.toString(), countColumnIndexes);
   }

   public SelectQueryInfo createDistinctCountSelectStatementIncludingNullRecords (QueryTable queryTable,
            QueryColumn queryColumn) {
      StringBuilder sqlStatement = new StringBuilder();
      sqlStatement.append(SQL_SELECT_CLAUSE);
      sqlStatement.append(SQL_SPACE_DELIMITER);
      sqlStatement.append(StatType.COUNT);
      sqlStatement.append(SQL_SUBQUERY_START_WRAPPER);
      sqlStatement.append(SQL_DISTINCT_KEYWORD);
      sqlStatement.append(SQL_SPACE_DELIMITER);
      sqlStatement.append(createNullColumnRepresentation(queryColumn));
      sqlStatement.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatement.append(SQL_SPACE_DELIMITER);
      sqlStatement.append(SQL_JOIN_FROM_CLAUSE);
      sqlStatement.append(SQL_SPACE_DELIMITER);
      sqlStatement.append(createTableRepresentation(queryTable));
      List<Integer> countColumnIndexes = new ArrayList<Integer>();
      countColumnIndexes.add(0);
      return new SelectQueryInfo(sqlStatement.toString(), countColumnIndexes);
   }

   private String createNullColumnRepresentation (QueryColumn queryColumn) {
      StringBuilder nullColumnRepresentation = new StringBuilder();
      ISQLAdaptor sqlAdaptor = SQLAdaptorFactory.getInstance().getSQLAdaptor(providerTypeValue);
      nullColumnRepresentation.append(sqlAdaptor.getNullRepresentationFunction(queryColumn.getDataType()));
      nullColumnRepresentation.append(SQL_SUBQUERY_START_WRAPPER);
      nullColumnRepresentation.append(createColumnRepresentation(queryColumn));
      nullColumnRepresentation.append(SQL_ENTITY_SEPERATOR);
      nullColumnRepresentation.append(getNullValueRepresentationByDataType(queryColumn.getDataType()));
      nullColumnRepresentation.append(SQL_SUBQUERY_END_WRAPPER);
      return nullColumnRepresentation.toString();
   }

   public String createSQLStatementForColumnScale (QueryTable queryTable, QueryColumn queryColumn) {
      StringBuilder sqlStatementColumnScale = new StringBuilder();
      sqlStatementColumnScale.append(SQL_SELECT_CLAUSE);
      sqlStatementColumnScale.append(SQL_SPACE_DELIMITER);
      sqlStatementColumnScale.append(getIfNullKeyword());
      sqlStatementColumnScale.append(SQL_SUBQUERY_START_WRAPPER);
      sqlStatementColumnScale.append(SQL_MAX_FUNCTION);
      sqlStatementColumnScale.append(SQL_SUBQUERY_START_WRAPPER);
      sqlStatementColumnScale.append(SQL_LENGTH_FUNCTION);
      sqlStatementColumnScale.append(SQL_SUBQUERY_START_WRAPPER);
      sqlStatementColumnScale.append(SQL_SUBSTR_FUNCTION);
      sqlStatementColumnScale.append(SQL_SUBQUERY_START_WRAPPER);
      sqlStatementColumnScale.append(createColumnRepresentation(queryColumn));
      sqlStatementColumnScale.append(SQL_ENTITY_SEPERATOR);
      sqlStatementColumnScale.append(SQL_INSTR_FUNCTION);
      sqlStatementColumnScale.append(SQL_SUBQUERY_START_WRAPPER);
      sqlStatementColumnScale.append(createColumnRepresentation(queryColumn));
      sqlStatementColumnScale.append(SQL_ENTITY_SEPERATOR);
      sqlStatementColumnScale.append(SQL_DOBULE_QUOTE_ALIAS_SEPERATOR);
      sqlStatementColumnScale.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatementColumnScale.append(SQL_ADDITION_OPERATOR);
      sqlStatementColumnScale.append(1);
      sqlStatementColumnScale.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatementColumnScale.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatementColumnScale.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatementColumnScale.append(SQL_ENTITY_SEPERATOR);
      sqlStatementColumnScale.append(0);
      sqlStatementColumnScale.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatementColumnScale.append(SQL_SPACE_DELIMITER);
      sqlStatementColumnScale.append(SQL_JOIN_FROM_CLAUSE);
      sqlStatementColumnScale.append(SQL_SPACE_DELIMITER);
      sqlStatementColumnScale.append(createTableRepresentation(queryTable));
      sqlStatementColumnScale.append(SQL_SPACE_DELIMITER);
      sqlStatementColumnScale.append(SQL_WHERE_CLAUSE);
      sqlStatementColumnScale.append(SQL_SPACE_DELIMITER);
      sqlStatementColumnScale.append(createColumnRepresentation(queryColumn));
      sqlStatementColumnScale.append(SQL_SPACE_DELIMITER);
      sqlStatementColumnScale.append(SQL_LIKE_KEYWORD);
      sqlStatementColumnScale.append(SQL_SPACE_DELIMITER);
      sqlStatementColumnScale.append(QUOTE);
      sqlStatementColumnScale.append(SQL_WILD_CHAR_EXPRESSION_FOR_DECIMAL);
      sqlStatementColumnScale.append(QUOTE);
      return sqlStatementColumnScale.toString();
   }

   public String createSQLStatementForNonDecimalColumnPrecision (QueryTable queryTable, QueryColumn queryColumn) {
      StringBuilder sqlStatmentNonDecimal = new StringBuilder();
      sqlStatmentNonDecimal.append(SQL_SELECT_CLAUSE);
      sqlStatmentNonDecimal.append(SQL_SPACE_DELIMITER);
      sqlStatmentNonDecimal.append(getIfNullKeyword());
      sqlStatmentNonDecimal.append(SQL_SUBQUERY_START_WRAPPER);
      sqlStatmentNonDecimal.append(SQL_MAX_FUNCTION);
      sqlStatmentNonDecimal.append(SQL_SUBQUERY_START_WRAPPER);
      sqlStatmentNonDecimal.append(SQL_LENGTH_FUNCTION);
      sqlStatmentNonDecimal.append(SQL_SUBQUERY_START_WRAPPER);
      sqlStatmentNonDecimal.append(createColumnRepresentation(queryColumn));
      sqlStatmentNonDecimal.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatmentNonDecimal.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatmentNonDecimal.append(SQL_ENTITY_SEPERATOR);
      sqlStatmentNonDecimal.append(0);
      sqlStatmentNonDecimal.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatmentNonDecimal.append(SQL_SPACE_DELIMITER);
      sqlStatmentNonDecimal.append(SQL_JOIN_FROM_CLAUSE);
      sqlStatmentNonDecimal.append(SQL_SPACE_DELIMITER);
      sqlStatmentNonDecimal.append(createTableRepresentation(queryTable));
      return sqlStatmentNonDecimal.toString();
   }

   public String createSQLStatementForDecimalColumnDecimalDataPrecision (QueryTable queryTable, QueryColumn queryColumn) {
      StringBuilder sqlStatmentDecimalColumnDecimalData = new StringBuilder();
      sqlStatmentDecimalColumnDecimalData.append(SQL_SELECT_CLAUSE);
      sqlStatmentDecimalColumnDecimalData.append(SQL_SPACE_DELIMITER);
      sqlStatmentDecimalColumnDecimalData.append(getIfNullKeyword());
      sqlStatmentDecimalColumnDecimalData.append(SQL_SUBQUERY_START_WRAPPER);
      sqlStatmentDecimalColumnDecimalData.append(SQL_MAX_FUNCTION);
      sqlStatmentDecimalColumnDecimalData.append(SQL_SUBQUERY_START_WRAPPER);
      sqlStatmentDecimalColumnDecimalData.append(SQL_SUBQUERY_START_WRAPPER);
      sqlStatmentDecimalColumnDecimalData.append(SQL_INSTR_FUNCTION);
      sqlStatmentDecimalColumnDecimalData.append(SQL_SUBQUERY_START_WRAPPER);
      sqlStatmentDecimalColumnDecimalData.append(createColumnRepresentation(queryColumn));
      sqlStatmentDecimalColumnDecimalData.append(SQL_ENTITY_SEPERATOR);
      sqlStatmentDecimalColumnDecimalData.append(SQL_DOBULE_QUOTE_ALIAS_SEPERATOR);
      sqlStatmentDecimalColumnDecimalData.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatmentDecimalColumnDecimalData.append(SQL_SPACE_DELIMITER);
      sqlStatmentDecimalColumnDecimalData.append(SQL_MINUS_OPERATOR);
      sqlStatmentDecimalColumnDecimalData.append(1);
      sqlStatmentDecimalColumnDecimalData.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatmentDecimalColumnDecimalData.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatmentDecimalColumnDecimalData.append(SQL_ENTITY_SEPERATOR);
      sqlStatmentDecimalColumnDecimalData.append(0);
      sqlStatmentDecimalColumnDecimalData.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatmentDecimalColumnDecimalData.append(SQL_SPACE_DELIMITER);
      sqlStatmentDecimalColumnDecimalData.append(SQL_JOIN_FROM_CLAUSE);
      sqlStatmentDecimalColumnDecimalData.append(SQL_SPACE_DELIMITER);
      sqlStatmentDecimalColumnDecimalData.append(createTableRepresentation(queryTable));
      sqlStatmentDecimalColumnDecimalData.append(SQL_SPACE_DELIMITER);
      sqlStatmentDecimalColumnDecimalData.append(SQL_WHERE_CLAUSE);
      sqlStatmentDecimalColumnDecimalData.append(SQL_SPACE_DELIMITER);
      sqlStatmentDecimalColumnDecimalData.append(createColumnRepresentation(queryColumn));
      sqlStatmentDecimalColumnDecimalData.append(SQL_SPACE_DELIMITER);
      sqlStatmentDecimalColumnDecimalData.append(SQL_LIKE_KEYWORD);
      sqlStatmentDecimalColumnDecimalData.append(SQL_SPACE_DELIMITER);
      sqlStatmentDecimalColumnDecimalData.append(QUOTE);
      sqlStatmentDecimalColumnDecimalData.append(SQL_WILD_CHAR_EXPRESSION_FOR_DECIMAL);
      sqlStatmentDecimalColumnDecimalData.append(QUOTE);
      return sqlStatmentDecimalColumnDecimalData.toString();
   }

   public String createSQLStatementForDecimalColumnIntegerDataPrecision (QueryTable queryTable, QueryColumn queryColumn) {
      StringBuilder sqlStatmentDecimalColumnIntegerData = new StringBuilder();
      sqlStatmentDecimalColumnIntegerData.append(SQL_SELECT_CLAUSE);
      sqlStatmentDecimalColumnIntegerData.append(SQL_SPACE_DELIMITER);
      sqlStatmentDecimalColumnIntegerData.append(getIfNullKeyword());
      sqlStatmentDecimalColumnIntegerData.append(SQL_SUBQUERY_START_WRAPPER);
      sqlStatmentDecimalColumnIntegerData.append(SQL_MAX_FUNCTION);
      sqlStatmentDecimalColumnIntegerData.append(SQL_SUBQUERY_START_WRAPPER);
      sqlStatmentDecimalColumnIntegerData.append(SQL_LENGTH_FUNCTION);
      sqlStatmentDecimalColumnIntegerData.append(SQL_SUBQUERY_START_WRAPPER);
      sqlStatmentDecimalColumnIntegerData.append(createColumnRepresentation(queryColumn));
      sqlStatmentDecimalColumnIntegerData.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatmentDecimalColumnIntegerData.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatmentDecimalColumnIntegerData.append(SQL_ENTITY_SEPERATOR);
      sqlStatmentDecimalColumnIntegerData.append(0);
      sqlStatmentDecimalColumnIntegerData.append(SQL_SUBQUERY_END_WRAPPER);
      sqlStatmentDecimalColumnIntegerData.append(SQL_SPACE_DELIMITER);
      sqlStatmentDecimalColumnIntegerData.append(SQL_JOIN_FROM_CLAUSE);
      sqlStatmentDecimalColumnIntegerData.append(SQL_SPACE_DELIMITER);
      sqlStatmentDecimalColumnIntegerData.append(createTableRepresentation(queryTable));
      sqlStatmentDecimalColumnIntegerData.append(SQL_SPACE_DELIMITER);
      sqlStatmentDecimalColumnIntegerData.append(SQL_WHERE_CLAUSE);
      sqlStatmentDecimalColumnIntegerData.append(SQL_SPACE_DELIMITER);
      sqlStatmentDecimalColumnIntegerData.append(createColumnRepresentation(queryColumn));
      sqlStatmentDecimalColumnIntegerData.append(SQL_SPACE_DELIMITER);
      sqlStatmentDecimalColumnIntegerData.append(SQL_NOT_LIKE_KEYWORD);
      sqlStatmentDecimalColumnIntegerData.append(SQL_SPACE_DELIMITER);
      sqlStatmentDecimalColumnIntegerData.append(QUOTE);
      sqlStatmentDecimalColumnIntegerData.append(SQL_WILD_CHAR_EXPRESSION_FOR_DECIMAL);
      sqlStatmentDecimalColumnIntegerData.append(QUOTE);
      return sqlStatmentDecimalColumnIntegerData.toString();
   }

   public String createMaximumDataLengthSelectStatement (QueryTable queryTable, QueryColumn queryColumn) {
      StringBuilder maxDataLengthSelectStatement = new StringBuilder();
      maxDataLengthSelectStatement.append(SQL_SELECT_CLAUSE);
      maxDataLengthSelectStatement.append(SQL_SPACE_DELIMITER);
      maxDataLengthSelectStatement.append(SQL_MAX_FUNCTION);
      maxDataLengthSelectStatement.append(SQL_SUBQUERY_START_WRAPPER);
      maxDataLengthSelectStatement.append(getSQLLengthFunction());
      maxDataLengthSelectStatement.append(SQL_SUBQUERY_START_WRAPPER);
      maxDataLengthSelectStatement.append(createColumnRepresentation(queryColumn));
      maxDataLengthSelectStatement.append(SQL_SUBQUERY_END_WRAPPER);
      maxDataLengthSelectStatement.append(SQL_SUBQUERY_END_WRAPPER);
      maxDataLengthSelectStatement.append(SQL_SPACE_DELIMITER);
      maxDataLengthSelectStatement.append(SQL_JOIN_FROM_CLAUSE);
      maxDataLengthSelectStatement.append(SQL_SPACE_DELIMITER);
      maxDataLengthSelectStatement.append(createTableRepresentation(queryTable));
      return maxDataLengthSelectStatement.toString();
   }

   protected String getSQLLengthFunction () {
      return SQL_LENGTH_FUNCTION;
   }

   protected String createColumnsCommaSeperatedRepresentation (List<QueryColumn> queryColumns) {
      List<String> columnNames = new ArrayList<String>();
      for (QueryColumn queryColumn : queryColumns) {
         columnNames.add(createColumnRepresentation(queryColumn));
      }
      return ExecueCoreUtil.joinCollection(columnNames);
   }

   private String createColumnsCommaSeperatedRepresentation (List<QueryColumn> queryColumns,
            boolean isColumnPlaceholders) {
      List<String> columnNames = new ArrayList<String>();
      for (QueryColumn queryColumn : queryColumns) {
         if (isColumnPlaceholders) {
            columnNames.add(queryColumn.getColumnName());
         } else {
            columnNames.add(createColumnRepresentation(queryColumn));
         }
      }
      return ExecueCoreUtil.joinCollection(columnNames);
   }

   protected abstract Object createColumnsCommaSeperatedRepresentationWithDateHandling (List<QueryColumn> queryColumns);

   protected abstract Object createColumnsCommaSeperatedRepresentation (List<QueryColumn> sourceQueryColumns,
            List<QueryColumn> destinationQueryColumns);

   protected String createColumnDefinition (QueryColumn column) {
      StringBuilder columnDefiniton = new StringBuilder();
      columnDefiniton.append(createColumnRepresentation(column));
      columnDefiniton.append(SQL_SPACE_DELIMITER);
      columnDefiniton.append(getProviderSpecificDataType(column.getDataType()).getValue());
      if (!DataType.TIME.equals(column.getDataType()) && !DataType.DATE.equals(column.getDataType())
               && !DataType.DATETIME.equals(column.getDataType())) {
         appendColumnLengthClause(columnDefiniton, column);
      }
      String notNullClause = getNotNullAndDefaultConditions(column);
      if (!StringUtils.isBlank(notNullClause)) {
         columnDefiniton.append(SQL_SPACE_DELIMITER);
         columnDefiniton.append(notNullClause);
      }
      if (column.isAutoIncrement()) {
         ISQLAdaptor sqlAdaptor = SQLAdaptorFactory.getInstance().getSQLAdaptor(providerTypeValue);
         String autoIncrementClause = sqlAdaptor.getAutoIncrementClause();
         if (!StringUtils.isBlank(autoIncrementClause)) {
            columnDefiniton.append(SQL_SPACE_DELIMITER);
            columnDefiniton.append(autoIncrementClause);
         }
      }
      return columnDefiniton.toString();
   }

   protected void appendColumnLengthClause (StringBuilder columnDefiniton, QueryColumn column) {
      if (column.getPrecision() > 0 && applyPrecisionBasedOnDataType(column.getDataType())) {
         columnDefiniton.append(SQL_SUBQUERY_START_WRAPPER);
         columnDefiniton.append(column.getPrecision());
         if (column.getScale() > 0) {
            columnDefiniton.append(SQL_ENTITY_SEPERATOR);
            columnDefiniton.append(column.getScale());
         }
         columnDefiniton.append(SQL_SUBQUERY_END_WRAPPER);
      }

   }

   protected boolean applyPrecisionBasedOnDataType (DataType dataType) {
      return true;
   }

   protected String getNotNullAndDefaultConditions (QueryColumn column) {
      StringBuilder notNullDefaultCondition = new StringBuilder();
      if (!column.isNullable()) {
         notNullDefaultCondition.append(NOT_NULL_KEYWORD);
         notNullDefaultCondition.append(SQL_SPACE_DELIMITER);
      }
      if (!StringUtils.isBlank(column.getDefaultValue())) {
         String defaultValue = DEFAULT_VALUE_STRING;
         defaultValue = defaultValue.replaceAll(DEFAULT_VALUE_PLACE_HOLDER, column.getDefaultValue());
         notNullDefaultCondition.append(defaultValue);

      }
      return notNullDefaultCondition.toString().trim();
   }

   protected String createTableRepresentation (String tableName) {
      return createObjectNameRepresentation(tableName);
   }

   protected String createObjectNameRepresentation (String objectName) {
      return objectName;
   }

   /**
    * The table representation can differ for each of the database, so the respective UtilService has to override this
    * method for its specific implementation
    */
   protected String createTableRepresentation (QueryTable queryTable) {
      String tableName = queryTable.getTableName();
      if (CheckType.YES.equals(queryTable.getVirtual())) {
         tableName = queryTable.getActualName();
      }
      return createTableRepresentation(tableName);
   }

   protected String createColumnRepresentation (QueryColumn queryColumn) {
      return createObjectNameRepresentation(queryColumn.getColumnName());
   }

   protected DataType getProviderSpecificDataType (DataType dataType) {
      return dataType;
   }

   public SelectQueryInfo createCountNotNullRecordsLengthStatement (QueryTable queryTable, QueryColumn column,
            int recordLength) {
      StringBuilder countRecordsLengthStmt = new StringBuilder();
      countRecordsLengthStmt.append(SQL_SELECT_CLAUSE);
      countRecordsLengthStmt.append(SQL_SPACE_DELIMITER);
      countRecordsLengthStmt.append(SQL_COUNT_FUNCTION);
      countRecordsLengthStmt.append(SQL_FUNCTION_START_WRAPPER);
      countRecordsLengthStmt.append(SQL_WILD_CHAR);
      countRecordsLengthStmt.append(SQL_FUNCTION_END_WRAPPER);
      countRecordsLengthStmt.append(SQL_SPACE_DELIMITER);
      countRecordsLengthStmt.append(SQL_JOIN_FROM_CLAUSE);
      countRecordsLengthStmt.append(SQL_SPACE_DELIMITER);
      countRecordsLengthStmt.append(createTableRepresentation(queryTable));
      countRecordsLengthStmt.append(SQL_SPACE_DELIMITER);
      countRecordsLengthStmt.append(SQL_WHERE_CLAUSE);
      countRecordsLengthStmt.append(SQL_SPACE_DELIMITER);
      countRecordsLengthStmt.append(SQL_LENGTH_FUNCTION);
      countRecordsLengthStmt.append(SQL_FUNCTION_START_WRAPPER);
      countRecordsLengthStmt.append(createColumnRepresentation(column));
      countRecordsLengthStmt.append(SQL_FUNCTION_END_WRAPPER);
      countRecordsLengthStmt.append(SQL_SPACE_DELIMITER);
      countRecordsLengthStmt.append(JOIN_CONDITION_OPERATOR);
      countRecordsLengthStmt.append(SQL_SPACE_DELIMITER);
      countRecordsLengthStmt.append(recordLength);
      countRecordsLengthStmt.append(SQL_SPACE_DELIMITER);
      countRecordsLengthStmt.append(WHERE_CLAUSE_SEPERATOR);
      countRecordsLengthStmt.append(SQL_SPACE_DELIMITER);
      countRecordsLengthStmt.append(createColumnRepresentation(column));
      countRecordsLengthStmt.append(SQL_SPACE_DELIMITER);
      countRecordsLengthStmt.append(IS_NOT_NULL_KEYWORD);
      List<Integer> countColumnIndexes = new ArrayList<Integer>();
      countColumnIndexes.add(0);
      return new SelectQueryInfo(countRecordsLengthStmt.toString(), countColumnIndexes);
   }

   @Override
   public String createTableUsingExistingTableWithoutData (QueryTable newQueryTable, QueryTable existingQueryTable) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(CREATE_TABLE_COMMAND);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(createTableRepresentation(newQueryTable));
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(SQL_ALIAS);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(createSelectStatement(existingQueryTable, false));
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(SQL_WHERE_CLAUSE);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append("1");
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(JOIN_CONDITION_OPERATOR);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append("2");
      return stringBuilder.toString();
   }

   /**
    * @return the providerTypeValue
    */
   public Integer getProviderTypeValue () {
      return providerTypeValue;
   }

   /**
    * @param providerTypeValue
    *           the providerTypeValue to set
    */
   public void setProviderTypeValue (Integer providerTypeValue) {
      this.providerTypeValue = providerTypeValue;
   }

   protected String getIfNullKeyword () {
      return SQL_IFNULL_FUNCTION;
   }

}
