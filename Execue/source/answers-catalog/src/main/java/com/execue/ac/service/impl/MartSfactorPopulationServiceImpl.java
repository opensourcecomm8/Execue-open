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
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.execue.ac.bean.AnswersCatalogConfigurationContext;
import com.execue.ac.bean.BatchCountAlgorithmStaticInput;
import com.execue.ac.bean.ConceptColumnMapping;
import com.execue.ac.bean.MartConfigurationContext;
import com.execue.ac.bean.MartCreationInputInfo;
import com.execue.ac.bean.MartCreationOutputInfo;
import com.execue.ac.bean.MartCreationPopulatedContext;
import com.execue.ac.bean.MartFractionalDataSetTempTableStructure;
import com.execue.ac.bean.MartFractionalDatasetIslandInfo;
import com.execue.ac.bean.MartFractionalDatasetTableStructure;
import com.execue.ac.bean.MartPopulationTableStructure;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.MartSfactorPopulationException;
import com.execue.ac.helper.MartCreationServiceHelper;
import com.execue.ac.service.IAnswersCatalogDataAccessService;
import com.execue.ac.service.IAnswersCatalogSQLQueryGenerationService;
import com.execue.ac.service.IMartBatchCountPopulationService;
import com.execue.ac.service.IMartCreationConstants;
import com.execue.ac.service.IMartOperationalConstants;
import com.execue.ac.service.IMartSfactorPopulationService;
import com.execue.ac.util.AnswersCatalogUtil;
import com.execue.core.bean.ThreadPoolTaskStatus;
import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.FromEntity;
import com.execue.core.common.bean.querygen.LimitEntity;
import com.execue.core.common.bean.querygen.OrderEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryFormula;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.bean.querygen.SQLTable;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.ArithmeticOperatorType;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.OrderEntityType;
import com.execue.core.common.type.QueryFormulaOperandType;
import com.execue.core.common.type.QueryFormulaType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.service.ExecueFixedSizeThreadPoolManager;
import com.execue.core.type.ThreadPoolTaskStatusType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessException;

