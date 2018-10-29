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


package com.execue.reporting.presentation.test.tranform;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.reporting.presentation.service.IPresentationTransformService;
import com.execue.reporting.presentation.service.PresentationTransformServiceFactory;
import com.execue.reporting.presentation.service.impl.transform.GridPresentationTransformService;
import com.execue.reporting.presentation.service.impl.transform.PivotPresentationTransformService;

public class PresentationTranformTest extends com.execue.reporting.presentation.test.PresentationBaseTest {

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   @Test
   public void testFactoryFromBean () {
      IPresentationTransformService transformService = getPresentationTransformServiceFactory().getTransformService(
               PivotPresentationTransformService.name);
      transformService.getHeader(1, 1, 1);
      transformService = getPresentationTransformServiceFactory().getTransformService(
               GridPresentationTransformService.name);
      transformService.getHeader(1, 1, 1);
   }

   @Test
   public void testFactoryFromFactory () {
      IPresentationTransformService transformService = PresentationTransformServiceFactory.getInstance()
               .getTransformService(PivotPresentationTransformService.name);
      transformService.getHeader(1, 1, 1);
      transformService = PresentationTransformServiceFactory.getInstance().getTransformService(
               GridPresentationTransformService.name);
      transformService.getHeader(1, 1, 1);
   }
}