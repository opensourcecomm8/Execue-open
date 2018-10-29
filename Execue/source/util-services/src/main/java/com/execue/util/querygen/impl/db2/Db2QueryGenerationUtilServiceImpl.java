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


package com.execue.util.querygen.impl.db2;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.QueryConditionOperandType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.util.querygen.impl.SQLQueryGenerationUtilServiceImpl;

/**
 * DB2 specific query generation utilities come here
 * 
 * @author Prasanna
 */
public class Db2QueryGenerationUtilServiceImpl extends SQLQueryGenerationUtilServiceImpl {

   @Override
   public String createIndexPingStatement (String indexName, QueryTable tableName) {
      // SELECT INDNAME FROM SYSCAT.INDEXES WHERE TABSCHEMA NOT LIKE 'SYS%' AND INDNAME = INDEXNAME;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(SQL_SELECT_CLAUSE);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(DB2_SYSTEM_CATALOG_INDEX_NAME);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(SQL_JOIN_FROM_CLAUSE);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(DB2_SYSTEM_CATLOG_NAME);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(SQL_WHERE_CLAUSE);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(DB2_SYSTEM_CATALOG_SCHEMA_NAME);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(SQL_NOT_LIKE_KEYWORD);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(QUOTE);
      stringBuilder.append(DB2_SYSTEM_CATALOG);
      stringBuilder.append(QUOTE);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(WHERE_CLAUSE_SEPERATOR);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(DB2_SYSTEM_CATALOG_INDEX_NAME);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(JOIN_CONDITION_OPERATOR);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(QUOTE);
      stringBuilder.append(indexName);
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
   protected String getSQLLengthFunction () {
      return DB2_SQL_LENGTH_FUNCTION;
   }

   @Override
   protected String createTableRepresentation (QueryTable queryTable) {
      String tableName = super.createTableRepresentation(queryTable);
      StringBuilder sb = new StringBuilder();
      sb.append(queryTable.getOwner().trim()).append(SQL_ALIAS_SEPERATOR).append(tableName.trim());
      return sb.toString();

   }

   @Override
   protected boolean applyPrecisionBasedOnDataType (DataType dataType) {
      // DB2 doesn't take precision value for int and bigint data type
      if (DataType.INT.equals(dataType) || DataType.LARGE_INTEGER.equals(dataType)) {
         return false;
      }
      return true;
   }

   @Override
   public String createUpdateStatement (QueryTable targetTable, List<QueryTable> sourceTables,
            List<ConditionEntity> setConditions, List<ConditionEntity> whereConditions) {

      List<String> tablesSet = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionNotEmpty(sourceTables)) {
         for (QueryTable queryTable : sourceTables) {
            tablesSet.add(getTableClause(queryTable));
         }
      }

      // Here we are preparing source and target conditions from list of whereConditions
      List<ConditionEntity> sourceWhereConditions = new ArrayList<ConditionEntity>();
      List<ConditionEntity> targetWhereConditions = new ArrayList<ConditionEntity>();
      for (ConditionEntity whereCondition : whereConditions) {
         if (QueryConditionOperandType.TABLE_COLUMN.equals(whereCondition.getOperandType())) {
            for (QueryTableColumn rhsTableColumn : whereCondition.getRhsTableColumns()) {
               if (targetTable.getTableName().equalsIgnoreCase(
                        whereCondition.getLhsTableColumn().getTable().getTableName())
                        || targetTable.getTableName().equalsIgnoreCase(rhsTableColumn.getTable().getTableName())) {
                  targetWhereConditions.add(whereCondition);
               } else {
                  sourceWhereConditions.add(whereCondition);
               }
            }
         } else if (QueryConditionOperandType.VALUE.equals(whereCondition.getOperandType())) {
            sourceWhereConditions.add(whereCondition);
         }
      }

      List<String> setConditionsList = buildSetConditions(setConditions);
      List<String> sourceTableWhereConditions = buildWhereConditions(sourceWhereConditions);
      List<String> targetTablesWhereConditions = buildWhereConditions(targetWhereConditions);

      StringBuilder updateStatement = new StringBuilder();

      if (ExecueCoreUtil.isCollectionEmpty(sourceTables)) {
         // Example 1: When we update just target table without any joins
         // update account2_fx_65 FHY11 set FHY11.sfactor=20.000 where FHY11.account_id=351764
         updateStatement.append(UPDATE_TABLE_KEYWORD);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(targetTable.getTableName());
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(targetTable.getAlias());
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(SQL_SET_KEYWORD);
         updateStatement.append(SQL_SPACE_DELIMITER);
         updateStatement.append(ExecueCoreUtil.joinCollection(setConditionsList));

         // Here we are merging the source and target where conditions because there is only target table.
         List<String> whereConditionsList = new ArrayList<String>(sourceTableWhereConditions);
         whereConditionsList.addAll(targetTablesWhereConditions);

         if (ExecueCoreUtil.isCollectionNotEmpty(whereConditionsList)) {
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(SQL_WHERE_CLAUSE);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(ExecueCoreUtil.joinCollection(whereConditionsList, SQL_SPACE_DELIMITER
                     + WHERE_CLAUSE_SEPERATOR + SQL_SPACE_DELIMITER));
         }
      } else {
         // Example 2: When we update target table using join on one source table
         // MERGE INTO ACCOUNT2_FX_65 MHF11
         // USING POPULATION_FX_samplemart1_66 MQF64
         // ON MHF11.ACCOUNT_ID = MQF64.ACCOUNT_ID
         // WHEN MATCHED THEN UPDATE SET MHF11.SFACTOR = MQF64.SFACTOR;
         if (ExecueCoreUtil.isCollectionEmpty(sourceTableWhereConditions)) {
            updateStatement.append(DB2_MERGE_KEYWORD);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(targetTable.getTableName());
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(targetTable.getAlias());
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(DB2_USING_KEYWORD);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(ExecueCoreUtil.joinCollection(tablesSet));
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(JOIN_CONDITION_SEPERATOR);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(ExecueCoreUtil.joinCollection(targetTablesWhereConditions, SQL_SPACE_DELIMITER
                     + WHERE_CLAUSE_SEPERATOR + SQL_SPACE_DELIMITER));
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(DB2_WHEN_MATCHED_KEYWORD);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(DB2_THEN_KEYWORD);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(UPDATE_TABLE_KEYWORD);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(SQL_SET_KEYWORD);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(ExecueCoreUtil.joinCollection(setConditionsList));
         } else {
            // Example 3: When we update target table using join on multiple source tables
            // merge into t1
            // using (select t2.c2, t3.c4 from t2, t3 where t2.c3=t3.c3) as t
            // on t1.c2=t.c2
            // when matched then update set t1.c1=t.c4;
            updateStatement.append(DB2_MERGE_KEYWORD);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(targetTable.getTableName());
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(targetTable.getAlias());
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(DB2_USING_KEYWORD);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(SQL_SUBQUERY_START_WRAPPER);
            updateStatement.append(SQL_SELECT_CLAUSE);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(createSelectColumns(setConditions, targetWhereConditions));
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(SQL_JOIN_FROM_CLAUSE);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(ExecueCoreUtil.joinCollection(tablesSet));
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(SQL_WHERE_CLAUSE);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(ExecueCoreUtil.joinCollection(sourceTableWhereConditions, SQL_SPACE_DELIMITER
                     + WHERE_CLAUSE_SEPERATOR + SQL_SPACE_DELIMITER));
            updateStatement.append(SQL_SUBQUERY_END_WRAPPER);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(SQL_ALIAS);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(DB2_UPDATE_SELECT_QUERY_ALIAS_NAME);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(JOIN_CONDITION_SEPERATOR);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(createOnConditions(targetWhereConditions, targetTable));
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(DB2_WHEN_MATCHED_KEYWORD);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(DB2_THEN_KEYWORD);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(UPDATE_TABLE_KEYWORD);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(SQL_SET_KEYWORD);
            updateStatement.append(SQL_SPACE_DELIMITER);
            updateStatement.append(createSetConditions(setConditions));
         }
      }
      return updateStatement.toString();
   }

