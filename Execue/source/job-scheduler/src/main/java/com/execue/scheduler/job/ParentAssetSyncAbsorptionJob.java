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

import org.apache.log4j.Logger;

import com.execue.core.common.bean.ac.AssetOperationDetail;
import com.execue.core.common.bean.entity.AnswersCatalogContext;
import com.execue.core.common.bean.swi.AssetSyncAbsorptionContext;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.core.common.type.AnswersCatalogOperationType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.platform.helper.AnswersCatalogPlatformServiceHelper;
import com.execue.platform.swi.operation.synchronization.IParentAssetSyncAbsorptionService;
import com.execue.qdata.exception.AnswersCatalogManagementQueueException;
import com.execue.qdata.service.IAnswersCatalogManagementQueueService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IAssetOperationDetailService;

/**
 * @author Nitesh
 *
 */
public class ParentAssetSyncAbsorptionJob extends BaseLineJob {

   private static final Logger                   logger = Logger.getLogger(ParentAssetSyncAbsorptionJob.class);

   private IParentAssetSyncAbsorptionService     parentAssetSyncAbsorptionService;
   private IAnswersCatalogManagementQueueService answersCatalogManagementQueueService;
   private IAssetOperationDetailService          assetOperationDetailService;

   /* (non-Javadoc)
    * @see com.execue.scheduler.job.BaseLineJob#process(com.execue.scheduler.jobdata.ExecueJobData)
    */
   @Override
   protected void process (ExecueJobData jobData) throws ExecueJobSchedulerException {
      logger.debug("Invoking Parent Asset Sync Absorption Job [" + jobData.getJobName() + "]");
      AssetSyncAbsorptionContext parentAssetSyncAbsorptionContext = (AssetSyncAbsorptionContext) jobData
               .getRequestContextData();
      Long answersCatalogManagementQueueId = parentAssetSyncAbsorptionContext.getAnswersCatalogManagementQueueId();
      AssetOperationDetail assetOperationDetail = null;
      try {
         initializeDependencies();
         assetOperationDetail = AnswersCatalogPlatformServiceHelper.prepareAssetOperationDetail(
                  parentAssetSyncAbsorptionContext.getAssetId(), null, jobData.getJobRequest().getId(),
                  AnswersCatalogOperationType.PARENT_ASSET_SYNC_ABSORPTION, ACManagementOperationStatusType.INPROGRESS);
         getAssetOperationDetailService().createAssetOperationDetail(assetOperationDetail);

         parentAssetSyncAbsorptionContext.setJobRequest(jobData.getJobRequest());
         //update ACMQ with INPROGRESS  status
         getAnswersCatalogManagementQueueService().updateOperationStatus(answersCatalogManagementQueueId,
                  ACManagementOperationStatusType.INPROGRESS);
         boolean absorbAssetSyncInfo = getParentAssetSyncAbsorptionService().absorbAssetSyncInfo(
                  parentAssetSyncAbsorptionContext);
         if (absorbAssetSyncInfo) {
            jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);
            // TODO:: NK: Check with GA/VG if we need to do this??
            // get the existing context and updated the latest operation only on it
            AnswersCatalogContext existingAnswersCatalogContext = getJobDataService()
                     .getAnswersCatalogContextByAssetId(parentAssetSyncAbsorptionContext.getAssetId());
            if (existingAnswersCatalogContext != null) {
               existingAnswersCatalogContext.setLatestOperation(AnswersCatalogOperationType.getType(jobData
                        .getJobRequest().getJobType().toString()));
               getJobDataService().updateAnswersCatalogContext(existingAnswersCatalogContext);
            }

            //update ACMQ with SUCCESSFUL  status
            getAnswersCatalogManagementQueueService().updateOperationStatus(answersCatalogManagementQueueId,
                     ACManagementOperationStatusType.SUCCESSFUL);
         } else {
            if (logger.isDebugEnabled()) {
               logger.debug("Failed to sync the parent asset");
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
      setParentAssetSyncAbsorptionService((IParentAssetSyncAbsorptionService) SpringContextHolder
               .getBean("parentAssetSyncAbsorptionService"));
      setAnswersCatalogManagementQueueService((IAnswersCatalogManagementQueueService) SpringContextHolder
               .getBean("answersCatalogManagementQueueService"));
      setCoreConfigurationService((ICoreConfigurationService) SpringContextHolder.getBean("coreConfigurationService"));
      setAssetOperationDetailService((IAssetOperationDetailService) SpringContextHolder
               .getBean("assetOperationDetailService"));

   }

   /**
    * @return the parentAssetSyncAbsorptionService
    */
   public IParentAssetSyncAbsorptionService getParentAssetSyncAbsorptionService () {
      return parentAssetSyncAbsorptionService;
   }

   /**
    * @param parentAssetSyncAbsorptionService the parentAssetSyncAbsorptionService to set
    */
   public void setParentAssetSyncAbsorptionService (IParentAssetSyncAbsorptionService parentAssetSyncAbsorptionService) {
      this.parentAssetSyncAbsorptionService = parentAssetSyncAbsorptionService;
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
