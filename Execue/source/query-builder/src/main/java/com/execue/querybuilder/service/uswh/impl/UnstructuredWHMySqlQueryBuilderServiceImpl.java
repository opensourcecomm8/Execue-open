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

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.uss.UnstructuredKeywordSearchInput;
import com.execue.core.common.bean.uss.UnstructuredSearchInput;
import com.execue.core.common.type.FeatureValueType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.util.queryadaptor.ISQLAdaptor;
import com.execue.util.queryadaptor.SQLAdaptorFactory;

/**
 * MySql specific query builder routines come here
 * 
 * @author Vishay
 * @version 1.0
 * @since 03/01/12
 */
public class UnstructuredWHMySqlQueryBuilderServiceImpl extends UnstructuredWHQueryBuilderServiceImpl {

   private static final String SEARCH_RESULT_ORDER_DELIMITER = "##";

   public String buildSemantifiedContentKeyWordMatchQuery (Long contextId,
            UnstructuredKeywordSearchInput unstructuredKeywordSearchInput, String againstQueryTokens) {

      Long userQueryId = unstructuredKeywordSearchInput.getUserQueryId();
      List<String> userRequestedLocations = unstructuredKeywordSearchInput.getUserRequestedLocations();
      String dbFunctionNameForMultipleLocationQuery = unstructuredKeywordSearchInput
               .getDbFunctionNameForMultipleLocationQuery();
      Integer userQueryDistanceLimit = unstructuredKeywordSearchInput.getUserQueryDistanceLimit();

      boolean applyImagePresentFilter = unstructuredKeywordSearchInput.isApplyImagePresentFilter();
      boolean isLocationBased = unstructuredKeywordSearchInput.isLocationBased();
      boolean useDbFunctionForMultipleLocationQuery = unstructuredKeywordSearchInput
               .isUseDbFunctionForMultipleLocationQuery();
      // Declare Query Related Variables
      String sckwTableAlias = "sckw";
      String scTableAlias = "sc";
      String uqliTableAlias = "uqli";
      String joinQueryAlias = "sckwm";

      StringBuilder kwMatchQuerySelectClause = new StringBuilder();
      StringBuilder kwMatchQueryFromClause = new StringBuilder();
      StringBuilder kwMatchQueryWhereClause = new StringBuilder();

      StringBuilder joinQuerySelectClause = new StringBuilder();
      StringBuilder joinQueryFromClause = new StringBuilder();
      StringBuilder joinQueryWhereClause = new StringBuilder();
      StringBuilder orderByClause = new StringBuilder();

      kwMatchQuerySelectClause.append("SELECT sckw.context_id, " + userQueryId
               + " as query_id, sckw.semantified_content_id, MATCH(sckw.key_word_text) AGAINST('" + againstQueryTokens
               + "') AS match_score");

      kwMatchQueryFromClause.append(" FROM semantified_content_key_word ").append(sckwTableAlias).append(
               ", semantified_content ").append(scTableAlias);

      kwMatchQueryWhereClause.append(" WHERE sckw.context_id = " + contextId
               + " AND MATCH(sckw.key_word_text) AGAINST('" + againstQueryTokens
               + "') AND sckw.semantified_content_id = sc.id AND sc.processing_state <> 'P'");

      if (applyImagePresentFilter) {
         kwMatchQueryWhereClause.append(" AND sc.IMAGE_URL <> ''");
      }

      orderByClause.append(" ORDER BY match_score DESC");
      String limitClause = " LIMIT :minResult , :maxResult";
      String kwMatchQuery = "";
      if (isLocationBased) {
         if (userRequestedLocations.size() > 1) {
            // Handle multiple location
            if (useDbFunctionForMultipleLocationQuery) {
               // using db function for multiple location handling

               kwMatchQuerySelectClause.append(", :executionDate as execution_date");

               kwMatchQueryWhereClause.append(" AND ");
               kwMatchQueryWhereClause.append(getMinDistanceUserFunctionCallAsString(userRequestedLocations,
                        scTableAlias, dbFunctionNameForMultipleLocationQuery));
               kwMatchQueryWhereClause.append(" <= ").append(getSQRTBasedDistanceLimit(userQueryDistanceLimit));

               orderByClause.append(", ").append(
                        getMinDistanceUserFunctionCallAsString(userRequestedLocations, scTableAlias,
                                 dbFunctionNameForMultipleLocationQuery)).append(" ASC");
               orderByClause.append(", ").append(scTableAlias).append(".content_date DESC");

               kwMatchQuery = kwMatchQuerySelectClause.toString() + kwMatchQueryFromClause + kwMatchQueryWhereClause
                        + orderByClause + limitClause;
            } else {

               kwMatchQuerySelectClause.append(", ").append(scTableAlias).append(".longitude");
               kwMatchQuerySelectClause.append(", ").append(scTableAlias).append(".latitude");
               kwMatchQuerySelectClause.append(", ").append(scTableAlias).append(".content_date");

               joinQuerySelectClause.append("SELECT ").append(joinQueryAlias).append(".context_id, ");
               joinQuerySelectClause.append(joinQueryAlias).append(".query_id, ").append(joinQueryAlias).append(
                        ".semantified_content_id, ");
               joinQuerySelectClause.append(joinQueryAlias).append(".match_score, :executionDate as execution_date");

               joinQueryFromClause.append(" FROM user_query_location_info ").append(uqliTableAlias).append(" JOIN (");

               // using join query for multiple location handling
               joinQueryWhereClause.append(") AS ").append(joinQueryAlias).append(
                        " ON uqli.QUERY_ID = :userQueryId AND ");
               joinQueryWhereClause
                        .append(getSQRTBasedDistanceStringForMultipleLocation(joinQueryAlias, uqliTableAlias));
               joinQueryWhereClause.append("<=").append(getSQRTBasedDistanceLimit(userQueryDistanceLimit));

               orderByClause.append(", ").append(
                        getSQRTBasedDistanceStringForMultipleLocation(joinQueryAlias, uqliTableAlias)).append(" ASC");
               orderByClause.append(", ").append(joinQueryAlias).append(".content_date DESC");

               kwMatchQuery = joinQuerySelectClause.toString() + joinQueryFromClause
                        + kwMatchQuerySelectClause.toString() + kwMatchQueryFromClause + kwMatchQueryWhereClause
                        + joinQueryWhereClause + orderByClause + limitClause;

            }
         } else {
            // Handle single location

            kwMatchQuerySelectClause.append(", :executionDate as execution_date");

            kwMatchQueryWhereClause.append(" AND ");
            kwMatchQueryWhereClause.append(getSQRTBasedDistanceStringForSingleLocation(userRequestedLocations.get(0),
                     scTableAlias));
            kwMatchQueryWhereClause.append(" <= ").append(getSQRTBasedDistanceLimit(userQueryDistanceLimit));

            orderByClause.append(", ").append(
                     getSQRTBasedDistanceStringForSingleLocation(userRequestedLocations.get(0), scTableAlias)).append(
                     " ASC");
            orderByClause.append(", ").append(scTableAlias).append(".content_date DESC");

            kwMatchQuery = kwMatchQuerySelectClause.toString() + kwMatchQueryFromClause + kwMatchQueryWhereClause
                     + orderByClause + limitClause;
         }
      } else {
         // If location is not present
         kwMatchQuerySelectClause.append(", :executionDate as execution_date");
         kwMatchQuery = kwMatchQuerySelectClause.toString() + kwMatchQueryFromClause + kwMatchQueryWhereClause
                  + orderByClause + limitClause;
      }

      // populate the semantified content keyword match table for the user query id
      String insertClause = "INSERT INTO sem_content_kw_match(context_id, query_id, semantified_content_id,match_score,execution_date) ";
      return insertClause + kwMatchQuery;
   }

