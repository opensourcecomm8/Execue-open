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


package com.execue.handler.swi.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.type.AssociationType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.EntityTriplePropertyType;
import com.execue.core.common.type.HierarchyType;
import com.execue.core.common.type.OriginType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.bean.UIPathDefinition;
import com.execue.handler.bean.UIRelation;
import com.execue.handler.swi.IAttributeAssociationServiceHandler;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPathDefinitionDeletionService;
import com.execue.swi.service.IPathDefinitionManagementService;
import com.execue.swi.service.IPathDefinitionRetrievalService;

public class AttributeAssociationServiceHandlerImpl implements IAttributeAssociationServiceHandler {

   private IKDXRetrievalService             kdxRetrievalService;

   private IKDXCloudRetrievalService        kdxCloudRetrievalService;
   private IPathDefinitionRetrievalService  pathDefinitionRetrievalService;
   private IPathDefinitionManagementService pathDefinitionManagementService;
   private IPathDefinitionDeletionService   pathDefinitionDeletionService;

   public void associateAttributePathDefinitions (Long modelId, List<UIPathDefinition> savedPathdefinitions,
            List<UIPathDefinition> selectedPathdefinitions) throws HandlerException {
      try {
         getPathDefinitionDeletionService().deletePathDefinitions(getPathDefinitionIdsList(savedPathdefinitions));
         getPathDefinitionDeletionService().deleteEntityTripleDefinitions(getETDIdsList(savedPathdefinitions));
         List<PathDefinition> allPathDefinitions = new ArrayList<PathDefinition>();
         allPathDefinitions.addAll(generatePathDefinitions(modelId, selectedPathdefinitions, false));
         allPathDefinitions.addAll(generatePathDefinitions(modelId, selectedPathdefinitions, true));
         for (PathDefinition pathDefinition : allPathDefinitions) {
            getPathDefinitionManagementService().createPathDefinition(pathDefinition);
         }
      } catch (KDXException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      } catch (SWIException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }

   }

