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
package com.execue.nlp.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.core.common.bean.entity.Rule;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.bean.nlp.InstanceInformation;
import com.execue.core.common.bean.nlp.ListNormalizedData;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.NormalizedDataEntity;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.bean.nlp.RecognizedType;
import com.execue.core.common.bean.nlp.TimeFrameNormalizedData;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.CloudCategory;
import com.execue.core.common.type.CloudComponentSelectionType;
import com.execue.core.common.type.ComponentCategory;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.SearchFilterType;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.CombinationGenerator;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.entity.Association;
import com.execue.nlp.bean.entity.ConceptEntity;
import com.execue.nlp.bean.entity.ConceptProfileEntity;
import com.execue.nlp.bean.entity.InstanceEntity;
import com.execue.nlp.bean.entity.InstanceProfileEntity;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.ProfileEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.bean.entity.ReferedTokenPosition;
import com.execue.nlp.bean.entity.TypeEntity;
import com.execue.nlp.bean.matrix.CloudParticipationMonitor;
import com.execue.nlp.bean.matrix.Possibility;
import com.execue.nlp.bean.matrix.RootMatrix;
import com.execue.nlp.configuration.INLPConfigurationService;
import com.execue.nlp.exception.NLPExceptionCodes;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.nlp.rule.IValidationRule;
import com.execue.nlp.service.CloudServiceFactory;
import com.execue.nlp.service.ICloudService;
import com.execue.nlp.util.NLPUtilities;
import com.execue.ontology.configuration.OntologyConstants;
import com.execue.swi.configuration.ILocationConfigurationService;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IKDXRetrievalService;

/**
 * @author Nitesh
 */
public class NLPServiceHelper {

   private final Logger                  logger = Logger.getLogger(NLPServiceHelper.class);

   private INLPConfigurationService      nlpConfigurationService;
   private ILocationConfigurationService locationConfigurationService;
   private IBaseKDXRetrievalService      baseKDXRetrievalService;
   private IKDXRetrievalService          kdxRetrievalService;
   private IKDXModelService              kdxModelService;
   private IKDXCloudRetrievalService     kdxCloudRetrievalService;
   private CloudServiceFactory           cloudServiceFactory;
   private ISWIConfigurationService      swiConfigurationService;
   private IApplicationRetrievalService  applicationRetrievalService;

   /**
    * @param cloudCategory
    * @return the corresponding implementation of ICloudService based on the input cloudCategory
    */
   public ICloudService getCloudService (CloudCategory cloudCategory) {
      return cloudServiceFactory.getCloudService(cloudCategory);
   }

   /**
    * @return Long - base model group id
    * @throws KDXException
    */
   public Long getBaseGroupId () throws KDXException {
      return getBaseKDXRetrievalService().getBaseGroup().getId();
   }

   /**
    * This method returns the set of cloud component bed ids for the given recognition entities. 
    * Component Bed Ids could be:
    * 1) type bed id
    * 2) concept bed id
    * 3) relation bed id
    * 4) concept profile bed id
    * 5) location child bed id
    * 
    * @param recognitionEntities
    * @return the Set<Long>
    */
   public Set<Long> getComponentBedIdsFromRecEntities (List<IWeightedEntity> recognitionEntities) {
      Set<Long> compBedIds = new HashSet<Long>();
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return compBedIds;
      }

