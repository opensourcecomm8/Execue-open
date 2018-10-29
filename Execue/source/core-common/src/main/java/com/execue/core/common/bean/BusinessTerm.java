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

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.type.QuerySectionType;

/**
 * @author Raju Gottumukkala
 */
public class BusinessTerm {

   private BusinessEntityTerm     businessEntityTerm;
   private BusinessStat           businessStat;
   private boolean                requestedByUser;
   private Double                 businessTermWeight;
   private Range                  range;
   private Long                   profileBusinessEntityDefinitionId;
   // TODO: -RG- need to figure out a better way to get this value without N DB calls for the same profile id
   private String                 profileName;
   private boolean                fromCohort        = false;
   private boolean                fromPopulation    = false;
   private boolean                fromDistribution  = false;
   private INormalizedData        normalizedData;
   private boolean                alternateEntity   = false;
   private List<QuerySectionType> querySectionTypes = new ArrayList<QuerySectionType>();

   public Double getBusinessTermWeight () {
      return businessTermWeight;
   }

   public void setBusinessTermWeight (Double businessTermWeight) {
      this.businessTermWeight = businessTermWeight;
   }

   public BusinessEntityTerm getBusinessEntityTerm () {
      businessEntityTerm.setRequestedByUser(this.requestedByUser);
      businessEntityTerm.setAlternateEntity(this.alternateEntity);
      return businessEntityTerm;
   }

   public void setBusinessEntityTerm (BusinessEntityTerm businessEntityTerm) {
      this.businessEntityTerm = businessEntityTerm;
   }

   public BusinessStat getBusinessStat () {
      return businessStat;
   }

   public void setBusinessStat (BusinessStat businessStat) {
      this.businessStat = businessStat;
   }

   public boolean isRequestedByUser () {
      return requestedByUser;
   }

   public void setRequestedByUser (boolean requestedByUser) {
      this.requestedByUser = requestedByUser;
   }

   public Range getRange () {
      return range;
   }

   public void setRange (Range range) {
      this.range = range;
   }

   public Long getProfileDomainEntityDefinitionId () {
      return profileBusinessEntityDefinitionId;
   }

   public void setProfileBusinessEntityDefinitionId (Long profileBusinessEntityDefinitionId) {
      this.profileBusinessEntityDefinitionId = profileBusinessEntityDefinitionId;
   }

   public boolean isFromCohort () {
      return fromCohort;
   }

   public void setFromCohort (boolean fromCohort) {
      this.fromCohort = fromCohort;
   }

   public boolean isFromPopulation () {
      return fromPopulation;
   }

   public void setFromPopulation (boolean fromPopulation) {
      this.fromPopulation = fromPopulation;
   }

   public boolean isFromDistribution () {
      return fromDistribution;
   }

   public void setFromDistribution (boolean fromDistribution) {
      this.fromDistribution = fromDistribution;
   }

   /**
    * @return the normalizedData
    */
   public INormalizedData getNormalizedData () {
      return normalizedData;
   }

   /**
    * @param normalizedData
    *           the normalizedData to set
    */
   public void setNormalizedData (INormalizedData normalizedData) {
      this.normalizedData = normalizedData;
   }

   public boolean isAlternateEntity () {
      return alternateEntity;
   }

   public void setAlternateEntity (boolean alternateEntity) {
      this.alternateEntity = alternateEntity;
   }

   public String getProfileName () {
      return profileName;
   }

   public void setProfileName (String profileName) {
      this.profileName = profileName;
   }

   public List<QuerySectionType> getQuerySectionTypes () {
      return querySectionTypes;
   }

   public void setQuerySectionTypes (List<QuerySectionType> querySectionTypes) {
      this.querySectionTypes = querySectionTypes;
   }

}