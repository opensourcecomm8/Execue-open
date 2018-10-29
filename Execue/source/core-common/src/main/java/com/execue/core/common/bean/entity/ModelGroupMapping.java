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

import com.execue.core.common.type.CheckType;

public class ModelGroupMapping implements Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private Model             model;
   private ModelGroup        modelGroup;
   private CheckType         owner;
   private CheckType         primary;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Model getModel () {
      return model;
   }

   public void setModel (Model model) {
      this.model = model;
   }

   public ModelGroup getModelGroup () {
      return modelGroup;
   }

   public void setModelGroup (ModelGroup modelGroup) {
      this.modelGroup = modelGroup;
   }

   public CheckType getOwner () {
      return owner;
   }

   public void setOwner (CheckType owner) {
      this.owner = owner;
   }

   public CheckType getPrimary () {
      return primary;
   }

   public void setPrimary (CheckType primary) {
      this.primary = primary;
   }
}
