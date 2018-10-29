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


package com.execue.core.common.bean.publisher;

import java.io.Serializable;

import com.execue.core.common.bean.entity.DateFormat;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.GranularityType;

public class QueryColumnDetail implements Serializable {

   private ColumnType      kdxDataType;
   private CheckType       isPopulation    = CheckType.NO;
   private CheckType       isDistribution  = CheckType.NO;
   private CheckType       isTimeBased     = CheckType.NO;
   private CheckType       isLocationBased = CheckType.NO;
   private String          format;
   private DateFormat      dateFormat;
   private String          unit;
   private ConversionType  unitType;
   private GranularityType granularity;
   private CheckType       defaultMetric;

   public GranularityType getGranularity () {
      return granularity;
   }

   public void setGranularity (GranularityType granularity) {
      this.granularity = granularity;
   }

   public ColumnType getKdxDataType () {
      return kdxDataType;
   }

   public void setKdxDataType (ColumnType kdxDataType) {
      this.kdxDataType = kdxDataType;
   }

   public CheckType getIsPopulation () {
      return isPopulation;
   }

   public void setIsPopulation (CheckType isPopulation) {
      this.isPopulation = isPopulation;
   }

   public CheckType getIsDistribution () {
      return isDistribution;
   }

   public void setIsDistribution (CheckType isDistribution) {
      this.isDistribution = isDistribution;
   }

   public CheckType getIsTimeBased () {
      return isTimeBased;
   }

   public void setIsTimeBased (CheckType isTimeBased) {
      this.isTimeBased = isTimeBased;
   }

   public CheckType getIsLocationBased () {
      return isLocationBased;
   }

   public void setIsLocationBased (CheckType isLocationBased) {
      this.isLocationBased = isLocationBased;
   }

   public String getFormat () {
      return format;
   }

   public void setFormat (String format) {
      this.format = format;
   }

   public String getUnit () {
      return unit;
   }

   public void setUnit (String unit) {
      this.unit = unit;
   }

   public ConversionType getUnitType () {
      return unitType;
   }

   public void setUnitType (ConversionType unitType) {
      this.unitType = unitType;
   }

   public DateFormat getDateFormat () {
      return dateFormat;
   }

   public void setDateFormat (DateFormat dateFormat) {
      this.dateFormat = dateFormat;
   }

   
   public CheckType getDefaultMetric () {
      return defaultMetric;
   }

   
   public void setDefaultMetric (CheckType defaultMetric) {
      this.defaultMetric = defaultMetric;
   }

}
