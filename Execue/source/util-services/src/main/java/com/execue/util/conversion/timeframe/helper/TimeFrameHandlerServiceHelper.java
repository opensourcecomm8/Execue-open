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


package com.execue.util.conversion.timeframe.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.nlp.NormalizedDataEntity;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.DynamicValueQualifierType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.util.conversion.timeframe.ITimeFrameConstants;
import com.execue.util.conversion.timeframe.bean.TimeFrameConversionOutputForm;
import com.execue.util.conversion.timeframe.bean.TimeFrameRangeComponent;
import com.execue.util.conversion.timeframe.util.TimeFrameUtility;

/**
 * This helper class is required by time frame handler service to convert the time frame normalized data to target
 * format
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/04/2011
 */
public class TimeFrameHandlerServiceHelper implements ITimeFrameConstants {

   /**
    * This method returns the distinct values among the list.
    * 
    * @param normalizedValues
    * @return distinct set of values
    */
   public List<String> getDistinctNormalizedValues (List<String> normalizedValues) {
      List<String> uniqueNormalizedValues = new ArrayList<String>();
      for (String value : normalizedValues) {
         if (!uniqueNormalizedValues.contains(value)) {
            uniqueNormalizedValues.add(value);
         }
      }
      return uniqueNormalizedValues;
   }

   /**
    * This method returns the index of the largest element in the list.
    * 
    * @param elementList
    * @return index of largest element
    */
   public Long getLargestElementIndex (List<Long> elementList) {
      Long largestElementIndex = 0L;
      Long largestElement = elementList.get(0);
      for (int index = 1; index < elementList.size(); index++) {
         Long currentElement = elementList.get(index);
         if (currentElement > largestElement) {
            largestElement = currentElement;
            largestElementIndex = new Long(index);
         }
      }
      return largestElementIndex;
   }

   /**
    * This method returns the index of the shortest element in the list.
    * 
    * @param elementList
    * @return index of shortest element
    */
   public Long getShortestElementIndex (List<Long> elementList) {
      Long shortestElementIndex = 0L;
      Long shortestElement = elementList.get(0);
      for (int index = 1; index < elementList.size(); index++) {
         Long currentElement = elementList.get(index);
         if (currentElement < shortestElement) {
            shortestElement = currentElement;
            shortestElementIndex = new Long(index);
         }
      }
      return shortestElementIndex;
   }

   /**
    * This method checks if the normalized data component missing
    * 
    * @param normalizedDataEntity
    * @return boolean
    */
   public boolean isTimeFrameNormalizedDataComponentMissing (NormalizedDataEntity normalizedDataEntity) {
      boolean isTimeFrameNormalizedDataComponentMissing = false;
      if (normalizedDataEntity == null) {
         isTimeFrameNormalizedDataComponentMissing = true;
      } else if (normalizedDataEntity != null && ExecueCoreUtil.isEmpty(normalizedDataEntity.getValue())) {
         isTimeFrameNormalizedDataComponentMissing = true;
      }
      return isTimeFrameNormalizedDataComponentMissing;
   }

   /**
    * This method returns the starting month of the quarter
    * 
    * @param quarterValue
    * @return month value
    */
   public String getStartingMonthForQuarter (String quarterValue) {
      String startingMonthValue = null;
      int quarter = Integer.parseInt(quarterValue);
      if (quarter == 1) {
         startingMonthValue = "0";
      } else if (quarter == 2) {
         startingMonthValue = "3";
      } else if (quarter == 3) {
         startingMonthValue = "6";
      } else if (quarter == 4) {
         startingMonthValue = "9";
      }
      return startingMonthValue;
   }

   /**
    * This method returns the ending month of the quarter
    * 
    * @param quarterValue
    * @return month value
    */
   public String getEndingMonthForQuarter (String quarterValue) {
      String endingMonthValue = null;
      int quarter = Integer.parseInt(quarterValue);
      if (quarter == 1) {
         endingMonthValue = "2";
      } else if (quarter == 2) {
         endingMonthValue = "5";
      } else if (quarter == 3) {
         endingMonthValue = "8";
      } else if (quarter == 4) {
         endingMonthValue = "11";
      }
      return endingMonthValue;
   }

