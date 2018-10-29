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


package com.execue.security.service;

import java.util.List;

import com.execue.core.IService;
import com.execue.core.common.bean.ISecurityBean;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.security.ExecueAccessControlEntry;
import com.execue.core.common.type.ExecueBasePermissionType;

public interface IAclService extends IService {

   /* 
    public void addObjectPermissions (ISecurityBean object, String roleName, Class clazz);
    
    public void removeObjectPermissions (ISecurityBean object, String roleName, Class clazz);
    */

   public void setAssetObjectPermissions (Asset asset, String roleNameOrUserId, boolean isUser);

   public void setApplicationObjectPermissions (Application app, String roleNameOrUserId, boolean isUser);

   public void removeAssetObjectPermissions (Asset asset);

   public void removeApplicationObjectPermissions (Asset asset);
   
   public void removeObjectPermissions (ISecurityBean object);

   public List<ExecueAccessControlEntry> readAclEntities (ISecurityBean object);

   public List<? extends ISecurityBean> applyAcl (List<? extends ISecurityBean> objectList);

   /*
   public void getAssetObjectPermissions (ISecurityBean object);
    */

   /**
    * Get all the permission on this object and for the role given
    * if there is any with Grant or Revoke (meaning Mask being READ or DELETE) then it means there exists an entry for this object and role combination
    * 
    * if entry exists,
    *    then
    *       if existing entry is same as the request information
    *          then
    *             just return, do not do any thing
    *       else
    *          update the entry as requested, only when overridePermissionflag is true
    *       end if;
    * else
    *    create a new acl entry with what is requested 
    * end if;
    */
   public void createUpdatePermissionByRole (ISecurityBean securityObject, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission);
   
   /**
    * Read all the roles that are existing in ACL irrespective of the permission (GRANT/REVOKE)
    * 
    * @param securityObject
    * @return List of ROLE Names that the security object is defined definition for
    */
   public List<String> readAllRolesByObject(ISecurityBean securityObject);

   public List<ExecueBasePermissionType> readUniquePermissionTypesOnObjectForRole (
            List<? extends ISecurityBean> securityObjectList, String roleName);

}
