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


package com.execue.querygen.service.impl.sas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.querygen.QueryClauseElement;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryStructure;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.QueryClauseType;
import com.execue.core.common.type.QueryCombiningType;
import com.execue.core.common.type.QueryElementType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.QueryFormatUtility;
import com.execue.querygen.service.impl.SQLQueryGenerationServiceImpl;

/**
 * SAS specific routines will be here
 * 
 * @author Nitesh Khandelwal
 * @version 1.0
 */
public class SASQueryGenerationServiceImpl extends SQLQueryGenerationServiceImpl {

   private static final Logger logger                                   = Logger
                                                                                 .getLogger(SASQueryGenerationServiceImpl.class);

   private static final String TEMP_STRING_BASED_DATE_FORMAT            = "MONYY8.";
   private static final String TEMP_STRING_BASED_DATE_FORMAT_MONTH_PART = "6,3";
   private static final String TEMP_STRING_BASED_DATE_FORMAT_YEAR_PART  = "1,4";

   // TODO : -RG- Above three constants are need to solve the problem of "2000 MAR" 
   //   to be treated as Date with a format of "MAR 2000", which is not a straight forward way

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.querygen.service.impl.SQLQueryGenerationServiceImpl#enhanceQueryStructure(com.execue.core.common.bean.querygen.QueryStructure)
    */
   @Override
   protected QueryStructure enhanceQueryStructure (QueryStructure queryStructure) {

      // This method resets the clauses as per the SAS provider query syntax
      // Eg: In SAS group by clause CASE statement is misbehaving, so use CASE stmt alias
      updateQueryStructure(queryStructure);

      queryStructure = handleRollupQuery(queryStructure);

      return queryStructure;
   }

