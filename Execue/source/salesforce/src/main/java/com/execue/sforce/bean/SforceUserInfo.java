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


/**
 * 
 */
package com.execue.sforce.bean;


/**
 * This class stores the salesforce user information
 * 
 * @author Nitesh
 * 
 *
 */
public class SforceUserInfo {
   
   private String organizationId;
   private String organizationName;
   private String profileId;
   private String userEmail;
   private String userId;
   private String userName;
   
   /**
    * @return the organizationId
    */
   public String getOrganizationId () {
      return organizationId;
   }
   
   /**
    * @param organizationId the organizationId to set
    */
   public void setOrganizationId (String organizationId) {
      this.organizationId = organizationId;
   }
   
   /**
    * @return the organizationName
    */
   public String getOrganizationName () {
      return organizationName;
   }
   
   /**
    * @param organizationName the organizationName to set
    */
   public void setOrganizationName (String organizationName) {
      this.organizationName = organizationName;
   }
   
   /**
    * @return the profileId
    */
   public String getProfileId () {
      return profileId;
   }
   
   /**
    * @param profileId the profileId to set
    */
   public void setProfileId (String profileId) {
      this.profileId = profileId;
   }
   
   /**
    * @return the userEmail
    */
   public String getUserEmail () {
      return userEmail;
   }
   
   /**
    * @param userEmail the userEmail to set
    */
   public void setUserEmail (String userEmail) {
      this.userEmail = userEmail;
   }
   
   /**
    * @return the userId
    */
   public String getUserId () {
      return userId;
   }
   
   /**
    * @param userId the userId to set
    */
   public void setUserId (String userId) {
      this.userId = userId;
   }
   
   /**
    * @return the userName
    */
   public String getUserName () {
      return userName;
   }
   
   /**
    * @param userName the userName to set
    */
   public void setUserName (String userName) {
      this.userName = userName;
   }
}
