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


package com.execue.platform.helper;

import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.ac.AnswersCatalogMaintenanceRequestInfo;
import com.execue.core.common.bean.ac.AnswersCatalogManagementQueue;
import com.execue.core.common.bean.ac.AssetOperationDetail;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.ACManagementOperationSourceType;
import com.execue.core.common.type.ACManagementOperationStatusType;
import com.execue.core.common.type.AnswersCatalogOperationType;
import com.execue.core.util.ExecueCoreUtil;

public class AnswersCatalogPlatformServiceHelper {

   /**
    * @param parentAssetId
    * @param assetId
    * @param dependentManagementId
    * @param operationContext
    * @param remarks
    * @param operationSourceType
    * @param operationStatusType
    * @param operationType
    * @return the AnswersCatalogManagementQueue
    */
   public static AnswersCatalogManagementQueue prepareAnswersCatalogManagementQueue (Long parentAssetId, Long assetId,
            String dependentManagementId, String operationContext, String remarks,
            ACManagementOperationSourceType operationSourceType, ACManagementOperationStatusType operationStatusType,
            AnswersCatalogOperationType operationType) {
      AnswersCatalogManagementQueue answersCatalogManagementQueue = new AnswersCatalogManagementQueue();
      answersCatalogManagementQueue.setAssetId(assetId);
      answersCatalogManagementQueue.setDependentManagementId(dependentManagementId);
      answersCatalogManagementQueue.setOperationContext(operationContext);
      answersCatalogManagementQueue.setOperationSourceType(operationSourceType);
      answersCatalogManagementQueue.setOperationStatus(operationStatusType);
      answersCatalogManagementQueue.setOperationType(operationType);
      answersCatalogManagementQueue.setParentAssetId(parentAssetId);
      answersCatalogManagementQueue.setRemarks(remarks);
      answersCatalogManagementQueue.setRequestedDate(new Date());
      return answersCatalogManagementQueue;
   }

   /**
    * @param applicationId
    * @param modelId
    * @param parentAssetId
    * @param userId
    * @param existingAssetId
    * @param selectedRangeLookups
    * @param selectedSimpleLookups
    * @param prominentDimensions
    * @param prominentMeasures
    * @param targetAsset
    * @return the AnswersCatalogMaintenanceRequestInfo
    */
   public static AnswersCatalogMaintenanceRequestInfo prepareAnswersCatalogMaintenanceRequestInfo (Long applicationId,
            Long modelId, Long parentAssetId, User user, Long existingAssetId, List<Range> selectedRangeLookups,
            List<String> selectedSimpleLookups, List<String> prominentDimensions, List<String> prominentMeasures,
            Asset targetAsset) {
      adjustUserOnRanges(selectedRangeLookups, user);
      AnswersCatalogMaintenanceRequestInfo answersCatalogMaintenanceRequestInfo = new AnswersCatalogMaintenanceRequestInfo();
      answersCatalogMaintenanceRequestInfo.setApplicationId(applicationId);
      answersCatalogMaintenanceRequestInfo.setModelId(modelId);
      answersCatalogMaintenanceRequestInfo.setParentAssetId(parentAssetId);
      answersCatalogMaintenanceRequestInfo.setUserId(user.getId());
      answersCatalogMaintenanceRequestInfo.setExistingAssetId(existingAssetId);
      answersCatalogMaintenanceRequestInfo.setSelectedRangeLookups(selectedRangeLookups);
      answersCatalogMaintenanceRequestInfo.setSelectedSimpleLookups(selectedSimpleLookups);
      answersCatalogMaintenanceRequestInfo.setProminentDimensions(prominentDimensions);
      answersCatalogMaintenanceRequestInfo.setProminentMeasures(prominentMeasures);
      answersCatalogMaintenanceRequestInfo.setTargetAsset(targetAsset);
      return answersCatalogMaintenanceRequestInfo;
   }
   
   private static void adjustUserOnRanges(List<Range> selectedRangeLookups, User user) {
      if (ExecueCoreUtil.isCollectionEmpty(selectedRangeLookups)) {
         return;
      }
      for (Range range : selectedRangeLookups) {
         if (range.getUser() == null) {
            range.setUser(user);
         } else if (range.getUser().getId() >= 1) {
            continue;
         } else {
            range.setUser(user);
         }
      }
   }

   /** 
    * @param assetId
    *  @param parentAssetId
    * @param jobRequestId
    * @param assetOperationType
    * @param assetOperationStatus
    * @return AssetOperationDetail
    */
   public static AssetOperationDetail prepareAssetOperationDetail (Long assetId, Long parentAssetId, Long jobRequestId,
            AnswersCatalogOperationType assetOperationType, ACManagementOperationStatusType assetOperationStatus) {
      AssetOperationDetail assetOperationDetail = new AssetOperationDetail();
      assetOperationDetail.setAssetId(assetId);
      assetOperationDetail.setParentAssetId(parentAssetId);
      assetOperationDetail.setJobRequestId(jobRequestId);
      assetOperationDetail.setAssetOperationType(assetOperationType);
      assetOperationDetail.setAssetOperationStatus(assetOperationStatus);
      return assetOperationDetail;

   }

}
