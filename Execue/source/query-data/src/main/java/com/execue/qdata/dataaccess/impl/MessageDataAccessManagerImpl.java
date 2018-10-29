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


package com.execue.qdata.dataaccess.impl;

import java.util.Date;

import com.execue.core.common.bean.entity.Message;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.qdata.dao.IMessageDAO;
import com.execue.qdata.dataaccess.IMessageDataAccessManager;
import com.execue.qdata.exception.QueryDataException;

public class MessageDataAccessManagerImpl implements IMessageDataAccessManager {

   private IMessageDAO messageDAO;

   public void cleanOldMessages (Date messagesCleanupDate) throws QueryDataException {
      try {
         getMessageDAO().cleanOldMessages(messagesCleanupDate);
      } catch (DataAccessException e) {
         throw new QueryDataException(e.getCode(), e);
      }
   }

   public void createMessage (Message message) throws QueryDataException {
      try {
         getMessageDAO().create(message);
      } catch (DataAccessException e) {
         throw new QueryDataException(e.getCode(), e);
      }
   }

   public Message getMessage (long messageId) throws QueryDataException {
      try {
         return getMessageDAO().getById(messageId, Message.class);
      } catch (DataAccessException e) {
         throw new QueryDataException(e.getCode(), e);
      }
   }

   public void updateMessage (Message message) throws QueryDataException {
      try {
         getMessageDAO().update(message);
      } catch (DataAccessException e) {
         throw new QueryDataException(e.getCode(), e);
      }
   }

   public Date getMaxMessageModifiedDate () throws QueryDataException {
      try {
         return getMessageDAO().getMaxMessageModifiedDate();
      } catch (DataAccessException e) {
         throw new QueryDataException(e.getCode(), e);
      }
   }

   /**
    * @return the messageDAO
    */
   public IMessageDAO getMessageDAO () {
      return messageDAO;
   }

   /**
    * @param messageDAO the messageDAO to set
    */
   public void setMessageDAO (IMessageDAO messageDAO) {
      this.messageDAO = messageDAO;
   }

}
