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


package com.execue.querygen.service.impl.db2;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.querygen.QueryClauseElement;
import com.execue.core.common.bean.querygen.QueryStructure;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.OrderEntityType;
import com.execue.core.common.type.QueryClauseType;
import com.execue.core.common.type.QueryElementType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.querygen.service.impl.SQLQueryGenerationServiceImpl;

/**
 * @author prasanna
 */
public class Db2QueryGenerationServiceImpl extends SQLQueryGenerationServiceImpl {

   @Override
   protected QueryStructure enhanceQueryStructure (QueryStructure queryStructure) {
      queryStructure = handleRollupQuery(queryStructure);
      return queryStructure;
   }

   @Override
   protected String getSTDDEV (StatType functionName, String columnString) {
      StringBuilder sb = new StringBuilder();
      sb.append(DB2_SQL_STDDEV).append(SQL_FUNCTION_START_WRAPPER).append(columnString)
               .append(SQL_FUNCTION_END_WRAPPER);
      return sb.toString();
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.querygen.service.impl.SQLQueryGenerationServiceImpl#handleRollupQuery(com.execue.core.common.bean.querygen.QueryStructure)
    */
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
      String groupBySimpleStringWithRollup = SQL_DB2_ROLLUP_FUNCTION + SQL_FUNCTION_START_WRAPPER + groupBySimpleString
               + SQL_FUNCTION_END_WRAPPER;

      QueryClauseElement groupClauseElement = new QueryClauseElement();
      groupClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      groupClauseElement.setSimpleString(groupBySimpleStringWithRollup);

      List<QueryClauseElement> groupElementsWithRollup = new ArrayList<QueryClauseElement>();
      groupElementsWithRollup.add(groupClauseElement);

      // Reset the group by elements
      queryStructure.setGroupElements(groupElementsWithRollup);

      // Prepare the order by clause using grouping function
      List<QueryClauseElement> orderByElements = queryStructure.getOrderElements();
      List<QueryClauseElement> newOrderByElements = new ArrayList<QueryClauseElement>();
      for (QueryClauseElement orderByElement : orderByElements) {
         String simpleString = SQL_DB2_GROUPING_FUNCTION + SQL_FUNCTION_START_WRAPPER
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

      // Reset the order by elements
      queryStructure.setOrderElements(newOrderByElements);

      return queryStructure;
   }

   /**
    * Transforms the columnString to CAST(columnString As DECIMAL(30,6)) if data type is of numeric nature else
    * CAST(columnString As VARCHAR(25)) if data type is of string nature NOTE: VARCHAR being 25 in length is a glitch if
    * used any thing other than in COUNT function
    */
   @Override
   protected String getColumnStringCastToDataType (String columnString, DataType dataType) {
      if (ExecueBeanUtil.isStringDataType(dataType)) {
         return getColumnStringCasted(columnString, DataType.STRING.getValue(), SQL_DB2_MAX_STRING_PRECISION, null);
      } else if (ExecueBeanUtil.isNumericDataType(dataType)) {
         return getColumnStringCasted(columnString, DataType.NUMBER.getValue(), SQL_DB2_MAX_DECIMAL_PRECISION,
                  SQL_DB2_MAX_DECIMAL_SCALE);
      } else {
         return columnString;
      }
   }

   /**
    * This method is used only when there is a Distinct set on the Column createdColumnString is not used in here
    */
   @Override
   protected String getColumnStringCastToDataType (QueryTableColumn columnInfo, String createdColumnString) {
      QueryTableColumn clonedColumnInfo = ExecueBeanCloneUtil.cloneQueryTableColumn(columnInfo);
      clonedColumnInfo.getColumn().setDistinct(false);
      String columnString = createColumString(clonedColumnInfo);
      String castColumnString = getColumnStringCastToDataType(columnString, columnInfo.getColumn().getDataType());
      if (columnInfo.getColumn().isDistinct()
               && !columnInfo.getColumn().getColumnName().equalsIgnoreCase(SQL_WILD_CHAR)) {
         castColumnString = SQL_DISTINCT_KEYWORD + SQL_SPACE_DELIMITER + castColumnString;
      }
      return castColumnString;
   }

   private String getColumnStringCasted (String columnString, String dataType, Integer precision, Integer scale) {
      StringBuilder sb = new StringBuilder();

      sb.append(SQL_DB2_CAST_FUNCTION);
      sb.append(SQL_FUNCTION_START_WRAPPER);
      sb.append(columnString);
      sb.append(SQL_SPACE_DELIMITER);
      sb.append(SQL_ALIAS);
      sb.append(SQL_SPACE_DELIMITER);
      sb.append(dataType);
      if (precision != null) {
         sb.append(SQL_FUNCTION_START_WRAPPER);
         sb.append(precision);
         if (scale != null) {
            sb.append(SQL_ENTITY_SEPERATOR);
            sb.append(scale);
         }
         sb.append(SQL_FUNCTION_END_WRAPPER);
      }
      sb.append(SQL_FUNCTION_END_WRAPPER);

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