   public List<UIConcept> getConceptsByType (Long modelId, String typeName) throws HandlerException {
      try {
         List<BusinessEntityDefinition> concepts = getKdxRetrievalService().getConceptsOfParticularType(modelId,
                  typeName);
         return transformIntoUIConcepts(concepts);
      } catch (KDXException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<UIPathDefinition> getExistingPathDefinitions (Long srcBedId, Long relationBedId) throws HandlerException {
      try {
         List<PathDefinition> pathDefinitions = getPathDefinitionRetrievalService()
                  .getPathDefinitionsBySrcBedAndRelation(srcBedId, relationBedId);
         return transformIntoUIPathdefinitions(pathDefinitions, srcBedId);
      } catch (SWIException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
   }

   public UIRelation getRelationByName (Long modelId, String relationName) throws HandlerException {
      try {
         BusinessEntityDefinition relationBed = getKdxRetrievalService().getRelationBEDByNameIncludingBaseGroup(
                  modelId, relationName);
         return tranformRelation(relationBed);
      } catch (KDXException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }
   }

   private UIRelation tranformRelation (BusinessEntityDefinition relationBed) {
      UIRelation uiRelation = new UIRelation();
      uiRelation.setBedId(relationBed.getId());
      Relation relation = relationBed.getRelation();
      uiRelation.setId(relation.getId());
      uiRelation.setName(relation.getName());
      uiRelation.setDisplayName(relation.getDisplayName());
      return uiRelation;
   }

   private List<UIPathDefinition> transformIntoUIPathdefinitions (List<PathDefinition> pathDefinations, Long srcBedId) {
      List<UIPathDefinition> uiPathDefinitions = new ArrayList<UIPathDefinition>();
      if (ExecueCoreUtil.isCollectionNotEmpty(pathDefinations)) {
         for (PathDefinition pathDefinition : pathDefinations) {
            List<Path> paths = new ArrayList<Path>(pathDefinition.getPaths());
            if (ExecueCoreUtil.isCollectionNotEmpty(paths)) {
               EntityTripleDefinition etd = paths.get(0).getEntityTripleDefinition();
               UIPathDefinition uiPathDefinition = new UIPathDefinition();
               uiPathDefinition.setPathDefinitionId(pathDefinition.getId());
               uiPathDefinition.setEntityTripleDefinitionId(etd.getId());
               uiPathDefinition.setSourceConceptBedId(etd.getSourceBusinessEntityDefinition().getId());
               uiPathDefinition.setSourceConceptName(etd.getSourceBusinessEntityDefinition().getConcept().getName());
               uiPathDefinition.setDestinationConceptBedId(etd.getDestinationBusinessEntityDefinition().getId());
               uiPathDefinition.setDestinationConceptName(etd.getDestinationBusinessEntityDefinition().getConcept()
                        .getName());
               uiPathDefinition.setRelationName(etd.getRelation().getRelation().getName());
               uiPathDefinition.setRelationBedId(etd.getRelation().getId());
               if (srcBedId.equals(etd.getDestinationBusinessEntityDefinition().getId())) {
                  uiPathDefinition.setReverseAssociation(true);
               }
               uiPathDefinitions.add(uiPathDefinition);
            }
         }
      }
      return uiPathDefinitions;
   }

   private List<UIConcept> transformIntoUIConcepts (List<BusinessEntityDefinition> concepts) {
      List<UIConcept> uiConcepts = new ArrayList<UIConcept>();
      if (ExecueCoreUtil.isCollectionNotEmpty(concepts)) {
         for (BusinessEntityDefinition bed : concepts) {
            UIConcept uiConcept = new UIConcept();
            uiConcept.setName(bed.getConcept().getName());
            uiConcept.setDisplayName(bed.getConcept().getDisplayName());
            uiConcept.setBedId(bed.getId());
            uiConcept.setId(bed.getConcept().getId());
            uiConcepts.add(uiConcept);
         }
      }
      return uiConcepts;
   }

   private List<PathDefinition> generatePathDefinitions (Long modelId, List<UIPathDefinition> pathDefinitions,
            boolean reverseAssociation) throws KDXException {
      Cloud cloud = getKdxCloudRetrievalService().getDefaultAppCloud(modelId);
      List<PathDefinition> pathDefinitionsList = new ArrayList<PathDefinition>();
      if (ExecueCoreUtil.isCollectionNotEmpty(pathDefinitions)) {
         for (UIPathDefinition pathDefinition : pathDefinitions) {
            BusinessEntityDefinition sourceBED = null;
            BusinessEntityDefinition destinationBED = null;
            if (!reverseAssociation) {
               sourceBED = getKdxRetrievalService().getBusinessEntityDefinitionById(
                        pathDefinition.getSourceConceptBedId());
               destinationBED = getKdxRetrievalService().getBusinessEntityDefinitionById(
                        pathDefinition.getDestinationConceptBedId());
            } else {
               sourceBED = getKdxRetrievalService().getBusinessEntityDefinitionById(
                        pathDefinition.getDestinationConceptBedId());
               destinationBED = getKdxRetrievalService().getBusinessEntityDefinitionById(
                        pathDefinition.getSourceConceptBedId());
            }
            BusinessEntityDefinition relationBED = getKdxRetrievalService().getBusinessEntityDefinitionById(
                     pathDefinition.getRelationBedId());

            EntityTripleDefinition currentETD = ExecueBeanManagementUtil.prepareEntityTripleDefinition(sourceBED,
                     relationBED, destinationBED, EntityTripleDefinitionType.ATTRIBUTE_RELATION_ATTRIBUTE);
            currentETD.setCardinality(1);
            currentETD.setPropertyType(EntityTriplePropertyType.NORMAL);
            currentETD.setOrigin(OriginType.USER);
            // Create the path definition
            PathDefinition pd = new PathDefinition();
            pd.setDestinationBusinessEntityDefinition(currentETD.getDestinationBusinessEntityDefinition());
            pd.setPathRules(null);
            pd.setSourceBusinessEntityDefinition(currentETD.getSourceBusinessEntityDefinition());
            pd.setHierarchyType(HierarchyType.NON_HIERARCHICAL);
            pd.setPriority(1);
            pd.setPathLength(1);
            pd.setCentralConceptType(CheckType.NO);
            pd.setType(currentETD.getTripleType());
            pd.setCloudId(cloud.getId());
            pd.setAssociationType(AssociationType.CONVERTABLE_ASSOCIATION);
            // Create the path
            Path path = new Path();
            path.setEntityTripleDefinition(currentETD);
            path.setEntityTripleOrder(0);
            path.setPathDefinition(pd);
            Set<Path> pathSet = new HashSet<Path>();
            pathSet.add(path);
            pd.setPaths(pathSet);
            pathDefinitionsList.add(pd);
         }
      }
      return pathDefinitionsList;
   }

   private List<Long> getPathDefinitionIdsList (List<UIPathDefinition> pathDefinitions) {
      List<Long> pathDefinitionIds = new ArrayList<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(pathDefinitions)) {
         for (UIPathDefinition pathDefinition : pathDefinitions) {
            pathDefinitionIds.add(pathDefinition.getPathDefinitionId());
         }
      }
      return pathDefinitionIds;
   }

   private List<Long> getETDIdsList (List<UIPathDefinition> pathDefinitions) {
      List<Long> etdIds = new ArrayList<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(pathDefinitions)) {
         for (UIPathDefinition pathDefinition : pathDefinitions) {
            etdIds.add(pathDefinition.getEntityTripleDefinitionId());
         }
      }
      return etdIds;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IKDXCloudRetrievalService getKdxCloudRetrievalService () {
      return kdxCloudRetrievalService;
   }

   public void setKdxCloudRetrievalService (IKDXCloudRetrievalService kdxCloudRetrievalService) {
      this.kdxCloudRetrievalService = kdxCloudRetrievalService;
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

   public IPathDefinitionDeletionService getPathDefinitionDeletionService () {
      return pathDefinitionDeletionService;
   }

   public void setPathDefinitionDeletionService (IPathDefinitionDeletionService pathDefinitionDeletionService) {
      this.pathDefinitionDeletionService = pathDefinitionDeletionService;
   }

}
