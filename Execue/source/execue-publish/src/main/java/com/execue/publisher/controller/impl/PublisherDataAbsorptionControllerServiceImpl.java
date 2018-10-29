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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.csvreader.CsvReader;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileTableDetails;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.core.common.bean.publisher.DBTable;
import com.execue.core.common.bean.publisher.DBTableColumn;
import com.execue.core.common.bean.publisher.DBTableDataNormalizedInfo;
import com.execue.core.common.bean.publisher.DBTableInfo;
import com.execue.core.common.bean.publisher.DBTableNormalizedInfo;
import com.execue.core.common.bean.publisher.PublisherBatchDataReadContext;
import com.execue.core.common.bean.publisher.PublisherDataAbsorptionContext;
import com.execue.core.common.bean.publisher.PublisherMetaReadContext;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.type.CSVEmptyField;
import com.execue.core.common.type.CSVStringEnclosedCharacterType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.publisher.absorbtion.IPublisherDataAbsorbtionService;
import com.execue.publisher.configuration.IPublisherConfigurationService;
import com.execue.publisher.controller.IPublisherDataAbsorptionControllerService;
import com.execue.publisher.evaluate.IPublisherDataEvaluationService;
import com.execue.publisher.exception.PublisherException;
import com.execue.publisher.exception.PublisherExceptionCodes;
import com.execue.publisher.upload.IPublisherDataUploadService;
import com.execue.publisher.util.PublisherUtilityHelper;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.exception.PublishedFileException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IDataSourceSelectionService;
import com.execue.swi.service.IPublishedFileManagementService;
import com.execue.swi.service.IPublishedFileRetrievalService;

public class PublisherDataAbsorptionControllerServiceImpl implements IPublisherDataAbsorptionControllerService {

   private IPublisherDataAbsorbtionService publisherDataAbsorbtionService;
   private IPublisherDataUploadService     publisherDataUploadService;

   private IPublisherConfigurationService  publisherConfigurationService;
   private IPublisherDataEvaluationService publisherDataEvaluationService;
   private IJobDataService                 jobDataService;
   private IPublishedFileManagementService publishedFileManagementService;
   private IPublishedFileRetrievalService  publishedFileRetrievalService;
   private ICoreConfigurationService       coreConfigurationService;
   private IDataSourceSelectionService     dataSourceSelectionService;

   private static final Logger             logger = Logger
                                                           .getLogger(PublisherDataAbsorptionControllerServiceImpl.class);

