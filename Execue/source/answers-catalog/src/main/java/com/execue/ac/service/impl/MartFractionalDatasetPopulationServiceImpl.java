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
import com.execue.ac.bean.BasicSamplingAlgorithmInput;
import com.execue.ac.bean.BasicSamplingAlgorithmStaticInput;
import com.execue.ac.bean.ConceptColumnMapping;
import com.execue.ac.bean.MartConfigurationContext;
import com.execue.ac.bean.MartCreationInputInfo;
import com.execue.ac.bean.MartCreationOutputInfo;
import com.execue.ac.bean.MartCreationPopulatedContext;
import com.execue.ac.bean.MartFractionalDataSetTempTableStructure;
import com.execue.ac.bean.MeasureStatInfo;
import com.execue.ac.bean.SamplingAlgorithmInput;
import com.execue.ac.bean.SourceAssetMappingInfo;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.MartFractionalDatasetPopulationException;
import com.execue.ac.helper.MartCreationServiceHelper;
import com.execue.ac.service.IAnswersCatalogDataAccessService;
import com.execue.ac.service.IAnswersCatalogSQLQueryGenerationService;
import com.execue.ac.service.IBasicSampleSizeCalculatorService;
import com.execue.ac.service.IMartCreationConstants;
import com.execue.ac.service.IMartFractionalDatasetPopulationService;
import com.execue.ac.service.IMartOperationalConstants;
import com.execue.ac.service.ISampleSizeCalculatorService;
import com.execue.ac.util.AnswersCatalogUtil;
import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.FromEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.bean.querygen.SQLIndex;
import com.execue.core.common.bean.querygen.SQLTable;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.querygen.service.ICubeQueryEnhancerService;
import com.execue.util.querygen.IQueryGenerationUtilService;
import com.execue.util.querygen.QueryGenerationUtilServiceFactory;

