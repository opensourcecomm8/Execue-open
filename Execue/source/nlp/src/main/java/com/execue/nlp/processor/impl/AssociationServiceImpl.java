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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.type.PathSelectionType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.nlp.bean.ProcessorInput;
import com.execue.nlp.bean.association.AssociationPath;
import com.execue.nlp.bean.entity.AppCloudEntity;
import com.execue.nlp.bean.entity.ConceptEntity;
import com.execue.nlp.bean.entity.ConceptProfileEntity;
import com.execue.nlp.bean.entity.EntityFactory;
import com.execue.nlp.bean.entity.InstanceEntity;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.RecognizedCloudEntity;
import com.execue.nlp.bean.entity.ReferedTokenPosition;
import com.execue.nlp.bean.entity.TypeEntity;
import com.execue.nlp.processor.IAssociationService;
import com.execue.nlp.type.AssociationDirectionType;
import com.execue.nlp.util.NLPUtilities;
import com.execue.ontology.exceptions.OntologyException;
import com.execue.ontology.service.IOntologyService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXRetrievalService;

/**
 * @author Nitesh
 */
public class AssociationServiceImpl implements IAssociationService {

   private static Logger        logger = Logger.getLogger(AssociationServiceImpl.class);
   private IOntologyService     ontologyService;
   private IKDXRetrievalService kdxRetrievalService;

   public List<AssociationPath> getPathDefinitions (List<IWeightedEntity> weightedEntities) throws OntologyException {
      List<AssociationPath> pathDefs = new ArrayList<AssociationPath>(1);

      Set<Long> pathBedIds = NLPUtilities.getAllBedIdsForConceptTypeEntity(weightedEntities);

      // Return if the BED list is empty
      if (ExecueCoreUtil.isCollectionEmpty(pathBedIds)) {
         return pathDefs;
      }

      // Get all the path definitions where source or destination bed id is any of the path bed id
      Map<String, List<PathDefinition>> sourceDestPathDefMap = getOntologyService()
               .getAllNonParentPathsForSourceAndDestination(pathBedIds, pathBedIds);

      if (CollectionUtils.isEmpty(sourceDestPathDefMap.keySet())) {
         return pathDefs;
      }

      // Populate the association path definitions
      pathDefs = populateAssociationPaths(weightedEntities, sourceDestPathDefMap);

      // Filter the subset paths
      filterSubsetPathDefs(pathDefs);

      return pathDefs;
   }

