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

import java.util.Iterator;
import java.util.List;

public class AppUsagePopularity {

   private Long                         appId;
   private String                       name;
   private String                       appURL;
   private String                       status;
   private Long                         appImgId;
   private Double                       appConstanFactor;
   private Long                         appPopularity;
   private Long                         businessEntityPopularity;
   private Long                         usetHitsRank;
   private Long                         useEntityRank;
   private Double                       usageScore;
   private Double                       entityUsageScore;
   private Double                       totalUsageScore;
   private List<AppBusinessEntityTrend> businessEntityTrend;

   public Long getAppId () {
      return appId;
   }

   public void setAppId (Long appID) {
      this.appId = appID;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getAppURL () {
      return appURL;
   }

   public void setAppURL (String appURL) {
      this.appURL = appURL;
   }

   public String getStatus () {
      return status;
   }

   public void setStatus (String status) {
      this.status = status;
   }

   public Long getAppImgId () {
      return appImgId;
   }

   public void setAppImgId (Long appImgID) {
      this.appImgId = appImgID;
   }

   public Long getAppPopularity () {
      return appPopularity;
   }

   public void setAppPopularity (Long appPopularity) {
      this.appPopularity = appPopularity;
   }

   public List<AppBusinessEntityTrend> getBusinessEntityTrend () {
      return businessEntityTrend;
   }

   public void setBusinessEntityTrend (List<AppBusinessEntityTrend> businessEntityTrend) {
      this.businessEntityTrend = businessEntityTrend;
   }

   public Long getBusinessEntityPopuloritySum () {
      Long businessEntitySum = new Long("0");
      for (Iterator iterator = businessEntityTrend.iterator(); iterator.hasNext();) {
         AppBusinessEntityTrend businessEntity = (AppBusinessEntityTrend) iterator.next();
         businessEntitySum = businessEntitySum + businessEntity.getPopularity();

      }
      return businessEntitySum;

   }

   public Long getUsetHitsRank () {
      return usetHitsRank;
   }

   public void setUsetHitsRank (Long usetHitsRank) {
      this.usetHitsRank = usetHitsRank;
   }

   public Double getUsageScore () {
      return usageScore;
   }

   public void setUsageScore (Double usageScore) {
      this.usageScore = usageScore;
   }

   public Long getUseEntityRank () {
      return useEntityRank;
   }

   public void setUseEntityRank (Long useEntityRank) {
      this.useEntityRank = useEntityRank;
   }

   public Double getEntityUsageScore () {
      return entityUsageScore;
   }

   public void setEntityUsageScore (Double entityUsageScore) {
      this.entityUsageScore = entityUsageScore;
   }

   public Double getTotalUsageScore () {
      return totalUsageScore;
   }

   public void setTotalUsageScore (Double totalUsageScore) {
      this.totalUsageScore = totalUsageScore;
   }

   public Long getBusinessEntityPopularity () {
      return businessEntityPopularity;
   }

   public void setBusinessEntityPopularity (Long businessEntityPopularity) {
      this.businessEntityPopularity = businessEntityPopularity;
   }

   public Double getAppConstanRandFactor () {
      return appConstanFactor;
   }

   public void setAppConstanRandFactor (Double appConstanFactor) {
      this.appConstanFactor = appConstanFactor;
   }

}
