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


package com.execue.nlp.processor.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.nlp.InstanceInformation;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.NormalizedCloudDataFactory;
import com.execue.core.common.bean.nlp.NormalizedDataEntity;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.bean.nlp.RecognizedType;
import com.execue.core.common.bean.nlp.ValueRealizationNormalizedData;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.RecognitionType;
import com.execue.core.common.type.SearchFilterType;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.nlp.bean.Group;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.entity.ConceptEntity;
import com.execue.nlp.bean.entity.EntityFactory;
import com.execue.nlp.bean.entity.InstanceEntity;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.PWEntity;
import com.execue.nlp.bean.entity.ProfileEntity;
import com.execue.nlp.bean.entity.PropertyEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.ReferedTokenPosition;
import com.execue.nlp.bean.entity.SFLEntity;
import com.execue.nlp.bean.entity.TypeEntity;
import com.execue.nlp.bean.matrix.Summary;
import com.execue.nlp.engine.barcode.token.TokenUtility;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.nlp.helper.NLPServiceHelper;
import com.execue.nlp.processor.IndividualRecognitionProcessor;
import com.execue.nlp.util.NLPUtilities;
import com.execue.nlp.util.RegexUtilities;
import com.execue.ontology.exceptions.OntologyException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXModelService;

/**
 * @author Nihar
 * @since Nov 10, 2009 Created copy of the original OntoRecognition Processor so that In semantic Scoping we will not
 *        change the original Processor until the code gets some stability. Once the SemanticScoping code is good to go
 *        we can replace the original one with this.
 * @version 1.1 Commented the filterByTopCluster call as this was not model based. filtering code is moved to
 *          EliminationService to be sure that we filter the entities only for a model not across the models.
 */

public class SemanticScopingOntoRecognitionProcessor extends IndividualRecognitionProcessor {

   private static final Logger log = Logger.getLogger(SemanticScopingOntoRecognitionProcessor.class);
   private IKDXModelService    kdxModelService;
   private NLPServiceHelper    nlpServiceHelper;

   @Override
   protected void execute (ProcessorInput input) {
      try {
         recognizeDomainResources(input);
      } catch (KDXException e) {
         throw new NLPSystemException(e.getCode(), e.getMessage(), e);
      }
   }

   /**
    * Method to recognize OntoTerms By word and regex.
    * 
    * @param input
    * @throws KDXException
    * @throws OntologyException
    */
   private void recognizeDomainResources (ProcessorInput input) throws KDXException {

      // Guard condition to return if the input list is empty
      List<IWeightedEntity> recognitionEntities = input.getRecognitionEntities();
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return;
      }

      SearchFilter searchFilter = input.getSearchFilter();
      boolean searchFilterProvided = searchFilter != null
               && searchFilter.getSearchFilterType() != SearchFilterType.GENERAL;

      // Get the set of model group ids which will be used to filter out the onto terms which are not part of this model
      // group ids
      // and search filter is defined
      // TODO: NK: Later should use this set of model group ids to query the ri onto terms. So that can remove the
      // current filter logic
      // defined at code level as onto terms will get filtered at query level itself.
      Set<Long> modelGroupIds = new HashSet<Long>(1);
      if (searchFilterProvided) {
         modelGroupIds = getNlpServiceHelper().getModelGroupIdsBySearchFilter(searchFilter);
      }

      // List of tokens which will be used to query the ri onto terms
      List<String> tokenNames = new ArrayList<String>();

      // Map of linguistic root against RIParallelWord
      Map<String, RIParallelWord> linguisticRootPWMap = new HashMap<String, RIParallelWord>(1);

      // Fill the Maps of words by recognition entity's possible word for each Referred Token Position
      Map<ReferedTokenPosition, Map<IWeightedEntity, Set<String>>> wordsByRecEntityMapByRTPMap = new HashMap<ReferedTokenPosition, Map<IWeightedEntity, Set<String>>>(
               1);

      // This map is used in creating the regex instance if the regex pattern matches with any of token names of the rec
      // entity
      Map<IWeightedEntity, Set<String>> tokenNamesByRecEntity = new HashMap<IWeightedEntity, Set<String>>(1);

      // Populate the required information for the above collections and maps from the input recognition entities
      populateRequiredInfoFromRecEntities(recognitionEntities, tokenNames, tokenNamesByRecEntity,
               wordsByRecEntityMapByRTPMap, linguisticRootPWMap);

      if (CollectionUtils.isEmpty(tokenNames)) {
         return;
      }

      boolean skipLocationTypeRecognition = input.isSkipLocationTypeRecognition();
      Map<String, List<RIOntoTerm>> ontoTermsByWord = new HashMap<String, List<RIOntoTerm>>();
      if (CollectionUtils.isEmpty(modelGroupIds)) {
         ontoTermsByWord = getKdxRetrievalService().getOntoTermsMapByLookupWords(tokenNames,
                  skipLocationTypeRecognition);
      } else {
         ontoTermsByWord = getKdxRetrievalService().getOntoTermsMapByLookupWords(tokenNames,
                  new ArrayList<Long>(modelGroupIds), skipLocationTypeRecognition);
      }

