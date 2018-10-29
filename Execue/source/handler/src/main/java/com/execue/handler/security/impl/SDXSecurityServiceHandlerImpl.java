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


package com.execue.handler.security.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.type.ExecueBasePermissionType;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIMember;
import com.execue.handler.bean.UITable;
import com.execue.handler.security.ISDXSecurityServiceHandler;
import com.execue.platform.security.ISecurityDefinitionWrapperService;
import com.execue.security.UserContextService;
import com.execue.security.exception.SecurityException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * 
 * @author Jitendra
 *
 */

public class SDXSecurityServiceHandlerImpl extends UserContextService implements ISDXSecurityServiceHandler {

   private ISecurityDefinitionWrapperService securityDefinitionWrapperService;
   private ISDXRetrievalService              sdxRetrievalService;
   private Logger                            logger = Logger.getLogger(SDXSecurityServiceHandlerImpl.class);

   @Override
   public void addAssetsPermission (String roleName, List<Long> selectedAssetIds, List<Long> existingAssetIds)
            throws HandlerException {
      List<Long> unSelectedAssetIds = new ArrayList<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(existingAssetIds)) {
         for (Long assetId : existingAssetIds) {
            if (!selectedAssetIds.contains(assetId)) {
               unSelectedAssetIds.add(assetId);
            }
         }
      }
      try {
         List<Asset> selectedAssets = new ArrayList<Asset>();
         for (Long selectedAssetId : selectedAssetIds) {
            selectedAssets.add(getSdxRetrievalService().getAssetById(selectedAssetId));
         }
         getSecurityDefinitionWrapperService().applyRolePermissionOnAssetsWithPropagation(selectedAssets, roleName,
                  ExecueBasePermissionType.GRANT, true);

         if (ExecueCoreUtil.isCollectionNotEmpty(unSelectedAssetIds)) {
            List<Asset> unSelectedAssets = new ArrayList<Asset>();
            for (Long unSelectedAssetId : unSelectedAssetIds) {
               unSelectedAssets.add(getSdxRetrievalService().getAssetById(unSelectedAssetId));
            }
            getSecurityDefinitionWrapperService().applyRolePermissionOnAssetsWithPropagation(unSelectedAssets,
                     roleName, ExecueBasePermissionType.REVOKE, true);

         }
      } catch (SDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      } catch (SecurityException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }

   }

   @Override
   public void addTablesPermission (String roleName, List<Long> selectedTableIds, List<Long> existingTableIds)
            throws HandlerException {
      List<Long> unSelectedTableIds = new ArrayList<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(existingTableIds)) {
         for (Long tableId : existingTableIds) {
            if (!selectedTableIds.contains(tableId)) {
               unSelectedTableIds.add(tableId);
            }
         }
      }

      try {
         List<Tabl> selectedTables = new ArrayList<Tabl>();
         for (Long selectedTableId : selectedTableIds) {
            selectedTables.add(getSdxRetrievalService().getTableById(selectedTableId));
         }
         getSecurityDefinitionWrapperService().applyRolePermissionOnTablesWithPropagation(selectedTables, roleName,
                  ExecueBasePermissionType.GRANT, true);

         if (ExecueCoreUtil.isCollectionNotEmpty(unSelectedTableIds)) {
            List<Tabl> unSelectedTables = new ArrayList<Tabl>();
            for (Long unSelectedTableId : unSelectedTableIds) {
               unSelectedTables.add(getSdxRetrievalService().getTableById(unSelectedTableId));
            }
            getSecurityDefinitionWrapperService().applyRolePermissionOnTablesWithPropagation(unSelectedTables,
                     roleName, ExecueBasePermissionType.REVOKE, true);

         }
      } catch (SDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      } catch (SecurityException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }

   }

   @Override
   public void addColumnsPermission (String roleName, List<Long> selectedColumnIds, List<Long> permittedColumnIds)
            throws HandlerException {
      List<Long> unSelectedColumnIds = new ArrayList<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(permittedColumnIds)) {
         for (Long columnId : permittedColumnIds) {
            if (!selectedColumnIds.contains(columnId)) {
               unSelectedColumnIds.add(columnId);
            }
         }
      }
      try {
         List<Colum> selectedColumns = new ArrayList<Colum>();
         for (Long selectedColumnId : selectedColumnIds) {
            selectedColumns.add(getSdxRetrievalService().getColumnById(selectedColumnId));
         }
         getSecurityDefinitionWrapperService().applyRolePermissionOnColumns(selectedColumns, roleName,
                  ExecueBasePermissionType.GRANT, true);

         if (ExecueCoreUtil.isCollectionNotEmpty(unSelectedColumnIds)) {
            List<Colum> unSelectedColumns = new ArrayList<Colum>();
            for (Long unSelectedColumnId : unSelectedColumnIds) {
               unSelectedColumns.add(getSdxRetrievalService().getColumnById(unSelectedColumnId));
            }
            getSecurityDefinitionWrapperService().applyRolePermissionOnColumns(unSelectedColumns, roleName,
                     ExecueBasePermissionType.REVOKE, true);

         }
      } catch (SDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      } catch (SecurityException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }

   }

   @Override
   public void addMembersPermission (String roleName, List<Long> selectedMemberIds, List<Long> permittedMemberIds)
            throws HandlerException {
      List<Long> unSelectedMemberIds = new ArrayList<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(permittedMemberIds)) {
         for (Long memberId : permittedMemberIds) {
            if (!selectedMemberIds.contains(memberId)) {
               unSelectedMemberIds.add(memberId);
            }
         }
      }

      try {
         List<Membr> selectedMembers = new ArrayList<Membr>();
         for (Long selectedMemberId : selectedMemberIds) {
            selectedMembers.add(getSdxRetrievalService().getMemberById(selectedMemberId));
         }
         getSecurityDefinitionWrapperService().applyRolePermissionOnMembers(selectedMembers, roleName,
                  ExecueBasePermissionType.GRANT, true);

         if (ExecueCoreUtil.isCollectionNotEmpty(unSelectedMemberIds)) {
            List<Membr> unSelectedMembers = new ArrayList<Membr>();
            for (Long unSelectedMemberId : unSelectedMemberIds) {
               unSelectedMembers.add(getSdxRetrievalService().getMemberById(unSelectedMemberId));
            }
            getSecurityDefinitionWrapperService().applyRolePermissionOnMembers(unSelectedMembers, roleName,
                     ExecueBasePermissionType.REVOKE, true);

         }
      } catch (SDXException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      } catch (SecurityException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }

   }

   @Override
   public List<Long> getPermittedAssetIdsForRole (String roleName, List<Asset> assets) throws HandlerException {
      try {
         return getSecurityDefinitionWrapperService().getPermittedObjectIdsForRole(roleName, assets);
      } catch (SecurityException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   @Override
   public List<Long> getPermittedTableIdsForRole (String roleName, List<UITable> pageTables) throws HandlerException {
      try {
         return getSecurityDefinitionWrapperService().getPermittedObjectIdsForRole(roleName,
                  transformIntoTables(pageTables));
      } catch (SecurityException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   @Override
   public List<Long> getPermittedColumnIdsForRole (String roleName, List<Colum> pageColumns) throws HandlerException {
      try {
         return getSecurityDefinitionWrapperService().getPermittedObjectIdsForRole(roleName, pageColumns);
      } catch (SecurityException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   @Override
   public List<Long> getPermittedMemberIdsForRole (String roleName, List<UIMember> pageMembers) throws HandlerException {
      try {
         return getSecurityDefinitionWrapperService().getPermittedObjectIdsForRole(roleName,
                  transformIntoMembers(pageMembers));
      } catch (SecurityException e) {
         logger.error(e);
         throw new HandlerException(e.getCode(), e);
      }
   }

   private List<Tabl> transformIntoTables (List<UITable> uiTables) {
      List<Tabl> tables = new ArrayList<Tabl>();
      if (ExecueCoreUtil.isCollectionNotEmpty(uiTables)) {
         for (UITable uiTable : uiTables) {
            Tabl tabel = new Tabl();
            tabel.setId(uiTable.getId());
            tabel.setName(uiTable.getDisplayName());
            tables.add(tabel);
         }
      }
      return tables;
   }

   private List<Membr> transformIntoMembers (List<UIMember> pageMembers) {
      List<Membr> members = new ArrayList<Membr>();
      if (ExecueCoreUtil.isCollectionNotEmpty(pageMembers)) {
         for (UIMember uiMember : pageMembers) {
            Membr membr = new Membr();
            membr.setId(uiMember.getId());
            membr.setLookupValue(uiMember.getName());
            membr.setLongDescription(uiMember.getDescription());
            members.add(membr);
         }
      }
      return members;
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

}