   private List<AssociationPath> populateAssociationPaths (List<IWeightedEntity> weightedEntities,
            Map<String, List<PathDefinition>> sourceDestPathDefMap) {
      int tokensSize = weightedEntities.size();
      List<AssociationPath> pathDefs = new ArrayList<AssociationPath>(1);
      // loop through to find path triples
      for (int first = 0; first < tokensSize - 1; first++) {
         RecognitionEntity leftRec = (RecognitionEntity) weightedEntities.get(first);
         // TODO as of now skipping property entity needs to populate again once Ri path is done.
         if (leftRec.getEntityType() == RecognitionEntityType.PROPERTY_ENTITY) {
            continue;
         }
         boolean leftIsAttribute = ((OntoEntity) leftRec).isAttributeType();
         boolean leftIsConcept = false;
         Long leftTypeBedId = null;
         Long leftResourceID = null;
         if (leftRec.getEntityType() == RecognitionEntityType.CONCEPT_PROFILE_ENTITY) {
            leftIsConcept = true;
            leftResourceID = ((ConceptProfileEntity) leftRec).getProfileID();
            leftTypeBedId = ((TypeEntity) leftRec).getTypeBedId();
         } else if (leftRec.getEntityType() == RecognitionEntityType.TYPE_ENTITY
                  || ((ConceptEntity) leftRec).getConceptBedId() == null) {
            leftResourceID = ((TypeEntity) leftRec).getTypeBedId();
            leftTypeBedId = ((TypeEntity) leftRec).getTypeBedId();
         } else {
            leftIsConcept = true;
            leftResourceID = ((ConceptEntity) leftRec).getConceptBedId();
            leftTypeBedId = ((TypeEntity) leftRec).getTypeBedId();
         }

         for (int second = first + 1; second < tokensSize; second++) {
            RecognitionEntity rightRec = (RecognitionEntity) weightedEntities.get(second);
            if (rightRec.getEntityType() == RecognitionEntityType.PROPERTY_ENTITY) {
               continue;
            }
            // Skip recognitions if the position overlaps as can't be part of same possibility and hence association is
            // not possible
            ReferedTokenPosition rtpSource = new ReferedTokenPosition(leftRec.getReferedTokenPositions());
            ReferedTokenPosition rtpDest = new ReferedTokenPosition(rightRec.getReferedTokenPositions());
            if (rtpSource.isOverLap(rtpDest)) {
               continue;
            }

            boolean rightIsAttribute = ((OntoEntity) rightRec).isAttributeType();
            boolean rightIsConcept = false;
            Long rightResourceID = null;
            Long rightTypeBedId = null;
            if (rightRec.getEntityType() == RecognitionEntityType.CONCEPT_PROFILE_ENTITY) {
               rightIsConcept = true;
               rightResourceID = ((ConceptProfileEntity) rightRec).getProfileID();
               rightTypeBedId = ((TypeEntity) rightRec).getTypeBedId();
            } else if (rightRec.getEntityType() == RecognitionEntityType.TYPE_ENTITY
                     || ((ConceptEntity) rightRec).getConceptBedId() == null) {
               rightTypeBedId = ((TypeEntity) rightRec).getTypeBedId();
               rightResourceID = ((TypeEntity) rightRec).getTypeBedId();
            } else {
               rightIsConcept = true;
               rightTypeBedId = ((TypeEntity) rightRec).getTypeBedId();
               rightResourceID = ((ConceptEntity) rightRec).getConceptBedId();
            }

            if (leftIsAttribute && rightIsAttribute) {
               // No Path Defs Possible between two attributes
               continue;
            } else if (leftIsAttribute && rightIsConcept || leftIsConcept && rightIsAttribute) {
               if (leftIsAttribute) {
                  String key = rightResourceID + "-" + leftResourceID;
                  if (sourceDestPathDefMap.containsKey(key)) {
                     for (PathDefinition path : sourceDestPathDefMap.get(key)) {
                        AssociationPath assocPath = getAssociationPath(leftRec, rightRec, path,
                                 AssociationDirectionType.RIGHT_ASSOCIATION);
                        boolean proceed = validateAssociationForRelations(assocPath, rtpDest, rtpSource,
                                 weightedEntities);
                        if (proceed) {
                           pathDefs.add(assocPath);
                        }
                     }
                  } else {
                     // Check if path exists between rightResourceID and left type be id
                     key = rightResourceID + "-" + leftTypeBedId;
                     if (sourceDestPathDefMap.containsKey(key)) {
                        for (PathDefinition path : sourceDestPathDefMap.get(key)) {
                           AssociationPath assocPath = getAssociationPath(leftRec, rightRec, path,
                                    AssociationDirectionType.RIGHT_ASSOCIATION);
                           boolean proceed = validateAssociationForRelations(assocPath, rtpDest, rtpSource,
                                    weightedEntities);
                           if (proceed) {
                              pathDefs.add(assocPath);
                           }
                        }
                     }
                  }
               } else if (rightIsAttribute) {
                  String key = leftResourceID + "-" + rightResourceID;
                  if (sourceDestPathDefMap.containsKey(key)) {
                     for (PathDefinition path : sourceDestPathDefMap.get(key)) {
                        AssociationPath assocPath = getAssociationPath(rightRec, leftRec, path,
                                 AssociationDirectionType.LEFT_ASSOCIATION);
                        boolean proceed = validateAssociationForRelations(assocPath, rtpSource, rtpDest,
                                 weightedEntities);
                        if (proceed) {
                           pathDefs.add(assocPath);
                        }
                     }

                  } else {
                     // Check if path exists between leftResourceID and right type be id
                     key = leftResourceID + "-" + rightTypeBedId;
                     if (sourceDestPathDefMap.containsKey(key)) {
                        for (PathDefinition path : sourceDestPathDefMap.get(key)) {
                           AssociationPath assocPath = getAssociationPath(rightRec, leftRec, path,
                                    AssociationDirectionType.LEFT_ASSOCIATION);
                           boolean proceed = validateAssociationForRelations(assocPath, rtpSource, rtpDest,
                                    weightedEntities);
                           if (proceed) {
                              pathDefs.add(assocPath);
                           }
                        }
                     }
                  }
               }
            } else if (leftIsConcept && rightIsConcept) {
               // This is the place to process CRC Paths
               // Also now that we know what Source and Destination in user query are being connected
               // we can check for validity of these paths w.r.t. relation present in them i.e.
               // 1. Check for any user specified property appearing in between giving us clue as in
               // which path out of multiple paths to be chosen
               List<AssociationPath> assocPaths = getCCPathDefinitions(leftResourceID, rightResourceID,
                        sourceDestPathDefMap);
               // We do this property present validation only if multiple paths are present.
               for (AssociationPath assocPath : assocPaths) {
                  if (AssociationDirectionType.LEFT_ASSOCIATION.equals(assocPath.getDirection())) {
                     boolean proceed = validateAssociationForRelations(assocPath, rtpSource, rtpDest, weightedEntities);
                     if (proceed) {
                        assocPath.setSource(leftRec);
                        assocPath.setDestination(rightRec);
                        pathDefs.add(assocPath);
                     }
                  } else if (AssociationDirectionType.RIGHT_ASSOCIATION.equals(assocPath.getDirection())) {
                     boolean proceed = validateAssociationForRelations(assocPath, rtpDest, rtpSource, weightedEntities);
                     if (proceed) {
                        assocPath.setSource(rightRec);
                        assocPath.setDestination(leftRec);
                        pathDefs.add(assocPath);
                     }
                  }
               }
            }
         }
      }
      return pathDefs;
   }

