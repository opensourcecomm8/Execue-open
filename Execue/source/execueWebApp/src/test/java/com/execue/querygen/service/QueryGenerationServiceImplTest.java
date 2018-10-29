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


package com.execue.querygen.service;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.JoinEntity;
import com.execue.core.common.bean.querygen.LimitEntity;
import com.execue.core.common.bean.querygen.OrderEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryGenerationInput;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.JoinType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.OrderEntityType;
import com.execue.core.common.type.QueryConditionOperandType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.type.StatType;

public class QueryGenerationServiceImplTest extends ExeCueBaseTest {

   private QueryGenerationOutput queryGenerationOutput;
   private Logger                logger = Logger.getLogger(QueryGenerationServiceImplTest.class);

   public QueryGenerationOutput getQueryGenerationOutput () {
      return queryGenerationOutput;
   }

   public void setQueryGenerationOutput (QueryGenerationOutput queryGenerationOutput) {
      this.queryGenerationOutput = queryGenerationOutput;
   }

   @BeforeClass
   public static void setUp () {
      baseTestSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTestTeardown();
   }

   @Test
   public void testGenerateQuery () {
      QueryGenerationInput queryGenerationInput = prepareQueryGenerationInput();
      QueryGenerationOutput queryGenerationOutput = getQueryGenerationService(queryGenerationInput.getTargetAsset())
               .generateQuery(queryGenerationInput);
      assertNotNull("QueryGenerationOutput Object is Null", queryGenerationOutput);
      logger.debug(getQueryGenerationService(queryGenerationInput.getTargetAsset()).extractQueryRepresentation(
               queryGenerationOutput).getQueryString());
      setQueryGenerationOutput(queryGenerationOutput);
   }

   // @Test
   public void testExtractQueryString () {
      QueryGenerationOutput queryGenerationOutput = getQueryGenerationOutput();
      logger.debug(getQueryGenerationService(queryGenerationOutput.getQueryGenerationInput().getTargetAsset())
               .extractQueryRepresentation(queryGenerationOutput).getQueryString());
   }

   public QueryGenerationInput prepareQueryGenerationInput () {
      QueryGenerationInput queryGenerationInput = new QueryGenerationInput();
      Asset asset = new Asset();
      queryGenerationInput.setTargetAsset(asset);
      queryGenerationInput.setInputQueries(createQueries());
      return queryGenerationInput;
   }

   private RangeDetail getRangeDetail (Range range, String order, String description, Double lowerLimit,
            Double upperLimit) {
      RangeDetail rangeDetail = new RangeDetail();
      rangeDetail.setRange(range);
      rangeDetail.setOrder(order);
      rangeDetail.setValue(String.valueOf(order));
      rangeDetail.setDescription(description);
      rangeDetail.setLowerLimit(lowerLimit);
      rangeDetail.setUpperLimit(upperLimit);
      return rangeDetail;
   }

