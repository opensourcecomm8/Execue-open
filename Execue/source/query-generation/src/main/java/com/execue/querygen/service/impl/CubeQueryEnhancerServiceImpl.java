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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.FromEntity;
import com.execue.core.common.bean.querygen.JoinEntity;
import com.execue.core.common.bean.querygen.OrderEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.FromEntityType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.OrderEntityType;
import com.execue.core.common.type.QueryCombiningType;
import com.execue.core.common.type.QueryConditionOperandType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.common.util.ExecueRangeUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.querygen.service.ICubeQueryEnhancerService;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * This service enhances the cube query to fit it to the structure cube can understand.
 * 
 * @author Vishay
 * @version 1.0
 * @since 20/01/2011
 */
public class CubeQueryEnhancerServiceImpl implements ICubeQueryEnhancerService {

   private static final Logger       logger = Logger.getLogger(CubeQueryEnhancerServiceImpl.class);
   private ISDXRetrievalService      sdxRetrievalService;
   private ICoreConfigurationService coreConfigurationService;

   /**
    * This method is used to modify the query object if the inputAsset is cube and subType is superset and owned by
    * execue. This will modify the query to the structure cube can understand. This has prerequisite of having stats on
    * each of the measures in the select section.
    * 
    * @param query
    * @param targetAsset
    * @throws SDXException
    */
   public Query modifyExecueOwnedCubeQuery (Asset asset, Query query) {

      // TODO: NK/GA:: rollup is handled only at a plain query level but not at combined query level
      query = handleRollupQuery(query);

      if (query.getCombiningType() == null) {
         modifyExecueOwnedCubeQueryForSimpleQuery(asset, query);
      } else {
         List<Query> combiningQueries = query.getCombiningQueries();
         for (Query combiningQuery : combiningQueries) {
            modifyExecueOwnedCubeQueryForSimpleQuery(asset, combiningQuery);
         }
      }
      return query;
   }

   /**
    * @param query
    * @return
    */
   private Query handleRollupQuery (Query query) {
      if (query.isRollupQuery()) {

         List<SelectEntity> existingGroupEntities = query.getGroupingEntities();

         // List to maintain the combining inner queries
         List<Query> combiningQueries = new ArrayList<Query>();

         // initialize the size and counter variables
         int groupByClauseElementSize = existingGroupEntities.size();
         int notEqualsAllCounter = 1;

         // We need to prepare as many query object as the number of group by elements i.e number of levels of hierarchy
         for (int index = 1; index <= groupByClauseElementSize; index++) {
            // Get all the existing selects and where clause entries
            Query combiningQuery = null;
            try {
               combiningQuery = ExecueBeanCloneUtil.cloneQuery(query);
            } catch (CloneNotSupportedException e) {
               e.printStackTrace();
            }

            // handle the condition entities for rollup
            handleConditionEntitiesForRollup(combiningQuery, notEqualsAllCounter);

            // Set the rollup query flag to false for combining query
            combiningQuery.setRollupQuery(false);

            // Nullify the ordering entities, this is being set at last after all cube enhancing logic
            combiningQuery.setOrderingEntities(null);

            // Add the inner query structure to the list
            combiningQueries.add(combiningQuery);

            // Increment the not null counter
            notEqualsAllCounter++;
         }

         // Reset the query as combining query with all above list of combining queries and UNION as combination type
         query = new Query();
         query.setCombiningQueries(combiningQueries);
         query.setCombiningType(QueryCombiningType.UNION);
      }
      return query;
   }

   private Map<SelectEntity, ConditionEntity> getMatchedSelectEntities (List<SelectEntity> existingGroupEntities,
            List<ConditionEntity> existingWhereEntities) {
      Map<QueryTableColumn, ConditionEntity> condityEntityByQueryTableColumn = new HashMap<QueryTableColumn, ConditionEntity>();
      for (ConditionEntity conditionEntity : existingWhereEntities) {
         if (conditionEntity.getOperandType() == QueryConditionOperandType.VALUE) {
            condityEntityByQueryTableColumn.put(conditionEntity.getLhsTableColumn(), conditionEntity);
         }
      }

      Map<SelectEntity, ConditionEntity> matchedSelectEntities = new HashMap<SelectEntity, ConditionEntity>();
      for (SelectEntity selectEntity : existingGroupEntities) {
         if (condityEntityByQueryTableColumn.containsKey(selectEntity.getTableColumn())) {
            matchedSelectEntities.put(selectEntity, condityEntityByQueryTableColumn.get(selectEntity.getTableColumn()));
         }
      }
      return matchedSelectEntities;
   }

