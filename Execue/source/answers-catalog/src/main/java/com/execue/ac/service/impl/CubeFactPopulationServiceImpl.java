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
import com.execue.ac.bean.ConceptColumnMapping;
import com.execue.ac.bean.CubeConfigurationContext;
import com.execue.ac.bean.CubeCreationInputInfo;
import com.execue.ac.bean.CubeCreationOutputInfo;
import com.execue.ac.bean.CubeCreationPopulatedContext;
import com.execue.ac.bean.CubeFactTableStructure;
import com.execue.ac.bean.CubePreFactTableStructure;
import com.execue.ac.bean.CubeSourceColumnMapping;
import com.execue.ac.bean.CubeUpdationDimensionInfo;
import com.execue.ac.bean.CubeUpdationInputInfo;
import com.execue.ac.bean.CubeUpdationOutputInfo;
import com.execue.ac.bean.CubeUpdationPopulatedContext;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.CubeFactPopulationException;
import com.execue.ac.helper.CubeCreationServiceHelper;
import com.execue.ac.service.IAnswersCatalogDataAccessService;
import com.execue.ac.service.IAnswersCatalogSQLQueryGenerationService;
import com.execue.ac.service.ICubeCreationConstants;
import com.execue.ac.service.ICubeFactPopulationService;
import com.execue.ac.service.ICubeOperationalConstants;
import com.execue.ac.util.AnswersCatalogUtil;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.FromEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.bean.querygen.SQLIndex;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.util.querygen.IQueryGenerationUtilService;
import com.execue.util.querygen.QueryGenerationUtilServiceFactory;

