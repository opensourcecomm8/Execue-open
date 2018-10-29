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
package com.execue.nlp.rule.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Rule;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.association.AssociationPath;
import com.execue.nlp.bean.entity.ConceptEntity;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.PropertyEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.bean.entity.ReferedTokenPosition;
import com.execue.nlp.bean.entity.TypeEntity;
import com.execue.nlp.bean.rules.AssociationRuleType;
import com.execue.nlp.bean.rules.weight.assignment.WeightAssignmentRule;
import com.execue.nlp.bean.rules.weight.assignment.WeightAssignmentRulesContent;
import com.execue.nlp.configuration.INLPConfigurationService;
import com.execue.nlp.engine.barcode.token.TokenUtility;
import com.execue.nlp.rule.AssignmentRuleInput;
import com.execue.nlp.rule.IWeightAssignmentRule;
import com.execue.nlp.rule.service.IRuleProcessingService;
import com.execue.nlp.type.AssociationDirectionType;
import com.execue.nlp.util.NLPUtilities;

/**
 * @author Nitesh
 */
public class PathRulesExecutor {

   private IRuleProcessingService   ruleProcessingService;
   private INLPConfigurationService nlpConfigurationService;

   /**
    * Method to process the weightAssignmentRules on the list of associated paths present in the appCloudEntity
    * 
    * @param appCloudEntity
    * @param weightAssignmentRulesContent
    */
   public void processWeightAssignmentRules (RecognizedCloudEntity appCloudEntity,
            WeightAssignmentRulesContent weightAssignmentRulesContent, ProcessorInput processorInput) {
      List<AssociationPath> assocPaths = appCloudEntity.getPaths();
      Map<Long, IWeightAssignmentRule> weightAssignmentRulesByRuleId = weightAssignmentRulesContent
               .getWeightAssignmentRules();
      WeightAssignmentRule defaultLeftWeightAssignmentRule = (WeightAssignmentRule) weightAssignmentRulesContent
               .getDefaultLeftWeightAssignmentRule();
      WeightAssignmentRule defaultRightWeightAssignmentRule = (WeightAssignmentRule) weightAssignmentRulesContent
               .getDefaultRightWeightAssignmentRule();
      List<AssociationPath> pathsToRemove = new ArrayList<AssociationPath>(1);
      final double pathQualityThreshold = getNlpConfigurationService().getPathQualityThreshold();
      for (AssociationPath associationPath : assocPaths) {
         if (isImplicitAssociationPath(associationPath)) {
            updateWeightInformationForImplicitPath(associationPath);
            continue;
         }
         Set<Rule> rules = associationPath.getRules();
         RecognitionEntity source = (RecognitionEntity) associationPath.getSource();
         RecognitionEntity destination = (RecognitionEntity) associationPath.getDestination();
         AssociationDirectionType direction = associationPath.getDirection();

         // Populate the in between entities map for both recognition and un-recognition entities
         Map<ReferedTokenPosition, List<IWeightedEntity>> inBetweenRecognitionEntityMap = new HashMap<ReferedTokenPosition, List<IWeightedEntity>>();
         Map<ReferedTokenPosition, List<IWeightedEntity>> inBetweenUnRecognitionEntityMap = new HashMap<ReferedTokenPosition, List<IWeightedEntity>>();
         populateInBetweenEntitiesMap(appCloudEntity, associationPath.getSource(), associationPath.getDestination(),
                  processorInput, inBetweenRecognitionEntityMap, inBetweenUnRecognitionEntityMap);

         // If there are no rules defined for the association, then apply the default left/right association rule
         if (CollectionUtils.isEmpty(rules)) {
            AssignmentRuleInput assignmentRuleInput = new AssignmentRuleInput();
            assignmentRuleInput.setSource(source);
            assignmentRuleInput.setDestination(destination);
            assignmentRuleInput.setValidByAssociation(associationPath.isValidByAssociation());
            // Get the quality and proximity reduction value
            double qualityReduction = NLPConstants.MIN_QUALITY;
            double proximityReduction = NLPConstants.MIN_QUALITY;
            List<IWeightedEntity> entitiesInBetween = ExecueCoreUtil
                     .mergeCollectionAsList(inBetweenRecognitionEntityMap.values());
            entitiesInBetween.addAll(ExecueCoreUtil.mergeCollectionAsList(inBetweenUnRecognitionEntityMap.values()));
            if (AssociationDirectionType.LEFT_ASSOCIATION == direction) {
               qualityReduction = getRuleProcessingService().getWeight(assignmentRuleInput,
                        defaultLeftWeightAssignmentRule);
               proximityReduction = getRuleProcessingService().getProximityReductionValue(
                        defaultLeftWeightAssignmentRule, entitiesInBetween);
            } else if (AssociationDirectionType.RIGHT_ASSOCIATION == direction) {
               qualityReduction = getRuleProcessingService().getWeight(assignmentRuleInput,
                        defaultRightWeightAssignmentRule);
               proximityReduction = getRuleProcessingService().getProximityReductionValue(
                        defaultRightWeightAssignmentRule, entitiesInBetween);
            }

            double quality = NLPConstants.MAX_QUALITY;
            quality = quality - (qualityReduction + proximityReduction);
            if (quality < pathQualityThreshold) {
               pathsToRemove.add(associationPath);
            }

            WeightInformation weightInformation = new WeightInformation();
            weightInformation.setRecognitionQuality(quality);
            weightInformation.setRecognitionWeight(NLPConstants.MAX_WEIGHT);
            weightInformation.setPeanalty(NLPConstants.MAX_QUALITY - quality);
            associationPath.setWeightInformation(weightInformation);
         } else {

            // Group the rules based on the association rule type
            Set<Rule> weightRules = new HashSet<Rule>(1);
            Set<Rule> validationRules = new HashSet<Rule>(1);
            for (Rule rule : rules) {
               Long ruleId = rule.getId();
               if (weightAssignmentRulesByRuleId.containsKey(ruleId)) {
                  WeightAssignmentRule weightAssignmentRule = (WeightAssignmentRule) weightAssignmentRulesByRuleId
                           .get(ruleId);
                  AssociationRuleType associationRuleType = weightAssignmentRule.getAssociationRuleType();
                  if (associationRuleType == AssociationRuleType.VALIDATION) {
                     validationRules.add(rule);
                  } else {
                     weightRules.add(rule);
                  }
               }
            }

            // Get the default quality based on the direction of the association  
            double quality = NLPConstants.MAX_QUALITY;
            if (associationPath.getDirection() == AssociationDirectionType.LEFT_ASSOCIATION
                     && defaultLeftWeightAssignmentRule != null) {
               quality = defaultLeftWeightAssignmentRule.getWeight();
            } else if (associationPath.getDirection() == AssociationDirectionType.RIGHT_ASSOCIATION
                     && defaultRightWeightAssignmentRule != null) {
               quality = defaultRightWeightAssignmentRule.getWeight();
            }

            // Set the initial weight information to the association
            WeightInformation weightInformation = new WeightInformation();
            weightInformation.setRecognitionQuality(quality);
            weightInformation.setRecognitionWeight(NLPConstants.MAX_WEIGHT);
            associationPath.setWeightInformation(weightInformation);
            if (quality < pathQualityThreshold) {
               pathsToRemove.add(associationPath);
            } else {
               // Execute the validation type weight assignment rules
               executeValidationRules(associationPath, validationRules, weightAssignmentRulesByRuleId,
                        inBetweenRecognitionEntityMap, appCloudEntity, pathsToRemove);

               // Execute the weight type weight assignment rules
               executeWeightRules(associationPath, weightRules, weightAssignmentRulesByRuleId,
                        inBetweenRecognitionEntityMap, inBetweenUnRecognitionEntityMap, pathsToRemove);

            }
         }
      }
      // Remove the paths which are already added to pathsToRemove
      appCloudEntity.getPaths().removeAll(pathsToRemove);
      pathsToRemove.clear();

      // Again Remove if there are paths below quality threshold
      for (AssociationPath associationPath : assocPaths) {
         if (associationPath.getWeightInformation().getRecognitionQuality() < pathQualityThreshold) {
            pathsToRemove.add(associationPath);
         }
      }
      appCloudEntity.getPaths().removeAll(pathsToRemove);
      filterPathsIfPathWithUserSpecifiedRelationExists(appCloudEntity, processorInput);
   }

