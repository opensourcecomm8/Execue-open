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


package com.execue.governor.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.HierarchyTerm;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.governor.AssetQuery;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.governor.EntityMapping;
import com.execue.core.common.bean.governor.EntityMappingInfo;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.governor.exception.GovernorException;
import com.execue.governor.helper.GovernorServiceHelper;
import com.execue.governor.service.IAssetQueryGenerationService;
import com.execue.governor.service.IGovernorPopularityHitService;
import com.execue.governor.service.IGovernorService;
import com.execue.governor.service.IQueryFederationService;
import com.execue.governor.service.IQueryMappingService;
import com.execue.governor.service.IStructuredQueryPopulationService;
import com.execue.governor.service.IStructuredQueryWeightCalculationService;

/**
 * This class contain method for extracting the AssetQueries from businessQuery.
 * 
 * @author Raju Gottumukkala
 * @version 1.0
 * @since 10/02/09
 */
public class GovernorServiceImpl implements IGovernorService {

   private static final Logger                      log = Logger.getLogger(GovernorServiceImpl.class);

   private IQueryMappingService                     queryMappingService;
   private IQueryFederationService                  queryFederationService;
   private IAssetQueryGenerationService             assetQueryGenerationService;
   private IStructuredQueryPopulationService        structuredQueryPopulationService;
   private IStructuredQueryWeightCalculationService structuredQueryWeightCalculationService;
   private IGovernorPopularityHitService            governorPopularityHitService;

