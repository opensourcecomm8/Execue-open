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
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.execue.ac.bean.MartCreationInputInfo;
import com.execue.ac.bean.MartCreationOutputInfo;
import com.execue.ac.bean.MartFractionalDataSetTempTableStructure;
import com.execue.ac.bean.MartFractionalDatasetTableStructure;
import com.execue.ac.bean.MartFractionalPopulationTableStructure;
import com.execue.ac.bean.MartPopulationTableStructure;
import com.execue.ac.bean.MartWarehouseTableStructure;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.DataMartCreationException;
import com.execue.ac.exception.MartAssetExtractionException;
import com.execue.ac.exception.MartContextPopulationException;
import com.execue.ac.exception.MartFractionalDatasetPopulationException;
import com.execue.ac.exception.MartInputValidatorException;
import com.execue.ac.exception.MartMergePopulationException;
import com.execue.ac.exception.MartPopulationSliceException;
import com.execue.ac.exception.MartResourceCleanupException;
import com.execue.ac.exception.MartSfactorPopulationException;
import com.execue.ac.exception.MartWarehouseExtractionException;
import com.execue.ac.service.IDataMartCreationService;
import com.execue.ac.service.IMartAssetExtractionService;
import com.execue.ac.service.IMartContextPopulationService;
import com.execue.ac.service.IMartFractionalDatasetPopulationService;
import com.execue.ac.service.IMartInputValidatorService;
import com.execue.ac.service.IMartMergePopulationService;
import com.execue.ac.service.IMartOperationalConstants;
import com.execue.ac.service.IMartPopulationSliceService;
import com.execue.ac.service.IMartResourceCleanupService;
import com.execue.ac.service.IMartSfactorPopulationService;
import com.execue.ac.service.IMartWarehouseExtractionService;
import com.execue.core.bean.ThreadPoolTaskStatus;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.service.ExecueFixedSizeThreadPoolManager;
import com.execue.core.type.ThreadPoolTaskStatusType;

