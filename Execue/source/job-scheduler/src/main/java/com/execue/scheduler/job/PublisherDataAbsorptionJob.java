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

import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.publisher.PublisherDataAbsorptionContext;
import com.execue.core.common.type.AppOperationType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationParamName;
import com.execue.core.common.type.PublisherFlowOperationType;
import com.execue.swi.exception.PublishedFileException;
import com.execue.swi.exception.KDXException;
import com.execue.publisher.controller.IPublisherDataAbsorptionControllerService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;
import com.execue.swi.service.IApplicationManagementService;
import com.execue.swi.service.IPublishedFileManagementService;
import com.execue.swi.service.IPublishedFileRetrievalService;

public class PublisherDataAbsorptionJob extends BaseLineJob {

   private static final Logger                       logger = Logger.getLogger(PublisherDataAbsorptionJob.class);

   private IPublisherDataAbsorptionControllerService publisherDataAbsorptionControllerService;

   private IPublishedFileRetrievalService            publishedFileRetrievalService;

   private IPublishedFileManagementService           publishedFileManagementService;

   private IApplicationManagementService             applicationManagementService;

   protected void process (ExecueJobData jobData) throws ExecueJobSchedulerException {
      PublishedFileInfo publishedFileInfo = null;
      PublisherDataAbsorptionContext publisherDataAbsorptionContext = null;
      try {
         logger.debug("Invoking on publisherDataAbsorptionJob Job [" + jobData.getJobName() + "]");

         initializeDependencies();

         publisherDataAbsorptionContext = (PublisherDataAbsorptionContext) jobData.getRequestContextData();
         publishedFileInfo = getPublishedFileRetrievalService().getPublishedFileInfoById(
                  publisherDataAbsorptionContext.getFileInfoId());
         publisherDataAbsorptionContext.setJobRequest(jobData.getJobRequest());

         publishedFileInfo.setCurrentOperationStatus(JobStatus.INPROGRESS);
         publishedFileInfo.setCurrentOperation(PublisherFlowOperationType.ABSORPTION_AND_DATA_ANALYSIS);
         getPublishedFileManagementService().updatePublishedFileInfo(publishedFileInfo);

         publisherDataAbsorptionControllerService.publishData(publisherDataAbsorptionContext);

         publishedFileInfo = getPublishedFileRetrievalService().getPublishedFileInfoById(publishedFileInfo.getId());
         publishedFileInfo.setCurrentOperationStatus(JobStatus.SUCCESS);
         getPublishedFileManagementService().updatePublishedFileInfo(publishedFileInfo);

         jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);
         if (publisherDataAbsorptionContext.getApplicationId() != null) {
            getApplicationManagementService().updateApplicationOperationDetails(
                     publisherDataAbsorptionContext.getApplicationId(), AppOperationType.ANALYZED,
                     jobData.getJobRequest().getId(), JobStatus.SUCCESS);
         }
      } catch (Exception e) {
         jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         logger.error("JOB Execution failed for job -- " + jobData.getJobName());
         try {
            publishedFileInfo = getPublishedFileRetrievalService().getPublishedFileInfoById(
                     publisherDataAbsorptionContext.getFileInfoId());
            publishedFileInfo.setCurrentOperationStatus(JobStatus.FAILURE);
            getPublishedFileManagementService().updatePublishedFileInfo(publishedFileInfo);
            if (publisherDataAbsorptionContext.getApplicationId() != null) {
               getApplicationManagementService().updateApplicationOperationDetails(
                        publisherDataAbsorptionContext.getApplicationId(), AppOperationType.ANALYZING,
                        jobData.getJobRequest().getId(), JobStatus.FAILURE);
            }
         } catch (PublishedFileException exp) {
            logger.error("JOB Execution failed for job -- [" + exp.getMessage() + "]" + jobData.getJobName());
            e.printStackTrace();
         } catch (KDXException kdxException) {
            kdxException.printStackTrace();
         }
         logger.error(e, e);
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
      PublisherDataAbsorptionContext publisherDataAbsorptionContext = (PublisherDataAbsorptionContext) jobData
               .getRequestContextData();
      subjectParams.put(NotificationParamName.FILE_NAME, publisherDataAbsorptionContext.getFileName());
      bodyParams.put(NotificationParamName.FILE_NAME, publisherDataAbsorptionContext.getFileName());
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
      PublisherDataAbsorptionContext publisherDataAbsorptionContext = (PublisherDataAbsorptionContext) jobData
               .getRequestContextData();
      subjectParams.put(NotificationParamName.FILE_NAME, publisherDataAbsorptionContext.getFileName());
      bodyParams.put(NotificationParamName.FILE_NAME, publisherDataAbsorptionContext.getFileName());
   }

   private void initializeDependencies () throws Exception {
      publisherDataAbsorptionControllerService = (IPublisherDataAbsorptionControllerService) SpringContextHolder
               .getBean("publisherDataAbsorptionControllerService");
      publishedFileRetrievalService = (IPublishedFileRetrievalService) SpringContextHolder
               .getBean("publishedFileRetrievalService");
      publishedFileManagementService = (IPublishedFileManagementService) SpringContextHolder
               .getBean("publishedFileManagementService");
      applicationManagementService = (IApplicationManagementService) SpringContextHolder
               .getBean("applicationManagementService");
   }

   public IApplicationManagementService getApplicationManagementService () {
      return applicationManagementService;
   }

   public void setApplicationManagementService (IApplicationManagementService applicationManagementService) {
      this.applicationManagementService = applicationManagementService;
   }

   public IPublisherDataAbsorptionControllerService getPublisherDataAbsorptionControllerService () {
      return publisherDataAbsorptionControllerService;
   }

   public void setPublisherDataAbsorptionControllerService (
            IPublisherDataAbsorptionControllerService publisherDataAbsorptionControllerService) {
      this.publisherDataAbsorptionControllerService = publisherDataAbsorptionControllerService;
   }

   public IPublishedFileRetrievalService getPublishedFileRetrievalService () {
      return publishedFileRetrievalService;
   }

   public void setPublishedFileRetrievalService (IPublishedFileRetrievalService publishedFileRetrievalService) {
      this.publishedFileRetrievalService = publishedFileRetrievalService;
   }

   public IPublishedFileManagementService getPublishedFileManagementService () {
      return publishedFileManagementService;
   }

   public void setPublishedFileManagementService (IPublishedFileManagementService publishedFileManagementService) {
      this.publishedFileManagementService = publishedFileManagementService;
   }

}
