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


package com.execue.scheduler.configuration.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.type.JobType;
import com.execue.core.configuration.IConfigurable;
import com.execue.core.exception.ConfigurationException;
import com.execue.scheduler.ExecueJob;
import com.execue.scheduler.bean.SeedJobCreationDetail;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.jobdata.ExecueJobSchedule;
import com.execue.scheduler.props.ExecueTaskProps;
import com.execue.scheduler.service.IExecueJobSchedulerService;
import com.thoughtworks.xstream.XStream;

public class JobSchedulerConfigurableService implements IConfigurable {

   private Logger                               logger = Logger.getLogger(JobSchedulerConfigurationServiceImpl.class);
   private JobSchedulerConfigurationServiceImpl jobSchedulerConfigurationService;
   private IExecueJobSchedulerService           execueJobSchedulerService;
   private String                               seedFile;

   public JobSchedulerConfigurableService (String seedFile) {
      this.seedFile = seedFile;
   }

   @Override
   public void doConfigure () throws ConfigurationException {
      absorbSeedJobs();
   }

   @Override
   public void reConfigure () throws ConfigurationException {
      throw new UnsupportedOperationException("Reconfiguration not provided on the JobScheduler");

   }

   private void absorbSeedJobs () {
      boolean operationSucceeded = true;
      try {
         String seedJobXMLFile = readFileAsString(seedFile);
         XStream xstream = new XStream();
         xstream.alias("job-scheduler", com.execue.scheduler.bean.SeedJobCreationDetail.class);
         xstream.alias("execueJob", com.execue.scheduler.ExecueJob.class);

         SeedJobCreationDetail jobCreationDetail = null;
         ExecueJobData jobData = null;
         ExecueJobSchedule jobSchedule = null;
         String scheduleName = null;
         String jobScheduleCombo = "";
         ArrayList<String> scheduleNames = null;

         jobCreationDetail = (SeedJobCreationDetail) xstream.fromXML(seedJobXMLFile);

         if (jobCreationDetail != null) {
            for (ExecueJob execueJob : jobCreationDetail.getExecueJobs()) {
               scheduleNames = new ArrayList<String>();
               scheduleName = null;
               jobScheduleCombo = identifyJobScheduleCombination(execueJob, scheduleNames);

               if (logger.isDebugEnabled()) {
                  logger.debug(execueJob.getJobData().getJobName() + "," + jobScheduleCombo + "," + scheduleNames);
               }

               if (jobScheduleCombo.equals(ExecueTaskProps.ADD_NONE)) {
                  // simply add the new job
                  try {
                     jobData = execueJob.getJobData();
                     getExecueJobSchedulerService().addJobWithoutTrigger(jobData);
                  } catch (ExecueJobSchedulerException e) {
                     operationSucceeded = false;
                     logger.error("Failed to add job " + jobData.getJobName());
                  }

               }

               else if (jobScheduleCombo.equals(ExecueTaskProps.ADD_ADD)
                        || jobScheduleCombo.equals(ExecueTaskProps.RETAIN_ADD)) {
                  // add the new job along with the schedule
                  try {
                     jobData = execueJob.getJobData();
                     jobSchedule = execueJob.getJobSchedule();

                     // check to see if a schedule name is provided
                     if (scheduleNames.size() == 1) {
                        scheduleName = scheduleNames.get(0);
                     }
                     if (logger.isDebugEnabled()) {
                        logger.debug("Scheduling job " + jobData.getJobName() + "with trigger " + scheduleName);
                     }
                     getExecueJobSchedulerService().scheduleJob(jobData, jobSchedule, scheduleName);
                  } catch (ExecueJobSchedulerException e) {
                     operationSucceeded = false;
                     logger.error("Failed to add job " + jobData.getJobName());
                  }

               }

               else if (jobScheduleCombo.equals(ExecueTaskProps.RETAIN_DELETEALL)) {

                  // remove all schedules for the job
                  try {
                     jobData = execueJob.getJobData();

                     if (logger.isDebugEnabled())
                        logger.debug("Deleting all schedules of job " + jobData.getJobName());

                     getExecueJobSchedulerService().removeAllTriggersOfJob(jobData.getJobName(), jobData.getJobGroup());

                  } catch (ExecueJobSchedulerException e) {
                     operationSucceeded = false;
                     logger.error("Failed to remove all schedules for job " + jobData.getJobName());
                  }

               }

               else if (jobScheduleCombo.equals(ExecueTaskProps.RETAIN_DELETE)) {

                  // remove all schedules for the job
                  try {
                     jobData = execueJob.getJobData();

                     for (String name : scheduleNames) {
                        if (logger.isDebugEnabled())
                           logger.debug("Deleting trigger " + name);

                        getExecueJobSchedulerService().removeJobTrigger(name);
                     }

                  } catch (ExecueJobSchedulerException e) {
                     operationSucceeded = false;
                     logger.error("Failed to remove schedules for job " + jobData.getJobName());
                  }

               } else if (jobScheduleCombo.equals(ExecueTaskProps.DELETE_NONE)) {
                  // remove all schedules for the job
                  try {
                     jobData = execueJob.getJobData();

                     getExecueJobSchedulerService().removeJob(jobData.getJobName(), jobData.getJobGroup());

                  } catch (ExecueJobSchedulerException e) {
                     operationSucceeded = false;
                     logger.error("Failed to remove for job " + jobData.getJobName());
                  }

               }

            }
            //load JobDataByJobTypeMap 
            getJobSchedulerConfigurationService().loadJobDataByJobTypeMap(
                     populateJobDataByJobTypeMap(jobCreationDetail.getExecueJobs()));
         }

      } catch (IOException e) {
         operationSucceeded = false;
      }

      listSchedulerJobsInfo();
   }

