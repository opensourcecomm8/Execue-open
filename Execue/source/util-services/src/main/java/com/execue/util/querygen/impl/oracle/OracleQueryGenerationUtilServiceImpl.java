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


package com.execue.util.querygen.impl.oracle;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.DataType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.util.querygen.impl.SQLQueryGenerationUtilServiceImpl;

/**
 * Oracle specific query generation utilities come here
 * 
 * @author Vishay
 * @version 1.0
 * @since 13/08/09
 */
public class OracleQueryGenerationUtilServiceImpl extends SQLQueryGenerationUtilServiceImpl {

   @Override
   protected String getNotNullAndDefaultConditions (QueryColumn column) {
      StringBuilder notNullDefaultCondition = new StringBuilder();
      if (!StringUtils.isBlank(column.getDefaultValue())) {
         String defaultValue = DEFAULT_VALUE_STRING;
         defaultValue = defaultValue.replaceAll(DEFAULT_VALUE_PLACE_HOLDER, column.getDefaultValue());
         notNullDefaultCondition.append(defaultValue);
      }
      if (!column.isNullable()) {
         notNullDefaultCondition.append(SQL_SPACE_DELIMITER);
         notNullDefaultCondition.append(NOT_NULL_KEYWORD);
         notNullDefaultCondition.append(SQL_SPACE_DELIMITER);
      }
      return notNullDefaultCondition.toString().trim();
   }

   @Override
   protected DataType getProviderSpecificDataType (DataType dataType) {
      DataType providerDataType = dataType;
      if (DataType.NUMBER.equals(dataType) || DataType.INT.equals(dataType) || DataType.LARGE_INTEGER.equals(dataType)) {
         providerDataType = DataType.NUMBER;
      } else if (DataType.TEXT.equals(dataType)) {
         providerDataType = DataType.STRING;
      }
      return providerDataType;
   }

   @Override
   public String createUpdateStatement (QueryTable targetTable, List<QueryTable> sourceTables,
            List<ConditionEntity> setConditions, List<ConditionEntity> whereConditions) {
      StringBuilder updateStatement = new StringBuilder();
      if (ExecueCoreUtil.isCollectionEmpty(sourceTables)) {
         updateStatement = new StringBuilder(super.createUpdateStatement(targetTable, sourceTables, setConditions,
                  whereConditions));
      } else {
         String targetTableClause = getTableClause(targetTable);
         List<String> tablesSet = new ArrayList<String>();
         for (QueryTable queryTable : sourceTables) {
            tablesSet.add(getTableClause(queryTable));
         }
         List<String> lhsSetConditionsList = buildLhsConditions(setConditions);
         List<String> rhsSetConditionsList = buildRhsConditions(setConditions);
         List<String> whereConditionsList = buildWhereConditions(whereConditions);

         updateStatement.append(UPDATE_TABLE_KEYWORD);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(targetTableClause);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(SQL_SET_KEYWORD);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(SQL_SUBQUERY_START_WRAPPER);
         updateStatement.append(ExecueCoreUtil.joinCollection(lhsSetConditionsList));
         updateStatement.append(SQL_SUBQUERY_END_WRAPPER);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(JOIN_CONDITION_OPERATOR);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(SQL_SUBQUERY_START_WRAPPER);
         updateStatement.append(prepareInnerSelectStatementForUpdate(tablesSet, rhsSetConditionsList,
                  whereConditionsList, true));
         updateStatement.append(SQL_SUBQUERY_END_WRAPPER);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(ORACLE_WHERE_EXISTS_CLAUSE);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(SQL_SUBQUERY_START_WRAPPER);
         updateStatement.append(prepareInnerSelectStatementForUpdate(tablesSet, rhsSetConditionsList,
                  whereConditionsList, false));
         updateStatement.append(SQL_SUBQUERY_END_WRAPPER);
      }
      return updateStatement.toString();
   }

   private String prepareInnerSelectStatementForUpdate (List<String> sourceTablesSet,
            List<String> rhsSetConditionsList, List<String> whereConditionsList, boolean needSelectClause) {
      StringBuilder innerSelectStatement = new StringBuilder();
      innerSelectStatement.append(SQL_SELECT_CLAUSE);
      innerSelectStatement.append(SQL_SPACE_DELIMITER);
      if (needSelectClause) {
         innerSelectStatement.append(ExecueCoreUtil.joinCollection(rhsSetConditionsList));
      } else {
         innerSelectStatement.append("1");
      }
      innerSelectStatement.append(SQL_SPACE_DELIMITER);
      innerSelectStatement.append(SQL_JOIN_FROM_CLAUSE);
      innerSelectStatement.append(SQL_SPACE_DELIMITER);
      innerSelectStatement.append(ExecueCoreUtil.joinCollection(sourceTablesSet));
      if (ExecueCoreUtil.isCollectionNotEmpty(whereConditionsList)) {
         innerSelectStatement.append(SQL_SPACE_DELIMITER);
         innerSelectStatement.append(SQL_WHERE_CLAUSE);
         innerSelectStatement.append(SQL_SPACE_DELIMITER);
         innerSelectStatement.append(ExecueCoreUtil.joinCollection(whereConditionsList, SQL_SPACE_DELIMITER
                  + WHERE_CLAUSE_SEPERATOR + SQL_SPACE_DELIMITER));
      }
      return innerSelectStatement.toString();
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
      stringBuilder.append(ORACLE_USER_INDEX_NAME);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(SQL_WHERE_CLAUSE);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(ORACLE_USER_INDEX_COLUMN_NAME);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(JOIN_CONDITION_OPERATOR);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(QUOTE);
      stringBuilder.append(indexName.toUpperCase());
      stringBuilder.append(QUOTE);
      return stringBuilder.toString();
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
      selectStatment.append(SQL_WHERE_CLAUSE);
      selectStatment.append(SQL_SPACE_DELIMITER);
      selectStatment.append(ORACLE_LIMIT_CLAUSE);
      selectStatment.append(SQL_SPACE_DELIMITER);
      selectStatment.append(LESSER_THAN_EQUAL_OPERATOR);
      selectStatment.append(SQL_SPACE_DELIMITER);
      selectStatment.append(1);
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
