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


package com.execue.core.common.util;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.querygen.QueryClauseElement;
import com.execue.core.common.bean.querygen.QueryStructure;
import com.execue.core.common.type.OrderEntityType;
import com.execue.core.common.type.QueryElementType;
import com.execue.core.common.type.QuerySectionType;
import com.execue.core.constants.ISQLQueryConstants;
import com.execue.core.util.ExecueCoreUtil;

/**
 * This class is utility to prepare a query string from query structure in a formatted manner
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/06/09
 */
public class QueryFormatUtility implements ISQLQueryConstants {

   public static final String NEW_LINE                   = "<br/>";
   public static final String SQL_ENTITY_SEPERATOR       = ",";
   public static final String BOLD_START                 = "<b>";
   public static final String BOLD_END                   = "</b>";
   public static final String TAB                        = "&nbsp;";
   public static final String SQL_SPACE_DELIMITER        = " ";
   public static final String SQL_SELECT_CLAUSE          = "SELECT";
   public static final String SQL_JOIN_FROM_CLAUSE       = "FROM";
   public static final String SQL_WHERE_CLAUSE           = "WHERE";
   public static final String SQL_ORDER_BY_CLAUSE        = "ORDER BY";
   public static final String SQL_GROUP_BY_CLAUSE        = "GROUP BY";
   public static final String SQL_SUBQUERY_START_WRAPPER = "(";
   public static final String SQL_SUBQUERY_END_WRAPPER   = ")";
   public static final String SQL_CASE_WHEN              = "WHEN";
   public static final String SQL_CASE_ELSE              = "ELSE";
   public static final String SQL_CASE                   = "CASE";
   public static final String SQL_CASE_END               = "END";
   public static final String SQL_ALIAS                  = " AS ";
   public static final String SQL_AND_CONDITION          = " AND";
   public static int          TAB_SPACE                  = 0;
   public static int          CURRENT_TAB_SPACE          = 0;
   public final static int    DEFAULT_TAB_SPACE          = 8;

   public static String prepareFormattedQueryString (QueryStructure queryStructure) {
      return prepareFormattedQueryString(queryStructure, false);
   }

   public static String prepareFormattedQueryString (QueryStructure queryStructure, boolean isSubQueryCall) {

      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(BOLD_START);
      stringBuilder.append(SQL_SELECT_CLAUSE);
      stringBuilder.append(BOLD_END);
      for (QueryClauseElement selectQueryClauseElement : queryStructure.getSelectElements()) {
         switch (selectQueryClauseElement.getQueryElementType()) {
            case SIMPLE_STRING:
               stringBuilder.append(getFormattedSimpleString(selectQueryClauseElement.getSimpleString()));
               if (ExecueCoreUtil.isNotEmpty(selectQueryClauseElement.getAlias())) {
                  stringBuilder.append(SQL_ALIAS);
                  stringBuilder.append(selectQueryClauseElement.getAlias());
               }
               stringBuilder.append(SQL_ENTITY_SEPERATOR);
               break;
            case CASE_STATEMENT:
               List<String> caseString = selectQueryClauseElement.getCaseStatement();
               stringBuilder.append(getFormattedCaseStatement(caseString));
               stringBuilder.append(SQL_ALIAS);
               stringBuilder.append(selectQueryClauseElement.getAlias());
               stringBuilder.append(SQL_ENTITY_SEPERATOR);
               break;
            case SUB_QUERY:
               QueryStructure subQueryStructure = selectQueryClauseElement.getSubQuery();
               stringBuilder.append(getFormattedSubQuery(subQueryStructure, selectQueryClauseElement,
                        QuerySectionType.SELECT));
               stringBuilder.append(SQL_ENTITY_SEPERATOR);
               break;
         }
      }
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      stringBuilder.append(NEW_LINE);
      if (isSubQueryCall) {
         stringBuilder.append(getTabbedSpace(CURRENT_TAB_SPACE));
      }
      stringBuilder.append(getFromClauseFormattedString(queryStructure));

      if (ExecueCoreUtil.isCollectionNotEmpty(queryStructure.getWhereElements())) {
         stringBuilder.append(NEW_LINE);
         if (isSubQueryCall) {
            stringBuilder.append(getTabbedSpace(CURRENT_TAB_SPACE));
         }
         stringBuilder.append(BOLD_START);
         stringBuilder.append(SQL_WHERE_CLAUSE);
         stringBuilder.append(BOLD_END);
         for (QueryClauseElement whereQueryClauseElement : queryStructure.getWhereElements()) {
            switch (whereQueryClauseElement.getQueryElementType()) {
               case SIMPLE_STRING:
                  stringBuilder.append(getFormattedSimpleString(whereQueryClauseElement.getSimpleString()));
                  stringBuilder.append(SQL_AND_CONDITION);
                  break;
               case SUB_QUERY:
                  stringBuilder.append(getFormattedSimpleString(whereQueryClauseElement.getSimpleString()));
                  QueryStructure subQueryStructure = whereQueryClauseElement.getSubQuery();
                  stringBuilder.append(getFormattedSubQuery(subQueryStructure, whereQueryClauseElement,
                           QuerySectionType.CONDITION));
                  stringBuilder.append(SQL_AND_CONDITION);
                  break;
            }
         }
         stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length());
      }