   private List<Query> createQueries () {
      List<Query> queries = new ArrayList<Query>();

      Range range = new Range();
      Set<RangeDetail> rangeDetails = new HashSet<RangeDetail>();
      rangeDetails.add(getRangeDetail(range, "0", "Low", null, new Double(100)));
      rangeDetails.add(getRangeDetail(range, "1", "Medium", new Double(200), new Double(300)));
      rangeDetails.add(getRangeDetail(range, "2", "High", new Double(300), null));

      range.setRangeDetails(rangeDetails);

      // preparing tableColum select entity
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      SelectEntity rangeSelectEntity = createTableColumSelectEntity("Asset", "assetAlias", "name", null, false);
      rangeSelectEntity.setRange(range);
      selectEntities.add(rangeSelectEntity);

      selectEntities.add(createTableColumSelectEntity("Asset", "assetAlias", "name", null, false));

      // prepare subquery for selectentity
      List<SelectEntity> subQueryEntities = new ArrayList<SelectEntity>();
      subQueryEntities.add(createTableColumSelectEntity("Tabl", "tablAlias", "name", StatType.COUNT, true));
      Query subQuery = createQuery(subQueryEntities, null, null, null, null, null, null, "subQueryAlias");

      // add select entities
      selectEntities.add(createSubQuerySelectEntity(subQuery));

      // preparing tableColum where entity
      List<ConditionEntity> whereEntities = new ArrayList<ConditionEntity>();
      QueryTableColumn lhsQueryTableColumn = createQueryTableColumn("Asset", "assetAlias", "id", false);
      QueryTableColumn rhsQueryTableColumn = createQueryTableColumn("Tabl", "tableAlias", "id", false);

      whereEntities.add(createTableColumWhereEntity(lhsQueryTableColumn, OperatorType.EQUALS, rhsQueryTableColumn));

      // prepare subQuery for where entity
      // whereEntities.add(createSubQueryWhereEntity(lhsQueryTableColumn, OperatorType.IN, subQuery));

      // preparing Value for Where Entity
      // whereEntities.add(createValueWhereEntity(lhsQueryTableColumn, OperatorType.GREATER_THAN, createQueryValue(
      // DataType.CHARACTER, "1000")));

      List<SelectEntity> groupingEntities = new ArrayList<SelectEntity>();

      groupingEntities.add(createTableColumGroupByEntity("Colum", "columAlias", "colum", null, false));
      // groupingEntities.add(createsubQueryGroupByEntity(subQuery));

      List<ConditionEntity> havingEntities = new ArrayList<ConditionEntity>();
      //
      // havingEntities.add(createTableColumnHavingEntity(lhsQueryTableColumn, OperatorType.NOT_EQUALS,
      // rhsQueryTableColumn));
      // havingEntities.add(createSubQueryHavingEntity(lhsQueryTableColumn, OperatorType.IN, subQuery));
      // havingEntities.add(createValueHavingEntity(lhsQueryTableColumn, OperatorType.LESS_THAN, createQueryValue(
      // DataType.CHARACTER, "1000")));
      //
      List<OrderEntity> orderingEntities = new ArrayList<OrderEntity>();

      orderingEntities.add(createTableColumnOrderByEntity("Membr", "membrAlias", "desc", null, false,
               OrderEntityType.ASCENDING));
      orderingEntities.add(createSubQueryOrderByEntity(subQuery, OrderEntityType.DESCENDING));

      LimitEntity limitEntity = createLimitEntity(10);
      // Creating JoinEntities, Later has to remove as Module itself with create Joins by himself

      List<JoinEntity> joinEntities = new ArrayList<JoinEntity>();
      populateJoinEntities(joinEntities);
      // List<JoinEntity> joinEntities = new ArrayList<JoinEntity>();
      // joinEntities.add(createJoinEntity(lhsQueryTableColumn, rhsQueryTableColumn, JoinType.INNER));
      // joinEntities.add(createJoinEntity(lhsQueryTableColumn, rhsQueryTableColumn, JoinType.LEFT_OUTER));
      Query query = createQuery(selectEntities, whereEntities, groupingEntities, havingEntities, orderingEntities,
               joinEntities, limitEntity, null);
      // Adding First Query
      queries.add(query);

      // Create another Query in order to test aliases correction.

      List<SelectEntity> selectEntities2 = new ArrayList<SelectEntity>();
      selectEntities2.add(createTableColumSelectEntity("AssetEntityDefinition", "assetAlias", "name", null, false));

      Query query2 = createQuery(selectEntities2, null, null, null, null, null, null, null);

      // queries.add(query2);
      return queries;
   }

   private LimitEntity createLimitEntity (int i) {
      LimitEntity limitEntity = new LimitEntity();
      limitEntity.setEndingNumber(i);
      return limitEntity;
   }

