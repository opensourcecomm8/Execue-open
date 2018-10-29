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

import com.execue.core.common.bean.AbstractNormalizedData;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.type.DateQualifier;

/**
 * @author Nitesh
 */
public class TimeFrameNormalizedData extends AbstractNormalizedData {

   protected NormalizedDataEntity           valuePreposition;
   protected NormalizedDataEntity           day;
   protected NormalizedDataEntity           week;
   protected NormalizedDataEntity           month;
   protected NormalizedDataEntity           quarter;
   protected NormalizedDataEntity           year;
   protected NormalizedDataEntity           hour;
   protected NormalizedDataEntity           minute;
   protected NormalizedDataEntity           second;
   protected NormalizedDataEntity           timeQualifier;
   protected WeekDayNormalizedDataComponent weekDayNormalizedDataComponent;
   protected RecognizedType                 timeFrameType;
   protected DateQualifier                  dateQualifier;
   protected String                         type;
   protected Long                           timeFrameBedId;
   protected boolean                        rangeTimeFound;

   /**
    * @return the NormalizedDataType.TIME_FRAME_CLOUD_TYPE
    */
   public NormalizedDataType getNormalizedDataType () {
      return NormalizedDataType.TIME_FRAME_NORMALIZED_DATA;
   }

   @Override
   public String toString () {
      return getValue();
   }

   @Override
   public boolean equals (Object obj) {
      return (obj instanceof TimeFrameNormalizedData || obj instanceof String)
               && this.toString().equals(obj.toString());
   }

   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   @Override
   public Object clone () throws CloneNotSupportedException {
      TimeFrameNormalizedData clonedTFNormalizedData = (TimeFrameNormalizedData) super.clone();
      clonedTFNormalizedData.setType(type);
      clonedTFNormalizedData.setTimeFrameBedId(timeFrameBedId);
      if (valuePreposition != null) {
         clonedTFNormalizedData.setValuePreposition((NormalizedDataEntity) valuePreposition.clone());
      }
      if (second != null) {
         clonedTFNormalizedData.setSecond((NormalizedDataEntity) second.clone());
      }
      if (minute != null) {
         clonedTFNormalizedData.setMinute((NormalizedDataEntity) minute.clone());
      }
      if (hour != null) {
         clonedTFNormalizedData.setHour((NormalizedDataEntity) hour.clone());
      }
      if (week != null) {
         clonedTFNormalizedData.setWeek((NormalizedDataEntity) week.clone());
      }
      if (day != null) {
         clonedTFNormalizedData.setDay((NormalizedDataEntity) day.clone());
      }
      if (month != null) {
         clonedTFNormalizedData.setMonth((NormalizedDataEntity) month.clone());
      }
      if (quarter != null) {
         clonedTFNormalizedData.setQuarter((NormalizedDataEntity) quarter.clone());
      }
      if (year != null) {
         clonedTFNormalizedData.setYear((NormalizedDataEntity) year.clone());
      }
      if (weekDayNormalizedDataComponent != null) {
         clonedTFNormalizedData
                  .setWeekDayNormalizedDataComponent((WeekDayNormalizedDataComponent) weekDayNormalizedDataComponent
                           .clone());

      }
      clonedTFNormalizedData.setDateQualifier(this.dateQualifier);
      return clonedTFNormalizedData;
   }

   public String getValue () {
      StringBuilder sb = new StringBuilder();
      if (hour != null) {
         sb.append(hour.getValue());
         if (minute != null) {
            sb.append(":").append(minute.getValue());
            if (second != null) {
               sb.append(":").append(second.getValue());
            }
         }
         if (day != null) {
            sb.append(day.getValue());
         }
         appendMonthAndYearValue(sb);

      } else if (weekDayNormalizedDataComponent != null) {
         appendWeekdayComponentsValue(sb);
         if (week != null) {
            sb.append(week.getValue());
         }
         appendMonthAndYearValue(sb);
      }
      if (week != null) {
         sb.append(week.getValue());
         appendMonthAndYearValue(sb);

      } else if (day != null) {
         sb.append(day.getValue());
         appendMonthAndYearValue(sb);

      } else if (month != null) {
         sb.append(month.getValue());
         if (year != null) {
            sb.append("-").append(year.getValue());
         }
      } else if (quarter != null) {
         sb.append(quarter.getValue());
         if (year != null) {
            sb.append("-").append(year.getValue());
         }
      } else if (year != null) {
         sb.append(year.getValue());
      }
      return sb.toString();
   }

