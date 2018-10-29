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

import java.util.Comparator;
import java.util.Date;

import com.execue.util.conversion.timeframe.bean.TimeFrameRangeComponent;
import com.execue.util.conversion.timeframe.util.TimeFrameUtility;

/**
 * This comparator class is used to compare two range elements. We need format to convert them to date and then compare.
 * 
 * @author Vishay
 * @version 1.0
 */
public class TimeFrameRangeComparator implements Comparator<TimeFrameRangeComponent> {

   private String dateFormat;

   public TimeFrameRangeComparator (String dateFormat) {
      super();
      this.dateFormat = dateFormat;
   }

   public int compare (TimeFrameRangeComponent firstTimeFrameRangeComponent,
            TimeFrameRangeComponent secondTimeFrameRangeComponent) {
      Date firstDate = TimeFrameUtility.buildDateObject(firstTimeFrameRangeComponent.getLowerRange(), dateFormat);
      Date secondDate = TimeFrameUtility.buildDateObject(secondTimeFrameRangeComponent.getLowerRange(), dateFormat);
      return firstDate.compareTo(secondDate);
   }

}
