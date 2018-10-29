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

import com.execue.core.common.bean.swi.SFLTermTokenWeightContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationParamName;
import com.execue.platform.swi.IKDXMaintenanceService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;

/**
 * This job is for updating sfltermtoken weights based on hits
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/09/09
 */
public class UpdateSFLTermTokenWeightJob extends BaseLineJob {

   private static final Logger    log                   = Logger.getLogger(UpdateSFLTermTokenWeightJob.class);
   private IKDXMaintenanceService kdxMaintenanceService = null;

   @Override
   protected void process (ExecueJobData jobData) throws ExecueJobSchedulerException {
      SFLTermTokenWeightContext sflTermTokenWeightContext = null;
      try {
         sflTermTokenWeightContext = (SFLTermTokenWeightContext) jobData.getRequestContextData();
         log.debug("Invoking on updateSflTermTokenWeight Job  [" + jobData.getJobName() + "]");
         initializeDependencies();
         sflTermTokenWeightContext.setJobRequest(jobData.getJobRequest());
         kdxMaintenanceService.updateSFLTermTokensWeightOnHits(sflTermTokenWeightContext);
         jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);
      } catch (Exception e) {
         e.printStackTrace();
         jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         log.error("JOB Execution failed for job -- " + jobData.getJobName());
         jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         log.error(e, e);
      }
   }

   @Override
   protected void prepareCompletionMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      SFLTermTokenWeightContext sflTermTokenWeightContext = (SFLTermTokenWeightContext) jobData.getRequestContextData();

      subjectParams.put(NotificationParamName.MODEL_NAME, sflTermTokenWeightContext.getModelId() + "");
      bodyParams.put(NotificationParamName.MODEL_NAME, sflTermTokenWeightContext.getModelId() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
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
      SFLTermTokenWeightContext sflTermTokenWeightContext = (SFLTermTokenWeightContext) jobData.getRequestContextData();

      subjectParams.put(NotificationParamName.MODEL_NAME, sflTermTokenWeightContext.getModelId() + "");
      bodyParams.put(NotificationParamName.MODEL_NAME, sflTermTokenWeightContext.getModelId() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");

   }

   private void initializeDependencies () throws Exception {
      kdxMaintenanceService = (IKDXMaintenanceService) SpringContextHolder.getBean("kdxMaintenanceService");
   }

}
