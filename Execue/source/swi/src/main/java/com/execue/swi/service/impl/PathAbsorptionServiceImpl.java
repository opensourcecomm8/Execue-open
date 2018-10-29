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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.EntityTriplePropertyType;
import com.execue.core.common.type.HierarchyType;
import com.execue.core.common.type.OriginType;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IPathAbsorptionService;
import com.execue.swi.service.IPathDefinitionDeletionService;
import com.execue.swi.service.IPathDefinitionManagementService;
import com.execue.swi.service.IPathDefinitionRetrievalService;

/**
 * This class contains the operations to absorb the paths
 * 
 * @author Nitesh
 * @version 1.0
 * @since 02/06/10
 */
public class PathAbsorptionServiceImpl implements IPathAbsorptionService {

   private static final Logger              logger = Logger.getLogger(PathAbsorptionServiceImpl.class);

   private IPathDefinitionRetrievalService  pathDefinitionRetrievalService;
   private IPathDefinitionDeletionService   pathDefinitionDeletionService;
   private IPathDefinitionManagementService pathDefinitionManagementService;
   private IKDXModelService                 kdxModelService;

   public void absorbIndirectPaths (Long cloudId, Long modelId) throws SWIException {

      // Get and delete the existing indirect triples for cloud
      List<Long> pathDefinitionIds = getPathDefinitionRetrievalService().getAllIndirectPathDefIdsForCloud(cloudId);
      if (!CollectionUtils.isEmpty(pathDefinitionIds)) {
         getPathDefinitionDeletionService().deletePathDefinitions(pathDefinitionIds);
      }

      // Get the existing triples
      Set<String> existingTriples = getPathDefinitionRetrievalService().getAllPathDefTriplesForModel(modelId);

      if (CollectionUtils.isEmpty(existingTriples)) {
         if (logger.isDebugEnabled()) {
            logger.debug("No Paths defined for the given model id: " + modelId);
         }
         existingTriples = new HashSet<String>();
      }

      // Step 1. Absorb Non Parent Indirect Triples
      if (logger.isDebugEnabled()) {
         logger.debug("Step 1. Absorb Non Parent Indirect Triples");
      }
      List<PathDefinition> indirectPaths = getNonParentIndirectCCPaths(modelId, existingTriples);
      for (PathDefinition path : indirectPaths) {
         path.setCloudId(cloudId);
         getPathDefinitionManagementService().createPathDefinition(path);
      }

      // Step 2. Absorb Parent Indirect Triple
      if (logger.isDebugEnabled()) {
         logger.debug("Step 2. Absorb Indirect Parent Triple");
      }
      List<PathDefinition> parentPaths = getPathDefinitionRetrievalService().getAllParentPaths(modelId);
      List<PathDefinition> indirectParentPaths = getParentIndirectPaths(parentPaths, existingTriples);
      for (PathDefinition path : indirectParentPaths) {
         path.setCloudId(cloudId);
         getPathDefinitionManagementService().createPathDefinition(path);
      }

      // Step 3. Assign Parent Properties to Children
      if (logger.isDebugEnabled()) {
         logger.debug("Step 3. Assign Parent Properties to Children");
      }
      parentPaths = getPathDefinitionRetrievalService().getAllParentPaths(modelId);
      // TODO: NK: Check with AP/NA should we skip the ARA paths here??
      List<PathDefinition> propagatedDefinitions = absorbParentPropertiesInAllChildConcepts(parentPaths, modelId,
               existingTriples, true);
      for (PathDefinition path : propagatedDefinitions) {
         path.setCloudId(cloudId);
         getPathDefinitionManagementService().createPathDefinition(path);
      }

      // Step 4. Relate children as destination where Parent is related as destination
      if (logger.isDebugEnabled()) {
         logger.debug("Step 4. Relate children as destination where parent is related to destination");
      }
      List<PathDefinition> childDestinationPaths = assignChildrenAsRangeInPlaceOfParent(parentPaths, modelId,
               existingTriples);
      for (PathDefinition path : childDestinationPaths) {
         path.setCloudId(cloudId);
         getPathDefinitionManagementService().createPathDefinition(path);
      }

      // At the end, mark the central concept paths
      getPathDefinitionManagementService().markCentralConceptPaths(modelId);
   }

