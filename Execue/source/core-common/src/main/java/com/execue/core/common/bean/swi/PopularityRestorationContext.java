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

public class PopularityRestorationContext extends JobRequestIdentifier {

   private Long applicationId;

   private Long modelId;

   private Long userId;

   /**
    * @return the appllicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param appllicationId
    *           the appllicationId to set
    */
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

   
   public Long getModelId () {
      return modelId;
   }

   
   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

}
