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

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.VerticalEntityBasedRedirection;
import com.execue.core.common.bean.nlp.TokenCandidate;
import com.execue.core.common.bean.qi.QuerySuggestion;
import com.execue.core.common.bean.uss.UniversalUnstructuredSearchResult;

/**
 * @author kaliki
 * @since 4.0
 */
public class QueryResult {

   private long                                    id;
   private String                                  originalQuery;
   private String                                  queryName;
   private List<PossibilityResult>                 possibilites;
   private List<String>                            messages;
   private List<QuerySuggestion>                   querySuggestions;
   private String                                  error;
   private List<TokenCandidate>                    tokenCandedates;
   private int                                     counter;
   private int                                     requestedPage;
   private int                                     pageSize;
   private int                                     pageCount;                           // totalResultCount
   private boolean                                 cacheResultPresent;
   private SearchFilter                            searchFilter;
   private VerticalEntityBasedRedirection          verticalEntityBasedRedirection;
   private List<Long>                              verticalEntityBasedRedirectionBEDIds;
   private boolean                                 onlyConfiguredEntitiesExists;
   // this variable use to get tkrname and ajaxurl for three tab report in result page
   private VerticalEntityBasedRedirection          entityBasedRedirection;
   private List<UniversalUnstructuredSearchResult> unstructuredSearchResult;
   private List<RelatedUserQuery>                  relatedUserQueries;
   private boolean                                 redirectURLPresent = false;
   private boolean                                 userQueryInfoSaved = false;
   private String                                  redirectURL;
   private boolean                                 isDisplaySqlQuery  = false;

   public int getCounter () {
      return counter;
   }

   public void setCounter (int counter) {
      this.counter = counter;
   }

   public String getQueryName () {
      return queryName;
   }

   public void setQueryName (String queryName) {
      this.queryName = queryName;
   }

   public List<PossibilityResult> getPossibilites () {
      if (possibilites == null) {
         possibilites = new ArrayList<PossibilityResult>();
      }
      return possibilites;
   }

   public void setPossibilites (List<PossibilityResult> possibilites) {
      this.possibilites = possibilites;
   }

   public long getId () {
      return id;
   }

   public void setId (long id) {
      this.id = id;
   }

   public List<String> getMessages () {
      if (messages == null) {
         messages = new ArrayList<String>(1);
      }
      return messages;
   }

   public void setMessages (List<String> messages) {
      this.messages = messages;
   }

   public List<QuerySuggestion> getQuerySuggestions () {
      return querySuggestions;
   }

   public void setQuerySuggestions (List<QuerySuggestion> querySuggestions) {
      this.querySuggestions = querySuggestions;
   }

   public String getError () {
      return error;
   }

   public void setError (String error) {
      this.error = error;
   }

   /**
    * @return the tokenCandedates
    */
   public List<TokenCandidate> getTokenCandedates () {
      return tokenCandedates;
   }

   /**
    * @param tokenCandedates
    *           the tokenCandedates to set
    */
   public void setTokenCandedates (List<TokenCandidate> tokenCandedates) {
      this.tokenCandedates = tokenCandedates;
   }

   public int getRequestedPage () {
      return requestedPage;
   }

   public void setRequestedPage (int requestedPage) {
      this.requestedPage = requestedPage;
   }

   public int getPageSize () {
      return pageSize;
   }

   public void setPageSize (int pageSize) {
      this.pageSize = pageSize;
   }

   public int getPageCount () {
      return pageCount;
   }

   public void setPageCount (int pageCount) {
      this.pageCount = pageCount;
   }

   public boolean isCacheResultPresent () {
      return cacheResultPresent;
   }

   public void setCacheResultPresent (boolean cacheResultPresent) {
      this.cacheResultPresent = cacheResultPresent;
   }

   public SearchFilter getSearchFilter () {
      return searchFilter;
   }

   public void setSearchFilter (SearchFilter searchFilter) {
      this.searchFilter = searchFilter;
   }

