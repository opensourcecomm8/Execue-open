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

import java.util.Date;

public class UserQueryLocationInformation implements java.io.Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private Long              queryId;
   private Long              contextId;
   private Long              locationId;
   private Long              locationBedId;
   private Double            latitude;
   private Double            longitude;
   private Date              executionDate;
   private String            locationDisplayName;

   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder(1);
      sb.append(queryId).append(" ").append(contextId).append(" ").append(locationId).append(" ").append(latitude)
               .append(" ").append(longitude);
      return sb.toString();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof UserQueryLocationInformation || obj instanceof String)
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
    * @return the queryId
    */
   public Long getQueryId () {
      return queryId;
   }

   /**
    * @param queryId
    *           the queryId to set
    */
   public void setQueryId (Long queryId) {
      this.queryId = queryId;
   }

   /**
    * @return the contextId
    */
   public Long getContextId () {
      return contextId;
   }

   /**
    * @param contextId the contextId to set
    */
   public void setContextId (Long contextId) {
      this.contextId = contextId;
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
    * @return the executionDate
    */
   public Date getExecutionDate () {
      return executionDate;
   }

   /**
    * @param executionDate the executionDate to set
    */
   public void setExecutionDate (Date executionDate) {
      this.executionDate = executionDate;
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
}