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


package com.execue.handler.swi;

import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.DefaultMetric;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.type.CheckType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.handler.IHandler;
import com.execue.handler.bean.UIAssetGrainInfo;
import com.execue.handler.bean.UIColumn;
import com.execue.handler.bean.UIMember;
import com.execue.handler.bean.UITable;

public interface ISDXServiceHandler extends IHandler {

   public void createDataSource (DataSource dataSource) throws ExeCueException;

   public void updateDataSource (DataSource dataSource) throws ExeCueException;

   public DataSource encryptDataSourceCredential (DataSource dataSource) throws ExeCueException;

   public void deleteDataSource (DataSource dataSource) throws ExeCueException;

   public List<DataSource> getDisplayableDataSources () throws ExeCueException;

   public DataSource getDataSource (String dataSourceName) throws ExeCueException;

   public void createAsset (Asset asset) throws ExeCueException;

   public void updateAsset (Asset asset) throws ExeCueException;

   public List<Asset> getAllAssets (Long applicationId) throws ExeCueException;

   public List<Asset> getAllParentAssets (Long applicationId) throws ExeCueException;

   public void updateAssetTables (Asset asset, List<TableInfo> tablesInfo) throws ExeCueException;

   public void updateColumn (Long assetId, Long tableId, Colum column) throws ExeCueException;

   public void createAssetTables (Asset asset, List<TableInfo> tables) throws ExeCueException;

   public List<TableInfo> getAssetTables (Asset asset) throws ExeCueException;

   public Asset getAsset (Long applicationId, String assetName) throws ExeCueException;

   public Asset getAsset (Long assetId) throws ExeCueException;

   public List<UITable> getAllAssetTables (Asset asset) throws ExeCueException;

   public TableInfo getAssetTable (Asset asset, String tableName) throws ExeCueException;

   public TableInfo getTableInfo (Asset asset, String tableName, String description, String owner,
            boolean isVirtualTable) throws ExeCueException;

   public Integer getMembersCountFromSource (Asset asset, Tabl table, Colum lookupColumn) throws ExeCueException;

   public List<Membr> getMembersFromSource (Asset asset, Tabl table) throws ExeCueException;

   // Methods for getting objects from asset source - Start
   public List<UITable> getAllSourceTables (Asset asset) throws ExeCueException;

   public TableInfo getSourceTable (Asset asset, Tabl table) throws ExeCueException;

   // Methods for getting objects from asset source - End

   public void createMembers (Asset asset, Tabl table, List<Colum> columns, List<Membr> members) throws ExeCueException;

   public void deleteMembers (List<Membr> members) throws ExeCueException;

   public List<String> deleteTables (Asset asset, List<String> tableNamesForDeletion) throws ExeCueException;

   public Long deleteAsset (Long applicationId, Asset asset) throws ExeCueException;

   public void createConstraints (Asset asset) throws ExeCueException;

   public void createDirectJoins (Asset asset) throws ExeCueException;

   public void saveAssetGrain (Long modelId, UIAssetGrainInfo assetGrainInfo, Long assetId) throws ExeCueException;

   public UIAssetGrainInfo getAssetGrainInfo (Long modelId, Long assetId) throws ExeCueException;

   public List<UITable> getSimpleHierarchicalLookupTables (Long assetId) throws ExeCueException;

   public List<UITable> getAllTables (Asset asset) throws ExeCueException;

   public void createMembersByJob (Asset asset, Tabl table, int totalMembers) throws ExeCueException;

   public boolean isAssetExistsUnderBatchProcess (Long assetId) throws ExeCueException;

   public boolean isTableUnderBatchProcess (Long tableId) throws ExeCueException;

   public boolean isParentAsset (Long assetId, Long applicationId) throws ExeCueException;

   public List<Asset> getAllInactiveAssets (Long applicationId) throws ExeCueException;

   public List<UIColumn> getUIColumnsOFTable (UITable uiTable) throws ExeCueException;

   public Asset getAssetPopulatedByApplicationOwner (Long assetId) throws ExeCueException;

   public DataSource getDataSourceForUploadedDatasets () throws ExeCueException;

   public void updateTableForSystemDefaultMetric (Long id, CheckType eligibleSystemDefaultMetric)
            throws ExeCueException;

   public void saveUpdateDefaultMetrics (Long id, List<DefaultMetric> existingDefaultMetrics) throws ExeCueException;

   public List<UITable> getAllAssetFactTables (Asset asset) throws ExeCueException;

   public TableInfo prepareVirtualTableInfo (Asset asset, Tabl virtualTable) throws ExeCueException;

   public Tabl getTableById (Long tableId) throws ExeCueException;

   public List<Colum> getAssetTableColumnsByPage (Long assetId, Long tableId, Page page) throws ExeCueException;

   public List<Asset> getAssetsByApplicationId (Long applicationId) throws HandlerException;

   public boolean isColumnUnderBatchProcess (Long columnId) throws ExeCueException;

   public boolean isAssetExists (Long applicationId, String assetName) throws ExeCueException;

   public Map<String, List<Asset>> validateAssetUnderMaintenance (Long applicationId, Asset asset)
            throws HandlerException;

   public List<UIMember> getAssetTableMembersByPage (Long selectedAssetId, Long selectedTableId, Page pageDetail)
            throws HandlerException;
}
