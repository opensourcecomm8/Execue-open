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


package com.execue.qdata.dataaccess;

import java.util.List;

import com.execue.core.common.bean.entity.AnswersCatalogContext;
import com.execue.core.common.bean.entity.JobHistoryOperationalStatus;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.type.JobType;
import com.execue.qdata.exception.QueryDataException;

public interface IJobDataAccessManager {

   public void createJobRequest (JobRequest jobRequest) throws QueryDataException;

   public void updateJobRequest (JobRequest jobRequest) throws QueryDataException;

   public void createJobOperationStatus (JobOperationalStatus jobOperationalStatus) throws QueryDataException;

   public void updateJobOperationStatus (JobOperationalStatus jobOperationalStatus) throws QueryDataException;

   public void deleteJobOperationStatus (List<JobOperationalStatus> jobOperationalStatus) throws QueryDataException;

   public void createJobHistoryOperationalStatus (List<JobHistoryOperationalStatus> jobHistoryOperationalStatus)
            throws QueryDataException;

   public void createAnswersCatalogContext (AnswersCatalogContext answersCatalogContext) throws QueryDataException;

   public void updateAnswersCatalogContext (AnswersCatalogContext answersCatalogContext) throws QueryDataException;

   public void deleteAnswersCatalogContextByAssetId (Long assetId) throws QueryDataException;

   public List<JobOperationalStatus> getJobOperationalStatus (long jobRequestId) throws QueryDataException;

   public List<JobHistoryOperationalStatus> getJobHistoryOperationalStatus (long jobRequestId)
            throws QueryDataException;

   public List<JobRequest> getJobRequestByType (JobType jobType) throws QueryDataException;

   public JobRequest getJobRequestById (Long jobRequestId) throws QueryDataException;

   public List<JobRequest> getJobRequestByUser (Long userId) throws QueryDataException;

   public JobType getJobTypeByLatestRequestedDate () throws QueryDataException;

   public JobType getJobTypeByJobRequestId (Long jobRequestId) throws QueryDataException;

   public JobOperationalStatus getLatestJobOperationalStatus (long jobRequestId) throws QueryDataException;

   public JobHistoryOperationalStatus getLatestJobHistoryOperationalStatus (long jobRequestId)
            throws QueryDataException;

   public AnswersCatalogContext getAnswersCatalogContextByAssetId (Long assetId) throws QueryDataException;

   public List<AnswersCatalogContext> getAnswersCatalogContextByParentAssetIdForCubes (Long parentAssetId)
            throws QueryDataException;

   public List<AnswersCatalogContext> getAnswersCatalogContextByParentAssetIdForMarts (Long parentAssetId)
            throws QueryDataException;

}
