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
 * SAS specific query builder routines come here
 * 
 * @author Nitesh
 * @version 1.0
 * @since 03/02/12
 */
public class ClientSourceSASQueryBuilderServiceImpl extends ClientSourceQueryBuilderServiceImpl {

   @Override
   public String getDynamicRangesQuery (String rangeColumnAlias, String innerQueryString, int[] band) {

      /*
       * INPUT QUERY:
       * =============
       * SELECT DISTINCT EIP70.CYCL_BAL AS MQF91 FROM BILLING.BILLING2 EIP70 INNER JOIN BILLING.ACCOUNT2 LWN91 ON EIP70.ACCOUNT_ID=LWN91.ACCOUNT_ID WHERE  EIP70.PERF_YM BETWEEN 200611 AND 2007101
       * ORDER BY EIP70.CYCL_BAL;
       * 
       * OUTPUT QUERY:
       * ============ 
       * SELECT MIN(MQF91), MAX(MQF91) FROM (SELECT DISTINCT EIP70.CYCL_BAL AS MQF91 FROM   BILLING.BILLING2 EIP70 INNER JOIN BILLING.ACCOUNT2 LWN91 ON EIP70.ACCOUNT_ID=LWN91.ACCOUNT_ID WHERE  EIP70.PERF_YM BETWEEN 200611 AND 2007101) sasiq 
       * WHERE MONOTONIC() BETWEEN 1 AND 1067
       *  
       * */

      int[] modifiedBand = ClientSourceQueryBuilderServiceHelper.resetBandValuesIfRequired(band);
      int startRowNum = modifiedBand[0] + 1;
      int endRowNum = modifiedBand[1];

      // Remove the ORDER BY clause from the inner query if it exists
      int orderByClauseIndex = innerQueryString.indexOf(SQL_ORDER_BY_CLAUSE);
      StringBuilder modifiedInnerQueryString = new StringBuilder();
      if (orderByClauseIndex > 0) {
         modifiedInnerQueryString.append(innerQueryString.substring(0, orderByClauseIndex));
      } else {
         // Do not modify anything
         modifiedInnerQueryString.append(innerQueryString);
      }

      // Prepare the limit clause condition using SAS MONOTONIC 
      StringBuilder limitClauseConditionUsingMonotonic = new StringBuilder();
      limitClauseConditionUsingMonotonic.append(SQL_SPACE_DELIMITER).append(SAS_LIMIT_CLAUSE_MONOTONIC).append(
               SQL_FUNCTION_START_WRAPPER).append(SQL_FUNCTION_END_WRAPPER).append(SQL_SPACE_DELIMITER).append(
               SQL_BETWEEN_FUNCTION).append(SQL_SPACE_DELIMITER).append(startRowNum).append(SQL_SPACE_DELIMITER)
               .append(WHERE_CLAUSE_SEPERATOR).append(SQL_SPACE_DELIMITER).append(endRowNum);

      // Prepare the final query string which uses SAS monotonic function for limiting the result
      StringBuffer queryString = new StringBuffer();
      queryString.append(SQL_SELECT_CLAUSE).append(SQL_SPACE_DELIMITER);
      queryString.append(SQL_MIN_FUNCTION).append(SQL_FUNCTION_START_WRAPPER).append(rangeColumnAlias).append(
               SQL_FUNCTION_END_WRAPPER);
      queryString.append(SQL_ENTITY_SEPERATOR).append(SQL_SPACE_DELIMITER);
      queryString.append(SQL_MAX_FUNCTION).append(SQL_FUNCTION_START_WRAPPER).append(rangeColumnAlias).append(
               SQL_FUNCTION_END_WRAPPER);
      queryString.append(ClientSourceQueryBuilderServiceHelper.surroundWithSpaces(SQL_JOIN_FROM_CLAUSE));
      queryString.append(SQL_SUBQUERY_START_WRAPPER).append(modifiedInnerQueryString);
      queryString.append(SQL_SUBQUERY_END_WRAPPER).append(SQL_SPACE_DELIMITER).append(SAS_LIMIT_INNER_QUERY_ALIAS_NAME);
      queryString.append(SQL_SPACE_DELIMITER).append(SQL_WHERE_CLAUSE).append(limitClauseConditionUsingMonotonic);
      return queryString.toString();
   }
}
