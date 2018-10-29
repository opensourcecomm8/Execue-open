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

import com.execue.core.common.type.NewsCategory;
import com.execue.core.common.type.ProcessingFlagType;

/**
 * @author Nihar
 */
public class NewsItem implements Serializable {

   private long               id;
   private String             url;
   private String             title;
   private String             summary;
   private String             source;
   private NewsCategory       category;
   private ProcessingFlagType processed;
   private Date               addedDate;
   private String             failureCause;
   private Long               batchId;

   /**
    * @return the failureCause
    */
   public String getFailureCause () {
      return failureCause;
   }

   /**
    * @param failureCause
    *           the failureCause to set
    */
   public void setFailureCause (String failureCause) {
      this.failureCause = failureCause;
   }

   /**
    * @return the addedDate
    */
   public Date getAddedDate () {
      return addedDate;
   }

   /**
    * @param addedDate
    *           the addedDate to set
    */
   public void setAddedDate (Date addedDate) {
      this.addedDate = addedDate;
   }

   /**
    * @return the id
    */
   public long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (long id) {
      this.id = id;
   }

   /**
    * @return the url
    */
   public String getUrl () {
      return url;
   }

   /**
    * @param url
    *           the url to set
    */
   public void setUrl (String url) {
      this.url = url;
   }

   /**
    * @return the title
    */
   public String getTitle () {
      return title;
   }

   /**
    * @param title
    *           the title to set
    */
   public void setTitle (String title) {
      this.title = title;
   }

   /**
    * @return the summary
    */
   public String getSummary () {
      return summary;
   }

   /**
    * @param summary
    *           the summary to set
    */
   public void setSummary (String summary) {
      this.summary = summary;
   }

   /**
    * @return the source
    */
   public String getSource () {
      return source;
   }

   /**
    * @param source
    *           the source to set
    */
   public void setSource (String source) {
      this.source = source;
   }

   /**
    * @return the category
    */
   public NewsCategory getCategory () {
      return category;
   }

   /**
    * @param category
    *           the category to set
    */
   public void setCategory (NewsCategory category) {
      this.category = category;
   }

   /**
    * @return the processed
    */
   public ProcessingFlagType getProcessed () {
      return processed;
   }

   /**
    * @param processed
    *           the processed to set
    */
   public void setProcessed (ProcessingFlagType processed) {
      this.processed = processed;
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

}