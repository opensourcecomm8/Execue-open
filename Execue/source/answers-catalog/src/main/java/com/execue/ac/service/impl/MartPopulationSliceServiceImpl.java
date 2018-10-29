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

import org.apache.log4j.Logger;

import com.execue.ac.bean.AnswersCatalogConfigurationContext;
import com.execue.ac.bean.MartConfigurationContext;
import com.execue.ac.bean.MartCreationInputInfo;
import com.execue.ac.bean.MartCreationOutputInfo;
import com.execue.ac.bean.MartCreationPopulatedContext;
import com.execue.ac.bean.MartInputParametersContext;
import com.execue.ac.bean.MartPopulationTableStructure;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.MartPopulationSliceException;
import com.execue.ac.helper.MartCreationServiceHelper;
import com.execue.ac.service.IAnswersCatalogDataAccessService;
import com.execue.ac.service.IAnswersCatalogSQLQueryGenerationService;
import com.execue.ac.service.IMartCreationConstants;
import com.execue.ac.service.IMartOperationalConstants;
import com.execue.ac.service.IMartPopulationSliceService;
import com.execue.ac.service.IRandomNumberGeneratorService;
import com.execue.ac.util.AnswersCatalogUtil;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryFormula;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryTableColumn;
import com.execue.core.common.bean.querygen.SQLIndex;
import com.execue.core.common.bean.querygen.SQLTable;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.QueryFormulaType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.util.queryadaptor.ISQLAdaptor;
import com.execue.util.queryadaptor.SQLAdaptorFactory;
import com.execue.util.querygen.IQueryGenerationUtilService;
import com.execue.util.querygen.QueryGenerationUtilServiceFactory;

