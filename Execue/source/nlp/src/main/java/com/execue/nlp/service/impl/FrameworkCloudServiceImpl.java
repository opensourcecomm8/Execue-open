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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.helper.NLPServiceHelper;
import com.execue.nlp.rule.service.impl.CloudRuleExecutor;
import com.execue.nlp.service.ICloudService;
import com.execue.nlp.util.NLPUtilities;
import com.execue.swi.exception.KDXException;

/**
 * @author John Mallavalli
 */
public class FrameworkCloudServiceImpl implements ICloudService {

   private final Logger      logger                    = Logger.getLogger(TypeCloudServiceImpl.class);
   private CloudRuleExecutor cloudRuleExecutor;
   private NLPServiceHelper  nlpServiceHelper;

   // TODO: -JM- externalize the values
   private static final Long COMP_STATISTICS_TYPE      = 110L;
   private static final Long NUMBER_TYPE               = 401L;
   private static final Long ENUMERATION_BEHAVIOR_TYPE = 9004L;
   private static final Long MEASURABLE_ENTITY_TYPE    = 101L;
   private static final Long COMPARATIVE_BEHAVIOR_TYPE = 9006L;

   public List<RecognizedCloudEntity> getCloudEntities (List<RICloud> riClouds,
            List<IWeightedEntity> recognitionEntities, ProcessorInput processorInput) throws KDXException {
      List<RecognizedCloudEntity> frameWorkCloudEntities = new ArrayList<RecognizedCloudEntity>(1);
      if (CollectionUtils.isEmpty(riClouds)) {
         return frameWorkCloudEntities;
      }
      // Group entities by model group and create frame work cloud entities separately for each model.
      Map<Long, List<IWeightedEntity>> entitiesByModelGroup = NLPUtilities
               .groupRecEntitiesByModelAndBase(recognitionEntities);
      for (Entry<Long, List<IWeightedEntity>> entry : entitiesByModelGroup.entrySet()) {
         List<RecognizedCloudEntity> recognizedCloudEntities = getNlpServiceHelper().getRecognizedCloudEntities(
                  riClouds, entry.getValue(), processorInput);
         if (!CollectionUtils.isEmpty(recognizedCloudEntities)) {
            frameWorkCloudEntities.addAll(recognizedCloudEntities);
         }
      }
      frameWorkCloudEntities = getNlpServiceHelper().splitBasedOnDuplicateRefTokenPositions(frameWorkCloudEntities);
      List<RecognizedCloudEntity> splitBasedOnFrequency = getNlpServiceHelper().splitBasedOnFrequency(
               frameWorkCloudEntities);
      List<RecognizedCloudEntity> recCloudEntitiesAfterFilterCloud = getNlpServiceHelper().filterInvalidClouds(
               splitBasedOnFrequency, recognitionEntities);
      return recCloudEntitiesAfterFilterCloud;
   }