   private static final String getMinDistanceUserFunctionCallAsString (List<String> userRequestedLocations,
            String alias, String dbFunctionNameForMultipleLocationQuery) {

      return dbFunctionNameForMultipleLocationQuery + "(" + alias + ".LONGITUDE," + alias + ".LATITUDE, '"
               + ExecueCoreUtil.joinCollection(userRequestedLocations, ":") + "')";
   }

   private double getSQRTBasedDistanceLimit (Integer distanceLimit) {
      double sqrtBasedDistanceLimit = Math.pow(distanceLimit / 69d, 2);
      return sqrtBasedDistanceLimit;
   }

   private static final String getSQRTBasedDistanceStringForMultipleLocation (String targetTableAlias,
            String uqliTableAlias) {
      return "POW((" + targetTableAlias + ".LONGITUDE - " + uqliTableAlias + ".LONGITUDE), 2) + POW(("
               + targetTableAlias + ".LATITUDE - " + uqliTableAlias + ".LATITUDE), 2)";
   }

   private Object getSQRTBasedDistanceStringForSingleLocation (String userRequestedLocation, String alias) {
      String[] location = userRequestedLocation.split("~~");
      String longitude = location[0];
      String latitude = location[1];
      return "POW((" + alias + ".LONGITUDE - (" + longitude + ")), 2) + POW((" + alias + ".LATITUDE - (" + latitude
               + ")), 2)";
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.querybuilder.service.uswh.IUnstructuredWHQueryBuilderService#bulidCompleteKeywordBasedResultsQuery(java.util.List,
    *      boolean, java.lang.String)
    */
   @Override
   public String bulidCompleteKeywordBasedResultsQuery (List<String> selectColumnNames, boolean isLocationBased,
            String userRequestedSortOrder) {

      StringBuilder selectClause = new StringBuilder();
      selectClause.append("SELECT scfi.semantified_content_id, ").append(
               prepareSelectedColumnNamesWithAlias(selectColumnNames, "scfi")).append("scfi.semantified_content_date");
      if (isLocationBased) {
         selectClause.append(", scfi.location_display_name");
      }
      StringBuilder fromClause = new StringBuilder();
      fromClause.append(" FROM sem_content_kw_match kwm, sem_content_feature_info scfi");
      StringBuilder whereClause = new StringBuilder();
      whereClause
               .append(" WHERE kwm.query_id = :userQueryId and kwm.semantified_content_id = scfi.semantified_content_id");

      String groupByClause = " GROUP BY scfi.semantified_content_id";
      String orderByClause = "";
      if (!ExecueCoreUtil.isEmpty(userRequestedSortOrder)) {
         String[] sortOrder = userRequestedSortOrder.split(SEARCH_RESULT_ORDER_DELIMITER);
         orderByClause = " ORDER BY scfi." + sortOrder[0] + " " + sortOrder[1] + ", kwm.ID";
      } else {
         orderByClause = " ORDER BY kwm.ID";
      }

      String finalQuery = selectClause.toString() + fromClause.toString() + whereClause + groupByClause + orderByClause;

      return finalQuery;
   }

