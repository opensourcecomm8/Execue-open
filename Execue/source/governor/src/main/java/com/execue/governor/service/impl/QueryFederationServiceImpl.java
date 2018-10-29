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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.bean.swi.PossibleAssetInfo;
import com.execue.core.common.bean.swi.comparator.PossibleAssetInfoComparatorByAssetWeight;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.governor.exception.QueryFederationException;
import com.execue.governor.service.IQueryFederationService;
import com.execue.util.MathUtil;

/**
 * This class contain methods which is used to federate structured queries by asset, grain, weight and priority.
 * 
 * @author Vishay
 * @version 1.0
 * @since 10/02/09
 */
public class QueryFederationServiceImpl implements IQueryFederationService {

   private static final Logger logger = Logger.getLogger(QueryFederationServiceImpl.class);

   /**
    * This method is used to federate structured queries by asset. Assume User asked for specific information which
    * cannot be answered by Cube then we will reduce the weight for that query.
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return structuredQueries
    */
   public List<StructuredQuery> federateByAsset (BusinessQuery businessQuery, List<StructuredQuery> structuredQueries)
            throws QueryFederationException {
      logger.debug("Inside federateByAsset method");
      logger.debug("Got businessQuery which will act as a reference in order to filter the structuredQueries : "
               + businessQuery);
      logger.debug("Got StructuredQueries list which we need to filter on the basis of relevance of queries : "
               + structuredQueries);

      // TODO: -VG- has to implement
      return structuredQueries;
   }

   /**
    * This method is used to federate structured queries based on asset priorities. If Weights are very close for
    * structured queries then we will sort them on basis of asset priorities. This method uses a comparator in order to
    * compare two structured queries. The comparator is using asset priority for comparing two queries.
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return structuredQueries
    */
   public List<StructuredQuery> federateByPriority (BusinessQuery businessQuery, List<StructuredQuery> structuredQueries)
            throws QueryFederationException {
      logger.debug("Inside federateByPriority method");
      logger.debug("Got businessQuery which will act as a reference in order to filter the structuredQueries : "
               + businessQuery);
      logger.debug("Got StructuredQueries list which we need to filter on the basis of priority of their assets : "
               + structuredQueries);
      Map<Long, List<StructuredQuery>> baseAssetStructuredQueriesMap = new HashMap<Long, List<StructuredQuery>>();
      for (StructuredQuery structuredQuery : structuredQueries) {
         if (structuredQuery.getAsset().getBaseAssetId() == null) {
            List<StructuredQuery> structuredQueriesList = new ArrayList<StructuredQuery>();
            structuredQueriesList.add(structuredQuery);
            baseAssetStructuredQueriesMap.put(structuredQuery.getAsset().getId(), structuredQueriesList);
         }
      }

      for (StructuredQuery structuredQuery : structuredQueries) {
         if (structuredQuery.getAsset().getBaseAssetId() != null) {
            Long matchedStructuredQueryAsset = getMatchedStructuredQueryAsset(structuredQuery,
                     baseAssetStructuredQueriesMap.keySet());
            if (matchedStructuredQueryAsset != null) {
               baseAssetStructuredQueriesMap.get(matchedStructuredQueryAsset).add(structuredQuery);
            } else {
               List<StructuredQuery> structuredQueriesList = new ArrayList<StructuredQuery>();
               structuredQueriesList.add(structuredQuery);
               baseAssetStructuredQueriesMap.put(structuredQuery.getAsset().getBaseAssetId(), structuredQueriesList);
            }
         }
      }

      // sort the inner sets for each base asset and take off the queries
      // which are less than required percentile
      // based on priority
      List<StructuredQuery> weightPriorityFederatedQueries = new ArrayList<StructuredQuery>();
      for (Long assetId : baseAssetStructuredQueriesMap.keySet()) {
         List<StructuredQuery> structuredQueriesSet = baseAssetStructuredQueriesMap.get(assetId);
         List<Double> structuredQueryWeights = new ArrayList<Double>();
         for (StructuredQuery structuredQuery : structuredQueriesSet) {
            structuredQueryWeights.add(structuredQuery.getAssetWeight());
         }
         List<Double> weightsInTopCluster = MathUtil.getTopCluster(structuredQueryWeights);
         List<StructuredQuery> weightFederatedQueries = new ArrayList<StructuredQuery>();
         for (StructuredQuery structuredQuery : structuredQueriesSet) {
            if (weightsInTopCluster.contains(structuredQuery.getAssetWeight())) {
               weightFederatedQueries.add(structuredQuery);
            }
         }
         if (weightFederatedQueries.size() > 1) {
            List<Double> structuredQueryPriorties = new ArrayList<Double>();
            for (StructuredQuery structuredQuery : weightFederatedQueries) {
               structuredQueryPriorties.add(structuredQuery.getAsset().getPriority());
            }
            List<Double> priortiesInTopCluster = MathUtil.getTopCluster(structuredQueryPriorties);
            for (StructuredQuery structuredQuery : weightFederatedQueries) {
               if (priortiesInTopCluster.contains(structuredQuery.getAsset().getPriority())) {
                  weightPriorityFederatedQueries.add(structuredQuery);
               }
            }

         } else {
            weightPriorityFederatedQueries.addAll(weightFederatedQueries);
         }
         /*
          * Map<String, Double> structuredQueryPriorties = new HashMap<String, Double>(); for (StructuredQuery
          * structuredQuery : structuredQueriesSet) { structuredQueryPriorties
          * .put(structuredQuery.getAsset().getName(), structuredQuery.getAsset().getPriority()); } Map<String, Double>
          * relativePercentageMap = ExecueCoreUtil.getRelativePercentageMap(structuredQueryPriorties); for
          * (StructuredQuery structuredQuery : structuredQueriesSet) { String formattedPercentile =
          * String.format("%.2f", relativePercentageMap.get(structuredQuery.getAsset() .getName()));
          * structuredQuery.setRelativePriority(Double.parseDouble(formattedPercentile)); }
          */
      }

      /*
       * double minPriorityBasedPercentage = getGovernorConfiguration().getDouble(
       * GovernorConfigurationConstants.MIN_PRIORITY_BASED_QUERY_PERCENTAGE_KEY); for (StructuredQuery structuredQuery :
       * structuredQueries) { if (structuredQuery.getRelativePriority() >= minPriorityBasedPercentage) {
       * priorityFederatedQueries.add(structuredQuery); } }
       */
      return weightPriorityFederatedQueries;
   }

