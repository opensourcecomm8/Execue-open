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

import java.util.List;

import com.execue.core.common.type.AssetGrainType;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.GranularityType;

/**
 * @author JTiwari
 * @since 26/11/2009
 */

public class AssetGrainInfo {

   private Long               mappingId;
   private String             conceptDisplayName;
   private AssetGrainType     grainType;
   private List<BehaviorType> behaviorTypes;
   private GranularityType    grain;

   /**
    * @return the mappingId
    */
   public Long getMappingId () {
      return mappingId;
   }

   /**
    * @param mappingId
    *           the mappingId to set
    */
   public void setMappingId (Long mappingId) {
      this.mappingId = mappingId;
   }

   /**
    * @return the conceptDisplayName
    */
   public String getConceptDisplayName () {
      return conceptDisplayName;
   }

   /**
    * @param conceptDisplayName
    *           the conceptDisplayName to set
    */
   public void setConceptDisplayName (String conceptDisplayName) {
      this.conceptDisplayName = conceptDisplayName;
   }

   /**
    * @return the grainType
    */
   public AssetGrainType getGrainType () {
      return grainType;
   }

   /**
    * @param grainType
    *           the grainType to set
    */
   public void setGrainType (AssetGrainType grainType) {
      this.grainType = grainType;
   }

   public GranularityType getGrain () {
      return grain;
   }

   public void setGrain (GranularityType grain) {
      this.grain = grain;
   }

   public List<BehaviorType> getBehaviorTypes () {
      return behaviorTypes;
   }

   public void setBehaviorTypes (List<BehaviorType> behaviorTypes) {
      this.behaviorTypes = behaviorTypes;
   }

}
