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


package com.execue.publisher.absorbtion.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.AssetExtractionInput;
import com.execue.core.common.bean.publisher.DBTableDataNormalizedInfo;
import com.execue.core.common.bean.publisher.DBTableNormalizedInfo;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SelectQueryInfo;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.type.SuccessFailureType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.platform.helper.PublishedFileJDBCHelper;
import com.execue.platform.swi.IAssetExtractionService;
import com.execue.platform.swi.ISDX2KDXMappingService;
import com.execue.publisher.absorbtion.IPublisherDataAbsorbtionService;
import com.execue.publisher.exception.PublisherException;
import com.execue.publisher.exception.PublisherExceptionCodes;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDX2KDXMappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.util.querygen.IQueryGenerationUtilService;
import com.execue.util.querygen.QueryGenerationUtilServiceFactory;

/**
 * This service is for absorbing the data into datasource location
 * 
 * @author Vishay
 * @since 20/10/09
 * @version 1.0
 */
public class PublisherDataAbsorbtionServiceImpl implements IPublisherDataAbsorbtionService {

   private static final Logger               log = Logger.getLogger(PublisherDataAbsorbtionServiceImpl.class);

   private QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory;
   private ISDXRetrievalService              sdxRetrievalService;
   private IAssetExtractionService           assetExtractionService;
   private ISDX2KDXMappingService            sdx2kdxMappingService;
   private IMappingRetrievalService          mappingRetrievalService;
   private IKDXRetrievalService              kdxRetrievalService;
   private ISWIConfigurationService          swiConfigurationService;
   private PublishedFileJDBCHelper           publishedFileJDBCHelper;

   public boolean absorbPublisherData (DataSource dataSource, DBTableDataNormalizedInfo dbTableDataNormalizedInfo)
            throws PublisherException {
      boolean absorbDataSuccess = true;
      try {
         List<Integer> parameterTypes = ExecueBeanUtil.getSQLParameterTypes(dbTableDataNormalizedInfo
                  .getNormalizedColumns());
         // SQLTable sqlTable = ExecueBeanManagementUtil.prepareSQLTableFromQueryColumns(dbTableDataNormalizedInfo
         // .getDbTable().getTableName(), dbTableDataNormalizedInfo.getNormalizedColumns());

         QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(dbTableDataNormalizedInfo.getDbTable()
                  .getTableName(), null, dataSource.getOwner());
         List<QueryColumn> normalizedColumns = dbTableDataNormalizedInfo.getNormalizedColumns();

         String dmlStatement = getQueryGenerationUtilService(dataSource.getProviderType()).createInsertStatement(
                  queryTable, normalizedColumns, true);
         List<Integer> executeManipulationStatements = getPublishedFileJDBCHelper().executeDMLStatements(
                  dataSource.getName(), dmlStatement, dbTableDataNormalizedInfo.getNormalizedDataPoints(),
                  parameterTypes);
         // TODO -VG- : analyse the output and return corresponding value
      } catch (DataAccessException e) {
         absorbDataSuccess = false;
         throw new PublisherException(PublisherExceptionCodes.PUBLISHER_DATA_ABSORBTION_EXCEPTION_CODE,
                  "Failed to load data from the file. Error - " + e.getMessage());
      }
      return absorbDataSuccess;
   }

   public boolean absorbPublisherMetaData (DataSource dataSource, DBTableNormalizedInfo dbTableNormalizedInfo)
            throws PublisherException {
      boolean status = true;
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(dbTableNormalizedInfo.getDbTable()
               .getTableName(), null, dataSource.getOwner());
      List<QueryColumn> normalizedColumns = dbTableNormalizedInfo.getNormalizedColumns();

      IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(dataSource
               .getProviderType());
      String ddlStatement = queryGenerationUtilService.createTableCreateStatement(queryTable, normalizedColumns);
      if (log.isDebugEnabled()) {
         log.debug("DDL Statement : " + ddlStatement);
      }
      try {
         getPublishedFileJDBCHelper().executeDDLStatement(dataSource.getName(), ddlStatement);
      } catch (DataAccessException e) {
         status = false;
         throw new PublisherException(PublisherExceptionCodes.PUBLISHER_META_DATA_ABSORBTION_EXCEPTION_CODE,
                  "Failed to process the column names from the uploaded file. Erorr - " + e.getMessage());
      }
      return status;
   }