/**
 * This service represents the step2 of mart creation. It means for each dimension create a fractional table along with
 * distributions from cube or ETL.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class MartFractionalDatasetPopulationServiceImpl implements IMartFractionalDatasetPopulationService,
         IMartCreationConstants, IMartOperationalConstants {

   private IAnswersCatalogSQLQueryGenerationService answersCatalogSQLQueryGenerationService;
   private IAnswersCatalogDataAccessService         answersCatalogDataAccessService;
   private QueryGenerationUtilServiceFactory        queryGenerationUtilServiceFactory;
   private MartCreationServiceHelper                martCreationServiceHelper;
   private IBasicSampleSizeCalculatorService        basicSampleSizeCalculatorService;
   private ISampleSizeCalculatorService             sampleSizeCalculatorService;
   private ICubeQueryEnhancerService                cubeQueryEnhancerService;
   private static final Logger                      logger = Logger
                                                                    .getLogger(MartFractionalDatasetPopulationServiceImpl.class);

   /**
    * This method creates the fractional dataset table per dimension passed to it, There are four scenarios here 1. Cube
    * has the dimension so we can populate the data from cube 2. Cube is in same location as target data source 3. cube
    * is in different location as target data source 4. Cube doesn't have dimension, we are using source to populate 5.
    * Source is in same place as target 6. Source is in different place as target In nutshell, if source data source
    * whether coming from cube or source is same as target data source, then we can use insert into select else ETL
    * 
    * @param martCreationOutputInfo
    * @param dimensionName
    * @return martFractionalDataSetTempTableStructure
    * @throws MartFractionalDatasetPopulationException
    */
   public MartFractionalDataSetTempTableStructure createFractionalDataset (
            MartCreationOutputInfo martCreationOutputInfo, String dimensionName)
            throws MartFractionalDatasetPopulationException {
      MartCreationInputInfo martCreationInputInfo = martCreationOutputInfo.getMartCreationInputInfo();
      MartCreationContext martCreationContext = martCreationInputInfo.getMartCreationContext();
      MartFractionalDataSetTempTableStructure fractionalDataSetTempTable = null;
      JobRequest jobRequest = martCreationContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      Exception exception = null;
      try {
         MartCreationPopulatedContext martCreationPopulatedContext = martCreationInputInfo
                  .getMartCreationPopulatedContext();

         Asset sourceAsset = martCreationContext.getSourceAsset();
         Asset targetAsset = martCreationContext.getTargetAsset();
         DataSource targetDataSource = targetAsset.getDataSource();
         DataSource sourceDataSource = sourceAsset.getDataSource();
         AssetProviderType targetProviderType = targetDataSource.getProviderType();

         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  GENERATE_FRACTIONAL_DATA_SET_TABLE_STRUCTURE);
         // get the dimension mapping from cube map.
         boolean useExistingCube = true;
         SourceAssetMappingInfo sourceAssetMapping = martCreationPopulatedContext.getProminentDimensionCubeMap().get(
                  dimensionName);
         // if not found then get from source information.
         if (sourceAssetMapping == null) {
            sourceAssetMapping = martCreationPopulatedContext.getSourceAssetMappingInfo();
            useExistingCube = false;
         }

         if (logger.isDebugEnabled()) {
            logger.debug("Using the Existing Cube " + useExistingCube);
         }
         boolean autoIncrementClauseSupported = getQueryGenerationUtilService(targetProviderType)
                  .isAutoIncrementClauseSupported();

         if (logger.isDebugEnabled()) {
            logger.debug("Auto Increment Clause Supported " + autoIncrementClauseSupported);
         }

         // get the corresponding column level mapping for the dimension
         ConceptColumnMapping matchedDimensionConceptColumnMapping = AnswersCatalogUtil.getMatchedDimensionMapping(
                  dimensionName, sourceAssetMapping.getPopulatedProminentDimensions());

         // populating the fractional dataset temp table structure.
         fractionalDataSetTempTable = populateFractionalDataSetTempTableStructure(martCreationOutputInfo,
                  sourceAssetMapping, matchedDimensionConceptColumnMapping, useExistingCube);

         // populate the create table columns sequence
         SQLTable sequenceCreateTableColumns = populateCreateTableColumnsSequence(fractionalDataSetTempTable,
                  autoIncrementClauseSupported);

         QueryTable fractionalDataSetTempQueryTable = ExecueBeanManagementUtil.prepareQueryTable(
                  fractionalDataSetTempTable.getTableName(), null, targetDataSource.getOwner());

         // prepare create table query string
         String fractionalDatasetCreateQuery = answersCatalogSQLQueryGenerationService.prepareCreateTableQuery(
                  targetProviderType, fractionalDataSetTempQueryTable, sequenceCreateTableColumns.getColumns());

         if (logger.isDebugEnabled()) {
            logger.debug("Create Table Query : " + fractionalDatasetCreateQuery);
         }

         // select query from cube will be in different form as from source. Override the source asset and source data
         // source which can be used later in the decision whether to to use insert into select or ETL
         Query selectQuery = null;
         if (useExistingCube) {
            sourceAsset = sourceAssetMapping.getSourceAsset();
            sourceDataSource = sourceAssetMapping.getSourceAsset().getDataSource();
            selectQuery = populateSelectQueryObjectForCube(fractionalDataSetTempTable, sourceAsset);
         } else {
            selectQuery = populateSelectQueryObject(fractionalDataSetTempTable);
         }

         // preparing select query string.
         String fractionalDatasetSelectQuery = answersCatalogSQLQueryGenerationService.prepareSelectQuery(sourceAsset,
                  selectQuery);

         if (logger.isDebugEnabled()) {
            logger.debug("Select Query : " + fractionalDatasetSelectQuery);
         }

         // populate the insert table columns sequence
         List<QueryColumn> sequenceInsertTableColumns = populateInsertTableColumnsSequence(fractionalDataSetTempTable);

         // if source and target location is same, use insert into select else ETL
         if (sourceDataSource.getName().equalsIgnoreCase(targetDataSource.getName())) {
            String fractionalDatasetInsertQuery = answersCatalogSQLQueryGenerationService.prepareInsertQuery(
                     targetProviderType, fractionalDataSetTempQueryTable, sequenceInsertTableColumns);
            if (logger.isDebugEnabled()) {
               logger.debug("Insert Query : " + fractionalDatasetInsertQuery);
            }
            getMartCreationServiceHelper().transferLocalData(targetDataSource, fractionalDatasetCreateQuery,
                     fractionalDatasetInsertQuery, fractionalDatasetSelectQuery);
         } else {
            Map<String, String> selectQueryAliases = populateSelectQueryAliases(fractionalDataSetTempTable);
            String fractionalDatasetETLInsertQuery = answersCatalogSQLQueryGenerationService.prepareETLInsertQuery(
                     targetProviderType, fractionalDataSetTempQueryTable, sequenceInsertTableColumns,
                     selectQueryAliases);
            if (logger.isDebugEnabled()) {
               logger.debug("Insert ETL Query : " + fractionalDatasetETLInsertQuery);
            }
            getMartCreationServiceHelper().transferRemoteData(sourceDataSource, targetDataSource,
                     fractionalDatasetETLInsertQuery, fractionalDatasetSelectQuery, fractionalDatasetCreateQuery,
                     fractionalDataSetTempTable.getTableName());
         }
         getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);

         // populate the ID column if provider is oracle.
         if (!autoIncrementClauseSupported) {
            if (logger.isDebugEnabled()) {
               logger.debug("Populating the ID Column Data");
            }
            if (AssetProviderType.Oracle.equals(targetProviderType)) {
               getMartCreationServiceHelper().populateIdColumnData(targetDataSource,
                        fractionalDataSetTempTable.getTableName(), fractionalDataSetTempTable.getIdColumn());
            }
            // handle other providers if auto increment clause not supported
         }

         // NOTE: Create needed indexes on Fractional Temp Table
         createIndexesOnFractionalDatasetTempTable(martCreationInputInfo.getAnswersCatalogConfigurationContext(),
                  martCreationContext, fractionalDataSetTempTable);

         // populate the slice count.
         if (logger.isDebugEnabled()) {
            logger.debug("Populating the Slice Count Column");
         }
         jobOperationalStatus = getMartCreationServiceHelper().createJobOperationalStatus(jobRequest,
                  SLICE_COUNT_COLUMN_POPULATION);
         populateSliceCount(targetAsset, fractionalDataSetTempTable, martCreationInputInfo);
         getMartCreationServiceHelper().updateJobOperationalStatus(jobOperationalStatus, jobRequest);
      } catch (AnswersCatalogException e) {
         exception = e;
         throw new MartFractionalDatasetPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_FRACTIONAL_DATASETS_POPULATION_EXCEPTION_CODE, e);
      } catch (Exception e) {
         exception = e;
         throw new MartFractionalDatasetPopulationException(
                  AnswersCatalogExceptionCodes.DEFAULT_DATA_MART_FRACTIONAL_DATASETS_POPULATION_EXCEPTION_CODE, e);
      } finally {
         if (jobOperationalStatus != null && exception != null) {
            try {
               getMartCreationServiceHelper().updateJobOperationalStatusForFailure(jobOperationalStatus, jobRequest,
                        exception.getMessage());
            } catch (AnswersCatalogException e) {
               throw new MartFractionalDatasetPopulationException(
                        AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
            }
         }
      }
      return fractionalDataSetTempTable;
   }

   private void createIndexesOnFractionalDatasetTempTable (
            AnswersCatalogConfigurationContext answersCatalogConfigurationContext,
            MartCreationContext martCreationContext, MartFractionalDataSetTempTableStructure fractionalDataSetTempTable)
            throws DataAccessException {
      DataSource targetDataSource = martCreationContext.getTargetAsset().getDataSource();

      List<SQLIndex> indexes = new ArrayList<SQLIndex>();

      // Index on Dimension column
      indexes.add(ExecueBeanManagementUtil.prepareSQLIndex(fractionalDataSetTempTable.getTableName(),
               fractionalDataSetTempTable.getDimensionColumn().getColumnName()));

      // Index on Slice Number column
      indexes.add(ExecueBeanManagementUtil.prepareSQLIndex(fractionalDataSetTempTable.getTableName(),
               fractionalDataSetTempTable.getSliceCountColumn().getColumnName()));

      // Index on all the distribution columns
      for (QueryColumn distributionColumn : fractionalDataSetTempTable.getDistributionColumns()) {
         indexes.add(ExecueBeanManagementUtil.prepareSQLIndex(fractionalDataSetTempTable.getTableName(),
                  distributionColumn.getColumnName()));
      }

      getAnswersCatalogDataAccessService().createMultipleIndexesOnTable(targetDataSource, indexes,
               answersCatalogConfigurationContext.getMaxDBObjectLength());
   }

   /**
    * This method populates the slice count column of fractional dataset table. For each row represented by ID column,
    * we read population and measures stddev and avg and use sample size calculator to get the slice count. This value
    * we will update in the column.
    * 
    * @param targetAsset
    * @param fractionalDataSetTempTable
    * @param samplingAlgorithmStaticInput
    * @throws Exception
    */
   private void populateSliceCount (Asset targetAsset,
            MartFractionalDataSetTempTableStructure fractionalDataSetTempTable,
            MartCreationInputInfo martCreationInputInfo) throws Exception {
      DataSource targetDataSource = targetAsset.getDataSource();
      String tableName = fractionalDataSetTempTable.getTableName();
      QueryTable queryTable = AnswersCatalogUtil.createQueryTable(tableName, targetDataSource.getOwner());

      Long minIdColumnValue = answersCatalogDataAccessService.getStatBasedColumnValue(targetDataSource, queryTable,
               fractionalDataSetTempTable.getIdColumn(), StatType.MINIMUM);
      Long maxIdColumnValue = answersCatalogDataAccessService.getStatBasedColumnValue(targetDataSource, queryTable,
               fractionalDataSetTempTable.getIdColumn(), StatType.MAXIMUM);
      if (logger.isDebugEnabled()) {
         logger.debug("Min ID Value : " + minIdColumnValue);
      }
      if (logger.isDebugEnabled()) {
         logger.debug("Max ID Value : " + maxIdColumnValue);
      }
      if (minIdColumnValue != null && maxIdColumnValue != null) {
         // for each id value,
         for (Long idColumnValue = minIdColumnValue; idColumnValue <= maxIdColumnValue; idColumnValue++) {
            if (logger.isDebugEnabled()) {
               logger.debug("Processing Id Value " + idColumnValue);
            }
            // prepare id condition entity
            ConditionEntity idConditionEntity = AnswersCatalogUtil.prepareConditionEntity(queryTable,
                     fractionalDataSetTempTable.getIdColumn(), idColumnValue.toString());

            Long sliceCount = 0l;
            if (martCreationInputInfo.isUseBasicSamplingAlgorithm()) {
               sliceCount = calculateSliceCountUsingBasicSamplingAlgorithm(queryTable, fractionalDataSetTempTable,
                        idConditionEntity, idColumnValue, martCreationInputInfo.getBasicSamplingAlgorithmStaticInput(),
                        targetAsset);
            } else {
               sliceCount = calculateSliceCountUsingSamplingAlgorithm(queryTable, fractionalDataSetTempTable,
                        idConditionEntity, idColumnValue, martCreationInputInfo, targetAsset);
            }

            // prepare slice count condition entity
            List<ConditionEntity> setConditions = new ArrayList<ConditionEntity>();
            setConditions.add(AnswersCatalogUtil.prepareConditionEntity(queryTable, fractionalDataSetTempTable
                     .getSliceCountColumn(), sliceCount.toString()));
            List<ConditionEntity> whereConditions = new ArrayList<ConditionEntity>();
            whereConditions.add(idConditionEntity);
            // update the slice count based on id value.
            answersCatalogDataAccessService.executeUpdateStatement(targetDataSource, queryTable, setConditions,
                     whereConditions);
         }
      }
   }

   private Long calculateSliceCountUsingBasicSamplingAlgorithm (QueryTable fractionalDataSetTempQueryTable,
            MartFractionalDataSetTempTableStructure fractionalDataSetTempTable, ConditionEntity idConditionEntity,
            Long idColumnValue, BasicSamplingAlgorithmStaticInput basicSamplingAlgorithmStaticInput, Asset targetAsset)
            throws Exception {
      DataSource targetDataSource = targetAsset.getDataSource();
      int measureColumnsSize = fractionalDataSetTempTable.getStatMeasureColumns().size();

      // prepare query for population and measures
      Query queryForPopulationAndMeasures = populateQueryForPopulationAndMeasures(fractionalDataSetTempTable,
               fractionalDataSetTempQueryTable);
      List<ConditionEntity> conditionEntities = new ArrayList<ConditionEntity>();
      conditionEntities.add(idConditionEntity);
      queryForPopulationAndMeasures.setWhereEntities(conditionEntities);
      // prepare select query string for each id
      String selectQuery = answersCatalogSQLQueryGenerationService.prepareSelectQuery(targetAsset,
               queryForPopulationAndMeasures);
      if (logger.isDebugEnabled()) {
         logger.debug("Select Query for Id " + idColumnValue + " : " + selectQuery);
      }
      ExeCueResultSet exeCueResultSet = answersCatalogDataAccessService.executeSQLQuery(targetDataSource,
               queryForPopulationAndMeasures, selectQuery);

      // prepare basic sampling algorithm input
      BasicSamplingAlgorithmInput samplingAlgorithmInput = prepareBasicSamplingAlgoInput(exeCueResultSet,
               measureColumnsSize);
      // calculate the slice count
      Long sliceCount = getBasicSampleSizeCalculatorService().populateSliceCount(samplingAlgorithmInput,
               basicSamplingAlgorithmStaticInput);
      return sliceCount;
   }

   private Long calculateSliceCountUsingSamplingAlgorithm (QueryTable fractionalDataSetTempQueryTable,
            MartFractionalDataSetTempTableStructure fractionalDataSetTempTable, ConditionEntity idConditionEntity,
            Long idColumnValue, MartCreationInputInfo martCreationInputInfo, Asset targetAsset) throws Exception {
      DataSource targetDataSource = targetAsset.getDataSource();

      // prepare query for population and measures
      Query queryForIslandInformation = populateQueryForIslandInformation(fractionalDataSetTempTable,
               fractionalDataSetTempQueryTable);
      List<ConditionEntity> conditionEntities = new ArrayList<ConditionEntity>();
      conditionEntities.add(idConditionEntity);
      queryForIslandInformation.setWhereEntities(conditionEntities);
      // prepare select query string for each id
      String selectQuery = answersCatalogSQLQueryGenerationService.prepareSelectQuery(targetAsset,
               queryForIslandInformation);
      if (logger.isDebugEnabled()) {
         logger.debug("Select Query for Id " + idColumnValue + " : " + selectQuery);
      }
      ExeCueResultSet exeCueResultSet = answersCatalogDataAccessService.executeSQLQuery(targetDataSource,
               queryForIslandInformation, selectQuery);

      SamplingAlgorithmInput samplingAlgorithmInput = prepareSamplingAlgoInput(exeCueResultSet,
               fractionalDataSetTempTable, martCreationInputInfo);
      Double sampleSizePercentage = getSampleSizeCalculatorService().calculateSampleSizePercentage(
               samplingAlgorithmInput, martCreationInputInfo.getSamplingAlgorithmStaticInput());
      Integer numberOfSlices = martCreationInputInfo.getMartCreationInputParametersContext().getNumberOfSlices();
      Double percentageNumberOfSlices = sampleSizePercentage / 100 * numberOfSlices;
      percentageNumberOfSlices = Math.ceil(percentageNumberOfSlices);
      Long sliceCount = percentageNumberOfSlices.longValue();
      return sliceCount;
   }

   /**
    * This method prepares the sampling algorithm input bean by parsing resultset for population and measures
    * 
    * @param populationMeasureResultSet
    * @param measureColumnsSize
    * @return samplingAlgorithmInput
    * @throws Exception
    */
   private BasicSamplingAlgorithmInput prepareBasicSamplingAlgoInput (ExeCueResultSet populationMeasureResultSet,
            int measureColumnsSize) throws Exception {
      BasicSamplingAlgorithmInput samplingAlgorithmInput = null;
      // move to record
      populationMeasureResultSet.next();
      samplingAlgorithmInput = new BasicSamplingAlgorithmInput();
      Long populationCount = populationMeasureResultSet.getLong(0);
      if (populationCount == null) {
         populationCount = 0L;
      }
      samplingAlgorithmInput.setPopulation(populationCount);
      List<MeasureStatInfo> measureStatInfoList = new ArrayList<MeasureStatInfo>();
      for (int index = 1; index <= measureColumnsSize;) {
         Double stddevValue = populationMeasureResultSet.getDouble(index++);
         if (stddevValue == null) {
            stddevValue = 0D;
         }
         Double meanValue = populationMeasureResultSet.getDouble(index++);
         if (meanValue == null) {
            meanValue = 0D;
         }
         Double minValue = populationMeasureResultSet.getDouble(index++);
         if (minValue == null) {
            minValue = 0D;
         }
         Double maxValue = populationMeasureResultSet.getDouble(index++);
         if (maxValue == null) {
            maxValue = 0D;
         }
         measureStatInfoList.add(new MeasureStatInfo(stddevValue, meanValue, minValue, maxValue));
         samplingAlgorithmInput.setMeasureStatInfo(measureStatInfoList);
      }
      return samplingAlgorithmInput;
   }

   /**
    * This method prepares the sampling algorithm input bean by parsing resultset for population and measures
    * 
    * @param martCreationInputInfo
    * @param populationMeasureResultSet
    * @param measureColumnsSize
    * @param prominentMeasureConceptColumnMapping
    * @return samplingAlgorithmInput
    * @throws Exception
    */
   private SamplingAlgorithmInput prepareSamplingAlgoInput (ExeCueResultSet resultSet,
            MartFractionalDataSetTempTableStructure fractionalDataSetTempTable,
            MartCreationInputInfo martCreationInputInfo) throws Exception {
      SamplingAlgorithmInput samplingAlgorithmInput = null;
      // move to record
      resultSet.next();
      samplingAlgorithmInput = new SamplingAlgorithmInput();
      int populationColumnIndex = 0;
      // get population information
      Long populationCount = resultSet.getLong(populationColumnIndex++);
      if (populationCount == null) {
         populationCount = 0L;
      }
      samplingAlgorithmInput.setPopulation(populationCount);
      // get distributions
      int distributionColumnsSize = fractionalDataSetTempTable.getDistributionColumns().size();
      List<String> distributionValues = new ArrayList<String>();
      int distributionColumnIndex = populationColumnIndex;
      for (; distributionColumnIndex < distributionColumnsSize + populationColumnIndex; distributionColumnIndex++) {
         distributionValues.add(resultSet.getString(distributionColumnIndex));
      }
      samplingAlgorithmInput.setDistributionValues(distributionValues);
      // get dimension
      int dimensionColumnIndex = distributionColumnIndex;
      String dimensionValue = resultSet.getString(dimensionColumnIndex++);
      samplingAlgorithmInput.setDimensionValue(dimensionValue);

      // get measure information
      List<MeasureStatInfo> measureStatInfoList = new ArrayList<MeasureStatInfo>();
      int measureColumnsSize = fractionalDataSetTempTable.getStatMeasureColumns().size();
      int measureColumnIndex = dimensionColumnIndex;
      for (; measureColumnIndex < measureColumnsSize + dimensionColumnIndex;) {
         Double stddevValue = resultSet.getDouble(measureColumnIndex++);
         if (stddevValue == null) {
            stddevValue = 0D;
         }
         Double meanValue = resultSet.getDouble(measureColumnIndex++);
         if (meanValue == null) {
            meanValue = 0D;
         }
         Double minValue = resultSet.getDouble(measureColumnIndex++);
         if (minValue == null) {
            minValue = 0D;
         }
         Double maxValue = resultSet.getDouble(measureColumnIndex++);
         if (maxValue == null) {
            maxValue = 0D;
         }
         measureStatInfoList.add(new MeasureStatInfo(stddevValue, meanValue, minValue, maxValue));
         samplingAlgorithmInput.setMeasureStatInfo(measureStatInfoList);
      }
      samplingAlgorithmInput.setFractionalDataSetTempTable(fractionalDataSetTempTable);
      samplingAlgorithmInput.setMartCreationInputInfo(martCreationInputInfo);
      return samplingAlgorithmInput;
   }

   /**
    * This method prepares select query from fractional temp dataset table for population and all the measures with
    * stats. We added From section also as these tables are not in swi so sql generation will not be able to do that
    * 
    * @param fractionalDataSetTempTable
    * @param queryTable
    * @return query
    */
   private Query populateQueryForPopulationAndMeasures (
            MartFractionalDataSetTempTableStructure fractionalDataSetTempTable, QueryTable queryTable) {
      List<SelectEntity> sequenceSelectEntities = new ArrayList<SelectEntity>();
      List<String> existingAliases = new ArrayList<String>();
      SelectEntity populationSelectEntity = new SelectEntity();
      populationSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      populationSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(fractionalDataSetTempTable
               .getPopulationColumn(), queryTable));
      populationSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
      sequenceSelectEntities.add(populationSelectEntity);

      for (QueryColumn statMeasureColumn : fractionalDataSetTempTable.getStatMeasureColumns()) {
         SelectEntity statMeasureSelectEntity = new SelectEntity();
         statMeasureSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
         statMeasureSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(statMeasureColumn,
                  queryTable));
         statMeasureSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
         sequenceSelectEntities.add(statMeasureSelectEntity);
      }

      List<FromEntity> fromEntities = new ArrayList<FromEntity>();
      fromEntities.add(ExecueBeanManagementUtil.createFromEntityObject(queryTable));
      Query query = new Query();
      query.setSelectEntities(sequenceSelectEntities);
      query.setFromEntities(fromEntities);
      return query;
   }

   /**
    * This method prepares select query from fractional temp dataset table for population along with distributions and
    * dimension in picture and for all the measures with stats . We added From section also as these tables are not in
    * swi so sql generation will not be able to do that
    * 
    * @param fractionalDataSetTempTable
    * @param queryTable
    * @return query
    */
   private Query populateQueryForIslandInformation (MartFractionalDataSetTempTableStructure fractionalDataSetTempTable,
            QueryTable queryTable) {
      List<SelectEntity> sequenceSelectEntities = new ArrayList<SelectEntity>();
      List<String> existingAliases = new ArrayList<String>();
      SelectEntity populationSelectEntity = new SelectEntity();
      populationSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      populationSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(fractionalDataSetTempTable
               .getPopulationColumn(), queryTable));
      populationSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
      sequenceSelectEntities.add(populationSelectEntity);

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

      for (QueryColumn statMeasureColumn : fractionalDataSetTempTable.getStatMeasureColumns()) {
         SelectEntity statMeasureSelectEntity = new SelectEntity();
         statMeasureSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
         statMeasureSelectEntity.setTableColumn(ExecueBeanManagementUtil.prepareQueryTableColumn(statMeasureColumn,
                  queryTable));
         statMeasureSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
         sequenceSelectEntities.add(statMeasureSelectEntity);
      }

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
    * @param fractionalDataSetTempTable
    * @return sqlTable
    */
   private List<QueryColumn> populateInsertTableColumnsSequence (
            MartFractionalDataSetTempTableStructure fractionalDataSetTempTable) {
      List<QueryColumn> sequenceInsertTableColumns = new ArrayList<QueryColumn>();
      sequenceInsertTableColumns.add(fractionalDataSetTempTable.getPopulationColumn());
      sequenceInsertTableColumns.addAll(fractionalDataSetTempTable.getDistributionColumns());
      sequenceInsertTableColumns.add(fractionalDataSetTempTable.getDimensionColumn());
      sequenceInsertTableColumns.addAll(fractionalDataSetTempTable.getStatMeasureColumns());
      return sequenceInsertTableColumns;
   }

   /**
    * This method prepares the create table columns sequence
    * 
    * @param fractionalDataSetTempTable
    * @param autoIncrementClauseSupported
    * @return sqlTable
    */
   private SQLTable populateCreateTableColumnsSequence (
            MartFractionalDataSetTempTableStructure fractionalDataSetTempTable, boolean autoIncrementClauseSupported) {
      QueryColumn idColumn = fractionalDataSetTempTable.getIdColumn();
      // if provider type have support for auto increment, we will use auto
      // increment to generate the id column.Else we will generate Id column later
      if (autoIncrementClauseSupported) {
         idColumn = ExecueBeanCloneUtil.cloneQueryColumn(idColumn);
         idColumn.setAutoIncrement(true);
      }
      List<QueryColumn> sequenceCreateTableColumns = new ArrayList<QueryColumn>();
      sequenceCreateTableColumns.add(idColumn);
      sequenceCreateTableColumns.add(fractionalDataSetTempTable.getPopulationColumn());
      sequenceCreateTableColumns.addAll(fractionalDataSetTempTable.getDistributionColumns());
      sequenceCreateTableColumns.add(fractionalDataSetTempTable.getDimensionColumn());
      sequenceCreateTableColumns.addAll(fractionalDataSetTempTable.getStatMeasureColumns());
      sequenceCreateTableColumns.add(fractionalDataSetTempTable.getSliceCountColumn());
      return ExecueBeanManagementUtil.prepareSQLTableFromQueryColumns(fractionalDataSetTempTable.getTableName(),
               sequenceCreateTableColumns);
   }

   /**
    * This method prepares select query for cube asset
    * 
    * @param fractionalDataSetTempTable
    * @param sourceAsset
    * @return query
    * @throws CloneNotSupportedException
    */
   private Query populateSelectQueryObjectForCube (MartFractionalDataSetTempTableStructure fractionalDataSetTempTable,
            Asset sourceAsset) throws CloneNotSupportedException {
      List<SelectEntity> sequenceSelectEntities = new ArrayList<SelectEntity>();
      sequenceSelectEntities.add(ExecueBeanCloneUtil.cloneSelectEntity(fractionalDataSetTempTable
               .getPopulationSelectEntity()));
      sequenceSelectEntities.addAll(ExecueBeanCloneUtil.cloneSelectEntities(fractionalDataSetTempTable
               .getDistributionSelectEntities()));
      sequenceSelectEntities.add(ExecueBeanCloneUtil.cloneSelectEntity(fractionalDataSetTempTable
               .getDimensionSelectEntity()));
      sequenceSelectEntities.addAll(ExecueBeanCloneUtil.cloneSelectEntities(fractionalDataSetTempTable
               .getStatMeasureSelectEntities()));
      Query query = new Query();
      query.setSelectEntities(sequenceSelectEntities);
      cubeQueryEnhancerService.modifyExecueOwnedCubeQuery(sourceAsset, query);
      return query;
   }

   /**
    * This method prepares the select query for source asset
    * 
    * @param fractionalDataSetTempTable
    * @return query
    * @throws CloneNotSupportedException
    */
   private Query populateSelectQueryObject (MartFractionalDataSetTempTableStructure fractionalDataSetTempTable)
            throws CloneNotSupportedException {
      List<SelectEntity> sequenceSelectEntities = new ArrayList<SelectEntity>();
      sequenceSelectEntities.add(ExecueBeanCloneUtil.cloneSelectEntity(fractionalDataSetTempTable
               .getPopulationSelectEntity()));
      sequenceSelectEntities.addAll(ExecueBeanCloneUtil.cloneSelectEntities(fractionalDataSetTempTable
               .getDistributionSelectEntities()));
      sequenceSelectEntities.add(ExecueBeanCloneUtil.cloneSelectEntity(fractionalDataSetTempTable
               .getDimensionSelectEntity()));
      sequenceSelectEntities.addAll(ExecueBeanCloneUtil.cloneSelectEntities(fractionalDataSetTempTable
               .getStatMeasureSelectEntities()));
      List<SelectEntity> sequenceGroupEntities = new ArrayList<SelectEntity>();
      sequenceGroupEntities.addAll(ExecueBeanCloneUtil.cloneSelectEntities(fractionalDataSetTempTable
               .getDistributionSelectEntities()));
      sequenceGroupEntities.add(ExecueBeanCloneUtil.cloneSelectEntity(fractionalDataSetTempTable
               .getDimensionSelectEntity()));
      Query query = new Query();
      query.setSelectEntities(sequenceSelectEntities);
      query.setGroupingEntities(sequenceGroupEntities);
      return query;
   }

   /**
    * This method prepares the map between insert and select as will be used in ETL process.
    * 
    * @param fractionalDataSetTempTableStructure
    * @return selectQueryAliases
    */
   private Map<String, String> populateSelectQueryAliases (
            MartFractionalDataSetTempTableStructure fractionalDataSetTempTableStructure) {
      Map<String, String> selectQueryAliases = new HashMap<String, String>();
      selectQueryAliases.put(fractionalDataSetTempTableStructure.getPopulationColumn().getColumnName(),
               fractionalDataSetTempTableStructure.getPopulationSelectEntity().getAlias());
      int index = 0;
      for (QueryColumn queryColumn : fractionalDataSetTempTableStructure.getDistributionColumns()) {
         selectQueryAliases.put(queryColumn.getColumnName(), fractionalDataSetTempTableStructure
                  .getDistributionSelectEntities().get(index++).getAlias());
      }
      selectQueryAliases.put(fractionalDataSetTempTableStructure.getDimensionColumn().getColumnName(),
               fractionalDataSetTempTableStructure.getDimensionSelectEntity().getAlias());
      index = 0;
      for (QueryColumn queryColumn : fractionalDataSetTempTableStructure.getStatMeasureColumns()) {
         selectQueryAliases.put(queryColumn.getColumnName(), fractionalDataSetTempTableStructure
                  .getStatMeasureSelectEntities().get(index++).getAlias());
      }
      return selectQueryAliases;
   }

   /**
    * This method populates the fractional dataset temp table structure.
    * 
    * @param martCreationOutputInfo
    * @param sourceAssetMapping
    * @param dimensionConceptColumnMapping
    * @param useExistingCube
    * @return fractionalDataSetTempTableStructure
    * @throws AnswersCatalogException
    * @throws DataAccessException
    */
   private MartFractionalDataSetTempTableStructure populateFractionalDataSetTempTableStructure (
            MartCreationOutputInfo martCreationOutputInfo, SourceAssetMappingInfo sourceAssetMapping,
            ConceptColumnMapping dimensionConceptColumnMapping, boolean useExistingCube)
            throws AnswersCatalogException, DataAccessException {
      MartCreationInputInfo martCreationInputInfo = martCreationOutputInfo.getMartCreationInputInfo();
      AnswersCatalogConfigurationContext answersCatalogConfigurationContext = martCreationInputInfo
               .getAnswersCatalogConfigurationContext();
      MartConfigurationContext martConfigurationContext = martCreationInputInfo.getMartConfigurationContext();
      MartCreationContext martCreationContext = martCreationInputInfo.getMartCreationContext();
      DataSource dataSource = martCreationContext.getTargetAsset().getDataSource();
      int maxDbObjectLength = answersCatalogConfigurationContext.getMaxDBObjectLength();
      DataType measureColumnDataType = answersCatalogConfigurationContext.getMeasureColumnDataType();
      DataType frequencyColumnDataType = answersCatalogConfigurationContext.getFrequencyColumnDataType();
      int measurePrecisionValue = answersCatalogConfigurationContext.getMeasurePrecisionValue();
      int measureScaleValue = answersCatalogConfigurationContext.getMeasureScaleValue();
      List<StatType> stats = martConfigurationContext.getApplicableStats();
      QueryColumn configurationIdColumn = martConfigurationContext.getFractionalTempIdColumn();
      QueryColumn configurationSliceCountColumn = martConfigurationContext.getFractionalTempSliceCountColumn();

      // preparing the tablename and check for uniqueness.
      String dimensionName = dimensionConceptColumnMapping.getConcept().getName();
      String staticFractionalTempTableNotation = martConfigurationContext.getFractionalTempTableNotation();
      String assetName = martCreationContext.getTargetAsset().getName();
      String finalFractionalTempTable = dimensionName + staticFractionalTempTableNotation + UNDERSCORE + assetName;
      finalFractionalTempTable = getAnswersCatalogDataAccessService().getUniqueNonExistentTableName(dataSource,
               finalFractionalTempTable, new ArrayList<String>(), maxDbObjectLength);

      // preparing the columns and check for uniqueness.
      List<String> existingColumnNames = new ArrayList<String>();

      // preparing the ID column.
      QueryColumn idColumn = ExecueBeanCloneUtil.cloneQueryColumn(configurationIdColumn);
      AnswersCatalogUtil.maintainColumnUniqueness(idColumn, existingColumnNames, maxDbObjectLength);

      // prepare count(population) column.
      QueryColumn populationQueryColumn = ExecueBeanCloneUtil.cloneQueryColumn(sourceAssetMapping
               .getPopulatedPopulation().getQueryColumn());
      populationQueryColumn.setColumnName(StatType.COUNT.getValue() + UNDERSCORE
               + populationQueryColumn.getColumnName());
      AnswersCatalogUtil.cleanQueryColumn(populationQueryColumn);
      populationQueryColumn.setDataType(frequencyColumnDataType);
      populationQueryColumn.setPrecision(measurePrecisionValue);
      AnswersCatalogUtil.maintainColumnUniqueness(populationQueryColumn, existingColumnNames, maxDbObjectLength);

      // preparing distribution columns.
      List<QueryColumn> distributionColumns = new ArrayList<QueryColumn>();
      for (ConceptColumnMapping distributionMapping : sourceAssetMapping.getPopulatedDistributions()) {
         QueryColumn distributionQueryColumn = ExecueBeanCloneUtil.cloneQueryColumn(distributionMapping
                  .getQueryColumn());
         AnswersCatalogUtil.cleanQueryColumn(distributionQueryColumn);
         AnswersCatalogUtil.maintainColumnUniqueness(distributionQueryColumn, existingColumnNames, maxDbObjectLength);
         distributionColumns.add(distributionQueryColumn);
      }

      // prepare dimension column
      QueryColumn dimensionQueryColumn = ExecueBeanCloneUtil.cloneQueryColumn(dimensionConceptColumnMapping
               .getQueryColumn());
      AnswersCatalogUtil.cleanQueryColumn(dimensionQueryColumn);
      AnswersCatalogUtil.maintainColumnUniqueness(dimensionQueryColumn, existingColumnNames, maxDbObjectLength);

      // prepare stat + measure columns.
      List<QueryColumn> statMeasureColumns = new ArrayList<QueryColumn>();
      for (ConceptColumnMapping measureMapping : sourceAssetMapping.getPopulatedProminentMeasures()) {
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

      // prepare slice count column.
      QueryColumn sliceCountColumn = ExecueBeanCloneUtil.cloneQueryColumn(configurationSliceCountColumn);
      AnswersCatalogUtil.maintainColumnUniqueness(sliceCountColumn, existingColumnNames, maxDbObjectLength);

      // preparing the select entities.
      List<String> existingAliases = new ArrayList<String>();

      // preparing the population select entity.
      SelectEntity populationSelectEntity = new SelectEntity();
      populationSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      populationSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(sourceAssetMapping
               .getPopulatedPopulation().getQueryTableColumn()));
      populationSelectEntity.setFunctionName(StatType.COUNT);
      populationSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));

      /*populationSelectEntity.setRoundFunctionTargetColumn(AnswersCatalogUtil.getRoundFunctionTargetColumn(
               frequencyColumnDataType, measurePrecisionValue, 0));*/
      // The above call should not be needed, as long as the function is count -RG-
      // preparing the distribution select entities.
      List<SelectEntity> distributionSelectEntities = new ArrayList<SelectEntity>();
      for (ConceptColumnMapping columnMapping : sourceAssetMapping.getPopulatedDistributions()) {
         SelectEntity distributionSelectEntity = new SelectEntity();
         distributionSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
         distributionSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(columnMapping
                  .getQueryTableColumn()));
         distributionSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));
         distributionSelectEntities.add(distributionSelectEntity);
      }

      // preparing the dimension select entity.
      SelectEntity dimensionSelectEntity = new SelectEntity();
      dimensionSelectEntity.setType(SelectEntityType.TABLE_COLUMN);
      dimensionSelectEntity.setTableColumn(ExecueBeanCloneUtil.cloneQueryTableColumn(dimensionConceptColumnMapping
               .getQueryTableColumn()));
      dimensionSelectEntity.setAlias(ExecueCoreUtil.getAlias(existingAliases));

      // preparing the stat + measure select entities.
      List<SelectEntity> statMeasureSelectEntities = new ArrayList<SelectEntity>();
      for (ConceptColumnMapping measureMapping : sourceAssetMapping.getPopulatedProminentMeasures()) {
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

      MartFractionalDataSetTempTableStructure fractionalDataSetTempTableStructure = new MartFractionalDataSetTempTableStructure();
      fractionalDataSetTempTableStructure.setTableName(finalFractionalTempTable);
      fractionalDataSetTempTableStructure.setIdColumn(idColumn);
      fractionalDataSetTempTableStructure.setPopulationColumn(populationQueryColumn);
      fractionalDataSetTempTableStructure.setDistributionColumns(distributionColumns);
      fractionalDataSetTempTableStructure.setDimensionColumn(dimensionQueryColumn);
      fractionalDataSetTempTableStructure.setStatMeasureColumns(statMeasureColumns);
      fractionalDataSetTempTableStructure.setSliceCountColumn(sliceCountColumn);
      fractionalDataSetTempTableStructure.setPopulationSelectEntity(populationSelectEntity);
      fractionalDataSetTempTableStructure.setDistributionSelectEntities(distributionSelectEntities);
      fractionalDataSetTempTableStructure.setDimensionSelectEntity(dimensionSelectEntity);
      fractionalDataSetTempTableStructure.setStatMeasureSelectEntities(statMeasureSelectEntities);
      fractionalDataSetTempTableStructure.setUsedExistingCube(useExistingCube);
      return fractionalDataSetTempTableStructure;

   }

   public ICubeQueryEnhancerService getCubeQueryEnhancerService () {
      return cubeQueryEnhancerService;
   }

   public void setCubeQueryEnhancerService (ICubeQueryEnhancerService cubeQueryEnhancerService) {
      this.cubeQueryEnhancerService = cubeQueryEnhancerService;
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

   private IQueryGenerationUtilService getQueryGenerationUtilService (AssetProviderType assetProviderType) {
      return queryGenerationUtilServiceFactory.getQueryGenerationUtilService(assetProviderType);
   }

   public QueryGenerationUtilServiceFactory getQueryGenerationUtilServiceFactory () {
      return queryGenerationUtilServiceFactory;
   }

   public void setQueryGenerationUtilServiceFactory (QueryGenerationUtilServiceFactory queryGenerationUtilServiceFactory) {
      this.queryGenerationUtilServiceFactory = queryGenerationUtilServiceFactory;
   }

   public IBasicSampleSizeCalculatorService getBasicSampleSizeCalculatorService () {
      return basicSampleSizeCalculatorService;
   }

   public void setBasicSampleSizeCalculatorService (IBasicSampleSizeCalculatorService basicSampleSizeCalculatorService) {
      this.basicSampleSizeCalculatorService = basicSampleSizeCalculatorService;
   }

   public ISampleSizeCalculatorService getSampleSizeCalculatorService () {
      return sampleSizeCalculatorService;
   }

   public void setSampleSizeCalculatorService (ISampleSizeCalculatorService sampleSizeCalculatorService) {
      this.sampleSizeCalculatorService = sampleSizeCalculatorService;
   }

}
