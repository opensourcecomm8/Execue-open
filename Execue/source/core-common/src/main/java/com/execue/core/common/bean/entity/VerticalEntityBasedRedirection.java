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

import com.execue.core.common.type.BusinessEntityType;

public class VerticalEntityBasedRedirection implements Serializable {

   private Long               id;
   private Long               verticalId;
   private Long               applicationId;
   private Long               businessEntityId;
   private BusinessEntityType businessEntityType;
   private String             redirectUrl;

   private transient Long     entityId;
   private transient String   tkrName;

   public Long getVerticalId () {
      return verticalId;
   }

   public void setVerticalId (Long verticalId) {
      this.verticalId = verticalId;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public Long getBusinessEntityId () {
      return businessEntityId;
   }

   public void setBusinessEntityId (Long businessEntityId) {
      this.businessEntityId = businessEntityId;
   }

   public BusinessEntityType getBusinessEntityType () {
      return businessEntityType;
   }

   public void setBusinessEntityType (BusinessEntityType businessEntityType) {
      this.businessEntityType = businessEntityType;
   }

   public String getRedirectUrl () {
      return redirectUrl;
   }

   public void setRedirectUrl (String redirectUrl) {
      this.redirectUrl = redirectUrl;
   }

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the entityId
    */
   public Long getEntityId () {
      return entityId;
   }

   /**
    * @param entityId
    *           the entityId to set
    */
   public void setEntityId (Long entityId) {
      this.entityId = entityId;
   }

   public String getTkrName () {
      return tkrName;
   }

   public void setTkrName (String tkrName) {
      this.tkrName = tkrName;
   }

}
