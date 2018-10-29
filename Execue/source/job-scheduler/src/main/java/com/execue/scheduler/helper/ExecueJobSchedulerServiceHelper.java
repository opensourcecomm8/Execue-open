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


package com.execue.scheduler.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.execue.scheduler.ExecueJob;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.jobdata.ExecueJobSchedule;
import com.execue.scheduler.props.ExecueTaskProps;
import com.execue.scheduler.util.JobSchedulerUtils;

public class ExecueJobSchedulerServiceHelper {

   private JobSchedulerUtils jobSchedulerUtils;
   private Logger            logger = Logger.getLogger(ExecueJobSchedulerServiceHelper.class);

   /**
    * this prepares the onDemand job, i.e., a JobDetail controller with no associated triggers Durability has to be set
    * to true for the job to be able to successfully added to the scheduler Volatility has to be set to true for the job
    * to be able to survive across scheduler restarts These jobs can be triggered by
    * scheduler.triggerJobWithVolatileTrigger(jobName, jobGroup) If the JobDetail[jobName:jobGroup] already exists, we
    * get a SchedulerException
    * 
    * @param jobName
    * @param jobGroup
    * @param jobClass
    * @return
    * @throws SchedulerException
    */
   public boolean registerJobDetailWithoutTrigger (ExecueJobData jobData) throws SchedulerException {

      JobDetail jobDetail = getJobDetailController(jobData);
      jobSchedulerUtils.registerJobDetailWithoutTrigger(jobDetail);

      return true;
   }

   public boolean scheduleJob (ExecueJobData jobData, ExecueJobSchedule jobSchedule, String triggerName)
            throws SchedulerException {

      // Get the JobDetail object for the jobname:jobgroup
      // If doesn't exist, create a new one
      JobDetail jobDetail = getJobDetailController(jobData);
      jobSchedulerUtils.scheduleJob(jobData, jobDetail, jobSchedule, triggerName);

      return true;
   }

   /**
    * OnDemand jobs can be fired in parallel (stateless)
    * 
    * @param jobName
    * @param jobGroup
    * @return
    * @throws SchedulerException
    */
   public boolean fireOnDemandJob (String jobName, String jobGroup) throws SchedulerException {

      /*
       * List currentlyExecJobs = new ArrayList();; JobExecutionContext currentJob = null; Iterator currentExecJobsIter =
       * null; String currentExecJobName = null; String currentExecJobGroup = null; currentlyExecJobs =
       * JobSchedulerUtils.getCurrentlyExecutingJobs(); currentExecJobsIter = currentlyExecJobs.iterator();
       * while(currentExecJobsIter.hasNext()) { currentJob = (JobExecutionContext)currentExecJobsIter.next();
       * currentExecJobName = currentJob.getJobDetail().getName(); currentExecJobGroup =
       * currentJob.getJobDetail().getGroup(); if((currentExecJobName.equals(jobName)) &&
       * (currentExecJobGroup.equals(jobGroup))) { throw new SchedulerException("OnDemandJob is already running !!!"); } }
       */
      return jobSchedulerUtils.fireOnDemandJob(jobName, jobGroup);
   }

   public boolean removeJobTrigger (String triggerName) throws SchedulerException {
      return jobSchedulerUtils.removeJobTrigger(triggerName);

   }

   public void removeAllJobs () throws SchedulerException {
      jobSchedulerUtils.removeAllJobsInScheduler();

   }

   public void removeAllTriggers () throws SchedulerException {
      jobSchedulerUtils.removeAllJobTriggersInScheduler();

   }

   public void removeAllTriggersOfJob (String jobName, String jobGroup) throws SchedulerException {
      jobSchedulerUtils.removeAllTriggersOfJob(jobName, jobGroup);

   }

   public void removeJob (String jobName, String jobGroup) throws SchedulerException {
      jobSchedulerUtils.removeJobInScheduler(jobName, jobGroup);

   }

