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


package com.execue.handler.swi;

import java.util.List;

import com.execue.core.common.type.JobType;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIJobHistory;
import com.execue.handler.bean.UIJobRequestStatus;

public interface IJobStatusServiceHandler {

   public UIJobRequestStatus getJobRequestStatus (JobType jobType) throws HandlerException;

   public List<UIJobHistory> getJobHistoryOperationalStatus (long jobRequestId) throws HandlerException;

   public List<UIJobHistory> getJobOperationalStatus (long jobRequestId) throws HandlerException;

   public UIJobRequestStatus getJobRequestStatus (Long jobRequestId) throws HandlerException;

   public JobType getJobTypeByLatestRequestedDate () throws HandlerException;

   public List<UIJobRequestStatus> getJobRequestByType (JobType jobType) throws HandlerException;
   
   public UIJobHistory getLatestJobHistoryOperationalStatus (long jobRequestId) throws HandlerException;

   public UIJobHistory getLatestJobOperationalStatus (long jobRequestId) throws HandlerException;
}
