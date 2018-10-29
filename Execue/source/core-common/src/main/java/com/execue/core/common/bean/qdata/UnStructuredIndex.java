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


package com.execue.core.common.bean.qdata;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.execue.core.common.type.ProcessingFlagType;

public class UnStructuredIndex implements Serializable {

   private static final long  serialVersionUID  = 1L;
   private Long               id;
   private Long               rfId;
   private String             url;
   private String             shortDescription;
   private String             longDescription;
   private String             contentSource;                                       // DBPedia, Wiki Pedia, New York
   // Times, YAHOO etc
   private String             contentSourceType;                                   // NEWS LINKS, RSS FEEDS, CRAWLING,
   // EXTERNAL META etc
   private Double             entityCount;
   private Double             maxMatchWeight;
   private Date               createdDate;
   private Set<UDXAttribute>  udxAttributes;
   private Date               contentDate;
   private String             imageUrl;
   private ProcessingFlagType imageUrlProcessed = ProcessingFlagType.NOT_PROCESSED;
   private Long               batchId;
   private Long               articleRefId;

   /**
    * @return the articleRefId
    */
   public Long getArticleRefId () {
      return articleRefId;
   }

   /**
    * @param articleRefId the articleRefId to set
    */
   public void setArticleRefId (Long articleRefId) {
      this.articleRefId = articleRefId;
   }

   /**
    * @return the batchId
    */
   public Long getBatchId () {
      return batchId;
   }

   /**
    * @param batchId
    *           the batchId to set
    */
   public void setBatchId (Long batchId) {
      this.batchId = batchId;
   }

   /**
    * @return the imageUrlProcessed
    */
   public ProcessingFlagType getImageUrlProcessed () {
      return imageUrlProcessed;
   }

   /**
    * @param imageUrlProcessed
    *           the imageUrlProcessed to set
    */
   public void setImageUrlProcessed (ProcessingFlagType imageUrlProcessed) {
      this.imageUrlProcessed = imageUrlProcessed;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getUrl () {
      return url;
   }

   public void setUrl (String url) {
      this.url = url;
   }

   public Long getRfId () {
      return rfId;
   }

   public void setRfId (Long rfId) {
      this.rfId = rfId;
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

   public String getContentSource () {
      return contentSource;
   }

   public void setContentSource (String contentSource) {
      this.contentSource = contentSource;
   }

   public String getContentSourceType () {
      return contentSourceType;
   }

   public void setContentSourceType (String contentSourceType) {
      this.contentSourceType = contentSourceType;
   }

   /**
    * @return the entityCount
    */
   public Double getEntityCount () {
      return entityCount;
   }

   /**
    * @param entityCount
    *           the entityCount to set
    */
   public void setEntityCount (Double entityCount) {
      this.entityCount = entityCount;
   }

   /**
    * @return the createdDate
    */

   public Date getCreatedDate () {
      return createdDate;
   }

   /**
    * @param createdDate
    *           the createdDate to set
    */

   public void setCreatedDate (Date createdDate) {
      this.createdDate = createdDate;
   }

   /**
    * @return the maxMatchWeight
    */
   public Double getMaxMatchWeight () {
      return maxMatchWeight;
   }

   /**
    * @param maxMatchWeight
    *           the maxMatchWeight to set
    */
   public void setMaxMatchWeight (Double maxMatchWeight) {
      this.maxMatchWeight = maxMatchWeight;
   }

   /**
    * @return the udxAttributes
    */
   public Set<UDXAttribute> getUdxAttributes () {
      return udxAttributes;
   }

   /**
    * @param udxAttributes
    *           the udxAttributes to set
    */
   public void setUdxAttributes (Set<UDXAttribute> udxAttributes) {
      this.udxAttributes = udxAttributes;
   }

   /**
    * @return the contentDate
    */
   public Date getContentDate () {
      return contentDate;
   }

   /**
    * @param contentDate
    *           the contentDate to set
    */
   public void setContentDate (Date contentDate) {
      this.contentDate = contentDate;
   }

   /**
    * @return the imageUrl
    */
   public String getImageUrl () {
      return imageUrl;
   }

   /**
    * @param imageUrl
    *           the imageUrl to set
    */
   public void setImageUrl (String imageUrl) {
      this.imageUrl = imageUrl;
   }

}
