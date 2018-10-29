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


package com.execue.sforce.replication.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.execue.core.common.api.querygen.IQueryGenerationUtilService;
import com.execue.core.common.api.querygen.QueryGenerationUtilServiceFactory;
import com.execue.core.common.api.swi.ISDXService;
import com.execue.core.common.api.swi.ISourceMetaDataService;
import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.SQLTable;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.util.ExecueBeanUtils;
import com.execue.core.configuration.ExecueConfiguration;
import com.execue.core.exception.swi.SDXException;
import com.execue.core.exception.swi.SourceMetaDataException;
import com.execue.core.util.ExeCueUtils;
import com.execue.core.util.ExecueDateTimeUtils;
import com.execue.sforce.absorption.ISforceAbsorptionService;
import com.execue.sforce.bean.SObjectColumn;
import com.execue.sforce.bean.SObjectNormalizedData;
import com.execue.sforce.bean.SObjectNormalizedMeta;
import com.execue.sforce.bean.SObjectTable;
import com.execue.sforce.bean.SforceLoginContext;
import com.execue.sforce.bean.entity.SObjectMetaEntity;
import com.execue.sforce.configuration.ISforceConfigurationConstants;
import com.execue.sforce.configuration.ISforceConstants;
import com.execue.sforce.convertable.ISforceConvertableService;
import com.execue.sforce.dataaccess.ISforceDataAccessService;
import com.execue.sforce.exception.SforceException;
import com.execue.sforce.exception.SforceExceptionCodes;
import com.execue.sforce.helper.SforceUtilityHelper;
import com.execue.sforce.parser.IParseSoapResponseService;
import com.execue.sforce.replication.ISforceReplicationService;
import com.execue.sforce.soap.ISOAPRequestResponseService;

/**
 * This interface controls the replication flow for sforce user
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/08/09
 */
public class SforceReplicationServiceImpl implements ISforceReplicationService, ISforceConstants {

   private IParseSoapResponseService         parseSOAPResponseService;
   private ISforceAbsorptionService          sforceAbsorptionService;
   private ISOAPRequestResponseService       soapRequestResponseService;
   private ISourceMetaDataService            sourceMetaDataService;
   private QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory;
   private ISDXService                       sdxService;
   private ExecueConfiguration               sforceConfiguration;
   private ISforceConvertableService         sforceConvertableService;
   private ISforceDataAccessService          sforceDataAccessService;

   public boolean startReplication (SforceLoginContext sforceLoginContext) throws SforceException {
      boolean completeStatus = false;
      boolean status = replicateSforceMeta(sforceLoginContext);
      boolean replicateSforceData = replicateSforceData(sforceLoginContext);
      // analyse the status
      return completeStatus;
   }

   public boolean replicateSforceMeta (SforceLoginContext sforceLoginContext) {
      try {
         List<SObjectNormalizedMeta> sobjectNormalizedMetaList = getSObjectNormalizedMeta(sforceLoginContext);
         String targetDataSource = sforceConfiguration
                  .getProperty(ISforceConfigurationConstants.DEFAULT_TARGET_DATA_SOURCE_NAME_KEY);

         DataSource dataSource = getSdxService().getDataSource(targetDataSource);
         for (SObjectNormalizedMeta sObjectNormalizedMeta : sobjectNormalizedMetaList) {

            getSforceAbsorptionService().absorbSObjectMeta(dataSource, sObjectNormalizedMeta);
         }
      } catch (SforceException e) {
         return false;
      } catch (SDXException e) {
         return false;
      }
      return true;
   }

