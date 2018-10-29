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


package com.execue.util.conversion.timeframe;

import java.util.Date;
import java.util.List;

import com.execue.core.common.type.DateQualifier;
import com.execue.util.conversion.timeframe.bean.TimeFrameRangeComponent;
import com.execue.util.conversion.timeframe.bean.TimeFrameWeekdayConversionInputInfo;

/**
 * This service contains methods need to enhance the basic time frame conversion.It has methods for weekday conversion
 * and finding the range between fromDate and toDate.
 * 
 * @author Vishay
 * @version 1.0
 */
public interface ITimeFrameConversionEnhancerService {

   public List<Date> timeFrameWeekDayConversion (TimeFrameWeekdayConversionInputInfo weekdayConversionInputInfo)
            throws Exception;

   public List<TimeFrameRangeComponent> adjustSelectedDaysTimeOnBoundaries (Date fromDate, Date toDate,
            List<Date> selectedDays, String format) throws Exception;

   public List<TimeFrameRangeComponent> timeFrameDateTimeRangeConversion (Date fromDate, Date toDate, String format,
            DateQualifier dateQualifier) throws Exception;

}
