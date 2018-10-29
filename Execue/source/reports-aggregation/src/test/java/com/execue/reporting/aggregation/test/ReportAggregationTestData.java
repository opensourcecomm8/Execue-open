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


package com.execue.reporting.aggregation.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.BusinessStat;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.governor.AssetEntityTerm;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.AssetEntityType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.type.StatType;

public class ReportAggregationTestData {

   private static BusinessTerm generateBusinessTerm (String name, boolean setUserDefinedStat) {
      Concept businessEntity = new Concept();
      businessEntity.setName(name);
      // Add average as the default statistic
      Set<Stat> stats = new HashSet<Stat>();
      Stat stat = new Stat(StatType.AVERAGE);
      stats.add(stat);
      businessEntity.setStats(stats);
      BusinessTerm businessTerm = new BusinessTerm();
      BusinessStat businessStat = new BusinessStat();
      stat = new Stat(StatType.SUM);
      businessStat.setStat(stat);
      businessStat.setRequestedByUser(setUserDefinedStat);
      businessTerm.setBusinessStat(businessStat);
      BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
      businessEntityTerm.setBusinessEntity(businessEntity);
      businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
      businessTerm.setBusinessEntityTerm(businessEntityTerm);
      return businessTerm;
   }

   private static AssetEntityTerm generateAssetTerm (String name, ColumnType columnType) {
      Colum assetEntity = new Colum();
      assetEntity.setName(name);
      assetEntity.setKdxDataType(columnType);
      AssetEntityTerm assetTerm = new AssetEntityTerm();
      assetTerm.setAssetEntity(assetEntity);
      assetTerm.setAssetEntityType(AssetEntityType.COLUMN);
      return assetTerm;
   }

   public static StructuredQuery generateLogicalQuery () {
      List<BusinessAssetTerm> businessAssetTerms = new ArrayList<BusinessAssetTerm>();
      // prepare the first BusinessAssetTerm
      BusinessTerm businessTerm = generateBusinessTerm("sales", true);
      // prepare AssetTerms
      AssetEntityTerm assetTerm = generateAssetTerm("SALES_AMT", ColumnType.MEASURE);
      // prepare the BusinessAssetTerm
      BusinessAssetTerm businessAssetTerm = new BusinessAssetTerm();
      businessAssetTerm.setBusinessTerm(businessTerm);
      businessAssetTerm.setAssetEntityTerm(assetTerm);
      businessAssetTerms.add(businessAssetTerm);

      // prepare the second BusinessAssetTerm
      businessTerm = generateBusinessTerm("interest", false);
      // another AssetTerm
      assetTerm = generateAssetTerm("INTEREST", ColumnType.MEASURE);
      // prepare the BusinessAssetTerm
      businessAssetTerm = new BusinessAssetTerm();
      businessAssetTerm.setBusinessTerm(businessTerm);
      businessAssetTerm.setAssetEntityTerm(assetTerm);
      businessAssetTerms.add(businessAssetTerm);

      // prepare the third BusinessAssetTerm
      businessTerm = generateBusinessTerm("sales", false);
      // another AssetTerm
      assetTerm = generateAssetTerm("SALES_REGIONS", ColumnType.DIMENSION);
      // prepare the BusinessAssetTerm
      businessAssetTerm = new BusinessAssetTerm();
      businessAssetTerm.setBusinessTerm(businessTerm);
      businessAssetTerm.setAssetEntityTerm(assetTerm);
      businessAssetTerms.add(businessAssetTerm);

      // prepare the structured query
      StructuredQuery structuredQuery = new StructuredQuery();
      structuredQuery.setMetrics(businessAssetTerms);
      // set the asset
      Asset asset = new Asset();
      asset.setName("ASSET_SALES");
      // asset.setType(AssetType.Mart.name());
      structuredQuery.setAsset(asset);
      return structuredQuery;
   }

   public static Query generatePhysicalQuery () {
      SelectEntity selectEntity = new SelectEntity();
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();

      // prepare SelectEntity object for SALES_AMT
      QueryTableColumn tableColumn = new QueryTableColumn();
      QueryTable qTable = new QueryTable();
      qTable.setTableName("SALES");
      Tabl ownerTable = new Tabl();
      ownerTable.setName("SALES");
      // qTable.setTable(ownerTable);
      QueryColumn qColumn = new QueryColumn();
      String colName = "SALES_AMT";
      Colum column = new Colum();
      column.setName(colName);
      column.setKdxDataType(ColumnType.MEASURE);
      column.setOwnerTable(ownerTable);
      // qColumn.setColumn(column);
      qColumn.setColumnName(colName);

      tableColumn.setColumn(qColumn);
      tableColumn.setTable(qTable);

      selectEntity.setTableColumn(tableColumn);
      selectEntity.setType(SelectEntityType.TABLE_COLUMN);
      selectEntities.add(selectEntity);

      // prepare SelectEntity object for SALES_REGIONS
      tableColumn = new QueryTableColumn();
      qColumn = new QueryColumn();
      colName = "SALES_REGIONS";
      column = new Colum();
      column.setName(colName);
      column.setKdxDataType(ColumnType.DIMENSION);
      column.setOwnerTable(ownerTable);
      // qColumn.setColumn(column);
      qColumn.setColumnName(colName);

      tableColumn.setColumn(qColumn);
      tableColumn.setTable(qTable);

      selectEntity.setTableColumn(tableColumn);
      selectEntity.setType(SelectEntityType.TABLE_COLUMN);
      selectEntities.add(selectEntity);
      Query query = new Query();
      query.setSelectEntities(selectEntities);
      return query;
   }
}