   private List<SObjectNormalizedMeta> getSObjectNormalizedMeta (SforceLoginContext sforceLoginContext)
            throws SforceException {
      List<SObjectNormalizedMeta> sobjectNormalizedMetaList = new ArrayList<SObjectNormalizedMeta>();

      // TODO : -VG- against the job request came in context set the status for process
      String soapDescribeTabsRequestXML = SforceUtilityHelper.prepareSOAPDescribeTabsRequestXML(sforceLoginContext
               .getPartnerSessionId());
      String soapDescribeTabsResponseXML = getSoapRequestResponseService().executeSOAPRequest(
               sforceLoginContext.getPartnerSessionURL(), soapDescribeTabsRequestXML);
      String saopDescribeTabsResponseValidityStatus = getParseSOAPResponseService().checkSOAPResponseForValidity(
               soapDescribeTabsResponseXML);
      if (saopDescribeTabsResponseValidityStatus == null) {
         Set<SObjectTable> sObjectTables = getParseSOAPResponseService().parseSOAPDescribeTabsResponseXML(
                  soapDescribeTabsResponseXML);
         for (SObjectTable sObjectTable : sObjectTables) {
            String soapMetaRequestXML = SforceUtilityHelper.prepareSOAPMetaRequestXML(sforceLoginContext
                     .getPartnerSessionId(), sObjectTable);
            String soapMetaResponseXML = getSoapRequestResponseService().executeSOAPRequest(
                     sforceLoginContext.getPartnerSessionURL(), soapMetaRequestXML);
            String soapMetaReponseValidityStatus = getParseSOAPResponseService().checkSOAPResponseForValidity(
                     soapMetaResponseXML);
            if (soapMetaReponseValidityStatus == null) {
               List<SObjectColumn> sobjectColumns = getParseSOAPResponseService().parseSOAPMetaResponseXML(
                        soapMetaResponseXML);
               SObjectNormalizedMeta sObjectNormalizedMeta = getSforceConvertableService()
                        .populateSObjectNormalizedMeta(sObjectTable, sobjectColumns);
               sobjectNormalizedMetaList.add(sObjectNormalizedMeta);

            } else {
               // log the status
            }
         }
      } else {
         // log the status
      }
      return sobjectNormalizedMetaList;

   }

   /**
    * This method replicates the data from sales force using sessionId and sessionURL passed and store the data in data
    * source location
    * 
    * @param partnerSessionId
    * @param partnerSessionURL
    * @return status
    * @throws SforceException
    */
   public boolean replicateSforceData (SforceLoginContext sforceLoginContext) throws SforceException {
      try {
         // TODO : -VG- against the job request came in context set the status for process
         String targetDataSource = sforceConfiguration
                  .getProperty(ISforceConfigurationConstants.DEFAULT_TARGET_DATA_SOURCE_NAME_KEY);
         int dataBatchSize = sforceConfiguration.getInt(ISforceConfigurationConstants.DATA_BATCH_SIZE_KEY);
         DataSource dataSource = getSdxService().getDataSource(targetDataSource);
         List<Tabl> tablesFromSource = getSourceMetaDataService().getTablesFromSource(dataSource);
         List<Thread> threads = new ArrayList<Thread>(tablesFromSource.size());
         for (Tabl tabl : tablesFromSource) {
            DataReplication dataReplication = new DataReplication(tabl, dataSource, dataBatchSize, sforceLoginContext);
            Thread thread = new Thread(dataReplication);
            threads.add(thread);
            thread.start();
         }
         for (Thread thread : threads) {
            try {
               thread.join();
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
         return false;
      } catch (SDXException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_DEFAULT_EXCEPTION_CODE, e);
      } catch (SourceMetaDataException e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_DEFAULT_EXCEPTION_CODE, e);
      }
   }

   private void handleQueryLocator (String queryLocator, SforceLoginContext sforceLoginContext, String tableName,
            List<QueryColumn> queryColumns, DataSource dataSource, int dataBatchSize) throws SforceException {
      do {
         String soapQueryLocatorXML = SforceUtilityHelper.prepareSOAPQueryLocatorXML(sforceLoginContext
                  .getPartnerSessionId(), queryLocator, dataBatchSize);
         String soapResponseXML = getSoapRequestResponseService().executeSOAPRequest(
                  sforceLoginContext.getPartnerSessionURL(), soapQueryLocatorXML);
         String validityStatus = getParseSOAPResponseService().checkSOAPResponseForValidity(soapResponseXML);
         if (validityStatus == null) {
            List<List<String>> soapResponseData = getParseSOAPResponseService().parseSOAPDataResponseXML(queryColumns,
                     soapResponseXML);
            SObjectTable sObjectTable = new SObjectTable();
            sObjectTable.setTableName(tableName);
            SObjectNormalizedData sObjectNormalizedData = getSforceConvertableService().populateSObjectNormalizedData(
                     sObjectTable, queryColumns, soapResponseData);
            boolean status = getSforceAbsorptionService().absorbSObjectData(dataSource, sObjectNormalizedData);
            queryLocator = getParseSOAPResponseService().parseSOAPDataResponseXMLForQueryLocator(soapResponseXML);
         } else {
            // log the execption
         }
      } while (queryLocator != null);
   }

