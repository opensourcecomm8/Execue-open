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


package com.execue.handler.reports.impl;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.AssetResult;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.driver.qi.IQueryInterfaceDriver;
import com.execue.handler.reports.IReportsHandler;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IMessageService;

public class ReportsHandlerImpl implements IReportsHandler {

   private IMessageService       messageService;
   private IQueryInterfaceDriver queryInterfaceDriver;

   private static final Logger   logger = Logger.getLogger(ReportsHandlerImpl.class);

   public String getReportStatus (Long messageId) throws HandlerException {
      try {
         return messageService.getMessageStatus(messageId).getValue();
      } catch (QueryDataException queryDataException) {
         logger.error("MessageException in ReportsHandlerImpl", queryDataException);
         logger.error("Actual Error : [" + queryDataException.getCode() + "] " + queryDataException.getMessage());
         logger.error("Cause : " + queryDataException.getCause());
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, "Error from MessageService",
                  queryDataException);
      }
   }

   public AssetResult getReportSummaryDetails (AssetResult assetResult, long queryId) throws HandlerException {
      try {
         assetResult = queryInterfaceDriver.getPopulatedAssetResult(assetResult, queryId, false);
      } catch (ExeCueException exeCueException) {
         logger.error("ExeCueException in ReportsHandlerImpl", exeCueException);
         logger.error("Actual Error : [" + exeCueException.getCode() + "] " + exeCueException.getMessage());
         logger.error("Cause : " + exeCueException.getCause());
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, "Error from Driver", exeCueException);
      }
      return assetResult;
   }

   public IMessageService getMessageService () {
      return messageService;
   }

   public void setMessageService (IMessageService messageService) {
      this.messageService = messageService;
   }

   public IQueryInterfaceDriver getQueryInterfaceDriver () {
      return queryInterfaceDriver;
   }

   public void setQueryInterfaceDriver (IQueryInterfaceDriver queryInterfaceDriver) {
      this.queryInterfaceDriver = queryInterfaceDriver;
   }
}
