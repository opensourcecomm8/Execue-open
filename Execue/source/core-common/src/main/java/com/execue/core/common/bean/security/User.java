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
import java.util.Date;
import java.util.Set;

import com.execue.core.common.bean.SecurityGroupType;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.type.CheckType;

public class User implements Serializable {

   private Long                id;
   private String              username;
   private String              password;
   private String              salt;
   private String              firstName;
   private String              lastName;
   private String              fullName;
   private String              emailId;
   private UserStatus          status;
   private int                 numberOfFailedLoginAttempts;
   private Date                lastLoginDate;
   private boolean             changePassword;
   private Date                createdDate;
   private Date                modifiedDate;
   private Set<SecurityGroups> groups;
   private Set<Application>    applications;
   private String              encryptedKey;
   private SecurityGroupType   securityGroupType;
   private CheckType           isPublisher;
   private String              address1;
   private String              address2;
   private String              city;
   private String              state;
   private String              zip;
   private String              Country;
   private Set<DataSource>     dataSources;

   public String getUsername () {
      return username;
   }

   public void setUsername (String username) {
      this.username = username;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getPassword () {
      return password;
   }

   public void setPassword (String password) {
      this.password = password;
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

   public String getEmailId () {
      return emailId;
   }

   public void setEmailId (String emailId) {
      this.emailId = emailId;
   }

   public Date getLastLoginDate () {
      return lastLoginDate;
   }

   public void setLastLoginDate (Date lastLoginDate) {
      this.lastLoginDate = lastLoginDate;
   }

   public boolean isChangePassword () {
      return changePassword;
   }

   public void setChangePassword (boolean changePassword) {
      this.changePassword = changePassword;
   }

   public int getNumberOfFailedLoginAttempts () {
      return numberOfFailedLoginAttempts;
   }

   public void setNumberOfFailedLoginAttempts (int numberOfFailedLoginAttempts) {
      this.numberOfFailedLoginAttempts = numberOfFailedLoginAttempts;
   }

   public Set<SecurityGroups> getGroups () {
      return groups;
   }

   public void setGroups (Set<SecurityGroups> groups) {
      this.groups = groups;
   }

   public UserStatus getStatus () {
      return status;
   }

   public void setStatus (UserStatus status) {
      this.status = status;
   }

   public String getEncryptedKey () {
      return encryptedKey;
   }

   public void setEncryptedKey (String encryptedKey) {
      this.encryptedKey = encryptedKey;
   }

   public Set<Application> getApplications () {
      return applications;
   }

   public void setApplications (Set<Application> applications) {
      this.applications = applications;
   }

   /**
    * @return the securityGroupType
    */
   public SecurityGroupType getSecurityGroupType () {
      return securityGroupType;
   }

   /**
    * @param securityGroupType
    *           the securityGroupType to set
    */
   public void setSecurityGroupType (SecurityGroupType securityGroupType) {
      this.securityGroupType = securityGroupType;
   }

   public Date getCreatedDate () {
      return createdDate;
   }

   public void setCreatedDate (Date createdDate) {
      this.createdDate = createdDate;
   }

   public Date getModifiedDate () {
      return modifiedDate;
   }

   public void setModifiedDate (Date modifiedDate) {
      this.modifiedDate = modifiedDate;
   }

   /**
    * @return the isPublisher
    */
   public CheckType getIsPublisher () {
      return isPublisher;
   }

   /**
    * @param isPublisher
    *           the isPublisher to set
    */
   public void setIsPublisher (CheckType isPublisher) {
      this.isPublisher = isPublisher;
   }

   /**
    * @return the address1
    */
   public String getAddress1 () {
      return address1;
   }

   /**
    * @param address1
    *           the address1 to set
    */
   public void setAddress1 (String address1) {
      this.address1 = address1;
   }

   /**
    * @return the address2
    */
   public String getAddress2 () {
      return address2;
   }

   /**
    * @param address2
    *           the address2 to set
    */
   public void setAddress2 (String address2) {
      this.address2 = address2;
   }

   /**
    * @return the city
    */
   public String getCity () {
      return city;
   }

   /**
    * @param city
    *           the city to set
    */
   public void setCity (String city) {
      this.city = city;
   }

   /**
    * @return the zip
    */
   public String getZip () {
      return zip;
   }

   /**
    * @param zip
    *           the zip to set
    */
   public void setZip (String zip) {
      this.zip = zip;
   }

   /**
    * @return the country
    */
   public String getCountry () {
      return Country;
   }

   /**
    * @param country
    *           the country to set
    */
   public void setCountry (String country) {
      Country = country;
   }

   /**
    * @return the state
    */
   public String getState () {
      return state;
   }

   /**
    * @param state
    *           the state to set
    */
   public void setState (String state) {
      this.state = state;
   }

   public Set<DataSource> getDataSources () {
      return dataSources;
   }

   public void setDataSources (Set<DataSource> dataSources) {
      this.dataSources = dataSources;
   }

   /**
    * @return the fullName
    */
   public String getFullName () {
      return fullName;
   }

   /**
    * @param fullName
    *           the fullName to set
    */
   public void setFullName (String fullName) {
      this.fullName = fullName;
   }

   /**
    * 
    * @return salt value
    */
   
   public String getSalt () {
      return salt;
   }

   /**
    * 
    * @param salt
    *          the slat value to set
    */
   
   public void setSalt (String salt) {
      this.salt = salt;
   }

}
