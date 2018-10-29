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
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.bean.entity.ReferedTokenPosition;
import com.execue.nlp.bean.entity.TypeEntity;
import com.execue.nlp.bean.matrix.CloudParticipationMonitor;
import com.execue.nlp.helper.NLPServiceHelper;
import com.execue.nlp.rule.service.impl.CloudRuleExecutor;
import com.execue.nlp.service.ICloudService;
import com.execue.nlp.util.NLPUtilities;
import com.execue.swi.exception.KDXException;

public abstract class AbstractMeaningFinderCloudService implements ICloudService {

   protected CloudRuleExecutor cloudRuleExecutor;
   protected NLPServiceHelper  nlpServiceHelper;
   private MeaningFinderHelper meaningFinderHelper;

   public abstract List<IWeightedEntity> getRecEntitiesToBeProcessed (List<IWeightedEntity> recognitionEntities);

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.service.ICloudService#getCloudEntities(java.util.List, java.util.List)
    */
   public List<RecognizedCloudEntity> getCloudEntities (List<RICloud> riClouds,
            List<IWeightedEntity> recognitionEntities, ProcessorInput processorInput) throws KDXException {

      List<IWeightedEntity> recEntitiesToBeProcessed = getRecEntitiesToBeProcessed(recognitionEntities);

      // Get the recognized cloud entities, for each cloud, having the recognition entities matching the cloud components 
      List<RecognizedCloudEntity> recCloudEntities = getNlpServiceHelper().getRecognizedCloudEntities(riClouds,
               recEntitiesToBeProcessed, processorInput);

      if (CollectionUtils.isEmpty(recCloudEntities)) {
         return recCloudEntities;
      }

      // Split the cloud entity if we have multiple recognitions on the same token positions
      List<RecognizedCloudEntity> recCloudEntitiesAfterDuplicateRefTokenPositionSplit = getNlpServiceHelper()
               .splitBasedOnDuplicateRefTokenPositions(recCloudEntities);

      // Split the cloud entity if the user query frequency for recognized entity is more than the specified
      // frequency in the DB
      List<RecognizedCloudEntity> recCloudEntitiesAfterFrequencySplit = getNlpServiceHelper().splitBasedOnFrequency(
               recCloudEntitiesAfterDuplicateRefTokenPositionSplit);

      // Filter the cloud based on the invalid components 
      List<RecognizedCloudEntity> recCloudEntitiesAfterFilterCloud = getNlpServiceHelper().filterInvalidClouds(
               recCloudEntitiesAfterFrequencySplit, recEntitiesToBeProcessed);

      // Finally, Split the cloud entity based on the continuity
      List<RecognizedCloudEntity> recCloudEntitiesAfterContinuitySplit = getNlpServiceHelper().splitBasedOnContinuity(
               recCloudEntitiesAfterFilterCloud);

      return recCloudEntitiesAfterContinuitySplit;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.service.ICloudService#processValidationRules(com.execue.nlp.bean.entity.RecognizedCloudEntity,
    *      com.execue.nlp.bean.ProcessorInput)
    */
   public boolean processValidationRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      // Sort entities in cloud by Referred Token Position
      NLPUtilities.sortRecognitionEntitiesByPosition(cloudEntity.getRecognitionEntities());

