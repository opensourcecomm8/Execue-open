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


package com.execue.core.common.bean.swi;

import java.io.Serializable;
import java.util.Date;

import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;

/**
 * @author Nitesh
 */
public class UserQueryPossibility implements Serializable {

   private Long               id;
   private Long               modelId;
   private Long               possibilityId;
   private Long               entityBedId;
   private BusinessEntityType entityType;
   private Double             recWeight;
   private Double             maxPossibleWeight;
   private Double             typeBasedWeight;
   private Long               userQueryId;
   private Date               executionDate;
   private CheckType          measureGroupBy              = CheckType.NO;
   private CheckType          measureConditionWithoutStat = CheckType.NO;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public Long getPossibilityId () {
      return possibilityId;
   }

   public void setPossibilityId (Long possibilityId) {
      this.possibilityId = possibilityId;
   }

   public Long getEntityBedId () {
      return entityBedId;
   }

   public void setEntityBedId (Long entityBedId) {
      this.entityBedId = entityBedId;
   }

   public BusinessEntityType getEntityType () {
      return entityType;
   }

   public void setEntityType (BusinessEntityType entityType) {
      this.entityType = entityType;
   }

   public Double getRecWeight () {
      return recWeight;
   }

   public void setRecWeight (Double recWeight) {
      this.recWeight = recWeight;
   }

   public Double getMaxPossibleWeight () {
      return maxPossibleWeight;
   }

   public void setMaxPossibleWeight (Double maxPossibleWeight) {
      this.maxPossibleWeight = maxPossibleWeight;
   }

   public Double getTypeBasedWeight () {
      return typeBasedWeight;
   }

   public void setTypeBasedWeight (Double typeBasedWeight) {
      this.typeBasedWeight = typeBasedWeight;
   }

   public Long getUserQueryId () {
      return userQueryId;
   }

   public void setUserQueryId (Long userQueryId) {
      this.userQueryId = userQueryId;
   }

   /**
    * @return the executionDate
    */
   public Date getExecutionDate () {
      return executionDate;
   }

   /**
    * @param executionDate
    *           the executionDate to set
    */
   public void setExecutionDate (Date executionDate) {
      this.executionDate = executionDate;
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

}
