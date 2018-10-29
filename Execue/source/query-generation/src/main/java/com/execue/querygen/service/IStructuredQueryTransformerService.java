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


package com.execue.querygen.service;

import java.util.Map;

import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;

public interface IStructuredQueryTransformerService {

   /**
    * Construct the Query generation output object based on the asset properties in order to provide input to the query
    * generation service. This method will populate Query object using the information stored in StructuredQuery. For
    * putting the aliases for query object, we have used a counter called queryAliasCounter along with string "OQ" -
    * outer query.
    * 
    * @param structuredQuery
    * @param aliasBusinessAssetTermMap
    * @return queryGenerationOutput
    */
   public QueryGenerationOutput populateQueryGenerationOutput (StructuredQuery structuredQuery,
            Map<String, BusinessAssetTerm> aliasBusinessAssetTermMap);
}