   private void executeWeightRules (AssociationPath associationPath, Set<Rule> weightRules,
            Map<Long, IWeightAssignmentRule> weightAssignmentRulesByRuleId,
            Map<ReferedTokenPosition, List<IWeightedEntity>> inBetweenEntitiesMap,
            Map<ReferedTokenPosition, List<IWeightedEntity>> inBetweenUnRecognitionEntityMap,
            List<AssociationPath> pathsToRemove) {

      if (CollectionUtils.isEmpty(weightRules) || MapUtils.isEmpty(inBetweenEntitiesMap)) {
         return;
      }

      // Set the initial quality to reduce as minimum
      double qualityReductionByWeightRules = NLPConstants.MIN_QUALITY;
      List<IWeightAssignmentRule> matchedWeightAssignmentRules = getMatchedWeightAssignmentRules(weightRules,
               weightAssignmentRulesByRuleId, associationPath.getDirection());

      // Calculate the quality reduction value for all the in between entities by processing the weight assignment rules 
      for (Entry<ReferedTokenPosition, List<IWeightedEntity>> entry : inBetweenEntitiesMap.entrySet()) {
         List<IWeightedEntity> entities = entry.getValue();

         for (IWeightedEntity entity : entities) {
            AssignmentRuleInput assignmentRuleInput = prepareRuleInput(entity, associationPath);
            if (!CollectionUtils.isEmpty(matchedWeightAssignmentRules)) {
               double qualityReduction = getRuleProcessingService().getWeight(assignmentRuleInput,
                        matchedWeightAssignmentRules);
               qualityReductionByWeightRules += qualityReduction;
               if (qualityReduction >= NLPConstants.MAX_QUALITY) {
                  associationPath.addUnAllowedRecognitions(entity);
                  continue;
               }
            }
            associationPath.addAllowedRecognitions(entity);
            if (assignmentRuleInput.isAllowedInProximity()) {
               associationPath.getAllowedInProximity().add(entity);
            }
         }

         if (associationPath.getUnAllowedRecognitions().containsAll(entities)) {
            pathsToRemove.add(associationPath);
            break;
         }
      }

      if (!CollectionUtils.isEmpty(associationPath.getAllowedRecognitions())) {

         // Get the proximity reduction value for all in between entities and allowed in between entities
         WeightAssignmentRule rule = (WeightAssignmentRule) matchedWeightAssignmentRules.get(0);
         List<IWeightedEntity> entitiesInBetween = ExecueCoreUtil.mergeCollectionAsList(inBetweenEntitiesMap.values());
         entitiesInBetween.addAll(ExecueCoreUtil.mergeCollectionAsList(inBetweenUnRecognitionEntityMap.values()));

         double qualityReductionByInBetweenEntitiesProximity = getRuleProcessingService().getProximityReductionValue(
                  rule, entitiesInBetween);
         double qualityAdditionForAllowedEntitiesInProximity = getRuleProcessingService().getProximityReductionValue(
                  rule, associationPath.getAllowedInProximity());

         // Compute the final recognition quality for the association  
         double finalRecognitionQuality = associationPath.getWeightInformation().getRecognitionQuality()
                  - (qualityReductionByInBetweenEntitiesProximity + qualityReductionByWeightRules)
                  + qualityAdditionForAllowedEntitiesInProximity;
         associationPath.getWeightInformation().setRecognitionQuality(finalRecognitionQuality);
         associationPath.getWeightInformation().setPeanalty(NLPConstants.MAX_QUALITY - finalRecognitionQuality);
      }
   }

