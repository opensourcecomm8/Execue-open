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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.HierarchyRelationType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.bean.UIPathDefinition;
import com.execue.handler.bean.UIRelation;
import com.execue.handler.swi.ITripleDefinitionsServiceHandler;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IKDXRetrievalService;

public class TripleDefinitionsServiceHandlerImpl implements ITripleDefinitionsServiceHandler {

   private IKDXModelService          kdxModelService;
   private IKDXRetrievalService      kdxRetrievalService;
   private ICoreConfigurationService coreConfigurationService;

   public List<UIPathDefinition> getUIPathDefinitionsForHeirarchy (Long modelId, Long conceptBedId,
            HierarchyRelationType heirarchyRelationType) throws HandlerException {
      List<UIPathDefinition> hierarchyPathDefinitions = new ArrayList<UIPathDefinition>();

      Long relationBEDId = Long.parseLong(heirarchyRelationType.getValue().split("#")[0]);
      List<EntityTripleDefinition> entityTripleDefinitions = new ArrayList<EntityTripleDefinition>(1);
      try {
         if (heirarchyRelationType == HierarchyRelationType.ADD_CHILD) {
            entityTripleDefinitions = getKdxModelService().getEntityTriplesForSourceAndRelation(conceptBedId,
                     relationBEDId);
            hierarchyPathDefinitions = transformEntityTripleDefinitions(entityTripleDefinitions, CheckType.YES, false,
                     false);
         } else if (heirarchyRelationType == HierarchyRelationType.ADD_PARENT) {
            entityTripleDefinitions = getKdxModelService().getEntityTriplesForDestinationAndRelation(relationBEDId,
                     conceptBedId);
            hierarchyPathDefinitions = transformEntityTripleDefinitions(entityTripleDefinitions, CheckType.YES, false,
                     true);
         } else {
            // TODO:-JT added mock data to display in screen for any other heirarchyRelationType case
            UIPathDefinition uiPathDefinition = new UIPathDefinition();
            uiPathDefinition.setEntityTripleDefinitionId(101L);
            uiPathDefinition.setSourceConceptBedId(1001L);
            uiPathDefinition.setSourceConceptName("Age");
            uiPathDefinition.setDestinationConceptBedId(1001L);
            uiPathDefinition.setDestinationConceptName("AddressHierarchy");
            uiPathDefinition.setRelationBedId(10005L);

            UIPathDefinition uiPathDefinition1 = new UIPathDefinition();
            uiPathDefinition1.setEntityTripleDefinitionId(102L);
            uiPathDefinition1.setSourceConceptBedId(1001L);
            uiPathDefinition1.setSourceConceptName("Age");
            uiPathDefinition1.setDestinationConceptBedId(1002L);
            uiPathDefinition1.setDestinationConceptName("LoanHierarchy");
            uiPathDefinition1.setRelationBedId(10006L);

            hierarchyPathDefinitions.add(uiPathDefinition);
            hierarchyPathDefinitions.add(uiPathDefinition1);
         }
      } catch (SWIException e) {
         throw new HandlerException(e.getCode(), e);
      }
      return hierarchyPathDefinitions;
   }

   private List<UIPathDefinition> transformEntityTripleDefinitions (
            List<EntityTripleDefinition> entityTripleDefinitions, CheckType source, boolean skipHierarchyRelations,
            boolean flipSourceAndDestination) throws KDXException {
      List<UIPathDefinition> uiPathDefinitions = new ArrayList<UIPathDefinition>();
      if (ExecueCoreUtil.isCollectionEmpty(entityTripleDefinitions)) {
         return uiPathDefinitions;
      }

      for (EntityTripleDefinition etd : entityTripleDefinitions) {
         String relationName = etd.getRelation().getRelation().getName();
         if (skipHierarchyRelations
                  && (ExecueConstants.PARENT_PROPERTY.equals(relationName) || ExecueConstants.IS_COMPOSED_OF_PROPERTY
                           .equals(relationName))) {
            continue;
         }
         UIPathDefinition uiPathDefinition = new UIPathDefinition();
         uiPathDefinition.setSource(source);
         uiPathDefinition.setEntityTripleDefinitionId(etd.getId());
         if (flipSourceAndDestination) {
            uiPathDefinition.setSourceConceptBedId(etd.getDestinationBusinessEntityDefinition().getId());
            if (BusinessEntityType.CONCEPT.equals(etd.getDestinationBusinessEntityDefinition().getEntityType())) {
               uiPathDefinition.setSourceConceptName(etd.getDestinationBusinessEntityDefinition().getConcept()
                        .getName());
            } else {
               // TODO: -JM- handle the concept profile case
               // ignore the current path definition and continue
               continue;
            }
         } else {
            uiPathDefinition.setSourceConceptBedId(etd.getSourceBusinessEntityDefinition().getId());
            if (BusinessEntityType.CONCEPT.equals(etd.getSourceBusinessEntityDefinition().getEntityType())) {
               uiPathDefinition.setSourceConceptName(etd.getSourceBusinessEntityDefinition().getConcept().getName());
            } else {
               // TODO: -JM- handle the concept profile case
               // ignore the current path definition and continue
               continue;
            }
         }

         if (flipSourceAndDestination) {
            uiPathDefinition.setDestinationConceptBedId(etd.getSourceBusinessEntityDefinition().getId());
            if (BusinessEntityType.CONCEPT.equals(etd.getSourceBusinessEntityDefinition().getEntityType())) {
               uiPathDefinition.setDestinationConceptName(etd.getSourceBusinessEntityDefinition().getConcept()
                        .getName());
            } else {
               // TODO: -JM- handle the concept profile case
               // ignore the current path definition and continue
               continue;
            }
         } else {
            uiPathDefinition.setDestinationConceptBedId(etd.getDestinationBusinessEntityDefinition().getId());
            if (BusinessEntityType.CONCEPT.equals(etd.getDestinationBusinessEntityDefinition().getEntityType())) {
               uiPathDefinition.setDestinationConceptName(etd.getDestinationBusinessEntityDefinition().getConcept()
                        .getName());
            } else {
               // TODO: -JM- handle the concept profile case
               // ignore the current path definition and continue
               continue;
            }
         }
         uiPathDefinition.setRelationName(relationName);
         uiPathDefinition.setRelationBedId(etd.getRelation().getId());
         uiPathDefinitions.add(uiPathDefinition);
      }
      return uiPathDefinitions;
   }

