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


package com.execue.publisher.controller.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.DateFormat;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileTableDetails;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.bean.publisher.DBTable;
import com.execue.core.common.bean.publisher.DBTableColumn;
import com.execue.core.common.bean.publisher.DBTableInfo;
import com.execue.core.common.bean.publisher.DBTableNormalizedInfo;
import com.execue.core.common.bean.publisher.PublisherDataEvaluationContext;
import com.execue.core.common.bean.publisher.QueryColumnDetail;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.SQLIndex;
import com.execue.core.common.bean.swi.PublishAssetContext;
import com.execue.core.common.type.AssetOwnerType;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.common.type.PublishedFileType;
import com.execue.core.common.type.PublisherProcessType;
import com.execue.core.common.type.SuccessFailureType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.platform.IBusinessModelPreparationService;
import com.execue.platform.IVirtualTableManagementService;
import com.execue.platform.helper.PublishedFileJDBCHelper;
import com.execue.platform.swi.IAssetSourcePublisherMergeService;
import com.execue.platform.swi.IKDXDataTypePopulationService;
import com.execue.platform.swi.IPublishService;
import com.execue.publisher.absorbtion.IPublisherDataAbsorbtionService;
import com.execue.publisher.configuration.IPublisherConfigurationService;
import com.execue.publisher.controller.IPublisherDataEvaluationControllerService;
import com.execue.publisher.evaluate.IPublisherDataEvaluationService;
import com.execue.publisher.exception.PublisherException;
import com.execue.publisher.exception.PublisherExceptionCodes;
import com.execue.publisher.util.PublisherUtilityHelper;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.qdata.service.IUserNotificationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.PublishedFileException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IConversionService;
import com.execue.swi.service.IDefaultMetricService;
import com.execue.swi.service.IPublishedFileManagementService;
import com.execue.swi.service.IPublishedFileRetrievalService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.util.querygen.IQueryGenerationUtilService;
import com.execue.util.querygen.QueryGenerationUtilServiceFactory;

public class PublisherDataEvaluationControllerServiceImpl implements IPublisherDataEvaluationControllerService {

   private IPublisherDataAbsorbtionService   publisherDataAbsorbtionService;
   private IPublisherConfigurationService    publisherConfigurationService;
   private IPublisherDataEvaluationService   publisherDataEvaluationService;
   private IJobDataService                   jobDataService;
   private IPublishedFileRetrievalService    publishedFileRetrievalService;
   private IPublishedFileManagementService   publishedFileManagementService;
   private IUserNotificationService          userNotificationService;

   private IConversionService                conversionService;
   private IAssetSourcePublisherMergeService assetSourcePublisherMergeService;
   private IVirtualTableManagementService    virtualTableManagementService;
   private IDefaultMetricService             defaultMetricService;
   private PublishedFileJDBCHelper           publishedFileJDBCHelper;
   private IPublishService                   publishService;
   private IBusinessModelPreparationService  businessModelPreparationService;
   private QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory;
   private ICoreConfigurationService         coreConfigurationService;
   private ISDXManagementService             sdxManagementService;
   private ISDXRetrievalService              sdxRetrievalService;
   private IKDXDataTypePopulationService     kdxDataTypePopulationService;
   private IApplicationRetrievalService      applicationRetrievalService;

   private static final Logger               logger = Logger
                                                             .getLogger(PublisherDataEvaluationControllerServiceImpl.class);

