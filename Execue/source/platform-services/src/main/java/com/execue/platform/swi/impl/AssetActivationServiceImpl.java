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


package com.execue.platform.swi.impl;

import java.util.List;

import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.bean.entity.Asset;
import com.execue.platform.swi.IAssetActivationService;
import com.execue.platform.swi.ICorrectMappingService;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IDefaultMetricService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * Implementation for IAssetActivationService
 * 
 * @author execue
 */
public class AssetActivationServiceImpl implements IAssetActivationService {

   ICorrectMappingService correctMappingService;
   ISDXRetrievalService   sdxRetrievalService;
   ISDXManagementService  sdxManagementService;
   IDefaultMetricService  defaultMetricService;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IAssetActivationService#activateAsset(java.lang.Long)
    */
   public void activateAsset (Long userId, Long assetId) throws SDXException {
      try {
         Asset asset = getSdxRetrievalService().getAsset(assetId);

         // Correct the mapping for the asset
         getCorrectMappingService().correctMappingsForAsset(asset.getApplication().getId(), assetId);

         // populate the default metrics
         getDefaultMetricService().populateDefaultMetricsAsset(assetId);

         // Activate the asset
         asset.setStatus(StatusEnum.ACTIVE);
         getSdxManagementService().updateAsset(asset);

      } catch (MappingException e) {
         throw new SDXException(e.getCode(), e);
      } catch (SWIException e) {
         e.printStackTrace();
         throw new SDXException(e.getCode(), e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.swi.IAssetActivationService#activateAssets(java.util.List)
    */
   public void activateAssets (Long userId, List<Long> assetIds) throws SDXException {
      for (Long assetId : assetIds) {
         activateAsset(userId, assetId);
      }
   }

   public void inactivateAsset (Long userId, Long assetId) throws SDXException {
      try {
         // TODO: -JVK- add any other cleanup tasks that are required to be performed before deactivating the asset
         Asset asset = getSdxRetrievalService().getAsset(assetId);
         asset.setStatus(StatusEnum.INACTIVE);
         getSdxManagementService().updateAsset(asset);
      } catch (SWIException e) {
         e.printStackTrace();
         throw new SDXException(e.getCode(), e);
      }
   }

   // private void applyAssetPermissions (Asset asset) {
   // if (aclService != null) {
   // // TODO: -KA- Move auth creating to dummy auth service. Move role hardcode to acl service constants.
   // GrantedAuthority authority = new GrantedAuthorityImpl("ROLE_PUBLISHER");
   // GrantedAuthority[] list = { authority };
   // Authentication authentication = new AnonymousAuthenticationToken("publisher", "system", list);
   // SecurityContextHolder.getContext().setAuthentication(authentication);
   // getAclService().setAssetObjectPermissions(asset, "ROLE_USER", false);
   // getAclService().setAssetObjectPermissions(asset, "ROLE_ANONYMOUS", false);
   // }
   // }

   public ICorrectMappingService getCorrectMappingService () {
      return correctMappingService;
   }

   public void setCorrectMappingService (ICorrectMappingService correctMappingService) {
      this.correctMappingService = correctMappingService;
   }

   /**
    * @return the defaultMetricService
    */
   public IDefaultMetricService getDefaultMetricService () {
      return defaultMetricService;
   }

   /**
    * @param defaultMetricService
    *           the defaultMetricService to set
    */
   public void setDefaultMetricService (IDefaultMetricService defaultMetricService) {
      this.defaultMetricService = defaultMetricService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public ISDXManagementService getSdxManagementService () {
      return sdxManagementService;
   }

   public void setSdxManagementService (ISDXManagementService sdxManagementService) {
      this.sdxManagementService = sdxManagementService;
   }

}
