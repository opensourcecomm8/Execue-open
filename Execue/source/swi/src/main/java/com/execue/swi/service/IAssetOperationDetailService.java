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

import java.util.List;

import com.execue.core.common.bean.ac.AssetOperationDetail;
import com.execue.swi.exception.SDXException;

public interface IAssetOperationDetailService extends ISWIService {

   public void createAssetOperationDetail (AssetOperationDetail assetOperationDetail) throws SDXException;

   public void updateAssetOperationDetail (AssetOperationDetail assetOperationDetail) throws SDXException;

   public void deleteAssetOperationDetail (AssetOperationDetail assetOperationDetail) throws SDXException;

   public List<AssetOperationDetail> getAssetOperationDetailByAssetId (Long assetId) throws SDXException;

   public boolean isAssetUnderMaintenance (Long assetId) throws SDXException;

   public List<AssetOperationDetail> getUnderProcessAssetOperationDetailsByParentAssetId (Long parentAssetId)
            throws SDXException;

}