   public List<UIConcept> getUIConceptsForHeirarchy (Long modelId, Long conceptBedId,
            HierarchyRelationType heirarchyRelationType) throws HandlerException {
      List<UIConcept> concepts = new ArrayList<UIConcept>(1);
      if (heirarchyRelationType == HierarchyRelationType.ADD_PARENT
               || heirarchyRelationType == HierarchyRelationType.ADD_CHILD) {
         List<BusinessEntityDefinition> eligibleConceptBEDs = new ArrayList<BusinessEntityDefinition>(1);
         List<Long> allParentBedIds = new ArrayList<Long>(1);
         List<Long> allChildBedIds = new ArrayList<Long>(1);
         List<BusinessEntityDefinition> conceptBEDsOfModel = new ArrayList<BusinessEntityDefinition>(1);
         try {
            // Get all the parent and child bed ids for the given concept
            allParentBedIds = getKdxModelService().getAllParentBedIds(conceptBedId, modelId);
            allChildBedIds = getKdxModelService().getAllChildBedIds(conceptBedId, modelId);

            // Get all the concept beds for the given model
            conceptBEDsOfModel = getKdxRetrievalService().getConceptBEDsOfModel(modelId);
         } catch (KDXException e) {
            throw new HandlerException(e.getCode(), e);
         }

         // Filter all the parent and child bed ids from the existing concepts of the model
         Map<Long, BusinessEntityDefinition> conceptBedIdMap = prepareBedIdMap(conceptBEDsOfModel);
         Set<Long> conceptBedIds = conceptBedIdMap.keySet();
         conceptBedIds.removeAll(allParentBedIds);
         conceptBedIds.removeAll(allChildBedIds);
         conceptBedIds.remove(conceptBedId);
         for (Long conceptBed : conceptBedIds) {
            eligibleConceptBEDs.add(conceptBedIdMap.get(conceptBed));
         }

         // Perform UI Transformation
         concepts = transformRealizedConcepts(eligibleConceptBEDs);
      } else {
         // TODO: Need to handle the add part HierarchyRelationType case
         UIConcept address = new UIConcept();
         address.setBedId(1001L);
         address.setDisplayName("AddressHierarchy");
         UIConcept loan = new UIConcept();
         loan.setBedId(1002L);
         loan.setDisplayName("LoanHierarchy");
         UIConcept bankAsset = new UIConcept();
         bankAsset.setBedId(1003L);
         bankAsset.setDisplayName("LoanHierarchy");
         concepts.add(address);
         concepts.add(bankAsset);
         concepts.add(loan);
      }
      return concepts;
   }

   private Map<Long, BusinessEntityDefinition> prepareBedIdMap (List<BusinessEntityDefinition> conceptBEDsOfModel) {
      Map<Long, BusinessEntityDefinition> conceptBedIdMap = new HashMap<Long, BusinessEntityDefinition>(1);
      for (BusinessEntityDefinition businessEntityDefinition : conceptBEDsOfModel) {
         conceptBedIdMap.put(businessEntityDefinition.getId(), businessEntityDefinition);
      }
      return conceptBedIdMap;
   }

   private List<UIConcept> transformRealizedConcepts (List<BusinessEntityDefinition> businessEntityDefinitions) {
      List<UIConcept> uiConcepts = new ArrayList<UIConcept>(1);
      if (CollectionUtils.isEmpty(businessEntityDefinitions)) {
         return uiConcepts;
      }
      for (BusinessEntityDefinition businessEntityDefinition : businessEntityDefinitions) {
         UIConcept uiConcept = new UIConcept();
         uiConcept.setId(businessEntityDefinition.getConcept().getId());
         uiConcept.setBedId(businessEntityDefinition.getId());
         uiConcept.setDisplayName(businessEntityDefinition.getConcept().getDisplayName());
         uiConcepts.add(uiConcept);
      }
      return uiConcepts;
   }

