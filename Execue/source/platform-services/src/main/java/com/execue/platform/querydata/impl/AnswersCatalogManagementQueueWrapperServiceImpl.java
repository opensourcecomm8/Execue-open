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
package com.execue.platform.querydata.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.ac.AnswersCatalogManagementQueue;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.swi.AssetDeletionContext;
import com.execue.core.common.type.ACManagementOperationSourceType;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.core.common.type.AnswersCatalogOperationType;
import com.execue.core.common.util.ExeCueXMLUtils;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.helper.AnswersCatalogPlatformServiceHelper;
import com.execue.platform.querydata.IAnswersCatalogManagementQueueWrapperService;
import com.execue.qdata.configuration.IQueryDataConfigurationService;
import com.execue.qdata.exception.AnswersCatalogManagementQueueException;
import com.execue.qdata.service.IAnswersCatalogManagementQueueService;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * @author Nitesh
 */
public class AnswersCatalogManagementQueueWrapperServiceImpl implements IAnswersCatalogManagementQueueWrapperService {

   private static final Logger                   logger = Logger
                                                                 .getLogger(AnswersCatalogManagementQueueWrapperServiceImpl.class);

   private ISDXRetrievalService                  sdxRetrievalService;
   private IAnswersCatalogManagementQueueService answersCatalogManagementQueueService;
   private IQueryDataConfigurationService        queryDataConfigurationService;

   /*
    * This method will validate the latest pending entry in queue. If validation succeeds then it updates the entry in
    * QUEUE as INITIATED and returns that entry itself. If validation fails, then will check for further pending entry
    * in queue till all the entries are exhausted either because of validation or no further pending record found, in
    * that case, it will return null.
    */
   @Override
   public AnswersCatalogManagementQueue validateAndPickLatestPendingRecord (List<Long> idsToIgnore)
            throws AnswersCatalogManagementQueueException {

      // NOTE:: This method is using synchronized block to lock the object of the IAnswersCatalogManagementQueueService.
      // This is necessary as we need to perform several validation check before picking the valid pending record from
      // the queue.
      // So no parallel activity should be perform on the same queue at the same time. Hence lock is required.
      // Also we will update the valid entry from PENDING to INITIATED at the end once it passes all the validation.
      // Hence this valid entry will not participate in any further PENDING tasks, hence its the caller
      // responsibility to make sure it gets updated with the proper status based on its usage.

      AnswersCatalogManagementQueue answersCatalogManagementQueue = null;
      boolean validRecordFound = false;
      synchronized (answersCatalogManagementQueueService) {
         do {
            answersCatalogManagementQueue = getAnswersCatalogManagementQueueService().getLatestPendingRecord(
                     idsToIgnore);

            if (answersCatalogManagementQueue == null) {
               break;
            }

            Long acmqId = answersCatalogManagementQueue.getId();
            // There should not be any other operation being executed on this asset
            Long assetId = answersCatalogManagementQueue.getAssetId();
            if (assetId != null) {
               boolean assetUnderProgress = getAnswersCatalogManagementQueueService()
                        .isAnswersCatalogManagementQueueUnderProgress(assetId);
               if (assetUnderProgress) {
                  idsToIgnore.add(acmqId);
                  if (logger.isDebugEnabled()) {
                     logger.debug("Answers catalog management queue is already in progress with same asset id: "
                              + assetId);
                  }
                  continue;
               }
            }

            // If the request has a dependent request id then that dependent request should have been completed by this
            // moment
            String dependentManagementId = answersCatalogManagementQueue.getDependentManagementId();
            if (ExecueCoreUtil.isNotEmpty(dependentManagementId)) {
               List<Long> dependentManagementIds = ExecueCoreUtil.getLongListFromString(dependentManagementId,
                        getQueryDataConfigurationService().getSqlConcatDelimeter());
               if (ExecueCoreUtil.isCollectionNotEmpty(dependentManagementIds)) {
                  boolean dependantACMQCompleted = getAnswersCatalogManagementQueueService()
                           .isDependantAnswersCatalogManagementQueueCompleted(dependentManagementIds);

                  if (!dependantACMQCompleted) {
                     idsToIgnore.add(acmqId);
                     if (logger.isDebugEnabled()) {
                        logger.debug("Answers catalog management queue is already in progress with dependant id: "
                                 + dependentManagementIds);
                     }
                     continue;
                  }
               }
            }

            // There should not be any operation running on it's parent asset if the current asset is a child
            Long parentAssetId = answersCatalogManagementQueue.getParentAssetId();
            if (parentAssetId != null) {
               boolean parentAssetUnderProgress = getAnswersCatalogManagementQueueService()
                        .isAnswersCatalogManagementQueueUnderProgress(parentAssetId);
               if (parentAssetUnderProgress) {
                  idsToIgnore.add(acmqId);
                  if (logger.isDebugEnabled()) {
                     logger.debug("Answers catalog management queue is already in progress with parent asset id: "
                              + parentAssetId);
                  }
                  continue;
               }
            } else {
               // There should not be any operation running on it's children assets if the current asset is a parent to
               // some other
               try {
                  List<Asset> childAssets = getSdxRetrievalService().getAllChildAssets(assetId);
                  if (ExecueCoreUtil.isCollectionNotEmpty(childAssets)) {
                     List<Long> childAssetIds = new ArrayList<Long>();
                     for (Asset asset : childAssets) {
                        childAssetIds.add(asset.getId());
                     }

                     boolean childAssetUnderProgress = getAnswersCatalogManagementQueueService()
                              .isAnswersCatalogManagementQueueUnderProgress(childAssetIds);

                     if (childAssetUnderProgress) {
                        idsToIgnore.add(acmqId);
                        if (logger.isDebugEnabled()) {
                           logger
                                    .debug("Answers catalog management queue is in progress for some of the child asset ids: "
                                             + childAssetIds);
                        }
                        continue;
                     }
                  }
               } catch (SDXException e) {
                  throw new AnswersCatalogManagementQueueException(e.getCode(), e);
               }
            }

            // Mark the queue entry as initiated
            getAnswersCatalogManagementQueueService().updateOperationStatus(answersCatalogManagementQueue.getId(),
                     ACManagementOperationStatusType.INITIATED);
            validRecordFound = true;
         } while (!validRecordFound); // Execute till no valid record found or answersCatalogManagementQueue is null
      }
      return answersCatalogManagementQueue;
   }

