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


package com.execue.core.common.bean;

import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.nlp.CandidateEntity;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.security.UserRemoteLocationInfo;

/**
 * @author John Mallavalli
 */
public class UserInput {

   private String                   request;
   private String                   requestedPage;
   private List<CandidateEntity>    entities;
   private QueryForm                queryForm;
   private String                   pageSize;
   // flag for disabling the query cache
   private boolean                  disableQueryCache = false;
   private Long                     userId;

   // For fetch response method
   private Long                     userQueryId;
   private Long                     businessQueryId;
   private Long                     assetId;
   private SearchFilter             searchFilter;
   private UserRemoteLocationInfo   userRemoteLocationInfo;
   private Set<SemanticPossibility> userPossibilities;

   public String getRequest () {
      return request;
   }

   public void setRequest (String request) {
      this.request = request;
   }

   public String getRequestedPage () {
      return requestedPage;
   }

   public void setRequestedPage (String requestedPage) {
      this.requestedPage = requestedPage;
   }

   public List<CandidateEntity> getEntities () {
      return entities;
   }

   public void setEntities (List<CandidateEntity> entities) {
      this.entities = entities;
   }

   public Long getUserQueryId () {
      return userQueryId;
   }

   public void setUserQueryId (Long userQueryId) {
      this.userQueryId = userQueryId;
   }

   public Long getBusinessQueryId () {
      return businessQueryId;
   }

   public void setBusinessQueryId (Long businessQueryId) {
      this.businessQueryId = businessQueryId;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public QueryForm getQueryForm () {
      return queryForm;
   }

   public void setQueryForm (QueryForm queryForm) {
      this.queryForm = queryForm;
   }

   public String getPageSize () {
      return pageSize;
   }

   public void setPageSize (String pageSize) {
      this.pageSize = pageSize;
   }

   public boolean isDisableQueryCache () {
      return disableQueryCache;
   }

   public void setDisableQueryCache (boolean disableQueryCache) {
      this.disableQueryCache = disableQueryCache;
   }

   public SearchFilter getSearchFilter () {
      return searchFilter;
   }

   public void setSearchFilter (SearchFilter searchFilter) {
      this.searchFilter = searchFilter;
   }

   /**
    * @return the userRemoteLocationInfo
    */
   public UserRemoteLocationInfo getUserRemoteLocationInfo () {
      return userRemoteLocationInfo;
   }

   /**
    * @param userRemoteLocationInfo
    *           the userRemoteLocationInfo to set
    */
   public void setUserRemoteLocationInfo (UserRemoteLocationInfo userRemoteLocationInfo) {
      this.userRemoteLocationInfo = userRemoteLocationInfo;
   }

   public Set<SemanticPossibility> getUserPossibilities () {
      return userPossibilities;
   }

   public void setUserPossibilities (Set<SemanticPossibility> userPossibilities) {
      this.userPossibilities = userPossibilities;
   }

   public boolean isRequestSemantified () {
      return userPossibilities != null;
   }

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }

}