   public boolean publishedDataEvaluation (PublisherDataEvaluationContext publisherDataEvaluationContext)
            throws PublisherException {
      boolean status = true;
      String errorMessage = "";
      DataSource targetDataSource = null;
      JobOperationalStatus jobOperationalStatus = null;
      List<DBTableNormalizedInfo> evaluatedDBTableNormalizedInfoList = new ArrayList<DBTableNormalizedInfo>();
      JobRequest jobRequest = publisherDataEvaluationContext.getJobRequest();
      try {
         PublishedFileInfo publishedFileInfo = getPublishedFileRetrievalService().getPublishedFileInfoById(
                  publisherDataEvaluationContext.getFileInfoId());

         correctEvaludatedColumnNameByDisplayName(publishedFileInfo.getId());

         // Get the target Data Source
         targetDataSource = getSdxRetrievalService().getDataSourceById(publishedFileInfo.getDatasourceId());

         // Get the base information of columns.
         List<DBTableInfo> dbTableInfoList = getBaseTableInfoByFileId(publisherDataEvaluationContext.getFileInfoId());
         List<DBTableNormalizedInfo> dbTableNormalizedInfoList = new ArrayList<DBTableNormalizedInfo>();
         for (DBTableInfo dbTableInfo : dbTableInfoList) {
            dbTableNormalizedInfoList.add(PublisherUtilityHelper.normalizeDBTableInfo(dbTableInfo));
         }

         // Get the evaluated information of columns.
         evaluatedDBTableNormalizedInfoList = getNormalizedTableInfoByFileId(publisherDataEvaluationContext
                  .getFileInfoId(), targetDataSource.getProviderType());

         List<DBTableNormalizedInfo> evaluatedNormalizedDBTableInfoList = new ArrayList<DBTableNormalizedInfo>();

         if (ExecueCoreUtil.isCollectionNotEmpty(dbTableNormalizedInfoList)) {
            int index = 0;
            for (DBTableNormalizedInfo dbTableNormalizedInfo : dbTableNormalizedInfoList) {

               jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                        "Absorbing the evaluated data", JobStatus.INPROGRESS, null, new Date());
               getJobDataService().createJobOperationStatus(jobOperationalStatus);

               DBTableNormalizedInfo evaluatedDBTableNormalizedInfo = evaluatedDBTableNormalizedInfoList.get(index);

               absorbPublisherMetaData(evaluatedDBTableNormalizedInfo, dbTableNormalizedInfo, targetDataSource);

               jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                        JobStatus.SUCCESS, null, new Date());
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);

               // virtual lookup evaluation is not required if asset creation is not required because
               // we will loose the analysis done
               if (publisherDataEvaluationContext.isDatasetCollectionCreation()) {
                  jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                           "Evaluating for possible lookups", JobStatus.INPROGRESS, null, new Date());

                  getJobDataService().createJobOperationStatus(jobOperationalStatus);

                  List<QueryColumn> virtualLookupColumns = getPublisherDataEvaluationService().evaluateVirtualLooks(
                           evaluatedDBTableNormalizedInfo, targetDataSource);

                  evaluatedDBTableNormalizedInfo.setVirtualLookupColumns(virtualLookupColumns);

                  jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                           JobStatus.SUCCESS, null, new Date());

