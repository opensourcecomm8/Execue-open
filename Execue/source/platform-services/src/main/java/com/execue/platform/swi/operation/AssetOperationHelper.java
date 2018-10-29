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


package com.execue.platform.swi.operation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetOperationInfo;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.HistoryAssetOperationInfo;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.swi.AssetAnalysisReport;
import com.execue.core.common.bean.swi.AssetAnalysisReportInfo;
import com.execue.core.common.bean.swi.AssetAnalysisTableInfo;
import com.execue.core.common.bean.swi.AssetOperationColumn;
import com.execue.core.common.bean.swi.AssetOperationMember;
import com.execue.core.common.bean.swi.AssetOperationTable;
import com.execue.core.common.bean.swi.AssetSyncInfo;
import com.execue.core.common.bean.swi.ColumnSyncInfo;
import com.execue.core.common.bean.swi.MemberSyncInfo;
import com.execue.core.common.bean.swi.OperationAsset;
import com.execue.core.common.type.AssetOperationType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.OperationEnum;
import com.execue.core.common.util.ExecueBeanUtil;
import com.thoughtworks.xstream.XStream;

public class AssetOperationHelper {

   public static OperationAsset transformAsset (Asset asset) {
      OperationAsset operationAsset = new OperationAsset();
      operationAsset.setId(asset.getId());
      operationAsset.setName(asset.getName());
      operationAsset.setDisplayName(asset.getDisplayName());
      return operationAsset;
   }

   public static AssetOperationTable transformTable (Tabl tabl) {
      AssetOperationTable assetOperationTable = new AssetOperationTable();
      assetOperationTable.setId(tabl.getId());
      assetOperationTable.setName(tabl.getName());
      assetOperationTable.setDisplayName(tabl.getDisplayName());
      assetOperationTable.setDescription(tabl.getDescription());
      assetOperationTable.setOwner(tabl.getOwner());
      assetOperationTable.setVirtual(tabl.getVirtual());
      return assetOperationTable;
   }

   public static Tabl transformAssetOperationTable (AssetOperationTable assetOperationTable) {
      Tabl tabl = new Tabl();
      tabl.setId(assetOperationTable.getId());
      tabl.setName(assetOperationTable.getName());
      tabl.setDisplayName(assetOperationTable.getDisplayName());
      tabl.setDescription(assetOperationTable.getDescription());
      tabl.setOwner(assetOperationTable.getOwner());
      tabl.setVirtual(assetOperationTable.getVirtual());
      return tabl;
   }

   public static AssetOperationColumn transformColumn (Colum column) {
      AssetOperationColumn assetOperationColumn = new AssetOperationColumn();
      assetOperationColumn.setId(column.getId());
      assetOperationColumn.setName(column.getName());
      assetOperationColumn.setDescription(column.getDescription());
      assetOperationColumn.setDataType(column.getDataType());
      assetOperationColumn.setDefaultValue(column.getDefaultValue());
      assetOperationColumn.setIsConstraintColum(column.getIsConstraintColum());
      assetOperationColumn.setKdxDataType(column.getKdxDataType());
      assetOperationColumn.setNullable(column.isNullable());
      assetOperationColumn.setPrecision(column.getPrecision());
      assetOperationColumn.setRequired(column.getRequired());
      assetOperationColumn.setScale(column.getScale());
      return assetOperationColumn;
   }

   public static Colum transformAssetOperationColumn (AssetOperationColumn operationColumn) {
      Colum colum = new Colum();
      colum.setId(operationColumn.getId());
      colum.setName(operationColumn.getName());
      colum.setDescription(operationColumn.getDescription());
      colum.setDataType(operationColumn.getDataType());
      colum.setDefaultValue(operationColumn.getDefaultValue());
      colum.setIsConstraintColum(operationColumn.getIsConstraintColum());
      colum.setKdxDataType(operationColumn.getKdxDataType());
      colum.setNullable(operationColumn.isNullable());
      colum.setPrecision(operationColumn.getPrecision());
      colum.setRequired(operationColumn.getRequired());
      colum.setScale(operationColumn.getScale());
      return colum;
   }

   public static AssetOperationMember transformMember (Membr member) {
      AssetOperationMember assetOperationMember = new AssetOperationMember();
      assetOperationMember.setId(member.getId());
      assetOperationMember.setLookupValue(member.getLookupValue());
      assetOperationMember.setLookupDescription(member.getLookupDescription());
      assetOperationMember.setKdxLookupDescription(member.getKdxLookupDescription());
      assetOperationMember.setLongDescription(member.getLongDescription());
      assetOperationMember.setLowerLimit(member.getLowerLimit());
      assetOperationMember.setUpperLimit(member.getUpperLimit());
      return assetOperationMember;
   }

   public static Membr transformAssetOperationMember (AssetOperationMember operationMember) {
      Membr member = new Membr();
      member.setId(operationMember.getId());
      member.setLookupValue(operationMember.getLookupValue());
      member.setLookupDescription(operationMember.getLookupDescription());
      member.setKdxLookupDescription(operationMember.getKdxLookupDescription());
      member.setLongDescription(operationMember.getLongDescription());
      member.setLowerLimit(operationMember.getLowerLimit());
      member.setUpperLimit(operationMember.getUpperLimit());
      return member;
   }

   public static List<AssetOperationTable> transformTables (List<Tabl> tables) {
      List<AssetOperationTable> assetOperationTables = new ArrayList<AssetOperationTable>();
      for (Tabl table : tables) {
         assetOperationTables.add(transformTable(table));
      }
      return assetOperationTables;
   }

   public static List<AssetOperationColumn> transformColumns (List<Colum> columns) {
      List<AssetOperationColumn> assetOperationColumns = new ArrayList<AssetOperationColumn>();
      for (Colum column : columns) {
         assetOperationColumns.add(transformColumn(column));
      }
      return assetOperationColumns;
   }

