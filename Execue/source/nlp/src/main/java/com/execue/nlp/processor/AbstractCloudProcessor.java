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


package com.execue.nlp.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.bean.nlp.RecognizedType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.CloudCategory;
import com.execue.core.common.type.OperatorType;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.association.AssociationPath;
import com.execue.nlp.bean.entity.Association;
import com.execue.nlp.bean.entity.ConceptEntity;
import com.execue.nlp.bean.entity.EntityFactory;
import com.execue.nlp.bean.entity.InstanceEntity;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.PropertyEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.bean.entity.ReferedTokenPosition;
import com.execue.nlp.bean.entity.TypeEntity;
import com.execue.nlp.configuration.INLPConfigurationService;
import com.execue.nlp.exception.NLPExceptionCodes;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.nlp.helper.NLPServiceHelper;
import com.execue.nlp.util.NLPUtilities;
import com.execue.ontology.exceptions.OntologyException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;

/**
 * @author Abhijit Patil
 * @since Feb 9, 2010 : 11:49:31 AM
 */

public abstract class AbstractCloudProcessor implements IProcessor {

   private INLPConfigurationService  nlpConfigurationService;
   private IKDXCloudRetrievalService kdxCloudRetrievalService;
   private NLPServiceHelper          nlpServiceHelper;
   private IKDXRetrievalService      kdxRetrievalService;

   /**
    * Retrieves the cloud entities. <BR>
    * All the processors should override this method to provide the specific implementation.
    * 
    * @param input
    * @return
    * @throws NLPSystemException
    */
   public abstract List<RecognizedCloudEntity> getClouds (ProcessorInput input) throws NLPSystemException;

   // Entry point for processing all types of clouds
   public void process (ProcessorInput input) throws NLPSystemException {
      // check for multiple occurrences of types
      groupOccurrencesOfSimilarType(input);
      // check for unification.
      performUnification(input);
      // obtain the cloud entities
      List<RecognizedCloudEntity> cloudEntities = getClouds(input);
      // process each of the cloud entities
      if (ExecueCoreUtil.isCollectionNotEmpty(cloudEntities)) {
         for (RecognizedCloudEntity cloudEntity : cloudEntities) {
            processCloud(cloudEntity, input);
         }
      }
   }

   private void performUnification (ProcessorInput input) {
      Map<Long, List<RecognitionEntity>> recEntitiesByIdMap = input.getRecEntitiesByIdMap();
      Map<Integer, List<IWeightedEntity>> recEntitiesByIndividualPositionMap = NLPUtilities
               .getRecognitionEntitiesByPositionMap(input.getRecognitionEntities());
      Map<ReferedTokenPosition, List<IWeightedEntity>> recEntitiesByRTPMap = NLPUtilities
               .getRecognitionEntitiesByRTPMap(input.getRecognitionEntities());
      Map<Long, List<RecognitionEntity>> unifiedEntitiesByModelGroup = new HashMap<Long, List<RecognitionEntity>>(1);
      for (Entry<Long, List<RecognitionEntity>> entry : recEntitiesByIdMap.entrySet()) {
         if (entry.getValue().size() < 2) {
            continue;
         }
         List<RecognitionEntity> recognitionEntities = entry.getValue();
         NLPUtilities.sortRecognitionEntitiesByRTP(recognitionEntities);
         // Prepare the rec entities by position linked hash map for start and end(if is different) position of the
         // entity
         Map<Integer, List<RecognitionEntity>> recEntitiesByStartAndEndPositionMap = new TreeMap<Integer, List<RecognitionEntity>>();
         for (RecognitionEntity weightedEntity : recognitionEntities) {
            Integer position = weightedEntity.getStartPosition();
            List<RecognitionEntity> recEntities = recEntitiesByStartAndEndPositionMap.get(position);
            if (recEntities == null) {
               recEntities = new ArrayList<RecognitionEntity>(1);
               recEntitiesByStartAndEndPositionMap.put(position, recEntities);
            }
            recEntities.add(weightedEntity);
            if (!position.equals(weightedEntity.getEndPosition())) {
               position = weightedEntity.getEndPosition();
               recEntities = recEntitiesByStartAndEndPositionMap.get(position);
               if (recEntities == null) {
                  recEntities = new ArrayList<RecognitionEntity>(1);
                  recEntitiesByStartAndEndPositionMap.put(position, recEntities);
               }
               recEntities.add(weightedEntity);
            }
         }
         // TODO: NK: Enhance the unification logic to be performed at entity level(covering its full position i.e RTP)
         // but
         // not as below which is done at individual position level
         Integer position = null;
         for (Entry<Integer, List<RecognitionEntity>> recEntitiesByPositionEntry : recEntitiesByStartAndEndPositionMap
                  .entrySet()) {
            for (RecognitionEntity recEntity : recEntitiesByPositionEntry.getValue()) {
               if (position == null) {
                  break;
               }
               List<RecognitionEntity> recEntityList = recEntitiesByStartAndEndPositionMap.get(position);
               if (CollectionUtils.isEmpty(recEntityList)) {
                  break;
               }
               checkAndPerformUnification(recEntity, recEntityList, recEntitiesByIndividualPositionMap,
                        recEntitiesByRTPMap, unifiedEntitiesByModelGroup, input);
            }
            position = recEntitiesByPositionEntry.getKey();
         }
      }
      input.setUnifiedEntitiesByModelGroup(unifiedEntitiesByModelGroup);
   }

