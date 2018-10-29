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
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.SnowFlakesAbsorptionContext;
import com.execue.core.common.bean.ontology.OntologyAbsorptionContext;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ontology.OntologyException;

/**
 * @author Nitesh
 */
public class SnowFlakesTermsAbsorptionTest extends ExeCueBaseTest {

   @Before
   public void setup () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTeardown();
   }

   @Test
   public void testAbsorbSnowFlakesTerms () {
      try {
         long modelId = 110L;
         OntologyAbsorptionContext ontologyAbsorptionContext = new OntologyAbsorptionContext();
         ontologyAbsorptionContext.setModelId(modelId);
         getSnowFlakesTermsAbsorptionService().absorbSnowFlakeTerms(ontologyAbsorptionContext);
      } catch (OntologyException e) {
         e.printStackTrace();
      }
   }

//   @Test
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