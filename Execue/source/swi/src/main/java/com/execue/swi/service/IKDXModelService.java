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


package com.execue.swi.service;

/**
 * @author Abhijit
 * @since Jul 15, 2009 : 5:04:03 PM
 */

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;

public interface IKDXModelService extends ISWIService {

   /**
    * Provides the type of the entity by BED Id
    * 
    * @param bedId
    * @return
    * @throws SWIException
    */
   public BusinessEntityType getBusinessEntityType (Long bedId) throws SWIException;

   /**
    * Return Entity Triple Definition Object for the source, relation, destination combination
    * 
    * @param sourceBEDId
    * @param relationBEDId
    * @param destBEDId
    * @return
    * @throws SWIException
    */
   public EntityTripleDefinition getEntityTriple (Long sourceBEDId, Long relationBEDId, Long destBEDId)
            throws SWIException;

   /**
    * Returns a List of Entity Triple Definitions for a Source-Relation combination
    * 
    * @param sourceBEDId
    * @param relationBEDId
    * @return
    * @throws SWIException
    */
   public List<EntityTripleDefinition> getEntityTriplesForSourceAndRelation (Long sourceBEDId, Long relationBEDId)
            throws SWIException;

   /**
    * Returns a List of Entity Triple Definitions for a Relation-Destination combination
    * 
    * @param relationBEDId
    * @param destBEDId
    * @return
    * @throws SWIException
    */
   public List<EntityTripleDefinition> getEntityTriplesForDestinationAndRelation (Long relationBEDId, Long destBEDId)
            throws SWIException;

   /**
    * Returns a List of Entity Triple Definitions for the Source
    * 
    * @param sourceBEDId
    * @return the List<EntityTripleDefinition>
    * @throws SWIException
    */
   public List<EntityTripleDefinition> getEntityTriplesForSource (Long sourceBEDId) throws SWIException;

   /**
    * Returns a List of Entity Triple Definitions for the given Source and triple type
    * 
    * @param sourceBEDId
    * @return the List<EntityTripleDefinition>
    * @throws SWIException
    */
   public List<EntityTripleDefinition> getEntityTriplesForSourceAndTripleType (Long sourceBEDId,
            EntityTripleDefinitionType tripleType) throws SWIException;

   /**
    * Returns a List of Entity Triple Definitions for the Destination
    * 
    * @param destBEDId
    * @return the List<EntityTripleDefinition>
    * @throws SWIException
    */
   public List<EntityTripleDefinition> getEntityTriplesForDestination (Long destBEDId) throws SWIException;

   /**
    * Returns a List of Entity Triple Definitions for the given Destination and triple type
    * 
    * @param destBEDId
    * @param tripleType
    * @return the List<EntityTripleDefinition>
    * @throws SWIException
    */
   public List<EntityTripleDefinition> getEntityTriplesForDestinationAndTripleType (Long destBEDId,
            EntityTripleDefinitionType tripleType) throws SWIException;

   /**
    * Return all the Entity triple definitions which contain specified relation BED ID
    * 
    * @param relBEDId
    * @return
    * @throws SWIException
    */
   public List<EntityTripleDefinition> getEntityTriplesForRelation (Long relBEDId) throws SWIException;

   /**
    * Returns a List of complete Path Definition Objects describing Paths between Source and Destination Each Path
    * Definition contains Complete List of Path Definition ETD i.e. Triples on that Path The Path Definitions are
    * filtered by Legth specified by calling method
    * 
    * @param sourceBEDId
    * @param destBEDId
    * @param length
    * @return
    * @throws SWIException
    */
   public List<PathDefinition> getPathDefinitionDetailsLessThanLength (Long sourceBEDId, Long destBEDId, int length)
            throws SWIException;

   /**
    * Returns a List of complete Path Definition Objects describing Paths between Source and Destination Each Path
    * Definition contains Complete List of Path Definition ETD i.e. Triples on that Path
    * 
    * @param sourceBEDId
    * @param destBEDId
    * @return
    * @throws SWIException
    */
   public List<PathDefinition> getPathDefinitionDetails (Long sourceBEDId, Long destBEDId) throws KDXException;

   /**
    * Returns a List of complete Path Definition Objects describing Paths between Source and Destination
    * 
    * @param sourceBEDId
    * @param destBEDId
    * @return
    * @throws KDXException
    */
   public List<PathDefinition> getParentPathDefinitions (Long sourceBEDId, Long destBEDId) throws KDXException;

   public List<PathDefinition> getChildPathDefinitionsForSource (Long sourceBEDId) throws KDXException;

   public List<BusinessEntityDefinition> getChildPathDestinationBEDsForSource (Long sourceBEDId) throws KDXException;

