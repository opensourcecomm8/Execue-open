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


package com.execue.web.core.action.audittrail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.execue.core.common.type.AuditLogType;
import com.execue.core.exception.HandlerException;
import com.execue.handler.audittrail.IAuditTrailServiceHandler;
import com.execue.handler.bean.UIAuditLogType;
import com.execue.handler.bean.UIUser;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Common action class for all user auditlog screens
 * @author Jitendra
 *
 */
public class AuditLogAction extends ActionSupport {

   private List<UIUser>              users;                   //User auto suggestion
   private List<UIAuditLogType>      auditLogTypes;           // Audit log type auto suggestion
   private String                    search;                  // search keyword
   private IAuditTrailServiceHandler auditTrailServiceHandler;

   public String showUsers () {
      try {
         users = getAuditTrailServiceHandler().getUsers(search);
      } catch (HandlerException e) {
         e.printStackTrace();
      }

      return SUCCESS;
   }

   public String showAuditLogType () {
      auditLogTypes = new ArrayList<UIAuditLogType>();
      List<AuditLogType> auditLogTypeList = Arrays.asList(AuditLogType.values());
      for (AuditLogType auditLogType : auditLogTypeList) {
         UIAuditLogType uiAuditLogType = new UIAuditLogType();
         uiAuditLogType.setId(auditLogType.getValue());
         uiAuditLogType.setDisplayName(prepareDisplayName(auditLogType));
         auditLogTypes.add(uiAuditLogType);
      }
      return SUCCESS;
   }

   private String prepareDisplayName (AuditLogType auditLogType) {
      String auditLogTypeDisplayName = "Login";
      switch (auditLogType) {
         case LOGOUT:
            auditLogTypeDisplayName = "Log out";
            break;
         case LOGIN_FAILURE:
            auditLogTypeDisplayName = "Login failure";
            break;
         case PASSWORD_RESET:
            auditLogTypeDisplayName = "Password reset";
            break;
      }
      return auditLogTypeDisplayName;
   }

   /**
    * @return the users
    */
   public List<UIUser> getUsers () {
      return users;
   }

   /**
    * @param users the users to set
    */
   public void setUsers (List<UIUser> users) {
      this.users = users;
   }

   /**
    * @return the auditLogTypes
    */
   public List<UIAuditLogType> getAuditLogTypes () {
      return auditLogTypes;
   }

   /**
    * @param auditLogTypes the auditLogTypes to set
    */
   public void setAuditLogTypes (List<UIAuditLogType> auditLogTypes) {
      this.auditLogTypes = auditLogTypes;
   }

   /**
    * @return the search
    */
   public String getSearch () {
      return search;
   }

   /**
    * @param search the search to set
    */
   public void setSearch (String search) {
      this.search = search;
   }

   /**
    * @return the auditTrailServiceHandler
    */
   public IAuditTrailServiceHandler getAuditTrailServiceHandler () {
      return auditTrailServiceHandler;
   }

   /**
    * @param auditTrailServiceHandler the auditTrailServiceHandler to set
    */
   public void setAuditTrailServiceHandler (IAuditTrailServiceHandler auditTrailServiceHandler) {
      this.auditTrailServiceHandler = auditTrailServiceHandler;
   }

}