   public String getDisplayValue () {
      StringBuilder value = new StringBuilder();
      if (valuePreposition != null) {
         value = value.append(valuePreposition.getDisplayValue()).append(" ");
      }
      if (hour != null) {
         value.append(hour.getValue());
         if (minute != null) {
            value.append(":").append(minute.getValue());
            if (second != null) {
               value.append(":").append(second.getValue()).append(" ");
            } else {
               value.append(" ");
            }
         } else {
            value.append(" ");
         }
         if (timeQualifier != null) {
            value.append(timeQualifier.getDisplayValue()).append(" ");
         }
         if (day != null) {
            value.append(day.getDisplayValue());
         }
         appendMonthAndYearDisplayValue(value);

      } else if (weekDayNormalizedDataComponent != null) {
         appendWeekdayComponentsDisplayValue(value);
         if (week != null) {
            value.append(week.getValue());
         }
         appendMonthAndYearDisplayValue(value);
      } else if (week != null) {
         value.append(week.getValue());
         appendMonthAndYearDisplayValue(value);

      } else if (day != null) {
         value.append(day.getDisplayValue());
         appendMonthAndYearDisplayValue(value);
      } else if (month != null) {
         value.append(month.getDisplayValue());
         if (year != null) {
            value.append("-").append(year.getDisplayValue());
         }
      } else if (quarter != null) {
         value.append(quarter.getDisplayValue());
         if (year != null) {
            value.append("-").append(year.getDisplayValue());
         }
      } else if (year != null) {
         value.append(year.getDisplayValue());
      }
      return value.toString();
   }

   /**
    * @return the rangeTimeFound
    */
   public boolean isRangeTimeFound () {
      return rangeTimeFound;
   }

   /**
    * @param rangeTimeFound
    *           the rangeTimeFound to set
    */
   public void setRangeTimeFound (boolean rangeTimeFound) {
      this.rangeTimeFound = rangeTimeFound;
   }

   /**
    * @return the dateQualifier
    */
   public DateQualifier getDateQualifier () {
      return dateQualifier;
   }

