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
 * MsSql specific query builder routines come here
 * 
 * @author Vishay
 * @version 1.0
 * @since 03/01/12
 */
public class ClientSourceMsSqlQueryBuilderServiceImpl extends ClientSourceQueryBuilderServiceImpl {

   @Override
   public String getDynamicRangesQuery (String rangeColumnAlias, String innerQueryString, int[] band) {
      int[] modifiedBand = ClientSourceQueryBuilderServiceHelper.resetBandValuesIfRequired(band);
      int workingSetCount = modifiedBand[1];
      int finalSetCount = modifiedBand[1] - modifiedBand[0];
      String INNER_QUERY_ALIAS = "INQ";
      StringBuffer queryString = new StringBuffer();
      queryString.append(SQL_SELECT_CLAUSE).append(SQL_SPACE_DELIMITER);
      queryString.append(SQL_MIN_FUNCTION).append(SQL_FUNCTION_START_WRAPPER).append(rangeColumnAlias).append(
               SQL_FUNCTION_END_WRAPPER);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(SQL_ALIAS)).append(SQL_MIN_FUNCTION_ALIAS);
      queryString.append(SQL_ENTITY_SEPERATOR).append(SQL_SPACE_DELIMITER);
      queryString.append(SQL_MAX_FUNCTION).append(SQL_FUNCTION_START_WRAPPER).append(rangeColumnAlias).append(
               SQL_FUNCTION_END_WRAPPER);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(SQL_ALIAS)).append(SQL_MAX_FUNCTION_ALIAS);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(SQL_JOIN_FROM_CLAUSE)).append(
               SQL_SUBQUERY_START_WRAPPER);
      // start of first 'top n' query
      queryString.append(SQL_SELECT_CLAUSE).append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(MSSQL_LIMIT_CLAUSE));
      queryString.append(finalSetCount).append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(rangeColumnAlias));
      queryString.append(SQL_JOIN_FROM_CLAUSE).append(SQL_SPACE_DELIMITER);
      queryString.append(SQL_SUBQUERY_START_WRAPPER);
      // add the 'top m' to the inner query string
      queryString.append(ClientSourceQueryBuilderServiceHelper.modifyToAddTopKeyword(workingSetCount, innerQueryString)).append(
               SQL_SUBQUERY_END_WRAPPER);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(INNER_QUERY_ALIAS));
      queryString.append(SQL_ORDER_BY_CLAUSE).append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(rangeColumnAlias))
               .append(SQL_ORDER_BY_DESC);
      queryString.append(SQL_SUBQUERY_END_WRAPPER).append(SQL_SPACE_DELIMITER).append(
               MSSQL_LIMIT_INNER_QUERY_ALIAS_NAME);
      return queryString.toString();
   }

}
