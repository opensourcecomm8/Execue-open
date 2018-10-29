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


package com.execue.message;

import org.apache.log4j.Logger;

import com.execue.core.common.type.MessageStatusType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.service.ExecueSystemCachedThreadPoolManager;
import com.execue.message.bean.MessageInfo;
import com.execue.message.bean.MessageInput;
import com.execue.message.exception.MessageException;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IMessageService;

/**
 * @author kaliki
 * @since 4.0
 */
public abstract class AbstractThreadMessage implements IMessage {

   private static final Logger                   logger = Logger.getLogger(AbstractThreadMessage.class);

   protected IMessageService                     messageService;
   protected ICoreConfigurationService           coreConfigurationService;
   protected ExecueSystemCachedThreadPoolManager execueSystemCachedThreadPoolManager;

   public IMessageService getMessageService () {
      return messageService;
   }

   public void setMessageService (IMessageService messageService) {
      this.messageService = messageService;
   }

   public abstract void process (Object o) throws MessageException;

   public MessageInfo processMessage (final MessageInput input) throws MessageException {
      MessageInfo message = null;
      try {
         final long messageId = messageService.createMessage(input.getTransactionId());
         message = new MessageInfo();
         message.setId(messageId);
         message.setStatus(MessageStatusType.CREATED);

         if (getCoreConfigurationService().isThreadPoolForAggregationProcessingEnabled()) {
            execueSystemCachedThreadPoolManager.submitTask(new processMessageTask(messageId, input));
         } else {
            Thread thread = new Thread() {

               public void run () {
                  try {
                     messageService.updateMessage(messageId, MessageStatusType.PROCESSING);
                     process(input.getObject());
                     messageService.updateMessage(messageId, MessageStatusType.COMPLETED);
                  } catch (Error e) {
                     try {
                        e.printStackTrace();
                        logger.error("Error while processing message");
                        messageService.updateMessage(messageId, MessageStatusType.ERROR);
                     } catch (Exception ee) {
                        // TODO: handle exception
                        logger.error("Error while updating the message status to ERROR status");
                     }
                  } catch (Exception e) {
                     try {
                        e.printStackTrace();
                        logger.error("Error while processing message");
                        messageService.updateMessage(messageId, MessageStatusType.ERROR);
                     } catch (Exception ee) {
                        // TODO: handle exception
                        logger.error("Error while updating the message status to ERROR status");
                     }
                  }
               }
            };
            thread.start();
         }
         // set the current status before returning
         message.setStatus(messageService.getMessageStatus(messageId));
      } catch (QueryDataException queryDataException) {
         throw new MessageException(queryDataException.getCode(), queryDataException);
      }
      return message;
   }

   private class processMessageTask implements Runnable {

      private Long         messageId;
      private MessageInput messageInput;

      public void run () {
         try {
            messageService.updateMessage(messageId, MessageStatusType.PROCESSING);
            process(messageInput.getObject());
            messageService.updateMessage(messageId, MessageStatusType.COMPLETED);
         } catch (Error e) {
            try {
               e.printStackTrace();
               logger.error("Error while processing message");
               messageService.updateMessage(messageId, MessageStatusType.ERROR);
            } catch (Exception ee) {
               // TODO: handle exception
               logger.error("Error while updating the message status to ERROR status");
            }
         } catch (Exception e) {
            try {
               e.printStackTrace();
               logger.error("Error while processing message");
               messageService.updateMessage(messageId, MessageStatusType.ERROR);
            } catch (Exception ee) {
               // TODO: handle exception
               logger.error("Error while updating the message status to ERROR status");
            }
         }
      }

      public processMessageTask (Long messageId, MessageInput messageInput) {
         this.messageId = messageId;
         this.messageInput = messageInput;
      }

   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService
    *           the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   public ExecueSystemCachedThreadPoolManager getExecueSystemCachedThreadPoolManager () {
      return execueSystemCachedThreadPoolManager;
   }

   public void setExecueSystemCachedThreadPoolManager (
            ExecueSystemCachedThreadPoolManager execueSystemCachedThreadPoolManager) {
      this.execueSystemCachedThreadPoolManager = execueSystemCachedThreadPoolManager;
   }
}
