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
package com.execue.nlp.processor.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.bean.nlp.RecognizedType;
import com.execue.core.common.type.CloudCategory;
import com.execue.core.common.type.CloudOutput;
import com.execue.core.common.type.SearchFilterType;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.association.AssociationPath;
import com.execue.nlp.bean.entity.AppCloudEntity;
import com.execue.nlp.bean.entity.InstanceEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.bean.entity.ReferedTokenPosition;
import com.execue.nlp.bean.entity.TypeEntity;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.nlp.processor.AbstractCloudProcessor;
import com.execue.nlp.processor.IAssociationService;
import com.execue.nlp.util.NLPUtilities;
import com.execue.ontology.exceptions.OntologyException;
import com.execue.swi.exception.KDXException;

/**
 * @author Nihar
 */
public class MeaningEnhancer extends AbstractCloudProcessor {

   private final Logger        logger = Logger.getLogger(MeaningEnhancer.class);
   private IAssociationService associationService;

   /**
    * Method to get the List Of AppCloud Entities based on the passed recognition entities.
    * 
    * @param processorInput
    *           processor input
    * @return the List<RecognizedCloudEntity>
    */
   @Override
   public List<RecognizedCloudEntity> getClouds (ProcessorInput processorInput) {
      try {
         List<RecognizedCloudEntity> cloudEntities = new ArrayList<RecognizedCloudEntity>(1);
         List<IWeightedEntity> recognitionEntities = processorInput.getRecognitionEntities();
         // Guard condition to return if the input list is empty
         if (CollectionUtils.isEmpty(recognitionEntities)) {
            return cloudEntities;
         }

         Set<Long> componentBedIds = getNlpServiceHelper().getComponentBedIdsFromRecEntities(recognitionEntities);
         if (CollectionUtils.isEmpty(componentBedIds)) {
            return cloudEntities;
         }

         SearchFilter searchFilter = processorInput.getSearchFilter();
         Map<CloudCategory, List<RICloud>> riCloudsMap = new HashMap<CloudCategory, List<RICloud>>();
         if (searchFilter.getSearchFilterType() == SearchFilterType.APP) {
            Long appId = searchFilter.getId();
            Cloud defaultAppCloudByAppId = getKdxCloudRetrievalService().getDefaultAppCloudByAppId(appId);
            riCloudsMap = getKdxCloudRetrievalService().getRICloudsByCompBedIdsAndCloudOutput(componentBedIds,
                     CloudOutput.ENHANCED, defaultAppCloudByAppId.getId());
         } else {
            // TODO - NK/GA - Can restrict to the model group ids in the rec entities
            riCloudsMap = getKdxCloudRetrievalService().getRICloudsByCompBedIdsAndCloudOutput(componentBedIds,
                     CloudOutput.ENHANCED);
         }

         // TODO: -JM- externalize the cloud categories
         // Iteratively get the cloud entities by the cloud category
         List<CloudCategory> cloudCategories = new ArrayList<CloudCategory>();
         cloudCategories.add(CloudCategory.FRAMEWORK_CLOUD);
         cloudCategories.add(CloudCategory.APP_CLOUD);
         for (CloudCategory cloudCategory : cloudCategories) {
            List<RICloud> riClouds = riCloudsMap.get(cloudCategory);
            List<RecognizedCloudEntity> cloudCategoryEntities = getNlpServiceHelper().getCloudService(cloudCategory)
                     .getCloudEntities(riClouds, recognitionEntities, processorInput);
            cloudEntities.addAll(cloudCategoryEntities);
         }
         return cloudEntities;
      } catch (KDXException kdxException) {
         throw new NLPSystemException(kdxException.getCode(), kdxException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.processor.AbstractCloudProcessor#processCloud(com.execue.nlp.bean.entity.RecognizedCloudEntity,
    *      com.execue.nlp.bean.ProcessorInput)
    */
   @Override
   public void processCloud (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput)
            throws NLPSystemException {
      super.processCloud(cloudEntity, processorInput);
      if (ifCloudShouldBeProcessed(cloudEntity) && cloudEntity.getCategory() == CloudCategory.APP_CLOUD) {
         filterUnAssociatedTFEntities(cloudEntity);
      }
   }

   @Override
   public boolean processValidationRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {

      // NOTE: For framework cloud, we do not have any validation rules defined as of now, once we have it
      if (cloudEntity.getCategory() == CloudCategory.FRAMEWORK_CLOUD) {
         boolean isValid = super.processValidationRules(cloudEntity, processorInput);
         if (!isValid) {
            return false;
         }
      }
      return getNlpServiceHelper().getCloudService(cloudEntity.getCategory()).processValidationRules(cloudEntity,
               processorInput);
   }

   @Override
   public void processRecognitionRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      getNlpServiceHelper().getCloudService(cloudEntity.getCategory()).processRecognitionRules(cloudEntity,
               processorInput);
   }

   @Override
   public void retrieveAssociations (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      try {
         if (cloudEntity.getCategory().equals(CloudCategory.APP_CLOUD)) {
            List<RecognizedCloudEntity> appCloudEntities = processorInput.getAppCloudEntities();
            appCloudEntities.add(cloudEntity);
            logAppsInformation(appCloudEntities);
            // Sort the recognition entities by position
            NLPUtilities.sortRecognitionEntitiesByRTP(cloudEntity.getRecognitionEntities());
            // Perform Association on cloud.
            populateAssociations(cloudEntity);
         }
      } catch (OntologyException e) {
         throw new NLPSystemException(e.code, e);
      }
   }

   /**
    * @param appCloudEntity
    *           App cloud Entity
    * @throws OntologyException
    */
   // 1. For Each App
   // 1.1. Get Associations for all Entities, no association is allowed between Recognition with Same or
   // Overlapping Referred Token Positions
   // 1.2. Set the List<Path-Definition> in App-Cloud-Entity
   private void populateAssociations (RecognizedCloudEntity appCloudEntity) throws OntologyException {
      List<AssociationPath> pathDefinitions = getAssociationService().getPathDefinitions(
               appCloudEntity.getRecognitionEntities());
      appCloudEntity.setPaths(pathDefinitions);
   }

   @Override
   public void processWeightAssignmentRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      getNlpServiceHelper().getCloudService(cloudEntity.getCategory()).processWeightAssignmentRules(cloudEntity,
               processorInput);
   }

