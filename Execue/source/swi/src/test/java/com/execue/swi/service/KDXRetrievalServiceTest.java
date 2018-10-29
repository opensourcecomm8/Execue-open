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
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.entity.BusinessEntityByWord;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.RISharedUserModelMapping;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.bean.governor.BusinessEntityInfo;
import com.execue.core.common.bean.nlp.CandidateEntity;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.swi.SWICommonBaseTest;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;

public class KDXRetrievalServiceTest extends SWICommonBaseTest {

   Logger logger = Logger.getLogger(KDXRetrievalServiceTest.class);

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   // @Test
   public void testGetBusinessEntityDefinitionByIds () {
      Long modelId = 100L;
      Long conceptId = 501L;
      try {
         BusinessEntityDefinition bed = getKDXRetrievalService().getBusinessEntityDefinitionByIds(modelId, conceptId,
                  null);
         System.out.println("bed id : " + bed.getId());
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetConceptBEDByName () {
      Long modelId = 100L;
      String conceptName = "Product";
      try {
         BusinessEntityDefinition bed = getKDXRetrievalService().getConceptBEDByName(modelId, conceptName);
         System.out.println("bed id : " + bed.getId());
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetModelById () {
      Long modelId = 100L;
      try {
         Model model = getKDXRetrievalService().getModelById(modelId);
         System.out.println("Model Name : " + model.getName());
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetConcepts () {
      Long modelId = 100L;
      try {
         List<Concept> concepts = getKDXRetrievalService().getConcepts(modelId);
         for (Concept concept : concepts) {
            logger.debug("Concept name --" + concept.getName());

         }
      } catch (KDXException e) {
         e.printStackTrace();
      }

   }

   // @Test
   public void testGetInstances () {
      Long modelId = 100L;
      Long conceptId = 3054L;
      try {
         List<Instance> instances = getKDXRetrievalService().getInstances(modelId, conceptId);
         for (Instance instance : instances) {
            logger.debug("Instance name --" + instance.getName());
         }

      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetBusinessEntityDefinitionByNames () {
      String modelName = "ABCCards Model";
      String conceptName = "Overlimit";
      String instanceName = "Overlimit2";
      try {
         BusinessEntityDefinition businessEntityDefinition = getKDXRetrievalService()
                  .getBusinessEntityDefinitionByNames(modelName, conceptName, instanceName);
         logger.debug("BED ID --" + businessEntityDefinition.getId());
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetInstancesOfConceptBySearchString () {
      Long modelId = 100L;
      Long conceptId = 3054L;
      String instanceSearchString = "limit";
      try {
         List<Instance> instances = getKDXRetrievalService().getInstancesOfConceptBySearchString(modelId, conceptId,
                  instanceSearchString);
         for (Instance instance : instances) {
            logger.debug("instance name --" + instance.getName());
         }
      } catch (KDXException e) {
         e.printStackTrace();
      }

   }

   // @Test
   public void testGetBEDByInstanceDisplayName () {
      String modelName = "ABCCards Model";
      String conceptName = "Overlimit";
      String instanceName = "Non Overlimit";
      try {
         BusinessEntityDefinition businessEntityDefinition = getKDXRetrievalService().getBEDByInstanceDisplayName(
                  modelName, conceptName, instanceName);
         logger.debug("BED ID --" + businessEntityDefinition.getId());
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetLatestInstanceInserted () {
      Long modelId = 100L;
      Long conceptId = 3054L;
      try {
         Instance instance = getKDXRetrievalService().getLatestInstanceInserted(modelId, conceptId);
         logger.debug("Instance name --" + instance.getName());
      } catch (KDXException e) {
         e.printStackTrace();
      }

   }

   // @Test
   public void testGetAllSFLTerms () {
      Long modelId = 100L;
      try {
         List<SFLTerm> sflTerms = getKDXRetrievalService().getAllSFLTerms();
         for (SFLTerm term : sflTerms) {
            logger.debug("sflterm value --" + term.getBusinessTerm());
         }
      } catch (KDXException e) {
         e.printStackTrace();
      }

   }

   // @Test
   public void testGetAllConceptsBusinessEntities () {
      Long modelId = 100L;

      try {
         List<BusinessEntityInfo> businessTerms = getKDXRetrievalService().getAllConceptBusinessEntities(modelId);
         for (BusinessEntityInfo term : businessTerms) {
            logger.debug("businessEntityTerm value --" + term.getBusinessEntityTermDisplayName());
         }
      } catch (KDXException e) {
         e.printStackTrace();
      }

   }

   // @Test
   public void testIsExactBusinessTerm () {
      Long modelId = 100L;
      String businessTermName = "Overlimit";
      try {
         Long id = getKDXRetrievalService().isExactBusinessTerm(modelId, businessTermName);
         logger.debug("businessTerm id --" + id);

      } catch (KDXException e) {
         e.printStackTrace();
      }

   }

   // @Test
   public void testIsPartOfBusinessTerm () {
      Long modelId = 100L;
      String businessTermName = "Overlimit";
      try {
         Boolean status = getKDXRetrievalService().isPartOfBusinessTerm(modelId, businessTermName);
         logger.debug("businessTerm id status --" + status);

      } catch (KDXException e) {
         e.printStackTrace();
      }

   }

   // @Test
   public void testGetPrimaryGroup () {
      Long modelId = 102L;
      try {
         ModelGroup modelGroup = getKDXRetrievalService().getPrimaryGroup(modelId);
         logger.debug("modelGroup name --" + modelGroup.getName());
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testConceptWithStat () {
      Concept concept = null;
      try {

         concept = getKDXRetrievalService().getPopulatedConceptWithStats(100L, "FICO SCORE");
         logger.info("Description " + concept.getDescription());
         // log.info("Stat "+concept.getStats());
      } catch (Exception e) {
         e.printStackTrace();
      }

      org.junit.Assert.assertNotNull(concept);
   }

   // @Test
   public void testGetMappingById () {
      try {
         Mapping m = getKDXRetrievalService().getMappingById(5002L);
         System.out.println(m.getAssetEntityDefinition().getAsset());
      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetStatByBusinessEntityId () {
      try {
         Stat stat = getKDXRetrievalService().getStatByBusinessEntityId(172L);
         System.out.println(stat.getId());
         System.out.println(stat.getStatType().getValue());
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetStatByName () {
      try {
         Stat stat = getKDXRetrievalService().getStatByName("SUM");
         System.out.println(stat.getId());
         System.out.println(stat.getDisplayName());
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetBusinessEntityWordMapByLookupWords () {
      List<String> words = new ArrayList<String>();
      words.add("ChargedOff");
      words.add("Revolving");
      try {
         @SuppressWarnings ("unused")
         Map<String, BusinessEntityByWord> businessEntityWordMap = getKDXRetrievalService()
                  .getBusinessEntityWordMapByLookupWords(words);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetDisplayNameByBEDIds () {
      List<Long> bedIds = new ArrayList<Long>();
      bedIds.add(1007L);
      bedIds.add(3286L);
      try {
         System.out.println(getKDXRetrievalService().getDisplayNamesByBEDIds(bedIds));
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetConceptByName () {
      String conceptName = "Sector";
      try {

         Concept concept = getKDXRetrievalService().getConceptByName(101L, conceptName);
         if (logger.isDebugEnabled()) {
            logger.debug("Concept id for concept name [" + conceptName + "] : " + concept.getId());
         }
      } catch (KDXException kdxException) {
         logger.error(kdxException, kdxException);
         fail(kdxException.getMessage());
      }
   }

   // @Test
   public void testGetRelationByName () {
      String relationName = "belongsToSector";
      try {
         Relation relation = getKDXRetrievalService().getRelationByName(101L, relationName, false);
         if (logger.isDebugEnabled()) {
            logger.debug("Relation id for relation name [" + relationName + "] : " + relation.getId());
         }
      } catch (KDXException kdxException) {
         logger.error(kdxException, kdxException);
         fail(kdxException.getMessage());
      }
   }

   // @Test
   public void testUpdateOrderForSFLTermTokens () {
      try {
         getKDXManagementService().updateOrderForSFLTermTokens();
      } catch (KDXException kdxException) {
         logger.error(kdxException, kdxException);
         fail(kdxException.getMessage());
      }
   }

   // @Test
   public void testUpdateWeightsForCandidateEntities () {
      CandidateEntity candidateEntity = new CandidateEntity();
      candidateEntity.setType(RecognitionEntityType.SFL_ENTITY);
      candidateEntity.setId(10L);
      candidateEntity.setName("return on equity");
      List<String> words = new ArrayList<String>();
      words.add("return");
      candidateEntity.setWords(words);
      CandidateEntity candidateEntity1 = new CandidateEntity();
      candidateEntity1.setType(RecognitionEntityType.PW_ENTITY);
      candidateEntity1.setId(1029L);
      candidateEntity1.setName("abducted");
      List<CandidateEntity> enList = new ArrayList<CandidateEntity>();
      enList.add(candidateEntity);
      try {
         getKDXManagementService().updateWeightsForCandidateEntities(enList);
      } catch (KDXException kdxException) {
         logger.error(kdxException, kdxException);
         fail(kdxException.getMessage());
      }
   }

   // @Test

   public void testGetBEDByMappingconceptName () {
      Long modelId = 101L;
      Long mappingId = 1001L;
      String conceptDisplayName = "";
      try {
         getKDXRetrievalService().getBEDByMappingConceptName(modelId, mappingId, conceptDisplayName);
      } catch (KDXException e) {
         e.printStackTrace();
         fail(e.getMessage());
      }

   }

   // @Test
   public void testLookupTypeConcepts () {
      Long modelId = 101L;
      try {
         getKDXRetrievalService().getLookupTypeConceptsForModelBySearchString(modelId, "co", null, 10L);
      } catch (KDXException e) {
         e.printStackTrace();

      }
   }

   // @Test
   public void testConceptMatchedBehavior () {
      try {
         boolean isConceptMatchedBehavior = getKDXRetrievalService().isConceptMatchedBehavior(19740L,
                  BehaviorType.MULTIVALUED);
         System.out.println("boolean value::::"
                  + ExecueBeanUtil.getCorrespondingCheckTypeValue(isConceptMatchedBehavior));
      } catch (KDXException e) {
         e.printStackTrace();

      }

   }

   // @Test
   public void testRIShareModelMapping () {
      try {
         Set<Long> modelGroupIds = getModelGroupIds(getKDXRetrievalService().getUserModelGroupsByModelId(110L));
         RISharedUserModelMapping sharedUserModelMappingByAppInstanceBedId = getKDXRetrievalService()
                  .getRISharedUserModelMappingByAppInstanceBedId(728074L, modelGroupIds);

         System.out.println(sharedUserModelMappingByAppInstanceBedId);
      } catch (KDXException e) {
         e.printStackTrace();

      }

   }

   //  @Test
   public void testOrphanSFLTerms () {
      try {
         List<SFLTerm> orphanSFLTerms = getKDXRetrievalService().getOrphanSFLTerms(111L);
         for (SFLTerm sflTerm : orphanSFLTerms) {
            System.out.println("sflTerm name:::::::::" + sflTerm.getBusinessTerm());
         }
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   @Test
   public void testGetConceotByBedIds () {
      try {
         List<Long> conceptBEDIds = new ArrayList<Long>();
         conceptBEDIds.add(103l);
         conceptBEDIds.add(104l);
         conceptBEDIds.add(105l);
         List<Concept> concepts = getKDXRetrievalService().getConceptByBEDIds(conceptBEDIds);
         for (Concept concept : concepts) {
            System.out.println("concept name:::::::::" + concept.getDisplayName());
         }
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }
}
