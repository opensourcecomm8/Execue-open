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

import java.util.Date;

import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.publisher.PublisherDataAbsorptionContext;
import com.execue.core.common.type.AppOperationType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.JobType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.publisher.exception.PublisherException;
import com.execue.publisher.exception.PublisherExceptionCodes;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.jobdata.ExecueJobSchedule;
import com.execue.scheduler.props.ExecueTaskProps;
import com.execue.scheduler.service.BaseJobService;
import com.execue.scheduler.service.IExecueJobSchedulerService;
import com.execue.scheduler.service.IPublisherDataAbsorbtionJobService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.PublishedFileException;
import com.execue.swi.service.IApplicationManagementService;
import com.execue.swi.service.IPublishedFileManagementService;
import com.execue.swi.service.IPublishedFileRetrievalService;
import com.thoughtworks.xstream.XStream;

public class PublisherDataAbsorptionJobServiceImpl extends BaseJobService implements IPublisherDataAbsorbtionJobService {

   private IExecueJobSchedulerService      execueJobSchedulerService;
   private IJobDataService                 jobDataService;
   private IPublishedFileRetrievalService  publishedFileRetrievalService;
   private IPublishedFileManagementService publishedFileManagementService;
   private IApplicationManagementService   applicationManagementService;

   public Long schedulePublisherDataAbsorbtion (PublisherDataAbsorptionContext publisherDataAbsorptionContext)
            throws PublisherException {
      JobRequest jobRequest = null;
      try {
         ExecueJobData jobData = new ExecueJobData();
         populateJobData(jobData, JobType.PUBLISHER_DATA_ABSORPTION);
         XStream xStream = new XStream();
         String publisherDataAbsorptionInputXML = xStream.toXML(publisherDataAbsorptionContext);
         ExecueJobSchedule jobSchedule = new ExecueJobSchedule();
         jobSchedule.setStartTime(null);
         jobSchedule.setEndTime(null);
         jobSchedule.setNoOfRepeats(0);
         jobSchedule.setPeriodicity(ExecueTaskProps.MINUTELY_SCHEDULE);

         jobRequest = ExecueBeanManagementUtil.prepareJobRequest(JobType.PUBLISHER_DATA_ABSORPTION,
                  publisherDataAbsorptionInputXML, JobStatus.PENDING, new Date(), publisherDataAbsorptionContext
                           .getUserId());
         jobDataService.createJobRequest(jobRequest);
         jobData.setJobRequest(jobRequest);
         execueJobSchedulerService.scheduleJob(jobData, jobSchedule, null);
         PublishedFileInfo publishedFileInfo = getPublishedFileRetrievalService().getPublishedFileInfoById(
                  publisherDataAbsorptionContext.getFileInfoId());
         publishedFileInfo.setAbsorbtionJobRequestId(jobRequest.getId());
         publisherDataAbsorptionContext.setJobRequest(jobRequest);
         getPublishedFileManagementService().updatePublishedFileInfo(publishedFileInfo);
         if (publisherDataAbsorptionContext.getApplicationId() != null) {
            getApplicationManagementService().updateApplicationOperationDetails(
                     publisherDataAbsorptionContext.getApplicationId(), AppOperationType.ANALYZING, jobRequest.getId(),
                     JobStatus.INPROGRESS);
         }
      } catch (ExecueJobSchedulerException e) {
         e.printStackTrace();
         throw new PublisherException(PublisherExceptionCodes.PUBLISHER_DATA_ABSORPTION_FAILED_EXCEPTION_CODE, e);
      } catch (QueryDataException e) {
         throw new PublisherException(PublisherExceptionCodes.PUBLISHER_DATA_ABSORPTION_FAILED_EXCEPTION_CODE, e);
      } catch (PublishedFileException publishedFileException) {
         throw new PublisherException(publishedFileException.getCode(), publishedFileException);
      } catch (KDXException kdxException) {
         throw new PublisherException(kdxException.getCode(), kdxException);
      }
      return jobRequest.getId();
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public IApplicationManagementService getApplicationManagementService () {
      return applicationManagementService;
   }

   public void setApplicationManagementService (IApplicationManagementService applicationManagementService) {
      this.applicationManagementService = applicationManagementService;
   }

   public IPublishedFileRetrievalService getPublishedFileRetrievalService () {
      return publishedFileRetrievalService;
   }

   public void setPublishedFileRetrievalService (IPublishedFileRetrievalService publishedFileRetrievalService) {
      this.publishedFileRetrievalService = publishedFileRetrievalService;
   }

   public IPublishedFileManagementService getPublishedFileManagementService () {
      return publishedFileManagementService;
   }

   public void setPublishedFileManagementService (IPublishedFileManagementService publishedFileManagementService) {
      this.publishedFileManagementService = publishedFileManagementService;
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
