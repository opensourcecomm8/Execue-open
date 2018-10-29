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


package com.execue.qdata.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.ac.AnswersCatalogManagementQueue;
import com.execue.core.common.type.ACManagementOperationSourceType;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.qdata.dataaccess.IAnswersCatalogManagementQueueDataAccessManager;
import com.execue.qdata.exception.AnswersCatalogManagementQueueException;
import com.execue.qdata.service.IAnswersCatalogManagementQueueService;

public class AnswersCatalogManagementQueueServiceImpl implements IAnswersCatalogManagementQueueService {

   private IAnswersCatalogManagementQueueDataAccessManager answersCatalogManagementQueueDataAccessManager;

   @Override
   public void createAnswersCatalogManagementQueue (AnswersCatalogManagementQueue answersCatalogManagementQueue)
            throws AnswersCatalogManagementQueueException {

      getAnswersCatalogManagementQueueDataAccessManager().createAnswersCatalogManagementQueue(
               answersCatalogManagementQueue);
   }

   @Override
   public void createAnswersCatalogManagementQueue (
            List<AnswersCatalogManagementQueue> answersCatalogManagementQueueList)
            throws AnswersCatalogManagementQueueException {
      getAnswersCatalogManagementQueueDataAccessManager().createAnswersCatalogManagementQueue(
               answersCatalogManagementQueueList);
   }

   @Override
   public void deleteAnswersCatalogManagementQueue (AnswersCatalogManagementQueue answersCatalogManagementQueue)
            throws AnswersCatalogManagementQueueException {
      getAnswersCatalogManagementQueueDataAccessManager().deleteAnswersCatalogManagementQueue(
               answersCatalogManagementQueue);
   }

   @Override
   public void deleteAnswersCatalogManagementQueue (
            List<AnswersCatalogManagementQueue> answersCatalogManagementQueueList)
            throws AnswersCatalogManagementQueueException {
      getAnswersCatalogManagementQueueDataAccessManager().deleteAnswersCatalogManagementQueue(
               answersCatalogManagementQueueList);
   }

   @Override
   public void deleteCompletedOperationsByRequestedDate (Date requestedDate)
            throws AnswersCatalogManagementQueueException {
      getAnswersCatalogManagementQueueDataAccessManager().deleteCompletedOperationsByRequestedDate(requestedDate);
   }

   @Override
   public AnswersCatalogManagementQueue getAnswersCatalogManagementQueue (Long id)
            throws AnswersCatalogManagementQueueException {
      return getAnswersCatalogManagementQueueDataAccessManager().getAnswersCatalogManagementQueue(id);
   }

   @Override
   public int getACMQInProgressCountExcludingUserRequestOperationSrcType ()
            throws AnswersCatalogManagementQueueException {
      return getAnswersCatalogManagementQueueDataAccessManager()
               .getACMQInProgressCountExcludingUserRequestOperationSrcType();
   }

   @Override
   public List<AnswersCatalogManagementQueue> getAllAnswersCatalogManagementQueueByOperationStatus (
            ACManagementOperationStatusType operationStatusType) throws AnswersCatalogManagementQueueException {
      return getAnswersCatalogManagementQueueDataAccessManager().getAllAnswersCatalogManagementQueueByOperationStatus(
               operationStatusType);
   }

   @Override
   public boolean isAnswersCatalogManagementQueueUnderProgress (List<Long> assetIds)
            throws AnswersCatalogManagementQueueException {
      return getAnswersCatalogManagementQueueDataAccessManager().isAnswersCatalogManagementQueueUnderProgress(assetIds);
   }

   @Override
   public boolean isAnswersCatalogManagementQueueUnderProgress (Long assetId)
            throws AnswersCatalogManagementQueueException {
      List<Long> assetIds = new ArrayList<Long>();
      assetIds.add(assetId);
      return getAnswersCatalogManagementQueueDataAccessManager().isAnswersCatalogManagementQueueUnderProgress(assetIds);
   }

