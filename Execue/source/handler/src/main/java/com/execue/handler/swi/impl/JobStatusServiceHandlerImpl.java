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


package com.execue.handler.swi.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.entity.JobHistoryOperationalStatus;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.JobType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIJobHistory;
import com.execue.handler.bean.UIJobRequestStatus;
import com.execue.handler.swi.IJobStatusServiceHandler;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IUserManagementService;

public class JobStatusServiceHandlerImpl implements IJobStatusServiceHandler {

   private IJobDataService        jobDataService;
   private IUserManagementService usersManagementService;

   public List<UIJobHistory> getJobHistoryOperationalStatus (long jobRequestId) throws HandlerException {
      List<UIJobHistory> uiJobHistory = new ArrayList<UIJobHistory>();
      try {
         List<JobHistoryOperationalStatus> jobHistory = getJobDataService()
                  .getJobHistoryOperationalStatus(jobRequestId);
         for (JobHistoryOperationalStatus eachRecord : jobHistory) {
            uiJobHistory.add(transformToUIJobHistory(eachRecord));
         }
      } catch (QueryDataException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
      return uiJobHistory;
   }

   public List<UIJobHistory> getJobOperationalStatus (long jobRequestId) throws HandlerException {
      List<UIJobHistory> uiJobHistory = new ArrayList<UIJobHistory>();
      try {
         List<JobOperationalStatus> jobHistory = getJobDataService().getJobOperationalStatus(jobRequestId);
         for (JobOperationalStatus eachRecord : jobHistory) {
            uiJobHistory.add(transformToUIJobHistory(eachRecord));
         }
      } catch (QueryDataException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }

      return uiJobHistory;
   }

   private UIJobHistory transformToUIJobHistory (JobOperationalStatus jobOperationalStatus) {
      UIJobHistory uiJobHistory = new UIJobHistory();
      uiJobHistory.setEndDate(jobOperationalStatus.getEndDate());
      uiJobHistory.setStartDate(jobOperationalStatus.getStartDate());
      uiJobHistory.setJobStatus(jobOperationalStatus.getJobStatus());
      uiJobHistory.setJobType(jobOperationalStatus.getJobType());
      uiJobHistory.setOperationalStage(jobOperationalStatus.getOperationalStage());
      uiJobHistory.setStatusDetail(jobOperationalStatus.getStatusDetail());
      return uiJobHistory;
   }

   private UIJobHistory transformToUIJobHistory (JobHistoryOperationalStatus jobHistoryOperationalStatus) {
      UIJobHistory uiJobHistory = new UIJobHistory();
      uiJobHistory.setEndDate(jobHistoryOperationalStatus.getEndDate());
      uiJobHistory.setStartDate(jobHistoryOperationalStatus.getStartDate());
      uiJobHistory.setJobStatus(jobHistoryOperationalStatus.getJobStatus());
      uiJobHistory.setJobType(jobHistoryOperationalStatus.getJobType());
      uiJobHistory.setOperationalStage(jobHistoryOperationalStatus.getOperationalStage());
      uiJobHistory.setStatusDetail(jobHistoryOperationalStatus.getStatusDetail());
      return uiJobHistory;
   }

   public UIJobRequestStatus getJobRequestStatus (JobType jobType) throws HandlerException {
      JobRequest selectedRequest = null;
      try {
         List<JobRequest> jobRequests = getJobDataService().getJobRequestByType(jobType);
         long requestId = -1;
         // TODO: SNM - need to run this logic in HSQL
         for (JobRequest eachRequest : jobRequests) {
            if (eachRequest.getId() > requestId) {
               requestId = eachRequest.getId();
               selectedRequest = eachRequest;
            }
         }
         if (CollectionUtils.isEmpty(jobRequests)) {
            return null;
         }
      } catch (QueryDataException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
      return transformAsUIJobRequestStatus(selectedRequest);
   }

   public UIJobRequestStatus getJobRequestStatus (Long jobRequestId) throws HandlerException {
      try {
         return transformAsUIJobRequestStatus(getJobDataService().getJobRequestById(jobRequestId));
      } catch (QueryDataException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
   }

   private UIJobRequestStatus transformAsUIJobRequestStatus (JobRequest jobRequest) {
      UIJobRequestStatus uiCubeRequestStatus = new UIJobRequestStatus();
      uiCubeRequestStatus.setCompletionDate(jobRequest.getCompletionDate());
      uiCubeRequestStatus.setRequestedDate(jobRequest.getRequestedDate());
      uiCubeRequestStatus.setJobStatus(jobRequest.getJobStatus());
      uiCubeRequestStatus.setRequestData(jobRequest.getRequestData());
      uiCubeRequestStatus.setJobType(jobRequest.getJobType());
      uiCubeRequestStatus.setId(jobRequest.getId());
      return uiCubeRequestStatus;
   }

   private List<UIJobRequestStatus> transformUIJobRequestStatus (List<JobRequest> jobsRequest) throws HandlerException {
      List<UIJobRequestStatus> uiJobs = new ArrayList<UIJobRequestStatus>();
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(jobsRequest)) {
            for (JobRequest jobRequest : jobsRequest) {
               UIJobRequestStatus uiJobRequestStatus = new UIJobRequestStatus();
               uiJobRequestStatus.setId(jobRequest.getId());
               uiJobRequestStatus.setJobStatus(jobRequest.getJobStatus());
               uiJobRequestStatus.setJobType(jobRequest.getJobType());
               User user = usersManagementService.getUserById(jobRequest.getUserId());
               if (user != null) {
                  uiJobRequestStatus.setUserName(user.getUsername());
               }
               uiJobRequestStatus.setRequestedDate(jobRequest.getRequestedDate());
               uiJobs.add(uiJobRequestStatus);
            }
         }
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
      return uiJobs;

   }

   public JobType getJobTypeByLatestRequestedDate () throws HandlerException {
      try {
         return getJobDataService().getJobTypeByLatestRequestedDate();
      } catch (QueryDataException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }

   }

   public List<UIJobRequestStatus> getJobRequestByType (JobType jobType) throws HandlerException {
      try {
         List<JobRequest> JobsRequest = getJobDataService().getJobRequestByType(jobType);
         return transformUIJobRequestStatus(JobsRequest);
      } catch (QueryDataException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }

   }

   /**
    * @return the usersManagementService
    */
   public IUserManagementService getUsersManagementService () {
      return usersManagementService;
   }

   /**
    * @param usersManagementService
    *           the usersManagementService to set
    */
   public void setUsersManagementService (IUserManagementService usersManagementService) {
      this.usersManagementService = usersManagementService;
   }

   public UIJobHistory getLatestJobHistoryOperationalStatus (long jobRequestId) throws HandlerException {
      UIJobHistory uiJobHistory = null;
      try {
         uiJobHistory = transformToUIJobHistory(getJobDataService().getLatestJobHistoryOperationalStatus(jobRequestId));
      } catch (QueryDataException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
      return uiJobHistory;
   }

   public UIJobHistory getLatestJobOperationalStatus (long jobRequestId) throws HandlerException {
      UIJobHistory uiJobHistory = null;
      try {
         uiJobHistory = transformToUIJobHistory(getJobDataService().getLatestJobOperationalStatus(jobRequestId));
      } catch (QueryDataException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
      return uiJobHistory;
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

}
