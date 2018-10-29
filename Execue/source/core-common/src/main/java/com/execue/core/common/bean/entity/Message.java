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
import java.util.HashSet;
import java.util.Set;

/**
 * @author kaliki
 * @since 4.0
 */
public class Message implements java.io.Serializable {

   private Long                id;
   private Long                transactionId;
   private String              type;
   private String              currentStatus;
   private Date                dateCreated;
   private Date                dateModified;
   private Set<MessageHistory> messageHistories = new HashSet<MessageHistory>(0);

   public Message () {
   }

   public Message (Long transactionId, String type, String currentStatus, Date dateCreated, Date dateModified,
            Set<MessageHistory> messageHistories) {
      this.transactionId = transactionId;
      this.type = type;
      this.currentStatus = currentStatus;
      this.dateCreated = dateCreated;
      this.dateModified = dateModified;
      this.messageHistories = messageHistories;
   }

   public Long getId () {
      return this.id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getTransactionId () {
      return this.transactionId;
   }

   public void setTransactionId (Long transactionId) {
      this.transactionId = transactionId;
   }

   public String getType () {
      return this.type;
   }

   public void setType (String type) {
      this.type = type;
   }

   public String getCurrentStatus () {
      return this.currentStatus;
   }

   public void setCurrentStatus (String currentStatus) {
      this.currentStatus = currentStatus;
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

   public Set<MessageHistory> getMessageHistories () {
      return this.messageHistories;
   }

   public void setMessageHistories (Set<MessageHistory> messageHistories) {
      this.messageHistories = messageHistories;
   }

}