   private void checkAndPerformUnification (RecognitionEntity recEntity, List<RecognitionEntity> recEntityList,
            Map<Integer, List<IWeightedEntity>> recEntitesByPosMap,
            Map<ReferedTokenPosition, List<IWeightedEntity>> recEntitiesByRTPMap,
            Map<Long, List<RecognitionEntity>> unifiedEntitiesByModelGroup, ProcessorInput processorInput) {
      RecognitionEntity mergedEntity = null;
      if (recEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY) {
         for (RecognitionEntity conceptRecEntity : recEntityList) {
            // Check for concept rec entity for unification as the input rec entity is instance rec entity
            if (conceptRecEntity.getEntityType() == RecognitionEntityType.CONCEPT_ENTITY
                     || conceptRecEntity.getEntityType() == RecognitionEntityType.TYPE_ENTITY) {
               if (recEntity.getNLPtag().equals(NLPConstants.NLP_TAG_ONTO_TYPE_INSTANCE)
                        || recEntity.getNLPtag().equals(NLPConstants.NLP_TAG_ONTO_REALIZED_TYPE_INSTANCE)) {
                  mergedEntity = unifyIfValid(conceptRecEntity, recEntity, recEntitesByPosMap);
                  // if entity is merged correctly populate the unified entity map.
                  if (mergedEntity != null && !mergedEntity.equals(recEntity)) {
                     populateUnifiedEntities(recEntity, mergedEntity, conceptRecEntity, recEntitiesByRTPMap,
                              unifiedEntitiesByModelGroup, processorInput);
                  }
               }
               if (conceptRecEntity.getNLPtag().equals(NLPConstants.NLP_TAG_ONTO_CONCEPT)
                        && recEntity.getNLPtag().equals(NLPConstants.NLP_TAG_ONTO_INSTANCE)) {
                  mergedEntity = unifyIfValid(conceptRecEntity, recEntity, recEntitesByPosMap);
                  // if entity is merged correctly populate the unified entity map.
                  if (mergedEntity != null && !mergedEntity.equals(recEntity)) {
                     populateUnifiedEntities(recEntity, mergedEntity, conceptRecEntity, recEntitiesByRTPMap,
                              unifiedEntitiesByModelGroup, processorInput);
                  }
               }
            }
         }
      } else if (recEntity.getEntityType() == RecognitionEntityType.CONCEPT_ENTITY
               || recEntity.getEntityType() == RecognitionEntityType.TYPE_ENTITY) {
         for (RecognitionEntity instanceRecEntity : recEntityList) {
            // Check for instance rec entity for unification as the input rec entity is concept or type rec entity
            if (instanceRecEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY) {
               if (recEntity.getNLPtag().equals(NLPConstants.NLP_TAG_ONTO_TYPE)
                        && instanceRecEntity.getNLPtag().equals(NLPConstants.NLP_TAG_ONTO_TYPE_INSTANCE)) {
                  mergedEntity = unifyIfValid(recEntity, instanceRecEntity, recEntitesByPosMap);
                  if (mergedEntity != null && !mergedEntity.equals(instanceRecEntity)) {
                     populateUnifiedEntities(instanceRecEntity, mergedEntity, recEntity, recEntitiesByRTPMap,
                              unifiedEntitiesByModelGroup, processorInput);
                  }
               }
               if (recEntity.getNLPtag().equals(NLPConstants.NLP_TAG_ONTO_CONCEPT)
                        && (instanceRecEntity.getNLPtag().equals(NLPConstants.NLP_TAG_ONTO_INSTANCE)
                                 || instanceRecEntity.getNLPtag().equals(NLPConstants.NLP_TAG_ONTO_TYPE_INSTANCE) || instanceRecEntity
                                 .getNLPtag().equals(NLPConstants.NLP_TAG_ONTO_REALIZED_TYPE_INSTANCE))) {
                  mergedEntity = unifyIfValid(recEntity, instanceRecEntity, recEntitesByPosMap);
                  if (mergedEntity != null && !mergedEntity.equals(instanceRecEntity)) {
                     populateUnifiedEntities(instanceRecEntity, mergedEntity, recEntity, recEntitiesByRTPMap,
                              unifiedEntitiesByModelGroup, processorInput);
                  }
               }
            }
         }
      }
   }

