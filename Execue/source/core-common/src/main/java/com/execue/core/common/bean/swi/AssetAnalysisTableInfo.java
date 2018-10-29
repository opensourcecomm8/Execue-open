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

import java.io.Serializable;
import java.util.List;

import com.execue.core.common.type.AssetAnalysisThresholdType;

public class AssetAnalysisTableInfo implements Serializable {

   private AssetOperationTable        operationTable;
   private AssetAnalysisThresholdType assetAnalysisThresholdType;
   private String                     thresholdMessage;
   private List<AssetOperationColumn> operationColumns;
   private List<AssetOperationMember> operationMembers;

   public AssetAnalysisThresholdType getAssetAnalysisThresholdType () {
      return assetAnalysisThresholdType;
   }

   public void setAssetAnalysisThresholdType (AssetAnalysisThresholdType assetAnalysisThresholdType) {
      this.assetAnalysisThresholdType = assetAnalysisThresholdType;
   }

   public String getThresholdMessage () {
      return thresholdMessage;
   }

   public void setThresholdMessage (String thresholdMessage) {
      this.thresholdMessage = thresholdMessage;
   }

   public AssetOperationTable getOperationTable () {
      return operationTable;
   }

   public void setOperationTable (AssetOperationTable operationTable) {
      this.operationTable = operationTable;
   }

   public List<AssetOperationColumn> getOperationColumns () {
      return operationColumns;
   }

   public void setOperationColumns (List<AssetOperationColumn> operationColumns) {
      this.operationColumns = operationColumns;
   }

   public List<AssetOperationMember> getOperationMembers () {
      return operationMembers;
   }

   public void setOperationMembers (List<AssetOperationMember> operationMembers) {
      this.operationMembers = operationMembers;
   }

}
