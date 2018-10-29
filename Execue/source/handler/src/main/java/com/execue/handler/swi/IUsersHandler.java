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


package com.execue.handler.swi;

import java.util.List;

import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.bean.security.UserRequest;
import com.execue.core.common.bean.security.UserRequestType;
import com.execue.core.common.type.CheckType;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIUserRequest;

public interface IUsersHandler {

   public User getUserById (Long id) throws HandlerException;

   public void createUser (User user) throws HandlerException;

   public void updateUser (User user, String originalPassword) throws HandlerException;

   public List<User> getUsers () throws HandlerException;

   public boolean assignGroup (User user, List<SecurityGroups> groups) throws HandlerException;

   public User getUserAndGroups (Long id) throws HandlerException;

   public boolean activateUser (String encryptedKey) throws HandlerException;

   public void createAppUser (User user) throws HandlerException;

   public User resetPassword (String encryptedKey) throws HandlerException;

   public void changePassword (User user, String oldPassword) throws HandlerException;

   public void forgotPassword (String userName) throws HandlerException;

   public void changePassword (User user) throws HandlerException;

   public User getUserByName (String username) throws HandlerException;

   public void createUserRequest (UserRequest userRequest) throws HandlerException;

   public List<UIUserRequest> getFreshUserRequestsByRequestType (UserRequestType userRequestType) throws HandlerException;

   public List<UIUserRequest> getUserRequestsByAcceptRejectType (UserRequestType userRequestType,
            CheckType acceptRejectType) throws HandlerException;

   public void updateUserRequests (CheckType acceptRejectType, UserRequestType userRequestType, List<UIUserRequest> userRequests) throws HandlerException;

   public void persistUserRequestInfo (UIUserRequest userRequest) throws HandlerException;

}
