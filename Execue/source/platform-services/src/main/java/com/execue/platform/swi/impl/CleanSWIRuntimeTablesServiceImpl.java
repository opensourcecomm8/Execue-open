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


package com.execue.platform.swi.impl;

import java.util.Date;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.swi.RuntimeTablesCleanupContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.platform.swi.ICleanSWIRuntimeTablesService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IApplicationDeletionService;
import com.execue.swi.service.IPopularityService;
import com.execue.swi.service.IUserQueryPossibilityService;

public class CleanSWIRuntimeTablesServiceImpl implements ICleanSWIRuntimeTablesService {

   private static final Logger          logger = Logger.getLogger(CleanSWIRuntimeTablesServiceImpl.class);
   private IUserQueryPossibilityService userQueryPossibilityService;
   private IPopularityService           popularityService;
   private IApplicationDeletionService  applicationDeletionService;
   private IJobDataService              jobDataService;

   public void cleanSWIRuntimeTables (RuntimeTablesCleanupContext runtimeTablesCleanupContext) throws SWIException {
      JobOperationalStatus jobOperationalStatus = null;
      JobRequest jobRequest = runtimeTablesCleanupContext.getJobRequest();
      try {
         try {

            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Cleaning UserQueryPossiblities Table", JobStatus.INPROGRESS, null, new Date());
            jobDataService.createJobOperationStatus(jobOperationalStatus);
            getUserQueryPossibilityService().cleanUserQueryPossibilities();
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         } catch (SWIException swiException) {
            if (logger.isDebugEnabled()) {
               logger
                        .debug("Exception occured while cleaning UserQueryPossiblities. Proceeding further to cleanup Popularities ");
            }
         }
         try {
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Cleaning PopularityHit Table", JobStatus.INPROGRESS, null, new Date());
            jobDataService.createJobOperationStatus(jobOperationalStatus);
            getPopularityService().deleteProcessedPopularityHit();
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         } catch (SWIException swiException) {
            if (logger.isDebugEnabled()) {
               logger
                        .debug("Exception occured while cleaning Popularities . Proceeding further to cleanup Application Operations ");
            }
         }
         try {
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Cleaning ApplicationOperations Table", JobStatus.INPROGRESS, null, new Date());
            jobDataService.createJobOperationStatus(jobOperationalStatus);
            getApplicationDeletionService().deleteApplicationOperationData();
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         } catch (SWIException swiException) {
            if (logger.isDebugEnabled()) {
               logger.debug("Exception occured while Cleaning ApplicationOperation Records");
            }
         }
      } catch (ExeCueException exception) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, exception
                     .getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new SWIException(SWIExceptionCodes.RUNTIME_TABLES_CLEANUP_FAILURE_CODE, exception);
            }
         }
         throw new SWIException(SWIExceptionCodes.RUNTIME_TABLES_CLEANUP_FAILURE_CODE, exception);
      }
   }

   public IUserQueryPossibilityService getUserQueryPossibilityService () {
      return userQueryPossibilityService;
   }

   public void setUserQueryPossibilityService (IUserQueryPossibilityService userQueryPossibilityService) {
      this.userQueryPossibilityService = userQueryPossibilityService;
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public IApplicationDeletionService getApplicationDeletionService () {
      return applicationDeletionService;
   }

   public void setApplicationDeletionService (IApplicationDeletionService applicationDeletionService) {
      this.applicationDeletionService = applicationDeletionService;
   }

   public void setPopularityService (IPopularityService popularityService) {
      this.popularityService = popularityService;
   }

   public IPopularityService getPopularityService () {
      return popularityService;
   }

}
