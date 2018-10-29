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


package com.execue.web.core.action.swi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.PageSearch;
import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.IndicatorBehaviorType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.PageSearchType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.handler.bean.UIJobRequestResult;
import com.execue.handler.bean.UIJobRequestStatus;
import com.execue.handler.bean.UITable;

public class SDXAction extends SWIPaginationAction {

   /**
    * 
    */
   private static final long        serialVersionUID                  = 1L;
   private static final Logger      log                               = Logger.getLogger(SDXAction.class);

   private static final Integer     MAX_ALLOWED_MEMBERS_MAIN_FLOW     = 20;
   private static final Integer     MAX_ALLOWED_MEMBERS_FOR_INDICATOR = 2;

   private Asset                    asset;
   private List<Asset>              assets;

   private List<Tabl>               tables;
   private List<Colum>              cols;
   private List<Membr>              members;
   private Tabl                     table;
   private List<TableInfo>          tableInfoList;
   private List<DataSource>         dataSources;
   private List<UITable>            sourceTables;
   private List<UITable>            assetTables;
   private String                   dataSourceName;
   private String                   assetName;
   private String                   tableNameFromSource;
   private String                   tableNameFromAsset;
   private SDXStatus                sdxStatus;
   private List<Colum>              columnsForSelection;
   private List<String>             tableNamesForDeletion;
   private List<Long>               grainIds;
   private List<UITable>            parentTables;

   private List<Colum>              subListForColumnInfo;
   private List<Membr>              subListForMemberInfo;
   private String                   sourceName                        = ASSET;
   private String                   SOURCE                            = "source";
   // TODO:read from constants file
   public static int                PAGESIZE                          = 8;
   public static int                COLUMN_MEMBER_PAGESIZE            = 5;
   public static int                numberOfLinks                     = 8;
   public static int                PAGESIZEFORSOURCETABLES           = 9;
   public static int                numberOfLinksForSourceTables      = 4;
   public static int                PAGESIZEFORASSETDETAILS           = 11;
   public static int                numberOfLinksForAssetDetails      = 4;
   private boolean                  updatingColumnInfo                = true;
   private boolean                  updatingMemberInfo                = true;
   private String                   batchProcessTable;
   private String                   paginationType;
   private UIJobRequestResult       jobRequestResult;
   private UIJobRequestStatus       jobRequest;
   private Map<String, List<Asset>> underProcessAssetMap;
   private String                   assetUnderProcess                 = "false";
   private static final String      ASSET_UNDER_PROCESS               = "assetUnderProcess";

   // Action Methods

   public String list () {
      try {
         assets = getSdxServiceHandler().getAllAssets(getApplicationContext().getAppId());
         if (paginationType != null && paginationType.equalsIgnoreCase("availableAssets")) {
            paginationForAssets();
         }

      } catch (ExeCueException exeCueException) {
         sdxStatus.setMessage(exeCueException.getMessage());
         log.error(exeCueException, exeCueException);
      }
      return SUCCESS;
   }

   @SuppressWarnings ("unchecked")
   public String assetSubListForAssetDetails () {

      int reqPageNo = Integer.parseInt(getRequestedPage());

      int fromIndex = 1;
      int toIndex = 1;

      if (paginationType != null && paginationType.equalsIgnoreCase("availableAssets")) {
         List<Asset> assetList = (List<Asset>) getHttpSession().get("ASSETSFORPAGINATION");

         int tempTotCount = (assetList.size() / PAGESIZEFORASSETDETAILS);
         int rmndr = assetList.size() % PAGESIZEFORASSETDETAILS;
         if (rmndr != 0) {
            tempTotCount++;
         }

         if (reqPageNo <= tempTotCount) {
            fromIndex = ((reqPageNo - 1) * PAGESIZEFORASSETDETAILS);
            toIndex = reqPageNo * PAGESIZEFORASSETDETAILS;
            if (toIndex > assetList.size())
               toIndex = (assetList.size());
         }

         log.info("Getting Assets SubList from -> " + fromIndex + " to " + toIndex);
         assets = assetList.subList(fromIndex, toIndex);
      }
      return SUCCESS;
   }

   public String validateAssetUnderMaintenance () {
      try {
         Map<String, List<Asset>> assetExistsInAcmqMap = getSdxServiceHandler().validateAssetUnderMaintenance(
                  getApplicationContext().getAppId(), asset);
         if (assetExistsInAcmqMap.size() > 0) {
            setUnderProcessAssetMap(assetExistsInAcmqMap);
            setAssetUnderProcess("true");
            return ASSET_UNDER_PROCESS;
         }

      } catch (ExeCueException exeCueException) {
         sdxStatus.setMessage(exeCueException.getMessage());
         log.error(exeCueException, exeCueException);
      }
      return SUCCESS;
   }

   public String deleteAsset () {
      try {
         jobRequestResult = new UIJobRequestResult();
         asset = getSdxServiceHandler().getAsset(asset.getId());
         Long jobRequestId = getSdxServiceHandler().deleteAsset(getApplicationContext().getAppId(), asset);

         setJobRequest(getJobStatusServiceHandler().getJobRequestStatus(jobRequestId));
         getJobRequestResult().setStatus(getJobRequest().getJobStatus());
         getJobRequestResult().setJobId(getJobRequest().getId());
      } catch (ExeCueException exeCueException) {
         sdxStatus.setMessage(exeCueException.getMessage());
         log.error(exeCueException, exeCueException);
      }
      return SUCCESS;
   }

   public String newAsset () {
      try {
         assets = getSdxServiceHandler().getAllAssets(getApplicationContext().getAppId());
         asset = new Asset();
         getDataSourcesForAssetDefinition();
      } catch (ExeCueException execueException) {
         sdxStatus.setMessage(execueException.getMessage());
         log.error(execueException, execueException);
         return SUCCESS;
      }
      return SUCCESS;
   }

