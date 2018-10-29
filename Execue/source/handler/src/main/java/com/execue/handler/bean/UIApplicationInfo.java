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

import java.io.Serializable;
import java.util.List;

import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.PublishAssetMode;

public class UIApplicationInfo implements Serializable {

   private Long                       applicationId;
   private String                     applicationName;
   private String                     applicationDescription;
   private String                     applicationURL;
   private Long                       applicationImageId;
   private String                     publisherName;
   private Long                       modelId;
   private String                     completeAppDescription;
   private List<UIApplicationExample> appExamples;
   private String                     applicationTag;
   private String                     applicationTitle;
   private PublishAssetMode           publishMode;
   private AppSourceType              sourceType = AppSourceType.STRUCTURED;

   public Long getApplicationImageId () {
      return applicationImageId;
   }

   public void setApplicationImageId (Long applicationImageId) {
      this.applicationImageId = applicationImageId;
   }

   public String getApplicationName () {
      return applicationName;
   }

   public void setApplicationName (String applicationName) {
      this.applicationName = applicationName;
   }

   public String getApplicationDescription () {
      return applicationDescription;
   }

   public void setApplicationDescription (String applicationDescription) {
      this.applicationDescription = applicationDescription;
   }

   public String getApplicationURL () {
      return applicationURL;
   }

   public void setApplicationURL (String applicationURL) {
      this.applicationURL = applicationURL;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public String getPublisherName () {
      return publisherName;
   }

   public void setPublisherName (String publisherName) {
      this.publisherName = publisherName;
   }

   public List<UIApplicationExample> getAppExamples () {
      return appExamples;
   }

   public void setAppExamples (List<UIApplicationExample> appExamples) {
      this.appExamples = appExamples;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public String getCompleteAppDescription () {
      return completeAppDescription;
   }

   public void setCompleteAppDescription (String completeAppDescription) {
      this.completeAppDescription = completeAppDescription;
   }

   public String getApplicationTag () {
      return applicationTag;
   }

   public void setApplicationTag (String applicationTag) {
      this.applicationTag = applicationTag;
   }

   public String getApplicationTitle () {
      return applicationTitle;
   }

   public void setApplicationTitle (String applicationTitle) {
      this.applicationTitle = applicationTitle;
   }

   public PublishAssetMode getPublishMode () {
      return publishMode;
   }

   public void setPublishMode (PublishAssetMode publishMode) {
      this.publishMode = publishMode;
   }

   /**
    * @return the sourceType
    */
   public AppSourceType getSourceType () {
      return sourceType;
   }

   /**
    * @param sourceType the sourceType to set
    */
   public void setSourceType (AppSourceType sourceType) {
      this.sourceType = sourceType;
   }

}
