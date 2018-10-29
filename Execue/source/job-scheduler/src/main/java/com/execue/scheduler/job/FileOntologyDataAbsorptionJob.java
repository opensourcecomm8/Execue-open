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
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.ontology.OntologyAbsorptionContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.NotificationParamName;
import com.execue.ontology.absorbtion.IFileOntologyDataAbsorptionService;
import com.execue.scheduler.exception.ExecueJobSchedulerException;
import com.execue.scheduler.jobdata.ExecueJobData;
import com.execue.scheduler.listener.SpringContextHolder;

public class FileOntologyDataAbsorptionJob extends BaseLineJob {

   private IFileOntologyDataAbsorptionService fileOntologyDataAbsorptionService = null;

   private static final Logger                log                               = Logger
                                                                                         .getLogger(FileOntologyDataAbsorptionJob.class);

   @Override
   protected void process (ExecueJobData jobData) throws ExecueJobSchedulerException {
      OntologyAbsorptionContext ontologyAbsorptionContext = null;
      try {
         ontologyAbsorptionContext = (OntologyAbsorptionContext) jobData.getRequestContextData();
         log.debug("Invoking on file Ontology DataAbsorptionJob Job [" + jobData.getJobName() + "]");
         initializeDependencies();
         ontologyAbsorptionContext.setJobRequest(jobData.getJobRequest());
         fileOntologyDataAbsorptionService.absorbOntology(ontologyAbsorptionContext);
         jobData.getJobRequest().setJobStatus(JobStatus.SUCCESS);
      } catch (Exception e) {
         e.printStackTrace();
         jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         log.error("JOB Execution failed for job -- " + jobData.getJobName());
         jobData.getJobRequest().setJobStatus(JobStatus.FAILURE);
         log.error(e, e);
      }
   }

   @Override
   protected void prepareCompletionMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      OntologyAbsorptionContext ontologyAbsorptionContext = (OntologyAbsorptionContext) jobData.getRequestContextData();

      subjectParams.put(NotificationParamName.MODEL_NAME, ontologyAbsorptionContext.getModelId() + "");
      bodyParams.put(NotificationParamName.MODEL_NAME, ontologyAbsorptionContext.getModelId() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.scheduler.job.BaseLineJob#prepareInitiationMessage(com.execue.scheduler.jobdata.ExecueJobData,
    *      java.util.Map, java.util.Map)
    */
   @Override
   protected void prepareInitiationMessage (ExecueJobData jobData, Map<NotificationParamName, String> subjectParams,
            Map<NotificationParamName, String> bodyParams) throws ExecueJobSchedulerException {
      OntologyAbsorptionContext ontologyAbsorptionContext = (OntologyAbsorptionContext) jobData.getRequestContextData();

      subjectParams.put(NotificationParamName.MODEL_NAME, ontologyAbsorptionContext.getModelId() + "");
      bodyParams.put(NotificationParamName.MODEL_NAME, ontologyAbsorptionContext.getModelId() + "");
      bodyParams.put(NotificationParamName.TIME_STAMP, new Date() + "");

   }

   private void initializeDependencies () throws Exception {
      fileOntologyDataAbsorptionService = (IFileOntologyDataAbsorptionService) SpringContextHolder
               .getBean("fileOntologyDataAbsorptionService");
   }
}
