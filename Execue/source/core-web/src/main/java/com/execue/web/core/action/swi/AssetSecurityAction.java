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
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.CheckType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIApplicationModelInfo;
import com.execue.handler.bean.UIAsset;
import com.execue.handler.security.ISDXSecurityServiceHandler;
import com.execue.handler.swi.IApplicationServiceHandler;
import com.execue.handler.swi.ISDXServiceHandler;
import com.execue.handler.swi.IUsersHandler;

/**
 * 
 * @author Jitendra
 *
 */
public class AssetSecurityAction extends SWIPaginationAction {

   private static final Logger          logger          = Logger.getLogger(AssetSecurityAction.class);

   private List<User>                   users;
   private List<UIApplicationModelInfo> applications;
   private List<UIAsset>                assets;
   private boolean                      isRoleBased     = true;
   private Long                         selectedApplicationId;
   private List<Long>                   selectedAssetIds;
   private List<Long>                   selectedUserIds;
   private List<String>                 selectedRoles;

   private ISDXSecurityServiceHandler   sdxSecurityServiceHandler;
   private IApplicationServiceHandler   applicationServiceHandler;
   private ISDXServiceHandler           sdxServiceHandler;

   private IUsersHandler                usersHandler;
   private String                       selectedRoleName;
   private List<Long>                   permittedAssetIds;

   private static final int             PAGE_SIZE       = 60;
   private static final int             NUMBER_OF_LINKS = 4;

   @Override
   public String processPage () throws ExeCueException {
      try {
         getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
         getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
         List<Asset> listOfDataset = getSdxServiceHandler().getAllParentAssets(selectedApplicationId);
         // set the record count
         getPageDetail().setRecordCount(Long.valueOf(listOfDataset.size()));
         List<Asset> pageAssets = getProcessedResults(listOfDataset, getPageDetail());

         permittedAssetIds = getSdxSecurityServiceHandler().getPermittedAssetIdsForRole(selectedRoleName, pageAssets);
         assets = filterAssetsWithAssignedRole(pageAssets, permittedAssetIds);
         // push the page object into request
         //TODO-JT- There is an issue with current pagination with sort field so set it null for now
         getPageDetail().setSortList(null);
         getHttpRequest().put(PAGINATION, getPageDetail());
      } catch (Exception exception) {
         logger.error(exception, exception);
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
      return SUCCESS;
   }

   public String showApplications () {
      try {
         applications = getApplicationServiceHandler().getAllApplicationsOrderedByName();
      } catch (HandlerException e) {
         logger.error(e);
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String showUsers () {
      try {
         users = getUsersHandler().getUsers();
         isRoleBased = false;
      } catch (HandlerException e) {
         logger.error(e);
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String addAssetsPermission () {

      try {
         getSdxSecurityServiceHandler().addAssetsPermission(selectedRoleName, selectedAssetIds, permittedAssetIds);
         addActionMessage(getText("execue.object.permission.save.success"));
      } catch (HandlerException e) {
         logger.error(e);
         e.printStackTrace();
         addActionError("Error:" + e.getMessage());
      }
      return SUCCESS;
   }

   private List<Asset> getProcessedResults (List<Asset> listOfDataset, Page pageDetail) {
      // modify the page object with the new record count which will modify the page count as well
      pageDetail.setRecordCount(Long.valueOf(listOfDataset.size()));
      List<Asset> pageDatasets = new ArrayList<Asset>();
      // manipulate the list to return the set of concepts belonging to the page requested
      int start = (pageDetail.getRequestedPage().intValue() - 1) * pageDetail.getPageSize().intValue();
      int end = start + pageDetail.getPageSize().intValue();
      if (end > pageDetail.getRecordCount().intValue()) {
         end = (pageDetail.getRecordCount().intValue());
      }
      for (int i = start; i < end; i++) {
         pageDatasets.add(listOfDataset.get(i));
      }
      return pageDatasets;
   }

   private List<UIAsset> filterAssetsWithAssignedRole (List<Asset> pageAssets, List<Long> permittedAssetIds) {
      List<UIAsset> uiAssets = new ArrayList<UIAsset>();
      for (UIAsset asset : transformUIAssets(pageAssets)) {
         uiAssets.add(asset);
         if (ExecueCoreUtil.isCollectionNotEmpty(permittedAssetIds)) {
            for (Long permittedAssetId : permittedAssetIds) {
               if (permittedAssetId.equals(asset.getId())) {
                  asset.setHasAclPermission(CheckType.YES);
               }
            }

         }
      }
      return uiAssets;
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
    * @return the applicationServiceHandler
    */
   public IApplicationServiceHandler getApplicationServiceHandler () {
      return applicationServiceHandler;
   }

   /**
    * @param applicationServiceHandler the applicationServiceHandler to set
    */
   public void setApplicationServiceHandler (IApplicationServiceHandler applicationServiceHandler) {
      this.applicationServiceHandler = applicationServiceHandler;
   }

   /**
    * @return the applications
    */
   public List<UIApplicationModelInfo> getApplications () {
      return applications;
   }

   /**
    * @param applications the applications to set
    */
   public void setApplications (List<UIApplicationModelInfo> applications) {
      this.applications = applications;
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
    * @return the selectedApplicationId
    */
   public Long getSelectedApplicationId () {
      return selectedApplicationId;
   }

   /**
    * @param selectedApplicationId the selectedApplicationId to set
    */
   public void setSelectedApplicationId (Long selectedApplicationId) {
      this.selectedApplicationId = selectedApplicationId;
   }

   /**
    * @return the usersHandler
    */
   public IUsersHandler getUsersHandler () {
      return usersHandler;
   }

   /**
    * @param usersHandler the usersHandler to set
    */
   public void setUsersHandler (IUsersHandler usersHandler) {
      this.usersHandler = usersHandler;
   }

   /**
    * @return the users
    */
   public List<User> getUsers () {
      return users;
   }

   /**
    * @param users the users to set
    */
   public void setUsers (List<User> users) {
      this.users = users;
   }

   /**
    * @return the isRoleBased
    */
   public boolean isRoleBased () {
      return isRoleBased;
   }

   /**
    * @param isRoleBased the isRoleBased to set
    */
   public void setRoleBased (boolean isRoleBased) {
      this.isRoleBased = isRoleBased;
   }

   /**
    * @return the selectedAssetIds
    */
   public List<Long> getSelectedAssetIds () {
      return selectedAssetIds;
   }

   /**
    * @param selectedAssetIds the selectedAssetIds to set
    */
   public void setSelectedAssetIds (List<Long> selectedAssetIds) {
      this.selectedAssetIds = selectedAssetIds;
   }

   /**
    * @return the selectedUserIds
    */
   public List<Long> getSelectedUserIds () {
      return selectedUserIds;
   }

   /**
    * @param selectedUserIds the selectedUserIds to set
    */
   public void setSelectedUserIds (List<Long> selectedUserIds) {
      this.selectedUserIds = selectedUserIds;
   }

   /**
    * @return the selectedRoles
    */
   public List<String> getSelectedRoles () {
      return selectedRoles;
   }

   /**
    * @param selectedRoles the selectedRoles to set
    */
   public void setSelectedRoles (List<String> selectedRoles) {
      this.selectedRoles = selectedRoles;
   }

   /**
    * @return the permittedAssetIds
    */
   public List<Long> getPermittedAssetIds () {
      return permittedAssetIds;
   }

   /**
    * @param permittedAssetIds the permittedAssetIds to set
    */
   public void setPermittedAssetIds (List<Long> permittedAssetIds) {
      this.permittedAssetIds = permittedAssetIds;
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
