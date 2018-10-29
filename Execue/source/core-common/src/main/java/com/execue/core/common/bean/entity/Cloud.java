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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.CloudCategory;
import com.execue.core.common.type.CloudOutput;

public class Cloud implements Serializable {

   private static final long                   serialVersionUID = 1L;
   private Long                                id;
   private String                              name;
   private CloudCategory                       category;
   private Long                                outputBedId;
   private String                              outputName;
   private CloudOutput                         cloudOutput;
   private String                              cloudParticipation;
   private Integer                             requiredComponentCount;
   private Set<Model>                          models;
   private Set<CloudComponent>                 cloudComponents;
   private Set<BusinessEntityDefinition>       cloudAllowedComponents;
   private Set<BusinessEntityDefinition>       cloudAllowedBehavior;
   private Set<Rule>                           cloudValidationRules;
   private CheckType                           isDefault;
   private transient Set<Long>                 cloudAllowedComponentsIds;
   private transient Set<Long>                 cloudAllowedBehaviorIds;
   private transient Set<Long>                 cloudComponentIds;
   private transient Map<Long, Integer>        cloudComponentsFrequency;
   private transient Map<Long, CloudComponent> cloudComponentInfoMap;

   // TODO: NK: We should also have cloud recognition rules

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
    * @return the name
    */
   public String getName () {
      return name;
   }

   /**
    * @param name
    *           the name to set
    */
   public void setName (String name) {
      this.name = name;
   }

   /**
    * @return the category
    */
   public CloudCategory getCategory () {
      return category;
   }

   /**
    * @param category
    *           the category to set
    */
   public void setCategory (CloudCategory category) {
      this.category = category;
   }

   /**
    * @return the cloudOutput
    */
   public CloudOutput getCloudOutput () {
      return cloudOutput;
   }

   /**
    * @param cloudOutput
    *           the cloudOutput to set
    */
   public void setCloudOutput (CloudOutput cloudOutput) {
      this.cloudOutput = cloudOutput;
   }

   /**
    * @return the models
    */
   public Set<Model> getModels () {
      return models;
   }

   /**
    * @param models
    *           the models to set
    */
   public void setModels (Set<Model> models) {
      this.models = models;
   }

   /**
    * @return the cloudComponents
    */
   public Set<CloudComponent> getCloudComponents () {
      return cloudComponents;
   }

   /**
    * @param cloudComponents
    *           the cloudComponents to set
    */
   public void setCloudComponents (Set<CloudComponent> cloudComponents) {
      this.cloudComponents = cloudComponents;
   }

   /**
    * @return the cloudAllowedComponents
    */
   public Set<BusinessEntityDefinition> getCloudAllowedComponents () {
      return cloudAllowedComponents;
   }

   /**
    * @param cloudAllowedComponents
    *           the cloudAllowedComponents to set
    */
   public void setCloudAllowedComponents (Set<BusinessEntityDefinition> cloudAllowedComponents) {
      this.cloudAllowedComponents = cloudAllowedComponents;
   }

   /**
    * @return the cloudValidationRules
    */
   public Set<Rule> getCloudValidationRules () {
      return cloudValidationRules;
   }

   /**
    * @param cloudValidationRules
    *           the cloudValidationRules to set
    */
   public void setCloudValidationRules (Set<Rule> cloudValidationRules) {
      this.cloudValidationRules = cloudValidationRules;
   }

   /**
    * @return the cloudParticipation
    */
   public String getCloudParticipation () {
      return cloudParticipation;
   }

   /**
    * @param cloudParticipation
    *           the cloudParticipation to set
    */
   public void setCloudParticipation (String cloudParticipation) {
      this.cloudParticipation = cloudParticipation;
   }

   /**
    * @return the requiredComponentCount
    */
   public Integer getRequiredComponentCount () {
      return requiredComponentCount;
   }

   /**
    * @param requiredComponentCount
    *           the requiredComponentCount to set
    */
   public void setRequiredComponentCount (Integer requiredComponentCount) {
      this.requiredComponentCount = requiredComponentCount;
   }

   public Set<BusinessEntityDefinition> getCloudAllowedBehavior () {
      return cloudAllowedBehavior;
   }

   public void setCloudAllowedBehavior (Set<BusinessEntityDefinition> cloudAllowedBehavior) {
      this.cloudAllowedBehavior = cloudAllowedBehavior;
   }

   public CheckType getIsDefault () {
      return isDefault;
   }

   public void setIsDefault (CheckType isDefault) {
      this.isDefault = isDefault;
   }

   /**
    * @return the cloudAllowedComponentsIds
    */
   public Set<Long> getCloudAllowedComponentsIds () {
      if (cloudAllowedComponentsIds == null) {
         cloudAllowedComponentsIds = new HashSet<Long>(1);
         return cloudAllowedComponentsIds;
      }
      return cloudAllowedComponentsIds;
   }

