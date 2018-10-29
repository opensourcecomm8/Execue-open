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


package com.execue.platform.swi.operation.synchronization.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.execue.core.common.bean.ac.AnswersCatalogContextEntityInfo;
import com.execue.core.common.bean.ac.AnswersCatalogManagementQueue;
import com.execue.core.common.bean.ac.AnswersCatalogUpdationContext;
import com.execue.core.common.bean.ac.CubeCreationContext;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.AnswersCatalogContext;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.swi.AssetSyncAbsorptionContext;
import com.execue.core.common.bean.swi.AssetSyncInfo;
import com.execue.core.common.bean.swi.MemberSyncInfo;
import com.execue.core.common.type.ACManagementOperationSourceType;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.core.common.type.AnswersCatalogOperationType;
import com.execue.core.util.ExeCueXMLUtils;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.exception.AssetSynchronizationException;
import com.execue.platform.helper.AnswersCatalogPlatformServiceHelper;
import com.execue.platform.swi.operation.synchronization.IAssetSyncConsumptionService;
import com.execue.qdata.configuration.IQueryDataConfigurationService;
import com.execue.qdata.exception.AnswersCatalogManagementQueueException;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IAnswersCatalogManagementQueueService;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.MappingException;
import com.execue.swi.service.IMappingRetrievalService;
import com.thoughtworks.xstream.XStream;

public class AssetSyncConsumptionServiceImpl implements IAssetSyncConsumptionService {

   private IAnswersCatalogManagementQueueService answersCatalogManagementQueueService;

   private IMappingRetrievalService              mappingRetrievalService;

   private IJobDataService                       jobDataService;

   private IQueryDataConfigurationService        queryDataConfigurationService;

