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


package com.execue.platform.security;

import java.util.List;

import com.execue.core.common.bean.ISecurityBean;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.type.ExecueBasePermissionType;
import com.execue.security.exception.SecurityException;

/**
 * Service to handle the needs of Defining Security 
 *    Also handles the security based filtering over objects defined with security 
 *       by wrapping over the AclService calls
 *  
 * @author gopal
 */
public interface ISecurityDefinitionWrapperService {

   /**
    * Apply the requested permission on the assets provided and on all their 
    *    child entities as well till the leaf level entity
    *    update happens only when overridePermission is true, else existing entry will not be touched
    *    
    * @param assets
    * @param roleName
    * @param permissionType
    * @throws SecurityException
    */
   public void applyRolePermissionOnAssetsWithPropagation (List<Asset> assets, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) throws SecurityException;

   /**
    * Apply the requested permission on the tables provided and on all their 
    *    child entities as well till the leaf level entity
    *    If table is of any lookup type then propagate the permission to all it's members as well
    *    update happens only when overridePermission is true, else existing entry will not be touched
    *    
    * @param tables
    * @param roleName
    * @param permissionType
    * @throws SecurityException
    */
   public void applyRolePermissionOnTablesWithPropagation (List<Tabl> tables, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) throws SecurityException;

   /**
    * Apply the requested permission on the columns provided
    * Look for other columns mapped to the concept that is mapped to this column
    *    apply the same permission on those columns as well
    *    update happens only when overridePermission is true, else existing entry will not be touched
    * 
    * @param columns
    * @param roleName
    * @param permissionType
    * @throws SecurityException
    */
   public void applyRolePermissionOnColumns (List<Colum> columns, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) throws SecurityException;

   /**
    * Apply the requested permission on the members provided
    *   update happens only when overridePermission is true, else existing entry will not be touched
    *   
    * @param members
    * @param roleName
    * @param permissionType
    * @throws SecurityException
    */
   public void applyRolePermissionOnMembers (List<Membr> members, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) throws SecurityException;

   /**
    * Apply the requested permission on the assets provided and do not propagate 
    *    this request to the child entities of them
    *    update happens only when overridePermission is true, else existing entry will not be touched
    *     
    * @param assets
    * @param roleName
    * @param permissionType
    * @throws SecurityException
    */
   public void applyRolePermissionOnAssetsWithOutPropagation (List<Asset> assets, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) throws SecurityException;

   /**
    * Apply the requested permission on the tables provided and do not propagate 
    *    this request to the child entities of them
    *    update happens only when overridePermission is true, else existing entry will not be touched
    *    
    * @param tables
    * @param roleName
    * @param permissionType
    * @throws SecurityException
    */
   public void applyRolePermissionOnTablesWithOutPropagation (List<Tabl> tables, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) throws SecurityException;

   /**
    * Get list of objectId that are permitted for given role
    * 
    * @param role
    * @param List<ISecurityBean>
    */

   public List<Long> getPermittedObjectIdsForRole (String roleName, List<? extends ISecurityBean> objects)
            throws SecurityException;

   /**
    * Get the Assets that are only permitted to access by applying defined Security
    * 
    * @param assets
    * @return
    * @throws SecurityException
    */
   public List<Asset> filterAssetsBySecurity (List<Asset> assets) throws SecurityException;

   /**
    * Get the Tables that are only permitted to access by applying Defined Security
    * 
    * @param tables
    * @return
    * @throws SecurityException
    */
   public List<Tabl> filterTablesBySecurity (List<Tabl> tables) throws SecurityException;

   /**
    * Get the Columns that are only permitted to access by applying Defined Security
    * 
    * @param columns
    * @return
    * @throws SecurityException
    */
   public List<Colum> filterColumnsBySecurity (List<Colum> columns) throws SecurityException;

   /**
    * Get the columns by AED IDs and then pass on to filterColumnsBySecurity method
    * return the corresponding Column AED IDs that are permitted
    * 
    * @param columnAEDIDs
    * @return permittedColumnAEDIDs
    * @throws SecurityException
    */
   public List<Long> filterColumnAEDIDsBySecurity (List<Long> columnAEDIDs) throws SecurityException;
   
   /**
    * Get the Members that are only permitted to access by applying Defined Security
    * 
    * @param members
    * @return
    * @throws SecurityException
    */
   public List<Membr> filterMembersBySecurity (List<Membr> members) throws SecurityException;

   /**
    * Get members by AED IDs and then pass on to filterMembersBySecurity method
    * return the corresponding Member AED IDs for the permitted members
    * 
    * @param memberAEDIDs
    * @return permittedMemberAEDIDs
    * @throws SecurityException
    */
   public List<Long> filterMemberAEDIDsBySecurity (List<Long> memberAEDIDs) throws SecurityException;
   
   /**
    * Delete the Security Definitions on all of objects provided, no propagation is 
    *    taken care off and should not be as well
    * 
    * @param assets
    * @throws SecurityException
    */
   public void deleteSecurityDefinitions (List<? extends ISecurityBean> securityObjects) throws SecurityException;
   
   /**
    * Delete the Security Definitions on the object provided, no propagation is 
    *    taken care off and should not be as well
    * 
    * @param assets
    * @throws SecurityException
    */
   public void deleteSecurityDefinitions (ISecurityBean securityObject) throws SecurityException;
   
}
