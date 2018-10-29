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


package com.execue.core.common.bean.entity;

import java.io.Serializable;

import com.execue.core.common.type.AppOperationType;
import com.execue.core.common.type.JobStatus;

/**
 * Represents previously requested Operation and the Job assigned for the operation. Status of the operation be taken
 * from the JobRequest associated with the jobRequestId. OperationType 'None' represents there was no operation
 * requested on the Application earlier.
 * 
 * @author Raju Gottumukkala
 * @since 4.4.4.R1
 */
public class ApplicationOperation implements Serializable {

   private Long             id;
   private Long             applicationId;
   private AppOperationType operationType   = AppOperationType.NONE;
   private Long             jobRequestId;
   private JobStatus        operationStatus = JobStatus.INPROGRESS;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public AppOperationType getOperationType () {
      return operationType;
   }

   public void setOperationType (AppOperationType operationType) {
      this.operationType = operationType;
   }

   public Long getJobRequestId () {
      return jobRequestId;
   }

   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

   public JobStatus getOperationStatus () {
      return operationStatus;
   }

   public void setOperationStatus (JobStatus operationStatus) {
      this.operationStatus = operationStatus;
   }

}
