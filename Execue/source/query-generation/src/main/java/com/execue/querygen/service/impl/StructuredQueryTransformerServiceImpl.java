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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.StructuredCondition;
import com.execue.core.common.bean.governor.StructuredLimitClause;
import com.execue.core.common.bean.governor.StructuredOrderClause;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.FromEntity;
import com.execue.core.common.bean.querygen.JoinEntity;
import com.execue.core.common.bean.querygen.LimitEntity;
import com.execue.core.common.bean.querygen.OrderEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryGenerationInput;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.JoinType;
import com.execue.core.common.type.OperandType;
import com.execue.core.common.type.QueryConditionOperandType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.querygen.service.ICubeQueryEnhancerService;
import com.execue.querygen.service.IStructuredQueryTransformerService;
import com.execue.querygen.service.QueryGenerationServiceFactory;

public class StructuredQueryTransformerServiceImpl implements IStructuredQueryTransformerService {

   private static final Logger           logger = Logger.getLogger(StructuredQueryTransformerServiceImpl.class);
   private QueryGenerationServiceFactory queryGenerationServiceFactory;
   private ICubeQueryEnhancerService     cubeQueryEnhancerService;

   /**
    * Construct the Query generation input object based on the asset properties in order to provide input to the query
    * generation service. This method will populate Query object using the information stored in StructuredQuery. For
    * putting the aliases for query object, we have used a counter called queryAliasCounter along with string "OQ" -
    * outer query.
    * 
    * @param structuredQuery
    * @param aliasBusinessAssetTermMap
    * @return queryGenerationInput
    */
   public QueryGenerationOutput populateQueryGenerationOutput (StructuredQuery structuredQuery,
            Map<String, BusinessAssetTerm> aliasBusinessAssetTermMap) {

      if (logger.isDebugEnabled()) {
         logger.debug("Inside populateQueryGenerationOutput method");
         logger.debug("Got StructuredQuery for which we need to prepare QueryGenerationOutput object : "
                  + structuredQuery);
      }

      long totalStartTime = System.currentTimeMillis();
      List<Integer> mainStructuredQueryIndexes = new ArrayList<Integer>();
      StructuredQuery mainStructuredQuery = populateStructuredQuery(mainStructuredQueryIndexes, structuredQuery, false);
      long startTime = System.currentTimeMillis();
      Query mainQuery = populateQuery(mainStructuredQuery, aliasBusinessAssetTermMap);
      long endTime = 0;

      if (logger.isDebugEnabled()) {
         endTime = System.currentTimeMillis();
         logger.debug("Time taken to populate Query from structured query " + (endTime - startTime) / 1000.0
                  + " seconds");
      }

      QueryGenerationInput mainQueryGenerationInput = populateQueryGenerationInput(mainQuery, "OQ1", structuredQuery
               .getAsset());
      boolean isExecueOwnedCube = ExecueBeanUtil.isExecueOwnedCube(structuredQuery.getAsset());
      if (isExecueOwnedCube) {
         startTime = System.currentTimeMillis();
         Asset asset = mainQueryGenerationInput.getTargetAsset();
         List<Query> inputQueries = mainQueryGenerationInput.getInputQueries();
         List<Query> modifiedQueries = new ArrayList<Query>();
         for (Query query : inputQueries) {
            Query modifiedQuery = getCubeQueryEnhancerService().modifyExecueOwnedCubeQuery(asset, query);
            modifiedQueries.add(modifiedQuery);
         }
         mainQueryGenerationInput.setInputQueries(modifiedQueries);
         if (logger.isDebugEnabled()) {
            endTime = System.currentTimeMillis();
            logger.debug("Time taken to modify query for cube " + (endTime - startTime) / 1000.0 + " seconds");
         }
      }
      startTime = System.currentTimeMillis();
      QueryGenerationOutput mainQueryGenerationOutput = getQueryGenerationServiceFactory().getQueryGenerationService(
               structuredQuery.getAsset()).generateQuery(mainQueryGenerationInput);

      if (logger.isDebugEnabled()) {
         endTime = System.currentTimeMillis();
         logger.debug("Time taken to generate QueryGenerationOuput" + (endTime - startTime) / 1000.0 + " seconds");
      }

      if (structuredQuery.getCohort() != null) {
         // TODO:: NK:: Need to handled combining query scenario(eg. hierarchy query) for cohorts
         List<Integer> cohortStructuredQueryIndexes = new ArrayList<Integer>();
         StructuredQuery cohortStructuredQuery = populateStructuredQuery(cohortStructuredQueryIndexes, structuredQuery,
                  true);
         Query cohortQuery = populateQuery(cohortStructuredQuery, aliasBusinessAssetTermMap);
         QueryGenerationInput cohortQueryGenerationInput = populateQueryGenerationInput(cohortQuery, "OQ2",
                  structuredQuery.getAsset());
         QueryGenerationOutput cohortQueryGenerationOutput = getQueryGenerationServiceFactory()
                  .getQueryGenerationService(structuredQuery.getAsset()).generateQuery(cohortQueryGenerationInput);
         // merge query generation inputs
         Query mergedRawQuery = alignCohortQuery(mainQueryGenerationOutput.getQueryGenerationInput().getInputQueries()
                  .get(0), cohortQueryGenerationOutput.getQueryGenerationInput().getInputQueries().get(0),
                  mainStructuredQueryIndexes, cohortStructuredQueryIndexes);
         Query mergedPopulatedQuery = alignCohortQuery(mainQueryGenerationOutput.getResultQuery(),
                  cohortQueryGenerationOutput.getResultQuery(), mainStructuredQueryIndexes,
                  cohortStructuredQueryIndexes);
         // prepare merged query generation output
         QueryGenerationInput queryGenerationInput = populateQueryGenerationInput(mergedRawQuery, "OQ3",
                  structuredQuery.getAsset());
         mainQueryGenerationOutput.setQueryGenerationInput(queryGenerationInput);
         mainQueryGenerationOutput.setResultQuery(mergedPopulatedQuery);
      }

      long totalEndTime = 0;
      if (logger.isDebugEnabled()) {
         totalEndTime = System.currentTimeMillis();
         logger.debug("Time taken to generate QueryGenerationOutput " + (totalEndTime - totalStartTime) / 1000.0
                  + " seconds");
      }

      return mainQueryGenerationOutput;
   }