   private void updateUnificationWeightInfo (RecognitionEntity mergedEntity) {
      Long cloudId = getNlpConfigurationService().getUnificationFrameworkCloudId();
      mergedEntity.addFoundFramework(cloudId, NLPUtilities.getDefaultWeightInformation());

   }

   /**
    * @param recEntity
    * @param mergedEntity
    * @param conceptEntity
    * @param recEntitiesByRTPMap
    * @param unifiedEntitiesByModelGroup
    * @param processorInput
    */
   private void populateUnifiedEntities (RecognitionEntity recEntity, RecognitionEntity mergedEntity,
            RecognitionEntity conceptEntity, Map<ReferedTokenPosition, List<IWeightedEntity>> recEntitiesByRTPMap,
            Map<Long, List<RecognitionEntity>> unifiedEntitiesByModelGroup, ProcessorInput processorInput) {

      // Update the unification-framework-cloud weight information in the merged entity
      updateUnificationWeightInfo(mergedEntity);

      processorInput.getRecognitionEntities().add(mergedEntity);
      // if instance entity is already qualifed and is merged also the single one can be removed.
      InstanceEntity instanceEntity = (InstanceEntity) recEntity;
      List<IWeightedEntity> entitiesAtInstancePos = recEntitiesByRTPMap.get(new ReferedTokenPosition(instanceEntity
               .getReferedTokenPositions()));
      // get the entities by the conce3pt position.
      List<IWeightedEntity> entitiesAtConceptPos = recEntitiesByRTPMap.get(new ReferedTokenPosition(conceptEntity
               .getReferedTokenPositions()));
      OntoEntity ontoEntity = (OntoEntity) mergedEntity;
      Long modelGroupId = ontoEntity.getModelGroupId();
      // get the entities by the modelGroupId.
      Map<Long, List<IWeightedEntity>> entitiesAtConceptPosByModelGroup = NLPUtilities
               .getRecEntitiesMapByModelGroup(entitiesAtConceptPos);
      // get the list of entities for the current modelGroup.
      List<IWeightedEntity> entitiesAtConceptPosForCurrentMOdel = entitiesAtConceptPosByModelGroup.get(modelGroupId);
      // if only one entity exist in that model for that position set the entities can be unified and merged together .
      // there is no need of keeping the single entities.
      if (entitiesAtConceptPosForCurrentMOdel != null && entitiesAtConceptPosForCurrentMOdel.size() == 1) {
         if (instanceEntity.getConceptBedId() != null) {
            processorInput.getRecognitionEntities().remove(instanceEntity);
         }

         if (modelGroupId != null) {
            List<RecognitionEntity> entities = unifiedEntitiesByModelGroup.get(modelGroupId);
            if (entities == null) {
               entities = new ArrayList<RecognitionEntity>(1);
               unifiedEntitiesByModelGroup.put(modelGroupId, entities);
            }
            entities.add(recEntity);
         }
      }
   }

