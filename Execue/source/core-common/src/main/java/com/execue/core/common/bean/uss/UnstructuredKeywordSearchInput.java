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
package com.execue.core.common.bean.uss;

import java.util.List;

/**
 * @author Nitesh
 *
 */
public class UnstructuredKeywordSearchInput {

   private List<String> userRequestedLocations;
   private Long         applicationId;
   private Long         userQueryId;
   private String       userQueryTokens;
   private Integer      userQueryDistanceLimit;
   private Integer      maxRecordCount;
   private boolean      isLocationBased;
   private boolean      applyImagePresentFilter;
   private boolean      useDbFunctionForMultipleLocationQuery;
   private String       dbFunctionNameForMultipleLocationQuery;

   /**
    * @return the userRequestedLocations
    */
   public List<String> getUserRequestedLocations () {
      return userRequestedLocations;
   }

   /**
    * @param userRequestedLocations the userRequestedLocations to set
    */
   public void setUserRequestedLocations (List<String> userRequestedLocations) {
      this.userRequestedLocations = userRequestedLocations;
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
    * @return the userQueryId
    */
   public Long getUserQueryId () {
      return userQueryId;
   }

   /**
    * @param userQueryId the userQueryId to set
    */
   public void setUserQueryId (Long userQueryId) {
      this.userQueryId = userQueryId;
   }

   /**
    * @return the userQueryTokens
    */
   public String getUserQueryTokens () {
      return userQueryTokens;
   }

   /**
    * @param userQueryTokens the userQueryTokens to set
    */
   public void setUserQueryTokens (String userQueryTokens) {
      this.userQueryTokens = userQueryTokens;
   }

   /**
    * @return the userQueryDistanceLimit
    */
   public Integer getUserQueryDistanceLimit () {
      return userQueryDistanceLimit;
   }

   /**
    * @param userQueryDistanceLimit the userQueryDistanceLimit to set
    */
   public void setUserQueryDistanceLimit (Integer userQueryDistanceLimit) {
      this.userQueryDistanceLimit = userQueryDistanceLimit;
   }

   /**
    * @return the maxRecordCount
    */
   public Integer getMaxRecordCount () {
      return maxRecordCount;
   }

   /**
    * @param maxRecordCount the maxRecordCount to set
    */
   public void setMaxRecordCount (Integer maxRecordCount) {
      this.maxRecordCount = maxRecordCount;
   }

   /**
    * @return the isLocationBased
    */
   public boolean isLocationBased () {
      return isLocationBased;
   }

   /**
    * @param isLocationBased the isLocationBased to set
    */
   public void setLocationBased (boolean isLocationBased) {
      this.isLocationBased = isLocationBased;
   }

   /**
    * @return the applyImagePresentFilter
    */
   public boolean isApplyImagePresentFilter () {
      return applyImagePresentFilter;
   }

   /**
    * @param applyImagePresentFilter the applyImagePresentFilter to set
    */
   public void setApplyImagePresentFilter (boolean applyImagePresentFilter) {
      this.applyImagePresentFilter = applyImagePresentFilter;
   }

   /**
    * @return the useDbFunctionForMultipleLocationQuery
    */
   public boolean isUseDbFunctionForMultipleLocationQuery () {
      return useDbFunctionForMultipleLocationQuery;
   }

   /**
    * @param useDbFunctionForMultipleLocationQuery the useDbFunctionForMultipleLocationQuery to set
    */
   public void setUseDbFunctionForMultipleLocationQuery (boolean useDbFunctionForMultipleLocationQuery) {
      this.useDbFunctionForMultipleLocationQuery = useDbFunctionForMultipleLocationQuery;
   }

   /**
    * @return the dbFunctionNameForMultipleLocationQuery
    */
   public String getDbFunctionNameForMultipleLocationQuery () {
      return dbFunctionNameForMultipleLocationQuery;
   }

   /**
    * @param dbFunctionNameForMultipleLocationQuery the dbFunctionNameForMultipleLocationQuery to set
    */
   public void setDbFunctionNameForMultipleLocationQuery (String dbFunctionNameForMultipleLocationQuery) {
      this.dbFunctionNameForMultipleLocationQuery = dbFunctionNameForMultipleLocationQuery;
   }

}