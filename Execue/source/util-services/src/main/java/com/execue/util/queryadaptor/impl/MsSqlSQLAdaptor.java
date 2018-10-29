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


package com.execue.util.queryadaptor.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.querygen.QueryClauseElement;
import com.execue.core.common.bean.querygen.QueryStructure;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.OrderEntityType;
import com.execue.core.common.type.QueryElementType;
import com.execue.core.common.util.QueryFormatUtility;
import com.execue.core.constants.ISQLQueryConstants;
import com.execue.util.queryadaptor.ISQLAdaptor;

/**
 * @author Nitesh
 */
public class MsSqlSQLAdaptor implements ISQLAdaptor, ISQLQueryConstants {

   @Override
   public QueryStructure handleLimitClause (QueryStructure queryStructure) {
      /*
       * Example 1: Query without joins
       * 
       * SELECT DISTINCT GHW56, YMV33 
       * FROM
       * (SELECT DISTINCT PQV05.CARD_TYPE_CD AS GHW56,PQV05.CARD_TYPE_CD_DESC AS YMV33, ROW_NUMBER() OVER (ORDER BY PQV05.CARD_TYPE_CD DESC) AS RNUM
       * FROM dbo.CARD_TYPE PQV05) PQV06
       * WHERE RNUM BETWEEN 1 AND 10
       * ORDER BY RNUM ASC;
       * 
       * Example 2: Query having Joins
       * SELECT DISTINCT GHW56, YMV33
       * FROM
       * (SELECT DISTINCT psg.PORT_SEG AS GHW56,psg.PORT_SEG_GRP AS YMV33, ROW_NUMBER() OVER (ORDER BY psg.PORT_SEG DESC) AS RNUM
       * FROM dbo.PORT_SEG_GRP PQV05, dbo.PORT_SEG psg
       * where PQV05.PORT_SEG_GRP = psg.PORT_SEG_GRP) PQV06
       * WHERE RNUM BETWEEN 1 AND 10
       * ORDER BY RNUM ASC;
       * 
       * */
      QueryClauseElement limitElement = queryStructure.getLimitElement();
      String limitClause = limitElement.getSimpleString();
      String[] indices = limitClause.split(",");
      int startIndex = Integer.parseInt(indices[0]);
      int endIndex = Integer.parseInt(indices[1]);

      // Prepare the outer select clause elements i.e. Select trp, act, str
      String selectClauseAliasString = QueryFormatUtility.prepareSelectClauseAliasString(queryStructure
               .getSelectElements());

      QueryClauseElement outerQuerySelectElement = new QueryClauseElement();
      outerQuerySelectElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      outerQuerySelectElement.setSimpleString(selectClauseAliasString);

      List<QueryClauseElement> outerQuerySelectElements = new ArrayList<QueryClauseElement>();
      outerQuerySelectElements.add(outerQuerySelectElement);

      // Prepare the outer query from clause elements i.e. original query without order by and limit as subquery
      QueryClauseElement outerQueryfromElement = new QueryClauseElement();
      outerQueryfromElement.setQueryElementType(QueryElementType.SUB_QUERY);
      outerQueryfromElement.setSubQuery(queryStructure);
      outerQueryfromElement.setAlias(MSSQL_LIMIT_INNER_QUERY_ALIAS_NAME);

      List<QueryClauseElement> outerQueryFromElements = new ArrayList<QueryClauseElement>();
      outerQueryFromElements.add(outerQueryfromElement);

      // Prepare the select element clause for mssql specific rownum over order by call
      // ROW_NUMBER() OVER (ORDER BY psg.PORT_SEG DESC) AS RNUM

      // GET THE ORDER BY CLAUSE
      String orderByString = QueryFormatUtility.prepareOrderByClauseString(queryStructure.getOrderElements());

      StringBuilder rowNumSelectClauseString = new StringBuilder();
      rowNumSelectClauseString.append(MSSQL_ROW_NUMBER_FUNCTION);
      rowNumSelectClauseString.append(SQL_FUNCTION_START_WRAPPER).append(SQL_FUNCTION_END_WRAPPER);
      rowNumSelectClauseString.append(surroundWithSpaces(SQL_OVER_KEYWORD)).append(SQL_FUNCTION_START_WRAPPER);
      rowNumSelectClauseString.append(SQL_ORDER_BY_CLAUSE).append(SQL_SPACE_DELIMITER).append(orderByString).append(
               SQL_FUNCTION_END_WRAPPER).append(SQL_SPACE_DELIMITER).append(SQL_ALIAS).append(SQL_SPACE_DELIMITER)
               .append(MSSQL_ROW_NUMBER_KEYWORD_ALIAS_NAME);

      QueryClauseElement rowNumSelectElement = new QueryClauseElement();
      rowNumSelectElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      rowNumSelectElement.setSimpleString(rowNumSelectClauseString.toString());

      // Add the rowNumSelectElement to the existing queryStructure
      queryStructure.getSelectElements().add(rowNumSelectElement);

      // Prepare the where clause element for the outer query
      // WHERE RNUM BETWEEN 1 AND 10
      StringBuilder outerQueryWhereLimitClause = new StringBuilder();
      outerQueryWhereLimitClause.append(MSSQL_ROW_NUMBER_KEYWORD_ALIAS_NAME);
      outerQueryWhereLimitClause.append(surroundWithSpaces(SQL_BETWEEN_FUNCTION)).append(startIndex).append(
               surroundWithSpaces(WHERE_CLAUSE_SEPERATOR)).append(endIndex);

      QueryClauseElement outerQueryWhereClauseElement = new QueryClauseElement();
      outerQueryWhereClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      outerQueryWhereClauseElement.setSimpleString(outerQueryWhereLimitClause.toString());

      List<QueryClauseElement> outerQueryWhereClauseElements = new ArrayList<QueryClauseElement>();
      outerQueryWhereClauseElements.add(outerQueryWhereClauseElement);

      // Prepare the order clause element for the outer query
      // ORDER BY RNUM ASC
      StringBuilder outerQueryOrderClause = new StringBuilder();
      outerQueryOrderClause.append(MSSQL_ROW_NUMBER_KEYWORD_ALIAS_NAME);

      QueryClauseElement outerQueryOrderClauseElement = new QueryClauseElement();
      outerQueryOrderClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      outerQueryOrderClauseElement.setSimpleString(outerQueryOrderClause.toString());
      outerQueryOrderClauseElement.setOrderEntityType(OrderEntityType.ASCENDING);

      List<QueryClauseElement> outerQueryOrderClauseElements = new ArrayList<QueryClauseElement>();
      outerQueryOrderClauseElements.add(outerQueryOrderClauseElement);

      // NULLIFY THE LIMIT ELEMENT AND ORDER BY ELEMENT from the original queryStructure
      queryStructure.setLimitElement(null);
      queryStructure.setOrderElements(null);

      // Prepare the outer query
      QueryStructure outerQueryStructure = new QueryStructure();
      outerQueryStructure.setSelectElements(outerQuerySelectElements);
      outerQueryStructure.setFromElements(outerQueryFromElements);
      outerQueryStructure.setWhereElements(outerQueryWhereClauseElements);
      outerQueryStructure.setOrderElements(outerQueryOrderClauseElements);

      return outerQueryStructure;
   }

