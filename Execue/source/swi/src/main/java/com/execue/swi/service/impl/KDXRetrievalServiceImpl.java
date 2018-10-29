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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.ContentCleanupPattern;
import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Behavior;
import com.execue.core.common.bean.entity.BusinessEntityByWord;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityVariation;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.DefaultInstanceValue;
import com.execue.core.common.bean.entity.EntityDetailType;
import com.execue.core.common.bean.entity.Hierarchy;
import com.execue.core.common.bean.entity.HierarchyDetail;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.ModelGroupMapping;
import com.execue.core.common.bean.entity.PossibleAttribute;
import com.execue.core.common.bean.entity.PossibleDetailType;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.entity.RISharedUserModelMapping;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.Rule;
import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.SFLTermToken;
import com.execue.core.common.bean.entity.SecondaryWord;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.bean.entity.Vertical;
import com.execue.core.common.bean.entity.VerticalAppExample;
import com.execue.core.common.bean.governor.BusinessEntityInfo;
import com.execue.core.common.bean.nlp.SFLInfo;
import com.execue.core.common.bean.swi.PopularityBusinessEntityDefinitionInfo;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.configuration.ILocationConfigurationService;
import com.execue.swi.dataaccess.IKDXDataAccessManager;
import com.execue.swi.dataaccess.IMappingDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IKDXRetrievalService;

public class KDXRetrievalServiceImpl implements IKDXRetrievalService {

   private IKDXDataAccessManager         kdxDataAccessManager;
   private IMappingDataAccessManager     mappingDataAccessManager;
   private ILocationConfigurationService locationConfigurationService;
   private ICoreConfigurationService     coreConfigurationService;

   private static final Logger           logger = Logger.getLogger(KDXRetrievalServiceImpl.class);

