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

import com.execue.core.common.bean.swi.PublishAssetContext;
import com.execue.core.common.bean.swi.PublishContext;
import com.execue.core.common.type.AppOperationType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationParamName;
import com.execue.platform.swi.IPublishService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IApplicationManagementService;

/**
 * @author john
 */
public class PublishAssetJob extends BaseLineJob {

   private static final Logger           logger = Logger.getLogger(BusinessModelPreparationJob.class);
   private IPublishService               publishService;
   private IApplicationManagementService applicationManagementService;

   @Override
   protected void process (ExecueJobData jobData) throws ExecueJobSchedulerException {
      PublishContext publishContext = null;
      logger.debug("Invoking on business Model Preparation Job [" + jobData.getJobName() + "]");
      try {
         initializeDependencies();
         publishContext = (PublishAssetContext) jobData.getRequestContextData();
         publishContext.setJobRequest(jobData.getJobRequest());
         jobData.getJobRequest().setJobStatus(JobStatus.INPROGRESS);

         /* businessModelPreparationService.prepareBusinessModel(businessModelPreparationContext); */
         publishService.publishAppHierarchy(publishContext);
         jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);
         if (publishContext.getApplicationId() != null) {
            getApplicationManagementService().updateApplicationOperationDetails(publishContext.getApplicationId(),
                     AppOperationType.PUBLISHED, jobData.getJobRequest().getId(), JobStatus.SUCCESS);
         }
      } catch (Exception ex) {
         jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         if (publishContext.getApplicationId() != null) {
            try {
               getApplicationManagementService().updateApplicationOperationDetails(publishContext.getApplicationId(),
                        AppOperationType.PUBLISHING, jobData.getJobRequest().getId(), JobStatus.FAILURE);
            } catch (KDXException kdxException) {
               kdxException.printStackTrace();
            }
         }
         logger.error("JOB Execution failed for job -- " + jobData.getJobName());
         logger.error(ex, ex);
      }
   }

   private void initializeDependencies () throws Exception {
      publishService = (IPublishService) SpringContextHolder.getBean("publishService");
      applicationManagementService = (IApplicationManagementService) SpringContextHolder
               .getBean("applicationManagementService");
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
      PublishAssetContext publishAssetContext = (PublishAssetContext) jobData.getRequestContextData();
      subjectParams.put(NotificationParamName.APPLICATION_NAME, publishAssetContext.getApplicationId() + "");
      subjectParams.put(NotificationParamName.ASSET_NAME, publishAssetContext.getAssetId() + "");
      bodyParams.put(NotificationParamName.APPLICATION_NAME, publishAssetContext.getApplicationId() + "");
      bodyParams.put(NotificationParamName.ASSET_NAME, publishAssetContext.getAssetId() + "");
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
      PublishAssetContext publishAssetContext = (PublishAssetContext) jobData.getRequestContextData();
      subjectParams.put(NotificationParamName.APPLICATION_NAME, publishAssetContext.getApplicationId() + "");
      subjectParams.put(NotificationParamName.ASSET_NAME, publishAssetContext.getAssetId() + "");
      bodyParams.put(NotificationParamName.APPLICATION_NAME, publishAssetContext.getApplicationId() + "");
      bodyParams.put(NotificationParamName.ASSET_NAME, publishAssetContext.getAssetId() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");

   }

   public IApplicationManagementService getApplicationManagementService () {
      return applicationManagementService;
   }

   public void setApplicationManagementService (IApplicationManagementService applicationManagementService) {
      this.applicationManagementService = applicationManagementService;
   }
}
