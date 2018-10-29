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
import com.execue.ac.bean.BatchCountAlgorithmStaticInput;
import com.execue.ac.bean.ConceptColumnMapping;
import com.execue.ac.bean.MartConfigurationContext;
import com.execue.ac.bean.MartCreationInputInfo;
import com.execue.ac.bean.MartCreationOutputInfo;
import com.execue.ac.bean.MartCreationPopulatedContext;
import com.execue.ac.bean.MartFractionalPopulationTableStructure;
import com.execue.ac.bean.MartWarehouseTableStructure;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.MartWarehouseExtractionException;
import com.execue.ac.helper.MartCreationServiceHelper;
import com.execue.ac.service.IAnswersCatalogDataAccessService;
import com.execue.ac.service.IAnswersCatalogSQLQueryGenerationService;
import com.execue.ac.service.IMartBatchCountPopulationService;
import com.execue.ac.service.IMartCreationConstants;
import com.execue.ac.service.IMartOperationalConstants;
import com.execue.ac.service.IMartWarehouseExtractionService;
import com.execue.ac.util.AnswersCatalogUtil;
import com.execue.core.bean.ThreadPoolTaskStatus;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.FromEntity;
import com.execue.core.common.bean.querygen.LimitEntity;
import com.execue.core.common.bean.querygen.OrderEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SQLIndex;
import com.execue.core.common.bean.querygen.SQLTable;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.OrderEntityType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.service.ExecueFixedSizeThreadPoolManager;
import com.execue.core.type.ThreadPoolTaskStatusType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * This service represents the step5 of mart creation process. In this step we will copy source into AC based on sampled
 * population.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class MartWarehouseExtractionServiceImpl implements IMartWarehouseExtractionService, IMartCreationConstants,
         IMartOperationalConstants {

   private IAnswersCatalogSQLQueryGenerationService answersCatalogSQLQueryGenerationService;
   private IAnswersCatalogDataAccessService         answersCatalogDataAccessService;
   private MartCreationServiceHelper                martCreationServiceHelper;
   private IMartBatchCountPopulationService         martBatchCountPopulationService;
   private ISDXRetrievalService                     sdxRetrievalService;
   private static final Logger                      logger = Logger.getLogger(MartWarehouseExtractionServiceImpl.class);

   /**
    * This method will extract the warehouse (source asset tables) for mart asset based on sampled population table
    * created.
    * 
    * @param martCreationOutputInfo
    * @return MartWarehouseTableStructure list
    * @throws MartWarehouseExtractionException
    */
   public List<MartWarehouseTableStructure> extractWarehouse (MartCreationOutputInfo martCreationOutputInfo)
            throws MartWarehouseExtractionException {
      MartCreationInputInfo martCreationInputInfo = martCreationOutputInfo.getMartCreationInputInfo();
      MartCreationContext martCreationContext = martCreationInputInfo.getMartCreationContext();
      List<MartWarehouseTableStructure> warehouseTableStructureList = new ArrayList<MartWarehouseTableStructure>();
      JobRequest jobRequest = martCreationContext.getJobRequest();
      Exception exception = null;
      JobOperationalStatus jobOperationalStatus = null;
      ExecueFixedSizeThreadPoolManager threadPoolManager = null;
      try {
         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  WAREHOUSE_EXTRACTION);
         // populate the warehouse table structure list.
         warehouseTableStructureList = populateMartWarehouseTableStructureList(martCreationInputInfo);

         // start a thread pool to process warehouse tables parallelly. The number of threads will be configured number
         // lets say max. of 5 threads
         if (logger.isInfoEnabled()) {
            logger.info("Thread Pool is being Created on Tables for Extracting Warehouse");
         }
         threadPoolManager = new ExecueFixedSizeThreadPoolManager(martCreationInputInfo.getMartConfigurationContext()
                  .getWarehouseExtractionThreadPoolSize(), martCreationInputInfo.getMartConfigurationContext()
                  .getThreadWaitTime());
         List<Future<? extends ThreadPoolTaskStatus>> taskStatusList = new ArrayList<Future<? extends ThreadPoolTaskStatus>>();
         // for each table structure.
         for (MartWarehouseTableStructure warehouseTableStructure : warehouseTableStructureList) {
            Future<ThreadPoolTaskStatus> submitTask = threadPoolManager.submitTask(new WarehouseTableExtraction(
                     martCreationOutputInfo, warehouseTableStructure));
            taskStatusList.add(submitTask);
         }
         if (logger.isInfoEnabled()) {
            logger.info("Waiting for Thread Pool on Tables for Extracting Warehouse to be completed");
         }
         threadPoolManager.waitTillComplete();
         if (logger.isInfoEnabled()) {
            logger.info("Thread Pool on Tables for Extracting Warehouse got completed");
         }
         // if all tasks are successfully completed then only we can go to next steps
         if (threadPoolManager.doesAllTasksCompletedSuccessfully(taskStatusList)) {
            getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
         } else {
            getMartCreationServiceHelper().updateJobOperationalStatusForFailure(
                     jobOperationalStatus,
                     jobRequest,
                     "There was error in extracting warehouse tables : "
                              + threadPoolManager.composeFailureReason(taskStatusList));
            throw new MartWarehouseExtractionException(
                     AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_WAREHOUSE_EXTRACTION_EXCEPTION_CODE,
                     "There was error in extracting warehouse tables : "
                              + threadPoolManager.composeFailureReason(taskStatusList));
         }
      } catch (MartWarehouseExtractionException e) {
         exception = e;
         throw new MartWarehouseExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_WAREHOUSE_EXTRACTION_EXCEPTION_CODE, e);
      } catch (AnswersCatalogException e) {
         exception = e;
         throw new MartWarehouseExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_WAREHOUSE_EXTRACTION_EXCEPTION_CODE, e);
      } catch (SDXException e) {
         exception = e;
         throw new MartWarehouseExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_WAREHOUSE_EXTRACTION_EXCEPTION_CODE, e);
      } catch (DataAccessException e) {
         exception = e;
         throw new MartWarehouseExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_WAREHOUSE_EXTRACTION_EXCEPTION_CODE, e);
      } catch (InterruptedException e) {
         exception = e;
         throw new MartWarehouseExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_THREAD_POOL_RELATED_EXCEPTION_CODE, e);
      } catch (ExecutionException e) {
         exception = e;
         throw new MartWarehouseExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_THREAD_POOL_RELATED_EXCEPTION_CODE, e);
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
               throw new MartWarehouseExtractionException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return warehouseTableStructureList;
   }

   class WarehouseTableExtraction implements Callable<ThreadPoolTaskStatus> {

      private MartWarehouseTableStructure warehouseTableStructure;
      private MartCreationOutputInfo      martCreationOutputInfo;

      public WarehouseTableExtraction (MartCreationOutputInfo martCreationOutputInfo,
               MartWarehouseTableStructure warehouseTableStructure) {
         super();
         this.martCreationOutputInfo = martCreationOutputInfo;
         this.warehouseTableStructure = warehouseTableStructure;
      }

      @Override
      public ThreadPoolTaskStatus call () throws Exception {
         ThreadPoolTaskStatus threadPoolTaskStatus = new ThreadPoolTaskStatus();
         threadPoolTaskStatus.setTaskStatusType(ThreadPoolTaskStatusType.STARTED);
         try {
            extractTableFromWarehouse(warehouseTableStructure, martCreationOutputInfo);
            threadPoolTaskStatus.setTaskStatusType(ThreadPoolTaskStatusType.SUCCESS);
         } catch (Exception exception) {
            threadPoolTaskStatus.setTaskStatusType(ThreadPoolTaskStatusType.FAILED);
            threadPoolTaskStatus.setFailureReason(exception.getMessage());
            logger.error(exception, exception);
         }
         return threadPoolTaskStatus;
      }
   }

   private SQLTable prepareCreateTableColumnsStructure (MartWarehouseTableStructure warehouseTableStructure) {
      List<QueryColumn> queryColumns = new ArrayList<QueryColumn>();
      queryColumns.addAll(warehouseTableStructure.getMartTableColumns());
      if (warehouseTableStructure.isPopulationTable()) {
         queryColumns.add(ExecueBeanCloneUtil.cloneQueryColumn(warehouseTableStructure.getPopulationSfactorColumn()));
      }
      return ExecueBeanManagementUtil.prepareSQLTableFromQueryColumns(warehouseTableStructure.getMartTableName(),
               queryColumns);
   }

   private List<QueryColumn> prepareInsertTableColumnsStructureForSourceAndTargetAtSameLocation (
            MartWarehouseTableStructure warehouseTableStructure) {
      List<QueryColumn> queryColumns = new ArrayList<QueryColumn>();
      queryColumns.addAll(warehouseTableStructure.getMartTableColumns());
      if (warehouseTableStructure.isPopulationTable()) {
         queryColumns.add(warehouseTableStructure.getPopulationSfactorColumn());
      }
      return queryColumns;
   }

   private List<QueryColumn> prepareInsertTableColumnsStructureForSourceAndTargetAtDifferentLocation (
            MartWarehouseTableStructure warehouseTableStructure) {
      List<QueryColumn> queryColumns = new ArrayList<QueryColumn>();
      queryColumns.addAll(warehouseTableStructure.getMartTableColumns());
      return queryColumns;
   }

   private boolean extractTableFromWarehouse (MartWarehouseTableStructure warehouseTableStructure,
            MartCreationOutputInfo martCreationOutputInfo) throws MartWarehouseExtractionException {
      boolean extractionSuccess = true;
      try {
         MartCreationInputInfo martCreationInputInfo = martCreationOutputInfo.getMartCreationInputInfo();
         MartCreationContext martCreationContext = martCreationInputInfo.getMartCreationContext();
         MartCreationPopulatedContext martCreationPopulatedContext = martCreationInputInfo
                  .getMartCreationPopulatedContext();
         Asset sourceAsset = martCreationContext.getSourceAsset();
         Asset targetAsset = martCreationContext.getTargetAsset();
         DataSource sourceDataSource = sourceAsset.getDataSource();
         DataSource targetDataSource = targetAsset.getDataSource();
         AssetProviderType targetProviderType = targetAsset.getDataSource().getProviderType();
         MartFractionalPopulationTableStructure fractionalPopulationTable = martCreationOutputInfo
                  .getFractionalPopulationTable();
         BatchCountAlgorithmStaticInput batchCountAlgorithmStaticInput = martCreationInputInfo
                  .getBatchCountAlgorithmStaticInput();
         Long recordCount = fractionalPopulationTable.getPopulationCount();

         // if it is not virtual table.
         if (CheckType.NO.equals(warehouseTableStructure.getSourceTable().getVirtual())) {
            if (logger.isDebugEnabled()) {
               logger.debug("For Source Asset Table " + warehouseTableStructure.getSourceTable().getName()
                        + " , Mart Table : " + warehouseTableStructure.getMartTableName());
            }
            // prepare create table columns sequence
            SQLTable createTableColumnsStructure = prepareCreateTableColumnsStructure(warehouseTableStructure);

            QueryTable warehouseQueryTable = ExecueBeanManagementUtil.prepareQueryTable(warehouseTableStructure
                     .getMartTableName(), null, targetDataSource.getOwner());

            // prepare create table query
            String createTableQuery = answersCatalogSQLQueryGenerationService.prepareCreateTableQuery(
                     targetProviderType, warehouseQueryTable, createTableColumnsStructure.getColumns());

            if (logger.isDebugEnabled()) {
               logger.debug("Create Table Query " + createTableQuery);
            }

            // prepare raw select query.
            Query selectQueryObject = populateSelectQueryObject(warehouseTableStructure);

            // if source location is same as target location
            if (martCreationContext.isTargetDataSourceSameAsSourceDataSource()) {

               List<QueryColumn> insertTableColumnsStructureForSourceAndTargetAtSameLocation = prepareInsertTableColumnsStructureForSourceAndTargetAtSameLocation(warehouseTableStructure);
               // prepare insert query
               String insertQuery = answersCatalogSQLQueryGenerationService.prepareInsertQuery(targetProviderType,
                        warehouseQueryTable, insertTableColumnsStructureForSourceAndTargetAtSameLocation);

               if (logger.isDebugEnabled()) {
                  logger.debug("Insert Table Query " + insertQuery);
               }

               ConceptColumnMapping populatedPopulation = martCreationPopulatedContext.getSourceAssetMappingInfo()
                        .getPopulatedPopulation();
               // final fractional sampled table.
               QueryTable queryTable = AnswersCatalogUtil.createQueryTable(fractionalPopulationTable
                        .getMergedTableName(), targetDataSource.getOwner());
               // if it is population table, add sfactor column select section as both tables are in
               // same location.
               if (warehouseTableStructure.isPopulationTable()) {
                  SelectEntity selectEntity = new SelectEntity();
                  selectEntity.setType(SelectEntityType.TABLE_COLUMN);
                  selectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(ExecueBeanManagementUtil
                           .prepareQueryTableColumn(fractionalPopulationTable.getSfactorColumn(), queryTable)));
                  // check for existing aliases list
                  selectEntity.setAlias(ExecueCoreUtil.getAlias(new ArrayList<String>()));
                  selectQueryObject.getSelectEntities().add(selectEntity);
               }

               // if it is fact table, add condition to get the data only for sampled population.
               if (LookupType.None.equals(warehouseTableStructure.getSourceTable().getLookupType())
                        || warehouseTableStructure.isPopulationTable()) {
                  List<ConditionEntity> whereConditions = new ArrayList<ConditionEntity>();
                  whereConditions.add(AnswersCatalogUtil.prepareConditionEntity(populatedPopulation.getQueryTable(),
                           populatedPopulation.getQueryColumn(), queryTable, fractionalPopulationTable
                                    .getPopulationColumn()));
                  selectQueryObject.setWhereEntities(whereConditions);
               }

               // prepare select query
               String selectQueryForSource = answersCatalogSQLQueryGenerationService.prepareSelectQuery(sourceAsset,
                        selectQueryObject);
               if (logger.isDebugEnabled()) {
                  logger.debug("Select Table Query " + selectQueryForSource);
               }

               // transfer the local data.
               getMartCreationServiceHelper().transferLocalData(targetDataSource, createTableQuery, insertQuery,
                        selectQueryForSource);
            }
            // this case will run if source and target are different.
            else {

               // prepare raw select query
               String selectQueryForSource = answersCatalogSQLQueryGenerationService.prepareSelectQuery(sourceAsset,
                        selectQueryObject);
               if (logger.isDebugEnabled()) {
                  logger.debug("Raw Select Table Query " + selectQueryForSource);
               }

               // prepare insert select alias map
               Map<String, String> selectQueryAliases = populateSelectQueryAliases(warehouseTableStructure,
                        selectQueryObject.getSelectEntities());

               List<QueryColumn> insertTableColumnsStructureForSourceAndTargetAtDifferentLocation = prepareInsertTableColumnsStructureForSourceAndTargetAtDifferentLocation(warehouseTableStructure);

               // prepare insert etl based query
               String insertETLQuery = answersCatalogSQLQueryGenerationService.prepareETLInsertQuery(
                        targetProviderType, warehouseQueryTable,
                        insertTableColumnsStructureForSourceAndTargetAtDifferentLocation, selectQueryAliases);

               if (logger.isDebugEnabled()) {
                  logger.debug("Insert Table ETL Query " + insertETLQuery);
               }

               // fact table, batch based population based on sampled population table.
               if (LookupType.None.equals(warehouseTableStructure.getSourceTable().getLookupType())
                        || warehouseTableStructure.isPopulationTable()) {
                  if (logger.isDebugEnabled()) {
                     logger.debug("Fact Table Data Transfer");
                  }

                  // base query for reading population table records
                  Query baseQueryForRecordsPopulationQuery = prepareRecordsPopulationQuery(martCreationOutputInfo);

                  Integer batchSize = getMartBatchCountPopulationService().populateBatchCountForPopulation(
                           batchCountAlgorithmStaticInput, selectQueryForSource.length());

                  if (logger.isDebugEnabled()) {
                     logger.debug("Batch Size " + batchSize);
                  }

                  if (martCreationInputInfo.getMartConfigurationContext()
                           .isSplitBatchDataTransferToAvoidSubConditions()) {
                     // based on flag override this value
                     batchSize = batchCountAlgorithmStaticInput.getMaxAllowedExpressionsInCondition();
                  }

                  // start a thread pool to process warehouse tables parallelly. The number of threads will be
                  // configured number lets say max. of 5 threads
                  if (logger.isInfoEnabled()) {
                     logger.info("Thread Pool is being Created on Fact Table ["
                              + warehouseTableStructure.getMartTableName() + "] for Extracting Data by Batches");
                  }
                  ExecueFixedSizeThreadPoolManager threadPoolManager = null;
                  try {
                     threadPoolManager = new ExecueFixedSizeThreadPoolManager(martCreationInputInfo
                              .getMartConfigurationContext().getBatchDataTransferThreadPoolSize(),
                              martCreationInputInfo.getMartConfigurationContext().getThreadWaitTime());
                     List<Future<? extends ThreadPoolTaskStatus>> taskStatusList = new ArrayList<Future<? extends ThreadPoolTaskStatus>>();
                     int maxTasksAllowedPerBatch = martCreationInputInfo.getMartConfigurationContext()
                              .getMaxTasksAllowedPerThreadPool();
                     int taskCounter = 0;
                     for (Long i = 1L; i <= recordCount; i = i + batchSize) {
                        taskCounter++;
                        LimitEntity limitEntity = new LimitEntity();
                        limitEntity.setStartingNumber(i);
                        limitEntity.setEndingNumber(i + batchSize - 1);

                        Query clonedSelectQueryObject = ExecueBeanCloneUtil.cloneQuery(selectQueryObject);
                        Query clonedBaseQueryForRecordsPopulationQuery = ExecueBeanCloneUtil
                                 .cloneQuery(baseQueryForRecordsPopulationQuery);

                        Future<ThreadPoolTaskStatus> submitTask = threadPoolManager
                                 .submitTask(new BatchRemoteDataTransfer(martCreationInputInfo,
                                          clonedBaseQueryForRecordsPopulationQuery, limitEntity,
                                          clonedSelectQueryObject, insertETLQuery, createTableQuery,
                                          warehouseTableStructure));
                        taskStatusList.add(submitTask);

                        if (maxTasksAllowedPerBatch <= taskCounter) {
                           if (logger.isInfoEnabled()) {
                              logger.info("Waiting for Thread Pool on Fact Table ["
                                       + warehouseTableStructure.getMartTableName()
                                       + "] for Extracting Data by Batches to be completed");
                           }
                           threadPoolManager.waitTillTaskComplete(taskStatusList);
                           taskStatusList = new ArrayList<Future<? extends ThreadPoolTaskStatus>>();
                           // reset the counter
                           taskCounter = 0;
                           // if all tasks are successfully completed , it means extraction was successful.
                           if (!threadPoolManager.doesAllTasksCompletedSuccessfully(taskStatusList)) {
                              logger
                                       .error("Warehouse Extraction failed for one of the tasks for batch data transfer : "
                                                + threadPoolManager.composeFailureReason(taskStatusList));
                              extractionSuccess = false;
                              throw new MartWarehouseExtractionException(
                                       AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_WAREHOUSE_EXTRACTION_EXCEPTION_CODE,
                                       "Warehouse Extraction failed for one of the tasks for batch data transfer : "
                                                + threadPoolManager.composeFailureReason(taskStatusList));
                           }
                        }
                     }
                     if (logger.isInfoEnabled()) {
                        logger.info("Waiting for Thread Pool on Fact Table ["
                                 + warehouseTableStructure.getMartTableName()
                                 + "] for Extracting Data by Batches to be completed");
                     }
                     threadPoolManager.waitTillComplete();
                     if (logger.isInfoEnabled()) {
                        logger.info("Thread Pool on Fact Table [" + warehouseTableStructure.getMartTableName()
                                 + "] for Extracting Data by Batches got completed");
                     }
                     // if all tasks are successfully completed , it means extraction was successful.
                     if (!threadPoolManager.doesAllTasksCompletedSuccessfully(taskStatusList)) {
                        extractionSuccess = false;
                        throw new MartWarehouseExtractionException(
                                 AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_WAREHOUSE_EXTRACTION_EXCEPTION_CODE,
                                 "Warehouse Extraction Failed in Batch Data Transfer : "
                                          + threadPoolManager.composeFailureReason(taskStatusList));
                     }
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
               // look up table, direct copy
               else {
                  if (logger.isDebugEnabled()) {
                     logger.debug("Lookup Table Data Transfer");
                  }
                  getMartCreationServiceHelper().transferRemoteData(sourceDataSource, targetDataSource, insertETLQuery,
                           selectQueryForSource, createTableQuery, warehouseTableStructure.getMartTableName());
               }
               // handle population table, add sfactor column to population table and populate it.
               if (warehouseTableStructure.isPopulationTable()) {
                  if (logger.isDebugEnabled()) {
                     logger.debug("Add Sfactor Column to Population Table");
                  }
                  updateSfactorColumnToPopulationTable(martCreationOutputInfo, warehouseTableStructure);
               }
            }
            // create index on table.
            if (logger.isDebugEnabled()) {
               logger.debug("Create Indexes on Table " + warehouseTableStructure.getMartTableName());
            }
            createIndexesOnMartTable(martCreationInputInfo, warehouseTableStructure);
         }
      } catch (AnswersCatalogException e) {
         throw new MartWarehouseExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_WAREHOUSE_EXTRACTION_EXCEPTION_CODE, e);
      } catch (DataAccessException e) {
         throw new MartWarehouseExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_WAREHOUSE_EXTRACTION_EXCEPTION_CODE, e);
      } catch (Exception e) {
         throw new MartWarehouseExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_WAREHOUSE_EXTRACTION_EXCEPTION_CODE, e);
      }
      return extractionSuccess;
   }

   private void batchRemoteDataTransfer (MartCreationInputInfo martCreationInputInfo,
            Query baseQueryForRecordsPopulationQuery, LimitEntity limitEntity, Query selectQueryObject,
            String insertETLQuery, String createTableQuery, MartWarehouseTableStructure warehouseTableStructure)
            throws MartWarehouseExtractionException {
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

         // batch based select query
         String batchBasedSelectQuery = prepareBatchBasedSelectQuery(sourceAsset, selectQueryObject,
                  conditionalEntityForPopulation);

         if (logger.isDebugEnabled()) {
            logger.debug("Batch Based Select Query " + batchBasedSelectQuery);
         }

         // transfer remote data
         getMartCreationServiceHelper().transferRemoteData(sourceDataSource, targetDataSource, insertETLQuery,
                  batchBasedSelectQuery, createTableQuery, warehouseTableStructure.getMartTableName());
      } catch (AnswersCatalogException e) {
         throw new MartWarehouseExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_WAREHOUSE_EXTRACTION_EXCEPTION_CODE, e);
      } catch (DataAccessException e) {
         throw new MartWarehouseExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_WAREHOUSE_EXTRACTION_EXCEPTION_CODE, e);
      } catch (Exception e) {
         throw new MartWarehouseExtractionException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_WAREHOUSE_EXTRACTION_EXCEPTION_CODE, e);
      }

   }

   class BatchRemoteDataTransfer implements Callable<ThreadPoolTaskStatus> {

      private MartCreationInputInfo       martCreationInputInfo;
      private Query                       baseQueryForRecordsPopulationQuery;
      private LimitEntity                 limitEntity;
      private Query                       selectQueryObject;
      private String                      insertETLQuery;
      private String                      createTableQuery;
      private MartWarehouseTableStructure warehouseTableStructure;

      public BatchRemoteDataTransfer (MartCreationInputInfo martCreationInputInfo,
               Query baseQueryForRecordsPopulationQuery, LimitEntity limitEntity, Query selectQueryObject,
               String insertETLQuery, String createTableQuery, MartWarehouseTableStructure warehouseTableStructure) {
         super();
         this.martCreationInputInfo = martCreationInputInfo;
         this.baseQueryForRecordsPopulationQuery = baseQueryForRecordsPopulationQuery;
         this.limitEntity = limitEntity;
         this.selectQueryObject = selectQueryObject;
         this.insertETLQuery = insertETLQuery;
         this.createTableQuery = createTableQuery;
         this.warehouseTableStructure = warehouseTableStructure;
      }

      @Override
      public ThreadPoolTaskStatus call () throws Exception {
         ThreadPoolTaskStatus threadPoolTaskStatus = new ThreadPoolTaskStatus();
         threadPoolTaskStatus.setTaskStatusType(ThreadPoolTaskStatusType.STARTED);
         try {
            batchRemoteDataTransfer(martCreationInputInfo, baseQueryForRecordsPopulationQuery, limitEntity,
                     selectQueryObject, insertETLQuery, createTableQuery, warehouseTableStructure);
            threadPoolTaskStatus.setTaskStatusType(ThreadPoolTaskStatusType.SUCCESS);
         } catch (Exception exception) {
            threadPoolTaskStatus.setTaskStatusType(ThreadPoolTaskStatusType.FAILED);
            threadPoolTaskStatus.setFailureReason(exception.getMessage());
            logger.error(exception, exception);
         }
         return threadPoolTaskStatus;
      }
   }

   /**
    * This method adds sfactor column to population table and populate that column.
    * 
    * @param martCreationOutputInfo
    * @param warehouseTableStructure
    * @throws AnswersCatalogException
    * @throws DataAccessException
    */
   private void updateSfactorColumnToPopulationTable (MartCreationOutputInfo martCreationOutputInfo,
            MartWarehouseTableStructure warehouseTableStructure) throws AnswersCatalogException, DataAccessException {
      MartCreationInputInfo martCreationInputInfo = martCreationOutputInfo.getMartCreationInputInfo();
      MartCreationPopulatedContext martCreationPopulatedContext = martCreationInputInfo
               .getMartCreationPopulatedContext();
      MartFractionalPopulationTableStructure fractionalPopulationTable = martCreationOutputInfo
               .getFractionalPopulationTable();
      DataSource targetDataSource = martCreationInputInfo.getMartCreationPopulatedContext().getTargetAsset()
               .getDataSource();
      // create the index on population table and mark the flag to avoid creating duplicate indexes on same table
      createIndexesOnMartTable(martCreationInputInfo, warehouseTableStructure);
      warehouseTableStructure.setIndexCreated(true);
      // update sfactor on this table based on sampled population table.
      // find the population column of source.
      QueryColumn sourcePopulationColumn = martCreationPopulatedContext.getSourceAssetMappingInfo()
               .getPopulatedPopulation().getQueryColumn();

      List<String> existingAliases = new ArrayList<String>();
      QueryTable sourceMartPopulationTable = AnswersCatalogUtil.createQueryTable(warehouseTableStructure
               .getMartTableName(), existingAliases, targetDataSource.getOwner());
      QueryTable fractionalQueryTable = AnswersCatalogUtil.createQueryTable(fractionalPopulationTable
               .getMergedTableName(), existingAliases, targetDataSource.getOwner());

      List<QueryTable> queryTables = new ArrayList<QueryTable>();
      queryTables.add(fractionalQueryTable);

      // set sfactor column
      List<ConditionEntity> setConditions = new ArrayList<ConditionEntity>();
      setConditions.add(AnswersCatalogUtil.prepareConditionEntity(sourceMartPopulationTable, warehouseTableStructure
               .getPopulationSfactorColumn(), fractionalQueryTable, fractionalPopulationTable.getSfactorColumn()));

      // based on population column.
      List<ConditionEntity> whereConditions = new ArrayList<ConditionEntity>();
      whereConditions.add(AnswersCatalogUtil.prepareConditionEntity(sourceMartPopulationTable, sourcePopulationColumn,
               fractionalQueryTable, fractionalPopulationTable.getPopulationColumn()));

      answersCatalogDataAccessService.executeUpdateStatement(targetDataSource, sourceMartPopulationTable, queryTables,
               setConditions, whereConditions);
   }

   /**
    * This method prepares the batch based select query to read data.
    * 
    * @param sourceAsset
    * @param selectQueryForSource
    * @param conditionalEntityForPopulation
    * @return batchBasedSelectQueryString
    * @throws AnswersCatalogException
    */
   private String prepareBatchBasedSelectQuery (Asset sourceAsset, Query selectQueryForSource,
            ConditionEntity conditionalEntityForPopulation) throws AnswersCatalogException {
      List<ConditionEntity> conditionEntities = new ArrayList<ConditionEntity>();
      conditionEntities.add(conditionalEntityForPopulation);
      selectQueryForSource.setWhereEntities(conditionEntities);
      selectQueryForSource.setFromEntities(null);
      String batchBasedSelectQueryString = answersCatalogSQLQueryGenerationService.prepareSelectQuery(sourceAsset,
               selectQueryForSource);
      return batchBasedSelectQueryString;
   }

   /**
    * This method creates index on mart tables.
    * 
    * @param martCreationInputInfo
    * @param warehouseTableStructure
    * @throws AnswersCatalogException
    * @throws DataAccessException
    */
   private void createIndexesOnMartTable (MartCreationInputInfo martCreationInputInfo,
            MartWarehouseTableStructure warehouseTableStructure) throws AnswersCatalogException, DataAccessException {
      if (warehouseTableStructure.isIndexCreated()) {
         return;
      }
      String martTableName = warehouseTableStructure.getMartTableName();
      DataSource dataSource = martCreationInputInfo.getMartCreationContext().getTargetAsset().getDataSource();
      AnswersCatalogConfigurationContext answersCatalogConfigurationContext = martCreationInputInfo
               .getAnswersCatalogConfigurationContext();
      int maxDBObjectLength = answersCatalogConfigurationContext.getMaxDBObjectLength();
      List<SQLIndex> indexes = new ArrayList<SQLIndex>();

      for (Colum colum : warehouseTableStructure.getSourceColumns()) {
         if (ColumnType.DIMENSION.equals(colum.getKdxDataType()) || ColumnType.ID.equals(colum.getKdxDataType())
                  || ColumnType.SIMPLE_LOOKUP.equals(colum.getKdxDataType())
                  || ColumnType.SIMPLE_HIERARCHY_LOOKUP.equals(colum.getKdxDataType())
                  || ColumnType.RANGE_LOOKUP.equals(colum.getKdxDataType())) {
            // Keep collecting the needed indexes on the same table
            indexes.add(ExecueBeanManagementUtil.prepareSQLIndex(martTableName, colum.getName()));
         }
      }

      getAnswersCatalogDataAccessService().createMultipleIndexesOnTable(dataSource, indexes, maxDBObjectLength);
   }

   /**
    * This method prepares the query which returns records from population table
    * 
    * @param martCreationOutputInfo
    * @return recordPopulationQuery
    */
   private Query prepareRecordsPopulationQuery (MartCreationOutputInfo martCreationOutputInfo) {
      MartFractionalPopulationTableStructure fractionalPopulationTable = martCreationOutputInfo
               .getFractionalPopulationTable();

      DataSource targetDataSource = martCreationOutputInfo.getMartCreationInputInfo().getMartCreationContext()
               .getTargetAsset().getDataSource();
      QueryTable queryTable = AnswersCatalogUtil.createQueryTable(fractionalPopulationTable.getMergedTableName(),
               targetDataSource.getOwner());
      QueryColumn populationColumn = fractionalPopulationTable.getPopulationColumn();

      // preparing the select entities.
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      SelectEntity populationSelectEntity = new SelectEntity();
      populationSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      populationSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(ExecueBeanManagementUtil
               .prepareQueryTableColumn(populationColumn, queryTable)));
      populationSelectEntity.setAlias(ExecueCoreUtil.getAlias(new ArrayList<String>()));
      selectEntities.add(populationSelectEntity);

      // preparing the from entities
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      fromEntities.add(ExecueBeanManagementUtil.createFromEntityObject(queryTable));

      // prepare ordering entities
      List<OrderEntity> orderEntities = new ArrayList<OrderEntity>();
      SelectEntity idSelectEntity = new SelectEntity();
      idSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      idSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(ExecueBeanManagementUtil
               .prepareQueryTableColumn(populationColumn, queryTable)));
      OrderEntity orderEntity = new OrderEntity();
      orderEntity.setSelectEntity(idSelectEntity);
      orderEntity.setOrderType(OrderEntityType.ASCENDING);
      orderEntities.add(orderEntity);

      Query recordPopulationQuery = new Query();
      recordPopulationQuery.setSelectEntities(selectEntities);
      recordPopulationQuery.setFromEntities(fromEntities);
      recordPopulationQuery.setOrderingEntities(orderEntities);
      return recordPopulationQuery;
   }

   /**
    * This method prepares select query for table structure.
    * 
    * @param warehouseTableStructure
    * @return query
    */
   private Query populateSelectQueryObject (MartWarehouseTableStructure warehouseTableStructure) {
      Tabl sourceTable = warehouseTableStructure.getSourceTable();
      List<Colum> sourceColumns = warehouseTableStructure.getSourceColumns();
      QueryTable sourceQueryTable = ExecueBeanManagementUtil.prepareQueryTable(sourceTable);
      List<SelectEntity> martTableSelectEntities = new ArrayList<SelectEntity>();
      List<String> existingAliases = new ArrayList<String>();
      for (Colum colum : sourceColumns) {
         QueryColumn queryColumn = ExecueBeanManagementUtil.prepareQueryColumn(colum);
         queryColumn.setDefaultValue(null);
         queryColumn.setNullable(true);
         SelectEntity selectEntity = new SelectEntity();
         selectEntity.setType(SelectEntityType.TABLE_COLUMN);
         selectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(ExecueBeanManagementUtil
                  .prepareQueryTableColumn(queryColumn, sourceQueryTable)));
         selectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
         martTableSelectEntities.add(selectEntity);
      }
      Query query = new Query();
      query.setSelectEntities(martTableSelectEntities);
      return query;
   }

   /**
    * This method prepares the insert into select query alias map used for ETL
    * 
    * @param warehouseTableStructure
    * @param selectEntities
    * @return selectQueryAliases
    */
   private Map<String, String> populateSelectQueryAliases (MartWarehouseTableStructure warehouseTableStructure,
            List<SelectEntity> selectEntities) {
      Map<String, String> selectQueryAliases = new HashMap<String, String>();
      int index = 0;
      for (QueryColumn queryColumn : warehouseTableStructure.getMartTableColumns()) {
         selectQueryAliases.put(queryColumn.getColumnName(), selectEntities.get(index++).getAlias());
      }
      return selectQueryAliases;
   }

   /**
    * This method prepares the warehouse table structure list for each source asset table.
    * 
    * @param martCreationInputInfo
    * @return warehouseTableStructureList
    * @throws SDXException
    * @throws AnswersCatalogException
    * @throws DataAccessException
    */
   private List<MartWarehouseTableStructure> populateMartWarehouseTableStructureList (
            MartCreationInputInfo martCreationInputInfo) throws SDXException, AnswersCatalogException,
            DataAccessException {
      AnswersCatalogConfigurationContext answersCatalogConfigurationContext = martCreationInputInfo
               .getAnswersCatalogConfigurationContext();
      MartConfigurationContext martConfigurationContext = martCreationInputInfo.getMartConfigurationContext();
      MartCreationContext martCreationContext = martCreationInputInfo.getMartCreationContext();
      MartCreationPopulatedContext martCreationPopulatedContext = martCreationInputInfo
               .getMartCreationPopulatedContext();
      DataSource dataSource = martCreationContext.getTargetAsset().getDataSource();
      int maxDbObjectLength = answersCatalogConfigurationContext.getMaxDBObjectLength();
      Asset sourceAsset = martCreationContext.getSourceAsset();
      ConceptColumnMapping sourcePopulationTableMapping = martCreationPopulatedContext.getSourceAssetMappingInfo()
               .getPopulatedPopulation();

      List<MartWarehouseTableStructure> warehouseTableStructureList = new ArrayList<MartWarehouseTableStructure>();
      List<Tabl> sourceTables = getSdxRetrievalService().getAllTables(sourceAsset);
      for (Tabl sourceTable : sourceTables) {
         List<Colum> sourceTableColumns = getSdxRetrievalService().getColumnsOfTable(sourceTable);

         // preparing the tablename and check for uniqueness.
         List<String> existingTableNames = new ArrayList<String>();

         if (ExecueCoreUtil.isCollectionNotEmpty(warehouseTableStructureList)) {
            for (MartWarehouseTableStructure warehouseTableStructure : warehouseTableStructureList) {
               existingTableNames.add(warehouseTableStructure.getMartTableName());
            }
         }

         String martTableNotation = martConfigurationContext.getFractionalTableNotation();
         String finalMartTable = sourceTable.getName() + martTableNotation;
         finalMartTable = getAnswersCatalogDataAccessService().getUniqueNonExistentTableName(dataSource,
                  finalMartTable, existingTableNames, maxDbObjectLength);

         List<QueryColumn> martQueryColumns = new ArrayList<QueryColumn>();
         Map<String, QueryColumn> sourceMartColumnMap = new HashMap<String, QueryColumn>();
         List<String> existingColumnNames = new ArrayList<String>();
         for (Colum colum : sourceTableColumns) {
            QueryColumn queryColumn = ExecueBeanManagementUtil.prepareQueryColumn(colum);
            AnswersCatalogUtil.cleanQueryColumn(queryColumn);
            AnswersCatalogUtil.maintainColumnUniqueness(queryColumn, existingColumnNames, maxDbObjectLength);
            martQueryColumns.add(queryColumn);
            sourceMartColumnMap.put(colum.getName(), queryColumn);
         }
         boolean populationTable = false;
         QueryColumn sfactorColumn = null;
         if (sourceTable.getName().equalsIgnoreCase(sourcePopulationTableMapping.getQueryTable().getTableName())) {
            populationTable = true;
            sfactorColumn = martConfigurationContext.getFractionalTableSfactorColumn();
            AnswersCatalogUtil.maintainColumnUniqueness(sfactorColumn, existingColumnNames, maxDbObjectLength);
         }

         MartWarehouseTableStructure warehouseTableStructure = new MartWarehouseTableStructure();
         warehouseTableStructure.setSourceTable(sourceTable);
         warehouseTableStructure.setSourceColumns(sourceTableColumns);
         warehouseTableStructure.setMartTableName(finalMartTable);
         warehouseTableStructure.setMartTableColumns(martQueryColumns);
         warehouseTableStructure.setSourceMartColumnMap(sourceMartColumnMap);
         warehouseTableStructure.setPopulationTable(populationTable);
         warehouseTableStructure.setPopulationSfactorColumn(sfactorColumn);
         warehouseTableStructureList.add(warehouseTableStructure);
      }
      return warehouseTableStructureList;
   }

   public IMartBatchCountPopulationService getMartBatchCountPopulationService () {
      return martBatchCountPopulationService;
   }

   public void setMartBatchCountPopulationService (IMartBatchCountPopulationService martBatchCountPopulationService) {
      this.martBatchCountPopulationService = martBatchCountPopulationService;
   }

   public IAnswersCatalogSQLQueryGenerationService getAnswersCatalogSQLQueryGenerationService () {
      return answersCatalogSQLQueryGenerationService;
   }

   public void setAnswersCatalogSQLQueryGenerationService (
            IAnswersCatalogSQLQueryGenerationService answersCatalogSQLQueryGenerationService) {
      this.answersCatalogSQLQueryGenerationService = answersCatalogSQLQueryGenerationService;
   }

   public IAnswersCatalogDataAccessService getAnswersCatalogDataAccessService () {
      return answersCatalogDataAccessService;
   }

   public void setAnswersCatalogDataAccessService (IAnswersCatalogDataAccessService answersCatalogDataAccessService) {
      this.answersCatalogDataAccessService = answersCatalogDataAccessService;
   }

   public MartCreationServiceHelper getMartCreationServiceHelper () {
      return martCreationServiceHelper;
   }

   public void setMartCreationServiceHelper (MartCreationServiceHelper martCreationServiceHelper) {
      this.martCreationServiceHelper = martCreationServiceHelper;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

}
