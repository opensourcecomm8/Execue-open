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

import java.util.Date;
import java.util.Set;

import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ModelCategoryType;

/**
 * This class represents the Model object.
 */
public class Model implements java.io.Serializable {

   private static final long      serialVersionUID        = 1L;
   private Long                   id;
   private String                 name;
   private Date                   createdDate;
   private Date                   modifiedDate;
   private ModelCategoryType      category                = ModelCategoryType.USER;
   private Long                   popularity;
   private StatusEnum             status                  = StatusEnum.INACTIVE;
   private CheckType              evaluate                = CheckType.YES;
   private CheckType              indexEvaluationRequired = CheckType.YES;
   private Set<Application>       applications;
   private Set<ModelGroup>        modelGroups;
   private Set<ModelGroupMapping> modelGroupMappings;
   private Set<Cloud>             clouds;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public Long getPopularity () {
      return popularity;
   }

   public void setPopularity (Long popularity) {
      this.popularity = popularity;
   }

   public StatusEnum getStatus () {
      return status;
   }

   public void setStatus (StatusEnum status) {
      this.status = status;
   }

   public Set<Application> getApplications () {
      return applications;
   }

   public void setApplications (Set<Application> applications) {
      this.applications = applications;
   }

   public Set<ModelGroup> getModelGroups () {
      return modelGroups;
   }

   public void setModelGroups (Set<ModelGroup> modelGroups) {
      this.modelGroups = modelGroups;
   }

   public Set<ModelGroupMapping> getModelGroupMappings () {
      return modelGroupMappings;
   }

   public void setModelGroupMappings (Set<ModelGroupMapping> modelGroupMappings) {
      this.modelGroupMappings = modelGroupMappings;
   }

   public Date getCreatedDate () {
      return createdDate;
   }

   public void setCreatedDate (Date createdDate) {
      this.createdDate = createdDate;
   }

   public Date getModifiedDate () {
      return modifiedDate;
   }

   public void setModifiedDate (Date modifiedDate) {
      this.modifiedDate = modifiedDate;
   }

   @Override
   public boolean equals (Object obj) {
      return this.id.equals(((Model) obj).getId());
   }

   @Override
   public int hashCode () {
      return this.id.intValue();
   }

   /**
    * @return the clouds
    */
   public Set<Cloud> getClouds () {
      return clouds;
   }

   /**
    * @param clouds
    *           the clouds to set
    */
   public void setClouds (Set<Cloud> clouds) {
      this.clouds = clouds;
   }

   /**
    * @return the evaluate
    */
   public CheckType getEvaluate () {
      return evaluate;
   }

   /**
    * @param evaluate
    *           the evaluate to set
    */
   public void setEvaluate (CheckType evaluate) {
      this.evaluate = evaluate;
   }

   /**
    * @return the indexEvaluationRequired
    */
   public CheckType getIndexEvaluationRequired () {
      return indexEvaluationRequired;
   }

   /**
    * @param indexEvaluationRequired
    *           the indexEvaluationRequired to set
    */
   public void setIndexEvaluationRequired (CheckType indexEvaluationRequired) {
      this.indexEvaluationRequired = indexEvaluationRequired;
   }

   public ModelCategoryType getCategory () {
      return category;
   }

   public void setCategory (ModelCategoryType category) {
      this.category = category;
   }

}
