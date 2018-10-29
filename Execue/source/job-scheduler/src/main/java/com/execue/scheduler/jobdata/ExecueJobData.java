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


package com.execue.scheduler.jobdata;

import java.io.Serializable;
import java.util.Date;

import com.execue.core.common.bean.IExeCueJobRequestContextData;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.type.JobType;

public class ExecueJobData implements Serializable {

   /**
    * 
    */
   private static final long            serialVersionUID   = 7938280439084168223L;
   /*
    * ExecueJobData has all the necessary info pertaining to a job JobClass is the class with execution logic
    */
   private JobRequest                   jobRequest;

   private String                       jobName;
   private String                       jobController;
   private JobType                      jobType;
   private Class                        jobClass;
   private String                       jobData;
   private Date                         previousExecTime;
   private Date                         nextExecTime;
   private Date                         jobStartTime;
   private Date                         jobEndTime;
   private long                         jobDuration;
   private int                          jobStatus;
   private Date                         dateAdded;
   private Date                         dateModified;

   private String                       jobGroup;
   private String                       jobDescription;
   private boolean                      isScheduled        = false;
   private String                       reportToEmail;

   private String                       seedOperation;

   private IExeCueJobRequestContextData requestContextData;
   private boolean                      notificationNeeded = false;

   public JobRequest getJobRequest () {
      return jobRequest;
   }

   public void setJobRequest (JobRequest jobRequest) {
      this.jobRequest = jobRequest;
   }

   public Date getDateAdded () {
      return dateAdded;
   }

   public void setDateAdded (Date dateAdded) {
      this.dateAdded = dateAdded;
   }

   public Date getDateModified () {
      return dateModified;
   }

   public void setDateModified (Date dateModified) {
      this.dateModified = dateModified;
   }

   public Class getJobClass () {
      return jobClass;
   }

   public void setJobClass (Class jobClass) {
      this.jobClass = jobClass;
   }

   public String getJobData () {
      return jobData;
   }

   public void setJobData (String jobData) {
      this.jobData = jobData;
   }

   public String getJobDescription () {
      return jobDescription;
   }

   public void setJobDescription (String jobDescription) {
      this.jobDescription = jobDescription;
   }

   public String getJobGroup () {
      return jobGroup;
   }

   public void setJobGroup (String jobGroup) {
      this.jobGroup = jobGroup;
   }

   public String getJobName () {
      return jobName;
   }

   public void setJobName (String jobName) {
      this.jobName = jobName;
   }

   public int getJobStatus () {
      return jobStatus;
   }

   public void setJobStatus (int jobStatus) {
      this.jobStatus = jobStatus;
   }

   public JobType getJobType () {
      return jobType;
   }

   public void setJobType (JobType jobType) {
      this.jobType = jobType;
   }

   public Date getJobEndTime () {
      return jobEndTime;
   }

   public void setJobEndTime (Date jobEndTime) {
      this.jobEndTime = jobEndTime;
   }

   public Date getJobStartTime () {
      return jobStartTime;
   }

   public void setJobStartTime (Date jobStartTime) {
      this.jobStartTime = jobStartTime;
   }

   public long getJobDuration () {
      return jobDuration;
   }

   public void setJobDuration (long jobDuration) {
      this.jobDuration = jobDuration;
   }

   public String getJobController () {
      return jobController;
   }

   public void setJobController (String jobController) {
      this.jobController = jobController;
   }

   public String getReportToEmail () {
      return reportToEmail;
   }

   public void setReportToEmail (String reportToEmail) {
      this.reportToEmail = reportToEmail;
   }

   public Date getPreviousExecTime () {
      return previousExecTime;
   }

   public void setPreviousExecTime (Date previousExecTime) {
      this.previousExecTime = previousExecTime;
   }

   public Date getNextExecTime () {
      return nextExecTime;
   }

   public void setNextExecTime (Date nextExecTime) {
      this.nextExecTime = nextExecTime;
   }

   public String getSeedOperation () {
      return seedOperation;
   }

   public void setSeedOperation (String seedOperation) {
      this.seedOperation = seedOperation;
   }

   /**
    * @return the requestContextData
    */
   public IExeCueJobRequestContextData getRequestContextData () {
      return requestContextData;
   }

   /**
    * @param requestContextData
    *           the requestContextData to set
    */
   public void setRequestContextData (IExeCueJobRequestContextData requestContextData) {
      this.requestContextData = requestContextData;
   }

   /**
    * @return the notificationNeeded
    */
   public boolean isNotificationNeeded () {
      return notificationNeeded;
   }

   /**
    * @param notificationNeeded
    *           the notificationNeeded to set
    */
   public void setNotificationNeeded (boolean notificationNeeded) {
      this.notificationNeeded = notificationNeeded;
   }

   public boolean isScheduled () {
      return isScheduled;
   }

   public void setScheduled (boolean isScheduled) {
      this.isScheduled = isScheduled;
   }

}
