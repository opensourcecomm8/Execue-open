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
import com.execue.core.common.bean.ontology.OntologyAbsorptionContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.JobType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.ontology.exceptions.OntologyException;
import com.execue.ontology.exceptions.OntologyExceptionCodes;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.jobdata.ExecueJobSchedule;
import com.execue.scheduler.props.ExecueTaskProps;
import com.execue.scheduler.service.BaseJobService;
import com.execue.scheduler.service.IExecueJobSchedulerService;
import com.execue.scheduler.service.IFileOntologyDataAbsorptionJobService;
import com.thoughtworks.xstream.XStream;

public class FileOntologyDataAbsorptionJobServiceImpl extends BaseJobService implements
         IFileOntologyDataAbsorptionJobService {

   private IExecueJobSchedulerService execueJobSchedulerService;
   private IJobDataService            jobDataService;

   public Long scheduleFileOntologyDataAbsorbtion (OntologyAbsorptionContext ontologyDataAbsorptionContext)
            throws OntologyException {
      JobRequest jobRequest = null;
      try {
         ExecueJobData jobData = new ExecueJobData();
         populateJobData(jobData, JobType.FILE_ONTOLOGY_DATA_ABSORPTION);
         XStream xStream = new XStream();
         String ontologyDataAbsorptionInputXML = xStream.toXML(ontologyDataAbsorptionContext);
         ExecueJobSchedule jobSchedule = new ExecueJobSchedule();
         jobSchedule.setStartTime(null);
         jobSchedule.setEndTime(null);
         jobSchedule.setNoOfRepeats(0);
         jobSchedule.setPeriodicity(ExecueTaskProps.MINUTELY_SCHEDULE);

         jobRequest = ExecueBeanManagementUtil.prepareJobRequest(JobType.FILE_ONTOLOGY_DATA_ABSORPTION,
                  ontologyDataAbsorptionInputXML, JobStatus.PENDING, new Date(), ontologyDataAbsorptionContext
                           .getUserId());
         jobDataService.createJobRequest(jobRequest);
         jobData.setJobRequest(jobRequest);
         execueJobSchedulerService.scheduleJob(jobData, jobSchedule, null);
      } catch (ExecueJobSchedulerException e) {
         e.printStackTrace();
         throw new OntologyException(OntologyExceptionCodes.ONTOLOGY_DATA_ABSORPTION_FAILED_EXCEPTION_CODE, e);
      } catch (QueryDataException e) {
         throw new OntologyException(OntologyExceptionCodes.ONTOLOGY_DATA_ABSORPTION_FAILED_EXCEPTION_CODE, e);
      }
      return jobRequest.getId();
   }

   /**
    * @return the jobDataService
    */
   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   /**
    * @param jobDataService
    *           the jobDataService to set
    */
   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
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
