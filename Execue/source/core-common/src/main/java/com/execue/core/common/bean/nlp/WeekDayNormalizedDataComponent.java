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

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.util.TimeInformation;
import com.execue.core.common.type.DynamicValueQualifierType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.util.ExecueCoreUtil;

/**
 * @author Nitesh
 */
public class WeekDayNormalizedDataComponent implements Cloneable {

   private NormalizedDataEntity       adjective;
   private NormalizedDataEntity       number;
   private List<NormalizedDataEntity> weekdays;
   private DynamicValueQualifierType  adjectiveQualifierType;
   private String                     type;
   private Long                       weekDayBedId;
   private List<TimeInformation>      timeInformation;
   private OperatorType               operator;
   private boolean                    timeProvided;          // flag to denote if the time is provided for weekday.
   private boolean                    weekend;               // to qualify if the question asked was for weekend

   public NormalizedDataType getNormalizedDataType () {
      return NormalizedDataType.WEEK_DAY_NORMALIZED_DATA;
   }

   @Override
   public String toString () {
      return getValue();
   }

   @Override
   public boolean equals (Object obj) {
      return (obj instanceof WeekDayNormalizedDataComponent || obj instanceof String)
               && this.toString().equals(obj.toString());
   }

   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   @Override
   public Object clone () throws CloneNotSupportedException {
      WeekDayNormalizedDataComponent clonedWeekNormalizedData = (WeekDayNormalizedDataComponent) super.clone();
      if (number != null) {
         clonedWeekNormalizedData.setNumber((NormalizedDataEntity) number.clone());
      }
      if (adjective != null) {
         clonedWeekNormalizedData.setAdjective((NormalizedDataEntity) adjective.clone());
      }
      clonedWeekNormalizedData.setWeekDayBedId(weekDayBedId);
      List<NormalizedDataEntity> clonedWeekDays = new ArrayList<NormalizedDataEntity>();
      if (ExecueCoreUtil.isCollectionNotEmpty(weekdays)) {
         for (NormalizedDataEntity weekDay : weekdays) {
            clonedWeekDays.add((NormalizedDataEntity) weekDay.clone());
         }
      }
      clonedWeekNormalizedData.setTimeInformation(timeInformation);
      clonedWeekNormalizedData.setOperator(operator);
      clonedWeekNormalizedData.setWeekdays(clonedWeekDays);
      clonedWeekNormalizedData.setType(type);
      clonedWeekNormalizedData.setAdjectiveQualifierType(adjectiveQualifierType);
      clonedWeekNormalizedData.setWeekend(weekend);
      clonedWeekNormalizedData.setTimeProvided(timeProvided);
      return clonedWeekNormalizedData;
   }

   public String getValue () {
      StringBuilder sb = new StringBuilder();
      if (adjective != null) {
         sb.append(adjective.getValue()).append(" ");
      }
      if (number != null) {
         sb.append(number.getValue()).append(" ");
      }
      if (weekdays != null) {
         for (NormalizedDataEntity entity : weekdays) {
            sb.append(entity.getValue()).append(" ");
         }
      }
      return sb.toString();
   }

   public String getDisplayValue () {
      StringBuilder sb = new StringBuilder();
      if (adjective != null) {
         sb.append(adjective.getDisplayValue()).append(" ");
      }
      if (number != null) {
         sb.append(number.getDisplayValue()).append(" ");
      }
      if (weekdays != null) {
         for (NormalizedDataEntity entity : weekdays) {
            sb.append(entity.getDisplayValue()).append(" ");
         }
      }
      return sb.toString();
   }

   /**
    * @return the weekend
    */
   public boolean isWeekend () {
      return weekend;
   }

   /**
    * @param weekend
    *           the weekend to set
    */
   public void setWeekend (boolean weekend) {
      this.weekend = weekend;
   }

   public String getType () {
      return type;
   }

   public Long getTypeBedId () {
      return weekDayBedId;
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

   /**
    * @return the weekdays
    */
   public List<NormalizedDataEntity> getWeekdays () {
      if (weekdays == null) {
         weekdays = new ArrayList<NormalizedDataEntity>(1);
      }
      return weekdays;
   }

   /**
    * @param weekdays
    *           the weekdays to set
    */
   public void setWeekdays (List<NormalizedDataEntity> weekdays) {
      this.weekdays = weekdays;
   }

   /**
    * @return the weekDayBedId
    */
   public Long getWeekDayBedId () {
      return weekDayBedId;
   }

   /**
    * @param weekDayBedId
    *           the weekDayBedId to set
    */
   public void setWeekDayBedId (Long weekDayBedId) {
      this.weekDayBedId = weekDayBedId;
   }

   /**
    * @param type
    *           the type to set
    */
   public void setType (String type) {
      this.type = type;
   }

   public DynamicValueQualifierType getAdjectiveQualifierType () {
      return adjectiveQualifierType;
   }

   public void setAdjectiveQualifierType (DynamicValueQualifierType adjectiveQualifierType) {
      this.adjectiveQualifierType = adjectiveQualifierType;
   }

   /**
    * @return the timeProvided
    */
   public boolean isTimeProvided () {
      return timeProvided;
   }

   /**
    * @param timeProvided
    *           the timeProvided to set
    */
   public void setTimeProvided (boolean timeProvided) {
      this.timeProvided = timeProvided;
   }

   /**
    * @return the timeInformation
    */
   public List<TimeInformation> getTimeInformation () {
      return timeInformation;
   }

   /**
    * @param timeInformation
    *           the timeInformation to set
    */
   public void setTimeInformation (List<TimeInformation> timeInformation) {
      this.timeInformation = timeInformation;
   }

   /**
    * @param timeInformation
    *           the timeInformation to set
    */
   public void addTimeInformation (TimeInformation timeInfo) {
      if (this.timeInformation == null) {
         timeInformation = new ArrayList<TimeInformation>(1);
      }
      timeInformation.add(timeInfo);
   }

   /**
    * @return the operator
    */
   public OperatorType getOperator () {
      return operator;
   }

   /**
    * @param operator
    *           the operator to set
    */
   public void setOperator (OperatorType operator) {
      this.operator = operator;
   }

}