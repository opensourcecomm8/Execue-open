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
package com.execue.scheduler.service.impl;

import java.util.Date;

import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.swi.AssetSyncAbsorptionContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.JobType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.jobdata.ExecueJobSchedule;
import com.execue.scheduler.props.ExecueTaskProps;
import com.execue.scheduler.service.BaseJobService;
import com.execue.scheduler.service.IExecueJobSchedulerService;
import com.execue.scheduler.service.IParentAssetSyncAbsorptionJobService;
import com.thoughtworks.xstream.XStream;

/**
 * This service schedules the parent synch absorption job
 * 
 * @author Nitesh
 * @version 1.0
 * @since 09/04/12
 *
 */
public class ParentAssetSyncAbsorptionJobServiceImpl extends BaseJobService implements
         IParentAssetSyncAbsorptionJobService {

   private IExecueJobSchedulerService execueJobSchedulerService;
   private IJobDataService            jobDataService;

   /* (non-Javadoc)
    * @see com.execue.scheduler.service.IParentAssetSyncAbsortpionJobService#scheduleParentAssetSyncAbsortpionJob(com.execue.core.common.bean.swi.AssetSyncAbsorptionContext)
    */
   @Override
   public Long scheduleParentAssetSyncAbsortpionJob (AssetSyncAbsorptionContext parentAssetSyncAbsorptionContext)
            throws AnswersCatalogException {
      try {
         ExecueJobData jobData = new ExecueJobData();
         populateJobData(jobData, JobType.PARENT_ASSET_SYNCHRONIZATION);
         XStream xStream = new XStream();
         String parentAssetSyncAbsorptionContextXML = xStream.toXML(parentAssetSyncAbsorptionContext);
         ExecueJobSchedule jobSchedule = new ExecueJobSchedule();
         jobSchedule.setStartTime(null);
         jobSchedule.setEndTime(null);
         jobSchedule.setNoOfRepeats(0);
         jobSchedule.setPeriodicity(ExecueTaskProps.MINUTELY_SCHEDULE);
         long userId = parentAssetSyncAbsorptionContext.getUserId();
         JobRequest jobRequest = ExecueBeanManagementUtil.prepareJobRequest(JobType.PARENT_ASSET_SYNCHRONIZATION,
                  parentAssetSyncAbsorptionContextXML, JobStatus.PENDING, new Date(), userId);
         jobDataService.createJobRequest(jobRequest);
         jobData.setJobRequest(jobRequest);
         execueJobSchedulerService.scheduleJob(jobData, jobSchedule, null);
         return jobRequest.getId();
      } catch (ExecueJobSchedulerException e) {
         throw new AnswersCatalogException(
                  AnswersCatalogExceptionCodes.DEFAULT_PARENT_ASSET_SYNCHRONIZATION_JOB_SCHEDULING_FAILURE_CODE, e);
      } catch (QueryDataException e) {
         throw new AnswersCatalogException(
                  AnswersCatalogExceptionCodes.DEFAULT_PARENT_ASSET_SYNCHRONIZATION_JOB_SCHEDULING_FAILURE_CODE, e);
      }
   }

   /**
    * @return the execueJobSchedulerService
    */
   public IExecueJobSchedulerService getExecueJobSchedulerService () {
      return execueJobSchedulerService;
   }

   /**
    * @param execueJobSchedulerService the execueJobSchedulerService to set
    */
   public void setExecueJobSchedulerService (IExecueJobSchedulerService execueJobSchedulerService) {
      this.execueJobSchedulerService = execueJobSchedulerService;
   }

   /**
    * @param jobDataService the jobDataService to set
    */
   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }
}