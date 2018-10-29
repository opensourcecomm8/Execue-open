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


package com.execue.ac.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.execue.ac.bean.AnswersCatalogConfigurationContext;
import com.execue.ac.bean.ConceptColumnMapping;
import com.execue.ac.bean.CubeConfigurationContext;
import com.execue.ac.bean.CubeCreationInputInfo;
import com.execue.ac.bean.CubeCreationOutputInfo;
import com.execue.ac.bean.CubeCreationPopulatedContext;
import com.execue.ac.bean.CubePreFactTableDataTransferQueryInfo;
import com.execue.ac.bean.CubePreFactTableStructure;
import com.execue.ac.bean.CubeUpdatePreFactTableStructure;
import com.execue.ac.bean.CubeUpdationDimensionInfo;
import com.execue.ac.bean.CubeUpdationInputInfo;
import com.execue.ac.bean.CubeUpdationOutputInfo;
import com.execue.ac.bean.CubeUpdationPopulatedContext;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.CubePreFactPopulationException;
import com.execue.ac.helper.CubeCreationServiceHelper;
import com.execue.ac.service.IAnswersCatalogDataAccessService;
import com.execue.ac.service.IAnswersCatalogSQLQueryGenerationService;
import com.execue.ac.service.ICubeCreationConstants;
import com.execue.ac.service.ICubeOperationalConstants;
import com.execue.ac.service.ICubePreFactPopulationService;
import com.execue.ac.util.AnswersCatalogUtil;
import com.execue.core.bean.ThreadPoolTaskStatus;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.bean.querygen.SQLTable;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.service.ExecueFixedSizeThreadPoolManager;
import com.execue.core.type.ThreadPoolTaskStatusType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.util.querygen.IQueryGenerationUtilService;
import com.execue.util.querygen.QueryGenerationUtilServiceFactory;

