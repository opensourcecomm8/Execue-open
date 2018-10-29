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


package com.execue.core.common.bean.entity.unstructured.ca;

import java.io.Serializable;
import java.util.Date;

public class Feed implements Serializable {

   private Long    id;
   private String  feedName;
   private String  mainLink;
   private String  rssLink;
   private String  imageLink;
   private Long    categoryId;
   private Date    lastUpdate;
   private Date    lastAddition;
   private Integer numHeadlines;
   private Long    maxLinks;
   private Long    statTotal;
   private Long    statExpand;
   private Long    statClick;
   private String  status;

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the feedName
    */
   public String getFeedName () {
      return feedName;
   }

   /**
    * @param feedName the feedName to set
    */
   public void setFeedName (String feedName) {
      this.feedName = feedName;
   }

   /**
    * @return the mainLink
    */
   public String getMainLink () {
      return mainLink;
   }

   /**
    * @param mainLink the mainLink to set
    */
   public void setMainLink (String mainLink) {
      this.mainLink = mainLink;
   }

   /**
    * @return the rssLink
    */
   public String getRssLink () {
      return rssLink;
   }

   /**
    * @param rssLink the rssLink to set
    */
   public void setRssLink (String rssLink) {
      this.rssLink = rssLink;
   }

   /**
    * @return the imageLink
    */
   public String getImageLink () {
      return imageLink;
   }

   /**
    * @param imageLink the imageLink to set
    */
   public void setImageLink (String imageLink) {
      this.imageLink = imageLink;
   }

   /**
    * @return the categoryId
    */
   public Long getCategoryId () {
      return categoryId;
   }

   /**
    * @param categoryId the categoryId to set
    */
   public void setCategoryId (Long categoryId) {
      this.categoryId = categoryId;
   }

   /**
    * @return the lastUpdate
    */
   public Date getLastUpdate () {
      return lastUpdate;
   }

   /**
    * @param lastUpdate the lastUpdate to set
    */
   public void setLastUpdate (Date lastUpdate) {
      this.lastUpdate = lastUpdate;
   }

   /**
    * @return the lastAddition
    */
   public Date getLastAddition () {
      return lastAddition;
   }

   /**
    * @param lastAddition the lastAddition to set
    */
   public void setLastAddition (Date lastAddition) {
      this.lastAddition = lastAddition;
   }

   /**
    * @return the numHeadlines
    */
   public Integer getNumHeadlines () {
      return numHeadlines;
   }

   /**
    * @param numHeadlines the numHeadlines to set
    */
   public void setNumHeadlines (Integer numHeadlines) {
      this.numHeadlines = numHeadlines;
   }

   public Long getMaxLinks () {
      return maxLinks;
   }

   public void setMaxLinks (Long maxLinks) {
      this.maxLinks = maxLinks;
   }

   public Long getStatTotal () {
      return statTotal;
   }

   public void setStatTotal (Long statTotal) {
      this.statTotal = statTotal;
   }

   public Long getStatExpand () {
      return statExpand;
   }

   public void setStatExpand (Long statExpand) {
      this.statExpand = statExpand;
   }

   public Long getStatClick () {
      return statClick;
   }

   public void setStatClick (Long statClick) {
      this.statClick = statClick;
   }

   public String getStatus () {
      return status;
   }

   public void setStatus (String status) {
      this.status = status;
   }

}
