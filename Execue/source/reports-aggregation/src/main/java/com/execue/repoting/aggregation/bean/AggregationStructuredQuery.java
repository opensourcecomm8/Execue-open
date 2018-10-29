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


package com.execue.repoting.aggregation.bean;

import java.util.List;

/**
 * @author John Mallavalli
 */
public class AggregationStructuredQuery {

   private Long                               assetId;
   private Long                               modelId;
   private List<AggregationBusinessAssetTerm> metrics;
   private List<AggregationCondition>         conditions;
   private AggregationLimitClause             topBottom;
   private List<AggregationBusinessAssetTerm> populations;
   private AggregationStructuredQuery         cohort;
   private AggregationBusinessAssetTerm       scalingFactor;
   private List<AggregationBusinessAssetTerm> summarizations;
   private List<AggregationOrderClause>       orderClauses;
   private Double                             structuredQueryWeight = 0D;
   private Double                             relevance;
   private boolean                            rollupQuery           = false;

   public Double getRelevance () {
      return relevance;
   }

   public void setRelevance (Double relevance) {
      this.relevance = relevance;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public List<AggregationBusinessAssetTerm> getMetrics () {
      return metrics;
   }

   public void setMetrics (List<AggregationBusinessAssetTerm> metrics) {
      this.metrics = metrics;
   }

   public List<AggregationCondition> getConditions () {
      return conditions;
   }

   public void setConditions (List<AggregationCondition> conditions) {
      this.conditions = conditions;
   }

   public AggregationLimitClause getTopBottom () {
      return topBottom;
   }

   public void setTopBottom (AggregationLimitClause topBottom) {
      this.topBottom = topBottom;
   }

   public List<AggregationBusinessAssetTerm> getPopulations () {
      return populations;
   }

   public void setPopulations (List<AggregationBusinessAssetTerm> populations) {
      this.populations = populations;
   }

   public AggregationStructuredQuery getCohort () {
      return cohort;
   }

   public void setCohort (AggregationStructuredQuery cohort) {
      this.cohort = cohort;
   }

   public AggregationBusinessAssetTerm getScalingFactor () {
      return scalingFactor;
   }

   public void setScalingFactor (AggregationBusinessAssetTerm scalingFactor) {
      this.scalingFactor = scalingFactor;
   }

   public Double getStructuredQueryWeight () {
      return structuredQueryWeight;
   }

   public void setStructuredQueryWeight (Double structuredQueryWeight) {
      this.structuredQueryWeight = structuredQueryWeight;
   }

   public List<AggregationBusinessAssetTerm> getSummarizations () {
      return summarizations;
   }

   public void setSummarizations (List<AggregationBusinessAssetTerm> summarizations) {
      this.summarizations = summarizations;
   }

   public List<AggregationOrderClause> getOrderClauses () {
      return orderClauses;
   }

   public void setOrderClauses (List<AggregationOrderClause> orderClauses) {
      this.orderClauses = orderClauses;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
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