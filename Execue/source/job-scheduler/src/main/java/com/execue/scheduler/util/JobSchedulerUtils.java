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


package com.execue.scheduler.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

import com.execue.core.util.ExecueStringUtil;
import com.execue.scheduler.ExecueJob;
import com.execue.scheduler.ExecueJobScheduler;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.jobdata.ExecueJobSchedule;
import com.execue.scheduler.props.ExecueTaskProps;

public class JobSchedulerUtils {

   private Logger logger = Logger.getLogger(JobSchedulerUtils.class);

   /*
    * Based on the supplied schedule - Schedules a monthly-based job with the day of month, hour, minute, etc. Schedules
    * a weekly-based job with dayOfWeek, hour, minute Schedules a daily-based job with hour, minute, etc. Schedules a
    * minutely-based job with no. of repeats, and interval in min b/w fires etc. The min job data required are jobname,
    * jobgroup, jobdescription and jobclass The min job schedule required are trigger periodicity (hourly, daily,
    * weekly, monthly) and related data [dayOfMonth, dayOfWeek, hourOfDay, min] startTime, endTime (can be indefinite)
    * Sets up the startTime, if provided and the endTime If the endTime is not provided, the job is set to be INDEFINITE
    */

   private Trigger prepareJobTrigger (ExecueJobData jobData, JobDetail jobDetail, ExecueJobSchedule jobSchedule,
            String triggerName) {
      Trigger trigger = null;

      // Ensure uniqueness of the triggername; this is taken care of by appending the jobname with system time in
      // millisec
      if (triggerName == null) {
         triggerName = jobDetail.getName() + ":" + jobDetail.getGroup();
         // Max limit for trigger name is set as 80, hence truncating it to 55 as keeping rest of 25 chars for TRIGGER_NAME_EXTN and 
         // System.currentTimeMillis()
         triggerName = ExecueStringUtil.getNormalizedName(triggerName, 55);
         triggerName = triggerName + ExecueTaskProps.TRIGGER_NAME_EXTN + "_" + System.currentTimeMillis();
      }

      // All these triggers are created in the DEFAULT group
      if (jobSchedule.getPeriodicity() == ExecueTaskProps.MONTHLY_SCHEDULE) {
         trigger = prepareMonthlyTrigger(triggerName, jobSchedule);

      } else if (jobSchedule.getPeriodicity() == ExecueTaskProps.WEEKLY_SCHEDULE) {
         trigger = prepareWeeklyTrigger(triggerName, jobSchedule);

      } else if (jobSchedule.getPeriodicity() == ExecueTaskProps.DAILY_SCHEDULE) {
         trigger = prepareDailyTrigger(triggerName, jobSchedule);

      } else if (jobSchedule.getPeriodicity() == ExecueTaskProps.HOURLY_SCHEDULE) {
         trigger = prepareHourlyTrigger(triggerName, jobSchedule);

      } else if (jobSchedule.getPeriodicity() == ExecueTaskProps.MINUTELY_SCHEDULE) {
         trigger = prepareMinutelyTrigger(triggerName, jobSchedule);

      }

      if (logger.isDebugEnabled()) {
         logger.debug("Preparing job trigger -- Trigger name ::" + trigger.getName());
      }

      if (jobSchedule.getStartTime() != null) {
         trigger.setStartTime(jobSchedule.getStartTime());
      }
      // set the endTime only when the trigger is not on indefinite schedule 
      if (!jobSchedule.isIndefinite() && jobSchedule.getEndTime() != null) {
         trigger.setEndTime(jobSchedule.getEndTime());
      }

      // fill in the jobDetail jobDataMap now
      // get the jobData from the jobDataMap of controller's jobDetail

      Map<String, Object> triggerjobProps = new HashMap<String, Object>();
      // triggerjobProps.put(ExecueTaskProps.EXJOB_DATA, jobData2);

      // every job should have knowledge of its JobDetail controller
      // trigger's jobData can be different from the JobDetail controller's jobData
      jobData.setJobController(jobDetail.getName());
      jobData.setJobDescription(jobDetail.getDescription());
      triggerjobProps.put(ExecueTaskProps.EXJOB_DATA, jobData);
      triggerjobProps.put(ExecueTaskProps.EXJOB_SCHEDULE, jobSchedule);

      // adding the trigger to the JobDetail Controller
      trigger.setJobName(jobDetail.getName());
      trigger.setJobGroup(jobDetail.getGroup());
      trigger.setJobDataMap(new JobDataMap(triggerjobProps));

      return trigger;

   }

