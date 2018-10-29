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
import java.util.Date;

import com.execue.core.common.type.ProcessingFlagType;

public class SemantifiedContent implements Serializable {

   private Long               id;
   private String             contentSource;                                       // Article Posted location information
   private Long               contextId;                                           // Application Id
   private Long               articleRefId;                                        // Id from Source Content refering to this article
   private String             url;
   private String             imageUrl;
   private String             shortDescription;
   private String             longDescription;
   private Date               contentDate;
   private Date               createdDate;
   private ProcessingFlagType imageUrlProcessed = ProcessingFlagType.NOT_PROCESSED;
   private Long               batchId;
   private ProcessingFlagType processingState   = ProcessingFlagType.NOT_PROCESSED;
   private Long               userQueryId;
   private Long               locationId;
   private String             locationDisplayName;
   private Double             latitude;
   private Double             longitude;

   public Long getId () {
      return id;
   }

   /**
    * @return the processingState
    */
   public ProcessingFlagType getProcessingState () {
      return processingState;
   }

   /**
    * @param processingState the processingState to set
    */
   public void setProcessingState (ProcessingFlagType processingState) {
      this.processingState = processingState;
   }

   /**
    * @return the userQueryId
    */
   public Long getUserQueryId () {
      return userQueryId;
   }

   /**
    * @param userQueryId the userQueryId to set
    */
   public void setUserQueryId (Long userQueryId) {
      this.userQueryId = userQueryId;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getContentSource () {
      return contentSource;
   }

   public void setContentSource (String contentSource) {
      this.contentSource = contentSource;
   }

   public Long getContextId () {
      return contextId;
   }

   public void setContextId (Long contextId) {
      this.contextId = contextId;
   }

   public Long getArticleRefId () {
      return articleRefId;
   }

   public void setArticleRefId (Long articleRefId) {
      this.articleRefId = articleRefId;
   }

   public String getUrl () {
      return url;
   }

   public void setUrl (String url) {
      this.url = url;
   }

   public String getImageUrl () {
      return imageUrl;
   }

   public void setImageUrl (String imageUrl) {
      this.imageUrl = imageUrl;
   }

   public String getShortDescription () {
      return shortDescription;
   }

   public void setShortDescription (String shortDescription) {
      this.shortDescription = shortDescription;
   }

   public String getLongDescription () {
      return longDescription;
   }

   public void setLongDescription (String longDescription) {
      this.longDescription = longDescription;
   }

   public Date getContentDate () {
      return contentDate;
   }

   public void setContentDate (Date contentDate) {
      this.contentDate = contentDate;
   }

   public Date getCreatedDate () {
      return createdDate;
   }

   public void setCreatedDate (Date createdDate) {
      this.createdDate = createdDate;
   }

   public ProcessingFlagType getImageUrlProcessed () {
      return imageUrlProcessed;
   }

   public void setImageUrlProcessed (ProcessingFlagType imageUrlProcessed) {
      this.imageUrlProcessed = imageUrlProcessed;
   }

   public Long getBatchId () {
      return batchId;
   }

   public void setBatchId (Long batchId) {
      this.batchId = batchId;
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

}
