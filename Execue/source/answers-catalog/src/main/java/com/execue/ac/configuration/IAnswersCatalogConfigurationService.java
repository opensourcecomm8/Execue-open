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


package com.execue.ac.configuration;

import java.util.List;

public interface IAnswersCatalogConfigurationService {

   // sampling algo constants
   public static final String SAMPLING_ALGO_ERROR_RATE_PERCENTAGE_KEY                       = "ans-catalog.mart.sampling-algo.error-rate-percentage";
   public static final String SAMPLING_ALGO_CONFIDENCE_LEVEL_PERCENTAGE_KEY                 = "ans-catalog.mart.sampling-algo.confidence-level-percentage";
   public static final String SAMPLING_ALGO_MIN_SAMPLE_PERCENTAGE_OF_POPULATION_KEY         = "ans-catalog.mart.sampling-algo.min-sample-percentage-of-population";
   public static final String SAMPLING_ALGO_MAX_SAMPLE_PERCENTAGE_OF_POPULATION_ALLOWED_KEY = "ans-catalog.mart.sampling-algo.max-sample-percentage-of-population-allowed";

   // handler level constants
   public static final String MAX_DIMENSIONS_KEY                                            = "ans-catalog.mart.static-values.max-eligible-dimensions";
   public static final String MAX_MEASURES_KEY                                              = "ans-catalog.mart.static-values.max-eligible-measures";

   // mart based flags constants
   public static final String MART_TARGET_DATASOURCE_SAME_AS_SOURCE_DATASOURCE_KEY          = "ans-catalog.mart.flags.target-data-source-same-as-source-data-source";
   public static final String MART_USE_BASIC_SAMPLING_ALGO_KEY                              = "ans-catalog.mart.flags.use-basic-sampling-algo";

   // cube based flags constants
   public static final String CUBE_TARGET_DATASOURCE_SAME_AS_SOURCE_DATASOURCE_KEY          = "ans-catalog.cube.flags.target-data-source-same-as-source-data-source";

   // optimaldset algo constants
   public static final String MIN_USAGE_PERCENTAGE_KEY                                      = "ans-catalog.optimaldset-algo.cube.min-usage-percentage";
   public static final String MAX_SPACE_PERCENTAGE_KEY                                      = "ans-catalog.optimaldset-algo.cube.max-space-percentage";
   public static final String APPLY_CONSTRAINTS_IN_OPTIMAL_DSET_ALGORITHM_KEY               = "ans-catalog.optimaldset-algo.apply-contraints";
   public static final String DEDUCE_SPACE_AT_RUNTIME_IN_OPTIMAL_DSET_ALGORITHM_KEY         = "ans-catalog.optimaldset-algo.deduce-space-at-runtime";
   public static final String CONFIGURED_PARENT_ASSET_SPACE_FOR_OPTIMAL_DSET_ALGORITHM_KEY  = "ans-catalog.optimaldset-algo.configured-parent-asset-space";
   public static final String CONFIGURED_NUMBER_OF_MEASURES_IN_PARENT_ASSET                 = "ans-catalog.optimaldset-algo.configured-number-of-measures";
   public static final String CONFIGURED_DIMENSION_LOOKUP_VALUE_COLUMN_SIZE                 = "ans-catalog.optimaldset-algo.configured-dimension-lookup-value-column-length";

   public String getStatsConceptName ();

   public String getMeasureColumnDataType ();

   public String getFrequencyColumnDataType ();

   public Integer getMeasureColumnPrecision ();

   public Integer getMeasureColumnScale ();

   public List<String> getDefaultStatNames ();

   public String getDimensionColumnDataType ();

   public Integer getDimensionColumnMinimunPrecision ();

   public Integer getStatColumnPrecision ();

   public String getFrequencyColumnPrefix ();

   public String getSimpleLookupDimensionPrefix ();

   public String getRangeLookupDimensionPrefix ();

   public String getQueryIdColumnName ();

   public String getQueryIdColumnDataType ();

   public Integer getQueryIdColumnPrecision ();

   public String getRangeLookupLowerLimitColumnName ();

   public String getRangeLookupUpperLimitColumnName ();

   public String getDescriptionColumnPrefix ();

   public String getDescriptionColumnDataType ();

   public Integer getDescriptionColumnPrecision ();

   public String getCubeFactTablePrefix ();

   public String getCubePreFactTableSuffix ();

   public Boolean isCubeTargetDataSourceSameAsSourceDataSource ();

   public Double getBasicSamplingErrorRate ();

   public Double getBasicSamplingConfidenceLevel ();

   public Double getBasicSamplingLdConstantOne ();

   public Double getBasicSamplingLdConstantTwo ();

   public Boolean isBasicSamplingFloorSettingRequired ();

