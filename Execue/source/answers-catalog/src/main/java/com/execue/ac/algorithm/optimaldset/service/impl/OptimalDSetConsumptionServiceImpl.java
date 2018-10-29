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
package com.execue.ac.algorithm.optimaldset.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetCandidateDimensionPattern;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetCubeOutputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetDimensionInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetMartOutputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetMeasureInfo;
import com.execue.ac.algorithm.optimaldset.helper.OptimalDSetHelper;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetConsumptionService;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.service.IAnswersCatalogContextBuilderService;
import com.execue.core.common.bean.ac.AnswersCatalogContextEntityInfo;
import com.execue.core.common.bean.ac.AnswersCatalogMaintenanceRequestInfo;
import com.execue.core.common.bean.ac.AnswersCatalogManagementQueue;
import com.execue.core.common.bean.ac.CubeCreationContext;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.AnswersCatalogContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.bean.swi.AssetDeletionContext;
import com.execue.core.common.type.ACManagementOperationSourceType;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.core.common.type.AnswersCatalogOperationType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExeCueXMLUtils;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.helper.AnswersCatalogPlatformServiceHelper;
import com.execue.qdata.configuration.IQueryDataConfigurationService;
import com.execue.qdata.exception.AnswersCatalogManagementQueueException;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IAnswersCatalogManagementQueueService;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IUserManagementService;
import com.thoughtworks.xstream.XStream;

/**
 * @author Nitesh
 */
public class OptimalDSetConsumptionServiceImpl implements IOptimalDSetConsumptionService {

   private static final Logger                   logger = Logger.getLogger(OptimalDSetConsumptionServiceImpl.class);

   private ICoreConfigurationService             coreConfigurationService;
   private IQueryDataConfigurationService        queryDataConfigurationService;
   private IJobDataService                       jobDataService;
   private IAnswersCatalogContextBuilderService  answersCatalogContextBuilderService;
   private IApplicationRetrievalService          applicationRetrievalService;
   private IAnswersCatalogManagementQueueService answersCatalogManagementQueueService;
   private OptimalDSetHelper                     optimalDSetHelper;
   private IUserManagementService                userManagementService;

