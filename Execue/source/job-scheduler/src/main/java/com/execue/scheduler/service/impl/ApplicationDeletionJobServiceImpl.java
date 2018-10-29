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
import com.execue.core.common.bean.swi.ApplicationDeletionContext;
import com.execue.core.common.type.AppOperationType;
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
import com.execue.scheduler.service.IApplicationDeletionJobService;
import com.execue.scheduler.service.IExecueJobSchedulerService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IApplicationManagementService;
import com.thoughtworks.xstream.XStream;

public class ApplicationDeletionJobServiceImpl extends BaseJobService implements IApplicationDeletionJobService {

   private IExecueJobSchedulerService    execueJobSchedulerService;
   private IJobDataService               jobDataService;
   private IApplicationManagementService applicationManagementService;

   public Long scheduleApplicationDeletionJob (ApplicationDeletionContext applicationDeletionContext)
            throws SWIException {
      JobRequest jobRequest = null;
      try {
         ExecueJobData jobData = new ExecueJobData();
         populateJobData(jobData, JobType.APPLICATION_DELETION);
         XStream xStream = new XStream();
         String applicationDeletionInputXML = xStream.toXML(applicationDeletionContext);
         ExecueJobSchedule jobSchedule = new ExecueJobSchedule();
         jobSchedule.setStartTime(null);
         jobSchedule.setEndTime(null);
         jobSchedule.setNoOfRepeats(0);
         jobSchedule.setPeriodicity(ExecueTaskProps.MINUTELY_SCHEDULE);
         jobRequest = ExecueBeanManagementUtil.prepareJobRequest(JobType.APPLICATION_DELETION,
                  applicationDeletionInputXML, JobStatus.PENDING, new Date(), applicationDeletionContext.getUserId());

         jobDataService.createJobRequest(jobRequest);
         jobData.setJobRequest(jobRequest);
         execueJobSchedulerService.scheduleJob(jobData, jobSchedule, null);
         if (applicationDeletionContext.getApplicationId() != null) {
            getApplicationManagementService().updateApplicationOperationDetails(
                     applicationDeletionContext.getApplicationId(), AppOperationType.DELETING, jobRequest.getId(),
                     JobStatus.INPROGRESS);
         }
      } catch (ExecueJobSchedulerException execueJobSchedulerException) {
         throw new SWIException(SWIExceptionCodes.SDX_DELETION_JOB_FAILED, execueJobSchedulerException);
      } catch (QueryDataException queryDataException) {
         throw new SWIException(SWIExceptionCodes.SDX_DELETION_JOB_FAILED, queryDataException);
      } catch (KDXException sdxException) {
         throw new SWIException(sdxException.getCode(), sdxException);
      }
      return jobRequest.getId();
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
