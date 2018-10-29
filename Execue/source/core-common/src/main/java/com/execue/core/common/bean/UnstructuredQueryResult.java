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


package com.execue.core.common.bean;

import java.util.Set;

import com.execue.core.common.bean.nlp.SemanticPossibility;

public class UnstructuredQueryResult extends QueryResult {

   private Set<SemanticPossibility> semanticPossibilities;
   private Long                     applicationId;
   private Integer                  userQueryFeatureCount;
   private Integer                  userQueryRecordCount;
   private Integer                  userQueryDistanceLimit;
   private String                   colorCodedRequest;
   private boolean                  applyImagePresentFilter;
   private String                   locationDisplayName;
   private boolean                  isMultipleLocation = false;

   /**
    * @return the semanticPossibilities
    */
   public Set<SemanticPossibility> getSemanticPossibilities () {
      return semanticPossibilities;
   }

   /**
    * @param semanticPossibilities the semanticPossibilities to set
    */
   public void setSemanticPossibilities (Set<SemanticPossibility> semanticPossibilities) {
      this.semanticPossibilities = semanticPossibilities;
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
    * @return the colorCodedRequest
    */
   public String getColorCodedRequest () {
      return colorCodedRequest;
   }

   /**
    * @param colorCodedRequest the colorCodedRequest to set
    */
   public void setColorCodedRequest (String colorCodedRequest) {
      this.colorCodedRequest = colorCodedRequest;
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
    * @return the locationDisplayName
    */
   public String getLocationDisplayName () {
      return locationDisplayName;
   }

   /**
    * @param locationDisplayName the locationDisplayName to set
    */
   public void setLocationDisplayName (String locationDisplayName) {
      this.locationDisplayName = locationDisplayName;
   }

   /**
    * @return the isMultipleLocation
    */
   public boolean isMultipleLocation () {
      return isMultipleLocation;
   }

   /**
    * @param isMultipleLocation the isMultipleLocation to set
    */
   public void setMultipleLocation (boolean isMultipleLocation) {
      this.isMultipleLocation = isMultipleLocation;
   }

}