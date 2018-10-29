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

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.util.UtilCommonBaseTest;

public class TestDynamicDateFormulaEvaluationServiceImpl extends UtilCommonBaseTest {

   @BeforeClass
   public static void setup () {
      baseSetup();
   }

   @AfterClass
   public static void baseTestTearDown () {
      baseTestTearDown();
   }

   @Test
   public void testEvaluateDateFormulaForQuarters () {
      String currentValue = "";
      String operator = "";
      String inputValue = "";
      String output = "";

      currentValue = "20072";
      operator = "-";
      inputValue = "7";
      output = getDynamicDateFormulaEvaluationService().evaluateDateFormula(
               "(yyyyQ, QUARTER, " + currentValue + ", " + operator + ", ?)", inputValue);
      Assert.assertEquals("20053", output);

      currentValue = "20072";
      operator = "-";
      inputValue = "3";
      output = getDynamicDateFormulaEvaluationService().evaluateDateFormula(
               "(yyyyQ, QUARTER, " + currentValue + ", " + operator + ", ?)", inputValue);
      Assert.assertEquals("20063", output);

      currentValue = "20072";
      operator = "-";
      inputValue = "1";
      output = getDynamicDateFormulaEvaluationService().evaluateDateFormula(
               "(yyyyQ, QUARTER, " + currentValue + ", " + operator + ", ?)", inputValue);
      Assert.assertEquals("20071", output);

      currentValue = "20072";
      operator = "+";
      inputValue = "1";
      output = getDynamicDateFormulaEvaluationService().evaluateDateFormula(
               "(yyyyQ, QUARTER, " + currentValue + ", " + operator + ", ?)", inputValue);
      Assert.assertEquals("20073", output);

      currentValue = "20072";
      operator = "+";
      inputValue = "3";
      output = getDynamicDateFormulaEvaluationService().evaluateDateFormula(
               "(yyyyQ, QUARTER, " + currentValue + ", " + operator + ", ?)", inputValue);
      Assert.assertEquals("20081", output);

      currentValue = "20072";
      operator = "+";
      inputValue = "7";
      output = getDynamicDateFormulaEvaluationService().evaluateDateFormula(
               "(yyyyQ, QUARTER, " + currentValue + ", " + operator + ", ?)", inputValue);
      Assert.assertEquals("20091", output);
   }

}
