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
import java.util.Map;

import com.execue.core.common.bean.AnswersCatalogManagementQueueIdentifier;
import com.execue.core.common.bean.entity.Asset;

/**
 * This bean represents the cube creation context. It contains information required to create a cube
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public class CubeCreationContext extends AnswersCatalogManagementQueueIdentifier {

   private Long                         applicationId;
   private Long                         modelId;
   private Long                         userId;
   private Asset                        sourceAsset;
   private Asset                        targetAsset;
   private List<String>                 frequencyMeasures;
   private List<String>                 simpleLookupDimensions;
   private List<CubeRangeDimensionInfo> rangeLookupDimensions;
   private List<String>                 measures;
   private boolean                      targetDataSourceSameAsSourceDataSource;
   private List<Long>                   simpleLookupDimensionBEDIDs;
   private Map<Long, List<Long>>        rangeLookupDimensionBEDIDs;

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
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

   public List<String> getFrequencyMeasures () {
      return frequencyMeasures;
   }

   public void setFrequencyMeasures (List<String> frequencyMeasures) {
      this.frequencyMeasures = frequencyMeasures;
   }

   public List<String> getSimpleLookupDimensions () {
      return simpleLookupDimensions;
   }

   public void setSimpleLookupDimensions (List<String> simpleLookupDimensions) {
      this.simpleLookupDimensions = simpleLookupDimensions;
   }

   public List<String> getMeasures () {
      return measures;
   }

   public void setMeasures (List<String> measures) {
      this.measures = measures;
   }

   public boolean isTargetDataSourceSameAsSourceDataSource () {
      return targetDataSourceSameAsSourceDataSource;
   }

   public void setTargetDataSourceSameAsSourceDataSource (boolean targetDataSourceSameAsSourceDataSource) {
      this.targetDataSourceSameAsSourceDataSource = targetDataSourceSameAsSourceDataSource;
   }

   public List<Long> getSimpleLookupDimensionBEDIDs () {
      return simpleLookupDimensionBEDIDs;
   }

   public void setSimpleLookupDimensionBEDIDs (List<Long> simpleLookupDimensionBEDIDs) {
      this.simpleLookupDimensionBEDIDs = simpleLookupDimensionBEDIDs;
   }

   public Map<Long, List<Long>> getRangeLookupDimensionBEDIDs () {
      return rangeLookupDimensionBEDIDs;
   }

   public void setRangeLookupDimensionBEDIDs (Map<Long, List<Long>> rangeLookupDimensionBEDIDs) {
      this.rangeLookupDimensionBEDIDs = rangeLookupDimensionBEDIDs;
   }

   public List<CubeRangeDimensionInfo> getRangeLookupDimensions () {
      return rangeLookupDimensions;
   }

   public void setRangeLookupDimensions (List<CubeRangeDimensionInfo> rangeLookupDimensions) {
      this.rangeLookupDimensions = rangeLookupDimensions;
   }

}