   private JoinEntity createJoinEntity (QueryTableColumn queryTableColumn, QueryTableColumn queryTableColumn2,
            JoinType joinType) {
      JoinEntity joinEntity = new JoinEntity();
      joinEntity.setLhsOperand(queryTableColumn);
      joinEntity.setRhsOperand(queryTableColumn2);
      joinEntity.setType(joinType);
      return joinEntity;
   }

   private OrderEntity createTableColumnOrderByEntity (String tableName, String tableAlias, String ColumnName,
            StatType functionName, boolean isDistinct, OrderEntityType orderEntityType) {
      OrderEntity orderEntity = new OrderEntity();
      orderEntity.setSelectEntity(createTableColumSelectEntity(tableName, tableAlias, ColumnName, functionName,
               isDistinct));
      orderEntity.setOrderType(orderEntityType);
      return orderEntity;

   }

   private OrderEntity createSubQueryOrderByEntity (Query subQuery, OrderEntityType orderEntityType) {
      OrderEntity orderEntity = new OrderEntity();
      orderEntity.setSelectEntity(createSubQuerySelectEntity(subQuery));
      orderEntity.setOrderType(orderEntityType);
      return orderEntity;
   }

   private ConditionEntity createTableColumnHavingEntity (QueryTableColumn lhsTableColumn, OperatorType operatorType,
            QueryTableColumn rhsTableColumn) {
      return createTableColumWhereEntity(lhsTableColumn, operatorType, rhsTableColumn);

   }

   private ConditionEntity createValueHavingEntity (QueryTableColumn lhsTableColumn, OperatorType operatorType,
            QueryValue rhsValue) {
      return createValueWhereEntity(lhsTableColumn, operatorType, rhsValue);
   }

   private ConditionEntity createSubQueryHavingEntity (QueryTableColumn lhsTableColumn, OperatorType operatorType,
            Query rhsSubQuery) {
      return createSubQueryWhereEntity(lhsTableColumn, operatorType, rhsSubQuery);

   }

   private SelectEntity createTableColumGroupByEntity (String tableName, String tableAlias, String columnName,
            StatType functionName, boolean isDistinct) {
      return createTableColumSelectEntity(tableName, tableAlias, columnName, functionName, isDistinct);
   }

   private SelectEntity createsubQueryGroupByEntity (Query subQuery) {
      return createSubQuerySelectEntity(subQuery);
   }

   private ConditionEntity createValueWhereEntity (QueryTableColumn lhsQueryTableColumn, OperatorType operatorType,
            QueryValue rhsValue) {
      ConditionEntity conditionEntity = new ConditionEntity();
      conditionEntity.setLhsTableColumn(lhsQueryTableColumn);
      conditionEntity.setOperandType(QueryConditionOperandType.VALUE);
      conditionEntity.setOperator(operatorType);
      List<QueryValue> queryValues = new ArrayList<QueryValue>();
      queryValues.add(rhsValue);
      conditionEntity.setRhsValues(queryValues);
      return conditionEntity;
   }

   private ConditionEntity createSubQueryWhereEntity (QueryTableColumn lhsQueryTableColumn, OperatorType operatorType,
            Query subQuery) {
      ConditionEntity conditionEntity = new ConditionEntity();
      conditionEntity.setLhsTableColumn(lhsQueryTableColumn);
      conditionEntity.setOperator(operatorType);
      conditionEntity.setOperandType(QueryConditionOperandType.SUB_QUERY);
      conditionEntity.setRhsSubQuery(subQuery);
      return conditionEntity;
   }

   private ConditionEntity createTableColumWhereEntity (QueryTableColumn lhsTableColumn, OperatorType operatorType,
            QueryTableColumn rhsTableColumn) {
      ConditionEntity conditionEntity = new ConditionEntity();
      conditionEntity.setLhsTableColumn(lhsTableColumn);
      conditionEntity.setOperator(operatorType);
      List<QueryTableColumn> rhsTableColums = new ArrayList<QueryTableColumn>();
      rhsTableColums.add(rhsTableColumn);
      conditionEntity.setRhsTableColumns(rhsTableColums);
      conditionEntity.setOperandType(QueryConditionOperandType.TABLE_COLUMN);
      return conditionEntity;
   }

