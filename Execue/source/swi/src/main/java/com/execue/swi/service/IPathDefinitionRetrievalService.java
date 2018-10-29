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

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.InstancePathDefinition;
import com.execue.core.common.bean.entity.InstanceTripleDefinition;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.PathSelectionType;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;

public interface IPathDefinitionRetrievalService {

   /**
    * Get the first path definition by source and destination BEDs ordered by path definitions ids
    * 
    * @param sourceEntityDefinition
    * @param destinationBusinessEntityDefinition
    * @return
    * @throws SWIException
    */
   public PathDefinition getPathDefinition (BusinessEntityDefinition sourceEntityDefinition,
            BusinessEntityDefinition destinationBusinessEntityDefinition) throws SWIException;

   /**
    * Get the list of path definitions by source and destination BEDs ordered by path definitions ids
    * 
    * @param sourceEntityDefinition
    * @param destinationBusinessEntityDefinition
    * @return
    * @throws SWIException
    */
   public List<PathDefinition> getPathDefinitions (BusinessEntityDefinition sourceEntityDefinition,
            BusinessEntityDefinition destinationBusinessEntityDefinition) throws SWIException;

   /**
    * Get the id's of the Path Definition based on source and destination id's ordered by path definition ids
    * 
    * @param sourceBEDId
    * @param destinationBEDId
    * @return
    * @throws SWIException
    */
   public List<Long> getPathDefinitionIds (Long sourceBEDId, Long destinationBEDId) throws SWIException;

   /**
    * @param sourceBEDId
    * @param destinationBEDId
    * @return
    * @throws SWIException
    */
   public List<PathDefinition> getPathDefinitions (Long sourceBEDId, Long destinationBEDId) throws SWIException;

   /**
    * Get the direct path definition based on the subject (source), predicate (relation) and object (destination)
    * definitions id's (Entity Triple Definition attribute id's). If more than one path definition exists then throws an
    * exception.
    * 
    * @param sourceETDBEDId
    * @param relationETDBEDId
    * @param destinationETDBEDId
    * @return
    * @throws SWIException
    */
   public PathDefinition getDirectPathDefinitionByETDIds (Long sourceETDBEDId, Long relationETDBEDId,
            Long destinationETDBEDId) throws SWIException;

   /**
    * This method is exactly same as getDirectPathDefinitionByETDIds but loaBED with id's to the first level hierarchy
    * 
    * @param sourceETDBEDId
    * @param relationETDBEDId
    * @param destinationETDBEDId
    * @return
    * @throws SWIException
    */
   public PathDefinition getDirectPathDefinitionDetailsByETDIds (Long sourceETDBEDId, Long relationETDBEDId,
            Long destinationETDBEDId) throws SWIException;

   /**
    * Get the direct path definition id based on the subject (source), predicate (relation) and object (destination)
    * definitions name's (Entity Triple Definition attribute name's). If more than one path definition exists then
    * throws an exception.
    * 
    * @param sourceConceptName
    * @param relationName
    * @param destinationConceptName
    * @return
    * @throws SWIException
    */
   public Long getDirectPathDefinitionIdByETDNames (String sourceConceptName, String relationName,
            String destinationConceptName) throws SWIException;

   /**
    * Provides the Relation of the Direct Path Definition. This method is transactional
    * 
    * @param pathDefinitionId
    * @return
    * @throws SWIException
    */
   public Relation getDirectPathDefRelationByPathDefId (Long pathDefinitionId) throws SWIException;

   /**
    * Get the Instance Path Definition by source and destination BEDs and path definition<br>
    * If more than one entity exists(should not be), then returns the first one ordered by path definition, source and
    * destination BEDs
    * 
    * @param sourceBEDId
    * @param destinationBEDId
    * @param pathDefinitionId
    * @return
    * @throws SWIException
    */
   public InstancePathDefinition getInstancePathDefinition (Long sourceBEDId, Long destinationBEDId,
            Long pathDefinitionId) throws SWIException;

