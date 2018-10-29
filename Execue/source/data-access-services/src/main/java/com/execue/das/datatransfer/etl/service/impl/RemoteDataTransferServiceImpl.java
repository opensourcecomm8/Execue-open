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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.type.SuccessFailureType;
import com.execue.das.datatransfer.etl.bean.DataTransferInput;
import com.execue.das.datatransfer.etl.bean.DataTransferQuery;
import com.execue.das.datatransfer.etl.bean.DataTransferQueryStatus;
import com.execue.das.datatransfer.etl.bean.DataTransferStatus;
import com.execue.das.datatransfer.etl.bean.ETLInput;
import com.execue.das.datatransfer.etl.exception.ETLException;
import com.execue.das.datatransfer.etl.exception.RemoteDataTransferException;
import com.execue.das.datatransfer.etl.helper.RemoteDataTransferHelper;
import com.execue.das.datatransfer.etl.service.IETLService;
import com.execue.das.datatransfer.etl.service.IRemoteDataTransferConstants;
import com.execue.das.datatransfer.etl.service.IRemoteDataTransferService;
import com.execue.das.datatransfer.etl.util.DataTransferUtil;
import com.execue.das.exception.DataAccessServicesExceptionCodes;

/**
 * This class is helper class used by the services to create cube/mart owned by ExeCue for ETL purpose.
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/01/11
 */
public class RemoteDataTransferServiceImpl implements IRemoteDataTransferService, IRemoteDataTransferConstants {

   private static final Logger      logger = Logger.getLogger(RemoteDataTransferServiceImpl.class);
   private IETLService              etlService;
   private RemoteDataTransferHelper remoteDataTransferHelper;

   /**
    * This method transfers data using ETL process
    * 
    * @param dataTransferInput
    * @return dataTransferStatus
    */
   public DataTransferStatus transferRemoteData (DataTransferInput dataTransferInput)
            throws RemoteDataTransferException {
      DataTransferStatus dataTransferStatus = null;
      try {
         logger.debug("Started transfer remote data");
         dataTransferStatus = performRemoteDataTransfer(dataTransferInput);
         logger.debug("checking Status of remote data transfer");
         if (!checkETLExecutionStatus(dataTransferStatus)) {
            logger.debug("Transfer failed and going for retrying");
            retryRemoteDataTransfer(dataTransferStatus, RETRY_COUNT);
         }
      } catch (InterruptedException e) {
         throw new RemoteDataTransferException(
                  DataAccessServicesExceptionCodes.DEFAULT_DATA_TRANSFER_ETL_PROCESS_EXCEPTION_CODE, e);
      }
      return dataTransferStatus;
   }

   public DataTransferStatus transferRemoteData (DataSource sourceDataSource, DataSource targetDataSource,
            String targetInsertQuery, String sourceSelectQuery, String targetCreateQuery, String targetTable)
            throws RemoteDataTransferException {
      DataTransferInput dataTransferInput = DataTransferUtil.prepareDataTransferInput(sourceDataSource,
               targetDataSource, targetInsertQuery, sourceSelectQuery, targetCreateQuery, targetTable);
      return transferRemoteData(dataTransferInput);
   }

   public DataTransferStatus transferRemoteData (DataSource sourceDataSource, DataSource targetDataSource,
            String targetInsertQuery, String sourceSelectQuery, String targetCreateQuery, String targetRollBackQuery,
            String targetTable) throws RemoteDataTransferException {
      DataTransferInput dataTransferInput = DataTransferUtil.prepareDataTransferInput(sourceDataSource,
               targetDataSource, targetInsertQuery, sourceSelectQuery, targetCreateQuery, targetRollBackQuery,
               targetTable);
      return transferRemoteData(dataTransferInput);
   }

   private DataTransferInput prepareRetryDataTransferInput (DataTransferStatus dataTransferStatus) {
      // this will prepare datatranferInput from dataTranferstatus by checking
      // the status of each of the query inside.
      // It will include only those queries which has failed
      DataTransferInput dataTransferInput = new DataTransferInput();
      List<DataTransferQuery> dataTransferQueries = new ArrayList<DataTransferQuery>();
      for (int count = 0; count < dataTransferStatus.getDataTransferQueryStatus().size(); count++) {
         DataTransferQueryStatus dataTransferQueryStatus = dataTransferStatus.getDataTransferQueryStatus().get(count);
         if (dataTransferQueryStatus.getQueryExecutionStatus().getValue() == SuccessFailureType.FAILURE.getValue()) {
            DataTransferQuery remoteQuery = new DataTransferQuery();
            remoteQuery = dataTransferStatus.getDataTransferInput().getDataTransferQueries().get(count);
            dataTransferQueries.add(remoteQuery);
         }
      }
      dataTransferInput.setTargetDataSource(dataTransferStatus.getDataTransferInput().getTargetDataSource());
      dataTransferInput.setSourceDataSource(dataTransferStatus.getDataTransferInput().getSourceDataSource());
      dataTransferInput.setDataTransferQueries(dataTransferQueries);
      return dataTransferInput;
   }

