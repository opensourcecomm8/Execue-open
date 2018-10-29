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
public class DefaultNormalizedData extends AbstractNormalizedData {

   private String value;
   private String displayValue;
   private String type;
   private Long   typeBedId;

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
      return (obj instanceof DefaultNormalizedData || obj instanceof String) && this.toString().equals(obj.toString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#clone()
    */
   @Override
   public Object clone () throws CloneNotSupportedException {
      DefaultNormalizedData normalizedData = (DefaultNormalizedData) super.clone();
      normalizedData.setType(type);
      normalizedData.setTypeBedId(typeBedId);
      normalizedData.setValue(value);
      normalizedData.setDisplayValue(displayValue);
      return normalizedData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getNormalizedDataType()
    */
   public NormalizedDataType getNormalizedDataType () {
      return NormalizedDataType.DEFAULT_NORMALIZED_DATA;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getValue()
    */
   public String getValue () {
      return value;
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
    * @see com.execue.core.common.bean.INormalizedData#getTypeBedId()
    */
   public Long getTypeBedId () {
      return typeBedId;
   }

   /**
    * @param value
    *           the value to set
    */
   public void setValue (String value) {
      this.value = value;
   }

   public String getDisplayValue () {
      return displayValue;
   }

   /**
    * @param type
    *           the type to set
    */
   public void setType (String type) {
      this.type = type;
   }

   /**
    * @param typeBedId
    *           the typeBedId to set
    */
   public void setTypeBedId (Long typeBedId) {
      this.typeBedId = typeBedId;
   }

   /**
    * @param displayValue the displayValue to set
    */
   public void setDisplayValue (String displayValue) {
      this.displayValue = displayValue;
   }
}