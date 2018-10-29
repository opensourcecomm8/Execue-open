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


package com.execue.web.core.action.qi;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.AssetResult;
import com.execue.core.common.type.MessageStatusType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.handler.reports.IReportsHandler;
import com.opensymphony.xwork2.ActionSupport;

public class ReportStatusAction extends ActionSupport {

   private long                queryId;
   private long                messageId;
   private long                assetId;
   private long                businessQueryId;
   private AssetResult         result;
   private IReportsHandler     reportHandler;
   private static final String ERR    = "error.";
   private Logger              logger = Logger.getLogger(ReportStatusAction.class);

   public IReportsHandler getReportHandler () {
      return reportHandler;
   }

   public void setReportHandler (IReportsHandler reportHandler) {
      this.reportHandler = reportHandler;
   }

   // TODO: JVK exception handling - handle exceptions in Handler and set the error field of AssetResult
   public String checkStatus () {
      if (logger.isDebugEnabled()) {
         logger.debug("QueryId......" + queryId);
         logger.debug("MessageId......" + messageId);
         logger.debug("AssetId......" + assetId);
         logger.debug("BusinessQueryId......" + businessQueryId);
      }
      try {
         String status = reportHandler.getReportStatus(messageId);
         if (MessageStatusType.COMPLETED.getValue().equalsIgnoreCase(status)) {
            logger.debug("COMPLETED.......");
            AssetResult assetResult = new AssetResult();
            assetResult.setAssetId(assetId);
            assetResult.setMessageId(messageId);
            assetResult.setBusinessQueryId(businessQueryId);
            result = reportHandler.getReportSummaryDetails(assetResult, queryId);
            if (result != null) {
               result.setReportStatus(status);
            }
         } else {
            logger.debug("PROCESSING.......");
            result = new AssetResult();
            result.setReportStatus(status);
         }
         // TODO: -JVK- move this delay into the else block
         // Introduce delay before retrying
         // Thread.sleep(2000);
      } catch (Exception e) {
         e.printStackTrace();
         logger.error("Error in the ReportStatusAction : " + e.getCause());
         addActionError(getText(ERR + Integer.toString(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE)));
      }
      return SUCCESS;
   }

   public long getQueryId () {
      return queryId;
   }

   public void setQueryId (long queryId) {
      this.queryId = queryId;
   }

   public long getMessageId () {
      return messageId;
   }

   public void setMessageId (long messageId) {
      this.messageId = messageId;
   }

   public long getAssetId () {
      return assetId;
   }

   public void setAssetId (long assetId) {
      this.assetId = assetId;
   }

   public AssetResult getResult () {
      return result;
   }

   public void setResult (AssetResult result) {
      this.result = result;
   }

   public long getBusinessQueryId () {
      return businessQueryId;
   }

   public void setBusinessQueryId (long businessQueryId) {
      this.businessQueryId = businessQueryId;
   }
}
