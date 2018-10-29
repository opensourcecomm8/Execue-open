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

import java.util.List;

import com.execue.core.common.bean.entity.Asset;

/**
 * Input dataholder for query generation service
 * 
 * @author Jayadev
 */
public class QueryGenerationInput {

   /**
    * Target asset for which the query needs to be generated.<br/> Asset object should poses the Data Source
    * Information in order to identify asset completely
    */
   private Asset       targetAsset;

   
   /**
    * Contains number of queries that needs to be considered for query generation.<br/> Possibility of more than one
    * query could be because of cohort or the caller might be NLP based implementation.
    */
   private List<Query> inputQueries;

   /**
    * Not all the times defaults needs to be applied.<br/> Defaults be applied only when this flag is set
    */
   private boolean     defaultsProcessingRequired = false;

   public Asset getTargetAsset () {
      return targetAsset;
   }

   public void setTargetAsset (Asset targetAsset) {
      this.targetAsset = targetAsset;
   }

   public boolean isDefaultsProcessingRequired () {
      return defaultsProcessingRequired;
   }

   public void setDefaultsProcessingRequired (boolean defaultsProcessingRequired) {
      this.defaultsProcessingRequired = defaultsProcessingRequired;
   }

   public List<Query> getInputQueries () {
      return inputQueries;
   }

   public void setInputQueries (List<Query> inputQueries) {
      this.inputQueries = inputQueries;
   }

}
