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


package com.execue.nlp.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.graph.Graph;
import com.execue.core.common.bean.graph.GraphComponentType;
import com.execue.core.common.bean.graph.GraphPath;
import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.common.bean.nlp.RecognitionEntityType;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.nlp.WordRecognitionState;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.nlp.bean.entity.Association;
import com.execue.nlp.bean.entity.ConceptEntity;
import com.execue.nlp.bean.entity.ConceptProfileEntity;
import com.execue.nlp.bean.entity.InstanceEntity;
import com.execue.nlp.bean.entity.InstanceProfileEntity;
import com.execue.nlp.bean.entity.OntoEntity;
import com.execue.nlp.bean.entity.ProfileEntity;
import com.execue.nlp.bean.entity.PropertyEntity;
import com.execue.nlp.bean.entity.RecognitionEntity;
import com.execue.nlp.bean.entity.ReferedTokenPosition;
import com.execue.nlp.bean.entity.TypeEntity;
import com.execue.nlp.bean.matrix.Possibility;
import com.execue.nlp.util.NLPUtilities;
import com.execue.swi.service.IKDXRetrievalService;

/**
 * @author Abhijit
 * @since Sep 25, 2008 : 4:23:29 PM
 */
public class ReducedFormGenerator {

   private static final Logger  log = Logger.getLogger(ReducedFormGenerator.class);

   private IKDXRetrievalService kdxRetrievalService;

