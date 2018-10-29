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


package com.execue.web.core.action.swi;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.JobType;
import com.execue.core.exception.ExeCueException;
import com.execue.handler.bean.UIJobCurrentStatus;
import com.execue.handler.bean.UIJobHistory;
import com.execue.handler.bean.UIJobRequestStatus;

public class JobRequestStatusAction extends SWIAction {

   private static final long        serialVersionUID = 132252941747834476L;

   private List<UIJobRequestStatus> jobs;
   private JobType                  selectedJobType;
   private UIJobRequestStatus       jobRequest;
   private List<UIJobHistory>       jobStatusSteps;
   private static String            UPLOAD_STATUS    = "upload";
   private static final Logger      log              = Logger.getLogger(JobRequestStatusAction.class);
   private UIJobCurrentStatus       latestJobStatus;
   private UIJobHistory             latestJobStatusDetail;

   // TODO : is only here to support getCurrentJobStatus() action method
   private Long                     jobRequestId;

   public String input () {
      try {
         selectedJobType = getJobStatusServiceHandler().getJobTypeByLatestRequestedDate();
         getJobsByType();
      } catch (ExeCueException e) {
         e.printStackTrace();
         return ERROR;
      }
      return SUCCESS;
   }

   public String getJobsByType () {
      try {
         jobs = getJobStatusServiceHandler().getJobRequestByType(selectedJobType);
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String getJobStatus () {
      if (log.isDebugEnabled()) {
         log.debug("Job Request Id : " + jobRequest.getId());
      }
      if (JobType.PUBLISHER_DATA_ABSORPTION.equals(jobRequest.getJobType())
               || JobType.PUBLISHER_DATA_EVALUATION.equals(jobRequest.getJobType())) {
         return UPLOAD_STATUS;

      } else {
         try {
            setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(getJobRequest().getId()));
            if (JobStatus.SUCCESS.equals(getJobRequest().getJobStatus())
                     || JobStatus.FAILURE.equals(getJobRequest().getJobStatus())) {
               setJobStatusSteps(getJobStatusServiceHandler().getJobHistoryOperationalStatus(getJobRequest().getId()));
            } else {
               setJobStatusSteps(getJobStatusServiceHandler().getJobOperationalStatus(getJobRequest().getId()));
            }

         } catch (ExeCueException exception) {
            log.error(exception, exception);
            addActionError("An error occurred while extracting request information");
         }

      }
      return SUCCESS;

   }
   
   public String showStatusDetails () {
      try {
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(getJobRequestId()));
         if (JobStatus.SUCCESS.equals(getJobRequest().getJobStatus())
                  || JobStatus.FAILURE.equals(getJobRequest().getJobStatus())) {
            setJobStatusSteps(getJobStatusServiceHandler().getJobHistoryOperationalStatus(getJobRequest().getId()));
         } else {
            setJobStatusSteps(getJobStatusServiceHandler().getJobOperationalStatus(getJobRequest().getId()));
         }
      } catch (ExeCueException exception) {
         log.error(exception, exception);
         addActionError("An error occurred while extracting request information");
      }
      return SUCCESS;
   }
   
   public String getCurrentJobStatus () {
      try {
         latestJobStatus = new UIJobCurrentStatus();
         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(getJobRequestId()));
         getLatestJobStatus().setJobId(getJobRequest().getId());
         getLatestJobStatus().setStatus(getJobRequest().getJobStatus());
         if (JobStatus.SUCCESS.equals(getJobRequest().getJobStatus())
                  || JobStatus.FAILURE.equals(getJobRequest().getJobStatus())) {
            setLatestJobStatusDetail(getJobStatusServiceHandler().getLatestJobHistoryOperationalStatus(
                     getJobRequest().getId()));
         } else {
            setLatestJobStatusDetail(getJobStatusServiceHandler()
                     .getLatestJobOperationalStatus(getJobRequest().getId()));
         }
         getLatestJobStatus().setCurrMsg(getLatestJobStatusDetail().getOperationalStage());
      } catch (ExeCueException exception) {
         log.error(exception, exception);
         addActionError("An error occurred while extracting request information");
         getLatestJobStatus().setCurrMsg("An error occurred while extracting request information");
         getLatestJobStatus().setStatus(JobStatus.FAILURE);
         return SUCCESS;
      }
      return SUCCESS;
   }

   public JobOperationalStatus getStatusByJob () {
      return new JobOperationalStatus();
   }

   public List<JobType> getJobTypes () {
      return Arrays.asList(JobType.values());
   }

   /**
    * @return the jobs
    */
   public List<UIJobRequestStatus> getJobs () {
      return jobs;
   }

   /**
    * @param jobs
    *           the jobs to set
    */
   public void setJobs (List<UIJobRequestStatus> jobs) {
      this.jobs = jobs;
   }

   /**
    * @return the selectedJobType
    */
   public JobType getSelectedJobType () {
      return selectedJobType;
   }

   /**
    * @param selectedJobType
    *           the selectedJobType to set
    */
   public void setSelectedJobType (JobType selectedJobType) {
      this.selectedJobType = selectedJobType;
   }

   /**
    * @return the jobStatusSteps
    */
   public List<UIJobHistory> getJobStatusSteps () {
      return jobStatusSteps;
   }

   /**
    * @param jobStatusSteps
    *           the jobStatusSteps to set
    */
   public void setJobStatusSteps (List<UIJobHistory> jobStatusSteps) {
      this.jobStatusSteps = jobStatusSteps;
   }

   /**
    * @return the jobRequest
    */
   public UIJobRequestStatus getJobRequest () {
      return jobRequest;
   }

   /**
    * @param jobRequest
    *           the jobRequest to set
    */
   public void setJobRequest (UIJobRequestStatus jobRequest) {
      this.jobRequest = jobRequest;
   }

   /**
    * @return the latestJobStatus
    */
   public UIJobCurrentStatus getLatestJobStatus () {
      return latestJobStatus;
   }

   /**
    * @param latestJobStatus
    *           the latestJobStatus to set
    */
   public void setLatestJobStatus (UIJobCurrentStatus latestJobStatus) {
      this.latestJobStatus = latestJobStatus;
   }

   /**
    * @return the latestJobStatusDetail
    */
   public UIJobHistory getLatestJobStatusDetail () {
      return latestJobStatusDetail;
   }

   /**
    * @param latestJobStatusDetail
    *           the latestJobStatusDetail to set
    */
   public void setLatestJobStatusDetail (UIJobHistory latestJobStatusDetail) {
      this.latestJobStatusDetail = latestJobStatusDetail;
   }

   /**
    * @return the jobRequestId
    */
   public Long getJobRequestId () {
      return jobRequestId;
   }

   /**
    * @param jobRequestId
    *           the jobRequestId to set
    */
   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

}
