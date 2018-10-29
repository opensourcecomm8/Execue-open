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
import com.execue.core.common.bean.swi.BusinessModelPreparationContext;
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
import com.execue.scheduler.service.IBusinessModelPreparationJobService;
import com.execue.scheduler.service.IExecueJobSchedulerService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.thoughtworks.xstream.XStream;

/**
 * This service schedules the business model preparation job
 * 
 * @author Vishay
 * @version 1.0
 * @since 10/09/09
 */
public class BusinessModelPreparationJobServiceImpl extends BaseJobService implements
         IBusinessModelPreparationJobService {

   private IExecueJobSchedulerService execueJobSchedulerService;
   private IJobDataService            jobDataService;

   public Long scheduleBusinessModelPreparationJob (BusinessModelPreparationContext businessModelPrepartionContext)
            throws KDXException {
      try {
         ExecueJobData jobData = new ExecueJobData();
         populateJobData(jobData, JobType.BUSINESS_MODEL_PREPARATION);
         XStream xStream = new XStream();
         String sflTermTokenWeightInputXML = xStream.toXML(businessModelPrepartionContext);
         ExecueJobSchedule jobSchedule = new ExecueJobSchedule();
         jobSchedule.setStartTime(null);
         jobSchedule.setEndTime(null);
         jobSchedule.setNoOfRepeats(0);
         jobSchedule.setPeriodicity(ExecueTaskProps.MINUTELY_SCHEDULE);

         long userId = businessModelPrepartionContext.getUserId();
         JobRequest jobRequest = ExecueBeanManagementUtil.prepareJobRequest(JobType.BUSINESS_MODEL_PREPARATION,
                  sflTermTokenWeightInputXML, JobStatus.PENDING, new Date(), userId);
         jobDataService.createJobRequest(jobRequest);
         jobData.setJobRequest(jobRequest);
         execueJobSchedulerService.scheduleJob(jobData, jobSchedule, null);
         return jobRequest.getId();
      } catch (ExecueJobSchedulerException e) {
         throw new KDXException(SWIExceptionCodes.BUSINESS_MODEL_PREPARATION_JOB_SCHEDULING_FAILURE_CODE, e);
      } catch (QueryDataException e) {
         throw new KDXException(SWIExceptionCodes.BUSINESS_MODEL_PREPARATION_JOB_SCHEDULING_FAILURE_CODE, e);
      }
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
