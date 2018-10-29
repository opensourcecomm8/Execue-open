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


package com.execue.ac.algorithm.optimaldset.bean;

import java.util.List;

/**
 * @author Vishay
 * @version 1.0
 * @since 18/02/2012
 */
public class OptimalDSetStaticLevelInputInfo {

   private Long                                       parentAssetId;
   private Double                                     parentAssetSpace;
   private Double                                     minUsagePercentage;
   private Double                                     maxSpacePercentage;
   private List<OptimalDSetPastUsageDimensionPattern> allPastUsagePatterns;
   private List<OptimalDSetDimensionInfo>             distinctDimensions;

   private boolean                                    applyConstraints;
   private boolean                                    deduceSpaceAtRuntime;

   private Double                                     configuredParentAssetSpace;
   private Integer                                    configuredNumberOfMeasuresInParentAsset;
   private Integer                                    configuredDimensionLookupValueColumnSize;

   public Double getParentAssetSpace () {
      return parentAssetSpace;
   }

   public void setParentAssetSpace (Double parentAssetSpace) {
      this.parentAssetSpace = parentAssetSpace;
   }

   public Long getParentAssetId () {
      return parentAssetId;
   }

   public void setParentAssetId (Long parentAssetId) {
      this.parentAssetId = parentAssetId;
   }

   public List<OptimalDSetPastUsageDimensionPattern> getAllPastUsagePatterns () {
      return allPastUsagePatterns;
   }

   public void setAllPastUsagePatterns (List<OptimalDSetPastUsageDimensionPattern> allPastUsagePatterns) {
      this.allPastUsagePatterns = allPastUsagePatterns;
   }

   public Double getMinUsagePercentage () {
      return minUsagePercentage;
   }

   public void setMinUsagePercentage (Double minUsagePercentage) {
      this.minUsagePercentage = minUsagePercentage;
   }

   public Double getMaxSpacePercentage () {
      return maxSpacePercentage;
   }

   public void setMaxSpacePercentage (Double maxSpacePercentage) {
      this.maxSpacePercentage = maxSpacePercentage;
   }

   public List<OptimalDSetDimensionInfo> getDistinctDimensions () {
      return distinctDimensions;
   }

   public void setDistinctDimensions (List<OptimalDSetDimensionInfo> distinctDimensions) {
      this.distinctDimensions = distinctDimensions;
   }

   public boolean isApplyConstraints () {
      return applyConstraints;
   }

   public void setApplyConstraints (boolean applyConstraints) {
      this.applyConstraints = applyConstraints;
   }

   public boolean isDeduceSpaceAtRuntime () {
      return deduceSpaceAtRuntime;
   }

   public void setDeduceSpaceAtRuntime (boolean deduceSpaceAtRuntime) {
      this.deduceSpaceAtRuntime = deduceSpaceAtRuntime;
   }

   public Double getConfiguredParentAssetSpace () {
      return configuredParentAssetSpace;
   }

   public void setConfiguredParentAssetSpace (Double configuredParentAssetSpace) {
      this.configuredParentAssetSpace = configuredParentAssetSpace;
   }

   public Integer getConfiguredNumberOfMeasuresInParentAsset () {
      return configuredNumberOfMeasuresInParentAsset;
   }

   public void setConfiguredNumberOfMeasuresInParentAsset (Integer configuredNumberOfMeasuresInParentAsset) {
      this.configuredNumberOfMeasuresInParentAsset = configuredNumberOfMeasuresInParentAsset;
   }

   public Integer getConfiguredDimensionLookupValueColumnSize () {
      return configuredDimensionLookupValueColumnSize;
   }

   public void setConfiguredDimensionLookupValueColumnSize (Integer configuredDimensionLookupValueColumnSize) {
      this.configuredDimensionLookupValueColumnSize = configuredDimensionLookupValueColumnSize;
   }

}
