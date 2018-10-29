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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityVariation;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Hierarchy;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.SFLTermToken;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.bean.entity.comparator.InstanceDisplaNameComparator;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.bean.UIInstance;
import com.execue.handler.swi.IKDXServiceHandler;
import com.execue.platform.IRealTimeBusinessEntityWrapperService;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.swi.IBusinessEntityDeletionWrapperService;
import com.execue.platform.swi.IBusinessEntityManagementWrapperService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.uswh.service.IUnstructuredWarehouseRetrievalService;

public class KDXServiceHandlerImpl implements IKDXServiceHandler {

   private IKDXRetrievalService                    kdxRetrievalService;
   private IKDXManagementService                   kdxManagementService;
   private IBusinessEntityManagementWrapperService businessEntityManagementWrapperService;
   private IUnstructuredWarehouseRetrievalService  unstructuredWarehouseRetrievalService;
   private IRealTimeBusinessEntityWrapperService   realTimeBusinessEntityWrapperService;
   private IKDXCloudRetrievalService               kdxCloudRetrievalService;
   private IBaseKDXRetrievalService                baseKDXRetrievalService;
   private ICoreConfigurationService               coreConfigurationService;
   private IBusinessEntityDeletionWrapperService   businessEntityDeletionWrapperService;
   private static final Logger                     logger = Logger.getLogger(KDXServiceHandlerImpl.class);

   public Set<Stat> getAllStats () throws ExeCueException {
      return new TreeSet<Stat>(getKdxRetrievalService().getAllStats());
   }

   public Concept getConcept (Long conceptId) throws ExeCueException {
      return getKdxRetrievalService().getConceptById(conceptId);
   }

   public Model getModel (Long modelId) throws ExeCueException {
      return getKdxRetrievalService().getModelById(modelId);
   }

   public Cloud getDefaultAppCloud (Long modelId) throws ExeCueException {
      return getKdxCloudRetrievalService().getDefaultAppCloud(modelId);
   }

