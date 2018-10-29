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
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.QueryElementType;
import com.execue.core.common.util.QueryFormatUtility;
import com.execue.core.constants.ISQLQueryConstants;
import com.execue.util.queryadaptor.ISQLAdaptor;

/**
 * @author Nitesh
 */
public class OracleSQLAdaptor implements ISQLAdaptor, ISQLQueryConstants {

   @Override
   public QueryStructure handleLimitClause (QueryStructure queryStructure) {

      // Create New QueryStructure for oracle pagination i.e. if limit is defined, we create the below structure
      /*
       * select * from ( select <alias>.*, ROWNUM rnum from ( <original queryStructure without limit> ) <alias> where
       * ROWNUM <= :MAX_ROW_TO_FETCH ) where rnum >= :MIN_ROW_TO_FETCH;
       */

      QueryClauseElement limitElement = queryStructure.getLimitElement();
      String limitEntity = limitElement.getSimpleString();
      String[] tokens = limitEntity.split(",");
      Long startingNumber = new Long(tokens[0]);
      Long endingNumber = new Long(tokens[1]);

      // NULLIFY THE LIMIT ELEMENT from the original queryStructure
      queryStructure.setLimitElement(null);

      // Prepare sub query select clause elements i.e. <alias>.*, ROWNUM rnum
      List<QueryClauseElement> subQuerySelectElements = new ArrayList<QueryClauseElement>();

      QueryClauseElement subQuerySelectElement1 = new QueryClauseElement();
      subQuerySelectElement1.setQueryElementType(QueryElementType.SIMPLE_STRING);
      subQuerySelectElement1.setSimpleString(ORACLE_LIMIT_INNER_QUERY_ALIAS_NAME + SQL_ALIAS_SEPERATOR + SQL_WILD_CHAR);

      QueryClauseElement subQuerySelectElement2 = new QueryClauseElement();
      subQuerySelectElement2.setQueryElementType(QueryElementType.SIMPLE_STRING);
      subQuerySelectElement2.setSimpleString(ORACLE_LIMIT_CLAUSE);
      subQuerySelectElement2.setAlias(ORACLE_ROWNUM_KEYWORD_ALIAS_NAME);

      subQuerySelectElements.add(subQuerySelectElement1);
      subQuerySelectElements.add(subQuerySelectElement2);

      // Prepare sub query from clause elements i.e. original query as subquery
      List<QueryClauseElement> subQueryFromElements = new ArrayList<QueryClauseElement>();
      QueryClauseElement subQueryFromElement = new QueryClauseElement();
      subQueryFromElement.setQueryElementType(QueryElementType.SUB_QUERY);
      subQueryFromElement.setSubQuery(queryStructure);
      subQueryFromElement.setAlias(ORACLE_LIMIT_INNER_QUERY_ALIAS_NAME);
      subQueryFromElements.add(subQueryFromElement);

      // Prepare the sub query where clause elements i.e. ROWNUM <= :MAX_ROW_TO_FETCH
      List<QueryClauseElement> subQueryWhereElements = new ArrayList<QueryClauseElement>();
      QueryClauseElement subQueryWhereElement = new QueryClauseElement();
      subQueryWhereElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      StringBuilder whereClause = new StringBuilder();
      whereClause.append(ORACLE_LIMIT_CLAUSE).append(SQL_SPACE_DELIMITER).append(
               OperatorType.LESS_THAN_EQUALS.getValue()).append(SQL_SPACE_DELIMITER).append(endingNumber);
      subQueryWhereElement.setSimpleString(whereClause.toString());
      subQueryWhereElements.add(subQueryWhereElement);

      // Prepare the sub query
      QueryStructure subQueryStructure = new QueryStructure();
      subQueryStructure.setSelectElements(subQuerySelectElements);
      subQueryStructure.setFromElements(subQueryFromElements);
      subQueryStructure.setWhereElements(subQueryWhereElements);

      // Prepare the top level query select clause elements i.e. *
      List<QueryClauseElement> topQuerySelectElements = new ArrayList<QueryClauseElement>();
      String selectClauseAliasString = QueryFormatUtility.prepareSelectClauseAliasString(queryStructure
               .getSelectElements());
      QueryClauseElement topLevelQuerySelectElement = new QueryClauseElement();
      topLevelQuerySelectElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      topLevelQuerySelectElement.setSimpleString(selectClauseAliasString);
      topQuerySelectElements.add(topLevelQuerySelectElement);

      // Prepare the top level query from clause elements i.e. above sub query as subquery
      List<QueryClauseElement> topLevelQueryFromElements = new ArrayList<QueryClauseElement>();
      QueryClauseElement topLevelfromElement = new QueryClauseElement();
      topLevelfromElement.setQueryElementType(QueryElementType.SUB_QUERY);
      topLevelfromElement.setSubQuery(subQueryStructure);
      topLevelQueryFromElements.add(topLevelfromElement);

      // Prepare the top level query where clause elements i.e. rnum >= :MIN_ROW_TO_FETCH
      List<QueryClauseElement> topLeveQueryWhereElements = new ArrayList<QueryClauseElement>();
      QueryClauseElement topLevelQuerywhereElement = new QueryClauseElement();
      topLevelQuerywhereElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      StringBuilder topLevelQueryWhereClause = new StringBuilder();
      topLevelQueryWhereClause.append(ORACLE_ROWNUM_KEYWORD_ALIAS_NAME).append(SQL_SPACE_DELIMITER).append(
               OperatorType.GREATER_THAN_EQUALS.getValue()).append(SQL_SPACE_DELIMITER).append(startingNumber);
      topLevelQuerywhereElement.setSimpleString(topLevelQueryWhereClause.toString());
      topLeveQueryWhereElements.add(topLevelQuerywhereElement);

      // Prepare the top level query
      QueryStructure topLevelQueryStructure = new QueryStructure();
      topLevelQueryStructure.setSelectElements(topQuerySelectElements);
      topLevelQueryStructure.setFromElements(topLevelQueryFromElements);
      topLevelQueryStructure.setWhereElements(topLeveQueryWhereElements);

      return topLevelQueryStructure;
   }

   @Override
   public String createRandomNumberGeneratorFormula (Integer lowerBound, Integer upperBound) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(SQL_TRUNC_FUNCTION);
      stringBuilder.append(SQL_SUBQUERY_START_WRAPPER);
      stringBuilder.append(ORACLE_RANDOM_NUMBER_FUNCTION);
      stringBuilder.append(SQL_SUBQUERY_START_WRAPPER);
      stringBuilder.append(lowerBound);
      stringBuilder.append(SQL_ENTITY_SEPERATOR);
      stringBuilder.append(upperBound + 1);
      stringBuilder.append(SQL_SUBQUERY_END_WRAPPER);
      stringBuilder.append(SQL_SUBQUERY_END_WRAPPER);
      return stringBuilder.toString();

   }

   /**
    * In oracle, there is no auto increment clause.
    * 
    * @return
    */
   @Override
   public String getAutoIncrementClause () {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getNullRepresentationFunction (DataType dataType) {
      return ORACLE_NULL_CHECK_KEYWORD;
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
      stringBuildler.append(queryTable.getTableName());
      if (appendAlias) {
         String tableAlias = queryTable.getAlias();
         stringBuildler.append(SQL_SPACE_DELIMITER).append(tableAlias);
      }
      return stringBuildler.toString();
   }
}
