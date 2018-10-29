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
import com.execue.core.common.type.PublishAssetMode;

/**
 * Context to contain the information required to Publish the hierarchy of an Application
 * 
 * @author Raju Gottumukkala
 *
 */
public class PublishContext extends JobRequestIdentifier {

   private Long             modelId;
   private Long             applicationId;
   private Long             userId;
   private PublishAssetMode publishMode;

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

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }

   public PublishAssetMode getPublishMode () {
      return publishMode;
   }

   public void setPublishMode (PublishAssetMode publishMode) {
      this.publishMode = publishMode;
   }

}
