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

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.type.CheckType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIAsset;
import com.execue.handler.bean.UITable;
import com.execue.handler.security.ISDXSecurityServiceHandler;
import com.execue.handler.swi.ISDXServiceHandler;

/**
 * 
 * @author Jitendra
 *
 */
public class TableSecurityAction extends SWIPaginationAction {

   private static final Logger        logger           = Logger.getLogger(TableSecurityAction.class);

   private static final long          serialVersionUID = 1L;
   private static final int           PAGE_SIZE        = 60;
   private static final int           NUMBER_OF_LINKS  = 4;

   private List<UITable>              tables;
   private List<UIAsset>              assets;
   private String                     selectedRoleName;
   private List<Long>                 selectedTableIds;
   private Long                       selectedAssetId;

   private ISDXServiceHandler         sdxServiceHandler;
   private ISDXSecurityServiceHandler sdxSecurityServiceHandler;

   private List<Long>                 permittedTableIds;

   @Override
   public String processPage () throws ExeCueException {
      getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
      getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
      Asset selectedAsset = getSdxServiceHandler().getAsset(selectedAssetId);
      List<UITable> listOfTable = getSdxServiceHandler().getAllTables(selectedAsset);
      List<UITable> pageTables = getProcessedResults(listOfTable, getPageDetail());

      permittedTableIds = getSdxSecurityServiceHandler().getPermittedTableIdsForRole(selectedRoleName, pageTables);
      tables = filterTablesWithAssignedRole(pageTables, permittedTableIds);
      // push the page object into request
      //TODO-JT- There is an issue with current pagination with sort field so set it null for now
      getPageDetail().setSortList(null);
      getHttpRequest().put(PAGINATION, getPageDetail());

      return SUCCESS;
   }

   public String addTablesPermission () {

      try {
         getSdxSecurityServiceHandler().addTablesPermission(selectedRoleName, selectedTableIds, permittedTableIds);
         addActionMessage(getText("execue.object.permission.save.success"));
      } catch (HandlerException e) {
         logger.error(e);
         e.printStackTrace();
         addActionError("Error:" + e.getMessage());
      }
      return SUCCESS;
   }

   public String showPemittedAssetForRole () {
      try {
         List<Asset> allParentAssets = getSdxServiceHandler().getAllParentAssets(getApplicationContext().getAppId());
         List<Long> permittedAssetIdsForRole = getSdxSecurityServiceHandler().getPermittedAssetIdsForRole(
                  selectedRoleName, allParentAssets);
         assets = filterAssetsWithAssignedRole(allParentAssets, permittedAssetIdsForRole);
      } catch (HandlerException e) {
         logger.error(e);
         e.printStackTrace();
      } catch (ExeCueException e) {
         logger.error(e);
         e.printStackTrace();
      }

      return SUCCESS;
   }

   private List<UITable> getProcessedResults (List<UITable> listOfTable, Page pageDetail) {
      // modify the page object with the new record count which will modify the page count as well
      pageDetail.setRecordCount(Long.valueOf(listOfTable.size()));

      List<UITable> pageTables = new ArrayList<UITable>();
      // manipulate the list to return the set of concepts belonging to the page requested
      int start = (pageDetail.getRequestedPage().intValue() - 1) * pageDetail.getPageSize().intValue();
      int end = start + pageDetail.getPageSize().intValue();
      if (end > pageDetail.getRecordCount().intValue()) {
         end = (pageDetail.getRecordCount().intValue());
      }
      for (int i = start; i < end; i++) {
         pageTables.add(listOfTable.get(i));
      }
      return pageTables;
   }

   private List<UITable> filterTablesWithAssignedRole (List<UITable> pageTables, List<Long> permittedTableIds) {
      for (UITable table : pageTables) {
         if (ExecueCoreUtil.isCollectionNotEmpty(permittedTableIds)) {
            for (Long permittedTableId : permittedTableIds) {
               if (permittedTableId.equals(table.getId())) {
                  table.setHasAclPermission(CheckType.YES);
               }
            }
         }
      }
      return pageTables;
   }

   private List<UIAsset> filterAssetsWithAssignedRole (List<Asset> assets, List<Long> permittedAssetIds) {
      List<UIAsset> assetsForSelectedRole = new ArrayList<UIAsset>();
      for (UIAsset asset : transformUIAssets(assets)) {
         if (ExecueCoreUtil.isCollectionNotEmpty(permittedAssetIds)) {
            for (Long permittedAssetId : permittedAssetIds) {
               if (permittedAssetId.equals(asset.getId())) {
                  assetsForSelectedRole.add(asset);
               }
            }
         }
      }

      return assetsForSelectedRole;
   }

   private List<UIAsset> transformUIAssets (List<Asset> assets) {
      List<UIAsset> uiAssets = new ArrayList<UIAsset>();
      if (ExecueCoreUtil.isCollectionNotEmpty(assets)) {
         for (Asset asset : assets) {
            UIAsset uiAsset = new UIAsset();
            uiAsset.setId(asset.getId());
            uiAsset.setName(asset.getDisplayName());
            uiAssets.add(uiAsset);
         }
      }
      return uiAssets;
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
    * @return the assets
    */
   public List<UIAsset> getAssets () {
      return assets;
   }

   /**
    * @param assets the assets to set
    */
   public void setAssets (List<UIAsset> assets) {
      this.assets = assets;
   }

   /**
    * @return the selectedTableIds
    */
   public List<Long> getSelectedTableIds () {
      return selectedTableIds;
   }

   /**
    * @param selectedTableIds the selectedTableIds to set
    */
   public void setSelectedTableIds (List<Long> selectedTableIds) {
      this.selectedTableIds = selectedTableIds;
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
    * @return the permittedTableIds
    */
   public List<Long> getPermittedTableIds () {
      return permittedTableIds;
   }

   /**
    * @param permittedTableIds the permittedTableIds to set
    */
   public void setPermittedTableIds (List<Long> permittedTableIds) {
      this.permittedTableIds = permittedTableIds;
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

}
