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


package com.execue.util.querygen.impl.postgres;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.DataType;
import com.execue.core.constants.ISQLQueryConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.util.querygen.impl.SQLQueryGenerationUtilServiceImpl;

/**
 * @author Prasanna
 */
public class PostgresQueryGenerationUtilServiceImpl extends SQLQueryGenerationUtilServiceImpl implements
         ISQLQueryConstants {

   private static final String NUMERIC_DATATYPE_FORMAT_STRING = "9";

   @Override
   public SelectQueryInfo createPingSelectStatement (QueryTable queryTable) {
      StringBuilder selectStatment = new StringBuilder();
      selectStatment.append(SQL_SELECT_CLAUSE);
      selectStatment.append(SQL_SPACE_DELIMITER);
      selectStatment.append(SQL_COUNT_FUNCTION);
      selectStatment.append(SQL_FUNCTION_START_WRAPPER);
      selectStatment.append("1");
      selectStatment.append(SQL_FUNCTION_END_WRAPPER);
      selectStatment.append(SQL_SPACE_DELIMITER);
      selectStatment.append(SQL_JOIN_FROM_CLAUSE);
      selectStatment.append(SQL_SPACE_DELIMITER);
      selectStatment.append(createTableRepresentation(queryTable));
      selectStatment.append(SQL_SPACE_DELIMITER);
      selectStatment.append(SQL_LIMIT_CLAUSE);
      selectStatment.append(SQL_SPACE_DELIMITER);
      selectStatment.append(1);
      selectStatment.append(SQL_SPACE_DELIMITER);
      selectStatment.append(POSTGRES_OFFSET_CLAUSE);
      selectStatment.append(SQL_SPACE_DELIMITER);
      selectStatment.append(0);
      List<Integer> countColumnIndexes = new ArrayList<Integer>();
      countColumnIndexes.add(0);
      return new SelectQueryInfo(selectStatment.toString(), countColumnIndexes);

   }

   @Override
   protected Object createColumnsCommaSeperatedRepresentationWithDateHandling (List<QueryColumn> queryColumns) {
      return null;
   }

   @Override
   protected Object createColumnsCommaSeperatedRepresentation (List<QueryColumn> sourceQueryColumns,
            List<QueryColumn> destinationQueryColumns) {
      List<String> columnNames = new ArrayList<String>();
      for (QueryColumn srcQueryColumn : sourceQueryColumns) {
         columnNames.add(getFormattedColumnNameRepresentation(sourceQueryColumns.indexOf(srcQueryColumn),
                  srcQueryColumn, destinationQueryColumns));
      }
      return ExecueCoreUtil.joinCollection(columnNames);
   }

   private QueryColumn getDestinationColumnWithDiffDataType (int index, QueryColumn sourceQueryColumn,
            List<QueryColumn> destinationQueryColumns) {
      QueryColumn destinationQueryColumn = null;
      if (!destinationQueryColumns.get(index).getDataType().equals(sourceQueryColumn.getDataType())) {
         destinationQueryColumn = destinationQueryColumns.get(index);
      }
      return destinationQueryColumn;
   }

   private String getFormattedColumnNameRepresentation (int sourceQueryColumnIndex, QueryColumn sourceQueryColumn,
            List<QueryColumn> destinationQueryColumns) {
      String columnRepresentation = createColumnRepresentation(sourceQueryColumn);
      boolean dateTimeDataType = false;
      if (DataType.DATETIME.equals(sourceQueryColumn.getDataType())) {
         columnRepresentation = POSTGRES_TO_TIMESTAMP_FUNCTION + SQL_SUBQUERY_START_WRAPPER + columnRepresentation
                  + SQL_ENTITY_SEPERATOR + QUOTE + sourceQueryColumn.getDataFormat() + QUOTE + SQL_SUBQUERY_END_WRAPPER;
         dateTimeDataType = true;
      } else if (DataType.DATE.equals(sourceQueryColumn.getDataType())) {
         columnRepresentation = POSTGRES_STRING_TO_DATE_FUNCTION + SQL_SUBQUERY_START_WRAPPER + columnRepresentation
                  + SQL_ENTITY_SEPERATOR + QUOTE + sourceQueryColumn.getDataFormat() + QUOTE + SQL_SUBQUERY_END_WRAPPER;
         dateTimeDataType = true;
      }
      QueryColumn destinationQueryColumn = getDestinationColumnWithDiffDataType(sourceQueryColumnIndex,
               sourceQueryColumn, destinationQueryColumns);
      if (destinationQueryColumn != null && !dateTimeDataType) {
         if (destinationQueryColumn.getDataType() == DataType.INT
                  || destinationQueryColumn.getDataType() == DataType.LARGE_INTEGER) {
            columnRepresentation = POSTGRES_TO_NUMBER_FUNCTION + SQL_SUBQUERY_START_WRAPPER + columnRepresentation
                     + SQL_ENTITY_SEPERATOR + QUOTE
                     + getFormateStringForIntegerDataType(destinationQueryColumn.getPrecision()) + QUOTE
                     + SQL_SUBQUERY_END_WRAPPER;
         } else if (destinationQueryColumn.getDataType() == DataType.NUMBER) {
            columnRepresentation = POSTGRES_TO_NUMBER_FUNCTION
                     + SQL_SUBQUERY_START_WRAPPER
                     + columnRepresentation
                     + SQL_ENTITY_SEPERATOR
                     + QUOTE
                     + getFormateStringForNumericDataType(destinationQueryColumn.getPrecision(), destinationQueryColumn
                              .getScale()) + QUOTE + SQL_SUBQUERY_END_WRAPPER;
         }
      }
      return columnRepresentation;
   }

   private String getFormateStringForIntegerDataType (int precision) {
      StringBuilder builder = new StringBuilder();
      int index = 1;
      while (index <= precision) {
         builder.append(NUMERIC_DATATYPE_FORMAT_STRING);
         index++;
      }
      return builder.toString();
   }

   private String getFormateStringForNumericDataType (int precision, int scale) {
      StringBuilder builder = new StringBuilder();
      int index = 1;
      while (index <= precision) {
         builder.append(NUMERIC_DATATYPE_FORMAT_STRING);
         index++;
      }
      builder.append(SQL_ALIAS_SEPERATOR);
      index = 1;
      while (index <= scale) {
         builder.append(NUMERIC_DATATYPE_FORMAT_STRING);
         index++;
      }
      return builder.toString();
   }

   @Override
   public SelectQueryInfo createCountNotNullStringToDateStatement (QueryTable queryTable, QueryColumn queryColumn,
            String dateFormat) {
      StringBuilder countNotNullStringToDateStatement = new StringBuilder();
      countNotNullStringToDateStatement.append(SQL_SELECT_CLAUSE);
      countNotNullStringToDateStatement.append(SQL_SPACE_DELIMITER);
      countNotNullStringToDateStatement.append(SQL_COUNT_FUNCTION);
      countNotNullStringToDateStatement.append(SQL_SUBQUERY_START_WRAPPER);
      countNotNullStringToDateStatement.append(POSTGRES_STRING_TO_DATE_FUNCTION);
      countNotNullStringToDateStatement.append(SQL_SUBQUERY_START_WRAPPER);
      countNotNullStringToDateStatement.append(createColumnRepresentation(queryColumn));
      countNotNullStringToDateStatement.append(SQL_ENTITY_SEPERATOR);
      countNotNullStringToDateStatement.append(QUOTE);
      countNotNullStringToDateStatement.append(dateFormat);
      countNotNullStringToDateStatement.append(QUOTE);
      countNotNullStringToDateStatement.append(SQL_SUBQUERY_END_WRAPPER);
      countNotNullStringToDateStatement.append(SQL_SUBQUERY_END_WRAPPER);
      countNotNullStringToDateStatement.append(SQL_SPACE_DELIMITER);
      countNotNullStringToDateStatement.append(SQL_JOIN_FROM_CLAUSE);
      countNotNullStringToDateStatement.append(SQL_SPACE_DELIMITER);
      countNotNullStringToDateStatement.append(createTableRepresentation(queryTable));
      countNotNullStringToDateStatement.append(SQL_SPACE_DELIMITER);
      countNotNullStringToDateStatement.append(SQL_WHERE_CLAUSE);
      countNotNullStringToDateStatement.append(SQL_SPACE_DELIMITER);
      countNotNullStringToDateStatement.append(createColumnRepresentation(queryColumn));
      countNotNullStringToDateStatement.append(SQL_SPACE_DELIMITER);
      countNotNullStringToDateStatement.append(IS_NOT_NULL_KEYWORD);
      List<Integer> countColumnIndexes = new ArrayList<Integer>();
      countColumnIndexes.add(0);
      return new SelectQueryInfo(countNotNullStringToDateStatement.toString(), countColumnIndexes);
   }

   @Override
   public String createIndexPingStatement (String indexName, QueryTable tableName) {
      // select * from pg_class where relname='IDX_SLICE_COUNTEMP_PRODUCTMART';
      StringBuilder indexPingStatement = new StringBuilder();
      indexPingStatement.append(SQL_SELECT_CLAUSE);
      indexPingStatement.append(SQL_SPACE_DELIMITER);
      indexPingStatement.append(SQL_WILD_CHAR);
      indexPingStatement.append(SQL_SPACE_DELIMITER);
      indexPingStatement.append(SQL_JOIN_FROM_CLAUSE);
      indexPingStatement.append(SQL_SPACE_DELIMITER);
      indexPingStatement.append(POSTGRES_PG_CLASS_TABLE);
      indexPingStatement.append(SQL_SPACE_DELIMITER);
      indexPingStatement.append(SQL_WHERE_CLAUSE);
      indexPingStatement.append(SQL_SPACE_DELIMITER);
      indexPingStatement.append(POSTGRES_REL_NAME);
      indexPingStatement.append(SQL_SPACE_DELIMITER);
      indexPingStatement.append("=");
      indexPingStatement.append(SQL_SPACE_DELIMITER);
      indexPingStatement.append(QUOTE);
      indexPingStatement.append(indexName);
      indexPingStatement.append(QUOTE);
      return indexPingStatement.toString();
   }

   @Override
   protected String getRegexKeyword () {
      return POSTGRES_REGEX_KEYWORD;
   }

   @Override
   protected String getIfNullKeyword () {
      return POSTGRES_IF_NULL_KEYWORD;
   }

   @Override
   protected String createColumnDefinition (QueryColumn column) {
      StringBuilder columnDefiniton = new StringBuilder();
      columnDefiniton.append(createColumnRepresentation(column));
      columnDefiniton.append(SQL_SPACE_DELIMITER);
      if (column.isAutoIncrement()) {
         columnDefiniton.append(SQL_SPACE_DELIMITER);
         columnDefiniton.append(POSTGRES_AUTO_INCREMENT_VALUE);
      } else {
         columnDefiniton.append(getProviderSpecificDataType(column.getDataType()).getValue());
         if (!DataType.TIME.equals(column.getDataType()) && !DataType.DATE.equals(column.getDataType())
                  && !DataType.DATETIME.equals(column.getDataType()) && !DataType.TEXT.equals(column.getDataType())) {
            appendColumnLengthClause(columnDefiniton, column);
         }
      }
      String notNullClause = getNotNullAndDefaultConditions(column);
      if (!StringUtils.isBlank(notNullClause)) {
         columnDefiniton.append(SQL_SPACE_DELIMITER);
         columnDefiniton.append(notNullClause);
      }
      return columnDefiniton.toString();
   }

   @Override
   protected void appendColumnLengthClause (StringBuilder columnDefiniton, QueryColumn column) {
      if (!DataType.INT.equals(column.getDataType()) && !DataType.LARGE_INTEGER.equals(column.getDataType())) {
         super.appendColumnLengthClause(columnDefiniton, column);
      }
   }

   @Override
   protected String createObjectNameRepresentation (String objectName) {
      return DOUBLE_QUOTE + super.createObjectNameRepresentation(objectName) + DOUBLE_QUOTE;
   }

   @Override
   public String createUpdateStatement (QueryTable targetTable, List<QueryTable> sourceTables,
            List<ConditionEntity> setConditions, List<ConditionEntity> whereConditions) {

      StringBuilder updateStatement = new StringBuilder();
      List<String> setConditionsList = buildSetConditions(setConditions);
      List<String> whereConditionsList = buildWhereConditions(whereConditions);

      if (ExecueCoreUtil.isCollectionEmpty(sourceTables)) {
         // Example 1: When we update just target table without any joins
         // update account2_fx_65 FHY11 set sfactor=20.000 where FHY11.account_id=351764
         updateStatement.append(UPDATE_TABLE_KEYWORD);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(DOUBLE_QUOTE + targetTable.getTableName() + DOUBLE_QUOTE);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(targetTable.getAlias());
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

      } else {
         // Example 2: When we update target table using join on one source table
         // UPDATE "BILLING2_FX_0" ULD27 SET "SFACTOR" = SNS95."SFACTOR" FROM "POPULATION_FX_DFSDFSDFSD" SNS95 WHERE
         // ULD27."ACCOUNT_ID" = SNS95."ACCOUNT_ID"

         List<String> sourceTablesList = new ArrayList<String>();
         if (ExecueCoreUtil.isCollectionNotEmpty(sourceTables)) {
            for (QueryTable queryTable : sourceTables) {
               sourceTablesList.add(getTableClause(queryTable));
            }
         }

         updateStatement.append(UPDATE_TABLE_KEYWORD);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(DOUBLE_QUOTE + targetTable.getTableName() + DOUBLE_QUOTE);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(targetTable.getAlias());
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(SQL_SET_KEYWORD);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(ExecueCoreUtil.joinCollection(setConditionsList));
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(SQL_JOIN_FROM_CLAUSE);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(ExecueCoreUtil.joinCollection(sourceTablesList));

         if (ExecueCoreUtil.isCollectionNotEmpty(whereConditionsList)) {
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(SQL_WHERE_CLAUSE);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(ExecueCoreUtil.joinCollection(whereConditionsList, SQL_SPACE_DELIMITER
                     + WHERE_CLAUSE_SEPERATOR + SQL_SPACE_DELIMITER));
         }

      }
      return updateStatement.toString();
   }

   @Override
   public String getRandomFunction () {
      return SQL_POSTGRES_RAND_FUNCTION;
   }

   @Override
   protected List<String> buildSetConditions (List<ConditionEntity> conditionEntities) {
      return buildConditions(conditionEntities, true, false);
   }

   @Override
   protected DataType getProviderSpecificDataType (DataType dataType) {
      DataType providerDataType = dataType;
      if (DataType.DATETIME.equals(dataType)) {
         providerDataType = DataType.TIMESTAMP;
      }
      return providerDataType;
   }

}
