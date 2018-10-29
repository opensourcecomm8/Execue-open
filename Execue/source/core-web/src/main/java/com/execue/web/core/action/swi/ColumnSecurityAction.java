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

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.type.CheckType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIColumn;
import com.execue.handler.bean.UITable;
import com.execue.handler.security.ISDXSecurityServiceHandler;
import com.execue.handler.swi.ISDXServiceHandler;

/**
 * 
 * @author Jitendra
 *
 */
public class ColumnSecurityAction extends SWIPaginationAction {

   private static final Logger        logger           = Logger.getLogger(ColumnSecurityAction.class);

   private static final long          serialVersionUID = 1L;
   private static final int           PAGE_SIZE        = 60;
   private static final int           NUMBER_OF_LINKS  = 4;

   private ISDXServiceHandler         sdxServiceHandler;
   private ISDXSecurityServiceHandler sdxSecurityServiceHandler;

   private List<UIColumn>             columns;
   private Long                       selectedTableId;
   private Long                       selectedAssetId;
   private List<Long>                 permittedColumnIds;
   private List<Long>                 selectedColumnIds;
   private String                     selectedRoleName;
   private List<UITable>              tables;

   @Override
   public String processPage () throws ExeCueException {
      getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
      getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
      UITable uiTable = new UITable();
      uiTable.setId(selectedTableId);
      List<Colum> pageColumns = getSdxServiceHandler().getAssetTableColumnsByPage(selectedAssetId, selectedTableId,
               getPageDetail());
      permittedColumnIds = getSdxSecurityServiceHandler().getPermittedColumnIdsForRole(selectedRoleName, pageColumns);
      columns = filterColumnsWithAssignedRole(pageColumns, permittedColumnIds);
      //TODO-JT- There is an issue with current pagination with sort field so set it null for now
      getPageDetail().setSortList(null);
      getHttpRequest().put(PAGINATION, getPageDetail());

      return SUCCESS;
   }

   public String showTablesForAsset () {
      Asset asset = new Asset();
      asset.setId(selectedAssetId);
      try {
         List<UITable> uiTables = getSdxServiceHandler().getAllTables(asset);
         List<Long> permittedTableIds = getSdxSecurityServiceHandler().getPermittedTableIdsForRole(selectedRoleName,
                  uiTables);
         tables = filterTablesWithAssignedRole(uiTables, permittedTableIds);
      } catch (ExeCueException e) {
         logger.error(e);
         e.printStackTrace();
      }

      return SUCCESS;
   }

   public String addColumnsPermission () {
      try {
         getSdxSecurityServiceHandler().addColumnsPermission(selectedRoleName, selectedColumnIds, permittedColumnIds);
         addActionMessage(getText("execue.object.permission.save.success"));
      } catch (HandlerException e) {
         logger.error(e);
         e.printStackTrace();
         addActionError("Error:" + e.getMessage());
      }
      return SUCCESS;
   }

   private List<UITable> filterTablesWithAssignedRole (List<UITable> tables, List<Long> permittedTableIds) {
      List<UITable> filteredTables = new ArrayList<UITable>();
      for (UITable table : tables) {
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

   private List<UIColumn> filterColumnsWithAssignedRole (List<Colum> pageColumns, List<Long> permittedColumnIds) {
      List<UIColumn> uiColums = transformIntoUIColums(pageColumns);
      for (UIColumn column : uiColums) {
         if (ExecueCoreUtil.isCollectionNotEmpty(permittedColumnIds)) {
            for (Long permittedColumnId : permittedColumnIds) {
               if (permittedColumnId.equals(column.getId())) {
                  column.setHasAclPermission(CheckType.YES);
               }
            }
         }
      }
      return uiColums;
   }

   private List<UIColumn> transformIntoUIColums (List<Colum> pageColumns) {
      List<UIColumn> uiColums = new ArrayList<UIColumn>();
      if (ExecueCoreUtil.isCollectionNotEmpty(pageColumns)) {
         for (Colum column : pageColumns) {
            UIColumn uiColumn = new UIColumn();
            uiColumn.setId(column.getId());
            uiColumn.setName(column.getName());
            uiColums.add(uiColumn);
         }
      }
      return uiColums;
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
    * @return the permittedColumnIds
    */
   public List<Long> getPermittedColumnIds () {
      return permittedColumnIds;
   }

   /**
    * @param permittedColumnIds the permittedColumnIds to set
    */
   public void setPermittedColumnIds (List<Long> permittedColumnIds) {
      this.permittedColumnIds = permittedColumnIds;
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
    * @return the columns
    */
   public List<UIColumn> getColumns () {
      return columns;
   }

   /**
    * @param columns the columns to set
    */
   public void setColumns (List<UIColumn> columns) {
      this.columns = columns;
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
    * @return the selectedColumnIds
    */
   public List<Long> getSelectedColumnIds () {
      return selectedColumnIds;
   }

   /**
    * @param selectedColumnIds the selectedColumnIds to set
    */
   public void setSelectedColumnIds (List<Long> selectedColumnIds) {
      this.selectedColumnIds = selectedColumnIds;
   }

}
