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


package com.execue.swi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.core.common.bean.security.SecurityRoles;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.bean.security.UserRequest;
import com.execue.core.common.bean.security.UserRequestType;
import com.execue.core.common.type.CheckType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.swi.dataaccess.IUserManagementDataAccessManager;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IUserManagementService;

public class UserManagementServiceImpl implements IUserManagementService {

   private IUserManagementDataAccessManager userManagementDataAccessManager;

   private Logger                           logger = Logger.getLogger(UserManagementServiceImpl.class);

   public IUserManagementDataAccessManager getUserManagementDataAccessManager () {
      return userManagementDataAccessManager;
   }

   public void setUserManagementDataAccessManager (IUserManagementDataAccessManager userManagementDataAccessManager) {
      this.userManagementDataAccessManager = userManagementDataAccessManager;
   }

   public boolean assignGroup (User user, List<SecurityGroups> groups) {
      boolean result = true;
      try {
         return userManagementDataAccessManager.assignGroup(user, groups);
      } catch (SWIException e) {
         e.printStackTrace();
         result = false;
      }

      return result;
   }

   public boolean changePassword (String userid, String oldPassword, String newPassword) {
      boolean result = true;
      try {
         return userManagementDataAccessManager.changePassword(userid, oldPassword, newPassword);
      } catch (SWIException e) {
         e.printStackTrace();
         result = false;
      }

      return result;
   }

   public User getUser (String userid) throws SWIException {
      return userManagementDataAccessManager.getUser(userid);
   }

   public User getUserForAuthentication (String userid) throws SWIException {
      User user = getUser(userid);
      if (user == null) {
         return null;
      }
      Set<SecurityGroups> groups = user.getGroups();
      if (groups != null) {
         for (SecurityGroups securityGroup : groups) {
            Set<SecurityRoles> roles = securityGroup.getRoles();
            if (roles != null) {
               for (SecurityRoles securityRole : roles) {
                  securityRole.getDescription();
               }
            }
         }
      }
      return user;
   }

   public User getUserForAuthenticationByUserId (Long userid) throws SWIException {
      User user = getUserById(userid);
      if (user == null) {
         return null;
      }
      Set<SecurityGroups> groups = user.getGroups();
      if (groups != null) {
         for (SecurityGroups securityGroup : groups) {
            Set<SecurityRoles> roles = securityGroup.getRoles();
            if (roles != null) {
               for (SecurityRoles securityRole : roles) {
                  securityRole.getDescription();
               }
            }
         }
      }
      return user;
   }

   public List<User> getUsers () {
      try {
         return userManagementDataAccessManager.getUsers();
      } catch (SWIException e) {
         e.printStackTrace();
      }
      return new ArrayList<User>();
   }

   public boolean updateUserPasswordAttempt (String userid, int noOfAttments) {
      logger.debug("updateUserPasswordAttempt " + noOfAttments);
      boolean result = true;
      try {
         return userManagementDataAccessManager.updateUserPasswordAttempt(userid, noOfAttments);
      } catch (SWIException e) {
         e.printStackTrace();
         result = false;
      }
      return result;
   }

   public void assignGroupRoles (SecurityGroups group, List<SecurityRoles> roles) {
      try {
         userManagementDataAccessManager.assignGroupRoles(group, roles);
      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   public Long createGroup (SecurityGroups group) throws SWIException {
      if (userManagementDataAccessManager.objectExistsByField(group.getName(), "name", SecurityGroups.class)) {
         throw new SWIException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, "Group with name [" + group.getName()
                  + "] already exists");
      }
      return userManagementDataAccessManager.createGroup(group);
   }

   public Long createRole (SecurityRoles role) throws SWIException {
      if (userManagementDataAccessManager.objectExistsByField(role.getName(), "name", SecurityRoles.class)) {
         throw new SWIException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, "Role with name [" + role.getName()
                  + "] already exists");
      }
      return userManagementDataAccessManager.createRole(role);
   }

   public SecurityGroups getGroupById (Long id) {
      SecurityGroups group = null;
      try {
         group = userManagementDataAccessManager.getGroupById(id);
      } catch (SWIException e) {
         e.printStackTrace();
      }
      return group;
   }

