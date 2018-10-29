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


package com.execue.util.conversion.timeframe.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.nlp.NormalizedDataEntity;
import com.execue.core.common.bean.nlp.TimeFrameNormalizedData;
import com.execue.core.common.bean.nlp.WeekDayNormalizedDataComponent;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.DynamicValueQualifierType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueDateTimeUtils;
import com.execue.util.conversion.timeframe.ITimeFrameConversionEnhancerService;
import com.execue.util.conversion.timeframe.bean.TimeFrameConversionInputTargetInfo;
import com.execue.util.conversion.timeframe.bean.TimeFrameRangeComponent;
import com.execue.util.conversion.timeframe.bean.TimeFrameWeekdayConversionInputInfo;
import com.execue.util.conversion.timeframe.helper.TimeFrameHandlerServiceHelper;
import com.execue.util.conversion.timeframe.util.TimeFrameUtility;

/**
 * This service contains methods need to enhance the basic time frame conversion.It has methods for weekday conversion
 * and finding the range between fromDate and toDate.
 * 
 * @author Vishay
 * @version 1.0
 */
public class TimeFrameConversionEnhancerServiceImpl extends TimeFrameHandlerServiceHelper implements
         ITimeFrameConversionEnhancerService {

   private static Map<String, String> calendarWeekDayMap = new HashMap<String, String>();
   static {
      populateCalendarWeekDayMap(calendarWeekDayMap);
   }

   /**
    * This method performs the time frame conversion for weekday handling.
    * 
    * @param weekdayConversionInputInfo
    * @return timeFrameConversionOutputForm
    */
   public List<Date> timeFrameWeekDayConversion (TimeFrameWeekdayConversionInputInfo weekdayConversionInputInfo)
            throws Exception {
      List<Date> filteredValues = new ArrayList<Date>();
      TimeFrameWeekdayConversionInputInfo clonedTimeFrameWeekdayConversionInputInfo = (TimeFrameWeekdayConversionInputInfo) weekdayConversionInputInfo
               .clone();
      WeekDayNormalizedDataComponent weekDayNormalizedDataComponent = clonedTimeFrameWeekdayConversionInputInfo
               .getWeekDayNormalizedDataComponent();
      TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo = clonedTimeFrameWeekdayConversionInputInfo
               .getTimeFrameConversionInputTargetInfo();
      String dateFormat = timeFrameConversionInputTargetInfo.getTargetFormat();
      DateQualifier dateQualifier = timeFrameConversionInputTargetInfo.getTargetDateQualifier();
      TimeFrameRangeComponent range = clonedTimeFrameWeekdayConversionInputInfo.getRange();
      // adjust the range based on default time frame.
      range = adjustTimeFrameRangeComponent(range, timeFrameConversionInputTargetInfo, weekDayNormalizedDataComponent
               .getAdjectiveQualifierType());

      if (DateQualifier.DAY.equals(dateQualifier) || DateQualifier.SECOND.equals(dateQualifier)) {
         adjustNormalizedWeekDayForCalendarWeekDay(weekDayNormalizedDataComponent);
         Date lowerRange = TimeFrameUtility.buildDateObject(range.getLowerRange(), dateFormat);
         Date upperRange = TimeFrameUtility.buildDateObject(range.getUpperRange(), dateFormat);
         List<NormalizedDataEntity> weekdays = weekDayNormalizedDataComponent.getWeekdays();
         // for each of the weekday
         for (NormalizedDataEntity weekDayEntity : weekdays) {
            // get all the days in range
            List<Date> daysInRangeByWeekday = ExecueDateTimeUtils.getAllDaysInRangeByWeekday(lowerRange, upperRange,
                     Integer.parseInt(weekDayEntity.getValue()));
            if (ExecueCoreUtil.isCollectionNotEmpty(daysInRangeByWeekday)) {
               // filter them based on adjective
               filteredValues.addAll(filterValuesByAdjective(daysInRangeByWeekday, weekDayNormalizedDataComponent,
                        lowerRange));
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(filteredValues)) {
            Collections.sort(filteredValues);
         }
      }
      return filteredValues;
   }

   /**
    * This method finds the range of elements between fromdate and todate. The target has to be seconds that is the only
    * way we will find some ranges values between two dates.
    */
   public List<TimeFrameRangeComponent> timeFrameDateTimeRangeConversion (Date fromDate, Date toDate, String format,
            DateQualifier qualifier) throws Exception {
      List<TimeFrameRangeComponent> timeFrameRangeComponentList = null;
      if (DateQualifier.SECOND.equals(qualifier)) {
         // getting all days in range including both
         List<Date> daysInRange = ExecueDateTimeUtils.getAllDaysInRange(fromDate, toDate);
         if (ExecueCoreUtil.isCollectionNotEmpty(daysInRange)) {
            timeFrameRangeComponentList = adjustSelectedDaysTimeOnBoundaries(fromDate, toDate, daysInRange, format);
         }
      }
      return timeFrameRangeComponentList;
   }

   /**
    * This method adjusts the range based on qualifier and default time frame normalized data. If lower range is less
    * than default and higher range is greater than default, then we are fine else we need to make adjustment to the
    * range based on the qualifier.
    * 
    * @param timeFrameRangeComponent
    * @param timeFrameConversionInputTargetInfo
    * @param qualifierType
    * @return
    * @throws Exception
    */
   private TimeFrameRangeComponent adjustTimeFrameRangeComponent (TimeFrameRangeComponent timeFrameRangeComponent,
            TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo,
            DynamicValueQualifierType qualifierType) throws Exception {
      DateQualifier targetDateQualifier = timeFrameConversionInputTargetInfo.getTargetDateQualifier();
      String targetFormat = timeFrameConversionInputTargetInfo.getTargetFormat();
      TimeFrameNormalizedData defaultedTimeFrameNormalizedData = timeFrameConversionInputTargetInfo
               .getDefaultedTimeFrameNormalizedData();
      TimeFrameRangeComponent adjustedTimeFrameRangeComponent = timeFrameRangeComponent;
      if (!DynamicValueQualifierType.ALL.equals(qualifierType)) {
         long defaultTimeAsLong = TimeFrameUtility.parseTimeFrameNormalizedDataAsLong(defaultedTimeFrameNormalizedData,
                  targetFormat, targetDateQualifier);
         long lowerRangeTimeAsLong = TimeFrameUtility.buildDateObject(timeFrameRangeComponent.getLowerRange(),
                  targetFormat).getTime();
         long upperRangeTimeAsLong = TimeFrameUtility.buildDateObject(timeFrameRangeComponent.getUpperRange(),
                  targetFormat).getTime();
         if (lowerRangeTimeAsLong < defaultTimeAsLong && upperRangeTimeAsLong > defaultTimeAsLong) {
            String defaultTimeAsString = TimeFrameUtility.parseTimeFrameNormalizedData(
                     defaultedTimeFrameNormalizedData, targetFormat, targetDateQualifier);
            if (isHigherRangeQualifier(qualifierType)) {
               adjustedTimeFrameRangeComponent = new TimeFrameRangeComponent(timeFrameRangeComponent.getLowerRange(),
                        defaultTimeAsString);
            } else {
               adjustedTimeFrameRangeComponent = new TimeFrameRangeComponent(defaultTimeAsString,
                        timeFrameRangeComponent.getUpperRange());
            }

         }
      }
      return adjustedTimeFrameRangeComponent;
   }

   /**
    * This method filters values by adjective. In case of all, we take everything. In case of last and first, we start
    * from beginning or end and pick up 'n' number of elements. In case of next we check if the lower range is same as
    * first element then we start from second else start from first in the list.
    * 
    * @param values
    * @param weekDayNormalizedDataComponent
    * @param lowerRange
    * @return filteredValues
    */
   private List<Date> filterValuesByAdjective (List<Date> values,
            WeekDayNormalizedDataComponent weekDayNormalizedDataComponent, Date lowerRange) {
      List<Date> filteredValues = new ArrayList<Date>();
      DynamicValueQualifierType adjectiveQualifierType = weekDayNormalizedDataComponent.getAdjectiveQualifierType();
      Integer relativeValue = 0;
      if (weekDayNormalizedDataComponent.getNumber() != null) {
         relativeValue = Integer.valueOf(weekDayNormalizedDataComponent.getNumber().getValue());
      }
      switch (adjectiveQualifierType) {
         case ALL:
            filteredValues.addAll(values);
            break;
         case FIRST:
            for (int index = 0; index < values.size(); index++) {
               filteredValues.add(values.get(index));
               if (filteredValues.size() == relativeValue) {
                  break;
               }
            }
            break;
         case NEXT:
            int startingIndex = 0;
            if (isFirstElementSameAsLowerRangeTillDayLevel(values, lowerRange)) {
               startingIndex = 1;
            }
            for (int index = startingIndex; index < values.size(); index++) {
               filteredValues.add(values.get(index));
               if (filteredValues.size() == relativeValue) {
                  break;
               }
            }
            break;
         case LAST:
            for (int index = values.size() - 1; index >= 0; index--) {
               filteredValues.add(values.get(index));
               if (filteredValues.size() == relativeValue) {
                  break;
               }
            }
            break;
      }
      return filteredValues;
   }

   /**
    * adjusts the time stamp on selected days based on from date and to date. If the from date is equal to the first
    * element from selected days list ignoring the time stamp then, set the time stamp of from date to the first element
    * of the selected days list. If the to date is equal to the last element from the selected days list ignoring the
    * time stamp then, set the time stamp of the to date to the last element of the selected days list.
    * 
    * @param fromDate
    * @param toDate
    * @param selectedDays
    * @return timeFrameRangeComponentList
    */
   public List<TimeFrameRangeComponent> adjustSelectedDaysTimeOnBoundaries (Date fromDate, Date toDate,
            List<Date> selectedDays, String format) {
      List<TimeFrameRangeComponent> timeFrameRangeComponentList = new ArrayList<TimeFrameRangeComponent>();
      boolean isFirstElementSameAsLowerRange = false;
      boolean isLastElementSameAsUpperRange = false;
      if (ExecueDateTimeUtils.areDatesSameTillDayLevel(fromDate, selectedDays.get(0))) {
         isFirstElementSameAsLowerRange = true;
      }
      if (ExecueDateTimeUtils.areDatesSameTillDayLevel(toDate, selectedDays.get(selectedDays.size() - 1))) {
         isLastElementSameAsUpperRange = true;
      }
      int index = 0;
      for (Date selectedDate : selectedDays) {
         index++;
         Date lowerRange = null;
         Date upperRange = null;
         if (isFirstElementSameAsLowerRange && index == 1) {
            lowerRange = fromDate;
         } else {
            lowerRange = TimeFrameUtility.addLowerRangeTimeForDay(selectedDate);
         }
         if (isLastElementSameAsUpperRange && index == selectedDays.size()) {
            upperRange = toDate;
         } else {
            upperRange = TimeFrameUtility.addHigherRangeTimeForDay(selectedDate);
         }
         timeFrameRangeComponentList.add(TimeFrameUtility.buildTimeFrameRangeComponent(lowerRange, upperRange, format));
      }
      return timeFrameRangeComponentList;
   }

   private boolean isFirstElementSameAsLowerRangeTillDayLevel (List<Date> values, Date lowerRange) {
      Date firstElement = values.get(0);
      return ExecueDateTimeUtils.areDatesSameTillDayLevel(firstElement, lowerRange);
   }

   private void adjustNormalizedWeekDayForCalendarWeekDay (WeekDayNormalizedDataComponent weekDayNormalizedDataComponent) {
      List<NormalizedDataEntity> weekdays = weekDayNormalizedDataComponent.getWeekdays();
      for (NormalizedDataEntity normalizedDataEntity : weekdays) {
         normalizedDataEntity.setValue(calendarWeekDayMap.get(normalizedDataEntity.getValue()));
      }
   }

}
