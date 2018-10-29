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


package com.execue.querygen.service.impl.mssql;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.querygen.QueryClauseElement;
import com.execue.core.common.bean.querygen.QueryStructure;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.type.OrderEntityType;
import com.execue.core.common.type.QueryClauseType;
import com.execue.core.common.type.QueryElementType;
import com.execue.core.common.type.StatType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.querygen.service.impl.SQLQueryGenerationServiceImpl;

/**
 * MSSQL routines specific routines will be here
 * 
 * @author Vishay
 * @version 1.0
 */
public class MsSqlQueryGenerationServiceImpl extends SQLQueryGenerationServiceImpl {

   private static final Logger logger = Logger.getLogger(MsSqlQueryGenerationServiceImpl.class);

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.querygen.service.impl.SQLQueryGenerationServiceImpl#enhanceQueryStructure(com.execue.core.common.bean.querygen.QueryStructure)
    */
   @Override
   protected QueryStructure enhanceQueryStructure (QueryStructure queryStructure) {
      queryStructure = handleRollupQuery(queryStructure);
      return queryStructure;
   }
   
   @Override
   protected QueryStructure handleRollupQuery (QueryStructure queryStructure) {
      if (!queryStructure.isRollupQuery()) {
         return queryStructure;
      }

      // Reset the simple group by with group by with rollup function clause 
      List<QueryClauseElement> groupElements = queryStructure.getGroupElements();
      List<String> groupByElementStrings = new ArrayList<String>();
      for (QueryClauseElement queryClauseElement : groupElements) {
         String stringRepresentation = getStringRepresentationByElementType(queryClauseElement, QueryClauseType.GROUPBY);
         groupByElementStrings.add(stringRepresentation);
      }

      String groupBySimpleString = ExecueCoreUtil.joinCollection(groupByElementStrings, SQL_ENTITY_SEPERATOR);
      String groupBySimpleStringWithRollup = groupBySimpleString + SQL_SPACE_DELIMITER + SQL_MSSQL_ROLLUP_KEYWORD;

      QueryClauseElement groupClauseElement = new QueryClauseElement();
      groupClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      groupClauseElement.setSimpleString(groupBySimpleStringWithRollup);

      List<QueryClauseElement> groupElementsWithRollup = new ArrayList<QueryClauseElement>();
      groupElementsWithRollup.add(groupClauseElement);

      //Reset the group by elements
      queryStructure.setGroupElements(groupElementsWithRollup);

      // Prepare the order by clause using grouping function
      List<QueryClauseElement> orderByElements = queryStructure.getOrderElements();
      List<QueryClauseElement> newOrderByElements = new ArrayList<QueryClauseElement>();
      for (QueryClauseElement orderByElement : orderByElements) {
         String simpleString = SQL_MSSQL_GROUPING_FUNCTION + SQL_FUNCTION_START_WRAPPER
                  + getStringRepresentationByElementType(orderByElement, QueryClauseType.ORDERBY)
                  + SQL_FUNCTION_END_WRAPPER;

         // Prepare the grouping order by
         QueryClauseElement groupingOrderByClauseElement = new QueryClauseElement();
         groupingOrderByClauseElement.setSimpleString(simpleString);
         groupingOrderByClauseElement.setAlias(orderByElement.getAlias());
         groupingOrderByClauseElement.setQueryElementType(orderByElement.getQueryElementType());
         groupingOrderByClauseElement.setCaseStatement(orderByElement.getCaseStatement());
         groupingOrderByClauseElement.setOrderEntityType(OrderEntityType.DESCENDING);

         // Add the grouping order by 
         newOrderByElements.add(groupingOrderByClauseElement);
         newOrderByElements.add(orderByElement);
      }

      //Reset the order by elements
      queryStructure.setOrderElements(newOrderByElements);

      return queryStructure;
   }

   @Override
   protected String getSTDDEV (StatType functionName, String columnString) {
      StringBuilder sb = new StringBuilder();
      sb.append(MSSQL_SQL_STDDEV).append(SQL_FUNCTION_START_WRAPPER).append(columnString).append(
               SQL_FUNCTION_END_WRAPPER);
      return sb.toString();
   }
   
   @Override
   protected String prepareDateFormOfValue (QueryValue queryValue, String columnDBDateFormat) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(QUOTE).append(queryValue.getValue()).append(QUOTE);
      return stringBuilder.toString();
   }
   
   @Override
   protected String prepareColumnForStringToDateHandling (String columnString, String columnDBDateFormat) {
      StringBuilder sb = new StringBuilder();
      sb.append(columnString);
      return sb.toString();
   }
   
   @Override
   protected String prepareColumnForDateToStringHandling (String columnString, String columnDBDateFormat) {
      StringBuilder sb = new StringBuilder();
      sb.append(columnString);
      return sb.toString();
   }
}