      // Maintain the instancesRecognitionsMap for any RTP if we have multiple instances of the same concept/type bed
      // id, then
      // will later use this map to filter those instance recognitions
      Map<ReferedTokenPosition, Map<Long, List<RecognitionEntity>>> instanceRecognitionsMap = new HashMap<ReferedTokenPosition, Map<Long, List<RecognitionEntity>>>();
      Map<ReferedTokenPosition, List<IWeightedEntity>> recEntitiesByRTPMap = NLPUtilities
               .getRecognitionEntitiesByRTPMap(recognitionEntities);
      List<IWeightedEntity> unrecognizedEntities = new ArrayList<IWeightedEntity>();
      for (Entry<ReferedTokenPosition, List<IWeightedEntity>> recEntitiesByRTPEntry : recEntitiesByRTPMap.entrySet()) {
         ReferedTokenPosition rtp = recEntitiesByRTPEntry.getKey();
         Map<RecognitionEntity, List<RIOntoTerm>> ontoTermsByRecEntityMap = populateOntoTermsByRecognitionEntityMap(
                  wordsByRecEntityMapByRTPMap.get(rtp), ontoTermsByWord);
         if (MapUtils.isEmpty(ontoTermsByRecEntityMap)) {
            unrecognizedEntities.addAll(recEntitiesByRTPEntry.getValue());
            continue;
         }
         Map<Long, IWeightedEntity> finalRecEntitiesByEntityBedId = new HashMap<Long, IWeightedEntity>();
         Map<Long, List<RecognitionEntity>> recEntitiesByEntityBedIdMap = new HashMap<Long, List<RecognitionEntity>>();
         for (Entry<RecognitionEntity, List<RIOntoTerm>> entry : ontoTermsByRecEntityMap.entrySet()) {
            RecognitionEntity recEntity = entry.getKey();
            List<RIOntoTerm> ontoTerms = entry.getValue();
            createEntitiesBasedOnOntoTerms(modelGroupIds, searchFilterProvided, linguisticRootPWMap,
                     finalRecEntitiesByEntityBedId, recEntitiesByEntityBedIdMap, recEntity, ontoTerms);
            if (!MapUtils.isEmpty(recEntitiesByEntityBedIdMap)) {
               Map<Long, List<RecognitionEntity>> recEntitiesByEntityBedId = instanceRecognitionsMap.get(rtp);
               if (recEntitiesByEntityBedId == null) {
                  instanceRecognitionsMap.put(rtp, recEntitiesByEntityBedIdMap);
               } else {
                  recEntitiesByEntityBedId.putAll(recEntitiesByEntityBedIdMap);
               }
            }
         }

         List<IWeightedEntity> filteredEntities = new ArrayList<IWeightedEntity>(1);
         if (getNlpConfigurationService().isFilterOntoRecognitions()) {
            // Get the list of recognition entities for each of the Concept/Type bedid(key of the map) for the given rtp
            // filter the instance recognitions by retrieving the top cluster
            for (ReferedTokenPosition instanceRecsRTP : instanceRecognitionsMap.keySet()) {
               filteredEntities = getEntitiesToFilterByTopCluster(instanceRecognitionsMap.get(instanceRecsRTP));
               finalRecEntitiesByEntityBedId.values().removeAll(filteredEntities);
            }
         }

         List<IWeightedEntity> finalRecEntities = new ArrayList<IWeightedEntity>();
         finalRecEntities.addAll(finalRecEntitiesByEntityBedId.values());
         if (!CollectionUtils.isEmpty(finalRecEntities)) {
            for (IWeightedEntity weightedEntity : finalRecEntities) {
               if (((RecognitionEntity) weightedEntity).getEntityType() == RecognitionEntityType.INSTANCE_ENTITY) {
                  InstanceEntity instanceEntity = (InstanceEntity) weightedEntity;
                  List<InstanceInformation> instanceInformations = instanceEntity.getInstanceInformations();
                  boolean isFromSharedModel = instanceEntity.getModelGroupId().equals(
                           ExecueConstants.LOCATION_MODEL_GROUP_ID);
                  List<InstanceInformation> topQualityInstanceInformations = getTopQualityInstanceInformations(
                           instanceInformations, isFromSharedModel);
                  // Update the instance entity by best entities and its weight information
                  instanceEntity.setInstanceInformations(topQualityInstanceInformations);
                  instanceEntity.setWeightInformation(topQualityInstanceInformations.get(0).getWeightInformation());
               }
               input.addOutputRecognitionEntity(weightedEntity);
            }
         }
      }

