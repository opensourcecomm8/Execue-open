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

public class UDXKeywordMatch implements Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private Long              queryId;
   private Long              udxId;
   private Double            matchScore;
   private Date              executionDate;

   /**
    * @return the executionDate
    */
   public Date getExecutionDate () {
      return executionDate;
   }

   /**
    * @param executionDate
    *           the executionDate to set
    */
   public void setExecutionDate (Date executionDate) {
      this.executionDate = executionDate;
   }

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the udxId
    */
   public Long getUdxId () {
      return udxId;
   }

   /**
    * @param udxId
    *           the udxId to set
    */
   public void setUdxId (Long udxId) {
      this.udxId = udxId;
   }

   /**
    * @return the queryId
    */
   public Long getQueryId () {
      return queryId;
   }

   /**
    * @param queryId
    *           the queryId to set
    */
   public void setQueryId (Long queryId) {
      this.queryId = queryId;
   }

   /**
    * @return the matchScore
    */
   public Double getMatchScore () {
      return matchScore;
   }

   /**
    * @param matchScore
    *           the matchScore to set
    */
   public void setMatchScore (Double matchScore) {
      this.matchScore = matchScore;
   }
}