   private String prepareSelectedColumnNamesWithAlias (List<String> selectColumnNames, String alias) {
      List<String> columnNamesWithAlias = new ArrayList<String>();
      String tempColumnNames = null;
      for (String columnName : selectColumnNames) {
         columnNamesWithAlias.add(alias + "." + columnName);
      }
      tempColumnNames = ExecueCoreUtil.joinCollection(columnNamesWithAlias);
      if (!ExecueCoreUtil.isEmpty(tempColumnNames)) {
         tempColumnNames = tempColumnNames + ", ";
         return tempColumnNames;
      }
      return tempColumnNames;
   }

   @Override
   public String bulidCountQueryForCompleteKeywordBasedResult (String queryCompleteKeywordBasedResults) {
      String clountSelectClause = "SELECT COUNT(U.semantified_content_id) AS FINAL_COUNT FROM (";
      String countQuery = clountSelectClause + queryCompleteKeywordBasedResults + ") AS U";
      return countQuery;

   }

   @Override
   public String bulidOrderByClauseForCompleteKeywordBasedResult (List<String> searchResultOrder,
            boolean isLocationBased, boolean applyKeyWordSearchFilter) {
      if (searchResultOrder.isEmpty()) {
         return "";
      }
      StringBuilder sb = new StringBuilder(1);
      sb.append("ORDER BY ");
      for (String columnAndOrderType : searchResultOrder) {
         // Skip the KEY_WORD_MATCH_SCORE if the applyKeyWordSearchFilter is false
         if (!applyKeyWordSearchFilter && columnAndOrderType.startsWith("KEY_WORD_MATCH_SCORE")) {
            continue;
         }
         if (!isLocationBased && columnAndOrderType.startsWith("DISTANCE")) {
            continue;
         }
         // Split the columnAndOrderType which is in format KEY_WORD_MATCH_SCORE##DESC
         String[] split = columnAndOrderType.split(SEARCH_RESULT_ORDER_DELIMITER);
         sb.append(split[0]).append(" ").append(split[1]).append(", ");
      }
      return sb.toString().substring(0, sb.length() - 2);
   }

