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


package com.execue.platform.swi.impl;

import java.util.Date;

import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.swi.BusinessModelPreparationContext;
import com.execue.core.common.bean.swi.PublishAssetContext;
import com.execue.core.common.bean.swi.PublishContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.platform.IBusinessModelPreparationService;
import com.execue.platform.security.ISecurityDefinitionPublishWrapperService;
import com.execue.platform.swi.IAssetPublishService;
import com.execue.platform.swi.IPublishService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IApplicationManagementService;

public class PublishServiceImpl implements IPublishService {

   private IAssetPublishService                     assetPublishService;
   private IJobDataService                          jobDataService;
   private IApplicationManagementService            applicationManagementService;
   private IBusinessModelPreparationService         businessModelPreparationService;
   private ISecurityDefinitionPublishWrapperService securityDefinitionPublishWrapperService;

   public void publishAppHierarchy (PublishContext publishContext) throws ExeCueException {
      String errorMessage = "";
      boolean status = true;
      JobOperationalStatus jobOperationalStatus = null;
      JobRequest jobRequest = publishContext.getJobRequest();
      try {

         BusinessModelPreparationContext businessModelPreparationContext = new BusinessModelPreparationContext();
         businessModelPreparationContext.setApplicationId(publishContext.getApplicationId());
         businessModelPreparationContext.setModelId(publishContext.getModelId());
         businessModelPreparationContext.setAssetId(((PublishAssetContext) publishContext).getAssetId());
         businessModelPreparationContext.setJobRequest(publishContext.getJobRequest());
         businessModelPreparationContext.setUserId(publishContext.getUserId());

         status = getBusinessModelPreparationService().prepareBusinessModel(businessModelPreparationContext);

         if (status) {

            // Publishing the ACL Security for the Asset
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Publishing the Default Security for the Asset", JobStatus.INPROGRESS, null, new Date());
            getJobDataService().createJobOperationStatus(jobOperationalStatus);

            // update the acl security for the asset
            getSecurityDefinitionPublishWrapperService().applyRolePermissionOnAssetWithPropagation(
                     publishContext.getUserId(), ((PublishAssetContext) publishContext).getAssetId());

            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);

            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Publishing the Dataset Collection", JobStatus.INPROGRESS, null, new Date());
            getJobDataService().createJobOperationStatus(jobOperationalStatus);

            getAssetPublishService().publishAsset(publishContext.getApplicationId(),
                     ((PublishAssetContext) publishContext).getAssetId(), publishContext.getUserId(),
                     publishContext.getPublishMode());

            // NOTE: -RG- Do not update the Application Publish mode from this flow.
            //   Publish Mode on Application is modified from the Application Creation/Updation process itself
            /*
            appPublishMode = getAssetPublishService().publishAsset(publishContext.getApplicationId(),
                     ((PublishAssetContext) publishContext).getAssetId(), publishContext.getUserId(),
                     publishContext.getPublishMode());

            // update the publish_mode for application
            getApplicationManagementService().updateApplicationPublishAssetMode(publishContext.getApplicationId(),
                     appPublishMode);
            */

            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         }
      } catch (SDXException sdxException) {
         status = false;
         errorMessage = sdxException.getMessage();
         throw new SDXException(sdxException.getCode(), sdxException);
      } catch (QueryDataException e) {
         status = false;
         errorMessage = e.getMessage();
         throw new SDXException(e.getCode(), e);
      } catch (KDXException e) {
         status = false;
         errorMessage = e.getMessage();
         throw new SDXException(e.getCode(), e);
      } finally {
         if (!status && jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, errorMessage,
                     new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException queryDataException) {
               throw new SDXException(queryDataException.getCode(), queryDataException);
            }
         }
      }
   }

   public IAssetPublishService getAssetPublishService () {
      return assetPublishService;
   }

   public void setAssetPublishService (IAssetPublishService assetPublishService) {
      this.assetPublishService = assetPublishService;
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public IApplicationManagementService getApplicationManagementService () {
      return applicationManagementService;
   }

   public void setApplicationManagementService (IApplicationManagementService applicationManagementService) {
      this.applicationManagementService = applicationManagementService;
   }

   public IBusinessModelPreparationService getBusinessModelPreparationService () {
      return businessModelPreparationService;
   }

   public void setBusinessModelPreparationService (IBusinessModelPreparationService businessModelPreparationService) {
      this.businessModelPreparationService = businessModelPreparationService;
   }

   /**
    * @return the securityDefinitionPublishWrapperService
    */
   public ISecurityDefinitionPublishWrapperService getSecurityDefinitionPublishWrapperService () {
      return securityDefinitionPublishWrapperService;
   }

   /**
    * @param securityDefinitionPublishWrapperService the securityDefinitionPublishWrapperService to set
    */
   public void setSecurityDefinitionPublishWrapperService (
            ISecurityDefinitionPublishWrapperService securityDefinitionPublishWrapperService) {
      this.securityDefinitionPublishWrapperService = securityDefinitionPublishWrapperService;
   }

}
