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

import java.util.Set;

import com.execue.core.common.type.BatchType;

/**
 * This class represents the BatchProcess object.
 * 
 * @author MurthySN
 * @version 1.0
 * @since 24/02/10
 */
public class BatchProcess implements java.io.Serializable {

   private static final long       serialVersionUID = 1L;
   private Long                    id;
   private Long                    applicationId;
   private Long                    modelId;
   private Long                    assetId;
   private Long                    jobRequestId;
   private BatchType               batchType;
   private Set<BatchProcessDetail> batchProcessDetails;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

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

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public Long getJobRequestId () {
      return jobRequestId;
   }

   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

   public BatchType getBatchType () {
      return batchType;
   }

   public void setBatchType (BatchType batchType) {
      this.batchType = batchType;
   }

   public Set<BatchProcessDetail> getBatchProcessDetails () {
      return batchProcessDetails;
   }

   public void setBatchProcessDetails (Set<BatchProcessDetail> batchProcessDetails) {
      this.batchProcessDetails = batchProcessDetails;
   }
}