   public String showAsset () {

      try {
         // dataSources = getSdxServiceHandler().getAllDataSources();
         assets = getSdxServiceHandler().getAllAssets(getApplicationContext().getAppId());
         Long sessionAssetId = getApplicationContext().getAssetId();
         if (sessionAssetId != null) {
            for (Asset localAsset : assets) {
               if (localAsset.getId().longValue() == sessionAssetId.longValue()) {
                  asset = localAsset;
                  break;
               }
            }
         }

         if (asset == null) {
            asset = new Asset();
         }
      } catch (ExeCueException execueException) {
         sdxStatus.setMessage(execueException.getMessage());
         log.error(execueException, execueException);
         return SUCCESS;
      }

      return SUCCESS;
   }

   public String editAsset () {
      try {
         assets = getSdxServiceHandler().getAllAssets(getApplicationContext().getAppId());
         Long assetId = asset.getId();
         Long sessionAssetId = getApplicationContext().getAssetId();
         if (sessionAssetId != null) {
            for (Asset localAsset : assets) {
               if (localAsset.getId().longValue() == assetId.longValue()) {
                  asset = localAsset;
                  if (asset.getId().longValue() != sessionAssetId.longValue()) {
                     getApplicationContext().setAssetId(assetId);
                  }
                  break;
               }
            }
         }

         if (asset == null) {
            asset = new Asset();
         }
      } catch (ExeCueException execueException) {
         sdxStatus.setMessage(execueException.getMessage());
         log.error(execueException, execueException);
         return ERROR;
      }

      return SUCCESS;
   }

   public String showAssetDefinition () {
      try {
         getDataSourcesForAssetDefinition();
         if (asset != null && asset.getId() != null) {
            asset = getSdxServiceHandler().getAsset(asset.getId());
            if (asset.getId() != getApplicationContext().getAssetId()) {
               getApplicationContext().setAssetId(asset.getId());
               getApplicationContext().setAssetName(asset.getName());
            }
         } else {
            asset = new Asset();
         }
         return SUCCESS;
      } catch (ExeCueException execueException) {
         sdxStatus.setMessage(execueException.getMessage());
         log.error(execueException, execueException);
         return ERROR;
      }
   }

   public String showSourceTables () {
      try {
         populateDefaultStatus();
         asset = getSdxServiceHandler().getAsset(asset.getId());
         sourceTables = getSdxServiceHandler().getAllSourceTables(asset);

         if (paginationType != null && paginationType.equalsIgnoreCase("sourceTables")) {
            paginationForSourceTables();
         }
      } catch (ExeCueException exeCueException) {
         sdxStatus.setMessage(exeCueException.getMessage());
         log.error(exeCueException, exeCueException);
      }
      return SUCCESS;
   }

   @SuppressWarnings ("unchecked")
   public String showSubSourceTables () {

      int reqPageNo = Integer.parseInt(getRequestedPage());

      int fromIndex = 1;
      int toIndex = 1;

      if (paginationType != null && paginationType.equalsIgnoreCase("sourceTables")) {
         List<UITable> tableList = (List<UITable>) getHttpSession().get("SOURCETABLESFORPAGINATION");

         int tempTotCount = (tableList.size() / PAGESIZEFORSOURCETABLES);
         int rmndr = tableList.size() % PAGESIZEFORSOURCETABLES;
         if (rmndr != 0) {
            tempTotCount++;
         }

         if (reqPageNo <= tempTotCount) {
            fromIndex = ((reqPageNo - 1) * PAGESIZEFORSOURCETABLES);
            toIndex = reqPageNo * PAGESIZEFORSOURCETABLES;
            if (toIndex > tableList.size())
               toIndex = (tableList.size());
         }

         log.info("Getting Source tables SubList from -> " + fromIndex + " to " + toIndex);
         sourceTables = tableList.subList(fromIndex, toIndex);
      }
      return SUCCESS;
   }

   public String showAssetTables () {
      try {
         populateDefaultStatus();
         asset = getSdxServiceHandler().getAsset(asset.getId());
         assetTables = getSdxServiceHandler().getAllAssetTables(asset);
      } catch (ExeCueException exeCueException) {
         sdxStatus.setMessage(exeCueException.getMessage());
         log.error(exeCueException, exeCueException);
      }
      return SUCCESS;

   }

   public String showTableInfo () {
      updatingColumnInfo = false;
      updatingMemberInfo = false;
      try {
         populateDefaultStatus();
         asset = getSdxServiceHandler().getAsset(asset.getId());
         boolean isVirtualTable = false;
         if (CheckType.YES.equals(table.getVirtual())) {
            isVirtualTable = true;
         }
         Long tableId = populateTableDefaults(isVirtualTable);
         batchProcessTable = "false";
         if (tableId != null) {
            if (getSdxServiceHandler().isTableUnderBatchProcess(tableId)) {
               batchProcessTable = "true";
               addActionMessage(getText("execue.tables.deletion.under.batchprocess"));
            }
            setSourceName(ASSET);
         } else {
            setSourceName(SOURCE);
            if (isVirtualTable) {
               setSourceName(ASSET);
            }
         }
      } catch (ExeCueException exeCueException) {
         sdxStatus.setMessage(exeCueException.getMessage());
         log.error(exeCueException, exeCueException);
      }
      return SUCCESS;
   }

   @SuppressWarnings ("unchecked")
   public String showSubMemberColumnInfo () {
      setPageSize(COLUMN_MEMBER_PAGESIZE);
      setRequestedPage(getRequestedPage());
      prepareRequestedPageDetail();
      if (paginationType != null && paginationType.equalsIgnoreCase("column")) {
         log.info("Getting Columns SubList from");
         subListForColumnInfo = getColumnPaginationResults((List<Colum>) getHttpSession().get("COLUMNLIST"),
                  getPageDetail());
      } else if (paginationType != null && paginationType.equalsIgnoreCase("member")) {
         log.info("Getting Members SubList from  ");
         subListForMemberInfo = getMemberPaginationResults((List<Membr>) getHttpSession().get("MEMBERLIST"),
                  getPageDetail());
      }

      return SUCCESS;
   }