   /**
    * This method will check and add/update the condition entities based on rollup requirement for cube. If hierarchy
    * dimension is already part of the where condition, then we check if need to reset it to ALL based on its index and
    * the given input notEqualsAllCounter. If hierarchy dimension is not part of the where condition, then we just
    * create the new condition with the operator type as NOT EQUALS or EQUALS based on comparing its index with the
    * given notEqualsAllCounter
    * 
    * @param combiningQuery
    * @param notEqualsAllCounter
    */
   private void handleConditionEntitiesForRollup (Query combiningQuery, int notEqualsAllCounter) {

      List<SelectEntity> hierarchySelectEntities = combiningQuery.getGroupingEntities();
      List<ConditionEntity> existingWhereEntities = combiningQuery.getWhereEntities();

      // Get the select entities which are not part of where condition entities
      // Apart from these matched entities others will be added as condition entities
      Map<SelectEntity, ConditionEntity> matchedSelectEntities = getMatchedSelectEntities(hierarchySelectEntities,
               existingWhereEntities);

      List<ConditionEntity> whereConditionEntitiesForRollup = new ArrayList<ConditionEntity>();

      for (int index = 0; index < hierarchySelectEntities.size(); index++) {
         SelectEntity hierarchySelectEntity = hierarchySelectEntities.get(index);
         ConditionEntity matchedConditionEntity = matchedSelectEntities.get(hierarchySelectEntity);
         if (matchedConditionEntity != null) {
            // This condition is already part of existing where conditions and we do not need to over ride the condition
            // value
            // as index is less than the not null counter, hence continue
            continue;
         }

         // If existing condition not found, then create a new condition for this hierarchy dimension
         ConditionEntity conditionEntity = new ConditionEntity();
         conditionEntity.setOperandType(QueryConditionOperandType.VALUE);
         conditionEntity.setLhsTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(hierarchySelectEntity
                  .getTableColumn()));
         if (index < notEqualsAllCounter) {
            conditionEntity.setOperator(OperatorType.NOT_EQUALS);
         } else {
            conditionEntity.setOperator(OperatorType.EQUALS);
         }

         QueryValue queryValue = new QueryValue();
         queryValue.setValue(getCoreConfigurationService().getCubeAllValue());
         queryValue.setDataType(DataType.STRING);
         List<QueryValue> queryValues = new ArrayList<QueryValue>();
         queryValues.add(queryValue);

         conditionEntity.setRhsValues(queryValues);
         whereConditionEntitiesForRollup.add(conditionEntity);
      }

