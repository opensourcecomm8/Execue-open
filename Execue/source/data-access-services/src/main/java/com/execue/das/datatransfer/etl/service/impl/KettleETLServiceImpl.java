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
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

import com.execue.das.datatransfer.etl.bean.ETLInput;
import com.execue.das.datatransfer.etl.bean.DataTransferQueryStatus;
import com.execue.das.datatransfer.etl.exception.ETLException;
import com.execue.das.datatransfer.etl.helper.KettleETLInputHelper;
import com.execue.das.datatransfer.etl.service.IETLService;
import com.execue.das.exception.DataAccessServicesExceptionCodes;

public class KettleETLServiceImpl implements IETLService {

   private static final Logger logger = Logger.getLogger(KettleETLServiceImpl.class);

   public DataTransferQueryStatus executeETLProcess (ETLInput etlInput) throws ETLException {
      DataTransferQueryStatus remoteQueryStatus = new DataTransferQueryStatus();
      try {
         if (logger.isDebugEnabled()) {
            logger.debug("Inside executeETLProcess method");
         }
         TransMeta transMeta = KettleETLInputHelper.prepareKettleInput(etlInput);
         String sql = transMeta.getSQLStatementsString();
         if (logger.isDebugEnabled()) {
            logger.debug("Query :: " + sql);
         }
         Database targetDatabase = new Database(transMeta.findDatabase("TARGET"));
         targetDatabase.connect();
         targetDatabase.execStatements(sql);
         Trans trans = new Trans(transMeta);
         trans.execute(null);
         trans.waitUntilFinished();
         remoteQueryStatus.setRecordCount(Integer.valueOf(String.valueOf(trans.getResult().getNrLinesWritten())).intValue());
      } catch (KettleException e) {

         e.printStackTrace();
         throw new ETLException(DataAccessServicesExceptionCodes.DEFAULT_ETL_KETTLE_EXCEPTION_CODE, "KettleException",
                  e);
      }
      return remoteQueryStatus;
   }

}
