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


package com.execue.handler.bean;

import com.execue.core.common.bean.security.UserRequestType;
import com.execue.core.common.type.CheckType;

/**
 * This UI object represents UserRequest,DemoUserRequest and feedback
 * 
 * @author jitendra
 */

public class UIUserRequest {

   private Long            id;
   private Long            userId;
   private String          firstName;
   private String          lastName;
   private String          userFullName;
   private String          jobTitle;
   private String          organization;
   private String          region;
   private CheckType       updateNotification = CheckType.NO;
   private String          subject;
   private String          notes;
   private String          emailId;
   private String          contactPhoneNum;
   private UserRequestType userRequestType;
   private String          adminComment;
   private CheckType       acceptRejectRequest;              // Y-Accept,N-Reject

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
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

   public UserRequestType getUserRequestType () {
      return userRequestType;
   }

   public void setUserRequestType (UserRequestType userRequestType) {
      this.userRequestType = userRequestType;
   }

   public String getAdminComment () {
      return adminComment;
   }

   public void setAdminComment (String adminComment) {
      this.adminComment = adminComment;
   }

   public CheckType getAcceptRejectRequest () {
      return acceptRejectRequest;
   }

   public void setAcceptRejectRequest (CheckType acceptRejectRequest) {
      this.acceptRejectRequest = acceptRejectRequest;
   }

   public CheckType getUpdateNotification () {
      return updateNotification;
   }

   public void setUpdateNotification (CheckType updateNotification) {
      this.updateNotification = updateNotification;
   }

   public String getUserFullName () {
      return userFullName;
   }

   public void setUserFullName (String userFullName) {
      this.userFullName = userFullName;
   }

}
