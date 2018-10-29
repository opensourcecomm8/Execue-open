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


package com.execue.swi.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.bean.entity.AppDataSource;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.AssetOperationInfo;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Constraint;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.HistoryAssetOperationInfo;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.bean.governor.AssetEntityTerm;
import com.execue.core.common.type.ConstraintType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.EntityType;
import com.execue.core.common.type.OperationType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IAssetDetailService;
import com.execue.swi.service.IBusinessEntityMaintenanceService;
import com.execue.swi.service.IJoinService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ISDXDeletionService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.swi.validation.ISWIValidator;

public class SDXManagementServiceImpl extends SDXCommonServiceImpl implements ISDXManagementService {

   private Logger                            logger = Logger.getLogger(SDXManagementServiceImpl.class);

   private IAssetDetailService               assetDetailService;

   private ISWIConfigurationService          swiConfigurationService;

   private IKDXRetrievalService              kdxRetrievalService;

   private IBusinessEntityMaintenanceService businessEntityMaintenanceService;

   private IJoinService                      joinService;

   private ISDXRetrievalService              sdxRetrievalService;

   private ISDXDeletionService               sdxDeletionService;

   private ISWIValidator                     swiValidator;

   public IAssetDetailService getAssetDetailService () {
      return assetDetailService;
   }

   public void setAssetDetailService (IAssetDetailService assetDetailService) {
      this.assetDetailService = assetDetailService;
   }

   @Override
   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   @Override
   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   @Override
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   @Override
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   @Override
   public IBusinessEntityMaintenanceService getBusinessEntityMaintenanceService () {
      return businessEntityMaintenanceService;
   }

   @Override
   public void setBusinessEntityMaintenanceService (IBusinessEntityMaintenanceService businessEntityMaintenanceService) {
      this.businessEntityMaintenanceService = businessEntityMaintenanceService;
   }

   public IJoinService getJoinService () {
      return joinService;
   }

   public void setJoinService (IJoinService joinService) {
      this.joinService = joinService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   // ********************* Methods related to DataSource starts **************************

   public void createDataSource (DataSource dataSource) throws SDXException {
      if (getSwiValidator().dataSourceAlreadyExists(dataSource)) {
         throw new SDXException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, "DataSource with name ["
                  + dataSource.getDisplayName() + "] already exists");
      }
      getSdxDataAccessManager().createDataSource(dataSource);
   }

