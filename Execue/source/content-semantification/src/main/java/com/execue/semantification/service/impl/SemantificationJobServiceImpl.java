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


/**
 * 
 */
package com.execue.semantification.service.impl;

import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.JobType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.semantification.exception.SemantificationException;
import com.execue.semantification.service.ISemantificationJobService;

/**
 * @author Murthy
 */
public class SemantificationJobServiceImpl implements ISemantificationJobService {

   private IJobDataService jobDataService;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.semantification.service.ISemantificationJobService#createFeatureCountJobRequest()
    */
   public void createFeatureCountJobRequest () throws SemantificationException {
      try {
         Long userId = 1L; // Default it to system user
         JobRequest jobRequest = ExecueBeanManagementUtil.prepareJobRequest(JobType.FEATURE_COUNT, null,
                  JobStatus.PENDING, new Date(), userId);
         getJobDataService().createJobRequest(jobRequest);
      } catch (QueryDataException queryDataException) {
         throw new SemantificationException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, queryDataException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.semantification.service.ISemantificationJobService#getLatestFeatureCountJobRequest()
    */
   public JobRequest getLatestFeatureCountJobRequest () throws SemantificationException {
      JobRequest jobRequest = null;
      try {
         List<JobRequest> jobRequests = getJobDataService().getJobRequestByType(JobType.FEATURE_COUNT);
         if (ExecueCoreUtil.isCollectionNotEmpty(jobRequests)) {
            jobRequest = jobRequests.get(0);
         }
      } catch (QueryDataException e) {
         throw new SemantificationException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return jobRequest;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.semantification.service.ISemantificationJobService#updateFeatureCountJobRequest(com.execue.core.common.bean.entity.JobRequest)
    */
   public void updateFeatureCountJobRequest (JobRequest jobRequest) throws SemantificationException {
      try {
         getJobDataService().updateJobRequest(jobRequest);
      } catch (QueryDataException e) {
         throw new SemantificationException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
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
}