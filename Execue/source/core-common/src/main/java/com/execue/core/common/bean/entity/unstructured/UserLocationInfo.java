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

public class UserLocationInfo implements Serializable {

   private Long   locationId;
   private Long   locationBedId;
   private Double latitude;
   private Double longitude;
   private String locationDisplayName;

   /**
    * @return the locationBedId
    */
   public Long getLocationBedId () {
      return locationBedId;
   }

   /**
    * @param locationBedId the locationBedId to set
    */
   public void setLocationBedId (Long locationBedId) {
      this.locationBedId = locationBedId;
   }

   /**
    * @return the latitude
    */
   public Double getLatitude () {
      return latitude;
   }

   /**
    * @param latitude the latitude to set
    */
   public void setLatitude (Double latitude) {
      this.latitude = latitude;
   }

   /**
    * @return the longitude
    */
   public Double getLongitude () {
      return longitude;
   }

   /**
    * @param longitude the longitude to set
    */
   public void setLongitude (Double longitude) {
      this.longitude = longitude;
   }

   /**
    * @return the locationId
    */
   public Long getLocationId () {
      return locationId;
   }

   /**
    * @param locationId the locationId to set
    */
   public void setLocationId (Long locationId) {
      this.locationId = locationId;
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

}
