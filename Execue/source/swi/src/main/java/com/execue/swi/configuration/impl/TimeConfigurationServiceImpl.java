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


package com.execue.swi.configuration.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.util.TimeRange;
import com.execue.core.configuration.IConfiguration;
import com.execue.swi.configuration.ITimeConfigurationService;

/**
 * Class to load the time related configurations.
 * 
 * @author Prsanana
 */
public class TimeConfigurationServiceImpl implements ITimeConfigurationService {

   private static final String    TIME_RANGE_NAME       = "time.timeRanges.timeRange.rangeName";
   private static final String    TIME_RANGE_START_TIME = "time.timeRanges.timeRange.startTime";
   private static final String    TIME_RANGE_END_TIME   = "time.timeRanges.timeRange.endTime";

   private Map<String, TimeRange> timeRangeMap;
   private IConfiguration         timeConfiguration;

   public void loadTimeRangeDetails () {
      Map<String, TimeRange> timeRangeMap = new HashMap<String, TimeRange>(1);
      List<String> rangeNameList = getTimeConfiguration().getList(TIME_RANGE_NAME);
      List<String> startTimeList = getTimeConfiguration().getList(TIME_RANGE_START_TIME);
      List<String> endTimeList = getTimeConfiguration().getList(TIME_RANGE_END_TIME);

      int size = rangeNameList.size();
      for (int rangeIndex = 0; rangeIndex < size; rangeIndex++) {
         String rangeName = (String) rangeNameList.get(rangeIndex);
         String startTime = (String) startTimeList.get(rangeIndex);
         String endTime = (String) endTimeList.get(rangeIndex);
         TimeRange timeRange = new TimeRange(rangeName, startTime, endTime);
         timeRangeMap.put(rangeName.toLowerCase(), timeRange);
      }
      this.timeRangeMap = timeRangeMap;
   }

   public TimeRange getTimeRange (String rangeName) {
      return this.timeRangeMap.get(rangeName.toLowerCase());
   }

   public Map<String, TimeRange> getTimeRangeMap () {
      return timeRangeMap;
   }

   /**
    * @return the timeConfiguration
    */
   public IConfiguration getTimeConfiguration () {
      return timeConfiguration;
   }

   /**
    * @param timeConfiguration the timeConfiguration to set
    */
   public void setTimeConfiguration (IConfiguration timeConfiguration) {
      this.timeConfiguration = timeConfiguration;
   }
}
