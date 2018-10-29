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


package com.execue.core.common.bean.batchMaintenance;

import com.execue.core.common.bean.JobRequestIdentifier;

/**
 * @author Nitesh
 * @since 29/04/2010
 */
public class InstanceAbsorptionContext extends JobRequestIdentifier {

   private Long    modelId;
   private Long    assetId;
   private Long    columnAedId;
   private Long    conceptBedId;
   private Long    userId;
   private Boolean generateSuggestionMappings; // if false, then save suggestions

   /**
    * @return the modelId
    */
   public Long getModelId () {
      return modelId;
   }

   /**
    * @param modelId
    *           the modelId to set
    */
   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   /**
    * @return the assetId
    */
   public Long getAssetId () {
      return assetId;
   }

   /**
    * @param assetId
    *           the assetId to set
    */
   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   /**
    * @return the columnAedId
    */
   public Long getColumnAedId () {
      return columnAedId;
   }

   /**
    * @param columnAedId
    *           the columnAedId to set
    */
   public void setColumnAedId (Long columnAedId) {
      this.columnAedId = columnAedId;
   }

   /**
    * @return the conceptBedId
    */
   public Long getConceptBedId () {
      return conceptBedId;
   }

   /**
    * @param conceptBedId
    *           the conceptBedId to set
    */
   public void setConceptBedId (Long conceptBedId) {
      this.conceptBedId = conceptBedId;
   }

   /**
    * @return the userId
    */
   public Long getUserId () {
      return userId;
   }

   /**
    * @param userId
    *           the userId to set
    */
   public void setUserId (Long userId) {
      this.userId = userId;
   }

   /**
    * @return the generateSuggestionMappings
    */
   public Boolean isGenerateSuggestionMappings () {
      return generateSuggestionMappings;
   }

   /**
    * @param generateSuggestionMappings
    *           the generateSuggestionMappings to set
    */
   public void setGenerateSuggestionMappings (Boolean generateSuggestionMappings) {
      this.generateSuggestionMappings = generateSuggestionMappings;
   }
}