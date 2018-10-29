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
import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.CloudComponentSelectionType;
import com.execue.core.common.type.ComponentCategory;

public class CloudComponent implements Serializable, Cloneable {

   private static final long                 serialVersionUID = 1L;
   private Long                              id;
   private Cloud                             cloud;
   private BusinessEntityDefinition          componentBed;
   private BusinessEntityDefinition          componentTypeBed;
   private Double                            importance;
   private Integer                           frequency;
   // TODO: -JM- check with AP regarding this field
   private ComponentCategory                 componentCategory;
   private BusinessEntityType                representativeEntityType;
   private Long                              requiredBehavior;
   private CheckType                         required;
   private CheckType                         outputComponent;
   private String                            defaultValue;
   private transient List<WeightInformation> weightInfoList;
   private transient List<IWeightedEntity>   recognitionEntities;
   private Integer                           cloudPart;
   private CloudComponentSelectionType       cloudSelection   = CloudComponentSelectionType.ENOUGH_FOR_CLOUD_SELECTION; ;

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

   @Override
   public Object clone () throws CloneNotSupportedException {
      CloudComponent clonedCloudComponentInfo = (CloudComponent) super.clone();
      clonedCloudComponentInfo.setId(id);
      clonedCloudComponentInfo.setCloud(cloud);
      clonedCloudComponentInfo.setComponentBed(componentBed);
      clonedCloudComponentInfo.setComponentTypeBed(componentTypeBed);
      clonedCloudComponentInfo.setImportance(importance);
      clonedCloudComponentInfo.setFrequency(frequency);
      clonedCloudComponentInfo.setComponentCategory(componentCategory);
      clonedCloudComponentInfo.setRepresentativeEntityType(representativeEntityType);
      clonedCloudComponentInfo.setRequiredBehavior(requiredBehavior);
      clonedCloudComponentInfo.setRequired(required);
      clonedCloudComponentInfo.setDefaultValue(defaultValue);
      if (weightInfoList != null) {
         List<WeightInformation> clonedWeightInfoList = new ArrayList<WeightInformation>(1);
         clonedWeightInfoList.addAll(weightInfoList);
         clonedCloudComponentInfo.setWeightInfoList(clonedWeightInfoList);
      }
      if (recognitionEntities != null) {
         List<IWeightedEntity> clonedRecognitionEntities = new ArrayList<IWeightedEntity>(1);
         clonedRecognitionEntities.addAll(recognitionEntities);
         clonedCloudComponentInfo.setRecognitionEntities(clonedRecognitionEntities);
      }
      return clonedCloudComponentInfo;
   }

   public List<IWeightedEntity> getRecognitionEntities () {
      return recognitionEntities;
   }

   public void setRecognitionEntities (List<IWeightedEntity> recognitionEntities) {
      this.recognitionEntities = recognitionEntities;
   }

   public List<WeightInformation> getWeightInfoList () {
      return weightInfoList;
   }

   public void setWeightInfoList (List<WeightInformation> weightInfoList) {
      this.weightInfoList = weightInfoList;
   }

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the cloud
    */
   public Cloud getCloud () {
      return cloud;
   }

   /**
    * @param cloud
    *           the cloud to set
    */
   public void setCloud (Cloud cloud) {
      this.cloud = cloud;
   }

   /**
    * @return the componentBedId
    */
   public BusinessEntityDefinition getComponentBed () {
      return componentBed;
   }

   /**
    * @param componentBedId
    *           the componentBedId to set
    */
   public void setComponentBed (BusinessEntityDefinition componentBed) {
      this.componentBed = componentBed;
   }

   /**
    * @return the componentTypeBedId
    */
   public BusinessEntityDefinition getComponentTypeBed () {
      return componentTypeBed;
   }

   /**
    * @param componentTypeBedId
    *           the componentTypeBedId to set
    */
   public void setComponentTypeBed (BusinessEntityDefinition componentTypeBed) {
      this.componentTypeBed = componentTypeBed;
   }

   /**
    * @return the weight
    */
   public Double getImportance () {
      return importance;
   }

   /**
    * @param weight
    *           the weight to set
    */
   public void setImportance (Double importance) {
      this.importance = importance;
   }

   /**
    * @return the frequency
    */
   public Integer getFrequency () {
      return frequency;
   }

   /**
    * @param frequency
    *           the frequency to set
    */
   public void setFrequency (Integer frequency) {
      this.frequency = frequency;
   }

   /**
    * @return the componentCategory
    */
   public ComponentCategory getComponentCategory () {
      return componentCategory;
   }

   /**
    * @param componentCategory
    *           the componentCategory to set
    */
   public void setComponentCategory (ComponentCategory componentCategory) {
      this.componentCategory = componentCategory;
   }

   /**
    * @return the requiredBehavior
    */
   public Long getRequiredBehavior () {
      return requiredBehavior;
   }

   /**
    * @param requiredBehavior
    *           the requiredBehavior to set
    */
   public void setRequiredBehavior (Long requiredBehavior) {
      this.requiredBehavior = requiredBehavior;
   }

   /**
    * @return the required
    */
   public CheckType getRequired () {
      return required;
   }

   /**
    * @param required
    *           the required to set
    */
   public void setRequired (CheckType required) {
      this.required = required;
   }

   /**
    * @return the defaultValue
    */
   public String getDefaultValue () {
      return defaultValue;
   }

   /**
    * @param defaultValue
    *           the defaultValue to set
    */
   public void setDefaultValue (String defaultValue) {
      this.defaultValue = defaultValue;
   }

   public boolean isCloudComponentRequired () {
      return (this.getRequired() == CheckType.YES);
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

   /**
    * @return representativeEntityType
    */
   public BusinessEntityType getRepresentativeEntityType () {
      return representativeEntityType;
   }

   /**
    * @param representativeEntityType
    */
   public void setRepresentativeEntityType (BusinessEntityType representativeEntityType) {
      this.representativeEntityType = representativeEntityType;
   }

}