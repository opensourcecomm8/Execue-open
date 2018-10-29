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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.execue.security.bean.ExecueUserDetails;
import com.execue.web.core.bean.MenuSelection;
import com.execue.web.core.helper.MenuHelper;
import com.execue.web.core.util.ExecueWebConstants;
import com.googlecode.jsonplugin.JSONUtil;

public class ExecueAuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter {
/*
   private static Logger logger = Logger.getLogger(ExecueAuthenticationProcessingFilter.class);

   @Override
   protected void onSuccessfulAuthentication (HttpServletRequest request, HttpServletResponse response,
            Authentication authResult) throws IOException {
      // set menu changes
      MenuSelection selection = (MenuSelection) request.getSession().getAttribute(ExecueWebConstants.CURRENT_MENU_SESSION_ATTRIBUTE);
      if (selection == null) {
         selection = new MenuSelection();
      }
      MenuHelper.populateDefaultMenuType(selection);
      
      if (isAjaxRequest(request)) {
         AjaxLogin login = getLoginObject();
         if (request.getParameter("noRedirect") != null) {
            login.setUrl("");
         } else {
            login.setUrl(determineTargetUrl(request));
         }
         sendJsonResponse(login, response);
      }
   }

   @Override
   protected void onUnsuccessfulAuthentication (HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException {
      if (isAjaxRequest(request)) {
         AjaxLogin login = getLoginObject();
         login.setErrMsg(((AuthenticationException) request.getSession().getAttribute(
                  AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY)).getMessage());
         sendJsonResponse(login, response);
      }
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

   protected AjaxLogin getLoginObject () {
      AjaxLogin login = new AjaxLogin();

      try {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication != null) {
            // We should populate user's full name (first name and last name ) by ajax login.
            ExecueUserDetails userDetails = (ExecueUserDetails) authentication.getPrincipal();
            // login.setName(authentication.getName());
            login.setName(userDetails.getFullName());
            GrantedAuthority[] roles = authentication.getAuthorities();
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

   protected void sendRedirect (HttpServletRequest request, HttpServletResponse response, String url)
            throws IOException {
      if (!isAjaxRequest(request)) {
         super.sendRedirect(request, response, url);
      }
   }

   private boolean isAjaxRequest (HttpServletRequest request) {
      return request.getParameter("ajax") != null;
   }
   */
}
