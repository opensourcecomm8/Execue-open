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


package com.execue.core.common.bean.ac;

import java.util.List;

import com.execue.core.common.bean.AnswersCatalogManagementQueueIdentifier;
import com.execue.core.common.bean.entity.Asset;

/**
 * This bean represents the mart creation context. It contains information required to create a mart
 * 
 * @author Vishay
 * @version 1.0
 * @since 10/01/11
 */
public class MartCreationContext extends AnswersCatalogManagementQueueIdentifier {

   private Long         applicationId;
   private Long         modelId;
   private Long         userId;
   private Asset        sourceAsset;
   private Asset        targetAsset;
   private String       population;
   private List<String> distributions;
   private List<String> prominentMeasures;
   private List<String> prominentDimensions;
   private boolean      targetDataSourceSameAsSourceDataSource;

   private List<Long>   prominentMeasureBEDIDs;
   private List<Long>   prominentDimensionBEDIDs;

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public List<String> getDistributions () {
      return distributions;
   }

   public void setDistributions (List<String> distributions) {
      this.distributions = distributions;
   }

   public List<String> getProminentMeasures () {
      return prominentMeasures;
   }

   public void setProminentMeasures (List<String> prominentMeasures) {
      this.prominentMeasures = prominentMeasures;
   }

   public List<String> getProminentDimensions () {
      return prominentDimensions;
   }

   public void setProminentDimensions (List<String> prominentDimensions) {
      this.prominentDimensions = prominentDimensions;
   }

   public String getPopulation () {
      return population;
   }

   public void setPopulation (String population) {
      this.population = population;
   }

   public Asset getSourceAsset () {
      return sourceAsset;
   }

   public void setSourceAsset (Asset sourceAsset) {
      this.sourceAsset = sourceAsset;
   }

   public Asset getTargetAsset () {
      return targetAsset;
   }

   public void setTargetAsset (Asset targetAsset) {
      this.targetAsset = targetAsset;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }

   public boolean isTargetDataSourceSameAsSourceDataSource () {
      return targetDataSourceSameAsSourceDataSource;
   }

   public void setTargetDataSourceSameAsSourceDataSource (boolean targetDataSourceSameAsSourceDataSource) {
      this.targetDataSourceSameAsSourceDataSource = targetDataSourceSameAsSourceDataSource;
   }

   public List<Long> getProminentMeasureBEDIDs () {
      return prominentMeasureBEDIDs;
   }

   public void setProminentMeasureBEDIDs (List<Long> prominentMeasureBEDIDs) {
      this.prominentMeasureBEDIDs = prominentMeasureBEDIDs;
   }

   public List<Long> getProminentDimensionBEDIDs () {
      return prominentDimensionBEDIDs;
   }

   public void setProminentDimensionBEDIDs (List<Long> prominentDimensionBEDIDs) {
      this.prominentDimensionBEDIDs = prominentDimensionBEDIDs;
   }

}
