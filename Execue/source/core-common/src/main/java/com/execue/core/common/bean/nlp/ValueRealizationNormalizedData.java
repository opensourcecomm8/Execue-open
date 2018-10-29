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

import com.execue.core.common.bean.entity.NormalizedDataType;

/**
 * @author Nitesh
 */
public class ValueRealizationNormalizedData extends UnitNormalizedData {

   NormalizedDataEntity operator;
   NormalizedDataEntity valuePreposition;
   String               type;
   Long                 valueTypeBeId;

   /**
    * @return the NormalizedDataType.VALUE_CLOUD_TYPE
    */
   @Override
   public NormalizedDataType getNormalizedDataType () {
      return NormalizedDataType.VALUE_NORMALIZED_DATA;
   }

   /*
    * public void OPValueNormalizedData () { }
    */
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
      return (obj instanceof ValueRealizationNormalizedData || obj instanceof String)
               && this.toString().equals(obj.toString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#clone()
    */
   @Override
   public Object clone () throws CloneNotSupportedException {
      ValueRealizationNormalizedData clonedOPValueNormalizedData = (ValueRealizationNormalizedData) super.clone();
      clonedOPValueNormalizedData.setNumber(number);
      clonedOPValueNormalizedData.setOperator(operator);
      clonedOPValueNormalizedData.setValuePreposition(valuePreposition);
      clonedOPValueNormalizedData.setUnitScale(unitScale);
      clonedOPValueNormalizedData.setUnitSymbol(unitSymbol);
      clonedOPValueNormalizedData.setType(type);
      clonedOPValueNormalizedData.setValueTypeBeId(valueTypeBeId);

      return clonedOPValueNormalizedData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getType()
    */
   @Override
   public String getType () {
      return type;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getType()
    */
   @Override
   public Long getTypeBedId () {
      return valueTypeBeId;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getValue()
    */
   @Override
   public String getValue () {
      return (operator != null ? operator.getValue() + " " : "")
               + (valuePreposition != null ? valuePreposition.getValue() + " " : "") + super.getValue();
   }

   /**
    * @return the operator
    */
   public NormalizedDataEntity getOperator () {
      return operator;
   }

   /**
    * @param operator
    *           the operator to set
    */
   public void setOperator (NormalizedDataEntity operator) {
      this.operator = operator;
   }

   /**
    * @param type
    *           the type to set
    */
   @Override
   public void setType (String type) {
      this.type = type;
   }

   /**
    * @return the valueTypeBeId
    */
   public Long getValueTypeBeId () {
      return valueTypeBeId;
   }

   /**
    * @param valueTypeBeId
    *           the valueTypeBeId to set
    */
   public void setValueTypeBeId (Long valueTypeBeId) {
      this.valueTypeBeId = valueTypeBeId;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.UnitNormalizedData#getDisplayValue()
    */
   @Override
   public String getDisplayValue () {
      return (operator != null ? operator.getDisplayValue() + " " : "")
               + (valuePreposition != null ? valuePreposition.getDisplayValue() + " " : "") + super.getDisplayValue();
   }

   /**
    * @return the valuePreposition
    */
   public NormalizedDataEntity getValuePreposition () {
      return valuePreposition;
   }

   /**
    * @param valuePreposition
    *           the valuePreposition to set
    */
   public void setValuePreposition (NormalizedDataEntity valuePreposition) {
      this.valuePreposition = valuePreposition;
   }
}
