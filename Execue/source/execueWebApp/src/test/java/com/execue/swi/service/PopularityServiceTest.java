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

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.swi.PopularityHitMaintenanceContext;
import com.execue.core.common.type.TermType;
import com.execue.core.exception.swi.SWIException;

/**
 * @author John Mallavalli
 */
public class PopularityServiceTest extends ExeCueBaseTest {

   @Before
   public void setup () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTeardown();
   }

   // @Test
   public void updatePopularityTest () {
      Long termId = 3L;
      TermType type = TermType.ASSET_ENTITY;
      Long hits = 2L;
      try {
         getPopularityService().updatePopularity(termId, type, hits);
      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   @Test
   public void updateTermPopularityTest () {
      try {
         PopularityHitMaintenanceContext popularityHitMaintenanceContext = new PopularityHitMaintenanceContext();
         popularityHitMaintenanceContext.setUserId(1L);
         getPopularityService().updateTermsBasedOnPopularity(popularityHitMaintenanceContext);
      } catch (SWIException e) {
         e.printStackTrace();
         Assert.fail("Failed to update the terms in the popularity hits table: " + e.getMessage());
      }
   }
}
