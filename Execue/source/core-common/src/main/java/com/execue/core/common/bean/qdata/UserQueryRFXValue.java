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

public class UserQueryRFXValue extends RFXValue {

   private static final long serialVersionUID = 1L;
   private Long              queryId;
   private Double            startValue;
   private Double            endValue;
   private Integer           tripleIdentifier;

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder(1);
      if (queryId != null) {
         sb.append(queryId);
      }
      if (startValue != null) {
         sb.append(startValue);
      }
      if (endValue != null) {
         sb.append(endValue);
      }
      if (tripleIdentifier != null) {
         sb.append(tripleIdentifier);
      }
      return sb.append(super.toString()).toString();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof UserQueryRFXValue || obj instanceof String)
               && this.toString().equalsIgnoreCase(obj.toString());
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
    * @return the queryId
    */
   public Long getQueryId () {
      return queryId;
   }

   /**
    * @param queryId the queryId to set
    */
   public void setQueryId (Long queryId) {
      this.queryId = queryId;
   }

   /**
    * @return the startValue
    */
   public Double getStartValue () {
      return startValue;
   }

   /**
    * @param startValue the startValue to set
    */
   public void setStartValue (Double startValue) {
      this.startValue = startValue;
   }

   /**
    * @return the endValue
    */
   public Double getEndValue () {
      return endValue;
   }

   /**
    * @param endValue the endValue to set
    */
   public void setEndValue (Double endValue) {
      this.endValue = endValue;
   }

   /**
    * @return the tripleIdentifier
    */
   public Integer getTripleIdentifier () {
      return tripleIdentifier;
   }

   /**
    * @param tripleIdentifier
    *           the tripleIdentifier to set
    */
   public void setTripleIdentifier (Integer tripleIdentifier) {
      this.tripleIdentifier = tripleIdentifier;
   }
}