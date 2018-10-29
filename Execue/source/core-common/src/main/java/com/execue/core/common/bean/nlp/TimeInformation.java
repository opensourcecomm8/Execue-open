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
package com.execue.core.common.bean.nlp;

import com.execue.core.common.type.DateQualifier;

/**
 * @author Nihar Agrawal
 */
public class TimeInformation {

   NormalizedDataEntity hour;
   NormalizedDataEntity minute;
   NormalizedDataEntity second;
   NormalizedDataEntity timeQualifier;
   DateQualifier        timeUnitQualifier;

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

   /**
    * @return the timeUnitQualifier
    */
   public DateQualifier getTimeUnitQualifier () {
      return timeUnitQualifier;
   }

   /**
    * @param timeUnitQualifier
    *           the timeUnitQualifier to set
    */
   public void setTimeUnitQualifier (DateQualifier timeUnitQualifier) {
      this.timeUnitQualifier = timeUnitQualifier;
   }

}
