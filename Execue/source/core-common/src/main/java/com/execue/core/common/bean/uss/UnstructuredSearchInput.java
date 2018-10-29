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

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.qdata.UniversalSearchResultFeatureHeader;

/**
 * @author Nitesh
 *
 */
public class UnstructuredSearchInput {

   private Long                                     applicationId;
   private Long                                     userQueryId;
   private Integer                                  userQueryFeatureCount;
   private Integer                                  userQueryRecordCount;
   private Integer                                  distanceLimit;
   private boolean                                  applyKeyWordSearchFilter;
   private boolean                                  isLocationBased;
   private boolean                                  applyPartialMatchFilter;
   private boolean                                  imagePresent;
   private boolean                                  displayCloseMatchCount;
   private List<String>                             searchResultOrder;
   private List<String>                             userRequestedLocations;
   private List<UniversalSearchResultFeatureHeader> universalSearchResultFeatureHeaders;
   private List<String>                             selectColumnNames;
   private String                                   userRequestedSortOrder;                //this will be used in case of keyword search
   private boolean                                  useDbFunctionForMultipleLocationQuery;
   private String                                   dbFunctionNameForMultipleLocationQuery;

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
    * @return the userQueryFeatureCount
    */
   public Integer getUserQueryFeatureCount () {
      return userQueryFeatureCount;
   }

   /**
    * @param userQueryFeatureCount the userQueryFeatureCount to set
    */
   public void setUserQueryFeatureCount (Integer userQueryFeatureCount) {
      this.userQueryFeatureCount = userQueryFeatureCount;
   }

   /**
    * @return the userQueryRecordCount
    */
   public Integer getUserQueryRecordCount () {
      return userQueryRecordCount;
   }

   /**
    * @param userQueryRecordCount the userQueryRecordCount to set
    */
   public void setUserQueryRecordCount (Integer userQueryRecordCount) {
      this.userQueryRecordCount = userQueryRecordCount;
   }

   /**
    * @return the distanceLimit
    */
   public Integer getDistanceLimit () {
      return distanceLimit;
   }

   /**
    * @param distanceLimit the distanceLimit to set
    */
   public void setDistanceLimit (Integer distanceLimit) {
      this.distanceLimit = distanceLimit;
   }

   /**
    * @return the applyKeyWordSearchFilter
    */
   public boolean isApplyKeyWordSearchFilter () {
      return applyKeyWordSearchFilter;
   }

   /**
    * @param applyKeyWordSearchFilter the applyKeyWordSearchFilter to set
    */
   public void setApplyKeyWordSearchFilter (boolean applyKeyWordSearchFilter) {
      this.applyKeyWordSearchFilter = applyKeyWordSearchFilter;
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
    * @return the applyPartialMatchFilter
    */
   public boolean isApplyPartialMatchFilter () {
      return applyPartialMatchFilter;
   }

   /**
    * @param applyPartialMatchFilter the applyPartialMatchFilter to set
    */
   public void setApplyPartialMatchFilter (boolean applyPartialMatchFilter) {
      this.applyPartialMatchFilter = applyPartialMatchFilter;
   }

   /**
    * @return the imagePresent
    */
   public boolean isImagePresent () {
      return imagePresent;
   }

   /**
    * @param imagePresent the imagePresent to set
    */
   public void setImagePresent (boolean imagePresent) {
      this.imagePresent = imagePresent;
   }

   /**
    * @return the displayCloseMatchCount
    */
   public boolean isDisplayCloseMatchCount () {
      return displayCloseMatchCount;
   }

   /**
    * @param displayCloseMatchCount the displayCloseMatchCount to set
    */
   public void setDisplayCloseMatchCount (boolean displayCloseMatchCount) {
      this.displayCloseMatchCount = displayCloseMatchCount;
   }

   /**
    * @return the searchResultOrder
    */
   public List<String> getSearchResultOrder () {
      if (searchResultOrder == null) {
         searchResultOrder = new ArrayList<String>();
      }
      return searchResultOrder;
   }

   /**
    * @param searchResultOrder the searchResultOrder to set
    */
   public void setSearchResultOrder (List<String> searchResultOrder) {
      this.searchResultOrder = searchResultOrder;
   }

   /**
    * @return the userRequestedLocations
    */
   public List<String> getUserRequestedLocations () {
      if (userRequestedLocations == null) {
         userRequestedLocations = new ArrayList<String>();
      }
      return userRequestedLocations;
   }

   /**
    * @param userRequestedLocations the userRequestedLocations to set
    */
   public void setUserRequestedLocations (List<String> userRequestedLocations) {
      this.userRequestedLocations = userRequestedLocations;
   }

   /**
    * @return the universalSearchResultFeatureHeaders
    */
   public List<UniversalSearchResultFeatureHeader> getUniversalSearchResultFeatureHeaders () {
      return universalSearchResultFeatureHeaders;
   }

   /**
    * @param universalSearchResultFeatureHeaders the universalSearchResultFeatureHeaders to set
    */
   public void setUniversalSearchResultFeatureHeaders (
            List<UniversalSearchResultFeatureHeader> universalSearchResultFeatureHeaders) {
      this.universalSearchResultFeatureHeaders = universalSearchResultFeatureHeaders;
   }

   /**
    * @return the selectColumnNames
    */
   public List<String> getSelectColumnNames () {
      return selectColumnNames;
   }

   /**
    * @param selectColumnNames the selectColumnNames to set
    */
   public void setSelectColumnNames (List<String> selectColumnNames) {
      this.selectColumnNames = selectColumnNames;
   }

   /**
    * @return the userRequestedSortOrder
    */
   public String getUserRequestedSortOrder () {
      return userRequestedSortOrder;
   }

   /**
    * @param userRequestedSortOrder the userRequestedSortOrder to set
    */
   public void setUserRequestedSortOrder (String userRequestedSortOrder) {
      this.userRequestedSortOrder = userRequestedSortOrder;
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