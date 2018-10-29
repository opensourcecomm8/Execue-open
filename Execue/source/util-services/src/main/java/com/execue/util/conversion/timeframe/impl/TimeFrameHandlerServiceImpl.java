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

import java.text.ParseException;
import java.util.ArrayList;
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
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueDateTimeUtils;
import com.execue.util.conversion.timeframe.ITimeFrameConstants;
import com.execue.util.conversion.timeframe.ITimeFrameConversionEnhancerService;
import com.execue.util.conversion.timeframe.ITimeFrameHandlerService;
import com.execue.util.conversion.timeframe.bean.TimeFrameConversionInputSourceInfo;
import com.execue.util.conversion.timeframe.bean.TimeFrameConversionInputStructure;
import com.execue.util.conversion.timeframe.bean.TimeFrameConversionInputStructureList;
import com.execue.util.conversion.timeframe.bean.TimeFrameConversionInputTargetInfo;
import com.execue.util.conversion.timeframe.bean.TimeFrameConversionOutputForm;
import com.execue.util.conversion.timeframe.bean.TimeFrameConversionOutputInfo;
import com.execue.util.conversion.timeframe.bean.TimeFrameRangeComponent;
import com.execue.util.conversion.timeframe.bean.TimeFrameRelativeDataComponent;
import com.execue.util.conversion.timeframe.bean.TimeFrameWeekdayConversionInputInfo;
import com.execue.util.conversion.timeframe.helper.TimeFrameHandlerServiceHelper;
import com.execue.util.conversion.timeframe.type.RelativeQualifierType;
import com.execue.util.conversion.timeframe.type.TimeFrameConversionOutputOperandType;
import com.execue.util.conversion.timeframe.util.TimeFrameUtility;
import com.ibm.icu.text.SimpleDateFormat;

/**
 * This service is used for converting the time frame normalized data into the target format
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/04/2011
 */