   private SelectEntity createSubQuerySelectEntity (Query subQuery) {
      SelectEntity selectEntity = new SelectEntity();
      selectEntity.setType(SelectEntityType.SUB_QUERY);
      selectEntity.setSubQuery(subQuery);
      return selectEntity;
   }

   private SelectEntity createTableColumSelectEntity (String tableName, String tableAlias, String columnName,
            StatType functionName, boolean isDistinct) {
      SelectEntity selectEntity = new SelectEntity();
      if (functionName != null) {
         selectEntity.setFunctionName(functionName);
      }
      selectEntity.setType(SelectEntityType.TABLE_COLUMN);
      selectEntity.setTableColumn(createQueryTableColumn(tableName, tableAlias, columnName, isDistinct));
      return selectEntity;

   }

   private Query createQuery (List<SelectEntity> selectEntities, List<ConditionEntity> whereEntities,
            List<SelectEntity> groupingEntites, List<ConditionEntity> havingEntities,
            List<OrderEntity> orderingEntities, List<JoinEntity> joinEntities, LimitEntity limitEntity,
            String queryAlias) {
      Query query = new Query();
      query.setSelectEntities(selectEntities);
      if (whereEntities != null) {
         query.setWhereEntities(whereEntities);
      }
      if (groupingEntites != null) {
         query.setGroupingEntities(groupingEntites);
      }
      if (havingEntities != null) {
         query.setHavingEntities(havingEntities);
      }
      if (orderingEntities != null) {
         query.setOrderingEntities(orderingEntities);
      }
      if (joinEntities != null) {
         query.setJoinEntities(joinEntities);
      }
      if (limitEntity != null) {
         query.setLimitingCondition(limitEntity);
      }
      if (queryAlias != null) {
         query.setAlias(queryAlias);
      }
      return query;
   }

   private QueryTableColumn createQueryTableColumn (String tableName, String tableAlias, String columnName,
            boolean isDistinct) {
      QueryTableColumn queryTableColum = new QueryTableColumn();
      queryTableColum.setTable((createQueryTable(tableName, tableAlias)));
      queryTableColum.setColumn(createQueryColumn(columnName, isDistinct));
      return queryTableColum;
   }

   private QueryTable createQueryTable (String tableName, String tableAlias) {
      QueryTable queryTable = new QueryTable();
      queryTable.setTableName(tableName);
      queryTable.setAlias(tableAlias);
      return queryTable;
   }

   public QueryColumn createQueryColumn (String columnName, boolean isDistinct) {
      return createQueryColumn(columnName, isDistinct, DataType.STRING, 5, 0);
   }

   public QueryColumn createQueryColumn (String columnName, boolean isDistinct, DataType dataType, int precision,
            int scale) {
      QueryColumn queryColumn = new QueryColumn();
      queryColumn.setColumnName(columnName);
      queryColumn.setDistinct(isDistinct);
      queryColumn.setDataType(dataType);
      queryColumn.setPrecision(precision);
      queryColumn.setScale(scale);
      return queryColumn;
   }

   private QueryValue createQueryValue (DataType dataType, String value) {
      QueryValue queryValue = new QueryValue();
      queryValue.setDataType(dataType);
      queryValue.setValue(value);
      return queryValue;
   }

