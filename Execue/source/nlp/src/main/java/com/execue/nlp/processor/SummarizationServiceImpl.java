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
package com.execue.nlp.processor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.bean.nlp.RecognizedType;
import com.execue.core.common.bean.nlp.UserQuery;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.constants.ExecueConstants;
import com.execue.nlp.bean.entity.Association;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.ReferedTokenPosition;
import com.execue.nlp.bean.entity.TypeEntity;
import com.execue.nlp.bean.matrix.Possibility;
import com.execue.nlp.bean.matrix.RootMatrix;
import com.execue.nlp.configuration.INLPConfigurationService;
import com.execue.nlp.exception.NLPExceptionCodes;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.nlp.helper.NLPServiceHelper;
import com.execue.nlp.util.NLPUtilities;
import com.execue.util.MathUtil;

/**
 * @author Nitesh
 */
public class SummarizationServiceImpl implements ISummarizationService {

   private final Logger                logger = Logger.getLogger(SummarizationServiceImpl.class);
   private NLPServiceHelper            nlpServiceHelper;
   private IReducedFormRevesionService reducedFormRevesionService;
   private INLPConfigurationService    nlpConfiguration;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.processor.ISummarizationService#getUnambiguousPossibilities(com.execue.nlp.bean.matrix.Possibility)
    */
   private List<Possibility> getUnambiguousPossibilities (Possibility possibility, RootMatrix rootMatrix,
            UserQuery userQuery, Map<Double, List<Integer>> possibilityListByWeight) throws CloneNotSupportedException {
      List<Possibility> unAmbiguousPossibilities = new ArrayList<Possibility>(1);
      if (CollectionUtils.isEmpty(possibility.getRecognitionEntities())) {
         return unAmbiguousPossibilities;
      }
      // Get the default central concepts
      List<IWeightedEntity> defaultCentralConcepts = NLPUtilities.getDefaultCentralConcepts(possibility
               .getRecognitionEntities());
      int possibilityCount = nlpConfiguration.getMaxPossibilityCountForSearchType(userQuery.getSearchType());
      // remove the default central concepts from the total rec entities in the possibility
      List<IWeightedEntity> filteredRecEntities = new ArrayList<IWeightedEntity>(1);
      filteredRecEntities.addAll(possibility.getRecognitionEntities());
      if (!CollectionUtils.isEmpty(possibility.getUnrecognizedBaseRecEntities())) {
         filteredRecEntities.addAll(possibility.getUnrecognizedBaseRecEntities());
      }
      filteredRecEntities.removeAll(defaultCentralConcepts);

      // get the unambiguous possibility
      unAmbiguousPossibilities = NLPUtilities.getUnambiguousPossibilities(filteredRecEntities, possibilityCount);

      // Set the id and add the default central concepts if not exists in the unambiguous possibility
      List<Possibility> clonedPossibilitiesWithDefaultCentralConcepts = new ArrayList<Possibility>(1);
      List<Possibility> possibilitiesToRemove = new ArrayList<Possibility>(1);
      for (Possibility unambiguousPossibility : unAmbiguousPossibilities) {
         unambiguousPossibility.setId(rootMatrix.getNextPossibilityId());
         unambiguousPossibility.setModel(possibility.getModel());
         if (!unambiguousPossibility.isNonAttributePossibility() && !rootMatrix.isFromArticle()) {
            possibilitiesToRemove.add(unambiguousPossibility);
            continue;
         }

         if (!CollectionUtils.isEmpty(defaultCentralConcepts)) {
            if (!unambiguousPossibility.isCentralConceptExists()) {
               possibilitiesToRemove.add(unambiguousPossibility);
               for (IWeightedEntity weightedEntity : defaultCentralConcepts) {
                  Possibility clonedPossibility = (Possibility) unambiguousPossibility.clone();
                  clonedPossibility.setId(rootMatrix.getNextPossibilityId());
                  clonedPossibility.getRecognitionEntities().add(weightedEntity);
                  clonedPossibilitiesWithDefaultCentralConcepts.add(clonedPossibility);
                  double averageRecognitionWeightForPossibility = calculateAverageRecognitionWeightForPossibility(
                           clonedPossibility, rootMatrix.getUserQueryTokensCount());
                  List<Integer> possibilitiesByWeight = possibilityListByWeight
                           .get(averageRecognitionWeightForPossibility);
                  if (possibilitiesByWeight == null) {
                     possibilitiesByWeight = new ArrayList<Integer>(1);
                     possibilityListByWeight.put(averageRecognitionWeightForPossibility, possibilitiesByWeight);
                  }
                  possibilitiesByWeight.add(clonedPossibility.getId());
               }
            } else {
               double averageRecognitionWeightForPossibility = calculateAverageRecognitionWeightForPossibility(
                        unambiguousPossibility, rootMatrix.getUserQueryTokensCount());
               List<Integer> possibilitiesByWeight = possibilityListByWeight
                        .get(averageRecognitionWeightForPossibility);
               if (possibilitiesByWeight == null) {
                  possibilitiesByWeight = new ArrayList<Integer>(1);
                  possibilityListByWeight.put(averageRecognitionWeightForPossibility, possibilitiesByWeight);
               }
               possibilitiesByWeight.add(unambiguousPossibility.getId());
            }
         } else {
            double averageRecognitionWeightForPossibility = calculateAverageRecognitionWeightForPossibility(
                     unambiguousPossibility, rootMatrix.getUserQueryTokensCount());
            List<Integer> possibilitiesByWeight = possibilityListByWeight.get(averageRecognitionWeightForPossibility);
            if (possibilitiesByWeight == null) {
               possibilitiesByWeight = new ArrayList<Integer>(1);
               possibilityListByWeight.put(averageRecognitionWeightForPossibility, possibilitiesByWeight);
            }
            possibilitiesByWeight.add(unambiguousPossibility.getId());
         }

      }
      unAmbiguousPossibilities.removeAll(possibilitiesToRemove);
      unAmbiguousPossibilities.addAll(clonedPossibilitiesWithDefaultCentralConcepts);
      return unAmbiguousPossibilities;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.processor.ISummarizationService#setAssociations(java.util.List, java.util.List)
    */
   private void setAssociations (RootMatrix rootMatrix, Possibility possibility, List<Association> associations,
            List<Possibility> unAmbiguousPossibilities, int possibilityCount) throws CloneNotSupportedException {
      Map<Long, List<Association>> associationMap = new LinkedHashMap<Long, List<Association>>();
      int newPossibilityCount = 1;
      populateAssociationMapByPathId(associationMap, associations);
      for (Entry<Long, List<Association>> entry : associationMap.entrySet()) {
         List<Possibility> newPossiList = new ArrayList<Possibility>(1);
         for (Possibility unAmbiguousPossibility : unAmbiguousPossibilities) {
            List<IWeightedEntity> weightedEntities = unAmbiguousPossibility.getRecognitionEntities();
            boolean addedAssociation = false;
            for (Association association : entry.getValue()) {
               boolean addAssociation = false;
               if (weightedEntities.contains(association.getPathComponent().get(0))
                        && weightedEntities.contains(association.getPathComponent().get(
                                 association.getPathComponent().size() - 1))) {
                  addAssociation = true;
                  if (!CollectionUtils.isEmpty(association.getUnAllowedRecs())) {
                     for (IWeightedEntity weightedEntity : weightedEntities) {
                        if (association.getUnAllowedRecs().contains(weightedEntity)) {
                           addAssociation = false;
                           break;
                        }
                     }
                  }
               }
               if (addAssociation) {
                  addedAssociation = addAssociationToPossibility(association, unAmbiguousPossibility, rootMatrix
                           .getConvertibleBeds());
                  if (!addedAssociation && newPossibilityCount < possibilityCount) {
                     Possibility newPossibility = (Possibility) unAmbiguousPossibility.clone();
                     newPossibilityCount++;
                     List<Association> newAssocList = new ArrayList<Association>(1);
                     newAssocList.addAll(unAmbiguousPossibility.getAssociations());
                     newPossibility.setAssociations(newAssocList);
                     newPossibility.setId(rootMatrix.getNextPossibilityId());
                     Association association2 = null;
                     for (Association association3 : newPossibility.getAssociations()) {
                        if (association.getPathDefId().equals(association3.getPathDefId())) {
                           association2 = association3;
                           break;
                        }

                     }
                     newPossibility.getAssociations().remove(association2);
                     addedAssociation = addAssociationToPossibility(association, newPossibility, rootMatrix
                              .getConvertibleBeds());
                     newPossiList.add(newPossibility);
                  }

               }
            }
         }
         unAmbiguousPossibilities.addAll(newPossiList);
         // Iterate Through the Default Central Concepts to check if need to add these.
      }
      for (Possibility unAmbiguousPossibility : unAmbiguousPossibilities) {
         List<IWeightedEntity> impliciAddedConcepts = NLPUtilities.getImlicitAddedConcepts(possibility
                  .getRecognitionEntities());
         for (IWeightedEntity weightedEntity : impliciAddedConcepts) {
            RecognitionEntity implicitAddedConcept = (RecognitionEntity) weightedEntity;
            boolean removeConcept = true;
            for (Association association : unAmbiguousPossibility.getAssociations()) {
               if (association.getPathComponent().contains(implicitAddedConcept)) {
                  removeConcept = false;
                  break;
               }
            }
            if (removeConcept) {
               unAmbiguousPossibility.getRecognitionEntities().remove(implicitAddedConcept);
            }
         }
      }
   }

   public boolean addAssociationToPossibility (Association association, Possibility possibility,
            List<Set<Long>> convertibleBeds) {
      boolean addAssociation = true;
      addAssociation = checkIfAssociationCanBeAdded(association, possibility, convertibleBeds);
      if (!addAssociation) {
         return addAssociation;
      }
      RecognitionEntity startEntity = association.getPathComponent().get(0);
      RecognitionEntity destEntity = association.getPathComponent().get(association.getPathComponent().size() - 1);

      int index = possibility.getRecognitionEntities().indexOf(startEntity);
      RecognitionEntity recEntity1 = (RecognitionEntity) possibility.getRecognitionEntities().get(index);
      int lastIndex = possibility.getRecognitionEntities().indexOf(destEntity);
      RecognitionEntity recEntity2 = (RecognitionEntity) possibility.getRecognitionEntities().get(lastIndex);
      startEntity.setNormalizedData(recEntity1.getNormalizedData());
      destEntity.setNormalizedData(recEntity2.getNormalizedData());

      ReferedTokenPosition rtpSource = new ReferedTokenPosition(association.getPathComponent().get(0)
               .getOriginalReferedTokenPositions());
      ReferedTokenPosition rtpDest = new ReferedTokenPosition(association.getPathComponent().get(
               association.getPathComponent().size() - 1).getOriginalReferedTokenPositions());
      List<Integer> inBetweenPositions = rtpSource.getInBetweenPositions(rtpDest);
      List<Integer> unusedPos = getUnusedPositions(inBetweenPositions, possibility.getRecognitionEntities());
      for (Integer unusedPosition : unusedPos) {
         for (IWeightedEntity weightedEntity : association.getUnAllowedRecs()) {
            RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
            if (recEntity.getOriginalReferedTokenPositions().contains(unusedPosition)) {
               addAssociation = false;
               break;
            }
         }
         if (!addAssociation) {
            break;
         }
      }
      if (addAssociation) {
         possibility.addAssociations(association);
         return true;
      }
      return false;
   }

   private boolean checkIfAssociationCanBeAdded (Association association, Possibility possibility,
            List<Set<Long>> convertibleBeds) {
      for (Association addedAssociation : possibility.getAssociations()) {
         if (association.getCardinality() <= 1 && association.conceptuallyEquals(addedAssociation, convertibleBeds)) {
            return false;
         }
      }
      return true;
   }

   /**
    * Populate the Map of the Association as sourceid-destId if concept is not defined for source or destination use
    * typeBeid .
    * 
    * @param associationMap
    * @param associations
    */
   private void populateAssociationMapByPathId (Map<Long, List<Association>> associationMap,
            List<Association> associations) {
      for (Association association : associations) {
         List<Association> assocList = associationMap.get(association.getPathDefId());
         if (assocList == null) {
            assocList = new ArrayList<Association>(1);
            associationMap.put(association.getPathDefId(), assocList);
         }
         assocList.add(association);
      }

   }

   private List<Integer> getUnusedPositions (List<Integer> inBetweenPositions, List<IWeightedEntity> recognitionEntities) {
      List<Integer> unusedPos = new ArrayList<Integer>(1);
      for (Integer pos : inBetweenPositions) {
         boolean positionCovered = false;
         for (IWeightedEntity weightedEntity : recognitionEntities) {
            RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
            if (recEntity.getReferedTokenPositions().contains(pos)) {
               positionCovered = true;
               break;
            }
         }
         if (!positionCovered) {
            unusedPos.add(pos);
         }
      }
      return unusedPos;
   }

   public List<Possibility> filterByHangingRelations (List<Possibility> allUnAmbiguousPossibilities) {
      if (CollectionUtils.isEmpty(allUnAmbiguousPossibilities)) {
         return allUnAmbiguousPossibilities;
      }
      SortedMap<Integer, List<Possibility>> possibilitiesByUnconnectedRelations = new TreeMap<Integer, List<Possibility>>();
      for (Possibility possibility : allUnAmbiguousPossibilities) {
         int count = getHangingRelationCountForPossibility(possibility);
         List<Possibility> possibilityList = possibilitiesByUnconnectedRelations.get(count);
         if (possibilityList == null) {
            possibilityList = new ArrayList<Possibility>(1);
            possibilitiesByUnconnectedRelations.put(count, possibilityList);
         }
         possibilityList.add(possibility);
      }
      List<Possibility> values = (List<Possibility>) possibilitiesByUnconnectedRelations.values().toArray()[0];
      return values;
   }

   private int getHangingRelationCountForPossibility (Possibility possibility) {
      List<IWeightedEntity> recognitionEntities = possibility.getRecognitionEntities();
      int count = 0;
      // Get all the path components
      Set<IWeightedEntity> associationEntities = new HashSet<IWeightedEntity>(1);
      List<Association> associations = possibility.getAssociations();
      for (Association association : associations) {
         associationEntities.addAll(association.getPathComponent());
      }

      for (IWeightedEntity weightedEntity : recognitionEntities) {
         RecognitionEntity recEntity = (RecognitionEntity) weightedEntity;
         if (recEntity.getEntityType() == RecognitionEntityType.PROPERTY_ENTITY
                  && !associationEntities.contains(recEntity)) {
            count++;
         }
      }
      return count;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.processor.ISummarizationService#populateUnambiguousPossibilities(com.execue.nlp.bean.matrix.RootMatrix,
    *      com.execue.core.common.bean.nlp.UserQuery)
    */
   public void populateUnambiguousPossibilities (RootMatrix rootMatrix, UserQuery userQuery) {
      List<Possibility> enhancedSemanticsBasedPossibilityList = rootMatrix.getPossibilities();
      // Get the unambiguous possibilities for each app cloud based possibility and set the associations
      List<Possibility> allUnAmbiguousPossibilities = new ArrayList<Possibility>(1);
      try {
         Map<Possibility, List<Possibility>> unambiguousPossibilitiesByOrigPossibility = new HashMap<Possibility, List<Possibility>>(
                  1);
         Map<Double, List<Integer>> possibilityListByWeight = new HashMap<Double, List<Integer>>(1);
         for (Possibility enhancedSemanticsBasedPossibility : enhancedSemanticsBasedPossibilityList) {
            // remove all the unassociated attribute entities from the possibility.
            removeUnAssociatedAttributeEntities(enhancedSemanticsBasedPossibility);

            List<Possibility> unAmbiguousPossibilities = getUnambiguousPossibilities(enhancedSemanticsBasedPossibility,
                     rootMatrix, userQuery, possibilityListByWeight);
            if (!CollectionUtils.isEmpty(unAmbiguousPossibilities)) {
               unambiguousPossibilitiesByOrigPossibility.put(enhancedSemanticsBasedPossibility,
                        unAmbiguousPossibilities);
            }

         }
         Set<Double> keySet = possibilityListByWeight.keySet();
         List<Double> topCluster = MathUtil.getMidwayTopCluster(new ArrayList<Double>(keySet));
         possibilityListByWeight.keySet().retainAll(topCluster);

         for (Entry<Possibility, List<Possibility>> entry : unambiguousPossibilitiesByOrigPossibility.entrySet()) {
            logAssociationsBeforeUnambiguousPossibilityCreation(entry.getKey());
            List<Association> associations = entry.getKey().getAssociations();
            String appName = null;
            Set<Application> applications = entry.getKey().getModel().getApplications();
            for (Application application : applications) {
               appName = application.getName();
               break;
            }
            Set<Integer> validIds = new HashSet<Integer>(1);
            for (List<Integer> ids : possibilityListByWeight.values()) {
               validIds.addAll(ids);
            }
            List<Possibility> validUnAmbiguousPossibilities = new ArrayList<Possibility>();
            for (Possibility possibility : entry.getValue()) {
               List<Association> origAssociations = new ArrayList<Association>();
               origAssociations.addAll(associations);
               if (!validIds.contains(possibility.getId())) {
                  continue;
               }
               List<Association> revisedAssocList = checkIfNeedToReviseAssociationForApp(possibility, origAssociations,
                        appName);
               Collections.sort(revisedAssocList, new Comparator<Association>() {

                  public int compare (Association a1, Association a2) {
                     double association1Peanalty = a1.getWeightInformation().getPeanalty();
                     double association2Peanalty = a2.getWeightInformation().getPeanalty();
                     if (association1Peanalty > association2Peanalty) {
                        return 1;
                     } else if (association1Peanalty < association2Peanalty) {
                        return -1;
                     } else {
                        return 0;
                     }
                  }
               });
               List<Possibility> newPossibilityList = new ArrayList<Possibility>(1);
               newPossibilityList.add(possibility);
               int possibilityCount = nlpConfiguration.getMaxPossibilityCountForSearchType(userQuery.getSearchType());
               Map<IWeightedEntity, Association> defaultAssociationBySource = populateDefaultPathMapBySourceConcept(
                        possibility, revisedAssocList);
               setAssociations(rootMatrix, possibility, revisedAssocList, newPossibilityList, possibilityCount);
               // add the default path and concept in the possibility if destination attrbute is not associated by an
               // explicit token.
               addDefaultPathAndRecognition(possibility, defaultAssociationBySource);
               validUnAmbiguousPossibilities.addAll(newPossibilityList);
            }
            logAssociationsAfterUnambiguousPossibilityCreation(validUnAmbiguousPossibilities);
            allUnAmbiguousPossibilities.addAll(validUnAmbiguousPossibilities);
         }
      } catch (CloneNotSupportedException e) {
         logger.error(e.getMessage(), e);
         throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
      }

      // filter hanging relations
      if (getNlpServiceHelper().getNlpConfigurationService().isFilterPossibilityWithHangingRelations()) {
         allUnAmbiguousPossibilities = filterByHangingRelations(allUnAmbiguousPossibilities);
      }
      // removeUnconnectedTimeFrameIfAtleastOneTFIsConnected(allUnAmbiguousPossibilities);
      //      logAssociationsAfterUnambiguousPossibilityCreation(allUnAmbiguousPossibilities);

      // Calculate and update the weight for unambiguous possibilities
      getNlpServiceHelper().updatePossibilityWeight(allUnAmbiguousPossibilities, rootMatrix);

      rootMatrix.setPossibilities(allUnAmbiguousPossibilities);
   }

   /**
    * @param possibility
    * @param revisedAssocList
    * @return
    */
   private Map<IWeightedEntity, Association> populateDefaultPathMapBySourceConcept (Possibility possibility,
            List<Association> revisedAssocList) {
      List<IWeightedEntity> defaultNonCentralConcepts = NLPUtilities.getDefaultNonCentralConcepts(possibility
               .getRecognitionEntities());
      List<Association> defaultAssociations = NLPUtilities.getDefaultAssociations(revisedAssocList);
      if (!CollectionUtils.isEmpty(defaultAssociations)) {
         revisedAssocList.removeAll(defaultAssociations);
      }
      Map<IWeightedEntity, Association> defaultAssociationBySource = new HashMap<IWeightedEntity, Association>(1);
      for (IWeightedEntity weightedEntity : defaultNonCentralConcepts) {
         for (Association association : defaultAssociations) {
            if (association.getPathComponent().get(0).equals(weightedEntity)) {
               defaultAssociationBySource.put(weightedEntity, association);
               break;
            }
         }
      }
      return defaultAssociationBySource;
   }

   private void addDefaultPathAndRecognition (Possibility possibility,
            Map<IWeightedEntity, Association> defaultAssociationBySource) {

      for (Entry<IWeightedEntity, Association> entry : defaultAssociationBySource.entrySet()) {
         Association association = entry.getValue();
         IWeightedEntity dest = association.getPathComponent().get(association.getPathComponent().size() - 1);
         if (possibility.getRecognitionEntities().contains(dest)) {
            boolean destinationAssociated = false;
            for (Association association2 : possibility.getAssociations()) {
               if (association2.getPathComponent().contains(dest)) {
                  destinationAssociated = true;
                  break;
               }
            }
            if (!destinationAssociated) {
               possibility.getRecognitionEntities().add(entry.getKey());
               List<Association> associations = possibility.getAssociations();
               if (CollectionUtils.isEmpty(associations)) {
                  possibility.setAssociations(associations);
               }
               associations.add(association);
            }
         }
      }

   }

   /**
    * Method to remove any of the attribute type entity if it not connected in the possibility.
    * 
    * @param enhancedSemanticsBasedPossibility
    */
   private void removeUnAssociatedAttributeEntities (Possibility enhancedSemanticsBasedPossibility) {
      List<IWeightedEntity> entitiesToRemove = new ArrayList<IWeightedEntity>(1);
      for (IWeightedEntity weightedEntity : enhancedSemanticsBasedPossibility.getRecognitionEntities()) {
         TypeEntity typeEntity = (TypeEntity) weightedEntity;
         // TODO -NA- as of now only removing the attributes if the are from base.
         // need to think of more generic rule here
         if (typeEntity.getBehaviors().contains(BehaviorType.ATTRIBUTE) && typeEntity.getModelGroupId().equals(1L)) {
            boolean entityAssociated = false;
            for (Association association : enhancedSemanticsBasedPossibility.getAssociations()) {
               if (association.getPathComponent().contains(typeEntity)) {
                  entityAssociated = true;
                  break;
               }
            }
            if (!entityAssociated) {
               entitiesToRemove.add(typeEntity);
            }
         }
      }
      if (!CollectionUtils.isEmpty(entitiesToRemove)) {
         enhancedSemanticsBasedPossibility.getRecognitionEntities().removeAll(entitiesToRemove);
      }

   }

   private List<Association> checkIfNeedToReviseAssociationForApp (Possibility possibility,
            List<Association> associations, String appName) {
      if (CollectionUtils.isEmpty(associations)) {
         return associations;
      }
      String appSpecificMethodByAppName = getNlpConfiguration().getAppSpecificMethodByAppName(appName);
      if (appSpecificMethodByAppName != null) {
         Set<Association> associationSet = new HashSet<Association>(1);
         Method[] methods = reducedFormRevesionService.getClass().getMethods();
         for (Method method : methods) {
            if (method.getName().equalsIgnoreCase(appSpecificMethodByAppName)) {
               try {
                  // TODO -NA- for now directly calling with Given arguments a framework has to be written to call it
                  // correctly
                  associationSet.addAll((Set<Association>) method.invoke(reducedFormRevesionService, associations,
                           possibility));
               } catch (IllegalArgumentException e) {
                  logger.error(e.getMessage(), e);
                  throw new NLPSystemException(NLPExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
               } catch (IllegalAccessException e) {
                  logger.error(e.getMessage(), e);
                  throw new NLPSystemException(NLPExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
               } catch (InvocationTargetException e) {
                  logger.error(e.getMessage(), e);
                  throw new NLPSystemException(NLPExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
               }
            }
         }
         associations.clear();
         associations.addAll(associationSet);
         return associations;
      } else {
         return associations;
      }
   }

   private double calculateAverageRecognitionWeightForPossibility (Possibility possibility, int userQueryTokenCount) {
      double sumWeight = 0.0;
      for (IWeightedEntity recEntity : possibility.getRecognitionEntities()) {
         if (((RecognitionEntity) recEntity).getPosition() >= 0) {
            double finalWeight = recEntity.getWeightInformation().getFinalWeight();
            sumWeight = sumWeight + finalWeight;
         }

      }
      double averageRecognitionWeight = sumWeight / userQueryTokenCount;
      return averageRecognitionWeight;
   }

   /**
    * @param allUnAmbiguousPossibilities
    */
   private void removeUnconnectedTimeFrameIfAtleastOneTFIsConnected (List<Possibility> allUnAmbiguousPossibilities) {
      for (Possibility possibility : allUnAmbiguousPossibilities) {
         List<RecognitionEntity> unconnectedTFs = new ArrayList<RecognitionEntity>(1);
         List<RecognitionEntity> connectedTFs = new ArrayList<RecognitionEntity>(1);
         for (IWeightedEntity weightedEntity : possibility.getRecognitionEntities()) {
            TypeEntity typeEntity = (TypeEntity) weightedEntity;
            if (typeEntity.getTypeDisplayName().equals(RecognizedType.TF_TYPE.getValue())) {
               boolean isConnected = false;
               for (Association association : possibility.getAssociations()) {
                  RecognitionEntity recognitionEntity = association.getPathComponent().get(
                           association.getPathComponent().size() - 1);
                  if (typeEntity.equals(recognitionEntity)) {
                     isConnected = true;
                     break;
                  }
               }
               if (isConnected) {
                  connectedTFs.add(typeEntity);
               } else {
                  unconnectedTFs.add(typeEntity);
               }
            }
         }
         if (!CollectionUtils.isEmpty(connectedTFs) && !CollectionUtils.isEmpty(unconnectedTFs)) {
            possibility.getRecognitionEntities().removeAll(unconnectedTFs);
         } else if (!CollectionUtils.isEmpty(unconnectedTFs) && unconnectedTFs.size() > 1) {
            Collections.sort(unconnectedTFs, new Comparator<RecognitionEntity>() {

               public int compare (RecognitionEntity o1, RecognitionEntity o2) {
                  return (int) (o2.getWeightInformation().getFinalWeight() - o1.getWeightInformation().getFinalWeight());
               }

            });
            RecognitionEntity recEntity = unconnectedTFs.get(0);
            possibility.getRecognitionEntities().removeAll(unconnectedTFs);
            possibility.getRecognitionEntities().add(recEntity);
         }
      }

   }

   /**
    * Method to log the association information
    * 
    * @param modelBasedPossibilityList
    */
   private void logAssociationsBeforeUnambiguousPossibilityCreation (Possibility possibility) {
      if (logger.isDebugEnabled()) {
         StringBuffer sb = new StringBuffer();
         sb.append("\n\nAssociations Before Unambiguous Possibility Generation");
         sb.append("\nPossibility Id: ").append(possibility.getId());
         List<Association> associations = possibility.getAssociations();
         if (!CollectionUtils.isEmpty(associations)) {
            for (Association association : associations) {
               sb.append("\n\t\t").append(association);
            }
         }
         logger.debug(sb.toString());
      }
   }

   /**
    * Method to log the association information
    * 
    * @param modelBasedPossibilityList
    */
   private void logAssociationsAfterUnambiguousPossibilityCreation (List<Possibility> modelBasedPossibilityList) {
      if (logger.isDebugEnabled()) {
         StringBuffer sb = new StringBuffer();
         sb.append("\n\nAssociations After Unambiguous Possibility Generation");
         for (Possibility possibility : modelBasedPossibilityList) {
            sb.append("\nPossibility Id: ").append(possibility.getId());
            List<Association> associations = possibility.getAssociations();
            if (!CollectionUtils.isEmpty(associations)) {
               for (Association association : associations) {
                  sb.append("\n\t\t").append(association);
               }
            }
         }
         logger.debug(sb.toString());
      }
   }

   /**
    * Method to create and set the recognition entities for the given possibility
    * 
    * @param possibility
    */
   public void updatPossibilityWithSemanticScopingOutput (Possibility possibility) {

      // Massage the data, i.e. set the onto recognition entities for the possibility
      List<IWeightedEntity> ontoRecognitionEntities = new ArrayList<IWeightedEntity>();
      List<IWeightedEntity> basePunctuationEntities = new ArrayList<IWeightedEntity>();

      for (IWeightedEntity weightedEntity : possibility.getRecognitionEntities()) {
         if (weightedEntity instanceof OntoEntity) {
            TypeEntity typeEntity = (TypeEntity) weightedEntity;
            if (typeEntity.getTypeDisplayName().equals(ExecueConstants.PUNCTUATION_TYPE)) {
               basePunctuationEntities.add(typeEntity);
            } else if (!ontoRecognitionEntities.contains(weightedEntity)) {
               ontoRecognitionEntities.add(weightedEntity);
            }
         }
      }

      // Filter the sub set rec entities. 
      // Default version is provided here(just returns same list) but can be over ridden for specific app level summarization service
      filterSubSetOntoEntities(ontoRecognitionEntities);

      // Set the respective recognition entities of the possibility
      possibility.setRecognitionEntities(ontoRecognitionEntities);
      possibility.setUnrecognizedBaseRecEntities(basePunctuationEntities);
   }

   /**
    * filter the sub set rec entities . default version is provides in this css can be over ridden for specific // apps
    * 
    * @param entities
    * @return
    */
   public List<IWeightedEntity> filterSubSetOntoEntities (List<IWeightedEntity> entities) {
      return entities;
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
    * @return the reducedFormRevesionService
    */
   public IReducedFormRevesionService getReducedFormRevesionService () {
      return reducedFormRevesionService;
   }

   /**
    * @param reducedFormRevesionService
    *           the reducedFormRevesionService to set
    */
   public void setReducedFormRevesionService (IReducedFormRevesionService reducedFormRevesionService) {
      this.reducedFormRevesionService = reducedFormRevesionService;
   }

   /**
    * @return the nlpConfiguration
    */
   public INLPConfigurationService getNlpConfiguration () {
      return nlpConfiguration;
   }

   /**
    * @param nlpConfiguration
    *           the nlpConfiguration to set
    */
   public void setNlpConfiguration (INLPConfigurationService nlpConfiguration) {
      this.nlpConfiguration = nlpConfiguration;
   }
}