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


package com.execue.swi.bean;

public class BusinessEntityDefinitionMaintenanceInfo {

   private Long   conceptBedId;
   private Long   conceptId;
   private Long   oldTypeBedId;
   private String oldTypeName;
   private String newConceptName;
   private Long   newTypeBedId;
   private String newTypeName;

   public Long getConceptBedId () {
      return conceptBedId;
   }

   public void setConceptBedId (Long conceptBedId) {
      this.conceptBedId = conceptBedId;
   }

   public Long getConceptId () {
      return conceptId;
   }

   public void setConceptId (Long conceptId) {
      this.conceptId = conceptId;
   }

   public Long getOldTypeBedId () {
      return oldTypeBedId;
   }

   public void setOldTypeBedId (Long oldTypeBedId) {
      this.oldTypeBedId = oldTypeBedId;
   }

   public Long getNewTypeBedId () {
      return newTypeBedId;
   }

   public void setNewTypeBedId (Long newTypeBedId) {
      this.newTypeBedId = newTypeBedId;
   }

   public String getOldTypeName () {
      return oldTypeName;
   }

   public void setOldTypeName (String oldTypeName) {
      this.oldTypeName = oldTypeName;
   }

   public String getNewTypeName () {
      return newTypeName;
   }

   public void setNewTypeName (String newTypeName) {
      this.newTypeName = newTypeName;
   }

   
   public String getNewConceptName () {
      return newConceptName;
   }

   
   public void setNewConceptName (String newConceptName) {
      this.newConceptName = newConceptName;
   }

}
