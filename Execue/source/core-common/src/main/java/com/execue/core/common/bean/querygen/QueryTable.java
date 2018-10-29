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

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.LookupType;

public class QueryTable {

   private String     owner;
   private String     actualName;
   private String     tableName;
   private String     alias;
   private LookupType tableType;
   private CheckType  virtual;

   public String getTableName () {
      return tableName;
   }

   public void setTableName (String tableName) {
      this.tableName = tableName;
   }

   public String getAlias () {
      return alias;
   }

   public void setAlias (String alias) {
      this.alias = alias;
   }

   @Override
   public String toString () {
      return getTableName().concat(getAlias()).concat(getTableType().getValue());
   }

   @Override
   public boolean equals (Object obj) {
      if (obj instanceof QueryTable) {
         QueryTable queryTable = (QueryTable) obj;
         return this.toString().equalsIgnoreCase(queryTable.toString());
      } else {
         return false;
      }
   }

   @Override
   public int hashCode () {
      return this.toString().hashCode();
   }

   public LookupType getTableType () {
      return tableType;
   }

   public void setTableType (LookupType tableType) {
      this.tableType = tableType;
   }

   public String getOwner () {
      return owner;
   }

   public void setOwner (String owner) {
      this.owner = owner;
   }

   public String getActualName () {
      return actualName;
   }

   public void setActualName (String actualName) {
      this.actualName = actualName;
   }

   public CheckType getVirtual () {
      return virtual;
   }

   public void setVirtual (CheckType virtual) {
      this.virtual = virtual;
   }
}
