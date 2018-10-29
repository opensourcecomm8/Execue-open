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

import java.util.List;

import com.execue.core.common.type.CheckType;

public class UITable {

   private String         name;
   private String         owner;
   private String         actualName;
   private String         displayName;
   private char           virtual;
   private Long           id;
   private String         description;
   private String         lookupColumnName;
   private List<UIColumn> columns;
   private boolean        absorbed                    = true;
   private boolean        pkDefined                   = true;
   private boolean        constraintDefined           = true;
   private CheckType      eligibleSystemDefaultMetric = CheckType.NO;
   private CheckType      hasAclPermission            = CheckType.NO;

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

   public List<UIColumn> getColumns () {
      return columns;
   }

   public void setColumns (List<UIColumn> columns) {
      this.columns = columns;
   }

   public String getLookupColumnName () {
      return lookupColumnName;
   }

   public void setLookupColumnName (String lookupColumnName) {
      this.lookupColumnName = lookupColumnName;
   }

   public boolean isAbsorbed () {
      return absorbed;
   }

   public void setAbsorbed (boolean absorbed) {
      this.absorbed = absorbed;
   }

   public boolean isPkDefined () {
      return pkDefined;
   }

   public void setPkDefined (boolean pkDefined) {
      this.pkDefined = pkDefined;
   }

   public boolean isConstraintDefined () {
      return constraintDefined;
   }

   public void setConstraintDefined (boolean constraintDefined) {
      this.constraintDefined = constraintDefined;
   }

   public String getOwner () {
      return owner;
   }

   public void setOwner (String owner) {
      this.owner = owner;
   }

   public String getActualName () {
      return actualName;
   }

   public void setActualName (String actualName) {
      this.actualName = actualName;
   }

   public char getVirtual () {
      return virtual;
   }

   public void setVirtual (char virtual) {
      this.virtual = virtual;
   }

   /**
    * @return the displayName
    */
   public String getDisplayName () {
      return displayName;
   }

   /**
    * @param displayName
    *           the displayName to set
    */
   public void setDisplayName (String displayName) {
      this.displayName = displayName;
   }

   public CheckType getEligibleSystemDefaultMetric () {
      return eligibleSystemDefaultMetric;
   }

   public void setEligibleSystemDefaultMetric (CheckType eligibleSystemDefaultMetric) {
      this.eligibleSystemDefaultMetric = eligibleSystemDefaultMetric;
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
