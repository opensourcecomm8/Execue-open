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
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.service.ICubeOperationalConstants;
import com.execue.core.common.bean.ApplicationContext;
import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.ac.AnswersCatalogMaintenanceRequestInfo;
import com.execue.core.common.bean.ac.AnswersCatalogManagementQueue;
import com.execue.core.common.bean.ac.AnswersCatalogUpdationContext;
import com.execue.core.common.bean.ac.CubeCreationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.type.ACManagementOperationSourceType;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.core.common.type.AnswersCatalogOperationType;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.util.AssetGrainUtils;
import com.execue.core.common.util.ExeCueXMLUtils;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIAsset;
import com.execue.handler.bean.UICubeCreation;
import com.execue.handler.bean.UIInstanceMember;
import com.execue.handler.bean.UIStatus;
import com.execue.handler.comparator.UIInstanceMemberNameComparator;
import com.execue.handler.swi.ICubeManagementServiceHandler;
import com.execue.handler.swi.IPreferencesServiceHandler;
import com.execue.platform.helper.AnswersCatalogPlatformServiceHelper;
import com.execue.platform.helper.RangeSuggestionServiceHelper;
import com.execue.platform.swi.IRangeSuggestionService;
import com.execue.qdata.exception.AnswersCatalogManagementQueueException;
import com.execue.qdata.exception.QueryDataException;
import com.execue.scheduler.service.ICubeCreationJobService;
import com.execue.scheduler.service.ICubeRefreshJobService;
import com.execue.scheduler.service.ICubeUpdationJobService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;