   /**
    * @param associationPath
    * @param validationRules
    * @param weightAssignmentRules
    * @param inBetweenEntitiesMap
    * @param appCloudEntity
    * @param pathsToRemove
    */
   private void executeValidationRules (AssociationPath associationPath, Set<Rule> validationRules,
            Map<Long, IWeightAssignmentRule> weightAssignmentRules,
            Map<ReferedTokenPosition, List<IWeightedEntity>> inBetweenEntitiesMap,
            RecognizedCloudEntity appCloudEntity, List<AssociationPath> pathsToRemove) {
      boolean isValidPath = false;
      ConceptEntity destinationEntity = (ConceptEntity) associationPath.getDestination();

      // Guard condition to check if the destination is of type concept then returns
      if (destinationEntity.getEntityType() == RecognitionEntityType.CONCEPT_ENTITY) {
         return;
      }

      // If no validation or in between entities exists, then path is valid
      if (CollectionUtils.isEmpty(validationRules) || MapUtils.isEmpty(inBetweenEntitiesMap)) {
         isValidPath = true;
      }

      for (Rule rule : validationRules) {
         Long ruleId = rule.getId();
         IWeightAssignmentRule weightAssignmentRule = weightAssignmentRules.get(ruleId);
         if (weightAssignmentRule == null) {
            continue;
         }
         for (Entry<ReferedTokenPosition, List<IWeightedEntity>> entry : inBetweenEntitiesMap.entrySet()) {
            List<IWeightedEntity> entities = entry.getValue();
            for (IWeightedEntity entity : entities) {
               AssignmentRuleInput assignmentRuleInput = prepareRuleInput(entity, associationPath);
               boolean valid = getRuleProcessingService().validate(assignmentRuleInput, weightAssignmentRule);
               if (!valid) {
                  associationPath.addUnAllowedRecognitions(entity);
               } else {
                  isValidPath = true;
               }
            }
         }
      }

      if (isValidPath) {
         // If we get valid path then remove the other paths where destination is same as this concept of validation type association path 
         for (IWeightedEntity weightedEntity : appCloudEntity.getRecognitionEntities()) {
            OntoEntity ontoEntity = (OntoEntity) weightedEntity;
            if (ontoEntity.getEntityType() == RecognitionEntityType.CONCEPT_ENTITY
                     && ((ConceptEntity) ontoEntity).getConceptBedId().equals(destinationEntity.getConceptBedId())) {
               for (AssociationPath path : appCloudEntity.getPaths()) {
                  if (path.getDestination().equals(ontoEntity)) {
                     pathsToRemove.add(path);
                  }
               }
               break;
            }
         }
      } else {
         pathsToRemove.add(associationPath);
      }
   }

