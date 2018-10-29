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

import com.execue.ac.bean.MartCreationOutputInfo;
import com.execue.ac.service.IDataMartCreationService;
import com.execue.core.common.bean.ac.AssetOperationDetail;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.AnswersCatalogContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.core.common.type.AnswersCatalogOperationType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationParamName;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.platform.helper.AnswersCatalogPlatformServiceHelper;
import com.execue.qdata.exception.AnswersCatalogManagementQueueException;
import com.execue.qdata.service.IAnswersCatalogManagementQueueService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IAssetOperationDetailService;

public class DataMartCreationJob extends BaseLineJob {

   private static final Logger                   logger = Logger.getLogger(DataMartCreationJob.class);

   private IDataMartCreationService              dataMartCreationService;
   private IAnswersCatalogManagementQueueService answersCatalogManagementQueueService;
   private IAssetOperationDetailService          assetOperationDetailService;

   @Override
   public void process (ExecueJobData jobData) throws ExecueJobSchedulerException {
      logger.debug("Invoking Mart Creation Job [" + jobData.getJobName() + "]");
      MartCreationContext martCreationContext = (MartCreationContext) jobData.getRequestContextData();
      Long answersCatalogManagementQueueId = martCreationContext.getAnswersCatalogManagementQueueId();
      AssetOperationDetail assetOperationDetail = null;
      try {
         initializeDependencies();
         //TODO-JT- In case of mart creation adding parent asset Id(since mart asset is not created yet) in AssetOperationDetail Table,but in case of update and refresh we are adding mart assetId(existingAssetId).Need to clarify.
         assetOperationDetail = AnswersCatalogPlatformServiceHelper.prepareAssetOperationDetail(null,
                  martCreationContext.getSourceAsset().getId(), jobData.getJobRequest().getId(),
                  AnswersCatalogOperationType.MART_CREATION, ACManagementOperationStatusType.INPROGRESS);

         getAssetOperationDetailService().createAssetOperationDetail(assetOperationDetail);
         martCreationContext.setJobRequest(jobData.getJobRequest());
         //update ACMQ with INPROGRESS  status
         getAnswersCatalogManagementQueueService().updateOperationStatus(answersCatalogManagementQueueId,
                  ACManagementOperationStatusType.INPROGRESS);
         MartCreationOutputInfo martCreationOutputInfo = dataMartCreationService.dataMartCreation(martCreationContext);
         if (martCreationOutputInfo != null) {
            Asset martAsset = martCreationOutputInfo.getMartCreationInputInfo().getMartCreationPopulatedContext()
                     .getTargetAsset();
            if (logger.isDebugEnabled()) {
               logger.debug("Mart Asset Id " + martAsset.getId());
            }
            if (martCreationOutputInfo.isCreationSuccessful()) {
               jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);
               // Creating Answer Catalog Context Entry.
               AnswersCatalogContext answersCatalogContext = new AnswersCatalogContext();
               answersCatalogContext.setContextData(jobData.getJobRequest().getRequestData());
               answersCatalogContext.setAssetId(martCreationContext.getTargetAsset().getId());
               answersCatalogContext.setParentAssetId(martCreationContext.getSourceAsset().getId());
               answersCatalogContext.setUserId(getCoreConfigurationService().getAdminUserId());
               answersCatalogContext.setLatestOperation(AnswersCatalogOperationType.getType(jobData.getJobRequest()
                        .getJobType().toString()));
               getJobDataService().createAnswersCatalogContext(answersCatalogContext);
            }

            //update ACMQ with SUCCESSFUL  status and the mart asset id
            getAnswersCatalogManagementQueueService().updateOperationStatusAndAssetId(answersCatalogManagementQueueId,
                     ACManagementOperationStatusType.SUCCESSFUL, martAsset.getId());

         } else {
            if (logger.isDebugEnabled()) {
               logger.debug("Failed to create Mart");
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
      setDataMartCreationService((IDataMartCreationService) SpringContextHolder.getBean("dataMartCreationService"));
      setAnswersCatalogManagementQueueService((IAnswersCatalogManagementQueueService) SpringContextHolder
               .getBean("answersCatalogManagementQueueService"));
      setCoreConfigurationService((ICoreConfigurationService) SpringContextHolder.getBean("coreConfigurationService"));
      setAssetOperationDetailService((IAssetOperationDetailService) SpringContextHolder
               .getBean("assetOperationDetailService"));
   }

   @Override
   protected void prepareInitiationMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      MartCreationContext martCreationContext = (MartCreationContext) jobData.getRequestContextData();
      subjectParams.put(NotificationParamName.ASSET_NAME, martCreationContext.getSourceAsset().getId() + "");
      bodyParams.put(NotificationParamName.ASSET_NAME, martCreationContext.getSourceAsset().getId() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
   }

   @Override
   protected void prepareCompletionMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      MartCreationContext martCreationContext = (MartCreationContext) jobData.getRequestContextData();
      subjectParams.put(NotificationParamName.ASSET_NAME, martCreationContext.getTargetAsset().getId() + "");
      bodyParams.put(NotificationParamName.ASSET_NAME, martCreationContext.getTargetAsset().getId() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
   }

   public IDataMartCreationService getDataMartCreationService () {
      return dataMartCreationService;
   }

   public void setDataMartCreationService (IDataMartCreationService dataMartCreationService) {
      this.dataMartCreationService = dataMartCreationService;
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