   @Override
   public void consumeAssetSyncInfo (AssetSyncAbsorptionContext assetSyncAbsorptionContext)
            throws AssetSynchronizationException {
      try {

         AssetSyncInfo assetSyncInfo = assetSyncAbsorptionContext.getAssetSyncInfo();

         Long parentAssetId = assetSyncInfo.getAssetId();

         // AnswersCatalogManagementQueue answersCatalogManagementQueue = prepareAnswersCatalogManagementQueue(assetSyncAbsorptionContext);

         AnswersCatalogManagementQueue answersCatalogManagementQueue = AnswersCatalogPlatformServiceHelper
                  .prepareAnswersCatalogManagementQueue(null, assetSyncAbsorptionContext.getAssetId(), null,
                           ExeCueXMLUtils.getXMLStringFromObject(assetSyncAbsorptionContext), null,
                           ACManagementOperationSourceType.ASSET_SYNC, ACManagementOperationStatusType.PENDING,
                           AnswersCatalogOperationType.PARENT_ASSET_SYNC_ABSORPTION);

         // Post an entry in AnswersCatalogManagementQueue for parent asset synch.
         getAnswersCatalogManagementQueueService().createAnswersCatalogManagementQueue(answersCatalogManagementQueue);

         // Get AnswersCatalogContext entries for cubes and populate AnswersCatalogContextEntityInfoMap.
         List<AnswersCatalogContext> acContextByParentAssetIdForCubes = getJobDataService()
                  .getAnswersCatalogContextByParentAssetIdForCubes(parentAssetId);

         Map<AnswersCatalogContextEntityInfo, Long> acContextEntityAssetIdMapForCubes = new HashMap<AnswersCatalogContextEntityInfo, Long>();

         for (AnswersCatalogContext answersCatalogContext : acContextByParentAssetIdForCubes) {
            AnswersCatalogContextEntityInfo acContextEntityInfoForCubes = prepareAnswersCatalogContextEntityInfoForCubes(answersCatalogContext);
            acContextEntityAssetIdMapForCubes.put(acContextEntityInfoForCubes, answersCatalogContext.getAssetId());
         }

         // Get AnswersCatalogContext entries for mart and populate AnswersCatalogContextEntityInfoMap.
         List<AnswersCatalogContext> acContextByParentAssetIdForMarts = getJobDataService()
                  .getAnswersCatalogContextByParentAssetIdForMarts(parentAssetId);

         Map<AnswersCatalogContextEntityInfo, Long> acContextEntityAssetIdMapForMarts = new HashMap<AnswersCatalogContextEntityInfo, Long>();

         for (AnswersCatalogContext answersCatalogContext : acContextByParentAssetIdForMarts) {
            AnswersCatalogContextEntityInfo acContextEntityInfoForMart = prepareAnswersCatalogContextEntityInfoForMart(answersCatalogContext);
            acContextEntityAssetIdMapForMarts.put(acContextEntityInfoForMart, answersCatalogContext.getAssetId());
         }

         // Get the parent asset dimesnions to check for the updation of cubes and mart .
         TreeSet<String> parentAssetDimensions = getParentAssetDimensions(assetSyncAbsorptionContext.getModelId(),
                  parentAssetId, assetSyncInfo.getMemberSyncInfo());

         // filter entries which does not require any updation for both cubes and mart.
         filterAnswersCatalogContextEntityNotEligibleForUpdation(acContextEntityAssetIdMapForCubes,
                  parentAssetDimensions);

         filterAnswersCatalogContextEntityNotEligibleForUpdation(acContextEntityAssetIdMapForMarts,
                  parentAssetDimensions);

         String dependentManagementId = String.valueOf(answersCatalogManagementQueue.getId());

         // Post entries in to AnswersCatalogManagementQueue for cubes and marts eligible for updation.

         Collection<Long> cubeAssetIdsTobeUpdated = acContextEntityAssetIdMapForCubes.values();
         if (ExecueCoreUtil.isCollectionNotEmpty(cubeAssetIdsTobeUpdated)) {
            List<AnswersCatalogManagementQueue> acManagementQueueForCubes = prepareAnswersCatalogManagementQueues(
                     assetSyncAbsorptionContext, dependentManagementId, AnswersCatalogOperationType.CUBE_UPDATION,
                     cubeAssetIdsTobeUpdated);
            getAnswersCatalogManagementQueueService().createAnswersCatalogManagementQueue(acManagementQueueForCubes);
         }

         Collection<Long> martAssetIdsTobeUpdated = acContextEntityAssetIdMapForMarts.values();
         if (ExecueCoreUtil.isCollectionNotEmpty(martAssetIdsTobeUpdated)) {
            List<AnswersCatalogManagementQueue> acManagementQueueForMarts = prepareAnswersCatalogManagementQueues(
                     assetSyncAbsorptionContext, dependentManagementId, AnswersCatalogOperationType.MART_UPDATION,
                     martAssetIdsTobeUpdated);
            getAnswersCatalogManagementQueueService().createAnswersCatalogManagementQueue(acManagementQueueForMarts);
         }
      } catch (AnswersCatalogManagementQueueException e) {
         throw new AssetSynchronizationException(e.getCode(), e);
      } catch (QueryDataException e) {
         throw new AssetSynchronizationException(e.getCode(), e);
      } catch (MappingException e) {
         throw new AssetSynchronizationException(e.getCode(), e);
      }
   }

   private List<AnswersCatalogManagementQueue> prepareAnswersCatalogManagementQueues (
            AssetSyncAbsorptionContext assetSyncAbsorptionContext, String dependentManagementId,
            AnswersCatalogOperationType answersCatalogOperationType, Collection<Long> assetIdsMarkedForUpdation) {
      List<AnswersCatalogManagementQueue> answersCatalogManagementQueues = new ArrayList<AnswersCatalogManagementQueue>();
      for (Long assetId : assetIdsMarkedForUpdation) {
         AnswersCatalogManagementQueue answersCatalogManagementQueue = new AnswersCatalogManagementQueue();
         answersCatalogManagementQueue.setAssetId(assetId);
         answersCatalogManagementQueue.setDependentManagementId(dependentManagementId);
         Long applicationId = assetSyncAbsorptionContext.getApplicationId();
         Long modelId = assetSyncAbsorptionContext.getModelId();
         Long userId = assetSyncAbsorptionContext.getUserId();
         AnswersCatalogUpdationContext answersCatalogUpdationContext = prepareAnswersCatalogUpdationContext(
                  applicationId, assetId, modelId, userId);
         XStream xStream = new XStream();
         String contextData = xStream.toXML(answersCatalogUpdationContext);
         answersCatalogManagementQueue.setOperationContext(contextData);
         answersCatalogManagementQueue.setOperationSourceType(ACManagementOperationSourceType.ASSET_SYNC);
         answersCatalogManagementQueue.setOperationStatus(ACManagementOperationStatusType.PENDING);
         answersCatalogManagementQueue.setOperationType(answersCatalogOperationType);
         answersCatalogManagementQueue.setParentAssetId(assetSyncAbsorptionContext.getAssetSyncInfo().getAssetId());
         answersCatalogManagementQueue.setRemarks(null);
         answersCatalogManagementQueue.setRequestedDate(new Date());
         answersCatalogManagementQueues.add(answersCatalogManagementQueue);
      }
      return answersCatalogManagementQueues;
   }