   private List<Membr> getMemberPaginationResults (List<Membr> memberList, Page pageDetail) {
      List<PageSearch> searchList = pageDetail.getSearchList();
      List<Membr> searchMemberList = new ArrayList<Membr>();
      if (ExecueCoreUtil.isCollectionNotEmpty(searchList)) {
         PageSearch search = searchList.get(0);
         // check for the search info
         if (PageSearchType.STARTS_WITH == search.getType()) {
            for (Membr member : memberList) {
               String cDispName = member.getLookupDescription();
               if (cDispName.toLowerCase().startsWith(search.getString().toLowerCase())) {
                  searchMemberList.add(member);
               }
            }
         } else if (PageSearchType.CONTAINS == search.getType()) {
            for (Membr member : memberList) {
               String cDispName = member.getLookupDescription();
               if (cDispName.toLowerCase().contains(search.getString().toLowerCase())) {
                  searchMemberList.add(member);
               }
            }
         }
      }
      // modify the page object with the new record count which will modify the page count as well
      if (ExecueCoreUtil.isCollectionNotEmpty(searchMemberList) || ExecueCoreUtil.isCollectionNotEmpty(searchList)) {
         pageDetail.setRecordCount(Long.valueOf(searchMemberList.size()));
         getHttpSession().put("MEMBERLIST", searchMemberList);
      } else {
         memberList = (List<Membr>) getHttpSession().get("MEMBERLIST_MAIN");
         pageDetail.setRecordCount(Long.valueOf(memberList.size()));
         getHttpSession().put("MEMBERLIST", memberList);
      }
      // manipulate the list to return the set of concepts belonging to the page requested
      int start = (pageDetail.getRequestedPage().intValue() - 1) * pageDetail.getPageSize().intValue();
      int end = start + pageDetail.getPageSize().intValue();
      if (end > pageDetail.getRecordCount().intValue()) {
         end = (pageDetail.getRecordCount().intValue());
      }
      List<Membr> pageMembers = new ArrayList<Membr>();
      if (ExecueCoreUtil.isCollectionNotEmpty(searchMemberList) || ExecueCoreUtil.isCollectionNotEmpty(searchList)) {
         pageMembers = searchMemberList.subList(start, end);
      } else {
         pageMembers = memberList.subList(start, end);
      }
      return pageMembers;
   }

   private List<Colum> getColumnPaginationResults (List<Colum> columnList, Page pageDetail) {
      List<PageSearch> searchList = pageDetail.getSearchList();
      List<Colum> searchColumnList = new ArrayList<Colum>();
      if (ExecueCoreUtil.isCollectionNotEmpty(searchList)) {
         PageSearch search = searchList.get(0);
         // check for the search info
         if (PageSearchType.STARTS_WITH == search.getType()) {
            for (Colum dataset : columnList) {
               String cDispName = dataset.getDisplayName();
               if (cDispName.toLowerCase().startsWith(search.getString().toLowerCase())) {
                  searchColumnList.add(dataset);
               }
            }
         } else if (PageSearchType.CONTAINS == search.getType()) {
            for (Colum dataset : columnList) {
               String cDispName = dataset.getDisplayName();
               if (cDispName.toLowerCase().contains(search.getString().toLowerCase())) {
                  searchColumnList.add(dataset);
               }
            }
         }
      }
      // modify the page object with the new record count which will modify the page count as well
      if (ExecueCoreUtil.isCollectionNotEmpty(searchColumnList) || ExecueCoreUtil.isCollectionNotEmpty(searchList)) {
         pageDetail.setRecordCount(Long.valueOf(searchColumnList.size()));
         getHttpSession().put("COLUMNLIST", searchColumnList);
      } else {
         columnList = (List<Colum>) getHttpSession().get("COLUMNLIST_MAIN");
         pageDetail.setRecordCount(Long.valueOf(columnList.size()));
         getHttpSession().put("COLUMNLIST", columnList);
      }
      // manipulate the list to return the set of concepts belonging to the page requested
      int start = (pageDetail.getRequestedPage().intValue() - 1) * pageDetail.getPageSize().intValue();
      int end = start + pageDetail.getPageSize().intValue();
      if (end > pageDetail.getRecordCount().intValue()) {
         end = (pageDetail.getRecordCount().intValue());
      }
      List<Colum> pageColumns = new ArrayList<Colum>();
      if (ExecueCoreUtil.isCollectionNotEmpty(searchColumnList) || ExecueCoreUtil.isCollectionNotEmpty(searchList)) {
         pageColumns = searchColumnList.subList(start, end);
      } else {
         pageColumns = columnList.subList(start, end);
      }
      return pageColumns;
   }

