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


package com.execue.nlp.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.RISharedUserModelMapping;
import com.execue.core.common.bean.nlp.ListNormalizedData;
import com.execue.core.common.bean.nlp.LocationNormalizedData;
import com.execue.core.common.bean.nlp.LocationNormalizedDataEntity;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.NormalizedDataEntity;
import com.execue.core.common.bean.nlp.RangeNormalizedData;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.bean.nlp.RelativeTimeNormalizedData;
import com.execue.core.common.bean.nlp.TimeFrameNormalizedData;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CloudCategory;
import com.execue.core.common.type.ComponentCategory;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.LocationType;
import com.execue.core.common.type.ModelCategoryType;
import com.execue.core.constants.ExecueConstants;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.entity.AppCloudEntity;
import com.execue.nlp.bean.entity.ConceptEntity;
import com.execue.nlp.bean.entity.EntityFactory;
import com.execue.nlp.bean.entity.InstanceEntity;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.bean.entity.ReferedTokenPosition;
import com.execue.nlp.bean.entity.TypeEntity;
import com.execue.nlp.bean.rules.weight.assignment.WeightAssignmentRulesContent;
import com.execue.nlp.configuration.INLPConfigurationService;
import com.execue.nlp.exception.NLPExceptionCodes;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.nlp.helper.NLPServiceHelper;
import com.execue.nlp.rule.service.impl.PathRulesExecutor;
import com.execue.nlp.service.ICloudService;
import com.execue.nlp.util.NLPUtilities;
import com.execue.ontology.configuration.OntologyConstants;
import com.execue.ontology.exceptions.OntologyException;
import com.execue.ontology.service.IOntologyService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPathDefinitionRetrievalService;
import com.execue.util.MathUtil;

/**
 * @author John Mallavalli
 */
public class AppCloudServiceImpl implements ICloudService {

   private final Logger                    logger = Logger.getLogger(AppCloudServiceImpl.class);
   private NLPServiceHelper                nlpServiceHelper;
   private PathRulesExecutor               pathRulesExecutor;
   private IKDXModelService                kdxModelService;
   private IOntologyService                ontologyService;
   private INLPConfigurationService        nlpConfigurationService;
   private IKDXRetrievalService            kdxRetrievalService;
   private IKDXCloudRetrievalService       kdxCloudRetrievalService;
   private IPathDefinitionRetrievalService pathDefinitionRetrievalService;

   public boolean ifCloudShouldBeProcessed (RecognizedCloudEntity cloudEntity) {
      return true;
   }

