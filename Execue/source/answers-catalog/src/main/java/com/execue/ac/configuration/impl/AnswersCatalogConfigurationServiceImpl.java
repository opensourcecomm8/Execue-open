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


package com.execue.ac.configuration.impl;

import java.util.List;

import com.execue.ac.configuration.IAnswersCatalogConfigurationService;
import com.execue.core.configuration.IConfiguration;

public class AnswersCatalogConfigurationServiceImpl implements IAnswersCatalogConfigurationService {

   private IConfiguration      answersCatalogConfiguration;
   private IConfiguration      answersCatalogDBConfiguration;

   // answers-catalog constants

   private static final String STATS_CONCEPT_NAME_KEY                                                                           = "ans-catalog.stat-concept-name";
   private static final String MEASURE_COLUMN_DATA_TYPE                                                                         = "ans-catalog.measure-column-data-type";
   private static final String FREQUENCY_COLUMN_DATA_TYPE                                                                       = "ans-catalog.frequency-column-data-type";
   private static final String MEASURE_COLUMN_PRECISION_VALUE_KEY                                                               = "ans-catalog.measure-column-precision";
   private static final String MEASURE_COLUMN_SCALE_VALUE_KEY                                                                   = "ans-catalog.measure-column-scale";

   // cube level constants
   private static final String DEFAULT_STAT_NAMES_KEY                                                                           = "ans-catalog.cube.static-values.stats";
   private static final String DIMENSION_COLUMN_DATA_TYPE_KEY                                                                   = "ans-catalog.cube.static-values.dimension-column-data-type";
   private static final String DIMENSION_COLUMN_MINIMUM_PRECISION_KEY                                                           = "ans-catalog.cube.static-values.dimension-column-minimum-precision";
   private static final String STAT_COLUMN_PRECISION_KEY                                                                        = "ans-catalog.cube.static-values.stat-column-precision";

   private static final String FREQUENCY_COLUMN_PREFIX_KEY                                                                      = "ans-catalog.cube.static-values.frequency-column-prefix";
   private static final String SIMPLE_LOOKUP_DIMENSION_PREFIX_KEY                                                               = "ans-catalog.cube.static-values.simple-lookup-dimension-prefix";
   private static final String RANGE_LOOKUP_DIMENSION_PREFIX_KEY                                                                = "ans-catalog.cube.static-values.range-lookup-dimension-prefix";
   private static final String QUERYID_COLUMN_NAME_KEY                                                                          = "ans-catalog.cube.static-values.queryId-column-name";
   private static final String QUERYID_COLUMN_DATA_TYPE_KEY                                                                     = "ans-catalog.cube.static-values.queryId-column-data-type";
   private static final String QUERYID_COLUMN_PRECISION_KEY                                                                     = "ans-catalog.cube.static-values.queryId-column-length";
   private static final String RANGE_LOOKUP_LOWER_LIMIT_COLUMN_NAME_KEY                                                         = "ans-catalog.cube.static-values.range-lookup-lower-limit-column-name";
   private static final String RANGE_LOOKUP_LOWER_UPPER_COLUMN_NAME_KEY                                                         = "ans-catalog.cube.static-values.range-lookup-upper-limit-column-name";
   private static final String DESCRIPTION_COLUMN_PREFIX_KEY                                                                    = "ans-catalog.cube.static-values.description-column-suffix";
   private static final String DESCRIPTION_COLUMN_DATA_TYPE_KEY                                                                 = "ans-catalog.cube.static-values.description-column-data-type";
   private static final String DESCRIPTION_COLUMN_PRECISION_KEY                                                                 = "ans-catalog.cube.static-values.description-column-precision";
   private static final String CUBE_FACT_TABLE_PREFIX_KEY                                                                       = "ans-catalog.cube.static-values.cube-fact-table-prefix";
   private static final String CUBE_PRE_FACT_TABLE_SUFFIX_KEY                                                                   = "ans-catalog.cube.static-values.cube-pre-fact-table-suffix";
   private static final String PAST_PATTERN_TO_CONSIDER_IN_DAYS_FOR_CUBE                                                        = "ans-catalog.cube.static-values.past-patterns-to-consider-for-optimal-dset-in-days";
   private static final String DATA_TRANSFER_QUERIES_EXECUTION_THREAD_POOL_SIZE                                                 = "ans-catalog.cube.static-values.thread-pool.data-transfer-queries-execution-thread-pool-size";
   private static final String CUBE_THREAD_WAIT_TIME                                                                            = "ans-catalog.cube.static-values.thread-pool.thread-wait-time";
   private static final String CLEAN_TEMPORARY_RESOURCES_FOR_CUBE                                                               = "ans-catalog.cube.static-values.clean-temporary-resources";

