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
 * 
 * @author jitendra
 *
 */

public class UIAssetCreationInfo {

   private List<String> dimensions;
   private List<String> measures;
   private List<String> stats;
   private String       population;
   private String       distribution;

   private Double       confidenceLevel;
   private Double       errorRate;
   private Double       sample;
   private boolean      isDataAvailable = false; // just for a check in UI
   private String       lastModifiedDate;

   /**
    * @return the dimensions
    */
   public List<String> getDimensions () {
      return dimensions;
   }

   /**
    * @param dimensions the dimensions to set
    */
   public void setDimensions (List<String> dimensions) {
      this.dimensions = dimensions;
   }

   /**
    * @return the measures
    */
   public List<String> getMeasures () {
      return measures;
   }

   /**
    * @param measures the measures to set
    */
   public void setMeasures (List<String> measures) {
      this.measures = measures;
   }

   /**
    * @return the stats
    */
   public List<String> getStats () {
      return stats;
   }

   /**
    * @param stats the stats to set
    */
   public void setStats (List<String> stats) {
      this.stats = stats;
   }

   //   /**
   //    * @return the population
   //    */
   //   public String getPopulation () {
   //      return population;
   //   }
   //
   //   /**
   //    * @param population the population to set
   //    */
   //   public void setPopulation (String population) {
   //      this.population = population;
   //   }

   /**
    * @return the confidenceLevel
    */
   public Double getConfidenceLevel () {
      return confidenceLevel;
   }

   /**
    * @param confidenceLevel the confidenceLevel to set
    */
   public void setConfidenceLevel (Double confidenceLevel) {
      this.confidenceLevel = confidenceLevel;
   }

   /**
    * @return the errorRate
    */
   public Double getErrorRate () {
      return errorRate;
   }

   /**
    * @param errorRate the errorRate to set
    */
   public void setErrorRate (Double errorRate) {
      this.errorRate = errorRate;
   }

   /**
    * @return the sample
    */
   public Double getSample () {
      return sample;
   }

   /**
    * @param sample the sample to set
    */
   public void setSample (Double sample) {
      this.sample = sample;
   }

   /**
    * @return the population
    */
   public String getPopulation () {
      return population;
   }

   /**
    * @param population the population to set
    */
   public void setPopulation (String population) {
      this.population = population;
   }

   /**
    * @return the distribution
    */
   public String getDistribution () {
      return distribution;
   }

   /**
    * @param distribution the distribution to set
    */
   public void setDistribution (String distribution) {
      this.distribution = distribution;
   }

   /**
    * @return the isDataAvailable
    */
   public boolean isDataAvailable () {
      return isDataAvailable;
   }

   /**
    * @param isDataAvailable the isDataAvailable to set
    */
   public void setDataAvailable (boolean isDataAvailable) {
      this.isDataAvailable = isDataAvailable;
   }

   
   public String getLastModifiedDate () {
      return lastModifiedDate;
   }

   
   public void setLastModifiedDate (String lastModifiedDate) {
      this.lastModifiedDate = lastModifiedDate;
   }
}
