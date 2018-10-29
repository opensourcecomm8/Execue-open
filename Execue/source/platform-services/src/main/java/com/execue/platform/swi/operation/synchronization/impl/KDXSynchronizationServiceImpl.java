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


package com.execue.platform.swi.operation.synchronization.impl;

import com.execue.core.common.bean.swi.AssetSyncAbsorptionContext;
import com.execue.core.common.bean.swi.AssetSyncInfo;
import com.execue.core.common.bean.swi.AssetSynchronizationContext;
import com.execue.core.common.bean.swi.IndexFormManagementContext;
import com.execue.core.common.type.SyncRequestLevel;
import com.execue.platform.exception.AssetSynchronizationException;
import com.execue.platform.swi.IAssetActivationService;
import com.execue.platform.swi.IIndexFormManagementService;
import com.execue.platform.swi.operation.synchronization.IAssetSyncPopulateService;
import com.execue.platform.swi.operation.synchronization.IKDXSynchronizationService;
import com.execue.platform.swi.operation.synchronization.IParentAssetSyncAbsorptionService;
import com.execue.swi.exception.SWIException;

/**
 * @author John Mallavalli
 */
public class KDXSynchronizationServiceImpl implements IKDXSynchronizationService {

   private IAssetSyncPopulateService         assetSyncPopulateService;
   private IAssetActivationService           assetActivationService;
   private IParentAssetSyncAbsorptionService parentAssetSyncAbsorptionService;
   private IIndexFormManagementService       indexFormManagementService;

   public void synchronizeKDX (Long modelId, Long assetId, Long userId) throws SWIException {
      try {
         // 1. deactivate the asset
         // TODO: populate the UserId
         // assetActivationService.inactivateAsset(userId, assetId);

         // 2. run the sync service to get the member sync info only
         AssetSynchronizationContext assetSynchronizationContext = new AssetSynchronizationContext();
         assetSynchronizationContext.setAssetId(assetId);
         assetSynchronizationContext.setUserId(userId);
         assetSynchronizationContext.setSyncRequestLevel(SyncRequestLevel.MEMBER);

         AssetSyncInfo assetSyncInfo = assetSyncPopulateService.populateAssetSyncInfo(assetSynchronizationContext);
         if (assetSyncInfo.isSyncChangedFound()) {

            AssetSyncAbsorptionContext assetSyncAbsorptionContext = new AssetSyncAbsorptionContext();
            assetSyncAbsorptionContext.setAssetSyncInfo(assetSyncInfo);
            assetSyncAbsorptionContext.setAssetId(assetSynchronizationContext.getAssetId());
            assetSyncAbsorptionContext.setApplicationId(assetSynchronizationContext.getApplicationId());
            assetSyncAbsorptionContext.setModelId(assetSynchronizationContext.getModelId());
            assetSyncAbsorptionContext.setUserId(assetSynchronizationContext.getUserId());
            assetSyncAbsorptionContext.setSyncRequestLevel(assetSynchronizationContext.getSyncRequestLevel());

            // TODO -RG- Correct Below Service ASAP
            getParentAssetSyncAbsorptionService().absorbAssetSyncInfo(assetSyncAbsorptionContext);
         }

         // TODO : -RG- Correct Mappings and DDV execution, deletion of BED Maint info should be happenign here?

         // 3. manage the index forms
         IndexFormManagementContext indexFormManagementContext = new IndexFormManagementContext();
         indexFormManagementContext.setModelId(modelId);
         indexFormManagementService.manageIndexForms(indexFormManagementContext);

         // TODO: In the above step, the new instances will be identified and the corresponding index forms will be
         // created, but what about the sync-up that needs to happen between the members & instances in the case where
         // the member's lookup-desc/display-name/name have changed and for those members which get deleted

         // 4. activate the asset
         // TODO: populate the UserId
         // assetActivationService.activateAsset(userId, assetId);
      } catch (AssetSynchronizationException e) {
         e.printStackTrace();
      }
   }

   public IAssetSyncPopulateService getAssetSyncPopulateService () {
      return assetSyncPopulateService;
   }

   public void setAssetSyncPopulateService (IAssetSyncPopulateService assetSyncPopulateService) {
      this.assetSyncPopulateService = assetSyncPopulateService;
   }

   public IAssetActivationService getAssetActivationService () {
      return assetActivationService;
   }

   public void setAssetActivationService (IAssetActivationService assetActivationService) {
      this.assetActivationService = assetActivationService;
   }

   public IIndexFormManagementService getIndexFormManagementService () {
      return indexFormManagementService;
   }

   public void setIndexFormManagementService (IIndexFormManagementService indexFormManagementService) {
      this.indexFormManagementService = indexFormManagementService;
   }

   public IParentAssetSyncAbsorptionService getParentAssetSyncAbsorptionService () {
      return parentAssetSyncAbsorptionService;
   }

   public void setParentAssetSyncAbsorptionService (IParentAssetSyncAbsorptionService parentAssetSyncAbsorptionService) {
      this.parentAssetSyncAbsorptionService = parentAssetSyncAbsorptionService;
   }
}
