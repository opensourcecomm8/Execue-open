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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.execue.core.common.bean.SecurityGroupType;
import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.bean.security.UserRequest;
import com.execue.core.common.bean.security.UserRequestType;
import com.execue.core.common.bean.security.UserStatus;
import com.execue.core.common.type.CheckType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIUserRequest;
import com.execue.handler.swi.IUsersHandler;
import com.execue.security.bean.ExecueUserDetails;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IUserManagementService;
import com.execue.util.cryptography.ICryptographyService;
import com.execue.util.cryptography.exception.CryptographyException;
import com.execue.util.cryptography.type.EncryptionAlgorithm;
import com.execue.util.mail.ISMTPMailService;
import com.execue.util.mail.bean.MailEntity;
import com.execue.util.mail.exception.SMTPMailException;

public class UsersHandlerImpl implements IUsersHandler {

   private static final Logger       log = Logger.getLogger(UsersHandlerImpl.class);

   private IUserManagementService    userManagementService;
   private ISMTPMailService          smtpMailService;
   private ICryptographyService      cryptographyService;
   private ICoreConfigurationService coreConfigurationService;
   private PasswordEncoder           passwordEncoder;
   private SaltSource                saltSource;

   public void createUser (User user) throws HandlerException {
      try {
         if (user != null) {
            user.setCreatedDate(new Date());
            user.setFullName(user.getFirstName() + " " + user.getLastName());
            user.setSalt(getRandomAlphanumeric());
            Object salt = saltSource.getSalt(new ExecueUserDetails(user, null));
            user.setPassword(getPasswordEncoder().encodePassword(user.getPassword(), salt));
            userManagementService.createUser(user);
         }
      } catch (SWIException se) {
         if (se.getCode() == ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS)
            throw new HandlerException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, se.getMessage());
         else {
            throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, se.getMessage());
         }
      }
   }

   public User getUserById (Long id) throws HandlerException {
      try {
         return userManagementService.getUserById(id);
      } catch (Exception e) {
         log.error("exception in handler : " + e);
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<User> getUsers () throws HandlerException {
      try {
         return userManagementService.getUsers();
      } catch (Exception e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updateUser (User user, String originalPassword) throws HandlerException {
      try {
         User userDetail = userManagementService.getUser(user.getUsername());
         if (userDetail != null) {
            userDetail.setFirstName(user.getFirstName());
            userDetail.setLastName(user.getLastName());
            user.setFullName(user.getFirstName() + " " + user.getLastName());
            userDetail.setStatus(user.getStatus());
            if (!"*****".equals(user.getPassword())) {
               if (userDetail.getSalt() == null) {
                  userDetail.setSalt(getRandomAlphanumeric());
               }
               Object salt = saltSource.getSalt(new ExecueUserDetails(userDetail, null));
               userDetail.setPassword(getPasswordEncoder().encodePassword(user.getPassword(), salt));
            }
            userManagementService.updateUser(userDetail);
         }

      } catch (Exception e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public boolean assignGroup (User user, List<SecurityGroups> groups) throws HandlerException {
      boolean result = false;
      try {
         result = userManagementService.assignGroup(user, groups);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return result;
   }

   public User getUserAndGroups (Long id) throws HandlerException {
      try {
         return userManagementService.getUserWithGroups(id);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public boolean activateUser (String encryptedKey) throws HandlerException {
      boolean isUserAlreadyActive = true;
      try {
         User user = userManagementService.getUserByEncryptedKey(encryptedKey);
         if (user != null) {
            if (user.getStatus().getValue().equalsIgnoreCase(UserStatus.INACTIVE.getValue())) {
               user.setStatus(UserStatus.ACTIVE);
               userManagementService.updateUser(user);
               SecurityGroups securityGroup = new SecurityGroups();
               securityGroup.setId(Long.valueOf(SecurityGroupType.PUBLIHSER_GROUP.getValue()));
               List<SecurityGroups> securityGroups = new ArrayList<SecurityGroups>();
               securityGroups.add(securityGroup);
               userManagementService.assignGroup(user, securityGroups);
               isUserAlreadyActive = false;
            }
         }
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.USER_ACTIVATION_FAILED, e);
      }
      return isUserAlreadyActive;
   }

   public void createAppUser (User user) throws HandlerException {
      try {
         if (user != null) {
            user.setEncryptedKey(ExecueCoreUtil.removeSpecialCharacters(getEncryptedKey(user.getUsername())));
            user.setStatus(UserStatus.INACTIVE);
            user.setEmailId(user.getUsername());
            user.setCreatedDate(new Date());
            user.setFullName(user.getFirstName() + " " + user.getLastName());
            user.setSalt(getRandomAlphanumeric());
            Object salt = saltSource.getSalt(new ExecueUserDetails(user, null));
            user.setPassword(getPasswordEncoder().encodePassword(user.getPassword(), salt));
            userManagementService.createUser(user);
            MailEntity mailEntity = prepareMailEntityForAppUser(user.getEmailId());
            if (mailEntity != null) {
               mailEntity.setSubject(getCoreConfigurationService().getMailServerSubject());
               String body = getCoreConfigurationService().getMailServerUserActivationBody();
               String url = getCoreConfigurationService().getMailServerUrl();
               mailEntity.setMessage(getMessageBody(body, url, user));
               smtpMailService.postMail(mailEntity);
            }
         }
      } catch (SWIException se) {
         if (se.getCode() == ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS) {
            throw new HandlerException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, se.getMessage());
         } else {
            throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, se.getMessage());
         }
      } catch (SMTPMailException sMailException) {
         throw new HandlerException(ExeCueExceptionCodes.MAIL_SENDING_FAILED, sMailException);
      }
   }

   /**
    * This private method will be use to prepare mail entity for app user creation and fotgot password
    * 
    * @param emailId
    * @return
    */
   private MailEntity prepareMailEntityForAppUser (String emailId) {
      MailEntity mailEntity = new MailEntity();
      mailEntity.setFrom(getCoreConfigurationService().getMailServerFrom());
      mailEntity.setHostName(getCoreConfigurationService().getMailServerHostName());
      mailEntity.setUserName(getCoreConfigurationService().getMailServerUserName());
      mailEntity.setPassword(getCoreConfigurationService().getMailServerPassword());
      List<String> recipients = new ArrayList<String>();
      recipients.add(emailId);
      mailEntity.setRecipients(recipients);
      return mailEntity;
   }

   private MailEntity getMailEntity () {
      MailEntity mailEntity = new MailEntity();
      mailEntity.setFrom(getCoreConfigurationService().getMailServerFrom());
      mailEntity.setHostName(getCoreConfigurationService().getMailServerHostName());
      mailEntity.setUserName(getCoreConfigurationService().getMailServerUserName());
      mailEntity.setPassword(getCoreConfigurationService().getMailServerPassword());
      List<String> recipients = new ArrayList<String>();
      recipients.add(getCoreConfigurationService().getMailServerTo());
      mailEntity.setRecipients(recipients);
      return mailEntity;
   }

   private String getMessageBody (String message, String url, User user) {
      if (message != null) {
         message = message.replace("@@url@@", url);
         message = message.replace("@@encryptedKey@@", user.getEncryptedKey());
      }
      return message;

   }

   private String getMessageBody (String message, UserRequest userRequest) {
      if (message != null) {
         message = message.replace("@@emailId@@", userRequest.getEmailId());
         message = message.replace("@@contactNum@@", userRequest.getContactPhoneNum());

      }
      return message;

   }

   public void changePassword (User uiUser) throws HandlerException {
      try {
         User user = userManagementService.getUser(uiUser.getUsername());
         if (user != null) {
            if (log.isDebugEnabled()) {
               log.debug("Reset password");
               log.debug("userPassword:" + user.getPassword());
            }
            if (user.getSalt() == null) {
               user.setSalt(getRandomAlphanumeric());
            }
            Object salt = saltSource.getSalt(new ExecueUserDetails(user, null));
            user.setPassword(getPasswordEncoder().encodePassword(uiUser.getPassword(), salt));
            user.setStatus(UserStatus.ACTIVE);
            user.setChangePassword(true);
            userManagementService.updateUser(user);
         }
      } catch (SWIException se) {
         se.printStackTrace();
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, se.getMessage());
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
               if (user.getSalt() == null) {
                  user.setSalt(getRandomAlphanumeric());
               }
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

   public User resetPassword (String encryptedKey) throws HandlerException {
      User user = null;
      try {
         user = userManagementService.getUserByEncryptedKey(encryptedKey);
      } catch (SWIException e) {
         e.printStackTrace();
      }
      return user;
   }

   public void forgotPassword (String userName) throws HandlerException {
      try {
         User user = userManagementService.getUser(userName);
         if (user != null) {
            user.setEncryptedKey(ExecueCoreUtil.removeSpecialCharacters(getEncryptedKey(user.getUsername())));
            user.setStatus(UserStatus.INACTIVE);
            userManagementService.updateUser(user);
            MailEntity mailEntity = prepareMailEntityForAppUser(user.getEmailId());
            if (mailEntity != null) {
               mailEntity.setSubject(getCoreConfigurationService().getMailServerUserResetPasswordSubject());
               String body = getCoreConfigurationService().getMailServerUserResetPasswordBody();
               String url = getCoreConfigurationService().getMailServerUserResetPasswordUrl();
               mailEntity.setMessage(getMessageBody(body, url, user));
               smtpMailService.postMail(mailEntity);
            }
         }
      } catch (SWIException se) {
         se.printStackTrace();
         if (se.getCode() == ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED) {
            throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, se.getMessage());
         } else {
            throw new HandlerException(ExeCueExceptionCodes.USER_UPDATION_FAILED, se.getMessage());
         }

      } catch (SMTPMailException sMailException) {
         sMailException.printStackTrace();
         throw new HandlerException(ExeCueExceptionCodes.MAIL_SENDING_FAILED, sMailException);
      }

   }

   public void createUserRequest (UserRequest userRequest) throws HandlerException {
      try {
         userManagementService.createUserRequest(userRequest);
         MailEntity mailEntity = getMailEntity();
         if (mailEntity != null) {
            mailEntity.setSubject(getCoreConfigurationService().getMailServerSubjectAdvancedOptions());
            String body = getCoreConfigurationService().getMailServerUserAdvancedOptionsBody();
            mailEntity.setMessage(getMessageBody(body, userRequest));
            smtpMailService.postMail(mailEntity);
         }
      } catch (SWIException se) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, se.getMessage());
      } catch (SMTPMailException sMailException) {
         throw new HandlerException(ExeCueExceptionCodes.MAIL_SENDING_FAILED, sMailException);
      }
   }

   public List<UIUserRequest> getFreshUserRequestsByRequestType (UserRequestType userRequestType)
            throws HandlerException {
      List<UIUserRequest> uiUserRequests = new ArrayList<UIUserRequest>();
      try {
         List<UserRequest> userRequests = userManagementService.getFreshUserRequestsByRequestType(userRequestType);
         if (ExecueCoreUtil.isCollectionNotEmpty(userRequests)) {
            for (UserRequest userRequest : userRequests) {
               uiUserRequests.add(tranformUserRequest(userRequest));
            }
         }
         return uiUserRequests;
      } catch (SWIException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   public List<UIUserRequest> getUserRequestsByAcceptRejectType (UserRequestType userRequestType,
            CheckType acceptRejectType) throws HandlerException {
      List<UIUserRequest> uiUserRequests = new ArrayList<UIUserRequest>();
      try {
         List<UserRequest> userRequests = userManagementService.getUserRequestsByAcceptRejectType(userRequestType,
                  acceptRejectType);
         if (ExecueCoreUtil.isCollectionNotEmpty(userRequests)) {
            for (UserRequest userRequest : userRequests) {
               uiUserRequests.add(tranformUserRequest(userRequest));
            }
         }
         return uiUserRequests;
      } catch (SWIException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   private String getRandomAlphanumeric () {
      return RandomStringUtils.randomAlphanumeric(5);
   }

   private String getEncryptedKey (String userName) throws HandlerException {
      String encryptKey = null;
      try {
         Calendar now = Calendar.getInstance();
         String unencryptedString = userName + now.getTimeInMillis();
         encryptKey = cryptographyService.encryptBase64(unencryptedString, "execuelite", EncryptionAlgorithm.DES);
      } catch (CryptographyException cryptographyException) {
         cryptographyException.printStackTrace();
         throw new HandlerException(ExeCueExceptionCodes.ENCRPTION_PROCESS_FAILED, cryptographyException);
      }

      return encryptKey;
   }

   public User getUserByName (String username) throws HandlerException {
      try {
         return userManagementService.getUser(username);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, e);
      }
   }

   public void updateUserRequests (CheckType acceptRejectType, UserRequestType userRequestType,
            List<UIUserRequest> userRequests) throws HandlerException {
      try {
         List<UserRequest> userRequestsToBeSaved = new ArrayList<UserRequest>();
         if (ExecueCoreUtil.isCollectionNotEmpty(userRequests)) {
            for (UIUserRequest uiUserRequest : userRequests) {
               UserRequest userRequestFromDB = userManagementService.getUserRequestById(uiUserRequest.getId());
               userRequestFromDB.setComment(uiUserRequest.getAdminComment());
               userRequestFromDB.setAcceptRejectRequest(uiUserRequest.getAcceptRejectRequest());
               userRequestsToBeSaved.add(userRequestFromDB);
            }
         }
         userManagementService.updateUserRequests(userRequestsToBeSaved);
         for (UserRequest userRequestTobeSaved : userRequestsToBeSaved) {
            if (CheckType.YES.equals(acceptRejectType) && UserRequestType.ADVANCED_OPTIONS.equals(userRequestType)) {
               assignAdvanceGroup(userRequestTobeSaved);
            }
            postEmail(acceptRejectType, userRequestType, userRequestTobeSaved);
         }

      } catch (SWIException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (SMTPMailException e) {
         e.printStackTrace();
      }
   }

   private void assignAdvanceGroup (UserRequest userRequestTobeSaved) throws SWIException {
      User user = userManagementService.getUserById(userRequestTobeSaved.getUserId());
      SecurityGroups group = userManagementService.getGroupById(Long.valueOf(SecurityGroupType.ADV_PUBLISHER_GROUP
               .getValue()));
      List<SecurityGroups> userGroups = userManagementService.getUserGroups(user.getId());
      if (!isGroupAlreadyAssigned(group, userGroups)) {
         userGroups.add(group);
      }
      userManagementService.assignGroup(user, userGroups);
   }

   private void postEmail (CheckType acceptRejectType, UserRequestType userRequestType, UserRequest userRequest)
            throws SMTPMailException {
      MailEntity mailEntity = getMailEntity();
      if (mailEntity != null) {
         if (UserRequestType.ADVANCED_OPTIONS.equals(userRequestType)) {
            List<String> recipients = new ArrayList<String>();
            recipients.add(userRequest.getEmailId());
            mailEntity.setRecipients(recipients);
            mailEntity.setSubject(getCoreConfigurationService().getMailServerSubjectAdvancedOptions());
            String messageBody = getCoreConfigurationService().getMailServerUserAdvancedOptionsAcceptRejectBody();
            mailEntity.setMessage(getUserRequestMessageBody(messageBody, userRequest));

         } else if (UserRequestType.DEMO_REQUEST.equals(userRequestType)) {
            List<String> recipients = new ArrayList<String>();
            recipients.add(userRequest.getEmailId());
            mailEntity.setRecipients(recipients);
            mailEntity.setSubject(getCoreConfigurationService().getMailServerDemoRequestSubject());
            String messageBody = getCoreConfigurationService().getMailServerUserDemoRequestsAcceptRejectBody();
            mailEntity.setMessage(getUserRequestMessageBody(messageBody, userRequest));

         } else if (UserRequestType.GENERAL_FEEDBACK.equals(userRequestType)) {
            List<String> recipients = new ArrayList<String>();
            recipients.add(userRequest.getEmailId());
            mailEntity.setRecipients(recipients);
            mailEntity.setSubject(getCoreConfigurationService().getMailServerFeedbackSubject());
            String messageBody = getCoreConfigurationService().getMailServerFeedbackAcceptRejectBody();
            mailEntity.setMessage(getUserRequestMessageBody(messageBody, userRequest));

         }
         smtpMailService.postMail(mailEntity);
      }
   }

   private String getUserRequestMessageBody (String messageBody, UserRequest userRequest) {
      if (messageBody != null) {
         messageBody = messageBody.replace("@@comments@@", (userRequest.getComment() != null ? userRequest.getComment()
                  : ""));
      }
      return messageBody;
   }

   public void persistUserRequestInfo (UIUserRequest uiUserRequest) throws HandlerException {
      try {
         userManagementService.createUserRequest(tranformUIUserRequest(uiUserRequest));
      } catch (SWIException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   private UserRequest tranformUIUserRequest (UIUserRequest uiUserRequest) {
      UserRequest userRequest = new UserRequest();
      userRequest.setId(uiUserRequest.getId());
      userRequest.setUserId(uiUserRequest.getUserId());
      userRequest.setFirstName(uiUserRequest.getFirstName());
      userRequest.setLastName(uiUserRequest.getLastName());
      userRequest.setJobTitle(uiUserRequest.getJobTitle());
      userRequest.setOrganization(uiUserRequest.getOrganization());
      userRequest.setRegion(uiUserRequest.getRegion());
      userRequest.setUpdateNotification(uiUserRequest.getUpdateNotification());
      userRequest.setSubject(uiUserRequest.getSubject());
      userRequest.setNotes(uiUserRequest.getNotes());
      userRequest.setEmailId(uiUserRequest.getEmailId());
      userRequest.setContactPhoneNum(uiUserRequest.getContactPhoneNum());
      userRequest.setComment(uiUserRequest.getAdminComment());
      userRequest.setUserRequestType(uiUserRequest.getUserRequestType());
      userRequest.setAcceptRejectRequest(uiUserRequest.getAcceptRejectRequest());
      return userRequest;
   }

   private UIUserRequest tranformUserRequest (UserRequest userRequest) {
      UIUserRequest uiUserRequest = new UIUserRequest();
      uiUserRequest.setId(userRequest.getId());
      uiUserRequest.setUserId(userRequest.getUserId());
      uiUserRequest.setFirstName((userRequest.getFirstName() != null) ? userRequest.getFirstName() : "");
      uiUserRequest.setLastName((userRequest.getLastName() != null) ? userRequest.getLastName() : "");
      uiUserRequest.setUserFullName(uiUserRequest.getFirstName() + " " + uiUserRequest.getLastName());
      uiUserRequest.setJobTitle(userRequest.getJobTitle());
      uiUserRequest.setOrganization(userRequest.getOrganization());
      uiUserRequest.setRegion(userRequest.getRegion());
      uiUserRequest.setUpdateNotification(userRequest.getUpdateNotification());
      uiUserRequest.setSubject(userRequest.getSubject());
      uiUserRequest.setNotes(userRequest.getNotes());
      uiUserRequest.setEmailId(userRequest.getEmailId());
      uiUserRequest.setContactPhoneNum(userRequest.getContactPhoneNum());
      uiUserRequest.setAdminComment(userRequest.getComment());
      uiUserRequest.setUserRequestType(userRequest.getUserRequestType());
      uiUserRequest.setAcceptRejectRequest(userRequest.getAcceptRejectRequest());
      return uiUserRequest;
   }

   private boolean isGroupAlreadyAssigned (SecurityGroups group, List<SecurityGroups> userGroups) {
      boolean goupExists = false;
      for (SecurityGroups securityGroup : userGroups) {
         if (securityGroup.getId().equals(group.getId())) {
            goupExists = true;
            break;
         }
      }
      return goupExists;
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

   public void setSmtpMailService (ISMTPMailService smtpMailService) {
      this.smtpMailService = smtpMailService;
   }

   public void setCryptographyService (ICryptographyService cryptographyService) {
      this.cryptographyService = cryptographyService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   public SaltSource getSaltSource () {
      return saltSource;
   }

   public void setSaltSource (SaltSource saltSource) {
      this.saltSource = saltSource;
   }
}
