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

import java.util.Date;
import java.util.Set;

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.PublishedFileType;
import com.execue.core.common.type.PublishedOperationType;
import com.execue.core.common.type.PublisherFlowOperationType;
import com.execue.core.common.type.PublisherProcessType;

/**
 * This class represents the PublishedFileInfo object.
 * 
 * @author MurthySN
 * @version 1.0
 * @since 27/10/09
 */
public class PublishedFileInfo implements java.io.Serializable {

   private static final long             serialVersionUID          = 1L;
   private Long                          id;
   private String                        fileName;
   private String                        fileDescription;
   private String                        originalFileName;
   private String                        fileLocation;
   private Long                          userId;
   private Long                          applicationId;
   private Long                          modelId;
   private Long                          datasourceId;
   private Date                          firstAbsorptionDate;
   private Date                          lastAbsorptionDate;
   private CheckType                     operationSuccessful       = CheckType.NO;
   private PublishedOperationType        publishedOperationType    = PublishedOperationType.ADDITION;
   private Long                          evaluationJobRequestId;
   private Long                          absorbtionJobRequestId;
   private PublisherFlowOperationType    currentOperation;
   private JobStatus                     currentOperationStatus;
   private PublishedFileType             sourceType;
   private CheckType                     fileLink                  = CheckType.NO;
   private CheckType                     fileAbsorbed              = CheckType.NO;
   private CheckType                     datasetCollectionCreation = CheckType.NO;
   private Set<PublishedFileTableInfo>   publishedFileTablesInfo;
   private Set<PublishedFileInfoDetails> publishedFileInfoDetails;
   private PublisherProcessType          publisherProcessType;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public Date getFirstAbsorptionDate () {
      return firstAbsorptionDate;
   }

   public void setFirstAbsorptionDate (Date firstAbsorptionDate) {
      this.firstAbsorptionDate = firstAbsorptionDate;
   }

   public Date getLastAbsorptionDate () {
      return lastAbsorptionDate;
   }

   public void setLastAbsorptionDate (Date lastAbsorptionDate) {
      this.lastAbsorptionDate = lastAbsorptionDate;
   }

   public Long getDatasourceId () {
      return datasourceId;
   }

   public void setDatasourceId (Long datasourceId) {
      this.datasourceId = datasourceId;
   }

   public String getFileLocation () {
      return fileLocation;
   }

   public void setFileLocation (String fileLocation) {
      this.fileLocation = fileLocation;
   }

   public PublishedOperationType getPublishedOperationType () {
      return publishedOperationType;
   }

   public void setPublishedOperationType (PublishedOperationType publishedOperationType) {
      this.publishedOperationType = publishedOperationType;
   }

   public Set<PublishedFileTableInfo> getPublishedFileTablesInfo () {
      return publishedFileTablesInfo;
   }

   public void setPublishedFileTablesInfo (Set<PublishedFileTableInfo> publishedFileTablesInfo) {
      this.publishedFileTablesInfo = publishedFileTablesInfo;
   }

   public Set<PublishedFileInfoDetails> getPublishedFileInfoDetails () {
      return publishedFileInfoDetails;
   }

   public void setPublishedFileInfoDetails (Set<PublishedFileInfoDetails> publishedFileInfoDetails) {
      this.publishedFileInfoDetails = publishedFileInfoDetails;
   }

   public String getOriginalFileName () {
      return originalFileName;
   }

   public void setOriginalFileName (String originalFileName) {
      this.originalFileName = originalFileName;
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

   public String getFileName () {
      return fileName;
   }

   public void setFileName (String fileName) {
      this.fileName = fileName;
   }

   public String getFileDescription () {
      return fileDescription;
   }

   public void setFileDescription (String fileDescription) {
      this.fileDescription = fileDescription;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public CheckType getOperationSuccessful () {
      return operationSuccessful;
   }

   public void setOperationSuccessful (CheckType operationSuccessful) {
      this.operationSuccessful = operationSuccessful;
   }

   public CheckType getFileLink () {
      return fileLink;
   }

   public void setFileLink (CheckType fileLink) {
      this.fileLink = fileLink;
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

   public Long getEvaluationJobRequestId () {
      return evaluationJobRequestId;
   }

   public void setEvaluationJobRequestId (Long evaluationJobRequestId) {
      this.evaluationJobRequestId = evaluationJobRequestId;
   }

   public Long getAbsorbtionJobRequestId () {
      return absorbtionJobRequestId;
   }

   public void setAbsorbtionJobRequestId (Long absorbtionJobRequestId) {
      this.absorbtionJobRequestId = absorbtionJobRequestId;
   }

   public PublisherProcessType getPublisherProcessType () {
      return publisherProcessType;
   }

   public void setPublisherProcessType (PublisherProcessType publisherProcessType) {
      this.publisherProcessType = publisherProcessType;
   }

}