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

import com.execue.core.common.type.AppSourceType;

/**
 * @author JTiwari
 * @since 13/11/09
 */
public class UIApplicationModelInfo implements Serializable {

   private String        applicationName;
   private Long          applicationId;
   private Long          modelId;
   private String        applicationURL;
   private AppSourceType sourceType = AppSourceType.STRUCTURED;

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

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId
    *           the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   /**
    * @return the modelId
    */
   public Long getModelId () {
      return modelId;
   }

   /**
    * @param modelId
    *           the modelId to set
    */
   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public String getApplicationURL () {
      return applicationURL;
   }

   public void setApplicationURL (String applicationURL) {
      this.applicationURL = applicationURL;
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
