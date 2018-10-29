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


package com.execue.swi.dataaccess.impl;

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
import com.execue.dataaccess.swi.dao.IPathDefinitionDAO;
import com.execue.swi.dataaccess.IPathDefinitionDataAccessManager;
import com.execue.swi.exception.KDXException;

/**
 * This class contains the operations for Path, PathDefintion and EntityTrippleDefinition object
 * 
 * @author Vishay
 * @version 1.0
 * @since 01/07/09
 */
public class PathDefinitionDataAccessManagerImpl implements IPathDefinitionDataAccessManager {

   private IPathDefinitionDAO pathDefinitionDAO;

   public void createPathDefinition (PathDefinition pathDefinition) throws DataAccessException {
      getPathDefinitionDAO().createPathDefinition(pathDefinition);
   }

   public void deletePathDefinition (PathDefinition pathDefinition) throws DataAccessException {
      getPathDefinitionDAO().deletePathDefinition(pathDefinition);
   }

   public PathDefinition getPathDefinition (BusinessEntityDefinition sourceEntityDefinition,
            BusinessEntityDefinition destinationDomainEntityDefinition) throws DataAccessException {
      return getPathDefinitionDAO().getPathDefinition(sourceEntityDefinition, destinationDomainEntityDefinition);
   }

   public List<PathDefinition> getPathDefinitions (BusinessEntityDefinition sourceEntityDefinition,
            BusinessEntityDefinition destinationDomainEntityDefinition) throws DataAccessException {
      return getPathDefinitionDAO().getPathDefinitions(sourceEntityDefinition, destinationDomainEntityDefinition);
   }

   public List<Long> getDestinationBEDIds (Long sourceBEDId, Long pathDefinitionId) throws DataAccessException {
      return getPathDefinitionDAO().getDestinationBEDIds(sourceBEDId, pathDefinitionId);
   }

   public InstancePathDefinition getInstancePathDefinition (Long sourceBEDId, Long destinationBEDId,
            Long pathDefinitionId) throws DataAccessException {
      return getPathDefinitionDAO().getInstancePathDefinition(sourceBEDId, destinationBEDId, pathDefinitionId);
   }

   public List<InstancePathDefinition> getInstancePathDefinitionsByDestinationAndPathIDs (Long destinationBEDId,
            Long pathDefinitionId) throws DataAccessException {
      return getPathDefinitionDAO().getInstancePathDefinitionsForDestinationAndPath(destinationBEDId, pathDefinitionId);
   }

   public List<InstancePathDefinition> getInstancePathDefinitionsBySourceAndPathIDs (Long sourceBEDId,
            Long pathDefinitionId) throws DataAccessException {
      return getPathDefinitionDAO().getInstancePathDefinitionsForSourceAndPath(sourceBEDId, pathDefinitionId);
   }

   public List<InstancePathDefinition> getInstancePathDefinitionsByPathID (Long pathDefinitionId)
            throws DataAccessException {
      return getPathDefinitionDAO().getInstancePathDefinitionsForPath(pathDefinitionId);
   }

   public List<Long> getSourceBEDIds (Long destinationBEDId, Long pathDefinitionId) throws DataAccessException {
      return getPathDefinitionDAO().getSourceBEDIds(destinationBEDId, pathDefinitionId);
   }

   public List<Long> getPathDefinitionIds (Long sourceBEDId, Long destinationBEDId) throws DataAccessException {
      return getPathDefinitionDAO().getPathDefinitionIds(sourceBEDId, destinationBEDId);
   }

   public List<PathDefinition> getPathDefinitions (Long sourceBEDId, Long destinationBEDId) throws DataAccessException {
      return getPathDefinitionDAO().getParentPathDefinitions(sourceBEDId, destinationBEDId);
   }

   public PathDefinition getDirectPathDefinitionByETDIds (Long sourceETDBEDId, Long relationETDBEDId,
            Long destinationETDBEDId) throws DataAccessException {
      return getPathDefinitionDAO().getDirectPathDefinitionByETDIds(sourceETDBEDId, relationETDBEDId,
               destinationETDBEDId);
   }

   public Long getDirectPathDefinitionIdByETDNames (String sourceConceptName, String relationName,
            String destinationConceptName) throws DataAccessException {
      return getPathDefinitionDAO().getDirectPathDefinitionIdByETDNames(sourceConceptName, relationName,
               destinationConceptName);
   }

   public IPathDefinitionDAO getPathDefinitionDAO () {
      return pathDefinitionDAO;
   }

   public void setPathDefinitionDAO (IPathDefinitionDAO pathDefinitionDAO) {
      this.pathDefinitionDAO = pathDefinitionDAO;
   }

   public PathDefinition getPathDefinitionById (Long pathDefinitionId) throws DataAccessException {
      return getPathDefinitionDAO().getById(pathDefinitionId, PathDefinition.class);
   }

   public void createInstancePathDefinition (InstancePathDefinition instancePathDefinition) throws DataAccessException {
      getPathDefinitionDAO().create(instancePathDefinition);
   }