   private Long getMatchedStructuredQueryAsset (StructuredQuery structuredQuery, Set<Long> assetIds) {
      Long matchedAssetId = null;
      for (Long assetId : assetIds) {
         if (structuredQuery.getAsset().getBaseAssetId().equals(assetId)) {
            matchedAssetId = assetId;
            break;
         }
      }
      return matchedAssetId;
   }

   /**
    * This method is used to federate structured queries based on query weights.This method uses a comparator in order
    * to compare two structured queries.The comparator is using weight as an attribute to compare two queries.
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return structuredQueries
    */
   public List<StructuredQuery> federateByWeight (BusinessQuery businessQuery, List<StructuredQuery> structuredQueries)
            throws QueryFederationException {
      List<StructuredQuery> weightFederatedQueries = new ArrayList<StructuredQuery>();
      logger.debug("Inside federateByWeight method");
      logger.debug("Got businessQuery which will act as a reference in order to filter the structuredQueries : "
               + businessQuery);
      logger.debug("Got StructuredQueries list which we need to filter on the basis of their weights : "
               + structuredQueries);
      // calculate the percentile for structured queries based on their
      // weights
      // Map<String, Double> structuredQueryWeightMap = new HashMap<String,
      // Double>();
      List<Double> structuredQueryWeights = new ArrayList<Double>();
      for (StructuredQuery structuredQuery : structuredQueries) {
         // structuredQueryWeightMap.put(structuredQuery.getAsset().getName(),
         // structuredQuery.getStructuredQueryWeight());
         structuredQueryWeights.add(structuredQuery.getStructuredQueryWeight());
      }
      List<Double> topWeights = MathUtil.getLiberalTopCluster(structuredQueryWeights);
      for (StructuredQuery structuredQuery : structuredQueries) {
         if (topWeights.contains(structuredQuery.getStructuredQueryWeight())) {
            weightFederatedQueries.add(structuredQuery);
         }
      }
      /*
       * Map<String, Double> relativePercentageMap = ExecueCoreUtil.getRelativePercentageMap(structuredQueryWeights);
       * for (StructuredQuery structuredQuery : structuredQueries) { String formattedPercentile = String.format("%.2f",
       * relativePercentageMap.get(structuredQuery.getAsset() .getName()));
       * structuredQuery.setStructuredQueryWeight(Double.parseDouble(formattedPercentile)); } double
       * minQueryWeightPercentage = getGovernorConfiguration().getDouble(
       * GovernorConfigurationConstants.MIN_WEIGHT_QUERY_PERCENTAGE_KEY); for (StructuredQuery structuredQuery :
       * structuredQueries) { if (structuredQuery.getStructuredQueryWeight().doubleValue() >= minQueryWeightPercentage) {
       * weightFederatedQueries.add(structuredQuery); } }
       */

      // Collections.sort(weightFederatedQueries, new
      // StructuredQueryWeightComparator());
      return weightFederatedQueries;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.governor.service.IQueryFederationService#calculateRrelativePercentageWeights(com.execue.core.common.bean.BusinessQuery,
    *      java.util.List)
    */
   public List<StructuredQuery> calculateRelativePercentageWeights (BusinessQuery businessQuery,
            List<StructuredQuery> structuredQueries) {
      // calculate the percentile for structured queries based on their
      // weights in comparison to business query
      Map<String, Double> structuredQueryWeightMap = new HashMap<String, Double>();
      for (StructuredQuery structuredQuery : structuredQueries) {
         structuredQueryWeightMap.put(structuredQuery.getAsset().getName(), structuredQuery.getStructuredQueryWeight());
      }
      structuredQueryWeightMap.put("##BQ##", businessQuery.getBusinessQueryWeight());
      Map<String, Double> relativePercentageMap = ExecueCoreUtil.getRelativePercentageMap(structuredQueryWeightMap);
      for (StructuredQuery structuredQuery : structuredQueries) {
         String formattedPercentile = String.format("%.2f", relativePercentageMap.get(structuredQuery.getAsset()
                  .getName()));
         structuredQuery.setStructuredQueryWeight(Double.parseDouble(formattedPercentile));
      }
      return structuredQueries;
   }

