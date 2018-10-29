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


package com.execue.core.common.bean;

import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.common.type.BehaviorType;

public class TypeConceptAssociationInfo {

   private BusinessEntityDefinition                sourceBed;
   private BusinessEntityDefinition                bedType;
   private Cloud                                   cloud;
   private Long                                    modelId;
   private List<BehaviorType>                      behaviorTypes;
   private Map<Long, List<EntityTripleDefinition>> attributePaths;
   private BusinessEntityDefinition                detailTypeBed;
   private boolean                                 attributesProvided;
   private boolean                                 behaviorProvided;
   private boolean                                 detailTypeProvided;
   // this section variables is for clearing the specific trace, else clean the entire tree.
   private List<BehaviorType>                      previousBehaviorTypes;
   private List<Long>                              previousAttributePathDefinitionIds;
   private boolean                                 previousBehaviorTypesProvided;
   private boolean                                 previousAttributePathDefinitionIdsProvided;
   private boolean                                 isAdvanceSave                        = false;
   private boolean                                 typeChangedFromLocationToNonLocation = false;

   public BusinessEntityDefinition getSourceBed () {
      return sourceBed;
   }

   public void setSourceBed (BusinessEntityDefinition sourceBed) {
      this.sourceBed = sourceBed;
   }

   public BusinessEntityDefinition getBedType () {
      return bedType;
   }

   public void setBedType (BusinessEntityDefinition bedType) {
      this.bedType = bedType;
   }

   public Cloud getCloud () {
      return cloud;
   }

   public void setCloud (Cloud cloud) {
      this.cloud = cloud;
   }

   public List<BehaviorType> getBehaviorTypes () {
      return behaviorTypes;
   }

   public void setBehaviorTypes (List<BehaviorType> behaviorTypes) {
      this.behaviorTypes = behaviorTypes;
   }

   public boolean isAttributesProvided () {
      return attributesProvided;
   }

   public void setAttributesProvided (boolean attributesProvided) {
      this.attributesProvided = attributesProvided;
   }

   public Map<Long, List<EntityTripleDefinition>> getAttributePaths () {
      return attributePaths;
   }

   public void setAttributePaths (Map<Long, List<EntityTripleDefinition>> attributePaths) {
      this.attributePaths = attributePaths;
   }

   public boolean isBehaviorProvided () {
      return behaviorProvided;
   }

   public void setBehaviorProvided (boolean behaviorProvided) {
      this.behaviorProvided = behaviorProvided;
   }

   public List<BehaviorType> getPreviousBehaviorTypes () {
      return previousBehaviorTypes;
   }

   public void setPreviousBehaviorTypes (List<BehaviorType> previousBehaviorTypes) {
      this.previousBehaviorTypes = previousBehaviorTypes;
   }

   public List<Long> getPreviousAttributePathDefinitionIds () {
      return previousAttributePathDefinitionIds;
   }

   public void setPreviousAttributePathDefinitionIds (List<Long> previousAttributePathDefinitionIds) {
      this.previousAttributePathDefinitionIds = previousAttributePathDefinitionIds;
   }

   public boolean isPreviousAttributePathDefinitionIdsProvided () {
      return previousAttributePathDefinitionIdsProvided;
   }

   public void setPreviousAttributePathDefinitionIdsProvided (boolean previousAttributePathDefinitionIdsProvided) {
      this.previousAttributePathDefinitionIdsProvided = previousAttributePathDefinitionIdsProvided;
   }

   public boolean isPreviousBehaviorTypesProvided () {
      return previousBehaviorTypesProvided;
   }

   public void setPreviousBehaviorTypesProvided (boolean previousBehaviorTypesProvided) {
      this.previousBehaviorTypesProvided = previousBehaviorTypesProvided;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public BusinessEntityDefinition getDetailTypeBed () {
      return detailTypeBed;
   }

   public void setDetailTypeBed (BusinessEntityDefinition detailTypeBed) {
      this.detailTypeBed = detailTypeBed;
   }

   public boolean isDetailTypeProvided () {
      return detailTypeProvided;
   }

   public void setDetailTypeProvided (boolean detailTypeProvided) {
      this.detailTypeProvided = detailTypeProvided;
   }

   public boolean isAdvanceSave () {
      return isAdvanceSave;
   }

   public void setAdvanceSave (boolean isAdvanceSave) {
      this.isAdvanceSave = isAdvanceSave;
   }

   public boolean isTypeChangedFromLocationToNonLocation () {
      return typeChangedFromLocationToNonLocation;
   }

   public void setTypeChangedFromLocationToNonLocation (boolean typeChangedFromLocationToNonLocation) {
      this.typeChangedFromLocationToNonLocation = typeChangedFromLocationToNonLocation;
   }

}
