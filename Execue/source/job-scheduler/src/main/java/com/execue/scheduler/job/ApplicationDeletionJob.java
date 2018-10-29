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

import com.execue.core.common.bean.swi.ApplicationDeletionContext;
import com.execue.core.common.type.AppOperationType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationParamName;
import com.execue.platform.swi.IApplicationDeletionWrapperService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IApplicationManagementService;

public class ApplicationDeletionJob extends BaseLineJob {

   private static final Logger                log = Logger.getLogger(ApplicationDeletionJob.class);

   private IApplicationManagementService      applicationManagementService;
   private IApplicationDeletionWrapperService applicationDeletionWrapperService;

   @Override
   protected void process (ExecueJobData jobData) throws ExecueJobSchedulerException {
      ApplicationDeletionContext applicationDeletionContext = null;

      try {
         applicationDeletionContext = (ApplicationDeletionContext) jobData.getRequestContextData();
         log.debug("Invoking on Application Deletion Job [" + jobData.getJobName() + "]");
         initializeDependencies();
         applicationDeletionContext.setJobRequest(jobData.getJobRequest());
         getApplicationDeletionWrapperService().deleteApplication(applicationDeletionContext);
         jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);
         if (applicationDeletionContext.getApplicationId() != null) {
            getApplicationManagementService().updateApplicationOperationDetails(
                     applicationDeletionContext.getApplicationId(), AppOperationType.DELETED,
                     jobData.getJobRequest().getId(), JobStatus.SUCCESS);
         }
      } catch (Exception e) {
         log.error("JOB Execution failed for job -- " + jobData.getJobName());
         jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         if (applicationDeletionContext.getApplicationId() != null) {
            try {
               getApplicationManagementService().updateApplicationOperationDetails(
                        applicationDeletionContext.getApplicationId(), AppOperationType.DELETING,
                        jobData.getJobRequest().getId(), JobStatus.FAILURE);
            } catch (KDXException kdxException) {
               kdxException.printStackTrace();
            }
         }
         log.error(e, e);
      }
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
      ApplicationDeletionContext applicationDeletionContext = (ApplicationDeletionContext) jobData
               .getRequestContextData();

      subjectParams.put(NotificationParamName.APPLICATION_NAME, applicationDeletionContext.getApplicationId() + "");
      bodyParams.put(NotificationParamName.APPLICATION_NAME, applicationDeletionContext.getApplicationId() + "");
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
      ApplicationDeletionContext applicationDeletionContext = (ApplicationDeletionContext) jobData
               .getRequestContextData();

      subjectParams.put(NotificationParamName.APPLICATION_NAME, applicationDeletionContext.getApplicationId() + "");
      bodyParams.put(NotificationParamName.APPLICATION_NAME, applicationDeletionContext.getApplicationId() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");

   }

   private void initializeDependencies () throws Exception {
      applicationDeletionWrapperService = (IApplicationDeletionWrapperService) SpringContextHolder
               .getBean("applicationDeletionWrapperService");
      applicationManagementService = (IApplicationManagementService) SpringContextHolder
               .getBean("applicationManagementService");
   }

   public IApplicationManagementService getApplicationManagementService () {
      return applicationManagementService;
   }

   public void setApplicationManagementService (IApplicationManagementService applicationManagementService) {
      this.applicationManagementService = applicationManagementService;
   }

   public IApplicationDeletionWrapperService getApplicationDeletionWrapperService () {
      return applicationDeletionWrapperService;
   }

   public void setApplicationDeletionWrapperService (
            IApplicationDeletionWrapperService applicationDeletionWrapperService) {
      this.applicationDeletionWrapperService = applicationDeletionWrapperService;
   }

}
