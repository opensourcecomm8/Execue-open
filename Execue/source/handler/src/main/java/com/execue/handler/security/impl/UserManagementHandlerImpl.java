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


package com.execue.handler.security.impl;

import java.util.List;

import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.core.common.bean.security.SecurityRoles;
import com.execue.core.common.bean.security.User;
import com.execue.core.exception.ExeCueException;
import com.execue.handler.security.IUserManagementHanlder;
import com.execue.swi.service.IUserManagementService;

public class UserManagementHandlerImpl implements IUserManagementHanlder {

   private IUserManagementService userManagementService;

   public boolean assignGroup (User user, List<SecurityGroups> groups) throws ExeCueException {
      return userManagementService.assignGroup(user, groups);
   }

   public void assignGroupRoles (SecurityGroups group, List<SecurityRoles> roles) throws ExeCueException {
      // TODO Auto-generated method stub
      userManagementService.assignGroupRoles(group, roles);

   }

   public boolean changePassword (String userid, String oldPassword, String newPassword) throws ExeCueException {
      // TODO Auto-generated method stub
      return false;
   }

   public Long createGroup (SecurityGroups group) throws ExeCueException {
      // TODO Auto-generated method stub
      return null;
   }

   public Long createRole (SecurityRoles role) throws ExeCueException {
      // TODO Auto-generated method stub
      return null;
   }

   public Long createUser (User user) throws ExeCueException {
      // TODO Auto-generated method stub
      return null;
   }

   public SecurityGroups getGroupById (Long id) throws ExeCueException {
      // TODO Auto-generated method stub
      return null;
   }

   public List<SecurityGroups> getGroups (String status) throws ExeCueException {
      // TODO Auto-generated method stub
      return null;
   }

   public SecurityRoles getRoleById (Long id) throws ExeCueException {
      // TODO Auto-generated method stub
      return null;
   }

   public List<SecurityRoles> getRoles (String status) throws ExeCueException {
      // TODO Auto-generated method stub
      return null;
   }

   public User getUser (String userid) throws ExeCueException {
      // TODO Auto-generated method stub
      return null;
   }

   public User getUserById (Long id) throws ExeCueException {
      // TODO Auto-generated method stub
      return null;
   }

   public void updateGroup (SecurityGroups group) throws ExeCueException {
      // TODO Auto-generated method stub

   }

   public void updateRole (SecurityRoles role) throws ExeCueException {
      // TODO Auto-generated method stub

   }

   public void updateUser (User user) throws ExeCueException {
      // TODO Auto-generated method stub

   }

   public boolean updateUserPasswordAttempt (String userid, int noOfAttments) throws ExeCueException {
      // TODO Auto-generated method stub
      return false;
   }

}