   public List<SecurityGroups> getGroups (String status) {
      List<SecurityGroups> groups = null;
      try {
         groups = userManagementDataAccessManager.getGroups(status);
      } catch (SWIException e) {
         e.printStackTrace();
      }
      return groups;

   }

   public SecurityRoles getRoleById (Long id) {
      SecurityRoles role = null;
      try {
         role = userManagementDataAccessManager.getRoleById(id);
      } catch (SWIException e) {
         e.printStackTrace();
      }

      return role;
   }

   public List<SecurityRoles> getRoles (String status) {
      List<SecurityRoles> roles = null;
      try {
         roles = userManagementDataAccessManager.getRoles(status);
      } catch (SWIException e) {
         e.printStackTrace();
      }
      return roles;
   }

   public User getUserById (Long id) {
      User user = null;
      try {
         user = userManagementDataAccessManager.getUserById(id);
      } catch (SWIException e) {
         e.printStackTrace();
      }
      return user;
   }

   public void updateGroup (SecurityGroups group) {
      try {
         userManagementDataAccessManager.updateGroup(group);
      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   public void updateRole (SecurityRoles role) {
      try {
         userManagementDataAccessManager.updateRole(role);
      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   public Long createUser (User user) throws SWIException {

      if (userManagementDataAccessManager.objectExistsByField(user.getUsername(), "username", User.class)) {
         throw new SWIException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, "User with name [" + user.getUsername()
                  + "] already exists");
      }
      return userManagementDataAccessManager.createUser(user);
   }

   public Long createUserRequest (UserRequest userRequest) throws SWIException {
      return userManagementDataAccessManager.createUserRequest(userRequest);
   }

   public void updateUser (User user) {
      try {
         userManagementDataAccessManager.updateUser(user);
      } catch (SWIException e) {
         e.printStackTrace();
      }

   }

   public List<SecurityGroups> getAllGroups () {
      try {
         return userManagementDataAccessManager.getAllGroups();
      } catch (SWIException e) {
         e.printStackTrace();
      }

      return new ArrayList<SecurityGroups>();
   }

   public List<SecurityRoles> getAllRoles () throws SWIException {
      return userManagementDataAccessManager.getAllRoles();
   }

   public void deleteGroup (SecurityGroups group) throws SWIException {
      userManagementDataAccessManager.deleteGroup(group);

   }

   public void deleteSecurityRole (SecurityRoles role) throws SWIException {
      userManagementDataAccessManager.deleteRole(role);

   }

   public List<SecurityGroups> getUserGroups (long userId) throws SWIException {
      return userManagementDataAccessManager.getUserGroups(userId);
   }

   public User getUserWithGroups (Long userId) throws SWIException {
      User user = userManagementDataAccessManager.getUserById(userId);
      if (user != null) {
         Set<SecurityGroups> groups = user.getGroups();
         for (SecurityGroups securityGroup : groups) {
            securityGroup.getDescription();
         }
      }
      return user;
   }

   public SecurityGroups getGroupWithRoles (Long id) throws SWIException {
      SecurityGroups group = userManagementDataAccessManager.getGroupById(id);
      if (group != null) {
         Set<SecurityRoles> roles = group.getRoles();
         for (SecurityRoles securityRole : roles) {
            securityRole.getDescription();
         }
      }
      return group;
   }

   @Override
   public List<User> suggestUsers (String searchString, int limit) throws SWIException {
      return getUserManagementDataAccessManager().suggestUsers(searchString, limit);
   }

   public User getUserByEncryptedKey (String encryptedKey) throws SWIException {
      return userManagementDataAccessManager.getUserByEncryptedKey(encryptedKey);
   }

   public List<UserRequest> getFreshUserRequestsByRequestType (UserRequestType userRequestType) throws SWIException {
      return userManagementDataAccessManager.getFreshUserRequestsByRequestType(userRequestType);
   }

   public List<UserRequest> getUserRequestsByAcceptRejectType (UserRequestType userRequestType,
            CheckType acceptRejectType) throws SWIException {
      return userManagementDataAccessManager.getUserRequestsByAcceptRejectType(userRequestType, acceptRejectType);
   }

   public UserRequest getUserRequestById (Long id) throws SWIException {
      return userManagementDataAccessManager.getUserRequestById(id);

   }

   public void updateUserRequests (List<UserRequest> userRequests) throws SWIException {
      userManagementDataAccessManager.updateUserRequests(userRequests);
   }

}
