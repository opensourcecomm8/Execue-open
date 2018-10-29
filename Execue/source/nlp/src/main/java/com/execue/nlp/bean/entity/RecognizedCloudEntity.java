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


/**
 * 
 */
package com.execue.nlp.bean.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.type.CloudCategory;
import com.execue.nlp.bean.association.AssociationPath;
import com.execue.nlp.rule.IRecognitionRule;
import com.execue.nlp.rule.IValidationRule;

/**
 * @author Nitesh
 */
public class RecognizedCloudEntity implements Cloneable {

   private Cloud                               cloud;
   private Long                                modelGroupId;
   private WeightInformation                   weightInformation;
   private List<IWeightedEntity>               recognitionEntities;
   private Set<IWeightedEntity>                allowedRecognitionEntities;
   private List<IValidationRule>               validationRules;
   private List<IRecognitionRule>              recognitionRules;
   private Map<Long, BusinessEntityDefinition> typeBedMap;
   private List<AssociationPath>               paths;
   private boolean                             defaultAdded;

   @Override
   public Object clone () throws CloneNotSupportedException {
      RecognizedCloudEntity clonedRecognizedCloudEntity = (RecognizedCloudEntity) super.clone();
      clonedRecognizedCloudEntity.setCloud(this.cloud.clone());
      clonedRecognizedCloudEntity.setModelGroupId(this.modelGroupId);
      clonedRecognizedCloudEntity.setWeightInformation(null); // As need to re-calculate the weight information
      clonedRecognizedCloudEntity.setRecognitionEntities(getClonedRecEntities());
      Set<IWeightedEntity> allowedRecognitionEntities = new HashSet<IWeightedEntity>();
      //TODO: NK:: Do we need to first clone and then add??
      allowedRecognitionEntities.addAll(this.getAllowedRecognitionEntities());
      clonedRecognizedCloudEntity.setAllowedRecognitionEntities(allowedRecognitionEntities);
      clonedRecognizedCloudEntity.setValidationRules(this.validationRules);
      clonedRecognizedCloudEntity.setRecognitionRules(this.recognitionRules);
      clonedRecognizedCloudEntity.setPaths(null); // if we need this, should reset it to appropriately scoped associations
      return clonedRecognizedCloudEntity;
   }

   /**
    * @return the List<IWeightedEntity>
    * @throws CloneNotSupportedException
    */
   private List<IWeightedEntity> getClonedRecEntities () throws CloneNotSupportedException {
      List<IWeightedEntity> recEntities = new ArrayList<IWeightedEntity>(1);
      if (recognitionEntities != null) {
         // for frame Work cloud don't clone the Rec Entities.
         if (cloud.getCategory() == CloudCategory.FRAMEWORK_CLOUD) {
            for (IWeightedEntity weightedEntity : recognitionEntities) {
               recEntities.add(weightedEntity);
            }
         } else {
            for (IWeightedEntity weightedEntity : recognitionEntities) {
               recEntities.add((IWeightedEntity) weightedEntity.clone());
            }
         }
      }
      return recEntities;
   }

   @Override
   public String toString () {
      StringBuffer sb = new StringBuffer();
      if (this.cloud != null) {
         sb.append(this.cloud.getId()).append(" ").append(this.cloud.getName()).append(" ").append(getWeight()).append(
                  " ").append(getQuality());
      }
      sb.append("\n").append(this.recognitionEntities);
      return sb.toString().trim();
   }

   @Override
   public boolean equals (Object obj) {
      return (obj instanceof RecognizedCloudEntity || obj instanceof String) && this.toString().equals(obj.toString());
   }

   @Override
   public int hashCode () {
      return this.toString().hashCode();
   }

   /**
    * @return the cloudComponents
    */
   public Set<Long> getCloudComponents () {
      if (CollectionUtils.isEmpty(cloud.getCloudComponentIds())) {
         for (CloudComponent cloudComponent : cloud.getCloudComponents()) {
            cloud.getCloudComponentIds().add(cloudComponent.getComponentBed().getId());
         }
      }
      return cloud.getCloudComponentIds();
   }

   /**
    * @return the cloudAllowedComponents
    */
   public Set<Long> getCloudAllowedComponents () {
      if (CollectionUtils.isEmpty(cloud.getCloudAllowedComponentsIds())) {
         Set<BusinessEntityDefinition> cloudAllowedComponents = cloud.getCloudAllowedComponents();
         for (BusinessEntityDefinition businessEntityDefinition : cloudAllowedComponents) {
            cloud.getCloudAllowedComponentsIds().add(businessEntityDefinition.getId());
         }
      }
      return cloud.getCloudAllowedComponentsIds();
   }

   /**
    * @return the Category of the cloud in the RecognizedCloudEntity
    */
   public CloudCategory getCategory () {
      return cloud.getCategory();
   }

   /**
    * @return the weight
    */
   public double getWeight () {
      if (weightInformation == null) {
         weightInformation = new WeightInformation();
      }
      return weightInformation.getRecognitionWeight();
   }

   public void addRecognitionEntity (IWeightedEntity weightedEntity) {
      if (recognitionEntities == null) {
         recognitionEntities = new ArrayList<IWeightedEntity>(1);
      }
      recognitionEntities.add(weightedEntity);
   }

