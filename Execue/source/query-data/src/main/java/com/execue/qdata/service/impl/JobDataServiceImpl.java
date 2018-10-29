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


package com.execue.qdata.service.impl;

import java.util.List;

import com.execue.core.common.bean.ac.CubeCreationContext;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.AnswersCatalogContext;
import com.execue.core.common.bean.entity.JobHistoryOperationalStatus;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.type.JobType;
import com.execue.core.util.ExeCueXMLUtils;
import com.execue.qdata.dataaccess.IJobDataAccessManager;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;

/**
 * This service contains the methods for job status information
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public class JobDataServiceImpl implements IJobDataService {

   private IJobDataAccessManager jobDataAccessManager;

   public void createAnswersCatalogContext (AnswersCatalogContext answersCatalogContext) throws QueryDataException {
      getJobDataAccessManager().createAnswersCatalogContext(answersCatalogContext);
   }

   public void createJobHistoryOperationalStatus (List<JobHistoryOperationalStatus> jobHistoryOperationalStatus)
            throws QueryDataException {
      getJobDataAccessManager().createJobHistoryOperationalStatus(jobHistoryOperationalStatus);
   }

   public void createJobOperationStatus (JobOperationalStatus jobOperationalStatus) throws QueryDataException {
      getJobDataAccessManager().createJobOperationStatus(jobOperationalStatus);
   }

   public void createJobRequest (JobRequest jobRequest) throws QueryDataException {
      getJobDataAccessManager().createJobRequest(jobRequest);
   }

   public void deleteJobOperationStatus (List<JobOperationalStatus> jobOperationalStatus) throws QueryDataException {
      getJobDataAccessManager().deleteJobOperationStatus(jobOperationalStatus);
   }

   public List<JobOperationalStatus> getJobOperationalStatus (long jobRequestId) throws QueryDataException {
      return getJobDataAccessManager().getJobOperationalStatus(jobRequestId);
   }

   public void updateAnswersCatalogContext (AnswersCatalogContext answersCatalogContext) throws QueryDataException {
      getJobDataAccessManager().updateAnswersCatalogContext(answersCatalogContext);
   }

   public void deleteAnswersCatalogContextByAssetId (Long assetId) throws QueryDataException {
      getJobDataAccessManager().deleteAnswersCatalogContextByAssetId(assetId);
   }

   public void updateJobOperationStatus (JobOperationalStatus jobOperationalStatus) throws QueryDataException {
      getJobDataAccessManager().updateJobOperationStatus(jobOperationalStatus);
   }

   public void updateJobRequest (JobRequest jobRequest) throws QueryDataException {
      getJobDataAccessManager().updateJobRequest(jobRequest);
   }

   public IJobDataAccessManager getJobDataAccessManager () {
      return jobDataAccessManager;
   }

   public void setJobDataAccessManager (IJobDataAccessManager jobDataAccessManager) {
      this.jobDataAccessManager = jobDataAccessManager;
   }

   public List<JobHistoryOperationalStatus> getJobHistoryOperationalStatus (long jobRequestId)
            throws QueryDataException {
      return getJobDataAccessManager().getJobHistoryOperationalStatus(jobRequestId);
   }

   public List<JobRequest> getJobRequestByType (JobType jobType) throws QueryDataException {
      return getJobDataAccessManager().getJobRequestByType(jobType);
   }

   public JobRequest getJobRequestById (Long jobRequestId) throws QueryDataException {
      return getJobDataAccessManager().getJobRequestById(jobRequestId);
   }

   public List<JobRequest> getJobRequestByUser (Long userId) throws QueryDataException {
      return getJobDataAccessManager().getJobRequestByUser(userId);
   }

   public JobType getJobTypeByLatestRequestedDate () throws QueryDataException {
      return getJobDataAccessManager().getJobTypeByLatestRequestedDate();
   }

   public JobHistoryOperationalStatus getLatestJobHistoryOperationalStatus (long jobRequestId)
            throws QueryDataException {
      return getJobDataAccessManager().getLatestJobHistoryOperationalStatus(jobRequestId);
   }

   public JobOperationalStatus getLatestJobOperationalStatus (long jobRequestId) throws QueryDataException {
      return getJobDataAccessManager().getLatestJobOperationalStatus(jobRequestId);
   }

   public CubeCreationContext getCubeCreationContextByAssetId (Long assetId) throws QueryDataException {
      CubeCreationContext cubeCreationContext = null;
      AnswersCatalogContext answersCatalogContext = getJobDataAccessManager()
               .getAnswersCatalogContextByAssetId(assetId);
      if (answersCatalogContext != null) {
         cubeCreationContext = (CubeCreationContext) ExeCueXMLUtils.getObjectFromXMLString(answersCatalogContext
                  .getContextData());
      }
      return cubeCreationContext;
   }

   public MartCreationContext getMartCreationContextByAssetId (Long assetId) throws QueryDataException {
      MartCreationContext martCreationContext = null;
      AnswersCatalogContext answersCatalogContext = getJobDataAccessManager()
               .getAnswersCatalogContextByAssetId(assetId);
      if (answersCatalogContext != null) {
         martCreationContext = (MartCreationContext) ExeCueXMLUtils.getObjectFromXMLString(answersCatalogContext
                  .getContextData());
      }
      return martCreationContext;
   }

   @Override
   public AnswersCatalogContext getAnswersCatalogContextByAssetId (Long assetId) throws QueryDataException {
      return getJobDataAccessManager().getAnswersCatalogContextByAssetId(assetId);
   }

   @Override
   public List<AnswersCatalogContext> getAnswersCatalogContextByParentAssetIdForCubes (Long parentAssetId)
            throws QueryDataException {
      return getJobDataAccessManager().getAnswersCatalogContextByParentAssetIdForCubes(parentAssetId);
   }

   @Override
   public List<AnswersCatalogContext> getAnswersCatalogContextByParentAssetIdForMarts (Long parentAssetId)
            throws QueryDataException {
      return getJobDataAccessManager().getAnswersCatalogContextByParentAssetIdForMarts(parentAssetId);
   }

}
