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


/**
 * 
 */
package com.execue.core.common.bean.entity;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.type.IBaseEnumType;

/**
 * @author Nitesh
 */
public enum TimeFrameType implements IBaseEnumType {

   DAY_TIME_FRAME ("DayTimeFrame"), WEEK_TIME_FRAME ("WeekTimeFrame"), MONTH_TIME_FRAME ("MonthTimeFrame"), QUARTER_TIME_FRAME (
            "QuarterTimeFrame"), YEAR_TIME_FRAME ("YearTimeFrame"), YEAR_TIME_FRAME_CONCEPT ("YearTimeFrameConcept"), TIME (
            "Time");

   private String                                  value;
   private static final Map<String, TimeFrameType> reverseLookupMap = new HashMap<String, TimeFrameType>();

   static {
      for (TimeFrameType timeFrameType : EnumSet.allOf(TimeFrameType.class)) {
         reverseLookupMap.put(timeFrameType.value, timeFrameType);
      }
   }

   TimeFrameType (String className) {
      this.value = className;
   }

   public String getValue () {
      return this.value;
   }

   public static TimeFrameType getTimeFrameType (String value) {
      return reverseLookupMap.get(value);
   }
}