   private Map<JobType, ExecueJobData> populateJobDataByJobTypeMap (List<ExecueJob> execueJobs) {
      Map<JobType, ExecueJobData> jobDataByJobTypeMap = new HashMap<JobType, ExecueJobData>();
      for (ExecueJob execueJob : execueJobs) {
         jobDataByJobTypeMap.put(execueJob.getJobData().getJobType(), execueJob.getJobData());
      }
      return jobDataByJobTypeMap;
   }

   private void listSchedulerJobsInfo () {
      // list all the jobs added

      String jobSchedule = "";
      String jobExecutionStatus = "";

      if (logger.isDebugEnabled()) {
         logger
                  .debug("JobName, JobGroup, JobExecutionClass, JobType, JobStatus, Periodicity, PreviousExecution, NextExecution, ScheduleName");

         for (ExecueJob job : getExecueJobSchedulerService().getAllJobsInScheduler()) {

            jobSchedule = null;
            jobExecutionStatus = null;

            switch (job.getJobSchedule().getPeriodicity()) {
               case ExecueTaskProps.MONTHLY_SCHEDULE:
                  jobSchedule = "MONTHLY";
                  break;
               case ExecueTaskProps.WEEKLY_SCHEDULE:
                  jobSchedule = "WEEKLY";
                  break;
               case ExecueTaskProps.DAILY_SCHEDULE:
                  jobSchedule = "DAILY";
                  break;
               case ExecueTaskProps.HOURLY_SCHEDULE:
                  jobSchedule = "HOURLY";
                  break;
               default:
            }

            switch (job.getExecutionStatus()) {
               case ExecueTaskProps.TASK_RUNNING:
                  jobExecutionStatus = "RUNNING";
                  break;
               case ExecueTaskProps.TASK_PENDING:
                  jobExecutionStatus = "PENDING";
                  break;

               default:
            }

            // JobName JobGroup JobExecutionClass JobType (Scheduled/Unscheduled) JobStatus(Running/Pending)
            // Periodicity (Monthly/Weekly/Daily/Hourly), PreviousExecutionTime, NextExecutionTime

            if (logger.isDebugEnabled()) {
               logger.debug(job.getJobData().getJobName() + "," + job.getJobData().getJobGroup() + ","
                        + job.getJobData().getJobClass().getName() + "," + job.getJobType() + "," + jobExecutionStatus
                        + "," + jobSchedule + "," + job.getJobData().getPreviousExecTime() + ","
                        + job.getJobData().getNextExecTime() + "," + job.getTriggerName());
            }
         }
      }
   }

