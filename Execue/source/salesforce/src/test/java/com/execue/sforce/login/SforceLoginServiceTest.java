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


package com.execue.sforce.login;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.sforce.SforceBaseTest;
import com.execue.sforce.bean.SforceLoginContext;
import com.execue.sforce.exception.SforceException;
/**
 * This interface contains the absorption services to target data source
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public class SforceLoginServiceTest extends SforceBaseTest {

   @Before
   public void setUp () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTearDown();
   }

   @Test
   public void testSforceLogin () {
      try {
         SforceLoginContext loginContext = getSforceLoginService().loginToSforce();
         System.out.println(loginContext.getPartnerSessionId());
         System.out.println(loginContext.getPartnerSessionURL());
      } catch (SforceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
