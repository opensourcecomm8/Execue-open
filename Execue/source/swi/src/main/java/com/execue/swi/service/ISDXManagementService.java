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


package com.execue.swi.service;

import java.util.List;

import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.bean.entity.AppDataSource;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.AssetOperationInfo;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Constraint;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.HistoryAssetOperationInfo;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.bean.governor.AssetEntityTerm;
import com.execue.swi.exception.SDXException;

public interface ISDXManagementService extends ISDXCommonService {

   // ********************* Methods related to DataSource starts **************************

   public void createDataSource (DataSource dataSource) throws SDXException;

   public void updateDataSource (DataSource dataSource) throws SDXException;

   // ********************* Methods related to DataSource ends **************************

   // ********************* Methods related to Asset starts **************************

   public void createAsset (Asset asset) throws SDXException;

   public void updateAsset (Asset asset) throws SDXException;

   // ********************* Methods related to Asset ends **************************

   // ********************* Methods related to Table starts **************************

   public void createAssetTables (Asset asset, List<TableInfo> tablesInfo) throws SDXException;

   public void updateAssetTables (Asset asset, List<TableInfo> tablesInfo) throws SDXException;

   public void updateTable (Tabl table) throws SDXException;

   public void updateTables (List<Tabl> tables) throws SDXException;

   // ********************* Methods related to Table ends **************************

   // ********************* Methods related to Column starts **************************

   public void createColumns (Asset asset, Tabl table, List<Colum> columns) throws SDXException;

   public void updateColumn (Long assetId, Long tableId, Colum colum) throws SDXException;

   public void updateColumns (Long assetId, Long tableId, List<Colum> colums) throws SDXException;

   // ********************* Methods related to Column ends **************************

   // ********************* Methods related to Member starts **************************

   public void createMembers (Asset asset, Tabl table, List<Colum> columns, List<Membr> members) throws SDXException;

   public void updateMember (Membr membr) throws SDXException;

   public void updateMembers (List<Membr> members) throws SDXException;

   // ********************* Methods related to Member ends **************************

   // ********************* Methods related to Constraints starts **************************

   public void createConstraints (List<Constraint> constraints) throws SDXException;

   public void updateConstraints (List<Constraint> constraints) throws SDXException;

   // ********************* Methods related to Constraints ends **************************

   // ********************* Methods related to AED's starts **************************
   public void updateAssetEntityDefinition (AssetEntityDefinition assetEntityDefinition) throws SDXException;

   public Integer updateAssetEntitiesPopularity (List<AssetEntityTerm> assetEntityTerms) throws SDXException;

   // ********************* Methods related to AssetOperation and AssetHistoryOperation's starts
   // **************************

   public void createAssetOperation (AssetOperationInfo assetOperationInfo) throws SDXException;

   public void createHistoryAssetOperation (HistoryAssetOperationInfo historyAssetOperationInfo) throws SDXException;

   public void updateAssetOperation (AssetOperationInfo assetOperationInfo) throws SDXException;

   public void updateHistoryAssetOperation (HistoryAssetOperationInfo historyAssetOperationInfo) throws SDXException;

   // ********************* Methods related to AssetOperation and AssetHistoryOperation's ends
   // **************************

   // ******************************* Miscellaneous methods*********************************************

   public boolean isAssetTableColumnChangedNameUnique (Long assetId, Long tableId, Long columnId,
            String columnDisplayName) throws SDXException;

   public void createAppDataSourceMapping (AppDataSource appDataSource) throws SDXException;

   public void createAppDataSourceMappings (List<AppDataSource> appDataSources) throws SDXException;

   public void updateAssetStatus (Long assetId, StatusEnum status) throws SDXException;

}
