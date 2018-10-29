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

import java.util.List;

public class SQLIndex {

   private String       name;
   private String       tableName;
   private List<String> columnNames;
   private boolean      unique = false;

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getTableName () {
      return tableName;
   }

   public void setTableName (String tableName) {
      this.tableName = tableName;
   }

   public List<String> getColumnNames () {
      return columnNames;
   }

   public void setColumnNames (List<String> columnNames) {
      this.columnNames = columnNames;
   }

   public boolean isUnique () {
      return unique;
   }

   public void setUnique (boolean unique) {
      this.unique = unique;
   }

}
