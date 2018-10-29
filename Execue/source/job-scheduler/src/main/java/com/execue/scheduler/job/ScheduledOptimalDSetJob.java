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
package com.execue.scheduler.job;

import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetWrapperService;
import com.execue.core.common.bean.optimaldset.OptimalDSetContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationParamName;
import com.execue.core.common.type.OperationRequestLevel;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;

/**
 * @author jitendra
 *
 */
public class ScheduledOptimalDSetJob extends BaseLineJob {

   private Logger                     logger = Logger.getLogger(OptimalDSetJob.class);
   private IOptimalDSetWrapperService optimalDSetWrapperService;
   private ICoreConfigurationService  coreConfigurationService;

   @Override
   protected void process (ExecueJobData jobData) throws ExecueJobSchedulerException {
      try {
         logger.debug("Invoking Scheduled Optmal DSet Job [" + jobData.getJobName() + "]");
         initializeDependencies();
         OptimalDSetContext optimalDSetContext = new OptimalDSetContext();
         optimalDSetContext.setOperationRequestLevel(OperationRequestLevel.SYSTEM);
         optimalDSetContext.setUserId(getCoreConfigurationService().getAdminUserId());
         optimalDSetContext.setJobRequest(jobData.getJobRequest());
         getOptimalDSetWrapperService().executeOptimalDSet(optimalDSetContext);
         jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);
      } catch (Exception ex) {
         jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         logger.error("JOB Execution failed for job -- " + jobData.getJobName());
         logger.error(ex, ex);
      }

   }

   private void initializeDependencies () throws Exception {
      setOptimalDSetWrapperService((IOptimalDSetWrapperService) SpringContextHolder
               .getBean("optimalDSetWrapperService"));
      setCoreConfigurationService((ICoreConfigurationService) SpringContextHolder.getBean("coreConfigurationService"));
   }

   @Override
   protected void prepareInitiationMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      //TODO-JT- Need to see if need to add notification message. 
   }

   @Override
   protected void prepareCompletionMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      //TODO-JT- Need to see if need to add notification message. 

   }

   /**
    * @return the optimalDSetWrapperService
    */
   public IOptimalDSetWrapperService getOptimalDSetWrapperService () {
      return optimalDSetWrapperService;
   }

   /**
    * @param optimalDSetWrapperService the optimalDSetWrapperService to set
    */
   public void setOptimalDSetWrapperService (IOptimalDSetWrapperService optimalDSetWrapperService) {
      this.optimalDSetWrapperService = optimalDSetWrapperService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }
}
