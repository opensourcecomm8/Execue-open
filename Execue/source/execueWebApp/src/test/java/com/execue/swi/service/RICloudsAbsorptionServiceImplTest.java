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

import static junit.framework.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.exception.swi.KDXException;

public class RICloudsAbsorptionServiceImplTest extends ExeCueBaseTest {

   private static final Logger log = Logger.getLogger(RICloudsAbsorptionServiceImplTest.class);

   @BeforeClass
   public static void setUp () {
      baseTestSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTestTeardown();
   }

   // @Test
   public void testRegenerateRICloudFromCloud () {
      try {
         Model model = getKDXRetrievalService().getModelByName("Base Model");
         Cloud cloud = getKDXCloudRetrievalService().getCloudByName("MonthTimeFrame");
         getRICloudsAbsorptionService().regenerateRICloudFromCloud(cloud, model.getId());
      } catch (KDXException kdxException) {
         log.error(kdxException, kdxException);
         fail(kdxException.getMessage());
      }
   }
}
