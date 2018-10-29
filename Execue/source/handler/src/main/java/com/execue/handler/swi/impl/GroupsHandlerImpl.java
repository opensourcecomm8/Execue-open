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


package com.execue.handler.swi.impl;

import java.util.List;

import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.core.common.bean.security.SecurityRoles;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.handler.swi.IGroupsHandler;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IUserManagementService;

public class GroupsHandlerImpl implements IGroupsHandler {

   private IUserManagementService userManagementService;

   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   public void assignGroupRoles (SecurityGroups group, List<SecurityRoles> roles) throws HandlerException {
      try {
         userManagementService.assignGroupRoles(group, roles);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public Long createGroup (SecurityGroups group) throws HandlerException {
      try {
         return userManagementService.createGroup(group);
      } catch (SWIException e) {
         if (e.getCode() == ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS)
            throw new HandlerException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, e.getMessage());
         else {
            throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
         }
      }
   }

   public SecurityGroups getGroupById (Long id) throws HandlerException {
      try {
         return userManagementService.getGroupById(id);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<SecurityGroups> getGroups (String status) throws HandlerException {
      try {
         return userManagementService.getGroups(status);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updateGroup (SecurityGroups group) throws HandlerException {
      try {
         userManagementService.updateGroup(group);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<SecurityGroups> getAllGroups () throws HandlerException {
      try {
         return userManagementService.getAllGroups();
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void deleteGroup (SecurityGroups group) throws HandlerException {
      try {
         userManagementService.deleteGroup(group);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<SecurityGroups> getUserGroups (long userId) throws HandlerException {
      try {
         return userManagementService.getUserGroups(userId);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public SecurityGroups getGroupAndRoles (Long id) throws HandlerException {
      try {
         return userManagementService.getGroupWithRoles(id);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }
}