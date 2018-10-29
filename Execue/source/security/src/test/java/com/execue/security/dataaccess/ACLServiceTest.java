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


package com.execue.security.dataaccess;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.ExecueBasePermissionType;
import com.execue.security.bean.ExecueUserDetails;

public class ACLServiceTest extends SecurityCommonBaseTest {

   @BeforeClass
   public static void setUp () {
      baseSetup();
      setSecurityAuthentication(1L);
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
      SecurityContextHolder.clearContext();
   }

   // @Test
   public void testAddAssetObjectPremission () {
      Asset asset = populateAsset();
      getAclService().setAssetObjectPermissions(asset, "ROLE_USER", false);
   }
   
   // @Test
   public void testReadAccess() {
      //create new ace for asset
      //testCreateACL();
      testAssetAccess();
   }
   
   //@Test
   public void testCreateACL () {
      Asset asset = populateAsset(105L);
      ObjectIdentity objectIdentity = new ObjectIdentityImpl(asset.getClass().getCanonicalName(), asset.getId());
      MutableAcl acl = getMutableACLService().createAcl(objectIdentity);
      Sid sid = new GrantedAuthoritySid("ROLE_USER");
      acl.insertAce(0, BasePermission.READ, sid , true);
      getMutableACLService().updateAcl(acl);
      System.out.println("done");
   }
   
   //@Test
   @SuppressWarnings ("unchecked")
   public void testAssetAccess() {
      List<Asset> assets = getAssets();
      assets = (List<Asset>) getAclService().applyAcl(assets);
      System.out.println("Asset list after security filter: ");
      for (Asset asset : assets) {
         System.out.println(asset.getId());
      }
   }

   //@Test
   public void testReadACL () {
      Asset asset = populateAsset(104L);
      ObjectIdentity objectIdentity = new ObjectIdentityImpl(asset.getClass().getCanonicalName(), asset.getId());
      Acl acl = getMutableACLService().readAclById(objectIdentity);
      List<AccessControlEntry> entries = acl.getEntries();
      for (AccessControlEntry accessControlEntry : entries) {
         System.out.println(accessControlEntry.getSid() + " Permission: "
                  + accessControlEntry.getPermission().toString());
      }
   }

   //@Test
   public void testUpdateACL () {
      Asset asset = populateAsset(104L);
      ObjectIdentity objectIdentity = new ObjectIdentityImpl(asset.getClass().getCanonicalName(), asset.getId());
      MutableAcl acl = (MutableAcl) getMutableACLService().readAclById(objectIdentity);
      acl.updateAce(0, BasePermission.DELETE);
      getMutableACLService().updateAcl(acl);
   }

   // @Test
   public void testRemoveAssetObjectPermissions () {
      Asset asset = populateAsset();
      getAclService().removeAssetObjectPermissions(asset);
   }

   // @Test
   public void testReadAllRolesByObject () {
      List<String> roleNames = getAclService().readAllRolesByObject(populateAsset(1013L));
      System.out.println(roleNames);
   }

   @Test
   public void testReadUniquePermissionTypeOnObjectForRole () {
      List<Asset> securityObjects = new ArrayList<Asset>();
      securityObjects.add(populateAsset(1013L));
      securityObjects.add(populateAsset(1014L));
      securityObjects.add(populateAsset(1015L));
      securityObjects.add(populateAsset(1015L));

      String roleName = "ROLE_ADMIN";

      List<ExecueBasePermissionType> uniquePermissionTypes = getAclService().readUniquePermissionTypesOnObjectForRole(
               securityObjects, roleName);
      
      System.out.println(uniquePermissionTypes);
   }

   public List<Asset> getAssets () {
      List<Asset> assets = new ArrayList<Asset>();
      assets.add(populateAsset(105L));
      assets.add(populateAsset(104L));
      return assets;
   }

   private static void setSecurityAuthentication (Long userId) {
      User user = new User();
      user.setId(userId);
      user.setFirstName("Admin");
      user.setLastName("Admin");

      List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
      GrantedAuthority authority = new GrantedAuthorityImpl("ROLE_USER");
      authorities.add(authority);

      ExecueUserDetails execueUserDetails = new ExecueUserDetails(user, authorities);
      Authentication authentication = new PreAuthenticatedAuthenticationToken(execueUserDetails, user.getLastName(),
               authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);

   }
}
