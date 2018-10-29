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
package com.execue.platform.security.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.security.ExecueAccessControlEntry;
import com.execue.core.common.bean.security.SecurityGroups;
import com.execue.core.common.bean.security.SecurityRoles;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.ExecueBasePermissionType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.configuration.IPlatformServicesConfigurationService;
import com.execue.platform.security.ISecurityDefinitionPublishWrapperService;
import com.execue.platform.security.ISecurityDefinitionWrapperService;
import com.execue.security.bean.ExecueUserDetails;
import com.execue.security.exception.SecurityException;
import com.execue.security.service.IAclService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.swi.service.IUserManagementService;

/**
 * @author Nitesh
 *
 */
public class SecurityDefinitionPublishWrapperServiceImpl implements ISecurityDefinitionPublishWrapperService {

   private static final Logger                   log = Logger.getLogger(SecurityDefinitionWrapperServiceImpl.class);

   private ICoreConfigurationService             coreConfigurationService;
   private IPlatformServicesConfigurationService platformServicesConfigurationService;
   private IAclService                           aclService;
   private ISDXRetrievalService                  sdxRetrievalService;
   private IMappingRetrievalService              mappingRetrievalService;
   private ISecurityDefinitionWrapperService     securityDefinitionWrapperService;
   private IUserManagementService                userManagementService;
   private IBaseKDXRetrievalService              baseKDXRetrievalService;
   private IKDXRetrievalService                  kdxRetrievalService;

   /*
    * (non-Javadoc)
    * @see com.execue.platform.security.ISecurityDefinitionPublishWrapperService#applyRolePermissionOnAssetWithPropagation(java.lang.Long, java.lang.Long)
    */
   @Override
   public void applyRolePermissionOnAssetWithPropagation (Long userId, Long assetId) throws SecurityException {
      try {

         /*
          * NOTE : -JT- We need to reset SecurityAuthentication because in job flow Security Context become null 
          *    and internal implementation of IAclService trying to fetch Authentication from SecurityContextHolder,
          *    We need to see best possible solution for that. 
          */
         setSecurityAuthentication(userId);

         // Get the asset
         Asset asset = getSdxRetrievalService().getAsset(assetId);

         // Apply the ROLE_ADMIN security definition to the asset
         applyAdminRoleOnAsset(asset);

         if (asset.getType() == AssetType.Relational) {

            // Get the child assets
            List<Asset> childAssets = getSdxRetrievalService().getAllChildAssets(assetId);

            // Apply the ROLE_ADMIN security definition to all the child assets
            if (ExecueCoreUtil.isCollectionNotEmpty(childAssets)) {
               applyAdminRoleOnAssets(childAssets);
            } else {
               if (log.isDebugEnabled()) {
                  log.debug("No child asset exists for the parent asset: " + asset.getName());
               }
            }

            // Process the non-admin security roles
            List<ExecueAccessControlEntry> baseAssetAclEntities = getAclService().readAclEntities(asset);

            // Apply the parent security definition to the related asset entities like table, column, members, etc
            applyAssetRolesOnRelatedAssetEntities(asset, baseAssetAclEntities);

            // Copy the parent security definition to all the child assets
            if (ExecueCoreUtil.isCollectionNotEmpty(childAssets)) {
               copyParentSecurityPermissionOnChildAssets(asset, childAssets, baseAssetAclEntities);
            }
         } else {
            // Get the parent asset
            Long parentAssetId = asset.getBaseAssetId();
            Asset parentAsset = getSdxRetrievalService().getAsset(parentAssetId);

            // Get the parent security definitions
            List<ExecueAccessControlEntry> parentAssetAclEntities = getAclService().readAclEntities(parentAsset);

            // Copy the parent security definition to all the child assets
            List<Asset> childAssets = new ArrayList<Asset>();
            childAssets.add(asset);
            copyParentSecurityPermissionOnChildAssets(parentAsset, childAssets, parentAssetAclEntities);
         }

      } catch (SDXException e) {
         throw new SecurityException(e.getCode(), e);
      } catch (MappingException e) {
         throw new SecurityException(e.getCode(), e);
      } catch (KDXException e) {
         throw new SecurityException(e.getCode(), e);
      }
   }

   /**
    * Wrapper over applyAdminRoleOnAssets
    * 
    * @param asset
    * @throws SecurityException
    */
   private void applyAdminRoleOnAsset (Asset asset) throws SecurityException {
      List<Asset> assets = new ArrayList<Asset>();
      assets.add(asset);
      applyAdminRoleOnAssets(assets);
   }