   public List<BusinessEntityDefinition> getChildPathDestinationBEDsForSourceForModel (Long modelId, Long sourceBEDId)
            throws KDXException;

   /**
    * Returns a List of complete Path Definition Objects describing Paths between Source and Destination Each Path
    * Definition contains Complete List of Path Definition ETD i.e. Triples on that Path. In this case additional check
    * is done if Path Definition has Path Type = "CC" i.e. Concept to Concept
    * 
    * @param sourceBEDId
    * @param destBEDId
    * @return
    * @throws SWIException
    */
   public List<PathDefinition> getConceptToConceptPathDefinitionDetails (Long sourceBEDId, Long destBEDId)
            throws SWIException;

   public PathDefinition getConceptToConceptShortestPathDefinitionDetails (Long sourceDEDId, Long destDEDId)
            throws SWIException;

   /**
    * Returns a List of complete Path Definition Objects describing Paths between Source and Destination Each Path
    * Definition contains Complete List of Path Definition ETD i.e. Triples on that Path. In this case additional check
    * is done if Path Definition has Path Type = "CA" i.e. Concept to Attribute
    * 
    * @param sourceBEDId
    * @param destBEDId
    * @return
    * @throws SWIException
    */
   public List<PathDefinition> getConceptToAttributePathDefinitionDetails (Long sourceBEDId, Long destBEDId)
            throws SWIException;

   /**
    * Returns a List of complete Path Definition Objects describing Paths between Source and Destination Each Path
    * Definition contains Complete List of Path Definition ETD i.e. Triples on that Path. In this case additional check
    * is done if Path Definition has Path Type = "RR" i.e. Relation to Relation
    * 
    * @param sourceBEDId
    * @param destBEDId
    * @return
    * @throws SWIException
    */
   public List<PathDefinition> getRelationToRelationPathDefinitionDetails (Long sourceBEDId, Long destBEDId)
            throws SWIException;

   /**
    * Returns all the Concepts with Enumeration Concept Flag set to "Y"
    * 
    * @return
    * @throws SWIException
    */
   public List<BusinessEntityDefinition> getAllEnumerationConcepts () throws SWIException;

   /**
    * Returns all the Concepts with Quantitative Concept Flag set to "Y"
    * 
    * @return
    * @throws SWIException
    */
   public List<BusinessEntityDefinition> getAllQuantitativeConcepts () throws SWIException;

   /**
    * Returns all the Concepts with Comparative Concept Flag set to "Y"
    * 
    * @return
    * @throws SWIException
    */
   public List<BusinessEntityDefinition> getAllComparativeConcepts () throws SWIException;

   /**
    * Returns all the Concepts with Distribution Concept Flag set to "Y"
    * 
    * @return
    * @throws SWIException
    */
   public List<BusinessEntityDefinition> getAllDistributionConcepts () throws SWIException;

   /**
    * Returns all the Concepts with Abstract Concept Flag set to "Y"
    * 
    * @return
    * @throws SWIException
    */
   public List<BusinessEntityDefinition> getAllAbstractConcepts () throws SWIException;

   /**
    * Returns all the Concepts with Attribute Concept Flag set to "Y"
    * 
    * @return
    * @throws SWIException
    */
   public List<BusinessEntityDefinition> getAllAttributeConcepts () throws SWIException;

   /**
    * Returns TRUE if the Concept IBed by bedID is Central Concept i.e. Central Concept flag set to "Y"
    * 
    * @param bedID
    * @return
    * @throws SWIException
    */
   public boolean isGrainConcept (Long bedID) throws SWIException;

   /**
    * Returns TRUE if the Concept IBed by bedID is Abstract Concept i.e. Abstract Concept flag set to "Y"
    * 
    * @param bedID
    * @return
    * @throws SWIException
    */
   public boolean isAbstract (Long bedID) throws SWIException;

   /**
    * Returns TRUE if the Concept IBed by bedID is Attribute Concept i.e. Attribute Concept flag set to "Y"
    * 
    * @param bedID
    * @return
    * @throws SWIException
    */
   public boolean isAttribute (Long bedID) throws SWIException;

   /**
    * Returns TRUE if the Concept IBed by bedID is Comparative Concept i.e. Comparative Concept flag set to "Y"
    * 
    * @param bedID
    * @return
    * @throws SWIException
    */
   public boolean isComparativeConcept (Long bedID) throws SWIException;

   /**
    * Returns TRUE if the Concept IBed by bedID is Enumeration Concept i.e. Enumeration Concept flag set to "Y"
    * 
    * @param bedID
    * @return
    * @throws SWIException
    */
   public boolean isEnumerationConcept (Long bedID) throws SWIException;

