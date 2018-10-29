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


package com.execue.platform.swi;

import com.execue.core.common.type.PublishAssetMode;
import com.execue.swi.exception.SDXException;

/**
 * This service provides the methods required in publishing process of an Asset
 * 
 * @author Kaliki
 */
public interface IAssetPublishService {

   /**
    * Publish the asset provided by following mentioned sequence of steps Steps, 1. Create access control entries 2.
    * Modify the asset PUBLISH MODE
    * 
    * @throws SDXException
    */
   public PublishAssetMode publishAsset (Long applicationId, Long assetId, Long userId, PublishAssetMode publishMode)
            throws SDXException;

}
