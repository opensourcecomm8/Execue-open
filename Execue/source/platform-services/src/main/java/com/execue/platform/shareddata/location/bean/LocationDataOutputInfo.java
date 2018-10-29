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


package com.execue.platform.shareddata.location.bean;

import com.execue.core.common.type.LocationType;

/**
 * @author Vishay
 */
public class LocationDataOutputInfo {

   private LocationType locationType;
   private Long         countryBedId;
   private Long         stateBedId;
   private Long         cityBedId;

   public LocationDataOutputInfo (LocationType locationType, Long countryBedId, Long stateBedId, Long cityBedId) {
      super();
      this.locationType = locationType;
      this.countryBedId = countryBedId;
      this.stateBedId = stateBedId;
      this.cityBedId = cityBedId;
   }

   public Long getCountryBedId () {
      return countryBedId;
   }

   public void setCountryBedId (Long countryBedId) {
      this.countryBedId = countryBedId;
   }

   public Long getStateBedId () {
      return stateBedId;
   }

   public void setStateBedId (Long stateBedId) {
      this.stateBedId = stateBedId;
   }

   public Long getCityBedId () {
      return cityBedId;
   }

   public void setCityBedId (Long cityBedId) {
      this.cityBedId = cityBedId;
   }

   /**
    * @return the locationType
    */
   public LocationType getLocationType () {
      return locationType;
   }

   /**
    * @param locationType the locationType to set
    */
   public void setLocationType (LocationType locationType) {
      this.locationType = locationType;
   }
}