   /**
    * In this method we are preparing select columns from setConditions and targetWhereConditionsList by taking only rhs
    * columns.
    * 
    * @param setConditions
    * @param targetWhereConditionsList
    * @return selectColumns
    */
   private String createSelectColumns (List<ConditionEntity> setConditions,
            List<ConditionEntity> targetWhereConditionsList) {

      List<String> selectColumnsList = new ArrayList<String>();

      for (ConditionEntity setCondition : setConditions) {
         StringBuilder selectColumns = new StringBuilder();
         for (QueryTableColumn rhsTableColumn : setCondition.getRhsTableColumns()) {
            selectColumns.append(rhsTableColumn.getTable().getAlias());
            selectColumns.append(SQL_ALIAS_SEPERATOR);
            selectColumns.append(rhsTableColumn.getColumn().getColumnName());
         }
         selectColumnsList.add(selectColumns.toString());
      }
      for (ConditionEntity targetWhereCondition : targetWhereConditionsList) {
         StringBuilder selectColumns = new StringBuilder();
         for (QueryTableColumn rhsTableColumn : targetWhereCondition.getRhsTableColumns()) {
            selectColumns.append(rhsTableColumn.getTable().getAlias());
            selectColumns.append(SQL_ALIAS_SEPERATOR);
            selectColumns.append(rhsTableColumn.getColumn().getColumnName());
         }
         selectColumnsList.add(selectColumns.toString());
      }

      return ExecueCoreUtil.joinCollection(selectColumnsList);
   }

