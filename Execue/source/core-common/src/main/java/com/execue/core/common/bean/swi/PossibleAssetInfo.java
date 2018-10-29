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


package com.execue.core.common.bean.swi;

import java.io.Serializable;
import java.util.Date;

import com.execue.core.common.bean.uss.UniversalSearchResultItemType;
import com.execue.core.common.type.AppSourceType;

public class PossibleAssetInfo implements Serializable {

   private Long                         assetId;
   private Long                         modelId;
   private Long                         possibilityId;
   private Double                       totalTypeBasedWeight;
   private Double                       standarizedTotalTypeBasedWeight;
   private Double                       relavance;
   private boolean                      fromQueryCache  = false;
   private Date                         cachedDate;
   private Long                         userQueryId;
   private Long                         businessQueryId;
   private AppSourceType                applicationType = AppSourceType.STRUCTURED;
   private UniversalSearchResultItemType resultItemType;

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public Long getPossibilityId () {
      return possibilityId;
   }

   public void setPossibilityId (Long possibilityId) {
      this.possibilityId = possibilityId;
   }

   public Double getTotalTypeBasedWeight () {
      return totalTypeBasedWeight;
   }

   public void setTotalTypeBasedWeight (Double totalTypeBasedWeight) {
      this.totalTypeBasedWeight = totalTypeBasedWeight;
   }

   public Double getStandarizedTotalTypeBasedWeight () {
      return standarizedTotalTypeBasedWeight;
   }

   public void setStandarizedTotalTypeBasedWeight (Double standarizedTotalTypeBasedWeight) {
      this.standarizedTotalTypeBasedWeight = standarizedTotalTypeBasedWeight;
   }

   /**
    * @return the relavance
    */
   public Double getRelavance () {
      return relavance;
   }

   /**
    * @param relavance
    *           the relavance to set
    */
   public void setRelavance (Double relavance) {
      this.relavance = relavance;
   }

   public boolean isFromQueryCache () {
      return fromQueryCache;
   }

   public void setFromQueryCache (boolean fromQueryCache) {
      this.fromQueryCache = fromQueryCache;
   }

   public Long getUserQueryId () {
      return userQueryId;
   }

   public void setUserQueryId (Long userQueryId) {
      this.userQueryId = userQueryId;
   }

   public Long getBusinessQueryId () {
      return businessQueryId;
   }

   public void setBusinessQueryId (Long businessQueryId) {
      this.businessQueryId = businessQueryId;
   }

   public Date getCachedDate () {
      return cachedDate;
   }

   public void setCachedDate (Date cachedDate) {
      this.cachedDate = cachedDate;
   }

   /**
    * @return the applicationType
    */
   public AppSourceType getApplicationType () {
      return applicationType;
   }

   /**
    * @param applicationType
    *           the applicationType to set
    */
   public void setApplicationType (AppSourceType applicationType) {
      this.applicationType = applicationType;
   }
}