   private boolean checkETLExecutionStatus (DataTransferStatus dataTransferStatus) {
      boolean status = true;
      for (DataTransferQueryStatus remoteQueryStatus : dataTransferStatus.getDataTransferQueryStatus()) {
         if (SuccessFailureType.FAILURE.equals(remoteQueryStatus.getQueryExecutionStatus())) {
            status = false;
            break;
         }
      }
      return status;
   }

   private void retryRemoteDataTransfer (DataTransferStatus dataTransferStatus, int retryCount)
            throws RemoteDataTransferException, InterruptedException {
      // retry the data transfer
      int numAttempts = 1;
      do {
         // wait for some time before retry
         Thread.sleep(RETRY_WAIT_TIME);
         // prepare retry data transfer input depends on which all the
         // queries has failed
         DataTransferInput retryDataTransferInput = prepareRetryDataTransferInput(dataTransferStatus);
         // transfer the remote data and check the status again, if failed
         // then again retry it with increased
         // numAttempts.
         DataTransferStatus retryDataTransferStatus = performRemoteDataTransfer(retryDataTransferInput);
         if (checkETLExecutionStatus(retryDataTransferStatus)) {
            break;
         }
         numAttempts++;
      } while (numAttempts < retryCount);
   }

   private DataTransferStatus performRemoteDataTransfer (DataTransferInput dataTransferInput)
            throws RemoteDataTransferException {

      // first create the local tables
      remoteDataTransferHelper.createTargetTable(dataTransferInput);
      List<DataTransferQuery> dataTransferQueries = dataTransferInput.getDataTransferQueries();
      Iterator<DataTransferQuery> dataTransferQueryIterator = dataTransferQueries.iterator();
      DataTransferQuery dataTransferQuery = null;
      List<DataTransferQueryStatus> dataTransferQueryResults = new ArrayList<DataTransferQueryStatus>();
      DataTransferQueryStatus dataTransferQueryStatus = null;

      /*
       * Execute the remote queries This can be done sequentially as in a loop shown below, or in parallel using an
       * asynchronous mechanism
       */
      while (dataTransferQueryIterator.hasNext()) {

         dataTransferQuery = dataTransferQueryIterator.next();
         dataTransferQueryStatus = new DataTransferQueryStatus();
         dataTransferQueryStatus.setQueryExecutionStatus(SuccessFailureType.SUCCESS); // success
         dataTransferQueryStatus.setQueryId(dataTransferQuery.getId());

         // prepare an ETL input object
         ETLInput etlInput = remoteDataTransferHelper.prepareETLInputFromRemoteQuery(dataTransferQuery,
                  dataTransferInput);

         try {
            dataTransferQueryStatus = etlService.executeETLProcess(etlInput);
         } catch (ETLException e) {
            dataTransferQueryStatus.setErrorCode(e.getCode());
            dataTransferQueryStatus.setErrorMsg(e.getMessage());
            dataTransferQueryStatus.setQueryExecutionStatus(SuccessFailureType.FAILURE); // failure
            dataTransferQueryStatus.setQueryRollbackStatus(remoteDataTransferHelper.rollBackTransfer(dataTransferInput
                     .getTargetDataSource(), dataTransferQuery.getTargetRollbackStatement()));
         }

         dataTransferQueryResults.add(dataTransferQueryStatus);
      }

      DataTransferStatus dataTransferStatus = new DataTransferStatus();
      dataTransferStatus.setDataTransferQueryStatus(dataTransferQueryResults);
      dataTransferStatus.setDataTransferInput(dataTransferInput);
      return dataTransferStatus;
   }

   public void setEtlService (IETLService etlService) {
      this.etlService = etlService;
   }

   public RemoteDataTransferHelper getRemoteDataTransferHelper () {
      return remoteDataTransferHelper;
   }

   public void setRemoteDataTransferHelper (RemoteDataTransferHelper remoteDataTransferHelper) {
      this.remoteDataTransferHelper = remoteDataTransferHelper;
   }

}