   /**
    *  Asset under consideration needs to be assigned with GRANT Access for ADMIN ROLE with propagation
    *  Call the regular propagation with role being ADMIN and permission being GRANT
    *  this will take care off permitting newly added entities as well
    *  
    * @param asset
    * @param assets
    * @throws SecurityException
    */
   private void applyAdminRoleOnAssets (List<Asset> assets) throws SecurityException {

      String adminRole = ExecueConstants.ROLE_ADMIN;
      ExecueBasePermissionType grantPermissionType = ExecueBasePermissionType.GRANT;
      if (log.isDebugEnabled()) {
         List<String> assetNames = new ArrayList<String>();
         for (Asset asset : assets) {
            assetNames.add(asset.getName());
         }
         log
                  .debug("Granting Admin Role to the security for the Asset(s): "
                           + ExecueCoreUtil.joinCollection(assetNames));
      }
      getSecurityDefinitionWrapperService().applyRolePermissionOnAssetsWithPropagation(assets, adminRole,
               grantPermissionType, true);
   }

   /**
    * Build an Authentication Object for the given User identification
    *    and set it to the Security Context
    *    
    * @param userId
    */
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
    * For every role and every child entity on this asset we need to make sure there is an entry exists
    * if there is no entry defined on any entity that means, that is a new entity
    * in such cases, a new entry needs to be made for the ROLE in hand with the permission of the immediate parent entity
    * if table is under consideration for new entry then Asset's permission needs to be used to assign on the Table
    * in case a Column we need to use Permission on Table as the permission for the Column and so on
    * For existing entries we should not alter any thing in this process 
    *    (goal of this step is to handle newly added entities only)
    * 
    * @param asset
    * @param baseAssetAclEntities
    * @throws SDXException
    * @throws SecurityException
    */
   private void applyAssetRolesOnRelatedAssetEntities (Asset asset, List<ExecueAccessControlEntry> baseAssetAclEntities)
            throws SDXException, SecurityException {

      if (ExecueCoreUtil.isCollectionEmpty(baseAssetAclEntities) || baseAssetAclEntities.size() == 1
               && baseAssetAclEntities.get(0).getGrantedAuthority().equalsIgnoreCase(ExecueConstants.ROLE_ADMIN)) {
         if (log.isDebugEnabled()) {
            log.debug("No other security roles found for the asset: " + asset.getName());
         }
         return;
      }

      // Get all the tables 
      List<Tabl> tables = getSdxRetrievalService().getAllTables(asset);
      // apply the role permissions of the asset to the tables if there is no permission exists, hence overridePermission is set to false
      boolean overridePermission = false;
      for (ExecueAccessControlEntry baseAssetAccessControlEntry : baseAssetAclEntities) {
         String baseAssetGrantedAuthority = baseAssetAccessControlEntry.getGrantedAuthority();
         if (baseAssetGrantedAuthority.equalsIgnoreCase(ExecueConstants.ROLE_ADMIN)) {
            continue;
         }
         ExecueBasePermissionType baseAssetPermissionType = baseAssetAccessControlEntry.getBasePermissionType();
         getSecurityDefinitionWrapperService().applyRolePermissionOnTablesWithOutPropagation(tables,
                  baseAssetGrantedAuthority, baseAssetPermissionType, overridePermission);
      }

      for (Tabl table : tables) {
         // Get all the columns
         List<Colum> columns = getSdxRetrievalService().getColumnsOfTable(table);
         List<ExecueAccessControlEntry> tableAclEntities = getAclService().readAclEntities(table);
         if (ExecueCoreUtil.isCollectionEmpty(tableAclEntities) || tableAclEntities.size() == 1
                  && tableAclEntities.get(0).getGrantedAuthority().equalsIgnoreCase(ExecueConstants.ROLE_ADMIN)) {
            if (log.isDebugEnabled()) {
               log.debug("No other security roles found for the table: " + table.getName());
            }
            continue;
         }

         // apply the role permissions of the table to the column if there is no permission exists
         for (ExecueAccessControlEntry tableAccessControlEntry : tableAclEntities) {
            String tableGrantedAuthority = tableAccessControlEntry.getGrantedAuthority();
            if (tableGrantedAuthority.equalsIgnoreCase(ExecueConstants.ROLE_ADMIN)) {
               continue;
            }
            ExecueBasePermissionType tablePermissionType = tableAccessControlEntry.getBasePermissionType();
            getSecurityDefinitionWrapperService().applyRolePermissionOnColumns(columns, tableGrantedAuthority,
                     tablePermissionType, overridePermission);
         }

         // block for members if table type is of any lookup
         if (getPlatformServicesConfigurationService().isMemberLevelAccessRightsPropagationNeeded()
                  && (LookupType.SIMPLE_LOOKUP == table.getLookupType()
                           || LookupType.RANGE_LOOKUP == table.getLookupType()
                           || LookupType.SIMPLEHIERARCHICAL_LOOKUP == table.getLookupType() || LookupType.RANGEHIERARCHICAL_LOOKUP == table
                           .getLookupType())) {
            applyRolePermissionOnMembers(table, columns, tableAclEntities, overridePermission);
         }
      }
   }