      boolean isValid = true;
      if (ExecueCoreUtil.isCollectionNotEmpty(cloudEntity.getValidationRules())) {
         isValid = getCloudRuleExecutor().processValidationRule(cloudEntity, cloudEntity.getValidationRules());
      }
      return isValid;
   }

   /**
    * @param cloudEntity
    * @param defaultedOutputRecognitionEntities
    */
   public void filterDefaultRecognition (RecognizedCloudEntity cloudEntity,
            List<IWeightedEntity> defaultedOutputRecognitionEntities) {
      if (CollectionUtils.isEmpty(defaultedOutputRecognitionEntities)) {
         return;
      }
      ReferedTokenPosition referedTokenPosition = new ReferedTokenPosition();
      for (IWeightedEntity weightedEntity : cloudEntity.getRecognitionEntities()) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         referedTokenPosition.addAll(recEntity.getReferedTokenPositions());
      }
      List<IWeightedEntity> entitiesToRemove = new ArrayList<IWeightedEntity>(1);
      for (IWeightedEntity weightedEntity : defaultedOutputRecognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         ReferedTokenPosition rtp = new ReferedTokenPosition(recEntity.getReferedTokenPositions());
         if (rtp.isSubset(referedTokenPosition)
                  && ((TypeEntity) recEntity).getTypeBedId().equals(cloudEntity.getCloudOutputBedId())) {
            entitiesToRemove.add(recEntity);
         }
      }
      defaultedOutputRecognitionEntities.removeAll(entitiesToRemove);

   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.service.ICloudService#processWeightAssignmentRules(com.execue.nlp.bean.entity.RecognizedCloudEntity,
    *      com.execue.nlp.bean.ProcessorInput)
    */
   public void processWeightAssignmentRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      return; // do nothing
   }

   /**
    * This method calculates the weight of the recognition entity based on the RTP present in the normalizedData
    * 
    * @param recognizedCloudEntity
    * @return the WeightInformation
    */
   public WeightInformation calculateRecEntityWeight (RecognizedCloudEntity recognizedCloudEntity) {
      double recQuality = 0.0;
      double recWeight = 0.0;
      for (Entry<Long, CloudComponent> entry : recognizedCloudEntity.getCloudComponentInfoMap().entrySet()) {
         CloudComponent cloudComponent = entry.getValue();
         for (IWeightedEntity weightedEntity : cloudComponent.getRecognitionEntities()) {
            RecognitionEntity recognitionEntity = (RecognitionEntity) weightedEntity;
            // If frequency is not defined for components i.e less than or equal to zero or cloud is location 
            // then we calculate the quality as per the components found in the recognizedCloudEntity
            // and hence not consider the frequency and importance of the component
            if (NLPConstants.LOCATION_CLOUD.equalsIgnoreCase(recognizedCloudEntity.getCloud().getName())) {
               recQuality = (recQuality == 0.0 ? 1 : recQuality) * recognitionEntity.getRecognitionQuality();
            } else {
               recQuality = recQuality + recognitionEntity.getRecognitionQuality() * cloudComponent.getImportance()
                        / (cloudComponent.getFrequency() * 100);
            }
            recWeight = recWeight + recognitionEntity.getRecognitionWeight();
         }
      }
      WeightInformation weightInformation = new WeightInformation();
      weightInformation.setRecognitionQuality(recQuality);
      weightInformation.setRecognitionWeight(recWeight);
      return weightInformation;
   }

   protected void updateCloudMonitorForRecognitionEntities (RecognizedCloudEntity cloudEntity,
            ProcessorInput processorInput) {
      CloudParticipationMonitor cloudParticipationMonitor = processorInput.getCloudParticipationMonitor();
      cloudParticipationMonitor.addCloudProcessed(cloudEntity);

      // TODO: NK: Can  get rid of below logic in future, as we are now not using it anywhere
      for (IWeightedEntity weightedEntity : cloudEntity.getRecognitionEntities()) {
         cloudParticipationMonitor.addCloudAsPartcipatedForRecEntity(weightedEntity, cloudEntity.getCloud().getId()
                  + "" + NLPUtilities.getReferredTokenPositions(cloudEntity.getRecognitionEntities()));
      }

   }

   protected void updateCloudMonitorForRecognitionEntitiesWithDefault (RecognizedCloudEntity cloudEntity,
            ProcessorInput processorInput, RecognitionEntity recognitionEntity) {
      CloudParticipationMonitor cloudParticipationMonitor = processorInput.getCloudParticipationMonitor();
      for (IWeightedEntity weightedEntity : cloudEntity.getRecognitionEntities()) {
         cloudParticipationMonitor.addCloudAsPartcipatedWithDefaultForRecEntity(weightedEntity, recognitionEntity
                  .toString());
      }

   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.service.ICloudService#ifCloudShouldBeProcessed(com.execue.nlp.bean.entity.RecognizedCloudEntity)
    */
   public boolean ifCloudShouldBeProcessed (RecognizedCloudEntity cloudEntity) {
      return true;
   }

   /**
    * @return the cloudRuleExecutor
    */
   public CloudRuleExecutor getCloudRuleExecutor () {
      return cloudRuleExecutor;
   }

   /**
    * @param cloudRuleExecutor
    *           the cloudRuleExecutor to set
    */
   public void setCloudRuleExecutor (CloudRuleExecutor cloudRuleExecutor) {
      this.cloudRuleExecutor = cloudRuleExecutor;
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
    * @return the meaningFinderHelper
    */
   public MeaningFinderHelper getMeaningFinderHelper () {
      return meaningFinderHelper;
   }

   /**
    * @param meaningFinderHelper
    *           the meaningFinderHelper to set
    */
   public void setMeaningFinderHelper (MeaningFinderHelper meaningFinderHelper) {
      this.meaningFinderHelper = meaningFinderHelper;
   }
}