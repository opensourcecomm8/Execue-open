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

import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;

/**
 * Object for representing the entity-relation-entity association
 * 
 * @author John Mallavalli
 */
public class UIAttribute {

   private String             attributeName;
   private Long               attributeTypeId;
   private Long               attributeBedId;
   private Long               relationBedId;
   private String             relationName;
   private CheckType          isRequired;
   private CheckType          multipleRealizations;
   private BusinessEntityType entityType;

   public String getRelationName () {
      return relationName;
   }

   public void setRelationName (String relationName) {
      this.relationName = relationName;
   }

   public void setEntityType (BusinessEntityType entityType) {
      this.entityType = entityType;
   }

   public CheckType getIsRequired () {
      return isRequired;
   }

   public void setIsRequired (CheckType isRequired) {
      this.isRequired = isRequired;
   }

   public String getAttributeName () {
      return attributeName;
   }

   public void setAttributeName (String attributeName) {
      this.attributeName = attributeName;
   }

   public Long getAttributeTypeId () {
      return attributeTypeId;
   }

   public void setAttributeTypeId (Long attributeTypeId) {
      this.attributeTypeId = attributeTypeId;
   }

   public Long getAttributeBedId () {
      return attributeBedId;
   }

   public void setAttributeBedId (Long attributeBedId) {
      this.attributeBedId = attributeBedId;
   }

   public BusinessEntityType getEntityType () {
      return entityType;
   }

   public Long getRelationBedId () {
      return relationBedId;
   }

   public void setRelationBedId (Long relationBedId) {
      this.relationBedId = relationBedId;
   }

   public CheckType getMultipleRealizations () {
      return multipleRealizations;
   }

   public void setMultipleRealizations (CheckType multipleRealizations) {
      this.multipleRealizations = multipleRealizations;
   }

}
