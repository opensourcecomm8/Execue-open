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


package com.execue.querybuilder.service.client.impl;

import com.execue.querybuilder.service.client.helper.ClientSourceQueryBuilderServiceHelper;

/**
 * Teradata specific query builder routines come here
 * 
 * @author Vishay
 * @version 1.0
 * @since 03/01/12
 */
public class ClientSourceTeradataQueryBuilderServiceImpl extends ClientSourceQueryBuilderServiceImpl {

   @Override
   public String getDynamicRangesQuery (String rangeColumnAlias, String innerQueryString, int[] band) {
      StringBuffer queryString = new StringBuffer();
      String INNER_QUERY_ALIAS = "INQ";
      int[] modifiedBand = ClientSourceQueryBuilderServiceHelper.resetBandValuesIfRequired(band);
      int startRowNum = modifiedBand[0] + 1;
      int endRowNum = modifiedBand[1];

      // process the ORDER BY clause from the inner query
      int iOrderByIndex = innerQueryString.indexOf(SQL_ORDER_BY_CLAUSE);
      String innerQueryStringWithoutOrderBy = innerQueryString.substring(0, (iOrderByIndex - 1));
      String orderByClause = innerQueryString.substring(iOrderByIndex);
      StringBuffer sbModifiedOrderByClause = new StringBuffer();
      sbModifiedOrderByClause.append(SQL_ORDER_BY_CLAUSE).append(
               ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(rangeColumnAlias)).append(
               ClientSourceQueryBuilderServiceHelper.getOrderByType(orderByClause));

      // query pattern:
      // select min(<rangeColAlias>) as <MIN_VAL_ALIAS>, max(<rangeColAlias>) as <MAX_VAL_ALIAS> from
      // (select <rangeColAlias> from
      // (<innerQueryWithoutOrderByClause>) <innerQueryAlias>
      // qualify row_number() over (order by <rangeColAlias> <sortOrder>) between <startRowNum> and <endRowNum>)
      // <outerQueryAlias>
      queryString.append(SQL_SELECT_CLAUSE).append(SQL_SPACE_DELIMITER);
      queryString.append(SQL_MIN_FUNCTION).append(SQL_FUNCTION_START_WRAPPER).append(rangeColumnAlias).append(
               SQL_FUNCTION_END_WRAPPER);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(SQL_ALIAS)).append(
               SQL_MIN_FUNCTION_ALIAS);
      queryString.append(SQL_ENTITY_SEPERATOR).append(SQL_SPACE_DELIMITER);
      queryString.append(SQL_MAX_FUNCTION).append(SQL_FUNCTION_START_WRAPPER).append(rangeColumnAlias).append(
               SQL_FUNCTION_END_WRAPPER);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(SQL_ALIAS)).append(
               SQL_MAX_FUNCTION_ALIAS);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(SQL_JOIN_FROM_CLAUSE));

      queryString.append(SQL_SUBQUERY_START_WRAPPER).append(SQL_SELECT_CLAUSE).append(
               ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(rangeColumnAlias));
      queryString.append(SQL_JOIN_FROM_CLAUSE).append(SQL_SPACE_DELIMITER).append(SQL_SUBQUERY_START_WRAPPER);

      queryString.append(innerQueryStringWithoutOrderBy).append(SQL_SUBQUERY_END_WRAPPER).append(SQL_SPACE_DELIMITER)
               .append(INNER_QUERY_ALIAS);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(TERADATA_QUALIFY_KEYWORD));
      queryString.append(TERADATA_ROW_NUMBER_FUNCTION).append(SQL_FUNCTION_START_WRAPPER).append(
               SQL_FUNCTION_END_WRAPPER);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(SQL_OVER_KEYWORD));
      queryString.append(SQL_FUNCTION_START_WRAPPER).append(sbModifiedOrderByClause).append(SQL_FUNCTION_END_WRAPPER);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(SQL_BETWEEN_FUNCTION));
      queryString.append(startRowNum).append(
               ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(WHERE_CLAUSE_SEPERATOR)).append(endRowNum);
      queryString.append(SQL_SUBQUERY_END_WRAPPER).append(SQL_SPACE_DELIMITER).append(
               TERADATA_LIMIT_INNER_QUERY_ALIAS_NAME);
      return queryString.toString();
   }

}
