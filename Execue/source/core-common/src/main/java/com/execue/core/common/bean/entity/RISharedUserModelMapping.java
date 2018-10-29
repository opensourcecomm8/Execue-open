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
public class RISharedUserModelMapping implements Serializable {

   private Long id;
   private Long baseInstanceBedId;
   private Long modelGroupId;
   private Long instanceBedId;
   private Long conceptBedId;

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   public Long getBaseInstanceBedId () {
      return baseInstanceBedId;
   }

   public void setBaseInstanceBedId (Long baseInstanceBedId) {
      this.baseInstanceBedId = baseInstanceBedId;
   }

   public Long getModelGroupId () {
      return modelGroupId;
   }

   public void setModelGroupId (Long modelGroupId) {
      this.modelGroupId = modelGroupId;
   }

   public Long getInstanceBedId () {
      return instanceBedId;
   }

   public void setInstanceBedId (Long instanceBedId) {
      this.instanceBedId = instanceBedId;
   }

   public Long getConceptBedId () {
      return conceptBedId;
   }

   public void setConceptBedId (Long conceptBedId) {
      this.conceptBedId = conceptBedId;
   }
}
