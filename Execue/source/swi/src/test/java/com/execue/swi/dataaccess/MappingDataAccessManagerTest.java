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


package com.execue.swi.dataaccess;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.entity.Concept;
import com.execue.swi.SWICommonBaseTest;
import com.execue.swi.exception.MappingException;

public class MappingDataAccessManagerTest extends SWICommonBaseTest {

   @BeforeClass
   public static void setup () {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTearDown();
   }

   @Test
   public void testGetMappedDistributionConcepts () {
      Long assetId = 11L;
      try {
         List<Concept> concepts = getMappingDataAccessManager().getMappedDistributionConceptsByEntityBehavior(assetId);
         for (Concept concept : concepts) {
            System.out.println("Concept id : " + concept.getId());
         }
      } catch (MappingException me) {
         me.printStackTrace();
      }
   }
}
