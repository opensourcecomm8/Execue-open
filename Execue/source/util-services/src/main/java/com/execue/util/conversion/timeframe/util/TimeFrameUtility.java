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


package com.execue.util.conversion.timeframe.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.nlp.NormalizedDataEntity;
import com.execue.core.common.bean.nlp.RelativeTimeNormalizedData;
import com.execue.core.common.bean.nlp.TimeFrameNormalizedData;
import com.execue.core.common.bean.nlp.WeekDayNormalizedDataComponent;
import com.execue.core.common.bean.util.TimeInformation;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.DynamicValueQualifierType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.util.conversion.timeframe.ITimeFrameConstants;
import com.execue.util.conversion.timeframe.bean.TimeFrameConversionInputSourceInfo;
import com.execue.util.conversion.timeframe.bean.TimeFrameConversionInputStructure;
import com.execue.util.conversion.timeframe.bean.TimeFrameConversionInputTargetInfo;
import com.execue.util.conversion.timeframe.bean.TimeFrameRangeComponent;
import com.ibm.icu.text.SimpleDateFormat;

/**
 * This utility class contains methods related to date and calendar operations but more specific to time frame handling
 * process
 * 
 * @author Vishay
 * @version 1.0
 */
public class TimeFrameUtility implements ITimeFrameConstants {

   private static Map<Integer, String> calendarMonthMap = new HashMap<Integer, String>();
   static {
      populateCalendarMonthMap(calendarMonthMap);
   }

