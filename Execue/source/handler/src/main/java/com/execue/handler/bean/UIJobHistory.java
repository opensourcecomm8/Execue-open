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


package com.execue.handler.bean;

import java.util.Date;

import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.JobType;


public class UIJobHistory {
   
   private JobType           jobType;
   private String            operationalStage;
   private JobStatus         jobStatus;
   private String            statusDetail;
   private Date              startDate;
   private Date              endDate;
   private long              userId;
   
   public JobType getJobType () {
      return jobType;
   }
   
   public void setJobType (JobType jobType) {
      this.jobType = jobType;
   }
   
   public String getOperationalStage () {
      return operationalStage;
   }
   
   public void setOperationalStage (String operationalStage) {
      this.operationalStage = operationalStage;
   }
   
   public JobStatus getJobStatus () {
      return jobStatus;
   }
   
   public void setJobStatus (JobStatus jobStatus) {
      this.jobStatus = jobStatus;
   }
   
   public String getStatusDetail () {
      return statusDetail;
   }
   
   public void setStatusDetail (String statusDetail) {
      this.statusDetail = statusDetail;
   }
   
   public Date getStartDate () {
      return startDate;
   }
   
   public void setStartDate (Date startDate) {
      this.startDate = startDate;
   }
   
   public Date getEndDate () {
      return endDate;
   }
   
   public void setEndDate (Date endDate) {
      this.endDate = endDate;
   }
   
   public long getUserId () {
      return userId;
   }
   
   public void setUserId (long userId) {
      this.userId = userId;
   }

}