   public VerticalEntityBasedRedirection getVerticalEntityBasedRedirection () {
      return verticalEntityBasedRedirection;
   }

   public void setVerticalEntityBasedRedirection (VerticalEntityBasedRedirection verticalEntityBasedRedirection) {
      this.verticalEntityBasedRedirection = verticalEntityBasedRedirection;
   }

   public List<Long> getVerticalEntityBasedRedirectionBEDIds () {
      return verticalEntityBasedRedirectionBEDIds;
   }

   public void setVerticalEntityBasedRedirectionBEDIds (List<Long> verticalEntityBasedRedirectionBEDIds) {
      this.verticalEntityBasedRedirectionBEDIds = verticalEntityBasedRedirectionBEDIds;
   }

   /**
    * @return the unstructuredSearchResult
    */
   public List<UniversalUnstructuredSearchResult> getUnstructuredSearchResult () {
      return unstructuredSearchResult;
   }

   /**
    * @param unstructuredSearchResult
    *           the unstructuredSearchResult to set
    */
   public void setUnstructuredSearchResult (List<UniversalUnstructuredSearchResult> unstructuredSearchResult) {
      this.unstructuredSearchResult = unstructuredSearchResult;
   }

   public boolean isOnlyConfiguredEntitiesExists () {
      return onlyConfiguredEntitiesExists;
   }

   public void setOnlyConfiguredEntitiesExists (boolean onlyConfiguredEntitiesExists) {
      this.onlyConfiguredEntitiesExists = onlyConfiguredEntitiesExists;
   }

   /**
    * @return the relatedUserQueries
    */
   public List<RelatedUserQuery> getRelatedUserQueries () {
      return relatedUserQueries;
   }

   /**
    * @param relatedUserQueries
    *           the relatedUserQueries to set
    */
   public void setRelatedUserQueries (List<RelatedUserQuery> relatedUserQueries) {
      this.relatedUserQueries = relatedUserQueries;
   }

   public VerticalEntityBasedRedirection getEntityBasedRedirection () {
      return entityBasedRedirection;
   }

   public void setEntityBasedRedirection (VerticalEntityBasedRedirection entityBasedRedirection) {
      this.entityBasedRedirection = entityBasedRedirection;
   }

   public String getRedirectURL () {
      return redirectURL;
   }

   public void setRedirectURL (String redirectURL) {
      this.redirectURL = redirectURL;
   }

   /**
    * @return the redirectURLPresent
    */
   public boolean isRedirectURLPresent () {
      return redirectURLPresent;
   }

   /**
    * @param redirectURLPresent
    *           the redirectURLPresent to set
    */
   public void setRedirectURLPresent (boolean redirectURLPresent) {
      this.redirectURLPresent = redirectURLPresent;
   }

   /**
    * @return the originalQuery
    */
   public String getOriginalQuery () {
      return originalQuery;
   }

   /**
    * @param originalQuery
    *           the originalQuery to set
    */
   public void setOriginalQuery (String originalQuery) {
      this.originalQuery = originalQuery;
   }

   /**
    * @return the userQueryInfoSaved
    */
   public boolean isUserQueryInfoSaved () {
      return userQueryInfoSaved;
   }

   /**
    * @param userQueryInfoSaved
    *           the userQueryInfoSaved to set
    */
   public void setUserQueryInfoSaved (boolean userQueryInfoSaved) {
      this.userQueryInfoSaved = userQueryInfoSaved;
   }

   /**
    * @return the isDisplaySqlQuery
    */
   public boolean isDisplaySqlQuery () {
      return isDisplaySqlQuery;
   }

   /**
    * @param isDisplaySqlQuery the isDisplaySqlQuery to set
    */
   public void setDisplaySqlQuery (boolean isDisplaySqlQuery) {
      this.isDisplaySqlQuery = isDisplaySqlQuery;
   }

}