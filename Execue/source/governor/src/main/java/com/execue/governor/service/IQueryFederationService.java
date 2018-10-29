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


package com.execue.governor.service;

import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.bean.swi.PossibleAssetInfo;
import com.execue.governor.exception.QueryFederationException;

/**
 * This class contain methods which is used to federate structured queries by asset, grain, weight and priority.
 * 
 * @author Raju Gottumukkala
 * @version 1.0
 * @since 10/02/09
 */
public interface IQueryFederationService {

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
            throws QueryFederationException;

   /**
    * This method is used to federate structured queries based on query weights.This method uses a comparator in order
    * to compare two structured queries.The comparator is using weight as an attribute to compare two queries.
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return structuredQueries
    */
   public List<StructuredQuery> federateByWeight (BusinessQuery businessQuery, List<StructuredQuery> structuredQueries)
            throws QueryFederationException;

   /**
    * This method is used to federate structured queries by asset. Assume User asked for specific information which
    * cannot be answered by Cube then we will reduce the weight for that query.
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return structuredQueries
    */
   public List<StructuredQuery> federateByAsset (BusinessQuery businessQuery, List<StructuredQuery> structuredQueries)
            throws QueryFederationException;

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
            throws QueryFederationException;

   /**
    * This method is used to federate structured queries based on cohort section available. If cohort section is
    * available in business query, then the structured queries for cube will be discarded
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return structuredQueries
    */
   public List<StructuredQuery> federateByCohortSection (BusinessQuery businessQuery,
            List<StructuredQuery> structuredQueries);

   /**
    * This method is used to federate structured queries based on limit section available. If limit section is available
    * in business query, then the structured queries for cube will be discarded
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return structuredQueries
    */
   public List<StructuredQuery> federateByLimitSection (BusinessQuery businessQuery,
            List<StructuredQuery> structuredQueries);

   /**
    * This method will be used to calculate the relative weights for the list of structured query.
    * 
    * @param businessQuery
    * @param structuredQueries
    * @return
    */
   public List<StructuredQuery> calculateRelativePercentageWeights (BusinessQuery businessQuery,
            List<StructuredQuery> structuredQueries);

   /**
    * This method will federate the list of PossibleAssetInfo for each possibility based on cohort, limit, weight and
    * priority
    * 
    * @param possibleAssetForPossibilityMap
    * @param businessQueryMap
    * @param filteredAssetsMap
    */
   public void federatePossibleAssetInfo (Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap,
            Map<Long, BusinessQuery> businessQueryMap, Map<Long, Asset> filteredAssetsMap);

   // public void federatePossibleAssetInfoByWeight (Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap);
   //
   // public void federatePossibleAssetInfoByPriority (Map<Long, List<PossibleAssetInfo>>
   // possibleAssetForPossibilityMap,
   // Map<Long, BusinessQuery> businessQueryMap, Map<Long, Asset> filteredAssetsMap);
   public void federatePossibleAssetInfoByTopCluster (
            Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap,
            Map<Long, BusinessQuery> businessQueryMap, Map<Long, Asset> filteredAssetsMap);

}