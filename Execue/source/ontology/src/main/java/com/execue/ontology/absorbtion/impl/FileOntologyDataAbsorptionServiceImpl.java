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


package com.execue.ontology.absorbtion.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.ontology.OntologyAbsorptionContext;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.EntityTriplePropertyType;
import com.execue.core.common.type.HierarchyType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.OriginType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueStringUtil;
import com.execue.ontology.absorbtion.IFileOntologyDataAbsorptionService;
import com.execue.ontology.bean.OntoClass;
import com.execue.ontology.bean.OntoProperty;
import com.execue.ontology.bean.Triple;
import com.execue.ontology.configuration.IOntologyConfiguration;
import com.execue.ontology.configuration.OntologyConstants;
import com.execue.ontology.exceptions.OntologyException;
import com.execue.ontology.exceptions.OntologyExceptionCodes;
import com.execue.ontology.service.IOntologyService;
import com.execue.ontology.service.OntologyFactory;
import com.execue.ontology.service.ontoModel.IOntologyModelServices;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.swi.IBusinessEntityManagementWrapperService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPathAbsorptionService;
import com.execue.swi.service.IPathDefinitionManagementService;
import com.execue.swi.service.IPathDefinitionRetrievalService;

public class FileOntologyDataAbsorptionServiceImpl implements IFileOntologyDataAbsorptionService {

   private final Logger                            logger = Logger
                                                                   .getLogger(FileOntologyDataAbsorptionServiceImpl.class);

   protected IOntologyConfiguration                ontologyConfigurationService;
   private IOntologyService                        ontologyService;
   private IPathDefinitionRetrievalService         pathDefinitionRetrievalService;
   private IPathDefinitionManagementService        pathDefinitionManagementService;
   private IKDXManagementService                   kdxManagementService;
   private IKDXRetrievalService                    kdxRetrievalService;
   private IBaseKDXRetrievalService                baseKDXRetrievalService;
   private List<String>                            skipConceptList;
   private List<String>                            skipPropertyList;
   private IJobDataService                         jobDataService;
   private IPathAbsorptionService                  pathAbsorptionService;
   private IKDXModelService                        kdxModelService;
   private IBusinessEntityManagementWrapperService businessEntityManagementWrapperService;