public class CubeManagementServiceHandlerImpl extends AbstractAnswerCatalogManagementServiceHandler implements
         ICubeManagementServiceHandler {

   private static Logger              logger = Logger.getLogger(CubeManagementServiceHandlerImpl.class);

   private ICubeCreationJobService    cubeCreationJobService;
   private ICubeUpdationJobService    cubeUpdationJobService;
   private ICubeRefreshJobService     cubeRefreshJobService;
   private IPreferencesServiceHandler preferencesServiceHandler;
   private IRangeSuggestionService    rangeSuggestionService;

   public List<Concept> getEligibleDimensions (Long assetId) throws HandlerException {
      try {
         return getKdxRetrievalService().getConceptsForAsset(assetId);
      } catch (KDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   @Override
   public List<Concept> getEligibleConceptsOfAssetForCubeByPage (Long assetId, Page page) throws HandlerException {
      try {
         return getKdxRetrievalService().getEligibleConceptsOfAssetForCubeByPage(assetId, page);
      } catch (KDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   @Override
   public Range getSuggestedRangesForConcept (Long modelId, Long assetId, Long conceptBedId) throws HandlerException {
      try {
         Range existingRange = getPreferencesServiceHandler().getExistingRangeForConcept(modelId, conceptBedId);
         if (existingRange != null) {
            Range clonedExistingRange = ExecueBeanCloneUtil.cloneRange(existingRange);
            return resetIdsToNull(clonedExistingRange);
         } else {
            return getRangeSuggestionService().deduceRange(
                     RangeSuggestionServiceHelper.populateDynamicRangeInput(modelId, assetId, conceptBedId));
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }

   }

   private Range resetIdsToNull (Range range) {
      range.setId(null);
      range.setUser(null);
      Set<RangeDetail> rangeDetails = range.getRangeDetails();
      for (RangeDetail rangeDetail : rangeDetails) {
         rangeDetail.setId(null);
      }
      return range;
   }

   public List<UIInstanceMember> getConceptDetails (Long modelId, Long conceptId, Long assetId) throws HandlerException {
      UIInstanceMember instanceMember = null;
      List<UIInstanceMember> instanceMembers = new ArrayList<UIInstanceMember>();
      try {
         List<Instance> instances = getKdxRetrievalService().getInstances(modelId, conceptId);

         if (instances != null) {
            for (Instance instance : instances) {
               instanceMember = new UIInstanceMember();
               instanceMember.setInstanceId(instance.getId());
               instanceMember.setInstanceDisplayName(instance.getDisplayName());
               BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
               businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT_LOOKUP_INSTANCE);
               businessEntityTerm.setBusinessEntity(instance);

               List<Mapping> mappings = getMappingRetrievalService().getAssetEntities(businessEntityTerm, modelId,
                        assetId);
               if (ExecueCoreUtil.isCollectionNotEmpty(mappings)) {
                  List<AssetEntityDefinition> assetEntityDefinitions = ExecueBeanUtil
                           .getAssetEntityDefinitions(mappings);
                  AssetEntityDefinition correctAssetEntityDefinition = getSdxRetrievalService()
                           .pickCorrectAssetEntityDefinition(assetEntityDefinitions);
                  Membr membr = correctAssetEntityDefinition.getMembr();
                  instanceMember.setMemberId(membr.getId());
                  instanceMember.setMemberValue(membr.getLookupValue());
                  instanceMember.setMemberDescription(membr.getLookupDescription());
                  instanceMembers.add(instanceMember);
               }

            }
         }
      } catch (MappingException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      } catch (SDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      } catch (KDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
      if (instanceMembers.size() > 0) {
         Collections.sort(instanceMembers, new UIInstanceMemberNameComparator());
      }
      return instanceMembers;
   }

   public UIStatus createCube (UICubeCreation uiCubeCreationContext, ApplicationContext applicationContext)
            throws HandlerException {
      UIStatus uiStatus = new UIStatus();
      try {
         Long parentAssetId = uiCubeCreationContext.getBaseAsset().getId();
         if (getAnswersCatalogManagementQueueService().isAnswersCatalogManagementQueueUnderProgress(parentAssetId)) {
            uiStatus.setStatus(false);
            uiStatus.addErrorMessage(ICubeOperationalConstants.ASSET_ALREADY_UNDER_PROCESS_MESSAGE);
         }
         if (uiStatus.isStatus()) {
            // TODO: -RG- When Static Ranges start coming via the screen, this method needs to be enhanced
            adjustRangeIds(uiCubeCreationContext);
            AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo = AnswersCatalogPlatformServiceHelper
                     .prepareAnswersCatalogMaintenanceRequestInfo(applicationContext.getAppId(), applicationContext
                              .getModelId(), parentAssetId, getUserContext().getUser(), null, uiCubeCreationContext
                              .getSelectedRanges(), populateSimpleLookupDiemnsions(uiCubeCreationContext
                              .getSelectedConcepts()), null, null, uiCubeCreationContext.getTargetAsset());

            CubeCreationContext cubeCreationContext = getAnswersCatalogContextBuilderService()
                     .buildCubeCreationContext(answersCatalogMaintenanceRequestInfo);
            // populate ACMQ context and create an entry in ACMQ
            AnswersCatalogManagementQueue answersCatalogManagementQueue = AnswersCatalogPlatformServiceHelper
                     .prepareAnswersCatalogManagementQueue(parentAssetId, null, null, ExeCueXMLUtils
                              .getXMLStringFromObject(cubeCreationContext), null,
                              ACManagementOperationSourceType.USER_REQUEST, ACManagementOperationStatusType.INITIATED,
                              AnswersCatalogOperationType.CUBE_CREATION);
            getAnswersCatalogManagementQueueService()
                     .createAnswersCatalogManagementQueue(answersCatalogManagementQueue);

            //set mcmqId to context
            cubeCreationContext.setAnswersCatalogManagementQueueId(answersCatalogManagementQueue.getId());
            uiStatus.setJobRequestId(getCubeCreationJobService().scheduleCubeCreationJob(cubeCreationContext));
         }
      } catch (AnswersCatalogException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      } catch (AnswersCatalogManagementQueueException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
      return uiStatus;
   }

   @Override
   public UIStatus refreshCube (Long existingAssetId, ApplicationContext applicationContext) throws HandlerException {
      UIStatus uiStatus = new UIStatus();
      //TODO-JT- Issue with cube refresh, we are inserting record in AOD table with existingId but in case of 
      //refresh we are deleting and then creating asset.So in this case in AOD table we have an entry with assetId that is not even exist.
      try {
         Long parentAssetId = getAssetById(existingAssetId).getBaseAssetId();
         if (getAnswersCatalogManagementQueueService().isAnswersCatalogManagementQueueUnderProgress(
                  prepareAssetIds(existingAssetId, parentAssetId))) {
            uiStatus.setStatus(false);
            uiStatus.addErrorMessage(ICubeOperationalConstants.ASSET_OR_PARENT_ASSET_ALREADY_UNDER_PROCESS_MESSAGE);
         }
         if (uiStatus.isStatus()) {
            AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo = AnswersCatalogPlatformServiceHelper
                     .prepareAnswersCatalogMaintenanceRequestInfo(applicationContext.getAppId(), applicationContext
                              .getModelId(), parentAssetId, getUserContext().getUser(), existingAssetId, null, null,
                              null, null, null);
            AnswersCatalogUpdationContext cubeRefreshContext = getAnswersCatalogContextBuilderService()
                     .buildCubeRefreshContext(answersCatalogMaintenanceRequestInfo);
            AnswersCatalogManagementQueue answersCatalogManagementQueue = AnswersCatalogPlatformServiceHelper
                     .prepareAnswersCatalogManagementQueue(parentAssetId, existingAssetId, null, ExeCueXMLUtils
                              .getXMLStringFromObject(cubeRefreshContext), null,
                              ACManagementOperationSourceType.USER_REQUEST, ACManagementOperationStatusType.INITIATED,
                              AnswersCatalogOperationType.CUBE_REFRESH);
            getAnswersCatalogManagementQueueService()
                     .createAnswersCatalogManagementQueue(answersCatalogManagementQueue);
            //set mcmqId to context
            cubeRefreshContext.setAnswersCatalogManagementQueueId(answersCatalogManagementQueue.getId());
            uiStatus.setJobRequestId(getCubeRefreshJobService().scheduleCubeRefreshJob(cubeRefreshContext));
         }
      } catch (AnswersCatalogException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      } catch (AnswersCatalogManagementQueueException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
      return uiStatus;
   }

   @Override
   public UIStatus updateCube (Long existingAssetId, ApplicationContext applicationContext) throws HandlerException {
      UIStatus uiStatus = new UIStatus();
      try {
         Long parentAssetId = getAssetById(existingAssetId).getBaseAssetId();
         if (getAnswersCatalogManagementQueueService().isAnswersCatalogManagementQueueUnderProgress(
                  prepareAssetIds(existingAssetId, parentAssetId))) {
            uiStatus.setStatus(false);
            uiStatus.addErrorMessage(ICubeOperationalConstants.ASSET_OR_PARENT_ASSET_ALREADY_UNDER_PROCESS_MESSAGE);
         }
         if (uiStatus.isStatus()) {
            AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo = AnswersCatalogPlatformServiceHelper
                     .prepareAnswersCatalogMaintenanceRequestInfo(applicationContext.getAppId(), applicationContext
                              .getModelId(), parentAssetId, getUserContext().getUser(), existingAssetId, null, null,
                              null, null, null);
            AnswersCatalogUpdationContext cubeUpdationContext = getAnswersCatalogContextBuilderService()
                     .buildCubeUpdationContext(answersCatalogMaintenanceRequestInfo);
            AnswersCatalogManagementQueue answersCatalogManagementQueue = AnswersCatalogPlatformServiceHelper
                     .prepareAnswersCatalogManagementQueue(parentAssetId, existingAssetId, null, ExeCueXMLUtils
                              .getXMLStringFromObject(cubeUpdationContext), null,
                              ACManagementOperationSourceType.USER_REQUEST, ACManagementOperationStatusType.INITIATED,
                              AnswersCatalogOperationType.CUBE_UPDATION);
            getAnswersCatalogManagementQueueService()
                     .createAnswersCatalogManagementQueue(answersCatalogManagementQueue);
            //set mcmqId to context
            cubeUpdationContext.setAnswersCatalogManagementQueueId(answersCatalogManagementQueue.getId());
            uiStatus.setJobRequestId(getCubeUpdationJobService().scheduleCubeUpdationJob(cubeUpdationContext));
         }
      } catch (AnswersCatalogException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      } catch (AnswersCatalogManagementQueueException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
      return uiStatus;
   }

   public Concept getConceptById (Long conceptId) throws HandlerException {
      try {
         return getKdxRetrievalService().getConceptById(conceptId);
      } catch (KDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   public Concept getConceptByBEDId (Long conceptBedId) throws HandlerException {
      try {
         return getKdxRetrievalService().getConceptByBEDId(conceptBedId);
      } catch (KDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<String> getDefaultStats () throws HandlerException {
      List<String> defaultStatNames = new ArrayList<String>();
      try {
         List<Stat> stats = getAnswersCatalogDefaultsService().getDefaultStatsForCubeCreation();
         for (Stat stat : stats) {
            defaultStatNames.add(stat.getDisplayName());
         }
      } catch (AnswersCatalogException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
      return defaultStatNames;
   }

   public List<String> getDefaultDimensions (Long assetId) throws HandlerException {
      try {
         List<Mapping> sourceAssetGrain = getMappingRetrievalService().getAssetGrain(assetId);
         // get the default distribution concept name.
         String defaultDistributionConceptName = null;
         Mapping defaultDistributionConcept = AssetGrainUtils.getDefaultDistributionConcept(sourceAssetGrain);
         if (defaultDistributionConcept != null) {
            defaultDistributionConceptName = defaultDistributionConcept.getBusinessEntityDefinition().getConcept()
                     .getName();
         }
         List<String> defaultDimensionNames = new ArrayList<String>();
         defaultDimensionNames.add(defaultDistributionConceptName);
         return defaultDimensionNames;
      } catch (MappingException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }

   }

   public ColumnType getConceptKDXType (Long modelId, Long conceptId, Long assetId) throws HandlerException {
      ColumnType columnType = null;
      try {
         BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
         businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
         Concept concept = getKdxRetrievalService().getConceptById(conceptId);
         businessEntityTerm.setBusinessEntity(concept);
         List<Mapping> assetEntities = getMappingRetrievalService().getAssetEntities(businessEntityTerm, modelId,
                  assetId);
         if (ExecueCoreUtil.isCollectionNotEmpty(assetEntities)) {
            List<AssetEntityDefinition> assetEntityDefinitions = ExecueBeanUtil
                     .getAssetEntityDefinitions(assetEntities);
            AssetEntityDefinition correctAssetEntityDefinition = getSdxRetrievalService()
                     .pickCorrectAssetEntityDefinition(assetEntityDefinitions);
            columnType = correctAssetEntityDefinition.getColum().getKdxDataType();
         }
      } catch (MappingException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      } catch (KDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      } catch (SDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
      return columnType;
   }

   public int getMappedInstanceCount (Long modelId, Long conceptId, Long assetId) throws HandlerException {
      int mappedInstanceCount = 0;
      try {
         List<Instance> mappedInstances = getMappingRetrievalService().getMappedInstances(modelId, conceptId, assetId);
         if (!CollectionUtils.isEmpty(mappedInstances)) {
            mappedInstanceCount = mappedInstances.size();
         }
      } catch (MappingException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
      return mappedInstanceCount;
   }

   @Override
   public Asset getAssetById (Long assetId) throws HandlerException {
      try {
         return getSdxRetrievalService().getAssetById(assetId);
      } catch (SDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }

   }

   private void adjustRangeIds (UICubeCreation uiCubeCreationContext) {
      // TODO: -RG- When Static Ranges start coming via the screen, this method needs to be enhanced
      /*
       * When the static ranges comes in to the picture, Dynamic ranges should get adjusted to the 
       * configured value in the flow it self rather than in this method
       */
      for (Range range : uiCubeCreationContext.getSelectedRanges()) {
         range.setId(getCoreConfigurationService().getDynamicRangeId());
      }
   }

   private List<String> populateSimpleLookupDiemnsions (List<Concept> selectedConcepts) throws HandlerException {
      // populate the simple lookup dimensions
      Set<String> requestedSimpleLookupDimensions = new HashSet<String>();
      for (Concept concept : selectedConcepts) {
         requestedSimpleLookupDimensions.add(concept.getName());
      }
      return new ArrayList<String>(requestedSimpleLookupDimensions);
   }

   public List<UIAsset> getAssetsByTypeForApplication (Long appId) throws HandlerException {
      try {
         return transformUIAssets(getSdxRetrievalService().getAssetsByTypeForApplication(appId, AssetType.Cube));
      } catch (SDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   public CubeCreationContext getCubeCreationContextByAssetId (Long assetId) throws HandlerException {
      try {
         return getJobDataService().getCubeCreationContextByAssetId(assetId);
      } catch (QueryDataException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   public ICubeCreationJobService getCubeCreationJobService () {
      return cubeCreationJobService;
   }

   public void setCubeCreationJobService (ICubeCreationJobService cubeCreationJobService) {
      this.cubeCreationJobService = cubeCreationJobService;
   }

   public ICubeUpdationJobService getCubeUpdationJobService () {
      return cubeUpdationJobService;
   }

   public void setCubeUpdationJobService (ICubeUpdationJobService cubeUpdationJobService) {
      this.cubeUpdationJobService = cubeUpdationJobService;
   }

   public ICubeRefreshJobService getCubeRefreshJobService () {
      return cubeRefreshJobService;
   }

   public void setCubeRefreshJobService (ICubeRefreshJobService cubeRefreshJobService) {
      this.cubeRefreshJobService = cubeRefreshJobService;
   }

   /**
    * @return the preferencesServiceHandler
    */
   public IPreferencesServiceHandler getPreferencesServiceHandler () {
      return preferencesServiceHandler;
   }

   /**
    * @param preferencesServiceHandler the preferencesServiceHandler to set
    */
   public void setPreferencesServiceHandler (IPreferencesServiceHandler preferencesServiceHandler) {
      this.preferencesServiceHandler = preferencesServiceHandler;
   }

   /**
    * @return the rangeSuggestionService
    */
   public IRangeSuggestionService getRangeSuggestionService () {
      return rangeSuggestionService;
   }

   /**
    * @param rangeSuggestionService the rangeSuggestionService to set
    */
   public void setRangeSuggestionService (IRangeSuggestionService rangeSuggestionService) {
      this.rangeSuggestionService = rangeSuggestionService;
   }

}
