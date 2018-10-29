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

public class KeyWord implements java.io.Serializable {

   private Long                     id;
   private BusinessEntityDefinition businessEntityDefinition;
   private ModelGroup               modelGroup;
   private String                   word;
   private Set<ParallelWord>        parallelWords;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getWord () {
      return word;
   }

   public void setWord (String word) {
      this.word = word;
   }

   public Set<ParallelWord> getParallelWords () {
      return parallelWords;
   }

   public void setParallelWords (Set<ParallelWord> parallelWords) {
      this.parallelWords = parallelWords;
   }

   public ModelGroup getModelGroup () {
      return modelGroup;
   }

   public void setModelGroup (ModelGroup modelGroup) {
      this.modelGroup = modelGroup;
   }

   public BusinessEntityDefinition getBusinessEntityDefinition () {
      return businessEntityDefinition;
   }

   public void setBusinessEntityDefinition (BusinessEntityDefinition businessEntityDefinition) {
      this.businessEntityDefinition = businessEntityDefinition;
   }

}
