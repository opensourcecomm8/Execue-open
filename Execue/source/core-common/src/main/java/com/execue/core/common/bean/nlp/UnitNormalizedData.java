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
 * @author Nitesh
 */
public class UnitNormalizedData extends AbstractNormalizedData implements Cloneable {

   NormalizedDataEntity unitSymbol;
   NormalizedDataEntity unitScale;
   NormalizedDataEntity number;
   String               type;
   Long                 unitTypeBeId;

   /**
    * @return the NormalizedDataType.UNIT_CLOUD_TYPE
    */
   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getNormalizedDataType()
    */
   public NormalizedDataType getNormalizedDataType () {
      return NormalizedDataType.UNIT_NORMALIZED_DATA;
   }

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
      return ((obj instanceof UnitNormalizedData) || (obj instanceof String)) && this.toString().equals(obj.toString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#clone()
    */
   @Override
   public Object clone () throws CloneNotSupportedException {
      UnitNormalizedData clonedOPValueNormalizedData = (UnitNormalizedData) super.clone();
      clonedOPValueNormalizedData.setNumber(number);
      clonedOPValueNormalizedData.setUnitScale(unitScale);
      clonedOPValueNormalizedData.setUnitSymbol(unitSymbol);
      clonedOPValueNormalizedData.setType(type);
      clonedOPValueNormalizedData.setUnitTypeBeId(unitTypeBeId);
      return clonedOPValueNormalizedData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getType()
    */
   public String getType () {
      return type;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getType()
    */
   public Long getTypeBedId () {
      return unitTypeBeId;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getValue()
    */
   public String getValue () {
      return (number != null ? number.getValue() + " " : "") + (unitScale != null ? unitScale.getValue() + " " : "")
               + (unitSymbol != null ? unitSymbol.getValue() + " " : "");

   }

   /**
    * @return the unitSymbol
    */
   public NormalizedDataEntity getUnitSymbol () {
      return unitSymbol;
   }

   /**
    * @param unitSymbol
    *           the unitSymbol to set
    */
   public void setUnitSymbol (NormalizedDataEntity unitSymbol) {
      this.unitSymbol = unitSymbol;
   }

   /**
    * @return the unitScale
    */
   public NormalizedDataEntity getUnitScale () {
      return unitScale;
   }

   /**
    * @param unitScale
    *           the unitScale to set
    */
   public void setUnitScale (NormalizedDataEntity unitscale) {
      this.unitScale = unitscale;
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

   /**
    * @return the unitTypeBeId
    */
   public Long getUnitTypeBeId () {
      return unitTypeBeId;
   }

   /**
    * @param unitTypeBeId
    *           the unitTypeBeId to set
    */
   public void setUnitTypeBeId (Long unitTypeBeId) {
      this.unitTypeBeId = unitTypeBeId;
   }

   /**
    * @param type
    *           the type to set
    */
   public void setType (String type) {
      this.type = type;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getDisplayValue()
    */
   public String getDisplayValue () {
      return (number != null ? number.getDisplayValue() + " " : "")
               + (unitScale != null ? unitScale.getDisplayValue() + " " : "")
               + (unitSymbol != null ? unitSymbol.getDisplayValue() + " " : "");
   }
}
