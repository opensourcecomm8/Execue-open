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


package com.execue.core.common.bean.aggregation;

/**
 * This bean class is for holding data of one column and is part of the Row data
 * 
 * @author John Mallavalli
 */

public class QueryDataColumnData {

   private String columnName;
   private String columnValue;

   public QueryDataColumnData () {
   }

   public QueryDataColumnData (String columnName, String columnValue) {
      this.columnName = columnName;
      this.columnValue = columnValue;
   }

   public String getColumnName () {
      return columnName;
   }

   public void setColumnName (String columnName) {
      this.columnName = columnName;
   }

   public String getColumnValue () {
      return columnValue;
   }

   public void setColumnValue (String columnValue) {
      this.columnValue = columnValue;
   }
}