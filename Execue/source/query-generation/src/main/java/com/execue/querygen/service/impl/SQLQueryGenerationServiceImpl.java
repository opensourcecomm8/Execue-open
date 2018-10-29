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


package com.execue.querygen.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DateFormat;
import com.execue.core.common.bean.entity.JoinDefinition;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.comparator.RangeDetailOrderComparator;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.FromEntity;
import com.execue.core.common.bean.querygen.JoinEntity;
import com.execue.core.common.bean.querygen.OrderEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryClauseElement;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryFormula;
import com.execue.core.common.bean.querygen.QueryGenerationInput;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;
import com.execue.core.common.bean.querygen.QueryRepresentation;
import com.execue.core.common.bean.querygen.QueryStructure;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.bean.querygen.SQLQuery;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.ArithmeticOperatorType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.FromEntityType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.PublishedFileType;
import com.execue.core.common.type.QueryClauseType;
import com.execue.core.common.type.QueryCombiningType;
import com.execue.core.common.type.QueryConditionOperandType;
import com.execue.core.common.type.QueryElementType;
import com.execue.core.common.type.QueryFormulaOperandType;
import com.execue.core.common.type.QueryFormulaType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.QueryFormatUtility;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.constants.ISQLQueryConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.querygen.exception.QueryGenerationException;
import com.execue.querygen.exception.QueryGenerationExceptionCodes;
import com.execue.querygen.service.IQueryGenerationService;
import com.execue.querygen.service.impl.mysql.MySqlQueryGenerationServiceImpl;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.JoinException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IJoinService;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.util.queryadaptor.ISQLAdaptor;
import com.execue.util.queryadaptor.SQLAdaptorFactory;

/**
 * This abstract class is used for generating SQL syntax query from List<Query> queries.
 * It applies merging, delete duplicates and apply joins in order to create a SQL syntax query.
 * It has an utility method that returns the String Implementation of the SQL query Object.
 * 
 * http://swik.net/MySQL/MySQL+vs+MS+SQL+Server
 * http://www.mssqlcity.com/Articles/Compare/sql_server_vs_oracle.htm
 * 
 * above links contain ms sql and oracle with mysql information.
 * 
 * @author Jayadev
 * @version 1.0
 * @since 24/01/09
 */
public abstract class SQLQueryGenerationServiceImpl implements IQueryGenerationService, ISQLQueryConstants {

   private static final Logger       logger        = Logger.getLogger(SQLQueryGenerationServiceImpl.class);
   private int                       aliasSequence = 0;
   private IJoinService              joinService;
   private ISDXRetrievalService      sdxRetrievalService;
   private ISWIConfigurationService  swiConfigurationService;
   private ICoreConfigurationService coreConfigurationService;
   private Asset                     asset;
   protected Integer                 providerTypeValue;

   /**
    * Check for multiple Queries in order to find out if merging is required or not. If merge the queries else proceed
    * further<br/> If defaultsProcessingRequired is true then apply defaults. Remove duplicates which should eliminate
    * duplicates from all of the entities of query object<br/> From all the individual component of the Query object,
    * gather the tables and populate the FromEntity<br/> If the entries in FromEntity are more than one, then apply
    * joins and optimize the joins by remove unnecessary joins based on the existing where conditions<br/> Remove
    * duplicates which should eliminate duplicates from all of the entities of query object<br/><br/> Output Object is
    * ready to be used for extracting the string representation of the query.
    * 
    * @param queryGenerationInput
    * @return queryGenerationOutput
    */

   public QueryGenerationOutput generateQuery (QueryGenerationInput queryGenerationInput) {

      List<Query> queries = queryGenerationInput.getInputQueries();
      // Populate From Segment
      for (Query query : queries) {
         // once it populates the from entities, then it will do remove duplicates also.
         // If there are subqueries in any of the sections, it will populate the from section for that query also
         populateFromEntities(query);
         handleVirtualLookupTables(queryGenerationInput.getTargetAsset().getId(), query);
      }

      boolean combingTypeQueryPresent = checkIfCombiningTypeQueryPresent(queries);
      Query resultQuery = null;
      if (combingTypeQueryPresent) {

         // Prepare the map by grouping combining queries based on the index order of the list
         Map<Integer, List<Query>> queriesByIndexOrder = prepareCombiningQueriesByIndexOrderMap(queries);

         // override the same alias before merging the queries.
         correctAliasDuplicationForCombiningQueries(queriesByIndexOrder);

         // Assumption is we should get the same combining type for all the queries 
         QueryCombiningType combiningType = queries.get(0).getCombiningType();

         // Merge the input queries into resultQuery
         resultQuery = mergeQueriesForCombiningQueries(queriesByIndexOrder, combiningType);
      } else {
         // override the same alias before merging the queries.
         correctAliasDuplication(queries);

         // Merge the input queries into resultQuery
         resultQuery = mergeQueries(queries);
      }

      // Remove Duplicates
      removeDuplicates(resultQuery);

      // check if query has subquery in from clause, then apply joins for that also, other section needs to be handled
      if (combingTypeQueryPresent) {
         List<Query> combiningQueries = resultQuery.getCombiningQueries();
         for (Query combinigQuery : combiningQueries) {
            handleJoinsForFromAndConditionEntity(queryGenerationInput, combinigQuery);
         }
      } else {
         handleJoinsForFromAndConditionEntity(queryGenerationInput, resultQuery);
      }

      // apply joins for outer query
      handleJoins(queryGenerationInput.getTargetAsset(), resultQuery);

      final QueryGenerationOutput queryGenerationOutput = new QueryGenerationOutput();
      queryGenerationOutput.setQueryGenerationInput(queryGenerationInput);
      queryGenerationOutput.setResultQuery(resultQuery);
      return queryGenerationOutput;
   }

   /**
    * @param queryGenerationInput
    * @param resultQuery
    */
   private void handleJoinsForFromAndConditionEntity (QueryGenerationInput queryGenerationInput, Query resultQuery) {
      // check if query has subquery in from clause, then apply joins for that also, other section needs to be handled
      if (ExecueCoreUtil.isListElementsNotEmpty(resultQuery.getFromEntities())) {
         for (FromEntity fromEntity : resultQuery.getFromEntities()) {
            if (FromEntityType.SUB_QUERY.equals(fromEntity.getType())) {
               handleJoins(queryGenerationInput.getTargetAsset(), fromEntity.getSubQuery());
            }
         }
         for (ConditionEntity conditionEntity : resultQuery.getWhereEntities()) {
            if (QueryConditionOperandType.SUB_QUERY.equals(conditionEntity.getOperandType())) {
               handleJoins(queryGenerationInput.getTargetAsset(), conditionEntity.getRhsSubQuery());
            }
         }
      }
   }

   private void correctAliasDuplicationForCombiningQueries (Map<Integer, List<Query>> queriesByIndexOrder) {
      for (Entry<Integer, List<Query>> entry : queriesByIndexOrder.entrySet()) {
         List<Query> queriesToMerge = entry.getValue();
         correctAliasDuplication(queriesToMerge);
      }
   }

   private boolean checkIfCombiningTypeQueryPresent (List<Query> queries) {
      boolean combiningTypeQueryPresent = false;
      for (Query query : queries) {
         if (query.getCombiningType() != null) {
            combiningTypeQueryPresent = true;
            break;
         }
      }
      return combiningTypeQueryPresent;
   }

   private void handleJoins (Asset asset, Query query) {
      if (query.getCombiningType() == null) {
         handleJoinsForSimpleQuery(asset, query);
      } else {
         List<Query> combiningQueries = query.getCombiningQueries();
         for (Query combiningQuery : combiningQueries) {
            handleJoinsForSimpleQuery(asset, combiningQuery);
         }
      }
   }

   private void handleJoinsForSimpleQuery (Asset asset, Query query) {
      try {
         applyJoins(asset, query);
         optimizeJoins(query);
         mergeJoinEntitiesToFromEntities(query);
         handleVirtualLookupTables(asset.getId(), query);
      } catch (QueryGenerationException QueryGenerationException) {
         logger.error("Should not be handled : ", QueryGenerationException);
      }
      // Remove Duplicates
      removeDuplicates(query);
   }

   private void handleVirtualLookupTables (Long assetId, Query query) {
      if (query.getCombiningType() == null) {
         handleVirtualLookupTablesForSimpleQuery(assetId, query);
      } else {
         List<Query> combiningQueries = query.getCombiningQueries();
         for (Query combiningQuery : combiningQueries) {
            handleVirtualLookupTablesForSimpleQuery(assetId, combiningQuery);
         }
      }
   }

   /**
    * @param assetId
    * @param query
    */
   private void handleVirtualLookupTablesForSimpleQuery (Long assetId, Query query) {
      try {
         List<FromEntity> fromEntities = query.getFromEntities();
         Map<String, QueryTable> tableActualNameAliasMap = new HashMap<String, QueryTable>();
         for (FromEntity fromEntity : fromEntities) {
            if (FromEntityType.TABLE.equals(fromEntity.getType())) {
               QueryTable table = fromEntity.getTable();
               if (CheckType.NO.equals(table.getVirtual())) {
                  if (!tableActualNameAliasMap.containsKey(table.getTableName())) {
                     tableActualNameAliasMap.put(table.getTableName(), table);
                  }
               }
            }
         }
         for (FromEntity fromEntity : fromEntities) {
            if (FromEntityType.TABLE.equals(fromEntity.getType())) {
               QueryTable table = fromEntity.getTable();
               if (CheckType.YES.equals(table.getVirtual())) {
                  QueryTable tableToUse = null;
                  if (tableActualNameAliasMap.containsKey(table.getActualName())) {
                     tableToUse = tableActualNameAliasMap.get(table.getActualName());
                  } else {
                     Tabl parentTable = getSdxRetrievalService().getAssetTable(assetId, table.getActualName());
                     tableToUse = ExecueBeanManagementUtil.prepareQueryTable(parentTable);
                     tableToUse.setAlias(table.getAlias());
                     tableActualNameAliasMap.put(tableToUse.getTableName(), tableToUse);
                  }
                  adjustOtherSections(assetId, query, table, tableToUse);
                  copyTableInfo(table, tableToUse);
               }
            } else if (FromEntityType.SUB_QUERY.equals(fromEntity.getType())) {
               handleVirtualLookupTables(assetId, fromEntity.getSubQuery());
            }
         }
      } catch (SDXException sdxException) {
         logger.error("Should not be handled : ", sdxException);
      }
   }

   private void adjustOtherSections (Long assetId, Query query, QueryTable table, QueryTable tableToUse) {
      adjustSelectEntities(assetId, query.getSelectEntities(), table, tableToUse);
      adjustSelectEntities(assetId, query.getGroupingEntities(), table, tableToUse);
      adjustOrderEntities(assetId, query.getOrderingEntities(), table, tableToUse);
      adjustConditionalEntities(assetId, query.getWhereEntities(), table, tableToUse);
      adjustConditionalEntities(assetId, query.getHavingEntities(), table, tableToUse);
      adjustSelectEntities(assetId, query.getPopulationEntities(), table, tableToUse);
      adjustJoinEntities(query.getJoinEntities(), table, tableToUse);
   }

   private void adjustJoinEntities (List<JoinEntity> joinEntities, QueryTable table, QueryTable tableToUse) {
      if (ExecueCoreUtil.isCollectionNotEmpty(joinEntities)) {
         for (JoinEntity joinEntity : joinEntities) {
            QueryTable lhsTable = joinEntity.getLhsOperand().getTable();
            QueryTable rhsTable = joinEntity.getRhsOperand().getTable();
            if (table.equals(lhsTable)) {
               copyTableInfo(lhsTable, tableToUse);
            }
            if (table.equals(rhsTable)) {
               copyTableInfo(rhsTable, tableToUse);
            }
         }
      }
   }

   private void adjustConditionalEntities (Long assetId, List<ConditionEntity> conditionalEntities, QueryTable table,
            QueryTable tableToUse) {
      if (ExecueCoreUtil.isCollectionNotEmpty(conditionalEntities)) {
         for (ConditionEntity conditionEntity : conditionalEntities) {
            if (conditionEntity.isSubCondition()) {
               adjustConditionalEntities(assetId, conditionEntity.getSubConditionEntities(), table, tableToUse);
            } else {
               QueryTable queryTable = conditionEntity.getLhsTableColumn().getTable();
               if (table.equals(queryTable)) {
                  copyTableInfo(queryTable, tableToUse);
               }
               if (QueryConditionOperandType.TABLE_COLUMN.equals(conditionEntity.getOperandType())) {
                  List<QueryTableColumn> rhsTableColumns = conditionEntity.getRhsTableColumns();
                  for (QueryTableColumn queryTableColumn : rhsTableColumns) {
                     QueryTable rhsTable = queryTableColumn.getTable();
                     if (table.equals(rhsTable)) {
                        copyTableInfo(rhsTable, tableToUse);
                     }
                  }
               } else if (QueryConditionOperandType.SUB_QUERY.equals(conditionEntity.getOperandType())) {
                  handleVirtualLookupTables(assetId, conditionEntity.getRhsSubQuery());
               }
            }
         }
      }
   }

   private void adjustOrderEntities (Long assetId, List<OrderEntity> orderingEntities, QueryTable table,
            QueryTable tableToUse) {
      if (ExecueCoreUtil.isCollectionNotEmpty(orderingEntities)) {
         List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
         for (OrderEntity orderEntity : orderingEntities) {
            selectEntities.add(orderEntity.getSelectEntity());
         }
         adjustSelectEntities(assetId, selectEntities, table, tableToUse);
      }
   }

   private void adjustSelectEntities (Long assetId, List<SelectEntity> selectEntities, QueryTable table,
            QueryTable tableToUse) {
      if (ExecueCoreUtil.isCollectionNotEmpty(selectEntities)) {
         for (SelectEntity selectEntity : selectEntities) {
            if (SelectEntityType.TABLE_COLUMN.equals(selectEntity.getType())) {
               QueryTable queryTable = selectEntity.getTableColumn().getTable();
               if (table.equals(queryTable)) {
                  copyTableInfo(queryTable, tableToUse);
               }
            } else if (SelectEntityType.SUB_QUERY.equals(selectEntity.getType())) {
               handleVirtualLookupTables(assetId, selectEntity.getSubQuery());
            }
         }
      }
   }

