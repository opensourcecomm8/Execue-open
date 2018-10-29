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


package com.execue.core.common.bean.swi;

import java.util.List;

import com.execue.core.common.type.AssetAnalysisOperationType;
import com.execue.core.common.type.PublishAssetMode;

public class AssetAnalysisReport {

   private List<AssetAnalysisReportInfo> assetAnalysisReportInfoList;

   private OperationAsset                operationAsset;

   private PublishAssetMode              selectedPublishAssetMode;

   private AssetAnalysisOperationType    selectedOperationalType;

   public List<AssetAnalysisReportInfo> getAssetAnalysisReportInfoList () {
      return assetAnalysisReportInfoList;
   }

   public void setAssetAnalysisReportInfoList (List<AssetAnalysisReportInfo> assetAnalysisReportInfoList) {
      this.assetAnalysisReportInfoList = assetAnalysisReportInfoList;
   }

   public OperationAsset getOperationAsset () {
      return operationAsset;
   }

   public void setOperationAsset (OperationAsset operationAsset) {
      this.operationAsset = operationAsset;
   }

   public PublishAssetMode getSelectedPublishAssetMode () {
      return selectedPublishAssetMode;
   }

   public void setSelectedPublishAssetMode (PublishAssetMode selectedPublishAssetMode) {
      this.selectedPublishAssetMode = selectedPublishAssetMode;
   }

   public AssetAnalysisOperationType getSelectedOperationalType () {
      return selectedOperationalType;
   }

   public void setSelectedOperationalType (AssetAnalysisOperationType selectedOperationalType) {
      this.selectedOperationalType = selectedOperationalType;
   }

}
