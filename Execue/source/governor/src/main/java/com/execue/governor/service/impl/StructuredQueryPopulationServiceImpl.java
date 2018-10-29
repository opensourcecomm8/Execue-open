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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.BusinessCondition;
import com.execue.core.common.bean.BusinessLimitClause;
import com.execue.core.common.bean.BusinessOrderClause;
import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.BusinessStat;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.HierarchyTerm;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Conversion;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.bean.governor.AssetEntityTerm;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.governor.EntityMappingInfo;
import com.execue.core.common.bean.governor.LightAssetEntityDefinitionInfo;
import com.execue.core.common.bean.governor.StructuredCondition;
import com.execue.core.common.bean.governor.StructuredLimitClause;
import com.execue.core.common.bean.governor.StructuredOrderClause;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.type.AssetEntityType;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConstraintSubType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.OperandType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.OrderEntityType;
import com.execue.core.common.type.OrderLimitEntityType;
import com.execue.core.common.type.QuerySectionType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.AssetGrainUtils;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.governor.configuration.IGovernorConfigurationService;
import com.execue.governor.exception.GovernorException;
import com.execue.governor.exception.GovernorExceptionCodes;
import com.execue.governor.helper.GovernorServiceHelper;
import com.execue.governor.service.IGovernorTimeFrameHandlerService;
import com.execue.governor.service.IStructuredQueryEntityFilterationService;
import com.execue.governor.service.IStructuredQueryPopulationService;
import com.execue.platform.swi.conversion.unitscale.ITypeConvertor;
import com.execue.platform.swi.conversion.unitscale.TypeConvertorFactory;
import com.execue.platform.swi.conversion.unitscale.exception.UnitScaleConversionException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IConversionService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ILookupService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * This service populates the structured queries for business query
 * 
 * @author Vishay Gupta
 * @version 1.0
 * @since 24/08/10
 */
public class StructuredQueryPopulationServiceImpl implements IStructuredQueryPopulationService {

   private static final Logger                      logger = Logger
                                                                    .getLogger(StructuredQueryPopulationServiceImpl.class);

   private IKDXRetrievalService                     kdxRetrievalService;

   private IBaseKDXRetrievalService                 baseKDXRetrievalService;

   private IMappingRetrievalService                 mappingRetrievalService;

   private IGovernorConfigurationService            governorConfigurationService;

   private ICoreConfigurationService                coreConfigurationService;

   private ISDXRetrievalService                     sdxRetrievalService;

   private TypeConvertorFactory                     typeConvertorFactory;

   private IConversionService                       conversionService;

   private ILookupService                           lookupService;

   private IGovernorTimeFrameHandlerService         governorTimeFrameHandlerService;

   private IStructuredQueryEntityFilterationService structuredQueryEntityFilterationService;

   public void loadPopulatedBusinessEntityTerms (Set<BusinessEntityTerm> businessEntityTerms) throws GovernorException {
      try {
         for (BusinessEntityTerm businessEntityTerm : businessEntityTerms) {
            if (BusinessEntityType.CONCEPT.equals(businessEntityTerm.getBusinessEntityType())) {
               Concept concept = (Concept) businessEntityTerm.getBusinessEntity();
               Concept populatedConcept = getKdxRetrievalService().getPopulatedConceptWithStats(concept.getId());
               businessEntityTerm.setBusinessEntity(populatedConcept);
            }
         }
      } catch (KDXException kdxException) {
         throw new GovernorException(GovernorExceptionCodes.DEFAULT_SYSTEM_EXCEPTION_CODE, kdxException);
      }
   }

