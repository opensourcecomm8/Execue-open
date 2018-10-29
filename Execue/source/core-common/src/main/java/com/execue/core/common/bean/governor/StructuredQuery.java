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


package com.execue.core.common.bean.governor;

import java.util.List;

import com.execue.core.common.bean.HierarchyTerm;
import com.execue.core.common.bean.entity.Asset;

/**
 * @author Raju Gottumukkala
 */
public class StructuredQuery {

   private Asset                       asset;
   private Long                        modelId;
   private Long                        assetAEDId;
   private List<BusinessAssetTerm>     metrics;
   private List<StructuredCondition>   conditions;
   private List<BusinessAssetTerm>     summarizations;
   private List<StructuredOrderClause> orderClauses;
   private List<StructuredCondition>   havingClauses;
   private List<BusinessAssetTerm>     populations;
   private List<HierarchyTerm>         hierarchies;
   private StructuredLimitClause       topBottom;
   private StructuredQuery             cohort;
   private BusinessAssetTerm           scalingFactor;
   private Double                      relativePriority             = 0D;
   private Double                      structuredQueryWeight        = 0D;
   private Double                      assetWeight                  = 0D;
   private Double                      standarizedApplicationWeight = 0D;
   private Double                      standarizedPossiblityWeight  = 0D;
   private Double                      standarizedAssetWeight       = 0D;
   private Double                      relevance                    = 0D;
   private boolean                     rollupQuery                  = false;

   public Asset getAsset () {
      return asset;
   }

   public void setAsset (Asset asset) {
      this.asset = asset;
   }

   public List<BusinessAssetTerm> getMetrics () {
      return metrics;
   }

   public void setMetrics (List<BusinessAssetTerm> metrics) {
      this.metrics = metrics;
   }

   public List<StructuredCondition> getConditions () {
      return conditions;
   }

   public void setConditions (List<StructuredCondition> conditions) {
      this.conditions = conditions;
   }

   public List<BusinessAssetTerm> getSummarizations () {
      return summarizations;
   }

   public void setSummarizations (List<BusinessAssetTerm> summarizations) {
      this.summarizations = summarizations;
   }

   public List<StructuredOrderClause> getOrderClauses () {
      return orderClauses;
   }

   public void setOrderClauses (List<StructuredOrderClause> orderClauses) {
      this.orderClauses = orderClauses;
   }

   public List<StructuredCondition> getHavingClauses () {
      return havingClauses;
   }

   public void setHavingClauses (List<StructuredCondition> havingClauses) {
      this.havingClauses = havingClauses;
   }

   public Double getStructuredQueryWeight () {
      return structuredQueryWeight;
   }

   public void setStructuredQueryWeight (Double structuredQueryWeight) {
      this.structuredQueryWeight = structuredQueryWeight;
   }

   public List<BusinessAssetTerm> getPopulations () {
      return populations;
   }

   public void setPopulations (List<BusinessAssetTerm> populations) {
      this.populations = populations;
   }

   public StructuredQuery getCohort () {
      return cohort;
   }

   public void setCohort (StructuredQuery cohort) {
      this.cohort = cohort;
   }

   public StructuredLimitClause getTopBottom () {
      return topBottom;
   }

   public void setTopBottom (StructuredLimitClause topBottom) {
      this.topBottom = topBottom;
   }

   public BusinessAssetTerm getScalingFactor () {
      return scalingFactor;
   }

   public void setScalingFactor (BusinessAssetTerm scalingFactor) {
      this.scalingFactor = scalingFactor;
   }

   public Double getRelativePriority () {
      return relativePriority;
   }

   public void setRelativePriority (Double relativePriority) {
      this.relativePriority = relativePriority;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public Long getAssetAEDId () {
      return assetAEDId;
   }

   public void setAssetAEDId (Long assetAEDId) {
      this.assetAEDId = assetAEDId;
   }

   public Double getRelevance () {
      return relevance;
   }

   public void setRelevance (Double relevance) {
      this.relevance = relevance;
   }

   public Double getStandarizedApplicationWeight () {
      return standarizedApplicationWeight;
   }

   public void setStandarizedApplicationWeight (Double standarizedApplicationWeight) {
      this.standarizedApplicationWeight = standarizedApplicationWeight;
   }

   public Double getStandarizedPossiblityWeight () {
      return standarizedPossiblityWeight;
   }

   public void setStandarizedPossiblityWeight (Double standarizedPossiblityWeight) {
      this.standarizedPossiblityWeight = standarizedPossiblityWeight;
   }

   public Double getStandarizedAssetWeight () {
      return standarizedAssetWeight;
   }

   public void setStandarizedAssetWeight (Double standarizedAssetWeight) {
      this.standarizedAssetWeight = standarizedAssetWeight;
   }

   public Double getAssetWeight () {
      return assetWeight;
   }

   public void setAssetWeight (Double assetWeight) {
      this.assetWeight = assetWeight;
   }

   public List<HierarchyTerm> getHierarchies () {
      return hierarchies;
   }

   public void setHierarchies (List<HierarchyTerm> hierarchies) {
      this.hierarchies = hierarchies;
   }

   /**
    * @return the rollupQuery
    */
   public boolean isRollupQuery () {
      return rollupQuery;
   }

   /**
    * @param rollupQuery the rollupQuery to set
    */
   public void setRollupQuery (boolean rollupQuery) {
      this.rollupQuery = rollupQuery;
   }

}