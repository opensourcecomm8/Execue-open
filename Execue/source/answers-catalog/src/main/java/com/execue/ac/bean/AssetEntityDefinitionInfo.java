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

/**
 * Class used for creating a cube.
 * 
 * @author Vishay
 * @version 1.0
 */
public class AssetEntityDefinitionInfo {

   @Override
   public boolean equals (Object obj) {
      if (obj instanceof AssetEntityDefinitionInfo) {
         AssetEntityDefinitionInfo assetEntityDefinitionInfo = (AssetEntityDefinitionInfo) obj;
         return (this.assetName.equalsIgnoreCase(assetEntityDefinitionInfo.getAssetName())
                  && this.tableName.equalsIgnoreCase(assetEntityDefinitionInfo.getTableName()) && this.columnName
                  .equalsIgnoreCase(assetEntityDefinitionInfo.getColumnName()));
      }
      return false;
   }

   @Override
   public int hashCode () {
      // TODO : -VG- better implementation is required
      return 100;
   }

   String columnName;
   String tableName;
   String assetName;
   String memberName;

   public String getColumnName () {
      return columnName;
   }

   public void setColumnName (String columnName) {
      this.columnName = columnName;
   }

   public String getTableName () {
      return tableName;
   }

   public void setTableName (String tableName) {
      this.tableName = tableName;
   }

   public String getAssetName () {
      return assetName;
   }

   public void setAssetName (String assetName) {
      this.assetName = assetName;
   }

   public String getMemberName () {
      return memberName;
   }

   public void setMemberName (String memberName) {
      this.memberName = memberName;
   }
}