   /**
    * Get the list of Instance Path Definition by destination BED and path definition ordered by path definition and
    * destination BEDs
    * 
    * @param destinationBEDId
    * @param pathDefinitionId
    * @return List of Instance Path Defintions
    * @throws SWIException
    */
   public List<InstancePathDefinition> getInstancePathDefinitionsByDestinationAndPath (Long destinationBEDId,
            Long pathDefinitionId) throws SWIException;

   /**
    * Get the list of Instance Path Definition by source BED and path definition ordered by path definition and source
    * BED
    * 
    * @param sourceBEDId
    * @param pathDefinitionId
    * @return List of Instance Path Defintions
    * @throws SWIException
    */
   public List<InstancePathDefinition> getInstancePathDefinitionsBySourceAndPath (Long sourceBEDId,
            Long pathDefinitionId) throws SWIException;

   /**
    * Get the list of Instance Path Definitions for a specifc Path
    * 
    * @param pathDefinitionId
    *           Path ID of a specific Path
    * @return List of Instance Path Defintions
    * @throws SWIException
    */
   public List<InstancePathDefinition> getInstancePathDefinitionsByPath (Long pathDefinitionId) throws SWIException;

   /**
    * Get the list source BED id's by destination BEDs and path definitions ordered by path definition and destination
    * BEDs
    * 
    * @param destinationBEDId
    * @param pathDefinitionId
    * @return
    * @throws SWIException
    */
   public List<Long> getSourceBEDIds (Long destinationBEDId, Long pathDefinitionId) throws SWIException;

   /**
    * Get the list destination BED id's by source BEDs and path definitions ordered by path definition and source BEDs
    * 
    * @param sourceBEDId
    * @param pathDefinitionId
    * @return
    * @throws SWIException
    */
   public List<Long> getDestinationBEDIds (Long sourceBEDId, Long pathDefinitionId) throws SWIException;

   /**
    * Get the list of all the immediate parent path definitions of length 1
    * 
    * @return the List<PathDefinition>
    */
   public List<PathDefinition> getImmediateParentPaths ();

   /**
    * Get the list of all the parent path definitions for the given model
    * 
    * @param modelId
    * @return the List<PathDefinition>
    */
   public List<PathDefinition> getAllParentPaths (Long modelId);

   public List<PathDefinition> getAllNonParentDirectCCPaths (Long modelId) throws SWIException;;

   public List<PathDefinition> getAllDirectPathsBySourceId (Long sourceBedId, Long modelId) throws SWIException;

   public List<PathDefinition> getAllDirectPaths (Long modelId, Long entityBedId, EntityTripleDefinitionType etdType)
            throws SWIException;

   public List<PathDefinition> getAllDirectPathsByDestinationId (Long destBedId, Long modelId) throws SWIException;

   public List<PathDefinition> getAllNonParentDirectPathsBySourceId (Long sourceBedId, Long modelId)
            throws SWIException;

   public List<PathDefinition> getAllNonParentDirectPathsByDestinationId (Long destBedId, Long modelId)
            throws SWIException;

   public List<PathDefinition> getNonParentCCPathsForDestination (Long destBedId) throws SWIException;

   public List<PathDefinition> getAllCentralConceptPathsToMark (Long modelId, Long populationBedId) throws SWIException;

   public Set<String> getAllPathDefTriplesForModel (Long modelId) throws SWIException;

   public List<Long> getNonBasePathDefinitions (List<ModelGroup> userModelGroups) throws SWIException;

   public List<Long> getBaseToNonBasePathDefinitions (ModelGroup baseModelGroup, List<ModelGroup> userModelGroups)
            throws SWIException;

   public List<Long> getNonBaseToBasePathDefinitions (List<ModelGroup> userModelGroups, ModelGroup baseModelGroup)
            throws SWIException;

   public List<Long> getNonBaseEntityTripleDefinitions (List<ModelGroup> userModelGroups) throws SWIException;

   public List<Long> getBaseToNonBaseEntityTripleDefinitions (ModelGroup baseModelGroup,
            List<ModelGroup> userModelGroups) throws SWIException;

   public List<Long> getNonBaseToBaseEntityTripleDefinitions (List<ModelGroup> userModelGroups,
            ModelGroup baseModelGroup) throws SWIException;

