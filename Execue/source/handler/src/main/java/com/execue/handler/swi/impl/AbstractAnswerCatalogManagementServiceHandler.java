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


package com.execue.handler.swi.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.execue.ac.service.IAnswersCatalogContextBuilderService;
import com.execue.ac.service.IAnswersCatalogDefaultsService;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetCreationInfo;
import com.execue.core.common.bean.entity.AssetExtendedDetail;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExeCueXMLUtils;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIAsset;
import com.execue.qdata.service.IAnswersCatalogManagementQueueService;
import com.execue.qdata.service.IJobDataService;
import com.execue.security.UserContextService;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IAssetDetailService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * 
 * @author jitendra
 *
 */
public abstract class AbstractAnswerCatalogManagementServiceHandler extends UserContextService {

   private IJobDataService                       jobDataService;
   private IKDXRetrievalService                  kdxRetrievalService;
   private ISDXRetrievalService                  sdxRetrievalService;
   private IMappingRetrievalService              mappingRetrievalService;
   private IAnswersCatalogContextBuilderService  answersCatalogContextBuilderService;
   private ICoreConfigurationService             coreConfigurationService;
   private IAnswersCatalogDefaultsService        answersCatalogDefaultsService;
   private IAnswersCatalogManagementQueueService answersCatalogManagementQueueService;
   private IAssetDetailService                   assetDetailService;

   //methods

   protected List<UIAsset> transformUIAssets (List<Asset> assets) throws SDXException {
      List<UIAsset> uiAssets = new ArrayList<UIAsset>();
      if (ExecueCoreUtil.isCollectionNotEmpty(assets)) {
         for (Asset asset : assets) {
            UIAsset uiAsset = new UIAsset();
            uiAsset.setId(asset.getId());
            uiAsset.setName(asset.getDisplayName());
            uiAsset.setDescription(asset.getDescription());
            
            Asset baseAsset = getSdxRetrievalService().getAssetById(asset.getBaseAssetId());
            uiAsset.setParentAssetId(baseAsset.getId());
            uiAsset.setParentAssetName(baseAsset.getDisplayName());
            
            AssetExtendedDetail assetExtendedDetail = getAssetDetailService().getAssetExtendedDetailByAssetId(asset.getId());
            String assetCreationInfoString = assetExtendedDetail.getCreationInfo();
            if (!StringUtils.isBlank(assetCreationInfoString)) {
               AssetCreationInfo assetCreationInfo = (AssetCreationInfo) ExeCueXMLUtils.getObjectFromXMLString(assetCreationInfoString);
               if (assetCreationInfo.getLastModifiedTime() != null) {
                  uiAsset.setLastModifiedDate(ExecueCoreUtil.getDateAsString(assetCreationInfo.getLastModifiedTime()));
               }
            }
            
            uiAssets.add(uiAsset);
         }
      }
      return uiAssets;
   }

   protected List<Long> prepareAssetIds (Long existingAssetId, Long parentAssetId) {
      List<Long> assetIds = new ArrayList<Long>();
      assetIds.add(parentAssetId);
      assetIds.add(existingAssetId);
      return assetIds;
   }

   /**
    * @return the jobDataService
    */
   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   /**
    * @param jobDataService the jobDataService to set
    */
   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the sdxRetrievalService
    */
   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   /**
    * @param sdxRetrievalService the sdxRetrievalService to set
    */
   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   /**
    * @return the mappingRetrievalService
    */
   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   /**
    * @param mappingRetrievalService the mappingRetrievalService to set
    */
   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   /**
    * @return the answersCatalogContextBuilderService
    */
   public IAnswersCatalogContextBuilderService getAnswersCatalogContextBuilderService () {
      return answersCatalogContextBuilderService;
   }

   /**
    * @param answersCatalogContextBuilderService the answersCatalogContextBuilderService to set
    */
   public void setAnswersCatalogContextBuilderService (
            IAnswersCatalogContextBuilderService answersCatalogContextBuilderService) {
      this.answersCatalogContextBuilderService = answersCatalogContextBuilderService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   /**
    * @return the answersCatalogDefaultsService
    */
   public IAnswersCatalogDefaultsService getAnswersCatalogDefaultsService () {
      return answersCatalogDefaultsService;
   }

   /**
    * @param answersCatalogDefaultsService the answersCatalogDefaultsService to set
    */
   public void setAnswersCatalogDefaultsService (IAnswersCatalogDefaultsService answersCatalogDefaultsService) {
      this.answersCatalogDefaultsService = answersCatalogDefaultsService;
   }

   /**
    * @return the answersCatalogManagementQueueService
    */
   public IAnswersCatalogManagementQueueService getAnswersCatalogManagementQueueService () {
      return answersCatalogManagementQueueService;
   }

   /**
    * @param answersCatalogManagementQueueService the answersCatalogManagementQueueService to set
    */
   public void setAnswersCatalogManagementQueueService (
            IAnswersCatalogManagementQueueService answersCatalogManagementQueueService) {
      this.answersCatalogManagementQueueService = answersCatalogManagementQueueService;
   }

   
   public IAssetDetailService getAssetDetailService () {
      return assetDetailService;
   }

   
   public void setAssetDetailService (IAssetDetailService assetDetailService) {
      this.assetDetailService = assetDetailService;
   }

}