   private RecognitionEntity unifyIfValid (RecognitionEntity conceptEntity, RecognitionEntity instanceEntity,
            Map<Integer, List<IWeightedEntity>> recEntitesByPosMap) {
      // move validation to the above method
      ReferedTokenPosition rtp1 = new ReferedTokenPosition(instanceEntity.getReferedTokenPositions());
      ReferedTokenPosition rtp2 = new ReferedTokenPosition(conceptEntity.getReferedTokenPositions());
      if (rtp1.isSubset(rtp2) || rtp2.isSubset(rtp1)) {
         return instanceEntity;
      }
      if (((TypeEntity) conceptEntity).getTypeDisplayName().equals(RecognizedType.VALUE_TYPE.toString())) {
         return instanceEntity;
      }
      // check if the concept Entity has concept, mark the instance Entity as CLI
      List<Integer> inBetweenPositions = rtp1.getInBetweenPositions(rtp2);
      if (CollectionUtils.isEmpty(inBetweenPositions)) {
         instanceEntity = mergeEntitiesForUnification(conceptEntity, instanceEntity, null);
      } else if (inBetweenPositions.size() == 1 && recEntitesByPosMap.get(inBetweenPositions.get(0)) != null) {
         List<IWeightedEntity> entities = recEntitesByPosMap.get(inBetweenPositions.get(0));
         for (IWeightedEntity entity : entities) {
            RecognitionEntity weightedEntity = (RecognitionEntity) entity;
            if (weightedEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY) {
               InstanceEntity inBetweenEntity = (InstanceEntity) weightedEntity;
               // unify only for operator "=" and conjunctions
               if (inBetweenEntity.getTypeDisplayName().equals(ExecueConstants.CONJUNCTION_TYPE)
                        || OperatorType.EQUALS.getValue().equals(
                                 nlpConfigurationService.getDisplaySymbolBasedOnInstanceName(inBetweenEntity
                                          .getDefaultInstanceValue()))) {
                  instanceEntity = mergeEntitiesForUnification(conceptEntity, instanceEntity, inBetweenEntity);
                  break;
               }
            }
         }
      }
      return instanceEntity;
   }

