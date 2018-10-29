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

import java.util.Set;

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ModelCategoryType;

/**
 * This class represents the Model Group object.
 */
public class ModelGroup implements java.io.Serializable {

   private static final long             serialVersionUID = 1L;
   private Long                          id;
   private String                        name;
   private ModelCategoryType             category         = ModelCategoryType.USER;
   private Long                          contextId;
   private CheckType                     shared           = CheckType.NO;
   private Set<Model>                    models;
   private Set<ModelGroupMapping>        modelGroupMappings;
   private Set<BusinessEntityDefinition> businessEntityDefinitions;

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

   public Set<Model> getModels () {
      return models;
   }

   public void setModels (Set<Model> models) {
      this.models = models;
   }

   public Set<ModelGroupMapping> getModelGroupMappings () {
      return modelGroupMappings;
   }

   public void setModelGroupMappings (Set<ModelGroupMapping> modelGroupMappings) {
      this.modelGroupMappings = modelGroupMappings;
   }

   public Set<BusinessEntityDefinition> getBusinessEntityDefinitions () {
      return businessEntityDefinitions;
   }

   public void setBusinessEntityDefinitions (Set<BusinessEntityDefinition> businessEntityDefinitions) {
      this.businessEntityDefinitions = businessEntityDefinitions;
   }

   public ModelCategoryType getCategory () {
      return category;
   }

   public void setCategory (ModelCategoryType category) {
      this.category = category;
   }

   public Long getContextId () {
      return contextId;
   }

   public void setContextId (Long contextId) {
      this.contextId = contextId;
   }

   public CheckType getShared () {
      return shared;
   }

   public void setShared (CheckType shared) {
      this.shared = shared;
   }

}