   private void updateWeightInformationForImplicitPath (AssociationPath associationPath) {
      associationPath.setWeightInformation(NLPUtilities.getDefaultWeightInformation());
   }

   private boolean isImplicitAssociationPath (AssociationPath associationPath) {
      if (((RecognitionEntity) associationPath.getSource()).getPosition() < 0
               || ((RecognitionEntity) associationPath.getDestination()).getPosition() < 0) {
         return true;
      }
      return false;
   }

   /**
    * // 1. create pathDef map as Source-destBeID as key // 2. get the in between entities between source and dest. //
    * 3. store the relation name. if any path share the same relation rest gets invalidated.
    * 
    * @param appCloudEntity
    * @param processorInput
    */
   private void filterPathsIfPathWithUserSpecifiedRelationExists (RecognizedCloudEntity appCloudEntity,
            ProcessorInput processorInput) {
      Map<String, List<AssociationPath>> pathsMapBySrcDest = pouplatePathDefMapBySourceAndDestBeIdAndPos(appCloudEntity
               .getPaths());
      List<AssociationPath> pathsToRemove = new ArrayList<AssociationPath>(1);

      for (Entry<String, List<AssociationPath>> entry : pathsMapBySrcDest.entrySet()) {
         Set<Long> relationBedIds = new HashSet<Long>(1);
         List<AssociationPath> assocPathList = entry.getValue();
         AssociationPath associationPath = assocPathList.get(0);

         Map<ReferedTokenPosition, List<IWeightedEntity>> inBetweenRecognitionEntityMap = new HashMap<ReferedTokenPosition, List<IWeightedEntity>>();
         Map<ReferedTokenPosition, List<IWeightedEntity>> inBetweenUnRecognitionEntityMap = new HashMap<ReferedTokenPosition, List<IWeightedEntity>>();
         populateInBetweenEntitiesMap(appCloudEntity, associationPath.getSource(), associationPath.getDestination(),
                  processorInput, inBetweenRecognitionEntityMap, inBetweenUnRecognitionEntityMap);
         for (List<IWeightedEntity> entities : inBetweenRecognitionEntityMap.values()) {
            if (entities.size() == 1) {
               IWeightedEntity weightedEntity = entities.get(0);
               if (((RecognitionEntity) weightedEntity).getEntityType() == RecognitionEntityType.PROPERTY_ENTITY) {
                  relationBedIds.add(((PropertyEntity) weightedEntity).getRelationBedId());
               }
            }
         }
         List<AssociationPath> tripleWithRelation = new ArrayList<AssociationPath>(1);

         if (!relationBedIds.isEmpty()) {
            for (AssociationPath associationPath2 : assocPathList) {
               List<EntityTripleDefinition> entityTriples = associationPath2.getETDs();
               for (EntityTripleDefinition entityTripleDefinition : entityTriples) {
                  Long relationId = entityTripleDefinition.getRelation().getId();
                  if (relationBedIds.contains(relationId)) {
                     tripleWithRelation.add(associationPath2);
                  }
               }
            }
         }
         if (!tripleWithRelation.isEmpty()) {
            for (AssociationPath path : assocPathList) {
               if (!tripleWithRelation.contains(path)) {
                  pathsToRemove.add(path);
               }
            }
         }
      }
      appCloudEntity.getPaths().removeAll(pathsToRemove);
   }

