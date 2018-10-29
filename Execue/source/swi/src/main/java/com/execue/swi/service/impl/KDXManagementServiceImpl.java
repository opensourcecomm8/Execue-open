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


package com.execue.swi.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityVariation;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.EntityBehavior;
import com.execue.core.common.bean.entity.EntityDetailType;
import com.execue.core.common.bean.entity.Hierarchy;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.ModelGroupMapping;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.entity.RISharedUserModelMapping;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.SFLTermToken;
import com.execue.core.common.bean.entity.SecondaryWord;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.bean.entity.VerticalAppWeight;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.nlp.CandidateEntity;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.CloudComponentSelectionType;
import com.execue.core.common.type.ComponentCategory;
import com.execue.core.common.type.EntityType;
import com.execue.core.common.type.OperationType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.dataaccess.IKDXCloudDataAccessManager;
import com.execue.swi.dataaccess.IKDXDataAccessManager;
import com.execue.swi.dataaccess.IKDXModelDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IBusinessEntityMaintenanceService;
import com.execue.swi.service.IKDXCloudManagementService;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.validation.ISWIValidator;

public class KDXManagementServiceImpl implements IKDXManagementService {

   private static final Logger               logger = Logger.getLogger(KDXManagementServiceImpl.class);

   private IKDXDataAccessManager             kdxDataAccessManager;
   private IKDXModelDataAccessManager        kdxModelDataAccessManager;
   private ISWIValidator                     swiValidator;
   private IKDXRetrievalService              kdxRetrievalService;
   private IBaseKDXRetrievalService          baseKDXRetrievalService;
   private ISWIConfigurationService          swiConfigurationService;
   private IKDXCloudRetrievalService         kdxCloudRetrievalService;
   private IKDXCloudManagementService        kdxCloudManagementService;
   private IKDXCloudDataAccessManager        kdxCloudDataAccessManager;
   private IBusinessEntityMaintenanceService businessEntityMaintenanceService;

   public IKDXCloudManagementService getKdxCloudManagementService () {
      return kdxCloudManagementService;
   }

   public void setKdxCloudManagementService (IKDXCloudManagementService kdxCloudManagementService) {
      this.kdxCloudManagementService = kdxCloudManagementService;
   }

   public IKDXCloudRetrievalService getKdxCloudRetrievalService () {
      return kdxCloudRetrievalService;
   }

   public void setKdxCloudRetrievalService (IKDXCloudRetrievalService kdxCloudRetrievalService) {
      this.kdxCloudRetrievalService = kdxCloudRetrievalService;
   }

   /**
    * @return the swiConfigurationService
    */
   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   /**
    * @param swiConfigurationService
    *           the swiConfigurationService to set
    */
   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public void createModel (Model model, Application application) throws KDXException {
      Set<Application> applications = new HashSet<Application>();
      applications.add(application);
      model.setApplications(applications);
      getKdxDataAccessManager().createModel(model);
   }

   public void createModelGroup (ModelGroup modelGroup) throws KDXException {
      getKdxDataAccessManager().createModelGroup(modelGroup);
   }

   public void createModelGroupMapping (Model model, ModelGroup modelGroup, CheckType owner, CheckType primary)
            throws KDXException {
      ModelGroupMapping modelGroupMapping = new ModelGroupMapping();
      modelGroupMapping.setModel(model);
      modelGroupMapping.setModelGroup(modelGroup);
      modelGroupMapping.setPrimary(primary);
      modelGroupMapping.setOwner(owner);
      getKdxDataAccessManager().createModelGroupMapping(modelGroupMapping);
   }

   public void createRealizedType (Long modelId, Type type, boolean isRealizedType, Long knowledgeId)
            throws KDXException {
      adjustTypeName(type);
      if (getKdxRetrievalService().getTypeByName(type.getName()) != null) {
         throw new KDXException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, "Type with name [" + type.getName()
                  + "] already exists");
      }
      getKdxDataAccessManager().createType(modelId, type, isRealizedType, knowledgeId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXManagementService#createType(com.execue.core.common.bean.entity.Type)
    */
   public void createType (Long modelId, Type type, boolean isRealizedType, Long knowledgeId) throws KDXException {
      adjustTypeName(type);
      if (getKdxRetrievalService().getTypeByName(type.getName()) != null) {
         throw new KDXException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, "Type with name [" + type.getName()
                  + "] already exists");
      }
      getKdxDataAccessManager().createType(modelId, type, isRealizedType, knowledgeId);
   }