   private void logAppsInformation (List<RecognizedCloudEntity> appCloudEntities) {
      if (logger.isDebugEnabled()) {
         StringBuilder sb = new StringBuilder();
         for (RecognizedCloudEntity appCloudEntity : appCloudEntities) {
            sb.append("\nFor Model id ").append(appCloudEntity.getModelGroupId()).append(" Rec Entities are : ");
            for (IWeightedEntity recEntity : appCloudEntity.getRecognitionEntities()) {
               sb.append(recEntity).append("\t");
            }
         }
         logger.debug(sb.toString());
      }
   }

   @Override
   public void getDefaultConceptForUnassociatedValue (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput)
            throws OntologyException, KDXException {
      if (cloudEntity.getCategory() != CloudCategory.APP_CLOUD) {
         return;
      }
      filterUnAssociatedValueIfAssociatedExists(cloudEntity);
      Set<Long> beIds = new HashSet<Long>(1);
      List<InstanceEntity> unAssociatedAttributes = new ArrayList<InstanceEntity>(1);
      Map<IWeightedEntity, List<IWeightedEntity>> associatedValueRealizationBySource = new HashMap<IWeightedEntity, List<IWeightedEntity>>(
               1);
      AppCloudEntity appCloudEntity = (AppCloudEntity) cloudEntity;
      for (IWeightedEntity weightedEntity : cloudEntity.getRecognitionEntities()) {
         TypeEntity typeEntity = (TypeEntity) weightedEntity;
         if (typeEntity.getTypeDisplayName().equals(RecognizedType.VALUE_TYPE.getValue())
                  && typeEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY) {
            boolean isAssociated = false;
            for (AssociationPath association : appCloudEntity.getPaths()) {
               IWeightedEntity destination = association.getDestination();
               if (destination.equals(typeEntity)) {
                  List<IWeightedEntity> associatedEntities = associatedValueRealizationBySource.get(association
                           .getSource());
                  if (associatedEntities == null) {
                     associatedEntities = new ArrayList<IWeightedEntity>(1);
                     associatedValueRealizationBySource.put(association.getSource(), associatedEntities);
                     associatedEntities.add(destination);
                  } else {
                     InstanceEntity destInstanceEntity = (InstanceEntity) destination;
                     boolean isDifferentValueRealization = true;
                     for (IWeightedEntity weightedEntity2 : associatedEntities) {
                        InstanceEntity exInstanceEntity = (InstanceEntity) weightedEntity2;
                        if (exInstanceEntity.getConceptBedId().equals(destInstanceEntity.getConceptBedId())) {
                           isDifferentValueRealization = false;
                           break;
                        }
                     }
                     if (isDifferentValueRealization) {
                        associatedEntities.add(destination);
                     }
                  }
                  isAssociated = true;
                  break;
               }
            }
            if (!isAssociated) {
               InstanceEntity instanceEntity = (InstanceEntity) typeEntity;
               if (instanceEntity.getConceptBedId() != null) {
                  beIds.add(instanceEntity.getConceptBedId());
                  unAssociatedAttributes.add(instanceEntity);
               }
            }
         }
      }
      for (Entry<IWeightedEntity, List<IWeightedEntity>> entry : associatedValueRealizationBySource.entrySet()) {
         // More then one different value realization is associated with the same source, 
         // so need to get the default path for value realization.
         if (entry.getValue().size() > 1) {
            for (IWeightedEntity weightedEntity : entry.getValue()) {
               InstanceEntity instanceEntity = (InstanceEntity) weightedEntity;
               beIds.add(instanceEntity.getConceptBedId());
               unAssociatedAttributes.add(instanceEntity);
            }
         }
      }
      if (!CollectionUtils.isEmpty(beIds)) {
         getAssociationService().populateDefaultPaths(appCloudEntity, processorInput, unAssociatedAttributes, beIds);
      }
   }

