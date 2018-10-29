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
import java.util.Set;

import com.execue.core.common.type.CheckType;

/**
 * This bean represents the query asked by the user
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public class QDataUserQuery implements Serializable {

   private Long                      id;
   private Date                      executionDate;
   private String                    normalizedQueryString;
   private CheckType                 requestSuccessful = CheckType.NO;
   private Set<QDataReducedQuery>    reducedQueries;
   private Set<QDataBusinessQuery>   businessQueries;
   private Set<QDataAggregatedQuery> aggregatedQueries;
   private Set<QDataUserQueryColumn> queryColumns;
   private Long                      userId;
   private CheckType                 anonymousUser     = CheckType.NO;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Date getExecutionDate () {
      return executionDate;
   }

   public void setExecutionDate (Date executionDate) {
      this.executionDate = executionDate;
   }

   public String getNormalizedQueryString () {
      return normalizedQueryString;
   }

   public void setNormalizedQueryString (String normalizedQueryString) {
      this.normalizedQueryString = normalizedQueryString;
   }

   public Set<QDataReducedQuery> getReducedQueries () {
      return reducedQueries;
   }

   public void setReducedQueries (Set<QDataReducedQuery> reducedQueries) {
      this.reducedQueries = reducedQueries;
   }

   public Set<QDataBusinessQuery> getBusinessQueries () {
      return businessQueries;
   }

   public void setBusinessQueries (Set<QDataBusinessQuery> businessQueries) {
      this.businessQueries = businessQueries;
   }

   public Set<QDataAggregatedQuery> getAggregatedQueries () {
      return aggregatedQueries;
   }

   public void setAggregatedQueries (Set<QDataAggregatedQuery> aggregatedQueries) {
      this.aggregatedQueries = aggregatedQueries;
   }

   public Set<QDataUserQueryColumn> getQueryColumns () {
      return queryColumns;
   }

   public void setQueryColumns (Set<QDataUserQueryColumn> queryColumns) {
      this.queryColumns = queryColumns;
   }

   public CheckType getRequestSuccessful () {
      return requestSuccessful;
   }

   public void setRequestSuccessful (CheckType requestSuccessful) {
      this.requestSuccessful = requestSuccessful;
   }

   public CheckType getAnonymousUser () {
      return anonymousUser;
   }

   public void setAnonymousUser (CheckType anonymousUser) {
      this.anonymousUser = anonymousUser;
   }

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }
}
