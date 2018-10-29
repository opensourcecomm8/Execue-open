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


package com.execue.util.querygen.impl.mssql;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.DataType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.util.querygen.impl.SQLQueryGenerationUtilServiceImpl;

/**
 * MsSql specific query generation utilities come here
 * 
 * @author Vishay
 * @version 1.0
 * @since 13/08/09
 */
public class MsSqlQueryGenerationUtilServiceImpl extends SQLQueryGenerationUtilServiceImpl {

   @Override
   protected boolean applyPrecisionBasedOnDataType (DataType dataType) {
      // MSSQL doesn't take precision value for int and bigint data type
      if (DataType.INT.equals(dataType) || DataType.LARGE_INTEGER.equals(dataType)) {
         return false;
      }
      return true;
   }

   @Override
   public String createIndexPingStatement (String indexName, QueryTable queryTable) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(SQL_SELECT_CLAUSE);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(SQL_WILD_CHAR);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(SQL_JOIN_FROM_CLAUSE);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(MSSQL_SYSTEM_INDEX_NAME);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(SQL_WHERE_CLAUSE);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(MSSQL_SYSTEM_INDEX_COLUMN_NAME);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(JOIN_CONDITION_OPERATOR);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(QUOTE);
      stringBuilder.append(indexName);
      stringBuilder.append(QUOTE);
      return stringBuilder.toString();
   }

   @Override
   public String createUpdateStatement (QueryTable targetTable, List<QueryTable> sourceTables,
            List<ConditionEntity> setConditions, List<ConditionEntity> whereConditions) {
      // Example: Update table without joins
      // UPDATE YHY79
      // SET YHY79.SLICE_COUNT = 1
      // FROM dbo.CARDTYPE_FX_TEMP_CTbyFS YHY79 WHERE YHY79.ID = 1;

      // Example: Update table using joins
      // UPDATE YCS18
      // SET YCS18.SFACTOR = MLM57.SFACTOR
      // FROM dbo.ACCOUNT2_FX YCS18,dbo.POPULATION_FX_CTbyFS MLM57
      // WHERE YCS18.ACCOUNT_ID = MLM57.ACCOUNT_ID;

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
      updateStatement.append(targetTable.getAlias());
      updateStatement.append(SQL_SPACE_DELIMITER);
      updateStatement.append(SQL_SET_KEYWORD);
      updateStatement.append(SQL_SPACE_DELIMITER);
      updateStatement.append(ExecueCoreUtil.joinCollection(setConditionsList));
      updateStatement.append(SQL_SPACE_DELIMITER);
      updateStatement.append(SQL_JOIN_FROM_CLAUSE);
      updateStatement.append(SQL_SPACE_DELIMITER);
      updateStatement.append(ExecueCoreUtil.joinCollection(tablesSet));

      if (ExecueCoreUtil.isCollectionNotEmpty(whereConditionsList)) {
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(SQL_WHERE_CLAUSE);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(ExecueCoreUtil.joinCollection(whereConditionsList, SQL_SPACE_DELIMITER
                  + WHERE_CLAUSE_SEPERATOR + SQL_SPACE_DELIMITER));
      }
      return updateStatement.toString();
   }

   @Override
   protected String getSQLLengthFunction () {
      return MSSQL_SQL_LENGTH_FUNCTION;
   }

   @Override
   protected String createTableRepresentation (QueryTable queryTable) {
      String tableName = super.createTableRepresentation(queryTable);
      StringBuilder sb = new StringBuilder();
      sb.append(queryTable.getOwner().trim()).append(SQL_ALIAS_SEPERATOR).append(tableName.trim());
      return sb.toString();
   }

   @Override
   public SelectQueryInfo createPingSelectStatement (QueryTable queryTable) {
      // TODO : -PK- need to improve the statement
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
      List<Integer> countColumnIndexes = new ArrayList<Integer>();
      countColumnIndexes.add(0);
      return new SelectQueryInfo(selectStatment.toString(), countColumnIndexes);
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
   protected Object createColumnsCommaSeperatedRepresentation (List<QueryColumn> sourceQueryColumns,
            List<QueryColumn> destinationQueryColumns) {
      // TODO Auto-generated method stub
      return null;
   }

}