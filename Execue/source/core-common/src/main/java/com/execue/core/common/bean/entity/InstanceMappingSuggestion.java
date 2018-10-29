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

public class InstanceMappingSuggestion implements Serializable {

   private Long   id;

   private Long   applicationId;

   private Long   modelId;

   private Long   modelGroupId;

   private Long   assetAEDId;

   private Long   tablAEDId;

   private String tablDisplayName;

   private Long   columAEDId;

   private String columDisplayName;

   private Long   conceptBEDId;

   private String conceptDisplayName;

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId
    *           the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

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
    * @return the modelGroupId
    */
   public Long getModelGroupId () {
      return modelGroupId;
   }

   /**
    * @param modelGroupId
    *           the modelGroupId to set
    */
   public void setModelGroupId (Long modelGroupId) {
      this.modelGroupId = modelGroupId;
   }

   /**
    * @return the assetAEDId
    */
   public Long getAssetAEDId () {
      return assetAEDId;
   }

   /**
    * @param assetAEDId
    *           the assetAEDId to set
    */
   public void setAssetAEDId (Long assetAEDId) {
      this.assetAEDId = assetAEDId;
   }

   /**
    * @return the tablAEDId
    */
   public Long getTablAEDId () {
      return tablAEDId;
   }

   /**
    * @param tablAEDId
    *           the tablAEDId to set
    */
   public void setTablAEDId (Long tablAEDId) {
      this.tablAEDId = tablAEDId;
   }

   /**
    * @return the tablDisplayName
    */
   public String getTablDisplayName () {
      return tablDisplayName;
   }

   /**
    * @param tablDisplayName
    *           the tablDisplayName to set
    */
   public void setTablDisplayName (String tablDisplayName) {
      this.tablDisplayName = tablDisplayName;
   }

   /**
    * @return the columAEDId
    */
   public Long getColumAEDId () {
      return columAEDId;
   }

   /**
    * @param columAEDId
    *           the columAEDId to set
    */
   public void setColumAEDId (Long columAEDId) {
      this.columAEDId = columAEDId;
   }

   /**
    * @return the columDisplayName
    */
   public String getColumDisplayName () {
      return columDisplayName;
   }

   /**
    * @param columDisplayName
    *           the columDisplayName to set
    */
   public void setColumDisplayName (String columDisplayName) {
      this.columDisplayName = columDisplayName;
   }

   /**
    * @return the conceptBEDId
    */
   public Long getConceptBEDId () {
      return conceptBEDId;
   }

   /**
    * @param conceptBEDId
    *           the conceptBEDId to set
    */
   public void setConceptBEDId (Long conceptBEDId) {
      this.conceptBEDId = conceptBEDId;
   }

   /**
    * @return the conceptDisplayName
    */
   public String getConceptDisplayName () {
      return conceptDisplayName;
   }

   /**
    * @param conceptDisplayName
    *           the conceptDisplayName to set
    */
   public void setConceptDisplayName (String conceptDisplayName) {
      this.conceptDisplayName = conceptDisplayName;
   }

}
