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


package com.execue.platform.swi.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.core.common.bean.security.SecurityRoles;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.swi.IAccessControlPublishService;
import com.execue.security.bean.ExecueUserDetails;
import com.execue.security.service.IAclService;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IUserManagementService;

/**
 * @author john
 */
public class AccessControlPublishServiceImpl implements IAccessControlPublishService {

   private IAclService              aclService;
   private IUserManagementService   userManagementService;
   private ISWIConfigurationService swiConfigurationService;

   public IUserManagementService getUserManagementService () {
      return userManagementService;
   }

   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public void publishAccessControl (Long userId, Asset asset, PublishAssetMode oldAssetMode) throws SWIException {
      // get the application
      Application application = asset.getApplication();
      publishAccessControl(userId, application, asset, oldAssetMode);
   }

   public void publishAccessControl (Long userId, Application application, Asset asset, PublishAssetMode oldAssetMode)
            throws SWIException {
      // String userName = getUserManagementService().getUserById(userId).getUsername();
      // based on the user selection: Community or Local, add entries into the ACL related tables
      PublishAssetMode publishMode = asset.getPublishMode();

      if (oldAssetMode == publishMode) {
         // noting to change
         return;
      }
      //TODO-JT- We need to reset SecurityAuthentication because in  job flow Security Context become null and and internal implementation of IAclService trying to fetch Authentication from SecurityContextHolder,We need to see best possible solution for that.  
      setSecurityAuthentication(userId);
      getAclService().removeAssetObjectPermissions(asset);
      if (PublishAssetMode.COMMUNITY.equals(publishMode)) {
         List<String> community_roles = getSwiConfigurationService().getPublishCommunityAclRoles();
         for (String roleName : community_roles) {
            getAclService().setAssetObjectPermissions(asset, roleName, false);
         }
         // getExecueAclService().setApplicationObjectPermissions(application, roleName, false);
      } else if (PublishAssetMode.LOCAL.equals(publishMode)) {
         // set the object permission by user name using Principal
         getAclService().setAssetObjectPermissions(asset, String.valueOf(userId), true);
         // TODO- KA - Entry for system user to have access
         getAclService().setAssetObjectPermissions(asset, "1", true);
         // getExecueAclService().setApplicationObjectPermissions(application, userName, true);
      }

   }

   private void setSecurityAuthentication (Long userId) {
      try {
         User user = getUserManagementService().getUserForAuthenticationByUserId(userId);
         Set<SecurityGroups> groups = user.getGroups();
         Set<String> userRoles = new HashSet<String>();
         getUserRoles(groups, userRoles);
         List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
         if (ExecueCoreUtil.isCollectionNotEmpty(userRoles)) {
            for (String role : userRoles) {
               GrantedAuthority authority = new GrantedAuthorityImpl(role);
               authorities.add(authority);
            }
         }
         //TODO: -KA- CHECK IF ROLE_ADMIN HAVE TO BE GRANTED WHEN NO ROLE IS ASSINGED TO USER ?
         if (ExecueCoreUtil.isCollectionEmpty(authorities)) {
            GrantedAuthority authority = new GrantedAuthorityImpl(ExecueConstants.ROLE_ADMIN);
            authorities.add(authority);
         }

         ExecueUserDetails execueUserDetails = new ExecueUserDetails(user, authorities);
         Authentication authentication = new PreAuthenticatedAuthenticationToken(execueUserDetails, user.getLastName(),
                  authorities);
         SecurityContextHolder.getContext().setAuthentication(authentication);

      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   private void getUserRoles (Set<SecurityGroups> groups, Set<String> userRoles) {
      for (SecurityGroups securityGroups : groups) {
         Set<SecurityRoles> roles = securityGroups.getRoles();
         for (SecurityRoles securityRoles : roles) {
            userRoles.add(securityRoles.getName());
         }
      }
   }

   /**
    * @return the aclService
    */
   public IAclService getAclService () {
      return aclService;
   }

   /**
    * @param aclService the aclService to set
    */
   public void setAclService (IAclService aclService) {
      this.aclService = aclService;
   }

}
