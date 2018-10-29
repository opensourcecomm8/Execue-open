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


package com.execue.core.common.bean.security;

import java.io.Serializable;

/**
 * Object to store user's location information from the request origination
 * 
 * @author Raju Gottumukkala
 */
@SuppressWarnings ("serial")
public class UserRemoteLocationInfo implements Serializable {

   private String  countryCode;
   private String  countryName;
   private String  cityName;                                   // from remote location
   private String  stateName;                                  // from remote location
   private String  stateCode;                                  // from remote location
   private String  zipCode;                                    // Zip Code from remote location
   private String  ipAddress;                                  // from remote location
   private String  cityCenterZipCode;                          // Zip Code of the city center either by remote location
   private String  latitude;                                   // Latitude from remote location
   private String  longitude;                                  // Longitude from remote location
   // or user preference
   private boolean cityCenterZipCodeFromUserPreference = false;
   private String  preferredZipCode;                           // Zip Code that user Selected
   private String  preferredCityName;                          // City Name that user Selected
   private String  preferredStateName;                         // State Name that user Selected
   private String  preferredStateCode;                         // State Code that user Selected

   public String getCityName () {
      return cityName;
   }

   public void setCityName (String cityName) {
      this.cityName = cityName;
   }

   public String getStateName () {
      return stateName;
   }

   public void setStateName (String stateName) {
      this.stateName = stateName;
   }

   public String getStateCode () {
      return stateCode;
   }

   public void setStateCode (String stateCode) {
      this.stateCode = stateCode;
   }

   public String getZipCode () {
      return zipCode;
   }

   public void setZipCode (String zipCode) {
      this.zipCode = zipCode;
   }

   public String getIpAddress () {
      return ipAddress;
   }

   public void setIpAddress (String ipAddress) {
      this.ipAddress = ipAddress;
   }

   public String getCityCenterZipCode () {
      return cityCenterZipCode;
   }

   public void setCityCenterZipCode (String cityCenterZipCode) {
      this.cityCenterZipCode = cityCenterZipCode;
   }

   public boolean isCityCenterZipCodeFromUserPreference () {
      return cityCenterZipCodeFromUserPreference;
   }

   public void setCityCenterZipCodeFromUserPreference (boolean cityCenterZipCodeFromUserPreference) {
      this.cityCenterZipCodeFromUserPreference = cityCenterZipCodeFromUserPreference;
   }

   public String getPreferredZipCode () {
      return preferredZipCode;
   }

   public void setPreferredZipCode (String preferredZipCode) {
      this.preferredZipCode = preferredZipCode;
   }

   public String getPreferredCityName () {
      return preferredCityName;
   }

   public void setPreferredCityName (String preferredCityName) {
      this.preferredCityName = preferredCityName;
   }

   public String getPreferredStateName () {
      return preferredStateName;
   }

   public void setPreferredStateName (String preferredStateName) {
      this.preferredStateName = preferredStateName;
   }

   public String getPreferredStateCode () {
      return preferredStateCode;
   }

   public void setPreferredStateCode (String preferredStateCode) {
      this.preferredStateCode = preferredStateCode;
   }

   public String getLatitude () {
      return latitude;
   }

   public void setLatitude (String latitude) {
      this.latitude = latitude;
   }

   public String getLongitude () {
      return longitude;
   }

   public void setLongitude (String longitude) {
      this.longitude = longitude;
   }

   public String getCountryCode () {
      return countryCode;
   }

   public void setCountryCode (String countryCode) {
      this.countryCode = countryCode;
   }

   public String getCountryName () {
      return countryName;
   }

   public void setCountryName (String countryName) {
      this.countryName = countryName;
   }

}
