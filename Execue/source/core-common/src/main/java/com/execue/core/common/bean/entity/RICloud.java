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

import java.io.Serializable;

import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.CloudCategory;
import com.execue.core.common.type.CloudComponentSelectionType;
import com.execue.core.common.type.CloudOutput;
import com.execue.core.common.type.ComponentCategory;

public class RICloud implements Serializable {

   private static final long           serialVersionUID = 1L;

   private Long                        id;

   private Long                        cloudId;
   private String                      cloudName;
   private Long                        cloudOutputBusinessEntityId;
   private String                      cloudOutputName;
   private CloudCategory               cloudCategory;
   private CloudOutput                 cloudOutput;

   private Long                        modelGroupId;

   private Long                        componentBusinessEntityId;
   private String                      componentName;

   private Long                        componentTypeBusinessEntityId;
   private String                      componentTypeName;

   private Long                        realizationBusinessEntityId;
   private String                      realizationName;

   private Long                        requiredBehaviorBusinessEntityId;
   private String                      requiredBehaviorName;

   private ComponentCategory           componentCategory;
   private BusinessEntityType          representativeEntityType;
   private Double                      importance;
   private Integer                     frequency;
   private CheckType                   required;
   private String                      defaultValue;
   private Long                        primaryRICloudId;
   private CheckType                   outputComponent;
   private Integer                     cloudPart;
   private CloudComponentSelectionType cloudSelection   = CloudComponentSelectionType.ENOUGH_FOR_CLOUD_SELECTION;

   /**
    * @return the cloudSelection
    */
   public CloudComponentSelectionType getCloudSelection () {
      return cloudSelection;
   }

   /**
    * @param cloudSelection
    *           the cloudSelection to set
    */
   public void setCloudSelection (CloudComponentSelectionType cloudSelection) {
      this.cloudSelection = cloudSelection;
   }

   /**
    * @return the cloudPart
    */
   public Integer getCloudPart () {
      return cloudPart;
   }

   /**
    * @param cloudPart
    *           the cloudPart to set
    */
   public void setCloudPart (Integer cloudPart) {
      this.cloudPart = cloudPart;
   }

   /**
    * @return the outputComponent
    */
   public CheckType getOutputComponent () {
      return outputComponent;
   }

   /**
    * @param outputComponent
    *           the outputComponent to set
    */
   public void setOutputComponent (CheckType outputComponent) {
      this.outputComponent = outputComponent;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString () {
      StringBuffer sb = new StringBuffer();
      sb.append(cloudId).append(" ").append(cloudName);
      if (cloudOutputName != null) {
         sb.append(" ").append(cloudOutputName);
      }
      if (componentBusinessEntityId != null) {
         sb.append(" ").append(componentBusinessEntityId).append(" ").append(componentTypeName);
      }
      if (realizationBusinessEntityId != null) {
         sb.append(" ").append(realizationBusinessEntityId).append(" ").append(realizationName);
      }
      sb.append(" ").append(cloudCategory);
      return sb.toString();
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getCloudId () {
      return cloudId;
   }

   public void setCloudId (Long cloudId) {
      this.cloudId = cloudId;
   }

   public String getCloudName () {
      return cloudName;
   }

   public void setCloudName (String cloudName) {
      this.cloudName = cloudName;
   }

   public CloudCategory getCloudCategory () {
      return cloudCategory;
   }

   public void setCloudCategory (CloudCategory cloudCategory) {
      this.cloudCategory = cloudCategory;
   }

   public CloudOutput getCloudOutput () {
      return cloudOutput;
   }

   public void setCloudOutput (CloudOutput cloudOutput) {
      this.cloudOutput = cloudOutput;
   }

   public Long getModelGroupId () {
      return modelGroupId;
   }

   public void setModelGroupId (Long modelGroupId) {
      this.modelGroupId = modelGroupId;
   }

   public Long getComponentBusinessEntityId () {
      return componentBusinessEntityId;
   }

   public void setComponentBusinessEntityId (Long componentBusinessEntityId) {
      this.componentBusinessEntityId = componentBusinessEntityId;
   }

   public String getComponentName () {
      return componentName;
   }

   public void setComponentName (String componentName) {
      this.componentName = componentName;
   }

   public Long getComponentTypeBusinessEntityId () {
      return componentTypeBusinessEntityId;
   }

   public void setComponentTypeBusinessEntityId (Long componentTypeBusinessEntityId) {
      this.componentTypeBusinessEntityId = componentTypeBusinessEntityId;
   }

   public String getComponentTypeName () {
      return componentTypeName;
   }

   public void setComponentTypeName (String componentTypeName) {
      this.componentTypeName = componentTypeName;
   }

   public Long getRealizationBusinessEntityId () {
      return realizationBusinessEntityId;
   }

   public void setRealizationBusinessEntityId (Long realizationBusinessEntityId) {
      this.realizationBusinessEntityId = realizationBusinessEntityId;
   }

   public String getRealizationName () {
      return realizationName;
   }

   public void setRealizationName (String realizationName) {
      this.realizationName = realizationName;
   }

   public Long getRequiredBehaviorBusinessEntityId () {
      return requiredBehaviorBusinessEntityId;
   }

   public void setRequiredBehaviorBusinessEntityId (Long requiredBehaviorBusinessEntityId) {
      this.requiredBehaviorBusinessEntityId = requiredBehaviorBusinessEntityId;
   }

   public String getRequiredBehaviorName () {
      return requiredBehaviorName;
   }

   public void setRequiredBehaviorName (String requiredBehaviorName) {
      this.requiredBehaviorName = requiredBehaviorName;
   }

   public ComponentCategory getComponentCategory () {
      return componentCategory;
   }

   public void setComponentCategory (ComponentCategory componentCategory) {
      this.componentCategory = componentCategory;
   }

   public Double getImportance () {
      return importance;
   }

   public void setImportance (Double importance) {
      this.importance = importance;
   }

   public Integer getFrequency () {
      return frequency;
   }

   public void setFrequency (Integer frequency) {
      this.frequency = frequency;
   }

   public CheckType getRequired () {
      return required;
   }

   public void setRequired (CheckType required) {
      this.required = required;
   }

   public String getDefaultValue () {
      return defaultValue;
   }

   public void setDefaultValue (String defaultValue) {
      this.defaultValue = defaultValue;
   }

   public Long getPrimaryRICloudId () {
      return primaryRICloudId;
   }

   public void setPrimaryRICloudId (Long primaryRICloudId) {
      this.primaryRICloudId = primaryRICloudId;
   }

   public BusinessEntityType getRepresentativeEntityType () {
      return representativeEntityType;
   }

   public void setRepresentativeEntityType (BusinessEntityType representativeEntityType) {
      this.representativeEntityType = representativeEntityType;
   }

   public Long getCloudOutputBusinessEntityId () {
      return cloudOutputBusinessEntityId;
   }

   public void setCloudOutputBusinessEntityId (Long cloudOutputBusinessEntityId) {
      this.cloudOutputBusinessEntityId = cloudOutputBusinessEntityId;
   }

   public String getCloudOutputName () {
      return cloudOutputName;
   }

   public void setCloudOutputName (String cloudOutputName) {
      this.cloudOutputName = cloudOutputName;
   }
}