   public Double getBasicSamplingMultiplicationFactorForFloor ();

   public Boolean useBasicSamplingFunctionAlone ();

   public Double getBasicSamplingPLowValue ();

   public Double getBasicSamplingDefaultNumberOfSlicesFactor ();

   public Integer getBasicSamplingMinimumPopulationSize ();

   public Double getBasicSamplingMaxSamplePercentageOfPopulation ();

   public Boolean isRandomNumberGeneratedByDBApproach ();

   public Boolean isMartTargetDataSourceSameAsSourceDataSource ();

   public String getScallingFactorConceptName ();

   public List<String> getMartApplicableStats ();

   public String getPopulationTableName ();

   public String getPopulationTableUniqueIdColumnName ();

   public String getPopulationTableUniqueIdColumnDataType ();

   public Integer getPopulationTableUniqueIdColumnLength ();

   public String getPopulationTableSliceNumberColumnName ();

   public String getPopulationTableSliceNumberColumnDataType ();

   public Integer getPopulationTableSliceNumberColumnLength ();

   public String getFractionalTempTableNotation ();

   public String getFractionalTempTableUniqueIdColumnName ();

   public String getFractionalTempTableUniqueIdColumnDataType ();

   public Integer getFractionalTempTableUniqueIdColumnLength ();

   public String getFractionalTempTableSliceCountColumnName ();

   public String getFractionalTempTableSliceCountColumnDataType ();

   public Integer getFractionalTempTableSliceCountColumnLength ();

   public String getFractionalTableNotation ();

   public String getFractionalTableSFactorColumnName ();

   public String getFractionalTableSFactorColumnDataType ();

   public Integer getFractionalTableSFactorColumnScale ();

   public Integer getFractionalTableSFactorColumnPrecision ();

   public String getFractionalPopulationTempTableName ();

   public String getFractionalPopulationTableName ();

   public List<String> getSlicingAlgorithmEligibilityCriteriaRecords ();

   public List<String> getSlicingAlgorithmEligibilityCriteriaPercentages ();

   public Integer getSlicingAlgorithmMinSlices ();

   public Integer getSlicingAlgorithmMaxSlices ();

   public Integer getSlicingAlgorithmAvgSlices ();

   public Integer getMartMaxDimensions ();

   public Integer getMartMaxMeasures ();

   public Long getBatchCountAlgoSQLQueryMaxSize ();

   public Integer getBatchCountAlgoSQLQueryEmptyWhereConditionSize ();

   public Integer getBatchCountAlgoSQLQueryEmptyWhereConditionRecordBufferLength ();

   public Integer getBatchCountAlgoSQLMaxAllowedExpressionsPerCondition ();

   public Double getMinUsagePercentage ();

   public Double getMaxSpacePercentage ();

   public boolean useBasicSamplingAlgo ();

   public Double getSamplingAlgoErrorRatePercentage ();

   public Double getSamplingAlgoConfidenceLevelPercentage ();

   public Double getSamplingAlgoMinSamplePercentageOfPopulation ();

   public Double getSamplingAlgoMaxSamplePercentageOfPopulationAllowed ();

   public Double getSamplingAlgoSubGroupingMinPopulationPercentageRequired ();

   public Double getSamplingAlgoSubGroupingMaxCoefficientOfVarianceAllowed ();

   public Double getSamplingAlgoSubGroupingMaxCoefficientOfVarianceAllowedBetweenTwoGroups ();

   public Integer getPastPatternsToConsiderForOptimalDSetInDaysForMart ();

   public Integer getPastPatternsToConsiderForOptimalDSetInDaysForCube ();

   public boolean buildQueryHistoryFromQdata ();

   public boolean applyContraintsInOptimalDSetAlgorithm ();

   public boolean deduceSpaceAtRuntimeInOptimalDSetAlgorithm ();

   public Double getConfiguredParentAssetSpaceForOptimalDSetAlgorithm ();

   public Integer getConfiguredNumberOfMeasuresInParentAsset ();

   public Integer getConfiguredDimensionLookupValueColumnSize ();

   public Integer getWarehouseExtractionThreadPoolSize ();

   public Integer getDataTransferQueriesExecutionThreadPoolSize ();

   public Integer getBatchDataTransferThreadPoolSize ();

   public boolean isSplitBatchDataTransferToAvoidSubConditions ();

   public Integer getFractionalTablePopulationThreadPoolSize ();

   public boolean isCleanTemporaryResourcesOnMart ();

   public boolean isCleanTemporaryResourcesOnCube ();

   public Integer getMaxTasksAllowedPerThreadPool ();

   public Integer getThreadWaitTimeForMart ();

   public Integer getThreadWaitTimeForCube ();
}
