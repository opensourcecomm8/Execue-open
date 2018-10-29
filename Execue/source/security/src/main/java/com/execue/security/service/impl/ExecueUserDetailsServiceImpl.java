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


package com.execue.security.service.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.core.common.bean.security.SecurityRoles;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.security.bean.ExecueUserDetails;
import com.execue.swi.service.IUserManagementService;

public class ExecueUserDetailsServiceImpl implements UserDetailsService {

   @Autowired
   private IUserManagementService userManagementService;

   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException, DataAccessException {
      User user = null;
      try {
         try {
            user = userManagementService.getUserForAuthentication(username);
         } catch (Exception e) {
         }
         if (user == null) {
            throw new UsernameNotFoundException("user not found.");
         }
         List<SecurityRoles> userRoles = new ArrayList<SecurityRoles>();
         Set<SecurityGroups> userGroups = user.getGroups();
         if (userGroups != null) {
            for (SecurityGroups eachGroup : userGroups) {

               Set<SecurityRoles> groupRoles = eachGroup.getRoles();

               if (groupRoles != null) {
                  for (SecurityRoles eachRole : groupRoles) {
                     userRoles.add(eachRole);
                  }
               }
            }
         }
         if (userRoles.size() <= 0) {
            throw new UsernameNotFoundException("user not found.");
         }
         List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
         
         for (SecurityRoles eachRole : userRoles) {
            grantedAuthorities.add(new GrantedAuthorityImpl(eachRole.getName()));
         }
         user = ExecueBeanCloneUtil.cloneUser(user);
         UserDetails userDetails = new ExecueUserDetails(user, grantedAuthorities);
         return userDetails;
      } catch (Exception e) {
         throw new UsernameNotFoundException("user not found.");
      }
   }

}