      boolean isLocationTypeFound = false;
      for (IWeightedEntity entity : recognitionEntities) {
         if (entity instanceof OntoEntity) {
            OntoEntity ontoEntity = (OntoEntity) entity;
            // Always add the type bed id
            Long typeBedId = ((TypeEntity) ontoEntity).getTypeBedId();
            if (typeBedId != null) {
               // check if we have the location type
               if (getLocationConfigurationService().isLocationType(typeBedId)) {
                  isLocationTypeFound = true;
               }
               compBedIds.add(typeBedId);
            }

            // For instances, only get the concept bed id
            if (ontoEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY
                     || ontoEntity.getEntityType() == RecognitionEntityType.INSTANCE_PROFILE_ENTITY) {
               Long conceptBedId = ((ConceptEntity) ontoEntity).getConceptBedId();
               if (conceptBedId != null) {
                  compBedIds.add(conceptBedId);
               }
            } else {
               // Could be relation, concept, concept profile or type itself
               compBedIds.add(ontoEntity.getId());
            }
            // Finally, also add the behaviors
            Set<Long> behaviordBEdIds = ontoEntity.getBehaviorBedIds();
            compBedIds.addAll(behaviordBEdIds);
         }
      }
      if (isLocationTypeFound) {
         compBedIds.addAll(getLocationConfigurationService().getChildBedIdsByParentBedId(
                  ExecueConstants.LOCATION_TYPE_BED_ID));
      }
      return compBedIds;
   }

   /**
    * This method returns the Recognized Cloud Entity for given list of RI Clouds
    * 
    * @param riClouds
    * @param recognitionEntities
    * @param processorInput
    * @return the List<RecognizedCloudEntity>
    * @throws KDXException
    */
   public List<RecognizedCloudEntity> getRecognizedCloudEntities (List<RICloud> riClouds,
            List<IWeightedEntity> recognitionEntities, ProcessorInput processorInput) throws KDXException {
      List<RecognizedCloudEntity> recCloudEntities = new ArrayList<RecognizedCloudEntity>();
      Map<Long, List<RICloud>> cloudIdMap = groupCloudComponents(riClouds);
      Map<Long, List<IWeightedEntity>> recognitionEntitiesByTypeAndBehaviorBed = NLPUtilities
               .getRecognitionEntitiesByComponentBedMap(recognitionEntities);
      Set<Entry<Long, List<RICloud>>> entrySet = cloudIdMap.entrySet();

      for (Entry<Long, List<RICloud>> entry : entrySet) {
         RecognizedCloudEntity recognizedCloudEntity = getRecognizedCloudEntity(entry.getValue(),
                  recognitionEntitiesByTypeAndBehaviorBed, processorInput);
         if (recognizedCloudEntity != null) {
            recCloudEntities.add(recognizedCloudEntity);
         }
      }
      return recCloudEntities;
   }

   /**
    * This method populates the recEntitiesByIdMap for the given list of recognitionEntities. Here the key is concept
    * bed id or type bed id. In case of concept profiles the key is concept profile id
    * 
    * @param recEntitiesByIdMap
    * @param recognitionEntities
    */
   public void populateRecEntitiesById (Map<Long, List<RecognitionEntity>> recEntitiesByIdMap,
            List<IWeightedEntity> recognitionEntities) {
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return;
      }
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         // key list for the entity is created so that concept can be added as concept bed and type bed Id also.
         List<Long> keyListForEntity = new ArrayList<Long>(1);
         Long key = null;
         if (recEntity.getEntityType() == RecognitionEntityType.PROPERTY_ENTITY) {
            continue;
         }
         if (recEntity instanceof ConceptEntity) {
            if (recEntity instanceof ProfileEntity) {
               if (recEntity.getEntityType() == RecognitionEntityType.INSTANCE_PROFILE_ENTITY) {
                  InstanceProfileEntity entity = (InstanceProfileEntity) recEntity;
                  key = entity.getConceptBedId();
               } else {
                  ConceptProfileEntity entity = (ConceptProfileEntity) recEntity;
                  key = entity.getProfileID();
               }
            } else {
               ConceptEntity entity = (ConceptEntity) recEntity;
               key = entity.getConceptBedId();
               if (entity.getNormalizedData() != null
                        && entity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LOCATION_NORMALIZED_DATA) {
                  keyListForEntity.addAll(getLocationConfigurationService().getChildBedIdsByParentBedId(
                           entity.getTypeBedId()));
               }
            }
         }
         if (key == null) {
            TypeEntity entity = (TypeEntity) recEntity;
            key = entity.getTypeBedId();
         }
         keyListForEntity.add(key);
         if (recEntity.getEntityType() == RecognitionEntityType.CONCEPT_ENTITY) {
            keyListForEntity.add(((ConceptEntity) recEntity).getTypeBedId());
         }
         for (Long currentKey : keyListForEntity) {
            List<RecognitionEntity> recEntityList = recEntitiesByIdMap.get(currentKey);
            if (ExecueCoreUtil.isCollectionEmpty(recEntityList)) {
               recEntityList = new ArrayList<RecognitionEntity>();
               recEntitiesByIdMap.put(currentKey, recEntityList);
            }
            if (!recEntityList.contains(recEntity)) {
               recEntityList.add(recEntity);
            }
         }
      }
   }

   /**
    * @param weightedEntity
    * @param typeBed
    * @return the NormalizedDataEntity
    */
   public NormalizedDataEntity getNormalizeDataEntity (IWeightedEntity weightedEntity, BusinessEntityDefinition typeBed) {
      NormalizedDataEntity normalizedDataEntity = new NormalizedDataEntity();
      if (typeBed != null) {
         normalizedDataEntity.setTypeBedId(typeBed.getId());
      }
      if (weightedEntity instanceof InstanceEntity) {
         // TODO: NK: Need to check how to handle multiple instance information if they comes here
         InstanceInformation instanceInformation = ((InstanceEntity) weightedEntity).getInstanceInformations().get(0);
         normalizedDataEntity.setValue(instanceInformation.getInstanceValue());
         normalizedDataEntity.setDisplayValue(instanceInformation.getInstanceDisplayName());
         normalizedDataEntity.setDisplaySymbol(instanceInformation.getDisplaySymbol());
         if (instanceInformation.getInstanceBedId() != null) {
            normalizedDataEntity.setValueBedId(instanceInformation.getInstanceBedId());
            normalizedDataEntity.setValueKnowledgeId(instanceInformation.getKnowledgeId());
            normalizedDataEntity.setWeightInformation(instanceInformation.getWeightInformation());
         } else {
            normalizedDataEntity.setValue(((OntoEntity) weightedEntity).getWord());
            normalizedDataEntity.setDisplayValue(((OntoEntity) weightedEntity).getWord());
            normalizedDataEntity.setWeightInformation(((OntoEntity) weightedEntity).getWeightInformation());
         }
      } else {
         // TODO: NK: should we ever come here??
         normalizedDataEntity.setValue(((OntoEntity) weightedEntity).getWord());
         normalizedDataEntity.setDisplayValue(((OntoEntity) weightedEntity).getWord());
         normalizedDataEntity.setWeightInformation(((OntoEntity) weightedEntity).getWeightInformation());
      }
      return normalizedDataEntity;
   }

   /**
    * @param inputRecognitions
    * @return the maxIteration Integer
    */
   public Integer getMaxIteration (List<IWeightedEntity> inputRecognitions) {
      int iteration = 1;
      for (IWeightedEntity weightedEntity : inputRecognitions) {
         RecognitionEntity recognitionEntity = (RecognitionEntity) weightedEntity;
         if (recognitionEntity.getIteration() > iteration) {
            iteration = recognitionEntity.getIteration();
         }
      }
      return iteration;
   }

   /**
    * This method returns the RecognizedCloudEntity
    * 
    * @param riClouds
    * @param recognitionEntitiesByComponentBedId
    * @param processorInput
    * @return the RecognizedCloudEntity
    */
   private RecognizedCloudEntity getRecognizedCloudEntity (List<RICloud> riClouds,
            Map<Long, List<IWeightedEntity>> recognitionEntitiesByComponentBedId, ProcessorInput processorInput)
            throws KDXException {
      if (CollectionUtils.isEmpty(riClouds)) {
         return null;
      }
      // Set cloud entity weight and recognition entities based on the recognitions found
      double cloudEntityWeight = 0;
      double cloudEntityQuality = 0;
      int count = 0;
      List<IWeightedEntity> cloudRecognitionEntities = new ArrayList<IWeightedEntity>();

      Map<Long, BusinessEntityDefinition> typeBedById = new HashMap<Long, BusinessEntityDefinition>();
      Map<Long, Integer> frequencyByComponentId = new HashMap<Long, Integer>(1);
      Map<Long, CloudComponent> cloudComponentInfoByPartId = new HashMap<Long, CloudComponent>();
      Long cloudId = riClouds.get(0).getCloudId();
      Cloud cloud = getNlpConfigurationService().getClonedCloudById(cloudId);
      if (cloud == null) {
         cloud = kdxCloudRetrievalService.getCloudById(cloudId);
      }
      int existingRequiredCompCount = 0;
      long missingCloudPartId = 1;
      for (RICloud riCloud : riClouds) {
         List<IWeightedEntity> validComponentRecognitionEntities = new ArrayList<IWeightedEntity>(1);
         List<IWeightedEntity> componentRecognitionEntities = recognitionEntitiesByComponentBedId.get(riCloud
                  .getComponentBusinessEntityId());
         if (!CollectionUtils.isEmpty(componentRecognitionEntities)) {
            addValidEntitiesToCloud(riCloud, validComponentRecognitionEntities, componentRecognitionEntities,
                     processorInput.getCloudParticipationMonitor());
            if (riCloud.getComponentCategory() == ComponentCategory.BEHAVIOR_WITH_TYPE
                     || riCloud.getComponentCategory() == ComponentCategory.ONLY_BEHAVIOR) {
               // TODO -N/A- behavior entities should not repeat if they are already added by a type.
               validComponentRecognitionEntities.removeAll(cloudRecognitionEntities);
            }
         }
         if (CollectionUtils.isEmpty(validComponentRecognitionEntities)) {
            continue;
         }

         // Populate the frequency by comp bed id map
         frequencyByComponentId.put(riCloud.getComponentBusinessEntityId(), riCloud.getFrequency());

         if (riCloud.getComponentTypeBusinessEntityId() != null) {
            // TODO -NA- As of now instead of querying the DB, creating the new BusinessEntityDefinition Object and
            // setting the id.
            // We will need to change the data structure here.
            Type type = new Type();
            type.setName(riCloud.getComponentTypeName());
            BusinessEntityDefinition compTypeBusinessEntity = new BusinessEntityDefinition();
            compTypeBusinessEntity.setId(riCloud.getComponentTypeBusinessEntityId());
            compTypeBusinessEntity.setType(type);
            typeBedById.put(riCloud.getComponentTypeBusinessEntityId(), compTypeBusinessEntity);
         }

         // Create the cloud component
         CloudComponent cloudComponentInfo = new CloudComponent();
         cloudComponentInfo.setCloud(cloud);
         cloudComponentInfo.setRecognitionEntities(validComponentRecognitionEntities);

         for (IWeightedEntity entity : validComponentRecognitionEntities) {
            RecognitionEntity recognitionEntity = (RecognitionEntity) entity;
            cloudEntityQuality += recognitionEntity.getWeightInformation().getRecognitionQuality();
            cloudEntityWeight += recognitionEntity.getWeightInformation().getRecognitionWeight();
            cloudRecognitionEntities.add(recognitionEntity);
            count++;
         }
         if (riCloud.getRealizationBusinessEntityId() != null) {
            BusinessEntityDefinition realizationBusinessEntity = new BusinessEntityDefinition();
            realizationBusinessEntity.setId(riCloud.getRealizationBusinessEntityId());
            cloudComponentInfo.setComponentBed(realizationBusinessEntity);
         } else if (cloud.getCategory() == CloudCategory.FRAMEWORK_CLOUD) {
            BusinessEntityDefinition realizationBusinessEntity = new BusinessEntityDefinition();
            realizationBusinessEntity.setId(riCloud.getComponentBusinessEntityId());
            cloudComponentInfo.setComponentBed(realizationBusinessEntity);
         }
         if (riCloud.getComponentTypeBusinessEntityId() != null) {
            BusinessEntityDefinition compTypeBusinessEntity = new BusinessEntityDefinition();
            compTypeBusinessEntity.setId(riCloud.getComponentTypeBusinessEntityId());
            cloudComponentInfo.setComponentTypeBed(compTypeBusinessEntity);
         }
         cloudComponentInfo.setComponentCategory(riCloud.getComponentCategory());
         cloudComponentInfo.setDefaultValue(riCloud.getDefaultValue());
         cloudComponentInfo.setRepresentativeEntityType(riCloud.getRepresentativeEntityType());
         cloudComponentInfo.setCloudPart(riCloud.getCloudPart());
         cloudComponentInfo.setFrequency(riCloud.getFrequency());
         cloudComponentInfo.setRequired(riCloud.getRequired());
         cloudComponentInfo.setOutputComponent(riCloud.getOutputComponent());
         cloudComponentInfo.setRequiredBehavior(riCloud.getRequiredBehaviorBusinessEntityId());
         cloudComponentInfo.setImportance(riCloud.getImportance());
         // TODO -NA- always the cloud Part should be the key of this map. Have to give a Patch to set the proper cloud
         // Part in all the clouds and then remove the if check.
         if (riCloud.getCloudPart() != null) {
            CloudComponent cloudComponent = cloudComponentInfoByPartId.get(riCloud.getCloudPart().longValue());
            if (cloudComponent != null) {
               cloudComponent.getRecognitionEntities().addAll(cloudComponentInfo.getRecognitionEntities());
            } else {
               cloudComponentInfoByPartId.put(riCloud.getCloudPart().longValue(), cloudComponentInfo);
            }
         } else {
            cloudComponentInfoByPartId.put(missingCloudPartId, cloudComponentInfo);
         }
         if (riCloud.getRequired() == CheckType.YES) {
            existingRequiredCompCount++;
         }
         missingCloudPartId++;
      }
      int cloudRequiredCompCount = cloud.getRequiredComponentCount();
      // If the existing required component is less than the actual required component, then the cloud shall be
      // invalidated.
      if (MapUtils.isEmpty(cloudComponentInfoByPartId) || existingRequiredCompCount < cloudRequiredCompCount) {
         return null;
      }

      cloud.setCloudComponentsFrequency(frequencyByComponentId);
      cloud.setCloudComponentInfoMap(cloudComponentInfoByPartId);

      // Create the weight information for the cloud entity
      WeightInformation weightInformation = new WeightInformation();
      weightInformation.setRecognitionWeight(cloudEntityWeight);
      weightInformation.setRecognitionQuality(cloudEntityQuality / count);

      // Create and populate the recognized cloud entity
      RecognizedCloudEntity recognizedCloudEntity = new RecognizedCloudEntity();
      recognizedCloudEntity.setCloud(cloud);
      recognizedCloudEntity.setModelGroupId(riClouds.get(0).getModelGroupId());
      recognizedCloudEntity.setWeightInformation(weightInformation);
      recognizedCloudEntity.setRecognitionEntities(cloudRecognitionEntities);
      recognizedCloudEntity.setTypeBedMap(typeBedById);
      if (!CollectionUtils.isEmpty(cloud.getCloudAllowedBehavior())) {
         for (BusinessEntityDefinition behavior : cloud.getCloudAllowedBehavior()) {
            recognizedCloudEntity.addCloudAllowedBehavior(behavior.getId());
         }
      }
      recognizedCloudEntity.setValidationRules(getValidationRulesForCloud(cloud.getCloudValidationRules()));
      return recognizedCloudEntity;
   }

   /**
    * @param riCloud
    * @param recognitionEntities
    * @param entities
    * @param cloudParticipationMonitor
    */
   private void addValidEntitiesToCloud (RICloud riCloud, List<IWeightedEntity> recognitionEntities,
            List<IWeightedEntity> entities, CloudParticipationMonitor cloudParticipationMonitor) {
      for (IWeightedEntity weightedEntity : entities) {

         boolean validComponentType = false;
         // For only behavior is sufficient type of component don't need to validate by component type
         if (riCloud.getComponentCategory() == ComponentCategory.ONLY_BEHAVIOR) {
            validComponentType = true;
         } else {
            validComponentType = riCloud.getComponentTypeBusinessEntityId().equals(
                     ((TypeEntity) weightedEntity).getTypeBedId());
         }
         if (!validComponentType) {
            continue;
         }

         boolean validRepresentativeEntityType = NLPUtilities.checkIfEntityIsOfEntityType(riCloud
                  .getRepresentativeEntityType(), weightedEntity);
         if (!validRepresentativeEntityType) {
            continue;
         }
         Long requiredBehaviorId = riCloud.getRequiredBehaviorBusinessEntityId();
         if (requiredBehaviorId != null) {
            BehaviorType behaviorType = BehaviorType.getType(requiredBehaviorId.intValue());
            boolean validRequiredBehavior = NLPUtilities.isValidBasedOnRequiredBehavior(behaviorType, weightedEntity);
            if (!validRequiredBehavior) {
               continue;
            }
         }
         recognitionEntities.add(weightedEntity);
      }
   }

   private List<IValidationRule> getValidationRulesForCloud (Set<Rule> rules) {
      Map<Long, IValidationRule> validationRulesMap = getNlpConfigurationService().getValidationRulesContent()
               .getValidationRules();
      List<IValidationRule> validationRules = new ArrayList<IValidationRule>();
      for (Rule rule : rules) {
         validationRules.add(validationRulesMap.get(rule.getId()));
      }
      return validationRules;
   }

   /**
    * Method to group the RI cloud component BE IDs against the cloud id Eg: For TF101(Month, Year) and TF102(Quarter,
    * Year) Returns <TF101,<<Month>,<Year>>> <TF102,<<Quarter>,<Year>>>
    * 
    * @param riClouds
    *           List<RICloud>
    * @return Map<Long, List<RICloud>> for each cloud id
    */
   public Map<Long, List<RICloud>> groupCloudComponents (List<RICloud> riClouds) {
      Map<Long, List<RICloud>> cloudIdMap = new HashMap<Long, List<RICloud>>();
      if (ExecueCoreUtil.isCollectionNotEmpty(riClouds)) {
         for (RICloud riCloud : riClouds) {
            if (cloudIdMap.containsKey(riCloud.getCloudId())) {
               List<RICloud> riCloudsList = cloudIdMap.get(riCloud.getCloudId());
               riCloudsList.add(riCloud);
            } else {
               List<RICloud> riCloudsList = new ArrayList<RICloud>();
               riCloudsList.add(riCloud);
               cloudIdMap.put(riCloud.getCloudId(), riCloudsList);
            }
         }
      }
      return cloudIdMap;
   }

   /**
    * Method to group the RI cloud component BE IDs against the cloud id Eg: For TF101(Month, Year) and TF102(Quarter,
    * Year) Returns <TF101,<<Month>,<Year>>> <TF102,<<Quarter>,<Year>>>
    * 
    * @param riClouds
    *           List<RICloud>
    * @param isFromArticle
    * @return Map<Long, List<RICloud>> for each cloud id
    */
   public Map<Long, List<RICloud>> groupAppCloudComponents (List<RICloud> riClouds, boolean isFromArticle) {
      Map<Long, List<RICloud>> cloudIdMap = new HashMap<Long, List<RICloud>>();
      if (ExecueCoreUtil.isCollectionNotEmpty(riClouds)) {
         for (RICloud riCloud : riClouds) {
            if (cloudIdMap.containsKey(riCloud.getCloudId())) {
               List<RICloud> riCloudsList = cloudIdMap.get(riCloud.getCloudId());
               riCloudsList.add(riCloud);
            } else if (isFromArticle
                     || riCloud.getCloudSelection() == CloudComponentSelectionType.ENOUGH_FOR_CLOUD_SELECTION) {
               List<RICloud> riCloudsList = new ArrayList<RICloud>();
               riCloudsList.add(riCloud);
               cloudIdMap.put(riCloud.getCloudId(), riCloudsList);
            }
         }
      }
      return cloudIdMap;
   }

   /**
    * @param recCloudEntities
    * @return the List<RecognizedCloudEntity>
    */
   public List<RecognizedCloudEntity> splitBasedOnFrequency (List<RecognizedCloudEntity> recCloudEntities) {
      if (CollectionUtils.isEmpty(recCloudEntities)) {
         return recCloudEntities;
      }
      Set<RecognizedCloudEntity> newRecognizedCloudEntities = new HashSet<RecognizedCloudEntity>(1);
      for (RecognizedCloudEntity recognizedCloudEntity : recCloudEntities) {

         // Skip for app clouds
         if (recognizedCloudEntity.getCategory() == CloudCategory.APP_CLOUD) {
            continue;
         }

         Map<Long, Integer> frequencyByComponentId = new HashMap<Long, Integer>(1);
         for (Entry<Long, CloudComponent> entry : recognizedCloudEntity.getCloudComponentInfoMap().entrySet()) {
            frequencyByComponentId.put(entry.getKey(), entry.getValue().getRecognitionEntities().size());
         }
         List<Long> moreFrequencyList = new ArrayList<Long>(1);
         List<IWeightedEntity> matchedFrequencyList = new ArrayList<IWeightedEntity>(1);
         for (Entry<Long, Integer> frequencyByComponentIdEntry : frequencyByComponentId.entrySet()) {
            Long cloudComponentId = frequencyByComponentIdEntry.getKey();
            Integer componentCurrentFrequency = frequencyByComponentIdEntry.getValue();
            Integer cloudComponentFrequency = recognizedCloudEntity.getCloudComponentInfoMap().get(cloudComponentId)
                     .getFrequency();
            if (cloudComponentFrequency == null) {
               continue;
            }
            if (cloudComponentFrequency < componentCurrentFrequency) {
               moreFrequencyList.add(cloudComponentId);
            } else {
               matchedFrequencyList.addAll(recognizedCloudEntity.getCloudComponentInfoMap().get(cloudComponentId)
                        .getRecognitionEntities());
            }
         }

         if (!CollectionUtils.isEmpty(moreFrequencyList)) {
            RecognizedCloudEntity clonedRCE = null;
            try {
               clonedRCE = (RecognizedCloudEntity) recognizedCloudEntity.clone();
            } catch (CloneNotSupportedException e) {
               throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
            }
            clonedRCE.setRecognitionEntities(matchedFrequencyList);
            List<RecognizedCloudEntity> recCloudEntityList = new ArrayList<RecognizedCloudEntity>(1);
            recCloudEntityList.add(clonedRCE);
            for (Long cloudCompId : moreFrequencyList) {
               recCloudEntityList = splitAndAddRecCloudEntity(recCloudEntityList, recognizedCloudEntity
                        .getCloudComponentInfoMap().get(cloudCompId).getRecognitionEntities(), cloudCompId);
            }
            // if after split continuous clouds span over the complete [position set on which the original cloud was we
            // can filter the clouds which are not continuous.
            filterBasedOnContinuity(recCloudEntityList, recognizedCloudEntity);
            newRecognizedCloudEntities.addAll(recCloudEntityList);
         } else {
            newRecognizedCloudEntities.add(recognizedCloudEntity);
         }
      }

      List<RecognizedCloudEntity> allRecognizedCloudEntitiesList = new ArrayList<RecognizedCloudEntity>(1);
      allRecognizedCloudEntitiesList.addAll(newRecognizedCloudEntities);

      updateCloudCompononentInfo(allRecognizedCloudEntitiesList);

      return allRecognizedCloudEntitiesList;
   }

   /**
    * This method splits the each RecognizedCloudEntity based on its recognitions. 
    * Will split the cloud and make sure each splitted cloud has the continuous recognitions in it.
    * Eg: A cloud has 4 recognitions at positions [1] [34] [45] [78], then we split this cloud 
    * into 3 clouds which has continuous recognitions as {[1]}, {[34][45]} and {[78]}    
    *  
    * @param recCloudEntities
    * @return the List<RecognizedCloudEntity>
    */
   public List<RecognizedCloudEntity> splitBasedOnContinuity (List<RecognizedCloudEntity> recCloudEntities) {
      if (CollectionUtils.isEmpty(recCloudEntities)) {
         return recCloudEntities;
      }
      Set<RecognizedCloudEntity> continuousPositionClouds = new HashSet<RecognizedCloudEntity>();
      for (RecognizedCloudEntity cloudEntity : recCloudEntities) {
         // Skip for app clouds
         if (cloudEntity.getCategory() == CloudCategory.APP_CLOUD) {
            continue;
         }

         // Get the cloud positions and check if is continuous
         TreeSet<Integer> cloudPositions = NLPUtilities.getReferredTokenPositions(cloudEntity.getRecognitionEntities());
         cloudPositions.addAll(NLPUtilities.getReferredTokenPositions(cloudEntity.getAllowedRecognitionEntities()));
         ReferedTokenPosition rtp = new ReferedTokenPosition();
         rtp.setReferedTokenPositions(cloudPositions);
         List<Integer> inBetweenPos = rtp.getInBetweenPos();

         // If it is continuous, then further split is not possible, hence add it to the list and continue
         if (CollectionUtils.isEmpty(inBetweenPos)) {
            continuousPositionClouds.add(cloudEntity);
            continue;
         }

         // If it is not continuous, then break them into the continuous clouds 
         List<IWeightedEntity> recognitionEntities = cloudEntity.getRecognitionEntities();
         NLPUtilities.sortRecognitionEntitiesByRTP(recognitionEntities);
         ReferedTokenPosition continuousRTP = new ReferedTokenPosition();
         List<IWeightedEntity> continuousRecognitionEntities = new ArrayList<IWeightedEntity>();
         for (IWeightedEntity weightedEntity : recognitionEntities) {
            RecognitionEntity recognitionEntity = (RecognitionEntity) weightedEntity;
            continuousRTP.addAll(recognitionEntity.getReferedTokenPositions());
            if (!CollectionUtils.isEmpty(continuousRTP.getInBetweenPos())
                     && !CollectionUtils.isEmpty(continuousRecognitionEntities)) {
               RecognizedCloudEntity clonedCloudEntity = null;
               try {
                  clonedCloudEntity = (RecognizedCloudEntity) cloudEntity.clone();
               } catch (CloneNotSupportedException e) {
                  throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
               }
               clonedCloudEntity.setRecognitionEntities(continuousRecognitionEntities);
               continuousPositionClouds.add(clonedCloudEntity);
               continuousRecognitionEntities = new ArrayList<IWeightedEntity>();
               continuousRTP.clear();
               continuousRTP.addAll(recognitionEntity.getReferedTokenPositions());
            }
            continuousRecognitionEntities.add(weightedEntity);
         }

         if (!CollectionUtils.isEmpty(continuousRecognitionEntities)) {
            RecognizedCloudEntity clonedCloudEntity = null;
            try {
               clonedCloudEntity = (RecognizedCloudEntity) cloudEntity.clone();
            } catch (CloneNotSupportedException e) {
               throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
            }
            clonedCloudEntity.setRecognitionEntities(continuousRecognitionEntities);
            continuousPositionClouds.add(clonedCloudEntity);
         }
      }

      List<RecognizedCloudEntity> continuousPositionCloudsList = new ArrayList<RecognizedCloudEntity>(
               continuousPositionClouds);
      updateCloudCompononentInfo(continuousPositionCloudsList);
      return continuousPositionCloudsList;
   }

   /**
    * If after split continuous clouds span over the complete [position set on which the original cloud was we can
    * filter the clouds which are not continuous.
    * 
    * @param recCloudEntityList
    * @param recognizedCloudEntity
    */
   private void filterBasedOnContinuity (List<RecognizedCloudEntity> recCloudEntityList,
            RecognizedCloudEntity recognizedCloudEntity) {
      TreeSet<Integer> referredTokenPositions = NLPUtilities.getReferredTokenPositions(recognizedCloudEntity
               .getRecognitionEntities());
      TreeSet<Integer> continuousCloudPositions = new TreeSet<Integer>();
      List<RecognizedCloudEntity> continuousClouds = new ArrayList<RecognizedCloudEntity>(1);
      List<RecognizedCloudEntity> nonContinuousClouds = new ArrayList<RecognizedCloudEntity>(1);
      for (RecognizedCloudEntity cloudEntity : recCloudEntityList) {
         TreeSet<Integer> cloudPositions = NLPUtilities.getReferredTokenPositions(cloudEntity.getRecognitionEntities());
         ReferedTokenPosition rtp = new ReferedTokenPosition();
         rtp.setReferedTokenPositions(cloudPositions);
         List<Integer> inBetweenPos = rtp.getInBetweenPos();
         if (CollectionUtils.isEmpty(inBetweenPos)) {
            continuousClouds.add(cloudEntity);
            continuousCloudPositions.addAll(cloudPositions);
         } else {
            nonContinuousClouds.add(cloudEntity);
         }
      }
      if (!CollectionUtils.isEmpty(continuousClouds) && !CollectionUtils.isEmpty(nonContinuousClouds)
               && continuousCloudPositions.containsAll(referredTokenPositions)) {
         recCloudEntityList.removeAll(nonContinuousClouds);
      }
   }

   private void updateCloudCompononentInfo (Collection<RecognizedCloudEntity> recognizedCloudEntityList) {
      if (!CollectionUtils.isEmpty(recognizedCloudEntityList)) {
         for (RecognizedCloudEntity recognizedCloudEntity : recognizedCloudEntityList) {
            List<IWeightedEntity> cloudRecognitionEntities = recognizedCloudEntity.getRecognitionEntities();
            Map<Long, CloudComponent> cloudComponentInfoMap = recognizedCloudEntity.getCloudComponentInfoMap();
            for (CloudComponent cloudComponentInfo : cloudComponentInfoMap.values()) {
               cloudComponentInfo.getRecognitionEntities().retainAll(cloudRecognitionEntities);
            }
         }
      }
   }

   /**
    * Method to return the list of recognition entities(instance of type entity only) by type bed id from the given
    * input list of recognition entities
    * 
    * @param recognitionEntities
    * @return the Map<Long, List<IWeightedEntity>>
    */
   public Map<Long, List<IWeightedEntity>> getRecognitionEntitiesByTypeBedMap (List<IWeightedEntity> recognitionEntities) {
      Map<Long, List<IWeightedEntity>> recognitionEntitiesByTypeBedId = new HashMap<Long, List<IWeightedEntity>>();
      for (IWeightedEntity recognitionEntity : recognitionEntities) {
         if (recognitionEntity instanceof TypeEntity) {
            Long typeBedId = ((TypeEntity) recognitionEntity).getTypeBedId();
            // If child of location, then consider the type as location itself
            if (getLocationConfigurationService().isChildOfLocationType(typeBedId)) {
               typeBedId = getLocationConfigurationService().getParentBedIdForChildBedId(typeBedId);
            }

            List<IWeightedEntity> recEntities = recognitionEntitiesByTypeBedId.get(typeBedId);
            if (recEntities == null) {
               recEntities = new ArrayList<IWeightedEntity>();
               recognitionEntitiesByTypeBedId.put(typeBedId, recEntities);
            }
            recEntities.add(recognitionEntity);
         }
      }
      return recognitionEntitiesByTypeBedId;
   }

   private List<RecognizedCloudEntity> splitAndAddRecCloudEntity (List<RecognizedCloudEntity> recCloudEntityList,
            List<IWeightedEntity> recEntities, Long typeBedId) {

      List<RecognizedCloudEntity> clonedRecCloudEntityList = new ArrayList<RecognizedCloudEntity>(1);
      for (RecognizedCloudEntity recognizedCloudEntity : recCloudEntityList) {
         Integer cloudComponentFrequency = recognizedCloudEntity.getCloudComponentInfoMap().get(typeBedId)
                  .getFrequency();

         CombinationGenerator cg = new CombinationGenerator(recEntities.size(), cloudComponentFrequency);
         int[] a = null;
         while ((a = cg.getNext()) != null) {
            RecognizedCloudEntity clonedRCE = null;
            try {
               clonedRCE = (RecognizedCloudEntity) recognizedCloudEntity.clone();
            } catch (CloneNotSupportedException e) {
               throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
            }
            boolean invalidClone = false;
            for (int i : a) {
               RecognitionEntity recEntity = (RecognitionEntity) recEntities.get(i);
               if (!clonedRCE.getRecognitionEntities().contains(recEntity)) {
                  // check if all the referred token position of the current entity already added in cloud.
                  TreeSet<Integer> cloudRTP = NLPUtilities
                           .getReferredTokenPositions(clonedRCE.getRecognitionEntities());
                  if (cloudRTP.containsAll(recEntity.getReferedTokenPositions())) {
                     invalidClone = true;
                     break;
                  }
                  clonedRCE.addRecognitionEntity(recEntity);

               } else {
                  invalidClone = true;
                  break;
               }
            }
            if (!invalidClone) {
               clonedRecCloudEntityList.add(clonedRCE);
            }
         }
      }
      return clonedRecCloudEntityList;
   }

   /**
    * This method removes the invalid clouds if the in between entities contains the unallowed components for the cloud
    * 
    * @param recCloudEntities
    * @param originalEntities
    * @return the List<RecognizedCloudEntity>
    */
   public List<RecognizedCloudEntity> filterInvalidClouds (List<RecognizedCloudEntity> recCloudEntities,
            List<IWeightedEntity> originalEntities) {
      List<RecognizedCloudEntity> recCloudEntityList = new ArrayList<RecognizedCloudEntity>(1);
      for (RecognizedCloudEntity recognizedCloudEntity : recCloudEntities) {

         // Skip for app clouds
         if (recognizedCloudEntity.getCategory() == CloudCategory.APP_CLOUD) {
            recCloudEntityList.add(recognizedCloudEntity);
            continue;
         }
         Set<Integer> invalidPositions = new TreeSet<Integer>();
         boolean inBetweenAllowed = checkIfInBetweenRecEntitiesAllowed(recognizedCloudEntity, originalEntities,
                  invalidPositions);

         ReferedTokenPosition invalidPosRTP = new ReferedTokenPosition();
         invalidPosRTP.addAll(invalidPositions);
         if (!inBetweenAllowed) {
            inBetweenAllowed = filterInvalidComponents(recognizedCloudEntity, invalidPosRTP);
         }
         if (inBetweenAllowed) {
            filterSubsetComponents(recognizedCloudEntity);
            addInBetweenAllowedRecognitions(recognizedCloudEntity, originalEntities);
            recCloudEntityList.add(recognizedCloudEntity);
         }

      }
      return recCloudEntityList;
   }

   private void addInBetweenAllowedRecognitions (RecognizedCloudEntity recognizedCloudEntity,
            List<IWeightedEntity> originalEntities) {

      List<IWeightedEntity> existingRecEntities = new ArrayList<IWeightedEntity>(1);
      existingRecEntities.addAll(recognizedCloudEntity.getRecognitionEntities());
      if (CollectionUtils.isEmpty(existingRecEntities)) {
         return;
      }
      Map<Integer, List<IWeightedEntity>> origRecEntitiesByPosMap = NLPUtilities
               .getRecognitionEntitiesByPositionMap(originalEntities);

      TreeSet<Integer> existingRecsPositions = NLPUtilities.getAllReferredTokenPositions(existingRecEntities);
      ReferedTokenPosition rtp = new ReferedTokenPosition(existingRecsPositions);
      List<Integer> inBetweenPos = rtp.getInBetweenPos();
      TreeSet<Integer> existingPositions = new TreeSet<Integer>();
      for (Integer pos : inBetweenPos) {
         List<IWeightedEntity> weightedEntitiesList = origRecEntitiesByPosMap.get(pos);
         TreeSet<Integer> posAllRTPs = NLPUtilities.getAllReferredTokenPositions(weightedEntitiesList);
         if (weightedEntitiesList != null && !existingPositions.containsAll(posAllRTPs)) {
            addIfAllowedEntityExistsAtPosition(recognizedCloudEntity, weightedEntitiesList, existingPositions);
         }
      }
   }

   /**
    * Method to filter the recognition entities for each non required component recognition entities if they are subset
    * of the positions covered by the required components recognition entities
    * 
    * @param recognizedCloudEntity
    */
   private void filterSubsetComponents (RecognizedCloudEntity recognizedCloudEntity) {
      List<IWeightedEntity> requiredRecognitionEntities = new ArrayList<IWeightedEntity>(1);
      Map<Long, CloudComponent> notRequiredComps = new HashMap<Long, CloudComponent>(1);
      List<Long> invalidCompIds = new ArrayList<Long>(1);

      for (Entry<Long, CloudComponent> entry : recognizedCloudEntity.getCloudComponentInfoMap().entrySet()) {
         if (entry.getValue().isCloudComponentRequired()) {
            requiredRecognitionEntities.addAll(entry.getValue().getRecognitionEntities());
         } else {
            notRequiredComps.put(entry.getKey(), entry.getValue());
         }
      }
      // If required components are empty, then can't filter anything hence return
      if (CollectionUtils.isEmpty(requiredRecognitionEntities)) {
         return;
      }

      // Get all the positions of the required component recognition entities
      TreeSet<Integer> requiredRecsPositions = new TreeSet<Integer>();
      for (IWeightedEntity weightedEntity : requiredRecognitionEntities) {
         requiredRecsPositions.addAll(((RecognitionEntity) weightedEntity).getReferedTokenPositions());
      }

      // Check if the not required component recognition entity is subset of the required component recognition
      // entity(ies) positions
      // then add them to the remove list
      for (Entry<Long, CloudComponent> entry : notRequiredComps.entrySet()) {
         CloudComponent notRequiredComp = entry.getValue();
         List<IWeightedEntity> recEnList = notRequiredComp.getRecognitionEntities();
         List<IWeightedEntity> entitiesToRemove = new ArrayList<IWeightedEntity>(1);
         for (IWeightedEntity weightedEntity : recEnList) {
            ReferedTokenPosition entityRtp = new ReferedTokenPosition(((RecognitionEntity) weightedEntity)
                     .getReferedTokenPositions());
            if (requiredRecsPositions.containsAll(entityRtp.getReferedTokenPositions())) {
               entitiesToRemove.add(weightedEntity);
            }
         }

         // Check if not required component itself is invalid i.e. when all recognition entities of the component are
         // present in the entities to removed list else remove only those invalid entities
         if (!CollectionUtils.isEmpty(entitiesToRemove) && entitiesToRemove.containsAll(recEnList)) {
            invalidCompIds.add(entry.getKey());
         } else if (!CollectionUtils.isEmpty(entitiesToRemove)) {
            recognizedCloudEntity.getRecognitionEntities().removeAll(entitiesToRemove);
            notRequiredComp.getRecognitionEntities().removeAll(entitiesToRemove);
         }
      }

      // Clean up the invalid components info
      for (Long compId : invalidCompIds) {
         recognizedCloudEntity.getCloudComponentInfoMap().remove(compId);
         recognizedCloudEntity.getRecognitionEntities()
                  .removeAll(notRequiredComps.get(compId).getRecognitionEntities());
      }

   }

   public boolean checkIfInBetweenRecEntitiesAllowed (RecognizedCloudEntity recognizedCloudEntity,
            List<IWeightedEntity> originalEntities, Set<Integer> invalidPositions) {
      boolean inBetweenAllowed = true;

      List<IWeightedEntity> existingRecEntities = new ArrayList<IWeightedEntity>(1);
      existingRecEntities.addAll(recognizedCloudEntity.getRecognitionEntities());
      // TODO -NA- Not able to reproduce this issue in Local. This is a Test fix. this scenario shall never come.
      if (CollectionUtils.isEmpty(existingRecEntities)) {
         return false;
      }
      Map<Integer, List<IWeightedEntity>> origRecEntitesByPosMap = NLPUtilities
               .getRecognitionEntitiesByPositionMap(originalEntities);

      TreeSet<Integer> existingRecsPositions = NLPUtilities.getAllReferredTokenPositions(existingRecEntities);
      ReferedTokenPosition rtp = new ReferedTokenPosition(existingRecsPositions);
      List<Integer> inBetweenPos = rtp.getInBetweenPos();
      TreeSet<Integer> existingPositions = new TreeSet<Integer>();
      for (Integer pos : inBetweenPos) {
         List<IWeightedEntity> weightedEntitiesList = origRecEntitesByPosMap.get(pos);
         TreeSet<Integer> posAllRTPs = NLPUtilities.getAllReferredTokenPositions(weightedEntitiesList);
         if (weightedEntitiesList == null || weightedEntitiesList != null && !existingPositions.containsAll(posAllRTPs)
                  && !ifAllowedEntityExistAtPosition(weightedEntitiesList, recognizedCloudEntity, existingPositions)) {
            inBetweenAllowed = false;
            invalidPositions.add(pos);
         }
      }
      return inBetweenAllowed;
   }

   /**
    * @param recognizedCloudEntity
    * @param invalidPosRTP
    * @return the boolean true/false
    */
   private boolean filterInvalidComponents (RecognizedCloudEntity recognizedCloudEntity,
            ReferedTokenPosition invalidPosRTP) {
      List<IWeightedEntity> requiredRecEntities = new ArrayList<IWeightedEntity>(1);
      Map<Long, CloudComponent> notRequiredComps = new HashMap<Long, CloudComponent>(1);

      for (Entry<Long, CloudComponent> entry : recognizedCloudEntity.getCloudComponentInfoMap().entrySet()) {
         if (entry.getValue().isCloudComponentRequired()) {
            requiredRecEntities.addAll(entry.getValue().getRecognitionEntities());
         } else {
            notRequiredComps.put(entry.getKey(), entry.getValue());
         }
      }
      TreeSet<Integer> requiredRecsPositions = NLPUtilities.getAllReferredTokenPositions(requiredRecEntities);
      if (CollectionUtils.isEmpty(requiredRecsPositions)) {
         updateInvalidNotRequiredComps(recognizedCloudEntity, invalidPosRTP, notRequiredComps, requiredRecsPositions);
         return true;
      }

      ReferedTokenPosition rtp = new ReferedTokenPosition(requiredRecsPositions);
      List<Integer> inBetweenPos = rtp.getInBetweenPos();
      ReferedTokenPosition inBetweenRTP = new ReferedTokenPosition(inBetweenPos);
      if (inBetweenRTP.isOverLap(invalidPosRTP)) {
         return false;
      } else {
         updateInvalidNotRequiredComps(recognizedCloudEntity, invalidPosRTP, notRequiredComps, requiredRecsPositions);
         return true;
      }
   }

   /**
    * @param recognizedCloudEntity
    * @param invalidPosRTP
    * @param notRequiredComps
    * @param invalidCompIds
    * @param requiredRecsPositions
    */
   private void updateInvalidNotRequiredComps (RecognizedCloudEntity recognizedCloudEntity,
            ReferedTokenPosition invalidPosRTP, Map<Long, CloudComponent> notRequiredComps,
            TreeSet<Integer> requiredRecsPositions) {
      List<Long> invalidCompIds = new ArrayList<Long>(1);

      for (Entry<Long, CloudComponent> entry : notRequiredComps.entrySet()) {
         CloudComponent notRequiredComp = entry.getValue();
         List<IWeightedEntity> recEnList = notRequiredComp.getRecognitionEntities();
         TreeSet<Integer> pos = NLPUtilities.getAllReferredTokenPositions(recEnList);
         ReferedTokenPosition newRTP = new ReferedTokenPosition();
         newRTP.addAll(requiredRecsPositions);
         newRTP.addAll(pos);

         if (!CollectionUtils.isEmpty(newRTP.getInBetweenPos())) {
            ReferedTokenPosition inBetweenRTP = new ReferedTokenPosition(newRTP.getInBetweenPos());
            if (inBetweenRTP.isOverLap(invalidPosRTP)) {
               invalidCompIds.add(entry.getKey());
            }
         }
      }
      for (Long compId : invalidCompIds) {
         recognizedCloudEntity.getCloudComponentInfoMap().remove(compId);
         recognizedCloudEntity.getRecognitionEntities()
                  .removeAll(notRequiredComps.get(compId).getRecognitionEntities());
      }
   }

   /**
    * This method checks if the given recEntity is in allowed component list of the RecognizedCloudEntity
    * 
    * @param weightedEntitiesList
    * @param recognizedCloudEntity
    * @param existingPositions
    * @return the boolean true/false
    */
   private boolean ifAllowedEntityExistAtPosition (List<IWeightedEntity> weightedEntitiesList,
            RecognizedCloudEntity recognizedCloudEntity, TreeSet<Integer> existingPositions) {
      Set<Long> cloudAllowedComponents = recognizedCloudEntity.getCloudAllowedComponents();
      Set<Long> cloudAllowedBehaviors = recognizedCloudEntity.getCloudAllowedBehavior();
      boolean isValid = false;
      for (IWeightedEntity weightedEntity : weightedEntitiesList) {
         TypeEntity typeEntity = (TypeEntity) weightedEntity;
         Set<BehaviorType> behaviors = typeEntity.getBehaviors();
         List<Long> behaviorIds = getBehaviorIds(behaviors);
         Long typeBedId = typeEntity.getTypeBedId();
         if (cloudAllowedComponents.contains(typeBedId)) {
            existingPositions.addAll(typeEntity.getReferedTokenPositions());
            isValid = true;
         } else if (CollectionUtils.containsAny(behaviorIds, cloudAllowedBehaviors)) {
            existingPositions.addAll(typeEntity.getReferedTokenPositions());
            isValid = true;
         }
      }
      return isValid;
   }

   private void addIfAllowedEntityExistsAtPosition (RecognizedCloudEntity recognizedCloudEntity,
            List<IWeightedEntity> weightedEntitiesList, TreeSet<Integer> existingPositions) {
      Set<Long> cloudAllowedComponents = recognizedCloudEntity.getCloudAllowedComponents();
      Set<Long> cloudAllowedBehaviors = recognizedCloudEntity.getCloudAllowedBehavior();
      for (IWeightedEntity weightedEntity : weightedEntitiesList) {
         TypeEntity typeEntity = (TypeEntity) weightedEntity;
         Set<BehaviorType> behaviors = typeEntity.getBehaviors();
         List<Long> behaviorIds = getBehaviorIds(behaviors);
         Long typeBedId = typeEntity.getTypeBedId();
         if (cloudAllowedComponents.contains(typeBedId)) {
            recognizedCloudEntity.addAllowedRecognitionEntity(weightedEntity);
            existingPositions.addAll(typeEntity.getReferedTokenPositions());
         } else if (CollectionUtils.containsAny(behaviorIds, cloudAllowedBehaviors)) {
            recognizedCloudEntity.addAllowedRecognitionEntity(weightedEntity);
            existingPositions.addAll(typeEntity.getReferedTokenPositions());
         }
      }
   }

   private List<Long> getBehaviorIds (Set<BehaviorType> behaviors) {
      List<Long> behaviorIds = new ArrayList<Long>();
      for (BehaviorType behaviorType : behaviors) {
         behaviorIds.add(behaviorType.getValue().longValue());
      }
      return behaviorIds;
   }

   public void updateBehaviors (List<IWeightedEntity> recognitionEntities) throws KDXException {
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         Long entityBedId = null;
         if (recEntity.getEntityType() == RecognitionEntityType.PROPERTY_ENTITY) {
            continue;
         }
         List<BehaviorType> behaviors = new ArrayList<BehaviorType>(1);
         if (recEntity.getEntityType() == RecognitionEntityType.CONCEPT_ENTITY
                  || recEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY) {
            if (recEntity.getNLPtag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_INSTANCE)
                     || recEntity.getNLPtag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_CONCEPT)) {
               entityBedId = ((ConceptEntity) recEntity).getConceptBedId();
            }
         }
         if (entityBedId == null) {
            Long typeBedId = ((TypeEntity) recEntity).getTypeBedId();
            behaviors = getKdxRetrievalService().getAllPossibleBehavior(typeBedId);
         } else {
            behaviors = getKdxRetrievalService().getAllBehaviorForEntity(entityBedId);
         }
         for (BehaviorType behaviorType : behaviors) {
            ((OntoEntity) recEntity).addBehaviorBedId(behaviorType.getValue().longValue());
         }
         ((OntoEntity) recEntity).setBehaviors(new HashSet<BehaviorType>(behaviors));
      }
   }

   /**
    * @param possibility
    */
   public void calculateEntitiesWeight (Possibility possibility, WeightInformation baseWeightInformation) {
      double recognitionQuality = 0.0;
      double recognitionWeight = 0.0;
      int numRecognitions = 0;
      Set<Application> applications = possibility.getModel().getApplications();
      boolean associationExists = false;
      String appName = null;
      for (Application application : applications) {
         associationExists = application.getAssociationExist() == CheckType.YES;
         appName = application.getName();
         break;
      }
      ReferedTokenPosition possibilityRTP = new ReferedTokenPosition();
      if (associationExists) {
         for (Association association : possibility.getAssociations()) {
            for (IWeightedEntity weightedEntity : possibility.getRecognitionEntities()) {
               RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
               List<RecognitionEntity> pathComponents = association.getPathComponent();
               if (recEntity.getPosition() >= 0 && pathComponents.contains(recEntity)) {
                  double weightReduction = association.getWeightInformation().getPeanalty() / 3;
                  recEntity.setRecognitionQuality(recEntity.getRecognitionQuality() - weightReduction);
               }
            }
         }
      }
      for (IWeightedEntity weightedEntity : possibility.getRecognitionEntities()) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         if (recEntity.getPosition() >= 0) {
            ReferedTokenPosition rtp = new ReferedTokenPosition(recEntity.getReferedTokenPositions());
            possibilityRTP.addAll(rtp.getReferedTokenPositionsList());
            double recEntityQuality = recEntity.getRecognitionQuality() * rtp.getReferedTokenPositions().size();
            if (associationExists && possibility.getRecognitionEntities().size() > 1) {
               if (!nlpConfigurationService.getAllowedUnconnectedTypes().contains(
                        ((TypeEntity) recEntity).getTypeDisplayName().toLowerCase())) {
                  recEntityQuality = peanalizeWeightIfEntityIsNotConnected(recEntityQuality, recEntity, possibility
                           .getAssociations());
               }
            }
            recognitionQuality = recognitionQuality + recEntityQuality;
            numRecognitions = numRecognitions + rtp.getReferedTokenPositions().size();
         }
      }
      recognitionWeight = NLPConstants.MAX_WEIGHT * possibilityRTP.getReferedTokenPositions().size();
      recognitionQuality = recognitionQuality / numRecognitions;
      double finalWeight = recognitionQuality * recognitionWeight;
      boolean penalizeForImplicitConceptAssociation = penalizeForImplicitConceptAssociation(appName);

      double peanalty = 0.0;
      if (penalizeForImplicitConceptAssociation) {
         peanalty = peanalizeWeightIfConnectedThroughImplicitTokens(finalWeight, possibility);
      }
      WeightInformation weightInformation = new WeightInformation();
      weightInformation.setRecognitionQuality(recognitionQuality);
      weightInformation.setRecognitionWeight(recognitionWeight);
      weightInformation.setPeanalty(peanalty);
      finalWeight = weightInformation.getFinalWeight();
      double factor = finalWeight / baseWeightInformation.getFinalWeight();
      if (logger.isDebugEnabled()) {
         logger.debug("Weight Info : For posiibility " + possibility.getId() + " finalWeight " + finalWeight
                  + " factor is" + factor + "\n" + possibility.getRecognitionEntities());
      }
      possibility.setWeightInformationForExplicitEntities(weightInformation);
      // update weight information for associations Which are connecting explicit entities.
      updateWeightInformationForAssocs(possibility);
   }

   private void updateWeightInformationForAssocs (Possibility possibility) {
      // update weight information for associations Which are connecting explicit entities.
      double quality = 0.0;
      double weight = 0.0;
      int count = 0;
      double proximityPenalty = 0.0;
      for (Association association : possibility.getAssociations()) {
         if (!association.isForImplicitToken()) {
            quality += association.getWeightInformation().getRecognitionQuality();
            weight += association.getWeightInformation().getRecognitionWeight();
            count = count + 1;
         } else {
            // if the association is between one of the implicit token calculate the proximity reduction
            int subjectPostion = association.getSubjectPostion();
            int predicatePosition = association.getObjectPostion();
            int proximity = predicatePosition - subjectPostion;
            if (proximity < 0) {
               proximity = proximity * -1;
            }
            proximityPenalty = proximity + proximityPenalty;
         }
      }
      if (count == 0) {
         quality = 0;
      } else {
         quality = quality / count;
      }
      WeightInformation assocWeightInfo = new WeightInformation();
      assocWeightInfo.setRecognitionQuality(quality);
      assocWeightInfo.setRecognitionWeight(weight);
      possibility.setWeightInfoForAssociation(assocWeightInfo);
      proximityPenalty = proximityPenalty * 0.1;
      possibility.setProximityPenalty(proximityPenalty);
   }

   private boolean penalizeForImplicitConceptAssociation (String appName) {
      return getNlpConfigurationService().getPenaltyFlagForImplicitConceptAssociationByAppName(appName);

   }

   /**
    * If a recEntity is not connected in the graph the weight for the entity will be reduced by 20%
    * 
    * @param recEntityFinalWeight
    * @param recEntity
    * @param associations
    * @return
    */
   private double peanalizeWeightIfEntityIsNotConnected (double recEntityFinalWeight, RecognitionEntity recEntity,
            List<Association> associations) {
      boolean directPath = false;
      boolean entityConnceted = false;
      Association inDirectAssociation = null;
      for (Association association : associations) {
         List<RecognitionEntity> pathComponents = association.getPathComponent();
         if (pathComponents.contains(recEntity)) {
            entityConnceted = true;
            if (association.getPathComponent().size() == 3) {
               directPath = true;
            } else {
               inDirectAssociation = association;
            }
         }
      }
      if (!entityConnceted) {
         double weightReduction = Double.parseDouble(getNlpConfigurationService()
                  .getUnassociatedEntityWeightReduction());
         recEntityFinalWeight = recEntityFinalWeight - recEntityFinalWeight * weightReduction / 100;
         double recEntityQuality = recEntity.getRecognitionQuality() - recEntity.getRecognitionQuality()
                  * weightReduction / 100;
         recEntity.setRecognitionQuality(recEntityQuality);
         return recEntityFinalWeight;
      } else {
         if (directPath) {
            return recEntityFinalWeight;
         } else {
            double weightReduction = Double.parseDouble(getNlpConfigurationService()
                     .getIndirectAssociationWeightReduction());
            int pathLength = 1;
            if (inDirectAssociation != null) {
               pathLength = inDirectAssociation.getPathComponent().size() / 3;
               if (inDirectAssociation.getPathComponent().size() % 3 != 0) {
                  pathLength = pathLength + 1;
               }
            }
            recEntityFinalWeight = recEntityFinalWeight - recEntityFinalWeight * weightReduction * pathLength / 100;
            double recEntityQuality = recEntity.getRecognitionQuality() - recEntity.getRecognitionQuality()
                     * weightReduction * pathLength / 100;
            recEntity.setRecognitionQuality(recEntityQuality);
            return recEntityFinalWeight;
         }
      }
   }

   private double peanalizeWeightIfConnectedThroughImplicitTokens (double finalWeight, Possibility possibility) {
      List<RecognitionEntity> recEnitiesInAssocWithImplicitTokens = new ArrayList<RecognitionEntity>(1);
      for (Association association : possibility.getAssociations()) {
         if (association.isForImplicitToken()) {
            List<RecognitionEntity> pathComponents = association.getPathComponent();
            RecognitionEntity start = pathComponents.get(0);
            RecognitionEntity end = pathComponents.get(pathComponents.size() - 1);
            if (start.getPosition() >= 0) {
               recEnitiesInAssocWithImplicitTokens.add(start);
            } else if (end.getPosition() >= 0) {
               recEnitiesInAssocWithImplicitTokens.add(end);
            }
         }
      }

      if (recEnitiesInAssocWithImplicitTokens.size() > 0) {
         double weightReduction = Double.parseDouble(getNlpConfigurationService()
                  .getImplicitAssociationWeightReduction());
         return finalWeight * weightReduction / 100;
      }
      return 0.0;
   }

   public void updatePossibilityWeight (List<Possibility> possibilities, RootMatrix rootMatrix) {
      for (Possibility possibility : possibilities) {
         calculateEntitiesWeight(possibility, rootMatrix.getBaseWeightInformation());
      }
   }

   /**
    * @param recognitionEntities
    * @return
    */
   public double getRecognitionQualityWithoutImplicitRecs (List<IWeightedEntity> recognitionEntities) {
      double quality = 0;
      int totalRecognitions = 0;
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         // Skip the implicit entities
         if (recEntity.getPosition() < 0) {
            continue;
         }
         ReferedTokenPosition rtp = new ReferedTokenPosition(recEntity.getReferedTokenPositions());
         double recEntityQuality = recEntity.getRecognitionQuality() * rtp.getReferedTokenPositions().size();
         quality = quality + recEntityQuality;
         totalRecognitions = totalRecognitions + rtp.getReferedTokenPositions().size();
      }
      if (totalRecognitions == 0) {
         return quality;
      }
      return quality / totalRecognitions;
   }

   public double getRecognitionQualityWithoutImplicitRecsForUserQuery (List<IWeightedEntity> recognitionEntities,
            int userQueryTokenCount) {
      double quality = 0;
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         // Skip the implicit entities
         if (recEntity.getPosition() < 0) {
            continue;
         }
         quality = quality + recEntity.getRecognitionQuality();
      }
      return quality / userQueryTokenCount;
   }

   public void populateModelBasedPossibilities (RootMatrix rootMatrix) throws KDXException {
      Possibility possibility = rootMatrix.getPossibilities().get(0);
      Map<Long, List<IWeightedEntity>> recEntitiesByModelGroupId = new HashMap<Long, List<IWeightedEntity>>();
      List<IWeightedEntity> baseEntities = new ArrayList<IWeightedEntity>(1);
      for (IWeightedEntity weightedEntity : possibility.getRecognitionEntities()) {
         OntoEntity ontoEntity = (OntoEntity) weightedEntity;
         Long modelGroupId = ontoEntity.getModelGroupId();
         if (modelGroupId == null || modelGroupId.equals(1L)) {
            if (ontoEntity.getEntityType() != RecognitionEntityType.TYPE_ENTITY) {
               baseEntities.add(ontoEntity);
            }
         } else {
            List<IWeightedEntity> recEnList = recEntitiesByModelGroupId.get(modelGroupId);
            if (recEnList == null) {
               recEnList = new ArrayList<IWeightedEntity>(1);
               recEntitiesByModelGroupId.put(modelGroupId, recEnList);
            }
            recEnList.add(ontoEntity);
         }
      }
      List<Possibility> possibilities = new ArrayList<Possibility>(1);

      for (Entry<Long, List<IWeightedEntity>> entry : recEntitiesByModelGroupId.entrySet()) {
         Possibility poss = new Possibility();
         Model model = getKdxRetrievalService().getModelByUserModelGroupId(entry.getKey());
         poss.setId(rootMatrix.getNextPossibilityId());
         poss.setModel(model);
         List<IWeightedEntity> weighteEntities = new ArrayList<IWeightedEntity>(1);
         weighteEntities.addAll(entry.getValue());
         weighteEntities.addAll(baseEntities);
         poss.setRecognitionEntities(weighteEntities);
         possibilities.add(poss);
      }
      rootMatrix.setPossibilities(possibilities);
   }

   /**
    * Method to filter the subset entities for the same type.
    * 
    * @param recognitionEntities
    */
   public void filterSubsetTypeEntities (Set<IWeightedEntity> recognitionEntities) {
      if (CollectionUtils.isEmpty(recognitionEntities) || recognitionEntities.size() < 2) {
         return;
      }
      Map<Long, List<IWeightedEntity>> recognitionEntitiesByTypeBedId = new HashMap<Long, List<IWeightedEntity>>(1);
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         TypeEntity typeEntity = (TypeEntity) weightedEntity;
         if (typeEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY) {
            if (typeEntity.getNLPtag().equals(NLPConstants.NLP_TAG_ONTO_REALIZED_TYPE_INSTANCE)
                     || typeEntity.getNLPtag().equals(NLPConstants.NLP_TAG_ONTO_TYPE_INSTANCE)
                     || typeEntity.getTypeDisplayName().equalsIgnoreCase(RecognizedType.VALUE_TYPE.getValue())) {
               Long typeBedId = typeEntity.getTypeBedId();
               List<IWeightedEntity> entities = recognitionEntitiesByTypeBedId.get(typeBedId);
               if (entities == null) {
                  entities = new ArrayList<IWeightedEntity>(1);
                  recognitionEntitiesByTypeBedId.put(typeBedId, entities);
               }
               entities.add(typeEntity);
            }
         }
      }

      Set<IWeightedEntity> removeList = new HashSet<IWeightedEntity>(1);
      applySubsetFilterOnTypeRecognitionEntities(recognitionEntitiesByTypeBedId, recognitionEntities, removeList);
      recognitionEntities.removeAll(removeList);
   }

   public void filterBaseValueRecognitionEntities (List<IWeightedEntity> recognitionEntities) {
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return;
      }
      NLPUtilities.sortRecognitionEntitiesByRTPSize(recognitionEntities);
      Map<ReferedTokenPosition, List<IWeightedEntity>> posEntityMap = NLPUtilities
               .getRecognitionEntitiesByRTPMap(recognitionEntities);
      for (Entry<ReferedTokenPosition, List<IWeightedEntity>> entry : posEntityMap.entrySet()) {
         List<IWeightedEntity> baseEntities = new ArrayList<IWeightedEntity>(1);
         List<IWeightedEntity> appEntities = new ArrayList<IWeightedEntity>(1);
         for (IWeightedEntity weightedEntity : entry.getValue()) {
            RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
            if (recEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY) {
               if (((InstanceEntity) recEntity).getTypeDisplayName().equals(ExecueConstants.VALUE_TYPE)) {
                  if (((InstanceEntity) recEntity).getModelGroupId().equals(1L)) {
                     baseEntities.add(recEntity);
                  } else {
                     appEntities.add(recEntity);
                  }
               }
            }
         }
         if (!CollectionUtils.isEmpty(appEntities)) {
            recognitionEntities.removeAll(baseEntities);
         }
      }
   }

   /**
    * Filter the subset base recognition entities if for the same position we have the app level super set realization
    * entities. eg: operator, adjective, etc
    * 
    * @param recognitionEntities
    */
   public void filterSubsetBaseRecognitionEntities (List<IWeightedEntity> recognitionEntities) {
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return;
      }
      Map<ReferedTokenPosition, List<IWeightedEntity>> posEntityMap = NLPUtilities
               .getRecognitionEntitiesByRTPWithSubsetRTPMap(recognitionEntities);
      List<IWeightedEntity> baseSubsetEntities = new ArrayList<IWeightedEntity>(1);
      for (Entry<ReferedTokenPosition, List<IWeightedEntity>> entry : posEntityMap.entrySet()) {
         ReferedTokenPosition superSetRTP = entry.getKey();
         List<IWeightedEntity> recEntityList = entry.getValue();
         for (IWeightedEntity weightedEntity : recEntityList) {
            RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
            ReferedTokenPosition recEntityRTP = new ReferedTokenPosition(recEntity.getReferedTokenPositions());
            if (recEntityRTP.isSubset(superSetRTP)
                     && recEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY
                     && ((InstanceEntity) recEntity).getModelGroupId().equals(1L)) {
               baseSubsetEntities.add(recEntity);
            }
         }
      }
      recognitionEntities.removeAll(baseSubsetEntities);
   }

   /**
    * This method filters all the subset recognition entities if the top level superset recognition(s) 
    * is of full quality. Else doesn't filter any of the subset recognition(s).  
    * 
    * @param recognitionEntities
    */
   public void filterEntitiesIfFullMatchFound (List<IWeightedEntity> recognitionEntities) {
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return;
      }
      Map<ReferedTokenPosition, List<IWeightedEntity>> posEntityMap = NLPUtilities
               .getRecognitionEntitiesByRTPWithSubsetRTPMap(recognitionEntities);
      List<IWeightedEntity> finalEnList = new ArrayList<IWeightedEntity>(1);
      for (Entry<ReferedTokenPosition, List<IWeightedEntity>> entry : posEntityMap.entrySet()) {
         List<IWeightedEntity> recEntityList = entry.getValue();
         RecognitionEntity recEntity = (RecognitionEntity) recEntityList.get(0);
         if (recEntity.getRecognitionQuality() == 1) {
            // full match found.
            for (IWeightedEntity weightedEntity : recEntityList) {
               recEntity = (RecognitionEntity) weightedEntity;
               if (recEntity.getRecognitionQuality() == 1) {
                  finalEnList.add(recEntity);
               }
            }
         } else {
            finalEnList.addAll(entry.getValue());
         }
      }
      recognitionEntities.retainAll(finalEnList);
   }

   /**
    * Method to filter the OntoEntities which are at the same positions by topCluster. as we are doing this for an
    * AppCloud it will not affect other app clouds. This method first sorts the RecEntities By positions size i.e. the
    * more positions a recEntity is referring comes first. A map will be created for these positions and all the
    * entities which refers to the same position or are subset of this referred token positions wll take part in
    * filtering together.
    * 
    * @param appCloudEntity
    */
   public void filterEntitiesByTopCluster (List<IWeightedEntity> recognitionEntities) {
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return;
      }
      NLPUtilities.sortRecognitionEntitiesByRTPSize(recognitionEntities);
      Map<ReferedTokenPosition, List<IWeightedEntity>> posEntityMap = NLPUtilities
               .getRecognitionEntitiesByRTPMap(recognitionEntities);
      List<IWeightedEntity> finalEnList = new ArrayList<IWeightedEntity>(1);
      // TODO: NK: Need to check with AP, if we should set the applyLiberal flag to true/false
      for (Entry<ReferedTokenPosition, List<IWeightedEntity>> entry : posEntityMap.entrySet()) {
         finalEnList.addAll(NLPUtilities.performTopCluster(entry.getValue(), false));
      }
      recognitionEntities.retainAll(finalEnList);
   }

   /**
    * Method to filter the IWeightedEntity of type EntityType.TYPE_ENTITY in the input appCloudEntity
    * 
    * @param appCloudEntity
    *           app cloud entity
    */
   public void filterTypeEntities (Collection<IWeightedEntity> recognitionEntities) {
      List<IWeightedEntity> removeList = new ArrayList<IWeightedEntity>(1);
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         if (recEntity.getEntityType() == RecognitionEntityType.TYPE_ENTITY) {
            removeList.add(weightedEntity);
         }// TODO put this filter for number at the correct place.
         else if (recEntity.getNLPtag() == NLPConstants.NLP_TAG_ONTO_TYPE_REGEX_INSTANCE) {
            removeList.add(weightedEntity);
         } else if (RecognizedType.getCloudType(((TypeEntity) recEntity).getTypeDisplayName()) == RecognizedType.COMPARATIVE_STATISTICS_TYPE) {
            removeList.add(weightedEntity);
         }
      }
      recognitionEntities.removeAll(removeList);
   }

   /**
    * Method to filter the subset entities for the same type realizations.
    * 
    * @param recognitionEntities
    */
   public void filterSubsetTypeRealizationEntities (Collection<IWeightedEntity> recognitionEntities) {
      if (CollectionUtils.isEmpty(recognitionEntities) || recognitionEntities.size() < 2) {
         return;
      }
      Map<Long, List<IWeightedEntity>> recognitionEntitiesByTypeBedId = new HashMap<Long, List<IWeightedEntity>>(1);
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         TypeEntity typeEntity = (TypeEntity) weightedEntity;
         if (typeEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY
                  || typeEntity.getEntityType() == RecognitionEntityType.CONCEPT_ENTITY) {
            Long typeBedId = typeEntity.getTypeBedId();
            List<IWeightedEntity> entities = recognitionEntitiesByTypeBedId.get(typeBedId);
            if (entities == null) {
               entities = new ArrayList<IWeightedEntity>(1);
               recognitionEntitiesByTypeBedId.put(typeBedId, entities);
            }
            entities.add(typeEntity);
         }
      }

      Set<IWeightedEntity> removeList = new HashSet<IWeightedEntity>(1);
      applySubsetFilterOnTypeRealizatinRecognitionEntities(recognitionEntitiesByTypeBedId, recognitionEntities,
               removeList);
      recognitionEntities.removeAll(removeList);
   }

   /**
    * Method to filter the shared model type entities like location if the positions of them are equal or subset of any
    * other shared model recognition entity.
    * 
    * @param recognitionEntities
    */
   public void filterSharedModelTypeEntities (Collection<IWeightedEntity> recognitionEntities) {
      if (CollectionUtils.isEmpty(recognitionEntities) || recognitionEntities.size() < 2) {
         return;
      }
      Map<Long, List<IWeightedEntity>> recognitionEntitiesBySharedBedId = new HashMap<Long, List<IWeightedEntity>>(1);
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         TypeEntity typeEntity = (TypeEntity) weightedEntity;
         Long modelGroupId = typeEntity.getModelGroupId();
         // Check if the recognition entity is from type cloud(model group is null) or from shared model group
         // and entity type is instance entity
         if ((modelGroupId == null || ExecueConstants.LOCATION_MODEL_GROUP_ID.equals(modelGroupId))
                  && typeEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY) {
            Long typeBedId = typeEntity.getTypeBedId();
            // If child of location then consider the type as location itself
            if (getLocationConfigurationService().isLocationType(typeBedId)
                     || getLocationConfigurationService().isChildOfLocationType(typeBedId)) {
               if (getLocationConfigurationService().isChildOfLocationType(typeBedId)) {
                  typeBedId = getLocationConfigurationService().getParentBedIdForChildBedId(typeBedId);
               }
               List<IWeightedEntity> entities = recognitionEntitiesBySharedBedId.get(typeBedId);
               if (entities == null) {
                  entities = new ArrayList<IWeightedEntity>(1);
                  recognitionEntitiesBySharedBedId.put(typeBedId, entities);
               }
               entities.add(typeEntity);
            }
         }
      }

      List<IWeightedEntity> removeList = new ArrayList<IWeightedEntity>(1);
      applyFilterOnSharedRecognitionEntities(recognitionEntitiesBySharedBedId, removeList);
      recognitionEntities.removeAll(removeList);
   }

   /**
    * @param recognitionEntitiesByTypeBedId
    * @param recognitionEntities
    * @param removeList
    */
   private void applySubsetFilterOnTypeRecognitionEntities (
            Map<Long, List<IWeightedEntity>> recognitionEntitiesByTypeBedId, Set<IWeightedEntity> recognitionEntities,
            Set<IWeightedEntity> removeList) {
      for (Entry<Long, List<IWeightedEntity>> entry : recognitionEntitiesByTypeBedId.entrySet()) {
         List<IWeightedEntity> entities = entry.getValue();
         // if there is only one entity for type skip the filtering
         if (entities.size() < 2) {
            continue;
         }
         for (IWeightedEntity weightedEntity : entities) {
            RecognitionEntity recEntity1 = (RecognitionEntity) weightedEntity;
            TypeEntity typeEntity1 = (TypeEntity) recEntity1;
            if (removeList.contains(recEntity1)) {
               continue;
            }
            List<Integer> referedTokenPositions = recEntity1.getReferedTokenPositions();
            ReferedTokenPosition rtp1 = new ReferedTokenPosition(referedTokenPositions);
            for (IWeightedEntity weightedEntity2 : entities) {
               RecognitionEntity recEntity2 = (RecognitionEntity) weightedEntity2;
               TypeEntity typeEntity2 = (TypeEntity) recEntity2;
               if (recEntity1.equals(recEntity2) || removeList.contains(weightedEntity2)) {
                  continue;
               }
               List<Integer> referedTokenPositions2 = recEntity2.getReferedTokenPositions();
               ReferedTokenPosition rtp2 = new ReferedTokenPosition(referedTokenPositions2);
               if (rtp1.equals(rtp2)) {
                  // Check if they are of same realized type, then only go with the filter
                  // Eg: Plain Value(or even currency) type recognition can be on same position as Number Value type recognition, 
                  // but they should not get filtered here even though they all are value type realizations
                  Long conceptBedId1 = ((ConceptEntity) recEntity1).getConceptBedId();
                  Long conceptBedId2 = ((ConceptEntity) recEntity2).getConceptBedId();
                  if (conceptBedId1 != null && !conceptBedId1.equals(conceptBedId2)) {
                     continue;
                  } else if (conceptBedId2 != null && !conceptBedId2.equals(conceptBedId1)) {
                     continue;
                  }
                  // Filter based on the weight if recognitions are on the same positions
                  double weight1 = recEntity1.getWeightInformation().getFinalWeight();
                  double weight2 = recEntity2.getWeightInformation().getFinalWeight();
                  if (weight1 < weight2) {
                     removeList.add(recEntity1);
                  } else if (weight2 < weight1) {
                     removeList.add(recEntity2);
                  }
               } else if (rtp2.isSubset(rtp1)) {
                  // if recEntity 2 is subset of recEntity1
                  boolean entityToRemove = true;
                  List<Integer> overlappingPositions = rtp1.getOverlappingPositions(rtp2);
                  // get the extra referred token positions in entity1
                  ReferedTokenPosition remainingPos = new ReferedTokenPosition();
                  remainingPos.addAll(rtp1.getReferedTokenPositions());
                  remainingPos.getReferedTokenPositions().removeAll(overlappingPositions);

                  List<IWeightedEntity> relatedEntities = NLPUtilities.getRecognitionEntitiesForRelatedPosition(
                           recognitionEntities, remainingPos);

                  // If there remaining position are part of any other cloud then entityToRemove flag will be
                  // reset to false.
                  for (IWeightedEntity relatedEntity : relatedEntities) {
                     if (relatedEntity.equals(weightedEntity2) || relatedEntity.equals(weightedEntity)) {
                        continue;
                     }
                     ReferedTokenPosition rtp = new ReferedTokenPosition(((RecognitionEntity) weightedEntity)
                              .getReferedTokenPositions());
                     if (!remainingPos.equals(rtp) && remainingPos.isOverLap(rtp)) {
                        TypeEntity typeEntity = (TypeEntity) relatedEntity;
                        TypeEntity subEntity = (TypeEntity) recEntity2;
                        if (!subEntity.getTypeBedId().equals(typeEntity.getTypeBedId())) {
                           entityToRemove = false;
                           break;
                        }
                     }
                     if (!entityToRemove) {
                        break;
                     }
                  }
                  if (entityToRemove && typeEntity1.getTypeBedId().equals(typeEntity2.getTypeBedId())) {
                     removeList.add(recEntity2);
                  }
               }
            }
         }
      }
   }

   /**
    * @param recognitionEntitiesByTypeBedId
    * @param recognitionEntities
    * @param removeList
    */
   private void applySubsetFilterOnTypeRealizatinRecognitionEntities (
            Map<Long, List<IWeightedEntity>> recognitionEntitiesByTypeBedId,
            Collection<IWeightedEntity> recognitionEntities, Set<IWeightedEntity> removeList) {
      for (Entry<Long, List<IWeightedEntity>> entry : recognitionEntitiesByTypeBedId.entrySet()) {
         List<IWeightedEntity> entities = entry.getValue();
         // if there is only one entity for type skip the filtering
         if (entities.size() < 2) {
            continue;
         }
         for (IWeightedEntity weightedEntity : entities) {
            RecognitionEntity recEntity1 = (RecognitionEntity) weightedEntity;
            TypeEntity typeEntity1 = (TypeEntity) recEntity1;
            if (removeList.contains(recEntity1)) {
               continue;
            }
            List<Integer> referedTokenPositions = recEntity1.getReferedTokenPositions();
            ReferedTokenPosition rtp1 = new ReferedTokenPosition(referedTokenPositions);
            for (IWeightedEntity weightedEntity2 : entities) {
               RecognitionEntity recEntity2 = (RecognitionEntity) weightedEntity2;
               TypeEntity typeEntity2 = (TypeEntity) recEntity2;
               if (recEntity1.equals(recEntity2) || removeList.contains(weightedEntity2)) {
                  continue;
               }
               // Check if they are of same realized type, then only go with the filter
               Long conceptBedId1 = ((ConceptEntity) recEntity1).getConceptBedId();
               Long conceptBedId2 = ((ConceptEntity) recEntity2).getConceptBedId();
               if (conceptBedId1 != null && !conceptBedId1.equals(conceptBedId2)) {
                  continue;
               } else if (conceptBedId2 != null && !conceptBedId2.equals(conceptBedId1)) {
                  continue;
               }
               List<Integer> referedTokenPositions2 = recEntity2.getReferedTokenPositions();
               ReferedTokenPosition rtp2 = new ReferedTokenPosition(referedTokenPositions2);
               if (rtp1.equals(rtp2)) {
                  // Filter based on the weight if recognitions are on the same positions
                  double weight1 = recEntity1.getWeightInformation().getFinalWeight();
                  double weight2 = recEntity2.getWeightInformation().getFinalWeight();
                  if (weight1 < weight2) {
                     removeList.add(recEntity1);
                  } else if (weight2 < weight1) {
                     removeList.add(recEntity2);
                  }
               } else if (rtp2.isSubset(rtp1)) {
                  removeList.add(recEntity2);
               }
            }
         }
      }
   }

   /**
    * @param recognitionEntitiesBySharedBedId
    * @param removeList
    */
   private void applyFilterOnSharedRecognitionEntities (
            Map<Long, List<IWeightedEntity>> recognitionEntitiesBySharedBedId, List<IWeightedEntity> removeList) {
      for (Entry<Long, List<IWeightedEntity>> entry : recognitionEntitiesBySharedBedId.entrySet()) {
         List<IWeightedEntity> entities = entry.getValue();
         // If there is only one entity for shared type skip the filtering
         if (entities.size() < 2) {
            continue;
         }
         for (IWeightedEntity weightedEntity : entities) {
            RecognitionEntity recEntity1 = (RecognitionEntity) weightedEntity;
            TypeEntity typeEntity1 = (TypeEntity) recEntity1;
            if (removeList.contains(recEntity1)) {
               continue;
            }
            ReferedTokenPosition rtp1 = new ReferedTokenPosition(recEntity1.getReferedTokenPositions());
            for (IWeightedEntity weightedEntity2 : entities) {
               RecognitionEntity recEntity2 = (RecognitionEntity) weightedEntity2;
               if (recEntity1.equals(recEntity2) || removeList.contains(recEntity2)) {
                  continue;
               }
               ReferedTokenPosition rtp2 = new ReferedTokenPosition(recEntity2.getReferedTokenPositions());
               // if rtp 1 is equals to rtp 2, then remove the rec entity which refers to the child of location type bed
               // id
               if (rtp1.equals(rtp2) || rtp1.isSubset(rtp2)) {
                  if (getLocationConfigurationService().isChildOfLocationType(typeEntity1.getTypeBedId())) {
                     removeList.add(recEntity1);
                  } else {
                     removeList.add(recEntity2);
                  }
               }
            }
         }
      }
   }

   /**
    * Method to remove the less priority Type Recognition Instance entity when for the same refered Token positions
    * Higher priority Type Recognition is Found. For Example Time Recognition takes the Higher priority as Compare To
    * Value Recognition. Also remove the Value Recognition if it is subset of TF recognition and vice versa remove the
    * TF recognition if it is subset of the Value Recognition. TODO -NA- Another Way to solve this could be to increase
    * the popularity of Time Concepts as compare to currency but that could not be fixed by generic rules.
    * 
    * @param cumulativeList
    */
   public void filterLessPriorityTypeRecognitions (Set<IWeightedEntity> cumulativeList) {
      if (CollectionUtils.isEmpty(cumulativeList)) {
         return;
      }
      Map<ReferedTokenPosition, List<IWeightedEntity>> recEntitiesByRTPMap = NLPUtilities
               .getRecognitionEntitiesByRTPMap(new ArrayList<IWeightedEntity>(cumulativeList));
      Map<ReferedTokenPosition, List<IWeightedEntity>> timeRecognitions = new HashMap<ReferedTokenPosition, List<IWeightedEntity>>(
               1);
      Map<ReferedTokenPosition, List<IWeightedEntity>> valueRecognitions = new HashMap<ReferedTokenPosition, List<IWeightedEntity>>(
               1);

      for (Entry<ReferedTokenPosition, List<IWeightedEntity>> entry : recEntitiesByRTPMap.entrySet()) {
         for (IWeightedEntity weightedEntity : entry.getValue()) {
            TypeEntity typeEntity = (TypeEntity) weightedEntity;
            if (typeEntity.getTypeDisplayName().equalsIgnoreCase(RecognizedType.TF_TYPE.getValue())) {
               List<IWeightedEntity> entities = timeRecognitions.get(entry.getKey());
               if (entities == null) {
                  entities = new ArrayList<IWeightedEntity>(1);
                  timeRecognitions.put(entry.getKey(), entities);
               }
               entities.add(typeEntity);
            } else if (typeEntity.getTypeDisplayName().equalsIgnoreCase(RecognizedType.VALUE_TYPE.getValue())) {
               List<IWeightedEntity> entities = valueRecognitions.get(entry.getKey());
               if (entities == null) {
                  entities = new ArrayList<IWeightedEntity>(1);
                  valueRecognitions.put(entry.getKey(), entities);
               }
               entities.add(typeEntity);
            }
         }
      }
      Set<IWeightedEntity> recognitionsToBeRemoved = new HashSet<IWeightedEntity>(1);
      for (Entry<ReferedTokenPosition, List<IWeightedEntity>> tfByRTPEntry : timeRecognitions.entrySet()) {
         ReferedTokenPosition tfRTP = tfByRTPEntry.getKey();
         for (Entry<ReferedTokenPosition, List<IWeightedEntity>> valueByRTPEntry : valueRecognitions.entrySet()) {
            ReferedTokenPosition valueRTP = valueByRTPEntry.getKey();
            if (valueRTP.isSubset(tfRTP)) {
               recognitionsToBeRemoved.addAll(valueByRTPEntry.getValue());
            } else if (tfRTP.isSubset(valueRTP)) {
               recognitionsToBeRemoved.addAll(tfByRTPEntry.getValue());
            }
         }
      }
      if (!CollectionUtils.isEmpty(recognitionsToBeRemoved)) {
         cumulativeList.removeAll(recognitionsToBeRemoved);
      }
   }

   public Set<Long> getModelGroupIdsBySearchFilter (SearchFilter searchFilter) {
      Set<Long> modelGroupIds = new HashSet<Long>(1);
      boolean searchFilterProvided = searchFilter != null
               && searchFilter.getSearchFilterType() != SearchFilterType.GENERAL;
      if (searchFilterProvided) {
         if (searchFilter.getSearchFilterType() == SearchFilterType.APP) {
            try {
               Application application = getApplicationRetrievalService().getApplicationById(searchFilter.getId());
               populateValidModelGroupIds(modelGroupIds, application);
            } catch (KDXException e) {
               throw new NLPSystemException(e.getCode(), e.getMessage(), e);
            }
         } else if (searchFilter.getSearchFilterType() == SearchFilterType.VERTICAL) {
            try {
               List<Application> appList = getApplicationRetrievalService().getApplicationsByVerticalId(
                        searchFilter.getId());
               for (Application application : appList) {
                  populateValidModelGroupIds(modelGroupIds, application);
               }
            } catch (KDXException e) {
               throw new NLPSystemException(e.getCode(), e.getMessage(), e);
            }
         } else if (searchFilter.getSearchFilterType() == SearchFilterType.APP_SCOPED) {
            try {
               for (Long applicationId : searchFilter.getAppIds()) {
                  Application application = getApplicationRetrievalService().getApplicationById(applicationId);
                  populateValidModelGroupIds(modelGroupIds, application);
               }
            } catch (KDXException e) {
               throw new NLPSystemException(e.getCode(), e.getMessage(), e);
            }
         }
      }
      return modelGroupIds;
   }

   private void populateValidModelGroupIds (Set<Long> modelGroupIds, Application application) {
      for (Model model : application.getModels()) {
         for (ModelGroup mg : model.getModelGroups()) {
            modelGroupIds.add(mg.getId());
         }
      }
   }

   /**
    * Method to perform list of similar type/concept instances.
    * 
    * @param recognitionEntities
    * @param instancesByParentBedId
    *           where parent can be type or concept bed id
    * @param origRecEntitesByPosMap
    * @param unrecognizedBaseEntityByPosMap
    * @param unrecognizedEntityByPosMap
    * @param removeIndividualEntities
    */
   public void performListForInstances (List<IWeightedEntity> recognitionEntities,
            Map<Long, List<IWeightedEntity>> instancesByParentBedId,
            Map<Integer, List<IWeightedEntity>> origRecEntitesByPosMap,
            Map<Integer, List<IWeightedEntity>> unrecognizedBaseEntityByPosMap,
            Map<Integer, List<IWeightedEntity>> unrecognizedEntityByPosMap, boolean removeIndividualEntities) {
      HashSet<IWeightedEntity> finalEntities = new HashSet<IWeightedEntity>();
      finalEntities.addAll(recognitionEntities);
      for (Entry<Long, List<IWeightedEntity>> entry : instancesByParentBedId.entrySet()) {
         // If there are less then 2 entities for a component continue.
         if (entry.getValue().size() < 2) {
            continue;
         }
         // List to keep track of the entities which participated in list.
         List<IWeightedEntity> entitiesParticipatedInList = new ArrayList<IWeightedEntity>();
         // Stack to maintain the merged as well as the existing entities
         Stack<IWeightedEntity> entitiesToVisit = new Stack<IWeightedEntity>();
         List<IWeightedEntity> recEntities = entry.getValue();
         // Sort the recEntities By position
         NLPUtilities.sortRecognitionEntitiesByPosition(recEntities);
         for (IWeightedEntity weightedEntity : recEntities) {
            if (CollectionUtils.isEmpty(entitiesToVisit)) {
               entitiesToVisit.push(weightedEntity);
            } else {
               OntoEntity currentEntity = (OntoEntity) weightedEntity;
               Set<IWeightedEntity> mergedEntities = new HashSet<IWeightedEntity>(1);
               for (int i = entitiesToVisit.size() - 1; i >= 0; i--) {
                  OntoEntity entityToCheck = (OntoEntity) entitiesToVisit.get(i);
                  checkIfListCanBePerformed(entityToCheck, currentEntity, origRecEntitesByPosMap,
                           unrecognizedBaseEntityByPosMap, unrecognizedEntityByPosMap, entitiesParticipatedInList,
                           mergedEntities, removeIndividualEntities);
               }
               if (!CollectionUtils.isEmpty(mergedEntities)) {
                  entitiesToVisit.addAll(mergedEntities);
               } else {
                  entitiesToVisit.push(weightedEntity);
               }
            }
         }
         if (!CollectionUtils.isEmpty(entitiesParticipatedInList)) {
            entitiesToVisit.removeAll(entitiesParticipatedInList);
            finalEntities.removeAll(entitiesParticipatedInList);
         }
         finalEntities.addAll(entitiesToVisit);
      }
      recognitionEntities.clear();
      recognitionEntities.addAll(finalEntities);
   }

   /**
    * @param recEntity1
    * @param recEntity2
    * @param origRecEntitiesByPosition
    * @param unrecognizedBaseEntitiesByPosition
    * @param unrecognizedEntitiesByPosition
    * @param entitiesParticipatedInList
    * @param mergedEntities
    * @param removeIndividualEntities
    */
   private void checkIfListCanBePerformed (OntoEntity recEntity1, OntoEntity recEntity2,
            Map<Integer, List<IWeightedEntity>> origRecEntitiesByPosition,
            Map<Integer, List<IWeightedEntity>> unrecognizedBaseEntitiesByPosition,
            Map<Integer, List<IWeightedEntity>> unrecognizedEntitiesByPosition,
            List<IWeightedEntity> entitiesParticipatedInList, Set<IWeightedEntity> mergedEntities,
            boolean removeIndividualEntities) {
      ReferedTokenPosition rtp1 = new ReferedTokenPosition(recEntity1.getReferedTokenPositions());
      ReferedTokenPosition rtp2 = new ReferedTokenPosition(recEntity2.getReferedTokenPositions());
      List<Integer> inBetweenPositions = rtp1.getInBetweenPositions(rtp2);
      boolean canMerge = false;
      RecognitionEntity conjunctionEntity = null;
      if (recEntity1.getBehaviors().contains(BehaviorType.MUTUALYEXCLUSIVE)
               || recEntity2.getBehaviors().contains(BehaviorType.MUTUALYEXCLUSIVE)) {
         canMerge = false;
      } else if (recEntity1.getId() != null && recEntity2.getId() != null
               && recEntity1.getId().equals(recEntity2.getId())) {
         canMerge = false;
      } else if (rtp1.equals(rtp2) || rtp1.getReferedTokenPositions().containsAll(rtp2.getReferedTokenPositions())
               || rtp2.getReferedTokenPositions().containsAll(rtp1.getReferedTokenPositions())) {
         canMerge = false;
      } else if (rtp1.isOverLap(rtp2)
               && (recEntity1.getNLPtag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_INSTANCE) || recEntity1
                        .getNLPtag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_CONCEPT_INSTANCE))
               && (recEntity2.getNLPtag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_INSTANCE) || recEntity2
                        .getNLPtag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_CONCEPT_INSTANCE))) {
         if (((InstanceEntity) recEntity1).getInstanceInformations().containsAll(
                  ((InstanceEntity) recEntity2).getInstanceInformations())
                  || ((InstanceEntity) recEntity2).getInstanceInformations().containsAll(
                           ((InstanceEntity) recEntity1).getInstanceInformations())) {
            canMerge = false;
         } else {
            canMerge = true;
         }
      } else if (CollectionUtils.isEmpty(inBetweenPositions)) {
         canMerge = true;
      } else if (inBetweenPositions.size() == 1) {
         List<IWeightedEntity> inBetweenEntities = origRecEntitiesByPosition.get(inBetweenPositions.get(0));
         if (CollectionUtils.isEmpty(inBetweenEntities)) {
            // Check for in b/w positions are part of unrecognized entities or unrecognized base entities to handle the
            // merge accordingly
            List<IWeightedEntity> unrecognizedEntities = unrecognizedEntitiesByPosition.get(inBetweenPositions.get(0));
            List<IWeightedEntity> unrecognizedBaseEntities = unrecognizedBaseEntitiesByPosition.get(inBetweenPositions
                     .get(0));
            if (ExecueCoreUtil.isCollectionNotEmpty(unrecognizedEntities)) {
               // If we get something in b/w as unrecognized, then we cannot merge
               canMerge = false;
            } else if (CollectionUtils.isEmpty(unrecognizedBaseEntities)) {
               // If unrecognized base recognition entities is empty, then we can merge
               canMerge = true;
            } else if (CollectionUtils.size(unrecognizedBaseEntities) == 1) {
               // If unrecognized base entities is in b/w and is only one then also we can merge
               canMerge = true;
               // Also set the unrecognized base entity as conjunction entity
               RecognitionEntity recEntity = (RecognitionEntity) unrecognizedBaseEntities.get(0);
               conjunctionEntity = recEntity;
            }
         } else {
            for (IWeightedEntity inBetween : inBetweenEntities) {
               RecognitionEntity recEntity = (RecognitionEntity) inBetween;
               if (recEntity instanceof TypeEntity) {
                  TypeEntity typeEntity = (TypeEntity) recEntity;
                  if (typeEntity.getTypeDisplayName().equals(OntologyConstants.COORDINATING_CONCJUNCTION)
                           || typeEntity.getTypeDisplayName().equalsIgnoreCase(OntologyConstants.PUNCTUATION)) {
                     conjunctionEntity = typeEntity;
                     canMerge = true;
                  }
               }
            }
         }
      }
      if (canMerge) {
         canMerge = validateListParticipantsByDetailType(recEntity1, recEntity2);
      }
      if (canMerge) {
         RecognitionEntity newRecEntity = mergeRecEntitiesForList(recEntity1, recEntity2, conjunctionEntity);
         mergedEntities.add(recEntity1);
         mergedEntities.add(newRecEntity);
         if (removeIndividualEntities) {
            entitiesParticipatedInList.add(recEntity1);
            entitiesParticipatedInList.add(recEntity2);
         }
         if (conjunctionEntity != null) {
            entitiesParticipatedInList.add(conjunctionEntity);
         }
      }
   }

   /**
    * Method to validate if the two recEntities can be part of list based on detail type if the entities are of type TF
    * and if either of entity is rance it can not make list with another single entity.
    * 
    * @param recEntity1
    * @param recEntity2
    * @return
    */
   private boolean validateListParticipantsByDetailType (OntoEntity recEntity1, OntoEntity recEntity2) {
      InstanceEntity instanceEntity1 = (InstanceEntity) recEntity1;
      InstanceEntity instanceEntity2 = (InstanceEntity) recEntity2;
      if (instanceEntity1.getNormalizedData() != null && instanceEntity2.getNormalizedData() != null) {
         INormalizedData normalizedData1 = instanceEntity1.getNormalizedData();
         INormalizedData normalizedData2 = instanceEntity2.getNormalizedData();
         // Check if only one of the normalized data is of type range, then return false 
         if (normalizedData1.getNormalizedDataType() == NormalizedDataType.RANGE_NORMALIZED_DATA
                  && normalizedData2.getNormalizedDataType() != NormalizedDataType.RANGE_NORMALIZED_DATA
                  || normalizedData2.getNormalizedDataType() == NormalizedDataType.RANGE_NORMALIZED_DATA
                  && normalizedData1.getNormalizedDataType() != NormalizedDataType.RANGE_NORMALIZED_DATA) {
            return false;
         } else {
            DateQualifier detailType1 = getDetailTypeForTimeFrameNormalizedData(normalizedData1);
            DateQualifier detailType2 = getDetailTypeForTimeFrameNormalizedData(normalizedData2);
            if (detailType1 != null || detailType2 != null) {
               // Check if both the detail type are same, then return true else false
               if (detailType1 == detailType2) {
                  return true;
               } else {
                  return false;
               }
            }
         }
      }
      // return true, for all other default case
      return true;
   }

   private DateQualifier getDetailTypeForTimeFrameNormalizedData (INormalizedData normalizedData) {

      if (normalizedData instanceof TimeFrameNormalizedData) {
         TimeFrameNormalizedData tfNormalizedData1 = (TimeFrameNormalizedData) normalizedData;
         return tfNormalizedData1.getDateQualifier();
      } else if (normalizedData instanceof ListNormalizedData) {
         ListNormalizedData listNormalizedData = (ListNormalizedData) normalizedData;
         NormalizedDataEntity normalizedDataEntity = listNormalizedData.getNormalizedDataEntities().get(0);
         if (normalizedDataEntity.getNormalizedData() != null
                  && normalizedDataEntity.getNormalizedData() instanceof TimeFrameNormalizedData) {
            TimeFrameNormalizedData tfNormalizedData = (TimeFrameNormalizedData) normalizedDataEntity
                     .getNormalizedData();
            return tfNormalizedData.getDateQualifier();
         }
      }
      return null;
   }

   /**
    * Method to merge the rec entities for list.
    * 
    * @param recEntity1
    * @param recEntity2
    * @param conjunctionEntity
    * @return
    */
   private RecognitionEntity mergeRecEntitiesForList (RecognitionEntity recEntity1, RecognitionEntity recEntity2,
            RecognitionEntity conjunctionEntity) {
      RecognitionEntity mergedRecognitionEntity = null;
      try {
         // TODO: NK: Currently cloning the first one as both rec entity will be of same type
         mergedRecognitionEntity = (RecognitionEntity) recEntity1.clone();
      } catch (CloneNotSupportedException e) {
         throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
      }

      // Calculate the rec entity related information
      ReferedTokenPosition referedTokenPositions = new ReferedTokenPosition(recEntity1.getReferedTokenPositions());
      referedTokenPositions.addAll(recEntity2.getReferedTokenPositions());

      ReferedTokenPosition origReferedTokenPositions = new ReferedTokenPosition(recEntity1
               .getOriginalReferedTokenPositions());
      origReferedTokenPositions.addAll(recEntity2.getOriginalReferedTokenPositions());

      ReferedTokenPosition rtp1 = new ReferedTokenPosition(recEntity1.getReferedTokenPositions());
      ReferedTokenPosition rtp2 = new ReferedTokenPosition(recEntity2.getReferedTokenPositions());
      double recEntity1Quality = recEntity1.getRecognitionQuality() * rtp1.getReferedTokenPositions().size();
      double recEntity2Quality = recEntity2.getRecognitionQuality() * rtp2.getReferedTokenPositions().size();
      double individualRTPSize = rtp1.getReferedTokenPositions().size() + rtp2.getReferedTokenPositions().size();
      // There can be overlap ref positions for whom we need to adjust the weights
      // Hence to set the merged entity recognition weight, we need to consider the weight factor
      // i.e merged rtp size divided by sum of individual rtp size
      ReferedTokenPosition mergedRTP = new ReferedTokenPosition();
      mergedRTP.addAll(rtp1.getReferedTokenPositions());
      mergedRTP.addAll(rtp2.getReferedTokenPositions());
      int mergedRTPSize = mergedRTP.getReferedTokenPositions().size();
      double weightFactor = mergedRTPSize / individualRTPSize;
      double quality = (recEntity1Quality + recEntity2Quality) / individualRTPSize;
      double weight = (recEntity1.getRecognitionWeight() + recEntity2.getRecognitionWeight()) * weightFactor;
      if (conjunctionEntity != null) {
         quality = (quality * referedTokenPositions.getReferedTokenPositions().size() + conjunctionEntity
                  .getRecognitionQuality())
                  / (referedTokenPositions.getReferedTokenPositions().size() + 1);
         referedTokenPositions.addAll(conjunctionEntity.getReferedTokenPositions());
         origReferedTokenPositions.addAll(conjunctionEntity.getOriginalReferedTokenPositions());

         weight = weight + conjunctionEntity.getRecognitionWeight();
      }

      WeightInformation weightInformation = new WeightInformation();
      weightInformation.setRecognitionQuality(quality);
      weightInformation.setRecognitionWeight(weight);

      Set<NormalizedDataEntity> normalizedDataEntities = new HashSet<NormalizedDataEntity>();
      normalizedDataEntities.addAll(getNormalizedDataEntities(recEntity1));
      normalizedDataEntities.addAll(getNormalizedDataEntities(recEntity2));
      ListNormalizedData listNormalizedData = new ListNormalizedData();
      listNormalizedData.setType(((TypeEntity) recEntity1).getTypeDisplayName());
      listNormalizedData.setTypeBedId(((TypeEntity) recEntity1).getTypeBedId());
      listNormalizedData.setNormalizedDataEntities(new ArrayList<NormalizedDataEntity>(normalizedDataEntities));
      listNormalizedData.setReferredTokenPositions(referedTokenPositions.getReferedTokenPositions());
      listNormalizedData.setOriginalReferredTokenPositions(origReferedTokenPositions.getReferedTokenPositions());

      mergedRecognitionEntity.setWeightInformation(weightInformation);
      InstanceEntity instanceEntity1 = (InstanceEntity) recEntity1;
      InstanceEntity instanceEntity2 = (InstanceEntity) recEntity2;
      InstanceEntity newInstanceEntity = (InstanceEntity) mergedRecognitionEntity;
      // reset the instance bedId for new RecEntity.
      List<InstanceInformation> newInstanceInformations = new ArrayList<InstanceInformation>(1);
      newInstanceInformations.addAll(instanceEntity1.getInstanceInformations());
      newInstanceInformations.addAll(instanceEntity2.getInstanceInformations());
      newInstanceEntity.setInstanceInformations(newInstanceInformations);
      newInstanceEntity.setReferedTokenPositions(new ArrayList<Integer>(referedTokenPositions
               .getReferedTokenPositions()));
      newInstanceEntity.setOriginalReferedTokenPositions(new ArrayList<Integer>(origReferedTokenPositions
               .getReferedTokenPositions()));
      newInstanceEntity.setNormalizedData(listNormalizedData);
      newInstanceEntity.setStartPosition(referedTokenPositions.getReferedTokenPositions().first());
      newInstanceEntity.setEndPosition(referedTokenPositions.getReferedTokenPositions().last());
      // Update the hits info
      List<RecognitionEntity> inputRecs = new ArrayList<RecognitionEntity>(1);
      inputRecs.add(recEntity1);
      inputRecs.add(recEntity2);
      newInstanceEntity.setHitsUpdateInfo(NLPUtilities.getHitsUpdateInfo(inputRecs));

      return newInstanceEntity;
   }

   private List<NormalizedDataEntity> getNormalizedDataEntities (RecognitionEntity recognitionEntity) {
      List<NormalizedDataEntity> normalizedDataEntities = new ArrayList<NormalizedDataEntity>();
      INormalizedData normalizedData = recognitionEntity.getNormalizedData();
      if (normalizedData != null && normalizedData.getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
         normalizedDataEntities.addAll(((ListNormalizedData) normalizedData).getNormalizedDataEntities());
      } else {
         NormalizedDataEntity normalizeDataEntity = getNormalizeDataEntity(recognitionEntity, null);
         normalizedDataEntities.add(normalizeDataEntity);
         normalizeDataEntity.setNormalizedData(recognitionEntity.getNormalizedData());
      }
      return normalizedDataEntities;
   }

   /**
    * Method to return the map containing the list of recognition entities against the type bed id. This will consider
    * only the entities which are instances of type or realized types
    * 
    * @param recognitionEntities
    * @return the Map<Long, List<IWeightedEntity>>
    */
   public Map<Long, List<IWeightedEntity>> groupInstanceEntitiesByTypeBedId (
            Collection<IWeightedEntity> recognitionEntities) {
      Map<Long, List<IWeightedEntity>> instancesByTypeBedId = new HashMap<Long, List<IWeightedEntity>>(1);
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         TypeEntity typeEntity = (TypeEntity) weightedEntity;
         if (typeEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY
                  && (typeEntity.getNLPtag().equals(NLPConstants.NLP_TAG_ONTO_REALIZED_TYPE_INSTANCE) || typeEntity
                           .getNLPtag().equals(NLPConstants.NLP_TAG_ONTO_TYPE_INSTANCE))) {
            List<IWeightedEntity> entities = instancesByTypeBedId.get(typeEntity.getTypeBedId());
            if (entities == null) {
               entities = new ArrayList<IWeightedEntity>(1);
               instancesByTypeBedId.put(typeEntity.getTypeBedId(), entities);
            }
            entities.add(typeEntity);
         }
      }
      return instancesByTypeBedId;
   }

   /**
    * @param recEnList
    * @return
    */
   public Map<Long, List<IWeightedEntity>> groupInstanceEntitiesByConceptBeId (List<IWeightedEntity> recEnList) {
      Map<Long, List<IWeightedEntity>> instancesByConceptBedId = new HashMap<Long, List<IWeightedEntity>>(1);
      for (IWeightedEntity weightedEntity : recEnList) {
         TypeEntity typeEntity = (TypeEntity) weightedEntity;
         if (typeEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY) {
            ConceptEntity conceptEntity = (ConceptEntity) typeEntity;
            if (conceptEntity.getConceptBedId() == null) {
               continue;
            }
            List<IWeightedEntity> entities = instancesByConceptBedId.get(conceptEntity.getConceptBedId());
            if (entities == null) {
               entities = new ArrayList<IWeightedEntity>(1);
               instancesByConceptBedId.put(conceptEntity.getConceptBedId(), entities);
            }
            entities.add(typeEntity);
         }
      }
      return instancesByConceptBedId;
   }

   /**
    * Method to check and split the cloud entity if two entities at the same referred token positions exist in the
    * cloud. This is done to be sure that no cloud have two different meaning for the same positions.
    * 
    * @param frameWorkCloudEntities
    * @return
    * @throws CloneNotSupportedException
    */
   public List<RecognizedCloudEntity> splitBasedOnDuplicateRefTokenPositions (
            List<RecognizedCloudEntity> recognizedCloudEntities) {
      List<RecognizedCloudEntity> finalCloudEntities = new ArrayList<RecognizedCloudEntity>(1);
      for (RecognizedCloudEntity cloudEntity : recognizedCloudEntities) {
         Map<ReferedTokenPosition, Map<Long, List<IWeightedEntity>>> recEntitiesByPart = new HashMap<ReferedTokenPosition, Map<Long, List<IWeightedEntity>>>(
                  1);
         Map<ReferedTokenPosition, Map<Long, List<IWeightedEntity>>> moreRecEntitiesByPart = new HashMap<ReferedTokenPosition, Map<Long, List<IWeightedEntity>>>(
                  1);
         // populate the map of the rec Entities which are at same position and exists in the cloud
         populateDuplicatePositionsEntityInCloud(cloudEntity, recEntitiesByPart, moreRecEntitiesByPart);
         if (MapUtils.isEmpty(moreRecEntitiesByPart)) {
            finalCloudEntities.add(cloudEntity);
            continue;
         } else {
            // clone the original cloud entity and remove all the duplicate position entities from cloud.
            List<RecognizedCloudEntity> newClouds = new ArrayList<RecognizedCloudEntity>(1);

            RecognizedCloudEntity clonedCloudEntity = null;
            try {
               clonedCloudEntity = (RecognizedCloudEntity) cloudEntity.clone();
            } catch (CloneNotSupportedException e) {
               throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
            }
            for (Entry<ReferedTokenPosition, Map<Long, List<IWeightedEntity>>> entry : moreRecEntitiesByPart.entrySet()) {
               for (Entry<Long, List<IWeightedEntity>> entry2 : entry.getValue().entrySet()) {
                  CloudComponent cloudComponent = clonedCloudEntity.getCloudComponentInfoMap().get(entry2.getKey());
                  cloudComponent.getRecognitionEntities().removeAll(entry2.getValue());
                  clonedCloudEntity.getRecognitionEntities().removeAll(entry2.getValue());
               }
            }
            newClouds.add(clonedCloudEntity);
            // for each duplicate entity clone the cloud so that same position occurs only once in the cloud.
            for (Entry<ReferedTokenPosition, Map<Long, List<IWeightedEntity>>> entry : moreRecEntitiesByPart.entrySet()) {
               newClouds = splitEntitiesForDuplicatePositions(newClouds, entry.getValue());
               finalCloudEntities.addAll(newClouds);
            }
         }
      }
      // Update the cloud component as the rec entities got split
      updateCloudCompononentInfo(finalCloudEntities);
      // Filter the cloud if the split resulted in missing of the required cloud component
      finalCloudEntities = filterBasedOnRequiredCompCount(finalCloudEntities);

      return finalCloudEntities;

   }

   private List<RecognizedCloudEntity> filterBasedOnRequiredCompCount (List<RecognizedCloudEntity> cloudEntities) {
      if (CollectionUtils.isEmpty(cloudEntities)) {
         return cloudEntities;
      }
      List<RecognizedCloudEntity> finalRecCloudEntity = new ArrayList<RecognizedCloudEntity>(1);
      for (RecognizedCloudEntity recognizedCloudEntity : cloudEntities) {
         if (isValidCloudBasedOnRequiredCompCount(recognizedCloudEntity)) {
            finalRecCloudEntity.add(recognizedCloudEntity);
         }
      }

      return finalRecCloudEntity;
   }

   private boolean isValidCloudBasedOnRequiredCompCount (RecognizedCloudEntity recognizedCloudEntity) {
      int requiredComponentCount = recognizedCloudEntity.getCloud().getRequiredComponentCount().intValue();
      if (requiredComponentCount == 0) {
         return true;
      }
      Map<Long, CloudComponent> cloudComponentInfoMap = recognizedCloudEntity.getCloudComponentInfoMap();
      int currentRequiredCount = 0;
      for (Entry<Long, CloudComponent> entry : cloudComponentInfoMap.entrySet()) {
         if (!CollectionUtils.isEmpty(entry.getValue().getRecognitionEntities())
                  && entry.getValue().getRequired() == CheckType.YES) {
            currentRequiredCount++;
         }
      }
      if (currentRequiredCount >= requiredComponentCount) {
         return true;
      }
      return false;
   }

   /**
    * @param newClouds
    * @param entitiesMap
    * @return
    */
   private List<RecognizedCloudEntity> splitEntitiesForDuplicatePositions (List<RecognizedCloudEntity> newClouds,
            Map<Long, List<IWeightedEntity>> entitiesMap) {
      List<RecognizedCloudEntity> clonedEntities = new ArrayList<RecognizedCloudEntity>(1);
      for (RecognizedCloudEntity cloudEntity : newClouds) {
         for (Entry<Long, List<IWeightedEntity>> entry : entitiesMap.entrySet()) {
            for (IWeightedEntity weightedEntity : entry.getValue()) {
               RecognizedCloudEntity clonedRCE = null;
               try {
                  clonedRCE = (RecognizedCloudEntity) cloudEntity.clone();
               } catch (CloneNotSupportedException e) {
                  throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
               }
               CloudComponent cloudComponent = clonedRCE.getCloudComponentInfoMap().get(entry.getKey());
               cloudComponent.getRecognitionEntities().add(weightedEntity);
               clonedRCE.addRecognitionEntity(weightedEntity);
               clonedEntities.add(clonedRCE);
            }
         }
      }
      return clonedEntities;
   }

   /**
    * @param cloudEntity
    * @param recEntitiesByPart
    * @param moreRecEntitiesByPart
    */
   private void populateDuplicatePositionsEntityInCloud (RecognizedCloudEntity cloudEntity,
            Map<ReferedTokenPosition, Map<Long, List<IWeightedEntity>>> recEntitiesByPart,
            Map<ReferedTokenPosition, Map<Long, List<IWeightedEntity>>> moreRecEntitiesByPart) {
      for (Entry<Long, CloudComponent> entry : cloudEntity.getCloudComponentInfoMap().entrySet()) {
         CloudComponent cloudComponent = entry.getValue();
         List<IWeightedEntity> recognitionEntities = cloudComponent.getRecognitionEntities();
         Map<ReferedTokenPosition, List<IWeightedEntity>> recEntitesListByRTPMap = NLPUtilities
                  .getRecognitionEntitiesByRTPMap(recognitionEntities);
         for (Entry<ReferedTokenPosition, List<IWeightedEntity>> entry2 : recEntitesListByRTPMap.entrySet()) {
            Map<Long, List<IWeightedEntity>> map = recEntitiesByPart.get(entry2.getKey());
            if (map == null) {
               map = new HashMap<Long, List<IWeightedEntity>>(1);
               map.put(entry.getKey(), entry2.getValue());
               recEntitiesByPart.put(entry2.getKey(), map);
            } else {
               map = recEntitiesByPart.remove(entry2.getKey());
               map.put(entry.getKey(), entry2.getValue());
               moreRecEntitiesByPart.put(entry2.getKey(), map);
            }
         }
      }
   }

   public List<IWeightedEntity> getNonRealizableTypeEntities (Collection<IWeightedEntity> recognitionEntities) {
      List<IWeightedEntity> nonRealizableTypeEntities = new ArrayList<IWeightedEntity>(1);

      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return nonRealizableTypeEntities;
      }
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         TypeEntity typeEntity = (TypeEntity) weightedEntity;
         if (getSwiConfigurationService().getNonRealizableTypeBedIds().contains(typeEntity.getTypeBedId())) {
            nonRealizableTypeEntities.add(weightedEntity);
         }
      }
      return nonRealizableTypeEntities;
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

   /**
    * @return the cloudServiceFactory
    */
   public CloudServiceFactory getCloudServiceFactory () {
      return cloudServiceFactory;
   }

   /**
    * @param cloudServiceFactory
    *           the cloudServiceFactory to set
    */
   public void setCloudServiceFactory (CloudServiceFactory cloudServiceFactory) {
      this.cloudServiceFactory = cloudServiceFactory;
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

   /**
    * @return the locationConfigurationService
    */
   public ILocationConfigurationService getLocationConfigurationService () {
      return locationConfigurationService;
   }

   /**
    * @param locationConfigurationService
    *           the locationConfigurationService to set
    */
   public void setLocationConfigurationService (ILocationConfigurationService locationConfigurationService) {
      this.locationConfigurationService = locationConfigurationService;
   }

   /**
    * @return the applicationRetrievalService
    */
   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   /**
    * @param applicationRetrievalService the applicationRetrievalService to set
    */
   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }
}