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

public class TypeEntity extends OntoEntity implements Cloneable, Serializable {

   private static final long serialVersionUID = 1L;
   protected Long            typeBedId;
   protected String          typeDisplayName;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.OntoEntity#toString()
    */
   @Override
   public String toString () {
      String superString = super.toString();
      if (typeDisplayName != null) {
         return superString + " " + typeDisplayName;
      } else {
         return superString;
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.OntoEntity#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof TypeEntity || obj instanceof String) && this.toString().equalsIgnoreCase(obj.toString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.OntoEntity#hashCode()
    */
   @Override
   public int hashCode () {
      return this.toString().hashCode();
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.OntoEntity#clone()
    */
   @Override
   public Object clone () throws CloneNotSupportedException {
      TypeEntity typeEntity = (TypeEntity) super.clone();
      typeEntity.setTypeDisplayName(this.getTypeDisplayName());
      return typeEntity;
   }

   /**
    * @return the typeBedId
    */
   public Long getTypeBedId () {
      return typeBedId;
   }

   /**
    * @param typeBedId
    *           the typeBedId to set
    */
   public void setTypeBedId (Long typeBedId) {
      this.typeBedId = typeBedId;
   }

   /**
    * @return the typeDisplayName
    */
   public String getTypeDisplayName () {
      return typeDisplayName;
   }

   /**
    * @param typeDisplayName
    *           the typeDisplayName to set
    */
   public void setTypeDisplayName (String typeDisplayName) {
      this.typeDisplayName = typeDisplayName;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.OntoEntity#getDisplayName()
    */
   @Override
   public String getDisplayName () {
      return typeDisplayName;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.OntoEntity#getId()
    */
   @Override
   public Long getId () {
      return typeBedId;
   }
}