/**
 * This service represents the step3 of mart creation. It means create actual population table from each dimensional
 * table created in step2. Populate the sfactor for each record in each created table needs to be populated.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class MartSfactorPopulationServiceImpl implements IMartSfactorPopulationService, IMartCreationConstants,
         IMartOperationalConstants {

   private static final Logger                      logger = Logger.getLogger(MartSfactorPopulationServiceImpl.class);

   private IMartBatchCountPopulationService         martBatchCountPopulationService;
   private IAnswersCatalogDataAccessService         answersCatalogDataAccessService;
   private IAnswersCatalogSQLQueryGenerationService answersCatalogSQLQueryGenerationService;
   private MartCreationServiceHelper                martCreationServiceHelper;

   /**
    * This method creates the fractional dataset table for each dimension. It uses the information from temp table
    * created and process the islands one by one to create fractional dataset table.
    * 
    * @param martCreationOutputInfo
    * @param fractionalDataSetTempTable
    * @param dimension
    * @return martFractionalDatasetTableStructure
    * @throws MartSfactorPopulationException
    */
   public MartFractionalDatasetTableStructure sfactorPopulation (MartCreationOutputInfo martCreationOutputInfo,
            MartFractionalDataSetTempTableStructure fractionalDataSetTempTable, String dimension)
            throws MartSfactorPopulationException {
      MartCreationInputInfo martCreationInputInfo = martCreationOutputInfo.getMartCreationInputInfo();
      MartCreationContext martCreationContext = martCreationInputInfo.getMartCreationContext();
      MartFractionalDatasetTableStructure fractionalDatasetTableStructure = null;
      JobRequest jobRequest = martCreationContext.getJobRequest();
      Exception exception = null;
      JobOperationalStatus jobOperationalStatus = null;
      ExecueFixedSizeThreadPoolManager threadPoolManager = null;
      try {
         boolean sfactorPopulationSuccess = true;
         Asset sourceAsset = martCreationContext.getSourceAsset();
         Asset targetAsset = martCreationContext.getTargetAsset();
         DataSource sourceDataSource = sourceAsset.getDataSource();
         DataSource targetDataSource = targetAsset.getDataSource();
         AssetProviderType targetProviderType = targetDataSource.getProviderType();
         Integer numberOfSlices = martCreationInputInfo.getMartCreationInputParametersContext().getNumberOfSlices();
         BatchCountAlgorithmStaticInput batchCountAlgorithmStaticInput = martCreationInputInfo
                  .getBatchCountAlgorithmStaticInput();

         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  SFACTOR_POPULATION);
         // prepare the fractional dataset table structure.
         fractionalDatasetTableStructure = populateFractionalDatasetTableStructure(martCreationOutputInfo,
                  fractionalDataSetTempTable, dimension);

         // prepare the create table columns sequence
         SQLTable createTableColumnsSequence = populateCreateTableColumnsSequence(fractionalDatasetTableStructure);

         QueryTable fractionalDatasetQueryTable = ExecueBeanManagementUtil.prepareQueryTable(
                  fractionalDatasetTableStructure.getTableName(), null, targetDataSource.getOwner());

         // prepare the create table query string
         String createTableQuery = answersCatalogSQLQueryGenerationService.prepareCreateTableQuery(targetProviderType,
                  fractionalDatasetQueryTable, createTableColumnsSequence.getColumns());

         if (logger.isDebugEnabled()) {
            logger.debug("Create Table Query " + createTableQuery);
         }

         // prepare the insert table columns sequence
         List<QueryColumn> insertTableColumnsSequence = populateInsertTableColumnsSequence(fractionalDatasetTableStructure);

         // if target location is same as source location, we can build one join query as all the tables are in one
         // place
         // means source and target are in once place.
         if (martCreationContext.isTargetDataSourceSameAsSourceDataSource()) {

            // create insert query string
            String insertQuery = answersCatalogSQLQueryGenerationService.prepareInsertQuery(targetProviderType,
                     fractionalDatasetQueryTable, insertTableColumnsSequence);

            if (logger.isDebugEnabled()) {
               logger.debug("Insert Table Query " + insertQuery);
            }

            // prepare select query which contains join between source tables and target tables created for mart.
            Query selectJoinQuery = populateJoinQueryForSfactor(martCreationOutputInfo, fractionalDataSetTempTable,
                     fractionalDatasetTableStructure, dimension, numberOfSlices);

            // prepare select query string
            String selectJoinQueryString = answersCatalogSQLQueryGenerationService.prepareSelectQuery(sourceAsset,
                     selectJoinQuery);

            if (logger.isDebugEnabled()) {
               logger.debug("Select Table Join Based Query " + selectJoinQueryString);
            }

            // transfer the local data into fractional dataset table.
            getMartCreationServiceHelper().transferLocalData(targetDataSource, createTableQuery, insertQuery,
                     selectJoinQueryString);
         } else {
            // prepare insert select query aliases
            Map<String, String> selectQueryAliases = populateSelectQueryAliases(fractionalDatasetTableStructure);

            // prepare insert ETL based Query
            String insertETLQuery = answersCatalogSQLQueryGenerationService.prepareETLInsertQuery(targetProviderType,
                     fractionalDatasetQueryTable, insertTableColumnsSequence, selectQueryAliases);

            if (logger.isDebugEnabled()) {
               logger.debug("Insert ETL Query " + insertETLQuery);
            }

            // start processing each island.
            String tableName = fractionalDataSetTempTable.getTableName();
            QueryTable queryTable = AnswersCatalogUtil.createQueryTable(tableName, targetDataSource.getOwner());

            // preparing base query for reading island information.
            Query queryForDistributionDimensionAndSliceCount = populateQueryForDistributionDimensionAndSliceCount(
                     fractionalDataSetTempTable, queryTable);

            Long minIdColumnValue = answersCatalogDataAccessService.getStatBasedColumnValue(targetDataSource,
                     queryTable, fractionalDataSetTempTable.getIdColumn(), StatType.MINIMUM);
            Long maxIdColumnValue = answersCatalogDataAccessService.getStatBasedColumnValue(targetDataSource,
                     queryTable, fractionalDataSetTempTable.getIdColumn(), StatType.MAXIMUM);
            if (logger.isDebugEnabled()) {
               logger.debug("Min ID Value : " + minIdColumnValue);
            }
            if (logger.isDebugEnabled()) {
               logger.debug("Max ID Value : " + maxIdColumnValue);
            }
            if (minIdColumnValue != null && maxIdColumnValue != null) {
               // for each island which mean each row from temp table, read dimension, distribution and slice count
               // values check if sliceCount is same as numberOfSlices, it means the entire population needs to be
               // picked so we dont need to add population condition, else we need to get total records count based on
               // sliceCount
               // from population table. Read those many records and then batch by batch, we need to run select query
               // and dump the data into fractional dataset table.

               // start a thread pool to process all islands and all the batches parallelly. The number of threads will be
               // configured number lets say max. of 5 threads
               if (logger.isInfoEnabled()) {
                  logger.info("Thread Pool is being Created for pulling Population Records for [" + dimension + "]");
               }
               threadPoolManager = new ExecueFixedSizeThreadPoolManager(martCreationInputInfo
                        .getMartConfigurationContext().getBatchDataTransferThreadPoolSize(), martCreationInputInfo
                        .getMartConfigurationContext().getThreadWaitTime());
               List<Future<? extends ThreadPoolTaskStatus>> taskStatusList = new ArrayList<Future<? extends ThreadPoolTaskStatus>>();

               int maxTasksAllowedPerBatch = martCreationInputInfo.getMartConfigurationContext()
                        .getMaxTasksAllowedPerThreadPool();
               int taskCounter = 0;

               // Loop over all the islands
               for (Long idColumnValue = minIdColumnValue; idColumnValue <= maxIdColumnValue; idColumnValue++) {
                  if (logger.isDebugEnabled()) {
                     logger.debug("Processing Island " + idColumnValue);
                  }

                  // read the island information
                  MartFractionalDatasetIslandInfo fractionalDatasetIslandInfo = populateFractionalDatasetIslandInfo(
                           targetAsset, queryTable, fractionalDataSetTempTable,
                           queryForDistributionDimensionAndSliceCount, idColumnValue);
                  Integer sliceCount = fractionalDatasetIslandInfo.getSliceCount();
                  // calculate the sfactor value
                  /*
                   * SAFCTOR is representation value that one population record in sampled mart is equivalent to the how
                   * many population records in source warehouse population The formula for SFACTOR will be total number
                   * of slices that the source warehouse is cut into divided by number of slices were picked up in
                   * building the mart
                   */
                  Double sfactor = (double) numberOfSlices / (double) sliceCount;

                  // prepare conditions for distribution and dimension from source asset using island information
                  List<ConditionEntity> conditionalEntitiesForDistributionsAndDimension = prepareConditionalEntitiesForDistributionsAndDimension(
                           martCreationInputInfo, dimension, fractionalDatasetIslandInfo.getDistributionValues(),
                           fractionalDatasetIslandInfo.getDimensionValue());

                  // prepare default sfactor select query when numberOfSlices is same as SliceCount, means whole of the
                  // population needs to be considered
                  String defaultSfactorSelectQuery = prepareDefaultSfactorSelectQuery(sourceAsset,
                           fractionalDatasetTableStructure, conditionalEntitiesForDistributionsAndDimension);

                  // if sfactor value is default, then we can transfer the data using ETL.
                  if (sfactor.equals(DEFAULT_SFACTOR_VALUE)) {
                     if (logger.isDebugEnabled()) {
                        logger
                                 .debug("Default Sfactor Select Query When slices to be picked is same as number of slices"
                                          + defaultSfactorSelectQuery);
                     }

                     getMartCreationServiceHelper().transferRemoteData(sourceDataSource, targetDataSource,
                              insertETLQuery, defaultSfactorSelectQuery, createTableQuery,
                              fractionalDatasetTableStructure.getTableName());
                  } else {
                     // prepare actual sfactor select query
                     Query actualSfactorSelectQuery = populateSfactorQueryObject(fractionalDatasetTableStructure,
                              sfactor);

                     // get the total count of records based on slice count.
                     Long recordCountBasedOnSliceCount = getTotalRecordCountBasedOnSliceCount(martCreationOutputInfo,
                              sliceCount);

                     if (logger.isDebugEnabled()) {
                        logger.debug("Total Record count " + recordCountBasedOnSliceCount);
                     }
                     if (recordCountBasedOnSliceCount > 0) {
                        // build base query for getting the records from population table based on slice count.
                        Query baseQueryForRecordsPopulationQuery = prepareRecordsPopulationQuery(
                                 martCreationOutputInfo, sliceCount);

                        // calculate the batch size
                        Integer batchSize = getMartBatchCountPopulationService().populateBatchCountForPopulation(
                                 batchCountAlgorithmStaticInput, defaultSfactorSelectQuery.length());

                        if (logger.isDebugEnabled()) {
                           logger.debug("Batch Size " + batchSize);
                        }

                        if (martCreationInputInfo.getMartConfigurationContext()
                                 .isSplitBatchDataTransferToAvoidSubConditions()) {
                           // based on flag override this value
                           batchSize = batchCountAlgorithmStaticInput.getMaxAllowedExpressionsInCondition();
                        }

                        // batch by batch, we will get the population data and add population as well as distribution,
                        // dimension conditions to base actual sfactor query
                        for (Long i = 1L; i <= recordCountBasedOnSliceCount; i = i + batchSize) {
                           taskCounter++;
                           LimitEntity limitEntity = new LimitEntity();
                           limitEntity.setStartingNumber(i);
                           limitEntity.setEndingNumber(i + batchSize - 1);

                           Query clonedBaseQueryForRecordsPopulationQuery = ExecueBeanCloneUtil
                                    .cloneQuery(baseQueryForRecordsPopulationQuery);
                           Query clonedActualSfactorSelectQuery = ExecueBeanCloneUtil
                                    .cloneQuery(actualSfactorSelectQuery);

                           Future<ThreadPoolTaskStatus> submitTask = threadPoolManager
                                    .submitTask(new BatchRemoteDataTransfer(martCreationInputInfo,
                                             clonedBaseQueryForRecordsPopulationQuery, limitEntity,
                                             clonedActualSfactorSelectQuery,
                                             conditionalEntitiesForDistributionsAndDimension, insertETLQuery,
                                             createTableQuery, fractionalDatasetTableStructure));

                           taskStatusList.add(submitTask);

                           if (logger.isDebugEnabled()) {
                              logger.debug("Task submitted for Island [" + idColumnValue + "] of [" + dimension
                                       + "] from [" + limitEntity.getStartingNumber() + "] to ["
                                       + limitEntity.getEndingNumber() + "] population records");
                           }

                           if (maxTasksAllowedPerBatch <= taskCounter) {
                              threadPoolManager.waitTillTaskComplete(taskStatusList);
                              taskStatusList = new ArrayList<Future<? extends ThreadPoolTaskStatus>>();
                              // reset the counter
                              taskCounter = 0;
                              // if all tasks are successfully completed , it means extraction was successful.
                              if (!threadPoolManager.doesAllTasksCompletedSuccessfully(taskStatusList)) {
                                 logger
                                          .error("Sfactor Population failed for one of the tasks for batch data transfer : "
                                                   + threadPoolManager.composeFailureReason(taskStatusList));
                                 sfactorPopulationSuccess = false;
                                 throw new MartSfactorPopulationException(
                                          AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_SFACTOR_POPULATION_EXCEPTION_CODE,
                                          "Sfactor Population failed for one of the tasks for batch data transfer : "
                                                   + threadPoolManager.composeFailureReason(taskStatusList));
                              }
                           }
                        }
                     }
                  }
               } // End of for Loop on islands 
               if (logger.isInfoEnabled()) {
                  logger.info("Waiting for Thread Pool on pulling Population Records for [" + dimension
                           + "] to be completed");
               }
               threadPoolManager.waitTillComplete();
               if (logger.isInfoEnabled()) {
                  logger.info("Thread Pool on pulling Population Records for [" + dimension + "] got completed");
               }
               // if all tasks are successfully completed , it means extraction was successful.
               if (!threadPoolManager.doesAllTasksCompletedSuccessfully(taskStatusList)) {
                  threadPoolManager.forceShutdown();
                  logger.error("Sfactor Population failed for one of the tasks for batch data transfer : "
                           + threadPoolManager.composeFailureReason(taskStatusList));
                  sfactorPopulationSuccess = false;
                  throw new MartSfactorPopulationException(
                           AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_SFACTOR_POPULATION_EXCEPTION_CODE,
                           "Sfactor Population failed for one of the tasks for batch data transfer : "
                                    + threadPoolManager.composeFailureReason(taskStatusList));
               }
            }
         }

         if (sfactorPopulationSuccess) {
            // NOTE: Create Indexes on FractionalDatasetTable
            createIndexesOnFactionalDatasetTable(martCreationInputInfo.getAnswersCatalogConfigurationContext(),
                     martCreationContext, fractionalDatasetTableStructure);
            getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
         }
      } catch (AnswersCatalogException e) {
         exception = e;
         throw new MartSfactorPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_SFACTOR_POPULATION_EXCEPTION_CODE, e);
      } catch (Exception e) {
         exception = e;
         throw new MartSfactorPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_SFACTOR_POPULATION_EXCEPTION_CODE, e);
      } finally {
         if (threadPoolManager != null) {
            try {
               threadPoolManager.forceShutdown();
            } catch (Exception e) {
               // NOTE: We should not throw the exception from here
               logger.warn("COULDN'T COMPLETE FORCE FULL SHUT DOWN.. " + e.getMessage());
            }
         }
         if (jobOperationalStatus != null && exception != null) {
            try {
               getMartCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new MartSfactorPopulationException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return fractionalDatasetTableStructure;
   }

   class BatchRemoteDataTransfer implements Callable<ThreadPoolTaskStatus> {

      private MartCreationInputInfo               martCreationInputInfo;
      private Query                               baseQueryForRecordsPopulationQuery;
      private LimitEntity                         limitEntity;
      private Query                               actualSfactorSelectQuery;
      private List<ConditionEntity>               conditionalEntitiesForDistributionsAndDimension;
      private String                              insertETLQuery;
      private String                              createTableQuery;
      private MartFractionalDatasetTableStructure fractionalDatasetTableStructure;

      public BatchRemoteDataTransfer (MartCreationInputInfo martCreationInputInfo,
               Query baseQueryForRecordsPopulationQuery, LimitEntity limitEntity, Query actualSfactorSelectQuery,
               List<ConditionEntity> conditionalEntitiesForDistributionsAndDimension, String insertETLQuery,
               String createTableQuery, MartFractionalDatasetTableStructure fractionalDatasetTableStructure) {
         super();
         this.martCreationInputInfo = martCreationInputInfo;
         this.baseQueryForRecordsPopulationQuery = baseQueryForRecordsPopulationQuery;
         this.limitEntity = limitEntity;
         this.actualSfactorSelectQuery = actualSfactorSelectQuery;
         this.conditionalEntitiesForDistributionsAndDimension = conditionalEntitiesForDistributionsAndDimension;
         this.insertETLQuery = insertETLQuery;
         this.createTableQuery = createTableQuery;
         this.fractionalDatasetTableStructure = fractionalDatasetTableStructure;
      }

      @Override
      public ThreadPoolTaskStatus call () throws Exception {
         ThreadPoolTaskStatus threadPoolTaskStatus = new ThreadPoolTaskStatus();
         threadPoolTaskStatus.setTaskStatusType(ThreadPoolTaskStatusType.STARTED);
         try {
            batchRemoteDataTransfer(martCreationInputInfo, baseQueryForRecordsPopulationQuery, limitEntity,
                     actualSfactorSelectQuery, conditionalEntitiesForDistributionsAndDimension, insertETLQuery,
                     createTableQuery, fractionalDatasetTableStructure);
            threadPoolTaskStatus.setTaskStatusType(ThreadPoolTaskStatusType.SUCCESS);
         } catch (Exception exception) {
            threadPoolTaskStatus.setTaskStatusType(ThreadPoolTaskStatusType.FAILED);
            threadPoolTaskStatus.setFailureReason(exception.getMessage());
            logger.error(exception, exception);
         }
         return threadPoolTaskStatus;
      }
   }

   private void batchRemoteDataTransfer (MartCreationInputInfo martCreationInputInfo,
            Query baseQueryForRecordsPopulationQuery, LimitEntity limitEntity, Query actualSfactorSelectQuery,
            List<ConditionEntity> conditionalEntitiesForDistributionsAndDimension, String insertETLQuery,
            String createTableQuery, MartFractionalDatasetTableStructure fractionalDatasetTableStructure)
            throws MartSfactorPopulationException {
      try {
         MartCreationPopulatedContext martCreationPopulatedContext = martCreationInputInfo
                  .getMartCreationPopulatedContext();
         Asset sourceAsset = martCreationPopulatedContext.getSourceAsset();
         Asset targetAsset = martCreationPopulatedContext.getTargetAsset();
         DataSource sourceDataSource = sourceAsset.getDataSource();
         DataSource targetDataSource = targetAsset.getDataSource();
         BatchCountAlgorithmStaticInput batchCountAlgorithmStaticInput = martCreationInputInfo
                  .getBatchCountAlgorithmStaticInput();
         // add condition entity for population.
         ConditionEntity conditionalEntityForPopulation = getMartCreationServiceHelper()
                  .prepareConditionalEntityForPopulation(martCreationInputInfo, targetAsset,
                           baseQueryForRecordsPopulationQuery, limitEntity,
                           batchCountAlgorithmStaticInput.getMaxAllowedExpressionsInCondition());

         // prepare actual sfactor based select query
         String actualSfactorSelectQueryString = prepareActualSfactorSelectQuery(sourceAsset, actualSfactorSelectQuery,
                  conditionalEntitiesForDistributionsAndDimension, conditionalEntityForPopulation);

         if (logger.isDebugEnabled()) {
            logger.debug("Actual Sfactor Select Query " + actualSfactorSelectQueryString);
         }

         // transfer remote data
         getMartCreationServiceHelper().transferRemoteData(sourceDataSource, targetDataSource, insertETLQuery,
                  actualSfactorSelectQueryString, createTableQuery, fractionalDatasetTableStructure.getTableName());

      } catch (AnswersCatalogException e) {
         throw new MartSfactorPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_SFACTOR_POPULATION_EXCEPTION_CODE, e);
      } catch (DataAccessException e) {
         throw new MartSfactorPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_SFACTOR_POPULATION_EXCEPTION_CODE, e);
      } catch (Exception e) {
         throw new MartSfactorPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_SFACTOR_POPULATION_EXCEPTION_CODE, e);
      }

   }

   private void createIndexesOnFactionalDatasetTable (
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext,
            MartCreationContext martCreationContext, MartFractionalDatasetTableStructure fractionalDatasetTableStructure)
            throws DataAccessException {
      DataSource targetDataSource = martCreationContext.getTargetAsset().getDataSource();

      // Index on Population column
      getAnswersCatalogDataAccessService().createIndexOnTable(
               targetDataSource,
               ExecueBeanManagementUtil.prepareSQLIndex(fractionalDatasetTableStructure.getTableName(),
                        fractionalDatasetTableStructure.getPopulationColumn().getColumnName()),
               answersCatalogConfigurationContext.getMaxDBObjectLength());

   }

   /**
    * This method prepares the select query from source using condition entities for distribution and dimension values.
    * It uses DEFAULT_SFACTOR_VALUE as one of the selects and other select is population.
    * 
    * @param sourceAsset
    * @param fractionalDatasetTableStructure
    * @param conditionalEntitiesForDistributionsAndDimension
    * @return defaultSfactorSelectQueryForSource
    * @throws AnswersCatalogException
    */
   private String prepareDefaultSfactorSelectQuery (Asset sourceAsset,
            MartFractionalDatasetTableStructure fractionalDatasetTableStructure,
            List<ConditionEntity> conditionalEntitiesForDistributionsAndDimension) throws AnswersCatalogException {
      Query defaultSfactorSelectQuery = populateSfactorQueryObject(fractionalDatasetTableStructure,
               DEFAULT_SFACTOR_VALUE);
      defaultSfactorSelectQuery.setWhereEntities(conditionalEntitiesForDistributionsAndDimension);
      String defaultSfactorSelectQueryForSource = answersCatalogSQLQueryGenerationService.prepareSelectQuery(
               sourceAsset, defaultSfactorSelectQuery);
      return defaultSfactorSelectQueryForSource;
   }

   /**
    * This method prepares the select query from source using condition entities for distribution, dimension and
    * population values. It uses actual sfactor as one of the selects and other as population.
    * 
    * @param sourceAsset
    * @param actualSfactorSelectQuery
    * @param conditionalEntitiesForDistributionsAndDimension
    * @param conditionalEntityForPopulation
    * @return actualSfactorSelectQueryString
    * @throws AnswersCatalogException
    */
   private String prepareActualSfactorSelectQuery (Asset sourceAsset, Query actualSfactorSelectQuery,
            List<ConditionEntity> conditionalEntitiesForDistributionsAndDimension,
            ConditionEntity conditionalEntityForPopulation) throws AnswersCatalogException {
      List<ConditionEntity> conditionEntities = new ArrayList<ConditionEntity>();
      conditionEntities.add(conditionalEntityForPopulation);
      conditionEntities.addAll(conditionalEntitiesForDistributionsAndDimension);
      actualSfactorSelectQuery.setWhereEntities(conditionEntities);
      String actualSfactorSelectQueryString = answersCatalogSQLQueryGenerationService.prepareSelectQuery(sourceAsset,
               actualSfactorSelectQuery);
      return actualSfactorSelectQueryString;
   }

   /**
    * This method gets the total number of records in population table based on slice count.
    * 
    * @param martCreationOutputInfo
    * @param sliceCount
    * @return recordCount
    * @throws Exception
    */
   private Long getTotalRecordCountBasedOnSliceCount (MartCreationOutputInfo martCreationOutputInfo, Integer sliceCount)
            throws Exception {
      Asset targetAsset = martCreationOutputInfo.getMartCreationInputInfo().getMartCreationPopulatedContext()
               .getTargetAsset();
      Query countPopulationQuery = prepareCountPopulationQuery(martCreationOutputInfo, sliceCount);
      String countPopulationQueryString = answersCatalogSQLQueryGenerationService.prepareSelectQuery(targetAsset,
               countPopulationQuery);
      ExeCueResultSet resultSet = answersCatalogDataAccessService.executeSQLQuery(targetAsset.getDataSource(),
               countPopulationQuery, countPopulationQueryString);
      // move to record
      resultSet.next();
      Long recordCount = resultSet.getLong(0);
      return recordCount;
   }

   /**
    * This method reads one island information represented by one unique id pased to it.
    * 
    * @param targetAsset
    * @param fractionalDatasetTempQueryTable
    * @param fractionalDataSetTempTableStructure
    * @param queryForDistributionDimensionAndSliceCount
    * @param idColumnValue
    * @return martFractionalDatasetIslandInfo
    * @throws Exception
    */
   private MartFractionalDatasetIslandInfo populateFractionalDatasetIslandInfo (Asset targetAsset,
            QueryTable fractionalDatasetTempQueryTable,
            MartFractionalDataSetTempTableStructure fractionalDataSetTempTableStructure,
            Query queryForDistributionDimensionAndSliceCount, Long idColumnValue) throws Exception {
      // clean the base query from previous trace.
      queryForDistributionDimensionAndSliceCount.setWhereEntities(null);
      queryForDistributionDimensionAndSliceCount.setFromEntities(null);

      int distributionColumnsSize = fractionalDataSetTempTableStructure.getDistributionColumns().size();
      QueryColumn idColumn = fractionalDataSetTempTableStructure.getIdColumn();
      ConditionEntity conditionEntity = AnswersCatalogUtil.prepareConditionEntity(fractionalDatasetTempQueryTable,
               idColumn, idColumnValue.toString());
      // add id based condition entity.
      queryForDistributionDimensionAndSliceCount.addConditionEntity(conditionEntity);
      String selectQueryForTempTable = answersCatalogSQLQueryGenerationService.prepareSelectQuery(targetAsset,
               queryForDistributionDimensionAndSliceCount);
      ExeCueResultSet exeCueResultSet = answersCatalogDataAccessService.executeSQLQuery(targetAsset.getDataSource(),
               queryForDistributionDimensionAndSliceCount, selectQueryForTempTable);
      // at the max there is only 1 row possible;
      exeCueResultSet.next();
      List<String> distributionValues = new ArrayList<String>();
      for (int index = 0; index < distributionColumnsSize; index++) {
         distributionValues.add(exeCueResultSet.getString(index));
      }
      String dimensionValue = exeCueResultSet.getString(distributionColumnsSize);
      Integer sliceCount = exeCueResultSet.getInt(distributionColumnsSize + 1);
      // prepare island information.
      return new MartFractionalDatasetIslandInfo(distributionValues, dimensionValue, sliceCount);
   }

   /**
    * This method will be used when source and target locations are same. It contains join between population table from
    * target, distribution and dimension lookup tables from source, join between population table from source to
    * population table at target. It adds condition for slice count < slice number. It calculates sfactor on the fly.
    * 
    * @param martCreationOutputInfo
    * @param fractionalDataSetTempTable
    * @param fractionalDatasetTable
    * @param dimensionName
    * @param numberOfSlices
    * @return query
    */
   private Query populateJoinQueryForSfactor (MartCreationOutputInfo martCreationOutputInfo,
            MartFractionalDataSetTempTableStructure fractionalDataSetTempTable,
            MartFractionalDatasetTableStructure fractionalDatasetTable, String dimensionName, Integer numberOfSlices) {

      MartCreationInputInfo martCreationInputInfo = martCreationOutputInfo.getMartCreationInputInfo();
      MartCreationPopulatedContext martCreationPopulatedContext = martCreationInputInfo
               .getMartCreationPopulatedContext();
      // prepare existing aliases
      List<String> existingAliases = new ArrayList<String>();
      for (ConceptColumnMapping distributionConceptColumnMapping : martCreationPopulatedContext
               .getSourceAssetMappingInfo().getPopulatedDistributions()) {
         existingAliases.add(distributionConceptColumnMapping.getQueryTable().getAlias());
      }
      List<ConceptColumnMapping> populatedProminentDimensions = martCreationPopulatedContext
               .getSourceAssetMappingInfo().getPopulatedProminentDimensions();
      ConceptColumnMapping matchedDimensionMapping = AnswersCatalogUtil.getMatchedDimensionMapping(dimensionName,
               populatedProminentDimensions);
      existingAliases.add(matchedDimensionMapping.getQueryTable().getAlias());
      ConceptColumnMapping populatedPopulation = martCreationPopulatedContext.getSourceAssetMappingInfo()
               .getPopulatedPopulation();
      existingAliases.add(populatedPopulation.getQueryTable().getAlias());

      // prepare population table and sd table alias.
      MartPopulationTableStructure populationTable = martCreationOutputInfo.getPopulationTable();
      QueryTable populationQueryTable = AnswersCatalogUtil.createQueryTable(populationTable.getTableName(),
               existingAliases, martCreationInputInfo.getMartCreationContext().getTargetAsset().getDataSource()
                        .getOwner());
      QueryTable fractionalDatasetTempQueryTable = AnswersCatalogUtil.createQueryTable(fractionalDataSetTempTable
               .getTableName(), existingAliases, martCreationInputInfo.getMartCreationContext().getTargetAsset()
               .getDataSource().getOwner());
      // preparation of query
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      selectEntities.add(fractionalDatasetTable.getPopulationSelectEntity());
      List<String> existingSelectEntityAliases = new ArrayList<String>();
      existingSelectEntityAliases.add(fractionalDatasetTable.getPopulationSelectEntity().getAlias());

      QueryColumn sliceCountColumn = fractionalDataSetTempTable.getSliceCountColumn();
      QueryTableColumn sliceCountQueryTableColumn = ExecueBeanCloneUtil.cloneQueryTableColumn(ExecueBeanManagementUtil
               .prepareQueryTableColumn(sliceCountColumn, fractionalDatasetTempQueryTable));
      QueryFormula queryFormula = new QueryFormula();
      queryFormula.setQueryFormulaType(QueryFormulaType.DYNAMIC);
      queryFormula.setArithmeticOperatorType(ArithmeticOperatorType.DIVISION);
      queryFormula.setFirstOperandType(QueryFormulaOperandType.TABLE_COLUMN);
      queryFormula.setFirstOperandQueryTableColumn(sliceCountQueryTableColumn);
      queryFormula.setSecondOperandType(QueryFormulaOperandType.VALUE);
      queryFormula.setSecondOperandValue(numberOfSlices.toString());
      SelectEntity sfactorSelectEntity = new SelectEntity();
      sfactorSelectEntity.setType(SelectEntityType.FORMULA);
      sfactorSelectEntity.setFormula(queryFormula);

      sfactorSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingSelectEntityAliases));
      selectEntities.add(sfactorSelectEntity);

      // update object with new select entity;
      fractionalDatasetTable.setSfactorSelectEntity(sfactorSelectEntity);

      // conditional entities.
      List<ConditionEntity> conditionEntities = new ArrayList<ConditionEntity>();

      // join between population tables from source to target
      conditionEntities.add(AnswersCatalogUtil.prepareConditionEntity(populationQueryTable, populationTable
               .getPopulationColumn(), populatedPopulation.getQueryTable(), populatedPopulation.getQueryColumn()));

      // join between dimension on source to dimension on target
      conditionEntities.add(AnswersCatalogUtil.prepareConditionEntity(fractionalDatasetTempQueryTable,
               fractionalDataSetTempTable.getDimensionColumn(), matchedDimensionMapping.getQueryTable(),
               matchedDimensionMapping.getQueryColumn()));

      // join between distribution from source to target
      int index = 0;
      for (ConceptColumnMapping distributionConceptColumnMapping : martCreationPopulatedContext
               .getSourceAssetMappingInfo().getPopulatedDistributions()) {
         conditionEntities.add(AnswersCatalogUtil.prepareConditionEntity(fractionalDatasetTempQueryTable,
                  fractionalDataSetTempTable.getDistributionColumns().get(index++), distributionConceptColumnMapping
                           .getQueryTable(), distributionConceptColumnMapping.getQueryColumn()));
      }

      // condition for slice number has to be less than slice count from fractional dataset table.
      conditionEntities.add(AnswersCatalogUtil.prepareConditionEntity(populationQueryTable, populationTable
               .getSliceNumberColumn(), fractionalDatasetTempQueryTable, fractionalDataSetTempTable
               .getSliceCountColumn(), OperatorType.LESS_THAN_EQUALS));

      Query query = new Query();
      query.setSelectEntities(selectEntities);
      query.setWhereEntities(conditionEntities);
      return query;
   }

   /**
    * This method prepares the query which can fetch the records from population table based on slice count.
    * 
    * @param martCreationOutputInfo
    * @param sliceCount
    * @return recordPopulationQuery
    */
   private Query prepareRecordsPopulationQuery (MartCreationOutputInfo martCreationOutputInfo, Integer sliceCount) {
      MartPopulationTableStructure populationTable = martCreationOutputInfo.getPopulationTable();
      DataSource dataSource = martCreationOutputInfo.getMartCreationInputInfo().getMartCreationContext()
               .getTargetAsset().getDataSource();
      QueryTable queryTable = AnswersCatalogUtil
               .createQueryTable(populationTable.getTableName(), dataSource.getOwner());
      QueryColumn idColumn = populationTable.getIdColumn();
      QueryColumn populationColumn = populationTable.getPopulationColumn();
      QueryColumn sliceNumberColumn = populationTable.getSliceNumberColumn();

      // preparing the select entities.
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      SelectEntity populationSelectEntity = new SelectEntity();
      populationSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      populationSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(ExecueBeanManagementUtil
               .prepareQueryTableColumn(populationColumn, queryTable)));
      populationSelectEntity.setAlias(ExecueCoreUtil.getAlias(new ArrayList<String>()));
      selectEntities.add(populationSelectEntity);

      // preparing the condition entities.
      List<ConditionEntity> conditionEntities = new ArrayList<ConditionEntity>();
      conditionEntities.add(AnswersCatalogUtil.prepareConditionEntity(queryTable, sliceNumberColumn, sliceCount
               .toString(), OperatorType.LESS_THAN_EQUALS));

      // preparing the from entities
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      fromEntities.add(ExecueBeanManagementUtil.createFromEntityObject(queryTable));

      // prepare ordering entities, because limit entity will be used.
      List<OrderEntity> orderEntities = new ArrayList<OrderEntity>();
      SelectEntity idSelectEntity = new SelectEntity();
      idSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      idSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(ExecueBeanManagementUtil
               .prepareQueryTableColumn(idColumn, queryTable)));
      OrderEntity orderEntity = new OrderEntity();
      orderEntity.setSelectEntity(idSelectEntity);
      orderEntity.setOrderType(OrderEntityType.ASCENDING);
      orderEntities.add(orderEntity);

      Query recordPopulationQuery = new Query();
      recordPopulationQuery.setSelectEntities(selectEntities);
      recordPopulationQuery.setWhereEntities(conditionEntities);
      recordPopulationQuery.setFromEntities(fromEntities);
      recordPopulationQuery.setOrderingEntities(orderEntities);
      return recordPopulationQuery;
   }

   /**
    * This method returns the query which counts the records in population table based on slice count.
    * 
    * @param martCreationOutputInfo
    * @param sliceCount
    * @return countPopulationQuery
    */
   private Query prepareCountPopulationQuery (MartCreationOutputInfo martCreationOutputInfo, Integer sliceCount) {
      MartPopulationTableStructure populationTable = martCreationOutputInfo.getPopulationTable();
      DataSource dataSource = martCreationOutputInfo.getMartCreationInputInfo().getMartCreationContext()
               .getTargetAsset().getDataSource();
      QueryTable queryTable = AnswersCatalogUtil
               .createQueryTable(populationTable.getTableName(), dataSource.getOwner());
      QueryColumn populationColumn = populationTable.getPopulationColumn();
      QueryColumn sliceNumberColumn = populationTable.getSliceNumberColumn();

      // preparing the select entities.
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      SelectEntity populationSelectEntity = new SelectEntity();
      populationSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      populationSelectEntity.setFunctionName(StatType.COUNT);
      populationSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(ExecueBeanManagementUtil
               .prepareQueryTableColumn(populationColumn, queryTable)));
      populationSelectEntity.setAlias(ExecueCoreUtil.getAlias(new ArrayList<String>()));
      selectEntities.add(populationSelectEntity);

      // preparing the condition entities.
      List<ConditionEntity> conditionEntities = new ArrayList<ConditionEntity>();
      conditionEntities.add(AnswersCatalogUtil.prepareConditionEntity(queryTable, sliceNumberColumn, sliceCount
               .toString(), OperatorType.LESS_THAN_EQUALS));

      // preparing the from entities
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      fromEntities.add(ExecueBeanManagementUtil.createFromEntityObject(queryTable));

      Query countPopulationQuery = new Query();
      countPopulationQuery.setSelectEntities(selectEntities);
      countPopulationQuery.setWhereEntities(conditionEntities);
      countPopulationQuery.setFromEntities(fromEntities);
      return countPopulationQuery;
   }

   /**
    * This method prepares the base query which reads the island info(distribution, dimension and slice count for that
    * island)
    * 
    * @param fractionalDataSetTempTable
    * @param queryTable
    * @return query
    */
   private Query populateQueryForDistributionDimensionAndSliceCount (
            MartFractionalDataSetTempTableStructure fractionalDataSetTempTable, QueryTable queryTable) {
      List<SelectEntity> sequenceSelectEntities = new ArrayList<SelectEntity>();
      List<String> existingAliases = new ArrayList<String>();
      for (QueryColumn distributionColumn : fractionalDataSetTempTable.getDistributionColumns()) {
         SelectEntity distributionSelectEntity = new SelectEntity();
         distributionSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
         distributionSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(distributionColumn,
                  queryTable));
         distributionSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
         sequenceSelectEntities.add(distributionSelectEntity);
      }

      SelectEntity dimensionSelectEntity = new SelectEntity();
      dimensionSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      dimensionSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(fractionalDataSetTempTable
               .getDimensionColumn(), queryTable));
      dimensionSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
      sequenceSelectEntities.add(dimensionSelectEntity);

      SelectEntity sliceCountSelectEntity = new SelectEntity();
      sliceCountSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      sliceCountSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(fractionalDataSetTempTable
               .getSliceCountColumn(), queryTable));
      sliceCountSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
      sequenceSelectEntities.add(sliceCountSelectEntity);

      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      fromEntities.add(ExecueBeanManagementUtil.createFromEntityObject(queryTable));
      Query query = new Query();
      query.setSelectEntities(sequenceSelectEntities);
      query.setFromEntities(fromEntities);
      return query;
   }

   /**
    * This method prepares the insert table columns sequence
    * 
    * @param fractionalDatasetTableStructure
    * @return sqlTable
    */
   private List<QueryColumn> populateInsertTableColumnsSequence (
            MartFractionalDatasetTableStructure fractionalDatasetTableStructure) {
      List<QueryColumn> sequenceInsertTableColumns = new ArrayList<QueryColumn>();
      sequenceInsertTableColumns.add(fractionalDatasetTableStructure.getPopulationColumn());
      sequenceInsertTableColumns.add(fractionalDatasetTableStructure.getSfactorColumn());
      return sequenceInsertTableColumns;
   }

   /**
    * This method prepares the select query for population and sfactor as value passed to it.
    * 
    * @param fractionalDatasetTableStructure
    * @param sfactor
    * @return query
    */
   private Query populateSfactorQueryObject (MartFractionalDatasetTableStructure fractionalDatasetTableStructure,
            Double sfactor) {
      List<SelectEntity> sequenceSelectEntities = new ArrayList<SelectEntity>();
      sequenceSelectEntities.add(fractionalDatasetTableStructure.getPopulationSelectEntity());
      fractionalDatasetTableStructure.getSfactorSelectEntity().getQueryValue().setValue(sfactor.toString());
      sequenceSelectEntities.add(fractionalDatasetTableStructure.getSfactorSelectEntity());
      Query query = new Query();
      query.setSelectEntities(sequenceSelectEntities);
      return query;
   }

   /**
    * This method prepares the create table columns sequence
    * 
    * @param fractionalDataSetTempTableStructure
    * @return sqlTable.
    */
   private SQLTable populateCreateTableColumnsSequence (
            MartFractionalDatasetTableStructure fractionalDataSetTempTableStructure) {
      List<QueryColumn> sequenceCreateTableColumns = new ArrayList<QueryColumn>();
      sequenceCreateTableColumns.add(fractionalDataSetTempTableStructure.getPopulationColumn());
      sequenceCreateTableColumns.add(fractionalDataSetTempTableStructure.getSfactorColumn());
      return ExecueBeanManagementUtil.prepareSQLTableFromQueryColumns(fractionalDataSetTempTableStructure
               .getTableName(), sequenceCreateTableColumns);
   }

   /**
    * This method prepares the insert select query alias which will be used for ETL.
    * 
    * @param fractionalDatasetTableStructure
    * @return selectQueryAliases
    */
   private Map<String, String> populateSelectQueryAliases (
            MartFractionalDatasetTableStructure fractionalDatasetTableStructure) {
      Map<String, String> selectQueryAliases = new HashMap<String, String>();
      selectQueryAliases.put(fractionalDatasetTableStructure.getPopulationColumn().getColumnName(),
               fractionalDatasetTableStructure.getPopulationSelectEntity().getAlias());
      selectQueryAliases.put(fractionalDatasetTableStructure.getSfactorColumn().getColumnName(),
               fractionalDatasetTableStructure.getSfactorSelectEntity().getAlias());
      return selectQueryAliases;
   }

   /**
    * This method populates the MartFractionalDatasetTableStructure.
    * 
    * @param martCreationOutputInfo
    * @param fractionalDataSetTempTable
    * @param dimension
    * @return MartFractionalDatasetTableStructure
    * @throws AnswersCatalogException
    * @throws DataAccessException
    */
   private MartFractionalDatasetTableStructure populateFractionalDatasetTableStructure (
            MartCreationOutputInfo martCreationOutputInfo,
            MartFractionalDataSetTempTableStructure fractionalDataSetTempTable, String dimension)
            throws AnswersCatalogException, DataAccessException {
      MartCreationInputInfo martCreationInputInfo = martCreationOutputInfo.getMartCreationInputInfo();
      AnswersCatalogConfigurationContext answersCatalogConfigurationContext = martCreationInputInfo
               .getAnswersCatalogConfigurationContext();
      MartConfigurationContext martConfigurationContext = martCreationInputInfo.getMartConfigurationContext();
      MartCreationContext martCreationContext = martCreationInputInfo.getMartCreationContext();
      MartCreationPopulatedContext martCreationPopulatedContext = martCreationInputInfo
               .getMartCreationPopulatedContext();
      DataSource dataSource = martCreationContext.getTargetAsset().getDataSource();
      int maxDbObjectLength = answersCatalogConfigurationContext.getMaxDBObjectLength();
      QueryColumn configurationSfactorColumn = martConfigurationContext.getFractionalTableSfactorColumn();

      // preparing the tablename and check for uniqueness.

      String staticFractionalTempTableNotation = martConfigurationContext.getFractionalTableNotation();
      String assetName = martCreationContext.getTargetAsset().getName();
      String finalFractionalTable = dimension + staticFractionalTempTableNotation + UNDERSCORE + assetName;
      finalFractionalTable = getAnswersCatalogDataAccessService().getUniqueNonExistentTableName(dataSource,
               finalFractionalTable, new ArrayList<String>(), maxDbObjectLength);

      // preparing the columns and check for uniqueness.
      List<String> existingColumnNames = new ArrayList<String>();

      QueryColumn populationQueryColumn = ExecueBeanCloneUtil.cloneQueryColumn(martCreationPopulatedContext
               .getSourceAssetMappingInfo().getPopulatedPopulation().getQueryColumn());
      AnswersCatalogUtil.cleanQueryColumn(populationQueryColumn);
      AnswersCatalogUtil.maintainColumnUniqueness(populationQueryColumn, existingColumnNames, maxDbObjectLength);

      QueryColumn sfactorColumn = ExecueBeanCloneUtil.cloneQueryColumn(configurationSfactorColumn);
      AnswersCatalogUtil.maintainColumnUniqueness(sfactorColumn, existingColumnNames, maxDbObjectLength);

      List<String> existingAliases = new ArrayList<String>();
      SelectEntity populationSelectEntity = new SelectEntity();
      populationSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      populationSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(martCreationPopulatedContext
               .getSourceAssetMappingInfo().getPopulatedPopulation().getQueryTableColumn()));
      populationSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));

      SelectEntity sfactorSelectEntity = new SelectEntity();
      sfactorSelectEntity.setType(SelectEntityType.VALUE);
      QueryValue queryValue = new QueryValue();
      queryValue.setDataType(sfactorColumn.getDataType());
      queryValue.setValue(Double.valueOf(DEFAULT_SFACTOR_VALUE).toString());
      sfactorSelectEntity.setQueryValue(queryValue);
      sfactorSelectEntity.setRoundFunctionTargetColumn(AnswersCatalogUtil.getRoundFunctionTargetColumn(sfactorColumn
               .getDataType(), sfactorColumn.getPrecision(), sfactorColumn.getScale()));
      sfactorSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));

      MartFractionalDatasetTableStructure fractionalDatasetTableStructure = new MartFractionalDatasetTableStructure();
      fractionalDatasetTableStructure.setTableName(finalFractionalTable);
      fractionalDatasetTableStructure.setPopulationColumn(populationQueryColumn);
      fractionalDatasetTableStructure.setSfactorColumn(sfactorColumn);
      fractionalDatasetTableStructure.setPopulationSelectEntity(populationSelectEntity);
      fractionalDatasetTableStructure.setSfactorSelectEntity(sfactorSelectEntity);
      fractionalDatasetTableStructure.setMartFractionalDataSetTempTableStructure(fractionalDataSetTempTable);
      return fractionalDatasetTableStructure;
   }

   /**
    * This method prepares the condition entities for distribution and dimension based on value passed.
    * 
    * @param martCreationInputInfo
    * @param dimensionName
    * @param distributionValues
    * @param dimensionValue
    * @return conditionEntities
    */
   private List<ConditionEntity> prepareConditionalEntitiesForDistributionsAndDimension (
            MartCreationInputInfo martCreationInputInfo, String dimensionName, List<String> distributionValues,
            String dimensionValue) {
      MartCreationPopulatedContext martCreationPopulatedContext = martCreationInputInfo
               .getMartCreationPopulatedContext();
      List<ConditionEntity> conditionEntities = new ArrayList<ConditionEntity>();
      int index = 0;
      for (ConceptColumnMapping distributionConceptColumnMapping : martCreationPopulatedContext
               .getSourceAssetMappingInfo().getPopulatedDistributions()) {
         conditionEntities
                  .add(AnswersCatalogUtil.prepareConditionEntity(distributionConceptColumnMapping.getQueryTable(),
                           distributionConceptColumnMapping.getQueryColumn(), distributionValues.get(index++)));
      }
      List<ConceptColumnMapping> populatedProminentDimensions = martCreationPopulatedContext
               .getSourceAssetMappingInfo().getPopulatedProminentDimensions();
      ConceptColumnMapping matchedDimensionMapping = AnswersCatalogUtil.getMatchedDimensionMapping(dimensionName,
               populatedProminentDimensions);
      conditionEntities.add(AnswersCatalogUtil.prepareConditionEntity(matchedDimensionMapping.getQueryTable(),
               matchedDimensionMapping.getQueryColumn(), dimensionValue));
      return conditionEntities;
   }

   public IMartBatchCountPopulationService getMartBatchCountPopulationService () {
      return martBatchCountPopulationService;
   }

   public void setMartBatchCountPopulationService (IMartBatchCountPopulationService martBatchCountPopulationService) {
      this.martBatchCountPopulationService = martBatchCountPopulationService;
   }

   public IAnswersCatalogDataAccessService getAnswersCatalogDataAccessService () {
      return answersCatalogDataAccessService;
   }

   public void setAnswersCatalogDataAccessService (IAnswersCatalogDataAccessService answersCatalogDataAccessService) {
      this.answersCatalogDataAccessService = answersCatalogDataAccessService;
   }

   public IAnswersCatalogSQLQueryGenerationService getAnswersCatalogSQLQueryGenerationService () {
      return answersCatalogSQLQueryGenerationService;
   }

   public void setAnswersCatalogSQLQueryGenerationService (
            IAnswersCatalogSQLQueryGenerationService answersCatalogSQLQueryGenerationService) {
      this.answersCatalogSQLQueryGenerationService = answersCatalogSQLQueryGenerationService;
   }

   public MartCreationServiceHelper getMartCreationServiceHelper () {
      return martCreationServiceHelper;
   }

   public void setMartCreationServiceHelper (MartCreationServiceHelper martCreationServiceHelper) {
      this.martCreationServiceHelper = martCreationServiceHelper;
   }

}
