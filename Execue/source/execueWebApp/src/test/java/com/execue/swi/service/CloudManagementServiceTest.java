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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.Rule;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.CloudCategory;
import com.execue.core.common.type.CloudOutput;
import com.execue.core.common.type.ComponentCategory;
import com.execue.core.exception.swi.KDXException;

/**
 * @author execue
 */
public class CloudManagementServiceTest extends ExeCueBaseTest {

   private final Logger logger = Logger.getLogger(CloudManagementServiceTest.class);

   @BeforeClass
   public static void setUp () throws Exception {
      baseTestSetup();
   }

   @AfterClass
   public static void tearDown () throws Exception {
      baseTestTeardown();
   }

   @Test
   public void testDeleteClouds() {
      try {
         List<Cloud> clouds =  getKDXCloudRetrievalService().getAllCloudsByModelId(110L);
         System.out.println(clouds.size());
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }
   
   public void testCreate () {
      Cloud cloud = new Cloud();
      cloud.setName("XYZ-TimeFrame");
      cloud.setCategory(CloudCategory.TYPE_CLOUD);
      cloud.setCloudOutput(CloudOutput.NEW_VALUE);
      cloud.setRequiredComponentCount(1);
      cloud.setTypeName("TimeFrame");
      cloud.setTypeBedId(201L);
      cloud.setCloudValidationRules(getCloudRules(cloud));
      // cloud.setCloudComponents(getCloudComponents(cloud));
      cloud.setCloudAllowedBehavior(getCloudAllowedBehavior(cloud));
      cloud.setCloudAllowedComponents(getCloudAllowedComponents(cloud));
      try {
         cloud.setModels(getBaseCloudModels());
         getCloudManagementService().create(cloud);
         logger.debug("created cloud with id : " + cloud.getId());
         getCloudManagementService().addComponentsToCloud(new ArrayList<CloudComponent>(getCloudComponents(cloud)));
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   //@Test
   public void testRemove () {
      Cloud cloud = new Cloud();
      cloud.setId(5L);
      try {
         getCloudManagementService().removeComponentsFromCloud(getCloudComponentsToBeRemoved(cloud));
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   private Set<Model> getBaseCloudModels () throws KDXException {
      Set<Model> models = new HashSet<Model>();
      Model model = getBaseKDXRetrievalService().getBaseModel();
      models.add(model);
      return models;
   }

   private Set<BusinessEntityDefinition> getCloudAllowedComponents (Cloud cloud) {
      Set<BusinessEntityDefinition> allowedComponents = new HashSet<BusinessEntityDefinition>();
      BusinessEntityDefinition bed = new BusinessEntityDefinition();
      bed.setId(408L);
      allowedComponents.add(bed);
      bed = new BusinessEntityDefinition();
      bed.setId(204L);
      allowedComponents.add(bed);
      return allowedComponents;
   }

   private Set<BusinessEntityDefinition> getCloudAllowedBehavior (Cloud cloud) {
      Set<BusinessEntityDefinition> allowedBehavior = new HashSet<BusinessEntityDefinition>();
      return allowedBehavior;
   }

   private Set<CloudComponent> getCloudComponents (Cloud cloud) throws KDXException {
      Set<CloudComponent> cloudComponents = new HashSet<CloudComponent>();
      CloudComponent comp = new CloudComponent();
      Long bedId = 204L;
      BusinessEntityDefinition typeBED = getKDXRetrievalService().getBusinessEntityDefinitionById(bedId);
      comp.setComponentBed(typeBED);
      comp.setComponentCategory(ComponentCategory.TYPE);
      comp.setComponentTypeBed(typeBED);
      comp.setDefaultValue(null);
      comp.setEntityType(BusinessEntityType.TYPE_LOOKUP_INSTANCE);
      comp.setFrequency(1);
      comp.setRequired(CheckType.YES);
      comp.setRequiredBehavior(null);
      comp.setImportance(50.00);
      comp.setCloud(cloud);
      cloudComponents.add(comp);

      bedId = 202L;
      typeBED = getKDXRetrievalService().getBusinessEntityDefinitionById(bedId);
      comp = new CloudComponent();
      comp.setComponentBed(typeBED);
      comp.setComponentCategory(ComponentCategory.TYPE);
      comp.setComponentTypeBed(typeBED);
      comp.setDefaultValue(null);
      comp.setEntityType(BusinessEntityType.TYPE_LOOKUP_INSTANCE);
      comp.setFrequency(1);
      comp.setRequired(CheckType.NO);
      comp.setRequiredBehavior(null);
      comp.setImportance(50.00);
      comp.setCloud(cloud);
      cloudComponents.add(comp);
      return cloudComponents;
   }

   private List<CloudComponent> getCloudComponentsToBeRemoved (Cloud cloud) throws KDXException {
      List<CloudComponent> ccList = new ArrayList<CloudComponent>();
      CloudComponent cc = new CloudComponent();
      Long bedId = 202L;
      BusinessEntityDefinition typeBED = getKDXRetrievalService().getBusinessEntityDefinitionById(bedId);
      cc.setCloud(cloud);
      cc.setComponentBed(typeBED);
      ccList.add(cc);
      return ccList;
   }

   private Set<Rule> getCloudRules (Cloud cloud) {
      Set<Rule> rules = new HashSet<Rule>();
      Rule rule = new Rule();
      rule.setId(2L);
      rules.add(rule);
      rule = new Rule();
      rule.setId(3L);
      rules.add(rule);
      return rules;
   }
}
