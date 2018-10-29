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


package com.execue.core.common.bean.nlp;

import com.execue.core.common.bean.AbstractNormalizedData;
import com.execue.core.common.bean.entity.NormalizedDataType;

/**
 * @author Nihar
 */
public class RelativeNormalizedData extends AbstractNormalizedData {

   NormalizedDataEntity adjective;
   NormalizedDataEntity number;
   NormalizedDataEntity conceptEntity;
   String               type;

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString () {
      return getValue();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof RelativeNormalizedData || obj instanceof String) && this.toString().equals(obj.toString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#clone()
    */
   @Override
   public Object clone () throws CloneNotSupportedException {
      RelativeNormalizedData clonedRelativeNormalizedData = (RelativeNormalizedData) super.clone();
      clonedRelativeNormalizedData.setAdjective((NormalizedDataEntity) adjective.clone());
      clonedRelativeNormalizedData.setNumber((NormalizedDataEntity) number.clone());
      clonedRelativeNormalizedData.setConceptEntity((NormalizedDataEntity) conceptEntity.clone());
      return clonedRelativeNormalizedData;
   }

   public String getDisplayValue () {
      if (number != null) {
         return adjective.getDisplayValue() + " " + number.getDisplayValue() + " " + conceptEntity.getDisplayValue();
      } else {
         return adjective.getDisplayValue() + " " + conceptEntity.getDisplayValue();
      }
   }

   /**
    * @return the adjective
    */
   public NormalizedDataEntity getAdjective () {
      return adjective;
   }

   /**
    * @param adjective
    *           the adjective to set
    */
   public void setAdjective (NormalizedDataEntity adjective) {
      this.adjective = adjective;
   }

   /**
    * @return the number
    */
   public NormalizedDataEntity getNumber () {
      return number;
   }

   /**
    * @param number
    *           the number to set
    */
   public void setNumber (NormalizedDataEntity number) {
      this.number = number;
   }

   public NormalizedDataType getNormalizedDataType () {
      return NormalizedDataType.RELATIVE_NORMALIZED_DATA;
   }

   public String getType () {
      return type;
   }

   public Long getTypeBedId () {
      return conceptEntity.getTypeBedId();
   }

   public String getValue () {
      if (number != null && adjective != null && conceptEntity != null) {
         return adjective.getValue() + " " + number.getValue() + " " + conceptEntity.getValue();
      } else if (adjective != null && conceptEntity != null) {
         return adjective.getValue() + " " + conceptEntity.getValue();
      }
      return null;
   }

   /**
    * @return the conceptEntity
    */
   public NormalizedDataEntity getConceptEntity () {
      return conceptEntity;
   }

   /**
    * @param conceptEntity
    *           the conceptEntity to set
    */
   public void setConceptEntity (NormalizedDataEntity conceptEntity) {
      this.conceptEntity = conceptEntity;
   }
}