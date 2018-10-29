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


package com.execue.core.common.bean.security;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.type.BooleanType;

public class SecurityRoles implements Serializable {

   private Long                id;
   private String              name;
   private String              description;
   private StatusEnum          status;
   private BooleanType         systemRole;
   private Date                createdDate;
   private Date                modifiedDate;
   private Set<SecurityGroups> securityGroups;

   public BooleanType getSystemRole () {
      return systemRole;
   }

   public void setSystemRole (BooleanType systemRole) {
      this.systemRole = systemRole;
   }

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

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   public StatusEnum getStatus () {
      return status;
   }

   public void setStatus (StatusEnum status) {
      this.status = status;
   }

   public Set<SecurityGroups> getSecurityGroups () {
      return securityGroups;
   }

   public void setSecurityGroups (Set<SecurityGroups> securityGroups) {
      this.securityGroups = securityGroups;
   }

   public Date getCreatedDate () {
      return createdDate;
   }

   public void setCreatedDate (Date createdDate) {
      this.createdDate = createdDate;
   }

   public Date getModifiedDate () {
      return modifiedDate;
   }

   public void setModifiedDate (Date modifiedDate) {
      this.modifiedDate = modifiedDate;
   }

}