   /**
    * @param answersCatalogManagementQueue
    * @param userId
    * @throws AnswersCatalogManagementQueueException
    */
   public void validateAndCreateCubeDeletionACMQEntry (List<Long> dependantACMQIds, List<Long> failedACMQIds,
            Long userId) throws AnswersCatalogManagementQueueException {

      // NOTE:: This method is using synchronized block to lock the object of the IAnswersCatalogManagementQueueService.
      // This is necessary as we need to perform several validation check before creating the cube deletion record in 
      // the queue for the dependanat acmq ids which are successful.
      // So no parallel activity should be perform on the dependant acmq ids at the same time. Hence lock is required.
      // Also we will update the failed acmq ids list if dependant acmq ids are failed.

      // Check for each dependent acmq id's asset if it got created successfully, then rollback that cube creation by scheduling the delete cube job  
      synchronized (answersCatalogManagementQueueService) {

         for (Long dependantACMQId : dependantACMQIds) {
            AnswersCatalogManagementQueue answersCatalogManagementQueue = getAnswersCatalogManagementQueueService()
                     .getAnswersCatalogManagementQueue(dependantACMQId);
            if (ACManagementOperationStatusType.SUCCESSFUL == answersCatalogManagementQueue.getOperationStatus()) {

               Long assetId = answersCatalogManagementQueue.getAssetId();
               Long baseAssetId = answersCatalogManagementQueue.getParentAssetId();

               // Check if it is already scheduled for rollback
               boolean isCubeDeletionInProgress = getAnswersCatalogManagementQueueService()
                        .isACMQCubeDeletionInProgress(assetId);

               if (!isCubeDeletionInProgress) {
                  // Prepare the new asset deletion context to rollback the creation
                  AssetDeletionContext assetDeletionContextToRollback = new AssetDeletionContext();
                  assetDeletionContextToRollback.setAssetId(assetId);
                  assetDeletionContextToRollback.setParentAssetId(baseAssetId);
                  assetDeletionContextToRollback.setUserId(userId);

                  // populate ACMQ context and create an entry in ACMQ
                  AnswersCatalogManagementQueue answersCatalogManagementQueueDeleteEntry = AnswersCatalogPlatformServiceHelper
                           .prepareAnswersCatalogManagementQueue(answersCatalogManagementQueue.getParentAssetId(),
                                    answersCatalogManagementQueue.getAssetId(), null, ExeCueXMLUtils
                                             .getXMLStringFromObject(assetDeletionContextToRollback), null,
                                    ACManagementOperationSourceType.SYSTEM_INITIATED,
                                    ACManagementOperationStatusType.PENDING, AnswersCatalogOperationType.CUBE_DELETION);

                  getAnswersCatalogManagementQueueService().createAnswersCatalogManagementQueue(
                           answersCatalogManagementQueueDeleteEntry);
               }
            } else {
               failedACMQIds.add(answersCatalogManagementQueue.getId());
            }
         }
      }
   }

   /**
    * @return the sdxRetrievalService
    */
   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   /**
    * @param sdxRetrievalService
    *           the sdxRetrievalService to set
    */
   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
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

   public IQueryDataConfigurationService getQueryDataConfigurationService () {
      return queryDataConfigurationService;
   }

   public void setQueryDataConfigurationService (IQueryDataConfigurationService queryDataConfigurationService) {
      this.queryDataConfigurationService = queryDataConfigurationService;
   }

}
