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


package com.execue.scheduler.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.execue.core.common.bean.IExeCueJobRequestContextData;
import com.execue.core.common.bean.entity.JobHistoryOperationalStatus;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationCategory;
import com.execue.core.common.type.NotificationParamName;
import com.execue.core.common.type.NotificationType;
import com.execue.core.common.type.TemplateType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.exception.UserNotificationException;
import com.execue.qdata.service.IJobDataService;
import com.execue.qdata.service.IUserNotificationService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.exception.ExecueJobSchedulerExceptionCodes;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;
import com.execue.scheduler.props.ExecueTaskProps;
import com.execue.swi.service.IUserManagementService;
import com.thoughtworks.xstream.XStream;

/**
 * Job Wrapper for ExeCue Jobs.
 * 
 * @author Raju Gottumukkala
 */
public abstract class BaseLineJob implements Job {

   private static final Logger       log                     = Logger.getLogger(BaseLineJob.class);

   // Purely Local variables Begin
   private static final String       INITIATION_NOTIFICATION = "IN";
   private static final String       COMPLETION_NOTIFICATION = "CN";
   // Purely Local variables End

   public static final String        OPERATION_INITIATED     = "initiated";
   public static final String        OPERATION_COMPLETED     = "completed";
   public static final String        OPERATION_FAILED        = "failed";

   private IUserNotificationService  userNotificationService;
   private IJobDataService           jobDataService;
   private IUserManagementService    userService;
   private ICoreConfigurationService coreConfigurationService;

   /**
    * Overrides the execute from org.quartz.Job.
    */
   public void execute (JobExecutionContext jobExecutionContext) throws JobExecutionException {
      ExecueJobData jobData = null;
      try {
         init();
         ExecueJobData jobClassData = (ExecueJobData) jobExecutionContext.getJobDetail().getJobDataMap().get(
                  ExecueTaskProps.EXJOB_DATA);
         if (jobClassData.isScheduled()) {
            JobRequest jobRequest = ExecueBeanManagementUtil.prepareJobRequest(jobClassData.getJobType(), null,
                     JobStatus.PENDING, new Date(), 1L);
            jobDataService.createJobRequest(jobRequest);
            jobData = (ExecueJobData) jobExecutionContext.getTrigger().getJobDataMap().get(ExecueTaskProps.EXJOB_DATA);
            jobData.setJobRequest(jobRequest);
         }
         jobData = preProcess(jobExecutionContext);
         process(jobData);

      } catch (Exception exp) {
         if (jobData != null) {
            jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         }
         throw new JobExecutionException(ExecueJobSchedulerExceptionCodes.JOB_PROCESSING_FAILED + " - "
                  + exp.getMessage(), exp);
      } finally {
         try {
            postProcess(jobData);
         } catch (ExecueJobSchedulerException jobSchedException) {
            log.error(jobSchedException, jobSchedException);
         }
      }
   }

   /**
    * Abstract method to be implemented by inherited classes. If an exception is eaten up with in this method
    * implementation by the inheriting classes for any reason, JobStatus on JobRequest contained in ExecueJobData should
    * be set accordingly
    * 
    * @param jobData
    * @throws ExecueJobSchedulerException
    */
   protected abstract void process (ExecueJobData jobData) throws ExecueJobSchedulerException;