   private String surroundWithSpaces (String text) {
      StringBuilder sb = new StringBuilder();
      sb.append(SQL_SPACE_DELIMITER).append(text).append(SQL_SPACE_DELIMITER);
      return sb.toString();
   }

   @Override
   public String createRandomNumberGeneratorFormula (Integer lowerBound, Integer upperBound) {
      // Following formula is used for generating random number in mssql
      // lowerBound + convert(int, (upperBound-lowerBound+1)*RAND(checksum(newid())))
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(lowerBound + "+");
      stringBuilder.append(SQL_MSSQL_CONVERT_FUNCTION + "(INT,");
      stringBuilder.append("(");
      stringBuilder.append(upperBound);
      stringBuilder.append("-");
      stringBuilder.append(lowerBound);
      stringBuilder.append("+1");
      stringBuilder.append(")*");
      stringBuilder.append(SQL_MSSQL_RAND_FUNCTION);
      stringBuilder.append("(");
      stringBuilder.append(SQL_MSSQL_CHECKSUM_FUNCTION);
      stringBuilder.append("(");
      stringBuilder.append(SQL_MSSQL_NEWID_FUNCTION);
      stringBuilder.append("()");
      stringBuilder.append(")))");
      return stringBuilder.toString();
   }

   @Override
   public String getAutoIncrementClause () {
      return MSSQL_AUTO_INCREMENT_VALUE;
   }

   @Override
   public String getNullRepresentationFunction (DataType dataType) {
      return MSSQL_NULL_CHECK_KEYWORD;
   }

   @Override
   public boolean isAutoIncrementClauseSupported () {
      return true;
   }

   @Override
   public boolean isMultipleIndexesWithSingleStatementSupported () {
      return false;
   }

   @Override
   public String createColumRepresentationQueryTableColumn (QueryTableColumn queryTableColumn) {
      StringBuilder columnRepresentation = new StringBuilder();
      columnRepresentation.append(queryTableColumn.getTable().getAlias()).append(SQL_ALIAS_SEPERATOR).append(
               queryTableColumn.getColumn().getColumnName());
      return columnRepresentation.toString();
   }

   @Override
   public String createTableRepresentationQueryTableColumn (QueryTable queryTable, boolean appendAlias) {
      StringBuilder stringBuildler = new StringBuilder();
      String tableOwner = queryTable.getOwner();
      String tableName = queryTable.getTableName();
      String tableAlias = queryTable.getAlias();
      stringBuildler.append(tableOwner).append(SQL_ALIAS_SEPERATOR).append(tableName);
      if (appendAlias) {
         stringBuildler.append(SQL_SPACE_DELIMITER).append(tableAlias);
      }
      return stringBuildler.toString();
   }

}
