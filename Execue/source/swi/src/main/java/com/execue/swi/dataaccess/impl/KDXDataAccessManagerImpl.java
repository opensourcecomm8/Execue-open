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


package com.execue.swi.dataaccess.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.ApplicationInfo;
import com.execue.core.common.bean.ContentCleanupPattern;
import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.ApplicationDetail;
import com.execue.core.common.bean.entity.ApplicationExample;
import com.execue.core.common.bean.entity.ApplicationOperation;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityMaintenanceInfo;
import com.execue.core.common.bean.entity.BusinessEntityVariation;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.DefaultDynamicValue;
import com.execue.core.common.bean.entity.EntityBehavior;
import com.execue.core.common.bean.entity.EntityDetailType;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Hierarchy;
import com.execue.core.common.bean.entity.HierarchyDetail;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.ModelGroupMapping;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.PossibleAttribute;
import com.execue.core.common.bean.entity.PossibleDetailType;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.entity.RISharedUserModelMapping;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.SFLTermToken;
import com.execue.core.common.bean.entity.SecondaryWord;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.bean.entity.UnstructuredApplicationDetail;
import com.execue.core.common.bean.entity.Vertical;
import com.execue.core.common.bean.entity.VerticalAppExample;
import com.execue.core.common.bean.entity.VerticalAppWeight;
import com.execue.core.common.bean.entity.VerticalEntityBasedRedirection;
import com.execue.core.common.bean.entity.wrapper.AppDashBoardInfo;
import com.execue.core.common.bean.governor.BusinessEntityInfo;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.nlp.SFLInfo;
import com.execue.core.common.bean.swi.PopularityBusinessEntityDefinitionInfo;
import com.execue.core.common.type.AppOperationType;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.EntityType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.OperationType;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.dataaccess.IKDXDataAccessManager;
import com.execue.swi.dataaccess.KDXDAOComponents;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;

public class KDXDataAccessManagerImpl extends KDXDAOComponents implements IKDXDataAccessManager {