   /**
    * If the rollup query flag is set, then this method updates and returns the modified query structure which has 
    * necessary components for rollup query support in SAS.  
    *  
    * It returns the same query structure if the rollup query flag is not set  
    * 
    * @param queryStructure
    * @return the modified QueryStructure 
    */
   @Override
   protected QueryStructure handleRollupQuery (QueryStructure queryStructure) {
      if (!queryStructure.isRollupQuery()) {
         return queryStructure;
      }

      // Initial Input Query:

      /* SELECT EIP70.PERF_YR AS FOB80,EIP70.PERF_QTR AS XVC51,EIP70.PERF_YM AS ANZ60,AVG(EIP70.MRC_AMT) AS PGT83,COUNT(LWN91.ACCOUNT_ID) AS EGV66 
       FROM  BILLING.BILLING2 EIP70 INNER JOIN BILLING.ACCOUNT2 LWN91 ON EIP70.ACCOUNT_ID=LWN91.ACCOUNT_ID 
       WHERE EIP70.PERF_YR =2006   
       GROUP BY EIP70.PERF_YR,EIP70.PERF_QTR,EIP70.PERF_YM 
       ORDER BY EIP70.PERF_YR ASC,EIP70.PERF_QTR ASC,EIP70.PERF_YM ASC*/

      // Modified Output Query:
      /*      Select a.yr, a.qtr, a.ym, a.mrc_amt_sum, a.mrc_amt_avg from (
                     Select perf_yr as yr, perf_qtr as qtr, perf_ym as ym, sum(mrc_amt) as mrc_amt_sum, avg(mrc_amt) as mrc_amt_avg
                     From billing.billing2
                     where perf_yr = 2006
                     group by perf_yr, perf_qtr, perf_ym
                     union all
                     Select perf_yr as yr, perf_qtr as qtr, '' as ym, sum(mrc_amt) as mrc_amt_sum, avg(mrc_amt) as mrc_amt_avg
                     From billing.billing2
                     where perf_yr = 2006
                     group by perf_yr, perf_qtr
                     union all
                     Select perf_yr as yr, '' as qtr, '' as ym, sum(mrc_amt) as mrc_amt_sum, avg(mrc_amt) as mrc_amt_avg
                     From billing.billing2
                     where perf_yr = 2006
                     group by perf_yr
                  ) a
                  order by a.yr, a.qtr, a.ym

      */

      // Get all the existing selects and group by elements
      List<QueryClauseElement> existingGroupElements = queryStructure.getGroupElements();
      List<QueryClauseElement> existingSelectElements = queryStructure.getSelectElements();

      // List to maintain the combining inner query structures
      List<QueryStructure> combiningQueryStructures = new ArrayList<QueryStructure>();

      // initialize the size and counter variables  
      int groupByClauseElementSize = existingGroupElements.size();
      int limitCounter = groupByClauseElementSize;

      // We need to prepare as many query structure as the number of group by elements
      for (int index = 1; index <= groupByClauseElementSize; index++) {

         // Get the modified from elements
         List<QueryClauseElement> selectElements = getModifiedSelectElements(existingSelectElements,
                  groupByClauseElementSize, limitCounter);

         List<QueryClauseElement> fromElements = queryStructure.getFromElements();
         List<QueryClauseElement> joinElements = queryStructure.getJoinElements();
         List<QueryClauseElement> whereElements = queryStructure.getWhereElements();

         // Get the modified group elements based on the limit counter
         List<QueryClauseElement> groupElements = getModifiedGroupElements(existingGroupElements, limitCounter);

         List<QueryClauseElement> havingElements = queryStructure.getHavingElements();
         QueryClauseElement limitElement = queryStructure.getLimitElement();

         QueryStructure innerQueryStructure = new QueryStructure();
         innerQueryStructure.setSelectElements(selectElements);
         innerQueryStructure.setFromElements(fromElements);
         innerQueryStructure.setJoinElements(joinElements);
         innerQueryStructure.setWhereElements(whereElements);
         innerQueryStructure.setGroupElements(groupElements);
         innerQueryStructure.setHavingElements(havingElements);
         innerQueryStructure.setLimitElement(limitElement);
         innerQueryStructure.setOrderElements(null); // Empty the order elements for inner query structure
         innerQueryStructure.setRollupQuery(false); // Set the rollup query flag to false for inner query structure

         // Add the inner query structure to the list
         combiningQueryStructures.add(innerQueryStructure);

         // Decrement the counter
         limitCounter--;
      }

      // Prepare the main inner query structure with all above list of inner query structures as UNION combination type 
      QueryStructure innerQueryStructure = new QueryStructure();
      innerQueryStructure.setCombiningQueryStructures(combiningQueryStructures);
      innerQueryStructure.setCombiningType(QueryCombiningType.UNION);

      // Prepare the outer query structure elements

      // Prepare the select elements
      QueryClauseElement selectClauseElement = new QueryClauseElement();
      selectClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      String selectAliasString = QueryFormatUtility.prepareSelectClauseAliasString(queryStructure.getSelectElements());
      selectClauseElement.setSimpleString(selectAliasString);
      List<QueryClauseElement> selectElements = new ArrayList<QueryClauseElement>();
      selectElements.add(selectClauseElement);

      // Prepare the from elements
      QueryClauseElement fromClauseElement = new QueryClauseElement();
      fromClauseElement.setQueryElementType(QueryElementType.SUB_QUERY);
      fromClauseElement.setSubQuery(innerQueryStructure);
      fromClauseElement.setAlias(SAS_ROLLUP_QUERY_ALIAS_NAME);

      List<QueryClauseElement> fromElements = new ArrayList<QueryClauseElement>();
      fromElements.add(fromClauseElement);

      // Prepare the order by elements
      String orderByAliasString = QueryFormatUtility.prepareOrderByClauseAliasString(queryStructure.getOrderElements());
      QueryClauseElement orderByClauseElement = new QueryClauseElement();
      orderByClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      orderByClauseElement.setSimpleString(orderByAliasString);

      List<QueryClauseElement> orderElements = new ArrayList<QueryClauseElement>();
      orderElements.add(orderByClauseElement);

      // Prepare the outer query structure  
      QueryStructure outerQueryStructure = new QueryStructure();
      outerQueryStructure.setSelectElements(selectElements);
      outerQueryStructure.setFromElements(fromElements);
      outerQueryStructure.setOrderElements(orderElements);

      return outerQueryStructure;
   }

   private List<QueryClauseElement> getModifiedSelectElements (List<QueryClauseElement> existingFromElements,
            int groupByClauseElementCounter, int limitCounter) {
      List<QueryClauseElement> queryClauseElements = new ArrayList<QueryClauseElement>();
      int index = 0;
      for (; index < groupByClauseElementCounter; index++) {
         QueryClauseElement queryClauseElement = existingFromElements.get(index);
         if (index >= limitCounter) {
            // Nullify the select clause value once we reach the limit counter
            QueryClauseElement newQueryClauseElement = QueryFormatUtility.getSimpleQueryClauseElement("",
                     queryClauseElement.getAlias());
            queryClauseElements.add(newQueryClauseElement);
         } else {
            queryClauseElements.add(queryClauseElement);
         }
      }

      // Add the remaining select elements as it is
      while (index < existingFromElements.size()) {
         queryClauseElements.add(existingFromElements.get(index++));
      }

      return queryClauseElements;
   }