   /**
    * This method is used to federate structured queries by grain. It will first find all the concepts inside the
    * structured query and then find all the concepts in the asset grain. If any of the concepts inside the structured
    * query appears in the asset grain, it will increase the query weight, otherwise it will reduce the weight of the
    * query by some amount which is configurable. It uses weight comparator to sort the queries after increase/decrease
    * weights.
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return structuredQueries
    */
   public List<StructuredQuery> federateByGrain (BusinessQuery businessQuery, List<StructuredQuery> structuredQueries)
            throws QueryFederationException {
      logger.debug("Inside federateByGrain method");
      logger.debug("Got businessQuery which will act as a reference in order to filter the structuredQueries : "
               + businessQuery);
      logger.debug("Got StructuredQueries list which we need to filter on the basis of grain : " + structuredQueries);
      // TODO: -VG- logic needs to be rewritten
      // for (StructuredQuery structuredQuery : structuredQueries) {
      // // find the concepts inside structured query
      // Set<String> structuredQueryConcepts =
      // getStructuredQueryConcepts(structuredQuery);
      // // find the concepts(grain) for the asset
      // Set<String> assetGrainConcepts =
      // getAssetGrainConcepts(structuredQuery);
      // boolean grainFound = false;
      // // check if the query has grain, then increase the weight
      // for (String structuredQueryConcept : structuredQueryConcepts) {
      // if (assetGrainConcepts.contains(structuredQueryConcept)) {
      // grainFound = true;
      // break;
      // }
      // }
      // if (grainFound) {
      // structuredQuery.setStructuredQueryWeight(structuredQuery.getStructuredQueryWeight()
      // + QueryWeight.QUERY_GRAIN_WEIGHT.getValue());
      // } else {
      // structuredQuery.setStructuredQueryWeight(structuredQuery.getStructuredQueryWeight()
      // - QueryWeight.QUERY_GRAIN_WEIGHT.getValue());
      // }
      // }
      // Collections.sort(structuredQueries, new
      // StructuredQueryWeightComparator());
      // // TODO: -VG- take off some structured Queries, less weight queries.
      return structuredQueries;

   }

