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
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.platform.swi.IVerticalPopularityUpdationService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IVerticalService;

public class VerticalPopularityUpdationServiceImpl implements IVerticalPopularityUpdationService {

   private IJobDataService     jobDataService;
   private IVerticalService    verticalService;
   private static final Logger logger = Logger.getLogger(VerticalPopularityUpdationServiceImpl.class);

   public void populateVerticalPopularityJob (JobRequest jobrequest) throws SWIException {
      JobOperationalStatus jobOperationalStatus = null;
      try {
         try {
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobrequest,
                     "Started population of verticalPopularity job", JobStatus.INPROGRESS,
                     "Started population of verticalPopularity job", new Date());
            getJobDataService().createJobOperationStatus(jobOperationalStatus);
            // starting the population code via dao services
            getVerticalService().populateVerticalPopularity();
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, "Completed", new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         } catch (SWIException swie) {
            if (logger.isDebugEnabled()) {
               logger.debug("Exception occured while running the vertical populartiy job: " + swie.getMessage());
            }
         }
      } catch (ExeCueException exception) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, exception
                     .getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException ede) {
               throw new SWIException(SWIExceptionCodes.VERTICAL_POPULARITY_JOB_SCHEDULING_FAILURE_CODE, ede);
            }
         }
         throw new SWIException(SWIExceptionCodes.VERTICAL_POPULARITY_JOB_SCHEDULING_FAILURE_CODE, exception);
      }
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public IVerticalService getVerticalService () {
      return verticalService;
   }

   public void setVerticalService (IVerticalService verticalService) {
      this.verticalService = verticalService;
   }
}