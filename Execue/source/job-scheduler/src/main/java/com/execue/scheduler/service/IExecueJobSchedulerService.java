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


package com.execue.scheduler.service;

import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;

import com.execue.scheduler.ExecueJob;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.jobdata.ExecueJobSchedule;

public interface IExecueJobSchedulerService {

   /**
    * Returns a list of all Scheduled Jobs ExecueJob is one of the following - 1. The trigger that has info of when to
    * fire the task - SCHEDULED_JOB 2. The dormant jobs in the system to which triggers are not yet associated -
    * UNSCHEDULED_JOB 3. The volatile trigger that's running an on-demand job - ONDEMAND_JOB
    * 
    * @return
    * @throws SchedulerException
    */

   /*
    * Getters
    */
   public List<ExecueJob> getAllJobsInScheduler ();

   public List<String> getJobGroups ();

   public Map<String, List<String>> getJobControllerMap ();

   public List<ExecueJob> getAllJobsOfGroupAndName (String jobGroup, String jobName);

   public List<String> getAllJobControllersOfGroup (String jobGroup);

   public String getJobDescription (String jobName, String jobGroup);

   /**
    * Sets up a job that is durable and non-volatile
    * 
    * @return
    * @throws ExecueJobSchedulerException
    * @throws SchedulerException
    */
   public void addJobWithoutTrigger (ExecueJobData jobData) throws ExecueJobSchedulerException;

   /**
    * triggers the on-demand job with a volatile trigger This approach is not currently being used as we will not be
    * able to supply any information to a job Trigger forms the conduit for feeding data to the job execution
    * 
    * @return
    * @throws BusinessLogicException
    * @throws SchedulerException
    */
   public void fireOnDemandJob (String jobName, String jobGroup) throws ExecueJobSchedulerException;

   /**
    * If the job(JobDetail) already exists in the system, the trigger(defined by name, jobSchedule) will be attached to
    * it If not, a new job(JobDetail) will be added and the trigger will be attached to it The client prepares the
    * jobdata and jobschedule before making this call
    * 
    * @param jobData
    * @param jobSchedule
    * @return
    * @throws SchedulerException
    */
   public void scheduleJob (ExecueJobData jobData, ExecueJobSchedule jobSchedule, String triggerName)
            throws ExecueJobSchedulerException;

   /*
    * Mutators
    */
   /**
    * removes the trigger if its not currently in the process of firing the job
    * 
    * @param jobName
    * @param jobGroup
    * @return @
    */
   public void removeJobTrigger (String triggerName) throws ExecueJobSchedulerException;

   /**
    * deletes all the jobs (JobDetails) from the scheduler Facility is also available to pause all thejobs
    * 
    * @throws SchedulerException
    */
   public void removeAllJobs () throws ExecueJobSchedulerException;

   /**
    * deletes all the job triggers from the scheduler
    * 
    * @throws SchedulerException
    */
   public void removeAllTriggers () throws ExecueJobSchedulerException;

   /**
    * deletes all the given job's(JobDetail's) triggers from the scheduler
    * 
    * @throws SchedulerException
    */
   public void removeAllTriggersOfJob (String jobName, String jobGroup) throws ExecueJobSchedulerException;

   /**
    * deletes the job (JobDetail) from the scheduler
    * 
    * @throws SchedulerException
    */
   public void removeJob (String jobName, String jobGroup) throws ExecueJobSchedulerException;

   public void startScheduler () throws ExecueJobSchedulerException;

   public void stopScheduler () throws ExecueJobSchedulerException;

}