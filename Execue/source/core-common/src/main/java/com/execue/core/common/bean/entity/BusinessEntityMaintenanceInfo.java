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

import com.execue.core.common.type.EntityType;
import com.execue.core.common.type.OperationType;

public class BusinessEntityMaintenanceInfo implements Serializable {

   private Long          id;
   private Long          entityId;
   private EntityType    entityType;
   private Long          modelId;
   private OperationType operationType;
   private Long          parentId;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public EntityType getEntityType () {
      return entityType;
   }

   public void setEntityType (EntityType entityType) {
      this.entityType = entityType;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public OperationType getOperationType () {
      return operationType;
   }

   public void setOperationType (OperationType operationType) {
      this.operationType = operationType;
   }

   public Long getEntityId () {
      return entityId;
   }

   public void setEntityId (Long entityId) {
      this.entityId = entityId;
   }

   public Long getParentId () {
      return parentId;
   }

   public void setParentId (Long parentId) {
      this.parentId = parentId;
   }
}
