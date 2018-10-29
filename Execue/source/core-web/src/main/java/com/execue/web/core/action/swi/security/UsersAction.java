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

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.execue.core.common.bean.security.User;
import com.execue.core.common.bean.security.UserStatus;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;

public class UsersAction extends BaseUserAction implements SessionAware {

   private List<User>      users;
   private String          confirmPassword;
   private static Logger   logger         = Logger.getLogger(UsersAction.class);

   // TODO:-Added default usersPagination will remove, one new pagination will be implemented
   private String          paginationType = "usersPagination";
   public static final int PAGESIZE       = 9;
   public static final int numberOfLinks  = 15;
   private String          requestedPage;
   private Map             httpSessionData;
   private String          originalPassword;
   private String          username;
   private Long            userId;

   // Action Methods

   public String list () {
      try {
         users = getUsersHandler().getUsers();
         if (paginationType != null && paginationType.equalsIgnoreCase("usersPagination")) {
            paginationForUsers();
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   private void paginationForUsers () {
      if (requestedPage == null)
         requestedPage = "1";
      httpSessionData.put("USERSFORPAGINATION", users);
      int tempSize = 0;
      tempSize = (users.size() <= PAGESIZE) ? users.size() : PAGESIZE;
      users = users.subList(0, tempSize);
   }

   @SuppressWarnings ("unchecked")
   public String showUsersSubList () {
      int reqPageNo = Integer.parseInt(getRequestedPage());
      int fromIndex = 1;
      int toIndex = 1;
      if (paginationType != null && paginationType.equalsIgnoreCase("usersPagination")) {
         List<User> userList = (List<User>) httpSessionData.get("USERSFORPAGINATION");
         int tempTotCount = (userList.size() / PAGESIZE);
         int rmndr = userList.size() % PAGESIZE;
         if (rmndr != 0) {
            tempTotCount++;
         }

         if (reqPageNo <= tempTotCount) {
            fromIndex = ((reqPageNo - 1) * PAGESIZE);
            toIndex = reqPageNo * PAGESIZE;
            if (toIndex > userList.size()) {
               toIndex = (userList.size());
            }

         }
         users = userList.subList(fromIndex, toIndex);
      }
      return SUCCESS;
   }

   public String input () {
      if (user != null && user.getId() != null) {
         try {
            user = usersHandler.getUserById(user.getId());
            if (user != null) {
               confirmPassword = user.getPassword();
               originalPassword = user.getPassword();
            }

         } catch (Exception e) {
            return ERROR;
         }

         if (logger.isDebugEnabled()) {
            logger.debug("user name " + user.getUsername());
         }
      }
      return INPUT;
   }

   public String save () {
      try {
         if (user.getId() == null) {
            usersHandler.createUser(user);
            addActionMessage(getText("execue.global.insert.success", new String[] { getText("execue.user.label") + " "
                     + user.getUsername() }));
         } else {
            usersHandler.updateUser(user, originalPassword);
            addActionMessage(getText("execue.global.update.success", new String[] { getText("execue.user.label") + " "
                     + user.getUsername() }));
         }
      } catch (HandlerException e) {
         if (e.getCode() == ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS) {
            addActionError(getText("execue.global.exist.message", new String[] { user.getUsername() }));
         } else {
            addActionError(getText("execue.global.error ", e.getMessage()));
         }
         return ERROR;
      }
      return SUCCESS;
   }

   public String updateUserStatus () {
      try {
         if (user != null && user.getId() != null) {
            user = usersHandler.getUserById(user.getId());
            if (UserStatus.ACTIVE.equals(user.getStatus())) {
               user.setStatus(UserStatus.INACTIVE);
               usersHandler.updateUser(user, originalPassword);
            } else if (UserStatus.INACTIVE.equals(user.getStatus())) {
               user.setStatus(UserStatus.ACTIVE);
               usersHandler.updateUser(user, originalPassword);
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return INPUT;
   }

   public String getUserByName () {
      try {
         user = usersHandler.getUserByName(username);
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String getUserById () {
      try {
         user = usersHandler.getUserById(userId);
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;

   }

   public Map getHttpSessionData () {
      return httpSessionData;
   }

   public void setSession (Map httpSessionData) {
      this.httpSessionData = httpSessionData;
   }

   public String getRequestedPage () {
      return requestedPage;
   }

   public void setRequestedPage (String requestedPage) {
      this.requestedPage = requestedPage;
   }

   public String getPaginationType () {
      return paginationType;
   }

   public void setPaginationType (String paginationType) {
      this.paginationType = paginationType;
   }

   public String getOriginalPassword () {
      return originalPassword;
   }

   public void setOriginalPassword (String originalPassword) {
      this.originalPassword = originalPassword;
   }

   public List<User> getUsers () {
      return users;
   }

   public void setUsers (List<User> users) {
      this.users = users;
   }

   public String getConfirmPassword () {
      return confirmPassword;
   }

   public void setConfirmPassword (String confirmPassword) {
      this.confirmPassword = confirmPassword;
   }

   /**
    * @return the username
    */
   public String getUsername () {
      return username;
   }

   /**
    * @param username
    *           the username to set
    */
   public void setUsername (String username) {
      this.username = username;
   }

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }

}
