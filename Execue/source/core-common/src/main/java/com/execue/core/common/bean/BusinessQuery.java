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


package com.execue.core.common.bean;

import java.util.List;

import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.UserInterfaceType;

/**
 * @author Raju Gottumukkala
 */
public class BusinessQuery {

   private List<BusinessTerm>        metrics;
   private List<BusinessCondition>   conditions;
   private List<BusinessTerm>        summarizations;
   private List<BusinessOrderClause> orderClauses;
   private BusinessLimitClause       topBottom;
   private List<BusinessCondition>   havingClauses;
   private List<BusinessTerm>        populations;
   private BusinessQuery             cohort;
   private List<HierarchyTerm>       hierarchies;
   private Double                    businessQueryWeight          = 0D;
   private boolean                   populationDefaulted;                                    // true means user not
   // specified any population
   private Long                      modelId;
   private String                    applicationName;
   private String                    requestRecognition;
   private long                      applicationId;
   private UserInterfaceType         userInterfaceType;
   private Double                    maxPossibleBQWeight          = 0D;
   private Double                    standarizedApplicationWeight = 0D;
   private Double                    standarizedPossiblityWeight  = 0D;
   private AppSourceType             appSourceType                = AppSourceType.STRUCTURED;
   // is true if the business query is scoped by a vertical or app
   private boolean                   scoped                       = false;

   public String getApplicationName () {
      return applicationName;
   }

   public void setApplicationName (String applicationName) {
      this.applicationName = applicationName;
   }

   public String getRequestRecognition () {
      return requestRecognition;
   }

   public void setRequestRecognition (String requestRecognition) {
      this.requestRecognition = requestRecognition;
   }

   public Double getBusinessQueryWeight () {
      return businessQueryWeight;
   }

   public void setBusinessQueryWeight (Double businessQueryWeight) {
      this.businessQueryWeight = businessQueryWeight;
   }

   public List<BusinessTerm> getMetrics () {
      return metrics;
   }

   public void setMetrics (List<BusinessTerm> metrics) {
      this.metrics = metrics;
   }

   public List<BusinessCondition> getConditions () {
      return conditions;
   }

   public void setConditions (List<BusinessCondition> conditions) {
      this.conditions = conditions;
   }

   public List<BusinessTerm> getSummarizations () {
      return summarizations;
   }

   public void setSummarizations (List<BusinessTerm> summarizations) {
      this.summarizations = summarizations;
   }

   public List<BusinessOrderClause> getOrderClauses () {
      return orderClauses;
   }

   public void setOrderClauses (List<BusinessOrderClause> orderClauses) {
      this.orderClauses = orderClauses;
   }

   public List<BusinessCondition> getHavingClauses () {
      return havingClauses;
   }

   public void setHavingClauses (List<BusinessCondition> havingClauses) {
      this.havingClauses = havingClauses;
   }

   public BusinessQuery getCohort () {
      return cohort;
   }

   public void setCohort (BusinessQuery cohort) {
      this.cohort = cohort;
   }

   public List<BusinessTerm> getPopulations () {
      return populations;
   }

   public void setPopulations (List<BusinessTerm> populations) {
      this.populations = populations;
   }

   public BusinessLimitClause getTopBottom () {
      return topBottom;
   }

   public void setTopBottom (BusinessLimitClause topBottom) {
      this.topBottom = topBottom;
   }

   public boolean isPopulationDefaulted () {
      return populationDefaulted;
   }

   public void setPopulationDefaulted (boolean populationDefaulted) {
      this.populationDefaulted = populationDefaulted;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (long applicationId) {
      this.applicationId = applicationId;
   }

   public UserInterfaceType getUserInterfaceType () {
      return userInterfaceType;
   }

   public void setUserInterfaceType (UserInterfaceType userInterfaceType) {
      this.userInterfaceType = userInterfaceType;
   }

   /**
    * @return the standarizedApplicationWeight
    */
   public Double getStandarizedApplicationWeight () {
      return standarizedApplicationWeight;
   }

   /**
    * @param standarizedApplicationWeight
    *           the standarizedApplicationWeight to set
    */
   public void setStandarizedApplicationWeight (Double standarizedApplicationWeight) {
      this.standarizedApplicationWeight = standarizedApplicationWeight;
   }

   /**
    * @return the standarizedPossiblityWeight
    */
   public Double getStandarizedPossiblityWeight () {
      return standarizedPossiblityWeight;
   }

   /**
    * @param standarizedPossiblityWeight
    *           the standarizedPossiblityWeight to set
    */
   public void setStandarizedPossiblityWeight (Double standarizedPossiblityWeight) {
      this.standarizedPossiblityWeight = standarizedPossiblityWeight;
   }

   /**
    * @return the scoped
    */
   public boolean isScoped () {
      return scoped;
   }

   /**
    * @param scoped
    *           the scoped to set
    */
   public void setScoped (boolean scoped) {
      this.scoped = scoped;
   }

   /**
    * @return the appSourceType
    */
   public AppSourceType getAppSourceType () {
      return appSourceType;
   }

   /**
    * @param appSourceType
    *           the appSourceType to set
    */
   public void setAppSourceType (AppSourceType appSourceType) {
      this.appSourceType = appSourceType;
   }

   /**
    * @return the maxPossibleBQWeight
    */
   public Double getMaxPossibleBQWeight () {
      return maxPossibleBQWeight;
   }

   /**
    * @param maxPossibleBQWeight
    *           the maxPossibleBQWeight to set
    */
   public void setMaxPossibleBQWeight (Double maxPossibleBQWeight) {
      this.maxPossibleBQWeight = maxPossibleBQWeight;
   }

   public List<HierarchyTerm> getHierarchies () {
      return hierarchies;
   }

   public void setHierarchies (List<HierarchyTerm> hierarchies) {
      this.hierarchies = hierarchies;
   }

}
