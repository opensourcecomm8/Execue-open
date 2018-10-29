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


package com.execue.platform.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.GranularityType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.IVirtualTableManagementService;
import com.execue.platform.exception.SourceMetaDataException;
import com.execue.platform.swi.ISourceMetaDataService;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

public class VirtualTableManagementServiceImpl implements IVirtualTableManagementService {

   private ISDXRetrievalService     sdxRetrievalService;
   private ISDXManagementService    sdxManagementService;
   private ISourceMetaDataService   sourceMetaDataService;
   private ISWIConfigurationService swiConfigurationService;

   public TableInfo prepareVirtualTableInfo (Asset asset, Tabl originalTable, Tabl virtualTable) throws SWIException {
      TableInfo tableInfo = new TableInfo();
      Tabl clonedOriginalTable = ExecueBeanCloneUtil.cloneTable(originalTable);

      // TODO : -RG- Don't we need to set AE references to null on cloned table ??

      clonedOriginalTable.setId(null);
      clonedOriginalTable.setVirtual(CheckType.YES);
      clonedOriginalTable.setEligibleDefaultMetric(CheckType.NO);
      clonedOriginalTable.setName(virtualTable.getName());
      clonedOriginalTable.setDisplayName(virtualTable.getDisplayName());
      clonedOriginalTable.setDescription(virtualTable.getDescription());
      clonedOriginalTable.setLookupType(virtualTable.getLookupType());
      clonedOriginalTable.setActualName(originalTable.getName());
      clonedOriginalTable.setLookupValueColumn(virtualTable.getLookupValueColumn());
      clonedOriginalTable.setLookupDescColumn(virtualTable.getLookupDescColumn());
      clonedOriginalTable.setLowerLimitColumn(virtualTable.getLowerLimitColumn());
      clonedOriginalTable.setUpperLimitColumn(virtualTable.getUpperLimitColumn());
      clonedOriginalTable.setIndicator(virtualTable.getIndicator());
      tableInfo.setTable(clonedOriginalTable);
      List<Colum> originalTableColumns = getSdxRetrievalService().getColumnsOfTable(originalTable);
      List<Colum> virtualTableColumns = new ArrayList<Colum>();

      if (ExecueCoreUtil.isNotEmpty(clonedOriginalTable.getLookupValueColumn())) {
         Colum lookupValueColumn = getColumnByNameFromList(originalTableColumns, clonedOriginalTable
                  .getLookupValueColumn(), true);
         lookupValueColumn.setKdxDataType(ExecueBeanUtil.getColumnType(clonedOriginalTable.getLookupType()));
         virtualTableColumns.add(lookupValueColumn);
      }
      String tempColumnName = null;
      if (ExecueCoreUtil.isNotEmpty(clonedOriginalTable.getLookupDescColumn())
               && ExecueCoreUtil.isNotEmpty(clonedOriginalTable.getLookupValueColumn())
               && clonedOriginalTable.getLookupDescColumn()
                        .equalsIgnoreCase(clonedOriginalTable.getLookupValueColumn())) {
         Colum lookupDescColumn = getColumnByNameFromList(originalTableColumns, clonedOriginalTable
                  .getLookupDescColumn(), false);
         lookupDescColumn.setName(lookupDescColumn.getName()
                  + getSwiConfigurationService().getVirtualLookupDescriptionColumnSuffix());
         tempColumnName = lookupDescColumn.getName();
         lookupDescColumn.setDisplayName(tempColumnName.replaceAll("_", " "));
         clonedOriginalTable.setLookupDescColumn(lookupDescColumn.getName());
         clonedOriginalTable.setVirtualTableDescColumnExistsOnSource(CheckType.NO);
         virtualTableColumns.add(lookupDescColumn);
      } else if (ExecueCoreUtil.isNotEmpty(clonedOriginalTable.getLookupDescColumn())) {
         virtualTableColumns.add(getColumnByNameFromList(originalTableColumns, clonedOriginalTable
                  .getLookupDescColumn(), false));
      }
      if (ExecueCoreUtil.isNotEmpty(clonedOriginalTable.getLowerLimitColumn())) {
         virtualTableColumns.add(getColumnByNameFromList(originalTableColumns, clonedOriginalTable
                  .getLowerLimitColumn(), false));
      }
      if (ExecueCoreUtil.isNotEmpty(clonedOriginalTable.getUpperLimitColumn())) {
         virtualTableColumns.add(getColumnByNameFromList(originalTableColumns, clonedOriginalTable
                  .getUpperLimitColumn(), false));
      }
      tableInfo.setColumns(virtualTableColumns);
      return tableInfo;
   }

   private Colum getColumnByNameFromList (List<Colum> originalColumns, String columnName, boolean isValueColumn) {
      Colum matchedColum = null;
      for (Colum colum : originalColumns) {
         if (colum.getName().equalsIgnoreCase(columnName)) {
            matchedColum = colum;
            break;
         }
      }
      matchedColum = ExecueBeanCloneUtil.cloneColum(matchedColum);
      matchedColum.setId(null);
      if (!isValueColumn) {
         matchedColum.setKdxDataType(ColumnType.NULL);
         matchedColum.setConversionType(null);
         matchedColum.setUnit(null);
         matchedColum.setDataFormat(null);
         matchedColum.setDefaultMetric(CheckType.NO);
         matchedColum.setGranularity(GranularityType.NA);
      }
      return matchedColum;
   }

   public ISourceMetaDataService getSourceMetaDataService () {
      return sourceMetaDataService;
   }

   public void setSourceMetaDataService (ISourceMetaDataService sourceMetaDataService) {
      this.sourceMetaDataService = sourceMetaDataService;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public void createVirtualTableInfo (Asset asset, TableInfo tableInfo) throws SWIException {
      try {
         List<Membr> members = getSourceMetaDataService().getMembersFromSource(asset, tableInfo.getTable(), null);
         tableInfo.setMembers(members);
         List<TableInfo> tablesInfo = new ArrayList<TableInfo>();
         tablesInfo.add(tableInfo);
         getSdxManagementService().createAssetTables(asset, tablesInfo);
      } catch (SourceMetaDataException sourceMetaDataException) {

      }
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public ISDXManagementService getSdxManagementService () {
      return sdxManagementService;
   }

   public void setSdxManagementService (ISDXManagementService sdxManagementService) {
      this.sdxManagementService = sdxManagementService;
   }

}