      if (ExecueCoreUtil.isCollectionNotEmpty(queryStructure.getGroupElements())) {
         stringBuilder.append(NEW_LINE);
         if (isSubQueryCall) {
            stringBuilder.append(getTabbedSpace(CURRENT_TAB_SPACE));
         }
         stringBuilder.append(BOLD_START);
         stringBuilder.append(SQL_GROUP_BY_CLAUSE);
         stringBuilder.append(BOLD_END);
         for (QueryClauseElement groupQueryClauseElement : queryStructure.getGroupElements()) {
            switch (groupQueryClauseElement.getQueryElementType()) {
               case SIMPLE_STRING:
                  stringBuilder.append(getFormattedSimpleString(groupQueryClauseElement.getSimpleString()));
                  stringBuilder.append(SQL_ENTITY_SEPERATOR);
                  break;
               case CASE_STATEMENT:
                  List<String> caseString = groupQueryClauseElement.getCaseStatement();

                  stringBuilder.append(getFormattedCaseStatement(caseString));
                  stringBuilder.append(SQL_ENTITY_SEPERATOR);
                  break;
            }
         }
         stringBuilder.deleteCharAt(stringBuilder.length() - 1);

         // Populate the having clause
         if (ExecueCoreUtil.isCollectionNotEmpty(queryStructure.getHavingElements())) {
            stringBuilder.append(NEW_LINE);
            if (isSubQueryCall) {
               stringBuilder.append(getTabbedSpace(CURRENT_TAB_SPACE));
            }
            stringBuilder.append(BOLD_START);
            stringBuilder.append(SQL_HAVING_CLAUSE);
            stringBuilder.append(BOLD_END);
            for (QueryClauseElement havingClauseElement : queryStructure.getHavingElements()) {
               switch (havingClauseElement.getQueryElementType()) {
                  case SIMPLE_STRING:
                     stringBuilder.append(getFormattedSimpleString(havingClauseElement.getSimpleString()));
                     stringBuilder.append(SQL_AND_CONDITION);
                     break;
                  case SUB_QUERY:
                     stringBuilder.append(getFormattedSimpleString(havingClauseElement.getSimpleString()));
                     QueryStructure subQueryStructure = havingClauseElement.getSubQuery();
                     stringBuilder.append(getFormattedSubQuery(subQueryStructure, havingClauseElement,
                              QuerySectionType.HAVING));
                     stringBuilder.append(SQL_AND_CONDITION);
                     break;
               }
            }
            stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length());
         }
      }

      if (ExecueCoreUtil.isCollectionNotEmpty(queryStructure.getOrderElements())) {
         stringBuilder.append(NEW_LINE);
         if (isSubQueryCall) {
            stringBuilder.append(getTabbedSpace(CURRENT_TAB_SPACE));
         }
         stringBuilder.append(BOLD_START);
         stringBuilder.append(SQL_ORDER_BY_CLAUSE);
         stringBuilder.append(BOLD_END);
         for (QueryClauseElement orderByQueryClauseElement : queryStructure.getOrderElements()) {
            switch (orderByQueryClauseElement.getQueryElementType()) {
               case SIMPLE_STRING:
                  stringBuilder.append(getFormattedSimpleString(orderByQueryClauseElement.getSimpleString()));
                  break;
               case CASE_STATEMENT:
                  List<String> caseString = orderByQueryClauseElement.getCaseStatement();
                  stringBuilder.append(getFormattedCaseStatement(caseString));
                  break;
            }
            stringBuilder.append(SQL_SPACE_DELIMITER);
            stringBuilder.append(orderByQueryClauseElement.getOrderEntityType().getValue());
            stringBuilder.append(SQL_ENTITY_SEPERATOR);
         }
         stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      }
      if (isValidLimitClause(queryStructure.getLimitElement())) {
         stringBuilder.append(SQL_SPACE_DELIMITER);
         stringBuilder.append(queryStructure.getLimitElement().getSimpleString());
      }
      if (!isSubQueryCall) {
         stringBuilder.append(NEW_LINE);
      }
      return stringBuilder.toString();
   }

   /**
    * @param queryStructure
    * @param stringBuilder
    */
   private static String getFromClauseFormattedString (QueryStructure queryStructure) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(BOLD_START);
      stringBuilder.append(SQL_JOIN_FROM_CLAUSE);
      stringBuilder.append(BOLD_END);

      if (ExecueCoreUtil.isCollectionNotEmpty(queryStructure.getJoinElements())) {
         for (QueryClauseElement joinQueryClauseElement : queryStructure.getJoinElements()) {
            stringBuilder.append(NEW_LINE);
            stringBuilder.append(getTabbedSpace(TAB_SPACE));
            stringBuilder.append(joinQueryClauseElement.getSimpleString());
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(queryStructure.getFromElements())) {
            stringBuilder.append(SQL_ENTITY_SEPERATOR);
         }
      }

      if (ExecueCoreUtil.isCollectionNotEmpty(queryStructure.getFromElements())) {
         for (QueryClauseElement fromQueryClauseElement : queryStructure.getFromElements()) {
            switch (fromQueryClauseElement.getQueryElementType()) {
               case SIMPLE_STRING:
                  stringBuilder.append(getTabbedSpace(TAB_SPACE));
                  stringBuilder.append(getFormattedSimpleString(fromQueryClauseElement.getSimpleString()));
                  if (ExecueCoreUtil.isNotEmpty(fromQueryClauseElement.getAlias())) {
                     stringBuilder.append(SQL_SPACE_DELIMITER).append(fromQueryClauseElement.getAlias());
                  }
                  stringBuilder.append(SQL_ENTITY_SEPERATOR);
                  break;
               case SUB_QUERY:
                  QueryStructure subQueryStructure = fromQueryClauseElement.getSubQuery();
                  stringBuilder.append(getFormattedSubQuery(subQueryStructure, fromQueryClauseElement, null));
                  stringBuilder.append(SQL_ENTITY_SEPERATOR);
                  break;
            }
         }
         stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      }

      return stringBuilder.toString();
   }

   public static String prepareQueryString (QueryStructure queryStructure) {
      return prepareQueryString(queryStructure, false);
   }

   public static String prepareQueryString (QueryStructure queryStructure, boolean isSubQueryCall) {
      StringBuilder queryString = new StringBuilder();
      if (queryStructure.getCombiningType() == null) {
         queryString.append(prepareQueryStringForSimpleQuery(queryStructure, isSubQueryCall));
      } else {
         List<QueryStructure> combiningQueryStructures = queryStructure.getCombiningQueryStructures();
         List<String> combiningQueries = new ArrayList<String>();
         for (QueryStructure combiningQueryStructure : combiningQueryStructures) {
            String combiningQuery = prepareQueryStringForSimpleQuery(combiningQueryStructure, isSubQueryCall);
            combiningQueries.add(combiningQuery);
         }
         // Join the combining queries by the combining type like UNION, UNION ALL, MINUS, etc
         queryString.append(ExecueCoreUtil.joinCollection(combiningQueries, SQL_SPACE_DELIMITER
                  + queryStructure.getCombiningType().getValue() + SQL_SPACE_DELIMITER));

         // Prepare the order by 
         if (ExecueCoreUtil.isCollectionNotEmpty(queryStructure.getOrderElements())) {
            queryString.append(SQL_SPACE_DELIMITER);
            queryString.append(SQL_ORDER_BY_CLAUSE);
            queryString.append(SQL_SPACE_DELIMITER);
            queryString.append(prepareOrderByClauseString(queryStructure.getOrderElements()));
         }
      }

      return queryString.toString();
   }

   private static String prepareQueryStringForSimpleQuery (QueryStructure queryStructure, boolean isSubQueryCall) {

      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(SQL_SELECT_CLAUSE);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      for (QueryClauseElement selectQueryClauseElement : queryStructure.getSelectElements()) {
         switch (selectQueryClauseElement.getQueryElementType()) {
            case SIMPLE_STRING:
               String simpleString = selectQueryClauseElement.getSimpleString();
               if (ExecueCoreUtil.isEmpty(simpleString)) {
                  stringBuilder.append(EMPTY_QUOTE);
               } else {
                  stringBuilder.append(simpleString);
               }
               if (ExecueCoreUtil.isNotEmpty(selectQueryClauseElement.getAlias())) {
                  stringBuilder.append(SQL_ALIAS);
                  stringBuilder.append(selectQueryClauseElement.getAlias());
               }
               stringBuilder.append(SQL_ENTITY_SEPERATOR);
               break;
            case CASE_STATEMENT:
               List<String> caseStatements = selectQueryClauseElement.getCaseStatement();
               stringBuilder.append(getCaseStatementString(caseStatements));
               if (ExecueCoreUtil.isNotEmpty(selectQueryClauseElement.getAlias())) {
                  stringBuilder.append(SQL_ALIAS);
                  stringBuilder.append(selectQueryClauseElement.getAlias());
               }
               stringBuilder.append(SQL_ENTITY_SEPERATOR);
               break;
            case SUB_QUERY:
               QueryStructure subQueryStructure = selectQueryClauseElement.getSubQuery();
               stringBuilder.append(getSubQueryString(subQueryStructure, selectQueryClauseElement,
                        QuerySectionType.SELECT));
               stringBuilder.append(SQL_ENTITY_SEPERATOR);
               break;
         }

      }
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);

      // Append the from clause string
      stringBuilder.append(getFromClauseString(queryStructure));

      if (ExecueCoreUtil.isCollectionNotEmpty(queryStructure.getWhereElements())) {
         stringBuilder.append(SQL_SPACE_DELIMITER);
         stringBuilder.append(SQL_WHERE_CLAUSE);
         for (QueryClauseElement whereQueryClauseElement : queryStructure.getWhereElements()) {
            switch (whereQueryClauseElement.getQueryElementType()) {
               case SIMPLE_STRING:
                  stringBuilder.append(SQL_SPACE_DELIMITER);
                  stringBuilder.append(whereQueryClauseElement.getSimpleString());
                  stringBuilder.append(SQL_SPACE_DELIMITER);
                  stringBuilder.append(SQL_AND_CONDITION);
                  break;
               case SUB_QUERY:
                  stringBuilder.append(SQL_SPACE_DELIMITER);
                  stringBuilder.append(whereQueryClauseElement.getSimpleString());
                  stringBuilder.append(SQL_SPACE_DELIMITER);
                  QueryStructure subQueryStructure = whereQueryClauseElement.getSubQuery();
                  stringBuilder.append(getSubQueryString(subQueryStructure, whereQueryClauseElement,
                           QuerySectionType.CONDITION));
                  stringBuilder.append(SQL_AND_CONDITION);
                  break;
            }
         }
         stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length());
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(queryStructure.getGroupElements())) {
         stringBuilder.append(SQL_SPACE_DELIMITER);
         stringBuilder.append(SQL_GROUP_BY_CLAUSE);
         stringBuilder.append(SQL_SPACE_DELIMITER);
         for (QueryClauseElement groupQueryClauseElement : queryStructure.getGroupElements()) {
            switch (groupQueryClauseElement.getQueryElementType()) {
               case SIMPLE_STRING:
                  stringBuilder.append(groupQueryClauseElement.getSimpleString());
                  stringBuilder.append(SQL_ENTITY_SEPERATOR);
                  break;
               case CASE_STATEMENT:
                  List<String> caseStatements = groupQueryClauseElement.getCaseStatement();
                  stringBuilder.append(getCaseStatementString(caseStatements));
                  stringBuilder.append(SQL_ENTITY_SEPERATOR);
                  break;
            }
         }
         stringBuilder.deleteCharAt(stringBuilder.length() - 1);

         // Populate the having cluases, if any
         if (ExecueCoreUtil.isCollectionNotEmpty(queryStructure.getHavingElements())) {
            stringBuilder.append(SQL_SPACE_DELIMITER);
            stringBuilder.append(SQL_HAVING_CLAUSE);
            for (QueryClauseElement havingClauseElement : queryStructure.getHavingElements()) {
               switch (havingClauseElement.getQueryElementType()) {
                  case SIMPLE_STRING:
                     stringBuilder.append(SQL_SPACE_DELIMITER);
                     stringBuilder.append(havingClauseElement.getSimpleString());
                     stringBuilder.append(SQL_SPACE_DELIMITER);
                     stringBuilder.append(SQL_AND_CONDITION);
                     break;
                  case SUB_QUERY:
                     stringBuilder.append(SQL_SPACE_DELIMITER);
                     stringBuilder.append(havingClauseElement.getSimpleString());
                     stringBuilder.append(SQL_SPACE_DELIMITER);
                     QueryStructure subQueryStructure = havingClauseElement.getSubQuery();
                     stringBuilder.append(getSubQueryString(subQueryStructure, havingClauseElement,
                              QuerySectionType.HAVING));
                     stringBuilder.append(SQL_AND_CONDITION);
                     break;
               }
            }
            stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length());
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(queryStructure.getOrderElements())) {
         stringBuilder.append(SQL_SPACE_DELIMITER);
         stringBuilder.append(SQL_ORDER_BY_CLAUSE);
         stringBuilder.append(SQL_SPACE_DELIMITER);
         stringBuilder.append(prepareOrderByClauseString(queryStructure.getOrderElements()));
      }

      if (isValidLimitClause(queryStructure.getLimitElement())) {
         stringBuilder.append(SQL_SPACE_DELIMITER);
         stringBuilder.append(queryStructure.getLimitElement().getSimpleString());
      }
      return stringBuilder.toString();
   }

   /**
    * @param queryStructure
    * @param stringBuilder
    */
   private static String getFromClauseString (QueryStructure queryStructure) {

      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(SQL_JOIN_FROM_CLAUSE);
      stringBuilder.append(SQL_SPACE_DELIMITER);

      if (ExecueCoreUtil.isCollectionNotEmpty(queryStructure.getJoinElements())) {
         for (QueryClauseElement joinQueryClauseElement : queryStructure.getJoinElements()) {
            stringBuilder.append(SQL_SPACE_DELIMITER);
            stringBuilder.append(joinQueryClauseElement.getSimpleString());
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(queryStructure.getFromElements())) {
            stringBuilder.append(SQL_ENTITY_SEPERATOR).append(SQL_SPACE_DELIMITER);
         }
      }

      if (ExecueCoreUtil.isCollectionNotEmpty(queryStructure.getFromElements())) {
         for (QueryClauseElement fromQueryClauseElement : queryStructure.getFromElements()) {
            switch (fromQueryClauseElement.getQueryElementType()) {
               case SIMPLE_STRING:
                  stringBuilder.append(fromQueryClauseElement.getSimpleString());
                  if (ExecueCoreUtil.isNotEmpty(fromQueryClauseElement.getAlias())) {
                     stringBuilder.append(SQL_SPACE_DELIMITER).append(fromQueryClauseElement.getAlias());
                  }
                  stringBuilder.append(SQL_ENTITY_SEPERATOR);
                  break;
               case SUB_QUERY:
                  QueryStructure subQueryStructure = fromQueryClauseElement.getSubQuery();
                  // TODO:NK:: Need to handle the combination type queries
                  if (subQueryStructure.getCombiningType() == null) {
                     stringBuilder.append(getSubQueryString(subQueryStructure, fromQueryClauseElement, null));
                  } else {
                     List<String> subQueryStrings = new ArrayList<String>();
                     for (QueryStructure combiningQueryStructure : subQueryStructure.getCombiningQueryStructures()) {
                        subQueryStrings.add(prepareQueryString(combiningQueryStructure, true));
                     }
                     String joinedSubQueryString = ExecueCoreUtil.joinCollection(subQueryStrings, SQL_SPACE_DELIMITER
                              + subQueryStructure.getCombiningType().getValue() + SQL_SPACE_DELIMITER);
                     stringBuilder.append(SQL_SUBQUERY_START_WRAPPER);
                     stringBuilder.append(joinedSubQueryString);
                     stringBuilder.append(SQL_SUBQUERY_END_WRAPPER);
                     stringBuilder.append(SQL_SPACE_DELIMITER);
                     if (ExecueCoreUtil.isNotEmpty(fromQueryClauseElement.getAlias())) {
                        stringBuilder.append(fromQueryClauseElement.getAlias());
                     }
                     stringBuilder.append(SQL_SPACE_DELIMITER);
                  }
                  stringBuilder.append(SQL_ENTITY_SEPERATOR);
                  break;
            }
         }
         stringBuilder.deleteCharAt(stringBuilder.length() - 1);
      }
      return stringBuilder.toString();
   }

   /**
    * @param stringBuilder
    * @param caseStatements
    */
   private static String getCaseStatementString (List<String> caseStatements) {
      StringBuilder stringBuilder = new StringBuilder();
      for (String caseStatement : caseStatements) {
         stringBuilder.append(caseStatement);
         stringBuilder.append(SQL_SPACE_DELIMITER);
      }
      return stringBuilder.toString();
   }

   /**
    * @param limitElement
    * @return boolean true/false if limit clause is valid
    */
   public static boolean isValidLimitClause (QueryClauseElement limitElement) {
      return !(limitElement == null || ExecueCoreUtil.isEmpty(limitElement.getSimpleString()));
   }

   private static String getSubQueryString (QueryStructure subQueryStructure,
            QueryClauseElement selectQueryClauseElement, QuerySectionType sourceQuerySectionType) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(SQL_SUBQUERY_START_WRAPPER);
      stringBuilder.append(prepareQueryString(subQueryStructure, true));
      stringBuilder.append(SQL_SUBQUERY_END_WRAPPER);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      if (!QuerySectionType.SELECT.equals(sourceQuerySectionType)
               && !QuerySectionType.CONDITION.equals(sourceQuerySectionType)) {
         if (ExecueCoreUtil.isNotEmpty(selectQueryClauseElement.getAlias())) {
            stringBuilder.append(selectQueryClauseElement.getAlias());
         }
      } else {
         /*
          * if (!QueryElementType.SUB_QUERY.equals(queryClauseElement.getQueryElementType()))
          * subQueryString.append(queryClauseElement.getAlias());
          */
         // subQueryString.append(NEW_LINE);
         stringBuilder.append(SQL_SUBQUERY_END_WRAPPER);
      }
      return stringBuilder.toString();
   }

   private static String getTabbedSpace (int tabCount) {
      int tabSpace = DEFAULT_TAB_SPACE + tabCount;
      StringBuilder tabbedString = new StringBuilder();
      for (int count = 0; count < tabSpace; count++) {
         tabbedString.append(TAB);
      }
      return tabbedString.toString();
   }

   private static String getFormattedSubQuery (QueryStructure subQueryStructure, QueryClauseElement queryClauseElement,
            QuerySectionType sourceQuerySectionType) {
      StringBuilder subQueryString = new StringBuilder();
      subQueryString.append(NEW_LINE);
      subQueryString.append(getTabbedSpace(TAB_SPACE));
      CURRENT_TAB_SPACE = TAB_SPACE;
      TAB_SPACE += DEFAULT_TAB_SPACE;
      subQueryString.append(SQL_SUBQUERY_START_WRAPPER);
      subQueryString.append(prepareFormattedQueryString(subQueryStructure, true));
      // subQueryString.append(getTabbedSpace(CURRENT_TAB_SPACE));
      subQueryString.append(SQL_SUBQUERY_END_WRAPPER);
      subQueryString.append(SQL_SPACE_DELIMITER);
      CURRENT_TAB_SPACE -= TAB_SPACE;
      TAB_SPACE -= DEFAULT_TAB_SPACE;
      if (!QuerySectionType.SELECT.equals(sourceQuerySectionType)
               && !QuerySectionType.CONDITION.equals(sourceQuerySectionType)) {
         if (ExecueCoreUtil.isNotEmpty(queryClauseElement.getAlias())) {
            subQueryString.append(queryClauseElement.getAlias());
         }
      } else {
         /*
          * if (!QueryElementType.SUB_QUERY.equals(queryClauseElement.getQueryElementType()))
          * subQueryString.append(queryClauseElement.getAlias());
          */
         // subQueryString.append(NEW_LINE);
         subQueryString.append(SQL_SUBQUERY_END_WRAPPER);
      }
      return subQueryString.toString();
   }

   private static String getFormattedCaseStatement (List<String> caseString) {
      StringBuilder caseQueryString = new StringBuilder();
      CURRENT_TAB_SPACE = TAB_SPACE;
      TAB_SPACE += DEFAULT_TAB_SPACE;
      for (String string : caseString) {
         caseQueryString.append(NEW_LINE);

         if (string.toUpperCase().contains(SQL_CASE_WHEN) || string.toUpperCase().contains(SQL_CASE_ELSE)) {
            caseQueryString.append(getTabbedSpace(TAB_SPACE));
         }
         if (string.toUpperCase().contains(SQL_CASE) || string.toUpperCase().contains(SQL_CASE_END)) {
            caseQueryString.append(getTabbedSpace(CURRENT_TAB_SPACE));
            caseQueryString.append(string.replace(string, BOLD_START + string + BOLD_END));
         } else {
            caseQueryString.append(string);
         }
      }
      CURRENT_TAB_SPACE -= TAB_SPACE;
      TAB_SPACE -= DEFAULT_TAB_SPACE;
      return caseQueryString.toString();
   }

   private static String getFormattedSimpleString (String simpleString) {
      StringBuilder simpleQueryString = new StringBuilder();
      simpleQueryString.append(NEW_LINE);
      simpleQueryString.append(getTabbedSpace(TAB_SPACE));
      simpleQueryString.append(simpleString);
      return simpleQueryString.toString();
   }

   public static QueryClauseElement getSimpleQueryClauseElement (String simpleString) {
      return getSimpleQueryClauseElement(simpleString, null);
   }

   public static QueryClauseElement getSimpleQueryClauseElement (String simpleString, String alias) {
      return getSimpleQueryClauseElement(simpleString, alias, null);
   }

   /**
    * @param simpleString
    * @param alias
    * @param orderEntityType
    * @return the QueryClauseElement
    */
   public static QueryClauseElement getSimpleQueryClauseElement (String simpleString, String alias,
            OrderEntityType orderEntityType) {
      QueryClauseElement queryClauseElement = new QueryClauseElement();
      queryClauseElement.setSimpleString(simpleString);
      queryClauseElement.setAlias(alias);
      queryClauseElement.setOrderEntityType(orderEntityType);
      queryClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
      return queryClauseElement;
   }

   /**
    * @param caseStatement
    * @param alias
    * @param orderEntityType
    * @return the QueryClauseElement
    */
   public static QueryClauseElement getCaseQueryClauseElement (List<String> caseStatement, String alias,
            OrderEntityType orderEntityType) {
      QueryClauseElement queryClauseElement = new QueryClauseElement();
      queryClauseElement.setCaseStatement(caseStatement);
      queryClauseElement.setAlias(alias);
      queryClauseElement.setOrderEntityType(orderEntityType);
      queryClauseElement.setQueryElementType(QueryElementType.CASE_STATEMENT);
      return queryClauseElement;
   }

   /**
    * @param subQuery
    * @param alias
    * @return the QueryClauseElement
    */
   public static QueryClauseElement getSubQueryClauseElement (QueryStructure subQuery, String alias) {
      QueryClauseElement queryClauseElement = new QueryClauseElement();
      queryClauseElement.setSubQuery(subQuery);
      queryClauseElement.setAlias(alias);
      queryClauseElement.setQueryElementType(QueryElementType.SUB_QUERY);
      return queryClauseElement;
   }

   public static String prepareSelectClauseAliasString (List<QueryClauseElement> selectElements) {
      List<String> aliasList = new ArrayList<String>();
      for (QueryClauseElement queryClauseElement : selectElements) {
         // If alias is empty, then return the actual column name i.e. simple string
         if (ExecueCoreUtil.isEmpty(queryClauseElement.getAlias())) {
            aliasList.add(queryClauseElement.getSimpleString());
         } else {
            aliasList.add(queryClauseElement.getAlias());
         }
      }
      return ExecueCoreUtil.joinCollection(aliasList);
   }

   /**
    * @param queryStructure
    * @param stringBuilder
    */
   public static String prepareOrderByClauseString (List<QueryClauseElement> orderByClauseElements) {
      List<String> orderByClauses = new ArrayList<String>();
      for (QueryClauseElement orderByQueryClauseElement : orderByClauseElements) {
         StringBuilder orderByQueryElement = new StringBuilder();
         switch (orderByQueryClauseElement.getQueryElementType()) {
            case SIMPLE_STRING:
               orderByQueryElement.append(orderByQueryClauseElement.getSimpleString());
               break;
            case CASE_STATEMENT:
               List<String> caseStatements = orderByQueryClauseElement.getCaseStatement();
               orderByQueryElement.append(getCaseStatementString(caseStatements));
               break;
         }
         if (orderByQueryClauseElement.getOrderEntityType() != null) {
            orderByQueryElement.append(SQL_SPACE_DELIMITER).append(
                     orderByQueryClauseElement.getOrderEntityType().getValue());
         }
         orderByClauses.add(orderByQueryElement.toString());
      }
      return ExecueCoreUtil.joinCollection(orderByClauses);
   }

   public static String prepareOrderByClauseAliasString (List<QueryClauseElement> orderByClauseElements) {
      List<String> orderByClauses = new ArrayList<String>();
      if (ExecueCoreUtil.isListElementsNotEmpty(orderByClauseElements)) {
         for (QueryClauseElement queryClauseElement : orderByClauseElements) {
            String alias = queryClauseElement.getAlias();
            if (ExecueCoreUtil.isEmpty(alias)) {
               orderByClauses.add(queryClauseElement.getSimpleString() + SQL_SPACE_DELIMITER
                        + queryClauseElement.getOrderEntityType().getValue());
            } else {
               orderByClauses.add(alias + SQL_SPACE_DELIMITER + queryClauseElement.getOrderEntityType().getValue());
            }
         }
      }
      return ExecueCoreUtil.joinCollection(orderByClauses);
   }
}
