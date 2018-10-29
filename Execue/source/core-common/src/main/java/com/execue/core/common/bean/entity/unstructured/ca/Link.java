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

public class Link implements Serializable {

   private Long   id;
   private Long   feedId;
   private String link;
   private String title;
   private String description;
   private Long   state;
   private Date   publishedDate;
   private Long   guid;

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
    * @return the feedId
    */
   public Long getFeedId () {
      return feedId;
   }

   /**
    * @param feedId the feedId to set
    */
   public void setFeedId (Long feedId) {
      this.feedId = feedId;
   }

   /**
    * @return the link
    */
   public String getLink () {
      return link;
   }

   /**
    * @param link the link to set
    */
   public void setLink (String link) {
      this.link = link;
   }

   /**
    * @return the title
    */
   public String getTitle () {
      return title;
   }

   /**
    * @param title the title to set
    */
   public void setTitle (String title) {
      this.title = title;
   }

   /**
    * @return the description
    */
   public String getDescription () {
      return description;
   }

   /**
    * @param description the description to set
    */
   public void setDescription (String description) {
      this.description = description;
   }

   /**
    * @return the state
    */
   public Long getState () {
      return state;
   }

   /**
    * @param state the state to set
    */
   public void setState (Long state) {
      this.state = state;
   }

   /**
    * @return the publishedDate
    */
   public Date getPublishedDate () {
      return publishedDate;
   }

   /**
    * @param publishedDate the publishedDate to set
    */
   public void setPublishedDate (Date publishedDate) {
      this.publishedDate = publishedDate;
   }

   /**
    * @return the guid
    */
   public Long getGuid () {
      return guid;
   }

   /**
    * @param guid the guid to set
    */
   public void setGuid (Long guid) {
      this.guid = guid;
   }

}
