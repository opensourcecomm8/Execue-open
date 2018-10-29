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


package com.execue.util.conversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

/**
 * This service evaluates the date formula for the input value to get evaluated value
 * 
 * @author Vishay
 * @version 1.0
 * @since 29/10/09
 */
public class DynamicDateFormulaEvaluationServiceImpl implements IDynamicDateFormulaEvaluationService {

   private static final Logger log = Logger.getLogger(DynamicDateFormulaEvaluationServiceImpl.class);

   // sample formula : (YYYY, YEAR, 1996, -, ?)
   public String evaluateDateFormula (String dateFormulaExpression, Object inputValue) {
      String evaluatedValue = null;
      try {
         String substring = dateFormulaExpression.substring(1, dateFormulaExpression.length() - 1);
         StringTokenizer stringTokenizer = new StringTokenizer(substring, ",");
         String dateFormat = stringTokenizer.nextToken().trim();
         // support for year, month and quarter(Support for quarter is not there)
         String dataUnit = stringTokenizer.nextToken().trim();
         String defaultValue = stringTokenizer.nextToken().trim();
         // add and deletion
         String operator = stringTokenizer.nextToken().trim();
         if ("QUARTER".equalsIgnoreCase(dataUnit)) {
            evaluatedValue = getQuarterValueRequested(dateFormat, dataUnit, defaultValue, operator, inputValue
                     .toString());
         } else if ("MONTH".equalsIgnoreCase(dataUnit)) {
            evaluatedValue = getMonthValueRequested(dateFormat, dataUnit, defaultValue, operator, inputValue.toString());
         } else { // YEAR
            SimpleDateFormat sourceDateFormat = new SimpleDateFormat(dateFormat);
            Date sourceDate = sourceDateFormat.parse(defaultValue);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sourceDate);
            int calendarField = getCorrepondingCalendarField(dataUnit);
            int calendarValueToAbsorb = getCalendarValueToAbsorb(inputValue, operator);
            if (!(calendarField == -1 || calendarValueToAbsorb == 0)) {
               calendar.add(calendarField, calendarValueToAbsorb);
               Date updatedDate = calendar.getTime();
               evaluatedValue = sourceDateFormat.format(updatedDate);
            }
         }
      } catch (java.text.ParseException e) {
         e.printStackTrace();
      }
      return evaluatedValue;
   }

   private String getMonthValueRequested (String dateFormat, String dataUnit, String currentValue, String operator,
            String inputValue) throws ParseException {
      String requestedMonthValue = null;

      // Get the indexes of Month format
      List<Integer> monthIndexes = getMonthIndexes(dateFormat);

      // Get the month format
      String monthFormat = getMonthFormat(dateFormat, monthIndexes);

      // Current Date
      Date currentDate = getDate(dateFormat, currentValue);

      // Requested Date
      Date dateRequested = getRequestedDateForMonth(currentDate, Integer.parseInt(inputValue), operator);

      // Requested Month
      String requestedMonth = getMonth(dateRequested, monthFormat);

      // Get format with out Month
      String dateFormatExcludingMonth = getFormatExcludingQuarter(dateFormat, monthIndexes);

      // Formatted Requested date with out month
      String formattedDateWithOutMonth = getString(dateRequested, dateFormatExcludingMonth);

      // Requested date with month formatted
      requestedMonthValue = getQuarterAdjustedString(requestedMonth, formattedDateWithOutMonth, dateFormat,
               monthIndexes);
      return requestedMonthValue;
   }

   private String getMonthFormat (String dateFormat, List<Integer> monthIndexes) {
      String monthFormat = "";
      for (int integer : monthIndexes) {
         monthFormat += dateFormat.substring(integer, integer + 1);
      }
      return monthFormat;
   }

   private int getCalendarValueToAbsorb (Object inputValue, String operator) {
      int calendarValue = 0;
      calendarValue = Integer.parseInt(inputValue.toString());
      if (operator.equalsIgnoreCase("-")) {
         calendarValue = -calendarValue;
      } else {
         calendarValue = +calendarValue;
      }
      return calendarValue;
   }

   private int getCorrepondingCalendarField (String dataUnit) {
      int calendarField = -1;
      if (dataUnit.equalsIgnoreCase("YEAR")) {
         calendarField = Calendar.YEAR;
      } else if (dataUnit.equalsIgnoreCase("MONTH")) {
         calendarField = Calendar.MONTH;
      }
      return calendarField;
   }

   private String getQuarterValueRequested (String dateFormat, String dateUnit, String currentValue, String operator,
            String inputValue) throws ParseException {
      String requestedQuarterValue = null;
      // Get the indexes of Quarter format
      List<Integer> quarterIndexes = getQuarterIndexes(dateFormat);
      // Get the quarter format
      String quarterFormat = getQuarterFormat(dateFormat, quarterIndexes);

      Integer currentQuarterValue = getQuarterValue(currentValue, quarterIndexes);

      // Get format with out Quarter
      String dateFormatExcludingQuarter = getFormatExcludingQuarter(dateFormat, quarterIndexes);
      // Value excluding quarter
      String currentValueExcludingQuarter = getValueExcludingQuarter(currentValue, quarterIndexes);
      // Date with out Quarter
      Date dateExcludingQuarter = getDate(dateFormatExcludingQuarter, currentValueExcludingQuarter);

      // Add months based on quarter that is excluded
      Calendar cal = GregorianCalendar.getInstance();
      cal.setTime(dateExcludingQuarter);
      cal.add(Calendar.MONTH, ((currentQuarterValue * 3) - 1));
      dateExcludingQuarter = cal.getTime();

      // Requested Date
      Date dateRequested = getRequestedQuarterBasedDate(dateExcludingQuarter, Integer.parseInt(inputValue), operator);
      // Requested Quarter
      String requestedQuarter = getQuarter(dateRequested, quarterFormat);
      // Formatted Requested date with out quarter
      String formattedDateWithOutQuarter = getString(dateRequested, dateFormatExcludingQuarter);
      // Requested date with Quarter formatted
      requestedQuarterValue = getQuarterAdjustedString(requestedQuarter, formattedDateWithOutQuarter, dateFormat,
               quarterIndexes);
      return requestedQuarterValue;
   }

   private Integer getQuarterValue (String currentValue, List<Integer> quarterIndexes) {
      String quarterValue = "";
      for (Integer integer : quarterIndexes) {
         quarterValue += currentValue.charAt(integer);
      }
      return new Integer(quarterValue);
   }

   private List<Integer> getQuarterIndexes (String dateFormat) {
      List<Integer> indexes = new ArrayList<Integer>();
      int fromIndex = 0;
      int currentIndex = 0;
      int length = dateFormat.length();
      while ((currentIndex = dateFormat.indexOf("Q", fromIndex)) >= 0) {
         indexes.add(currentIndex);
         fromIndex = currentIndex + 1;
         if (fromIndex >= length) {
            break;
         }
      }
      return indexes;
   }

   private List<Integer> getMonthIndexes (String dateFormat) {
      List<Integer> indexes = new ArrayList<Integer>();
      int fromIndex = 0;
      int currentIndex = 0;
      int length = dateFormat.length();
      while ((currentIndex = dateFormat.indexOf("M", fromIndex)) >= 0) {
         indexes.add(currentIndex);
         fromIndex = currentIndex + 1;
         if (fromIndex >= length) {
            break;
         }
      }
      return indexes;
   }

   private String getQuarterFormat (String dateFormat, List<Integer> quarterIndexes) {
      String quarterFormat = "";
      for (int integer : quarterIndexes) {
         quarterFormat += dateFormat.substring(integer, integer + 1);
      }
      return quarterFormat;
   }

   private String getFormatExcludingQuarter (String dateFormat, List<Integer> quarterIndexes) {
      String format = "";
      for (int index = 0; index < dateFormat.length(); index++) {
         if (!quarterIndexes.contains(index)) {
            format += dateFormat.substring(index, index + 1);
         }
      }
      return format;
   }

   private String getValueExcludingQuarter (String currentValue, List<Integer> quarterIndexes) {
      String correctedValue = "";
      for (int index = 0; index < currentValue.length(); index++) {
         if (!quarterIndexes.contains(index)) {
            correctedValue += currentValue.substring(index, index + 1);
         }
      }
      return correctedValue;
   }

   private Date getDate (String dateFormatExcludingQuarter, String currentValueExcludingQuarter) throws ParseException {
      SimpleDateFormat sdf = new SimpleDateFormat(dateFormatExcludingQuarter);
      return sdf.parse(currentValueExcludingQuarter);
   }

   private String getString (Date date, String format) {
      SimpleDateFormat sdf = new SimpleDateFormat(format);
      return sdf.format(date);
   }

   private String getQuarterAdjustedString (String adjustedQuarter, String adjustedDate, String sourceFormat,
            List<Integer> quarterIndexes) {
      String requestedDateString = "";
      if (quarterIndexes.get(0) == 0) {
         requestedDateString = adjustedQuarter;
         requestedDateString += adjustedDate;
      } else if (quarterIndexes.size() > 1 && quarterIndexes.get(1) == sourceFormat.length() - 1) {
         requestedDateString = adjustedDate;
         requestedDateString += adjustedQuarter;
      } else {
         requestedDateString = adjustedDate.substring(0, quarterIndexes.get(0));
         requestedDateString += adjustedQuarter;
         requestedDateString += adjustedDate.substring(quarterIndexes.get(0));
      }
      return requestedDateString;
   }

   private Date getRequestedQuarterBasedDate (Date dateExcludingQuarter, int inputValue, String operator) {
      Date requestedDate = null;
      if (log.isDebugEnabled()) {
         log.debug("date In : " + dateExcludingQuarter);
      }
      int months = inputValue * 3;
      Calendar cal = GregorianCalendar.getInstance();
      cal.setTime(dateExcludingQuarter);
      if ("-".equals(operator)) {
         cal.add(Calendar.MONTH, -(months));
      } else {
         cal.add(Calendar.MONTH, (months));
      }
      requestedDate = cal.getTime();
      if (log.isDebugEnabled()) {
         log.debug("Date Out :" + requestedDate);
      }
      return requestedDate;
   }

   private Date getRequestedDateForMonth (Date date, int inputValue, String operator) {
      Date requestedDate = null;
      if (log.isDebugEnabled()) {
         log.debug("date In : " + date);
      }
      int months = inputValue;
      Calendar cal = GregorianCalendar.getInstance();
      cal.setTime(date);
      if ("-".equals(operator)) {
         cal.add(Calendar.MONTH, -(months));
      } else {
         cal.add(Calendar.MONTH, (months));
      }
      requestedDate = cal.getTime();
      if (log.isDebugEnabled()) {
         log.debug("Date Out :" + requestedDate);
      }
      return requestedDate;
   }

   private String getMonth (Date date, String monthFormat) {
      Calendar cal = GregorianCalendar.getInstance();
      cal.setTime(date);
      int month = cal.get(Calendar.MONTH) + 1; // NK: month number is coming 1 less, so adding 1 for now
      String requestedMonth = "" + month;
      while (requestedMonth.length() != monthFormat.length()) {
         requestedMonth = "0" + requestedMonth;
      }
      return requestedMonth;
   }

   private String getQuarter (Date date, String quarterFormat) {
      Calendar cal = GregorianCalendar.getInstance();
      cal.setTime(date);
      int month = cal.get(Calendar.MONTH);
      double quarterBase = Math.ceil((month + 1) * 1.0 / 3.0);
      int quarter = (int) quarterBase;
      String requestedQuarter = "" + quarter;
      while (requestedQuarter.length() != quarterFormat.length()) {
         requestedQuarter = "0" + requestedQuarter;
      }
      return requestedQuarter;
   }

}
