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


package com.execue.core.common.bean.querygen;


/**
 * Output data holder for query generation service
 * @author Jayadev
 *
 */
public class QueryGenerationOutput {

	private QueryGenerationInput queryGenerationInput;
	private Query resultQuery;
   
   public QueryGenerationInput getQueryGenerationInput () {
      return queryGenerationInput;
   }
   
   public void setQueryGenerationInput (QueryGenerationInput queryGenerationInput) {
      this.queryGenerationInput = queryGenerationInput;
   }
   
   public Query getResultQuery () {
      return resultQuery;
   }
   
   public void setResultQuery (Query resultQuery) {
      this.resultQuery = resultQuery;
   }

}
