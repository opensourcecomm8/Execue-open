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


package com.execue.platform.security.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.ISecurityBean;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.security.ExecueAccessControlEntry;
import com.execue.core.common.type.ExecueBasePermissionType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.configuration.IPlatformServicesConfigurationService;
import com.execue.platform.security.ISecurityDefinitionWrapperService;
import com.execue.security.exception.SecurityException;
import com.execue.security.service.IAclService;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

public class SecurityDefinitionWrapperServiceImpl implements ISecurityDefinitionWrapperService {

   private static final Logger                   logger = Logger.getLogger(SecurityDefinitionWrapperServiceImpl.class);

   private ICoreConfigurationService             coreConfigurationService;
   private IPlatformServicesConfigurationService platformServicesConfigurationService;

   private IAclService                           aclService;
   private ISDXRetrievalService                  sdxRetrievalService;
   private IMappingRetrievalService              mappingRetrievalService;

   @Override
   public void applyRolePermissionOnAssetsWithOutPropagation (List<Asset> assets, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) throws SecurityException {
      for (Asset asset : assets) {
         applyRolePermission(asset, roleName, permissionType, overridePermission);
      }
   }

   @Override
   public void applyRolePermissionOnAssetsWithPropagation (List<Asset> assets, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) throws SecurityException {
      for (Asset asset : assets) {
         applyRolePermissionOnAssetWithPropagation(asset, roleName, permissionType, overridePermission);
      }
   }

   @Override
   public void applyRolePermissionOnColumns (List<Colum> columns, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) throws SecurityException {
      for (Colum column : columns) {
         applyRolePermission(column, roleName, permissionType, overridePermission);
         propagatePermissionToJoinedColumns(column, roleName, permissionType, overridePermission);
      }
   }

   @Override
   public void applyRolePermissionOnMembers (List<Membr> members, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) throws SecurityException {
      for (Membr member : members) {
         applyRolePermission(member, roleName, permissionType, overridePermission);
      }
   }

   @Override
   public void applyRolePermissionOnTablesWithOutPropagation (List<Tabl> tables, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) throws SecurityException {
      for (Tabl table : tables) {
         applyRolePermission(table, roleName, permissionType, overridePermission);
      }
   }

   @Override
   public void applyRolePermissionOnTablesWithPropagation (List<Tabl> tables, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) throws SecurityException {
      for (Tabl table : tables) {
         applyRolePermissionOnTableWithPropagation(table, roleName, permissionType, overridePermission);
      }
   }