   /**
    * Get the count of members
    * Get the allowed batch count for members
    * Get members in batches
    *    on each batch and for each tableAclEntity call SecurityDefinitionWrapperServiceImpl.applyRolePermissionOnMembers
    *       with the same permission that the table is assigned with
    *    
    * @param lookupTable
    * @param lookupTableColumns
    * @param tableAclEntities
    * @param overridePermission
    * @throws SDXException
    * @throws SecurityException
    */
   private void applyRolePermissionOnMembers (Tabl lookupTable, List<Colum> lookupTableColumns,
            List<ExecueAccessControlEntry> tableAclEntities, boolean overridePermission) throws SDXException,
            SecurityException {

      // Get the lookup value column
      Colum lookupValueColumn = ExecueBeanUtil.findCorrespondingColumn(lookupTableColumns, lookupTable
               .getLookupValueColumn());

      // total count of members from swi.
      Long membersCount = getSdxRetrievalService().getTotalMembersCountOfColumn(lookupValueColumn);
      Integer batchSize = getCoreConfigurationService().getSDXMemberAbsorptionBatchSize();
      double batchCount = Math.ceil(membersCount.doubleValue() / batchSize.doubleValue());
      List<Membr> batchMembers = new ArrayList<Membr>();
      Long batchNumber = 1L;
      do {
         batchMembers = getSdxRetrievalService().getColumnMembersByPage(lookupValueColumn, batchNumber,
                  batchSize.longValue());
         // for each batch of members, apply all the table level role permissions
         for (ExecueAccessControlEntry execueAccessControlEntry : tableAclEntities) {
            String tableGrantedAuthority = execueAccessControlEntry.getGrantedAuthority();
            if (tableGrantedAuthority.equalsIgnoreCase(ExecueConstants.ROLE_ADMIN)) {
               continue;
            }
            ExecueBasePermissionType tablePermissionType = execueAccessControlEntry.getBasePermissionType();
            getSecurityDefinitionWrapperService().applyRolePermissionOnMembers(batchMembers, tableGrantedAuthority,
                     tablePermissionType, overridePermission);
         }
         batchNumber++;
      } while (ExecueCoreUtil.isCollectionNotEmpty(batchMembers) && batchNumber.intValue() <= batchCount);
   }

   /**
    * Copies the Permissions to all the Child Asset's entities from the permissions defined on the Parent Asset
    * 
    * @param parentAsset
    * @param childAssets
    * @param parentAssetAclEntities
    * @throws SecurityException
    * @throws MappingException
    * @throws SDXException
    * @throws KDXException
    */
   private void copyParentSecurityPermissionOnChildAssets (Asset parentAsset, List<Asset> childAssets,
            List<ExecueAccessControlEntry> parentAssetAclEntities) throws SecurityException, MappingException,
            SDXException, KDXException {

      // Return if there is only ROLE_ADMIN found
      if (ExecueCoreUtil.isCollectionEmpty(parentAssetAclEntities) || parentAssetAclEntities.size() == 1
               && parentAssetAclEntities.get(0).getGrantedAuthority().equalsIgnoreCase(ExecueConstants.ROLE_ADMIN)) {

         if (log.isDebugEnabled()) {
            log.debug("No other security roles found for the parent asset: " + parentAsset.getName());
         }
         return;
      }

      // Group the parent asset security roles into two separate grant and revoke list based on the permission type
      List<ExecueAccessControlEntry> parentAssetRevokeAclEntities = new ArrayList<ExecueAccessControlEntry>();
      List<ExecueAccessControlEntry> parentAssetGrantAclEntities = new ArrayList<ExecueAccessControlEntry>();
      for (ExecueAccessControlEntry parentAssetAccessControlEntry : parentAssetAclEntities) {

         String parentAssetGrantedAuthority = parentAssetAccessControlEntry.getGrantedAuthority();
         if (parentAssetGrantedAuthority.equalsIgnoreCase(ExecueConstants.ROLE_ADMIN)) {
            continue;
         }

         ExecueBasePermissionType parentAssetPermissionType = parentAssetAccessControlEntry.getBasePermissionType();
         if (parentAssetPermissionType == ExecueBasePermissionType.GRANT) {
            parentAssetGrantAclEntities.add(parentAssetAccessControlEntry);
         } else {
            parentAssetRevokeAclEntities.add(parentAssetAccessControlEntry);
         }
      }

      // Copy the parent asset revoke ACL entities to all the child assets 
      copyParentAssetRevokeAclEntities(childAssets, parentAssetRevokeAclEntities);

      // Copy the parent asset grant ACL entities to all the child assets 
      copyParentAssetGrantAclEntities(childAssets, parentAssetGrantAclEntities);
   }