   public SemanticPossibility generateReducedForm (Possibility possibility, Integer sentenceId) {
      SemanticPossibility reducedForm = new SemanticPossibility();
      List<IWeightedEntity> recEntities = possibility.getRecognitionEntities();
      if (CollectionUtils.isEmpty(recEntities)) {
         return reducedForm;
      }
      Map<ReferedTokenPosition, DomainRecognition> recognitionByRTP = new HashMap<ReferedTokenPosition, DomainRecognition>(
               1);
      Map<Integer, WordRecognitionState> wordRecognitionStates = new HashMap<Integer, WordRecognitionState>();
      for (IWeightedEntity recEntity : recEntities) {
         WordRecognitionState recState = new WordRecognitionState();
         // Get Default Recognition Entity
         RecognitionEntity entity = (RecognitionEntity) recEntity;
         if (entity instanceof OntoEntity) {
            DomainRecognition rec = getRecognition(entity, reducedForm.getReducedForm());
            Long modelGroupId = ((OntoEntity) entity).getModelGroupId();
            if (entity instanceof ConceptEntity) {
               ConceptEntity conceptEntity = (ConceptEntity) entity;
               if (conceptEntity.getConceptDisplayName() != null) {
                  recState.setConceptName(ExecueStringUtil.addSpaceAtUpperCase(conceptEntity.getConceptDisplayName()));
               }
            }
            if (entity instanceof InstanceEntity) {
               InstanceEntity instanceEntity = (InstanceEntity) entity;
               String instanceName = instanceEntity.getInstanceDisplayString();
               recState.setInstanceName(instanceName);
               // populate the conjunction type if the entity is an instance of conjunction
               populateConjunctionType(recState, entity);
            }
            // info for suppressing the base concept's info on screen
            // -JM- 04-OCT-2011: Bye-pass this rule for Value based types
            if (modelGroupId == null || modelGroupId == 1) {
               ConceptEntity cEntity = (ConceptEntity) entity;
               recState.setBaseConcept(true);
               if (cEntity.getTypeBedId().longValue() == ExecueConstants.VALUE_TYPE_BED_ID.longValue()) {
                  recState.setBaseConcept(false);
               }
            }
            recognitionByRTP.put(new ReferedTokenPosition(entity.getReferedTokenPositions()), rec);
         }
         recState.setRecognitionType(entity.getNLPtag());
         // iterate through the original referred token positions
         for (Integer originalPosition : entity.getOriginalReferedTokenPositions()) {
            wordRecognitionStates.put(originalPosition, recState);
         }
      }
      Set<ReferedTokenPosition> positionsAdded = new HashSet<ReferedTokenPosition>(1);
      DomainRecognition node;
      sortAssociationByEndPosition(possibility.getAssociations());
      for (Association association : possibility.getAssociations()) {
         GraphPath path = new GraphPath();
         path.setValidateInstanceTriple(association.isInstanceTripleExist());
         path.setSentenceId(sentenceId);
         List<RecognitionEntity> pathComponents = association.getPathComponent();
         for (int i = 0; i < pathComponents.size() - 1;) {
            RecognitionEntity source = pathComponents.get(i);
            DomainRecognition sourceNode = getRecognition(source, reducedForm.getReducedForm());
            sourceNode.setSentenceId(sentenceId);
            sourceNode.setType(GraphComponentType.Vertex);
            path.addPathComponent(sourceNode);
            addVertexComponent(reducedForm, sourceNode, positionsAdded, new ReferedTokenPosition(source
                     .getReferedTokenPositions()), null);
            RecognitionEntity relation = pathComponents.get(++i);
            DomainRecognition relationNode = getRecognition(relation, reducedForm.getReducedForm());
            relationNode.setType(GraphComponentType.Edge);
            relationNode.setSentenceId(sentenceId);
            path.addPathComponent(relationNode);
            addEdgeComponent(reducedForm, relationNode, positionsAdded, new ReferedTokenPosition(relation
                     .getReferedTokenPositions()), sourceNode);
            RecognitionEntity destination = pathComponents.get(++i);
            DomainRecognition destNode = getRecognition(destination, reducedForm.getReducedForm());
            destNode.setSentenceId(sentenceId);
            destNode.setType(GraphComponentType.Vertex);
            path.addPathComponent(destNode);
            addVertexComponent(reducedForm, destNode, positionsAdded, new ReferedTokenPosition(destination
                     .getReferedTokenPositions()), relationNode);
            // set the isAssociated flag
            WordRecognitionState wordRecognitionState = null;
            wordRecognitionState = wordRecognitionStates.get(source.getPosition());
            if (wordRecognitionState != null) {
               wordRecognitionState.setAssociated(true);
            }
            wordRecognitionState = wordRecognitionStates.get(destination.getPosition());
            if (wordRecognitionState != null) {
               wordRecognitionState.setAssociated(true);
            }
         }
         reducedForm.getReducedForm().addPath(path);
      }
      // process the WRS for the unrecognized entities if available in the possibility
      List<IWeightedEntity> unrecognizedEntities = possibility.getUnrecognizedEntities();
      if (ExecueCoreUtil.isCollectionNotEmpty(unrecognizedEntities)) {
         for (IWeightedEntity uEntity : unrecognizedEntities) {
            RecognitionEntity unrecogEntity = (RecognitionEntity) uEntity;
            WordRecognitionState wordRecogState = new WordRecognitionState();
            wordRecogState.setPosType(unrecogEntity.getNLPtag());
            wordRecogState.setValidPosType(NLPUtilities.checkValidPosType(wordRecogState.getPosType()));
            wordRecognitionStates.put(unrecogEntity.getPosition(), wordRecogState);
         }
      }

      for (Map.Entry<ReferedTokenPosition, DomainRecognition> record : recognitionByRTP.entrySet()) {
         if (!positionsAdded.contains(record.getKey())) {
            node = record.getValue();
            if (isNodeToBeAdded(node)) {
               node.setType(GraphComponentType.Vertex);
               node.setSentenceId(sentenceId);
               reducedForm.getReducedForm().addGraphComponent(node, null);
            }
         }
      }
      reducedForm.setWordRecognitionStates(wordRecognitionStates);
      return reducedForm;
   }

