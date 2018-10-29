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

public class SemantifiedContentKeywordMatch implements Serializable {

   private Long   id;
   private Long   contextId;           // Application Id
   private Long   queryId;
   private Long   semantifiedContentId;
   private Double matchScore;
   private Date   executionDate;

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

   public Long getQueryId () {
      return queryId;
   }

   public void setQueryId (Long queryId) {
      this.queryId = queryId;
   }

   public Long getSemantifiedContentId () {
      return semantifiedContentId;
   }

   public void setSemantifiedContentId (Long semantifiedContentId) {
      this.semantifiedContentId = semantifiedContentId;
   }

   public Double getMatchScore () {
      return matchScore;
   }

   public void setMatchScore (Double matchScore) {
      this.matchScore = matchScore;
   }

   public Date getExecutionDate () {
      return executionDate;
   }

   public void setExecutionDate (Date executionDate) {
      this.executionDate = executionDate;
   }

}