   /**
    * As Child assets are connected to parent only by KDX entities, we need go round about 
    *    to copy the security definitions
    * 
    * Get all the mapped concepts from the Model
    * For each concept,
    *    get the SDX entity of Parent Asset and the defined definition on it for each ROLE
    *    Copy this definition to all the child asset entities that are mapped to the same concept
    *    This Step makes sure that all the child asset column entities are defined with permissions 
    *       same as parent asset column entity definitions
    * 
    * For all the Concepts which are dimensions, follow the similar logic at Instance level
    *    This step makes sure that we handled member level definitions are copied
    * 
    * Till this point we would have definitions copied at Asset, Column and Member level
    * 
    * To copy Table Level Definitions we need to perform below logic.,
    *    On each table get unique permission from the permissions of all the columns on each role
    *    if the outcome is more than one permission, then assign GRANT on Table for the role
    *    if the outcome is one permission, then assign the same permission to the Table
    *    repeat for all the roles
    * 
    * @param childAssets
    * @param grantParentAssetAclEntities
    * @throws MappingException
    * @throws SecurityException
    * @throws SDXException
    * @throws KDXException 
    */
   private void copyParentAssetGrantAclEntities (List<Asset> childAssets,
            List<ExecueAccessControlEntry> grantParentAssetAclEntities) throws MappingException, SecurityException,
            SDXException, KDXException {

      for (ExecueAccessControlEntry parentAssetAccessControlEntry : grantParentAssetAclEntities) {

         String parentAssetGrantedAuthority = parentAssetAccessControlEntry.getGrantedAuthority();
         ExecueBasePermissionType parentAssetPermissionType = parentAssetAccessControlEntry.getBasePermissionType();
         getSecurityDefinitionWrapperService().applyRolePermissionOnAssetsWithOutPropagation(childAssets,
                  parentAssetGrantedAuthority, parentAssetPermissionType, false);
      }

      for (Asset childAsset : childAssets) {

         copyColumnAndMemberPermissions(childAsset, grantParentAssetAclEntities);

         copyTablePermissions(childAsset, grantParentAssetAclEntities);

      }
   }

