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

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.execue.qdata.exception.QueryDataException;
import com.execue.message.bean.MessageInfo;
import com.execue.message.bean.MessageInput;
import com.execue.message.exception.MessageException;
import com.execue.qdata.service.IMessageService;

public class JMSMessageSender implements IMessage {

   private JmsTemplate       jmsTemplate;
   protected IMessageService messageService;

   public void setMessageService (IMessageService messageService) {
      this.messageService = messageService;
   }

   public JmsTemplate getJmsTemplate () {
      return jmsTemplate;
   }

   public void setJmsTemplate (JmsTemplate jmsTemplate) {
      this.jmsTemplate = jmsTemplate;
   }

   public MessageInfo processMessage (final MessageInput input) throws MessageException {
      MessageInfo message = null;
      try {
         long messageId = messageService.createMessage(input.getTransactionId());
         message = new MessageInfo();
         message.setId(messageId);
         jmsTemplate.send(new MessageCreator() {

            public Message createMessage (Session session) throws JMSException {

               ObjectMessage objectMessage = session.createObjectMessage();

               objectMessage.setObject((Serializable) input.getObject());

               return objectMessage;
            }
         });

         // do post processing
      } catch (QueryDataException queryDataException) {
         throw new MessageException(queryDataException.getCode(), queryDataException);
      }
      return message;
   }

}