   /**
    * This method checks if the year is leap or not.
    * 
    * @param year
    *           value
    * @return boolean
    */
   public static boolean isLeapYear (Integer yearValue) {
      return (yearValue % 4 == 0);
   }

   /**
    * This method gets the months for number of quarters.
    * 
    * @param number
    *           of quarters
    * @return number of months
    */
   public Integer getMonthCountForQuarters (Integer quarters) {
      return quarters * 3;
   }

   public Integer getDayCountForQuarters (Integer quarters) {
      return quarters * 90;
   }

   public Integer getDayCountForWeeks (Integer weeks) {
      return weeks * 7;
   }

   public static void populateCalendarWeekDayMap (Map<String, String> calendarWeekDayMap) {
      calendarWeekDayMap.put("Sunday", "1");
      calendarWeekDayMap.put("Monday", "2");
      calendarWeekDayMap.put("Tuesday", "3");
      calendarWeekDayMap.put("Wednesday", "4");
      calendarWeekDayMap.put("Thursday", "5");
      calendarWeekDayMap.put("Friday", "6");
      calendarWeekDayMap.put("Saturday", "7");
   }

   /**
    * This method populates the map for month of the year versus its calendar representation
    * 
    * @param calendarMonthMap
    */
   public static void populateCalendarMonthMap (Map<String, String> calendarMonthMap) {
      calendarMonthMap.put("January", "0");
      calendarMonthMap.put("February", "1");
      calendarMonthMap.put("March", "2");
      calendarMonthMap.put("April", "3");
      calendarMonthMap.put("May", "4");
      calendarMonthMap.put("June", "5");
      calendarMonthMap.put("July", "6");
      calendarMonthMap.put("August", "7");
      calendarMonthMap.put("September", "8");
      calendarMonthMap.put("October", "9");
      calendarMonthMap.put("November", "10");
      calendarMonthMap.put("December", "11");
      calendarMonthMap.put("Jan", "0");
      calendarMonthMap.put("Feb", "1");
      calendarMonthMap.put("Mar", "2");
      calendarMonthMap.put("Apr", "3");
      calendarMonthMap.put("May", "4");
      calendarMonthMap.put("Jun", "5");
      calendarMonthMap.put("Jul", "6");
      calendarMonthMap.put("Aug", "7");
      calendarMonthMap.put("Sep", "8");
      calendarMonthMap.put("Oct", "9");
      calendarMonthMap.put("Nov", "10");
      calendarMonthMap.put("Dec", "11");
      calendarMonthMap.put("0", "0");
      calendarMonthMap.put("1", "1");
      calendarMonthMap.put("2", "2");
      calendarMonthMap.put("3", "3");
      calendarMonthMap.put("4", "4");
      calendarMonthMap.put("5", "5");
      calendarMonthMap.put("6", "6");
      calendarMonthMap.put("7", "7");
      calendarMonthMap.put("8", "8");
      calendarMonthMap.put("9", "9");
      calendarMonthMap.put("10", "10");
      calendarMonthMap.put("11", "11");
   }

   /**
    * This method populates the map with calendar month as key and value as last day of the month. Special entry for
    * february to handle leap year case is added
    * 
    * @param monthLastDayMap
    */
   public static void populateMonthLastDayMap (Map<String, String> monthLastDayMap) {
      monthLastDayMap.put("0", "31");
      monthLastDayMap.put("1", "28");
      monthLastDayMap.put("1" + LEAP_YEAR_KEY_NOTATION, "29");
      monthLastDayMap.put("2", "31");
      monthLastDayMap.put("3", "30");
      monthLastDayMap.put("4", "31");
      monthLastDayMap.put("5", "30");
      monthLastDayMap.put("6", "31");
      monthLastDayMap.put("7", "31");
      monthLastDayMap.put("8", "30");
      monthLastDayMap.put("9", "31");
      monthLastDayMap.put("10", "30");
      monthLastDayMap.put("11", "31");
   }