   /**
    * To assign Table Level Security Definitions we need to perform below logic.,
    *    On each table get unique permission from the permissions of all the columns of that table on each role
    *    if the outcome is more than one permission, then assign GRANT on Table for the role
    *    if the outcome is one permission, then assign the same permission to the Table
    *    repeat for all the roles
    * 
    * NOTE : This should be performed after all the Column and Member Level permission are copied 
    *    from the parent asset to this child asset.
    * 
    * @param childAsset
    * @param grantParentAssetAclEntities
    * @throws SDXException
    * @throws SecurityException
    */
   private void copyTablePermissions (Asset childAsset, List<ExecueAccessControlEntry> grantParentAssetAclEntities)
            throws SDXException, SecurityException {
      // Handle the table level permissions
      List<Tabl> childTables = getSdxRetrievalService().getAllTables(childAsset);
      Map<ExecueAccessControlEntry, List<Tabl>> childTablesByGrantRoles = new HashMap<ExecueAccessControlEntry, List<Tabl>>();
      Map<ExecueAccessControlEntry, List<Tabl>> childTablesByRevokeRoles = new HashMap<ExecueAccessControlEntry, List<Tabl>>();
      for (Tabl childTable : childTables) {
         List<Colum> columnsOfTable = getSdxRetrievalService().getColumnsOfTable(childTable);
         for (ExecueAccessControlEntry parentAssetAclEntity : grantParentAssetAclEntities) {
            List<ExecueBasePermissionType> columnExecueAccessControlEntry = getAclService()
                     .readUniquePermissionTypesOnObjectForRole(columnsOfTable,
                              parentAssetAclEntity.getGrantedAuthority());
            boolean onlyOne = columnExecueAccessControlEntry.size() == 1;
            if (onlyOne) {
               // only one unique role means we should add same role permission type to the table
               ExecueBasePermissionType permissionType = columnExecueAccessControlEntry.get(0);
               if (permissionType == ExecueBasePermissionType.GRANT) {
                  List<Tabl> grantRoleTables = childTablesByGrantRoles.get(parentAssetAclEntity);
                  if (grantRoleTables == null) {
                     grantRoleTables = new ArrayList<Tabl>();
                     childTablesByGrantRoles.put(parentAssetAclEntity, grantRoleTables);
                  }
                  grantRoleTables.add(childTable);
               } else {
                  List<Tabl> revokeRoleTables = childTablesByRevokeRoles.get(parentAssetAclEntity);
                  if (revokeRoleTables == null) {
                     revokeRoleTables = new ArrayList<Tabl>();
                     childTablesByRevokeRoles.put(parentAssetAclEntity, revokeRoleTables);
                  }
                  revokeRoleTables.add(childTable);
               }
            } else {
               // more than one unique roles means we should add it to the grant 
               List<Tabl> grantRoleTables = childTablesByGrantRoles.get(parentAssetAclEntity);
               if (grantRoleTables == null) {
                  grantRoleTables = new ArrayList<Tabl>();
                  childTablesByGrantRoles.put(parentAssetAclEntity, grantRoleTables);
               }
               grantRoleTables.add(childTable);
            }
         }
      }
      for (Entry<ExecueAccessControlEntry, List<Tabl>> entry : childTablesByGrantRoles.entrySet()) {
         ExecueAccessControlEntry key = entry.getKey();
         List<Tabl> tables = entry.getValue();
         getSecurityDefinitionWrapperService().applyRolePermissionOnTablesWithOutPropagation(tables,
                  key.getGrantedAuthority(), ExecueBasePermissionType.GRANT, true);
      }

      for (Entry<ExecueAccessControlEntry, List<Tabl>> entry : childTablesByRevokeRoles.entrySet()) {
         ExecueAccessControlEntry key = entry.getKey();
         List<Tabl> tables = entry.getValue();
         getSecurityDefinitionWrapperService().applyRolePermissionOnTablesWithOutPropagation(tables,
                  key.getGrantedAuthority(), ExecueBasePermissionType.REVOKE, true);
      }
   }

   /**
    * Get the map of Concept BED ID and the list columns mapped to the Concept for Parent Asset
    * Get a similar map from child asset as well
    * Per each entry of the Parent Asset map
    *    get one Column permission from parent asset and apply the same permission to 
    *       all the columns that are mapped to this concept in child asset
    * 
    * @param childAsset
    * @param grantParentAssetAclEntities 
    * @throws MappingException
    * @throws SecurityException
    * @throws KDXException 
    * @throws SDXException 
    */
   private void copyColumnAndMemberPermissions (Asset childAsset,
            List<ExecueAccessControlEntry> grantParentAssetAclEntities) throws MappingException, SecurityException,
            KDXException, SDXException {
      Long parentAssetId = childAsset.getBaseAssetId();
      Long childAssetId = childAsset.getId();
      Map<Long, List<Long>> mappedParentColumnIdsByConceptBedId = getMappingRetrievalService()
               .getMappedColumnIDsByConceptBEDID(parentAssetId);

      Map<Long, List<Long>> mappedChildColumnIdsByConceptBedId = getMappingRetrievalService()
               .getMappedColumnIDsByConceptBEDID(childAssetId);

      for (Entry<Long, List<Long>> parentColumnEntry : mappedParentColumnIdsByConceptBedId.entrySet()) {
         Long conceptBedId = parentColumnEntry.getKey();
         List<Long> parentColumnIds = parentColumnEntry.getValue();
         Long parentColumnId = parentColumnIds.get(0);
         Colum parentColum = new Colum();
         parentColum.setId(parentColumnId);
         // Get the acl entries for the parent column
         List<ExecueAccessControlEntry> parentColumnAclEntities = getAclService().readAclEntities(parentColum);
         if (ExecueCoreUtil.isCollectionEmpty(parentColumnAclEntities) || parentColumnAclEntities.size() == 1
                  && parentColumnAclEntities.get(0).getGrantedAuthority().equalsIgnoreCase(ExecueConstants.ROLE_ADMIN)) {
            if (log.isDebugEnabled()) {
               log.debug("No other security roles found for the parent column: " + parentColum.getId());
            }
            continue;
         }
         // Propagate the acl entries to all the child column
         List<Long> childColumnIds = mappedChildColumnIdsByConceptBedId.get(conceptBedId);
         if (ExecueCoreUtil.isCollectionEmpty(childColumnIds)) {
            continue;
         }
         List<Colum> childColumns = new ArrayList<Colum>();
         for (Long childColumnId : childColumnIds) {
            Colum childColumn = new Colum();
            childColumn.setId(childColumnId);
            childColumns.add(childColumn);
         }

         for (ExecueAccessControlEntry execueAccessControlEntry : parentColumnAclEntities) {
            String parentColumGrantedAuthority = execueAccessControlEntry.getGrantedAuthority();
            if (parentColumGrantedAuthority.equalsIgnoreCase(ExecueConstants.ROLE_ADMIN)) {
               continue;
            }
            ExecueBasePermissionType parentColumnPermissionType = execueAccessControlEntry.getBasePermissionType();
            getSecurityDefinitionWrapperService().applyRolePermissionOnColumns(childColumns,
                     parentColumGrantedAuthority, parentColumnPermissionType, true);
         }

         copyMemberPermissions(conceptBedId, childAsset);
      }

      if (ExecueBeanUtil.isExecueOwnedCube(childAsset)) {

         // In case of Cube, add a GRANT permission on the stat column for all the ROLEs
         copyGrantPermissionToStatTable(grantParentAssetAclEntities, childAssetId);

         // In case of Cube, copy the same permission to the range lookup table as the lookup value column has 
         copyRangeLookupPermissions(childAssetId);

      }

      if (ExecueBeanUtil.isExecueOwnedMart(childAsset)) {

         // In case of Mart, add a GRANT permission on SFACTOR column for all the ROLEs
         copyGrantPermissionToSFactorColumn(grantParentAssetAclEntities, childAssetId);

      }
   }

