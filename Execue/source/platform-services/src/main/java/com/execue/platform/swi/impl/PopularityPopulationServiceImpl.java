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

import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.swi.PopularityHitMaintenanceContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.platform.swi.IPopularityPopulationService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPopularityService;

/**
 * @author John Mallavalli
 */
public class PopularityPopulationServiceImpl implements IPopularityPopulationService {

   private IPopularityService       popularityService;
   private IJobDataService          jobDataService;
   private IKDXRetrievalService     kdxRetrievalService;
   private ISWIConfigurationService swiConfigurationService;

   public void updateTermsBasedOnPopularity (PopularityHitMaintenanceContext popularityHitMaintenanceContext)
            throws SWIException {
      JobOperationalStatus jobOperationalStatus = null;
      int batchSize = getSwiConfigurationService().getPopularityBatchSize();
      try {
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(popularityHitMaintenanceContext
                  .getJobRequest(), "Updating popularity", JobStatus.INPROGRESS, null, new Date());
         jobDataService.createJobOperationStatus(jobOperationalStatus);

         List<Model> models = getPopularityService().updateTermsBasedOnPopularity(batchSize);
         /*
          * for (Model model : models) { SFLTermTokenWeightContext sTermTokenWeightContext = new
          * SFLTermTokenWeightContext();
          * sTermTokenWeightContext.setJobRequest(popularityHitMaintenanceContext.getJobRequest());
          * sTermTokenWeightContext.setModelId(model.getId());
          * kdxMaintenanceService.updateSFLTermTokensWeightOnHits(sTermTokenWeightContext); }
          */
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
      } catch (ExeCueException exception) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, exception
                     .getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new SWIException(SWIExceptionCodes.UPDATE_POPULARITY_HIT_UPDATION_JOB_SCHEDULING_FAILURE_CODE,
                        exception);
            }
         }
         throw new SWIException(SWIExceptionCodes.UPDATE_POPULARITY_HIT_UPDATION_JOB_SCHEDULING_FAILURE_CODE, exception);
      }
   }

   public void updateRIOntoTermsPopularity (PopularityHitMaintenanceContext popularityHitMaintenanceContext)
            throws SWIException {
      JobOperationalStatus jobOperationalStatus = null;
      int batchSize = getSwiConfigurationService().getPopularityBatchSize();
      try {
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(popularityHitMaintenanceContext
                  .getJobRequest(), "Updating RIOntoTerms popularity", JobStatus.INPROGRESS, null, new Date());
         jobDataService.createJobOperationStatus(jobOperationalStatus);

         getPopularityService().updateRIOntoTermsPopularity(popularityHitMaintenanceContext.getModelId(), batchSize);
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
      } catch (ExeCueException exception) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, exception
                     .getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new SWIException(
                        SWIExceptionCodes.UPDATE_RI_ONTO_TERM_POPULARITY_HIT_UPDATION_JOB_SCHEDULING_FAILURE_CODE,
                        exception);
            }
         }
         exception.printStackTrace();
         throw new SWIException(
                  SWIExceptionCodes.UPDATE_RI_ONTO_TERM_POPULARITY_HIT_UPDATION_JOB_SCHEDULING_FAILURE_CODE, exception);
      }

   }

   /**
    * @return the jobDataService
    */
   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   /**
    * @param jobDataService
    *           the jobDataService to set
    */
   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public IPopularityService getPopularityService () {
      return popularityService;
   }

   public void setPopularityService (IPopularityService popularityService) {
      this.popularityService = popularityService;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

}
