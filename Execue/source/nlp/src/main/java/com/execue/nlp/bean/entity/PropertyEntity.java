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


package com.execue.nlp.bean.entity;

import java.io.Serializable;

public class PropertyEntity extends TypeEntity implements Cloneable, Serializable {

   private static final long serialVersionUID = 1L;
   protected Long            relationBedId;
   protected String          relationDisplayName;

   @Override
   public Object clone () throws CloneNotSupportedException {
      PropertyEntity propertyEntity = (PropertyEntity) super.clone();
      propertyEntity.setRelationBedId(this.getRelationBedId());
      propertyEntity.setRelationDisplayName(this.getRelationDisplayName());
      return propertyEntity;
   }

   /**
    * @return the relationBedId
    */
   public Long getRelationBedId () {
      return relationBedId;
   }

   /**
    * @param relationBedId
    *           the relationBedId to set
    */
   public void setRelationBedId (Long relationBedId) {
      this.relationBedId = relationBedId;
   }

   /**
    * @return the relationDisplayName
    */
   public String getRelationDisplayName () {
      return relationDisplayName;
   }

   /**
    * @param relationDisplayName
    *           the relationDisplayName to set
    */
   public void setRelationDisplayName (String relationDisplayName) {
      this.relationDisplayName = relationDisplayName;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.OntoEntity#getDisplayName()
    */
   @Override
   public String getDisplayName () {
      return relationDisplayName;
   }

   @Override
   public Long getId () {
      return relationBedId;
   }
}
