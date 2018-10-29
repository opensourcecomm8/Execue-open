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

public class AppNewsUsageWeight {

   private long    weightsId;
   private Double  usageWeight1;
   private Double  usageWeight2;
   private Double  newsWeight1;
   private Double  newsWeight2;
   private Double  usage;
   private Double  news;
   private Integer totalApps;
   private Long    globalRandFactor;

   public Long getGlobalRandFactor () {
      return globalRandFactor;
   }

   public void setGlobalRandFactor (Long globalRandFactor) {
      this.globalRandFactor = globalRandFactor;
   }

   public Double getUsageWeight1 () {
      return usageWeight1;
   }

   public void setUsageWeight1 (Double usageWeight1) {
      this.usageWeight1 = usageWeight1;
   }

   public Double getUsageWeight2 () {
      return usageWeight2;
   }

   public void setUsageWeight2 (Double usageWeight2) {
      this.usageWeight2 = usageWeight2;
   }

   public Double getNewsWeight1 () {
      return newsWeight1;
   }

   public void setNewsWeight1 (Double newsWeight1) {
      this.newsWeight1 = newsWeight1;
   }

   public Double getNewsWeight2 () {
      return newsWeight2;
   }

   public void setNewsWeight2 (Double newsWeight2) {
      this.newsWeight2 = newsWeight2;
   }

   public Double getUsage () {
      return usage;
   }

   public void setUsage (Double usage) {
      this.usage = usage;
   }

   public Double getNews () {
      return news;
   }

   public void setNews (Double news) {
      this.news = news;
   }

   public Integer getTotalApps () {
      return totalApps;
   }

   public void setTotalApps (Integer totalApps) {
      this.totalApps = totalApps;
   }

   public long getWeightsId () {
      return weightsId;
   }

   public void setWeightsId (long weightsId) {
      this.weightsId = weightsId;
   }

}