   /**
    * Method to filter the unassociated value entries if for same referred token Position an associated realization
    * exists,.
    * 
    * @param cloudEntity
    */
   private void filterUnAssociatedValueIfAssociatedExists (RecognizedCloudEntity cloudEntity) {
      // Maps to have the associated and unassociated attributes by ReferedTokenPositions.
      Map<ReferedTokenPosition, List<InstanceEntity>> unAssociatedAttributesByRTP = new HashMap<ReferedTokenPosition, List<InstanceEntity>>(
               1);
      Map<ReferedTokenPosition, List<InstanceEntity>> associatedAttributesByRTP = new HashMap<ReferedTokenPosition, List<InstanceEntity>>(
               1);
      AppCloudEntity appCloudEntity = (AppCloudEntity) cloudEntity;
      for (IWeightedEntity weightedEntity : cloudEntity.getRecognitionEntities()) {
         TypeEntity typeEntity = (TypeEntity) weightedEntity;
         if (typeEntity.getTypeDisplayName().equals(RecognizedType.VALUE_TYPE.getValue())
                  && typeEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY) {
            boolean isAssociated = false;
            for (AssociationPath association : appCloudEntity.getPaths()) {
               IWeightedEntity destination = association.getDestination();
               if (destination.equals(typeEntity)) {
                  isAssociated = true;
                  break;
               }
            }
            if (!isAssociated) {
               ReferedTokenPosition rtp = new ReferedTokenPosition(typeEntity.getReferedTokenPositions());
               List<InstanceEntity> attributes = unAssociatedAttributesByRTP.get(rtp);
               if (attributes == null) {
                  attributes = new ArrayList<InstanceEntity>(1);
                  unAssociatedAttributesByRTP.put(rtp, attributes);
               }
               attributes.add((InstanceEntity) typeEntity);
            } else {
               ReferedTokenPosition rtp = new ReferedTokenPosition(typeEntity.getReferedTokenPositions());
               List<InstanceEntity> attributes = associatedAttributesByRTP.get(rtp);
               if (attributes == null) {
                  attributes = new ArrayList<InstanceEntity>(1);
                  associatedAttributesByRTP.put(rtp, attributes);
               }
               attributes.add((InstanceEntity) typeEntity);
            }
         }
      }
      // iterate through the list of associated attributes and see if for the same position set un-associated
      // attributes
      // exists we don't need to get default concept for those un-associated attrs.
      for (Entry<ReferedTokenPosition, List<InstanceEntity>> entry : associatedAttributesByRTP.entrySet()) {
         List<InstanceEntity> attList = unAssociatedAttributesByRTP.get(entry.getKey());
         if (!CollectionUtils.isEmpty(attList)) {
            cloudEntity.getRecognitionEntities().removeAll(attList);
         }
      }

   }

