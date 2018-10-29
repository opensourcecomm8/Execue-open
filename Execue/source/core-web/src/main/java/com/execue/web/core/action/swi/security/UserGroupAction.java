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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.core.common.bean.security.User;
import com.execue.core.exception.HandlerException;
import com.execue.handler.swi.IGroupsHandler;

public class UserGroupAction extends BaseUserAction {

   private IGroupsHandler       groupsHandler;
   private List<SecurityGroups> groups;
   private List<User>           users;
   private List<Long>           selectedGroups;
   private static Logger        logger = Logger.getLogger(UserGroupAction.class);

   public String showUserGroups () {
      try {
         groups = groupsHandler.getAllGroups();
         if (user != null && user.getId() != null) {
            user = usersHandler.getUserAndGroups(user.getId());
            if (user != null && user.getGroups() == null) {
               user.setGroups(new HashSet<SecurityGroups>());
            }
            List<SecurityGroups> existingUserGroups = new ArrayList<SecurityGroups>();
            for (SecurityGroups userSecurityGroup : user.getGroups()) {
               for (SecurityGroups allSecurityGroup : groups) {
                  if (userSecurityGroup.getId().equals(allSecurityGroup.getId())) {
                     existingUserGroups.add(allSecurityGroup);
                  }
               }
            }
            groups.removeAll(existingUserGroups);
         }
         users = usersHandler.getUsers();
      } catch (HandlerException e) {
         e.printStackTrace();
         return ERROR;
      }
      return SUCCESS;
   }

   public String assignGroups () {
      try {

         user = usersHandler.getUserById(user.getId());
         List<SecurityGroups> gList = new ArrayList<SecurityGroups>();
         for (Long gid : selectedGroups) {
            SecurityGroups group = new SecurityGroups();
            group.setId(gid);
            gList.add(group);
         }
         usersHandler.assignGroup(user, gList);
         addActionMessage(getText("execue.users.group.update.success", new String[] { user.getUsername() }));
      } catch (HandlerException e1) {
         e1.printStackTrace();
         return ERROR;
      }

      return SUCCESS;
   }

   public IGroupsHandler getGroupsHandler () {
      return groupsHandler;
   }

   public void setGroupsHandler (IGroupsHandler groupsHandler) {
      this.groupsHandler = groupsHandler;
   }

   public List<SecurityGroups> getGroups () {
      return groups;
   }

   public void setGroups (List<SecurityGroups> groups) {
      this.groups = groups;
   }

   public List<User> getUsers () {
      return users;
   }

   public void setUsers (List<User> users) {
      this.users = users;
   }

   public List<Long> getSelectedGroups () {
      return selectedGroups;
   }

   public void setSelectedGroups (List<Long> selectedGroups) {
      this.selectedGroups = selectedGroups;
   }
}