/**
 * This service is the wrapper to create data mart
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class DataMartCreationServiceImpl implements IDataMartCreationService, IMartOperationalConstants {

   private IMartInputValidatorService              martInputValidatorService;
   private IMartContextPopulationService           martContextPopulationService;
   private IMartPopulationSliceService             martPopulationSliceService;
   private IMartFractionalDatasetPopulationService martFractionalDatasetPopulationService;
   private IMartSfactorPopulationService           martSfactorPopulationService;
   private IMartMergePopulationService             martMergePopulationService;
   private IMartWarehouseExtractionService         martWarehouseExtractionService;
   private IMartResourceCleanupService             martResourceCleanupService;
   private IMartAssetExtractionService             martAssetExtractionService;

   private static final Logger                     logger = Logger.getLogger(DataMartCreationServiceImpl.class);

   // assumptions :

   // a. In ETL select statements alias should be the column name of the insert query as while
   // creation of table we have taken care of uniqueness factor

   // b. stddev and mean will be the sequence of stats while applying to measures.

   // c. Population table structure will follow ID,Population and SLICE_NUMBER column sequence

   // d. Fractional temp tables will follow ID,COUNT(Population),Distributions,Dimension,STDDEV(m1),avg(m1),...
   // stddev(mn),avg(mn).

   // e. All the tables needs to be checked for uniqueness and columns within the tables
   // because mroe than one mart can be built at same place.

   // step 1 - validate the input for mart creation

   // step 2 - slicing the population

   // step 3 - fractional datasets creation

   // step 4 - sfactor population

   // step 5 - merge sampled population

   // step 6 - warehouse extraction.

   // step 7 - cleanup resources.

   // step 8 - asset extraction.

   public IMartAssetExtractionService getMartAssetExtractionService () {
      return martAssetExtractionService;
   }

   public void setMartAssetExtractionService (IMartAssetExtractionService martAssetExtractionService) {
      this.martAssetExtractionService = martAssetExtractionService;
   }

   /**
    * This method controls the data mart creation flow. It executes the steps defined to create data mart
    * 
    * @param martCreationContext
    * @return martCreationOutputInfo
    * @throws DataMartCreationException
    */
   public MartCreationOutputInfo dataMartCreation (MartCreationContext martCreationContext)
            throws DataMartCreationException {
      MartCreationOutputInfo martCreationOutputInfo = null;
      ExecueFixedSizeThreadPoolManager threadPoolManager = null;
      try {
         // validating the mart creation context
         if (logger.isInfoEnabled()) {
            logger.info("Validation of the Mart Creation Context");
         }
         boolean isMartCreationContextValid = martInputValidatorService
                  .validateMartCreationContext(martCreationContext);
         // if mart creation context is valid
         if (isMartCreationContextValid) {
            martCreationOutputInfo = new MartCreationOutputInfo();

            // populating the mart creation input info using mart creation context
            if (logger.isInfoEnabled()) {
               logger.info("Populating the Mart Creation Context");
            }
            MartCreationInputInfo martCreationInputInfo = martContextPopulationService
                     .populateMartCreationInputInfo(martCreationContext);
            martCreationOutputInfo.setMartCreationInputInfo(martCreationInputInfo);

            // step 1. slicing the population.
            if (logger.isInfoEnabled()) {
               logger.info("Population Slicing Started");
            }
            MartPopulationTableStructure populationTableStructure = martPopulationSliceService
                     .populationSlicing(martCreationOutputInfo);
            martCreationOutputInfo.setPopulationTable(populationTableStructure);
            if (logger.isInfoEnabled()) {
               logger.info("Population Table Name " + populationTableStructure.getTableName());
            }

            if (logger.isInfoEnabled()) {
               logger.info("Thread Pool is being Created for Dim Level Fractional Tables");
            }
            // start a thread pool to process fractional tables parallelly. thread pool 
            //   size is a configured value by name FractionalTablePopulationThreadPoolSize
            threadPoolManager = new ExecueFixedSizeThreadPoolManager(martCreationInputInfo
                     .getMartConfigurationContext().getFractionalTablePopulationThreadPoolSize(), martCreationInputInfo
                     .getMartConfigurationContext().getThreadWaitTime());
            List<Future<? extends ThreadPoolTaskStatus>> taskStatusList = new ArrayList<Future<? extends ThreadPoolTaskStatus>>();
            for (String dimension : martCreationContext.getProminentDimensions()) {
               Future<ThreadPoolTaskStatus> submitTask = threadPoolManager
                        .submitTask(new FractionalDataSetTablePopulation(martCreationOutputInfo, dimension));
               taskStatusList.add(submitTask);
            }
            if (logger.isInfoEnabled()) {
               logger.info("Waiting for Thread Pool on Dim Level Fractional Tables to be completed");
            }
            threadPoolManager.waitTillComplete();
            if (logger.isInfoEnabled()) {
               logger.info("Thread Pool on Dim Level Fractional Tables got completed");
            }
            // if all tasks are successfully completed then only we can go to next steps
            if (!threadPoolManager.doesAllTasksCompletedSuccessfully(taskStatusList)) {

               martCreationOutputInfo.setCreationSuccessful(false);
               logger
                        .error("There was error in processing fractional tables and sfactor population for one of the dimensions : "
                                 + threadPoolManager.composeFailureReason(taskStatusList));
               throw new DataMartCreationException(
                        AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_FRACTIONAL_DATASETS_POPULATION_EXCEPTION_CODE,
                        "There was error in processing fractional tables and sfactor population for one of the dimensions : "
                                 + threadPoolManager.composeFailureReason(taskStatusList));
            } else {
               // step 4. populating the fractional population table.
               if (logger.isInfoEnabled()) {
                  logger.info("Merge all the fractional datasets");
               }
               MartFractionalPopulationTableStructure fractionalPopulationTable = martMergePopulationService
                        .mergeSampledPopulation(martCreationOutputInfo);
               martCreationOutputInfo.setFractionalPopulationTable(fractionalPopulationTable);
               if (logger.isDebugEnabled()) {
                  logger
                           .debug("Final Merged Population Table Name : "
                                    + fractionalPopulationTable.getMergedTableName());
               }

               // step 5. warehouse extraction based on sampled population.
               if (logger.isInfoEnabled()) {
                  logger.info("Starting the Warehouse Extraction Process");
               }
               List<MartWarehouseTableStructure> warehouseTableStructureList = martWarehouseExtractionService
                        .extractWarehouse(martCreationOutputInfo);
               martCreationOutputInfo.setWarehouseTableStructure(warehouseTableStructureList);
               if (logger.isInfoEnabled()) {
                  logger.info("Tables Extracted :");
                  for (MartWarehouseTableStructure martWarehouseTableStructure : warehouseTableStructureList) {
                     logger.info(martWarehouseTableStructure.getMartTableName());
                  }
               }

               // step 6. Resource clean up.
               if (logger.isInfoEnabled()) {
                  logger.info("Resource Cleanup.");
               }
               if (martCreationInputInfo.getMartConfigurationContext().isCleanTemporaryResources()) {
                  martResourceCleanupService.cleanupResources(martCreationOutputInfo);
               } else {
                  if (logger.isInfoEnabled()) {
                     logger.info("Temporary Resources Cleanup skipped as per configuration");
                  }
               }
               // step 7. Asset extraction
               if (logger.isInfoEnabled()) {
                  logger.info("Extracting the Asset");
               }
               martCreationOutputInfo.setCreationSuccessful(martAssetExtractionService
                        .extractAsset(martCreationOutputInfo));
            }
         }

      } catch (MartInputValidatorException e) {
         throw new DataMartCreationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_INPUT_VALIDATION_EXCEPTION_CODE, e);
      } catch (MartWarehouseExtractionException e) {
         throw new DataMartCreationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_WAREHOUSE_EXTRACTION_EXCEPTION_CODE, e);
      } catch (MartMergePopulationException e) {
         throw new DataMartCreationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_MERGE_POPULATION_EXCEPTION_CODE, e);
      } catch (MartPopulationSliceException e) {
         throw new DataMartCreationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_POPULATION_SLICING_EXCEPTION_CODE, e);
      } catch (MartContextPopulationException e) {
         throw new DataMartCreationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_CONEXT_POPULATION_EXCEPTION_CODE, e);
      } catch (MartResourceCleanupException e) {
         throw new DataMartCreationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_RESOURCE_CLEANUP_EXCEPTION_CODE, e);
      } catch (MartAssetExtractionException e) {
         throw new DataMartCreationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_ASSET_EXTRACTION_EXCEPTION_CODE, e);
      } catch (InterruptedException e) {
         throw new DataMartCreationException(AnswersCatalogExceptionCodes.DEFAULT_THREAD_POOL_RELATED_EXCEPTION_CODE, e);
      } catch (ExecutionException e) {
         throw new DataMartCreationException(AnswersCatalogExceptionCodes.DEFAULT_THREAD_POOL_RELATED_EXCEPTION_CODE, e);
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
      return martCreationOutputInfo;
   }

   private void populateFractionalDataSetTable (MartCreationOutputInfo martCreationOutputInfo, String dimension)
            throws DataMartCreationException {
      try {
         // step 2. populating the fractional temp tables.
         if (logger.isInfoEnabled()) {
            logger.info("Fractional Dataset Temp table Creation for dimension : " + dimension);
         }
         MartFractionalDataSetTempTableStructure fractionalDataSetTempTable = martFractionalDatasetPopulationService
                  .createFractionalDataset(martCreationOutputInfo, dimension);
         martCreationOutputInfo.addFractionalDataSetTempTable(fractionalDataSetTempTable);
         if (logger.isInfoEnabled()) {
            logger.info("Fractional Dataset Temp table name " + fractionalDataSetTempTable.getTableName());
         }
         // step 3. populating the fractional tables.
         if (logger.isInfoEnabled()) {
            logger.info("Fractional Dataset table Creation and Sfactor population for dimension : " + dimension);
         }
         MartFractionalDatasetTableStructure fractionalDatasetTable = martSfactorPopulationService.sfactorPopulation(
                  martCreationOutputInfo, fractionalDataSetTempTable, dimension);
         martCreationOutputInfo.addFractionalDataSetTable(fractionalDatasetTable);
         if (logger.isInfoEnabled()) {
            logger.info("Fractional Dataset table name " + fractionalDatasetTable.getTableName());
         }
      } catch (MartSfactorPopulationException e) {
         throw new DataMartCreationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_SFACTOR_POPULATION_EXCEPTION_CODE, e);
      } catch (MartFractionalDatasetPopulationException e) {
         throw new DataMartCreationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_FRACTIONAL_DATASETS_POPULATION_EXCEPTION_CODE, e);
      }
   }

   class FractionalDataSetTablePopulation implements Callable<ThreadPoolTaskStatus> {

      private MartCreationOutputInfo martCreationOutputInfo;
      private String                 dimension;

      public FractionalDataSetTablePopulation (MartCreationOutputInfo martCreationOutputInfo, String dimension) {
         super();
         this.martCreationOutputInfo = martCreationOutputInfo;
         this.dimension = dimension;
      }

      @Override
      public ThreadPoolTaskStatus call () throws Exception {
         ThreadPoolTaskStatus threadPoolTaskStatus = new ThreadPoolTaskStatus();
         threadPoolTaskStatus.setTaskStatusType(ThreadPoolTaskStatusType.STARTED);
         try {
            populateFractionalDataSetTable(this.martCreationOutputInfo, this.dimension);
            threadPoolTaskStatus.setTaskStatusType(ThreadPoolTaskStatusType.SUCCESS);
         } catch (Exception exception) {
            threadPoolTaskStatus.setTaskStatusType(ThreadPoolTaskStatusType.FAILED);
            threadPoolTaskStatus.setFailureReason(exception.getMessage());
            logger.error(exception, exception);
         }
         return threadPoolTaskStatus;
      }
   }

   public void setMartContextPopulationService (IMartContextPopulationService martContextPopulationService) {
      this.martContextPopulationService = martContextPopulationService;
   }

   public IMartPopulationSliceService getMartPopulationSliceService () {
      return martPopulationSliceService;
   }

   public void setMartPopulationSliceService (IMartPopulationSliceService martPopulationSliceService) {
      this.martPopulationSliceService = martPopulationSliceService;
   }

   public IMartFractionalDatasetPopulationService getMartFractionalDatasetPopulationService () {
      return martFractionalDatasetPopulationService;
   }

   public void setMartFractionalDatasetPopulationService (
            IMartFractionalDatasetPopulationService martFractionalDatasetPopulationService) {
      this.martFractionalDatasetPopulationService = martFractionalDatasetPopulationService;
   }

   public IMartSfactorPopulationService getMartSfactorPopulationService () {
      return martSfactorPopulationService;
   }

   public void setMartSfactorPopulationService (IMartSfactorPopulationService martSfactorPopulationService) {
      this.martSfactorPopulationService = martSfactorPopulationService;
   }

   public IMartWarehouseExtractionService getMartWarehouseExtractionService () {
      return martWarehouseExtractionService;
   }

   public void setMartWarehouseExtractionService (IMartWarehouseExtractionService martWarehouseExtractionService) {
      this.martWarehouseExtractionService = martWarehouseExtractionService;
   }

   public IMartMergePopulationService getMartMergePopulationService () {
      return martMergePopulationService;
   }

   public void setMartMergePopulationService (IMartMergePopulationService martMergePopulationService) {
      this.martMergePopulationService = martMergePopulationService;
   }

   public IMartInputValidatorService getMartInputValidatorService () {
      return martInputValidatorService;
   }

   public void setMartInputValidatorService (IMartInputValidatorService martInputValidatorService) {
      this.martInputValidatorService = martInputValidatorService;
   }

   public IMartResourceCleanupService getMartResourceCleanupService () {
      return martResourceCleanupService;
   }

   public void setMartResourceCleanupService (IMartResourceCleanupService martResourceCleanupService) {
      this.martResourceCleanupService = martResourceCleanupService;
   }

   public IMartContextPopulationService getMartContextPopulationService () {
      return martContextPopulationService;
   }

}