   // basic sampling algo constants
   private static final String BASIC_SAMPLING_ERROR_RATE_KEY                                                                    = "ans-catalog.mart.basic-sampling-algo.error-rate";
   private static final String BASIC_SAMPLING_CONFIDENCE_LEVEL_KEY                                                              = "ans-catalog.mart.basic-sampling-algo.confidence-level";
   private static final String BASIC_SAMPLING_LD_CONSTANT_ONE_KEY                                                               = "ans-catalog.mart.basic-sampling-algo.ld-constant-one";
   private static final String BASIC_SAMPLING_LD_CONSTANT_TWO_KEY                                                               = "ans-catalog.mart.basic-sampling-algo.ld-constant-two";
   private static final String BASIC_SAMPLING_IS_FLOOR_SETTING_REQUIRED_KEY                                                     = "ans-catalog.mart.basic-sampling-algo.is-floor-setting-required";
   private static final String BASIC_SAMPLING_MULTIPLICATION_FACTOR_FOR_FLOOR_KEY                                               = "ans-catalog.mart.basic-sampling-algo.multiplication-factor-for-floor";
   private static final String BASIC_SAMPLING_USE_FUNCTION_ALONE_KEY                                                            = "ans-catalog.mart.basic-sampling-algo.use-function-alone";
   private static final String BASIC_SAMPLING_P_LOW_VALUE_KEY                                                                   = "ans-catalog.mart.basic-sampling-algo.p-low";
   private static final String BASIC_SAMPLING_DEFAULT_NUMBER_OF_SLICES_FACTOR_KEY                                               = "ans-catalog.mart.basic-sampling-algo.default-number-of-slices-factor";
   private static final String BASIC_SAMPLING_MINIMUM_POPULATION_SIZE_KEY                                                       = "ans-catalog.mart.basic-sampling-algo.minimum-population-size";
   private static final String BASIC_SAMPLING_MAX_SAMPLE_PERCENTAGE_OF_POPULATION_KEY                                           = "ans-catalog.mart.basic-sampling-algo.max-sample-percentage-of-population";

   // sampling algo constants
   private static final String SAMPLING_ALGO_SUB_GROUPING_VALIDATION_MIN_POPULATION_PERCENTAGE_REQUIRED_KEY                     = "ans-catalog.mart.sampling-algo.sub-grouping-validation-rules.min-population-percentage-required";
   private static final String SAMPLING_ALGO_SUB_GROUPING_VALIDATION_MAX_COEFFICIENT_OF_VARIANCE_ALLOWED_KEY                    = "ans-catalog.mart.sampling-algo.sub-grouping-validation-rules.max-coefficient-of-variance-allowed";
   private static final String SAMPLING_ALGO_SUB_GROUPING_VALIDATION_MAX_COEFFICIENT_OF_VARIANCE_ALLOWED_BETWEEN_TWO_GROUPS_KEY = "ans-catalog.mart.sampling-algo.sub-grouping-validation-rules.max-coefficient-of-variance-allowed-between-two-groups";

   // mart based flags constants
   private static final String RANDOM_NUMBER_GENERATOR_DB_APPROACH_KEY                                                          = "ans-catalog.mart.flags.random-number-assigment-db-approach";

   // mart based static values
   private static final String SCALING_FACTOR_CONCEPT_NAME_KEY                                                                  = "ans-catalog.mart.static-values.scaling-factor-concept-name";
   private static final String APPLICABLE_STATS_KEY                                                                             = "ans-catalog.mart.static-values.stats";
   private static final String POPULATION_TABLE_NAME_KEY                                                                        = "ans-catalog.mart.static-values.population-table-name";
   private static final String POPULATION_TABLE_UNIQUE_ID_COLUMN_NAME_KEY                                                       = "ans-catalog.mart.static-values.population-table-uniqueId-column-name";
   private static final String POPULATION_TABLE_UNIQUE_ID_COLUMN_DATA_TYPE_KEY                                                  = "ans-catalog.mart.static-values.population-table-uniqueId-column-data-type";
   private static final String POPULATION_TABLE_UNIQUE_ID_COLUMN_LENGTH_KEY                                                     = "ans-catalog.mart.static-values.population-table-uniqueId-column-length";
   private static final String POPULATION_TABLE_SLICE_NUMBER_COLUMN_NAME_KEY                                                    = "ans-catalog.mart.static-values.population-table-slice-number-column-name";
   private static final String POPULATION_TABLE_SLICE_NUMBER_COLUMN_DATA_TYPE_KEY                                               = "ans-catalog.mart.static-values.population-table-slice-number-column-data-type";
   private static final String POPULATION_TABLE_SLICE_NUMBER_COLUMN_LENGTH_KEY                                                  = "ans-catalog.mart.static-values.population-table-slice-number-column-length";

