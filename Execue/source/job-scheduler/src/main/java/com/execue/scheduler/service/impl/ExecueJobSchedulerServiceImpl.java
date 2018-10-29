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


package com.execue.scheduler.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.SchedulerException;

import com.execue.scheduler.ExecueJob;
import com.execue.scheduler.ExecueJobScheduler;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.exception.ExecueJobSchedulerExceptionCodes;
import com.execue.scheduler.helper.ExecueJobSchedulerServiceHelper;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.jobdata.ExecueJobSchedule;
import com.execue.scheduler.service.IExecueJobSchedulerService;

public class ExecueJobSchedulerServiceImpl implements IExecueJobSchedulerService {

   Logger                                  logger = Logger.getLogger(ExecueJobSchedulerServiceImpl.class);

   private ExecueJobSchedulerServiceHelper execueJobSchedulerServiceHelper;

   // private JobSchedulerUtils jobSchedulerUtils;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.IExecueJobSchedulerService#getAllJobsInScheduler()
    */
   public List<ExecueJob> getAllJobsInScheduler () {
      try {
         return getExecueJobSchedulerServiceHelper().getAllExecueJobs();
      } catch (SchedulerException e) {
         // TODO Auto-generated catch block
         return new ArrayList<ExecueJob>();
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.IExecueJobSchedulerService#addJobWithoutTrigger(com.execue.scheduler.jobdata.ExecueJobData)
    */
   public void addJobWithoutTrigger (ExecueJobData jobData) throws ExecueJobSchedulerException {
      try {

         getExecueJobSchedulerServiceHelper().registerJobDetailWithoutTrigger(jobData);

      } catch (ObjectAlreadyExistsException e) {
         logger.error("ObjectAlreadyExistsException when attempting to add job[Name, Group] -- " + "["
                  + jobData.getJobName() + "," + jobData.getJobGroup() + "]");
         throw new ExecueJobSchedulerException(ExecueJobSchedulerExceptionCodes.JOB_ALREADY_EXISTS,
                  "addJobWithoutTrigger FAILED !!!");

      } catch (SchedulerException e) {
         logger.error("addJobWithoutTrigger FAILED when attempting to add job[Name, Group] -- " + "["
                  + jobData.getJobName() + "," + jobData.getJobGroup() + "]");
         ;
         throw new ExecueJobSchedulerException(ExecueJobSchedulerExceptionCodes.ADD_JOB_FAILED,
                  "addJobWithoutTrigger FAILED !!!");
      }
   }

   private boolean isSchedulerStarted () throws ExecueJobSchedulerException {
      try {
         return ExecueJobScheduler.getSchedulerInstance().isStarted();
      } catch (SchedulerException e) {
         throw new ExecueJobSchedulerException(ExecueJobSchedulerExceptionCodes.SCHEDULER_STATUS_UNKNOWN,
                  "isSchedulerStarted FAILED !!!");
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.IExecueJobSchedulerService#fireOnDemandJob(java.lang.String, java.lang.String)
    */
   public void fireOnDemandJob (String jobName, String jobGroup) throws ExecueJobSchedulerException {
      try {
         startScheduler();

         // registerJobWithScheduler should already have created JobDetail
         // with the supplied name:group
         // otherwise the call will fail
         getExecueJobSchedulerServiceHelper().fireOnDemandJob(jobName, jobGroup);
      } catch (SchedulerException e) {
         // TODO Auto-generated catch block
         throw new ExecueJobSchedulerException(ExecueJobSchedulerExceptionCodes.ONDEMAND_JOB_FIRING_FAILED,
                  "fireOnDemandJob FAILED !!!");
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.IExecueJobSchedulerService#scheduleJob(com.execue.scheduler.jobdata.ExecueJobData,
    *      com.execue.scheduler.jobdata.ExecueJobSchedule, java.lang.String)
    */
   public void scheduleJob (ExecueJobData jobData, ExecueJobSchedule jobSchedule, String triggerName)
            throws ExecueJobSchedulerException {
      try {
         //startScheduler();
         getExecueJobSchedulerServiceHelper().scheduleJob(jobData, jobSchedule, triggerName);
      } catch (SchedulerException e) {
         throw new ExecueJobSchedulerException(ExecueJobSchedulerExceptionCodes.SCHEDULING_FAILED, e.getMessage());
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.IExecueJobSchedulerService#removeJobTrigger(java.lang.String)
    */
   public void removeJobTrigger (String triggerName) throws ExecueJobSchedulerException {
      try {
         getExecueJobSchedulerServiceHelper().removeJobTrigger(triggerName);
      } catch (SchedulerException e) {
         throw new ExecueJobSchedulerException(ExecueJobSchedulerExceptionCodes.REMOVE_JOB_TRIGGER_FAILED,
                  "removeJobTrigger FAILED !!!");
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.IExecueJobSchedulerService#removeAllJobs()
    */
   public void removeAllJobs () throws ExecueJobSchedulerException {
      try {
         getExecueJobSchedulerServiceHelper().removeAllJobs();
      } catch (SchedulerException e) {
         throw new ExecueJobSchedulerException(ExecueJobSchedulerExceptionCodes.REMOVE_ALL_JOBS_FAILED,
                  "removeAllJobs FAILED !!!");
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.IExecueJobSchedulerService#removeAllTriggers()
    */
   public void removeAllTriggers () throws ExecueJobSchedulerException {
      try {
         getExecueJobSchedulerServiceHelper().removeAllTriggers();
      } catch (SchedulerException e) {
         throw new ExecueJobSchedulerException(ExecueJobSchedulerExceptionCodes.REMOVE_JOB_TRIGGER_FAILED,
                  "removeAllTriggers FAILED !!!");
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.IExecueJobSchedulerService#removeAllTriggersOfJob(java.lang.String, java.lang.String)
    */
   public void removeAllTriggersOfJob (String jobName, String jobGroup) throws ExecueJobSchedulerException {
      try {
         getExecueJobSchedulerServiceHelper().removeAllTriggersOfJob(jobName, jobGroup);
      } catch (SchedulerException e) {
         throw new ExecueJobSchedulerException(ExecueJobSchedulerExceptionCodes.REMOVE_JOB_TRIGGER_FAILED,
                  "removeAllTriggersOfJob FAILED !!!");
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.IExecueJobSchedulerService#removeJob(java.lang.String, java.lang.String)
    */
   public void removeJob (String jobName, String jobGroup) throws ExecueJobSchedulerException {
      try {
         getExecueJobSchedulerServiceHelper().removeJob(jobName, jobGroup);
      } catch (SchedulerException e) {
         throw new ExecueJobSchedulerException(ExecueJobSchedulerExceptionCodes.REMOVE_ALL_JOBS_FAILED,
                  "removeJob FAILED !!!");
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.IExecueJobSchedulerService#getJobGroups()
    */
   public List<String> getJobGroups () {
      try {
         return getExecueJobSchedulerServiceHelper().getJobGroups();
      } catch (SchedulerException e) {
         // TODO Auto-generated catch block
         return new ArrayList<String>();
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.IExecueJobSchedulerService#getJobControllerMap()
    */
   public Map<String, List<String>> getJobControllerMap () {
      try {
         return getExecueJobSchedulerServiceHelper().getJobControllerMap();
      } catch (SchedulerException e) {
         // TODO Auto-generated catch block
         return new HashMap<String, List<String>>();
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.IExecueJobSchedulerService#getAllJobsOfGroupAndName(java.lang.String, java.lang.String)
    */
   public List<ExecueJob> getAllJobsOfGroupAndName (String jobGroup, String jobName) {
      try {
         return getExecueJobSchedulerServiceHelper().getAllJobsOfGroupAndName(jobGroup, jobName);
      } catch (SchedulerException e) {
         // TODO Auto-generated catch block
         return new ArrayList<ExecueJob>();
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.IExecueJobSchedulerService#getAllJobControllersOfGroup(java.lang.String)
    */
   public List<String> getAllJobControllersOfGroup (String jobGroup) {
      try {
         return getExecueJobSchedulerServiceHelper().getAllJobControllersOfGroup(jobGroup);
      } catch (SchedulerException e) {
         // TODO Auto-generated catch block
         return new ArrayList<String>();
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.IExecueJobSchedulerService#getJobDescription(java.lang.String, java.lang.String)
    */
   public String getJobDescription (String jobName, String jobGroup) {
      try {
         return getExecueJobSchedulerServiceHelper().getJobDescription(jobName, jobGroup);
      } catch (SchedulerException e) {
         // TODO Auto-generated catch block
         return "no description";
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.IExecueJobSchedulerService#startScheduler()
    */
   /*
    * public void scheduleOnDemandJob(ExecueJobData jobData) { scheduleMinutelyJob(jobData); } private static void
    * scheduleMinutelyJob(ExecueJobData jobData) { ExecueJobSchedule jobSchedule = new ExecueJobSchedule();
    * jobSchedule.setStartTime(new Date()); jobSchedule.setNoOfRepeats(0);
    * jobSchedule.setPeriodicity(ExecueTaskProps.MINUTELY_SCHEDULE); ExecueJob exJob = new ExecueJob();
    * exJob.setJobData(jobData); exJob.setJobSchedule(jobSchedule); exJob.setJobType(ExecueTaskProps.SCHEDULED_JOB);
    * ExecueJobSchedulerServiceImpl.scheduleJob(exJob); }
    */
   public void startScheduler () throws ExecueJobSchedulerException {
      if (!isSchedulerStarted()) {
         try {
            ExecueJobScheduler.getSchedulerInstance().start();
         } catch (SchedulerException e) {
            throw new ExecueJobSchedulerException(ExecueJobSchedulerExceptionCodes.SCHEDULER_STATUS_UNKNOWN,
                     "startScheduler FAILED !!!");
         }
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.IExecueJobSchedulerService#stopScheduler()
    */
   public void stopScheduler () throws ExecueJobSchedulerException {
      if (isSchedulerStarted()) {
         try {
            ExecueJobScheduler.getSchedulerInstance().shutdown();
         } catch (SchedulerException e) {
            throw new ExecueJobSchedulerException(ExecueJobSchedulerExceptionCodes.SCHEDULER_STATUS_UNKNOWN,
                     "stopScheduler FAILED !!!");
         }
      }
   }

   /**
    * @return the execueJobSchedulerServiceHelper
    */
   public ExecueJobSchedulerServiceHelper getExecueJobSchedulerServiceHelper () {
      return execueJobSchedulerServiceHelper;
   }

   /**
    * @param execueJobSchedulerServiceHelper the execueJobSchedulerServiceHelper to set
    */
   public void setExecueJobSchedulerServiceHelper (ExecueJobSchedulerServiceHelper execueJobSchedulerServiceHelper) {
      this.execueJobSchedulerServiceHelper = execueJobSchedulerServiceHelper;
   }

}