   /**
    * @param cloudAllowedComponentsIds
    *           the cloudAllowedComponentsIds to set
    */
   public void setCloudAllowedComponentsIds (Set<Long> cloudAllowedComponentsIds) {
      this.cloudAllowedComponentsIds = cloudAllowedComponentsIds;
   }

   /**
    * @return the cloudAllowedBehaviorIds
    */
   public Set<Long> getCloudAllowedBehaviorIds () {
      if (cloudAllowedBehaviorIds == null) {
         cloudAllowedBehaviorIds = new HashSet<Long>(1);
         return cloudAllowedBehaviorIds;
      }
      return cloudAllowedBehaviorIds;
   }

   /**
    * @param cloudAllowedBehaviorIds
    *           the cloudAllowedBehaviorIds to set
    */
   public void setCloudAllowedBehaviorIds (Set<Long> cloudAllowedBehaviorIds) {
      this.cloudAllowedBehaviorIds = cloudAllowedBehaviorIds;
   }

   /**
    * @return the cloudComponentIds
    */
   public Set<Long> getCloudComponentIds () {
      if (cloudComponentIds == null) {
         cloudComponentIds = new HashSet<Long>(1);
         return cloudComponentIds;
      }
      return cloudComponentIds;
   }

   /**
    * @param cloudComponentIds
    *           the cloudComponentIds to set
    */
   public void setCloudComponentIds (Set<Long> cloudComponentIds) {
      this.cloudComponentIds = cloudComponentIds;
   }

   /**
    * @return the cloudComponentsFrequency
    */
   public Map<Long, Integer> getCloudComponentsFrequency () {
      if (cloudComponentsFrequency == null) {
         cloudComponentsFrequency = new HashMap<Long, Integer>(1);
         return cloudComponentsFrequency;
      }
      return cloudComponentsFrequency;
   }

   /**
    * @param cloudComponentsFrequency
    *           the cloudComponentsFrequency to set
    */
   public void setCloudComponentsFrequency (Map<Long, Integer> cloudComponentsFrequency) {
      this.cloudComponentsFrequency = cloudComponentsFrequency;
   }

   /**
    * @return the cloudComponentInfoMap
    */
   public Map<Long, CloudComponent> getCloudComponentInfoMap () {
      if (cloudComponentInfoMap == null) {
         cloudComponentInfoMap = new HashMap<Long, CloudComponent>(1);
         return cloudComponentInfoMap;
      }
      return cloudComponentInfoMap;
   }

   /**
    * @param cloudComponentInfoMap
    *           the cloudComponentInfoMap to set
    */
   public void setCloudComponentInfoMap (Map<Long, CloudComponent> cloudComponentInfoMap) {
      this.cloudComponentInfoMap = cloudComponentInfoMap;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#clone()
    */
   @Override
   public Cloud clone () throws CloneNotSupportedException {
      Cloud cloud = new Cloud();
      cloud.setName(name);
      cloud.setId(id);
      cloud.setCategory(category);
      cloud.setCloudAllowedBehavior(cloudAllowedBehavior);
      cloud.setCloudAllowedBehaviorIds(cloudAllowedBehaviorIds);
      cloud.setCloudAllowedComponents(cloudAllowedComponents);
      cloud.setCloudAllowedComponentsIds(cloudAllowedComponentsIds);
      cloud.setCloudComponentIds(cloudComponentIds);
      cloud.setCloudComponents(cloudComponents);
      cloud.setCloudOutput(cloudOutput);
      cloud.setCloudValidationRules(cloudValidationRules);
      cloud.setCloudParticipation(cloudParticipation);
      cloud.setIsDefault(isDefault);
      cloud.setModels(models);
      cloud.setRequiredComponentCount(requiredComponentCount);
      cloud.setOutputBedId(outputBedId);
      cloud.setOutputName(outputName);
      if (cloudComponentInfoMap != null) {
         Map<Long, CloudComponent> clonedCloudComponentInfoMap = new HashMap<Long, CloudComponent>(1);
         for (Entry<Long, CloudComponent> entry : cloudComponentInfoMap.entrySet()) {
            clonedCloudComponentInfoMap.put(entry.getKey(), (CloudComponent) entry.getValue().clone());
         }
         cloud.setCloudComponentInfoMap(clonedCloudComponentInfoMap);
      }
      cloud.setCloudComponentsFrequency(cloudComponentsFrequency);
      return cloud;
   }

   public Long getOutputBedId () {
      return outputBedId;
   }

   public void setOutputBedId (Long outputBedId) {
      this.outputBedId = outputBedId;
   }

   public String getOutputName () {
      return outputName;
   }

   public void setOutputName (String outputName) {
      this.outputName = outputName;
   }
}