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

import com.execue.core.common.bean.IAssetEntity;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.type.AssetEntityType;

public class AssetEntityTerm {

   private IAssetEntity    assetEntity;
   private AssetEntityType assetEntityType;
   private Long            assetEntityDefinitionId;

   public IAssetEntity getAssetEntity () {
      return assetEntity;
   }

   public void setAssetEntity (IAssetEntity assetEntity) {
      this.assetEntity = assetEntity;
   }

   public AssetEntityType getAssetEntityType () {
      return assetEntityType;
   }

   public void setAssetEntityType (AssetEntityType assetEntityType) {
      this.assetEntityType = assetEntityType;
   }

   public Long getAssetEntityDefinitionId () {
      return assetEntityDefinitionId;
   }

   public void setAssetEntityDefinitionId (Long assetEntityDefinitionId) {
      this.assetEntityDefinitionId = assetEntityDefinitionId;
   }

   @Override
   public boolean equals (Object obj) {
      boolean isEqual = false;
      if (obj instanceof AssetEntityTerm) {
         AssetEntityTerm assetEntityTerm = (AssetEntityTerm) obj;
         if (this.assetEntityType.equals(assetEntityTerm.getAssetEntityType())) {
            if (this.assetEntityDefinitionId.longValue() == assetEntityTerm.getAssetEntityDefinitionId().longValue()) {
               isEqual = true;
            }
         }
      }
      return isEqual;
   }

   @Override
   public int hashCode () {
      return this.assetEntityDefinitionId.intValue();
   }

   @Override
   public String toString () {
      if (AssetEntityType.ASSET.equals(this.assetEntityType)) {
         Asset asset = (Asset) this.assetEntity;
      } else if (AssetEntityType.TABLE.equals(this.assetEntityType)) {
         Tabl tabl = (Tabl) this.assetEntity;
      } else if (AssetEntityType.COLUMN.equals(this.assetEntityType)) {
         Colum colum = (Colum) this.assetEntity;
      } else if (AssetEntityType.MEMBER.equals(this.assetEntityType)) {
         Membr membr = (Membr) this.assetEntity;
      }
      return super.toString();
   }
}
