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

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.swi.ApplicationScopeIndexDetail;
import com.execue.swi.SWIBaseTest;
import com.execue.swi.exception.SWIException;

public class EASIndexServiceTest extends SWIBaseTest {

   Logger log = Logger.getLogger(EASIndexServiceTest.class);

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @Test
   public void testGetApplicationScopeIndexDetailByAppId () {
      try {
         Long appId = 101L;
         System.out.println("inside testGetApplicationScopeIndexDetailByAppId");
         ApplicationScopeIndexDetail applicationScopeIndexDetail = getEASIndexService()
                  .getApplicationScopeIndexDetailByAppId(appId);
         System.out.println(applicationScopeIndexDetail);

      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

}