   /**
    * This method is helper method for federateByGrain() in order to find concepts belonging to asset grain. From the
    * asset, it will get grain which is list of domainEntityDefinitions and find the concepts inside them.
    * 
    * @param structuredQuery
    * @return assetGrainConcepts
    */
   // private Set<String> getAssetGrainConcepts (StructuredQuery
   // structuredQuery) {
   // logger.debug("Inside getAssetGrainConcepts method");
   // logger.debug("Got StructuredQuery which has asset associated with it : "
   // + structuredQuery);
   // Set<String> assetGrainConcepts = new HashSet<String>();
   // Set<BusinessEntityDefinition> domainEntityDefinitions =
   // structuredQuery.getAsset().getGrain();
   // for (BusinessEntityDefinition domainEntityDefinition :
   // domainEntityDefinitions) {
   // assetGrainConcepts.add(domainEntityDefinition.getConcept().getName());
   // }
   // return assetGrainConcepts;
   // }
   /**
    * This method is helper method for federateByGrain() in order to return conceptName from the businessEntityTerm. If
    * businessEntityTerm type is Concept, it will return conceptName, otherwise null.
    * 
    * @param businessAssetTerm
    * @return conceptName
    */
   // private String getConceptName (BusinessAssetTerm businessAssetTerm) {
   // logger.debug("Inside getConceptName method");
   // logger.debug("Got businessAssetTerm for which we need to find ConceptName
   // : " + businessAssetTerm);
   // String conceptName = null;
   // if
   // (BusinessEntityType.CONCEPT.equals(businessAssetTerm.getBusinessTerm().getBusinessEntityTerm()
   // .getBusinessEntityType())) {
   // Concept concept = (Concept)
   // businessAssetTerm.getBusinessTerm().getBusinessEntityTerm().getBusinessEntity();
   // conceptName = concept.getName();
   // }
   // return conceptName;
   // }
   /**
    * This method id helper method to federateByGrain() in order to find Concepts of structured query passed. From each
    * section, we will find the concepts from businessEntityTerm(if it is a concept). If structured query has subquery
    * then it will recursively add the subquery concepts into the main query concepts list.
    * 
    * @param structuredQuery
    * @return conceptNames.
    */
   // private Set<String> getStructuredQueryConcepts (StructuredQuery
   // structuredQuery) {
   // logger.debug("Inside getStructuredQueryConcepts method");
   // logger.debug("Got StruturedQuery for which we need to find the concepts :
   // " + structuredQuery);
   // Set<String> structuredQueryConcepts = new HashSet<String>();
   // if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getMetrics())) {
   // for (BusinessAssetTerm businessAssetTerm : structuredQuery.getMetrics())
   // {
   // String ConceptName = getConceptName(businessAssetTerm);
   // if (ConceptName != null) {
   // structuredQueryConcepts.add(ConceptName);
   // }
   // }
   // }
   // if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getConditions())) {
   // for (StructuredCondition structuredCondition :
   // structuredQuery.getConditions()) {
   // String lhsConceptName =
   // getConceptName(structuredCondition.getLhsBusinessAssetTerm());
   // if (lhsConceptName != null) {
   // structuredQueryConcepts.add(lhsConceptName);
   // }
   // if
   // (OperandType.BUSINESS_TERM.equals(structuredCondition.getOperandType()))
   // {
   // for (BusinessAssetTerm businessAssetTerm :
   // structuredCondition.getRhsBusinessAssetTerms()) {
   // String rhsConceptName = getConceptName(businessAssetTerm);
   // if (rhsConceptName != null) {
   // structuredQueryConcepts.add(rhsConceptName);
   // }
   // }
   // } else if
   // (structuredCondition.getOperandType().equals(OperandType.BUSINESS_QUERY))
   // {
   // structuredQueryConcepts.addAll(getStructuredQueryConcepts(structuredCondition.getRhsStructuredQuery()));
   // }
   // }
   // }
   // if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getSummarizations()))
   // {
   // for (BusinessAssetTerm businessAssetTerm :
   // structuredQuery.getSummarizations()) {
   // String ConceptName = getConceptName(businessAssetTerm);
   // if (ConceptName != null) {
   // structuredQueryConcepts.add(ConceptName);
   // }
   // }
   // }
   // if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getHavingClauses())) {
   // for (StructuredCondition structuredCondition :
   // structuredQuery.getHavingClauses()) {
   // String lhsConceptName =
   // getConceptName(structuredCondition.getLhsBusinessAssetTerm());
   // if (lhsConceptName != null) {
   // structuredQueryConcepts.add(lhsConceptName);
   // }
   // if
   // (structuredCondition.getOperandType().equals(OperandType.BUSINESS_TERM))
   // {
   // for (BusinessAssetTerm businessAssetTerm :
   // structuredCondition.getRhsBusinessAssetTerms()) {
   // String rhsConceptName = getConceptName(businessAssetTerm);
   // if (rhsConceptName != null) {
   // structuredQueryConcepts.add(rhsConceptName);
   // }
   // }
   // } else if
   // (structuredCondition.getOperandType().equals(OperandType.BUSINESS_QUERY))
   // {
   // structuredQueryConcepts.addAll(getStructuredQueryConcepts(structuredCondition.getRhsStructuredQuery()));
   // }
   // }
   // }
   // if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getOrderClauses())) {
   // for (StructuredOrderClause structuredOrderClause :
   // structuredQuery.getOrderClauses()) {
   // String ConceptName =
   // getConceptName(structuredOrderClause.getBusinessAssetTerm());
   // if (ConceptName != null) {
   // structuredQueryConcepts.add(ConceptName);
   // }
   // }
   // }
   // return structuredQueryConcepts;
   // }
   /**
    * This method is used to federate structured queries based on cohort section available. If cohort section is
    * available in business query, then the structured queries for cube will be discarded
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return structuredQueries
    */
   public List<StructuredQuery> federateByCohortSection (BusinessQuery businessQuery,
            List<StructuredQuery> structuredQueries) {
      List<StructuredQuery> structuredQueriesForCube = new ArrayList<StructuredQuery>();
      if (isValidCohortExists(businessQuery)) {
         for (StructuredQuery structuredQuery : structuredQueries) {
            boolean isExecueOwnedCubeQuery = ExecueBeanUtil.isExecueOwnedCube(structuredQuery.getAsset());
            if (isExecueOwnedCubeQuery) {
               structuredQueriesForCube.add(structuredQuery);
            }
         }
      }
      structuredQueries.removeAll(structuredQueriesForCube);
      return structuredQueries;
   }

