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


package com.execue.core.common.bean.governor;

import java.util.List;
import java.util.Map;

/**
 * This class represents the information extracted from mapping which is required at population of structured queries.
 * 
 * @author Vishay
 * @version 1.0
 * @since 09/12/09
 */
public class EntityMappingInfo {

   private BusinessEntityTerm                              businessEntityTerm;
   private Map<Long, List<LightAssetEntityDefinitionInfo>> assetBasedLightAssetEntityDefinitions;
   private Map<Long, AssetEntityTerm>                      assetBasedAssetEntityTerm;

   public Map<Long, List<LightAssetEntityDefinitionInfo>> getAssetBasedLightAssetEntityDefinitions () {
      return assetBasedLightAssetEntityDefinitions;
   }

   public void setAssetBasedLightAssetEntityDefinitions (
            Map<Long, List<LightAssetEntityDefinitionInfo>> assetBasedLightAssetEntityDefinitions) {
      this.assetBasedLightAssetEntityDefinitions = assetBasedLightAssetEntityDefinitions;
   }

   public Map<Long, AssetEntityTerm> getAssetBasedAssetEntityTerm () {
      return assetBasedAssetEntityTerm;
   }

   public void setAssetBasedAssetEntityTerm (Map<Long, AssetEntityTerm> assetBasedAssetEntityTerm) {
      this.assetBasedAssetEntityTerm = assetBasedAssetEntityTerm;
   }

   
   public BusinessEntityTerm getBusinessEntityTerm () {
      return businessEntityTerm;
   }

   
   public void setBusinessEntityTerm (BusinessEntityTerm businessEntityTerm) {
      this.businessEntityTerm = businessEntityTerm;
   }

}