   /**
    * Method to get all the path definition where source bed has the given behaviorType
    * 
    * @param modelId
    * @param behaviorType
    * @return the List<PathDefinition>
    * @throws SWIException
    */
   public List<PathDefinition> getAllPathsBySourceBehaviorType (Long modelId, BehaviorType behaviorType)
            throws SWIException;

   /**
    * Method to get the list of indirect path definition ids for the given cloud
    * 
    * @param cloudId
    * @return the List<Long>
    * @throws SWIException
    */
   public List<Long> getAllIndirectPathDefIdsForCloud (Long cloudId) throws SWIException;

   /**
    * This method returns the list of indirect path definition ids for the given direct path definition id
    * 
    * @param pathDefinitionId
    * @return the List<Long>
    * @throws SWIException
    */
   public List<Long> getIndirectPathDefinitionIdsForDirectPath (Long pathDefinitionId) throws SWIException;

   /**
    * This method returns the path definition for the given path definition id
    * 
    * @param pathDefinitinoId
    * @return the PathDefinition
    * @throws SWIException
    */
   public PathDefinition getPathDefinitionById (Long pathDefinitinoId) throws SWIException;

   /**
    * Method to get all The CRA paths by SourceBeId
    * 
    * @param sourceBedId
    * @param modelId
    * @return
    * @throws SWIException
    */
   public List<EntityTripleDefinition> getAllTypeOriginPathsBySourceId (Long sourceBedId, Long modelId)
            throws SWIException;

   public InstanceTripleDefinition getInstanceTripleDefinition (Long sourceBeId, Long relationBeId, Long destinationBeId)
            throws SWIException;

   public List<Set<Long>> getConvertableTypePaths (List<Long> realizationBedIds) throws SWIException;

   public Set<Long> getConvertableTypePaths (Long realizationBedId) throws SWIException;

   public List<Long> getInstanceTripleSrcIdsByDestBeIds (Set<Long> instanceIdsInQuery) throws SWIException;

   public List<Long> getInstanceTripleDestIdsBySrcBeIds (Set<Long> instanceIdsInQuery) throws SWIException;

   public List<PathDefinition> getPathDefinitionsBySrcBedAndRelation (Long bedId, Long relationBedId)
            throws SWIException;

   public List<PathDefinition> getNonParentPathsByDestIdAndPathSelectionType (Set<Long> destBeIds, Long modelId,
            PathSelectionType pathSelectionType) throws SWIException;

   public List<PathDefinition> getNonParentPathsByDestBedIds (Set<Long> destBEDIds, Long modelGroupId)
            throws SWIException;

   public List<PathDefinition> getNonParentPathsBySrcBedIds (Set<Long> srcBEDIds, Long modelGroupId)
            throws SWIException;

   public List<Long> getAllPathDefinitionsBySourceBedId (Long cloudId, Long sourceBedId) throws KDXException;

   public List<PathDefinition> getRelatedNonParentDirectCCPaths (Long cloudId, Long sourceBedId, Long destinationBedId)
            throws KDXException;

   /**
    * This Method returns the list of indirect paths
    * 
    * @param parentPaths
    * @param existingTriples
    * @return the List<PathDefinition>
    */
   public List<PathDefinition> getIndirectPaths (List<PathDefinition> parentPaths, Set<String> existingTriples)
            throws SWIException;

   public boolean checkIfInstancePathExists (Long sourceBeId, Long relationBeId, Long destinationBeId)
            throws SWIException;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getIndirectPathDefinitionIdsForDirectPath(java.lang.Long)
    */
   public List<Long> getPathDefinitionIdsForETD (Long etdId) throws SWIException;

   public List<InstanceTripleDefinition> getInstanceTripleDefinitionsByDestinationId (Long destinationInstanceBedId)
            throws SWIException;

   /**
    * Method to get the direct paths from concept to all the value realization concepts concept if of destination would
    * be thye key
    * 
    * @param cloudId
    * @param modelId
    * @return
    * @throws KDXException
    */
   public Map<Long, List<PathDefinition>> getDirectValuePathDefinitonByDestId (Long cloudId, Long modelId)
            throws KDXException;

   public List<PathDefinition> getAllDirectPaths (Long entityBedId) throws KDXException;
}
