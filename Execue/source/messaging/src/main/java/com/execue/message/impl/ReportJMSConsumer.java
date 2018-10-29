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


package com.execue.message.impl;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;

public class ReportJMSConsumer implements MessageListener {

   private JmsTemplate         template;
   private String              myId   = "foo";
   private Connection          connection;
   private Session             session;
   private MessageConsumer     consumer;
   private static final Logger logger = Logger.getLogger(ReportJMSConsumer.class);

   public JmsTemplate getTemplate () {
      return template;
   }

   public void setTemplate (JmsTemplate template) {
      this.template = template;
   }

   public void start () throws JMSException {
      String selector = "next = '" + myId + "'";

      try {
         ConnectionFactory factory = template.getConnectionFactory();
         connection = factory.createConnection();

         // we might be a reusable connection in spring
         // so lets only set the client ID once if its not set
         synchronized (connection) {
            if (connection.getClientID() == null) {
               connection.setClientID(myId);
            }
         }

         connection.start();

         session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
         consumer = session.createConsumer(template.getDefaultDestination(), selector, false);
         consumer.setMessageListener(this);
      } catch (JMSException ex) {
         ex.printStackTrace();
         throw ex;
      }
   }

   public void stop () throws JMSException {
      if (consumer != null) {
         consumer.close();
      }
      if (session != null) {
         session.close();
      }
      if (connection != null) {
         connection.close();
      }
   }

   public void onMessage (Message arg0) {
      logger.debug("Message reached");

   }

}
