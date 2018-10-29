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
public class ComparativeInfoNormalizedData extends AbstractNormalizedData implements Cloneable {

   NormalizedDataEntity limit;
   NormalizedDataEntity statistics;
   String               type;
   Long                 typeBeId;

   /**
    * @return the NormalizedDataType.UNIT_CLOUD_TYPE
    */
   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getNormalizedDataType()
    */
   public NormalizedDataType getNormalizedDataType () {
      return NormalizedDataType.COMPARATIVE_INFO_NORMALIZED_DATA;
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
      return ((obj instanceof ComparativeInfoNormalizedData) || (obj instanceof String))
               && this.toString().equals(obj.toString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#clone()
    */
   @Override
   public Object clone () throws CloneNotSupportedException {
      ComparativeInfoNormalizedData clonedNormalizedData = (ComparativeInfoNormalizedData) super.clone();
      clonedNormalizedData.setLimit(limit);
      clonedNormalizedData.setStatistics(statistics);
      clonedNormalizedData.setType(type);
      clonedNormalizedData.setTypeBeId(typeBeId);
      return clonedNormalizedData;
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
      return typeBeId;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getValue()
    */
   public String getValue () {
      return (statistics != null ? statistics.getValue() + " " : "") + (limit != null ? limit.getValue() + " " : "");

   }

   /**
    * @return the unitScale
    */
   public NormalizedDataEntity getStatistics () {
      return statistics;
   }

   /**
    * @param statistics
    *           the unitScale to set
    */
   public void setStatistics (NormalizedDataEntity unitscale) {
      this.statistics = unitscale;
   }

   /**
    * @return the number
    */
   public NormalizedDataEntity getLimit () {
      return limit;
   }

   /**
    * @param number
    *           the number to set
    */
   public void setLimit (NormalizedDataEntity limit) {
      this.limit = limit;
   }

   /**
    * @return the unitTypeBeId
    */
   public Long getTypeBeId () {
      return typeBeId;
   }

   /**
    * @param unitTypeBeId
    *           the unitTypeBeId to set
    */
   public void setTypeBeId (Long typeBeId) {
      this.typeBeId = typeBeId;
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
      return (statistics != null ? statistics.getDisplayValue() + " " : "")
               + (limit != null ? limit.getDisplayValue() + " " : "");
   }
}
