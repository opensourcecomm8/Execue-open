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


package com.execue.core.common.bean.swi;

import java.io.Serializable;
import java.util.Date;

import com.execue.core.common.type.TermType;

/**
 * @author John Mallavalli
 */
public class PopularityHit implements Serializable {

   private Long     id;
   private Long     termId;
   private TermType type;
   private Long     hits;
   private String   processingState;
   private Date     createdDate;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getTermId () {
      return termId;
   }

   public void setTermId (Long termId) {
      this.termId = termId;
   }

   public TermType getType () {
      return type;
   }

   public void setType (TermType type) {
      this.type = type;
   }

   public Long getHits () {
      return hits;
   }

   public void setHits (Long hits) {
      this.hits = hits;
   }

   public String getProcessingState () {
      return processingState;
   }

   public void setProcessingState (String processingState) {
      this.processingState = processingState;
   }

   public Date getCreatedDate () {
      return createdDate;
   }

   public void setCreatedDate (Date createdDate) {
      this.createdDate = createdDate;
   }
}