   private AnswersCatalogUpdationContext prepareAnswersCatalogUpdationContext (Long applicationId, Long assetId,
            Long modelId, Long userId) {
      AnswersCatalogUpdationContext answersCatalogUpdationContext = new AnswersCatalogUpdationContext();
      answersCatalogUpdationContext.setApplicationId(applicationId);
      answersCatalogUpdationContext.setExistingAssetId(assetId);
      answersCatalogUpdationContext.setModelId(modelId);
      answersCatalogUpdationContext.setUserId(userId);
      return answersCatalogUpdationContext;
   }

   private void filterAnswersCatalogContextEntityNotEligibleForUpdation (
            Map<AnswersCatalogContextEntityInfo, Long> answersCatalogContextAndEntityMap,
            TreeSet<String> parentAssetDimensions) {
      List<AnswersCatalogContextEntityInfo> acContextEntityToBeRemoved = new ArrayList<AnswersCatalogContextEntityInfo>();
      for (AnswersCatalogContextEntityInfo answersCatalogContextEntityInfo : answersCatalogContextAndEntityMap.keySet()) {
         if (!answersCatalogContextEntityInfo.isAtleastOneDimensionPresent(parentAssetDimensions)) {
            acContextEntityToBeRemoved.add(answersCatalogContextEntityInfo);
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(acContextEntityToBeRemoved)) {
         for (AnswersCatalogContextEntityInfo acContextEntityInfo : acContextEntityToBeRemoved) {
            answersCatalogContextAndEntityMap.remove(acContextEntityInfo);
         }
      }
   }

   private TreeSet<String> getBusinessEntitiesForContextComparision (List<Long> businessEntityIds) {
      TreeSet<String> businessEntities = new TreeSet<String>();
      for (Long id : businessEntityIds) {
         businessEntities.add(String.valueOf(id));
      }
      return businessEntities;
   }

   private AnswersCatalogContextEntityInfo prepareAnswersCatalogContextEntityInfoForMart (
            AnswersCatalogContext answersCatalogContext) {
      AnswersCatalogContextEntityInfo answersCatalogContextEntityInfo = new AnswersCatalogContextEntityInfo();
      String contextData = answersCatalogContext.getContextData();
      MartCreationContext martCreationContext = (MartCreationContext) ExeCueXMLUtils
               .getObjectFromXMLString(contextData);
      TreeSet<String> requestedDimensions = getBusinessEntitiesForContextComparision(martCreationContext
               .getProminentDimensionBEDIDs());
      answersCatalogContextEntityInfo.setRequestedDimensions(requestedDimensions);
      return answersCatalogContextEntityInfo;
   }

   private AnswersCatalogContextEntityInfo prepareAnswersCatalogContextEntityInfoForCubes (
            AnswersCatalogContext answersCatalogContext) {
      String contextData = answersCatalogContext.getContextData();
      if (ExecueCoreUtil.isEmpty(contextData)) {
         return null;
      }
      AnswersCatalogContextEntityInfo answersCatalogContextEntityInfo = new AnswersCatalogContextEntityInfo();
      CubeCreationContext cubeCreationContext = (CubeCreationContext) ExeCueXMLUtils
               .getObjectFromXMLString(contextData);
      List<String> dimensionBEDIDsForContextComparision = getDimensionBEDIDsForContextComparision(cubeCreationContext);
      answersCatalogContextEntityInfo.setRequestedDimensions(new TreeSet<String>(dimensionBEDIDsForContextComparision));
      return answersCatalogContextEntityInfo;
   }

   private List<String> getDimensionBEDIDsForContextComparision (CubeCreationContext cubeCreationContext) {
      List<String> dimensionBEDIDs = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionNotEmpty(cubeCreationContext.getSimpleLookupDimensionBEDIDs())) {
         for (Long bedId : cubeCreationContext.getSimpleLookupDimensionBEDIDs()) {
            dimensionBEDIDs.add(bedId.toString());
         }
      }
      if (cubeCreationContext.getRangeLookupDimensionBEDIDs() != null) {
         List<Long> rangeIds = null;
         String bedIdRangeIdDelimiter = getQueryDataConfigurationService().getSqlConcatDelimeter();
         for (Long bedId : cubeCreationContext.getRangeLookupDimensionBEDIDs().keySet()) {
            rangeIds = cubeCreationContext.getRangeLookupDimensionBEDIDs().get(bedId);
            for (Long rangeId : rangeIds) {
               dimensionBEDIDs.add(bedId + bedIdRangeIdDelimiter + rangeId);
            }
         }
      }
      return dimensionBEDIDs;
   }