   /**
    * Get the Scaling Factor Concept
    * Get the columns mapped to this concept from the child asset
    * On all these columns apply GRANT permission for the roles that the child asset is having GRANT/REVOKE
    *  
    * @param grantParentAssetAclEntities
    * @param childAssetId
    * @throws KDXException
    * @throws MappingException
    * @throws SecurityException
    */
   private void copyGrantPermissionToSFactorColumn (List<ExecueAccessControlEntry> grantParentAssetAclEntities,
            Long childAssetId) throws KDXException, MappingException, SecurityException {
      BusinessEntityDefinition conceptBed = getBaseKDXRetrievalService().getConceptBEDByName(
               ExecueConstants.SCALING_FACTOR_CONCEPT_NAME);

      List<Long> sfactorColumnIds = getMappingRetrievalService().getMappedColumnIDsForConceptBEDID(conceptBed.getId(),
               childAssetId);
      List<Colum> childColumns = new ArrayList<Colum>();
      for (Long sfactorColumnId : sfactorColumnIds) {
         Colum sfactorColumn = new Colum();
         sfactorColumn.setId(sfactorColumnId);
         childColumns.add(sfactorColumn);
      }

      for (ExecueAccessControlEntry execueAccessControlEntry : grantParentAssetAclEntities) {
         String parentColumGrantedAuthority = execueAccessControlEntry.getGrantedAuthority();
         if (parentColumGrantedAuthority.equalsIgnoreCase(ExecueConstants.ROLE_ADMIN)) {
            continue;
         }
         getSecurityDefinitionWrapperService().applyRolePermissionOnColumns(childColumns, parentColumGrantedAuthority,
                  ExecueBasePermissionType.GRANT, true);
      }
   }

   /**
    * Get the all the Range Lookup Tables of the provided child asset
    * On each Range Lookup Table,
    *    get the lookup value column
    *    get all the permissions that the lookup value column has
    *    copy each of these permissions to the corresponding Range Lookup Table with propagation
    * 
    * @param childAssetId
    * @throws SDXException
    * @throws SecurityException
    */
   private void copyRangeLookupPermissions (Long childAssetId) throws SDXException, SecurityException {
      List<Tabl> rangeLookupTypeTables = getSdxRetrievalService().getTablesByLookupType(childAssetId,
               LookupType.RANGE_LOOKUP);
      if (ExecueCoreUtil.isCollectionEmpty(rangeLookupTypeTables)) {
         if (log.isDebugEnabled()) {
            log.debug("No Range Lookup Type Tables exists for asset Id: " + childAssetId);
         }
         return;
      }
      for (Tabl rangeLookupTypeTable : rangeLookupTypeTables) {
         List<Colum> columnsOfTable = getSdxRetrievalService().getColumnsOfTable(rangeLookupTypeTable);
         Colum lookupValueColumn = ExecueBeanUtil.findCorrespondingColumn(columnsOfTable, rangeLookupTypeTable
                  .getLookupValueColumn());
         List<ExecueAccessControlEntry> readAclEntities = getAclService().readAclEntities(lookupValueColumn);
         List<Tabl> tables = new ArrayList<Tabl>();
         tables.add(rangeLookupTypeTable);

         for (ExecueAccessControlEntry execueAccessControlEntry : readAclEntities) {
            String parentColumGrantedAuthority = execueAccessControlEntry.getGrantedAuthority();
            if (parentColumGrantedAuthority.equalsIgnoreCase(ExecueConstants.ROLE_ADMIN)) {
               continue;
            }
            ExecueBasePermissionType basePermissionType = execueAccessControlEntry.getBasePermissionType();
            getSecurityDefinitionWrapperService().applyRolePermissionOnTablesWithPropagation(tables,
                     parentColumGrantedAuthority, basePermissionType, true);
         }
      }
   }

