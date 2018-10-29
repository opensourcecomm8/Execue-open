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

import com.execue.core.common.bean.batchMaintenance.InstanceAbsorptionContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationParamName;
import com.execue.platform.swi.ISDX2KDXMappingService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;

/**
 * @author Nitesh
 * @since 29/04/2010
 */

public class InstanceAbsorptionJob extends BaseLineJob {

   private static final Logger    logger = Logger.getLogger(InstanceAbsorptionJob.class);
   private ISDX2KDXMappingService sdx2kdxMappingService;

   @Override
   protected void process (ExecueJobData jobData) throws ExecueJobSchedulerException {

      InstanceAbsorptionContext instanceAbsorptionContext = null;
      logger.debug("Invoking on SDXBatchMaintenanceService [" + jobData.getJobName() + "]");
      try {
         initializeDependencies();
         instanceAbsorptionContext = (InstanceAbsorptionContext) jobData.getRequestContextData();
         instanceAbsorptionContext.setJobRequest(jobData.getJobRequest());
         getSdx2kdxMappingService().createBatchProcess(instanceAbsorptionContext);
         if (instanceAbsorptionContext.isGenerateSuggestionMappings()) {
            getSdx2kdxMappingService().generateInstanceMappingSuggestions(instanceAbsorptionContext);
         } else {
            getSdx2kdxMappingService().saveInstanceMappingSuggestions(instanceAbsorptionContext);
         }
         getSdx2kdxMappingService().deleteBatchProcess(instanceAbsorptionContext);
         jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);
      } catch (Exception ex) {
         jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         logger.error("JOB Execution failed for job -- " + jobData.getJobName());
         logger.error(ex, ex);
      }
   }

   private void initializeDependencies () throws Exception {
      sdx2kdxMappingService = (ISDX2KDXMappingService) SpringContextHolder.getBean("sdx2kdxMappingService");
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
      InstanceAbsorptionContext instanceAbsorptionContext = (InstanceAbsorptionContext) jobData.getRequestContextData();
      subjectParams.put(NotificationParamName.ASSET_NAME, instanceAbsorptionContext.getAssetId() + "");
      subjectParams.put(NotificationParamName.MODEL_NAME, instanceAbsorptionContext.getModelId() + "");
      bodyParams.put(NotificationParamName.ASSET_NAME, instanceAbsorptionContext.getAssetId() + "");
      subjectParams.put(NotificationParamName.MODEL_NAME, instanceAbsorptionContext.getModelId() + "");
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
      InstanceAbsorptionContext instanceAbsorptionContext = (InstanceAbsorptionContext) jobData.getRequestContextData();
      subjectParams.put(NotificationParamName.ASSET_NAME, instanceAbsorptionContext.getAssetId() + "");
      subjectParams.put(NotificationParamName.MODEL_NAME, instanceAbsorptionContext.getModelId() + "");
      bodyParams.put(NotificationParamName.ASSET_NAME, instanceAbsorptionContext.getAssetId() + "");
      subjectParams.put(NotificationParamName.MODEL_NAME, instanceAbsorptionContext.getModelId() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");

   }

   public ISDX2KDXMappingService getSdx2kdxMappingService () {
      return sdx2kdxMappingService;
   }

   public void setSdx2kdxMappingService (ISDX2KDXMappingService sdx2kdxMappingService) {
      this.sdx2kdxMappingService = sdx2kdxMappingService;
   }

}
