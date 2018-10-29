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

import com.execue.core.common.bean.swi.PopularityRestorationContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationParamName;
import com.execue.platform.IPopularityDispersionService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;

public class PopularityDispersionJob extends BaseLineJob {

   private static Logger                logger = Logger.getLogger(PopularityDispersionJob.class);
   private IPopularityDispersionService popularityDispersionService;

   @Override
   protected void process (ExecueJobData jobData) throws ExecueJobSchedulerException {
      PopularityRestorationContext popularityRestorationContext = null;
      logger.debug("Invoking Popularity Dispersion Job [" + jobData.getJobName() + "]");
      try {
         initializeDependencies();
         popularityRestorationContext = (PopularityRestorationContext) jobData.getRequestContextData();
         popularityRestorationContext.setJobRequest(jobData.getJobRequest());
         popularityDispersionService.dispersePopularityInfo(popularityRestorationContext);
         jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);
      } catch (Exception ex) {
         jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         logger.error("JOB Execution failed for job -- " + jobData.getJobName());
         logger.error(ex, ex);
      }
   }

   private void initializeDependencies () throws Exception {
      popularityDispersionService = (IPopularityDispersionService) SpringContextHolder
               .getBean("popularityDispersionService");
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
      PopularityRestorationContext popularityRestorationContext = (PopularityRestorationContext) jobData
               .getRequestContextData();
      subjectParams.put(NotificationParamName.APPLICATION_NAME, popularityRestorationContext.getApplicationId() + "");
      bodyParams.put(NotificationParamName.APPLICATION_NAME, popularityRestorationContext.getApplicationId() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
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
      PopularityRestorationContext popularityRestorationContext = (PopularityRestorationContext) jobData
               .getRequestContextData();
      subjectParams.put(NotificationParamName.APPLICATION_NAME, popularityRestorationContext.getApplicationId() + "");
      bodyParams.put(NotificationParamName.APPLICATION_NAME, popularityRestorationContext.getApplicationId() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
   }
}
