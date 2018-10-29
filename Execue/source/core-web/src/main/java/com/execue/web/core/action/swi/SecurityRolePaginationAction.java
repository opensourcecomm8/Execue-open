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


package com.execue.web.core.action.swi;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.security.SecurityRoles;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.swi.IRolesHandler;

public class SecurityRolePaginationAction extends SWIPaginationAction {

   private List<SecurityRoles> roles;

   private IRolesHandler       rolesHandler;
   private static final int    PAGE_SIZE       = 9;
   private static final int    NUMBER_OF_LINKS = 4;

   @Override
   public String processPage () throws ExeCueException {
      try {
         getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
         getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
         List<SecurityRoles> allRoles = rolesHandler.getAllRoles();
         List<SecurityRoles> filteredAdminRoleList = filterAdminRole(allRoles);
         // set the record count
         getPageDetail().setRecordCount(Long.valueOf(filteredAdminRoleList.size()));
         roles = getProcessedResults(filteredAdminRoleList, getPageDetail());
         // push the page object into request
         getHttpRequest().put(PAGINATION, getPageDetail());
      } catch (Exception exception) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
      return SUCCESS;
   }

   private List<SecurityRoles> getProcessedResults (List<SecurityRoles> listOfRoles, Page pageDetail) {
      pageDetail.setRecordCount(Long.valueOf(listOfRoles.size()));
      List<SecurityRoles> pageRoles = new ArrayList<SecurityRoles>();
      int start = (pageDetail.getRequestedPage().intValue() - 1) * pageDetail.getPageSize().intValue();
      int end = start + pageDetail.getPageSize().intValue();
      if (end > pageDetail.getRecordCount().intValue()) {
         end = (pageDetail.getRecordCount().intValue());
      }
      for (int i = start; i < end; i++) {
         pageRoles.add(listOfRoles.get(i));
      }
      return pageRoles;
   }

   private List<SecurityRoles> filterAdminRole (List<SecurityRoles> allRoles) {
      List<SecurityRoles> roles = new ArrayList<SecurityRoles>();
      if (ExecueCoreUtil.isCollectionNotEmpty(allRoles)) {
         for (SecurityRoles securityRole : allRoles) {
            if (!"ROLE_ADMIN".equalsIgnoreCase(securityRole.getName())) {
               roles.add(securityRole);
            }
         }
      }
      return roles;

   }

   /**
    * @return the rolesHandler
    */
   public IRolesHandler getRolesHandler () {
      return rolesHandler;
   }

   /**
    * @param rolesHandler the rolesHandler to set
    */
   public void setRolesHandler (IRolesHandler rolesHandler) {
      this.rolesHandler = rolesHandler;
   }

   /**
    * @return the roles
    */
   public List<SecurityRoles> getRoles () {
      return roles;
   }

   /**
    * @param roles the roles to set
    */
   public void setRoles (List<SecurityRoles> roles) {
      this.roles = roles;
   }

}