   /**
    * Call back method for inherited classes to customize the Initiation Notification details
    * 
    * @param jobData
    * @return
    * @throws ExecueJobSchedulerException
    */
   protected void prepareInitiationMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
   }

   /**
    * Call back method for inherited classes to customize the Completion Notification details
    * 
    * @param jobData
    * @return
    * @throws ExecueJobSchedulerException
    */
   protected void prepareCompletionMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
   }

   /**
    * loads the dependencies from Spring Context
    * 
    * @throws ExecueJobSchedulerException
    */
   private void init () throws ExecueJobSchedulerException {
      try {
         userNotificationService = (IUserNotificationService) SpringContextHolder.getBean("userNotificationService");
         jobDataService = (IJobDataService) SpringContextHolder.getBean("jobDataService");
         userService = ((IUserManagementService) SpringContextHolder.getBean("userManagementService"));
         coreConfigurationService = (ICoreConfigurationService) SpringContextHolder.getBean("coreConfigurationService");
      } catch (Exception exp) {
         throw new ExecueJobSchedulerException(ExecueJobSchedulerExceptionCodes.JOB_PRE_PROCESSING_FAILED, exp);
      }
   }

   /**
    * preProcess to extract ExecueJobData object from JobExecutionContext. Extracts The request context data from string
    * based XML to IExeCueJobRequestContextData and sets to ExecueJobData instance. Invokes postNotification with
    * INITIATION_NOTIFICATION flag. Update the JobRequest with INPROGRESS status
    * 
    * @param jobExecutionContext
    * @return
    * @throws ExecueJobSchedulerException
    */
   private ExecueJobData preProcess (JobExecutionContext jobExecutionContext) throws ExecueJobSchedulerException {
      if (log.isDebugEnabled()) {
         log.debug("Pre Processing");
      }
      ExecueJobData jobData = null;
      try {
         jobData = (ExecueJobData) jobExecutionContext.getTrigger().getJobDataMap().get(ExecueTaskProps.EXJOB_DATA);
         String requestData = jobData.getJobRequest().getRequestData();
         if (requestData != null) {
            jobData.setRequestContextData((IExeCueJobRequestContextData) new XStream().fromXML(requestData));
         }
         getJobDataService().updateJobRequest(
                  ExecueBeanManagementUtil.modifyJobRequest(jobData.getJobRequest(), JobStatus.INPROGRESS, null));

         postNotification(jobData, INITIATION_NOTIFICATION);
      } catch (Exception exp) {
         throw new ExecueJobSchedulerException(ExecueJobSchedulerExceptionCodes.JOB_PRE_PROCESSING_FAILED, exp);
      }
      return jobData;
   }

   /**
    * Place holder for any post processing in general. Invokes postNotification with COMPLETION_NOTIFICATION flag.
    * Update the JobRequest with latest status and then move the operation status messages to history table from live
    * table
    * 
    * @param jobData
    * @throws ExecueJobSchedulerException
    */
   private void postProcess (ExecueJobData jobData) throws ExecueJobSchedulerException {
      try {
         jobDataService.updateJobRequest(ExecueBeanManagementUtil.modifyJobRequest(jobData.getJobRequest(), jobData
                  .getJobRequest().getJobStatus(), new Date()));
      } catch (QueryDataException qde) {
         log.error(qde, qde);
      }
      try {
         List<JobOperationalStatus> jobOperationalStatus = jobDataService.getJobOperationalStatus(jobData
                  .getJobRequest().getId());
         List<JobHistoryOperationalStatus> jobHistoryOperationalStatus = ExecueBeanCloneUtil
                  .cloneJobOperationStatus(jobOperationalStatus);
         jobDataService.createJobHistoryOperationalStatus(jobHistoryOperationalStatus);
         jobDataService.deleteJobOperationStatus(jobOperationalStatus);
      } catch (QueryDataException qde) {
         log.error(qde, qde);
      }
      postNotification(jobData, COMPLETION_NOTIFICATION);
   }

   /**
    * Posts the notification if the Job is set to true for notifications
    * 
    * @param jobData
    * @param notificationType
    * @throws ExecueJobSchedulerException
    */
   private void postNotification (ExecueJobData jobData, String notificationType) throws ExecueJobSchedulerException {
      try {
         if (jobData.isNotificationNeeded()) {
            NotificationType type = NotificationType.getTypeByLiteral(jobData.getJobRequest().getJobType().toString());
            NotificationCategory category = NotificationCategory.OFFLINE_JOB;
            Long userId = jobData.getJobRequest().getUserId();
            Map<NotificationParamName, String> subjectParams = new HashMap<NotificationParamName, String>();
            Map<NotificationParamName, String> bodyParams = new HashMap<NotificationParamName, String>();
            initializeNotificationParams(type, subjectParams, bodyParams);
            if (INITIATION_NOTIFICATION.equals(notificationType)) {
               subjectParams.put(NotificationParamName.OPERATION, OPERATION_INITIATED);
               bodyParams.put(NotificationParamName.OPERATION, OPERATION_INITIATED);
               prepareInitiationMessage(jobData, subjectParams, bodyParams);
            } else {
               if (JobStatus.FAILURE.equals(jobData.getJobRequest().getJobStatus())) {
                  subjectParams.put(NotificationParamName.OPERATION, OPERATION_FAILED);
                  bodyParams.put(NotificationParamName.OPERATION, OPERATION_FAILED);
               } else {
                  subjectParams.put(NotificationParamName.OPERATION, OPERATION_COMPLETED);
                  bodyParams.put(NotificationParamName.OPERATION, OPERATION_COMPLETED);
               }
               prepareCompletionMessage(jobData, subjectParams, bodyParams);
            }

            getUserNotificationService().createUserNotification(type, category, userId, subjectParams, bodyParams);
         }
      } catch (UserNotificationException une) {
         throw new ExecueJobSchedulerException(une.getCode(), une);
      }

   }

   private void initializeNotificationParams (NotificationType type, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws UserNotificationException {
      Map<TemplateType, String> notificationTemplateParams = getUserNotificationService()
               .getUserNotificationTemplateParams(type);
      String subjectParamNames = notificationTemplateParams.get(TemplateType.SUBJECT);
      String[] subjectParamNamesList = subjectParamNames.split("~");
      for (String subjectParam : subjectParamNamesList) {
         subjectParams.put(NotificationParamName.getType(subjectParam), "");
      }
      String bodyParamNames = notificationTemplateParams.get(TemplateType.BODY_CONTENT);
      String[] bodyParamNamesList = bodyParamNames.split("~");
      for (String bodyParam : bodyParamNamesList) {
         bodyParams.put(NotificationParamName.getType(bodyParam), "");
      }

   }

   /**
    * /**
    * 
    * @return the userNotificationService
    */
   private IUserNotificationService getUserNotificationService () {
      return userNotificationService;
   }

   /**
    * @return the jobDataService
    */
   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   /**
    * @return the userService
    */
   public IUserManagementService getUserService () {
      return userService;
   }

   /**
    * @param userService
    *           the userService to set
    */
   public void setUserService (IUserManagementService userService) {
      this.userService = userService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}