   private class DataReplication implements Runnable {

      private Tabl               tabl;
      private DataSource         dataSource;
      private int                dataBatchSize;
      private SforceLoginContext sforceLoginContext;

      public DataReplication (Tabl tabl, DataSource dataSource, int dataBatchSize, SforceLoginContext sforceLoginContext) {
         this.tabl = tabl;
         this.dataSource = dataSource;
         this.dataBatchSize = dataBatchSize;
         this.sforceLoginContext = sforceLoginContext;
      }

      public void run () {
         try {
            doDataReplication(tabl, dataSource, dataBatchSize, sforceLoginContext);
         } catch (SforceException sforceException) {
            sforceException.printStackTrace();
         }
      }

      private boolean doDataReplication (Tabl tabl, DataSource dataSource, int dataBatchSize,
               SforceLoginContext sforceLoginContext) throws SforceException {
         try {
            String tableName = tabl.getName();
            List<Colum> columnsFromSource = getSourceMetaDataService().getColumnsFromSource(dataSource, tabl);
            List<QueryColumn> queryColumns = ExecueBeanUtils.prepareQueryColumns(columnsFromSource);
            SQLTable sqlTable = ExecueBeanUtils.prepareSQLTableFromQueryColumns(tableName, queryColumns);
            // TODO : -VG- need to get the data source provider of sforce
            String selectStatement = getQueryGenerationUtilService(AssetProviderType.Oracle).createSelectStatement(
                     sqlTable);
            String soapRequestXML = SforceUtilityHelper.prepareSOAPDataRequestXML(sforceLoginContext
                     .getPartnerSessionId(), selectStatement, dataBatchSize);
            String soapResponseXML = getSoapRequestResponseService().executeSOAPRequest(
                     sforceLoginContext.getPartnerSessionURL(), soapRequestXML);
            String validityStatus = getParseSOAPResponseService().checkSOAPResponseForValidity(soapResponseXML);
            if (validityStatus == null) {
               List<List<String>> soapResponseData = getParseSOAPResponseService().parseSOAPDataResponseXML(
                        queryColumns, soapResponseXML);
               SObjectTable sObjectTable = new SObjectTable();
               sObjectTable.setTableName(tableName);
               SObjectNormalizedData sObjectNormalizedData = getSforceConvertableService()
                        .populateSObjectNormalizedData(sObjectTable, queryColumns, soapResponseData);
               boolean status = getSforceAbsorptionService().absorbSObjectData(dataSource, sObjectNormalizedData);
               String queryLocator = getParseSOAPResponseService().parseSOAPDataResponseXMLForQueryLocator(
                        soapResponseXML);
               if (queryLocator != null) {
                  handleQueryLocator(queryLocator, sforceLoginContext, tableName, queryColumns, dataSource,
                           dataBatchSize);
               }
               // based on that made some more decisions
            } else {
               // log the status
            }
            // TODO : -VG- analyse the status and return appropriate value
         } catch (SourceMetaDataException sourceMetaDataException) {
            throw new SforceException(SforceExceptionCodes.SFORCE_DEFAULT_EXCEPTION_CODE, sourceMetaDataException);
         }
         return true;
      }

   }

   public IParseSoapResponseService getParseSOAPResponseService () {
      return parseSOAPResponseService;
   }

   public void setParseSOAPResponseService (IParseSoapResponseService parseSOAPResponseService) {
      this.parseSOAPResponseService = parseSOAPResponseService;
   }

   public ExecueConfiguration getSforceConfiguration () {
      return sforceConfiguration;
   }

   public void setSforceConfiguration (ExecueConfiguration sforceConfiguration) {
      this.sforceConfiguration = sforceConfiguration;
   }

   public ISOAPRequestResponseService getSoapRequestResponseService () {
      return soapRequestResponseService;
   }

   public void setSoapRequestResponseService (ISOAPRequestResponseService soapRequestResponseService) {
      this.soapRequestResponseService = soapRequestResponseService;
   }

   public ISourceMetaDataService getSourceMetaDataService () {
      return sourceMetaDataService;
   }

