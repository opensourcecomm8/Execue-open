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


package com.execue.core.common.bean.swi;

import java.io.Serializable;

import com.execue.core.common.type.BusinessEntityType;

/**
 * @author John Mallavalli
 */
public class BusinessEntityDefinitionInfo implements Serializable {

   private Long               id;
   private BusinessEntityType businessEntityType;
   private Long               popularity;
   private Long               modelGroupId;

   public Long getModelGroupId () {
      return modelGroupId;
   }

   public void setModelGroupId (Long modelGroupId) {
      this.modelGroupId = modelGroupId;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public BusinessEntityType getBusinessEntityType () {
      return businessEntityType;
   }

   public void setBusinessEntityType (BusinessEntityType businessEntityType) {
      this.businessEntityType = businessEntityType;
   }

   public Long getPopularity () {
      return popularity;
   }

   public void setPopularity (Long popularity) {
      this.popularity = popularity;
   }

}
