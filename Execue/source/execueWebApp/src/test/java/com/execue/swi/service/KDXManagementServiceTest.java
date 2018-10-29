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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.SecondaryWord;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.exception.swi.KDXException;
import com.execue.swi.configuration.SWIConfigurationConstants;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.util.ExecueConstants;

public class KDXManagementServiceTest extends ExeCueBaseTest {

   Logger logger = Logger.getLogger(KDXManagementServiceTest.class);

   @Before
   public void setUp () throws Exception {
      baseTestSetup();
   }

   @After
   public void tearDown () throws Exception {
      baseTestTeardown();
   }

   // @Test
   public void testCreateModel () {
      try {
         Long applicationId = 100L;
         Application application = getKDXRetrievalService().getApplicationById(applicationId);
         Model model = new Model();
         model.setName("testModel");
         model.setCreatedDate(new Date());
         getKDXManagementService().createModel(model, application);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testCreateModelGroup () {
      ModelGroup modelGroup = new ModelGroup();
      modelGroup.setName("testModelGroup");
      try {
         getKDXManagementService().createModelGroup(modelGroup);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testCreateConcept () {
      Concept concept = new Concept();
      concept.setName("TestConcept");
      concept.setDisplayName("Test Concept");
      concept.setDescription("Test Concept");
      try {
         getKDXManagementService().createConcept(112L, concept);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testCreateConceptWithAdditionalBehavior () {
      Concept concept = new Concept();
      concept.setName("TestConcept");
      concept.setDisplayName("Test Concept");
      concept.setDescription("Test Concept");
      try {
         BusinessEntityDefinition conceptBed = getKDXManagementService().createConcept(101L, concept);

         // Add the additional behavior as population
         BehaviorType behaviorType = BehaviorType.POPULATION;
         List<BehaviorType> behaviorTypes = new ArrayList<BehaviorType>();
         behaviorTypes.add(behaviorType);
         getConceptTypeAssociationService().createEntityBehaviors(conceptBed, behaviorTypes);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testCreateConceptWithType () {
      Concept concept = new Concept();
      concept.setName("TestConceptWithTypes");
      concept.setDisplayName("Test Concept With Types");
      concept.setDescription("Test Concept With Types");
      try {
         Type type = getKDXRetrievalService().getTypeByName(ExecueConstants.MEASURABLE_ENTITY_TYPE);
         getKDXManagementService().createConcept(101L, type, concept);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testAssignConceptType () {
      try {
         Long measurableEntityBedId = 10001L; // Eg. Net sales
         Long timeFrameEntityBedId = 10002L; // Eg. NominalQuarter

         BusinessEntityDefinition measurableEntityBed = getKDXRetrievalService().getBusinessEntityDefinitionById(
                  measurableEntityBedId);
         BusinessEntityDefinition timeFrameBed = getKDXRetrievalService().getBusinessEntityDefinitionById(
                  timeFrameEntityBedId);

         BusinessEntityDefinition measurableEntityType = getKDXRetrievalService().getTypeBedByName(
                  ExecueConstants.MEASURABLE_ENTITY_TYPE);
         BusinessEntityDefinition timeFrameType = getKDXRetrievalService().getTypeBedByName(
                  ExecueConstants.TIME_FRAME_TYPE);

         Long modelId = 101L;
         Cloud cloud = getKDXCloudRetrievalService().getDefaultAppCloud(modelId);

         getConceptTypeAssociationService().assignConceptType(
                  populateTypeConceptInfo(timeFrameBed, timeFrameType, cloud, null, null, false, false));

         getConceptTypeAssociationService().assignConceptType(
                  populateTypeConceptInfo(measurableEntityBed, measurableEntityType, cloud, null, null, false, false));
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      }
   }

   // @Test
   public void testCreateInstance () {
      Instance instance = new Instance();
      instance.setName("testInstance");
      instance.setDisplayName("testInstanceName");
      try {
         getKDXManagementService().createInstance(102L, 3208L, instance);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testCreateRelation () {
      Relation relation = new Relation();
      relation.setName("testRelation");
      relation.setDisplayName("testRelation");
      try {
         getKDXManagementService().createRelation(102L, relation);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testUpdateConcept () {
      Long conceptId = 3208L;
      try {
         Concept concept = getKDXRetrievalService().getConceptById(conceptId);
         concept.setDisplayName("UpdatedConcept");
         getKDXManagementService().saveOrUpdateConcept(102L, concept);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testCreateType () {
      Type type = new Type();
      Long modelId = 1L;
      type.setName("NewType");
      type.setDisplayName("New Type");

      // Create the type
      try {
         getKDXManagementService().createType(modelId, type);
      } catch (KDXException e) {
         if (e.getCode() != SWIExceptionCodes.ENTITY_ALREADY_EXISTS) {
            Assert.fail(e.getMessage());
         } else {
            if (logger.isInfoEnabled()) {
               logger.info("Type Name: " + type.getName() + " Already Exists in model");
            }
         }
      }

      // Create the realized type
      Type realizedType = new Type();
      realizedType.setName("testRealizedType_1_New");
      realizedType.setDisplayName(realizedType.getName());

      try {
         getKDXManagementService().createType(modelId, realizedType, true);
      } catch (KDXException e) {
         if (e.getCode() != SWIExceptionCodes.ENTITY_ALREADY_EXISTS) {
            Assert.fail(e.getMessage());
         } else {
            if (logger.isInfoEnabled()) {
               logger.info("Realized Type Name: " + realizedType.getName() + " Already Exists in model");
            }
         }
      }
   }

   @Test
   public void testDeleteSecondaryWordsForModel () {
      Long modelId = 101L;
      try {
         List<SecondaryWord> secondaryWords = getKDXRetrievalService().getAllSecondaryWordsForModel(modelId);
         getKDXManagementService().deleteSecondaryWords(secondaryWords);
      } catch (KDXException e) {
         Assert.fail(e.getMessage());
      }
   }

   @Test
   public void testCreateSecondaryWords () {
      // TODO: NK: currently getting the threshold count from configuration, later should calculate based on some
      // percentage
      Long threshold = Long.parseLong(getSWIConfiguration().getConfiguration().getProperty(
               SWIConfigurationConstants.DEFAULT_THRESHOLD_FOR_SECONDARY_WORDS));
      Long modelId = 101L;
      try {
         ModelGroup modelGroup = getKDXRetrievalService().getPrimaryGroup(modelId);
         Set<String> secondaryWordsStr = getKDXRetrievalService().getEligibleSecondaryWordsForModel(modelId, threshold);
         if (logger.isDebugEnabled()) {
            logger.debug("\nEligible Secondary Words: " + secondaryWordsStr);
         }
         List<SecondaryWord> secondaryWords = new ArrayList<SecondaryWord>(1);

         for (String word : secondaryWordsStr) {
            SecondaryWord sw = populateSecondaryWord(modelGroup, word);
            secondaryWords.add(sw);
         }
         getKDXManagementService().createSecondaryWords(secondaryWords);
      } catch (KDXException e) {
         Assert.fail(e.getMessage());
      }
   }

   private SecondaryWord populateSecondaryWord (ModelGroup modelGroup, String word) {
      SecondaryWord sw = new SecondaryWord();
      sw.setWord(word);
      sw.setDefaultWeight(5.0d);
      sw.setFrequency(1L);
      sw.setModelGroup(modelGroup);
      return sw;
   }

}
