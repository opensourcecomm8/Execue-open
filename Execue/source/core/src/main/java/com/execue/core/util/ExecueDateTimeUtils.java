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


package com.execue.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.format.ISODateTimeFormat;

import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;

/**
 * A utility class for date time utility methods.
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/08/09
 */
public class ExecueDateTimeUtils {

   public static java.sql.Time getSQLTimeFromString (String dateTimeInISODateTimeFormat) {
      return new java.sql.Time(ISODateTimeFormat.dateTime().parseDateTime(dateTimeInISODateTimeFormat).toDate()
               .getTime());
   }

   public static java.sql.Date getSQLDateFromString (String dateTimeInISODateTimeFormat) {
      return new java.sql.Date(ISODateTimeFormat.dateTime().parseDateTime(dateTimeInISODateTimeFormat).toDate()
               .getTime());
   }

   public static java.sql.Timestamp getSQLTimeStampFromString (String dateTimeInISODateTimeFormat) {
      return new java.sql.Timestamp(ISODateTimeFormat.dateTime().parseDateTime(dateTimeInISODateTimeFormat).toDate()
               .getTime());
   }

   public static java.util.Date getDateFromString (String dateTimeInISODateTimeFormat) {
      return ISODateTimeFormat.dateTime().parseDateTime(dateTimeInISODateTimeFormat).toDate();
   }

   public static java.sql.Date getSQLDateFromDate (java.util.Date utilDate) {
      return new java.sql.Date(utilDate.getTime());
   }

