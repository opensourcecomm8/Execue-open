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


/**
 *
 */
package com.execue.core.common.bean.qdata;

import java.util.Date;

/**
 * @author Nitesh
 */
public class UniversalSearchResult {

   private Long   udxId;
   private Long   rfId;
   private Long   userQueryId;
   private Long   applicationId;
   private Double matchWeight;
   private Double userQueryMaxWeigth;
   private String searchType;
   private Double entityCount;
   private Date   contentDate;

   /**
    * @return the contentDate
    */
   public Date getContentDate () {
      return contentDate;
   }

   /**
    * @param contentDate
    *           the contentDate to set
    */
   public void setContentDate (Date contentDate) {
      this.contentDate = contentDate;
   }

   /**
    * @return the udxId
    */
   public Long getUdxId () {
      return udxId;
   }

   /**
    * @param udxId
    *           the udxId to set
    */
   public void setUdxId (Long udxId) {
      this.udxId = udxId;
   }

   /**
    * @return the rfId
    */
   public Long getRfId () {
      return rfId;
   }

   /**
    * @param rfId
    *           the rfId to set
    */
   public void setRfId (Long rfId) {
      this.rfId = rfId;
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
    * @return the matchWeight
    */
   public Double getMatchWeight () {
      return matchWeight;
   }

   /**
    * @param matchWeight
    *           the matchWeight to set
    */
   public void setMatchWeight (Double matchWeight) {
      this.matchWeight = matchWeight;
   }

   /**
    * @return the userQueryMaxWeigth
    */
   public Double getUserQueryMaxWeigth () {
      return userQueryMaxWeigth;
   }

   /**
    * @param userQueryMaxWeigth
    *           the userQueryMaxWeigth to set
    */
   public void setUserQueryMaxWeigth (Double userQueryMaxWeigth) {
      this.userQueryMaxWeigth = userQueryMaxWeigth;
   }

   /**
    * @return the searchType
    */
   public String getSearchType () {
      return searchType;
   }

   /**
    * @param searchType
    *           the searchType to set
    */
   public void setSearchType (String searchType) {
      this.searchType = searchType;
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
}