   private List<PathDefinition> getNonParentIndirectCCPaths (Long modelId, Set<String> existingTriples)
            throws SWIException {
      List<PathDefinition> directPaths = getPathDefinitionRetrievalService().getAllNonParentDirectCCPaths(modelId);
      return getPathDefinitionRetrievalService().getIndirectPaths(directPaths, existingTriples);
   }

   private List<PathDefinition> getParentIndirectPaths (List<PathDefinition> parentPaths, Set<String> existingTriples)
            throws SWIException {
      return getPathDefinitionRetrievalService().getIndirectPaths(parentPaths, existingTriples);
   }

   public List<PathDefinition> absorbParentPropertiesInAllChildConcepts (List<PathDefinition> parentPaths,
            Long modelId, Set<String> existingTriples) throws SWIException {
      return absorbParentPropertiesInAllChildConcepts(parentPaths, modelId, existingTriples, false);
   }

   public List<PathDefinition> absorbParentPropertiesInAllChildConcepts (List<PathDefinition> parentPaths,
            Long modelId, Set<String> existingTriples, boolean excludeARA) throws SWIException {
      // 1. get all parent concepts, i.e. get All parent paths, then their source id.
      // 2. For each parent concept, get all non parent direct paths
      // 3. Get all children of that Parent,
      // using same parent path but this time searching by source, and getting destinations.
      // 4. for each such path, create a new path with each children as source and current destination as destination.
      List<PathDefinition> newPathDefinitions = new ArrayList<PathDefinition>();
      for (PathDefinition parentPathDefinition : parentPaths) {
         long parentBedId = parentPathDefinition.getSourceBusinessEntityDefinition().getId();
         List<PathDefinition> nonParentDirectPaths = getPathDefinitionRetrievalService()
                  .getAllNonParentDirectPathsBySourceId(parentBedId, modelId);
         for (PathDefinition directPath : nonParentDirectPaths) {
            if (excludeARA && EntityTripleDefinitionType.ATTRIBUTE_RELATION_ATTRIBUTE == directPath.getType()) {
               continue;
            }
            BusinessEntityDefinition source = parentPathDefinition.getDestinationBusinessEntityDefinition();
            BusinessEntityDefinition destination = directPath.getDestinationBusinessEntityDefinition();
            for (Path path : directPath.getPaths()) {
               BusinessEntityDefinition relation = path.getEntityTripleDefinition().getRelation();
               String key = source.getId() + "-" + relation.getId() + "-" + destination.getId() + "-1";
               if (existingTriples.contains(key)) {
                  if (logger.isDebugEnabled()) {
                     logger.debug("Skipping duplicate triple in absorbParentPropertiesInAllChildConcepts: " + key);
                  }
                  continue;
               }
               PathDefinition pd = createPathDefinition(source, relation, destination, path, OriginType.Hierarchy);
               newPathDefinitions.add(pd);
               existingTriples.add(key);
            }
         }
      }
      return newPathDefinitions;
   }

