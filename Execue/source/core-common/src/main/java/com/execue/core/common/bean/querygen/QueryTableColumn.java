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

public class QueryTableColumn implements Cloneable {

   private QueryTable  table;
   private QueryColumn column;

   public QueryTable getTable () {
      return table;
   }

   public void setTable (QueryTable table) {
      this.table = table;
   }

   public QueryColumn getColumn () {
      return column;
   }

   public void setColumn (QueryColumn column) {
      this.column = column;
   }

   @Override
   public boolean equals (Object obj) {
      if (obj instanceof QueryTableColumn) {
         QueryTableColumn queryTableColumn = (QueryTableColumn) obj;
         return this.toString().equalsIgnoreCase(queryTableColumn.toString());
      } else {
         return false;
      }
   }

   @Override
   public int hashCode () {
      return this.toString().hashCode();
   }

   @Override
   public String toString () {
      return getTable().toString() + getColumn().getColumnName();
   }
}
