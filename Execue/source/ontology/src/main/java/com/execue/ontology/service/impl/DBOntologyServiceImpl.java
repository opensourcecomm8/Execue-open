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


package com.execue.ontology.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.EntityTriplePropertyType;
import com.execue.core.common.type.HierarchyType;
import com.execue.core.common.type.OriginType;
import com.execue.core.common.type.PathSelectionType;
import com.execue.ontology.bean.OntoClass;
import com.execue.ontology.bean.OntoProperty;
import com.execue.ontology.bean.OntoResource;
import com.execue.ontology.bean.Triple;
import com.execue.ontology.configuration.OntologyConstants;
import com.execue.ontology.exceptions.OntologyException;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPathDefinitionManagementService;
import com.execue.swi.service.IPathDefinitionRetrievalService;

public class DBOntologyServiceImpl extends AbstractDBOntologyServiceImpl {

   private static final Logger              log = Logger.getLogger(DBOntologyServiceImpl.class);

   private IBaseKDXRetrievalService         baseKDXRetrievalService;
   private IKDXModelService                 kdxModelService;
   private IPathDefinitionRetrievalService  pathDefinitionRetrievalService;
   private IPathDefinitionManagementService pathDefinitionManagementService;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#isConcept(java.lang.Long)
    */
   public boolean isConcept (Long conceptID) throws OntologyException {
      try {
         return getKdxModelService().getBusinessEntityType(conceptID).equals(BusinessEntityType.CONCEPT);
      } catch (SWIException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#isInstance(java.lang.Long)
    */
   public boolean isInstance (Long instance) throws OntologyException {
      try {
         return getKdxModelService().getBusinessEntityType(instance).equals(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);
      } catch (SWIException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#isProperty(java.lang.Long)
    */
   public boolean isProperty (Long propID) throws OntologyException {
      try {
         return getKdxModelService().getBusinessEntityType(propID).equals(BusinessEntityType.RELATION);
      } catch (SWIException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#getConcept(java.lang.Long)
    */
   public OntoClass getConcept (Long resourceID) throws OntologyException {
      try {
         BusinessEntityDefinition ded = kdxRetrievalService.getBusinessEntityDefinitionById(resourceID);
         Concept concept = ded.getConcept();
         // TODO -AP- ask nihar why added
         if (concept == null) {
            return null;
         }
         OntoClass oc = new OntoClass(concept.getDisplayName(), concept.getName());
         oc.setDomainEntityId(ded.getId());
         return oc;
      } catch (KDXException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#getProperty(java.lang.Long)
    */
   public OntoProperty getProperty (Long resourceID) throws OntologyException {
      try {
         BusinessEntityDefinition ded = kdxRetrievalService.getBusinessEntityDefinitionById(resourceID);
         Relation relation = ded.getRelation();
         OntoProperty op = new OntoProperty(relation.getDisplayName(), relation.getName());
         op.setDomainEntityId(relation.getId());
         return op;
      } catch (KDXException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#hasParent(java.lang.Long, java.lang.Long)
    */
   public boolean hasParent (Long resourceID, Long parentResourceID) throws OntologyException {
      try {
         List<PathDefinition> paths = kdxModelService.getParentPathDefinitions(parentResourceID, resourceID);
         return !CollectionUtils.isEmpty(paths);
      } catch (SWIException e) {
         log.error(e.getMessage(), e);
         return false;
      }
   }

   /**
    * @param subjectID
    * @param objectID
    * @return
    * @throws OntologyException
    */
   public List<List<Triple>> getTriples (Long subjectID, Long objectID) throws OntologyException {
      // List<PathDefinition> paths = kdxModelService.getPathDefinitionDetails(subjectDEDId, objectDEDId);
      // Parse each Path to get List<Path> from which internal List<Triple> is created
      // Each Path object contains ETD, from which a single Triple can be created

      List<PathDefinition> pathsDefinitions = new ArrayList<PathDefinition>();
      try {
         pathsDefinitions = kdxModelService.getPathDefinitionDetails(subjectID, objectID);
      } catch (SWIException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
      List<List<Triple>> allTriplesList = new ArrayList<List<Triple>>();
      for (PathDefinition pathDefinition : pathsDefinitions) {
         List<Triple> tripleList = new ArrayList<Triple>();
         for (Path path : pathDefinition.getPaths()) {
            EntityTripleDefinition tripleDef = path.getEntityTripleDefinition();
            Triple triple = new Triple();
            triple.setDomain(tripleDef.getSourceBusinessEntityDefinition().getId().toString());
            triple.setProperty(tripleDef.getRelation().getId().toString());
            triple.setCardinality(tripleDef.getCardinality());
            triple.setRange(tripleDef.getDestinationBusinessEntityDefinition().getId().toString());
            tripleList.add(triple);

         }
         allTriplesList.add(tripleList);
      }
      // Parse each Path to get List<Path> from which internal List<Triple> is created
      // Each Path object contains ETD, from which a single Triple can be created
      return allTriplesList;
   }

   /**
    * @param propID1
    * @param propID2
    * @return
    * @throws OntologyException
    */
   public boolean areInverseProperties (Long propID1, Long propID2) throws OntologyException {
      // return kdxModelService.getTriple(propName1, "inverse", propName2) != null;
      try {
         BusinessEntityDefinition relation = baseKDXRetrievalService
                  .getRelationBEDByName(OntologyConstants.INVERESE_PROPERTY);
         EntityTripleDefinition triple = kdxModelService.getEntityTriple(propID1, relation.getId(), propID2);
         return triple != null;
      } catch (SWIException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#getPropertiesWithRange(java.lang.Long)
    */
   public List<OntoProperty> getPropertiesWithRange (Long classID) throws OntologyException {
      List<OntoProperty> propList = new ArrayList<OntoProperty>();
      List<EntityTripleDefinition> triples = new ArrayList<EntityTripleDefinition>();
      try {
         triples = kdxModelService.getEntityTriplesForDestination(classID);
      } catch (SWIException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
      for (EntityTripleDefinition triple : triples) {
         Relation relation = triple.getRelation().getRelation();
         String relName = relation.getName();
         String relDisplayName = relation.getDisplayName();
         propList.add(new OntoProperty(relDisplayName, relName));
      }
      return propList;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#getPropertiesWithDomain(java.lang.Long)
    */
   public List<OntoProperty> getPropertiesWithDomain (Long classID) throws OntologyException {
      List<OntoProperty> propList = new ArrayList<OntoProperty>();
      List<EntityTripleDefinition> triples = new ArrayList<EntityTripleDefinition>();
      try {
         triples = kdxModelService.getEntityTriplesForSource(classID);
      } catch (SWIException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
      for (EntityTripleDefinition triple : triples) {
         Relation relation = triple.getRelation().getRelation();
         String relName = relation.getName();
         String relDisplayName = relation.getDisplayName();
         propList.add(new OntoProperty(relDisplayName, relName));
      }
      return propList;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#getRequiredPropertiesWithDomain(java.lang.Long)
    */
   public List<OntoProperty> getRequiredPropertiesWithDomain (Long classID) throws OntologyException {
      // List<EntityTripleDefinition> triples = kdxModelService.getEntityTriplesForSource(classDEDId)
      // Return only those which have required flag true
      List<EntityTripleDefinition> triples = new ArrayList<EntityTripleDefinition>();
      try {
         triples = kdxModelService.getEntityTriplesForSource(classID);
      } catch (SWIException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
      List<OntoProperty> propList = new ArrayList<OntoProperty>();
      for (EntityTripleDefinition triple : triples) {
         if (triple.getPropertyType().equals(EntityTriplePropertyType.REQUIRED)) {
            Relation relation = triple.getRelation().getRelation();
            String relName = relation.getName();
            String relDisplayName = relation.getDisplayName();
            propList.add(new OntoProperty(relDisplayName, relName));

         }
      }
      // Return only those which have required flag true
      return propList;

   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#getGrainConcepts()
    */
   public List<BusinessEntityDefinition> getCentralConceptsDEDs (Long modelId) throws OntologyException {
      try {
         Long centralConceptDedID = getBaseKDXRetrievalService().getConceptBEDByName(
                  OntologyConstants.POPULATION_CONCEPT).getId();
         return kdxModelService.getChildPathDestinationBEDsForSourceForModel(modelId, centralConceptDedID);
      } catch (SWIException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#hasParent(java.lang.Long, java.lang.String)
    */
   public boolean hasParent (Long resourceID, String parentName) throws OntologyException {
      // TODO -AP- add implementation like below for FLAGs
      try {
         if (OntologyConstants.GRAIN_CONCEPT.equals(parentName)) {
            return getKdxModelService().isGrainConcept(resourceID);
         } else if (OntologyConstants.QUANTITATIVE_CONCEPT.equals(parentName)) {
            return getKdxModelService().isQuantitativeConcept(resourceID);
         } else if (OntologyConstants.INSTANCE_CONCEPT.equals(parentName)) {
            return getKdxModelService().isEnumerationConcept(resourceID);
         } else if (OntologyConstants.COMPARATIVE_CONCEPT.equals(parentName)) {
            return getKdxModelService().isComparativeConcept(resourceID);
         } else if (OntologyConstants.ABSTRACT_CONCEPT.equals(parentName)) {
            return getKdxModelService().isAbstract(resourceID);
         } else if (OntologyConstants.DISTRIBUTION_CONCEPT.equals(parentName)) {
            return getKdxModelService().isDistributionConcept(resourceID);
         } else if (OntologyConstants.ATTRIBUTE.equals(parentName)) {
            return getKdxModelService().isAttribute(resourceID);
         }
      } catch (SWIException e) {
         log.error(e.getMessage(), e);
         return false;
      }
      return false;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#getAllParentPathDefinitionsForDestination(java.lang.Long)
    */
   public List<OntoClass> getAllParentConcepts (Long conceptID) throws OntologyException {
      try {
         List<OntoClass> parentClasses = new ArrayList<OntoClass>();
         Collection<Concept> parentConcepts = getKdxModelService().getAllParentConcepts(conceptID);
         for (Concept concept : parentConcepts) {
            OntoClass ontoClass = new OntoClass(concept.getDisplayName(), concept.getName());
            parentClasses.add(ontoClass);
         }
         return parentClasses;
      } catch (KDXException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#getImmediateParentConcepts(java.lang.Long)
    */
   public List<OntoClass> getImmediateParentConcepts (Long conceptID) throws OntologyException {
      try {
         List<OntoClass> parentClasses = new ArrayList<OntoClass>();
         List<Concept> parentConcepts = getKdxModelService().getImmediateParentConcepts(conceptID);
         for (Concept concept : parentConcepts) {
            OntoClass ontoClass = new OntoClass(concept.getDisplayName(), concept.getName());
            parentClasses.add(ontoClass);
         }
         return parentClasses;
      } catch (KDXException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#reachAndGetCentralConcept(java.lang.Long)
    */
   public List<OntoResource> reachAndGetCentralConcept (Long destinationDEDID) throws OntologyException {
      List<OntoResource> centralConcepts = new ArrayList<OntoResource>();
      try {
         List<PathDefinition> pathsToCentralConcept = getKdxModelService().getCentralConceptPathsForDestination(
                  destinationDEDID);
         PathDefinition selectedPDefinition = null;
         int minPathLength = 0;
         for (PathDefinition path : pathsToCentralConcept) {
            if (path.getPathLength() < minPathLength || minPathLength == 0) {
               minPathLength = path.getPathLength();
            }
         }
         for (PathDefinition path : pathsToCentralConcept) {
            if (minPathLength == path.getPathLength()) {
               selectedPDefinition = path;
               Long dedId = selectedPDefinition.getSourceBusinessEntityDefinition().getId();
               Concept c = kdxRetrievalService.getBusinessEntityDefinitionById(dedId).getConcept();
               OntoClass or = new OntoClass(c.getDisplayName(), c.getName());
               or.setDomainEntityId(dedId);
               centralConcepts.add(or);
            }
         }
         // TODO -AP-NH- now get the minimum length path and then return the Source Concept for it
      } catch (SWIException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
      return centralConcepts;
   }

   public List<BusinessEntityDefinition> reachAndGetCentralConceptBEDs (Long destinationDEDID) throws OntologyException {
      List<BusinessEntityDefinition> centralConcepts = new ArrayList<BusinessEntityDefinition>();
      try {
         List<PathDefinition> pathsToCentralConcept = getKdxModelService().getCentralConceptPathsForDestination(
                  destinationDEDID);
         PathDefinition selectedPDefinition = null;
         int minPathLength = 0;
         for (PathDefinition path : pathsToCentralConcept) {
            if (path.getPathLength() < minPathLength || minPathLength == 0) {
               minPathLength = path.getPathLength();
            }
         }
         for (PathDefinition path : pathsToCentralConcept) {
            if (minPathLength == path.getPathLength()) {
               selectedPDefinition = path;
               Long dedId = selectedPDefinition.getSourceBusinessEntityDefinition().getId();
               BusinessEntityDefinition bed = kdxRetrievalService.getBusinessEntityDefinitionById(dedId);
               centralConcepts.add(bed);
            }
         }
         // TODO -AP-NH- now get the minimum length path and then return the Source Concept for it
      } catch (SWIException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
      return centralConcepts;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#reachAndGetCentralConcept(java.lang.Long)
    */
   public List<OntoProperty> getPreferredProperties (Long conceptID) throws OntologyException {
      List<OntoProperty> preferredProp = new ArrayList<OntoProperty>(1);
      List<OntoProperty> props = getPropertiesWithDomain(conceptID);
      for (OntoProperty prop : props) {
         if (hasParent(prop.getName(), OntologyConstants.PREFERRED_PROPERTY)) {
            preferredProp.add(prop);
         }
      }
      return preferredProp;
   }

   public List<List<EntityTripleDefinition>> getCCPaths (Long sourceID, Long destID) throws OntologyException {
      List<List<EntityTripleDefinition>> etdList = new ArrayList<List<EntityTripleDefinition>>();
      try {
         PathDefinition pathDef = getKdxModelService().getConceptToConceptShortestPathDefinitionDetails(sourceID,
                  destID);
         List<EntityTripleDefinition> etdForPath = new ArrayList<EntityTripleDefinition>();
         if (pathDef != null) {
            for (Path path : pathDef.getPaths()) {
               etdForPath.add(path.getEntityTripleDefinition());
            }
            etdList.add(etdForPath);
         }
      } catch (SWIException e) {
         throw new OntologyException(e.code, e.getMessage(), e);
      }
      return etdList;
   }

   public List<EntityTripleDefinition> getCAPaths (Long sourceConceptDEDID, Long destinationConceptDEDID)
            throws OntologyException {
      List<EntityTripleDefinition> etdList = new ArrayList<EntityTripleDefinition>();
      try {
         List<PathDefinition> pathDefinitions = getKdxModelService().getConceptToAttributePathDefinitionDetails(
                  sourceConceptDEDID, destinationConceptDEDID);
         for (PathDefinition pathDef : pathDefinitions) {
            for (Path path : pathDef.getPaths()) {
               etdList.add(path.getEntityTripleDefinition());
            }
         }
      } catch (SWIException e) {
         throw new OntologyException(e.code, e.getMessage(), e);
      }
      return etdList;
   }

   public List<List<EntityTripleDefinition>> getPaths (Long sourceConceptID, Long destinationConceptID)
            throws OntologyException {
      // TODO Auto-generated method stub
      return null;
   }

   public List<PathDefinition> absorbParentPropertiesInAllChildConcepts (List<PathDefinition> parentPaths, Long modelId)
            throws OntologyException {
      return absorbParentPropertiesInAllChildConcepts(parentPaths, false, modelId);
   }

   public List<PathDefinition> absorbParentPropertiesInAllChildConcepts (List<PathDefinition> parentPaths,
            boolean excludeARA, Long modelId) throws OntologyException {
      List<PathDefinition> newPathDefinitions = new ArrayList<PathDefinition>();
      try {
         // 1. get all parent concepts, i.e. get All parent paths, then their source id.
         // 2. For each parent concept, get all non parent direct paths
         // 3. Get all children of that Parent,
         // using same parent path but this time searching by source, and getting destinations.
         // 4. for each such path, create a new path with each children as source and current destination as
         // destination.
         int count = 0;
         Set<String> existingTriples = getPathDefinitionRetrievalService().getAllPathDefTriplesForModel(modelId);
         if (CollectionUtils.isEmpty(existingTriples)) {
            existingTriples = new HashSet<String>();
         }
         for (PathDefinition parentPathDefinition : parentPaths) {
            count++;
            long sourceDedId = parentPathDefinition.getSourceBusinessEntityDefinition().getId();
            List<PathDefinition> tempPathDefinitions = new ArrayList<PathDefinition>();
            List<PathDefinition> directPaths = getPathDefinitionRetrievalService().getAllDirectPathsBySourceId(
                     sourceDedId, modelId);
            for (PathDefinition directPath : directPaths) {
               if (excludeARA && EntityTripleDefinitionType.ATTRIBUTE_RELATION_ATTRIBUTE == directPath.getType()) {
                  continue;
               }
               BusinessEntityDefinition destination = directPath.getDestinationBusinessEntityDefinition();
               BusinessEntityDefinition childConcept = parentPathDefinition.getDestinationBusinessEntityDefinition();
               for (Path path : directPath.getPaths()) {
                  BusinessEntityDefinition relationDed = path.getEntityTripleDefinition().getRelation();
                  if (existingTriples.contains(childConcept.getId() + "-" + relationDed.getId() + "-"
                           + destination.getId())) {
                     if (log.isDebugEnabled()) {
                        log.debug("Skipping duplicate triple in absorbParentPropertiesInAllChildConcepts: "
                                 + childConcept.getId() + "-" + relationDed.getId() + "-" + destination.getId());
                     }
                     continue;
                  }
                  if (!relationDed.getRelation().getName().equals(OntologyConstants.PARENT_PROPERTY)) {
                     // Create Entity triple for this
                     EntityTripleDefinition etd = new EntityTripleDefinition();
                     etd.setCardinality(path.getEntityTripleDefinition().getCardinality());
                     etd.setDestinationBusinessEntityDefinition(destination);
                     if (getKdxModelService().isAttribute(destination.getId())) {
                        etd.setTripleType(EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
                     } else {
                        etd.setTripleType(EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT);
                     }
                     etd.setSourceBusinessEntityDefinition(childConcept);
                     etd.setRelation(relationDed);
                     etd.setOrigin(OriginType.Hierarchy);
                     etd.setBaseETDId(path.getEntityTripleDefinition().getId());
                     etd.setFunctional(path.getEntityTripleDefinition().isFunctional());
                     etd.setInverseFunctional(path.getEntityTripleDefinition().isInverseFunctional());
                     etd.setRelationSpecified(path.getEntityTripleDefinition().isRelationSpecified());
                     etd.setPropertyType(EntityTriplePropertyType.NORMAL);
                     // Create path Definition
                     PathDefinition pd = new PathDefinition();
                     pd.setSourceBusinessEntityDefinition(childConcept);
                     pd.setDestinationBusinessEntityDefinition(destination);
                     if (getKdxModelService().isAttribute(destination.getId())) {
                        pd.setType(EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
                     } else {
                        pd.setType(EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT);
                     }
                     pd.setHierarchyType(HierarchyType.NON_HIERARCHICAL);
                     pd.setPriority(1);
                     pd.setPathLength(directPath.getPathLength());
                     pd.setCentralConceptType(CheckType.NO);
                     Set<Path> newPaths = new HashSet<Path>();
                     // Create Path
                     Path newPath = new Path();
                     newPath.setEntityTripleDefinition(etd);
                     newPath.setEntityTripleOrder(path.getEntityTripleOrder());
                     newPath.setPathDefinition(pd);
                     newPaths.add(newPath);
                     pd.setPaths(newPaths);
                     existingTriples.add(childConcept.getId() + "-" + relationDed.getId() + "-" + destination.getId());

                     tempPathDefinitions.add(pd);
                  }
               }
            }
            newPathDefinitions.addAll(tempPathDefinitions);
         }
      } catch (SWIException e) {
         throw new OntologyException(e.code, e.getMessage(), e);
      }
      return newPathDefinitions;
   }

   public List<PathDefinition> assignChildrenAsRangeInPlaceOfParent (List<PathDefinition> parentPaths, Long modelId)
            throws OntologyException {
      List<PathDefinition> newPathDefinitions = new ArrayList<PathDefinition>();
      try {
         Map<String, PathDefinition> pathsMap = new HashMap<String, PathDefinition>();
         Set<String> existingTriples = getPathDefinitionRetrievalService().getAllPathDefTriplesForModel(modelId);
         if (CollectionUtils.isEmpty(existingTriples)) {
            existingTriples = new HashSet<String>();
         }
         for (PathDefinition parentPathDefinition : parentPaths) {
            long destDedId = parentPathDefinition.getDestinationBusinessEntityDefinition().getId();
            long parentDedId = parentPathDefinition.getSourceBusinessEntityDefinition().getId();
            if (pathsMap.containsKey(parentDedId + "-" + destDedId)) {
               continue;
            }
            pathsMap.put(parentDedId + "-" + destDedId, parentPathDefinition);
            List<PathDefinition> tempDefinitions = new ArrayList<PathDefinition>();
            List<PathDefinition> directPaths = getPathDefinitionRetrievalService().getAllDirectPathsByDestinationId(
                     parentDedId, modelId);
            for (PathDefinition directPath : directPaths) {
               if (CheckType.YES.equals(directPath.getHierarchyType())) {
                  continue;
               }
               if (EntityTripleDefinitionType.ATTRIBUTE_RELATION_ATTRIBUTE.equals(directPath.getType())) {
                  continue;
               }
               BusinessEntityDefinition source = directPath.getSourceBusinessEntityDefinition();
               BusinessEntityDefinition childConcept = parentPathDefinition.getDestinationBusinessEntityDefinition();
               for (Path path : directPath.getPaths()) {
                  BusinessEntityDefinition relationDed = path.getEntityTripleDefinition().getRelation();
                  if (existingTriples.contains(source.getId() + "-" + relationDed.getId() + "-" + childConcept.getId())) {
                     if (log.isDebugEnabled()) {
                        log.debug("Skipping duplicate triple in assignChildrenAsRangeInPlaceOfParent: "
                                 + source.getId() + "-" + relationDed.getId() + "-" + childConcept.getId());
                     }
                     continue;
                  }
                  if (!relationDed.getRelation().getName().equals(OntologyConstants.PARENT_PROPERTY)) {
                     // Create Entity triple for this
                     EntityTripleDefinition etd = new EntityTripleDefinition();
                     etd.setCardinality(path.getEntityTripleDefinition().getCardinality());
                     etd.setDestinationBusinessEntityDefinition(childConcept);
                     etd.setRelation(relationDed);
                     etd.setOrigin(OriginType.Hierarchy);
                     etd.setSourceBusinessEntityDefinition(source);
                     if (getKdxModelService().isAttribute(childConcept.getId())) {
                        etd.setTripleType(EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
                     } else {
                        etd.setTripleType(EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT);
                     }
                     etd.setFunctional(path.getEntityTripleDefinition().isFunctional());
                     etd.setBaseETDId(path.getEntityTripleDefinition().getId());
                     etd.setInverseFunctional(path.getEntityTripleDefinition().isInverseFunctional());
                     etd.setRelationSpecified(path.getEntityTripleDefinition().isRelationSpecified());
                     etd.setPropertyType(EntityTriplePropertyType.NORMAL);
                     // Create path Definition
                     PathDefinition pd = new PathDefinition();
                     pd.setSourceBusinessEntityDefinition(source);
                     pd.setDestinationBusinessEntityDefinition(childConcept);
                     etd.setSourceBusinessEntityDefinition(source);
                     if (getKdxModelService().isAttribute(childConcept.getId())) {
                        pd.setType(EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
                     } else {
                        pd.setType(EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT);
                     }
                     pd.setHierarchyType(HierarchyType.NON_HIERARCHICAL);

                     pd.setPriority(1);
                     pd.setPathLength(directPath.getPathLength());
                     pd.setCentralConceptType(CheckType.NO);
                     Set<Path> newPaths = new HashSet<Path>();

                     // Create Path
                     Path newPath = new Path();
                     newPath.setEntityTripleDefinition(etd);
                     newPath.setEntityTripleOrder(path.getEntityTripleOrder());
                     newPath.setPathDefinition(pd);
                     newPaths.add(newPath);
                     pd.setPaths(newPaths);
                     existingTriples.add(source.getId() + "-" + relationDed.getId() + "-" + childConcept.getId());

                     tempDefinitions.add(pd);
                  }
               }
            }
            newPathDefinitions.addAll(tempDefinitions);
         }
      } catch (SWIException e) {
         throw new OntologyException(e.code, e.getMessage(), e);
      }
      return newPathDefinitions;
   }

   @Override
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   @Override
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#getDomainLookupOntoInstanceTermsForModel(java.lang.String,
    *      com.execue.core.common.bean.entity.Model)
    */
   public List<RIOntoTerm> getDomainLookupOntoInstanceTermsForModel (String keyWord, Model model)
            throws OntologyException {
      List<String> words = new ArrayList<String>();
      words.add(keyWord);
      try {
         return getKdxRetrievalService().getTermsByLookupWordsAndEntityTypeInModel(words,
                  BusinessEntityType.CONCEPT_LOOKUP_INSTANCE, model);
      } catch (KDXException e) {
         throw new OntologyException(e.code, e.getMessage(), e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#markCentralConceptPaths(java.lang.Long)
    */
   public void markCentralConceptPaths (Long modelId) throws OntologyException {
      try {
         BusinessEntityDefinition populationBED = getBaseKDXRetrievalService().getConceptBEDByName(
                  OntologyConstants.POPULATION_CONCEPT);
         List<PathDefinition> centralPaths = getPathDefinitionRetrievalService().getAllCentralConceptPathsToMark(
                  modelId, populationBED.getId());
         for (PathDefinition path : centralPaths) {
            path.setCentralConceptType(CheckType.YES);
         }
         getPathDefinitionManagementService().updatePathDefinitions(centralPaths);
      } catch (KDXException kdxException) {
         throw new OntologyException(kdxException.code, kdxException);
      } catch (SWIException e) {
         throw new OntologyException(e.code, e);
      }
   }

   public Map<String, List<PathDefinition>> getAllNonParentPathsForSourceAndDestination (Set<Long> sourceBEIds,
            Set<Long> destBeIds) throws OntologyException {
      try {
         List<PathDefinition> pathDefs = getKdxModelService().getAllNonParentPathsForSourceAndDestination(sourceBEIds,
                  destBeIds);
         Map<String, List<PathDefinition>> pathDefMapBetweenSourceAndDest = new HashMap<String, List<PathDefinition>>(1);
         for (PathDefinition pathDef : pathDefs) {
            Long sourceBEID = pathDef.getSourceBusinessEntityDefinition().getId();
            Long destBEID = pathDef.getDestinationBusinessEntityDefinition().getId();
            // TODO no need to keep path with same source and destination.
            if (sourceBEID.equals(destBEID)) {
               continue;
            }
            String key = sourceBEID + "-" + destBEID;
            List<PathDefinition> pathdefinitions = pathDefMapBetweenSourceAndDest.get(key);
            if (CollectionUtils.isEmpty(pathdefinitions)) {
               pathdefinitions = new ArrayList<PathDefinition>();
            }
            pathdefinitions.add(pathDef);
            pathDefMapBetweenSourceAndDest.put(key, pathdefinitions);
         }
         return pathDefMapBetweenSourceAndDest;
      } catch (KDXException kdxException) {
         throw new OntologyException(kdxException.code, kdxException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#getCAPathsFromPathDefMap(java.lang.Long, java.lang.Long,
    *      java.util.Map)
    */
   public List<EntityTripleDefinition> getCAPathsFromPathDefMap (Long sourceBEId, Long destBEId,
            Map<String, List<PathDefinition>> sourceDestPathDefMap) {
      String key = sourceBEId + "-" + destBEId;
      List<EntityTripleDefinition> etdList = new ArrayList<EntityTripleDefinition>();
      List<PathDefinition> pathDefinitions = sourceDestPathDefMap.get(key);
      if (CollectionUtils.isEmpty(pathDefinitions)) {
         return etdList;
      }
      for (PathDefinition pathDef : pathDefinitions) {
         for (Path path : pathDef.getPaths()) {
            etdList.add(path.getEntityTripleDefinition());
         }
      }
      return etdList;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.ontology.service.IOntologyService#getCCPathsFromPathDefMap(java.lang.Long, java.lang.Long,
    *      java.util.Map)
    */
   public List<List<EntityTripleDefinition>> getCCPathsFromPathDefMap (Long sourceBEId, Long destBEId,
            Map<String, List<PathDefinition>> sourceDestPathDefMap) {
      List<List<EntityTripleDefinition>> etdList = new ArrayList<List<EntityTripleDefinition>>();
      List<PathDefinition> pathDefs = sourceDestPathDefMap.get(sourceBEId + "-" + destBEId);
      if (CollectionUtils.isEmpty(pathDefs)) {
         return etdList;
      }
      // TODO -NA- at present only the Shortest length pathDefinition will be returned betWeen Concept to Concept.
      PathDefinition pathDef = null;
      for (PathDefinition path : pathDefs) {
         if (pathDef == null || path.getPathLength() < pathDef.getPathLength()) {
            pathDef = path;
         }
      }
      List<EntityTripleDefinition> etdForPath = new ArrayList<EntityTripleDefinition>();
      for (Path path : pathDef.getPaths()) {
         etdForPath.add(path.getEntityTripleDefinition());
      }
      etdList.add(etdForPath);
      return etdList;
   }

   public List<BusinessEntityDefinition> getDomainRegexInstances () throws OntologyException {
      try {
         return kdxRetrievalService.getRegularExpressionBasedInstances();
      } catch (KDXException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);
      }
   }

   public Map<Long, PathDefinition> getNonParentPathsByDestIdAndPathSelectionType (Set<Long> destBeIds,
            PathSelectionType pathSelectionType, Long modelId) throws OntologyException {
      try {
         List<PathDefinition> pathDefinitions = getPathDefinitionRetrievalService()
                  .getNonParentPathsByDestIdAndPathSelectionType(destBeIds, modelId, pathSelectionType);
         Map<Long, PathDefinition> pathsMap = new HashMap<Long, PathDefinition>(1);
         for (PathDefinition path : pathDefinitions) {
            if (!pathsMap.containsKey(path.getDestinationBusinessEntityDefinition().getId())) {
               pathsMap.put(path.getDestinationBusinessEntityDefinition().getId(), path);
            }
         }
         return pathsMap;
      } catch (SWIException e) {
         throw new OntologyException(e.getCode(), e.getMessage(), e);

      }
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

   public IPathDefinitionRetrievalService getPathDefinitionRetrievalService () {
      return pathDefinitionRetrievalService;
   }

   public void setPathDefinitionRetrievalService (IPathDefinitionRetrievalService pathDefinitionRetrievalService) {
      this.pathDefinitionRetrievalService = pathDefinitionRetrievalService;
   }

   public IPathDefinitionManagementService getPathDefinitionManagementService () {
      return pathDefinitionManagementService;
   }

   public void setPathDefinitionManagementService (IPathDefinitionManagementService pathDefinitionManagementService) {
      this.pathDefinitionManagementService = pathDefinitionManagementService;
   }
}
