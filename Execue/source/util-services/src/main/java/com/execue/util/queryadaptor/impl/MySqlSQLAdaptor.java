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

import com.execue.core.common.bean.querygen.QueryClauseElement;
import com.execue.core.common.bean.querygen.QueryStructure;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.type.DataType;
import com.execue.core.constants.ISQLQueryConstants;
import com.execue.util.queryadaptor.ISQLAdaptor;

/**
 * @author Nitesh
 */
public class MySqlSQLAdaptor implements ISQLAdaptor, ISQLQueryConstants {

   public QueryStructure handleLimitClause (QueryStructure queryStructure) {
      QueryClauseElement limitElement = queryStructure.getLimitElement();
      String limitEntity = limitElement.getSimpleString();
      String[] tokens = limitEntity.split(",");
      Long startingNumber = new Long(tokens[0]);
      Long endingNumber = new Long(tokens[1]);
      Long offset = startingNumber - 1;
      Long maxRows = endingNumber - offset;
      StringBuilder sblimitClause = new StringBuilder();
      sblimitClause.append(MYSQL_LIMIT_CLAUSE).append(SQL_SPACE_DELIMITER).append(offset).append(SQL_ENTITY_SEPERATOR)
               .append(maxRows);
      limitElement.setSimpleString(sblimitClause.toString());
      return queryStructure;
   }

   @Override
   public String createRandomNumberGeneratorFormula (Integer lowerBound, Integer upperBound) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(SQL_MYSQL_FLOOR_FUNCTION + "(");
      stringBuilder.append(lowerBound);
      stringBuilder.append(" + (");
      stringBuilder.append(SQL_MYSQL_RAND_FUNCTION);
      stringBuilder.append("() * ");
      stringBuilder.append(upperBound);
      stringBuilder.append("))");
      return stringBuilder.toString();
   }

   @Override
   public String getAutoIncrementClause () {
      return SQL_MYSQL_AUTO_INCREMENT_VALUE;
   }

   @Override
   public String getNullRepresentationFunction (DataType dataType) {
      return MYSQL_NULL_CHECK_KEYWORD;
   }

   @Override
   public boolean isAutoIncrementClauseSupported () {
      return true;
   }
   
   @Override
   public boolean isMultipleIndexesWithSingleStatementSupported () {
      return true;
   }

   @Override
   public String createColumRepresentationQueryTableColumn (QueryTableColumn queryTableColumn) {
      StringBuilder columnRepresentation = new StringBuilder();
      columnRepresentation.append(queryTableColumn.getTable().getAlias()).append(SQL_ALIAS_SEPERATOR).append(
               SQL_COLUMN_BINDER).append(queryTableColumn.getColumn().getColumnName()).append(SQL_COLUMN_BINDER);
      return columnRepresentation.toString();
   }

   @Override
   public String createTableRepresentationQueryTableColumn (QueryTable queryTable, boolean appendAlias) {
      StringBuilder stringBuildler = new StringBuilder();
      stringBuildler.append(SQL_COLUMN_BINDER).append(queryTable.getTableName()).append(SQL_COLUMN_BINDER);
      if (appendAlias) {
         stringBuildler.append(SQL_SPACE_DELIMITER).append(queryTable.getAlias());
      }
      return stringBuildler.toString();
   }
}