   private static final String FRACTIONAL_TEMP_TABLE_NOTATION_KEY                                                               = "ans-catalog.mart.static-values.fractional-temp-table-notation";
   private static final String FRACTIONAL_TEMP_TABLE_UNIQUEID_COLUMN_NAME_KEY                                                   = "ans-catalog.mart.static-values.fractional-temp-table-uniqueId-column-name";
   private static final String FRACTIONAL_TEMP_TABLE_UNIQUEID_COLUMN_DATA_TYPE_KEY                                              = "ans-catalog.mart.static-values.fractional-temp-table-uniqueId-column-data-type";
   private static final String FRACTIONAL_TEMP_TABLE_UNIQUEID_COLUMN_LENGTH_KEY                                                 = "ans-catalog.mart.static-values.fractional-temp-table-uniqueId-column-length";
   private static final String FRACTIONAL_TEMP_TABLE_SLICE_COUNT_COLUMN_NAME_KEY                                                = "ans-catalog.mart.static-values.fractional-temp-table-slice-count-column-name";
   private static final String FRACTIONAL_TEMP_TABLE_SLICE_COUNT_COLUMN_DATA_TYPE_KEY                                           = "ans-catalog.mart.static-values.fractional-temp-table-slice-count-column-data-type";
   private static final String FRACTIONAL_TEMP_TABLE_SLICE_COUNT_COLUMN_LENGTH_KEY                                              = "ans-catalog.mart.static-values.fractional-temp-table-slice-count-column-length";

   private static final String FRACTIONAL_TABLE_NOTATION_KEY                                                                    = "ans-catalog.mart.static-values.fractional-table-notation";
   private static final String FRACTIONAL_TABLE_SFACTOR_COLUMN_NAME_KEY                                                         = "ans-catalog.mart.static-values.fractional-table-sfactor-column-name";
   private static final String FRACTIONAL_TABLE_SFACTOR_COLUMN_DATA_TYPE_KEY                                                    = "ans-catalog.mart.static-values.fractional-table-sfactor-column-data-type";
   private static final String FRACTIONAL_TABLE_SFACTOR_COLUMN_PRECISION_KEY                                                    = "ans-catalog.mart.static-values.fractional-table-sfactor-column-precision";
   private static final String FRACTIONAL_TABLE_SFACTOR_COLUMN_SCALE_KEY                                                        = "ans-catalog.mart.static-values.fractional-table-sfactor-column-scale";
   private static final String FRACTIONAL_POPULATION_TEMP_TABLE_NAME_KEY                                                        = "ans-catalog.mart.static-values.fractional-population-temp-table-name";
   private static final String FRACTIONAL_POPULATION_TABLE_NAME_KEY                                                             = "ans-catalog.mart.static-values.fractional-population-table-name";
   private static final String PAST_PATTERN_TO_CONSIDER_IN_DAYS_FOR_MART                                                        = "ans-catalog.mart.static-values.past-patterns-to-consider-for-optimal-dset-in-days";
   private static final String WAREHOUSE_EXTRACTION_THREAD_POOL_SIZE                                                            = "ans-catalog.mart.static-values.thread-pool.warehouse-extraction-thread-pool-size";
   private static final String BATCH_DATA_TRANSFER_THREAD_POOL_SIZE                                                             = "ans-catalog.mart.static-values.thread-pool.batch-data-transfer-thread-pool-size";
   private static final String SPLIT_BATCH_DATA_TRANSFER_TO_AVOID_SUB_CONDITIONS                                                = "ans-catalog.mart.static-values.split-batch-data-transfer-to-avoid-sub-conditions";
   private static final String CLEAN_TEMPORARY_RESOURCES_FOR_MART                                                               = "ans-catalog.mart.static-values.clean-temporary-resources";

   // mart based slicing algo constants
   private static final String SLICING_ALGO_ELIGIBILITY_CRITERIA_RECORDS_KEY                                                    = "ans-catalog.mart.slicing-algo.slicing-eligibility-criteria.criteria.records";
   private static final String SLICING_ALGO_ELIGIBILITY_CRITERIA_PERCENTAGE_KEY                                                 = "ans-catalog.mart.slicing-algo.slicing-eligibility-criteria.criteria.percentage";
   private static final String SLICING_ALGO_MIN_SLICES_KEY                                                                      = "ans-catalog.mart.slicing-algo.min-slices";
   private static final String SLICING_ALGO_MAX_SLICES_KEY                                                                      = "ans-catalog.mart.slicing-algo.max-slices";
   private static final String SLICING_ALGO_AVG_SLICES_KEY                                                                      = "ans-catalog.mart.slicing-algo.avg-slices";

