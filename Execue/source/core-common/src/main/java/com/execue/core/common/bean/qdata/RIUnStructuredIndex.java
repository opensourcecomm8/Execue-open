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


/**
 *
 */
package com.execue.core.common.bean.qdata;

import java.util.Date;

/**
 * @author Nitesh
 */
public class RIUnStructuredIndex extends RIUniversalSearch {

   private static final long serialVersionUID = 1L;
   private Long              udxId;
   private Long              rfId;
   private Date              contentDate;

   @Override
   public String toString () {
      return super.toString();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof RIUnStructuredIndex || obj instanceof String)
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
    * @return the udxId
    */
   public Long getUdxId () {
      return udxId;
   }

   /**
    * @param udxId
    *           the udxId to set
    */
   public void setUdxId (Long udxId) {
      this.udxId = udxId;
   }

   @Override
   protected Long getContextId () {
      return udxId;
   }

   public Date getContentDate () {
      return contentDate;
   }

   public void setContentDate (Date contentDate) {
      this.contentDate = contentDate;
   }

   public static long getSerialVersionUID () {
      return serialVersionUID;
   }

   public Long getRfId () {
      return rfId;
   }

   public void setRfId (Long rfId) {
      this.rfId = rfId;
   }
}