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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.ac.AssetOperationDetail;
import com.execue.core.common.bean.swi.AssetDeletionContext;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.core.common.type.AnswersCatalogOperationType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationParamName;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.helper.AnswersCatalogPlatformServiceHelper;
import com.execue.platform.querydata.IAnswersCatalogManagementQueueWrapperService;
import com.execue.platform.swi.IAssetDeletionWrapperService;
import com.execue.qdata.exception.AnswersCatalogManagementQueueException;
import com.execue.qdata.service.IAnswersCatalogManagementQueueService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IAssetOperationDetailService;

public class AssetDeletionJob extends BaseLineJob {

   private static final Logger                          logger = Logger.getLogger(AssetDeletionJob.class);
   private IAssetDeletionWrapperService                 assetDeletionWrapperService;
   private IAnswersCatalogManagementQueueService        answersCatalogManagementQueueService;
   private IAnswersCatalogManagementQueueWrapperService answersCatalogManagementQueueWrapperService;
   private IAssetOperationDetailService                 assetOperationDetailService;

   @Override
   protected void process (ExecueJobData jobData) throws ExecueJobSchedulerException {
      logger.debug("Invoking on Asset Deletion Job [" + jobData.getJobName() + "]");
      AssetDeletionContext assetDeletionContext = (AssetDeletionContext) jobData.getRequestContextData();
      Long answersCatalogManagementQueueId = assetDeletionContext.getAnswersCatalogManagementQueueId();
      AssetOperationDetail assetOperationDetail = null;
      try {

         // Initialize the dependencies
         initializeDependencies();

         // Create the entry in the asset operation detail
         assetOperationDetail = AnswersCatalogPlatformServiceHelper.prepareAssetOperationDetail(assetDeletionContext
                  .getAssetId(), assetDeletionContext.getParentAssetId(), jobData.getJobRequest().getId(),
                  AnswersCatalogOperationType.ASSET_DELETION, ACManagementOperationStatusType.INPROGRESS);
         getAssetOperationDetailService().createAssetOperationDetail(assetOperationDetail);

         //Update ACMQ with INPROGRESS  status
         getAnswersCatalogManagementQueueService().updateOperationStatus(answersCatalogManagementQueueId,
                  ACManagementOperationStatusType.INPROGRESS);

         boolean successfullyCompleted = true;
         List<Long> dependantACMQIds = assetDeletionContext.getDependantACMQIds();
         if (ExecueCoreUtil.isCollectionNotEmpty(dependantACMQIds)) {
            successfullyCompleted = getAnswersCatalogManagementQueueService()
                     .isDependantAnswersCatalogManagementQueueSuccessfullyCompleted(dependantACMQIds);
         }

         if (successfullyCompleted) {
            // If all the dependent acmq ids are successfully completed, then proceed with the deletion
            assetDeletionContext.setJobRequest(jobData.getJobRequest());
            getAssetDeletionWrapperService().deleteAsset(assetDeletionContext);

            // Update the status as SUCCESS
            getAnswersCatalogManagementQueueService().updateOperationStatus(answersCatalogManagementQueueId,
                     ACManagementOperationStatusType.SUCCESSFUL);
         } else {

            // If all the dependent acmq ids are NOT successfully completed, then based on the asset type perform the necessary cleanup actions

            // If the AnswersCatalogOperationType is CUBE_DELETION
            if (AnswersCatalogOperationType.CUBE_DELETION == assetDeletionContext.getOperationType()) {
               // Check for each dependent acmq id's asset if it got created successfully, then rollback that cube creation by scheduling the delete cube job  
               List<Long> failedACMQIds = new ArrayList<Long>();
               Long userId = assetDeletionContext.getUserId();

               // Validate and create the cube deletion acmq entries for all successful dependent cube creation acmq ids
               getAnswersCatalogManagementQueueWrapperService().validateAndCreateCubeDeletionACMQEntry(
                        dependantACMQIds, failedACMQIds, userId);

               String remarks = "Could not delete the asset because of the failed dependant management queue ids: "
                        + ExecueCoreUtil.joinLongCollection(failedACMQIds);

               // Update the status as INACTIVATED and put the appropriate remarks
               getAnswersCatalogManagementQueueService().updateOperationStatusAndRemarks(
                        answersCatalogManagementQueueId, ACManagementOperationStatusType.INACTIVATED, remarks);
            }

            // If the AnswersCatalogOperationType is MART_DELETION
            if (AnswersCatalogOperationType.MART_DELETION == assetDeletionContext.getOperationType()) {

               // Update the status as INACTIVATED and put the appropriate remarks
               String remarks = "Could not delete the asset because of the failed dependant management queue ids: "
                        + ExecueCoreUtil.joinLongCollection(dependantACMQIds);

               getAnswersCatalogManagementQueueService().updateOperationStatusAndRemarks(
                        answersCatalogManagementQueueId, ACManagementOperationStatusType.INACTIVATED, remarks);
            }
         }

         // Update the status as SUCCESS
         jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);
      } catch (Exception ex) {
         jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         try {
            getAnswersCatalogManagementQueueService().updateOperationStatusAndRemarks(answersCatalogManagementQueueId,
                     ACManagementOperationStatusType.FAILED, ex.getMessage());
         } catch (AnswersCatalogManagementQueueException e) {
            throw new ExecueJobSchedulerException(e.getCode(), e);
         }
         logger.error("JOB Execution failed for job -- " + jobData.getJobName());
         logger.error(ex, ex);
      } finally {
         try {
            if (assetOperationDetail != null && assetOperationDetail.getId() != null) {
               assetOperationDetail.setAssetOperationStatus(ACManagementOperationStatusType.SUCCESSFUL);
               getAssetOperationDetailService().updateAssetOperationDetail(assetOperationDetail);
            }
         } catch (SDXException e) {
            logger.error(e, e);
         }
      }
   }

   private void initializeDependencies () throws Exception {
      setAssetDeletionWrapperService((IAssetDeletionWrapperService) SpringContextHolder
               .getBean("assetDeletionWrapperService"));
      setAnswersCatalogManagementQueueService((IAnswersCatalogManagementQueueService) SpringContextHolder
               .getBean("answersCatalogManagementQueueService"));
      setAssetOperationDetailService((IAssetOperationDetailService) SpringContextHolder
               .getBean("assetOperationDetailService"));
      setAnswersCatalogManagementQueueWrapperService((IAnswersCatalogManagementQueueWrapperService) SpringContextHolder
               .getBean("answersCatalogManagementQueueWrapperService"));

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
      AssetDeletionContext assetDeletionContext = (AssetDeletionContext) jobData.getRequestContextData();
      subjectParams.put(NotificationParamName.ASSET_NAME, assetDeletionContext.getAssetId() + "");
      bodyParams.put(NotificationParamName.ASSET_NAME, assetDeletionContext.getAssetId() + "");
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
      AssetDeletionContext assetDeletionContext = (AssetDeletionContext) jobData.getRequestContextData();
      subjectParams.put(NotificationParamName.ASSET_NAME, assetDeletionContext.getAssetId() + "");
      bodyParams.put(NotificationParamName.ASSET_NAME, assetDeletionContext.getAssetId() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
   }

   public IAssetDeletionWrapperService getAssetDeletionWrapperService () {
      return assetDeletionWrapperService;
   }

   public void setAssetDeletionWrapperService (IAssetDeletionWrapperService assetDeletionWrapperService) {
      this.assetDeletionWrapperService = assetDeletionWrapperService;
   }

   /**
    * @return the answersCatalogManagementQueueService
    */
   public IAnswersCatalogManagementQueueService getAnswersCatalogManagementQueueService () {
      return answersCatalogManagementQueueService;
   }

   /**
    * @param answersCatalogManagementQueueService the answersCatalogManagementQueueService to set
    */
   public void setAnswersCatalogManagementQueueService (
            IAnswersCatalogManagementQueueService answersCatalogManagementQueueService) {
      this.answersCatalogManagementQueueService = answersCatalogManagementQueueService;
   }

   /**
    * @return the assetOperationDetailService
    */
   public IAssetOperationDetailService getAssetOperationDetailService () {
      return assetOperationDetailService;
   }

   /**
    * @param assetOperationDetailService the assetOperationDetailService to set
    */
   public void setAssetOperationDetailService (IAssetOperationDetailService assetOperationDetailService) {
      this.assetOperationDetailService = assetOperationDetailService;
   }

   /**
    * @return the answersCatalogManagementQueueWrapperService
    */
   public IAnswersCatalogManagementQueueWrapperService getAnswersCatalogManagementQueueWrapperService () {
      return answersCatalogManagementQueueWrapperService;
   }

   /**
    * @param answersCatalogManagementQueueWrapperService the answersCatalogManagementQueueWrapperService to set
    */
   public void setAnswersCatalogManagementQueueWrapperService (
            IAnswersCatalogManagementQueueWrapperService answersCatalogManagementQueueWrapperService) {
      this.answersCatalogManagementQueueWrapperService = answersCatalogManagementQueueWrapperService;
   }
}
