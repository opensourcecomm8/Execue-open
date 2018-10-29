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


package com.execue.core.common.bean;

import java.util.List;

/**
 * @author Vishy Dasari
 */
public class SQLExeCueResultSetMetaData implements ExeCueResultSetMetaData {

   private int           columnCount;
   private List<String>  columnNames;
   private List<String>  columnLabels;
   private List<String>  columnTypeNames;
   private List<String>  catalogNames;
   private List<String>  schemaNames;
   private List<String>  tableNames;
   private List<Integer> columnTypes;
   private List<Integer> columnPrecisions;
   private List<Integer> columnScales;

   public int getColumnCount () throws Exception {
      return this.columnCount;
   }

   public String getColumnName (int index) throws Exception {
      return this.columnNames.get(index);
   }

   public String getColumnLabel (int index) throws Exception {
      return this.columnLabels.get(index);
   }

   public String getColumnTypeName (int index) throws Exception {
      return this.columnTypeNames.get(index);
   }

   public String getCatalogName (int index) throws Exception {
      return this.catalogNames.get(index);
   }

   public String getSchemaName (int index) throws Exception {
      return this.schemaNames.get(index);
   }

   public String getTableName (int index) throws Exception {
      return this.tableNames.get(index);
   }

   public Integer getColumnPrecision (int index) throws Exception {
      return this.columnPrecisions.get(index);
   }

   public Integer getColumnScale (int index) throws Exception {
      return this.columnScales.get(index);
   }

   public Integer getColumnType (int index) throws Exception {
      return this.columnTypes.get(index);
   }

   public void setColumnCount (int columnCount) {
      this.columnCount = columnCount;
   }

   public void setColumnNames (List<String> columnNames) {
      this.columnNames = columnNames;
   }

   public void setColumnLabels (List<String> columnLabels) {
      this.columnLabels = columnLabels;
   }

   public void setColumnTypeNames (List<String> columnTypeNames) {
      this.columnTypeNames = columnTypeNames;
   }

   public void setCatalogNames (List<String> catalogNames) {
      this.catalogNames = catalogNames;
   }

   public void setSchemaNames (List<String> schemaNames) {
      this.schemaNames = schemaNames;
   }

   public void setTableNames (List<String> tableNames) {
      this.tableNames = tableNames;
   }

   public void setColumnPrecisions (List<Integer> columnPrecisions) {
      this.columnPrecisions = columnPrecisions;
   }

   public void setColumnScales (List<Integer> columnScales) {
      this.columnScales = columnScales;
   }

   public void setColumnTypes (List<Integer> columnTypes) {
      this.columnTypes = columnTypes;
   }
}
