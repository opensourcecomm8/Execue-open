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


package com.execue.scheduler;

import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.jobdata.ExecueJobSchedule;

public class ExecueJob {

   /*
    * ExecueJob consists of jobData and jobSchedule
    */
   private ExecueJobData     jobData;
   private ExecueJobSchedule jobSchedule;

   private String            triggerName;
   private String            jobType;

   private int               executionStatus;

   /**
    * @return
    */
   public ExecueJobData getJobData () {
      return jobData;
   }

   /**
    * @param jobData
    */
   public void setJobData (ExecueJobData jobData) {
      this.jobData = jobData;
   }

   /**
    * @return
    */
   public ExecueJobSchedule getJobSchedule () {
      return jobSchedule;
   }

   /**
    * @param jobSchedule
    */
   public void setJobSchedule (ExecueJobSchedule jobSchedule) {
      this.jobSchedule = jobSchedule;
   }

   public String getTriggerName () {
      return triggerName;
   }

   public void setTriggerName (String triggerName) {
      this.triggerName = triggerName;
   }

   public String getJobType () {
      return jobType;
   }

   public void setJobType (String jobType) {
      this.jobType = jobType;
   }

   public int getExecutionStatus () {
      return executionStatus;
   }

   public void setExecutionStatus (int executionStatus) {
      this.executionStatus = executionStatus;
   }

}
