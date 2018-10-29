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

import org.apache.log4j.Logger;

import com.execue.ac.bean.AnswersCatalogConfigurationContext;
import com.execue.ac.bean.MartConfigurationContext;
import com.execue.ac.bean.MartCreationInputInfo;
import com.execue.ac.bean.MartCreationOutputInfo;
import com.execue.ac.bean.MartCreationPopulatedContext;
import com.execue.ac.bean.MartFractionalDatasetTableStructure;
import com.execue.ac.bean.MartFractionalPopulationTableStructure;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.MartMergePopulationException;
import com.execue.ac.helper.MartCreationServiceHelper;
import com.execue.ac.service.IAnswersCatalogDataAccessService;
import com.execue.ac.service.IAnswersCatalogSQLQueryGenerationService;
import com.execue.ac.service.IMartCreationConstants;
import com.execue.ac.service.IMartMergePopulationService;
import com.execue.ac.service.IMartOperationalConstants;
import com.execue.ac.util.AnswersCatalogUtil;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.querygen.FromEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SQLTable;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessException;

/**
 * This service represents the step 4 of mart. It means merge all population tables and de-dup them and create final
 * samples population table.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class MartMergePopulationServiceImpl implements IMartMergePopulationService, IMartCreationConstants,
         IMartOperationalConstants {

   private IAnswersCatalogSQLQueryGenerationService answersCatalogSQLQueryGenerationService;
   private IAnswersCatalogDataAccessService         answersCatalogDataAccessService;
   private MartCreationServiceHelper                martCreationServiceHelper;
   private static final Logger                      logger = Logger.getLogger(MartMergePopulationServiceImpl.class);

   /**
    * This method merges all the fractional datasets creates one per dimension. It de-dups the records and creates final
    * merged table which contains unique sampled population which will be used for extraction of the warehouse.
    * 
    * @param martCreationOutputInfo
    * @return martFractionalPopulationTableStructure
    * @throws MartMergePopulationException
    */
   public MartFractionalPopulationTableStructure mergeSampledPopulation (MartCreationOutputInfo martCreationOutputInfo)
            throws MartMergePopulationException {
      MartCreationInputInfo martCreationInputInfo = martCreationOutputInfo.getMartCreationInputInfo();
      MartCreationContext martCreationContext = martCreationInputInfo.getMartCreationContext();
      MartFractionalPopulationTableStructure fractionalPopulationTableStructure = null;
      JobRequest jobRequest = martCreationContext.getJobRequest();
      Exception exception = null;
      JobOperationalStatus jobOperationalStatus = null;
      try {
         Asset targetAsset = martCreationContext.getTargetAsset();
         DataSource targetDataSource = targetAsset.getDataSource();
         AssetProviderType targetProviderType = targetDataSource.getProviderType();

         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  PREPARE_SAMPLED_POPULATION_TABLE);

         // prepare the fractional population table structure.
         fractionalPopulationTableStructure = populateFractionalPopulationTableStructure(martCreationOutputInfo);

         // prepare the create table columns sequence for temp merged table.
         SQLTable createFractionalPopulationTempTable = populateCreateTableColumnsSequence(
                  fractionalPopulationTableStructure, fractionalPopulationTableStructure.getMergedTempTableName());

         QueryTable fractionalPopulationTempQueryTable = ExecueBeanManagementUtil.prepareQueryTable(
                  fractionalPopulationTableStructure.getMergedTempTableName(), null, targetDataSource.getOwner());

         // prepare the create table query string for temp merged table.
         String createFractionalPopulationTempTableQuery = answersCatalogSQLQueryGenerationService
                  .prepareCreateTableQuery(targetProviderType, fractionalPopulationTempQueryTable,
                           createFractionalPopulationTempTable.getColumns());

         // prepare the insert table columns sequence for temp merged table.
         List<QueryColumn> fractionalPopulationTempTableColumnSequence = populateInsertTableColumnsSequence(fractionalPopulationTableStructure);

         // prepare the insert table query string for temp merged table.
         String insertFractionalPopulationTempTableQuery = answersCatalogSQLQueryGenerationService.prepareInsertQuery(
                  targetProviderType, fractionalPopulationTempQueryTable, fractionalPopulationTempTableColumnSequence);

         if (logger.isDebugEnabled()) {
            logger.debug("Create Temp Table Query " + createFractionalPopulationTempTableQuery);
         }

         if (logger.isDebugEnabled()) {
            logger.debug("Insert Temp Table Query " + insertFractionalPopulationTempTableQuery);
         }

         // for each fractional dataset table prepare select query and dump the data into temp table.
         for (MartFractionalDatasetTableStructure fractionalDatasetTableStructure : martCreationOutputInfo
                  .getFractionalDataSetTables()) {
            // prepare select query for fractional dataset table in picture.
            Query mergeSelectQuery = populateMergeSelectQuery(fractionalDatasetTableStructure, targetDataSource);
            // generate query string
            String mergeSelectQueryString = answersCatalogSQLQueryGenerationService.prepareSelectQuery(targetAsset,
                     mergeSelectQuery);
            if (logger.isDebugEnabled()) {
               logger.debug("Select Temp Table Query for Dataset " + fractionalDatasetTableStructure.getTableName()
                        + " : " + mergeSelectQueryString);
            }
            getMartCreationServiceHelper().transferLocalData(targetDataSource,
                     fractionalPopulationTableStructure.getMergedTempTableName(),
                     createFractionalPopulationTempTableQuery, insertFractionalPopulationTempTableQuery,
                     mergeSelectQueryString);
         }

         // NOTE: Create needed indexes on merged population temp fx table
         createIndexesOnFractionalPopulationTempTable(martCreationInputInfo.getAnswersCatalogConfigurationContext(),
                  martCreationContext, fractionalPopulationTableStructure);

         // prepare the create table columns sequence for final merged table.
         SQLTable createFractionalPopulationTable = populateCreateTableColumnsSequence(
                  fractionalPopulationTableStructure, fractionalPopulationTableStructure.getMergedTableName());

         QueryTable fractionalPopulationQueryTable = ExecueBeanManagementUtil.prepareQueryTable(
                  fractionalPopulationTableStructure.getMergedTableName(), null, targetDataSource.getOwner());

         // prepare the create query string for final merged table.
         String createFractionalPopulationTableQuery = answersCatalogSQLQueryGenerationService.prepareCreateTableQuery(
                  targetProviderType, fractionalPopulationQueryTable, createFractionalPopulationTable.getColumns());

         // prepare the insert table columns sequence for final merged table.
         List<QueryColumn> fractionalPopulationColumnSequence = populateInsertTableColumnsSequence(fractionalPopulationTableStructure);

         // prepare the insert query string for final merged table.
         String insertFractionalPopulationTableQuery = answersCatalogSQLQueryGenerationService.prepareInsertQuery(
                  targetProviderType, fractionalPopulationQueryTable, fractionalPopulationColumnSequence);

         if (logger.isDebugEnabled()) {
            logger.debug("Create Merged Table Query " + createFractionalPopulationTableQuery);
         }

         if (logger.isDebugEnabled()) {
            logger.debug("Insert Merged Table Query " + insertFractionalPopulationTableQuery);
         }

         // prepare the de-dup based select query
         Query deDupSelectQuery = populateDeDupSelectQuery(fractionalPopulationTableStructure, targetDataSource);

         // prepare the de-dup based select query string
         String deDupSelectQueryString = answersCatalogSQLQueryGenerationService.prepareSelectQuery(targetAsset,
                  deDupSelectQuery);

         if (logger.isDebugEnabled()) {
            logger.debug("Dedup Select Query " + deDupSelectQueryString);
         }

         getMartCreationServiceHelper().transferLocalData(targetDataSource, createFractionalPopulationTableQuery,
                  insertFractionalPopulationTableQuery, deDupSelectQueryString);

         // NOTE: Create needed indexes on population fx table
         createIndexesOnFractionalPopulationTable(martCreationInputInfo.getAnswersCatalogConfigurationContext(),
                  martCreationContext, fractionalPopulationTableStructure);

         // populate the record count of the sampled population
         Long recordCount = answersCatalogDataAccessService.getStatBasedColumnValue(targetDataSource,
                  fractionalPopulationQueryTable, fractionalPopulationTableStructure.getPopulationColumn(),
                  StatType.COUNT);
         if (logger.isDebugEnabled()) {
            logger.debug("Sampled Population Record Count " + recordCount);
         }
         fractionalPopulationTableStructure.setPopulationCount(recordCount);

         getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
      } catch (AnswersCatalogException e) {
         exception = e;
         throw new MartMergePopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_MERGE_POPULATION_EXCEPTION_CODE, e);
      } catch (DataAccessException e) {
         exception = e;
         throw new MartMergePopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_MERGE_POPULATION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getMartCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new MartMergePopulationException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return fractionalPopulationTableStructure;
   }

   private void createIndexesOnFractionalPopulationTable (
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext,
            MartCreationContext martCreationContext,
            MartFractionalPopulationTableStructure fractionalPopulationTableStructure) throws DataAccessException {
      DataSource targetDataSource = martCreationContext.getTargetAsset().getDataSource();

      // Index on Population column
      getAnswersCatalogDataAccessService().createIndexOnTable(
               targetDataSource,
               ExecueBeanManagementUtil.prepareSQLIndex(fractionalPopulationTableStructure.getMergedTableName(),
                        fractionalPopulationTableStructure.getPopulationColumn().getColumnName()),
               answersCatalogConfigurationContext.getMaxDBObjectLength());
   }

   private void createIndexesOnFractionalPopulationTempTable (
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext,
            MartCreationContext martCreationContext,
            MartFractionalPopulationTableStructure fractionalPopulationTableStructure) throws DataAccessException {
      DataSource targetDataSource = martCreationContext.getTargetAsset().getDataSource();

      // Index on Population column
      getAnswersCatalogDataAccessService().createIndexOnTable(
               targetDataSource,
               ExecueBeanManagementUtil.prepareSQLIndex(fractionalPopulationTableStructure.getMergedTempTableName(),
                        fractionalPopulationTableStructure.getPopulationColumn().getColumnName()),
               answersCatalogConfigurationContext.getMaxDBObjectLength());
   }

   /**
    * This method prepares the de dup select query from temp merged population table.
    * 
    * @param fractionalPopulationTableStructure
    * @param targetDataSource 
    * @return query
    */
   private Query populateDeDupSelectQuery (MartFractionalPopulationTableStructure fractionalPopulationTableStructure,
            DataSource targetDataSource) {
      QueryTable queryTable = AnswersCatalogUtil.createQueryTable(fractionalPopulationTableStructure
               .getMergedTempTableName(), targetDataSource.getOwner());
      QueryColumn populationColumn = fractionalPopulationTableStructure.getPopulationColumn();
      QueryColumn sfactorColumn = fractionalPopulationTableStructure.getSfactorColumn();

      // preparing the select entities.
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      List<String> existingAliases = new ArrayList<String>();

      SelectEntity populationSelectEntity = new SelectEntity();
      populationSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      populationSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(ExecueBeanManagementUtil
               .prepareQueryTableColumn(populationColumn, queryTable)));
      populationSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
      selectEntities.add(populationSelectEntity);

      // preparing the sfactor column. We will use min(sfactor) if there are multiple occurences of the same population
      // in temp table.
      SelectEntity sfactorSelectEntity = new SelectEntity();
      sfactorSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      sfactorSelectEntity.setFunctionName(StatType.MINIMUM);
      sfactorSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(ExecueBeanManagementUtil
               .prepareQueryTableColumn(sfactorColumn, queryTable)));
      sfactorSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
      selectEntities.add(sfactorSelectEntity);

      // preparing the from entities
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      fromEntities.add(ExecueBeanManagementUtil.createFromEntityObject(queryTable));

      // preparing the group entities
      List<SelectEntity> groupEntities = new ArrayList<SelectEntity>();
      groupEntities.add(populationSelectEntity);

      Query dedupSelectQuery = new Query();
      dedupSelectQuery.setSelectEntities(selectEntities);
      dedupSelectQuery.setGroupingEntities(groupEntities);
      dedupSelectQuery.setFromEntities(fromEntities);
      return dedupSelectQuery;

   }

   /**
    * This method prepares the select query from fractional dataset table. It selects population and sfactor column from
    * fractional dataset table.
    * 
    * @param fractionalDatasetTableStructure
    * @param targetDataSource 
    * @return query
    */
   private Query populateMergeSelectQuery (MartFractionalDatasetTableStructure fractionalDatasetTableStructure,
            DataSource targetDataSource) {
      QueryTable queryTable = AnswersCatalogUtil.createQueryTable(fractionalDatasetTableStructure.getTableName(),
               targetDataSource.getOwner());
      QueryColumn populationColumn = fractionalDatasetTableStructure.getPopulationColumn();
      QueryColumn sfactorColumn = fractionalDatasetTableStructure.getSfactorColumn();

      // preparing the select entities.
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      List<String> existingAliases = new ArrayList<String>();

      SelectEntity populationSelectEntity = new SelectEntity();
      populationSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      populationSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(ExecueBeanManagementUtil
               .prepareQueryTableColumn(populationColumn, queryTable)));
      populationSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
      selectEntities.add(populationSelectEntity);

      SelectEntity sfactorSelectEntity = new SelectEntity();
      sfactorSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      sfactorSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(ExecueBeanManagementUtil
               .prepareQueryTableColumn(sfactorColumn, queryTable)));
      sfactorSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
      selectEntities.add(sfactorSelectEntity);

      // preparing the from entities
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      fromEntities.add(ExecueBeanManagementUtil.createFromEntityObject(queryTable));

      Query mergeSelectQuery = new Query();
      mergeSelectQuery.setSelectEntities(selectEntities);
      mergeSelectQuery.setFromEntities(fromEntities);
      return mergeSelectQuery;

   }

   /**
    * This method prepares the insert table columns sequence
    * 
    * @param fractionalPopulationTableStructure
    * @param tableName
    * @return sqlTable.
    */
   private List<QueryColumn> populateInsertTableColumnsSequence (
            MartFractionalPopulationTableStructure fractionalPopulationTableStructure) {
      List<QueryColumn> sequenceCreateTableColumns = new ArrayList<QueryColumn>();
      sequenceCreateTableColumns.add(fractionalPopulationTableStructure.getPopulationColumn());
      sequenceCreateTableColumns.add(fractionalPopulationTableStructure.getSfactorColumn());
      return sequenceCreateTableColumns;
   }

   /**
    * This method prepares create table columns sequence
    * 
    * @param fractionalPopulationTableStructure
    * @param tableName
    * @return sqlTable
    */
   private SQLTable populateCreateTableColumnsSequence (
            MartFractionalPopulationTableStructure fractionalPopulationTableStructure, String tableName) {
      List<QueryColumn> sequenceCreateTableColumns = new ArrayList<QueryColumn>();
      sequenceCreateTableColumns.add(fractionalPopulationTableStructure.getPopulationColumn());
      sequenceCreateTableColumns.add(fractionalPopulationTableStructure.getSfactorColumn());
      return ExecueBeanManagementUtil.prepareSQLTableFromQueryColumns(tableName, sequenceCreateTableColumns);
   }

   /**
    * This method prepares the mart fractional population table structure.
    * 
    * @param martCreationOutputInfo
    * @return martFractionalPopulationTableStructure
    * @throws AnswersCatalogException
    * @throws DataAccessException
    */
   private MartFractionalPopulationTableStructure populateFractionalPopulationTableStructure (
            MartCreationOutputInfo martCreationOutputInfo) throws AnswersCatalogException, DataAccessException {
      MartCreationInputInfo martCreationInputInfo = martCreationOutputInfo.getMartCreationInputInfo();
      AnswersCatalogConfigurationContext answersCatalogConfigurationContext = martCreationInputInfo
               .getAnswersCatalogConfigurationContext();
      MartConfigurationContext martConfigurationContext = martCreationInputInfo.getMartConfigurationContext();
      MartCreationContext martCreationContext = martCreationInputInfo.getMartCreationContext();
      MartCreationPopulatedContext martCreationPopulatedContext = martCreationInputInfo
               .getMartCreationPopulatedContext();
      DataSource dataSource = martCreationContext.getTargetAsset().getDataSource();
      int maxDbObjectLength = answersCatalogConfigurationContext.getMaxDBObjectLength();

      // preparing the table names and check for uniqueness.

      List<String> existingTableNames = new ArrayList<String>();
      String assetName = martCreationContext.getTargetAsset().getName();

      // preparing the temp merged table
      String staticFractionalPopulationTempTable = martConfigurationContext.getFractionalPopulationTempTableName();
      String finalFractionalPopulationTempTable = staticFractionalPopulationTempTable + UNDERSCORE + assetName;
      finalFractionalPopulationTempTable = getAnswersCatalogDataAccessService().getUniqueNonExistentTableName(
               dataSource, finalFractionalPopulationTempTable, existingTableNames, maxDbObjectLength);

      existingTableNames.add(finalFractionalPopulationTempTable);

      // preparing the final merged table
      String staticFractionalPopulationTable = martConfigurationContext.getFractionalPopulationTableName();
      String finalFractionalPopulationTable = staticFractionalPopulationTable + UNDERSCORE + assetName;
      finalFractionalPopulationTable = getAnswersCatalogDataAccessService().getUniqueNonExistentTableName(dataSource,
               finalFractionalPopulationTable, existingTableNames, maxDbObjectLength);

      // preparing the columns and check for uniqueness.
      List<String> existingColumnNames = new ArrayList<String>();

      // preparing the population column.
      QueryColumn populationQueryColumn = ExecueBeanCloneUtil.cloneQueryColumn(martCreationPopulatedContext
               .getSourceAssetMappingInfo().getPopulatedPopulation().getQueryColumn());
      populationQueryColumn.setDefaultValue(null);
      populationQueryColumn.setNullable(true);
      AnswersCatalogUtil.maintainColumnUniqueness(populationQueryColumn, existingColumnNames, maxDbObjectLength);

      // preparing the sfactor column
      QueryColumn sfactorColumn = martConfigurationContext.getFractionalTableSfactorColumn();
      AnswersCatalogUtil.maintainColumnUniqueness(sfactorColumn, existingColumnNames, maxDbObjectLength);

      MartFractionalPopulationTableStructure fractionalPopulationTableStructure = new MartFractionalPopulationTableStructure();
      fractionalPopulationTableStructure.setMergedTempTableName(finalFractionalPopulationTempTable);
      fractionalPopulationTableStructure.setMergedTableName(finalFractionalPopulationTable);
      fractionalPopulationTableStructure.setPopulationColumn(populationQueryColumn);
      fractionalPopulationTableStructure.setSfactorColumn(sfactorColumn);
      return fractionalPopulationTableStructure;

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

}
