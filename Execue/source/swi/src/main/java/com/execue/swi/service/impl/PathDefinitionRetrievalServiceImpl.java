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


package com.execue.swi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.InstancePathDefinition;
import com.execue.core.common.bean.entity.InstanceTripleDefinition;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.Rule;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.HierarchyType;
import com.execue.core.common.type.PathSelectionType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.dataaccess.IPathDefinitionDataAccessManager;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IPathDefinitionRetrievalService;

public class PathDefinitionRetrievalServiceImpl implements IPathDefinitionRetrievalService {

   private static final Logger              logger = Logger.getLogger(PathDefinitionRetrievalServiceImpl.class);

   private IPathDefinitionDataAccessManager pathDefinitionDataAccessManager;
   private IKDXModelService                 kdxModelService;

   public boolean checkIfInstancePathExists (Long sourceBeId, Long relationBeId, Long destinationBeId)
            throws SWIException {
      return getInstanceTripleDefinition(sourceBeId, relationBeId, destinationBeId) != null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getAllCentralConceptPathsToMark(java.lang.Long,
    *      java.lang.Long)
    */
   public List<PathDefinition> getAllCentralConceptPathsToMark (Long modelId, Long populationBedId) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getAllCentralConceptPathsToMark(modelId, populationBedId);
      } catch (DataAccessException e) {
         throw new SWIException(e.code, e);
      }
   }

   public List<PathDefinition> getAllDirectPaths (Long modelId, Long entityBedId, EntityTripleDefinitionType pathType)
            throws SWIException {
      try {
         List<PathDefinition> pathDefinitions = getPathDefinitionDataAccessManager().getAllDirectPaths(modelId,
                  entityBedId, pathType);
         for (PathDefinition pathDefinition : pathDefinitions) {
            List<Path> paths = new ArrayList<Path>(pathDefinition.getPaths());
            navigateETD(paths.get(0).getEntityTripleDefinition());
         }
         return pathDefinitions;
      } catch (DataAccessException e) {
         throw new SWIException(e.code, e);
      }
   }

   private void navigateETD (EntityTripleDefinition etd) {
      etd.getCardinality();
      etd.getRelation();
      etd.getRelation().getRelation();
      etd.getRelation().getRelation().getName();
      etd.getRelation().getType();
      etd.getRelation().getType().getName();
      etd.getSourceBusinessEntityDefinition();
      etd.getSourceBusinessEntityDefinition().getConcept();
      etd.getSourceBusinessEntityDefinition().getType();
      etd.getSourceBusinessEntityDefinition().getType().getName();
      if (etd.getSourceBusinessEntityDefinition().getConcept() != null) {
         etd.getSourceBusinessEntityDefinition().getConcept().getName();
      } else {
         etd.getSourceBusinessEntityDefinition().getConceptProfile();
         if (etd.getSourceBusinessEntityDefinition().getConceptProfile() != null) {
            etd.getSourceBusinessEntityDefinition().getConceptProfile().getName();
         }
      }
      etd.getDestinationBusinessEntityDefinition();
      Type type = etd.getDestinationBusinessEntityDefinition().getType();
      type.getName();
      etd.getDestinationBusinessEntityDefinition().getConcept();
      if (etd.getDestinationBusinessEntityDefinition().getConcept() != null) {
         etd.getDestinationBusinessEntityDefinition().getConcept().getName();
      } else {
         etd.getDestinationBusinessEntityDefinition().getConceptProfile();
         if (etd.getDestinationBusinessEntityDefinition().getConceptProfile() != null) {
            etd.getDestinationBusinessEntityDefinition().getConceptProfile().getName();
         }
      }
   }

   public List<PathDefinition> getAllDirectPathsByDestinationId (Long destBedId, Long modelId) throws SWIException {
      try {
         List<PathDefinition> paths = getPathDefinitionDataAccessManager().getAllDirectPathsByDestinationId(destBedId,
                  modelId);
         navigatePathDefinitions(paths);
         return paths;
      } catch (DataAccessException e) {
         throw new SWIException(e.code, e);
      }
   }

   private void navigatePathDefinitions (List<PathDefinition> paths) {
      for (PathDefinition pathDefinition : paths) {
         navigatePathDefinition(pathDefinition);
      }
   }

   /**
    * This method touches the complete path definition hierarchy
    * 
    * @param pathDefinition
    */
   private void navigatePathDefinition (PathDefinition pathDefinition) {

      pathDefinition.getPathRules();
      pathDefinition.getSourceBusinessEntityDefinition();
      pathDefinition.getSourceBusinessEntityDefinition().getConcept();

      if (pathDefinition.getSourceBusinessEntityDefinition().getConcept() != null) {
         pathDefinition.getSourceBusinessEntityDefinition().getConcept().getName();
      } else {
         pathDefinition.getSourceBusinessEntityDefinition().getConceptProfile();
         if (pathDefinition.getSourceBusinessEntityDefinition().getConceptProfile() != null) {
            pathDefinition.getSourceBusinessEntityDefinition().getConceptProfile().getName();
         }
      }
      pathDefinition.getDestinationBusinessEntityDefinition();
      pathDefinition.getDestinationBusinessEntityDefinition().getConcept();
      if (pathDefinition.getDestinationBusinessEntityDefinition().getConcept() != null) {
         pathDefinition.getDestinationBusinessEntityDefinition().getConcept().getName();
      } else {
         pathDefinition.getDestinationBusinessEntityDefinition().getConceptProfile();
         if (pathDefinition.getDestinationBusinessEntityDefinition().getConceptProfile() != null) {
            pathDefinition.getDestinationBusinessEntityDefinition().getConceptProfile().getName();
         }
      }
      pathDefinition.getPaths();
      for (Path path : pathDefinition.getPaths()) {
         PathDefinition pathDefinition2 = path.getPathDefinition();
         pathDefinition2.getPathLength();
         navigateETD(path.getEntityTripleDefinition());
      }
      if (!CollectionUtils.isEmpty(pathDefinition.getPathRules())) {
         for (Rule rule : pathDefinition.getPathRules()) {
            rule.getName();
         }
      }
   }

   public List<PathDefinition> getAllDirectPathsBySourceId (Long sourceBedId, Long modelId) throws SWIException {
      try {
         List<PathDefinition> paths = getPathDefinitionDataAccessManager().getAllDirectPathsBySourceId(sourceBedId,
                  modelId);
         navigatePathDefinitions(paths);
         return paths;
      } catch (DataAccessException e) {
         throw new SWIException(e.code, e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getAllIndirectPathDefIdsForCloud(java.lang.Long)
    */
   public List<Long> getAllIndirectPathDefIdsForCloud (Long cloudId) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getAllIndirectPathDefIdsForCloud(cloudId);
      } catch (KDXException kdxException) {
         throw new SWIException(kdxException.getCode(), kdxException);
      }
   }

   public List<PathDefinition> getAllNonParentDirectCCPaths (Long modelId) {
      List<PathDefinition> paths = getPathDefinitionDataAccessManager().getAllNonParentDirectCCPaths(modelId);
      navigatePathDefinitions(paths);
      return paths;
   }

   public List<PathDefinition> getAllNonParentDirectPathsByDestinationId (Long destBedId, Long modelId)
            throws SWIException {
      List<PathDefinition> paths = getPathDefinitionDataAccessManager().getAllNonParentDirectPathsByDestinationId(
               destBedId, modelId);
      navigatePathDefinitions(paths);
      return paths;
   }

   public List<PathDefinition> getAllNonParentDirectPathsBySourceId (Long sourceBedId, Long modelId)
            throws SWIException {
      List<PathDefinition> paths = getPathDefinitionDataAccessManager().getAllNonParentDirectPathsBySourceId(
               sourceBedId, modelId);
      navigatePathDefinitions(paths);
      return paths;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getAllParentPaths(java.lang.Long)
    */
   public List<PathDefinition> getAllParentPaths (Long modelId) {
      List<PathDefinition> paths = getPathDefinitionDataAccessManager().getAllParentPaths(modelId);
      for (PathDefinition pathDefinition : paths) {
         pathDefinition.getDestinationBusinessEntityDefinition();
         pathDefinition.getPaths();
         for (Path path : pathDefinition.getPaths()) {
            path.getEntityTripleDefinition();
            path.getEntityTripleDefinition().getCardinality();
            path.getEntityTripleDefinition().getRelation();
            path.getEntityTripleDefinition().getRelation().getRelation();
            path.getEntityTripleDefinition().getRelation().getRelation().getName();
            path.getEntityTripleDefinition().getSourceBusinessEntityDefinition();
            path.getEntityTripleDefinition().getSourceBusinessEntityDefinition().getConcept();
            path.getEntityTripleDefinition().getSourceBusinessEntityDefinition().getConcept().getName();
            path.getEntityTripleDefinition().getDestinationBusinessEntityDefinition();
            path.getEntityTripleDefinition().getDestinationBusinessEntityDefinition().getConcept();
            path.getEntityTripleDefinition().getDestinationBusinessEntityDefinition().getConcept().getName();
         }
      }
      return paths;
   }

   public Set<String> getAllPathDefTriplesForModel (Long modelId) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getAllPathDefTriplesForModel(modelId);
      } catch (DataAccessException e) {
         throw new SWIException(e.code, e);
      }
   }

   public List<Long> getAllPathDefinitionsBySourceBedId (Long cloudId, Long sourceBedId) throws KDXException {
      return getPathDefinitionDataAccessManager().getAllPathDefinitionsBySourceBedId(cloudId, sourceBedId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getAllPathsBySourceBehaviorType(java.lang.Long,
    *      com.execue.core.common.type.BehaviorType)
    */
   public List<PathDefinition> getAllPathsBySourceBehaviorType (Long modelId, BehaviorType behaviorType)
            throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getAllPathsBySourceBehaviorType(modelId, behaviorType);
      } catch (KDXException kdxException) {
         throw new SWIException(kdxException.getCode(), kdxException);
      }
   }

   public List<EntityTripleDefinition> getAllTypeOriginPathsBySourceId (Long sourceBedId, Long modelId)
            throws SWIException {
      List<EntityTripleDefinition> paths = getPathDefinitionDataAccessManager().getAllTypeOriginPathsBySourceId(
               sourceBedId, modelId);
      // navigatePathDefinitions(paths);
      return paths;
   }

   public List<Long> getBaseToNonBaseEntityTripleDefinitions (ModelGroup baseModelGroup,
            List<ModelGroup> userModelGroups) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getBaseToNonBaseEntityTripleDefinitions(baseModelGroup,
                  userModelGroups);
      } catch (KDXException kdxException) {
         throw new SWIException(kdxException.getCode(), kdxException);
      }
   }

   public List<Long> getBaseToNonBasePathDefinitions (ModelGroup baseModelGroup, List<ModelGroup> userModelGroups)
            throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getBaseToNonBasePathDefinitions(baseModelGroup, userModelGroups);
      } catch (KDXException kdxException) {
         throw new SWIException(kdxException.getCode(), kdxException);
      }
   }

   public List<Set<Long>> getConvertableTypePaths (List<Long> realizationBedIds) throws KDXException {
      List<PathDefinition> convertableTypePaths = getPathDefinitionDataAccessManager().getConvertableTypePaths(
               realizationBedIds);
      List<Set<Long>> allPaths = new ArrayList<Set<Long>>(1);
      for (PathDefinition path : convertableTypePaths) {
         boolean added = false;
         for (Set<Long> ids : allPaths) {
            if (ids.contains(path.getSourceBusinessEntityDefinition().getId())
                     || ids.contains(path.getDestinationBusinessEntityDefinition().getId())) {
               added = true;
               ids.add(path.getSourceBusinessEntityDefinition().getId());
               ids.add(path.getDestinationBusinessEntityDefinition().getId());
               break;
            }
         }
         if (!added) {
            Set<Long> ids = new HashSet<Long>(1);
            ids.add(path.getSourceBusinessEntityDefinition().getId());
            ids.add(path.getDestinationBusinessEntityDefinition().getId());
            allPaths.add(ids);
         }
      }
      for (Long realizationBedId : realizationBedIds) {
         boolean added = false;
         for (Set<Long> ids : allPaths) {
            if (ids.contains(realizationBedId)) {
               added = true;
               break;
            }
         }
         if (!added) {
            Set<Long> ids = new HashSet<Long>(1);
            ids.add(realizationBedId);
            allPaths.add(ids);
         }

      }
      return allPaths;
   }

   public Set<Long> getConvertableTypePaths (Long realizationBedId) throws SWIException {
      List<PathDefinition> convertableTypePaths = getPathDefinitionDataAccessManager().getConvertableTypePaths(
               realizationBedId);
      Set<Long> convertableBedIds = new HashSet<Long>();
      convertableBedIds.add(realizationBedId);
      if (ExecueCoreUtil.isCollectionNotEmpty(convertableTypePaths)) {
         for (PathDefinition path : convertableTypePaths) {
            convertableBedIds.add(path.getSourceBusinessEntityDefinition().getId());
            convertableBedIds.add(path.getDestinationBusinessEntityDefinition().getId());
         }
      }
      convertableBedIds.remove(realizationBedId);
      return convertableBedIds;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getDestinationBEDIds(java.lang.Long, java.lang.Long)
    */
   public List<Long> getDestinationBEDIds (Long sourceBEDId, Long pathDefinitionId) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getDestinationBEDIds(sourceBEDId, pathDefinitionId);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getDirectPathDefRelationByPathDefId(java.lang.Long)
    */
   public Relation getDirectPathDefRelationByPathDefId (Long pathDefinitionId) throws SWIException {
      Relation relation = null;
      try {
         PathDefinition pathDefinition = getPathDefinitionDataAccessManager().getPathDefinitionById(pathDefinitionId);
         if (pathDefinition.getPathLength() == 1) {
            relation = ((Path) pathDefinition.getPaths().toArray()[0]).getEntityTripleDefinition().getRelation()
                     .getRelation();
            relation.getName();
         }
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
      return relation;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getDirectPathDefinitionByETDIds(java.lang.Long,
    *      java.lang.Long, java.lang.Long)
    */
   public PathDefinition getDirectPathDefinitionByETDIds (Long sourceETDBEDId, Long relationETDBEDId,
            Long destinationETDBEDId) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getDirectPathDefinitionByETDIds(sourceETDBEDId, relationETDBEDId,
                  destinationETDBEDId);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public PathDefinition getDirectPathDefinitionDetailsByETDIds (Long sourceETDBEDId, Long relationETDBEDId,
            Long destinationETDBEDId) throws SWIException {
      PathDefinition pathDefinition = getDirectPathDefinitionByETDIds(sourceETDBEDId, relationETDBEDId,
               destinationETDBEDId);
      if (pathDefinition == null) {
         return pathDefinition;
      }

      pathDefinition.getSourceBusinessEntityDefinition().getId();
      pathDefinition.getDestinationBusinessEntityDefinition().getId();
      for (Path path : pathDefinition.getPaths()) {
         path.getEntityTripleDefinition().getId();
      }

      return pathDefinition;
   }

   public Long getDirectPathDefinitionIdByETDNames (String sourceConceptName, String relationName,
            String destinationConceptName) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getDirectPathDefinitionIdByETDNames(sourceConceptName,
                  relationName, destinationConceptName);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<PathDefinition> getImmediateParentPaths () {
      return getPathDefinitionDataAccessManager().getImmediateParentPaths();
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getIndirectPathDefinitionIdsForDirectPath(java.lang.Long)
    */
   public List<Long> getIndirectPathDefinitionIdsForDirectPath (Long pathDefinitionId) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getIndirectPathDefinitionIdsForDirectPath(pathDefinitionId);
      } catch (KDXException kdxException) {
         throw new SWIException(kdxException.getCode(), kdxException);
      }
   }

   public List<PathDefinition> getIndirectPaths (List<PathDefinition> directPaths, Set<String> existingTriples)
            throws SWIException {
      // Logic --
      // TO get Indirect Paths, we have to connect all those direct paths where end of one path is start of another
      // This has to be done till no more additional paths can be found
      // Also it is important to make sure that we do not get any Cyclic Paths
      //
      // Algorithm --
      // 1. First Get the Direct paths from KDX i.e. Paths with Length 1
      // 2. One set of Paths is always this List of Single Length Paths (path1)
      // 3. Other set of Paths is always the latest set of indirect paths found (path2)
      // 4. Thus every iteration, we will get paths with increasing length by one, till we do not find more paths
      // 5. For getting new Path, check if End of "path1" is same as "path2" or vice versa
      // 5.1. To ensure that we do not get Cyclic paths, check if new destination is already part of existing path

      // One input is direct paths
      // Other input is newly created "n" length indirect paths
      // But at start as "n" value is 1 it is same as input direct paths
      List<PathDefinition> pathsByLength = new ArrayList<PathDefinition>(directPaths);
      // Map of Path to List of domain entities that are part of it
      Map<String, List<List<Long>>> pathToTriplesMap = new HashMap<String, List<List<Long>>>();
      // List of Final Set of Paths
      List<PathDefinition> finalIndirectPaths = new ArrayList<PathDefinition>();
      // Check for Limiting condition
      while (!pathsByLength.isEmpty()) {
         // List contains indirect paths list found during each hop(iteration)
         List<PathDefinition> newIndirectPaths = new ArrayList<PathDefinition>();
         // Iterate over each path of direct path
         for (PathDefinition directPath : directPaths) {
            // Iterate over Paths of Length "n"
            for (PathDefinition nLengthPath : pathsByLength) {
               // Check for Self Comparison, needed only for paths with length 1
               if (directPath.equals(nLengthPath)) {
                  continue;
               }
               // Check for Connection between path1 and path2
               Long directPathSourceID = directPath.getSourceBusinessEntityDefinition().getId();
               Long directPathDestinationID = directPath.getDestinationBusinessEntityDefinition().getId();
               Long nLengthPathSourceID = nLengthPath.getSourceBusinessEntityDefinition().getId();
               Long nLengthPathDestinationID = nLengthPath.getDestinationBusinessEntityDefinition().getId();

               EntityTripleDefinitionType sourceType = directPath.getType();
               EntityTripleDefinitionType destinationType = nLengthPath.getType();

               // Check if any one is CRA or ARA path, if yes then do not proceed
               if (sourceType.equals(EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE)
                        || destinationType.equals(EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE)) {
                  continue;
               }
               if (sourceType.equals(EntityTripleDefinitionType.ATTRIBUTE_RELATION_ATTRIBUTE)
                        && !destinationType.equals(EntityTripleDefinitionType.ATTRIBUTE_RELATION_ATTRIBUTE)
                        || !sourceType.equals(EntityTripleDefinitionType.ATTRIBUTE_RELATION_ATTRIBUTE)
                        && destinationType.equals(EntityTripleDefinitionType.ATTRIBUTE_RELATION_ATTRIBUTE)) {
                  continue;
               }

               PathDefinition newPath = null;
               if (directPathDestinationID.equals(nLengthPathSourceID)
                        && !directPathSourceID.equals(nLengthPathDestinationID)
                        && !directPathDestinationID.equals(nLengthPathDestinationID)) {
                  String key = directPathSourceID + "-" + nLengthPathDestinationID;
                  boolean addPath = false;
                  if (pathToTriplesMap.containsKey(key)) {
                     boolean foundPath = true;
                     List<List<Long>> pathTripls = pathToTriplesMap.get(key);
                     for (List<Long> pathTrpl : pathTripls) {
                        foundPath = true;
                        if (CollectionUtils.isEmpty(directPath.getPaths())) {
                           if (logger.isDebugEnabled()) {
                              logger.debug("PathDef Id: " + directPath.getId());
                           }
                        }
                        foundPath = foundPath
                                 && pathTrpl.contains(((Path) directPath.getPaths().toArray()[0])
                                          .getEntityTripleDefinition().getId());
                        for (Path pth : nLengthPath.getPaths()) {
                           foundPath = foundPath && pathTrpl.contains(pth.getEntityTripleDefinition().getId());
                        }
                        if (foundPath) {
                           break;
                        }
                     }
                     if (!foundPath) {
                        addPath = true;
                        List<Long> pathTrpl = new ArrayList<Long>();
                        pathTrpl.add(((Path) directPath.getPaths().toArray()[0]).getEntityTripleDefinition().getId());
                        for (Path pth : nLengthPath.getPaths()) {
                           pathTrpl.add(pth.getEntityTripleDefinition().getId());
                        }
                        pathTripls.add(pathTrpl);
                     }
                  } else {
                     addPath = true;
                     List<Long> pathTrpl = new ArrayList<Long>();
                     pathTrpl.add(((Path) directPath.getPaths().toArray()[0]).getEntityTripleDefinition().getId());
                     for (Path pth : nLengthPath.getPaths()) {
                        pathTrpl.add(pth.getEntityTripleDefinition().getId());
                     }
                     List<List<Long>> pathTripls = new ArrayList<List<Long>>();
                     pathTripls.add(pathTrpl);
                     pathToTriplesMap.put(key, pathTripls);
                  }
                  if (!addPath) {
                     continue;
                  }
                  // Get new Length
                  int length = directPath.getPathLength() + nLengthPath.getPathLength();
                  // Create new Indirect Path
                  newPath = new PathDefinition();
                  // Get new Path Length
                  newPath.setPathLength(length);
                  // Set Priority
                  newPath.setPriority(1);
                  // Source is source of path1
                  newPath.setSourceBusinessEntityDefinition(directPath.getSourceBusinessEntityDefinition());
                  // Destination is destination of path2
                  newPath.setDestinationBusinessEntityDefinition(nLengthPath.getDestinationBusinessEntityDefinition());
                  newPath.setCentralConceptType(CheckType.NO);
                  // Type is CRC - It is important to note that all indirect paths must be CRC/ARA paths
                  if (getKdxModelService().isAttribute(nLengthPath.getDestinationBusinessEntityDefinition().getId())) {
                     if (getKdxModelService().isAttribute(directPath.getSourceBusinessEntityDefinition().getId())) {
                        newPath.setType(EntityTripleDefinitionType.ATTRIBUTE_RELATION_ATTRIBUTE);
                     } else {
                        newPath.setType(EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
                     }
                  } else {
                     newPath.setType(EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT);
                  }
                  Set<Path> paths = new HashSet<Path>();
                  int i = 0;
                  List<Path> pathsToAdd = new ArrayList<Path>();
                  pathsToAdd.addAll(directPath.getPaths());
                  pathsToAdd.addAll(nLengthPath.getPaths());
                  for (Path path : pathsToAdd) {
                     EntityTripleDefinition triple = path.getEntityTripleDefinition();
                     Path pathEtd = new Path();
                     pathEtd.setEntityTripleDefinition(triple);
                     pathEtd.setEntityTripleOrder(i++);
                     pathEtd.setPathDefinition(newPath);
                     paths.add(pathEtd);

                  }
                  newPath.setPaths(paths);
               }

               if (newPath != null) {
                  if (directPath.getHierarchyType().equals(HierarchyType.PARENTAGE)
                           && nLengthPath.getHierarchyType().equals(HierarchyType.PARENTAGE)) {
                     newPath.setHierarchyType(HierarchyType.PARENTAGE);
                  } else {
                     newPath.setHierarchyType(HierarchyType.NON_HIERARCHICAL);
                  }

                  // TODO: NK: Check with AP/NA if the below duplicate check is fine??
                  Object[] paths = newPath.getPaths().toArray();
                  Long relId = ((Path) paths[0]).getEntityTripleDefinition().getRelation().getRelation().getId();
                  String key = newPath.getSourceBusinessEntityDefinition().getId() + "-" + relId + "-"
                           + newPath.getDestinationBusinessEntityDefinition().getId() + "-" + newPath.getPathLength();
                  if (existingTriples.contains(key)) {
                     continue;
                  }
                  newIndirectPaths.add(newPath);
                  existingTriples.add(key);
               }
            }
         }
         pathsByLength = new ArrayList<PathDefinition>(newIndirectPaths);
         finalIndirectPaths.addAll(newIndirectPaths);
      }
      return finalIndirectPaths;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getInstancePathDefinition(java.lang.Long,
    *      java.lang.Long, java.lang.Long)
    */
   public InstancePathDefinition getInstancePathDefinition (Long sourceBEDId, Long destinationBEDId,
            Long pathDefinitionId) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getInstancePathDefinition(sourceBEDId, destinationBEDId,
                  pathDefinitionId);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getInstancePathDefinitionsForDestinationAndPath(java.lang
    *      .Long, java.lang.Long)
    */
   public List<InstancePathDefinition> getInstancePathDefinitionsByDestinationAndPath (Long destinationBEDId,
            Long pathDefinitionId) throws SWIException {
      try {
         List<InstancePathDefinition> returnPaths = new ArrayList<InstancePathDefinition>();
         List<InstancePathDefinition> paths = getPathDefinitionDataAccessManager()
                  .getInstancePathDefinitionsByDestinationAndPathIDs(destinationBEDId, pathDefinitionId);
         for (InstancePathDefinition path : paths) {
            if (!returnPaths.contains(path)) {
               returnPaths.add(path);
            }
         }
         return returnPaths;
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getInstancePathDefinitionsByPath(java.lang.Long)
    */
   public List<InstancePathDefinition> getInstancePathDefinitionsByPath (Long pathDefinitionId) throws SWIException {
      try {
         List<InstancePathDefinition> returnPaths = new ArrayList<InstancePathDefinition>();
         List<InstancePathDefinition> paths = getPathDefinitionDataAccessManager().getInstancePathDefinitionsByPathID(
                  pathDefinitionId);
         for (InstancePathDefinition path : paths) {
            if (!returnPaths.contains(path)) {
               returnPaths.add(path);
            }
         }
         return returnPaths;
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getInstancePathDefinitionsBySourceAndPathIDs(java.lang.Long,
    *      java.lang.Long)
    */
   public List<InstancePathDefinition> getInstancePathDefinitionsBySourceAndPath (Long sourceBEDId,
            Long pathDefinitionId) throws SWIException {
      try {
         List<InstancePathDefinition> returnPaths = new ArrayList<InstancePathDefinition>();
         List<InstancePathDefinition> paths = getPathDefinitionDataAccessManager()
                  .getInstancePathDefinitionsBySourceAndPathIDs(sourceBEDId, pathDefinitionId);
         for (InstancePathDefinition path : paths) {
            if (!returnPaths.contains(path)) {
               returnPaths.add(path);
            }
         }
         return returnPaths;
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public InstanceTripleDefinition getInstanceTripleDefinition (Long sourceBeId, Long relationBeId, Long destinationBeId)
            throws SWIException {
      return getPathDefinitionDataAccessManager()
               .getInstanceTripleDefinition(sourceBeId, relationBeId, destinationBeId);

   }

   public List<InstanceTripleDefinition> getInstanceTripleDefinitionsByDestinationId (Long destinationInstanceBedId)
            throws SWIException {
      return getPathDefinitionDataAccessManager().getInstanceTripleDefinitionsByDestinationInstanceBedId(
               destinationInstanceBedId);
   }

   public List<Long> getInstanceTripleDestIdsBySrcBeIds (Set<Long> instanceIdsInQuery) throws SWIException {
      return getPathDefinitionDataAccessManager().getInstanceTripleDestIdsBySrcBeIds(instanceIdsInQuery);

   }

   public List<Long> getInstanceTripleSrcIdsByDestBeIds (Set<Long> instanceIdsInQuery) throws SWIException {
      return getPathDefinitionDataAccessManager().getInstanceTripleSrcIdsByDestBeIds(instanceIdsInQuery);
   }

   public List<Long> getNonBaseEntityTripleDefinitions (List<ModelGroup> userModelGroups) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getNonBaseEntityTripleDefinitions(userModelGroups);
      } catch (KDXException kdxException) {
         throw new SWIException(kdxException.getCode(), kdxException);
      }
   }

   public List<Long> getNonBasePathDefinitions (List<ModelGroup> userModelGroups) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getNonBasePathDefinitions(userModelGroups);
      } catch (KDXException kdxException) {
         throw new SWIException(kdxException.getCode(), kdxException);
      }
   }

   public List<Long> getNonBaseToBaseEntityTripleDefinitions (List<ModelGroup> userModelGroups,
            ModelGroup baseModelGroup) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getNonBaseToBaseEntityTripleDefinitions(userModelGroups,
                  baseModelGroup);
      } catch (KDXException kdxException) {
         throw new SWIException(kdxException.getCode(), kdxException);
      }
   }

   public List<Long> getNonBaseToBasePathDefinitions (List<ModelGroup> userModelGroups, ModelGroup baseModelGroup)
            throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getNonBaseToBasePathDefinitions(userModelGroups, baseModelGroup);
      } catch (KDXException kdxException) {
         throw new SWIException(kdxException.getCode(), kdxException);
      }
   }

   public List<PathDefinition> getNonParentCCPathsForDestination (Long destBedId) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getNonParentCCPathsForDestination(destBedId);
      } catch (DataAccessException e) {
         throw new SWIException(e.code, e);
      }
   }

   public List<PathDefinition> getNonParentPathsByDestBedIds (Set<Long> destBEDIds, Long modelGroupId)
            throws SWIException {
      List<PathDefinition> pathDefinitionsByDestIdAndPathSelectionType = getPathDefinitionDataAccessManager()
               .getNonParentPathsByDestBedIds(destBEDIds, modelGroupId);
      navigatePathDefinitions(pathDefinitionsByDestIdAndPathSelectionType);
      return pathDefinitionsByDestIdAndPathSelectionType;
   }

   public List<PathDefinition> getNonParentPathsByDestIdAndPathSelectionType (Set<Long> destBeIds, Long modelId,
            PathSelectionType pathSelectionType) throws SWIException {
      List<PathDefinition> pathDefinitionsByDestIdAndPathSelectionType = getPathDefinitionDataAccessManager()
               .getNonParentPathsByDestIdAndPathSelectionType(destBeIds, modelId, pathSelectionType);
      navigatePathDefinitions(pathDefinitionsByDestIdAndPathSelectionType);
      return pathDefinitionsByDestIdAndPathSelectionType;
   }

   public List<PathDefinition> getNonParentPathsBySrcBedIds (Set<Long> srcBEDIds, Long modelGroupId)
            throws SWIException {
      List<PathDefinition> pathDefinitionsByDestIdAndPathSelectionType = getPathDefinitionDataAccessManager()
               .getNonParentPathsBySrcBedIds(srcBEDIds, modelGroupId);
      navigatePathDefinitions(pathDefinitionsByDestIdAndPathSelectionType);
      return pathDefinitionsByDestIdAndPathSelectionType;
   }

   public PathDefinition getPathDefinition (BusinessEntityDefinition sourceEntityDefinition,
            BusinessEntityDefinition destinationBusinessEntityDefinition) throws SWIException {
      try {
         PathDefinition pathDefinition = getPathDefinitionDataAccessManager().getPathDefinition(sourceEntityDefinition,
                  destinationBusinessEntityDefinition);
         navigatePathDefinition(pathDefinition);
         return pathDefinition;
      } catch (DataAccessException e) {
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getPathDefinitionById(java.lang.Long)
    */
   public PathDefinition getPathDefinitionById (Long pathDefinitionId) throws SWIException {
      try {
         PathDefinition pathDefinition = getPathDefinitionDataAccessManager().getPathDefinitionById(pathDefinitionId);
         navigatePathDefinition(pathDefinition);
         return pathDefinition;
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getPathDefinitionIds(java.lang.Long, java.lang.Long)
    */
   public List<Long> getPathDefinitionIds (Long sourceBEDId, Long destinationBEDId) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getPathDefinitionIds(sourceBEDId, destinationBEDId);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * @seecom.execue.core.common.api.swi.IPathDefinitionService#getPathDefinitions(com.execue.core.common.bean.entity.
    * BusinessEntityDefinition, com.execue.core.common.bean.entity.BusinessEntityDefinition)
    */
   public List<PathDefinition> getPathDefinitions (BusinessEntityDefinition sourceEntityDefinition,
            BusinessEntityDefinition destinationBusinessEntityDefinition) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getPathDefinitions(sourceEntityDefinition,
                  destinationBusinessEntityDefinition);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getPathDefinitionIds(java.lang.Long, java.lang.Long)
    */
   public List<PathDefinition> getPathDefinitions (Long sourceBEDId, Long destinationBEDId) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getPathDefinitions(sourceBEDId, destinationBEDId);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   public List<PathDefinition> getPathDefinitionsBySrcBedAndRelation (Long bedId, Long relationBedId)
            throws SWIException {
      List<PathDefinition> pathDefinitionsBySrcBedAndRelation = getPathDefinitionDataAccessManager()
               .getPathDefinitionsBySrcBedAndRelation(bedId, relationBedId);
      navigatePathDefinitions(pathDefinitionsBySrcBedAndRelation);
      return pathDefinitionsBySrcBedAndRelation;
   }

   public List<PathDefinition> getRelatedNonParentDirectCCPaths (Long cloudId, Long sourceBedId, Long destinationBedId)
            throws KDXException {
      List<PathDefinition> paths = getPathDefinitionDataAccessManager().getRelatedNonParentDirectCCPaths(cloudId,
               sourceBedId, destinationBedId);
      navigatePathDefinitions(paths);
      return paths;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getSourceBEDIds(java.lang.Long, java.lang.Long)
    */
   public List<Long> getSourceBEDIds (Long destinationBEDId, Long pathDefinitionId) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getSourceBEDIds(destinationBEDId, pathDefinitionId);
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(dataAccessException.getCode(), dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IPathDefinitionService#getIndirectPathDefinitionIdsForDirectPath(java.lang.Long)
    */
   public List<Long> getPathDefinitionIdsForETD (Long etdId) throws SWIException {
      try {
         return getPathDefinitionDataAccessManager().getPathDefinitionIdsForETD(etdId);
      } catch (KDXException kdxException) {
         throw new SWIException(kdxException.getCode(), kdxException);
      }
   }

   public IPathDefinitionDataAccessManager getPathDefinitionDataAccessManager () {
      return pathDefinitionDataAccessManager;
   }

   public void setPathDefinitionDataAccessManager (IPathDefinitionDataAccessManager pathDefinitionDataAccessManager) {
      this.pathDefinitionDataAccessManager = pathDefinitionDataAccessManager;
   }

   public Map<Long, List<PathDefinition>> getDirectValuePathDefinitonByDestId (Long cloudId, Long modelId)
            throws KDXException {
      return getPathDefinitionDataAccessManager().getDirectValuePathDefinitonByDestId(cloudId, modelId);
   }

   public List<PathDefinition> getAllDirectPaths (Long entityBedId) throws KDXException {
      return getPathDefinitionDataAccessManager().getAllDirectPaths(entityBedId);
   }

   /**
    * @return the kdxModelService
    */
   public IKDXModelService getKdxModelService () {
      return kdxModelService;
   }

   /**
    * @param kdxModelService
    *           the kdxModelService to set
    */
   public void setKdxModelService (IKDXModelService kdxModelService) {
      this.kdxModelService = kdxModelService;
   }

}