   /**
    * This method creates the string represnetation of joinEntities passed to it.
    * 
    * @param joinEntities
    * @return StringBuilder
    */
   public StringBuilder createJoinCondition (List<JoinEntity> joinEntities) {
      StringBuilder stringBuilder = new StringBuilder();
      List<JoinEntity> originalJoinEntities = new ArrayList<JoinEntity>();
      for (JoinEntity joinEntity : joinEntities) {
         originalJoinEntities.add(joinEntity);
      }
      JoinEntity firstJoinEntity = joinEntities.get(0);
      List<String> initialtablesList = new ArrayList<String>();
      initialtablesList.add(firstJoinEntity.getLhsOperand().getTable().getTableName());
      initialtablesList.add(firstJoinEntity.getRhsOperand().getTable().getTableName());

      List<String> joinedTablesList = findJoinedTables(initialtablesList, joinEntities);

      stringBuilder.append(joinedTablesList.get(0));
      for (int i = 1; i < joinedTablesList.size(); i++) {
         int innerJoinCount = 0;
         int leftOuterJoinCount = 0;
         int rightOuterJoinCount = 0;
         for (int j = 0; j < i; j++) {
            List<JoinEntity> foundJoinEntities = findJoinEntities(joinedTablesList.get(j), joinedTablesList.get(i),
                     originalJoinEntities);
            if (foundJoinEntities.size() > 0) {
               originalJoinEntities.removeAll(foundJoinEntities);
               for (JoinEntity joinEntity : foundJoinEntities) {
                  switch (joinEntity.getType()) {
                     case INNER:
                        countConditionalAppender(stringBuilder, innerJoinCount, joinEntity, joinedTablesList.get(i));
                        innerJoinCount++;
                        break;
                     case LEFT_OUTER:
                        countConditionalAppender(stringBuilder, leftOuterJoinCount, joinEntity, joinedTablesList.get(i));
                        leftOuterJoinCount++;
                        leftOuterJoinCount++;
                        break;
                     case RIGHT_OUTER:
                        countConditionalAppender(stringBuilder, rightOuterJoinCount, joinEntity, joinedTablesList
                                 .get(i));
                        rightOuterJoinCount++;
                        break;
                  }
               }
            }
         }
      }

      return stringBuilder;
   }

   /**
    * This method appends the joinEntity passed to it on basis of count.
    * 
    * @param stringBuilder
    * @param count
    * @param joinEntity
    * @param tableName
    * @return StringBuilder
    */
   private StringBuilder countConditionalAppender (StringBuilder stringBuilder, int count, JoinEntity joinEntity,
            String tableName) {
      if (count == 0) {
         stringBuilder.append(" " + joinEntity.getType().getValue() + " " + tableName + " on ");
      } else {
         stringBuilder.append(",");
      }
      stringBuilder.append(joinEntity.getLhsOperand().getTable().getAlias() + "."
               + joinEntity.getLhsOperand().getColumn().getColumnName() + "="
               + joinEntity.getRhsOperand().getTable().getAlias() + "."
               + joinEntity.getRhsOperand().getColumn().getColumnName());
      return stringBuilder;
   }

   /**
    * This method will find the tables which are joined
    * 
    * @param initialTables
    * @param joinEntities
    * @return joinedTables
    */
   private List<String> findJoinedTables (List<String> initialTables, List<JoinEntity> joinEntities) {
      List<String> joinedTables = new ArrayList<String>();
      joinedTables.addAll(initialTables);
      List<String> tempTables = new ArrayList<String>();
      tempTables.addAll(initialTables);
      while (tempTables.size() > 0) {
         initialTables.clear();
         initialTables.addAll(tempTables);
         tempTables = new ArrayList<String>();
         for (String tableName : initialTables) {
            List<JoinEntity> foundJoinEntities = findJoinEntity(tableName, joinEntities);
            if (foundJoinEntities.size() > 0) {
               joinEntities.removeAll(foundJoinEntities);
               for (JoinEntity foundJoinEntity : foundJoinEntities) {
                  String newTable = null;
                  if (foundJoinEntity.getLhsOperand().getTable().getTableName().equalsIgnoreCase(tableName)) {
                     newTable = foundJoinEntity.getRhsOperand().getTable().getTableName();
                  } else {
                     newTable = foundJoinEntity.getLhsOperand().getTable().getTableName();
                  }
                  if (!joinedTables.contains(newTable)) {
                     joinedTables.add(newTable);
                     tempTables.add(newTable);
                  }
               }
            }
         }
      }

      return joinedTables;
   }