   public String createAsset () {
      populateDefaultStatus();
      try {
         // dataSources = getSdxServiceHandler().getDisplayableDataSources();
         // populateGrainForAsset();
         Application application = new Application();
         application.setId(getApplicationContext().getAppId());
         application.setName(getApplicationContext().getApplicationName());
         asset.setApplication(application);
         if (asset.getId() == null) {
            // populateGrain
            // TODO: -JVK- TEMPFIX - remove later
            asset.setPriority(1.00);
            // Stop query execution on any parent asset created out of Client Warehouse directly
            if (asset.getBaseAssetId() == null) {
               asset.setQueryExecutionAllowed(CheckType.NO);
            }
            asset.setName(ExecueStringUtil.getNormalizedName(ExecueCoreUtil.generateAlphanumericString(asset
                     .getDisplayName())));
            getSdxServiceHandler().createAsset(asset);
            setAsset(getSdxServiceHandler().getAsset(asset.getId()));
            sdxStatus.setAssetId(asset.getId());
            addActionMessage(getText("execue.asset.create.success"));
         } else {
            // populateGrain
            sdxStatus.setAssetId(asset.getId());
            Asset existingAsset = getSdxServiceHandler().getAsset(asset.getId());
            existingAsset.setName(ExecueStringUtil.getNormalizedName(ExecueCoreUtil.generateAlphanumericString(asset
                     .getDisplayName())));
            existingAsset.setDisplayName(asset.getDisplayName());
            existingAsset.setDescription(asset.getDescription());
            existingAsset.setStatus(asset.getStatus());
            if (existingAsset.getBaseAssetId() == null) {
               existingAsset.setQueryExecutionAllowed(asset.getQueryExecutionAllowed());
            }

            getSdxServiceHandler().updateAsset(existingAsset);
            asset = existingAsset;
            addActionMessage(getText("execue.asset.update.success"));
         }

         getAssetDetailServiceHandler().createUpdateAssetDetail(asset.getId());
         if (asset.getId() != getApplicationContext().getAssetId()) {
            getApplicationContext().setAssetId(asset.getId());
            getApplicationContext().setAssetName(asset.getName());
         }
      } catch (ExeCueException execueException) {
         sdxStatus.setMessage(execueException.getMessage());
         addActionMessage(execueException.getMessage());
         log.error(execueException, execueException);
         return SUCCESS;
      }
      return SUCCESS;
   }

   public String activateAsset () {
      try {
         asset = getSdxServiceHandler().getAsset(getAsset().getId());
         // getSdxServiceHandler().createConstraints(asset);
         getSdxServiceHandler().createDirectJoins(asset);
      } catch (ExeCueException execueException) {
         addActionMessage(execueException.getMessage());
         log.error(execueException, execueException);
         return SUCCESS;
      }
      return SUCCESS;
   }

   @SuppressWarnings ("unchecked")
   public String createTable () {
      log.debug("In create Update Table ");
      populateDefaultStatus();
      try {
         asset = getSdxServiceHandler().getAsset(asset.getId());
         prepareTableInfoForLookupTypeAndVirtual();
         // Check if table exist with virtual table name
         if (isTableAlreadyExists()) {
            addActionMessage(getText("execue.table.already.exists"));
            return INPUT;
         }
         List<TableInfo> tableInfoObjects = new ArrayList<TableInfo>();
         TableInfo tableInfo = new TableInfo();
         String actionMessage = null;
         Integer totalMembers = null;
         if (!LookupType.None.equals(table.getLookupType()) && table.getLookupType() != null) {
            List<Colum> columns = (List<Colum>) getHttpSession().get("COLUMNLIST");
            Colum lookupColumn = ExecueBeanUtil.findCorrespondingColumn(columns, table.getLookupValueColumn());
            totalMembers = getSdxServiceHandler().getMembersCountFromSource(asset, table, lookupColumn);
            if (CheckType.YES.equals(table.getIndicator())) {
               if (totalMembers != null && totalMembers.intValue() != MAX_ALLOWED_MEMBERS_FOR_INDICATOR) {
                  addActionError(getText("execue.table.indicator.error"));
                  return INPUT;
               }
            }
         }
         if (table.getId() == null) {
            // Create
            List<Colum> columns = (List<Colum>) getHttpSession().get("COLUMNLIST");
            if (CheckType.YES.equals(table.getVirtual())) {
               tableInfo = getSdxServiceHandler().prepareVirtualTableInfo(asset, table);
            } else {
               tableInfo.setColumns(columns);
               tableInfo.setTable(table);
               if (table.getLookupType() != null && !LookupType.None.equals(table.getLookupType())) {
                  updateColumnWithTableLookupType(tableInfo);
               }
            }
            updateColumnWithIndicatorInfo(tableInfo);
            tableInfoObjects.add(tableInfo);
            getSdxServiceHandler().createAssetTables(asset, tableInfoObjects);
            if (!LookupType.None.equals(table.getLookupType()) && table.getLookupType() != null) {
               createMembers(tableInfo, totalMembers, false, actionMessage);
            }
            if (actionMessage == null) {
               actionMessage = getText("execue.table.create.success");
            }
         } else {
            // Update
            tableInfo.setTable(table);
            TableInfo assetTable = getSdxServiceHandler().getAssetTable(asset, table.getName());
            tableInfo.setColumns(assetTable.getColumns());
            Tabl swiTable = assetTable.getTable();
            swiTable.setDescription(table.getDescription());
            // if swi table is initially marked as indicator but later on indicator got removed.
            updateTableAndMemberForIndicatorChange(tableInfo, assetTable, swiTable);
            // if swi table is fact table,then he might have changed this to lookup table.
            if (LookupType.None.equals(swiTable.getLookupType())) {
               // if it has changes to lookup table, then modify the column type and get the members
               if (!LookupType.None.equals(table.getLookupType())) {
                  updateColumnWithTableLookupType(tableInfo);
                  createMembers(tableInfo, totalMembers, true, actionMessage);
               }
            } else {
               // if swi table is lookup and user might have changed it to fact table.
               resetTableAndMemberForLookupTypeChange(tableInfo, assetTable, swiTable);
               tableInfo.setTable(swiTable);
               if (CheckType.YES.equals(table.getIndicator())) {
                  updateIndicatorInfoForMembers(tableInfo, assetTable);
               }
            }
            updateColumnWithIndicatorInfo(tableInfo);
            tableInfoObjects.add(tableInfo);
            getSdxServiceHandler().updateAssetTables(asset, tableInfoObjects);
            if (actionMessage == null) {
               actionMessage = getText("execue.table.update.success");
            }
         }
         addActionMessage(actionMessage);
         // This is just to identify that table has been created and now source name will be ASSET
         setSourceName(ASSET);
         populateTableDefaults(false);
      } catch (ExeCueException execueException) {
         sdxStatus.setMessage(execueException.getMessage());
         log.error(execueException, execueException);
         return SUCCESS;
      }
      return SUCCESS;
   }

