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

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.platform.swi.IAccessControlPublishService;
import com.execue.platform.swi.IAssetPublishService;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * Implementation for IAssetPublishJobService
 * 
 * @author execue
 */
public class AssetPublishServiceImpl implements IAssetPublishService {

   private ISDXRetrievalService         sdxRetrievalService;
   private ISDXManagementService        sdxManagementService;
   private IAccessControlPublishService accessControlPublishService;
   private IApplicationRetrievalService applicationRetrievalService;

   public PublishAssetMode publishAsset (Long applicationId, Long assetId, Long userId, PublishAssetMode publishMode)
            throws SDXException {
      try {
         processAssetPublishToCommunity(applicationId, assetId, userId, publishMode);
      } catch (SWIException swIException) {
         throw new SDXException(swIException.getCode(), swIException);
      }
      return publishMode;
   }

   //NOTE: -RG- Below logic is no more valid. Refer to the caller code for the Note
   /*
   public PublishAssetMode publishAsset (Long applicationId, Long assetId, Long userId, PublishAssetMode publishMode)
            throws SDXException {
      PublishAssetMode appPublishMode = publishMode;
      try {
         if (getApplicationRetrievalService().isModelEvaluationRequiredByAppSourceType(applicationId)) {
            //appPublishMode = processAssetPublish(applicationId, assetId, userId, publishMode);
            
            // Force Asset publish mode to Community always to support Enterprise Model
            //TODO: -RG- Need to revisited if we need control at asset publish mode as well
            processAssetPublishToCommunity(applicationId, assetId, userId, PublishAssetMode.COMMUNITY);
         }
      } catch (SWIException swIException) {
         throw new SDXException(swIException.getCode(), swIException);
      }
      return appPublishMode;
   }
   */

   /**
    * @param applicationId
    * @param assetId
    * @param userId
    * @param publishMode
    * @return
    * @throws SDXException
    * @throws SWIException
    */
   private PublishAssetMode processAssetPublish (Long applicationId, Long assetId, Long userId,
            PublishAssetMode publishMode) throws SDXException, SWIException {

      Asset asset = getSdxRetrievalService().getAsset(assetId);
      // update the asset for publish mode
      PublishAssetMode previousAssetPublishMode = asset.getPublishMode();
      asset.setPublishMode(publishMode);
      getSdxManagementService().updateAsset(asset);

      // Apply asset security permissions
      // TODO:: NK/GA:: NEED TO REVISIT COMMUNITY/LOCAL based permission logic for portal
      // AS SECURITY IS NOW HANDLED IN SEPARATE SecurityDefinitionPublishWrapperServiceImpl service
      // getAccessControlPublishService().publishAccessControl(userId, asset, previousAssetPublishMode);


      // If at the least one asset is published for Community then Application should be
      // published to Community else published to Local only
      int publiclyAvailableAssetCount = getSdxRetrievalService().getPubliclyAvailableAssetCount(applicationId);
      PublishAssetMode appPublishMode = PublishAssetMode.LOCAL;
      if (publiclyAvailableAssetCount > 0) {
         appPublishMode = PublishAssetMode.COMMUNITY;
      }
      return appPublishMode;
   }
   
   private void processAssetPublishToCommunity (Long applicationId, Long assetId, Long userId,
            PublishAssetMode publishMode) throws SDXException, SWIException {
      Asset asset = getSdxRetrievalService().getAsset(assetId);
      asset.setPublishMode(publishMode);
      getSdxManagementService().updateAsset(asset);
   }

   public IAccessControlPublishService getAccessControlPublishService () {
      return accessControlPublishService;
   }

   public void setAccessControlPublishService (IAccessControlPublishService accessControlPublishService) {
      this.accessControlPublishService = accessControlPublishService;
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

   /**
    * @return the applicationRetrievalService
    */
   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   /**
    * @param applicationRetrievalService
    *           the applicationRetrievalService to set
    */
   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

}
