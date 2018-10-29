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


package com.execue.qdata.dataaccess.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.entity.AnswersCatalogContext;
import com.execue.core.common.bean.entity.JobHistoryOperationalStatus;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.type.JobType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.qdata.exception.QueryDataException;
import com.execue.dataaccess.qdata.dao.IJobDataDAO;
import com.execue.qdata.dataaccess.IJobDataAccessManager;

public class JobDataAccessManagerImpl implements IJobDataAccessManager {

   private IJobDataDAO jobDataDAO;

   public void createAnswersCatalogContext (AnswersCatalogContext answersCatalogContext) throws QueryDataException {
      try {
         getJobDataDAO().create(answersCatalogContext);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void createJobHistoryOperationalStatus (List<JobHistoryOperationalStatus> jobHistoryOperationalStatus)
            throws QueryDataException {
      try {
         for (JobHistoryOperationalStatus newJobHistoryOperationalStatus : jobHistoryOperationalStatus) {
            getJobDataDAO().create(newJobHistoryOperationalStatus);
         }
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void createJobOperationStatus (JobOperationalStatus jobOperationalStatus) throws QueryDataException {
      try {
         adjustStatusDetailForMaxLength(jobOperationalStatus);
         getJobDataDAO().create(jobOperationalStatus);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   private void adjustStatusDetailForMaxLength (JobOperationalStatus jobOperationalStatus) {
      int maxStatusDetailLength = 60000;
      if (jobOperationalStatus == null) {
         return;
      }
      if (StringUtils.isBlank(jobOperationalStatus.getStatusDetail())) {
         return;
      }
      if (jobOperationalStatus.getStatusDetail().length() <= maxStatusDetailLength) {
         return;
      }
      jobOperationalStatus.setStatusDetail(jobOperationalStatus.getStatusDetail().substring(0, (maxStatusDetailLength-1)));
   }

   public void createJobRequest (JobRequest jobRequest) throws QueryDataException {
      try {
         getJobDataDAO().create(jobRequest);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void deleteJobOperationStatus (List<JobOperationalStatus> jobOperationalStatus) throws QueryDataException {
      try {
         for (JobOperationalStatus newJobOperationalStatus : jobOperationalStatus) {
            getJobDataDAO().delete(newJobOperationalStatus);
         }
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<JobOperationalStatus> getJobOperationalStatus (long jobRequestId) throws QueryDataException {
      try {
         return getJobDataDAO().getJobOperationalStatus(jobRequestId);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updateAnswersCatalogContext (AnswersCatalogContext answersCatalogContext) throws QueryDataException {
      try {
         getJobDataDAO().update(answersCatalogContext);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void deleteAnswersCatalogContextByAssetId (Long assetId) throws QueryDataException {
      try {
         getJobDataDAO().deleteAnswersCatalogContextByAssetId(assetId);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updateJobOperationStatus (JobOperationalStatus jobOperationalStatus) throws QueryDataException {
      try {
         adjustStatusDetailForMaxLength(jobOperationalStatus);
         getJobDataDAO().update(jobOperationalStatus);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updateJobRequest (JobRequest jobRequest) throws QueryDataException {
      try {
         getJobDataDAO().update(jobRequest);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public IJobDataDAO getJobDataDAO () {
      return jobDataDAO;
   }

   public void setJobDataDAO (IJobDataDAO jobDataDAO) {
      this.jobDataDAO = jobDataDAO;
   }

   public List<JobHistoryOperationalStatus> getJobHistoryOperationalStatus (long jobRequestId)
            throws QueryDataException {
      try {
         return getJobDataDAO().getJobHistoryOperationalStatus(jobRequestId);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<JobRequest> getJobRequestByType (JobType jobType) throws QueryDataException {
      try {
         return getJobDataDAO().getJobRequestByType(jobType);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public JobRequest getJobRequestById (Long jobRequestId) throws QueryDataException {
      try {
         return getJobDataDAO().getById(jobRequestId, JobRequest.class);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<JobRequest> getJobRequestByUser (Long userId) throws QueryDataException {
      try {
         return getJobDataDAO().getJobRequestByUser(userId);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public JobType getJobTypeByLatestRequestedDate () throws QueryDataException {
      try {
         return getJobDataDAO().getJobTypeByLatestRequestedDate();
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public JobType getJobTypeByJobRequestId (Long jobRequestId) throws QueryDataException {
      try {
         return getJobDataDAO().getJobTypeByJobRequestId(jobRequestId);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public JobHistoryOperationalStatus getLatestJobHistoryOperationalStatus (long jobRequestId)
            throws QueryDataException {
      try {
         return getJobDataDAO().getLatestJobHistoryOperationalStatus(jobRequestId);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public JobOperationalStatus getLatestJobOperationalStatus (long jobRequestId) throws QueryDataException {
      try {
         return getJobDataDAO().getLatestJobOperationalStatus(jobRequestId);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public AnswersCatalogContext getAnswersCatalogContextByAssetId (Long assetId) throws QueryDataException {
      try {
         return getJobDataDAO().getAnswersCatalogContextByAssetId(assetId);
      } catch (DataAccessException e) {
         throw new QueryDataException(e.getCode(), e);
      }
   }

   @Override
   public List<AnswersCatalogContext> getAnswersCatalogContextByParentAssetIdForCubes (Long parentAssetId)
            throws QueryDataException {
      try {
         return getJobDataDAO().getAnswersCatalogContextByParentAssetIdForCubes(parentAssetId);
      } catch (DataAccessException e) {
         throw new QueryDataException(e.getCode(), e);
      }
   }

   @Override
   public List<AnswersCatalogContext> getAnswersCatalogContextByParentAssetIdForMarts (Long parentAssetId)
            throws QueryDataException {
      try {
         return getJobDataDAO().getAnswersCatalogContextByParentAssetIdForMarts(parentAssetId);
      } catch (DataAccessException e) {
         throw new QueryDataException(e.getCode(), e);
      }
   }
}
