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
 * @author Nitesh
 *
 */
public class SforceUserProfileInfo {
   
   private String id;
   private String name;
   private String userLicenseId;
   private String userType;
   private String permissionsViewAllData;
   private String permissionsManageSelfService;
   private String permissionsApiEnabled;
   
   /**
    * @return the id
    */
   public String getId () {
      return id;
   }
   
   /**
    * @param id the id to set
    */
   public void setId (String id) {
      this.id = id;
   }
   
   /**
    * @return the name
    */
   public String getName () {
      return name;
   }
   
   /**
    * @param name the name to set
    */
   public void setName (String name) {
      this.name = name;
   }
   
   /**
    * @return the userLicenseId
    */
   public String getUserLicenseId () {
      return userLicenseId;
   }
   
   /**
    * @param userLicenseId the userLicenseId to set
    */
   public void setUserLicenseId (String userLicenseId) {
      this.userLicenseId = userLicenseId;
   }
   
   /**
    * @return the userType
    */
   public String getUserType () {
      return userType;
   }
   
   /**
    * @param userType the userType to set
    */
   public void setUserType (String userType) {
      this.userType = userType;
   }
   
   /**
    * @return the permissionsViewAllData
    */
   public String getPermissionsViewAllData () {
      return permissionsViewAllData;
   }
   
   /**
    * @param permissionsViewAllData the permissionsViewAllData to set
    */
   public void setPermissionsViewAllData (String permissionsViewAllData) {
      this.permissionsViewAllData = permissionsViewAllData;
   }
   
   /**
    * @return the permissionsManageSelfService
    */
   public String getPermissionsManageSelfService () {
      return permissionsManageSelfService;
   }
   
   /**
    * @param permissionsManageSelfService the permissionsManageSelfService to set
    */
   public void setPermissionsManageSelfService (String permissionsManageSelfService) {
      this.permissionsManageSelfService = permissionsManageSelfService;
   }
   
   /**
    * @return the permissionsApiEnabled
    */
   public String getPermissionsApiEnabled () {
      return permissionsApiEnabled;
   }
   
   /**
    * @param permissionsApiEnabled the permissionsApiEnabled to set
    */
   public void setPermissionsApiEnabled (String permissionsApiEnabled) {
      this.permissionsApiEnabled = permissionsApiEnabled;
   }
   
}