   @Override
   public Concept getConceptById (Long conceptId) throws KDXException {
      try {
         return getKdxDataAccessManager().getById(conceptId, Concept.class);
      } catch (SWIException swiException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, swiException);
      }
   }

   @Override
   public BusinessEntityDefinition getMappedConceptBEDForColumn (Long assetId, Long tableId, Long columnId)
            throws MappingException {
      BusinessEntityDefinition mappedConceptBed = null;
      Mapping mapping = getMappingDataAccessManager().getMappedConceptForColumn(assetId, tableId, columnId);
      if (mapping != null) {
         mappedConceptBed = mapping.getBusinessEntityDefinition();
         mappedConceptBed.getConcept().getName();
      }
      return mappedConceptBed;
   }

   @Override
   public Instance getInstanceById (Long instanceId) throws KDXException {
      try {
         return getKdxDataAccessManager().getById(instanceId, Instance.class);
      } catch (SWIException swiException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, swiException);
      }
   }

   @Override
   public Model getModelById (Long modelId) throws KDXException {
      try {
         return getKdxDataAccessManager().getById(modelId, Model.class);
      } catch (SWIException swiException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, swiException);
      }
   }

   @Override
   public Model getModelByUserModelGroupId (Long userModelGroupId) throws KDXException {
      return getKdxDataAccessManager().getModelByUserModelGroupId(userModelGroupId);
   }

   @Override
   public List<Stat> getAllStats () throws KDXException {
      return getKdxDataAccessManager().getAllStats();
   }

   @Override
   public Behavior getBehaviorById (Long behaviorId) throws KDXException {
      try {
         return getKdxDataAccessManager().getById(behaviorId, Behavior.class);
      } catch (SWIException swiException) {
         throw new KDXException(swiException.getCode(), swiException);
      }
   }

   @Override
   public List<Concept> getConcepts (Long modelId) throws KDXException {
      return getKdxDataAccessManager().getConcepts(modelId);
   }

   @Override
   public List<Instance> getInstances (Long modelId, Long conceptId) throws KDXException {
      return getKdxDataAccessManager().getInstances(modelId, conceptId);
   }

   @Override
   public List<Instance> getTypeInstances (Long modelId, Long typeId) throws KDXException {
      return getKdxDataAccessManager().getTypeInstances(modelId, typeId);
   }

   @Override
   public List<Instance> getInstancesByPage (Long modelId, Long conceptId, Page page) throws KDXException {
      return getKdxDataAccessManager().getInstancesByPage(modelId, conceptId, page);
   }

   @Override
   public Map<String, Instance> getInstanceDisplayNameMap (Long modelId, Long conceptId) throws KDXException {
      List<Instance> instances = getKdxDataAccessManager().getInstances(modelId, conceptId);
      Map<String, Instance> instancesMap = new HashMap<String, Instance>();
      if (CollectionUtils.isEmpty(instances)) {
         return instancesMap;
      }
      for (Instance instance : instances) {
         instancesMap.put(instance.getDisplayName(), instance);
      }
      return instancesMap;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXRetrievalService#getInstancesCount(java.lang.Long, java.lang.Long)
    */
   @Override
   public long getInstancesCount (Long modelId, Long conceptId) throws KDXException {
      return getKdxDataAccessManager().getInstancesCount(modelId, conceptId);
   }

   @Override
   public Concept getPopulatedConceptWithStats (Long conceptId) throws KDXException {
      Concept concept = getConceptById(conceptId);
      if (concept != null) {
         Set<Stat> statSet = concept.getStats();
         for (Stat stat : statSet) {
            stat.getStatType();
         }
      }
      return concept;
   }

   @Override
   public Concept getPopulatedConceptWithStats (Long modelId, String conceptName) throws KDXException {
      Concept concept = getConceptByName(modelId, conceptName);
      if (concept != null) {
         loadStats(concept);
      }
      return concept;
   }

   private void loadStats (Concept concept) {
      Set<Stat> statSet = concept.getStats();
      for (Stat stat : statSet) {
         stat.getStatType();
      }
   }

   @Override
   public Stat getStatByName (String name) throws KDXException {
      try {
         return getKdxDataAccessManager().getStatByName(name);
      } catch (SWIException swiException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, swiException);
      }
   }

   @Override
   public BusinessEntityDefinition getBusinessEntityDefinitionByIds (Long modelId, Long conceptId, Long instanceId)
            throws KDXException {
      try {
         BusinessEntityDefinition businessEntityDefinition = getKdxDataAccessManager()
                  .getBusinessEntityDefinitionByIds(modelId, conceptId, instanceId);
         if (businessEntityDefinition != null) {
            Type type = businessEntityDefinition.getType();
            type.getName();
            ModelGroup modelGroup = businessEntityDefinition.getModelGroup();
            if (modelGroup != null) {
               modelGroup.getId();
               Concept concept = businessEntityDefinition.getConcept();
               if (concept != null) {
                  concept.getName();
                  Instance instance = businessEntityDefinition.getInstance();
                  if (instance != null) {
                     instance.getName();
                  }
               }
            }
         }
         return businessEntityDefinition;
      } catch (SWIException swiException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, swiException);
      }
   }

   @Override
   public BusinessEntityDefinition getUnpopulatedBusinessEntityDefinitionByIds (Long modelId, Long conceptId,
            Long instanceId) throws KDXException {
      try {
         return getKdxDataAccessManager().getBusinessEntityDefinitionByIds(modelId, conceptId, instanceId);

      } catch (SWIException swiException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, swiException);
      }
   }

   @Override
   public BusinessEntityDefinition getBusinessEntityDefinitionByTypeIds (Long modelId, Long typeId, Long instanceId)
            throws KDXException {
      try {
         // TODO: SNM: This method can be removed, if we re-factor the existing getBusinessEntityDefinitionByIds method
         // to also handle the type id use case
         BusinessEntityDefinition businessEntityDefinition = getKdxDataAccessManager()
                  .getBusinessEntityDefinitionByTypeIds(modelId, typeId, instanceId);
         if (businessEntityDefinition != null) {
            Type type = businessEntityDefinition.getType();
            type.getName();
            ModelGroup modelGroup = businessEntityDefinition.getModelGroup();
            if (modelGroup != null) {
               modelGroup.getId();

               Instance instance = businessEntityDefinition.getInstance();
               if (instance != null) {
                  instance.getName();
               }
            }
         }
         return businessEntityDefinition;
      } catch (SWIException swiException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, swiException);
      }
   }

   @Override
   public BusinessEntityDefinition getBusinessEntityDefinitionByNames (String modelName, String conceptName,
            String instanceName) throws KDXException {
      try {
         BusinessEntityDefinition businessEntityDefinition = getBusinessEntityDefinitionWrapperByNames(modelName,
                  conceptName, instanceName);
         if (businessEntityDefinition != null) {
            Type type = businessEntityDefinition.getType();
            type.getName();
            ModelGroup modelGroup = businessEntityDefinition.getModelGroup();
            if (modelGroup != null) {
               modelGroup.getId();
               Concept concept = businessEntityDefinition.getConcept();
               if (concept != null) {
                  concept.getName();
                  Instance instance = businessEntityDefinition.getInstance();
                  if (instance != null) {
                     instance.getName();
                  }
               }
            }
         }
         return businessEntityDefinition;
      } catch (SWIException swiException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, swiException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXRetrievalService#getBusinessEntityDefinitionWrapperByNames(java.lang.String,
    *      java.lang.String, java.lang.String)
    */
   @Override
   public BusinessEntityDefinition getBusinessEntityDefinitionWrapperByNames (String modelName, String conceptName,
            String instanceName) throws KDXException {
      try {
         return getKdxDataAccessManager().getBusinessEntityDefinitionByNames(modelName, conceptName, instanceName);
      } catch (SWIException swiException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, swiException);
      }
   }

   @Override
   public BusinessEntityDefinition getBEDByMappingConceptName (Long modelId, Long mappingId, String conceptDisplayName)
            throws KDXException {
      return getKdxDataAccessManager().getBEDByMappingConceptName(modelId, mappingId, conceptDisplayName);
   }

   @Override
   public List<Concept> getConceptsBySearchString (Long modelId, String searchString) throws KDXException {
      return getKdxDataAccessManager().getConceptsBySearchString(modelId, searchString);
   }

   @Override
   public BusinessEntityDefinition getBusinessEntityDefinitionById (Long bedId) throws KDXException {
      try {
         BusinessEntityDefinition businessEntityDefinition = getKdxDataAccessManager().getById(bedId,
                  BusinessEntityDefinition.class);
         if (businessEntityDefinition != null) {
            ModelGroup modelGroup = businessEntityDefinition.getModelGroup();
            if (modelGroup != null) {
               modelGroup.getId();
               Type type = businessEntityDefinition.getType();
               if (type != null) {
                  type.getName();
               }
               Concept concept = businessEntityDefinition.getConcept();
               if (concept != null) {
                  concept.getName();
                  loadStats(concept);
                  Instance instance = businessEntityDefinition.getInstance();
                  if (instance != null) {
                     instance.getName();
                  }
                  InstanceProfile instanceProfile = businessEntityDefinition.getInstanceProfile();
                  if (instanceProfile != null) {
                     instanceProfile.getName();
                  }
               } else if (businessEntityDefinition.getConceptProfile() != null) {
                  businessEntityDefinition.getConceptProfile().getName();
               } else {
                  Relation relation = businessEntityDefinition.getRelation();
                  if (relation != null) {
                     relation.getName();
                  }
               }
            }
         }
         return businessEntityDefinition;
      } catch (KDXException kdxException) {
         throw kdxException;
      } catch (SWIException swiException) {
         throw new KDXException(swiException.getCode(), swiException);
      }
   }

   @Override
   public List<Instance> getInstancesOfConceptBySearchString (Long modelId, Long conceptId, String searchString)
            throws KDXException {
      return getKdxDataAccessManager().getInstancesOfConceptBySearchString(modelId, conceptId, searchString);
   }

   @Override
   public List<Concept> getConceptsForAsset (Long assetId) throws KDXException {
      return getKdxDataAccessManager().getConceptsForAsset(assetId);
   }

   @Override
   public List<Concept> getConceptsForAssetMetaInfo (Long assetId) throws KDXException {
      return getKdxDataAccessManager().getConceptsForAssetMetaInfo(assetId);
   }

   @Override
   public List<Concept> getMeasureConceptsForAsset (Long assetId) throws KDXException {
      return getKdxDataAccessManager().getMeasureConceptsForAsset(assetId);
   }

   @Override
   public Integer getMeasureConceptsCountForAsset (Long assetId) throws KDXException {
      return getKdxDataAccessManager().getMeasureConceptsCountForAsset(assetId);
   }

   @Override
   public BusinessEntityDefinition getBEDByInstanceDisplayName (String businessName, String conceptName,
            String instDisplayName) throws KDXException {
      BusinessEntityDefinition businessEntityDefinition = null;
      try {
         businessEntityDefinition = getKdxDataAccessManager().getBEDByInstanceDisplayName(businessName, conceptName,
                  instDisplayName);
         if (businessEntityDefinition != null) {
            Concept concept = businessEntityDefinition.getConcept();
            concept.getDisplayName();
            Instance instance = businessEntityDefinition.getInstance();
            instance.getDisplayName();
         }
      } catch (SWIException swiException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, swiException);
      }
      return businessEntityDefinition;
   }

   @Override
   public List<SecondaryWord> getAllSecondaryWords () throws KDXException {
      return getKdxDataAccessManager().getAllSecondaryWords();
   }

   @Override
   public List<RIParallelWord> getParallelWordsByLookupWord (String word) throws KDXException {
      return getKdxDataAccessManager().getParallelWordsByLookupWord(word);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXRetrievalService#getParallelWordsByLookupWords(java.util.List,
    *      java.util.Set)
    */
   @Override
   public List<RIParallelWord> getParallelWordsByLookupWords (List<String> words, Set<Long> modelGroupIds)
            throws KDXException {
      return getKdxDataAccessManager().getParallelWordsByLookupWords(words, modelGroupIds);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXRetrievalService#getParallelWordsByLookupWords(java.util.List)
    */
   @Override
   public List<RIParallelWord> getParallelWordsByLookupWords (List<String> words) throws KDXException {
      return getKdxDataAccessManager().getParallelWordsByLookupWords(words);
   }

   @Override
   public List<RIOntoTerm> getTermsByLookupWord (String word) throws KDXException {
      return getKdxDataAccessManager().getTermsByLookupWord(word);
   }

   @Override
   public List<RIOntoTerm> getTermsByLookupWordAndEntityType (String word, BusinessEntityType entityType)
            throws KDXException {
      return getKdxDataAccessManager().getTermsByLookupWordAndEntityType(word, entityType);
   }

   private List<RIOntoTerm> getTermsByLookupWords (List<String> words, boolean skipLocationTypeRecognition)
            throws KDXException {
      return getKdxDataAccessManager().getTermsByLookupWords(words, skipLocationTypeRecognition,
               getLocationChildTypeBedIds(skipLocationTypeRecognition));
   }

   @Override
   public List<RIOntoTerm> getTermsByLookupWordsAndEntityType (List<String> words, BusinessEntityType entityType)
            throws KDXException {
      return getKdxDataAccessManager().getTermsByLookupWordsAndEntityType(words, entityType);
   }

   @Override
   public Map<String, SecondaryWord> getAllSecondaryWordsMapForModel (Long modelId) throws KDXException {
      Map<String, SecondaryWord> secondaryWordsMap = new HashMap<String, SecondaryWord>(1);

      List<SecondaryWord> allSecondaryWordsForModel = getKdxDataAccessManager().getAllSecondaryWordsForModel(modelId);
      if (CollectionUtils.isEmpty(allSecondaryWordsForModel)) {
         return secondaryWordsMap;
      }
      for (SecondaryWord secondaryWord : allSecondaryWordsForModel) {
         secondaryWordsMap.put(secondaryWord.getWord().toLowerCase(), secondaryWord);
      }
      return secondaryWordsMap;
   }

   @Override
   public Map<String, Double> getAllSecondaryWordsWeightMapForModel (Long modelId) throws KDXException {
      Map<String, Double> secondaryWordsMap = new HashMap<String, Double>(1);

      List<SecondaryWord> allSecondaryWordsForModel = getKdxDataAccessManager().getAllSecondaryWordsForModel(modelId);
      if (CollectionUtils.isEmpty(allSecondaryWordsForModel)) {
         return secondaryWordsMap;
      }
      for (SecondaryWord secondaryWord : allSecondaryWordsForModel) {
         secondaryWordsMap.put(secondaryWord.getWord().toLowerCase(), secondaryWord.getDefaultWeight());
      }
      return secondaryWordsMap;
   }

   @Override
   public List<BusinessEntityDefinition> getRegularExpressionBasedInstances () throws KDXException {
      List<BusinessEntityDefinition> regExInstances = getKdxDataAccessManager().getRegularExpressionBasedInstances();
      Pattern compiledPattern = null;
      for (BusinessEntityDefinition businessEntityDefinition : regExInstances) {
         compiledPattern = Pattern.compile(businessEntityDefinition.getInstance().getExpression());
         businessEntityDefinition.getInstance().setCompiledRegexPattern(compiledPattern);
         if (businessEntityDefinition.getConcept() != null) {
            businessEntityDefinition.getConcept().getName();// For lazy loading
         }
         // purposes
         businessEntityDefinition.getType().getName();// For lazy loading
         // purposes
         businessEntityDefinition.getModelGroup().getName();
      }
      return regExInstances;
   }

   @Override
   public Map<String, BusinessEntityByWord> getBusinessEntityWordMapByLookupWords (List<String> words)
            throws KDXException {
      List<RIOntoTerm> riOntoTerms = getTermsByLookupWords(words, false);
      Map<String, BusinessEntityByWord> businessEntityWordMap = populateBusinessEntityWordMap(riOntoTerms);
      return businessEntityWordMap;
   }

   @Override
   public Map<String, List<RIOntoTerm>> getOntoTermsMapByLookupWords (List<String> words,
            boolean skipLocationTypeRecognition) throws KDXException {
      List<RIOntoTerm> riOntoTerms = getTermsByLookupWords(words, skipLocationTypeRecognition);
      return getRIOntoTermsMap(riOntoTerms);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.service.IKDXRetrievalService#getOntoTermsMapByLookupWords(java.util.List, java.util.List)
    */
   @Override
   public Map<String, List<RIOntoTerm>> getOntoTermsMapByLookupWords (List<String> words, List<Long> modelGroupIds,
            boolean skipLocationTypeRecognition) throws KDXException {
      List<RIOntoTerm> riOntoTerms = getTermsByLookupWords(words, modelGroupIds, skipLocationTypeRecognition);
      return getRIOntoTermsMap(riOntoTerms);
   }

   @Override
   public BusinessEntityByWord getBusinessEntityWordMapByLookupWord (String word) throws KDXException {
      List<RIOntoTerm> riOntoTerms = getTermsByLookupWord(word);
      BusinessEntityByWord businessEntityWord = populateBusinessEntityWord(riOntoTerms);
      return businessEntityWord;
   }

   @Override
   public SFLTerm getSFLTermByWord (String word) throws KDXException {
      return getKdxDataAccessManager().getSFLTermByWord(word);
   }

   @Override
   public SFLTerm getSFLTermById (Long sflTermId) throws KDXException {
      try {
         SFLTerm sflTerm = getKdxDataAccessManager().getById(sflTermId, SFLTerm.class);
         Set<SFLTermToken> tokens = sflTerm.getSflTermTokens();
         if (!CollectionUtils.isEmpty(tokens)) {
            for (SFLTermToken token : tokens) {
               token.getBusinessTermToken();
            }
         }
         return sflTerm;
      } catch (SWIException swiException) {
         throw new KDXException(swiException.getCode(), swiException);
      }
   }

   @Override
   public List<SFLTermToken> getSFLTermTokensByLookupWord (String word) throws KDXException {
      return getKdxDataAccessManager().getSFLTermTokensByLookupWord(word);
   }

   @Override
   public List<SFLTermToken> getSFLTermTokensByLookupWords (List<String> words) throws KDXException {
      return getKdxDataAccessManager().getSFLTermTokensByLookupWords(words);
   }

   @Override
   public List<SFLTermToken> getSFLTermTokensByTermId (Long sflTermId) throws KDXException {
      return getKdxDataAccessManager().getSFLTermTokensByTermId(sflTermId);
   }

   @Override
   public List<SFLTermToken> getFullSFLTermTokensByLookupWords (List<String> words) throws KDXException {
      List<SFLTermToken> termTokensByLookupWords = getKdxDataAccessManager().getSFLTermTokensByLookupWords(words);
      for (SFLTermToken termToken : termTokensByLookupWords) {
         termToken.getSflTerm();
         termToken.getSflTerm().getBusinessTerm();
      }
      return termTokensByLookupWords;
   }

   @Override
   public List<SFLInfo> getSFLInfoForWords (List<String> words, Set<Long> modelGroupIds) throws KDXException {
      boolean groupConcatDBSupported = getCoreConfigurationService().isGroupConcatDBSupported();
      return getKdxDataAccessManager().getSFLInfoForWords(words, modelGroupIds, groupConcatDBSupported);
   }

   @Override
   public DefaultInstanceValue getDefaultInstanceValue (Long businessEntityDefinitonId) throws KDXException {
      try {
         return getKdxDataAccessManager().getByField(businessEntityDefinitonId.toString(),
                  "businessEntityDefinitonId.id", DefaultInstanceValue.class);
      } catch (SWIException swiException) {
         throw new KDXException(swiException.getCode(), swiException);
      }
   }

   private BusinessEntityByWord populateBusinessEntityWord (List<RIOntoTerm> riOntoTerms) throws KDXException {
      BusinessEntityByWord businessEntityByWord = null;
      Map<String, BusinessEntityByWord> businessEntityWordMap = populateBusinessEntityWordMap(riOntoTerms);
      if (businessEntityWordMap.size() > 0) {
         businessEntityByWord = ((List<BusinessEntityByWord>) businessEntityWordMap.values()).get(0);
      }
      return businessEntityByWord;
   }

   private Map<String, List<RIOntoTerm>> getRIOntoTermsMap (List<RIOntoTerm> riOntoTermsList) {
      Map<String, List<RIOntoTerm>> riOntoTermsMap = new HashMap<String, List<RIOntoTerm>>();

      String keyWord = null;
      for (RIOntoTerm riOntoTerm : riOntoTermsList) {
         keyWord = riOntoTerm.getWord().toLowerCase().trim();
         List<RIOntoTerm> riOntoTerms = riOntoTermsMap.get(keyWord);
         if (riOntoTerms == null) {
            riOntoTerms = new ArrayList<RIOntoTerm>();
         }
         riOntoTerms.add(riOntoTerm);
         riOntoTermsMap.put(keyWord, riOntoTerms);
      }
      return riOntoTermsMap;
   }

   private Map<String, BusinessEntityByWord> populateBusinessEntityWordMap (List<RIOntoTerm> riOntoTerms)
            throws KDXException {
      Map<String, BusinessEntityByWord> businessEntityWordMap = new HashMap<String, BusinessEntityByWord>();

      String keyWord = null;
      String tempKeyWord = null;
      BusinessEntityType entityType = null;

      BusinessEntityByWord businessEntityByWord = new BusinessEntityByWord();

      for (RIOntoTerm riOntoTerm : riOntoTerms) {

         keyWord = riOntoTerm.getWord();

         tempKeyWord = keyWord.toLowerCase().trim();
         businessEntityByWord = businessEntityWordMap.get(tempKeyWord);
         if (businessEntityByWord == null) {
            businessEntityByWord = new BusinessEntityByWord();
            businessEntityByWord.setWord(keyWord);
         }
         businessEntityWordMap.put(businessEntityByWord.getWord().toLowerCase().trim(), businessEntityByWord);

         /*
          * if (!keyWord.equalsIgnoreCase(businessEntityByWord.getWord())) { if
          * (!StringUtils.isBlank(businessEntityByWord.getWord())) {
          * businessEntityWordMap.put(businessEntityByWord.getWord().toLowerCase().trim(), businessEntityByWord); }
          * businessEntityByWord = new DomainEntityByWord(); businessEntityByWord.setWord(keyWord); }
          */

         entityType = riOntoTerm.getEntityType();
         BusinessEntityDefinition bed = new BusinessEntityDefinition();
         Concept concept = null;
         Instance instance = null;
         Relation relation = null;
         ConceptProfile conceptProfile = null;
         InstanceProfile instanceProfile = null;

         ModelGroup modelGroup = new ModelGroup();
         modelGroup.setId(riOntoTerm.getModelGroupId());
         bed.setModelGroup(modelGroup);

         if (BusinessEntityType.RELATION.equals(entityType)) {

            relation = new Relation();
            relation.setName(riOntoTerm.getRelationName());
            relation.setId(riOntoTerm.getRelationBEDID());

            bed.setRelation(relation);

            bed.setRecognitionType(riOntoTerm.getWordType());
            bed.setId(riOntoTerm.getRelationBEDID());
            bed.setEntityType(entityType);

            businessEntityByWord.getRelations().add(bed);
         } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(entityType)) {
            instance = new Instance();
            instance.setName(riOntoTerm.getInstanceName());
            instance.setId(riOntoTerm.getInstanceBEDID());

            concept = new Concept();
            concept.setName(riOntoTerm.getConceptName());
            concept.setId(riOntoTerm.getConceptBEDID());

            bed.setInstance(instance);
            bed.setConcept(concept);

            bed.setRecognitionType(riOntoTerm.getWordType());
            bed.setId(riOntoTerm.getInstanceBEDID());
            bed.setEntityType(entityType);

            businessEntityByWord.getInstances().add(bed);
         } else if (BusinessEntityType.CONCEPT_PROFILE.equals(entityType)) {

            conceptProfile = new ConceptProfile();
            conceptProfile.setName(riOntoTerm.getProfileName());
            conceptProfile.setId(riOntoTerm.getProfileBEDID());

            concept = new Concept();
            concept.setName(riOntoTerm.getConceptName());
            concept.setId(riOntoTerm.getConceptBEDID());

            bed.setConceptProfile(conceptProfile);
            bed.setConcept(concept);

            bed.setRecognitionType(riOntoTerm.getWordType());
            bed.setId(riOntoTerm.getProfileBEDID());
            bed.setEntityType(entityType);

            businessEntityByWord.getProfiles().add(bed);
         } else if (BusinessEntityType.INSTANCE_PROFILE.equals(entityType)) {

            instanceProfile = new InstanceProfile();
            instanceProfile.setName(riOntoTerm.getProfileName());
            instanceProfile.setId(riOntoTerm.getProfileBEDID());

            concept = new Concept();
            concept.setName(riOntoTerm.getConceptName());
            concept.setId(riOntoTerm.getConceptBEDID());

            bed.setInstanceProfile(instanceProfile);
            bed.setConcept(concept);

            bed.setRecognitionType(riOntoTerm.getWordType());
            bed.setId(riOntoTerm.getProfileBEDID());
            bed.setEntityType(entityType);

            businessEntityByWord.getProfiles().add(bed);
         } else if (BusinessEntityType.CONCEPT.equals(entityType)) {
            concept = new Concept();
            concept.setName(riOntoTerm.getConceptName());
            concept.setId(riOntoTerm.getConceptBEDID());

            bed.setConcept(concept);

            bed.setRecognitionType(riOntoTerm.getWordType());
            bed.setId(riOntoTerm.getConceptBEDID());
            bed.setEntityType(entityType);

            businessEntityByWord.getConcepts().add(bed);
         } else if (BusinessEntityType.CONCEPT_RELATION.equals(entityType)) {

            relation = new Relation();
            relation.setName(riOntoTerm.getRelationName());
            relation.setId(riOntoTerm.getRelationBEDID());

            concept = new Concept();
            concept.setName(riOntoTerm.getConceptName());
            concept.setId(riOntoTerm.getConceptBEDID());

            bed.setRelation(relation);
            bed.setConcept(concept);

            bed.setRecognitionType(riOntoTerm.getWordType());
            bed.setId(riOntoTerm.getRelationBEDID());
            bed.setEntityType(entityType);

            businessEntityByWord.getRelations().add(bed);
         } else {
            throw new KDXException(SWIExceptionCodes.UNKNOWN_BUSINESS_ENTITY_TYPE, "Unknown Business Entity Type ["
                     + entityType + "]");
         }
      }
      /*
       * if (!StringUtils.isBlank(businessEntityByWord.getWord())) {
       * businessEntityWordMap.put(businessEntityByWord.getWord().toLowerCase().trim(), businessEntityByWord); }
       */
      return businessEntityWordMap;
   }

   @Override
   public Stat getStatByBusinessEntityId (Long businessEntityDefinitionId) throws KDXException {
      return getKdxDataAccessManager().getStatByBusinessEntityId(businessEntityDefinitionId);
   }

   @Override
   public Mapping getMappingById (Long mappingId) throws KDXException {
      try {
         return getKdxDataAccessManager().getById(mappingId, Mapping.class);
      } catch (SWIException swiException) {
         throw new KDXException(swiException.getCode(), swiException);
      }
   }

   @Override
   public Type getTypeByName (String typeName) throws KDXException {
      return getKdxDataAccessManager().getTypeByName(typeName);
   }

   @Override
   public BusinessEntityDefinition getTypeBusinessEntityDefinition (Long typeId) throws KDXException {
      return getKdxDataAccessManager().getTypeBusinessEntityDefinition(typeId);
   }

   @Override
   public BusinessEntityDefinition getPopulatedTypeBusinessEntityDefinition (Long typeId) throws KDXException {
      BusinessEntityDefinition typeBusinessEntityDefinition = getKdxDataAccessManager()
               .getTypeBusinessEntityDefinition(typeId);
      Type type = typeBusinessEntityDefinition.getType();
      type.getName();
      return typeBusinessEntityDefinition;
   }

   @Override
   public Concept getConceptByDisplayName (Long modelId, String conceptDisplayName) throws KDXException {
      return getKdxDataAccessManager().getConceptByDisplayName(modelId, conceptDisplayName);
   }

   @Override
   public Concept getConceptByName (Long modelId, String conceptName) throws KDXException {
      return getKdxDataAccessManager().getConceptByName(modelId, conceptName);
   }

   public Relation getRelationByName (Long modelId, String relationName, boolean includeBaseModelGroup)
            throws KDXException {
      return getKdxDataAccessManager().getRelationByName(modelId, relationName, includeBaseModelGroup);
   }

   @Override
   public BusinessEntityDefinition getRelationBEDByName (Long modelId, String relationName) throws KDXException {
      return getKdxDataAccessManager().getRelationBEDByName(modelId, relationName);
   }

   @Override
   public BusinessEntityDefinition getRelationBEDByNameIncludingBaseGroup (Long modelId, String relationName)
            throws KDXException {
      BusinessEntityDefinition relationBed = getKdxDataAccessManager().getRelationBEDByNameIncludingBaseGroup(modelId,
               relationName);
      Relation relation = relationBed.getRelation();
      relation.getName();
      return relationBed;
   }

   @Override
   public BusinessEntityDefinition getConceptBEDByName (Long modelId, String conceptName) throws KDXException {
      return getKdxDataAccessManager().getConceptBEDByName(modelId, conceptName);
   }

   @Override
   public BusinessEntityDefinition getInstanceBEDByName (Long modelId, String conceptName, String instanceName)
            throws KDXException {
      BusinessEntityDefinition instanceBed = getKdxDataAccessManager().getInstanceBEDByName(modelId, conceptName,
               instanceName);
      instanceBed.getInstance().getName();
      return instanceBed;
   }

   @Override
   public BusinessEntityDefinition getInstanceBEDByDisplayName (Long modelId, String conceptName,
            String instanceDisplayName) throws KDXException {
      return getKdxDataAccessManager().getInstanceBEDByDisplayName(modelId, conceptName, instanceDisplayName);
   }

   @SuppressWarnings ("unchecked")
   @Override
   public Map<Long, String> getDisplayNamesByBEDIds (List<Long> bedIds) throws KDXException {
      Map<Long, String> displayNameMapByBEDId = new HashMap<Long, String>();
      try {
         String displayName = null;
         List<BusinessEntityDefinition> beds = (List<BusinessEntityDefinition>) getKdxDataAccessManager().getByIds(
                  bedIds, BusinessEntityDefinition.class);
         for (BusinessEntityDefinition businessEntityDefinition : beds) {
            if (BusinessEntityType.CONCEPT.equals(businessEntityDefinition.getEntityType())) {
               displayName = businessEntityDefinition.getConcept().getDisplayName();
            } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(businessEntityDefinition.getEntityType())
                     || BusinessEntityType.REGEX_INSTANCE.equals(businessEntityDefinition.getEntityType())) {
               displayName = businessEntityDefinition.getInstance().getDisplayName();
            } else if (BusinessEntityType.RELATION.equals(businessEntityDefinition.getEntityType())) {
               displayName = businessEntityDefinition.getRelation().getDisplayName();
            } else if (BusinessEntityType.CONCEPT_PROFILE.equals(businessEntityDefinition.getEntityType())) {
               displayName = businessEntityDefinition.getConceptProfile().getDisplayName();
            } else if (BusinessEntityType.INSTANCE_PROFILE.equals(businessEntityDefinition.getEntityType())) {
               displayName = businessEntityDefinition.getInstanceProfile().getDisplayName();
            }
            displayNameMapByBEDId.put(businessEntityDefinition.getId(), displayName);
         }
      } catch (SWIException swiException) {
         throw new KDXException(swiException.getCode(), swiException);
      }
      return displayNameMapByBEDId;
   }

   @Override
   public Relation getRelationByID (Long resourceID) throws KDXException {
      try {
         return getKdxDataAccessManager().getById(resourceID, Relation.class);
      } catch (SWIException swiException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, swiException);
      }
   }

   @Override
   public List<Instance> getInstancesByDisplayNameAcrossAllConcepts (Long modelId, String instanceDisplayName)
            throws KDXException {
      try {
         return getKdxDataAccessManager().getInstancesByDisplayNameAcrossAllConcepts(modelId, instanceDisplayName);
      } catch (KDXException kdxException) {
         throw kdxException;
      }
   }

   @Override
   public String getInstanceNameByBedId (Long bedId) throws KDXException {
      Instance instance = getBusinessEntityDefinitionById(bedId).getInstance();
      if (instance != null) {
         return instance.getDisplayName() == null ? instance.getName() : instance.getDisplayName();
      }
      return null;
   }

   @Override
   public Instance getLatestInstanceInserted (Long modelId, Long conceptId) throws KDXException {
      return getKdxDataAccessManager().getLatestInstanceInserted(modelId, conceptId);
   }

   @Override
   public List<SFLTerm> getAllSFLTerms () throws KDXException {
      return getKdxDataAccessManager().getAllSFLTerms();
   }

   @Override
   public boolean doesSFLTermExist (String sflTerm) throws KDXException {
      return getKdxDataAccessManager().getSFLTerms(sflTerm).size() != 0;
   }

   @Override
   public List<BusinessEntityInfo> getAllConceptBusinessEntities (Long modelId) throws KDXException {
      return getKdxDataAccessManager().getAllConceptsBusinessEntities(modelId);
   }

   @Override
   public List<BusinessEntityInfo> getAllConceptProfileBusinessEntities (Long modelId) throws KDXException {
      return getKdxDataAccessManager().getAllConceptProfileBusinessEntities(modelId);
   }

   @Override
   public List<BusinessEntityInfo> getAllInstanceBusinessEntities (Long modelId) throws KDXException {
      return getKdxDataAccessManager().getAllInstanceBusinessEntities(modelId);
   }

   @Override
   public List<BusinessEntityInfo> getAllInstanceProfileBusinessEntities (Long modelId) throws KDXException {
      return getKdxDataAccessManager().getAllInstanceProfileBusinessEntities(modelId);
   }

   @Override
   public List<BusinessEntityInfo> getAllRelationBusinessEntities (Long modelId) throws KDXException {
      return getKdxDataAccessManager().getAllRelationBusinessEntities(modelId);
   }

   public Long isExactBusinessTerm (Long modelId, String inputString) throws KDXException {
      boolean isTermFound = false;
      Long businessEntityDefinitionId = null;
      businessEntityDefinitionId = checkWithConceptName(modelId, inputString);
      if (businessEntityDefinitionId != null) {
         isTermFound = true;
      }
      // we will not check further if isTermFound is true
      if (!isTermFound) {
         businessEntityDefinitionId = checkWithConceptProfileName(modelId, inputString);
         if (businessEntityDefinitionId != null) {
            isTermFound = true;
         }
      }
      if (!isTermFound) {
         businessEntityDefinitionId = checkWithInstancProfileName(modelId, inputString);
         if (businessEntityDefinitionId != null) {
            isTermFound = true;
         }
      }
      if (!isTermFound) {
         businessEntityDefinitionId = checkWithRelationName(modelId, inputString);
         if (businessEntityDefinitionId != null) {
            isTermFound = true;
         }
      }
      if (!isTermFound) {
         businessEntityDefinitionId = checkWithInstanceName(modelId, inputString);
         if (businessEntityDefinitionId != null) {
            isTermFound = true;
         }
      }
      return businessEntityDefinitionId;
   }

   private Long checkWithInstanceName (Long modelId, String inputString) throws KDXException {
      logger.debug("check with the InstanceName");
      Long businessEntityDefId = null;
      List<BusinessEntityInfo> instances = getAllInstanceBusinessEntities(modelId);
      for (BusinessEntityInfo instance : instances) {
         if (instance.getBusinessEntityTermDisplayName().equalsIgnoreCase(inputString)) {
            businessEntityDefId = instance.getBusinessEntityTermId();
            logger.debug("keyword name matched with of Instance name and Id:" + businessEntityDefId);
            break;
         }
      }
      return businessEntityDefId;
   }

   private Long checkWithRelationName (Long modelId, String inputString) throws KDXException {
      logger.debug("check with the Relation");
      Long businessEntityDefId = null;
      List<BusinessEntityInfo> relations = getAllRelationBusinessEntities(modelId);
      for (BusinessEntityInfo relation : relations) {
         if (relation.getBusinessEntityTermDisplayName().equalsIgnoreCase(inputString)) {
            businessEntityDefId = relation.getBusinessEntityTermId();
            logger.debug("keyword name matched with of Relation  and Id:" + businessEntityDefId);
            break;
         }
      }
      return businessEntityDefId;
   }

   private Long checkWithInstancProfileName (Long modelId, String inputString) throws KDXException {
      logger.debug("check with the Instance Profile name");
      Long businessEntityDefId = null;
      List<BusinessEntityInfo> instanceProfiles = getAllInstanceProfileBusinessEntities(modelId);
      for (BusinessEntityInfo instanceProfile : instanceProfiles) {
         if (instanceProfile.getBusinessEntityTermDisplayName().equalsIgnoreCase(inputString)) {
            businessEntityDefId = instanceProfile.getBusinessEntityTermId();
            logger.debug("keyword name matched with of InstanceProfile Name and Id:" + businessEntityDefId);
            break;
         }
      }
      return businessEntityDefId;
   }

   private Long checkWithConceptProfileName (Long modelId, String inputString) throws KDXException {
      logger.debug("check with the Concept Profile name");
      Long businessEntityDefId = null;
      List<BusinessEntityInfo> conceptProfiles = getAllConceptProfileBusinessEntities(modelId);
      for (BusinessEntityInfo conceptProfile : conceptProfiles) {
         if (conceptProfile.getBusinessEntityTermDisplayName().equalsIgnoreCase(inputString)) {
            businessEntityDefId = conceptProfile.getBusinessEntityTermId();
            logger.debug("keyword name matched with of ConceptProfile Name and Id:"
                     + conceptProfile.getBusinessEntityTermId());
            break;
         }
      }
      return businessEntityDefId;
   }

   private Long checkWithConceptName (Long modelId, String inputString) throws KDXException {
      logger.debug("check with the Concept name");
      Long businessEntityDefId = null;
      List<BusinessEntityInfo> concepts = getAllConceptBusinessEntities(modelId);
      for (BusinessEntityInfo concept : concepts) {
         if (concept.getBusinessEntityTermDisplayName().equalsIgnoreCase(inputString)) {
            businessEntityDefId = concept.getBusinessEntityTermId();
            logger.debug("keyword name matched with of Concept Name and Id:" + businessEntityDefId);
            break;
         }
      }
      return businessEntityDefId;
   }

   @Override
   public boolean isPartOfBusinessTerm (Long modelId, String inputString) throws KDXException {
      boolean isPartOfBusinessTerm = false;
      List<BusinessEntityInfo> concepts = getAllConceptBusinessEntities(modelId);
      outer: for (BusinessEntityInfo businessEntityInfo : concepts) {
         Scanner scanner = new Scanner(businessEntityInfo.getBusinessEntityTermDisplayName());
         while (scanner.hasNext()) {
            if (scanner.next().equalsIgnoreCase(inputString)) {
               logger.debug("keyword is a part of Concept Name");
               isPartOfBusinessTerm = true;
               break outer;
            }
         }
      }
      if (!isPartOfBusinessTerm) {
         logger.debug("Check whether kewword is part of ConceptProfile name");
         List<BusinessEntityInfo> conceptProfiles = getAllConceptProfileBusinessEntities(modelId);
         outer: for (BusinessEntityInfo businessEntityInfo : conceptProfiles) {
            Scanner scanner = new Scanner(businessEntityInfo.getBusinessEntityTermDisplayName());
            while (scanner.hasNext()) {
               if (scanner.next().equalsIgnoreCase(inputString)) {
                  logger.debug("keyword is a part of ConceptProfile Name");
                  isPartOfBusinessTerm = true;
                  break outer;
               }
            }
         }
      }
      if (!isPartOfBusinessTerm) {
         logger.debug("Check whether kewword is part of InstanceProfile name");
         List<BusinessEntityInfo> instanceProfiles = getAllInstanceProfileBusinessEntities(modelId);
         outer: for (BusinessEntityInfo businessEntityInfo : instanceProfiles) {
            Scanner scanner = new Scanner(businessEntityInfo.getBusinessEntityTermDisplayName());
            while (scanner.hasNext()) {
               if (scanner.next().equalsIgnoreCase(inputString)) {
                  logger.debug("keyword is a part of InstanceProfile Name");
                  isPartOfBusinessTerm = true;
                  break outer;
               }
            }
         }
      }
      if (!isPartOfBusinessTerm) {
         logger.debug("Check whether kewword is part of Relation name");
         List<BusinessEntityInfo> relations = getAllRelationBusinessEntities(modelId);
         outer: for (BusinessEntityInfo businessEntityInfo : relations) {
            Scanner scanner = new Scanner(businessEntityInfo.getBusinessEntityTermDisplayName());
            while (scanner.hasNext()) {
               if (scanner.next().equalsIgnoreCase(inputString)) {
                  isPartOfBusinessTerm = true;
                  logger.debug("keyword is a part of relation Name");
                  break outer;
               }
            }
         }
      }
      if (!isPartOfBusinessTerm) {
         logger.debug("Check whether kewword is part of Instance name");
         List<BusinessEntityInfo> instances = getAllInstanceBusinessEntities(modelId);
         outer: for (BusinessEntityInfo businessEntityInfo : instances) {
            Scanner scanner = new Scanner(businessEntityInfo.getBusinessEntityTermDisplayName());
            while (scanner.hasNext()) {
               if (scanner.next().equalsIgnoreCase(inputString)) {
                  logger.debug("keyword is a part of Instance Name");
                  isPartOfBusinessTerm = true;
                  break outer;
               }
            }
         }
      }
      return isPartOfBusinessTerm;
   }

   @Override
   public ModelGroup getPrimaryGroup (Long modelId) throws KDXException {
      return getKdxDataAccessManager().getPrimaryGroup(modelId);
   }

   @Override
   public boolean isModelExist (String modelName) throws KDXException {
      return getKdxDataAccessManager().isModelExist(modelName);
   }

   @Override
   public boolean isCloudExist (String cloudName) throws KDXException {
      return getKdxDataAccessManager().isCloudExist(cloudName);
   }

   @Override
   public List<Model> getModelsByApplicationId (Long applicationId) throws KDXException {
      return getKdxDataAccessManager().getModelsByApplicationId(applicationId);
   }

   @Override
   public void updateSFLTermTokens (List<SFLTermToken> termTokens) throws KDXException {
      getKdxDataAccessManager().updateSFLTermTokens(termTokens);
   }

   @Override
   public List<SFLTerm> getSFLTermsForKeyWord (String keyWord) throws KDXException {
      return getKdxDataAccessManager().getSFLTermsForKeyWord(keyWord);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXRetrievalService#getTermsByLookupWordAndEntityTypeInModel(java.lang.String,
    *      com.execue.core.common.type.BusinessEntityType, com.execue.core.common.bean.entity.Model)
    */
   @Override
   public List<RIOntoTerm> getTermsByLookupWordsAndEntityTypeInModel (List<String> words,
            BusinessEntityType entityType, Model model) throws KDXException {
      List<RIOntoTerm> ontoTerms = new ArrayList<RIOntoTerm>();
      for (ModelGroup group : model.getModelGroups()) {
         List<RIOntoTerm> groupTerms = new ArrayList<RIOntoTerm>();
         groupTerms = getKdxDataAccessManager().getTermsByLookupWordsAndEntityTypeInGroup(words, entityType,
                  group.getId());
         if (!CollectionUtils.isEmpty(groupTerms)) {
            ontoTerms.addAll(groupTerms);
         }

      }
      return ontoTerms;
   }

   @Override
   public List<Instance> getInstancesForAssetMetaInfo (Long modelId, Long conceptId) throws KDXException {
      return getKdxDataAccessManager().getInstancesForAssetMetaInfo(modelId, conceptId);

   }

   @Override
   public List<Model> getModelsByGroupIds (Set<Long> groupIds) throws KDXException {
      return getKdxDataAccessManager().getModelByGroupIds(groupIds);
   }

   @Override
   public List<Concept> getConcepts (List<ModelGroup> userModelGroups) throws KDXException {
      return getKdxDataAccessManager().getConcepts(userModelGroups);
   }

   @Override
   public List<Relation> getRelations (List<ModelGroup> userModelGroups) throws KDXException {
      return getKdxDataAccessManager().getRelations(userModelGroups);
   }

   @Override
   public List<Relation> getRelationsByModel (Long modelId) throws KDXException {
      return getKdxDataAccessManager().getRelationsByModel(modelId);
   }

   @Override
   public List<ModelGroupMapping> getPopulatedModelGroupMapping (Long modelId) throws KDXException {
      List<ModelGroupMapping> modelGroupMappings = getKdxDataAccessManager().getPopulatedModelGroupMapping(modelId);
      for (ModelGroupMapping modelGroupMapping : modelGroupMappings) {
         Model model = modelGroupMapping.getModel();
         model.getName();
         ModelGroup modelGroup = modelGroupMapping.getModelGroup();
         modelGroup.getName();
      }
      return modelGroupMappings;
   }

   @Override
   public List<BusinessEntityDefinition> getInstanceBEDsByConceptId (long modelId, long conceptId) throws KDXException {
      List<BusinessEntityDefinition> businessEntityDefinitions = getKdxDataAccessManager().getInstanceBEDsByConceptId(
               modelId, conceptId);
      for (BusinessEntityDefinition businessEntityDefinition : businessEntityDefinitions) {
         if (businessEntityDefinition != null) {
            ModelGroup modelGroup = businessEntityDefinition.getModelGroup();
            if (modelGroup != null) {
               modelGroup.getId();
               Concept concept = businessEntityDefinition.getConcept();
               if (concept != null) {
                  concept.getName();
                  Instance instance = businessEntityDefinition.getInstance();
                  if (instance != null) {
                     instance.getName();
                  }
               }
            }
         }
      }
      return businessEntityDefinitions;
   }

   @Override
   public List<Long> getInstanceBEDIdsByConceptId (Long modelId, Long conceptId) throws KDXException {
      return getKdxDataAccessManager().getInstanceBEDIdsByConceptId(modelId, conceptId);
   }

   @Override
   public Model getModelByName (String modelName) throws KDXException {
      return getKdxDataAccessManager().getModelByName(modelName);
   }

   @Override
   public List<PopularityBusinessEntityDefinitionInfo> getPopularityInfoForModelGroup (Long modelGroupId)
            throws KDXException {
      return getKdxDataAccessManager().getPopularityInfoForModelGroup(modelGroupId);
   }

   @Override
   public List<Long> getSFLTermIdsTobeDeleted () throws KDXException {
      return getKdxDataAccessManager().getSFLTermIdsTobeDeleted();
   }

   @Override
   public List<Long> getAllSFLTermIds () throws KDXException {
      return getKdxDataAccessManager().getAllSFLTermIds();
   }

   @Override
   public List<SFLTerm> getPopulatedSFLTerms (List<Long> sflTermIds) throws KDXException {
      return getKdxDataAccessManager().getPopulatedSFLTerms(sflTermIds);
   }

   @Override
   public List<Long> getSFLTermIdsForNonZeroHits () throws KDXException {
      return getKdxDataAccessManager().getSFLTermIdsForNonZeroHits();
   }

   @Override
   public List<BusinessEntityDefinition> getConceptBEDsOfModel (Long modelId) throws KDXException {
      List<BusinessEntityDefinition> conceptBEDList = getKdxDataAccessManager().getConceptBEDsOfModel(modelId);
      if (ExecueCoreUtil.isCollectionNotEmpty(conceptBEDList)) {
         for (BusinessEntityDefinition businessEntityDefinition : conceptBEDList) {
            if (businessEntityDefinition != null) {
               ModelGroup modelGroup = businessEntityDefinition.getModelGroup();
               if (modelGroup != null) {
                  modelGroup.getId();
                  Concept concept = businessEntityDefinition.getConcept();
                  if (concept != null) {
                     concept.getName();
                     Instance instance = businessEntityDefinition.getInstance();
                     if (instance != null) {
                        instance.getName();
                     }
                  }
               }
            }
         }
      }
      return conceptBEDList;
   }

   @Override
   public List<SFLTerm> getOrphanSFLTerms (Long modelGroupId) throws KDXException {
      return getKdxDataAccessManager().getOrphanSFLTerms(modelGroupId);
   }

   @Override
   public List<RIOntoTerm> getRIOntoTermsByBEDId (Long bedId, BusinessEntityType type) throws KDXException {
      return getKdxDataAccessManager().getRIOntoTermsByBEDId(bedId, type);
   }

   @Override
   public Long getInstanceBEDsCountByConceptId (Long conceptId) throws KDXException {
      return getKdxDataAccessManager().getInstanceBEDsCountByConceptId(conceptId);
   }

   @Override
   public List<BusinessEntityDefinition> getInstanceBEDsByPage (Long conceptId, Long pageNumber, Long pageSize)
            throws KDXException {
      List<BusinessEntityDefinition> businessEntityDefinitions = getKdxDataAccessManager().getInstanceBEDsByPage(
               conceptId, pageNumber, pageSize);
      for (BusinessEntityDefinition businessEntityDefinition : businessEntityDefinitions) {
         if (businessEntityDefinition != null) {
            ModelGroup modelGroup = businessEntityDefinition.getModelGroup();
            if (modelGroup != null) {
               modelGroup.getId();
               Concept concept = businessEntityDefinition.getConcept();
               if (concept != null) {
                  concept.getName();
                  Instance instance = businessEntityDefinition.getInstance();
                  if (instance != null) {
                     instance.getName();
                  }
               }
            }
         }
      }
      return businessEntityDefinitions;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXRetrievalService#getTermsByLookupWordAndTypeBedId(java.lang.String,
    *      java.lang.Long)
    */
   @Override
   public List<RIOntoTerm> getTermsByLookupWordAndTypeBedId (String word, Long typeBedId) throws KDXException {
      return getKdxDataAccessManager().getTermsByLookupWordAndTypeBedId(word, typeBedId);
   }

   @Override
   public List<RIOntoTerm> getConceptTermsByTypeBedIdAndModelGroupId (Long typeBedId, Long modelGroupId)
            throws KDXException {
      return getKdxDataAccessManager().getConceptTermsByTypeBedIdAndModelGroupId(typeBedId, modelGroupId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXRetrievalService#getAllPossibleBehavior(java.lang.Long)
    */
   @Override
   public List<BehaviorType> getAllPossibleBehavior (Long typeBeId) throws KDXException {
      return getKdxDataAccessManager().getAllPossibleBehavior(typeBeId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXRetrievalService#getAllBehaviorForEntity(java.lang.Long)
    */
   @Override
   public List<BehaviorType> getAllBehaviorForEntity (Long entityBedId) throws KDXException {
      return getKdxDataAccessManager().getAllBehaviorForEntity(entityBedId);
   }

   @Override
   public List<RIOntoTerm> getTermsByLookupWordAndConceptBedId (String word, Long conceptBedId) throws KDXException {
      return getKdxDataAccessManager().getTermsByLookupWordAndConceptBedId(word, conceptBedId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXRetrievalService#getPossibleAttributesByIds(java.util.List)
    */
   @Override
   public List<PossibleAttribute> getPossibleAttributesByIds (List<Long> possibleAttributeIds) throws KDXException {
      List<PossibleAttribute> possibleAttributes = getKdxDataAccessManager().getPossibleAttributesByIds(
               possibleAttributeIds);
      navigatePossibleAttributes(possibleAttributes);
      return possibleAttributes;
   }

   @Override
   public List<PossibleAttribute> getAllPossibleAttributes (Long typeBedId) throws KDXException {
      List<PossibleAttribute> possibleAttributes = getKdxDataAccessManager().getAllPossibleAttributes(typeBedId);
      navigatePossibleAttributes(possibleAttributes);
      return possibleAttributes;
   }

   private void navigatePossibleAttributes (List<PossibleAttribute> possibleAttributes) {
      if (ExecueCoreUtil.isCollectionNotEmpty(possibleAttributes)) {
         for (PossibleAttribute possibleAttribute : possibleAttributes) {
            BusinessEntityDefinition componentTypeBED = possibleAttribute.getComponentTypeBed();
            if (componentTypeBED != null) {
               Type sourceType = componentTypeBED.getType();
               sourceType.getName();
               if (BusinessEntityType.REALIZED_TYPE.equals(componentTypeBED.getEntityType())) {
                  componentTypeBED.getConcept().getName();
               }
               Concept sourceConcept = componentTypeBED.getConcept();
               if (sourceConcept != null) {
                  sourceConcept.getName();
               }
            }
            BusinessEntityDefinition relationBed = possibleAttribute.getRelationBed();
            if (relationBed != null) {
               relationBed.getRelation();
               relationBed.getRelation().getName();
            }
            BusinessEntityDefinition typeBED = possibleAttribute.getTypeBed();
            if (typeBED != null) {
               Type sourceType = typeBED.getType();
               sourceType.getName();
            }
            Set<Rule> rules = possibleAttribute.getRules();
            for (Rule rule : rules) {
               rule.getName();
               Set<PossibleAttribute> possibleAttributes2 = rule.getPossibleAttributes();
               for (PossibleAttribute possibleAttribute2 : possibleAttributes2) {
                  possibleAttribute2.getRelationBed();
               }
            }
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXRetrievalService#getTypeBedByName(java.lang.String)
    */
   @Override
   public BusinessEntityDefinition getTypeBedByName (String typeName) throws KDXException {
      return getTypeBedByName(typeName, false);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXRetrievalService#getTypeBedByName(java.lang.String, boolean)
    */
   @Override
   public BusinessEntityDefinition getTypeBedByName (String typeName, boolean isRealizedType) throws KDXException {

      BusinessEntityDefinition businessEntityDefinition = getKdxDataAccessManager().getTypeBedByName(typeName,
               isRealizedType);
      if (businessEntityDefinition != null) {
         Type type = businessEntityDefinition.getType();
         type.getName();
      }
      return businessEntityDefinition;
   }

   @Override
   public BusinessEntityDefinition getTypeBedByTypeID (Long typeID) throws KDXException {
      return getTypeBedByTypeID(typeID, false);
   }

   @Override
   public BusinessEntityDefinition getTypeBedByTypeID (Long typeID, boolean isRealizedType) throws KDXException {
      return getKdxDataAccessManager().getTypeBedByTypeID(typeID, isRealizedType);
   }

   @Override
   public List<BusinessEntityDefinition> getRealizationsForTypeInModelIncludingBaseGroup (Long typeId, Long modelId)
            throws KDXException {
      return getKdxDataAccessManager().getRealizationsForTypeInModelIncludingBaseGroup(typeId, modelId);
   }

   @Override
   public List<BusinessEntityDefinition> getPopulatedRealizationsForTypeInModelIncludingBaseGroup (Long typeId,
            Long modelGroupId) throws KDXException {
      List<BusinessEntityDefinition> realizations = getKdxDataAccessManager()
               .getRealizationsForTypeInModelIncludingBaseGroup(typeId, modelGroupId);
      if (ExecueCoreUtil.isCollectionNotEmpty(realizations)) {
         for (BusinessEntityDefinition businessEntityDefinition : realizations) {
            if (businessEntityDefinition != null) {
               Concept concept = businessEntityDefinition.getConcept();
               if (concept != null) {
                  concept.getName();
               }
            }
         }
      }
      return realizations;
   }

   @Override
   public List<Type> getAllTypes () throws KDXException {
      return getKdxDataAccessManager().getAllTypes();
   }

   @Override
   public List<RIOntoTerm> getRegularExpressionBasedOntoTerms () throws KDXException {
      return getKdxDataAccessManager().getRegularExpressionBasedOntoTerms();
   }

   @Override
   public PossibleAttribute getPossibleAttribute (Long typeBedId, Long componentTypeBedId, Long relationBedId)
            throws KDXException {
      PossibleAttribute possibleAttribute = getKdxDataAccessManager().getPossibleAttribute(typeBedId,
               componentTypeBedId, relationBedId);
      navigatePossibleAttribute(possibleAttribute);
      return possibleAttribute;
   }

   @Override
   public PossibleAttribute getPossibleAttribute (Long typeBedId, Long componentTypeBedId) throws KDXException {
      return getKdxDataAccessManager().getPossibleAttribute(typeBedId, componentTypeBedId);
   }

   /**
    * @param possibleAttribute
    * @return
    */
   private void navigatePossibleAttribute (PossibleAttribute possibleAttribute) {
      List<PossibleAttribute> possibleAttributes = new ArrayList<PossibleAttribute>(1);
      if (possibleAttribute != null) {
         possibleAttributes.add(possibleAttribute);
      }
      navigatePossibleAttributes(possibleAttributes);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IKDXRetrievalService#getSFLTermsForInstancesOfConceptByBatchNumber(java.lang.Long,
    *      java.lang.Long, java.lang.Long)
    */
   @Override
   public List<SFLTerm> getSFLTermsForInstancesOfConceptByBatchNumber (Long conceptBedId, Long batchNumber,
            Long batchSize) throws KDXException {
      List<SFLTerm> sflTerms = getKdxDataAccessManager().getSFLTermsForInstancesOfConceptByBatchNumber(conceptBedId,
               batchNumber, batchSize);
      for (SFLTerm term : sflTerms) {
         Set<SFLTermToken> sflTermTokens = term.getSflTermTokens();
         for (SFLTermToken termToken : sflTermTokens) {
            termToken.getBusinessTermToken();
         }
      }
      return sflTerms;
   }

   @Override
   public List<BusinessEntityDefinition> getAllAttributes (Long modelId, Long typeId) throws KDXException {
      List<BusinessEntityDefinition> attributes = new ArrayList<BusinessEntityDefinition>();
      BusinessEntityDefinition typeBED = getTypeBusinessEntityDefinition(typeId);
      // build the type list which is available in the possible_attributes table
      List<PossibleAttribute> possibleAttributes = getAllPossibleAttributes(typeBED.getId());
      List<String> typeNames = new ArrayList<String>();
      for (PossibleAttribute possibleAttribute : possibleAttributes) {
         typeNames.add(possibleAttribute.getComponentTypeBed().getType().getName());
      }
      List<BusinessEntityDefinition> attributeEntities = getKdxDataAccessManager().getAllAttributeEntities(modelId);
      for (BusinessEntityDefinition attributeEntity : attributeEntities) {
         attributeEntity.getConcept().getName();
         if (!checkIfExistsInTypeList(typeNames, attributeEntity)) {
            attributes.add(attributeEntity);
         }
      }
      return attributes;
   }

   private boolean checkIfExistsInTypeList (List<String> typeNames, BusinessEntityDefinition entity) {
      return typeNames.contains(entity.getType().getName());
   }

   @Override
   public List<SecondaryWord> getAllSecondaryWordsForModel (Long modelId) throws KDXException {
      return getKdxDataAccessManager().getAllSecondaryWordsForModel(modelId);
   }

   @Override
   public Set<String> getEligibleSecondaryWordsForModel (Long modelId, Long threshold) throws KDXException {
      List<String> secondaryWordsList = getKdxDataAccessManager().getEligibleSecondaryWordsForModel(modelId, threshold);
      Set<String> secondaryWords = new HashSet<String>(1);
      if (!CollectionUtils.isEmpty(secondaryWordsList)) {
         secondaryWords.addAll(secondaryWordsList);
      }
      return secondaryWords;
   }

   @Override
   public List<Relation> suggestRelationsByName (Long modelId, String searchString, int maxResults) throws KDXException {
      return getKdxDataAccessManager().suggestRelationsByName(modelId, searchString, maxResults);
   }

   @Override
   public BusinessEntityDefinition getPopulatedBEDForIndexForms (Long BEDId) throws KDXException {
      try {
         BusinessEntityDefinition businessEntityDefinition = getKdxDataAccessManager().getById(BEDId,
                  BusinessEntityDefinition.class);
         if (businessEntityDefinition != null) {
            ModelGroup modelGroup = businessEntityDefinition.getModelGroup();
            if (modelGroup != null) {
               modelGroup.getId();
               businessEntityDefinition.getType().getName();
               if (BusinessEntityType.CONCEPT.equals(businessEntityDefinition.getEntityType())) {
                  businessEntityDefinition.getConcept().getName();
               } else if (BusinessEntityType.TYPE.equals(businessEntityDefinition.getEntityType())) {
                  // Type is already loaded above, anything related to type need to load here.
               } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(businessEntityDefinition.getEntityType())) {
                  businessEntityDefinition.getInstance().getName();
                  businessEntityDefinition.getConcept().getName();
               } else if (BusinessEntityType.TYPE_LOOKUP_INSTANCE.equals(businessEntityDefinition.getEntityType())) {
                  businessEntityDefinition.getInstance().getName();
               } else if (BusinessEntityType.CONCEPT_PROFILE.equals(businessEntityDefinition.getEntityType())) {
                  businessEntityDefinition.getConceptProfile().getName();
               } else if (BusinessEntityType.INSTANCE_PROFILE.equals(businessEntityDefinition.getEntityType())) {
                  businessEntityDefinition.getInstanceProfile().getName();
               } else if (BusinessEntityType.RELATION.equals(businessEntityDefinition.getEntityType())) {
                  businessEntityDefinition.getRelation().getName();
               }
            }
         }
         return businessEntityDefinition;
      } catch (KDXException kdxException) {
         throw kdxException;
      } catch (SWIException swiException) {
         throw new KDXException(swiException.getCode(), swiException);
      }
   }

   @Override
   public BusinessEntityDefinition getBEDByBehaviorName (String behaviorName) throws KDXException {
      return getKdxDataAccessManager().getBEDByBehaviorName(behaviorName);
   }

   @Override
   public boolean isMatchedBusinessEntityType (Long bedId, String typeName) throws KDXException {
      return getKdxDataAccessManager().isMatchedBusinessEntityType(bedId, typeName);
   }

   @Override
   public boolean isConceptMatchedBusinessEntityType (Long conceptId, Long modelId, String typeName)
            throws KDXException {
      return getKdxDataAccessManager().isConceptMatchedBusinessEntityType(conceptId, modelId, typeName);
   }

   @Override
   public List<BusinessEntityDefinition> getConceptsOfParticularType (Long modelId, String typeName)
            throws KDXException {
      return getKdxDataAccessManager().getConceptsOfParticularType(modelId, typeName);
   }

   @Override
   public List<Long> getSFLTermIdsByContextId (Long contextId) throws KDXException {
      return getKdxDataAccessManager().getSFLTermIdsByContextId(contextId);
   }

   @Override
   public Vertical getVerticalByName (String name) throws KDXException {
      return getKdxDataAccessManager().getVerticalByName(name);
   }

   @Override
   public Vertical getVerticalById (Long id) throws KDXException {
      try {
         return getKdxDataAccessManager().getById(id, Vertical.class);
      } catch (SWIException swiException) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, swiException);
      }
   }

   @Override
   public Map<String, List<VerticalAppExample>> getVerticalAppExamples () throws KDXException {
      return getKdxDataAccessManager().getVerticalAppExamples();
   }

   @Override
   public Map<String, List<VerticalAppExample>> getVerticalAppExamplesByDay (int day) throws KDXException {
      return getKdxDataAccessManager().getVerticalAppExamplesByDay(day);
   }

   @Override
   public List<Concept> getConceptsByPopularity (Long modelId, Long limt) throws KDXException {
      return getKdxDataAccessManager().getConceptsByPopularity(modelId, limt);
   }

   @Override
   public List<Instance> getInstancesByPopularity (Long modelId, Long conceptId, Long limit) throws KDXException {
      return getKdxDataAccessManager().getInstancesByPopularity(modelId, conceptId, limit);
   }

   @Override
   public Instance getInstanceByInstanceName (Long conceptBedId, String instanceName) throws KDXException {
      return getKdxDataAccessManager().getInstanceByInstanceName(conceptBedId, instanceName);
   }

   @Override
   public Map<Long, RIOntoTerm> getConceptOntoTermsByConceptBedIds (Set<Long> conceptBedIds) throws KDXException {
      Map<Long, RIOntoTerm> ontoTermsMapByConceptBedId = new HashMap<Long, RIOntoTerm>(1);
      if (CollectionUtils.isEmpty(conceptBedIds)) {
         return ontoTermsMapByConceptBedId;
      }
      List<RIOntoTerm> ontoTerms = getKdxDataAccessManager().getConceptOntoTermsByConceptBedIds(conceptBedIds);
      if (CollectionUtils.isEmpty(ontoTerms)) {
         return ontoTermsMapByConceptBedId;
      }
      for (RIOntoTerm ontoTerm : ontoTerms) {
         ontoTermsMapByConceptBedId.put(ontoTerm.getConceptBEDID(), ontoTerm);
      }
      return ontoTermsMapByConceptBedId;
   }

   @Override
   public List<Model> getAllModels () throws KDXException {
      return getKdxDataAccessManager().getAllModels();
   }

   @Override
   public List<PossibleDetailType> getPossibleDetailTypes (Long typeBedId) throws KDXException {
      List<PossibleDetailType> possibleDetailTypes = getKdxDataAccessManager().getPossibleDetailTypes(typeBedId);
      if (ExecueCoreUtil.isCollectionNotEmpty(possibleDetailTypes)) {
         for (PossibleDetailType possibleDetailType : possibleDetailTypes) {
            BusinessEntityDefinition detailTypeBed = possibleDetailType.getDetailTypeBed();
            if (detailTypeBed != null) {
               detailTypeBed.getType().getName();
            }
            BusinessEntityDefinition typeBed = possibleDetailType.getTypeBed();
            if (typeBed != null) {
               typeBed.getType().getName();
            }
         }
      }
      return possibleDetailTypes;
   }

   @Override
   public List<BusinessEntityDefinition> getPossibleDetailTypesForType (Long typeBedId) throws KDXException {
      List<BusinessEntityDefinition> possibleDetailTypes = getKdxDataAccessManager().getPossibleDetailTypesForType(
               typeBedId);
      if (ExecueCoreUtil.isCollectionNotEmpty(possibleDetailTypes)) {
         for (BusinessEntityDefinition possibleDetailType : possibleDetailTypes) {
            possibleDetailType.getType().getName();
         }
      }
      return possibleDetailTypes;
   }

   @Override
   public EntityDetailType getDetailTypeForConcept (Long conceptBedId) throws KDXException {
      EntityDetailType detailType = getKdxDataAccessManager().getDetailTypeForConcept(conceptBedId);
      if (detailType != null) {
         detailType.getEntityBed().getConcept().getName();
         detailType.getDetailTypeBed().getType().getName();
      }
      return detailType;
   }

   @Override
   public BusinessEntityDefinition getBEDByRealizationName (Long modelId, String realizationName) throws KDXException {
      return getKdxDataAccessManager().getBEDByRealizationName(modelId, realizationName);
   }

   @Override
   public BusinessEntityDefinition getDefaultPopulatedDetailType (Long typeBedId) throws KDXException {
      BusinessEntityDefinition defaultDetailType = getKdxDataAccessManager().getDefaultDetailType(typeBedId);
      if (defaultDetailType != null) {
         Type type = defaultDetailType.getType();
         type.getName();
      }
      return defaultDetailType;
   }

   @Override
   public List<Long> getModelGroupIdsByApplicationId (Long applicationId) throws KDXException {
      return getKdxDataAccessManager().getModelGroupIdsByApplicationId(applicationId);
   }

   @Override
   public List<Concept> getMappedConceptsForParticularBehaviour (Long modelId, Long assetId, BehaviorType behaviourType)
            throws KDXException {
      return getKdxDataAccessManager().getMappedConceptsForParticularBehaviour(modelId, assetId, behaviourType);
   }

   @Override
   public List<Concept> getMappedConceptsForParticularType (Long modelId, Long assetId, String typeName)
            throws KDXException {
      return getKdxDataAccessManager().getMappedConceptsForParticularType(modelId, assetId, typeName);
   }

   @Override
   public List<Type> getAllVisibleTypes () throws KDXException {
      return getKdxDataAccessManager().getAllVisibleTypes();
   }

   @Override
   public List<BusinessEntityDefinition> getAllNonAttributeEntities (Long modelId) throws KDXException {
      List<BusinessEntityDefinition> allNonAttributeEntities = getKdxDataAccessManager().getAllNonAttributeEntities(
               modelId);
      // TODO:-JT- will revisit this method as we may not need type here.
      if (ExecueCoreUtil.isCollectionNotEmpty(allNonAttributeEntities)) {
         for (BusinessEntityDefinition attributeEntity : allNonAttributeEntities) {
            attributeEntity.getConcept().getName();
            attributeEntity.getType().getName();
         }
      }
      return allNonAttributeEntities;
   }

   @Override
   public Map<String, String> getConceptInstanceDisplayNamesByAbbrevatedNames (Long modelId, String conceptName,
            List<String> abbrevatedNames) throws KDXException {
      return getKdxDataAccessManager().getConceptInstanceDisplayNamesByAbbrevatedNames(modelId, conceptName,
               abbrevatedNames);
   }

   @Override
   public Instance getTopPopularityInstance (Long modelId, String originAirportConceptName) throws KDXException {
      return getKdxDataAccessManager().getTopPopularityInstance(modelId, originAirportConceptName);
   }

   @Override
   public Map<Long, RIOntoTerm> getOntoTermsByEntityBeId (Set<Long> entityBedIds) throws KDXException {
      return getKdxDataAccessManager().getOntoTermsByEntityBeId(entityBedIds);
   }

   @Override
   public List<ModelGroup> getUserModelGroupsByModelId (Long modelId) throws KDXException {
      return getKdxDataAccessManager().getUserModelGroupsByModelId(modelId);
   }

   @Override
   public List<BusinessEntityVariation> getBusinessEntityVariations (Long entityBedId) throws KDXException {
      return getKdxDataAccessManager().getBusinessEntityVariations(entityBedId);
   }

   @Override
   public List<String> getBusinessEntityVariationNames (Long entityBedId) throws KDXException {
      return getKdxDataAccessManager().getBusinessEntityVariationNames(entityBedId);
   }

   @Override
   public List<RISharedUserModelMapping> getRISharedUserModelMappings (List<Long> baseInstanceBedIds,
            Set<Long> modelGroupIds) throws KDXException {
      return getKdxDataAccessManager().getRISharedUserModelMappings(baseInstanceBedIds, modelGroupIds);
   }

   @Override
   public List<RISharedUserModelMapping> getRISharedUserModelMappings (List<Long> baseInstanceBedIds)
            throws KDXException {
      return getKdxDataAccessManager().getRISharedUserModelMappings(baseInstanceBedIds);
   }

   @Override
   public List<Long> getConceptBedIdsHavingInstances (Long modelId) throws KDXException {
      return getKdxDataAccessManager().getConceptBedIdsHavingInstances(modelId);
   }

   @Override
   public List<Long> getConceptBedsHavingBehaviorType (List<Long> conceptBedIds, BehaviorType behaviorType)
            throws KDXException {
      return getKdxDataAccessManager().getConceptBedsHavingBehaviorType(conceptBedIds, behaviorType);
   }

   @Override
   public List<Instance> getInstancesByBEDIds (List<Long> instanceBEDIds) throws KDXException {
      return getKdxDataAccessManager().getInstancesByBEDIds(instanceBEDIds);
   }

   @Override
   public Concept getConceptByBEDId (Long conceptBEDId) throws KDXException {
      Concept concept = null;
      try {
         BusinessEntityDefinition businessEntityDefinition = getKdxDataAccessManager().getById(conceptBEDId,
                  BusinessEntityDefinition.class);
         if (businessEntityDefinition != null) {
            concept = businessEntityDefinition.getConcept();
            concept.getName();
         }
      } catch (SWIException e) {
         throw new KDXException(e.getCode(), e);
      }
      return concept;
   }

   @Override
   public List<BusinessEntityInfo> getInstanceBusinessEntitiesByPageForModel (Long modelId, Page page)
            throws KDXException {
      return getKdxDataAccessManager().getInstanceBusinessEntitiesByPageForModel(modelId, page);
   }

   @Override
   public List<BusinessEntityInfo> getConceptBusinessEntitiesByPageForModel (Long modelId, Page page)
            throws KDXException {
      return getKdxDataAccessManager().getConceptBusinessEntitiesByPageForModel(modelId, page);
   }

   @Override
   public List<BusinessEntityInfo> getConceptProfileBusinessEntitiesByPageForModel (Long modelId, Page page)
            throws KDXException {
      return getKdxDataAccessManager().getConceptProfileBusinessEntitiesByPageForModel(modelId, page);
   }

   @Override
   public List<BusinessEntityInfo> getInstanceProfileBusinessEntitiesByPageForModel (Long modelId, Page page)
            throws KDXException {
      return getKdxDataAccessManager().getInstanceProfileBusinessEntitiesByPageForModel(modelId, page);
   }

   @Override
   public List<BusinessEntityInfo> getRelationBusinessEntitiesByPageForModel (Long modelId, Page page)
            throws KDXException {
      return getKdxDataAccessManager().getRelationBusinessEntitiesByPageForModel(modelId, page);
   }

   @Override
   public List<Concept> getEligibleConceptsOfAssetForCubeByPage (Long assetId, Page page) throws KDXException {
      return getKdxDataAccessManager().getEligibleConceptsOfAssetForCubeByPage(assetId, page);
   }

   @Override
   public ModelGroup getModelGroupByTypeBedId (Long typeBedId) throws KDXException {
      return getKdxDataAccessManager().getModelGroupByTypeBedId(typeBedId);
   }

   @Override
   public List<Long> getInstanceIdsByConceptId (Long modelId, Long conceptId) throws KDXException {
      return getKdxDataAccessManager().getInstanceIdsByConceptId(modelId, conceptId);
   }

   @Override
   public List<BusinessEntityDefinition> getBedByModelGroupsAndConceptId (List<Long> modelGroupIds, Long conceptId)
            throws KDXException {
      return getBedByModelGroupsAndConceptId(modelGroupIds, conceptId);
   }

   @Override
   public List<Long> findMatchingTypeInstanceIncludingVariations (Long modelGroupId, Long typeId, String instanceValue,
            BusinessEntityType entityType) throws KDXException {
      return getKdxDataAccessManager().findMatchingTypeInstanceIncludingVariations(modelGroupId, typeId, instanceValue,
               entityType);
   }

   private List<RIOntoTerm> getTermsByLookupWords (List<String> words, List<Long> modelGroupIds,
            boolean skipLocationTypeRecognition) throws KDXException {
      return getKdxDataAccessManager().getTermsByLookupWords(words, modelGroupIds, skipLocationTypeRecognition,
               getLocationChildTypeBedIds(skipLocationTypeRecognition));
   }

   @Override
   public List<Concept> getLookupTypeConceptsForModelBySearchString (Long modelId, String searchString,
            List<Long> locationRealizedTypeIds, Long retrievalLimit) throws KDXException {
      return getKdxDataAccessManager().getLookupTypeConceptsForModelBySearchString(modelId, searchString,
               locationRealizedTypeIds, retrievalLimit);
   }

   @Override
   public Long getEntityCountWithTypeBedIds (Long modelId, List<Long> locationChildrenBedIds) throws KDXException {
      return getKdxDataAccessManager().getEntityCountWithTypeBedIds(modelId, locationChildrenBedIds);
   }

   @Override
   public RISharedUserModelMapping getRISharedUserModelMappingByAppInstanceBedId (Long appInstanceBedId,
            Set<Long> modelGroupIds) throws KDXException {
      return getKdxDataAccessManager().getRISharedUserModelMappingByAppInstanceBedId(appInstanceBedId, modelGroupIds);
   }

   @Override
   public BusinessEntityDefinition getRelationBEDById (Long modelId, Long relationId) throws KDXException {
      return getKdxDataAccessManager().getRelationBEDById(modelId, relationId);

   }

   @Override
   public Hierarchy getHierarchyById (Long hierarchyId) throws KDXException {
      try {
         Hierarchy hierarchy = getKdxDataAccessManager().getById(hierarchyId, Hierarchy.class);
         Set<HierarchyDetail> hierarchyDetails = hierarchy.getHierarchyDetails();
         for (HierarchyDetail hierarchyDetail : hierarchyDetails) {
            hierarchyDetail.getConceptBedId();
         }
         return hierarchy;
      } catch (SWIException swiException) {
         throw new KDXException(SWIExceptionCodes.ENTITY_RETRIEVAL_FAILED, swiException);
      }
   }

   @Override
   public List<Hierarchy> getHierarchiesByModelId (Long modelId) throws KDXException {
      List<Hierarchy> hierarchies = getKdxDataAccessManager().getHierarchiesByModelId(modelId);
      for (Hierarchy hierarchy : hierarchies) {
         Set<HierarchyDetail> hierarchyDetails = hierarchy.getHierarchyDetails();
         for (HierarchyDetail hierarchyDetail : hierarchyDetails) {
            hierarchyDetail.getConceptBedId();
         }
      }
      return hierarchies;
   }

   @Override
   public List<Hierarchy> getExistingHierarchiesForConcept (Long conceptBedId) throws KDXException {
      List<Hierarchy> hierarchies = getKdxDataAccessManager().getExistingHierarchiesForConcept(conceptBedId);
      for (Hierarchy hierarchy : hierarchies) {
         Set<HierarchyDetail> hierarchyDetails = hierarchy.getHierarchyDetails();
         for (HierarchyDetail hierarchyDetail : hierarchyDetails) {
            hierarchyDetail.getConceptBedId();
         }
      }
      return hierarchies;
   }

   /**
    * @param skipLocationTypeRecognition
    * @return
    */
   private List<Long> getLocationChildTypeBedIds (boolean skipLocationTypeRecognition) {
      List<Long> locationChildTypeBedIds = null;
      if (skipLocationTypeRecognition) {
         locationChildTypeBedIds = getLocationConfigurationService().getChildBedIdsByParentBedId(
                  ExecueConstants.LOCATION_TYPE_BED_ID);
      }
      return locationChildTypeBedIds;
   }

   public boolean isConceptMatchedBehavior (Long conceptBedId, BehaviorType behaviorType) throws KDXException {
      return getKdxDataAccessManager().isConceptMatchedBehavior(conceptBedId, behaviorType);
   }

   @Override
   public List<ContentCleanupPattern> getContentCleanupPatterns (Long applicationId) throws KDXException {
      return getKdxDataAccessManager().getContentCleanupPatterns(applicationId);
   }

   @Override
   public void createContentCleanupPattern (ContentCleanupPattern contentCleanupPattern) throws KDXException {
      getKdxDataAccessManager().createContentCleanupPattern(contentCleanupPattern);
   }

   @Override
   public boolean hasInstances (Long modelId, Long conceptId) throws KDXException {
      return getKdxDataAccessManager().hasInstances(modelId, conceptId);
   }

   @Override
   public List<Concept> getConceptByBEDIds (List<Long> conceptBEDIds) throws KDXException {
      return getKdxDataAccessManager().getConceptByBEDIds(conceptBEDIds);
   }

   @Override
   public Hierarchy getHierarchyByNameForModel (Long modelId, String hierarchyName) throws KDXException {
      return getKdxDataAccessManager().getHierarchyByNameForModel(modelId, hierarchyName);
   }

   @Override
   public List<Hierarchy> getHierarchiesByConceptBEDIds (List<Long> conceptBEDIDs) throws KDXException {
      List<Hierarchy> hierarchies = getKdxDataAccessManager().getHierarchiesByConceptBEDIds(conceptBEDIDs);
      for (Hierarchy hierarchy : hierarchies) {
         Set<HierarchyDetail> hierarchyDetails = hierarchy.getHierarchyDetails();
         for (HierarchyDetail hierarchyDetail : hierarchyDetails) {
            hierarchyDetail.getId();
         }
      }
      return hierarchies;
   }

   public IKDXDataAccessManager getKdxDataAccessManager () {
      return kdxDataAccessManager;
   }

   public void setKdxDataAccessManager (IKDXDataAccessManager kdxDataAccessManager) {
      this.kdxDataAccessManager = kdxDataAccessManager;
   }

   public IMappingDataAccessManager getMappingDataAccessManager () {
      return mappingDataAccessManager;
   }

   public void setMappingDataAccessManager (IMappingDataAccessManager mappingDataAccessManager) {
      this.mappingDataAccessManager = mappingDataAccessManager;
   }

   public ILocationConfigurationService getLocationConfigurationService () {
      return locationConfigurationService;
   }

   public void setLocationConfigurationService (ILocationConfigurationService locationConfigurationService) {
      this.locationConfigurationService = locationConfigurationService;
   }

   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}