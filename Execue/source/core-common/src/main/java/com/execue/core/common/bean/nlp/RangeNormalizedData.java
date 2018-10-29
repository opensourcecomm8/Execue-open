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
import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.bean.entity.NormalizedDataType;

/**
 * @author Nitesh
 */
public class RangeNormalizedData extends AbstractNormalizedData implements INormalizedData {

   private NormalizedDataEntity start;
   private NormalizedDataEntity end;
   private String               type;
   private Long                 typeBedId;

   /**
    * @return the NormalizedDataType.RANGE_CLOUD_TYPE
    */
   public NormalizedDataType getNormalizedDataType () {
      return NormalizedDataType.RANGE_NORMALIZED_DATA;
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
      return ((obj instanceof RangeNormalizedData) || (obj instanceof String))
               && this.toString().equals(obj.toString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#clone()
    */
   @Override
   public Object clone () throws CloneNotSupportedException {
      RangeNormalizedData rangeNormalizedData = (RangeNormalizedData) super.clone();
      NormalizedDataEntity clonedStart = (NormalizedDataEntity) start.clone();
      rangeNormalizedData.setStart(clonedStart);
      NormalizedDataEntity clonedEnd = (NormalizedDataEntity) end.clone();
      rangeNormalizedData.setEnd(clonedEnd);
      rangeNormalizedData.setType(type);
      rangeNormalizedData.setTypeBedId(typeBedId);
      return rangeNormalizedData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getDisplayValue()
    */
   public String getDisplayValue () {
      return (start != null ? start.getDisplayValue() : "") + " -- " + (end != null ? end.getDisplayValue() : "");
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
    * @see com.execue.core.common.bean.INormalizedData#getValue()
    */
   public String getValue () {
      return (start != null ? start.getValue() : "") + " -- " + (end != null ? end.getValue() : "");
   }

   /**
    * @return the start
    */
   public NormalizedDataEntity getStart () {
      return start;
   }

   /**
    * @param start
    *           the start to set
    */
   public void setStart (NormalizedDataEntity start) {
      this.start = start;
   }

   /**
    * @return the end
    */
   public NormalizedDataEntity getEnd () {
      return end;
   }

   /**
    * @param end
    *           the end to set
    */
   public void setEnd (NormalizedDataEntity end) {
      this.end = end;
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
    * @param type
    *           the type to set
    */
   public void setType (String type) {
      this.type = type;
   }
}
