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

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.ApplicationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.handler.swi.IPreferencesServiceHandler;
import com.execue.handler.swi.ISDXServiceHandler;

public class AssetSelectionAction extends SWIAction {

   private static final Logger        log = Logger.getLogger(AssetSelectionAction.class);
   private List<Asset>                assets;
   private String                     sourceName;
   private String                     assetName;
   private Long                       assetId;
   private IPreferencesServiceHandler preferencesServiceHandler;
   private ISDXServiceHandler         sdxServiceHandler;
   private Long                       selectedAssetId;

   public String submitSelectedAsset () {
      String result = SUCCESS;
      ApplicationContext appContext = getApplicationContext();
      if (selectedAssetId == null || selectedAssetId <= 0) {// Forward to new Asset
         appContext.setAssetId(null);
         appContext.setAssetName(null);
         return ASSET_NEW;
      } else {
         appContext.setAssetId(selectedAssetId);
         appContext.setAssetName(assetName);
      }
      if (JOIN.equalsIgnoreCase(sourceName)) {
         result = JOIN;
      } else if (MAPPING.equalsIgnoreCase(sourceName)) {
         result = MAPPING;
      } else if (CONSTRAINTS.equalsIgnoreCase(sourceName)) {
         result = CONSTRAINTS;
      } else if (CUBE_REQUEST.equalsIgnoreCase(sourceName)) {
         result = CUBE_REQUEST;
      } else if (ASSET.equalsIgnoreCase(sourceName)) {
         result = ASSET;
      } else if (DEFAULT_METRICS.equalsIgnoreCase(sourceName)) {
         result = DEFAULT_METRICS;
      } else if (DEFAULT_DYNAMIC_VALUE.equalsIgnoreCase(sourceName)) {
         result = DEFAULT_DYNAMIC_VALUE;
      } else if (MART_CREATION.equalsIgnoreCase(sourceName)) {
         result = MART_CREATION;
      }
      return result;
   }

   public List<Asset> getAssets () {
      return assets;
   }

   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

   public String getSourceName () {
      return sourceName;
   }

   public void setSourceName (String sourceName) {
      this.sourceName = sourceName;
   }

   public IPreferencesServiceHandler getPreferencesServiceHandler () {
      return preferencesServiceHandler;
   }

   public void setPreferencesServiceHandler (IPreferencesServiceHandler preferencesServiceHandler) {
      this.preferencesServiceHandler = preferencesServiceHandler;
   }

   public ISDXServiceHandler getSdxServiceHandler () {
      return sdxServiceHandler;
   }

   public void setSdxServiceHandler (ISDXServiceHandler sdxServiceHandler) {
      this.sdxServiceHandler = sdxServiceHandler;
   }

   public String getAssetName () {
      return assetName;
   }

   public void setAssetName (String assetName) {
      this.assetName = assetName;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   /**
    * @return the selectedAssetId
    */
   public Long getSelectedAssetId () {
      return selectedAssetId;
   }

   /**
    * @param selectedAssetId
    *           the selectedAssetId to set
    */
   public void setSelectedAssetId (Long selectedAssetId) {
      this.selectedAssetId = selectedAssetId;
   }

}
