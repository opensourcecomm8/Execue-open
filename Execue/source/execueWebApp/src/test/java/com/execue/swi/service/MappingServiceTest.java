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

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.exception.swi.MappingException;
import com.execue.core.util.ExeCueUtils;

public class MappingServiceTest extends ExeCueBaseTest {

   private static final Logger logger = Logger.getLogger(MappingServiceTest.class);

   @BeforeClass
   public static void setUp () {
      baseTestSetup();
   }

   private static void printMapping (Mapping mapping) {
      System.out.println("Mapping : [" + mapping.getAssetEntityDefinition().getId() + " <-> "
               + mapping.getBusinessEntityDefinition().getId() + "]");
   }

   // @Test
   public void testGetMappingByMappingId () {
      Long mappingId = 10001L;
      try {
         Mapping mapping = getMappingService().getMapping(mappingId);
         printMapping(mapping);
      } catch (MappingException me) {
         me.printStackTrace();
      }
   }

   // @Test
   public void testGetMappingsForAsset () {
      Long assetId = 17L;
      try {
         List<Mapping> mappings = getMappingService().getMappingsForAsset(assetId);
         System.out.println("Mappings size ::" + mappings.size());

      } catch (MappingException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetGrainConcepts () {
      Long modelId = 100L;
      Long assetId = 11L;
      try {
         List<Mapping> grains = getMappingService().getGrainConcepts(modelId, assetId);
         for (Mapping mapping : grains) {
            printMapping(mapping);
         }
      } catch (MappingException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetDistributionConcepts () {
      Long modelId = 100L;
      Long assetId = 11L;
      try {
         List<Mapping> grains = getMappingService().getDistributionConcepts(modelId, assetId);
         for (Mapping mapping : grains) {
            printMapping(mapping);
         }
      } catch (MappingException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetAssetGrain () {
      try {
         List<Mapping> grain = getMappingService().getAssetGrain(11L);
         for (Mapping mapping : grain) {
            System.out.println(mapping.getAssetEntityDefinition().getColum().getName());
            System.out.println(mapping.getAssetGrainType());
            System.out.println(mapping.getBusinessEntityDefinition().getConcept().getName());
         }
      } catch (MappingException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testCreateMappings () {
      List<Mapping> mappings = new ArrayList<Mapping>();
      Mapping mapping = new Mapping();
      BusinessEntityDefinition bed = new BusinessEntityDefinition();
      bed.setId(10250L);
      mapping.setBusinessEntityDefinition(bed);
      AssetEntityDefinition aed = new AssetEntityDefinition();
      aed.setId(130L);
      mapping.setAssetEntityDefinition(aed);
      mappings.add(mapping);
      try {
         getMappingService().createMappings(mappings);
      } catch (MappingException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetAssetEntities () {
      String TEST_CONCEPT_NAME = "FicoScore";
      Long modelId = 100L;
      Long assetId = 11L;
      BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
      Concept concept = new Concept();
      concept.setName(TEST_CONCEPT_NAME);
      businessEntityTerm.setBusinessEntity(concept);
      businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);

      List<Mapping> mappings = null;
      try {
         mappings = getMappingService().getAssetEntities(businessEntityTerm, modelId, assetId);
      } catch (MappingException e) {
         e.printStackTrace();
      }
      logger.debug(mappings.size());
      for (Mapping mapping : mappings) {
         logger.debug("Inside test " + mapping.getAssetEntityDefinition());
         logger.debug("Inside Test " + mapping.getAssetEntityDefinition().getAsset());
         logger.debug(mapping.getAssetEntityDefinition().getTabl().getName());
         logger.debug(mapping.getAssetEntityDefinition().getColum());
         logger.debug(mapping.getAssetEntityDefinition().getMembr());
      }
   }

   // TODO: -JVK- add test methods for deleteBusinessEntityMappings and deleteAssetEntityMappings service methods

   // @Test
   public void testGetUIMappings () {
      Long modelId = 100L;
      Long assetId = 11L;
      try {
         List<Mapping> mappings = getMappingService().getUIMappings(assetId, modelId);
         if (mappings.size() > 12) {
            Mapping mapping = mappings.get(10);
            logger.debug("ID : " + mapping.getId());
            logger.debug("mapped column : " + mapping.getAssetEntityDefinition().getColum().getName());
            logger.debug("mapped concept : " + mapping.getBusinessEntityDefinition().getConcept().getName());
         }
      } catch (MappingException e) {
         e.printStackTrace();
      }
   }

   // TODO: -JVK- add test method for deleteUIMappings service method

   // @Test
   public void testPersistUIMappings () {
      // Set the asset, table & column objects into AED object
      // Set the model_group id, concept id in the concept of bed
      List<Mapping> mappings = new ArrayList<Mapping>();
      Mapping mapping = new Mapping();
      BusinessEntityDefinition bed = new BusinessEntityDefinition();
      Concept concept = new Concept();
      concept.setId(3088L);
      bed.setConcept(concept);
      ModelGroup modelGroup = new ModelGroup();
      modelGroup.setId(100L);
      bed.setModelGroup(modelGroup);
      mapping.setBusinessEntityDefinition(bed);
      AssetEntityDefinition aed = new AssetEntityDefinition();
      Asset asset = new Asset();
      asset.setId(11L);
      aed.setAsset(asset);
      Tabl table = new Tabl();
      table.setId(109L);
      aed.setTabl(table);
      Colum column = new Colum();
      column.setId(1092L);
      aed.setColum(column);

      mapping.setAssetEntityDefinition(aed);
      mappings.add(mapping);
      try {
         getMappingService().persistUIMappings(mappings);
      } catch (MappingException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetMappingsForAED () {
      Long assetEntityDefinitionId = 113L;
      try {
         List<Mapping> mappingsForAED = getMappingService().getMappingsForAED(assetEntityDefinitionId);
         if (!ExeCueUtils.isCollectionEmpty(mappingsForAED)) {
            for (Mapping mapping : mappingsForAED) {
               printMapping(mapping);
            }
         }
      } catch (MappingException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetExistingMappingsForColumns () {
      Long assetId = 11L;
      try {
         List<Mapping> existingMappingsForColumns = getMappingService().getExistingMappingsForColumns(assetId);
         System.out.println("Mappings size: " + existingMappingsForColumns.size());
      } catch (MappingException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetMappingByAEDAndBED () {
      Long aedId = 113L;
      Long bedId = 138L;
      try {
         Mapping mapping = getMappingService().getMapping(aedId, bedId);
         if (mapping != null) {
            printMapping(mapping);
         }
      } catch (MappingException me) {
         me.printStackTrace();
      }
   }

   // @Test
   public void testGetMappingsForBED () {
      Long businessEntityDefinitionId = 138L;
      try {
         List<Mapping> mappingsForBED = getMappingService().getMappingsForBED(businessEntityDefinitionId);
         if (!ExeCueUtils.isCollectionEmpty(mappingsForBED)) {
            for (Mapping mapping : mappingsForBED) {
               printMapping(mapping);
            }
         }
      } catch (MappingException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testUpdateMapping () {
      Mapping mapping;
      try {
         mapping = getMappingService().getMapping(60004L);
         BusinessEntityDefinition businessEntityDefinition = mapping.getBusinessEntityDefinition();
         businessEntityDefinition.setId(10250L);
         getMappingService().updateMapping(mapping);
      } catch (MappingException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testUpdateMappings () {
      List<Mapping> mappings = new ArrayList<Mapping>();
      try {
         Mapping mapping = getMappingService().getMapping(60004L);
         BusinessEntityDefinition businessEntityDefinition = mapping.getBusinessEntityDefinition();
         businessEntityDefinition.setId(10061L);
         mappings.add(mapping);
         getMappingService().updateMappings(mappings);
      } catch (MappingException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetExistingMappingsForMembers () {
      Long assetId = 11L;
      try {
         List<Mapping> existingMappingsForMembers = getMappingService().getExistingMappingsForMembers(assetId);
         System.out.println("Mappings size: " + existingMappingsForMembers.size());
      } catch (MappingException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetMappedInstances () {
      Long modelId = 100L;
      Long conceptId = 3006L;
      Long assetId = 11L;
      try {
         List<Instance> mappedInstances = getMappingService().getMappedInstances(modelId, conceptId, assetId);
         System.out.println("Mapped Instances size : " + mappedInstances.size());
      } catch (MappingException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testUpdatePrimaryForUniqueMappingsByAsset () {
      try {
         Long updateCount = getMappingService().updatePrimaryForUniqueMappingsByAsset(new Long(17));
         System.out.println("Rows Updated : " + updateCount);
      } catch (MappingException mappingException) {
         mappingException.printStackTrace();
         fail("Updation Failed");
      }
   }

   // @Test
   public void testGetNonUniqueMappingsForAsset () {
      try {
         List<Mapping> nonUniqueMappingsForAsset = getMappingService().getNonUniqueMappingsForAsset(new Long(17));
         System.out.println("Non-Unique Mapping count for asset id [17]: " + nonUniqueMappingsForAsset.size());
      } catch (MappingException mappingException) {
         mappingException.printStackTrace();
         fail("Updation Failed");
      }
   }

   // @Test
   public void testGetBEDIdsOfConceptsMappedWithDiffInNature () {
      try {
         List<Mapping> mappingsOfConceptsMappedWithDiffInNature = getMappingService()
                  .getMappingsOfConceptsMappedWithDiffInNature(new Long(19));
         System.out.println("Mappings Of Concepts Mapped With Difference In Nature for asset id [19]: "
                  + mappingsOfConceptsMappedWithDiffInNature.size());
      } catch (MappingException mappingException) {
         mappingException.printStackTrace();
         fail("Updation Failed");
      }
   }

   @AfterClass
   public static void tearDown () {
      baseTestTeardown();
   }
}