   /*  private AnswersCatalogManagementQueue prepareAnswersCatalogManagementQueue (
              AssetSyncAbsorptionContext assetSyncAbsorptionContext) {

        AnswersCatalogManagementQueue answersCatalogManagementQueue = new AnswersCatalogManagementQueue();
        answersCatalogManagementQueue.setAssetId(assetSyncAbsorptionContext.getAssetId());
        answersCatalogManagementQueue.setDependentManagementId(null);
        XStream xStream = new XStream();
        String operationContext = xStream.toXML(assetSyncAbsorptionContext);
        answersCatalogManagementQueue.setOperationContext(operationContext);
        answersCatalogManagementQueue.setOperationSourceType(ACManagementOperationSourceType.ASSET_SYNC);
        answersCatalogManagementQueue.setOperationStatus(ACManagementOperationStatusType.PENDING);
        answersCatalogManagementQueue.setOperationType(AnswersCatalogOperationType.PARENT_ASSET_SYNC_ABSORPTION);
        answersCatalogManagementQueue.setParentAssetId(null);
        answersCatalogManagementQueue.setRemarks(null);
        answersCatalogManagementQueue.setRequestedDate(new Date());
        return answersCatalogManagementQueue;
     }*/

   private TreeSet<String> getParentAssetDimensions (Long modelId, Long assetId, List<MemberSyncInfo> memberSyncInfos)
            throws MappingException {
      TreeSet<String> dimensions = new TreeSet<String>();
      for (MemberSyncInfo memberSyncInfo : memberSyncInfos) {
         Long tableId = memberSyncInfo.getTabl().getId();
         Long columnId = memberSyncInfo.getColum().getId();
         if (ExecueCoreUtil.isCollectionNotEmpty(memberSyncInfo.getAddedMembers())
                  || ExecueCoreUtil.isCollectionNotEmpty(memberSyncInfo.getDeletedMembers())) {
            Mapping mapping = getMappingRetrievalService().getPrimaryMapping(modelId, assetId, tableId, columnId);
            Long bedId = mapping.getBusinessEntityDefinition().getId();
            dimensions.add(String.valueOf(bedId));
         }
      }
      return dimensions;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   public IAnswersCatalogManagementQueueService getAnswersCatalogManagementQueueService () {
      return answersCatalogManagementQueueService;
   }

   public void setAnswersCatalogManagementQueueService (
            IAnswersCatalogManagementQueueService answersCatalogManagementQueueService) {
      this.answersCatalogManagementQueueService = answersCatalogManagementQueueService;
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public IQueryDataConfigurationService getQueryDataConfigurationService () {
      return queryDataConfigurationService;
   }

   public void setQueryDataConfigurationService (IQueryDataConfigurationService queryDataConfigurationService) {
      this.queryDataConfigurationService = queryDataConfigurationService;
   }

}
