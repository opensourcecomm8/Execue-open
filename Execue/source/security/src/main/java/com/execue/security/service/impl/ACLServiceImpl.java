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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import com.execue.core.common.bean.ISecurityBean;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.security.ExecueAccessControlEntry;
import com.execue.core.common.type.ExecueBasePermissionType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.security.service.IAclService;

public class ACLServiceImpl implements IAclService {

   private MutableAclService mutableAclService;

   private Logger            logger = Logger.getLogger(ACLServiceImpl.class);

   public ACLServiceImpl () {
   }

   public void addObjectPermissions (ISecurityBean object, String roleNameOrUserId, boolean isUser) {
      ObjectIdentity oid = new ObjectIdentityImpl(object.getClass().getCanonicalName(), object.getId());
      setPermission(oid, roleNameOrUserId, isUser);
   }

   public void removeObjectPermissions (ISecurityBean object, String roleName) {
   }

   public void removeObjectPermissions (ISecurityBean object) {
      ObjectIdentity oid = new ObjectIdentityImpl(object.getClass().getCanonicalName(), object.getId());
      mutableAclService.deleteAcl(oid, true);
   }

   public void setApplicationObjectPermissions (Application app, String roleNameOrUserId, boolean isUser) {
      ObjectIdentity oid = new ObjectIdentityImpl(app.getClass().getCanonicalName(), app.getId());
      setPermission(oid, roleNameOrUserId, isUser);
   }

   public void setAssetObjectPermissions (Asset asset, String roleNameOrUserId, boolean isUser) {
      ObjectIdentity oid = new ObjectIdentityImpl(asset.getClass().getCanonicalName(), asset.getId());
      setPermission(oid, roleNameOrUserId, isUser);

   }

   public void removeAssetObjectPermissions (Asset asset) {
      removeObjectPermissions(asset);
   }

   public void removeApplicationObjectPermissions (Asset asset) {
      removeObjectPermissions(asset);
   }

   @Override
   public List<ExecueAccessControlEntry> readAclEntities (ISecurityBean object) {
      List<ExecueAccessControlEntry> aclEntries = new ArrayList<ExecueAccessControlEntry>();
      ObjectIdentity objectIdentity = new ObjectIdentityImpl(object.getClass().getCanonicalName(), object.getId());
      try {
         List<AccessControlEntry> entries = getMutableAclService().readAclById(objectIdentity).getEntries();
         for (AccessControlEntry accessControlEntry : entries) {
            ExecueAccessControlEntry execueAccessControlEntry = new ExecueAccessControlEntry();
            if (logger.isDebugEnabled()) {
               logger.debug(accessControlEntry.getSid() + " Permission: "
                        + accessControlEntry.getPermission().toString());
            }
            GrantedAuthoritySid sid = (GrantedAuthoritySid) accessControlEntry.getSid();
            execueAccessControlEntry.setGrantedAuthority(sid.getGrantedAuthority());
            execueAccessControlEntry
                     .setBasePermissionType(getExecueBasePermissionType((BasePermission) accessControlEntry
                              .getPermission()));
            aclEntries.add(execueAccessControlEntry);
         }

      } catch (NotFoundException exception) {
         //Returning null in case no record found for given object id
         return null;
      }
      return aclEntries;

   }

   @Secured ( { "AFTER_ACL_COLLECTION_READ" })
   public List<? extends ISecurityBean> applyAcl (List<? extends ISecurityBean> objectList) {
      return objectList;
   }

   public void createUpdatePermissionByRole (ISecurityBean securityObject, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) {
      ObjectIdentity objectIdentity = new ObjectIdentityImpl(securityObject.getClass().getCanonicalName(),
               securityObject.getId());
      BasePermission basePermission = getBasePermission(permissionType);
      MutableAcl acl;
      boolean existingEntryFound = false;
      try {
         acl = (MutableAcl) mutableAclService.readAclById(objectIdentity);
         List<AccessControlEntry> entries = acl.getEntries();
         int aclEntryIndex = 0;
         for (AccessControlEntry accessControlEntry : entries) {
            if (((GrantedAuthoritySid) accessControlEntry.getSid()).getGrantedAuthority().equals(roleName)) {
               existingEntryFound = true;
               if (!((BasePermission) accessControlEntry.getPermission()).equals(basePermission) && overridePermission) {
                  acl.updateAce(aclEntryIndex, basePermission);
                  mutableAclService.updateAcl(acl);
               }
               break;
            }
            aclEntryIndex++;
         }

         if (!existingEntryFound) {
            Sid sid = new GrantedAuthoritySid(roleName);
            acl.insertAce(acl.getEntries().size(), basePermission, sid, true);
            mutableAclService.updateAcl(acl);
         }
      } catch (NotFoundException nfe) {
         acl = mutableAclService.createAcl(objectIdentity);
         Sid sid = new GrantedAuthoritySid(roleName);
         acl.insertAce(acl.getEntries().size(), basePermission, sid, true);
         mutableAclService.updateAcl(acl);
      }

   }

   @Override
   public List<String> readAllRolesByObject (ISecurityBean securityObject) {
      List<String> roleNames = new ArrayList<String>();
      try {
         ObjectIdentity objectIdentity = new ObjectIdentityImpl(securityObject.getClass().getCanonicalName(),
                  securityObject.getId());
         MutableAcl acl = (MutableAcl) mutableAclService.readAclById(objectIdentity);
         for (AccessControlEntry accessControlEntry : acl.getEntries()) {
            roleNames.add(((GrantedAuthoritySid) accessControlEntry.getSid()).getGrantedAuthority());
         }
      } catch (NotFoundException nfe) {
         // Do nothing, this means there are no entries existing for this Object Identity
      }
      return roleNames;
   }

