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


package com.execue.handler.security;

import java.util.List;

import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.core.common.bean.security.SecurityRoles;
import com.execue.core.common.bean.security.User;
import com.execue.core.exception.ExeCueException;

public interface IUserManagementHanlder {

   // user methods
   public User getUserById (Long id) throws ExeCueException;

   public Long createUser (User user) throws ExeCueException;

   public void updateUser (User user) throws ExeCueException;

   public User getUser (String userid) throws ExeCueException;

   public boolean assignGroup (User user, List<SecurityGroups> groups) throws ExeCueException;

   public boolean changePassword (String userid, String oldPassword, String newPassword) throws ExeCueException;

   public boolean updateUserPasswordAttempt (String userid, int noOfAttments) throws ExeCueException;

   // group methods
   public Long createGroup (SecurityGroups group) throws ExeCueException;

   public void updateGroup (SecurityGroups group) throws ExeCueException;

   public SecurityGroups getGroupById (Long id) throws ExeCueException;

   public List<SecurityGroups> getGroups (String status) throws ExeCueException;

   public void assignGroupRoles (SecurityGroups group, List<SecurityRoles> roles) throws ExeCueException;

   // group methods
   
   public Long createRole (SecurityRoles role) throws ExeCueException;

   public void updateRole (SecurityRoles role) throws ExeCueException;

   public SecurityRoles getRoleById (Long id) throws ExeCueException;

   public List<SecurityRoles> getRoles (String status) throws ExeCueException;

}
