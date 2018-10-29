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


package com.execue.swi.service.impl;

import com.execue.core.common.bean.entity.AssetDetail;
import com.execue.core.common.bean.entity.AssetExtendedDetail;
import com.execue.swi.dataaccess.IAssetDetailDataAccessManager;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.IAssetDetailService;

public class AssetDetailServiceImpl implements IAssetDetailService {

   private IAssetDetailDataAccessManager assetDetailDataAccessManager;

   public AssetDetail getAssetDetailById (Long assetDetailId) throws SDXException {
      return getAssetDetailDataAccessManager().getById(assetDetailId, AssetDetail.class);
   }

   public AssetExtendedDetail getAssetExtendedDetailById (Long assetExtendedDetailId) throws SDXException {
      return getAssetDetailDataAccessManager().getById(assetExtendedDetailId, AssetExtendedDetail.class);
   }

   public AssetDetail getAssetDetailInfo (Long assetId) throws SDXException {
      return getAssetDetailDataAccessManager().getAssetDetailInfo(assetId);
   }

   public AssetExtendedDetail getAssetExtendedDetailInfo (Long assetDetailId) throws SDXException {
      return getAssetDetailDataAccessManager().getAssetExtendedDetailInfo(assetDetailId);
   }

   public void createUpdateAssetDetail (AssetDetail assetDetail) throws SDXException {
      AssetExtendedDetail assetExtendedDetail = null;
      if (assetDetail.getId() == null) {
         getAssetDetailDataAccessManager().createAssetDetail(assetDetail);
         assetExtendedDetail = new AssetExtendedDetail();
         assetExtendedDetail.setAssetDetail(assetDetail);
         getAssetDetailDataAccessManager().createAssetExtendedDetail(assetExtendedDetail);
      } else {
         assetExtendedDetail = assetDetail.getAssetExtendedDetail();
         getAssetDetailDataAccessManager().updateAssetExtendedDetail(assetExtendedDetail);
         getAssetDetailDataAccessManager().updateAssetDetail(assetDetail);
      }

   }

   public void deleteAssetDetail (Long assetId) throws SDXException {
      getAssetDetailDataAccessManager().deleteAssetDetail(assetId);

   }

   public AssetExtendedDetail getAssetExtendedDetailByAssetId (Long assetId) throws SDXException {
      return getAssetDetailDataAccessManager().getAssetExtendedDetailByAssetId(assetId);
   }

   public IAssetDetailDataAccessManager getAssetDetailDataAccessManager () {
      return assetDetailDataAccessManager;
   }

   public void setAssetDetailDataAccessManager (IAssetDetailDataAccessManager assetDetailDataAccessManager) {
      this.assetDetailDataAccessManager = assetDetailDataAccessManager;
   }

}