   public void setSourceMetaDataService (ISourceMetaDataService sourceMetaDataService) {
      this.sourceMetaDataService = sourceMetaDataService;
   }

   public ISDXService getSdxService () {
      return sdxService;
   }

   public void setSdxService (ISDXService sdxService) {
      this.sdxService = sdxService;
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

   public ISforceConvertableService getSforceConvertableService () {
      return sforceConvertableService;
   }

   public void setSforceConvertableService (ISforceConvertableService sforceConvertableService) {
      this.sforceConvertableService = sforceConvertableService;
   }

   public ISforceAbsorptionService getSforceAbsorptionService () {
      return sforceAbsorptionService;
   }

   public void setSforceAbsorptionService (ISforceAbsorptionService sforceAbsorptionService) {
      this.sforceAbsorptionService = sforceAbsorptionService;
   }

   public boolean startDataSyncUpProcess (SforceLoginContext sforceLoginContext) throws SforceException {
      syncDeletedRecords(sforceLoginContext);
      syncUpdatedRecords(sforceLoginContext);
      return false;
   }

   private boolean syncUpdatedRecords (SforceLoginContext sforceLoginContext) throws SforceException {
      try {
         // TODO : -VG- against the job request came in context set the status for process
         int dataBatchSize = sforceConfiguration.getInt(ISforceConfigurationConstants.DATA_BATCH_SIZE_KEY);
         String targetDataSource = sforceConfiguration
                  .getProperty(ISforceConfigurationConstants.DEFAULT_TARGET_DATA_SOURCE_NAME_KEY);
         DataSource dataSource = getSdxService().getDataSource(targetDataSource);
         List<Tabl> tablesFromSource = getSourceMetaDataService().getTablesFromSource(dataSource);
         for (Tabl tabl : tablesFromSource) {
            System.out.println("tableName :" + tabl.getName());
            String startDate = ExecueDateTimeUtils.getISODateTimeFormatFromDate(getSforceDataAccessService()
                     .getLastModifiedData(tabl.getName()));
            System.out.println("startdate :" + startDate);
            String endDate = ExecueDateTimeUtils.getISODateTimeFormatFromDate(new Date());
            System.out.println("End date :" + endDate);
            String soapResponseXML = getUpdatedRecords(tabl, startDate, endDate, sforceLoginContext);
            String validityStatus = getParseSOAPResponseService().checkSOAPResponseForValidity(soapResponseXML);
            System.out.println(validityStatus);
            if (validityStatus == null) {
               // if we found the updated records list properly
               List<String> soapResponseData = getParseSOAPResponseService()
                        .parseSOAPUpdateResponseXML(soapResponseXML);
               System.out.println("list size :" + soapResponseData.size());
               if (!ExeCueUtils.isCollectionEmpty(soapResponseData)) {
                  List<Colum> columnsFromSource = getSourceMetaDataService().getColumnsFromSource(dataSource, tabl);
                  List<QueryColumn> queryColumns = ExecueBeanUtils.prepareQueryColumns(columnsFromSource);
                  SQLTable sqlTable = ExecueBeanUtils.prepareSQLTableFromQueryColumns(tabl.getName(), queryColumns);
                  // TODO : revist this logic
                  int startCounter = 0;
                  do {
                     int endCounter = startCounter + 10;
                     if (startCounter + 10 >= soapResponseData.size()) {
                        endCounter = soapResponseData.size();
                     }
                     List<String> subList = soapResponseData.subList(startCounter, endCounter - 1);
                     fetchAndStoreUpdatedRecords(sforceLoginContext, subList, sqlTable, dataSource, dataBatchSize);
                     startCounter = endCounter;
                  } while (startCounter < soapResponseData.size());

               }
               // meta table, update the last modified date
               SObjectMetaEntity sobjectMetaEntity = getSforceDataAccessService().getSObjectMetaByName(tabl.getName());

               String lastModifiedDate = getParseSOAPResponseService().parseSOAPUpdateResponseXMLForModificatonDate(
                        soapResponseXML);
               System.out.println("lastmodifiedDate :" + lastModifiedDate);
               Date date = ExecueDateTimeUtils.getDateFromString(lastModifiedDate);
               System.out.println(date);
               sobjectMetaEntity.setLastModifiedData(ExecueDateTimeUtils.getDateFromString(lastModifiedDate));
               getSforceDataAccessService().updateSobjectMetaEntity(sobjectMetaEntity);
            }
         }
      } catch (SDXException sdxException) {
         throw new SforceException(SforceExceptionCodes.SFORCE_DEFAULT_EXCEPTION_CODE, sdxException);
      } catch (SourceMetaDataException sourceMetaDataException) {
         throw new SforceException(SforceExceptionCodes.SFORCE_DEFAULT_EXCEPTION_CODE, sourceMetaDataException);
      }
      return false;
   }

   private void fetchAndStoreUpdatedRecords (SforceLoginContext sforceLoginContext, List<String> soapResponseData,
            SQLTable sqlTable, DataSource dataSource, int dataBatchSize) throws SforceException {
      // TODO : -VG- need to get the data source provider of sforce
      String selectStatement = prepareSelectRecordsBasedOnIdStatement(getQueryGenerationUtilService(
               AssetProviderType.Oracle).createSelectStatement(sqlTable), soapResponseData);
      System.out.println(selectStatement);
      String soapInsertRequestXML = SforceUtilityHelper.prepareSOAPDataRequestXML(sforceLoginContext
               .getPartnerSessionId(), selectStatement, dataBatchSize);

      // ToDo : need to handle batch size
      String soapInsertResponseXML = getSoapRequestResponseService().executeSOAPRequest(
               sforceLoginContext.getPartnerSessionURL(), soapInsertRequestXML);
      String insertValidityStatus = getParseSOAPResponseService().checkSOAPResponseForValidity(soapInsertResponseXML);
      System.out.println(insertValidityStatus);
      if (insertValidityStatus == null) {
         List<QueryColumn> queryColumns = sqlTable.getColumns();
         List<List<String>> soapInsertResponseData = getParseSOAPResponseService().parseSOAPDataResponseXML(
                  sqlTable.getColumns(), soapInsertResponseXML);
         List<List<String>> insertDataPoints = new ArrayList<List<String>>();
         List<List<String>> updateDataPoints = new ArrayList<List<String>>();
         splitInsertAndUpdateRecords(dataSource, sqlTable.getTableName(), soapInsertResponseData, updateDataPoints,
                  insertDataPoints);
         System.out.println("insertRecords List :" + insertDataPoints.size());
         System.out.println("updated Records List : " + updateDataPoints.size());
         if (!ExeCueUtils.isCollectionEmpty(insertDataPoints)) {
            absorbUpdatedRecords(dataSource, sqlTable, insertDataPoints, true);
         }
         if (!ExeCueUtils.isCollectionEmpty(updateDataPoints)) {
            absorbUpdatedRecords(dataSource, sqlTable, updateDataPoints, false);
         }
      }
   }

   private void absorbUpdatedRecords (DataSource dataSource, SQLTable sqlTable, List<List<String>> dataPoints,
            boolean isInsertion) throws SforceException {
      SObjectTable sObjectTable = new SObjectTable();
      sObjectTable.setTableName(sqlTable.getTableName());
      List<QueryColumn> queryColumns = sqlTable.getColumns();
      SObjectNormalizedData sObjectNormalizedUpdateSyncData = getSforceConvertableService()
               .populateSObjectNormalizedData(sObjectTable, queryColumns, dataPoints);
      absorbSObjectSyncData(dataSource, sObjectNormalizedUpdateSyncData, isInsertion);

   }

   private void splitInsertAndUpdateRecords (DataSource dataSource, String tableName,
            List<List<String>> soapInsertResponseData, List<List<String>> updateDataPoints,
            List<List<String>> insertDataPoints) throws SforceException {
      List<String> existingRecordIds = new ArrayList<String>();
      String selectStatement = selectQueryForIds(tableName);
      ExeCueResultSet resultSet = getSforceAbsorptionService().executeSelectStatement(dataSource, selectStatement);
      try {
         while (resultSet.next()) {
            existingRecordIds.add(resultSet.getString("Id"));
         }
      } catch (Exception e) {
         throw new SforceException(SforceExceptionCodes.SFORCE_DEFAULT_EXCEPTION_CODE, e);
      }
      for (List<String> repsonseData : soapInsertResponseData) {
         String rowId = repsonseData.get(0);
         if (existingRecordIds.contains(rowId)) {
            updateDataPoints.add(repsonseData);
         } else {
            insertDataPoints.add(repsonseData);
         }
      }
   }

   private String selectQueryForIds (String tableName) {
      StringBuilder selectStatement = new StringBuilder();
      selectStatement.append(SQL_SELECT_CLAUSE);
      selectStatement.append(SQL_SPACE_DELIMITER);
      selectStatement.append(ID_WORD_CONSTANT);
      selectStatement.append(SQL_SPACE_DELIMITER);
      selectStatement.append(SQL_JOIN_FROM_CLAUSE);
      selectStatement.append(SQL_SPACE_DELIMITER);
      selectStatement.append(tableName);
      return selectStatement.toString();

   }

   private String getUpdatedRecords (Tabl tabl, String startDate, String endDate, SforceLoginContext sforceLoginContext)
            throws SforceException {
      String soapRequestXML = SforceUtilityHelper.prepareSOAPUpdateRecordsRequestXML(tabl.getName(), startDate,
               endDate, sforceLoginContext.getPartnerSessionId());
      return (getSoapRequestResponseService().executeSOAPRequest(sforceLoginContext.getPartnerSessionURL(),
               soapRequestXML));
   }

   private boolean syncDeletedRecords (SforceLoginContext sforceLoginContext) throws SforceException {
      try {
         // TODO : -VG- against the job request came in context set the status for process
         int dataBatchSize = sforceConfiguration.getInt(ISforceConfigurationConstants.DATA_BATCH_SIZE_KEY);
         String targetDataSource = sforceConfiguration
                  .getProperty(ISforceConfigurationConstants.DEFAULT_TARGET_DATA_SOURCE_NAME_KEY);
         DataSource dataSource = getSdxService().getDataSource(targetDataSource);
         List<Tabl> tablesFromSource = getSourceMetaDataService().getTablesFromSource(dataSource);
         for (Tabl tabl : tablesFromSource) {
            String startDate = ExecueDateTimeUtils.getISODateTimeFormatFromDate(getSforceDataAccessService()
                     .getLastModifiedData(tabl.getName()));
            String endDate = ExecueDateTimeUtils.getISODateTimeFormatFromDate(new Date());
            String soapRequestXML = SforceUtilityHelper.prepareSOAPDeleteRecordsRequestXML(tabl.getName(), startDate,
                     endDate, sforceLoginContext.getPartnerSessionId());
            String soapResponseXML = getSoapRequestResponseService().executeSOAPRequest(
                     sforceLoginContext.getPartnerSessionURL(), soapRequestXML);
            String validityStatus = getParseSOAPResponseService().checkSOAPResponseForValidity(soapResponseXML);
            if (validityStatus == null) {
               // if we found the deleted records list properly
               List<String> soapResponseData = getParseSOAPResponseService()
                        .parseSOAPDeleteResponseXML(soapResponseXML);
               // if the list of records to delete is not empty
               if (!ExeCueUtils.isCollectionEmpty(soapResponseData)) {
                  createTempTable(dataSource, tabl);
                  boolean status = deleteDeletedRecords(dataSource, tabl, soapResponseData);
                  updateTempTables(dataSource, status, tabl);
               }
            }
            // if an exception occurs because of date issues or recycle bin purge issues
            else {
               createTempTable(dataSource, tabl);
               emptyMainTable(dataSource, tabl);
               boolean status = insertFreshRecords(dataSource, tabl, dataBatchSize, sforceLoginContext);
               updateTempTables(dataSource, status, tabl);
            }
         }
      } catch (SDXException sdxException) {
         throw new SforceException(SforceExceptionCodes.SFORCE_DEFAULT_EXCEPTION_CODE, sdxException);
      } catch (SourceMetaDataException sourceMetaDataException) {
         throw new SforceException(SforceExceptionCodes.SFORCE_DEFAULT_EXCEPTION_CODE, sourceMetaDataException);
      }
      return false;
   }

   private boolean insertFreshRecords (DataSource dataSource, Tabl tabl, int dataBatchSize,
            SforceLoginContext sforceLoginContext) throws SforceException {
      boolean isInsertionSuccess = true;
      DataReplication dataReplication = new DataReplication(tabl, dataSource, dataBatchSize, sforceLoginContext);
      Thread thread = new Thread(dataReplication);
      thread.start();
      try {
         thread.join();
      } catch (InterruptedException e) {
         isInsertionSuccess = false;
         throw new SforceException(SforceExceptionCodes.SFORCE_DEFAULT_EXCEPTION_CODE, e);
      }
      return isInsertionSuccess;
   }

   private boolean emptyMainTable (DataSource dataSource, Tabl tabl) throws SforceException {
      IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(dataSource
               .getProviderType());
      String deleteStatement = queryGenerationUtilService.createTableDeleteStatement(tabl.getName());
      return getSforceAbsorptionService().executeDefinitionStatement(dataSource, deleteStatement);
   }

   private boolean deleteDeletedRecords (DataSource dataSource, Tabl tabl, List<String> soapResponseData)
            throws SforceException {
      String deleteStatement = prepareDeleteRecordsBasedOnIdStatement(dataSource, tabl, soapResponseData);
      return getSforceAbsorptionService().executeDefinitionStatement(dataSource, deleteStatement);
   }

   private String prepareDeleteRecordsBasedOnIdStatement (DataSource dataSource, Tabl tabl, List<String> idsList) {
      IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(dataSource
               .getProviderType());
      String deleteStatement = queryGenerationUtilService.createTableDeleteStatement(tabl.getName());
      StringBuilder deleteRecordsOnIdStatement = new StringBuilder();
      deleteRecordsOnIdStatement.append(deleteStatement);
      deleteRecordsOnIdStatement.append(SQL_SPACE_DELIMITER);
      deleteRecordsOnIdStatement.append(SQL_WHERE_CLAUSE);
      deleteRecordsOnIdStatement.append(SQL_SPACE_DELIMITER);
      deleteRecordsOnIdStatement.append(ID_WORD_CONSTANT);
      deleteRecordsOnIdStatement.append(SQL_SPACE_DELIMITER);
      deleteRecordsOnIdStatement.append(WHERE_CONDITION_SEPERATOR);
      deleteRecordsOnIdStatement.append(SQL_SPACE_DELIMITER);
      deleteRecordsOnIdStatement.append(SQL_FUNCTION_START_WRAPPER);
      List<String> quoteOnIds = new ArrayList<String>();
      for (String id : idsList) {
         quoteOnIds.add(QUOTE + id + QUOTE);
      }
      String commaSeperatedListOfIds = ExeCueUtils.joinCollection(quoteOnIds);
      deleteRecordsOnIdStatement.append(commaSeperatedListOfIds);
      deleteRecordsOnIdStatement.append(SQL_FUNCTION_END_WRAPPER);
      return deleteRecordsOnIdStatement.toString();
   }

   private String prepareSelectRecordsBasedOnIdStatement (String selectQuery, List<String> idsList) {
      StringBuilder selectStatement = new StringBuilder();
      selectStatement.append(selectQuery);
      selectStatement.append(SQL_SPACE_DELIMITER);
      selectStatement.append(SQL_WHERE_CLAUSE);
      selectStatement.append(SQL_SPACE_DELIMITER);
      selectStatement.append(ID_WORD_CONSTANT);
      selectStatement.append(SQL_SPACE_DELIMITER);
      selectStatement.append(WHERE_CONDITION_SEPERATOR);
      selectStatement.append(SQL_SPACE_DELIMITER);
      selectStatement.append(SQL_FUNCTION_START_WRAPPER);
      List<String> quoteOnIds = new ArrayList<String>();
      for (String id : idsList) {
         quoteOnIds.add(QUOTE + id + QUOTE);
      }
      String commaSeperatedListOfIds = ExeCueUtils.joinCollection(quoteOnIds);
      selectStatement.append(commaSeperatedListOfIds);
      selectStatement.append(SQL_FUNCTION_END_WRAPPER);
      return selectStatement.toString();
   }

   private String prepareTempTableCreateStatement (Tabl tabl) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(CREATE_TABLE_COMMAND);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(tabl.getName());
      stringBuilder.append(SQL_WORD_SEPERATOR);
      stringBuilder.append(TEMP_WORD_CONSTANT);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(SQL_ALIAS);
      stringBuilder.append(SQL_SELECT_CLAUSE);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(SQL_WILD_CHAR);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(SQL_JOIN_FROM_CLAUSE);
      stringBuilder.append(SQL_SPACE_DELIMITER);
      stringBuilder.append(tabl.getName());
      return stringBuilder.toString();
   }

