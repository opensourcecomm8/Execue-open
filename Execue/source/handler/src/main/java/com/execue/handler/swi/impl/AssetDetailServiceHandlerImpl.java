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

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetCreationInfo;
import com.execue.core.common.bean.entity.AssetDetail;
import com.execue.core.common.bean.entity.AssetExtendedDetail;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.type.StatType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExeCueXMLUtils;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.handler.bean.UIAssetCreationInfo;
import com.execue.handler.bean.UIAssetDetail;
import com.execue.handler.swi.IAssetDetailServiceHandler;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IAssetDetailService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

public class AssetDetailServiceHandlerImpl implements IAssetDetailServiceHandler {

   private static final Logger  logger = Logger.getLogger(AssetDetailServiceHandlerImpl.class);

   private IAssetDetailService  assetDetailService;
   private ISDXRetrievalService sdxRetrievalService;
   private IKDXRetrievalService kdxRetrievalService;

   public AssetDetail getAssetDetailInfo (Long assetId) throws HandlerException {
      AssetDetail assetDetail = null;
      try {
         assetDetail = getAssetDetailService().getAssetDetailInfo(assetId);
      } catch (SDXException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return assetDetail;
   }

   public AssetExtendedDetail getAssetExtendedDetailInfo (Long assetDetailId) throws HandlerException {
      AssetExtendedDetail assetExtendedDetail = null;
      try {
         assetExtendedDetail = getAssetDetailService().getAssetExtendedDetailInfo(assetDetailId);
      } catch (SDXException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
      return assetExtendedDetail;
   }

   public void createUpdateAssetDetail (Long assetId) throws HandlerException {
      AssetDetail assetDetail = null;
      try {
         assetDetail = getAssetDetailService().getAssetDetailInfo(assetId);
         if (assetDetail != null) {
            getAssetDetailService().createUpdateAssetDetail(assetDetail);
         } else {
            assetDetail = new AssetDetail();
            assetDetail.setAssetId(assetId);
            getAssetDetailService().createUpdateAssetDetail(assetDetail);
         }
      } catch (SDXException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void createUpdateAssetDetail (Long assetId, UIAssetDetail uiAssetDetail) throws HandlerException {
      try {
         AssetDetail assetDetail = getAssetDetailService().getAssetDetailById(uiAssetDetail.getAssetDetailId());
         AssetExtendedDetail assetExtendedDetail = getAssetDetailService().getAssetExtendedDetailById(
                  uiAssetDetail.getExtendedAssetDetailId());
         saveAssetDetails(assetDetail, assetExtendedDetail, uiAssetDetail);
      } catch (SDXException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void deleteAssetDetail (Long assetId) throws HandlerException {
      try {
         getAssetDetailService().deleteAssetDetail(assetId);
      } catch (SDXException e) {
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }

   }

   public AssetExtendedDetail getAssetExtendedDetailByAssetId (Long assetId) throws HandlerException {
      try {
         return getAssetDetailService().getAssetExtendedDetailByAssetId(assetId);
      } catch (SDXException sdxException) {
         throw new HandlerException(sdxException.getCode(), sdxException);
      }
   }

   public void createUpdateAssetDetailByApplication (UIAssetDetail uiAssetDetail, boolean isDisclaimerSave)
            throws HandlerException {
      try {
         List<Asset> assets = getSdxRetrievalService().getAssetsByApplicationId(uiAssetDetail.getApplicationId());
         if (ExecueCoreUtil.isCollectionNotEmpty(assets)) {
            for (Asset asset : assets) {
               AssetDetail assetDetail = getAssetDetailInfo(asset.getId());
               AssetExtendedDetail assetExtendedDetail = getAssetExtendedDetailInfo(assetDetail.getId());
               if (isDisclaimerSave) {
                  uiAssetDetail.setExtendedNote(assetExtendedDetail.getExtendedNote());
               } else {
                  uiAssetDetail.setExtendedDisclaimer(assetExtendedDetail.getExtendedDisclaimer());
               }
               saveAssetDetails(assetDetail, assetExtendedDetail, uiAssetDetail);

            }
         }
      } catch (SDXException sdxException) {
         throw new HandlerException(sdxException.getCode(), sdxException);
      }

   }

   @Override
   public UIAssetCreationInfo getAssetCreationInfo (Long assetId) throws HandlerException {
      UIAssetCreationInfo uiAssetCreationInfo = new UIAssetCreationInfo();
      try {
         AssetExtendedDetail assetDetail = getAssetDetailService().getAssetExtendedDetailByAssetId(assetId);
         //  uiAssetCreationInfo = pupulateDummyUIAssetCreationInfo(uiAssetCreationInfo);
         if (assetDetail != null && assetDetail.getCreationInfo() != null) {

            AssetCreationInfo assetCreationInfo = (AssetCreationInfo) ExeCueXMLUtils.getObjectFromXMLString(assetDetail
                     .getCreationInfo());
            if (assetCreationInfo != null) {
               uiAssetCreationInfo.setDimensions(getConceptDisplayNames(assetCreationInfo.getDimensionBEDIDs()));
               uiAssetCreationInfo.setMeasures(getConceptDisplayNames(assetCreationInfo.getMeasureBEDIDs()));
               if (ExecueCoreUtil.isCollectionNotEmpty(assetCreationInfo.getStats())) {
                  List<String> stats = new ArrayList<String>();
                  for (StatType statType : assetCreationInfo.getStats()) {
                     stats.add(statType.getDescription());
                  }
                  uiAssetCreationInfo.setStats(stats);
               }
               if (assetCreationInfo.getLastModifiedTime() != null) {
                  uiAssetCreationInfo.setLastModifiedDate(ExecueCoreUtil.getDateAsString(assetCreationInfo
                           .getLastModifiedTime()));
               }
               uiAssetCreationInfo.setConfidenceLevel(assetCreationInfo.getConfidenceLevel());
               uiAssetCreationInfo.setSample(assetCreationInfo.getSample());
               uiAssetCreationInfo.setErrorRate(assetCreationInfo.getErrorRate());
               uiAssetCreationInfo.setDataAvailable(true);//just to check in UI whether data is available or not
            }
         }
      } catch (KDXException e) {
         logger.error(logger);
         new HandlerException(e.getCode(), e);
      } catch (SDXException e) {
         logger.error(logger);
         new HandlerException(e.getCode(), e);
      }

      return uiAssetCreationInfo;
   }

   private UIAssetCreationInfo pupulateDummyUIAssetCreationInfo (UIAssetCreationInfo uiAssetCreationInfo) {
      List<String> measeures = new ArrayList<String>();
      measeures.add("Fico score");
      measeures.add("Behaviour score");
      uiAssetCreationInfo.setMeasures(measeures);

      List<String> dimensions = new ArrayList<String>();
      dimensions.add("Acctiont Status");
      dimensions.add("Operation Status");

      List<String> stats = new ArrayList<String>();
      stats.add(StatType.AVERAGE.getValue());
      uiAssetCreationInfo.setStats(stats);
      uiAssetCreationInfo.setDimensions(dimensions);

      uiAssetCreationInfo.setSample(10.0);
      uiAssetCreationInfo.setErrorRate(3.0);
      // uiAssetCreationInfo.setConfidenceLevel(95.0);
      uiAssetCreationInfo.setDataAvailable(true);
      return uiAssetCreationInfo;

   }

   private List<String> getConceptDisplayNames (List<Long> conceptBEDIds) throws KDXException {
      //TODO:-JT- need to check if we need to send modelId along with bedids.
      List<Concept> concepts = getKdxRetrievalService().getConceptByBEDIds(conceptBEDIds);
      List<String> conceptNameList = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionNotEmpty(concepts)) {
         for (Concept concept : concepts) {
            conceptNameList.add(concept.getName());
         }
      }
      return conceptNameList;
   }

   private void saveAssetDetails (AssetDetail assetDetail, AssetExtendedDetail assetExtendedDetail,
            UIAssetDetail uiAssetDetail) throws SDXException {
      // TODO : -JT- need read number of character from configuration
      if (ExecueCoreUtil.isNotEmpty(uiAssetDetail.getExtendedDisclaimer())) {
         assetDetail
                  .setShortDisclaimer(ExecueStringUtil.getTruncatedString(uiAssetDetail.getExtendedDisclaimer(), 100));
      }
      // if we user does not enter any short not but entered extended note, then we will get 100 character out
      // of
      // extended note and create short not
      if (ExecueCoreUtil.isEmpty(uiAssetDetail.getShortNote())
               && ExecueCoreUtil.isNotEmpty(uiAssetDetail.getExtendedNote())) {
         assetDetail.setShortNote(ExecueStringUtil.getTruncatedString(uiAssetDetail.getExtendedNote(), 100));
      } else {
         assetDetail.setShortNote(uiAssetDetail.getShortNote());
      }
      assetExtendedDetail.setAssetDetail(assetDetail);
      assetExtendedDetail.setExtendedDisclaimer(uiAssetDetail.getExtendedDisclaimer());
      assetExtendedDetail.setExtendedNote(uiAssetDetail.getExtendedNote());
      assetDetail.setAssetExtendedDetail(assetExtendedDetail);
      getAssetDetailService().createUpdateAssetDetail(assetDetail);

   }

   public IAssetDetailService getAssetDetailService () {
      return assetDetailService;
   }

   public void setAssetDetailService (IAssetDetailService assetDetailService) {
      this.assetDetailService = assetDetailService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
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

}
