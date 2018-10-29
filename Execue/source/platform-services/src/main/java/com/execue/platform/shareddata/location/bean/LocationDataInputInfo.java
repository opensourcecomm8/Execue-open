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

/**
 * @author Vishay
 */
public class LocationDataInputInfo {

   private String zipCode;
   private String countryName;
   private String stateName;
   private String cityName;
   private Double latitude;
   private Double longitude;

   public String getZipCode () {
      return zipCode;
   }

   public void setZipCode (String zipCode) {
      this.zipCode = zipCode;
   }

   public String getCountryName () {
      return countryName;
   }

   public void setCountryName (String countryName) {
      this.countryName = countryName;
   }

   public String getStateName () {
      return stateName;
   }

   public void setStateName (String stateName) {
      this.stateName = stateName;
   }

   public String getCityName () {
      return cityName;
   }

   public void setCityName (String cityName) {
      this.cityName = cityName;
   }

   public Double getLatitude () {
      return latitude;
   }

   public void setLatitude (Double latitude) {
      this.latitude = latitude;
   }

   public Double getLongitude () {
      return longitude;
   }

   public void setLongitude (Double longitude) {
      this.longitude = longitude;
   }
   
   public String getPrintString () {
      StringBuilder sb = new StringBuilder();
      
      sb.append("Zip : ");
      sb.append(getZipCode());
      sb.append(", ");
      
      sb.append("City : ");
      sb.append(getCityName());
      sb.append(", ");
      
      sb.append("State : ");
      sb.append(getStateName());
      sb.append(", ");
      
      sb.append("Country : ");
      sb.append(getCountryName());
      sb.append(", ");
      
      sb.append("Latitude : ");
      sb.append(getLatitude());
      sb.append(", ");
      
      sb.append("Longitude : ");
      sb.append(getLongitude());
      
      return sb.toString();
   }

}
