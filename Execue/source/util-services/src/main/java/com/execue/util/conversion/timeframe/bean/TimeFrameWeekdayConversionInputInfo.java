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

import com.execue.core.common.bean.nlp.WeekDayNormalizedDataComponent;

/**
 * This bean represents the input info required for time frame weekday conversion. It contains weekday component and
 * conversion input info and range of values on which weekday conversion has to applied.
 * 
 * @author Vishay
 * @version 1.0
 */
public class TimeFrameWeekdayConversionInputInfo implements Cloneable {

   private WeekDayNormalizedDataComponent     weekDayNormalizedDataComponent;
   private TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo;
   private TimeFrameRangeComponent            range;

   public TimeFrameWeekdayConversionInputInfo () {
      super();
      // TODO Auto-generated constructor stub
   }

   public TimeFrameWeekdayConversionInputInfo (WeekDayNormalizedDataComponent weekDayNormalizedDataComponent,
            TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo, TimeFrameRangeComponent range) {
      super();
      this.weekDayNormalizedDataComponent = weekDayNormalizedDataComponent;
      this.timeFrameConversionInputTargetInfo = timeFrameConversionInputTargetInfo;
      this.range = range;
   }

   public WeekDayNormalizedDataComponent getWeekDayNormalizedDataComponent () {
      return weekDayNormalizedDataComponent;
   }

   public void setWeekDayNormalizedDataComponent (WeekDayNormalizedDataComponent weekDayNormalizedDataComponent) {
      this.weekDayNormalizedDataComponent = weekDayNormalizedDataComponent;
   }

   public TimeFrameRangeComponent getRange () {
      return range;
   }

   public void setRange (TimeFrameRangeComponent range) {
      this.range = range;
   }

   @Override
   public Object clone () throws CloneNotSupportedException {
      TimeFrameWeekdayConversionInputInfo clonedTimeFrameWeekdayConversionInputInfo = (TimeFrameWeekdayConversionInputInfo) super
               .clone();
      clonedTimeFrameWeekdayConversionInputInfo
               .setTimeFrameConversionInputTargetInfo((TimeFrameConversionInputTargetInfo) timeFrameConversionInputTargetInfo
                        .clone());
      clonedTimeFrameWeekdayConversionInputInfo.setRange((TimeFrameRangeComponent) range.clone());
      clonedTimeFrameWeekdayConversionInputInfo
               .setWeekDayNormalizedDataComponent((WeekDayNormalizedDataComponent) weekDayNormalizedDataComponent
                        .clone());
      return clonedTimeFrameWeekdayConversionInputInfo;
   }

   public TimeFrameConversionInputTargetInfo getTimeFrameConversionInputTargetInfo () {
      return timeFrameConversionInputTargetInfo;
   }

   public void setTimeFrameConversionInputTargetInfo (
            TimeFrameConversionInputTargetInfo timeFrameConversionInputTargetInfo) {
      this.timeFrameConversionInputTargetInfo = timeFrameConversionInputTargetInfo;
   }
}
