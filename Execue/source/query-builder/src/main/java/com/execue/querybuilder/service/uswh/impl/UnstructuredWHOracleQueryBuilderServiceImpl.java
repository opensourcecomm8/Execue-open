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


package com.execue.querybuilder.service.uswh.impl;

import java.util.List;

import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.uss.UnstructuredKeywordSearchInput;
import com.execue.core.common.bean.uss.UnstructuredSearchInput;

/**
 * Oracle specific query builder routines come here
 * 
 * @author Vishay
 * @version 1.0
 * @since 03/01/12
 */
public class UnstructuredWHOracleQueryBuilderServiceImpl extends UnstructuredWHQueryBuilderServiceImpl {

   @Override
   public String buildSemantifiedContentKeyWordMatchQuery (Long contextId,
            UnstructuredKeywordSearchInput unstructuredKeywordSearchInput, String aganistQueryTokens) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String bulidCompleteKeywordBasedResultsQuery (List<String> selectColumnNames, boolean isLocationBased,
            String userRequestedSortOrder) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String bulidCountQueryForCompleteKeywordBasedResult (String queryCompleteKeywordBasedResults) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String bulidOrderByClauseForCompleteKeywordBasedResult (List<String> searchResultOrder,
            boolean isLocationBased, boolean applyKeyWordSearchFilter) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String bulidSemantifiedContentSearchQuery (UnstructuredSearchInput unstructuredSearchInput,
            Integer requiredInfoSum, String joinQueryResultAlias) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String bulidKeyWordSearchFilterQuery (Long contextId, String orderByClause, String coreSearchQuery,
            boolean applyKeyWordSearchFilter, List<String> selectColumnNames, boolean isLocationBased) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String bulidPerfectMatchQuery (String coreSearchQuery, boolean usingLocationJoinQuery,
            String joinQueryResultAlias) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String bulidMightMatchQuery (String coreSearchQuery, boolean usingLocationJoinQuery,
            String joinQueryResultAlias) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String bulidTotalCountQuery (String coreSearchQuery) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String buildDeleteDedupOnTargetTempTableQuery (QueryTable queryTable) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String buildInsertSourceContentFromTempTableQuery (QueryTable queryTable) {
      // TODO Auto-generated method stub
      return null;
   }

}
