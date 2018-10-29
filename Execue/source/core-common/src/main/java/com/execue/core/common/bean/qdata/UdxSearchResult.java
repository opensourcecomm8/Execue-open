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


/**
 *
 */
package com.execue.core.common.bean.qdata;

import java.io.Serializable;

/**
 * @author Nihar
 */
public class UdxSearchResult implements Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private Long              queryId;
   private Long              udxId;
   private Long              rfId;
   private Double            matchWeight;
   private Integer           searchType;
   private Double            entityCount;
   private Long              appId;
   private String            matchedTriples;
   private Integer           matchedTriplesSum;

   /**
    * @return the matchedTriples
    */
   public String getMatchedTriples () {
      return matchedTriples;
   }

   /**
    * @param matchedTriples
    *           the matchedTriples to set
    */
   public void setMatchedTriples (String matchedTriples) {
      this.matchedTriples = matchedTriples;
   }

   /**
    * @return the appId
    */
   public Long getAppId () {
      return appId;
   }

   /**
    * @param appId
    *           the appId to set
    */
   public void setAppId (Long appId) {
      this.appId = appId;
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
    * @return the matchWeight
    */
   public Double getMatchWeight () {
      return matchWeight;
   }

   /**
    * @param matchWeight
    *           the matchWeight to set
    */
   public void setMatchWeight (Double matchWeight) {
      this.matchWeight = matchWeight;
   }

   /**
    * @return the searchType
    */
   public Integer getSearchType () {
      return searchType;
   }

   /**
    * @param searchType
    *           the searchType to set
    */
   public void setSearchType (Integer searchType) {
      this.searchType = searchType;
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
    * @return the rfId
    */
   public Long getRfId () {
      return rfId;
   }

   /**
    * @param rfId
    *           the rfId to set
    */
   public void setRfId (Long rfId) {
      this.rfId = rfId;
   }

   /**
    * @return the matchedTriplesSum
    */
   public Integer getMatchedTriplesSum () {
      return matchedTriplesSum;
   }

   /**
    * @param matchedTriplesSum the matchedTriplesSum to set
    */
   public void setMatchedTriplesSum (Integer matchedTriplesSum) {
      this.matchedTriplesSum = matchedTriplesSum;
   }
}