/**
 * This service represents the step1 of mart creation. It creates population table copy and assign slice_number to each
 * record.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class MartPopulationSliceServiceImpl implements IMartPopulationSliceService, IMartCreationConstants,
         IMartOperationalConstants {

   private IRandomNumberGeneratorService            randomNumberGeneratorService;
   private IAnswersCatalogSQLQueryGenerationService answersCatalogSQLQueryGenerationService;
   private MartCreationServiceHelper                martCreationServiceHelper;
   private QueryGenerationUtilServiceFactory        queryGenerationUtilServiceFactory;
   private IAnswersCatalogDataAccessService         answersCatalogDataAccessService;
   private static final Logger                      logger = Logger.getLogger(MartPopulationSliceServiceImpl.class);

   /**
    * This method slices the population across numberOfSlices using random number approach.
    * 
    * @param martCreationOutputInfo
    * @return martPopulationTableStructure
    * @throws MartPopulationSliceException
    */
   public MartPopulationTableStructure populationSlicing (MartCreationOutputInfo martCreationOutputInfo)
            throws MartPopulationSliceException {
      MartCreationInputInfo martCreationInputInfo = martCreationOutputInfo.getMartCreationInputInfo();
      MartCreationContext martCreationContext = martCreationInputInfo.getMartCreationContext();
      MartPopulationTableStructure populationTableStructure = null;
      JobRequest jobRequest = martCreationContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      Exception exception = null;
      try {
         Asset sourceAsset = martCreationContext.getSourceAsset();
         Asset targetAsset = martCreationContext.getTargetAsset();
         DataSource sourceDataSource = sourceAsset.getDataSource();
         DataSource targetDataSource = targetAsset.getDataSource();
         AssetProviderType targetProviderType = targetAsset.getDataSource().getProviderType();
         boolean randomNumberGeneratorDBApproach = martCreationInputInfo.getMartConfigurationContext()
                  .isRandomNumberGeneratorDBApproach();
         boolean autoIncrementClauseSupported = getQueryGenerationUtilService(targetProviderType)
                  .isAutoIncrementClauseSupported();

         if (logger.isDebugEnabled()) {
            logger.debug("Random Number DB approach " + randomNumberGeneratorDBApproach);
         }

         if (logger.isDebugEnabled()) {
            logger.debug("Auto Increment Clause Supported By Provider " + autoIncrementClauseSupported);
         }

         // generate the population table structure
         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  GENERATE_POPULATION_TABLE_STRUCTURE);
         populationTableStructure = populatePopulationTableStructure(martCreationInputInfo);

         // generate the create table columns sequence
         SQLTable sequenceCreateTableColumns = populateCreateTableColumnsSequence(populationTableStructure,
                  autoIncrementClauseSupported);

         QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(populationTableStructure.getTableName(),
                  null, targetDataSource.getOwner());

         // generate create table query
         String populationCreateTableQuery = answersCatalogSQLQueryGenerationService.prepareCreateTableQuery(
                  targetProviderType, queryTable, sequenceCreateTableColumns.getColumns());

         // generate the select query
         Query selectQuery = populateSelectQueryObject(populationTableStructure);

         // generate select table query
         String populationSelectQuery = answersCatalogSQLQueryGenerationService.prepareSelectQuery(sourceAsset,
                  selectQuery);

         if (logger.isDebugEnabled()) {
            logger.debug("Create Table Query :  " + populationCreateTableQuery);
         }

         if (logger.isDebugEnabled()) {
            logger.debug("Select Query :  " + populationSelectQuery);
         }

         // generate the insert table columns sequence
         List<QueryColumn> sequenceInsertTableColumns = populateInsertTableColumnsSequence(populationTableStructure);

         // if target data source same as source data source means we are building mart at source location,
         // we can use insert into select rather than ETL
         if (martCreationContext.isTargetDataSourceSameAsSourceDataSource()) {
            String populationInsertQuery = answersCatalogSQLQueryGenerationService.prepareInsertQuery(
                     targetProviderType, queryTable, sequenceInsertTableColumns);
            if (logger.isDebugEnabled()) {
               logger.debug("Insert Query :  " + populationInsertQuery);
            }
            getMartCreationServiceHelper().transferLocalData(targetDataSource, populationCreateTableQuery,
                     populationInsertQuery, populationSelectQuery);
         } else {
            Map<String, String> selectQueryAliases = populateSelectQueryAliases(populationTableStructure);
            String populationETLInsertQuery = answersCatalogSQLQueryGenerationService.prepareETLInsertQuery(
                     targetProviderType, queryTable, sequenceInsertTableColumns, selectQueryAliases);
            if (logger.isDebugEnabled()) {
               logger.debug("Insert ETL Query :  " + populationETLInsertQuery);
            }
            getMartCreationServiceHelper().transferRemoteData(sourceDataSource, targetDataSource,
                     populationETLInsertQuery, populationSelectQuery, populationCreateTableQuery,
                     populationTableStructure.getTableName());
         }
         getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         // populate the ID column if provider doesn't support auto increment clause
         if (!autoIncrementClauseSupported) {
            // populate the ID column if provider is oracle.
            if (AssetProviderType.Oracle.equals(targetProviderType)) {
               if (logger.isDebugEnabled()) {
                  logger.debug("Populating the ID Column Data");
               }
               getMartCreationServiceHelper().populateIdColumnData(targetDataSource,
                        populationTableStructure.getTableName(), populationTableStructure.getIdColumn());
            }
            // handle other providers if auto increment clause not supported
         }

         // populate slice_number column if db approach is not followed.
         if (!randomNumberGeneratorDBApproach) {
            if (logger.isDebugEnabled()) {
               logger.debug("Populating the Slice Number Column");
            }
            jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                     SLICE_NUMBER_COLUMN_POPULATION);
            Integer numberOfSlices = martCreationInputInfo.getMartCreationInputParametersContext().getNumberOfSlices();
            populateSliceNumberColumn(targetDataSource, populationTableStructure, numberOfSlices);
            getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
         }

         // Create indexes on this local population table for Population and Slice Number columns
         if (logger.isDebugEnabled()) {
            logger.debug("Creating indexes on population table");
         }
         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  INDEX_CREATION_ON_POPULATION_TABLE);
         createIndexesOnPopulationTable(martCreationInputInfo.getAnswersCatalogConfigurationContext(),
                  martCreationContext, populationTableStructure);
         getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

      } catch (AnswersCatalogException e) {
         exception = e;
         throw new MartPopulationSliceException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_POPULATION_SLICING_EXCEPTION_CODE, e);
      } catch (DataAccessException e) {
         exception = e;
         throw new MartPopulationSliceException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_POPULATION_SLICING_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getMartCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new MartPopulationSliceException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return populationTableStructure;
   }

   /**
    * Creates indexes on Population Column and Slice Number column
    *   NOTE: Index on ID column is not needed as it is a PRIMARY KEY
    * 
    * @param answersCatalogConfigurationContext
    * @param martCreationContext
    * @param populationTableStructure
    * @throws DataAccessException
    */
   private void createIndexesOnPopulationTable (AnswersCatalogConfigurationContext answersCatalogConfigurationContext,
            MartCreationContext martCreationContext, MartPopulationTableStructure populationTableStructure)
            throws DataAccessException {
      DataSource targetDataSource = martCreationContext.getTargetAsset().getDataSource();

      List<SQLIndex> indexes = new ArrayList<SQLIndex>();

      // Index on Population column
      indexes.add(ExecueBeanManagementUtil.prepareSQLIndex(populationTableStructure.getTableName(),
               populationTableStructure.getPopulationColumn().getColumnName()));

      // Index on Slice Number column
      indexes.add(ExecueBeanManagementUtil.prepareSQLIndex(populationTableStructure.getTableName(),
               populationTableStructure.getSliceNumberColumn().getColumnName()));

      getAnswersCatalogDataAccessService().createMultipleIndexesOnTable(targetDataSource, indexes,
               answersCatalogConfigurationContext.getMaxDBObjectLength());

   }

   /**
    * This method populates the slice number column. This will be used if DB approach for random number assigment is not
    * followed. Read the min and max id value and for each id value generate a random number and update the row with
    * that number
    * 
    * @param targetDataSource
    * @param populationTableStructure
    * @param numberOfSlices
    * @throws AnswersCatalogException
    * @throws DataAccessException
    */
   private void populateSliceNumberColumn (DataSource targetDataSource,
            MartPopulationTableStructure populationTableStructure, Integer numberOfSlices)
            throws AnswersCatalogException, DataAccessException {

      String tableName = populationTableStructure.getTableName();
      QueryTable queryTable = AnswersCatalogUtil.createQueryTable(tableName, targetDataSource.getOwner());

      Long minIdColumnValue = answersCatalogDataAccessService.getStatBasedColumnValue(targetDataSource, queryTable,
               populationTableStructure.getIdColumn(), StatType.MINIMUM);
      Long maxIdColumnValue = answersCatalogDataAccessService.getStatBasedColumnValue(targetDataSource, queryTable,
               populationTableStructure.getIdColumn(), StatType.MAXIMUM);

      QueryColumn idColumn = ExecueBeanCloneUtil.cloneQueryColumn(populationTableStructure.getIdColumn());
      QueryColumn sliceNumberColumn = ExecueBeanCloneUtil.cloneQueryColumn(populationTableStructure
               .getSliceNumberColumn());
      // for each row , generate random number and fire an update statement to update the slice number column.
      for (Long idColumnValue = minIdColumnValue; idColumnValue <= maxIdColumnValue; idColumnValue++) {
         Integer nextRandomElement = randomNumberGeneratorService.getNextRandomElement(numberOfSlices);
         idColumn.setDefaultValue(idColumnValue.toString());
         sliceNumberColumn.setDefaultValue(nextRandomElement.toString());
         List<ConditionEntity> setConditions = new ArrayList<ConditionEntity>();
         setConditions.add(AnswersCatalogUtil.prepareConditionEntity(queryTable, idColumn, idColumnValue.toString()));
         List<ConditionEntity> whereConditions = new ArrayList<ConditionEntity>();
         whereConditions.add(AnswersCatalogUtil.prepareConditionEntity(queryTable, sliceNumberColumn, nextRandomElement
                  .toString()));
         answersCatalogDataAccessService.executeUpdateStatement(targetDataSource, queryTable, setConditions,
                  whereConditions);
      }
   }

   /**
    * This method prepares select query. It adds slice number select entity if db approach is followed.
    * 
    * @param populationTableStructure
    * @return query
    */
   private Query populateSelectQueryObject (MartPopulationTableStructure populationTableStructure) {
      List<SelectEntity> sequenceSelectTableColumns = new ArrayList<SelectEntity>();
      sequenceSelectTableColumns.add(populationTableStructure.getPopulationSelectEntity());
      if (populationTableStructure.isDbApproachForSliceNumber()) {
         sequenceSelectTableColumns.add(populationTableStructure.getSliceNumberSelectEntity());
      }
      Query query = new Query();
      query.setSelectEntities(sequenceSelectTableColumns);
      return query;
   }

   /**
    * This method populates the insert table columns sequence
    * 
    * @param populationTableStructure
    * @return sqlTable
    */
   private List<QueryColumn> populateInsertTableColumnsSequence (MartPopulationTableStructure populationTableStructure) {
      List<QueryColumn> sequenceInsertTableColumns = new ArrayList<QueryColumn>();
      sequenceInsertTableColumns.add(populationTableStructure.getPopulationColumn());
      // if we are populating slice number using DB approach then add slice number column to insert section as well as
      // select section, else we will populate manually later
      if (populationTableStructure.isDbApproachForSliceNumber()) {
         sequenceInsertTableColumns.add(populationTableStructure.getSliceNumberColumn());
      }
      return sequenceInsertTableColumns;
   }

   /**
    * This method prepares the create table columns sequence.
    * 
    * @param populationTableStructure
    * @param targetProviderType
    * @return sqlTable
    */
   private SQLTable populateCreateTableColumnsSequence (MartPopulationTableStructure populationTableStructure,
            boolean autoIncrementClauseSupported) {
      QueryColumn idColumn = populationTableStructure.getIdColumn();
      // if provider type have support for auto increment, we will use auto
      // increment to generate the id column.Else we will generate Id column later
      if (autoIncrementClauseSupported) {
         idColumn = ExecueBeanCloneUtil.cloneQueryColumn(idColumn);
         idColumn.setAutoIncrement(true);
      }
      List<QueryColumn> sequenceCreateTableColumns = new ArrayList<QueryColumn>();
      sequenceCreateTableColumns.add(idColumn);
      sequenceCreateTableColumns.add(populationTableStructure.getPopulationColumn());
      sequenceCreateTableColumns.add(populationTableStructure.getSliceNumberColumn());
      return ExecueBeanManagementUtil.prepareSQLTableFromQueryColumns(populationTableStructure.getTableName(),
               sequenceCreateTableColumns);
   }

   /**
    * This method prepares the map between insert and select as will be used in ETL process.
    * 
    * @param populationTableStructure
    * @return selectQueryAliases
    */
   private Map<String, String> populateSelectQueryAliases (MartPopulationTableStructure populationTableStructure) {
      Map<String, String> selectQueryAliases = new HashMap<String, String>();
      selectQueryAliases.put(populationTableStructure.getPopulationColumn().getColumnName(), populationTableStructure
               .getPopulationSelectEntity().getAlias());
      selectQueryAliases.put(populationTableStructure.getSliceNumberColumn().getColumnName(), populationTableStructure
               .getSliceNumberSelectEntity().getAlias());
      return selectQueryAliases;
   }

   /**
    * This method populates the table structure of population table.
    * 
    * @param martCreationInputInfo
    * @return martPopulationTableStructure
    * @throws AnswersCatalogException
    * @throws DataAccessException
    */
   private MartPopulationTableStructure populatePopulationTableStructure (MartCreationInputInfo martCreationInputInfo)
            throws AnswersCatalogException, DataAccessException {
      AnswersCatalogConfigurationContext answersCatalogConfigurationContext = martCreationInputInfo
               .getAnswersCatalogConfigurationContext();
      MartConfigurationContext martConfigurationContext = martCreationInputInfo.getMartConfigurationContext();
      MartCreationPopulatedContext martCreationPopulatedContext = martCreationInputInfo
               .getMartCreationPopulatedContext();
      MartInputParametersContext martCreationInputParametersContext = martCreationInputInfo
               .getMartCreationInputParametersContext();
      DataSource targetDataSource = martCreationPopulatedContext.getTargetAsset().getDataSource();
      DataSource sourceDataSource = martCreationPopulatedContext.getSourceAsset().getDataSource();
      int maxDbObjectLength = answersCatalogConfigurationContext.getMaxDBObjectLength();
      boolean isRandomNumberGeneratorDBApproach = martConfigurationContext.isRandomNumberGeneratorDBApproach();
      QueryColumn configurationIdColumn = martConfigurationContext.getPopulationTableIdColumn();

      // preparing the table name and check for uniqueness.
      String staticPopulationTableName = martConfigurationContext.getPopulationTableName();
      String assetName = martCreationPopulatedContext.getTargetAsset().getName();
      String finalPopulationTableName = staticPopulationTableName + UNDERSCORE + assetName;
      finalPopulationTableName = getAnswersCatalogDataAccessService().getUniqueNonExistentTableName(targetDataSource,
               finalPopulationTableName, new ArrayList<String>(), maxDbObjectLength);

      // preparing the columns and check for uniqueness.
      List<String> existingColumnNames = new ArrayList<String>();
      // adding the ID column
      QueryColumn idColumn = ExecueBeanCloneUtil.cloneQueryColumn(configurationIdColumn);
      AnswersCatalogUtil.maintainColumnUniqueness(idColumn, existingColumnNames, maxDbObjectLength);
      // adding the population query column
      QueryColumn populationQueryColumn = ExecueBeanCloneUtil.cloneQueryColumn(martCreationPopulatedContext
               .getSourceAssetMappingInfo().getPopulatedPopulation().getQueryColumn());
      populationQueryColumn.setDefaultValue(null);
      populationQueryColumn.setNullable(true);
      AnswersCatalogUtil.maintainColumnUniqueness(populationQueryColumn, existingColumnNames, maxDbObjectLength);
      // adding the slice number column.
      QueryColumn sliceNumberColumn = martConfigurationContext.getPopulationTableSliceNumberColumn();
      AnswersCatalogUtil.maintainColumnUniqueness(sliceNumberColumn, existingColumnNames, maxDbObjectLength);

      // preparing the select entities.
      List<String> existingAliases = new ArrayList<String>();
      // population column select entity
      QueryTableColumn populationQueryTableColumn = martCreationPopulatedContext.getSourceAssetMappingInfo()
               .getPopulatedPopulation().getQueryTableColumn();
      SelectEntity populationSelectEntity = new SelectEntity();
      populationSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      populationSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(populationQueryTableColumn));
      populationSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));

      // slice number select entity. It is static formula in DB approach which generates slice number
      SelectEntity sliceNumberSelectEntity = new SelectEntity();
      sliceNumberSelectEntity.setType(SelectEntityType.FORMULA);
      QueryFormula randomNumberQueryFormula = new QueryFormula();
      randomNumberQueryFormula.setQueryFormulaType(QueryFormulaType.STATIC);
      ISQLAdaptor sqlAdaptor = SQLAdaptorFactory.getInstance().getSQLAdaptor(
               sourceDataSource.getProviderType().getValue());
      String randomNumberGeneratorFormula = sqlAdaptor.createRandomNumberGeneratorFormula(1,
               martCreationInputParametersContext.getNumberOfSlices());
      randomNumberQueryFormula.setStaticFormula(randomNumberGeneratorFormula);
      sliceNumberSelectEntity.setFormula(randomNumberQueryFormula);
      sliceNumberSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));

      MartPopulationTableStructure martPopulationTableStructure = new MartPopulationTableStructure();
      martPopulationTableStructure.setTableName(finalPopulationTableName);
      martPopulationTableStructure.setIdColumn(idColumn);
      martPopulationTableStructure.setPopulationColumn(populationQueryColumn);
      martPopulationTableStructure.setSliceNumberColumn(sliceNumberColumn);
      martPopulationTableStructure.setPopulationSelectEntity(populationSelectEntity);
      martPopulationTableStructure.setSliceNumberSelectEntity(sliceNumberSelectEntity);
      martPopulationTableStructure.setDbApproachForSliceNumber(isRandomNumberGeneratorDBApproach);
      return martPopulationTableStructure;

   }

   public IRandomNumberGeneratorService getRandomNumberGeneratorService () {
      return randomNumberGeneratorService;
   }

   public void setRandomNumberGeneratorService (IRandomNumberGeneratorService randomNumberGeneratorService) {
      this.randomNumberGeneratorService = randomNumberGeneratorService;
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