   private QueryGenerationInput populateQueryGenerationInput (Query query, String queryAlias, Asset asset) {
      QueryGenerationInput queryGenerationInput = new QueryGenerationInput();
      List<Query> inputQueries = new ArrayList<Query>();
      query.setAlias(queryAlias);
      inputQueries.add(query);
      queryGenerationInput.setInputQueries(inputQueries);
      queryGenerationInput.setTargetAsset(asset);
      return queryGenerationInput;
   }

   private StructuredQuery populateStructuredQuery (List<Integer> structuredQueryIndexes,
            StructuredQuery structuredQuery, boolean isCohort) {
      StructuredQuery sQuery = new StructuredQuery();
      sQuery.setAsset(structuredQuery.getAsset());
      sQuery.setPopulations(structuredQuery.getPopulations());
      sQuery.setRollupQuery(structuredQuery.isRollupQuery());
      List<BusinessAssetTerm> metrics = structuredQuery.getMetrics();
      if (isCohort) {
         StructuredQuery cohortQuery = structuredQuery.getCohort();
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getMetrics())) {
            List<BusinessAssetTerm> cohortMetrics = new ArrayList<BusinessAssetTerm>();
            for (int index = 0; index < metrics.size(); index++) {
               BusinessAssetTerm businessAssetTerm = metrics.get(index);
               if (businessAssetTerm.getBusinessTerm().isFromCohort()) {
                  cohortMetrics.add(businessAssetTerm);
                  structuredQueryIndexes.add(index);
               }
               if (businessAssetTerm.getBusinessTerm().isFromPopulation()) {
                  cohortMetrics.add(businessAssetTerm);
               }
            }
            sQuery.setMetrics(cohortMetrics);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getSummarizations())) {
            List<BusinessAssetTerm> cohortSummarizations = new ArrayList<BusinessAssetTerm>();
            for (BusinessAssetTerm businessAssetTerm : structuredQuery.getSummarizations()) {
               if (businessAssetTerm.getBusinessTerm().isFromCohort()) {
                  cohortSummarizations.add(businessAssetTerm);
               }
            }
            sQuery.setSummarizations(cohortSummarizations);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getOrderClauses())) {
            List<StructuredOrderClause> cohortOrderClauses = new ArrayList<StructuredOrderClause>();
            for (StructuredOrderClause structuredOrderClause : structuredQuery.getOrderClauses()) {
               if (structuredOrderClause.getBusinessAssetTerm().getBusinessTerm().isFromCohort()) {
                  cohortOrderClauses.add(structuredOrderClause);
               }
            }
            sQuery.setOrderClauses(cohortOrderClauses);
         }
         sQuery.setConditions(cohortQuery.getConditions());
      } else {
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getMetrics())) {
            List<BusinessAssetTerm> mainMetrics = new ArrayList<BusinessAssetTerm>();
            for (BusinessAssetTerm businessAssetTerm : metrics) {
               if (!businessAssetTerm.getBusinessTerm().isFromCohort()) {
                  mainMetrics.add(businessAssetTerm);
                  structuredQueryIndexes.add(metrics.indexOf(businessAssetTerm));
               }
            }
            sQuery.setMetrics(mainMetrics);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getSummarizations())) {
            List<BusinessAssetTerm> mainSummarizations = new ArrayList<BusinessAssetTerm>();
            for (BusinessAssetTerm businessAssetTerm : structuredQuery.getSummarizations()) {
               if (!businessAssetTerm.getBusinessTerm().isFromCohort()) {
                  mainSummarizations.add(businessAssetTerm);
               }
            }
            sQuery.setSummarizations(mainSummarizations);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getOrderClauses())) {
            List<StructuredOrderClause> mainOrderClauses = new ArrayList<StructuredOrderClause>();
            for (StructuredOrderClause structuredOrderClause : structuredQuery.getOrderClauses()) {
               if (!structuredOrderClause.getBusinessAssetTerm().getBusinessTerm().isFromCohort()) {
                  mainOrderClauses.add(structuredOrderClause);
               }
            }
            sQuery.setOrderClauses(mainOrderClauses);
         }
         sQuery.setConditions(structuredQuery.getConditions());
         sQuery.setHavingClauses(structuredQuery.getHavingClauses());
         sQuery.setScalingFactor(structuredQuery.getScalingFactor());
         sQuery.setStructuredQueryWeight(structuredQuery.getStructuredQueryWeight());
         sQuery.setTopBottom(structuredQuery.getTopBottom());
      }
      return sQuery;
   }

   private Query alignCohortQuery (Query mainQuery, Query cohortQuery, List<Integer> mainStructuredQueryIndexes,
            List<Integer> cohortStructuredQueryIndexes) {
      // First ensure the tables which are repeating have different aliases
      List<FromEntity> mainQueryFromEntities = mainQuery.getFromEntities();
      List<FromEntity> cohortQueryFromEntities = cohortQuery.getFromEntities();
      for (FromEntity cohortQueryFromEntity : cohortQueryFromEntities) {
         for (FromEntity mainQueryFromEntity : mainQueryFromEntities) {
            if (cohortQueryFromEntity.getTable().getTableName().equals(mainQueryFromEntity.getTable().getTableName())
                     && cohortQueryFromEntity.getTable().getAlias().equals(mainQueryFromEntity.getTable().getAlias())) {
               String oldAlias = cohortQueryFromEntity.getTable().getAlias();
               String newAlias = "C" + oldAlias;
               String tableName = cohortQueryFromEntity.getTable().getTableName();
               updateAliases(oldAlias, newAlias, tableName, cohortQuery);
            }
         }
      }

      // After handling the aliases, merge all the sections of the cohort query with the main query
      if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getSelectEntities())) {
         List<SelectEntity> cohortSelectEntites = getCohortSelectsExcludingPopulations(cohortQuery);
         mainQuery.setSelectEntities(mergeSelectEntities(mainQuery.getSelectEntities(), cohortSelectEntites,
                  mainStructuredQueryIndexes, cohortStructuredQueryIndexes));
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getFromEntities())) {
         mainQuery.getFromEntities().addAll(cohortQuery.getFromEntities());
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getWhereEntities())) {
         if (ExecueCoreUtil.isCollectionEmpty(mainQuery.getWhereEntities())) {
            mainQuery.setWhereEntities(new ArrayList<ConditionEntity>());
         }
         mainQuery.getWhereEntities().addAll(cohortQuery.getWhereEntities());
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getGroupingEntities())) {
         if (ExecueCoreUtil.isCollectionEmpty(mainQuery.getGroupingEntities())) {
            mainQuery.setGroupingEntities(new ArrayList<SelectEntity>());
         }
         mainQuery.getGroupingEntities().addAll(cohortQuery.getGroupingEntities());
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getOrderingEntities())) {
         if (ExecueCoreUtil.isCollectionEmpty(mainQuery.getOrderingEntities())) {
            mainQuery.setOrderingEntities(new ArrayList<OrderEntity>());
         }
         mainQuery.getOrderingEntities().addAll(cohortQuery.getOrderingEntities());
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getHavingEntities())) {
         if (ExecueCoreUtil.isCollectionEmpty(mainQuery.getHavingEntities())) {
            mainQuery.setHavingEntities(new ArrayList<ConditionEntity>());
         }
         mainQuery.getHavingEntities().addAll(cohortQuery.getHavingEntities());
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getJoinEntities())) {
         if (ExecueCoreUtil.isCollectionEmpty(mainQuery.getJoinEntities())) {
            mainQuery.setJoinEntities(new ArrayList<JoinEntity>());
         }
         mainQuery.getJoinEntities().addAll(cohortQuery.getJoinEntities());
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(mainQuery.getPopulationEntities())
               && ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getPopulationEntities())) {
         JoinEntity joinEntity = new JoinEntity();
         QueryTableColumn mainQueryTableColumn = mainQuery.getPopulationEntities().get(0).getTableColumn();
         QueryTableColumn cohortQueryTableColumn = cohortQuery.getPopulationEntities().get(0).getTableColumn();
         joinEntity.setLhsOperand(mainQueryTableColumn);
         joinEntity.setRhsOperand(cohortQueryTableColumn);
         joinEntity.setType(JoinType.INNER);
         if (ExecueCoreUtil.isCollectionEmpty(mainQuery.getJoinEntities())) {
            mainQuery.setJoinEntities(new ArrayList<JoinEntity>());
         }
         mainQuery.getJoinEntities().add(joinEntity);
      }
      return mainQuery;
   }

   private List<SelectEntity> mergeSelectEntities (List<SelectEntity> mainSelectEntities,
            List<SelectEntity> cohortSelectEntites, List<Integer> mainStructuredQueryIndexes,
            List<Integer> cohortStructuredQueryIndexes) {
      int mergedListSize = mainSelectEntities.size() + cohortSelectEntites.size();
      List<SelectEntity> mergedSelectEntities = new ArrayList<SelectEntity>();
      for (int i = 0; i < mergedListSize; i++) {
         mergedSelectEntities.add(null);
      }
      for (int i = 0; i < mainStructuredQueryIndexes.size(); i++) {
         mergedSelectEntities.set(mainStructuredQueryIndexes.get(i), mainSelectEntities.get(i));
      }
      for (int i = 0; i < cohortStructuredQueryIndexes.size(); i++) {
         mergedSelectEntities.set(cohortStructuredQueryIndexes.get(i), cohortSelectEntites.get(i));
      }
      return mergedSelectEntities;
   }

   /**
    * Prepare the list of selects from passed in Query object excluding the populations
    * 
    * @param query
    * @return selects
    */
   private List<SelectEntity> getCohortSelectsExcludingPopulations (Query query) {
      if (ExecueCoreUtil.isCollectionEmpty(query.getPopulationEntities())) {
         return query.getSelectEntities();
      }
      List<SelectEntity> selects = new ArrayList<SelectEntity>();
      for (SelectEntity selectEntity : query.getSelectEntities()) {
         if (!isEntityExistsInPopulation(selectEntity, query.getPopulationEntities())) {
            selects.add(selectEntity);
         }
      }
      return selects;
   }

   private boolean isEntityExistsInPopulation (SelectEntity currSelectEntity, List<SelectEntity> populationEntities) {
      boolean isExists = false;
      for (SelectEntity populationEntity : populationEntities) {
         QueryTableColumn selectEntityQueryTableColumn = currSelectEntity.getTableColumn();
         QueryTableColumn populationEntityTableColumn = populationEntity.getTableColumn();
         if (selectEntityQueryTableColumn.getTable().equals(populationEntityTableColumn.getTable())
                  && selectEntityQueryTableColumn.getColumn().getColumnName().equalsIgnoreCase(
                           populationEntityTableColumn.getColumn().getColumnName())) {
            isExists = true;
            break;
         }
      }
      return isExists;
   }

   private void updateAliases (String oldAlias, String newAlias, String tableName, Query cohortQuery) {
      if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getSelectEntities())) {
         for (SelectEntity selectEntity : cohortQuery.getSelectEntities()) {
            QueryTable queryTable = selectEntity.getTableColumn().getTable();
            if (tableName.equals(queryTable.getTableName()) && oldAlias.equals(queryTable.getAlias())) {
               selectEntity.getTableColumn().getTable().setAlias(newAlias);
            }
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getFromEntities())) {
         for (FromEntity fromEntity : cohortQuery.getFromEntities()) {
            QueryTable queryTable = fromEntity.getTable();
            if (tableName.equals(queryTable.getTableName()) && oldAlias.equals(queryTable.getAlias())) {
               fromEntity.getTable().setAlias(newAlias);
            }
         }
      }
      // where
      if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getWhereEntities())) {
         for (ConditionEntity whereEntity : cohortQuery.getWhereEntities()) {
            updateConditionalEntityAliasForCohortQuery(whereEntity, oldAlias, newAlias, tableName);
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getGroupingEntities())) {
         for (SelectEntity groupEntity : cohortQuery.getGroupingEntities()) {
            QueryTable queryTable = groupEntity.getTableColumn().getTable();
            if (tableName.equals(queryTable.getTableName()) && oldAlias.equals(queryTable.getAlias())) {
               groupEntity.getTableColumn().getTable().setAlias(newAlias);
            }
         }
      }

      if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getPopulationEntities())) {
         for (SelectEntity populationEntity : cohortQuery.getPopulationEntities()) {
            QueryTable queryTable = populationEntity.getTableColumn().getTable();
            if (tableName.equals(queryTable.getTableName()) && oldAlias.equals(queryTable.getAlias())) {
               populationEntity.getTableColumn().getTable().setAlias(newAlias);
            }
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getOrderingEntities())) {
         for (OrderEntity orderEntity : cohortQuery.getOrderingEntities()) {
            QueryTable queryTable = orderEntity.getSelectEntity().getTableColumn().getTable();
            if (tableName.equals(queryTable.getTableName()) && oldAlias.equals(queryTable.getAlias())) {
               orderEntity.getSelectEntity().getTableColumn().getTable().setAlias(newAlias);
            }
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getHavingEntities())) {
         for (ConditionEntity havingEntity : cohortQuery.getHavingEntities()) {
            updateConditionalEntityAliasForCohortQuery(havingEntity, oldAlias, newAlias, tableName);
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getJoinEntities())) {
         for (JoinEntity joinEntity : cohortQuery.getJoinEntities()) {
            QueryTable queryTable = joinEntity.getLhsOperand().getTable();
            if (tableName.equals(queryTable.getTableName()) && oldAlias.equals(queryTable.getAlias())) {
               joinEntity.getLhsOperand().getTable().setAlias(newAlias);
            }
            queryTable = joinEntity.getRhsOperand().getTable();
            if (tableName.equals(queryTable.getTableName()) && oldAlias.equals(queryTable.getAlias())) {
               joinEntity.getRhsOperand().getTable().setAlias(newAlias);
            }
         }
      }
   }

   private void updateConditionalEntityAliasForCohortQuery (ConditionEntity conditionEntity, String oldAlias,
            String newAlias, String tableName) {
      if (conditionEntity.isSubCondition()) {
         for (ConditionEntity subConditionEntity : conditionEntity.getSubConditionEntities()) {
            updateConditionalEntityAliasForCohortQuery(subConditionEntity, oldAlias, newAlias, tableName);
         }
      } else {
         QueryTable queryTable = conditionEntity.getLhsTableColumn().getTable();
         if (tableName.equals(queryTable.getTableName()) && oldAlias.equals(queryTable.getAlias())) {
            conditionEntity.getLhsTableColumn().getTable().setAlias(newAlias);
         }
      }
   }

   /**
    * This method is helper method to populateQueryGenerationInput() in order to create Query object. It populates each
    * section of query object using the information in structured query and finally creating a query object out of them
    * For aliases, we have used strings to distinguish each section of the query : "SE" means select Entity in Query
    * object, "SQ" means sub query in Query object's ConditionEntity, "GE" means Group Entity in query object and "OE"
    * means order entity in query object
    * 
    * @param structuredQuery
    * @param aliasBusinessAssetTermMap
    * @return query
    */
   private Query populateQuery (StructuredQuery structuredQuery,
            Map<String, BusinessAssetTerm> aliasBusinessAssetTermMap) {

      if (logger.isDebugEnabled()) {
         logger.debug("Inside populateQuery method");
         logger.debug("Got StructuredQuery for which we need to prepare Query object : " + structuredQuery);
      }

      Query query = null;
      List<SelectEntity> selectEntities = populateSelectEntities(structuredQuery.getMetrics(), true,
               aliasBusinessAssetTermMap);
      List<ConditionEntity> whereEntities = populateConditionalEntities(structuredQuery.getConditions(), "SQ");
      List<SelectEntity> groupingEntities = populateSelectEntities(structuredQuery.getSummarizations(), false,
               aliasBusinessAssetTermMap);
      List<ConditionEntity> havingEntities = populateConditionalEntities(structuredQuery.getHavingClauses(), "SQ");
      LimitEntity limitEntity = null;
      if (structuredQuery.getTopBottom() != null) {
         limitEntity = createLimitEntity(structuredQuery.getTopBottom());
      }
      List<OrderEntity> orderingEntities = populateOrderingEntities(structuredQuery.getOrderClauses(), "OE",
               aliasBusinessAssetTermMap);
      List<SelectEntity> populationEntities = populateSelectEntities(structuredQuery.getPopulations(), false,
               aliasBusinessAssetTermMap);
      QueryTableColumn scalingFactorEntity = null;
      if (structuredQuery.getScalingFactor() != null) {
         scalingFactorEntity = populateScalingFactorEntity(structuredQuery.getScalingFactor());

         if (logger.isDebugEnabled()) {
            logger.debug("Got ScalingFactor Entity for Query " + scalingFactorEntity);
         }
      }
      // at least selectEntites should be there
      if (ExecueCoreUtil.isCollectionNotEmpty(selectEntities)) {
         query = new Query();
         query.setRollupQuery(structuredQuery.isRollupQuery());
         query.setSelectEntities(selectEntities);
         if (ExecueCoreUtil.isCollectionNotEmpty(whereEntities)) {
            query.setWhereEntities(whereEntities);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(groupingEntities)) {
            query.setGroupingEntities(groupingEntities);
         }
         // having cannot come without groupby
         if (ExecueCoreUtil.isCollectionNotEmpty(havingEntities)
                  && ExecueCoreUtil.isCollectionNotEmpty(groupingEntities)) {
            query.setHavingEntities(havingEntities);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(orderingEntities)) {
            query.setOrderingEntities(orderingEntities);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(populationEntities)) {
            query.setPopulationEntities(populationEntities);
         }
         if (limitEntity != null) {
            query.setLimitingCondition(limitEntity);
         }
         if (scalingFactorEntity != null) {
            query.setScalingFactorEntity(scalingFactorEntity);
         }
      }
      return query;
   }

   private QueryTableColumn populateScalingFactorEntity (BusinessAssetTerm scalingFactor) {
      return ExecueBeanManagementUtil.createQueryTableColum(scalingFactor);
   }

   /**
    * This method will create the limit entity which has information about the number of rows it has to limit to
    */
   private LimitEntity createLimitEntity (StructuredLimitClause topBottom) {
      if (logger.isDebugEnabled()) {
         logger.debug("Creating limit entity");
      }

      LimitEntity limitEntity = new LimitEntity();
      limitEntity.setStartingNumber(1L);

      if (topBottom.getStartValue() != null) {
         limitEntity.setStartingNumber(topBottom.getStartValue().longValue());
      }

      limitEntity.setEndingNumber(topBottom.getLimitValue().longValue());
      return limitEntity;
   }

   /**
    * This method is helper method to populateQueryGenerationInput() in order to populate List<SelectEntity> for
    * selectEntities in query object.
    * 
    * @param businessAssetTerms
    * @param aliasBusinessAssetTermMap
    * @return selectEntities
    */
   private List<SelectEntity> populateSelectEntities (List<BusinessAssetTerm> businessAssetTerms,
            boolean isSelectSectionCall, Map<String, BusinessAssetTerm> aliasBusinessAssetTermMap) {
      if (logger.isDebugEnabled()) {
         logger.debug("Inside populateSelectEntities method");
      }

      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessAssetTerms)) {
         List<String> aliases = new ArrayList<String>();
         for (BusinessAssetTerm businessAssetTerm : businessAssetTerms) {
            SelectEntity selectEntity = new SelectEntity();
            if (BusinessEntityType.CONCEPT.equals(businessAssetTerm.getBusinessTerm().getBusinessEntityTerm()
                     .getBusinessEntityType())) {
               QueryTableColumn queryTableColumn = ExecueBeanManagementUtil.createQueryTableColum(businessAssetTerm);
               selectEntity.setTableColumn(queryTableColumn);
               selectEntity.setType(SelectEntityType.TABLE_COLUMN);

               if (logger.isDebugEnabled()) {
                  logger.debug("Checking if the range is defined. Range : "
                           + businessAssetTerm.getBusinessTerm().getRange());
               }

               if (businessAssetTerm.getBusinessTerm().getRange() != null) {
                  selectEntity.setRange(businessAssetTerm.getBusinessTerm().getRange());
               }
               selectEntities.add(selectEntity);

            } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(businessAssetTerm.getBusinessTerm()
                     .getBusinessEntityTerm().getBusinessEntityType())) {
               QueryValue queryValue = new QueryValue();
               Membr membr = (Membr) businessAssetTerm.getAssetEntityTerm().getAssetEntity();
               Colum colum = membr.getLookupColumn();
               queryValue.setDataType(colum.getDataType());
               queryValue.setValue(membr.getLookupValue());
               selectEntity.setType(SelectEntityType.VALUE);
               selectEntity.setQueryValue(queryValue);
               selectEntities.add(selectEntity);
            }
            if (businessAssetTerm.getBusinessTerm().getBusinessStat() != null) {
               selectEntity.setFunctionName(businessAssetTerm.getBusinessTerm().getBusinessStat().getStat()
                        .getStatType());
            }
            if (isSelectSectionCall) {
               String alias = ExecueCoreUtil.getAlias(aliases);
               selectEntity.setAlias(alias);
               aliasBusinessAssetTermMap.put(alias, businessAssetTerm);
               aliases.add(alias);
            }
         }
      }
      return selectEntities;
   }

   /**
    * This method is helper method to populateQueryGenerationInput() in order to populate ConditionalEntities for either
    * where clause or having clause of query object.
    * 
    * @param structuredConditions
    * @param aliasType
    * @return conditionalEntities
    */
   private List<ConditionEntity> populateConditionalEntities (List<StructuredCondition> structuredConditions,
            String aliasType) {
      List<ConditionEntity> conditionalEntities = new ArrayList<ConditionEntity>();
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredConditions)) {
         for (StructuredCondition structuredCondition : structuredConditions) {
            if (structuredCondition.isSubCondition()) {
               ConditionEntity conditionEntity = new ConditionEntity();
               conditionEntity.setSubCondition(true);
               List<ConditionEntity> subConditionEntities = new ArrayList<ConditionEntity>();
               for (StructuredCondition subCondition : structuredCondition.getSubConditions()) {
                  subConditionEntities.add(populateConditionEntity(subCondition));
               }
               conditionEntity.setSubConditionEntities(subConditionEntities);
               conditionalEntities.add(conditionEntity);
            } else {
               conditionalEntities.add(populateConditionEntity(structuredCondition));
            }
         }
      }
      return conditionalEntities;
   }

   private ConditionEntity populateConditionEntity (StructuredCondition structuredCondition) {
      ConditionEntity conditionalEntity = new ConditionEntity();
      boolean conditionFullyPopulated = false;
      QueryTableColumn lhsQueryTableColum = ExecueBeanManagementUtil.createQueryTableColum(structuredCondition
               .getLhsBusinessAssetTerm());
      conditionalEntity.setLhsTableColumn(lhsQueryTableColum);
      if (structuredCondition.getLhsBusinessAssetTerm().getBusinessTerm().getBusinessStat() != null) {
         conditionalEntity.setLhsFunctionName(structuredCondition.getLhsBusinessAssetTerm().getBusinessTerm()
                  .getBusinessStat().getStat().getStatType());
      }
      conditionalEntity.setSubCondition(structuredCondition.isSubCondition());
      conditionalEntity.setOperator(structuredCondition.getOperator());
      // if right hand side is a business term, it must be an
      // instance, get its member value and
      // put it into query values of conditional entity.
      if (OperandType.BUSINESS_TERM.equals(structuredCondition.getOperandType())) {
         conditionalEntity.setOperandType(QueryConditionOperandType.VALUE);
         List<QueryValue> rhsValues = new ArrayList<QueryValue>();
         for (BusinessAssetTerm businessAssetTerm : structuredCondition.getRhsBusinessAssetTerms()) {
            if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(businessAssetTerm.getBusinessTerm()
                     .getBusinessEntityTerm().getBusinessEntityType())) {
               Membr membr = (Membr) businessAssetTerm.getAssetEntityTerm().getAssetEntity();
               QueryValue queryValue = new QueryValue();
               DataType dataType = lhsQueryTableColum.getColumn().getDataType();
               String lookupValue = membr.getLookupValue();
               // handling special character in member value
               if (DataType.STRING.equals(dataType)) {
                  lookupValue = lookupValue.replaceAll("'", "\\\\'");
               }
               queryValue.setValue(lookupValue);
               queryValue.setDataType(dataType);
               rhsValues.add(queryValue);
            }
         }
         conditionalEntity.setRhsValues(rhsValues);
         conditionFullyPopulated = true;
      } else if (OperandType.BUSINESS_QUERY.equals(structuredCondition.getOperandType())) {
         conditionalEntity.setOperandType(QueryConditionOperandType.SUB_QUERY);
         Map<String, BusinessAssetTerm> aliasBusinessAssetTermMap = new HashMap<String, BusinessAssetTerm>();
         Query subQuery = populateQuery(structuredCondition.getRhsStructuredQuery(), aliasBusinessAssetTermMap);
         subQuery.setAlias(ExecueCoreUtil.getAlias(new ArrayList<String>()));
         conditionalEntity.setRhsSubQuery(subQuery);
         conditionFullyPopulated = true;
      } else if (OperandType.VALUE.equals(structuredCondition.getOperandType())) {
         conditionalEntity.setOperandType(QueryConditionOperandType.VALUE);
         for (QueryValue queryValue : structuredCondition.getRhsValues()) {
            queryValue.setDataType(lhsQueryTableColum.getColumn().getDataType());
         }
         conditionalEntity.setRhsValues(structuredCondition.getRhsValues());
         conditionFullyPopulated = true;
      }
      if (!conditionFullyPopulated) {
         conditionalEntity = null;
      }
      return conditionalEntity;
   }

   /**
    * This method is helper method to populateQueryGenerationInput() in order to populate OrderEntities for order clause
    * of query object.
    * 
    * @param orderClauses
    * @param aliasType
    * @param aliasBusinessAssetTermMap
    * @return orderEntities
    */
   private List<OrderEntity> populateOrderingEntities (List<StructuredOrderClause> orderClauses, String aliasType,
            Map<String, BusinessAssetTerm> aliasBusinessAssetTermMap) {
      if (logger.isDebugEnabled()) {
         logger.debug("Inside populateOrderingentities method");
         logger.debug("Got orderClauses for which we need to prepare OrderEntities for Query object : " + orderClauses);
         logger.debug("Got aliasType which indiated the prefix for the alias " + aliasType);
      }
      // prepare the reverse lookup map of aliases and business asset terms
      Map<BusinessAssetTerm, String> reverseBusinessAssetTermAliasMap = new HashMap<BusinessAssetTerm, String>();
      if (!MapUtils.isEmpty(aliasBusinessAssetTermMap)) {
         for (Entry<String, BusinessAssetTerm> entry : aliasBusinessAssetTermMap.entrySet()) {
            reverseBusinessAssetTermAliasMap.put(entry.getValue(), entry.getKey());
         }
      }
      List<OrderEntity> orderEntities = new ArrayList<OrderEntity>();
      if (ExecueCoreUtil.isCollectionNotEmpty(orderClauses)) {
         for (StructuredOrderClause structuredOrderClause : orderClauses) {
            BusinessAssetTerm businessAssetTerm = structuredOrderClause.getBusinessAssetTerm();
            OrderEntity orderEntity = new OrderEntity();
            SelectEntity selectEntity = new SelectEntity();
            String selectClauseAlias = reverseBusinessAssetTermAliasMap.get(structuredOrderClause
                     .getBusinessAssetTerm());
            if (ExecueCoreUtil.isNotEmpty(selectClauseAlias)) {
               selectEntity.setAlias(selectClauseAlias);
            }
            if (businessAssetTerm.getBusinessTerm().getBusinessStat() != null) {
               selectEntity.setFunctionName(businessAssetTerm.getBusinessTerm().getBusinessStat().getStat()
                        .getStatType());
            }
            if (BusinessEntityType.CONCEPT.equals(businessAssetTerm.getBusinessTerm().getBusinessEntityTerm()
                     .getBusinessEntityType())) {
               selectEntity.setTableColumn(ExecueBeanManagementUtil.createQueryTableColum(businessAssetTerm));
               selectEntity.setType(SelectEntityType.TABLE_COLUMN);

               if (logger.isDebugEnabled()) {
                  logger.debug("Checking if the raise is defined. Range : "
                           + businessAssetTerm.getBusinessTerm().getRange());
               }

               if (businessAssetTerm.getBusinessTerm().getRange() != null) {
                  selectEntity.setRange(businessAssetTerm.getBusinessTerm().getRange());
               }
               orderEntity.setSelectEntity(selectEntity);
               orderEntity.setOrderType(structuredOrderClause.getOrderEntityType());
               orderEntities.add(orderEntity);
            } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(businessAssetTerm.getBusinessTerm()
                     .getBusinessEntityTerm().getBusinessEntityType())) {
               QueryValue queryValue = new QueryValue();
               Membr membr = (Membr) businessAssetTerm.getAssetEntityTerm().getAssetEntity();
               Colum colum = membr.getLookupColumn();
               queryValue.setDataType(colum.getDataType());
               queryValue.setValue(membr.getLookupValue());
               selectEntity.setType(SelectEntityType.VALUE);
               selectEntity.setQueryValue(queryValue);
               orderEntity.setSelectEntity(selectEntity);
               orderEntity.setOrderType(structuredOrderClause.getOrderEntityType());
               orderEntities.add(orderEntity);
            }
         }
      }
      return orderEntities;
   }

   public QueryGenerationServiceFactory getQueryGenerationServiceFactory () {
      return queryGenerationServiceFactory;
   }

   public void setQueryGenerationServiceFactory (QueryGenerationServiceFactory queryGenerationServiceFactory) {
      this.queryGenerationServiceFactory = queryGenerationServiceFactory;
   }

   public ICubeQueryEnhancerService getCubeQueryEnhancerService () {
      return cubeQueryEnhancerService;
   }

   public void setCubeQueryEnhancerService (ICubeQueryEnhancerService cubeQueryEnhancerService) {
      this.cubeQueryEnhancerService = cubeQueryEnhancerService;
   }

}
