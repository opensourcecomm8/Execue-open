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


package com.execue.semantification.bean;

import java.io.Serializable;
import java.util.Date;

import com.execue.core.common.bean.entity.unstructured.SemantifiedContent;
import com.execue.core.common.type.ProcessingFlagType;

/**
 * @author Prasanna
 */
public class SemantificationInput implements Serializable {

   private static final long  serialVersionUID  = 1L;
   private Long               id;
   private String             url;
   private String             title;
   private String             summary;
   private String             source;
   private Long               contextId;                                           // Application Id
   private ProcessingFlagType processed         = ProcessingFlagType.NOT_PROCESSED;
   private Date               addedDate;
   private boolean            resemantification = false;
   private SemantifiedContent existingSemantifiedContent;

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

   /**
    * @return the resemantification
    */
   public boolean isResemantification () {
      return resemantification;
   }

   /**
    * @param resemantification the resemantification to set
    */
   public void setResemantification (boolean resemantification) {
      this.resemantification = resemantification;
   }

   /**
    * @return the existingSemantifiedContent
    */
   public SemantifiedContent getExistingSemantifiedContent () {
      return existingSemantifiedContent;
   }

   /**
    * @param existingSemantifiedContent the existingSemantifiedContent to set
    */
   public void setExistingSemantifiedContent (SemantifiedContent existingSemantifiedContent) {
      this.existingSemantifiedContent = existingSemantifiedContent;
   }

}
