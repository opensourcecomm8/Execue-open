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


package com.execue.handler.bean;

import java.util.List;

/**
 * This bean represents the grain definition object populated by screen which will be saved to swi
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/07/09
 */
public class UIAssetGrainInfo {

   private List<UIAssetGrain> eligibleGrain;
   private List<UIAssetGrain> existingAssetGrain;
   private UIAssetGrain       defaultPopulationGrain;
   private UIAssetGrain       defaultDistributionGrain;
   private String             defaultDistributionValue;
   private List<UIAssetGrain> assetDefaultMetrics;
   private List<UIAssetGrain> eligibleDefaultMetrics;
   private List<UIAssetGrain> updatedEligibleAssetGrain;

   public List<UIAssetGrain> getEligibleGrain () {
      return eligibleGrain;
   }

   public void setEligibleGrain (List<UIAssetGrain> eligibleGrain) {
      this.eligibleGrain = eligibleGrain;
   }

   public List<UIAssetGrain> getExistingAssetGrain () {
      return existingAssetGrain;
   }

   public void setExistingAssetGrain (List<UIAssetGrain> existingAssetGrain) {
      this.existingAssetGrain = existingAssetGrain;
   }

   public String getDefaultDistributionValue () {
      return defaultDistributionValue;
   }

   public void setDefaultDistributionValue (String defaultDistributionValue) {
      this.defaultDistributionValue = defaultDistributionValue;
   }

   public List<UIAssetGrain> getAssetDefaultMetrics () {
      return assetDefaultMetrics;
   }

   public void setAssetDefaultMetrics (List<UIAssetGrain> assetDefaultMetrics) {
      this.assetDefaultMetrics = assetDefaultMetrics;
   }

   public List<UIAssetGrain> getEligibleDefaultMetrics () {
      return eligibleDefaultMetrics;
   }

   public void setEligibleDefaultMetrics (List<UIAssetGrain> eligibleDefaultMetrics) {
      this.eligibleDefaultMetrics = eligibleDefaultMetrics;
   }

   /**
    * @return the updatedEligibleAssetGrain
    */
   public List<UIAssetGrain> getUpdatedEligibleAssetGrain () {
      return updatedEligibleAssetGrain;
   }

   /**
    * @param updatedEligibleAssetGrain
    *           the updatedEligibleAssetGrain to set
    */
   public void setUpdatedEligibleAssetGrain (List<UIAssetGrain> updatedEligibleAssetGrain) {
      this.updatedEligibleAssetGrain = updatedEligibleAssetGrain;
   }

   public UIAssetGrain getDefaultPopulationGrain () {
      return defaultPopulationGrain;
   }

   public void setDefaultPopulationGrain (UIAssetGrain defaultPopulationGrain) {
      this.defaultPopulationGrain = defaultPopulationGrain;
   }

   public UIAssetGrain getDefaultDistributionGrain () {
      return defaultDistributionGrain;
   }

   public void setDefaultDistributionGrain (UIAssetGrain defaultDistributionGrain) {
      this.defaultDistributionGrain = defaultDistributionGrain;
   }

}