   public void updateDataSource (DataSource dataSource) throws SDXException {
      if (getSwiValidator().dataSourceAlreadyExists(dataSource)) {
         throw new SDXException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, "DataSource with name ["
                  + dataSource.getName() + "] already exists");
      }
      getSdxDataAccessManager().updateDataSource(dataSource);

   }

   // ********************* Methods related to DataSource ends **************************

   // ********************* Methods related to Asset starts **************************

   public void createAsset (Asset asset) throws SDXException {
      if (getSdxRetrievalService().getAsset(asset.getApplication().getId(), asset.getName()) != null) {
         throw new SDXException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, "Asset with name [" + asset.getName()
                  + "] already exists");
      }
      getSdxDataAccessManager().createAsset(asset);
   }

   public void updateAsset (Asset asset) throws SDXException {
      getSdxDataAccessManager().updateAsset(asset);
   }

   @Override
   public void updateAssetStatus (Long assetId, StatusEnum status) throws SDXException {
      getSdxDataAccessManager().updateAssetStatus(assetId, status);
   }

   // ********************* Methods related to Asset ends **************************

   // ********************* Methods related to Table starts **************************

   public void createAssetTables (Asset asset, List<TableInfo> tablesInfo) throws SDXException {
      for (TableInfo tableInfo : tablesInfo) {
         updateColumnsForMissingUnitPopulation(tableInfo.getColumns());
      }
      getSdxDataAccessManager().createAssetTables(asset, tablesInfo);
   }

   private void updateColumnsForMissingUnitPopulation (List<Colum> colums) {
      for (Colum colum : colums) {
         updateColumnForMissingUnitPopulation(colum);
      }
   }

   private void updateColumnForMissingUnitPopulation (Colum colum) {
      if (colum.getConversionType() != null && colum.getUnit() == null) {
         if (colum.getDataFormat() != null) {
            String dateFormat = colum.getDataFormat();
            if (DataType.DATETIME.equals(colum.getDataType()) && colum.getFileDateFormat() != null) {
               dateFormat = colum.getFileDateFormat();
            }
            DateQualifier dateQualifier = getSwiConfigurationService().getDateQualifier(dateFormat);
            if (dateQualifier != null) {
               colum.setUnit(dateQualifier.getValue());
            }
         }
      }
   }

   public void updateAssetTables (Asset asset, List<TableInfo> tablesInfo) throws SDXException {
      for (TableInfo tableInfo : tablesInfo) {
         updateColumnsForMissingUnitPopulation(tableInfo.getColumns());
         updateBusinessEntityDefinitionInfoForColumns(asset.getId(), tableInfo.getTable().getId(), tableInfo
                  .getColumns());
      }
      getSdxDataAccessManager().updateAssetTables(asset, tablesInfo);

   }

   private void updateBusinessEntityDefinitionInfoForColumns (Long assetId, Long tableId, List<Colum> colums)
            throws SDXException {
      for (Colum colum : colums) {
         updateBusinessEntityDefinitionInfoForColumn(assetId, tableId, colum);
      }
   }

   private void updateBusinessEntityDefinitionInfoForColumn (Long assetId, Long tableId, Colum colum)
            throws SDXException {
      try {
         BusinessEntityDefinition mappedConceptBED = getKdxRetrievalService().getMappedConceptBEDForColumn(assetId,
                  tableId, colum.getId());
         if (mappedConceptBED != null) {
            Model model = getKdxRetrievalService().getModelByUserModelGroupId(mappedConceptBED.getModelGroup().getId());
            getBusinessEntityMaintenanceService().createBusinessEntityMaintenanceInfo(
                     ExecueBeanManagementUtil.prepareBusinessEntityMaintenanceInfo(mappedConceptBED.getId(),
                              EntityType.CONCEPT, model.getId(), OperationType.UPDATE, null));
         }
      } catch (MappingException mappingException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, mappingException);
      } catch (KDXException kdxException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, kdxException);
      }
   }

   public void updateTable (Tabl table) throws SDXException {
      getSdxDataAccessManager().updateTable(table);
   }

   public void updateTables (List<Tabl> tables) throws SDXException {
      getSdxDataAccessManager().updateTables(tables);
   }

   // ********************* Methods related to Table ends **************************

   // ********************* Methods related to Column starts **************************

   public void createColumns (Asset asset, Tabl table, List<Colum> columns) throws SDXException {
      updateColumnsForMissingUnitPopulation(columns);
      updateBusinessEntityDefinitionInfoForColumns(asset.getId(), table.getId(), columns);
      getSdxDataAccessManager().createColumns(asset, table, columns);
   }

   public void updateColumns (Long assetId, Long tableId, List<Colum> colums) throws SDXException {
      updateColumnsForMissingUnitPopulation(colums);
      updateBusinessEntityDefinitionInfoForColumns(assetId, tableId, colums);
      getSdxDataAccessManager().updateColumns(colums);
   }

   // ********************* Methods related to Column ends **************************

   // ********************* Methods related to Member starts **************************

   public void createMembers (Asset asset, Tabl table, List<Colum> columns, List<Membr> members) throws SDXException {
      getSdxDataAccessManager().createMembers(asset, table, columns, members);
   }

   public void updateMember (Membr member) throws SDXException {
      getSdxDataAccessManager().updateMember(member);
   }

   public void updateMembers (List<Membr> members) throws SDXException {
      getSdxDataAccessManager().updateMembers(members);
   }

   // ********************* Methods related to Member ends **************************

   // ********************* Methods related to Constraints starts **************************

   public void createConstraints (List<Constraint> constraints) throws SDXException {
      logger.debug("Creating Constraints");
      getSdxDataAccessManager().createConstraints(constraints);
   }

   public void updateConstraints (List<Constraint> constraints) throws SDXException {
      for (Constraint constraint : constraints) {
         if (ConstraintType.PRIMARY_KEY.equals(constraint.getType())) {
            for (Colum colum : constraint.getConstraintColums()) {
               List<Constraint> referencedConstraints = getSdxRetrievalService()
                        .getReferencedConstraints(colum.getId());
               getSdxDeletionService().deleteConstraints(referencedConstraints);
            }
            getSdxDataAccessManager().updateConstraint(constraint);
         } else if (ConstraintType.FOREIGN_KEY.equals(constraint.getType())) {
            getSdxDataAccessManager().updateConstraint(constraint);
         }
      }
   }

   // ********************* Methods related to Constraints ends **************************

   // ********************* Methods related to AED's starts **************************
   public void updateAssetEntityDefinition (AssetEntityDefinition assetEntityDefinition) throws SDXException {
      getSdxDataAccessManager().updateAssetEntityDefinition(assetEntityDefinition);
   }

   public Integer updateAssetEntitiesPopularity (List<AssetEntityTerm> assetEntityTerms) throws SDXException {
      return getSdxDataAccessManager().updateAssetEntitiesPopularity(assetEntityTerms);
   }

   // ********************* Methods related to AED's ends **************************

   // ********************* Methods related to AssetOperation and AssetHistoryOperation's starts
   // **************************

   public void createAssetOperation (AssetOperationInfo assetOperationInfo) throws SDXException {
      getSdxDataAccessManager().createAssetOperation(assetOperationInfo);

   }

   public void createHistoryAssetOperation (HistoryAssetOperationInfo historyAssetOperationInfo) throws SDXException {
      getSdxDataAccessManager().createHistoryAssetOperation(historyAssetOperationInfo);
   }

   public void updateAssetOperation (AssetOperationInfo assetOperationInfo) throws SDXException {
      getSdxDataAccessManager().updateAssetOperation(assetOperationInfo);
   }

   public void updateHistoryAssetOperation (HistoryAssetOperationInfo historyAssetOperationInfo) throws SDXException {
      getSdxDataAccessManager().updateHistoryAssetOperation(historyAssetOperationInfo);
   }

   // ********************* Methods related to AssetOperation and AssetHistoryOperation's ends
   // **************************

   // ******************************* Miscellaneous methods*********************************************

   public boolean isAssetTableColumnChangedNameUnique (Long assetId, Long tableId, Long columnId,
            String columnDisplayName) throws SDXException {
      boolean assetTableColumnChangedNameUnique = true;
      if (ExecueCoreUtil.isCollectionNotEmpty(getSdxDataAccessManager().getOtherAssetTableColumnIdsByDisplayName(
               assetId, tableId, columnId, columnDisplayName))) {
         assetTableColumnChangedNameUnique = false;
      }
      return assetTableColumnChangedNameUnique;
   }

   public void createAppDataSourceMapping (AppDataSource appDataSource) throws SDXException {
      getSdxDataAccessManager().createAppDataSourceMapping(appDataSource);
   }

   public void createAppDataSourceMappings (List<AppDataSource> appDataSources) throws SDXException {
      getSdxDataAccessManager().createAppDataSourceMappings(appDataSources);
   }

   public ISDXDeletionService getSdxDeletionService () {
      return sdxDeletionService;
   }

   public void setSdxDeletionService (ISDXDeletionService sdxDeletionService) {
      this.sdxDeletionService = sdxDeletionService;
   }

   public ISWIValidator getSwiValidator () {
      return swiValidator;
   }

   public void setSwiValidator (ISWIValidator swiValidator) {
      this.swiValidator = swiValidator;
   }
}