   // mart based batch size algo
   private static final String BATCH_COUNT_ALGO_SQL_QUERY_MAX_SIZE_KEY                                                          = "ans-catalog.mart.batch-count-algo.sql-query-max-size";
   private static final String BATCH_COUNT_ALGO_SQL_QUERY_EMPTY_WHERE_CONDITION_SIZE_KEY                                        = "ans-catalog.mart.batch-count-algo.sql-query-empty-where-condition-size";
   private static final String BATCH_COUNT_ALGO_SQL_QUERY_WHERE_CONDITION_RECORD_BUFFER_LENGTH_KEY                              = "ans-catalog.mart.batch-count-algo.sql-query-where-condition-record-buffer-length";
   private static final String BATCH_COUNT_ALGO_SQL_MAX_ALLOWED_EXPRESSIONS_PER_CONDITION_KEY                                   = "ans-catalog.mart.batch-count-algo.sql-max-allowed-expressions-per-condition";

   // optimaldset algo constants
   private static final String BUILD_QUERY_HISTORY_FROM_QDATA                                                                   = "ans-catalog.optimaldset-algo.build-query-history-from-qdata";

   private static final String FRACTIONAL_TABLE_POPULATION_THREADPOOL_SIZE                                                      = "ans-catalog.mart.static-values.thread-pool.fractional-table-population-thread-pool-size";
   private static final String MAX_TASKS_ALLOWED_PER_THREAD_POOL                                                                = "ans-catalog.mart.static-values.thread-pool.max-tasks-allowed-per-thread-pool";
   private static final String MART_THREAD_WAIT_TIME                                                                            = "ans-catalog.mart.static-values.thread-pool.thread-wait-time";

   /**
    * @return the answersCatalogConfiguration
    */
   public IConfiguration getAnswersCatalogConfiguration () {
      return answersCatalogConfiguration;
   }

   /**
    * @param answersCatalogConfiguration
    *           the answersCatalogConfiguration to set
    */
   public void setAnswersCatalogConfiguration (IConfiguration answersCatalogConfiguration) {
      this.answersCatalogConfiguration = answersCatalogConfiguration;
   }

   @Override
   public Integer getBatchCountAlgoSQLMaxAllowedExpressionsPerCondition () {

      return getAnswersCatalogConfiguration().getInt(BATCH_COUNT_ALGO_SQL_MAX_ALLOWED_EXPRESSIONS_PER_CONDITION_KEY);

   }

   @Override
   public Integer getBatchCountAlgoSQLQueryEmptyWhereConditionRecordBufferLength () {
      return getAnswersCatalogConfiguration().getInt(
               BATCH_COUNT_ALGO_SQL_QUERY_WHERE_CONDITION_RECORD_BUFFER_LENGTH_KEY);
   }

   @Override
   public Integer getBatchCountAlgoSQLQueryEmptyWhereConditionSize () {
      return getAnswersCatalogConfiguration().getInt(BATCH_COUNT_ALGO_SQL_QUERY_EMPTY_WHERE_CONDITION_SIZE_KEY);
   }

   @Override
   public Long getBatchCountAlgoSQLQueryMaxSize () {
      return getAnswersCatalogConfiguration().getLong(BATCH_COUNT_ALGO_SQL_QUERY_MAX_SIZE_KEY);
   }

   @Override
   public Double getBasicSamplingConfidenceLevel () {
      return getAnswersCatalogConfiguration().getDouble(BASIC_SAMPLING_CONFIDENCE_LEVEL_KEY);
   }

   @Override
   public String getCubeFactTablePrefix () {
      return getAnswersCatalogConfiguration().getProperty(CUBE_FACT_TABLE_PREFIX_KEY);
   }

   @Override
   public String getCubePreFactTableSuffix () {
      return getAnswersCatalogConfiguration().getProperty(CUBE_PRE_FACT_TABLE_SUFFIX_KEY);
   }

   @Override
   public Boolean isCubeTargetDataSourceSameAsSourceDataSource () {
      return getAnswersCatalogDBConfiguration().getBoolean(CUBE_TARGET_DATASOURCE_SAME_AS_SOURCE_DATASOURCE_KEY);
   }

   @Override
   public Double getBasicSamplingDefaultNumberOfSlicesFactor () {
      return getAnswersCatalogConfiguration().getDouble(BASIC_SAMPLING_DEFAULT_NUMBER_OF_SLICES_FACTOR_KEY);
   }

   @Override
   public List<String> getDefaultStatNames () {
      return getAnswersCatalogConfiguration().getList(DEFAULT_STAT_NAMES_KEY);
   }

   @Override
   public String getDescriptionColumnDataType () {
      return getAnswersCatalogConfiguration().getProperty(DESCRIPTION_COLUMN_DATA_TYPE_KEY);
   }

   @Override
   public Integer getDescriptionColumnPrecision () {
      return getAnswersCatalogConfiguration().getInt(DESCRIPTION_COLUMN_PRECISION_KEY);
   }

   @Override
   public String getDescriptionColumnPrefix () {
      return getAnswersCatalogConfiguration().getProperty(DESCRIPTION_COLUMN_PREFIX_KEY);
   }

