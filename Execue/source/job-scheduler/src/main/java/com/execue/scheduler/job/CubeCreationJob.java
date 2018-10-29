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

import com.execue.ac.bean.CubeCreationOutputInfo;
import com.execue.ac.service.ICubeCreationService;
import com.execue.core.common.bean.ac.AssetOperationDetail;
import com.execue.core.common.bean.ac.CubeCreationContext;
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

/**
 * @author Vishay
 */
public class CubeCreationJob extends BaseLineJob {

   private static final Logger                   logger = Logger.getLogger(CubeCreationJob.class);

   private ICubeCreationService                  cubeCreationService;
   private IAnswersCatalogManagementQueueService answersCatalogManagementQueueService;
   private IAssetOperationDetailService          assetOperationDetailService;

   @Override
   public void process (ExecueJobData jobData) throws ExecueJobSchedulerException {
      logger.debug("Invoking Cube Creation Job [" + jobData.getJobName() + "]");
      CubeCreationContext cubeCreationContext = (CubeCreationContext) jobData.getRequestContextData();
      Long answersCatalogManagementQueueId = cubeCreationContext.getAnswersCatalogManagementQueueId();
      AssetOperationDetail assetOperationDetail = null;
      try {
         initializeDependencies();
         assetOperationDetail = AnswersCatalogPlatformServiceHelper.prepareAssetOperationDetail(null,
                  cubeCreationContext.getSourceAsset().getId(), jobData.getJobRequest().getId(),
                  AnswersCatalogOperationType.CUBE_CREATION, ACManagementOperationStatusType.INPROGRESS);
         getAssetOperationDetailService().createAssetOperationDetail(assetOperationDetail);

         cubeCreationContext.setJobRequest(jobData.getJobRequest());
         //update ACMQ with INPROGRESS  status
         getAnswersCatalogManagementQueueService().updateOperationStatus(answersCatalogManagementQueueId,
                  ACManagementOperationStatusType.INPROGRESS);
         CubeCreationOutputInfo cubeCreationOutputInfo = getCubeCreationService().cubeCreation(cubeCreationContext);
         if (cubeCreationOutputInfo != null) {
            Asset cubeAsset = cubeCreationOutputInfo.getCubeCreationInputInfo().getCubeCreationPopulatedContext()
                     .getTargetAsset();
            if (logger.isDebugEnabled()) {
               logger.debug("Cube Asset Id " + cubeAsset.getId());
            }

            if (cubeCreationOutputInfo.isCreationSuccessful()) {
               jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);
               // Creating Answer Catalog Context Entry.
               AnswersCatalogContext answersCatalogContext = new AnswersCatalogContext();
               answersCatalogContext.setContextData(jobData.getJobRequest().getRequestData());
               answersCatalogContext.setAssetId(cubeAsset.getId());
               answersCatalogContext.setParentAssetId(cubeCreationContext.getSourceAsset().getId());
               answersCatalogContext.setUserId(getCoreConfigurationService().getAdminUserId());
               answersCatalogContext.setLatestOperation(AnswersCatalogOperationType.getType(jobData.getJobRequest()
                        .getJobType().toString()));
               getJobDataService().createAnswersCatalogContext(answersCatalogContext);
            }

            //update ACMQ with SUCCESSFUL  status and the cube asset id
            getAnswersCatalogManagementQueueService().updateOperationStatusAndAssetId(answersCatalogManagementQueueId,
                     ACManagementOperationStatusType.SUCCESSFUL, cubeAsset.getId());

         } else {
            if (logger.isDebugEnabled()) {
               logger.debug("Failed to create Cube");
            }
            jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
            //update ACMQ with FAILED  status
            getAnswersCatalogManagementQueueService().updateOperationStatus(answersCatalogManagementQueueId,
                     ACManagementOperationStatusType.FAILED);
         }
      } catch (Exception ex) {
         try {
            jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
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
      setCubeCreationService((ICubeCreationService) SpringContextHolder.getBean("cubeCreationService"));
      setAnswersCatalogManagementQueueService((IAnswersCatalogManagementQueueService) SpringContextHolder
               .getBean("answersCatalogManagementQueueService"));
      setCoreConfigurationService((ICoreConfigurationService) SpringContextHolder.getBean("coreConfigurationService"));
      setAssetOperationDetailService((IAssetOperationDetailService) SpringContextHolder
               .getBean("assetOperationDetailService"));

   }

   @Override
   protected void prepareInitiationMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      CubeCreationContext cubeCreationContext = (CubeCreationContext) jobData.getRequestContextData();
      subjectParams.put(NotificationParamName.ASSET_NAME, cubeCreationContext.getSourceAsset().getName() + "");
      bodyParams.put(NotificationParamName.ASSET_NAME, cubeCreationContext.getSourceAsset().getName() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
   }

   @Override
   protected void prepareCompletionMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      CubeCreationContext cubeCreationContext = (CubeCreationContext) jobData.getRequestContextData();
      subjectParams.put(NotificationParamName.ASSET_NAME, cubeCreationContext.getTargetAsset().getName() + "");
      bodyParams.put(NotificationParamName.ASSET_NAME, cubeCreationContext.getTargetAsset().getName() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
   }

   /**
    * @return the cubeCreationService
    */
   public ICubeCreationService getCubeCreationService () {
      return cubeCreationService;
   }

   /**
    * @param cubeCreationService the cubeCreationService to set
    */
   public void setCubeCreationService (ICubeCreationService cubeCreationService) {
      this.cubeCreationService = cubeCreationService;
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