   /**
    * A task has a trigger to invoke, and a job to execute The SchedulerTask has the trigger properties JobClass has the
    * task execution logic Trigger [name, group] are needed for creating a new trigger for this task TriggerName may be
    * supplied The jobDetail and Trigger have to be created internally by taking required info from the client
    * 
    * @param task
    * @return
    * @throws SchedulerException
    */
   public boolean scheduleJob (ExecueJobData jobData, JobDetail jobDetail, ExecueJobSchedule jobSchedule,
            String triggerName) throws SchedulerException {

      boolean ret_type = false;
      Scheduler scheduler = ExecueJobScheduler.getSchedulerInstance();
      // prepare a unique trigger[jobname:jobgroup_trigger_systemTimeInMilliSec] with the available schedule
      // if the triggerName is not supplied
      Trigger trigger = prepareJobTrigger(jobData, jobDetail, jobSchedule, triggerName);

      // we check to see if the jobDetail is already added to the scheduler
      // a) the jobDetail is existing in the scheduler, but may or maynot have any associated triggers
      // b) the jobDetail is not yet added to the scheduler, hence add the jobDetail along with the trigger
      Trigger[] jobTriggers = null;
      jobTriggers = getTriggersOfJob(jobDetail.getName(), jobDetail.getGroup());

      // condition a
      // see if the jobDetail is already registered with the scheduler, i.e., with/without triggers
      if (jobTriggers != null && jobTriggers.length > 0
               || isJobAlreadyExisting(jobDetail.getName(), jobDetail.getGroup())) {
         // Schedule the given Trigger with the Job identified by the Trigger's settings.
         scheduler.scheduleJob(trigger);

         // condition b
      } else // make sure that a new jobDetail is returned by execueJobUtils.getJobDetailController()
      // this means that the JobDetail object is created, but not yet added to the scheduler
      {
         // Add the given JobDetail to the Scheduler, and associate the given Trigger with it
         scheduler.scheduleJob(jobDetail, trigger);
      }

      jobTriggers = null;
      ret_type = true;
      return ret_type;
   }

   /*
    * A triggerName has to be unique in the system as the DEFAULT_GROUP is used
    */
   private boolean isTriggerAlreadyExisting (String triggerName) throws SchedulerException {
      boolean existing = false;

      Scheduler scheduler = ExecueJobScheduler.getSchedulerInstance();
      String[] triggerNames = scheduler.getTriggerNames(Scheduler.DEFAULT_GROUP);

      if (triggerNames.length > 0) {

         for (int i = 0; i < triggerNames.length; i++) {
            // this is a very inefficient way of checking in a loop
            // need to change this method to take in an array of triggernames instead of just one
            if (triggerName.equals(triggerNames[i])) {
               return true;
            }
         }
      }

      return existing;
   }

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
   public boolean registerJobDetailWithoutTrigger (JobDetail jobDetail) throws SchedulerException {
      boolean ret_type = false;

      // if the JobDetail is newly created in the above call, then the following call succeeds
      // Otherwise, the following call would fail as a JobDetail with the given name:group is already registered
      ExecueJobScheduler.getSchedulerInstance().addJob(jobDetail, false);

      ret_type = true;
      return ret_type;
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
      ExecueJobScheduler.getSchedulerInstance().triggerJobWithVolatileTrigger(jobName, jobGroup);

      return true;
   }

   /**
    * The job has to be an active one, i.e., the controller job We get the Trigger from each of the currently executing
    * jobs and compare against the trigger sent in Trigger name is only required; Sending in the entire object for
    * future extensibility
    * 
    * @param exJob
    * @return
    * @throws SchedulerException
    *            TODO - this method implementation is not complete
    */
   public boolean isTriggerActive (String triggerName) throws SchedulerException {
      boolean ret_type = false;

      List<JobExecutionContext> currentlyExecJobs = new ArrayList<JobExecutionContext>();
      JobExecutionContext currentJob = null;
      Iterator<JobExecutionContext> currentExecJobsIter = null;

      Trigger currentTrigger = null;

      currentlyExecJobs = getCurrentlyExecutingJobs();
      currentExecJobsIter = currentlyExecJobs.iterator();

      while (currentExecJobsIter.hasNext()) {
         currentJob = currentExecJobsIter.next();
         currentTrigger = currentJob.getTrigger();

         if (triggerName.equals(currentTrigger.getName())) {
            return true;
         }
      }
      return ret_type;
   }

