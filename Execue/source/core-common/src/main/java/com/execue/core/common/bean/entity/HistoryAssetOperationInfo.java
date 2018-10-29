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


package com.execue.core.common.bean.entity;

import java.io.Serializable;
import java.util.Date;

import com.execue.core.common.type.AssetOperationType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.OperationEnum;

/**
 * This entity represents the schema table h_asset_operation_info
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/06/10
 */
public class HistoryAssetOperationInfo implements Serializable {

   private static final long  serialVersionUID = 1L;
   private Long               id;
   private Long               assetId;
   private Date               startDate;
   private Date               completionDate;
   private String             assetOperationData;
   private CheckType          changeFound;
   private CheckType          status;
   private OperationEnum      operation;
   private AssetOperationType assetOperationType;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public Date getStartDate () {
      return startDate;
   }

   public void setStartDate (Date startDate) {
      this.startDate = startDate;
   }

   public CheckType getChangeFound () {
      return changeFound;
   }

   public void setChangeFound (CheckType changeFound) {
      this.changeFound = changeFound;
   }

   public CheckType getStatus () {
      return status;
   }

   public void setStatus (CheckType status) {
      this.status = status;
   }

   public OperationEnum getOperation () {
      return operation;
   }

   public void setOperation (OperationEnum operation) {
      this.operation = operation;
   }

   public Date getCompletionDate () {
      return completionDate;
   }

   public void setCompletionDate (Date completionDate) {
      this.completionDate = completionDate;
   }

   public String getAssetOperationData () {
      return assetOperationData;
   }

   public void setAssetOperationData (String assetOperationData) {
      this.assetOperationData = assetOperationData;
   }

   public AssetOperationType getAssetOperationType () {
      return assetOperationType;
   }

   public void setAssetOperationType (AssetOperationType assetOperationType) {
      this.assetOperationType = assetOperationType;
   }

}