   @SuppressWarnings ("unchecked")
   public List<ExecueBasePermissionType> readUniquePermissionTypesOnObjectForRole (
            List<? extends ISecurityBean> securityObjectList, String roleName) {
      Set<ExecueBasePermissionType> uniquePermissionTypes = null;
      try {
         Map<ObjectIdentity, Acl> objectIdentifierAclMap = mutableAclService
                  .readAclsById(getObjectIdentities(securityObjectList));
         uniquePermissionTypes = getUniquePermissionTypes(roleName, objectIdentifierAclMap);
      } catch (NotFoundException nfe) {
         // Do nothing, this means there are no entries existing for this Object Identity
      }
      return new ArrayList<ExecueBasePermissionType>(uniquePermissionTypes);
   }

   /**
    * @param uniquePermissionTypes
    * @param objectIdentifierAclMap
    */
   private Set<ExecueBasePermissionType> getUniquePermissionTypes (String roleName,
            Map<ObjectIdentity, Acl> objectIdentifierAclMap) {
      Set<ExecueBasePermissionType> uniquePermissionTypes = new HashSet<ExecueBasePermissionType>();
      if (ExecueCoreUtil.isMapEmpty(objectIdentifierAclMap)) {
         return uniquePermissionTypes;
      }
      for (Acl acl : objectIdentifierAclMap.values()) {
         for (AccessControlEntry accessControlEntry : acl.getEntries()) {
            if (((GrantedAuthoritySid) accessControlEntry.getSid()).getGrantedAuthority().equals(roleName)) {
               uniquePermissionTypes.add(getExecueBasePermissionType((BasePermission) accessControlEntry
                        .getPermission()));
            }
         }
      }
      return uniquePermissionTypes;
   }

   private List<ObjectIdentity> getObjectIdentities (List<? extends ISecurityBean> securityObjectList) {
      List<ObjectIdentity> objectIdentities = new ArrayList<ObjectIdentity>();
      for (ISecurityBean securityBean : securityObjectList) {
         objectIdentities.add(getObjectIdentity(securityBean));
      }
      return objectIdentities;
   }

   private ObjectIdentity getObjectIdentity (ISecurityBean securityObject) {
      return new ObjectIdentityImpl(securityObject.getClass().getCanonicalName(), securityObject.getId());
   }

   private Sid[] getGrantedAuthoritySids (List<String> roleNames) {
      Sid[] sids = new Sid[roleNames.size()];
      int index = 0;
      for (String roleName : roleNames) {
         sids[index++] = getGrantedAuthoritySid(roleName);
      }
      return sids;
   }

   private Sid[] getGrantedAuthoritySids (String roleName) {
      Sid[] sids = new Sid[1];
      sids[0] = getGrantedAuthoritySid(roleName);
      return sids;
   }

   private Sid getGrantedAuthoritySid (String roleName) {
      return new GrantedAuthoritySid(roleName);
   }

   public void setBusinessTermObjectPermissions (ISecurityBean object, String roleName, Class clazz) {
   }

   public void getAssetObjectPermissions (ISecurityBean object) {
      // TODO Auto-generated method stub

   }

   public MutableAclService getMutableAclService () {
      return mutableAclService;
   }

   public void setMutableAclService (MutableAclService mutableAclService) {
      this.mutableAclService = mutableAclService;
   }

   private BasePermission getBasePermission (ExecueBasePermissionType execueBasePermissionType) {
      Permission basePermission = BasePermission.READ;
      if (ExecueBasePermissionType.WRITE.equals(execueBasePermissionType)) {
         basePermission = BasePermission.WRITE;
      } else if (ExecueBasePermissionType.CREATE.equals(execueBasePermissionType)) {
         basePermission = BasePermission.CREATE;
      } else if (ExecueBasePermissionType.REVOKE.equals(execueBasePermissionType)) {
         basePermission = BasePermission.DELETE;
      } else if (ExecueBasePermissionType.ADMINISTRATION.equals(execueBasePermissionType)) {
         basePermission = BasePermission.ADMINISTRATION;
      }
      return (BasePermission) basePermission;
   }

   private ExecueBasePermissionType getExecueBasePermissionType (BasePermission permission) {
      ExecueBasePermissionType basePermissionType = ExecueBasePermissionType.GRANT;
      if (BasePermission.WRITE.equals(permission)) {
         basePermissionType = ExecueBasePermissionType.WRITE;
      } else if (BasePermission.CREATE.equals(permission)) {
         basePermissionType = ExecueBasePermissionType.CREATE;
      } else if (BasePermission.DELETE.equals(permission)) {
         basePermissionType = ExecueBasePermissionType.REVOKE;
      } else if (BasePermission.ADMINISTRATION.equals(permission)) {
         basePermissionType = ExecueBasePermissionType.ADMINISTRATION;
      }
      return basePermissionType;
   }

   private void setPermission (ObjectIdentity oid, String roleNameOrUserId, boolean isUser) {
      MutableAcl acl;

      try {
         acl = (MutableAcl) mutableAclService.readAclById(oid);
      } catch (NotFoundException nfe) {
         acl = mutableAclService.createAcl(oid);
      }

      Sid sid = null;
      if (isUser) {
         sid = new PrincipalSid(roleNameOrUserId);
      } else {
         sid = new GrantedAuthoritySid(roleNameOrUserId);
      }

      acl.insertAce(acl.getEntries().size(), BasePermission.READ, sid, true);
      mutableAclService.updateAcl(acl);

      if (logger.isDebugEnabled()) {
         logger.debug("Added permission " + BasePermission.READ + " for Sid " + sid + " securedObject " + oid);
      }

   }

}