   @Override
   public boolean isAnyChildAssetAnswersCatalogManagementQueueUnderProgress (Long parentAssetId)
            throws AnswersCatalogManagementQueueException {
      return getAnswersCatalogManagementQueueDataAccessManager()
               .isAnyChildAssetAnswersCatalogManagementQueueUnderProgress(parentAssetId);
   }

   @Override
   public boolean isDependantAnswersCatalogManagementQueueCompleted (List<Long> dependentManagementIds)
            throws AnswersCatalogManagementQueueException {
      return getAnswersCatalogManagementQueueDataAccessManager().isDependantAnswersCatalogManagementQueueCompleted(
               dependentManagementIds);
   }

   @Override
   public boolean isDependantAnswersCatalogManagementQueueSuccessfullyCompleted (List<Long> dependentManagementIds)
            throws AnswersCatalogManagementQueueException {
      return getAnswersCatalogManagementQueueDataAccessManager()
               .isDependantAnswersCatalogManagementQueueSuccessfullyCompleted(dependentManagementIds);
   }

   @Override
   public void updateOperationStatus (Long acmqId, ACManagementOperationStatusType operaionStatusType)
            throws AnswersCatalogManagementQueueException {
      getAnswersCatalogManagementQueueDataAccessManager().updateOperationStatus(acmqId, operaionStatusType);
   }

   public void updateOperationStatusAndAssetId (Long acmqId, ACManagementOperationStatusType operaionStatusType,
            Long assetId) throws AnswersCatalogManagementQueueException {
      getAnswersCatalogManagementQueueDataAccessManager().updateOperationStatusAndAssetId(acmqId, operaionStatusType,
               assetId);
   }

   @Override
   public void updateOperationStatusAndRemarks (Long acmqId, ACManagementOperationStatusType operaionStatusType,
            String remarks) throws AnswersCatalogManagementQueueException {
      getAnswersCatalogManagementQueueDataAccessManager().updateOperationStatusAndRemarks(acmqId, operaionStatusType,
               remarks);
   }

   @Override
   public boolean isAnswersCatalogManagementQueueUnderProgressForSourceType (Long parentAssetId,
            ACManagementOperationSourceType operationSourceType) throws AnswersCatalogManagementQueueException {
      return getAnswersCatalogManagementQueueDataAccessManager()
               .isAnswersCatalogManagementQueueUnderProgressForSourceType(parentAssetId, operationSourceType);
   }

   @Override
   public boolean isACMQCubeDeletionInProgress (Long assetId) throws AnswersCatalogManagementQueueException {
      return getAnswersCatalogManagementQueueDataAccessManager().isACMQCubeDeletionInProgress(assetId);
   }

   @Override
   public AnswersCatalogManagementQueue getLatestPendingRecord (List<Long> idsToIgnore)
            throws AnswersCatalogManagementQueueException {
      return getAnswersCatalogManagementQueueDataAccessManager().getLatestPendingRecord(idsToIgnore);
   }

   @Override
   public void invalidateAnswersCatalogManagementQueues (List<Long> assetIds)
            throws AnswersCatalogManagementQueueException {
      getAnswersCatalogManagementQueueDataAccessManager().invalidateAnswersCatalogManagementQueues(assetIds);
   }

   @Override
   public void invalidateAnswersCatalogManagementQueues (Long assetId) throws AnswersCatalogManagementQueueException {
      List<Long> assetIds = new ArrayList<Long>();
      assetIds.add(assetId);
      invalidateAnswersCatalogManagementQueues(assetIds);
   }

   public IAnswersCatalogManagementQueueDataAccessManager getAnswersCatalogManagementQueueDataAccessManager () {
      return answersCatalogManagementQueueDataAccessManager;
   }

   public void setAnswersCatalogManagementQueueDataAccessManager (
            IAnswersCatalogManagementQueueDataAccessManager answersCatalogManagementQueueDataAccessManager) {
      this.answersCatalogManagementQueueDataAccessManager = answersCatalogManagementQueueDataAccessManager;
   }

}