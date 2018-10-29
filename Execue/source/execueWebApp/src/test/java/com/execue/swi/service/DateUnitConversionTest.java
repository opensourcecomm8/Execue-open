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


package com.execue.swi.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.Conversion;
import com.execue.core.exception.swi.SWIException;
import com.execue.util.conversion.DateConversion;
import com.execue.util.conversion.DateConversionInfo;
import com.execue.util.conversion.DateUnitType;

/**
 * This test case is for testing DateType conversions
 * 
 * @author Nitesh
 * @version 1.0
 * @since 10/11/09
 */
public class DateUnitConversionTest extends ExeCueBaseTest {

   @Before
   public void setUp () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTeardown();
   }

//   @Test
   public void testDateUnitConversion () {
         
      String queryValue = "2007";
      try {
         DateConversionInfo dateConversionInfo = new DateConversionInfo();
         dateConversionInfo.setInput(queryValue);
         dateConversionInfo.setSourceUnitType(DateUnitType.YEAR);
         dateConversionInfo.setDestinationUnitType(DateUnitType.MONTH);
         DateConversion dc = getDateUnitCoversion().convert(dateConversionInfo);
         printDates(dc);
         dateConversionInfo.setDestinationUnitType(DateUnitType.QUARTER);
         dc = getDateUnitCoversion().convert(dateConversionInfo);
         printDates(dc);
      } catch (ParseException e) {
         Assert.fail("Unable to convert date" + e.getMessage());
      }
   }

   @Test
   public void testDataFormatConversion () {
         
      String queryValue = "2007";
      try {
         //Convert YEAR to Month
         DateConversionInfo dateConversionInfo = new DateConversionInfo();
         dateConversionInfo.setInput(queryValue);
         dateConversionInfo.setSourceUnitType(DateUnitType.YEAR);
         dateConversionInfo.setDestinationUnitType(DateUnitType.MONTH);
         DateConversion dc = getDateUnitCoversion().convert(dateConversionInfo);
         
         Conversion targetConversion = new Conversion();
         targetConversion.setUnit(dc.getDateUnitType().getValue());
         targetConversion.setFormat("yyyyMM");
         applyFormat(dc, targetConversion);
         
         //Convert YEAR to Quarter
         dateConversionInfo.setDestinationUnitType(DateUnitType.QUARTER);
         dc = getDateUnitCoversion().convert(dateConversionInfo);
         targetConversion.setUnit(dc.getDateUnitType().getValue());
         targetConversion.setFormat("yyyyQ");
         applyFormat(dc, targetConversion);
         
      } catch (ParseException e) {
         Assert.fail("Unable to convert date" + e.getMessage());
      } catch (SWIException e) {
         Assert.fail("Unable to format date" + e.getMessage());
      }
   }
   
   private void applyFormat (DateConversion dc, Conversion targetConversion) throws SWIException {
      List<Date> convertedDates = dc.getConvertedDates();
      for (Date convertedDate : convertedDates) {
         System.out.println("Formatted Value: " + getDateTypeConvertor().convert(targetConversion, convertedDate));
      }
   }

   /**
    * @param dc
    */
   private void printDates (DateConversion dc) {
      List<Date> dts = dc.getConvertedDates();
      for (Date date : dts) {
         System.out.println(date.toString());
      }
   }
}
