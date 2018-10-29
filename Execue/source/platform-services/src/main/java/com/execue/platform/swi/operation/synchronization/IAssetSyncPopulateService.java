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


package com.execue.platform.swi.operation.synchronization;

import java.util.List;

import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.swi.AssetSyncInfo;
import com.execue.core.common.bean.swi.AssetSynchronizationContext;
import com.execue.platform.exception.AssetSynchronizationException;

/**
 * This service populates the asset sync object by finding the differences with source
 * 
 * @author Vishay
 * @version 1.0
 * @since 20/08/09
 */

public interface IAssetSyncPopulateService {

   // This method will populate the asset sync object by comparing it with source. if we don't find any changes return
   // false, record has to go in the database with status as no change found .For each asset only the latest entry
   // should be there, so before insertion, move the old record to history if it exists
   public AssetSyncInfo populateAssetSyncInfo (AssetSynchronizationContext assetSynchronizationContext) throws AssetSynchronizationException;

   public List<Membr> getAddedMembers (Long parentAssetId, Long assetId, Long modelId, Long conceptId)
            throws AssetSynchronizationException;

   public List<Membr> getDeletedMembers (Long parentAssetId, Long assetId, Long modelId, Long conceptId)
            throws AssetSynchronizationException;

}