   protected boolean validateAssociationForRelations (AssociationPath assocPath, ReferedTokenPosition rtpSource,
            ReferedTokenPosition rtpDest, List<IWeightedEntity> weightedEntities) {
      return true;
   }

   private void filterSubsetPathDefs (List<AssociationPath> pathDefs) {
      if (CollectionUtils.isEmpty(pathDefs)) {
         return;
      }
      List<AssociationPath> pathsToRemove = new ArrayList<AssociationPath>(1);
      for (AssociationPath associationPath : pathDefs) {
         List<AssociationPath> subsetPaths = new ArrayList<AssociationPath>(1);
         for (AssociationPath pathDef : pathDefs) {
            if (associationPath == pathDef) {
               continue;
            }
            if (pathDef.isSubsetOf(associationPath)) {
               subsetPaths.add(pathDef);
            }
         }
         List<EntityTripleDefinition> subsetEtds = new ArrayList<EntityTripleDefinition>(1);
         for (AssociationPath path : subsetPaths) {
            subsetEtds.addAll(path.getETDs());
         }
         if (subsetEtds.containsAll(associationPath.getETDs())) {
            pathsToRemove.add(associationPath);
         } else {
            pathsToRemove.addAll(subsetPaths);
         }
      }
      pathDefs.removeAll(pathsToRemove);
   }

   /**
    * @param leftRec
    * @param rightRec
    * @param path
    * @return
    */
   private AssociationPath getAssociationPath (RecognitionEntity leftRec, RecognitionEntity rightRec,
            PathDefinition path, AssociationDirectionType direction) {
      AssociationPath assocPath = new AssociationPath();
      assocPath.setPath(path);
      assocPath.setDirection(direction);
      assocPath.setSource(rightRec);
      assocPath.setDestination(leftRec);
      return assocPath;
   }

   private List<AssociationPath> getCCPathDefinitions (Long leftResourceID, Long rightResourceID,
            Map<String, List<PathDefinition>> sourceDestPathDefMap) {
      List<AssociationPath> associationPaths = new ArrayList<AssociationPath>();
      List<PathDefinition> paths = null;
      String key = leftResourceID + "-" + rightResourceID;
      if (sourceDestPathDefMap.containsKey(key)) {
         paths = getShortestPaths(sourceDestPathDefMap.get(key));
         for (PathDefinition path : paths) {
            AssociationPath associationPath = new AssociationPath();
            associationPath.setPath(path);
            associationPath.setDirection(AssociationDirectionType.LEFT_ASSOCIATION);
            associationPaths.add(associationPath);
         }
      } else {
         key = rightResourceID + "-" + leftResourceID;
         if (sourceDestPathDefMap.containsKey(key)) {
            paths = getShortestPaths(sourceDestPathDefMap.get(key));
            for (PathDefinition path : paths) {
               AssociationPath associationPath = new AssociationPath();
               associationPath.setPath(path);
               associationPath.setDirection(AssociationDirectionType.RIGHT_ASSOCIATION);
               associationPaths.add(associationPath);
            }
         }
      }
      return associationPaths;
   }

   // TODO: -JM-
   // currently in the case of multiple short paths, returning the first in the list.
   // Need to change it so that the best shortest path is returned
   protected List<PathDefinition> getShortestPaths (List<PathDefinition> pathDefinitions) {
      Map<Integer, List<PathDefinition>> pathMap = new HashMap<Integer, List<PathDefinition>>();
      for (PathDefinition pathDef : pathDefinitions) {
         List<PathDefinition> pathList = pathMap.get(pathDef.getPathLength());
         if (ExecueCoreUtil.isCollectionEmpty(pathList)) {
            pathList = new ArrayList<PathDefinition>();
            pathList.add(pathDef);
            pathMap.put(pathDef.getPathLength(), pathList);
         } else {
            pathList.add(pathDef);
         }
      }
      Set<Integer> keySet = pathMap.keySet();
      List<Integer> keyList = new ArrayList<Integer>(keySet);
      Collections.sort(keyList);
      List<PathDefinition> paths = new ArrayList<PathDefinition>();
      paths.add(pathMap.get(keyList.get(0)).get(0));
      return paths;
   }

