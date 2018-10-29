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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class ExecueAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

   private String loginFormUrlForAjax;

   @Override
   protected String determineUrlToUseForThisRequest (HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) {
      if (request.getParameter("ajax") != null) {
         return loginFormUrlForAjax;
      } else {
         return super.determineUrlToUseForThisRequest(request, response, exception);
      }
   }

   public String getLoginFormUrlForAjax () {
      return loginFormUrlForAjax;
   }

   public void setLoginFormUrlForAjax (String loginFormUrlForAjax) {
      this.loginFormUrlForAjax = loginFormUrlForAjax;
   }

}