   /**
    * This method is used to federate possibleAssetForPossibilityMap based on cohort section available. If cohort
    * section is available in business query, then the possible asset queries for cube will be discarded
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return structuredQueries
    */
   private void federateByCohortSection (Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap,
            Map<Long, BusinessQuery> businessQueryMap, Map<Long, Asset> filteredAssetsMap) {
      Set<Entry<Long, List<PossibleAssetInfo>>> entries = possibleAssetForPossibilityMap.entrySet();
      for (Iterator<Entry<Long, List<PossibleAssetInfo>>> iterator = entries.iterator(); iterator.hasNext();) {
         Entry<Long, List<PossibleAssetInfo>> entry = iterator.next();
         if (isValidCohortExists(businessQueryMap.get(entry.getKey()))) {
            federateByCohort(entry.getValue(), filteredAssetsMap);

            // Remove the entry itself, if all the values are federated
            if (ExecueCoreUtil.isCollectionEmpty(entry.getValue())) {
               iterator.remove();
            }
         }
      }
   }

   /**
    * This method federates the cohort possibleAssetInfoList queries if asset is execue owned cube
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return structuredQueries
    */
   private void federateByCohort (List<PossibleAssetInfo> possibleAssetInfoList, Map<Long, Asset> filteredAssetsMap) {

      for (Iterator<PossibleAssetInfo> iterator = possibleAssetInfoList.iterator(); iterator.hasNext();) {
         PossibleAssetInfo possibleAssetInfo = iterator.next();
         Asset asset = filteredAssetsMap.get(possibleAssetInfo.getAssetId());
         boolean isExecueOwnedCubeQuery = ExecueBeanUtil.isExecueOwnedCube(asset);
         if (isExecueOwnedCubeQuery) {
            iterator.remove();
         }
      }
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
    * This method is used to federate structured queries based on limit section available. If limit section is available
    * in business query, then the structured queries for cube will be discarded
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return structuredQueries
    */
   public List<StructuredQuery> federateByLimitSection (BusinessQuery businessQuery,
            List<StructuredQuery> structuredQueries) {
      List<StructuredQuery> structuredQueriesForCube = new ArrayList<StructuredQuery>();
      if (businessQuery.getTopBottom() != null) {
         for (StructuredQuery structuredQuery : structuredQueries) {
            boolean isExecueOwnedCubeQuery = ExecueBeanUtil.isExecueOwnedCube(structuredQuery.getAsset());
            if (isExecueOwnedCubeQuery) {
               structuredQueriesForCube.add(structuredQuery);
            }
         }
      }
      structuredQueries.removeAll(structuredQueriesForCube);
      return structuredQueries;
   }

   /**
    * This method is used to federate possibleAssetForPossibilityMap queries based on limit section available. If limit
    * section is available in business query, then the possible asset queries for cube will be discarded
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return structuredQueries
    */
   private void federateByLimitSection (Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap,
            Map<Long, BusinessQuery> businessQueryMap, Map<Long, Asset> filteredAssetsMap) {
      Set<Entry<Long, List<PossibleAssetInfo>>> entries = possibleAssetForPossibilityMap.entrySet();
      for (Iterator<Entry<Long, List<PossibleAssetInfo>>> iterator = entries.iterator(); iterator.hasNext();) {
         Entry<Long, List<PossibleAssetInfo>> entry = iterator.next();
         if (businessQueryMap.get(entry.getKey()).getTopBottom() != null) {
            federateByLimit(entry.getValue(), filteredAssetsMap);

            // Remove the entry itself, if all the values are federated
            if (ExecueCoreUtil.isCollectionEmpty(entry.getValue())) {
               iterator.remove();
            }
         }
      }
   }

   /**
    * This method federates the limit possibleAssetInfoList if the asset is execue owned cube
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return structuredQueries
    */
   private void federateByLimit (List<PossibleAssetInfo> possibleAssetInfoList, Map<Long, Asset> filteredAssetsMap) {

      for (Iterator<PossibleAssetInfo> iterator = possibleAssetInfoList.iterator(); iterator.hasNext();) {
         PossibleAssetInfo possibleAssetInfo = iterator.next();
         Asset asset = filteredAssetsMap.get(possibleAssetInfo.getAssetId());
         boolean isExecueOwnedCubeQuery = ExecueBeanUtil.isExecueOwnedCube(asset);
         if (isExecueOwnedCubeQuery) {
            iterator.remove();
         }
      }
   }

   /**
    * This method is used to federate PossibleAssetInfo based on query weights.
    * 
    * @param possibleAssetForPossibilityMap
    */
   private void federateByWeight (Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap) {
      // federate each possibility by weight
      Set<Entry<Long, List<PossibleAssetInfo>>> entries = possibleAssetForPossibilityMap.entrySet();
      for (Iterator<Entry<Long, List<PossibleAssetInfo>>> iterator = entries.iterator(); iterator.hasNext();) {
         Entry<Long, List<PossibleAssetInfo>> entry = iterator.next();
         entry.setValue(federateByWeight(entry.getValue()));
      }
   }

   /**
    * This method is used to federate structured queries based on query weights.This method uses a comparator in order
    * to compare two PossibleAssetInfo weight.The comparator is using weight as an attribute to compare two queries.
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return structuredQueries
    */
   private List<PossibleAssetInfo> federateByWeight (List<PossibleAssetInfo> possibleAssetInfos) {
      // check for the assets which is answering from both cache and live flow.
      // collect the assets and do top cluster on top of them. if both survives then take off the live asset
      // from the group

      // prepare a map of key as assetId and value as List<PossibleAssetInfo> max can be 2, because
      // one result from cache and one live.
      Map<Long, List<PossibleAssetInfo>> assetPossibleAssetInfo = new HashMap<Long, List<PossibleAssetInfo>>();
      for (PossibleAssetInfo possibleAssetInfo : possibleAssetInfos) {
         if (assetPossibleAssetInfo.keySet().contains(possibleAssetInfo.getAssetId())) {
            assetPossibleAssetInfo.get(possibleAssetInfo.getAssetId()).add(possibleAssetInfo);
         } else {
            List<PossibleAssetInfo> newPossibleAssetInfos = new ArrayList<PossibleAssetInfo>();
            newPossibleAssetInfos.add(possibleAssetInfo);
            assetPossibleAssetInfo.put(possibleAssetInfo.getAssetId(), newPossibleAssetInfos);
         }
      }

      List<PossibleAssetInfo> finalPossibleAssetInfoList = new ArrayList<PossibleAssetInfo>();
      // top cluster and removal of live result from the map entries
      for (Long assetId : assetPossibleAssetInfo.keySet()) {
         PossibleAssetInfo finalPossibleAssetInfo = null;
         List<PossibleAssetInfo> possibleAssetInfo = assetPossibleAssetInfo.get(assetId);
         if (possibleAssetInfo.size() == 2) {
            List<PossibleAssetInfo> topClusterResults = runTopClusterOnPossibleAssetInfoList(possibleAssetInfo);
            // if cache and live both survived top cluster, we will take live one off.
            if (topClusterResults.size() == 2) {
               PossibleAssetInfo livePossibleAssetInfo = getLivePossibleAssetInfo(topClusterResults);
               topClusterResults.remove(livePossibleAssetInfo);
            }
            finalPossibleAssetInfo = topClusterResults.get(0);
         }
         // it means only one result per asset.
         else {
            finalPossibleAssetInfo = possibleAssetInfo.get(0);
         }
         finalPossibleAssetInfoList.add(finalPossibleAssetInfo);
      }

      // get a list of possibleAssetInfo with each asset has only one possibleAssetInfo.
      finalPossibleAssetInfoList = runTopClusterOnPossibleAssetInfoList(finalPossibleAssetInfoList);
      Collections.sort(finalPossibleAssetInfoList, new PossibleAssetInfoComparatorByAssetWeight());
      return finalPossibleAssetInfoList;
   }

   private PossibleAssetInfo getLivePossibleAssetInfo (List<PossibleAssetInfo> possibleAssetInfoList) {
      PossibleAssetInfo livePossibleAssetInfo = null;
      for (PossibleAssetInfo possibleAssetInfo : possibleAssetInfoList) {
         if (!possibleAssetInfo.isFromQueryCache()) {
            livePossibleAssetInfo = possibleAssetInfo;
            break;
         }
      }
      return livePossibleAssetInfo;
   }

   private List<PossibleAssetInfo> runTopClusterOnPossibleAssetInfoList (List<PossibleAssetInfo> possibleAssetInfoList) {
      List<PossibleAssetInfo> weightFederatedAssets = new ArrayList<PossibleAssetInfo>();

      List<Double> assetTypeWeights = new ArrayList<Double>();
      for (PossibleAssetInfo possibleAssetInfo : possibleAssetInfoList) {
         assetTypeWeights.add(possibleAssetInfo.getTotalTypeBasedWeight());
      }
      List<Double> topWeights = MathUtil.getLiberalTopCluster(assetTypeWeights);
      for (PossibleAssetInfo possibleAssetInfo : possibleAssetInfoList) {
         if (topWeights.contains(possibleAssetInfo.getTotalTypeBasedWeight())) {
            weightFederatedAssets.add(possibleAssetInfo);
         }
      }
      return weightFederatedAssets;
   }

   /**
    * This method is used to federate structured queries based on asset priorities. If Weights are very close for
    * structured queries then we will sort them on basis of asset priorities. This method uses a comparator in order to
    * compare two structured queries. The comparator is using asset priority for comparing two queries.
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return structuredQueries
    */
   private void federateByPriority (Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap,
            Map<Long, BusinessQuery> businessQueryMap, Map<Long, Asset> filteredAssetsMap) {
      logger.debug("Inside federateByPriority method");

      Set<Entry<Long, List<PossibleAssetInfo>>> entries = possibleAssetForPossibilityMap.entrySet();
      for (Iterator<Entry<Long, List<PossibleAssetInfo>>> iterator = entries.iterator(); iterator.hasNext();) {
         Entry<Long, List<PossibleAssetInfo>> entry = iterator.next();
         BusinessQuery businessQuery = businessQueryMap.get(entry.getKey());
         logger.debug("Got businessQuery which will act as a reference in order to filter the structuredQueries : "
                  + businessQuery);

         List<PossibleAssetInfo> possibleAssetInfoList = entry.getValue();
         /*
          * logger.debug("Got StructuredQueries list which we need to filter on the basis of priority of their assets : " +
          * structuredQueries);
          */
         Map<Long, List<PossibleAssetInfo>> basePossibleAssetInfoMap = new HashMap<Long, List<PossibleAssetInfo>>();
         for (PossibleAssetInfo possibleAssetInfo : possibleAssetInfoList) {
            Asset asset = filteredAssetsMap.get(possibleAssetInfo.getAssetId());
            if (asset.getBaseAssetId() == null) {
               List<PossibleAssetInfo> basePossibleAssetInfoList = new ArrayList<PossibleAssetInfo>();
               basePossibleAssetInfoList.add(possibleAssetInfo);
               basePossibleAssetInfoMap.put(asset.getId(), basePossibleAssetInfoList);
            }
         }

         for (PossibleAssetInfo possibleAssetInfo : possibleAssetInfoList) {
            Asset asset = filteredAssetsMap.get(possibleAssetInfo.getAssetId());
            if (asset.getBaseAssetId() != null) {
               Long matchedPossibleAssetInfoAsset = getMatchedPossibleAssetInfoAsset(possibleAssetInfo,
                        basePossibleAssetInfoMap.keySet(), filteredAssetsMap);
               if (matchedPossibleAssetInfoAsset != null) {
                  basePossibleAssetInfoMap.get(matchedPossibleAssetInfoAsset).add(possibleAssetInfo);
               } else {
                  List<PossibleAssetInfo> structuredQueriesList = new ArrayList<PossibleAssetInfo>();
                  structuredQueriesList.add(possibleAssetInfo);
                  basePossibleAssetInfoMap.put(filteredAssetsMap.get(possibleAssetInfo.getAssetId()).getBaseAssetId(),
                           structuredQueriesList);
               }
            }
         }

         // sort the inner sets for each base asset and take off the queries
         // which are less than required percentile based on priority
         List<PossibleAssetInfo> priorityFederatedPossibleAssetInfo = new ArrayList<PossibleAssetInfo>();
         for (Long assetId : basePossibleAssetInfoMap.keySet()) {
            List<PossibleAssetInfo> possibleAssetInfos = basePossibleAssetInfoMap.get(assetId);
            List<Double> possibleAssetInfoWeights = new ArrayList<Double>();
            for (PossibleAssetInfo possibleAssetInfo : possibleAssetInfos) {
               possibleAssetInfoWeights.add(possibleAssetInfo.getTotalTypeBasedWeight());
            }

            // TODO:: RG:: Commenting the below code, as not required
            // we apply similar weight based filter but with liberal top cluster in previous federateByWeight call 
            //            List<Double> weightsInTopCluster = MathUtil.getTopCluster(possibleAssetInfoWeights);
            //            List<PossibleAssetInfo> weightFederatedPossibleAssetInfo = new ArrayList<PossibleAssetInfo>();
            //            for (PossibleAssetInfo possibleAssetInfo : possibleAssetInfos) {
            //               if (weightsInTopCluster.contains(possibleAssetInfo.getTotalTypeBasedWeight())) {
            //                  weightFederatedPossibleAssetInfo.add(possibleAssetInfo);
            //               }
            //            }
            if (possibleAssetInfos.size() > 1) {
               List<Double> possibleAssetInfoPriorities = new ArrayList<Double>();
               for (PossibleAssetInfo possibleAssetInfo : possibleAssetInfos) {
                  possibleAssetInfoPriorities.add(filteredAssetsMap.get(possibleAssetInfo.getAssetId()).getPriority());
               }
               List<Double> prioritiesInTopCluster = MathUtil.getTopCluster(possibleAssetInfoPriorities);
               for (PossibleAssetInfo possibleAssetInfo : possibleAssetInfos) {
                  if (prioritiesInTopCluster.contains(filteredAssetsMap.get(possibleAssetInfo.getAssetId())
                           .getPriority())) {
                     priorityFederatedPossibleAssetInfo.add(possibleAssetInfo);
                  }
               }
            } else {
               priorityFederatedPossibleAssetInfo.addAll(possibleAssetInfos);
            }
         }
         Collections.sort(priorityFederatedPossibleAssetInfo, new PossibleAssetInfoComparatorByAssetWeight());
         entry.setValue(priorityFederatedPossibleAssetInfo);
      }
   }

   private Long getMatchedPossibleAssetInfoAsset (PossibleAssetInfo structuredQuery, Set<Long> assetIds,
            Map<Long, Asset> filteredAssetsMap) {
      Long matchedAssetId = null;
      for (Long assetId : assetIds) {
         if (filteredAssetsMap.get(structuredQuery.getAssetId()).getBaseAssetId().equals(assetId)) {
            matchedAssetId = assetId;
            break;
         }
      }
      return matchedAssetId;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IQueryFederationService#federatePossibleAssetInfo(java.util.Map,
    *      java.util.Map, java.util.Map)
    */
   public void federatePossibleAssetInfo (Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap,
            Map<Long, BusinessQuery> businessQueryMap, Map<Long, Asset> filteredAssetsMap) {

      federateByCohortSection(possibleAssetForPossibilityMap, businessQueryMap, filteredAssetsMap);

      federateByLimitSection(possibleAssetForPossibilityMap, businessQueryMap, filteredAssetsMap);

   }

   public void federatePossibleAssetInfoByTopCluster (
            Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap,
            Map<Long, BusinessQuery> businessQueryMap, Map<Long, Asset> filteredAssetsMap) {
      federateByWeight(possibleAssetForPossibilityMap);

      federateByPriority(possibleAssetForPossibilityMap, businessQueryMap, filteredAssetsMap);

   }
}