   /**
    * scheduler.deleteJob() deletes the jobDetail related to [jobName, jobGroup] scheduler.unscheduleJob(triggerName,
    * triggerGroup) removes the trigger from the scheduler
    * 
    * @param jobName
    * @param jobGroup
    * @return
    * @throws SchedulerException
    *            TODO - this method implementation is not complete
    */
   public boolean removeJobTrigger (String triggerName) throws SchedulerException {
      if (!isTriggerAlreadyExisting(triggerName)) {
         throw new SchedulerException("Trigger not present in the system -- " + triggerName);
      }

      Scheduler scheduler = ExecueJobScheduler.getSchedulerInstance();

      // the trigger in question shouldn't be running any job right now, i.e., inactive
      if (!isTriggerActive(triggerName)) {
         return scheduler.unscheduleJob(triggerName, Scheduler.DEFAULT_GROUP);
      } else {
         // this is an active trigger (isJobActive = true) that's executing the job
         throw new SchedulerException("Unable to delete a trigger during job execution");
      }
   }

   /**
    * Remove (delete) the Trigger with the given name, and store the new given one - which must be associated with the
    * same job (the new trigger must have the job name & group specified) - however, the new trigger need not have the
    * same name as the old trigger.
    * 
    * @param task
    * @return
    * @throws SchedulerException
    *            TODO - need to test this method; General advice is not to use this Its better to delete a trigger and
    *            create a new one
    */
   public boolean rescheduleJob (ExecueJob exJob, Trigger newTrigger) throws SchedulerException {
      boolean ret_type = false;

      Scheduler scheduler = null;

      try {
         scheduler = ExecueJobScheduler.getSchedulerInstance();
      } catch (SchedulerException e2) {
         // TODO Auto-generated catch block
         e2.getMessage();
         return ret_type;
      }

      // taskname and taskgroup are required
      // these will be transformed into trigger name and trigger group

      String jobName = exJob.getJobData().getJobName();
      String jobGroup = exJob.getJobData().getJobGroup();
      String oldTriggerName = exJob.getTriggerName();

      if (jobName == null || jobGroup == null || newTrigger == null) {
         return ret_type;
      }

      // this will hook the new trigger up with the appropriate jobDetail
      // jobDetailController in our design

      newTrigger.setJobName(jobName);
      newTrigger.setJobGroup(jobGroup);

      // TODO - make sure that jobData and jobSchedules are properly copied to the
      // new trigger's jobDataMap; jobSchedule changes, whereas jobData remains the same

      scheduler.rescheduleJob(oldTriggerName, Scheduler.DEFAULT_GROUP, newTrigger);

      ret_type = true;
      return ret_type;
   }

   /**
    * Return a list of JobExecutionContext objects that represent all currently executing Jobs in this Scheduler
    * instance. This method is not cluster aware. That is, it will only return Jobs currently executing in this
    * Scheduler instance, not across the entire cluster. Note that the list returned is an 'instantaneous' snap-shot,
    * and that as soon as it's returned, the true list of executing jobs may be different. Also please read the doc
    * associated with JobExecutionContext- especially if you're using RMI. Throws: SchedulerException See Also:
    * JobExecutionContext
    * 
    * @return
    * @throws SchedulerException
    */
   public List<JobExecutionContext> getCurrentlyExecutingJobs () throws SchedulerException {
      Scheduler scheduler = ExecueJobScheduler.getSchedulerInstance();

      return scheduler.getCurrentlyExecutingJobs();

   }

   /**
    * the jobNameGroup is formatted as jobName:jobGroup this routine checks for the uniqueness of the supplied jobName
    * 
    * @param jobDetail
    * @return
    * @throws SchedulerException
    */
   public boolean isJobAlreadyExisting (String jobName, String jobGroup) throws SchedulerException {
      boolean jobMatch = false;

      if (jobName == null || jobGroup == null) {
         return jobMatch;
      }

      List<JobDetail> jobList = listAllJobsInScheduler();
      JobDetail detail = null;
      Iterator<JobDetail> listIterator = jobList.iterator();

      while (listIterator.hasNext()) {
         detail = listIterator.next();
         if (jobName.equals(detail.getName()) && jobGroup.equals(detail.getGroup())) {
            return true;
         }
      }

      return jobMatch;
   }

