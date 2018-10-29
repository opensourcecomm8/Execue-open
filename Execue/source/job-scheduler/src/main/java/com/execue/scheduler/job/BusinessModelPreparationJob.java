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
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.swi.BusinessModelPreparationContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationParamName;
import com.execue.platform.IBusinessModelPreparationService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;
import com.thoughtworks.xstream.XStream;

/**
 * This job is for preparing the business model based on the input
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/09/09
 */
public class BusinessModelPreparationJob extends BaseLineJob {

   private static final Logger              logger = Logger.getLogger(BusinessModelPreparationJob.class);
   private IBusinessModelPreparationService businessModelPreparationService;

   @Override
   protected void process (ExecueJobData jobData) throws ExecueJobSchedulerException {
      initializeDependencies();
      try {
         logger.debug("Invoking on business Model Preparation Job [" + jobData.getJobName() + "]");
         BusinessModelPreparationContext businessModelPrepartionContext = (BusinessModelPreparationContext) new XStream()
                  .fromXML(jobData.getJobRequest().getRequestData());
         businessModelPrepartionContext.setJobRequest(jobData.getJobRequest());
         businessModelPreparationService.prepareBusinessModel(businessModelPrepartionContext);
         jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);
      } catch (Exception e) {
         jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         logger.debug("JOB Execution failed for job : " + jobData.getJobName());
         logger.error(e);

      }

   }

   private void initializeDependencies () {
      businessModelPreparationService = (IBusinessModelPreparationService) SpringContextHolder
               .getBean("businessModelPreparationService");
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.job.BaseLineJob#prepareCompletionMessage(com.execue.scheduler.jobdata.ExecueJobData,
    *      java.util.Map, java.util.Map)
    */
   @Override
   protected void prepareCompletionMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      BusinessModelPreparationContext businessModelPreparationContext = (BusinessModelPreparationContext) jobData
               .getRequestContextData();

      subjectParams
               .put(NotificationParamName.APPLICATION_NAME, businessModelPreparationContext.getApplicationId() + "");
      bodyParams.put(NotificationParamName.APPLICATION_NAME, businessModelPreparationContext.getApplicationId() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.job.BaseLineJob#prepareInitiationMessage(com.execue.scheduler.jobdata.ExecueJobData,
    *      java.util.Map, java.util.Map)
    */
   @Override
   protected void prepareInitiationMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      BusinessModelPreparationContext businessModelPreparationContext = (BusinessModelPreparationContext) jobData
               .getRequestContextData();

      subjectParams
               .put(NotificationParamName.APPLICATION_NAME, businessModelPreparationContext.getApplicationId() + "");
      bodyParams.put(NotificationParamName.APPLICATION_NAME, businessModelPreparationContext.getApplicationId() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");

   }

}
