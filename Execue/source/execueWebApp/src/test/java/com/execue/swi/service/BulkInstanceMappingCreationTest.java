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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;

public class BulkInstanceMappingCreationTest extends ExeCueBaseTest {

   @BeforeClass
   public static void setUp () {
      baseTestSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTestTeardown();
   }

   @Test
   public void testCreateInstanceMappings () {
      long modelId = 110;
      long conceptId = 1028; //officer concept id
      long assetId = 1001; //cruncbase asset id
      long tablId = 1027; //officer tabl id
      long columId = 1136; //officer_id column
      int batchSize = 2000;
      long totalInsert = 0;
      long currentMemberId = 83216L; 
      while(totalInsert < 36000) {
         getBulkInstanceMappingCreation().createInstanceMappings(modelId, conceptId, assetId, tablId, columId, currentMemberId, batchSize);
         currentMemberId += batchSize; //currentMemberId         
         totalInsert += batchSize;
      }
   }
}
