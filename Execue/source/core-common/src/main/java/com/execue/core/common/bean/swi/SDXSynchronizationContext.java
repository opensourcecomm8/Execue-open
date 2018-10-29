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

import com.execue.core.common.bean.JobRequestIdentifier;
import com.execue.core.common.type.OperationRequestLevel;
import com.execue.core.common.type.SyncRequestLevel;

public class SDXSynchronizationContext extends JobRequestIdentifier {

   private Long                  applicationId;
   private Long                  modelId;
   private Long                  id;
   private OperationRequestLevel requestLevel;
   private SyncRequestLevel      syncRequestLevel;
   private Long                  userId;

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   /**
    * @return the userId
    */
   public Long getUserId () {
      return userId;
   }

   /**
    * @param userId
    *           the userId to set
    */
   public void setUserId (Long userId) {
      this.userId = userId;
   }

   public OperationRequestLevel getRequestLevel () {
      return requestLevel;
   }

   public void setRequestLevel (OperationRequestLevel requestLevel) {
      this.requestLevel = requestLevel;
   }

   public SyncRequestLevel getSyncRequestLevel () {
      return syncRequestLevel;
   }

   public void setSyncRequestLevel (SyncRequestLevel syncRequestLevel) {
      this.syncRequestLevel = syncRequestLevel;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

}
