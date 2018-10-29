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


package com.execue.web.core.action.swi.security;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.security.User;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;

public class UserPasswordAction extends UsersAction {

   private static final long serialVersionUID = 1L;
   private static Logger     logger           = Logger.getLogger(UserPasswordAction.class);
   private User              user;
   private String            encryptedKey;
   private String            oldPassword;
   private String            confirmPassword;

   // Action Methods

   public String forgotPassword () {
      try {
         getUsersHandler().forgotPassword(user.getUsername());
         addActionMessage(getText("execue.user.password.reset.message"));
      } catch (HandlerException e) {
         e.printStackTrace();
         if (e.getCode() == ExeCueExceptionCodes.MAIL_SENDING_FAILED) {
            addActionError(getText("execue.email.send.failure"));
         } else if (e.getCode() == ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED) {
            addActionError(getText("execue.user.name.invalid"));
         } else {
            addActionError(getText("execue.errors.general"));
         }
         return ERROR;
      }
      return SUCCESS;
   }

   public String resetPassword () {
      try {
         user = getUsersHandler().resetPassword(encryptedKey);
         if (user == null) {
            addActionError(getText("execue.errors.unable.process"));
            return ERROR;
         }

      } catch (HandlerException e) {
         e.printStackTrace();
         addActionError(getText("execue.errors.unable.process"));
         return ERROR;
      }
      return SUCCESS;
   }

   public String changePassword () {
      try {
         if (logger.isDebugEnabled()) {
            logger.debug("oldPassword:" + oldPassword);
         }
         if (oldPassword != null) {
            getUsersHandler().changePassword(user, oldPassword);
            addActionMessage(getText("execue.user.password.reset.success"));
         } else {
            getUsersHandler().changePassword(user);
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

   public User getUser () {
      return user;
   }

   public void setUser (User user) {
      this.user = user;
   }

   /**
    * @return the encryptedKey
    */
   public String getEncryptedKey () {
      return encryptedKey;
   }

   /**
    * @param encryptedKey
    *           the encryptedKey to set
    */
   public void setEncryptedKey (String encryptedKey) {
      this.encryptedKey = encryptedKey;
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

   /**
    * @return the confirmPassword
    */
   public String getConfirmPassword () {
      return confirmPassword;
   }

   /**
    * @param confirmPassword
    *           the confirmPassword to set
    */
   public void setConfirmPassword (String confirmPassword) {
      this.confirmPassword = confirmPassword;
   }

}
