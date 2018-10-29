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

public class QDataReducedQuery implements java.io.Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private Long              userQueryId;
   private Long              applicationId;
   private String            reducedQueryString;
   private Double            entityCount;
   private Double            maxMatchWeight;
   private Double            reducedQueryWeight;
   private Double            baseUserQueryWeight;
   private Double            reducedQueryMatchPercent;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the userQueryId
    */
   public Long getUserQueryId () {
      return userQueryId;
   }

   /**
    * @param userQueryId
    *           the userQueryId to set
    */
   public void setUserQueryId (Long userQueryId) {
      this.userQueryId = userQueryId;
   }

   /**
    * @return the reducedQueryString
    */
   public String getReducedQueryString () {
      return reducedQueryString;
   }

   /**
    * @param reducedQueryString
    *           the reducedQueryString to set
    */
   public void setReducedQueryString (String reducedQueryString) {
      this.reducedQueryString = reducedQueryString;
   }

   /**
    * @return the entityCount
    */
   public Double getEntityCount () {
      return entityCount;
   }

   /**
    * @param entityCount
    *           the entityCount to set
    */
   public void setEntityCount (Double entityCount) {
      this.entityCount = entityCount;
   }

   /**
    * @return the maxMatchWeight
    */
   public Double getMaxMatchWeight () {
      return maxMatchWeight;
   }

   /**
    * @param maxMatchWeight
    *           the maxMatchWeight to set
    */
   public void setMaxMatchWeight (Double maxMatchWeight) {
      this.maxMatchWeight = maxMatchWeight;
   }

   /**
    * @return the reducedQueryWeight
    */
   public Double getReducedQueryWeight () {
      return reducedQueryWeight;
   }

   /**
    * @param reducedQueryWeight the reducedQueryWeight to set
    */
   public void setReducedQueryWeight (Double reducedQueryWeight) {
      this.reducedQueryWeight = reducedQueryWeight;
   }

   /**
    * @return the baseUserQueryWeight
    */
   public Double getBaseUserQueryWeight () {
      return baseUserQueryWeight;
   }

   /**
    * @param baseUserQueryWeight the baseUserQueryWeight to set
    */
   public void setBaseUserQueryWeight (Double baseUserQueryWeight) {
      this.baseUserQueryWeight = baseUserQueryWeight;
   }

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   /**
    * @return the reducedQueryMatchPercent
    */
   public Double getReducedQueryMatchPercent () {
      return reducedQueryMatchPercent;
   }

   /**
    * @param reducedQueryMatchPercent the reducedQueryMatchPercent to set
    */
   public void setReducedQueryMatchPercent (Double reducedQueryMatchPercent) {
      this.reducedQueryMatchPercent = reducedQueryMatchPercent;
   }
}