   private List<QueryClauseElement> getModifiedGroupElements (List<QueryClauseElement> existingGroupElements,
            int groupByClauseElementCounter) {
      List<QueryClauseElement> queryClauseElements = new ArrayList<QueryClauseElement>();
      int counter = 0;
      for (int index = 0; index < groupByClauseElementCounter; index++) {
         QueryClauseElement queryClauseElement = existingGroupElements.get(index);
         if (counter >= groupByClauseElementCounter) {
            break;
         }
         queryClauseElements.add(queryClauseElement);
         counter++;
      }
      return queryClauseElements;
   }

   private void updateQueryStructure (QueryStructure queryStructure) {
      List<QueryClauseElement> selectElements = queryStructure.getSelectElements();
      List<QueryClauseElement> groupByElements = queryStructure.getGroupElements();
      List<QueryClauseElement> orderByElements = queryStructure.getOrderElements();
      if (CollectionUtils.isEmpty(groupByElements)) {
         return;
      }

      Map<String, QueryClauseElement> selectClauseElementByStringRepresentation = prepareQueryClauseElementByStringRepresentationMap(
               selectElements, QueryClauseType.SELECT);

      Map<String, QueryClauseElement> groupClauseElementByStringRepresentation = prepareQueryClauseElementByStringRepresentationMap(
               groupByElements, QueryClauseType.GROUPBY);

      Map<String, QueryClauseElement> orderClauseElementByStringRepresentation = prepareQueryClauseElementByStringRepresentationMap(
               orderByElements, QueryClauseType.ORDERBY);

      for (Entry<String, QueryClauseElement> groupElementByStringRepresentation : groupClauseElementByStringRepresentation
               .entrySet()) {
         QueryClauseElement groupByClauseElement = groupElementByStringRepresentation.getValue();
         if (groupByClauseElement.getQueryElementType() != QueryElementType.CASE_STATEMENT) {
            continue;
         }
         String caseStatementString = groupElementByStringRepresentation.getKey();
         QueryClauseElement selectClauseElement = selectClauseElementByStringRepresentation.get(caseStatementString);
         QueryClauseElement orderByClauseElement = orderClauseElementByStringRepresentation.get(caseStatementString);

         // Update the group by element
         groupByClauseElement.setSimpleString(selectClauseElement.getAlias());
         groupByClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
         groupByClauseElement.setCaseStatement(new ArrayList<String>());

         // Update the order by element
         if (orderByClauseElement != null) {
            orderByClauseElement.setAlias(null);
            orderByClauseElement.setSimpleString(selectClauseElement.getAlias());
            orderByClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
            orderByClauseElement.setCaseStatement(new ArrayList<String>());
         }
      }
   }

   @Override
   protected String getSTDDEV (StatType functionName, String columnString) {
      StringBuilder sb = new StringBuilder();
      sb.append(SAS_SQL_STDDEV).append(SQL_FUNCTION_START_WRAPPER).append(columnString)
               .append(SQL_FUNCTION_END_WRAPPER);
      return sb.toString();
   }

   protected String getMinMax (StatType functionName, String columnString, DataType columnDataType) {
      if (DataType.DATE == columnDataType || DataType.DATETIME == columnDataType || DataType.TIME == columnDataType) {
         StringBuilder sb = new StringBuilder();
         sb.append(functionName.getValue()).append(SQL_FUNCTION_START_WRAPPER).append(columnString).append(
                  SQL_FUNCTION_END_WRAPPER).append(" ").append(getFormatIdentifierBasedOnDataType(columnDataType));
         return sb.toString();
      }
      return super.getMinMax(functionName, columnString, columnDataType);
   }

   /**
    * TODO: -RG- Other formats for TIME, DATETIME needs to be corrected
    * 
    * @param columnDataType
    * @return
    */
   private String getFormatIdentifierBasedOnDataType (DataType columnDataType) {
      String format = "";
      switch (columnDataType) {
         case DATETIME:
            format = SAS_SQL_DATE_FORMAT_DEFAULT_IDENTIFIER;
            break;
         case DATE:
            format = SAS_SQL_DATE_FORMAT_DEFAULT_IDENTIFIER;
            break;
         default:
            format = SAS_SQL_DATE_FORMAT_DEFAULT_IDENTIFIER;
            break;
      }
      return format;
   }