   public static List<AssetOperationMember> transformMembers (List<Membr> members) {
      List<AssetOperationMember> assetOperationMembers = new ArrayList<AssetOperationMember>();
      for (Membr member : members) {
         assetOperationMembers.add(transformMember(member));
      }
      return assetOperationMembers;
   }

   public static List<Tabl> transformAssetOperationTables (List<AssetOperationTable> operationTables) {
      List<Tabl> tables = new ArrayList<Tabl>();
      for (AssetOperationTable assetOperationTable : operationTables) {
         tables.add(transformAssetOperationTable(assetOperationTable));
      }
      return tables;
   }

   public static List<Colum> transformAssetOperationColumns (List<AssetOperationColumn> operationColumns) {
      List<Colum> columns = new ArrayList<Colum>();
      for (AssetOperationColumn operationColumn : operationColumns) {
         columns.add(transformAssetOperationColumn(operationColumn));
      }
      return columns;
   }

   public static List<Membr> tranformAssetOperationMembers (List<AssetOperationMember> operationMembers) {
      List<Membr> members = new ArrayList<Membr>();
      for (AssetOperationMember operationMember : operationMembers) {
         members.add(transformAssetOperationMember(operationMember));
      }
      return members;
   }

   public static AssetOperationInfo populateAssetOperationInfo (Long assetId, Date startDate, Date completionDate,
            String assetOperationData, boolean changeFound, CheckType status, OperationEnum operation,
            AssetOperationType assetOperationType) {
      AssetOperationInfo assetOperationInfo = new AssetOperationInfo();
      assetOperationInfo.setAssetId(assetId);
      assetOperationInfo.setAssetOperationData(assetOperationData);
      assetOperationInfo.setStartDate(startDate);
      assetOperationInfo.setCompletionDate(completionDate);
      assetOperationInfo.setChangeFound(ExecueBeanUtil.getCorrespondingCheckTypeValue(changeFound));
      assetOperationInfo.setStatus(status);
      assetOperationInfo.setOperation(operation);
      assetOperationInfo.setAssetOperationType(assetOperationType);
      return assetOperationInfo;
   }

   public static HistoryAssetOperationInfo populateHistoryAssetOperationInfo (AssetOperationInfo assetOperationInfo) {
      HistoryAssetOperationInfo historyAssetOperationInfo = new HistoryAssetOperationInfo();
      historyAssetOperationInfo.setAssetId(assetOperationInfo.getAssetId());
      historyAssetOperationInfo.setAssetOperationData(assetOperationInfo.getAssetOperationData());
      historyAssetOperationInfo.setChangeFound(assetOperationInfo.getChangeFound());
      historyAssetOperationInfo.setCompletionDate(assetOperationInfo.getCompletionDate());
      historyAssetOperationInfo.setOperation(assetOperationInfo.getOperation());
      historyAssetOperationInfo.setStartDate(assetOperationInfo.getStartDate());
      historyAssetOperationInfo.setStatus(assetOperationInfo.getStatus());
      historyAssetOperationInfo.setAssetOperationType(assetOperationInfo.getAssetOperationType());
      return historyAssetOperationInfo;
   }

   public static String convertAssetSyncInfoToXML (AssetSyncInfo assetSyncInfo) {
      XStream xstream = new XStream();
      xstream.alias("assetSyncInfo", AssetSyncInfo.class);
      xstream.alias("columnSyncInfo", ColumnSyncInfo.class);
      xstream.alias("memberSyncInfo", MemberSyncInfo.class);
      return xstream.toXML(assetSyncInfo);
   }

   public static AssetSyncInfo convertXMLtoAssetSyncObject (String assetSyncXML) {
      XStream xstream = new XStream();
      xstream.alias("assetSyncInfo", AssetSyncInfo.class);
      xstream.alias("columnSyncInfo", ColumnSyncInfo.class);
      xstream.alias("memberSyncInfo", MemberSyncInfo.class);
      return (AssetSyncInfo) xstream.fromXML(assetSyncXML);
   }

   public static String convertAnalysisReportToXML (AssetAnalysisReport assetAnalysisReport) {
      XStream xstream = new XStream();
      xstream.alias("assetAnalysisReport", AssetAnalysisReport.class);
      xstream.addImplicitCollection(AssetAnalysisReport.class, "assetAnalysisReportInfoList");
      xstream.alias("AssetAnalysisReportInfo", AssetAnalysisReportInfo.class);
      xstream.addImplicitCollection(AssetAnalysisReportInfo.class, "assetAnalysisTablesInfo");
      xstream.alias("table", AssetAnalysisTableInfo.class);
      xstream.alias("column", AssetOperationColumn.class);
      xstream.alias("member", AssetOperationMember.class);
      return xstream.toXML(assetAnalysisReport);
   }

   @SuppressWarnings ("unchecked")
   public static AssetAnalysisReport convertXMLToAnalysisReportObject (String analysisReportXML) {
      XStream xstream = new XStream();
      xstream.alias("assetAnalysisReport", AssetAnalysisReport.class);
      xstream.addImplicitCollection(AssetAnalysisReport.class, "assetAnalysisReportInfoList");
      xstream.addImplicitCollection(AssetAnalysisReportInfo.class, "assetAnalysisTablesInfo");
      xstream.alias("AssetAnalysisReportInfo", AssetAnalysisReportInfo.class);
      xstream.alias("table", AssetAnalysisTableInfo.class);
      xstream.alias("column", AssetOperationColumn.class);
      xstream.alias("member", AssetOperationMember.class);
      return (AssetAnalysisReport) xstream.fromXML(analysisReportXML);
   }
}