   /**
    * Method to check if the node should be added to RF will not add the attribute entities to RF if they are not
    * connected.
    * 
    * @param recognition
    * @return the boolean true/false
    */
   private boolean isNodeToBeAdded (DomainRecognition recognition) {
      return !ExecueConstants.CONJUNCTION_TYPE.equals(recognition.getTypeName())
               && !ExecueConstants.COORDINATING_CONCJUNCTION_TYPE.equals(recognition.getTypeName())
               && !ExecueConstants.BY_CONJUNCTION_TYPE.equals(recognition.getTypeName())
               && !ExecueConstants.ADJECTIVE_TYPE.equals(recognition.getTypeName())
               && !ExecueConstants.PREPOSITION_TYPE.equals(recognition.getTypeName())
               && !ExecueConstants.COMPARATIVE_STATISTICS_TYPE.equals(recognition.getTypeName())
               && !ExecueConstants.UNIT_SYMBOL_TYPE.equals(recognition.getTypeName())
               && !ExecueConstants.UNIT_SCALE_TYPE.equals(recognition.getTypeName())
               && !ExecueConstants.OPERATOR_TYPE.equals(recognition.getTypeName())
               && !ExecueConstants.VALUE_TYPE.equals(recognition.getTypeName())
               && !ExecueConstants.VALUE_PREPOSITION_TYPE.equals(recognition.getTypeName());

   }

   private void sortAssociationByEndPosition (List<Association> associations) {
      Collections.sort(associations, new Comparator<Association>() {

         public int compare (Association a1, Association a2) {
            RecognitionEntity endEntity1 = a1.getPathComponent().get(a1.getPathComponent().size() - 1);
            RecognitionEntity endEntity2 = a2.getPathComponent().get(a2.getPathComponent().size() - 1);
            return endEntity1.getPosition().compareTo(endEntity2.getPosition());
         }

      });

   }

   private void populateConjunctionType (WordRecognitionState recState, RecognitionEntity entity) {
      // TODO: -JM- move to configuration to load the conjunctions
      List<String> conjunctionTypes = new ArrayList<String>();
      conjunctionTypes.add("Conjunction");
      conjunctionTypes.add("Preposition");
      conjunctionTypes.add("ByConjunction");
      conjunctionTypes.add("SubordinatingConjunction");
      conjunctionTypes.add("CoordinatingConjunction");
      conjunctionTypes.add("RangePreposition");
      InstanceEntity instanceEntity = (InstanceEntity) entity;
      String typeDisplayName = instanceEntity.getTypeDisplayName();
      if (ExecueCoreUtil.isNotEmpty(typeDisplayName) && conjunctionTypes.contains(typeDisplayName)) {
         recState.setConjunctionType(typeDisplayName);
      }
   }