   /**
    * returns a List of ExecueJobs in the system These are controller jobs[can be run on-demand; no schedule attached],
    * archived controller jobs and scheduled controller jobs ExecueJob is one of the following definitions - 1. The
    * trigger that has info of when to fire the task - SCHEDULED_JOB 2. The dormant jobs in the system to which triggers
    * are not yet associated - UNSCHEDULED_JOB 3. The volatile trigger that's running an on-demand job - ONDEMAND_JOB
    * 
    * @return
    * @throws SchedulerException
    */
   public List<ExecueJob> getAllExecueJobs () throws SchedulerException {
      // retrieve all the JobDetail controller instances stored in the system
      List<JobDetail> jobsList = jobSchedulerUtils.listAllJobsInScheduler();
      List<ExecueJob> exJobsList = new ArrayList<ExecueJob>(); // list of EXJOBs
      Iterator<JobDetail> jobListIter = jobsList.iterator();
      JobDetail jobDetail = null;

      // a job in the system is defined by its JobDetail [group:name:class:description]
      while (jobListIter.hasNext()) {
         jobDetail = (JobDetail) jobListIter.next();
         // prepare add the EXJOB to the list
         populateAndAddExJob(jobDetail, exJobsList);
      }
      return exJobsList;

   }

   public List<String> getJobGroups () throws SchedulerException {
      return jobSchedulerUtils.listAllJobGroupsInScheduler();
   }

   /**
    * returns a map [jobGroup, jobNamesInGroup]
    * 
    * @return
    * @throws SchedulerException
    */
   public Map<String, List<String>> getJobControllerMap () throws SchedulerException {
      // the above map contains both pending and archived jobs
      // we just need the controller jobs against each group in the map

      List<String> jobGroupList = getJobGroups();
      Iterator<String> listIter = jobGroupList.iterator();
      String jobGroupName = null;

      Map<String, List<String>> returnMap = new HashMap<String, List<String>>();

      while (listIter.hasNext()) {
         jobGroupName = (String) listIter.next();
         returnMap.put(jobGroupName, getAllJobControllersOfGroup(jobGroupName));
      }
      return returnMap;
   }

   /**
    * returns a map [jobGroup, jobNamesInGroup]
    * 
    * @return
    * @throws SchedulerException
    */
   public Map<String, List<String>> getJobMap () throws SchedulerException {
      return jobSchedulerUtils.getJobMap();
   }

   /**
    * returns a List of EXJOBs that are of supplied group the return list can be controller, scheduled, archived jobs
    * under different jobnames of the supplied group
    * 
    * @return
    * @throws SchedulerException
    */
   public List<ExecueJob> getAllJobsOfGroup (String jobGroup) throws SchedulerException {
      // get a list of controller, archived and scheduled jobs

      /*
       * ExecueJob is one of the following definitions - 1. The trigger that has info of when to fire the task -
       * SCHEDULED_JOB 2. The dormant jobs in the system to which triggers are not yet associated - UNSCHEDULED_JOB 3.
       * The volatile trigger that's running an on-demand job - ONDEMAND_JOB
       */

      List<ExecueJob> allJobsList = getAllExecueJobs();
      ExecueJob exJob = null;
      ExecueJobData jobData = null;

      List<ExecueJob> jobsOfSuppliedGroup = new ArrayList<ExecueJob>();
      Iterator<ExecueJob> allJobsListIter = allJobsList.iterator();

      while (allJobsListIter.hasNext()) {
         exJob = allJobsListIter.next();
         jobData = exJob.getJobData();

         if (jobData.getJobGroup().equals(jobGroup)) {
            jobsOfSuppliedGroup.add(exJob);
         }
      }
      return jobsOfSuppliedGroup;
   }

