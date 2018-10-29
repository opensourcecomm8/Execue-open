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

import com.execue.core.common.bean.AnswersCatalogManagementQueueIdentifier;

public class AssetDeletionContext extends AnswersCatalogManagementQueueIdentifier {

   private Long assetId;
   private Long parentAssetId;
   private Long userId;

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   /**
    * @return the parentAssetId
    */
   public Long getParentAssetId () {
      return parentAssetId;
   }

   /**
    * @param parentAssetId the parentAssetId to set
    */
   public void setParentAssetId (Long parentAssetId) {
      this.parentAssetId = parentAssetId;
   }

}