   public List<Model> getModelsByApplicationId (Long applicationId) throws ExeCueException {
      try {
         return getKdxRetrievalService().getModelsByApplicationId(applicationId);
      } catch (KDXException e) {
         throw new ExeCueException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, e.getMessage());
      }
   }

   public Instance getInstance (Long instanceId) throws ExeCueException {
      return getKdxRetrievalService().getInstanceById(instanceId);
   }

   public List<Concept> getConcepts (Long modelId) throws ExeCueException {
      return getKdxRetrievalService().getConcepts(modelId);
   }

   public List<Concept> getConceptsForAssetMetaInfo (Long assetId) throws ExeCueException {
      return getKdxRetrievalService().getConceptsForAssetMetaInfo(assetId);
   }

   public List<Instance> getInstances (Long modelId, Long conceptId) throws ExeCueException {
      return getKdxRetrievalService().getInstances(modelId, conceptId);
   }

   public List<Instance> getInstancesByPage (Long modelId, Long conceptId, Page page) throws ExeCueException {
      return getKdxRetrievalService().getInstancesByPage(modelId, conceptId, page);
   }

   public List<Instance> getInstancesForAssetMetaInfo (Long modelId, Long conceptId) throws ExeCueException {
      return getKdxRetrievalService().getInstancesForAssetMetaInfo(modelId, conceptId);
   }

   public void createConcept (Long modelId, Concept concept) throws ExeCueException {
      try {
         getBaseKDXRetrievalService().validateEntityForReservedWord(concept, BusinessEntityType.CONCEPT);
         getBusinessEntityManagementWrapperService().createConcept(modelId, concept);
      } catch (PlatformException e) {
         throw new ExeCueException(e.getCode(), e);
      }
   }

   public void createInstance (Long appId, Long modelId, AppSourceType appSourceType, Long conceptId, Instance instance)
            throws ExeCueException {
      try {
         getBaseKDXRetrievalService().validateEntityForReservedWord(instance,
                  BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);
         boolean regularInstanceCreation = true;
         if (AppSourceType.UNSTRUCTURED.equals(appSourceType)) {
            BusinessEntityDefinition conceptBED = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
                     conceptId, null);
            if (getUnstructuredWarehouseRetrievalService().isFeatureExists(appId, conceptBED.getId())) {
               regularInstanceCreation = false;
               getRealTimeBusinessEntityWrapperService().createInstance(modelId, conceptId, instance);
            }
         }
         if (regularInstanceCreation) {
            getBusinessEntityManagementWrapperService().createInstance(modelId, conceptId, instance);
         }
      } catch (PlatformException e) {
         throw new ExeCueException(e.getCode(), e);
      }
   }

   public void createHierarchy (Long modelId, Hierarchy hierarchy) throws ExeCueException {
      getKdxManagementService().createHierarchy(modelId, hierarchy);
   }

   public void updateHierarchy (Long modelId, Hierarchy hierarchy) throws ExeCueException {
      getKdxManagementService().updateHierarchy(modelId, hierarchy);
   }

   public void updateConcept (Long modelId, Concept concept) throws ExeCueException {
      try {
         getBaseKDXRetrievalService().validateEntityForReservedWord(concept, BusinessEntityType.CONCEPT);
         getKdxManagementService().updateConcept(modelId, concept);
      } catch (KDXException e) {
         throw new ExeCueException(e.getCode(), e);
      }
   }

   public void updateInstance (Long appId, Long modelId, AppSourceType appSourceType, Long conceptId, Instance instance)
            throws ExeCueException {
      try {
         getBaseKDXRetrievalService().validateEntityForReservedWord(instance,
                  BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);
         boolean regularInstanceUpdation = true;
         if (AppSourceType.UNSTRUCTURED.equals(appSourceType)) {
            BusinessEntityDefinition instanceBED = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
                     conceptId, instance.getId());
            if (getUnstructuredWarehouseRetrievalService().isFeatureValueExists(appId, instanceBED.getId())) {
               regularInstanceUpdation = false;
               getRealTimeBusinessEntityWrapperService().updateInstance(modelId, conceptId, instance);
            }
         }
         if (regularInstanceUpdation) {
            getKdxManagementService().updateInstance(modelId, conceptId, instance);
         }
      } catch (KDXException e) {
         throw new ExeCueException(e.getCode(), e);
      }
   }

   public BusinessEntityDefinition getBusinessEntityDefinitionByIds (Long modelId, Long conceptId, Long instanceId)
            throws ExeCueException {
      return getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId, conceptId, instanceId);
   }

   public Concept getConceptByName (Long modelId, String conceptName) throws HandlerException {
      try {
         return getKdxRetrievalService().getConceptByName(modelId, conceptName);
      } catch (KDXException kdxException) {
         logger.error(kdxException);
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, kdxException);
      }
   }

   // TODO: singleSWI Merge : CHANGE DOMAIN TO MODEL AND KDXSerive Changes

   public List<SFLTerm> getAllExistingSFLTerms () throws HandlerException {
      List<SFLTerm> sflTerms = new ArrayList<SFLTerm>();
      try {
         sflTerms = getKdxRetrievalService().getAllSFLTerms();
      } catch (KDXException kdxException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, kdxException);
      }
      return sflTerms;
   }

   public List<SFLTermToken> getSFLTermTokensBySFLTerm (String sflTerm) throws HandlerException {
      List<SFLTermToken> sflTermsTokens = new ArrayList<SFLTermToken>();
      try {
         sflTermsTokens = getKdxRetrievalService().getSFLTermTokensByLookupWord(sflTerm);
      } catch (KDXException kdxException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, kdxException);
      }
      return sflTermsTokens;
   }

   public void updateSFLTermTokens (List<SFLTermToken> termTokens) throws HandlerException {
      try {
         getKdxRetrievalService().updateSFLTermTokens(termTokens);
      } catch (KDXException kdxException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, kdxException);
      }
   }

   public List<SFLTerm> getSFLTermsByKeyWord (String keyWord) throws HandlerException {
      List<SFLTerm> sflTerms = new ArrayList<SFLTerm>();
      try {
         sflTerms = getKdxRetrievalService().getSFLTermsForKeyWord(keyWord);
      } catch (KDXException kdxException) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, kdxException);
      }
      return sflTerms;
   }

   public List<Relation> getRelations (Long modelId) throws ExeCueException {
      return getKdxRetrievalService().getRelationsByModel(modelId);
   }

   public Relation getRelation (Long relationId) throws ExeCueException {
      return getKdxRetrievalService().getRelationByID(relationId);
   }

   public void createRelation (Long modelId, Relation relation) throws ExeCueException {
      try {
         getBusinessEntityManagementWrapperService().createRelation(modelId, relation);
      } catch (PlatformException e) {
         throw new ExeCueException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, e.getMessage());
      }
   }

   public void updateRelation (Long modelId, Relation relation) throws ExeCueException {
      getKdxManagementService().updateRelation(modelId, relation);
   }

   public List<UIConcept> getBusinessTermsByPopularity (Long modelId) throws ExeCueException {
      List<UIConcept> uiConcepts = new ArrayList<UIConcept>();
      List<Concept> conceptsByPopularity = getKdxRetrievalService().getConceptsByPopularity(modelId,
               Long.valueOf(getCoreConfigurationService().getConceptRetrievalLimit()));
      if (ExecueCoreUtil.isCollectionNotEmpty(conceptsByPopularity)) {
         for (Concept concept : conceptsByPopularity) {
            UIConcept uiConcept = transformUIConcept(concept);
            List<Instance> instances = getKdxRetrievalService().getInstancesByPopularity(modelId, concept.getId(),
                     Long.valueOf(getCoreConfigurationService().getInstanceRetrievalLimit()));
            Collections.sort(instances, new InstanceDisplaNameComparator());
            uiConcept.setInstances(transformUIInstances(instances));
            uiConcepts.add(uiConcept);
         }
      }

      return uiConcepts;
   }

   private List<UIInstance> transformUIInstances (List<Instance> instances) {
      List<UIInstance> uiInstances = new ArrayList<UIInstance>();
      for (Instance instance : instances) {
         UIInstance uiInstance = new UIInstance();
         uiInstance.setId(instance.getId());
         uiInstance.setDisplayName(instance.getDisplayName());
         uiInstances.add(uiInstance);
      }
      return uiInstances;
   }

   private UIConcept transformUIConcept (Concept concept) {
      UIConcept uiConcept = new UIConcept();
      uiConcept.setDisplayName(concept.getDisplayName());
      uiConcept.setId(concept.getId());
      return uiConcept;
   }

   public Concept getPopulatedConceptWithStats (Long conceptId) throws ExeCueException {
      try {
         return getKdxRetrievalService().getPopulatedConceptWithStats(conceptId);
      } catch (KDXException e) {
         throw new ExeCueException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, e.getMessage());
      }
   }

   public void saveBusinessEntityVariations (Long modelId, Long bedId, List<String> variations) throws ExeCueException {
      getKdxManagementService().deleteBusinessEntityVariationsByBedId(bedId);
      if (ExecueCoreUtil.isCollectionNotEmpty(variations)) {
         ModelGroup primaryGroup = getKdxRetrievalService().getPrimaryGroup(modelId);
         getKdxManagementService().createBusinessEntityVariations(
                  prepareBusinessEntityVariations(primaryGroup.getId(), bedId, variations));
      }
   }

   @Override
   public void deleteConceptHeirarchy (Long modelId, Long conceptId) throws HandlerException {
      try {
         BusinessEntityDefinition conceptBed = getKdxRetrievalService().getUnpopulatedBusinessEntityDefinitionByIds(
                  modelId, conceptId, null);
         getBusinessEntityDeletionWrapperService().deleteConceptHeirarchy(modelId, conceptBed.getId());
      } catch (PlatformException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }

   }

   @Override
   public void deleteInstanceHeirarchy (Long modelId, Long conceptId, Long instanceId) throws HandlerException {
      try {
         BusinessEntityDefinition parentConceptBedId = getKdxRetrievalService()
                  .getUnpopulatedBusinessEntityDefinitionByIds(modelId, conceptId, null);
         BusinessEntityDefinition instanceBed = getKdxRetrievalService().getUnpopulatedBusinessEntityDefinitionByIds(
                  modelId, conceptId, instanceId);
         getBusinessEntityDeletionWrapperService().deleteInstanceHeirarchy(modelId, parentConceptBedId.getId(),
                  instanceBed.getId());
      } catch (PlatformException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }

   }

   @Override
   public void deleteInstancesHeirarchyForConcept (Long modelId, Long conceptId) throws HandlerException {
      try {
         getBusinessEntityDeletionWrapperService().deleteInstanceHeirarchyForConcept(modelId, conceptId);
      } catch (PlatformException e) {
         throw new HandlerException(e.getCode(), e);
      }

   }

   @Override
   public void deleteRelationHeirarchy (Long modelId, Long relationId) throws HandlerException {
      try {
         BusinessEntityDefinition relationBED = getKdxRetrievalService().getRelationBEDById(modelId, relationId);
         getBusinessEntityDeletionWrapperService().deleteRelationHeirarchy(modelId, relationBED.getId());
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (PlatformException e) {
         throw new HandlerException(e.getCode(), e);
      }

   }

   private List<BusinessEntityVariation> prepareBusinessEntityVariations (Long modelGroupId, Long bedId,
            List<String> variations) {
      List<BusinessEntityVariation> businessEntityVariations = new ArrayList<BusinessEntityVariation>();
      for (String variation : variations) {
         BusinessEntityVariation businessEntityVariation = new BusinessEntityVariation();
         businessEntityVariation.setBusinessEntityId(bedId);
         businessEntityVariation.setVariation(variation);
         businessEntityVariation.setModelGroupId(modelGroupId);
         businessEntityVariations.add(businessEntityVariation);
      }
      return businessEntityVariations;
   }

   public List<String> getBusinessEntityVariations (Long entityBedId) throws ExeCueException {
      List<BusinessEntityVariation> businessEntityVariations = getKdxRetrievalService().getBusinessEntityVariations(
               entityBedId);
      List<String> variations = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionNotEmpty(businessEntityVariations)) {
         populateVariations(variations, businessEntityVariations);

      }
      return variations;
   }

   @Override
   public boolean hasInstances (Long modelId, Long conceptId) throws HandlerException {
      try {
         return getKdxRetrievalService().hasInstances(modelId, conceptId);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   private void populateVariations (List<String> variations, List<BusinessEntityVariation> businessEntityVariations) {
      for (BusinessEntityVariation businessEntityVariation : businessEntityVariations) {
         variations.add(businessEntityVariation.getVariation());
      }
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

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService
    *           the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   public IKDXCloudRetrievalService getKdxCloudRetrievalService () {
      return kdxCloudRetrievalService;
   }

   public void setKdxCloudRetrievalService (IKDXCloudRetrievalService kdxCloudRetrievalService) {
      this.kdxCloudRetrievalService = kdxCloudRetrievalService;
   }

   public IBusinessEntityManagementWrapperService getBusinessEntityManagementWrapperService () {
      return businessEntityManagementWrapperService;
   }

   public void setBusinessEntityManagementWrapperService (
            IBusinessEntityManagementWrapperService businessEntityManagementWrapperService) {
      this.businessEntityManagementWrapperService = businessEntityManagementWrapperService;
   }

   public IRealTimeBusinessEntityWrapperService getRealTimeBusinessEntityWrapperService () {
      return realTimeBusinessEntityWrapperService;
   }

   public void setRealTimeBusinessEntityWrapperService (
            IRealTimeBusinessEntityWrapperService realTimeBusinessEntityWrapperService) {
      this.realTimeBusinessEntityWrapperService = realTimeBusinessEntityWrapperService;
   }

   /**
    * @return the unstructuredWarehouseRetrievalService
    */
   public IUnstructuredWarehouseRetrievalService getUnstructuredWarehouseRetrievalService () {
      return unstructuredWarehouseRetrievalService;
   }

   /**
    * @param unstructuredWarehouseRetrievalService
    *           the unstructuredWarehouseRetrievalService to set
    */
   public void setUnstructuredWarehouseRetrievalService (
            IUnstructuredWarehouseRetrievalService unstructuredWarehouseRetrievalService) {
      this.unstructuredWarehouseRetrievalService = unstructuredWarehouseRetrievalService;
   }

   /**
    * @return the businessEntityDeletionWrapperService
    */
   public IBusinessEntityDeletionWrapperService getBusinessEntityDeletionWrapperService () {
      return businessEntityDeletionWrapperService;
   }

   /**
    * @param businessEntityDeletionWrapperService
    *           the businessEntityDeletionWrapperService to set
    */
   public void setBusinessEntityDeletionWrapperService (
            IBusinessEntityDeletionWrapperService businessEntityDeletionWrapperService) {
      this.businessEntityDeletionWrapperService = businessEntityDeletionWrapperService;
   }

}
