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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.scheduler.bean.SeedJobCreationDetail;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.jobdata.ExecueJobSchedule;
import com.execue.scheduler.props.ExecueTaskProps;
import com.execue.scheduler.service.IExecueJobSchedulerService;
import com.thoughtworks.xstream.XStream;

public class ExecueJobSchedulerServiceTest {

   private IExecueJobSchedulerService execueJobSchedulerService;
   private Logger                     logger = Logger.getLogger(ExecueJobSchedulerServiceTest.class);
   private ApplicationContext         appContext;

   @Before
   public void setUp () {

      appContext = new ClassPathXmlApplicationContext("/bean-config/execue-job-scheduler.xml");
      execueJobSchedulerService = (IExecueJobSchedulerService) appContext.getBean("execueJobSchedulerService");

      try {
         execueJobSchedulerService.startScheduler();
      } catch (ExecueJobSchedulerException e) {
         logger.error(e.getMessage());
      }

   }

   private void createSampleSeedXML () {
      ExecueJob execueJob1 = new ExecueJob();

      ExecueJobData jobData1 = new ExecueJobData();
      ExecueJobSchedule jobSchedule1 = new ExecueJobSchedule();

      jobData1.setJobName("job1");
      jobData1.setJobGroup("default");
      //  jobData1.setJobClass(com.execue.scheduler.job.OptimalDSetJob.class);
      jobData1.setJobDescription("job1 description");
      jobData1.setSeedOperation("ADD");

      jobSchedule1.setPeriodicity(ExecueTaskProps.MONTHLY_SCHEDULE);
      jobSchedule1.setDayOfMonth(new Integer(ExecueTaskProps.EX_DAYS_OF_MONTH[0]).intValue());
      jobSchedule1.setHourOfDay(9);
      jobSchedule1.setMinuteOfHour(9);
      jobSchedule1.setSeedOperation("ADD");
      jobSchedule1.setStartTime(new Date());
      jobSchedule1.setEndTime(new Date());

      execueJob1.setJobData(jobData1);
      execueJob1.setJobSchedule(jobSchedule1);

      ExecueJob execueJob2 = new ExecueJob();

      ExecueJobData jobData2 = new ExecueJobData();
      ExecueJobSchedule jobSchedule2 = new ExecueJobSchedule();

      jobData2.setJobName("job2");
      jobData2.setJobGroup("default");
      // jobData2.setJobClass(com.execue.scheduler.job.OptimalDSetJob.class);
      jobData2.setJobDescription("job2 description");
      jobData2.setSeedOperation("ADD");

      jobSchedule2.setPeriodicity(ExecueTaskProps.MONTHLY_SCHEDULE);
      jobSchedule2.setDayOfMonth(new Integer(ExecueTaskProps.EX_DAYS_OF_MONTH[0]).intValue());
      jobSchedule2.setHourOfDay(9);
      jobSchedule2.setMinuteOfHour(9);
      jobSchedule2.setSeedOperation("ADD");

      execueJob2.setJobData(jobData2);
      execueJob2.setJobSchedule(jobSchedule2);

      List<ExecueJob> execueJobs = new ArrayList<ExecueJob>();
      execueJobs.add(execueJob1);
      execueJobs.add(execueJob2);

      SeedJobCreationDetail seedJobCreationDetail = new SeedJobCreationDetail();
      seedJobCreationDetail.setExecueJobs(execueJobs);

      if (logger.isDebugEnabled())
         logger.debug(new XStream().toXML(seedJobCreationDetail));

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

      if (logger.isDebugEnabled()) {
         logger.debug("identifyJobScheduleCombination ::" + combination);
      }
      return combination;

   }

   @Test
   public void absorbSeedJobs () {

      boolean operationSucceeded = true;

      try {
         String seedJobXMLFile = readFileAsString("/bean-config/job-scheduler-test/quartz-seed.xml");

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
                     execueJobSchedulerService.addJobWithoutTrigger(jobData);
                  } catch (ExecueJobSchedulerException e) {
                     operationSucceeded = false;
                     logger.error("Failed to add job " + jobData.getJobName());
                  }
                  Assert.assertTrue("Failed to add seed job ", operationSucceeded);
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
                     execueJobSchedulerService.scheduleJob(jobData, jobSchedule, scheduleName);
                  } catch (ExecueJobSchedulerException e) {
                     operationSucceeded = false;
                     logger.error("Failed to add job " + jobData.getJobName());
                  }
                  Assert.assertTrue("Failed to add seed job ", operationSucceeded);
               }

               else if (jobScheduleCombo.equals(ExecueTaskProps.RETAIN_DELETEALL)) {

                  // remove all schedules for the job
                  try {
                     jobData = execueJob.getJobData();

                     if (logger.isDebugEnabled())
                        logger.debug("Deleting all schedules of job " + jobData.getJobName());

                     execueJobSchedulerService.removeAllTriggersOfJob(jobData.getJobName(), jobData.getJobGroup());

                  } catch (ExecueJobSchedulerException e) {
                     operationSucceeded = false;
                     logger.error("Failed to remove all schedules for job " + jobData.getJobName());
                  }
                  Assert.assertTrue("Failed to remove all schedules ", operationSucceeded);
               }

               else if (jobScheduleCombo.equals(ExecueTaskProps.RETAIN_DELETE)) {

                  // remove all schedules for the job
                  try {
                     jobData = execueJob.getJobData();

                     for (String name : scheduleNames) {
                        if (logger.isDebugEnabled())
                           logger.debug("Deleting trigger " + name);

                        execueJobSchedulerService.removeJobTrigger(name);
                     }

                  } catch (ExecueJobSchedulerException e) {
                     operationSucceeded = false;
                     logger.error("Failed to remove schedules for job " + jobData.getJobName());
                  }
                  Assert.assertTrue("Failed to remove schedules ", operationSucceeded);
               } else if (jobScheduleCombo.equals(ExecueTaskProps.DELETE_NONE)) {
                  // remove all schedules for the job
                  try {
                     jobData = execueJob.getJobData();

                     execueJobSchedulerService.removeJob(jobData.getJobName(), jobData.getJobGroup());

                  } catch (ExecueJobSchedulerException e) {
                     operationSucceeded = false;
                     logger.error("Failed to remove for job " + jobData.getJobName());
                  }
                  Assert.assertTrue("Failed to remove job ", operationSucceeded);
               }

            }
         }

      } catch (IOException e) {
         operationSucceeded = false;
      }
      Assert.assertTrue("Failed to read seed jobs file ", operationSucceeded);

      listSchedulerJobsInfo();
   }

   public void listSchedulerJobsInfo () {
      // list all the jobs added

      String jobSchedule = "";
      String jobExecutionStatus = "";

      if (logger.isDebugEnabled()) {
         logger
                  .debug("JobName, JobGroup, JobExecutionClass, JobType, JobStatus, Periodicity, PreviousExecution, NextExecution, ScheduleName");

         for (ExecueJob job : execueJobSchedulerService.getAllJobsInScheduler()) {

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

   @After
   public void tearDown () {
      try {
         execueJobSchedulerService.stopScheduler();
      } catch (ExecueJobSchedulerException e) {
         logger.error(e.getMessage());
      }
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

}
