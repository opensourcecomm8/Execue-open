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


package com.execue.swi.dataaccess;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.InstancePathDefinition;
import com.execue.core.common.bean.entity.InstanceTripleDefinition;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.PathSelectionType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.KDXException;

/**
 * This interface contains the operations for Path, PathDefintion and EntityTrippleDefinition object
 * 
 * @author Vishay
 * @version 1.0
 * @since 01/07/09
 */
public interface IPathDefinitionDataAccessManager {

   public PathDefinition getPathDefinitionById (Long pathDefinitionId) throws DataAccessException;

   public void createPathDefinition (PathDefinition pathDefinition) throws DataAccessException;

   public void deletePathDefinition (PathDefinition pathDefinition) throws DataAccessException;

   public PathDefinition getPathDefinition (BusinessEntityDefinition sourceEntityDefinition,
            BusinessEntityDefinition destinationDomainEntityDefinition) throws DataAccessException;

   public List<PathDefinition> getPathDefinitions (BusinessEntityDefinition sourceEntityDefinition,
            BusinessEntityDefinition destinationBusinessEntityDefinition) throws DataAccessException;

   public List<Long> getPathDefinitionIds (Long sourceBEDId, Long destinationBEDId) throws DataAccessException;

   public List<PathDefinition> getPathDefinitions (Long sourceBEDId, Long destinationBEDId) throws DataAccessException;

   public PathDefinition getDirectPathDefinitionByETDIds (Long sourceETDBEDId, Long relationETDBEDId,
            Long destinationETDBEDId) throws DataAccessException;

   public Long getDirectPathDefinitionIdByETDNames (String sourceConceptName, String relationName,
            String destinationConceptName) throws DataAccessException;

   public InstancePathDefinition getInstancePathDefinition (Long sourceBEDId, Long destinationBEDId,
            Long pathDefinitionId) throws DataAccessException;

   public List<InstancePathDefinition> getInstancePathDefinitionsByDestinationAndPathIDs (Long destinationBEDId,
            Long pathDefinitionId) throws DataAccessException;

   public List<InstancePathDefinition> getInstancePathDefinitionsBySourceAndPathIDs (Long sourceBEDId,
            Long pathDefinitionId) throws DataAccessException;

   public List<InstancePathDefinition> getInstancePathDefinitionsByPathID (Long pathDefinitionId)
            throws DataAccessException;

   public List<Long> getSourceBEDIds (Long destinationBEDId, Long pathDefinitionId) throws DataAccessException;

   public List<Long> getDestinationBEDIds (Long sourceBEDId, Long pathDefinitionId) throws DataAccessException;

   public void createInstancePathDefinition (InstancePathDefinition instancePathDefinition) throws DataAccessException;

   public List<PathDefinition> getAllDirectPathsBySourceId (Long sourceBedId, Long modelId) throws DataAccessException;

   public List<PathDefinition> getAllDirectPathsByDestinationId (Long destBedId, Long modelId)
            throws DataAccessException;

   public List<PathDefinition> getAllDirectPaths ();

   public List<PathDefinition> getAllDirectPaths (Long modelId, Long entityBedId, EntityTripleDefinitionType pathType)
            throws DataAccessException;

   public List<PathDefinition> getAllParentPaths (Long modelId);

   public List<PathDefinition> getImmediateParentPaths ();

   public List<PathDefinition> getAllNonParentPaths ();

   public List<PathDefinition> getAllNonParentDirectCCPaths (Long modelId);

   public List<PathDefinition> getNonParentCCPathsForDestination (Long destinationId) throws DataAccessException;

   public List<PathDefinition> getAllCentralConceptPathsToMark (Long modelId, Long populationBedId)
            throws DataAccessException;

   /**
    * Method to update all the path Definition
    * 
    * @param pathDefinitions
    * @throws DataAccessException
    */
   public void updatePathDefinitions (List<PathDefinition> pathDefinitions) throws DataAccessException;

   public Set<String> getAllPathDefTriplesForModel (Long modelId) throws DataAccessException;

   public void deleteEntityTripleDefinitions (List<Long> etdIds) throws DataAccessException;

   public void deletePathDefinitions (List<Long> pathDefinitionIds) throws DataAccessException;

   // ----------- Methods to get PathDefinitions and ETDs for base and non-base model groups------------

   public List<Long> getNonBasePathDefinitions (List<ModelGroup> userModelGroups) throws KDXException;

   public List<Long> getBaseToNonBasePathDefinitions (ModelGroup baseModelGroup, List<ModelGroup> userModelGroups)
            throws KDXException;