   /**
    * @param dateQualifier
    *           the dateQualifier to set
    */
   public void setDateQualifier (DateQualifier dateQualifier) {
      this.dateQualifier = dateQualifier;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getType()
    */
   public String getType () {
      return type;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getTypeBedId()
    */
   public Long getTypeBedId () {
      return timeFrameBedId;
   }

   /**
    * @param sb
    */
   protected void appendWeekdayComponentsValue (StringBuilder sb) {
      if (weekDayNormalizedDataComponent.getAdjective() != null) {
         sb.append(weekDayNormalizedDataComponent.getAdjective().getValue()).append(' ');
      }
      if (weekDayNormalizedDataComponent.getNumber() != null) {
         sb.append(weekDayNormalizedDataComponent.getNumber().getValue()).append(' ');

      }
      if (weekDayNormalizedDataComponent.getWeekdays() != null) {
         for (NormalizedDataEntity normalizedDataEntity : weekDayNormalizedDataComponent.getWeekdays()) {
            sb.append(normalizedDataEntity.getValue()).append(' ');
         }
      }
   }

   /**
    * @param sb
    */
   protected void appendWeekdayComponentsDisplayValue (StringBuilder sb) {
      if (weekDayNormalizedDataComponent.getAdjective() != null) {
         sb.append(weekDayNormalizedDataComponent.getAdjective().getDisplayValue()).append(' ');
      }
      if (weekDayNormalizedDataComponent.getNumber() != null) {
         sb.append(weekDayNormalizedDataComponent.getNumber().getDisplayValue()).append(' ');

      }
      if (weekDayNormalizedDataComponent.getWeekdays() != null) {
         for (NormalizedDataEntity normalizedDataEntity : weekDayNormalizedDataComponent.getWeekdays()) {
            sb.append(normalizedDataEntity.getDisplayValue()).append(' ');
         }
      }
   }

   /**
    * @param sb
    */
   private void appendMonthAndYearValue (StringBuilder sb) {
      if (month != null) {
         sb.append("-").append(month.getValue());
      }
      if (year != null) {
         sb.append("-").append(year.getValue());
      }
   }

   /**
    * @return the month
    */
   public NormalizedDataEntity getMonth () {
      return month;
   }

   /**
    * @param month
    *           the month to set
    */
   public void setMonth (NormalizedDataEntity month) {
      this.month = month;
   }

   /**
    * @return the quarter
    */
   public NormalizedDataEntity getQuarter () {
      return quarter;
   }

   /**
    * @param quarter
    *           the quarter to set
    */
   public void setQuarter (NormalizedDataEntity quarter) {
      this.quarter = quarter;
   }

   /**
    * @return the year
    */
   public NormalizedDataEntity getYear () {
      return year;
   }

   /**
    * @param year
    *           the year to set
    */
   public void setYear (NormalizedDataEntity year) {
      this.year = year;
   }

   /**
    * @param type
    *           the type to set
    */
   public void setType (String type) {
      this.type = type;
   }

   /**
    * @return the timeFrameBedId
    */
   public Long getTimeFrameBedId () {
      return timeFrameBedId;
   }

   /**
    * @param timeFrameBedId
    *           the timeFrameBedId to set
    */
   public void setTimeFrameBedId (Long timeFrameBedId) {
      this.timeFrameBedId = timeFrameBedId;
   }

   /**
    * @param value
    */
   protected void appendMonthAndYearDisplayValue (StringBuilder value) {
      if (month != null) {
         value.append("-").append(month.getDisplayValue());
      }
      if (year != null) {
         value.append("-").append(year.getDisplayValue());
      }
   }

   /**
    * @return the timeFrameType
    */
   public RecognizedType getTimeFrameType () {
      return timeFrameType;
   }

   /**
    * @param timeFrameType
    *           the timeFrameType to set
    */
   public void setTimeFrameType (RecognizedType timeFrameType) {
      this.timeFrameType = timeFrameType;
   }

   /**
    * @return the day
    */
   public NormalizedDataEntity getDay () {
      return day;
   }

   /**
    * @param day
    *           the day to set
    */
   public void setDay (NormalizedDataEntity day) {
      this.day = day;
   }

   /**
    * @return the valuePreposition
    */
   public NormalizedDataEntity getValuePreposition () {
      return valuePreposition;
   }

   /**
    * @param valuePreposition
    *           the valuePreposition to set
    */
   public void setValuePreposition (NormalizedDataEntity valuePreposition) {
      this.valuePreposition = valuePreposition;
   }

   /**
    * @return the hour
    */
   public NormalizedDataEntity getHour () {
      return hour;
   }

   /**
    * @param hour
    *           the hour to set
    */
   public void setHour (NormalizedDataEntity hour) {
      this.hour = hour;
   }

   /**
    * @return the minute
    */
   public NormalizedDataEntity getMinute () {
      return minute;
   }

   /**
    * @param minute
    *           the minute to set
    */
   public void setMinute (NormalizedDataEntity minute) {
      this.minute = minute;
   }

   /**
    * @return the second
    */
   public NormalizedDataEntity getSecond () {
      return second;
   }

   /**
    * @param second
    *           the second to set
    */
   public void setSecond (NormalizedDataEntity second) {
      this.second = second;
   }

   /**
    * @return the week
    */
   public NormalizedDataEntity getWeek () {
      return week;
   }

   /**
    * @param week
    *           the week to set
    */
   public void setWeek (NormalizedDataEntity week) {
      this.week = week;
   }

   /**
    * @return the timeQualifier
    */
   public NormalizedDataEntity getTimeQualifier () {
      return timeQualifier;
   }

   /**
    * @param timeQualifier
    *           the timeQualifier to set
    */
   public void setTimeQualifier (NormalizedDataEntity timeQualifier) {
      this.timeQualifier = timeQualifier;
   }

   public WeekDayNormalizedDataComponent getWeekDayNormalizedDataComponent () {
      return weekDayNormalizedDataComponent;
   }

   public void setWeekDayNormalizedDataComponent (WeekDayNormalizedDataComponent weekDayNormalizedDataComponent) {
      this.weekDayNormalizedDataComponent = weekDayNormalizedDataComponent;
   }

}