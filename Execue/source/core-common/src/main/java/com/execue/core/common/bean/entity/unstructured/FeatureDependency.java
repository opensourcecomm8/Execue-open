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


package com.execue.core.common.bean.entity.unstructured;

import java.io.Serializable;

import com.execue.core.common.type.FeatureDependencyType;

public class FeatureDependency implements Serializable {

   /**
    * 
    */
   private static final long     serialVersionUID = 1L;
   private Long                  id;
   private Long                  contextId;
   private Long                  featureId;
   private Long                  dependencyFeatureId;
   private FeatureDependencyType dependencyType;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getFeatureId () {
      return featureId;
   }

   public void setFeatureId (Long featureId) {
      this.featureId = featureId;
   }

   public Long getDependencyFeatureId () {
      return dependencyFeatureId;
   }

   public void setDependencyFeatureId (Long dependencyFeatureId) {
      this.dependencyFeatureId = dependencyFeatureId;
   }

   public FeatureDependencyType getDependencyType () {
      return dependencyType;
   }

   public void setDependencyType (FeatureDependencyType dependencyType) {
      this.dependencyType = dependencyType;
   }

   public Long getContextId () {
      return contextId;
   }

   public void setContextId (Long contextId) {
      this.contextId = contextId;
   }

}
