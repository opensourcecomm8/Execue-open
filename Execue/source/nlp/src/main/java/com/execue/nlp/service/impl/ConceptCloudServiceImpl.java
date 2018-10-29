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
package com.execue.nlp.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.nlp.InstanceInformation;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.entity.ConceptEntity;
import com.execue.nlp.bean.entity.EntityFactory;
import com.execue.nlp.bean.entity.InstanceEntity;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.nlp.util.NLPUtilities;
import com.execue.swi.exception.KDXException;

/**
 * @author Nihar
 */
public class ConceptCloudServiceImpl extends AbstractMeaningFinderCloudService {

   private final Logger logger = Logger.getLogger(ConceptCloudServiceImpl.class);

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

   private List<RecognitionEntity> getDefaultRecognition (RecognizedCloudEntity recognizedCloudEntity,
            Integer currentIteration) {
      List<INormalizedData> normalizedDataList = getMeaningFinderHelper().getNormalizedCloudData(recognizedCloudEntity);
      List<RecognitionEntity> recEntities = new ArrayList<RecognitionEntity>(1);
      try {
         Set<Long> conceptBedIds = new HashSet<Long>(1);
         conceptBedIds.add(recognizedCloudEntity.getCloudOutputBedId());
         Long outputConceptId = null;
         for (CloudComponent cloudComponent : recognizedCloudEntity.getCloud().getCloudComponentInfoMap().values()) {
            if (cloudComponent.getOutputComponent() == CheckType.YES) {
               List<IWeightedEntity> recognitionEntities = cloudComponent.getRecognitionEntities();

               for (IWeightedEntity weightedEntity : recognitionEntities) {
                  ConceptEntity conceptEntity = (ConceptEntity) weightedEntity;
                  if (conceptEntity.getConceptBedId() == null) {
                     return recEntities;
                  } else if (outputConceptId == null) {
                     outputConceptId = conceptEntity.getConceptBedId();
                     conceptBedIds.add(outputConceptId);
                  } else if (!outputConceptId.equals(conceptEntity.getConceptBedId())) {
                     return recEntities;
                  }
               }
            }
         }
         Map<Long, RIOntoTerm> conceptOntoTermsByConceptBedIds = getNlpServiceHelper().getKdxRetrievalService()
                  .getConceptOntoTermsByConceptBedIds(conceptBedIds);
         if (conceptOntoTermsByConceptBedIds.get(recognizedCloudEntity.getCloudOutputBedId()) != null) {
            recEntities = getRecognitionEntities(normalizedDataList, recognizedCloudEntity,
                     conceptOntoTermsByConceptBedIds.get(recognizedCloudEntity.getCloudOutputBedId()), currentIteration);
         } else if (conceptOntoTermsByConceptBedIds.get(outputConceptId) != null) {
            recEntities = getRecognitionEntities(normalizedDataList, recognizedCloudEntity,
                     conceptOntoTermsByConceptBedIds.get(outputConceptId), currentIteration);
         }
      } catch (KDXException e) {
         throw new NLPSystemException(e.code, e);
      }
      return recEntities;
   }

   private List<RecognitionEntity> getRecognitionEntities (List<INormalizedData> normalizedDataList,
            RecognizedCloudEntity recognizedCloudEntity, RIOntoTerm conceptOntoTerm, Integer currentIteration) {
      List<RecognitionEntity> recEntityList = new ArrayList<RecognitionEntity>(1);

      for (INormalizedData normalizedData : normalizedDataList) {
         List<Integer> finalReferedPositions = new ArrayList<Integer>(1);
         finalReferedPositions.addAll(normalizedData.getReferredTokenPositions());
         int startPosition = finalReferedPositions.get(0);
         int endPosition = finalReferedPositions.get(finalReferedPositions.size() - 1);

         String nlpTag = NLPConstants.NLP_TAG_ONTO_INSTANCE;
         InstanceEntity rec = (InstanceEntity) EntityFactory.getRecognitionEntity(
                  RecognitionEntityType.INSTANCE_ENTITY, nlpTag);

         WeightInformation weightInformation = calculateRecEntityWeight(recognizedCloudEntity);
         rec.setWeightInformation(weightInformation);
         rec.setNormalizedData(normalizedData);
         rec.setWord(normalizedData.getValue());
         rec.setPopularity(conceptOntoTerm.getPopularity());
         rec.setTypeBedId(conceptOntoTerm.getTypeBEDID());
         rec.setTypeDisplayName(conceptOntoTerm.getTypeName());
         rec.setConceptBedId(conceptOntoTerm.getConceptBEDID());
         rec.setConceptDisplayName(conceptOntoTerm.getConceptName());
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

   @Override
   public List<IWeightedEntity> getRecEntitiesToBeProcessed (List<IWeightedEntity> recognitionEntities) {
      return recognitionEntities;
   }
}