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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Hierarchy;
import com.execue.core.common.bean.entity.HierarchyDetail;
import com.execue.core.common.bean.governor.BusinessEntityInfo;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.swi.IHierarchyDefinitionServiceHandler;
import com.execue.security.UserContextService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;

/**
 * 
 * @author Jitendra
 *
 */
public class HierarchyDefinitionServiceHandlerImpl extends UserContextService implements
         IHierarchyDefinitionServiceHandler {

   private static final Logger       logger = Logger.getLogger(HierarchyDefinitionServiceHandlerImpl.class);

   private IKDXRetrievalService      kdxRetrievalService;
   private IKDXManagementService     kdxManagementService;
   private ICoreConfigurationService coreConfigurationService;

   @Override
   public List<Hierarchy> getHierarchiesForModel (Long modelId) throws HandlerException {
      try {
         return getKdxRetrievalService().getHierarchiesByModelId(modelId);
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }
   }

   @Override
   public List<UIConcept> getExistingHierarchyDefinitions (Long hierarchyId) throws HandlerException {
      List<UIConcept> hierarchyDetails = new ArrayList<UIConcept>();
      try {
         Hierarchy hierarchy = getKdxRetrievalService().getHierarchyById(hierarchyId);
         hierarchyDetails = getHierarchyDetailsAsUIConcepts(hierarchy);
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }
      return hierarchyDetails;
   }

   @Override
   public List<UIConcept> getAllDimensionsForModel (Long modelId) throws HandlerException {
      List<UIConcept> dimensionUIConcepts = new ArrayList<UIConcept>();
      try {
         List<BusinessEntityInfo> allConceptBusinessEntities = getKdxRetrievalService().getAllConceptBusinessEntities(
                  modelId);
         List<Long> allConceptBEDIDs = getBEDIDsFromBusinessEntities(allConceptBusinessEntities);
         List<Long> dimensionConceptBEDIDs = getKdxRetrievalService().getConceptBedsHavingBehaviorType(
                  allConceptBEDIDs, BehaviorType.ENUMERATION);
         List<Concept> dimensionConcepts = new ArrayList<Concept>();
         Concept dimConcept = new Concept();
         for (Long dimensionConceptBEDID : dimensionConceptBEDIDs) {
            dimConcept = getKdxRetrievalService().getConceptByBEDId(dimensionConceptBEDID);
            dimConcept.setBedId(dimensionConceptBEDID);
            dimensionConcepts.add(dimConcept);
         }
         dimensionUIConcepts = transformToUIConcpets(dimensionConcepts);
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }
      return dimensionUIConcepts;
   }

   @Override
   public Hierarchy saveUpdateHierarchyDefinitions (Long modelId, Hierarchy hierarchy,
            List<Long> hierarchyDetailConceptBEDIDs) throws HandlerException {
      try {
         prepareHierarchyDefinition(modelId, hierarchy, hierarchyDetailConceptBEDIDs);
         if (hierarchy.getId() == null) {
            getKdxManagementService().createHierarchy(modelId, hierarchy);
         } else {
            getKdxManagementService().updateHierarchy(modelId, hierarchy);
         }
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }
      return hierarchy;
   }

   @Override
   public void deleteHierarchyDefinitions (Long hierarchyId) throws HandlerException {
      try {
         Hierarchy hierarchy = getKdxRetrievalService().getHierarchyById(hierarchyId);
         getKdxManagementService().deleteHierarchy(hierarchy);
      } catch (KDXException kdxException) {
         throw new HandlerException(kdxException.getCode(), kdxException);
      }

   }

   @Override
   public Long getMaxHierarchySize () {
      return getCoreConfigurationService().getMaxHierarchySize();
   }

   private void prepareHierarchyDefinition (Long modelId, Hierarchy hierarchy, List<Long> hierarchyDetailConceptBEDIDs)
            throws KDXException {
      hierarchy.setModelGroupId(getKdxRetrievalService().getPrimaryGroup(modelId).getId());
      hierarchy.setHierarchyDetails(new HashSet<HierarchyDetail>());
      HierarchyDetail hierarchyDetail = null;
      int hierarchylevel = 0;
      for (Long hierarchyDetailConceptBEDID : hierarchyDetailConceptBEDIDs) {
         hierarchyDetail = new HierarchyDetail();
         hierarchyDetail.setConceptBedId(hierarchyDetailConceptBEDID);
         hierarchyDetail.setLevel(hierarchylevel++);
         hierarchy.getHierarchyDetails().add(hierarchyDetail);
      }
   }

   private List<Long> getBEDIDsFromBusinessEntities (List<BusinessEntityInfo> allConceptBusinessEntities) {
      List<Long> bedIDs = new ArrayList<Long>();
      for (BusinessEntityInfo beInfo : allConceptBusinessEntities) {
         bedIDs.add(beInfo.getBusinessEntityTermId());
      }
      return bedIDs;
   }

   private List<UIConcept> transformToUIConcpets (List<Concept> concepts) {
      List<UIConcept> uiConcepts = new ArrayList<UIConcept>();
      for (Concept concept : concepts) {
         uiConcepts.add(transformToUIConcpet(concept));
      }
      return uiConcepts;
   }

   private UIConcept transformToUIConcpet (Concept concept) {
      UIConcept uiConcept = new UIConcept();

      uiConcept.setBedId(concept.getBedId());
      uiConcept.setId(concept.getId());
      uiConcept.setName(concept.getName());
      uiConcept.setDisplayName(concept.getDisplayName());

      return uiConcept;
   }

   private List<UIConcept> getHierarchyDetailsAsUIConcepts (Hierarchy hierarchy) throws KDXException {
      List<UIConcept> hierarchyDetailConcepts = new ArrayList<UIConcept>();
      List<HierarchyDetail> hierarchyDetails = new ArrayList<HierarchyDetail>();
      hierarchyDetails.addAll(hierarchy.getHierarchyDetails());
      Collections.sort(hierarchyDetails);
      Concept concept = null;
      for (HierarchyDetail hierarchyDetail : hierarchyDetails) {
         concept = getKdxRetrievalService().getConceptByBEDId(hierarchyDetail.getConceptBedId());
         concept.setBedId(hierarchyDetail.getConceptBedId());
         hierarchyDetailConcepts.add(transformToUIConcpet(concept));
      }
      return hierarchyDetailConcepts;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
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
