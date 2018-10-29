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

import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.JobRequest;
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
import com.execue.scheduler.service.IDataMartCreationJobService;
import com.execue.scheduler.service.IExecueJobSchedulerService;
import com.thoughtworks.xstream.XStream;

/**
 * This service schedules the cube creation job
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public class DataMartCreationJobServiceImpl extends BaseJobService implements IDataMartCreationJobService {

   private IExecueJobSchedulerService execueJobSchedulerService;
   private IJobDataService            jobDataService;

   public Long scheduleMartCreationJob (MartCreationContext martCreationContext) throws AnswersCatalogException {
      try {
         ExecueJobData jobData = new ExecueJobData();
         populateJobData(jobData, JobType.MART_CREATION);
         XStream xStream = new XStream();
         String martCreationXML = xStream.toXML(martCreationContext);
         ExecueJobSchedule jobSchedule = new ExecueJobSchedule();
         jobSchedule.setStartTime(null);
         jobSchedule.setEndTime(null);
         jobSchedule.setNoOfRepeats(0);
         jobSchedule.setPeriodicity(ExecueTaskProps.MINUTELY_SCHEDULE);

         JobRequest jobRequest = ExecueBeanManagementUtil.prepareJobRequest(JobType.MART_CREATION, martCreationXML,
                  JobStatus.PENDING, new Date(), martCreationContext.getUserId());
         jobDataService.createJobRequest(jobRequest);
         jobData.setJobRequest(jobRequest);
         execueJobSchedulerService.scheduleJob(jobData, jobSchedule, null);
         return jobRequest.getId();
      } catch (ExecueJobSchedulerException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.MART_JOB_SCHEDULING_FAILURE_CODE, e);
      } catch (QueryDataException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.MART_JOB_SCHEDULING_FAILURE_CODE, e);
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