   /**
    * Method to populate the Map of Association paths as value against the sourceId,pos-destId,pos as key.
    * 
    * @param paths
    * @return
    */
   private Map<String, List<AssociationPath>> pouplatePathDefMapBySourceAndDestBeIdAndPos (List<AssociationPath> paths) {
      Map<String, List<AssociationPath>> pathsMapBySrcDest = new HashMap<String, List<AssociationPath>>(1);
      for (AssociationPath associationPath : paths) {
         IWeightedEntity source = associationPath.getSource();
         TypeEntity sourceEntity = (TypeEntity) source;
         IWeightedEntity destination = associationPath.getDestination();
         TypeEntity destinationEntity = (TypeEntity) destination;
         String key = sourceEntity.getId() + "," + sourceEntity.getPosition();
         key = key + " - " + destinationEntity.getId() + "," + destinationEntity.getPosition();
         List<AssociationPath> pathsForKey = pathsMapBySrcDest.get(key);
         if (pathsForKey == null) {
            pathsForKey = new ArrayList<AssociationPath>(1);
            pathsMapBySrcDest.put(key, pathsForKey);
         }
         pathsForKey.add(associationPath);

      }
      return pathsMapBySrcDest;
   }

   /**
    * This method returns the list of weight assignment rules from the map of weightAssignmentRules for the matched
    * direction of the assignmentRuleInput
    * @param rulesTobeMatched
    * @param weightAssignmentRules
    * @param assignmentRuleInput
    * 
    * @return the list of IWeightAssignmentRule
    */
   private List<IWeightAssignmentRule> getMatchedWeightAssignmentRules (Set<Rule> rulesTobeMatched,
            Map<Long, IWeightAssignmentRule> weightAssignmentRules, AssociationDirectionType associationDirectionType) {
      List<IWeightAssignmentRule> matchedWeightAssignmentRules = new ArrayList<IWeightAssignmentRule>(1);
      if (CollectionUtils.isEmpty(rulesTobeMatched)) {
         return matchedWeightAssignmentRules;
      }
      for (Rule rule : rulesTobeMatched) {
         Long ruleId = rule.getId();
         if (weightAssignmentRules.containsKey(ruleId)) {
            WeightAssignmentRule weightAssignmentRule = (WeightAssignmentRule) weightAssignmentRules.get(ruleId);
            if (associationDirectionType == weightAssignmentRule.getDefaultWeightAssignmentRuleContent()
                     .getAssociationDirection()) {
               matchedWeightAssignmentRules.add(weightAssignmentRule);
            }
         }
      }
      return matchedWeightAssignmentRules;
   }

