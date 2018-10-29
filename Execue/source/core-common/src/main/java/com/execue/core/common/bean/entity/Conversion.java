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


package com.execue.core.common.bean.entity;

import java.io.Serializable;

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ConversionType;

/**
 * Object containing the required formatting information at common denominator level.<br/>This object doesn't exist on
 * its own, rather be associated to Query entity where a value is also associated.<br/>Examples are Condition and
 * Having.
 * 
 * @author Raju Gottumukkala
 */
public class Conversion implements Serializable {

   private Long           id;
   private ConversionType type;                         // Examples are Date, Distance, Weight, Currency etc
   private String         unit;                         // Examples are Month, Mile, Pound, Dollar etc
   private String         format;                       // Examples are 'YYYYMM' for all other units it is null
   private int            order;                        // Order for displaying the units
   private String         unitDisplay;                  // Equivalent to unit, but only for display purposes, as unit is unit in
   // coding
   private CheckType      base;                         // Common Denominator for Type
   private Long           valueRealizationInstanceBedId;
   private Long           valueRealizationBedId;
   private Long           detailTypeBedId;

   public Long getValueRealizationBedId () {
      return valueRealizationBedId;
   }

   public void setValueRealizationBedId (Long valueRealizationBedId) {
      this.valueRealizationBedId = valueRealizationBedId;
   }

   public Long getDetailTypeBedId () {
      return detailTypeBedId;
   }

   public void setDetailTypeBedId (Long detailTypeBedId) {
      this.detailTypeBedId = detailTypeBedId;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public ConversionType getType () {
      return type;
   }

   public void setType (ConversionType type) {
      this.type = type;
   }

   public String getUnit () {
      return unit;
   }

   public void setUnit (String unit) {
      this.unit = unit;
   }

   public String getFormat () {
      return format;
   }

   public void setFormat (String format) {
      this.format = format;
   }

   public int getOrder () {
      return order;
   }

   public void setOrder (int order) {
      this.order = order;
   }

   public String getUnitDisplay () {
      return unitDisplay;
   }

   public void setUnitDisplay (String unitDisplay) {
      this.unitDisplay = unitDisplay;
   }

   public CheckType getBase () {
      return base;
   }

   public void setBase (CheckType base) {
      this.base = base;
   }

   /**
    * @return the valueRealizationInstanceBedId
    */
   public Long getValueRealizationInstanceBedId () {
      return valueRealizationInstanceBedId;
   }

   /**
    * @param valueRealizationInstanceBedId
    *           the valueRealizationInstanceBedId to set
    */
   public void setValueRealizationInstanceBedId (Long valueRealizationInstanceBedId) {
      this.valueRealizationInstanceBedId = valueRealizationInstanceBedId;
   }

}