   public boolean publishData (PublisherDataAbsorptionContext publisherDataAbsorptionContext) throws PublisherException {
      boolean status = true;
      String errorMessage = "";
      PublisherMetaReadContext publisherMetaReadContext = null;
      PublisherBatchDataReadContext publisherBatchDataReadContext = null;
      JobOperationalStatus jobOperationalStatus = null;
      JobRequest jobRequest = publisherDataAbsorptionContext.getJobRequest();
      try {
         // Get the target Data Source
         DataSource targetDataSource = getDataSourceSelectionService().getDataSourceForUploadedDatasets(
                  publisherDataAbsorptionContext.getUserId());

         logger.debug("published fileInfo Id :" + publisherDataAbsorptionContext.getFileInfoId());

         PublishedFileInfo publishedFileInfo = getPublishedFileRetrievalService().getPublishedFileInfoById(
                  publisherDataAbsorptionContext.getFileInfoId());

         publishedFileInfo.setDatasourceId(targetDataSource.getId());
         getPublishedFileManagementService().updatePublishedFileInfo(publishedFileInfo);

         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Collecting the file meta data", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);

         publisherMetaReadContext = populatePublisherMetaContext(publisherDataAbsorptionContext.getApplicationId(),
                  publisherDataAbsorptionContext.getUserId(), publisherDataAbsorptionContext.isColumnsAvailable(),
                  publisherDataAbsorptionContext.getFilePath(), publisherDataAbsorptionContext.getDataDelimeter(),
                  publisherDataAbsorptionContext.getOriginalFileName(), publishedFileInfo.getFileName());

         List<DBTableInfo> dbTablesList = getPublisherDataUploadService().populateDBTables(publisherMetaReadContext);

         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);

         if (ExecueCoreUtil.isCollectionNotEmpty(dbTablesList)) {
            for (DBTableInfo dbTableInfo : dbTablesList) {
               DBTableNormalizedInfo dbTableNormalizedInfo = PublisherUtilityHelper.normalizeDBTableInfo(dbTableInfo);

               // Persist the file table info (temp table name, evaluated table name and work sheet)
               PublishedFileTableInfo publishedFileTableInfo = persistPublishedTableInfo(publishedFileInfo, dbTableInfo
                        .getDbTable());
               // Persist the file table details with the original columns info
               List<PublishedFileTableDetails> publishedFileTableDetailsList = persistPublishedTableDetails(
                        publishedFileTableInfo, dbTableInfo);

               publishedFileTableInfo.setPublishedFileTableDetails(new HashSet<PublishedFileTableDetails>(
                        publishedFileTableDetailsList));

               // absorb the meta of the table. if success then absorb the data in batches
               jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                        "Absorbing the file meta data", JobStatus.INPROGRESS, null, new Date());
               getJobDataService().createJobOperationStatus(jobOperationalStatus);

               if (publisherDataAbsorbtionService.absorbPublisherMetaData(targetDataSource, dbTableNormalizedInfo)) {
                  jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                           JobStatus.SUCCESS, null, new Date());
                  getJobDataService().updateJobOperationStatus(jobOperationalStatus);

                  int startingLineNumber = 0;
                  if (publisherDataAbsorptionContext.isColumnsAvailable()) {
                     startingLineNumber = 2;
                  }
                  publisherBatchDataReadContext = populateBatchDataReadContext(publisherDataAbsorptionContext
                           .getFilePath(), startingLineNumber, dbTableInfo.getDbTableColumns().size(),
                           publisherDataAbsorptionContext.getDataDelimeter(), publisherDataAbsorptionContext
                                    .getCsvEmptyField(), publisherDataAbsorptionContext
                                    .getCsvStringEnclosedCharacterType());

                  jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                           "Absorbing the file data", JobStatus.INPROGRESS, null, new Date());
                  getJobDataService().createJobOperationStatus(jobOperationalStatus);

                  List<List<Object>> dbTableDataBatch = null;
                  boolean dataBatchAbsorbtionSuccess = true;
                  do {
                     dbTableDataBatch = publisherDataUploadService
                              .populateDBTableDataBatch(publisherBatchDataReadContext);
                     logger.debug("Got Data batch of size " + dbTableDataBatch.size());
                     if (ExecueCoreUtil.isCollectionEmpty(dbTableDataBatch)) {
                        break;
                     }
                     DBTableDataNormalizedInfo dbTableDataNormalizedInfo = PublisherUtilityHelper
                              .populateDBTableDataNormalizedInfo(dbTableNormalizedInfo, dbTableDataBatch);
                     dataBatchAbsorbtionSuccess = getPublisherDataAbsorbtionService().absorbPublisherData(
                              targetDataSource, dbTableDataNormalizedInfo);
                     logger.debug("Successfully absorbed the current data batch");

                     // making the starting line 0 means read from current position of buffered reader now.
                     publisherBatchDataReadContext.setStartingLine(0);
                  } while (dataBatchAbsorbtionSuccess);

                  jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                           JobStatus.SUCCESS, null, new Date());
                  getJobDataService().updateJobOperationStatus(jobOperationalStatus);

