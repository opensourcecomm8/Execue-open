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


package com.execue.handler.bean;

import com.execue.core.common.type.CheckType;
import com.execue.handler.bean.grid.IGridBean;

public class UIMember implements IGridBean {

   private String    name;
   private Long      id;
   private Long      optionalMemberId;
   private String    description;
   private boolean   instanceExist;
   private Long      parentConceptId;
   private CheckType hasAclPermission = CheckType.NO;

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   public boolean isInstanceExist () {
      return instanceExist;
   }

   public void setInstanceExist (boolean instanceExist) {
      this.instanceExist = instanceExist;
   }

   public Long getParentConceptId () {
      return parentConceptId;
   }

   public void setParentConceptId (Long parentConceptId) {
      this.parentConceptId = parentConceptId;
   }

   public Long getOptionalMemberId () {
      return optionalMemberId;
   }

   public void setOptionalMemberId (Long optionalMemberId) {
      this.optionalMemberId = optionalMemberId;
   }

   /**
    * @return the hasAclPermission
    */
   public CheckType getHasAclPermission () {
      return hasAclPermission;
   }

   /**
    * @param hasAclPermission the hasAclPermission to set
    */
   public void setHasAclPermission (CheckType hasAclPermission) {
      this.hasAclPermission = hasAclPermission;
   }

}
