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


package com.execue.publisher.controller;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.publisher.PublisherDataAbsorptionContext;
import com.execue.core.common.type.CSVEmptyField;
import com.execue.core.common.type.CSVStringEnclosedCharacterType;
import com.execue.core.common.type.JobType;
import com.execue.publisher.PublisherBaseTest;
import com.execue.publisher.exception.PublisherException;

/**
 * This test case is for testing uploading of csv file into datasource location
 * 
 * @author Vishay
 * @since 20/10/09
 * @version 1.0
 */
public class PublisherDataControllerServiceTest extends PublisherBaseTest {

   @BeforeClass
   public static void setUp () {
      baseTestSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTestTearDown();
   }

   @Test
   public void testPublishData () throws PublisherException {
      PublisherDataAbsorptionContext publisherDataAbsorptionContext = new PublisherDataAbsorptionContext();
      publisherDataAbsorptionContext
               .setFilePath("C:\\Users\\execue\\Desktop\\sample-csv\\sample-csv-billing-fx-less.csv");
      publisherDataAbsorptionContext.setApplicationId(new Long(10));
      publisherDataAbsorptionContext.setColumnsAvailable(true);
      publisherDataAbsorptionContext.setDataDelimeter(",");
      publisherDataAbsorptionContext.setCsvEmptyField(CSVEmptyField.EMPTY);
      publisherDataAbsorptionContext.setCsvStringEnclosedCharacterType(CSVStringEnclosedCharacterType.NONE);
      publisherDataAbsorptionContext.setModelId(1L);
      publisherDataAbsorptionContext.setUserId(new Long(1));
      JobRequest jobRequest = new JobRequest();
      jobRequest.setId(1L);
      jobRequest.setUserId(1L);
      jobRequest.setJobType(JobType.PUBLISHER_DATA_ABSORPTION);
      publisherDataAbsorptionContext.setJobRequest(jobRequest);
      // getPublisherDataControllerService().publishData(publisherDataAbsorptionContext);
   }

   // @Test
   // public void testAbsorbRIOntoTerms () {
   // try {
   // List<Long> DEDIds = new ArrayList<Long>();
   // DEDIds.add(new Long(10001)); // Record Type concept ded id
   // DEDIds.add(new Long(10023)); // Record Type 1 instance ded id
   // getDomainOntologyAbsorptionService().absorbRIOntoTerms(DEDIds, 1L);
   // } catch (OntologyException e) {
   // e.printStackTrace();
   // Assert.fail("Failed to absorb the domain ontology into the database: " + e.getMessage());
   // }
   // }

   // @Test
   // public void testAbsorbSnowFlakes () {
   // try {
   // List<Long> DEDIds = new ArrayList<Long>();
   // DEDIds.add(new Long(10001)); // Record Type concept ded id
   // DEDIds.add(new Long(10023)); // Record Type 1 instance ded id
   // getDomainOntologyAbsorptionService().absorbSnowFlakes(DEDIds);
   // } catch (OntologyException e) {
   // e.printStackTrace();
   // Assert.fail("Failed to absorb the snow flakes into the database: " + e.getMessage());
   // }
   // }
}
