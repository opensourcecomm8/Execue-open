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


package com.execue.web.core.action.account;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.CountryLookup;
import com.execue.core.common.bean.security.UserRequest;
import com.execue.core.common.bean.security.UserRequestType;
import com.execue.core.common.type.CheckType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIUserRequest;
import com.execue.handler.swi.account.IAccountHandler;
import com.execue.web.core.action.swi.security.BaseUserAction;

public class AccountProfileAction extends BaseUserAction {

   private static final Logger logger = Logger.getLogger(AccountProfileAction.class);

   private IAccountHandler     accountHandler;
   private String              confirmPassword;
   private List<CountryLookup> countryCodes;
   private String              oldPassword;
   private UserRequest         userRequest;
   private List<UIUserRequest> userRequests;
   private UserRequestType     userRequestType;
   private CheckType           acceptRejectType;

   // Action Methods

   public String showProfile () {
      if (user == null || user.getId() == null) {
         try {
            user = accountHandler.getUser();
            countryCodes = accountHandler.getCountryCodes();
         } catch (Exception e) {
            logger.error("Err", e);
            return ERROR;
         }
      }
      return SUCCESS;
   }

   public String updateProfile () {
      try {
         accountHandler.updateProfile(user);
         countryCodes = accountHandler.getCountryCodes();
         addActionMessage(getText("execue.user.account.update.success"));
         // showProfile();
      } catch (HandlerException e) {
         e.printStackTrace();
         logger.error("Err", e);
      }
      return SUCCESS;
   }

   public String changePassword () {
      try {
         if (logger.isDebugEnabled()) {
            logger.debug("oldPassword:" + oldPassword);
         }
         if (ExecueCoreUtil.isNotEmpty(oldPassword)) {
            accountHandler.changePassword(user, oldPassword);
            addActionMessage(getText("execue.user.password.reset.success"));
         }
      } catch (HandlerException e) {
         e.printStackTrace();
         if (e.getCode() == ExeCueExceptionCodes.INVALID_PASSWORD) {
            addActionError(getText("execue.user.password.invalid"));
         } else {
            addActionError(getText("execue.errors.unable.process"));
         }
         return ERROR;
      }
      return SUCCESS;
   }

   public String showAdvancedOptions () {
      if (user == null || user.getId() == null) {
         try {
            user = accountHandler.getUser();
            userRequest = new UserRequest();
            userRequest.setEmailId(user.getEmailId());
         } catch (Exception e) {

            logger.error("Err", e);
            return ERROR;
         }
      }
      return SUCCESS;
   }

   public String createAdvanceOptions () {
      try {
         user = accountHandler.getUser();
         if (user != null) {
            userRequest.setUserId(user.getId());
            userRequest.setFirstName(user.getFirstName());
            userRequest.setLastName(user.getLastName());
            userRequest.setUserRequestType(UserRequestType.ADVANCED_OPTIONS);
         }
         usersHandler.createUserRequest(userRequest);
         addActionMessage(getText("execue.advancedoptions.success"));
      } catch (HandlerException e) {
         if (e.getCode() == ExeCueExceptionCodes.MAIL_SENDING_FAILED) {
            addActionError(getText("execue.email.send.failure"));
         } else {
            addActionError(getText("execue.errors.general"));
         }
         return ERROR;
      }
      return SUCCESS;
   }

   public String showUserRequests () {
      try {
         if (acceptRejectType != null) {
            userRequests = getUsersHandler().getUserRequestsByAcceptRejectType(userRequestType, acceptRejectType);
         } else {
            userRequests = getUsersHandler().getFreshUserRequestsByRequestType(userRequestType);
         }

      } catch (HandlerException e) {
         e.printStackTrace();
      }

      return SUCCESS;
   }

   public String updateUserRequests () {
      try {
         getUsersHandler().updateUserRequests(acceptRejectType,userRequestType, userRequests);
         setAcceptRejectType(null);
         userRequests = getUsersHandler().getFreshUserRequestsByRequestType(userRequestType);
         addActionMessage(getText("execue.advancedoptions.update.successfull"));
      } catch (HandlerException e) {
         e.printStackTrace();
         addActionError(getText("execue.errors.general"));
      }
      return SUCCESS;
   }

   public List<UserRequestType> getUserRequestTypes () {
      return (List<UserRequestType>) Arrays.asList(UserRequestType.values());
   }

   public String getConfirmPassword () {
      return confirmPassword;
   }

   public void setConfirmPassword (String confirmPassword) {
      this.confirmPassword = confirmPassword;
   }

   public IAccountHandler getAccountHandler () {
      return accountHandler;
   }

   public void setAccountHandler (IAccountHandler accountHandler) {
      this.accountHandler = accountHandler;
   }

   /**
    * @return the countryCodes
    */
   public List<CountryLookup> getCountryCodes () {
      return countryCodes;
   }

   /**
    * @param countryCodes
    *           the countryCodes to set
    */
   public void setCountryCodes (List<CountryLookup> countryCodes) {
      this.countryCodes = countryCodes;
   }

   /**
    * @return the oldPassword
    */
   public String getOldPassword () {
      return oldPassword;
   }

   /**
    * @param oldPassword
    *           the oldPassword to set
    */
   public void setOldPassword (String oldPassword) {
      this.oldPassword = oldPassword;
   }

   public UserRequest getUserRequest () {
      return userRequest;
   }

   public void setUserRequest (UserRequest userRequest) {
      this.userRequest = userRequest;
   }

   public UserRequestType getUserRequestType () {
      return userRequestType;
   }

   public void setUserRequestType (UserRequestType userRequestType) {
      this.userRequestType = userRequestType;
   }

   public CheckType getAcceptRejectType () {
      return acceptRejectType;
   }

   public void setAcceptRejectType (CheckType acceptRejectType) {
      this.acceptRejectType = acceptRejectType;
   }

   public List<UIUserRequest> getUserRequests () {
      return userRequests;
   }

   public void setUserRequests (List<UIUserRequest> userRequests) {
      this.userRequests = userRequests;
   }

}
