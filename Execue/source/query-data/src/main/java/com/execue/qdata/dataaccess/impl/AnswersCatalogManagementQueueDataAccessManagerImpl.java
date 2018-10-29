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


package com.execue.qdata.dataaccess.impl;

import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.ac.AnswersCatalogManagementQueue;
import com.execue.core.common.type.ACManagementOperationSourceType;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.qdata.dao.IAnswersCatalogManagementQueueDAO;
import com.execue.qdata.dataaccess.IAnswersCatalogManagementQueueDataAccessManager;
import com.execue.qdata.exception.AnswersCatalogManagementQueueException;
import com.execue.qdata.exception.QueryDataExceptionCodes;

public class AnswersCatalogManagementQueueDataAccessManagerImpl implements
         IAnswersCatalogManagementQueueDataAccessManager {

   private IAnswersCatalogManagementQueueDAO answersCatalogManagementQueueDAO;

   @Override
   public void createAnswersCatalogManagementQueue (AnswersCatalogManagementQueue answersCatalogManagementQueue)
            throws AnswersCatalogManagementQueueException {
      try {
         getAnswersCatalogManagementQueueDAO().create(answersCatalogManagementQueue);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(
                  QueryDataExceptionCodes.QUERY_ANSWERS_CATALOG_MANAGEMENT_QUEUE_CREATION_FAILED, e);
      }
   }

   @Override
   public void createAnswersCatalogManagementQueue (
            List<AnswersCatalogManagementQueue> answersCatalogManagementQueueList)
            throws AnswersCatalogManagementQueueException {
      try {
         getAnswersCatalogManagementQueueDAO().createAll(answersCatalogManagementQueueList);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(
                  QueryDataExceptionCodes.QUERY_ANSWERS_CATALOG_MANAGEMENT_QUEUE_CREATION_FAILED, e);
      }
   }

   @Override
   public void deleteAnswersCatalogManagementQueue (AnswersCatalogManagementQueue answersCatalogManagementQueue)
            throws AnswersCatalogManagementQueueException {
      try {
         getAnswersCatalogManagementQueueDAO().delete(answersCatalogManagementQueue);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(
                  QueryDataExceptionCodes.QUERY_ANSWERS_CATALOG_MANAGEMENT_QUEUE_DELETION_FAILED, e);
      }
   }

   @Override
   public void deleteAnswersCatalogManagementQueue (
            List<AnswersCatalogManagementQueue> answersCatalogManagementQueueList)
            throws AnswersCatalogManagementQueueException {
      try {
         getAnswersCatalogManagementQueueDAO().deleteAll(answersCatalogManagementQueueList);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(
                  QueryDataExceptionCodes.QUERY_ANSWERS_CATALOG_MANAGEMENT_QUEUE_DELETION_FAILED, e);
      }
   }

   @Override
   public AnswersCatalogManagementQueue getAnswersCatalogManagementQueue (Long id)
            throws AnswersCatalogManagementQueueException {
      try {
         return getAnswersCatalogManagementQueueDAO().getById(id, AnswersCatalogManagementQueue.class);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(
                  QueryDataExceptionCodes.QUERY_GET_ANSWERS_CATALOG_MANAGEMENT_QUEUE_BY_ID_FAILED, e);
      }
   }

   @Override
   public void deleteCompletedOperationsByRequestedDate (Date requestedDate)
            throws AnswersCatalogManagementQueueException {
      try {
         getAnswersCatalogManagementQueueDAO().deleteCompletedOperationsByRequestedDate(requestedDate);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(
                  QueryDataExceptionCodes.QUERY_DELETE_ANSWERS_CATALOG_MANAGEMENT_QUEUE_BY_REQUEST_DATE_FAILED, e);
      }
   }

   @Override
   public List<AnswersCatalogManagementQueue> getAllAnswersCatalogManagementQueueByOperationStatus (
            ACManagementOperationStatusType operationStatusType) throws AnswersCatalogManagementQueueException {
      try {
         return getAnswersCatalogManagementQueueDAO().getAllAnswersCatalogManagementQueueByOperationStatus(
                  operationStatusType);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(
                  QueryDataExceptionCodes.QUERY_GET_PENDING_ANSWERS_CATALOG_MANAGEMENT_QUEUE_FAILED, e);
      }
   }

   @Override
   public int getACMQInProgressCountExcludingUserRequestOperationSrcType ()
            throws AnswersCatalogManagementQueueException {
      try {
         return getAnswersCatalogManagementQueueDAO().getACMQInProgressCountExcludingUserRequestOperationSrcType();
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(e.getCode(), e);
      }
   }

   @Override
   public boolean isAnswersCatalogManagementQueueUnderProgress (List<Long> assetIds)
            throws AnswersCatalogManagementQueueException {
      try {
         return getAnswersCatalogManagementQueueDAO().isAnswersCatalogManagementQueueUnderProgress(assetIds);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(e.getCode(), e);
      }
   }

   @Override
   public boolean isAnyChildAssetAnswersCatalogManagementQueueUnderProgress (Long parentAssetId)
            throws AnswersCatalogManagementQueueException {
      try {
         return getAnswersCatalogManagementQueueDAO().isAnyChildAssetAnswersCatalogManagementQueueUnderProgress(
                  parentAssetId);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(e.getCode(), e);
      }
   }

   @Override
   public boolean isDependantAnswersCatalogManagementQueueCompleted (List<Long> dependentManagementIds)
            throws AnswersCatalogManagementQueueException {
      try {
         return getAnswersCatalogManagementQueueDAO().isDependantAnswersCatalogManagementQueueCompleted(
                  dependentManagementIds);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(e.getCode(), e);
      }
   }

   @Override
   public boolean isDependantAnswersCatalogManagementQueueSuccessfullyCompleted (List<Long> dependentManagementIds)
            throws AnswersCatalogManagementQueueException {
      try {
         return getAnswersCatalogManagementQueueDAO().isDependantAnswersCatalogManagementQueueSuccessfullyCompleted(
                  dependentManagementIds);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(e.getCode(), e);
      }
   }

   @Override
   public void updateOperationStatus (Long acmqId, ACManagementOperationStatusType operaionStatusType)
            throws AnswersCatalogManagementQueueException {
      try {
         getAnswersCatalogManagementQueueDAO().updateOperationStatus(acmqId, operaionStatusType);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(e.getCode(), e);
      }
   }

   public void updateOperationStatusAndAssetId (Long acmqId, ACManagementOperationStatusType operaionStatusType,
            Long assetId) throws AnswersCatalogManagementQueueException {
      try {
         getAnswersCatalogManagementQueueDAO().updateOperationStatusAndAssetId(acmqId, operaionStatusType, assetId);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(e.getCode(), e);
      }
   }

   public void updateOperationStatusAndRemarks (Long acmqId, ACManagementOperationStatusType operaionStatusType,
            String remarks) throws AnswersCatalogManagementQueueException {
      try {
         getAnswersCatalogManagementQueueDAO().updateOperationStatusAndRemarks(acmqId, operaionStatusType, remarks);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(e.getCode(), e);
      }
   }

   @Override
   public boolean isAnswersCatalogManagementQueueUnderProgressForSourceType (Long parentAssetId,
            ACManagementOperationSourceType operationSourceType) throws AnswersCatalogManagementQueueException {
      try {
         return getAnswersCatalogManagementQueueDAO().isAnswersCatalogManagementQueueUnderProgressForSourceType(
                  parentAssetId, operationSourceType);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(e.getCode(), e);
      }
   }

   @Override
   public boolean isACMQCubeDeletionInProgress (Long assetId) throws AnswersCatalogManagementQueueException {
      try {
         return getAnswersCatalogManagementQueueDAO().isACMQCubeDeletionInProgress(assetId);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(e.getCode(), e);
      }
   }

   @Override
   public AnswersCatalogManagementQueue getLatestPendingRecord (List<Long> idsToIgnore)
            throws AnswersCatalogManagementQueueException {
      try {
         return getAnswersCatalogManagementQueueDAO().getLatestPendingRecord(idsToIgnore);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(e.getCode(), e);
      }
   }

   @Override
   public void invalidateAnswersCatalogManagementQueues (List<Long> assetIds)
            throws AnswersCatalogManagementQueueException {
      try {
         getAnswersCatalogManagementQueueDAO().invalidateAnswersCatalogManagementQueues(assetIds);
      } catch (DataAccessException e) {
         throw new AnswersCatalogManagementQueueException(e.getCode(), e);
      }
   }

   public IAnswersCatalogManagementQueueDAO getAnswersCatalogManagementQueueDAO () {
      return answersCatalogManagementQueueDAO;
   }

   public void setAnswersCatalogManagementQueueDAO (IAnswersCatalogManagementQueueDAO answersCatalogManagementQueueDAO) {
      this.answersCatalogManagementQueueDAO = answersCatalogManagementQueueDAO;
   }

}