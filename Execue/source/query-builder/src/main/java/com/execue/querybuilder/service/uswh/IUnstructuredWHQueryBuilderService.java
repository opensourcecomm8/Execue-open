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


package com.execue.querybuilder.service.uswh;

import java.util.List;

import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.uss.UnstructuredKeywordSearchInput;
import com.execue.core.common.bean.uss.UnstructuredSearchInput;

/**
 * Interface for query builder
 * 
 * @author Vishay
 * @version 1.0
 * @since 03/01/12
 */
public interface IUnstructuredWHQueryBuilderService {

   public String buildSemantifiedContentKeyWordMatchQuery (Long contextId,
            UnstructuredKeywordSearchInput unstructuredKeywordSearchInput, String againstQueryTokens);

   public String bulidCompleteKeywordBasedResultsQuery (List<String> selectColumnNames, boolean isLocationBased,
            String userRequestedSortOrder);

   public String bulidCountQueryForCompleteKeywordBasedResult (String queryCompleteKeywordBasedResults);

   public String bulidOrderByClauseForCompleteKeywordBasedResult (List<String> searchResultOrder,
            boolean isLocationBased, boolean applyKeyWordSearchFilter);

   public String bulidSemantifiedContentSearchQuery (UnstructuredSearchInput unstructuredSearchInput,
            Integer requiredInfoSum, String joinQueryResultAlias);

   public String bulidKeyWordSearchFilterQuery (Long contextId, String orderByClause, String coreSearchQuery,
            boolean applyKeyWordSearchFilter, List<String> selectColumnNames, boolean isLocationBased);

   public String bulidPerfectMatchQuery (String coreSearchQuery, boolean usingLocationJoinQuery,
            String joinQueryResultAlias);

   public String bulidMightMatchQuery (String coreSearchQuery, boolean usingLocationJoinQuery,
            String joinQueryResultAlias);

   public String bulidTotalCountQuery (String coreSearchQuery);

   public String buildDeleteDedupOnTargetTempTableQuery (QueryTable queryTable);

   public String buildInsertSourceContentFromTempTableQuery (QueryTable queryTable);

}
