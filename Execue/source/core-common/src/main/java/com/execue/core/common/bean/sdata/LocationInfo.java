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


package com.execue.core.common.bean.sdata;

/**
 * @author john
 */
public class LocationInfo {

   private Long countryId;
   private Long stateId;
   private Long cityId;

   public Long getCountryId () {
      return countryId;
   }

   public void setCountryId (Long countryId) {
      this.countryId = countryId;
   }

   public Long getStateId () {
      return stateId;
   }

   public void setStateId (Long stateId) {
      this.stateId = stateId;
   }

   public Long getCityId () {
      return cityId;
   }

   public void setCityId (Long cityId) {
      this.cityId = cityId;
   }
}
