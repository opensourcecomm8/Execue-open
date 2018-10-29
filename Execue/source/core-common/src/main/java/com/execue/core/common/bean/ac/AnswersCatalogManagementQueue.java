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


package com.execue.core.common.bean.ac;

import java.io.Serializable;
import java.util.Date;

import com.execue.core.common.type.ACManagementOperationSourceType;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.core.common.type.AnswersCatalogOperationType;

public class AnswersCatalogManagementQueue implements Serializable {

   private Long                            id;
   private Long                            assetId;
   private Long                            parentAssetId;
   private String                          dependentManagementId;
   private AnswersCatalogOperationType     operationType;
   private String                          operationContext;
   private Date                            requestedDate;
   private ACManagementOperationSourceType operationSourceType;
   private ACManagementOperationStatusType operationStatus;
   private String                          remarks;

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

   public Long getParentAssetId () {
      return parentAssetId;
   }

   public void setParentAssetId (Long parentAssetId) {
      this.parentAssetId = parentAssetId;
   }

   public String getDependentManagementId () {
      return dependentManagementId;
   }

   public void setDependentManagementId (String dependentManagementId) {
      this.dependentManagementId = dependentManagementId;
   }

   public AnswersCatalogOperationType getOperationType () {
      return operationType;
   }

   public void setOperationType (AnswersCatalogOperationType operationType) {
      this.operationType = operationType;
   }

   public String getOperationContext () {
      return operationContext;
   }

   public void setOperationContext (String operationContext) {
      this.operationContext = operationContext;
   }

   public Date getRequestedDate () {
      return requestedDate;
   }

   public void setRequestedDate (Date requestedDate) {
      this.requestedDate = requestedDate;
   }

   public ACManagementOperationSourceType getOperationSourceType () {
      return operationSourceType;
   }

   public void setOperationSourceType (ACManagementOperationSourceType operationSourceType) {
      this.operationSourceType = operationSourceType;
   }

   public ACManagementOperationStatusType getOperationStatus () {
      return operationStatus;
   }

   public void setOperationStatus (ACManagementOperationStatusType operationStatus) {
      this.operationStatus = operationStatus;
   }

   public String getRemarks () {
      return remarks;
   }

   public void setRemarks (String remarks) {
      this.remarks = remarks;
   }

}
