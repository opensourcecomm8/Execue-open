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


package com.execue.das.datatransfer.etl.service.impl;

import org.apache.log4j.Logger;

import scriptella.execution.EtlExecutor;
import scriptella.execution.EtlExecutorException;
import scriptella.execution.ExecutionStatistics;
import scriptella.execution.ExecutionStatistics.ElementInfo;

import com.execue.das.datatransfer.etl.bean.ETLInput;
import com.execue.das.datatransfer.etl.bean.DataTransferQueryStatus;
import com.execue.das.datatransfer.etl.exception.ETLException;
import com.execue.das.datatransfer.etl.helper.ScriptellaETLInputHelper;
import com.execue.das.datatransfer.etl.service.IETLService;
import com.execue.das.exception.DataAccessServicesExceptionCodes;

public class ScriptellaETLServiceImpl implements IETLService {

   private static final Logger log = Logger.getLogger(ScriptellaETLServiceImpl.class);

   /**
    * Prepares scriptella input and executes the scriptella xml file
    */
   public DataTransferQueryStatus executeETLProcess (ETLInput etlInput) throws ETLException {
      DataTransferQueryStatus remoteQueryStatus = new DataTransferQueryStatus();
      try {
         EtlExecutor etlExecutor = EtlExecutor.newExecutor(ScriptellaETLInputHelper.prepareScriptellaInput(etlInput));
         ExecutionStatistics executionStatistics = etlExecutor.execute();
         populateStatus(executionStatistics, remoteQueryStatus);
      } catch (EtlExecutorException etlExecutorException) {
         throw new ETLException(DataAccessServicesExceptionCodes.DEFAULT_ETL_SCRIPTELLA_EXCEPTION_CODE,
                  "EtlExecutorException", etlExecutorException);
      }
      return remoteQueryStatus;
   }

   private void populateStatus (ExecutionStatistics executionStatistics, DataTransferQueryStatus remoteQueryStatus) {
      try {
         remoteQueryStatus.setStartTime(executionStatistics.getStartDate().getTime());
         remoteQueryStatus.setEndTime(executionStatistics.getFinishDate().getTime());
         int recordCount = 0;
         for (ElementInfo elementInfo : executionStatistics.getElements()) {
            if (elementInfo.getId().contains("script")) {
               recordCount = recordCount + elementInfo.getSuccessfulExecutionCount();
            }
         }
         remoteQueryStatus.setRecordCount(recordCount);
      } catch (Exception exception) {
         log.error("THIS SHOULD NOT HAVE HAPPENED [EATEN EXCEPTION] : " + exception.getMessage());
      }
   }

}
