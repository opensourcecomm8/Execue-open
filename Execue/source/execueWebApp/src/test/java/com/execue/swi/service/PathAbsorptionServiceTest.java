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

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.exception.swi.SWIException;

public class PathAbsorptionServiceTest extends ExeCueBaseTest {

   Logger logger = Logger.getLogger(PathAbsorptionServiceTest.class);

   @Before
   public void setUp () throws Exception {
      baseTestSetup();
   }

   @After
   public void tearDown () throws Exception {
      baseTestTeardown();
   }

   @Test
   public void testAbsorbPathDefinitions () {
      Long modelId = 527L;
      try {
         Cloud cloud = getKDXCloudRetrievalService().getDefaultAppCloud(modelId);
         getPathAbsorptionService().absorbIndirectPaths(cloud.getId(), modelId);
      } catch (SWIException e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      }
   }

   // @Test
   public void testDeleteIndirectPathDefinitionsForCloud () {
      Long cloudId = 1004L;

      try {
         // Get and delete the existing indirect triples for cloud
         List<Long> pathDefinitionIds = getPathDefinitionService().getAllIndirectPathDefIdsForCloud(cloudId);
         if (!CollectionUtils.isEmpty(pathDefinitionIds)) {
            getPathDefinitionService().deletePathDefinitions(pathDefinitionIds);
         }
      } catch (SWIException e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      }
   }
}