   private void updateColumnWithIndicatorInfo (TableInfo tableInfo) {
      Tabl tabl = tableInfo.getTable();
      String lookupValueColumn = tabl.getLookupValueColumn();
      if (ExecueCoreUtil.isNotEmpty(lookupValueColumn)) {
         for (Colum colum : tableInfo.getColumns()) {
            if (lookupValueColumn.equalsIgnoreCase(colum.getName())) {
               colum.setIndicator(tabl.getIndicator());
               break;
            }
         }
      }
   }

   public String updateColumns () {
      try {
         log.debug("Updating Columns ");
         asset = getSdxServiceHandler().getAsset(asset.getId());
         TableInfo tblInfo = new TableInfo();
         log.debug("table.getName() " + table.getName());
         tblInfo.setTable(getSdxServiceHandler().getAssetTable(asset, table.getName()).getTable());
         tblInfo.setColumns(getSubListForColumnInfo());
         // tblInfo.setColumns(getCols());
         List<TableInfo> tblInfoList = new ArrayList<TableInfo>();
         tblInfoList.add(tblInfo);
         getSdxServiceHandler().updateAssetTables(asset, tblInfoList);
         log.debug("Update Columns Called");
         populateTableDefaults(false);
         addActionMessage(getText("execue.columns.update.success"));
         log.debug("column updated successful");
         return SUCCESS;
      } catch (ExeCueException execueException) {
         sdxStatus.setMessage(execueException.getMessage());
         log.error(execueException, execueException);
         return SUCCESS;
      }
   }

   public String updateMembers () {
      try {
         asset = getSdxServiceHandler().getAsset(asset.getId());
         TableInfo tblInfo = new TableInfo();
         TableInfo assetTable = getSdxServiceHandler().getAssetTable(asset, table.getName());
         tblInfo.setTable(assetTable.getTable());
         tblInfo.setColumns(assetTable.getColumns());
         tblInfo.setMembers(getSubListForMemberInfo());
         List<TableInfo> tblInfoList = new ArrayList<TableInfo>();
         tblInfoList.add(tblInfo);
         getSdxServiceHandler().updateAssetTables(asset, tblInfoList);
         log.debug("updateMembers Called");
         populateTableDefaults(false);
         log.debug("memeber update successful");
         addActionMessage(getText("execue.members.update.success"));
         return SUCCESS;
      } catch (ExeCueException execueException) {
         sdxStatus.setMessage(execueException.getMessage());
         log.error(execueException, execueException);
         return SUCCESS;
      }
   }

   public String input () {
      try {
         dataSources = getSdxServiceHandler().getDisplayableDataSources();
         if (asset != null && asset.getId() != null) {
            asset = getSdxServiceHandler().getAsset(asset.getId());
            if (table != null && table.getName() != null)
               populateTableDefaults(false);
         }
      } catch (ExeCueException execueException) {
         sdxStatus.setMessage(execueException.getMessage());
         log.error(execueException, execueException);
         return INPUT;
      }
      return INPUT;

   }

   public String deleteSelectedTables () {
      try {
         asset = getSdxServiceHandler().getAsset(asset.getId());
         adjustTableNamesForDeletion(tableNamesForDeletion);
         List<String> batchMaintenanceTableNames = getSdxServiceHandler().deleteTables(asset, tableNamesForDeletion);
         if (ExecueCoreUtil.isCollectionNotEmpty(batchMaintenanceTableNames)) {
            StringBuilder message = new StringBuilder(getText("execue.tables.under.batchprocess"));
            message.append(" [");
            for (int counter = 0; counter < batchMaintenanceTableNames.size(); counter++) {
               if (counter == 0)
                  message.append(batchMaintenanceTableNames.get(counter));
               else
                  message.append("," + batchMaintenanceTableNames.get(counter));
            }
            message.append("]");
            addActionMessage(message.toString());
         } else
            addActionMessage(getText("execue.table.deletion.success"));

         assetTables = getSdxServiceHandler().getAllAssetTables(asset);
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         addActionMessage(getText("execue.table.deletion.failure"));
      }
      return SUCCESS;
   }

   public String updateAssetStatus () {
      try {
         if (asset != null && asset.getId() != null) {
            if (!getSdxServiceHandler().isAssetExistsUnderBatchProcess(asset.getId())) {
               asset = getSdxServiceHandler().getAsset(asset.getId());
               if (StatusEnum.ACTIVE.equals(asset.getStatus())) {
                  asset.setStatus(StatusEnum.INACTIVE);
                  getSdxServiceHandler().updateAsset(asset);
               } else if (StatusEnum.INACTIVE.equals(asset.getStatus())) {
                  asset.setStatus(StatusEnum.ACTIVE);
                  getSdxServiceHandler().updateAsset(asset);
               }
               // reinitialize the list with updated assets.
               list();
            } else {
               addActionMessage(getText("execue.asset.job.update.failure"));
            }
         } else {
            addActionMessage(getText("execue.asset.update.failure"));
         }
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         addActionMessage(getText("execue.asset.update.failure"));
         return ERROR;
      }
      return SUCCESS;
   }

   private void getDataSourcesForAssetDefinition () throws ExeCueException {
      dataSources = getSdxServiceHandler().getDisplayableDataSources();
   }

   private void paginationForAssets () {
      if (getRequestedPage() == null) {
         setRequestedPage(DEFAULT_REQUESTED_PAGE);
      }

      getHttpSession().put("ASSETSFORPAGINATION", assets);

      int tempSize = 0;
      if (assets.size() <= PAGESIZEFORASSETDETAILS)
         tempSize = assets.size();
      else
         tempSize = PAGESIZEFORASSETDETAILS;
      log.info("displaying initial sublist");
      assets = assets.subList(0, tempSize);
   }