   public void transformData (DataSource dataSource, DBTableNormalizedInfo sourceDbTableNormalizedInfo,
            DBTableNormalizedInfo destDbTableNormalizedInfo) throws PublisherException {
      try {
         IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(dataSource
                  .getProviderType());

         // Prepare the insert statement
         // SQLTable destSQLTable = ExecueBeanManagementUtil.prepareSQLTableFromQueryColumns(destDbTableNormalizedInfo
         // .getDbTable().getTableName(), destDbTableNormalizedInfo.getNormalizedColumns());

         QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(destDbTableNormalizedInfo.getDbTable()
                  .getTableName(), null, dataSource.getOwner());
         List<QueryColumn> normalizedColumns = destDbTableNormalizedInfo.getNormalizedColumns();

         String insertStatement = queryGenerationUtilService
                  .createInsertStatement(queryTable, normalizedColumns, false);

         // Prepare the select statement
         // SQLTable sourceSQLTable =
         // ExecueBeanManagementUtil.prepareSQLTableFromQueryColumns(sourceDbTableNormalizedInfo
         // .getDbTable().getTableName(), sourceDbTableNormalizedInfo.getNormalizedColumns());
         QueryTable sourceQueryTable = ExecueBeanManagementUtil.prepareQueryTable(sourceDbTableNormalizedInfo
                  .getDbTable().getTableName(), null, dataSource.getOwner());
         List<QueryColumn> queryColumns = sourceDbTableNormalizedInfo.getNormalizedColumns();
         String selectStatement = queryGenerationUtilService.createSelectStatementWithDateHandling(sourceQueryTable,
                  queryColumns, normalizedColumns);

         // Prepare the insert into select statement
         String sqlQueryForDataTransfer = insertStatement + " " + selectStatement;
         List<Integer> parameterTypes = ExecueBeanUtil.getSQLParameterTypes(destDbTableNormalizedInfo
                  .getNormalizedColumns());
         if (log.isDebugEnabled()) {
            log.debug("SQL for transformation : " + sqlQueryForDataTransfer);
         }
         int status = getPublishedFileJDBCHelper().executeDMLStatement(dataSource.getName(), sqlQueryForDataTransfer,
                  null, parameterTypes);
      } catch (DataAccessException e) {
         throw new PublisherException(PublisherExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public SuccessFailureType absorbAsset (Asset asset, List<DBTableNormalizedInfo> dbTableNormalizedInfoList)
            throws PublisherException {
      try {
         List<Tabl> tables = new ArrayList<Tabl>();
         for (DBTableNormalizedInfo dbTableNormalizedInfo : dbTableNormalizedInfoList) {
            Tabl tabl = new Tabl();
            tabl.setName(dbTableNormalizedInfo.getDbTable().getTableName());
            tabl.setDisplayName(dbTableNormalizedInfo.getDbTable().getDisplayName());
            tabl.setLookupType(LookupType.None);
            tabl.setEligibleDefaultMetric(CheckType.YES);
            tables.add(tabl);
         }
         AssetExtractionInput assetRegistrationInput = prepareAssetRegistrationInput(asset, tables);
         return getAssetExtractionService().registerAsset(assetRegistrationInput);
      } catch (SDXException e) {
         throw new PublisherException(SWIExceptionCodes.ASSET_CREATION_FAILED, e);
      }
   }

   /**
    * This method prepares assetExtractionInput using asset and List<tables>
    * 
    * @param asset
    * @param tables
    * @return assetRegistrationInput
    */
   private AssetExtractionInput prepareAssetRegistrationInput (Asset asset, List<Tabl> tables) {
      AssetExtractionInput assetRegistrationInput = new AssetExtractionInput();
      assetRegistrationInput.setRemainingTablesAreFacts(true);
      assetRegistrationInput.setSourceAsset(asset);
      assetRegistrationInput.setTables(tables);
      return assetRegistrationInput;
   }

   public List<Long> mapSDX2KDX (Asset asset, Long modelId, List<Long> freshlyCreatedConceptDEDs,
            List<Long> freshlyCreatedInstanceBEDs) throws PublisherException {
      try {
         Model model = getKdxRetrievalService().getModelById(modelId);
         List<Tabl> tables = getSdxRetrievalService().getAllTables(asset);
         for (Tabl table : tables) {
            List<Colum> columns = getSdxRetrievalService().getColumnsOfTable(table);
            List<Long> freshCreatedConceptBEDsForTable = getSdx2kdxMappingService().mapColumnsForAssetSyncUpProcess(
                     asset, table, columns, model);
            freshlyCreatedConceptDEDs.addAll(freshCreatedConceptBEDsForTable);
            if (LookupType.SIMPLE_LOOKUP.equals(table.getLookupType())) {
               Colum lookupValueColumn = null;
               for (Colum column : columns) {
                  if (table.getLookupValueColumn().equals(column.getName())) {
                     lookupValueColumn = column;
                     break;
                  }
               }
               AssetEntityDefinition assetEntityDefinition = getSdxRetrievalService().getAssetEntityDefinitionByIds(
                        asset, table, lookupValueColumn, null);
               List<Mapping> mappingsForAED = getMappingRetrievalService().getMappingsForAED(
                        assetEntityDefinition.getId());
               BusinessEntityDefinition conceptBED = mappingsForAED.get(0).getBusinessEntityDefinition();
               List<Membr> members = getSdxRetrievalService().getColumnMembers(lookupValueColumn);
               List<Long> freshCreatedInstanceBEDsForColumn = getSdx2kdxMappingService()
                        .mapMembersForAssetSyncUpProcess(asset, table, lookupValueColumn, members, conceptBED, model);
               freshlyCreatedInstanceBEDs.addAll(freshCreatedInstanceBEDsForColumn);
            }
         }
      } catch (SDXException sdxException) {
         throw new PublisherException(PublisherExceptionCodes.PUBLISHER_DATA_ABSORBTION_EXCEPTION_CODE, sdxException);
      } catch (SDX2KDXMappingException e) {
         throw new PublisherException(PublisherExceptionCodes.PUBLISHER_DATA_ABSORBTION_EXCEPTION_CODE, e);
      } catch (MappingException e) {
         throw new PublisherException(PublisherExceptionCodes.PUBLISHER_DATA_ABSORBTION_EXCEPTION_CODE, e);
      } catch (KDXException e) {
         throw new PublisherException(PublisherExceptionCodes.PUBLISHER_DATA_ABSORBTION_EXCEPTION_CODE, e);
      }
      return freshlyCreatedConceptDEDs;
   }

   public void populateTotalRecordsCount (DataSource dataSource, DBTableNormalizedInfo evaluatedDBTableNormalizedInfo)
            throws PublisherException {
      try {
         QueryColumn queryColumn = new QueryColumn();
         queryColumn.setColumnName("*");
         String tableName = evaluatedDBTableNormalizedInfo.getDbTable().getTableName();
         QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tableName);
         IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(dataSource
                  .getProviderType());
         SelectQueryInfo countSelectStatement = queryGenerationUtilService.createStatBasedSelectStatement(queryTable,
                  queryColumn, StatType.COUNT);
         if (log.isDebugEnabled()) {
            log.debug("Count Select Statement : " + countSelectStatement.getSelectQuery());
         }
         ExeCueResultSet resultSet = getPublishedFileJDBCHelper().executeSQLQuery(dataSource.getName(),
                  countSelectStatement);
         int totalRecordCount = 0;
         while (resultSet.next()) {
            totalRecordCount = resultSet.getInt(0);
         }
         evaluatedDBTableNormalizedInfo.setTotalRecordsCount(Integer.valueOf(totalRecordCount).longValue());
      } catch (Exception e) {
         throw new PublisherException(PublisherExceptionCodes.PUBLISHER_DATA_ABSORBTION_EXCEPTION_CODE, e);
      }
   }

   private IQueryGenerationUtilService getQueryGenerationUtilService (AssetProviderType assetProviderType) {
      return getQueryGenerationUtilServiceFactory().getQueryGenerationUtilService(assetProviderType);
   }

   public QueryGenerationUtilServiceFactory getQueryGenerationUtilServiceFactory () {
      return queryGenerationUtilServiceFactory;
   }

   public void setQueryGenerationUtilServiceFactory (QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory) {
      this.queryGenerationUtilServiceFactory = queryGenerationUtilServiceFactory;
   }

   public IAssetExtractionService getAssetExtractionService () {
      return assetExtractionService;
   }

   public void setAssetExtractionService (IAssetExtractionService assetExtractionService) {
      this.assetExtractionService = assetExtractionService;
   }

   public ISDX2KDXMappingService getSdx2kdxMappingService () {
      return sdx2kdxMappingService;
   }

   public void setSdx2kdxMappingService (ISDX2KDXMappingService sdx2kdxMappingService) {
      this.sdx2kdxMappingService = sdx2kdxMappingService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   /**
    * @return the publishedFileJDBCHelper
    */
   public PublishedFileJDBCHelper getPublishedFileJDBCHelper () {
      return publishedFileJDBCHelper;
   }

   /**
    * @param publishedFileJDBCHelper
    *           the publishedFileJDBCHelper to set
    */
   public void setPublishedFileJDBCHelper (PublishedFileJDBCHelper publishedFileJDBCHelper) {
      this.publishedFileJDBCHelper = publishedFileJDBCHelper;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

}
