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


package com.execue.util.querygen.impl.sas;

import java.util.List;

import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.DataType;
import com.execue.util.querygen.impl.SQLQueryGenerationUtilServiceImpl;

/**
 * SAS specific query generation utilities come here
 * 
 * @author Nitesh
 * @version 1.0
 * @since 25/01/2012
 */
public class SASQueryGenerationUtilServiceImpl extends SQLQueryGenerationUtilServiceImpl {

   @Override
   public String createIndexPingStatement (String indexName, QueryTable queryTable) {
      // StringBuilder stringBuilder = new StringBuilder();
      // stringBuilder.append(SQL_SELECT_CLAUSE);
      // stringBuilder.append(SQL_SPACE_DELIMITER);
      // stringBuilder.append(SQL_WILD_CHAR);
      // stringBuilder.append(SQL_SPACE_DELIMITER);
      // stringBuilder.append(SQL_JOIN_FROM_CLAUSE);
      // stringBuilder.append(SQL_SPACE_DELIMITER);
      // stringBuilder.append(MSSQL_SYSTEM_INDEX_NAME);
      // stringBuilder.append(SQL_SPACE_DELIMITER);
      // stringBuilder.append(SQL_WHERE_CLAUSE);
      // stringBuilder.append(SQL_SPACE_DELIMITER);
      // stringBuilder.append(MSSQL_SYSTEM_INDEX_COLUMN_NAME);
      // stringBuilder.append(SQL_SPACE_DELIMITER);
      // stringBuilder.append(JOIN_CONDITION_OPERATOR);
      // stringBuilder.append(SQL_SPACE_DELIMITER);
      // stringBuilder.append(QUOTE);
      // stringBuilder.append(indexName);
      // stringBuilder.append(QUOTE);
      // return stringBuilder.toString();
      throw new UnsupportedOperationException("Yet to implement in SAS");
   }

   @Override
   protected String createTableRepresentation (QueryTable queryTable) {
      String tableName = super.createTableRepresentation(queryTable);
      StringBuilder sb = new StringBuilder();
      sb.append(queryTable.getOwner().trim()).append(SQL_ALIAS_SEPERATOR).append(tableName.trim());
      return sb.toString();
   }

   @Override
   public String createMaximumDataLengthSelectStatement (QueryTable queryTable, QueryColumn queryColumn) {
      StringBuilder maxDataLengthSelectStatement = new StringBuilder();
      maxDataLengthSelectStatement.append(SQL_SELECT_CLAUSE);
      maxDataLengthSelectStatement.append(SQL_SPACE_DELIMITER);
      maxDataLengthSelectStatement.append(SQL_MAX_FUNCTION);
      maxDataLengthSelectStatement.append(SQL_SUBQUERY_START_WRAPPER);
      maxDataLengthSelectStatement.append(SQL_LENGTH_FUNCTION);
      maxDataLengthSelectStatement.append(SQL_SUBQUERY_START_WRAPPER);
      maxDataLengthSelectStatement.append(SQL_SAS_CAT_FUNCTION);
      maxDataLengthSelectStatement.append(SQL_SUBQUERY_START_WRAPPER);
      maxDataLengthSelectStatement.append(createColumnRepresentation(queryColumn));
      maxDataLengthSelectStatement.append(SQL_ENTITY_SEPERATOR);
      maxDataLengthSelectStatement.append(EMPTY_QUOTE);
      maxDataLengthSelectStatement.append(SQL_SUBQUERY_END_WRAPPER);
      maxDataLengthSelectStatement.append(SQL_SUBQUERY_END_WRAPPER);
      maxDataLengthSelectStatement.append(SQL_SUBQUERY_END_WRAPPER);
      maxDataLengthSelectStatement.append(SQL_SPACE_DELIMITER);
      maxDataLengthSelectStatement.append(SQL_JOIN_FROM_CLAUSE);
      maxDataLengthSelectStatement.append(SQL_SPACE_DELIMITER);
      maxDataLengthSelectStatement.append(createTableRepresentation(queryTable));
      return maxDataLengthSelectStatement.toString();
   }

   @Override
   public SelectQueryInfo createPingSelectStatement (QueryTable queryTable) {
      // TODO : -RG- need to implement
      return null;
   }

   @Override
   protected Object createColumnsCommaSeperatedRepresentationWithDateHandling (List<QueryColumn> queryColumns) {
      // TODO:: NK:: Need to implement
      return null;
   }

   @Override
   public SelectQueryInfo createCountNotNullStringToDateStatement (QueryTable queryTable, QueryColumn queryColumn,
            String dateFormat) {
      // TODO:: NK:: Need to implement
      return null;
   }

   @Override
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
            valueRepresenation = QUOTE + NULL_VALUE_STRING_REPRESENTATION + QUOTE;
            break;
         case TIME:
         case DATE:
         case DATETIME:
            valueRepresenation = NULL_VALUE_NUMBER_REPRESENTATION;
            break;
      }
      return valueRepresenation;
   }

   @Override
   protected Object createColumnsCommaSeperatedRepresentation (List<QueryColumn> sourceQueryColumns,
            List<QueryColumn> destinationQueryColumns) {
      // TODO Auto-generated method stub
      return null;
   }
}
