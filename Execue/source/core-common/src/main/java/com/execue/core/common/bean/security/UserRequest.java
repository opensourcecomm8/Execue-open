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


package com.execue.core.common.bean.security;

import java.io.Serializable;

import com.execue.core.common.type.CheckType;

public class UserRequest implements Serializable {

   private Long            id;
   private Long            userId;
   private String          firstName;
   private String          lastName;
   private String          jobTitle;
   private String          organization;
   private String          region;
   private CheckType       updateNotification;
   private String          subject;
   private String          notes;
   private String          emailId;
   private String          contactPhoneNum;
   private UserRequestType userRequestType;
   private String          comment;
   private CheckType       acceptRejectRequest; // Y-Accept,N-Reject

   public UserRequestType getUserRequestType () {
      return userRequestType;
   }

   public void setUserRequestType (UserRequestType userRequestType) {
      this.userRequestType = userRequestType;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getSubject () {
      return subject;
   }

   public void setSubject (String subject) {
      this.subject = subject;
   }

   public String getNotes () {
      return notes;
   }

   public void setNotes (String notes) {
      this.notes = notes;
   }

   public String getEmailId () {
      return emailId;
   }

   public void setEmailId (String emailId) {
      this.emailId = emailId;
   }

   public String getContactPhoneNum () {
      return contactPhoneNum;
   }

   public void setContactPhoneNum (String contactPhoneNum) {
      this.contactPhoneNum = contactPhoneNum;
   }

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }

   public String getComment () {
      return comment;
   }

   public void setComment (String comment) {
      this.comment = comment;
   }

   public CheckType getAcceptRejectRequest () {
      return acceptRejectRequest;
   }

   public void setAcceptRejectRequest (CheckType acceptRejectRequest) {
      this.acceptRejectRequest = acceptRejectRequest;
   }

   public String getFirstName () {
      return firstName;
   }

   public void setFirstName (String firstName) {
      this.firstName = firstName;
   }

   public String getLastName () {
      return lastName;
   }

   public void setLastName (String lastName) {
      this.lastName = lastName;
   }

   public String getJobTitle () {
      return jobTitle;
   }

   public void setJobTitle (String jobTitle) {
      this.jobTitle = jobTitle;
   }

   public String getOrganization () {
      return organization;
   }

   public void setOrganization (String organization) {
      this.organization = organization;
   }

   public String getRegion () {
      return region;
   }

   public void setRegion (String region) {
      this.region = region;
   }

   public CheckType getUpdateNotification () {
      return updateNotification;
   }

   public void setUpdateNotification (CheckType updateNotification) {
      this.updateNotification = updateNotification;
   }

}
