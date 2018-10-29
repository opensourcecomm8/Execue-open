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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.AbstractNormalizedData;
import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.nlp.InstanceInformation;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.entity.EntityFactory;
import com.execue.nlp.bean.entity.InstanceEntity;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.util.NLPUtilities;

/**
 * @author John Mallavalli
 */
public class TypeCloudServiceImpl extends AbstractMeaningFinderCloudService {

   private final Logger logger = Logger.getLogger(TypeCloudServiceImpl.class);

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.service.ICloudService#processRecognitionRules(com.execue.nlp.bean.entity.RecognizedCloudEntity,
    *      com.execue.nlp.bean.ProcessorInput)
    */
   public void processRecognitionRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      if (ExecueCoreUtil.isCollectionNotEmpty(cloudEntity.getRecognitionRules())) {

      } else {
         processDefaultRecognitionRules(cloudEntity, processorInput);
      }
   }

   private void processDefaultRecognitionRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      List<IWeightedEntity> outputEntities = processorInput.getOutputRecognitionEntities();
      List<IWeightedEntity> processedEntities = processorInput.getProcessedRecognitions();
      List<IWeightedEntity> defaultedOutputRecognitionEntities = processorInput.getDefaultedOutputRecognitionEntities();
      List<IWeightedEntity> processedRecognitionsWithDefaults = processorInput.getProcessedRecognitionsWithDefaults();
      List<RecognitionEntity> newRecognitionEntities = getDefaultRecognition(cloudEntity, processorInput
               .getCurrentIteration());
      logNormalizedData(newRecognitionEntities);
      if (!CollectionUtils.isEmpty(newRecognitionEntities)) {
         if (!cloudEntity.isDefaultAdded()) {
            outputEntities.addAll(newRecognitionEntities);
            updateCloudMonitorForRecognitionEntities(cloudEntity, processorInput);
            processedEntities.addAll(cloudEntity.getRecognitionEntities());
            filterDefaultRecognition(cloudEntity, defaultedOutputRecognitionEntities);
         } else {
            boolean invalidCloud = false;
            for (IWeightedEntity weightedEntity : cloudEntity.getRecognitionEntities()) {
               if (processorInput.getCloudParticipationMonitor().isCloudProcessedWithDefaultForRecEntity(
                        weightedEntity, newRecognitionEntities.get(0).toString())) {
                  invalidCloud = true;
               }
            }
            if (!invalidCloud) {
               updateCloudMonitorForRecognitionEntitiesWithDefault(cloudEntity, processorInput, newRecognitionEntities
                        .get(0));
               defaultedOutputRecognitionEntities.addAll(newRecognitionEntities);
               processedRecognitionsWithDefaults.addAll(cloudEntity.getRecognitionEntities());
            }
         }
      }
   }

   private void logNormalizedData (List<RecognitionEntity> recongitionEntities) {
      if (logger.isDebugEnabled()) {
         for (RecognitionEntity recongitionEntity : recongitionEntities) {
            INormalizedData normalizedData = recongitionEntity.getNormalizedData();
            logger.debug("Cloud Type: " + normalizedData.getType() + " - " + normalizedData.getTypeBedId()
                     + " NormalizedData: " + normalizedData);
         }
      }
   }

   /**
    * Method to return the list of default recognition entities for the given recognizedCloudEntity
    * 
    * @param recognizedCloudEntity
    * @param currentIteration
    * @return the List<RecognitionEntity>
    */
   private List<RecognitionEntity> getDefaultRecognition (RecognizedCloudEntity recognizedCloudEntity,
            Integer currentIteration) {
      List<INormalizedData> normalizedDataList = getMeaningFinderHelper().getNormalizedCloudData(recognizedCloudEntity);
      List<RecognitionEntity> recEntities = new ArrayList<RecognitionEntity>(1);
      if (!CollectionUtils.isEmpty(normalizedDataList)) {
         AbstractNormalizedData normalizedData = (AbstractNormalizedData) normalizedDataList.get(0);
         if (normalizedData.isDefaultAdded()) {
            recognizedCloudEntity.setDefaultAdded(true);
         }
         recEntities = getRecognitionEntities(normalizedDataList, recognizedCloudEntity, currentIteration);
      }
      return recEntities;
   }

   /**
    * Method to return the list of recognition entities for the given list of INormalizedData
    * 
    * @param normalizedDataList
    * @param recognizedCloudEntity
    * @param currentIteration
    * @return the List<RecognitionEntity>
    */
   private List<RecognitionEntity> getRecognitionEntities (List<INormalizedData> normalizedDataList,
            RecognizedCloudEntity recognizedCloudEntity, Integer currentIteration) {

      List<RecognitionEntity> recEntityList = new ArrayList<RecognitionEntity>(1);

      for (INormalizedData normalizedData : normalizedDataList) {

         TreeSet<Integer> finalReferedPositionSet = new TreeSet<Integer>();
         finalReferedPositionSet.addAll(normalizedData.getReferredTokenPositions());
         Set<IWeightedEntity> allowedRecognitionEntities = recognizedCloudEntity.getAllowedRecognitionEntities();
         if (checkIfAllowedRecognitionsPositionToBeAdded(allowedRecognitionEntities)) {
            finalReferedPositionSet.addAll(NLPUtilities.getReferredTokenPositions(allowedRecognitionEntities));
         }
         List<Integer> finalReferedPositions = new ArrayList<Integer>(1);
         finalReferedPositions.addAll(finalReferedPositionSet);
         int startPosition = finalReferedPositions.get(0);
         int endPosition = finalReferedPositions.get(finalReferedPositions.size() - 1);

         String nlpTag = NLPUtilities.getNLPTag(normalizedData.getNormalizedDataType());
         InstanceEntity rec = (InstanceEntity) EntityFactory.getRecognitionEntity(
                  RecognitionEntityType.INSTANCE_ENTITY, nlpTag);

         WeightInformation weightInformation = calculateRecEntityWeight(recognizedCloudEntity);
         rec.setWeightInformation(weightInformation);
         rec.setNormalizedData(normalizedData);
         rec.setWord(normalizedData.getValue());
         rec.setTypeBedId(normalizedData.getTypeBedId());
         rec.setTypeDisplayName(normalizedData.getType());
         InstanceInformation information = new InstanceInformation();
         information.setInstanceValue(normalizedData.getValue());
         information.setInstanceDisplayName(normalizedData.getDisplayValue());
         rec.addInstanceInformation(information);
         rec.setReferedTokenPositions(finalReferedPositions);
         rec.addOriginalReferedTokenPositions(normalizedData.getOriginalReferredTokenPositions());
         rec.setStartPosition(startPosition);
         rec.setEndPosition(endPosition);
         // TODO: NK: Need to revisit the logic to set the position??
         // Currently setting the start position as position from the rtp of normalized data
         rec.setPosition(startPosition);
         rec.setIteration(currentIteration + 1);
         if (normalizedData.getNormalizedDataType() == NormalizedDataType.RANGE_NORMALIZED_DATA) {
            rec.addFlag(ExecueConstants.RANGE);
         }
         if (normalizedData.getNormalizedDataType() == NormalizedDataType.COMPARATIVE_INFO_NORMALIZED_DATA) {
            ((OntoEntity) rec).setModelGroupId(1L);
         }
         rec.setHitsUpdateInfo(NLPUtilities.getHitsUpdateInfo(recognizedCloudEntity.getRecognitionEntities()));
         recEntityList.add(rec);
      }
      return recEntityList;
   }

   /**
    * This method checks if any of the recognitions is from the type cloud i.e. normalized data is present, 
    * then returns false else returns true 
    * 
    * @param allowedRecognitionEntities
    * @return true/false
    */
   private boolean checkIfAllowedRecognitionsPositionToBeAdded (Collection<IWeightedEntity> allowedRecognitionEntities) {
      if (CollectionUtils.isEmpty(allowedRecognitionEntities)) {
         return false;
      }
      for (IWeightedEntity weightedEntity : allowedRecognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         //Check if any of entity has the normalized data, then it means it is recognized via type cloud, hence return
         //false as this cannot be considered as the positions of new recognition 
         if (recEntity.getNormalizedData() != null) {
            return false;
         } else if (recEntity.getNLPtag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_TYPE_REGEX_INSTANCE)) {
            // regex type instance cannot be considered
            return false;
         } else if (recEntity.getNLPtag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_TYPE_INSTANCE)) {
            // type instance cannot be considered
            return false;
         }
      }
      return true;
   }

   @Override
   public List<IWeightedEntity> getRecEntitiesToBeProcessed (List<IWeightedEntity> recognitionEntities) {
      List<IWeightedEntity> entitiesToProcess = new ArrayList<IWeightedEntity>(1);
      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         if (recEntity.getNLPtag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_REALIZED_TYPE)
                  || recEntity.getNLPtag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_REALIZED_TYPE_INSTANCE)
                  || recEntity.getNLPtag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_TYPE)
                  || recEntity.getNLPtag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_TYPE_INSTANCE)
                  || recEntity.getNLPtag().equalsIgnoreCase(NLPConstants.NLP_TAG_ONTO_TYPE_REGEX_INSTANCE)
                  || ((OntoEntity) recEntity).getModelGroupId() != null
                  && ((OntoEntity) recEntity).getModelGroupId().equals(1L)) {

            entitiesToProcess.add(recEntity);
         }
      }
      return entitiesToProcess;
   }
}