   // returns the combination type and related list of schedule names, if any
   private String identifyJobScheduleCombination (ExecueJob execueJob, ArrayList<String> scheduleNames) {

      // /** Possible combinations of jobs schedules */

      // ** JOB SCHEDULE */
      // **----------------------*/
      // 1* ADD ADD:ScheduleName --- Add a new job and a new schedule(optional) */
      // 2* RETAIN ADD:ScheduleName --- Retain existing job, but add a brand new schedule(optional) */
      // 3* RETAIN DELETEALL --- Retain the job, but delete all the related schedules */
      // 4* RETAIN DELETE:ScheduleName1, ScheduleName2,... --- Retain the job, but delete all the named schedules */
      // 5* DELETE --- Delete the job, it will delete all the related schedules */

      ExecueJobData jobData = null;
      ExecueJobSchedule jobSchedule = null;
      String seedOperation = "";
      String combination = "";

      jobData = execueJob.getJobData();

      if (jobData != null) {

         if ("ADD".equals(execueJob.getJobData().getSeedOperation())
                  || "RETAIN".equals(execueJob.getJobData().getSeedOperation())) {
            // see if there is a schedule
            jobSchedule = execueJob.getJobSchedule();
            if (jobSchedule != null) {
               seedOperation = execueJob.getJobSchedule().getSeedOperation();
               if (seedOperation.contains("ADD")) {

                  if ("ADD".equals(execueJob.getJobData().getSeedOperation()))
                     combination = ExecueTaskProps.ADD_ADD;
                  else
                     combination = ExecueTaskProps.RETAIN_ADD;

                  // case 1 [ADD, ADD]
                  // check if a schedule name is provided

                  String[] tokens = seedOperation.split(ExecueTaskProps.OPERATION_SCHEDULE_SEPARATOR);
                  if (tokens.length > 1) {
                     scheduleNames.add(tokens[1]);
                  }

               } else if (seedOperation.equals("DELETEALL")) {
                  if ("RETAIN".equals(execueJob.getJobData().getSeedOperation()))
                     combination = ExecueTaskProps.RETAIN_DELETEALL;

               } else if (seedOperation.contains("DELETE")) {
                  String[] tokens = seedOperation.split(ExecueTaskProps.OPERATION_SCHEDULE_SEPARATOR);
                  // atleast 1 schedule name to be deleted is mandatory after DELETE keyword
                  if (tokens.length < 2) {
                     return ExecueTaskProps.BAD_COMBINATION;
                  } else {
                     String[] deleteScheduleNames = tokens[1].split(ExecueTaskProps.SCHEDULE_NAME_SEPARATOR);
                     for (int i = 0; i < deleteScheduleNames.length; i++) {
                        scheduleNames.add(deleteScheduleNames[i]);
                     }
                     if ("RETAIN".equals(execueJob.getJobData().getSeedOperation()))
                        combination = ExecueTaskProps.RETAIN_DELETE;
                  }

               }
            } else {
               if ("ADD".equals(execueJob.getJobData().getSeedOperation()))
                  combination = ExecueTaskProps.ADD_NONE;
            }
         } else if ("DELETE".equals(execueJob.getJobData().getSeedOperation())) {
            combination = ExecueTaskProps.DELETE_NONE;
         }
      }

      return combination;

   }

   private String readFileAsString (String filePath) throws java.io.IOException {

      InputStreamReader inputStreamReader = new InputStreamReader(getClass().getResourceAsStream(filePath));
      StringBuffer fileData = new StringBuffer(1000);

      // BufferedReader reader = new BufferedReader(new FileReader(filePath));
      BufferedReader reader = new BufferedReader(inputStreamReader);

      char[] buf = new char[1024];
      int numRead = 0;
      while ((numRead = reader.read(buf)) != -1) {
         String readData = String.valueOf(buf, 0, numRead);
         fileData.append(readData);
         buf = new char[1024];
      }
      reader.close();

      return fileData.toString();
   }

   /**
    * @return the jobSchedulerConfigurationService
    */
   public JobSchedulerConfigurationServiceImpl getJobSchedulerConfigurationService () {
      return jobSchedulerConfigurationService;
   }

   /**
    * @param jobSchedulerConfigurationService the jobSchedulerConfigurationService to set
    */
   public void setJobSchedulerConfigurationService (
            JobSchedulerConfigurationServiceImpl jobSchedulerConfigurationService) {
      this.jobSchedulerConfigurationService = jobSchedulerConfigurationService;
   }

   /**
    * @return the execueJobSchedulerService
    */
   public IExecueJobSchedulerService getExecueJobSchedulerService () {
      return execueJobSchedulerService;
   }

   /**
    * @param execueJobSchedulerService the execueJobSchedulerService to set
    */
   public void setExecueJobSchedulerService (IExecueJobSchedulerService execueJobSchedulerService) {
      this.execueJobSchedulerService = execueJobSchedulerService;
   }

}