   /**
    * This method finds the java calendar field corresponding to date qualifier.
    * 
    * @param dateQualifier
    * @return calendar field.
    */
   public Integer getCorrespondingCalendarField (DateQualifier dateQualifier) {
      Integer calendarField = -1;
      if (dateQualifier.equals(DateQualifier.YEAR)) {
         calendarField = Calendar.YEAR;
      } else if (dateQualifier.equals(DateQualifier.MONTH)) {
         calendarField = Calendar.MONTH;
      } else if (dateQualifier.equals(DateQualifier.DAY)) {
         calendarField = Calendar.DAY_OF_MONTH;
      } else if (dateQualifier.equals(DateQualifier.HOUR)) {
         calendarField = Calendar.HOUR_OF_DAY;
      } else if (dateQualifier.equals(DateQualifier.MINUTE)) {
         calendarField = Calendar.MINUTE;
      } else if (dateQualifier.equals(DateQualifier.SECOND)) {
         calendarField = Calendar.SECOND;
      }
      return calendarField;
   }

   /**
    * This method gets the quarters for the number of months.
    * 
    * @param number
    *           of months
    * @return number of quarters
    */
   public Integer getQuarterCountForMonths (Integer months) {
      int exactQuarters = months / 3;
      int extraMonths = months % 3;
      if (extraMonths > 0) {
         exactQuarters += 1;
      }
      return exactQuarters;
   }

   /**
    * This method gets the quarters for the number of days.
    * 
    * @param number
    *           of days
    * @return number of quarters
    */
   public Integer getQuarterCountForDays (Integer days) {
      int exactQuarters = days / 90;
      int extraDays = days % 90;
      if (extraDays > 0) {
         exactQuarters += 1;
      }
      return exactQuarters;
   }

   /**
    * This method populated the normalized data entity.
    * 
    * @param value
    * @return normalizedDataEntity
    */
   public static NormalizedDataEntity populateNormalizedDataEntity (String value) {
      return TimeFrameUtility.populateNormalizedDataEntity(value);
   }

   /**
    * This method parses time frame range components to return list of values inside them.
    * 
    * @param timeFrameRangeComponentList
    * @return parsedRangeValues
    */
   public static List<String> parseTimeFrameRangeComponents (List<TimeFrameRangeComponent> timeFrameRangeComponentList) {
      List<String> parsedRangeValues = new ArrayList<String>();
      for (TimeFrameRangeComponent timeFrameRangeComponent : timeFrameRangeComponentList) {
         parsedRangeValues.add(timeFrameRangeComponent.getLowerRange());
         parsedRangeValues.add(timeFrameRangeComponent.getUpperRange());
      }
      return parsedRangeValues;
   }

   /**
    * This method parses the conversion ouput form and return list of values back
    * 
    * @param timeFrameConversionOutputForm
    * @return parsedValues
    */
   public static List<String> parseTimeFrameConversionOutputForm (
            TimeFrameConversionOutputForm timeFrameConversionOutputForm) {
      List<String> parsedValues = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionNotEmpty(timeFrameConversionOutputForm.getValues())) {
         parsedValues = timeFrameConversionOutputForm.getValues();
      } else if (ExecueCoreUtil.isCollectionNotEmpty(timeFrameConversionOutputForm.getRangeValues())) {
         parsedValues = parseTimeFrameRangeComponents(timeFrameConversionOutputForm.getRangeValues());
      }
      return parsedValues;
   }

   public static boolean isHigherRangeQualifier (DynamicValueQualifierType dynamicValueQualifierType) {
      boolean isHigherRangeQualifier = false;
      if (DynamicValueQualifierType.LAST.equals(dynamicValueQualifierType)) {
         isHigherRangeQualifier = true;
      } else if (DynamicValueQualifierType.FIRST.equals(dynamicValueQualifierType)
               || DynamicValueQualifierType.NEXT.equals(dynamicValueQualifierType)) {
         isHigherRangeQualifier = false;
      }
      return isHigherRangeQualifier;
   }
}
