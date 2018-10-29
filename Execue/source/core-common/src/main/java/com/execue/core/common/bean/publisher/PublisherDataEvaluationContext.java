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


package com.execue.core.common.bean.publisher;

import com.execue.core.common.bean.JobRequestIdentifier;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.type.PublisherProcessType;

/**
 * This bean represents the PublisherDataEvaluationContext . It contains information required to invoke the service
 * 
 * @author MurthySN
 * @version 1.0
 * @since 01/10/09
 */
public class PublisherDataEvaluationContext extends JobRequestIdentifier {

   private Long                 applicationId;
   private Long                 userId;
   private Long                 modelId;
   private Asset                asset;
   private Long                 fileInfoId;
   private String               originalFileName;
   private boolean              datasetCollectionCreation = false;
   private PublisherProcessType publisherProcessType;

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

   public Asset getAsset () {
      return asset;
   }

   public void setAsset (Asset asset) {
      this.asset = asset;
   }

   public Long getFileInfoId () {
      return fileInfoId;
   }

   public void setFileInfoId (Long fileInfoId) {
      this.fileInfoId = fileInfoId;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public String getOriginalFileName () {
      return originalFileName;
   }

   public void setOriginalFileName (String originalFileName) {
      this.originalFileName = originalFileName;
   }

   public boolean isDatasetCollectionCreation () {
      return datasetCollectionCreation;
   }

   public void setDatasetCollectionCreation (boolean datasetCollectionCreation) {
      this.datasetCollectionCreation = datasetCollectionCreation;
   }

   public PublisherProcessType getPublisherProcessType () {
      return publisherProcessType;
   }

   public void setPublisherProcessType (PublisherProcessType publisherProcessType) {
      this.publisherProcessType = publisherProcessType;
   }

}
