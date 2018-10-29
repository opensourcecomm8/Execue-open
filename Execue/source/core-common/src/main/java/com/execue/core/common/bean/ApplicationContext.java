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


package com.execue.core.common.bean;

import com.execue.core.common.type.AppSourceType;

public class ApplicationContext {

   private Long          modelId;
   private Long          appId;
   private String        applicationName;
   private String        assetName;
   private Long          assetId;
   private AppSourceType appSourceType;

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public Long getAppId () {
      return appId;
   }

   public void setAppId (Long appId) {
      this.appId = appId;
   }

   public String getApplicationName () {
      return applicationName;
   }

   public void setApplicationName (String applicationName) {
      this.applicationName = applicationName;
   }

   /**
    * @return the assetName
    */
   public String getAssetName () {
      return assetName;
   }

   /**
    * @param assetName
    *           the assetName to set
    */
   public void setAssetName (String assetName) {
      this.assetName = assetName;
   }

   /**
    * @return the assetId
    */
   public Long getAssetId () {
      return assetId;
   }

   /**
    * @param assetId
    *           the assetId to set
    */
   public void setAssetId (Long assetId) {
      this.assetId = assetId;
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
