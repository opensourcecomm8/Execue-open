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
import com.execue.core.common.bean.ontology.OntologyAbsorptionContext;
import com.execue.core.exception.ontology.OntologyException;
import com.execue.core.exception.swi.KDXException;

/**
 * @author Aditya
 */
public class FileOntologyAbsorptionTest extends ExeCueBaseTest {

   @Before
   public void setup () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTeardown();
   }

   @Test
   public void testabsorbOntology () {
      try {
         Long modelId = 101L;
         OntologyAbsorptionContext fileOntologyDataAbsorptionContext = new OntologyAbsorptionContext();
         fileOntologyDataAbsorptionContext.setModelId(modelId);
         fileOntologyDataAbsorptionContext
                  .setCloudId(getKDXCloudRetrievalService().getDefaultAppCloud(modelId).getId());
         fileOntologyDataAbsorptionContext.setFilePath("/apps/Ontologies/test.owl");

         getFileOntologyDataAbsorptionService().absorbOntology(fileOntologyDataAbsorptionContext);
      } catch (OntologyException e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      }
   }

   // @Test
   public void testScheduleFileOntologyDataAbsorbtion () {
      try {
         Long modelId = 101L;
         OntologyAbsorptionContext fileOntologyDataAbsorptionContext = new OntologyAbsorptionContext();
         fileOntologyDataAbsorptionContext.setModelId(modelId);
         fileOntologyDataAbsorptionContext
                  .setCloudId(getKDXCloudRetrievalService().getDefaultAppCloud(modelId).getId());
         fileOntologyDataAbsorptionContext.setFilePath("/apps/Ontologies/Ratings.owl");
         getFileOntologyDataAbsorptionJobService()
                  .scheduleFileOntologyDataAbsorbtion(fileOntologyDataAbsorptionContext);
      } catch (OntologyException e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      }
   }

}
