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
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.service.ICubeOperationalConstants;
import com.execue.core.common.bean.ApplicationContext;
import com.execue.core.common.bean.ac.AnswersCatalogMaintenanceRequestInfo;
import com.execue.core.common.bean.ac.AnswersCatalogManagementQueue;
import com.execue.core.common.bean.ac.AnswersCatalogUpdationContext;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.type.ACManagementOperationSourceType;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.core.common.type.AnswersCatalogOperationType;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.util.AssetGrainUtils;
import com.execue.core.common.util.ExeCueXMLUtils;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIAsset;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.bean.UIStatus;
import com.execue.handler.swi.IMartManagementServiceHandler;
import com.execue.platform.helper.AnswersCatalogPlatformServiceHelper;
import com.execue.qdata.exception.AnswersCatalogManagementQueueException;
import com.execue.qdata.exception.QueryDataException;
import com.execue.scheduler.service.IDataMartCreationJobService;
import com.execue.scheduler.service.IDataMartRefreshJobService;
import com.execue.scheduler.service.IDataMartUpdationJobService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;

public class MartManagementServiceHandlerImpl extends AbstractAnswerCatalogManagementServiceHandler implements
         IMartManagementServiceHandler {

   private static Logger               logger = Logger.getLogger(MartManagementServiceHandlerImpl.class);
   private IDataMartCreationJobService dataMartCreationJobService;
   private IDataMartRefreshJobService  dataMartRefreshJobService;
   private IDataMartUpdationJobService dataMartUpdationJobService;

   public UIStatus updateMart (ApplicationContext applicationContext, Long existingAssetId) throws HandlerException {
      try {
         UIStatus uiStatus = new UIStatus();
         Long parentAssetId = getSdxRetrievalService().getAssetById(existingAssetId).getBaseAssetId();
         if (getAnswersCatalogManagementQueueService().isAnswersCatalogManagementQueueUnderProgress(
                  prepareAssetIds(existingAssetId, parentAssetId))) {
            uiStatus.setStatus(false);
            uiStatus.addErrorMessage(ICubeOperationalConstants.ASSET_OR_PARENT_ASSET_ALREADY_UNDER_PROCESS_MESSAGE);
         }
         if (uiStatus.isStatus()) {
            AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo = AnswersCatalogPlatformServiceHelper
                     .prepareAnswersCatalogMaintenanceRequestInfo(applicationContext.getAppId(), applicationContext
                              .getModelId(), parentAssetId, getUserContext().getUser(), existingAssetId, null,
                              null, null, null, null);
            AnswersCatalogUpdationContext martUpdationContext = getAnswersCatalogContextBuilderService()
                     .buildMartUpdationContext(answersCatalogMaintenanceRequestInfo);

            AnswersCatalogManagementQueue answersCatalogManagementQueue = AnswersCatalogPlatformServiceHelper
                     .prepareAnswersCatalogManagementQueue(parentAssetId, existingAssetId, null, ExeCueXMLUtils
                              .getXMLStringFromObject(martUpdationContext), null,
                              ACManagementOperationSourceType.USER_REQUEST, ACManagementOperationStatusType.INITIATED,
                              AnswersCatalogOperationType.MART_UPDATION);
            getAnswersCatalogManagementQueueService()
                     .createAnswersCatalogManagementQueue(answersCatalogManagementQueue);
            martUpdationContext.setAnswersCatalogManagementQueueId(answersCatalogManagementQueue.getId());
            uiStatus.setJobRequestId(getDataMartUpdationJobService().scheduleMartUpdationJob(martUpdationContext));
         }
         return uiStatus;
      } catch (AnswersCatalogException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      } catch (AnswersCatalogManagementQueueException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      } catch (SDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   public UIStatus refreshMart (ApplicationContext applicationContext, Long existingAssetId) throws HandlerException {
      try {
         UIStatus uiStatus = new UIStatus();
         Long parentAssetId = getSdxRetrievalService().getAssetById(existingAssetId).getBaseAssetId();
         if (getAnswersCatalogManagementQueueService().isAnswersCatalogManagementQueueUnderProgress(
                  prepareAssetIds(existingAssetId, parentAssetId))) {
            uiStatus.setStatus(false);
            uiStatus.addErrorMessage(ICubeOperationalConstants.ASSET_OR_PARENT_ASSET_ALREADY_UNDER_PROCESS_MESSAGE);
         }
         if (uiStatus.isStatus()) {
            AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo = AnswersCatalogPlatformServiceHelper
                     .prepareAnswersCatalogMaintenanceRequestInfo(applicationContext.getAppId(), applicationContext
                              .getModelId(), parentAssetId, getUserContext().getUser(), existingAssetId, null,
                              null, null, null, null);
            AnswersCatalogUpdationContext martRefreshContext = getAnswersCatalogContextBuilderService()
                     .buildMartRefreshContext(answersCatalogMaintenanceRequestInfo);

            AnswersCatalogManagementQueue answersCatalogManagementQueue = AnswersCatalogPlatformServiceHelper
                     .prepareAnswersCatalogManagementQueue(parentAssetId, existingAssetId, null, ExeCueXMLUtils
                              .getXMLStringFromObject(martRefreshContext), null,
                              ACManagementOperationSourceType.USER_REQUEST, ACManagementOperationStatusType.INITIATED,
                              AnswersCatalogOperationType.MART_REFRESH);
            getAnswersCatalogManagementQueueService()
                     .createAnswersCatalogManagementQueue(answersCatalogManagementQueue);
            martRefreshContext.setAnswersCatalogManagementQueueId(answersCatalogManagementQueue.getId());
            uiStatus.setJobRequestId(getDataMartRefreshJobService().scheduleMartRefreshJob(martRefreshContext));
         }
         return uiStatus;
      } catch (AnswersCatalogException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      } catch (AnswersCatalogManagementQueueException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      } catch (SDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   public UIStatus createMart (ApplicationContext applicationContext, Asset targetAsset,
            List<UIConcept> prominentMeasures, List<UIConcept> prominentDimensions) throws HandlerException {

      try {
         UIStatus uiStatus = new UIStatus();
         Long parentAssetId = applicationContext.getAssetId();
         if (getAnswersCatalogManagementQueueService().isAnswersCatalogManagementQueueUnderProgress(parentAssetId)) {
            uiStatus.setStatus(false);
            uiStatus.addErrorMessage(ICubeOperationalConstants.ASSET_ALREADY_UNDER_PROCESS_MESSAGE);
         }
         if (uiStatus.isStatus()) {
            AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo = AnswersCatalogPlatformServiceHelper
                     .prepareAnswersCatalogMaintenanceRequestInfo(applicationContext.getAppId(), applicationContext
                              .getModelId(), parentAssetId, getUserContext().getUser(), null, null, null,
                              getConceptNames(prominentDimensions), getConceptNames(prominentMeasures), targetAsset);
            MartCreationContext martCreationContext = getAnswersCatalogContextBuilderService()
                     .buildMartCreationContext(answersCatalogMaintenanceRequestInfo);
            // populate ACMQ context and create an entry in ACMQ
            AnswersCatalogManagementQueue answersCatalogManagementQueue = AnswersCatalogPlatformServiceHelper
                     .prepareAnswersCatalogManagementQueue(parentAssetId, null, null, ExeCueXMLUtils
                              .getXMLStringFromObject(martCreationContext), null,
                              ACManagementOperationSourceType.USER_REQUEST, ACManagementOperationStatusType.INITIATED,
                              AnswersCatalogOperationType.MART_CREATION);
            getAnswersCatalogManagementQueueService()
                     .createAnswersCatalogManagementQueue(answersCatalogManagementQueue);
            martCreationContext.setAnswersCatalogManagementQueueId(answersCatalogManagementQueue.getId());
            uiStatus.setJobRequestId(getDataMartCreationJobService().scheduleMartCreationJob(martCreationContext));
         }
         return uiStatus;
      } catch (AnswersCatalogException answersCatalogException) {
         logger.error(answersCatalogException);
         throw new HandlerException(answersCatalogException.getCode(), answersCatalogException);
      } catch (AnswersCatalogManagementQueueException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }

   }

   private List<String> getConceptNames (List<UIConcept> uiConcepts) {
      List<String> conceptNames = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionNotEmpty(uiConcepts)) {
         for (UIConcept uiConcept : uiConcepts) {
            conceptNames.add(uiConcept.getName());
         }
      }
      return conceptNames;
   }

   public List<UIConcept> getEligibleProminentDimensions (Long modelId, Long assetId) throws HandlerException {
      try {
         List<Concept> mappedDimensionConcepts = getKdxRetrievalService().getMappedConceptsForParticularBehaviour(
                  modelId, assetId, BehaviorType.ENUMERATION);
         //TODO-JT- removed population and distribution from eligible Dimensions so that user not able to select any such dimensions, will revisit it because we are doing the same during mart context creation
         if (ExecueCoreUtil.isCollectionNotEmpty(mappedDimensionConcepts)) {
            List<Mapping> mappings = getMappingRetrievalService().getAssetGrain(assetId);
            Concept population = getPopulation(mappings);
            List<Concept> distributions = getDistributions(mappings);
            if (ExecueCoreUtil.isCollectionNotEmpty(distributions)) {
               mappedDimensionConcepts.removeAll(distributions);
            }
            if (population != null) {
               mappedDimensionConcepts.remove(population);
            }
         }
         return tranformUIConcept(mappedDimensionConcepts);
      } catch (KDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);

      } catch (MappingException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   private List<Concept> getDistributions (List<Mapping> mappings) {
      List<Concept> distributionConcepts = new ArrayList<Concept>();
      List<Mapping> distributionConceptMappings = AssetGrainUtils.getAllDistributionConcepts(mappings);
      if (ExecueCoreUtil.isCollectionNotEmpty(distributionConceptMappings)) {
         for (Mapping mapping : distributionConceptMappings) {
            distributionConcepts.add(mapping.getBusinessEntityDefinition().getConcept());
         }
      }
      return distributionConcepts;
   }

   private Concept getPopulation (List<Mapping> mappings) {
      Concept populationConcept = null;
      Mapping defaultPopulationConceptMapping = AssetGrainUtils.getDefaultPopulationConcept(mappings);
      if (defaultPopulationConceptMapping != null) {
         populationConcept = defaultPopulationConceptMapping.getBusinessEntityDefinition().getConcept();
      } else {
         List<Mapping> populationConcepts = AssetGrainUtils.getPopulationConcepts(mappings);
         if (ExecueCoreUtil.isCollectionNotEmpty(populationConcepts)) {
            populationConcept = populationConcepts.get(0).getBusinessEntityDefinition().getConcept();
         }
      }
      return populationConcept;
   }

   public List<UIConcept> getEligibleProminentMeasures (Long modelId, Long assetId) throws HandlerException {
      try {
         List<Concept> mappedMeasureConcepts = getKdxRetrievalService().getMappedConceptsForParticularType(modelId,
                  assetId, ExecueConstants.MEASURABLE_ENTITY_TYPE);
         return tranformUIConcept(mappedMeasureConcepts);
      } catch (KDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<Asset> getAllParentAssets (Long applicationId) throws HandlerException {
      try {
         return getSdxRetrievalService().getAllParentAssets(applicationId);
      } catch (SDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<UIAsset> getMartsForApplication (Long appId) throws HandlerException {
      try {
         return transformUIAssets(getSdxRetrievalService().getAssetsByTypeForApplication(appId, AssetType.Mart));
      } catch (SDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   @Override
   public MartCreationContext getMartCreationContextByAssetId (Long existingAssetId) throws HandlerException {
      try {
         return getJobDataService().getMartCreationContextByAssetId(existingAssetId);
      } catch (QueryDataException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   private List<UIConcept> tranformUIConcept (List<Concept> concepts) {
      List<UIConcept> uiConcepts = new ArrayList<UIConcept>();
      if (ExecueCoreUtil.isCollectionNotEmpty(concepts)) {
         for (Concept concept : concepts) {
            UIConcept uiConcept = new UIConcept();
            uiConcept.setId(concept.getId());
            uiConcept.setName(concept.getName());
            uiConcept.setDisplayName(concept.getDisplayName());
            uiConcepts.add(uiConcept);
         }
      }
      return uiConcepts;
   }

   public IDataMartCreationJobService getDataMartCreationJobService () {
      return dataMartCreationJobService;
   }

   public void setDataMartCreationJobService (IDataMartCreationJobService dataMartCreationJobService) {
      this.dataMartCreationJobService = dataMartCreationJobService;
   }

   public IDataMartRefreshJobService getDataMartRefreshJobService () {
      return dataMartRefreshJobService;
   }

   public void setDataMartRefreshJobService (IDataMartRefreshJobService dataMartRefreshJobService) {
      this.dataMartRefreshJobService = dataMartRefreshJobService;
   }

   public IDataMartUpdationJobService getDataMartUpdationJobService () {
      return dataMartUpdationJobService;
   }

   public void setDataMartUpdationJobService (IDataMartUpdationJobService dataMartUpdationJobService) {
      this.dataMartUpdationJobService = dataMartUpdationJobService;
   }

}