   private void filterUnAssociatedTFEntities (RecognizedCloudEntity cloudEntity) {
      AppCloudEntity appCloudEntity = (AppCloudEntity) cloudEntity;
      Map<ReferedTokenPosition, List<IWeightedEntity>> recEntitesListByRTPMap = NLPUtilities
               .getRecognitionEntitiesByRTPMap(appCloudEntity.getRecognitionEntities());
      for (Entry<String, List<IWeightedEntity>> entry : appCloudEntity.getEntityListByTFposId().entrySet()) {
         boolean isAnyEntryAssociated = false;
         List<IWeightedEntity> unassociatedEntities = new ArrayList<IWeightedEntity>(1);
         for (IWeightedEntity weightedEntity : entry.getValue()) {
            boolean entityAssociated = false;
            for (AssociationPath association : appCloudEntity.getPaths()) {
               if (association.getSource().equals(weightedEntity)
                        || association.getDestination().equals(weightedEntity)) {
                  boolean checkIfAssociationIsValid = checkIfAssociationIsValid(recEntitesListByRTPMap, association
                           .getUnAllowedRecognitions());
                  entityAssociated = checkIfAssociationIsValid;
               }
            }
            if (!entityAssociated) {
               unassociatedEntities.add(weightedEntity);
            } else {
               isAnyEntryAssociated = true;
            }
         }
         if (isAnyEntryAssociated && !CollectionUtils.isEmpty(unassociatedEntities)) {
            cloudEntity.getRecognitionEntities().removeAll(unassociatedEntities);
         }
      }
   }

   /**
    * Method to check if an association is valid . if for any position number of recognition is equal to the unallowed
    * comps at that position that means the path is not valid.
    * 
    * @param recEntitesListByRTPMap
    * @param unAllowedRecognitions
    * @return
    */
   private boolean checkIfAssociationIsValid (Map<ReferedTokenPosition, List<IWeightedEntity>> recEntitesListByRTPMap,
            List<IWeightedEntity> unAllowedRecognitions) {
      if (CollectionUtils.isEmpty(unAllowedRecognitions)) {
         return true;
      }
      Map<ReferedTokenPosition, List<IWeightedEntity>> unAllowedRecognitionByRTPMap = NLPUtilities
               .getRecognitionEntitiesByRTPMap(unAllowedRecognitions);
      for (Entry<ReferedTokenPosition, List<IWeightedEntity>> entry : unAllowedRecognitionByRTPMap.entrySet()) {
         List<IWeightedEntity> entities = recEntitesListByRTPMap.get(entry.getKey());
         if (!CollectionUtils.isEmpty(entities) && !CollectionUtils.isEmpty(entry.getValue())
                  && entities.size() == entry.getValue().size()) {
            return false;
         }
      }
      return true;
   }

   /**
    * @return the associationService
    */
   public IAssociationService getAssociationService () {
      return associationService;
   }

   /**
    * @param associationService
    *           the associationService to set
    */
   public void setAssociationService (IAssociationService associationService) {
      this.associationService = associationService;
   }
}