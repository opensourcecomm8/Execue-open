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


package com.execue.das.solr.bean;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.util.ExecueCoreUtil;

/**
 * @author Vishay
 */
public class SolrFacetQueryInput {

   private List<SolrConditionEntity>   queryConditions;
   private List<SolrConditionEntity>   filterQueryConditions;
   private SolrDistanceConditionEntity distanceCondition;
   private List<SolrFacetFieldEntity>  facetFields;
   private List<SolrFacetQueryEntity>  facetQueries;

   public List<SolrConditionEntity> getQueryConditions () {
      return queryConditions;
   }

   public void setQueryConditions (List<SolrConditionEntity> queryConditions) {
      this.queryConditions = queryConditions;
   }

   public void addQueryCondition (SolrConditionEntity conditionEntity) {
      if (conditionEntity != null) {
         if (ExecueCoreUtil.isCollectionEmpty(queryConditions)) {
            queryConditions = new ArrayList<SolrConditionEntity>();
         }
         queryConditions.add(conditionEntity);
      }
   }

   public void addQueryConditions (List<SolrConditionEntity> conditionEntities) {
      if (ExecueCoreUtil.isCollectionEmpty(queryConditions)) {
         queryConditions = new ArrayList<SolrConditionEntity>();
      }
      queryConditions.addAll(conditionEntities);
   }

   public List<SolrConditionEntity> getFilterQueryConditions () {
      return filterQueryConditions;
   }

   public void setFilterQueryConditions (List<SolrConditionEntity> filterQueryConditions) {
      this.filterQueryConditions = filterQueryConditions;
   }

   public SolrDistanceConditionEntity getDistanceCondition () {
      return distanceCondition;
   }

   public void setDistanceCondition (SolrDistanceConditionEntity distanceCondition) {
      this.distanceCondition = distanceCondition;
   }

   public List<SolrFacetFieldEntity> getFacetFields () {
      return facetFields;
   }

   public void setFacetFields (List<SolrFacetFieldEntity> facetFields) {
      this.facetFields = facetFields;
   }

   public List<SolrFacetQueryEntity> getFacetQueries () {
      return facetQueries;
   }

   public void setFacetQueries (List<SolrFacetQueryEntity> facetQueries) {
      this.facetQueries = facetQueries;
   }

}