   private void createTempTable (DataSource dataSource, Tabl tabl) throws SforceException {
      String createTempTableStatment = prepareTempTableCreateStatement(tabl);
      getSforceAbsorptionService().executeDefinitionStatement(dataSource, createTempTableStatment);
   }

   private void updateTempTables (DataSource dataSource, boolean status, Tabl tabl) throws SforceException {
      IQueryGenerationUtilService queryGenerationUtilService = getQueryGenerationUtilService(dataSource
               .getProviderType());
      if (status) {
         String tableToDrop = tabl.getName() + SQL_WORD_SEPERATOR + TEMP_WORD_CONSTANT;
         String dropTableQuery = queryGenerationUtilService.createTableDropStatement(tableToDrop);
         getSforceAbsorptionService().executeDefinitionStatement(dataSource, dropTableQuery);
      } else {
         String dropTableQuery = queryGenerationUtilService.createTableDropStatement(tabl.getName());
         String fromTable = tabl.getName() + SQL_WORD_SEPERATOR + TEMP_WORD_CONSTANT;
         String renameTableQuery = queryGenerationUtilService.createRenameStatement(fromTable, tabl.getName());
         getSforceAbsorptionService().executeDefinitionStatement(dataSource, dropTableQuery);
         getSforceAbsorptionService().executeDefinitionStatement(dataSource, renameTableQuery);
      }
   }

