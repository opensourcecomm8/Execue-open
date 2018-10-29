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


package com.execue.handlers;

import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.exception.HandlerException;

public class PreferencesServiceHandlerTest extends ExeCueBaseTest {

   @BeforeClass
   public static void setUp () {
      baseTestSetup();
   }

   @Test
   public void testBusinessTerm () {
      Long modelId = 100L;
      try {
         getPreferencesServiceHandler().getAllBusinessTermEntities(modelId);
      } catch (HandlerException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   public static void tearDown () {
      baseTestTeardown();
   }
}