   /**
    * This method will create multiple structured Queries from business query each per asset passed. The structured
    * Query is per Asset, so it might or might not contain all the businessTerms because the asset might not have
    * mappings to all the business terms present in business query. If asset doesn't have any businessTerm in metrics
    * section, the structured query won't be created for that asset. If asset has mappings for the businessTerm on left
    * hand side of condition but not for the right hand side, it wont put the left side also in structured query. If
    * subquery doesn't have anything in metrics section, it wont come in final structured query. It will use
    * entityMappings passed to it in order to find mappings for businessTerms per Asset. If there is no mapping exist
    * for some businessTerm, it won't put that businessTerm into structured query. In the end we will get List<StructuredQuery>
    * which can run on asset defined inside structuredQuery.
    * 
    * @param answerAssets
    * @param businessQuery
    * @param entityMappings
    * @return structuredQueries
    */
   public List<StructuredQuery> populateStructuredQueries (Set<Asset> answerAssets, BusinessQuery businessQuery,
            List<EntityMappingInfo> entityMappingInfo) throws GovernorException {
      logger.debug("Inside the populateStructuredQueries method");
      logger.debug("Got the assets for which we need to prepare the Structured Queries " + answerAssets);
      logger.debug("Got the business Query : " + businessQuery);
      List<StructuredQuery> structuredQueries = new ArrayList<StructuredQuery>();
      try {
         long startTime = System.currentTimeMillis();
         for (Asset asset : answerAssets) {
            logger.debug("Calling the populateStructuredQuery method for asset " + asset.getName());
            StructuredQuery structuredQuery = populateStructuredQuery(asset, businessQuery, entityMappingInfo);
            if (structuredQuery != null) {
               structuredQuery.setModelId(businessQuery.getModelId());
               structuredQueries.add(structuredQuery);
            }
         }
         long endTime = System.currentTimeMillis();
         logger.debug("time taken to populateStructuredQuery for all answerAssets " + (endTime - startTime) / 1000.0
                  + " seconds");
         logger.debug("Size of Structured Queries generated " + structuredQueries.size());
         // applying defaults on structured query and populate cohort query
         // for structured queries except cube. For mart
         // queries, scaling factor has to be added. For Cube queries, we
         // need to modify the query to make it cube
         // compatible
         startTime = System.currentTimeMillis();
         for (StructuredQuery structuredQuery : structuredQueries) {
            // handle time frame conditions in each SQ.
            if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getConditions())) {
               for (StructuredCondition structuredCondition : structuredQuery.getConditions()) {
                  // it means because of time frame handling governor picked different term in the lhs.
                  // we need to remove the original term from metrics and add new term to metric.
                  // removal of original term has to be checked based on if it is in metrics just
                  // because of CONDITION section addition and nothing else.
                  // second point is it should not be requested by user himself as a metric.
                  if (structuredCondition.getOriginalLhsBusinessTerm() != null) {
                     removeBusinessTermFromMetrics(structuredQuery.getMetrics(), structuredCondition
                              .getOriginalLhsBusinessTerm(), QuerySectionType.CONDITION);
                     GovernorServiceHelper.addUpdateBusinessAssetTermToMetrics(structuredQuery.getMetrics(),
                              structuredCondition.getLhsBusinessAssetTerm(), false, false);
                  }
               }
            }

            boolean isExecueOwnedMart = ExecueBeanUtil.isExecueOwnedMart(structuredQuery.getAsset());
            boolean isExecueOwnedCube = ExecueBeanUtil.isExecueOwnedCube(structuredQuery.getAsset());
            long startTime2 = System.currentTimeMillis();
            if (getGovernorConfigurationService().isApplyDefaults()) {
               applyDefaults(structuredQuery);
            }
            if (getGovernorConfigurationService().isApplySecurityFilters()) {
               // Filter the StructureQuery on the basis of security permission
               // If the filtered StructureQuery is null then we need not process further.
               structuredQuery = getStructuredQueryEntityFilterationService().filterStructuredQueryBySecurity(
                        structuredQuery);
            }
            // return an empty list of StructureQuery if the filtered StructureQuery is null.
            if (structuredQuery == null) {
               return new ArrayList<StructuredQuery>();
            } else {
               long endTime2 = System.currentTimeMillis();
               logger.debug("time taken to applyDefaults " + (endTime2 - startTime2) / 1000.0 + " seconds");
               startTime2 = System.currentTimeMillis();
               if (isExecueOwnedMart) {
                  logger.debug("populating the scaling factor for mart strcutured query " + structuredQuery);
                  populateScalingFactor(structuredQuery, entityMappingInfo);
               }
               endTime2 = System.currentTimeMillis();
               logger.debug("time taken to populateScalingFactor " + (endTime2 - startTime2) / 1000.0 + " seconds");
               if (isExecueOwnedCube) {
                  logger.debug("Modifying the structured query for cube " + structuredQuery);
                  startTime2 = System.currentTimeMillis();
                  modifyQueryForCube(structuredQuery);
                  endTime2 = System.currentTimeMillis();
                  logger.debug("time taken to modifyQueryForCube " + (endTime2 - startTime2) / 1000.0 + " seconds");

               } else {
                  logger.debug("Populating the cohort section for structured query " + structuredQuery);
                  // if population is there in structured query either user
                  // asked or defaults added and cohort query asked
                  // by user in business query, then go for population of
                  // cohort query

                  populateCohortQuery(structuredQuery, businessQuery, entityMappingInfo);
                  endTime2 = System.currentTimeMillis();
                  logger.debug("time taken to populateCohortQuery " + (endTime2 - startTime2) / 1000.0 + " seconds");

               }
            }
            endTime = System.currentTimeMillis();
            logger.debug("time taken to applyDefaults populateScalingFactor modifyQueryForCube populateCohortQuery  "
                     + (endTime - startTime) / 1000.0 + " seconds");
         }
      } catch (UnitScaleConversionException conversionException) {
         throw new GovernorException(GovernorExceptionCodes.DEFAULT_SYSTEM_EXCEPTION_CODE, conversionException);
      } catch (SWIException swiException) {
         throw new GovernorException(GovernorExceptionCodes.DEFAULT_SYSTEM_EXCEPTION_CODE, swiException);
      }
      return structuredQueries;
   }

   private void removeBusinessTermFromMetrics (List<BusinessAssetTerm> metrics, BusinessTerm originalLhsBusinessTerm,
            QuerySectionType querySectionType) {
      BusinessAssetTerm matchedBusinessAssetTerm = GovernorServiceHelper.getMatchedBusinessAssetTerm(
               originalLhsBusinessTerm, metrics);
      if (matchedBusinessAssetTerm != null) {
         // need to check if this is added to metrics only because of condition
         // second point is it should not be requested by user himself as a metric.
         List<QuerySectionType> querySectionTypes = matchedBusinessAssetTerm.getBusinessTerm().getQuerySectionTypes();
         if (ExecueCoreUtil.isCollectionNotEmpty(querySectionTypes) && querySectionTypes.size() == 1
                  && querySectionTypes.get(0).equals(QuerySectionType.CONDITION)
                  && !matchedBusinessAssetTerm.getBusinessTerm().isRequestedByUser()) {
            metrics.remove(matchedBusinessAssetTerm);
         }
      }
   }

   /**
    * This method populated the cohort query in the structured query from business query
    * 
    * @param structuredQuery
    * @param businessQuery
    * @param entityMappings
    */
   private void populateCohortQuery (StructuredQuery structuredQuery, BusinessQuery businessQuery,
            List<EntityMappingInfo> entityMappings) throws UnitScaleConversionException, SWIException,
            GovernorException {
      boolean isValidCohort = false;
      // if valid cohort exists and population is there, then try to populate
      // the cohort query
      if (isValidCohortExists(businessQuery) && ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getPopulations())) {
         StructuredQuery cohortQuery = populateStructuredQuery(structuredQuery.getAsset(), businessQuery.getCohort(),
                  entityMappings);
         if (isValidCohortExists(cohortQuery)) {
            isValidCohort = true;
            structuredQuery.setCohort(cohortQuery);
            // add popualtion to the cohort query metrics
            addPopulationToCohortQuery(structuredQuery);
         }
      }
      // if valid cohort doesn't exist we need to take off its traces from
      // main query metrics
      if (!isValidCohort) {
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getMetrics())) {
            List<BusinessAssetTerm> queryMetricsWithOutCohort = new ArrayList<BusinessAssetTerm>();
            for (BusinessAssetTerm businessAssetTerm : structuredQuery.getMetrics()) {
               if (!businessAssetTerm.getBusinessTerm().isFromCohort()) {
                  queryMetricsWithOutCohort.add(businessAssetTerm);
               }
            }
            structuredQuery.setMetrics(queryMetricsWithOutCohort);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getSummarizations())) {
            List<BusinessAssetTerm> querySummarizationsWithOutCohort = new ArrayList<BusinessAssetTerm>();
            for (BusinessAssetTerm businessAssetTerm : structuredQuery.getSummarizations()) {
               if (!businessAssetTerm.getBusinessTerm().isFromCohort()) {
                  querySummarizationsWithOutCohort.add(businessAssetTerm);
               }
            }
            structuredQuery.setSummarizations(querySummarizationsWithOutCohort);
         }
      }
   }

   /**
    * This method will put the default population to the structured query cohort section
    * 
    * @param structuredQuery
    */
   private void addPopulationToCohortQuery (StructuredQuery structuredQuery) {
      BusinessAssetTerm populationBusinessAssetTerm = structuredQuery.getPopulations().get(0);
      List<BusinessAssetTerm> cohortMetrics = structuredQuery.getCohort().getMetrics();
      if (ExecueCoreUtil.isCollectionEmpty(cohortMetrics)) {
         cohortMetrics = new ArrayList<BusinessAssetTerm>();
      }
      BusinessTerm clonedBusinessTerm = ExecueBeanCloneUtil.cloneBusinessTerm(populationBusinessAssetTerm
               .getBusinessTerm());
      clonedBusinessTerm.setFromPopulation(true);
      BusinessAssetTerm businessAssetTerm = new BusinessAssetTerm();
      businessAssetTerm.setBusinessTerm(clonedBusinessTerm);
      businessAssetTerm.setAssetEntityTerm(populationBusinessAssetTerm.getAssetEntityTerm());
      cohortMetrics.add(businessAssetTerm);
      structuredQuery.getCohort().setMetrics(cohortMetrics);
   }

   /**
    * This method checks whether the valid cohort query exists in business query
    * 
    * @param businessQuery
    * @return boolean value
    */
   private boolean isValidCohortExists (BusinessQuery businessQuery) {
      boolean isValidCohortExists = false;
      BusinessQuery cohortQuery = businessQuery.getCohort();
      if (cohortQuery != null && ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getConditions())) {
         isValidCohortExists = true;
      }
      return isValidCohortExists;
   }

   /**
    * This method checks whether the valid cohort query exists in structured query
    * 
    * @param structuredQuery
    * @return boolean value
    */
   private boolean isValidCohortExists (StructuredQuery cohortQuery) {
      boolean isValidCohortExists = false;
      if (cohortQuery != null && ExecueCoreUtil.isCollectionNotEmpty(cohortQuery.getConditions())) {
         isValidCohortExists = true;
      }
      return isValidCohortExists;
   }

   /**
    * This method will apply defaults to the structured query
    * 
    * @param structuredQuery
    * @throws GovernorException
    */
   private void applyDefaults (StructuredQuery structuredQuery) throws GovernorException {
      Asset asset = structuredQuery.getAsset();
      try {
         long startTime = System.currentTimeMillis();
         List<Mapping> assetGrain = getMappingRetrievalService().getAssetGrain(asset.getId());
         long endTime = System.currentTimeMillis();
         logger.debug("time taken to getAssetGrain " + (endTime - startTime) / 1000.0 + " seconds");

         Mapping defaultDistributionConcept = AssetGrainUtils.getDefaultDistributionConcept(assetGrain);
         Mapping defaultPopulationConcept = AssetGrainUtils.getDefaultPopulationConcept(assetGrain);
         // if both the DDC and DPC not defined, then get the grain at column granularity level.
         if (defaultDistributionConcept == null && defaultPopulationConcept == null) {
            logger.debug("Asset Grain is not defined for asset " + asset.getName());
            // get the asset grain by column granularity.
            List<Mapping> assetGrainByColumnGranularity = getMappingRetrievalService()
                     .getAssetGrainByColumnGranularity(asset.getId());
            if (ExecueCoreUtil.isCollectionNotEmpty(assetGrainByColumnGranularity)) {
               logger.debug("Got Asset grain defined at colum granularity level "
                        + assetGrainByColumnGranularity.size());
               for (Mapping mapping : assetGrainByColumnGranularity) {
                  BusinessAssetTerm columnGranularity = GovernorServiceHelper.prepareBusinessAssetTerm(mapping);
                  GovernorServiceHelper.addUpdateBusinessAssetTermToMetrics(structuredQuery.getMetrics(),
                           columnGranularity, false, false);
               }
            }
         } else {
            // if there are defaults defined then apply defaults on population,
            // where clause
            startTime = System.currentTimeMillis();
            if (ExecueCoreUtil.isCollectionNotEmpty(assetGrain)) {
               logger.debug("Got Grain Elements" + assetGrain.size());
               if (ExecueCoreUtil.isCollectionEmpty(structuredQuery.getPopulations())) {
                  applyPopulationDefaults(structuredQuery, assetGrain);
               }
               applyConditionalDefaults(structuredQuery, assetGrain);
            }
            endTime = System.currentTimeMillis();
            logger.debug("time taken to applyPopulationDefaults and applyConditionalDefaults " + (endTime - startTime)
                     / 1000.0 + " seconds");
         }
         // applying default metrics.
         startTime = System.currentTimeMillis();
         if (!isMeasureExists(structuredQuery.getMetrics(), structuredQuery.getModelId())) {
            Integer maxMetricsPerQuery = getGovernorConfigurationService().getMaxDefaultMetricsPerQuery();
            List<Long> tableIds = findUniqueFactTables(structuredQuery.getMetrics(), structuredQuery.getAsset().getId());
            List<Mapping> defaultMetricMappings = null;
            if (ExecueCoreUtil.isCollectionNotEmpty(tableIds)) {
               defaultMetricMappings = getMappingRetrievalService().getExistingDefaultMetrics(tableIds,
                        maxMetricsPerQuery);
            }
            // if we got no default metrics at table level, try to get at asset level
            if (ExecueCoreUtil.isCollectionEmpty(defaultMetricMappings)) {
               defaultMetricMappings = getMappingRetrievalService().getAssetDefaultMetrics(asset.getId(),
                        maxMetricsPerQuery);
            }
            if (ExecueCoreUtil.isCollectionNotEmpty(defaultMetricMappings)) {
               List<BusinessAssetTerm> defaultMetrics = new ArrayList<BusinessAssetTerm>();
               for (Mapping defaultMetricMapping : defaultMetricMappings) {
                  BusinessAssetTerm defaultMetricBusinessAssetTerm = GovernorServiceHelper
                           .prepareBusinessAssetTerm(defaultMetricMapping);
                  defaultMetricBusinessAssetTerm.getBusinessTerm().setRequestedByUser(false);
                  defaultMetrics.add(defaultMetricBusinessAssetTerm);
               }
               structuredQuery.getMetrics().addAll(defaultMetrics);
            }
         }
         endTime = System.currentTimeMillis();
         logger.debug("time taken to defaultMetricMappings " + (endTime - startTime) / 1000.0 + " seconds");
      } catch (MappingException e) {
         throw new GovernorException(GovernorExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      } catch (SDXException e) {
         throw new GovernorException(GovernorExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      } catch (KDXException e) {
         throw new GovernorException(GovernorExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   private List<Long> findUniqueFactTables (List<BusinessAssetTerm> metrics, Long assetId) throws MappingException {
      List<Long> factTables = new ArrayList<Long>();
      List<Long> businessEntityDefinitionIds = new ArrayList<Long>();
      for (BusinessAssetTerm businessAssetTerm : metrics) {
         businessEntityDefinitionIds.add(businessAssetTerm.getBusinessTerm().getBusinessEntityTerm()
                  .getBusinessEntityDefinitionId());
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(businessEntityDefinitionIds)) {
         factTables = getMappingRetrievalService().getDistinctFactTablesForBEDIdsByNonPrimaryMappings(
                  businessEntityDefinitionIds, assetId);
      }
      return factTables;
   }

   /**
    * Check if the metrics in the participating query does not contain any thing other than Population Concept, Concept
    * from Grain, Concept from Distributions and Concept with out any instances. If not return true, that means measure
    * exists already in query The check would have been as simple as make sure the column KDX data type is not measure
    * on any metrics. But this interfere with record level queries.
    * 
    * @param metrics
    * @param modelId
    * @return
    * @throws KDXException
    */
   private boolean isMeasureExists (List<BusinessAssetTerm> metrics, Long modelId) throws KDXException {
      boolean isMeasureExists = false;
      if (ExecueCoreUtil.isCollectionNotEmpty(metrics)) {
         for (BusinessAssetTerm businessAssetTerm : metrics) {
            if (BusinessEntityType.CONCEPT.equals(businessAssetTerm.getBusinessTerm().getBusinessEntityTerm()
                     .getBusinessEntityType())) {
               Concept concept = (Concept) businessAssetTerm.getBusinessTerm().getBusinessEntityTerm()
                        .getBusinessEntity();
               if (kdxRetrievalService.isConceptMatchedBusinessEntityType(concept.getId(), modelId,
                        ExecueConstants.MEASURABLE_ENTITY_TYPE)) {
                  isMeasureExists = true;
                  break;
               }
            }
         }
      }
      return isMeasureExists;
   }

   /**
    * This method will apply defaults to the where clause if there is no condition asked on distribution concepts. Pick
    * the default distribution concept and add a condition to the structured query
    * 
    * @param structuredQuery
    * @param assetGrain
    * @throws MappingException
    */
   private void applyConditionalDefaults (StructuredQuery structuredQuery, List<Mapping> assetGrain)
            throws SDXException, MappingException {
      Mapping defaultDistributionConcept = AssetGrainUtils.getDefaultDistributionConcept(assetGrain);
      if (ExecueCoreUtil.isCollectionEmpty(structuredQuery.getConditions())) {
         logger.debug("No conditions has asked by user");
         if (defaultDistributionConcept != null) {
            List<StructuredCondition> conditions = new ArrayList<StructuredCondition>();
            StructuredCondition structuredCondition = prepareStructuredCondition(defaultDistributionConcept,
                     structuredQuery.getAsset());
            if (structuredCondition != null) {
               BusinessAssetTerm businessAssetTerm = GovernorServiceHelper
                        .prepareBusinessAssetTerm(defaultDistributionConcept);
               logger.debug("Adding default distribution as new condition");
               conditions.add(structuredCondition);
               structuredQuery.setConditions(conditions);
               GovernorServiceHelper.addUpdateBusinessAssetTermToMetrics(structuredQuery.getMetrics(),
                        businessAssetTerm, false, true);
            }
         }
      } else {
         List<Mapping> totalDistributionConcepts = AssetGrainUtils.getDistributionConcepts(assetGrain);
         if (defaultDistributionConcept != null) {
            totalDistributionConcepts.add(defaultDistributionConcept);
         }
         List<BusinessAssetTerm> distributionBusinessAssetTerms = new ArrayList<BusinessAssetTerm>();
         logger.debug("Got distribution Concepts " + totalDistributionConcepts.size());
         for (Mapping mapping : totalDistributionConcepts) {
            distributionBusinessAssetTerms.add(GovernorServiceHelper.prepareBusinessAssetTerm(mapping));
         }
         List<BusinessAssetTerm> existingDistribution = new ArrayList<BusinessAssetTerm>();
         for (StructuredCondition structuredCondition : structuredQuery.getConditions()) {
            BusinessAssetTerm matchedBusinessAssetTerm = GovernorServiceHelper.getMatchedBusinessAssetTerm(
                     structuredCondition.getLhsBusinessAssetTerm(), distributionBusinessAssetTerms);
            if (matchedBusinessAssetTerm != null) {
               existingDistribution.add(matchedBusinessAssetTerm);
            }
         }
         if (ExecueCoreUtil.isCollectionEmpty(existingDistribution)) {
            logger.debug("Adding default distribution to conditions");
            if (defaultDistributionConcept != null) {
               StructuredCondition structuredCondition = prepareStructuredCondition(defaultDistributionConcept,
                        structuredQuery.getAsset());
               if (structuredCondition != null) {
                  structuredQuery.getConditions().add(structuredCondition);
               }
               BusinessAssetTerm defaultDistribution = GovernorServiceHelper
                        .prepareBusinessAssetTerm(defaultDistributionConcept);
               GovernorServiceHelper.addUpdateBusinessAssetTermToMetrics(structuredQuery.getMetrics(),
                        defaultDistribution, false, true);
            }
         } else {
            for (BusinessAssetTerm businessAssetTerm : existingDistribution) {
               GovernorServiceHelper.addUpdateBusinessAssetTermToMetrics(structuredQuery.getMetrics(),
                        businessAssetTerm, false, true);
            }
         }
      }
   }

   /**
    * This method will populate the structured condition from the mapping object which will get used while preparing
    * defaults for where clause
    * 
    * @param defaultDistributionConcept
    * @param asset
    * @return structuredCondition
    * @throws MappingException
    */
   private StructuredCondition prepareStructuredCondition (Mapping defaultDistributionConcept, Asset asset)
            throws SDXException, MappingException {
      StructuredCondition structuredCondition = null;
      BusinessAssetTerm defaultDistribution = GovernorServiceHelper
               .prepareBusinessAssetTerm(defaultDistributionConcept);
      String defaultValue = defaultDistributionConcept.getAssetEntityDefinition().getColum().getDefaultValue();
      if (ExecueCoreUtil.isNotEmpty(defaultValue)) {
         List<BusinessAssetTerm> rhsBusinessAssetTerms = new ArrayList<BusinessAssetTerm>();
         List<QueryValue> rhsQueryValues = new ArrayList<QueryValue>();
         OperatorType operatorType = getOperatorTypeAndPopulateRHSAssetTerms(defaultValue, rhsBusinessAssetTerms,
                  rhsQueryValues, defaultDistribution, asset);
         if (ExecueCoreUtil.isCollectionNotEmpty(rhsBusinessAssetTerms)) {
            structuredCondition = new StructuredCondition();
            structuredCondition.setLhsBusinessAssetTerm(defaultDistribution);
            structuredCondition.setOperandType(OperandType.BUSINESS_TERM);
            structuredCondition.setOperator(operatorType);
            structuredCondition.setRhsBusinessAssetTerms(rhsBusinessAssetTerms);
         }
      }
      return structuredCondition;
   }

   /**
    * This method will populate the defaults to the population section of structured query. Check if the central concept
    * is not there, then apply the default central concept as the population
    * 
    * @param structuredQuery
    * @param assetGrain
    */
   private void applyPopulationDefaults (StructuredQuery structuredQuery, List<Mapping> assetGrain) {
      Mapping defaultPopulationConcept = AssetGrainUtils.getDefaultPopulationConcept(assetGrain);
      if (defaultPopulationConcept != null) {
         if (ExecueCoreUtil.isCollectionEmpty(structuredQuery.getPopulations())) {
            logger.debug("Adding Default Population");
            List<BusinessAssetTerm> populations = new ArrayList<BusinessAssetTerm>();
            BusinessAssetTerm defaultPopulation = GovernorServiceHelper
                     .prepareBusinessAssetTerm(defaultPopulationConcept);
            populations.add(defaultPopulation);
            structuredQuery.setPopulations(populations);
            GovernorServiceHelper.addUpdateBusinessAssetTermToMetrics(structuredQuery.getMetrics(), defaultPopulation,
                     true, false);
         }
      }
   }

   /**
    * This method will read the default value and parse it and return the operator depends on type of the value
    * 
    * @param defaultValue
    * @param rhsQueryValues
    * @param queryValues
    * @param defaultDistribution
    * @return operatorType
    * @throws MappingException
    */
   private OperatorType getOperatorTypeAndPopulateRHSAssetTerms (String defaultValue,
            List<BusinessAssetTerm> rhsBusinessAssetTerms, List<QueryValue> rhsQueryValues,
            BusinessAssetTerm lhsBusinessAssetTerm, Asset asset) throws SDXException, MappingException {
      OperatorType operatorType = null;
      List<String> values = new ArrayList<String>();
      Colum lhsColumn = (Colum) lhsBusinessAssetTerm.getAssetEntityTerm().getAssetEntity();
      String rangeValueSeperator = getGovernorConfigurationService().getDefaultRangeValueSeprator();
      String multipleValuesSeperator = getGovernorConfigurationService().getDefaultMultipleValueSeprator();
      if (defaultValue.contains(rangeValueSeperator)) {
         operatorType = OperatorType.BETWEEN;
         StringTokenizer stringTokenizer = new StringTokenizer(defaultValue, rangeValueSeperator);
         while (stringTokenizer.hasMoreTokens()) {
            values.add(stringTokenizer.nextToken());
         }
      } else if (defaultValue.contains(multipleValuesSeperator)) {
         operatorType = OperatorType.IN;
         StringTokenizer stringTokenizer = new StringTokenizer(defaultValue, multipleValuesSeperator);
         while (stringTokenizer.hasMoreTokens()) {
            values.add(stringTokenizer.nextToken());
         }
      } else {
         operatorType = OperatorType.EQUALS;
         values.add(defaultValue);
      }
      if (ColumnType.DIMENSION.equals(lhsColumn.getKdxDataType())
               || ColumnType.SIMPLE_LOOKUP.equals(lhsColumn.getKdxDataType())) {
         List<Membr> members = getSdxRetrievalService().getMembersByLookupValues(asset, lhsColumn.getOwnerTable(),
                  lhsColumn, values);
         members = sortMembersByOrderOfValues(values, members);
         if (ExecueCoreUtil.isCollectionNotEmpty(members)) {
            for (Membr membr : members) {
               AssetEntityDefinition assetEntityDefinition = getSdxRetrievalService().getAssetEntityDefinitionByIds(
                        asset, lhsColumn.getOwnerTable(), lhsColumn, membr);
               List<Mapping> memberMappings = getMappingRetrievalService().getMappingsForAED(
                        assetEntityDefinition.getId());
               if (ExecueCoreUtil.isCollectionNotEmpty(memberMappings)) {
                  Mapping mapping = memberMappings.get(0);
                  BusinessEntityDefinition businessEntityDefinition = mapping.getBusinessEntityDefinition();
                  BusinessAssetTerm businessAssetTerm = new BusinessAssetTerm();
                  BusinessTerm businessTerm = new BusinessTerm();
                  BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
                  businessEntityTerm.setBusinessEntity(businessEntityDefinition.getInstance());
                  businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);
                  businessEntityTerm.setBusinessEntityDefinitionId(businessEntityDefinition.getId());
                  businessTerm.setBusinessEntityTerm(businessEntityTerm);
                  businessTerm.setRequestedByUser(false);
                  AssetEntityTerm assetEntityTerm = new AssetEntityTerm();
                  assetEntityTerm.setAssetEntity(assetEntityDefinition.getMembr());
                  assetEntityTerm.setAssetEntityType(AssetEntityType.MEMBER);
                  assetEntityTerm.setAssetEntityDefinitionId(assetEntityDefinition.getId());
                  businessAssetTerm.setBusinessTerm(businessTerm);
                  businessAssetTerm.setAssetEntityTerm(assetEntityTerm);
                  rhsBusinessAssetTerms.add(businessAssetTerm);
               }
            }
         }
         // based on how many mapped thing we found, operator will get
         // readjusted
         if (ExecueCoreUtil.isCollectionNotEmpty(rhsBusinessAssetTerms)) {
            if (OperatorType.BETWEEN.equals(operatorType)) {
               if (rhsBusinessAssetTerms.size() != 2) {
                  operatorType = OperatorType.EQUALS;
               }
            } else if (OperatorType.IN.equals(operatorType)) {
               if (rhsBusinessAssetTerms.size() == 1) {
                  operatorType = OperatorType.EQUALS;
               }
            }
         }
      } else {
         for (String value : values) {
            rhsQueryValues.add(buildQueryValue(asset, lhsColumn, value));
         }
      }
      return operatorType;
   }

   /**
    * @param lhsColumn
    * @param value
    * @return
    */
   private QueryValue buildQueryValue (Asset asset, Colum lhsColumn, String value) {
      QueryValue queryValue = new QueryValue();
      queryValue.setValue(value);
      queryValue.setDataType(lhsColumn.getDataType());
      return queryValue;
   }

   /**
    * This method will modify the structured query for execue owned cubes
    * 
    * @param structuredQuery
    */
   private void modifyQueryForCube (StructuredQuery structuredQuery) throws SDXException, GovernorException {
      // handle the case where concept is mapped to both measure and dimension
      // in cube and user asked the concept in
      // select section, the default column picked will be the dimension as
      // that is primary key. We have to override the
      // column picked with the measure column
      overrideMeasureColum(structuredQuery);
      // apply the stats on the all the measures. So this step has to be done
      // after step 2.
      applyMissingStats(structuredQuery);
      removeStatLessConditionsOnMeasures(structuredQuery);
      organizeCubeQuery(structuredQuery);
   }

   private void removeStatLessConditionsOnMeasures (StructuredQuery structuredQuery) {
      if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getConditions())) {
         List<StructuredCondition> statLessConditionsOnMeasures = new ArrayList<StructuredCondition>();
         for (StructuredCondition structuredCondition : structuredQuery.getConditions()) {
            Colum colum = (Colum) structuredCondition.getLhsBusinessAssetTerm().getAssetEntityTerm().getAssetEntity();
            if (ColumnType.MEASURE.equals(colum.getKdxDataType())) {
               if (structuredCondition.getLhsBusinessAssetTerm().getBusinessTerm().getBusinessStat() == null) {
                  statLessConditionsOnMeasures.add(structuredCondition);
               }
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(statLessConditionsOnMeasures)) {
            structuredQuery.getConditions().removeAll(statLessConditionsOnMeasures);
         }
      }
   }

   /**
    * This method will override the measure colums if user asked concept in select section that is mapped to both
    * dimension and measure colums in cube. In this case we have to override the defaults dimension colum with the
    * measure colum This method will override the measure colum if user asked concept in select section that is mapped
    * to both dimension and measure colums in cube. In this case we have to override the default dimension colum with
    * the measure colum
    * 
    * @param structuredQuery
    */
   private void overrideMeasureColum (StructuredQuery structuredQuery) throws GovernorException {
      logger.debug("Overriding the asset term for cube if asked by user in select section");
      // metric which is in condition has more than 2 assetentitydefs (duality) , we have to retain both else
      // we have to override if it is not a condition

      try {
         List<BusinessAssetTerm> toBeAddedBATs = new ArrayList<BusinessAssetTerm>();
         List<BusinessAssetTerm> lhsConditionBATs = new ArrayList<BusinessAssetTerm>();
         for (StructuredCondition structuredCondition : structuredQuery.getConditions()) {
            lhsConditionBATs.add(structuredCondition.getLhsBusinessAssetTerm());
         }
         for (BusinessAssetTerm businessAssetTerm : structuredQuery.getMetrics()) {
            if (businessAssetTerm.getAssetEntityDefinitions() != null
                     && businessAssetTerm.getAssetEntityDefinitions().size() > 1) {
               BusinessAssetTerm matchedBusinessAssetTerm = GovernorServiceHelper.getMatchedBusinessAssetTerm(
                        businessAssetTerm, lhsConditionBATs);
               // if stat is asked on a condition, it means it can be answered directly on a measure column
               // from the cube, if stat is not there, in that case we want range lookup to kick in
               boolean statBasedCondition = false;
               if (matchedBusinessAssetTerm == null
                        || matchedBusinessAssetTerm.getBusinessTerm().getBusinessStat() != null) {
                  statBasedCondition = true;
               }
               List<LightAssetEntityDefinitionInfo> assetEntityDefinitions = businessAssetTerm
                        .getAssetEntityDefinitions();

               for (LightAssetEntityDefinitionInfo lightAssetEntityDefinitionInfo : assetEntityDefinitions) {
                  if (ColumnType.MEASURE.equals(lightAssetEntityDefinitionInfo.getColumnType())) {
                     AssetEntityDefinition populatedAssetEntityDefinition = getSdxRetrievalService()
                              .getPopulatedAssetEntityDefinitionById(
                                       lightAssetEntityDefinitionInfo.getAssetEntityDefinitionId());
                     if (!statBasedCondition) {
                        BusinessAssetTerm clonedBusinessAssetTerm = ExecueBeanCloneUtil
                                 .cloneBusinessAssetTerm(businessAssetTerm);
                        clonedBusinessAssetTerm.setAssetEntityTerm(GovernorServiceHelper
                                 .populateAssetEntityTerm(populatedAssetEntityDefinition));
                        // remove the stat on the column which had dual nature and in this case being treated as
                        // dimension
                        businessAssetTerm.getBusinessTerm().setBusinessStat(null);
                        toBeAddedBATs.add(clonedBusinessAssetTerm);
                     } else {
                        if (matchedBusinessAssetTerm != null) {
                           matchedBusinessAssetTerm.setAssetEntityTerm(GovernorServiceHelper
                                    .populateAssetEntityTerm(populatedAssetEntityDefinition));
                        }
                        businessAssetTerm.setAssetEntityTerm(GovernorServiceHelper
                                 .populateAssetEntityTerm(populatedAssetEntityDefinition));
                     }
                     break;
                  }
               }
            }
         }
         structuredQuery.getMetrics().addAll(toBeAddedBATs);
      } catch (SDXException sdxException) {
         throw new GovernorException(sdxException.getCode(), sdxException.getMessage(), sdxException.getCause());
      }
   }

   /**
    * This method will apply stats on measures asked in metrics section.
    * 
    * @param structuredQuery
    */
   private void applyMissingStats (StructuredQuery structuredQuery) throws GovernorException {
      try {
         List<BusinessAssetTerm> businessAssetTerms = structuredQuery.getMetrics();
         for (BusinessAssetTerm businessAssetTerm : businessAssetTerms) {
            // check if business term doesn't have stat on it
            if (businessAssetTerm.getBusinessTerm().getBusinessStat() == null) {
               // check if this term is mapped to colum
               if (AssetEntityType.COLUMN.equals(businessAssetTerm.getAssetEntityTerm().getAssetEntityType())) {
                  // get the colum object
                  Colum colum = (Colum) businessAssetTerm.getAssetEntityTerm().getAssetEntity();
                  // check if business term is concept
                  if (BusinessEntityType.CONCEPT.equals(businessAssetTerm.getBusinessTerm().getBusinessEntityTerm()
                           .getBusinessEntityType())) {
                     // get the concept
                     Concept concept = (Concept) businessAssetTerm.getBusinessTerm().getBusinessEntityTerm()
                              .getBusinessEntity();

                     // check if the colum KDXDataType is measure or ID
                     /*
                      * if (ColumnType.ID.equals(colum.getKdxDataType())) { BusinessStat businessStat = new
                      * BusinessStat(); businessStat.setRequestedByUser(false);
                      * businessStat.setStat(getStat(StatType.COUNT));
                      * businessAssetTerm.getBusinessTerm().setBusinessStat(businessStat); } else
                      */
                     // NOTE: -RG- As the only ID column on cube is population column
                     // which inturn is nothing but Count of Population only.
                     // There is no need to add another STAT on top of.
                     // By doing the above logic commented, we can avoid COUNT stat in
                     // cube creation. Date : Mar 14 2012
                     if (ColumnType.MEASURE.equals(colum.getKdxDataType())) {
                        BusinessStat businessStat = new BusinessStat();
                        businessStat.setRequestedByUser(false);
                        // check if swi doesn't have a stat for this concept
                        if (ExecueCoreUtil.isCollectionEmpty(concept.getStats())) {
                           // apply system stat
                           businessStat.setStat(getStat(StatType.getStatType(getCoreConfigurationService()
                                    .getSystemLevelDefaultStat())));
                        }
                        // get the stats from swi and apply 0th one in the
                        // list.
                        else {
                           List<Stat> conceptStats = new ArrayList<Stat>(concept.getStats());
                           // TODO::NK:: If we have multiple stats defined on some concept, then the below behavior
                           // is not consistent as we might get any random stats because it is coming from persistent
                           // set
                           businessStat.setStat(conceptStats.get(0));
                        }
                        businessAssetTerm.getBusinessTerm().setBusinessStat(businessStat);
                     }
                  }
               }
            }
         }
      } catch (Exception exception) {
         throw new GovernorException(GovernorExceptionCodes.GOV_DEFAULT_EXCEPTION_CODE,
                  "Error while applying missing stats", exception.getCause());
      }
   }

   /**
    * This method will get the stat from swi
    * 
    * @param statType
    * @return stat
    */
   private Stat getStat (StatType statType) {
      Stat systemStat = null;
      try {
         systemStat = kdxRetrievalService.getStatByName(statType.getValue());
      } catch (KDXException e) {
         e.printStackTrace();
      }
      return systemStat;
   }

   /**
    * This method will organize the structured query by coming business asset terms from other sections to select
    * section if the business term is same but asset term is different. This scenario will come if some concept like
    * FICOSCORE is both a dimension and measure in cube. Then business query generator will not put the FICOSCORE asked
    * in other sections to select section and we are missing FICOSCORE which is mapped to dimension.
    * 
    * @param structuredQuery
    */
   private void organizeCubeQuery (StructuredQuery structuredQuery) throws GovernorException {
      try {
         List<BusinessAssetTerm> metrics = structuredQuery.getMetrics();
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getSummarizations())) {
            for (BusinessAssetTerm businessAssetTerm : structuredQuery.getSummarizations()) {
               BusinessAssetTerm metricBusinessAsset = GovernorServiceHelper.findBusinessAssetTermsForMetrics(
                        businessAssetTerm, metrics);
               if (metricBusinessAsset != null) {
                  logger.debug("Adding new BusinessAssetTerm to metrics section from summarizations section"
                           + ((Colum) metricBusinessAsset.getAssetEntityTerm().getAssetEntity()).getName());
                  metrics.add(metricBusinessAsset);
               }
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getConditions())) {
            for (StructuredCondition structuredCondition : structuredQuery.getConditions()) {
               BusinessAssetTerm metricBusinessAsset = GovernorServiceHelper.findBusinessAssetTermsForMetrics(
                        structuredCondition.getLhsBusinessAssetTerm(), metrics);
               if (metricBusinessAsset != null) {
                  logger.debug("Adding new BusinessAssetTerm to metrics section from conditions section"
                           + ((Colum) metricBusinessAsset.getAssetEntityTerm().getAssetEntity()).getName());
                  metrics.add(metricBusinessAsset);
               }
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getOrderClauses())) {
            for (StructuredOrderClause structuredOrderClause : structuredQuery.getOrderClauses()) {
               BusinessAssetTerm metricBusinessAsset = GovernorServiceHelper.findBusinessAssetTermsForMetrics(
                        structuredOrderClause.getBusinessAssetTerm(), metrics);
               if (metricBusinessAsset != null) {
                  logger.debug("Adding new BusinessAssetTerm to metrics section from order by section"
                           + ((Colum) metricBusinessAsset.getAssetEntityTerm().getAssetEntity()).getName());
                  metrics.add(metricBusinessAsset);
               }
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getPopulations())) {
            for (BusinessAssetTerm businessAssetTerm : structuredQuery.getPopulations()) {
               BusinessAssetTerm metricBusinessAsset = GovernorServiceHelper.findBusinessAssetTermsForMetrics(
                        businessAssetTerm, metrics);
               if (metricBusinessAsset != null) {
                  logger.debug("Adding new BusinessAssetTerm to metrics section from populations section"
                           + ((Colum) metricBusinessAsset.getAssetEntityTerm().getAssetEntity()).getName());
                  metrics.add(metricBusinessAsset);
               }
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getHavingClauses())) {
            for (StructuredCondition structuredCondition : structuredQuery.getHavingClauses()) {
               BusinessAssetTerm metricBusinessAsset = GovernorServiceHelper.findBusinessAssetTermsForMetrics(
                        structuredCondition.getLhsBusinessAssetTerm(), metrics);
               if (metricBusinessAsset != null) {
                  logger.debug("Adding new BusinessAssetTerm to metrics section from having section"
                           + ((Colum) metricBusinessAsset.getAssetEntityTerm().getAssetEntity()).getName());
                  metrics.add(metricBusinessAsset);
               }
            }
         }
      } catch (Exception exception) {
         throw new GovernorException(GovernorExceptionCodes.GOV_DEFAULT_EXCEPTION_CODE,
                  "Error while organizing cube query", exception.getCause());
      }
   }

   /**
    * This method will populate the scaling factor for structured query whose data source is mart
    * 
    * @param structuredQuery
    * @param entityMappings
    */
   private void populateScalingFactor (StructuredQuery structuredQuery, List<EntityMappingInfo> entityMappings)
            throws GovernorException {
      BusinessTerm scalingFactorBusinessTerm = createScalingFactorBusinessTerm();
      BusinessAssetTerm scalingFactorBusinessAssetTerm = GovernorServiceHelper.populateBusinessAssetTerm(
               scalingFactorBusinessTerm, entityMappings, structuredQuery.getAsset());
      logger.debug("Found Scaling Factor Business Asset Term " + scalingFactorBusinessAssetTerm);
      structuredQuery.setScalingFactor(scalingFactorBusinessAssetTerm);
   }

   /**
    * This method will prepare scaling factor business term
    * 
    * @return scalingFactorBusinessTerm
    */
   public BusinessTerm createScalingFactorBusinessTerm () throws GovernorException {
      try {
         String scalingFactorConceptName = getGovernorConfigurationService().getScalingFactorConceptName();
         BusinessEntityDefinition businessEntityDefinition = getBaseKDXRetrievalService().getConceptBEDByName(
                  scalingFactorConceptName);
         BusinessTerm businessTerm = new BusinessTerm();
         BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
         businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
         businessEntityTerm.setBusinessEntity(businessEntityDefinition.getConcept());
         businessEntityTerm.setBusinessEntityDefinitionId(businessEntityDefinition.getId().longValue());
         businessEntityTerm.setBaseGroupEntity(true);
         businessTerm.setBusinessEntityTerm(businessEntityTerm);
         businessTerm.setRequestedByUser(false);
         return businessTerm;
      } catch (KDXException e) {
         throw new GovernorException(GovernorExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   /**
    * This method is helper method for populateStructuredQueries() and being used in order to support recursion. This
    * will create a structured Query for Asset passed from the businessQuery passed using the entityMappings passed.
    * This method will return a valid structured query if an only if metrics section is present atleast.
    * 
    * @param asset
    * @param businessQuery
    * @param entityMappingInfo
    * @return structuredQuery
    */
   private StructuredQuery populateStructuredQuery (Asset asset, BusinessQuery businessQuery,
            List<EntityMappingInfo> entityMappingInfo) throws GovernorException {
      logger.debug("Inside populateStructuredQuery method");
      logger.debug("Got Asset for which structured Query has to be made : " + asset.getName());
      logger.debug("Got Business Query : " + businessQuery);
      logger.debug("Got entityMappings : " + entityMappingInfo);
      logger.debug("Populate Metrics section for structured Query");
      List<BusinessAssetTerm> metrics = GovernorServiceHelper.populateBusinessAssetTerms(businessQuery.getMetrics(),
               entityMappingInfo, asset);
      logger.debug("Populate Summarization section for structured Query");
      List<BusinessAssetTerm> summarizations = GovernorServiceHelper.populateBusinessAssetTerms(businessQuery
               .getSummarizations(), entityMappingInfo, asset);
      logger.debug("Populate Order section for structured Query");
      List<StructuredOrderClause> orderClauses = populateStructuredOrderClauses(businessQuery.getOrderClauses(),
               entityMappingInfo, asset);
      logger.debug("Populate Limit section for structured Query");
      StructuredLimitClause topBottom = populateLimitClause(businessQuery.getTopBottom(), entityMappingInfo, asset);
      if (topBottom != null) {
         StructuredOrderClause limitStructuredOrderClause = getOrderClauseFromLimitClause(topBottom);
         orderClauses.add(limitStructuredOrderClause);
      }

      // TODO :-VG- if the condition is in format sales > 10000 then cube cannot answer this condition(this case needs
      // knowledge of
      // KdxType so we will filter this conditions later when we go for modification of cube query), and in case
      // of relation assets, this condition can be answered.
      // if the condition is in format sum sales > 10000, then cube can answer this and for relation assets,
      // this condition needs to be moved to having clause.
      List<StructuredCondition> whereConditions = new ArrayList<StructuredCondition>();
      List<StructuredCondition> havingConditions = new ArrayList<StructuredCondition>();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getConditions())) {

         // if asset type is cube
         if (AssetType.Cube.equals(asset.getType())) {
            if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getConditions())) {
               whereConditions = populateStructuredConditions(businessQuery.getConditions(), entityMappingInfo, asset,
                        businessQuery.getModelId());
            }
         } else {
            // if asset type is relational
            // split the conditions.
            List<BusinessCondition> statConditions = new ArrayList<BusinessCondition>();
            List<BusinessCondition> plainConditions = new ArrayList<BusinessCondition>();
            for (BusinessCondition businessCondition : businessQuery.getConditions()) {
               if (businessCondition.getLhsBusinessTerm().getBusinessStat() != null) {
                  statConditions.add(businessCondition);
               } else {
                  plainConditions.add(businessCondition);
               }
            }
            if (ExecueCoreUtil.isCollectionNotEmpty(statConditions)) {
               havingConditions = populateStructuredConditions(statConditions, entityMappingInfo, asset, businessQuery
                        .getModelId());
            }
            if (ExecueCoreUtil.isCollectionNotEmpty(plainConditions)) {
               whereConditions = populateStructuredConditions(plainConditions, entityMappingInfo, asset, businessQuery
                        .getModelId());
            }
         }
      }

      // Populate the population section
      if (logger.isDebugEnabled()) {
         logger.debug("Populate Population section for structured Query");
      }
      List<BusinessAssetTerm> populations = GovernorServiceHelper.populateBusinessAssetTerms(businessQuery
               .getPopulations(), entityMappingInfo, asset);

      // Populate the hierarchy section
      List<HierarchyTerm> hierarchies = populateHierarchies(entityMappingInfo, asset, businessQuery.getHierarchies());

      // Prepare the structured query for cohort
      if (logger.isDebugEnabled()) {
         logger.debug("Preparing final structured Query");
      }

      StructuredQuery structuredQuery = new StructuredQuery();
      structuredQuery.setAsset(asset);
      structuredQuery.setModelId(businessQuery.getModelId());
      structuredQuery.setAssetAEDId(getSdxRetrievalService().getAssetEntityDefinitionByIds(asset, null, null, null)
               .getId());
      structuredQuery.setMetrics(metrics);
      structuredQuery.setConditions(whereConditions);
      structuredQuery.setSummarizations(summarizations);
      structuredQuery.setOrderClauses(orderClauses);
      structuredQuery.setHavingClauses(havingConditions);
      structuredQuery.setPopulations(populations);
      structuredQuery.setHierarchies(hierarchies);
      if (topBottom != null) {
         structuredQuery.setTopBottom(topBottom);
      }
      return structuredQuery;
   }

   private StructuredOrderClause getOrderClauseFromLimitClause (StructuredLimitClause topBottom)
            throws GovernorException {
      StructuredOrderClause orderClause = new StructuredOrderClause();
      try {
         topBottom.getBusinessAssetTerm().getBusinessTerm().setRequestedByUser(false);
         orderClause.setBusinessAssetTerm(topBottom.getBusinessAssetTerm());
         if (OrderLimitEntityType.TOP.equals(topBottom.getLimitType())) {
            orderClause.setOrderEntityType(OrderEntityType.DESCENDING);
         } else if (OrderLimitEntityType.BOTTOM.equals(topBottom.getLimitType())) {
            orderClause.setOrderEntityType(OrderEntityType.ASCENDING);
         }
      } catch (Exception exception) {
         throw new GovernorException(GovernorExceptionCodes.GOV_DEFAULT_EXCEPTION_CODE,
                  "Error while getting OrderClauseFromLimitClause", exception.getCause());
      }
      return orderClause;
   }

   private StructuredLimitClause populateLimitClause (BusinessLimitClause topBottom,
            List<EntityMappingInfo> entityMappings, Asset asset) throws GovernorException {
      StructuredLimitClause structuredLimitClause = null;
      if (topBottom != null) {
         BusinessAssetTerm businessAssetTerm = GovernorServiceHelper.populateBusinessAssetTerm(topBottom
                  .getBusinessTerm(), entityMappings, asset);
         if (businessAssetTerm != null) {
            structuredLimitClause = new StructuredLimitClause();
            structuredLimitClause.setBusinessAssetTerm(businessAssetTerm);
            try {
               Integer limitValue = Integer.parseInt(topBottom.getLimitValue());
               structuredLimitClause.setLimitValue(limitValue);
               structuredLimitClause.setLimitType(topBottom.getLimitType());
            } catch (NumberFormatException numberFormatException) {
               // the limit value is invalid
               structuredLimitClause = null;
            }
         }
      }
      return structuredLimitClause;
   }

   private List<StructuredOrderClause> populateStructuredOrderClauses (List<BusinessOrderClause> businessOrderClauses,
            List<EntityMappingInfo> entityMappings, Asset asset) throws GovernorException {
      List<StructuredOrderClause> orderClauses = new ArrayList<StructuredOrderClause>();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessOrderClauses)) {
         for (BusinessOrderClause businessOrderClause : businessOrderClauses) {
            StructuredOrderClause structuredOrderClause = new StructuredOrderClause();
            BusinessAssetTerm businessAssetTerm = GovernorServiceHelper.populateBusinessAssetTerm(businessOrderClause
                     .getBusinessTerm(), entityMappings, asset);
            if (businessAssetTerm != null) {
               structuredOrderClause.setBusinessAssetTerm(businessAssetTerm);
               structuredOrderClause.setOrderEntityType(businessOrderClause.getOrderEntityType());
               orderClauses.add(structuredOrderClause);
            }
         }
      }
      return orderClauses;
   }

   /**
    * This method is helper method to populateStructuredQueries() in order to populate StructuredConditions. This
    * requires extra handling of taking care that both sides of the condition are present per asset, then only add it to
    * structured Query, otherwise don't add anything.
    * 
    * @param businessConditions
    * @param metrics
    * @param entityMappings
    * @param asset
    * @param modelId
    * @return structuredConditions
    */
   private List<StructuredCondition> populateStructuredConditions (List<BusinessCondition> businessConditions,
            List<EntityMappingInfo> entityMappings, Asset asset, Long modelId) throws GovernorException {
      logger.debug("Inside populateStructuredConditions method");
      logger.debug("Got businessConditions for which we need to populate StructuredConditions : " + businessConditions);
      logger.debug("Got entityMapping using which we need to populate structuredConditions : " + entityMappings);
      logger.debug("Got Asset for which we need to prepare strcutured conditions : " + asset.getName());
      List<StructuredCondition> conditions = new ArrayList<StructuredCondition>();
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(businessConditions)) {
            for (BusinessCondition businessCondition : businessConditions) {
               // from the business condition left side concept, check if this is time frame
               // if concept is time frame, then call time frame handler.

               if (getKdxRetrievalService().isMatchedBusinessEntityType(
                        businessCondition.getLhsBusinessTerm().getBusinessEntityTerm().getBusinessEntityDefinitionId(),
                        ExecueConstants.TIME_FRAME_TYPE)) {
                  Long startTime = System.currentTimeMillis();
                  StructuredCondition timeFrameStructuredCondition = governorTimeFrameHandlerService
                           .buildTimeFrameCondition(businessCondition, entityMappings, asset);
                  if (timeFrameStructuredCondition != null) {
                     conditions.add(timeFrameStructuredCondition);
                  }
                  Long endTime = System.currentTimeMillis();
                  logger.warn("Building time Frame condition " + (endTime - startTime) + " ms");
               } else {
                  // get the lhs mapping.
                  BusinessAssetTerm lhsBusinessAssetTerm = GovernorServiceHelper.populateBusinessAssetTerm(
                           businessCondition.getLhsBusinessTerm(), entityMappings, asset);
                  // if lhs is not mapped, then ignore the condition, but in case of time frame, alternate list can be
                  // used
                  if (lhsBusinessAssetTerm != null) {
                     StructuredCondition structuredCondition = new StructuredCondition();
                     structuredCondition.setLhsBusinessAssetTerm(lhsBusinessAssetTerm);
                     structuredCondition.setOperator(businessCondition.getOperator());
                     structuredCondition.setOperandType(businessCondition.getOperandType());
                     if (OperandType.BUSINESS_TERM.equals(businessCondition.getOperandType())) {
                        logger.debug("Found RHS businessTerms : " + businessCondition.getRhsBusinessTerms());
                        List<BusinessAssetTerm> rhsBusinessAssetTerms = new ArrayList<BusinessAssetTerm>();
                        for (BusinessTerm businessTerm : businessCondition.getRhsBusinessTerms()) {
                           BusinessAssetTerm rhsBusinessAssetTerm = GovernorServiceHelper.populateBusinessAssetTerm(
                                    businessTerm, entityMappings, asset);
                           if (rhsBusinessAssetTerm != null) {
                              rhsBusinessAssetTerms.add(rhsBusinessAssetTerm);
                           }
                        }
                        // TODO : -VG- added logic to handle between operator right side values if one is mapped and
                        // other is not
                        boolean isRhsValuesSufficient = false;
                        if (OperatorType.BETWEEN.equals(businessCondition.getOperator())) {
                           if (rhsBusinessAssetTerms.size() == 2) {
                              isRhsValuesSufficient = true;
                           }
                        } else {
                           if (rhsBusinessAssetTerms.size() > 0) {
                              isRhsValuesSufficient = true;
                           }
                        }
                        if (isRhsValuesSufficient) {
                           logger.debug("Adding rhs business terms");
                           structuredCondition.setRhsBusinessAssetTerms(rhsBusinessAssetTerms);
                           conditions.add(structuredCondition);
                        }
                     } else if (OperandType.BUSINESS_QUERY.equals(businessCondition.getOperandType())) {
                        logger.debug("Found RHS subQuery : " + businessCondition.getRhsBusinessQuery());
                        StructuredQuery rhsStructuredQuery = populateStructuredQuery(asset, businessCondition
                                 .getRhsBusinessQuery(), entityMappings);
                        if (rhsStructuredQuery != null) {
                           structuredCondition.setRhsStructuredQuery(rhsStructuredQuery);
                           conditions.add(structuredCondition);
                        }
                     } else if (OperandType.VALUE.equals(businessCondition.getOperandType())) {
                        if (ExecueCoreUtil.isCollectionNotEmpty(businessCondition.getRhsValues())) {
                           logger.debug("Found RHS List<QueryValues> : " + businessCondition.getRhsValues());
                           Colum lhsColumn = (Colum) lhsBusinessAssetTerm.getAssetEntityTerm().getAssetEntity();
                           applyDefaultSourceConversions(businessCondition, structuredCondition, lhsColumn);
                           applyConversions(businessCondition, structuredCondition, asset, lhsColumn);
                        }
                        conditions.add(structuredCondition);
                     }
                  }
               }
            }
         }
      } catch (SWIException swiException) {
         throw new GovernorException(swiException.getCode(), "SWI Exception in populateStructuredConditions",
                  swiException.getCause());
      } catch (CloneNotSupportedException e) {
         throw new GovernorException(GovernorExceptionCodes.DEFAULT_EXCEPTION_CODE,
                  "SWI Exception in populateStructuredConditions", e.getCause());
      }
      return conditions;
   }

   private void applyDefaultSourceConversions (BusinessCondition businessCondition,
            StructuredCondition structuredCondition, Colum lhsColumn) throws SWIException {
      if (lhsColumn.getDataType().equals(DataType.NUMBER) || lhsColumn.getDataType().equals(DataType.INT)
               || lhsColumn.getDataType().equals(DataType.LARGE_INTEGER)) {
         Conversion baseNumberConversion = getConversionService().getBaseConversion(ConversionType.NUMBER);
         for (QueryValue queryValue : businessCondition.getRhsValues()) {
            if (queryValue.getNumberConversionId() == null) {
               queryValue.setNumberConversionId(baseNumberConversion.getId());
            }
         }
      }
   }

   /**
    * This method will do the conversion on query value if required
    * 
    * @param businessCondition
    * @param structuredCondition
    * @param asset
    * @param lhsColumn
    * @throws SWIException
    * @throws CloneNotSupportedException
    */
   private void applyConversions (BusinessCondition businessCondition, StructuredCondition structuredCondition,
            Asset asset, Colum lhsColumn) throws SWIException, CloneNotSupportedException {
      // clone the query value objects to avoid modifying the business query
      // query value objects
      List<QueryValue> clonedQueryValues = ExecueBeanCloneUtil.cloneQueryValues(businessCondition.getRhsValues(), null);
      Conversion sourceConversion = null;
      Conversion targetConversion = null;
      for (QueryValue queryValue : clonedQueryValues) {
         if (queryValue.getNumberConversionId() != null) {
            logger.debug("Query Value has conversion id " + queryValue.getNumberConversionId());
            logger.debug("Query Value has value " + queryValue.getValue());
            sourceConversion = getConversionService().getConversionById(queryValue.getNumberConversionId());
            targetConversion = new Conversion();
            if (ConversionType.NUMBER.equals(sourceConversion.getType())) {
               logger.debug("Number Conversion Required");
               if (ExecueCoreUtil.isEmpty(lhsColumn.getDataFormat())) {
                  logger.debug("Column doesnt have data format defined.Getting Base Format");
                  targetConversion = getConversionService().getBaseConversion(sourceConversion.getType());
               } else {
                  logger.debug("column has format associated with it " + lhsColumn.getDataFormat());
                  targetConversion.setFormat(lhsColumn.getDataFormat());
               }
               convertValue(sourceConversion, targetConversion, queryValue, ConversionType.NUMBER);
               queryValue.setTargetConversionFormat(targetConversion.getFormat());
               if (businessCondition.getConversionId() != null) {
                  logger.debug("Unit Conversion Required");
                  logger.debug("Unit Conversion Source Id" + businessCondition.getConversionId());
                  sourceConversion = getConversionService().getConversionById(businessCondition.getConversionId());
                  if (!ConversionType.LOCATION.equals(sourceConversion.getType())) {
                     if (ExecueCoreUtil.isEmpty(lhsColumn.getUnit())) {
                        logger.debug("Column doesnt have Unit defined.Getting Base Unit");
                        targetConversion = getConversionService().getBaseConversion(sourceConversion.getType());
                     } else {
                        logger.debug("column has Unit associated with it " + lhsColumn.getUnit());
                        targetConversion.setUnit(lhsColumn.getUnit());
                     }
                     convertValue(sourceConversion, targetConversion, queryValue, ConversionType.DEFAULT);
                     structuredCondition.setTargetConversionUnit(targetConversion.getUnit());
                  }
               }
            }
         }
      }
      structuredCondition.setRhsValues(clonedQueryValues);
   }

   /**
    * This method will do the conversion between source and destination conversions for query value and for
    * conversionType specified
    * 
    * @param source
    * @param target
    * @param queryValue
    * @param conversionType
    * @throws SWIException
    */
   private void convertValue (Conversion source, Conversion target, QueryValue queryValue, ConversionType conversionType)
            throws SWIException {
      if (isConversionRequired(source, target, conversionType)) {
         logger.debug("Getting convertor for ConversionType" + conversionType);
         ITypeConvertor numberTypeConvertor = getTypeConvertorFactory().getTypeConvertor(conversionType);
         if (!(CheckType.YES.equals(source.getBase()) || CheckType.YES.equals(target.getBase()))) {
            logger.debug("Convert the source to base and then new source to target");
            Conversion baseConversion = getConversionService().getBaseConversion(source.getType());
            queryValue.setValue(numberTypeConvertor.convert(source, baseConversion, queryValue.getValue()));
            queryValue.setValue(numberTypeConvertor.convert(baseConversion, target, queryValue.getValue()));
         } else {
            logger.debug("As source or destination is in base already, so directly doing the conversion");
            queryValue.setValue(numberTypeConvertor.convert(source, target, queryValue.getValue()));
         }
      }
   }

   private boolean isConversionRequired (Conversion source, Conversion target, ConversionType conversionType) {
      boolean isConversionRequired = true;
      if (ConversionType.NUMBER.equals(conversionType)) {
         if (source.getFormat().equalsIgnoreCase(target.getFormat())) {
            isConversionRequired = false;
         }
      } else if (ConversionType.DEFAULT.equals(conversionType)) {
         if (source.getUnit().equalsIgnoreCase(target.getUnit())) {
            isConversionRequired = false;
         }
      }
      return isConversionRequired;
   }

   private List<Membr> sortMembersByOrderOfValues (List<String> values, List<Membr> members) {
      List<Membr> sortedMemberList = new ArrayList<Membr>();
      for (String value : values) {
         for (Membr membr : members) {
            if (membr.getLookupValue().equalsIgnoreCase(value)) {
               sortedMemberList.add(membr);
               break;
            }
         }
      }
      return sortedMemberList;
   }

   public void correctAndLoadEntityMappingInfo (List<EntityMappingInfo> entityMappingInfo, Set<Asset> activeAnswerAssets)
            throws GovernorException {
      try {
         for (Asset asset : activeAnswerAssets) {
            for (EntityMappingInfo tempEntityMappingInfo : entityMappingInfo) {
               Map<Long, List<LightAssetEntityDefinitionInfo>> assetBasedLightAssetEntityDefinitions = tempEntityMappingInfo
                        .getAssetBasedLightAssetEntityDefinitions();
               List<LightAssetEntityDefinitionInfo> lightAssetEntityDefinitions = assetBasedLightAssetEntityDefinitions
                        .get(asset.getId());
               if (ExecueCoreUtil.isCollectionNotEmpty(lightAssetEntityDefinitions)) {

                  LightAssetEntityDefinitionInfo correctLightAssetEntityDefinition = pickCorrectAssetEntityDefinitionInfo(lightAssetEntityDefinitions);

                  AssetEntityDefinition assetEntityDefinition = getSdxRetrievalService()
                           .getPopulatedAssetEntityDefinitionById(
                                    correctLightAssetEntityDefinition.getAssetEntityDefinitionId());
                  AssetEntityTerm assetEntityTerm = GovernorServiceHelper
                           .populateAssetEntityTerm(assetEntityDefinition);
                  Map<Long, AssetEntityTerm> assetBasedAssetEntityTerm = tempEntityMappingInfo
                           .getAssetBasedAssetEntityTerm();
                  if (assetBasedAssetEntityTerm == null) {
                     Map<Long, AssetEntityTerm> newAssetBasedEntityTerm = new HashMap<Long, AssetEntityTerm>();
                     tempEntityMappingInfo.setAssetBasedAssetEntityTerm(newAssetBasedEntityTerm);
                  }
                  tempEntityMappingInfo.getAssetBasedAssetEntityTerm().put(asset.getId(), assetEntityTerm);
               }
            }
         }
      } catch (SDXException sdxException) {
         throw new GovernorException(GovernorExceptionCodes.DEFAULT_SYSTEM_EXCEPTION_CODE, sdxException);
      }
   }

   public void correctAndLoadEntityMappingInfo2 (List<EntityMappingInfo> entityMappingInfo,
            Set<Asset> activeAnswerAssets) throws GovernorException {
      try {
         for (Asset asset : activeAnswerAssets) {
            for (EntityMappingInfo tempEntityMappingInfo : entityMappingInfo) {
               Map<Long, List<LightAssetEntityDefinitionInfo>> assetBasedLightAssetEntityDefinitions = tempEntityMappingInfo
                        .getAssetBasedLightAssetEntityDefinitions();
               List<LightAssetEntityDefinitionInfo> lightAssetEntityDefinitions = assetBasedLightAssetEntityDefinitions
                        .get(asset.getId());
               if (ExecueCoreUtil.isCollectionNotEmpty(lightAssetEntityDefinitions)) {

                  // NK: No need of this as this is already being done offline with the correctMappingService job
                  // LightAssetEntityDefinitionInfo correctLightAssetEntityDefinition =
                  // pickCorrectAssetEntityDefinitionInfo(lightAssetEntityDefinitions);
                  LightAssetEntityDefinitionInfo correctLightAssetEntityDefinition = lightAssetEntityDefinitions.get(0);

                  AssetEntityDefinition assetEntityDefinition = getSdxRetrievalService()
                           .getPopulatedAssetEntityDefinitionById(
                                    correctLightAssetEntityDefinition.getAssetEntityDefinitionId());
                  AssetEntityTerm assetEntityTerm = GovernorServiceHelper
                           .populateAssetEntityTerm(assetEntityDefinition);
                  Map<Long, AssetEntityTerm> assetBasedAssetEntityTerm = tempEntityMappingInfo
                           .getAssetBasedAssetEntityTerm();
                  if (assetBasedAssetEntityTerm == null) {
                     Map<Long, AssetEntityTerm> newAssetBasedEntityTerm = new HashMap<Long, AssetEntityTerm>();
                     tempEntityMappingInfo.setAssetBasedAssetEntityTerm(newAssetBasedEntityTerm);
                  }
                  tempEntityMappingInfo.getAssetBasedAssetEntityTerm().put(asset.getId(), assetEntityTerm);
               }
            }
         }
      } catch (SDXException sdxException) {
         throw new GovernorException(GovernorExceptionCodes.DEFAULT_SYSTEM_EXCEPTION_CODE, sdxException);
      }
   }

   private LightAssetEntityDefinitionInfo pickCorrectAssetEntityDefinitionInfo (
            List<LightAssetEntityDefinitionInfo> assetEntityDefinitions) throws GovernorException {
      // rules for picking the right aed are
      // 3. pick up the dimension type(id, sl, rl, shl,rhl,dimension)
      // 2. if column belongs to lookup table
      // 1. if column is entire pk
      // 4. if column is part of pk
      // if the list of AED's if greater than 0, then we pick up the first one in the list.
      // this will take care of situation if one and only one entry exists.
      // if more than one entry exits, then we will iterate and find the one which is primary key
      // or whose datatype is dimension.
      LightAssetEntityDefinitionInfo correctAssetEntityDefinition = assetEntityDefinitions.get(0);
      boolean correctEntityFound = false;
      try {
         if (assetEntityDefinitions.size() == 1) {
            correctEntityFound = true;
         } else {
            if (!correctEntityFound) {
               for (LightAssetEntityDefinitionInfo assetEntityDefinition : assetEntityDefinitions) {
                  if (isTableTypeLookupCategory(assetEntityDefinition.getTableLookupType())) {
                     correctEntityFound = true;
                     correctAssetEntityDefinition = assetEntityDefinition;
                     break;
                  }
               }
            }
            if (!correctEntityFound) {
               for (LightAssetEntityDefinitionInfo assetEntityDefinition : assetEntityDefinitions) {
                  if (isKDXDataTypeDimensionCategory(assetEntityDefinition.getColumnType())) {
                     correctEntityFound = true;
                     correctAssetEntityDefinition = assetEntityDefinition;
                     break;
                  }
               }
            }
            if (!correctEntityFound) {
               for (LightAssetEntityDefinitionInfo assetEntityDefinition : assetEntityDefinitions) {
                  if (assetEntityDefinition.getPrimaryKey() != null
                           && ConstraintSubType.ENTIRE.equals(assetEntityDefinition.getPrimaryKey())) {
                     correctEntityFound = true;
                     correctAssetEntityDefinition = assetEntityDefinition;
                     break;
                  }
               }
            }
            if (!correctEntityFound) {
               for (LightAssetEntityDefinitionInfo assetEntityDefinition : assetEntityDefinitions) {
                  if (assetEntityDefinition.getPrimaryKey() != null
                           && ConstraintSubType.PART.equals(assetEntityDefinition.getPrimaryKey())) {
                     correctEntityFound = true;
                     correctAssetEntityDefinition = assetEntityDefinition;
                     break;
                  }
               }
            }
         }
      } catch (Exception exception) {
         throw new GovernorException(GovernorExceptionCodes.GOV_DEFAULT_EXCEPTION_CODE,
                  "Error in picking CorrectAssetEntityDefinitionInfo", exception.getCause());
      }
      return correctAssetEntityDefinition;
   }

   private boolean isKDXDataTypeDimensionCategory (ColumnType columnType) {
      boolean isDimensionCategory = false;
      if (ColumnType.ID.equals(columnType) || ColumnType.DIMENSION.equals(columnType)
               || ColumnType.SIMPLE_LOOKUP.equals(columnType) || ColumnType.RANGE_LOOKUP.equals(columnType)
               || ColumnType.SIMPLE_HIERARCHY_LOOKUP.equals(columnType)) {
         isDimensionCategory = true;
      }
      return isDimensionCategory;
   }

   private boolean isTableTypeLookupCategory (LookupType lookupType) {
      boolean isLookupTypeCategory = false;
      if (LookupType.SIMPLE_LOOKUP.equals(lookupType) || LookupType.RANGE_LOOKUP.equals(lookupType)
               || LookupType.SIMPLEHIERARCHICAL_LOOKUP.equals(lookupType)
               || LookupType.RANGEHIERARCHICAL_LOOKUP.equals(lookupType)) {
         isLookupTypeCategory = true;
      }
      return isLookupTypeCategory;
   }

   private List<HierarchyTerm> populateHierarchies (List<EntityMappingInfo> entityMappingInfo, Asset asset,
            List<HierarchyTerm> hierarchyTerms) {
      List<HierarchyTerm> filteredHierarchyTerms = new ArrayList<HierarchyTerm>();
      if (ExecueCoreUtil.isCollectionNotEmpty(hierarchyTerms)) {
         for (HierarchyTerm hierarchyTerm : hierarchyTerms) {
            // clone the HierarchyTerm.
            HierarchyTerm clonedHierarchyTerm = cloneHierarchyTerm(hierarchyTerm);
            // validate hierarchy if any of the participating entity in hierarchy has at least one mapping in this
            // asset then check for the number of BusinessAssetTerms.
            if (isValidHierarchy(clonedHierarchyTerm.getParticipatingQueryEntityIDs(), entityMappingInfo, asset)) {
               List<BusinessAssetTerm> businessAssetTerms = GovernorServiceHelper.populateBusinessAssetTerms(
                        clonedHierarchyTerm.getHierarchyBusinessDefinition(), entityMappingInfo, asset);
               if (ExecueCoreUtil.isCollectionNotEmpty(businessAssetTerms) && businessAssetTerms.size() > 1) {
                  clonedHierarchyTerm.setHierarchyBusinessAssetDefinition(businessAssetTerms);
                  filteredHierarchyTerms.add(clonedHierarchyTerm);
               }
            }
         }
      }
      return filteredHierarchyTerms;
   }

   /**
    * This method returns true if any of the participating entity has at least one mapping for this asset.
    * 
    * @param participatingQueryEntityIDs
    * @param entityMappingInfoList
    * @param asset
    * @return
    */
   private boolean isValidHierarchy (List<Long> participatingQueryEntityIDs,
            List<EntityMappingInfo> entityMappingInfoList, Asset asset) {
      boolean validHierarchy = false;
      for (Long entityId : participatingQueryEntityIDs) {
         for (EntityMappingInfo entityMappingInfo : entityMappingInfoList) {
            if (entityMappingInfo.getBusinessEntityTerm().getBusinessEntityDefinitionId().equals(entityId)) {
               AssetEntityTerm assetEntityTerm = entityMappingInfo.getAssetBasedAssetEntityTerm().get(asset.getId());
               if (assetEntityTerm != null) {
                  validHierarchy = true;
                  break;
               }
            }
         }
         // Found mapping for at least one participating entity,come out of iteration.
         if (validHierarchy) {
            break;
         }
      }
      return validHierarchy;
   }

   private HierarchyTerm cloneHierarchyTerm (HierarchyTerm toBeClonedHierarchyTerm) {
      HierarchyTerm clonedHierarchyTerm = new HierarchyTerm();
      if (toBeClonedHierarchyTerm != null) {
         List<BusinessTerm> clonedHierarchyBusinessDefinition = new ArrayList<BusinessTerm>();
         for (BusinessTerm toBeClonedBusinessTerm : toBeClonedHierarchyTerm.getHierarchyBusinessDefinition()) {
            clonedHierarchyBusinessDefinition.add(ExecueBeanCloneUtil.cloneBusinessTerm(toBeClonedBusinessTerm));
         }

         // This list will be empty till this time.
         List<BusinessAssetTerm> clonedHierarchyBusinessAssetDefinition = new ArrayList<BusinessAssetTerm>();
         if (ExecueCoreUtil.isCollectionNotEmpty(toBeClonedHierarchyTerm.getHierarchyBusinessAssetDefinition())) {
            for (BusinessAssetTerm toBeClonedBusinessAssetTerm : clonedHierarchyBusinessAssetDefinition) {
               // TODO : -AG- need to write clone method for BusinessAssetTerm.
               clonedHierarchyBusinessAssetDefinition.add(toBeClonedBusinessAssetTerm);
            }
         }

         List<Long> clonedParticipatingQueryEntityIDs = new ArrayList<Long>();
         for (Long toBeClonedQueryEntityId : toBeClonedHierarchyTerm.getParticipatingQueryEntityIDs()) {
            clonedParticipatingQueryEntityIDs.add(new Long(toBeClonedQueryEntityId.longValue()));
         }
         clonedHierarchyTerm.setHierarchyId(toBeClonedHierarchyTerm.getHierarchyId());
         clonedHierarchyTerm.setHierarchyName(toBeClonedHierarchyTerm.getHierarchyName());
         clonedHierarchyTerm.setHierarchyBusinessDefinition(clonedHierarchyBusinessDefinition);
         clonedHierarchyTerm.setHierarchyBusinessAssetDefinition(clonedHierarchyBusinessAssetDefinition);
         clonedHierarchyTerm.setParticipatingQueryEntityIDs(clonedParticipatingQueryEntityIDs);
      }
      return clonedHierarchyTerm;
   }

   public TypeConvertorFactory getTypeConvertorFactory () {
      return typeConvertorFactory;
   }

   public void setTypeConvertorFactory (TypeConvertorFactory typeConvertorFactory) {
      this.typeConvertorFactory = typeConvertorFactory;
   }

   public IConversionService getConversionService () {
      return conversionService;
   }

   public void setConversionService (IConversionService conversionService) {
      this.conversionService = conversionService;
   }

   public ILookupService getLookupService () {
      return lookupService;
   }

   public void setLookupService (ILookupService lookupService) {
      this.lookupService = lookupService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
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

   public IGovernorTimeFrameHandlerService getGovernorTimeFrameHandlerService () {
      return governorTimeFrameHandlerService;
   }

   public void setGovernorTimeFrameHandlerService (IGovernorTimeFrameHandlerService governorTimeFrameHandlerService) {
      this.governorTimeFrameHandlerService = governorTimeFrameHandlerService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   /**
    * @return the governorConfigurationService
    */
   public IGovernorConfigurationService getGovernorConfigurationService () {
      return governorConfigurationService;
   }

   /**
    * @param governorConfigurationService
    *           the governorConfigurationService to set
    */
   public void setGovernorConfigurationService (IGovernorConfigurationService governorConfigurationService) {
      this.governorConfigurationService = governorConfigurationService;
   }

   public IStructuredQueryEntityFilterationService getStructuredQueryEntityFilterationService () {
      return structuredQueryEntityFilterationService;
   }

   public void setStructuredQueryEntityFilterationService (
            IStructuredQueryEntityFilterationService structuredQueryEntityFilterationService) {
      this.structuredQueryEntityFilterationService = structuredQueryEntityFilterationService;
   }
}