   public BusinessEntityDefinition createConcept (Long modelId, Type type, Concept concept, Long knowledgeId)
            throws KDXException {
      // get the default cloud
      Cloud cloud = kdxCloudRetrievalService.getDefaultAppCloud(modelId);
      return createConcept(modelId, cloud, type, concept, knowledgeId);
   }

   public BusinessEntityDefinition createConcept (Long modelId, Cloud cloud, Type type, Concept concept,
            Long knowledgeId) throws KDXException {
      if (cloud == null) {
         cloud = kdxCloudRetrievalService.getDefaultAppCloud(modelId);
      }
      adjustConceptName(concept);
      if (getKdxRetrievalService().getConceptByName(modelId, concept.getName()) != null) {
         throw new KDXException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, "Concept with name [" + concept.getName()
                  + "] already exists");
      }
      BusinessEntityDefinition conceptBED = getKdxDataAccessManager()
               .createConcept(modelId, type, concept, knowledgeId);
      BusinessEntityDefinition typeBusinessEntityDefinition = kdxRetrievalService.getTypeBusinessEntityDefinition(type
               .getId());
      List<CloudComponent> componentsToBeAdded = new ArrayList<CloudComponent>();
      componentsToBeAdded.add(ExecueBeanManagementUtil.prepareCloudComponent(cloud, conceptBED,
               typeBusinessEntityDefinition, ComponentCategory.REALIZATION));
      kdxCloudManagementService.addComponentsToCloud(componentsToBeAdded);
      getBusinessEntityMaintenanceService().createBusinessEntityMaintenanceInfo(
               ExecueBeanManagementUtil.prepareBusinessEntityMaintenanceInfo(conceptBED.getId(),
                        com.execue.core.common.type.EntityType.CONCEPT, modelId, OperationType.ADD, null));
      return conceptBED;
   }

   public BusinessEntityDefinition createConcept (Long modelId, Concept concept, Long knowledgeId) throws KDXException {
      // get the default cloud
      Cloud cloud = kdxCloudRetrievalService.getDefaultAppCloud(modelId);
      // set the default type - OntoEntity
      Type type = kdxRetrievalService.getTypeByName(ExecueConstants.ONTO_ENTITY_TYPE);
      return createConcept(modelId, cloud, type, concept, knowledgeId);

   }

   // TODO: -JM- proper exception handling
   // TODO - NK: New method needs to be added to delete the component from the
   // cloud
   // TODO - NK: cloud component should always accompany this call
   public BusinessEntityDefinition updateConcept (Long modelId, Concept concept) throws KDXException {
      try {
         adjustConceptName(concept);
         BusinessEntityDefinition conceptBED = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
                  concept.getId(), null);
         Concept existingConcept = getKdxRetrievalService().getConceptByName(modelId, concept.getName());
         if (existingConcept != null && !concept.getId().equals(existingConcept.getId())) {
            throw new KDXException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, "Concept with name [" + concept.getName()
                     + "] already exists");
         }
         // TODO: NK: get the default cloud for now, should get the existing cloud
         Cloud cloud = kdxCloudRetrievalService.getDefaultAppCloud(modelId);
         BusinessEntityDefinition updatedConceptBED = getKdxDataAccessManager().updateConcept(modelId, cloud,
                  conceptBED.getType(), concept, conceptBED);
         getBusinessEntityMaintenanceService().createBusinessEntityMaintenanceInfo(
                  ExecueBeanManagementUtil.prepareBusinessEntityMaintenanceInfo(conceptBED.getId(), EntityType.CONCEPT,
                           modelId, OperationType.UPDATE, null));
         return updatedConceptBED;
      } catch (SWIException swiException) {
         logger.error(swiException);
         throw new KDXException(swiException.getCode(), swiException);
      }
   }

   // TODO: -JM- proper exception handling
   // TODO - NK: New method needs to be added to delete the component from the
   // cloud
   // TODO - NK: cloud component should always accompany this call
   public BusinessEntityDefinition saveOrUpdateConcept (Long modelId, Cloud cloud, Type type, Concept concept,
            Long knowledgeId) throws KDXException {
      BusinessEntityDefinition conceptBED = null;
      adjustConceptName(concept);
      try {
         Concept existingConcept = getKdxRetrievalService().getConceptByName(modelId, concept.getName());
         if (existingConcept == null) {
            conceptBED = createConcept(modelId, cloud, type, concept, knowledgeId);
         } else {
            concept.setId(existingConcept.getId());
            conceptBED = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId, concept.getId(), null);
            conceptBED = getKdxDataAccessManager().updateConcept(modelId, cloud, type, concept, conceptBED);
            getBusinessEntityMaintenanceService().createBusinessEntityMaintenanceInfo(
                     ExecueBeanManagementUtil.prepareBusinessEntityMaintenanceInfo(conceptBED.getId(),
                              EntityType.CONCEPT, modelId, OperationType.UPDATE, null));
         }
      } catch (KDXException kde) {
         if (kde.getCode() == DataAccessExceptionCodes.ENTITY_NOT_FOUND_CODE) {
            getKdxDataAccessManager().createConcept(modelId, type, concept, knowledgeId);
         } else {
            throw kde;
         }
      }
      return conceptBED;
   }

   public BusinessEntityDefinition saveOrUpdateConcept (Long modelId, Type type, Concept concept, Long knowledgeId)
            throws KDXException {
      // get the default cloud
      Cloud cloud = kdxCloudRetrievalService.getDefaultAppCloud(modelId);
      return saveOrUpdateConcept(modelId, cloud, type, concept, knowledgeId);
   }

   public BusinessEntityDefinition saveOrUpdateConcept (Long modelId, Concept concept, Long knowledgeId)
            throws KDXException {
      BusinessEntityDefinition conceptBED = null;
      adjustConceptName(concept);
      try {
         Concept existingConcept = getKdxRetrievalService().getConceptByName(modelId, concept.getName());
         if (existingConcept == null) {
            // get the default cloud
            Cloud cloud = kdxCloudRetrievalService.getDefaultAppCloud(modelId);
            // set the default type - OntoEntity
            Type type = kdxRetrievalService.getTypeByName(ExecueConstants.ONTO_ENTITY_TYPE);
            conceptBED = createConcept(modelId, cloud, type, concept, knowledgeId);
         } else {
            concept.setId(existingConcept.getId());
            conceptBED = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId, concept.getId(), null);
            // TODO: NK: get the default cloud for now, should get the existing cloud
            Cloud cloud = kdxCloudRetrievalService.getDefaultAppCloud(modelId);
            conceptBED = getKdxDataAccessManager().updateConcept(modelId, cloud, conceptBED.getType(), concept,
                     conceptBED);
            getBusinessEntityMaintenanceService().createBusinessEntityMaintenanceInfo(
                     ExecueBeanManagementUtil.prepareBusinessEntityMaintenanceInfo(conceptBED.getId(),
                              EntityType.CONCEPT, modelId, OperationType.UPDATE, null));
         }
      } catch (KDXException kde) {
         if (kde.getCode() == DataAccessExceptionCodes.ENTITY_NOT_FOUND_CODE) {
            // set the default type - OntoEntity
            Type type = kdxRetrievalService.getTypeByName(ExecueConstants.ONTO_ENTITY_TYPE);
            getKdxDataAccessManager().createConcept(modelId, type, concept, knowledgeId);
         } else {
            throw kde;
         }
      }
      return conceptBED;
   }

   public void updateOrderForSFLTermTokens () throws KDXException {
      getKdxDataAccessManager().insertOrderForSFLTermTokens();
   }

   public void updateSFLTermToken (SFLTermToken termToken) throws KDXException {
      getKdxDataAccessManager().updateSFLTermToken(termToken);
   }

   public void updateSFLTermTokens (List<SFLTermToken> termTokens) throws KDXException {
      getKdxDataAccessManager().updateSFLTermTokens(termTokens);
   }

   public void updateTokenWeightsForSFLTerm (Long sflTermId) throws KDXException {
      List<SFLTermToken> tokens = getKdxDataAccessManager().getSFLTermTokensByTermId(sflTermId);
      for (SFLTermToken token : tokens) {
         token.setHits(token.getHits() + 1);
         getKdxDataAccessManager().updateSFLTermToken(token);
      }
   }

   private void updateTokenWeightsForSFLTerm (Long sflTermId, List<String> words) throws KDXException {
      List<SFLTermToken> tokens = getKdxDataAccessManager().getSFLTermTokensByTermId(sflTermId);
      for (SFLTermToken token : tokens) {
         if (words.contains(token.getBusinessTermToken().toLowerCase())) {
            token.setHits(token.getHits() + 1);
            getKdxDataAccessManager().updateSFLTermToken(token);
         }
      }
   }

   private void updateWeightForParallelWord (Long id) throws KDXException {
      RIParallelWord parallelWord = getKdxDataAccessManager().getParallelWordById(id);
      parallelWord.setHits(parallelWord.getHits() + 1);
      getKdxDataAccessManager().updateRIParallelWord(parallelWord);
   }

   public void updateWeightsForCandidateEntities (List<CandidateEntity> entities) throws KDXException {
      for (CandidateEntity candidateEntity : entities) {
         List<String> words = candidateEntity.getWords();
         Long entityId = candidateEntity.getId();
         if (candidateEntity.getType().equals(com.execue.core.common.bean.nlp.RecognitionEntityType.PW_ENTITY)) {
            updateWeightForParallelWord(entityId);
         } else if (candidateEntity.getType().equals(com.execue.core.common.bean.nlp.RecognitionEntityType.SFL_ENTITY)) {
            updateTokenWeightsForSFLTerm(entityId, words);
         }
      }
   }

   public BusinessEntityDefinition updateRelation (Long modelId, Relation relation) throws KDXException {
      getKdxDataAccessManager().updateRelation(modelId, relation);
      BusinessEntityDefinition relationBED = getKdxRetrievalService().getRelationBEDByName(modelId, relation.getName());
      getBusinessEntityMaintenanceService().createBusinessEntityMaintenanceInfo(
               ExecueBeanManagementUtil.prepareBusinessEntityMaintenanceInfo(relationBED.getId(), EntityType.RELATION,
                        modelId, OperationType.ADD, null));
      return relationBED;
   }

   public Integer updateBusinessEntitiesPopularity (List<BusinessEntityTerm> businessEntityTerms) throws KDXException {
      return getKdxDataAccessManager().updateBusinessEntitiesPopularity(businessEntityTerms);
   }

   public void updateBusinessEntityDefinitions (List<BusinessEntityDefinition> businessEntityDefinitions)
            throws KDXException {
      getKdxDataAccessManager().updateBusinessEntityDefinitions(businessEntityDefinitions);
   }

   public BusinessEntityDefinition createInstance (Long modelId, Long conceptId, Instance instance, Long knowledgeId)
            throws KDXException {
      try {
         Concept concept = getKdxDataAccessManager().getById(conceptId, Concept.class);
         adjustInstanceName(modelId, concept, instance);
         if (getSwiValidator().instanceExistsForConcept(modelId, conceptId, instance.getDisplayName())) {
            throw new KDXException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, "Instance with name ["
                     + instance.getDisplayName() + "] already exists");
         }
         BusinessEntityDefinition businessEntityDefinition = getKdxRetrievalService().getBusinessEntityDefinitionByIds(
                  modelId, conceptId, null);
         BusinessEntityDefinition instanceBED = getKdxDataAccessManager().createInstance(modelId,
                  businessEntityDefinition, instance, knowledgeId);
         getBusinessEntityMaintenanceService().createBusinessEntityMaintenanceInfo(
                  ExecueBeanManagementUtil.prepareBusinessEntityMaintenanceInfo(instanceBED.getId(),
                           EntityType.CONCEPT_LOOKUP_INSTANCE, modelId, OperationType.ADD, businessEntityDefinition
                                    .getId()));
         return instanceBED;
      } catch (SWIException swiException) {
         throw new KDXException(swiException.getCode(), swiException);
      }
   }

   @Override
   public BusinessEntityDefinition createTypeInstance (Long modelId, Long typeId, Instance instance, Long knowledgeId)
            throws KDXException {
      return createTypeInstance(modelId, modelId, typeId, instance, knowledgeId);
   }

   public BusinessEntityDefinition createTypeInstance (Long modelId, Long typeModelGroupId, Long typeId,
            Instance instance, Long knowledgeId) throws KDXException {
      try {
         Type type = getKdxDataAccessManager().getById(typeId, Type.class);
         adjustTypeInstanceName(modelId, type, instance);
         if (getSwiValidator().instanceExistsForType(typeModelGroupId, typeId, instance.getDisplayName())) {
            throw new KDXException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, "Instance with name [" + instance.getName()
                     + "] already exists");
         }
         BusinessEntityDefinition typeBED = getKdxRetrievalService().getBusinessEntityDefinitionByTypeIds(
                  typeModelGroupId, typeId, null);
         BusinessEntityDefinition typeInstanceBED = getKdxDataAccessManager().createTypeInstance(modelId, typeBED,
                  instance, knowledgeId);
         getBusinessEntityMaintenanceService().createBusinessEntityMaintenanceInfo(
                  ExecueBeanManagementUtil.prepareBusinessEntityMaintenanceInfo(typeInstanceBED.getId(),
                           EntityType.TYPE_LOOKUP_INSTANCE, modelId, OperationType.ADD, typeBED.getId()));
         return typeInstanceBED;
      } catch (SWIException swiException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, swiException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXManagementService#createRelation(java.lang.Long,
    *      com.execue.core.common.bean.entity.Relation)
    */
   public BusinessEntityDefinition createRelation (Long modelId, Relation relation, Long knowledgeId)
            throws KDXException {
      // get the default cloud
      Cloud cloud = kdxCloudRetrievalService.getDefaultAppCloud(modelId);
      // set the default type - OntoEntity
      Type type = kdxRetrievalService.getTypeByName(ExecueConstants.ONTO_ENTITY_TYPE);
      return createRelation(modelId, cloud, type, relation, knowledgeId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXManagementService#createRelation(java.lang.Long,
    *      com.execue.core.common.bean.entity.Cloud, com.execue.core.common.bean.entity.Type,
    *      com.execue.core.common.bean.entity.Relation)
    */
   public BusinessEntityDefinition createRelation (Long modelId, Cloud cloud, Type type, Relation relation,
            Long knowledgeId) throws KDXException {
      adjustRelationName(relation);
      if (getKdxRetrievalService().getRelationByName(modelId, relation.getName(), true) != null) {
         throw new KDXException(SWIExceptionCodes.RELATION_ALREADY_EXIST, "Relation with name [" + relation.getName()
                  + "] already exists");
      }
      BusinessEntityDefinition relationBed = getKdxDataAccessManager().createRelation(modelId, relation, type,
               knowledgeId);
      BusinessEntityDefinition typeBusinessEntityDefinition = kdxRetrievalService.getTypeBusinessEntityDefinition(type
               .getId());
      List<CloudComponent> componentsToBeAdded = new ArrayList<CloudComponent>();
      componentsToBeAdded.add(ExecueBeanManagementUtil.prepareCloudComponent(cloud, relationBed,
               typeBusinessEntityDefinition, ComponentCategory.REALIZATION));
      kdxCloudManagementService.addComponentsToCloud(componentsToBeAdded);
      getBusinessEntityMaintenanceService().createBusinessEntityMaintenanceInfo(
               ExecueBeanManagementUtil.prepareBusinessEntityMaintenanceInfo(relationBed.getId(), EntityType.RELATION,
                        modelId, OperationType.ADD, null));
      return relationBed;
   }

   public BusinessEntityDefinition updateInstance (Long modelId, Long conceptId, Instance instance) throws KDXException {
      try {
         BusinessEntityDefinition conceptBED = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
                  conceptId, null);
         BusinessEntityDefinition existingInstanceBED = getKdxRetrievalService().getInstanceBEDByDisplayName(modelId,
                  conceptBED.getConcept().getName(), instance.getDisplayName());
         if (existingInstanceBED != null && !instance.getId().equals(existingInstanceBED.getInstance().getId())) {
            throw new KDXException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, "Instance with name ["
                     + instance.getDisplayName() + "] already exists");
         }
         BusinessEntityDefinition instanceBED = getKdxDataAccessManager().updateInstance(modelId, conceptId, instance);
         getBusinessEntityMaintenanceService().createBusinessEntityMaintenanceInfo(
                  ExecueBeanManagementUtil.prepareBusinessEntityMaintenanceInfo(instanceBED.getId(),
                           EntityType.CONCEPT_LOOKUP_INSTANCE, modelId, OperationType.UPDATE, conceptBED.getId()));
         return instanceBED;
      } catch (SWIException e) {
         logger.error(e);
         throw new KDXException(e.getCode(), e);
      }

   }

   public void createSFLTerm (SFLTerm sflTerm) throws KDXException {
      getKdxDataAccessManager().createSFLTerm(sflTerm);
   }

   public void createSecondaryWords (List<SecondaryWord> secondaryWords) throws KDXException {
      getKdxDataAccessManager().createSecondaryWords(secondaryWords);
   }

   private void adjustConceptName (Concept concept) {
      concept.setName(ExecueStringUtil.getNormalizedName(concept.getDisplayName(), 55));
   }

   private void adjustTypeName (Type type) {
      type.setName(ExecueStringUtil.getNormalizedName(type.getDisplayName(), 55));
   }

   private void adjustRelationName (Relation relation) {
      relation.setName(ExecueStringUtil.getNormalizedName(relation.getDisplayName(), 55));
   }

   private void adjustInstanceName (Long modelId, Concept concept, Instance instance) throws KDXException {
      List<Instance> instances = getKdxDataAccessManager().getInstances(modelId, concept.getId());
      int counter = 0;
      if (instances != null) {
         counter = instances.size();
      }
      instance.setName(concept.getName() + (counter + 1));
   }

   private void adjustTypeInstanceName (Long modelId, Type type, Instance instance) throws KDXException {
      List<Instance> instances = getKdxDataAccessManager().getTypeInstances(modelId, type.getId());
      int counter = 0;
      if (instances != null) {
         counter = instances.size();
      }
      instance.setName(type.getName() + (counter + 1));
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the baseKDXRetrievalService
    */
   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   /**
    * @param baseKDXRetrievalService
    *           the baseKDXRetrievalService to set
    */
   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXManagementService#deleteModel(java.lang.Long)
    */
   // public void deleteModel (Long modelId) throws KDXException {
   // try {
   // getPathDefinitionDataAccessManager().deletePathDefinitionsForModel(modelId);
   // getMappingService().deleteBusinessEntityMappingForModel(modelId);
   // getKdxDataAccessManager().deleteConceptAndRespectiveInstancesForModel(modelId);
   // } catch (DataAccessException e) {
   // throw new KDXException(e.code, e);
   // } catch (MappingException e) {
   // throw new KDXException(e.code, e);
   // }
   //
   // }
   /**
    * @return the mappingService
    */

   public void updateBusinessEntityDefinition (BusinessEntityDefinition businessEntityDefinition) throws KDXException {
      getKdxDataAccessManager().updateBusinessEntityDefinition(businessEntityDefinition);
   }

   public void updateModel (Model model) throws KDXException {
      getKdxDataAccessManager().updateModel(model);
   }

   public void restrictModelFromEvaluation (Long modelId) throws KDXException {
      Model model = getKdxRetrievalService().getModelById(modelId);
      model.setEvaluate(CheckType.NO);
      updateModel(model);
   }

   public void createEntityDetailType (EntityDetailType entityDetailType) throws KDXException {
      getKdxDataAccessManager().createEntityDetailType(entityDetailType);

   }

   /**
    * *******************************NLPV4 Code starts here**********************************************
    */

   /*
    * @throws KDXException
    */
   public void createCloud (Cloud cloud, Long modelId) throws SWIException {
      Model model = getKdxDataAccessManager().getById(modelId, Model.class);
      Cloud existingCloud = getKdxModelDataAccessManager().getCloudByName(cloud.getName());
      if (existingCloud != null) {
         throw new KDXException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, "Cloud with name [" + cloud.getName()
                  + "] already exists");
      }
      Set<Model> models = cloud.getModels();
      if (CollectionUtils.isEmpty(models)) {
         models = new HashSet<Model>(1);
      }
      models.add(model);
      cloud.setModels(models);
      getKdxModelDataAccessManager().createCloud(cloud);
   }

   public void addBusinessEntityToCloud (BusinessEntityDefinition bed, Long cloudId) throws SWIException {
      Cloud cloud = getKdxDataAccessManager().getById(cloudId, Cloud.class);
      Set<Cloud> clouds = bed.getClouds();
      if (CollectionUtils.isEmpty(clouds)) {
         clouds = new HashSet<Cloud>(1);
      }
      clouds.add(cloud);
      bed.setClouds(clouds);
      getKdxModelDataAccessManager().addBusinessEntityToCloud(bed);

   }

   public void createEntityBehaviors (Long conceptBedId, List<BehaviorType> behaviorTypes) throws KDXException {

      if (!CollectionUtils.isEmpty(behaviorTypes)) {
         List<BehaviorType> existingBehaviors = getKdxRetrievalService().getAllBehaviorForEntity(conceptBedId);
         List<EntityBehavior> entityBehaviors = new ArrayList<EntityBehavior>(1);
         for (BehaviorType behavior : behaviorTypes) {
            // skip the existing behaviors
            if (existingBehaviors.contains(behavior)) {
               continue;
            }

            EntityBehavior entityBehavior = new EntityBehavior();
            entityBehavior.setBehaviorType(behavior);
            entityBehavior.setEntityBedId(conceptBedId);
            entityBehaviors.add(entityBehavior);
         }
         getKdxDataAccessManager().createEntityBehaviors(entityBehaviors);
      }
   }

   public void updateCloudComponentsType (Long cloudId, BusinessEntityDefinition typeBED,
            BusinessEntityDefinition conceptBED, boolean isAttribute) throws KDXException {
      CloudComponent cloudComponent = getKdxCloudRetrievalService().getCloudComponentByCloudIdAndComponentBedId(
               cloudId, conceptBED.getId());
      if (cloudComponent != null) {
         cloudComponent.setComponentTypeBed(typeBED);
         if (isAttribute) {
            cloudComponent.setCloudSelection(CloudComponentSelectionType.NOT_ENOUGH_FOR_CLOUD_SELECTION);
         } else {
            cloudComponent.setCloudSelection(CloudComponentSelectionType.ENOUGH_FOR_CLOUD_SELECTION);
         }
         getKdxCloudDataAccessManager().updateCloudComponent(cloudComponent);
      }
   }

   public void updateConceptAndInstancesType (Long modelId, BusinessEntityDefinition typeBED,
            BusinessEntityDefinition conceptBED) throws KDXException {
      conceptBED.setType(typeBED.getType());
      List<BusinessEntityDefinition> bedsToUpdate = new ArrayList<BusinessEntityDefinition>(1);
      bedsToUpdate.add(conceptBED);
      // TODO :-VG- hack to make all the instances also of the same type as concept
      // Added the check, as we don't need this for concept profile bed
      if (conceptBED.getEntityType() == BusinessEntityType.CONCEPT) {
         List<BusinessEntityDefinition> instanceBEDs = getKdxRetrievalService().getInstanceBEDsByConceptId(modelId,
                  conceptBED.getConcept().getId());
         if (ExecueCoreUtil.isCollectionNotEmpty(instanceBEDs)) {
            for (BusinessEntityDefinition businessEntityDefinition : instanceBEDs) {
               businessEntityDefinition.setType(typeBED.getType());
            }
            bedsToUpdate.addAll(instanceBEDs);
         }
      }
      updateBusinessEntityDefinitions(bedsToUpdate);
   }

   public void createVerticalAppWeight (VerticalAppWeight verticalAppWeight) throws KDXException {
      getKdxDataAccessManager().createVerticalAppWeight(verticalAppWeight);
   }

   public IKDXCloudDataAccessManager getKdxCloudDataAccessManager () {
      return kdxCloudDataAccessManager;
   }

   public void setKdxCloudDataAccessManager (IKDXCloudDataAccessManager kdxCloudDataAccessManager) {
      this.kdxCloudDataAccessManager = kdxCloudDataAccessManager;
   }

   public IBusinessEntityMaintenanceService getBusinessEntityMaintenanceService () {
      return businessEntityMaintenanceService;
   }

   public void setBusinessEntityMaintenanceService (IBusinessEntityMaintenanceService businessEntityMaintenanceService) {
      this.businessEntityMaintenanceService = businessEntityMaintenanceService;
   }

   /**
    * ***************************** NLPV4 Code ends here **************************************
    */

   public void createBusinessEntityVariation (BusinessEntityVariation businessEntityVariation) throws KDXException {
      getKdxDataAccessManager().createBusinessEntityVariation(businessEntityVariation);
   }

   public void updateBusinessEntityVariation (BusinessEntityVariation businessEntityVariation) throws KDXException {
      getKdxDataAccessManager().updateBusinessEntityVariation(businessEntityVariation);
   }

   public void createRISharedUserModelMapping (RISharedUserModelMapping riSharedUserModelMapping) throws KDXException {
      getKdxDataAccessManager().createRISharedUserModelMapping(riSharedUserModelMapping);
   }

   public void deleteBusinessEntityVariationsByBedId (Long businessEntityId) throws KDXException {
      getKdxDataAccessManager().deleteBusinessEntityVariationsByBedId(businessEntityId);

   }

   public void deleteRIOntoTerm (RIOntoTerm riOntoTerm) throws KDXException {
      getKdxDataAccessManager().deleteRIOntoTerm(riOntoTerm);
   }

   public void createBusinessEntityVariations (List<BusinessEntityVariation> businessEntityVariations)
            throws KDXException {
      getKdxDataAccessManager().createBusinessEntityVariations(businessEntityVariations);
   }

   public void deleteInstanceRIOntoTermsByConceptBEDId (Long modelGroupId, Long conceptBEDId) throws KDXException {
      getKdxDataAccessManager().deleteInstanceRIOntoTermsByConceptBEDId(modelGroupId, conceptBEDId);
   }

   public void deleteRIOntoTermsByProfileBEDId (Long modelId, Long profileBEDId) throws KDXException {
      getKdxDataAccessManager().deleteRIOntoTermsByProfileBEDId(modelId, profileBEDId);
   }

   public void updateInstanceRIOntoTermsWithConceptInfo (Long modelGroupId, Long conceptBEDId, String conceptName,
            Long typeBEDId, String typeName) throws KDXException {
      getKdxDataAccessManager().updateInstanceRIOntoTermsWithConceptInfo(modelGroupId, conceptBEDId, conceptName,
               typeBEDId, typeName);
   }

   public void createRIOntoTerm (RIOntoTerm riOnTOTerm) throws KDXException {
      riOnTOTerm.setWord(riOnTOTerm.getWord().toLowerCase());
      getKdxDataAccessManager().createRIOntoTerm(riOnTOTerm);
   }

   @Override
   public void deleteInstanceVariationsForConcept (Long modelId, Long conceptId) throws KDXException {
      getKdxDataAccessManager().deleteInstanceVariationsForConcept(modelId, conceptId);

   }

   @Override
   public void createHierarchy (Long modelId, Hierarchy hierarchy) throws KDXException {
      boolean hierarchyExists = getSwiValidator().hierarchyExists(modelId, hierarchy);
      if (hierarchyExists) {
         throw new KDXException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, "Hierarchy with name [" + hierarchy.getName()
                  + "] or with same definition already exists ");
      }
      getKdxDataAccessManager().createHierarchy(hierarchy);
   }

   @Override
   public void updateHierarchy (Long modelId, Hierarchy hierarchy) throws KDXException {
      boolean hierarchyExists = getSwiValidator().hierarchyExists(modelId, hierarchy);
      if (hierarchyExists) {
         throw new KDXException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, "Hierarchy with name [" + hierarchy.getName()
                  + "] or with same definition already exists ");
      }
      getKdxDataAccessManager().updateHierarchy(hierarchy);
   }

   @Override
   public void deleteHierarchy (Hierarchy hierarchy) throws KDXException {
      getKdxDataAccessManager().deleteHierarchy(hierarchy);
   }

   public IKDXDataAccessManager getKdxDataAccessManager () {
      return kdxDataAccessManager;
   }

   public void setKdxDataAccessManager (IKDXDataAccessManager kdxDataAccessManager) {
      this.kdxDataAccessManager = kdxDataAccessManager;
   }

   public IKDXModelDataAccessManager getKdxModelDataAccessManager () {
      return kdxModelDataAccessManager;
   }

   public void setKdxModelDataAccessManager (IKDXModelDataAccessManager kdxModelDataAccessManager) {
      this.kdxModelDataAccessManager = kdxModelDataAccessManager;
   }

   public ISWIValidator getSwiValidator () {
      return swiValidator;
   }

   public void setSwiValidator (ISWIValidator swiValidator) {
      this.swiValidator = swiValidator;
   }
}