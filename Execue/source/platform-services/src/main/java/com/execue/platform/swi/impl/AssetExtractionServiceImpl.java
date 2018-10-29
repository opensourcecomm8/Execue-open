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


package com.execue.platform.swi.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetDetail;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.AssetExtractionInput;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.SuccessFailureType;
import com.execue.das.dataaccess.ISystemDataAccessService;
import com.execue.dataaccess.AssetSourceConnectionProvider;
import com.execue.dataaccess.exception.AssetSourceConnectionException;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.platform.exception.SourceMetaDataException;
import com.execue.platform.swi.IAssetExtractionService;
import com.execue.platform.swi.IConstraintPopulationService;
import com.execue.platform.swi.ISourceMetaDataService;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.JoinException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IAssetDetailService;
import com.execue.swi.service.IJoinService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

public class AssetExtractionServiceImpl implements IAssetExtractionService {

   private IJoinService                  joinService;

   private AssetSourceConnectionProvider assetSourceConnectionProvider;

   private ISourceMetaDataService        sourceMetaDataService;

   private ISDXManagementService         sdxManagementService;

   private ISDXRetrievalService          sdxRetrievalService;

   private IAssetDetailService           assetDetailService;

   private ISWIConfigurationService      swiConfigurationService;

   private IConstraintPopulationService  constraintPopulationService;

   private ISystemDataAccessService      systemDataAccessService;

   private static final Logger           logger = Logger.getLogger(AssetExtractionServiceImpl.class);

   public void absorbConstraints (Asset asset) throws SDXException {
      logger.debug("Inside absorbConstraints method");
      getConstraintPopulationService().createConstraintsByRelations(asset.getId());
      logger.error("Constraints successfully absorbed for : " + asset.getName());
   }

   public void absorbJoins (Asset asset) throws SDXException {
      logger.debug("Inside absorbJoins method");
      try {
         getJoinService().createJoins(asset.getId(), getJoinService().suggestDirectJoinsByForeignKey(asset.getId()));
      } catch (JoinException joinException) {
         throw new SDXException(SWIExceptionCodes.ASSET_CREATION_FAILED, joinException);
      }
   }

   public SuccessFailureType registerAsset (AssetExtractionInput assetExtractionInput) throws SDXException {
      logger.debug("Inside registerAsset method");
      SuccessFailureType successFailureType = SuccessFailureType.SUCCESS;
      Asset sourceAsset = assetExtractionInput.getSourceAsset();
      try {
         // Set the default for asset object
         sourceAsset.setStatus(StatusEnum.INACTIVE);
         logger.debug("Validating the Asset : " + sourceAsset.getName());
         if (!validateAsset(assetExtractionInput)) {
            logger.error("Validation Failed for Asset : " + sourceAsset.getName());
            successFailureType = SuccessFailureType.FAILURE;
         } else {

            logger.debug("Extracting the Tables, Columns and Members info using meta data");
            List<TableInfo> tables = new ArrayList<TableInfo>();
            List<Tabl> inputTables = assetExtractionInput.getTables();
            for (Tabl table : inputTables) {
               TableInfo tableInfo = getSourceMetaDataService().getTableFromSource(sourceAsset, table);
               tables.add(tableInfo);
            }
            logger.debug("Creating an Asset in system");
            sourceAsset.setPriority(100D);
            createAsset(sourceAsset);
            createAssetDetail(sourceAsset);
            logger.debug("Creating tables,colums and membrs in system");
            createAssetTables(sourceAsset, tables);
         }
      } catch (SDXException e) {
         logger.error("Asset extraction failed for asset : " + sourceAsset.getName());
         throw new SDXException(DataAccessExceptionCodes.ASSET_CREATION_FAILED, e);
      } catch (SourceMetaDataException e) {
         logger.error("Asset extraction failed for asset : " + sourceAsset.getName());
         throw new SDXException(DataAccessExceptionCodes.META_DATA_EXTRACTION_FAILED, e);
      }
      return successFailureType;
   }

   private void createAsset (Asset asset) throws SDXException {
      logger.debug("Inside createAsset method");
      getSdxManagementService().createAsset(asset);
   }

   private void createAssetTables (Asset asset, List<TableInfo> tablesInfo) throws SDXException {
      logger.debug("Inside createAssetTables method");
      getSdxManagementService().createAssetTables(asset, tablesInfo);
   }

   private void createAssetDetail (Asset asset) throws SDXException {
      AssetDetail assetDetail = new AssetDetail();
      assetDetail.setAssetId(asset.getId());

      getAssetDetailService().createUpdateAssetDetail(assetDetail);
   }

   /*
    * The following validations are performed 1) Validate asset to make sure that all the mandatory info is available 2)
    * Validate connection to the source 3) Evaluate tables info !!!
    */
   public boolean validateAsset (AssetExtractionInput assetExtractionInput) throws SDXException {
      logger.debug("Inside validateAsset method");
      boolean isValidAsset = false;
      if ((validateAssetInfo(assetExtractionInput)) && (validateAssetConnection(assetExtractionInput))
               && (validateAssetTables(assetExtractionInput)) && (!validateAssetExistance(assetExtractionInput))) {
         logger.debug("Asset is Valid : " + assetExtractionInput.getSourceAsset().getName());
         isValidAsset = true;
      }
      return isValidAsset;
   }

