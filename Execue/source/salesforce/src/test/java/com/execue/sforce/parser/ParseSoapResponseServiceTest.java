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


package com.execue.sforce.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.sforce.SforceBaseTest;
import com.execue.sforce.bean.SforceLoginContext;
import com.execue.sforce.exception.SforceException;
import com.execue.sforce.helper.SforceUtilityHelper;

/**
 * This class represents the test case of Parsing the soap response
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public class ParseSoapResponseServiceTest extends SforceBaseTest {

   @Before
   public void setUp () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTearDown();
   }

   @Test
   public void testCheckValidSOAPResponse () {
      try {
         SforceLoginContext loginToSforce = getSforceLoginService().loginToSforce();
         String selectQuerySOAPRequestXML = SforceUtilityHelper.prepareSOAPDataRequestXML(loginToSforce
                  .getPartnerSessionId(), "select name from account",200);
         String executeSOAPRequest = getSOAPRequestResponseService().executeSOAPRequest(
                  loginToSforce.getPartnerSessionURL(), selectQuerySOAPRequestXML);
         System.out.println(executeSOAPRequest);
         String checkSOAPResponseForValidity = getParseSoapResponseService().checkSOAPResponseForValidity(
                  executeSOAPRequest);
         System.out.println("Got validation message" + checkSOAPResponseForValidity);
      } catch (SforceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