   /**
    * Get the status of the job based on the trigger state Get the list of job execution contexts and see the triggers
    * that are running them. If there is a match, then the trigger is in RUNNING state, otherwise PENDING state
    * 
    * @param trigger
    * @return
    * @throws SchedulerException
    */
   public int getTriggerState (Trigger trigger) throws SchedulerException {

      List<JobExecutionContext> currentlyExecJobs = new ArrayList<JobExecutionContext>();
      JobExecutionContext currentJob = null;
      Iterator<JobExecutionContext> currentExecJobsIter = null;
      Trigger currentTrigger = null;

      currentlyExecJobs = jobSchedulerUtils.getCurrentlyExecutingJobs();
      currentExecJobsIter = currentlyExecJobs.iterator();

      while (currentExecJobsIter.hasNext()) {
         currentJob = currentExecJobsIter.next();
         currentTrigger = currentJob.getTrigger();

         if (currentTrigger.getName().equals(trigger.getName())) {
            return ExecueTaskProps.TASK_RUNNING;
         }
      }
      return ExecueTaskProps.TASK_PENDING;
   }

   /**
    * to display the details of an existing job to the user the jobs may be have pending schedules or no scheduleds yet
    * ExecueJob is one of the following - 1. The trigger that has info of when to fire the task - SCHEDULED_JOB 2. The
    * dormant jobs in the system to which triggers are not yet associated - UNSCHEDULED_JOB 3. The volatile trigger
    * that's running an on-demand job - ONDEMAND_JOB populates ExJobs with ExecueJobData and ExecueJobSchedule
    * 
    * @param jobName
    * @param jobGroup
    * @return
    * @throws SchedulerException
    */
   private void populateAndAddExJob (JobDetail jobDetail, List<ExecueJob> exJobList) throws SchedulerException {

      String jobName = jobDetail.getName();
      String jobGroup = jobDetail.getGroup();

      // populate the EXJOB with ExecueJobData and ExecueJobSchedule

      ExecueJobData jobData = null;
      ExecueJobSchedule jobSchedule = null;
      Trigger[] jobTriggers = null;
      Trigger trigger = null;
      ExecueJob exJob = null;

      // scheduled JobDetails have triggers attached to them
      jobTriggers = jobSchedulerUtils.getTriggersOfJob(jobName, jobGroup);

      // the jobDetail has triggers associated; each trigger is equivalent to a scheduled job
      if ((jobTriggers != null) && (jobTriggers.length > 0)) {

         // this is the controller job that has triggers associated
         // the job data is stored in the respective trigger's jobDataMap [jobData, jobSchedule]

         for (int i = 0; i < jobTriggers.length; i++) {
            trigger = jobTriggers[i];

            // beware of volatile triggers that are used to trigger the on-demand jobs
            // they exist for a very brief period of job execution and don't have
            // jobData and jobSchedule attached to them

            if (!trigger.isVolatile()) {

               // get the jobdata from the trigger
               jobData = (ExecueJobData) trigger.getJobDataMap().get(ExecueTaskProps.EXJOB_DATA);
               jobData.setJobName(trigger.getJobName());
               jobData.setJobStatus(getTriggerState(trigger)); // this is an active job stored in the trigger

               // next schedule for the job
               // trigger.getp
               jobData.setNextExecTime(trigger.getNextFireTime());
               jobData.setPreviousExecTime(trigger.getPreviousFireTime());
               jobSchedule = (ExecueJobSchedule) trigger.getJobDataMap().get(ExecueTaskProps.EXJOB_SCHEDULE);

               exJob = new ExecueJob();
               // this is done to make the triggername accessible on the UI for deletion of pending jobs
               // the job_type string will be parsed according to the format shown below

               if (logger.isDebugEnabled()) {
                  logger.debug("Inside populateAndAddExJob :: Retrieving trigger name " + trigger.getName());
               }
               exJob.setTriggerName(trigger.getName());
               exJob.setJobType(ExecueTaskProps.SCHEDULED_JOB);
               exJob
                        .setExecutionStatus(jobSchedulerUtils.isJobRunning(jobData.getJobName(), jobData.getJobGroup()) ? ExecueTaskProps.TASK_RUNNING
                                 : ExecueTaskProps.TASK_PENDING);

               exJob.setJobData(jobData);
               exJob.setJobSchedule(jobSchedule);
               exJobList.add(exJob);
            }// this is the case where we have the unscheduled controller job
            // being fired by a volatile trigger, i.e., on-demand
            else {
               // the trigger is volatile, i.e., firing an on-demand job using
               // a dormant?(need not be) JobDetail in the system

               exJob = new ExecueJob();

               // get jobdata from the jobDetail controller instead of the trigger
               jobData = (ExecueJobData) jobDetail.getJobDataMap().get(ExecueTaskProps.EXJOB_DATA);
               exJob.setJobType(ExecueTaskProps.ONDEMAND_JOB);
               exJob
                        .setExecutionStatus(jobSchedulerUtils.isJobRunning(jobData.getJobName(), jobData.getJobGroup()) ? ExecueTaskProps.TASK_RUNNING
                                 : ExecueTaskProps.TASK_PENDING);
               exJob.setJobData(jobData);
               exJobList.add(exJob);
            }
         }
      } else {
         // what if the controller job has no triggers left?
         // This can happen when the controller job is added newly to the system or
         // when the controller job's triggers are deleted by the user,
         // i.e., no schedules existing for a job
         exJob = new ExecueJob();

         // get jobdata from the jobDetail controller instead of the trigger
         jobData = (ExecueJobData) jobDetail.getJobDataMap().get(ExecueTaskProps.EXJOB_DATA);
         exJob.setJobType(ExecueTaskProps.UNSCHEDULED_JOB);
         exJob.setJobData(jobData);
         // setting job execution status doesn't make sense here as this job doesn't have a trigger
         exJob.setJobSchedule(new ExecueJobSchedule()); // set an empty schedule
         exJobList.add(exJob);
      }
   }

