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


package com.execue.core.common.bean.swi;

import com.execue.core.common.type.AssetEntityType;

public class PopularityAssetEntityDefinitionInfo {

   private String          assetName;
   private String          tableName;
   private String          ColumnName;
   private String          membrName;
   private Long            popularity;
   private AssetEntityType assetEntityType;

   public String getAssetName () {
      return assetName;
   }

   public void setAssetName (String assetName) {
      this.assetName = assetName;
   }

   public String getTableName () {
      return tableName;
   }

   public void setTableName (String tableName) {
      this.tableName = tableName;
   }

   public String getColumnName () {
      return ColumnName;
   }

   public void setColumnName (String columnName) {
      ColumnName = columnName;
   }

   public String getMembrName () {
      return membrName;
   }

   public void setMembrName (String membrName) {
      this.membrName = membrName;
   }

   public AssetEntityType getAssetEntityType () {
      return assetEntityType;
   }

   public void setAssetEntityType (AssetEntityType assetEntityType) {
      this.assetEntityType = assetEntityType;
   }

   public Long getPopularity () {
      return popularity;
   }

   public void setPopularity (Long popularity) {
      this.popularity = popularity;
   }

}
