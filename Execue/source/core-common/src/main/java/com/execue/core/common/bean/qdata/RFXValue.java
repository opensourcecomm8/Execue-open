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


package com.execue.core.common.bean.qdata;

import java.io.Serializable;

public abstract class RFXValue implements Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private Long              srcConceptBedId;
   private Long              relationBedId;
   private Long              destConceptBedId;
   private String            operator;
   private Double            value;

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder(1);
      if (srcConceptBedId != null) {
         sb.append(srcConceptBedId);
      }
      if (relationBedId != null) {
         sb.append(relationBedId);
      }
      if (destConceptBedId != null) {
         sb.append(destConceptBedId);
      }
      if (operator != null) {
         sb.append(operator);
      }
      if (value != null) {
         sb.append(value);
      }
      return sb.toString();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof RFXValue || obj instanceof String) && this.toString().equalsIgnoreCase(obj.toString());
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
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the srcConceptBedId
    */
   public Long getSrcConceptBedId () {
      return srcConceptBedId;
   }

   /**
    * @param srcConceptBedId
    *           the srcConceptBedId to set
    */
   public void setSrcConceptBedId (Long srcConceptBedId) {
      this.srcConceptBedId = srcConceptBedId;
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
    * @return the destConceptBedId
    */
   public Long getDestConceptBedId () {
      return destConceptBedId;
   }

   /**
    * @param destConceptBedId
    *           the destConceptBedId to set
    */
   public void setDestConceptBedId (Long destConceptBedId) {
      this.destConceptBedId = destConceptBedId;
   }

   /**
    * @return the operator
    */
   public String getOperator () {
      return operator;
   }

   /**
    * @param operator
    *           the operator to set
    */
   public void setOperator (String operator) {
      this.operator = operator;
   }

   /**
    * @return the value
    */
   public Double getValue () {
      return value;
   }

   /**
    * @param value
    *           the value to set
    */
   public void setValue (Double value) {
      this.value = value;
   }
}