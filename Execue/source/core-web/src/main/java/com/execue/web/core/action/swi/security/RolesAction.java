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

import org.apache.log4j.Logger;

import com.execue.core.common.bean.security.SecurityRoles;
import com.execue.core.common.type.BooleanType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.handler.swi.IRolesHandler;

public class RolesAction extends SecurityAction {

   private IRolesHandler      rolesHandler;
   private SecurityRoles       role;
   private List<SecurityRoles> roles;
   private static Logger      logger = Logger.getLogger(RolesAction.class);

   public SecurityRoles getRole () {
      return role;
   }

   public void setRole (SecurityRoles role) {
      this.role = role;
   }

   public List<SecurityRoles> getRoles () {
      return roles;
   }

   public void setRoles (List<SecurityRoles> roles) {
      this.roles = roles;
   }

   public IRolesHandler getRolesHandler () {
      return rolesHandler;
   }

   public void setRolesHandler (IRolesHandler rolesHandler) {
      this.rolesHandler = rolesHandler;
   }

   public String list () {
      try {
         roles = getRolesHandler().getAllRoles();
      } catch (Exception e) {
         e.printStackTrace();
         // TODO: populate the error message
      }
      return SUCCESS;
   }

   public String input () {
      if (role != null && role.getId() != null) {
         try {
            role = rolesHandler.getRoleById(role.getId());
         } catch (Exception e) {
            return ERROR;
         }
         if (logger.isDebugEnabled()) {
            logger.debug("role name " + role.getName());
         }
      }
      return INPUT;
   }

   public String save () {
      try {
         if (role.getId() == null) {
            //TODO-JT- Need to set system role.
            role.setSystemRole(BooleanType.YES);
            rolesHandler.createRole(role);
            addActionMessage(getText("execue.global.insert.success", new String[] { getText("execue.role.label") + " "
                     + role.getName() }));
         } else {    
            role.setSystemRole(BooleanType.YES);
            rolesHandler.updateRole(role);
            addActionMessage(getText("execue.global.update.success", new String[] { getText("execue.role.label") + " "
                     + role.getName() }));
         }
      } catch (HandlerException e) {
         if (e.getCode() == ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS) {
            addActionError(getText("execue.global.exist.message", new String[] { role.getName() }));
         } else {
            addActionError(getText("execue.global.error", new String[] { e.getMessage() }));
         }
         return ERROR;
      }
      return SUCCESS;
   }

   public String delete () {
      try {
         SecurityRoles securityRole = rolesHandler.getRoleById(role.getId());
         logger.debug("role Id  ::" + securityRole.getId());
         logger.debug("role Name ::" + securityRole.getName());
         logger.debug("role Description  ::" + securityRole.getDescription());
         rolesHandler.deleteRole(securityRole);
         //TODO-JT- Need to add action and eorror messages
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }
}
