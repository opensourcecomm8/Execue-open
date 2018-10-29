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

public class AppBusinessEntityTrend {

   private Long   businessEntityId;
   private String grainConcept;
   private Long   modelGroupId;
   private Long   popularity;

   private Double entityUsageScore;

   public Long getBusinessEntityId () {
      return businessEntityId;
   }

   public void setBusinessEntityId (Long businessEntityId) {
      this.businessEntityId = businessEntityId;
   }

   public String getGrainConcept () {
      return grainConcept;
   }

   public void setGrainConcept (String grainConcept) {
      this.grainConcept = grainConcept;
   }

   public Long getModelGroupId () {
      return modelGroupId;
   }

   public void setModelGroupId (Long modelGroupID) {
      this.modelGroupId = modelGroupID;
   }

   public Long getPopularity () {
      return popularity;
   }

   public void setPopularity (Long popularity) {
      this.popularity = popularity;
   }

}