   public void populateDefaultPaths (AppCloudEntity appCloudEntity, ProcessorInput processorInput,
            List<InstanceEntity> unAssociatedAttributes, Set<Long> beIds) throws OntologyException, KDXException {

      // Get all the path definitions
      Map<Long, PathDefinition> defaulPathByDestId = getOntologyService()
               .getNonParentPathsByDestIdAndPathSelectionType(beIds, PathSelectionType.DEFAULT_VALUE_PATH,
                        appCloudEntity.getModelGroupId());
      Set<Long> existingBedIds = NLPUtilities.getAllBedIdsForConceptTypeEntity(appCloudEntity.getRecognitionEntities());

      for (Long conceptBedId : beIds) {
         PathDefinition path = defaulPathByDestId.get(conceptBedId);
         // added chck to see if default Path does not exist in Model
         if (path == null) {
            continue;
         }
         Long sourceBedId = path.getSourceBusinessEntityDefinition().getId();
         //Check if source is already present in the cloud entities, then skip the default value path addition
         if (existingBedIds.contains(sourceBedId)) {
            continue;
         }
         Set<Long> conceptIds = new HashSet<Long>(1);
         conceptIds.add(sourceBedId);

         Map<Long, RIOntoTerm> conceptOntoTermsByConceptBedIds = getKdxRetrievalService()
                  .getConceptOntoTermsByConceptBedIds(conceptIds);
         if (conceptOntoTermsByConceptBedIds != null && conceptOntoTermsByConceptBedIds.size() > 0) {
            RecognitionEntity defaultAssociatedConceptRecognition = null;
            for (RIOntoTerm ontoTerm : conceptOntoTermsByConceptBedIds.values()) {
               defaultAssociatedConceptRecognition = addDefaultAssociatedConceptRecognition(appCloudEntity,
                        processorInput.getNextImplicitRecognitionCounter(), ontoTerm);
               appCloudEntity.addRecognitionEntity(defaultAssociatedConceptRecognition);
               break;
            }
            for (InstanceEntity instanceEntity : unAssociatedAttributes) {
               if (path.getDestinationBusinessEntityDefinition().getId().equals(instanceEntity.getConceptBedId())) {
                  AssociationPath associationPath = getAssociationPath(instanceEntity,
                           defaultAssociatedConceptRecognition, path, AssociationDirectionType.LEFT_ASSOCIATION);
                  associationPath.setWeightInformation(NLPUtilities.getDefaultWeightInformation());
                  associationPath.setDefaultPath(true);
                  appCloudEntity.getPaths().add(associationPath);
               }
            }
         }
      }
   }

   private RecognitionEntity addDefaultAssociatedConceptRecognition (RecognizedCloudEntity cloudEntity, int pos,
            RIOntoTerm ontoTerm) {
      RecognitionEntity entity = EntityFactory.getRecognitionEntity(RecognitionEntityType.CONCEPT_ENTITY, ontoTerm
               .getConceptName(), NLPConstants.NLP_TAG_ONTO_CONCEPT, null, pos);
      entity.addReferedTokenPosition(pos);
      entity.addOriginalReferedTokenPosition(pos);
      entity.setLevel(3);
      entity.setWeightInformation(NLPUtilities.getDefaultWeightInformationForImplicitEntity());
      entity.setWord(ontoTerm.getConceptName());
      ((TypeEntity) entity).setTypeBedId(ontoTerm.getTypeBEDID());
      ((TypeEntity) entity).setTypeDisplayName(ontoTerm.getTypeName());
      ((ConceptEntity) entity).setConceptDisplayName(ontoTerm.getConceptName());
      ((ConceptEntity) entity).setPopularity(ontoTerm.getPopularity());
      ((ConceptEntity) entity).setConceptBedId(ontoTerm.getConceptBEDID());
      ((ConceptEntity) entity).setModelGroupId(cloudEntity.getModelGroupId());
      entity.setStartPosition(pos);
      entity.setEndPosition(pos);
      entity.setPosition(pos);
      return entity;
   }

   /**
    * @return the ontologyService
    */
   public IOntologyService getOntologyService () {
      return ontologyService;
   }

   /**
    * @param ontologyService
    *           the ontologyService to set
    */
   public void setOntologyService (IOntologyService ontologyService) {
      this.ontologyService = ontologyService;
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
}