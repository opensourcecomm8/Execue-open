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


package com.execue.util.mail.bean;

import java.util.List;

/**
 * This bean represents the entire mail entity information which is used to send a mail
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/08/09
 */
public class MailEntity {

   private String       hostName;
   private String       userName;
   private String       password;
   private List<String> recipients;
   private String       subject;
   private String       message;
   private String       from;

   public List<String> getRecipients () {
      return recipients;
   }

   public void setRecipients (List<String> recipients) {
      this.recipients = recipients;
   }

   public String getSubject () {
      return subject;
   }

   public void setSubject (String subject) {
      this.subject = subject;
   }

   public String getMessage () {
      return message;
   }

   public void setMessage (String message) {
      this.message = message;
   }

   public String getFrom () {
      return from;
   }

   public void setFrom (String from) {
      this.from = from;
   }

   public String getHostName () {
      return hostName;
   }

   public void setHostName (String hostName) {
      this.hostName = hostName;
   }

   public String getUserName () {
      return userName;
   }

   public void setUserName (String userName) {
      this.userName = userName;
   }

   public String getPassword () {
      return password;
   }

   public void setPassword (String password) {
      this.password = password;
   }
}