   public List<PathDefinition> assignChildrenAsRangeInPlaceOfParent (List<PathDefinition> parentPaths, Long modelId,
            Set<String> existingTriples) throws SWIException {
      List<PathDefinition> newPathDefinitions = new ArrayList<PathDefinition>(1);
      for (PathDefinition parentPathDefinition : parentPaths) {
         long parentBedId = parentPathDefinition.getSourceBusinessEntityDefinition().getId();
         List<PathDefinition> directPaths = getPathDefinitionRetrievalService()
                  .getAllNonParentDirectPathsByDestinationId(parentBedId, modelId);
         for (PathDefinition directPath : directPaths) {
            if (EntityTripleDefinitionType.ATTRIBUTE_RELATION_ATTRIBUTE.equals(directPath.getType())) {
               continue;
            }
            BusinessEntityDefinition source = directPath.getSourceBusinessEntityDefinition();
            BusinessEntityDefinition destination = parentPathDefinition.getDestinationBusinessEntityDefinition();
            for (Path path : directPath.getPaths()) {
               BusinessEntityDefinition relation = path.getEntityTripleDefinition().getRelation();
               String key = source.getId() + "-" + relation.getId() + "-" + destination.getId() + "-1";
               if (existingTriples.contains(key)) {
                  if (logger.isDebugEnabled()) {
                     logger.debug("Skipping duplicate triple in assignChildrenAsRangeInPlaceOfParent: " + key);
                  }
                  continue;
               }
               // Create the path definition
               PathDefinition pd = createPathDefinition(source, relation, destination, path, OriginType.Hierarchy);
               newPathDefinitions.add(pd);
               existingTriples.add(key);
            }
         }
      }
      return newPathDefinitions;
   }

   /**
    * @param directPath
    * @param source
    * @param relation
    * @param destination
    * @param path
    * @param originType
    * @return
    */
   private PathDefinition createPathDefinition (BusinessEntityDefinition source, BusinessEntityDefinition relation,
            BusinessEntityDefinition destination, Path path, OriginType originType) throws SWIException {
      // Create Entity triple for this
      EntityTripleDefinition etd = new EntityTripleDefinition();
      etd.setCardinality(path.getEntityTripleDefinition().getCardinality());
      etd.setDestinationBusinessEntityDefinition(destination);
      etd.setOrigin(originType);
      if (getKdxModelService().isAttribute(destination.getId())) {
         etd.setTripleType(EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
      } else {
         etd.setTripleType(EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT);
      }
      etd.setSourceBusinessEntityDefinition(source);
      etd.setRelation(relation);
      etd.setBaseETDId(path.getEntityTripleDefinition().getId());
      etd.setFunctional(path.getEntityTripleDefinition().isFunctional());
      etd.setInverseFunctional(path.getEntityTripleDefinition().isInverseFunctional());
      etd.setRelationSpecified(path.getEntityTripleDefinition().isRelationSpecified());
      etd.setPropertyType(EntityTriplePropertyType.NORMAL);

      // Create path Definition
      PathDefinition pd = new PathDefinition();
      pd.setSourceBusinessEntityDefinition(source);
      pd.setDestinationBusinessEntityDefinition(destination);
      if (getKdxModelService().isAttribute(destination.getId())) {
         pd.setType(EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
      } else {
         pd.setType(EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT);
      }
      pd.setHierarchyType(HierarchyType.NON_HIERARCHICAL);
      pd.setPriority(1);
      pd.setPathLength(1);
      pd.setCentralConceptType(CheckType.NO);
      Set<Path> newPaths = new HashSet<Path>();

      // Create Path
      Path newPath = new Path();
      newPath.setEntityTripleDefinition(etd);
      newPath.setPathDefinition(pd);
      newPath.setEntityTripleOrder(path.getEntityTripleOrder());
      newPaths.add(newPath);
      pd.setPaths(newPaths);
      return pd;
   }

   public IPathDefinitionRetrievalService getPathDefinitionRetrievalService () {
      return pathDefinitionRetrievalService;
   }

   public void setPathDefinitionRetrievalService (IPathDefinitionRetrievalService pathDefinitionRetrievalService) {
      this.pathDefinitionRetrievalService = pathDefinitionRetrievalService;
   }

   public IPathDefinitionDeletionService getPathDefinitionDeletionService () {
      return pathDefinitionDeletionService;
   }

   public void setPathDefinitionDeletionService (IPathDefinitionDeletionService pathDefinitionDeletionService) {
      this.pathDefinitionDeletionService = pathDefinitionDeletionService;
   }

   public IPathDefinitionManagementService getPathDefinitionManagementService () {
      return pathDefinitionManagementService;
   }

   public void setPathDefinitionManagementService (IPathDefinitionManagementService pathDefinitionManagementService) {
      this.pathDefinitionManagementService = pathDefinitionManagementService;
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