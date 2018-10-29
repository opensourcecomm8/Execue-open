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
import com.execue.core.common.bean.swi.SDXSynchronizationContext;
import com.execue.core.common.type.OperationRequestLevel;
import com.execue.platform.exception.AssetSynchronizationException;
import com.execue.platform.swi.operation.synchronization.IAssetSyncConsumptionService;
import com.execue.platform.swi.operation.synchronization.IAssetSyncPopulateService;
import com.execue.platform.swi.operation.synchronization.IAssetSynchronizationWrapperService;

/**
 * This service controls the entire asset synchronization process.
 * 
 * @author Vishay
 * @version 1.0
 * @since 20/08/09
 */
public class AssetSynchronizationWrapperServiceImpl implements IAssetSynchronizationWrapperService {

   private IAssetSyncPopulateService    assetSyncPopulateService;
   private IAssetSyncConsumptionService assetSyncConsumptionService;

   public void synchronizeSDX (SDXSynchronizationContext sdxSynchronizationContext)
            throws AssetSynchronizationException {
      if (OperationRequestLevel.SYSTEM.equals(sdxSynchronizationContext.getRequestLevel())) {
         synchronizeSystem(sdxSynchronizationContext);
      } else if (OperationRequestLevel.APPLICATION.equals(sdxSynchronizationContext.getRequestLevel())) {
         synchronizeApplication(sdxSynchronizationContext);
      } else {
         synchronizeAsset(sdxSynchronizationContext);
      }
   }

   private void synchronizeSystem (SDXSynchronizationContext sdxSynchronizationContext)
            throws AssetSynchronizationException {
      /*
       * Get all the applications and on each of them call synchronizeApplication(SDXSynchronizationContext)
            by cloning the context and adjusting it accordingly with modelId
       */
   }

   private void synchronizeApplication (SDXSynchronizationContext sdxSynchronizationContext)
            throws AssetSynchronizationException {
      /*
       * get All the parent assets under the application
         on each of them call synchronizeAsset(SDXSynchronizationContext)
            by cloning the context and adjusting it accordingly
       */
   }

   private void synchronizeAsset (SDXSynchronizationContext sdxSynchronizationContext)
            throws AssetSynchronizationException {
      /*
        asset on which operation is requested should be in active state when this operation is being invoked
            else log a message and return from here
         validate whether this invocation can be proceeded or not based on the completion of the previous cycle
            if can be proceeded follow the process as below
            if can not be proceeded then log the message in job operational status and return from here
            this validation should be coming from dedicated service of sync package (so that can be called if needed by the handler)
         call the AssetSynchronizationService and get the output in the form of AssetSyncInfo
         call AssetSyncConsumptionService service with out put of previous call
       */
      // TODO : -RG- Prior Validation is needed

      AssetSynchronizationContext assetSynchronizationContext = new AssetSynchronizationContext();
      assetSynchronizationContext.setAssetId(sdxSynchronizationContext.getId());
      assetSynchronizationContext.setApplicationId(sdxSynchronizationContext.getApplicationId());
      assetSynchronizationContext.setModelId(sdxSynchronizationContext.getModelId());
      assetSynchronizationContext.setUserId(sdxSynchronizationContext.getUserId());
      assetSynchronizationContext.setJobRequest(sdxSynchronizationContext.getJobRequest());
      assetSynchronizationContext.setSyncRequestLevel(sdxSynchronizationContext.getSyncRequestLevel());
      
      assetSynchronization(assetSynchronizationContext);
   }

   private void assetSynchronization (AssetSynchronizationContext assetSynchronizationContext)
            throws AssetSynchronizationException {
      AssetSyncInfo assetSyncInfo = getAssetSyncPopulateService().populateAssetSyncInfo(assetSynchronizationContext);
      if (assetSyncInfo.isSyncChangedFound()) {

         AssetSyncAbsorptionContext assetSyncAbsorptionContext = new AssetSyncAbsorptionContext();
         assetSyncAbsorptionContext.setAssetSyncInfo(assetSyncInfo);
         assetSyncAbsorptionContext.setAssetId(assetSynchronizationContext.getAssetId());
         assetSyncAbsorptionContext.setApplicationId(assetSynchronizationContext.getApplicationId());
         assetSyncAbsorptionContext.setModelId(assetSynchronizationContext.getModelId());
         assetSyncAbsorptionContext.setUserId(assetSynchronizationContext.getUserId());
         assetSyncAbsorptionContext.setSyncRequestLevel(assetSynchronizationContext.getSyncRequestLevel());

         // call consumption service
         getAssetSyncConsumptionService().consumeAssetSyncInfo(assetSyncAbsorptionContext);
      } else {
         // TODO : -RG- Log proper information that there is no change found
      }
   }

   public IAssetSyncPopulateService getAssetSyncPopulateService () {
      return assetSyncPopulateService;
   }

   public void setAssetSyncPopulateService (IAssetSyncPopulateService assetSyncPopulateService) {
      this.assetSyncPopulateService = assetSyncPopulateService;
   }

   public IAssetSyncConsumptionService getAssetSyncConsumptionService () {
      return assetSyncConsumptionService;
   }

   public void setAssetSyncConsumptionService (IAssetSyncConsumptionService assetSyncConsumptionService) {
      this.assetSyncConsumptionService = assetSyncConsumptionService;
   }

}
