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


package com.execue.util.conversion.timeframe.bean;

import java.util.List;

import com.execue.core.common.type.OperatorType;
import com.execue.util.conversion.timeframe.type.TimeFrameConversionOutputOperandType;

/**
 * This bean contains the information after time frame normalized data conversion. It can have 4 variations depends on
 * type of conversion happened. It has target operator also.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/04/2011
 */
public class TimeFrameConversionOutputInfo {

   private OperatorType                         operatorType;
   private TimeFrameConversionOutputOperandType conversionOutputOperandType;
   private String                               singleOperandValue;
   private List<String>                         multipleOperandValues;
   private TimeFrameRangeComponent              doubleOperandValue;
   private List<TimeFrameRangeComponent>        subConditionOperandValues;

   public OperatorType getOperatorType () {
      return operatorType;
   }

   public void setOperatorType (OperatorType operatorType) {
      this.operatorType = operatorType;
   }

   public TimeFrameConversionOutputOperandType getConversionOutputOperandType () {
      return conversionOutputOperandType;
   }

   public void setConversionOutputOperandType (TimeFrameConversionOutputOperandType conversionOutputOperandType) {
      this.conversionOutputOperandType = conversionOutputOperandType;
   }

   public String getSingleOperandValue () {
      return singleOperandValue;
   }

   public void setSingleOperandValue (String singleOperandValue) {
      this.singleOperandValue = singleOperandValue;
   }

   public List<String> getMultipleOperandValues () {
      return multipleOperandValues;
   }

   public void setMultipleOperandValues (List<String> multipleOperandValues) {
      this.multipleOperandValues = multipleOperandValues;
   }

   public TimeFrameRangeComponent getDoubleOperandValue () {
      return doubleOperandValue;
   }

   public void setDoubleOperandValue (TimeFrameRangeComponent doubleOperandValue) {
      this.doubleOperandValue = doubleOperandValue;
   }

   public List<TimeFrameRangeComponent> getSubConditionOperandValues () {
      return subConditionOperandValues;
   }

   public void setSubConditionOperandValues (List<TimeFrameRangeComponent> subConditionOperandValues) {
      this.subConditionOperandValues = subConditionOperandValues;
   }

   public TimeFrameConversionOutputInfo (OperatorType operatorType,
            TimeFrameConversionOutputOperandType conversionOutputOperandType, String singleOperandValue,
            List<String> multipleOperandValues, TimeFrameRangeComponent doubleOperandValue,
            List<TimeFrameRangeComponent> subConditionOperandValues) {
      super();
      this.operatorType = operatorType;
      this.conversionOutputOperandType = conversionOutputOperandType;
      this.singleOperandValue = singleOperandValue;
      this.multipleOperandValues = multipleOperandValues;
      this.doubleOperandValue = doubleOperandValue;
      this.subConditionOperandValues = subConditionOperandValues;
   }

}
