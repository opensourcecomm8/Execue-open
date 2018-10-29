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

import com.execue.core.common.bean.BusinessTerm;

public class BusinessAssetTerm {

   private BusinessTerm                         businessTerm;
   private AssetEntityTerm                      assetEntityTerm;
   private List<LightAssetEntityDefinitionInfo> assetEntityDefinitions;

   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder();
      sb.append(businessTerm.getBusinessEntityTerm().getBusinessEntityDefinitionId());
      sb.append(assetEntityTerm.getAssetEntityDefinitionId());
      if (businessTerm.getBusinessStat() != null) {
         sb.append(businessTerm.getBusinessStat().getStat().getId());
      }
      sb.append(businessTerm.isFromCohort());
      return sb.toString();
   }

   @Override
   public boolean equals (Object obj) {
      return (obj instanceof BusinessAssetTerm || obj instanceof String) && this.toString().equals(obj.toString());
   }

   @Override
   public int hashCode () {
      return this.toString().hashCode();
   }

   public BusinessTerm getBusinessTerm () {
      return businessTerm;
   }

   public void setBusinessTerm (BusinessTerm businessTerm) {
      this.businessTerm = businessTerm;
   }

   public List<LightAssetEntityDefinitionInfo> getAssetEntityDefinitions () {
      return assetEntityDefinitions;
   }

   public void setAssetEntityDefinitions (List<LightAssetEntityDefinitionInfo> assetEntityDefinitions) {
      this.assetEntityDefinitions = assetEntityDefinitions;
   }

   public AssetEntityTerm getAssetEntityTerm () {
      return assetEntityTerm;
   }

   public void setAssetEntityTerm (AssetEntityTerm assetEntityTerm) {
      this.assetEntityTerm = assetEntityTerm;
   }

}
