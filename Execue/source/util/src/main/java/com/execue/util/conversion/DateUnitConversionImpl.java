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
package com.execue.util.conversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author Nitesh
 *
 */
public class DateUnitConversionImpl implements IDateUnitConversion {
   
   public DateConversion convert(DateConversionInfo dateConversionInfo) throws ParseException {
      
      String value = dateConversionInfo.getInput();
      DateUnitType sourceConversion = dateConversionInfo.getSourceUnitType();
      DateUnitType destConversion = dateConversionInfo.getDestinationUnitType();
      
      DateConversion dc = null;
      if(sourceConversion == DateUnitType.YEAR && destConversion == DateUnitType.QUARTER) {
         dc = new DateConversion();
         List<Date> convertedDates = getCovertedDatesForQuarter(value);
         
         dc.setDateUnitType(DateUnitType.QUARTER);
         dc.setConvertedDates(convertedDates);
      } else if(sourceConversion == DateUnitType.YEAR && destConversion == DateUnitType.MONTH) {
         List<Date> convertedDates = getCovertedDatesForMonth(value);
         dc = new DateConversion();
         dc.setDateUnitType(DateUnitType.MONTH);
         dc.setConvertedDates(convertedDates);
      } else if(sourceConversion == DateUnitType.QUARTER && destConversion == DateUnitType.MONTH) {
         //TODO: Need to handle 
      }
      return dc; 
   }
   
   private List<Date> getCovertedDatesForQuarter (String value) throws ParseException {
      List<Date> convertedDates = new ArrayList<Date>();
      for(int i=0; i<4; i++) {
         String convertedValue = value + ((i < 3) ? "0" + ((i + 1) * 3) : ((i + 1) * 3));  
         convertedDates.add(getDate("yyyyMM", convertedValue));
      }
      return convertedDates;
   }
   
   private List<Date> getCovertedDatesForMonth (String value) throws ParseException {
      List<Date> convertedDates = new ArrayList<Date>();
      for(int i=1; i<=12; i++) {
         String convertedValue = value + ((i < 9) ? "0" + i: i);  
         convertedDates.add(getDate("yyyyMM", convertedValue));
      }
      return convertedDates;
   }

   private Date getDate (String dateFormat, String currentValue) throws ParseException {
      SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
      return sdf.parse(currentValue);
   }
}
