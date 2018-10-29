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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.RIOntoTermsAbsorptionContext;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.ontology.RIOntoTermsCreationContext;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ontology.OntologyException;
import com.execue.core.exception.swi.KDXException;

/**
 * @author Aditya
 */
public class RIOntoTermsAbsorptionTest extends ExeCueBaseTest {

   private static final Logger logger = Logger.getLogger(RIOntoTermsAbsorptionTest.class);

   @Before
   public void setup () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTeardown();
   }

   // @Test
   public void testAbsorbAllRIOntoTerms () {
      RIOntoTermsAbsorptionContext riOntoTermsAbsorptionContext = new RIOntoTermsAbsorptionContext();
      riOntoTermsAbsorptionContext.setModelId(110L);
      try {
         getRiOntoTermsAbsorptionService().absorbOntoTerms(riOntoTermsAbsorptionContext);
      } catch (OntologyException e) {
         e.printStackTrace();
      }
   }

  

   // @Test
   public void tesScheduleRIOntoTermsAbsorption () throws KDXException {
      RIOntoTermsAbsorptionContext riOntoTermsAbsorptionContext = new RIOntoTermsAbsorptionContext();
      riOntoTermsAbsorptionContext.setModelId(1L);
      try {
         getRiOntoTermsAbsorptionJobService().scheduleRIOntoTermsAbsorption(riOntoTermsAbsorptionContext);
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void tesScheduleRIOntoTermsCreation () throws KDXException {
      List<BusinessEntityTerm> listToBeSent = new ArrayList<BusinessEntityTerm>();
      RIOntoTermsCreationContext riOntoTermsCreationContext = new RIOntoTermsCreationContext();
      riOntoTermsCreationContext.setUserId(1L);
      // Concept Company
      Concept concept = getKDXRetrievalService().getConceptById(1001L);

      BusinessEntityDefinition businessEntityDefinition2 = getKDXRetrievalService().getConceptBEDByName(101L,
               concept.getName());

      System.out.println("concept DED:" + businessEntityDefinition2.getId());

      BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
      businessEntityTerm.setBusinessEntity(concept);
      businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
      businessEntityTerm.setBusinessEntityDefinitionId(businessEntityDefinition2.getId());
      // Concept Industry
      Concept concept1 = getKDXRetrievalService().getConceptById(1002L);

      BusinessEntityDefinition businessEntityDefinition3 = getKDXRetrievalService().getConceptBEDByName(101L,
               concept1.getName());

      BusinessEntityTerm businessEntityTerm1 = new BusinessEntityTerm();
      businessEntityTerm1.setBusinessEntity(concept1);
      businessEntityTerm1.setBusinessEntityType(BusinessEntityType.CONCEPT);
      businessEntityTerm1.setBusinessEntityDefinitionId(businessEntityDefinition3.getId());

      // Instance Sector
      // 1155, 1156
      Instance instance = getKDXRetrievalService().getInstanceById(1155L);

      BusinessEntityTerm businessEntityTermInstance = new BusinessEntityTerm();
      businessEntityTermInstance.setBusinessEntity(instance);
      businessEntityTermInstance.setBusinessEntityDefinitionId(11033L);
      businessEntityTermInstance.setBusinessEntityType(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);

      Instance instance1 = getKDXRetrievalService().getInstanceById(1156L);

      BusinessEntityTerm businessEntityTermInstance1 = new BusinessEntityTerm();
      businessEntityTermInstance1.setBusinessEntity(instance1);
      businessEntityTermInstance1.setBusinessEntityDefinitionId(11034L);
      businessEntityTermInstance1.setBusinessEntityType(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);

      listToBeSent.add(businessEntityTerm);
      listToBeSent.add(businessEntityTerm1);
      listToBeSent.add(businessEntityTermInstance);
      listToBeSent.add(businessEntityTermInstance1);
      riOntoTermsCreationContext.setBusinessEntityTerms(listToBeSent);

      try {
         getRiOntoTermsCreationJobService().scheduleRIOntoTermsJob(riOntoTermsCreationContext);
      } catch (OntologyException e) {
         e.printStackTrace();
      }
   }

  
}