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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.type.CloudCategory;
import com.execue.core.common.type.CloudOutput;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.bean.entity.TypeEntity;
import com.execue.nlp.bean.matrix.CloudParticipationMonitor;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.nlp.processor.AbstractCloudProcessor;
import com.execue.swi.exception.KDXException;

/**
 * @author Nihar
 */
public class MeaningFinder extends AbstractCloudProcessor {

   @Override
   public void groupOccurrencesOfSimilarType (ProcessorInput input) {
      // TODO: NK: Need to revisit this grouping logic for both meaning enhancer and meaning finder
      Map<Long, List<RecognitionEntity>> recEntitiesByIdMap = input.getRecEntitiesByIdMap();
      populateRecEntitiesById(recEntitiesByIdMap, input.getRecognitionEntities());
   }

   /**
    * This method populates the recEntitiesByIdMap for the given list of recognitionEntities. Here the key is type bed
    * id and values are list of type instance
    * 
    * @param recEntitiesByIdMap
    * @param recognitionEntities
    */
   private void populateRecEntitiesById (Map<Long, List<RecognitionEntity>> recEntitiesByIdMap,
            List<IWeightedEntity> recognitionEntities) {
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return;
      }
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         // In Meaning Finder, we should not consider even the concept entities
         if (recEntity.getEntityType() == RecognitionEntityType.PROPERTY_ENTITY
                  || recEntity.getEntityType() == RecognitionEntityType.CONCEPT_ENTITY) {
            continue;
         }
         // Process only type instance
         if (recEntity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY
                  && (recEntity.getNLPtag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_CONCEPT_INSTANCE) || recEntity
                           .getNLPtag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_INSTANCE))) {
            continue;
         }
         TypeEntity entity = (TypeEntity) recEntity;
         Long key = entity.getTypeBedId();
         List<RecognitionEntity> recEntityList = recEntitiesByIdMap.get(key);
         if (ExecueCoreUtil.isCollectionEmpty(recEntityList)) {
            recEntityList = new ArrayList<RecognitionEntity>();
            recEntitiesByIdMap.put(key, recEntityList);
         }
         recEntityList.add(recEntity);
      }
   }

   @Override
   public List<RecognizedCloudEntity> getClouds (ProcessorInput processorInput) {
      List<IWeightedEntity> recognitionEntities = processorInput.getRecognitionEntities();
      if (!CollectionUtils.isEmpty(processorInput.getUnrecognizedBaseRecEntities())) {
         recognitionEntities.addAll(processorInput.getUnrecognizedBaseRecEntities());
      }
      List<RecognizedCloudEntity> cloudEntities = new ArrayList<RecognizedCloudEntity>(1);
      // Guard condition to return if the input list is empty
      if (CollectionUtils.isEmpty(recognitionEntities)) {
         return cloudEntities;
      }
      Set<Long> componentBedIds = getNlpServiceHelper().getComponentBedIdsFromRecEntities(recognitionEntities);
      try {
         Map<CloudCategory, List<RICloud>> riCloudsMap = new HashMap<CloudCategory, List<RICloud>>();
         riCloudsMap = getKdxCloudRetrievalService().getRICloudsByCompBedIdsAndCloudOutput(componentBedIds,
                  CloudOutput.NEW_VALUE);

         List<CloudCategory> cloudCategories = new ArrayList<CloudCategory>();
         cloudCategories.add(CloudCategory.TYPE_CLOUD);
         cloudCategories.add(CloudCategory.CONCEPT_CLOUD);
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

   @Override
   public boolean processValidationRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      // Validate the cloudEntity by checking if its already got processed. If yes cloud
      // will be invalidated.
      CloudParticipationMonitor cloudParticipationMonitor = processorInput.getCloudParticipationMonitor();
      if (cloudParticipationMonitor != null) {
         boolean isCloudAlreadyProcessed = cloudParticipationMonitor.isCloudAlreadyProcessed(cloudEntity);
         if (isCloudAlreadyProcessed) {
            return false;
         }
      }
      boolean isValid = super.processValidationRules(cloudEntity, processorInput);
      if (!isValid) {
         return false;
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

   }

   @Override
   public void processWeightAssignmentRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      getNlpServiceHelper().getCloudService(cloudEntity.getCategory()).processWeightAssignmentRules(cloudEntity,
               processorInput);
   }
}
