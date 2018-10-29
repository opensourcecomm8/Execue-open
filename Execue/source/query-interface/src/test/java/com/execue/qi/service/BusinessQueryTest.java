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


package com.execue.qi.service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.QueryForm;
import com.execue.core.exception.HandlerRequestTransformException;
import com.execue.qi.QueryInterfaceCommonBaseTest;
import com.execue.qi.exception.QIException;

public class BusinessQueryTest extends QueryInterfaceCommonBaseTest {

   @BeforeClass
   public static void setup () {
      baseSetup();
   }

   @Test
   public void generateBusinessQuery () {

      try {
         QueryForm queryForm = generatQueryFormFromXML("<qf><modelId>101</modelId><applicationId>101</applicationId><selectText/><selectTextProcessed/><selects><select><term><name>NetIncome</name><displayName>Net Income</displayName><type>CONCEPT</type></term></select></selects><conditions><condition><lhsBusinessTerm><term><name>NetBasicEPS</name><displayName>Net Basic EPS</displayName><type>CONCEPT</type></term><hasInstances>false</hasInstances><datatype>CURRENCY</datatype></lhsBusinessTerm><qiConversion><name>DOLLAR</name><displayName>Dollar</displayName><conversionId>21</conversionId></qiConversion><operator>GREATER_THAN</operator><rhsValue><values><qiValue><value>1000</value><qiConversion><name>MILLION</name><displayName>Million</displayName><conversionId>62</conversionId></qiConversion></qiValue></values></rhsValue></condition></conditions><populations/><summarizations/><cohort><summarizations/><conditions/></cohort> </qf>");
         BusinessQuery generateBusinessQuery = getQueryInterfaceService().generateBusinessQuery(queryForm);

      } catch (HandlerRequestTransformException e) {
         e.printStackTrace();
      } catch (QIException e) {
         e.printStackTrace();
      }

   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

}
