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

import java.util.List;

import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;

/**
 * This service provides the methods required in activation process of an Asset
 * 
 * @author execue
 */
public interface IAssetActivationService {

   /**
    * Activates the asset provided by following mentioned sequence of steps Steps, 1. Correct the mappings on the asset
    * 2. Create access control entries 3. Modify the asset status to Active
    * 
    * @throws SWIException
    */
   public void activateAsset (Long userId, Long assetId) throws SDXException;

   /**
    * Wrapper over activateAsset for 'n' number of assets
    * 
    * @param assetIds
    * @throws SWIException
    */
   public void activateAssets (Long userId, List<Long> assetIds) throws SDXException;

   public void inactivateAsset (Long userId, Long assetId) throws SDXException;

}