                  getJobDataService().updateJobOperationStatus(jobOperationalStatus);
               }
               evaluatedNormalizedDBTableInfoList.add(evaluatedDBTableNormalizedInfo);
               index++;
            }

            publishedFileInfo.setFileAbsorbed(CheckType.YES);

            getPublishedFileManagementService().updatePublishedFileInfo(publishedFileInfo);

            if (!publisherDataEvaluationContext.isDatasetCollectionCreation()) {
               dropTemporaryResources(dbTableNormalizedInfoList, jobRequest, targetDataSource);
               return status;
            }

            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Absorbing the Dataset", JobStatus.INPROGRESS, null, new Date());
            getJobDataService().createJobOperationStatus(jobOperationalStatus);

            // Absorb the asset
            Asset asset = absorbAsset(publisherDataEvaluationContext.getAsset(), publisherDataEvaluationContext
                     .getApplicationId(), targetDataSource, evaluatedDBTableNormalizedInfoList, publishedFileInfo
                     .getSourceType());

            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);

            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Analyzing the Dataset", JobStatus.INPROGRESS, null, new Date());
            getJobDataService().createJobOperationStatus(jobOperationalStatus);

            // Set the KDX data types for the columns where in user defined them and create virtual lookup
            modifyAssetTablesForUserChoice(asset, evaluatedDBTableNormalizedInfoList, publisherDataEvaluationContext
                     .getUserId());

            // Analyze the KDX Data TYpe for the columns which are not mentioned by User
            getKdxDataTypePopulationService().analyseKDXDataType(asset.getId());

            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
            getJobDataService().updateJobOperationStatus(jobOperationalStatus);

            // Update the file table info for the evaluated/update KDX types at asset
            /*
             * Update the file table info for the evaluated/update KDX types at asset. Though this step looks fine,
             * might not hold as the asset could be changed later on for the KDX data type which will not be captured
             * back to file info tables. So not required to perform this step. Hence commented. -RG-
             */
            // updateKDXForPublishedFileTableDetails(publishedFileTableDetails, asset);
            if (PublisherProcessType.SIMPLIFIED_PUBLISHER_PROCESS.equals(publisherDataEvaluationContext
                     .getPublisherProcessType())
                     && CheckType.YES.equals(publishedFileInfo.getDatasetCollectionCreation())) {
               // update published file info with model and application id.
               List<Long> freshlyCreatedConceptBEDs = new ArrayList<Long>();
               List<Long> freshlyCreatedInstanceBEDs = new ArrayList<Long>();
               // mapping start
               jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                        "Creating mappings", JobStatus.INPROGRESS, null, new Date());
               getJobDataService().createJobOperationStatus(jobOperationalStatus);
               getPublisherDataAbsorbtionService().mapSDX2KDX(asset, publishedFileInfo.getModelId(),
                        freshlyCreatedConceptBEDs, freshlyCreatedInstanceBEDs);

               jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                        JobStatus.SUCCESS, null, new Date());
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
               // mapping end

               // Prepare business model start

               // not required to get freshly created conceptBEDs and freshly created instance beds
               jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                        "Preparing business model and publishing dataset collection", JobStatus.INPROGRESS, null,
                        new Date());
               getJobDataService().createJobOperationStatus(jobOperationalStatus);

               PublishAssetContext publishAssetContext = PublisherUtilityHelper.populatePublishAssetContext(
                        publishedFileInfo, asset, jobRequest, PublishAssetMode.LOCAL);
               getPublishService().publishAppHierarchy(publishAssetContext);

               jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                        JobStatus.SUCCESS, null, new Date());
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
               // publish dataset collection end

            }

            // creating indexes on the source on dimension and id columns.
            int maxDbObjectLength = getCoreConfigurationService().getMaxDBObjectLength();
            for (DBTableNormalizedInfo evaluatedDBTableNormalizedInfo : evaluatedDBTableNormalizedInfoList) {
               List<SQLIndex> indexes = new ArrayList<SQLIndex>();
               Tabl factTable = getSdxRetrievalService().getAssetTable(asset.getId(),
                        evaluatedDBTableNormalizedInfo.getDbTable().getTableName());
               List<Colum> columns = getSdxRetrievalService().getColumnsOfTable(factTable);
               for (Colum colum : columns) {
                  if (ColumnType.DIMENSION.equals(colum.getKdxDataType())
                           || ColumnType.ID.equals(colum.getKdxDataType())) {
                     indexes.add(ExecueBeanManagementUtil.prepareSQLIndex(factTable.getName(), colum.getName()));
                  }
               }
               getPublishedFileJDBCHelper().createMultipleIndexesOnTable(asset.getDataSource(), indexes,
                        maxDbObjectLength);
            }
            dropTemporaryResources(dbTableNormalizedInfoList, jobRequest, targetDataSource);
         }
      } catch (SWIException swiException) {
         status = false;
         errorMessage = swiException.getMessage();
         throw new PublisherException(swiException.getCode(), swiException);
      } catch (Exception e) {
         e.printStackTrace();
         status = false;
         errorMessage = e.getMessage();
         throw new PublisherException(PublisherExceptionCodes.PUBLISHER_DEFAULT_EXCEPTION_CODE, e);
      } finally {
         if (!status) {
            // clean the created table in case of failure if any.
            if (ExecueCoreUtil.isCollectionNotEmpty(evaluatedDBTableNormalizedInfoList) && targetDataSource != null) {
               for (DBTableNormalizedInfo dbTableNormalizedInfo : evaluatedDBTableNormalizedInfoList) {
                  String tableName = dbTableNormalizedInfo.getDbTable().getTableName();
                  try {
                     getPublishedFileJDBCHelper().dropTable(targetDataSource, tableName);
                  } catch (DataAccessException e) {
                     throw new PublisherException(e.getCode(), e);
                  }
               }
            }
            if (jobOperationalStatus != null) {
               ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE,
                        errorMessage, new Date());
               try {
                  getJobDataService().updateJobOperationStatus(jobOperationalStatus);
               } catch (QueryDataException queryDataException) {
                  throw new PublisherException(queryDataException.getCode(), queryDataException);
               }
            }
         }
      }
      return status;
   }

   private void dropTemporaryResources (List<DBTableNormalizedInfo> dbTableNormalizedInfoList, JobRequest jobRequest,
            DataSource targetDataSource) throws QueryDataException, PublisherException, DataAccessException {
      JobOperationalStatus jobOperationalStatus = null;
      for (DBTableNormalizedInfo dbTableNormalizedInfo : dbTableNormalizedInfoList) {
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Cleaning the temporary resources", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);

         if (publisherConfigurationService.isDropTempTable()) {
            getPublishedFileJDBCHelper().dropTable(targetDataSource, dbTableNormalizedInfo.getDbTable().getTableName());
         }
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
      }
   }

   private Asset absorbAsset (Asset asset, Long applicationId, DataSource dataSource,
            List<DBTableNormalizedInfo> dbTableNormalizedInfoList, PublishedFileType sourceType)
            throws PublisherException, SDXException, KDXException {
      // absorb the asset in swi
      logger.debug("Absorbing an asset in swi");
      // TODO : -VG- check for name validity, if same, then append the random number to it.
      asset.setName(ExecueStringUtil.getNormalizedName(asset.getDisplayName()));
      asset.setStatus(StatusEnum.INACTIVE);
      asset.setDataSource(dataSource);
      asset.setType(AssetType.Relational);
      asset.setOwnerType(AssetOwnerType.ExeCue);
      asset.setPriority(1d);
      asset.setOriginType(sourceType);
      Application application = getApplicationRetrievalService().getApplicationById(applicationId);
      asset.setApplication(application);
      SuccessFailureType assetAbsorbtionStatus = getPublisherDataAbsorbtionService().absorbAsset(asset,
               dbTableNormalizedInfoList);
      logger.debug("Asset absorbtion status " + assetAbsorbtionStatus);
      return asset;
   }

   private void modifyAssetTablesForUserChoice (Asset asset,
            List<DBTableNormalizedInfo> evaluatedDBTableNormalizedInfoList, Long userId) throws SWIException {
      for (DBTableNormalizedInfo evaluatedDBTableNormalizedInfo : evaluatedDBTableNormalizedInfoList) {
         Tabl factTable = getSdxRetrievalService().getAssetTable(asset.getId(),
                  evaluatedDBTableNormalizedInfo.getDbTable().getTableName());
         List<Colum> columns = getSdxRetrievalService().getColumnsOfTable(factTable);
         assetSourcePublisherMergeService.mergeAssetPublisherColumns(asset, factTable, columns, userId);
         defaultMetricService.handleAssetDefaultMetrics(asset, factTable, columns, false);
         for (QueryColumn virtualQueryColumn : evaluatedDBTableNormalizedInfo.getVirtualLookupColumns()) {
            Tabl virtualTable = new Tabl();
            virtualTable.setName(virtualQueryColumn.getColumnName());
            virtualTable.setDisplayName(virtualQueryColumn.getColumnName());
            virtualTable.setDescription(virtualQueryColumn.getColumnName());
            virtualTable.setLookupType(LookupType.SIMPLE_LOOKUP);
            virtualTable.setLookupValueColumn(virtualQueryColumn.getColumnName());
            virtualTable.setLookupDescColumn(virtualQueryColumn.getColumnName());
            TableInfo virtualTableInfo = virtualTableManagementService.prepareVirtualTableInfo(asset, factTable,
                     virtualTable);
            virtualTableManagementService.createVirtualTableInfo(asset, virtualTableInfo);
         }
      }
   }

   private void absorbPublisherMetaData (DBTableNormalizedInfo evaluatedDBTableNormalizedInfo, DBTableInfo dbTableInfo,
            DataSource dataSource) throws PublisherException {
      try {
         DBTableNormalizedInfo orginalDbTableNormalizedInfo = PublisherUtilityHelper.normalizeDBTableInfo(dbTableInfo);
         int index = 0;
         for (QueryColumn queryColumn : evaluatedDBTableNormalizedInfo.getNormalizedColumns()) {
            // if column data type is date, two possibilities are there
            // if format associated is plain, then we need to store it as integer column
            // if format is complex, we need to attach the format in the source select query
            // so that data transfer happens properly
            if (DataType.DATE.equals(queryColumn.getDataType()) || DataType.DATETIME.equals(queryColumn.getDataType())) {
               DateFormat dateFormat = conversionService.getSupportedDateFormat(queryColumn.getDataFormat(), dataSource
                        .getProviderType());
               findCorrespondingColumnFromSource(orginalDbTableNormalizedInfo.getNormalizedColumns(), queryColumn,
                        dateFormat);
            }
            index++;
         }
         if (getPublisherDataAbsorbtionService().absorbPublisherMetaData(dataSource, evaluatedDBTableNormalizedInfo)) {
            getPublisherDataAbsorbtionService().transformData(dataSource, orginalDbTableNormalizedInfo,
                     evaluatedDBTableNormalizedInfo);
            getPublisherDataAbsorbtionService().populateTotalRecordsCount(dataSource, evaluatedDBTableNormalizedInfo);
         }
      } catch (SWIException sException) {
         throw new PublisherException(sException.getCode(), sException.getMessage());
      }
   }

   private void findCorrespondingColumnFromSource (List<QueryColumn> sourceQueryColumns,
            QueryColumn toBeMatchedQueryColumn, DateFormat dateFormat) {
      for (QueryColumn queryColumn : sourceQueryColumns) {
         if (toBeMatchedQueryColumn.getColumnName().equals(queryColumn.getColumnName())) {
            queryColumn.setDataType(toBeMatchedQueryColumn.getDataType());
            // need to use db specific format for sql query
            queryColumn.setDataFormat(dateFormat.getDbFormat());
            break;
         }
      }
   }

   private List<DBTableNormalizedInfo> getNormalizedTableInfoByFileId (Long fileId, AssetProviderType assetProviderType)
            throws PublishedFileException, SWIException {
      List<DBTableNormalizedInfo> evaluatedTableColumnList = new ArrayList<DBTableNormalizedInfo>();
      List<PublishedFileTableInfo> publishedFileTableList = getPublishedFileRetrievalService()
               .getPublishedFileTableInfoByFileId(fileId);
      List<PublishedFileTableDetails> publishedFileTableDetails = new ArrayList<PublishedFileTableDetails>();
      for (PublishedFileTableInfo publishedFileTableInfo : publishedFileTableList) {
         publishedFileTableDetails = getPublishedFileRetrievalService().getPublishedFileTableDetailsByTableId(
                  publishedFileTableInfo.getId());
         DBTableNormalizedInfo evaluatedTableColumn = new DBTableNormalizedInfo();
         DBTable dbTable = new DBTable();

         // TODO: -RG- We need to check for the existence of the table, if already exists then create an other name and
         // then set it
         dbTable.setTableName(publishedFileTableInfo.getEvaluatedTableName());
         dbTable.setDisplayName(publishedFileTableInfo.getDisplayTableName());
         List<QueryColumn> normalizedColumns = new ArrayList<QueryColumn>();
         List<QueryColumnDetail> normalizedColumnDetails = new ArrayList<QueryColumnDetail>();
         for (PublishedFileTableDetails fileTableDetail : publishedFileTableDetails) {
            normalizedColumns.add(getQueryColumnForPublishedFileTableColumn(fileTableDetail, assetProviderType));
            normalizedColumnDetails.add(PublisherUtilityHelper
                     .getQueryColumnDetailForPublishedFileTableColumn(fileTableDetail));
         }
         evaluatedTableColumn.setDbTable(dbTable);
         evaluatedTableColumn.setNormalizedColumns(normalizedColumns);
         evaluatedTableColumn.setNormalizedColumnDetails(normalizedColumnDetails);
         evaluatedTableColumnList.add(evaluatedTableColumn);
      }
      return evaluatedTableColumnList;
   }

   public QueryColumn getQueryColumnForPublishedFileTableColumn (PublishedFileTableDetails publishedFileTableDetails,
            AssetProviderType assetProviderType) throws SWIException {
      QueryColumn queryColumn = new QueryColumn();
      queryColumn.setColumnName(publishedFileTableDetails.getEvaluatedColumnName());
      queryColumn.setDataType(publishedFileTableDetails.getEvaluatedDataType());
      queryColumn.setPrecision(publishedFileTableDetails.getEvaluatedPrecision());
      queryColumn.setScale(publishedFileTableDetails.getEvaluatedScale());
      queryColumn.setDataFormat(publishedFileTableDetails.getFormat());
      queryColumn.setUnitType(publishedFileTableDetails.getUnitType());
      queryColumn.setGranularity(publishedFileTableDetails.getGranularity());
      return queryColumn;
   }

   private List<DBTableInfo> getBaseTableInfoByFileId (Long fileId) throws PublishedFileException {

      List<DBTableInfo> baseTableColumnList = new ArrayList<DBTableInfo>();
      List<PublishedFileTableInfo> publishedFileTableList = getPublishedFileRetrievalService()
               .getPublishedFileTableInfoByFileId(fileId);
      List<PublishedFileTableDetails> publishedFileTableDetails = new ArrayList<PublishedFileTableDetails>();
      for (PublishedFileTableInfo publishedFileTableInfo : publishedFileTableList) {
         publishedFileTableDetails = getPublishedFileRetrievalService().getPublishedFileTableDetailsByTableId(
                  publishedFileTableInfo.getId());
         DBTableInfo baseTableColumnInfo = new DBTableInfo();
         DBTable dbTable = new DBTable();
         dbTable.setTableName(publishedFileTableInfo.getTempTableName());
         dbTable.setDisplayName(publishedFileTableInfo.getDisplayTableName());
         List<DBTableColumn> baseColumns = new ArrayList<DBTableColumn>();
         for (PublishedFileTableDetails fileTableDetail : publishedFileTableDetails) {
            baseColumns.add(PublisherUtilityHelper.getDBTableColumnForPublishedFileTableColumn(fileTableDetail));
         }
         baseTableColumnInfo.setDbTable(dbTable);
         baseTableColumnInfo.setDbTableColumns(baseColumns);
         baseTableColumnList.add(baseTableColumnInfo);
      }
      return baseTableColumnList;
   }

   private void correctEvaludatedColumnNameByDisplayName (Long publishedFileInfoId) throws PublishedFileException {

      List<PublishedFileTableInfo> pubFileTableInfoList = getPublishedFileRetrievalService()
               .getFileTableInfoListByFileInfoId(publishedFileInfoId);

      List<PublishedFileTableDetails> pubFileTableDetails = null;
      String tempColumnName = null;
      Set<String> tempColumnNames = null;
      int counter = 0;
      String normalizationRegex = getPublisherConfigurationService().getEscapeSpecialCharactersRegEx();
      for (PublishedFileTableInfo publishedFileTableInfo : pubFileTableInfoList) {

         pubFileTableDetails = getPublishedFileRetrievalService().getPublishedFileTableDetailsByTableId(
                  publishedFileTableInfo.getId());
         counter = 0;
         tempColumnNames = new HashSet<String>();
         for (PublishedFileTableDetails publishedFileTableDetails : pubFileTableDetails) {
            tempColumnName = publishedFileTableDetails.getEvaluatedColumnDisplayName();
            tempColumnName = PublisherUtilityHelper.normalizeName(tempColumnName, normalizationRegex);
            if (tempColumnNames.contains(tempColumnName)) {
               tempColumnName = tempColumnName + "" + counter;
            }
            tempColumnNames.add(tempColumnName);
            publishedFileTableDetails.setEvaluatedColumnName(tempColumnName);
            counter++;
         }
         getPublishedFileManagementService().updatePublishedFileTableDetails(pubFileTableDetails);
      }
   }

   public IPublisherDataAbsorbtionService getPublisherDataAbsorbtionService () {
      return publisherDataAbsorbtionService;
   }

   public void setPublisherDataAbsorbtionService (IPublisherDataAbsorbtionService publisherDataAbsorbtionService) {
      this.publisherDataAbsorbtionService = publisherDataAbsorbtionService;
   }

   public IPublisherDataEvaluationService getPublisherDataEvaluationService () {
      return publisherDataEvaluationService;
   }

   public void setPublisherDataEvaluationService (IPublisherDataEvaluationService publisherDataEvaluationService) {
      this.publisherDataEvaluationService = publisherDataEvaluationService;
   }

   public IPublisherConfigurationService getPublisherConfigurationService () {
      return publisherConfigurationService;
   }

   public void setPublisherConfigurationService (IPublisherConfigurationService publisherConfigurationService) {
      this.publisherConfigurationService = publisherConfigurationService;
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public IUserNotificationService getUserNotificationService () {
      return userNotificationService;
   }

   public void setUserNotificationService (IUserNotificationService userNotificationService) {
      this.userNotificationService = userNotificationService;
   }

   public IConversionService getConversionService () {
      return conversionService;
   }

   public void setConversionService (IConversionService conversionService) {
      this.conversionService = conversionService;
   }

   public IAssetSourcePublisherMergeService getAssetSourcePublisherMergeService () {
      return assetSourcePublisherMergeService;
   }

   public void setAssetSourcePublisherMergeService (IAssetSourcePublisherMergeService assetSourcePublisherMergeService) {
      this.assetSourcePublisherMergeService = assetSourcePublisherMergeService;
   }

   public IDefaultMetricService getDefaultMetricService () {
      return defaultMetricService;
   }

   public void setDefaultMetricService (IDefaultMetricService defaultMetricService) {
      this.defaultMetricService = defaultMetricService;
   }

   public IVirtualTableManagementService getVirtualTableManagementService () {
      return virtualTableManagementService;
   }

   public void setVirtualTableManagementService (IVirtualTableManagementService virtualTableManagementService) {
      this.virtualTableManagementService = virtualTableManagementService;
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

   public IPublishService getPublishService () {
      return publishService;
   }

   public void setPublishService (IPublishService publishService) {
      this.publishService = publishService;
   }

   public IBusinessModelPreparationService getBusinessModelPreparationService () {
      return businessModelPreparationService;
   }

   public void setBusinessModelPreparationService (IBusinessModelPreparationService businessModelPreparationService) {
      this.businessModelPreparationService = businessModelPreparationService;
   }

   public IQueryGenerationUtilService getQueryGenerationUtilService (AssetProviderType assetProviderType) {
      return getQueryGenerationUtilServiceFactory().getQueryGenerationUtilService(assetProviderType);
   }

   public QueryGenerationUtilServiceFactory getQueryGenerationUtilServiceFactory () {
      return queryGenerationUtilServiceFactory;
   }

   public void setQueryGenerationUtilServiceFactory (QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory) {
      this.queryGenerationUtilServiceFactory = queryGenerationUtilServiceFactory;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
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

   public IPublishedFileRetrievalService getPublishedFileRetrievalService () {
      return publishedFileRetrievalService;
   }

   public void setPublishedFileRetrievalService (IPublishedFileRetrievalService publishedFileRetrievalService) {
      this.publishedFileRetrievalService = publishedFileRetrievalService;
   }

   public IPublishedFileManagementService getPublishedFileManagementService () {
      return publishedFileManagementService;
   }

   public void setPublishedFileManagementService (IPublishedFileManagementService publishedFileManagementService) {
      this.publishedFileManagementService = publishedFileManagementService;
   }

   public IKDXDataTypePopulationService getKdxDataTypePopulationService () {
      return kdxDataTypePopulationService;
   }

   public void setKdxDataTypePopulationService (IKDXDataTypePopulationService kdxDataTypePopulationService) {
      this.kdxDataTypePopulationService = kdxDataTypePopulationService;
   }

   /**
    * @return the applicationRetrievalService
    */
   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   /**
    * @param applicationRetrievalService the applicationRetrievalService to set
    */
   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

}
