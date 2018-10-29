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


package com.execue.scheduler.service.impl;

import java.util.Date;

import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.swi.CorrectMappingsMaintenanceContext;
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
import com.execue.scheduler.service.ICorrectMappingsMaintenanceJobService;
import com.execue.scheduler.service.IExecueJobSchedulerService;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.thoughtworks.xstream.XStream;

/**
 * This service schedules the correction of Primary flag in mappings
 * 
 * @author MurthySN
 * @version 1.0
 * @since 29/12/09
 */
public class CorrectMappingsMaintenanceJobServiceImpl extends BaseJobService implements
         ICorrectMappingsMaintenanceJobService {

   private IExecueJobSchedulerService execueJobSchedulerService;
   private IJobDataService            jobDataService;

   public Long scheduleCorrectMappingsMaintenanceJob (
            CorrectMappingsMaintenanceContext correctMappingsMaintenanceContext) throws MappingException {
      JobRequest jobRequest = null;
      try {
         ExecueJobData jobData = new ExecueJobData();
         populateJobData(jobData, JobType.CORRECT_MAPPINGS);
         XStream xStream = new XStream();
         String corretMappingsInputXML = xStream.toXML(correctMappingsMaintenanceContext);
         ExecueJobSchedule jobSchedule = new ExecueJobSchedule();
         jobSchedule.setStartTime(null);
         jobSchedule.setEndTime(null);
         jobSchedule.setNoOfRepeats(0);
         jobSchedule.setPeriodicity(ExecueTaskProps.MINUTELY_SCHEDULE);
         jobRequest = ExecueBeanManagementUtil.prepareJobRequest(JobType.CORRECT_MAPPINGS, corretMappingsInputXML,
                  JobStatus.PENDING, new Date(), 1L);
         jobDataService.createJobRequest(jobRequest);
         jobData.setJobRequest(jobRequest);
         execueJobSchedulerService.scheduleJob(jobData, jobSchedule, null);
      } catch (ExecueJobSchedulerException e) {
         throw new MappingException(SWIExceptionCodes.CORRECT_MAPPINGS_JOB_SCHEDULING_FAILURE_CODE, e);
      } catch (QueryDataException e) {
         throw new MappingException(SWIExceptionCodes.CORRECT_MAPPINGS_JOB_SCHEDULING_FAILURE_CODE, e);
      }
      return jobRequest.getId();
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
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

}
