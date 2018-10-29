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


package com.execue.core.common.bean.nlp;

import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.DynamicValueQualifierType;

/**
 * @author Nitesh
 */
public class RelativeTimeNormalizedData extends TimeFrameNormalizedData {

   protected NormalizedDataEntity      adjective;
   protected NormalizedDataEntity      number;
   protected NormalizedDataEntity      operator;
   protected DynamicValueQualifierType dynamicValueQualifierType;
   protected DateQualifier             relativeDateQualifier;

   /**
    * @return the operator
    */
   public NormalizedDataEntity getOperator () {
      return operator;
   }

   /**
    * @param operator
    *           the operator to set
    */
   public void setOperator (NormalizedDataEntity operator) {
      this.operator = operator;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.TimeFrameNormalizedData#getNormalizedDataType()
    */
   @Override
   public NormalizedDataType getNormalizedDataType () {
      return NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getType()
    */
   @Override
   public String getType () {
      return type;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getTypeBedId()
    */
   @Override
   public Long getTypeBedId () {
      return timeFrameBedId;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getValue()
    */
   @Override
   public String getValue () {
      StringBuilder sb = new StringBuilder();
      if (weekDayNormalizedDataComponent != null) {
         appendWeekdayComponentsValue(sb);
      }
      appendRelativeCompsValue(sb);
      return sb.toString();
   }

   /**
    * @param sb
    */
   private void appendRelativeCompsValue (StringBuilder sb) {
      if (number != null && adjective != null && relativeDateQualifier != null) {
         sb.append("relative " + adjective.getValue() + " " + number.getValue() + " "
                  + relativeDateQualifier.getValue());
      } else if (adjective != null && relativeDateQualifier != null) {
         sb.append("relative " + adjective.getValue() + " " + relativeDateQualifier.getValue());
      } else if (number != null && operator != null && relativeDateQualifier != null) {
         sb.append("relative " + operator.getValue() + " " + relativeDateQualifier.getValue());
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString () {
      return getValue();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof RelativeTimeNormalizedData || obj instanceof String)
               && this.toString().equals(obj.toString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#clone()
    */
   @Override
   public Object clone () throws CloneNotSupportedException {
      RelativeTimeNormalizedData clonedRelativeTFNormalizedData = (RelativeTimeNormalizedData) super.clone();
      if (adjective != null) {
         clonedRelativeTFNormalizedData.setAdjective((NormalizedDataEntity) adjective.clone());
      }
      if (operator != null) {
         clonedRelativeTFNormalizedData.setOperator((NormalizedDataEntity) operator.clone());

      }
      clonedRelativeTFNormalizedData.setNumber((NormalizedDataEntity) number.clone());
      clonedRelativeTFNormalizedData.setTimeFrameType(timeFrameType);
      clonedRelativeTFNormalizedData.setDynamicValueQualifierType(dynamicValueQualifierType);
      clonedRelativeTFNormalizedData.setRelativeDateQualifier(relativeDateQualifier);
      if (weekDayNormalizedDataComponent != null) {
         clonedRelativeTFNormalizedData
                  .setWeekDayNormalizedDataComponent((WeekDayNormalizedDataComponent) weekDayNormalizedDataComponent
                           .clone());

      }
      return clonedRelativeTFNormalizedData;
   }

   @Override
   public String getDisplayValue () {
      StringBuilder sb = new StringBuilder();
      return appendRelativeCompsDisplayValue(sb);
   }

   /**
    * @return
    */
   private String appendRelativeCompsDisplayValue (StringBuilder sb) {
      if (number != null && adjective != null) {
         sb.append(adjective.getDisplayValue() + " " + number.getDisplayValue() + " "
                  + relativeDateQualifier.getValue() + " " + super.getDisplayValue());
      } else if (adjective != null) {
         sb
                  .append(adjective.getDisplayValue() + " " + relativeDateQualifier.getValue() + " "
                           + super.getDisplayValue());
      } else {
         sb.append(operator.getDisplayValue() + " " + number.getDisplayValue() + " " + relativeDateQualifier.getValue()
                  + " " + super.getDisplayValue());
      }
      return sb.toString();
   }

   /**
    * @return the adjective
    */
   public NormalizedDataEntity getAdjective () {
      return adjective;
   }

   /**
    * @param adjective
    *           the adjective to set
    */
   public void setAdjective (NormalizedDataEntity adjective) {
      this.adjective = adjective;
   }

   /**
    * @return the number
    */
   public NormalizedDataEntity getNumber () {
      return number;
   }

   /**
    * @param number
    *           the number to set
    */
   public void setNumber (NormalizedDataEntity number) {
      this.number = number;
   }

   public DynamicValueQualifierType getDynamicValueQualifierType () {
      return dynamicValueQualifierType;
   }

   public void setDynamicValueQualifierType (DynamicValueQualifierType dynamicValueQualifierType) {
      this.dynamicValueQualifierType = dynamicValueQualifierType;
   }

   public DateQualifier getRelativeDateQualifier () {
      return relativeDateQualifier;
   }

   public void setRelativeDateQualifier (DateQualifier relativeDateQualifier) {
      this.relativeDateQualifier = relativeDateQualifier;
   }
}