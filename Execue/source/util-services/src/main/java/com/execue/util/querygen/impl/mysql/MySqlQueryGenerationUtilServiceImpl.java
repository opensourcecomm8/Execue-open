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


package com.execue.util.querygen.impl.mysql;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SQLIndex;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.DataType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.constants.ISQLQueryConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.util.querygen.impl.SQLQueryGenerationUtilServiceImpl;

/**
 * MySql specific query generation utilities come here
 * 
 * @author Vishay
 * @version 1.0
 * @since 13/08/09
 */
public class MySqlQueryGenerationUtilServiceImpl extends SQLQueryGenerationUtilServiceImpl implements
         ISQLQueryConstants {

   @Override
   protected String createObjectNameRepresentation (String objectName) {
      StringBuilder objectNameRepresentation = new StringBuilder();
      objectNameRepresentation.append(SQL_COLUMN_BINDER).append(objectName).append(SQL_COLUMN_BINDER);
      return objectNameRepresentation.toString();
   }

   @Override
   public String createTableCreateStatement (QueryTable queryTable, List<QueryColumn> columns) {
      StringBuilder sb = new StringBuilder();
      sb.append(super.createTableCreateStatement(queryTable, columns));
      sb.append(SQL_SPACE_DELIMITER);
      sb.append(SQL_MYSQL_ENGINE_DEFAULT);
      return sb.toString();
   }

   @Override
   public String createIndexPingStatement (String indexName, QueryTable queryTable) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(MYSQL_SHOW_COMMAND);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(MYSQL_SYSTEM_INDEX_NAME);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(SQL_JOIN_FROM_CLAUSE);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(createTableRepresentation(queryTable));
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(SQL_WHERE_CLAUSE);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(MYSQL_SYSTEM_INDEX_COLUMN_NAME);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(JOIN_CONDITION_OPERATOR);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(QUOTE);
      stringBuilder.append(indexName);
      stringBuilder.append(QUOTE);
      return stringBuilder.toString();
   }

   @Override
   public String createMultipleIndexsWithSingleStatement (List<SQLIndex> indexes) {

      StringBuilder indexStatement = new StringBuilder();
      indexStatement.append(ALTER_TABLE_COMMAND);
      indexStatement.append(SQL_SPACE_DELIMITER);
      indexStatement.append(createTableRepresentation(indexes.get(0).getTableName()));
      indexStatement.append(SQL_SPACE_DELIMITER);
      for (SQLIndex index : indexes) {
         indexStatement.append(ADD_COLUMN_KEYWORD);
         indexStatement.append(SQL_SPACE_DELIMITER);
         if (index.isUnique()) {
            indexStatement.append(INDEX_UNIQUE_KEYWORD);
            indexStatement.append(SQL_SPACE_DELIMITER);
         }
         indexStatement.append(INDEX_KEYWORD);
         indexStatement.append(SQL_SPACE_DELIMITER);
         indexStatement.append(createObjectNameRepresentation(index.getName()));
         indexStatement.append(SQL_SPACE_DELIMITER);
         indexStatement.append(SQL_SUBQUERY_START_WRAPPER);
         indexStatement.append(createColumnsCommaSeperatedRepresentation(ExecueBeanManagementUtil
                  .prepareQueryColumnsFromColumnNames(index.getColumnNames())));
         indexStatement.append(SQL_SUBQUERY_END_WRAPPER);
         indexStatement.append(SQL_ENTITY_SEPERATOR);
         indexStatement.append(SQL_SPACE_DELIMITER);
      }
      indexStatement.deleteCharAt(indexStatement.length()
               - (SQL_ENTITY_SEPERATOR.length() + SQL_SPACE_DELIMITER.length()));

      return indexStatement.toString();
   }

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
      selectStatment.append(0);
      selectStatment.append(SQL_ENTITY_SEPERATOR);
      selectStatment.append(1);
      List<Integer> countColumnIndexes = new ArrayList<Integer>();
      countColumnIndexes.add(0);
      return new SelectQueryInfo(selectStatment.toString(), countColumnIndexes);
   }

   @Override
   protected Object createColumnsCommaSeperatedRepresentationWithDateHandling (List<QueryColumn> queryColumns) {
      List<String> columnNames = new ArrayList<String>();
      for (QueryColumn queryColumn : queryColumns) {
         String columnRepresentation = createColumnRepresentation(queryColumn);
         if (DataType.DATETIME.equals(queryColumn.getDataType()) || DataType.DATE.equals(queryColumn.getDataType())) {
            columnRepresentation = SQL_MYSQL_STRING_TO_DATE_FUNCTION + SQL_SUBQUERY_START_WRAPPER
                     + columnRepresentation + SQL_ENTITY_SEPERATOR + QUOTE + queryColumn.getDataFormat() + QUOTE
                     + SQL_SUBQUERY_END_WRAPPER;
         }
         columnNames.add(columnRepresentation);
      }
      return ExecueCoreUtil.joinCollection(columnNames);
   }

   @Override
   public SelectQueryInfo createCountNotNullStringToDateStatement (QueryTable queryTable, QueryColumn queryColumn,
            String dateFormat) {
      StringBuilder countNotNullStringToDateStatement = new StringBuilder();
      countNotNullStringToDateStatement.append(SQL_SELECT_CLAUSE);
      countNotNullStringToDateStatement.append(SQL_SPACE_DELIMITER);
      countNotNullStringToDateStatement.append(SQL_COUNT_FUNCTION);
      countNotNullStringToDateStatement.append(SQL_SUBQUERY_START_WRAPPER);
      countNotNullStringToDateStatement.append(SQL_MYSQL_STRING_TO_DATE_FUNCTION);
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
   protected Object createColumnsCommaSeperatedRepresentation (List<QueryColumn> sourceQueryColumns,
            List<QueryColumn> destinationQueryColumns) {
      return createColumnsCommaSeperatedRepresentationWithDateHandling(sourceQueryColumns);
   }
}
