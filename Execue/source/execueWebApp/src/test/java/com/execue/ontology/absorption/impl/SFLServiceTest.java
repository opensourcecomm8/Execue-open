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


/**
 * 
 */
package com.execue.ontology.absorption.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.SnowFlakesAbsorptionContext;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.swi.KDXException;

/**
 * @author Nitesh
 */
public class SFLServiceTest extends ExeCueBaseTest {

   @Before
   public void setup () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTeardown();
   }

   @Test
   public void testGenerateSFLsForAllInstancesOfConcept () {
      try {
         Long modelId = 101L; // I2App model
         Long conceptId = 2036L; // provide fiscal year, fiscal quarter concept id
         getSFLService().generateSFLsForAllInstancesOfConcept(modelId, conceptId);
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      }
   }

   // @Test
   public void testScheduleSnowFlakesTermsAbsorption () {
      SnowFlakesAbsorptionContext snowFlakesAbsorptionContext = new SnowFlakesAbsorptionContext();
      snowFlakesAbsorptionContext.setModelId(104L);
      try {
         getSnowFlakesTermsAbsorptionJobService().scheduleSnowFlakesTermsAbsorption(snowFlakesAbsorptionContext);
      } catch (ExeCueException e) {
         e.printStackTrace();
      }

   }

}