   public List<UIConcept> getUIConceptsForRelation (Long modelId, Long conceptBedId) throws HandlerException {
      List<UIConcept> concepts = new ArrayList<UIConcept>(1);
      List<BusinessEntityDefinition> conceptBEDsOfModel = new ArrayList<BusinessEntityDefinition>(1);
      // Get all the concept beds for the given model
      try {
         conceptBEDsOfModel = getKdxRetrievalService().getConceptBEDsOfModel(modelId);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }

      // Filter only the selected concept from the existing concepts of the model
      Map<Long, BusinessEntityDefinition> conceptBedIdMap = prepareBedIdMap(conceptBEDsOfModel);
      Set<Long> conceptBedIds = conceptBedIdMap.keySet();
      conceptBedIds.remove(conceptBedId);
      List<BusinessEntityDefinition> eligibleConceptBEDs = new ArrayList<BusinessEntityDefinition>(1);
      for (Long conceptBed : conceptBedIds) {
         eligibleConceptBEDs.add(conceptBedIdMap.get(conceptBed));
      }

      // Perform UI Transformation
      concepts = transformRealizedConcepts(eligibleConceptBEDs);
      return concepts;
   }

   public List<UIPathDefinition> getUIPathDefinitionsForRelation (Long modelId, Long conceptBedId)
            throws HandlerException {

      List<UIPathDefinition> relationPathDefinitions = new ArrayList<UIPathDefinition>(1);
      try {
         List<EntityTripleDefinition> entityTripleDefinitions = getKdxModelService()
                  .getEntityTriplesForSourceAndTripleType(conceptBedId,
                           EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT);
         relationPathDefinitions.addAll(transformEntityTripleDefinitions(entityTripleDefinitions, CheckType.YES, true,
                  false));

         entityTripleDefinitions = getKdxModelService().getEntityTriplesForDestinationAndTripleType(conceptBedId,
                  EntityTripleDefinitionType.CONCEPT_RELATION_CONCEPT);

         relationPathDefinitions.addAll(transformEntityTripleDefinitions(entityTripleDefinitions, CheckType.NO, true,
                  false));
      } catch (SWIException e) {
         throw new HandlerException(e.getCode(), e);
      }

      // // TODO:-JT added mock data to display in screen
      // UIPathDefinition uiPathDefinition = new UIPathDefinition();
      // uiPathDefinition.setEntityTripleDefinitionId(101L);
      // uiPathDefinition.setSourceConceptBedId(1001L);
      // uiPathDefinition.setSourceConceptName("Age");
      // uiPathDefinition.setDestinationConceptBedId(1001L);
      // uiPathDefinition.setDestinationConceptName("Address");
      // uiPathDefinition.setRelationName("hasRelation");
      // uiPathDefinition.setRelationBedId(10005L);
      //
      // UIPathDefinition uiPathDefinition1 = new UIPathDefinition();
      // uiPathDefinition1.setEntityTripleDefinitionId(102L);
      // uiPathDefinition1.setSourceConceptBedId(1001L);
      // uiPathDefinition1.setSourceConceptName("Age");
      // uiPathDefinition1.setDestinationConceptBedId(1002L);
      // uiPathDefinition1.setDestinationConceptName("Loan");
      // uiPathDefinition1.setRelationName("hasNewRelation");
      // uiPathDefinition1.setRelationBedId(10006L);
      //
      // relationPathDefinitions.add(uiPathDefinition);
      // relationPathDefinitions.add(uiPathDefinition1);
      return relationPathDefinitions;
   }

   public List<UIRelation> suggestRelationsByName (Long modelId, String searchString) throws HandlerException {
      try {
         int resultSize = getCoreConfigurationService().getResultsPageSize();
         List<Relation> relations = getKdxRetrievalService().suggestRelationsByName(modelId, searchString, resultSize);
         return tranformRelation(relations);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   private List<UIRelation> tranformRelation (List<Relation> relations) {
      List<UIRelation> uiRelations = new ArrayList<UIRelation>();
      if (ExecueCoreUtil.isCollectionNotEmpty(relations)) {
         for (Relation relation : relations) {
            UIRelation uiRelation = new UIRelation();
            uiRelation.setId(relation.getId());
            uiRelation.setName(relation.getName());
            // TODO-JT- need to discuss whether we need to send name or display name
            // Set display name as relation name for auto suggest
            uiRelation.setDisplayName(relation.getName());
            uiRelations.add(uiRelation);
         }
      }
      return uiRelations;
   }

   public void saveHierarchyPathDefinitions (Long modelId, HierarchyRelationType selectedHierarchyType,
            List<UIPathDefinition> selectedHierarchyPathDefinitions,
            List<UIPathDefinition> savedHierarchyPathDefinitions) throws HandlerException {
      // TODO:- Need implementation

   }

   public void saveRelationPathDefinitions (Long modelId, List<UIPathDefinition> selectedRelationPathDefinitions,
            List<UIPathDefinition> savedRelationPathDefinitions) throws HandlerException {
      // TODO:- Need implementation

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

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}
