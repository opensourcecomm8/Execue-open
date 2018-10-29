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

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.querygen.QueryClauseElement;
import com.execue.core.common.bean.querygen.QueryStructure;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.QueryElementType;
import com.execue.core.constants.ISQLQueryConstants;
import com.execue.util.queryadaptor.ISQLAdaptor;

/**
 * @author Nitesh
 */
public class SASSQLAdaptor implements ISQLAdaptor, ISQLQueryConstants {

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.util.queryadaptor.ISQLAdaptor#handleLimitClause(com.execue.core.common.bean.querygen.QueryStructure)
    */
   public QueryStructure handleLimitClause (QueryStructure queryStructure) {
      QueryClauseElement limitElement = queryStructure.getLimitElement();
      String limitEntity = limitElement.getSimpleString();
      String[] tokens = limitEntity.split(",");
      Long startingNumber = new Long(tokens[0]);
      Long endingNumber = new Long(tokens[1]);
      List<QueryClauseElement> fromElements = queryStructure.getFromElements();
      List<QueryClauseElement> joinFromElements = queryStructure.getJoinElements();
      if (!CollectionUtils.isEmpty(fromElements)) {
         // Prepare the limit clause condition using SAS (FIRSOBS=1, OBS=50) syntax 
         StringBuilder sblimitClause = new StringBuilder();
         sblimitClause.append(SQL_SPACE_DELIMITER).append(SQL_FUNCTION_START_WRAPPER)
                  .append(SAS_LIMIT_CLAUSE_FIRST_OBS).append(SQL_SPACE_DELIMITER).append(JOIN_CONDITION_OPERATOR)
                  .append(SQL_SPACE_DELIMITER).append(startingNumber).append(SQL_SPACE_DELIMITER).append(
                           SAS_LIMIT_CLAUSE_OBS).append(SQL_SPACE_DELIMITER).append(JOIN_CONDITION_OPERATOR).append(
                           SQL_SPACE_DELIMITER).append(endingNumber).append(SQL_FUNCTION_END_WRAPPER);

         // Update the existing from clause
         QueryClauseElement fromClauseElement = fromElements.get(0);
         fromClauseElement.setSimpleString(fromClauseElement.getSimpleString() + sblimitClause);
      } else if (!CollectionUtils.isEmpty(joinFromElements)) {

         // Prepare the limit clause condition using SAS MONOTONIC i.e. MONOTONIC() BETWEEN 1 AND 50
         StringBuilder limitClauseConditionUsingMonotonic = new StringBuilder();
         limitClauseConditionUsingMonotonic.append(SAS_LIMIT_CLAUSE_MONOTONIC).append(SQL_FUNCTION_START_WRAPPER)
                  .append(SQL_FUNCTION_END_WRAPPER).append(SQL_SPACE_DELIMITER).append(SQL_BETWEEN_FUNCTION).append(
                           SQL_SPACE_DELIMITER).append(startingNumber).append(SQL_SPACE_DELIMITER).append(
                           WHERE_CLAUSE_SEPERATOR).append(SQL_SPACE_DELIMITER).append(endingNumber);

         QueryClauseElement whereClauseElement = new QueryClauseElement();
         whereClauseElement.setQueryElementType(QueryElementType.SIMPLE_STRING);
         whereClauseElement.setSimpleString(limitClauseConditionUsingMonotonic.toString());

         queryStructure.getWhereElements().add(whereClauseElement);
      }
      limitElement.setSimpleString(null); // NULLIFY THE LIMIT STRING AS IS BEING HANDLED IN ABOVE FROM CLAUSE OR WHERE CLAUSE
      return queryStructure;
   }

   @Override
   public String createRandomNumberGeneratorFormula (Integer lowerBound, Integer upperBound) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(SQL_MYSQL_FLOOR_FUNCTION + "(");
      stringBuilder.append(lowerBound);
      stringBuilder.append(" + (");
      stringBuilder.append(SQL_SAS_RAND_FUNCTION);
      stringBuilder.append("(1) * ");
      stringBuilder.append(upperBound);
      stringBuilder.append("))");
      return stringBuilder.toString();
   }

   @Override
   public String getAutoIncrementClause () {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getNullRepresentationFunction (DataType dataType) {
      String functionName = SAS_NULL_CHECK_KEYWORD;
      switch (dataType) {
         case NUMBER:
         case LARGE_INTEGER:
         case INT:
            functionName = SAS_NULL_CHECK_KEYWORD;
            break;
         case CHARACTER:
         case STRING:
         case TIME:
         case DATE:
         case DATETIME:
            functionName = SAS_NULL_CHECK_KEYWORD;
            break;
      }
      return functionName;
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
      stringBuildler.append(tableOwner.trim()).append(SQL_ALIAS_SEPERATOR).append(tableName.trim());
      if (appendAlias) {
         String tableAlias = queryTable.getAlias();
         stringBuildler.append(SQL_SPACE_DELIMITER).append(tableAlias);
      }
      return stringBuildler.toString();
   }
}