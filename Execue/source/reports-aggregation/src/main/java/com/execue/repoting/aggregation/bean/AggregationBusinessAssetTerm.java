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


package com.execue.repoting.aggregation.bean;

import com.execue.core.common.type.AssetEntityType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.StatType;

/**
 * @author John Mallavalli
 */
/**
 * @author john
 */
public class AggregationBusinessAssetTerm {

   private Long                 businessEntityDefinitionId;
   private Long                 assetEntityDefinitionId;
   private AggregationRangeInfo aggregationRangeInfo;
   private boolean              requestedByUser;
   private BusinessEntityType   businessEntityType;
   private AssetEntityType      assetEntityType;
   private StatType             businessStatType;
   private boolean              businessStatRequestedByUser;

   private ColumnType           kdxDataType;
   private String               columnName;
   private DataType             columnDataType;
   private String               ownerTableName;
   private LookupType           tableLookupType;
   private String               tableAlias;
   private String               tableOwner;
   private String               tableActualName;
   private CheckType            tableVirtual;
   private boolean              fromCohort       = false;
   private boolean              fromPopulation   = false;
   private boolean              fromDistribution = false;
   private boolean              dependantMeasure = false;
   private Integer              identifier;

   public Long getBusinessEntityDefinitionId () {
      return businessEntityDefinitionId;
   }

   public void setBusinessEntityDefinitionId (Long businessEntityDefinitionId) {
      this.businessEntityDefinitionId = businessEntityDefinitionId;
   }

   public Long getAssetEntityDefinitionId () {
      return assetEntityDefinitionId;
   }

   public void setAssetEntityDefinitionId (Long assetEntityDefinitionId) {
      this.assetEntityDefinitionId = assetEntityDefinitionId;
   }

   public AggregationRangeInfo getAggregationRangeInfo () {
      return aggregationRangeInfo;
   }

   public void setAggregationRangeInfo (AggregationRangeInfo aggregationRangeInfo) {
      this.aggregationRangeInfo = aggregationRangeInfo;
   }

   public boolean isRequestedByUser () {
      return requestedByUser;
   }

   public void setRequestedByUser (boolean requestedByUser) {
      this.requestedByUser = requestedByUser;
   }

   public BusinessEntityType getBusinessEntityType () {
      return businessEntityType;
   }

   public void setBusinessEntityType (BusinessEntityType businessEntityType) {
      this.businessEntityType = businessEntityType;
   }

   public AssetEntityType getAssetEntityType () {
      return assetEntityType;
   }

   public void setAssetEntityType (AssetEntityType assetEntityType) {
      this.assetEntityType = assetEntityType;
   }

   public ColumnType getKdxDataType () {
      return kdxDataType;
   }

   public void setKdxDataType (ColumnType kdxDataType) {
      this.kdxDataType = kdxDataType;
   }

   public String getColumnName () {
      return columnName;
   }

   public void setColumnName (String columnName) {
      this.columnName = columnName;
   }

   public DataType getColumnDataType () {
      return columnDataType;
   }

   public void setColumnDataType (DataType columnDataType) {
      this.columnDataType = columnDataType;
   }

   public String getOwnerTableName () {
      return ownerTableName;
   }

   public void setOwnerTableName (String ownerTableName) {
      this.ownerTableName = ownerTableName;
   }

   public LookupType getTableLookupType () {
      return tableLookupType;
   }

   public void setTableLookupType (LookupType tableLookupType) {
      this.tableLookupType = tableLookupType;
   }

   public String getTableAlias () {
      return tableAlias;
   }

   public void setTableAlias (String tableAlias) {
      this.tableAlias = tableAlias;
   }

   public String getTableOwner () {
      return tableOwner;
   }

   public void setTableOwner (String tableOwner) {
      this.tableOwner = tableOwner;
   }

   public String getTableActualName () {
      return tableActualName;
   }

   public void setTableActualName (String tableActualName) {
      this.tableActualName = tableActualName;
   }

   public CheckType getTableVirtual () {
      return tableVirtual;
   }

   public void setTableVirtual (CheckType tableVirtual) {
      this.tableVirtual = tableVirtual;
   }

   public boolean isFromCohort () {
      return fromCohort;
   }

   public void setFromCohort (boolean fromCohort) {
      this.fromCohort = fromCohort;
   }

   public boolean isFromPopulation () {
      return fromPopulation;
   }

   public void setFromPopulation (boolean fromPopulation) {
      this.fromPopulation = fromPopulation;
   }

   public boolean isFromDistribution () {
      return fromDistribution;
   }

   public void setFromDistribution (boolean fromDistribution) {
      this.fromDistribution = fromDistribution;
   }

   public Integer getIdentifier () {
      return identifier;
   }

   public void setIdentifier (Integer identifier) {
      this.identifier = identifier;
   }

   /**
    * @return the businessStatType
    */
   public StatType getBusinessStatType () {
      return businessStatType;
   }

   /**
    * @param businessStatType the businessStatType to set
    */
   public void setBusinessStatType (StatType businessStatType) {
      this.businessStatType = businessStatType;
   }

   /**
    * @return the businessStatRequestedByUser
    */
   public boolean isBusinessStatRequestedByUser () {
      return businessStatRequestedByUser;
   }

   /**
    * @param businessStatRequestedByUser the businessStatRequestedByUser to set
    */
   public void setBusinessStatRequestedByUser (boolean businessStatRequestedByUser) {
      this.businessStatRequestedByUser = businessStatRequestedByUser;
   }

   /**
    * @return the dependantMeasure
    */
   public boolean isDependantMeasure () {
      return dependantMeasure;
   }

   /**
    * @param dependantMeasure the dependantMeasure to set
    */
   public void setDependantMeasure (boolean dependantMeasure) {
      this.dependantMeasure = dependantMeasure;
   }
}