   public void absorbOntology (OntologyAbsorptionContext ontologyAbsorptionContext) throws OntologyException {
      // Step 1. Absorb Concepts
      // Step 2. Absorb Relations
      // Step 3. Absorb Direct Triples
      // Step 4. Absorb Direct Parent Triples
      JobOperationalStatus jobOperationalStatus = null;
      try {

         // NK: added null check to execute the absorbontology from the test case
         if (ontologyAbsorptionContext.getJobRequest() != null) {
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(ontologyAbsorptionContext
                     .getJobRequest(), "Ontology file absorption", JobStatus.INPROGRESS, null, new Date());
            jobDataService.createJobOperationStatus(jobOperationalStatus);
         }

         IOntologyModelServices ontModelServices = OntologyFactory.getInstance().getOntologyModelServices();
         skipConceptList = getOntologyConfigurationService().getOntologySkipConcepts();
         skipPropertyList = getOntologyConfigurationService().getOntologySkipProperties();
         // Load File Ontology in memory
         String filePath = "file:" + ontologyAbsorptionContext.getFilePath();
         ontModelServices.loadOntology(filePath);

         // Step 1. Absorb Concepts
         if (logger.isDebugEnabled()) {
            logger.debug("Step 1. Absorb Concepts");
         }
         absorbConcepts(ontModelServices, ontologyAbsorptionContext.getModelId());
         // Step 2. Absorb Relations
         if (logger.isDebugEnabled()) {
            logger.debug("Step 2. Absorb Relations");
         }

         absorbProperties(ontModelServices, ontologyAbsorptionContext.getModelId());

         // Step 3. Absorb Direct Triples
         if (logger.isDebugEnabled()) {
            logger.debug("Step 3. Absorb Direct Triples");
         }
         Set<String> existingTriples = null;

         // TODO: NK: check with AP/NA, should we also send the cloud id to get the existing triples??
         existingTriples = getPathDefinitionRetrievalService().getAllPathDefTriplesForModel(
                  ontologyAbsorptionContext.getModelId());

         if (CollectionUtils.isEmpty(existingTriples)) {
            if (logger.isDebugEnabled()) {
               logger.debug("No Paths defined for the given model");
            }
            existingTriples = new HashSet<String>();
         }
         absorbDirectTriples(ontModelServices, ontologyAbsorptionContext.getModelId(), ontologyAbsorptionContext
                  .getCloudId(), existingTriples);

         // Step 4. Absorb Direct Parent Triples
         if (logger.isDebugEnabled()) {
            logger.debug("Step 4. Absorb Direct Parent Triples");
         }
         List<PathDefinition> directParentPaths = absorbTriplesForParentRelations(ontModelServices,
                  ontologyAbsorptionContext.getModelId(), ontologyAbsorptionContext.getCloudId(), existingTriples);
         for (PathDefinition path : directParentPaths) {
            getPathDefinitionManagementService().createPathDefinition(path);
         }

         // NK: added null check to execute the absorb ontology from the test case
         if (ontologyAbsorptionContext.getJobRequest() != null) {
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         }
      } catch (ExeCueException exception) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, exception
                     .getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new OntologyException(OntologyExceptionCodes.ONTOLOGY_DATA_ABSORPTION_FAILED_EXCEPTION_CODE,
                        exception);
            }
         }
         exception.printStackTrace();
         throw new OntologyException(OntologyExceptionCodes.ONTOLOGY_DATA_ABSORPTION_FAILED_EXCEPTION_CODE, exception);
      }
   }

   private List<PathDefinition> absorbTriplesForParentRelations (IOntologyModelServices ontModelServices, Long modelId,
            Long cloudId, Set<String> existingTriples) throws KDXException {
      Map<String, OntoClass> conceptsMap = ontModelServices.getConcepts();
      List<PathDefinition> parentPathList = new ArrayList<PathDefinition>();
      try {
         for (Entry<String, OntoClass> entry : conceptsMap.entrySet()) {
            String conceptName = entry.getValue().getName();
            if (skipConceptList.contains(conceptName)) {
               continue;
            }
            List<String> parentConcepts = ontModelServices.getParentConcepts(entry.getValue(), false);
            BusinessEntityDefinition parentRelation = baseKDXRetrievalService
                     .getRelationBEDByName(OntologyConstants.PARENT_PROPERTY);
            BusinessEntityDefinition conceptBed = kdxRetrievalService.getConceptBEDByName(modelId, conceptName);
            // If conceptBed is null, Try to get it from base
            if (conceptBed == null) {
               conceptBed = baseKDXRetrievalService.getBusinessEntityDefinitionByNames(conceptName, null);
               if (conceptBed == null) {
                  continue;
               }
            }

            for (String parentConceptName : parentConcepts) {
               if (skipConceptList.contains(parentConceptName)) {
                  continue;
               }
               BusinessEntityDefinition parentBed = null;
               Concept parentConcept = kdxRetrievalService.getConceptByName(modelId, parentConceptName);
               // If parentConcept is null, Try to get the parentConcept from base
               if (parentConcept == null) {
                  parentConcept = baseKDXRetrievalService.getConceptByName(parentConceptName);
                  parentBed = baseKDXRetrievalService.getBusinessEntityDefinitionByNames(parentConceptName, null);
               } else {
                  parentBed = kdxRetrievalService
                           .getBusinessEntityDefinitionByIds(modelId, parentConcept.getId(), null);
               }

               // NK: Check for duplicate triples
               if (existingTriples
                        .contains(parentBed.getId() + "-" + parentRelation.getId() + "-" + conceptBed.getId())) {
                  if (logger.isDebugEnabled()) {
                     logger.debug("Skipping duplicate triple in absorbTriplesForParentRelations: " + parentBed.getId()
                              + "-" + parentRelation.getId() + "-" + conceptBed.getId());
                  }
                  continue;
               } else {
                  existingTriples.add(parentBed.getId() + "-" + parentRelation.getId() + "-" + conceptBed.getId());
               }

               // Create Entity triple Definition
               EntityTripleDefinition entityTriple = new EntityTripleDefinition();
               entityTriple.setCardinality(1);
               entityTriple.setRelation(parentRelation);
               entityTriple.setSourceBusinessEntityDefinition(parentBed);
               entityTriple.setDestinationBusinessEntityDefinition(conceptBed);
               entityTriple.setOrigin(OriginType.Hierarchy);
               if (getKdxModelService().isAttribute(parentBed.getId())
                        && getKdxModelService().isAttribute(conceptBed.getId())) {
                  entityTriple.setTripleType(EntityTripleDefinitionType.ATTRIBUTE_RELATION_ATTRIBUTE);
               } else if (getKdxModelService().isAttribute(conceptBed.getId())) {
                  entityTriple.setTripleType(EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
               } else {
                  entityTriple.setTripleType(EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT);
               }
               entityTriple.setPropertyType(EntityTriplePropertyType.NORMAL);
               // Create Path Definition
               PathDefinition pd = new PathDefinition();
               pd.setDestinationBusinessEntityDefinition(conceptBed);
               pd.setSourceBusinessEntityDefinition(parentBed);
               Set<Path> pathSet = new HashSet<Path>();
               pd.setPriority(1);
               pd.setPathLength(1);
               pd.setHierarchyType(HierarchyType.PARENTAGE);
               pd.setCloudId(cloudId);
               if (getKdxModelService().isAttribute(parentBed.getId())
                        && getKdxModelService().isAttribute(conceptBed.getId())) {
                  pd.setType(EntityTripleDefinitionType.ATTRIBUTE_RELATION_ATTRIBUTE);
               } else if (getKdxModelService().isAttribute(conceptBed.getId())) {
                  pd.setType(EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
               } else {
                  pd.setType(EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT);
               }
               // Create New Path
               Path path = new Path();
               path.setEntityTripleDefinition(entityTriple);
               path.setEntityTripleOrder(1);
               path.setPathDefinition(pd);
               pathSet.add(path);
               pd.setPaths(pathSet);
               parentPathList.add(pd);
            }
         }
      } catch (SWIException swiException) {
         throw new KDXException(swiException.getCode(), swiException);
      }
      return parentPathList;
   }

   private void absorbDirectTriples (IOntologyModelServices ontModelServices, Long modelId, Long cloudId,
            Set<String> existingTriples) {
      Map<String, Map<String, List<Triple>>> triples = ontModelServices.getTriples();

      for (Entry<String, Map<String, List<Triple>>> entry : triples.entrySet()) {
         Map<String, List<Triple>> valueMap = entry.getValue();
         for (Entry<String, List<Triple>> tripleEntry : valueMap.entrySet()) {
            List<Triple> triplesList = tripleEntry.getValue();
            for (Triple triple : triplesList) {
               PathDefinition pd = new PathDefinition();
               int cardinality = triple.getCardinality();
               String source = triple.getDomain();
               String range = triple.getRange();
               String relation = triple.getProperty();
               if (skipConceptList.contains(source) || skipConceptList.contains(range)
                        || skipPropertyList.contains(relation)) {
                  continue;
               }
               try {
                  BusinessEntityDefinition sourceBed = kdxRetrievalService.getConceptBEDByName(modelId, source);
                  if (sourceBed == null) {
                     sourceBed = baseKDXRetrievalService.getConceptBEDByName(source);
                  }
                  BusinessEntityDefinition destBed = kdxRetrievalService.getConceptBEDByName(modelId, range);
                  if (destBed == null) {
                     destBed = baseKDXRetrievalService.getConceptBEDByName(range);
                  }
                  BusinessEntityDefinition relBed = kdxRetrievalService.getRelationBEDByName(modelId, relation);
                  if (relBed == null) {
                     relBed = baseKDXRetrievalService.getRelationBEDByName(relation);
                  }
                  if (sourceBed == null || destBed == null || relBed == null) {
                     continue;
                  }
                  // Check for duplicate triples
                  if (existingTriples.contains(sourceBed.getId() + "-" + relBed.getId() + "-" + destBed.getId())) {
                     if (logger.isDebugEnabled()) {
                        logger.debug("Skipping Direct Triple in absorbDirectTriples: " + sourceBed.getId() + "-"
                                 + relBed.getId() + "-" + destBed.getId());
                     }
                     continue;
                  } else {
                     existingTriples.add(sourceBed.getId() + "-" + relBed.getId() + "-" + destBed.getId());
                  }
                  EntityTripleDefinition entityTriple = new EntityTripleDefinition();
                  entityTriple.setCardinality(cardinality);
                  entityTriple.setRelation(relBed);
                  entityTriple.setOrigin(OriginType.USER);
                  entityTriple.setSourceBusinessEntityDefinition(sourceBed);
                  entityTriple.setDestinationBusinessEntityDefinition(destBed);
                  if (getKdxModelService().isAttribute(destBed.getId())) {
                     entityTriple.setTripleType(EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
                  } else {
                     entityTriple.setTripleType(EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT);
                  }
                  entityTriple.setPropertyType(EntityTriplePropertyType.NORMAL);
                  Path path = new Path();
                  path.setEntityTripleDefinition(entityTriple);
                  path.setEntityTripleOrder(1);
                  path.setPathDefinition(pd);
                  Set<Path> pathSet = new HashSet<Path>();
                  pathSet.add(path);
                  pd.setDestinationBusinessEntityDefinition(destBed);
                  pd.setSourceBusinessEntityDefinition(sourceBed);
                  pd.setPaths(pathSet);
                  pd.setHierarchyType(HierarchyType.NON_HIERARCHICAL);
                  pd.setPriority(1);
                  pd.setPathLength(1);
                  pd.setCloudId(cloudId);
                  if (getKdxModelService().isAttribute(destBed.getId())) {
                     pd.setType(EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
                  } else {
                     pd.setType(EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT);
                  }

                  getPathDefinitionManagementService().createPathDefinition(pd);
               } catch (KDXException e) {
                  e.printStackTrace();
               } catch (SWIException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
            }
         }
      }
   }

   private void absorbProperties (IOntologyModelServices ontModelServices, Long modelId) throws KDXException {
      Map<String, OntoProperty> propertiesMap = ontModelServices.getProperties();
      for (Entry<String, OntoProperty> entry : propertiesMap.entrySet()) {
         OntoProperty property = entry.getValue();
         if (skipPropertyList.contains(property.getName())) {
            continue;
         }

         // Check for existing relations
         if (getBaseKDXRetrievalService().getRelationBEDByName(property.getName()) != null) {
            if (logger.isDebugEnabled()) {
               logger.debug("absorbProperties:: Skipping relation from the base: " + property.getName());
            }
            continue;
         }

         Relation relation = new Relation();
         relation.setName(property.getName());
         relation.setDescription(ExecueStringUtil.getUncompactedDisplayString(property.getName()));
         relation.setDisplayName(ExecueStringUtil.getUncompactedDisplayString(property.getName()));
         try {
            getBusinessEntityManagementWrapperService().createRelation(modelId, relation);
         } catch (PlatformException e) {
            if (e.code == SWIExceptionCodes.ENTITY_ALREADY_EXISTS) {
               logger.error("relation with name " + relation.getName() + " Exists in DB");
            } else {
               throw new KDXException(e.getCode(), e);
            }
         }
      }
   }

   private void absorbConcepts (IOntologyModelServices ontModelServices, Long modelId) {
      Map<String, OntoClass> conceptsMap = ontModelServices.getConcepts();
      for (Entry<String, OntoClass> entry : conceptsMap.entrySet()) {
         OntoClass ontoClass = entry.getValue();
         if (skipConceptList.contains(ontoClass.getName())) {
            continue;
         }

         Concept concept = new Concept();
         concept.setDisplayName(ExecueStringUtil.getUncompactedDisplayString(ontoClass.getName()));
         concept.setName(ontoClass.getName());
         // TODO: Uncomment the below line once we have the display name in ontoClass
         // concept.setDescription(ontoClass.getDisplayName());
         // TODO: Comment the below line once we have the display name in ontoClass
         concept.setDescription(concept.getDisplayName());
         List<String> parents = ontModelServices.getParentConcepts(ontoClass, true);
         try {
            // Check for base concept, if exists skip and continue with next concept
            Concept baseConcept = getBaseKDXRetrievalService().getConceptByName(ontoClass.getName());
            if (baseConcept != null) {
               if (logger.isDebugEnabled()) {
                  logger.debug("Skipping the base concept absorption: " + baseConcept.getName());
               }
               continue;
            }
            BusinessEntityDefinition conceptBed = getBusinessEntityManagementWrapperService().saveOrUpdateConcept(
                     modelId, concept);
            // create entity behaviors
            List<BehaviorType> behaviorTypes = getBehaviorTypes(parents);
            getKdxManagementService().createEntityBehaviors(conceptBed.getId(), behaviorTypes);
         } catch (KDXException e) {
            if (e.code == SWIExceptionCodes.ENTITY_ALREADY_EXISTS) {
               logger.error("Concept with name " + concept.getName() + " Exists in DB");
            }
         } catch (PlatformException e) {
            if (e.code == SWIExceptionCodes.ENTITY_ALREADY_EXISTS) {
               logger.error("Concept with name " + concept.getName() + " Exists in DB");
            }
         }
      }
   }

   private List<BehaviorType> getBehaviorTypes (List<String> parents) {
      List<BehaviorType> behaviorTypes = new ArrayList<BehaviorType>();
      if (parents.contains(OntologyConstants.ATTRIBUTE)) {
         behaviorTypes.add(BehaviorType.ATTRIBUTE);
      }
      if (parents.contains(OntologyConstants.ABSTRACT_CONCEPT)) {
         behaviorTypes.add(BehaviorType.ABSTRACT);
      }
      if (parents.contains(OntologyConstants.DISTRIBUTION_CONCEPT)) {
         behaviorTypes.add(BehaviorType.DISTRIBUTION);
      }
      if (parents.contains(OntologyConstants.COMPARATIVE_CONCEPT)) {
         behaviorTypes.add(BehaviorType.COMPARATIVE);
      }
      if (parents.contains(OntologyConstants.QUANTITATIVE_CONCEPT)) {
         behaviorTypes.add(BehaviorType.QUANTITATIVE);
      }
      if (parents.contains(OntologyConstants.INSTANCE_CONCEPT)) {
         behaviorTypes.add(BehaviorType.ENUMERATION);
      }

      if (parents.contains(OntologyConstants.POPULATION_CONCEPT)) {
         behaviorTypes.add(BehaviorType.GRAIN);
      }
      return behaviorTypes;
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

   public static CheckType getCheckTypeByBooleanVal (boolean flag) {
      Character value = 'N';
      if (flag) {
         value = 'Y';
      }
      return CheckType.getType(value);
   }

   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the baseKDXRetrievalService
    */
   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   /**
    * @param baseKDXRetrievalService
    *           the baseKDXRetrievalService to set
    */
   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   /**
    * @return the jobDataService
    */
   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   /**
    * @param jobDataService
    *           the jobDataService to set
    */
   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public IPathAbsorptionService getPathAbsorptionService () {
      return pathAbsorptionService;
   }

   public void setPathAbsorptionService (IPathAbsorptionService pathAbsorptionService) {
      this.pathAbsorptionService = pathAbsorptionService;
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

   public IBusinessEntityManagementWrapperService getBusinessEntityManagementWrapperService () {
      return businessEntityManagementWrapperService;
   }

   public void setBusinessEntityManagementWrapperService (
            IBusinessEntityManagementWrapperService businessEntityManagementWrapperService) {
      this.businessEntityManagementWrapperService = businessEntityManagementWrapperService;
   }

   public IOntologyConfiguration getOntologyConfigurationService () {
      return ontologyConfigurationService;
   }

   public void setOntologyConfigurationService (IOntologyConfiguration ontologyConfigurationService) {
      this.ontologyConfigurationService = ontologyConfigurationService;
   }

}
