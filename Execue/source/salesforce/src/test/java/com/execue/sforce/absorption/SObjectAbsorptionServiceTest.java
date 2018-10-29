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


package com.execue.sforce.absorption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.exception.ExeCueException;
import com.execue.sforce.SforceBaseTest;

/**
 * This class represents the test case of absorption service
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public class SObjectAbsorptionServiceTest extends SforceBaseTest {

   @Before
   public void setUp () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTearDown();
   }

   @Test
   public void testSObjectAbsorption () {
      try {
         getSObjectAbsorptionService().absorbSObjectData(null, null);
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
   }
}
