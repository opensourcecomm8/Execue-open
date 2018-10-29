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

/**
 * @author john
 */
public class BusinessEntityVariation implements Serializable {

   private Long   id;
   private Long   businessEntityId;
   private Long   modelGroupId;
   private String variation;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getBusinessEntityId () {
      return businessEntityId;
   }

   public void setBusinessEntityId (Long businessEntityId) {
      this.businessEntityId = businessEntityId;
   }

   public String getVariation () {
      return variation;
   }

   public void setVariation (String variation) {
      this.variation = variation;
   }

   public Long getModelGroupId () {
      return modelGroupId;
   }

   public void setModelGroupId (Long modelGroupId) {
      this.modelGroupId = modelGroupId;
   }
}
