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
public class BasicSamplingAlgorithmStaticInput {

   private Integer numberOfSlices;
   private Double  slicePercentage;
   private Long    defaultNumberOfSlices;
   private Integer minPopulation;
   private Double  maxSamplePercentageOfPopulation;
   private Double  floorMultiplicationFactor;
   private Double  confidenceLevel;
   private Double  errorRate;
   private Double  pLow;
   private Double  pHigh;
   private Double  ldConstantOne;
   private Double  ldConstantTwo;
   private boolean functionUseAlone;
   private boolean floorSettingRequired;

   public Integer getNumberOfSlices () {
      return numberOfSlices;
   }

   public void setNumberOfSlices (Integer numberOfSlices) {
      this.numberOfSlices = numberOfSlices;
   }

   public Double getSlicePercentage () {
      return slicePercentage;
   }

   public void setSlicePercentage (Double slicePercentage) {
      this.slicePercentage = slicePercentage;
   }

   public Long getDefaultNumberOfSlices () {
      return defaultNumberOfSlices;
   }

   public void setDefaultNumberOfSlices (Long defaultNumberOfSlices) {
      this.defaultNumberOfSlices = defaultNumberOfSlices;
   }

   public Integer getMinPopulation () {
      return minPopulation;
   }

   public void setMinPopulation (Integer minPopulation) {
      this.minPopulation = minPopulation;
   }

   public Double getMaxSamplePercentageOfPopulation () {
      return maxSamplePercentageOfPopulation;
   }

   public void setMaxSamplePercentageOfPopulation (Double maxSamplePercentageOfPopulation) {
      this.maxSamplePercentageOfPopulation = maxSamplePercentageOfPopulation;
   }

   public Double getFloorMultiplicationFactor () {
      return floorMultiplicationFactor;
   }

   public void setFloorMultiplicationFactor (Double floorMultiplicationFactor) {
      this.floorMultiplicationFactor = floorMultiplicationFactor;
   }

   public Double getConfidenceLevel () {
      return confidenceLevel;
   }

   public void setConfidenceLevel (Double confidenceLevel) {
      this.confidenceLevel = confidenceLevel;
   }

   public Double getErrorRate () {
      return errorRate;
   }

   public void setErrorRate (Double errorRate) {
      this.errorRate = errorRate;
   }

   public Double getPLow () {
      return pLow;
   }

   public void setPLow (Double low) {
      pLow = low;
   }

   public Double getPHigh () {
      return pHigh;
   }

   public void setPHigh (Double high) {
      pHigh = high;
   }

   public Double getLdConstantOne () {
      return ldConstantOne;
   }

   public void setLdConstantOne (Double ldConstantOne) {
      this.ldConstantOne = ldConstantOne;
   }

   public Double getLdConstantTwo () {
      return ldConstantTwo;
   }

   public void setLdConstantTwo (Double ldConstantTwo) {
      this.ldConstantTwo = ldConstantTwo;
   }

   public boolean isFunctionUseAlone () {
      return functionUseAlone;
   }

   public void setFunctionUseAlone (boolean functionUseAlone) {
      this.functionUseAlone = functionUseAlone;
   }

   public boolean isFloorSettingRequired () {
      return floorSettingRequired;
   }

   public void setFloorSettingRequired (boolean floorSettingRequired) {
      this.floorSettingRequired = floorSettingRequired;
   }

}