/**
 * This service is used for pre fact table preparation.
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
public class CubePreFactPopulationServiceImpl implements ICubePreFactPopulationService, ICubeCreationConstants,
         ICubeOperationalConstants {

   private IAnswersCatalogSQLQueryGenerationService answersCatalogSQLQueryGenerationService;
   private CubeCreationServiceHelper                cubeCreationServiceHelper;
   private ISDXRetrievalService                     sdxRetrievalService;
   private QueryGenerationUtilServiceFactory        queryGenerationUtilServiceFactory;
   private IAnswersCatalogDataAccessService         answersCatalogDataAccessService;
   private static final Logger                      logger = Logger.getLogger(CubePreFactPopulationServiceImpl.class);

   public CubePreFactTableStructure createCubePreFactTable (CubeCreationOutputInfo cubeCreationOutputInfo)
            throws CubePreFactPopulationException {
      CubePreFactTableStructure cubePreFactTableStructure = null;
      CubeCreationInputInfo cubeCreationInputInfo = cubeCreationOutputInfo.getCubeCreationInputInfo();
      AnswersCatalogConfigurationContext answersCatalogConfigurationContext = cubeCreationInputInfo
               .getAnswersCatalogConfigurationContext();
      CubeConfigurationContext cubeConfigurationContext = cubeCreationInputInfo.getCubeConfigurationContext();
      CubeCreationPopulatedContext cubeCreationPopulatedContext = cubeCreationInputInfo
               .getCubeCreationPopulatedContext();
      JobRequest jobRequest = cubeCreationPopulatedContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      Exception exception = null;
      try {
         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  GENERATE_PRE_FACT_TEMP_TABLE_STRUCTURE);

         // generate pre fact table structure
         cubePreFactTableStructure = populatePreCubeTableStructure(answersCatalogConfigurationContext,
                  cubeConfigurationContext, cubeCreationPopulatedContext);
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  PRE_FACT_TEMP_TABLE_DATA_POPULATION);
         // populate the queries to execute
         List<CubePreFactTableDataTransferQueryInfo> queriesList = buildQueriesList(cubeConfigurationContext,
                  cubePreFactTableStructure);
         // populate the pre fact table with queries to execute
         boolean preFactTablePopulationSuccess = populatePreFactTable(cubePreFactTableStructure,
                  cubeCreationPopulatedContext, cubeConfigurationContext, queriesList);
         if (preFactTablePopulationSuccess) {
            getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
         } else {
            getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                     "There was error in populating the prefact table");
            throw new CubePreFactPopulationException(
                     AnswersCatalogExceptionCodes.DEFAULT_CUBE_PREFACT_TABLE_POPULATION_EXCEPTION_CODE,
                     "There was error in populating the prefact table");
         }

      } catch (AnswersCatalogException e) {
         exception = e;
         throw new CubePreFactPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_PREFACT_TABLE_POPULATION_EXCEPTION_CODE, e);
      } catch (InterruptedException e) {
         exception = e;
         throw new CubePreFactPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_THREAD_POOL_RELATED_EXCEPTION_CODE, e);
      } catch (ExecutionException e) {
         exception = e;
         throw new CubePreFactPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_THREAD_POOL_RELATED_EXCEPTION_CODE, e);
      } catch (Exception e) {
         exception = e;
         throw new CubePreFactPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_PREFACT_TABLE_POPULATION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new CubePreFactPopulationException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return cubePreFactTableStructure;
   }

   public CubeUpdatePreFactTableStructure createCubePreFactTable (CubeUpdationOutputInfo cubeUpdationOutputInfo)
            throws CubePreFactPopulationException {
      CubeUpdatePreFactTableStructure cubeUpdatePreFactTableStructure = null;
      CubeUpdationInputInfo cubeUpdationInputInfo = cubeUpdationOutputInfo.getCubeUpdationInputInfo();
      CubeUpdationPopulatedContext cubeUpdationPopulatedContext = cubeUpdationInputInfo
               .getCubeUpdationPopulatedContext();
      List<CubeUpdationDimensionInfo> cubeUpdationDimensionInfoList = cubeUpdationPopulatedContext
               .getCubeUpdationDimensionInfoList();
      CubeCreationPopulatedContext existingCubeCreationPopulatedContext = cubeUpdationPopulatedContext
               .getExistingCubeCreationPopulatedContext();
      CubeConfigurationContext cubeConfigurationContext = cubeUpdationInputInfo.getCubeConfigurationContext();
      AnswersCatalogConfigurationContext answersCatalogConfigurationContext = cubeUpdationInputInfo
               .getAnswersCatalogConfigurationContext();
      JobRequest jobRequest = cubeUpdationPopulatedContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      Exception exception = null;
      try {
         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  GENERATE_PRE_FACT_TEMP_TABLE_STRUCTURE);
         // generate pre fact table structure
         CubePreFactTableStructure cubePreFactTableStructure = populatePreCubeTableStructure(
                  answersCatalogConfigurationContext, cubeConfigurationContext, existingCubeCreationPopulatedContext);
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  PRE_FACT_TEMP_TABLE_DATA_POPULATION_PER_DIMENSION_FOR_CUBE_UPDATE);

         // for each dimension, prepare the queries and dump the data into pre-fact table
         for (CubeUpdationDimensionInfo cubeUpdationDimensionInfo : cubeUpdationDimensionInfoList) {
            // populate the queries to execute
            List<CubePreFactTableDataTransferQueryInfo> queriesList = buildQueriesListForCubeUpdate(
                     cubeConfigurationContext, cubePreFactTableStructure, existingCubeCreationPopulatedContext,
                     cubeUpdationDimensionInfo);
            // populate the pre fact table with queries to execute
            boolean preFactTablePopulationSuccess = populatePreFactTable(cubePreFactTableStructure,
                     existingCubeCreationPopulatedContext, cubeConfigurationContext, queriesList);
            if (!preFactTablePopulationSuccess) {
               getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        "There was error in populating the prefact table");
               throw new CubePreFactPopulationException(
                        AnswersCatalogExceptionCodes.DEFAULT_CUBE_PREFACT_TABLE_POPULATION_EXCEPTION_CODE,
                        "There was error in populating the prefact table");
            }
         }
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  DE_DUP_PRE_FACT_TEMP_TABLE_DATA_AND_POPULATE_FINAL_TEMP_TABLE);

         // de-dup the pre-fact table and get a de-dup table name
         String deDupPreFactTableName = deDupPreFactTable(cubePreFactTableStructure,
                  answersCatalogConfigurationContext, existingCubeCreationPopulatedContext);

         cubeUpdatePreFactTableStructure = buildCubeUpdatePreFactTableStructure(cubePreFactTableStructure,
                  deDupPreFactTableName);
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

      } catch (AnswersCatalogException e) {
         exception = e;
         throw new CubePreFactPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_PREFACT_TABLE_POPULATION_EXCEPTION_CODE, e);
      } catch (Exception e) {
         exception = e;
         throw new CubePreFactPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_PREFACT_TABLE_POPULATION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new CubePreFactPopulationException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return cubeUpdatePreFactTableStructure;

   }

   private CubeUpdatePreFactTableStructure buildCubeUpdatePreFactTableStructure (
            CubePreFactTableStructure cubePreFactTableStructure, String deDupPreFactTableName) {
      CubeUpdatePreFactTableStructure cubeUpdatePreFactTableStructure = new CubeUpdatePreFactTableStructure();
      // the fact table created in the begining is temp fact table. Actual fact table is one
      // which is cerated after de-dup the temp fact table
      cubeUpdatePreFactTableStructure.setTempFactTableName(cubePreFactTableStructure.getTableName());
      cubeUpdatePreFactTableStructure
               .setFrequencyMeasureColumns(cubePreFactTableStructure.getFrequencyMeasureColumns());
      cubeUpdatePreFactTableStructure.setFrequencyMeasureSelectEntities(cubePreFactTableStructure
               .getFrequencyMeasureSelectEntities());
      cubeUpdatePreFactTableStructure.setQueryIdColumn(cubePreFactTableStructure.getQueryIdColumn());
      cubeUpdatePreFactTableStructure.setQueryIdSelectEntity(cubePreFactTableStructure.getQueryIdSelectEntity());
      cubeUpdatePreFactTableStructure.setRangeLookupColumns(cubePreFactTableStructure.getRangeLookupColumns());
      cubeUpdatePreFactTableStructure.setRangeLookupSelectEntities(cubePreFactTableStructure
               .getRangeLookupSelectEntities());
      cubeUpdatePreFactTableStructure.setSimpleLookupColumns(cubePreFactTableStructure.getSimpleLookupColumns());
      cubeUpdatePreFactTableStructure.setSimpleLookupSelectEntities(cubePreFactTableStructure
               .getSimpleLookupSelectEntities());
      cubeUpdatePreFactTableStructure.setStatMeasureColumns(cubePreFactTableStructure.getStatMeasureColumns());
      cubeUpdatePreFactTableStructure.setStatMeasureSelectEntities(cubePreFactTableStructure
               .getStatMeasureSelectEntities());
      // this is the actual fact table created after de-dup process
      cubeUpdatePreFactTableStructure.setTableName(deDupPreFactTableName);
      return cubeUpdatePreFactTableStructure;
   }

   private String deDupPreFactTable (CubePreFactTableStructure cubePreFactTableStructure,
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext,
            CubeCreationPopulatedContext cubeCreationPopulatedContext) throws CubePreFactPopulationException,
            DataAccessException {
      int maxDbObjectLength = answersCatalogConfigurationContext.getMaxDBObjectLength();
      Asset targetAsset = cubeCreationPopulatedContext.getTargetAsset();
      DataSource targetDataSource = targetAsset.getDataSource();
      AssetProviderType targetProviderType = targetDataSource.getProviderType();
      String deDupFactTableName = cubePreFactTableStructure.getTableName() + UNDERSCORE + DE_DUP_TABLE_NAME_SUFFIX;
      deDupFactTableName = getAnswersCatalogDataAccessService().getUniqueNonExistentTableName(targetDataSource,
               deDupFactTableName, new ArrayList<String>(), maxDbObjectLength);
      QueryTable deDupeFactQueryTable = ExecueBeanManagementUtil.prepareQueryTable(deDupFactTableName, null,
               targetDataSource.getOwner());
      String createStatement = getQueryGenerationUtilService(targetProviderType).createTableCreateStatement(
               deDupeFactQueryTable);
      if (logger.isDebugEnabled()) {
         logger.debug("Create De-Dup Table Statement " + createStatement);
      }

      // sql table object for de-dup select query
      SQLTable deDupSQLTable = prepareSQLTableForDeDupSelectQuery(cubePreFactTableStructure);
      List<QueryColumn> queryColumns = deDupSQLTable.getColumns();
      QueryTable factQueryTable = ExecueBeanManagementUtil.prepareQueryTable(cubePreFactTableStructure.getTableName(),
               null, targetDataSource.getOwner());

      String selectStatement = getQueryGenerationUtilService(targetProviderType).createSelectStatement(factQueryTable,
               queryColumns, true);
      if (logger.isDebugEnabled()) {
         logger.debug("Select Statement " + selectStatement);
      }

      String deDupQuery = createStatement + SQL_SPACE_DELIMETER + SQL_AS_KEYWORD + SQL_SPACE_DELIMETER
               + selectStatement;
      if (logger.isDebugEnabled()) {
         logger.debug("De Dup Query " + deDupQuery);
      }
      getAnswersCatalogDataAccessService().executeDDLStatement(targetDataSource.getName(), deDupQuery);

      return deDupFactTableName;
   }

   private SQLTable prepareSQLTableForDeDupSelectQuery (CubePreFactTableStructure cubePreFactTableStructure) {
      // prepare list of columns for de-dup avoiding queryId column
      List<QueryColumn> deDupQueryColumns = new ArrayList<QueryColumn>();
      deDupQueryColumns.addAll(cubePreFactTableStructure.getFrequencyMeasureColumns());
      deDupQueryColumns.addAll(cubePreFactTableStructure.getSimpleLookupColumns());
      deDupQueryColumns.addAll(cubePreFactTableStructure.getRangeLookupColumns());
      deDupQueryColumns.addAll(cubePreFactTableStructure.getStatMeasureColumns());
      SQLTable deDupSQLTable = ExecueBeanManagementUtil.prepareSQLTableFromQueryColumns(cubePreFactTableStructure
               .getTableName(), deDupQueryColumns);
      return deDupSQLTable;
   }

   private ConceptColumnMapping getMatchedConceptColumnMapping (
            CubeCreationPopulatedContext cubeCreationPopulatedContext, String dimensionName) {
      List<ConceptColumnMapping> simpleLookupDimensions = cubeCreationPopulatedContext
               .getPopulatedSimpleLookupDimensions();
      ConceptColumnMapping matchedConceptColumnMapping = AnswersCatalogUtil.getMatchedDimensionMapping(dimensionName,
               simpleLookupDimensions);
      return matchedConceptColumnMapping;

   }

   private void enhanceDataTransferQueryObjectForCubeUpdate (ConceptColumnMapping dimensionConceptColumnMapping,
            CubePreFactTableDataTransferQueryInfo dataTransferQuery, List<Membr> addedMembers) {
      ConditionEntity conditionEntity = AnswersCatalogUtil.prepareConditionEntity(dimensionConceptColumnMapping
               .getQueryTable(), dimensionConceptColumnMapping.getQueryColumn(), AnswersCatalogUtil
               .getMemberValues(addedMembers));
      dataTransferQuery.getSelectQuery().addConditionEntity(conditionEntity);
   }

   private List<List<SelectEntity>> getValidDimensionValueBasedCombinations (
            List<List<SelectEntity>> dimensionCombinationSelectEntities,
            ConceptColumnMapping dimensionConceptColumnMapping) {
      List<List<SelectEntity>> validDimensionValueBasedCombinations = new ArrayList<List<SelectEntity>>();
      for (List<SelectEntity> dimensionCombinationSelectEntity : dimensionCombinationSelectEntities) {
         for (SelectEntity selectEntity : dimensionCombinationSelectEntity) {
            if (selectEntity.getTableColumn() != null
                     && selectEntity.getTableColumn().equals(dimensionConceptColumnMapping.getQueryTableColumn())) {
               validDimensionValueBasedCombinations.add(dimensionCombinationSelectEntity);
               break;
            }
         }
      }
      return validDimensionValueBasedCombinations;
   }

   private List<List<SelectEntity>> getCubeAllValueBasedCombinations (
            List<List<SelectEntity>> allDimensionCombinationSelectEntities,
            List<List<SelectEntity>> validDimensionBasedCombinations) {
      List<List<SelectEntity>> cubeAllValueBasedCombinations = new ArrayList<List<SelectEntity>>();
      cubeAllValueBasedCombinations.addAll(allDimensionCombinationSelectEntities);
      cubeAllValueBasedCombinations.removeAll(validDimensionBasedCombinations);
      return cubeAllValueBasedCombinations;
   }

   private List<CubePreFactTableDataTransferQueryInfo> buildQueriesList (
            CubeConfigurationContext cubeConfigurationContext, CubePreFactTableStructure cubePreFactTableStructure)
            throws CubePreFactPopulationException, CloneNotSupportedException {
      // preparing the combination of the dimension c(n,r)
      List<List<SelectEntity>> dimensionSelectEntityCombinations = generateCombinationsByDimensionSet(
               cubeConfigurationContext, cubePreFactTableStructure);
      return buildDataTransferQueries(cubePreFactTableStructure, dimensionSelectEntityCombinations);
   }

   private List<CubePreFactTableDataTransferQueryInfo> buildQueriesListForCubeUpdate (
            CubeConfigurationContext cubeConfigurationContext, CubePreFactTableStructure cubePreFactTableStructure,
            CubeCreationPopulatedContext cubeCreationPopulatedContext,
            CubeUpdationDimensionInfo cubeUpdationDimensionInfo) throws CubePreFactPopulationException,
            CloneNotSupportedException {
      List<CubePreFactTableDataTransferQueryInfo> queriesToExecute = new ArrayList<CubePreFactTableDataTransferQueryInfo>();
      List<Membr> addedMembers = cubeUpdationDimensionInfo.getAddedMembers();
      String dimensionName = cubeUpdationDimensionInfo.getDimensionName();
      // get the matching concept column mapping for dimension
      ConceptColumnMapping matchedConceptColumnMapping = getMatchedConceptColumnMapping(cubeCreationPopulatedContext,
               dimensionName);

      // preparing the combination of the dimension c(n,r)
      List<List<SelectEntity>> dimensionSelectEntityCombinations = generateCombinationsByDimensionSet(
               cubeConfigurationContext, cubePreFactTableStructure);

      // need to identify the valid combinations which needs to be executed.
      // lets say we just have deletion in dimension, in that case we dont need to execute the queries where valid
      // dimension value exists. If we have some addition to the members then we need to execute the qury where valid
      // dimemsion value is there for the added members only.
      // in any case, we need to execute the queries which has cube default value(all) at the dimension value

      List<List<SelectEntity>> validDimensionValueBasedCombinations = getValidDimensionValueBasedCombinations(
               dimensionSelectEntityCombinations, matchedConceptColumnMapping);

      List<List<SelectEntity>> cubeAllValueBasedCombinations = getCubeAllValueBasedCombinations(
               dimensionSelectEntityCombinations, validDimensionValueBasedCombinations);

      List<CubePreFactTableDataTransferQueryInfo> cubeAllValueBasedQueries = buildDataTransferQueries(
               cubePreFactTableStructure, cubeAllValueBasedCombinations);
      queriesToExecute.addAll(cubeAllValueBasedQueries);

      // do we need to execute valid dimension combinations or not is based on do we have added members or not in this
      // dimension
      if (ExecueCoreUtil.isCollectionNotEmpty(addedMembers) && addedMembers.size() > 0) {
         List<CubePreFactTableDataTransferQueryInfo> validDimensionBasedQueries = buildDataTransferQueriesForCubeUpdate(
                  cubePreFactTableStructure, validDimensionValueBasedCombinations, matchedConceptColumnMapping,
                  addedMembers);
         queriesToExecute.addAll(validDimensionBasedQueries);
      }
      return queriesToExecute;
   }

   private List<CubePreFactTableDataTransferQueryInfo> buildDataTransferQueries (
            CubePreFactTableStructure cubePreFactTableStructure,
            List<List<SelectEntity>> dimensionSelectEntityCombinations) throws CloneNotSupportedException {
      List<CubePreFactTableDataTransferQueryInfo> queries = new ArrayList<CubePreFactTableDataTransferQueryInfo>();
      for (List<SelectEntity> dimensionCombinationSelectEntities : dimensionSelectEntityCombinations) {
         CubePreFactTableDataTransferQueryInfo dataTransferQuery = populateDataTransferQueryObject(
                  cubePreFactTableStructure, dimensionCombinationSelectEntities);
         queries.add(dataTransferQuery);
      }
      return queries;
   }

   private boolean populatePreFactTable (CubePreFactTableStructure cubePreFactTableStructure,
            CubeCreationPopulatedContext cubeCreationPopulatedContext,
            CubeConfigurationContext cubeConfigurationContext, List<CubePreFactTableDataTransferQueryInfo> selectQueries)
            throws AnswersCatalogException, InterruptedException, ExecutionException {
      Asset targetAsset = cubeCreationPopulatedContext.getTargetAsset();
      DataSource targetDataSource = targetAsset.getDataSource();
      AssetProviderType targetProviderType = targetDataSource.getProviderType();

      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(cubePreFactTableStructure.getTableName(),
               null, targetDataSource.getOwner());

      SQLTable sequenceCreateInsertTableColumns = populateCreateInsertTableColumnsSequence(cubePreFactTableStructure);

      String cubePreFactCreateQuery = answersCatalogSQLQueryGenerationService.prepareCreateTableQuery(
               targetProviderType, queryTable, sequenceCreateInsertTableColumns.getColumns());
      if (logger.isDebugEnabled()) {
         logger.debug("Pre Fact Create Table Query " + cubePreFactCreateQuery);
      }
      return executeDataTransferQueries(cubeCreationPopulatedContext, cubeConfigurationContext,
               cubePreFactTableStructure, sequenceCreateInsertTableColumns, cubePreFactCreateQuery, selectQueries);
   }

   private boolean executeDataTransferQueries (CubeCreationPopulatedContext cubeCreationPopulatedContext,
            CubeConfigurationContext cubeConfigurationContext, CubePreFactTableStructure cubePreFactTableStructure,
            SQLTable sequenceCreateInsertTableColumns, String cubeCreateQuery,
            List<CubePreFactTableDataTransferQueryInfo> dataTransferQueries) throws AnswersCatalogException,
            InterruptedException, ExecutionException {

      // Create the pre-fact table and then invoke the ETL processes in parallel to avoid concurrency in pre-fact
      // creation
      getCubeCreationServiceHelper().createPreFactTableOnTarget(
               cubeCreationPopulatedContext.getTargetAsset().getDataSource(), cubePreFactTableStructure.getTableName(),
               cubeCreateQuery);

      // for each combination, we will generate select query based on that combination and insert the data into temp
      // table.
      // start a thread pool to process fractional tables parallelly.
      Integer queryCounter = 1;
      if (logger.isInfoEnabled()) {
         logger.info("Thread Pool is being created for data transfer queries");
      }
      ExecueFixedSizeThreadPoolManager threadPoolManager = null;
      try {
         threadPoolManager = new ExecueFixedSizeThreadPoolManager(cubeConfigurationContext
                  .getDataTransferQueriesExecutionThreadPoolSize(), cubeConfigurationContext.getThreadWaitTime());
         List<Future<? extends ThreadPoolTaskStatus>> taskStatusList = new ArrayList<Future<? extends ThreadPoolTaskStatus>>();
         for (CubePreFactTableDataTransferQueryInfo dataTransferQuery : dataTransferQueries) {
            Future<ThreadPoolTaskStatus> submitTask = threadPoolManager.submitTask(new DataTransferQueryExecution(
                     cubeCreationPopulatedContext, cubePreFactTableStructure, dataTransferQuery,
                     sequenceCreateInsertTableColumns, cubeCreateQuery, queryCounter));
            taskStatusList.add(submitTask);
            queryCounter++;
         }
         if (logger.isInfoEnabled()) {
            logger.info("Waiting for Thread Pool on data transfer queries to be completed");
         }
         threadPoolManager.waitTillComplete();
         if (logger.isInfoEnabled()) {
            logger.info("Thread Pool on data transfer queries got completed");
         }
         // if all tasks are successfully completed then only we can go to next steps
         boolean dataTransferSucceeded = threadPoolManager.doesAllTasksCompletedSuccessfully(taskStatusList);

         if (!dataTransferSucceeded) {
            logger.error("Data Transfer failed : " + threadPoolManager.composeFailureReason(taskStatusList));
         }
         return dataTransferSucceeded;
      } finally {
         if (threadPoolManager != null) {
            try {
               threadPoolManager.forceShutdown();
            } catch (Exception e) {
               // NOTE: We should not throw the exception from here
               logger.warn("COULDN'T COMPLETE FORCE FULL SHUT DOWN.. " + e.getMessage());
            }
         }
      }
   }

   class DataTransferQueryExecution implements Callable<ThreadPoolTaskStatus> {

      private CubeCreationPopulatedContext          cubeCreationPopulatedContext;
      private CubePreFactTableStructure             cubePreFactTableStructure;
      private CubePreFactTableDataTransferQueryInfo dataTransferQuery;
      private SQLTable                              sequenceCreateInsertTableColumns;
      private String                                cubeCreateQuery;
      private Integer                               queryCounter;

      public DataTransferQueryExecution (CubeCreationPopulatedContext cubeCreationPopulatedContext,
               CubePreFactTableStructure cubePreFactTableStructure,
               CubePreFactTableDataTransferQueryInfo dataTransferQuery, SQLTable sequenceCreateInsertTableColumns,
               String cubeCreateQuery, Integer queryCounter) {
         super();
         this.cubeCreationPopulatedContext = cubeCreationPopulatedContext;
         this.cubePreFactTableStructure = cubePreFactTableStructure;
         this.dataTransferQuery = dataTransferQuery;
         this.sequenceCreateInsertTableColumns = sequenceCreateInsertTableColumns;
         this.cubeCreateQuery = cubeCreateQuery;
         this.queryCounter = queryCounter;
      }

      @Override
      public ThreadPoolTaskStatus call () throws Exception {
         ThreadPoolTaskStatus threadPoolTaskStatus = new ThreadPoolTaskStatus();
         threadPoolTaskStatus.setTaskStatusType(ThreadPoolTaskStatusType.STARTED);
         try {
            dataTransferUsingSelectQuery(cubeCreationPopulatedContext, cubePreFactTableStructure, dataTransferQuery,
                     sequenceCreateInsertTableColumns, cubeCreateQuery, queryCounter);
            threadPoolTaskStatus.setTaskStatusType(ThreadPoolTaskStatusType.SUCCESS);
         } catch (Exception exception) {
            threadPoolTaskStatus.setTaskStatusType(ThreadPoolTaskStatusType.FAILED);
            threadPoolTaskStatus.setFailureReason(exception.getMessage());
            logger.error(exception, exception);
         }
         return threadPoolTaskStatus;
      }
   }

   private List<CubePreFactTableDataTransferQueryInfo> buildDataTransferQueriesForCubeUpdate (
            CubePreFactTableStructure cubePreFactTableStructure,
            List<List<SelectEntity>> dimensionSelectEntityCombinations,
            ConceptColumnMapping matchedConceptColumnMapping, List<Membr> addedMembers)
            throws CloneNotSupportedException {
      List<CubePreFactTableDataTransferQueryInfo> dataTransferQueries = buildDataTransferQueries(
               cubePreFactTableStructure, dimensionSelectEntityCombinations);
      for (CubePreFactTableDataTransferQueryInfo dataTransferQuery : dataTransferQueries) {
         enhanceDataTransferQueryObjectForCubeUpdate(matchedConceptColumnMapping, dataTransferQuery, addedMembers);
      }
      return dataTransferQueries;
   }

   private List<List<SelectEntity>> generateCombinationsByDimensionSet (
            CubeConfigurationContext cubeConfigurationContext, CubePreFactTableStructure cubePreFactTableStructure)
            throws CloneNotSupportedException {
      List<SelectEntity> cubeDimensionSelectEntities = new ArrayList<SelectEntity>();
      cubeDimensionSelectEntities.addAll(ExecueBeanCloneUtil.cloneSelectEntities(cubePreFactTableStructure
               .getSimpleLookupSelectEntities()));
      cubeDimensionSelectEntities.addAll(cubePreFactTableStructure.getRangeLookupSelectEntities());
      List<List<SelectEntity>> dimensionSelectEntityCombinations = getCubeCreationServiceHelper()
               .populateDimensionSelectEntityCombinations(cubeDimensionSelectEntities, cubeConfigurationContext);
      if (logger.isDebugEnabled()) {
         logger.debug("Total Combinations Generated " + dimensionSelectEntityCombinations.size());
      }
      return dimensionSelectEntityCombinations;
   }

   private void dataTransferUsingSelectQuery (CubeCreationPopulatedContext cubeCreationPopulatedContext,
            CubePreFactTableStructure cubePreFactTableStructure,
            CubePreFactTableDataTransferQueryInfo dataTransferQuery, SQLTable sequenceCreateInsertTableColumns,
            String cubeCreateQuery, Integer queryCounter) throws AnswersCatalogException {
      if (logger.isDebugEnabled()) {
         logger.debug("Processing Combination " + queryCounter);
      }
      Asset sourceAsset = cubeCreationPopulatedContext.getSourceAsset();
      Asset targetAsset = cubeCreationPopulatedContext.getTargetAsset();
      DataSource sourceDataSource = sourceAsset.getDataSource();
      DataSource targetDataSource = targetAsset.getDataSource();
      String preFactTableName = cubePreFactTableStructure.getTableName();
      AssetProviderType targetProviderType = targetDataSource.getProviderType();
      String cubeSelectQuery = answersCatalogSQLQueryGenerationService.prepareSelectQuery(sourceAsset,
               dataTransferQuery.getSelectQuery());
      if (logger.isDebugEnabled()) {
         logger.debug("Select Query " + cubeSelectQuery);
      }

      // if both locations are same, then insert into select, else etl based inserts
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(sequenceCreateInsertTableColumns
               .getTableName(), null, targetDataSource.getOwner());
      if (cubeCreationPopulatedContext.isTargetDataSourceSameAsSourceDataSource()) {
         String cubeInsertQuery = answersCatalogSQLQueryGenerationService.prepareInsertQuery(targetProviderType,
                  queryTable, sequenceCreateInsertTableColumns.getColumns());
         if (logger.isDebugEnabled()) {
            logger.debug("Insert Into Select Query " + cubeInsertQuery);
         }
         getCubeCreationServiceHelper().transferLocalData(targetDataSource, preFactTableName, cubeCreateQuery,
                  cubeInsertQuery, cubeSelectQuery);
      } else {
         Map<String, String> selectQueryAliases = populateSelectQueryAliases(cubePreFactTableStructure);
         String cubeInsertQuery = answersCatalogSQLQueryGenerationService.prepareETLInsertQuery(targetProviderType,
                  queryTable, sequenceCreateInsertTableColumns.getColumns(), selectQueryAliases);
         if (logger.isDebugEnabled()) {
            logger.debug("Insert ETL Query " + cubeInsertQuery);
         }
         // preparing the rollback statement
         ConditionEntity conditionEntity = AnswersCatalogUtil.prepareConditionEntity(queryTable,
                  cubePreFactTableStructure.getQueryIdColumn(), dataTransferQuery.getQueryId().toString());
         String rollbackQuery = getQueryGenerationUtilService(targetProviderType).createTableDeleteStatement(
                  conditionEntity);
         if (logger.isDebugEnabled()) {
            logger.debug("Rollback Query " + rollbackQuery);
         }
         getCubeCreationServiceHelper().transferRemoteData(sourceDataSource, targetDataSource, cubeInsertQuery,
                  cubeSelectQuery, cubeCreateQuery, rollbackQuery, preFactTableName);
      }
   }

   /**
    * This method populates the structure of pre fact table
    * 
    * @param cubeCreationPopulatedContext
    * @param cubeConfigurationContext
    * @param answersCatalogConfigurationContext
    * @param maxDbObjectLength
    * @param targetAsset
    * @return CubePreFactTableStructure
    * @throws DataAccessException
    */
   private CubePreFactTableStructure populatePreCubeTableStructure (
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext,
            CubeConfigurationContext cubeConfigurationContext, CubeCreationPopulatedContext cubeCreationPopulatedContext)
            throws DataAccessException {
      Asset targetAsset = cubeCreationPopulatedContext.getTargetAsset();
      int maxDbObjectLength = answersCatalogConfigurationContext.getMaxDBObjectLength();
      DataType measureColumnDataType = answersCatalogConfigurationContext.getMeasureColumnDataType();
      DataType frequencyColumnDataType = answersCatalogConfigurationContext.getFrequencyColumnDataType();
      int measurePrecisionValue = answersCatalogConfigurationContext.getMeasurePrecisionValue();
      int measureScaleValue = answersCatalogConfigurationContext.getMeasureScaleValue();
      List<StatType> stats = cubeConfigurationContext.getApplicableStats();
      String cubePreFactTableSuffix = cubeConfigurationContext.getCubePreFactTableSuffix();
      String cubeFactTableSuffix = cubeConfigurationContext.getCubeFactTablePrefix();
      String frequencyColumnPrefix = cubeConfigurationContext.getFrequencyColumnPrefix();
      String simpleLookupColumnPrefix = cubeConfigurationContext.getSimpleLookupColumnPrefix();
      String rangeLookupColumnPrefix = cubeConfigurationContext.getRangeLookupColumnPrefix();
      DataType dimensionDataType = cubeConfigurationContext.getDimensionDataType();
      Integer minDimensionPrecision = cubeConfigurationContext.getMinDimensionPrecision();
      QueryColumn configurationQueryIdColumn = cubeConfigurationContext.getQueryIdColumn();

      String assetName = targetAsset.getName();
      String finalPreCubeTableName = cubePreFactTableSuffix + UNDERSCORE + cubeFactTableSuffix + UNDERSCORE + assetName;

      finalPreCubeTableName = getAnswersCatalogDataAccessService().getUniqueNonExistentTableName(
               targetAsset.getDataSource(), finalPreCubeTableName, new ArrayList<String>(), maxDbObjectLength);

      List<String> existingColumnNames = new ArrayList<String>();
      // preparing the frequency measure columns and check for uniqueness.
      List<QueryColumn> frequencyMeasureQueryColumns = new ArrayList<QueryColumn>();
      for (ConceptColumnMapping frequencyMeasureMapping : cubeCreationPopulatedContext.getPopulatedFrequencyMeasures()) {
         QueryColumn frequencyMeasureQueryColumn = ExecueBeanCloneUtil.cloneQueryColumn(frequencyMeasureMapping
                  .getQueryColumn());
         AnswersCatalogUtil.cleanQueryColumn(frequencyMeasureQueryColumn);
         frequencyMeasureQueryColumn.setColumnName(frequencyColumnPrefix + UNDERSCORE
                  + frequencyMeasureQueryColumn.getColumnName());
         frequencyMeasureQueryColumn.setDataType(frequencyColumnDataType);
         frequencyMeasureQueryColumn.setPrecision(measurePrecisionValue);
         AnswersCatalogUtil.maintainColumnUniqueness(frequencyMeasureQueryColumn, existingColumnNames,
                  maxDbObjectLength);
         frequencyMeasureQueryColumns.add(frequencyMeasureQueryColumn);
      }

      // preparing the SL dimension columns and check for uniqueness.
      List<QueryColumn> simpleLookupColumns = new ArrayList<QueryColumn>();
      for (ConceptColumnMapping simpleLookupDimensionMapping : cubeCreationPopulatedContext
               .getPopulatedSimpleLookupDimensions()) {
         QueryColumn simpleLookupQueryColumn = ExecueBeanCloneUtil.cloneQueryColumn(simpleLookupDimensionMapping
                  .getQueryColumn());
         AnswersCatalogUtil.cleanQueryColumn(simpleLookupQueryColumn);
         simpleLookupQueryColumn.setColumnName(simpleLookupColumnPrefix + UNDERSCORE
                  + simpleLookupDimensionMapping.getTabl().getName() + UNDERSCORE
                  + simpleLookupQueryColumn.getColumnName());
         // adjust the precision
         getCubeCreationServiceHelper().adjustDimensionColumnPrecision(simpleLookupQueryColumn, dimensionDataType,
                  minDimensionPrecision);
         AnswersCatalogUtil.maintainColumnUniqueness(simpleLookupQueryColumn, existingColumnNames, maxDbObjectLength);
         simpleLookupColumns.add(simpleLookupQueryColumn);
      }

      // preparing the RL dimension columns and check for uniqueness.
      List<QueryColumn> rangeLookupColumns = new ArrayList<QueryColumn>();
      for (ConceptColumnMapping rangeLookupDimensionMapping : cubeCreationPopulatedContext
               .getPopulatedRangeLookupDimensions()) {
         QueryColumn rangeLookupQueryColumn = ExecueBeanCloneUtil.cloneQueryColumn(rangeLookupDimensionMapping
                  .getQueryColumn());
         AnswersCatalogUtil.cleanQueryColumn(rangeLookupQueryColumn);
         rangeLookupQueryColumn.setColumnName(rangeLookupColumnPrefix + UNDERSCORE
                  + rangeLookupDimensionMapping.getTabl().getName() + UNDERSCORE
                  + rangeLookupQueryColumn.getColumnName());
         getCubeCreationServiceHelper().adjustDimensionColumnPrecision(rangeLookupQueryColumn, dimensionDataType,
                  minDimensionPrecision);
         AnswersCatalogUtil.maintainColumnUniqueness(rangeLookupQueryColumn, existingColumnNames, maxDbObjectLength);
         rangeLookupColumns.add(rangeLookupQueryColumn);
      }

      // preparing the stat measure columns and check for uniqueness.
      List<QueryColumn> statMeasureColumns = new ArrayList<QueryColumn>();
      for (ConceptColumnMapping measureMapping : cubeCreationPopulatedContext.getPopulatedMeasures()) {
         for (StatType stat : stats) {
            QueryColumn measureQueryColumn = ExecueBeanCloneUtil.cloneQueryColumn(measureMapping.getQueryColumn());
            AnswersCatalogUtil.cleanQueryColumn(measureQueryColumn);
            measureQueryColumn.setColumnName(stat.getValue() + UNDERSCORE + measureQueryColumn.getColumnName());
            measureQueryColumn.setDataType(measureColumnDataType);
            measureQueryColumn.setPrecision(measurePrecisionValue);
            measureQueryColumn.setScale(measureScaleValue);
            AnswersCatalogUtil.maintainColumnUniqueness(measureQueryColumn, existingColumnNames, maxDbObjectLength);
            statMeasureColumns.add(measureQueryColumn);
         }
      }

      QueryColumn queryIdColumn = ExecueBeanCloneUtil.cloneQueryColumn(configurationQueryIdColumn);
      AnswersCatalogUtil.maintainColumnUniqueness(queryIdColumn, existingColumnNames, maxDbObjectLength);

      // preparing the select entities.
      List<String> existingAliases = new ArrayList<String>();

      // preparing the select entities for frequency measure columns.
      List<SelectEntity> frequencyMeasureSelectEntities = new ArrayList<SelectEntity>();
      for (ConceptColumnMapping columnMapping : cubeCreationPopulatedContext.getPopulatedFrequencyMeasures()) {
         SelectEntity frequencyMeasureSelectEntity = new SelectEntity();
         frequencyMeasureSelectEntity.setFunctionName(StatType.COUNT);
         frequencyMeasureSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
         frequencyMeasureSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(columnMapping
                  .getQueryTableColumn()));
         frequencyMeasureSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
         frequencyMeasureSelectEntities.add(frequencyMeasureSelectEntity);
      }

      // preparing the select entities for simple lookup dimension columns.
      List<SelectEntity> simpleLookupSelectEntities = new ArrayList<SelectEntity>();
      for (ConceptColumnMapping columnMapping : cubeCreationPopulatedContext.getPopulatedSimpleLookupDimensions()) {
         SelectEntity simpleLookupSelectEntity = new SelectEntity();
         simpleLookupSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
         simpleLookupSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(columnMapping
                  .getQueryTableColumn()));
         simpleLookupSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
         
         // NOTE: Date to string is needed on SLs as the target is converted 
         //    to String column always in order to support 'all' value
         if (DataType.DATE == columnMapping.getColumn().getDataType() ||
                  DataType.DATETIME == columnMapping.getColumn().getDataType() ||
                  DataType.TIME == columnMapping.getColumn().getDataType()) {
            simpleLookupSelectEntity.setDateAsStringRequired(true);
         }
         
         simpleLookupSelectEntities.add(simpleLookupSelectEntity);
      }
      // preparing the select entities for range lookup dimension columns.
      List<SelectEntity> rangeLookupSelectEntities = new ArrayList<SelectEntity>();
      for (ConceptColumnMapping columnMapping : cubeCreationPopulatedContext.getPopulatedRangeLookupDimensions()) {
         SelectEntity rangeLookupSelectEntity = new SelectEntity();
         rangeLookupSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
         rangeLookupSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(columnMapping
                  .getQueryTableColumn()));
         rangeLookupSelectEntity.setRange(columnMapping.getRangeDefinition());
         rangeLookupSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
         rangeLookupSelectEntities.add(rangeLookupSelectEntity);
      }
      // preparing the select entities for stat measure dimension columns.
      List<SelectEntity> statMeasureSelectEntities = new ArrayList<SelectEntity>();
      for (ConceptColumnMapping measureMapping : cubeCreationPopulatedContext.getPopulatedMeasures()) {
         for (StatType stat : stats) {
            SelectEntity statMeasureSelectEntity = new SelectEntity();
            statMeasureSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
            statMeasureSelectEntity.setFunctionName(stat);
            statMeasureSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(measureMapping
                     .getQueryTableColumn()));
            statMeasureSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));

            statMeasureSelectEntity.setRoundFunctionTargetColumn(AnswersCatalogUtil.getRoundFunctionTargetColumn(
                     measureColumnDataType, measurePrecisionValue, measureScaleValue));

            statMeasureSelectEntities.add(statMeasureSelectEntity);
         }
      }

      SelectEntity queyIdSelectEntity = new SelectEntity();
      queyIdSelectEntity.setType(SelectEntityType.VALUE);
      QueryValue queryValue = new QueryValue();
      queryValue.setValue(DEFAULT_QUERY_ID_VALUE);
      queryValue.setDataType(queryIdColumn.getDataType());
      queyIdSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
      queyIdSelectEntity.setQueryValue(queryValue);

      CubePreFactTableStructure cubePreFactTableStructure = new CubePreFactTableStructure();
      cubePreFactTableStructure.setFrequencyMeasureColumns(frequencyMeasureQueryColumns);
      cubePreFactTableStructure.setSimpleLookupColumns(simpleLookupColumns);
      cubePreFactTableStructure.setRangeLookupColumns(rangeLookupColumns);
      cubePreFactTableStructure.setStatMeasureColumns(statMeasureColumns);
      cubePreFactTableStructure.setTableName(finalPreCubeTableName);
      cubePreFactTableStructure.setQueryIdColumn(queryIdColumn);
      cubePreFactTableStructure.setFrequencyMeasureSelectEntities(frequencyMeasureSelectEntities);
      cubePreFactTableStructure.setSimpleLookupSelectEntities(simpleLookupSelectEntities);
      cubePreFactTableStructure.setRangeLookupSelectEntities(rangeLookupSelectEntities);
      cubePreFactTableStructure.setStatMeasureSelectEntities(statMeasureSelectEntities);
      cubePreFactTableStructure.setQueryIdSelectEntity(queyIdSelectEntity);
      return cubePreFactTableStructure;
   }

   /**
    * This method defines the sequence of create and insert query
    * 
    * @param cubePreFactTable
    * @return sequenceCreateInsertTableColumns
    */
   private SQLTable populateCreateInsertTableColumnsSequence (CubePreFactTableStructure cubePreFactTable) {
      List<QueryColumn> sequenceCreateInsertTableColumns = new ArrayList<QueryColumn>();
      sequenceCreateInsertTableColumns.addAll(cubePreFactTable.getFrequencyMeasureColumns());
      sequenceCreateInsertTableColumns.addAll(cubePreFactTable.getSimpleLookupColumns());
      sequenceCreateInsertTableColumns.addAll(cubePreFactTable.getRangeLookupColumns());
      sequenceCreateInsertTableColumns.addAll(cubePreFactTable.getStatMeasureColumns());
      sequenceCreateInsertTableColumns.add(cubePreFactTable.getQueryIdColumn());
      return ExecueBeanManagementUtil.prepareSQLTableFromQueryColumns(cubePreFactTable.getTableName(),
               sequenceCreateInsertTableColumns);
   }

   /**
    * This method populates the select query object. Other than all value it puts everything in group by also
    * 
    * @param cubePreFactTableStructure
    * @param dimensionCombinationSelectEntities
    * @return query
    * @throws CloneNotSupportedException
    */
   private CubePreFactTableDataTransferQueryInfo populateDataTransferQueryObject (
            CubePreFactTableStructure cubePreFactTableStructure, List<SelectEntity> dimensionCombinationSelectEntities)
            throws CloneNotSupportedException {
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      selectEntities.addAll(cubePreFactTableStructure.getFrequencyMeasureSelectEntities());
      selectEntities.addAll(dimensionCombinationSelectEntities);
      selectEntities.addAll(cubePreFactTableStructure.getStatMeasureSelectEntities());
      // get the current query id value, use it and increment it.
      Integer queryIdCurrentValue = cubePreFactTableStructure.getQueryIdCurrentValue();
      SelectEntity clonedQueryIdSelectEntity = ExecueBeanCloneUtil.cloneSelectEntity(cubePreFactTableStructure
               .getQueryIdSelectEntity());
      clonedQueryIdSelectEntity.getQueryValue().setValue(queryIdCurrentValue.toString());
      selectEntities.add(clonedQueryIdSelectEntity);
      cubePreFactTableStructure.setQueryIdCurrentValue(queryIdCurrentValue + 1);
      List<SelectEntity> groupEntities = new ArrayList<SelectEntity>();
      for (SelectEntity selectEntity : dimensionCombinationSelectEntities) {
         if (!SelectEntityType.VALUE.equals(selectEntity.getType())) {
            groupEntities.add(selectEntity);
         }
      }
      Query query = new Query();
      query.setSelectEntities(ExecueBeanCloneUtil.cloneSelectEntities(selectEntities));
      query.setGroupingEntities(ExecueBeanCloneUtil.cloneSelectEntities(groupEntities));
      return new CubePreFactTableDataTransferQueryInfo(query, queryIdCurrentValue);
   }

   /**
    * This method prepares the alias map which will be used by ETL
    * 
    * @param cubePreFactTableStructure
    * @return selectQueryAliases
    */
   private Map<String, String> populateSelectQueryAliases (CubePreFactTableStructure cubePreFactTableStructure) {
      Map<String, String> selectQueryAliases = new HashMap<String, String>();
      int index = 0;
      for (QueryColumn queryColumn : cubePreFactTableStructure.getFrequencyMeasureColumns()) {
         selectQueryAliases.put(queryColumn.getColumnName(), cubePreFactTableStructure
                  .getFrequencyMeasureSelectEntities().get(index++).getAlias());
      }
      index = 0;
      for (QueryColumn queryColumn : cubePreFactTableStructure.getSimpleLookupColumns()) {
         selectQueryAliases.put(queryColumn.getColumnName(), cubePreFactTableStructure.getSimpleLookupSelectEntities()
                  .get(index++).getAlias());
      }

      index = 0;
      for (QueryColumn queryColumn : cubePreFactTableStructure.getRangeLookupColumns()) {
         selectQueryAliases.put(queryColumn.getColumnName(), cubePreFactTableStructure.getRangeLookupSelectEntities()
                  .get(index++).getAlias());
      }
      index = 0;
      for (QueryColumn queryColumn : cubePreFactTableStructure.getStatMeasureColumns()) {
         selectQueryAliases.put(queryColumn.getColumnName(), cubePreFactTableStructure.getStatMeasureSelectEntities()
                  .get(index++).getAlias());
      }
      selectQueryAliases.put(cubePreFactTableStructure.getQueryIdColumn().getColumnName(), cubePreFactTableStructure
               .getQueryIdSelectEntity().getAlias());
      return selectQueryAliases;
   }

   /**
    * @return the answersCatalogSQLQueryGenerationService
    */
   public IAnswersCatalogSQLQueryGenerationService getAnswersCatalogSQLQueryGenerationService () {
      return answersCatalogSQLQueryGenerationService;
   }

   /**
    * @param answersCatalogSQLQueryGenerationService
    *           the answersCatalogSQLQueryGenerationService to set
    */
   public void setAnswersCatalogSQLQueryGenerationService (
            IAnswersCatalogSQLQueryGenerationService answersCatalogSQLQueryGenerationService) {
      this.answersCatalogSQLQueryGenerationService = answersCatalogSQLQueryGenerationService;
   }

   /**
    * @return the cubeCreationServiceHelper
    */
   public CubeCreationServiceHelper getCubeCreationServiceHelper () {
      return cubeCreationServiceHelper;
   }

   /**
    * @param cubeCreationServiceHelper
    *           the cubeCreationServiceHelper to set
    */
   public void setCubeCreationServiceHelper (CubeCreationServiceHelper cubeCreationServiceHelper) {
      this.cubeCreationServiceHelper = cubeCreationServiceHelper;
   }

   /**
    * @return the sdxRetrievalService
    */
   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   /**
    * @param sdxRetrievalService
    *           the sdxRetrievalService to set
    */
   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   private IQueryGenerationUtilService getQueryGenerationUtilService (AssetProviderType assetProviderType) {
      return queryGenerationUtilServiceFactory.getQueryGenerationUtilService(assetProviderType);
   }

   public QueryGenerationUtilServiceFactory getQueryGenerationUtilServiceFactory () {
      return queryGenerationUtilServiceFactory;
   }

   public void setQueryGenerationUtilServiceFactory (QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory) {
      this.queryGenerationUtilServiceFactory = queryGenerationUtilServiceFactory;
   }

   public IAnswersCatalogDataAccessService getAnswersCatalogDataAccessService () {
      return answersCatalogDataAccessService;
   }

   public void setAnswersCatalogDataAccessService (IAnswersCatalogDataAccessService answersCatalogDataAccessService) {
      this.answersCatalogDataAccessService = answersCatalogDataAccessService;
   }

}
