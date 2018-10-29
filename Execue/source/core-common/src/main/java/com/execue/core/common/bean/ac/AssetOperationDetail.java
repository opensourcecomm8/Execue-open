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

import java.io.Serializable;

import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.core.common.type.AnswersCatalogOperationType;

/**
 * @author Prasanna
 */
public class AssetOperationDetail implements Serializable {

   private static final long               serialVersionUID = 1L;

   private Long                            id;
   private Long                            jobRequestId;
   private Long                            assetId;
   private Long                            parentAssetId;
   private AnswersCatalogOperationType     assetOperationType;
   private ACManagementOperationStatusType assetOperationStatus;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getJobRequestId () {
      return jobRequestId;
   }

   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public AnswersCatalogOperationType getAssetOperationType () {
      return assetOperationType;
   }

   public void setAssetOperationType (AnswersCatalogOperationType assetOperationType) {
      this.assetOperationType = assetOperationType;
   }

   public ACManagementOperationStatusType getAssetOperationStatus () {
      return assetOperationStatus;
   }

   public void setAssetOperationStatus (ACManagementOperationStatusType assetOperationStatus) {
      this.assetOperationStatus = assetOperationStatus;
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