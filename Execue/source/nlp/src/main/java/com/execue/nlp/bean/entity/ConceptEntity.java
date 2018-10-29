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

public class ConceptEntity extends TypeEntity implements Cloneable, Serializable {

   private static final long serialVersionUID = 1L;
   protected Long            conceptBedId;
   protected String          conceptDisplayName;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.TypeEntity#clone()
    */
   @Override
   public Object clone () throws CloneNotSupportedException {
      ConceptEntity conceptEntity = (ConceptEntity) super.clone();
      conceptEntity.setConceptBedId(this.getConceptBedId());
      conceptEntity.setConceptDisplayName(this.getConceptDisplayName());
      return conceptEntity;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.TypeEntity#toString()
    */
   @Override
   public String toString () {
      String superString = super.toString();
      if (conceptDisplayName != null) {
         String conceptString = superString + " " + conceptDisplayName;
         if (conceptBedId != null) {
            conceptString = conceptString + " " + conceptBedId;
         }
         return conceptString;
      } else {
         return superString;
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.TypeEntity#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof ConceptEntity || obj instanceof String)
               && this.toString().equalsIgnoreCase(obj.toString());

   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.TypeEntity#hashCode()
    */
   @Override
   public int hashCode () {
      return this.toString().hashCode();
   }

   /**
    * @return the conceptBedId
    */
   public Long getConceptBedId () {
      return conceptBedId;
   }

   /**
    * @param conceptBedId
    *           the conceptBedId to set
    */
   public void setConceptBedId (Long conceptBedId) {
      this.conceptBedId = conceptBedId;
   }

   /**
    * @return the conceptDisplayName
    */
   public String getConceptDisplayName () {
      return conceptDisplayName;
   }

   /**
    * @param conceptDisplayName
    *           the conceptDisplayName to set
    */
   public void setConceptDisplayName (String conceptDisplayName) {
      this.conceptDisplayName = conceptDisplayName;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.TypeEntity#getDisplayName()
    */
   @Override
   public String getDisplayName () {
      return conceptDisplayName;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.OntoEntity#getId()
    */
   @Override
   public Long getId () {
      return conceptBedId;
   }
}
