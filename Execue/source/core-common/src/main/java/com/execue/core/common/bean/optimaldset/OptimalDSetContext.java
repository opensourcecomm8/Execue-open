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


package com.execue.core.common.bean.optimaldset;

import com.execue.core.common.bean.JobRequestIdentifier;
import com.execue.core.common.type.OperationRequestLevel;

public class OptimalDSetContext extends JobRequestIdentifier {

   private Long                  id;                   // application id, asset id, or null(in case of system level )
   private Long                  modelId;              // This should come in case of asset level operation request
   private Long                  userId;               // for on demand job
   private OperationRequestLevel operationRequestLevel;

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the modelId
    */
   public Long getModelId () {
      return modelId;
   }

   /**
    * @param modelId the modelId to set
    */
   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   /**
    * @return the operationRequestLevel
    */
   public OperationRequestLevel getOperationRequestLevel () {
      return operationRequestLevel;
   }

   /**
    * @param operationRequestLevel the operationRequestLevel to set
    */
   public void setOperationRequestLevel (OperationRequestLevel operationRequestLevel) {
      this.operationRequestLevel = operationRequestLevel;
   }

   /**
    * @return the userId
    */
   public Long getUserId () {
      return userId;
   }

   /**
    * @param userId the userId to set
    */
   public void setUserId (Long userId) {
      this.userId = userId;
   }

}