   @Override
   public String getDimensionColumnDataType () {
      return getAnswersCatalogConfiguration().getProperty(DIMENSION_COLUMN_DATA_TYPE_KEY);
   }

   @Override
   public Integer getDimensionColumnMinimunPrecision () {
      return getAnswersCatalogConfiguration().getInt(DIMENSION_COLUMN_MINIMUM_PRECISION_KEY);
   }

   @Override
   public Double getBasicSamplingErrorRate () {
      return getAnswersCatalogConfiguration().getDouble(BASIC_SAMPLING_ERROR_RATE_KEY);
   }

   @Override
   public String getFractionalPopulationTableName () {
      return getAnswersCatalogConfiguration().getProperty(FRACTIONAL_POPULATION_TABLE_NAME_KEY);
   }

   @Override
   public String getFractionalPopulationTempTableName () {
      return getAnswersCatalogConfiguration().getProperty(FRACTIONAL_POPULATION_TEMP_TABLE_NAME_KEY);
   }

   @Override
   public String getFractionalTableNotation () {
      return getAnswersCatalogConfiguration().getProperty(FRACTIONAL_TABLE_NOTATION_KEY);
   }

   @Override
   public String getFractionalTableSFactorColumnDataType () {
      return getAnswersCatalogConfiguration().getProperty(FRACTIONAL_TABLE_SFACTOR_COLUMN_DATA_TYPE_KEY);
   }

   @Override
   public String getFractionalTableSFactorColumnName () {
      return getAnswersCatalogConfiguration().getProperty(FRACTIONAL_TABLE_SFACTOR_COLUMN_NAME_KEY);
   }

   @Override
   public Integer getFractionalTableSFactorColumnPrecision () {
      return getAnswersCatalogConfiguration().getInt(FRACTIONAL_TABLE_SFACTOR_COLUMN_PRECISION_KEY);
   }

   @Override
   public Integer getFractionalTableSFactorColumnScale () {
      return getAnswersCatalogConfiguration().getInt(FRACTIONAL_TABLE_SFACTOR_COLUMN_SCALE_KEY);
   }

   @Override
   public String getFractionalTempTableNotation () {
      return getAnswersCatalogConfiguration().getProperty(FRACTIONAL_TEMP_TABLE_NOTATION_KEY);
   }

   @Override
   public String getFractionalTempTableSliceCountColumnDataType () {
      return getAnswersCatalogConfiguration().getProperty(FRACTIONAL_TEMP_TABLE_SLICE_COUNT_COLUMN_DATA_TYPE_KEY);
   }

   @Override
   public Integer getFractionalTempTableSliceCountColumnLength () {
      return getAnswersCatalogConfiguration().getInt(FRACTIONAL_TEMP_TABLE_SLICE_COUNT_COLUMN_LENGTH_KEY);
   }

   @Override
   public String getFractionalTempTableSliceCountColumnName () {
      return getAnswersCatalogConfiguration().getProperty(FRACTIONAL_TEMP_TABLE_SLICE_COUNT_COLUMN_NAME_KEY);
   }

   @Override
   public String getFractionalTempTableUniqueIdColumnDataType () {
      return getAnswersCatalogConfiguration().getProperty(FRACTIONAL_TEMP_TABLE_UNIQUEID_COLUMN_DATA_TYPE_KEY);
   }

   @Override
   public Integer getFractionalTempTableUniqueIdColumnLength () {
      return getAnswersCatalogConfiguration().getInt(FRACTIONAL_TEMP_TABLE_UNIQUEID_COLUMN_LENGTH_KEY);
   }

   @Override
   public String getFractionalTempTableUniqueIdColumnName () {
      return getAnswersCatalogConfiguration().getProperty(FRACTIONAL_TEMP_TABLE_UNIQUEID_COLUMN_NAME_KEY);
   }

   @Override
   public String getFrequencyColumnDataType () {
      return getAnswersCatalogConfiguration().getProperty(FREQUENCY_COLUMN_DATA_TYPE);
   }

   @Override
   public String getFrequencyColumnPrefix () {
      return getAnswersCatalogConfiguration().getProperty(FREQUENCY_COLUMN_PREFIX_KEY);
   }

   @Override
   public Double getBasicSamplingLdConstantOne () {
      return getAnswersCatalogConfiguration().getDouble(BASIC_SAMPLING_LD_CONSTANT_ONE_KEY);
   }

   @Override
   public Double getBasicSamplingLdConstantTwo () {
      return getAnswersCatalogConfiguration().getDouble(BASIC_SAMPLING_LD_CONSTANT_TWO_KEY);
   }

   @Override
   public List<String> getMartApplicableStats () {
      return getAnswersCatalogConfiguration().getList(APPLICABLE_STATS_KEY);
   }

