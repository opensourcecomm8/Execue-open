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


package com.execue.core.common.bean.entity;

import java.util.Date;

/**
 * @author kaliki
 * @since 4.0
 */
public class MessageHistory implements java.io.Serializable {

   private Long    id;
   private Message message;
   private String  messageType;
   private String  status;
   private Date    dateCreated;
   private Date    dateModified;

   public MessageHistory () {
   }

   public MessageHistory (Message message, String messageType, String status, Date dateCreated, Date dateModified) {
      this.message = message;
      this.messageType = messageType;
      this.status = status;
      this.dateCreated = dateCreated;
      this.dateModified = dateModified;
   }

   public Long getId () {
      return this.id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Message getMessage () {
      return this.message;
   }

   public void setMessage (Message message) {
      this.message = message;
   }

   public String getMessageType () {
      return this.messageType;
   }

   public void setMessageType (String messageType) {
      this.messageType = messageType;
   }

   public String getStatus () {
      return this.status;
   }

   public void setStatus (String status) {
      this.status = status;
   }

   public Date getDateCreated () {
      return this.dateCreated;
   }

   public void setDateCreated (Date dateCreated) {
      this.dateCreated = dateCreated;
   }

   public Date getDateModified () {
      return this.dateModified;
   }

   public void setDateModified (Date dateModified) {
      this.dateModified = dateModified;
   }

}
