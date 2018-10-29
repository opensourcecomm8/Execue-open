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
package com.execue.swi.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.Rule;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.PublishedFileType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.dataaccess.IKDXDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * @author Nihar
 */
public class KDXModelServiceImpl implements IKDXModelService {

   private IKDXDataAccessManager        kdxDataAccessManager;
   private IKDXRetrievalService         kdxRetrievalService;
   private ISDXRetrievalService         sdxRetrievalService;
   private IApplicationRetrievalService applicationRetrievalService;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getAllAbstractConcepts()
    */
   public List<BusinessEntityDefinition> getAllAbstractConcepts () throws KDXException {
      return getKdxDataAccessManager().getAllAbstractConcepts();
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getAllAttributeConcepts()
    */
   public List<BusinessEntityDefinition> getAllAttributeConcepts () throws KDXException {
      return getKdxDataAccessManager().getAllAbstractConcepts();
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getAllComparativeConcepts()
    */
   public List<BusinessEntityDefinition> getAllComparativeConcepts () throws KDXException {
      return getKdxDataAccessManager().getAllComparativeConcepts();
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getAllDistributionConcepts()
    */
   public List<BusinessEntityDefinition> getAllDistributionConcepts () throws KDXException {
      return getKdxDataAccessManager().getAllDistributionConcepts();
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getAllEnumerationConcepts()
    */
   public List<BusinessEntityDefinition> getAllEnumerationConcepts () throws KDXException {
      return getKdxDataAccessManager().getAllEnumerationConcepts();
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getAllQuantitativeConcepts()
    */
   public List<BusinessEntityDefinition> getAllQuantitativeConcepts () throws KDXException {
      return getKdxDataAccessManager().getAllQuantitativeConcepts();
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getConceptToAttributePathDefinitionDetails(java.lang.Long,
    *      java.lang.Long)
    */
   public List<PathDefinition> getConceptToAttributePathDefinitionDetails (Long sourceBEDId, Long destBEDId)
            throws KDXException {
      List<PathDefinition> pdList = getKdxDataAccessManager().getConceptToAttributePathDefinitionDetails(sourceBEDId,
               destBEDId);
      for (PathDefinition pathDefinition : pdList) {
         pathDefinition.getPathLength();
         for (Path path : pathDefinition.getPaths()) {
            path.getEntityTripleDefinition().getCardinality();
            path.getEntityTripleDefinition().getDestinationBusinessEntityDefinition().getEntityType();
            path.getEntityTripleDefinition().getSourceBusinessEntityDefinition().getEntityType();
            path.getEntityTripleDefinition().getRelation().getEntityType();
         }
      }
      return pdList;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getConceptToConceptPathDefinitionDetails(java.lang.Long,
    *      java.lang.Long)
    */
   public List<PathDefinition> getConceptToConceptPathDefinitionDetails (Long sourceBEDId, Long destBEDId)
            throws KDXException {
      List<PathDefinition> pdList = getKdxDataAccessManager().getConceptToConceptPathDefinitionDetails(sourceBEDId,
               destBEDId);
      for (PathDefinition pathDefinition : pdList) {
         pathDefinition.getPathLength();
         for (Path path : pathDefinition.getPaths()) {
            path.getEntityTripleDefinition().getCardinality();
            path.getEntityTripleDefinition().getDestinationBusinessEntityDefinition().getEntityType();
            path.getEntityTripleDefinition().getSourceBusinessEntityDefinition().getEntityType();
            path.getEntityTripleDefinition().getRelation().getEntityType();
         }
      }
      return pdList;
   }

   public PathDefinition getConceptToConceptShortestPathDefinitionDetails (Long sourceDEDId, Long destDEDId)
            throws KDXException {
      List<PathDefinition> pdList = getKdxDataAccessManager().getConceptToConceptPathDefinitionDetails(sourceDEDId,
               destDEDId);
      PathDefinition pathDefinition = null;
      if (pdList != null && !pdList.isEmpty()) {
         pathDefinition = pdList.get(0);
         pathDefinition.getPathLength();
         for (Path path : pathDefinition.getPaths()) {
            path.getEntityTripleDefinition().getCardinality();
            path.getEntityTripleDefinition().getDestinationBusinessEntityDefinition().getEntityType();
            path.getEntityTripleDefinition().getSourceBusinessEntityDefinition().getEntityType();
            path.getEntityTripleDefinition().getRelation().getEntityType();
         }
      }
      return pathDefinition;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getBusinessEntityType(java.lang.Long)
    */
   public BusinessEntityType getBusinessEntityType (Long bedId) throws KDXException {
      return getKdxDataAccessManager().getBusinessEntityType(bedId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getEntityTriple(java.lang.Long, java.lang.Long,
    *      java.lang.Long)
    */
   public EntityTripleDefinition getEntityTriple (Long sourceBEDId, Long relationBEDId, Long destBEDId)
            throws KDXException {
      return getKdxDataAccessManager().getEntityTriple(sourceBEDId, relationBEDId, destBEDId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getEntityTriplesForDestination(java.lang.Long)
    */
   public List<EntityTripleDefinition> getEntityTriplesForDestination (Long destBEDId) throws KDXException {
      List<EntityTripleDefinition> tripleList = getKdxDataAccessManager().getEntityTriplesForDestination(destBEDId);
      for (EntityTripleDefinition triple : tripleList) {
         BusinessEntityDefinition sourceBED = triple.getSourceBusinessEntityDefinition();
         Concept concept = sourceBED.getConcept();
         if (concept != null) {
            concept.getName();
         }
         BusinessEntityDefinition relBED = triple.getRelation();
         relBED.getRelation().getDisplayName();
         BusinessEntityDefinition destBED = triple.getDestinationBusinessEntityDefinition();
         concept = destBED.getConcept();
         if (concept != null) {
            concept.getName();
         }
      }
      return tripleList;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getEntityTriplesForDestinationAndTripleType(java.lang.Long,
    *      com.execue.core.common.type.EntityTripleDefinitionType)
    */
   public List<EntityTripleDefinition> getEntityTriplesForDestinationAndTripleType (Long destBEDId,
            EntityTripleDefinitionType tripleType) throws KDXException {
      List<EntityTripleDefinition> tripleList = getKdxDataAccessManager().getEntityTriplesForDestinationAndTripleType(
               destBEDId, tripleType);
      for (EntityTripleDefinition triple : tripleList) {
         BusinessEntityDefinition sourceBED = triple.getSourceBusinessEntityDefinition();
         if (sourceBED.getConcept() != null) {
            sourceBED.getConcept().getName();
         }
         BusinessEntityDefinition relBED = triple.getRelation();
         relBED.getRelation().getDisplayName();
         BusinessEntityDefinition destBED = triple.getDestinationBusinessEntityDefinition();
         if (destBED.getConcept() != null) {
            destBED.getConcept().getName();
         }
      }
      return tripleList;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getEntityTriplesForDestinationAndRelation(java.lang.Long,
    *      java.lang.Long)
    */
   public List<EntityTripleDefinition> getEntityTriplesForDestinationAndRelation (Long relationBEDId, Long destBEDId)
            throws KDXException {
      List<EntityTripleDefinition> tripleList = getKdxDataAccessManager().getEntityTriplesForDestinationAndRelation(
               relationBEDId, destBEDId);
      for (EntityTripleDefinition triple : tripleList) {
         BusinessEntityDefinition sourceBED = triple.getSourceBusinessEntityDefinition();
         Concept concept = sourceBED.getConcept();
         if (concept != null) {
            concept.getName();
         }
         BusinessEntityDefinition relBED = triple.getRelation();
         relBED.getRelation().getDisplayName();
         BusinessEntityDefinition destBED = triple.getDestinationBusinessEntityDefinition();
         concept = destBED.getConcept();
         if (concept != null) {
            concept.getName();
         }
      }
      return tripleList;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getEntityTriplesForRelation(java.lang.Long)
    */
   public List<EntityTripleDefinition> getEntityTriplesForRelation (Long relBEDId) throws KDXException {
      return getKdxDataAccessManager().getEntityTriplesForRelation(relBEDId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getEntityTriplesForSource(java.lang.Long)
    */
   public List<EntityTripleDefinition> getEntityTriplesForSource (Long sourceBEDId) throws KDXException {
      List<EntityTripleDefinition> tripleList = getKdxDataAccessManager().getEntityTriplesForSource(sourceBEDId);
      for (EntityTripleDefinition triple : tripleList) {
         BusinessEntityDefinition sourceBED = triple.getSourceBusinessEntityDefinition();
         Concept concept = sourceBED.getConcept();
         if (concept != null) {
            concept.getName();
         }
         BusinessEntityDefinition relBED = triple.getRelation();
         relBED.getRelation().getDisplayName();
         BusinessEntityDefinition destBED = triple.getDestinationBusinessEntityDefinition();
         concept = destBED.getConcept();
         if (concept != null) {
            concept.getName();
         }
      }
      return tripleList;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getEntityTriplesForSourceAndTripleType(java.lang.Long,
    *      com.execue.core.common.type.EntityTripleDefinitionType)
    */
   public List<EntityTripleDefinition> getEntityTriplesForSourceAndTripleType (Long sourceBEDId,
            EntityTripleDefinitionType tripleType) throws KDXException {
      List<EntityTripleDefinition> tripleList = getKdxDataAccessManager().getEntityTriplesForSourceAndTripleType(
               sourceBEDId, tripleType);
      for (EntityTripleDefinition triple : tripleList) {
         BusinessEntityDefinition sourceBED = triple.getSourceBusinessEntityDefinition();
         Concept concept = sourceBED.getConcept();
         if (concept != null) {
            concept.getName();
         }
         BusinessEntityDefinition relBED = triple.getRelation();
         relBED.getRelation().getDisplayName();
         BusinessEntityDefinition destBED = triple.getDestinationBusinessEntityDefinition();
         concept = destBED.getConcept();
         if (concept != null) {
            concept.getName();
         }
      }
      return tripleList;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getEntityTriplesForSourceAndRelation(java.lang.Long,
    *      java.lang.Long)
    */
   public List<EntityTripleDefinition> getEntityTriplesForSourceAndRelation (Long sourceBEDId, Long relationBEDId)
            throws KDXException {
      List<EntityTripleDefinition> tripleList = getKdxDataAccessManager().getEntityTriplesForSourceAndRelation(
               sourceBEDId, relationBEDId);
      for (EntityTripleDefinition triple : tripleList) {
         BusinessEntityDefinition sourceBED = triple.getSourceBusinessEntityDefinition();
         Concept concept = sourceBED.getConcept();
         if (concept != null) {
            concept.getName();
         }
         BusinessEntityDefinition relBED = triple.getRelation();
         relBED.getRelation().getDisplayName();
         BusinessEntityDefinition destBED = triple.getDestinationBusinessEntityDefinition();
         concept = destBED.getConcept();
         if (concept != null) {
            concept.getName();
         }
      }
      return tripleList;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getPathDefinitionDetails(java.lang.Long, java.lang.Long)
    */
   public List<PathDefinition> getPathDefinitionDetails (Long sourceBEDId, Long destBEDId) throws KDXException {
      List<PathDefinition> pdList = getKdxDataAccessManager().getPathDefinitions(sourceBEDId, destBEDId);
      for (PathDefinition pathDefinition : pdList) {
         pathDefinition.getPathLength();
         for (Path path : pathDefinition.getPaths()) {
            path.getEntityTripleDefinition().getCardinality();
            path.getEntityTripleDefinition().getDestinationBusinessEntityDefinition().getEntityType();
            path.getEntityTripleDefinition().getSourceBusinessEntityDefinition().getEntityType();
            path.getEntityTripleDefinition().getRelation().getEntityType();
         }
      }
      return pdList;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.swi.dataaccess.IKDXDataAccessManager#getAllParentPathDefinitionsForDestination(java.lang.Long)
    */
   public List<PathDefinition> getAllParentPathDefinitionsForDestination (Long destinationBEDId) throws KDXException {
      List<PathDefinition> pdList = getKdxDataAccessManager().getAllParentPathDefinitionsForDestination(
               destinationBEDId);
      for (PathDefinition pathDefinition : pdList) {
         pathDefinition.getPathLength();
         for (Path path : pathDefinition.getPaths()) {
            path.getEntityTripleDefinition().getCardinality();
            path.getEntityTripleDefinition().getDestinationBusinessEntityDefinition().getEntityType();
            path.getEntityTripleDefinition().getSourceBusinessEntityDefinition().getEntityType();
            path.getEntityTripleDefinition().getRelation().getEntityType();
         }
      }
      return pdList;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getPathDefinitionDetails(java.lang.Long, java.lang.Long)
    */
   public List<PathDefinition> getParentPathDefinitions (Long sourceBEDId, Long destBEDId) throws KDXException {
      return getKdxDataAccessManager().getParentPathDefinitions(sourceBEDId, destBEDId);
   }

   public List<PathDefinition> getChildPathDefinitionsForSource (Long sourceBEDId) throws KDXException {
      return getKdxDataAccessManager().getChildPathDefinitionsForSource(sourceBEDId);
   }

   public List<BusinessEntityDefinition> getChildPathDestinationBEDsForSourceForModel (Long modelId, Long sourceBEDId)
            throws KDXException {
      List<BusinessEntityDefinition> beds = new ArrayList<BusinessEntityDefinition>();
      List<PathDefinition> pdList = getKdxDataAccessManager().getChildPathDefinitionsForSourceForModel(modelId,
               sourceBEDId);
      for (PathDefinition pathDefinition : pdList) {
         pathDefinition.getPathLength();
         for (Path path : pathDefinition.getPaths()) {
            path.getEntityTripleDefinition().getCardinality();
            beds.add(path.getEntityTripleDefinition().getDestinationBusinessEntityDefinition());
         }
      }
      return beds;
   }

   public Map<Long, Map<Long, String>> getAllChildToParentTriplesForModel (Long modelId) throws KDXException {
      return getKdxDataAccessManager().getAllChildToParentTriplesForModel(modelId);
   }

   public Map<Long, Map<Long, String>> getAllParentToChildTriplesForModel (Long modelId) throws KDXException {
      return getKdxDataAccessManager().getAllParentToChildTriplesForModel(modelId);
   }

   public List<Long> getAllChildBedIds (Long sourceBEDId, Long modelId) throws KDXException {
      List<Long> childBedIds = new ArrayList<Long>(1);
      // TODO: Hard-coding the relation Bed Id, later should get it from db
      Long relationBEDId = 506L;

      List<EntityTripleDefinition> entityTriplesForSourceAndRelation = getKdxDataAccessManager()
               .getEntityTriplesForSourceAndRelation(sourceBEDId, relationBEDId);

      if (CollectionUtils.isEmpty(entityTriplesForSourceAndRelation)) {
         return childBedIds;
      }

      Map<Long, Map<Long, String>> parentToChildInfo = getKdxDataAccessManager().getAllParentToChildTriplesForModel(
               modelId);

      for (EntityTripleDefinition entityTripleDefinition : entityTriplesForSourceAndRelation) {
         Long etdId = entityTripleDefinition.getId();
         childBedIds.add(entityTripleDefinition.getDestinationBusinessEntityDefinition().getId());
         Map<Long, String> parentToChildMap = parentToChildInfo.get(etdId);
         addChildBeds(parentToChildMap, parentToChildInfo, childBedIds);
      }

      return childBedIds;
   }

   private void addChildBeds (Map<Long, String> parentToChildMap, Map<Long, Map<Long, String>> parentToChildInfo,
            List<Long> childBedIds) {
      if (parentToChildMap == null || parentToChildMap.isEmpty()) {
         return;
      }
      for (Entry<Long, String> entry : parentToChildMap.entrySet()) {
         Long childETDId = entry.getKey();
         String childTriple = entry.getValue();
         String childId = childTriple.split("-")[2]; // Add the desination bed id
         childBedIds.add(new Long(childId));
         Map<Long, String> map = parentToChildInfo.get(childETDId);
         addChildBeds(map, parentToChildInfo, childBedIds);
      }
   }

   public List<Long> getAllParentBedIds (Long destBEDId, Long modelId) throws KDXException {
      List<Long> parentBedIds = new ArrayList<Long>(1);
      // TODO: Hard-coding the relation Bed Id, later should get it from db
      Long relationBEDId = 506L;

      List<EntityTripleDefinition> entityTriplesForDestinationAndRelation = getKdxDataAccessManager()
               .getEntityTriplesForDestinationAndRelation(relationBEDId, destBEDId);

      if (CollectionUtils.isEmpty(entityTriplesForDestinationAndRelation)) {
         return parentBedIds;
      }

      Map<Long, Map<Long, String>> childToParentInfo = getKdxDataAccessManager().getAllChildToParentTriplesForModel(
               modelId);
      for (EntityTripleDefinition entityTripleDefinition : entityTriplesForDestinationAndRelation) {
         Long etdId = entityTripleDefinition.getId();
         parentBedIds.add(entityTripleDefinition.getSourceBusinessEntityDefinition().getId());
         Map<Long, String> childToParentMap = childToParentInfo.get(etdId);
         addParentBeds(childToParentMap, childToParentInfo, parentBedIds);
      }

      return parentBedIds;
   }

   private void addParentBeds (Map<Long, String> childToParentMap, Map<Long, Map<Long, String>> childToParentInfo,
            List<Long> parentBedIds) {
      if (childToParentMap == null || childToParentMap.isEmpty()) {
         return;
      }
      for (Entry<Long, String> entry : childToParentMap.entrySet()) {
         Long parentETDId = entry.getKey();
         String parentTriple = entry.getValue();
         String parentId = parentTriple.split("-")[0]; // Add the source bed id
         parentBedIds.add(new Long(parentId));
         Map<Long, String> map = childToParentInfo.get(parentETDId);
         addParentBeds(map, childToParentInfo, parentBedIds);
      }
   }

   public List<BusinessEntityDefinition> getChildPathDestinationBEDsForSource (Long sourceBEDId) throws KDXException {
      List<BusinessEntityDefinition> beds = new ArrayList<BusinessEntityDefinition>();
      List<PathDefinition> pdList = getKdxDataAccessManager().getChildPathDefinitionsForSource(sourceBEDId);
      for (PathDefinition pathDefinition : pdList) {
         pathDefinition.getPathLength();
         for (Path path : pathDefinition.getPaths()) {
            path.getEntityTripleDefinition().getCardinality();
            beds.add(path.getEntityTripleDefinition().getDestinationBusinessEntityDefinition());
         }
      }
      return beds;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getPathDefinitionDetailsLessThanLength(java.lang.Long,
    *      java.lang.Long, int)
    */
   public List<PathDefinition> getPathDefinitionDetailsLessThanLength (Long sourceBEDId, Long destBEDId, int length)
            throws KDXException {
      List<PathDefinition> pdList = getKdxDataAccessManager().getPathDefinitionDetailsLessThanLength(sourceBEDId,
               destBEDId, length);
      for (PathDefinition pathDefinition : pdList) {
         pathDefinition.getPathLength();
         for (Path path : pathDefinition.getPaths()) {
            path.getEntityTripleDefinition().getCardinality();
         }
      }
      return pdList;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getRelationToRelationPathDefinitionDetails(java.lang.Long,
    *      java.lang.Long)
    */
   public List<PathDefinition> getRelationToRelationPathDefinitionDetails (Long sourceBEDId, Long destBEDId)
            throws KDXException {
      return getKdxDataAccessManager().getRelationToRelationPathDefinitionDetails(sourceBEDId, destBEDId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#isAbstractConcept(java.lang.Long)
    */
   public boolean isAbstract (Long bedID) throws KDXException {
      return getKdxDataAccessManager().isAbstractConcept(bedID);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#isAttributeConcept(java.lang.Long)
    */
   public boolean isAttribute (Long bedID) throws KDXException {
      return getKdxDataAccessManager().isAttributeConcept(bedID);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#isGrainConcept(java.lang.Long)
    */
   public boolean isGrainConcept (Long bedID) throws KDXException {
      return getKdxDataAccessManager().isGrainConcept(bedID);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#isComparativeConcept(java.lang.Long)
    */
   public boolean isComparativeConcept (Long bedID) throws KDXException {
      return getKdxDataAccessManager().isComparativeConcept(bedID);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#isDistributionConcept(java.lang.Long)
    */
   public boolean isDistributionConcept (Long bedID) throws KDXException {
      return getKdxDataAccessManager().isDistributionConcept(bedID);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#isEnumerationConcept(java.lang.Long)
    */
   public boolean isEnumerationConcept (Long bedID) throws KDXException {
      return getKdxDataAccessManager().isEnumerationConcept(bedID);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#isQuantitativeConcept(java.lang.Long)
    */
   public boolean isQuantitativeConcept (Long bedID) throws KDXException {
      return getKdxDataAccessManager().isQuantitativeConcept(bedID);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getRelatedBEDIdsByInstanceConceptBEDIds(java.util.List,
    *      java.util.List)
    */
   public Set<Long> getRelatedBEDIdsByInstanceConceptBEDIds (List<Long> instanceBEDIds, List<Long> conceptBEDIds)
            throws KDXException {
      return getKdxDataAccessManager().getRelatedBEDIdsByInstanceConceptBEDIds(instanceBEDIds, conceptBEDIds);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getRelatedBEDIdsByConceptBEDIds(java.util.List)
    */
   public Set<Long> getRelatedBEDIdsByConceptBEDIds (List<Long> conceptBEDIds) throws KDXException {
      try {
         return getKdxDataAccessManager().getRelatedBEDIdsByConceptBEDIds(conceptBEDIds);
      } catch (DataAccessException daException) {
         throw new KDXException(daException.getCode(), daException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getAllParentPathDefinitionsForDestination(java.lang.Long)
    */
   public Collection<Concept> getAllParentConcepts (Long destinationBEDId) throws KDXException {
      try {
         Set<Concept> concepts = new HashSet<Concept>();
         List<PathDefinition> pathDefinitions = getKdxDataAccessManager().getAllParentPathDefinitionsForDestination(
                  destinationBEDId);
         for (PathDefinition pd : pathDefinitions) {
            Concept concept = pd.getSourceBusinessEntityDefinition().getConcept();
            if (concept != null) {
               concept.getName();
               // concept.getConceptDetails().getAttribute();
               concepts.add(concept);
            }
         }
         return concepts;
      } catch (SWIException e) {
         throw new KDXException(e.code, e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getDirectParentPathDefinitionsForDestination(java.lang.Long)
    */
   public List<Concept> getImmediateParentConcepts (Long destinationBEDId) throws KDXException {
      try {
         List<Concept> concepts = new ArrayList<Concept>();
         List<PathDefinition> pathDefinitions = getKdxDataAccessManager()
                  .getImmediateParentPathDefinitionsForDestination(destinationBEDId);
         for (PathDefinition pd : pathDefinitions) {
            Concept concept = pd.getSourceBusinessEntityDefinition().getConcept();
            if (concept != null) {
               concept.getName();
               concepts.add(concept);
            }

         }
         return concepts;
      } catch (SWIException e) {
         throw new KDXException(e.code, e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getPathsBetweenMultipleSourcesToSingleDestination(java.lang.
    *      Long)
    */
   public List<PathDefinition> getPathsBetweenMultipleSourcesToSingleDestination (List<Long> sourceBEDIds,
            Long destinationBEDId) throws KDXException {
      return getKdxDataAccessManager()
               .getPathsBetweenMultipleSourcesToSingleDestination(sourceBEDIds, destinationBEDId);
   }

   public Path getPathByETD (EntityTripleDefinition etd) throws KDXException {
      return getKdxDataAccessManager().getPathByETD(etd);
   }

   public List<PathDefinition> getParentTriplesForDestList (Long sourceBED, List<Long> destinationBEDs)
            throws KDXException {
      return getKdxDataAccessManager().getParentPathsForDestList(sourceBED, destinationBEDs);
   }

   public List<PathDefinition> getImmediateParentPathDefinitionsForDestination (Long destinationBEDId)
            throws KDXException {
      List<PathDefinition> pathDefinitions = getKdxDataAccessManager().getImmediateParentPathDefinitionsForDestination(
               destinationBEDId);
      for (PathDefinition pathDefinition : pathDefinitions) {
         pathDefinition.getSourceBusinessEntityDefinition();
         pathDefinition.getSourceBusinessEntityDefinition().getConcept();
         pathDefinition.getSourceBusinessEntityDefinition().getConcept().getName();
      }
      return pathDefinitions;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getCentralConceptPathsForDestination(java.lang.Long)
    */
   public List<PathDefinition> getCentralConceptPathsForDestination (Long destinationBEDId) throws KDXException {
      return getKdxDataAccessManager().getCentralConceptPathsForDestination(destinationBEDId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#getAllPathsForSourceAndDestination(java.util.List,
    *      java.util.List)
    */
   public List<PathDefinition> getAllNonParentPathsForSourceAndDestination (Set<Long> sourceBeIds, Set<Long> destBeIds)
            throws KDXException {
      List<PathDefinition> pdList = getKdxDataAccessManager().getAllNonParentPathsForSourceAndDestination(
               new ArrayList<Long>(sourceBeIds), new ArrayList<Long>(destBeIds));
      for (PathDefinition pathDefinition : pdList) {
         pathDefinition.getPathLength();

         for (Path path : pathDefinition.getPaths()) {
            path.getEntityTripleDefinition().getCardinality();
            // path.getEntityTripleDefinition().getSourceBusinessEntityDefinition().getEntityType();
            // if (path.getEntityTripleDefinition().getSourceBusinessEntityDefinition().getConcept() != null) {
            // path.getEntityTripleDefinition().getSourceBusinessEntityDefinition().getConcept().getName();
            // }
            // if (path.getEntityTripleDefinition().getSourceBusinessEntityDefinition().getConceptProfile() != null) {
            // path.getEntityTripleDefinition().getSourceBusinessEntityDefinition().getConceptProfile().getName();
            // }
            // path.getEntityTripleDefinition().getSourceBusinessEntityDefinition().getType().getName();
            // path.getEntityTripleDefinition().getRelation().getEntityType();
            // path.getEntityTripleDefinition().getRelation().getRelation().getName();
            // path.getEntityTripleDefinition().getRelation().getType().getName();
            // path.getEntityTripleDefinition().getDestinationBusinessEntityDefinition().getEntityType();
            // path.getEntityTripleDefinition().getDestinationBusinessEntityDefinition().getType().getName();
            // NK: CRA triple might not have concept as destination
            // if (path.getEntityTripleDefinition().getDestinationBusinessEntityDefinition().getConcept() != null) {
            // path.getEntityTripleDefinition().getDestinationBusinessEntityDefinition().getConcept().getName();
            // }
         }
         for (Rule rule : pathDefinition.getPathRules()) {
            rule.getName();
         }
      }
      return pdList;
   }

   public boolean hasBehavior (Long resourceID, BehaviorType behaviorType) throws KDXException {
      return getKdxDataAccessManager().hasBehavior(resourceID, behaviorType);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#checkEntityHasBehavior(java.lang.Long,
    *      com.execue.core.common.type.BehaviorType)
    */
   public boolean checkEntityHasBehavior (Long entityBedId, BehaviorType behaviorType) throws KDXException {
      return getKdxDataAccessManager().checkEntityHasBehavior(entityBedId, behaviorType);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#isModelEvaluationRequired(java.lang.Long)
    */
   public boolean isModelEvaluationRequired (Long modelId) throws KDXException {
      boolean ontologyAbsorptionRequired = false;
      Model model = getKdxRetrievalService().getModelById(modelId);
      if (CheckType.YES.equals(model.getEvaluate())) {
         ontologyAbsorptionRequired = true;
      }
      return ontologyAbsorptionRequired;
   }

   /**
    * @param modelId
    * @return
    * @throws KDXException
    */
   public boolean isIndexEvaluationRequired (Long modelId) throws KDXException {
      boolean indexEvaluationRequired = false;
      Model model = getKdxRetrievalService().getModelById(modelId);
      if (CheckType.YES.equals(model.getIndexEvaluationRequired())) {
         indexEvaluationRequired = true;
      }
      return indexEvaluationRequired;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.model.IKDXModelService#isModelOfSingleFileBasedAsset(java.lang.Long)
    */
   public boolean isModelOfSingleFileBasedAsset (Long modelId) throws KDXException {
      boolean fileBased = false;
      List<Application> applications = getApplicationRetrievalService().getApplicationsByModelId(modelId);
      if (ExecueCoreUtil.isCollectionEmpty(applications)) {
         return fileBased;
      }
      try {
         List<Asset> assets = getSdxRetrievalService().getAssetsByApplicationId(applications.get(0).getId());
         if (ExecueCoreUtil.isCollectionNotEmpty(assets)) {
            if (assets.size() == 1 && !PublishedFileType.RDBMS.equals(assets.get(0).getOriginType())) {
               fileBased = true;
            }
         }
      } catch (SDXException sdxException) {
         throw new KDXException(sdxException.getCode(), sdxException);
      }
      return fileBased;
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

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IKDXDataAccessManager getKdxDataAccessManager () {
      return kdxDataAccessManager;
   }

   public void setKdxDataAccessManager (IKDXDataAccessManager kdxDataAccessManager) {
      this.kdxDataAccessManager = kdxDataAccessManager;
   }

   /**
    * @return the applicationRetrievalService
    */
   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   /**
    * @param applicationRetrievalService the applicationRetrievalService to set
    */
   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

}