   @Override
   public void processOptimalDSetCubeConsumption (OptimalDSetCubeOutputInfo cubeOptimalDsetOutput, Long parentAssetId,
            Long modelId) throws AnswersCatalogException {

      List<OptimalDSetCandidateDimensionPattern> levelPatterns = cubeOptimalDsetOutput.getLevelPatterns();

      if (ExecueCoreUtil.isCollectionEmpty(levelPatterns)) {
         if (logger.isDebugEnabled()) {
            logger.debug("No optimal DSet found for consumption");
         }
         return;
      }

      // Prepare the ac context entity info map for the existing cubes
      Map<AnswersCatalogContextEntityInfo, Long> answersCatalogContextEntityInfoByAssetId = new HashMap<AnswersCatalogContextEntityInfo, Long>();
      try {
         List<AnswersCatalogContext> answersCatalogContextByParentAssetIdForCubes = getJobDataService()
                  .getAnswersCatalogContextByParentAssetIdForCubes(parentAssetId);
         if (ExecueCoreUtil.isCollectionNotEmpty(answersCatalogContextByParentAssetIdForCubes)) {
            for (AnswersCatalogContext answersCatalogContext : answersCatalogContextByParentAssetIdForCubes) {
               AnswersCatalogContextEntityInfo answersCatalogContextEntityInfo = prepareAnswersCatalogContextEntityInfo(answersCatalogContext);
               answersCatalogContextEntityInfoByAssetId.put(answersCatalogContextEntityInfo, answersCatalogContext
                        .getAssetId());
            }
         }
      } catch (QueryDataException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      }

      // Prepare the dimension sets to be created list by checking if they are not already exists. If they already
      // exists then maintain it in the ac context list to be retained
      List<OptimalDSetCandidateDimensionPattern> dimensionSetsToBeCreated = new ArrayList<OptimalDSetCandidateDimensionPattern>();
      Set<AnswersCatalogContextEntityInfo> answersCatalogContextToBeRetained = new HashSet<AnswersCatalogContextEntityInfo>();
      for (OptimalDSetCandidateDimensionPattern optimalDSetCandidateDimensionPattern : levelPatterns) {
         List<OptimalDSetDimensionInfo> dimensions = optimalDSetCandidateDimensionPattern.getDimensions();
         AnswersCatalogContextEntityInfo answersCatalogContextEntityInfo = prepareAnswersCatalogContextEntityInfo(dimensions);
         Long existingAssetId = answersCatalogContextEntityInfoByAssetId.get(answersCatalogContextEntityInfo);
         if (existingAssetId == null) {
            dimensionSetsToBeCreated.add(optimalDSetCandidateDimensionPattern);
         } else {
            answersCatalogContextToBeRetained.add(answersCatalogContextEntityInfo);
         }
      }

      // Remove the answers catalog context entries from the map which are to be retained
      for (AnswersCatalogContextEntityInfo answersCatalogContextEntityInfo : answersCatalogContextToBeRetained) {
         answersCatalogContextEntityInfoByAssetId.remove(answersCatalogContextEntityInfo);
      }

      try {
         Long applicationId = getApplicationRetrievalService().getApplicationByModelId(modelId).getId();
         List<Long> acmqIds = new ArrayList<Long>();
         // Prepare the AnswersCatalogManagementQueue from the dimensionSetsToBeCreated list. This should be mark for
         // creation.
         if (ExecueCoreUtil.isCollectionNotEmpty(dimensionSetsToBeCreated)) {
            List<AnswersCatalogManagementQueue> cubeCreationAnswersCatalogManagementQueues = prepareCubeCreationAnswersCatalogManagementQueues(
                     dimensionSetsToBeCreated, applicationId, modelId, parentAssetId);
            for (AnswersCatalogManagementQueue answersCatalogManagementQueue : cubeCreationAnswersCatalogManagementQueues) {
               getAnswersCatalogManagementQueueService().createAnswersCatalogManagementQueue(
                        answersCatalogManagementQueue);
               acmqIds.add(answersCatalogManagementQueue.getId());
            }
         }

         // Get the context Ids for the remaining entries in the map. This should be mark for deletion
         Collection<Long> existingAssetIdToBeDeleted = answersCatalogContextEntityInfoByAssetId.values();
         if (ExecueCoreUtil.isCollectionNotEmpty(existingAssetIdToBeDeleted)) {
            // Get the comma separated dependent management Ids 
            String dependantManagementIds = ExecueCoreUtil.joinLongCollection(acmqIds,
                     getQueryDataConfigurationService().getSqlConcatDelimeter());
            List<AnswersCatalogManagementQueue> cubeDeletionAnswersCatalogManagementQueues = prepareCubeDeletionAnswersCatalogManagementQueues(
                     existingAssetIdToBeDeleted, applicationId, modelId, parentAssetId, dependantManagementIds);
            getAnswersCatalogManagementQueueService().createAnswersCatalogManagementQueue(
                     cubeDeletionAnswersCatalogManagementQueues);

         }
      } catch (AnswersCatalogManagementQueueException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      } catch (KDXException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      } catch (SWIException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      }
   }

   private AnswersCatalogContextEntityInfo prepareAnswersCatalogContextEntityInfo (
            List<OptimalDSetDimensionInfo> dimensions) {

      if (ExecueCoreUtil.isCollectionEmpty(dimensions)) {
         return null;
      }

      TreeSet<String> requestedDimensions = new TreeSet<String>(getOptimalDSetHelper()
               .getDimensionBEDIDsForContextComparision(dimensions));

      AnswersCatalogContextEntityInfo answersCatalogContextEntityInfo = new AnswersCatalogContextEntityInfo();
      answersCatalogContextEntityInfo.setRequestedDimensions(requestedDimensions);
      return answersCatalogContextEntityInfo;
   }

   private AnswersCatalogContextEntityInfo prepareAnswersCatalogContextEntityInfo (
            AnswersCatalogContext answersCatalogContext) {

      String contextData = answersCatalogContext.getContextData();
      if (ExecueCoreUtil.isEmpty(contextData)) {
         return null;
      }

      AnswersCatalogContextEntityInfo answersCatalogContextEntityInfo = new AnswersCatalogContextEntityInfo();
      CubeCreationContext cubeCreationContext = (CubeCreationContext) ExeCueXMLUtils
               .getObjectFromXMLString(contextData);
      List<String> dimensionBEDIDsForContextComparision = getOptimalDSetHelper()
               .getDimensionBEDIDsForContextComparision(cubeCreationContext);
      answersCatalogContextEntityInfo.setRequestedDimensions(new TreeSet<String>(dimensionBEDIDsForContextComparision));

      return answersCatalogContextEntityInfo;
   }

