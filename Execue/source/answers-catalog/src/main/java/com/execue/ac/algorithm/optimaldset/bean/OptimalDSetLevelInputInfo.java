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


/**
 * @author Vishay
 * @version 1.0
 * @since 18/02/2012
 */
public class OptimalDSetLevelInputInfo {

   private OptimalDSetLevelOutputInfo           previousLevelOutputInfo;
   private Integer                              currentLevelNum;
   private OptimalDSetStaticLevelInputInfo      optimalDSetStaticLevelInputInfo;
   private OptimalDSetSpaceCalculationInputInfo optimalDSetSpaceCalculationInputInfo;

   public OptimalDSetLevelInputInfo (OptimalDSetLevelOutputInfo previousLevelOutputInfo, Integer currentLevelNum,
            OptimalDSetStaticLevelInputInfo optimalDSetStaticLevelInputInfo,
            OptimalDSetSpaceCalculationInputInfo optimalDSetSpaceCalculationInputInfo) {
      super();
      this.previousLevelOutputInfo = previousLevelOutputInfo;
      this.currentLevelNum = currentLevelNum;
      this.optimalDSetStaticLevelInputInfo = optimalDSetStaticLevelInputInfo;
      this.optimalDSetSpaceCalculationInputInfo = optimalDSetSpaceCalculationInputInfo;
   }

   public Integer getCurrentLevelNum () {
      return currentLevelNum;
   }

   public void setCurrentLevelNum (Integer currentLevelNum) {
      this.currentLevelNum = currentLevelNum;
   }

   public OptimalDSetStaticLevelInputInfo getOptimalDSetStaticLevelInputInfo () {
      return optimalDSetStaticLevelInputInfo;
   }

   public void setOptimalDSetStaticLevelInputInfo (OptimalDSetStaticLevelInputInfo optimalDSetStaticLevelInputInfo) {
      this.optimalDSetStaticLevelInputInfo = optimalDSetStaticLevelInputInfo;
   }

   public OptimalDSetSpaceCalculationInputInfo getOptimalDSetSpaceCalculationInputInfo () {
      return optimalDSetSpaceCalculationInputInfo;
   }

   public void setOptimalDSetSpaceCalculationInputInfo (
            OptimalDSetSpaceCalculationInputInfo optimalDSetSpaceCalculationInputInfo) {
      this.optimalDSetSpaceCalculationInputInfo = optimalDSetSpaceCalculationInputInfo;
   }

   public OptimalDSetLevelOutputInfo getPreviousLevelOutputInfo () {
      return previousLevelOutputInfo;
   }

   public void setPreviousLevelOutputInfo (OptimalDSetLevelOutputInfo previousLevelOutputInfo) {
      this.previousLevelOutputInfo = previousLevelOutputInfo;
   }

}
