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

import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.bean.WeightInformation;

/**
 * @author Nitesh
 */
public class NormalizedDataEntity implements Cloneable {

   private String            value;
   private String            displayValue;
   private Long              typeBedId;
   private Long              valueBedId;
   private Long              valueKnowledgeId;
   private INormalizedData   normalizedData;
   private WeightInformation weightInformation;
   private String            displaySymbol;

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder();
      sb.append(value).append(" ").append(displayValue).append(" ").append(typeBedId).append(" ");
      if (valueBedId != null) {
         sb.append(valueBedId);
      }
      return sb.toString();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#clone()
    */
   @Override
   public Object clone () throws CloneNotSupportedException {
      NormalizedDataEntity clonedNormalizedDataEntity = (NormalizedDataEntity) super.clone();
      clonedNormalizedDataEntity.setValue(value);
      clonedNormalizedDataEntity.setValueBedId(valueBedId);
      clonedNormalizedDataEntity.setValueKnowledgeId(valueKnowledgeId);
      clonedNormalizedDataEntity.setDisplayValue(displayValue);
      clonedNormalizedDataEntity.setTypeBedId(typeBedId);
      clonedNormalizedDataEntity.setDisplaySymbol(displaySymbol);
      if (normalizedData != null) {
         clonedNormalizedDataEntity.setNormalizedData((INormalizedData) normalizedData.clone());
      }
      if (weightInformation != null) {
         clonedNormalizedDataEntity.setWeightInformation(weightInformation.clone());
      }
      return clonedNormalizedDataEntity;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof NormalizedDataEntity || obj instanceof String) && this.toString().equals(obj.toString());
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

   /**
    * @return the normalizedData
    */
   public INormalizedData getNormalizedData () {
      return normalizedData;
   }

   /**
    * @param normalizedData
    *           the normalizedData to set
    */
   public void setNormalizedData (INormalizedData normalizedData) {
      this.normalizedData = normalizedData;
   }

   /**
    * @return the value
    */
   public String getValue () {
      return value;
   }

   /**
    * @param value
    *           the value to set
    */
   public void setValue (String value) {
      this.value = value;
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
    * @return the valueBedId
    */
   public Long getValueBedId () {
      return valueBedId;
   }

   /**
    * @param valueBedId
    *           the valueBedId to set
    */
   public void setValueBedId (Long valueBedId) {
      this.valueBedId = valueBedId;
   }

   /**
    * @return the displayValue
    */
   public String getDisplayValue () {
      return displayValue;
   }

   /**
    * @param displayValue
    *           the displayValue to set
    */
   public void setDisplayValue (String displayValue) {
      this.displayValue = displayValue;
   }

   /**
    * @return the valueKnowledgeId
    */
   public Long getValueKnowledgeId () {
      return valueKnowledgeId;
   }

   /**
    * @param valueKnowledgeId
    *           the valueKnowledgeId to set
    */
   public void setValueKnowledgeId (Long valueKnowledgeId) {
      this.valueKnowledgeId = valueKnowledgeId;
   }

   /**
    * @return the weightInformation
    */
   public WeightInformation getWeightInformation () {
      return weightInformation;
   }

   /**
    * @param weightInformation
    *           the weightInformation to set
    */
   public void setWeightInformation (WeightInformation weightInformation) {
      this.weightInformation = weightInformation;
   }

   /**
    * @return the displaySymbol
    */
   public String getDisplaySymbol () {
      return displaySymbol;
   }

   /**
    * @param displaySymbol
    *           the displaySymbol to set
    */
   public void setDisplaySymbol (String displaySymbol) {
      this.displaySymbol = displaySymbol;
   }
}