   @Override
   public String bulidSemantifiedContentSearchQuery (UnstructuredSearchInput unstructuredSearchInput,
            Integer requiredInfoSum, String joinQueryResultAlias) {
      List<String> selectColumnNames = unstructuredSearchInput.getSelectColumnNames();
      Integer userQueryFeatureCount = unstructuredSearchInput.getUserQueryFeatureCount();
      Integer userQueryRecordCount = unstructuredSearchInput.getUserQueryRecordCount();
      List<String> userRequestedLocations = unstructuredSearchInput.getUserRequestedLocations();
      String dbFunctionNameForMultipleLocationQuery = unstructuredSearchInput
               .getDbFunctionNameForMultipleLocationQuery();
      Integer distanceLimit = unstructuredSearchInput.getDistanceLimit();

      boolean useDbFunctionForMultipleLocationQuery = unstructuredSearchInput.isUseDbFunctionForMultipleLocationQuery();
      boolean isLocationBased = unstructuredSearchInput.isLocationBased();
      boolean imagePresent = unstructuredSearchInput.isImagePresent();
      boolean applyPartialMatchFilter = unstructuredSearchInput.isApplyPartialMatchFilter();

      // Prepare the semantified content feature info query by joining the sem_content_feature_info and
      // user_query_feature_info
      String scfiTableAlias = "scfi";
      String uqfiTableAlias = "uqfi";
      String selectColumns = prepareSelectedColumnNamesWithAlias(selectColumnNames, scfiTableAlias);
      StringBuilder selectClause = new StringBuilder();
      selectClause.append("SELECT ").append(scfiTableAlias).append(".semantified_content_id, ").append(selectColumns)
               .append(scfiTableAlias).append(".semantified_content_date, (").append(requiredInfoSum).append("+ SUM(")
               .append(scfiTableAlias).append(".FEATURE_WEIGHT*uqfi.FEATURE_WEIGHT_FACTOR))/").append(
                        (userQueryFeatureCount * 10 + requiredInfoSum)).append(" * 100 AS SEMANTIC_SCORE,").append(
                        "COUNT(").append(scfiTableAlias).append(".FEATURE_ID)/").append(userQueryRecordCount).append(
                        " AS MATCHED_FACTOR");

      if (isLocationBased) {
         selectClause.append(", ").append(scfiTableAlias).append(".location_display_name");
         if (userRequestedLocations.size() > 1) {
            if (useDbFunctionForMultipleLocationQuery) {
               selectClause.append(", ").append(
                        getMinDistanceUserFunctionCallAsString(userRequestedLocations, scfiTableAlias,
                                 dbFunctionNameForMultipleLocationQuery)).append(" AS DISTANCE");
            } else {
               selectClause.append(", ").append(scfiTableAlias).append(".LONGITUDE, ").append(scfiTableAlias).append(
                        ".LATITUDE");
            }
         } else {
            selectClause.append(", ").append(
                     getSQRTBasedDistanceStringForSingleLocation(userRequestedLocations.get(0), scfiTableAlias))
                     .append(" AS DISTANCE");
         }
      }

      StringBuilder query = new StringBuilder();
      query.append(selectClause);
      query.append(getFromClause(scfiTableAlias, uqfiTableAlias));
      // Create and Append first where clause with Query ID and Image Present flag
      query.append(getWhereClause(imagePresent)).append(' ');
      query.append(getFeatureWhereClause());
      if (isLocationBased) {
         // Append where clause for distance
         query.append(getWhereClauseForDistance(distanceLimit, userRequestedLocations,
                  useDbFunctionForMultipleLocationQuery, dbFunctionNameForMultipleLocationQuery, scfiTableAlias));
      }
      // Group By Semantified Content ID as we need Semantified Content Specific Information
      query.append(" GROUP BY ").append(scfiTableAlias).append(".semantified_content_id");

      // Attach having clause to filter the partial matches
      if (applyPartialMatchFilter) {
         query.append(" HAVING MATCHED_FACTOR = 1");
      }

      if (isLocationBased && userRequestedLocations.size() > 1 && !useDbFunctionForMultipleLocationQuery) {
         String uqliTableAlias = "uqli";
         String selectColumnsForMultipleLocationJoinQuery = prepareSelectedColumnNamesWithAlias(selectColumnNames,
                  joinQueryResultAlias);
         StringBuilder selectClauseForMultipleLocationJoinQuery = new StringBuilder();
         selectClauseForMultipleLocationJoinQuery.append("SELECT ").append(joinQueryResultAlias).append(
                  ".semantified_content_id, ").append(selectColumnsForMultipleLocationJoinQuery).append(
                  joinQueryResultAlias).append(".semantified_content_date, ").append(joinQueryResultAlias).append(
                  ".SEMANTIC_SCORE, ").append(joinQueryResultAlias).append(".MATCHED_FACTOR, ").append(
                  joinQueryResultAlias).append(".location_display_name, ").append(
                  getSQRTBasedDistanceStringForMultipleLocation(joinQueryResultAlias, uqliTableAlias)).append(
                  " AS DISTANCE");
         StringBuilder fromClauseForMultipleLocationJoinQuery = new StringBuilder();
         fromClauseForMultipleLocationJoinQuery.append(" FROM user_query_location_info ").append(uqliTableAlias)
                  .append(" JOIN (");

         // Prepare where clause for multiple location join query
         StringBuilder whereClauseForMultipleLocationJoinQuery = new StringBuilder();
         whereClauseForMultipleLocationJoinQuery.append(") AS ").append(joinQueryResultAlias).append(" ON ");
         whereClauseForMultipleLocationJoinQuery.append(uqliTableAlias).append(".QUERY_ID = :userQueryId AND ");
         whereClauseForMultipleLocationJoinQuery.append(getSQRTBasedDistanceStringForMultipleLocation(
                  joinQueryResultAlias, uqliTableAlias));
         whereClauseForMultipleLocationJoinQuery.append(" <= ").append(getSQRTBasedDistanceLimit(distanceLimit));

         StringBuilder groupByClauseForMultipleLocationJoinQuery = new StringBuilder();
         groupByClauseForMultipleLocationJoinQuery.append(" GROUP BY ").append(joinQueryResultAlias).append(
                  ".semantified_content_id");

         // Prepare the final query with multiple location using join
         String finalQuery = selectClauseForMultipleLocationJoinQuery.toString()
                  + fromClauseForMultipleLocationJoinQuery + query + whereClauseForMultipleLocationJoinQuery
                  + groupByClauseForMultipleLocationJoinQuery;
         return finalQuery;
      }

      return query.toString();

   }