   /**
    * Here we will are building the on conditions but we are replacing the table of rhs with the update select query
    * alias.
    * 
    * @param targetWhereConditions
    * @param targetTable
    * @return onCondition
    */
   private String createOnConditions (List<ConditionEntity> targetWhereConditions, QueryTable targetTable) {

      List<String> onConditionList = new ArrayList<String>();

      for (ConditionEntity targetWhereCondition : targetWhereConditions) {
         if (targetTable.getTableName().equalsIgnoreCase(
                  targetWhereCondition.getLhsTableColumn().getTable().getTableName())) {
            QueryTableColumn rhsTableColumn = targetWhereCondition.getRhsTableColumns().get(0);

            StringBuilder onCondition = new StringBuilder();
            onCondition.append(targetWhereCondition.getLhsTableColumn().getTable().getAlias());
            onCondition.append(SQL_ALIAS_SEPERATOR);
            onCondition.append(targetWhereCondition.getLhsTableColumn().getColumn().getColumnName());
            onCondition.append(SQL_SPACE_DELIMITER);
            onCondition.append(targetWhereCondition.getOperator().getValue());
            onCondition.append(SQL_SPACE_DELIMITER);
            onCondition.append(DB2_UPDATE_SELECT_QUERY_ALIAS_NAME);
            onCondition.append(SQL_ALIAS_SEPERATOR);
            onCondition.append(rhsTableColumn.getColumn().getColumnName());
            onConditionList.add(onCondition.toString());
         } else {
            QueryTableColumn rhsTableColumn = targetWhereCondition.getRhsTableColumns().get(0);
            StringBuilder onCondition = new StringBuilder();
            onCondition.append(DB2_UPDATE_SELECT_QUERY_ALIAS_NAME);
            onCondition.append(SQL_ALIAS_SEPERATOR);
            onCondition.append(targetWhereCondition.getLhsTableColumn().getColumn().getColumnName());
            onCondition.append(SQL_SPACE_DELIMITER);
            onCondition.append(targetWhereCondition.getOperator().getValue());
            onCondition.append(SQL_SPACE_DELIMITER);
            onCondition.append(rhsTableColumn.getTable().getTableName());
            onCondition.append(SQL_ALIAS_SEPERATOR);
            onCondition.append(rhsTableColumn.getColumn().getColumnName());
            onConditionList.add(onCondition.toString());
         }

      }
      return ExecueCoreUtil.joinCollection(onConditionList, SQL_SPACE_DELIMITER + WHERE_CLAUSE_SEPERATOR
               + SQL_SPACE_DELIMITER);
   }

   /**
    * Here we will are building the set conditions but we are replacing the table of rhs with the update select query
    * alias.
    * 
    * @param setConditions
    * @return setContionStatement
    */
   private String createSetConditions (List<ConditionEntity> setConditions) {

      List<String> setContionStatementList = new ArrayList<String>();

      for (ConditionEntity setCondition : setConditions) {
         StringBuilder setContionStatement = new StringBuilder();
         setContionStatement.append(setCondition.getLhsTableColumn().getTable().getAlias());
         setContionStatement.append(SQL_ALIAS_SEPERATOR);
         setContionStatement.append(setCondition.getLhsTableColumn().getColumn().getColumnName());
         for (QueryTableColumn rhsTableColumn : setCondition.getRhsTableColumns()) {
            setContionStatement.append(SQL_SPACE_DELIMITER);
            setContionStatement.append(setCondition.getOperator().getValue());
            setContionStatement.append(SQL_SPACE_DELIMITER);
            setContionStatement.append(DB2_UPDATE_SELECT_QUERY_ALIAS_NAME);
            setContionStatement.append(SQL_ALIAS_SEPERATOR);
            setContionStatement.append(rhsTableColumn.getColumn().getColumnName());
         }
         setContionStatementList.add(setContionStatement.toString());
      }
      return ExecueCoreUtil.joinCollection(setContionStatementList);
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