   private DomainRecognition getRecognition (RecognitionEntity entity, Graph graph) {
      String businessEntityName = getBusinessEntityName(entity);
      if (entity.getEntityType() == RecognitionEntityType.PROPERTY_ENTITY) {
         String key = businessEntityName;
         if (!MapUtils.isEmpty(graph.getEdges()) && graph.getEdges().containsKey(key)) {
            return (DomainRecognition) graph.getEdges().get(key);
         }
      } else {
         String key = businessEntityName + ((ConceptEntity) entity).getConceptDisplayName() + "-"
                  + entity.getPosition();
         if (!MapUtils.isEmpty(graph.getVertices()) && graph.getVertices().containsKey(key)) {
            return (DomainRecognition) graph.getVertices().get(key);
         }
      }

      DomainRecognition rec = new DomainRecognition();
      rec.setWeightInformation(entity.getWeightInformation());
      rec.setPosition(entity.getPosition());
      rec.setNlpTag(entity.getNLPtag());
      rec.setOriginalPositions(entity.getOriginalReferedTokenPositions());
      rec.setGroupId(entity.getGroupId());
      rec.setFlags(entity.getFlags());
      rec.setBehaviors(((OntoEntity) entity).getBehaviors());
      rec.setKnowledgeId(((OntoEntity) entity).getKnowledgeId());
      rec.setTypeName(((TypeEntity) entity).getTypeDisplayName());
      rec.setTypeBEDId(((TypeEntity) entity).getTypeBedId());
      rec.setAlternateBedIds(entity.getAlternateBedIds());
      // TODO -NA- set the final Weight of recognition entity.
      // rec.setWeight(entity.getWeightInformation().getFinalWeight());
      if (entity.getEntityType() == RecognitionEntityType.PROPERTY_ENTITY) {
         rec.setRelationBEDId(((PropertyEntity) entity).getRelationBedId());
         rec.setRelationDisplayName(((PropertyEntity) entity).getRelationDisplayName());
      } else {

         if (entity instanceof ConceptEntity) {
            rec.setConceptName(((ConceptEntity) entity).getConceptDisplayName());
         }
         if (entity.getEntityType() == RecognitionEntityType.CONCEPT_PROFILE_ENTITY) {
            rec.setProfileName(((ConceptProfileEntity) entity).getProfileName());
            rec.setProfileBedId(((ConceptProfileEntity) entity).getProfileID());
         }
         rec.setConceptDisplayName(((ConceptEntity) entity).getConceptDisplayName());
         rec.setConceptBEDId(((ConceptEntity) entity).getConceptBedId());
         // For Instance and Profile Entities additional information is to be added to Domain Recognition
         if (entity instanceof InstanceEntity) {
            // As Conjunctions are also Instances, we need to set the Conjunction Type also
            InstanceEntity instanceEntity = (InstanceEntity) entity;
            rec.setInstanceInformations(instanceEntity.getInstanceInformations());
            // set the instance name into WRS
            rec.setNormalizedData(entity.getNormalizedData());
         } else if (entity.getEntityType() == RecognitionEntityType.INSTANCE_PROFILE_ENTITY) {
            rec.setProfileName(((ProfileEntity) entity).getProfileName());
            rec.setProfileBedId(((ProfileEntity) entity).getProfileID());
         }
      }
      return rec;
   }

   /**
    * @param entity
    * @return the String business entity name
    */
   private String getBusinessEntityName (RecognitionEntity entity) {
      String businessEntityName = null;
      if (entity.getEntityType() == RecognitionEntityType.PROPERTY_ENTITY) {
         businessEntityName = ((PropertyEntity) entity).getRelationDisplayName();
      } else if (entity.getEntityType() == RecognitionEntityType.CONCEPT_ENTITY) {
         businessEntityName = ((ConceptEntity) entity).getConceptDisplayName();

      } else if (entity.getEntityType() == RecognitionEntityType.INSTANCE_ENTITY) {
         businessEntityName = ((InstanceEntity) entity).getDefaultInstanceDisplayName();
         if (businessEntityName == null) {
            businessEntityName = ((InstanceEntity) entity).getDefaultInstanceValue();
         }
      } else if (entity.getEntityType() == RecognitionEntityType.CONCEPT_PROFILE_ENTITY) {
         businessEntityName = ((ConceptProfileEntity) entity).getProfileName();
      } else if (entity.getEntityType() == RecognitionEntityType.INSTANCE_PROFILE_ENTITY) {
         businessEntityName = ((InstanceProfileEntity) entity).getProfileName();
      } else {
         businessEntityName = ((TypeEntity) entity).getTypeDisplayName();
      }
      return businessEntityName;
   }

   private DomainRecognition addVertexComponent (SemanticPossibility reducedForm, DomainRecognition node,
            Set<ReferedTokenPosition> positionsAdded, ReferedTokenPosition referedTokenPosition,
            DomainRecognition prevNode) {
      reducedForm.getReducedForm().addGraphComponent(node, prevNode);
      positionsAdded.add(referedTokenPosition);
      return node;
   }

   private DomainRecognition addEdgeComponent (SemanticPossibility reducedForm, DomainRecognition node,
            Set<ReferedTokenPosition> positionsAdded, ReferedTokenPosition referedTokenPosition,
            DomainRecognition prevNode) {
      reducedForm.getReducedForm().addGraphComponent(node, prevNode);
      positionsAdded.add(referedTokenPosition);
      return node;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }
}
