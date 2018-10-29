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


package com.execue.handler.bean;

import com.execue.core.common.type.CheckType;

public class UITableForJoins {

   private String    tableName;
   private String    tableDisplayName;
   private Long      tableId;
   boolean           isJoined;
   private CheckType virtual;

   public String getTableName () {
      return tableName;
   }

   public void setTableName (String tableName) {
      this.tableName = tableName;
   }

   public Long getTableId () {
      return tableId;
   }

   public void setTableId (Long tableId) {
      this.tableId = tableId;
   }

   public boolean isJoined () {
      return isJoined;
   }

   public void setJoined (boolean isJoined) {
      this.isJoined = isJoined;
   }

   public CheckType getVirtual () {
      return virtual;
   }

   public void setVirtual (CheckType virtual) {
      this.virtual = virtual;
   }

   /**
    * @return the tableDisplayName
    */
   public String getTableDisplayName () {
      return tableDisplayName;
   }

   /**
    * @param tableDisplayName
    *           the tableDisplayName to set
    */
   public void setTableDisplayName (String tableDisplayName) {
      this.tableDisplayName = tableDisplayName;
   }

}