   public boolean processValidationRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      return true;
   }

   public void processRecognitionRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      processDefaultRecognitionRules(cloudEntity, processorInput);
   }

   private void processDefaultRecognitionRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      performList(processorInput, cloudEntity);

      getCentralConcepts(processorInput, cloudEntity);

      for (IWeightedEntity entity : cloudEntity.getRecognitionEntities()) {
         OntoEntity ontoEntity = (OntoEntity) entity;
         ontoEntity.setAppCloudId(cloudEntity.getCloud().getId());
      }
   }

   public void processWeightAssignmentRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      WeightAssignmentRulesContent weightAssignmentRulesContent = getNlpConfigurationService()
               .getWeightAssignmentRulesContent();
      // Apply the weight assignment rules
      applyWeightAssignmentRules(cloudEntity, processorInput, weightAssignmentRulesContent);
   }

   /**
    * This method returns the list of the AppCloudEntity
    * 
    * @param riClouds
    * @param recognitionEntities
    * @return the List<AppCloudEntity>
    * @throws KDXException
    */
   public List<RecognizedCloudEntity> getCloudEntities (List<RICloud> riClouds,
            List<IWeightedEntity> recognitionEntities, ProcessorInput processorInput) {
      List<RecognizedCloudEntity> appCloudEntities = new ArrayList<RecognizedCloudEntity>(1);
      List<RecognizedCloudEntity> finalCloudEntities = new ArrayList<RecognizedCloudEntity>(1);

      try {
         if (CollectionUtils.isEmpty(riClouds)) {
            return appCloudEntities;
         }
         // Get Map of Cloud against Cloud Component Entry
         Map<Long, List<RICloud>> riCloudsByCloudId = nlpServiceHelper.groupAppCloudComponents(riClouds, processorInput
                  .isFromArticle());

         // Get the app cloud entity by cloud id map, also populate the model group id for app cloud
         Map<Long, AppCloudEntity> appCloudEntityByCloudIdMap = getAppCloudEntityByCloudIdMap(riCloudsByCloudId);

         // Separate Concept and Type RI Entries
         Map<Long, List<IWeightedEntity>> recognitionEntitiesByTypeBedMap = getNlpServiceHelper()
                  .getRecognitionEntitiesByTypeBedMap(recognitionEntities);
         Map<Long, List<IWeightedEntity>> recognitionEntitiesByComponentBedMap = NLPUtilities
                  .getRecognizedEntitiesByComponentBedIdMap(recognitionEntities);

         Integer currentIteration = nlpServiceHelper.getMaxIteration(recognitionEntities);
         List<Long> realizationBedIds = new ArrayList<Long>(1);
         Map<Long, List<RICloud>> riCloudsByTypeBedId = new HashMap<Long, List<RICloud>>(1);
         for (Entry<Long, List<RICloud>> entry : riCloudsByCloudId.entrySet()) {
            Long cloudId = entry.getKey();
            List<RICloud> appRIClouds = entry.getValue();

            AppCloudEntity appCloudEntity = appCloudEntityByCloudIdMap.get(cloudId);

            // appCloudEntity.setCloud(cloud);
            boolean cloudSet = false;
            // For each cloud go through its RI entries
            for (RICloud riCloud : appRIClouds) {
               if (!cloudSet) {
                  cloudSet = true;
                  Cloud cloud = new Cloud();
                  cloud.setName(riCloud.getCloudName());
                  cloud.setId(riCloud.getCloudId());
                  cloud.setCategory(CloudCategory.APP_CLOUD);
                  appCloudEntity.setCloud(cloud);
               }
               // Process if Component is Type
               if (riCloud.getComponentCategory() == ComponentCategory.TYPE
                        || riCloud.getRepresentativeEntityType() == BusinessEntityType.REALIZED_TYPE) {
                  realizationBedIds.add(riCloud.getRealizationBusinessEntityId());
                  Long compTypeBedId = riCloud.getComponentTypeBusinessEntityId();
                  // If child of location, then consider the type as location itself
                  if (getNlpServiceHelper().getLocationConfigurationService().isChildOfLocationType(compTypeBedId)) {
                     compTypeBedId = getNlpServiceHelper().getLocationConfigurationService()
                              .getParentBedIdForChildBedId(compTypeBedId);
                  }
                  List<RICloud> riCloudsForType = riCloudsByTypeBedId.get(compTypeBedId);
                  if (CollectionUtils.isEmpty(riCloudsForType)) {
                     riCloudsForType = new ArrayList<RICloud>(1);
                     riCloudsByTypeBedId.put(compTypeBedId, riCloudsForType);
                  }
                  riCloudsForType.add(riCloud);
               } else {
                  List<IWeightedEntity> recEntities = recognitionEntitiesByComponentBedMap.get(riCloud
                           .getComponentBusinessEntityId());
                  if (!CollectionUtils.isEmpty(recEntities)) {
                     addRecEntitiesToAppCloud(currentIteration, appCloudEntity, riCloud, recEntities);
                  } else {
                     // Check if it exists in type bed map
                     Long componentBusinessEntityId = riCloud.getComponentBusinessEntityId();
                     if (getNlpServiceHelper().getLocationConfigurationService().isChildOfLocationType(
                              componentBusinessEntityId)) {
                        componentBusinessEntityId = ExecueConstants.LOCATION_TYPE_BED_ID;
                     }
                     recEntities = recognitionEntitiesByTypeBedMap.get(componentBusinessEntityId);
                     if (!CollectionUtils.isEmpty(recEntities)) {
                        addRecEntitiesToAppCloud(currentIteration, appCloudEntity, riCloud, recEntities);

                     }
                  }
               }
            }
            if (!CollectionUtils.isEmpty(appCloudEntity.getRecognitionEntities())
                     && (appCloudEntity.getModelGroupId() != null || !appCloudEntity.getModelGroupId().equals(0L))) {
               appCloudEntities.add(appCloudEntity);
            } else if (processorInput.isFromArticle() && !CollectionUtils.isEmpty(realizationBedIds)) {
               appCloudEntities.add(appCloudEntity);
            }
         }
         if (!CollectionUtils.isEmpty(realizationBedIds)) {
            processTypeRIClouds(recognitionEntitiesByTypeBedMap, currentIteration, appCloudEntities,
                     riCloudsByTypeBedId, realizationBedIds, processorInput);

         }
         for (RecognizedCloudEntity appCloudEntity : appCloudEntities) {
            // Skip if no recognitions founds for an app
            List<IWeightedEntity> appRecognitionEntities = appCloudEntity.getRecognitionEntities();
            if (CollectionUtils.isEmpty(appRecognitionEntities)) {
               continue;
            }
            // Filter Logic For AppCloud Entity.
            performFilterLogic(appCloudEntity);

            // TODO -NA- check if the filteration logic on the basis of attributes goes here
            boolean isOnlyForAttrbutes = checkForNonAttributeEntity((AppCloudEntity) appCloudEntity);
            if (!isOnlyForAttrbutes || processorInput.isFromArticle()) {
               finalCloudEntities.add(appCloudEntity);
            }
         }

      } catch (SWIException kdxException) {
         throw new NLPSystemException(kdxException.getCode(), kdxException.getMessage(), kdxException.getCause());
      }
      finalCloudEntities = filterAppCloudEntitiesBasedOnRecognitionWeight(finalCloudEntities);
      return finalCloudEntities;
   }

   private Map<Long, AppCloudEntity> getAppCloudEntityByCloudIdMap (Map<Long, List<RICloud>> riCloudsByCloudIdMap)
            throws KDXException {
      Map<Long, AppCloudEntity> appCloudEntityByCloudIdMap = new HashMap<Long, AppCloudEntity>(1);
      Long baseGroupModelId = nlpServiceHelper.getBaseGroupId();
      for (Entry<Long, List<RICloud>> entry : riCloudsByCloudIdMap.entrySet()) {
         Long cloudId = entry.getKey();
         List<RICloud> riClouds = entry.getValue();
         AppCloudEntity appCloudEntity = new AppCloudEntity();
         appCloudEntity.setModelGroupId(getAppModelGroupIdFromRICloud(riClouds, baseGroupModelId));
         appCloudEntityByCloudIdMap.put(cloudId, appCloudEntity);
      }
      return appCloudEntityByCloudIdMap;
   }

   private Long getAppModelGroupIdFromRICloud (List<RICloud> riClouds, Long baseGroupModelId) throws KDXException {
      Long modelGroupId = 0L;

      // First, try to get the model group id from the ri-cloud entries itself 
      for (RICloud riCloud : riClouds) {
         // We are picking the first model group id in the list which is not base model group id and setting it as
         // app cloud entity model group id
         if (!riCloud.getModelGroupId().equals(baseGroupModelId)) {
            modelGroupId = riCloud.getModelGroupId();
            return modelGroupId;
         }
      }

      // Hit the db to get the model group id from the cloud
      Cloud cloud = kdxCloudRetrievalService.getCloudById(riClouds.get(0).getCloudId());
      Set<Model> models = cloud.getModels();
      if (!CollectionUtils.isEmpty(models)) {
         for (Model model : models) {
            Set<ModelGroup> modelGroups = model.getModelGroups();
            for (ModelGroup modelGroup : modelGroups) {
               if (modelGroup.getCategory() == ModelCategoryType.USER) {
                  return modelGroup.getId();
               }
            }
         }
      }

      return modelGroupId;
   }

   /**
    * @param currentIteration
    * @param appCloudEntity
    * @param riCloud
    * @param recEntities
    */
   private void addRecEntitiesToAppCloud (Integer currentIteration, AppCloudEntity appCloudEntity, RICloud riCloud,
            List<IWeightedEntity> recEntities) {
      for (IWeightedEntity renEntity : recEntities) {
         OntoEntity ontoEntity = (OntoEntity) renEntity;
         ontoEntity.setModelGroupId(riCloud.getModelGroupId());
         // NK: Increment the iteration and mark the level as app cloud i.e. 3
         ontoEntity.setIteration(currentIteration + 1);
         ontoEntity.setLevel(3);
         appCloudEntity.addRecognitionEntity(ontoEntity);
      }
   }

   private void processTypeRIClouds (Map<Long, List<IWeightedEntity>> recognitionEntitiesByTypeBedId,
            Integer currentIteration, List<RecognizedCloudEntity> appCloudEntities,
            Map<Long, List<RICloud>> riCloudsByTypeBedId, List<Long> realizationBedIds, ProcessorInput processorInput)
            throws SWIException {
      Set<Long> realizations = new HashSet<Long>(realizationBedIds);
      List<Set<Long>> convertableBedsList = getPathDefinitionRetrievalService().getConvertableTypePaths(
               new ArrayList<Long>(realizations));
      updateConvertableBedsList(convertableBedsList, processorInput);
      Map<Long, RIOntoTerm> ontoTermsByRealizationId = getKdxRetrievalService().getConceptOntoTermsByConceptBedIds(
               new HashSet<Long>(realizationBedIds));

      Set<Long> sharedModelBedIds = new HashSet<Long>();
      for (Entry<Long, List<IWeightedEntity>> entry : recognitionEntitiesByTypeBedId.entrySet()) {
         List<IWeightedEntity> weightedEntities = entry.getValue();
         for (IWeightedEntity weightedEntity : weightedEntities) {
            if (((RecognitionEntity) weightedEntity).getEntityType() == RecognitionEntityType.INSTANCE_ENTITY) {
               InstanceEntity instanceEntity = (InstanceEntity) weightedEntity;
               INormalizedData normalizedData = instanceEntity.getNormalizedData();
               if (normalizedData != null) {
                  if (normalizedData.getNormalizedDataType() == NormalizedDataType.LOCATION_NORMALIZED_DATA) {
                     LocationNormalizedData locationNormalizedData = (LocationNormalizedData) normalizedData;
                     sharedModelBedIds.addAll(getLocationValueBedIds(locationNormalizedData));
                  } else if (normalizedData.getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA
                           && ((ListNormalizedData) normalizedData).getNormalizedDataEntities().get(0)
                                    .getNormalizedData().getNormalizedDataType() == NormalizedDataType.LOCATION_NORMALIZED_DATA) {
                     List<NormalizedDataEntity> normalizedDataEntities = ((ListNormalizedData) normalizedData)
                              .getNormalizedDataEntities();
                     for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
                        LocationNormalizedData locationNormalizedData = (LocationNormalizedData) normalizedDataEntity
                                 .getNormalizedData();
                        sharedModelBedIds.addAll(getLocationValueBedIds(locationNormalizedData));
                     }
                  }
               }
            }
         }
      }

      // Prepare the instanceBedId by conceptBedId map and put it against the shared instance bed id.
      Map<Long, Map<Long, Long>> instanceByConceptBedIdBySharedModelBedId = getRIUserSharedModelMappingInformation(
               sharedModelBedIds, new HashSet<Long>());

      // Process each app cloud entities for type component recognitions
      for (RecognizedCloudEntity appCloudEntity : appCloudEntities) {
         Map<Long, List<IWeightedEntity>> realizedTypeEntityMap = new HashMap<Long, List<IWeightedEntity>>(1);
         Map<String, List<IWeightedEntity>> entityListByTFposId = new HashMap<String, List<IWeightedEntity>>(1);

         for (Entry<Long, List<RICloud>> entry : riCloudsByTypeBedId.entrySet()) {
            Long typeBedId = entry.getKey();
            List<IWeightedEntity> recEntitiesList = recognitionEntitiesByTypeBedId.get(typeBedId);
            for (IWeightedEntity weightedEntity : recEntitiesList) {

               // Skip the concept entity cloning as they are already part of the app cloud
               if (((RecognitionEntity) weightedEntity).getEntityType() == RecognitionEntityType.CONCEPT_ENTITY) {
                  continue;
               }
               ConceptEntity conceptEntity = (ConceptEntity) weightedEntity;
               if (conceptEntity.getConceptBedId() != null) {
                  continue;
               }
               // If the entity is already unified for current App there is no need to create further realizations.
               if (processorInput.getUnifiedEntitiesByModelGroup() != null) {
                  List<RecognitionEntity> entities = processorInput.getUnifiedEntitiesByModelGroup().get(
                           appCloudEntity.getModelGroupId());
                  if (!CollectionUtils.isEmpty(entities) && entities.contains(conceptEntity)) {
                     continue;
                  }
               }

               List<Set<Long>> processedRIClouds = new ArrayList<Set<Long>>(1);
               List<RICloud> riClouds = entry.getValue();
               for (RICloud riCloud : riClouds) {
                  if (!riCloud.getCloudId().equals(appCloudEntity.getCloud().getId())) {
                     continue;
                  }
                  for (Set<Long> convertableBeIds : convertableBedsList) {
                     if (!processedRIClouds.contains(convertableBeIds)) {
                        if (convertableBeIds.contains(riCloud.getRealizationBusinessEntityId())) {
                           RIOntoTerm realizationTerm = null;
                           if (riCloud.getRealizationBusinessEntityId() != null) {
                              realizationTerm = ontoTermsByRealizationId.get(riCloud.getRealizationBusinessEntityId());
                              if (realizationTerm == null) {
                                 continue;
                              }
                           }
                           realizedTypeEntityMap.remove(riCloud.getComponentTypeBusinessEntityId());
                           if (conceptEntity.getModelGroupId() != null
                                    && !riCloud.getModelGroupId().equals(conceptEntity.getModelGroupId())
                                    && !(conceptEntity.getModelGroupId().equals(ExecueConstants.BASE_MODEL_GROUP_ID) || conceptEntity
                                             .getModelGroupId().equals(ExecueConstants.LOCATION_MODEL_GROUP_ID))) {
                              continue;
                           }

                           try {
                              processedRIClouds.add(convertableBeIds);
                              IWeightedEntity clonedWeightedEntity = (IWeightedEntity) weightedEntity.clone();
                              RecognitionEntity recognitionEntity = (RecognitionEntity) clonedWeightedEntity;

                              // Validate the shared model entity
                              if (getNlpServiceHelper().getLocationConfigurationService().isLocationType(typeBedId)) {
                                 updateSharedModelEntityInformation(recognitionEntity,
                                          instanceByConceptBedIdBySharedModelBedId, convertableBeIds);
                              }

                              realizationTerm = getRealizationTermbasedOnTypeQualifier(ontoTermsByRealizationId,
                                       convertableBeIds, realizationTerm, recognitionEntity);
                              ((OntoEntity) clonedWeightedEntity).setModelGroupId(appCloudEntity.getModelGroupId());
                              ((ConceptEntity) clonedWeightedEntity).setConceptBedId(realizationTerm.getConceptBEDID());
                              ((ConceptEntity) clonedWeightedEntity).setConceptDisplayName(realizationTerm
                                       .getConceptName());
                              ((ConceptEntity) clonedWeightedEntity).setPopularity(realizationTerm.getPopularity());
                              // NK: Increment the iteration and mark the level as app cloud i.e. 3
                              ((OntoEntity) clonedWeightedEntity).setIteration(currentIteration + 1);
                              ((OntoEntity) clonedWeightedEntity).setLevel(3);
                              // make it a concept Instance Entity.
                              ((OntoEntity) clonedWeightedEntity).setNLPtag(NLPConstants.NLP_TAG_ONTO_INSTANCE);
                              String posTFIdentifier = ((TypeEntity) clonedWeightedEntity).getTypeBedId() + "-"
                                       + ((OntoEntity) clonedWeightedEntity).getReferedTokenPositions();
                              ((OntoEntity) clonedWeightedEntity).setPosTFIdentifier(posTFIdentifier);
                              List<IWeightedEntity> recEntities = entityListByTFposId.get(posTFIdentifier);
                              if (recEntities == null) {
                                 recEntities = new ArrayList<IWeightedEntity>(1);
                                 entityListByTFposId.put(posTFIdentifier, recEntities);
                              }
                              recEntities.add(clonedWeightedEntity);
                              for (Long alternateBedId : convertableBeIds) {
                                 if (!alternateBedId.equals(realizationTerm.getConceptBEDID())) {
                                    ((OntoEntity) clonedWeightedEntity).getAlternateBedIds().add(alternateBedId);
                                 }
                              }
                              appCloudEntity.addRecognitionEntity(clonedWeightedEntity);
                           } catch (CloneNotSupportedException e) {
                              throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
                           }
                        }
                     }

                  }
               }

               // Base RT type entries should be added as is. Eg: conjunction like and
               if (CollectionUtils.isEmpty(processedRIClouds)) {
                  List<IWeightedEntity> recEnList = new ArrayList<IWeightedEntity>(1);
                  recEnList.add(weightedEntity);
                  RICloud riCloud = entry.getValue().get(0);
                  if (riCloud.getRepresentativeEntityType() == BusinessEntityType.REALIZED_TYPE) {
                     List<IWeightedEntity> recEntitiesForRICloud = realizedTypeEntityMap.get(riCloud
                              .getComponentTypeBusinessEntityId());
                     if (recEntitiesForRICloud == null) {
                        recEntitiesForRICloud = new ArrayList<IWeightedEntity>(1);
                        realizedTypeEntityMap.put(riCloud.getComponentTypeBusinessEntityId(), recEntitiesForRICloud);
                     }
                     recEntitiesForRICloud.add(weightedEntity);
                  }
               }
            }
         }

         for (Entry<Long, List<IWeightedEntity>> entry : realizedTypeEntityMap.entrySet()) {
            for (IWeightedEntity weightedEntity2 : entry.getValue()) {
               OntoEntity ontoEntity = (OntoEntity) weightedEntity2;
               ontoEntity.setModelGroupId(ExecueConstants.BASE_MODEL_GROUP_ID);
               // NK: Increment the iteration and mark the level as app cloud i.e. 3
               ontoEntity.setIteration(currentIteration + 1);
               ontoEntity.setLevel(3);
               appCloudEntity.addRecognitionEntity(ontoEntity);
            }
         }
         ((AppCloudEntity) appCloudEntity).setEntityListByTFposId(entityListByTFposId);
      }
   }

   private Collection<? extends Long> getLocationValueBedIds (LocationNormalizedData locationNormalizedData) {
      Set<Long> sharedModelBedIds = new HashSet<Long>();
      addValueBedIds(locationNormalizedData.getCities(), sharedModelBedIds);
      addValueBedIds(locationNormalizedData.getStates(), sharedModelBedIds);
      addValueBedIds(locationNormalizedData.getCountries(), sharedModelBedIds);
      addValueBedIds(locationNormalizedData.getCounties(), sharedModelBedIds);
      return sharedModelBedIds;
   }

   private void addValueBedIds (List<NormalizedDataEntity> cities, Set<Long> sharedModelBedIds) {
      for (NormalizedDataEntity normalizedDataEntity : cities) {
         sharedModelBedIds.add(normalizedDataEntity.getValueBedId());
      }
   }

   /**
    * @param sharedModelInstanceBedIds
    * @param modelGroupIds
    * @return the Map<Long, Map<Long, Long>> 
    */
   private Map<Long, Map<Long, Long>> getRIUserSharedModelMappingInformation (Set<Long> sharedModelInstanceBedIds,
            Set<Long> modelGroupIds) {
      Map<Long, Map<Long, Long>> instanceByConceptBySharedInstanceBedId = new HashMap<Long, Map<Long, Long>>();

      if (CollectionUtils.isEmpty(sharedModelInstanceBedIds)) {
         return instanceByConceptBySharedInstanceBedId;
      }

      //Get the ri shared user mapping information for the input base instance information bed id 
      List<RISharedUserModelMapping> riSharedUserModelMappings = new ArrayList<RISharedUserModelMapping>();
      try {
         List<Long> sharedModelInstanceIds = new ArrayList<Long>(sharedModelInstanceBedIds);
         if (CollectionUtils.isEmpty(modelGroupIds)) {
            riSharedUserModelMappings = getKdxRetrievalService().getRISharedUserModelMappings(sharedModelInstanceIds);
         } else {
            riSharedUserModelMappings = getKdxRetrievalService().getRISharedUserModelMappings(sharedModelInstanceIds,
                     modelGroupIds);
         }
      } catch (KDXException e) {
         throw new NLPSystemException(e.getCode(), e.getMessage(), e);
      }
      if (CollectionUtils.isEmpty(riSharedUserModelMappings)) {
         return instanceByConceptBySharedInstanceBedId;
      }

      // Prepare the instanceBedId by conceptBedId map and put it against the shared instance bed id.
      for (RISharedUserModelMapping riSharedUserModelMapping : riSharedUserModelMappings) {
         Map<Long, Long> instanceByConceptBedId = instanceByConceptBySharedInstanceBedId.get(riSharedUserModelMapping
                  .getBaseInstanceBedId());
         if (instanceByConceptBedId == null) {
            instanceByConceptBedId = new HashMap<Long, Long>();
            instanceByConceptBySharedInstanceBedId.put(riSharedUserModelMapping.getBaseInstanceBedId(),
                     instanceByConceptBedId);
         }
         instanceByConceptBedId.put(riSharedUserModelMapping.getConceptBedId(), riSharedUserModelMapping
                  .getInstanceBedId());
      }
      return instanceByConceptBySharedInstanceBedId;
   }

   private void updateSharedModelEntityInformation (RecognitionEntity recognitionEntity,
            Map<Long, Map<Long, Long>> instanceByConceptBedIdBySharedModelBedId, Set<Long> conceptBedIds) {
      try {
         if (recognitionEntity.getNormalizedData() == null) {
            return;
         }
         INormalizedData normalizedData = recognitionEntity.getNormalizedData();
         if (normalizedData.getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
            ListNormalizedData clonedListNormalizedData = (ListNormalizedData) recognitionEntity.getNormalizedData()
                     .clone();
            recognitionEntity.setNormalizedData(clonedListNormalizedData);
            List<NormalizedDataEntity> normalizedDataEntities = clonedListNormalizedData.getNormalizedDataEntities();
            for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
               LocationNormalizedData locationNormalizedData = (LocationNormalizedData) normalizedDataEntity
                        .getNormalizedData();
               updateLocationNormalizedData(instanceByConceptBedIdBySharedModelBedId, conceptBedIds,
                        locationNormalizedData);
            }
         } else {
            LocationNormalizedData locationNormalizedData = (LocationNormalizedData) recognitionEntity
                     .getNormalizedData().clone();
            recognitionEntity.setNormalizedData(locationNormalizedData);
            updateLocationNormalizedData(instanceByConceptBedIdBySharedModelBedId, conceptBedIds,
                     locationNormalizedData);
         }
      } catch (CloneNotSupportedException e) {
         e.printStackTrace();
      }

   }

   /**
    * @param instanceByConceptBedIdBySharedModelBedId
    * @param conceptBedIds
    * @param locationNormalizedData
    */
   private void updateLocationNormalizedData (Map<Long, Map<Long, Long>> instanceByConceptBedIdBySharedModelBedId,
            Set<Long> conceptBedIds, LocationNormalizedData locationNormalizedData) {
      List<NormalizedDataEntity> cities = locationNormalizedData.getCities();
      List<NormalizedDataEntity> states = locationNormalizedData.getStates();
      List<NormalizedDataEntity> counties = locationNormalizedData.getCounties();
      List<NormalizedDataEntity> countries = locationNormalizedData.getCountries();

      updateNormalizedDataEntities(cities, instanceByConceptBedIdBySharedModelBedId, conceptBedIds);
      updateNormalizedDataEntities(states, instanceByConceptBedIdBySharedModelBedId, conceptBedIds);
      updateNormalizedDataEntities(counties, instanceByConceptBedIdBySharedModelBedId, conceptBedIds);
      updateNormalizedDataEntities(countries, instanceByConceptBedIdBySharedModelBedId, conceptBedIds);
      LocationType locationType = getLocationType(locationNormalizedData);
      locationNormalizedData.setLocationType(locationType);
   }

   private LocationType getLocationType (LocationNormalizedData locationNormalizedData) {
      List<NormalizedDataEntity> cities = locationNormalizedData.getCities();
      if (!CollectionUtils.isEmpty(cities)) {
         return LocationType.CITY;
      }
      List<NormalizedDataEntity> states = locationNormalizedData.getStates();
      if (!CollectionUtils.isEmpty(states)) {
         return LocationType.STATE;
      }
      List<NormalizedDataEntity> counties = locationNormalizedData.getCounties();
      if (!CollectionUtils.isEmpty(counties)) {
         return LocationType.COUNTY;
      }
      List<NormalizedDataEntity> countries = locationNormalizedData.getCountries();
      if (!CollectionUtils.isEmpty(countries)) {
         return LocationType.COUNTRY;
      }
      return LocationType.CITY;
   }

   private void updateNormalizedDataEntities (List<NormalizedDataEntity> normalizedDataEntities,
            Map<Long, Map<Long, Long>> instanceByConceptBedIdBySharedModelBedId, Set<Long> conceptBedIds) {
      if (CollectionUtils.isEmpty(normalizedDataEntities)) {
         return;
      }
      List<NormalizedDataEntity> entitiesToRemove = new ArrayList<NormalizedDataEntity>();
      boolean entityFound = false;
      for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
         LocationNormalizedDataEntity locationNormalizedDataEntity = (LocationNormalizedDataEntity) normalizedDataEntity;
         Map<Long, Long> instanceByConceptBedId = instanceByConceptBedIdBySharedModelBedId
                  .get(locationNormalizedDataEntity.getValueBedId());
         if (MapUtils.isEmpty(instanceByConceptBedId)) {
            entitiesToRemove.add(normalizedDataEntity);
            continue;
         }
         entityFound = true;
         Set<Long> validConceptBedIds = instanceByConceptBedId.keySet();
         validConceptBedIds.retainAll(conceptBedIds);
         Map<Long, Long> validInstanceByConceptBedId = new HashMap<Long, Long>();
         for (Long validConceptBedId : validConceptBedIds) {
            validInstanceByConceptBedId.put(validConceptBedId, instanceByConceptBedId.get(validConceptBedId));
         }
         locationNormalizedDataEntity.setInstanceByConceptBedId(validInstanceByConceptBedId);
      }
      if (entityFound) {
         normalizedDataEntities.removeAll(entitiesToRemove);
      }
   }

   private void updateConvertableBedsList (List<Set<Long>> convertableBedsList, ProcessorInput processorInput) {
      List<Set<Long>> finalBeds = processorInput.getConvertibleBeds();
      for (Set<Long> beds : convertableBedsList) {
         if (beds.size() > 1) {
            finalBeds.add(beds);
         }
      }
   }

   /**
    * @param ontoTermsByRealizationId
    * @param convertableBeIds
    * @param realizationTerm
    * @param recognitionEntity
    * @return
    */
   private RIOntoTerm getRealizationTermbasedOnTypeQualifier (Map<Long, RIOntoTerm> ontoTermsByRealizationId,
            Set<Long> convertableBeIds, RIOntoTerm realizationTerm, RecognitionEntity recognitionEntity) {

      DateQualifier dateQualifier = null;
      // Check for Normalized data to be NULL
      if (recognitionEntity.getNormalizedData() == null) {
         return realizationTerm;
      } else if (recognitionEntity.getNormalizedData() instanceof TimeFrameNormalizedData) {
         TimeFrameNormalizedData tfNormalizedData = (TimeFrameNormalizedData) recognitionEntity.getNormalizedData();
         dateQualifier = tfNormalizedData.getDateQualifier();
         if (dateQualifier == null
                  && tfNormalizedData.getNormalizedDataType() == NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA) {
            dateQualifier = ((RelativeTimeNormalizedData) tfNormalizedData).getRelativeDateQualifier();
         }
      } else if (recognitionEntity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RANGE_NORMALIZED_DATA) {
         RangeNormalizedData rangeNormalizedData = (RangeNormalizedData) recognitionEntity.getNormalizedData();
         NormalizedDataEntity start = rangeNormalizedData.getStart();
         if (start.getNormalizedData() != null
                  && start.getNormalizedData().getNormalizedDataType() == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA) {
            TimeFrameNormalizedData tfNormalizedData = (TimeFrameNormalizedData) start.getNormalizedData();
            dateQualifier = tfNormalizedData.getDateQualifier();
         }
      } else if (recognitionEntity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
         ListNormalizedData listNormalizedData = (ListNormalizedData) recognitionEntity.getNormalizedData();
         NormalizedDataEntity start = listNormalizedData.getNormalizedDataEntities().get(0);
         if (start.getNormalizedData() != null
                  && start.getNormalizedData().getNormalizedDataType() == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA) {
            TimeFrameNormalizedData tfNormalizedData = (TimeFrameNormalizedData) start.getNormalizedData();
            dateQualifier = tfNormalizedData.getDateQualifier();
         }
      }
      if (dateQualifier == null) {
         return realizationTerm;
      }
      Map<DateQualifier, RIOntoTerm> dateOntoTerm = new HashMap<DateQualifier, RIOntoTerm>(1);
      for (Long bedId : convertableBeIds) {
         RIOntoTerm ontoTerm = ontoTermsByRealizationId.get(bedId);
         if (ontoTerm == null) {
            continue;
         }
         if (DateQualifier.YEAR.getValue().equalsIgnoreCase(ontoTerm.getDetailTypeName())) {
            dateOntoTerm.put(DateQualifier.YEAR, ontoTerm);
         } else if (DateQualifier.QUARTER.getValue().equalsIgnoreCase(ontoTerm.getDetailTypeName())) {
            dateOntoTerm.put(DateQualifier.QUARTER, ontoTerm);
         } else if (DateQualifier.MONTH.getValue().equalsIgnoreCase(ontoTerm.getDetailTypeName())) {
            dateOntoTerm.put(DateQualifier.MONTH, ontoTerm);
         } else if (DateQualifier.DAY.getValue().equalsIgnoreCase(ontoTerm.getDetailTypeName())) {
            dateOntoTerm.put(DateQualifier.DAY, ontoTerm);
         } else if (DateQualifier.WEEK.getValue().equalsIgnoreCase(ontoTerm.getDetailTypeName())) {
            dateOntoTerm.put(DateQualifier.WEEK, ontoTerm);
         } else if (DateQualifier.HOUR.getValue().equalsIgnoreCase(ontoTerm.getDetailTypeName())) {
            dateOntoTerm.put(DateQualifier.HOUR, ontoTerm);
         } else if (DateQualifier.MINUTE.getValue().equalsIgnoreCase(ontoTerm.getDetailTypeName())) {
            dateOntoTerm.put(DateQualifier.MINUTE, ontoTerm);
         } else if (DateQualifier.SECOND.getValue().equalsIgnoreCase(ontoTerm.getDetailTypeName())) {
            dateOntoTerm.put(DateQualifier.SECOND, ontoTerm);
         }
      }
      List<DateQualifier> dateQualifierConversionHierarchyList = NLPUtilities.dateQualifierHierarchyConversionMap
               .get(dateQualifier);
      for (DateQualifier possibleDateQualifier : dateQualifierConversionHierarchyList) {
         if (dateOntoTerm.containsKey(possibleDateQualifier)) {
            realizationTerm = dateOntoTerm.get(possibleDateQualifier);
            break;
         }
      }

      return realizationTerm;
   }

   /**
    * We need to add App level filtering before actual app processing. Once all the app clouds are created, we need to
    * get SUM of AVERAGE FULL WEIGHT for all the recognitions for each model.
    * 
    * @param appCloudEntities
    * @return the List<RecognizedCloudEntity>
    */
   private List<RecognizedCloudEntity> filterAppCloudEntitiesBasedOnRecognitionWeight (
            List<RecognizedCloudEntity> appCloudEntities) {
      Map<Double, List<RecognizedCloudEntity>> appCloudsByWeight = new HashMap<Double, List<RecognizedCloudEntity>>(1);
      List<Double> weightList = new ArrayList<Double>(1);
      for (RecognizedCloudEntity recognizedCloudEntity : appCloudEntities) {
         Set<Integer> positionsCovered = new HashSet<Integer>(1);
         List<Integer> totalRecsPos = new ArrayList<Integer>(1);
         double weight = 0.0;
         Set<ReferedTokenPosition> totalRTPs = new HashSet<ReferedTokenPosition>(1);
         Map<Integer, List<IWeightedEntity>> recEntitesListByPosMapNew = NLPUtilities
                  .getRecognitionEntitiesByPositionMap(recognizedCloudEntity.getRecognitionEntities());
         for (Entry<Integer, List<IWeightedEntity>> entry : recEntitesListByPosMapNew.entrySet()) {
            NLPUtilities.sortRecEntitiesByQuality(entry.getValue());
            IWeightedEntity weightedEntity = entry.getValue().get(0);
            RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
            ReferedTokenPosition rtp = new ReferedTokenPosition(recEntity.getReferedTokenPositions());
            if (!totalRTPs.contains(rtp)) {
               weight = weight + weightedEntity.getWeightInformation().getFinalWeight();
            }
            totalRTPs.add(rtp);
         }
         for (ReferedTokenPosition rtp : totalRTPs) {
            totalRecsPos.addAll(rtp.getReferedTokenPositions());
         }
         positionsCovered.addAll(totalRecsPos);
         weight = weight * positionsCovered.size() / totalRecsPos.size();
         weightList.add(weight);
         List<RecognizedCloudEntity> appClouds = appCloudsByWeight.get(weight);
         if (appClouds == null) {
            appClouds = new ArrayList<RecognizedCloudEntity>(1);
            appCloudsByWeight.put(weight, appClouds);
         }
         appClouds.add(recognizedCloudEntity);
      }
      // Use Midway filtering with CV Value as 0.15 this needs to go through a proper QA cycle.
      // List<Double> topCluster = MathUtil.getLiberalTopCluster(weightList);
      List<Double> topCluster = MathUtil.getMidwayTopCluster(weightList);
      List<RecognizedCloudEntity> entitiesAfterFilter = new ArrayList<RecognizedCloudEntity>(1);
      for (Entry<Double, List<RecognizedCloudEntity>> entry : appCloudsByWeight.entrySet()) {
         if (topCluster.contains(entry.getKey())) {
            entitiesAfterFilter.addAll(entry.getValue());
         }
      }
      return entitiesAfterFilter;
   }

   private boolean checkForNonAttributeEntity (AppCloudEntity appCloudEntity) {
      for (IWeightedEntity weightedEntity : appCloudEntity.getRecognitionEntities()) {
         OntoEntity recEntity = (OntoEntity) weightedEntity;
         if (!recEntity.isAttributeType() && recEntity.getModelGroupId() != 1) {
            return false;
         }
      }
      return true;
   }

   // 2. For Each App-Cloud
   // 2.1. Call Path-Rule-Executor
   // 2.1.1. For Each Path, check if any Weight-Assignment-Rule is associated, then
   // 2.1.1.1. Create Assignment-Rule-Input
   // 2.1.1.2. Call Rule-Processing-Service?get-Association-Weight method
   // 2.1.1.3. Set the received weight for the Path
   // 2.1.2. If Multiple Paths exist between same pair of Entities, keep only the best Path
   private void applyWeightAssignmentRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput,
            WeightAssignmentRulesContent weightAssignmentRulesContent) {
      if (cloudEntity.isWeightAssignmentRuleAssociated()) {
         getPathRulesExecutor().processWeightAssignmentRules(cloudEntity, weightAssignmentRulesContent, processorInput);
      }
   }

   private void performList (ProcessorInput input, RecognizedCloudEntity cloudEntity) {

      Map<Integer, List<IWeightedEntity>> origRecEntitesByPosMap = NLPUtilities
               .getRecognitionEntitiesByPositionMap(cloudEntity.getRecognitionEntities());
      Map<Integer, List<IWeightedEntity>> unrecognizedEntityByPosMap = NLPUtilities
               .getRecognitionEntitiesByPositionMap(input.getUnrecognizedEntities());
      Map<Integer, List<IWeightedEntity>> unrecognizedBaseEntityByPosMap = NLPUtilities
               .getRecognitionEntitiesByPositionMap(input.getUnrecognizedBaseRecEntities());
      Map<Long, List<IWeightedEntity>> instanceEntitiesByConceptBeId = nlpServiceHelper
               .groupInstanceEntitiesByConceptBeId(cloudEntity.getRecognitionEntities());
      // IN case of AppCloud don't remove the single entities even if the entities are merged
      getNlpServiceHelper().performListForInstances(cloudEntity.getRecognitionEntities(),
               instanceEntitiesByConceptBeId, origRecEntitesByPosMap, unrecognizedBaseEntityByPosMap,
               unrecognizedEntityByPosMap, false);
   }

   /**
    * Method to get the central concept for the cloudEntity.
    * 
    * @param processorInput
    * @param cloudEntity
    *           Cloud Entity
    */
   private void getCentralConcepts (ProcessorInput processorInput, RecognizedCloudEntity cloudEntity) {
      boolean isCentralConceptFound = false;
      Set<RecognitionEntity> nonCentralConceptEntities = new HashSet<RecognitionEntity>();
      Set<Integer> centralConceptPositions = new HashSet<Integer>(1);
      Set<Integer> nonCentralConceptPositions = new HashSet<Integer>(1);
      boolean centralConceptExistInUserQuery = false;
      for (IWeightedEntity weightedEntity : cloudEntity.getRecognitionEntities()) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         if (recEntity.getEntityType() == RecognitionEntityType.PROPERTY_ENTITY) {
            continue;
         }
         if (NLPUtilities.checkPopulationConcept(recEntity)) {
            recEntity.addFlag(OntologyConstants.POPULATION_CONCEPT);
            centralConceptExistInUserQuery = true;
            centralConceptPositions.addAll(recEntity.getReferedTokenPositions());
         } else {
            nonCentralConceptEntities.add(recEntity);
            nonCentralConceptPositions.addAll(recEntity.getReferedTokenPositions());

         }
      }
      centralConceptPositions.removeAll(nonCentralConceptPositions);
      if (!CollectionUtils.isEmpty(centralConceptPositions)) {
         isCentralConceptFound = true;
      }
      // If central concept is not found, then get the proposed/default central concepts
      if (!isCentralConceptFound) {
         Set<BusinessEntityDefinition> proposedCentralConcepts = new HashSet<BusinessEntityDefinition>(1);
         try {
            for (RecognitionEntity recognitionEntity : nonCentralConceptEntities) {
               ConceptEntity conceptEntity = (ConceptEntity) recognitionEntity;
               List<BusinessEntityDefinition> centralConcepts = getOntologyService().reachAndGetCentralConceptBEDs(
                        conceptEntity.getConceptBedId());
               if (!CollectionUtils.isEmpty(centralConcepts)) {
                  proposedCentralConcepts.addAll(centralConcepts);
               }
               if (!CollectionUtils.isEmpty(proposedCentralConcepts)) {
                  break;
               }
            }
         } catch (OntologyException e) {
            throw new NLPSystemException(e.code, e.getMessage());
         }

         try {
            if (!CollectionUtils.isEmpty(proposedCentralConcepts)) {
               int pos = processorInput.getNextImplicitRecognitionCounter();
               for (BusinessEntityDefinition bed : proposedCentralConcepts) {
                  // Check if central concept is not an abstract concept
                  boolean isAbstract = getKdxModelService().checkEntityHasBehavior(bed.getId(), BehaviorType.ABSTRACT);
                  if (!isAbstract) {
                     addCentralConceptRecognition(cloudEntity, pos, bed, centralConceptExistInUserQuery);
                  }
               }
            }
         } catch (KDXException kdxException) {
            throw new NLPSystemException(kdxException.getCode(), kdxException.getMessage(), kdxException.getCause());
         }
      }
   }

   private void addCentralConceptRecognition (RecognizedCloudEntity cloudEntity, int pos, BusinessEntityDefinition bed,
            boolean centralConceptExistInUserQuery) {
      RecognitionEntity entity = EntityFactory.getRecognitionEntity(RecognitionEntityType.CONCEPT_ENTITY, bed
               .getConcept().getName(), NLPConstants.NLP_TAG_ONTO_CONCEPT, null, pos);
      entity.addReferedTokenPosition(pos);
      entity.addOriginalReferedTokenPosition(pos);
      entity.setLevel(3);
      cloudEntity.addRecognitionEntity(entity);
      WeightInformation weightInformation = new WeightInformation();
      weightInformation.setRecognitionQuality(1);
      // assign half of MAX Recognition Weight as Central Concept is a implicit Concept.
      weightInformation.setRecognitionWeight(5);
      // set the importance as 0.5 for the implicit central concept if another once is present in User query.
      if (centralConceptExistInUserQuery) {
         weightInformation.setImportance(0.5);
      }
      entity.setWeightInformation(weightInformation);
      entity.setWord(bed.getConcept().getName());
      // TODO - NA - Need to get the onto entity bed from the db
      ((TypeEntity) entity).setTypeBedId(bed.getType().getId());
      ((TypeEntity) entity).setTypeDisplayName(bed.getType().getDisplayName());
      ((ConceptEntity) entity).setConceptDisplayName(bed.getConcept().getDisplayName());
      ((ConceptEntity) entity).setPopularity(bed.getPopularity());
      ((ConceptEntity) entity).setConceptBedId(bed.getId());
      ((ConceptEntity) entity).setModelGroupId(cloudEntity.getModelGroupId());
      entity.setStartPosition(pos);
      entity.setEndPosition(pos);
      entity.setPosition(pos);
      entity.addFlag(OntologyConstants.POPULATION_CONCEPT);
      if (logger.isDebugEnabled()) {
         logger.debug("Central Concept [" + bed + "] DED id is : " + ((OntoEntity) entity).getId());
      }
   }

   /**
    * This method filters the recognition entities as follows: 
    * 1) Entity of type as Type 
    * 2) Entity of type Value and is from the base model group  
    * 3) If we have multiple entities in same position keep the 100% match entity and discard the rest
    * 4) Filter by top cluster on each position
    * 
    * @param cloudEntity
    */
   private void performFilterLogic (RecognizedCloudEntity cloudEntity) {
      List<IWeightedEntity> appCloudRecognitionEntities = cloudEntity.getRecognitionEntities();
      List<IWeightedEntity> finalRecognitionEntities = new ArrayList<IWeightedEntity>();
      finalRecognitionEntities.addAll(appCloudRecognitionEntities);

      // Filter the recognition entities with entity type as type
      getNlpServiceHelper().filterTypeEntities(finalRecognitionEntities);

      // Filter the base value recognition entities if for the same position we have the app level value realization
      // entities
      getNlpServiceHelper().filterBaseValueRecognitionEntities(finalRecognitionEntities);

      // Filter the subset base recognition entities if for the same position we have the app level super set realization
      // entities. eg: operator, adjective, etc
      getNlpServiceHelper().filterSubsetBaseRecognitionEntities(finalRecognitionEntities);

      // Filter rest of the entities for a position if that position already has some 100% matching entity.
      getNlpServiceHelper().filterEntitiesIfFullMatchFound(finalRecognitionEntities);

      // Filter the Onto Entities of the same positions by Top Cluster.
      getNlpServiceHelper().filterEntitiesByTopCluster(finalRecognitionEntities);

      // Filter the Onto Entities of the subset positions if they are of same type realizations
      getNlpServiceHelper().filterSubsetTypeRealizationEntities(finalRecognitionEntities);

      cloudEntity.setRecognitionEntities(finalRecognitionEntities);
   }

   public IKDXModelService getKdxModelService () {
      return kdxModelService;
   }

   public void setKdxModelService (IKDXModelService kdxModelService) {
      this.kdxModelService = kdxModelService;
   }

   public INLPConfigurationService getNlpConfigurationService () {
      return nlpConfigurationService;
   }

   public void setNlpConfigurationService (INLPConfigurationService nlpConfigurationService) {
      this.nlpConfigurationService = nlpConfigurationService;
   }

   public PathRulesExecutor getPathRulesExecutor () {
      return pathRulesExecutor;
   }

   public void setPathRulesExecutor (PathRulesExecutor pathRulesExecutor) {
      this.pathRulesExecutor = pathRulesExecutor;
   }

   public NLPServiceHelper getNlpServiceHelper () {
      return nlpServiceHelper;
   }

   public void setNlpServiceHelper (NLPServiceHelper nlpServiceHelper) {
      this.nlpServiceHelper = nlpServiceHelper;
   }

   /**
    * @return the pathDefinitionRetrievalService
    */
   public IPathDefinitionRetrievalService getPathDefinitionRetrievalService () {
      return pathDefinitionRetrievalService;
   }

   /**
    * @param pathDefinitionRetrievalService
    *           the pathDefinitionRetrievalService to set
    */
   public void setPathDefinitionRetrievalService (IPathDefinitionRetrievalService pathDefinitionRetrievalService) {
      this.pathDefinitionRetrievalService = pathDefinitionRetrievalService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IKDXCloudRetrievalService getKdxCloudRetrievalService () {
      return kdxCloudRetrievalService;
   }

   public void setKdxCloudRetrievalService (IKDXCloudRetrievalService kdxCloudRetrievalService) {
      this.kdxCloudRetrievalService = kdxCloudRetrievalService;
   }

   /**
    * @return the ontologyService
    */
   public IOntologyService getOntologyService () {
      return ontologyService;
   }

   /**
    * @param ontologyService the ontologyService to set
    */
   public void setOntologyService (IOntologyService ontologyService) {
      this.ontologyService = ontologyService;
   }
}