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


package com.execue.qdata.dataaccess;

import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.ac.AnswersCatalogManagementQueue;
import com.execue.core.common.type.ACManagementOperationSourceType;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.qdata.exception.AnswersCatalogManagementQueueException;

public interface IAnswersCatalogManagementQueueDataAccessManager {

   public void createAnswersCatalogManagementQueue (AnswersCatalogManagementQueue answersCatalogManagementQueue)
            throws AnswersCatalogManagementQueueException;

   public void createAnswersCatalogManagementQueue (
            List<AnswersCatalogManagementQueue> answersCatalogManagementQueueList)
            throws AnswersCatalogManagementQueueException;

   public void deleteAnswersCatalogManagementQueue (AnswersCatalogManagementQueue answersCatalogManagementQueue)
            throws AnswersCatalogManagementQueueException;

   public void deleteAnswersCatalogManagementQueue (
            List<AnswersCatalogManagementQueue> answersCatalogManagementQueueList)
            throws AnswersCatalogManagementQueueException;

   public AnswersCatalogManagementQueue getAnswersCatalogManagementQueue (Long id)
            throws AnswersCatalogManagementQueueException;

   public void deleteCompletedOperationsByRequestedDate (Date requestedDate)
            throws AnswersCatalogManagementQueueException;

   public List<AnswersCatalogManagementQueue> getAllAnswersCatalogManagementQueueByOperationStatus (
            ACManagementOperationStatusType operationStatusType) throws AnswersCatalogManagementQueueException;

   public int getACMQInProgressCountExcludingUserRequestOperationSrcType ()
            throws AnswersCatalogManagementQueueException;

   public boolean isAnswersCatalogManagementQueueUnderProgress (List<Long> assetIds)
            throws AnswersCatalogManagementQueueException;

   public boolean isDependantAnswersCatalogManagementQueueCompleted (List<Long> dependentManagementId)
            throws AnswersCatalogManagementQueueException;

   public boolean isDependantAnswersCatalogManagementQueueSuccessfullyCompleted (List<Long> dependentManagementIds)
            throws AnswersCatalogManagementQueueException;

   public void updateOperationStatus (Long acmqId, ACManagementOperationStatusType operaionStatusType)
            throws AnswersCatalogManagementQueueException;

   public void updateOperationStatusAndAssetId (Long acmqId, ACManagementOperationStatusType operaionStatusType,
            Long assetId) throws AnswersCatalogManagementQueueException;

   public void updateOperationStatusAndRemarks (Long acmqId, ACManagementOperationStatusType operaionStatusType,
            String remarks) throws AnswersCatalogManagementQueueException;

   public boolean isAnswersCatalogManagementQueueUnderProgressForSourceType (Long parentAssetId,
            ACManagementOperationSourceType operationSourceType) throws AnswersCatalogManagementQueueException;

   public boolean isAnyChildAssetAnswersCatalogManagementQueueUnderProgress (Long parentAssetId)
            throws AnswersCatalogManagementQueueException;

   public AnswersCatalogManagementQueue getLatestPendingRecord (List<Long> idsToIgnore)
            throws AnswersCatalogManagementQueueException;

   public void invalidateAnswersCatalogManagementQueues (List<Long> assetIds)
            throws AnswersCatalogManagementQueueException;

   public boolean isACMQCubeDeletionInProgress (Long assetId) throws AnswersCatalogManagementQueueException;

}