   private String getWhereClause (boolean imagePresent) {
      StringBuilder whereClause = new StringBuilder(1);
      whereClause.append(" WHERE uqfi.QUERY_ID = :userQueryId");
      whereClause.append(" AND scfi.processing_state <> 'P'");
      if (imagePresent) {
         whereClause.append(" AND scfi.IMAGE_PRESENT = 'Y' ");
      }
      return whereClause.toString();
   }

   private String getFromClause (String scfiTableAlias, String uqfiTableAlias) {
      StringBuilder fromClause = new StringBuilder();
      fromClause.append(" FROM sem_content_feature_info ").append(scfiTableAlias).append(", user_query_feature_info ")
               .append(uqfiTableAlias);
      return fromClause.toString();
   }

   private String getFeatureWhereClause () {
      StringBuilder whereClause = new StringBuilder();

      // Attach Context id Where Clause
      whereClause.append(" AND scfi.CONTEXT_ID = uqfi.CONTEXT_ID");
      // Attach Features Where Clause
      whereClause.append(" AND scfi.FEATURE_ID = uqfi.FEATURE_ID");
      // Attach Feature Values Where Clause
      whereClause.append(" AND ((scfi.VALUE_TYPE = '").append(FeatureValueType.VALUE_STRING.getValue()).append("'");
      whereClause.append(" AND scfi.VALUE_STRING = uqfi.START_VALUE) ");
      whereClause.append(" OR (scfi.VALUE_TYPE = '").append(FeatureValueType.VALUE_NUMBER.getValue()).append("'");
      whereClause.append(" AND scfi.VALUE_NUMBER BETWEEN uqfi.START_VALUE AND uqfi.END_VALUE) ");
      whereClause.append(" OR (scfi.VALUE_TYPE = '").append(FeatureValueType.VALUE_DUMMY.getValue()).append("'))");

      return whereClause.toString();
   }