   @Override
   protected String prepareDateFormOfValue (QueryValue queryValue, String columnDBDateFormat) {
      StringBuilder stringBuilder = new StringBuilder();
      String value = queryValue.getValue();
      if (TEMP_STRING_BASED_DATE_FORMAT.equalsIgnoreCase(columnDBDateFormat)) {
         String[] dateComponents = value.split(SQL_SPACE_DELIMITER);
         if (dateComponents.length > 1) {
            value = dateComponents[1] + SQL_SPACE_DELIMITER + dateComponents[0];
         }
      }
      stringBuilder.append(SAS_STRING_TO_DATE_FUNCTION).append(SQL_SUBQUERY_START_WRAPPER).append(QUOTE).append(
               value).append(QUOTE).append(SQL_ENTITY_SEPERATOR).append(columnDBDateFormat)
               .append(SQL_SUBQUERY_END_WRAPPER);
      return stringBuilder.toString();
   }

   @Override
   protected String prepareColumnForStringToDateHandling (String columnString, String columnDBDateFormat) {
      StringBuilder sb = new StringBuilder();
      if (TEMP_STRING_BASED_DATE_FORMAT.equalsIgnoreCase(columnDBDateFormat)) {
         /*
          *  input(cat(substr(XBK76.Calendar_Month,6,3), ' ', substr(XBK76.Calendar_Month,1,4)), MONYY8.)
          */
         sb.append(SAS_STRING_TO_DATE_FUNCTION).append(SQL_SUBQUERY_START_WRAPPER);
         sb.append(SQL_SAS_CAT_FUNCTION).append(SQL_SUBQUERY_START_WRAPPER);
         sb.append(SQL_SUBSTR_FUNCTION).append(SQL_SUBQUERY_START_WRAPPER);
         sb.append(columnString);
         sb.append(SQL_ENTITY_SEPERATOR);
         sb.append(TEMP_STRING_BASED_DATE_FORMAT_MONTH_PART);
         sb.append(SQL_SUBQUERY_END_WRAPPER);
         sb.append(SQL_ENTITY_SEPERATOR);
         sb.append(QUOTE);
         sb.append(SQL_SPACE_DELIMITER);
         sb.append(QUOTE);
         sb.append(SQL_ENTITY_SEPERATOR);
         sb.append(SQL_SUBSTR_FUNCTION).append(SQL_SUBQUERY_START_WRAPPER);
         sb.append(columnString);
         sb.append(SQL_ENTITY_SEPERATOR);
         sb.append(TEMP_STRING_BASED_DATE_FORMAT_YEAR_PART);
         sb.append(SQL_SUBQUERY_END_WRAPPER);
         sb.append(SQL_SUBQUERY_END_WRAPPER);
         sb.append(SQL_ENTITY_SEPERATOR);
         sb.append(columnDBDateFormat);
         sb.append(SQL_SUBQUERY_END_WRAPPER);
      } else {
         /*
          *  INPUT(XBK76.Calendar_Month,MONYY8.) 
          */
         sb.append(SAS_STRING_TO_DATE_FUNCTION).append(SQL_SUBQUERY_START_WRAPPER).append(columnString).append(
                  SQL_ENTITY_SEPERATOR).append(columnDBDateFormat).append(SQL_SUBQUERY_END_WRAPPER);
      }
      return sb.toString();
   }
   
   protected String applyRoundLimit (String lhsColumnString, QueryColumn roundFuntionTargetColumn) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(lhsColumnString);
      if (roundFuntionTargetColumn != null) {
         stringBuilder = new StringBuilder();
         stringBuilder.append(SQL_ROUND_FUNCTION).append(SQL_FUNCTION_START_WRAPPER).append(lhsColumnString).append(
                  SQL_ENTITY_SEPERATOR).append(getRoundingDecimalRepresentation(roundFuntionTargetColumn.getScale())).append(SQL_FUNCTION_END_WRAPPER);
      }
      return stringBuilder.toString();
   }

   private String getRoundingDecimalRepresentation (int scale) {
      StringBuilder roundingDecimalRepresentation = new StringBuilder("0.");
      for (int i = 1; i < scale; i++) {
         roundingDecimalRepresentation.append("0");
      }
      roundingDecimalRepresentation.append("1");
      return roundingDecimalRepresentation.toString();
   }
   
   @Override
   protected String prepareColumnForDateToStringHandling (String columnString, String columnDBDateFormat) {
      StringBuilder sb = new StringBuilder();
      sb.append(columnString);
      return sb.toString();
   }
}