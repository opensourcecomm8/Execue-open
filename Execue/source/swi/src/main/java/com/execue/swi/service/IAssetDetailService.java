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


package com.execue.swi.service;

import com.execue.core.common.bean.entity.AssetDetail;
import com.execue.core.common.bean.entity.AssetExtendedDetail;
import com.execue.swi.exception.SDXException;

public interface IAssetDetailService extends ISWIService {

   public AssetDetail getAssetDetailById (Long assetDetailId) throws SDXException;

   public AssetExtendedDetail getAssetExtendedDetailInfo (Long assetDetailId) throws SDXException;

   public AssetExtendedDetail getAssetExtendedDetailById (Long assetExtendedDetailId) throws SDXException;

   public AssetDetail getAssetDetailInfo (Long assetId) throws SDXException;

   public AssetExtendedDetail getAssetExtendedDetailByAssetId (Long assetId) throws SDXException;

   public void createUpdateAssetDetail (AssetDetail assetDetail) throws SDXException;

   public void deleteAssetDetail (Long assetId) throws SDXException;

}
