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


package com.execue.core.common.bean.qdata;

import java.util.Date;

public class QueryCacheResultInfo {

   private Long   userQueryId;

   private Long   businessQueryId;

   private Long   assetId;

   private Double assetWeight;

   private Date   cachedDate;

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

   public Long getUserQueryId () {
      return userQueryId;
   }

   public void setUserQueryId (Long userQueryId) {
      this.userQueryId = userQueryId;
   }

   public Long getBusinessQueryId () {
      return businessQueryId;
   }

   public void setBusinessQueryId (Long businessQueryId) {
      this.businessQueryId = businessQueryId;
   }

   public Double getAssetWeight () {
      return assetWeight;
   }

   public void setAssetWeight (Double assetWeight) {
      this.assetWeight = assetWeight;
   }

   public Date getCachedDate () {
      return cachedDate;
   }

   public void setCachedDate (Date cachedDate) {
      this.cachedDate = cachedDate;
   }
}
