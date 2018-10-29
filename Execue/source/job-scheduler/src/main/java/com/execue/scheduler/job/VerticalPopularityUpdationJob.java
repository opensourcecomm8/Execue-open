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

import com.execue.core.common.bean.swi.VerticalPopularityUpdationContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationParamName;
import com.execue.platform.swi.IVerticalPopularityUpdationService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;

public class VerticalPopularityUpdationJob extends BaseLineJob {

   private static final Logger                logger = Logger.getLogger(VerticalPopularityUpdationJob.class);
   private IVerticalPopularityUpdationService verticalPopularityUpdationService;

   @Override
   protected void process (ExecueJobData jobData) throws ExecueJobSchedulerException {
      try {
         if (logger.isDebugEnabled()) {
            logger.debug("Job [ " + jobData.getJobName() + " ] invoked.");
         }
         initializeDependencies();
         VerticalPopularityUpdationContext verticalPopularityUpdationContext = new VerticalPopularityUpdationContext();
         verticalPopularityUpdationContext.setJobRequest(jobData.getJobRequest());
         getVerticalPopularityUpdationService().populateVerticalPopularityJob(jobData.getJobRequest());
         jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);
      } catch (Exception e) {
         logger.error(e, e);
         logger.error("JOB Execution failed for job -- " + jobData.getJobName());
         jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         e.printStackTrace();
      }

   }

   private void initializeDependencies () throws Exception {
      verticalPopularityUpdationService = (IVerticalPopularityUpdationService) SpringContextHolder
               .getBean("verticalPopularityUpdationService");
   }

   @Override
   protected void prepareInitiationMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      subjectParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
   }

   @Override
   protected void prepareCompletionMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      subjectParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
   }

   public IVerticalPopularityUpdationService getVerticalPopularityUpdationService () {
      return verticalPopularityUpdationService;
   }

   public void setVerticalPopularityUpdationService (
            IVerticalPopularityUpdationService verticalPopularityUpdationService) {
      this.verticalPopularityUpdationService = verticalPopularityUpdationService;
   }

}
