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

import org.apache.log4j.Logger;

import com.execue.core.common.bean.batchMaintenance.MemberAbsorptionContext;
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
import com.execue.scheduler.service.IExecueJobSchedulerService;
import com.execue.scheduler.service.IMemberAbsorptionJobService;
import com.execue.swi.exception.BatchMaintenanceException;
import com.thoughtworks.xstream.XStream;

/**
 * @author MurthySN
 * @since 03/02/2010
 */
public class MemberAbsorptionJobServiceImpl extends BaseJobService implements IMemberAbsorptionJobService {

   private static final Logger        logger = Logger.getLogger(MemberAbsorptionJobServiceImpl.class);

   private IExecueJobSchedulerService execueJobSchedulerService;
   private IJobDataService            jobDataService;

   public Long scheduleMemberAbsorptionJob (MemberAbsorptionContext memberAbsorptionContext)
            throws BatchMaintenanceException {
      JobRequest jobRequest = null;
      try {
         ExecueJobData jobData = new ExecueJobData();
         populateJobData(jobData, JobType.MEMBER_ABSORPTION);
         XStream xStream = new XStream();
         String memberAbsorptionInputXML = xStream.toXML(memberAbsorptionContext);
         logger.debug("input XML:-" + memberAbsorptionInputXML);
         ExecueJobSchedule jobSchedule = new ExecueJobSchedule();
         jobSchedule.setStartTime(null);
         jobSchedule.setEndTime(null);
         jobSchedule.setNoOfRepeats(0);
         jobSchedule.setPeriodicity(ExecueTaskProps.MINUTELY_SCHEDULE);
         jobRequest = ExecueBeanManagementUtil.prepareJobRequest(JobType.MEMBER_ABSORPTION, memberAbsorptionInputXML,
                  JobStatus.PENDING, new java.util.Date(), memberAbsorptionContext.getUserId());
         jobDataService.createJobRequest(jobRequest);
         jobData.setJobRequest(jobRequest);
         execueJobSchedulerService.scheduleJob(jobData, jobSchedule, null);
      } catch (ExecueJobSchedulerException e) {
         e.printStackTrace();
      } catch (QueryDataException e) {
         e.printStackTrace();
      }
      return jobRequest.getId();
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