   private String getWhereClauseForDistance (Integer distanceLimit, List<String> userRequestedLocations,
            boolean useDbFunctionForMultipleLocationQuery, String dbFunctionNameForMultipleLocationQuery,
            String scfiTableAlias) {
      StringBuilder query = new StringBuilder();
      if (userRequestedLocations.size() == 1) {
         query.append(" AND ");
         query.append(getSQRTBasedDistanceStringForSingleLocation(userRequestedLocations.get(0), scfiTableAlias));
         query.append(" <= ").append(getSQRTBasedDistanceLimit(distanceLimit));
      } else if (userRequestedLocations.size() > 1 && useDbFunctionForMultipleLocationQuery) {
         query.append(" AND ");
         query.append(getMinDistanceUserFunctionCallAsString(userRequestedLocations, scfiTableAlias,
                  dbFunctionNameForMultipleLocationQuery));
         query.append(" <= ").append(getSQRTBasedDistanceLimit(distanceLimit));
      }
      return query.toString();
   }

   @Override
   public String bulidKeyWordSearchFilterQuery (Long contextId, String orderByClause, String coreSearchQuery,
            boolean applyKeyWordSearchFilter, List<String> selectColumnNames, boolean isLocationBased) {
      String finalQuery = "";
      if (applyKeyWordSearchFilter) {
         String selectColumns = prepareSelectedColumnNamesWithAlias(selectColumnNames, "u");
         finalQuery = "SELECT u.semantified_content_id";

         finalQuery = finalQuery + "," + selectColumns;

         finalQuery = finalQuery + " u.semantified_content_date, u.SEMANTIC_SCORE, u.MATCHED_FACTOR";

         if (isLocationBased) {
            finalQuery = finalQuery + ", u.location_display_name";
         }

         finalQuery = finalQuery + ", AVG(kwm.match_score) AS KEY_WORD_MATCH_SCORE";
         finalQuery = finalQuery + " FROM sem_content_kw_match kwm RIGHT JOIN " + "(" + coreSearchQuery
                  + ") AS u ON u.semantified_content_id = kwm.semantified_content_id " + "AND kwm.context_id = "
                  + contextId + " AND kwm.query_id = :userQueryId GROUP BY u.semantified_content_id " + orderByClause;
      } else {
         finalQuery = coreSearchQuery + " " + orderByClause;
      }
      return finalQuery;
   }

