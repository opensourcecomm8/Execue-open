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

import com.execue.core.common.type.JobStatus;

/**
 * JSON object representation for Job Request Result. Carries error message, status enum and id of the job requested
 * 
 * @author execue
 */
public class UIJobRequestResult {

   private String    errMsg;
   private JobStatus status;
   private Long      jobId;

   /**
    * @return the errMsg
    */
   public String getErrMsg () {
      return errMsg;
   }

   /**
    * @param errMsg
    *           the errMsg to set
    */
   public void setErrMsg (String errMsg) {
      this.errMsg = errMsg;
   }

   /**
    * @return the status
    */
   public JobStatus getStatus () {
      return status;
   }

   /**
    * @param status
    *           the status to set
    */
   public void setStatus (JobStatus status) {
      this.status = status;
   }

   /**
    * @return the jobId
    */
   public Long getJobId () {
      return jobId;
   }

   /**
    * @param jobId
    *           the jobId to set
    */
   public void setJobId (Long jobId) {
      this.jobId = jobId;
   }
}
