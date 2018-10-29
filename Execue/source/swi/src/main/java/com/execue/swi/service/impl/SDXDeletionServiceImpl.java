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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetOperationInfo;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Constraint;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.HistoryAssetOperationInfo;
import com.execue.core.common.bean.entity.Join;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.type.ConstraintType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.dataaccess.IMappingDataAccessManager;
import com.execue.swi.exception.JoinException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IAssetDetailService;
import com.execue.swi.service.IDefaultDynamicValueService;
import com.execue.swi.service.IJoinService;
import com.execue.swi.service.ISDXDeletionService;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.swi.validation.ISWIValidator;

/**
 * This service acts as a wrapper for the deletion services
 * 
 * @author MurthySN
 * @version 1.0
 * @since 29/01/10
 */

public class SDXDeletionServiceImpl extends SDXCommonServiceImpl implements ISDXDeletionService {

   private ISDXRetrievalService        sdxRetrievalService;
   private IJoinService                joinService;
   private IAssetDetailService         assetDetailService;
   private IDefaultDynamicValueService defaultDynamicValueService;
   private IMappingDataAccessManager   mappingDataAccessManager;
   private ISWIValidator               swiValidator;
   private ISWIConfigurationService    swiConfigurationService;

   // Deletion methods
   public void deleteAsset (Asset asset) throws SDXException {
      getSdxDataAccessManager().deleteAsset(asset);
   }

   public void deleteDataSource (DataSource dataSource) throws SDXException {
      if (getSwiValidator().assetsExistForDataSource(dataSource.getId())) {
         throw new SDXException(SWIExceptionCodes.ENTITY_ALREADY_EXISTS, " DataSource is used by an existing asset");
      }
      getSdxDataAccessManager().deleteDataSource(dataSource);
   }

   public void deleteMembers (List<Membr> members) throws SDXException {
      getSdxDataAccessManager().deleteMembers(members);
   }
   
   @Override
   public void deleteMembersInBatches (Colum column, int batchSize) throws SDXException {
      getSdxDataAccessManager().deleteMembersInBatches(column, batchSize);
   }
   
   @Override
   public void deleteTablesWithOutConsideringChildEntities (List<Tabl> tables) throws SDXException {
      getSdxDataAccessManager().deleteTables(tables);
   }
   
   @Override
   public void deleteColumnsWithOutConsideringChildEntities (List<Colum> columns) throws SDXException {
      getSdxDataAccessManager().deleteColumns(columns);
   }

   public void deleteConstraints (List<Constraint> constraints) throws SDXException {
      for (Constraint constraint : constraints) {
         if (ConstraintType.PRIMARY_KEY.equals(constraint.getType())) {
            // if constraint columns are empty
            // from the constaint id, get the constraint from swi, lazy loaded method
            // update the column flags accordingly.
            // delete the constraint
            for (Colum colum : constraint.getConstraintColums()) {
               List<Constraint> referencedConstraints = getSdxRetrievalService()
                        .getReferencedConstraints(colum.getId());
               deleteConstraints(referencedConstraints);
            }
            getSdxDataAccessManager().deleteConstraint(constraint);
         } else if (ConstraintType.FOREIGN_KEY.equals(constraint.getType())) {
            getSdxDataAccessManager().deleteConstraint(constraint);
         }
      }
   }

   public void deleteConstraints (Long assetId) throws SDXException {
      Asset asset = null;
      try {
         asset = getSdxDataAccessManager().getById(assetId, Asset.class);
      } catch (SWIException swiException) {
         throw new SDXException(swiException.getCode(), swiException);
      }
      List<TableInfo> tablesInfo = getSdxRetrievalService().getAssetTables(asset);
      for (TableInfo tableInfo : tablesInfo) {
         Tabl tabl = tableInfo.getTable();
         List<Colum> tableColums = getSdxRetrievalService().getColumnsOfTable(tabl);
         for (Colum colum : tableColums) {
            Set<Constraint> columConstraints = colum.getConstraints();
            if (columConstraints.size() > 0) {
               deleteConstraints(new ArrayList<Constraint>(columConstraints));
            }
            colum.setConstraints(null);
            updateColumn(assetId, tabl.getId(), colum);
         }
      }
   }

   public void deleteAssetOperation (AssetOperationInfo assetOperationInfo) throws SDXException {
      getSdxDataAccessManager().deleteAssetOperation(assetOperationInfo);
   }

   public void deleteHistoryAssetOperation (HistoryAssetOperationInfo historyAssetOperationInfo) throws SDXException {
      getSdxDataAccessManager().deleteHistoryAssetOperation(historyAssetOperationInfo);
   }

   public void deleteAppDataSourceMappings (Long appId) throws SDXException {
      getSdxDataAccessManager().deleteAppDataSourceMappings(appId);
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IJoinService getJoinService () {
      return joinService;
   }

   public void setJoinService (IJoinService joinService) {
      this.joinService = joinService;
   }

   public IAssetDetailService getAssetDetailService () {
      return assetDetailService;
   }

   public void setAssetDetailService (IAssetDetailService assetDetailService) {
      this.assetDetailService = assetDetailService;
   }

   public IDefaultDynamicValueService getDefaultDynamicValueService () {
      return defaultDynamicValueService;
   }

   public void setDefaultDynamicValueService (IDefaultDynamicValueService defaultDynamicValueService) {
      this.defaultDynamicValueService = defaultDynamicValueService;
   }

   public IMappingDataAccessManager getMappingDataAccessManager () {
      return mappingDataAccessManager;
   }

   public void setMappingDataAccessManager (IMappingDataAccessManager mappingDataAccessManager) {
      this.mappingDataAccessManager = mappingDataAccessManager;
   }

   public ISWIValidator getSwiValidator () {
      return swiValidator;
   }

   public void setSwiValidator (ISWIValidator swiValidator) {
      this.swiValidator = swiValidator;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

}
