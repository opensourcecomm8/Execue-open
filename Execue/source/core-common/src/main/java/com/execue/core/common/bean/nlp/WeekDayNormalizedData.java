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

import com.execue.core.common.bean.AbstractNormalizedData;
import com.execue.core.common.bean.entity.NormalizedDataType;

/**
 * @author Nitesh
 */
public class WeekDayNormalizedData extends AbstractNormalizedData {

   NormalizedDataEntity       adjective;
   NormalizedDataEntity       number;
   List<NormalizedDataEntity> weekdays;
   String                     type;
   Long                       weekDayBedId;
   boolean                    weekend;

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

   public NormalizedDataType getNormalizedDataType () {
      return NormalizedDataType.WEEK_DAY_NORMALIZED_DATA;
   }

   public String getType () {
      return type;
   }

   public Long getTypeBedId () {
      return weekDayBedId;
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

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return ((obj instanceof WeekDayNormalizedData) || (obj instanceof String))
               && this.toString().equals(obj.toString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#clone()
    */
   @Override
   public Object clone () throws CloneNotSupportedException {
      WeekDayNormalizedData clonedWeekNormalizedData = (WeekDayNormalizedData) super.clone();
      clonedWeekNormalizedData.setNumber(number);
      clonedWeekNormalizedData.setAdjective(adjective);
      clonedWeekNormalizedData.setWeekDayBedId(weekDayBedId);
      clonedWeekNormalizedData.setWeekdays(weekdays);
      clonedWeekNormalizedData.setType(type);
      clonedWeekNormalizedData.setWeekend(weekend);
      return clonedWeekNormalizedData;
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

}