   /**
    * For the provided child asset
    *    get the Lookup Table of the lookup value column which is mapped to Statistics concept
    *    For all the Roles of the parent asset, assign GRANT permission on the above table with propagation
    * 
    * @param grantParentAssetAclEntities
    * @param childAssetId
    * @throws MappingException
    * @throws SecurityException
    */
   private void copyGrantPermissionToStatTable (List<ExecueAccessControlEntry> grantParentAssetAclEntities,
            Long childAssetId) throws MappingException, SecurityException {
      Tabl statTable = getMappingRetrievalService().getStatisticsMappedLookupTableOnCube(childAssetId);
      if (statTable == null) {
         log.warn("No Stat Table exists for asset Id: " + childAssetId);
         return;
      }
      List<Tabl> statTables = new ArrayList<Tabl>();
      statTables.add(statTable);

      for (ExecueAccessControlEntry execueAccessControlEntry : grantParentAssetAclEntities) {
         String parentColumGrantedAuthority = execueAccessControlEntry.getGrantedAuthority();
         if (parentColumGrantedAuthority.equalsIgnoreCase(ExecueConstants.ROLE_ADMIN)) {
            continue;
         }
         getSecurityDefinitionWrapperService().applyRolePermissionOnTablesWithPropagation(statTables,
                  parentColumGrantedAuthority, ExecueBasePermissionType.GRANT, true);
      }
   }

   /**
    * Concept passed in should be of Enumeration Nature, else do not perform any operation
    * Get the mapped instance count of the provided Concept
    * Get the Batch Size from configuration
    * Get the instances in batches and follow as below on each batch.,
    *    Get the map of instance and it's mapped member from parent asset
    *    Get a similar map from child asset as well
    *    Per each mapped instance of Parent Asset
    *       get one member permission from parent asset and apply the same permission to 
    *          all the members that are mapped to this instance in child asset
    *          
    * @param conceptBedId
    * @param childAsset
    * @throws KDXException
    * @throws MappingException
    * @throws SDXException
    * @throws SecurityException
    */
   private void copyMemberPermissions (Long conceptBedId, Asset childAsset) throws KDXException, MappingException,
            SDXException, SecurityException {

      if (!getKdxRetrievalService().isConceptMatchedBehavior(conceptBedId, BehaviorType.ENUMERATION)) {
         return;
      }

      Long parentAssetId = childAsset.getBaseAssetId();
      Long childAssetId = childAsset.getId();

      // Get the total count of mapped instances from swi.
      Long instancesCount = getMappingRetrievalService().getMappedInstanceCount(conceptBedId, parentAssetId);
      if (instancesCount != null && instancesCount.equals(0l)) {
         if (log.isDebugEnabled()) {
            log.debug("No Mapped Instances exists for concept bed id: " + conceptBedId);
         }
         return;
      }

      Integer batchSize = getCoreConfigurationService().getSDXMemberAbsorptionBatchSize();
      double batchCount = Math.ceil(instancesCount.doubleValue() / batchSize.doubleValue());
      List<Membr> parentMembers = new ArrayList<Membr>();
      Long batchNumber = 1L;

      do {
         Map<Long, Long> mappedParentMemberIdByInstanceBedId = getMappingRetrievalService()
                  .getMappedMemberIDByInstanceBEDID(conceptBedId, parentAssetId, batchNumber, batchSize.longValue());

         Map<Long, Long> mappedChildMemberIdByInstanceBedId = getMappingRetrievalService()
                  .getMappedMemberIDByInstanceBEDID(conceptBedId, childAssetId, batchNumber, batchSize.longValue());

         for (Entry<Long, Long> parentColumnEntry : mappedParentMemberIdByInstanceBedId.entrySet()) {
            Long instanceBedId = parentColumnEntry.getKey();
            Long parentMemberId = parentColumnEntry.getValue();
            Membr parentMember = new Membr();
            parentMember.setId(parentMemberId);
            // Get the acl entries for the parent column
            List<ExecueAccessControlEntry> parentMemberAclEntities = getAclService().readAclEntities(parentMember);
            if (ExecueCoreUtil.isCollectionEmpty(parentMemberAclEntities)
                     || parentMemberAclEntities.size() == 1
                     && parentMemberAclEntities.get(0).getGrantedAuthority().equalsIgnoreCase(
                              ExecueConstants.ROLE_ADMIN)) {
               if (log.isDebugEnabled()) {
                  log.debug("No other security roles found for the parent Member: " + parentMember.getId());
               }
               continue;
            }
            // Propagate the acl entries to the child member
            Long childMemberId = mappedChildMemberIdByInstanceBedId.get(instanceBedId);
            if (childMemberId == null) {
               continue;
            }
            List<Membr> childMembers = new ArrayList<Membr>();
            Membr childMember = new Membr();
            childMember.setId(childMemberId);
            childMembers.add(childMember);

            for (ExecueAccessControlEntry execueAccessControlEntry : parentMemberAclEntities) {
               String parentMemberGrantedAuthority = execueAccessControlEntry.getGrantedAuthority();
               if (parentMemberGrantedAuthority.equalsIgnoreCase(ExecueConstants.ROLE_ADMIN)) {
                  continue;
               }
               ExecueBasePermissionType parentMemberPermissionType = execueAccessControlEntry.getBasePermissionType();
               getSecurityDefinitionWrapperService().applyRolePermissionOnMembers(childMembers,
                        parentMemberGrantedAuthority, parentMemberPermissionType, true);
            }
         }
         batchNumber++;
      } while (ExecueCoreUtil.isCollectionNotEmpty(parentMembers) && batchNumber.intValue() <= batchCount);
   }