                  jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                           "Evaluating the data for types", JobStatus.INPROGRESS, null, new Date());
                  getJobDataService().createJobOperationStatus(jobOperationalStatus);

                  DBTableNormalizedInfo evaluatedDBTableNormalizedInfo = evaluatePublisherMetaData(dbTableInfo,
                           targetDataSource, publisherMetaReadContext);

                  // update the file table details for the evaluated table column names and data types
                  updatePublishedTableInfo(publishedFileInfo, publishedFileTableInfo, evaluatedDBTableNormalizedInfo
                           .getDbTable());
                  updatePublishedTableDetails(publishedFileTableDetailsList, evaluatedDBTableNormalizedInfo);

                  jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                           JobStatus.SUCCESS, null, new Date());
                  getJobDataService().updateJobOperationStatus(jobOperationalStatus);

               } else {
                  jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                           JobStatus.FAILURE, "File meta data absorption failed", new Date());
                  getJobDataService().updateJobOperationStatus(jobOperationalStatus);
               }
            }

         }

      } catch (FileNotFoundException fileNotFoundException) {
         status = false;
         errorMessage = fileNotFoundException.getMessage();
         throw new PublisherException(PublisherExceptionCodes.PUBLISHER_DEFAULT_EXCEPTION_CODE, fileNotFoundException);
      } catch (SDXException e) {
         status = false;
         errorMessage = e.getMessage();
         throw new PublisherException(PublisherExceptionCodes.PUBLISHER_DEFAULT_EXCEPTION_CODE, e);
      } catch (Exception e) {
         status = false;
         errorMessage = e.getMessage();
         throw new PublisherException(PublisherExceptionCodes.PUBLISHER_DEFAULT_EXCEPTION_CODE, e);
      } finally {
         if (!status && jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE, errorMessage,
                     new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new PublisherException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e1);
            }
         }
         if (publisherMetaReadContext != null && publisherMetaReadContext.getCsvReader() != null) {
            publisherMetaReadContext.getCsvReader().close();
         }
         if (publisherBatchDataReadContext != null && publisherBatchDataReadContext.getCsvReader() != null) {
            publisherBatchDataReadContext.getCsvReader().close();
         }
      }
      return status;
   }

   /*
    * private Asset absorbAsset (Asset asset, Long applicationId, DataSource dataSource, DBTableNormalizedInfo
    * dbTableNormalizedInfo, List<QueryColumn> virtualLookupColumns) throws PublisherException, SDXException,
    * KDXException { // absorb the asset in swi logger.debug("Absorbing an asset in swi"); // TODO : -VG- check for name
    * validity, if same, then append the random number to it.
    * asset.setName(ExecueCoreUtil.getNormalizedName(asset.getDisplayName())); asset.setStatus(StatusEnum.INACTIVE);
    * asset.setDataSource(dataSource); asset.setType(AssetType.Relational); asset.setOwnerType(AssetOwnerType.ExeCue);
    * asset.setPriority(1d); Application application = getKdxRetrievalService().getApplicationById(applicationId);
    * asset.setApplication(application); SuccessFailureType assetAbsorbtionStatus =
    * getPublisherDataAbsorbtionService().absorbAsset(asset, dbTableNormalizedInfo, virtualLookupColumns);
    * logger.debug("Asset absorbtion status " + assetAbsorbtionStatus); if
    * (SuccessFailureType.SUCCESS.equals(assetAbsorbtionStatus)) { asset.setStatus(StatusEnum.ACTIVE);
    * getSdxService().updateAsset(asset); } return asset; }
    */

   private DBTableNormalizedInfo evaluatePublisherMetaData (DBTableInfo dbTableInfo, DataSource dataSource,
            PublisherMetaReadContext publisherMetaReadContext) throws PublisherException {
      DBTableNormalizedInfo evaluatedDBTableNormalizedInfo = null;
      evaluatedDBTableNormalizedInfo = publisherDataEvaluationService.evaluateDBTableInfo(dbTableInfo, dataSource);
      evaluatedDBTableNormalizedInfo.getDbTable().setTableName(
               prepareEvaluatedTableName(
                        getCoreConfigurationService().getPrefixUploadedTableName() + "_"
                                 + publisherMetaReadContext.getUserId() + "_"
                                 + publisherMetaReadContext.getNormalizedFileName()).toLowerCase());
      return evaluatedDBTableNormalizedInfo;
   }

   /*
    * private void absorbPublisherMetaData (DBTableNormalizedInfo evaluatedDBTableNormalizedInfo, DBTableInfo
    * dbTableInfo, DataSource dataSource) throws PublisherException { if
    * (publisherDataAbsorbtionService.absorbPublisherMetaData(dataSource, evaluatedDBTableNormalizedInfo)) {
    * DBTableNormalizedInfo orginalDbTableNormalizedInfo = PublisherUtilityHelper.normalizeDBTableInfo(dbTableInfo);
    * publisherDataAbsorbtionService.transformData(dataSource, orginalDbTableNormalizedInfo,
    * evaluatedDBTableNormalizedInfo); publisherDataAbsorbtionService.populateTotalRecordsCount(dataSource,
    * evaluatedDBTableNormalizedInfo); } }
    */

   private PublisherMetaReadContext populatePublisherMetaContext (Long applicationId, Long userId,
            boolean isColumnsAvailable, String filePath, String dataDelimeter, String originalFileName,
            String displayFileName) throws FileNotFoundException {
      File file = new File(filePath);
      CsvReader csvReader = new CsvReader(new BufferedReader(new FileReader(file)), dataDelimeter.charAt(0));
      PublisherMetaReadContext publisherMetaReadContext = new PublisherMetaReadContext();
      publisherMetaReadContext.setApplicationId(applicationId);
      publisherMetaReadContext.setUserId(userId);
      publisherMetaReadContext.setColumnsAvailable(isColumnsAvailable);
      publisherMetaReadContext.setFileName(file.getName());
      publisherMetaReadContext.setCsvReader(csvReader);
      publisherMetaReadContext.setOriginalFileName(originalFileName);
      publisherMetaReadContext.setDisplayFileName(displayFileName);
      return publisherMetaReadContext;
   }

   private PublisherBatchDataReadContext populateBatchDataReadContext (String filePath, int startingLineNumber,
            int numColumns, String dataDelimeter, CSVEmptyField csvEmptyField,
            CSVStringEnclosedCharacterType csvStringEnclosedCharacterType) throws FileNotFoundException {
      File file = new File(filePath);
      CsvReader csvReader = new CsvReader(new BufferedReader(new FileReader(file)), dataDelimeter.charAt(0));
      csvReader.setSkipEmptyRecords(true);
      if (!CSVStringEnclosedCharacterType.NONE.equals(csvStringEnclosedCharacterType)) {
         csvReader.setUseTextQualifier(true);
         csvReader.setTextQualifier(csvStringEnclosedCharacterType.getValue().charAt(0));
      }
      csvReader.setTrimWhitespace(true);
      PublisherBatchDataReadContext publisherBatchDataReadContext = new PublisherBatchDataReadContext();
      publisherBatchDataReadContext.setCsvReader(csvReader);
      publisherBatchDataReadContext.setStartingLine(startingLineNumber);
      publisherBatchDataReadContext.setNumColumns(numColumns);
      publisherBatchDataReadContext.setBatchSize(publisherConfigurationService.getBatchSize());

      publisherBatchDataReadContext.setMaxDataLength(publisherConfigurationService.getMaxDataTypeLenght());

      List<String> charactersToEscape = publisherConfigurationService.getCharactersToEscape();
      List<CharSequence> characterSequencesToEscape = new ArrayList<CharSequence>();
      characterSequencesToEscape.addAll(charactersToEscape);
      publisherBatchDataReadContext.setCharactersToEscape(characterSequencesToEscape);

      publisherBatchDataReadContext.setEscapeCharacter(publisherConfigurationService.getEscapeCharacter());

      publisherBatchDataReadContext.setCsvEmptyField(csvEmptyField.getValue());
      publisherBatchDataReadContext.setReplaceValueMapToAvoidScriptInjection(getReplaceMapForAvoidingScriptInjection());
      return publisherBatchDataReadContext;
   }

   private Map<String, String> getReplaceMapForAvoidingScriptInjection () {
      return getPublisherConfigurationService().getScriptAvoidingInjectionMap();
   }

   private String prepareEvaluatedTableName (String tempTableName) {
      String tableName = tempTableName;
      tableName = tableName
               + "_"
               + RandomStringUtils.randomAlphanumeric(getPublisherConfigurationService()
                        .getTableNameRandomStringLength());
      tableName = ExecueStringUtil.getNormalizedName(tableName, getCoreConfigurationService().getMaxDBObjectLength());
      return tableName;
   }

   private PublishedFileTableInfo updatePublishedTableInfo (PublishedFileInfo publishedFileInfo,
            PublishedFileTableInfo publishedFileTableInfo, DBTable evaluatedTable) throws PublishedFileException {

      publishedFileTableInfo.setEvaluatedTableName(evaluatedTable.getTableName());

      getPublishedFileManagementService().updatePublishedFileTableInfo(publishedFileTableInfo);

      return publishedFileTableInfo;
   }

   private PublishedFileTableInfo persistPublishedTableInfo (PublishedFileInfo publishedFileInfo, DBTable baseTable)
            throws PublishedFileException {
      PublishedFileTableInfo publishedFileTableInfo = new PublishedFileTableInfo();

      publishedFileTableInfo.setTempTableName(baseTable.getTableName());
      publishedFileTableInfo.setPublishedFileInfo(publishedFileInfo);
      publishedFileTableInfo.setWorkSheetName(publishedFileInfo.getOriginalFileName());
      publishedFileTableInfo.setDisplayTableName(baseTable.getDisplayName());
      getPublishedFileManagementService().persistPublishedFileTableInfo(publishedFileTableInfo);

      return publishedFileTableInfo;
   }

   private List<PublishedFileTableDetails> persistPublishedTableDetails (PublishedFileTableInfo publishedFileTableInfo,
            DBTableInfo dbTableInfo) throws PublishedFileException {

      List<PublishedFileTableDetails> publishedFileTableDetailsList = new ArrayList<PublishedFileTableDetails>();

      int index = 0;
      for (DBTableColumn dbTableColumn : dbTableInfo.getDbTableColumns()) {
         PublishedFileTableDetails publishedFileTableDetails = new PublishedFileTableDetails();

         publishedFileTableDetails.setPublishedFileTableInfo(publishedFileTableInfo);

         publishedFileTableDetails.setBaseColumnName(dbTableColumn.getColumnName());
         publishedFileTableDetails.setBaseDataType(dbTableColumn.getDataType());
         publishedFileTableDetails.setBasePrecision(dbTableColumn.getPrecision());
         publishedFileTableDetails.setBaseScale(dbTableColumn.getScale());

         publishedFileTableDetails.setKdxDataType(ColumnType.NULL);

         publishedFileTableDetails.setColumnIndex(index);

         publishedFileTableDetailsList.add(publishedFileTableDetails);
         index++;
      }
      getPublishedFileManagementService().persistPublishedFileTableDetails(publishedFileTableDetailsList);
      return publishedFileTableDetailsList;
   }

   private List<PublishedFileTableDetails> updatePublishedTableDetails (
            List<PublishedFileTableDetails> publishedFileTableDetailsList,
            DBTableNormalizedInfo evaluatedDBTableNormalizedInfo) throws PublishedFileException {

      int index = 0;
      for (PublishedFileTableDetails publishedFileTableDetails : publishedFileTableDetailsList) {

         QueryColumn evaluatedColumn = evaluatedDBTableNormalizedInfo.getNormalizedColumns().get(index);

         publishedFileTableDetails.setEvaluatedColumnName(evaluatedColumn.getColumnName());
         publishedFileTableDetails.setEvaluatedColumnDisplayName(evaluatedColumn.getColumnName());
         publishedFileTableDetails.setEvaluatedDataType(evaluatedColumn.getDataType());
         publishedFileTableDetails.setEvaluatedPrecision(evaluatedColumn.getPrecision());
         publishedFileTableDetails.setEvaluatedScale(evaluatedColumn.getScale());
         publishedFileTableDetails.setOriginalEvaluatedDataType(evaluatedColumn.getDataType());
         publishedFileTableDetails.setOriginalEvaluatedPrecision(evaluatedColumn.getPrecision());
         publishedFileTableDetails.setOriginalEvaluatedScale(evaluatedColumn.getScale());
         publishedFileTableDetails.setFormat(evaluatedColumn.getDataFormat());
         publishedFileTableDetails.setUnitType(evaluatedColumn.getUnitType());
         publishedFileTableDetails.setOriginalUnitType(evaluatedColumn.getUnitType());
         index++;
      }
      getPublishedFileManagementService().updatePublishedFileTableDetails(publishedFileTableDetailsList);
      return publishedFileTableDetailsList;
   }

   public IPublisherDataAbsorbtionService getPublisherDataAbsorbtionService () {
      return publisherDataAbsorbtionService;
   }

   public void setPublisherDataAbsorbtionService (IPublisherDataAbsorbtionService publisherDataAbsorbtionService) {
      this.publisherDataAbsorbtionService = publisherDataAbsorbtionService;
   }

   public IPublisherDataUploadService getPublisherDataUploadService () {
      return publisherDataUploadService;
   }

   public void setPublisherDataUploadService (IPublisherDataUploadService publisherDataUploadService) {
      this.publisherDataUploadService = publisherDataUploadService;
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

   public IDataSourceSelectionService getDataSourceSelectionService () {
      return dataSourceSelectionService;
   }

   public void setDataSourceSelectionService (IDataSourceSelectionService dataSourceSelectionService) {
      this.dataSourceSelectionService = dataSourceSelectionService;
   }

   public IPublishedFileManagementService getPublishedFileManagementService () {
      return publishedFileManagementService;
   }

   public void setPublishedFileManagementService (IPublishedFileManagementService publishedFileManagementService) {
      this.publishedFileManagementService = publishedFileManagementService;
   }

   public IPublishedFileRetrievalService getPublishedFileRetrievalService () {
      return publishedFileRetrievalService;
   }

   public void setPublishedFileRetrievalService (IPublishedFileRetrievalService publishedFileRetrievalService) {
      this.publishedFileRetrievalService = publishedFileRetrievalService;
   }

}
