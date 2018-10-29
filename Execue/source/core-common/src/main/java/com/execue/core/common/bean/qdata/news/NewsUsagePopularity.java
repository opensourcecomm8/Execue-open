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


package com.execue.core.common.bean.qdata.news;

public class NewsUsagePopularity {

   private Long   appId;
   private Long   newsHitsPopularity   = 0L;
   private Long   newsEntityPopularity = 0L;
   private Long   usetHitsRank         = 0L;
   private Long   useEntityRank        = 0L;
   private Double usageScore           = 0.0;
   private Double entityUsageScore     = 0.0;
   private Double totalUsageScore      = 0.0; ;

   public Double getTotalUsageScore () {
      return totalUsageScore;
   }

   public void setTotalUsageScore (Double totalUsageScore) {
      this.totalUsageScore = totalUsageScore;
   }

   public Long getNewsHitsPopularity () {
      return newsHitsPopularity;
   }

   public void setNewsHitsPopularity (Long newsHitsPopularity) {
      this.newsHitsPopularity = newsHitsPopularity;
   }

   public Long getNewsEntityPopularity () {
      return newsEntityPopularity;
   }

   public void setNewsEntityPopularity (Long newsEntityPopularity) {
      this.newsEntityPopularity = newsEntityPopularity;
   }

   public Long getUsetHitsRank () {
      return usetHitsRank;
   }

   public void setUsetHitsRank (Long usetHitsRank) {
      this.usetHitsRank = usetHitsRank;
   }

   public Long getUseEntityRank () {
      return useEntityRank;
   }

   public void setUseEntityRank (Long useEntityRank) {
      this.useEntityRank = useEntityRank;
   }

   public Double getUsageScore () {
      return usageScore;
   }

   public void setUsageScore (Double usageScore) {
      this.usageScore = usageScore;
   }

   public Double getEntityUsageScore () {
      return entityUsageScore;
   }

   public void setEntityUsageScore (Double entityUsageScore) {
      this.entityUsageScore = entityUsageScore;
   }

   public Long getAppId () {
      return appId;
   }

   public void setAppId (Long appID) {
      this.appId = appID;
   }

}
