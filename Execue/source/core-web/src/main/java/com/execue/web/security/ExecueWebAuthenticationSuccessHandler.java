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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.execue.core.common.type.AuditLogType;
import com.execue.platform.audittrail.IAuditTrailWrapperService;
import com.execue.platform.exception.PlatformException;
import com.execue.security.bean.ExecueUserDetails;
import com.execue.security.util.ExecueSecurityUitl;
import com.execue.web.core.bean.MenuSelection;
import com.execue.web.core.helper.MenuHelper;
import com.execue.web.core.util.ExecueWebConstants;
import com.googlecode.jsonplugin.JSONUtil;

public class ExecueWebAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

   private static final Logger       logger = Logger.getLogger(ExecueWebAuthenticationSuccessHandler.class);

   private IAuditTrailWrapperService auditTrailWrapperService;

   @Override
   public void onAuthenticationSuccess (HttpServletRequest request, HttpServletResponse response,
            Authentication authResult) throws IOException {
      // set menu changes
      MenuSelection selection = (MenuSelection) request.getSession().getAttribute(
               ExecueWebConstants.CURRENT_MENU_SESSION_ATTRIBUTE);
      if (selection == null) {
         selection = new MenuSelection();
      }
      MenuHelper.populateDefaultMenuType(selection);

      //Call audit log service
      if (authResult != null) {
         ExecueUserDetails execueUserDetail = (ExecueUserDetails) authResult.getPrincipal();
         try {
            getAuditTrailWrapperService().prepareAndPersistUserAccessAuditInfo(execueUserDetail.getUser(),
                     AuditLogType.LOGIN, request.getRemoteAddr());
         } catch (PlatformException platformException) {
            platformException.printStackTrace();
            logger.error(platformException);
         }
      }

      if (isAjaxRequest(request)) {
         AjaxLogin login = getLoginObject();
         if (request.getParameter("noRedirect") != null) {
            login.setUrl("");
         } else {
            login.setUrl(determineTargetUrl(request, response));
         }
         sendJsonResponse(login, response);
      } else {
         getRedirectStrategy().sendRedirect(request, response, determineTargetUrl(request, response));
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

}
