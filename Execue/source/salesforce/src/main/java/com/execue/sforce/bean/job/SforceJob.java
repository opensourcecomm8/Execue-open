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


package com.execue.sforce.bean.job;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.execue.core.common.api.qdata.IJobStatusLogService;
import com.execue.core.common.bean.entity.JobHistoryOperationalStatus;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.util.ExecueBeanUtils;
import com.execue.core.exception.qdata.QueryDataException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;
import com.execue.scheduler.props.ExecueTaskProps;
import com.execue.sforce.bean.SforceLoginContext;
import com.execue.sforce.login.ISforceLoginService;
import com.execue.sforce.replication.ISforceReplicationService;

/**
 * This class represents the sforce job which does the replication for sforce user
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/08/09
 */
public class SforceJob implements Job {

   public void execute (JobExecutionContext context) throws JobExecutionException {
      Logger logger = Logger.getLogger(SforceJob.class);
      IJobStatusLogService jobDataService = (IJobStatusLogService) SpringContextHolder.getBean("jobDataService");
      ISforceLoginService sforceLoginService = (ISforceLoginService) SpringContextHolder.getBean("sforceLoginService");

      ISforceReplicationService sforceDataReplicationService = (ISforceReplicationService) SpringContextHolder
               .getBean("sforceDataReplicationService");

      JobRequest jobRequest = ((ExecueJobData) context.getTrigger().getJobDataMap().get(ExecueTaskProps.EXJOB_DATA))
               .getJobRequest();
      try {
         logger.debug("Invoking on Sforce Job [" + context.getJobDetail().getName() + "]");
         jobDataService.updateJobRequest(ExecueBeanUtils.modifyJobRequest(jobRequest, JobStatus.INPROGRESS, null));
         SforceLoginContext sforceLoginContext = sforceLoginService.loginToSforce();
         if (sforceLoginContext != null) {
            sforceLoginContext.setJobRequest(jobRequest);
            boolean jobStatus = sforceDataReplicationService.startReplication(sforceLoginContext);
         } else {
            // log the message that login failed
         }
         // TODO : -VG- analyse the status
         jobDataService.updateJobRequest(ExecueBeanUtils.modifyJobRequest(jobRequest, JobStatus.SUCCESS, new Date()));
      } catch (Exception e) {
         logger.debug("JOB Execution failed for job -- " + context.getJobDetail().getName());
         e.printStackTrace();
         try {
            jobDataService
                     .updateJobRequest(ExecueBeanUtils.modifyJobRequest(jobRequest, JobStatus.FAILURE, new Date()));
         } catch (QueryDataException e1) {
            logger.debug("JOB Execution failed for job -- " + context.getJobDetail().getName());
            e1.printStackTrace();
         }
      } finally {
         try {
            List<JobOperationalStatus> jobOperationalStatus = jobDataService
                     .getJobOperationalStatus(jobRequest.getId());
            List<JobHistoryOperationalStatus> jobHistoryOperationalStatus = ExecueBeanUtils
                     .cloneJobOperationStatus(jobOperationalStatus);
            jobDataService.createJobHistoryOperationalStatus(jobHistoryOperationalStatus);
            jobDataService.deleteJobOperationStatus(jobOperationalStatus);
         } catch (QueryDataException e) {
            e.printStackTrace();
         }
      }
   }
}