   /**
    * This method populates both the recognized and unrecognized in between entities map for the given input
    * source and destination
    * 
    * @param appCloudEntity
    * @param source
    * @param destination
    * @param processorInput
    * @param inBetweenRecognitionEntityMap
    * @param inBetweenUnRecognitionEntityMap
    */
   private void populateInBetweenEntitiesMap (RecognizedCloudEntity appCloudEntity, IWeightedEntity source,
            IWeightedEntity destination, ProcessorInput processorInput,
            Map<ReferedTokenPosition, List<IWeightedEntity>> inBetweenRecognitionEntityMap,
            Map<ReferedTokenPosition, List<IWeightedEntity>> inBetweenUnRecognitionEntityMap) {

      // Get the recognition entities by ReferedTokenPosition 
      Map<ReferedTokenPosition, List<IWeightedEntity>> recEntitesByRTPMap = NLPUtilities
               .getRecognitionEntitiesByRTPMap(appCloudEntity.getRecognitionEntities());

      // Get the un-recognition entities by ReferedTokenPosition
      List<IWeightedEntity> unrecognizedEntities = processorInput.getUnrecognizedEntities();
      Map<ReferedTokenPosition, List<IWeightedEntity>> unRecEntitesByRTPMap = NLPUtilities
               .getRecognitionEntitiesByRTPMap(unrecognizedEntities);

      // Get the extra non current app recognition entities by ReferedTokenPosition
      List<IWeightedEntity> extraNonCurrentAppRecognitionEntities = new ArrayList<IWeightedEntity>(1);
      extraNonCurrentAppRecognitionEntities.addAll(processorInput.getRecognitionEntities());
      if (!CollectionUtils.isEmpty(processorInput.getUnrecognizedBaseRecEntities())) {
         extraNonCurrentAppRecognitionEntities.addAll(processorInput.getUnrecognizedBaseRecEntities());
      }
      Map<ReferedTokenPosition, List<IWeightedEntity>> extraRecEntitesByRTPMap = NLPUtilities
               .getRecognitionEntitiesByRTPMap(extraNonCurrentAppRecognitionEntities);

      // Get the in between positions
      ReferedTokenPosition sourceRTP = new ReferedTokenPosition(((RecognitionEntity) source).getReferedTokenPositions());
      ReferedTokenPosition destRTP = new ReferedTokenPosition(((RecognitionEntity) destination)
               .getReferedTokenPositions());

      //      List<Integer> inBetweenPositions = sourceRTP.getInBetweenPositions(destRTP);
      List<Integer> combinedRTPs = new ArrayList<Integer>();
      combinedRTPs.addAll(sourceRTP.getReferedTokenPositionsList());
      combinedRTPs.addAll(destRTP.getReferedTokenPositionsList());
      ReferedTokenPosition combinedRTP = new ReferedTokenPosition(combinedRTPs);
      List<Integer> inBetweenPositions = combinedRTP.getInBetweenPos();

      // Populate the inBetweenRecognitionEntityMap and inBetweenUnRecognitionEntityMap for the in between positions
      for (Integer position : inBetweenPositions) {

         // Add the recognition entities 
         boolean positionAdded = checkAndPopulateInBetweenEntitiesIfFound(inBetweenRecognitionEntityMap,
                  recEntitesByRTPMap, position);
         if (!positionAdded) {
            positionAdded = checkAndPopulateInBetweenEntitiesIfFound(inBetweenRecognitionEntityMap,
                     extraRecEntitesByRTPMap, position);
         }

         // Add the unrecognition entities 
         checkAndPopulateInBetweenEntitiesIfFound(inBetweenUnRecognitionEntityMap, unRecEntitesByRTPMap, position);
      }
   }

