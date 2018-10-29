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


package com.execue.core.common.bean.entity.unstructured;

import java.io.Serializable;

/**
 * @author Murthy
 */
public class ZipCodeDistance implements Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private String            srcZipCode;
   private String            destZipCode;
   private Double            distance;

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
    * @return the srcZipCode
    */
   public String getSrcZipCode () {
      return srcZipCode;
   }

   /**
    * @param srcZipCode
    *           the srcZipCode to set
    */
   public void setSrcZipCode (String srcZipCode) {
      this.srcZipCode = srcZipCode;
   }

   /**
    * @return the destZipCode
    */
   public String getDestZipCode () {
      return destZipCode;
   }

   /**
    * @param destZipCode
    *           the destZipCode to set
    */
   public void setDestZipCode (String destZipCode) {
      this.destZipCode = destZipCode;
   }

   /**
    * @return the distance
    */
   public Double getDistance () {
      return distance;
   }

   /**
    * @param distance
    *           the distance to set
    */
   public void setDistance (Double distance) {
      this.distance = distance;
   }

}