   /**
    * This method is used to extract List of Asset Queries from BusinessQuery. It uses
    * governorStructuredQueryPopulationService to populate Unique BusinessEntityTerms. Then it uses queryMappingService
    * in order to find mappings from swi for each of the unique business terms. Then it corrects the mappings using
    * queryMappingService and find the unique assets which can answer the business query. It again uses the
    * governorStructuredQueryPopulationService to create structured queries for each of the unique assets. After it got
    * the structured queries, it goes to federation in order to decide which of the structured queries to drop because
    * they doesn't reflect the correct picture of business query. It uses governorStructuredQueryPopulationService again
    * to assign weights to each businessTerm and calculate the weight of the business Query. It used
    * governorStructuredQueryPopulationService to calculate the weight for each structured query.After that it uses
    * AssetQueryGenerationService to generate as many as asset queries as structured queries.
    * 
    * @param businessQuery
    * @return assetQueries
    * @throws GovernorException
    */
   public List<AssetQuery> extractDataAssetQueries (BusinessQuery businessQuery) throws GovernorException {

      // assign the weights to business query
      long startTime = System.currentTimeMillis();
      structuredQueryWeightCalculationService.assignBusinessQueryWeight(businessQuery);
      long endTime = System.currentTimeMillis();
      if (log.isDebugEnabled()) {
         log.debug("time taken to Assign Business Query Weight " + (endTime - startTime) / 1000.0 + " seconds");
      }
      startTime = System.currentTimeMillis();
      Set<BusinessEntityTerm> businessEntityTerms = GovernorServiceHelper.populateBusinessEntityTerms(businessQuery);

      // adding scaling factor business entity term to list of entity terms for which we need to extract EntityMappings
      businessEntityTerms.add(structuredQueryPopulationService.createScalingFactorBusinessTerm()
               .getBusinessEntityTerm());

      structuredQueryPopulationService.loadPopulatedBusinessEntityTerms(businessEntityTerms);
      List<EntityMapping> entityMappings = queryMappingService.extractEntityMappings(businessEntityTerms, businessQuery
               .getModelId());

      List<EntityMappingInfo> entityMappingInfo = new ArrayList<EntityMappingInfo>();
      Set<Asset> activeAnswerAssets = queryMappingService.correctMappings(entityMappings, entityMappingInfo,
               businessQuery);

      structuredQueryPopulationService.correctAndLoadEntityMappingInfo(entityMappingInfo, activeAnswerAssets);
      endTime = System.currentTimeMillis();
      if (log.isDebugEnabled()) {
         log.debug("time taken to extract and correct Entity Mappings " + (endTime - startTime) / 1000.0 + " seconds");
      }
      startTime = System.currentTimeMillis();

      List<StructuredQuery> structuredQueries = structuredQueryPopulationService.populateStructuredQueries(
               activeAnswerAssets, businessQuery, entityMappingInfo);
      if (CollectionUtils.isEmpty(structuredQueries)) {
         return new ArrayList<AssetQuery>();
      }
      endTime = System.currentTimeMillis();
      if (log.isDebugEnabled()) {
         log.debug("time taken to generate Structure Queries " + (endTime - startTime) / 1000.0 + " seconds");
      }
      startTime = System.currentTimeMillis();
      // assign weights to structured query on basis of business query weight
      structuredQueryWeightCalculationService.assignStructuredQueryWeight(structuredQueries);

      if (log.isDebugEnabled()) {
         log.debug("Initial Weights as double ");
         for (StructuredQuery sq : structuredQueries) {
            log.debug("Match Weights : " + sq.getAsset().getDisplayName() + "" + sq.getStructuredQueryWeight());
         }
      }

      // take off the structured queries on basis of cohort section present
      structuredQueries = queryFederationService.federateByCohortSection(businessQuery, structuredQueries);

      if (log.isDebugEnabled()) {
         log.debug("Weights after cohort based federation ..");
         for (StructuredQuery sq : structuredQueries) {
            log.debug("Match Percentage : " + sq.getAsset().getDisplayName() + "" + sq.getStructuredQueryWeight());
         }
      }

      // take off the structured queries on basis of limit section present
      structuredQueries = queryFederationService.federateByLimitSection(businessQuery, structuredQueries);

      if (log.isDebugEnabled()) {
         log.debug("Weights after top/bottom based federation ..");
         for (StructuredQuery sq : structuredQueries) {
            log.debug("Match Percentage : " + sq.getAsset().getDisplayName() + "" + sq.getStructuredQueryWeight());
         }
      }

      structuredQueries = queryFederationService.federateByWeight(businessQuery, structuredQueries);

      if (log.isDebugEnabled()) {
         log.debug("BQ Weight : " + businessQuery.getBusinessQueryWeight());
         log.debug("Initial Weights ..");
         for (StructuredQuery sq : structuredQueries) {
            log.debug("Match Percentage : " + sq.getAsset().getDisplayName() + " " + sq.getAsset().getId() + " "
                     + sq.getStructuredQueryWeight());
         }
      }

      structuredQueries = queryFederationService.federateByPriority(businessQuery, structuredQueries);

      if (log.isDebugEnabled()) {
         log.debug("Weights after priority based federation ..");
         for (StructuredQuery sq : structuredQueries) {
            log.debug("Match Percentage : " + sq.getAsset().getDisplayName() + " " + sq.getAsset().getId() + " "
                     + sq.getStructuredQueryWeight());
         }
      }

      structuredQueries = queryFederationService.calculateRelativePercentageWeights(businessQuery, structuredQueries);
      if (log.isDebugEnabled()) {
         log.debug("Weights after relative weights calculaton ..");
         for (StructuredQuery sq : structuredQueries) {
            log.debug("Match Percentage : " + sq.getAsset().getDisplayName() + " " + sq.getAsset().getId() + " "
                     + sq.getStructuredQueryWeight());
         }
      }
      if (log.isDebugEnabled()) {
         endTime = System.currentTimeMillis();
         log.debug("time taken to fedrate the Queries " + (endTime - startTime) / 1000.0 + " seconds");
      }

      startTime = System.currentTimeMillis();
      // update the popularity count for business and asset terms from business query and structured queries
      governorPopularityHitService.updatePopularityCounts(businessQuery, structuredQueries);
      if (log.isDebugEnabled()) {
         endTime = System.currentTimeMillis();
         log.debug("time taken to update the popularity counts " + (endTime - startTime) / 1000.0 + " seconds");
      }
      startTime = System.currentTimeMillis();
      List<AssetQuery> assetQueries = new ArrayList<AssetQuery>();
      for (StructuredQuery structuredQuery : structuredQueries) {
         assetQueries.add(assetQueryGenerationService.generateAssetQuery(structuredQuery));
      }
      if (log.isDebugEnabled()) {
         endTime = System.currentTimeMillis();
         log.debug("time taken to generate asset queries " + (endTime - startTime) / 1000.0 + " seconds");
      }
      return assetQueries;
   }

