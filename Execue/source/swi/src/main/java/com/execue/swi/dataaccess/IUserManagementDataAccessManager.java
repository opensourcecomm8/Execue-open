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


package com.execue.swi.dataaccess;

import java.io.Serializable;
import java.util.List;

import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.core.common.bean.security.SecurityRoles;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.bean.security.UserRequest;
import com.execue.core.common.bean.security.UserRequestType;
import com.execue.core.common.type.CheckType;
import com.execue.swi.exception.SWIException;

public interface IUserManagementDataAccessManager {

   // user methods

   public User getUserById (Long id) throws SWIException;

   public Long createUser (User user) throws SWIException;

   public void updateUser (User user) throws SWIException;

   public User getUser (String userid) throws SWIException;

   public List<User> getUsers () throws SWIException;

   public boolean assignGroup (User user, List<SecurityGroups> groups) throws SWIException;

   public boolean changePassword (String userid, String oldPassword, String newPassword) throws SWIException;

   public boolean updateUserPasswordAttempt (String userid, int noOfAttments) throws SWIException;

   // group methods

   public <DomainObject extends Serializable> DomainObject getByName (final String name, final Class<DomainObject> clazz)
            throws SWIException;

   public <DomainObject extends Serializable> boolean objectExistsByField (String fieldValue, String fieldName,
            Class<DomainObject> clazz) throws SWIException;

   public Long createGroup (SecurityGroups group) throws SWIException;

   public void updateGroup (SecurityGroups group) throws SWIException;

   public SecurityGroups getGroupById (Long id) throws SWIException;

   public List<SecurityGroups> getGroups (String status) throws SWIException;

   public void assignGroupRoles (SecurityGroups group, List<SecurityRoles> roles) throws SWIException;

   public List<SecurityGroups> getAllGroups () throws SWIException;

   public void deleteGroup (SecurityGroups group) throws SWIException;

   public List<SecurityGroups> getUserGroups (long userId) throws SWIException;

   // group methods

   public List<SecurityRoles> getAllRoles () throws SWIException;

   public Long createRole (SecurityRoles role) throws SWIException;

   public void updateRole (SecurityRoles role) throws SWIException;

   public SecurityRoles getRoleById (Long id) throws SWIException;

   public List<SecurityRoles> getRoles (String status) throws SWIException;

   public void deleteRole (SecurityRoles role) throws SWIException;

   public User getUserByEncryptedKey (String encryptedKey) throws SWIException;

   // UserRequest methods
   public Long createUserRequest (UserRequest userRequest) throws SWIException;

   public List<UserRequest> getFreshUserRequestsByRequestType (UserRequestType userRequestType) throws SWIException;

   public List<UserRequest> getUserRequestsByAcceptRejectType (UserRequestType userRequestType,
            CheckType acceptRejectType) throws SWIException;

   public UserRequest getUserRequestById (Long id) throws SWIException;

   public void updateUserRequests (List<UserRequest> userRequests) throws SWIException;

   public List<User> suggestUsers (String searchString, int limit) throws SWIException;

}
