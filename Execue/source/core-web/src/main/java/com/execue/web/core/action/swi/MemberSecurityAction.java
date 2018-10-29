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


package com.execue.web.core.action.swi;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.type.CheckType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIMember;
import com.execue.handler.bean.UITable;
import com.execue.handler.security.ISDXSecurityServiceHandler;
import com.execue.handler.swi.IPublishedFileColumnEvaluationServiceHandler;
import com.execue.handler.swi.ISDXServiceHandler;

public class MemberSecurityAction extends SWIPaginationAction {

   private static final Logger                          logger           = Logger.getLogger(MemberSecurityAction.class);

   private static final long                            serialVersionUID = 1L;
   private static final int                             PAGE_SIZE        = 60;
   private static final int                             NUMBER_OF_LINKS  = 4;

   private ISDXServiceHandler                           sdxServiceHandler;
   private ISDXSecurityServiceHandler                   sdxSecurityServiceHandler;
   private IPublishedFileColumnEvaluationServiceHandler publishedFileColumnEvaluationServiceHandler;

   private List<UIMember>                               members;
   private Long                                         selectedTableId;
   private Long                                         selectedAssetId;
   private List<Long>                                   permittedMemberIds;
   private List<Long>                                   selectedMemberIds;
   private String                                       selectedRoleName;
   private List<UITable>                                tables;

   //Action methods
   @Override
   public String processPage () throws ExeCueException {
      getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
      getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));

      List<UIMember> pageMembers = getSdxServiceHandler().getAssetTableMembersByPage(selectedAssetId, selectedTableId,
               getPageDetail());
      permittedMemberIds = getSdxSecurityServiceHandler().getPermittedMemberIdsForRole(selectedRoleName, pageMembers);
      members = filterMembersWithAssignedRole(pageMembers, permittedMemberIds);
      //TODO-JT- There is an issue with current pagination with sort field so set it null for now
      getPageDetail().setSortList(null);
      getHttpRequest().put(PAGINATION, getPageDetail());

      return SUCCESS;
   }

   public String showLookupTablesForAsset () {
      try {
         List<UITable> uiTables = getPublishedFileColumnEvaluationServiceHandler()
                  .getAssetLookupTables(selectedAssetId);
         List<Long> permittedTableIds = getSdxSecurityServiceHandler().getPermittedTableIdsForRole(selectedRoleName,
                  uiTables);
         tables = filterTablesWithAssignedRole(uiTables, permittedTableIds);
      } catch (ExeCueException e) {
         logger.error(e);
         e.printStackTrace();
      }

      return SUCCESS;
   }

   public String addMembersPermission () {
      try {
         getSdxSecurityServiceHandler().addMembersPermission(selectedRoleName, selectedMemberIds, permittedMemberIds);
         addActionMessage(getText("execue.object.permission.save.success"));
      } catch (HandlerException e) {
         logger.error(e);
         e.printStackTrace();
         addActionError("Error:" + e.getMessage());
      }
      return SUCCESS;
   }

   private List<UIMember> filterMembersWithAssignedRole (List<UIMember> members, List<Long> permittedMemberIds) {
      for (UIMember member : members) {
         if (ExecueCoreUtil.isCollectionNotEmpty(permittedMemberIds)) {
            for (Long permittedMemberId : permittedMemberIds) {
               if (permittedMemberId.equals(member.getId())) {
                  member.setHasAclPermission(CheckType.YES);
               }
            }
         }
      }
      return members;
   }

   private List<UITable> filterTablesWithAssignedRole (List<UITable> uiTables, List<Long> permittedTableIds) {
      List<UITable> filteredTables = new ArrayList<UITable>();
      for (UITable table : uiTables) {
         if (ExecueCoreUtil.isCollectionNotEmpty(permittedTableIds)) {
            for (Long permittedTableId : permittedTableIds) {
               if (permittedTableId.equals(table.getId())) {
                  filteredTables.add(table);
               }
            }
         }
      }
      return filteredTables;
   }

   /**
    * @return the sdxServiceHandler
    */
   public ISDXServiceHandler getSdxServiceHandler () {
      return sdxServiceHandler;
   }

   /**
    * @param sdxServiceHandler the sdxServiceHandler to set
    */
   public void setSdxServiceHandler (ISDXServiceHandler sdxServiceHandler) {
      this.sdxServiceHandler = sdxServiceHandler;
   }

   /**
    * @return the sdxSecurityServiceHandler
    */
   public ISDXSecurityServiceHandler getSdxSecurityServiceHandler () {
      return sdxSecurityServiceHandler;
   }

   /**
    * @param sdxSecurityServiceHandler the sdxSecurityServiceHandler to set
    */
   public void setSdxSecurityServiceHandler (ISDXSecurityServiceHandler sdxSecurityServiceHandler) {
      this.sdxSecurityServiceHandler = sdxSecurityServiceHandler;
   }

   /**
    * @return the members
    */
   public List<UIMember> getMembers () {
      return members;
   }

   /**
    * @param members the members to set
    */
   public void setMembers (List<UIMember> members) {
      this.members = members;
   }

   /**
    * @return the selectedTableId
    */
   public Long getSelectedTableId () {
      return selectedTableId;
   }

   /**
    * @param selectedTableId the selectedTableId to set
    */
   public void setSelectedTableId (Long selectedTableId) {
      this.selectedTableId = selectedTableId;
   }

   /**
    * @return the selectedAssetId
    */
   public Long getSelectedAssetId () {
      return selectedAssetId;
   }

   /**
    * @param selectedAssetId the selectedAssetId to set
    */
   public void setSelectedAssetId (Long selectedAssetId) {
      this.selectedAssetId = selectedAssetId;
   }

   /**
    * @return the permittedMemberIds
    */
   public List<Long> getPermittedMemberIds () {
      return permittedMemberIds;
   }

   /**
    * @param permittedMemberIds the permittedMemberIds to set
    */
   public void setPermittedMemberIds (List<Long> permittedMemberIds) {
      this.permittedMemberIds = permittedMemberIds;
   }

   /**
    * @return the selectedMemberIds
    */
   public List<Long> getSelectedMemberIds () {
      return selectedMemberIds;
   }

   /**
    * @param selectedMemberIds the selectedMemberIds to set
    */
   public void setSelectedMemberIds (List<Long> selectedMemberIds) {
      this.selectedMemberIds = selectedMemberIds;
   }

   /**
    * @return the selectedRoleName
    */
   public String getSelectedRoleName () {
      return selectedRoleName;
   }

   /**
    * @param selectedRoleName the selectedRoleName to set
    */
   public void setSelectedRoleName (String selectedRoleName) {
      this.selectedRoleName = selectedRoleName;
   }

   /**
    * @return the tables
    */
   public List<UITable> getTables () {
      return tables;
   }

   /**
    * @param tables the tables to set
    */
   public void setTables (List<UITable> tables) {
      this.tables = tables;
   }

   /**
    * @return the publishedFileColumnEvaluationServiceHandler
    */
   public IPublishedFileColumnEvaluationServiceHandler getPublishedFileColumnEvaluationServiceHandler () {
      return publishedFileColumnEvaluationServiceHandler;
   }

   /**
    * @param publishedFileColumnEvaluationServiceHandler the publishedFileColumnEvaluationServiceHandler to set
    */
   public void setPublishedFileColumnEvaluationServiceHandler (
            IPublishedFileColumnEvaluationServiceHandler publishedFileColumnEvaluationServiceHandler) {
      this.publishedFileColumnEvaluationServiceHandler = publishedFileColumnEvaluationServiceHandler;
   }

}
