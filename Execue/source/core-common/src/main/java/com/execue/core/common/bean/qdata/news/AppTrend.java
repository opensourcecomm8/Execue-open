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


public class AppTrend {

   private Long                       appId;
   private AppUsagePopularity appUsagePopularity;
   private NewsUsagePopularity        newsUsagePopularity;
   private Double                     appRank;

   public Long getAppId () {
      return appId;
   }

   public void setAppId (Long appID) {
      this.appId = appID;
   }

   public AppUsagePopularity getAppUsagePopularity () {
      return appUsagePopularity;
   }

   public void setAppUsagePopularity (AppUsagePopularity appUsagePopularity) {
      this.appUsagePopularity = appUsagePopularity;
   }

   public NewsUsagePopularity getNewsUsagePopularity () {
      return newsUsagePopularity;
   }

   public void setNewsTrend (NewsUsagePopularity newsUsagePopularity) {
      this.newsUsagePopularity = newsUsagePopularity;
   }

   public Double getAppRank () {
      return appRank;
   }

   public void setAppRank (Double appRank) {
      this.appRank = appRank;
   }

}