   public boolean processValidationRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      NLPUtilities.sortRecognitionEntitiesByPosition(cloudEntity.getRecognitionEntities());
      boolean isValid = checkIfCloudIsValid(cloudEntity, processorInput);
      if (isValid && ExecueCoreUtil.isCollectionNotEmpty(cloudEntity.getValidationRules())) {
         isValid = getCloudRuleExecutor().processValidationRule(cloudEntity, cloudEntity.getValidationRules());
      }
      return isValid;
   }

   public void processRecognitionRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      String cloudName = cloudEntity.getCloud().getName();
      if (cloudName.equalsIgnoreCase(NLPConstants.GROUP_BY_CLOUD)
               || cloudName.equalsIgnoreCase(NLPConstants.GROUP_BY1_CLOUD)) {
         performGroupBy(cloudEntity, processorInput);
      } else if (cloudName.equalsIgnoreCase(NLPConstants.TOP_BOTTOM_CLOUD)) {
         performTopBottom(cloudEntity, processorInput);
      }
      logRecognizedEntities(cloudEntity);
   }

   private void logRecognizedEntities (RecognizedCloudEntity recognizedCloudEntity) {
      if (logger.isDebugEnabled()) {
         StringBuilder cloudInfo = new StringBuilder();
         cloudInfo.append("Recognized FrameWork Cloud Entity Info: ");
         cloudInfo.append("\n").append(recognizedCloudEntity);
         logger.debug(cloudInfo);
      }
   }

   public boolean ifCloudShouldBeProcessed (RecognizedCloudEntity cloudEntity) {
      // If all recognition entities that are part of this cloud are part of some app cloud then proceed
      Long modelGroupId = null;
      for (IWeightedEntity entity : cloudEntity.getRecognitionEntities()) {
         OntoEntity ontoEntity = (OntoEntity) entity;
         Long entityModelId = ontoEntity.getModelGroupId();
         if (modelGroupId == null) {
            if (entityModelId != null && !entityModelId.equals(1L)) {
               modelGroupId = entityModelId;
            }
         } else {
            if (entityModelId != null && !entityModelId.equals(1L) && !modelGroupId.equals(entityModelId)) {
               return false;
            }
         }

         if (ontoEntity.getModelGroupId() == null) {
            return false;
         }
      }
      return true;
   }

   /**
    * This method checks if the in between entities are allowed or has the allowed behavior
    * 
    * @param cloudEntity
    * @param processorInput
    * @return boolean true/false
    */
   // TODO: NK: Also need to check the required behavior of component
   private boolean checkIfCloudIsValid (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      boolean isValid = getNlpServiceHelper().checkIfInBetweenRecEntitiesAllowed(cloudEntity,
               processorInput.getRecognitionEntities(), new TreeSet<Integer>());
      return isValid;
   }

   private void updateFlag (List<IWeightedEntity> recognitionEntities, String flagName) {
      for (IWeightedEntity entity : recognitionEntities) {
         RecognitionEntity recognitionEntity = (RecognitionEntity) entity;
         recognitionEntity.addFlag(flagName);
      }
   }

   private void performGroupBy (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {

      CloudComponent cloudComponent = null;
      for (CloudComponent cloudComponent2 : cloudEntity.getCloudComponentInfoMap().values()) {
         if (cloudComponent2.getComponentBed().getId().equals(406L)) {
            cloudComponent = cloudComponent2;
            break;
         }
      }
      RecognitionEntity weightedEntity = (RecognitionEntity) cloudComponent.getRecognitionEntities().get(0);
      Integer position = weightedEntity.getPosition();
      for (Entry<Long, CloudComponent> entry : cloudEntity.getCloudComponentInfoMap().entrySet()) {
         if (entry.getValue().getComponentBed().getId().equals(ENUMERATION_BEHAVIOR_TYPE)
                  || entry.getValue().getComponentBed().getId().equals(MEASURABLE_ENTITY_TYPE)) {
            RecognitionEntity recEntity = (RecognitionEntity) entry.getValue().getRecognitionEntities().get(0);
            if (recEntity.getPosition() > position) {
               updateFlag(entry.getValue().getRecognitionEntities(), ExecueConstants.BY_VARIABLE);
            }
         }
      }
   }

   private void performTopBottom (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {

      // TODO: NK: Hard coding the comparative behavior and topBottomMap for now
      // TODO: NK: check with AP, what we should have it here??
      Map<Long, String> topBottomMap = new HashMap<Long, String>(1);
      topBottomMap.put(COMP_STATISTICS_TYPE, ExecueConstants.QUERY_TYPE);
      topBottomMap.put(MEASURABLE_ENTITY_TYPE, ExecueConstants.GROUP_VARIABLE);
      topBottomMap.put(NUMBER_TYPE, ExecueConstants.COMPARATIVE_LIMIT);
      topBottomMap.put(COMPARATIVE_BEHAVIOR_TYPE, ExecueConstants.ORDER_BY);

      for (Entry<Long, CloudComponent> entry : cloudEntity.getCloudComponentInfoMap().entrySet()) {
         updateFlag(entry.getValue().getRecognitionEntities(), topBottomMap.get(entry.getValue().getComponentBed()
                  .getId()));
      }
   }

   public void processWeightAssignmentRules (RecognizedCloudEntity cloudEntity, ProcessorInput processorInput) {
      return; // do nothing for now
   }

   public CloudRuleExecutor getCloudRuleExecutor () {
      return cloudRuleExecutor;
   }

   public void setCloudRuleExecutor (CloudRuleExecutor cloudRuleExecutor) {
      this.cloudRuleExecutor = cloudRuleExecutor;
   }

   public NLPServiceHelper getNlpServiceHelper () {
      return nlpServiceHelper;
   }

   public void setNlpServiceHelper (NLPServiceHelper nlpServiceHelper) {
      this.nlpServiceHelper = nlpServiceHelper;
   }
}