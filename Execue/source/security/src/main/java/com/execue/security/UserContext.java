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


package com.execue.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.execue.core.common.bean.security.User;
import com.execue.security.bean.ExecueUserDetails;

/**
 * @author kaliki
 */
public class UserContext implements IUserContext {

   private static Logger logger = Logger.getLogger(UserContext.class);

   public User getUser () {
      try {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication != null) {
            ExecueUserDetails userDetails = (ExecueUserDetails) authentication.getPrincipal();
            if (userDetails != null) {
               return userDetails.getUser();
            }
         }
      } catch (Exception e) {
         logger.error("Error: while retriving object from spring security ", e);
      }
      // default to unknown user, set ID as -1
      if (logger.isDebugEnabled()) {
         logger.debug("user is null");
      }

      User user = new User();
      user.setId(-1L);
      return user;
   }

   public String getRole () {
      String role = null;
      try {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication != null) {
            ExecueUserDetails userDetails = (ExecueUserDetails) authentication.getPrincipal();
            if (userDetails != null) {
               if (userDetails.isAdmin()) {
                  role = "ROLE_ADMIN";
               } else if (userDetails.isPublisher()) {
                  role = "ROLE_PUBLISHER";
               }
            }
         }
      } catch (Exception e) {
         logger.error("Error: while retriving object from spring security ", e);
      }
      return role;
   }

   public List<String> getRoles () {
      try {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication != null) {
            ExecueUserDetails userDetails = (ExecueUserDetails) authentication.getPrincipal();
            return userDetails.getRoles();
         }
      } catch (Exception e) {
         logger.error("Error: while retriving object from spring security ", e);
      }
      return new ArrayList<String>(1);
   }

   public String getUsername () {
      User user = getUser();
      if (user != null) {
         return user.getUsername();
      } else {
         return "";
      }
   }

}
