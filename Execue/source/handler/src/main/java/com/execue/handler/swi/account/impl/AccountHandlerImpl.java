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


package com.execue.handler.swi.account.impl;

import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.execue.core.common.bean.entity.CountryLookup;
import com.execue.core.common.bean.security.User;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.handler.swi.account.IAccountHandler;
import com.execue.security.UserContextService;
import com.execue.security.bean.ExecueUserDetails;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.ILookupService;
import com.execue.swi.service.IUserManagementService;

public class AccountHandlerImpl extends UserContextService implements IAccountHandler {

   private static final Logger    log = Logger.getLogger(AccountHandlerImpl.class);

   private IUserManagementService userManagementService;
   private PasswordEncoder        passwordEncoder;
   private ILookupService         lookupService;
   private SaltSource             saltSource;

   public User getUser () throws HandlerException {
      Long id = getUserContext().getUser().getId();
      try {
         return userManagementService.getUserById(id);
      } catch (Exception e) {
         log.error("exception in handler : " + e);
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<CountryLookup> getCountryCodes () throws HandlerException {
      try {
         return getLookupService().getCountryCodes();
      } catch (SWIException e) {
         log.error("Country Code retrival failed.Exception in handler : " + e);
         throw new HandlerException(e.getCode(), e.getMessage());
      }
   }

   public void updateProfile (User user) throws HandlerException {
      try {
         User userDetail = userManagementService.getUser(user.getUsername());
         if (userDetail != null) {
            userDetail.setFirstName(user.getFirstName());
            userDetail.setLastName(user.getLastName());
            userDetail.setAddress1(user.getAddress1());
            userDetail.setAddress2(user.getAddress2());
            userDetail.setState(user.getState());
            userDetail.setCity(user.getCity());
            userDetail.setZip(user.getZip());
            userDetail.setCountry(user.getCountry());
            userManagementService.updateUser(userDetail);
         }

      } catch (SWIException e) {
         e.printStackTrace();
         log.error("Update user failed.Exception in handler : " + e);
         throw new HandlerException(ExeCueExceptionCodes.USER_UPDATION_FAILED, e.getMessage());
      }
   }

   public void changePassword (User uiUser, String oldPassword) throws HandlerException {
      try {
         User user = userManagementService.getUser(uiUser.getUsername());
         if (user != null) {
            if (user.getSalt() == null) {
               user.setSalt(getRandomAlphanumeric());
            }
            Object salt = saltSource.getSalt(new ExecueUserDetails(user, null));
            String userPasswordHashed = passwordEncoder.encodePassword(oldPassword, salt);
            if (log.isDebugEnabled()) {
               log.debug("Change password");
               log.debug("userPasswordHashed:" + userPasswordHashed);
               log.debug("userPassword:" + user.getPassword());
            }
            if (userPasswordHashed.equals(user.getPassword())) {
               user.setChangePassword(true);
               user.setPassword(getPasswordEncoder().encodePassword(uiUser.getPassword(), salt));
               userManagementService.updateUser(user);
            } else {
               throw new HandlerException(ExeCueExceptionCodes.INVALID_PASSWORD, "Invalid old Password.");
            }
         }

      } catch (SWIException e) {
         e.printStackTrace();
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e.getMessage());
      }

   }

   private String getRandomAlphanumeric () {
      return RandomStringUtils.randomAlphanumeric(5);
   }

   public PasswordEncoder getPasswordEncoder () {
      return passwordEncoder;
   }

   public void setPasswordEncoder (PasswordEncoder passwordEncoder) {
      this.passwordEncoder = passwordEncoder;
   }

   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   /**
    * @return the lookupService
    */
   public ILookupService getLookupService () {
      return lookupService;
   }

   /**
    * @param lookupService
    *           the lookupService to set
    */
   public void setLookupService (ILookupService lookupService) {
      this.lookupService = lookupService;
   }

   /**
    * @return the saltSource
    */
   public SaltSource getSaltSource () {
      return saltSource;
   }

   /**
    * @param saltSource the saltSource to set
    */
   public void setSaltSource (SaltSource saltSource) {
      this.saltSource = saltSource;
   }

}
