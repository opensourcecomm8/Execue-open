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


package com.execue.ac.bean;

import java.util.List;
import java.util.Map;

/**
 * This bean represents the table constraints information.It contains table name and list of primary key column names
 * and list of foreign key columns. The foreign key column has information about parent table and parent colum seperated
 * by '.' as delimeter
 * 
 * @author Vishay
 * @version 1.0
 * @since 27/06/09
 */
public class TableConstraintsInfo {

   private String              tableName;
   private List<String>        primaryKeyConstraintColumns;
   private Map<String, String> foreignKeyConstraintColumns;

   public String getTableName () {
      return tableName;
   }

   public void setTableName (String tableName) {
      this.tableName = tableName;
   }

   public List<String> getPrimaryKeyConstraintColumns () {
      return primaryKeyConstraintColumns;
   }

   public void setPrimaryKeyConstraintColumns (List<String> primaryKeyConstraintColumns) {
      this.primaryKeyConstraintColumns = primaryKeyConstraintColumns;
   }

   public Map<String, String> getForeignKeyConstraintColumns () {
      return foreignKeyConstraintColumns;
   }

   public void setForeignKeyConstraintColumns (Map<String, String> foreignKeyConstraintColumns) {
      this.foreignKeyConstraintColumns = foreignKeyConstraintColumns;
   }

}
