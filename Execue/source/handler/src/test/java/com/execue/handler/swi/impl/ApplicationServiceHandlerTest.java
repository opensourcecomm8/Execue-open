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


package com.execue.handler.swi.impl;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.exception.HandlerException;
import com.execue.handler.HandlerCommonTest;

public class ApplicationServiceHandlerTest extends HandlerCommonTest {

   private static final Logger logger = Logger.getLogger(ApplicationServiceHandlerTest.class);

   @Before
   public void setup () {
      baseSetup();

   }

   @After
   public void tearDown () {
      baseTeardown();
   }

   @Test
   public void testGetApplicationById () {
      Long applicationId = 1636L;
      try {
         Application application = getApplicationServiceHandler().getApplicationById(applicationId);
         logger.info(application.getName());
      } catch (HandlerException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

}
