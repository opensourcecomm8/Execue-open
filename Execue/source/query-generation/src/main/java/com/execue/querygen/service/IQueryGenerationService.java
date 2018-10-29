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

import com.execue.core.IService;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryGenerationInput;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;
import com.execue.core.common.bean.querygen.QueryRepresentation;

/**
 * @author Raju Gottumukkala
 */
public interface IQueryGenerationService extends IService {

   /**
    * Check for multiple Queries in order to find out if merging is required or not. If merge the queries else proceed
    * further<br/> If defaultsProcessingRequired is true then apply defaults. Remove duplicates which should eliminate
    * duplicates from all of the entities of query object<br/> From all the individual component of the Query object,
    * gather the tables and populate the FromEntity<br/> If the entries in FromEntity are more than one, then apply
    * joins and optimize the joins by remove unnecessary joins based on the existing where conditions<br/> Remove
    * duplicates which should eliminate duplicates from all of the entities of query object<br/><br/> Output Object is
    * ready to be used for extracting the string representation of the query.
    * 
    * @param queryGenerationInput
    * @return queryGenerationOutput
    */
   public QueryGenerationOutput generateQuery (QueryGenerationInput queryGenerationInput); // facade

   /**
    * This method will extract the query object from the queryGenerationOutput object, create sql clauses for each of
    * the entities and finally append all the clauses to each other in proper format to generate SQL syntax
    * representation of the Query.
    * 
    * @param queryGenerationOutput
    * @return query representation
    */
   public QueryRepresentation extractQueryRepresentation (Asset asset, Query query);

}