   public boolean absorbSObjectSyncData (DataSource dataSource, SObjectNormalizedData sObjectNormalizedData,
            boolean isFreshInsertion) throws SforceException {
      boolean absorbDataSuccess = false;
      List<Integer> parameterTypes = SforceUtilityHelper.getParameterTypes(sObjectNormalizedData
               .getNormalizedQueryColumns());
      SQLTable sqlTable = ExecueBeanUtils.prepareSQLTableFromQueryColumns(sObjectNormalizedData.getSObjectTable()
               .getTableName(), sObjectNormalizedData.getNormalizedQueryColumns());
      String dmlStatement = null;
      if (isFreshInsertion) {
         dmlStatement = getQueryGenerationUtilService(dataSource.getProviderType()).createInsertStatement(sqlTable,
                  false);
         System.out.println(dmlStatement);
         getSforceAbsorptionService().executeManipulationStatements(dataSource, dmlStatement,
                  sObjectNormalizedData.getNormalizedDataPoints(), parameterTypes);
      } else {
         for (List<Object> dataRow : sObjectNormalizedData.getNormalizedDataPoints()) {
            String rowId = (String) dataRow.get(0);
            dmlStatement = getQueryGenerationUtilService(dataSource.getProviderType()).createUpdateStatement(sqlTable);
            StringBuilder updateDMLStatement = new StringBuilder();
            updateDMLStatement.append(dmlStatement);
            updateDMLStatement.append(SQL_SPACE_DELIMITER);
            updateDMLStatement.append(SQL_WHERE_CLAUSE);
            updateDMLStatement.append(SQL_SPACE_DELIMITER);
            updateDMLStatement.append("ID");
            updateDMLStatement.append(SQL_SPACE_DELIMITER);
            updateDMLStatement.append(JOIN_CONDITION_OPERATOR);
            updateDMLStatement.append(SQL_SPACE_DELIMITER);
            updateDMLStatement.append(QUOTE);
            updateDMLStatement.append(rowId);
            updateDMLStatement.append(QUOTE);
            System.out.println(updateDMLStatement.toString());
            getSforceAbsorptionService().executeManipulationStatement(dataSource, updateDMLStatement.toString(),
                     dataRow, parameterTypes);
         }
      }
      // TODO -VG- : analyse the output and return corresponding value
      return absorbDataSuccess;
   }

   public boolean startMetaSyncUpProcess (SforceLoginContext sforceLoginContext) throws SforceException {

      // prepare sobjectNormalizedMeta from the xml and from the database compare both of them, if they are same leave
      // otherwise delete from the swi and get it from the salesforce and create new object in swi

      // TODO Auto-generated method stub

      return false;
   }

   public ISforceDataAccessService getSforceDataAccessService () {
      return sforceDataAccessService;
   }

   public void setSforceDataAccessService (ISforceDataAccessService sforceDataAccessService) {
      this.sforceDataAccessService = sforceDataAccessService;
   }

}