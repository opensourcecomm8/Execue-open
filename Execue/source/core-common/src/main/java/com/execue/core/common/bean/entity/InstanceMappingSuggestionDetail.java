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

public class InstanceMappingSuggestionDetail implements Serializable {

   private Long      id;

   private Long      instanceMappingSuggestionId;

   private Long      membrAEDId;

   private String    membrDisplayName;

   private Long      instanceId;

   private Long      instanceBEDId;

   private String    instanceDisplayName;

   private String    instanceDescription;

   private CheckType isInstanceExists;

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
    * @return the instanceMappingSuggestionId
    */
   public Long getInstanceMappingSuggestionId () {
      return instanceMappingSuggestionId;
   }

   /**
    * @param instanceMappingSuggestionId
    *           the instanceMappingSuggestionId to set
    */
   public void setInstanceMappingSuggestionId (Long instanceMappingSuggestionId) {
      this.instanceMappingSuggestionId = instanceMappingSuggestionId;
   }

   /**
    * @return the membrAEDId
    */
   public Long getMembrAEDId () {
      return membrAEDId;
   }

   /**
    * @param membrAEDId
    *           the membrAEDId to set
    */
   public void setMembrAEDId (Long membrAEDId) {
      this.membrAEDId = membrAEDId;
   }

   /**
    * @return the membrDisplayName
    */
   public String getMembrDisplayName () {
      return membrDisplayName;
   }

   /**
    * @param membrDisplayName
    *           the membrDisplayName to set
    */
   public void setMembrDisplayName (String membrDisplayName) {
      this.membrDisplayName = membrDisplayName;
   }

   /**
    * @return the instanceId
    */
   public Long getInstanceId () {
      return instanceId;
   }

   /**
    * @param instanceId
    *           the instanceId to set
    */
   public void setInstanceId (Long instanceId) {
      this.instanceId = instanceId;
   }

   /**
    * @return the instanceBEDId
    */
   public Long getInstanceBEDId () {
      return instanceBEDId;
   }

   /**
    * @param instanceBEDId
    *           the instanceBEDId to set
    */
   public void setInstanceBEDId (Long instanceBEDId) {
      this.instanceBEDId = instanceBEDId;
   }

   /**
    * @return the instanceDisplayName
    */
   public String getInstanceDisplayName () {
      return instanceDisplayName;
   }

   /**
    * @param instanceDisplayName
    *           the instanceDisplayName to set
    */
   public void setInstanceDisplayName (String instanceDisplayName) {
      this.instanceDisplayName = instanceDisplayName;
   }

   /**
    * @return the instanceDescription
    */
   public String getInstanceDescription () {
      return instanceDescription;
   }

   /**
    * @param instanceDescription
    *           the instanceDescription to set
    */
   public void setInstanceDescription (String instanceDescription) {
      this.instanceDescription = instanceDescription;
   }

   /**
    * @return the isInstanceExists
    */
   public CheckType getIsInstanceExists () {
      return isInstanceExists;
   }

   /**
    * @param isInstanceExists
    *           the isInstanceExists to set
    */
   public void setIsInstanceExists (CheckType isInstanceExists) {
      this.isInstanceExists = isInstanceExists;
   }

}
