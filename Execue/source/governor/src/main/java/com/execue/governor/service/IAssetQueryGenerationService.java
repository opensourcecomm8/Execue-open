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

import com.execue.core.common.bean.governor.AssetQuery;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.governor.exception.AssetQueryGenerationException;

/**
 * This interface contains method that converts structured query to assetquery.
 * 
 * @author Raju Gottumukkala
 * @version 1.0
 * @since 10/02/09
 */
public interface IAssetQueryGenerationService {

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
   public AssetQuery generateAssetQuery (StructuredQuery structuredQuery) throws AssetQueryGenerationException;
}