   private void paginationForSourceTables () {
      if (getRequestedPage() == null) {
         setRequestedPage(DEFAULT_REQUESTED_PAGE);
      }
      getHttpSession().put("SOURCETABLESFORPAGINATION", sourceTables);

      int tempSize = 0;
      if (sourceTables.size() <= PAGESIZEFORSOURCETABLES)
         tempSize = sourceTables.size();
      else
         tempSize = PAGESIZEFORSOURCETABLES;
      log.info("displaying initial sublist");
      sourceTables = sourceTables.subList(0, tempSize);
   }

   private void adjustTableNamesForDeletion (List<String> tableNamesForDeletion) {
      List<String> emptyTableNames = new ArrayList<String>();
      for (String tableName : tableNamesForDeletion) {
         if (tableName == null) {
            emptyTableNames.add(tableName);
         }
      }
      tableNamesForDeletion.removeAll(emptyTableNames);
   }

   @SuppressWarnings ("unchecked")
   private Long populateTableDefaults (boolean isVirtualTable) throws ExeCueException {

      TableInfo tableInfo = getSdxServiceHandler().getTableInfo(asset, table.getName(), table.getDescription(),
               table.getOwner(), isVirtualTable);
      table = tableInfo.getTable();
      cols = tableInfo.getColumns();
      members = tableInfo.getMembers();
      if (paginationType != null && paginationType.equalsIgnoreCase("column")) {
         paginationForColumn();
      } else if (paginationType != null && paginationType.equalsIgnoreCase("member")) {
         paginationForMember();
      }
      populateColumnsForSelection();
      getParentIDs(table.getName());
      return table.getId();
   }

   // private methods
   private void populateDefaultStatus () {
      sdxStatus = new SDXStatus();
      sdxStatus.setStatus("success");
      sdxStatus.setAssetId(asset.getId());
   }

   private void populateColumnsForSelection () {
      columnsForSelection = new ArrayList<Colum>();
      Colum col = new Colum();
      col.setName("None");
      columnsForSelection.add(col);
      columnsForSelection.addAll(getCols());
   }

   private void updateTableAndMemberForIndicatorChange (TableInfo tableInfo, TableInfo assetTable, Tabl swiTable) {
      if (CheckType.YES.equals(swiTable.getIndicator())) {
         if (!CheckType.YES.equals(table.getIndicator())) {
            swiTable.setIndicator(CheckType.NO);
            resetIndicatorInfoForMembers(tableInfo, assetTable);
         }
      }
   }

   private void resetIndicatorInfoForMembers (TableInfo tableInfo, TableInfo assetTable) {
      List<Membr> members = assetTable.getMembers();
      members.get(0).setIndicatorBehavior(null);
      members.get(1).setIndicatorBehavior(null);
      tableInfo.setMembers(members);
   }

   private void resetTableAndMemberForLookupTypeChange (TableInfo tableInfo, TableInfo assetTable, Tabl swiTable)
            throws ExeCueException {
      if (!LookupType.None.equals(swiTable.getLookupType())) {
         if (LookupType.None.equals(table.getLookupType())) {
            resetColumnWithTableLookupType(swiTable, tableInfo);
            tableInfo.setMembers(null);
            getSdxServiceHandler().deleteMembers(assetTable.getMembers());
            resetTableLookupTypeInfo(swiTable);
         }
      }
   }

   private void resetTableLookupTypeInfo (Tabl swiTable) {
      swiTable.setLookupType(LookupType.None);
      swiTable.setLookupValueColumn(null);
      swiTable.setLookupDescColumn(null);
      swiTable.setLowerLimitColumn(null);
      swiTable.setUpperLimitColumn(null);
      // this is done to avoid the lazy initlization exception
      swiTable.setAssetEntityDefinitions(null);
   }

   private void resetColumnWithTableLookupType (Tabl swiTable, TableInfo tableInfo) {
      List<Colum> columns = tableInfo.getColumns();
      for (Colum colum : columns) {
         if (colum.getName().equals(swiTable.getLookupValueColumn())) {
            colum.setKdxDataType(null);
            break;
         }

      }

   }

   private void updateIndicatorInfoForMembers (TableInfo tableInfo, TableInfo assetTable) {
      tableInfo.getTable().setIndicator(table.getIndicator());
      List<Membr> members = assetTable.getMembers();
      members.get(0).setIndicatorBehavior(IndicatorBehaviorType.POSITIVE);
      members.get(1).setIndicatorBehavior(IndicatorBehaviorType.NEGATIVE);
      tableInfo.setMembers(members);
   }

   private boolean isTableAlreadyExists () throws ExeCueException {
      boolean isTableAlreadyExists = false;
      List<UITable> swiTables = getSdxServiceHandler().getAllTables(asset);
      if (ExecueCoreUtil.isCollectionNotEmpty(swiTables) && table.getId() == null) {
         for (UITable switabl : swiTables) {
            if (switabl.getName().equalsIgnoreCase(table.getName())) {
               isTableAlreadyExists = true;
               break;
            }
         }
      }
      return isTableAlreadyExists;
   }

   private void prepareTableInfoForLookupTypeAndVirtual () {
      if (table.getId() == null && CheckType.YES.equals(table.getVirtual())) {
         table.setName(table.getDisplayName());
      }
      if (LookupType.None.equals(table.getLookupType())) {
         table.setLookupValueColumn(null);
         table.setLookupDescColumn(null);
         table.setLowerLimitColumn(null);
         table.setUpperLimitColumn(null);
         table.setParentTableId(null);
      } else if (LookupType.SIMPLE_LOOKUP.equals(table.getLookupType())) {
         table.setLowerLimitColumn(null);
         table.setUpperLimitColumn(null);
         table.setParentTableId(null);
      } else if (LookupType.SIMPLEHIERARCHICAL_LOOKUP.equals(table.getLookupType())) {
         table.setLowerLimitColumn(null);
         table.setUpperLimitColumn(null);
      } else if (LookupType.RANGE_LOOKUP.equals(table.getLookupType())) {
         table.setParentTableId(null);
      }
   }

