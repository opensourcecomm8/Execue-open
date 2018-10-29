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


package com.execue.das.datatransfer.etl.service;

import com.execue.core.common.bean.entity.DataSource;
import com.execue.das.datatransfer.etl.bean.DataTransferInput;
import com.execue.das.datatransfer.etl.bean.DataTransferStatus;
import com.execue.das.datatransfer.etl.exception.RemoteDataTransferException;

/**
 * This service performs the data transfer and it has retry mechanism also.
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/01/2011
 */
public interface IRemoteDataTransferService {

   /**
    * This method transfers data using ETL process
    * 
    * @param dataTransferInput
    * @return dataTransferStatus
    * @throws RemoteDataTransferException
    */
   public DataTransferStatus transferRemoteData (DataTransferInput dataTransferInput)
            throws RemoteDataTransferException;

   public DataTransferStatus transferRemoteData (DataSource localDataSource, DataSource targetDataSource,
            String localInsertQuery, String remoteSelectQuery, String localCreateQuery, String localTable)
            throws RemoteDataTransferException;

   public DataTransferStatus transferRemoteData (DataSource localDataSource, DataSource targetDataSource,
            String localInsertQuery, String remoteSelectQuery, String localCreateQuery, String rollBackQuery,
            String localTable) throws RemoteDataTransferException;
}