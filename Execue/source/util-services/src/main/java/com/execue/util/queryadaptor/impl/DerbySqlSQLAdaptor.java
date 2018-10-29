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

import com.execue.core.common.bean.querygen.QueryStructure;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.type.DataType;
import com.execue.core.constants.ISQLQueryConstants;
import com.execue.util.queryadaptor.ISQLAdaptor;


public class DerbySqlSQLAdaptor implements ISQLAdaptor, ISQLQueryConstants {

   @Override
   public String createColumRepresentationQueryTableColumn (QueryTableColumn queryTableColumn) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String createRandomNumberGeneratorFormula (Integer lowerBound, Integer upperBound) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String createTableRepresentationQueryTableColumn (QueryTable queryTable, boolean appendAlias) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getAutoIncrementClause () {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getNullRepresentationFunction (DataType dataType) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public QueryStructure handleLimitClause (QueryStructure queryStructure) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public boolean isAutoIncrementClauseSupported () {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean isMultipleIndexesWithSingleStatementSupported () {
      // TODO Auto-generated method stub
      return false;
   }

}