   private void copyTableInfo (QueryTable queryTable, QueryTable tableToUse) {
      queryTable.setTableName(tableToUse.getTableName());
      queryTable.setAlias(tableToUse.getAlias());
      queryTable.setTableType(tableToUse.getTableType());
      queryTable.setVirtual(tableToUse.getVirtual());
      queryTable.setActualName(tableToUse.getActualName());
   }

   private void mergeJoinEntitiesToFromEntities (Query query) {
      List<JoinEntity> joinEntities = query.getJoinEntities();
      List<FromEntity> fromEntities = query.getFromEntities();
      if (ExecueCoreUtil.isCollectionNotEmpty(joinEntities)) {
         for (JoinEntity joinEntity : joinEntities) {
            if (!isTableExists(joinEntity.getLhsOperand().getTable(), fromEntities)) {
               FromEntity fromEntity = new FromEntity();
               fromEntity.setType(FromEntityType.TABLE);
               fromEntity.setTable(joinEntity.getLhsOperand().getTable());
               fromEntities.add(fromEntity);
            }
            if (!isTableExists(joinEntity.getRhsOperand().getTable(), fromEntities)) {
               FromEntity fromEntity = new FromEntity();
               fromEntity.setType(FromEntityType.TABLE);
               fromEntity.setTable(joinEntity.getRhsOperand().getTable());
               fromEntities.add(fromEntity);
            }
         }
      }
   }

   private boolean isTableExists (QueryTable queryTable, List<FromEntity> fromEntities) {
      boolean isExists = false;
      for (FromEntity fromEntity : fromEntities) {
         if (FromEntityType.TABLE.equals(fromEntity.getType())) {
            if (fromEntity.getTable().equals(queryTable)) {
               isExists = true;
               break;
            }
         }
      }
      return isExists;
   }

   private void optimizeJoins (Query query) {
      List<JoinEntity> joinEntities = query.getJoinEntities();
      List<JoinEntity> toBeDeleted = new ArrayList<JoinEntity>();
      // List<JoinEntity> processedJoinEntities = new ArrayList<JoinEntity>();
      for (JoinEntity joinEntity : joinEntities) {
         // processedJoinEntities.add(joinEntity);
         int lookupTableCount = 0;
         QueryTableColumn lookupQueryTableColum = null;
         QueryTableColumn factQueryTableColum = null;
         boolean isLhsTableLookup = !LookupType.None.equals(joinEntity.getLhsOperand().getTable().getTableType());
         boolean isRhsTableLookup = !LookupType.None.equals(joinEntity.getRhsOperand().getTable().getTableType());

         // check if this lookup table is participating in any other join.
         if (isLhsTableLookup) {
            lookupTableCount++;
            lookupQueryTableColum = joinEntity.getLhsOperand();
            factQueryTableColum = joinEntity.getRhsOperand();
         }
         if (isRhsTableLookup) {
            lookupTableCount++;
            lookupQueryTableColum = joinEntity.getRhsOperand();
            factQueryTableColum = joinEntity.getLhsOperand();
         }
         if (lookupTableCount == 1) {
            // whether we have to delete or not, it depends on few decisions we need to make
            List<JoinEntity> matchedLookupTableJoinEntities = getMatchedJoinEntities(joinEntity, joinEntities,
                     lookupQueryTableColum, factQueryTableColum, query);
            if (ExecueCoreUtil.isListElementsEmpty(matchedLookupTableJoinEntities)) {
               toBeDeleted.add(joinEntity);
               deleteTableFromClause(lookupQueryTableColum, query);
               updateTableSelectClause(lookupQueryTableColum, factQueryTableColum, query.getSelectEntities());
               updateTableWhereClause(lookupQueryTableColum, factQueryTableColum, query.getWhereEntities());
               updateTableSelectClause(lookupQueryTableColum, factQueryTableColum, query.getGroupingEntities());
               updateTableWhereClause(lookupQueryTableColum, factQueryTableColum, query.getHavingEntities());
               updateTableOrderByClause(lookupQueryTableColum, factQueryTableColum, query.getOrderingEntities());
               updateTablePopulationClause(lookupQueryTableColum, factQueryTableColum, query.getPopulationEntities());
            } else {
               // TODO : -VG- handle the scenario where it is participating in other join entities
               updateTableJoinClause(lookupQueryTableColum, factQueryTableColum, matchedLookupTableJoinEntities);
            }
         }
      }
      query.getJoinEntities().removeAll(toBeDeleted);
   }

