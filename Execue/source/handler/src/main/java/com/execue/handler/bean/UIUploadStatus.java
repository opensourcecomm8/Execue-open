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

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.type.CheckType;

public class UIUploadStatus {

   private boolean      status      = false;
   private List<String> messages;
   private List<String> errorMessages;
   private boolean      absorbAsset = false;
   private Long         jobRequestId;
   private Long         publishedFileInfoId;
   private String       sourceDataFileName;

   private String       fileURL;

   private String       fileName;
   private String       fielDescription;
   private boolean      columnNamesPresent;
   private String       delimiter;
   private String       nullIdentifier;
   private String       stringEnclosure;
   private String       operationType;
   private String       sourceType;
   private String       tag;
   private String       applicationName;
   private CheckType    isCompressedFile;

   /**
    * @return the status
    */
   public boolean isStatus () {
      return status;
   }

   /**
    * @param status
    *           the status to set
    */
   public void setStatus (boolean status) {
      this.status = status;
   }

   /**
    * @return the messages
    */
   public List<String> getMessages () {
      return messages;
   }

   /**
    * @param messages
    *           the messages to set
    */
   public void setMessages (List<String> messages) {
      this.messages = messages;
   }

   /**
    * @param message
    */
   public void addMessage (String message) {
      if (getMessages() == null) {
         this.messages = new ArrayList<String>();
      }
      getMessages().add(message);
   }

   /**
    * @return the errorMessages
    */
   public List<String> getErrorMessages () {
      return errorMessages;
   }

   /**
    * @param errorMessages
    *           the errorMessages to set
    */
   public void setErrorMessages (List<String> errorMessages) {
      this.errorMessages = errorMessages;
   }

   /**
    * @param errorMessage
    */
   public void addErrorMessage (String errorMessage) {
      if (getErrorMessages() == null) {
         this.errorMessages = new ArrayList<String>();
      }
      getErrorMessages().add(errorMessage);
   }

   /**
    * @return the absorbAsset
    */
   public boolean isAbsorbAsset () {
      return absorbAsset;
   }

   /**
    * @param absorbAsset
    *           the absorbAsset to set
    */
   public void setAbsorbAsset (boolean absorbAsset) {
      this.absorbAsset = absorbAsset;
   }

   /**
    * @return the jobRequestId
    */
   public Long getJobRequestId () {
      return jobRequestId;
   }

   /**
    * @param jobRequestId
    *           the jobRequestId to set
    */
   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

   /**
    * @return the publishedFileInfoId
    */
   public Long getPublishedFileInfoId () {
      return publishedFileInfoId;
   }

   /**
    * @param publishedFileInfoId
    *           the publishedFileInfoId to set
    */
   public void setPublishedFileInfoId (Long publishedFileInfoId) {
      this.publishedFileInfoId = publishedFileInfoId;
   }

   /**
    * @return the sourceDataFileName
    */
   public String getSourceDataFileName () {
      return sourceDataFileName;
   }

   /**
    * @param sourceDataFileName
    *           the sourceDataFileName to set
    */
   public void setSourceDataFileName (String sourceDataFileName) {
      this.sourceDataFileName = sourceDataFileName;
   }

   /**
    * @return the fileURL
    */
   public String getFileURL () {
      return fileURL;
   }

   /**
    * @param fileURL
    *           the fileURL to set
    */
   public void setFileURL (String fileURL) {
      this.fileURL = fileURL;
   }

   /**
    * @return the fileName
    */
   public String getFileName () {
      return fileName;
   }

   /**
    * @param fileName
    *           the fileName to set
    */
   public void setFileName (String fileName) {
      this.fileName = fileName;
   }

   /**
    * @return the fielDescription
    */
   public String getFielDescription () {
      return fielDescription;
   }

   /**
    * @param fielDescription
    *           the fielDescription to set
    */
   public void setFielDescription (String fielDescription) {
      this.fielDescription = fielDescription;
   }

   /**
    * @return the columnNamesPresent
    */
   public boolean isColumnNamesPresent () {
      return columnNamesPresent;
   }

   /**
    * @param columnNamesPresent
    *           the columnNamesPresent to set
    */
   public void setColumnNamesPresent (boolean columnNamesPresent) {
      this.columnNamesPresent = columnNamesPresent;
   }

   /**
    * @return the delimiter
    */
   public String getDelimiter () {
      return delimiter;
   }

   /**
    * @param delimiter
    *           the delimiter to set
    */
   public void setDelimiter (String delimiter) {
      this.delimiter = delimiter;
   }

   /**
    * @return the nullIdentifier
    */
   public String getNullIdentifier () {
      return nullIdentifier;
   }

   /**
    * @param nullIdentifier
    *           the nullIdentifier to set
    */
   public void setNullIdentifier (String nullIdentifier) {
      this.nullIdentifier = nullIdentifier;
   }

   /**
    * @return the stringEnclosure
    */
   public String getStringEnclosure () {
      return stringEnclosure;
   }

   /**
    * @param stringEnclosure
    *           the stringEnclosure to set
    */
   public void setStringEnclosure (String stringEnclosure) {
      this.stringEnclosure = stringEnclosure;
   }

   /**
    * @return the operationType
    */
   public String getOperationType () {
      return operationType;
   }

   /**
    * @param operationType
    *           the operationType to set
    */
   public void setOperationType (String operationType) {
      this.operationType = operationType;
   }

   /**
    * @return the sourceType
    */
   public String getSourceType () {
      return sourceType;
   }

   /**
    * @param sourceType
    *           the sourceType to set
    */
   public void setSourceType (String sourceType) {
      this.sourceType = sourceType;
   }

   /**
    * @return the tag
    */
   public String getTag () {
      return tag;
   }

   /**
    * @param tag
    *           the tag to set
    */
   public void setTag (String tag) {
      this.tag = tag;
   }

   /**
    * @return the applicationName
    */
   public String getApplicationName () {
      return applicationName;
   }

   /**
    * @param applicationName
    *           the applicationName to set
    */
   public void setApplicationName (String applicationName) {
      this.applicationName = applicationName;
   }

   public CheckType getIsCompressedFile () {
      return isCompressedFile;
   }

   public void setIsCompressedFile (CheckType isCompressedFile) {
      this.isCompressedFile = isCompressedFile;
   }

}
