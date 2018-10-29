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


package com.execue.scheduler.util;

import java.util.Date;

import org.quartz.TriggerUtils;

public class DateUtils {

   /**
    * Returns a date that is rounded to the next even hour above the given date. For example an input date with a time
    * of 08:13:54 would result in a date with the time of 09:00:00. If the date's time is in the 23rd hour, the date's
    * 'day' will be promoted, and the time will be set to 00:00:00. Parameters: date - the Date to round, if null the
    * current time will be used Returns: the new rounded date
    * 
    * @param date
    * @return
    */
   public static Date getEvenHourDate (Date date) {
      return TriggerUtils.getEvenHourDate(date);
   }

   /**
    * Returns a date that is rounded to the previous even hour below the given date. For example an input date with a
    * time of 08:13:54 would result in a date with the time of 08:00:00. Parameters: date - the Date to round, if null
    * the current time will be used Returns: the new rounded date
    * 
    * @param date
    * @return
    */
   public static Date getEvenHourDateBefore (Date date) {
      return TriggerUtils.getEvenHourDateBefore(date);
   }

   /**
    * Returns a date that is rounded to the next even minute above the given date. For example an input date with a time
    * of 08:13:54 would result in a date with the time of 08:14:00. If the date's time is in the 59th minute, then the
    * hour (and possibly the day) will be promoted. Parameters: date - the Date to round, if null the current time will
    * be used Returns: the new rounded date
    * 
    * @param date
    * @return
    */
   public static Date getEvenMinuteDate (Date date) {
      return TriggerUtils.getEvenMinuteDate(date);
   }

   /**
    * Returns a date that is rounded to the previous even minute below the given date. For example an input date with a
    * time of 08:13:54 would result in a date with the time of 08:13:00. Parameters: date - the Date to round, if null
    * the current time will be used Returns: the new rounded date
    * 
    * @param date
    * @return
    */
   public static Date getEvenMinuteDateBefore (Date date) {
      return TriggerUtils.getEvenMinuteDateBefore(date);
   }

   /**
    * Returns a date that is rounded to the next even second above the given date. Parameters: date - the Date to round,
    * if null the current time will be used Returns: the new rounded date
    * 
    * @param date
    * @return
    */
   public static Date getEvenSecondDate (Date date) {
      return TriggerUtils.getEvenSecondDate(date);
   }

   /**
    * Returns a date that is rounded to the previous even second below the given date. For example an input date with a
    * time of 08:13:54.341 would result in a date with the time of 08:13:00.000. Parameters: date - the Date to round,
    * if null the current time will be used Returns: the new rounded date
    * 
    * @param date
    * @return
    */
   public static Date getEvenSecondDateBefore (Date date)

   {
      return TriggerUtils.getEvenSecondDateBefore(date);
   }

   /**
    * Returns a date that is rounded to the next even multiple of the given minute. For example an input date with a
    * time of 08:13:54, and an input minute-base of 5 would result in a date with the time of 08:15:00. The same input
    * date with an input minute-base of 10 would result in a date with the time of 08:20:00. But a date with the time
    * 08:53:31 and an input minute-base of 45 would result in 09:00:00, because the even-hour is the next 'base' for
    * 45-minute intervals. More examples: Input Time Minute-Base Result Time 11:16:41 20 11:20:00 11:36:41 20 11:40:00
    * 11:46:41 20 12:00:00 11:26:41 30 11:30:00 11:36:41 30 12:00:00 11:16:41 17 11:17:00 11:17:41 17 11:34:00 11:52:41
    * 17 12:00:00 11:52:41 5 11:55:00 11:57:41 5 12:00:00 11:17:41 0 12:00:00 11:17:41 1 11:08:00 Parameters: date - the
    * Date to round, if null the current time will be used minuteBase - the base-minute to set the time on Returns: the
    * new rounded date See Also: getNextGivenSecondDate(Date, int)
    * 
    * @param date
    * @param minuteBase
    * @return
    */
   public static Date getNextGivenMinuteDate (Date date, int minuteBase) {
      return TriggerUtils.getNextGivenMinuteDate(date, minuteBase);
   }

   /**
    * Returns a date that is rounded to the next even multiple of the given minute. The rules for calculating the second
    * are the same as those for calculating the minute in the method getNextGivenMinuteDate(..). Parameters: date - the
    * Date to round, if null the current time will be used secondBase - the base-second to set the time on Returns: the
    * new rounded date See Also: getNextGivenMinuteDate(Date, int)
    * 
    * @param date
    * @param secondBase
    * @return
    */
   public static Date getNextGivenSecondDate (Date date, int secondBase)

   {
      return TriggerUtils.getNextGivenSecondDate(date, secondBase);
   }

   /**
    * Get a Date object that represents the given time, on today's date. Parameters: second - The value (0-59) to give
    * the seconds field of the date minute - The value (0-59) to give the minutes field of the date hour - The value
    * (0-23) to give the hours field of the date Returns: the new date
    * 
    * @param second
    * @param minute
    * @param hour
    * @return
    */
   public static Date getDateOf (int second, int minute, int hour)

   {
      return TriggerUtils.getDateOf(second, minute, hour);
   }

   /**
    * Get a Date object that represents the given time, on the given date. Parameters: second - The value (0-59) to give
    * the seconds field of the date minute - The value (0-59) to give the minutes field of the date hour - The value
    * (0-23) to give the hours field of the date dayOfMonth - The value (1-31) to give the day of month field of the
    * date month - The value (1-12) to give the month field of the date Returns: the new date
    * 
    * @param second
    * @param minute
    * @param hour
    * @param dayOfMonth
    * @param month
    * @return
    */
   public static Date getDateOf (int second, int minute, int hour, int dayOfMonth, int month)

   {
      return TriggerUtils.getDateOf(second, minute, hour, dayOfMonth, month);
   }

   /**
    * Get a Date object that represents the given time, on the given date. Parameters: second - The value (0-59) to give
    * the seconds field of the date minute - The value (0-59) to give the minutes field of the date hour - The value
    * (0-23) to give the hours field of the date dayOfMonth - The value (1-31) to give the day of month field of the
    * date month - The value (1-12) to give the month field of the date year - The value (1970-2099) to give the year
    * field of the date Returns: the new date
    * 
    * @param second
    * @param minute
    * @param hour
    * @param dayOfMonth
    * @param month
    * @param year
    * @return
    */
   public static Date getDateOf (int second, int minute, int hour, int dayOfMonth, int month, int year)

   {
      return TriggerUtils.getDateOf(second, minute, hour, dayOfMonth, month, year);
   }

}
