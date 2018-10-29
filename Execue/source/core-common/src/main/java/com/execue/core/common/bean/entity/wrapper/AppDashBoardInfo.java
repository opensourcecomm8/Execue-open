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


package com.execue.core.common.bean.entity.wrapper;

import java.io.Serializable;

import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.type.AppCreationType;
import com.execue.core.common.type.AppOperationType;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.PublishAssetMode;

public class AppDashBoardInfo implements Serializable {

   private Long             appId;
   private String           appName;
   private String           appDescription;
   private StatusEnum       appStatus;
   private PublishAssetMode publishMode;
   private Long             modelId;
   private AppOperationType operationType;
   private Long             jobRequestId;
   private JobStatus        operationStatus;
   private int              assetCount;
   private AppCreationType  creationType;
   private AppSourceType    appSourceType;

   public Long getAppId () {
      return appId;
   }

   public void setAppId (Long appId) {
      this.appId = appId;
   }

   public String getAppName () {
      return appName;
   }

   public void setAppName (String appName) {
      this.appName = appName;
   }

   public String getAppDescription () {
      return appDescription;
   }

   public void setAppDescription (String appDescription) {
      this.appDescription = appDescription;
   }

   public StatusEnum getAppStatus () {
      return appStatus;
   }

   public void setAppStatus (StatusEnum appStatus) {
      this.appStatus = appStatus;
   }

   public PublishAssetMode getPublishMode () {
      return publishMode;
   }

   public void setPublishMode (PublishAssetMode publishMode) {
      this.publishMode = publishMode;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public AppOperationType getOperationType () {
      return operationType;
   }

   public void setOperationType (AppOperationType operationType) {
      this.operationType = operationType;
   }

   public Long getJobRequestId () {
      return jobRequestId;
   }

   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

   public int getAssetCount () {
      return assetCount;
   }

   public void setAssetCount (int assetCount) {
      this.assetCount = assetCount;
   }

   public JobStatus getOperationStatus () {
      return operationStatus;
   }

   public void setOperationStatus (JobStatus operationStatus) {
      this.operationStatus = operationStatus;
   }

   public AppCreationType getCreationType () {
      return creationType;
   }

   public void setCreationType (AppCreationType creationType) {
      this.creationType = creationType;
   }

   /**
    * @return the appSourceType
    */
   public AppSourceType getAppSourceType () {
      return appSourceType;
   }

   /**
    * @param appSourceType
    *           the appSourceType to set
    */
   public void setAppSourceType (AppSourceType appSourceType) {
      this.appSourceType = appSourceType;
   }

}