/**
 * This service is used for populating the fact table for cube.
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
public class CubeFactPopulationServiceImpl implements ICubeFactPopulationService, ICubeCreationConstants,
         ICubeOperationalConstants {

   private IAnswersCatalogSQLQueryGenerationService answersCatalogSQLQueryGenerationService;
   private IAnswersCatalogDataAccessService         answersCatalogDataAccessService;
   private CubeCreationServiceHelper                cubeCreationServiceHelper;
   private QueryGenerationUtilServiceFactory        queryGenerationUtilServiceFactory;
   private static final Logger                      logger = Logger.getLogger(CubeFactPopulationServiceImpl.class);

   @Override
   public CubeFactTableStructure createCubeFactTable (CubeCreationOutputInfo cubeCreationOutputInfo)
            throws CubeFactPopulationException {
      CubeFactTableStructure cubeFactTableStructure = null;
      CubeCreationInputInfo cubeCreationInputInfo = cubeCreationOutputInfo.getCubeCreationInputInfo();
      AnswersCatalogConfigurationContext answersCatalogConfigurationContext = cubeCreationInputInfo
               .getAnswersCatalogConfigurationContext();
      CubeConfigurationContext cubeConfigurationContext = cubeCreationInputInfo.getCubeConfigurationContext();
      CubeCreationPopulatedContext cubeCreationPopulatedContext = cubeCreationInputInfo
               .getCubeCreationPopulatedContext();
      CubePreFactTableStructure cubePreFactTableStructure = cubeCreationOutputInfo.getCubePreFactTableStructure();
      JobRequest jobRequest = cubeCreationPopulatedContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      Exception exception = null;
      try {
         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  GENERATE_FACT_TABLE_STRUCTURE);
         // creating fact table
         cubeFactTableStructure = createCubeFactTable(cubeCreationPopulatedContext, cubeConfigurationContext,
                  answersCatalogConfigurationContext, cubePreFactTableStructure);

         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  CREATING_INDEXES_ON_FACT_TABLE);
         // creating index on fact table
         createIndexesOnCubeFactTable(answersCatalogConfigurationContext, cubeCreationPopulatedContext,
                  cubeFactTableStructure);
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
      } catch (DataAccessException e) {
         exception = e;
         throw new CubeFactPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_FACT_TABLE_POPULATION_EXCEPTION_CODE, e);
      } catch (AnswersCatalogException e) {
         exception = e;
         throw new CubeFactPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_FACT_TABLE_POPULATION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new CubeFactPopulationException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return cubeFactTableStructure;
   }

   @Override
   public CubeFactTableStructure manageCubeFactTable (CubeUpdationOutputInfo cubeUpdationOutputInfo)
            throws CubeFactPopulationException {
      CubeFactTableStructure cubeTempFactTableStructure = null;
      CubeUpdationInputInfo cubeUpdationInputInfo = cubeUpdationOutputInfo.getCubeUpdationInputInfo();
      AnswersCatalogConfigurationContext answersCatalogConfigurationContext = cubeUpdationInputInfo
               .getAnswersCatalogConfigurationContext();
      CubeConfigurationContext cubeConfigurationContext = cubeUpdationInputInfo.getCubeConfigurationContext();
      CubeCreationPopulatedContext existingCubeCreationPopulatedContext = cubeUpdationInputInfo
               .getCubeUpdationPopulatedContext().getExistingCubeCreationPopulatedContext();
      CubePreFactTableStructure cubePreFactTableStructure = cubeUpdationOutputInfo.getCubeUpdatePreFactTableStructure();
      CubeUpdationPopulatedContext cubeUpdationPopulatedContext = cubeUpdationInputInfo
               .getCubeUpdationPopulatedContext();
      List<CubeUpdationDimensionInfo> cubeUpdationDimensionInfoList = cubeUpdationPopulatedContext
               .getCubeUpdationDimensionInfoList();
      JobRequest jobRequest = cubeUpdationPopulatedContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      Exception exception = null;
      try {
         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  GENERATE_FACT_TABLE_STRUCTURE_FOR_CUBE_UPDATE);
         // creating temp fact table
         cubeTempFactTableStructure = createCubeFactTable(existingCubeCreationPopulatedContext,
                  cubeConfigurationContext, answersCatalogConfigurationContext, cubePreFactTableStructure);
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  DELETEING_DELETED_MEMBERS_FROM_EXISTING_FACT_TABLE_PER_DIMENSION);

         // for each dimension, delete the members from the actual fact table at least 'all' has to be deleted
         for (CubeUpdationDimensionInfo cubeUpdationDimensionInfo : cubeUpdationDimensionInfoList) {
            // get the corresponding fact table mapping for current dimension
            ConceptColumnMapping existingCubeFactTableConceptColumnMapping = getMatchedFactTableConceptColumnMapping(
                     cubeUpdationPopulatedContext, cubeUpdationDimensionInfo);

            // delete the deleted members from the fact table
            deleteMembersFromExistingFactTable(existingCubeFactTableConceptColumnMapping, cubeUpdationPopulatedContext,
                     cubeUpdationDimensionInfo, cubeConfigurationContext);

         }
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         jobOperationalStatus = getCubeCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  TRANSFER_UPDATED_FACT_DATA_TO_EXISTING_FACT_TABLE);
         // transfer the data from this temp table to actual fact table.
         QueryTable existingCubeFactTable = cubeUpdationPopulatedContext.getExistingCubeFactTable();
         transferTempFactDataToExistingFactTable(existingCubeFactTable, cubeUpdationPopulatedContext,
                  cubeTempFactTableStructure);
         getCubeCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
      } catch (DataAccessException e) {
         exception = e;
         throw new CubeFactPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_FACT_TABLE_POPULATION_EXCEPTION_CODE, e);
      } catch (AnswersCatalogException e) {
         exception = e;
         throw new CubeFactPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_CUBE_FACT_TABLE_POPULATION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getCubeCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new CubeFactPopulationException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }

      return cubeTempFactTableStructure;
   }

   /**
    * This method transforms the structure of the temp table created in last step to generate fact table Structure : 1.
    * All frequency columns 2. All SL columns 3. All RL columns 4. SL_STAT column 5. All Measure Columns
    * 
    * @throws DataAccessException
    * @throws AnswersCatalogException
    */
   private CubeFactTableStructure createCubeFactTable (CubeCreationPopulatedContext cubeCreationPopulatedContext,
            CubeConfigurationContext cubeConfigurationContext,
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext,
            CubePreFactTableStructure cubePreFactTableStructure) throws DataAccessException, AnswersCatalogException {
      Asset targetAsset = cubeCreationPopulatedContext.getTargetAsset();
      DataSource targetDataSource = targetAsset.getDataSource();
      AssetProviderType targetProviderType = targetDataSource.getProviderType();
      List<StatType> applicableStats = cubeConfigurationContext.getApplicableStats();

      CubeFactTableStructure cubeFactTableStructure = populateCubeTableStructure(answersCatalogConfigurationContext,
               cubeConfigurationContext, cubeCreationPopulatedContext);
      List<QueryColumn> sequenceCreateInsertTableColumns = populateCreateInsertTableColumnsSequence(cubeFactTableStructure);
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(cubeFactTableStructure.getTableName(), null,
               targetDataSource.getOwner());
      String cubeCreateQuery = answersCatalogSQLQueryGenerationService.prepareCreateTableQuery(targetProviderType,
               queryTable, sequenceCreateInsertTableColumns);
      if (logger.isDebugEnabled()) {
         logger.debug("Cube Table Create Query " + cubeCreateQuery);
      }

      String cubeInsertQuery = answersCatalogSQLQueryGenerationService.prepareInsertQuery(targetProviderType,
               queryTable, sequenceCreateInsertTableColumns);
      if (logger.isDebugEnabled()) {
         logger.debug("Cube Table Insert Query " + cubeCreateQuery);
      }

      List<String> existingAliases = new ArrayList<String>();
      QueryTable preFactQueryTable = AnswersCatalogUtil.createQueryTable(cubePreFactTableStructure.getTableName(),
               targetDataSource.getOwner());
      // building the select entities for frequency measures, SL's, RL's , in nutshell all the columns on which stat
      // is not defined
      List<SelectEntity> staticSelectEntities = populateStaticSelectEntities(cubePreFactTableStructure,
               preFactQueryTable, existingAliases);

      // for each stat, we will build one select query, taking all stat less entities built upfront followed by
      // current SL_STAT column and all the measure columns on which current stat value is applied.
      int currentStatNumber = 0;
      for (StatType statType : applicableStats) {
         List<SelectEntity> statBasedMeasureSelectEntities = populateStatBasedMeasureSelectEntities(
                  cubePreFactTableStructure, cubeConfigurationContext, cubeFactTableStructure, preFactQueryTable,
                  existingAliases, statType, currentStatNumber);
         Query selectQuery = populateSelectQueryObject(preFactQueryTable, staticSelectEntities,
                  statBasedMeasureSelectEntities);
         String selectQueryString = answersCatalogSQLQueryGenerationService
                  .prepareSelectQuery(targetAsset, selectQuery);
         if (logger.isDebugEnabled()) {
            logger.debug("For Stat : " + statType.getValue() + " ,Cube Table Select Query " + cubeCreateQuery);
         }
         getCubeCreationServiceHelper().transferLocalData(targetDataSource, cubeFactTableStructure.getTableName(),
                  cubeCreateQuery, cubeInsertQuery, selectQueryString);
         currentStatNumber++;
      }

      return cubeFactTableStructure;
   }

   /**
    * This method creates indexes on fact table.
    * 
    * @param cubeCreationInputInfo
    * @param cubeFactTableStructure
    * @throws DataAccessException
    * @throws DataAccessException
    * @throws AnswersCatalogException
    */
   private void createIndexesOnCubeFactTable (AnswersCatalogConfigurationContext answersCatalogConfigurationContext,
            CubeCreationPopulatedContext cubeCreationPopulatedContext, CubeFactTableStructure cubeFactTableStructure)
            throws CubeFactPopulationException, DataAccessException {
      if (logger.isDebugEnabled()) {
         logger.debug("Creating Indexes on Fact table");
      }
      DataSource dataSource = cubeCreationPopulatedContext.getTargetAsset().getDataSource();
      int maxDBObjectLength = answersCatalogConfigurationContext.getMaxDBObjectLength();

      List<SQLIndex> indexes = new ArrayList<SQLIndex>();
      // for each lookup column including stat, we create a index
      for (QueryColumn queryColumn : cubeFactTableStructure.getCombinedLookupColumns()) {
         indexes.add(ExecueBeanManagementUtil.prepareSQLIndex(cubeFactTableStructure.getTableName(), queryColumn
                  .getColumnName()));
      }

      if (ExecueCoreUtil.isCollectionNotEmpty(indexes)) {
         getAnswersCatalogDataAccessService().createMultipleIndexesOnTable(dataSource, indexes, maxDBObjectLength);
      }
   }

   /**
    * This method populates the select query object.
    * 
    * @param preFactQueryTable
    * @param statLessSelectEntities
    * @param statBasedSelectEntities
    * @return query
    */
   private Query populateSelectQueryObject (QueryTable preFactQueryTable, List<SelectEntity> statLessSelectEntities,
            List<SelectEntity> statBasedSelectEntities) {
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      selectEntities.addAll(statLessSelectEntities);
      selectEntities.addAll(statBasedSelectEntities);
      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      fromEntities.add(ExecueBeanManagementUtil.createFromEntityObject(preFactQueryTable));
      Query query = new Query();
      query.setSelectEntities(selectEntities);
      query.setFromEntities(fromEntities);
      return query;
   }

   /**
    * This method populates the static select entities(frequency measures, simple lookups and range lookups) from pre
    * fact table
    * 
    * @param cubePreFactTableStructure
    * @param queryTable
    * @param existingAliases
    * @return statLessSelectEntities
    */
   private List<SelectEntity> populateStaticSelectEntities (CubePreFactTableStructure cubePreFactTableStructure,
            QueryTable queryTable, List<String> existingAliases) {
      // preparing the select entities for frequency measure columns.
      List<SelectEntity> staticSelectEntities = new ArrayList<SelectEntity>();
      for (QueryColumn queryColumn : cubePreFactTableStructure.getFrequencyMeasureColumns()) {
         SelectEntity frequencyMeasureSelectEntity = new SelectEntity();
         frequencyMeasureSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
         frequencyMeasureSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(queryColumn,
                  queryTable));
         frequencyMeasureSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
         staticSelectEntities.add(frequencyMeasureSelectEntity);
      }

      // preparing the select entities for simple lookup dimension columns.
      for (QueryColumn queryColumn : cubePreFactTableStructure.getSimpleLookupColumns()) {
         SelectEntity simpleLookupSelectEntity = new SelectEntity();
         simpleLookupSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
         simpleLookupSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(queryColumn,
                  queryTable));
         simpleLookupSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
         staticSelectEntities.add(simpleLookupSelectEntity);
      }
      // preparing the select entities for range lookup dimension columns.
      for (QueryColumn queryColumn : cubePreFactTableStructure.getRangeLookupColumns()) {
         SelectEntity rangeLookupSelectEntity = new SelectEntity();
         rangeLookupSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
         rangeLookupSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(queryColumn,
                  queryTable));
         rangeLookupSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
         staticSelectEntities.add(rangeLookupSelectEntity);
      }
      return staticSelectEntities;
   }

   /**
    * This method returns the stat based (measure columns)select entities from pre fact table on which current stat
    * passed has been applied
    * 
    * @param cubeCreationOutputInfo
    * @param cubeFactTableStructure
    * @param queryTable
    * @param existingAliases
    * @param currentStat
    * @param currentStatNumber
    * @return statBasedSelectEntities
    */
   private List<SelectEntity> populateStatBasedMeasureSelectEntities (
            CubePreFactTableStructure cubePreFactTableStructure, CubeConfigurationContext cubeConfigurationContext,
            CubeFactTableStructure cubeFactTableStructure, QueryTable queryTable, List<String> existingAliases,
            StatType currentStat, int currentStatNumber) {
      Integer totalStatsSize = cubeConfigurationContext.getApplicableStats().size();
      List<SelectEntity> statBasedSelectEntities = new ArrayList<SelectEntity>();

      SelectEntity statSelectEntity = new SelectEntity();
      statSelectEntity.setType(SelectEntityType.VALUE);
      QueryValue queryValue = new QueryValue();
      queryValue.setValue(currentStat.getValue());
      queryValue.setDataType(cubeFactTableStructure.getStatColumn().getDataType());
      statSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
      statSelectEntity.setQueryValue(queryValue);
      statBasedSelectEntities.add(statSelectEntity);

      // for all the stat measure columns, start from stat number and increment by total stats and pick the columns in
      // the sequence.
      List<QueryColumn> statBasedMeasureColumns = cubePreFactTableStructure.getStatMeasureColumns();
      for (int index = currentStatNumber; index < statBasedMeasureColumns.size(); index = index + totalStatsSize) {
         QueryColumn statBasedQueryColumn = statBasedMeasureColumns.get(index);
         SelectEntity measureSelectEntity = new SelectEntity();
         measureSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
         measureSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(statBasedQueryColumn,
                  queryTable));
         measureSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
         statBasedSelectEntities.add(measureSelectEntity);
      }

      return statBasedSelectEntities;
   }

   /**
    * This method defines the sequence of create insert query string
    * 
    * @param cubeFactTableStructure
    * @return sequenceCreateInsertTableColumns
    */
   private List<QueryColumn> populateCreateInsertTableColumnsSequence (CubeFactTableStructure cubeFactTableStructure) {
      List<QueryColumn> sequenceCreateInsertTableColumns = new ArrayList<QueryColumn>();
      for (CubeSourceColumnMapping cubeSourceColumnMapping : cubeFactTableStructure.getFrequencyMeasureColumns()) {
         sequenceCreateInsertTableColumns.add(cubeSourceColumnMapping.getQueryColumn());
      }
      for (CubeSourceColumnMapping cubeSourceColumnMapping : cubeFactTableStructure.getSimpleLookupColumns()) {
         sequenceCreateInsertTableColumns.add(cubeSourceColumnMapping.getQueryColumn());
      }
      for (CubeSourceColumnMapping cubeSourceColumnMapping : cubeFactTableStructure.getRangeLookupColumns()) {
         sequenceCreateInsertTableColumns.add(cubeSourceColumnMapping.getQueryColumn());
      }
      sequenceCreateInsertTableColumns.add(cubeFactTableStructure.getStatColumn());
      for (CubeSourceColumnMapping cubeSourceColumnMapping : cubeFactTableStructure.getMeasureColumns()) {
         sequenceCreateInsertTableColumns.add(cubeSourceColumnMapping.getQueryColumn());
      }
      return sequenceCreateInsertTableColumns;
   }

   /**
    * This method populates the cube table structure.
    * 
    * @param cubeCreationOutputInfo
    * @return CubeFactTableStructure
    * @throws DataAccessException
    */
   private CubeFactTableStructure populateCubeTableStructure (
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext,
            CubeConfigurationContext cubeConfigurationContext, CubeCreationPopulatedContext cubeCreationPopulatedContext)
            throws DataAccessException {
      Asset targetAsset = cubeCreationPopulatedContext.getTargetAsset();
      int maxDbObjectLength = answersCatalogConfigurationContext.getMaxDBObjectLength();
      DataType measureColumnDataType = answersCatalogConfigurationContext.getMeasureColumnDataType();
      DataType frequencyColumnDataType = answersCatalogConfigurationContext.getFrequencyColumnDataType();
      int measurePrecisionValue = answersCatalogConfigurationContext.getMeasurePrecisionValue();
      int measureScaleValue = answersCatalogConfigurationContext.getMeasureScaleValue();
      String cubeFactTablePrefix = cubeConfigurationContext.getCubeFactTablePrefix();
      String frequencyColumnPrefix = cubeConfigurationContext.getFrequencyColumnPrefix();
      String simpleLookupColumnPrefix = cubeConfigurationContext.getSimpleLookupColumnPrefix();
      String rangeLookupColumnPrefix = cubeConfigurationContext.getRangeLookupColumnPrefix();
      DataType dimensionDataType = cubeConfigurationContext.getDimensionDataType();
      Integer minDimensionPrecision = cubeConfigurationContext.getMinDimensionPrecision();
      QueryColumn configurationStatColumn = cubeConfigurationContext.getStatColumn();

      String assetName = targetAsset.getName();
      String finalCubeTableName = cubeFactTablePrefix + UNDERSCORE + assetName;
      finalCubeTableName = getAnswersCatalogDataAccessService().getUniqueNonExistentTableName(
               targetAsset.getDataSource(), finalCubeTableName, new ArrayList<String>(), maxDbObjectLength);

      List<String> existingColumnNames = new ArrayList<String>();

      // populating the stat column first for reason that we want column name to be static sl_stat
      QueryColumn statColumn = ExecueBeanCloneUtil.cloneQueryColumn(configurationStatColumn);
      AnswersCatalogUtil.maintainColumnUniqueness(statColumn, existingColumnNames, maxDbObjectLength);

      // preparing the frequency measure columns and check for uniqueness.
      List<CubeSourceColumnMapping> frequencyMeasureQueryColumns = new ArrayList<CubeSourceColumnMapping>();
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
         frequencyMeasureQueryColumns.add(AnswersCatalogUtil.prepareCubeSourceColumnMapping(
                  frequencyMeasureQueryColumn, frequencyMeasureMapping));
      }

      // preparing the SL dimension columns and check for uniqueness.
      List<CubeSourceColumnMapping> simpleLookupColumns = new ArrayList<CubeSourceColumnMapping>();
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
         simpleLookupColumns.add(AnswersCatalogUtil.prepareCubeSourceColumnMapping(simpleLookupQueryColumn,
                  simpleLookupDimensionMapping));
      }

      // preparing the RL dimension columns and check for uniqueness.
      List<CubeSourceColumnMapping> rangeLookupColumns = new ArrayList<CubeSourceColumnMapping>();
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
         rangeLookupColumns.add(AnswersCatalogUtil.prepareCubeSourceColumnMapping(rangeLookupQueryColumn,
                  rangeLookupDimensionMapping));
      }

      List<CubeSourceColumnMapping> measureColumns = new ArrayList<CubeSourceColumnMapping>();
      for (ConceptColumnMapping measureMapping : cubeCreationPopulatedContext.getPopulatedMeasures()) {
         QueryColumn measureQueryColumn = ExecueBeanCloneUtil.cloneQueryColumn(measureMapping.getQueryColumn());
         AnswersCatalogUtil.cleanQueryColumn(measureQueryColumn);
         measureQueryColumn.setColumnName(measureQueryColumn.getColumnName());
         measureQueryColumn.setDataType(measureColumnDataType);
         measureQueryColumn.setPrecision(measurePrecisionValue);
         measureQueryColumn.setScale(measureScaleValue);
         AnswersCatalogUtil.maintainColumnUniqueness(measureQueryColumn, existingColumnNames, maxDbObjectLength);
         measureColumns.add(AnswersCatalogUtil.prepareCubeSourceColumnMapping(measureQueryColumn, measureMapping));
      }

      CubeFactTableStructure cubeFactTableStructure = new CubeFactTableStructure();
      cubeFactTableStructure.setTableName(finalCubeTableName);
      cubeFactTableStructure.setFrequencyMeasureColumns(frequencyMeasureQueryColumns);
      cubeFactTableStructure.setSimpleLookupColumns(simpleLookupColumns);
      cubeFactTableStructure.setRangeLookupColumns(rangeLookupColumns);
      cubeFactTableStructure.setStatColumn(statColumn);
      cubeFactTableStructure.setMeasureColumns(measureColumns);
      return cubeFactTableStructure;
   }

   public CubeCreationServiceHelper getCubeCreationServiceHelper () {
      return cubeCreationServiceHelper;
   }

   public void setCubeCreationServiceHelper (CubeCreationServiceHelper cubeCreationServiceHelper) {
      this.cubeCreationServiceHelper = cubeCreationServiceHelper;
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

   private ConceptColumnMapping getMatchedFactTableConceptColumnMapping (
            CubeUpdationPopulatedContext cubeUpdationPopulatedContext,
            CubeUpdationDimensionInfo cubeUpdationDimensionInfo) {
      List<ConceptColumnMapping> updatedDimensionsExistingCubeFactTableMappings = cubeUpdationPopulatedContext
               .getUpdatedDimensionsExistingCubeFactTableMappings();
      String dimensionName = cubeUpdationDimensionInfo.getDimensionName();
      // get the matching concept column mapping for dimension
      ConceptColumnMapping matchedDimensionExistingCubeFactTableMapping = AnswersCatalogUtil
               .getMatchedDimensionMapping(dimensionName, updatedDimensionsExistingCubeFactTableMappings);
      return matchedDimensionExistingCubeFactTableMapping;
   }

   private void deleteMembersFromExistingFactTable (ConceptColumnMapping existingCubeFactTableConceptColumnMapping,
            CubeUpdationPopulatedContext cubeUpdationPopulatedContext,
            CubeUpdationDimensionInfo cubeUpdationDimensionInfo, CubeConfigurationContext cubeConfigurationContext)
            throws CubeFactPopulationException, DataAccessException {
      DataSource targetDataSource = cubeUpdationPopulatedContext.getTargetAsset().getDataSource();
      AssetProviderType targetProviderType = targetDataSource.getProviderType();
      String cubeAllValueRepresentation = cubeConfigurationContext.getCubeAllValueRepresentation();
      List<Membr> deletedMembers = cubeUpdationDimensionInfo.getDeletedMembers();

      List<String> deletedMemberValues = new ArrayList<String>();
      deletedMemberValues.add(cubeAllValueRepresentation);
      if (ExecueCoreUtil.isCollectionNotEmpty(deletedMembers)) {
         deletedMemberValues.addAll(AnswersCatalogUtil.getMemberValues(deletedMembers));
      }
      // add all value to it.
      ConditionEntity conditionEntity = AnswersCatalogUtil.prepareConditionEntity(
               existingCubeFactTableConceptColumnMapping.getQueryTable(), existingCubeFactTableConceptColumnMapping
                        .getQueryColumn(), deletedMemberValues);

      String deleteQuery = getQueryGenerationUtilService(targetProviderType)
               .createTableDeleteStatement(conditionEntity);
      if (logger.isDebugEnabled()) {
         logger.debug("Delete Query " + deleteQuery);
      }
      getAnswersCatalogDataAccessService().executeDDLStatement(targetDataSource.getName(), deleteQuery);
   }

   private void transferTempFactDataToExistingFactTable (QueryTable existingCubeFactTable,
            CubeUpdationPopulatedContext cubeUpdationPopulatedContext, CubeFactTableStructure tempCubeFactTableStructure)
            throws CubeFactPopulationException, DataAccessException {
      DataSource targetDataSource = cubeUpdationPopulatedContext.getTargetAsset().getDataSource();
      AssetProviderType targetProviderType = targetDataSource.getProviderType();

      // building insert statement for existing cube table
      String insertStatement = getQueryGenerationUtilService(targetProviderType).createInsertStatement(
               existingCubeFactTable);
      if (logger.isDebugEnabled()) {
         logger.debug("Insert Statement for " + insertStatement);
      }

      // building select statement from temp fact table
      QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(tempCubeFactTableStructure.getTableName(),
               null, targetDataSource.getOwner());
      String selectStatement = getQueryGenerationUtilService(targetProviderType).createSelectStatement(queryTable,
               false);
      if (logger.isDebugEnabled()) {
         logger.debug("Select Statement " + selectStatement);
      }
      String dataTransferQuery = insertStatement + SQL_SPACE_DELIMETER + selectStatement;
      getAnswersCatalogDataAccessService().executeDDLStatement(targetDataSource.getName(), dataTransferQuery);
   }

   public QueryGenerationUtilServiceFactory getQueryGenerationUtilServiceFactory () {
      return queryGenerationUtilServiceFactory;
   }

   public void setQueryGenerationUtilServiceFactory (QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory) {
      this.queryGenerationUtilServiceFactory = queryGenerationUtilServiceFactory;
   }

   private IQueryGenerationUtilService getQueryGenerationUtilService (AssetProviderType providerType) {
      return getQueryGenerationUtilServiceFactory().getQueryGenerationUtilService(providerType);
   }
}