   /**
    * Returns TRUE if the Concept IBed by bedID is Quantitative Concept i.e. Quantitative Concept flag set to "Y"
    * 
    * @param bedID
    * @return
    * @throws SWIException
    */
   public boolean isQuantitativeConcept (Long bedID) throws SWIException;

   /**
    * Returns TRUE if the Concept IBed by bedID is Distribution Concept i.e. Distribution Concept flag set to "Y"
    * 
    * @param bedID
    * @return
    * @throws SWIException
    */
   public boolean isDistributionConcept (Long bedID) throws SWIException;

   /**
    * TODO: -AP- Fill in the doc here
    * 
    * @param instanceBEDIds
    * @param conceptBEDIds
    * @return
    * @throws SWIException
    */
   public Set<Long> getRelatedBEDIdsByInstanceConceptBEDIds (List<Long> instanceBEDIds, List<Long> conceptBEDIds)
            throws SWIException;

   public Set<Long> getRelatedBEDIdsByConceptBEDIds (List<Long> conceptBEDIds) throws SWIException;

   /**
    * Method to get all the parent concept for passed bedId.
    * 
    * @param destinationBEDId
    * @return
    * @throws KDXException
    */
   public Collection<Concept> getAllParentConcepts (Long destinationBEDId) throws KDXException;

   /**
    * Method to get The immediate parent concepts for given bedId
    * 
    * @param destinationBEDId
    * @return
    * @throws KDXException
    */
   public List<Concept> getImmediateParentConcepts (Long destinationBEDId) throws KDXException;

   /**
    * Method returns the paths from any Central Concept to destination concept specified by passed BED ID
    * 
    * @param sourceBEDIDs
    * @param destinationBEDId
    * @return
    * @throws KDXException
    */
   public List<PathDefinition> getPathsBetweenMultipleSourcesToSingleDestination (List<Long> sourceBEDIDs,
            Long destinationBEDId) throws KDXException;

   public Path getPathByETD (EntityTripleDefinition etd) throws KDXException;

   public List<PathDefinition> getParentTriplesForDestList (Long sourceBEDId, List<Long> conceptBEDs)
            throws KDXException;

   /**
    * Method to get all the parent path definition for destinationBEDId
    * 
    * @param destinationBEDId
    * @return
    * @throws KDXException
    */
   public List<PathDefinition> getAllParentPathDefinitionsForDestination (Long destinationBEDId) throws KDXException;

   /**
    * Method to get the all immediate parent path definition for the given destinationBEDId
    * 
    * @param destinationBEDId
    * @return
    * @throws KDXException
    */
   public List<PathDefinition> getImmediateParentPathDefinitionsForDestination (Long destinationBEDId)
            throws KDXException;

   /**
    * Method to get the central Concept paths for a destination.
    * 
    * @param destinationBEDId
    * @return
    * @throws KDXException
    */
   public List<PathDefinition> getCentralConceptPathsForDestination (Long destinationBEDId) throws KDXException;

   public List<PathDefinition> getAllNonParentPathsForSourceAndDestination (Set<Long> sourceBEIds, Set<Long> destBeIds)
            throws KDXException;

   public boolean hasBehavior (Long resourceID, BehaviorType behaviorType) throws KDXException;

   /**
    * Method to check the behavior for the given entityBedId
    * 
    * @param entityBedId
    * @param behaviorType
    * @return the boolean true if the entity bed id has the behavior, else false
    * @throws KDXException
    */
   public boolean checkEntityHasBehavior (Long entityBedId, BehaviorType behaviorType) throws KDXException;

   /**
    * Analyze and check if the model evaluation and preparation is required or not
    * 
    * @param modelId
    * @return
    * @throws KDXException
    */
   public boolean isModelEvaluationRequired (Long modelId) throws KDXException;

   /**
    * TRUE if the Application associated to the model has only one asset and that asset is based on file
    * 
    * @param modelId
    * @return
    * @throws KDXException
    */
   public boolean isModelOfSingleFileBasedAsset (Long modelId) throws KDXException;

   public List<Long> getAllChildBedIds (Long sourceBEDId, Long modelId) throws KDXException;

   public List<Long> getAllParentBedIds (Long destBEDId, Long modelId) throws KDXException;

   public Map<Long, Map<Long, String>> getAllParentToChildTriplesForModel (Long modelId) throws KDXException;

   public Map<Long, Map<Long, String>> getAllChildToParentTriplesForModel (Long modelId) throws KDXException;

   public boolean isIndexEvaluationRequired (Long modelId) throws KDXException;

}