   private List<AnswersCatalogManagementQueue> prepareCubeDeletionAnswersCatalogManagementQueues (
            Collection<Long> existingAssetIdToBeDeleted, Long applicationId, Long modelId, Long parentAssetId,
            String dependentManagementIds) {
      List<AnswersCatalogManagementQueue> answersCatalogManagementQueues = new ArrayList<AnswersCatalogManagementQueue>();

      if (ExecueCoreUtil.isCollectionEmpty(existingAssetIdToBeDeleted)) {
         return answersCatalogManagementQueues;
      }

      String remarks = null; // TODO: NK: What should be the initial remarks for deletion operation??
      ACManagementOperationSourceType operationSourceType = ACManagementOperationSourceType.OPTIMAL_DSET;
      ACManagementOperationStatusType operationStatusType = ACManagementOperationStatusType.PENDING;
      AnswersCatalogOperationType operationType = AnswersCatalogOperationType.CUBE_DELETION;
      for (Long existingAssetId : existingAssetIdToBeDeleted) {
         AssetDeletionContext assetDeletionContext = new AssetDeletionContext();
         assetDeletionContext.setAssetId(existingAssetId);
         assetDeletionContext.setUserId(getCoreConfigurationService().getAdminUserId());
         XStream xStream = new XStream();
         String deleteOperationContext = xStream.toXML(assetDeletionContext);

         AnswersCatalogManagementQueue answersCatalogManagementQueue = AnswersCatalogPlatformServiceHelper
                  .prepareAnswersCatalogManagementQueue(parentAssetId, existingAssetId, dependentManagementIds,
                           deleteOperationContext, remarks, operationSourceType, operationStatusType, operationType);
         answersCatalogManagementQueues.add(answersCatalogManagementQueue);
      }

      return answersCatalogManagementQueues;
   }

   private List<AnswersCatalogManagementQueue> prepareCubeCreationAnswersCatalogManagementQueues (
            List<OptimalDSetCandidateDimensionPattern> dimensionSets, Long applicationId, Long modelId,
            Long parentAssetId) throws AnswersCatalogException, SWIException {
      List<AnswersCatalogManagementQueue> answersCatalogManagementQueues = new ArrayList<AnswersCatalogManagementQueue>();
      if (ExecueCoreUtil.isCollectionEmpty(dimensionSets)) {
         return answersCatalogManagementQueues;
      }

      Long userId = getCoreConfigurationService().getAdminUserId();
      User user = getUserManagementService().getUserById(userId);
      String remarks = null; // TODO: NK: What should be the initial remarks for creation operation??
      ACManagementOperationSourceType operationSourceType = ACManagementOperationSourceType.OPTIMAL_DSET;
      ACManagementOperationStatusType operationStatusType = ACManagementOperationStatusType.PENDING;
      AnswersCatalogOperationType operationType = AnswersCatalogOperationType.CUBE_CREATION;
      for (OptimalDSetCandidateDimensionPattern optimalDSetCandidateDimensionPattern : dimensionSets) {
         List<OptimalDSetDimensionInfo> dimensions = optimalDSetCandidateDimensionPattern.getDimensions();
         List<Range> selectedRangeLookups = getRangeLookups(dimensions);
         List<String> selectedSimpleLookups = getSimpleLookups(dimensions);
         Asset targetAsset = getTargetAsset(dimensions);

         AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo = AnswersCatalogPlatformServiceHelper
                  .prepareAnswersCatalogMaintenanceRequestInfo(applicationId, modelId, parentAssetId, user, null,
                           selectedRangeLookups, selectedSimpleLookups, null, null, targetAsset);

         CubeCreationContext cubeCreationContext = getAnswersCatalogContextBuilderService().buildCubeCreationContext(
                  answersCatalogMaintenanceRequestInfo);

         XStream xStream = new XStream();
         String operationContext = xStream.toXML(cubeCreationContext);

         AnswersCatalogManagementQueue answersCatalogManagementQueue = AnswersCatalogPlatformServiceHelper
                  .prepareAnswersCatalogManagementQueue(parentAssetId, null, null, operationContext, remarks,
                           operationSourceType, operationStatusType, operationType);
         answersCatalogManagementQueues.add(answersCatalogManagementQueue);
      }

      return answersCatalogManagementQueues;
   }

