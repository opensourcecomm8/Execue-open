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
public class OptimalDSetMartStaticLevelInputInfo {

   private Long                           parentAssetId;
   private List<OptimalDSetDimensionInfo> pastUsageDimensions;
   private List<OptimalDSetMeasureInfo>   pastUsageMeasures;
   private Integer                        maxEligibleDimensions;
   private Integer                        maxEligibleMeasures;

   public Long getParentAssetId () {
      return parentAssetId;
   }

   public void setParentAssetId (Long parentAssetId) {
      this.parentAssetId = parentAssetId;
   }

   public List<OptimalDSetDimensionInfo> getPastUsageDimensions () {
      return pastUsageDimensions;
   }

   public void setPastUsageDimensions (List<OptimalDSetDimensionInfo> pastUsageDimensions) {
      this.pastUsageDimensions = pastUsageDimensions;
   }

   public List<OptimalDSetMeasureInfo> getPastUsageMeasures () {
      return pastUsageMeasures;
   }

   public void setPastUsageMeasures (List<OptimalDSetMeasureInfo> pastUsageMeasures) {
      this.pastUsageMeasures = pastUsageMeasures;
   }

   public Integer getMaxEligibleDimensions () {
      return maxEligibleDimensions;
   }

   public void setMaxEligibleDimensions (Integer maxEligibleDimensions) {
      this.maxEligibleDimensions = maxEligibleDimensions;
   }

   public Integer getMaxEligibleMeasures () {
      return maxEligibleMeasures;
   }

   public void setMaxEligibleMeasures (Integer maxEligibleMeasures) {
      this.maxEligibleMeasures = maxEligibleMeasures;
   }

}
