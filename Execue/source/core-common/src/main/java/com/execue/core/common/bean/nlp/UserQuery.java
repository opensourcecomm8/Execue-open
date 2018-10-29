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


/*
 * Created on Jul 23, 2008
 */
package com.execue.core.common.bean.nlp;

import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.type.SearchType;

/**
 * @author kaliki
 */
public class UserQuery {

   private Long              userQueryId;
   private String            originalQuery;
   private String            query;
   private NLPInformation    nlpInformation;
   private SearchFilter      searchFilter;
   private WeightInformation baseWeightInformation;
   private SearchType        searchType;
   private boolean           backendSearch;
   private Integer           sentenceId;
   private boolean           fromArticle;                // To identify that this query is not stand alone but is one from a
   private boolean           skipLocationTypeRecognition;

   // big article.

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
    * @return the query
    */
   public String getQuery () {
      return query;
   }

   /**
    * @param query
    *           the query to set
    */
   public void setQuery (String query) {
      this.query = query;
   }

   /**
    * @return the nlpInformation
    */
   public NLPInformation getNlpInformation () {
      return nlpInformation;
   }

   /**
    * @param nlpInformation
    *           the nlpInformation to set
    */
   public void setNlpInformation (NLPInformation nlpInformation) {
      this.nlpInformation = nlpInformation;
   }

   /**
    * @return the searchFilter
    */
   public SearchFilter getSearchFilter () {
      return searchFilter;
   }

   /**
    * @param searchFilter
    *           the searchFilter to set
    */
   public void setSearchFilter (SearchFilter searchFilter) {
      this.searchFilter = searchFilter;
   }

   /**
    * @return the baseWeightInformation
    */
   public WeightInformation getBaseWeightInformation () {
      return baseWeightInformation;
   }

   /**
    * @param baseWeightInformation
    *           the baseWeightInformation to set
    */
   public void setBaseWeightInformation (WeightInformation baseWeightInformation) {
      this.baseWeightInformation = baseWeightInformation;
   }

   /**
    * @return the searchType
    */
   public SearchType getSearchType () {
      if (searchType == null) {
         return SearchType.DEFAULT;
      }
      return searchType;
   }

   /**
    * @param searchType
    *           the searchType to set
    */
   public void setSearchType (SearchType searchType) {
      this.searchType = searchType;
   }

   /**
    * @return the backendSearch
    */
   public boolean isBackendSearch () {
      return backendSearch;
   }

   /**
    * @param backendSearch
    *           the backendSearch to set
    */
   public void setBackendSearch (boolean backendSearch) {
      this.backendSearch = backendSearch;
   }

   /**
    * @return the sentenceId
    */
   public Integer getSentenceId () {
      return sentenceId;
   }

   /**
    * @param sentenceId
    *           the sentenceId to set
    */
   public void setSentenceId (Integer sentenceId) {
      this.sentenceId = sentenceId;
   }

   /**
    * @return the fromArticle
    */
   public boolean isFromArticle () {
      return fromArticle;
   }

   /**
    * @param fromArticle
    *           the fromArticle to set
    */
   public void setFromArticle (boolean fromArticle) {
      this.fromArticle = fromArticle;
   }

   /**
    * @return the userQueryId
    */
   public Long getUserQueryId () {
      return userQueryId;
   }

   /**
    * @param userQueryId the userQueryId to set
    */
   public void setUserQueryId (Long userQueryId) {
      this.userQueryId = userQueryId;
   }

   /**
    * @return the skipLocationTypeRecognition
    */
   public boolean isSkipLocationTypeRecognition () {
      return skipLocationTypeRecognition;
   }

   /**
    * @param skipLocationTypeRecognition the skipLocationTypeRecognition to set
    */
   public void setSkipLocationTypeRecognition (boolean skipLocationTypeRecognition) {
      this.skipLocationTypeRecognition = skipLocationTypeRecognition;
   }
}