   public void removeAllTriggersOfJob (String jobName, String jobGroup) throws SchedulerException {
      // first check if the job is registered in the scheduler
      if (!isJobAlreadyExisting(jobName, jobGroup)) {
         throw new SchedulerException("Job Not Registered -- " + jobGroup + ":" + jobName);
      }

      // get all the triggers of the given job
      Scheduler scheduler = ExecueJobScheduler.getSchedulerInstance();
      Trigger[] jobTriggers = scheduler.getTriggersOfJob(jobName, jobGroup);

      // donot remove active triggers
      List<String> activeTriggers = new ArrayList<String>();

      if (jobTriggers.length > 0) {

         for (int i = 0; i < jobTriggers.length; i++) {
            // this is a very inefficient way of checking in a loop
            // need to change this method to take in an array of triggernames instead of just one
            // this call is going to retrieve all the current job execution contexts everytime
            if (!isTriggerActive(jobTriggers[i].getName())) {
               scheduler.unscheduleJob(jobTriggers[i].getName(), Scheduler.DEFAULT_GROUP);
            } else {
               activeTriggers.add(jobTriggers[i].getName());
            }
         }

         // some of the triggers are currently executing; don't delete them
         // need to inform the user
         if (activeTriggers.size() > 0) {
            throw new SchedulerException("Active triggers Not Deleted -- " + activeTriggers.toString());
         }
      }

   }

   /**
    * Deletes all the job triggers from the scheduler only when they are not running
    * 
    * @throws SchedulerException
    */
   public void removeAllJobTriggersInScheduler () throws SchedulerException {
      // get a list of all jobs registered in the scheduler

      Scheduler scheduler = ExecueJobScheduler.getSchedulerInstance();
      String[] triggerNames = scheduler.getTriggerNames(Scheduler.DEFAULT_GROUP);

      List<String> activeTriggers = new ArrayList<String>();

      if (triggerNames.length > 0) {

         for (int i = 0; i < triggerNames.length; i++) {
            // this is a very inefficient way of checking in a loop
            // need to change this method to take in an array of triggernames instead of just one
            if (!isTriggerActive(triggerNames[i])) {
               scheduler.unscheduleJob(triggerNames[i], Scheduler.DEFAULT_GROUP);
            } else {
               activeTriggers.add(triggerNames[i]);
            }
         }

         // some of the triggers are currently executing; don't delete them
         // need to inform the user
         if (activeTriggers.size() > 0) {
            throw new SchedulerException("Active triggers Not Deleted -- " + activeTriggers.toString());
         }
      }
   }

   /**
    * Deletes all the controller JobDetails from the scheduler only when they are not running
    * 
    * @throws SchedulerException
    */
   public void removeAllJobsInScheduler () throws SchedulerException {
      // get a list of all jobs registered in the scheduler

      Scheduler scheduler = ExecueJobScheduler.getSchedulerInstance();
      List<JobDetail> jobsList = listAllJobsInScheduler();

      Iterator<JobDetail> jobListIter = jobsList.iterator();
      JobDetail jobDetail = null;

      // check to see that none of the registered jobs are presently executing
      List<String> runningJobs = new ArrayList<String>();

      while (jobListIter.hasNext()) {
         jobDetail = jobListIter.next();

         if (!isJobRunning(jobDetail.getName(), jobDetail.getGroup())) {
            scheduler.deleteJob(jobDetail.getName(), jobDetail.getGroup());
         } else {
            runningJobs.add(jobDetail.getGroup() + ":" + jobDetail.getName());
         }
      }

      // some of the jobs are running and hence their JobDetails are not deleted
      // need to inform the user
      if (runningJobs.size() > 0) {
         throw new SchedulerException("Running Jobs Not Deleted -- " + runningJobs.toString());
      }
   }

