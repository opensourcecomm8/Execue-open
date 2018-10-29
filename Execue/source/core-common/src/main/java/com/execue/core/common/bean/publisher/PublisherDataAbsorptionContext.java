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
import com.execue.core.common.type.CSVEmptyField;
import com.execue.core.common.type.CSVStringEnclosedCharacterType;
import com.execue.core.common.type.PublishedFileType;
import com.execue.core.common.type.PublishedOperationType;
import com.execue.core.common.type.PublisherProcessType;

/**
 * This bean represents the PublisherDataAbsorptionContext . It contains information required to invoke the service
 * 
 * @author MurthySN
 * @version 1.0
 * @since 01/10/09
 */
public class PublisherDataAbsorptionContext extends JobRequestIdentifier {

   private String                         filePath;
   private boolean                        isColumnsAvailable;
   private Long                           userId;
   private Long                           applicationId;
   private Long                           modelId;
   private String                         dataDelimeter;
   private CSVEmptyField                  csvEmptyField;
   private CSVStringEnclosedCharacterType csvStringEnclosedCharacterType;
   private PublishedOperationType         publishedOperationType;
   private Long                           fileInfoId;
   private String                         originalFileName;
   private String                         fileName;
   private String                         fileDescription;
   private PublishedFileType              sourceType;
   private boolean                        datasetCollectionCreation = false;
   private PublisherProcessType           publisherProcessType;

   public String getFilePath () {
      return filePath;
   }

   public void setFilePath (String filePath) {
      this.filePath = filePath;
   }

   public boolean isColumnsAvailable () {
      return isColumnsAvailable;
   }

   public void setColumnsAvailable (boolean isColumnsAvailable) {
      this.isColumnsAvailable = isColumnsAvailable;
   }

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }

   public CSVEmptyField getCsvEmptyField () {
      return csvEmptyField;
   }

   public void setCsvEmptyField (CSVEmptyField csvEmptyField) {
      this.csvEmptyField = csvEmptyField;
   }

   public CSVStringEnclosedCharacterType getCsvStringEnclosedCharacterType () {
      return csvStringEnclosedCharacterType;
   }

   public void setCsvStringEnclosedCharacterType (CSVStringEnclosedCharacterType csvStringEnclosedCharacterType) {
      this.csvStringEnclosedCharacterType = csvStringEnclosedCharacterType;
   }

   public PublishedOperationType getPublishedOperationType () {
      return publishedOperationType;
   }

   public void setPublishedOperationType (PublishedOperationType publishedOperationType) {
      this.publishedOperationType = publishedOperationType;
   }

   public String getOriginalFileName () {
      return originalFileName;
   }

   public void setOriginalFileName (String originalFileName) {
      this.originalFileName = originalFileName;
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

   public Long getFileInfoId () {
      return fileInfoId;
   }

   public void setFileInfoId (Long fileInfoId) {
      this.fileInfoId = fileInfoId;
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

   
   public String getDataDelimeter () {
      return dataDelimeter;
   }

   
   public void setDataDelimeter (String dataDelimeter) {
      this.dataDelimeter = dataDelimeter;
   }

}
