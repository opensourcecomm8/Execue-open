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


package com.execue.qdata.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Message;
import com.execue.core.common.type.MessageStatusType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.qdata.dataaccess.IMessageDataAccessManager;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IMessageService;

public class MessageServiceImpl implements IMessageService {

   private IMessageDataAccessManager messageDataAccessManager;
   private ICoreConfigurationService coreConfigurationService;

   private static final Logger       logger = Logger.getLogger(MessageServiceImpl.class);

   public MessageStatusType getMessageStatus (long messageId) throws QueryDataException {
      if (messageId == 0) {
         return MessageStatusType.PROCESSING;
      }
      Message message = getMessageDataAccessManager().getMessage(messageId);
      return MessageStatusType.getMessageStatusType(message.getCurrentStatus());
   }

   public long createMessage (long transId) throws QueryDataException {
      Message m = new Message();
      m.setTransactionId(transId);
      m.setCurrentStatus(MessageStatusType.CREATED.getValue());
      m.setDateCreated(Calendar.getInstance().getTime());
      getMessageDataAccessManager().createMessage(m);
      return m.getId();
   }

   public void updateMessage (long messageId, MessageStatusType status) throws QueryDataException {
      Message m = getMessageDataAccessManager().getMessage(messageId);
      m.setCurrentStatus(status.getValue());
      m.setDateModified(Calendar.getInstance().getTime());
      getMessageDataAccessManager().updateMessage(m);
   }

   public void cleanOldMessages () throws QueryDataException {
      int messagesCleanupHours = getCoreConfigurationService().getMessageCleanupHours();
      Date messagesCleanupDate = getCleanupDate(messagesCleanupHours);
      if (messagesCleanupDate != null) {
         getMessageDataAccessManager().cleanOldMessages(messagesCleanupDate);
      } else {
         logger.error("Cannot clean messages as the modified date was not present");
      }
   }

   private Date getCleanupDate (int messageCleanupHours) throws QueryDataException {
      Calendar cal = Calendar.getInstance();
      Date maxMessageModifiedDate = getMessageDataAccessManager().getMaxMessageModifiedDate();
      if (maxMessageModifiedDate != null) {
         cal.setTime(maxMessageModifiedDate);
         cal.add(Calendar.HOUR, -messageCleanupHours);
         return cal.getTime();
      } else {
         logger.error("Null Message max modified date ");
         return null;
      }
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   /**
    * @return the messageDataAccessManager
    */
   public IMessageDataAccessManager getMessageDataAccessManager () {
      return messageDataAccessManager;
   }

   /**
    * @param messageDataAccessManager the messageDataAccessManager to set
    */
   public void setMessageDataAccessManager (IMessageDataAccessManager messageDataAccessManager) {
      this.messageDataAccessManager = messageDataAccessManager;
   }

}
