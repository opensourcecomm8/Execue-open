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


package com.execue.core.common.bean.entity.audittrail;

import java.io.Serializable;

public class AnonymousUser implements Serializable {

   private Long   id;
   private String countryCode;
   private String countryName;
   private String cityName;
   private String stateName;
   private String stateCode;
   private String zipCode;
   private String ipLocation;
   private String latitude;
   private String longitude;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
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

   public String getIpLocation () {
      return ipLocation;
   }

   public void setIpLocation (String ipLocation) {
      this.ipLocation = ipLocation;
   }

}