   public List<AssetQuery> extractDataAssetQueries2 (BusinessQuery businessQuery, Set<Asset> answerAssets,
            Set<BusinessEntityTerm> businessEntityTerms) throws GovernorException {

      // assign the weights to business query
      long startTime = System.currentTimeMillis();
      getStructuredQueryWeightCalculationService().assignBusinessQueryWeight(businessQuery);
      long endTime = System.currentTimeMillis();
      if (log.isDebugEnabled()) {
         log.debug("time taken to Assign Business Query Weight " + (endTime - startTime) / 1000.0 + " seconds");
      }
      startTime = System.currentTimeMillis();

      // Populate BusnessEntityTerms for the Hierarchies.
      populateBusinessEntityTermsForHierarchies(businessEntityTerms, businessQuery.getHierarchies());

      // adding scaling factor business entity term to list of entity terms for which we need to extract EntityMappings
      businessEntityTerms.add(getStructuredQueryPopulationService().createScalingFactorBusinessTerm()
               .getBusinessEntityTerm());

      getStructuredQueryPopulationService().loadPopulatedBusinessEntityTerms(businessEntityTerms);

      List<EntityMapping> entityMappings = getQueryMappingService().extractEntityMappings(businessEntityTerms,
               businessQuery.getModelId(), getAssetIds(answerAssets));

      List<EntityMappingInfo> entityMappingInfo = GovernorServiceHelper.prepareEntityMappingInfo(entityMappings);

      getStructuredQueryPopulationService().correctAndLoadEntityMappingInfo2(entityMappingInfo, answerAssets);
      if (log.isDebugEnabled()) {
         endTime = System.currentTimeMillis();
         log.debug("time taken to extract and correct Entity Mappings " + (endTime - startTime) / 1000.0 + " seconds");
      }

      // Populate the initial structured queries
      startTime = System.currentTimeMillis();
      List<StructuredQuery> structuredQueries = getStructuredQueryPopulationService().populateStructuredQueries(
               answerAssets, businessQuery, entityMappingInfo);
      if (CollectionUtils.isEmpty(structuredQueries)) {
         return new ArrayList<AssetQuery>();
      }
      if (log.isDebugEnabled()) {
         endTime = System.currentTimeMillis();
         log.debug("time taken to generate Structure Queries " + (endTime - startTime) / 1000.0 + " seconds");
      }

      // Assign weights to structured query on basis of business query weight
      startTime = System.currentTimeMillis();
      getStructuredQueryWeightCalculationService().assignStructuredQueryWeight(structuredQueries);
      if (log.isDebugEnabled()) {
         log.debug("Initial Weights as double ");
         for (StructuredQuery sq : structuredQueries) {
            log.debug("Match Weights : " + sq.getAsset().getDisplayName() + "" + sq.getStructuredQueryWeight());
         }
      }

      // Calculate the percentile for structured queries based on their weights in comparison to business query
      structuredQueries = getQueryFederationService().calculateRelativePercentageWeights(businessQuery,
               structuredQueries);
      if (log.isDebugEnabled()) {
         log.debug("Weights after relative weights calculaton ..");
         for (StructuredQuery sq : structuredQueries) {
            log.debug("Match Percentage : " + sq.getAsset().getDisplayName() + " " + sq.getAsset().getId() + " "
                     + sq.getStructuredQueryWeight());
         }
      }
      if (log.isDebugEnabled()) {
         endTime = System.currentTimeMillis();
         log.debug("time taken to fedrate the Queries " + (endTime - startTime) / 1000.0 + " seconds");
      }

      // Update the popularity count for business and asset terms from business query and structured queries
      startTime = System.currentTimeMillis();
      getGovernorPopularityHitService().updatePopularityCounts(businessQuery, structuredQueries);
      if (log.isDebugEnabled()) {
         endTime = System.currentTimeMillis();
         log.debug("time taken to update the popularity counts " + (endTime - startTime) / 1000.0 + " seconds");
      }

      // Generate the asset queries out of structured queries
      startTime = System.currentTimeMillis();
      List<AssetQuery> assetQueries = new ArrayList<AssetQuery>();
      for (StructuredQuery structuredQuery : structuredQueries) {
         assetQueries.add(getAssetQueryGenerationService().generateAssetQuery(structuredQuery));
      }
      if (log.isDebugEnabled()) {
         endTime = System.currentTimeMillis();
         log.debug("time taken to generate asset queries " + (endTime - startTime) / 1000.0 + " seconds");
      }
      return assetQueries;
   }