   private List<String> getSimpleLookups (List<OptimalDSetDimensionInfo> dimensions) {
      List<String> simpleLookups = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionEmpty(dimensions)) {
         return simpleLookups;
      }
      for (OptimalDSetDimensionInfo optimalDSetDimensionInfo : dimensions) {
         // Skip the ranges
         if (optimalDSetDimensionInfo.getRange() != null) {
            continue;
         }
         simpleLookups.add(optimalDSetDimensionInfo.getName());
      }
      return simpleLookups;
   }

   private List<Range> getRangeLookups (List<OptimalDSetDimensionInfo> dimensions) {
      List<Range> rangeLookups = new ArrayList<Range>();
      if (ExecueCoreUtil.isCollectionEmpty(dimensions)) {
         return rangeLookups;
      }
      for (OptimalDSetDimensionInfo optimalDSetDimensionInfo : dimensions) {
         // Skip the dimensions
         if (optimalDSetDimensionInfo.getRange() == null) {
            continue;
         }
         rangeLookups.add(optimalDSetDimensionInfo.getRange());
      }
      return rangeLookups;
   }

   private Asset getTargetAsset (List<OptimalDSetDimensionInfo> optimalDSetDimensions) {
      Set<String> dimensionNames = new TreeSet<String>();
      for (OptimalDSetDimensionInfo optimalDSetDimension : optimalDSetDimensions) {
         dimensionNames.add(optimalDSetDimension.getName());
      }

      String targetAssetName = ExecueCoreUtil.joinCollection(dimensionNames);
      Asset targetAsset = new Asset();
      targetAsset.setName(targetAssetName);
      targetAsset.setDisplayName(targetAssetName);
      targetAsset.setDescription(targetAssetName);
      return targetAsset;
   }

   @Override
   public void processOptimalDSetMartConsumption (OptimalDSetMartOutputInfo martOptimalDsetOutput, Long parentAssetId,
            Long modelId) throws AnswersCatalogException {
      Map<AnswersCatalogContextEntityInfo, Long> answersCatalogContextEntityInfoByAssetIdMap = new HashMap<AnswersCatalogContextEntityInfo, Long>();
      try {
         List<AnswersCatalogContext> answersCatalogContextByParentAssetIdForMarts = getJobDataService()
                  .getAnswersCatalogContextByParentAssetIdForMarts(parentAssetId);
         if (ExecueCoreUtil.isCollectionNotEmpty(answersCatalogContextByParentAssetIdForMarts)) {
            for (AnswersCatalogContext answersCatalogContext : answersCatalogContextByParentAssetIdForMarts) {
               AnswersCatalogContextEntityInfo answersCatalogContextEntityInfo = prepareAnswersCatalogContextEntityInfoForMart(answersCatalogContext);
               answersCatalogContextEntityInfoByAssetIdMap.put(answersCatalogContextEntityInfo, answersCatalogContext
                        .getAssetId());
            }
         }

         AnswersCatalogContextEntityInfo acEntityInfoFromOptimalDsetOutput = prepareAnswersCatalogContextEntityInfoForMart(
                  martOptimalDsetOutput.getOutputDimensions(), martOptimalDsetOutput.getOutputMeasures());
         Long existingAssetId = answersCatalogContextEntityInfoByAssetIdMap.get(acEntityInfoFromOptimalDsetOutput);
         Long applicationId = getApplicationRetrievalService().getApplicationByModelId(modelId).getId();
         List<Long> acmqIds = new ArrayList<Long>();

         if (existingAssetId != null) {
            // Remove from map to retain this entry.
            answersCatalogContextEntityInfoByAssetIdMap.remove(acEntityInfoFromOptimalDsetOutput);
         } else {
            // create a new entry in management queue.
            List<OptimalDSetDimensionInfo> dimensions = martOptimalDsetOutput.getOutputDimensions();
            List<OptimalDSetMeasureInfo> measures = martOptimalDsetOutput.getOutputMeasures();
            AnswersCatalogManagementQueue answersCatalogManagementQueue = prepareMartCreationAnswersCatalogManagementQueue(
                     applicationId, modelId, parentAssetId, dimensions, measures);
            getAnswersCatalogManagementQueueService()
                     .createAnswersCatalogManagementQueue(answersCatalogManagementQueue);
            acmqIds.add(answersCatalogManagementQueue.getId());
         }

         // Mark for the deletion.
         Collection<Long> existingAssetIdToBeDeleted = answersCatalogContextEntityInfoByAssetIdMap.values();
         if (ExecueCoreUtil.isCollectionNotEmpty(existingAssetIdToBeDeleted)) {
            String dependentManagementIds = ExecueCoreUtil.joinLongCollection(acmqIds,
                     getQueryDataConfigurationService().getSqlConcatDelimeter());
            List<AnswersCatalogManagementQueue> martDeletionAnswersCatalogManagementQueues = prepareMartDeletionAnswersCatalogManagementQueues(
                     existingAssetIdToBeDeleted, applicationId, modelId, parentAssetId, dependentManagementIds);
            getAnswersCatalogManagementQueueService().createAnswersCatalogManagementQueue(
                     martDeletionAnswersCatalogManagementQueues);
         }
      } catch (QueryDataException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      } catch (AnswersCatalogException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      } catch (KDXException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      } catch (SWIException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      } catch (AnswersCatalogManagementQueueException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      }
   }

   private AnswersCatalogManagementQueue prepareMartCreationAnswersCatalogManagementQueue (Long applicationId,
            Long modelId, Long parentAssetId, List<OptimalDSetDimensionInfo> dimensions,
            List<OptimalDSetMeasureInfo> measures) throws AnswersCatalogException, SWIException {
      Long userId = getCoreConfigurationService().getAdminUserId();
      User user = getUserManagementService().getUserById(userId);
      String remarks = null;
      ACManagementOperationSourceType operationSourceType = ACManagementOperationSourceType.OPTIMAL_DSET;
      ACManagementOperationStatusType operationStatusType = ACManagementOperationStatusType.PENDING;
      AnswersCatalogOperationType operationType = AnswersCatalogOperationType.MART_CREATION;
      Asset targetAsset = getTargetAsset(dimensions);
      List<String> prominentDimensions = new ArrayList<String>();
      for (OptimalDSetDimensionInfo dimension : dimensions) {
         prominentDimensions.add(dimension.getName());
      }
      List<String> prominentMeasures = new ArrayList<String>();
      for (OptimalDSetMeasureInfo measure : measures) {
         prominentMeasures.add(measure.getName());
      }
      AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo = AnswersCatalogPlatformServiceHelper
               .prepareAnswersCatalogMaintenanceRequestInfo(applicationId, modelId, parentAssetId, user, null, null,
                        null, prominentDimensions, prominentMeasures, targetAsset);
      MartCreationContext martCreationContext = getAnswersCatalogContextBuilderService().buildMartCreationContext(
               answersCatalogMaintenanceRequestInfo);
      XStream xStream = new XStream();
      String operationContext = xStream.toXML(martCreationContext);
      return AnswersCatalogPlatformServiceHelper.prepareAnswersCatalogManagementQueue(parentAssetId, null, null,
               operationContext, remarks, operationSourceType, operationStatusType, operationType);
   }

   private List<AnswersCatalogManagementQueue> prepareMartDeletionAnswersCatalogManagementQueues (
            Collection<Long> existingAssetIdToBeDeleted, Long applicationId, Long modelId, Long parentAssetId,
            String dependentManagementId) {
      List<AnswersCatalogManagementQueue> answersCatalogManagementQueues = new ArrayList<AnswersCatalogManagementQueue>();
      if (ExecueCoreUtil.isCollectionEmpty(existingAssetIdToBeDeleted)) {
         return answersCatalogManagementQueues;
      }
      String remarks = null; // TODO: NK: What should be the initial remarks for deletion operation??
      ACManagementOperationSourceType operationSourceType = ACManagementOperationSourceType.OPTIMAL_DSET;
      ACManagementOperationStatusType operationStatusType = ACManagementOperationStatusType.PENDING;
      AnswersCatalogOperationType operationType = AnswersCatalogOperationType.MART_DELETION;
      for (Long existingAssetId : existingAssetIdToBeDeleted) {
         AssetDeletionContext assetDeletionContext = new AssetDeletionContext();
         assetDeletionContext.setAssetId(existingAssetId);
         assetDeletionContext.setUserId(getCoreConfigurationService().getAdminUserId());
         XStream xStream = new XStream();
         String deleteOperationContext = xStream.toXML(assetDeletionContext);
         AnswersCatalogManagementQueue answersCatalogManagementQueue = AnswersCatalogPlatformServiceHelper
                  .prepareAnswersCatalogManagementQueue(parentAssetId, existingAssetId, dependentManagementId,
                           deleteOperationContext, remarks, operationSourceType, operationStatusType, operationType);
         answersCatalogManagementQueues.add(answersCatalogManagementQueue);
      }
      return answersCatalogManagementQueues;
   }

   private AnswersCatalogContextEntityInfo prepareAnswersCatalogContextEntityInfoForMart (
            AnswersCatalogContext answersCatalogContext) {
      AnswersCatalogContextEntityInfo answersCatalogContextEntityInfo = new AnswersCatalogContextEntityInfo();
      String contextData = answersCatalogContext.getContextData();
      MartCreationContext martCreationContext = (MartCreationContext) ExeCueXMLUtils
               .getObjectFromXMLString(contextData);
      TreeSet<String> requestedDimensions = getOptimalDSetHelper().getBusinessEntitiesForContextComparision(
               martCreationContext.getProminentDimensionBEDIDs());
      TreeSet<String> requestedMeasures = getOptimalDSetHelper().getBusinessEntitiesForContextComparision(
               martCreationContext.getProminentMeasureBEDIDs());
      answersCatalogContextEntityInfo.setRequestedDimensions(requestedDimensions);
      answersCatalogContextEntityInfo.setRequestedMeasures(requestedMeasures);
      return answersCatalogContextEntityInfo;
   }

   private AnswersCatalogContextEntityInfo prepareAnswersCatalogContextEntityInfoForMart (
            List<OptimalDSetDimensionInfo> dimensions, List<OptimalDSetMeasureInfo> measures) {
      AnswersCatalogContextEntityInfo answersCatalogContextEntityInfo = new AnswersCatalogContextEntityInfo();
      List<Long> dimensionsBedIds = new ArrayList<Long>();
      for (OptimalDSetDimensionInfo dimension : dimensions) {
         dimensionsBedIds.add(dimension.getBedId());
      }
      List<Long> measureBedIds = new ArrayList<Long>();
      for (OptimalDSetMeasureInfo measure : measures) {
         measureBedIds.add(measure.getBedId());
      }
      answersCatalogContextEntityInfo.setRequestedDimensions(getOptimalDSetHelper()
               .getBusinessEntitiesForContextComparision(dimensionsBedIds));
      answersCatalogContextEntityInfo.setRequestedMeasures(getOptimalDSetHelper()
               .getBusinessEntitiesForContextComparision(measureBedIds));
      return answersCatalogContextEntityInfo;
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

   /**
    * @return the answersCatalogContextBuilderService
    */
   public IAnswersCatalogContextBuilderService getAnswersCatalogContextBuilderService () {
      return answersCatalogContextBuilderService;
   }

   /**
    * @param answersCatalogContextBuilderService
    *           the answersCatalogContextBuilderService to set
    */
   public void setAnswersCatalogContextBuilderService (
            IAnswersCatalogContextBuilderService answersCatalogContextBuilderService) {
      this.answersCatalogContextBuilderService = answersCatalogContextBuilderService;
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

   /**
    * @return the applicationRetrievalService
    */
   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   /**
    * @param applicationRetrievalService
    *           the applicationRetrievalService to set
    */
   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

   /**
    * @return the answersCatalogManagementQueueService
    */
   public IAnswersCatalogManagementQueueService getAnswersCatalogManagementQueueService () {
      return answersCatalogManagementQueueService;
   }

   /**
    * @param answersCatalogManagementQueueService
    *           the answersCatalogManagementQueueService to set
    */
   public void setAnswersCatalogManagementQueueService (
            IAnswersCatalogManagementQueueService answersCatalogManagementQueueService) {
      this.answersCatalogManagementQueueService = answersCatalogManagementQueueService;
   }

   /**
    * @return the queryDataConfigurationService
    */
   public IQueryDataConfigurationService getQueryDataConfigurationService () {
      return queryDataConfigurationService;
   }

   /**
    * @param queryDataConfigurationService
    *           the queryDataConfigurationService to set
    */
   public void setQueryDataConfigurationService (IQueryDataConfigurationService queryDataConfigurationService) {
      this.queryDataConfigurationService = queryDataConfigurationService;
   }

   /**
    * @return the optimalDSetHelper
    */
   public OptimalDSetHelper getOptimalDSetHelper () {
      return optimalDSetHelper;
   }

   /**
    * @param optimalDSetHelper
    *           the optimalDSetHelper to set
    */
   public void setOptimalDSetHelper (OptimalDSetHelper optimalDSetHelper) {
      this.optimalDSetHelper = optimalDSetHelper;
   }

   public IUserManagementService getUserManagementService () {
      return userManagementService;
   }

   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

}
