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


package com.execue.platform.querydata.impl;

import java.util.Date;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.swi.RuntimeTablesCleanupContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.platform.querydata.ICleanQDataRuntimeTablesService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.exception.QueryDataExceptionCodes;
import com.execue.qdata.exception.RFXException;
import com.execue.qdata.exception.UDXException;
import com.execue.qdata.service.IJobDataService;
import com.execue.qdata.service.IMessageService;
import com.execue.qdata.service.IRFXService;
import com.execue.qdata.service.IUDXService;

public class CleanQDataRuntimeTablesServiceImpl implements ICleanQDataRuntimeTablesService {

   private static final Logger logger = Logger.getLogger(CleanQDataRuntimeTablesServiceImpl.class);
   private IUDXService         udxService;
   private IRFXService         rfxService;
   private IJobDataService     jobDataService;
   private IMessageService     messageService;

   public void cleanQDataRuntimeTables (RuntimeTablesCleanupContext runtimeTablesCleanupContext)
            throws QueryDataException {
      JobOperationalStatus jobOperationalStatus = null;
      JobRequest jobRequest = runtimeTablesCleanupContext.getJobRequest();
      try {
         try {
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Cleaning RIUserQuery Table", JobStatus.INPROGRESS, null, new Date());
            jobDataService.createJobOperationStatus(jobOperationalStatus);
            getUdxService().cleanRIUserQueries();
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         } catch (UDXException uxdException) {
            if (logger.isDebugEnabled()) {
               logger
                        .debug("Exception occured while cleaning RI_UserQueries. Proceeding further to cleanup UserQueryReducedForms ");
            }
         }

         // NK: UserQueryReducedFormIndexes is no more getting populated, so no clean up required
         // try {
         // jobOperationalStatus = ExecueBeanCloneUtil.prepareJobOperationalStatus(jobRequest,
         // "Cleaning UserQueryRFX Table", JobStatus.INPROGRESS, null, new Date());
         // jobDataService.createJobOperationStatus(jobOperationalStatus);
         // getRfxService().cleanUserQueryReducedFormIndexes();
         // jobOperationalStatus = ExecueBeanCloneUtil.modifyJobOperationalStatus(jobOperationalStatus,
         // JobStatus.SUCCESS,
         // null, new Date());
         // getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         // } catch (RFXException rfxException) {
         // if (logger.isDebugEnabled()) {
         // logger.debug("Exception occured while cleaning UserQueryRFX.");
         // }
         // }
         try {
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Cleaning UserQueryRFXValue Table", JobStatus.INPROGRESS, null, new Date());
            jobDataService.createJobOperationStatus(jobOperationalStatus);
            getRfxService().cleanUserQueryRFXValue();
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         } catch (RFXException rfxException) {
            if (logger.isDebugEnabled()) {
               logger.debug("Exception occured while cleaning UserQueryRFXValue.");
            }
         }
         try {
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Cleaning UdxResults Table", JobStatus.INPROGRESS, null, new Date());
            jobDataService.createJobOperationStatus(jobOperationalStatus);
            getUdxService().cleanUdxResults();
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         } catch (UDXException udxException) {
            if (logger.isDebugEnabled()) {
               logger.debug("Exception occured while cleaning UdxResults.");
            }
         }

         // TODO: Clean up the message older than configurable no. of hours

         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Cleaning Message Table", JobStatus.INPROGRESS, null, new Date());
         jobDataService.createJobOperationStatus(jobOperationalStatus);
         getMessageService().cleanOldMessages();
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

      } catch (ExeCueException exception) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, exception
                     .getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new QueryDataException(QueryDataExceptionCodes.RUNTIME_TABLES_CLEANUP_FAILURE_CODE, exception);
            }
         }
         throw new QueryDataException(QueryDataExceptionCodes.RUNTIME_TABLES_CLEANUP_FAILURE_CODE, exception);
      }
   }

   public IUDXService getUdxService () {
      return udxService;
   }

   public void setUdxService (IUDXService udxService) {
      this.udxService = udxService;
   }

   public IRFXService getRfxService () {
      return rfxService;
   }

   public void setRfxService (IRFXService rfxService) {
      this.rfxService = rfxService;
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   /**
    * @return the messageService
    */
   public IMessageService getMessageService () {
      return messageService;
   }

   /**
    * @param messageService
    *           the messageService to set
    */
   public void setMessageService (IMessageService messageService) {
      this.messageService = messageService;
   }

}
