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

import com.execue.core.common.bean.AnswersCatalogManagementQueueIdentifier;

/**
 * This bean represents the answers catalog refresh context
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public class AnswersCatalogUpdationContext extends AnswersCatalogManagementQueueIdentifier {

   private Long applicationId;
   private Long modelId;
   private Long userId;
   private Long existingAssetId;
   private Long parentAssetId;

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

   public Long getExistingAssetId () {
      return existingAssetId;
   }

   public void setExistingAssetId (Long existingAssetId) {
      this.existingAssetId = existingAssetId;
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

}