   public <BusinessObject extends Serializable> BusinessObject getById (Long id, Class<BusinessObject> clazz)
            throws KDXException {
      try {
         return getBusinessEntityDefinitionDAO().getById(id, clazz);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<? extends Serializable> getByIds (List<Long> ids, Class<?> clazz) throws KDXException {
      try {
         return getBusinessEntityDefinitionDAO().getByIds(ids, clazz);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Stat> getAllStats () throws KDXException {
      try {
         return getStatDAO().getAllStats();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Concept> getConcepts (Long modelId) throws KDXException {
      try {
         return getConceptDAO().getConcepts(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.CONCEPT_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Instance> getInstances (Long modelId, Long conceptId) throws KDXException {
      try {
         return getInstanceDAO().getInstances(modelId, conceptId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.INSTANCE_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Instance> getTypeInstances (Long modelId, Long typeId) throws KDXException {
      try {
         return getInstanceDAO().getTypeInstances(modelId, typeId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.INSTANCE_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Instance> getInstancesByPage (Long modelId, Long conceptId, Page page) throws KDXException {
      try {
         return getInstanceDAO().getInstancesByPage(modelId, conceptId, page);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.INSTANCE_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#getInstancesCount(java.lang.Long, java.lang.Long)
    */
   public long getInstancesCount (Long modelId, Long conceptId) throws KDXException {
      try {
         return getInstanceDAO().getInstanceCount(modelId, conceptId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.INSTANCE_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public void createModel (Model model) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().create(model);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.MODEL_CREATION_FAILED, dataAccessException);
      }
   }

   public void createModelGroup (ModelGroup modelGroup) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().create(modelGroup);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void createModelGroupMapping (ModelGroupMapping modelGroupMapping) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().create(modelGroupMapping);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public <BusinessObject extends Serializable> BusinessObject getByField (String fieldValue, String fieldName,
            Class<BusinessObject> clazz) throws SWIException {
      try {
         return getBusinessEntityDefinitionDAO().getByField(fieldValue, fieldName, clazz);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Stat getStatByName (String name) throws KDXException {
      try {
         return getBusinessEntityDefinitionDAO().getByName(name, Stat.class);
      } catch (DataAccessException dae) {
         throw new KDXException(dae.getCode(), dae);
      }
   }

   public Model getModelByUserModelGroupId (Long userModelGroupId) throws KDXException {
      try {
         return getModelDAO().getModelByUserModelGroupId(userModelGroupId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getBusinessEntityDefinitionByIds (Long modelId, Long conceptId, Long instanceId)
            throws KDXException {
      try {
         // TODO: -JVK- write DAO Impl for this method
         return getBusinessEntityDefinitionDAO().getBusinessEntityDefinitionByIds(modelId, conceptId, instanceId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getBusinessEntityDefinitionByTypeIds (Long modelId, Long typeId, Long instanceId)
            throws KDXException {
      try {
         return getBusinessEntityDefinitionDAO().getBusinessEntityDefinitionByTypeIds(modelId, typeId, instanceId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getBusinessEntityDefinitionByIdsWithTypeLoaded (Long modelId, Long conceptId,
            Long instanceId) throws KDXException {
      try {
         // TODO: -JVK- write DAO Impl for this method
         return getBusinessEntityDefinitionDAO().getBusinessEntityDefinitionByIds(modelId, conceptId, instanceId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getBusinessEntityDefinitionByNames (String businessName, String conceptName,
            String instanceName) throws KDXException {
      try {
         // TODO: -JVK- write DAO Impl for this method
         return getBusinessEntityDefinitionDAO().getBusinessEntityDefinitionByNames(businessName, conceptName,
                  instanceName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition createType (Long modelId, Type type, boolean isRealizedType, Long knowledgeId)
            throws KDXException {
      ModelGroup modelGroup = getPrimaryGroup(modelId);
      BusinessEntityDefinition businessEntityDefinition = new BusinessEntityDefinition();
      businessEntityDefinition.setModelGroup(modelGroup);
      businessEntityDefinition.setType(type);
      businessEntityDefinition.setEntityType(isRealizedType ? BusinessEntityType.REALIZED_TYPE
               : BusinessEntityType.TYPE);

      // If is its realized type then create the concept too
      if (isRealizedType) {
         Concept concept = new Concept();
         concept.setName(type.getName());
         concept.setDisplayName(type.getDisplayName());
         businessEntityDefinition.setConcept(concept);
      }

      try {
         assignKnowledgeId(businessEntityDefinition, knowledgeId);
         getBusinessEntityDefinitionDAO().create(businessEntityDefinition);
         return businessEntityDefinition;
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.TYPE_CREATION_FAILED, dataAccessException);
      }

   }

   public BusinessEntityDefinition createConcept (Long modelId, Type type, Concept concept, Long knowledgeId)
            throws KDXException {
      ModelGroup modelGroup = getPrimaryGroup(modelId);
      BusinessEntityDefinition businessEntityDefinition = new BusinessEntityDefinition();
      businessEntityDefinition.setModelGroup(modelGroup);
      businessEntityDefinition.setType(type);
      businessEntityDefinition.setConcept(concept);
      businessEntityDefinition.setEntityType(BusinessEntityType.CONCEPT);
      try {
         assignKnowledgeId(businessEntityDefinition, knowledgeId);
         getBusinessEntityDefinitionDAO().create(businessEntityDefinition);
         return businessEntityDefinition;
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.CONCEPT_CREATION_FAILED, dataAccessException);
      }
   }

   public BusinessEntityDefinition createInstance (Long modelId, BusinessEntityDefinition conceptBED,
            Instance instance, Long knowledgeId) throws KDXException {
      BusinessEntityDefinition businessEntityDefinition = new BusinessEntityDefinition();
      ModelGroup modelGroup = getPrimaryGroup(modelId);
      businessEntityDefinition.setModelGroup(modelGroup);
      businessEntityDefinition.setConcept(conceptBED.getConcept());
      businessEntityDefinition.setInstance(instance);
      businessEntityDefinition.setType(conceptBED.getType());
      businessEntityDefinition.setEntityType(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);
      try {
         assignKnowledgeId(businessEntityDefinition, knowledgeId);
         getBusinessEntityDefinitionDAO().create(businessEntityDefinition);
         return businessEntityDefinition;
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.INSTANCE_CREATION_FAILED, dataAccessException);
      }
   }

   public BusinessEntityDefinition createTypeInstance (Long modelId, BusinessEntityDefinition typeBED,
            Instance instance, Long knowledgeId) throws KDXException {
      BusinessEntityDefinition businessEntityDefinition = new BusinessEntityDefinition();
      ModelGroup modelGroup = getPrimaryGroup(modelId);
      businessEntityDefinition.setModelGroup(modelGroup);
      businessEntityDefinition.setType(typeBED.getType());
      businessEntityDefinition.setInstance(instance);
      businessEntityDefinition.setEntityType(BusinessEntityType.TYPE_LOOKUP_INSTANCE);
      try {
         assignKnowledgeId(businessEntityDefinition, knowledgeId);
         getBusinessEntityDefinitionDAO().create(businessEntityDefinition);
         return businessEntityDefinition;
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.INSTANCE_CREATION_FAILED, dataAccessException);
      }
   }

   public BusinessEntityDefinition updateConcept (Long modelId, Cloud cloud, Type type, Concept concept,
            BusinessEntityDefinition conceptBed) throws KDXException {
      try {

         getBusinessEntityDefinitionDAO().update(concept);
         // TODO: NK: update cloud and type information

         return conceptBed;
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.CONCEPT_UPDATE_FAILED, dataAccessException);
      }
   }

   public BusinessEntityDefinition updateInstance (Long modelId, Long conceptId, Instance instance) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().update(instance);
         return getBusinessEntityDefinitionDAO().getBusinessEntityDefinitionByIds(modelId, conceptId, instance.getId());

      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.INSTANCE_UPDATE_FAILED, dataAccessException);
      }
   }

   public void updateRelation (Long modelId, Relation relation) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().update(relation);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.INSTANCE_UPDATE_FAILED, dataAccessException);
      }
   }

   public List<Concept> getConceptsBySearchString (Long modelId, String searchString) throws KDXException {
      try {
         return getConceptDAO().getConceptsBySearchString(modelId, searchString);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Instance> getInstancesOfConceptBySearchString (Long modelId, Long conceptId, String searchString)
            throws KDXException {
      try {
         return getInstanceDAO().getInstancesOfConceptBySearchString(modelId, conceptId, searchString);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Concept> getConceptsForAsset (Long assetId) throws KDXException {
      try {
         return getConceptDAO().getConceptsForAsset(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Concept> getConceptsForAssetMetaInfo (Long assetId) throws KDXException {
      try {
         return getConceptDAO().getConceptsForAssetMetaInfo(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Concept> getMeasureConceptsForAsset (Long assetId) throws KDXException {
      try {
         return getConceptDAO().getMeasureConceptsForAsset(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Integer getMeasureConceptsCountForAsset (Long assetId) throws KDXException {
      try {
         return getConceptDAO().getMeasureConceptsCountForAsset(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getBEDByInstanceDisplayName (String modelName, String conceptName,
            String instanceDisplayName) throws KDXException {
      try {
         return getInstanceDAO().getBEDByInstanceDisplayName(modelName, conceptName, instanceDisplayName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<SecondaryWord> getAllSecondaryWords () throws KDXException {
      try {
         return getSflWordDAO().getAllSecondaryWords();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<SecondaryWord> getAllSecondaryWordsForModel (Long modelId) throws KDXException {
      try {
         return getSflWordDAO().getAllSecondaryWordsForModel(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<RIParallelWord> getParallelWordsByLookupWord (String word) throws KDXException {
      try {
         return getParallelWordDAO().getParallelWordsByLookupWord(word);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#getParallelWordsByLookupWords(java.util.List, java.util.Set)
    */
   public List<RIParallelWord> getParallelWordsByLookupWords (List<String> words, Set<Long> modelGroupIds)
            throws KDXException {
      try {
         return getParallelWordDAO().getParallelWordsByLookupWords(words, modelGroupIds);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#getParallelWordsByLookupWords(java.util.List)
    */
   public List<RIParallelWord> getParallelWordsByLookupWords (List<String> words) throws KDXException {
      try {
         return getParallelWordDAO().getParallelWordsByLookupWords(words);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<RIOntoTerm> getTermsByLookupWord (String word) throws KDXException {
      try {
         return getOntoReverseIndexDAO().getTermsByLookupWord(word);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<RIOntoTerm> getTermsByLookupWordAndEntityType (String word, BusinessEntityType entityType)
            throws KDXException {
      try {
         return getOntoReverseIndexDAO().getTermsByLookupWordAndEntityType(word, entityType);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#getTermsByLookupWords(java.util.List)
    */
   public List<RIOntoTerm> getTermsByLookupWords (List<String> words, boolean skipLocationTypeRecognition,
            List<Long> locationChildTypeBedIds) throws KDXException {
      try {
         return getOntoReverseIndexDAO().getTermsByLookupWords(words, skipLocationTypeRecognition,
                  locationChildTypeBedIds);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#getTermsByLookupWords(java.util.List, java.util.List)
    */
   public List<RIOntoTerm> getTermsByLookupWords (List<String> words, List<Long> modelGroupIds,
            boolean skipLocationTypeRecognition, List<Long> locationChildTypeBedIds) throws KDXException {
      try {
         return getOntoReverseIndexDAO().getTermsByLookupWords(words, modelGroupIds, skipLocationTypeRecognition,
                  locationChildTypeBedIds);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<RIOntoTerm> getTermsByLookupWordsAndEntityType (List<String> words, BusinessEntityType entityType)
            throws KDXException {
      try {
         return getOntoReverseIndexDAO().getTermsByLookupWordsAndEntityType(words, entityType);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Map<Long, Map<String, SecondaryWord>> getAllSecondaryWordsMap () throws KDXException {
      List<SecondaryWord> secondaryWords = getAllSecondaryWords();
      Map<Long, Map<String, SecondaryWord>> secondaryWordsMap = new HashMap<Long, Map<String, SecondaryWord>>(1);
      if (CollectionUtils.isEmpty(secondaryWords)) {
         return secondaryWordsMap;
      }
      for (SecondaryWord secondaryWord : secondaryWords) {
         Long modelGroupId = secondaryWord.getModelGroup().getId();
         Map<String, SecondaryWord> map = secondaryWordsMap.get(modelGroupId);
         if (map == null) {
            map = new HashMap<String, SecondaryWord>(1);
            secondaryWordsMap.put(modelGroupId, map);
         }
         map.put(secondaryWord.getWord().toLowerCase(), secondaryWord);
      }
      return secondaryWordsMap;
   }

   public List<BusinessEntityDefinition> getRegularExpressionBasedInstances () throws KDXException {
      try {
         return getInstanceDAO().getRegularExpressionBasedInstances();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<SFLTermToken> getSFLTermTokensByLookupWord (String word) throws KDXException {
      try {
         return getSflWordDAO().getSFLTermTokensByLookupWord(word);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<SFLTermToken> getSFLTermTokensByLookupWords (List<String> words) throws KDXException {
      try {
         return getSflWordDAO().getSFLTermTokensByLookupWords(words);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Stat getStatByBusinessEntityId (Long businessEntityDefinitionId) throws KDXException {
      try {
         return getStatDAO().getStatByBusinessEntityId(businessEntityDefinitionId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getRelationBEDByName (Long modelId, String relationName) throws KDXException {
      try {
         return getRelationDAO().getBEDByRelationName(modelId, relationName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getRelationBEDByNameIncludingBaseGroup (Long modelId, String relationName)
            throws KDXException {
      try {
         return getRelationDAO().getRelationBEDByNameIncludingBaseGroup(modelId, relationName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getConceptBEDByName (Long modelId, String conceptName) throws KDXException {
      try {
         return getConceptDAO().getBEDByConceptName(modelId, conceptName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getConceptBEDByDisplayName (Long modelId, String conceptDisplayName)
            throws KDXException {
      try {
         return getConceptDAO().getBEDByConceptDisplayName(modelId, conceptDisplayName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getInstanceBEDByName (Long modelId, String conceptName, String instanceName)
            throws KDXException {
      try {
         return getInstanceDAO().getBEDByInstanceName(modelId, conceptName, instanceName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getInstanceBEDByDisplayName (Long modelId, String conceptName,
            String instanceDisplayName) throws KDXException {
      try {
         return getInstanceDAO().getBEDByInstanceDisplayName(modelId, conceptName, instanceDisplayName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<BusinessEntityDefinition> getAllAbstractConcepts () throws KDXException {
      try {
         return getEntityBehaviorDAO().getAllAbstractConcepts();
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<BusinessEntityDefinition> getAllAtributeConcepts () throws KDXException {
      try {
         return getEntityBehaviorDAO().getAllAtributeConcepts();
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<BusinessEntityDefinition> getAllComparativeConcepts () throws KDXException {
      try {
         return getEntityBehaviorDAO().getAllComparativeConcepts();
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<BusinessEntityDefinition> getAllDistributionConcepts () throws KDXException {
      try {
         return getEntityBehaviorDAO().getAllDistributionConcepts();
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<BusinessEntityDefinition> getAllEnumerationConcepts () throws KDXException {
      try {
         return getEntityBehaviorDAO().getAllEnumerationConcepts();
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<BusinessEntityDefinition> getAllQuantitativeConcepts () throws KDXException {
      try {
         return getEntityBehaviorDAO().getAllQuantitativeConcepts();
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public BusinessEntityType getBusinessEntityType (Long businessEntityDefinitionId) throws KDXException {
      try {
         return getBusinessEntityDefinitionDAO().getBusinessEntityType(businessEntityDefinitionId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public boolean isAbstractConcept (Long bedID) throws KDXException {
      try {
         return getBooleanValueForCheckType(getEntityBehaviorDAO().isAbstractConcept(bedID));
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public boolean isAttributeConcept (Long bedID) throws KDXException {
      try {
         return getBooleanValueForCheckType(getEntityBehaviorDAO().isAttributeConcept(bedID));
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#hasBehavior(java.lang.Long,
    *      com.execue.core.common.type.BehaviorType)
    */
   public boolean hasBehavior (Long bedID, BehaviorType behaviorType) throws KDXException {
      try {
         return getEntityBehaviorDAO().hasBehavior(bedID, behaviorType);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public boolean isGrainConcept (Long bedID) throws KDXException {
      try {
         return getBooleanValueForCheckType(getEntityBehaviorDAO().isGrainConcept(bedID));
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public boolean isComparativeConcept (Long bedID) throws KDXException {
      try {
         return getBooleanValueForCheckType(getEntityBehaviorDAO().isComparativeConcept(bedID));
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public boolean isDistributionConcept (Long bedID) throws KDXException {
      try {
         return getBooleanValueForCheckType(getEntityBehaviorDAO().isDistributionConcept(bedID));
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public boolean isEnumerationConcept (Long bedID) throws KDXException {
      try {
         return getBooleanValueForCheckType(getEntityBehaviorDAO().isEnumerationConcept(bedID));
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public boolean isQuantitativeConcept (Long bedID) throws KDXException {
      try {
         return getBooleanValueForCheckType(getEntityBehaviorDAO().isQuantitativeConcept(bedID));
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<PathDefinition> getConceptToAttributePathDefinitionDetails (Long sourceBEDId, Long destBEDId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getConceptToAttributePathDefinition(sourceBEDId, destBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<PathDefinition> getConceptToConceptPathDefinitionDetails (Long sourceBEDId, Long destBEDId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getConceptToConceptPathDefinition(sourceBEDId, destBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<PathDefinition> getRelationToRelationPathDefinitionDetails (Long sourceBEDId, Long destBEDId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getRelationToRelationPathDefinition(sourceBEDId, destBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<PathDefinition> getPathDefinitions (Long sourceBEDId, Long destBEDId) throws KDXException {
      try {
         return getPathDefinitionDAO().getPathDefinitions(sourceBEDId, destBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<PathDefinition> getParentPathDefinitions (Long sourceBEDId, Long destBEDId) throws KDXException {
      try {
         return getPathDefinitionDAO().getParentPathDefinitions(sourceBEDId, destBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<PathDefinition> getChildPathDefinitionsForSource (Long sourceBEDId) throws KDXException {
      try {
         return getPathDefinitionDAO().getChildPathDefinitionsForSource(sourceBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<PathDefinition> getChildPathDefinitionsForSourceForModel (Long modelId, Long sourceBEDId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getChildPathDefinitionsForSourceForModel(modelId, sourceBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<PathDefinition> getPathDefinitionDetailsLessThanLength (Long sourceBEDId, Long destBEDId, int length)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getPathDefinitionDetailsLessThanLength(sourceBEDId, destBEDId, length);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public EntityTripleDefinition getEntityTriple (Long sourceBEDId, Long relationBEDId, Long destBEDId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getEntityTriple(sourceBEDId, relationBEDId, destBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<EntityTripleDefinition> getEntityTriplesForDestination (Long destBEDId) throws KDXException {
      try {
         return getPathDefinitionDAO().getEntityTriplesForDestination(destBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<EntityTripleDefinition> getEntityTriplesForDestinationAndTripleType (Long destBEDId,
            EntityTripleDefinitionType tripleType) throws KDXException {
      try {
         return getPathDefinitionDAO().getEntityTriplesForDestinationAndTripleType(destBEDId, tripleType);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<EntityTripleDefinition> getEntityTriplesForDestinationAndRelation (Long relationBEDId, Long destBEDId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getEntityTriplesForDestinationAndRelation(relationBEDId, destBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<EntityTripleDefinition> getEntityTriplesForRelation (Long relBEDId) throws KDXException {
      try {
         return getPathDefinitionDAO().getEntityTriplesForRelation(relBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<EntityTripleDefinition> getEntityTriplesForSource (Long sourceBEDId) throws KDXException {
      try {
         return getPathDefinitionDAO().getEntityTriplesForSource(sourceBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<EntityTripleDefinition> getEntityTriplesForSourceAndTripleType (Long sourceBEDId,
            EntityTripleDefinitionType tripleType) throws KDXException {
      try {
         return getPathDefinitionDAO().getEntityTriplesForSourceAndTripleType(sourceBEDId, tripleType);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<EntityTripleDefinition> getEntityTriplesForSourceAndRelation (Long sourceBEDId, Long relationBEDId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getEntityTriplesForSourceAndRelation(sourceBEDId, relationBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   private boolean getBooleanValueForCheckType (CheckType ct) {
      if (ct == CheckType.YES) {
         return true;
      }
      return false;
   }

   public Set<Long> getRelatedBEDIdsByInstanceConceptBEDIds (List<Long> instanceBEDIds, List<Long> conceptBEDIds)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getRelatedBEDIdsByInstanceConceptBEDIds(instanceBEDIds, conceptBEDIds);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public Set<Long> getRelatedBEDIdsByConceptBEDIds (List<Long> conceptBEDIds) throws DataAccessException {
      return getPathDefinitionDAO().getRelatedBEDIdsByConceptBEDIds(conceptBEDIds);
   }

   public Integer updateBusinessEntitiesPopularity (List<BusinessEntityTerm> businessEntityTerms) throws KDXException {
      Integer rowsUpdated = 0;
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(businessEntityTerms)) {
            List<Long> businessEntityTermIds = new ArrayList<Long>();
            for (BusinessEntityTerm businessEntityTerm : businessEntityTerms) {
               businessEntityTermIds.add(businessEntityTerm.getBusinessEntityDefinitionId());
            }
            rowsUpdated = getPopularityHitDAO().updateBusinessEntitiesPopularity(businessEntityTermIds);
         }
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
      return rowsUpdated;
   }

   public List<PathDefinition> getAllParentPathDefinitionsForDestination (Long destinationBEDId) throws KDXException {
      try {
         return getPathDefinitionDAO().getAllParentPathDefinitionsForDestination(destinationBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<PathDefinition> getImmediateParentPathDefinitionsForDestination (Long destinationBEDId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getDirectParentPathDefinitionsForDestination(destinationBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<PathDefinition> getPathsBetweenMultipleSourcesToSingleDestination (List<Long> sourceBEDIDs,
            Long destinationBEDId) throws KDXException {
      try {
         return getPathDefinitionDAO()
                  .getPathsBetweenMultipleSourcesToSingleDestination(sourceBEDIDs, destinationBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public BusinessEntityDefinition createRelation (Long modelId, Relation relation, Type type, Long knowledgeId)
            throws KDXException {
      ModelGroup modelGroup = getPrimaryGroup(modelId);
      BusinessEntityDefinition businessEntityDefinition = new BusinessEntityDefinition();
      businessEntityDefinition.setModelGroup(modelGroup);
      businessEntityDefinition.setRelation(relation);
      businessEntityDefinition.setEntityType(BusinessEntityType.RELATION);
      businessEntityDefinition.setType(type);
      try {
         assignKnowledgeId(businessEntityDefinition, knowledgeId);
         getBusinessEntityDefinitionDAO().create(businessEntityDefinition);
         return businessEntityDefinition;
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.RELATION_CREATION_FAILED, dataAccessException);
      }
   }

   public List<Instance> getInstancesByDisplayNameAcrossAllConcepts (Long modelId, String instanceDisplayName)
            throws KDXException {
      try {
         return getInstanceDAO().getInstancesByDisplayNameAcrossAllConcepts(modelId, instanceDisplayName);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public void createRIOntoTerm (RIOntoTerm riOnTOTerm) throws KDXException {
      try {
         getOntoReverseIndexDAO().create(riOnTOTerm);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public Path getPathByETD (EntityTripleDefinition etd) throws KDXException {
      try {
         return getPathDefinitionDAO().getPathByETD(etd);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public Instance getLatestInstanceInserted (Long modelId, Long conceptId) throws KDXException {
      Instance latestInstance = null;
      try {
         List<Instance> instances = getInstances(modelId, conceptId);
         if (ExecueCoreUtil.isCollectionNotEmpty(instances)) {
            latestInstance = getInstanceDAO().getLatestInstanceInserted(modelId, conceptId);
         }
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
      return latestInstance;
   }

   public void insertOrderForSFLTermTokens () throws KDXException {
      try {
         List<SFLTerm> sflTerms = getAllSFLTerms();
         for (SFLTerm sflTerm : sflTerms) {
            String termWord = sflTerm.getBusinessTerm();
            String[] tokens = termWord.split(" ");
            Set<SFLTermToken> termTokens = sflTerm.getSflTermTokens();
            int i = 0;
            for (String token : tokens) {
               for (SFLTermToken termToken : termTokens) {
                  if (termToken.getBusinessTermToken().equalsIgnoreCase(token)) {
                     termToken.setOrder(i);
                     termTokens.remove(termToken);
                     getSflWordDAO().update(termToken);
                     break;
                  }
               }
               i++;
            }
         }
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public void updateSFLTermToken (SFLTermToken termToken) throws KDXException {
      try {
         getSflWordDAO().update(termToken);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<SFLTerm> getAllSFLTerms () throws KDXException {
      try {
         // TODO: -JVK- modify the DAO Impl for this method
         return getSflWordDAO().getAllSFLTerms();
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<SFLTerm> getSFLTerms (String sflTerm) throws KDXException {
      try {
         return getSflWordDAO().getSFLTerms(sflTerm);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public void updateSFLTermTokens (List<SFLTermToken> termTokens) throws KDXException {
      try {
         getSflWordDAO().updateAll(termTokens);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<SFLTermToken> getSFLTermTokensByTermId (Long sflTermId) throws KDXException {
      try {
         return getSflWordDAO().getSFLTermTokensByTermId(sflTermId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public SFLTerm getSFLTermByWord (String word) throws KDXException {
      try {
         return getSflWordDAO().getSFLTermByWord(word);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<SFLInfo> getSFLInfoForWords (List<String> words, Set<Long> modelGroupIds, boolean groupConcatDBSupported)
            throws KDXException {
      try {
         return getSflWordDAO().getSFLInfoForWords(words, modelGroupIds, groupConcatDBSupported);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public RIParallelWord getParallelWordById (Long id) throws KDXException {
      return getById(id, RIParallelWord.class);

   }

   public void updateRIParallelWord (RIParallelWord parallelWord) throws KDXException {
      try {
         getParallelWordDAO().update(parallelWord);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<PathDefinition> getParentPathsForDestList (Long sourceBED, List<Long> destinationBEDs)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getParentPathsForDestList(sourceBED, destinationBEDs);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<BusinessEntityInfo> getAllConceptProfileBusinessEntities (Long modelId) throws KDXException {
      try {
         return getProfileDAO().getAllConceptProfileBusinessEntities(modelId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<BusinessEntityInfo> getAllConceptsBusinessEntities (Long modelId) throws KDXException {
      try {
         return getConceptDAO().getAllConceptsBusinessEntities(modelId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<BusinessEntityInfo> getAllInstanceBusinessEntities (Long modelId) throws KDXException {
      try {
         return getInstanceDAO().getAllInstanceBusinessEntities(modelId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<BusinessEntityInfo> getAllInstanceProfileBusinessEntities (Long modelId) throws KDXException {
      try {
         return getProfileDAO().getAllInstanceProfileBusinessEntities(modelId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<BusinessEntityInfo> getAllRelationBusinessEntities (Long modelId) throws KDXException {
      try {
         return getRelationDAO().getAllRelationBusinessEntities(modelId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public void createSFLTerm (SFLTerm sflTerm) throws KDXException {
      try {
         for (SFLTermToken sfTermToken : sflTerm.getSflTermTokens()) {
            sfTermToken.setSflTerm(sflTerm);
         }
         getSflWordDAO().create(sflTerm);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public void createSecondaryWords (List<SecondaryWord> secondaryWords) throws KDXException {
      try {
         getSflWordDAO().createAll(secondaryWords);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public void deleteSFLTerm (SFLTerm sflTerm) throws KDXException {
      try {
         getSflWordDAO().delete(sflTerm);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<ApplicationInfo> getAllApplications () throws KDXException {
      try {
         return getApplicationDAO().getAllApplications();
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<Model> getModelsByApplicationId (Long applicationId) throws KDXException {
      try {
         return getModelDAO().getModelsByApplicationId(applicationId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public Application createApplication (Application application) throws KDXException {
      try {
         application = getBusinessEntityDefinitionDAO().create(application);
         ApplicationOperation appOperation = new ApplicationOperation();
         appOperation.setApplicationId(application.getId());
         getBusinessEntityDefinitionDAO().create(appOperation);
         return application;
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public ApplicationDetail createApplicationImage (ApplicationDetail applicationImage) throws KDXException {

      try {
         return getBusinessEntityDefinitionDAO().create(applicationImage);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.code, dataAccessException.getMessage(), dataAccessException);
      }
   }

   public void updateApplicationImage (ApplicationDetail applicationImage) throws KDXException {

      try {
         getBusinessEntityDefinitionDAO().update(applicationImage);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.code, dataAccessException.getMessage(), dataAccessException);
      }
   }

   public void deleteApplicationImage (ApplicationDetail applicationDetail) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().delete(applicationDetail);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.code, dataAccessException.getMessage(), dataAccessException);
      }
   }

   public ApplicationDetail getImageByApplicationId (Long applicationId) throws KDXException {
      try {
         return getApplicationDAO().getImageByApplicationId(applicationId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.code, dataAccessException.getMessage(), dataAccessException);
      }
   }

   public ApplicationDetail getImageByApplicationImageId (Long applicationId, Long imageId) throws KDXException {
      try {
         return getApplicationDAO().getImageByApplicationImageId(applicationId, imageId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.code, dataAccessException.getMessage(), dataAccessException);
      }
   }

   public void updateApplication (Application application) throws KDXException {
      try {
         List<Model> models = getModelsByApplicationId(application.getId());
         application.setModels(new HashSet<Model>(models));

         // NOTE: -RG- applicationKey should never be over-ridden
         // Get applicationKey it from db and then set it back to the object that is being updated
         application.setApplicationKey(getApplicationById(application.getId()).getApplicationKey());

         getBusinessEntityDefinitionDAO().update(application);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public void updateApplicationOperationDetails (Long applicationId, AppOperationType operationType,
            Long jobRequestId, JobStatus operationStatus) throws KDXException {
      try {
         ApplicationOperation appOperation = getBusinessEntityDefinitionDAO().getByField(applicationId,
                  "applicationId", ApplicationOperation.class);
         appOperation.setOperationType(operationType);
         appOperation.setJobRequestId(jobRequestId);
         appOperation.setOperationStatus(operationStatus);
         getBusinessEntityDefinitionDAO().update(appOperation);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<Application> getAllExistingApplications () throws KDXException {
      try {
         return getApplicationDAO().getAllExistingApplications();
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<Application> getAllActiveApplications () throws KDXException {
      try {
         return getApplicationDAO().getAllActiveApplications();
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<Application> getAllActiveStructuredApplications () throws KDXException {
      try {
         return getApplicationDAO().getAllActiveStructuredApplications();
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public List<Application> getAllApplicationsOrderedByName () throws KDXException {
      try {
         return getApplicationDAO().getAllApplicationsOrderedByName();
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public ModelGroup getPrimaryGroup (Long modelId) throws KDXException {
      try {
         return getModelDAO().getPrimaryGroup(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public Application getApplicationById (Long applicationId) throws KDXException {
      try {
         return getBusinessEntityDefinitionDAO().getById(applicationId, Application.class);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public BusinessEntityDefinition getBEDByMappingConceptName (Long modelId, Long mappingId, String conceptDisplayName)
            throws KDXException {

      try {
         return getMappingDAO().getBEDByMappingConceptName(modelId, mappingId, conceptDisplayName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void updateBusinessEntityDefinitions (List<BusinessEntityDefinition> businessEntityDefinitions)
            throws KDXException {
      try {
         for (BusinessEntityDefinition businessEntityDefinition : businessEntityDefinitions) {
            getBusinessEntityDefinitionDAO().update(businessEntityDefinition);
         }
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }

   }

   public List<SFLTerm> getSFLTermsForKeyWord (String keyWord) throws KDXException {
      try {
         return getSflWordDAO().getSFLTermsForKeyWord(keyWord);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e.getMessage(), e);
      }
   }

   public Type getTypeByName (String typeName) throws KDXException {
      try {
         return getTypeDAO().getTypeByName(typeName);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e.getMessage(), e);
      }
   }

   public Concept getConceptByName (Long modelId, String conceptName) throws KDXException {
      try {
         return getConceptDAO().getConceptByName(modelId, conceptName);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e.getMessage(), e);
      }
   }

   public Concept getConceptByDisplayName (Long modelId, String conceptDisplayName) throws KDXException {
      try {
         return getConceptDAO().getConceptByDisplayName(modelId, conceptDisplayName);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e.getMessage(), e);
      }
   }

   public Relation getRelationByName (Long modelId, String relationName, boolean includeBaseModelGroup)
            throws KDXException {
      try {
         return getRelationDAO().getRelationByName(modelId, relationName, includeBaseModelGroup);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e.getMessage(), e);
      }
   }

   public List<RIOntoTerm> getTermsByLookupWordAndEntityTypeInGroup (String word, BusinessEntityType entityType,
            Long modelGroupId) throws KDXException {
      try {
         return getOntoReverseIndexDAO().getTermsByLookupWordAndEntityTypeInGroup(word, entityType, modelGroupId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public boolean isApplicationExist (String applicationName) throws KDXException {
      try {
         return getApplicationDAO().isApplicationExist(applicationName);
      } catch (DataAccessException e) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   public boolean isModelExist (String modelName) throws KDXException {
      try {
         return getModelDAO().isModelExist(modelName);
      } catch (DataAccessException e) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   public boolean isCloudExist (String cloudName) throws KDXException {
      try {
         return getCloudDAO().isCloudExist(cloudName);
      } catch (DataAccessException e) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   public List<RIOntoTerm> getTermsByLookupWordsAndEntityTypeInGroup (List<String> words,
            BusinessEntityType entityType, Long modelGroupId) throws KDXException {
      try {
         return getOntoReverseIndexDAO().getTermsByLookupWordsAndEntityTypeInGroup(words, entityType, modelGroupId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Application> getApplications (Long userId) throws KDXException {
      try {
         return getApplicationDAO().getApplications(userId);
      } catch (DataAccessException e) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Instance> getInstancesForAssetMetaInfo (Long modelId, Long conceptId) throws KDXException {

      try {
         return getInstanceDAO().getInstancesForAssetMetaInfo(modelId, conceptId);
      } catch (DataAccessException e) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#getCentralConceptPathsForDestination(java.lang.Long)
    */
   public List<PathDefinition> getCentralConceptPathsForDestination (Long destinationBEDId) throws KDXException {
      try {
         return getPathDefinitionDAO().getCentralConceptPathsForDestination(destinationBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#getModelByGroupIds(java.util.Set)
    */
   public List<Model> getModelByGroupIds (Set<Long> groupIds) throws KDXException {
      try {
         return getModelDAO().getModelByGroupIds(groupIds);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   // /*
   // * (non-Javadoc)
   // *
   // * @see
   // com.execue.swi.dataaccess.IKDXDataAccessManager#deleteConceptAndRespectiveInstancesForModel(java.lang.Long)
   // */
   // public void deleteConceptAndRespectiveInstancesForModel (Long modelId)
   // throws KDXException {
   // try {
   // getParallelWordDAO().deleteKeyWordAndPWForModel(modelId);
   // getBusinessEntityDefinitionDAO().deleteConceptAndRespectiveInstancesForModel(modelId);
   // } catch (DataAccessException dataAccessException) {
   // throw new KDXException(dataAccessException.getCode(),
   // dataAccessException);
   // }
   //
   // }
   //
   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#getAllPathsForSourceAndDestination(java.util.List,
    *      java.util.List)
    */
   public List<PathDefinition> getAllNonParentPathsForSourceAndDestination (List<Long> sourceBeIds, List<Long> destBeIds)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getAllNonParentPathsForSourceAndDestination(sourceBeIds, destBeIds);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public void deleteConcepts (List<Concept> concepts) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().deleteConcepts(concepts);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public void deleteRelations (List<Relation> relations) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().deleteRelations(relations);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<Concept> getConcepts (List<ModelGroup> userModelGroups) throws KDXException {
      try {
         return getConceptDAO().getConcepts(userModelGroups);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<Relation> getRelations (List<ModelGroup> userModelGroups) throws KDXException {
      try {
         return getRelationDAO().getRelations(userModelGroups);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<Relation> getRelationsByModel (Long modelId) throws KDXException {
      try {
         return getRelationDAO().getRelationsByModel(modelId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<ModelGroupMapping> getPopulatedModelGroupMapping (Long modelId) throws KDXException {
      try {
         return getModelDAO().getPopulatedModelGroupMapping(modelId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public void cleanApplication (Application application) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().delete(application);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public void cleanModelGroupMappings (List<ModelGroupMapping> modelGroupMappings) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().deleteAll(modelGroupMappings);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public void cleanModelGroups (List<ModelGroup> userModelGroups) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().deleteAll(userModelGroups);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public void cleanModels (List<Model> models) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().deleteAll(models);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public void cleanRIOntoTerms (List<ModelGroup> userModelGroups) throws KDXException {
      try {
         getOntoReverseIndexDAO().cleanRIOntoTerms(userModelGroups);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void cleanDefaultDynamicValues (List<ModelGroup> userModelGroups) throws KDXException {
      try {
         getDefaultDynamicValueDAO().cleanDefaultDynamicValues(userModelGroups);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Long> getSFLTermIdsTobeDeleted () throws KDXException {
      List<Long> sflTermIds = null;
      try {
         sflTermIds = getSflWordDAO().getAllSFLTermIds();
         if (ExecueCoreUtil.isCollectionNotEmpty(sflTermIds)) {
            List<Long> riOntoMatchedSFLTermIds = getSflWordDAO().getRIOntoTermMatchedSFLTermIds();
            List<Long> parallelMatchedSFLTermIds = getSflWordDAO().getParallelWordMatchedSFLTermIds();
            Set<Long> matchedSflTermIds = new HashSet<Long>();
            if (ExecueCoreUtil.isCollectionNotEmpty(riOntoMatchedSFLTermIds)) {
               matchedSflTermIds.addAll(riOntoMatchedSFLTermIds);
            }
            if (ExecueCoreUtil.isCollectionNotEmpty(parallelMatchedSFLTermIds)) {
               matchedSflTermIds.addAll(parallelMatchedSFLTermIds);
            }
            sflTermIds.removeAll(matchedSflTermIds);

         }
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
      return sflTermIds;
   }

   public void cleanSFLTerms (List<Long> sflTermIds) throws KDXException {

      // TODO :-VG- need to do this in batches.
      try {
         for (Long sflTermId : sflTermIds) {
            SFLTerm sflTerm = getSflWordDAO().getById(sflTermId, SFLTerm.class);
            getSflWordDAO().delete(sflTerm);
         }
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }

   }

   public List<BusinessEntityDefinition> getInstanceBEDsByConceptId (long modelId, long conceptId) throws KDXException {
      try {
         return getInstanceDAO().getInstanceBEDsByConceptId(modelId, conceptId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Long> getInstanceBEDIdsByConceptId (Long modelId, Long conceptId) throws KDXException {
      try {
         return getInstanceDAO().getInstanceBEDIdsByConceptId(modelId, conceptId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void updateBusinessEntityDefinition (BusinessEntityDefinition businessEntityDefinition) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().update(businessEntityDefinition);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public Application getApplicationByName (String applicationName) throws KDXException {
      try {
         return getBusinessEntityDefinitionDAO().getByName(applicationName, Application.class);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public Application getApplicationByKey (String applicationKey) throws KDXException {
      try {
         return getApplicationDAO().getApplicationByKey(applicationKey);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public Model getModelByName (String modelName) throws KDXException {
      try {
         return getBusinessEntityDefinitionDAO().getByName(modelName, Model.class);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }

   }

   public void updateModel (Model model) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().update(model);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<PopularityBusinessEntityDefinitionInfo> getPopularityInfoForModelGroup (Long modelGroupId)
            throws KDXException {
      try {
         return getPopularityHitDAO().getPopularityInfoForModelGroup(modelGroupId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Long> getAllSFLTermIds () throws KDXException {
      try {
         return getSflWordDAO().getAllSFLTermIds();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }

   }

   public List<SFLTerm> getPopulatedSFLTerms (List<Long> sflTermIds) throws KDXException {
      try {
         return getSflWordDAO().getPopulatedSFLTerms(sflTermIds);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Long> getSFLTermIdsForNonZeroHits () throws KDXException {
      try {
         return getSflWordDAO().getSFLTermIdsForNonZeroHits();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Long> getInstanceProfileBEDIdsForConceptBED (Long conceptBEDId) throws KDXException {
      try {
         return getProfileDAO().getInstanceProfileBEDIdsForConceptBED(conceptBEDId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<BusinessEntityDefinition> getConceptBEDsOfModel (Long modelId) throws KDXException {
      try {
         return getConceptDAO().getConceptBEDsOfModel(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<SFLTerm> getOrphanSFLTerms (Long modelGroupId) throws KDXException {
      try {
         return getSflWordDAO().getOrphanSFLTerms(modelGroupId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<RIOntoTerm> getRIOntoTermsByBEDId (Long bedId, BusinessEntityType type) throws KDXException {
      try {
         return getOntoReverseIndexDAO().getRIOntoTermsByBEDId(bedId, type);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteRIOntoTerm (RIOntoTerm riOntoTerm) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().delete(riOntoTerm);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<RIOntoTerm> getTermsByLookupWordAndEntityTypeInGroupForInstance (String word,
            BusinessEntityType entityType, Long modelGroupId, Long conceptBEDID) throws KDXException {
      try {
         return getOntoReverseIndexDAO().getTermsByLookupWordAndEntityTypeInGroupForInstance(word, entityType,
                  modelGroupId, conceptBEDID);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Application> getApplicationsByModelId (Long modelId) throws KDXException {
      try {
         return getApplicationDAO().getApplicationsByModelId(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Application> getApplicationsByPage (Page pageDetail) throws KDXException {
      try {
         return getApplicationDAO().getApplicationsByPage(pageDetail);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<AppDashBoardInfo> getAppDashBoardInfosByPage (Page pageDetail) throws KDXException {
      try {
         return getApplicationDAO().getAppDashBoardInfosByPage(pageDetail);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<AppDashBoardInfo> getAppDashBoardInfosByPage (Page pageDetail, boolean advancedMenu) throws KDXException {
      try {
         return getApplicationDAO().getAppDashBoardInfosByPage(pageDetail, advancedMenu);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Application> getApplicationsByImageId (Long imageId) throws KDXException {
      try {
         return getApplicationDAO().getApplicationsByImageId(imageId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public Long getAllExistingApplicationsCount () throws KDXException {
      try {
         return getApplicationDAO().getAllExistingApplicationsCount();
      } catch (DataAccessException e) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public Long getInstanceBEDsCountByConceptId (Long conceptId) throws KDXException {
      try {
         return getInstanceDAO().getInstanceBEDsCountByConceptId(conceptId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<BusinessEntityDefinition> getInstanceBEDsByPage (Long conceptId, Long pageNumber, Long pageSize)
            throws KDXException {
      try {
         return getInstanceDAO().getInstanceBEDsByPage(conceptId, pageNumber, pageSize);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public ApplicationExample createApplicationExample (ApplicationExample applicationExample) throws KDXException {

      try {
         return getBusinessEntityDefinitionDAO().create(applicationExample);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.code, dataAccessException.getMessage(), dataAccessException);
      }
   }

   public void updateApplicationExample (ApplicationExample applicationExample) throws KDXException {

      try {
         getBusinessEntityDefinitionDAO().update(applicationExample);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.code, dataAccessException.getMessage(), dataAccessException);
      }
   }

   public void deleteApplicationExample (ApplicationExample applicationExample) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().delete(applicationExample);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.code, dataAccessException.getMessage(), dataAccessException);
      }
   }

   public void deleteApplicationExamples (List<ApplicationExample> applicationExamples) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().deleteAll(applicationExamples);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.code, dataAccessException.getMessage(), dataAccessException);
      }
   }

   public List<ApplicationExample> getAllAppExampleForApplication (Long appId) throws KDXException {
      try {
         return getApplicationDAO().getAllAppExampleForApplication(appId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<RIOntoTerm> getTermsByLookupWordAndTypeBedId (String word, Long typeBedId) throws KDXException {
      try {
         return getOntoReverseIndexDAO().getTermsByLookupWordAndTypeBedId(word, typeBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<RIOntoTerm> getConceptTermsByTypeBedIdAndModelGroupId (Long typeBedId, Long modelGroupId)
            throws KDXException {
      try {
         return getOntoReverseIndexDAO().getConceptTermsByTypeBedIdAndModelGroupId(typeBedId, modelGroupId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#getAllPossibleBehavior(java.lang.Long)
    */
   public List<BehaviorType> getAllPossibleBehavior (Long typeBedId) throws KDXException {
      try {
         return getEntityBehaviorDAO().getAllPossibleBehavior(typeBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<PossibleAttribute> getAllPossibleAttributes (Long typeBedId) throws KDXException {
      try {
         return getEntityAttributeDAO().getAllPossibleAttributes(typeBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#getAllBehaviorForEntity(java.lang.Long)
    */
   public List<BehaviorType> getAllBehaviorForEntity (Long entityBedId) throws KDXException {
      try {
         return getEntityBehaviorDAO().getAllBehaviorForEntity(entityBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#checkEntityHasBehavior(java.lang.Long,
    *      com.execue.core.common.type.BehaviorType)
    */
   public boolean checkEntityHasBehavior (Long entityBedId, BehaviorType behaviorType) throws KDXException {
      try {
         return getEntityBehaviorDAO().checkEntityHasBehavior(entityBedId, behaviorType);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<RIOntoTerm> getTermsByLookupWordAndConceptBedId (String word, Long conceptBedId) throws KDXException {
      try {
         return getOntoReverseIndexDAO().getTermsByLookupWordAndConceptBedId(word, conceptBedId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#createEntityBehaviors(java.util.List)
    */
   public void createEntityBehaviors (List<EntityBehavior> entityBehaviors) throws KDXException {
      for (EntityBehavior entityBehavior : entityBehaviors) {
         try {
            getBusinessEntityDefinitionDAO().create(entityBehavior);
         } catch (DataAccessException e) {
            throw new KDXException(e.getCode(), e);
         }
      }

   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#getRealizationsForTypeInModel(java.lang.Long, java.lang.Long)
    */
   public List<BusinessEntityDefinition> getRealizationsForTypeInModelIncludingBaseGroup (Long typeId, Long modelId)
            throws KDXException {
      try {
         return getTypeDAO().getRealizationsForTypeInModelIncludingBaseGroup(typeId, modelId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#getPossibleAttributesByIds(java.util.List)
    */
   public List<PossibleAttribute> getPossibleAttributesByIds (List<Long> possibleAttributeIds) throws KDXException {
      try {
         return getEntityAttributeDAO().getPossibleAttributesByIds(possibleAttributeIds);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public BusinessEntityDefinition getTypeBusinessEntityDefinition (Long typeId) throws KDXException {
      try {
         return getTypeDAO().getTypeBusinessEntityDefinition(typeId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getTypeBedByName (String typeName, boolean isRealizedType) throws KDXException {
      try {
         return getTypeDAO().getTypeBedByName(typeName, isRealizedType);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e.getMessage(), e);
      }
   }

   public BusinessEntityDefinition getTypeBedByTypeID (Long typeID, boolean isRealizedType) throws KDXException {
      try {
         return getTypeDAO().getTypeBedByTypeID(typeID, isRealizedType);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e.getMessage(), e);
      }
   }

   public List<Type> getAllTypes () throws KDXException {
      try {
         return getTypeDAO().getAllTypes();
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e.getMessage(), e);
      }
   }

   public List<RIOntoTerm> getRegularExpressionBasedOntoTerms () throws KDXException {
      try {
         return getOntoReverseIndexDAO().getRegularExpressionBasedOntoTerms();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteEntityBehavior (Long conceptBedId) throws KDXException {
      try {
         getEntityBehaviorDAO().deleteEntityBehavior(conceptBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public PossibleAttribute getPossibleAttribute (Long typeBedId, Long componentTypeBedId, Long relationBedId)
            throws KDXException {
      try {
         return getEntityAttributeDAO().getPossibleAttribute(typeBedId, componentTypeBedId, relationBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#getSFLTermsForInstancesOfConceptByBatchNumber(java.lang.Long,
    *      java.lang.Long, java.lang.Long)
    */
   public List<SFLTerm> getSFLTermsForInstancesOfConceptByBatchNumber (Long conceptBedId, Long batchNumber,
            Long batchSize) throws KDXException {
      try {
         return getSflWordDAO().getSFLTermsForInstancesOfConceptByBatchNumber(conceptBedId, batchNumber, batchSize);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<BusinessEntityDefinition> getAllAttributeEntities (Long modelId) throws KDXException {
      try {
         return getEntityAttributeDAO().getAllAttributeEntities(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<BusinessEntityDefinition> getAllAttributeTypes () throws KDXException {
      try {
         return getEntityAttributeDAO().getAllAttributeTypes();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#deleteSecondaryWords(java.util.List)
    */
   public void deleteSecondaryWords (List<SecondaryWord> secondaryWords) throws KDXException {
      try {
         getSflWordDAO().deleteAll(secondaryWords);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }

   }

   public List<String> getEligibleSecondaryWordsForModel (Long modelId, Long threshold) throws KDXException {
      try {
         return getSflWordDAO().getEligibleSecondaryWordsForModel(modelId, threshold);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteEntityBehaviors (Long conceptBedId, List<BehaviorType> behaviorTypes) throws KDXException {
      try {
         getEntityBehaviorDAO().deleteEntityBehaviors(conceptBedId, behaviorTypes);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Relation> suggestRelationsByName (Long modelId, java.lang.String searchString, int maxResults)
            throws KDXException {
      try {
         return getRelationDAO().suggestRelationsByName(modelId, searchString, maxResults);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public void createBusinessEntityMaintenanceInfo (BusinessEntityMaintenanceInfo businessEntityMaintenanceInfo)
            throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().create(businessEntityMaintenanceInfo);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void deleteBusinessEntityMaintenanceDetails (Long modelId) throws KDXException {
      try {
         getBusinessEntityMaintenanceDAO().deleteBusinessEntityMaintenanceDetails(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void deleteBusinessEntityMaintenanceDetails (Long modelId, EntityType entityType) throws KDXException {
      try {
         getBusinessEntityMaintenanceDAO().deleteBusinessEntityMaintenanceDetails(modelId, entityType);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Long> getBusinessEntityMaintenanceDetails (Long modelId, OperationType operationType,
            EntityType entityType) throws KDXException {
      try {
         return getBusinessEntityMaintenanceDAO().getBusinessEntityMaintenanceDetails(modelId, operationType,
                  entityType);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Long> getBusinessEntityMaintenanceParentDetails (Long modelId, EntityType entityType)
            throws KDXException {
      try {
         return getBusinessEntityMaintenanceDAO().getBusinessEntityMaintenanceParentDetails(modelId, entityType);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Long> getDistinctUpdatedEntityMaintenanceDetails (Long modelId, EntityType entityType)
            throws KDXException {
      try {
         return getBusinessEntityMaintenanceDAO().getDistinctUpdatedEntityMaintenanceDetails(modelId, entityType);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Long> getBusinessEntityMaintenanceDetailsByParentId (Long modelId, OperationType operationType,
            EntityType entityType, Long parentId) throws KDXException {
      try {
         return getBusinessEntityMaintenanceDAO().getBusinessEntityMaintenanceDetailsByParentId(modelId, operationType,
                  entityType, parentId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public Map<Long, Map<Long, String>> getAllChildToParentTriplesForModel (Long modelId) throws KDXException {
      try {
         return getPathDefinitionDAO().getAllChildToParentTriplesForModel(modelId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public Map<Long, Map<Long, String>> getAllParentToChildTriplesForModel (Long modelId) throws KDXException {
      try {
         return getPathDefinitionDAO().getAllParentToChildTriplesForModel(modelId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public List<Application> getApplicationsForVertical (String verticalName) throws KDXException {
      try {
         return getVerticalDAO().getApplicationsForVertical(verticalName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<String> getApplicationNamesForVertical (String verticalName) throws KDXException {
      try {
         return getVerticalDAO().getApplicationNamesForVertical(verticalName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<BusinessEntityDefinition> getDistinctUpdatedMisMatchedEnumerationConceptBEDsFromBEMDetails (Long modelId)
            throws KDXException {
      try {
         return getConceptDAO().getDistinctUpdatedMisMatchedEnumerationConceptBEDsFromBEMDetails(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void createDefaultDynamicValue (DefaultDynamicValue defaultDynamicValue) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().create(defaultDynamicValue);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }

   }

   public void deleteDefaultDynamicValue (DefaultDynamicValue defaultDynamicValue) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().delete(defaultDynamicValue);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }

   }

   public List<DefaultDynamicValue> getDefaultDynamicValues (Long assetId) throws KDXException {
      try {
         return getDefaultDynamicValueDAO().getDefaultDynamicValues(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<DefaultDynamicValue> getDefaultDynamicValues (Long assetId, Long bedId) throws KDXException {
      try {
         return getDefaultDynamicValueDAO().getDefaultDynamicValues(assetId, bedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public PossibleAttribute getPossibleAttribute (Long typeBedId, Long componentTypeBedId) throws KDXException {
      try {
         return getEntityAttributeDAO().getPossibleAttribute(typeBedId, componentTypeBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getBEDByBehaviorName (String behaviorName) throws KDXException {
      try {
         return getEntityBehaviorDAO().getBEDByBehaviorName(behaviorName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   private void assignKnowledgeId (BusinessEntityDefinition businessEntityDefinition, Long knowledgeId) {
      if (businessEntityDefinition.getKnowledgeId() == null) {
         businessEntityDefinition.setKnowledgeId(knowledgeId);
      }
   }

   public boolean isMatchedBusinessEntityType (Long bedId, String typeName) throws KDXException {
      try {
         return getTypeDAO().isMatchedBusinessEntityType(bedId, typeName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public boolean isConceptMatchedBusinessEntityType (Long conceptId, Long modelId, String typeName)
            throws KDXException {
      try {
         return getTypeDAO().isConceptMatchedBusinessEntityType(conceptId, modelId, typeName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void createDefaultDynamicValues (List<DefaultDynamicValue> defaultDynamicValues) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().saveOrUpdateAll(defaultDynamicValues);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<BusinessEntityDefinition> getConceptsOfParticularType (Long modelId, String typeName)
            throws KDXException {
      try {
         return getConceptDAO().getConceptsOfParticularType(modelId, typeName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getSFLTermIdsByContextId (Long contextId) throws KDXException {
      try {
         return getSflWordDAO().getSFLTermIdsByContextId(contextId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Application> getVerticalApplicationsByRank (Long verticalId, Long limit) throws KDXException {
      try {
         return getVerticalDAO().getVerticalApplicationsByRank(verticalId, limit);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void createVerticalAppWeight (VerticalAppWeight verticalAppWeight) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().create(verticalAppWeight);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Vertical getVerticalByName (String name) throws KDXException {
      try {
         return getVerticalDAO().getVerticalByName(name);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public VerticalAppWeight getVerticalAppWeightByApplicationId (Long id) throws KDXException {
      try {
         return getVerticalDAO().getVerticalAppWeightByApplicationId(id);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<VerticalEntityBasedRedirection> getVerticalRedirectionEntitiesByApplicationId (Long id)
            throws KDXException {
      try {
         return getVerticalDAO().getVerticalRedirectionEntitiesByApplicationId(id);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<VerticalAppExample> getVerticalAppExamplesByApplicationId (Long applicationId) throws KDXException {
      try {
         return getVerticalDAO().getVerticalAppExamplesByApplicationId(applicationId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<ApplicationOperation> getApplicationOperationsByApplicationId (Long applicationId) throws KDXException {
      try {
         return getApplicationDAO().getApplicationOperationsByApplicationId(applicationId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteVerticalAppWeight (VerticalAppWeight verticalAppWeight) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().delete(verticalAppWeight);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteVerticalRedirectionEntities (
            List<VerticalEntityBasedRedirection> verticalEntityBasedRedirectionList) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().deleteAll(verticalEntityBasedRedirectionList);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteVerticalAppExamples (List<VerticalAppExample> verticalAppExamples) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().deleteAll(verticalAppExamples);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteApplicationOperations (List<ApplicationOperation> applicationOperations) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().deleteAll(applicationOperations);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Map<String, List<VerticalAppExample>> getVerticalAppExamples () throws KDXException {
      try {
         List<Vertical> verticals = getVerticalDAO().getVerticals();
         Map<String, List<VerticalAppExample>> verticalAppExampleMap = new HashMap<String, List<VerticalAppExample>>();
         if (ExecueCoreUtil.isCollectionNotEmpty(verticals)) {
            for (Vertical vertical : verticals) {
               List<VerticalAppExample> verticalAppExamples = getVerticalDAO().getVerticalAppExamples(vertical.getId());
               verticalAppExampleMap.put(vertical.getName(), verticalAppExamples);
            }
         }
         return verticalAppExampleMap;
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Map<String, List<VerticalAppExample>> getVerticalAppExamplesByDay (int day) throws KDXException {
      try {
         List<Vertical> verticals = getVerticalDAO().getVerticals();
         Map<String, List<VerticalAppExample>> verticalAppExampleMap = new LinkedHashMap<String, List<VerticalAppExample>>();
         Map<Long, String> verticalMap = new HashMap<Long, String>();
         if (ExecueCoreUtil.isCollectionNotEmpty(verticals)) {
            for (Vertical vertical : verticals) {
               verticalMap.put(vertical.getId(), vertical.getName());
               verticalAppExampleMap.put(vertical.getName(), new ArrayList<VerticalAppExample>());
            }

            List<VerticalAppExample> verticalAppExamples = getVerticalDAO().getVerticalAppExamplesByDay(day);

            for (VerticalAppExample verticalAppExample : verticalAppExamples) {
               String name = verticalMap.get(verticalAppExample.getVerticalId());
               List<VerticalAppExample> list = verticalAppExampleMap.get(name);
               if (list != null) {
                  list.add(verticalAppExample);
               }
            }
         }
         return verticalAppExampleMap;
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Application> getApplicationsByRank (Long limit) throws KDXException {
      try {
         return getApplicationDAO().getApplicationsByRank(limit);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Concept> getConceptsByPopularity (Long modelId, Long limit) throws KDXException {
      try {
         return getConceptDAO().getConceptsByPopularity(modelId, limit);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.CONCEPT_RETRIEVAL_FAILED, dataAccessException);
      }
   }

   public List<Instance> getInstancesByPopularity (Long modelId, Long conceptId, Long limit) throws KDXException {
      try {
         return getInstanceDAO().getInstancesByPopularity(modelId, conceptId, limit);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Application> getApplicationsByVerticalId (Long verticalId) throws KDXException {
      try {
         return getVerticalDAO().getApplicationsByVerticalId(verticalId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteInstances (List<Instance> instances) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().deleteInstances(instances);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteBusinessEntityMaintenanceDetails (Long modelId, List<Long> entityBedIds, EntityType entityType)
            throws KDXException {
      try {
         getBusinessEntityMaintenanceDAO().deleteBusinessEntityMaintenanceDetails(modelId, entityBedIds, entityType);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Instance getInstanceByInstanceName (Long conceptBedId, String instanceName) throws KDXException {
      try {
         return getInstanceDAO().getInstanceByInstanceName(conceptBedId, instanceName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<RIOntoTerm> getConceptOntoTermsByConceptBedIds (Set<Long> conceptBedIds) throws KDXException {
      try {
         return getOntoReverseIndexDAO().getConceptOntoTermsByConceptBedIds(conceptBedIds);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Vertical> getAppVerticals (Long applicationId) throws KDXException {
      try {
         return getVerticalDAO().getAppVerticals(applicationId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void cleanApplicationOperationData () throws KDXException {
      try {
         getApplicationDAO().cleanApplicationOperationData();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Model> getAllModels () throws KDXException {
      try {
         return getModelDAO().getAllModels();
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<PossibleDetailType> getPossibleDetailTypes (Long typeBedId) throws KDXException {
      try {
         return getTypeDAO().getPossibleDetailTypes(typeBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<BusinessEntityDefinition> getPossibleDetailTypesForType (Long typeBedId) throws KDXException {
      try {
         return getTypeDAO().getPossibleDetailTypesForType(typeBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public EntityDetailType getDetailTypeForConcept (Long conceptBedId) throws KDXException {
      try {
         return getTypeDAO().getDetailTypeForConcept(conceptBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void createEntityDetailType (EntityDetailType entityDetailType) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().create(entityDetailType);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_DETAIL_TYPE_CREATE_FAILURE_CODE, dataAccessException);
      }

   }

   public void deleteEntityDetailTypeByConcept (Long conceptBedId) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().deleteEntityDetailTypeByConcept(conceptBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_DETAIL_TYPE_DELETE_FAILURE_CODE, dataAccessException);
      }
   }

   public BusinessEntityDefinition getBEDByRealizationName (Long modelId, String realizationName) throws KDXException {
      try {
         return getTypeDAO().getBEDByRealizationName(modelId, realizationName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public BusinessEntityDefinition getDefaultDetailType (Long typeBedId) throws KDXException {
      try {
         return getTypeDAO().getDefaultDetailType(typeBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getModelGroupIdsByApplicationId (Long applicationId) throws KDXException {
      try {
         return getModelDAO().getModelGroupIdsByApplicationId(applicationId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<DefaultDynamicValue> getDefaultDynamicValues (Long assetId, List<Long> bedIds) throws KDXException {
      try {
         return getDefaultDynamicValueDAO().getDefaultDynamicValues(assetId, bedIds);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Concept> getMappedConceptsForParticularBehaviour (Long modelId, Long assetId, BehaviorType behaviourType)
            throws KDXException {
      try {
         return getConceptDAO().getMappedConceptsForParticularBehaviour(modelId, assetId, behaviourType);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Concept> getMappedConceptsForParticularType (Long modelId, Long assetId, String typeName)
            throws KDXException {
      try {
         return getConceptDAO().getMappedConceptsForParticularType(modelId, assetId, typeName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Type> getAllVisibleTypes () throws KDXException {
      try {
         return getTypeDAO().getAllVisibleTypes();
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e.getMessage(), e);
      }
   }

   public List<BusinessEntityDefinition> getAllNonAttributeEntities (Long modelId) throws KDXException {
      try {
         return getEntityBehaviorDAO().getAllNonAttributeEntities(modelId);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e.getMessage(), e);
      }
   }

   public Map<String, String> getConceptInstanceDisplayNamesByAbbrevatedNames (Long modelId, String conceptName,
            List<String> abbrevatedNames) throws KDXException {
      try {
         return getBusinessEntityDefinitionDAO().getConceptInstanceDisplayNamesByAbbrevatedNames(modelId, conceptName,
                  abbrevatedNames);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e.getMessage(), e);
      }

   }

   public Instance getTopPopularityInstance (Long modelId, String originAirportConceptName) throws KDXException {
      try {
         return getInstanceDAO().getTopPopularityInstance(modelId, originAirportConceptName);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e.getMessage(), e);
      }

   }

   public Map<Long, RIOntoTerm> getOntoTermsByEntityBeId (Set<Long> entityBedIds) throws KDXException {
      try {
         return getOntoReverseIndexDAO().getOntoTermsByEntityBeId(entityBedIds);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<ModelGroup> getUserModelGroupsByModelId (Long modelId) throws KDXException {
      try {
         return getModelDAO().getUserModelGroupsByModelId(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<RISharedUserModelMapping> getRISharedUserModelMappings (List<Long> baseInstanceBedIds,
            Set<Long> modelGroupIds) throws KDXException {
      try {
         return getRiSharedUserModelMappingDAO().getRISharedUserModelMappings(baseInstanceBedIds, modelGroupIds);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<RISharedUserModelMapping> getRISharedUserModelMappings (List<Long> baseInstanceBedIds)
            throws KDXException {
      try {
         return getRiSharedUserModelMappingDAO().getRISharedUserModelMappings(baseInstanceBedIds);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /** **Business entity variation methods start ***** */
   public List<String> getBusinessEntityVariationNames (Long entityBedId) throws KDXException {
      try {
         return getBusinessEntityVariationDAO().getBusinessEntityVariationNames(entityBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<BusinessEntityVariation> getBusinessEntityVariations (Long entityBedId) throws KDXException {
      try {
         return getBusinessEntityVariationDAO().getBusinessEntityVariations(entityBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void createBusinessEntityVariation (BusinessEntityVariation businessEntityVariation) throws KDXException {
      try {
         getBusinessEntityVariationDAO().create(businessEntityVariation);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void createBusinessEntityVariations (List<BusinessEntityVariation> businessEntityVariations)
            throws KDXException {
      try {
         getBusinessEntityVariationDAO().createAll(businessEntityVariations);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteBusinessEntityVariation (BusinessEntityVariation businessEntityVariation) throws KDXException {
      try {
         getBusinessEntityVariationDAO().delete(businessEntityVariation);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void updateBusinessEntityVariation (BusinessEntityVariation businessEntityVariation) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().update(businessEntityVariation);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteBusinessEntityVariationsByBedId (Long businessEntityId) throws KDXException {
      try {
         getBusinessEntityVariationDAO().deleteBusinessEntityVariationsByBedId(businessEntityId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /** **Business entity variation methods start ***** */
   public void deleteRISharedUserModelMappings (List<Long> userModelGroupIds) throws KDXException {
      try {
         getRiSharedUserModelMappingDAO().deleteRISharedUserModelMappings(userModelGroupIds);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void createRISharedUserModelMapping (RISharedUserModelMapping riSharedUserModelMapping) throws KDXException {
      try {
         getOntoReverseIndexDAO().create(riSharedUserModelMapping);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteBusinessEntityVariations (List<Long> modelGroupIds) throws KDXException {
      try {
         getBusinessEntityVariationDAO().deleteBusinessEntityVariations(modelGroupIds);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getConceptBedIdsHavingInstances (Long modelId) throws KDXException {
      try {
         return getConceptDAO().getConceptBedIdsHavingInstances(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getConceptBedsHavingBehaviorType (List<Long> conceptBedIds, BehaviorType behaviorType)
            throws KDXException {
      try {
         return getConceptDAO().getConceptBedsHavingBehaviorType(conceptBedIds, behaviorType);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteBusinessEntityDefinitionById (Long bedId) throws KDXException {
      try {
         BusinessEntityDefinition businessEntityDefinition = getBusinessEntityDefinitionDAO().getById(bedId,
                  BusinessEntityDefinition.class);
         getBusinessEntityDefinitionDAO().delete(businessEntityDefinition);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteIndexFormsByBedId (Long instanceProfileBedId) throws KDXException {
      try {
         getOntoReverseIndexDAO().deleteIndexFormsByBedId(instanceProfileBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }

   }

   public List<Long> getNonSharedBusinessEntityMaintenanceDetailsByParentId (Long modelId, OperationType operationType,
            EntityType entityType, Long parentId) throws KDXException {
      try {
         return getBusinessEntityMaintenanceDAO().getNonSharedBusinessEntityMaintenanceDetailsByParentId(modelId,
                  operationType, entityType, parentId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Instance> getInstancesByBEDIds (List<Long> instanceBEDIds) throws KDXException {
      try {
         return getInstanceDAO().getInstancesByBEDIds(instanceBEDIds);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<BusinessEntityInfo> getInstanceBusinessEntitiesByPageForModel (Long modelId, Page page)
            throws KDXException {
      try {
         return getInstanceDAO().getInstanceBusinessEntitiesByPageForModel(modelId, page);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<BusinessEntityInfo> getConceptBusinessEntitiesByPageForModel (Long modelId, Page page)
            throws KDXException {
      try {
         return getConceptDAO().getConceptBusinessEntitiesByPageForModel(modelId, page);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<BusinessEntityInfo> getConceptProfileBusinessEntitiesByPageForModel (Long modelId, Page page)
            throws KDXException {
      try {
         return getProfileDAO().getConceptProfileBusinessEntitiesByPageForModel(modelId, page);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<BusinessEntityInfo> getInstanceProfileBusinessEntitiesByPageForModel (Long modelId, Page page)
            throws KDXException {
      try {
         return getProfileDAO().getInstanceProfileBusinessEntitiesByPageForModel(modelId, page);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<BusinessEntityInfo> getRelationBusinessEntitiesByPageForModel (Long modelId, Page page)
            throws KDXException {
      try {
         return getRelationDAO().getRelationBusinessEntitiesByPageForModel(modelId, page);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteRelation (Relation relation) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().delete(relation);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public void deleteConcept (Concept concept) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().delete(concept);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   public void deleteInstance (Instance instance) throws KDXException {
      try {
         getBusinessEntityDefinitionDAO().delete(instance);
      } catch (DataAccessException e) {
         throw new KDXException(e.getCode(), e);
      }
   }

   @Override
   public List<Concept> getEligibleConceptsOfAssetForCubeByPage (Long assetId, Page page) throws KDXException {
      try {
         return getConceptDAO().getEligibleConceptsOfAssetForCubeByPage(assetId, page);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public ModelGroup getModelGroupByTypeBedId (Long typeBedId) throws KDXException {
      try {
         return getModelDAO().getModelGroupByTypeBedId(typeBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<BusinessEntityDefinition> getBedByModelGroupsAndConceptId (List<Long> modelGroupIds, Long conceptId)
            throws KDXException {
      try {
         return getConceptDAO().getBedByModelGroupsAndConceptId(modelGroupIds, conceptId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteVerticalEntityRedirectionEntitiesByEntityBedId (Long entityBedId, BusinessEntityType entityType)
            throws KDXException {
      try {
         getVerticalDAO().deleteVerticalEntityRedirectionEntitiesByEntityBedId(entityBedId, entityType);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public void deleteDefaultDynamicValueByBedId (Long bedId) throws KDXException {
      try {
         getDefaultDynamicValueDAO().deleteDefaultDynamicValueByBedId(bedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Application> getApplicationsByType (AppSourceType appSourceType) throws KDXException {
      try {
         return getApplicationDAO().getApplicationsByType(appSourceType);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public void deleteInstanceRIOntoTermsByConceptBEDId (Long modelGroupId, Long conceptBEDId) throws KDXException {
      try {
         getOntoReverseIndexDAO().deleteInstanceRIOntoTermsByConceptBEDId(modelGroupId, conceptBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }

   }

   public void deleteRIOntoTermsByProfileBEDId (Long modelId, Long profileBEDId) throws KDXException {
      try {
         getOntoReverseIndexDAO().deleteRIOntoTermsByProfileBEDId(modelId, profileBEDId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }

   }

   public void updateInstanceRIOntoTermsWithConceptInfo (Long modelGroupId, Long conceptBEDId, String conceptName,
            Long typeBEDId, String typeName) throws KDXException {
      try {
         getOntoReverseIndexDAO().updateInstanceRIOntoTermsWithConceptInfo(modelGroupId, conceptBEDId, conceptName,
                  typeBEDId, typeName);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }

   }

   public List<Long> getInstanceIdsByConceptId (Long modelId, Long conceptId) throws KDXException {
      try {
         return getInstanceDAO().getInstanceIdsByConceptId(modelId, conceptId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public void deleteBusinessEntityMaintenanceDetailsByEntityBedId (Long entityBedId) throws KDXException {
      try {
         getBusinessEntityMaintenanceDAO().deleteBusinessEntityMaintenanceDetailsByEntityBedId(entityBedId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public void deleteBusinessEntityMaintenanceInstancesByConceptId (Long modelId, Long conceptId) throws KDXException {
      try {
         getBusinessEntityMaintenanceDAO().deleteBusinessEntityMaintenanceInstancesByConceptId(modelId, conceptId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   @Override
   public List<Long> findMatchingTypeInstanceIncludingVariations (Long modelGroupId, Long typeId, String instanceValue,
            BusinessEntityType entityType) throws KDXException {
      try {
         return getInstanceDAO().findMatchingTypeInstanceIncludingVariations(modelGroupId, typeId, instanceValue,
                  entityType);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   @Override
   public List<Concept> getLookupTypeConceptsForModelBySearchString (Long modelId, String searchString,
            List<Long> locationRealizedTypeIds, Long retrievalLimit) throws KDXException {
      try {
         return getConceptDAO().getLookupTypeConceptsForModelBySearchString(modelId, searchString,
                  locationRealizedTypeIds, retrievalLimit);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   @Override
   public Long getEntityCountWithTypeBedIds (Long modelId, List<Long> locationChildrenBedIds) throws KDXException {
      try {
         return getTypeDAO().getEntityCountWithTypeBedIds(modelId, locationChildrenBedIds);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   @Override
   public boolean isConceptMatchedBehavior (Long conceptBedId, BehaviorType behaviorType) throws KDXException {
      boolean isConceptMatchedBehavior = false;
      try {
         EntityBehavior entityBehavior = getEntityBehaviorDAO().getMatchingEntityBehavior(conceptBedId, behaviorType);
         if (entityBehavior != null) {
            isConceptMatchedBehavior = true;
         }
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
      return isConceptMatchedBehavior;
   }

   @Override
   public List<ContentCleanupPattern> getContentCleanupPatterns (Long applicationId) throws KDXException {
      try {
         return getContentCleanupPatternDAO().getContentCleanupPatterns(applicationId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   @Override
   public void createContentCleanupPattern (ContentCleanupPattern contentCleanupPattern) throws KDXException {
      try {
         getContentCleanupPatternDAO().create(contentCleanupPattern);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   @Override
   public UnstructuredApplicationDetail getUnstructuredApplicationDetailByApplicationId (Long applicationId)
            throws KDXException {
      try {
         return getUnstructuredApplicationDetailDAO().getUnstructuredApplicationDetailByApplicationId(applicationId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   @Override
   public void createUnstructuredApplicationDetail (UnstructuredApplicationDetail unstructuredApplicationDetail)
            throws KDXException {
      try {
         getUnstructuredApplicationDetailDAO().create(unstructuredApplicationDetail);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }

   }

   @Override
   public void updateUnstructuredApplicationDetail (UnstructuredApplicationDetail unstructuredApplicationDetail)
            throws KDXException {
      try {
         getUnstructuredApplicationDetailDAO().update(unstructuredApplicationDetail);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }

   }

   @Override
   public RISharedUserModelMapping getRISharedUserModelMappingByAppInstanceBedId (Long appInstanceBedId,
            Set<Long> modelGroupIds) throws KDXException {
      try {
         return getRiSharedUserModelMappingDAO().getRISharedUserModelMappingByAppInstanceBedId(appInstanceBedId,
                  modelGroupIds);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   @Override
   public BusinessEntityDefinition getRelationBEDById (Long modelId, Long relationId) throws KDXException {
      try {
         return getRelationDAO().getRelationBEDById(modelId, relationId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   @Override
   public void deleteInstanceVariationsForConcept (Long modelId, Long conceptId) throws KDXException {
      try {
         getBusinessEntityVariationDAO().deleteInstanceVariationsForConcept(modelId, conceptId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }

   }

   @Override
   public void deleteInstanceBedsForConcept (Long modelId, Long conceptId) throws KDXException {
      try {
         getInstanceDAO().deleteInstanceBedsForConcept(modelId, conceptId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }

   }

   @Override
   public void deleteInstanceByIds (List<Long> instanceIds) throws KDXException {
      try {
         getInstanceDAO().deleteInstanceByIds(instanceIds);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }

   }

   @Override
   public boolean hasInstances (Long modelId, Long conceptId) throws KDXException {
      try {
         return getConceptDAO().hasInstances(modelId, conceptId);
      } catch (DataAccessException dae) {
         throw new KDXException(dae.getCode(), dae);
      }

   }

   @Override
   public List<Concept> getConceptByBEDIds (List<Long> conceptBEDIds) throws KDXException {
      try {
         return getConceptDAO().getConceptByBEDIds(conceptBEDIds);
      } catch (DataAccessException dae) {
         throw new KDXException(dae.getCode(), dae);
      }

   }

   @Override
   public void createHierarchy (Hierarchy hierarchy) throws KDXException {
      try {
         addHierarchyToHiearchyDetails(hierarchy);
         getHierarchyDAO().create(hierarchy);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.MODEL_CREATION_FAILED, dataAccessException);
      }
   }

   @Override
   public void updateHierarchy (Hierarchy hierarchy) throws KDXException {
      try {
         addHierarchyToHiearchyDetails(hierarchy);
         getHierarchyDAO().merge(hierarchy);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.INSTANCE_UPDATE_FAILED, dataAccessException);
      }
   }

   private void addHierarchyToHiearchyDetails (Hierarchy hierarchy) {
      for (HierarchyDetail hierarchyDetail : hierarchy.getHierarchyDetails()) {
         hierarchyDetail.setHierarchy(hierarchy);
      }
   }

   @Override
   public void deleteHierarchy (Hierarchy hierarchy) throws KDXException {
      try {
         getHierarchyDAO().delete(hierarchy);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.INSTANCE_UPDATE_FAILED, dataAccessException);
      }
   }

   @Override
   public List<Hierarchy> getExistingHierarchiesForConcept (Long conceptBedId) throws KDXException {
      try {
         return getHierarchyDAO().getExistingHierarchiesForConcept(conceptBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.INSTANCE_UPDATE_FAILED, dataAccessException);
      }
   }

   @Override
   public List<Hierarchy> getHierarchiesByModelId (Long modelId) throws KDXException {
      try {
         return getHierarchyDAO().getHierarchiesByModelId(modelId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(SWIExceptionCodes.INSTANCE_UPDATE_FAILED, dataAccessException);
      }
   }

   @Override
   public Hierarchy getHierarchyByNameForModel (Long modelId, String hierarchyName) throws KDXException {
      try {
         return getHierarchyDAO().getHierarchyByNameForModel(modelId, hierarchyName);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   @Override
   public List<Hierarchy> getHierarchiesByConceptBEDIds (List<Long> conceptBEDIDs) throws KDXException {
      try {
         return getHierarchyDAO().getHierarchiesByConceptBEDIds(conceptBEDIDs);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   @Override
   public List<Application> getAllActiveStructuredApplicationsByPublishMode (PublishAssetMode publishMode)
            throws KDXException {
      try {
         return getApplicationDAO().getAllActiveStructuredApplicationsByPublishMode(publishMode);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   @Override
   public List<Application> getAllActiveStructuredEligibleApplicationsByUserId (Long userId,
            boolean skipOtherCommunityApps) throws KDXException {
      try {
         return getApplicationDAO().getAllActiveStructuredEligibleApplicationsByUserId(userId, skipOtherCommunityApps);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   @Override
   public List<Application> getApplicationsForByPublishModeOrderByRank (PublishAssetMode publishMode)
            throws KDXException {
      try {
         return getApplicationDAO().getApplicationsForByPublishModeOrderByRank(publishMode);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   @Override
   public List<Application> getAllEligibleApplicationsByUserId (Long userId, boolean skipOtherCommunityApps)
            throws KDXException {
      try {
         return getApplicationDAO().getAllEligibleApplicationsByUserId(userId, skipOtherCommunityApps);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

}