   /**
    * This method retrieves the ExecueJobData object from the JobDataMap of the associated JobDetail for the jobname,
    * jobGroup passed in
    * 
    * @param jobData
    * @param jobDataMap
    * @throws SchedulerException
    */
   private ExecueJobData retrieveExJobData (String jobName, String jobGroup) throws SchedulerException {
      JobDataMap jobDetailJobDataMap = jobSchedulerUtils.getJobDetailForJob(jobName, jobGroup).getJobDataMap();
      return (ExecueJobData) jobDetailJobDataMap.get(ExecueTaskProps.EXJOB_DATA);
   }

   /**
    * Takes a String array and concats the values into one long string delimited by the input delimiter.
    * 
    * @param array
    *           Array of String values to be concatted
    * @param delim
    *           delimiter to separate the String values.
    * @return Long String containing all the values delimited by the input delimiter.
    */
   public String toConcatString (String[] array, String delim) {
      if (array.length <= 0) {
         return null;
      }
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < array.length; ++i) {
         if (i > 0) {
            sb.append(delim);
         }
         sb.append(array[i]);
      }
      return sb.toString();
   }

   /**
    * Returns the readable completed message for a task that completed successfully.
    * 
    * @param startDate
    *           Date that the task first started running.
    * @return completion message with the date started and the date ended.
    */
   public String getCompletionMessage (Date startDate) {
      return "Started: " + startDate + " - Ended: " + new Date();
   }

   // this method checks if a controller JobDetail is available for the supplied name:group
   // otherwise, it will create one, but doesn't add it to the scheduler
   public JobDetail getJobDetailController (ExecueJobData jobData) throws SchedulerException {
      // get all the current jobs in DB to see if there is a job detail already present for the jobName:jobGroup

      String jobName = jobData.getJobName();
      String jobGroup = jobData.getJobGroup();

      // Check if a JobDetail exists for the provided name:group combination
      JobDetail jobDetail = jobSchedulerUtils.getJobDetailForJob(jobName, jobGroup);

      // note that the new jobDetail created below is not added to the scheduler by this function
      if (jobDetail == null) {
         // cannot find a JobDetail; Create a new one and return it back to the user
         jobDetail = new JobDetail(jobName, jobGroup, jobData.getJobClass());

         // multiple triggers can be attached to this controller job;
         // there will not be a specific schedule associated right now;

         jobDetail.setDurability(true);
         jobDetail.setVolatility(false);
         jobDetail.setDescription(jobData.getJobDescription());

         // multiple triggers can be attached to this controller job; hence there will not be a specific schedule
         // associated
         // this controller job on execution spawns new jobs
         // the job datamap will be written only once and cannot be persisted across multiple executions of the job
         // (Stateless)
         Map<String, Object> jobDataMap = new HashMap<String, Object>();
         jobData.setJobController(jobData.getJobName());
         jobDataMap.put(ExecueTaskProps.EXJOB_DATA, jobData);

         // new jobDetail; Set the datamaps
         jobDetail.setJobDataMap(new JobDataMap(jobDataMap));
      }
      // don't touch the datamaps of an existing jobDetail
      // this has already been added to the Scheduler
      return jobDetail;
   }

   public JobDetail getJobDetailForJob (String jobName, String jobGroup) throws SchedulerException {
      return jobSchedulerUtils.getJobDetailForJob(jobName, jobGroup);
   }

   public List<String> getAllJobControllersOfGroup (String jobGroup) throws SchedulerException {
      List<String> exJobNames = new ArrayList<String>();
      /*
       * the below call returns a list of jobs that may be active[associated to a trigger] or archived [already
       * executed] returns a list of controller, scheduled and archived jobs
       */
      List<ExecueJob> allJobsList = getAllJobsOfGroup(jobGroup);
      Iterator<ExecueJob> iter = allJobsList.iterator();
      ExecueJob exJob = null;

      while (iter.hasNext()) {
         exJob = iter.next();
         // we can get multiple active jobs for the same controller
         // as we can have jobs associated to multiple triggers
         if (!isJobAlreadyInList(exJobNames, exJob.getJobData().getJobController())) {
            exJobNames.add(exJob.getJobData().getJobController());
         }
      }
      return exJobNames;
   }

   private boolean isJobAlreadyInList (List<String> exJobNameList, String exJobName) {
      Iterator<String> listIter = exJobNameList.iterator();
      String jobNameInList = null;

      while (listIter.hasNext()) {
         jobNameInList = listIter.next();
         if (exJobName.equals(jobNameInList))
            return true;
      }
      return false;
   }

   /**
    * returns a list of ExJobs of the given name, group all jobs of [ListSiteUsers, users] for example there can be
    * scheduled or unscheduled ListSiteUsers jobs in the system this routine filters the unscheduled job though
    * [ListSiteLogins, users] is another set of jobs in the same users group
    * 
    * @param jobGroup
    * @param jobName
    * @return
    * @throws SchedulerException
    */
   public List<ExecueJob> getAllJobsOfGroupAndName (String jobGroup, String jobName) throws SchedulerException {
      List<ExecueJob> exJobsList = new ArrayList<ExecueJob>();
      List<ExecueJob> allJobsList = getAllJobsOfGroup(jobGroup);
      Iterator<ExecueJob> iter = allJobsList.iterator();
      ExecueJob exJob = null;

      while (iter.hasNext()) {
         exJob = iter.next();
         // filter the unscheduled job; get the scheduled and on-demand jobs
         if ((exJob.getJobData().getJobName().equals(jobName))
                  && (!exJob.getJobType().equals(ExecueTaskProps.UNSCHEDULED_JOB))) {
            exJobsList.add(exJob);
         }
      }
      return exJobsList;
   }

   public String getJobDescription (String jobName, String jobGroup) throws SchedulerException {
      return jobSchedulerUtils.getJobDetailForJob(jobName, jobGroup).getDescription();
   }

   public JobSchedulerUtils getJobSchedulerUtils () {
      return jobSchedulerUtils;
   }

   public void setJobSchedulerUtils (JobSchedulerUtils jobSchedulerUtils) {
      this.jobSchedulerUtils = jobSchedulerUtils;
   }
}
