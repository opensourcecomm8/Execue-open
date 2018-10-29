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


package com.execue.core.common.bean.entity.unstructured;

import java.io.Serializable;

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.OperatorType;

public class FeatureRange implements Serializable {

   private static final long   serialVersionUID     = 1L;
   private Long                id;
   private Long                featureId;
   private String              rangeName;
   private Double              startValue;
   private Double              endValue;
   private Integer             rangeOrder;
   private CheckType           rangeType            = CheckType.YES;
   private transient Long      featureRangeCount;
   private transient String    featureStartOperator = OperatorType.EQUALS.getValue();
   private transient String    featureEndOperator   = OperatorType.EQUALS.getValue();
   private transient CheckType selected             = CheckType.NO;

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the featureId
    */
   public Long getFeatureId () {
      return featureId;
   }

   /**
    * @param featureId
    *           the featureId to set
    */
   public void setFeatureId (Long featureId) {
      this.featureId = featureId;
   }

   /**
    * @return the startValue
    */
   public Double getStartValue () {
      return startValue;
   }

   /**
    * @param startValue
    *           the startValue to set
    */
   public void setStartValue (Double startValue) {
      this.startValue = startValue;
   }

   /**
    * @return the endValue
    */
   public Double getEndValue () {
      return endValue;
   }

   /**
    * @param endValue
    *           the endValue to set
    */
   public void setEndValue (Double endValue) {
      this.endValue = endValue;
   }

   /**
    * @return the rangeOrder
    */
   public Integer getRangeOrder () {
      return rangeOrder;
   }

   /**
    * @param rangeOrder
    *           the rangeOrder to set
    */
   public void setRangeOrder (Integer rangeOrder) {
      this.rangeOrder = rangeOrder;
   }

   /**
    * @return the featureRangeCount
    */
   public Long getFeatureRangeCount () {
      return featureRangeCount;
   }

   /**
    * @param featureRangeCount
    *           the featureRangeCount to set
    */
   public void setFeatureRangeCount (Long featureRangeCount) {
      this.featureRangeCount = featureRangeCount;
   }
   
   public CheckType getRangeType () {
      return rangeType;
   }

   public void setRangeType (CheckType rangeType) {
      this.rangeType = rangeType;
   }

   /**
    * @return the selected
    */
   public CheckType getSelected () {
      return selected;
   }

   /**
    * @param selected
    *           the selected to set
    */
   public void setSelected (CheckType selected) {
      this.selected = selected;
   }

   /**
    * @return the featureStartOperator
    */
   public String getFeatureStartOperator () {
      return featureStartOperator;
   }

   /**
    * @param featureStartOperator
    *           the featureStartOperator to set
    */
   public void setFeatureStartOperator (String featureStartOperator) {
      this.featureStartOperator = featureStartOperator;
   }

   /**
    * @return the featureEndOperator
    */
   public String getFeatureEndOperator () {
      return featureEndOperator;
   }

   /**
    * @param featureEndOperator
    *           the featureEndOperator to set
    */
   public void setFeatureEndOperator (String featureEndOperator) {
      this.featureEndOperator = featureEndOperator;
   }

   public String getRangeName () {
      return rangeName;
   }

   public void setRangeName (String rangeName) {
      this.rangeName = rangeName;
   }

}