   private void createMembers (TableInfo tableInfo, Integer totalMembers, boolean update, String actionMessage)
            throws ExeCueException {
      Tabl tabl = tableInfo.getTable();
      if (totalMembers > MAX_ALLOWED_MEMBERS_MAIN_FLOW) {
         getSdxServiceHandler().createMembersByJob(asset, tabl, totalMembers);
         // if method has been called for updation of a table
         if (update) {
            actionMessage = getText("execue.table.job.update.success");
         } else {
            actionMessage = getText("execue.table.job.create.success");
         }
      } else {
         List<Membr> members = getSdxServiceHandler().getMembersFromSource(asset, tabl);
         if (CheckType.YES.equals(tabl.getIndicator()) && totalMembers.equals(MAX_ALLOWED_MEMBERS_FOR_INDICATOR)) {
            members.get(0).setIndicatorBehavior(IndicatorBehaviorType.POSITIVE);
            members.get(1).setIndicatorBehavior(IndicatorBehaviorType.NEGATIVE);
         }
         getSdxServiceHandler().createMembers(asset, tabl, tableInfo.getColumns(), members);
      }
   }

   private void updateColumnWithTableLookupType (TableInfo tableInfo) {
      Tabl tabl = tableInfo.getTable();
      String lookupValueColumn = tabl.getLookupValueColumn();
      if (ExecueCoreUtil.isNotEmpty(lookupValueColumn)) {
         for (Colum colum : tableInfo.getColumns()) {
            if (lookupValueColumn.equalsIgnoreCase(colum.getName())) {
               colum.setKdxDataType(ExecueBeanUtil.getColumnType(tabl.getLookupType()));
               break;
            }
         }
      }
   }

   private void getParentIDs (String tableName) throws ExeCueException {

      if (asset != null && asset.getId() != null) {
         asset = getSdxServiceHandler().getAsset(asset.getId());
         List<UITable> simpleHierarchicalLookupTables = getSdxServiceHandler().getSimpleHierarchicalLookupTables(
                  asset.getId());
         if (ExecueCoreUtil.isCollectionNotEmpty(simpleHierarchicalLookupTables)) {
            UITable selfTable = null;
            for (UITable shlTable : simpleHierarchicalLookupTables) {
               if (tableName.equalsIgnoreCase(shlTable.getName())) {
                  selfTable = shlTable;
                  break;
               }
            }
            simpleHierarchicalLookupTables.remove(selfTable);
         }
         // parentTables=simpleHierarchicalLookupTables;
         UITable uiTabl = new UITable();
         uiTabl.setName("None");
         parentTables = new ArrayList<UITable>();
         parentTables.add(uiTabl);
         parentTables.addAll(simpleHierarchicalLookupTables);
      }
   }

   private void paginationForColumn () {
      if (updatingColumnInfo) {
         getHttpSession().put("COLUMNLIST", cols);
         getHttpSession().put("COLUMNLIST_MAIN", cols);
      } else {
         if (getHttpSession().containsKey("COLUMNLIST")) {
            log.info("Clearing the Session");
            getHttpSession().remove("COLUMNLIST");
            setRequestedPage("1");
         }
         if (getHttpSession().containsKey("COLUMNLIST_MAIN")) {
            getHttpSession().remove("COLUMNLIST_MAIN");
         }
         if (log.isDebugEnabled()) {
            log.debug("Loading for First Time, so setting URL, pageNo=1 ");
         }
         if (getRequestedPage() == null) {
            setRequestedPage(DEFAULT_REQUESTED_PAGE);
         }
         getHttpSession().put("COLUMNLIST", cols);
         getHttpSession().put("COLUMNLIST_MAIN", cols);
         int tempSize = 0;
         if (cols.size() <= COLUMN_MEMBER_PAGESIZE)
            tempSize = cols.size();
         else
            tempSize = COLUMN_MEMBER_PAGESIZE;
         if (log.isDebugEnabled()) {
            log.debug("displaying initial sublist");
         }
         subListForColumnInfo = cols.subList(0, tempSize);
      }

   }

   private void paginationForMember () {

      if (ExecueCoreUtil.isCollectionNotEmpty(members)) {

         if (updatingMemberInfo) {
            getHttpSession().put("MEMBERLIST", members);
            getHttpSession().put("MEMBERLIST_MAIN", members);

         } else {
            if (getHttpSession().containsKey("MEMBERLIST")) {
               log.info("Clearing the Session");
               getHttpSession().remove("MEMBERLIST");
               setRequestedPage("1");
            }
            if (getHttpSession().containsKey("MEMBERLIST_MAIN")) {
               getHttpSession().remove("MEMBERLIST_MAIN");
            }
            if (log.isDebugEnabled()) {
               log.debug("Loading for First Time, so setting URL, pageNo=1 ");
            }
            if (getRequestedPage() == null) {
               setRequestedPage(DEFAULT_REQUESTED_PAGE);
            }
            getHttpSession().put("MEMBERLIST", members);
            getHttpSession().put("MEMBERLIST_MAIN", members);

            int tempSize = 0;
            if (members.size() <= COLUMN_MEMBER_PAGESIZE)
               tempSize = members.size();
            else
               tempSize = COLUMN_MEMBER_PAGESIZE;
            if (log.isDebugEnabled()) {
               log.debug("displaying initial sublist");
            }
            subListForMemberInfo = members.subList(0, tempSize);
         }
      }
   }

   // mutators
   public List<UITable> getSourceTables () {
      return sourceTables;
   }

