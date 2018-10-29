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


package com.execue.core.common.bean.mapping;

import java.util.List;

import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;

public class ColumnMapping {

   private AssetEntityDefinition    assetEntityDefinition;
   private BusinessEntityDefinition businessEntityDefinition;
   private List<MemberMapping>      memberMappings;

   public AssetEntityDefinition getAssetEntityDefinition () {
      return assetEntityDefinition;
   }

   public void setAssetEntityDefinition (AssetEntityDefinition assetEntityDefinition) {
      this.assetEntityDefinition = assetEntityDefinition;
   }

   public List<MemberMapping> getMemberMappings () {
      return memberMappings;
   }

   public void setMemberMappings (List<MemberMapping> memberMappings) {
      this.memberMappings = memberMappings;
   }

   public BusinessEntityDefinition getBusinessEntityDefinition () {
      return businessEntityDefinition;
   }

   public void setBusinessEntityDefinition (BusinessEntityDefinition businessEntityDefinition) {
      this.businessEntityDefinition = businessEntityDefinition;
   }
}