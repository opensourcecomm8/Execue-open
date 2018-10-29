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

import java.util.List;

import com.execue.core.common.bean.security.SecurityRoles;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.handler.swi.IRolesHandler;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IUserManagementService;

public class RolesHandlerImpl implements IRolesHandler {

   private IUserManagementService userManagementService;

   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   public Long createRole (SecurityRoles role) throws HandlerException {
      try {
         return userManagementService.createRole(role);
      } catch (SWIException e) {
         if (e.getCode() == ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS) {
            throw new HandlerException(ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS, e.getMessage());
         } else {
            throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
         }
      }
   }

   public SecurityRoles getRoleById (Long id) throws HandlerException {
      try {
         return userManagementService.getRoleById(id);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<SecurityRoles> getRoles (String status) throws HandlerException {
      try {
         return userManagementService.getRoles(status);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updateRole (SecurityRoles role) throws HandlerException {
      try {
         userManagementService.updateRole(role);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<SecurityRoles> getAllRoles () throws HandlerException {
      try {
         return userManagementService.getAllRoles();
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void deleteRole (SecurityRoles role) throws HandlerException {
      try {
         userManagementService.deleteSecurityRole(role);
      } catch (SWIException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }
}
