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


package com.execue.core.common.bean.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.execue.core.common.type.StatType;

/**
 * To contain the information of an asset which was considered 
 *    while creation of that asset
 * Stored at AssetExtendedDetail as creationInfo 
 *    in the form of an XML String
 * 
 * @author gopal
 *
 */
public class AssetCreationInfo implements Serializable {

   private List<Long>     dimensionBEDIDs;
   private List<Long>     measureBEDIDs;
   private List<StatType> stats;
   private List<Long>     populationBEDIDs;
   private List<Long>     distributionBEDIDs;

   private Double         confidenceLevel;
   private Double         errorRate;
   private Double         sample;
   private Date           lastModifiedTime;

   public List<Long> getDimensionBEDIDs () {
      return dimensionBEDIDs;
   }

   public void setDimensionBEDIDs (List<Long> dimensionBEDIDs) {
      this.dimensionBEDIDs = dimensionBEDIDs;
   }

   public List<Long> getMeasureBEDIDs () {
      return measureBEDIDs;
   }

   public void setMeasureBEDIDs (List<Long> measureBEDIDs) {
      this.measureBEDIDs = measureBEDIDs;
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

   public Double getSample () {
      return sample;
   }

   public void setSample (Double sample) {
      this.sample = sample;
   }

   public List<Long> getDistributionBEDIDs () {
      return distributionBEDIDs;
   }

   public void setDistributionBEDIDs (List<Long> distributionBEDIDs) {
      this.distributionBEDIDs = distributionBEDIDs;
   }

   public List<StatType> getStats () {
      return stats;
   }

   public void setStats (List<StatType> stats) {
      this.stats = stats;
   }

   public List<Long> getPopulationBEDIDs () {
      return populationBEDIDs;
   }

   public void setPopulationBEDIDs (List<Long> populationBEDIDs) {
      this.populationBEDIDs = populationBEDIDs;
   }

   
   public Date getLastModifiedTime () {
      return lastModifiedTime;
   }

   
   public void setLastModifiedTime (Date lastModifiedTime) {
      this.lastModifiedTime = lastModifiedTime;
   }

}