   private boolean validateAssetExistance (AssetExtractionInput assetExtractionInput) throws SDXException {
      logger.debug("Inside validateAssetExistance method");
      Long applicationId = assetExtractionInput.getSourceAsset().getApplication().getId();
      String assetName = assetExtractionInput.getSourceAsset().getName();
      return getSdxRetrievalService().isAssetExists(applicationId, assetName);
   }

   private boolean validateAssetTables (AssetExtractionInput assetExtractionInput) throws SDXException {
      logger.debug("Inside getAssetTables method");
      boolean isAssetTablesExists = true;
      try {
         for (Tabl table : assetExtractionInput.getTables()) {
            // add the validation with actual name
            if (CheckType.NO.equals(table.getVirtual())) {
               if (!getSystemDataAccessService().isTableExists(assetExtractionInput.getSourceAsset().getDataSource(),
                        table.getName())) {
                  isAssetTablesExists = false;
                  break;
               }
            }
         }
      } catch (DataAccessException dataAccessException) {
         logger.error("Asset tables validation failed");
         throw new SDXException(DataAccessExceptionCodes.META_DATA_EXTRACTION_FAILED, dataAccessException);
      }
      return isAssetTablesExists;
   }

   private boolean validateAssetConnection (AssetExtractionInput assetExtractionInput) throws SDXException {
      logger.debug("Inside validateAssetConnection method");
      boolean isAssetConnectionPossible = true;
      Connection connection = null;
      try {
         logger.debug("Trying to establish connection to asset " + assetExtractionInput.getSourceAsset().getName());
         connection = getAssetSourceConnectionProvider().getConnection(assetExtractionInput.getSourceAsset());
         if (connection == null) {
            logger.debug("Connection established to asset : " + assetExtractionInput.getSourceAsset().getName());
            isAssetConnectionPossible = false;
         }
      } catch (AssetSourceConnectionException assetSourceConnectionException) {
         logger.error("Connection refused error");
         throw new SDXException(SWIExceptionCodes.ASSET_CONNECTION_REFUSED, assetSourceConnectionException);
      } finally {
         try {
            getAssetSourceConnectionProvider().closeConnection(connection);
         } catch (AssetSourceConnectionException e) {
            logger.error("Connection closed error");
            throw new SDXException(SWIExceptionCodes.ASSET_CONNECTION_REFUSED, e);
         }
      }
      return isAssetConnectionPossible;
   }

   private boolean validateAssetInfo (AssetExtractionInput assetExtractionInput) {
      logger.debug("Inside validateAssetInfo method");
      boolean isValidAsset = true;
      Asset asset = assetExtractionInput.getSourceAsset();
      if ((asset.getName().length() == 0) || (asset.getDataSource().getProviderType() == null)
               || (asset.getDataSource().getLocation().length() == 0)
               || (asset.getDataSource().getUserName().length() == 0)
               || (asset.getDataSource().getSchemaName().length() == 0) || (asset.getDataSource().getPort() == 0)) {
         logger.debug("Asset Info is Valid : " + asset.getName());
         isValidAsset = false;
      }
      return isValidAsset;
   }

   public IJoinService getJoinService () {
      return joinService;
   }

   public void setJoinService (IJoinService joinService) {
      this.joinService = joinService;
   }

   public AssetSourceConnectionProvider getAssetSourceConnectionProvider () {
      return assetSourceConnectionProvider;
   }

   public void setAssetSourceConnectionProvider (AssetSourceConnectionProvider assetSourceConnectionProvider) {
      this.assetSourceConnectionProvider = assetSourceConnectionProvider;
   }

   public ISourceMetaDataService getSourceMetaDataService () {
      return sourceMetaDataService;
   }

   public void setSourceMetaDataService (ISourceMetaDataService sourceMetaDataService) {
      this.sourceMetaDataService = sourceMetaDataService;
   }

   public IAssetDetailService getAssetDetailService () {
      return assetDetailService;
   }

   public void setAssetDetailService (IAssetDetailService assetDetailService) {
      this.assetDetailService = assetDetailService;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public ISDXManagementService getSdxManagementService () {
      return sdxManagementService;
   }

   public void setSdxManagementService (ISDXManagementService sdxManagementService) {
      this.sdxManagementService = sdxManagementService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IConstraintPopulationService getConstraintPopulationService () {
      return constraintPopulationService;
   }

   public void setConstraintPopulationService (IConstraintPopulationService constraintPopulationService) {
      this.constraintPopulationService = constraintPopulationService;
   }

   public ISystemDataAccessService getSystemDataAccessService () {
      return systemDataAccessService;
   }

   public void setSystemDataAccessService (ISystemDataAccessService systemDataAccessService) {
      this.systemDataAccessService = systemDataAccessService;
   }

}
