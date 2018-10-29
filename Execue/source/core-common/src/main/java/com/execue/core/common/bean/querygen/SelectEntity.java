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


package com.execue.core.common.bean.querygen;

import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.type.StatType;

public class SelectEntity {

   private StatType         functionName;
   private SelectEntityType type;
   private QueryTableColumn tableColumn;
   private Query            subQuery;
   private QueryFormula     formula;
   private String           alias;
   private QueryValue       queryValue;
   private Range            range;
   private QueryColumn      roundFunctionTargetColumn;
   private boolean          dateAsStringRequired;

   public SelectEntityType getType () {
      return type;
   }

   public void setType (SelectEntityType type) {
      this.type = type;
   }

   public QueryTableColumn getTableColumn () {
      return tableColumn;
   }

   public void setTableColumn (QueryTableColumn tableColumn) {
      this.tableColumn = tableColumn;
   }

   public Query getSubQuery () {
      return subQuery;
   }

   public void setSubQuery (Query subQuery) {
      this.subQuery = subQuery;
   }

   public QueryFormula getFormula () {
      return formula;
   }

   public void setFormula (QueryFormula formula) {
      this.formula = formula;
   }

   public String getAlias () {
      return alias;
   }

   public void setAlias (String alias) {
      this.alias = alias != null ? alias.toUpperCase() : alias;
   }

   public QueryValue getQueryValue () {
      return queryValue;
   }

   public void setQueryValue (QueryValue queryValue) {
      this.queryValue = queryValue;
   }

   public Range getRange () {
      return range;
   }

   public void setRange (Range range) {
      this.range = range;
   }

   public StatType getFunctionName () {
      return functionName;
   }

   public void setFunctionName (StatType functionName) {
      this.functionName = functionName;
   }

   public QueryColumn getRoundFunctionTargetColumn () {
      return roundFunctionTargetColumn;
   }

   public void setRoundFunctionTargetColumn (QueryColumn roundFunctionTargetColumn) {
      this.roundFunctionTargetColumn = roundFunctionTargetColumn;
   }

   public boolean isDateAsStringRequired () {
      return dateAsStringRequired;
   }

   public void setDateAsStringRequired (boolean dateAsStringRequired) {
      this.dateAsStringRequired = dateAsStringRequired;
   }

}
