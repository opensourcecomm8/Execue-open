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


package com.execue.querygen.service.impl.mysql;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.querygen.QueryClauseElement;
import com.execue.core.common.bean.querygen.QueryStructure;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.type.QueryClauseType;
import com.execue.core.common.type.QueryElementType;
import com.execue.core.common.util.QueryFormatUtility;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.querygen.service.impl.SQLQueryGenerationServiceImpl;

/**
 * MySql specific query generation routines come here
 * 
 * @author Jayadev
 */
public class MySqlQueryGenerationServiceImpl extends SQLQueryGenerationServiceImpl {

   private static final Logger logger = Logger.getLogger(MySqlQueryGenerationServiceImpl.class);

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

   /* (non-Javadoc)
    * @see com.execue.querygen.service.impl.SQLQueryGenerationServiceImpl#handleRollupQuery(com.execue.core.common.bean.querygen.QueryStructure)
    */
   @Override
   protected QueryStructure handleRollupQuery (QueryStructure queryStructure) {
      if (!queryStructure.isRollupQuery()) {
         return queryStructure;
      }

      // Empty the order by
      List<QueryClauseElement> outerQueryOrderByElements = new ArrayList<QueryClauseElement>(queryStructure
               .getOrderElements());
      queryStructure.setOrderElements(new ArrayList<QueryClauseElement>());

      // Reset the simple group by with group by rollup clause 
      List<QueryClauseElement> groupElements = queryStructure.getGroupElements();
      List<String> groupByElementStrings = new ArrayList<String>();
      for (QueryClauseElement queryClauseElement : groupElements) {
         String stringRepresentation = getStringRepresentationByElementType(queryClauseElement, QueryClauseType.GROUPBY);
         groupByElementStrings.add(stringRepresentation);
      }

      String groupBySimpleString = ExecueCoreUtil.joinCollection(groupByElementStrings, SQL_ENTITY_SEPERATOR);
      String groupBySimpleStringWithRollup = groupBySimpleString + SQL_SPACE_DELIMITER + SQL_MYSQL_ROLLUP_KEYWORD;

      QueryClauseElement groupClauseElement = new QueryClauseElement();
      groupClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      groupClauseElement.setSimpleString(groupBySimpleStringWithRollup);

      List<QueryClauseElement> groupElementsWithRollup = new ArrayList<QueryClauseElement>();
      groupElementsWithRollup.add(groupClauseElement);

      queryStructure.setGroupElements(groupElementsWithRollup);

      // Prepare the outer query structure elements
      QueryClauseElement selectClauseElement = new QueryClauseElement();
      selectClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      selectClauseElement.setSimpleString(SQL_WILD_CHAR);
      List<QueryClauseElement> selectElements = new ArrayList<QueryClauseElement>();
      selectElements.add(selectClauseElement);

      QueryClauseElement fromClauseElement = new QueryClauseElement();
      fromClauseElement.setQueryElementType(QueryElementType.SUB_QUERY);
      fromClauseElement.setSubQuery(queryStructure);
      fromClauseElement.setAlias(MYSQL_ROLLUP_QUERY_ALIAS_NAME);
      List<QueryClauseElement> fromElements = new ArrayList<QueryClauseElement>();
      fromElements.add(fromClauseElement);

      String orderByAliasString = QueryFormatUtility.prepareOrderByClauseAliasString(outerQueryOrderByElements);
      QueryClauseElement orderByClauseElement = new QueryClauseElement();
      orderByClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      orderByClauseElement.setSimpleString(orderByAliasString);
      List<QueryClauseElement> orderElements = new ArrayList<QueryClauseElement>();
      orderElements.add(orderByClauseElement);

      QueryStructure outerQueryStructure = new QueryStructure();

      outerQueryStructure.setSelectElements(selectElements);
      outerQueryStructure.setFromElements(fromElements);
      outerQueryStructure.setOrderElements(orderElements);

      return outerQueryStructure;
   }
   
   @Override
   protected String prepareDateFormOfValue (QueryValue queryValue, String columnDBDateFormat) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(SQL_MYSQL_STRING_TO_DATE_FUNCTION).append(SQL_SUBQUERY_START_WRAPPER).append(QUOTE)
               .append(queryValue.getValue()).append(QUOTE).append(SQL_ENTITY_SEPERATOR).append(QUOTE).append(
                        columnDBDateFormat).append(QUOTE).append(SQL_SUBQUERY_END_WRAPPER);
      return stringBuilder.toString();
   }
   
   @Override
   protected String prepareColumnForStringToDateHandling (String columnString, String columnDBDateFormat) {
      StringBuilder sb = new StringBuilder();
      sb.append(SQL_MYSQL_STRING_TO_DATE_FUNCTION).append(SQL_SUBQUERY_START_WRAPPER).append(columnString)
               .append(SQL_ENTITY_SEPERATOR).append(QUOTE).append(columnDBDateFormat).append(QUOTE).append(
                        SQL_SUBQUERY_END_WRAPPER);
      return sb.toString();
   }
   
   @Override
   protected String prepareColumnForDateToStringHandling (String columnString, String columnDBDateFormat) {
      StringBuilder sb = new StringBuilder();
      sb.append(SQL_MYSQL_DATE_FORMAT_FUNCTION).append(SQL_SUBQUERY_START_WRAPPER).append(columnString)
               .append(SQL_ENTITY_SEPERATOR).append(QUOTE).append(columnDBDateFormat).append(QUOTE).append(
                        SQL_SUBQUERY_END_WRAPPER);
      return sb.toString();
   }
}