   private RecognitionEntity mergeEntitiesForUnification (RecognitionEntity conceptEntity,
            RecognitionEntity instanceEntity, InstanceEntity inBetweenEntity) {
      RecognitionEntity newInstanceEntity = null;
      try {
         newInstanceEntity = (RecognitionEntity) instanceEntity.clone();
         if (conceptEntity instanceof ConceptEntity) {
            ConceptEntity conEntity = (ConceptEntity) conceptEntity;
            if (instanceEntity instanceof InstanceEntity) {
               InstanceEntity instEntity = (InstanceEntity) newInstanceEntity;
               instEntity.setConceptBedId(conEntity.getConceptBedId());
               instEntity.setConceptDisplayName(conEntity.getConceptDisplayName());
               instEntity.setNLPtag(NLPConstants.NLP_TAG_ONTO_INSTANCE);
            }
         }
      } catch (CloneNotSupportedException e) {
         throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
      }
      newInstanceEntity.getOriginalReferedTokenPositions().addAll(conceptEntity.getOriginalReferedTokenPositions());
      newInstanceEntity.getReferedTokenPositions().addAll(conceptEntity.getReferedTokenPositions());
      WeightInformation weightInformation = new WeightInformation();
      double quality = (instanceEntity.getRecognitionQuality() + conceptEntity.getRecognitionQuality()) / 2;
      double weight = instanceEntity.getRecognitionWeight() + conceptEntity.getRecognitionWeight();
      if (inBetweenEntity != null) {
         quality = (instanceEntity.getRecognitionQuality() + conceptEntity.getRecognitionQuality() + inBetweenEntity
                  .getRecognitionQuality()) / 3;
         weight = instanceEntity.getRecognitionWeight() + conceptEntity.getRecognitionWeight()
                  + inBetweenEntity.getRecognitionWeight();
         newInstanceEntity.getOriginalReferedTokenPositions()
                  .addAll(inBetweenEntity.getOriginalReferedTokenPositions());
         newInstanceEntity.getReferedTokenPositions().addAll(inBetweenEntity.getReferedTokenPositions());
      }
      weightInformation.setRecognitionQuality(quality);
      weightInformation.setRecognitionWeight(weight);
      newInstanceEntity.setWeightInformation(weightInformation);
      ReferedTokenPosition newPosition = new ReferedTokenPosition(newInstanceEntity.getReferedTokenPositions());
      newInstanceEntity.setStartPosition(newPosition.getReferedTokenPositions().first());
      newInstanceEntity.setEndPosition(newPosition.getReferedTokenPositions().last());
      if (conceptEntity.getEntityType() == RecognitionEntityType.CONCEPT_ENTITY) {
         ((InstanceEntity) newInstanceEntity).setModelGroupId(((ConceptEntity) conceptEntity).getModelGroupId());
      }
      // Update the hits info
      List<RecognitionEntity> inputRecs = new ArrayList<RecognitionEntity>(1);
      inputRecs.add(conceptEntity);
      inputRecs.add(instanceEntity);
      newInstanceEntity.setHitsUpdateInfo(NLPUtilities.getHitsUpdateInfo(inputRecs));
      return newInstanceEntity;
   }

   public void groupOccurrencesOfSimilarType (ProcessorInput input) {
      Map<Long, List<RecognitionEntity>> recEntitiesByIdMap = input.getRecEntitiesByIdMap();
      getNlpServiceHelper().populateRecEntitiesById(recEntitiesByIdMap, input.getRecognitionEntities());
   }

   /**
    * This method processes the cloud entity by applying all the applicable rules.
    * 
    * @param cloudEntity
    * @param processorInput
    * @throws NLPSystemException
    */
   public void processCloud (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput)
            throws NLPSystemException {
      boolean isValid = true;
      // Call gate-keeper method to check if cloud is valid for processing
      if (ifCloudShouldBeProcessed(cloudEntity)) {
         // Process Validation Rules from Database
         isValid = processValidationRules(cloudEntity, processorInput);
         // If valid do the recognition
         if (isValid) {
            processRecognitionRules(cloudEntity, processorInput);
         }
         // Retrieve associations
         retrieveAssociations(cloudEntity, processorInput);

         if (ExecueCoreUtil.isCollectionNotEmpty(cloudEntity.getPaths())) {
            if (cloudEntity.isWeightAssignmentRuleAssociated()) {
               processWeightAssignmentRules(cloudEntity, processorInput);
            }
         }
         try {
            getDefaultConceptForUnassociatedValue(cloudEntity, processorInput);
         } catch (OntologyException e) {
            throw new NLPSystemException(e.code, e);
         } catch (KDXException e) {
            throw new NLPSystemException(e.code, e);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(cloudEntity.getPaths())) {
            // populate AppcloudEntity with the prepared Association Object
            try {
               createAssociation(cloudEntity, processorInput);
            } catch (KDXException e) {
               throw new NLPSystemException(e.code, e);
            }
         }
      }
   }

