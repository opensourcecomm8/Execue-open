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


package com.execue.handler.bean;

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.CurrentPublisherFlowStatusType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.JobType;
import com.execue.core.common.type.PublishedFileType;
import com.execue.core.common.type.PublisherFlowOperationType;
import com.execue.handler.bean.grid.IGridBean;

public class UIPublishedFileInfo implements IGridBean {

   private String                         fileId;
   private String                         fileName;
   private String                         fileDescription;
   private String                         originalFileName;
   private String                         fileLocation;
   private Long                           applicationId;
   private Long                           modelId;
   private Long                           userId;
   private Long                           dataSourceId;
   private Long                           jobRequestId;
   private JobType                        jobType;
   private PublisherFlowOperationType     currentOperation;
   private JobStatus                      currentOperationStatus;
   private PublishedFileType              sourceType;
   private boolean                        eligibleForAbsorption;
   private CheckType                      fileAbsorbed;
   private CheckType                      datasetCollectionCreation = CheckType.NO;
   private CurrentPublisherFlowStatusType status;

   public String getFileId () {
      return fileId;
   }

   public void setFileId (String fileId) {
      this.fileId = fileId;
   }

   public String getFileName () {
      return fileName;
   }

   public void setFileName (String fileName) {
      this.fileName = fileName;
   }

   public String getOriginalFileName () {
      return originalFileName;
   }

   public void setOriginalFileName (String originalFileName) {
      this.originalFileName = originalFileName;
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

   public Long getDataSourceId () {
      return dataSourceId;
   }

   public void setDataSourceId (Long dataSourceId) {
      this.dataSourceId = dataSourceId;
   }

   public Long getJobRequestId () {
      return jobRequestId;
   }

   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

   public JobType getJobType () {
      return jobType;
   }

   public void setJobType (JobType jobType) {
      this.jobType = jobType;
   }

   public PublisherFlowOperationType getCurrentOperation () {
      return currentOperation;
   }

   public void setCurrentOperation (PublisherFlowOperationType currentOperation) {
      this.currentOperation = currentOperation;
   }

   public JobStatus getCurrentOperationStatus () {
      return currentOperationStatus;
   }

   public void setCurrentOperationStatus (JobStatus currentOperationStatus) {
      this.currentOperationStatus = currentOperationStatus;
   }

   public PublishedFileType getSourceType () {
      return sourceType;
   }

   public void setSourceType (PublishedFileType sourceType) {
      this.sourceType = sourceType;
   }

   public String getFileDescription () {
      return fileDescription;
   }

   public void setFileDescription (String fileDescription) {
      this.fileDescription = fileDescription;
   }

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }

   public boolean isEligibleForAbsorption () {
      return eligibleForAbsorption;
   }

   public void setEligibleForAbsorption (boolean eligibleForAbsorption) {
      this.eligibleForAbsorption = eligibleForAbsorption;
   }

   public String getFileLocation () {
      return fileLocation;
   }

   public void setFileLocation (String fileLocation) {
      this.fileLocation = fileLocation;
   }

   public CheckType getFileAbsorbed () {
      return fileAbsorbed;
   }

   public void setFileAbsorbed (CheckType fileAbsorbed) {
      this.fileAbsorbed = fileAbsorbed;
   }

   public CheckType getDatasetCollectionCreation () {
      return datasetCollectionCreation;
   }

   public void setDatasetCollectionCreation (CheckType datasetCollectionCreation) {
      this.datasetCollectionCreation = datasetCollectionCreation;
   }

   /**
    * @return the status
    */
   public CurrentPublisherFlowStatusType getStatus () {
      return status;
   }

   /**
    * @param status
    *           the status to set
    */
   public void setStatus (CurrentPublisherFlowStatusType status) {
      this.status = status;
   }

}
