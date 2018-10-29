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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.swi.ApplicationScopeIndexDetail;
import com.execue.core.common.bean.swi.EASIndexPeriodicContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationParamName;
import com.execue.platform.swi.IEASIndexPopulationService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;
import com.execue.swi.service.IEASIndexService;

/**
 * @author MurthySN
 * @since 16/02/2010
 */
public class EASIndexPeriodicJob extends BaseLineJob {

   private static final Logger        logger = Logger.getLogger(EASIndexPeriodicJob.class);
   private IEASIndexPopulationService easIndexPopulationService;
   private IEASIndexService           easIndexService;

   @Override
   protected void process (ExecueJobData jobData) throws ExecueJobSchedulerException {

      try {
         logger.debug("Invoking on EASIndexPeriodic Job [" + jobData.getJobName() + "]");
         initializeDependencies();
         Calendar calendar = new GregorianCalendar();
         calendar.add(Calendar.DAY_OF_MONTH, -7);
         Date lastRefreshDate = calendar.getTime();
         List<ApplicationScopeIndexDetail> aesList = getEasIndexService().getApplicationScopeIndexDetails(
                  lastRefreshDate);
         for (ApplicationScopeIndexDetail applicationScopeIndexDetail : aesList) {
            EASIndexPeriodicContext easIndexPeriodicContext = new EASIndexPeriodicContext();
            easIndexPeriodicContext.setJobRequest(jobData.getJobRequest());
            easIndexPeriodicContext.setApplicationId(applicationScopeIndexDetail.getAppId());
            getEasIndexPopulationService().populateEASIndex(easIndexPeriodicContext);
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
      easIndexService = (IEASIndexService) SpringContextHolder.getBean("easIndexService");
      easIndexPopulationService = (IEASIndexPopulationService) SpringContextHolder.getBean("easIndexPopulationService");
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

   public IEASIndexPopulationService getEasIndexPopulationService () {
      return easIndexPopulationService;
   }

   public void setEasIndexPopulationService (IEASIndexPopulationService easIndexPopulationService) {
      this.easIndexPopulationService = easIndexPopulationService;
   }

   public IEASIndexService getEasIndexService () {
      return easIndexService;
   }

   public void setEasIndexService (IEASIndexService easIndexService) {
      this.easIndexService = easIndexService;
   }

}