   public abstract void processRecognitionRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput);

   public abstract void retrieveAssociations (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput);

   public abstract void processWeightAssignmentRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput);

   public void getDefaultConceptForUnassociatedValue (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput)
            throws OntologyException, KDXException {
   }

   protected boolean ifCloudShouldBeProcessed (RecognizedCloudEntity cloudEntity) {
      return nlpServiceHelper.getCloudService(cloudEntity.getCategory()).ifCloudShouldBeProcessed(cloudEntity);
   }

   public boolean processValidationRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      // Validate based on DB rules
      return checkForValidCloudComponents(cloudEntity);
   }

   public boolean checkForValidCloudComponents (RecognizedCloudEntity cloudEntity) {
      boolean isValid = false;
      Set<Long> keysToremove = new HashSet<Long>(1);
      for (Entry<Long, CloudComponent> entry : cloudEntity.getCloudComponentInfoMap().entrySet()) {
         CloudComponent componentInfo = entry.getValue();
         if (cloudEntity.getCategory() == CloudCategory.CONCEPT_CLOUD && componentInfo.getFrequency() > 1) {
            List<IWeightedEntity> weightedEntities = componentInfo.getRecognitionEntities();
            Long typeBed = null;
            for (IWeightedEntity weightedEntity : weightedEntities) {
               TypeEntity typeEntity = (TypeEntity) weightedEntity;
               if (typeBed == null) {
                  typeBed = typeEntity.getTypeBedId();
               } else if (!typeBed.equals(typeEntity.getTypeBedId())) {
                  return false;
               }
            }
         }
         boolean isValidComponent = isValidBasedOnFrequency(componentInfo);
         if (!isValidComponent) {
            if (componentInfo.isCloudComponentRequired()) {
               return false;
            } else {
               keysToremove.add(entry.getKey());
            }
         }
         isValid = isValid || isValidComponent;
      }
      for (Long key : keysToremove) {
         CloudComponent cloudComponent = cloudEntity.getCloudComponentInfoMap().remove(key);
         cloudEntity.getRecognitionEntities().removeAll(cloudComponent.getRecognitionEntities());
      }
      return isValid;
   }

   /**
    * Check is valid based on frequency for the required component.
    * 
    * @param componentInfo
    *           Component to be checked for validation
    * @return
    */
   private boolean isValidBasedOnFrequency (CloudComponent componentInfo) {
      if (componentInfo.isCloudComponentRequired()) {
         if (componentInfo.getFrequency() > componentInfo.getRecognitionEntities().size()) {
            return false;
         }
      }
      return true;
   }

   private void createAssociation (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput)
            throws KDXException {
      List<AssociationPath> associationPathList = cloudEntity.getPaths();
      Map<Long, OntoEntity> recEntityByIdMap = NLPUtilities.getRecognitionEntityByIdMap(cloudEntity
               .getRecognitionEntities());
      Set<Long> bedIds = new HashSet<Long>();
      for (AssociationPath associationPath : associationPathList) {
         PathDefinition pd = associationPath.getPath();
         for (Path path : pd.getPaths()) {
            EntityTripleDefinition entityTripleDefinition = path.getEntityTripleDefinition();
            Long sourceBeId = entityTripleDefinition.getSourceBusinessEntityDefinition().getId();
            Long relationBeId = entityTripleDefinition.getRelation().getId();
            Long destBeId = entityTripleDefinition.getDestinationBusinessEntityDefinition().getId();
            bedIds.add(sourceBeId);
            bedIds.add(relationBeId);
            bedIds.add(destBeId);
         }
      }
      // get the ontoterms for in between path components this gives us a way to avoid lazy loading.
      Map<Long, RIOntoTerm> ontoTermsByEntityBeId = getKdxRetrievalService().getOntoTermsByEntityBeId(bedIds);
      for (AssociationPath associationPath : associationPathList) {
         PathDefinition pd = associationPath.getPath();
         Association association = new Association();
         association.setDefaultPath(associationPath.isDefaultPath());
         List<EntityTripleDefinition> etds = new ArrayList<EntityTripleDefinition>(1);
         for (Path path : pd.getPaths()) {
            etds.add(path.getEntityTripleDefinition());
         }
         List<RecognitionEntity> pathComponents = getPathComponents(association, etds, recEntityByIdMap,
                  ontoTermsByEntityBeId);

         // Replace the start and end rec with the actual source and dest rec
         try {
            pathComponents.set(0, (RecognitionEntity) associationPath.getSource().clone());
            pathComponents.set(pathComponents.size() - 1, (RecognitionEntity) associationPath.getDestination().clone());
         } catch (CloneNotSupportedException e) {
            throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
         }

         association.setPathDefId(pd.getId());
         association.setPathComponent(pathComponents);
         association.setSubjectPostion(((RecognitionEntity) associationPath.getSource()).getPosition());
         association.setObjectPostion(((RecognitionEntity) associationPath.getDestination()).getPosition());
         association.setWeightInformation(associationPath.getWeightInformation());
         association.setUnAllowedRecs(associationPath.getUnAllowedRecognitions());
         association.setAllowedRecs(associationPath.getAllowedRecognitions());
         boolean explicitAssociationExists = checkIfExplicitAssociationExists(association);
         // If no explicit association exists, then mark it as implicit token association
         association.setForImplicitToken(!explicitAssociationExists);
         processorInput.addAssociationForModel(cloudEntity.getModelGroupId(), association);
      }
   }

   private boolean checkIfExplicitAssociationExists (Association association) {
      boolean explicitAssociationFound = false;
      List<RecognitionEntity> pathComponents = association.getPathComponent();
      if (CollectionUtils.isEmpty(pathComponents)) {
         return explicitAssociationFound;
      }
      int index = 0;
      do {
         int srcPosition = pathComponents.get(index).getPosition();
         int destPosition = pathComponents.get(index + 2).getPosition();
         // Check if both the source and destination are explicit 
         if (srcPosition >= 0 && destPosition >= 0) {
            explicitAssociationFound = true;
            break;
         }
         index = index + 2;
      } while (index < pathComponents.size() - 1);
      return explicitAssociationFound;
   }

   private List<RecognitionEntity> getPathComponents (Association association, List<EntityTripleDefinition> tripleList,
            Map<Long, OntoEntity> recEntityByIdMap, Map<Long, RIOntoTerm> ontoTermsByEntityBeId) {
      List<RecognitionEntity> pathComponents = new ArrayList<RecognitionEntity>(1);
      int triplesSize = tripleList.size();
      BusinessEntityDefinition prevDest = null;
      int cardinality = -1;
      boolean instanceTripleExist = false;
      ReferedTokenPosition rtp = new ReferedTokenPosition();
      for (int i = 0; i < triplesSize; i++) {
         EntityTripleDefinition triple = tripleList.get(i);
         if (cardinality == -1 || triple.getCardinality() < cardinality) {
            cardinality = triple.getCardinality();
         }
         if (!instanceTripleExist) {
            instanceTripleExist = triple.getInstanceTripleExists() == CheckType.YES;
         }
         BusinessEntityDefinition sourceBusinessEntityDefinition = triple.getSourceBusinessEntityDefinition();
         OntoEntity ontoEntity = null;
         if (prevDest == null || prevDest.getId() != sourceBusinessEntityDefinition.getId()) {
            ontoEntity = recEntityByIdMap.get(sourceBusinessEntityDefinition.getId());
            if (ontoEntity != null && !rtp.isOverLap(new ReferedTokenPosition(ontoEntity.getReferedTokenPositions()))) {
               rtp.addAll(ontoEntity.getReferedTokenPositions());
               pathComponents.add(ontoEntity);
            } else {
               pathComponents.add(getConceptRecognitionEntity(ontoTermsByEntityBeId.get(sourceBusinessEntityDefinition
                        .getId())));
            }
         }
         BusinessEntityDefinition relation = triple.getRelation();
         ontoEntity = recEntityByIdMap.get(relation.getId());
         if (ontoEntity != null && !rtp.isOverLap(new ReferedTokenPosition(ontoEntity.getReferedTokenPositions()))) {
            rtp.addAll(ontoEntity.getReferedTokenPositions());
            pathComponents.add(recEntityByIdMap.get(relation.getId()));
         } else {
            pathComponents.add(getRelationRecognitionEntity(ontoTermsByEntityBeId.get(relation.getId())));
         }
         BusinessEntityDefinition destinationBusinessEntityDefinition = triple.getDestinationBusinessEntityDefinition();
         ontoEntity = recEntityByIdMap.get(destinationBusinessEntityDefinition.getId());
         if (ontoEntity != null && !rtp.isOverLap(new ReferedTokenPosition(ontoEntity.getReferedTokenPositions()))) {
            rtp.addAll(ontoEntity.getReferedTokenPositions());
            pathComponents.add(ontoEntity);
         } else {
            pathComponents.add(getConceptRecognitionEntity(ontoTermsByEntityBeId
                     .get(destinationBusinessEntityDefinition.getId())));
         }
         prevDest = destinationBusinessEntityDefinition;
      }
      association.setCardinality(cardinality);
      association.setInstanceTripleExist(instanceTripleExist);
      return pathComponents;
   }

   private RecognitionEntity getRelationRecognitionEntity (RIOntoTerm ontoTerm) {
      PropertyEntity entity = (PropertyEntity) EntityFactory.getRecognitionEntity(
               RecognitionEntityType.PROPERTY_ENTITY, ontoTerm.getRelationName(), NLPConstants.NLP_TAG_ONTO_PROPERTY,
               null, 0);
      entity.setModelGroupId(ontoTerm.getModelGroupId());
      entity.setPopularity(ontoTerm.getPopularity());
      entity.setTypeBedId(ontoTerm.getTypeBEDID());
      entity.setTypeDisplayName(ontoTerm.getTypeName());
      entity.setRelationBedId(ontoTerm.getRelationBEDID());
      entity.setRelationDisplayName(ontoTerm.getRelationName());
      entity.setPosition(-1);// NK: setting the negative value
      entity.addReferedTokenPosition(-1);
      entity.setWeightInformation(NLPUtilities.getDefaultWeightInformationForImplicitEntity());
      return entity;

   }

   private RecognitionEntity getConceptRecognitionEntity (RIOntoTerm ontoTerm) {
      String conceptName = ontoTerm.getConceptName();
      String conceptDisplayName = ontoTerm.getConceptName();
      ConceptEntity entity = (ConceptEntity) EntityFactory.getRecognitionEntity(RecognitionEntityType.CONCEPT_ENTITY,
               conceptName, NLPConstants.NLP_TAG_ONTO_CONCEPT, null, 0);
      entity.setModelGroupId(ontoTerm.getModelGroupId());
      entity.setPopularity(ontoTerm.getPopularity());
      entity.setTypeBedId(ontoTerm.getTypeBEDID());
      entity.setTypeDisplayName(ontoTerm.getTypeName());
      entity.setConceptBedId(ontoTerm.getConceptBEDID());
      entity.setConceptDisplayName(conceptDisplayName);
      entity.setPosition(-1); // setting the negative value
      entity.addReferedTokenPosition(-1);
      entity.setWeightInformation(NLPUtilities.getDefaultWeightInformationForImplicitEntity());
      return entity;
   }

   /**
    * @return the nlpConfigurationService
    */
   public INLPConfigurationService getNlpConfigurationService () {
      return nlpConfigurationService;
   }

   /**
    * @param nlpConfigurationService
    *           the nlpConfigurationService to set
    */
   public void setNlpConfigurationService (INLPConfigurationService nlpConfigurationService) {
      this.nlpConfigurationService = nlpConfigurationService;
   }

   /**
    * @return the kdxCloudRetrievalService
    */
   public IKDXCloudRetrievalService getKdxCloudRetrievalService () {
      return kdxCloudRetrievalService;
   }

   /**
    * @param kdxCloudRetrievalService
    *           the kdxCloudRetrievalService to set
    */
   public void setKdxCloudRetrievalService (IKDXCloudRetrievalService kdxCloudRetrievalService) {
      this.kdxCloudRetrievalService = kdxCloudRetrievalService;
   }

   public NLPServiceHelper getNlpServiceHelper () {
      return nlpServiceHelper;
   }

   public void setNlpServiceHelper (NLPServiceHelper nlpServiceHelper) {
      this.nlpServiceHelper = nlpServiceHelper;
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService
    *           the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }
}