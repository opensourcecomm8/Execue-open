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

public class UIJobRequestStatus {

   private long      id;
   private JobType   jobType;
   private String    requestData;
   private JobStatus jobStatus;
   private Date      requestedDate;
   private Date      completionDate;
   private String    userName;

   public JobType getJobType () {
      return jobType;
   }

   public void setJobType (JobType jobType) {
      this.jobType = jobType;
   }

   public String getRequestData () {
      return requestData;
   }

   public void setRequestData (String requestData) {
      this.requestData = requestData;
   }

   public JobStatus getJobStatus () {
      return jobStatus;
   }

   public void setJobStatus (JobStatus jobStatus) {
      this.jobStatus = jobStatus;
   }

   public Date getRequestedDate () {
      return requestedDate;
   }

   public void setRequestedDate (Date requestedDate) {
      this.requestedDate = requestedDate;
   }

   public Date getCompletionDate () {
      return completionDate;
   }

   public void setCompletionDate (Date completionDate) {
      this.completionDate = completionDate;
   }

   public long getId () {
      return id;
   }

   public void setId (long id) {
      this.id = id;
   }

   /**
    * @return the userName
    */
   public String getUserName () {
      return userName;
   }

   /**
    * @param userName
    *           the userName to set
    */
   public void setUserName (String userName) {
      this.userName = userName;
   }

}