   @Override
   public Integer getMartMaxDimensions () {
      return getAnswersCatalogDBConfiguration().getInt(MAX_DIMENSIONS_KEY);
   }

   @Override
   public Integer getMartMaxMeasures () {
      return getAnswersCatalogDBConfiguration().getInt(MAX_MEASURES_KEY);
   }

   @Override
   public Double getBasicSamplingMaxSamplePercentageOfPopulation () {
      return getAnswersCatalogConfiguration().getDouble(BASIC_SAMPLING_MAX_SAMPLE_PERCENTAGE_OF_POPULATION_KEY);
   }

   @Override
   public String getMeasureColumnDataType () {
      return getAnswersCatalogConfiguration().getProperty(MEASURE_COLUMN_DATA_TYPE);
   }

   @Override
   public Integer getMeasureColumnPrecision () {
      return getAnswersCatalogConfiguration().getInt(MEASURE_COLUMN_PRECISION_VALUE_KEY);
   }

   @Override
   public Integer getMeasureColumnScale () {
      return getAnswersCatalogConfiguration().getInt(MEASURE_COLUMN_SCALE_VALUE_KEY);
   }

   @Override
   public Integer getBasicSamplingMinimumPopulationSize () {
      return getAnswersCatalogConfiguration().getInt(BASIC_SAMPLING_MINIMUM_POPULATION_SIZE_KEY);
   }

   @Override
   public Double getBasicSamplingMultiplicationFactorForFloor () {
      return getAnswersCatalogConfiguration().getDouble(BASIC_SAMPLING_MULTIPLICATION_FACTOR_FOR_FLOOR_KEY);
   }

   @Override
   public Double getBasicSamplingPLowValue () {
      return getAnswersCatalogConfiguration().getDouble(BASIC_SAMPLING_P_LOW_VALUE_KEY);
   }

   @Override
   public String getPopulationTableName () {
      return getAnswersCatalogConfiguration().getProperty(POPULATION_TABLE_NAME_KEY);
   }

   @Override
   public String getPopulationTableSliceNumberColumnDataType () {
      return getAnswersCatalogConfiguration().getProperty(POPULATION_TABLE_SLICE_NUMBER_COLUMN_DATA_TYPE_KEY);
   }

   @Override
   public Integer getPopulationTableSliceNumberColumnLength () {
      return getAnswersCatalogConfiguration().getInt(POPULATION_TABLE_SLICE_NUMBER_COLUMN_LENGTH_KEY);
   }

   @Override
   public String getPopulationTableSliceNumberColumnName () {
      return getAnswersCatalogConfiguration().getProperty(POPULATION_TABLE_SLICE_NUMBER_COLUMN_NAME_KEY);
   }

   @Override
   public String getPopulationTableUniqueIdColumnDataType () {
      return getAnswersCatalogConfiguration().getProperty(POPULATION_TABLE_UNIQUE_ID_COLUMN_DATA_TYPE_KEY);
   }

   @Override
   public Integer getPopulationTableUniqueIdColumnLength () {
      return getAnswersCatalogConfiguration().getInt(POPULATION_TABLE_UNIQUE_ID_COLUMN_LENGTH_KEY);
   }

   @Override
   public String getPopulationTableUniqueIdColumnName () {
      return getAnswersCatalogConfiguration().getProperty(POPULATION_TABLE_UNIQUE_ID_COLUMN_NAME_KEY);
   }

   @Override
   public String getQueryIdColumnDataType () {
      return getAnswersCatalogConfiguration().getProperty(QUERYID_COLUMN_DATA_TYPE_KEY);
   }

   @Override
   public String getQueryIdColumnName () {
      return getAnswersCatalogConfiguration().getProperty(QUERYID_COLUMN_NAME_KEY);
   }

   @Override
   public Integer getQueryIdColumnPrecision () {
      return getAnswersCatalogConfiguration().getInt(QUERYID_COLUMN_PRECISION_KEY);
   }

   @Override
   public String getRangeLookupDimensionPrefix () {
      return getAnswersCatalogConfiguration().getProperty(RANGE_LOOKUP_DIMENSION_PREFIX_KEY);
   }

   @Override
   public String getRangeLookupLowerLimitColumnName () {
      return getAnswersCatalogConfiguration().getProperty(RANGE_LOOKUP_LOWER_LIMIT_COLUMN_NAME_KEY);
   }

   @Override
   public String getRangeLookupUpperLimitColumnName () {
      return getAnswersCatalogConfiguration().getProperty(RANGE_LOOKUP_LOWER_UPPER_COLUMN_NAME_KEY);
   }

   @Override
   public String getScallingFactorConceptName () {
      return getAnswersCatalogConfiguration().getProperty(SCALING_FACTOR_CONCEPT_NAME_KEY);
   }

