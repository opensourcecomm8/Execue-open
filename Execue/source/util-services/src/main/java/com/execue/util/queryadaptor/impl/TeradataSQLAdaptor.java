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
import com.execue.core.common.type.QueryElementType;
import com.execue.core.common.util.QueryFormatUtility;
import com.execue.core.constants.ISQLQueryConstants;
import com.execue.util.queryadaptor.ISQLAdaptor;

/**
 * @author Nitesh
 */
public class TeradataSQLAdaptor implements ISQLAdaptor, ISQLQueryConstants {

   @Override
   public QueryStructure handleLimitClause (QueryStructure queryStructure) {

      /**
       * Select a.trp as trp, a.act as act, a.str as str From ( select ftr.tran_period as trp, ftr.acct_type as act,
       * sum(ftr.tran_total) as str from financial.trans as ftr Group by ftr.tran_period, ftr.acct_type ) a qualify
       * row_number over (order by a.trp asc, a.act asc) between 0 and 11;
       */
      QueryClauseElement limitElement = queryStructure.getLimitElement();
      String limitClause = limitElement.getSimpleString();
      String[] indices = limitClause.split(",");
      int startIndex = Integer.parseInt(indices[0]);
      int endIndex = Integer.parseInt(indices[1]);

      // GET THE ORDER BY CLAUSE TO THE OUTER QUERY
      String orderByAliasString = QueryFormatUtility.prepareOrderByClauseAliasString(queryStructure.getOrderElements());
      StringBuilder orderByClause = new StringBuilder();
      orderByClause.append(SQL_ORDER_BY_CLAUSE).append(SQL_SPACE_DELIMITER);
      orderByClause.append(orderByAliasString);

      // NULLIFY THE LIMIT ELEMENT AND ORDER BY ELEMENT from the original queryStructure
      queryStructure.setLimitElement(null);
      queryStructure.setOrderElements(null);

      // Prepare the outer select clause elements i.e. Select trp, act, str
      List<QueryClauseElement> outerQuerySelectElements = new ArrayList<QueryClauseElement>();
      String selectClauseAliasString = QueryFormatUtility.prepareSelectClauseAliasString(queryStructure
               .getSelectElements());
      QueryClauseElement outerQuerySelectElement = new QueryClauseElement();
      outerQuerySelectElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      outerQuerySelectElement.setSimpleString(selectClauseAliasString);
      outerQuerySelectElements.add(outerQuerySelectElement);

      // Prepare the outer query from clause elements i.e. original query without order by and limit as subquery
      List<QueryClauseElement> outerQueryFromElements = new ArrayList<QueryClauseElement>();
      QueryClauseElement outerQueryfromElement = new QueryClauseElement();
      outerQueryfromElement.setQueryElementType(QueryElementType.SUB_QUERY);
      outerQueryfromElement.setSubQuery(queryStructure);
      outerQueryfromElement.setAlias(TERADATA_LIMIT_INNER_QUERY_ALIAS_NAME);
      outerQueryFromElements.add(outerQueryfromElement);

      // Prepare the outer query teradata specific limit clause
      // qualify row_number over (order by a.trp asc, a.act asc) between 0 and 11;

      QueryClauseElement outerQueryLimitElement = new QueryClauseElement();
      outerQueryLimitElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      StringBuilder outerQueryLimitClause = new StringBuilder();
      outerQueryLimitClause.append(surroundWithSpaces(TERADATA_QUALIFY_KEYWORD)).append(TERADATA_ROW_NUMBER_FUNCTION);
      outerQueryLimitClause.append(SQL_FUNCTION_START_WRAPPER).append(SQL_FUNCTION_END_WRAPPER);
      outerQueryLimitClause.append(surroundWithSpaces(SQL_OVER_KEYWORD)).append(SQL_FUNCTION_START_WRAPPER);
      outerQueryLimitClause.append(orderByClause).append(SQL_FUNCTION_END_WRAPPER);
      outerQueryLimitClause.append(surroundWithSpaces(SQL_BETWEEN_FUNCTION)).append(startIndex).append(
               surroundWithSpaces(WHERE_CLAUSE_SEPERATOR)).append(endIndex);
      outerQueryLimitElement.setSimpleString(outerQueryLimitClause.toString());

      // Prepare the outer query
      QueryStructure outerQueryStructure = new QueryStructure();
      outerQueryStructure.setSelectElements(outerQuerySelectElements);
      outerQueryStructure.setFromElements(outerQueryFromElements);
      outerQueryStructure.setLimitElement(outerQueryLimitElement);

      return outerQueryStructure;

   }

   private String surroundWithSpaces (String text) {
      StringBuilder sb = new StringBuilder();
      sb.append(SQL_SPACE_DELIMITER).append(text).append(SQL_SPACE_DELIMITER);
      return sb.toString();
   }

   /* 
    * NOTE: In Teradata, The RANDOM function generates a random number that is inclusive for the numbers specified in 
    * the SQL that is greater than or equal to the lowerBound and less than or equal to the upperBound. 
    */
   @Override
   public String createRandomNumberGeneratorFormula (Integer lowerBound, Integer upperBound) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(SQL_TERADATA_RAND_FUNCTION + "(");
      stringBuilder.append(lowerBound);
      stringBuilder.append(SQL_ENTITY_SEPERATOR);
      stringBuilder.append(upperBound);
      stringBuilder.append(")");
      return stringBuilder.toString();
   }

   @Override
   public String getAutoIncrementClause () {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getNullRepresentationFunction (DataType dataType) {
      return TERADATA_NULL_CHECK_KEYWORD;
   }

   @Override
   public boolean isAutoIncrementClauseSupported () {
      return false;
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
      stringBuildler.append(tableOwner).append(SQL_ALIAS_SEPERATOR).append(tableName);
      if (appendAlias) {
         String tableAlias = queryTable.getAlias();
         stringBuildler.append(SQL_SPACE_DELIMITER).append(tableAlias);
      }
      return stringBuildler.toString();
   }
}
