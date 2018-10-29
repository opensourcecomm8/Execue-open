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


package com.execue.core.common.bean.entity;

import java.io.Serializable;

public class CountryLookup implements Serializable {

   private Long   id;
   private String countryCode;
   private String description;

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
    * @return the countryCode
    */
   public String getCountryCode () {
      return countryCode;
   }

   /**
    * @param countryCode
    *           the countryCode to set
    */
   public void setCountryCode (String countryCode) {
      this.countryCode = countryCode;
   }

   /**
    * @return the description
    */
   public String getDescription () {
      return description;
   }

   /**
    * @param description
    *           the description to set
    */
   public void setDescription (String description) {
      this.description = description;
   }

}
