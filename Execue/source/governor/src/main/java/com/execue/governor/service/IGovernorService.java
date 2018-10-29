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
import java.util.Set;

import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.governor.AssetQuery;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.governor.exception.GovernorException;

/**
 * This class contain method for extracting the AssetQueries from businessQuery.
 * 
 * @author Raju Gottumukkala
 * @version 1.0
 * @since 10/02/09
 */
public interface IGovernorService {

   /**
    * This method is used to extract List of Asset Queries from BusinessQuery. It uses GovernorServiceHelper to populate
    * Unique BusinessEntityTerms. Then it uses queryMappingService in order to find mappings from swi for each of the
    * unique business terms. Then it corrects the mappings using queryMappingService and find the unique assets which
    * can answer the business query. It again uses the GovernorServiceHelper to create structured queries for each of
    * the unique assets. After it got the structured queries, it goes to federation in order to decide which of the
    * structured queries to drop because they doesn't reflect the correct picture of business query. It uses
    * GovernorServiceHelper again to assign weights to each businessTerm and calculate the weight of the business Query.
    * It used GovernorServiceHelper to calculate the weight for each structured query.After that it uses
    * AssetQueryGenerationService to generate as many as asset queries as structured queries.
    * 
    * @param businessQuery
    * @return assetQueries
    * @throws GovernorException
    */
   public List<AssetQuery> extractDataAssetQueries (BusinessQuery businessQuery) throws GovernorException;

   public List<AssetQuery> extractDataAssetQueries2 (BusinessQuery businessQuery, Set<Asset> answerAssets,
            Set<BusinessEntityTerm> businessEntityTerms) throws GovernorException;

   public Set<BusinessEntityTerm> populateBusinessEntityTerms (BusinessQuery businessQuery) throws GovernorException;
}
