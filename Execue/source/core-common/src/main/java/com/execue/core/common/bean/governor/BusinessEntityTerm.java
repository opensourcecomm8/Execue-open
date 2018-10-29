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


package com.execue.core.common.bean.governor;

import com.execue.core.common.bean.IBusinessEntity;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;

public class BusinessEntityTerm {

   private IBusinessEntity    businessEntity;
   private BusinessEntityType businessEntityType;
   private Long               businessEntityDefinitionId;
   private boolean            requestedByUser;
   private boolean            alternateEntity             = false;
   private boolean            baseGroupEntity             = false;
   private boolean            measurableEntity            = false;
   private CheckType          measureGroupBy              = CheckType.NO;
   private CheckType          measureConditionWithoutStat = CheckType.NO;
   private boolean            dependantMeasure            = false;

   @Override
   public String toString () {
      if (BusinessEntityType.CONCEPT.equals(this.businessEntityType)) {
         Concept concept = (Concept) this.businessEntity;
      } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(this.businessEntityType)) {
         Instance instance = (Instance) this.businessEntity;
      }
      return super.toString();
   }

   @Override
   public boolean equals (Object obj) {
      // TODO: -VG- need to take care of profiles and formula
      boolean isEqual = false;
      if (obj instanceof BusinessEntityTerm) {
         BusinessEntityTerm businessEntityTerm = (BusinessEntityTerm) obj;
         if (this.businessEntityType.equals(businessEntityTerm.getBusinessEntityType())) {
            if (this.businessEntityDefinitionId.longValue() == businessEntityTerm.getBusinessEntityDefinitionId()
                     .longValue()) {
               isEqual = true;
            }
         }
      }
      return isEqual;
   }

   @Override
   public int hashCode () {
      return this.businessEntityDefinitionId.intValue();
   }

   public IBusinessEntity getBusinessEntity () {
      return businessEntity;
   }

   public void setBusinessEntity (IBusinessEntity businessEntity) {
      this.businessEntity = businessEntity;
   }

   public BusinessEntityType getBusinessEntityType () {
      return businessEntityType;
   }

   public void setBusinessEntityType (BusinessEntityType businessEntityType) {
      this.businessEntityType = businessEntityType;
   }

   public Long getBusinessEntityDefinitionId () {
      return businessEntityDefinitionId;
   }

   public void setBusinessEntityDefinitionId (Long businessEntityDefinitionId) {
      this.businessEntityDefinitionId = businessEntityDefinitionId;
   }

   /**
    * @return the requestedByUser
    */
   public boolean isRequestedByUser () {
      return requestedByUser;
   }

   /**
    * @param requestedByUser
    *           the requestedByUser to set
    */
   public void setRequestedByUser (boolean requestedByUser) {
      this.requestedByUser = requestedByUser;
   }

   public boolean isAlternateEntity () {
      return alternateEntity;
   }

   public void setAlternateEntity (boolean alternateEntity) {
      this.alternateEntity = alternateEntity;
   }

   public boolean isBaseGroupEntity () {
      return baseGroupEntity;
   }

   public void setBaseGroupEntity (boolean baseGroupEntity) {
      this.baseGroupEntity = baseGroupEntity;
   }

   /**
    * @return the measureGroupBy
    */
   public CheckType getMeasureGroupBy () {
      return measureGroupBy;
   }

   /**
    * @param measureGroupBy the measureGroupBy to set
    */
   public void setMeasureGroupBy (CheckType measureGroupBy) {
      this.measureGroupBy = measureGroupBy;
   }

   /**
    * @return the measureConditionWithoutStat
    */
   public CheckType getMeasureConditionWithoutStat () {
      return measureConditionWithoutStat;
   }

   /**
    * @param measureConditionWithoutStat the measureConditionWithoutStat to set
    */
   public void setMeasureConditionWithoutStat (CheckType measureConditionWithoutStat) {
      this.measureConditionWithoutStat = measureConditionWithoutStat;
   }

   /**
    * @return the measurableEntity
    */
   public boolean isMeasurableEntity () {
      return measurableEntity;
   }

   /**
    * @param measurableEntity the measurableEntity to set
    */
   public void setMeasurableEntity (boolean measurableEntity) {
      this.measurableEntity = measurableEntity;
   }

   /**
    * @return the dependantMeasure
    */
   public boolean isDependantMeasure () {
      return dependantMeasure;
   }

   /**
    * @param dependantMeasure the dependantMeasure to set
    */
   public void setDependantMeasure (boolean dependantMeasure) {
      this.dependantMeasure = dependantMeasure;
   }
}