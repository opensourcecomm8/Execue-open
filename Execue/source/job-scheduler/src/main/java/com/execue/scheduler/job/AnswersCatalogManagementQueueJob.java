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


package com.execue.scheduler.job;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.acmq.controller.IAnswersCatalogMgmtQueueController;
import com.execue.core.common.bean.ac.AnswersCatalogManagementQueue;
import com.execue.core.common.bean.swi.AnswersCatalogManagementQueueContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationParamName;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.helper.AnswersCatalogMgmtQueueJobServiceHelper;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;

/**
 * 
 * @author jitendra
 *
 */
public class AnswersCatalogManagementQueueJob extends BaseLineJob {

   private static final Logger                     logger = Logger.getLogger(AnswersCatalogManagementQueueJob.class);

   private IAnswersCatalogMgmtQueueController      answersCatalogMgmtQueueController;
   private AnswersCatalogMgmtQueueJobServiceHelper answersCatalogMgmtQueueJobServiceHelper;

   @Override
   protected void process (ExecueJobData jobData) throws ExecueJobSchedulerException {
      try {
         initializeDependencies();
         AnswersCatalogManagementQueueContext answersCatalogManagementQueueContext = new AnswersCatalogManagementQueueContext();
         answersCatalogManagementQueueContext.setJobRequest(jobData.getJobRequest());
         List<AnswersCatalogManagementQueue> answersCatalogManagementQueues = getAnswersCatalogMgmtQueueController()
                  .processAnswersCatalogManagementQueue(answersCatalogManagementQueueContext);
         if (ExecueCoreUtil.isCollectionNotEmpty(answersCatalogManagementQueues)) {
            getAnswersCatalogMgmtQueueJobServiceHelper().processAnswersCatalogManagementQueueJob(
                     answersCatalogManagementQueues);
         }
         jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);
      } catch (Exception ex) {
         ex.printStackTrace();
         jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         logger.error("JOB Execution failed for job -- " + jobData.getJobName());
         jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         logger.error(ex, ex);
      }
   }

   private void initializeDependencies () throws Exception {
      answersCatalogMgmtQueueController = (IAnswersCatalogMgmtQueueController) SpringContextHolder
               .getBean("answersCatalogMgmtQueueController");

      answersCatalogMgmtQueueJobServiceHelper = (AnswersCatalogMgmtQueueJobServiceHelper) SpringContextHolder
               .getBean("answersCatalogMgmtQueueJobServiceHelper");

   }

   @Override
   protected void prepareInitiationMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
   }

   @Override
   protected void prepareCompletionMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
   }

   /**
   * @return the answersCatalogMgmtQueueController
   */
   public IAnswersCatalogMgmtQueueController getAnswersCatalogMgmtQueueController () {
      return answersCatalogMgmtQueueController;
   }

   /**
   * @param answersCatalogMgmtQueueController the answersCatalogMgmtQueueController to set
   */
   public void setAnswersCatalogMgmtQueueController (
            IAnswersCatalogMgmtQueueController answersCatalogMgmtQueueController) {
      this.answersCatalogMgmtQueueController = answersCatalogMgmtQueueController;
   }

   /**
    * @return the answersCatalogMgmtQueueJobServiceHelper
    */
   public AnswersCatalogMgmtQueueJobServiceHelper getAnswersCatalogMgmtQueueJobServiceHelper () {
      return answersCatalogMgmtQueueJobServiceHelper;
   }

   /**
    * @param answersCatalogMgmtQueueJobServiceHelper the answersCatalogMgmtQueueJobServiceHelper to set
    */
   public void setAnswersCatalogMgmtQueueJobServiceHelper (
            AnswersCatalogMgmtQueueJobServiceHelper answersCatalogMgmtQueueJobServiceHelper) {
      this.answersCatalogMgmtQueueJobServiceHelper = answersCatalogMgmtQueueJobServiceHelper;
   }

}
