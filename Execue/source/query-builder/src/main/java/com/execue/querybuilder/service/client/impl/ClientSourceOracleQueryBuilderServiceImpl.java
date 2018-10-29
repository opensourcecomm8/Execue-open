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
 * Oracle specific query builder routines come here
 * 
 * @author Vishay
 * @version 1.0
 * @since 03/01/12
 */
public class ClientSourceOracleQueryBuilderServiceImpl extends ClientSourceQueryBuilderServiceImpl {

   @Override
   public String getDynamicRangesQuery (String rangeColumnAlias, String innerQueryString, int[] band) {
      StringBuffer queryString = new StringBuffer();
      queryString.append(SQL_SELECT_CLAUSE).append(SQL_SPACE_DELIMITER);
      queryString.append(SQL_MIN_FUNCTION).append(SQL_FUNCTION_START_WRAPPER).append(rangeColumnAlias).append(
               SQL_FUNCTION_END_WRAPPER);
      queryString.append(SQL_ENTITY_SEPERATOR).append(SQL_SPACE_DELIMITER);
      queryString.append(SQL_MAX_FUNCTION).append(SQL_FUNCTION_START_WRAPPER).append(rangeColumnAlias).append(
               SQL_FUNCTION_END_WRAPPER);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(SQL_JOIN_FROM_CLAUSE));
      queryString.append(SQL_SUBQUERY_START_WRAPPER);
      queryString.append(SQL_SELECT_CLAUSE).append(SQL_SPACE_DELIMITER).append(rangeColumnAlias).append(
               SQL_ENTITY_SEPERATOR).append(SQL_SPACE_DELIMITER);
      queryString.append(ORACLE_LIMIT_CLAUSE).append(SQL_SPACE_DELIMITER).append(ORACLE_ROWNUM_KEYWORD_ALIAS_NAME);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(SQL_JOIN_FROM_CLAUSE));
      queryString.append(SQL_SUBQUERY_START_WRAPPER).append(innerQueryString);
      queryString.append(SQL_SUBQUERY_END_WRAPPER);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(SQL_WHERE_CLAUSE)).append(
               ORACLE_LIMIT_CLAUSE).append(
               ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(LESSER_THAN_EQUAL_OPERATOR)).append(band[1]);
      queryString.append(SQL_SUBQUERY_END_WRAPPER);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(SQL_WHERE_CLAUSE)).append(
               ORACLE_ROWNUM_KEYWORD_ALIAS_NAME);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(GREATER_THAN_EQUAL_OPERATOR)).append(
               band[0]);
      return queryString.toString();
   }

}