   public void removeJobInScheduler (String jobName, String jobGroup) throws SchedulerException {
      Scheduler scheduler = ExecueJobScheduler.getSchedulerInstance();
      // first check if the job is registered in the scheduler
      if (!isJobAlreadyExisting(jobName, jobGroup)) {
         throw new SchedulerException("Job Not Registered -- " + jobGroup + ":" + jobName);
      }

      // remove the job if its not presently running
      if (!isJobRunning(jobName, jobGroup)) {
         scheduler.deleteJob(jobName, jobGroup);
      } else {
         throw new SchedulerException("Running Job Not Deleted -- " + jobGroup + ":" + jobName);
      }
   }

   public boolean isJobRunning (String jobName, String jobGroup) throws SchedulerException {
      boolean jobExecuting = false;

      // get a list of currently executing jobs in the scheduler

      List<JobExecutionContext> currentlyExecJobs = new ArrayList<JobExecutionContext>();
      JobExecutionContext currentJob = null;
      currentlyExecJobs = getCurrentlyExecutingJobs();
      Iterator<JobExecutionContext> currentExecJobsIter = currentlyExecJobs.iterator();

      while (currentExecJobsIter.hasNext()) {
         currentJob = currentExecJobsIter.next();
         if (jobName.equals(currentJob.getJobDetail().getName())
                  && jobGroup.equals(currentJob.getJobDetail().getGroup())) {
            return true;
         }
      }

      return jobExecuting;
   }

   /**
    * @param triggerName
    * @param triggerProps
    * @return
    */
   private Trigger prepareMonthlyTrigger (String triggerName, ExecueJobSchedule jobSchedule) {
      Trigger trigger = null;

      int dayOfMonth = jobSchedule.getDayOfMonth();
      int hour = jobSchedule.getHourOfDay();
      int minute = jobSchedule.getMinuteOfHour();

      trigger = MonthlyTriggerUtils.makeMonthlyTrigger(triggerName, dayOfMonth, hour, minute);

      setTriggerIdentity(trigger, trigger.getName(), Scheduler.DEFAULT_GROUP);

      return trigger;
   }

   /**
    * @param triggerName
    * @param triggerProps
    * @return
    */
   private Trigger prepareWeeklyTrigger (String triggerName, ExecueJobSchedule jobSchedule) {
      Trigger trigger = null;

      int dayOfWeek = jobSchedule.getDayOfWeek();
      int hour = jobSchedule.getHourOfDay();
      int minute = jobSchedule.getMinuteOfHour();

      trigger = WeeklyTriggerUtils.makeWeeklyTrigger(triggerName, dayOfWeek, hour, minute);

      setTriggerIdentity(trigger, trigger.getName(), Scheduler.DEFAULT_GROUP);

      return trigger;
   }

   /**
    * @param triggerName
    * @param triggerProps
    * @return
    */
   private Trigger prepareDailyTrigger (String triggerName, ExecueJobSchedule jobSchedule) {
      Trigger trigger = null;

      int hour = jobSchedule.getHourOfDay();
      int minute = jobSchedule.getMinuteOfHour();

      trigger = DailyTriggerUtils.makeDailyTrigger(triggerName, hour, minute);

      setTriggerIdentity(trigger, trigger.getName(), Scheduler.DEFAULT_GROUP);

      return trigger;
   }

   /**
    * @param triggerName
    * @param triggerProps
    * @return
    */
   private Trigger prepareHourlyTrigger (String triggerName, ExecueJobSchedule jobSchedule) {
      Trigger trigger = null;

      int everyNthHour = jobSchedule.getEveryNthHour();

      trigger = HourlyTriggerUtils.makeHourlyTrigger(everyNthHour);

      setTriggerIdentity(trigger, triggerName, Scheduler.DEFAULT_GROUP);

      return trigger;
   }

   private Trigger prepareMinutelyTrigger (String triggerName, ExecueJobSchedule jobSchedule) {
      Trigger trigger = null;
      int noOfRepeats = jobSchedule.getNoOfRepeats();
      int intervalInMinutes = jobSchedule.getIntervalInMinutes();
      if (jobSchedule.isIndefinite()) {
         trigger = MinutelyTriggerUtils.makeMinutelyTrigger(intervalInMinutes);
      } else {
         trigger = MinutelyTriggerUtils.makeMinutelyTrigger(triggerName, intervalInMinutes, noOfRepeats);
      }
      setTriggerIdentity(trigger, triggerName, Scheduler.DEFAULT_GROUP);

      return trigger;
   }

