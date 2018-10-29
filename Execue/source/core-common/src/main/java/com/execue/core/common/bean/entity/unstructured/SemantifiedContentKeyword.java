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

public class SemantifiedContentKeyword implements Serializable {

   private Long   id;
   private Long   contextId;
   private Long   semantifiedContentId;
   private String keywordText;
   private Date   contentDate;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getContextId () {
      return contextId;
   }

   public void setContextId (Long contextId) {
      this.contextId = contextId;
   }

   public Long getSemantifiedContentId () {
      return semantifiedContentId;
   }

   public void setSemantifiedContentId (Long semantifiedContentId) {
      this.semantifiedContentId = semantifiedContentId;
   }

   public String getKeywordText () {
      return keywordText;
   }

   public void setKeywordText (String keywordText) {
      this.keywordText = keywordText;
   }

   public Date getContentDate () {
      return contentDate;
   }

   public void setContentDate (Date contentDate) {
      this.contentDate = contentDate;
   }
}