   @Override
   public String bulidPerfectMatchQuery (String coreSearchQuery, boolean usingLocationJoinQuery,
            String joinQueryResultAlias) {
      String selectClause = "SELECT COUNT(U.semantified_content_id) AS TOTAL_COUNT FROM (";
      String perfectMatchCondition = " HAVING MATCHED_FACTOR = 1 AND SEMANTIC_SCORE = 100";
      if (usingLocationJoinQuery) {
         int beginIndex = coreSearchQuery.indexOf(") AS " + joinQueryResultAlias);
         String queryTillGroupBy = coreSearchQuery.substring(0, beginIndex);
         String queryAfterGroupBy = coreSearchQuery.substring(beginIndex, coreSearchQuery.length());
         return selectClause + queryTillGroupBy + perfectMatchCondition + queryAfterGroupBy + ") AS U";
      } else {
         return selectClause + coreSearchQuery + perfectMatchCondition + ") AS U";
      }

   }

   @Override
   public String bulidMightMatchQuery (String coreSearchQuery, boolean usingLocationJoinQuery,
            String joinQueryResultAlias) {
      String selectClause = "SELECT COUNT(U.semantified_content_id) AS TOTAL_COUNT FROM (";
      String mightMatchCondition = " HAVING MATCHED_FACTOR = 1 AND SEMANTIC_SCORE < 100";
      if (usingLocationJoinQuery) {
         int beginIndex = coreSearchQuery.indexOf(") AS " + joinQueryResultAlias);
         String queryTillGroupBy = coreSearchQuery.substring(0, beginIndex);
         String queryAfterGroupBy = coreSearchQuery.substring(beginIndex, coreSearchQuery.length());
         return selectClause + queryTillGroupBy + mightMatchCondition + queryAfterGroupBy + ") AS U";
      } else {
         return selectClause + coreSearchQuery + mightMatchCondition + ") AS U";
      }
   }

   @Override
   public String bulidTotalCountQuery (String coreSearchQuery) {
      String selectClause = "SELECT COUNT(U.semantified_content_id) AS TOTAL_COUNT FROM (";
      String totalCountQuery = selectClause + coreSearchQuery + ") AS U";
      return totalCountQuery;
   }

   @Override
   public String buildDeleteDedupOnTargetTempTableQuery (QueryTable queryTable) {
      ISQLAdaptor sqlAdaptor = SQLAdaptorFactory.getInstance().getSQLAdaptor(getProviderTypeValue());
      String tableName = sqlAdaptor.createTableRepresentationQueryTableColumn(queryTable, false);
      return "Delete cttn1 from " + tableName + " cttn1," + tableName
               + " cttn2 where cttn1.url = cttn2.url and cttn1.id > cttn2.id";
   }

   @Override
   public String buildInsertSourceContentFromTempTableQuery (QueryTable queryTable) {
      ISQLAdaptor sqlAdaptor = SQLAdaptorFactory.getInstance().getSQLAdaptor(getProviderTypeValue());
      String tableName = sqlAdaptor.createTableRepresentationQueryTableColumn(queryTable, false);
      return " insert into source_content (url, title, description, source, context_id, added_date, source_item_id, source_server_id)"
               + "select url, title, description, source, context_id, added_date, source_item_id, source_server_id from "
               + tableName + " order by id";
   }
}
