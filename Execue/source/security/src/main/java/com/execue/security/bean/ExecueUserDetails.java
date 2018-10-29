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


package com.execue.security.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.execue.core.common.bean.security.User;

public class ExecueUserDetails implements UserDetails {

   private User               user;
   private List<GrantedAuthority> grantedAuthorities;

   public ExecueUserDetails (User user, List<GrantedAuthority> grantedAuthorities) {
      this.user = user;
      this.grantedAuthorities = grantedAuthorities;
   }

   public List<GrantedAuthority> getAuthorities () {
      return grantedAuthorities;
   }

   public String getPassword () {
      return user.getPassword();
   }

   // TODO: userid (long) object will be returned, as this will be used in ACL.
   // to get username user getUser.getUsername() 
   public String getUsername () {
      return String.valueOf(user.getId());
   }

   public boolean isAccountNonExpired () {
      // TODO Auto-generated method stub
      return true;
   }

   public boolean isAccountNonLocked () {
      // TODO Auto-generated method stub
      return true;
   }

   public boolean isCredentialsNonExpired () {
      return true;
   }

   public boolean isEnabled () {
      // TODO Auto-generated method stub
      return true;
   }

   public String getFullName () {
      return user.getFirstName() + " " + user.getLastName();
   }

   public User getUser () {
      return user;
   }

   public boolean isAdmin () {
      boolean isAdmin = false;
      for (GrantedAuthority role : grantedAuthorities) {
         if ("ROLE_ADMIN".equals(role.getAuthority())) {
            isAdmin = true;
            break;
         }
      }
      return isAdmin;
   }

   public boolean isPublisher () {
      boolean isPublisher = false;
      for (GrantedAuthority role : grantedAuthorities) {
         if ("ROLE_PUBLISHER".equals(role.getAuthority())) {
            isPublisher = true;
            break;
         }
      }
      return isPublisher;
   }

   public List<String> getRoles () {
      List<String> roles = new ArrayList<String>(1);
      for (GrantedAuthority role : grantedAuthorities) {
         roles.add(role.getAuthority());
      }
      return roles;
   }
   
   public String getSalt() {
      return user.getSalt();
   }
}