      // Add the where condition entities for rollup
      if (ExecueCoreUtil.isCollectionNotEmpty(whereConditionEntitiesForRollup)) {
         combiningQuery.getWhereEntities().addAll(whereConditionEntitiesForRollup);
      }
   }

   private void modifyExecueOwnedCubeQueryForSimpleQuery (Asset asset, Query query) {
      try {

         List<Tabl> cubeTables = getSdxRetrievalService().getAllTables(asset);
         List<QueryTableColumn> dimensionTableColumns = new ArrayList<QueryTableColumn>();
         for (Tabl tabl : cubeTables) {
            if (!LookupType.None.equals(tabl.getLookupType())) {
               QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tabl);
               List<Colum> columns = getSdxRetrievalService().getColumnsOfTable(tabl);
               Colum lookupValueColumn = ExecueBeanUtil.findCorrespondingColumn(columns, tabl.getLookupValueColumn());
               QueryColumn queryColumn = ExecueBeanManagementUtil.prepareQueryColumn(lookupValueColumn);
               dimensionTableColumns.add(ExecueBeanManagementUtil.prepareQueryTableColumn(queryColumn, queryTable));
            }
         }
         applyRangeBands(query, asset);
         handleComparitiveOperators(query, dimensionTableColumns, asset);
         modifyQueryStructureForCube(query, dimensionTableColumns, asset);
      } catch (SDXException e) {
         e.printStackTrace();
      } catch (CloneNotSupportedException e) {
         e.printStackTrace();
      }
   }

   /**
    * This method will check if any condition has been asked on range lookup, then convert the value asked by the user
    * to range band available in lookup table so that fact table can return the records.
    * 
    * @param query
    * @param asset
    */
   private void applyRangeBands (Query query, Asset asset) {
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(query.getWhereEntities())) {
            for (ConditionEntity conditionEntity : query.getWhereEntities()) {
               if (QueryConditionOperandType.VALUE.equals(conditionEntity.getOperandType())) {
                  QueryTableColumn lhsTableColumn = conditionEntity.getLhsTableColumn();
                  if (LookupType.RANGE_LOOKUP.equals(lhsTableColumn.getTable().getTableType())) {
                     OperatorType operator = conditionEntity.getOperator();
                     List<QueryValue> rhsQueryValues = conditionEntity.getRhsValues();
                     AssetEntityDefinition assetEntityDefinition = getSdxRetrievalService()
                              .getAssetEntityDefinitionByNames(asset.getApplication().getName(), asset.getName(),
                                       lhsTableColumn.getTable().getTableName(),
                                       lhsTableColumn.getColumn().getColumnName(), null);
                     // get the members for the column
                     List<Membr> columnMembers = getSdxRetrievalService().getColumnMembers(
                              assetEntityDefinition.getColum());
                     // convert the members to range details and populate
                     // the order
                     List<RangeDetail> ranges = populateRangeDetails(columnMembers);
                     // get the unique range bands
                     Set<String> uniqueRangeBands = findMatchingBands(rhsQueryValues, operator, ranges);

                     if (logger.isDebugEnabled()) {
                        logger.debug("Got Bands size " + uniqueRangeBands.size());
                     }

                     // populate the range bands instead of values in
                     // query
                     List<QueryValue> rangeQueryBands = new ArrayList<QueryValue>();
                     for (String rangeBand : uniqueRangeBands) {
                        QueryValue queryValue = new QueryValue();
                        queryValue.setDataType(DataType.STRING);
                        queryValue.setValue(rangeBand);
                        rangeQueryBands.add(queryValue);
                     }
                     if (uniqueRangeBands.size() == 1) {
                        conditionEntity.setOperator(OperatorType.EQUALS);
                     } else {
                        conditionEntity.setOperator(OperatorType.IN);
                     }
                     conditionEntity.setRhsValues(rangeQueryBands);
                  }
               }
            }
         }
      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   /**
    * This method will return the list of unique bands for the list of query values depends on operator asked
    * 
    * @param rhsQueryValues
    * @param operator
    * @param ranges
    * @return matchedRangeBands
    */
   private Set<String> findMatchingBands (List<QueryValue> rhsQueryValues, OperatorType operator,
            List<RangeDetail> ranges) {
      Set<String> matchedRangeBands = new HashSet<String>();
      List<Double> values = new ArrayList<Double>();
      for (QueryValue queryValue : rhsQueryValues) {
         values.add(Double.parseDouble(queryValue.getValue()));
      }
      List<RangeDetail> matchesRangeDetails = ExecueRangeUtil.findMatchingRangeDetails(values, operator, ranges);
      if (ExecueCoreUtil.isCollectionNotEmpty(matchesRangeDetails)) {
         for (RangeDetail rangeDetail : matchesRangeDetails) {
            matchedRangeBands.add(rangeDetail.getValue());
         }
      }
      return matchedRangeBands;
   }

   /**
    * This method will populate the range details from the list of members
    * 
    * @param columnMembers
    * @return rangeDetails
    */
   private List<RangeDetail> populateRangeDetails (List<Membr> columnMembers) {
      List<RangeDetail> rangeDetails = new ArrayList<RangeDetail>();
      List<BigDecimal> lowerLimits = new ArrayList<BigDecimal>();
      // get the lower limits in list other than null
      for (Membr membr : columnMembers) {
         if (membr.getLowerLimit() != null) {
            lowerLimits.add(membr.getLowerLimit());
         }
      }
      // sort them
      Collections.sort(lowerLimits);
      // for each member, if both values are null then continue otherwise
      // create a range detail and set the order where
      // we find the lower limit in the sorted list. if lower limit is null,
      // that is order 0.
      for (Membr membr : columnMembers) {
         if (membr.getLowerLimit() == null && membr.getUpperLimit() == null) {
            continue;
         }
         RangeDetail rangeDetail = new RangeDetail();
         if (membr.getLowerLimit() == null) {
            rangeDetail.setOrder(0);
         } else {
            int index = 1;
            for (BigDecimal bigDecimal : lowerLimits) {
               if (membr.getLowerLimit().equals(bigDecimal)) {
                  rangeDetail.setOrder(index);
                  break;
               }
               index++;
            }
         }
         rangeDetail.setLowerLimit(membr.getLowerLimit() == null ? null : membr.getLowerLimit().doubleValue());
         rangeDetail.setUpperLimit(membr.getUpperLimit() == null ? null : membr.getUpperLimit().doubleValue());
         rangeDetail.setDescription(membr.getLookupDescription());
         rangeDetail.setValue(membr.getLookupValue());
         rangeDetails.add(rangeDetail);
      }
      return rangeDetails;
   }

   private void handleComparitiveOperators (Query query, List<QueryTableColumn> dimensionTableColumns, Asset asset) {
      if (ExecueCoreUtil.isCollectionNotEmpty(query.getWhereEntities())) {
         if (ExecueCoreUtil.isCollectionNotEmpty(dimensionTableColumns)) {
            List<ConditionEntity> comparitiveEntities = new ArrayList<ConditionEntity>();
            for (ConditionEntity conditionEntity : query.getWhereEntities()) {
               if (QueryConditionOperandType.VALUE.equals(conditionEntity.getOperandType())
                        && LookupType.SIMPLE_LOOKUP.equals(conditionEntity.getLhsTableColumn().getTable()
                                 .getTableType())
                        && dimensionTableColumns.contains(conditionEntity.getLhsTableColumn())) {

                  boolean allQueryValueFound = false;
                  for (QueryValue queryValue : conditionEntity.getRhsValues()) {
                     if (getCoreConfigurationService().getCubeAllValue().equalsIgnoreCase(queryValue.getValue())) {
                        allQueryValueFound = true;
                        break;
                     }
                  }

                  // Skip if all value is already present in the condition
                  if (allQueryValueFound) {
                     continue;
                  }

                  switch (conditionEntity.getOperator()) {
                     case NOT_EQUALS:
                        if (logger.isDebugEnabled()) {
                           logger.debug("Condition has been asked on simple lookup with not equal to operator");
                        }
                        conditionEntity.setOperator(OperatorType.NOT_IN);
                        QueryValue queryValue = new QueryValue();
                        queryValue.setValue(getCoreConfigurationService().getCubeAllValue());
                        queryValue.setDataType(DataType.STRING);

                        conditionEntity.getRhsValues().add(queryValue);
                        break;
                     case GREATER_THAN:
                        if (logger.isDebugEnabled()) {
                           logger.debug("Condition has been asked on simple lookup with Greaterthan operator");
                        }
                        comparitiveEntities.add(prepareCubeAllConditionEntity(conditionEntity, asset));
                        break;
                     case LESS_THAN:
                        if (logger.isDebugEnabled()) {
                           logger.debug("Condition has been asked on simple lookup with LessThan operator");
                        }
                        comparitiveEntities.add(prepareCubeAllConditionEntity(conditionEntity, asset));
                        break;
                     case GREATER_THAN_EQUALS:
                        if (logger.isDebugEnabled()) {
                           logger.debug("Condition has been asked on simple lookup with GreaterThanEqualTo operator");
                        }
                        comparitiveEntities.add(prepareCubeAllConditionEntity(conditionEntity, asset));
                        break;
                     case LESS_THAN_EQUALS:
                        if (logger.isDebugEnabled()) {
                           logger.debug("Condition has been asked on simple lookup with LessThanEqualTo operator");
                        }
                        comparitiveEntities.add(prepareCubeAllConditionEntity(conditionEntity, asset));
                        break;
                  }
               }
            }
            query.getWhereEntities().addAll(comparitiveEntities);
         }
      }
   }

   private ConditionEntity prepareCubeAllConditionEntity (ConditionEntity existingConditionEntity, Asset asset) {
      ConditionEntity conditionEntity = new ConditionEntity();
      conditionEntity.setLhsTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(existingConditionEntity
               .getLhsTableColumn()));
      conditionEntity.setOperandType(QueryConditionOperandType.VALUE);
      QueryValue queryValue = new QueryValue();
      conditionEntity.setOperator(OperatorType.NOT_EQUALS);
      queryValue.setValue(getCoreConfigurationService().getCubeAllValue());
      queryValue.setDataType(DataType.STRING);
      List<QueryValue> rhsValues = new ArrayList<QueryValue>();
      rhsValues.add(queryValue);
      conditionEntity.setRhsValues(rhsValues);
      return conditionEntity;
   }

   private void modifyQueryStructureForCube (Query query, List<QueryTableColumn> dimensionTableColumns, Asset asset)
            throws CloneNotSupportedException {
      // get the unique stats from the select clause of query
      Set<String> uniqueQueryStats = getUniqueSelectEntityStats(query.getSelectEntities());
      if (logger.isDebugEnabled()) {
         logger.debug("List of unique stats " + uniqueQueryStats.size());
      }
      // get the where clause colums from the query
      List<QueryTableColumn> queryWhereClauseDimensionTableColumns = getQueryWhereClauseDimensionColums(query,
               dimensionTableColumns);
      if (logger.isDebugEnabled()) {
         logger.debug("List of where clause dimension columns asked by user size "
                  + queryWhereClauseDimensionTableColumns.size());
      }
      // get the select clause colums from the query
      List<QueryTableColumn> querySelectClauseDimensionTableColumns = getQuerySelectClauseDimensionColums(query,
               dimensionTableColumns);
      if (logger.isDebugEnabled()) {
         logger.debug("List of select clause dimension columns asked by user "
                  + querySelectClauseDimensionTableColumns.size());
      }
      // populate the missing dimensions for the cube which are not in query's
      // where clause
      populateMissingDimensions(query, dimensionTableColumns, queryWhereClauseDimensionTableColumns,
               querySelectClauseDimensionTableColumns, asset);
      // check if stats in the select clause is less than 1.
      if (uniqueQueryStats.size() <= 1) {
         // override SL_STAT dimension to stat found
         if (uniqueQueryStats.size() == 1) {
            if (logger.isDebugEnabled()) {
               logger.debug("Found one stat. Overriding that stat");
            }
            overrideStatConditionalEntity(query, new ArrayList<String>(uniqueQueryStats).get(0));
         }
         // taking the function name off from select entity and append the
         // function name to alias.
         populateMeasureAlias(query);

         // make the group by, order by and having null for cube outer query
         // TODO - VG- has to check for how to handle group by, order by and
         // having
         query.setGroupingEntities(null);
         query.setHavingEntities(null);
         populateOrderByDimensions(query, dimensionTableColumns, asset);
      }
      // if there are more than one unique stats in the select section of
      // query
      else {
         if (logger.isDebugEnabled()) {
            logger.debug("Found more than one stats. Generating Inner Queries for same ");
            logger.debug("List of Unique Stats found ");
            for (String stat : uniqueQueryStats) {
               logger.debug(stat);
            }
         }
         // create map for stat,innerQuery for each unique stat in the select
         // clause, This query is same as the outer
         // query if there is 1 stat case above
         Map<String, Query> fromQueries = populateFromInnerQueries(query, uniqueQueryStats);
         // populating the outer query with the information from inner
         // queries.
         populateOuterQuery(query, fromQueries, querySelectClauseDimensionTableColumns);

         // inner queries, group by, order by and having null
         // TODO - VG- has to check for how to handle group by, order by and
         // having
         for (String stat : fromQueries.keySet()) {
            Query fromQuery = fromQueries.get(stat);
            fromQuery.setOrderingEntities(null);
            fromQuery.setHavingEntities(null);
            fromQuery.setGroupingEntities(null);
            populateOrderByDimensions(fromQuery, dimensionTableColumns, asset);
         }

         // make the group by, order by and having null for cube outer query
         // TODO - VG- has to check for how to handle group by, order by and
         // having
         query.setGroupingEntities(null);
         query.setHavingEntities(null);
         populateOrderByDimensions(query, dimensionTableColumns, asset, true);
      }
   }

   private List<QueryTableColumn> getQuerySelectClauseDimensionColums (Query query,
            List<QueryTableColumn> dimensionTableColumns) {
      if (ExecueCoreUtil.isCollectionEmpty(query.getSelectEntities())) {
         return new ArrayList<QueryTableColumn>();
      }
      List<QueryTableColumn> querySelectClauseTableColums = new ArrayList<QueryTableColumn>();
      for (SelectEntity selectEntity : query.getSelectEntities()) {
         if (dimensionTableColumns.contains(selectEntity.getTableColumn())) {
            querySelectClauseTableColums.add(selectEntity.getTableColumn());
         }
      }
      return querySelectClauseTableColums;
   }

   private void populateOrderByDimensions (Query query, List<QueryTableColumn> dimensionTableColumns, Asset asset) {
      populateOrderByDimensions(query, dimensionTableColumns, asset, false);
   }

   private void populateOrderByDimensions (Query query, List<QueryTableColumn> dimensionTableColumns, Asset asset,
            boolean skipTableAliasComparision) {
      List<SelectEntity> queryDimensionSelectEntities = getDimensionsAsSelectEntities(query, dimensionTableColumns,
               skipTableAliasComparision);
      List<OrderEntity> orderEntities = new ArrayList<OrderEntity>();
      for (SelectEntity selectEntity : queryDimensionSelectEntities) {
         OrderEntity orderEntity = new OrderEntity();
         orderEntity.setSelectEntity(selectEntity);
         orderEntity.setOrderType(OrderEntityType.ASCENDING);
         orderEntities.add(orderEntity);
      }
      query.setOrderingEntities(orderEntities);
   }

   private List<SelectEntity> getDimensionsAsSelectEntities (Query query, List<QueryTableColumn> dimensionTableColumns,
            boolean skipTableAliasComparision) {
      if (ExecueCoreUtil.isCollectionEmpty(query.getSelectEntities())) {
         return new ArrayList<SelectEntity>();
      }
      List<SelectEntity> querySelectClauseColums = new ArrayList<SelectEntity>();
      for (SelectEntity selectEntity : query.getSelectEntities()) {
         if (dimensionTableColumns.contains(selectEntity.getTableColumn()) || skipTableAliasComparision
                  && isTableColumnExists(selectEntity.getTableColumn(), dimensionTableColumns)) {
            querySelectClauseColums.add(selectEntity);
         }
      }
      return querySelectClauseColums;
   }

   private boolean isTableColumnExists (QueryTableColumn tableColumn, List<QueryTableColumn> dimensionTableColumns) {
      boolean tableColumnExists = false;
      for (QueryTableColumn queryTableColumn : dimensionTableColumns) {
         if (queryTableColumn.getTable().getTableName().equals(tableColumn.getTable().getTableName())
                  && queryTableColumn.getColumn().getColumnName().equals(tableColumn.getColumn().getColumnName())) {
            tableColumnExists = true;
            break;
         }
      }
      return tableColumnExists;
   }

   /**
    * This method will populate the outer query object using knowledge of inner queries. It sets all the inner queries
    * to from clause of outer query. It takes the query dimensions which were already there in the original query and
    * prepare where clause for each of those dimensions using fromQueries. In the select section, if there is function
    * applied, then it changes the colum name to colum name + "_" + stat because inner query has this structure. It
    * takes the functions off from select entities.It using the aliases of the inner queries to access the select
    * entitites.
    * 
    * @param outerQuery
    * @param fromQueries
    * @param queryDimensions
    */
   private void populateOuterQuery (Query outerQuery, Map<String, Query> fromQueries,
            List<QueryTableColumn> querySelectClauseDimensionTableColumns) {
      // populate the outer query fromEntities with the inner queries.
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      for (String stat : fromQueries.keySet()) {
         Query fromQuery = fromQueries.get(stat);
         FromEntity fromEntity = new FromEntity();
         fromEntity.setType(FromEntityType.SUB_QUERY);
         fromEntity.setSubQuery(fromQuery);
         fromEntities.add(fromEntity);
      }
      outerQuery.setFromEntities(fromEntities);

      // populating the outer query where entities with the inner queries.
      List<Query> fromQueriesList = new ArrayList<Query>();
      for (String stat : fromQueries.keySet()) {
         fromQueriesList.add(fromQueries.get(stat));
      }
      List<ConditionEntity> whereEntities = new ArrayList<ConditionEntity>();
      // for each dimension in the original query, we need to have a where
      // condition between all the fromQueries.
      // fromquery1.d1 = fromquery2.d1 and fromquery2.d1 = fromquery3.d1 and
      // so on
      for (QueryTableColumn queryDimension : querySelectClauseDimensionTableColumns) {
         for (int i = 0; i < fromQueriesList.size() - 1; i++) {
            // prepare condition entities for i and i +1
            ConditionEntity conditionEntity = new ConditionEntity();
            conditionEntity.setLhsTableColumn(createQueryTableColum(null, fromQueriesList.get(i).getAlias(),
                     queryDimension.getColumn().getColumnName()));
            conditionEntity.setOperator(OperatorType.EQUALS);
            conditionEntity.setOperandType(QueryConditionOperandType.TABLE_COLUMN);
            List<QueryTableColumn> rhsTablecolumns = new ArrayList<QueryTableColumn>();
            rhsTablecolumns.add(createQueryTableColum(null, fromQueriesList.get(i + 1).getAlias(), queryDimension
                     .getColumn().getColumnName()));
            conditionEntity.setRhsTableColumns(rhsTablecolumns);
            whereEntities.add(conditionEntity);
         }
      }
      outerQuery.setWhereEntities(whereEntities);

      // populating the outer query select entities with the inner queries.
      // take first one in the list,
      String firstStat = null;
      for (String stat : fromQueries.keySet()) {
         firstStat = stat;
         break;
      }
      // take off the function names from the outer query select entites and
      // set the table aliases in select
      // entities to the inner query aliases. change the colum name of the
      // select entity if it has a function on it,
      // append the function name to colum name, because in the inner queries,
      // we are giving the aliases of select
      // entity as selectEntity column name +"_"+stat
      for (SelectEntity selectEntity : outerQuery.getSelectEntities()) {
         if (selectEntity.getFunctionName() == null) {
            selectEntity.getTableColumn().getTable().setAlias(fromQueries.get(firstStat).getAlias());
         } else {
            for (String stat : fromQueries.keySet()) {
               if (stat.equals(selectEntity.getFunctionName().getValue())) {
                  if (logger.isDebugEnabled()) {
                     logger.debug("For stat : " + stat);
                  }
                  selectEntity.getTableColumn().getColumn().setColumnName(
                           selectEntity.getTableColumn().getColumn().getColumnName() + "_" + stat);
                  selectEntity.getTableColumn().getTable().setAlias(fromQueries.get(stat).getAlias());
                  if (logger.isDebugEnabled()) {
                     logger.debug("Select Entity Column name "
                              + selectEntity.getTableColumn().getColumn().getColumnName());
                     logger.debug("Select entity alias " + selectEntity.getTableColumn().getTable().getAlias());
                  }
                  selectEntity.setFunctionName(null);
                  break;
               }
            }
         }
      }
   }

   /**
    * This method will take the functions from the select entites and put these function names in the alises of the
    * select entity
    * 
    * @param query
    */
   private void populateMeasureAlias (Query query) {
      for (SelectEntity selectEntity : query.getSelectEntities()) {
         if (selectEntity.getFunctionName() != null) {
            selectEntity.setFunctionName(null);
         }
      }
   }

   /**
    * This method will populate the inner queries if there are more than one stats on the query present. It prepares a
    * inner query for each of the stat. It takes all the select entities from the main query and removes some of them
    * using the knowledge of stat. Before altering we will replicate the original query select section otherwise it will
    * alter the original object itself.
    * 
    * @param query
    * @param uniqueStats
    * @return fromQueries
    * @throws CloneNotSupportedException
    */
   private Map<String, Query> populateFromInnerQueries (Query query, Set<String> uniqueStats)
            throws CloneNotSupportedException {
      if (logger.isDebugEnabled()) {
         logger.debug("Inside populateFromInnerQueries method");
      }
      int index = 0;
      Map<String, Query> fromQueries = new HashMap<String, Query>();
      for (String stat : uniqueStats) {
         Query fromQuery = new Query();
         // get the original query object
         replicateQuery(fromQuery, query);
         // override SL_STAT dimension to it.
         overrideStatConditionalEntity(fromQuery, stat);

         List<SelectEntity> fromQuerySelectEntities = new ArrayList<SelectEntity>();
         for (SelectEntity selectEntity : fromQuery.getSelectEntities()) {
            if (logger.isDebugEnabled()) {
               logger.debug("For Select Entity " + selectEntity.getTableColumn().getColumn().getColumnName());
               logger.debug(" Stat is " + selectEntity.getFunctionName());
            }
            if (selectEntity.getFunctionName() == null) {
               fromQuerySelectEntities.add(selectEntity);
               selectEntity.setAlias(selectEntity.getTableColumn().getColumn().getColumnName());
            } else if (stat.equals(selectEntity.getFunctionName().getValue())) {
               selectEntity.setAlias(selectEntity.getTableColumn().getColumn().getColumnName() + "_" + stat);
               fromQuerySelectEntities.add(selectEntity);
               selectEntity.setFunctionName(null);
            }
         }
         fromQuery.setSelectEntities(fromQuerySelectEntities);

         List<ConditionEntity> fromQueryConditionEntities = new ArrayList<ConditionEntity>();
         for (ConditionEntity conditionEntity : fromQuery.getWhereEntities()) {
            StatType conditionStat = conditionEntity.getLhsFunctionName();
            if (conditionStat != null) {
               if (stat.equalsIgnoreCase(conditionStat.getValue())) {
                  fromQueryConditionEntities.add(conditionEntity);
               }
            } else {
               fromQueryConditionEntities.add(conditionEntity);
            }
         }
         fromQuery.setWhereEntities(fromQueryConditionEntities);

         fromQuery.setAlias("IQ" + index++);
         fromQueries.put(stat, fromQuery);
      }
      return fromQueries;
   }

   /**
    * This method will do the physical cloning of the query to the fromQuery object. Because we are going to tweak inner
    * queries select section that will reflect on the outer query, in order to protect the outer query as it is, we will
    * do cloning element by elemnt of the select section.
    * 
    * @param fromQuery
    * @param query
    * @throws CloneNotSupportedException
    */
   private void replicateQuery (Query fromQuery, Query query) throws CloneNotSupportedException {
      if (ExecueCoreUtil.isCollectionNotEmpty(query.getSelectEntities())) {
         List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
         for (SelectEntity selectEntity : query.getSelectEntities()) {
            SelectEntity clonedSelectEntity = ExecueBeanCloneUtil.cloneSelectEntity(selectEntity);
            selectEntities.add(clonedSelectEntity);
         }
         fromQuery.setSelectEntities(selectEntities);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(query.getWhereEntities())) {
         List<ConditionEntity> conditionalEntities = new ArrayList<ConditionEntity>();
         for (ConditionEntity conditionEntity : query.getWhereEntities()) {
            ConditionEntity clonedConditionEntity = ExecueBeanCloneUtil.cloneConditionalEntity(conditionEntity);
            conditionalEntities.add(clonedConditionEntity);
         }
         fromQuery.setWhereEntities(conditionalEntities);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(query.getOrderingEntities())) {
         fromQuery.setOrderingEntities(new ArrayList<OrderEntity>(query.getOrderingEntities()));
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(query.getGroupingEntities())) {
         fromQuery.setGroupingEntities(new ArrayList<SelectEntity>(query.getGroupingEntities()));
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(query.getHavingEntities())) {
         fromQuery.setHavingEntities(new ArrayList<ConditionEntity>(query.getHavingEntities()));
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(query.getFromEntities())) {
         fromQuery.setFromEntities(new ArrayList<FromEntity>(query.getFromEntities()));
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(query.getJoinEntities())) {
         fromQuery.setJoinEntities(new ArrayList<JoinEntity>(query.getJoinEntities()));
      }
      if (query.getAlias() != null) {
         fromQuery.setAlias(new String(query.getAlias()));
      }
      fromQuery.setLimitingCondition(query.getLimitingCondition());
   }

   /**
    * This method will return the unique stats in the select entities list.
    * 
    * @param selectEntities
    * @return uniqueStats
    */
   private Set<String> getUniqueSelectEntityStats (List<SelectEntity> selectEntities) {
      List<String> stats = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionNotEmpty(selectEntities)) {
         for (SelectEntity selectEntity : selectEntities) {
            if (selectEntity.getFunctionName() != null) {
               stats.add(selectEntity.getFunctionName().getValue());
            }
         }
      }
      return new HashSet<String>(stats);
   }

   /**
    * This method will overide the dimension SL_STAT. It sets its value to the stat passed. Get the conditionalEntity
    * whose name is SL_STAT and override its value with value of stat passes.
    * 
    * @param query
    * @param stat
    */
   private void overrideStatConditionalEntity (Query query, String stat) {
      for (ConditionEntity conditionEntity : query.getWhereEntities()) {
         if (!conditionEntity.isSubCondition()
                  && conditionEntity.getLhsTableColumn().getColumn().getColumnName().equalsIgnoreCase(
                           getCoreConfigurationService().getStatisticsColumnName())) {
            conditionEntity.setOperator(OperatorType.EQUALS);
            conditionEntity.setOperandType(QueryConditionOperandType.VALUE);
            List<QueryValue> rhsValues = new ArrayList<QueryValue>();
            QueryValue queryValue = new QueryValue();
            queryValue.setValue(stat);
            queryValue.setDataType(DataType.STRING);
            rhsValues.add(queryValue);
            conditionEntity.setRhsValues(rhsValues);
            break;
         }
      }
   }

   /**
    * This method will prepare QueryTablecolum object using the information passed
    * 
    * @param tableName
    * @param tableAlias
    * @param columName
    * @return queryTableColum
    */
   private QueryTableColumn createQueryTableColum (String tableName, String tableAlias, String columName) {
      QueryTableColumn queryTableColumn = new QueryTableColumn();
      QueryTable queryTable = new QueryTable();
      QueryColumn queryColumn = new QueryColumn();
      queryTable.setTableName(tableName);
      queryTable.setAlias(tableAlias);
      queryColumn.setColumnName(columName);
      queryTableColumn.setTable(queryTable);
      queryTableColumn.setColumn(queryColumn);
      return queryTableColumn;
   }

   /**
    * This method will return the dimensions list which is present in where section of the query among all the
    * dimensions
    * 
    * @param query
    * @param dimensionColums
    * @return queryDimensions
    */
   private List<QueryTableColumn> getQueryWhereClauseDimensionColums (Query query,
            List<QueryTableColumn> dimensionTableColums) {
      // TODO : -VG- check this
      if (ExecueCoreUtil.isCollectionEmpty(query.getWhereEntities())) {
         return new ArrayList<QueryTableColumn>();
      }
      List<QueryTableColumn> queryWhereClauseTableColumns = new ArrayList<QueryTableColumn>();
      for (ConditionEntity conditionEntity : query.getWhereEntities()) {
         if (dimensionTableColums.contains(conditionEntity.getLhsTableColumn())) {
            queryWhereClauseTableColumns.add(conditionEntity.getLhsTableColumn());
         }
      }
      return queryWhereClauseTableColumns;
   }

   /**
    * This method will populate the dimensions which are not present in the where section of the query. It will populate
    * these dimensions according to the cube structured execue has
    * 
    * @param query
    * @param dimensionColums
    * @param queryWhereClauseDimensionColums
    * @param querySelectClauseDimensionColums
    */
   private void populateMissingDimensions (Query query, List<QueryTableColumn> dimensionColums,
            List<QueryTableColumn> queryWhereClauseDimensionColums,
            List<QueryTableColumn> querySelectClauseDimensionColums, Asset asset) {
      List<QueryTableColumn> cubeMissingDimensions = new ArrayList<QueryTableColumn>(dimensionColums);
      cubeMissingDimensions.removeAll(queryWhereClauseDimensionColums);
      // populate the conditional entities for each of the cube dimension
      // which is not present in query.
      // dimension = '#all#' is the conditional entity for each of the missing
      // dimension
      for (QueryTableColumn cubeMissingDimensionTableColumn : cubeMissingDimensions) {
         ConditionEntity conditionEntity = new ConditionEntity();
         conditionEntity.setOperandType(QueryConditionOperandType.VALUE);
         conditionEntity.setLhsTableColumn(cubeMissingDimensionTableColumn);
         if (querySelectClauseDimensionColums.contains(cubeMissingDimensionTableColumn)) {
            conditionEntity.setOperator(OperatorType.NOT_EQUALS);
         } else {
            conditionEntity.setOperator(OperatorType.EQUALS);
         }
         QueryValue queryValue = new QueryValue();
         queryValue.setValue(getCoreConfigurationService().getCubeAllValue());
         queryValue.setDataType(DataType.STRING);
         List<QueryValue> queryValues = new ArrayList<QueryValue>();
         queryValues.add(queryValue);
         conditionEntity.setRhsValues(queryValues);
         if (ExecueCoreUtil.isCollectionEmpty(query.getWhereEntities())) {
            List<ConditionEntity> whereEntities = new ArrayList<ConditionEntity>();
            whereEntities.add(conditionEntity);
            query.setWhereEntities(whereEntities);
         } else {
            query.getWhereEntities().add(conditionEntity);
         }
      }
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService
    *           the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}
