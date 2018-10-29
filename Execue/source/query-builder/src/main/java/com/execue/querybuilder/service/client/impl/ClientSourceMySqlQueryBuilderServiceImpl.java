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
 * MySql specific query builder routines come here
 * 
 * @author Vishay
 * @version 1.0
 * @since 03/01/12
 */
public class ClientSourceMySqlQueryBuilderServiceImpl extends ClientSourceQueryBuilderServiceImpl {

   @Override
   public String getDynamicRangesQuery (String rangeColumnAlias, String innerQueryString, int[] band) {
      int[] modifiedBand = ClientSourceQueryBuilderServiceHelper.resetBandValuesIfRequired(band);
      int startingRowPointer = modifiedBand[0];
      int toBeRetrievedRowCount = modifiedBand[1] - modifiedBand[0];
      StringBuffer queryString = new StringBuffer();
      queryString.append(SQL_SELECT_CLAUSE).append(SQL_SPACE_DELIMITER);
      queryString.append(SQL_MIN_FUNCTION).append(SQL_FUNCTION_START_WRAPPER).append(rangeColumnAlias).append(
               SQL_FUNCTION_END_WRAPPER);
      queryString.append(SQL_ENTITY_SEPERATOR).append(SQL_SPACE_DELIMITER);
      queryString.append(SQL_MAX_FUNCTION).append(SQL_FUNCTION_START_WRAPPER).append(rangeColumnAlias).append(
               SQL_FUNCTION_END_WRAPPER);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(SQL_JOIN_FROM_CLAUSE));
      queryString.append(SQL_SUBQUERY_START_WRAPPER).append(innerQueryString);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(MYSQL_LIMIT_CLAUSE)).append(
               startingRowPointer).append(SQL_ENTITY_SEPERATOR).append(SQL_SPACE_DELIMITER).append(
               toBeRetrievedRowCount);
      queryString.append(SQL_SUBQUERY_END_WRAPPER).append(SQL_SPACE_DELIMITER).append(
               MYSQL_LIMIT_INNER_QUERY_ALIAS_NAME);
      return queryString.toString();
   }

}
