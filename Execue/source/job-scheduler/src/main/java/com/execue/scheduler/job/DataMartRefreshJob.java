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

import com.execue.ac.bean.MartRefreshOutputInfo;
import com.execue.ac.service.IAnswersCatalogUpdationService;
import com.execue.core.common.bean.ac.AnswersCatalogUpdationContext;
import com.execue.core.common.bean.ac.AssetOperationDetail;
import com.execue.core.common.bean.entity.AnswersCatalogContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.core.common.type.AnswersCatalogOperationType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationParamName;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.platform.helper.AnswersCatalogPlatformServiceHelper;
import com.execue.qdata.exception.AnswersCatalogManagementQueueException;
import com.execue.qdata.service.IAnswersCatalogManagementQueueService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IAssetOperationDetailService;

/**
 * @author Vishay
 */
public class DataMartRefreshJob extends BaseLineJob {

   private static final Logger                   logger = Logger.getLogger(DataMartRefreshJob.class);

   private IAnswersCatalogUpdationService        answersCatalogUpdationService;
   private IAnswersCatalogManagementQueueService answersCatalogManagementQueueService;
   private IAssetOperationDetailService          assetOperationDetailService;

   @Override
   public void process (ExecueJobData jobData) throws ExecueJobSchedulerException {
      logger.debug("Invoking Mart Refresh Job [" + jobData.getJobName() + "]");
      AnswersCatalogUpdationContext answersCatalogUpdationContext = (AnswersCatalogUpdationContext) jobData
               .getRequestContextData();
      Long answersCatalogManagementQueueId = answersCatalogUpdationContext.getAnswersCatalogManagementQueueId();
      AssetOperationDetail assetOperationDetail = null;
      try {
         initializeDependencies();
         assetOperationDetail = AnswersCatalogPlatformServiceHelper.prepareAssetOperationDetail(
                  answersCatalogUpdationContext.getExistingAssetId(), answersCatalogUpdationContext.getParentAssetId(),
                  jobData.getJobRequest().getId(), AnswersCatalogOperationType.MART_REFRESH,
                  ACManagementOperationStatusType.INPROGRESS);
         getAssetOperationDetailService().createAssetOperationDetail(assetOperationDetail);
         AnswersCatalogContext existingAnswersCatalogContext = getJobDataService().getAnswersCatalogContextByAssetId(
                  answersCatalogUpdationContext.getExistingAssetId());
         answersCatalogUpdationContext.setJobRequest(jobData.getJobRequest());

         //update ACMQ with INPROGRESS  status
         getAnswersCatalogManagementQueueService().updateOperationStatus(answersCatalogManagementQueueId,
                  ACManagementOperationStatusType.INPROGRESS);
         MartRefreshOutputInfo martRefreshOutputInfo = getAnswersCatalogUpdationService().martRefresh(
                  answersCatalogUpdationContext);
         if (martRefreshOutputInfo != null) {
            if (martRefreshOutputInfo.isRefreshSuccessful()) {
               Asset cubeAsset = martRefreshOutputInfo.getMartCreationOutputInfo().getMartCreationInputInfo()
                        .getMartCreationPopulatedContext().getTargetAsset();
               if (logger.isDebugEnabled()) {
                  logger.debug("New Mart Asset Id " + cubeAsset.getId());
               }
               jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);

               // clone the existing ac context and then set the needed parameters and create it, as
               //  during refresh cycle this context got deleted.
               AnswersCatalogContext refreshedAnswersCatalogContext = ExecueBeanCloneUtil
                        .cloneAnswersCatalogContext(existingAnswersCatalogContext);
               refreshedAnswersCatalogContext.setAssetId(cubeAsset.getId());
               refreshedAnswersCatalogContext.setLatestOperation(AnswersCatalogOperationType.getType(jobData
                        .getJobRequest().getJobType().toString()));
               getJobDataService().createAnswersCatalogContext(refreshedAnswersCatalogContext);

            }
            //update ACMQ with SUCCESSFUL  status
            getAnswersCatalogManagementQueueService().updateOperationStatus(answersCatalogManagementQueueId,
                     ACManagementOperationStatusType.SUCCESSFUL);
         } else {
            if (logger.isDebugEnabled()) {
               logger.debug("Failed to refresh Mart");
            }
            jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
            //update ACMQ with FAILED  status
            getAnswersCatalogManagementQueueService().updateOperationStatus(answersCatalogManagementQueueId,
                     ACManagementOperationStatusType.FAILED);
         }
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
      setAnswersCatalogUpdationService((IAnswersCatalogUpdationService) SpringContextHolder
               .getBean("answersCatalogUpdationService"));
      setAnswersCatalogManagementQueueService((IAnswersCatalogManagementQueueService) SpringContextHolder
               .getBean("answersCatalogManagementQueueService"));
      setAssetOperationDetailService((IAssetOperationDetailService) SpringContextHolder
               .getBean("assetOperationDetailService"));
   }

   @Override
   protected void prepareInitiationMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
   }

   @Override
   protected void prepareCompletionMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
   }

   public IAnswersCatalogUpdationService getAnswersCatalogUpdationService () {
      return answersCatalogUpdationService;
   }

   public void setAnswersCatalogUpdationService (IAnswersCatalogUpdationService answersCatalogUpdationService) {
      this.answersCatalogUpdationService = answersCatalogUpdationService;
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

}
