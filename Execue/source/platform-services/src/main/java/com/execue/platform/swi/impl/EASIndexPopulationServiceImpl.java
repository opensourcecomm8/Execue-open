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
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.swi.ApplicationScopeIndexDetail;
import com.execue.core.common.bean.swi.EASIndexPeriodicContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.platform.configuration.IPlatformServicesConfigurationService;
import com.execue.platform.swi.IEASIndexPopulationService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IEASIndexService;
import com.execue.swi.service.IKDXModelService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.impl.EASIndexServiceImpl;

public class EASIndexPopulationServiceImpl implements IEASIndexPopulationService {

   private IKDXModelService                      kdxModelService;
   private IJobDataService                       jobDataService;
   private IKDXRetrievalService                  kdxRetrievalService;
   private IPlatformServicesConfigurationService platformServicesConfigurationService;
   private IEASIndexService                      easIndexService;
   private static final Logger                   logger = Logger.getLogger(EASIndexServiceImpl.class);

   public void populateEASIndex (EASIndexPeriodicContext easIndexPeriodicContext) throws SWIException {
      JobOperationalStatus jobOperationalStatus = null;
      JobRequest jobRequest = easIndexPeriodicContext.getJobRequest();
      Long applicationId = easIndexPeriodicContext.getApplicationId();
      try {
         if (getPlatformServicesConfigurationService().isAppScopeRefreshIndexEnabled()) {
            List<Model> models = getKdxRetrievalService().getModelsByApplicationId(applicationId);
            if (getKdxModelService().isIndexEvaluationRequired(getNonBaseModelId(models))) {
               for (Long modelGroupId : getKdxRetrievalService().getModelGroupIdsByApplicationId(applicationId)) {
                  try {
                     jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                              " truncating EAS Pre Index for appId :" + applicationId, JobStatus.INPROGRESS, null,
                              new Date());
                     jobDataService.createJobOperationStatus(jobOperationalStatus);
                     getEasIndexService().deleteEASPreIndexByApplicationId(applicationId);
                     jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                              JobStatus.SUCCESS, null, new Date());
                     getJobDataService().updateJobOperationStatus(jobOperationalStatus);
                  } catch (SWIException swiException) {
                     if (logger.isDebugEnabled()) {
                        logger.debug("Exception occured while truncating EAS Pre Index for appId :" + applicationId);
                     }
                  }
                  try {
                     jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                              " populating EAS Pre Index for SFLTerms for appId : " + applicationId,
                              JobStatus.INPROGRESS, null, new Date());
                     jobDataService.createJobOperationStatus(jobOperationalStatus);
                     getEasIndexService().populateEASPreIndexForSFLTerm(applicationId, modelGroupId);
                     jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                              JobStatus.SUCCESS, null, new Date());
                     getJobDataService().updateJobOperationStatus(jobOperationalStatus);
                  } catch (SWIException swiException) {
                     if (logger.isDebugEnabled()) {
                        logger.debug("Exception occured while populating EAS Pre Index for SFLTerms for appId :"
                                 + applicationId);
                     }
                  }
                  try {
                     jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                              " populating EAS Pre Index table for RIOntoterm for appId : " + applicationId,
                              JobStatus.INPROGRESS, null, new Date());
                     jobDataService.createJobOperationStatus(jobOperationalStatus);
                     getEasIndexService().populateEASPreIndexForRiOntoTerm(applicationId, modelGroupId);
                     jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                              JobStatus.SUCCESS, null, new Date());
                     getJobDataService().updateJobOperationStatus(jobOperationalStatus);
                  } catch (SWIException swiException) {
                     if (logger.isDebugEnabled()) {
                        logger.debug("Exception occured while populating EAS Pre Index for RIOntoterm for appId : "
                                 + applicationId);
                     }
                  }
                  try {
                     jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                              " truncating EAS Index for appId : " + applicationId, JobStatus.INPROGRESS, null,
                              new Date());
                     jobDataService.createJobOperationStatus(jobOperationalStatus);
                     getEasIndexService().deleteEASIndexByApplicationId(applicationId);
                     jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                              JobStatus.SUCCESS, null, new Date());
                     getJobDataService().updateJobOperationStatus(jobOperationalStatus);
                  } catch (SWIException swiException) {
                     if (logger.isDebugEnabled()) {
                        logger.debug("Exception occured while truncating EAS Index for appId " + applicationId);
                     }
                  }
                  try {
                     jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                              " populating EAS Index from EAS Pre Index for appId : " + applicationId,
                              JobStatus.INPROGRESS, null, new Date());
                     jobDataService.createJobOperationStatus(jobOperationalStatus);
                     getEasIndexService().populateEASIndexFromEASPreIndex(applicationId, modelGroupId);
                     jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                              JobStatus.SUCCESS, null, new Date());
                     getJobDataService().updateJobOperationStatus(jobOperationalStatus);
                  } catch (SWIException swiException) {
                     if (logger.isDebugEnabled()) {
                        logger.debug("Exception occured while populating EAS Index from EAS Pre Index for appId "
                                 + applicationId);
                     }
                  }
                  try {
                     jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                              " updating the index refresh date in detail for appId : " + applicationId,
                              JobStatus.INPROGRESS, null, new Date());
                     jobDataService.createJobOperationStatus(jobOperationalStatus);
                     ApplicationScopeIndexDetail applicationScopeIndexDetail = getEasIndexService()
                              .getApplicationScopeIndexDetailByAppId(applicationId);
                     applicationScopeIndexDetail.setLastRefreshDate(new Date());
                     getEasIndexService().updateApplicationScopeIndexDetail(applicationScopeIndexDetail);
                     jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                              JobStatus.SUCCESS, null, new Date());
                     getJobDataService().updateJobOperationStatus(jobOperationalStatus);
                  } catch (SWIException swiException) {
                     if (logger.isDebugEnabled()) {
                        logger.debug("Exception occured while updating the index refresh date in detail for appId "
                                 + applicationId);
                     }
                  }
               }
            }
         }
      } catch (ExeCueException exception) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, exception
                     .getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new SWIException(SWIExceptionCodes.EAS_INDEX_REFRESH_FAILURE_CODE, exception);
            }
         }
         throw new SWIException(SWIExceptionCodes.EAS_INDEX_REFRESH_FAILURE_CODE, exception);
      }

   }

   private Long getNonBaseModelId (List<Model> models) {
      Long modelId = models.get(0).getId();
      // TODO : -MGM- need to check the below code
      // for (Model model : models) {
      // if (CheckType.NO.equals(model.getBase())) {
      // modelId = model.getId();
      // }
      // }
      return modelId;
   }

   public IKDXModelService getKdxModelService () {
      return kdxModelService;
   }

   public void setKdxModelService (IKDXModelService kdxModelService) {
      this.kdxModelService = kdxModelService;
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IEASIndexService getEasIndexService () {
      return easIndexService;
   }

   public void setEasIndexService (IEASIndexService easIndexService) {
      this.easIndexService = easIndexService;
   }

   /**
    * @return the platformServicesConfigurationService
    */
   public IPlatformServicesConfigurationService getPlatformServicesConfigurationService () {
      return platformServicesConfigurationService;
   }

   /**
    * @param platformServicesConfigurationService the platformServicesConfigurationService to set
    */
   public void setPlatformServicesConfigurationService (
            IPlatformServicesConfigurationService platformServicesConfigurationService) {
      this.platformServicesConfigurationService = platformServicesConfigurationService;
   }

}
