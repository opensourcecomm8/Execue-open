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


package com.execue.swi.dataaccess.impl;

import java.io.Serializable;
import java.util.List;

import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.core.common.bean.security.SecurityRoles;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.bean.security.UserRequest;
import com.execue.core.common.bean.security.UserRequestType;
import com.execue.core.common.type.CheckType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.dataaccess.swi.dao.IUserManagementDAO;
import com.execue.swi.dataaccess.IUserManagementDataAccessManager;
import com.execue.swi.exception.SWIException;

public class UserManagementDataAccessManagerImpl implements IUserManagementDataAccessManager {

   private IUserManagementDAO userManagementDAO;
   private static String      USERNAME_FIELD = "username";

   public boolean assignGroup (User user, List<SecurityGroups> groups) throws SWIException {
      boolean result = false;
      try {
         result = userManagementDAO.assignGroup(user, groups);
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }
      return result;

   }

   public void assignGroupRoles (SecurityGroups group, List<SecurityRoles> roles) throws SWIException {
      try {
         userManagementDAO.assignGroupRoles(group, roles);
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }
   }

   public boolean changePassword (String userid, String oldPassword, String newPassword) throws SWIException {
      boolean result = false;
      User user = null;
      try {
         user = userManagementDAO.getUser(userid);
         if (user.getPassword().equals(oldPassword)) {
            result = userManagementDAO.changePassword(userid, oldPassword, newPassword);
         }
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }
      return result;
   }

   public Long createGroup (SecurityGroups group) throws SWIException {
      try {
         userManagementDAO.create(group);
         return group.getId();
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }
   }

   public Long createRole (SecurityRoles role) throws SWIException {
      try {
         userManagementDAO.create(role);
         return role.getId();
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }
   }

   public Long createUser (User user) throws SWIException {
      try {
         userManagementDAO.create(user);
         return user.getId();
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }
   }

   public Long createUserRequest (UserRequest userRequest) throws SWIException {
      try {
         userManagementDAO.create(userRequest);
         return userRequest.getId();
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }
   }

   public SecurityGroups getGroupById (Long id) throws SWIException {
      SecurityGroups securityGroup = null;
      try {
         securityGroup = userManagementDAO.getById(id, SecurityGroups.class);
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }
      return securityGroup;
   }

   public List<SecurityGroups> getGroups (String status) throws SWIException {
      List<SecurityGroups> securityGroups = null;
      try {
         securityGroups = userManagementDAO.getGroups(status);
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }
      return securityGroups;
   }

   public SecurityRoles getRoleById (Long id) throws SWIException {
      SecurityRoles role = null;
      try {
         role = userManagementDAO.getById(id, SecurityRoles.class);
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }
      return role;
   }

   public List<SecurityRoles> getRoles (String status) throws SWIException {
      List<SecurityRoles> securityRoles = null;
      try {
         securityRoles = userManagementDAO.getRoles(status);
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }
      return securityRoles;
   }

   public User getUser (String userid) throws SWIException {
      User user = null;
      try {
         user = userManagementDAO.getByField(userid, USERNAME_FIELD, User.class);
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, de);
      }
      return user;
   }

   public User getUserById (Long id) throws SWIException {
      User user = null;
      try {
         user = userManagementDAO.getById(id, User.class);
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }

      return user;
   }

   public void updateGroup (SecurityGroups group) throws SWIException {
      try {
         userManagementDAO.update(group);
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }
   }

   public void updateRole (SecurityRoles role) throws SWIException {
      try {
         userManagementDAO.update(role);
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }
   }

   public void updateUser (User user) throws SWIException {
      try {
         userManagementDAO.update(user);
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }

   }

   public boolean updateUserPasswordAttempt (String userid, int noOfAttempts) throws SWIException {
      boolean result = false;
      try {
         result = userManagementDAO.updateUserPasswordAttempt(userid, noOfAttempts);
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }
      return result;
   }

   public List<User> getUsers () throws SWIException {
      try {
         return userManagementDAO.getAllUsers();
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }
   }

   public IUserManagementDAO getUserManagementDAO () throws SWIException {
      return userManagementDAO;
   }

   public void setUserManagementDAO (IUserManagementDAO userManagementDAO) throws SWIException {
      this.userManagementDAO = userManagementDAO;
   }

   public List<SecurityGroups> getAllGroups () throws SWIException {
      try {
         return userManagementDAO.getAllGroups();
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }

   }

   public List<SecurityRoles> getAllRoles () throws SWIException {
      try {
         return userManagementDAO.getAllRoles();
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }

   }

   public void deleteGroup (SecurityGroups group) throws SWIException {
      try {
         userManagementDAO.delete(group);
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }

   }

   public void deleteRole (SecurityRoles role) throws SWIException {
      try {
         role.setSecurityGroups(null);
         userManagementDAO.update(role);
         userManagementDAO.delete(role);
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }

   }

   public List<SecurityGroups> getUserGroups (long userId) throws SWIException {
      try {
         return userManagementDAO.getUserGroups(userId);
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }

   }

   public <DomainObject extends Serializable> DomainObject getByName (String name, Class<DomainObject> clazz)
            throws SWIException {
      try {
         return userManagementDAO.getByName(name, clazz);
      } catch (DataAccessException de) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, de);
      }
   }

   public <DomainObject extends Serializable> boolean objectExistsByField (String fieldValue, String fieldName,
            Class<DomainObject> clazz) throws SWIException {
      boolean objectExists = true;
      try {
         userManagementDAO.getByField(fieldValue, fieldName, clazz);
      } catch (DataAccessException dataAccessException) {
         objectExists = false;
      }
      return objectExists;

   }

   public User getUserByEncryptedKey (String encryptedKey) throws SWIException {
      try {
         return userManagementDAO.getUserByEncryptedKey(encryptedKey);
      } catch (DataAccessException e) {
         throw new SWIException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<UserRequest> getFreshUserRequestsByRequestType (UserRequestType userRequestType) throws SWIException {
      try {
         return userManagementDAO.getFreshUserRequestsByRequestType(userRequestType);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public List<UserRequest> getUserRequestsByAcceptRejectType (UserRequestType userRequestType,
            CheckType acceptRejectType) throws SWIException {
      try {
         return userManagementDAO.getUserRequestsByAcceptRejectType(userRequestType, acceptRejectType);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public UserRequest getUserRequestById (Long id) throws SWIException {
      try {
         return userManagementDAO.getUserRequestById(id);
      } catch (DataAccessException e) {
         throw new SWIException(e.getCode(), e);
      }
   }

   public void updateUserRequests (List<UserRequest> userRequests) throws SWIException {
      try {
         userManagementDAO.updateAll(userRequests);
      } catch (DataAccessException e) {
         throw new SWIException(DataAccessExceptionCodes.USER_REQUEST_UPDATE_FAILED, e);
      }
   }

   @Override
   public List<User> suggestUsers (String searchString, int limit) throws SWIException {
      try {
         return userManagementDAO.suggestUsers(searchString, limit);
      } catch (DataAccessException e) {
         throw new SWIException(DataAccessExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
   }
}
