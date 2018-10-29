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
import com.execue.core.common.type.AssetGrainType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIAssetGrain;
import com.execue.handler.bean.UIAssetGrainInfo;

/**
 * This action class represents the grain screen.
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/07/09
 */
public class AssetGrainAction extends SWIAction {

   private static final long   serialVersionUID = 1L;

   private static final Logger log              = Logger.getLogger(AssetGrainAction.class);
   private List<Asset>         assets;
   private Asset               asset;
   private UIAssetGrainInfo    assetGrainInfo;
   private List<Long>          selectedAssetGrain;
   private Long                defaultPopulation;
   private Long                defaultDistribution;
   private List<Long>          mappingIds;
   private List<String>        grainTypes;
   private List<String>        displayNames;

   private String              status;
   private String              paginationType;
   public static final int     PAGESIZE         = 11;
   public static final int     numberOfLinks    = 4;
   private String              requestedPage;

   public String input () {
      try {
         assetGrainInfo = getSdxServiceHandler().getAssetGrainInfo(getApplicationContext().getModelId(), asset.getId());
         if (assetGrainInfo != null) {
            if (ExecueCoreUtil.isCollectionEmpty(assetGrainInfo.getExistingAssetGrain())) {
               assetGrainInfo.setUpdatedEligibleAssetGrain(assetGrainInfo.getEligibleGrain());
            } else {
               assetGrainInfo.setUpdatedEligibleAssetGrain(removeExistingGainsFromEligibleAssetGrainList());
            }
            if (assetGrainInfo.getDefaultPopulationGrain() != null) {
               setDefaultPopulation(assetGrainInfo.getDefaultPopulationGrain().getMappingId());
            }
            if (assetGrainInfo.getDefaultDistributionGrain() != null) {
               setDefaultDistribution(assetGrainInfo.getDefaultDistributionGrain().getMappingId());
            }
         }
         if (status != null) {
            if (status.equalsIgnoreCase("success")) {
               addActionMessage(getText("execue.asset.grain.update.success"));
            } else {
               addActionMessage(getText("execue.asset.grain.update.failure"));
            }
         }
      } catch (ExeCueException e) {
         return ERROR;
      }
      return INPUT;
   }

   public String updateAssetGrain () {
      if (log.isDebugEnabled()) {
         log.debug("defaultPopulation Id : " + defaultPopulation);
         log.debug("defaultDistribution Id : " + defaultDistribution);
      }
      List<UIAssetGrain> eligibleGrain = getEligibleGrain();
      UIAssetGrainInfo toBeSavedAssetGrainInfo = new UIAssetGrainInfo();
      List<UIAssetGrain> assetGrain = new ArrayList<UIAssetGrain>();
      if (ExecueCoreUtil.isCollectionNotEmpty(selectedAssetGrain)) {
         for (Long selectedMappingId : selectedAssetGrain) {
            log.debug("Grain Id Selected : " + selectedMappingId);
         }
         UIAssetGrain tempAssetGrain = null;
         for (Long grainId : selectedAssetGrain) {
            tempAssetGrain = getCorrespondingAssetGrain(eligibleGrain, grainId);
            assetGrain.add(tempAssetGrain);
         }
      }
      UIAssetGrain defaultPopulationGrain = null;
      UIAssetGrain defaultDistributionGrain = null;
      if (defaultPopulation != null) {
         defaultPopulationGrain = getCorrespondingAssetGrain(eligibleGrain, defaultPopulation);
      }
      if (defaultDistribution != null) {
         defaultDistributionGrain = getCorrespondingAssetGrain(eligibleGrain, defaultDistribution);
      }
      toBeSavedAssetGrainInfo.setExistingAssetGrain(assetGrain);
      toBeSavedAssetGrainInfo.setDefaultDistributionGrain(defaultDistributionGrain);
      toBeSavedAssetGrainInfo.setDefaultPopulationGrain(defaultPopulationGrain);
      toBeSavedAssetGrainInfo.setDefaultDistributionValue(assetGrainInfo.getDefaultDistributionValue());
      try {
         getSdxServiceHandler().saveAssetGrain(getApplicationContext().getModelId(), toBeSavedAssetGrainInfo,
                  asset.getId());
         addActionMessage(getText("execue.asset.grain.update.success"));
      } catch (ExeCueException e) {
         addActionMessage(getText("execue.asset.grain.update.failure"));
         return ERROR;
      }
      return SUCCESS;
   }

   public String showAllAssets () {
      try {
         assets = getSdxServiceHandler().getAllAssets(getApplicationContext().getAppId());
         if (paginationType != null && paginationType.equalsIgnoreCase("availableAssets")) {
            paginationForAssets();
         }
      } catch (ExeCueException e) {
         return ERROR;
      }
      return SUCCESS;
   }

   @SuppressWarnings ("unchecked")
   public String showSubAllAssets () {
      int reqPageNo = Integer.parseInt(getRequestedPage());
      int fromIndex = 1;
      int toIndex = 1;

      if (paginationType != null && paginationType.equalsIgnoreCase("availableAssets")) {
         List<Asset> assetList = (List<Asset>) getHttpSession().get("ASSETSFORPAGINATION");

         int tempTotCount = (assetList.size() / PAGESIZE);
         int rmndr = assetList.size() % PAGESIZE;
         if (rmndr != 0) {
            tempTotCount++;
         }

         if (reqPageNo <= tempTotCount) {
            fromIndex = ((reqPageNo - 1) * PAGESIZE);
            toIndex = reqPageNo * PAGESIZE;
            if (toIndex > assetList.size())
               toIndex = (assetList.size());
         }

         log.info("Getting Assets SubList from -> " + fromIndex + " to " + toIndex);
         assets = assetList.subList(fromIndex, toIndex);
      }
      return SUCCESS;
   }