   /**
    * This method propagates the parent asset security permission to all the child assets 
    *    and their related entities (tables, column, members)
    *   
    * @param childAssets
    * @param parentAssetRevokeAclEntities
    * @throws SecurityException
    * @throws SDXException
    */
   private void copyParentAssetRevokeAclEntities (List<Asset> childAssets,
            List<ExecueAccessControlEntry> parentAssetRevokeAclEntities) throws SecurityException, SDXException {
      if (ExecueCoreUtil.isCollectionEmpty(parentAssetRevokeAclEntities)) {
         return;
      }
      // Apply the role permissions of the parent asset to the child asset overriding the existing permission(if found)
      for (ExecueAccessControlEntry parentAssetAccessControlEntry : parentAssetRevokeAclEntities) {
         String parentAssetGrantedAuthority = parentAssetAccessControlEntry.getGrantedAuthority();
         ExecueBasePermissionType parentAssetPermissionType = parentAssetAccessControlEntry.getBasePermissionType();
         getSecurityDefinitionWrapperService().applyRolePermissionOnAssetsWithPropagation(childAssets,
                  parentAssetGrantedAuthority, parentAssetPermissionType, true);

      }
   }

   /**
    * @return the platformServicesConfigurationService
    */
   public IPlatformServicesConfigurationService getPlatformServicesConfigurationService () {
      return platformServicesConfigurationService;
   }

   /**
    * @param platformServicesConfigurationService the platformServicesConfigurationService to set
    */
   public void setPlatformServicesConfigurationService (
            IPlatformServicesConfigurationService platformServicesConfigurationService) {
      this.platformServicesConfigurationService = platformServicesConfigurationService;
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

   /**
    * @return the sdxRetrievalService
    */
   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   /**
    * @param sdxRetrievalService the sdxRetrievalService to set
    */
   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   /**
    * @return the securityDefinitionWrapperService
    */
   public ISecurityDefinitionWrapperService getSecurityDefinitionWrapperService () {
      return securityDefinitionWrapperService;
   }

   /**
    * @param securityDefinitionWrapperService the securityDefinitionWrapperService to set
    */
   public void setSecurityDefinitionWrapperService (ISecurityDefinitionWrapperService securityDefinitionWrapperService) {
      this.securityDefinitionWrapperService = securityDefinitionWrapperService;
   }

   /**
    * @return the mappingRetrievalService
    */
   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   /**
    * @param mappingRetrievalService the mappingRetrievalService to set
    */
   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   /**
    * @return the userManagementService
    */
   public IUserManagementService getUserManagementService () {
      return userManagementService;
   }

   /**
    * @param userManagementService the userManagementService to set
    */
   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   /**
    * @return the baseKDXRetrievalService
    */
   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   /**
    * @param baseKDXRetrievalService the baseKDXRetrievalService to set
    */
   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }
}