      // Process the regex onto recognition
      List<IWeightedEntity> regexRecognitions = recognizeRegexInstances(recognitionEntities, input.getGroup(),
               tokenNamesByRecEntity, unrecognizedEntities);
      for (IWeightedEntity weightedEntity : regexRecognitions) {
         input.addOutputRecognitionEntity(weightedEntity);
      }
      input.setUnrecognizedEntities(unrecognizedEntities);
      if (log.isDebugEnabled()) {
         logInstanceRecognitionMap(instanceRecognitionsMap);
      }
   }

   private List<InstanceInformation> getTopQualityInstanceInformations (List<InstanceInformation> instanceInformations,
            boolean isFromSharedModel) {
      if (instanceInformations.size() == 1) {
         return instanceInformations;
      }
      // prepare a map with the weight as the key
      Map<Double, Set<InstanceInformation>> instancesByQuality = new HashMap<Double, Set<InstanceInformation>>();
      for (InstanceInformation instanceInformation : instanceInformations) {
         Double quality = new Double(instanceInformation.getWeightInformation().getRecognitionQuality());
         Set<InstanceInformation> list = instancesByQuality.get(quality);
         if (ExecueCoreUtil.isCollectionEmpty(list)) {
            list = new HashSet<InstanceInformation>();
         }
         list.add(instanceInformation);
         instancesByQuality.put(quality, list);
      }
      // sort the keys of the map
      List<Double> weightList = new ArrayList<Double>(instancesByQuality.keySet());
      Collections.sort(weightList);
      Double topQuality = weightList.get(weightList.size() - 1);
      List<InstanceInformation> topQualityInstances = new ArrayList<InstanceInformation>(instancesByQuality
               .get(topQuality));

      // Limit the top quality instance recognitions to the configured max value only if is NOT from the shared model
      int maxAllowedInstanceRecognitions = getNlpConfigurationService().getMaxAllowledInstanceRecognitions();
      if (!isFromSharedModel && topQualityInstances.size() > maxAllowedInstanceRecognitions) {
         return topQualityInstances.subList(0, maxAllowedInstanceRecognitions);
      }
      return topQualityInstances;
   }

   /**
    * Method to create respective Onto Entity based on OntoTerm EntityType and populate the Maps with this information
    * 
    * @param modelGroupIds
    * @param searchFilterProvided
    * @param linguisticRootParallelWordMap
    * @param finalRecEntitiesByEntityBedId
    * @param recEntitiesByEntityBedIdMap
    * @param inputRecognitionEntity
    * @param ontoTerms
    * @throws KDXException
    */
   private void createEntitiesBasedOnOntoTerms (Set<Long> modelGroupIds, boolean searchFilterProvided,
            Map<String, RIParallelWord> linguisticRootParallelWordMap,
            Map<Long, IWeightedEntity> finalRecEntitiesByEntityBedId,
            Map<Long, List<RecognitionEntity>> recEntitiesByEntityBedIdMap, RecognitionEntity inputRecognitionEntity,
            List<RIOntoTerm> ontoTerms) throws KDXException {

      if (CollectionUtils.isEmpty(ontoTerms)) {
         return;
      }

      for (RIOntoTerm ontoTerm : ontoTerms) {

         // Check if the onto term is valid in the passed RecEntity context.
         boolean validTerm = validateOntoTermByContext(inputRecognitionEntity, ontoTerm);
         if (!validTerm) {
            continue;
         }
         Long entityBEDId = null;
         WeightInformation weightInformation = getWeightInformation(linguisticRootParallelWordMap,
                  inputRecognitionEntity, ontoTerm);
         BusinessEntityType entityType = ontoTerm.getEntityType();
         RecognitionEntity entity = null;

         if (entityType == BusinessEntityType.RELATION) {
            entity = EntityFactory.getRecognitionEntity(RecognitionEntityType.PROPERTY_ENTITY, ontoTerm
                     .getRelationName(), NLPConstants.NLP_TAG_ONTO_PROPERTY);
            ((PropertyEntity) entity).setRelationBedId(ontoTerm.getRelationBEDID());
            ((PropertyEntity) entity).setRelationDisplayName(ontoTerm.getWord());
            entityBEDId = ((PropertyEntity) entity).getRelationBedId();
         } else if (entityType.equals(BusinessEntityType.TYPE)) {
            entity = EntityFactory.getRecognitionEntity(RecognitionEntityType.TYPE_ENTITY, ontoTerm.getTypeName(),
                     NLPConstants.NLP_TAG_ONTO_TYPE);

            entityBEDId = ontoTerm.getTypeBEDID();
         } else if (entityType == BusinessEntityType.CONCEPT || entityType == BusinessEntityType.REALIZED_TYPE) {
            boolean isAbstract = false;
            if (entityType == BusinessEntityType.CONCEPT) {
               isAbstract = getKdxModelService().checkEntityHasBehavior(ontoTerm.getConceptBEDID(),
                        BehaviorType.ABSTRACT);
            } else {
               isAbstract = getKdxModelService().hasBehavior(ontoTerm.getTypeBEDID(), BehaviorType.ABSTRACT);
            }
            if (!isAbstract) {
               String nlpTag = NLPUtilities.getNLPTag(entityType);
               entity = EntityFactory.getRecognitionEntity(RecognitionEntityType.CONCEPT_ENTITY, ontoTerm
                        .getConceptName(), nlpTag);
               ((OntoEntity) entity).setWord(ontoTerm.getWord());
               populateConceptDetails(ontoTerm, entity);
               entityBEDId = ((ConceptEntity) entity).getConceptBedId();
            }
         } else if (entityType == BusinessEntityType.CONCEPT_LOOKUP_INSTANCE
                  || entityType == BusinessEntityType.TYPE_LOOKUP_INSTANCE
                  || entityType == BusinessEntityType.REALIZED_TYPE_LOOKUP_INSTANCE) {
            boolean processInstance = checkForValidNameTypeTerm(ontoTerm, inputRecognitionEntity, weightInformation);
            if (!processInstance) {
               continue;
            }

            if (entityType == BusinessEntityType.TYPE_LOOKUP_INSTANCE
                     || entityType == BusinessEntityType.REALIZED_TYPE_LOOKUP_INSTANCE) {
               entityBEDId = ontoTerm.getTypeBEDID();
            } else if (entityType.equals(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE)) {
               entityBEDId = ontoTerm.getConceptBEDID();
            }

            List<RecognitionEntity> recEnities = recEntitiesByEntityBedIdMap.get(entityBEDId);
            if (recEnities == null) {
               recEnities = new ArrayList<RecognitionEntity>();
               recEntitiesByEntityBedIdMap.put(entityBEDId, recEnities);

               // Instance entity will be created only once for a particular concept/type which is
               // at same position and will have multiple instance informations for each separate onto recognition
               String nlpTag = NLPUtilities.getNLPTag(entityType);
               entity = EntityFactory.getRecognitionEntity(RecognitionEntityType.INSTANCE_ENTITY, ontoTerm
                        .getInstanceName(), nlpTag);
               ((OntoEntity) entity).setWord(ontoTerm.getInstanceName());
               populateConceptDetails(ontoTerm, entity);
               recEnities.add(entity);
            } else {
               entity = recEnities.get(0);
            }

            // Create the instance information
            InstanceInformation information = new InstanceInformation();
            information.setInstanceBedId(ontoTerm.getInstanceBEDID());
            information.setKnowledgeId(ontoTerm.getKnowledgeId());
            information.setInstanceValue(ontoTerm.getInstanceName());
            information.setInstanceDisplayName(ontoTerm.getWord());
            information.setWeightInformation(weightInformation);
            // Check if the instanceName Has Any DisplaySymbol against It
            String displaySymbol = getNlpConfigurationService().getDisplaySymbolBasedOnInstanceName(
                     ontoTerm.getInstanceName());
            if (!StringUtils.isEmpty(displaySymbol)) {
               information.setDisplaySymbol(displaySymbol);
            }

            ((InstanceEntity) entity).addInstanceInformation(information);
            entityBEDId = information.getInstanceBedId();
         } else if (entityType == BusinessEntityType.CONCEPT_PROFILE) {
            entity = EntityFactory.getRecognitionEntity(RecognitionEntityType.CONCEPT_PROFILE_ENTITY, ontoTerm
                     .getProfileName(), NLPConstants.NLP_TAG_ONTO_CONCEPT_PROFILE);
            ((ProfileEntity) entity).setConceptBedId(ontoTerm.getConceptBEDID());
            populateProfileDetails(ontoTerm, entity);
            entityBEDId = ((ProfileEntity) entity).getProfileID();
         } else if (entityType == BusinessEntityType.INSTANCE_PROFILE) {
            entity = EntityFactory.getRecognitionEntity(RecognitionEntityType.INSTANCE_PROFILE_ENTITY, ontoTerm
                     .getProfileName(), NLPConstants.NLP_TAG_ONTO_INSTANCE_PROFILE);
            populateProfileDetails(ontoTerm, entity);
            populateConceptDetails(ontoTerm, entity);
            entityBEDId = ((ProfileEntity) entity).getProfileID();
         }
         // finally set the common information to all the entities
         if (entity != null) {
            updateCommonInformationInEntity(inputRecognitionEntity, entity, ontoTerm, weightInformation);
            addEntityToRecognitionList(finalRecEntitiesByEntityBedId, entity, entityBEDId);
         }
      }
   }

   /**
    * @param originalRecEntity
    * @param newRecEntity
    * @param ontoTerm
    * @param weightInformation
    */
   private void updateCommonInformationInEntity (RecognitionEntity originalRecEntity, RecognitionEntity newRecEntity,
            RIOntoTerm ontoTerm, WeightInformation weightInformation) {
      // Set the knowledge id
      ((OntoEntity) newRecEntity).setKnowledgeId(ontoTerm.getKnowledgeId());
      ((OntoEntity) newRecEntity).setModelGroupId(ontoTerm.getModelGroupId());
      ((OntoEntity) newRecEntity).setRecEntity(originalRecEntity);
      ((OntoEntity) newRecEntity).setPopularity(ontoTerm.getPopularity());
      ((TypeEntity) newRecEntity).setTypeBedId(ontoTerm.getTypeBEDID());
      ((TypeEntity) newRecEntity).setTypeDisplayName(ontoTerm.getTypeName());
      ((TypeEntity) newRecEntity).setDetailTypeBedId(ontoTerm.getDetailTypeBedId());
      ((TypeEntity) newRecEntity).setDetailTypeName(ontoTerm.getDetailTypeName());
      newRecEntity.setIteration(1);
      newRecEntity.setLevel(1);
      updatePositionInfo(originalRecEntity, newRecEntity);
      newRecEntity.setClusterInformation(originalRecEntity.getClusterInformation());
      newRecEntity.setWeightInformation(weightInformation);
      newRecEntity.setHitsUpdateInfo(originalRecEntity.getHitsUpdateInfo());

   }

   /**
    * @param recognitionEntities
    * @param tokenNames
    * @param tokenNamesByRecEntity
    * @param wordsByRecEntityMapByRTPMap
    * @param linguisticRootParallelWordMap
    */
   private void populateRequiredInfoFromRecEntities (List<IWeightedEntity> recognitionEntities,
            List<String> tokenNames, Map<IWeightedEntity, Set<String>> tokenNamesByRecEntity,
            Map<ReferedTokenPosition, Map<IWeightedEntity, Set<String>>> wordsByRecEntityMapByRTPMap,
            Map<String, RIParallelWord> linguisticRootParallelWordMap) {
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         Set<String> possibleWords = getPossibleWords(weightedEntity, linguisticRootParallelWordMap);
         tokenNames.addAll(possibleWords);
         tokenNamesByRecEntity.put(weightedEntity, possibleWords);
         ReferedTokenPosition rtp = new ReferedTokenPosition(((RecognitionEntity) weightedEntity)
                  .getReferedTokenPositions());
         Map<IWeightedEntity, Set<String>> wordsByRecEntityMap = wordsByRecEntityMapByRTPMap.get(rtp);
         if (wordsByRecEntityMap == null) {
            wordsByRecEntityMap = new HashMap<IWeightedEntity, Set<String>>(1);
            wordsByRecEntityMapByRTPMap.put(rtp, wordsByRecEntityMap);
         }
         wordsByRecEntityMap.put(weightedEntity, possibleWords);

      }
   }

   /**
    * Method to check the validity of onto term as per the Recognition Entity context. If recognition is SFL then check
    * the onto term model group id against the SFL context id. If recognition is PW word then check the onto term model
    * group id against the PW Context. Both should match to be valid. For any other type of recognition, default is true
    * i.e. valid.
    * 
    * @param recognitionEntity
    * @param ontoTerm
    */
   private boolean validateOntoTermByContext (RecognitionEntity recognitionEntity, RIOntoTerm ontoTerm) {
      if (recognitionEntity.getEntityType() == RecognitionEntityType.SFL_ENTITY) {
         Long contextId = ((SFLEntity) recognitionEntity).getContextId();
         if (!ontoTerm.getModelGroupId().equals(contextId)) {
            return false;
         }
      }
      if (recognitionEntity.getEntityType() == RecognitionEntityType.PW_ENTITY) {
         Long contextId = ((PWEntity) recognitionEntity).getContextId();
         if (!contextId.equals(1L) && !ontoTerm.getModelGroupId().equals(contextId)) {
            return false;
         }
      }
      return true;
   }

   /**
    * @param ontoTerm
    * @param entity
    */
   private void populateProfileDetails (RIOntoTerm ontoTerm, RecognitionEntity entity) {
      ((ProfileEntity) entity).setProfileID(ontoTerm.getProfileBEDID());
      ((ProfileEntity) entity).setProfileName(ontoTerm.getProfileName());
   }

   /**
    * @param ontoTerm
    * @param entity
    */
   private void populateConceptDetails (RIOntoTerm ontoTerm, RecognitionEntity entity) {
      ((ConceptEntity) entity).setConceptBedId(ontoTerm.getConceptBEDID());
      ((ConceptEntity) entity).setConceptDisplayName(ontoTerm.getConceptName());
   }

   /**
    * @param instancesRecognitionsMap
    */
   private void logInstanceRecognitionMap (
            Map<ReferedTokenPosition, Map<Long, List<RecognitionEntity>>> instancesRecognitionsMap) {
      log.debug("###############################################");
      if (instancesRecognitionsMap.size() > 0) {
         StringBuffer sb = new StringBuffer();
         for (ReferedTokenPosition pos : instancesRecognitionsMap.keySet()) {
            List<Long> filterBEDIds = new ArrayList<Long>();
            sb.append("Position : ").append(pos);
            Map<Long, List<RecognitionEntity>> map = instancesRecognitionsMap.get(pos);
            if (map.size() > 0) {
               for (Long bedId : map.keySet()) {
                  sb.append("\n\tEntity BED Id : ").append(bedId);
                  List<RecognitionEntity> list = map.get(bedId);
                  if (ExecueCoreUtil.isCollectionNotEmpty(list)) {
                     for (RecognitionEntity recEnt : list) {
                        sb.append("\n\t\t").append(recEnt);
                     }
                     if (list.size() > 10) {
                        filterBEDIds.add(bedId);
                     }
                  }
               }
               if (filterBEDIds.size() > 0) {
                  for (Long id : filterBEDIds) {
                     List<RecognitionEntity> filteredList = new ArrayList<RecognitionEntity>();
                     List<RecognitionEntity> list = map.get(id);
                     for (int i = 0; i < 3; i++) {
                        filteredList.add(list.get(i));
                     }
                     map.remove(id);
                     map.put(id, filteredList);
                  }
               }
            }
         }
         log.debug(sb.toString());
      }
      log.debug("###############################################");
   }

   /**
    * Method to check that if Instance is Of Type Name and the recEntity from which its being recognized is Altered then
    * the instance entity shall not be added.
    * 
    * @param ontoTerm
    * @param recognitionEntity
    * @param weightInformation
    * @return
    */
   private boolean checkForValidNameTypeTerm (RIOntoTerm ontoTerm, RecognitionEntity recognitionEntity,
            WeightInformation weightInformation) {
      if (ontoTerm.getTypeName().equals(RecognizedType.NAME_TYPE.getValue())) {
         if (recognitionEntity.isTokenAltered()) {
            if (recognitionEntity.getEntityType() == RecognitionEntityType.PW_ENTITY) {
               PWEntity pwEntity = (PWEntity) recognitionEntity;
               Map<String, RIParallelWord> wordValues = pwEntity.getWordValues();
               for (RIParallelWord parallelWord : wordValues.values()) {
                  if (parallelWord.getEquivalentWord().equalsIgnoreCase(ontoTerm.getWord())
                           && parallelWord.getIsDifferentWord() == CheckType.NO) {
                     return true;
                  }
               }
            }
            return false;
         } else if (recognitionEntity.getEntityType() == RecognitionEntityType.SFL_ENTITY) {
            double quality = recognitionEntity.getRecognitionQuality();
            double missingQuality = 1 - quality;
            int penalty = getNlpConfigurationService().getPenaltyForMissingNamePart();
            double adjustedQuality = weightInformation.getRecognitionQuality() - missingQuality * penalty / 100;
            weightInformation.setRecognitionQuality(adjustedQuality);
         }
      }
      return true;
   }

   private void addEntityToRecognitionList (Map<Long, IWeightedEntity> recognitionEntitiesByEntityBedId,
            RecognitionEntity currentEntity, Long entityBedId) {
      RecognitionEntity existingEntity = (RecognitionEntity) recognitionEntitiesByEntityBedId.get(entityBedId);
      if (existingEntity == null) {
         recognitionEntitiesByEntityBedId.put(entityBedId, currentEntity);
      } else {
         if (currentEntity.getWeightInformation().getRecognitionQuality() > existingEntity.getWeightInformation()
                  .getRecognitionQuality()) {
            recognitionEntitiesByEntityBedId.put(entityBedId, currentEntity);
         }
      }
   }

   private WeightInformation getWeightInformation (Map<String, RIParallelWord> linguisticRootParallelWordMap,
            RecognitionEntity recognitionEntity, RIOntoTerm ontoTerm) {
      double recognitionQuality = NLPConstants.MAX_QUALITY;
      double recognitionWeight = NLPConstants.MAX_WEIGHT;
      if (RecognitionEntityType.PW_ENTITY.equals(recognitionEntity.getEntityType())) {
         PWEntity pwEntity = (PWEntity) recognitionEntity;
         Map<String, RIParallelWord> parallelWords = pwEntity.getWordValues();
         RIParallelWord riParallelWord = parallelWords.get(ontoTerm.getWord().toLowerCase());
         if (riParallelWord == null) {
            riParallelWord = linguisticRootParallelWordMap.get(ontoTerm.getWord().toLowerCase());
         }
         recognitionQuality = recognitionQuality * riParallelWord.getQuality();
      } else {
         recognitionQuality = recognitionQuality * recognitionEntity.getWeightInformation().getRecognitionQuality();
         recognitionWeight = recognitionEntity.getWeightInformation().getRecognitionWeight();
      }
      RecognitionType recType = ontoTerm.getWordType();
      recognitionQuality = getNlpConfigurationService().getWeightForRecognitionType(recType) * recognitionQuality / 100;
      WeightInformation weightInformation = new WeightInformation();
      weightInformation.setRecognitionQuality(recognitionQuality);
      weightInformation.setRecognitionWeight(recognitionWeight);
      return weightInformation;
   }

   /**
    * This method first gets the Best instances for all the the concepts respectively and then run a topcluster across
    * these instances. Top Cluster of the Bests.
    * 
    * @param recognitionsMap
    * @return the List<IWeightedEntity>
    */
   private List<IWeightedEntity> getEntitiesToFilterByTopCluster (Map<Long, List<RecognitionEntity>> recognitionsMap) {
      if (ExecueCoreUtil.isMapEmpty(recognitionsMap)) {
         return new ArrayList<IWeightedEntity>(1);
      }
      List<IWeightedEntity> bestEntityByConceptList = new ArrayList<IWeightedEntity>(3);
      List<IWeightedEntity> invalidEntitiesList = new ArrayList<IWeightedEntity>(3);
      for (Long bedId : recognitionsMap.keySet()) {
         List<RecognitionEntity> recList = recognitionsMap.get(bedId);
         invalidEntitiesList.addAll(recList);
         bestEntityByConceptList.add(NLPUtilities.chooseBestRecognitionEntity(recList));
      }
      invalidEntitiesList.removeAll(bestEntityByConceptList);
      return invalidEntitiesList;
   }

   private Map<RecognitionEntity, List<RIOntoTerm>> populateOntoTermsByRecognitionEntityMap (
            Map<IWeightedEntity, Set<String>> wordsByEntity, Map<String, List<RIOntoTerm>> ontoTermsByWord) {
      Map<RecognitionEntity, List<RIOntoTerm>> recognitionTermsMap = new HashMap<RecognitionEntity, List<RIOntoTerm>>();

      for (Entry<IWeightedEntity, Set<String>> entry : wordsByEntity.entrySet()) {
         List<RIOntoTerm> tempTermList = new ArrayList<RIOntoTerm>();
         for (String word : entry.getValue()) {
            List<RIOntoTerm> terms = ontoTermsByWord.get(word.toLowerCase().trim());
            if (!CollectionUtils.isEmpty(terms)) {
               for (RIOntoTerm ontoTerm : terms) {
                  tempTermList.add(ontoTerm);
               }
            }
         }
         if (!CollectionUtils.isEmpty(tempTermList)) {
            recognitionTermsMap.put((RecognitionEntity) entry.getKey(), tempTermList);
         }
      }
      return recognitionTermsMap;
   }

   private Set<String> getPossibleWords (IWeightedEntity weightedEntity,
            Map<String, RIParallelWord> linguisticRootParallelWordMap) {
      List<String> possibleWords = new ArrayList<String>();
      boolean addLinguisticRoot = getNlpConfigurationService().isAddLinguisticRoot();
      TokenUtility.getWordForEntity(possibleWords, weightedEntity, linguisticRootParallelWordMap, addLinguisticRoot);
      Set<String> words = new HashSet<String>();
      words.addAll(possibleWords);
      return words;
   }

   private List<IWeightedEntity> recognizeRegexInstances (List<IWeightedEntity> recognitionEntities, Group grp,
            Map<IWeightedEntity, Set<String>> tokenNamesByRecEntity, List<IWeightedEntity> unrecognizedEntities)
            throws KDXException {
      List<IWeightedEntity> recoEntities = new ArrayList<IWeightedEntity>(1);
      List<RIOntoTerm> regexInstances = getKdxRetrievalService().getRegularExpressionBasedOntoTerms();
      Map<RIOntoTerm, Pattern> regexPatternMap = new HashMap<RIOntoTerm, Pattern>();
      for (RIOntoTerm riOntoTerm : regexInstances) {
         regexPatternMap.put(riOntoTerm, Pattern.compile(riOntoTerm.getWord()));
      }
      for (Entry<RIOntoTerm, Pattern> entry1 : regexPatternMap.entrySet()) {
         RIOntoTerm riOntoTerm = entry1.getKey();
         for (Entry<IWeightedEntity, Set<String>> entry : tokenNamesByRecEntity.entrySet()) {
            RecognitionEntity recEntity = (RecognitionEntity) entry.getKey();
            for (String word : entry.getValue()) {
               boolean matches = RegexUtilities.matches(entry1.getValue(), word);
               if (matches) {
                  unrecognizedEntities.remove(recEntity);
                  RecognitionEntity entity = EntityFactory.getRecognitionEntity(RecognitionEntityType.INSTANCE_ENTITY,
                           word, NLPConstants.NLP_TAG_ONTO_TYPE_REGEX_INSTANCE);
                  updatePositionInfo(recEntity, entity);
                  entity.setWeightInformation(getDummyWeightInformation());
                  if (entity instanceof InstanceEntity) {
                     ((InstanceEntity) entity).setKnowledgeId(riOntoTerm.getKnowledgeId());
                     ((InstanceEntity) entity).setTypeBedId(riOntoTerm.getTypeBEDID());
                     ((InstanceEntity) entity).setTypeDisplayName(riOntoTerm.getTypeName());
                     InstanceInformation information = new InstanceInformation();
                     information.setInstanceValue(recEntity.getWord());
                     information.setInstanceDisplayName(recEntity.getWord());
                     ((InstanceEntity) entity).addInstanceInformation(information);
                     ((OntoEntity) entity).setModelGroupId(riOntoTerm.getModelGroupId());
                  }
                  if (riOntoTerm.getConceptBEDID() != null) {
                     entity.setNLPtag(NLPConstants.NLP_TAG_ONTO_INSTANCE);
                     ((InstanceEntity) entity).setConceptBedId(riOntoTerm.getConceptBEDID());
                     ((InstanceEntity) entity).setConceptDisplayName(riOntoTerm.getConceptName());
                  }
                  // For currency regex instance also populate the normalized data
                  if (ExecueConstants.CURRENCY_CONCEPT.equalsIgnoreCase(riOntoTerm.getConceptName())) {
                     populateCurrencyValueNormalizedData((InstanceEntity) entity, word, entry1.getValue());
                  }
                  entity.setIteration(1);
                  entity.setLevel(1);
                  recoEntities.add(entity);
               }
            }
         }
      }
      if (log.isDebugEnabled()) {
         logInstanceRegexRecognitions(recoEntities);
      }
      return recoEntities;

   }

   /**
    * @param originalRecEntity
    * @param newRecEntity
    */
   private void updatePositionInfo (RecognitionEntity originalRecEntity, RecognitionEntity newRecEntity) {
      ReferedTokenPosition rtp = new ReferedTokenPosition(originalRecEntity.getReferedTokenPositions());
      newRecEntity.setPosition(rtp.getReferedTokenPositions().first());
      newRecEntity.setOriginalReferedTokenPositions(rtp.getReferedTokenPositionsList());
      newRecEntity.setReferedTokenPositions(rtp.getReferedTokenPositionsList());
   }

   private void logInstanceRegexRecognitions (List<IWeightedEntity> recoEntities) {
      StringBuffer sb = new StringBuffer();
      sb.append("\n################## Instance regex Recognitions ###########################");
      for (IWeightedEntity recEnt : recoEntities) {
         sb.append("\n\t\t").append(recEnt);
      }
      sb.append("\n###############################################");
      log.debug(sb.toString());
   }

   private void populateCurrencyValueNormalizedData (InstanceEntity entity, String word, Pattern currencyValuePattern) {

      Matcher matcher = currencyValuePattern.matcher(word);
      String operatorSymbol = "";
      String unitSymbol = "";
      String number = "";
      String unitScale = "";
      while (matcher.find()) {
         if (matcher.group(2) != null) {
            operatorSymbol = matcher.group(1);
            if (StringUtils.isEmpty(operatorSymbol)) {
               operatorSymbol = OperatorType.EQUALS.getValue();
            }
            unitSymbol = matcher.group(2);
            number = matcher.group(3);
            unitScale = matcher.group(7);
            if (getNlpConfigurationService().getUnitScaleMap().containsKey(unitScale.toLowerCase())) {
               unitScale = getNlpConfigurationService().getUnitScaleMap().get(unitScale);
            }
            if (getNlpConfigurationService().getUnitSymbolMap().containsKey(unitSymbol)) {
               unitSymbol = getNlpConfigurationService().getUnitSymbolMap().get(unitSymbol);
            }
         } else {
            operatorSymbol = matcher.group(8);
            if (StringUtils.isEmpty(operatorSymbol)) {
               operatorSymbol = OperatorType.EQUALS.getValue();
            }
            unitSymbol = matcher.group(14);
            number = matcher.group(9);
            unitScale = matcher.group(13);
            if (getNlpConfigurationService().getUnitScaleMap().containsKey(unitScale.toLowerCase())) {
               unitScale = getNlpConfigurationService().getUnitScaleMap().get(unitScale);
            }
            if (getNlpConfigurationService().getUnitSymbolMap().containsKey(unitSymbol)) {
               unitSymbol = getNlpConfigurationService().getUnitSymbolMap().get(unitSymbol);
            }
         }
      }

      ValueRealizationNormalizedData normalizedOPValueData = (ValueRealizationNormalizedData) NormalizedCloudDataFactory
               .getNormalizedCloudData(NormalizedDataType.VALUE_NORMALIZED_DATA);
      normalizedOPValueData.setValueTypeBeId(entity.getTypeBedId());
      normalizedOPValueData.setType(entity.getTypeDisplayName());
      normalizedOPValueData.addReferredTokenPositions(entity.getReferedTokenPositions());
      normalizedOPValueData.addOriginalReferredTokenPositions(entity.getOriginalReferedTokenPositions());

      NormalizedDataEntity operatorDataEntity = getNormalizeDataEntity(operatorSymbol, RecognizedType.OPERATOR_TYPE
               .getValue());
      operatorDataEntity.setDisplaySymbol(operatorSymbol);
      normalizedOPValueData.setOperator(operatorDataEntity);

      NormalizedDataEntity unitSymbolDataEntity = getNormalizeDataEntity(unitSymbol, RecognizedType.UNIT_SYMBOL_TYPE
               .getValue());
      normalizedOPValueData.setUnitSymbol(unitSymbolDataEntity);

      NormalizedDataEntity numberDataEntity = getNormalizeDataEntity(number, RecognizedType.NUMBER_TYPE.getValue());
      normalizedOPValueData.setNumber(numberDataEntity);

      if (!StringUtils.isEmpty(unitScale)) {
         NormalizedDataEntity unitScaleDataEntity = getNormalizeDataEntity(unitScale, RecognizedType.UNIT_SCALE_TYPE
                  .getValue());
         normalizedOPValueData.setUnitScale(unitScaleDataEntity);
      }

      entity.setNormalizedData(normalizedOPValueData);
   }

   private NormalizedDataEntity getNormalizeDataEntity (String value, String typeName) {
      NormalizedDataEntity normalizedDataEntity = new NormalizedDataEntity();
      try {
         RIOntoTerm ontoTerm = getBaseKDXRetrievalService().getOntotermByWordAndTypeName(value, typeName);
         if (ontoTerm != null) {
            normalizedDataEntity.setValueBedId(ontoTerm.getInstanceBEDID());
            normalizedDataEntity.setTypeBedId(ontoTerm.getTypeBEDID());
         }
      } catch (KDXException e) {
         throw new NLPSystemException(e.getCode(), e.getMessage(), e);
      }

      normalizedDataEntity.setValue(value);
      normalizedDataEntity.setDisplayValue(value);
      WeightInformation weightInformation = getDummyWeightInformation();
      normalizedDataEntity.setWeightInformation(weightInformation);
      return normalizedDataEntity;
   }

   private WeightInformation getDummyWeightInformation () {
      WeightInformation weightInformation = new WeightInformation();
      weightInformation.setRecognitionQuality(NLPConstants.MAX_QUALITY);
      weightInformation.setRecognitionWeight(NLPConstants.MAX_WEIGHT);
      return weightInformation;
   }

   @Override
   protected boolean isValidForExecution (Summary input) {
      return true;
   }

   @Override
   protected ProcessorInput initializeProcessorInput (ProcessorInput input) {
      return input;
   }

   @Override
   protected void updateProcessorInput (ProcessorInput input, ProcessorInput processorInput) {
      // Do nothing
   }

   /**
    * @return the nlpServiceHelper
    */
   public NLPServiceHelper getNlpServiceHelper () {
      return nlpServiceHelper;
   }

   /**
    * @param nlpServiceHelper
    *           the nlpServiceHelper to set
    */
   public void setNlpServiceHelper (NLPServiceHelper nlpServiceHelper) {
      this.nlpServiceHelper = nlpServiceHelper;
   }

   /**
    * @return the kdxModelService
    */
   public IKDXModelService getKdxModelService () {
      return kdxModelService;
   }

   /**
    * @param kdxModelService
    *           the kdxModelService to set
    */
   public void setKdxModelService (IKDXModelService kdxModelService) {
      this.kdxModelService = kdxModelService;
   }
}