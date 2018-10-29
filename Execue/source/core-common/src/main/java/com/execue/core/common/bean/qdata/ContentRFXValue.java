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

public class ContentRFXValue extends RFXValue {

   private static final long serialVersionUID = 1L;
   private Long              rfxId;

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder(1);
      if (rfxId != null) {
         sb.append(rfxId);
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
      return (obj instanceof ContentRFXValue || obj instanceof String)
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
    * @return the rfxId
    */
   public Long getRfxId () {
      return rfxId;
   }

   /**
    * @param rfxId
    *           the rfxId to set
    */
   public void setRfxId (Long rfxId) {
      this.rfxId = rfxId;
   }
}