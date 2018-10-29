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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Model;
import com.execue.swi.SWIBaseTest;
import com.execue.swi.exception.KDXException;

public class KDXDataAccessManagerTest extends SWIBaseTest {

   private static Random       random;
   private final static String SAMPLE_MODEL_NAME     = "Sample Model";
   private final static String SAMPLE_CONCEPT_NAME1  = "Concept1";
   private final static String SAMPLE_CONCEPT_NAME2  = "Concept2";
   private final static String SAMPLE_INSTANCE_NAME1 = "Instance1";
   private final static String SAMPLE_INSTANCE_NAME2 = "Instance2";
   private final static String UPDATE_DOMAIN_NAME    = "Updated Domain";
   private final static String UPDATE_CONCEPT_NAME1  = "Updated Concept 1";
   private final static String UPDATE_CONCEPT_NAME2  = "Updated Concept 2";
   private final static String UPDATE_INSTANCE_NAME1 = "Updated Instance 1";
   private final static String UPDATE_INSTANCE_NAME2 = "Updated Instance 2";
   private final static int    CONCEPT_INDEX         = 0;
   private final static int    CONCEPT_INDEX1        = 1;
   private final static int    INSTANCE_INDEX        = 0;
   private final static int    INSTANCE_INDEX1       = 1;

   @BeforeClass
   public static void setup () {
      baseSetup();
      random = new Random();
   }

   @AfterClass
   public static void tearDown () {
      baseTearDown();
   }

   // @Test
   // public void testProcessor () {
   // Model model = createModel();
   // List<Concept> conceptList = createConcepts();
   // List<Instance> instanceList = createInstances();
   // testCreateDomain(model);
   // testCreateInstance(model, conceptList.get(CONCEPT_INDEX), instanceList);
   // // testUpdateModel(model);
   // testUpdateConcept(model, conceptList);
   // // testUpdateInstance(domain, conceptList.get(CONCEPT_INDEX), instanceList);
   // // testDeleteInstances(domain, conceptList.get(CONCEPT_INDEX), instanceList);
   // // testDeleteConcepts(domain, conceptList);
   // }

   private Model createModel () {
      Model model = getSampleModel(SAMPLE_MODEL_NAME + random.nextInt());
      return model;
   }

   private List<Concept> createConcepts () {
      List<Concept> conceptList = new ArrayList<Concept>();
      Concept concept1 = getSampleConcept(SAMPLE_CONCEPT_NAME1);
      Concept concept2 = getSampleConcept(SAMPLE_CONCEPT_NAME2);
      conceptList.add(concept1);
      conceptList.add(concept2);
      return conceptList;
   }

   private List<Instance> createInstances () {
      List<Instance> instanceList = new ArrayList<Instance>();
      Instance instance1 = getSampleInstance(SAMPLE_INSTANCE_NAME1);
      Instance instance2 = getSampleInstance(SAMPLE_INSTANCE_NAME2);
      instanceList.add(instance1);
      instanceList.add(instance2);
      return instanceList;
   }

   private void testCreateDomain (Model model) {
      boolean creationSucceeded = true;
      try {
         getKDXDataAccessManager().createModel(model);
      } catch (KDXException e) {
         creationSucceeded = false;
      }
      Assert.assertTrue("Domain creation failed", creationSucceeded);
   }

   // private void testCreateInstance (Model model, Concept concept, List<Instance> instances) {
   // List<Instance> retrievedInstances = null;
   // try {
   // for (Instance instance : retrievedInstances) {
   // getKDXDataAccessManager().createInstance(model.getId(), concept.getId(), instance);
   //
   // }
   //
   // retrievedInstances = getKDXDataAccessManager().getInstances(model.getId(), concept.getId());
   // } catch (KDXException e) {
   // e.printStackTrace();
   // }
   //
   // Assert.assertTrue("Creation of Instances failed", retrievedInstances.size() == instances.size());
   //
   // if (retrievedInstances.size() > 0) {
   // Assert.assertTrue("Retrieval failed for instance", retrievedInstances.get(INSTANCE_INDEX).getName().equals(
   // SAMPLE_INSTANCE_NAME1)
   // || retrievedInstances.get(INSTANCE_INDEX).getName().equals(SAMPLE_INSTANCE_NAME2));
   // }
   //
   // }

   // private void testUpdateInstance (Domain domain, Concept concept, List<Instance> instances) {
   //
   // List<Instance> updatedInstances = null;
   // List<Concept> conceptList = new ArrayList<Concept>();
   // conceptList.add(concept);
   // instances.get(INSTANCE_INDEX).setName(UPDATE_INSTANCE_NAME1);
   // instances.get(INSTANCE_INDEX1).setName(UPDATE_INSTANCE_NAME2);
   // try {
   // getKDXDataAccessManager().updateInstances(domain, concept, instances);
   // // updatedInstances = getDomainEntityDefinitionDAO().getInstances(conceptList);
   // } catch (KDXException e) {
   // e.printStackTrace();
   //
   // } catch (DataAccessException dae) {
   // dae.printStackTrace();
   // }
   //
   // Assert.assertTrue("There should be two updates instances", updatedInstances.size() == instances.size());
   //
   // Assert.assertTrue("Updation failed for instances", updatedInstances.get(INSTANCE_INDEX).getName()
   // .equalsIgnoreCase("Updated Instance 1")
   // || updatedInstances.get(INSTANCE_INDEX).getName().equalsIgnoreCase("Updated Instance 2"));
   //
   // }

   // private void testDeleteConcepts (Domain domain, List<Concept> conceptList) {
   // Concept deletedConcept = null;
   // Instance deletedInstance = null;
   // try {
   // getKDXDataAccessManager().deleteConcepts(domain, conceptList);
   //
   // List<Instance> deletedInstances = getDomainEntityDefinitionDAO().getInstances(conceptList);
   //
   // for (Instance instance : deletedInstances) {
   //
   // deletedInstance = getDomainEntityDefinitionDAO().getById(instance.getId(), Instance.class);
   // Assert.assertTrue("Deletion failed for instances", deletedInstance == null);
   // }
   //
   // for (Concept concept : conceptList) {
   // deletedConcept = getDomainEntityDefinitionDAO().getById(concept.getId(), Concept.class);
   // Assert.assertTrue("Deletion failed for concept", deletedConcept == null);
   // }
   // } catch (KDXException e) {
   // e.printStackTrace();
   // } catch (DataAccessException dae) {
   // dae.printStackTrace();
   // }
   //
   // }

   // private void testDeleteInstances (Domain domain, Concept concept, List<Instance> instanceList) {
   // Instance deletedInstance = null;
   // try {
   // getKDXDataAccessManager().deleteInstances(domain, concept, instanceList);
   // for (Instance instance : instanceList) {
   // deletedInstance = getDomainEntityDefinitionDAO().getById(instance.getId(), Instance.class);
   // Assert.assertTrue("Instance deletion failed", deletedInstance == null);
   // }
   //
   // } catch (KDXException e) {
   // e.printStackTrace();
   //
   // } catch (DataAccessException dae) {
   // dae.printStackTrace();
   // }
   //
   // }
}