   @Override
   public String getSimpleLookupDimensionPrefix () {
      return getAnswersCatalogConfiguration().getProperty(SIMPLE_LOOKUP_DIMENSION_PREFIX_KEY);
   }

   @Override
   public Integer getSlicingAlgorithmAvgSlices () {
      return getAnswersCatalogConfiguration().getInt(SLICING_ALGO_AVG_SLICES_KEY);
   }

   @Override
   public List<String> getSlicingAlgorithmEligibilityCriteriaPercentages () {
      return getAnswersCatalogConfiguration().getList(SLICING_ALGO_ELIGIBILITY_CRITERIA_PERCENTAGE_KEY);
   }

   @Override
   public List<String> getSlicingAlgorithmEligibilityCriteriaRecords () {
      return getAnswersCatalogConfiguration().getList(SLICING_ALGO_ELIGIBILITY_CRITERIA_RECORDS_KEY);
   }

   @Override
   public Integer getSlicingAlgorithmMaxSlices () {
      return getAnswersCatalogConfiguration().getInt(SLICING_ALGO_MAX_SLICES_KEY);
   }

   @Override
   public Integer getSlicingAlgorithmMinSlices () {
      return getAnswersCatalogConfiguration().getInt(SLICING_ALGO_MIN_SLICES_KEY);

   }

   @Override
   public String getStatsConceptName () {
      return getAnswersCatalogConfiguration().getProperty(STATS_CONCEPT_NAME_KEY);

   }

   @Override
   public Boolean isBasicSamplingFloorSettingRequired () {
      return getAnswersCatalogConfiguration().getBoolean(BASIC_SAMPLING_IS_FLOOR_SETTING_REQUIRED_KEY);

   }

   @Override
   public Boolean isMartTargetDataSourceSameAsSourceDataSource () {
      return getAnswersCatalogDBConfiguration().getBoolean(MART_TARGET_DATASOURCE_SAME_AS_SOURCE_DATASOURCE_KEY);

   }

   @Override
   public Boolean isRandomNumberGeneratedByDBApproach () {
      return getAnswersCatalogConfiguration().getBoolean(RANDOM_NUMBER_GENERATOR_DB_APPROACH_KEY);
   }

   @Override
   public Boolean useBasicSamplingFunctionAlone () {
      return getAnswersCatalogConfiguration().getBoolean(BASIC_SAMPLING_USE_FUNCTION_ALONE_KEY);
   }

   @Override
   public Double getMaxSpacePercentage () {
      return getAnswersCatalogDBConfiguration().getDouble(MAX_SPACE_PERCENTAGE_KEY);
   }

   @Override
   public Double getMinUsagePercentage () {
      return getAnswersCatalogDBConfiguration().getDouble(MIN_USAGE_PERCENTAGE_KEY);
   }

   @Override
   public Integer getStatColumnPrecision () {
      return getAnswersCatalogConfiguration().getInt(STAT_COLUMN_PRECISION_KEY);
   }

   public boolean useBasicSamplingAlgo () {
      return getAnswersCatalogDBConfiguration().getBoolean(MART_USE_BASIC_SAMPLING_ALGO_KEY);
   }

   public Double getSamplingAlgoErrorRatePercentage () {
      return getAnswersCatalogDBConfiguration().getDouble(SAMPLING_ALGO_ERROR_RATE_PERCENTAGE_KEY);
   }

   public Double getSamplingAlgoConfidenceLevelPercentage () {
      return getAnswersCatalogDBConfiguration().getDouble(SAMPLING_ALGO_CONFIDENCE_LEVEL_PERCENTAGE_KEY);
   }

   public Double getSamplingAlgoMinSamplePercentageOfPopulation () {
      return getAnswersCatalogDBConfiguration().getDouble(SAMPLING_ALGO_MIN_SAMPLE_PERCENTAGE_OF_POPULATION_KEY);
   }

   public Double getSamplingAlgoMaxSamplePercentageOfPopulationAllowed () {
      return getAnswersCatalogDBConfiguration()
               .getDouble(SAMPLING_ALGO_MAX_SAMPLE_PERCENTAGE_OF_POPULATION_ALLOWED_KEY);
   }

   public Double getSamplingAlgoSubGroupingMinPopulationPercentageRequired () {
      return getAnswersCatalogConfiguration().getDouble(
               SAMPLING_ALGO_SUB_GROUPING_VALIDATION_MIN_POPULATION_PERCENTAGE_REQUIRED_KEY);
   }

   public Double getSamplingAlgoSubGroupingMaxCoefficientOfVarianceAllowed () {
      return getAnswersCatalogConfiguration().getDouble(
               SAMPLING_ALGO_SUB_GROUPING_VALIDATION_MAX_COEFFICIENT_OF_VARIANCE_ALLOWED_KEY);
   }

