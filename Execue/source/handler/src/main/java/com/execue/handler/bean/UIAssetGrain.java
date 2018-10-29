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


package com.execue.handler.bean;

import com.execue.core.common.type.AssetGrainType;

/**
 * This bean represents the grain definition object which will be parsed by screen to show the exisitng grain
 * 
 * @author Vishay
 * @version 1.0
 * @since 14/07/09
 */
public class UIAssetGrain {

   private Long           mappingId;
   private AssetGrainType grainType;
   private String         conceptDisplayName;

   public Long getMappingId () {
      return mappingId;
   }

   public void setMappingId (Long mappingId) {
      this.mappingId = mappingId;
   }

   public String getConceptDisplayName () {
      return conceptDisplayName;
   }

   public void setConceptDisplayName (String conceptDisplayName) {
      this.conceptDisplayName = conceptDisplayName;
   }

   public AssetGrainType getGrainType () {
      return grainType;
   }

   public void setGrainType (AssetGrainType grainType) {
      this.grainType = grainType;
   }
}
