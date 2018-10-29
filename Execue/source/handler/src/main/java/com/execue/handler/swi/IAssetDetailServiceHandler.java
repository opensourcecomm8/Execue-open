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


package com.execue.handler.swi;

import com.execue.core.common.bean.entity.AssetDetail;
import com.execue.core.common.bean.entity.AssetExtendedDetail;
import com.execue.core.exception.HandlerException;
import com.execue.handler.IHandler;
import com.execue.handler.bean.UIAssetCreationInfo;
import com.execue.handler.bean.UIAssetDetail;

public interface IAssetDetailServiceHandler extends IHandler {

   public AssetDetail getAssetDetailInfo (Long assetId) throws HandlerException;

   public AssetExtendedDetail getAssetExtendedDetailInfo (Long assetDetailId) throws HandlerException;

   public void createUpdateAssetDetail (Long assetId) throws HandlerException;

   public void createUpdateAssetDetail (Long assetId, UIAssetDetail uiAssetDetail) throws HandlerException;

   public void deleteAssetDetail (Long assetId) throws HandlerException;

   public AssetExtendedDetail getAssetExtendedDetailByAssetId (Long assetId) throws HandlerException;

   public void createUpdateAssetDetailByApplication (UIAssetDetail uiAssetDetail, boolean isDisclaimerSave)
            throws HandlerException;

   public UIAssetCreationInfo getAssetCreationInfo (Long assetId) throws HandlerException;

}
