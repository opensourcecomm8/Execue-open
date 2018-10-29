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


package com.execue.core.common.bean.governor;

import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConstraintSubType;
import com.execue.core.common.type.LookupType;

/**
 * This class represents the light weighted asset entity definition object.
 * 
 * @author Vishay
 * @version 1.0
 * @since 08/12/09
 */
public class LightAssetEntityDefinitionInfo {

   private Long              assetEntityDefinitionId;
   private Long              assetId;
   private LookupType        tableLookupType;
   private ColumnType        columnType;
   private ConstraintSubType primaryKey;

   public Long getAssetEntityDefinitionId () {
      return assetEntityDefinitionId;
   }

   public void setAssetEntityDefinitionId (Long assetEntityDefinitionId) {
      this.assetEntityDefinitionId = assetEntityDefinitionId;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public LookupType getTableLookupType () {
      return tableLookupType;
   }

   public void setTableLookupType (LookupType tableLookupType) {
      this.tableLookupType = tableLookupType;
   }

   public ColumnType getColumnType () {
      return columnType;
   }

   public void setColumnType (ColumnType columnType) {
      this.columnType = columnType;
   }

   public ConstraintSubType getPrimaryKey () {
      return primaryKey;
   }

   public void setPrimaryKey (ConstraintSubType primaryKey) {
      this.primaryKey = primaryKey;
   }
}