   /**
    * This method populates the Date object from timeFrameNormalizedData object.
    * 
    * @param timeFrameNormalizedData
    * @param dateQualifier
    * @return date
    */
   public static Date buildNormalizedDateObject (TimeFrameNormalizedData timeFrameNormalizedData,
            DateQualifier dateQualifier) {
      Calendar calendar = GregorianCalendar.getInstance();
      switch (dateQualifier) {
         case YEAR:
            calendar.set(Calendar.YEAR, Integer.parseInt(timeFrameNormalizedData.getYear().getValue()));
            break;
         // we need to put the day of the month else month conversion bombs in as it fills in the current day of the
         // month.
         case MONTH:
            calendar.set(Calendar.YEAR, Integer.parseInt(timeFrameNormalizedData.getYear().getValue()));
            calendar.set(Calendar.MONTH, Integer.parseInt(timeFrameNormalizedData.getMonth().getValue()));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(DEFAULT_FIRST_DAY));
            break;
         case DAY:
            calendar.set(Calendar.YEAR, Integer.parseInt(timeFrameNormalizedData.getYear().getValue()));
            calendar.set(Calendar.MONTH, Integer.parseInt(timeFrameNormalizedData.getMonth().getValue()));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(timeFrameNormalizedData.getDay().getValue()));
            break;
         case SECOND:
            calendar.set(Calendar.YEAR, Integer.parseInt(timeFrameNormalizedData.getYear().getValue()));
            calendar.set(Calendar.MONTH, Integer.parseInt(timeFrameNormalizedData.getMonth().getValue()));
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(timeFrameNormalizedData.getDay().getValue()));
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeFrameNormalizedData.getHour().getValue()));
            calendar.set(Calendar.MINUTE, Integer.parseInt(timeFrameNormalizedData.getMinute().getValue()));
            calendar.set(Calendar.SECOND, Integer.parseInt(timeFrameNormalizedData.getSecond().getValue()));
            break;
      }
      return calendar.getTime();
   }

   public static TimeFrameNormalizedData buildCurrentTimeNormalizedData () {
      TimeFrameNormalizedData timeFrameNormalizedData = new TimeFrameNormalizedData();
      Calendar calendar = Calendar.getInstance();
      timeFrameNormalizedData.setDateQualifier(DateQualifier.SECOND);
      timeFrameNormalizedData.setYear(populateNormalizedDataEntity(calendar.get(Calendar.YEAR) + ""));
      timeFrameNormalizedData.setMonth(populateNormalizedDataEntity(calendar.get(Calendar.MONTH) + ""));
      timeFrameNormalizedData.setDay(populateNormalizedDataEntity(calendar.get(Calendar.DAY_OF_MONTH) + ""));
      timeFrameNormalizedData.setHour(populateNormalizedDataEntity((calendar.get(Calendar.HOUR_OF_DAY)) + ""));
      timeFrameNormalizedData.setMinute(populateNormalizedDataEntity(calendar.get(Calendar.MINUTE) + ""));
      timeFrameNormalizedData.setSecond(populateNormalizedDataEntity(calendar.get(Calendar.SECOND) + ""));
      return timeFrameNormalizedData;
   }

   /**
    * This method populated the normalized data entity.
    * 
    * @param value
    * @return normalizedDataEntity
    */
   public static NormalizedDataEntity populateNormalizedDataEntity (String value) {
      NormalizedDataEntity normalizedDataEntity = new NormalizedDataEntity();
      normalizedDataEntity.setValue(value);
      return normalizedDataEntity;
   }

   /**
    * This method populates the TimeFrameNormalizedData object from string value.
    * 
    * @param value
    * @param dateQualifier
    * @param dateFormat
    * @return timeFrameNormalizedData
    * @throws GovernorException
    */
   public static TimeFrameNormalizedData populateTimeFrameNormalizedData (String value, DateQualifier dateQualifier,
            String dateFormat) throws ExeCueException {
      TimeFrameNormalizedData timeFrameNormalizedData = null;
      try {
         // if qualifier is quarter
         if (DateQualifier.QUARTER.equals(dateQualifier)) {
            timeFrameNormalizedData = populateQuarterTimeFrameNormalizedData(value, dateFormat);
         }
         // if qualifier is not quarter.
         else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            Date date = simpleDateFormat.parse(value);
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(date);
            timeFrameNormalizedData = new TimeFrameNormalizedData();
            timeFrameNormalizedData.setDateQualifier(dateQualifier);
            timeFrameNormalizedData.setYear(populateNormalizedDataEntity(calendar.get(Calendar.YEAR) + ""));
            if (DateQualifier.MONTH.equals(dateQualifier)) {
               timeFrameNormalizedData.setMonth(populateNormalizedDataEntity((calendar.get(Calendar.MONTH)) + ""));
            } else if (DateQualifier.DAY.equals(dateQualifier)) {
               timeFrameNormalizedData.setMonth(populateNormalizedDataEntity((calendar.get(Calendar.MONTH)) + ""));
               timeFrameNormalizedData.setDay(populateNormalizedDataEntity(calendar.get(Calendar.DAY_OF_MONTH) + ""));
            }// there is no format for hour and second in the database.
            else if (DateQualifier.SECOND.equals(dateQualifier)) {
               timeFrameNormalizedData.setMonth(populateNormalizedDataEntity((calendar.get(Calendar.MONTH)) + ""));
               timeFrameNormalizedData.setDay(populateNormalizedDataEntity(calendar.get(Calendar.DAY_OF_MONTH) + ""));
               timeFrameNormalizedData.setHour(populateNormalizedDataEntity((calendar.get(Calendar.HOUR_OF_DAY)) + ""));
               timeFrameNormalizedData.setMinute(populateNormalizedDataEntity(calendar.get(Calendar.MINUTE) + ""));
               timeFrameNormalizedData.setSecond(populateNormalizedDataEntity(calendar.get(Calendar.SECOND) + ""));
            }
         }
      } catch (ParseException e) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return timeFrameNormalizedData;
   }

   /**
    * This method builds the TimeFrameNormalizedData object from current date to the qualifier.
    * 
    * @param dateQualifier
    * @param dateFormat
    * @return timeFrameNormalizedData
    * @throws GovernorException
    */
   public static TimeFrameNormalizedData populateTimeFrameNormalizedData (DateQualifier dateQualifier, String dateFormat) {
      TimeFrameNormalizedData timeFrameNormalizedData = null;

      Calendar calendar = GregorianCalendar.getInstance();
      calendar.setTime(new Date());

      // if qualifier is quarter
      if (DateQualifier.QUARTER.equals(dateQualifier)) {
         String quarterValue = calendar.get(Calendar.YEAR)
                  + getCorrespondingQuarterForMonth(String.valueOf(calendar.get(Calendar.MONTH)));
         timeFrameNormalizedData = populateQuarterTimeFrameNormalizedData(quarterValue, dateFormat);
      }
      // if qualifier is not quarter.
      else {
         timeFrameNormalizedData = new TimeFrameNormalizedData();
         timeFrameNormalizedData.setDateQualifier(dateQualifier);
         timeFrameNormalizedData.setYear(populateNormalizedDataEntity(calendar.get(Calendar.YEAR) + ""));
         if (DateQualifier.MONTH.equals(dateQualifier)) {
            timeFrameNormalizedData.setMonth(populateNormalizedDataEntity((calendar.get(Calendar.MONTH)) + ""));
         } else if (DateQualifier.DAY.equals(dateQualifier)) {
            timeFrameNormalizedData.setMonth(populateNormalizedDataEntity((calendar.get(Calendar.MONTH)) + ""));
            timeFrameNormalizedData.setDay(populateNormalizedDataEntity(calendar.get(Calendar.DAY_OF_MONTH) + ""));
         }// there is no format for hour and second in the database.
         else if (DateQualifier.SECOND.equals(dateQualifier)) {
            timeFrameNormalizedData.setMonth(populateNormalizedDataEntity((calendar.get(Calendar.MONTH)) + ""));
            timeFrameNormalizedData.setDay(populateNormalizedDataEntity(calendar.get(Calendar.DAY_OF_MONTH) + ""));
            timeFrameNormalizedData.setHour(populateNormalizedDataEntity((calendar.get(Calendar.HOUR_OF_DAY)) + ""));
            timeFrameNormalizedData.setMinute(populateNormalizedDataEntity(calendar.get(Calendar.MINUTE) + ""));
            timeFrameNormalizedData.setSecond(populateNormalizedDataEntity(calendar.get(Calendar.SECOND) + ""));
         }
      }
      return timeFrameNormalizedData;
   }

   /**
    * This method populates the TimeFrameNormalizedData object from string value.
    * 
    * @param value
    * @param dateQualifier
    * @param dateFormat
    * @return timeFrameNormalizedData
    * @throws GovernorException
    */
   public static TimeFrameNormalizedData populateTimeFrameNormalizedData (Date date, DateQualifier dateQualifier,
            String dateFormat) {
      TimeFrameNormalizedData timeFrameNormalizedData = null;
      // if qualifier is quarter
      if (DateQualifier.QUARTER.equals(dateQualifier)) {
         timeFrameNormalizedData = populateQuarterTimeFrameNormalizedData(date, dateFormat);
      }
      // if qualifier is not quarter.
      else {
         Calendar calendar = GregorianCalendar.getInstance();
         calendar.setTime(date);
         timeFrameNormalizedData = new TimeFrameNormalizedData();
         timeFrameNormalizedData.setDateQualifier(dateQualifier);
         timeFrameNormalizedData.setYear(populateNormalizedDataEntity(calendar.get(Calendar.YEAR) + ""));
         if (DateQualifier.MONTH.equals(dateQualifier)) {
            timeFrameNormalizedData.setMonth(populateNormalizedDataEntity((calendar.get(Calendar.MONTH)) + ""));
         } else if (DateQualifier.DAY.equals(dateQualifier)) {
            timeFrameNormalizedData.setMonth(populateNormalizedDataEntity((calendar.get(Calendar.MONTH)) + ""));
            timeFrameNormalizedData.setDay(populateNormalizedDataEntity(calendar.get(Calendar.DAY_OF_MONTH) + ""));
         }// there is no format for hour and second in the database.
         else if (DateQualifier.SECOND.equals(dateQualifier)) {
            timeFrameNormalizedData.setMonth(populateNormalizedDataEntity((calendar.get(Calendar.MONTH)) + ""));
            timeFrameNormalizedData.setDay(populateNormalizedDataEntity(calendar.get(Calendar.DAY_OF_MONTH) + ""));
            timeFrameNormalizedData.setHour(populateNormalizedDataEntity((calendar.get(Calendar.HOUR_OF_DAY)) + ""));
            timeFrameNormalizedData.setMinute(populateNormalizedDataEntity(calendar.get(Calendar.MINUTE) + ""));
            timeFrameNormalizedData.setSecond(populateNormalizedDataEntity(calendar.get(Calendar.SECOND) + ""));
         }
      }
      return timeFrameNormalizedData;
   }

   /**
    * This method populates TimeFrameNormalizedData object from value if qualifier is quarter
    * 
    * @param value
    * @param dateFormat
    * @return quarterTimeFrameNormalizedData
    */
   public static TimeFrameNormalizedData populateQuarterTimeFrameNormalizedData (String value, String dateFormat) {
      TimeFrameNormalizedData quarterTimeFrameNormalizedData = new TimeFrameNormalizedData();
      quarterTimeFrameNormalizedData.setDateQualifier(DateQualifier.QUARTER);
      String year = null;
      String quarter = null;
      if (dateFormat.equalsIgnoreCase("yyyyQ")) {
         year = value.substring(0, 4);
         quarter = value.substring(4);
      }
      quarterTimeFrameNormalizedData.setYear(populateNormalizedDataEntity(year));
      quarterTimeFrameNormalizedData.setQuarter(populateNormalizedDataEntity(quarter));
      return quarterTimeFrameNormalizedData;
   }

   /**
    * This method populates TimeFrameNormalizedData object from value if qualifier is quarter
    * 
    * @param value
    * @param dateFormat
    * @return quarterTimeFrameNormalizedData
    */
   public static TimeFrameNormalizedData populateQuarterTimeFrameNormalizedData (Date date, String dateFormat) {
      TimeFrameNormalizedData quarterTimeFrameNormalizedData = new TimeFrameNormalizedData();
      quarterTimeFrameNormalizedData.setDateQualifier(DateQualifier.QUARTER);
      String year = null;
      String quarter = null;
      Calendar calendar = GregorianCalendar.getInstance();
      calendar.setTime(date);
      if (dateFormat.equalsIgnoreCase("yyyyQ")) {
         year = calendar.get(Calendar.YEAR) + "";
         Integer monthValue = calendar.get(Calendar.MONTH);
         quarter = getCorrespondingQuarterForMonth(monthValue + "") + "";
      }
      quarterTimeFrameNormalizedData.setYear(populateNormalizedDataEntity(year));
      quarterTimeFrameNormalizedData.setQuarter(populateNormalizedDataEntity(quarter));
      return quarterTimeFrameNormalizedData;
   }

   /**
    * This method parse the timeFrameNormalizedData object and convert it to a string value.
    * 
    * @param timeFrameNormalizedData
    * @param dateFormat
    * @param dateQualifier
    * @return normalizedValue
    */
   public static String parseTimeFrameNormalizedData (TimeFrameNormalizedData timeFrameNormalizedData,
            String dateFormat, DateQualifier dateQualifier) {
      String normalizedValue = null;
      // if qualifier is quarter
      if (DateQualifier.QUARTER.equals(dateQualifier)) {
         normalizedValue = parseQuarterTimeFrameNormalizedData(timeFrameNormalizedData, dateFormat);
      }
      // if qualifier is not quarter.
      else {
         Date normalizedDate = buildNormalizedDateObject(timeFrameNormalizedData, dateQualifier);
         normalizedValue = new SimpleDateFormat(dateFormat).format(normalizedDate);
      }
      return normalizedValue;
   }

   /**
    * This method parse the timeFrameNormalizedData object and convert it to a long value.
    * 
    * @param timeFrameNormalizedData
    * @param dateFormat
    * @param dateQualifier
    * @return normalizedValue
    */
   public static long parseTimeFrameNormalizedDataAsLong (TimeFrameNormalizedData timeFrameNormalizedData,
            String dateFormat, DateQualifier dateQualifier) {
      long normalizedValue = 0;
      // if qualifier is quarter
      if (DateQualifier.QUARTER.equals(dateQualifier)) {
         normalizedValue = Long.parseLong(parseQuarterTimeFrameNormalizedData(timeFrameNormalizedData, dateFormat));
      }
      // if qualifier is not quarter.
      else {
         Date normalizedDate = buildNormalizedDateObject(timeFrameNormalizedData, dateQualifier);
         normalizedValue = normalizedDate.getTime();
      }
      return normalizedValue;
   }

   /**
    * This method parses the quarterTimeFrameNormalizedData for a string normalized value.
    * 
    * @param quarterTimeFrameNormalizedData
    * @param dateFormat
    * @return quarterValue
    */
   private static String parseQuarterTimeFrameNormalizedData (TimeFrameNormalizedData quarterTimeFrameNormalizedData,
            String dateFormat) {
      String quarterValue = null;
      if (dateFormat.equalsIgnoreCase("yyyyQ")) {
         quarterValue = quarterTimeFrameNormalizedData.getYear().getValue()
                  + quarterTimeFrameNormalizedData.getQuarter().getValue();
      }
      return quarterValue;
   }

   /**
    * This method returns the quarter month falls in.
    * 
    * @param monthValue
    * @return quarter value.
    */
   public static String getCorrespondingQuarterForMonth (String monthValue) {
      String quarterValue = null;
      int month = Integer.parseInt(monthValue);
      if (month >= 0 && month <= 2) {
         quarterValue = "1";
      } else if (month >= 3 && month <= 5) {
         quarterValue = "2";
      } else if (month >= 6 && month <= 8) {
         quarterValue = "3";
      } else if (month >= 9 && month <= 11) {
         quarterValue = "4";
      }
      return quarterValue;
   }

   /**
    * This method populated the conversion hierarchy map.
    * 
    * @param dateQualifierHierarchyConversionMap
    */
   public static void populateDateQualifierHierarchyConversionMap (
            Map<DateQualifier, List<DateQualifier>> dateQualifierHierarchyConversionMap) {
      addEntry(dateQualifierHierarchyConversionMap, DateQualifier.YEAR, DateQualifier.YEAR, DateQualifier.QUARTER,
               DateQualifier.MONTH, DateQualifier.DAY, DateQualifier.HOUR, DateQualifier.MINUTE, DateQualifier.SECOND);
      addEntry(dateQualifierHierarchyConversionMap, DateQualifier.QUARTER, DateQualifier.QUARTER, DateQualifier.MONTH,
               DateQualifier.DAY, DateQualifier.HOUR, DateQualifier.MINUTE, DateQualifier.SECOND, DateQualifier.YEAR);
      addEntry(dateQualifierHierarchyConversionMap, DateQualifier.MONTH, DateQualifier.MONTH, DateQualifier.DAY,
               DateQualifier.HOUR, DateQualifier.MINUTE, DateQualifier.SECOND, DateQualifier.QUARTER,
               DateQualifier.YEAR);
      addEntry(dateQualifierHierarchyConversionMap, DateQualifier.WEEK, DateQualifier.WEEK, DateQualifier.DAY,
               DateQualifier.HOUR, DateQualifier.MINUTE, DateQualifier.SECOND, DateQualifier.MONTH,
               DateQualifier.QUARTER, DateQualifier.YEAR);
      addEntry(dateQualifierHierarchyConversionMap, DateQualifier.DAY, DateQualifier.DAY, DateQualifier.HOUR,
               DateQualifier.MINUTE, DateQualifier.SECOND, DateQualifier.MONTH, DateQualifier.QUARTER,
               DateQualifier.YEAR);
      addEntry(dateQualifierHierarchyConversionMap, DateQualifier.HOUR, DateQualifier.HOUR, DateQualifier.MINUTE,
               DateQualifier.SECOND, DateQualifier.DAY, DateQualifier.MONTH, DateQualifier.QUARTER, DateQualifier.YEAR);
      addEntry(dateQualifierHierarchyConversionMap, DateQualifier.MINUTE, DateQualifier.MINUTE, DateQualifier.SECOND,
               DateQualifier.HOUR, DateQualifier.DAY, DateQualifier.MONTH, DateQualifier.QUARTER, DateQualifier.YEAR);
      addEntry(dateQualifierHierarchyConversionMap, DateQualifier.SECOND, DateQualifier.SECOND, DateQualifier.MINUTE,
               DateQualifier.HOUR, DateQualifier.DAY, DateQualifier.MONTH, DateQualifier.QUARTER, DateQualifier.YEAR);
   }

   private static void addEntry (Map<DateQualifier, List<DateQualifier>> dateQualifierHierarchyConversionMap,
            DateQualifier dateQualifier, DateQualifier... dateQualifiers) {
      dateQualifierHierarchyConversionMap.put(dateQualifier, Arrays.asList(dateQualifiers));
   }

   public static Date buildDateObject (String value, String format) {
      Date date = null;
      try {
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
         date = simpleDateFormat.parse(value);
      } catch (ParseException e) {

      }
      return date;
   }

   public static Date addHigherRangeTimeForDay (Date date) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(DEFAULT_LAST_HOUR));
      calendar.set(Calendar.MINUTE, Integer.valueOf(DEFAULT_LAST_MINUTE));
      calendar.set(Calendar.SECOND, Integer.valueOf(DEFAULT_LAST_SECOND));
      return calendar.getTime();
   }

   public static Date addLowerRangeTimeForDay (Date date) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(DEFAULT_FIRST_HOUR));
      calendar.set(Calendar.MINUTE, Integer.valueOf(DEFAULT_FIRST_MINUTE));
      calendar.set(Calendar.SECOND, Integer.valueOf(DEFAULT_FIRST_SECOND));
      return calendar.getTime();
   }

   public static Date carryTimeComponent (Date toDate, Date fromDate) {
      Calendar toCalendar = Calendar.getInstance();
      toCalendar.setTime(toDate);
      Calendar fromCalendar = Calendar.getInstance();
      fromCalendar.setTime(fromDate);
      toCalendar.set(Calendar.HOUR_OF_DAY, fromCalendar.get(Calendar.HOUR_OF_DAY));
      toCalendar.set(Calendar.MINUTE, fromCalendar.get(Calendar.MINUTE));
      toCalendar.set(Calendar.SECOND, fromCalendar.get(Calendar.SECOND));
      return toCalendar.getTime();
   }

   public static TimeFrameRangeComponent buildTimeFrameRangeComponent (Date lowerRange, Date upperRange, String format) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
      String lowerRangeValue = simpleDateFormat.format(lowerRange);
      String upperRangeValue = simpleDateFormat.format(upperRange);
      return new TimeFrameRangeComponent(lowerRangeValue, upperRangeValue);
   }

   public static RelativeTimeNormalizedData buildDefaultRelativeTimeNormalizedData () {
      RelativeTimeNormalizedData relativeTimeNormalizedData = new RelativeTimeNormalizedData();
      relativeTimeNormalizedData.setRelativeDateQualifier(DateQualifier.DAY);
      relativeTimeNormalizedData.setNumber(populateNormalizedDataEntity("20"));
      relativeTimeNormalizedData.setDynamicValueQualifierType(DynamicValueQualifierType.NEXT);
      return relativeTimeNormalizedData;
   }

   public static RelativeTimeNormalizedData buildDefaultRelativeTimeNormalizedDataForWeekday () {
      RelativeTimeNormalizedData relativeTimeNormalizedData = new RelativeTimeNormalizedData();
      relativeTimeNormalizedData.setRelativeDateQualifier(DateQualifier.DAY);
      relativeTimeNormalizedData.setNumber(populateNormalizedDataEntity("7"));
      relativeTimeNormalizedData.setDynamicValueQualifierType(DynamicValueQualifierType.NEXT);
      return relativeTimeNormalizedData;
   }

   public static boolean checkIfNormalizedDataEqalsTillWeekdayComponents (TimeFrameNormalizedData normalizedData1,
            TimeFrameNormalizedData normalizedData2) {
      NormalizedDataEntity year1 = normalizedData1.getYear();
      NormalizedDataEntity year2 = normalizedData2.getYear();
      NormalizedDataEntity quarter1 = normalizedData1.getQuarter();
      NormalizedDataEntity quarter2 = normalizedData2.getQuarter();
      NormalizedDataEntity month1 = normalizedData1.getMonth();
      NormalizedDataEntity month2 = normalizedData2.getMonth();
      NormalizedDataEntity week1 = normalizedData1.getWeek();
      NormalizedDataEntity week2 = normalizedData2.getWeek();
      WeekDayNormalizedDataComponent weekDayNormalizedDataComponent1 = normalizedData1
               .getWeekDayNormalizedDataComponent();
      WeekDayNormalizedDataComponent weekDayNormalizedDataComponent2 = normalizedData2
               .getWeekDayNormalizedDataComponent();
      if (weekDayNormalizedDataComponent1 != null && weekDayNormalizedDataComponent2 != null) {
         if ((year1 == null && year2 == null) || (year1.equals(year2))) {
            if ((quarter1 == null && quarter2 == null) || (quarter1.equals(quarter2))) {
               if ((month1 == null && month2 == null) || (month1.equals(month2))) {
                  if ((week1 == null && week2 == null) || (week1.equals(week2))) {
                     if (weekDayNormalizedDataComponent1.equals(weekDayNormalizedDataComponent2)) {
                        return true;
                     }
                  }
               }
            }
         }
      }
      return false;
   }

   /**
    * @param normalizedData
    */
   public static TimeInformation getTimeInformation (TimeFrameNormalizedData normalizedData) {
      TimeInformation timeInformation = new TimeInformation();
      timeInformation.setHour(normalizedData.getHour());
      timeInformation.setMinute(normalizedData.getMinute());
      timeInformation.setSecond(normalizedData.getSecond());
      timeInformation.setTimeQualifier(normalizedData.getTimeQualifier());
      if (timeInformation.getSecond() != null) {
         timeInformation.setTimeUnitQualifier(DateQualifier.SECOND);
      } else if (timeInformation.getMinute() != null) {
         timeInformation.setTimeUnitQualifier(DateQualifier.MINUTE);
      } else {
         timeInformation.setTimeUnitQualifier(DateQualifier.HOUR);
      }
      return timeInformation;
   }

   public static void populateTimeInformationInWeekDayComponent (TimeFrameNormalizedData normalizedData) {
      WeekDayNormalizedDataComponent weekDayNormalizedDataComponent = normalizedData
               .getWeekDayNormalizedDataComponent();
      if (weekDayNormalizedDataComponent != null
               && weekDayNormalizedDataComponent.getTimeInformation() == null
               && (normalizedData.getHour() != null || normalizedData.getMinute() != null || normalizedData.getSecond() != null)) {
         TimeInformation timeInformation = getTimeInformation(normalizedData);
         weekDayNormalizedDataComponent.addTimeInformation(timeInformation);
      }
   }

   public static List<String> parseDateValues (List<Date> values, String format) {
      List<String> parsedValues = new ArrayList<String>();
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
      for (Date value : values) {
         parsedValues.add(simpleDateFormat.format(value));
      }
      return parsedValues;
   }

   public static TimeFrameConversionInputStructure buildTimeFrameConversionInputStructure (
            List<TimeFrameNormalizedData> timeFrameNormalizedDataList, NormalizedDataType normalizedDataType,
            OperatorType operatorType, String targetDateFormat, DateQualifier targetDateQualifier,
            TimeFrameNormalizedData defaultTimeFrameNormalizedData) {
      TimeFrameConversionInputSourceInfo timeFrameConversionInputSourceInfo = new TimeFrameConversionInputSourceInfo(
               timeFrameNormalizedDataList, normalizedDataType, operatorType);
      TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo = new TimeFrameConversionInputTargetInfo(
               targetDateFormat, targetDateQualifier, defaultTimeFrameNormalizedData);
      return new TimeFrameConversionInputStructure(timeFrameConversionInputSourceInfo,
               timeFrameConversionInputTargetInfo);
   }

   public static String getFormattedTimeFrameValueOfString (DateQualifier dateQualifier, String dateFormat,
            String timeFrameValue) throws ParseException {
      StringBuilder formattedMemberDesc = new StringBuilder(timeFrameValue);
      if (DateQualifier.QUARTER.equals(dateQualifier)) {
         if (dateFormat.equalsIgnoreCase("yyyyQ")) {
            String yearComponent = timeFrameValue.substring(0, 4);
            String quarterComponent = timeFrameValue.substring(4);
            formattedMemberDesc = new StringBuilder();
            formattedMemberDesc.append(TF_QUARTER_COMPONENT).append(SPACE_DELIMETER).append(quarterComponent).append(
                     SPACE_DELIMETER).append(yearComponent);
         }
      } else {
         Date parsedDate = new SimpleDateFormat(dateFormat).parse(timeFrameValue);
         Calendar calendar = new GregorianCalendar();
         calendar.setTime(parsedDate);
         formattedMemberDesc = new StringBuilder();
         Integer yearComponent = calendar.get(Calendar.YEAR);
         if (DateQualifier.YEAR.equals(dateQualifier)) {
            formattedMemberDesc.append(TF_YEAR_COMPONENT).append(SPACE_DELIMETER).append(yearComponent);
         } else if (DateQualifier.MONTH.equals(dateQualifier)) {
            Integer monthComponent = calendar.get(Calendar.MONTH);
            formattedMemberDesc.append(calendarMonthMap.get(monthComponent)).append(SPACE_DELIMETER).append(
                     yearComponent);
         } else if (DateQualifier.DAY.equals(dateQualifier)) {
            Integer monthComponent = calendar.get(Calendar.MONTH);
            Integer dayComponent = calendar.get(Calendar.DAY_OF_MONTH);
            formattedMemberDesc.append(calendarMonthMap.get(monthComponent)).append(SPACE_DELIMETER).append(
                     dayComponent).append(SPACE_DELIMETER).append(yearComponent);
         } else if (DateQualifier.HOUR.equals(dateQualifier)) {
            Integer monthComponent = calendar.get(Calendar.MONTH);
            Integer dayComponent = calendar.get(Calendar.DAY_OF_MONTH);
            Integer hourComponent = calendar.get(Calendar.HOUR_OF_DAY);
            formattedMemberDesc.append(calendarMonthMap.get(monthComponent)).append(SPACE_DELIMETER).append(
                     dayComponent).append(SPACE_DELIMETER).append(yearComponent);
            formattedMemberDesc.append(SPACE_DELIMETER).append(hourComponent).append("hr");
         } else if (DateQualifier.MINUTE.equals(dateQualifier)) {
            Integer monthComponent = calendar.get(Calendar.MONTH);
            Integer dayComponent = calendar.get(Calendar.DAY_OF_MONTH);
            Integer hourComponent = calendar.get(Calendar.HOUR_OF_DAY);
            Integer minuteComponent = calendar.get(Calendar.MINUTE);
            formattedMemberDesc.append(calendarMonthMap.get(monthComponent)).append(SPACE_DELIMETER).append(
                     dayComponent).append(SPACE_DELIMETER).append(yearComponent);
            formattedMemberDesc.append(SPACE_DELIMETER).append(hourComponent).append("h");
            formattedMemberDesc.append(SPACE_DELIMETER).append(minuteComponent).append("m");
         } else if (DateQualifier.SECOND.equals(dateQualifier)) {
            Integer monthComponent = calendar.get(Calendar.MONTH);
            Integer dayComponent = calendar.get(Calendar.DAY_OF_MONTH);
            Integer hourComponent = calendar.get(Calendar.HOUR_OF_DAY);
            Integer minuteComponent = calendar.get(Calendar.MINUTE);
            Integer secondComponent = calendar.get(Calendar.SECOND);
            formattedMemberDesc.append(calendarMonthMap.get(monthComponent)).append(SPACE_DELIMETER).append(
                     dayComponent).append(SPACE_DELIMETER).append(yearComponent);
            formattedMemberDesc.append(SPACE_DELIMETER).append(hourComponent).append("h");
            formattedMemberDesc.append(SPACE_DELIMETER).append(minuteComponent).append("m");
            formattedMemberDesc.append(SPACE_DELIMETER).append(secondComponent).append("s");
         }
      }
      return formattedMemberDesc.toString();
   }

   public static void populateCalendarMonthMap (Map<Integer, String> calendarMonthMap) {
      calendarMonthMap.put(0, "Jan");
      calendarMonthMap.put(1, "Feb");
      calendarMonthMap.put(2, "Mar");
      calendarMonthMap.put(3, "Apr");
      calendarMonthMap.put(4, "May");
      calendarMonthMap.put(5, "Jun");
      calendarMonthMap.put(6, "Jul");
      calendarMonthMap.put(7, "Aug");
      calendarMonthMap.put(8, "Sep");
      calendarMonthMap.put(9, "Oct");
      calendarMonthMap.put(10, "Nov");
      calendarMonthMap.put(11, "Dec");
   }
}
