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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.AuditLogType;
import com.execue.platform.audittrail.IAuditTrailWrapperService;
import com.execue.platform.exception.PlatformException;
import com.execue.security.bean.ExecueUserDetails;
import com.execue.security.util.ExecueSecurityUitl;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IUserManagementService;
import com.googlecode.jsonplugin.JSONUtil;

public class ExecueWebAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

   private static final Logger       logger = Logger.getLogger(ExecueWebAuthenticationFailureHandler.class);

   private IAuditTrailWrapperService auditTrailWrapperService;
   private IUserManagementService    userManagementService;

   @Override
   public void onAuthenticationFailure (HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
      //TODO-JT- need to find an alternate for 
      UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) authException
               .getAuthentication();
      try {
         User userForAuthentication = getUserManagementService().getUserForAuthentication(
                  user.getPrincipal().toString());
         if (userForAuthentication != null) {
            getAuditTrailWrapperService().prepareAndPersistUserAccessAuditInfo(userForAuthentication,
                     AuditLogType.LOGIN_FAILURE, request.getRemoteAddr());
         } else {
            if (logger.isDebugEnabled()) {
               logger.debug("User not found");
            }
         }

      } catch (PlatformException platformException) {
         platformException.printStackTrace();
         logger.error(platformException);
      } catch (SWIException swiException) {
         swiException.printStackTrace();
         logger.error(swiException);
      }

      if (isAjaxRequest(request)) {
         AjaxLogin login = getLoginObject();
         login.setErrMsg(authException.getMessage());
         sendJsonResponse(login, response);
      } else {
         super.onAuthenticationFailure(request, response, authException);
      }
   }

   private boolean isAjaxRequest (HttpServletRequest request) {
      return request.getParameter("ajax") != null;
   }

   protected AjaxLogin getLoginObject () {
      AjaxLogin login = new AjaxLogin();

      try {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication != null) {
            // We should populate user's full name (first name and last name ) by ajax login.
            ExecueUserDetails userDetails = (ExecueUserDetails) authentication.getPrincipal();
            // login.setName(authentication.getName());
            login.setName(userDetails.getFullName());
            List<GrantedAuthority> roles = ExecueSecurityUitl.getAuthoritiesFromContext();
            for (GrantedAuthority role : roles) {
               if ("ROLE_ADMIN".equals(role.getAuthority())) {
                  login.setAdmin(true);
               }
            }
            // ExecueUserDetails userDetails = (ExecueUserDetails) authentication.getPrincipal();
         } else {
            login.setName("User");
         }
      } catch (Exception e) {
         logger.error("Error: while retriving object from spring security ", e);
      }
      return login;
   }

   private void sendJsonResponse (AjaxLogin login, HttpServletResponse response) throws IOException {
      response.setContentType("text/plain;charset=utf-8");

      response.setHeader("Cache-Control", "no-cache");
      response.setDateHeader("Expires", 0);
      response.setHeader("Pragma", "no-cache");
      String jsonString = "";
      try {
         jsonString = JSONUtil.serialize(login);
         if (logger.isDebugEnabled()) {
            logger.debug(jsonString);
         }
      } catch (Exception e) {
      }
      response.getOutputStream().write(jsonString.getBytes("UTF-8"));
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

   /**
    * @return the userManagementService
    */
   public IUserManagementService getUserManagementService () {
      return userManagementService;
   }

   /**
    * @param userManagementService the userManagementService to set
    */
   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }
}