   @Override
   public List<Long> getPermittedObjectIdsForRole (String roleName, List<? extends ISecurityBean> objects)
            throws SecurityException {
      List<Long> objectIds = new ArrayList<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(objects)) {
         for (ISecurityBean object : objects) {
            List<ExecueAccessControlEntry> accessControlEntrries = getAclService().readAclEntities(object);
            objectIds.addAll(getPermittedObjectIds(roleName, object, accessControlEntrries));
         }
      }
      return objectIds;
   }

   @SuppressWarnings ("unchecked")
   public List<Asset> filterAssetsBySecurity (List<Asset> assets) throws SecurityException {
      return (List<Asset>) getAclService().applyAcl(assets);
   }

   @SuppressWarnings ("unchecked")
   public List<Tabl> filterTablesBySecurity (List<Tabl> tables) throws SecurityException {
      return (List<Tabl>) getAclService().applyAcl(tables);
   }

   @SuppressWarnings ("unchecked")
   public List<Colum> filterColumnsBySecurity (List<Colum> columns) throws SecurityException {
      return (List<Colum>) getAclService().applyAcl(columns);
   }

   @Override
   public List<Long> filterColumnAEDIDsBySecurity (List<Long> columnAEDIDs) throws SecurityException {
      try {
         Map<Long, Long> columnIDsByAEDIDs = getSdxRetrievalService().getColumnIDsByAEDIDs(columnAEDIDs);
         List<Colum> columns = populateColumnsForSecurityFilteration(columnIDsByAEDIDs.keySet());
         List<Colum> filterColumns = filterColumnsBySecurity(columns);
         return getCorrespondingColumnAEDIDs(filterColumns, columnIDsByAEDIDs);
      } catch (SDXException sdxException) {
         throw new SecurityException(sdxException.getCode(), sdxException);
      }
   }

   @Override
   @SuppressWarnings ("unchecked")
   public List<Membr> filterMembersBySecurity (List<Membr> members) throws SecurityException {
      return (List<Membr>) getAclService().applyAcl(members);
   }

   @Override
   public List<Long> filterMemberAEDIDsBySecurity (List<Long> memberAEDIDs) throws SecurityException {
      try {
         Map<Long, Long> memberIDsByAEDIDs = getSdxRetrievalService().getMemberIDsByAEDIDs(memberAEDIDs);
         List<Membr> members = populateMembersForSecurityFilteration(memberIDsByAEDIDs.keySet());
         List<Membr> filterMembers = filterMembersBySecurity(members);
         return getCorrespondingMemberAEDIDs(filterMembers, memberIDsByAEDIDs);
      } catch (SDXException sdxException) {
         throw new SecurityException(sdxException.getCode(), sdxException);
      }
   }

   /**
    * Prepare the list of Member objects by just filling in the id value alone to make the filtering feasible
    * 
    * @param memberIDs
    * @return
    */
   private List<Membr> populateMembersForSecurityFilteration (Set<Long> memberIDs) {
      List<Membr> members = new ArrayList<Membr>();
      Membr member = null;
      for (Long memberId : memberIDs) {
         member = new Membr();
         member.setId(memberId);
         members.add(member);
      }
      return members;
   }

   /**
    * Get the List of Column AED IDs for the corresponding Column IDS passed in
    * 
    * @param columns
    * @param columnIDSByColumnAEDIDs - Map with Column ID as the Key and Column AED ID as the value
    * @return
    */
   private List<Long> getCorrespondingColumnAEDIDs (List<Colum> columns, Map<Long, Long> columnIDSByColumnAEDIDs) {
      List<Long> columnAEDIDs = new ArrayList<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(columns)) {
         for (Colum column : columns) {
            columnAEDIDs.add(columnIDSByColumnAEDIDs.get(column.getId()));
         }
      }
      return columnAEDIDs;
   }

   /**
    * Get the List of Member AED IDs for the corresponding Member IDS passed in
    * 
    * @param members
    * @param memberIDSByMemberAEDIDs - Map with Member ID as the Key and Member AED ID as the value
    * @return
    */
   private List<Long> getCorrespondingMemberAEDIDs (List<Membr> members, Map<Long, Long> memberIDSByMemberAEDIDs) {
      List<Long> memberAEDIDs = new ArrayList<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(members)) {
         for (Membr member : members) {
            memberAEDIDs.add(memberIDSByMemberAEDIDs.get(member.getId()));
         }
      }
      return memberAEDIDs;
   }

   /**
    * Prepare the list of Member objects by just filling in the id value alone to make the filtering feasible
    * 
    * @param columnIDs
    * @return
    */
   private List<Colum> populateColumnsForSecurityFilteration (Set<Long> columnIDs) {
      List<Colum> columns = new ArrayList<Colum>();
      Colum column = null;
      for (Long columId : columnIDs) {
         column = new Colum();
         column.setId(columId);
         columns.add(column);
      }
      return columns;
   }

   /**
    * Get the other columns of the same asset that are mapped to the same concept as the given column
    * For all those columns apply the permission as the given column
    *  
    * @param column
    * @param roleName
    * @param permissionType
    * @param overridePermission
    * @throws SecurityException
    */
   private void propagatePermissionToJoinedColumns (Colum column, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) throws SecurityException {
      try {
         List<Colum> joinedColumns = getMappingRetrievalService().getJoinedColumnsByMappedConcept(column.getId());
         if (ExecueCoreUtil.isCollectionNotEmpty(joinedColumns)) {
            for (Colum joinedColum : joinedColumns) {
               applyRolePermission(joinedColum, roleName, permissionType, overridePermission);
            }
         }
      } catch (MappingException mappingException) {
         throw new SecurityException(mappingException.getCode(), mappingException);
      }
   }

   /**
    * Check whether the given Object is permitted for the given role.
    * If permitted then return as a list, else an empty list
    * NOTE : Though returned list will always contain one element, returned as list in order to avoid nulls
    * 
    * @param roleName
    * @param object
    * @param entries
    * @return
    */
   private List<Long> getPermittedObjectIds (String roleName, ISecurityBean object,
            List<ExecueAccessControlEntry> entries) {
      List<Long> objIds = new ArrayList<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(entries)) {
         for (ExecueAccessControlEntry accessControlEntry : entries) {
            if (ExecueBasePermissionType.GRANT.equals(accessControlEntry.getBasePermissionType())
                     && roleName.equals(accessControlEntry.getGrantedAuthority())) {
               objIds.add(object.getId());
            }
         }
      }
      return objIds;
   }

   /**
    * Apply the permission on the Asset
    * Get all the tables of the Asset and call applyRolePermissionOnTableWithPropagation on each table
    * 
    * @param asset
    * @param roleName
    * @param permissionType
    * @param overridePermission
    * @throws SecurityException
    */
   private void applyRolePermissionOnAssetWithPropagation (Asset asset, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) throws SecurityException {
      try {
         applyRolePermission(asset, roleName, permissionType, overridePermission);
         List<Tabl> tables = getSdxRetrievalService().getAllTables(asset);
         for (Tabl table : tables) {
            applyRolePermissionOnTableWithPropagation(table, roleName, permissionType, overridePermission);
         }
      } catch (SDXException sdxException) {
         throw new SecurityException(sdxException.getCode(), sdxException);
      }
   }

   /**
    * Apply the permission on the Table
    * Get all the columns of the table and apply permission on each of the columns by calling applyRolePermissionOnColumns
    * If the Table is of any type of lookup then call applyRolePermissionOnMembers
    * 
    * @param table
    * @param roleName
    * @param permissionType
    * @param overridePermission
    * @throws SecurityException
    */
   private void applyRolePermissionOnTableWithPropagation (Tabl table, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) throws SecurityException {
      try {
         
         applyRolePermission(table, roleName, permissionType, overridePermission);
         
         List<Colum> columns = getSdxRetrievalService().getColumnsOfTable(table);
         applyRolePermissionOnColumns(columns, roleName, permissionType, overridePermission);
         
         if (getPlatformServicesConfigurationService().isMemberLevelAccessRightsPropagationNeeded()
                  && (LookupType.SIMPLE_LOOKUP == table.getLookupType()
                           || LookupType.RANGE_LOOKUP == table.getLookupType()
                           || LookupType.SIMPLEHIERARCHICAL_LOOKUP == table.getLookupType() || LookupType.RANGEHIERARCHICAL_LOOKUP == table
                           .getLookupType())) {
            applyRolePermissionOnMembers(table, columns, roleName, permissionType, overridePermission);
         }
      } catch (SDXException sdxException) {
         throw new SecurityException(sdxException.getCode(), sdxException);
      }
   }

   /**
    * Get the Lookup Value Column from all the columns of the Table
    * Get the Member count of this lookup table based on the Lookup Value Column
    * Get the batch size from configuration
    * Based on Member count and batch size, get members in batches and call applyRolePermissionOnMembers
    *   on each of the batch
    * 
    * @param lookupTable
    * @param lookupTableColumns
    * @param roleName
    * @param permissionType
    * @param overridePermission
    * @throws SDXException
    * @throws SecurityException
    */
   private void applyRolePermissionOnMembers (Tabl lookupTable, List<Colum> lookupTableColumns, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) throws SDXException, SecurityException {

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
         // for each batch, apply the role permissions
         applyRolePermissionOnMembers(batchMembers, roleName, permissionType, overridePermission);
         batchNumber++;
      } while (ExecueCoreUtil.isCollectionNotEmpty(batchMembers) && batchNumber.intValue() <= batchCount);
   }

   /**
    * Call createUpdatePermissionByRole of AclService
    * 
    * @param targetObject
    * @param roleName
    * @param permissionType
    */
   private void applyRolePermission (ISecurityBean targetObject, String roleName,
            ExecueBasePermissionType permissionType, boolean overridePermission) {
      getAclService().createUpdatePermissionByRole(targetObject, roleName, permissionType, overridePermission);
   }
   
   @Override
   public void deleteSecurityDefinitions (List<? extends ISecurityBean> securityObjects) throws SecurityException {
      for (ISecurityBean iSecurityBean : securityObjects) {
         deleteSecurityDefinitions(iSecurityBean);
      }
   }
   
   @Override
   public void deleteSecurityDefinitions (ISecurityBean securityObject) throws SecurityException {
      getAclService().removeObjectPermissions(securityObject);   
   }
   
   public IAclService getAclService () {
      return aclService;
   }

   public void setAclService (IAclService aclService) {
      this.aclService = aclService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IPlatformServicesConfigurationService getPlatformServicesConfigurationService () {
      return platformServicesConfigurationService;
   }

   public void setPlatformServicesConfigurationService (
            IPlatformServicesConfigurationService platformServicesConfigurationService) {
      this.platformServicesConfigurationService = platformServicesConfigurationService;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
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

}