   public List<PathDefinition> getAllDirectPathsBySourceId (Long sourceBEDId, Long modelId) throws DataAccessException {
      return getPathDefinitionDAO().getAllDirectPathsBySourceId(sourceBEDId, modelId);
   }

   public List<PathDefinition> getAllDirectPathsByDestinationId (Long destBEDId, Long modelId)
            throws DataAccessException {
      return getPathDefinitionDAO().getAllDirectPathsByDestinationId(destBEDId, modelId);
   }

   public List<PathDefinition> getAllDirectPaths (Long modelId, Long entityBedId, EntityTripleDefinitionType pathType)
            throws DataAccessException {
      return getPathDefinitionDAO().getAllDirectPaths(modelId, entityBedId, pathType);
   }

   public List<PathDefinition> getAllNonParentDirectPathsBySourceId (long sourceBedId, Long modelId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getAllNonParentDirectPathsBySourceId(sourceBedId, modelId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<PathDefinition> getAllNonParentDirectPathsByDestinationId (Long destBEDId, Long modelId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getAllNonParentDirectPathsByDestinationId(destBEDId, modelId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<PathDefinition> getAllDirectPaths () {
      return getPathDefinitionDAO().getAllDirectPaths();
   }

   public List<PathDefinition> getAllParentPaths (Long modelId) {
      return getPathDefinitionDAO().getAllParentPaths(modelId);
   }

   public List<PathDefinition> getImmediateParentPaths () {
      return getPathDefinitionDAO().getImmediateParentPaths();
   }

   public List<PathDefinition> getAllNonParentPaths () {
      return getPathDefinitionDAO().getAllNonParentPaths();
   }

   public List<PathDefinition> getAllNonParentDirectCCPaths (Long modelId) {
      return getPathDefinitionDAO().getAllNonParentDirectCCPaths(modelId);
   }

   public List<PathDefinition> getRelatedNonParentDirectCCPaths (Long cloudId, Long sourceBedId, Long destinationBedId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getRelatedNonParentDirectCCPaths(cloudId, sourceBedId, destinationBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<PathDefinition> getNonParentCCPathsForDestination (Long destinationId) throws DataAccessException {
      return getPathDefinitionDAO().getNonParentCCPathsForDestination(destinationId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IPathDefinitionDataAccessManager#getAllCentralConceptPathsToMark(java.lang.Long,
    *      java.lang.Long)
    */
   public List<PathDefinition> getAllCentralConceptPathsToMark (Long modelId, Long populationBedId)
            throws DataAccessException {
      return getPathDefinitionDAO().getAllCentralConceptPathsToMark(modelId, populationBedId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IPathDefinitionDataAccessManager#updatePathDefinitions(java.util.List)
    */
   public void updatePathDefinitions (List<PathDefinition> pathDefinitions) throws DataAccessException {
      getPathDefinitionDAO().updateAll(pathDefinitions);
   }

   public Set<String> getAllPathDefTriplesForModel (Long modelId) throws DataAccessException {
      return getPathDefinitionDAO().getAllPathDefTriplesForModel(modelId);
   }

   public void deleteEntityTripleDefinitions (List<Long> etdIds) throws DataAccessException {
      getPathDefinitionDAO().deleteEntityTripleDefinitions(etdIds);
   }

   public void deletePathDefinitions (List<Long> pathDefinitionIds) throws DataAccessException {
      getPathDefinitionDAO().deletePathDefinitions(pathDefinitionIds);
   }

   // ----------- Methods to get PathDefinitions and ETDs for base and non-base model groups------------

   public List<Long> getNonBasePathDefinitions (List<ModelGroup> userModelGroups) throws KDXException {
      try {
         return getPathDefinitionDAO().getNonBasePathDefinitions(userModelGroups);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getBaseToNonBasePathDefinitions (ModelGroup baseModelGroup, List<ModelGroup> userModelGroups)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getBaseToNonBasePathDefinitions(baseModelGroup, userModelGroups);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getNonBaseToBasePathDefinitions (List<ModelGroup> userModelGroups, ModelGroup baseModelGroup)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getNonBaseToBasePathDefinitions(userModelGroups, baseModelGroup);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getNonBaseEntityTripleDefinitions (List<ModelGroup> userModelGroups) throws KDXException {
      try {
         return getPathDefinitionDAO().getNonBaseEntityTripleDefinitions(userModelGroups);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getBaseToNonBaseEntityTripleDefinitions (ModelGroup baseModelGroup,
            List<ModelGroup> userModelGroups) throws KDXException {
      try {
         return getPathDefinitionDAO().getBaseToNonBaseEntityTripleDefinitions(baseModelGroup, userModelGroups);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getNonBaseToBaseEntityTripleDefinitions (List<ModelGroup> userModelGroups,
            ModelGroup baseModelGroup) throws KDXException {
      try {
         return getPathDefinitionDAO().getNonBaseToBaseEntityTripleDefinitions(userModelGroups, baseModelGroup);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IPathDefinitionDataAccessManager#getAllPathsBySourceBehaviorType(java.lang.Long,
    *      com.execue.core.common.type.BehaviorType)
    */
   public List<PathDefinition> getAllPathsBySourceBehaviorType (Long modelId, BehaviorType behaviorType)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getAllPathsBySourceBehaviorType(modelId, behaviorType);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IPathDefinitionDataAccessManager#getAllIndirectPathDefIdsForCloud(java.lang.Long)
    */
   public List<Long> getAllIndirectPathDefIdsForCloud (Long cloudId) throws KDXException {
      try {
         return getPathDefinitionDAO().getAllIndirectPathDefIdsForCloud(cloudId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IPathDefinitionDataAccessManager#getIndirectPathDefinitionIdsForDirectPath(java.lang.Long)
    */
   public List<Long> getIndirectPathDefinitionIdsForDirectPath (Long pathDefinitionId) throws KDXException {
      try {
         return getPathDefinitionDAO().getIndirectPathDefinitionIdsForDirectPath(pathDefinitionId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IPathDefinitionDataAccessManager#getAllPathDefinitionsForCloud(java.lang.Long)
    */
   public List<PathDefinition> getAllPathDefinitionsForCloud (Long cloudId) throws KDXException {
      try {
         return getPathDefinitionDAO().getAllPathDefinitionsForCloud(cloudId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<EntityTripleDefinition> getAllTypeOriginPathsBySourceId (Long sourceBedId, Long modelId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getAllTypeOriginPathsBySourceId(sourceBedId, modelId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IPathDefinitionDataAccessManager#getPathDefinitionIdsForETD(java.lang.Long)
    */
   public List<Long> getPathDefinitionIdsForETD (Long etdId) throws KDXException {
      try {
         return getPathDefinitionDAO().getPathDefinitionIdsForETD(etdId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<PathDefinition> getPathDefinitionsByIds (List<Long> pathDefinitionIds) throws KDXException {
      try {
         return getPathDefinitionDAO().getPathDefinitionsByIds(pathDefinitionIds);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }

   }

   public InstanceTripleDefinition getInstanceTripleDefinition (Long sourceBeId, Long relationBeId, Long destinationBeId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getInstanceTripleDefinition(sourceBeId, relationBeId, destinationBeId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<InstanceTripleDefinition> getInstanceTripleDefinitionsByDestinationInstanceBedId (
            Long destinationInstanceBedId) throws KDXException {
      try {
         return getPathDefinitionDAO().getInstanceTripleDefinitionsByDestinationInstanceBedId(destinationInstanceBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }

   }

   public List<PathDefinition> getConvertableTypePaths (List<Long> realizationBedIds) throws KDXException {
      try {
         return getPathDefinitionDAO().getConvertableTypePaths(realizationBedIds);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }

   }

   public List<PathDefinition> getConvertableTypePaths (Long realizationBedId) throws KDXException {
      try {
         return getPathDefinitionDAO().getConvertableTypePaths(realizationBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }

   }

   public List<Long> getInstanceTripleSrcIdsByDestBeIds (Set<Long> instanceIdsInQuery) throws KDXException {
      try {
         return getPathDefinitionDAO().getInstanceTripleSrcIdsByDestBeIds(instanceIdsInQuery);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getInstanceTripleDestIdsBySrcBeIds (Set<Long> instanceIdsInQuery) throws KDXException {
      try {
         return getPathDefinitionDAO().getInstanceTripleDestIdsBySrcBeIds(instanceIdsInQuery);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<PathDefinition> getPathDefinitionsBySrcBedAndRelation (Long bedId, Long relationBedId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getPathDefinitionsBySrcBedAndRelation(bedId, relationBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<PathDefinition> getNonParentPathsByDestIdAndPathSelectionType (Set<Long> destBeIds, Long modelId,
            PathSelectionType pathSelectionType) throws KDXException {
      try {
         return getPathDefinitionDAO().getNonParentPathsByDestIdAndPathSelectionType(destBeIds, modelId,
                  pathSelectionType);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<PathDefinition> getNonParentPathsByDestBedIds (Set<Long> destBEDIds, Long modelGroupId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getNonParentPathsByDestBedIds(destBEDIds, modelGroupId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<PathDefinition> getNonParentPathsBySrcBedIds (Set<Long> srcBEDIds, Long modelGroupId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getNonParentPathsBySrcBedIds(srcBEDIds, modelGroupId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<Long> getAllPathDefinitionsBySourceBedId (Long cloudId, Long sourceBedId) throws KDXException {
      try {
         return getPathDefinitionDAO().getAllPathDefinitionsBySourceBedId(cloudId, sourceBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public Map<Long, List<PathDefinition>> getDirectValuePathDefinitonByDestId (Long cloudId, Long modelId)
            throws KDXException {
      try {
         return getPathDefinitionDAO().getDirectValuePathDefinitonByDestId(cloudId, modelId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<PathDefinition> getAllDirectPaths (Long entityBedId) throws KDXException {
      try {
         return getPathDefinitionDAO().getAllDirectPaths(entityBedId);
      } catch (DataAccessException dataAccessException) {
         throw new KDXException(dataAccessException.getCode(), dataAccessException);
      }
   }
}