   private void updateTablePopulationClause (QueryTableColumn lookupQueryTableColum,
            QueryTableColumn factQueryTableColum, List<SelectEntity> populationEntities) {
      if (ExecueCoreUtil.isListElementsNotEmpty(populationEntities)) {
         for (SelectEntity selectEntity : populationEntities) {
            if (SelectEntityType.TABLE_COLUMN.equals(selectEntity.getType())) {
               if (lookupQueryTableColum.getTable().equals(selectEntity.getTableColumn().getTable())) {
                  selectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(factQueryTableColum));
               }
            }
         }
      }
   }

   private void updateTableJoinClause (QueryTableColumn lookupQueryTableColum, QueryTableColumn factQueryTableColum,
            List<JoinEntity> matchedJoinEntities) {
      for (JoinEntity joinEntity : matchedJoinEntities) {
         if (lookupQueryTableColum.getTable().equals(joinEntity.getLhsOperand().getTable())
                  && lookupQueryTableColum.getColumn().getColumnName().equals(
                           joinEntity.getLhsOperand().getColumn().getColumnName())) {
            joinEntity.setLhsOperand(ExecueBeanCloneUtil.cloneQueryTableColumn(factQueryTableColum));
         } else if (lookupQueryTableColum.getTable().equals(joinEntity.getRhsOperand().getTable())
                  && lookupQueryTableColum.getColumn().getColumnName().equals(
                           joinEntity.getRhsOperand().getColumn().getColumnName())) {
            joinEntity.setRhsOperand(ExecueBeanCloneUtil.cloneQueryTableColumn(factQueryTableColum));
         }
      }
   }

   private List<JoinEntity> getMatchedJoinEntities (JoinEntity currJoinEntity, List<JoinEntity> joinEntities,
            QueryTableColumn lookupQueryTableColum, QueryTableColumn factQueryTableColum, Query query) {
      List<JoinEntity> tempJoinEntities = new ArrayList<JoinEntity>(joinEntities);
      tempJoinEntities.remove(currJoinEntity);
      Set<JoinEntity> matchedJoinEntities = new HashSet<JoinEntity>();
      for (JoinEntity joinEntity : tempJoinEntities) {
         if (joinEntity.getLhsOperand().getTable().equals(lookupQueryTableColum.getTable())) {
            matchedJoinEntities.add(joinEntity);
         }
         if (joinEntity.getRhsOperand().getTable().equals(lookupQueryTableColum.getTable())) {
            matchedJoinEntities.add(joinEntity);
         }
      }
      return new ArrayList<JoinEntity>(matchedJoinEntities);
   }

   private void updateTableOrderByClause (QueryTableColumn lookupQueryTableColum, QueryTableColumn factQueryTableColum,
            List<OrderEntity> orderEntities) {
      if (ExecueCoreUtil.isListElementsNotEmpty(orderEntities)) {
         for (OrderEntity orderEntity : orderEntities) {
            SelectEntity selectEntity = orderEntity.getSelectEntity();
            if (SelectEntityType.TABLE_COLUMN.equals(selectEntity.getType())) {
               if (lookupQueryTableColum.getTable().equals(selectEntity.getTableColumn().getTable())) {
                  selectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(factQueryTableColum));
               }
            }
         }
      }
   }

   private void updateTableWhereClause (QueryTableColumn lookupQueryTableColum, QueryTableColumn factQueryTableColum,
            List<ConditionEntity> conditionalEntities) {
      if (ExecueCoreUtil.isListElementsNotEmpty(conditionalEntities)) {
         for (ConditionEntity conditionEntity : conditionalEntities) {
            if (conditionEntity.isSubCondition()) {
               updateTableWhereClause(lookupQueryTableColum, factQueryTableColum, conditionEntity
                        .getSubConditionEntities());
            } else {
               if (lookupQueryTableColum.getTable().equals(conditionEntity.getLhsTableColumn().getTable())) {
                  conditionEntity.setLhsTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(factQueryTableColum));
               }
               if (QueryConditionOperandType.TABLE_COLUMN.equals(conditionEntity.getOperandType())) {
                  List<QueryTableColumn> finalRhsQueryTableColums = new ArrayList<QueryTableColumn>();
                  for (QueryTableColumn queryTableColumn : conditionEntity.getRhsTableColumns()) {
                     if (lookupQueryTableColum.getTable().equals(queryTableColumn.getTable())) {
                        finalRhsQueryTableColums.add(ExecueBeanCloneUtil.cloneQueryTableColumn(factQueryTableColum));
                     } else {
                        finalRhsQueryTableColums.add(ExecueBeanCloneUtil.cloneQueryTableColumn(queryTableColumn));
                     }
                  }
                  conditionEntity.setRhsTableColumns(finalRhsQueryTableColums);
               }
            }
         }
      }
   }

   private void deleteTableFromClause (QueryTableColumn lookupQueryTableColum, Query query) {
      FromEntity toBeDeletedFromEntity = null;
      for (FromEntity fromEntity : query.getFromEntities()) {
         if (FromEntityType.TABLE.equals(fromEntity.getType())) {
            if (lookupQueryTableColum.getTable().equals(fromEntity.getTable())) {
               toBeDeletedFromEntity = fromEntity;
               break;
            }
         }
      }
      if (ExecueCoreUtil.isListElementsNotEmpty(query.getFromEntities())) {
         query.getFromEntities().remove(toBeDeletedFromEntity);
      }
   }

   private void updateTableSelectClause (QueryTableColumn lookupQueryTableColum, QueryTableColumn factQueryTableColum,
            List<SelectEntity> selectEntities) {
      if (ExecueCoreUtil.isListElementsNotEmpty(selectEntities)) {
         for (SelectEntity selectEntity : selectEntities) {
            if (SelectEntityType.TABLE_COLUMN.equals(selectEntity.getType())) {
               if (lookupQueryTableColum.getTable().equals(selectEntity.getTableColumn().getTable())) {
                  selectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(factQueryTableColum));
               }
            }
         }
      }
   }

   /**
    * This method will find if different queries FromEntities has same aliases. Because when they get merged, there will
    * be a conflict, so in order to avoid the conflict we can modify the same aliases if any and udpate the modified
    * alias in each section of the Query
    * 
    * @param queries
    */
   private void correctAliasDuplication (List<Query> queries) {
      // From the first query, get all the fromEntities and add it to the list.
      List<FromEntity> finalFromEntities = queries.get(0).getFromEntities();

      // iterate over next set of queries.
      for (int i = 1; i < queries.size(); i++) {
         // find from entites for each query
         List<FromEntity> fromEntities = queries.get(i).getFromEntities();
         // iterate over each from entity
         for (FromEntity fromEntity : fromEntities) {
            String oldAlias = fromEntity.getTable().getAlias();
            // iterate over finalFromEntities list
            for (FromEntity fromEntity2 : finalFromEntities) {
               // check if the alias is already existing
               if (oldAlias.equalsIgnoreCase(fromEntity2.getTable().getAlias())) {
                  // created a new alias by increasing the sequence number.
                  // TODO: -VG- can use randomUtils to improve upon.
                  String newAlias = fromEntity.getTable().getAlias() + "" + aliasSequence++;
                  // set the new alias in fromEntity
                  fromEntity2.getTable().setAlias(newAlias);
                  // update new alias in each section of query
                  updateAliasSelectEntities(queries.get(i).getSelectEntities(), oldAlias, newAlias);
                  updateAliasConditionalEntities(queries.get(i).getWhereEntities(), oldAlias, newAlias);
                  updateAliasOrderEntities(queries.get(i).getOrderingEntities(), oldAlias, newAlias);
                  updateAliasSelectEntities(queries.get(i).getGroupingEntities(), oldAlias, newAlias);
                  updateAliasConditionalEntities(queries.get(i).getHavingEntities(), oldAlias, newAlias);

               }
            }
         }
         // After each iteration, we add the updated fromEntities to finalFromEntities list.
         finalFromEntities.addAll(fromEntities);
      }
   }

   /**
    * This method will update selectEntities for oldAliases with new ones.
    * 
    * @param selectEntities
    * @param oldAlias
    * @param newAlias
    */
   private void updateAliasSelectEntities (List<SelectEntity> selectEntities, String oldAlias, String newAlias) {
      if (ExecueCoreUtil.isListElementsNotEmpty(selectEntities)) {
         for (SelectEntity selectEntity : selectEntities) {
            if (SelectEntityType.TABLE_COLUMN.equals(selectEntity.getType())) {
               if (selectEntity.getTableColumn().getTable().getAlias().equalsIgnoreCase(oldAlias)) {
                  selectEntity.getTableColumn().getTable().setAlias(newAlias);
               }
            }
         }
      }
   }

   /**
    * This method will update conditionalEntities for oldAliases with new ones.
    * 
    * @param conditionalEntites
    * @param oldAlias
    * @param newAlias
    */
   private void updateAliasConditionalEntities (List<ConditionEntity> conditionalEntites, String oldAlias,
            String newAlias) {
      if (ExecueCoreUtil.isListElementsNotEmpty(conditionalEntites)) {
         for (ConditionEntity conditionEntity : conditionalEntites) {
            if (conditionEntity.getLhsTableColumn().getTable().getAlias().equalsIgnoreCase(oldAlias)) {
               conditionEntity.getLhsTableColumn().getTable().setAlias(newAlias);
            }
            if (QueryConditionOperandType.TABLE_COLUMN.equals(conditionEntity.getOperandType())) {
               for (QueryTableColumn rhsTableColumn : conditionEntity.getRhsTableColumns()) {
                  if (rhsTableColumn.getTable().getAlias().equalsIgnoreCase(oldAlias)) {
                     rhsTableColumn.getTable().setAlias(newAlias);
                  }
               }
            }
         }
      }
   }

   /**
    * This method will update orderEntities for oldAliases with new ones.
    * 
    * @param orderEntities
    * @param oldAlias
    * @param newAlias
    */
   private void updateAliasOrderEntities (List<OrderEntity> orderEntities, String oldAlias, String newAlias) {
      if (ExecueCoreUtil.isListElementsNotEmpty(orderEntities)) {
         for (OrderEntity orderEntity : orderEntities) {
            if (SelectEntityType.TABLE_COLUMN.equals(orderEntity.getSelectEntity().getType())) {
               if (orderEntity.getSelectEntity().getTableColumn().getTable().getAlias().equalsIgnoreCase(oldAlias)) {
                  orderEntity.getSelectEntity().getTableColumn().getTable().setAlias(newAlias);
               }
            }
         }
      }
   }

   /**
    * Merge the queries if the input contains more than one query object. Merge should happen segment by segment.
    * 
    * @param queries
    * @param queriesByIndexOrder 
    * @return resultQuery
    */
   private Query mergeQueriesForCombiningQueries (Map<Integer, List<Query>> queriesByIndexOrder,
            QueryCombiningType combiningType) {
      List<Query> combiningQueries = new ArrayList<Query>();
      for (Entry<Integer, List<Query>> entry : queriesByIndexOrder.entrySet()) {
         List<Query> queriesToMerge = entry.getValue();
         Query resultQuery = mergeQueries(queriesToMerge);
         combiningQueries.add(resultQuery);
      }

      // Prepare the main query
      Query mainQuery = new SQLQuery();
      mainQuery.setCombiningQueries(combiningQueries);
      mainQuery.setCombiningType(combiningType);

      return mainQuery;
   }

   private Map<Integer, List<Query>> prepareCombiningQueriesByIndexOrderMap (List<Query> queries) {
      Map<Integer, List<Query>> queriesByIndexOrder = new TreeMap<Integer, List<Query>>();
      if (ExecueCoreUtil.isCollectionEmpty(queries)) {
         return queriesByIndexOrder;
      }
      for (Query query : queries) {
         List<Query> combiningQueries = query.getCombiningQueries();
         for (int index = 0; index < combiningQueries.size(); index++) {
            Query combiningQuery = combiningQueries.get(index);
            List<Query> list = queriesByIndexOrder.get(index);
            if (list == null) {
               list = new ArrayList<Query>();
               queriesByIndexOrder.put(index, list);
            }
            list.add(combiningQuery);
         }
      }
      return queriesByIndexOrder;
   }

   /**
    * Merge the queries if the input contains more than one query object. Merge should happen segment by segment.
    * 
    * @param queries
    * @return resultQuery
    */
   private Query mergeQueries (final List<Query> queries) {
      final Query resultQuery = new SQLQuery();
      // initializes the Query Entities.
      initResultQuery(resultQuery);
      // Adding entities from each query to each other in order to merge them.
      // TODO: -RG- check the below comment
      /*
       * List can not be added directly, we have to look at the entity details in order to merge.<br/> First we extract
       * the from segment on each of the query elements in the list<br/> then we try to merge the queries<br/> while
       * doing so, make sure we verify the existanse of entity already in the first one, make sure the alias is not the
       * same.<br/> Meaning tables from 2 different queries should not be treated as a single table in merged query. If
       * the 2 tables are essetially same<br/> they will become one table in optimization(Alias optimization in query
       * optimization service)
       */

      // Setting the rollup query flag back to the result query which is required for performing rollups 
      if (ExecueCoreUtil.isCollectionNotEmpty(queries)) {
         resultQuery.setRollupQuery(queries.get(0).isRollupQuery());
      }

      // TODO : -VG- check for null pointers.
      for (final Query query : queries) {
         if (ExecueCoreUtil.isListElementsNotEmpty(query.getSelectEntities())) {
            resultQuery.getSelectEntities().addAll(query.getSelectEntities());
         }
         if (ExecueCoreUtil.isListElementsNotEmpty(query.getWhereEntities())) {
            resultQuery.getWhereEntities().addAll(query.getWhereEntities());
         }
         if (ExecueCoreUtil.isListElementsNotEmpty(query.getFromEntities())) {
            resultQuery.getFromEntities().addAll(query.getFromEntities());
         }
         if (ExecueCoreUtil.isListElementsNotEmpty(query.getOrderingEntities())) {
            resultQuery.getOrderingEntities().addAll(query.getOrderingEntities());
         }
         if (ExecueCoreUtil.isListElementsNotEmpty(query.getGroupingEntities())) {
            resultQuery.getGroupingEntities().addAll(query.getGroupingEntities());
         }
         if (ExecueCoreUtil.isListElementsNotEmpty(query.getHavingEntities())) {
            resultQuery.getHavingEntities().addAll(query.getHavingEntities());
         }
         if (ExecueCoreUtil.isListElementsNotEmpty(query.getJoinEntities())) {
            resultQuery.getJoinEntities().addAll(query.getJoinEntities());
         }
         if (query.getLimitingCondition() != null) {
            resultQuery.setLimitingCondition(query.getLimitingCondition());
         }
         if (query.getScalingFactorEntity() != null) {
            resultQuery.setScalingFactorEntity(query.getScalingFactorEntity());
         }
         if (ExecueCoreUtil.isListElementsNotEmpty(query.getPopulationEntities())) {
            resultQuery.getPopulationEntities().addAll(query.getPopulationEntities());
         }
      }
      return resultQuery;
   }

   /**
    * This method is helper method for mergeQueries in order to intialize all the query entities before merging them.
    * 
    * @param resultQuery
    */
   private void initResultQuery (final Query resultQuery) {
      resultQuery.setSelectEntities(new ArrayList<SelectEntity>());
      resultQuery.setWhereEntities(new ArrayList<ConditionEntity>());
      resultQuery.setFromEntities(new ArrayList<FromEntity>());
      resultQuery.setOrderingEntities(new ArrayList<OrderEntity>());
      resultQuery.setGroupingEntities(new ArrayList<SelectEntity>());
      resultQuery.setHavingEntities(new ArrayList<ConditionEntity>());
      resultQuery.setJoinEntities(new ArrayList<JoinEntity>());
      resultQuery.setPopulationEntities(new ArrayList<SelectEntity>());
   }

   /**
    * This method will extract the query object from the queryGenerationOutput object, create sql clauses for each of
    * the entities and finally append all the clauses to each other in proper format to generate SQL syntax
    * representation of the Query.
    * 
    * @param asset,query
    * @return queryRepresentation
    */
   public QueryRepresentation extractQueryRepresentation (Asset asset, Query query) {
      // TODO:: NK:: Should later remove this asset dependency from the code
      this.asset = asset;
      QueryStructure queryStructure = extractQueryStructure(query);

      // Get the final query structure
      QueryStructure finalQueryStructure = getFinalQueryStructure(queryStructure);

      // Get the final query string
      String finalQueryString = getQueryString(finalQueryStructure);

      // Prepare the query representation
      QueryRepresentation queryRepresentation = new QueryRepresentation();
      queryRepresentation.setQueryString(finalQueryString);
      queryRepresentation.setQueryStructure(finalQueryStructure);
      return queryRepresentation;
   }

   /**
    * @param queryStructure
    * @return
    */
   private QueryStructure getFinalQueryStructure (QueryStructure queryStructure) {
      QueryStructure finalQueryStructure = null;
      if (queryStructure.getCombiningType() == null) {
         finalQueryStructure = getFinalQueryStructureForSimpleQuery(queryStructure);
      } else {
         List<QueryStructure> combiningQueryStructures = new ArrayList<QueryStructure>();
         for (QueryStructure combiningQueryStructure : queryStructure.getCombiningQueryStructures()) {
            combiningQueryStructures.add(getFinalQueryStructureForSimpleQuery(combiningQueryStructure));
         }
         finalQueryStructure = new QueryStructure();
         finalQueryStructure.setCombiningQueryStructures(combiningQueryStructures);
         finalQueryStructure.setCombiningType(queryStructure.getCombiningType());

         // Handle any logic related to combining query structure
         rearrangeCombiningQueryStructure(finalQueryStructure);
      }

      return finalQueryStructure;
   }

   private void rearrangeCombiningQueryStructure (QueryStructure finalQueryStructure) {

      // This will remove the order by from each of the combining query structure. 
      // And add the order by to the wrapper level query structure with only aliases 
      handleOrderByForCombiningQueryStructure(finalQueryStructure);
   }

   private void handleOrderByForCombiningQueryStructure (QueryStructure finalQueryStructure) {
      List<QueryStructure> combiningQueryStructures = finalQueryStructure.getCombiningQueryStructures();
      List<QueryClauseElement> existingOrderElements = combiningQueryStructures.get(0).getOrderElements();
      if (ExecueCoreUtil.isCollectionEmpty(existingOrderElements)) {
         return;
      }
      List<QueryClauseElement> orderElements = new ArrayList<QueryClauseElement>(existingOrderElements);
      for (QueryStructure combiningQueryStructure : combiningQueryStructures) {
         // Nullify the order elements
         combiningQueryStructure.setOrderElements(null);
      }

      // Prepare the order elements with only aliases
      String orderByClauseAliasString = QueryFormatUtility.prepareOrderByClauseAliasString(orderElements);
      QueryClauseElement orderElement = new QueryClauseElement();
      orderElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      orderElement.setSimpleString(orderByClauseAliasString);

      List<QueryClauseElement> newOrderElements = new ArrayList<QueryClauseElement>();
      newOrderElements.add(orderElement);

      finalQueryStructure.setOrderElements(newOrderElements);
   }

   /**
    * This method creates the query structure representation of the Query passed.
    * 
    * @param query
    * @return queryStructure.
    */
   private QueryStructure extractQueryStructure (Query query) {
      QueryStructure queryStructure = null;
      if (query.getCombiningType() == null) {
         queryStructure = extractQueryStructureForSimpleQuery(query);
      } else {
         List<Query> combiningQueries = query.getCombiningQueries();
         List<QueryStructure> combiningQueryStructures = new ArrayList<QueryStructure>();
         for (Query combiningQuery : combiningQueries) {
            QueryStructure combiningQueryStructure = extractQueryStructureForSimpleQuery(combiningQuery);
            combiningQueryStructures.add(combiningQueryStructure);
         }
         queryStructure = new QueryStructure();
         queryStructure.setCombiningQueryStructures(combiningQueryStructures);
         queryStructure.setCombiningType(query.getCombiningType());
      }
      return queryStructure;
   }

   private QueryStructure extractQueryStructureForSimpleQuery (Query query) {

      QueryStructure queryStructure = new QueryStructure();
      queryStructure.setRollupQuery(query.isRollupQuery());
      queryStructure.setSelectElements(createSelectClause(query));
      queryStructure.setFromElements(createFromClause(query));
      queryStructure.setGroupElements(createGroupByClause(query));
      queryStructure.setOrderElements(createOrderByClause(query));
      queryStructure.setWhereElements(createWhereClause(query));
      queryStructure.setHavingElements(createHavingClause(query));
      queryStructure.setJoinElements(createJoinClause(query));
      queryStructure.setLimitElement(createLimitClause(query));

      return queryStructure;
   }

   /**
    * This method creates the different clauses from queryStructure. This will merge the from and join clauses as both
    * will come in same section for SQL query
    * 
    * @param queryStructure
    * @return queryClauses map
    */
   protected Map<String, String> prepareQueryClausesMap (QueryStructure queryStructure) {
      Map<String, String> queryClauses = new HashMap<String, String>();
      String selectClause = createQueryClause(queryStructure.getSelectElements(), QueryClauseType.SELECT);
      String whereClause = createQueryClause(queryStructure.getWhereElements(), QueryClauseType.WHERE);
      String groupByClause = createQueryClause(queryStructure.getGroupElements(), QueryClauseType.GROUPBY);
      String havingClause = createQueryClause(queryStructure.getHavingElements(), QueryClauseType.HAVING);
      String orderByClause = createQueryClause(queryStructure.getOrderElements(), QueryClauseType.ORDERBY);
      String joinClause = createQueryClause(queryStructure.getJoinElements(), QueryClauseType.JOIN);
      String fromClause = createQueryClause(queryStructure.getFromElements(), QueryClauseType.FROM);
      List<QueryClauseElement> limitClauseElements = new ArrayList<QueryClauseElement>();
      limitClauseElements.add(queryStructure.getLimitElement());
      String limitClause = createQueryClause(limitClauseElements, QueryClauseType.LIMIT);
      String joinFromClause = "";
      if (joinClause.trim().length() != 0) {
         joinFromClause = joinClause.trim();
      }
      if (fromClause.trim().length() != 0) {
         if (joinFromClause.trim().length() != 0) {
            joinFromClause += SQL_ENTITY_SEPERATOR + SQL_SPACE_DELIMITER;
         }
         joinFromClause += fromClause.trim();
      }

      queryClauses.put(SQL_SELECT_CLAUSE, selectClause);
      queryClauses.put(SQL_JOIN_FROM_CLAUSE, joinFromClause);
      queryClauses.put(SQL_WHERE_CLAUSE, whereClause);
      queryClauses.put(SQL_GROUP_BY_CLAUSE, groupByClause);
      queryClauses.put(SQL_HAVING_CLAUSE, havingClause);
      queryClauses.put(SQL_ORDER_BY_CLAUSE, orderByClause);
      queryClauses.put(SQL_LIMIT_CLAUSE, limitClause);
      return queryClauses;
   }

   private String getQueryString (QueryStructure queryStructure) {
      String queryString = QueryFormatUtility.prepareQueryString(queryStructure);
      if (logger.isDebugEnabled()) {
         logger.debug("Native SQL : " + queryString);
      }
      return queryString;
   }

   /**
    * This method is abstract method and has to be implemented by supported database vendors. This method enhances the
    * input query structure clauses based on the target database vendor
    * 
    * @param queryStructure
    */
   private QueryStructure getFinalQueryStructureForSimpleQuery (QueryStructure queryStructure) {

      // Enhance the query structure in the provider specific classes other than limit clause enhancement
      // Eg: Update the case statement in the Group by and order by in SAS with the alias
      QueryStructure enhancedQueryStructure = enhanceQueryStructure(queryStructure);

      // Check if limit clause is also present, then update the query structure accordingly using the corresponding
      // adaptor
      if (QueryFormatUtility.isValidLimitClause(enhancedQueryStructure.getLimitElement())) {
         ISQLAdaptor sqlAdaptor = SQLAdaptorFactory.getInstance().getSQLAdaptor(providerTypeValue);
         enhancedQueryStructure = sqlAdaptor.handleLimitClause(enhancedQueryStructure);
      }

      return enhancedQueryStructure;
   }

   protected abstract QueryStructure enhanceQueryStructure (QueryStructure queryStructure);

   /**
    * NOTE: Should over ride this method and handle the rollup queries in respective db specific implementations
    * @param queryStructure
    * @return {@link QueryStructure}
    */
   protected QueryStructure handleRollupQuery (QueryStructure queryStructure) {
      return queryStructure;
   }

   protected Map<String, QueryClauseElement> prepareQueryClauseElementByStringRepresentationMap (
            List<QueryClauseElement> queryClauseElements, QueryClauseType queryClauseType) {
      Map<String, QueryClauseElement> queryClauseElementByStringRepresentation = new HashMap<String, QueryClauseElement>();
      if (CollectionUtils.isEmpty(queryClauseElements)) {
         return queryClauseElementByStringRepresentation;
      }
      for (QueryClauseElement queryClauseElement : queryClauseElements) {
         String stringRepresentation = getStringRepresentationByElementType(queryClauseElement, queryClauseType);
         queryClauseElementByStringRepresentation.put(stringRepresentation, queryClauseElement);
      }
      return queryClauseElementByStringRepresentation;
   }

   /**
    * This method will prepare query clause based on query clause elements passes to it.
    * 
    * @param queryClauseElements
    * @param queryClauseType
    * @return String representation of query clause elements
    */
   private String createQueryClause (List<QueryClauseElement> queryClauseElements, QueryClauseType queryClauseType) {
      String clauseSeperator = SQL_ENTITY_SEPERATOR;
      switch (queryClauseType) {
         case HAVING:
         case WHERE:
            clauseSeperator = SQL_SPACE_DELIMITER + WHERE_CLAUSE_SEPERATOR + SQL_SPACE_DELIMITER;
            break;
      }
      StringBuilder queryClause = new StringBuilder();
      if (ExecueCoreUtil.isListElementsNotEmpty(queryClauseElements)) {
         for (QueryClauseElement queryClauseElement : queryClauseElements) {
            String stringRepresentationByElementType = getStringRepresentationByElementType(queryClauseElement,
                     queryClauseType);
            String stringRepresentationByClauseType = getStringRepresentationByClauseType(queryClauseElement,
                     queryClauseType);
            queryClause.append(stringRepresentationByElementType);
            queryClause.append(stringRepresentationByClauseType);
            queryClause.append(clauseSeperator);
            // deleting the extra SQL_ENTITY_SEPERATOR added
         }
         queryClause.delete(queryClause.length() - clauseSeperator.length(), queryClause.length());
      }
      return queryClause.toString();
   }

   protected String getStringRepresentationByElementType (QueryClauseElement queryClauseElement,
            QueryClauseType queryClauseType) {
      StringBuilder queryClauseStringBuilder = new StringBuilder();

      switch (queryClauseElement.getQueryElementType()) {
         case SIMPLE_STRING:
            queryClauseStringBuilder.append(queryClauseElement.getSimpleString());
            break;
         case CASE_STATEMENT:
            List<String> selectCaseElement = queryClauseElement.getCaseStatement();
            for (String str : selectCaseElement) {
               queryClauseStringBuilder.append(str).append(SQL_SPACE_DELIMITER);
            }
            queryClauseStringBuilder.delete(queryClauseStringBuilder.length() - SQL_SPACE_DELIMITER.length(),
                     queryClauseStringBuilder.length());
            break;
         case SUB_QUERY:
            if (QueryClauseType.WHERE.equals(queryClauseType)) {
               queryClauseStringBuilder.append(queryClauseElement.getSimpleString()).append(SQL_SPACE_DELIMITER);
            }
            QueryStructure subQueryElement = queryClauseElement.getSubQuery();
            queryClauseStringBuilder.append(SQL_SUBQUERY_START_WRAPPER).append(getQueryString(subQueryElement)).append(
                     SQL_SUBQUERY_END_WRAPPER);
            break;
      }
      return queryClauseStringBuilder.toString();
   }

   private String getStringRepresentationByClauseType (QueryClauseElement queryClauseElement,
            QueryClauseType queryClauseType) {
      StringBuilder queryClauseStringBuilder = new StringBuilder();

      switch (queryClauseType) {
         case FROM:
            if (queryClauseElement.getAlias() != null) {
               queryClauseStringBuilder.append(SQL_SPACE_DELIMITER).append(queryClauseElement.getAlias());
            }
            break;
         case SELECT:
            if (queryClauseElement.getAlias() != null) {
               queryClauseStringBuilder.append(SQL_SPACE_DELIMITER).append(SQL_ALIAS).append(SQL_SPACE_DELIMITER)
                        .append(queryClauseElement.getAlias());
            }
         case ORDERBY:
            if (QueryClauseType.ORDERBY.equals(queryClauseType)) {
               queryClauseStringBuilder.append(SQL_SPACE_DELIMITER).append(
                        queryClauseElement.getOrderEntityType().getValue());
            }
            break;
      }
      return queryClauseStringBuilder.toString();
   }

   /**
    * This method is helper method to extractQueryString() in order to generate SQL Select clause.
    * 
    * @param query
    * @return selectElements
    */
   private List<QueryClauseElement> createSelectClause (Query query) {
      List<QueryClauseElement> selectElements = new ArrayList<QueryClauseElement>();
      if (ExecueCoreUtil.isListElementsNotEmpty(query.getSelectEntities())) {
         for (SelectEntity entity : query.getSelectEntities()) {
            selectElements
                     .add(createSelectClauseElement(entity, query.getScalingFactorEntity(), QueryClauseType.SELECT));
         }
      }
      return selectElements;
   }

   /**
    * This method is helper method to extractQueryString() in order to generate SQL groupBy clause.
    * 
    * @param query
    * @return queryClauseElements
    */
   private List<QueryClauseElement> createGroupByClause (Query query) {
      List<QueryClauseElement> groupElements = new ArrayList<QueryClauseElement>();
      if (ExecueCoreUtil.isListElementsNotEmpty(query.getGroupingEntities())) {
         for (SelectEntity selectEntity : query.getGroupingEntities()) {
            groupElements.add(createSelectClauseElement(selectEntity, query.getScalingFactorEntity(),
                     QueryClauseType.GROUPBY));
         }
      }
      return groupElements;
   }

   /**
    * This method is helper method to extractQueryString() in order to generate SQL OrderBy clause.
    * 
    * @param query
    * @return orderElements
    */
   private List<QueryClauseElement> createOrderByClause (Query query) {
      List<QueryClauseElement> orderElements = new ArrayList<QueryClauseElement>();
      if (ExecueCoreUtil.isListElementsNotEmpty(query.getOrderingEntities())) {
         for (OrderEntity orderEntity : query.getOrderingEntities()) {
            QueryClauseElement orderClauseElement = createSelectClauseElement(orderEntity.getSelectEntity(), query
                     .getScalingFactorEntity(), QueryClauseType.ORDERBY);
            orderClauseElement.setOrderEntityType(orderEntity.getOrderType());
            orderElements.add(orderClauseElement);
         }
      }
      return orderElements;
   }

   /**
    * This method is helper method to extractQueryString() in order to generate SQL From clause.
    * 
    * @param query
    * @return whereElements
    */
   private List<QueryClauseElement> createWhereClause (Query query) {
      List<QueryClauseElement> whereElements = new ArrayList<QueryClauseElement>();
      if (ExecueCoreUtil.isListElementsNotEmpty(query.getWhereEntities())) {
         for (ConditionEntity whereEntity : query.getWhereEntities()) {
            if (whereEntity.isSubCondition()) {
               QueryClauseElement queryClauseElement = new QueryClauseElement();
               StringBuilder subConditionElement = new StringBuilder();
               queryClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
               subConditionElement.append(SQL_SUBQUERY_START_WRAPPER);
               int index = 0;
               for (ConditionEntity subCondition : whereEntity.getSubConditionEntities()) {
                  subConditionElement.append(createConditionalClauseElement(subCondition,
                           query.getScalingFactorEntity(), QueryClauseType.WHERE).getSimpleString());
                  if (index != whereEntity.getSubConditionEntities().size() - 1) {
                     subConditionElement.append(SQL_SPACE_DELIMITER);
                     subConditionElement.append(WHERE_CLAUSE_OR_SEPERATOR);
                     subConditionElement.append(SQL_SPACE_DELIMITER);
                  }
                  index++;
               }
               subConditionElement.append(SQL_SUBQUERY_END_WRAPPER);
               queryClauseElement.setSimpleString(subConditionElement.toString());
               whereElements.add(queryClauseElement);
            } else {
               whereElements.add(createConditionalClauseElement(whereEntity, query.getScalingFactorEntity(),
                        QueryClauseType.WHERE));
            }
         }
      }
      return whereElements;
   }

   /**
    * This method is helper method to extractQueryString() in order to generate SQL having clause.
    * 
    * @param query
    * @return havingElements
    */
   private List<QueryClauseElement> createHavingClause (Query query) {
      List<QueryClauseElement> havingElements = new ArrayList<QueryClauseElement>();
      if (ExecueCoreUtil.isListElementsNotEmpty(query.getHavingEntities())) {
         for (ConditionEntity havingEntity : query.getHavingEntities()) {
            havingElements.add(createConditionalClauseElement(havingEntity, query.getScalingFactorEntity(),
                     QueryClauseType.HAVING));
         }
      }
      return havingElements;
   }

   /**
    * This method will create query clause element for limit clause
    * 
    * @param query
    * @return queryClauseElement
    */
   private QueryClauseElement createLimitClause (Query query) {
      QueryClauseElement queryClauseElement = null;
      if (query.getLimitingCondition() != null) {
         StringBuilder limitValue = new StringBuilder();
         limitValue.append(query.getLimitingCondition().getStartingNumber());
         limitValue.append(SQL_ENTITY_SEPERATOR);
         limitValue.append(query.getLimitingCondition().getEndingNumber());
         queryClauseElement = new QueryClauseElement();
         queryClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
         queryClauseElement.setSimpleString(limitValue.toString());
      }
      return queryClauseElement;
   }

   /**
    * This method creates join clause for SQL query which in turn goes to From Clause of SQL query. Syntax of putting
    * joins in SQL query : (Table1 Table1Alias JOIN_TYPE Table2 Table2Alias ON Table1Alias.Colum1 = Table2Alias.Colum2)
    * 
    * @param joinEntities
    * @return joinClauseElements
    */
   private List<QueryClauseElement> createJoinClause (Query query) {
      List<QueryClauseElement> joinClauseElements = new ArrayList<QueryClauseElement>();
      if (ExecueCoreUtil.isListElementsNotEmpty(query.getJoinEntities())) {
         List<JoinEntity> joinEntities = new ArrayList<JoinEntity>(query.getJoinEntities());
         joinClauseElements = createJoinCondition(joinEntities);
      }
      return joinClauseElements;
   }

   /**
    * This method will be used to populate the query clause element for SelectEntity passed. If entity type is table
    * column, then it is simple string,if type is sub query, then we need to populate the sub query and alias separate.
    * 
    * @param entity
    * @param scalingFactorEntity
    * @param querySectionType
    * @return selectClauseElement
    */
   private QueryClauseElement createSelectClauseElement (SelectEntity entity, QueryTableColumn scalingFactorEntity,
            QueryClauseType querySectionType) {
      QueryClauseElement selectClauseElement = new QueryClauseElement();
      switch (entity.getType()) {
         case TABLE_COLUMN:
            if (entity.getRange() == null) {
               String columnString = createColumString(entity.getTableColumn());

               if (QueryClauseType.ORDERBY == querySectionType) {
                  // Handle string to date on Order By
                  boolean columnStringBased = isColumnStringBased(entity.getTableColumn().getColumn().getDataType());
                  String columnDBDateFormat = null;
                  if (columnStringBased) {
                     columnDBDateFormat = getColumnDBDateFormat(entity.getTableColumn().getColumn());
                  }
                  columnString = handleTFFunctionForStringBasedColumn(columnString, columnDBDateFormat,
                           columnStringBased, true);
               }

               // if function name is not empty , apply the function name on column.
               if (entity.getFunctionName() != null) {
                  columnString = applyFunctionColumn(columnString, entity.getTableColumn(), entity.getFunctionName(),
                           scalingFactorEntity);
               } else {
                  if (DataType.DATETIME.equals(entity.getTableColumn().getColumn().getDataType())
                           || DataType.DATE.equals(entity.getTableColumn().getColumn().getDataType())) {
                     if (!PublishedFileType.RDBMS.equals(asset.getOriginType())
                              && entity.getTableColumn().getColumn().getDataFormat() != null
                              && entity.getTableColumn().getColumn().getFileDateFormat() != null) {
                        DateFormat dateFormat = swiConfigurationService.getSupportedDateFormat(entity.getTableColumn()
                                 .getColumn().getFileDateFormat(), asset.getDataSource().getProviderType());
                        String fileDbFormat = dateFormat.getDbFormat();
                        boolean columnStringBased = isColumnStringBased(entity.getTableColumn().getColumn()
                                 .getDataType());
                        columnString = handleTFFunctionForStringBasedColumn(columnString, fileDbFormat,
                                 columnStringBased, true);
                     }
                  }
               }
               // handle the round function
               columnString = applyRoundLimit(columnString, entity.getRoundFunctionTargetColumn());
               
               // handle the date to string formatting
               columnString = applyDateToStringFormatting(columnString, entity);
               selectClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
               selectClauseElement.setSimpleString(columnString);
            } else {
               List<RangeDetail> rangeDetails = new ArrayList<RangeDetail>();
               for (RangeDetail rangeDetail : entity.getRange().getRangeDetails()) {
                  rangeDetails.add(rangeDetail);
               }
               selectClauseElement.setQueryElementType(QueryElementType.CASE_STATEMENT);
               selectClauseElement.setCaseStatement(createCaseStatementOnRange(rangeDetails, entity.getTableColumn()
                        .getTable(), entity.getTableColumn().getColumn()));
            }
            break;
         case FORMULA:
            // TODO : VG - create selectClause when the entity is FORMULA
            // Get the formula from SWI schema service and apply it on the column.
            // TODO: -VG- minimum implementation of query static formula
            QueryFormula formula = entity.getFormula();
            if (QueryFormulaType.STATIC.equals(formula.getQueryFormulaType())) {
               selectClauseElement.setSimpleString(entity.getFormula().getStaticFormula());
               selectClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
            } else if (QueryFormulaType.DYNAMIC.equals(formula.getQueryFormulaType())) {
               String firstOperand = null;
               if (QueryFormulaOperandType.TABLE_COLUMN.equals(formula.getFirstOperandType())) {
                  firstOperand = createColumString(formula.getFirstOperandQueryTableColumn());
               } else if (QueryFormulaOperandType.VALUE.equals(formula.getFirstOperandType())) {
                  firstOperand = formula.getFirstOperandValue();
               }
               String secondOperand = null;
               if (QueryFormulaOperandType.TABLE_COLUMN.equals(formula.getSecondOperandType())) {
                  secondOperand = createColumString(formula.getSecondOperandQueryTableColumn());
               } else if (QueryFormulaOperandType.VALUE.equals(formula.getSecondOperandType())) {
                  secondOperand = formula.getSecondOperandValue();
               }
               selectClauseElement.setSimpleString(firstOperand + formula.getArithmeticOperatorType().getValue()
                        + secondOperand);
               selectClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
            }
            break;
         case VALUE:
            StringBuilder valueColum = new StringBuilder();
            switch (entity.getQueryValue().getDataType()) {
               case CHARACTER:
               case STRING:
               case TIME:
               case DATE:
               case DATETIME:
                  valueColum.append(QUOTE).append(entity.getQueryValue().getValue()).append(QUOTE);
                  break;
               case INT:
               case LARGE_INTEGER:
               case NUMBER:
                  valueColum.append(entity.getQueryValue().getValue());
                  break;
            }
            selectClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
            selectClauseElement.setSimpleString(valueColum.toString());
            break;
         case SUB_QUERY:
            selectClauseElement.setQueryElementType(QueryElementType.SUB_QUERY);
            selectClauseElement.setSubQuery(extractQueryStructure(entity.getSubQuery()));
            selectClauseElement.setAlias(entity.getSubQuery().getAlias());
            break;
      }
      switch (querySectionType) {
         case ORDERBY:
         case SELECT:
            if (entity.getAlias() != null) {
               selectClauseElement.setAlias(entity.getAlias());
            }
            break;
      }
      return selectClauseElement;
   }

   private String applyDateToStringFormatting (String columnString, SelectEntity entity) {
      if (entity.isDateAsStringRequired()) {
         return prepareColumnForDateToStringHandling(columnString, getColumnDBDateFormat(entity.getTableColumn().getColumn()));   
      }
      return columnString;
   }

   /**
    * @param entity
    * @param columnDBDateFormat
    * @return
    */
   private String getColumnDBDateFormat (QueryColumn queryColumn) {
      String columnDBDateFormat = null;
      if (!StringUtils.isBlank(queryColumn.getDataFormat())) {
         DateFormat dateFormat = swiConfigurationService.getSupportedDateFormat(queryColumn.getDataFormat(), asset
                  .getDataSource().getProviderType());
         if (dateFormat != null && !StringUtils.isBlank(dateFormat.getDbFormat())) {
            columnDBDateFormat = dateFormat.getDbFormat();
         }
      }
      return columnDBDateFormat;
   }

   /**
    * This method will be used to populate the query clause element for ConditionalEntity passed. If entity type is
    * table colum, then simple string, if sub query then lhs will be simple string and rhs will be sub query. so both
    * will be set
    * 
    * @param conditionEntity
    * @param scalingFactorEntity
    * @param querySectionType
    * @return conditionClauseElement
    */
   private QueryClauseElement createConditionalClauseElement (ConditionEntity conditionEntity,
            QueryTableColumn scalingFactorEntity, QueryClauseType querySectionType) {
      QueryClauseElement conditionClauseElement = new QueryClauseElement();

      DataType lhsColumnDataType = conditionEntity.getLhsTableColumn().getColumn().getDataType();

      boolean columnStringBased = isColumnStringBased(lhsColumnDataType);
      String columnDBDateFormat = getColumnDBDateFormat(conditionEntity.getLhsTableColumn().getColumn());

      String lhsColumnString = createColumString(conditionEntity.getLhsTableColumn());

      boolean valueTFBased = isValueTFBased(conditionEntity);

      lhsColumnString = handleTFFunctionForStringBasedColumn(lhsColumnString, columnDBDateFormat, columnStringBased,
               valueTFBased);
      // TODO : -VG- need to take care of group functions on left and right side of where condition
      // if function name is not empty , apply the function name on colum.
      switch (querySectionType) {
         case HAVING:
            if (conditionEntity.getLhsFunctionName() != null) {
               lhsColumnString = applyFunctionColumn(lhsColumnString, conditionEntity.getLhsTableColumn(),
                        conditionEntity.getLhsFunctionName(), scalingFactorEntity);
            }
            break;
      }

      // create a string builder and put the left hand side string in it along with operator
      StringBuilder conditionElement = new StringBuilder();
      conditionElement.append(lhsColumnString).append(SQL_SPACE_DELIMITER).append(
               conditionEntity.getOperator().getValue());
      // If operator is of type is null and is not null, then it means that right side is not required
      if (OperatorType.IS_NULL.equals(conditionEntity.getOperator())
               || OperatorType.IS_NOT_NULL.equals(conditionEntity.getOperator())) {
         conditionClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
         conditionClauseElement.setSimpleString(conditionElement.toString());
      }
      // for all other operators, we have to do it on basis of type of operand to handle
      else {
         switch (conditionEntity.getOperandType()) {
            case TABLE_COLUMN:
               List<QueryTableColumn> rhsTableColumns = conditionEntity.getRhsTableColumns();
               if (rhsTableColumns.size() > 1) {
                  conditionElement.append(SQL_SUBQUERY_START_WRAPPER);
               }
               for (QueryTableColumn queryTableColumn : conditionEntity.getRhsTableColumns()) {
                  conditionElement.append(createColumString(queryTableColumn)).append(SQL_ENTITY_SEPERATOR);
               }
               conditionElement.deleteCharAt(conditionElement.length() - SQL_ENTITY_SEPERATOR.length());
               if (rhsTableColumns.size() > 1) {
                  conditionElement.append(SQL_SUBQUERY_END_WRAPPER);
               }
               conditionClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
               conditionClauseElement.setSimpleString(conditionElement.toString());
               break;
            case VALUE:
               List<QueryValue> rhsValues = conditionEntity.getRhsValues();
               switch (conditionEntity.getOperator()) {
                  case IN:
                  case NOT_IN:
                     conditionElement.append(SQL_SUBQUERY_START_WRAPPER);
                     for (QueryValue queryValue : rhsValues) {
                        switch (queryValue.getDataType()) {
                           case NUMBER:
                           case LARGE_INTEGER:
                           case INT:
                              conditionElement.append(queryValue.getValue()).append(SQL_ENTITY_SEPERATOR);
                              break;
                           case CHARACTER:
                           case STRING:
                           case TIME:
                           case DATE:
                           case DATETIME:
                              conditionElement.append(SQL_SPACE_DELIMITER).append(
                                       createValueString(queryValue, columnDBDateFormat, columnStringBased)).append(
                                       SQL_ENTITY_SEPERATOR);
                              break;
                        }
                     }
                     conditionElement.deleteCharAt(conditionElement.length() - SQL_ENTITY_SEPERATOR.length());
                     conditionElement.append(SQL_SUBQUERY_END_WRAPPER);
                     conditionClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
                     conditionClauseElement.setSimpleString(conditionElement.toString());
                     break;
                  case BETWEEN:
                     for (QueryValue queryValue : rhsValues) {
                        switch (queryValue.getDataType()) {
                           case NUMBER:
                           case LARGE_INTEGER:
                           case INT:
                              conditionElement.append(SQL_SPACE_DELIMITER).append(queryValue.getValue()).append(
                                       SQL_SPACE_DELIMITER).append(WHERE_CLAUSE_SEPERATOR);
                              break;
                           case CHARACTER:
                           case STRING:
                           case TIME:
                           case DATE:
                           case DATETIME:
                              conditionElement.append(SQL_SPACE_DELIMITER).append(
                                       createValueString(queryValue, columnDBDateFormat, columnStringBased)).append(
                                       SQL_SPACE_DELIMITER).append(WHERE_CLAUSE_SEPERATOR);
                              break;
                        }
                     }
                     conditionElement.delete(conditionElement.length() - WHERE_CLAUSE_SEPERATOR.length()
                              - SQL_SPACE_DELIMITER.length(), conditionElement.length());
                     conditionClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
                     conditionClauseElement.setSimpleString(conditionElement.toString());
                     break;
                  default:
                     for (QueryValue queryValue : rhsValues) {
                        switch (queryValue.getDataType()) {
                           case NUMBER:
                           case LARGE_INTEGER:
                           case INT:
                              conditionElement.append(queryValue.getValue()).append(SQL_ENTITY_SEPERATOR);
                              break;
                           case CHARACTER:
                           case STRING:
                           case TIME:
                           case DATE:
                           case DATETIME:
                              conditionElement.append(SQL_SPACE_DELIMITER).append(
                                       createValueString(queryValue, columnDBDateFormat, columnStringBased)).append(
                                       SQL_ENTITY_SEPERATOR);
                              break;
                        }
                     }
                     conditionElement.deleteCharAt(conditionElement.length() - SQL_ENTITY_SEPERATOR.length());
                     conditionClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
                     conditionClauseElement.setSimpleString(conditionElement.toString());
                     break;
               }
               break;
            case SUB_QUERY:
               conditionClauseElement.setSimpleString(conditionElement.toString());
               conditionClauseElement.setQueryElementType(QueryElementType.SUB_QUERY);
               conditionClauseElement.setSubQuery(extractQueryStructure(conditionEntity.getRhsSubQuery()));
               conditionClauseElement.setAlias(conditionEntity.getRhsSubQuery().getAlias());
               break;
         }
      }
      return conditionClauseElement;
   }

   private String handleTFFunctionForStringBasedColumn (String lhsColumnString, String columnDBDateFormat,
            boolean columnStringBased, boolean valueTFBased) {
      String enhancedLHSColumnString = lhsColumnString;
      if (!StringUtils.isBlank(columnDBDateFormat) && columnStringBased && valueTFBased) {
         enhancedLHSColumnString = prepareColumnForStringToDateHandling(lhsColumnString, columnDBDateFormat);
      }
      return enhancedLHSColumnString;
   }

   private boolean isValueTFBased (ConditionEntity conditionEntity) {
      boolean valueTFBased = true;
      if (QueryConditionOperandType.VALUE == conditionEntity.getOperandType()
               && ExecueCoreUtil.isCollectionNotEmpty(conditionEntity.getRhsValues())) {
         for (QueryValue queryValue : conditionEntity.getRhsValues()) {
            if (getCoreConfigurationService().getCubeAllValue().equalsIgnoreCase(queryValue.getValue())) {
               valueTFBased = false;
               break;
            }
         }
      }
      return valueTFBased;
   }

   /**
    * @param lhsColumnString
    * @param columnDBDateFormat
    * @param sb
    */
   protected abstract String prepareColumnForStringToDateHandling (String columnString, String columnDBDateFormat);
   
   protected abstract String prepareColumnForDateToStringHandling (String columnString, String columnDBDateFormat);

   /**
    * @param lhsColumnDataType
    */
   private boolean isColumnStringBased (DataType lhsColumnDataType) {
      boolean columnStringBased = false;
      if (DataType.CHARACTER == lhsColumnDataType || DataType.STRING == lhsColumnDataType
               || DataType.TEXT == lhsColumnDataType) {
         columnStringBased = true;
      }
      return columnStringBased;
   }

   private String createValueString (QueryValue queryValue, String columnDBDateFormat, boolean columnStringBased) {
      if (!StringUtils.isBlank(columnDBDateFormat)
               && !getCoreConfigurationService().getCubeAllValue().equalsIgnoreCase(queryValue.getValue())) {
         return prepareDateFormOfValue(queryValue, columnDBDateFormat);
      } else {
         StringBuilder stringBuilder = new StringBuilder();
         if (columnStringBased) {
            stringBuilder.append(QUOTE).append(queryValue.getValue()).append(QUOTE);
         } else {
            // Ideally, This should never come here. But sometimes if we have defined our source tables lookup and fact table column
            // with different datatypes for lookup value column. 
            // In order to fix that scenario, we also check for column string based here
            stringBuilder.append(queryValue.getValue());
         }
         return stringBuilder.toString();
      }
   }

   /**
    * @param queryValue
    * @return
    */
   protected abstract String prepareDateFormOfValue (QueryValue queryValue, String columnDBDateFormat);

   /**
    * This method will create string representation for Column using QueryTableColum object.
    * 
    * @param queryTableColumn
    * @return string representation.
    */
   protected String createColumString (QueryTableColumn queryTableColumn) {
      StringBuilder queryColum = new StringBuilder();
      // if column name is "*" then we wont apply alias and distinct keyword if any to column.
      if (queryTableColumn.getColumn().getColumnName().equalsIgnoreCase(SQL_WILD_CHAR)) {
         queryColum.append(queryTableColumn.getColumn().getColumnName());
      } else {
         // if distinct keyword is set, we will apply
         if (queryTableColumn.getColumn().isDistinct()) {
            queryColum.append(SQL_DISTINCT_KEYWORD).append(SQL_SPACE_DELIMITER);
         }
         // append alias and column name to columString
         ISQLAdaptor sqlAdaptor = SQLAdaptorFactory.getInstance().getSQLAdaptor(providerTypeValue);
         queryColum.append(sqlAdaptor.createColumRepresentationQueryTableColumn(queryTableColumn));
      }

      return queryColum.toString();
   }

   /**
    * This method is helper method to createSelectClause() in order to append the function name to the colum name.
    * 
    * @param columnString
    * @param columnInfo 
    * @param functionName
    * @param scalingFactorEntity
    * @return string with function name appended to colum.
    */
   protected String applyFunctionColumn (String columnString, QueryTableColumn columnInfo, StatType functionName,
            QueryTableColumn scalingFactorEntity) {
      StringBuilder stringBuilder = new StringBuilder();
      if (scalingFactorEntity != null) {
         String scalingFactorColum = createColumString(scalingFactorEntity);
         switch (functionName) {
            case SUM:
               stringBuilder.append(StatType.SUM.getValue()).append(SQL_FUNCTION_START_WRAPPER).append(
                        getColumnStringCastToDataType(columnString, columnInfo.getColumn().getDataType())).append(
                        SQL_WILD_CHAR)
                        .append(
                                 getColumnStringCastToDataType(scalingFactorColum, scalingFactorEntity.getColumn()
                                          .getDataType())).append(SQL_FUNCTION_END_WRAPPER);
               break;
            case AVERAGE:
               stringBuilder.append(StatType.SUM.getValue()).append(SQL_FUNCTION_START_WRAPPER).append(
                        getColumnStringCastToDataType(columnString, columnInfo.getColumn().getDataType())).append(
                        SQL_WILD_CHAR)
                        .append(
                                 getColumnStringCastToDataType(scalingFactorColum, scalingFactorEntity.getColumn()
                                          .getDataType())).append(SQL_FUNCTION_END_WRAPPER).append(DIVISION_SYMBOL)
                        .append(StatType.SUM.getValue()).append(SQL_FUNCTION_START_WRAPPER).append(
                                 getColumnStringCastToDataType(scalingFactorColum, scalingFactorEntity.getColumn()
                                          .getDataType())).append(SQL_FUNCTION_END_WRAPPER);
               break;
            case COUNT:
               stringBuilder.append(StatType.SUM.getValue()).append(SQL_FUNCTION_START_WRAPPER)
                        .append(
                                 getColumnStringCastToDataType(scalingFactorColum, scalingFactorEntity.getColumn()
                                          .getDataType())).append(SQL_FUNCTION_END_WRAPPER);
               break;
            case STDDEV:
               stringBuilder.append(getSTDDEV(functionName, getColumnStringCastToDataType(columnString, columnInfo
                        .getColumn().getDataType())));
               break;
         }
      } else if (functionName == StatType.STDDEV) {
         stringBuilder.append(getSTDDEV(functionName, getColumnStringCastToDataType(columnString, columnInfo
                  .getColumn().getDataType())));
      } else if (functionName == StatType.MINIMUM || functionName == StatType.MAXIMUM) {
         stringBuilder.append(getMinMax(functionName, getColumnStringCastToDataType(columnString, columnInfo
                  .getColumn().getDataType()), columnInfo.getColumn().getDataType()));
      } else {
         String castColumnString = columnString;
         if (StatType.AVERAGE == functionName || StatType.SUM == functionName || StatType.STDDEV == functionName) {
            castColumnString = getColumnStringCastToDataType(columnString, columnInfo.getColumn().getDataType());
         } else if (StatType.COUNT == functionName) {
            castColumnString = getColumnStringCastToDataType(columnInfo, columnString);
         }
         stringBuilder.append(functionName.getValue()).append(SQL_FUNCTION_START_WRAPPER).append(castColumnString);
         stringBuilder.append(SQL_FUNCTION_END_WRAPPER);
      }
      return stringBuilder.toString();
   }

   /**
    * For vendor specific implementation purposes
    * 
    * @param columnInfo
    * @return
    */
   protected String getColumnStringCastToDataType (QueryTableColumn columnInfo, String createdColumnString) {
      return createdColumnString;
   }

   /**
    * For vendor specific implementation purposes
    * 
    * @param columnString
    * @return
    */
   protected String getColumnStringCastToDataType (String columnString, DataType dataType) {
      return columnString;
   }

   protected String getSTDDEV (StatType functionName, String columnString) {
      StringBuilder sb = new StringBuilder();
      sb.append(functionName.getValue()).append(SQL_FUNCTION_START_WRAPPER).append(columnString).append(
               SQL_FUNCTION_END_WRAPPER);
      return sb.toString();
   }

   protected String getMinMax (StatType functionName, String columnString, DataType columnDataType) {
      StringBuilder sb = new StringBuilder();
      sb.append(functionName.getValue()).append(SQL_FUNCTION_START_WRAPPER).append(columnString).append(
               SQL_FUNCTION_END_WRAPPER);
      return sb.toString();
   }

   protected String applyRoundLimit (String lhsColumnString, QueryColumn roundFuntionTargetColumn) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(lhsColumnString);
      if (roundFuntionTargetColumn != null) {
         stringBuilder = new StringBuilder();
         stringBuilder.append(SQL_ROUND_FUNCTION).append(SQL_FUNCTION_START_WRAPPER).append(lhsColumnString).append(
                  SQL_ENTITY_SEPERATOR).append(roundFuntionTargetColumn.getScale()).append(SQL_FUNCTION_END_WRAPPER);
      }
      return stringBuilder.toString();
   }

   /**
    * This method is helper method to extractQueryString() in order to generate SQL From clause.
    * 
    * @param query
    * @return fromClauseElements
    */
   private List<QueryClauseElement> createFromClause (Query query) {
      List<JoinEntity> joinEntities = query.getJoinEntities();
      List<FromEntity> fromEntities = query.getFromEntities();
      List<QueryTable> joinTables = new ArrayList<QueryTable>();
      if (joinEntities != null) {
         for (JoinEntity joinEntity : joinEntities) {
            joinTables.add(joinEntity.getLhsOperand().getTable());
            joinTables.add(joinEntity.getRhsOperand().getTable());
         }
      }

      List<FromEntity> finalFromEntities = new ArrayList<FromEntity>();
      // add all the other from entities which is of type SubQuery
      for (FromEntity fromEntity : fromEntities) {
         if (FromEntityType.TABLE.equals(fromEntity.getType())) {
            if (!isQueryTableExists(fromEntity.getTable(), joinTables)) {
               finalFromEntities.add(fromEntity);
            }
         } else {
            finalFromEntities.add(fromEntity);
         }
      }
      List<QueryClauseElement> fromClauseElements = new ArrayList<QueryClauseElement>();
      QueryClauseElement fromClauseElement = null;
      if (ExecueCoreUtil.isListElementsNotEmpty(finalFromEntities)) {
         for (FromEntity fromEntity : finalFromEntities) {
            fromClauseElement = new QueryClauseElement();
            switch (fromEntity.getType()) {
               case TABLE:
                  fromClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
                  fromClauseElement.setAlias(fromEntity.getTable().getAlias());
                  ISQLAdaptor sqlAdaptor = SQLAdaptorFactory.getInstance().getSQLAdaptor(providerTypeValue);
                  fromClauseElement.setSimpleString(sqlAdaptor.createTableRepresentationQueryTableColumn(fromEntity
                           .getTable(), false));
                  break;
               case SUB_QUERY:
                  fromClauseElement.setQueryElementType(QueryElementType.SUB_QUERY);
                  fromClauseElement.setAlias(fromEntity.getSubQuery().getAlias());
                  fromClauseElement.setSubQuery(extractQueryStructure(fromEntity.getSubQuery()));
                  break;
            }
            fromClauseElements.add(fromClauseElement);
         }
      }
      return fromClauseElements;
   }

   private boolean isQueryTableExists (QueryTable currQueryTable, List<QueryTable> joinTables) {
      boolean isExists = false;
      for (QueryTable joinTable : joinTables) {
         if (joinTable.equals(currQueryTable)) {
            isExists = true;
            break;
         }
      }
      return isExists;
   }

   /**
    * This method will create join clause string representation for the list of joinEntities passed to it.
    * 
    * @param joinEntities
    * @return joinClauseElements
    */
   private List<QueryClauseElement> createJoinCondition (List<JoinEntity> joinEntities) {
      // saved the joinEntities for further reference
      List<JoinEntity> originalJoinEntities = new ArrayList<JoinEntity>(joinEntities);
      // find list of tables which are joined
      List<QueryTable> joinedTablesList = findJoinedTables(joinEntities);
      StringBuilder stringRepresentation = createStringRepresentation(joinedTablesList, originalJoinEntities);
      QueryClauseElement queryClauseElement = new QueryClauseElement();
      queryClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      queryClauseElement.setSimpleString(stringRepresentation.toString());
      List<QueryClauseElement> queryClauseElements = new ArrayList<QueryClauseElement>();
      queryClauseElements.add(queryClauseElement);
      return queryClauseElements;
   }

   /**
    * This method will be used to prepare string representation of join clause using list of joined tables and
    * Joinentities in hand.
    * 
    * @param joinedTablesList
    * @param originalJoinEntities
    * @return string representation
    */
   private StringBuilder createStringRepresentation (List<QueryTable> joinedTablesList,
            List<JoinEntity> originalJoinEntities) {
      StringBuilder joinClauseStringBuilder = new StringBuilder();
      // add the first table in list to string builder
      ISQLAdaptor sqlAdaptor = SQLAdaptorFactory.getInstance().getSQLAdaptor(providerTypeValue);
      joinClauseStringBuilder.append(sqlAdaptor
               .createTableRepresentationQueryTableColumn(joinedTablesList.get(0), true));

      // assume we got a list of joined tables something like this A,B,C,G,F. We will take B as first element and run
      // the inner loop from A to B and find and join if exists between them. Then we will move on to C as next element
      // in list and try to find joins between A-C and B-C, therefore the inner loop has to be from 0 to i-1 location if
      // i is location of element for which we are searching joins currently. In order to distinguish between types of
      // joins, we have maintained count for each type. Assume that we got one inner join and one outer join between
      // A-B, then we have to represent it something like A inner join B on 'condition' left outer join B on
      // 'condition'.
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
                        countConditionalAppender(joinClauseStringBuilder, innerJoinCount, joinEntity, joinedTablesList
                                 .get(i));
                        innerJoinCount++;
                        break;
                     case LEFT_OUTER:
                        countConditionalAppender(joinClauseStringBuilder, leftOuterJoinCount, joinEntity,
                                 joinedTablesList.get(i));
                        leftOuterJoinCount++;
                        break;
                     case RIGHT_OUTER:
                        countConditionalAppender(joinClauseStringBuilder, rightOuterJoinCount, joinEntity,
                                 joinedTablesList.get(i));
                        rightOuterJoinCount++;
                        break;
                  }
               }
            }
         }
      }
      return joinClauseStringBuilder;
   }

   /**
    * This method will be used to append joinEntity on basis of count passes to it.
    * 
    * @param stringBuilder
    * @param count
    * @param joinEntity
    * @param queryTable
    */
   private void countConditionalAppender (StringBuilder stringBuilder, int count, JoinEntity joinEntity,
            QueryTable queryTable) {
      // if count == 0, means this is first time so we will put jointype and on keywords, else we will keep on appending
      // "," before adding conditions in the on clause.
      ISQLAdaptor sqlAdaptor = SQLAdaptorFactory.getInstance().getSQLAdaptor(providerTypeValue);
      if (count == 0) {
         stringBuilder.append(SQL_SPACE_DELIMITER).append(joinEntity.getType().getValue()).append(SQL_SPACE_DELIMITER);
         stringBuilder.append(sqlAdaptor.createTableRepresentationQueryTableColumn(queryTable, true));
         stringBuilder.append(SQL_SPACE_DELIMITER).append(JOIN_CONDITION_SEPERATOR).append(SQL_SPACE_DELIMITER);
      } else {
         stringBuilder.append(SQL_SPACE_DELIMITER).append(WHERE_CLAUSE_SEPERATOR).append(SQL_SPACE_DELIMITER);
      }
      stringBuilder.append(sqlAdaptor.createColumRepresentationQueryTableColumn(joinEntity.getLhsOperand())).append(
               JOIN_CONDITION_OPERATOR).append(
               sqlAdaptor.createColumRepresentationQueryTableColumn(joinEntity.getRhsOperand()));
   }

   /**
    * This method will find all the tables in the list of joinEntities which has relations to one another
    * 
    * @param initialTables
    * @param joinEntities
    * @return joinedTables
    */
   private List<QueryTable> findJoinedTables (List<JoinEntity> joinEntities) {
      // take the first joinEntity and add the tables of that joinEntity to intialTablesList
      // from the intialTablesList
      JoinEntity firstJoinEntity = joinEntities.get(0);
      List<QueryTable> initialTables = new ArrayList<QueryTable>();
      initialTables.add(firstJoinEntity.getLhsOperand().getTable());
      initialTables.add(firstJoinEntity.getRhsOperand().getTable());

      // List which will contain all the joinedTables. Added initial set of tables to this list
      List<QueryTable> joinedTables = new ArrayList<QueryTable>();
      joinedTables.addAll(initialTables);
      // List which contains the tables which will be added during every iteration of search. intially, it contains the
      // same intial tables. This will be used for iteration purpose as we cannot modify the initialTables list because
      // of concurrentModification Exception.
      List<QueryTable> tempTables = new ArrayList<QueryTable>();
      tempTables.addAll(initialTables);
      while (tempTables.size() > 0) {
         // clear the intialTables everytime, because we need to iterate over only newly added tables.
         initialTables.clear();
         // add newly added tables to initialTables.
         initialTables.addAll(tempTables);
         // Reinitilaze the tempTables to empty so that, we can add the tempTables found during this iteration
         tempTables.clear();
         for (QueryTable queryTable : initialTables) {
            // try to find list of joinentites for table
            List<JoinEntity> foundJoinEntities = findJoinEntity(queryTable, joinEntities);
            if (foundJoinEntities.size() > 0) {
               // remove them from list.
               joinEntities.removeAll(foundJoinEntities);
               for (JoinEntity foundJoinEntity : foundJoinEntities) {
                  QueryTable newTable = null;
                  if (foundJoinEntity.getLhsOperand().getTable().equals(queryTable)) {
                     newTable = foundJoinEntity.getRhsOperand().getTable();
                  } else {
                     newTable = foundJoinEntity.getLhsOperand().getTable();
                  }
                  // if table already exists in the joinedTablesList,we wont add this table then
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
    * This method will find the JoinEntities for the tableName passed to it.
    * 
    * @param queryTable
    * @param joinEntities
    * @return foundJoinEntities
    */
   private List<JoinEntity> findJoinEntity (QueryTable queryTable, List<JoinEntity> joinEntities) {
      List<JoinEntity> foundJoinEntities = new ArrayList<JoinEntity>();
      // iterate and find if any joinentity has tableName on left or right side
      for (JoinEntity joinEntity : joinEntities) {
         if (joinEntity.getLhsOperand().getTable().equals(queryTable)
                  || joinEntity.getRhsOperand().getTable().equals(queryTable)) {
            foundJoinEntities.add(joinEntity);
         }
      }
      return foundJoinEntities;
   }

   /**
    * This method will find the JoinEntities between two tables passes to it.
    * 
    * @param queryTable1
    * @param queryTable2
    * @param joinEntities
    * @return foundJoinEntities
    */
   private List<JoinEntity> findJoinEntities (QueryTable queryTable1, QueryTable queryTable2,
            List<JoinEntity> joinEntities) {
      List<JoinEntity> foundJoinEntities = new ArrayList<JoinEntity>();
      // find all joinEntities which has the tables passes to it either on left side or right side
      for (JoinEntity joinEntity : joinEntities) {
         if (joinEntity.getLhsOperand().getTable().equals(queryTable1)
                  && joinEntity.getRhsOperand().getTable().equals(queryTable2)
                  || joinEntity.getLhsOperand().getTable().equals(queryTable2)
                  && joinEntity.getRhsOperand().getTable().equals(queryTable1)) {
            foundJoinEntities.add(joinEntity);
         }
      }
      return foundJoinEntities;
   }

   /**
    * This method populates the FromEntity for query object passed to it. It checks each segment of the query and get
    * the table information from them and keep on adding to the FromEntity of query object
    * 
    * @param query
    */
   private void populateFromEntities (Query query) {
      if (query.getCombiningType() == null) {
         populateSimpleQueryFromEntities(query);
      } else {
         List<Query> combiningQueries = query.getCombiningQueries();
         for (Query combinigQuery : combiningQueries) {
            populateSimpleQueryFromEntities(combinigQuery);
         }
      }
   }

   /**
    * @param query
    */
   private void populateSimpleQueryFromEntities (Query query) {
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();

      // get the list of fromEntities from each EntitySet and then add them to the fromEntities List and finally add
      // them to query object.

      // if from entities has subqueries inside it, means already populated, then dont populate from entities from other
      // sections, but populate the inner queries inside the from section
      if (ExecueCoreUtil.isListElementsNotEmpty(query.getFromEntities())) {
         createFromExistingFromEntities(query.getFromEntities());
      } else {
         if (ExecueCoreUtil.isListElementsNotEmpty(query.getSelectEntities())) {
            fromEntities.addAll(createFromSelectEntities(query.getSelectEntities()));
         }
         if (ExecueCoreUtil.isListElementsNotEmpty(query.getWhereEntities())) {
            fromEntities.addAll(createFromConditionalEntities(query.getWhereEntities()));
         }
         if (ExecueCoreUtil.isListElementsNotEmpty(query.getOrderingEntities())) {
            fromEntities.addAll(createFromOrderEntities(query.getOrderingEntities()));
         }
         if (ExecueCoreUtil.isListElementsNotEmpty(query.getGroupingEntities())) {
            fromEntities.addAll(createFromSelectEntities(query.getGroupingEntities()));
         }
         if (ExecueCoreUtil.isListElementsNotEmpty(query.getHavingEntities())) {
            fromEntities.addAll(createFromConditionalEntities(query.getHavingEntities()));
         }
         if (query.getScalingFactorEntity() != null) {
            fromEntities.add(createFromScalingFactorEntity(query.getScalingFactorEntity()));
         }
         query.setFromEntities(fromEntities);
         removeDuplicates(query);
      }
      // TODO: -RG- Cleanup this, as joins might not be in place by this time
      // fromEntities.addAll(createFromJoinEntities(query.getJoinEntities()));
   }

   private FromEntity createFromScalingFactorEntity (QueryTableColumn scalingFactorEntity) {
      return ExecueBeanManagementUtil.createFromEntityObject(scalingFactorEntity);
   }

   private void createFromExistingFromEntities (List<FromEntity> existingFromEntities) {
      if (ExecueCoreUtil.isListElementsNotEmpty(existingFromEntities)) {
         for (FromEntity fromEntity : existingFromEntities) {
            // if fromEntity is of type SUB_QUERY, then store the subquery in FromEntity object.
            if (FromEntityType.SUB_QUERY.equals(fromEntity.getType())) {
               populateFromEntities(fromEntity.getSubQuery());
            }
         }
      }
   }

   /**
    * This method is helper method to populateFromEntities() for getting Tables which belongs to SelectEntity section.
    * This method populates FromEntities list using the List<SelectEntity> selectEntities.
    * 
    * @param selectEntities
    * @return fromEntities
    */
   private List<FromEntity> createFromSelectEntities (List<SelectEntity> selectEntities) {
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      if (ExecueCoreUtil.isListElementsNotEmpty(selectEntities)) {
         for (SelectEntity selectEntity : selectEntities) {
            // if selectEntity is of type TABLE_COLUMN, then fetch the table info and store it into FromEntity
            // object.if some dummy column or * is there, we will fetch the corresponding table info from that.
            if (SelectEntityType.TABLE_COLUMN.equals(selectEntity.getType())) {
               fromEntities.add(ExecueBeanManagementUtil.createFromEntityObject(selectEntity.getTableColumn()));
            }
            // if selectEntity is of type SUB_QUERY, then store the subquery in FromEntity object.
            else if (selectEntity.getType().equals(SelectEntityType.SUB_QUERY)) {
               populateFromEntities(selectEntity.getSubQuery());
            }

         }
      }
      return fromEntities;
   }

   /**
    * This method is helper method to populateFromEntities() for getting Tables which belongs to ConditionalEntity
    * section. This method populates the FromEntity List using the List<ConditionalEntry> conditionalEntities
    * 
    * @param conditionEntities
    * @return fromEntities
    */
   private List<FromEntity> createFromConditionalEntities (List<ConditionEntity> conditionEntities) {
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      if (ExecueCoreUtil.isListElementsNotEmpty(conditionEntities)) {
         for (ConditionEntity conditionEntity : conditionEntities) {
            // get the LHS Table information and store it in FromEntity object.
            if (conditionEntity.isSubCondition()) {
               fromEntities.addAll(createFromConditionalEntities(conditionEntity.getSubConditionEntities()));
            } else {
               fromEntities.add(ExecueBeanManagementUtil.createFromEntityObject(conditionEntity.getLhsTableColumn()));
               // if RHS is of Type Table_Column then extract the Table information and store them into FromEntity
               // object
               if (QueryConditionOperandType.TABLE_COLUMN.equals(conditionEntity.getOperandType())) {
                  for (QueryTableColumn rhsTablecolum : conditionEntity.getRhsTableColumns()) {
                     fromEntities.add(ExecueBeanManagementUtil.createFromEntityObject(rhsTablecolum));
                  }
               }
               // if RHS is of type SUB_QUERY then extract the SUB_QUERY info and store them into FromEntity object.
               else if (QueryConditionOperandType.SUB_QUERY.equals(conditionEntity.getOperandType())) {
                  populateFromEntities(conditionEntity.getRhsSubQuery());
               }
            }
         }
      }
      return fromEntities;
   }

   /**
    * This method is helper method to populateFromEntities() for getting Tables which belongs to OrderEntity section.
    * This method populates the FromEntity list using the List<OrderEntity> orderEntities.
    * 
    * @param orderEntities
    * @return fromEntities
    */
   private List<FromEntity> createFromOrderEntities (List<OrderEntity> orderEntities) {
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();

      if (ExecueCoreUtil.isListElementsNotEmpty(orderEntities)) {
         List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
         // internally orderEntity has selectEntity inside it. So we create a list of selectentities from
         // orderentities and then use createFromSelectEntites() method to extract FromEntities information.
         for (OrderEntity orderEntity : orderEntities) {
            selectEntities.add(orderEntity.getSelectEntity());
         }

         fromEntities = createFromSelectEntities(selectEntities);
      }
      return fromEntities;
   }

   /**
    * Based on the tables in FromEntity, deduce the joins between the tables using JoinsService.<br/> Add the table
    * that are in joins as a result of non direct joins between table to FromEntity.<br/> Optimize the joins by
    * removing any duplicates came in the joins.<br/> Optimize the joins by analyzing the existing where condition
    * entities.<br/>
    * 
    * @param targetAsset
    * @param query
    * @throws QueryGenerationException
    */
   private void applyJoins (Asset targetAsset, Query query) throws QueryGenerationException {
      // TODO : VG - have to call swi services to get joins and then apply on query.
      List<FromEntity> fromEntities = query.getFromEntities();
      List<QueryTable> primaryTables = getTables(fromEntities);
      List<QueryTable> joiningTables = new ArrayList<QueryTable>();

      List<JoinEntity> joinEntities = getJoinEntities(targetAsset, primaryTables, joiningTables);

      populateQueryColumnFullyOnJoinEntities(joinEntities, targetAsset);

      // TODO: -RG- We are supposed to perform an add all to the existing entries??
      query.setJoinEntities(joinEntities);

      // TODO: -RG- Handle in line queries from From Entities segment
   }

   private void populateQueryColumnFullyOnJoinEntities (List<JoinEntity> joinEntities, Asset targetAsset)
            throws QueryGenerationException {
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(joinEntities)) {
            for (JoinEntity joinEntity : joinEntities) {
               Colum lhsOperandColumn = getSdxRetrievalService().getAssetTableColum(targetAsset.getId(),
                        joinEntity.getLhsOperand().getTable().getTableName(),
                        joinEntity.getLhsOperand().getColumn().getColumnName());
               QueryColumn lhsOperandQueryColumn = ExecueBeanManagementUtil.prepareQueryColumn(lhsOperandColumn);
               joinEntity.getLhsOperand().setColumn(lhsOperandQueryColumn);

               Colum rhsOperandColumn = getSdxRetrievalService().getAssetTableColum(targetAsset.getId(),
                        joinEntity.getRhsOperand().getTable().getTableName(),
                        joinEntity.getRhsOperand().getColumn().getColumnName());
               QueryColumn rhsOperandQueryColumn = ExecueBeanManagementUtil.prepareQueryColumn(rhsOperandColumn);
               joinEntity.getRhsOperand().setColumn(rhsOperandQueryColumn);
            }
         }
      } catch (SDXException sdxException) {
         throw new QueryGenerationException(sdxException.getCode(), sdxException);
      }
   }

   private List<QueryTable> getTables (List<FromEntity> fromEntities) {
      List<QueryTable> tables = new ArrayList<QueryTable>();
      for (FromEntity fromEntity : fromEntities) {
         if (FromEntityType.TABLE.equals(fromEntity.getType())) {
            tables.add(fromEntity.getTable());
         }
      }
      return tables;
   }

   private List<JoinEntity> getJoinEntities (Asset targetAsset, List<QueryTable> primaryTables,
            List<QueryTable> joiningTables) throws QueryGenerationException {
      List<JoinEntity> finalJoinEntities = new ArrayList<JoinEntity>();

      List<QueryTable> previousTables = new ArrayList<QueryTable>();
      for (QueryTable primaryTable : primaryTables) {
         for (QueryTable previousTable : previousTables) {
            List<JoinEntity> currJoinEntities = getJoinEntities(targetAsset, primaryTable, previousTable,
                     primaryTables, joiningTables);

            if (ExecueCoreUtil.isListElementsNotEmpty(currJoinEntities)) {
               // filter the join entities which are same.
               List<JoinEntity> existingJoinEntities = new ArrayList<JoinEntity>();
               for (int i = 0; i < currJoinEntities.size() - 1; i++) {
                  for (int j = i + 1; j < currJoinEntities.size(); j++) {
                     if (isJoinEntitiesEqual(currJoinEntities.get(i), currJoinEntities.get(j))) {
                        existingJoinEntities.add(currJoinEntities.get(j));
                     }
                  }
               }
               currJoinEntities.removeAll(existingJoinEntities);
               // take off join entities which already exists in the final join entities list
               existingJoinEntities = new ArrayList<JoinEntity>();
               for (JoinEntity currJoinEntity : currJoinEntities) {
                  for (JoinEntity finalJoinEntity : finalJoinEntities) {
                     if (isJoinEntitiesEqual(currJoinEntity, finalJoinEntity)) {
                        existingJoinEntities.add(currJoinEntity);
                        break;
                     }
                  }
               }
               // remove the existing definitions
               currJoinEntities.removeAll(existingJoinEntities);
               // add remaining ones to final list
               finalJoinEntities.addAll(currJoinEntities);
            }
         }
         previousTables.add(primaryTable);
      }

      return finalJoinEntities;
   }

   /**
    * This method will check two join entities whether they are same or not.
    * 
    * @param currJoinEntity
    * @param finalJoinEntity
    * @return boolean value
    */
   private boolean isJoinEntitiesEqual (JoinEntity currJoinEntity, JoinEntity finalJoinEntity) {
      String currEntityLhsTableName = currJoinEntity.getLhsOperand().getTable().getTableName();
      String currEntityRhsTableName = currJoinEntity.getRhsOperand().getTable().getTableName();
      String currEntityLhsAlias = currJoinEntity.getLhsOperand().getTable().getAlias();
      String currEntityRhsAlias = currJoinEntity.getRhsOperand().getTable().getAlias();
      String currEntityLhsColum = currJoinEntity.getLhsOperand().getColumn().getColumnName();
      String currEntityRhsColum = currJoinEntity.getRhsOperand().getColumn().getColumnName();

      String finalEntityLhsTableName = finalJoinEntity.getLhsOperand().getTable().getTableName();
      String finalEntityRhsTableName = finalJoinEntity.getRhsOperand().getTable().getTableName();
      String finalEntityLhsAlias = finalJoinEntity.getLhsOperand().getTable().getAlias();
      String finalEntityRhsAlias = finalJoinEntity.getRhsOperand().getTable().getAlias();
      String finalEntityLhsColum = finalJoinEntity.getLhsOperand().getColumn().getColumnName();
      String finalEntityRhsColum = finalJoinEntity.getRhsOperand().getColumn().getColumnName();

      boolean isLeftLeftEqual = currEntityLhsTableName.equalsIgnoreCase(finalEntityLhsTableName)
               && currEntityLhsAlias.equalsIgnoreCase(finalEntityLhsAlias)
               && currEntityLhsColum.equalsIgnoreCase(finalEntityLhsColum);

      boolean isLeftRightEqual = currEntityLhsTableName.equalsIgnoreCase(finalEntityRhsTableName)
               && currEntityLhsAlias.equalsIgnoreCase(finalEntityRhsAlias)
               && currEntityLhsColum.equalsIgnoreCase(finalEntityRhsColum);

      boolean isRightLeftEqual = currEntityRhsTableName.equalsIgnoreCase(finalEntityLhsTableName)
               && currEntityRhsAlias.equalsIgnoreCase(finalEntityLhsAlias)
               && currEntityRhsColum.equalsIgnoreCase(finalEntityLhsColum);

      boolean isRightRightEqual = currEntityRhsTableName.equalsIgnoreCase(finalEntityRhsTableName)
               && currEntityRhsAlias.equalsIgnoreCase(finalEntityRhsAlias)
               && currEntityRhsColum.equalsIgnoreCase(finalEntityRhsColum);
      return (isLeftLeftEqual || isLeftRightEqual) && (isRightLeftEqual || isRightRightEqual);

   }

   private List<JoinEntity> getJoinEntities (Asset targetAsset, QueryTable sourceTable, QueryTable destinationTable,
            List<QueryTable> primaryTables, List<QueryTable> joiningTables) throws QueryGenerationException {
      List<JoinEntity> joinEntities = new ArrayList<JoinEntity>();
      List<JoinDefinition> joinDefinitions = null;
      try {
         joinDefinitions = getJoinService().getAllExistingJoinDefinitions(targetAsset.getId(),
                  sourceTable.getTableName(), destinationTable.getTableName());
      } catch (JoinException joinException) {
         throw new QueryGenerationException(joinException.getCode(), joinException);
      }
      if (joinDefinitions == null) {
         return joinEntities;
      }
      JoinEntity tempJoinEntity = null;
      QueryTable lhsTable = null;
      QueryTable rhsTable = null;
      for (JoinDefinition joinDefinition : joinDefinitions) {
         tempJoinEntity = new JoinEntity();
         lhsTable = populateJoinEntityQueryTable(joinDefinition.getSourceTableName(), targetAsset, sourceTable,
                  destinationTable, primaryTables, joiningTables);
         rhsTable = populateJoinEntityQueryTable(joinDefinition.getDestTableName(), targetAsset, sourceTable,
                  destinationTable, primaryTables, joiningTables);
         tempJoinEntity.setLhsOperand(buildJoinOperand(joinDefinition.getSourceColumnName(), lhsTable));
         tempJoinEntity.setRhsOperand(buildJoinOperand(joinDefinition.getDestColumnName(), rhsTable));
         tempJoinEntity.setType(joinDefinition.getType());
         joinEntities.add(tempJoinEntity);
      }
      return joinEntities;
   }

   private QueryTable populateJoinEntityQueryTable (String tableName, Asset targetAsset, QueryTable sourceTable,
            QueryTable destinationTable, List<QueryTable> primaryTables, List<QueryTable> joiningTables)
            throws QueryGenerationException {
      QueryTable joinEntityQueryTable = null;
      if (tableName.equalsIgnoreCase(sourceTable.getTableName())
               || tableName.equalsIgnoreCase(destinationTable.getTableName())) {
         if (tableName.equalsIgnoreCase(sourceTable.getTableName())) {
            joinEntityQueryTable = sourceTable;
         } else {
            joinEntityQueryTable = destinationTable;
         }

      } else {
         joinEntityQueryTable = buildJoiningTable(primaryTables, joiningTables, targetAsset, tableName);
         joiningTables.add(joinEntityQueryTable);
      }
      return joinEntityQueryTable;
   }

   private QueryTable getQueryTableByName (List<QueryTable> queryTables, String tableName) {
      QueryTable existingQueryTable = null;
      // TODO: -RG- This is not the way to perform contains operation -- Refactor this block
      for (QueryTable queryTable : queryTables) {
         if (queryTable.getTableName().equals(tableName)) {
            existingQueryTable = queryTable;
            break;
         }
      }
      return existingQueryTable;
   }

   private QueryTable buildJoiningTable (List<QueryTable> primaryTables, List<QueryTable> joiningTables,
            Asset targetAsset, String tableName) throws QueryGenerationException {
      QueryTable joiningTable = getQueryTableByName(primaryTables, tableName);
      if (joiningTable == null) {
         joiningTable = getQueryTableByName(joiningTables, tableName);
      }
      if (joiningTable != null) {
         return joiningTable;
      }

      joiningTable = new QueryTable();
      joiningTable.setTableName(tableName);

      Tabl joiningTableRef = null;

      try {
         joiningTableRef = getSdxRetrievalService().getAssetTable(targetAsset.getId(), tableName);
      } catch (SWIException swiException) {
         throw new QueryGenerationException(swiException.getCode(), swiException);
      }

      if (joiningTableRef == null) {
         throw new QueryGenerationException(QueryGenerationExceptionCodes.JOINING_TABLE_NOT_FOUND, "Joining table "
                  + tableName + "not found in asset " + targetAsset.getId());
      }

      joiningTable.setTableType(joiningTableRef.getLookupType());
      joiningTable.setAlias(joiningTableRef.getAlias());
      joiningTable.setOwner(joiningTableRef.getOwner());
      return joiningTable;
   }

   private QueryTableColumn buildJoinOperand (String columnName, QueryTable queryTable) {
      QueryTableColumn queryTableColumn = new QueryTableColumn();
      QueryColumn queryColumn = new QueryColumn();
      queryColumn.setColumnName(columnName);
      queryTableColumn.setTable(queryTable);
      queryTableColumn.setColumn(queryColumn);
      return queryTableColumn;
   }

   /**
    * Duplicates may appear in any of the segments resulting from applying joins or applying defaults, remove the
    * duplicates if exists
    * 
    * @param query
    */
   private void removeDuplicates (Query query) {
      if (query.getCombiningType() == null) {
         removeDuplicatesForSimpleQuery(query);
      } else {
         List<Query> combiningQueries = query.getCombiningQueries();
         for (Query combinigQuery : combiningQueries) {
            removeDuplicatesForSimpleQuery(combinigQuery);
         }
      }
   }

   private void removeDuplicatesForSimpleQuery (Query query) {
      // TODO : VG -- check all the use cases before applying removeDuplicates.
      // TODO: -VG- Delete on basis of elements and not list by list
      // extract the entity from query and store that into set, this will remove the duplicates and then convert
      // that into list and store it back to query object.
      // if (ExecueCoreUtil.isListElementsNotEmpty(query.getSelectEntities())) {
      // query.setSelectEntities(new ArrayList<SelectEntity>(new HashSet<SelectEntity>(query.getSelectEntities())));
      // }
      // Removing the unwanted FromEntities. If tableName and Alias is same, then remove those entries
      if (ExecueCoreUtil.isListElementsNotEmpty(query.getFromEntities())) {
         // take care if FromEntity is a Table.
         List<FromEntity> fromEntities = new ArrayList<FromEntity>();
         for (FromEntity fromEntity : query.getFromEntities()) {
            fromEntities.add(fromEntity);
         }

         for (int i = 0; i < query.getFromEntities().size() - 1; i++) {
            FromEntity fromEntity = query.getFromEntities().get(i);
            if (FromEntityType.SUB_QUERY.equals(fromEntity.getType())) {
               continue;
            }
            for (int j = i + 1; j < query.getFromEntities().size(); j++) {
               FromEntity fromEntity2 = query.getFromEntities().get(j);
               if (fromEntity2.getType().equals(FromEntityType.SUB_QUERY)) {
                  continue;
               }

               if (fromEntity.getTable().getTableName().equalsIgnoreCase(fromEntity2.getTable().getTableName())
                        && fromEntity.getTable().getAlias().equalsIgnoreCase(fromEntity2.getTable().getAlias())) {
                  fromEntities.remove(fromEntity);
               }
            }
         }
         query.setFromEntities(fromEntities);
      }
      // if (ExecueCoreUtil.isListElementsNotEmpty(query.getWhereEntities())) {
      // query.setFromEntities(new ArrayList<FromEntity>(new HashSet<FromEntity>(query.getFromEntities())));
      // }
      // if (ExecueCoreUtil.isListElementsNotEmpty(query.getOrderingEntities())) {
      // query.setWhereEntities(new ArrayList<ConditionEntity>(new HashSet<ConditionEntity>(query.getWhereEntities())));
      // }
      // if (ExecueCoreUtil.isListElementsNotEmpty(query.getOrderingEntities())) {
      // query.setOrderingEntities(new ArrayList<OrderEntity>(new HashSet<OrderEntity>(query.getOrderingEntities())));
      // }
      // if (ExecueCoreUtil.isListElementsNotEmpty(query.getGroupingEntities())) {
      // query.setGroupingEntities(new ArrayList<SelectEntity>(new HashSet<SelectEntity>(query.getGroupingEntities())));
      // }
      // if (ExecueCoreUtil.isListElementsNotEmpty(query.getHavingEntities())) {
      // query
      // .setHavingEntities(new ArrayList<ConditionEntity>(new HashSet<ConditionEntity>(query
      // .getHavingEntities())));
      // }
      // if (ExecueCoreUtil.isListElementsNotEmpty(query.getJoinEntities())) {
      // query.setJoinEntities(new ArrayList<JoinEntity>(new HashSet<JoinEntity>(query.getJoinEntities())));
      // }      
   }

   /**
    * Create a CASE statement based on the range details provided.<br/> Range Details are sorted by order.<br/> Each
    * condition is greater than previous range detail's upper limit and less than or equal to current range detail's
    * upper limit.
    * 
    * @param List
    *           of RangeDetail - rangeDetails
    * @param QueryTable -
    *           table
    * @param QueryColumn -
    *           column
    * @return String - CASE statement prepared based on list of RangeDetail objects
    */
   private List<String> createCaseStatementOnRange (List<RangeDetail> rangeDetails, QueryTable queryTable,
            QueryColumn queryColumn) {
      QueryTableColumn queryTableColumn = new QueryTableColumn();
      queryTableColumn.setTable(queryTable);
      queryTableColumn.setColumn(queryColumn);
      ISQLAdaptor sqlAdaptor = SQLAdaptorFactory.getInstance().getSQLAdaptor(providerTypeValue);
      String columnRepresentation = sqlAdaptor.createColumRepresentationQueryTableColumn(queryTableColumn);
      List<String> caseStatements = new ArrayList<String>();
      caseStatements.add("CASE");

      RangeDetail previousRange = null;
      /**
       * Order the list by range detail's order value
       */
      Collections.sort(rangeDetails, new RangeDetailOrderComparator());
      for (RangeDetail currentRange : rangeDetails) {
         StringBuilder caseStmtBuilder = new StringBuilder();
         /*
          * Handle Lower Limit of the first range detail
          */
         if (currentRange.getLowerLimit() == null) {
            caseStmtBuilder.append("WHEN (");
            caseStmtBuilder.append(columnRepresentation);
            caseStmtBuilder.append(" <= ");
            caseStmtBuilder.append(currentRange.getUpperLimit());
            caseStmtBuilder.append(") THEN ");
            caseStmtBuilder.append(SINGLE_QUOTE);
            caseStmtBuilder.append(currentRange.getOrder());
            caseStmtBuilder.append(SINGLE_QUOTE);
         }
         /*
          * Handle Upper Limit of the last range detail
          */
         else if (currentRange.getUpperLimit() == null) {

            caseStmtBuilder.append("ELSE ");
            caseStmtBuilder.append(SINGLE_QUOTE);
            caseStmtBuilder.append(currentRange.getOrder());
            caseStmtBuilder.append(SINGLE_QUOTE);
         }
         /*
          * Handle the range details, when both upper and lower limit are not null
          */
         else if (currentRange.getLowerLimit() != null && currentRange.getUpperLimit() != null) {

            caseStmtBuilder.append("WHEN (");
            caseStmtBuilder.append(columnRepresentation);
            caseStmtBuilder.append(" > ");
            caseStmtBuilder.append(previousRange.getUpperLimit());
            caseStmtBuilder.append(" AND ");
            caseStmtBuilder.append(columnRepresentation);
            caseStmtBuilder.append(" <= ");
            caseStmtBuilder.append(currentRange.getUpperLimit());
            caseStmtBuilder.append(") THEN ");
            caseStmtBuilder.append(SINGLE_QUOTE);
            caseStmtBuilder.append(currentRange.getOrder());
            caseStmtBuilder.append(SINGLE_QUOTE);
         }
         previousRange = currentRange;
         caseStatements.add(caseStmtBuilder.toString());
      }
      caseStatements.add("END");

      return caseStatements;
   }// End of method createCaseStatementOnRange

   public static void main (String[] args) {
      QueryTableColumn queryTableColumn = ExecueBeanManagementUtil.prepareQueryTableColumn("column", DataType.STRING,
               "alias", "table", null, CheckType.NO, null, LookupType.None, false);
      String value = "500";
      QueryFormula queryFormula = new QueryFormula();
      queryFormula.setQueryFormulaType(QueryFormulaType.DYNAMIC);
      queryFormula.setArithmeticOperatorType(ArithmeticOperatorType.DIVISION);
      queryFormula.setFirstOperandType(QueryFormulaOperandType.TABLE_COLUMN);
      queryFormula.setFirstOperandQueryTableColumn(queryTableColumn);
      queryFormula.setSecondOperandType(QueryFormulaOperandType.VALUE);
      queryFormula.setSecondOperandValue(value);

      SelectEntity selectEntity = new SelectEntity();
      selectEntity.setFormula(queryFormula);
      selectEntity.setType(SelectEntityType.FORMULA);

      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      selectEntities.add(selectEntity);
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      FromEntity fromEntity = new FromEntity();
      fromEntity.setTable(queryTableColumn.getTable());
      fromEntity.setType(FromEntityType.TABLE);
      fromEntities.add(fromEntity);

      Query query = new Query();
      query.setSelectEntities(selectEntities);
      query.setFromEntities(fromEntities);
      Asset asset = new Asset();
      asset.setId(1L);
      QueryRepresentation extractQueryRepresentation = new MySqlQueryGenerationServiceImpl()
               .extractQueryRepresentation(asset, query);
      System.out.println(extractQueryRepresentation.getQueryString());
   }

   public Asset getAsset () {
      return asset;
   }

   public void setAsset (Asset asset) {
      this.asset = asset;
   }

   public IJoinService getJoinService () {
      return joinService;
   }

   public void setJoinService (IJoinService joinService) {
      this.joinService = joinService;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
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

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}