public class TimeFrameHandlerServiceImpl extends TimeFrameHandlerServiceHelper implements ITimeFrameHandlerService,
         ITimeFrameConstants {

   private ITimeFrameConversionEnhancerService timeFrameConversionEnhancerService;

   private static Map<String, String>          monthLastDayMap  = new HashMap<String, String>();
   private static Map<String, String>          calendarMonthMap = new HashMap<String, String>();

   static {
      populateMonthLastDayMap(monthLastDayMap);
      populateCalendarMonthMap(calendarMonthMap);
   }

   /**
    * It handles normal TimeFrame conditions as well as RelativeTimeFrames. In normal TimeFrames, condition can come as
    * equal to, between or range.Till,Since,Before,After conditions can also come in normal time frames. In
    * RelativeTimeFrames, the variations are First/Last.
    * 
    * @param timeFrameConversionInputInfo
    * @return timeFrameConversionOutputInfo
    * @throws SWIException
    */

   public TimeFrameConversionOutputInfo timeFrameConversion (
            TimeFrameConversionInputStructure timeFrameConversionInputStructure) throws Exception {
      TimeFrameConversionOutputInfo timeFrameConversionOutputInfo = null;
      try {
         TimeFrameConversionInputSourceInfo timeFrameConversionInputSourceInfo = timeFrameConversionInputStructure
                  .getTimeFrameConversionInputSourceInfo();
         TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo = timeFrameConversionInputStructure
                  .getTimeFrameConversionInputTargetInfo();
         List<TimeFrameConversionOutputForm> timeFrameConversionOutputFormList = buildTimeFrameConversionOutputForm(
                  timeFrameConversionInputSourceInfo, timeFrameConversionInputTargetInfo);
         OperatorType sourceOperatorType = timeFrameConversionInputSourceInfo.getSourceOperatorType();
         timeFrameConversionOutputInfo = populateTimeFrameConversionOutputInfo(timeFrameConversionOutputFormList,
                  sourceOperatorType, timeFrameConversionInputTargetInfo);
      } catch (CloneNotSupportedException e) {
         e.printStackTrace();
         throw new Exception("Error in Conversion");
      } catch (ParseException e) {
         e.printStackTrace();
         throw new Exception("Error in Conversion");
      }
      return timeFrameConversionOutputInfo;
   }

   private List<TimeFrameConversionOutputForm> buildTimeFrameConversionOutputForm (
            TimeFrameConversionInputSourceInfo timeFrameConversionInputSourceInfo,
            TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo) throws Exception {
      // Month is coming as jan, feb etc in normalized data. Time AM/PM resolution also needs to happen
      adjustTimeFrameNormalizedData(timeFrameConversionInputTargetInfo.getDefaultedTimeFrameNormalizedData());
      List<TimeFrameNormalizedData> timeFrameNormalizedDataList = timeFrameConversionInputSourceInfo
               .getTimeFrameNormalizedDataList();
      List<TimeFrameNormalizedData> cloneTimeFrameNormalizedDataList = ExecueBeanCloneUtil
               .cloneTimeFrameNormalizedDataList(timeFrameNormalizedDataList);
      List<TimeFrameConversionOutputForm> finalTimeFrameConversionOutputFormList = new ArrayList<TimeFrameConversionOutputForm>();
      for (TimeFrameNormalizedData timeFrameNormalizedData : cloneTimeFrameNormalizedDataList) {
         List<TimeFrameConversionOutputForm> timeFrameOutputFormList = processTimeFrame(timeFrameNormalizedData,
                  timeFrameConversionInputSourceInfo.getSourceOperatorType(), timeFrameConversionInputTargetInfo);
         mergeTimeFrameOutputFormList(finalTimeFrameConversionOutputFormList, timeFrameOutputFormList,
                  timeFrameConversionInputTargetInfo);
      }
      return finalTimeFrameConversionOutputFormList;
   }

   private void mergeTimeFrameOutputFormList (List<TimeFrameConversionOutputForm> timeFrameConversionOutputFormList,
            List<TimeFrameConversionOutputForm> timeFrameOutputFormList,
            TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo) throws ParseException {
      List<TimeFrameConversionOutputForm> afterMergeTimeFrameConversionOutputFormList = new ArrayList<TimeFrameConversionOutputForm>();
      if (ExecueCoreUtil.isCollectionNotEmpty(timeFrameConversionOutputFormList)) {
         afterMergeTimeFrameConversionOutputFormList.addAll(timeFrameConversionOutputFormList);
      }
      afterMergeTimeFrameConversionOutputFormList.addAll(timeFrameOutputFormList);
      List<TimeFrameConversionOutputForm> mergedTimeFrameConversionOutputFormList = new ArrayList<TimeFrameConversionOutputForm>();
      if (afterMergeTimeFrameConversionOutputFormList.size() == 1) {
         mergedTimeFrameConversionOutputFormList = afterMergeTimeFrameConversionOutputFormList;
      } else {
         OperatorType operatorType = afterMergeTimeFrameConversionOutputFormList.get(0).getFinalOperatorType();
         if (OperatorType.BETWEEN.equals(operatorType)) {
            DateQualifier targetDateQualifier = timeFrameConversionInputTargetInfo.getTargetDateQualifier();
            String targetDateFormat = timeFrameConversionInputTargetInfo.getTargetFormat();
            List<String> parseTimeFrameConversionOutputValues = new ArrayList<String>();
            for (TimeFrameConversionOutputForm timeFrameConversionOutputForm : afterMergeTimeFrameConversionOutputFormList) {
               parseTimeFrameConversionOutputValues
                        .addAll(parseTimeFrameConversionOutputForm(timeFrameConversionOutputForm));
            }
            List<String> distinctNormalizedValues = getDistinctNormalizedValues(parseTimeFrameConversionOutputValues);
            String MinNormalizedValue = findMinMaxNormalizedValue(distinctNormalizedValues, targetDateFormat,
                     targetDateQualifier, false);
            String MaxNormalizedValue = findMinMaxNormalizedValue(distinctNormalizedValues, targetDateFormat,
                     targetDateQualifier, true);
            TimeFrameRangeComponent timeFrameRangeComponent = new TimeFrameRangeComponent(MinNormalizedValue,
                     MaxNormalizedValue);
            TimeFrameConversionOutputForm timeFrameConversionOutputForm = new TimeFrameConversionOutputForm();
            timeFrameConversionOutputForm.add(timeFrameRangeComponent);
            mergedTimeFrameConversionOutputFormList.add(timeFrameConversionOutputForm);
         } else {
            mergedTimeFrameConversionOutputFormList = afterMergeTimeFrameConversionOutputFormList;
         }
      }
      timeFrameConversionOutputFormList.clear();
      timeFrameConversionOutputFormList.addAll(mergedTimeFrameConversionOutputFormList);
   }

   private List<TimeFrameConversionOutputForm> buildTimeFrameConversionOutputForm (
            TimeFrameConversionInputStructureList timeFrameConversionInputStructureList) throws Exception {
      List<TimeFrameConversionInputSourceInfo> timeFrameConversionInputSourceInfoList = timeFrameConversionInputStructureList
               .getTimeFrameConversionInputSourceInfoList();
      TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo = timeFrameConversionInputStructureList
               .getTimeFrameConversionInputTargetInfo();
      List<TimeFrameConversionOutputForm> timeFrameConversionOutputFormList = new ArrayList<TimeFrameConversionOutputForm>();
      for (TimeFrameConversionInputSourceInfo timeFrameConversionInputSourceInfo : timeFrameConversionInputSourceInfoList) {
         timeFrameConversionOutputFormList.addAll(buildTimeFrameConversionOutputForm(
                  timeFrameConversionInputSourceInfo, timeFrameConversionInputTargetInfo));
      }
      return timeFrameConversionOutputFormList;
   }

   public TimeFrameConversionOutputInfo timeFrameConversion (
            TimeFrameConversionInputStructureList timeFrameConversionInputStructureList) throws Exception {
      TimeFrameConversionOutputInfo timeFrameConversionOutputInfo = null;
      try {
         TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo = timeFrameConversionInputStructureList
                  .getTimeFrameConversionInputTargetInfo();
         List<TimeFrameConversionOutputForm> timeFrameConversionOutputFormList = buildTimeFrameConversionOutputForm(timeFrameConversionInputStructureList);
         OperatorType sourceOperatorType = timeFrameConversionInputStructureList.getOperatorType();
         timeFrameConversionOutputInfo = populateTimeFrameConversionOutputInfo(timeFrameConversionOutputFormList,
                  sourceOperatorType, timeFrameConversionInputTargetInfo);
      } catch (CloneNotSupportedException e) {
         e.printStackTrace();
         throw new Exception("Error in Conversion");
      } catch (ParseException e) {
         e.printStackTrace();
         throw new Exception("Error in Conversion");
      }
      return timeFrameConversionOutputInfo;
   }

   private List<TimeFrameConversionOutputForm> processTimeFrame (TimeFrameNormalizedData timeFrameNormalizedData,
            OperatorType operatorType, TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo)
            throws Exception {
      List<TimeFrameConversionOutputForm> timeFrameConversionOutputFormList = new ArrayList<TimeFrameConversionOutputForm>();
      if (timeFrameNormalizedData instanceof RelativeTimeNormalizedData) {
         timeFrameConversionOutputFormList = normalizeRelativeTimeFrameData(timeFrameNormalizedData, operatorType,
                  timeFrameConversionInputTargetInfo);
      } else if (timeFrameNormalizedData instanceof TimeFrameNormalizedData) {
         timeFrameConversionOutputFormList = normalizeTimeFrameNormalizedData(timeFrameNormalizedData, operatorType,
                  timeFrameConversionInputTargetInfo);
      }
      return timeFrameConversionOutputFormList;
   }

   /**
    * This method looks at conversion output form and use conversion input info along with source operator and builds
    * the conversion output info
    * 
    * @param timeFrameConversionOutputForm
    * @param sourceOperatorType
    * @param timeFrameConversionInputTargetInfo
    * @return timeFrameConversionOutputInfo
    * @throws ParseException
    */
   private TimeFrameConversionOutputInfo populateTimeFrameConversionOutputInfo (
            List<TimeFrameConversionOutputForm> timeFrameConversionOutputFormList, OperatorType sourceOperatorType,
            TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo) throws ParseException {
      TimeFrameConversionOutputForm timeFrameConversionOutputForm = addTimeFrameConversionOutputFormList(timeFrameConversionOutputFormList);
      List<String> parseTimeFrameConversionOutputValues = parseTimeFrameConversionOutputForm(timeFrameConversionOutputForm);
      List<String> distinctNormalizedValues = getDistinctNormalizedValues(parseTimeFrameConversionOutputValues);
      OperatorType outputOperatorType = null;
      TimeFrameConversionOutputOperandType operandType = null;
      String singleOperandValue = null;
      List<String> multipleOperandValue = null;
      TimeFrameRangeComponent doubleOperandValue = null;
      List<TimeFrameRangeComponent> subConditionOperandValue = null;

      DateQualifier targetDateQualifier = timeFrameConversionInputTargetInfo.getTargetDateQualifier();
      String targetDateFormat = timeFrameConversionInputTargetInfo.getTargetFormat();
      if (sourceOperatorType == null) {
         sourceOperatorType = OperatorType.EQUALS;
      }
      // handle the different variations in normal time frames.
      switch (sourceOperatorType) {
         // this case answers after year 2000
         case GREATER_THAN:
            // this case answers since year 2000
         case GREATER_THAN_EQUALS:
            // this case answers before year 2000
         case LESS_THAN:
            // this case answers till year 2000
         case LESS_THAN_EQUALS:
            // if there are two values, then we have to pick the one based on operator.
            if (distinctNormalizedValues.size() == 1) {
               singleOperandValue = distinctNormalizedValues.get(0);
            } else {
               singleOperandValue = pickAppropriateNormalizedValue(sourceOperatorType, distinctNormalizedValues,
                        targetDateFormat, targetDateQualifier);
            }
            operandType = TimeFrameConversionOutputOperandType.SINGLE;
            outputOperatorType = sourceOperatorType;
            break;
         case BETWEEN:
            // if both values are still same, then change the operator to equals.
            if (distinctNormalizedValues.size() == 1) {
               singleOperandValue = distinctNormalizedValues.get(0);
               operandType = TimeFrameConversionOutputOperandType.SINGLE;
               outputOperatorType = OperatorType.EQUALS;
            } else {
               String MinNormalizedValue = findMinMaxNormalizedValue(distinctNormalizedValues, targetDateFormat,
                        targetDateQualifier, false);
               String MaxNormalizedValue = findMinMaxNormalizedValue(distinctNormalizedValues, targetDateFormat,
                        targetDateQualifier, true);
               doubleOperandValue = new TimeFrameRangeComponent(MinNormalizedValue, MaxNormalizedValue);
               operandType = TimeFrameConversionOutputOperandType.DOUBLE;
               outputOperatorType = OperatorType.BETWEEN;
            }
            break;
         case EQUALS:
         case IN:
            // if both values are still same, then change the operator to equals.
            List<String> values = timeFrameConversionOutputForm.getValues();
            List<TimeFrameRangeComponent> rangeValues = timeFrameConversionOutputForm.getRangeValues();
            if (ExecueCoreUtil.isCollectionNotEmpty(values)) {
               List<String> distinctValues = getDistinctNormalizedValues(values);
               if (distinctValues.size() == 1) {
                  singleOperandValue = distinctValues.get(0);
                  operandType = TimeFrameConversionOutputOperandType.SINGLE;
                  outputOperatorType = OperatorType.EQUALS;
               } else {
                  multipleOperandValue = distinctValues;
                  operandType = TimeFrameConversionOutputOperandType.MULTIPLE;
                  outputOperatorType = OperatorType.IN;
               }
            } else if (ExecueCoreUtil.isCollectionNotEmpty(rangeValues)) {
               List<TimeFrameRangeComponent> distinctRanges = getDistinctRanges(rangeValues);
               if (distinctRanges.size() == 1) {
                  doubleOperandValue = distinctRanges.get(0);
                  operandType = TimeFrameConversionOutputOperandType.DOUBLE;
                  outputOperatorType = OperatorType.BETWEEN;
               } else {
                  // no operator in sub condition
                  subConditionOperandValue = distinctRanges;
                  operandType = TimeFrameConversionOutputOperandType.SUBCONDITION;
               }
            }
            break;

      }
      return new TimeFrameConversionOutputInfo(outputOperatorType, operandType, singleOperandValue,
               multipleOperandValue, doubleOperandValue, subConditionOperandValue);
   }

   private TimeFrameConversionOutputForm addTimeFrameConversionOutputFormList (
            List<TimeFrameConversionOutputForm> timeFrameConversionOutputFormList) {
      TimeFrameConversionOutputForm timeFrameConversionOutputForm = new TimeFrameConversionOutputForm();
      for (TimeFrameConversionOutputForm timeFrameOutputForm : timeFrameConversionOutputFormList) {
         timeFrameConversionOutputForm.addRangeComponents(timeFrameOutputForm.getRangeValues());
         timeFrameConversionOutputForm.add(timeFrameOutputForm.getValues());
      }
      timeFrameConversionOutputForm.setFinalOperatorType(timeFrameConversionOutputFormList.get(0)
               .getFinalOperatorType());
      return timeFrameConversionOutputForm;
   }

   /**
    * This method returns the distinct ranges among list.
    * 
    * @param rangeValues
    * @return
    */
   private List<TimeFrameRangeComponent> getDistinctRanges (List<TimeFrameRangeComponent> rangeValues) {
      List<TimeFrameRangeComponent> distinctRanges = new ArrayList<TimeFrameRangeComponent>();
      for (TimeFrameRangeComponent range : rangeValues) {
         if (!distinctRanges.contains(range)) {
            distinctRanges.add(range);
         }
      }
      return distinctRanges;
   }

   /**
    * This method normalized the relative time frame data.
    * 
    * @param timeFrameNormalizedData
    * @param timeFrameConversionInputTargetInfo
    * @return timeFrameConversionOutputForm
    * @throws Exception
    */
   private List<TimeFrameConversionOutputForm> normalizeRelativeTimeFrameData (
            TimeFrameNormalizedData timeFrameNormalizedData, OperatorType operatorType,
            TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo) throws Exception {
      List<TimeFrameConversionOutputForm> timeFrameConversionOutputFormList = new ArrayList<TimeFrameConversionOutputForm>();
      DateQualifier targetDateQualifier = timeFrameConversionInputTargetInfo.getTargetDateQualifier();
      TimeFrameNormalizedData defaultTimeFrameNormalizedData = timeFrameConversionInputTargetInfo
               .getDefaultedTimeFrameNormalizedData();

      RelativeTimeNormalizedData relativeTimeNormalizedData = (RelativeTimeNormalizedData) timeFrameNormalizedData;

      rephraseRelativeTimeFrameData(relativeTimeNormalizedData, targetDateQualifier);
      // Month is coming as jan,feb etc in normalized data. Time AM/PM resolution also needs to happen
      adjustTimeFrameNormalizedData(timeFrameNormalizedData);
      // populate the defaults to the data for filling missing components.
      // In relative time frames, it is not that straight forward to pick just the missing components.
      DynamicValueQualifierType dynamicValueQualifierType = relativeTimeNormalizedData.getDynamicValueQualifierType();
      DateQualifier relativeDataDateQualifier = relativeTimeNormalizedData.getRelativeDateQualifier();

      populateDefaultsForRelativeTimeFrames(timeFrameNormalizedData, defaultTimeFrameNormalizedData,
               dynamicValueQualifierType);

      // filling the relative time frame based on conversion involved
      boolean isUpperConversion = fillRelativeTimeFrameNormalizedData(timeFrameNormalizedData, targetDateQualifier,
               relativeDataDateQualifier, dynamicValueQualifierType);
      // building the range component based on lower/upper conversion and dynamicValueQualifier type.
      TimeFrameRangeComponent rangeComponent = prepareTimeRangeForRelativeNormalizedData(
               timeFrameConversionInputTargetInfo, timeFrameNormalizedData, isUpperConversion);

      // in case of relative there is possibility that both the lower range and upper range are same, in that case we
      // can convert to single value.
      if (rangeComponent.getLowerRange().equalsIgnoreCase(rangeComponent.getUpperRange())) {
         TimeFrameConversionOutputForm timeFrameConversionOutputForm = new TimeFrameConversionOutputForm();
         timeFrameConversionOutputForm.setFinalOperatorType(operatorType);
         timeFrameConversionOutputForm.add(rangeComponent.getLowerRange());
         timeFrameConversionOutputFormList.add(timeFrameConversionOutputForm);
      } else {
         TimeFrameConversionOutputForm timeFrameConversionOutputForm = new TimeFrameConversionOutputForm();
         timeFrameConversionOutputForm.setFinalOperatorType(operatorType);
         timeFrameConversionOutputForm.add(rangeComponent);
         timeFrameConversionOutputFormList.add(timeFrameConversionOutputForm);
         // handling weekday component
         List<TimeFrameConversionOutputForm> weekdayConversionOutputFormList = handleWeekdayComponent(
                  timeFrameNormalizedData, operatorType, timeFrameConversionInputTargetInfo, rangeComponent);
         if (ExecueCoreUtil.isCollectionNotEmpty(weekdayConversionOutputFormList)) {
            timeFrameConversionOutputFormList = weekdayConversionOutputFormList;
         }
      }

      return timeFrameConversionOutputFormList;
   }

   /**
    * This method prepares the range for relative time frame processing. It has conversion input info and relative time
    * frame normalized data which is populated by now. It uses one more flag upper/lower conversion
    * 
    * @param timeFrameConversionInputTargetInfo
    * @param timeFrameNormalizedData
    * @param defaultTimeFrameNormalizedData
    * @param isUpperConversion
    * @return timeFrameRangeComponent
    * @throws CloneNotSupportedException
    */
   private TimeFrameRangeComponent prepareTimeRangeForRelativeNormalizedData (
            TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo,
            TimeFrameNormalizedData timeFrameNormalizedData, boolean isUpperConversion)
            throws CloneNotSupportedException {
      RelativeTimeNormalizedData relativeTimeNormalizedData = (RelativeTimeNormalizedData) timeFrameNormalizedData;
      DynamicValueQualifierType dynamicValueQualifierType = relativeTimeNormalizedData.getDynamicValueQualifierType();
      DateQualifier relativeDataDateQualifier = relativeTimeNormalizedData.getRelativeDateQualifier();
      Integer relativeValue = Integer.valueOf(relativeTimeNormalizedData.getNumber().getValue());
      DateQualifier targetDateQualifier = timeFrameConversionInputTargetInfo.getTargetDateQualifier();
      String targetDateFormat = timeFrameConversionInputTargetInfo.getTargetFormat();

      TimeFrameNormalizedData lowerRange = null;
      TimeFrameNormalizedData upperRange = null;
      // in case of next, upper range is forward by relative value on the relative qualifier
      // lower range is based on conversion type(upper/lower) .
      // if upper conversion, move forward by 1 on the relative qualifier
      // if lower conversion, move forward by 1 on the target qualifier
      if (DynamicValueQualifierType.NEXT.equals(dynamicValueQualifierType)) {
         TimeFrameNormalizedData clonedTimeFrameNormalizedData = (TimeFrameNormalizedData) timeFrameNormalizedData
                  .clone();
         upperRange = convertRelativeTimeFrameNormalizedData(clonedTimeFrameNormalizedData,
                  timeFrameConversionInputTargetInfo, new TimeFrameRelativeDataComponent(relativeValue,
                           RelativeQualifierType.FORWARD, relativeDataDateQualifier));
         if (isUpperConversion) {
            lowerRange = convertRelativeTimeFrameNormalizedData(timeFrameNormalizedData,
                     timeFrameConversionInputTargetInfo, new TimeFrameRelativeDataComponent(1,
                              RelativeQualifierType.FORWARD, relativeDataDateQualifier));
         } else {
            lowerRange = convertRelativeTimeFrameNormalizedData(timeFrameNormalizedData,
                     timeFrameConversionInputTargetInfo, new TimeFrameRelativeDataComponent(1,
                              RelativeQualifierType.FORWARD, targetDateQualifier));
         }
      }
      // in case of last, upper range is converted time frame normalized value. lower range will be on conversion type
      // in upper conversion, move by relative value - 1 backward on relative qualifier
      // in lower conversion, move back by relative value on relative qualifier
      // and move forward by 1 on target qualifier
      else if (DynamicValueQualifierType.LAST.equals(dynamicValueQualifierType)) {
         if (isUpperConversion) {
            lowerRange = convertRelativeTimeFrameNormalizedData(timeFrameNormalizedData,
                     timeFrameConversionInputTargetInfo, new TimeFrameRelativeDataComponent(relativeValue - 1,
                              RelativeQualifierType.BACK, relativeDataDateQualifier));
         } else {
            lowerRange = convertRelativeTimeFrameNormalizedData(timeFrameNormalizedData,
                     timeFrameConversionInputTargetInfo, new TimeFrameRelativeDataComponent(relativeValue,
                              RelativeQualifierType.BACK, relativeDataDateQualifier));
            lowerRange = convertRelativeTimeFrameNormalizedData(lowerRange, timeFrameConversionInputTargetInfo,
                     new TimeFrameRelativeDataComponent(1, RelativeQualifierType.FORWARD, targetDateQualifier));
         }
         upperRange = (TimeFrameNormalizedData) timeFrameNormalizedData.clone();
      }
      // in case of first, lower range is converted time frame normalized value. upper range will be on conversion type
      // in upper conversion, move by relative value - 1 forward on relative qualifier
      // in lower conversion, move forward by relative value on relative qualifier
      // and move back by 1 on target qualifier
      else if (DynamicValueQualifierType.FIRST.equals(dynamicValueQualifierType)) {
         lowerRange = (TimeFrameNormalizedData) timeFrameNormalizedData.clone();
         if (isUpperConversion) {
            upperRange = convertRelativeTimeFrameNormalizedData(timeFrameNormalizedData,
                     timeFrameConversionInputTargetInfo, new TimeFrameRelativeDataComponent(relativeValue - 1,
                              RelativeQualifierType.FORWARD, relativeDataDateQualifier));
         } else {
            upperRange = convertRelativeTimeFrameNormalizedData(timeFrameNormalizedData,
                     timeFrameConversionInputTargetInfo, new TimeFrameRelativeDataComponent(relativeValue,
                              RelativeQualifierType.FORWARD, relativeDataDateQualifier));
            upperRange = convertRelativeTimeFrameNormalizedData(upperRange, timeFrameConversionInputTargetInfo,
                     new TimeFrameRelativeDataComponent(1, RelativeQualifierType.BACK, targetDateQualifier));
         }

      }
      String lowerRangeValue = TimeFrameUtility.parseTimeFrameNormalizedData(lowerRange, targetDateFormat,
               targetDateQualifier);
      String upperRangeValue = TimeFrameUtility.parseTimeFrameNormalizedData(upperRange, targetDateFormat,
               targetDateQualifier);
      return new TimeFrameRangeComponent(lowerRangeValue, upperRangeValue);
   }

   /**
    * This method rephrases the relativeness of the questions specially targeted for quarter datasets as we cannot do
    * calendar operations on quarter
    * 
    * @param relativeTimeNormalizedData
    * @param targetDateQualifier
    */
   private void rephraseRelativeTimeFrameData (RelativeTimeNormalizedData relativeTimeNormalizedData,
            DateQualifier targetDateQualifier) {
      Integer relativeValue = Integer.parseInt(relativeTimeNormalizedData.getNumber().getValue());
      DateQualifier relativeDateQualifier = relativeTimeNormalizedData.getRelativeDateQualifier();
      if (DateQualifier.WEEK.equals(relativeTimeNormalizedData.getRelativeDateQualifier())) {
         relativeDateQualifier = DateQualifier.DAY;
         relativeValue = getDayCountForWeeks(relativeValue);
      }
      if (DateQualifier.MONTH.equals(relativeDateQualifier) && DateQualifier.QUARTER.equals(targetDateQualifier)) {
         relativeValue = getQuarterCountForMonths(relativeValue);
         relativeDateQualifier = DateQualifier.QUARTER;
      }
      // If Question asked was in Days and answer has to come from quarter
      if (DateQualifier.DAY.equals(relativeDateQualifier) && DateQualifier.QUARTER.equals(targetDateQualifier)) {
         relativeValue = getQuarterCountForDays(relativeValue);
         relativeDateQualifier = DateQualifier.QUARTER;
      }
      relativeTimeNormalizedData.setNumber(populateNormalizedDataEntity(relativeValue.toString()));
      relativeTimeNormalizedData.setRelativeDateQualifier(relativeDateQualifier);
   }

   /**
    * This method parse the relative time frame normalized data for a string value. By this time timeFrameNormalizedData
    * is fully scoped for asset. Only thing we need to consider here is Quarter operations, as we cannot use calendar to
    * perform +/- operations on quarter we need separate handling for that. This method handles very low level
    * operations of relativeness to move forward or backward and by how much and on what qualifier.
    * 
    * @param timeFrameNormalizedData
    * @param timeFrameConversionInputTargetInfo
    * @param relativeDataComponent
    * @return timeFrameNormalizedData
    */
   private TimeFrameNormalizedData convertRelativeTimeFrameNormalizedData (
            TimeFrameNormalizedData timeFrameNormalizedData,
            TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo,
            TimeFrameRelativeDataComponent relativeDataComponent) {
      // adjust timeframe date qualifier for mins and hours
      if (DateQualifier.HOUR.equals(timeFrameNormalizedData.getDateQualifier())
               || DateQualifier.MINUTE.equals(timeFrameNormalizedData.getDateQualifier())) {
         timeFrameNormalizedData.setDateQualifier(DateQualifier.SECOND);
      }
      TimeFrameNormalizedData convertedTimeFrameNormalizedData = timeFrameNormalizedData;
      DateQualifier relativeDataDateQualifier = relativeDataComponent.getRelativeDateQualifier();
      DateQualifier normalizedDataDateQualifier = timeFrameNormalizedData.getDateQualifier();
      Integer relativeValue = relativeDataComponent.getRelativeValue();
      if (relativeValue > 0) {
         RelativeQualifierType relativeQualifierType = relativeDataComponent.getRelativeQualifierType();
         String targetFormat = timeFrameConversionInputTargetInfo.getTargetFormat();
         DateQualifier targetDateQualifier = timeFrameConversionInputTargetInfo.getTargetDateQualifier();
         // From here the actual parsing starts. If dateDateQualifier is of quarter type convert it to months.
         // e.g if user has asked last 5 quarters then convert this to last 15 months.
         if (DateQualifier.QUARTER.equals(relativeDataDateQualifier)) {
            relativeValue = getMonthCountForQuarters(relativeValue);
            relativeDataDateQualifier = DateQualifier.MONTH;
         }
         // if Data is in Quarter form, convert this to months as well. It means adjust the TFNormalizedData so that we
         // can process the TFNormalizedData using calendar
         if (DateQualifier.QUARTER.equals(normalizedDataDateQualifier)) {
            Integer quarterComponent = Integer.parseInt(timeFrameNormalizedData.getQuarter().getValue());
            Integer correspondingMonthComponent = getMonthCountForQuarters(quarterComponent);
            // According to calendar month, month value is one less than actual representation.
            correspondingMonthComponent -= 1;
            timeFrameNormalizedData.setMonth(populateNormalizedDataEntity(correspondingMonthComponent + ""));
            normalizedDataDateQualifier = DateQualifier.MONTH;
         }
         // get the value to be added or removed from data.
         Integer operatorBasedRelativeValue = getQualifierBasedRelativeValue(relativeValue, relativeQualifierType);

         // build date object
         Date normalizedDateObject = TimeFrameUtility.buildNormalizedDateObject(timeFrameNormalizedData,
                  normalizedDataDateQualifier);
         Calendar calendar = new GregorianCalendar();
         calendar.setTime(normalizedDateObject);
         Integer calendarField = getCorrespondingCalendarField(relativeDataDateQualifier);
         calendar.add(calendarField, operatorBasedRelativeValue);
         Date relativeDate = calendar.getTime();
         convertedTimeFrameNormalizedData = TimeFrameUtility.populateTimeFrameNormalizedData(relativeDate,
                  targetDateQualifier, targetFormat);
      }
      return convertedTimeFrameNormalizedData;
   }

   /**
    * This method returns the operator based relative value based on operator (+,- are the possible operators and value
    * to be integer)
    * 
    * @param relativeValue
    * @param operator
    * @return operatorBasedRelativeValue
    */
   public int getQualifierBasedRelativeValue (Integer relativeValue, RelativeQualifierType relativeQualifierType) {
      int operatorBasedRelativeValue = 0;
      if (RelativeQualifierType.FORWARD.equals(relativeQualifierType)) {
         operatorBasedRelativeValue = +relativeValue;
      } else if (RelativeQualifierType.BACK.equals(relativeQualifierType)) {
         operatorBasedRelativeValue = -relativeValue;
      }
      return operatorBasedRelativeValue;
   }

   /**
    * This method takes timeFrameNormalizedData as input which is populated by using defaults. It is full fledged entity
    * by now according to the qualifier. We need to convert this into target DateQualifier. The conversion rules are
    * defines as follows : 1. If Data Qualifier is yearly, then all the asset Data qualifiers can be answered because
    * they have been filled in populate defaults from DDV values. 2. If Data Qualifier is Month, a. If target Qualifier
    * is Year, then we need to fill in the default month(First/Last) based on operator(Last/First). b. If target
    * Qualifier is Month, no conversion involved. c. If target Qualifier is Quarter, no conversion involved.(Quarter to
    * Month conversion will be handled while parsing). d. If target Qualifier is Day, no conversion involved. b,c,d are
    * falling under lower conversion category for which we don't need to do anything as data is already filled in by
    * populate defaults for relative time frames. 3. If Data Qualifier is Quarter, a. If target Qualifier is Year, then
    * we need to fill in the default Quarter(First/Last) based on operator(Last/First). b. If target Qualifier is Month,
    * no conversion involved. c. If target Qualifier is Quarter, no conversion involved. d. If target Qualifier is Day,
    * no conversion involved. b,c,d are falling under lower conversion category for which we don't need to do anything
    * as data is already filled in by populate defaults for relative time frames. 4. If Data Qualifier is Day, a. If
    * target Qualifier is Year, then we need to fill in the default Month and Day(First/Last) based on
    * operator(Last/First). b. If target Qualifier is Month, then we need to fill in the default Month (First/Last)
    * based on operator(Last/First). c. If target Qualifier is Quarter, For quarter value, we get the First/Last month
    * based on operator. Fill in the corresponding month along with day. d. If target Qualifier is Day, no conversion
    * involved. b,c,d are falling under lower conversion category for which we don't need to do anything as data is
    * already filled in by populate defaults for relative time frames.
    * 
    * @param timeFrameNormalizedData
    * @param targetDateQualifier
    * @param relativeDataDateQualifier
    * @param dynamicValueQualifierType
    * @return boolean indicating upperConversion or not.
    */
   private boolean fillRelativeTimeFrameNormalizedData (TimeFrameNormalizedData timeFrameNormalizedData,
            DateQualifier targetDateQualifier, DateQualifier relativeDataDateQualifier,
            DynamicValueQualifierType dynamicValueQualifierType) {
      boolean isHigherRangeQualifier = isHigherRangeQualifier(dynamicValueQualifierType);
      boolean isUpperConversion = false;
      switch (relativeDataDateQualifier) {
         case MONTH:
            if (DateQualifier.YEAR.equals(targetDateQualifier)) {
               timeFrameNormalizedData.setMonth(populateConfigurableValues(DateQualifier.MONTH,
                        dynamicValueQualifierType, null));
               isUpperConversion = true;
            }
            break;
         case QUARTER:
            if (DateQualifier.YEAR.equals(targetDateQualifier)) {
               timeFrameNormalizedData.setQuarter(populateConfigurableValues(DateQualifier.QUARTER,
                        dynamicValueQualifierType, null));
               isUpperConversion = true;
            }
            break;
         case DAY:
            if (DateQualifier.YEAR.equals(targetDateQualifier)) {
               timeFrameNormalizedData.setMonth(populateConfigurableValues(DateQualifier.MONTH,
                        dynamicValueQualifierType, null));
               timeFrameNormalizedData.setDay(populateConfigurableValues(DateQualifier.DAY, dynamicValueQualifierType,
                        timeFrameNormalizedData));
               isUpperConversion = true;
            } else if (DateQualifier.MONTH.equals(targetDateQualifier)) {
               timeFrameNormalizedData.setDay(populateConfigurableValues(DateQualifier.DAY, dynamicValueQualifierType,
                        timeFrameNormalizedData));
               isUpperConversion = true;
            } else if (DateQualifier.QUARTER.equals(targetDateQualifier)) {
               String quarterValue = timeFrameNormalizedData.getQuarter().getValue();
               String monthValue = getStartingMonthForQuarter(quarterValue);
               if (isHigherRangeQualifier) {
                  monthValue = getEndingMonthForQuarter(quarterValue);
               }
               // this is needed to populate day value correctly.
               timeFrameNormalizedData.setMonth(populateNormalizedDataEntity(monthValue));
               timeFrameNormalizedData.setDay(populateConfigurableValues(DateQualifier.DAY, dynamicValueQualifierType,
                        timeFrameNormalizedData));
               isUpperConversion = true;
            }
            break;
         case HOUR:
         case MINUTE:
         case SECOND:
            if (DateQualifier.YEAR.equals(targetDateQualifier)) {
               timeFrameNormalizedData.setMonth(populateConfigurableValues(DateQualifier.MONTH,
                        dynamicValueQualifierType, null));
               timeFrameNormalizedData.setDay(populateConfigurableValues(DateQualifier.DAY, dynamicValueQualifierType,
                        timeFrameNormalizedData));
               timeFrameNormalizedData.setHour(populateConfigurableValues(DateQualifier.HOUR,
                        dynamicValueQualifierType, null));
               timeFrameNormalizedData.setMinute(populateConfigurableValues(DateQualifier.MINUTE,
                        dynamicValueQualifierType, null));
               timeFrameNormalizedData.setSecond(populateConfigurableValues(DateQualifier.SECOND,
                        dynamicValueQualifierType, null));
               isUpperConversion = true;
            } else if (DateQualifier.MONTH.equals(targetDateQualifier)) {
               timeFrameNormalizedData.setDay(populateConfigurableValues(DateQualifier.DAY, dynamicValueQualifierType,
                        timeFrameNormalizedData));
               timeFrameNormalizedData.setHour(populateConfigurableValues(DateQualifier.HOUR,
                        dynamicValueQualifierType, null));
               timeFrameNormalizedData.setMinute(populateConfigurableValues(DateQualifier.MINUTE,
                        dynamicValueQualifierType, null));
               timeFrameNormalizedData.setSecond(populateConfigurableValues(DateQualifier.SECOND,
                        dynamicValueQualifierType, null));
               isUpperConversion = true;
            } else if (DateQualifier.QUARTER.equals(targetDateQualifier)) {
               String quarterValue = timeFrameNormalizedData.getQuarter().getValue();
               String monthValue = getStartingMonthForQuarter(quarterValue);
               if (isHigherRangeQualifier) {
                  monthValue = getEndingMonthForQuarter(quarterValue);
               }
               // this is needed to populate day value correctly.
               timeFrameNormalizedData.setMonth(populateNormalizedDataEntity(monthValue));
               timeFrameNormalizedData.setDay(populateConfigurableValues(DateQualifier.DAY, dynamicValueQualifierType,
                        timeFrameNormalizedData));
               timeFrameNormalizedData.setHour(populateConfigurableValues(DateQualifier.HOUR,
                        dynamicValueQualifierType, null));
               timeFrameNormalizedData.setMinute(populateConfigurableValues(DateQualifier.MINUTE,
                        dynamicValueQualifierType, null));
               timeFrameNormalizedData.setSecond(populateConfigurableValues(DateQualifier.SECOND,
                        dynamicValueQualifierType, null));
               isUpperConversion = true;
            } else if (DateQualifier.DAY.equals(targetDateQualifier)) {
               timeFrameNormalizedData.setHour(populateConfigurableValues(DateQualifier.HOUR,
                        dynamicValueQualifierType, null));
               timeFrameNormalizedData.setMinute(populateConfigurableValues(DateQualifier.MINUTE,
                        dynamicValueQualifierType, null));
               timeFrameNormalizedData.setSecond(populateConfigurableValues(DateQualifier.SECOND,
                        dynamicValueQualifierType, null));
               isUpperConversion = true;
            }
            break;
      }
      if (isUpperConversion) {
         timeFrameNormalizedData.setDateQualifier(relativeDataDateQualifier);
      }
      return isUpperConversion;
   }

   /**
    * This method populates the defaults for relative time frame data. In relative time frames, it is not that straight
    * forward to pick just the missing components. In case of Month qualifier, we need to check if the year user has
    * asked is same as what is in default, then pick the month, else fill in the default month from configuration using
    * operator value. In case of Quarter qualifier, we need to check if the year user has asked is same as what is in
    * default, then pick the Quarter, else fill in the default Quarter from configuration using operator value. In case
    * of Day qualifier, we need to first fill in the month based on year logic. After that we need to check if the year
    * user has asked is same as what is in default along with month also then pick the Day, else fill in the default Day
    * from configuration using operator value.
    * 
    * @param timeFrameNormalizedData
    * @param defaultTimeFrameNormalizedData
    * @param dynamicValueQualifierType
    */
   private void populateDefaultsForRelativeTimeFrames (TimeFrameNormalizedData timeFrameNormalizedData,
            TimeFrameNormalizedData defaultTimeFrameNormalizedData, DynamicValueQualifierType dynamicValueQualifierType) {
      timeFrameNormalizedData.setDateQualifier(defaultTimeFrameNormalizedData.getDateQualifier());
      // need to populate year for all cases.
      if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getYear())) {
         timeFrameNormalizedData.setYear(defaultTimeFrameNormalizedData.getYear());
      }
      switch (defaultTimeFrameNormalizedData.getDateQualifier()) {
         case MONTH:
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getMonth())) {
               if (timeFrameNormalizedData.getYear().getValue().equalsIgnoreCase(
                        defaultTimeFrameNormalizedData.getYear().getValue())) {
                  timeFrameNormalizedData.setMonth(defaultTimeFrameNormalizedData.getMonth());
               } else {
                  timeFrameNormalizedData.setMonth(populateConfigurableValues(DateQualifier.MONTH,
                           dynamicValueQualifierType, null));
               }
            }
            break;
         case QUARTER:
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getQuarter())) {
               if (timeFrameNormalizedData.getYear().getValue().equalsIgnoreCase(
                        defaultTimeFrameNormalizedData.getYear().getValue())) {
                  timeFrameNormalizedData.setQuarter(defaultTimeFrameNormalizedData.getQuarter());
               } else {
                  timeFrameNormalizedData.setQuarter(populateConfigurableValues(DateQualifier.QUARTER,
                           dynamicValueQualifierType, null));
               }
            }
            break;
         case DAY:
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getMonth())) {
               if (timeFrameNormalizedData.getYear().getValue().equalsIgnoreCase(
                        defaultTimeFrameNormalizedData.getYear().getValue())) {
                  timeFrameNormalizedData.setMonth(defaultTimeFrameNormalizedData.getMonth());
               } else {
                  timeFrameNormalizedData.setMonth(populateConfigurableValues(DateQualifier.MONTH,
                           dynamicValueQualifierType, null));
               }
            }
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getDay())) {
               if (timeFrameNormalizedData.getYear().getValue().equalsIgnoreCase(
                        defaultTimeFrameNormalizedData.getYear().getValue())
                        && timeFrameNormalizedData.getMonth().getValue().equalsIgnoreCase(
                                 defaultTimeFrameNormalizedData.getMonth().getValue())) {
                  timeFrameNormalizedData.setDay(defaultTimeFrameNormalizedData.getDay());
               } else {
                  timeFrameNormalizedData.setDay(populateConfigurableValues(DateQualifier.DAY,
                           dynamicValueQualifierType, timeFrameNormalizedData));
               }
            }
            break;
         case SECOND:
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getMonth())) {
               if (timeFrameNormalizedData.getYear().getValue().equalsIgnoreCase(
                        defaultTimeFrameNormalizedData.getYear().getValue())) {
                  timeFrameNormalizedData.setMonth(defaultTimeFrameNormalizedData.getMonth());
               } else {
                  timeFrameNormalizedData.setMonth(populateConfigurableValues(DateQualifier.MONTH,
                           dynamicValueQualifierType, null));
               }
            }
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getDay())) {
               if (timeFrameNormalizedData.getYear().getValue().equalsIgnoreCase(
                        defaultTimeFrameNormalizedData.getYear().getValue())
                        && timeFrameNormalizedData.getMonth().getValue().equalsIgnoreCase(
                                 defaultTimeFrameNormalizedData.getMonth().getValue())) {
                  timeFrameNormalizedData.setDay(defaultTimeFrameNormalizedData.getDay());
               } else {
                  timeFrameNormalizedData.setDay(populateConfigurableValues(DateQualifier.DAY,
                           dynamicValueQualifierType, timeFrameNormalizedData));
               }
            }
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getHour())) {
               if (timeFrameNormalizedData.getYear().getValue().equalsIgnoreCase(
                        defaultTimeFrameNormalizedData.getYear().getValue())
                        && timeFrameNormalizedData.getMonth().getValue().equalsIgnoreCase(
                                 defaultTimeFrameNormalizedData.getMonth().getValue())
                        && timeFrameNormalizedData.getDay().getValue().equalsIgnoreCase(
                                 defaultTimeFrameNormalizedData.getDay().getValue())) {
                  timeFrameNormalizedData.setHour(defaultTimeFrameNormalizedData.getHour());
               } else {
                  timeFrameNormalizedData.setHour(populateConfigurableValues(DateQualifier.HOUR,
                           dynamicValueQualifierType, timeFrameNormalizedData));
               }
            }
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getMinute())) {
               if (timeFrameNormalizedData.getYear().getValue().equalsIgnoreCase(
                        defaultTimeFrameNormalizedData.getYear().getValue())
                        && timeFrameNormalizedData.getMonth().getValue().equalsIgnoreCase(
                                 defaultTimeFrameNormalizedData.getMonth().getValue())
                        && timeFrameNormalizedData.getDay().getValue().equalsIgnoreCase(
                                 defaultTimeFrameNormalizedData.getDay().getValue())
                        && timeFrameNormalizedData.getHour().getValue().equalsIgnoreCase(
                                 defaultTimeFrameNormalizedData.getHour().getValue())) {
                  timeFrameNormalizedData.setMinute(defaultTimeFrameNormalizedData.getMinute());
               } else {
                  timeFrameNormalizedData.setMinute(populateConfigurableValues(DateQualifier.MINUTE,
                           dynamicValueQualifierType, timeFrameNormalizedData));
               }
            }
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getSecond())) {
               if (timeFrameNormalizedData.getYear().getValue().equalsIgnoreCase(
                        defaultTimeFrameNormalizedData.getYear().getValue())
                        && timeFrameNormalizedData.getMonth().getValue().equalsIgnoreCase(
                                 defaultTimeFrameNormalizedData.getMonth().getValue())
                        && timeFrameNormalizedData.getDay().getValue().equalsIgnoreCase(
                                 defaultTimeFrameNormalizedData.getDay().getValue())
                        && timeFrameNormalizedData.getHour().getValue().equalsIgnoreCase(
                                 defaultTimeFrameNormalizedData.getHour().getValue())
                        && timeFrameNormalizedData.getMinute().getValue().equalsIgnoreCase(
                                 defaultTimeFrameNormalizedData.getMinute().getValue())) {
                  timeFrameNormalizedData.setSecond(defaultTimeFrameNormalizedData.getSecond());
               } else {
                  timeFrameNormalizedData.setSecond(populateConfigurableValues(DateQualifier.SECOND,
                           dynamicValueQualifierType, timeFrameNormalizedData));
               }
            }
            break;
      }
   }

   /**
    * This method populates the default first value or last value based on operator(means context last/first)
    * 
    * @param dateQualifier
    * @param operatorType
    * @param yearMonthNormalizedData
    * @return normalizedDataEntity
    */
   private NormalizedDataEntity populateConfigurableValues (DateQualifier dateQualifier,
            DynamicValueQualifierType dynamicValueQualifierType, TimeFrameNormalizedData yearMonthNormalizedData) {
      boolean isHigherRangeQualifier = isHigherRangeQualifier(dynamicValueQualifierType);
      String normalizedDataEntityValue = null;
      if (DateQualifier.MONTH.equals(dateQualifier)) {
         normalizedDataEntityValue = DEFAULT_FIRST_MONTH;
         if (isHigherRangeQualifier) {
            normalizedDataEntityValue = DEFAULT_LAST_MONTH;
         }
      } else if (DateQualifier.QUARTER.equals(dateQualifier)) {
         normalizedDataEntityValue = DEFAULT_FIRST_QUARTER;
         if (isHigherRangeQualifier) {
            normalizedDataEntityValue = DEFAULT_LAST_QUARTER;
         }
      } else if (DateQualifier.DAY.equals(dateQualifier)) {
         normalizedDataEntityValue = DEFAULT_FIRST_DAY;
         if (isHigherRangeQualifier) {
            String monthValue = yearMonthNormalizedData.getMonth().getValue();
            if (isLeapYear(Integer.parseInt(yearMonthNormalizedData.getYear().getValue()))
                     && monthValue.equalsIgnoreCase("1")) {
               monthValue = monthValue + LEAP_YEAR_KEY_NOTATION;
            }
            normalizedDataEntityValue = monthLastDayMap.get(monthValue);
         }
      } else if (DateQualifier.HOUR.equals(dateQualifier)) {
         normalizedDataEntityValue = DEFAULT_FIRST_HOUR;
         if (isHigherRangeQualifier) {
            normalizedDataEntityValue = DEFAULT_LAST_HOUR;
         }
      } else if (DateQualifier.MINUTE.equals(dateQualifier)) {
         normalizedDataEntityValue = DEFAULT_FIRST_MINUTE;
         if (isHigherRangeQualifier) {
            normalizedDataEntityValue = DEFAULT_LAST_MINUTE;
         }
      } else if (DateQualifier.SECOND.equals(dateQualifier)) {
         normalizedDataEntityValue = DEFAULT_FIRST_SECOND;
         if (isHigherRangeQualifier) {
            normalizedDataEntityValue = DEFAULT_LAST_SECOND;
         }
      }
      return populateNormalizedDataEntity(normalizedDataEntityValue);
   }

   /**
    * This method normalize the time frame normalized data and return output form
    * 
    * @param timeFrameNormalizedData
    * @param timeFrameConversionInputTargetInfo
    * @return timeFrameConversionOutputForm
    * @throws Exception
    */
   private List<TimeFrameConversionOutputForm> normalizeTimeFrameNormalizedData (
            TimeFrameNormalizedData timeFrameNormalizedData, OperatorType operatorType,
            TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo) throws Exception {
      List<TimeFrameConversionOutputForm> timeFrameConversionOutputFormList = new ArrayList<TimeFrameConversionOutputForm>();
      DateQualifier targetDateQualifier = timeFrameConversionInputTargetInfo.getTargetDateQualifier();
      String targetFormat = timeFrameConversionInputTargetInfo.getTargetFormat();
      TimeFrameNormalizedData defaultTimeFrameNormalizedData = timeFrameConversionInputTargetInfo
               .getDefaultedTimeFrameNormalizedData();
      // Month is coming as jan,feb etc in normalized data. Time AM/PM resolution also needs to happen.
      adjustTimeFrameNormalizedData(timeFrameNormalizedData);

      // populate the defaults in the normalized data to be processed.
      populateDefaults(timeFrameNormalizedData, defaultTimeFrameNormalizedData);
      // apply the actual conversion
      List<TimeFrameNormalizedData> convertedTimeFrameNormalizedDataList = convertTimeFrameNormalizedData(
               timeFrameNormalizedData, timeFrameConversionInputTargetInfo);
      // for each value, parse it and get the string value out.
      List<String> normalizedValues = new ArrayList<String>();
      for (TimeFrameNormalizedData convertedTimeFrameNormalizedData : convertedTimeFrameNormalizedDataList) {
         normalizedValues.add(TimeFrameUtility.parseTimeFrameNormalizedData(convertedTimeFrameNormalizedData,
                  targetFormat, targetDateQualifier));
      }
      // get the distinct values. If only one value is there then it is a single operand value and weekday conversion
      // cannot be applied because it needs range
      normalizedValues = getDistinctNormalizedValues(normalizedValues);
      if (normalizedValues.size() == 1) {
         TimeFrameConversionOutputForm timeFrameConversionOutputForm = new TimeFrameConversionOutputForm();
         timeFrameConversionOutputForm.setFinalOperatorType(operatorType);
         timeFrameConversionOutputForm.add(normalizedValues.get(0));
         timeFrameConversionOutputFormList.add(timeFrameConversionOutputForm);
      } else if (normalizedValues.size() == 2) {
         // building the range
         TimeFrameRangeComponent timeFrameRangeComponent = new TimeFrameRangeComponent(normalizedValues.get(0),
                  normalizedValues.get(1));
         // adding the range to output form
         TimeFrameConversionOutputForm timeFrameConversionOutputForm = new TimeFrameConversionOutputForm();
         timeFrameConversionOutputForm.setFinalOperatorType(operatorType);
         timeFrameConversionOutputForm.add(timeFrameRangeComponent);
         timeFrameConversionOutputFormList.add(timeFrameConversionOutputForm);
         // call the weekday handling.
         List<TimeFrameConversionOutputForm> weekdayConversionOutputFormList = handleWeekdayComponent(
                  timeFrameNormalizedData, operatorType, timeFrameConversionInputTargetInfo, timeFrameRangeComponent);
         if (ExecueCoreUtil.isCollectionNotEmpty(weekdayConversionOutputFormList)) {
            timeFrameConversionOutputFormList = weekdayConversionOutputFormList;
         }
      }
      return timeFrameConversionOutputFormList;
   }

   /**
    * This method applies the weekday conversion on range element. It can only be applied it target is day or second.
    * 
    * @param timeFrameNormalizedData
    * @param timeFrameConversionInputTargetInfo
    * @param rangeComponent
    * @return timeFrameConversionOutputForm
    * @throws Exception
    */
   private List<TimeFrameConversionOutputForm> handleWeekdayComponent (TimeFrameNormalizedData timeFrameNormalizedData,
            OperatorType operatorType, TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo,
            TimeFrameRangeComponent rangeComponent) throws Exception {
      List<TimeFrameConversionOutputForm> timeFrameConversionOutputFormList = new ArrayList<TimeFrameConversionOutputForm>();
      WeekDayNormalizedDataComponent weekDayNormalizedDataComponent = timeFrameNormalizedData
               .getWeekDayNormalizedDataComponent();
      DateQualifier targetDateQualifier = timeFrameConversionInputTargetInfo.getTargetDateQualifier();
      String targetFormat = timeFrameConversionInputTargetInfo.getTargetFormat();
      if ((DateQualifier.DAY.equals(targetDateQualifier) || DateQualifier.SECOND.equals(targetDateQualifier))
               && weekDayNormalizedDataComponent != null) {
         // building weekday conversion input info
         TimeFrameWeekdayConversionInputInfo weekdayConversionInputInfo = new TimeFrameWeekdayConversionInputInfo(
                  weekDayNormalizedDataComponent, timeFrameConversionInputTargetInfo, rangeComponent);
         // calling service to do the weekday conversion
         List<Date> rangeOfDays = timeFrameConversionEnhancerService
                  .timeFrameWeekDayConversion(weekdayConversionInputInfo);
         if (ExecueCoreUtil.isCollectionNotEmpty(rangeOfDays)) {
            if (DateQualifier.DAY.equals(targetDateQualifier)) {
               // parsing the date values, if it is day
               List<String> parseDateValues = TimeFrameUtility.parseDateValues(rangeOfDays, targetFormat);
               TimeFrameConversionOutputForm timeFrameConversionOutputForm = new TimeFrameConversionOutputForm();
               timeFrameConversionOutputForm.setFinalOperatorType(operatorType);
               timeFrameConversionOutputForm.setValues(parseDateValues);
               timeFrameConversionOutputFormList.add(timeFrameConversionOutputForm);
            } else if (DateQualifier.SECOND.equals(targetDateQualifier)) {
               // building the range for each value if target is second
               boolean isTimeProvided = weekDayNormalizedDataComponent.isTimeProvided();
               // if time provided, need to apply conversion with absolute day now.
               if (isTimeProvided) {
                  // for each absolute day time, need to apply the time asked.
                  if (rangeOfDays.size() == 1) {
                     List<TimeFrameNormalizedData> timeFrameNormalizedDataList = new ArrayList<TimeFrameNormalizedData>();
                     for (TimeInformation timeInformation : weekDayNormalizedDataComponent.getTimeInformation()) {
                        timeFrameNormalizedDataList.add(buildTimeFrameNormalizedData(rangeOfDays.get(0),
                                 timeInformation));
                     }
                     TimeFrameConversionInputSourceInfo timeFrameConversionInputSourceInfo = new TimeFrameConversionInputSourceInfo(
                              timeFrameNormalizedDataList, NormalizedDataType.TIME_FRAME_NORMALIZED_DATA,
                              weekDayNormalizedDataComponent.getOperator());
                     timeFrameConversionOutputFormList = buildTimeFrameConversionOutputForm(
                              timeFrameConversionInputSourceInfo, timeFrameConversionInputTargetInfo);
                  } else {
                     List<TimeFrameConversionInputSourceInfo> timeFrameConversionInputSourceInfoList = new ArrayList<TimeFrameConversionInputSourceInfo>();
                     for (Date date : rangeOfDays) {
                        List<TimeFrameNormalizedData> timeFrameNormalizedDataList = new ArrayList<TimeFrameNormalizedData>();
                        for (TimeInformation timeInformation : weekDayNormalizedDataComponent.getTimeInformation()) {
                           timeFrameNormalizedDataList.add(buildTimeFrameNormalizedData(date, timeInformation));
                        }
                        timeFrameConversionInputSourceInfoList.add(new TimeFrameConversionInputSourceInfo(
                                 timeFrameNormalizedDataList, NormalizedDataType.TIME_FRAME_NORMALIZED_DATA,
                                 weekDayNormalizedDataComponent.getOperator()));
                     }
                     TimeFrameConversionInputStructureList timeFrameConversionInputStructureList = new TimeFrameConversionInputStructureList(
                              timeFrameConversionInputSourceInfoList, timeFrameConversionInputTargetInfo,
                              OperatorType.IN);
                     timeFrameConversionOutputFormList = buildTimeFrameConversionOutputForm(timeFrameConversionInputStructureList);
                  }
               } else {
                  Date fromDate = TimeFrameUtility.buildDateObject(rangeComponent.getLowerRange(), targetFormat);
                  Date toDate = TimeFrameUtility.buildDateObject(rangeComponent.getUpperRange(), targetFormat);
                  List<TimeFrameRangeComponent> timeFrameRangeComponentList = timeFrameConversionEnhancerService
                           .adjustSelectedDaysTimeOnBoundaries(fromDate, toDate, rangeOfDays, targetFormat);
                  TimeFrameConversionOutputForm timeFrameConversionOutputForm = new TimeFrameConversionOutputForm();
                  timeFrameConversionOutputForm.setFinalOperatorType(operatorType);
                  timeFrameConversionOutputForm.setRangeValues(timeFrameRangeComponentList);
                  timeFrameConversionOutputFormList.add(timeFrameConversionOutputForm);
               }
            }
         }
      }
      return timeFrameConversionOutputFormList;
   }

   private TimeFrameNormalizedData buildTimeFrameNormalizedData (Date day, TimeInformation timeInformation)
            throws CloneNotSupportedException {
      TimeFrameNormalizedData timeFrameNormalizedData = TimeFrameUtility.populateTimeFrameNormalizedData(day,
               DateQualifier.DAY, null);
      timeFrameNormalizedData.setDateQualifier(timeInformation.getTimeUnitQualifier());
      timeFrameNormalizedData.setTimeQualifier(timeInformation.getTimeQualifier());
      if (timeInformation.getHour() != null) {
         timeFrameNormalizedData.setHour((NormalizedDataEntity) timeInformation.getHour().clone());
      }
      if (timeInformation.getMinute() != null) {
         timeFrameNormalizedData.setMinute((NormalizedDataEntity) timeInformation.getMinute().clone());
      }
      if (timeInformation.getSecond() != null) {
         timeFrameNormalizedData.setSecond((NormalizedDataEntity) timeInformation.getSecond().clone());
      }
      return timeFrameNormalizedData;
   }

   /**
    * This method replaces the string version of month with calendar integer value
    * 
    * @param timeFrameNormalizedData
    */
   private void adjustNormalizedDataForCalendarMonth (TimeFrameNormalizedData timeFrameNormalizedData) {
      NormalizedDataEntity monthEntity = timeFrameNormalizedData.getMonth();
      if (monthEntity != null) {
         monthEntity.setValue(calendarMonthMap.get(monthEntity.getValue()));
      }
   }

   /**
    * Rules are if 12 Am it means 0, all other am values are as it is. If it says 12 Pm, is is 12 only and if he says
    * anyother pm value then increment by 12.
    * 
    * @param timeFrameNormalizedData
    */
   private void adjustNormalizedDataForCalendarTimeUnit (TimeFrameNormalizedData timeFrameNormalizedData) {
      NormalizedDataEntity timeQualifier = timeFrameNormalizedData.getTimeQualifier();
      if (timeQualifier != null) {
         String timeQualifierValue = timeQualifier.getValue();
         NormalizedDataEntity hourNormalizedDataEntity = timeFrameNormalizedData.getHour();
         Integer hourValue = Integer.parseInt(hourNormalizedDataEntity.getValue());
         if (timeQualifierValue.equalsIgnoreCase(AM_REPRESENTATION)) {
            if (hourValue.equals(12)) {
               hourValue = 0;
            }
         } else if (timeQualifierValue.equalsIgnoreCase(PM_REPRESENTATION)) {
            if (!hourValue.equals(12)) {
               hourValue = hourValue + 12;
            }
         }
         hourNormalizedDataEntity.setValue(hourValue.toString());
      }
   }

   /**
    * This method adjusts the time frame normalized data for 2 digit to 4 digit year, month to calendar representation
    * and am/pm resolution.
    * 
    * @param timeFrameNormalizedData
    */
   private void adjustTimeFrameNormalizedData (TimeFrameNormalizedData timeFrameNormalizedData) {
      adjustNormalizedDataForTwoDigitYear(timeFrameNormalizedData);
      adjustNormalizedDataForCalendarMonth(timeFrameNormalizedData);
      adjustNormalizedDataForCalendarTimeUnit(timeFrameNormalizedData);
   }

   /**
    * This method converts 2 digit year to 4 digit year.
    * 
    * @param timeFrameNormalizedData
    */
   private void adjustNormalizedDataForTwoDigitYear (TimeFrameNormalizedData timeFrameNormalizedData) {
      NormalizedDataEntity yearNormalizedData = timeFrameNormalizedData.getYear();
      if (yearNormalizedData != null) {
         String yearValue = yearNormalizedData.getValue();
         if (yearValue.length() == 2) {
            String fourDigitYear = ExecueDateTimeUtils.convertTwoToFourDigitYear(yearValue);
            yearNormalizedData.setValue(fourDigitYear);
         }
      }
   }

   /**
    * This method takes timeFrameNormalizedData as input which is populated by using defaults. It is full fledged entity
    * by now according to the qualifier. We need to convert this into target DateQualifier. The conversion rules are
    * defines as follows : 1. If Asset is yearly, then all the data qualifiers can be answered by simply truncating all
    * other components other than year In other words, we can change the qualifier of TimeFrameNormalizedData object to
    * YEAR which is infact asset's qualifier. 2. If Asset is Quarterly, a. If Data Qualifier is Year, then it is
    * candidate for upper conversion and we will get two normalized data one with default first quarter , other will
    * default last quarter. b. If Data Qualifier is Quarter, no conversion involved. c. If Data Qualifier is Month, get
    * the corresponding quarter in which current month falls. d. If Data Qualifier is Day, There are two possibilities
    * here,user might have asked fully qualified day, that means month is also asked by user, then we should answer the
    * quarter value for that month, else answer the default quarter value which came because of default value for this
    * asset. 3. If Asset is Monthly, a. If Data Qualifier is Year, then it is candidate for upper conversion and we will
    * get two normalized data one with default first month , other will default last month. b. If Data Qualifier is
    * Month, no conversion involved. c. If Data Qualifier is Quarter, get the starting and ending month of that quarter
    * and build the range. d. If Data Qualifier is Day, No conversion is required as only data qualifier change to month
    * will do the job. 4. If Asset is Day, a. If Data Qualifier is Year, then it is candidate for upper conversion and
    * we will get two normalized data one with default first month , other will default last month. In addition we need
    * to put the default first day for first normalized data and last day for second normalized data. b. If Data
    * Qualifier is Month, then it is candidate for upper conversion and we will get two normalized data one with default
    * first month , other will default last month. c. If Data Qualifier is Quarter, get the starting and ending month of
    * that quarter and build the range following the same for day filling as we did for month and year. d. If Data
    * Qualifier is Day, No conversion is required.
    * 
    * @param timeFrameNormalizedData
    * @param timeFrameConversionInputTargetInfo
    * @return timeFrameNormalizedDataList
    * @throws CloneNotSupportedException
    */
   private List<TimeFrameNormalizedData> convertTimeFrameNormalizedData (
            TimeFrameNormalizedData timeFrameNormalizedData,
            TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo) throws CloneNotSupportedException {
      DateQualifier targetDateQualifier = timeFrameConversionInputTargetInfo.getTargetDateQualifier();
      List<TimeFrameNormalizedData> timeFrameNormalizedDataList = new ArrayList<TimeFrameNormalizedData>();
      timeFrameNormalizedDataList.add(timeFrameNormalizedData);
      switch (targetDateQualifier) {
         case QUARTER:
            if (DateQualifier.YEAR.equals(timeFrameNormalizedData.getDateQualifier())) {
               timeFrameNormalizedData.setQuarter(populateNormalizedDataEntity(DEFAULT_FIRST_QUARTER));
               TimeFrameNormalizedData clonedTimeFrameNormalizedData = (TimeFrameNormalizedData) timeFrameNormalizedData
                        .clone();
               clonedTimeFrameNormalizedData.setQuarter(populateNormalizedDataEntity(DEFAULT_LAST_QUARTER));
               timeFrameNormalizedDataList.add(clonedTimeFrameNormalizedData);
            } else if (DateQualifier.MONTH.equals(timeFrameNormalizedData.getDateQualifier())
                     || DateQualifier.DAY.equals(timeFrameNormalizedData.getDateQualifier())
                     || DateQualifier.HOUR.equals(timeFrameNormalizedData.getDateQualifier())
                     || DateQualifier.MINUTE.equals(timeFrameNormalizedData.getDateQualifier())) {
               String quarterValue = null;
               if (DateQualifier.MONTH.equals(timeFrameNormalizedData.getDateQualifier())) {
                  quarterValue = TimeFrameUtility.getCorrespondingQuarterForMonth(timeFrameNormalizedData.getMonth()
                           .getValue());
               } else if (DateQualifier.DAY.equals(timeFrameNormalizedData.getDateQualifier())
                        || DateQualifier.HOUR.equals(timeFrameNormalizedData.getDateQualifier())
                        || DateQualifier.MINUTE.equals(timeFrameNormalizedData.getDateQualifier())) {
                  NormalizedDataEntity monthNormalizedDataEntity = timeFrameNormalizedData.getMonth();
                  if (monthNormalizedDataEntity != null) {
                     quarterValue = TimeFrameUtility.getCorrespondingQuarterForMonth(monthNormalizedDataEntity
                              .getValue());
                  } else {
                     quarterValue = timeFrameNormalizedData.getQuarter().getValue();
                  }
               }
               timeFrameNormalizedData.setQuarter(populateNormalizedDataEntity(quarterValue));
            }
            break;
         case MONTH:
            if (DateQualifier.YEAR.equals(timeFrameNormalizedData.getDateQualifier())
                     || DateQualifier.QUARTER.equals(timeFrameNormalizedData.getDateQualifier())) {
               String firstMonth = null;
               String lastMonth = null;
               if (DateQualifier.YEAR.equals(timeFrameNormalizedData.getDateQualifier())) {
                  firstMonth = DEFAULT_FIRST_MONTH;
                  lastMonth = DEFAULT_LAST_MONTH;
               } else if (DateQualifier.QUARTER.equals(timeFrameNormalizedData.getDateQualifier())) {
                  firstMonth = getStartingMonthForQuarter(timeFrameNormalizedData.getQuarter().getValue());
                  lastMonth = getEndingMonthForQuarter(timeFrameNormalizedData.getQuarter().getValue());
               }
               timeFrameNormalizedData.setMonth(populateNormalizedDataEntity(firstMonth));
               TimeFrameNormalizedData clonedTimeFrameNormalizedData = (TimeFrameNormalizedData) timeFrameNormalizedData
                        .clone();
               clonedTimeFrameNormalizedData.setMonth(populateNormalizedDataEntity(lastMonth));
               timeFrameNormalizedDataList.add(clonedTimeFrameNormalizedData);
            }
            break;
         case DAY:
            if (DateQualifier.YEAR.equals(timeFrameNormalizedData.getDateQualifier())
                     || DateQualifier.QUARTER.equals(timeFrameNormalizedData.getDateQualifier())
                     || DateQualifier.MONTH.equals(timeFrameNormalizedData.getDateQualifier())) {
               String firstMonth = null;
               String lastMonth = null;
               if (DateQualifier.YEAR.equals(timeFrameNormalizedData.getDateQualifier())) {
                  firstMonth = DEFAULT_FIRST_MONTH;
                  lastMonth = DEFAULT_LAST_MONTH;
               } else if (DateQualifier.QUARTER.equals(timeFrameNormalizedData.getDateQualifier())) {
                  firstMonth = getStartingMonthForQuarter(timeFrameNormalizedData.getQuarter().getValue());
                  lastMonth = getEndingMonthForQuarter(timeFrameNormalizedData.getQuarter().getValue());
               } else if (DateQualifier.MONTH.equals(timeFrameNormalizedData.getDateQualifier())) {
                  firstMonth = timeFrameNormalizedData.getMonth().getValue();
                  lastMonth = timeFrameNormalizedData.getMonth().getValue();
               }
               timeFrameNormalizedData.setMonth(populateNormalizedDataEntity(firstMonth));
               timeFrameNormalizedData.setDay(populateNormalizedDataEntity(DEFAULT_FIRST_DAY));
               TimeFrameNormalizedData clonedTimeFrameNormalizedData = (TimeFrameNormalizedData) timeFrameNormalizedData
                        .clone();
               clonedTimeFrameNormalizedData.setMonth(populateNormalizedDataEntity(lastMonth));
               clonedTimeFrameNormalizedData.setDay(populateNormalizedDataEntity(monthLastDayMap.get(lastMonth)));
               timeFrameNormalizedDataList.add(clonedTimeFrameNormalizedData);
            }
            break;
         // hour and min can never come from target qualifier.
         // has to fill lower components based on data qualifier.
         case SECOND:
            String firstMonth = DEFAULT_FIRST_MONTH;
            String lastMonth = DEFAULT_LAST_MONTH;
            String firstDay = DEFAULT_FIRST_DAY;
            String lastDay = null;
            String firstHour = DEFAULT_FIRST_HOUR;
            String lastHour = DEFAULT_LAST_HOUR;
            String firstMinute = DEFAULT_FIRST_MINUTE;
            String lastMinute = DEFAULT_LAST_MINUTE;
            String firstSecond = DEFAULT_FIRST_SECOND;
            String lastSecond = DEFAULT_LAST_SECOND;
            if (DateQualifier.QUARTER.equals(timeFrameNormalizedData.getDateQualifier())) {
               firstMonth = getStartingMonthForQuarter(timeFrameNormalizedData.getQuarter().getValue());
               lastMonth = getEndingMonthForQuarter(timeFrameNormalizedData.getQuarter().getValue());
            } else if (DateQualifier.MONTH.equals(timeFrameNormalizedData.getDateQualifier())) {
               firstMonth = timeFrameNormalizedData.getMonth().getValue();
               lastMonth = timeFrameNormalizedData.getMonth().getValue();
            } else if (DateQualifier.DAY.equals(timeFrameNormalizedData.getDateQualifier())
                     || DateQualifier.HOUR.equals(timeFrameNormalizedData.getDateQualifier())
                     || DateQualifier.MINUTE.equals(timeFrameNormalizedData.getDateQualifier())
                     || DateQualifier.SECOND.equals(timeFrameNormalizedData.getDateQualifier())) {
               firstMonth = timeFrameNormalizedData.getMonth().getValue();
               lastMonth = timeFrameNormalizedData.getMonth().getValue();
               firstDay = timeFrameNormalizedData.getDay().getValue();
               lastDay = timeFrameNormalizedData.getDay().getValue();
               if (DateQualifier.HOUR.equals(timeFrameNormalizedData.getDateQualifier())) {
                  firstHour = timeFrameNormalizedData.getHour().getValue();
                  lastHour = timeFrameNormalizedData.getHour().getValue();
               } else if (DateQualifier.MINUTE.equals(timeFrameNormalizedData.getDateQualifier())) {
                  firstHour = timeFrameNormalizedData.getHour().getValue();
                  lastHour = timeFrameNormalizedData.getHour().getValue();
                  firstMinute = timeFrameNormalizedData.getMinute().getValue();
                  lastMinute = timeFrameNormalizedData.getMinute().getValue();
               } else if (DateQualifier.SECOND.equals(timeFrameNormalizedData.getDateQualifier())) {
                  firstHour = timeFrameNormalizedData.getHour().getValue();
                  lastHour = timeFrameNormalizedData.getHour().getValue();
                  firstMinute = timeFrameNormalizedData.getMinute().getValue();
                  lastMinute = timeFrameNormalizedData.getMinute().getValue();
                  firstSecond = timeFrameNormalizedData.getSecond().getValue();
                  lastSecond = timeFrameNormalizedData.getSecond().getValue();
               }
            }
            timeFrameNormalizedData.setMonth(populateNormalizedDataEntity(firstMonth));
            timeFrameNormalizedData.setDay(populateNormalizedDataEntity(firstDay));
            timeFrameNormalizedData.setHour(populateNormalizedDataEntity(firstHour));
            timeFrameNormalizedData.setMinute(populateNormalizedDataEntity(firstMinute));
            timeFrameNormalizedData.setSecond(populateNormalizedDataEntity(firstSecond));
            if (!DateQualifier.SECOND.equals(timeFrameNormalizedData.getDateQualifier())) {
               TimeFrameNormalizedData clonedTimeFrameNormalizedData = (TimeFrameNormalizedData) timeFrameNormalizedData
                        .clone();
               clonedTimeFrameNormalizedData.setMonth(populateNormalizedDataEntity(lastMonth));
               if (lastDay == null) {
                  lastDay = monthLastDayMap.get(lastMonth);
               }
               clonedTimeFrameNormalizedData.setDay(populateNormalizedDataEntity(lastDay));
               clonedTimeFrameNormalizedData.setHour(populateNormalizedDataEntity(lastHour));
               clonedTimeFrameNormalizedData.setMinute(populateNormalizedDataEntity(lastMinute));
               clonedTimeFrameNormalizedData.setSecond(populateNormalizedDataEntity(lastSecond));
               timeFrameNormalizedDataList.add(clonedTimeFrameNormalizedData);
            }
            break;
      }
      for (TimeFrameNormalizedData convertedTimeFrameNormalizedData : timeFrameNormalizedDataList) {
         convertedTimeFrameNormalizedData.setDateQualifier(targetDateQualifier);
      }
      return timeFrameNormalizedDataList;
   }

   /**
    * This method populates the missing components in the timeFrameNormalizedData using defaultTimeFrameNormalizedData
    * 
    * @param timeFrameNormalizedData
    * @param defaultTimeFrameNormalizedData
    */
   private void populateDefaults (TimeFrameNormalizedData timeFrameNormalizedData,
            TimeFrameNormalizedData defaultTimeFrameNormalizedData) {
      // need to populate year for all cases.
      if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getYear())) {
         timeFrameNormalizedData.setYear(defaultTimeFrameNormalizedData.getYear());
      }
      switch (defaultTimeFrameNormalizedData.getDateQualifier()) {
         case MONTH:
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getMonth())) {
               timeFrameNormalizedData.setMonth(defaultTimeFrameNormalizedData.getMonth());
            }
            break;
         case QUARTER:
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getQuarter())) {
               timeFrameNormalizedData.setQuarter(defaultTimeFrameNormalizedData.getQuarter());
            }
            break;
         case DAY:
            // for day month component has to be filled in.
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getMonth())) {
               timeFrameNormalizedData.setMonth(defaultTimeFrameNormalizedData.getMonth());
            }
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getDay())) {
               timeFrameNormalizedData.setDay(defaultTimeFrameNormalizedData.getDay());
            }
            break;
         case SECOND:
            // for second minute, hour, day, month components has to be filled in.
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getMonth())) {
               timeFrameNormalizedData.setMonth(defaultTimeFrameNormalizedData.getMonth());
            }
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getDay())) {
               timeFrameNormalizedData.setDay(defaultTimeFrameNormalizedData.getDay());
            }
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getHour())) {
               timeFrameNormalizedData.setHour(defaultTimeFrameNormalizedData.getHour());
            }
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getMinute())) {
               timeFrameNormalizedData.setMinute(defaultTimeFrameNormalizedData.getMinute());
            }
            if (isTimeFrameNormalizedDataComponentMissing(timeFrameNormalizedData.getSecond())) {
               timeFrameNormalizedData.setSecond(defaultTimeFrameNormalizedData.getSecond());
            }
            break;
      }
   }

   /**
    * This method returns the appropriate normalized value from the list of two values based on operator. The operator
    * here indicates since, before, after and till questions. It either picks the first value or last value in the list.
    * 
    * @param operatorType
    * @param normalizedValues
    * @param dateFormat
    * @param dateQualifier
    * @return normalizedValue
    * @throws ParseException
    */
   private String pickAppropriateNormalizedValue (OperatorType operatorType, List<String> normalizedValues,
            String dateFormat, DateQualifier dateQualifier) throws ParseException {
      String normalizedValue = null;
      switch (operatorType) {
         case GREATER_THAN:
            normalizedValue = findMinMaxNormalizedValue(normalizedValues, dateFormat, dateQualifier, true);
            break;
         case GREATER_THAN_EQUALS:
            normalizedValue = findMinMaxNormalizedValue(normalizedValues, dateFormat, dateQualifier, false);
            break;
         case LESS_THAN:
            normalizedValue = findMinMaxNormalizedValue(normalizedValues, dateFormat, dateQualifier, false);
            break;
         case LESS_THAN_EQUALS:
            normalizedValue = findMinMaxNormalizedValue(normalizedValues, dateFormat, dateQualifier, true);
            break;
      }
      return normalizedValue;
   }

   /**
    * This method returns the largest/smallest value from the list based on flag.
    * 
    * @param normalizedValues
    * @param dateFormat
    * @param dateQualifier
    * @param largest
    * @return normalizedValue
    * @throws ParseException
    */
   private String findMinMaxNormalizedValue (List<String> normalizedValues, String dateFormat,
            DateQualifier dateQualifier, boolean largest) throws ParseException {
      // collect the values in list based on format.
      List<Long> dateNormalizedValues = new ArrayList<Long>();
      for (String normalizedValue : normalizedValues) {
         if (DateQualifier.QUARTER.equals(dateQualifier)) {
            dateNormalizedValues.add(Long.parseLong(normalizedValue));
         } else {
            dateNormalizedValues.add(new SimpleDateFormat(dateFormat).parse(normalizedValue).getTime());
         }
      }
      // from the list, find the index of the largest/smallest element.
      Integer matchedIndex = 0;
      if (largest) {
         matchedIndex = getLargestElementIndex(dateNormalizedValues).intValue();
      } else {
         matchedIndex = getShortestElementIndex(dateNormalizedValues).intValue();
      }
      // return the actual element at matched index.
      return normalizedValues.get(matchedIndex);
   }

   public ITimeFrameConversionEnhancerService getTimeFrameConversionEnhancerService () {
      return timeFrameConversionEnhancerService;
   }

   public void setTimeFrameConversionEnhancerService (
            ITimeFrameConversionEnhancerService timeFrameConversionEnhancerService) {
      this.timeFrameConversionEnhancerService = timeFrameConversionEnhancerService;
   }

   public Map<String, String> getMonthLastDayMap () {
      return monthLastDayMap;
   }

   public static void setMonthLastDayMap (Map<String, String> monthLastDayMap) {
      TimeFrameHandlerServiceImpl.monthLastDayMap = monthLastDayMap;
   }

}
