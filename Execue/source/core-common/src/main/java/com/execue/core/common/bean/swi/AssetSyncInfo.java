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

import java.io.Serializable;
import java.util.List;

/**
 * This bean represents the asset level sync information
 * 
 * @author Vishay
 * @version 1.0
 * @since 20/08/09
 */
public class AssetSyncInfo implements Serializable {

   private Long                 assetId;
   private TableSyncInfo        tableSyncInfo;
   private List<ColumnSyncInfo> columnSyncInfo;
   private List<MemberSyncInfo> memberSyncInfo;
   private boolean              syncChangedFound;

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public TableSyncInfo getTableSyncInfo () {
      return tableSyncInfo;
   }

   public void setTableSyncInfo (TableSyncInfo tableSyncInfo) {
      this.tableSyncInfo = tableSyncInfo;
   }

   public List<ColumnSyncInfo> getColumnSyncInfo () {
      return columnSyncInfo;
   }

   public void setColumnSyncInfo (List<ColumnSyncInfo> columnSyncInfo) {
      this.columnSyncInfo = columnSyncInfo;
   }

   public List<MemberSyncInfo> getMemberSyncInfo () {
      return memberSyncInfo;
   }

   public void setMemberSyncInfo (List<MemberSyncInfo> memberSyncInfo) {
      this.memberSyncInfo = memberSyncInfo;
   }

   public boolean isSyncChangedFound () {
      return syncChangedFound;
   }

   public void setSyncChangedFound (boolean syncChangedFound) {
      this.syncChangedFound = syncChangedFound;
   }

}
