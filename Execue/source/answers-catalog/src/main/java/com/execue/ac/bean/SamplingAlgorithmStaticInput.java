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


package com.execue.ac.bean;

/**
 * This bean contains the input needed for sampling algorithm which is same for whole of the mart creation process.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class SamplingAlgorithmStaticInput {

   private Double minSamplePercentageOfPopulation;
   private Double maxSamplePercentageOfPopulationAllowed;
   private Double confidenceLevelPercentage;
   private Double errorRatePercentage;
   private Double confidenceLevelValue;
   private Double ErrorRateValue;
   private Double zValue;
   private Double subGroupingMinPopulationPercentageRequired;
   private Double subGroupingMaxCoefficientOfVarianceAllowed;
   private Double subGroupingMaxCoefficientOfVarianceAllowedBetweenTwoGroups;

   public Double getMinSamplePercentageOfPopulation () {
      return minSamplePercentageOfPopulation;
   }

   public void setMinSamplePercentageOfPopulation (Double minSamplePercentageOfPopulation) {
      this.minSamplePercentageOfPopulation = minSamplePercentageOfPopulation;
   }

   public Double getMaxSamplePercentageOfPopulationAllowed () {
      return maxSamplePercentageOfPopulationAllowed;
   }

   public void setMaxSamplePercentageOfPopulationAllowed (Double maxSamplePercentageOfPopulationAllowed) {
      this.maxSamplePercentageOfPopulationAllowed = maxSamplePercentageOfPopulationAllowed;
   }

   public Double getConfidenceLevelPercentage () {
      return confidenceLevelPercentage;
   }

   public void setConfidenceLevelPercentage (Double confidenceLevelPercentage) {
      this.confidenceLevelPercentage = confidenceLevelPercentage;
   }

   public Double getErrorRatePercentage () {
      return errorRatePercentage;
   }

   public void setErrorRatePercentage (Double errorRatePercentage) {
      this.errorRatePercentage = errorRatePercentage;
   }

   public Double getConfidenceLevelValue () {
      return confidenceLevelValue;
   }

   public void setConfidenceLevelValue (Double confidenceLevelValue) {
      this.confidenceLevelValue = confidenceLevelValue;
   }

   public Double getErrorRateValue () {
      return ErrorRateValue;
   }

   public void setErrorRateValue (Double errorRateValue) {
      ErrorRateValue = errorRateValue;
   }

   public Double getZValue () {
      return zValue;
   }

   public void setZValue (Double value) {
      zValue = value;
   }

   public Double getSubGroupingMinPopulationPercentageRequired () {
      return subGroupingMinPopulationPercentageRequired;
   }

   public void setSubGroupingMinPopulationPercentageRequired (Double subGroupingMinPopulationPercentageRequired) {
      this.subGroupingMinPopulationPercentageRequired = subGroupingMinPopulationPercentageRequired;
   }

   public Double getSubGroupingMaxCoefficientOfVarianceAllowed () {
      return subGroupingMaxCoefficientOfVarianceAllowed;
   }

   public void setSubGroupingMaxCoefficientOfVarianceAllowed (Double subGroupingMaxCoefficientOfVarianceAllowed) {
      this.subGroupingMaxCoefficientOfVarianceAllowed = subGroupingMaxCoefficientOfVarianceAllowed;
   }

   public Double getSubGroupingMaxCoefficientOfVarianceAllowedBetweenTwoGroups () {
      return subGroupingMaxCoefficientOfVarianceAllowedBetweenTwoGroups;
   }

   public void setSubGroupingMaxCoefficientOfVarianceAllowedBetweenTwoGroups (
            Double subGroupingMaxCoefficientOfVarianceAllowedBetweenTwoGroups) {
      this.subGroupingMaxCoefficientOfVarianceAllowedBetweenTwoGroups = subGroupingMaxCoefficientOfVarianceAllowedBetweenTwoGroups;
   }
}
