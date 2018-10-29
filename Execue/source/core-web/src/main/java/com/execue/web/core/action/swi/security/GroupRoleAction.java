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

import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.core.common.bean.security.SecurityRoles;
import com.execue.core.exception.HandlerException;
import com.execue.handler.swi.IRolesHandler;

public class GroupRoleAction extends BaseGroupAction {

   private IRolesHandler       rolesHandler;
   private List<SecurityRoles>  roles;
   private List<SecurityGroups> groups;
   private List<Long>          selectedRoles;

   public String showGroupRoles () {
      try {
         roles = rolesHandler.getAllRoles();
         if (group != null && group.getId() != null) {
            group = groupsHandler.getGroupAndRoles(group.getId());
            if (group.getRoles() == null) {
               group.setRoles(new HashSet<SecurityRoles>());
            }
            List<SecurityRoles> existingGroupRoles = new ArrayList<SecurityRoles>();
            for (SecurityRoles securityGroup : group.getRoles()) {
               for (SecurityRoles allSecurityRole : roles) {                 
                  if (securityGroup.getId().equals(allSecurityRole.getId())) {
                     existingGroupRoles.add(allSecurityRole);
                  }
               }
            }
            roles.removeAll(existingGroupRoles);           
         }
         groups = groupsHandler.getAllGroups();
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String assignRoles () {
      List<SecurityRoles> rList = new ArrayList<SecurityRoles>();
      for (Long gid : selectedRoles) {
         SecurityRoles role = new SecurityRoles();
         role.setId(gid);
         rList.add(role);
      }
      try {
         group = groupsHandler.getGroupById(group.getId());
         groupsHandler.assignGroupRoles(group, rList);
         addActionMessage(getText("execue.roles.update.success", new String[] { group.getName()}));
      } catch (NumberFormatException e1) {
         e1.printStackTrace();
      } catch (HandlerException e1) {
         e1.printStackTrace();
      }

      return SUCCESS;
   }

   public IRolesHandler getRolesHandler () {
      return rolesHandler;
   }

   public List<SecurityRoles> getRoles () {
      return roles;
   }

   public List<SecurityGroups> getGroups () {
      return groups;
   }

   public void setRolesHandler (IRolesHandler rolesHandler) {
      this.rolesHandler = rolesHandler;
   }

   public void setRoles (List<SecurityRoles> roles) {
      this.roles = roles;
   }

   public void setGroups (List<SecurityGroups> groups) {
      this.groups = groups;
   }

   public List<Long> getSelectedRoles () {
      return selectedRoles;
   }

   public void setSelectedRoles (List<Long> selectedRoles) {
      this.selectedRoles = selectedRoles;
   }

}
