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

import java.util.Date;
import java.util.Set;

/**
 * This bean represents the business query in the system
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public class QDataBusinessQuery implements java.io.Serializable {

   private Long                          id;
   private Long                          modelId;
   private Date                          executionDate;
   private String                        psuedoLanguageQueryString;
   private QDataUserQuery                userQuery;
   private Set<QDataBusinessQueryColumn> queryColumns;
   private Set<QDataAggregatedQuery>     aggregatedQueries;
   private String                        applicationName;
   private String                        requestRecognition;
   private long                          applicationId;

   public String getApplicationName () {
      return applicationName;
   }

   public void setApplicationName (String applicationName) {
      this.applicationName = applicationName;
   }

   public String getRequestRecognition () {
      return requestRecognition;
   }

   public void setRequestRecognition (String requestRecognition) {
      this.requestRecognition = requestRecognition;
   }

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

   public String getPsuedoLanguageQueryString () {
      return psuedoLanguageQueryString;
   }

   public void setPsuedoLanguageQueryString (String psuedoLanguageQueryString) {
      this.psuedoLanguageQueryString = psuedoLanguageQueryString;
   }

   public QDataUserQuery getUserQuery () {
      return userQuery;
   }

   public void setUserQuery (QDataUserQuery userQuery) {
      this.userQuery = userQuery;
   }

   public Set<QDataBusinessQueryColumn> getQueryColumns () {
      return queryColumns;
   }

   public void setQueryColumns (Set<QDataBusinessQueryColumn> queryColumns) {
      this.queryColumns = queryColumns;
   }

   public Set<QDataAggregatedQuery> getAggregatedQueries () {
      return aggregatedQueries;
   }

   public void setAggregatedQueries (Set<QDataAggregatedQuery> aggregatedQueries) {
      this.aggregatedQueries = aggregatedQueries;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (long applicationId) {
      this.applicationId = applicationId;
   }
}