   private List<UIAssetGrain> removeExistingGainsFromEligibleAssetGrainList () {
      List<UIAssetGrain> eligibleAssetGrains = new ArrayList<UIAssetGrain>();
      if (ExecueCoreUtil.isCollectionNotEmpty(assetGrainInfo.getEligibleGrain())
               && ExecueCoreUtil.isCollectionNotEmpty(assetGrainInfo.getExistingAssetGrain())) {
         List<UIAssetGrain> existingAssetGrains = assetGrainInfo.getExistingAssetGrain();
         for (UIAssetGrain uiAssetGrain : assetGrainInfo.getEligibleGrain()) {
            UIAssetGrain assetGrain = new UIAssetGrain();
            assetGrain.setConceptDisplayName(uiAssetGrain.getConceptDisplayName());
            assetGrain.setMappingId(uiAssetGrain.getMappingId());
            assetGrain.setGrainType(uiAssetGrain.getGrainType());
            eligibleAssetGrains.add(assetGrain);
         }
         for (UIAssetGrain existingAssetGrain : existingAssetGrains) {
            UIAssetGrain matchedAssetGrain = getMatchedAssetGrain(existingAssetGrain.getMappingId(),
                     eligibleAssetGrains);
            if (matchedAssetGrain != null) {
               eligibleAssetGrains.remove(matchedAssetGrain);
            }
         }
      }
      return eligibleAssetGrains;
   }

   private UIAssetGrain getMatchedAssetGrain (Long existingMappingId, List<UIAssetGrain> eligibleAssetGrains) {
      UIAssetGrain matchedAssetGrain = null;
      for (UIAssetGrain eligibleGrain : eligibleAssetGrains) {
         if (eligibleGrain.getMappingId().longValue() == existingMappingId.longValue()) {
            matchedAssetGrain = eligibleGrain;
            break;
         }
      }
      return matchedAssetGrain;
   }

   private void paginationForAssets () {
      if (requestedPage == null)
         requestedPage = "1";
      getHttpSession().put("ASSETSFORPAGINATION", assets);

      int tempSize = 0;
      if (assets.size() <= PAGESIZE)
         tempSize = assets.size();
      else
         tempSize = PAGESIZE;
      log.info("displaying initial sublist");
      assets = assets.subList(0, tempSize);
   }

   private UIAssetGrain getCorrespondingAssetGrain (List<UIAssetGrain> eligibleGrain, Long grainId) {
      UIAssetGrain assetGrain = new UIAssetGrain();
      if (ExecueCoreUtil.isCollectionNotEmpty(eligibleGrain)) {
         for (UIAssetGrain uAssetGrain : eligibleGrain) {
            if (uAssetGrain.getMappingId().longValue() == grainId.longValue()) {
               assetGrain = uAssetGrain;
               break;
            }
         }
      }
      return assetGrain;
   }

   private List<UIAssetGrain> getEligibleGrain () {
      List<UIAssetGrain> eligibleGrain = new ArrayList<UIAssetGrain>();
      UIAssetGrain tempGrain = null;
      int index = 0;
      if (ExecueCoreUtil.isCollectionNotEmpty(mappingIds)) {
         for (Long mappingId : mappingIds) {
            tempGrain = new UIAssetGrain();
            tempGrain.setMappingId(mappingId);
            tempGrain.setConceptDisplayName(displayNames.get(index));
            tempGrain.setGrainType(AssetGrainType.getType(grainTypes.get(index)));
            index++;
            eligibleGrain.add(tempGrain);
         }
      }
      return eligibleGrain;
   }

   public List<Asset> getAssets () {
      return assets;
   }

   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

   public UIAssetGrainInfo getAssetGrainInfo () {
      return assetGrainInfo;
   }

   public void setAssetGrainInfo (UIAssetGrainInfo assetGrainInfo) {
      this.assetGrainInfo = assetGrainInfo;
   }

   public List<Long> getSelectedAssetGrain () {
      return selectedAssetGrain;
   }

   public void setSelectedAssetGrain (List<Long> selectedAssetGrain) {
      this.selectedAssetGrain = selectedAssetGrain;
   }

   public Long getDefaultPopulation () {
      return defaultPopulation;
   }

   public void setDefaultPopulation (Long defaultPopulation) {
      this.defaultPopulation = defaultPopulation;
   }

   public Long getDefaultDistribution () {
      return defaultDistribution;
   }

   public void setDefaultDistribution (Long defaultDistribution) {
      this.defaultDistribution = defaultDistribution;
   }

   public Asset getAsset () {
      return asset;
   }

   public void setAsset (Asset asset) {
      this.asset = asset;
   }

   public List<Long> getMappingIds () {
      return mappingIds;
   }

   public void setMappingIds (List<Long> mappingIds) {
      this.mappingIds = mappingIds;
   }

   public List<String> getGrainTypes () {
      return grainTypes;
   }

   public void setGrainTypes (List<String> grainTypes) {
      this.grainTypes = grainTypes;
   }

   public List<String> getDisplayNames () {
      return displayNames;
   }

   public void setDisplayNames (List<String> displayNames) {
      this.displayNames = displayNames;
   }

   /**
    * @return the status
    */
   public String getStatus () {
      return status;
   }

   /**
    * @param status
    *           the status to set
    */
   public void setStatus (String status) {
      this.status = status;
   }

   public String getRequestedPage () {
      return requestedPage;
   }

   public void setRequestedPage (String requestedPage) {
      this.requestedPage = requestedPage;
   }

   public void setPaginationType (String paginationType) {
      this.paginationType = paginationType;
   }

   public String getPaginationType () {
      return paginationType;
   }

}
