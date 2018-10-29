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


package com.execue.ontology.service.setup.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.EntityTriplePropertyType;
import com.execue.core.common.type.HierarchyType;
import com.execue.core.common.type.OriginType;
import com.execue.core.util.ExecueStringUtil;
import com.execue.ontology.configuration.OntologyConstants;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.swi.IBusinessEntityManagementWrapperService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPathDefinitionManagementService;
import com.hp.hpl.jena.graph.query.Domain;

/**
 * @author Nitesh
 */
public class DomainOntologyAdapter {

   private Logger                                    logger = Logger.getLogger(DomainOntologyAdapter.class);

   protected IKDXRetrievalService                    kdxRetrievalService;
   protected IBusinessEntityManagementWrapperService businessEntityManagementWrapperService;
   private IPathDefinitionManagementService          pathDefinitionManagementService;
   protected IKDXModelService                        kdxModelService;
   private HibernateTemplate                         hibernateTemplate;

   public void absorbDomainOntology (Long modelId) throws SWIException {

      Model model = kdxRetrievalService.getModelById(modelId);
      // TODO: Need to iterate and get the each source concept with their concept type(i.e measure, enumeration,
      // distribution or IDConcept??)
      String destinationConceptName = "";
      String sourceConceptName = "";
      String relationName = "";

      // CASE 1. Concept is MEASURE
      if (logger.isDebugEnabled()) {
         logger.debug("CASE 1. Concept is MEASURE(Time or Location)");
      }
      sourceConceptName = "salary";
      relationName = OntologyConstants.TIME_FRAME_PROPERTY;
      destinationConceptName = OntologyConstants.TIME_CONCEPT; // or location or both
      absorbDirectTriple(sourceConceptName, destinationConceptName, relationName, model);
      // TODO: Should we also map to other time based concept like time range, time list, absolute time, abstract time??
      // END: CASE 1

      // CASE 2. Concept is Enumeration(DIMENSION)
      if (logger.isDebugEnabled()) {
         logger.debug("CASE 2. Concept is Enumeration(DIMENSION)");
      }
      sourceConceptName = "department";
      relationName = "hasDepartment";
      destinationConceptName = "Information"; // TODO: Still not yet mapped in base owl, so what should be this??

      // create the relation DED
      // createRelationDED(getKdxService().getDefaultDomain(), relationName);
      // absorbDirectTriple(sourceConceptName, destinationConceptName, relationName);
      // END: CASE 2

      // CASE 3. Concept is Distribution(Time or Location)
      if (logger.isDebugEnabled()) {
         logger.debug("CASE 3. Concept is Distribution(Time or Location)");
      }
      sourceConceptName = "salary";
      relationName = OntologyConstants.TIME_FRAME_PROPERTY;
      destinationConceptName = OntologyConstants.YEAR_CONCEPT;
      absorbDirectTriple(sourceConceptName, destinationConceptName, relationName, model);
      // TODO: Should we also map to other time based concept like year, month, week, day, quarter??
      // END: CASE 3

      // TODO: Need to handle later
      // CASE 4. IDConcept for IDs
      if (logger.isDebugEnabled()) {
         logger.debug("CASE 4. IDConcept for IDs");
      }
      sourceConceptName = "IDConcept";
      relationName = OntologyConstants.PARENT_PROPERTY;
      destinationConceptName = ""; // provide the relevant concept
      // absorbDirectTriple(sourceConceptName, destinationConceptName, relationName);
      // END: CASE 4
   }

   private void absorbDirectTriple (String sourceConceptName, String destinationConceptName, String relationName,
            Model model) {
      PathDefinition pd = new PathDefinition();
      int cardinality = 1; // TODO: Need to check from where can we get the cardinallity
      String source = sourceConceptName;
      String range = destinationConceptName;
      String relation = relationName;
      if (logger.isDebugEnabled()) {
         logger.debug(source + " - " + relation + " - " + range);
      }
      try {
         BusinessEntityDefinition sourceBed = kdxRetrievalService.getConceptBEDByName(model.getId(), source);
         BusinessEntityDefinition destBed = kdxRetrievalService.getConceptBEDByName(model.getId(), range);
         BusinessEntityDefinition relDed = kdxRetrievalService.getRelationBEDByName(model.getId(), relation);
         if (sourceBed == null || destBed == null || relDed == null) {
            return;
         }
         EntityTripleDefinition entityTriple = new EntityTripleDefinition();
         entityTriple.setCardinality(cardinality);
         entityTriple.setRelation(relDed);
         entityTriple.setSourceBusinessEntityDefinition(sourceBed);
         entityTriple.setDestinationBusinessEntityDefinition(destBed);
         if (getKdxModelService().isAttribute(destBed.getId())) {
            entityTriple.setTripleType(EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
         } else {
            entityTriple.setTripleType(EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT);
         }
         entityTriple.setPropertyType(EntityTriplePropertyType.NORMAL);
         entityTriple.setOrigin(OriginType.USER);
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
         if (getKdxModelService().isAttribute(destBed.getId())) {
            pd.setType(EntityTripleDefinitionType.CONCEPT_RELATION_ATTRIBUTE);
         } else {
            pd.setType(EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT);
         }

         if (logger.isDebugEnabled()) {
            logger.debug(sourceBed.getId() + " - " + destBed.getId() + " - " + relDed.getId());
         }
         getPathDefinitionManagementService().createPathDefinition(pd);
      } catch (KDXException e) {
         e.printStackTrace();
      } catch (SWIException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   /**
    * This method will create and return the domain entity definition having relation with the given name
    * 
    * @param domain
    * @param instanceDetail
    * @param conceptMap
    * @return
    * @throws KDXException
    */
   public void createRelationDED (Domain domain, String relationName, Model model) throws KDXException {
      try {
         Relation relation = new Relation();
         relation.setName(relationName);
         relation.setDescription(ExecueStringUtil.getUncompactedDisplayString(relationName));
         relation.setDisplayName(ExecueStringUtil.getUncompactedDisplayString(relationName));
         getBusinessEntityManagementWrapperService().createRelation(model.getId(), relation);
      } catch (PlatformException e) {
         e.printStackTrace();
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

   /**
    * @return the hibernateTemplate
    */
   public HibernateTemplate getHibernateTemplate () {
      return hibernateTemplate;
   }

   /**
    * @param hibernateTemplate
    *           the hibernateTemplate to set
    */
   public void setHibernateTemplate (HibernateTemplate hibernateTemplate) {
      this.hibernateTemplate = hibernateTemplate;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IPathDefinitionManagementService getPathDefinitionManagementService () {
      return pathDefinitionManagementService;
   }

   public void setPathDefinitionManagementService (IPathDefinitionManagementService pathDefinitionManagementService) {
      this.pathDefinitionManagementService = pathDefinitionManagementService;
   }

   public IBusinessEntityManagementWrapperService getBusinessEntityManagementWrapperService () {
      return businessEntityManagementWrapperService;
   }

   public void setBusinessEntityManagementWrapperService (
            IBusinessEntityManagementWrapperService businessEntityManagementWrapperService) {
      this.businessEntityManagementWrapperService = businessEntityManagementWrapperService;
   }
}
