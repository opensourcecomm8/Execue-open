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


/**
 * 
 */
package com.execue.platform.security;

import com.execue.security.exception.SecurityException;

/**
 * Service to handle the Security Definition distribution across the Asset Entities
 *    This service makes sure that the Security Definitions are in 
 *       consistent fashion with in the Asset and it's child assets
 *       
 * @author Nitesh
 */
public interface ISecurityDefinitionPublishWrapperService {

   /**
    * Apply ADMIN ROLE permission on the Asset provided, make this permission as GRANT always and with overriding
    *    This makes sure that all the new entities that were added to the asset are taken care of for Security Definitions
    * If the provided asset is a Parent Asset
    *    For all other Roles that this Asset has GRANT/REVOKE
    *       Propagate the higher level entity permissions to lower level entities with out overriding
    *          this makes sure that all the new entities that were added to the asset are taken care of for Security Definitions
    *    Get all the Child Assets of this Asset
    *    if child assets exists
    *       Apply ADMIN ROLE permission on all the child assets available, make this permission as GRANT always and with overriding
    *       For all other Roles that the Parent Asset has GRANT/REVOKE
    *          Copy all the permissions of the Parent Asset Entity to the corresponding Child Asset Entity
    *             Exclude the ADMIN ROLE, as it is already taken care of above
    *    end if
    * Else if the provided asset is a Child Asset
    *    Copy all the permissions of the Parent Asset Entity to the corresponding Child Asset Entity
    *       Exclude the ADMIN ROLE, as it is already taken care of above
    * End if
    * 
    * While copying parent asset permission to child asset, perform the below steps as well
    *    if the child asset is a Cube
    *       Assign GRANT on Statistics Lookup Table with propagation for all the Roles
    *       On each of the Range Lookups,
    *          Consider the permission of Lookup Value Column and then propagate the same permission 
    *             to the corresponding Lookup Table with Propagation
    *    else if the child asset is a Mart
    *       Assign GRANT on the column mapped to Scaling Factor Concept for all the Roles    
    *    end if
    * 
    * @param userId
    * @param assetId
    * @throws SecurityException
    */
   public void applyRolePermissionOnAssetWithPropagation (Long userId, Long assetId) throws SecurityException;

}
