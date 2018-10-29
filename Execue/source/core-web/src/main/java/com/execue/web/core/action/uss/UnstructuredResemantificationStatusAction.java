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


package com.execue.web.core.action.uss;

import org.apache.log4j.Logger;

import com.execue.core.common.type.MessageStatusType;
import com.execue.handler.reports.IReportsHandler;
import com.opensymphony.xwork2.ActionSupport;

public class UnstructuredResemantificationStatusAction extends ActionSupport {

   private static final long serialVersionUID = 1L;
   private Long              messageId;
   private IReportsHandler   reportHandler;
   private MessageStatusType messageStatus;

   private Logger            logger           = Logger.getLogger(UnstructuredResemantificationStatusAction.class);

   public IReportsHandler getReportHandler () {
      return reportHandler;
   }

   public void setReportHandler (IReportsHandler reportHandler) {
      this.reportHandler = reportHandler;
   }

   public String checkStatus () {
      if (logger.isDebugEnabled()) {
         logger.debug("MessageId......" + messageId);

      }
      try {
         String status = reportHandler.getReportStatus(messageId);
         setMessageStatus(MessageStatusType.getMessageStatusType(status));
      } catch (Exception e) {
         e.printStackTrace();
         logger.error("Error in the ReportStatusAction : " + e.getCause());
      }
      return SUCCESS;
   }

   public long getMessageId () {
      return messageId;
   }

   public void setMessageId (long messageId) {
      this.messageId = messageId;
   }

   /**
    * @return the messageStatus
    */
   public MessageStatusType getMessageStatus () {
      return messageStatus;
   }

   /**
    * @param messageStatus the messageStatus to set
    */
   public void setMessageStatus (MessageStatusType messageStatus) {
      this.messageStatus = messageStatus;
   }

}