   public static java.sql.Date getSQLDateFromStringDate (String dateInUniversalDateFormat) throws ExeCueException {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      try {
         return new java.sql.Date(dateFormat.parse(dateInUniversalDateFormat).getTime());
      } catch (ParseException parseException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, parseException);
      }
   }

   public static String getISODateTimeFormatFromDate (Date date) {
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      return formatter.format(date);
   }

   public static String getFormattedStringFromDate (Date date, String format) {
      DateFormat df = new SimpleDateFormat(format);
      return df.format(date);
   }

   public static java.sql.Date getSQLDateFromStringDate (String dateAsString, SimpleDateFormat dateFormat)
            throws ExeCueException {
      try {
         return new java.sql.Date(dateFormat.parse(dateAsString).getTime());
      } catch (ParseException parseException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, parseException);
      }
   }

   public static int getCorrespondingBooleanValueAsInteger (String stringBooleanValue) {
      int intBooleanValue = 0;
      if ("true".equalsIgnoreCase(stringBooleanValue)) {
         intBooleanValue = 1;
      }
      return intBooleanValue;
   }

   public static boolean getCorrespondingBooleanValueAsBoolean (String stringBooleanValue) {
      boolean booleanValue = false;
      if ("true".equalsIgnoreCase(stringBooleanValue)) {
         booleanValue = true;
      }
      return booleanValue;
   }

   /**
    * Pick up all the specified Days (of each of the week) from the provided range of dates.
    * 
    * @param fromDate -
    *           lower limit of the range, inclusive
    * @param toDate -
    *           upper limit of the date range, inclusive
    * @param weekday -
    *           Calendar.MONDAY | Calendar.FRIDAY etc
    * @return List of days by specified day of the week with time component truncated
    */
   public static List<Date> getAllDaysInRangeByWeekday (Date fromDate, Date toDate, int weekday) {
      List<Date> requestedDays = new ArrayList<Date>();

      // Build a calendar from lower limit and truncate the time component
      Calendar fromCal = GregorianCalendar.getInstance();
      fromCal.setTime(fromDate);
      fromCal.set(Calendar.HOUR_OF_DAY, 0);
      fromCal.set(Calendar.MINUTE, 0);
      fromCal.set(Calendar.SECOND, 0);
      fromCal.set(Calendar.MILLISECOND, 0);

      // Build a calendar from upper limit and truncate the time component
      Calendar toCal = GregorianCalendar.getInstance();
      toCal.setTime(toDate);
      toCal.set(Calendar.HOUR_OF_DAY, 0);
      toCal.set(Calendar.MINUTE, 0);
      toCal.set(Calendar.SECOND, 0);
      toCal.set(Calendar.MILLISECOND, 0);

      boolean firstDayFound = false;
      for (; fromCal.compareTo(toCal) < 1;) {
         // Check if first occurrence of the requested day found, if not
         // check the current
         // day against the request day.
         // If found, mark fisrtDayFoudn as true and add it to the list of
         // requested days
         // increment the lower limit calendar by 7 days (a week) and
         // continue the loop
         if (!firstDayFound && fromCal.get(Calendar.DAY_OF_WEEK) == weekday) {
            requestedDays.add(fromCal.getTime());
            firstDayFound = true;
            fromCal.add(Calendar.DAY_OF_MONTH, 7);
            continue;
         } else if (firstDayFound) {
            // add the current date from lower limit calendar to requested
            // days
            // and again increment the lower limit calendar by 7 days (a
            // week)
            requestedDays.add(fromCal.getTime());
            fromCal.add(Calendar.DAY_OF_MONTH, 7);
         } else {
            // increment the lower limit calendar by 1 day
            fromCal.add(Calendar.DAY_OF_MONTH, 1);
         }
      }
      return requestedDays;
   }

   public static List<Date> getAllDaysInRange (Date fromDate, Date toDate) {
      List<Date> requestedDays = new ArrayList<Date>();

      // Build a calendar from lower limit and truncate the time component
      Calendar fromCal = GregorianCalendar.getInstance();
      fromCal.setTime(fromDate);
      fromCal.set(Calendar.HOUR_OF_DAY, 0);
      fromCal.set(Calendar.MINUTE, 0);
      fromCal.set(Calendar.SECOND, 0);
      fromCal.set(Calendar.MILLISECOND, 0);

      // Build a calendar from upper limit and truncate the time component
      Calendar toCal = GregorianCalendar.getInstance();
      toCal.setTime(toDate);
      toCal.set(Calendar.HOUR_OF_DAY, 0);
      toCal.set(Calendar.MINUTE, 0);
      toCal.set(Calendar.SECOND, 0);
      toCal.set(Calendar.MILLISECOND, 0);

      for (; fromCal.compareTo(toCal) < 1;) {
         requestedDays.add(fromCal.getTime());
         // increment the lower limit calendar by 1 day
         fromCal.add(Calendar.DAY_OF_MONTH, 1);
      }
      return requestedDays;
   }

   public static boolean areDatesSameTillDayLevel (Date firstDate, Date secondDate) {
      Date firstDateExceptTime = truncateTimeFromDate(firstDate);
      Date secondDateExceptTime = truncateTimeFromDate(secondDate);
      return firstDateExceptTime.equals(secondDateExceptTime);
   }

   public static int compareDatesTillDayLevel (Date firstDate, Date secondDate) {
      Date firstDateExceptTime = truncateTimeFromDate(firstDate);
      Date secondDateExceptTime = truncateTimeFromDate(secondDate);
      return firstDateExceptTime.compareTo(secondDateExceptTime);
   }

   public static boolean areDatesSame (Date firstDate, Date secondDate) {
      return firstDate.equals(secondDate);
   }

   public static Date truncateTimeFromDate (Date date) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      return calendar.getTime();
   }

   public static String convertTwoToFourDigitYear (String twoDigitYear) {
      int year = Integer.parseInt(twoDigitYear);
      Calendar c = GregorianCalendar.getInstance();
      int centuryInt = c.get(Calendar.YEAR) - year;
      return StringUtils.left(Integer.toString(centuryInt), 2) + twoDigitYear;
   }

   public static Date getTodaysDate () {
      return Calendar.getInstance().getTime();
   }

   public static Date getOldDateByDays (Integer days) {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DAY_OF_YEAR, -days);
      return calendar.getTime();
   }

   public static Date getAdvanceDateByDays (Integer days) {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DAY_OF_YEAR, days);
      return calendar.getTime();
   }

   public static Date getOldDateByMonths (Integer months) {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.MONTH, -months);
      return calendar.getTime();
   }

   public static Date getAdvanceDateByMonths (Integer months) {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.MONTH, months);
      return calendar.getTime();
   }
}
