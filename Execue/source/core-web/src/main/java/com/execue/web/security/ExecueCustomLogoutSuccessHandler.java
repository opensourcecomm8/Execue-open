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


package com.execue.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.execue.platform.audittrail.IAuditTrailWrapperService;

public class ExecueCustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

   private IAuditTrailWrapperService auditTrailWrapperService;

   @Override
   public void onLogoutSuccess (HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
      //NOTE-JT- We can delete this object later on, we are not using for now
      //Call audit log service
      //      if (authentication != null) {
      //         ExecueUserDetails execueUserDetail = (ExecueUserDetails) authentication.getPrincipal();
      //         try {
      //            getAuditTrailWrapperService().prepareAndPersistUserAccessAuditInfo(execueUserDetail.getUser(),
      //                     AuditLogType.LOGOUT, request.getRemoteAddr());
      //         } catch (PlatformException platformException) {
      //            platformException.printStackTrace();
      //            logger.error(platformException);
      //         }
      //      }

      setDefaultTargetUrl("/index.jsp");
      super.onLogoutSuccess(request, response, authentication);
   }

   /**
    * @return the auditTrailWrapperService
    */
   public IAuditTrailWrapperService getAuditTrailWrapperService () {
      return auditTrailWrapperService;
   }

   /**
    * @param auditTrailWrapperService the auditTrailWrapperService to set
    */
   public void setAuditTrailWrapperService (IAuditTrailWrapperService auditTrailWrapperService) {
      this.auditTrailWrapperService = auditTrailWrapperService;
   }
}