   private void populateBusinessEntityTermsForHierarchies (Set<BusinessEntityTerm> businessEntityTerms,
            List<HierarchyTerm> hierarchyTerms) {
      if (ExecueCoreUtil.isCollectionNotEmpty(hierarchyTerms)) {
         for (HierarchyTerm hierarchyTerm : hierarchyTerms) {
            for (BusinessTerm businessTerm : hierarchyTerm.getHierarchyBusinessDefinition()) {
               businessEntityTerms.add(businessTerm.getBusinessEntityTerm());
            }
         }
      }
   }

   private List<Long> getAssetIds (Set<Asset> answerAssets) {
      List<Long> assetIds = new ArrayList<Long>();
      for (Asset asset : answerAssets) {
         assetIds.add(asset.getId());
      }
      return assetIds;
   }

   public Set<BusinessEntityTerm> populateBusinessEntityTerms (BusinessQuery businessQuery) throws GovernorException {
      return GovernorServiceHelper.populateBusinessEntityTerms(businessQuery);
   }

   public IStructuredQueryPopulationService getGovernorStructuredQueryPopulationService () {
      return structuredQueryPopulationService;
   }

   public void setStructuredQueryPopulationService (
            IStructuredQueryPopulationService governorStructuredQueryPopulationService) {
      this.structuredQueryPopulationService = governorStructuredQueryPopulationService;
   }

   public IStructuredQueryWeightCalculationService getStructuredQueryWeightCalculationService () {
      return structuredQueryWeightCalculationService;
   }

   public void setStructuredQueryWeightCalculationService (
            IStructuredQueryWeightCalculationService structuredQueryWeightCalculationService) {
      this.structuredQueryWeightCalculationService = structuredQueryWeightCalculationService;
   }

   public IStructuredQueryPopulationService getStructuredQueryPopulationService () {
      return structuredQueryPopulationService;
   }

   public IQueryMappingService getQueryMappingService () {
      return queryMappingService;
   }

   public void setQueryMappingService (IQueryMappingService queryMappingService) {
      this.queryMappingService = queryMappingService;
   }

   public IQueryFederationService getQueryFederationService () {
      return queryFederationService;
   }

   public void setQueryFederationService (IQueryFederationService queryFederationService) {
      this.queryFederationService = queryFederationService;
   }

   public IAssetQueryGenerationService getAssetQueryGenerationService () {
      return assetQueryGenerationService;
   }

   public void setAssetQueryGenerationService (IAssetQueryGenerationService assetQueryGenerationService) {
      this.assetQueryGenerationService = assetQueryGenerationService;
   }

   public IGovernorPopularityHitService getGovernorPopularityHitService () {
      return governorPopularityHitService;
   }

   public void setGovernorPopularityHitService (IGovernorPopularityHitService governorPopularityHitService) {
      this.governorPopularityHitService = governorPopularityHitService;
   }
}
