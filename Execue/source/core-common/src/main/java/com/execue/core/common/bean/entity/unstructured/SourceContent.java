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

public class SourceContent implements Serializable {

   private Long               id;
   private String             url;
   private String             title;
   private String             summary;
   private String             source;                                      // Article Posted location information
   private Long               contextId;                                   // Application Id
   private ProcessingFlagType processed = ProcessingFlagType.NOT_PROCESSED;
   private Date               addedDate;
   private String             failureCause;
   private Long               batchId;
   private Long               sourceItemId;                                // Id of the article at Source Content Aggregator
   private Long               sourceNodeId;                                // Data Source Id of teh Content Aggregator from where this article is pulled

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

   public String getTitle () {
      return title;
   }

   public void setTitle (String title) {
      this.title = title;
   }

   public String getSummary () {
      return summary;
   }

   public void setSummary (String summary) {
      this.summary = summary;
   }

   public String getSource () {
      return source;
   }

   public void setSource (String source) {
      this.source = source;
   }

   public Long getContextId () {
      return contextId;
   }

   public void setContextId (Long contextId) {
      this.contextId = contextId;
   }

   public ProcessingFlagType getProcessed () {
      return processed;
   }

   public void setProcessed (ProcessingFlagType processed) {
      this.processed = processed;
   }

   public Date getAddedDate () {
      return addedDate;
   }

   public void setAddedDate (Date addedDate) {
      this.addedDate = addedDate;
   }

   public String getFailureCause () {
      return failureCause;
   }

   public void setFailureCause (String failureCause) {
      this.failureCause = failureCause;
   }

   public Long getBatchId () {
      return batchId;
   }

   public void setBatchId (Long batchId) {
      this.batchId = batchId;
   }

   public Long getSourceItemId () {
      return sourceItemId;
   }

   public void setSourceItemId (Long sourceItemId) {
      this.sourceItemId = sourceItemId;
   }

   public Long getSourceNodeId () {
      return sourceNodeId;
   }

   public void setSourceNodeId (Long sourceNodeId) {
      this.sourceNodeId = sourceNodeId;
   }

}
