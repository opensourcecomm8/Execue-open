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


package com.execue.core.common.bean.entity;

import java.io.Serializable;

import com.execue.core.common.bean.IAssetEntity;

public class Indx implements Serializable, IAssetEntity {

   private String name;
   private String tableName;
   private String columnName;
   private int    ordinalPosition;
   private int    type;
   private String descend;

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

   public String getColumnName () {
      return columnName;
   }

   public void setColumnName (String columnName) {
      this.columnName = columnName;
   }

   public int getOrdinalPosition () {
      return ordinalPosition;
   }

   public void setOrdinalPosition (int ordinalPosition) {
      this.ordinalPosition = ordinalPosition;
   }

   /*
    * Types of Indexes: tableIndexStatistic = 0 tableIndexClustered = 1 tableIndexHashed = 2 tableIndexOther = 3
    */
   public int getType () {
      return type;
   }

   public void setType (int type) {
      this.type = type;
   }

   public String getDescend () {
      return descend;
   }

   public void setDescend (String descend) {
      this.descend = descend;
   }
}