   /**
    * Each job (JobDetail) will be qualified by its group, i.e., jobName:jobGroup This returns a list of Controller
    * jobDetails The controller jobs have jobData associated to their triggers
    * 
    * @return
    * @throws SchedulerException
    */
   public List<JobDetail> listAllJobsInScheduler () throws SchedulerException {
      Scheduler scheduler = ExecueJobScheduler.getSchedulerInstance();

      String[] jobGroups;
      String[] jobsInGroup;
      int i, j;

      List<JobDetail> jobsList = new ArrayList<JobDetail>();
      JobDetail jobDetail = null;
      jobGroups = scheduler.getJobGroupNames();

      for (i = 0; i < jobGroups.length; i++) {
         jobsInGroup = scheduler.getJobNames(jobGroups[i]);

         for (j = 0; j < jobsInGroup.length; j++) {
            jobDetail = scheduler.getJobDetail(jobsInGroup[j], jobGroups[i]);
            jobsList.add(jobDetail);
         }
      }

      return jobsList;
   }

   /**
    * @return
    * @throws SchedulerException
    */
   public Map<String, List<String>> getJobMap () throws SchedulerException {
      Map<String, List<String>> jobMap = new HashMap<String, List<String>>();
      String jobGroupName = null;
      List<String> jobGroupList = listAllJobGroupsInScheduler();
      Iterator<String> groupListIter = jobGroupList.iterator();

      while (groupListIter.hasNext()) {
         jobGroupName = groupListIter.next();
         jobMap.put(jobGroupName, listAllJobsInJobGroup(jobGroupName));
      }

      return jobMap;
   }

   /**
    * @param scheduler
    * @return
    * @throws SchedulerException
    */
   public List<String> listAllJobGroupsInScheduler () throws SchedulerException {
      Scheduler scheduler = ExecueJobScheduler.getSchedulerInstance();
      String[] jobGroups;
      int i;

      List<String> jobGroupsList = new ArrayList<String>();
      ;
      jobGroups = scheduler.getJobGroupNames();

      for (i = 0; i < jobGroups.length; i++) {
         jobGroupsList.add(jobGroups[i]);
      }
      return jobGroupsList;
   }

   /**
    * @param scheduler
    * @param jobGroupName
    * @return
    * @throws SchedulerException
    */
   public List<String> listAllJobsInJobGroup (String jobGroupName) throws SchedulerException {
      Scheduler scheduler = ExecueJobScheduler.getSchedulerInstance();
      String[] jobsInGroup;
      int j;

      List<String> jobsList = new ArrayList<String>();
      jobsInGroup = scheduler.getJobNames(jobGroupName);

      for (j = 0; j < jobsInGroup.length; j++) {
         jobsList.add(jobsInGroup[j]);
      }
      return jobsList;
   }

   /**
    * a single job can have multiple triggers; We have only one trigger for a BM job for now a single trigger is always
    * hooked to a unique job
    * 
    * @param scheduler
    * @param jobName
    * @param jobGroup
    * @return
    * @throws SchedulerException
    */
   public Trigger[] getTriggersOfJob (String jobName, String jobGroup) throws SchedulerException {
      Trigger[] jobTriggers;
      Scheduler scheduler = ExecueJobScheduler.getSchedulerInstance();
      jobTriggers = scheduler.getTriggersOfJob(jobName, jobGroup);

      return jobTriggers;
   }

   /**
    * returns the JobDetail for the supplied jobname, jobgroup
    * 
    * @param scheduler
    * @param jobName
    * @param jobGroup
    * @return
    * @throws SchedulerException
    */
   public JobDetail getJobDetailForJob (String jobName, String jobGroup) throws SchedulerException {
      Scheduler scheduler = ExecueJobScheduler.getSchedulerInstance();
      return scheduler.getJobDetail(jobName, jobGroup);
   }

   /**
    * Set the given Trigger's name to the given value, and its group to the given group. Parameters: trig - the tigger
    * to change name to name - the new trigger name group - the new trigger group
    * 
    * @param trig
    * @param name
    * @param group
    */
   public void setTriggerIdentity (Trigger trig, String name, String group) {
      TriggerUtils.setTriggerIdentity(trig, name, group);
   }

}
