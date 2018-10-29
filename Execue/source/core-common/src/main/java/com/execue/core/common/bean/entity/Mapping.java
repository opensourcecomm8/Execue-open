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


package com.execue.core.common.bean.entity;

import com.execue.core.common.type.AssetGrainType;
import com.execue.core.common.type.PrimaryMappingType;

/**
 * This class represents the mapping between assetentity and businessentity objects.
 * 
 * @author Vishay
 * @version 1.0
 * @since 12/01/09
 */
public class Mapping implements java.io.Serializable {

   private static final long        serialVersionUID = 1L;
   private Long                     id;
   private AssetEntityDefinition    assetEntityDefinition;
   private BusinessEntityDefinition businessEntityDefinition;
   private AssetGrainType           assetGrainType;
   private PrimaryMappingType       primary          = PrimaryMappingType.NON_PRIMARY;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public AssetEntityDefinition getAssetEntityDefinition () {
      return assetEntityDefinition;
   }

   public void setAssetEntityDefinition (AssetEntityDefinition assetEntityDefinition) {
      this.assetEntityDefinition = assetEntityDefinition;
   }

   public AssetGrainType getAssetGrainType () {
      return assetGrainType;
   }

   public void setAssetGrainType (AssetGrainType assetGrainType) {
      this.assetGrainType = assetGrainType;
   }

   public BusinessEntityDefinition getBusinessEntityDefinition () {
      return businessEntityDefinition;
   }

   public void setBusinessEntityDefinition (BusinessEntityDefinition businessEntityDefinition) {
      this.businessEntityDefinition = businessEntityDefinition;
   }

   public PrimaryMappingType getPrimary () {
      return primary;
   }

   public void setPrimary (PrimaryMappingType primary) {
      this.primary = primary;
   }

}