   public List<Long> getNonBaseToBasePathDefinitions (List<ModelGroup> userModelGroups, ModelGroup baseModelGroup)
            throws KDXException;

   public List<Long> getNonBaseEntityTripleDefinitions (List<ModelGroup> userModelGroups) throws KDXException;

   public List<Long> getBaseToNonBaseEntityTripleDefinitions (ModelGroup baseModelGroup,
            List<ModelGroup> userModelGroups) throws KDXException;

   public List<Long> getNonBaseToBaseEntityTripleDefinitions (List<ModelGroup> userModelGroups,
            ModelGroup baseModelGroup) throws KDXException;

   public List<PathDefinition> getAllNonParentDirectPathsBySourceId (long sourceBedId, Long modelId)
            throws KDXException;

   public List<PathDefinition> getAllNonParentDirectPathsByDestinationId (Long destBedId, Long modelId)
            throws KDXException;

   /**
    * Method to get all the path definition where source bed has the given behaviorType
    * 
    * @param modelId
    * @param behaviorType
    * @return the List<PathDefinition>
    * @throws KDXException
    */
   public List<PathDefinition> getAllPathsBySourceBehaviorType (Long modelId, BehaviorType behaviorType)
            throws KDXException;

   /**
    * Method to get the list of indirect path definition ids for the given cloud
    * 
    * @param cloudId
    * @return the List<Long>
    * @throws KDXException
    */
   public List<Long> getAllIndirectPathDefIdsForCloud (Long cloudId) throws KDXException;

   /**
    * This method returns the list of indirect path definition ids for the given direct path definition id
    * 
    * @param pathDefinitionId
    * @return the List<Long>
    * @throws DataAccessException
    */
   public List<Long> getIndirectPathDefinitionIdsForDirectPath (Long pathDefinitionId) throws KDXException;

   /**
    * This method returns the list of all the path definitions for the given cloud
    * 
    * @param cloudId
    * @throws KDXException
    */
   public List<PathDefinition> getAllPathDefinitionsForCloud (Long cloudId) throws KDXException;

   /**
    * @param sourceBedId
    * @param modelId
    * @return
    * @throws KDXException
    */
   public List<EntityTripleDefinition> getAllTypeOriginPathsBySourceId (Long sourceBedId, Long modelId)
            throws KDXException;

   public List<Long> getPathDefinitionIdsForETD (Long etdId) throws KDXException;

   public List<PathDefinition> getPathDefinitionsByIds (List<Long> pathDefinitionIds) throws KDXException;

   public InstanceTripleDefinition getInstanceTripleDefinition (Long sourceBeId, Long relationBeId, Long destinationBeId)
            throws KDXException;

   public List<PathDefinition> getConvertableTypePaths (List<Long> realizationBedIds) throws KDXException;

   public List<PathDefinition> getConvertableTypePaths (Long realizationBedId) throws KDXException;

   public List<Long> getInstanceTripleSrcIdsByDestBeIds (Set<Long> instanceIdsInQuery) throws KDXException;

   public List<Long> getInstanceTripleDestIdsBySrcBeIds (Set<Long> instanceIdsInQuery) throws KDXException;

   public List<PathDefinition> getPathDefinitionsBySrcBedAndRelation (Long bedId, Long relationBedId)
            throws KDXException;

   public List<PathDefinition> getNonParentPathsByDestIdAndPathSelectionType (Set<Long> destBeIds, Long modelId,
            PathSelectionType pathSelectionType) throws KDXException;

   public List<PathDefinition> getNonParentPathsByDestBedIds (Set<Long> destBEDIds, Long modelGroupId)
            throws KDXException;

   public List<PathDefinition> getNonParentPathsBySrcBedIds (Set<Long> srcBEDIds, Long modelGroupId)
            throws KDXException;

   public List<Long> getAllPathDefinitionsBySourceBedId (Long cloudId, Long sourceBedId) throws KDXException;

   public List<PathDefinition> getRelatedNonParentDirectCCPaths (Long cloudId, Long sourceBedId, Long destinationBedId)
            throws KDXException;

   public List<InstanceTripleDefinition> getInstanceTripleDefinitionsByDestinationInstanceBedId (
            Long destinationInstanceBedId) throws KDXException;

   public Map<Long, List<PathDefinition>> getDirectValuePathDefinitonByDestId (Long cloudId, Long modelId)
            throws KDXException;

   public List<PathDefinition> getAllDirectPaths (Long entityBedId) throws KDXException;
}