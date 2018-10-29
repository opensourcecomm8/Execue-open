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


package com.execue.sforce.job.impl;

import java.util.Date;

import com.execue.core.common.api.qdata.IJobStatusLogService;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.JobType;
import com.execue.core.common.util.ExecueBeanUtils;
import com.execue.core.configuration.ExecueConfiguration;
import com.execue.core.exception.qdata.QueryDataException;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.jobdata.ExecueJobSchedule;
import com.execue.scheduler.props.ExecueTaskProps;
import com.execue.scheduler.service.IExecueJobSchedulerService;
import com.execue.sforce.bean.SforceLoginContext;
import com.execue.sforce.configuration.ISforceConfigurationConstants;
import com.execue.sforce.exception.SforceException;
import com.execue.sforce.exception.SforceExceptionCodes;
import com.execue.sforce.job.ISforceJobService;

/**
 * This service schedules the data replication job for sforce user
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public class SforceJobServiceImpl implements ISforceJobService {

   private ExecueConfiguration        sforceConfiguration;
   private IJobStatusLogService       jobStatusLogService;
   private IExecueJobSchedulerService schedulerService;

   /**
    * This method schedules data replication job
    * 
    * @throws SforceException
    */
   public void scheduleSforceJob (SforceLoginContext sforceLoginContext) throws SforceException {
      try {
         ExecueJobData jobData = new ExecueJobData();
         jobData.setJobName(getSforceConfiguration().getProperty(
                  ISforceConfigurationConstants.DEFAULT_SFORCE_JOB_NAME_KEY));
         jobData.setJobGroup(getSforceConfiguration().getProperty(
                  ISforceConfigurationConstants.DEFAULT_SFORCE_JOB_GROUP_KEY));
         ExecueJobSchedule jobSchedule = new ExecueJobSchedule();
         jobSchedule.setStartTime(null);
         jobSchedule.setEndTime(null);
         jobSchedule.setNoOfRepeats(0);
         jobSchedule.setPeriodicity(ExecueTaskProps.MINUTELY_SCHEDULE);
         long userId = 1L;
         JobRequest jobRequest = ExecueBeanUtils.prepareJobRequest(JobType.SFORCE_DATA_REPLICATION, null,
                  JobStatus.PENDING, new Date(), userId);
         jobStatusLogService.createJobRequest(jobRequest);
         jobData.setJobRequest(jobRequest);
         schedulerService.scheduleJob(jobData, jobSchedule, null);
      } catch (QueryDataException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_JOB_SETUP_EXCEPTION_CODE, e);
      } catch (ExecueJobSchedulerException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_JOB_SETUP_EXCEPTION_CODE, e);
      }
   }

   public ExecueConfiguration getSforceConfiguration () {
      return sforceConfiguration;
   }

   public void setSforceConfiguration (ExecueConfiguration sforceConfiguration) {
      this.sforceConfiguration = sforceConfiguration;
   }

   public IJobStatusLogService getJobStatusLogService () {
      return jobStatusLogService;
   }

   public void setJobStatusLogService (IJobStatusLogService jobStatusLogService) {
      this.jobStatusLogService = jobStatusLogService;
   }

   public IExecueJobSchedulerService getSchedulerService () {
      return schedulerService;
   }

   public void setSchedulerService (IExecueJobSchedulerService schedulerService) {
      this.schedulerService = schedulerService;
   }
}
