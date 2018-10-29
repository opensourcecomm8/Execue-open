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

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Range;

/**
 * 
 * @author jitendra
 *
 */
public class AnswersCatalogMaintenanceRequestInfo {

   private Long         modelId;
   private Long         applicationId;
   private Long         userId;
   private Long         parentAssetId;
   private Long         existingAssetId;
   private Asset        targetAsset;
   private List<String> selectedSimpleLookups;
   private List<Range>  selectedRangeLookups;
   private List<String> prominentDimensions;
   private List<String> prominentMeasures;

   /**
    * @return the modelId
    */
   public Long getModelId () {
      return modelId;
   }

   /**
    * @param modelId the modelId to set
    */
   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   /**
    * @return the userId
    */
   public Long getUserId () {
      return userId;
   }

   /**
    * @param userId the userId to set
    */
   public void setUserId (Long userId) {
      this.userId = userId;
   }

   /**
    * @return the parentAssetId
    */
   public Long getParentAssetId () {
      return parentAssetId;
   }

   /**
    * @param parentAssetId the parentAssetId to set
    */
   public void setParentAssetId (Long parentAssetId) {
      this.parentAssetId = parentAssetId;
   }

   /**
    * @return the existingAssetId
    */
   public Long getExistingAssetId () {
      return existingAssetId;
   }

   /**
    * @param existingAssetId the existingAssetId to set
    */
   public void setExistingAssetId (Long existingAssetId) {
      this.existingAssetId = existingAssetId;
   }

   /**
    * @return the targetAsset
    */
   public Asset getTargetAsset () {
      return targetAsset;
   }

   /**
    * @param targetAsset the targetAsset to set
    */
   public void setTargetAsset (Asset targetAsset) {
      this.targetAsset = targetAsset;
   }

   /**
    * @return the selectedSimpleLookups
    */
   public List<String> getSelectedSimpleLookups () {
      return selectedSimpleLookups;
   }

   /**
    * @param selectedSimpleLookups the selectedSimpleLookups to set
    */
   public void setSelectedSimpleLookups (List<String> selectedSimpleLookups) {
      this.selectedSimpleLookups = selectedSimpleLookups;
   }

   /**
    * @return the selectedRangeLookups
    */
   public List<Range> getSelectedRangeLookups () {
      return selectedRangeLookups;
   }

   /**
    * @param selectedRangeLookups the selectedRangeLookups to set
    */
   public void setSelectedRangeLookups (List<Range> selectedRangeLookups) {
      this.selectedRangeLookups = selectedRangeLookups;
   }

   /**
    * @return the prominentDimensions
    */
   public List<String> getProminentDimensions () {
      return prominentDimensions;
   }

   /**
    * @param prominentDimensions the prominentDimensions to set
    */
   public void setProminentDimensions (List<String> prominentDimensions) {
      this.prominentDimensions = prominentDimensions;
   }

   /**
    * @return the prominentMeasures
    */
   public List<String> getProminentMeasures () {
      return prominentMeasures;
   }

   /**
    * @param prominentMeasures the prominentMeasures to set
    */
   public void setProminentMeasures (List<String> prominentMeasures) {
      this.prominentMeasures = prominentMeasures;
   }

}
