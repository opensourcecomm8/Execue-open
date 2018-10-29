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
package com.execue.acmq.controller.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.acmq.configuration.IAnswersCatalogMgmtQueueControllerConfigurationService;
import com.execue.acmq.controller.IAnswersCatalogMgmtQueueController;
import com.execue.acmq.exception.AnswersCatalogMgmtQueueControllerException;
import com.execue.core.common.bean.ac.AnswersCatalogManagementQueue;
import com.execue.core.common.bean.swi.AnswersCatalogManagementQueueContext;
import com.execue.platform.querydata.IAnswersCatalogManagementQueueWrapperService;
import com.execue.qdata.exception.AnswersCatalogManagementQueueException;
import com.execue.qdata.service.IAnswersCatalogManagementQueueService;

/**
 * @author Nitesh
 */
public class AnswersCatalogMgmtQueueController implements IAnswersCatalogMgmtQueueController {

   private static final Logger                                    logger = Logger
                                                                                  .getLogger(AnswersCatalogMgmtQueueController.class);

   private IAnswersCatalogMgmtQueueControllerConfigurationService answersCatalogMgmtQueueControllerConfigurationService;
   private IAnswersCatalogManagementQueueService                  answersCatalogManagementQueueService;
   private IAnswersCatalogManagementQueueWrapperService           answersCatalogManagementQueueWrapperService;

   // TODO: NK: Need to log the steps and update the operation status
   public List<AnswersCatalogManagementQueue> processAnswersCatalogManagementQueue (
            AnswersCatalogManagementQueueContext answersCatalogManagementQueueContext)
            throws AnswersCatalogMgmtQueueControllerException {
      try {

         // Get how many acmq entries are in progress
         int currentACMQInProgressCount = getAnswersCatalogManagementQueueService()
                  .getACMQInProgressCountExcludingUserRequestOperationSrcType();

         // Get max allowed acmq entries to be in progress
         int maxACMQInProgressCount = getAnswersCatalogMgmtQueueControllerConfigurationService()
                  .getMaxACMQInProgressCount();

         // Return if the currentACMQInProgressCount is matching the maxACMQInProgressCount
         List<AnswersCatalogManagementQueue> answersCatalogManagementQueues = new ArrayList<AnswersCatalogManagementQueue>();
         if (currentACMQInProgressCount >= maxACMQInProgressCount) {
            logger.debug("Maximum allowed jobs are already in progress...");
            return answersCatalogManagementQueues;
         }

         // Get all the pending AnswersCatalogManagementQueue in the order of consideration for execution
         List<Long> idsToIgnore = new ArrayList<Long>();
         while (currentACMQInProgressCount < maxACMQInProgressCount) {
            AnswersCatalogManagementQueue answersCatalogManagementQueue = getAnswersCatalogManagementQueueWrapperService()
                     .validateAndPickLatestPendingRecord(idsToIgnore);

            if (answersCatalogManagementQueue == null) {
               if (logger.isDebugEnabled()) {
                  logger.debug("No More Valid Pending answers catalog management queue entry exists... ");
               }
               break;
            }

            // Schedule the queue entry for execution
            answersCatalogManagementQueues.add(answersCatalogManagementQueue);

            // Increment the counter
            currentACMQInProgressCount++;
            if (currentACMQInProgressCount >= maxACMQInProgressCount) {
               logger.debug("Reached the maximum allowed jobs limit...");
               break;
            }
         }
         return answersCatalogManagementQueues;
      } catch (AnswersCatalogManagementQueueException e) {
         throw new AnswersCatalogMgmtQueueControllerException(e.getCode(), e);
      }
   }

   /**
    * @return the answersCatalogMgmtQueueControllerConfigurationService
    */
   public IAnswersCatalogMgmtQueueControllerConfigurationService getAnswersCatalogMgmtQueueControllerConfigurationService () {
      return answersCatalogMgmtQueueControllerConfigurationService;
   }

   /**
    * @param answersCatalogMgmtQueueControllerConfigurationService
    *           the answersCatalogMgmtQueueControllerConfigurationService to set
    */
   public void setAnswersCatalogMgmtQueueControllerConfigurationService (
            IAnswersCatalogMgmtQueueControllerConfigurationService answersCatalogMgmtQueueControllerConfigurationService) {
      this.answersCatalogMgmtQueueControllerConfigurationService = answersCatalogMgmtQueueControllerConfigurationService;
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
    * @return the answersCatalogManagementQueueWrapperService
    */
   public IAnswersCatalogManagementQueueWrapperService getAnswersCatalogManagementQueueWrapperService () {
      return answersCatalogManagementQueueWrapperService;
   }

   /**
    * @param answersCatalogManagementQueueWrapperService the answersCatalogManagementQueueWrapperService to set
    */
   public void setAnswersCatalogManagementQueueWrapperService (
            IAnswersCatalogManagementQueueWrapperService answersCatalogManagementQueueWrapperService) {
      this.answersCatalogManagementQueueWrapperService = answersCatalogManagementQueueWrapperService;
   }
}