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

public class SecondaryWord implements java.io.Serializable {

   private Long       id;
   private String     word;
   private Double     defaultWeight;
   private Long       frequency;
   private ModelGroup modelGroup;

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

   public Double getDefaultWeight () {
      return defaultWeight;
   }

   public void setDefaultWeight (Double defaultWeight) {
      this.defaultWeight = defaultWeight;
   }

   public Long getFrequency () {
      return frequency;
   }

   public void setFrequency (Long frequency) {
      this.frequency = frequency;
   }

   public ModelGroup getModelGroup () {
      return modelGroup;
   }

   public void setModelGroup (ModelGroup modelGroup) {
      this.modelGroup = modelGroup;
   }

}