   /**
    * @param inBetweenRecognitionEntityMap
    * @param recEntitesByRTPMap
    * @param position
    * @param positionAdded
    * @return true or false depending on if in between entity(ies) is added or not
    */
   private boolean checkAndPopulateInBetweenEntitiesIfFound (
            Map<ReferedTokenPosition, List<IWeightedEntity>> inBetweenRecognitionEntityMap,
            Map<ReferedTokenPosition, List<IWeightedEntity>> recEntitesByRTPMap, Integer position) {
      boolean positionAdded = false;
      for (Entry<ReferedTokenPosition, List<IWeightedEntity>> entry : recEntitesByRTPMap.entrySet()) {
         if (entry.getKey().getReferedTokenPositions().contains(position)) {
            List<IWeightedEntity> recEnList = inBetweenRecognitionEntityMap.get(entry.getKey());
            if (recEnList == null) {
               recEnList = new ArrayList<IWeightedEntity>(1);
               inBetweenRecognitionEntityMap.put(entry.getKey(), recEnList);
            }
            for (IWeightedEntity weightedEntity : entry.getValue()) {
               if (!recEnList.contains(weightedEntity)) {
                  recEnList.add(weightedEntity);
               }
            }
            positionAdded = true;
            break;
         }
      }
      return positionAdded;
   }

   private AssignmentRuleInput prepareRuleInput (IWeightedEntity entity, AssociationPath associationPath) {
      AssignmentRuleInput assignmentRuleInput = new AssignmentRuleInput();
      List<IWeightedEntity> addedRecs = new ArrayList<IWeightedEntity>(1);
      addedRecs.add(entity);
      List<String> recEntityStrings = TokenUtility.getRecognitionEntityAsString(addedRecs);
      String userQueryInStrignForm = ExecueCoreUtil.convertAsString(recEntityStrings);
      assignmentRuleInput.setUserQuery(userQueryInStrignForm);
      assignmentRuleInput.setSource(associationPath.getSource());
      assignmentRuleInput.setDestination(associationPath.getDestination());
      return assignmentRuleInput;
   }

   /**
    * @return the ruleProcessingService
    */
   public IRuleProcessingService getRuleProcessingService () {
      return ruleProcessingService;
   }

   /**
    * @param ruleProcessingService
    *           the ruleProcessingService to set
    */
   public void setRuleProcessingService (IRuleProcessingService ruleProcessingService) {
      this.ruleProcessingService = ruleProcessingService;
   }

   /**
    * @return the nlpConfigurationService
    */
   public INLPConfigurationService getNlpConfigurationService () {
      return nlpConfigurationService;
   }

   /**
    * @param nlpConfigurationService the nlpConfigurationService to set
    */
   public void setNlpConfigurationService (INLPConfigurationService nlpConfigurationService) {
      this.nlpConfigurationService = nlpConfigurationService;
   }
}
