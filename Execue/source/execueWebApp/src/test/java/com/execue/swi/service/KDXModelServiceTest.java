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
package com.execue.swi.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.exception.swi.KDXException;

/**
 * @author Nitesh
 */
public class KDXModelServiceTest extends ExeCueBaseTest {

   private Logger logger = Logger.getLogger(KDXModelServiceTest.class);

   @Before
   public void setUp () throws Exception {
      baseTestSetup();
   }

   @After
   public void tearDown () throws Exception {
      baseTestTeardown();
   }

   // @Test
   public void testHasBehavior () {
      long yearTypeBed = 202;
      try {
         // year is enumeration type
         Assert.assertTrue(getKDXModelService().hasBehavior(yearTypeBed, BehaviorType.ENUMERATION));
         // year in not abstract
         Assert.assertTrue(!getKDXModelService().hasBehavior(yearTypeBed, BehaviorType.ABSTRACT));
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail("Failed to find the behavior..." + e.getMessage());
      }
   }

   // @Test
   public void testEntityHasBehavior () {
      long nominalYearEntityId = 19739; // i.e Timeframe type
      try {
         // nominal year is quantitative type
         Assert.assertTrue(getKDXModelService().checkEntityHasBehavior(nominalYearEntityId, BehaviorType.QUANTITATIVE));
         // nominal year in not abstract
         Assert.assertTrue(!getKDXModelService().checkEntityHasBehavior(nominalYearEntityId, BehaviorType.ABSTRACT));
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail("Failed to find the behavior..." + e.getMessage());
      }
   }

   // @Test
   public void testGetAllPossibleBehavior () {
      long yearTypeBed = 202;
      try {
         List<BehaviorType> list = getKDXRetrievalService().getAllPossibleBehavior(yearTypeBed);
         if (logger.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder("Behavior Types: ");
            for (BehaviorType behaviorType : list) {
               sb.append(behaviorType + "\t");
            }
            logger.info(sb);
         }
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail("Failed to get all the possible behavior..." + e.getMessage());
      }
   }

   // @Test
   public void testGetAllBehaviorForEntity () {
      long entityBedId = 10001;
      try {
         List<BehaviorType> list = getKDXRetrievalService().getAllBehaviorForEntity(entityBedId);
         if (logger.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder("Behavior Types: ");
            for (BehaviorType behaviorType : list) {
               sb.append(behaviorType + "\t");
            }
            logger.info(sb);
         }
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail("Failed to get all the possible behavior..." + e.getMessage());
      }
   }

   // @Test
   public void testGetAllChildToParentTriplesForModel () {
      long modelId = 101; // I2App Model
      try {
         Map<Long, Map<Long, String>> allChildToParentTriplesForModel = getKDXModelService()
                  .getAllChildToParentTriplesForModel(modelId);
         if (logger.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder("Triples Info: ");
            for (Entry<Long, Map<Long, String>> entry : allChildToParentTriplesForModel.entrySet()) {
               sb.append("\nCHILD ETD ID: " + entry.getKey() + "\tParent Triples: " + entry.getValue().values());
            }
            logger.info(sb);
         }
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail("Failed to get all the child to parent triples..." + e.getMessage());
      }
   }

   // @Test
   public void testGetAllParentToChildTriplesForModel () {
      long modelId = 101; // I2App Model
      try {
         Map<Long, Map<Long, String>> allParentToChildTriplesForModel = getKDXModelService()
                  .getAllParentToChildTriplesForModel(modelId);
         if (logger.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder("Triples Info: ");
            for (Entry<Long, Map<Long, String>> entry : allParentToChildTriplesForModel.entrySet()) {
               sb.append("\nPARENT ETD ID: " + entry.getKey() + "\tChild Triples: " + entry.getValue().values());
            }
            logger.info(sb);
         }
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail("Failed to get all the parent to child triples..." + e.getMessage());
      }
   }

   @Test
   public void testGetAllParentBeds () {
      Long destBEDId = 10001L; // parent of company
      Long modelId = 101L;
      try {
         List<Long> allParentBedIds = getKDXModelService().getAllParentBedIds(destBEDId, modelId);
         if (logger.isInfoEnabled()) {
            logger.info("\nPARENT BED IDs: " + allParentBedIds);
         }
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail("Failed to get all the child bed ids ..." + e.getMessage());
      }
   }

   // @Test
   public void testGetAllChildBeds () {
      Long sourceBEDId = 10002L;
      Long modelId = 101L;
      try {
         List<Long> allChildBedIds = getKDXModelService().getAllChildBedIds(sourceBEDId, modelId);
         if (logger.isInfoEnabled()) {
            logger.info("\nCHILD BED IDs: " + allChildBedIds);
         }
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail("Failed to get all the child bed ids ..." + e.getMessage());
      }
   }
}