   public Double getSamplingAlgoSubGroupingMaxCoefficientOfVarianceAllowedBetweenTwoGroups () {
      return getAnswersCatalogConfiguration().getDouble(
               SAMPLING_ALGO_SUB_GROUPING_VALIDATION_MAX_COEFFICIENT_OF_VARIANCE_ALLOWED_BETWEEN_TWO_GROUPS_KEY);
   }

   @Override
   public Integer getPastPatternsToConsiderForOptimalDSetInDaysForCube () {
      return getAnswersCatalogConfiguration().getInt(PAST_PATTERN_TO_CONSIDER_IN_DAYS_FOR_CUBE);
   }

   @Override
   public Integer getPastPatternsToConsiderForOptimalDSetInDaysForMart () {
      return getAnswersCatalogConfiguration().getInt(PAST_PATTERN_TO_CONSIDER_IN_DAYS_FOR_MART);
   }

   @Override
   public Integer getDataTransferQueriesExecutionThreadPoolSize () {
      return getAnswersCatalogConfiguration().getInt(DATA_TRANSFER_QUERIES_EXECUTION_THREAD_POOL_SIZE);
   }

   @Override
   public Integer getWarehouseExtractionThreadPoolSize () {
      return getAnswersCatalogConfiguration().getInt(WAREHOUSE_EXTRACTION_THREAD_POOL_SIZE);
   }

   @Override
   public Integer getBatchDataTransferThreadPoolSize () {
      return getAnswersCatalogConfiguration().getInt(BATCH_DATA_TRANSFER_THREAD_POOL_SIZE);
   }

   @Override
   public boolean isSplitBatchDataTransferToAvoidSubConditions () {
      return getAnswersCatalogConfiguration().getBoolean(SPLIT_BATCH_DATA_TRANSFER_TO_AVOID_SUB_CONDITIONS);
   }

   @Override
   public boolean buildQueryHistoryFromQdata () {
      return getAnswersCatalogConfiguration().getBoolean(BUILD_QUERY_HISTORY_FROM_QDATA);
   }

   @Override
   public boolean applyContraintsInOptimalDSetAlgorithm () {
      return getAnswersCatalogDBConfiguration().getBoolean(APPLY_CONSTRAINTS_IN_OPTIMAL_DSET_ALGORITHM_KEY);
   }

   @Override
   public boolean deduceSpaceAtRuntimeInOptimalDSetAlgorithm () {
      return getAnswersCatalogDBConfiguration().getBoolean(DEDUCE_SPACE_AT_RUNTIME_IN_OPTIMAL_DSET_ALGORITHM_KEY);
   }

   @Override
   public Double getConfiguredParentAssetSpaceForOptimalDSetAlgorithm () {
      return getAnswersCatalogDBConfiguration().getDouble(CONFIGURED_PARENT_ASSET_SPACE_FOR_OPTIMAL_DSET_ALGORITHM_KEY);
   }

   @Override
   public Integer getConfiguredNumberOfMeasuresInParentAsset () {
      return getAnswersCatalogDBConfiguration().getInt(CONFIGURED_NUMBER_OF_MEASURES_IN_PARENT_ASSET);
   }

   @Override
   public Integer getConfiguredDimensionLookupValueColumnSize () {
      return getAnswersCatalogDBConfiguration().getInt(CONFIGURED_DIMENSION_LOOKUP_VALUE_COLUMN_SIZE);
   }

   @Override
   public Integer getFractionalTablePopulationThreadPoolSize () {
      return getAnswersCatalogConfiguration().getInt(FRACTIONAL_TABLE_POPULATION_THREADPOOL_SIZE);
   }

   @Override
   public Integer getMaxTasksAllowedPerThreadPool () {
      return getAnswersCatalogConfiguration().getInt(MAX_TASKS_ALLOWED_PER_THREAD_POOL);
   }

   @Override
   public Integer getThreadWaitTimeForCube () {
      return getAnswersCatalogConfiguration().getInt(CUBE_THREAD_WAIT_TIME);
   }

   @Override
   public Integer getThreadWaitTimeForMart () {
      return getAnswersCatalogConfiguration().getInt(MART_THREAD_WAIT_TIME);
   }

   public boolean isCleanTemporaryResourcesOnMart () {
      return getAnswersCatalogConfiguration().getBoolean(CLEAN_TEMPORARY_RESOURCES_FOR_MART);
   }

   public boolean isCleanTemporaryResourcesOnCube () {
      return getAnswersCatalogConfiguration().getBoolean(CLEAN_TEMPORARY_RESOURCES_FOR_CUBE);
   }

   public IConfiguration getAnswersCatalogDBConfiguration () {
      return answersCatalogDBConfiguration;
   }

   public void setAnswersCatalogDBConfiguration (IConfiguration answersCatalogDBConfiguration) {
      this.answersCatalogDBConfiguration = answersCatalogDBConfiguration;
   }

}
