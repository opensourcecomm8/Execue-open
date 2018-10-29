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


package com.execue.core.common.bean.sdata.location;

import java.io.Serializable;

import com.execue.core.common.type.NormalizedLocationType;

/**
 * @author Prasanna
 */
public class LocationPointInfo implements Serializable {

   private Long                   id;
   private String                 zipCode;
   private Long                   locationBedId;
   private NormalizedLocationType normalizedLocationType;
   private Double                 longitude;
   private Double                 latitude;
   private String                 location;
   private String                 locationDisplayName;

   public String getZipCode () {
      return zipCode;
   }

   public void setZipCode (String zipCode) {
      this.zipCode = zipCode;
   }

   public Long getLocationBedId () {
      return locationBedId;
   }

   public void setLocationBedId (Long locationBedId) {
      this.locationBedId = locationBedId;
   }

   public Double getLongitude () {
      return longitude;
   }

   public void setLongitude (Double longitude) {
      this.longitude = longitude;
   }

   public Double getLatitude () {
      return latitude;
   }

   public void setLatitude (Double latitude) {
      this.latitude = latitude;
   }

   public String getLocation () {
      return location;
   }

   public void setLocation (String location) {
      this.location = location;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the locationDisplayName
    */
   public String getLocationDisplayName () {
      return locationDisplayName;
   }

   /**
    * @param locationDisplayName the locationDisplayName to set
    */
   public void setLocationDisplayName (String locationDisplayName) {
      this.locationDisplayName = locationDisplayName;
   }

   /**
    * @return the normalizedLocationType
    */
   public NormalizedLocationType getNormalizedLocationType () {
      return normalizedLocationType;
   }

   /**
    * @param normalizedLocationType the normalizedLocationType to set
    */
   public void setNormalizedLocationType (NormalizedLocationType normalizedLocationType) {
      this.normalizedLocationType = normalizedLocationType;
   }

}