   public void setSourceTables (List<UITable> sourceTables) {
      this.sourceTables = sourceTables;
   }

   public List<UITable> getAssetTables () {
      return assetTables;
   }

   public void setAssetTables (List<UITable> assetTables) {
      this.assetTables = assetTables;
   }

   public String getAssetName () {
      return assetName;
   }

   public void setAssetName (String assetName) {
      this.assetName = assetName;
   }

   public String getTableNameFromSource () {
      return tableNameFromSource;
   }

   public void setTableNameFromSource (String tableNameFromSource) {
      this.tableNameFromSource = tableNameFromSource;
   }

   public String getTableNameFromAsset () {
      return tableNameFromAsset;
   }

   public void setTableNameFromAsset (String tableNameFromAsset) {
      this.tableNameFromAsset = tableNameFromAsset;
   }

   public List<DataSource> getDataSources () {
      return dataSources;
   }

   public void setDataSources (List<DataSource> dataSources) {
      this.dataSources = dataSources;
   }

   public List<TableInfo> getTableInfoList () {
      return tableInfoList;
   }

   public void setTableInfoList (List<TableInfo> tableInfoList) {
      this.tableInfoList = tableInfoList;
   }

   public Tabl getTable () {
      return table;
   }

   public void setTable (Tabl table) {
      this.table = table;
   }

   public Asset getAsset () {
      return asset;
   }

   public void setAsset (Asset asset) {
      this.asset = asset;
   }

   public List<Colum> getCols () {
      return cols;
   }

   public void setCols (List<Colum> cols) {
      this.cols = cols;
   }

   public List<Membr> getMembers () {
      return members;
   }

   public void setMembers (List<Membr> members) {
      this.members = members;
   }

   public String getDataSourceName () {
      return dataSourceName;
   }

   public void setDataSourceName (String dataSourceName) {
      this.dataSourceName = dataSourceName;
   }

   public List<Asset> getAssets () {
      return assets;
   }

   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

   public List<Tabl> getTables () {
      return tables;
   }

   public void setTables (List<Tabl> tables) {
      this.tables = tables;
   }

   public SDXStatus getSdxStatus () {
      return sdxStatus;
   }

   public void setSdxStatus (SDXStatus sdxStatus) {
      this.sdxStatus = sdxStatus;
   }

   public List<Colum> getColumnsForSelection () {
      return columnsForSelection;
   }

   public void setColumnsForSelection (List<Colum> columnsForSelection) {
      this.columnsForSelection = columnsForSelection;
   }

   public List<String> getTableNamesForDeletion () {
      return tableNamesForDeletion;
   }

   public void setTableNamesForDeletion (List<String> tableNamesForDeletion) {
      this.tableNamesForDeletion = tableNamesForDeletion;
   }

   public List<Long> getGrainIds () {
      return grainIds;
   }

   public void setGrainIds (List<Long> grainIds) {
      this.grainIds = grainIds;
   }

   public List<UITable> getParentTables () {
      return parentTables;
   }

   public void setParentTables (List<UITable> parentTables) {
      this.parentTables = parentTables;
   }

   public List<Colum> getSubListForColumnInfo () {
      return subListForColumnInfo;
   }

   public void setSubListForColumnInfo (List<Colum> subListForColumnInfo) {
      this.subListForColumnInfo = subListForColumnInfo;
   }

   public List<Membr> getSubListForMemberInfo () {
      return subListForMemberInfo;
   }

   public void setSubListForMemberInfo (List<Membr> subListForMemberInfo) {
      this.subListForMemberInfo = subListForMemberInfo;
   }

   public boolean isUpdatingColumnInfo () {
      return updatingColumnInfo;
   }

   public void setUpdatingColumnInfo (boolean updatingColumnInfo) {
      this.updatingColumnInfo = updatingColumnInfo;
   }

   public boolean isUpdatingMemberInfo () {
      return updatingMemberInfo;
   }

   public void setUpdatingMemberInfo (boolean updatingMemberInfo) {
      this.updatingMemberInfo = updatingMemberInfo;
   }

   public String getPaginationType () {
      return paginationType;
   }

   public void setPaginationType (String paginationType) {
      this.paginationType = paginationType;
   }

   public String getBatchProcessTable () {
      return batchProcessTable;
   }

   public void setBatchProcessTable (String batchProcessTable) {
      this.batchProcessTable = batchProcessTable;
   }

   public String getSourceName () {
      return sourceName;
   }

   public void setSourceName (String sourceName) {
      this.sourceName = sourceName;
   }

   public UIJobRequestResult getJobRequestResult () {
      return jobRequestResult;
   }

   public void setJobRequestResult (UIJobRequestResult jobRequestResult) {
      this.jobRequestResult = jobRequestResult;
   }

   public UIJobRequestStatus getJobRequest () {
      return jobRequest;
   }

   public void setJobRequest (UIJobRequestStatus jobRequest) {
      this.jobRequest = jobRequest;
   }

   /**
    * @return the underProcessAssetMap
    */
   public Map<String, List<Asset>> getUnderProcessAssetMap () {
      return underProcessAssetMap;
   }

   /**
    * @param underProcessAssetMap the underProcessAssetMap to set
    */
   public void setUnderProcessAssetMap (Map<String, List<Asset>> underProcessAssetMap) {
      this.underProcessAssetMap = underProcessAssetMap;
   }

   /**
    * @return the assetUnderProcess
    */
   public String getAssetUnderProcess () {
      return assetUnderProcess;
   }

   /**
    * @param assetUnderProcess the assetUnderProcess to set
    */
   public void setAssetUnderProcess (String assetUnderProcess) {
      this.assetUnderProcess = assetUnderProcess;
   }

   @Override
   public String processPage () throws ExeCueException {
      // TODO Auto-generated method stub
      return null;
   }

}
