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

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.type.CheckType;

public class UIConcept implements Comparable<UIConcept> {

   private String           name;
   private Long             id;
   private Long             bedId;
   private String           description;
   private String           displayName;
   private List<UIInstance> instances;
   private boolean          existing;
   private CheckType        isHierarchyExists = CheckType.NO;
   private int              prominentMeasureLimit;
   private int              prominentDimensionLimit;

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

   public List<UIInstance> getInstances () {
      return instances;
   }

   public void setInstances (List<UIInstance> instances) {
      this.instances = instances;
   }

   public String getDisplayName () {
      return displayName;
   }

   public void setDisplayName (String displayName) {
      this.displayName = displayName;
   }

   public boolean isExisting () {
      return existing;
   }

   public void setExisting (boolean existing) {
      this.existing = existing;
   }

   public Long getBedId () {
      return bedId;
   }

   public void setBedId (Long bedId) {
      this.bedId = bedId;
   }

   public CheckType getIsHierarchyExists () {
      return isHierarchyExists;
   }

   public void setIsHierarchyExists (CheckType isHierarchyExists) {
      this.isHierarchyExists = isHierarchyExists;
   }

   public int getProminentMeasureLimit () {
      return prominentMeasureLimit;
   }

   public void setProminentMeasureLimit (int prominentMeasureLimit) {
      this.prominentMeasureLimit = prominentMeasureLimit;
   }

   public int getProminentDimensionLimit () {
      return prominentDimensionLimit;
   }

   public void setProminentDimensionLimit (int prominentDimensionLimit) {
      this.prominentDimensionLimit = prominentDimensionLimit;
   }

   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder();
      if (!StringUtils.isBlank(getName())) {
         sb.append(getName().trim());
         sb.append("~");
      }
      if (!StringUtils.isBlank(getDisplayName())) {
         sb.append(getDisplayName().trim());
         sb.append("~");
      }
      sb.append(getId());
      sb.append("~");
      sb.append(getBedId());
      return sb.toString();
   }

   @Override
   public boolean equals (Object inObj) {
      return this.toString().equals(inObj.toString());
   }

   @Override
   public int hashCode () {
      return this.toString().hashCode();
   }

   @Override
   public int compareTo (UIConcept inUIConcept) {
      return this.toString().compareTo(inUIConcept.toString());
   }

}
