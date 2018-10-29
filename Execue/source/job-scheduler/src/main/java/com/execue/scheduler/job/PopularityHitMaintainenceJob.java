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

import com.execue.core.common.bean.swi.PopularityHitMaintenanceContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationParamName;
import com.execue.platform.swi.IPopularityPopulationService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;

public class PopularityHitMaintainenceJob extends BaseLineJob {

   private static final Logger          logger = Logger.getLogger(PopularityHitMaintainenceJob.class);
   private IPopularityPopulationService popularityPopulationService;

   @Override
   protected void process (ExecueJobData jobData) throws ExecueJobSchedulerException {
      PopularityHitMaintenanceContext popularityHitMaintenanceContext = null;
      try {
         popularityHitMaintenanceContext = (PopularityHitMaintenanceContext) jobData.getRequestContextData();
         logger.debug("Invoking on Popualrity Hit Maintenance Job [" + jobData.getJobName() + "]");
         initializeDependencies();
         popularityHitMaintenanceContext.setJobRequest(jobData.getJobRequest());
         getPopularityPopulationService().updateTermsBasedOnPopularity(popularityHitMaintenanceContext);
         jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);
      } catch (Exception ex) {
         jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         logger.error("JOB Execution failed for job -- " + jobData.getJobName());
         logger.error(ex, ex);
      }
   }

   private void initializeDependencies () throws Exception {
      popularityPopulationService = (IPopularityPopulationService) SpringContextHolder
               .getBean("popularityPopulationService");
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
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
   }

   public IPopularityPopulationService getPopularityPopulationService () {
      return popularityPopulationService;
   }

   public void setPopularityPopulationService (IPopularityPopulationService popularityPopulationService) {
      this.popularityPopulationService = popularityPopulationService;
   }
}