   public void addAllRecognitionEntity (Collection<? extends IWeightedEntity> weightedEntities) {
      if (recognitionEntities == null) {
         recognitionEntities = new ArrayList<IWeightedEntity>(1);
      }
      recognitionEntities.addAll(weightedEntities);
   }

   /**
    * @return the recognitionEntities
    */
   public List<IWeightedEntity> getRecognitionEntities () {
      return recognitionEntities;
   }

   /**
    * @param recognitionEntities
    *           the recognitionEntities to set
    */
   public void setRecognitionEntities (List<IWeightedEntity> recognitionEntities) {
      this.recognitionEntities = recognitionEntities;
   }

   /**
    * @return the quality
    */
   public double getQuality () {
      if (weightInformation == null) {
         weightInformation = new WeightInformation();
      }
      return weightInformation.getRecognitionQuality();
   }

   /**
    * @return the typeBedMap
    */
   public Map<Long, BusinessEntityDefinition> getTypeBedMap () {
      if (typeBedMap == null) {
         typeBedMap = new HashMap<Long, BusinessEntityDefinition>();
      }
      return typeBedMap;
   }

   /**
    * @param typeBedMap
    *           the typeBedMap to set
    */
   public void setTypeBedMap (Map<Long, BusinessEntityDefinition> typeBedMap) {
      this.typeBedMap = typeBedMap;
   }

   /**
    * @return the validationRules
    */
   public List<IValidationRule> getValidationRules () {
      return validationRules;
   }

   /**
    * @param validationRules
    *           the validationRules to set
    */
   public void setValidationRules (List<IValidationRule> validationRules) {
      this.validationRules = validationRules;
   }

   /**
    * @return the recognitionRules
    */
   public List<IRecognitionRule> getRecognitionRules () {
      return recognitionRules;
   }

   /**
    * @param recognitionRules
    *           the recognitionRules to set
    */
   public void setRecognitionRules (List<IRecognitionRule> recognitionRules) {
      this.recognitionRules = recognitionRules;
   }

   /**
    * @return outputBedId
    */
   public Long getCloudOutputBedId () {
      return cloud.getOutputBedId();
   }

   /**
    * @return outputName
    */
   public String getCloudOutputName () {
      return cloud.getOutputName();
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
    * @return the weightInformation
    */
   public WeightInformation getWeightInformation () {
      return weightInformation;
   }

   /**
    * @param weightInformation
    *           the weightInformation to set
    */
   public void setWeightInformation (WeightInformation weightInformation) {
      this.weightInformation = weightInformation;
   }

   /**
    * @return the cloudComponentsFrequency
    */
   public Map<Long, Integer> getCloudComponentsFrequency () {

      return cloud.getCloudComponentsFrequency();
   }

   /**
    * @return the modelGroupId
    */
   public Long getModelGroupId () {
      return modelGroupId;
   }

   /**
    * @param modelGroupId
    *           the modelGroupId to set
    */
   public void setModelGroupId (Long modelGroupId) {
      this.modelGroupId = modelGroupId;
   }

   /**
    * This method returns true if weight assignment rule is associated to cloud, else false
    * 
    * @return boolean true/false
    */
   public boolean isWeightAssignmentRuleAssociated () {
      return true;
   }

   public Map<Long, CloudComponent> getCloudComponentInfoMap () {
      return cloud.getCloudComponentInfoMap();
   }

   public Set<Long> getCloudAllowedBehavior () {
      if (CollectionUtils.isEmpty(cloud.getCloudAllowedBehaviorIds())) {
         Set<BusinessEntityDefinition> cloudAllowedBehavior = cloud.getCloudAllowedBehavior();
         for (BusinessEntityDefinition behavior : cloudAllowedBehavior) {
            cloud.getCloudAllowedBehaviorIds().add(behavior.getId());
         }
      }
      return cloud.getCloudAllowedBehaviorIds();
   }

   public void addCloudAllowedBehavior (Long cloudAllowedBehavior) {
      cloud.getCloudAllowedBehaviorIds().add(cloudAllowedBehavior);
   }

   /**
    * @return the defaultAdded
    */
   public boolean isDefaultAdded () {
      return defaultAdded;
   }

   /**
    * @param defaultAdded
    *           the defaultAdded to set
    */
   public void setDefaultAdded (boolean defaultAdded) {
      this.defaultAdded = defaultAdded;
   }

   public List<AssociationPath> getPaths () {
      return paths;
   }

   public void setPaths (List<AssociationPath> paths) {
      this.paths = paths;
   }

   /**
    * @return the allowedRecognitionEntities
    */
   public Set<IWeightedEntity> getAllowedRecognitionEntities () {
      if (allowedRecognitionEntities == null) {
         allowedRecognitionEntities = new HashSet<IWeightedEntity>();
      }
      return allowedRecognitionEntities;
   }

   /**
    * @param allowedRecognitionEntities the allowedRecognitionEntities to set
    */
   public void setAllowedRecognitionEntities (Set<IWeightedEntity> allowedRecognitionEntities) {
      this.allowedRecognitionEntities = allowedRecognitionEntities;
   }

   public void addAllowedRecognitionEntity (IWeightedEntity weightedEntity) {
      getAllowedRecognitionEntities().add(weightedEntity);
   }
}