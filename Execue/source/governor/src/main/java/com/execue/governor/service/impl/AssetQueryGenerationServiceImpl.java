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

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.governor.AssetQuery;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;
import com.execue.governor.exception.AssetQueryGenerationException;
import com.execue.governor.service.IAssetQueryGenerationService;
import com.execue.querygen.service.IQueryGenerationService;
import com.execue.querygen.service.IStructuredQueryTransformerService;
import com.execue.querygen.service.QueryGenerationServiceFactory;

/**
 * This class contain method for converting structured query to assetQuery.
 * 
 * @author Vishay
 * @version 1.0
 * @since 08/02/09
 */
public class AssetQueryGenerationServiceImpl implements IAssetQueryGenerationService {

   private static final Logger                logger = Logger.getLogger(AssetQueryGenerationServiceImpl.class);

   private QueryGenerationServiceFactory      queryGenerationServiceFactory;
   private IStructuredQueryTransformerService structuredQueryTransformerService;

   /**
    * This method generated AssetQuery from structuredQuery passed to it. It uses QueryGeneration and QueryOptimization
    * services to generate an aggregate query. It will first use governor's helper method to populate
    * QueryGenerationInput object which will be input to queryGeneration module. The physical query type(SQL, SPL, MDX,
    * XMLA) is determined based on the asset properties. Populate the normalized input required for query generation
    * module to be able to get the required physical IQuery(SQL, SPL etc.) back that will be grouped together with the
    * Structured Query to form the Asset Query Once Asset Query is generated, should optimize the query using
    * Optimization service.
    * 
    * @param structuredQuery
    * @return assetQuery
    * @throws AssetQueryGenerationException
    * @throws QueryOptimizationException
    */
   public AssetQuery generateAssetQuery (StructuredQuery structuredQuery) throws AssetQueryGenerationException {
      if (logger.isDebugEnabled()) {
         logger.debug("Inside generateAssetQuery method");
         logger.debug("Got structuredQuery for which we need to generate AssetQuery : " + structuredQuery);
      }
      AssetQuery assetQuery = new AssetQuery();
      assetQuery.setLogicalQuery(structuredQuery);
      Map<String, BusinessAssetTerm> aliasBusinessAssetTermMap = new HashMap<String, BusinessAssetTerm>();
      // IQueryGenerationService queryGenerationService = getQueryGenerationService(structuredQuery.getAsset());
      QueryGenerationOutput queryGenerationOutput = structuredQueryTransformerService.populateQueryGenerationOutput(
               structuredQuery, aliasBusinessAssetTermMap);
      // if (logger.isDebugEnabled()) {
      // logger.debug("Governor Query : "
      // + queryGenerationService.extractQueryRepresentation(queryGenerationOutput).getQueryString());
      // }
      assetQuery.setPhysicalQuery(queryGenerationOutput.getResultQuery());
      return assetQuery;
   }

   private IQueryGenerationService getQueryGenerationService (Asset asset) {
      return queryGenerationServiceFactory.getQueryGenerationService(asset);
   }

   public IStructuredQueryTransformerService getStructuredQueryTransformerService () {
      return structuredQueryTransformerService;
   }

   public void setStructuredQueryTransformerService (
            IStructuredQueryTransformerService structuredQueryTransformerService) {
      this.structuredQueryTransformerService = structuredQueryTransformerService;
   }

   public QueryGenerationServiceFactory getQueryGenerationServiceFactory () {
      return queryGenerationServiceFactory;
   }

   public void setQueryGenerationServiceFactory (QueryGenerationServiceFactory queryGenerationServiceFactory) {
      this.queryGenerationServiceFactory = queryGenerationServiceFactory;
   }

}