   /**
    * This method will find the joinEntity for the tableName if exists
    * 
    * @param tableName
    * @param joinEntities
    * @return List<JoinEntity>
    */
   private List<JoinEntity> findJoinEntity (String tableName, List<JoinEntity> joinEntities) {
      List<JoinEntity> foundJoinEntities = new ArrayList<JoinEntity>();
      for (JoinEntity joinEntity : joinEntities) {
         if (joinEntity.getLhsOperand().getTable().getTableName().equalsIgnoreCase(tableName)
                  || joinEntity.getRhsOperand().getTable().getTableName().equalsIgnoreCase(tableName)) {
            foundJoinEntities.add(joinEntity);
         }
      }
      return foundJoinEntities;
   }

   /**
    * This method will find the joinEntities for two table combinations
    * 
    * @param tableName1
    * @param tableName2
    * @param joinEntities
    * @return List<JoinEntity>
    */
   private List<JoinEntity> findJoinEntities (String tableName1, String tableName2, List<JoinEntity> joinEntities) {
      List<JoinEntity> foundJoinEntities = new ArrayList<JoinEntity>();
      for (JoinEntity joinEntity : joinEntities) {
         if (joinEntity.getLhsOperand().getTable().getTableName().equalsIgnoreCase(tableName1)
                  && joinEntity.getRhsOperand().getTable().getTableName().equalsIgnoreCase(tableName2)
                  || joinEntity.getLhsOperand().getTable().getTableName().equalsIgnoreCase(tableName2)
                  && joinEntity.getRhsOperand().getTable().getTableName().equalsIgnoreCase(tableName1)) {
            foundJoinEntities.add(joinEntity);
         }
      }
      return foundJoinEntities;
   }

   public void populateJoinEntities (List<JoinEntity> joinEntities) {
      joinEntities.add(createJoinEntity(createQueryTableColum("A", "a"), JoinType.INNER,
               createQueryTableColum("B", "b")));
      joinEntities.add(createJoinEntity(createQueryTableColum("A", "a1"), JoinType.INNER, createQueryTableColum("B",
               "b1")));

      joinEntities.add(createJoinEntity(createQueryTableColum("A", "a"), JoinType.LEFT_OUTER, createQueryTableColum(
               "C", "c")));

      joinEntities.add(createJoinEntity(createQueryTableColum("B", "b"), JoinType.INNER,
               createQueryTableColum("C", "c")));
      joinEntities.add(createJoinEntity(createQueryTableColum("A", "a"), JoinType.INNER,
               createQueryTableColum("F", "f")));
      joinEntities.add(createJoinEntity(createQueryTableColum("C", "c"), JoinType.INNER,
               createQueryTableColum("G", "g")));
      joinEntities.add(createJoinEntity(createQueryTableColum("F", "f"), JoinType.RIGHT_OUTER, createQueryTableColum(
               "G", "g")));

   }

   private JoinEntity createJoinEntity (QueryTableColumn lhsQueryTableColum, JoinType joinType,
            QueryTableColumn rhsQueryTableColum) {
      JoinEntity joinEntity = new JoinEntity();
      joinEntity.setLhsOperand(lhsQueryTableColum);
      joinEntity.setRhsOperand(rhsQueryTableColum);
      joinEntity.setType(joinType);
      return joinEntity;
   }

   private QueryTableColumn createQueryTableColum (String tableName, String columName) {
      QueryTableColumn queryTableColumn = new QueryTableColumn();
      QueryTable queryTable = new QueryTable();
      queryTable.setTableName(tableName);
      queryTable.setAlias(tableName);
      QueryColumn queryColumn = new QueryColumn();
      queryColumn.setColumnName(columName);
      queryTableColumn.setTable(queryTable);
      queryTableColumn.setColumn(queryColumn);
      return queryTableColumn;
   }

}