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


package com.execue.web.sysconfig.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.ac.configuration.IAnswersCatalogConfigurationService;
import com.execue.core.configuration.IConfiguration;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.driver.configuration.IDriverConfigurationService;
import com.execue.report.configuration.service.IPresentationConfigurationService;
import com.execue.repoting.aggregation.configuration.IAggregationConfigurationService;
import com.execue.web.sysconfig.bean.AnswersCatalogConfigurationViewInfo;
import com.execue.web.sysconfig.bean.CommonConfigurationViewInfo;
import com.execue.web.sysconfig.bean.ConfigurationProperty;
import com.execue.web.sysconfig.bean.ReportAggregationConfigurationViewInfo;
import com.execue.web.sysconfig.bean.ReportPresentationConfigurationViewInfo;
import com.execue.web.sysconfig.helper.SystemConfigurationServiceHelper;
import com.execue.web.sysconfig.service.ISystemConfigurationService;

public class SystemConfigurationServiceImpl implements ISystemConfigurationService {

   private ICoreConfigurationService           coreConfigurationService;
   private IDriverConfigurationService         driverConfigurationService;
   private IAggregationConfigurationService    aggregationConfigurationService;
   private IPresentationConfigurationService   presentationConfigurationService;
   private IAnswersCatalogConfigurationService answersCatalogConfigurationService;

   private IConfiguration                      coreDBConfiguration;
   private IConfiguration                      driverDBConfiguration;
   private IConfiguration                      answersCatalogDBConfiguration;
   private IConfiguration                      aggregationDBConfiguration;
   private IConfiguration                      rpDBConfiguration;

   @Override
   public AnswersCatalogConfigurationViewInfo getAnswersCatalogConfigurations () {
      AnswersCatalogConfigurationViewInfo ansCatConfig = new AnswersCatalogConfigurationViewInfo();

      // Build and populate modifiable properties

      ConfigurationProperty samplingAlgoErroRate = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.ANSWERS_CATALOG_SAMPLING_ERROR_RATE_IDENTITY,
               getAnswersCatalogConfigurationService().getSamplingAlgoErrorRatePercentage());
      ConfigurationProperty samplingAlgoConfidenceLevel = SystemConfigurationServiceHelper
               .prepareConfigurationProperty(
                        SystemConfigurationServiceHelper.ANSWERS_CATALOG_SAMPLING_CONFIDENCE_LEVEL_IDENTITY,
                        getAnswersCatalogConfigurationService().getSamplingAlgoConfidenceLevelPercentage());
      ConfigurationProperty samplingAlgoMinPopulation = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.ANSWERS_CATALOG_SAMPLING_MIN_SAMPLE_IDENTITY,
               getAnswersCatalogConfigurationService().getSamplingAlgoMinSamplePercentageOfPopulation());
      ConfigurationProperty samplingAlgoMaxPopulation = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.ANSWERS_CATALOG_SAMPLING_MAX_SAMPLE_IDENTITY,
               getAnswersCatalogConfigurationService().getSamplingAlgoMaxSamplePercentageOfPopulationAllowed());

      ConfigurationProperty martMaxDimensions = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.ANSWERS_CATALOG_MART_MAX_DIMS_IDENTITY,
               getAnswersCatalogConfigurationService().getMartMaxDimensions());
      ConfigurationProperty martMaxMeasures = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.ANSWERS_CATALOG_MART_MAX_MEASURES_IDENTITY,
               getAnswersCatalogConfigurationService().getMartMaxMeasures());

      ConfigurationProperty martSourceSameAsTarget = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.ANSWERS_CATALOG_MART_BE_BUILT_AT_SOURCE_IDENTITY,
               getAnswersCatalogConfigurationService().isMartTargetDataSourceSameAsSourceDataSource());
      martSourceSameAsTarget.setDisplayValue(SystemConfigurationServiceHelper.getCorrespondingCheckType(
               Boolean.valueOf(martSourceSameAsTarget.getValue())).getDescription());

      ConfigurationProperty martUseBasicAlgo = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.ANSWERS_CATALOG_MART_USE_BASIC_SAMPLING_ALGO_IDENTITY,
               getAnswersCatalogConfigurationService().useBasicSamplingAlgo());
      martUseBasicAlgo.setDisplayValue(SystemConfigurationServiceHelper.getCorrespondingCheckType(
               Boolean.valueOf(martUseBasicAlgo.getValue())).getDescription());

      ConfigurationProperty cubeSourceSameAsTarget = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.ANSWERS_CATALOG_CUBE_BE_BUILT_AT_SOURCE_IDENTITY,
               getAnswersCatalogConfigurationService().isCubeTargetDataSourceSameAsSourceDataSource());
      cubeSourceSameAsTarget.setDisplayValue(SystemConfigurationServiceHelper.getCorrespondingCheckType(
               Boolean.valueOf(cubeSourceSameAsTarget.getValue())).getDescription());

      ConfigurationProperty optCubeMinUsage = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.ANSWERS_CATALOG_OPTD_CUBE_MIN_USAGE_IDENTITY,
               getAnswersCatalogConfigurationService().getMinUsagePercentage());
      ConfigurationProperty optCubeMaxSpace = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.ANSWERS_CATALOG_OPTD_CUBE_MAX_SPACE_IDENTITY,
               getAnswersCatalogConfigurationService().getMaxSpacePercentage());

      ConfigurationProperty optApplyConstraints = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.ANSWERS_CATALOG_OPTD_APPLY_CONSTRAINTS_IDENTITY,
               getAnswersCatalogConfigurationService().applyContraintsInOptimalDSetAlgorithm());
      optApplyConstraints.setDisplayValue(SystemConfigurationServiceHelper.getCorrespondingCheckType(
               Boolean.valueOf(optApplyConstraints.getValue())).getDescription());

      ConfigurationProperty optSpaceAtRuntime = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.ANSWERS_CATALOG_OPTD_SPACE_AT_RUNTIME_IDENTITY,
               getAnswersCatalogConfigurationService().deduceSpaceAtRuntimeInOptimalDSetAlgorithm());
      optSpaceAtRuntime.setDisplayValue(SystemConfigurationServiceHelper.getCorrespondingCheckType(
               Boolean.valueOf(optSpaceAtRuntime.getValue())).getDescription());

      ConfigurationProperty optParentAssetSpace = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.ANSWERS_CATALOG_OPTD_PARENT_ASSET_SPACE_IDENTITY,
               getAnswersCatalogConfigurationService().getConfiguredParentAssetSpaceForOptimalDSetAlgorithm());
      optParentAssetSpace.setDisplayValue(String.valueOf(Double.valueOf(optParentAssetSpace.getValue()).longValue()));
      optParentAssetSpace.setValue(String.valueOf(Double.valueOf(optParentAssetSpace.getValue()).longValue()));

      ConfigurationProperty optNumberOfParentAssetMeasures = SystemConfigurationServiceHelper
               .prepareConfigurationProperty(
                        SystemConfigurationServiceHelper.ANSWERS_CATALOG_OPTD_NUMBER_OF_MEASURES_IN_PARENT_ASSET_IDENTITY,
                        getAnswersCatalogConfigurationService().getConfiguredNumberOfMeasuresInParentAsset());
      ConfigurationProperty optLookupValueColumnLengthAtParentAsset = SystemConfigurationServiceHelper
               .prepareConfigurationProperty(
                        SystemConfigurationServiceHelper.ANSWERS_CATALOG_OPTD_LOOKUP_COLUMN_LENGTH_IDENTITY,
                        getAnswersCatalogConfigurationService().getConfiguredDimensionLookupValueColumnSize());

      ansCatConfig.setSamplingAlgoConfidenceLevel(samplingAlgoConfidenceLevel);
      ansCatConfig.setSamplingAlgoErroRate(samplingAlgoErroRate);
      ansCatConfig.setSamplingAlgoMaxPopulation(samplingAlgoMaxPopulation);
      ansCatConfig.setSamplingAlgoMinPopulation(samplingAlgoMinPopulation);
      ansCatConfig.setMartMaxDimensions(martMaxDimensions);
      ansCatConfig.setMartMaxMeasures(martMaxMeasures);
      ansCatConfig.setMartSourceSameAsTarget(martSourceSameAsTarget);
      ansCatConfig.setMartUseBasicAlgo(martUseBasicAlgo);
      ansCatConfig.setCubeSourceSameAsTarget(cubeSourceSameAsTarget);
      ansCatConfig.setOptApplyConstraints(optApplyConstraints);
      ansCatConfig.setOptCubeMaxSpace(optCubeMaxSpace);
      ansCatConfig.setOptCubeMinUsage(optCubeMinUsage);
      ansCatConfig.setOptLookupValueColumnLengthAtParentAsset(optLookupValueColumnLengthAtParentAsset);
      ansCatConfig.setOptNumberOfParentAssetMeasures(optNumberOfParentAssetMeasures);
      ansCatConfig.setOptParentAssetSpace(optParentAssetSpace);
      ansCatConfig.setOptSpaceAtRuntime(optSpaceAtRuntime);

      // Build read only properties
      List<ConfigurationProperty> readOnlyConfigProperties = new ArrayList<ConfigurationProperty>();

      ansCatConfig.setReadOnlyConfigurations(readOnlyConfigProperties);

      return ansCatConfig;
   }

   @Override
   public CommonConfigurationViewInfo getCommonConfigurations () {

      CommonConfigurationViewInfo commonConfig = new CommonConfigurationViewInfo();

      // Build and populate modifiable properties
      ConfigurationProperty displayQueryStringConfigProperty = SystemConfigurationServiceHelper
               .prepareConfigurationProperty(SystemConfigurationServiceHelper.COMMON_SHOW_REVIEW_QUERY_IDENTITY,
                        getDriverConfigurationService().isDisplayQueryString());
      displayQueryStringConfigProperty.setDisplayValue(SystemConfigurationServiceHelper.getCorrespondingCheckType(
               Boolean.valueOf(displayQueryStringConfigProperty.getValue())).getDescription());

      ConfigurationProperty systemLevelDefaultStatConfigProperty = SystemConfigurationServiceHelper
               .prepareConfigurationProperty(SystemConfigurationServiceHelper.COMMON_SYSTEM_LEVEL_STAT_IDENTITY,
                        getCoreConfigurationService().getSystemLevelDefaultStat());
      systemLevelDefaultStatConfigProperty.setDisplayValue(SystemConfigurationServiceHelper.getCorrespondingStatType(
               systemLevelDefaultStatConfigProperty.getValue()).getDescription());

      commonConfig.setDisplayQueryString(displayQueryStringConfigProperty);
      commonConfig.setSystemLevelDefaultStat(systemLevelDefaultStatConfigProperty);

      // Build read only properties
      List<ConfigurationProperty> readOnlyConfigProperties = new ArrayList<ConfigurationProperty>();

      commonConfig.setReadOnlyConfigurations(readOnlyConfigProperties);

      return commonConfig;
   }

   @Override
   public ReportAggregationConfigurationViewInfo getReportAggregationConfigurations () {
      ReportAggregationConfigurationViewInfo aggConfig = new ReportAggregationConfigurationViewInfo();

      // Build and populate modifiable properties
      ConfigurationProperty enableDynamicRanges = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.REPORT_AGGREGATION_ENABLE_DYNAMIC_RANGES_IDENTITY,
               getAggregationConfigurationService().enableDynamicRanges());
      enableDynamicRanges.setDisplayValue(SystemConfigurationServiceHelper.getCorrespondingCheckType(
               Boolean.valueOf(enableDynamicRanges.getValue())).getDescription());

      ConfigurationProperty skipUniVariants = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.REPORT_AGGREGATION_SKIP_UNI_VARIANTS_IDENTITY,
               getAggregationConfigurationService().skipUnivariants());
      skipUniVariants.setDisplayValue(SystemConfigurationServiceHelper.getCorrespondingCheckType(
               Boolean.valueOf(skipUniVariants.getValue())).getDescription());

      ConfigurationProperty enableDetailReports = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.REPORT_AGGREGATION_ENABLE_DETAIL_REPORTS_IDENTITY,
               getAggregationConfigurationService().enableDetailReports());
      enableDetailReports.setDisplayValue(SystemConfigurationServiceHelper.getCorrespondingCheckType(
               Boolean.valueOf(enableDetailReports.getValue())).getDescription());

      ConfigurationProperty dynamicRangeBandCount = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.REPORT_AGGREGATION_DYNAMIC_RANGES_BAND_COUNT_IDENTITY,
               getAggregationConfigurationService().getDynamicRangesBandCount());
      ConfigurationProperty detailReportSelectionThreshold = SystemConfigurationServiceHelper
               .prepareConfigurationProperty(
                        SystemConfigurationServiceHelper.REPORT_AGGREGATION_DETAIL_REPORT_SELECTION_THRESHOLD_IDENTITY,
                        getAggregationConfigurationService().getDetailReportSelectionThreshold());
      ConfigurationProperty dataBrowserMinRecords = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.REPORT_AGGREGATION_DATA_BROWSER_MIN_RECORDS_IDENTITY,
               getAggregationConfigurationService().getDataBrowserMinRecords());
      ConfigurationProperty dataBrowserMaxRecords = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.REPORT_AGGREGATION_DATA_BROWSER_MAX_RECORDS_IDENTITY,
               getAggregationConfigurationService().getDataBrowserMaxRecords());
      ConfigurationProperty reportTitleLength = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.REPORT_AGGREGATION_REPORT_TITLE_LENGTH_IDENTITY,
               getAggregationConfigurationService().getReportTitleMaxAllowedLength());

      aggConfig.setEnableDynamicRanges(enableDynamicRanges);
      aggConfig.setSkipUniVariants(skipUniVariants);
      aggConfig.setEnableDetailReports(enableDetailReports);
      aggConfig.setDynamicRangeBandCount(dynamicRangeBandCount);
      aggConfig.setReportTitleLength(reportTitleLength);
      aggConfig.setDetailReportSelectionThreshold(detailReportSelectionThreshold);
      aggConfig.setDataBrowserMinRecords(dataBrowserMinRecords);
      aggConfig.setDataBrowserMaxRecords(dataBrowserMaxRecords);

      // Build read only properties
      List<ConfigurationProperty> readOnlyConfigProperties = new ArrayList<ConfigurationProperty>();

      aggConfig.setReadOnlyConfigurations(readOnlyConfigProperties);

      return aggConfig;
   }

   @Override
   public ReportPresentationConfigurationViewInfo getReportPresentationConfigurations () {
      ReportPresentationConfigurationViewInfo repConfig = new ReportPresentationConfigurationViewInfo();

      // Build and populate modifiable properties
      ConfigurationProperty axisLabelMaxLengthProperty = SystemConfigurationServiceHelper.prepareConfigurationProperty(
               SystemConfigurationServiceHelper.REPORT_PRESENTATION_CHART_AXIS_LABEL_MAXIMUM_LENGTH_IDENTITY,
               getPresentationConfigurationService().getAxisLabelMaxLength());

      repConfig.setAxisLabelMaxLength(axisLabelMaxLengthProperty);

      // Build read only properties
      List<ConfigurationProperty> readOnlyConfigProperties = new ArrayList<ConfigurationProperty>();

      repConfig.setReadOnlyConfigurations(readOnlyConfigProperties);

      return repConfig;
   }

   @Override
   public void saveAnswersCatalogConfigurations (AnswersCatalogConfigurationViewInfo answersCatalogConfoguration) {
      
      getAnswersCatalogDBConfiguration().saveConfigurationProperty(
               IAnswersCatalogConfigurationService.SAMPLING_ALGO_CONFIDENCE_LEVEL_PERCENTAGE_KEY,
               answersCatalogConfoguration.getSamplingAlgoConfidenceLevel().getValue());
      getAnswersCatalogDBConfiguration().saveConfigurationProperty(
               IAnswersCatalogConfigurationService.SAMPLING_ALGO_ERROR_RATE_PERCENTAGE_KEY,
               answersCatalogConfoguration.getSamplingAlgoErroRate().getValue());
      getAnswersCatalogDBConfiguration().saveConfigurationProperty(
               IAnswersCatalogConfigurationService.SAMPLING_ALGO_MAX_SAMPLE_PERCENTAGE_OF_POPULATION_ALLOWED_KEY,
               answersCatalogConfoguration.getSamplingAlgoMaxPopulation().getValue());
      getAnswersCatalogDBConfiguration().saveConfigurationProperty(
               IAnswersCatalogConfigurationService.SAMPLING_ALGO_MIN_SAMPLE_PERCENTAGE_OF_POPULATION_KEY,
               answersCatalogConfoguration.getSamplingAlgoMinPopulation().getValue());

      getAnswersCatalogDBConfiguration().saveConfigurationProperty(
               IAnswersCatalogConfigurationService.MART_TARGET_DATASOURCE_SAME_AS_SOURCE_DATASOURCE_KEY,
               answersCatalogConfoguration.getMartSourceSameAsTarget().getValue());
      getAnswersCatalogDBConfiguration().saveConfigurationProperty(
               IAnswersCatalogConfigurationService.MART_USE_BASIC_SAMPLING_ALGO_KEY,
               answersCatalogConfoguration.getMartUseBasicAlgo().getValue());

      getAnswersCatalogDBConfiguration().saveConfigurationProperty(
               IAnswersCatalogConfigurationService.CUBE_TARGET_DATASOURCE_SAME_AS_SOURCE_DATASOURCE_KEY,
               answersCatalogConfoguration.getCubeSourceSameAsTarget().getValue());

      getAnswersCatalogDBConfiguration().saveConfigurationProperty(
               IAnswersCatalogConfigurationService.MAX_DIMENSIONS_KEY,
               answersCatalogConfoguration.getMartMaxDimensions().getValue());
      getAnswersCatalogDBConfiguration().saveConfigurationProperty(
               IAnswersCatalogConfigurationService.MAX_MEASURES_KEY,
               answersCatalogConfoguration.getMartMaxMeasures().getValue());

      getAnswersCatalogDBConfiguration().saveConfigurationProperty(
               IAnswersCatalogConfigurationService.MAX_SPACE_PERCENTAGE_KEY,
               answersCatalogConfoguration.getOptCubeMaxSpace().getValue());
      getAnswersCatalogDBConfiguration().saveConfigurationProperty(
               IAnswersCatalogConfigurationService.MIN_USAGE_PERCENTAGE_KEY,
               answersCatalogConfoguration.getOptCubeMinUsage().getValue());

      getAnswersCatalogDBConfiguration().saveConfigurationProperty(
               IAnswersCatalogConfigurationService.APPLY_CONSTRAINTS_IN_OPTIMAL_DSET_ALGORITHM_KEY,
               answersCatalogConfoguration.getOptApplyConstraints().getValue());
      getAnswersCatalogDBConfiguration().saveConfigurationProperty(
               IAnswersCatalogConfigurationService.DEDUCE_SPACE_AT_RUNTIME_IN_OPTIMAL_DSET_ALGORITHM_KEY,
               answersCatalogConfoguration.getOptSpaceAtRuntime().getValue());

      getAnswersCatalogDBConfiguration().saveConfigurationProperty(
               IAnswersCatalogConfigurationService.CONFIGURED_DIMENSION_LOOKUP_VALUE_COLUMN_SIZE,
               answersCatalogConfoguration.getOptLookupValueColumnLengthAtParentAsset().getValue());
      getAnswersCatalogDBConfiguration().saveConfigurationProperty(
               IAnswersCatalogConfigurationService.CONFIGURED_NUMBER_OF_MEASURES_IN_PARENT_ASSET,
               answersCatalogConfoguration.getOptNumberOfParentAssetMeasures().getValue());
      getAnswersCatalogDBConfiguration().saveConfigurationProperty(
               IAnswersCatalogConfigurationService.CONFIGURED_PARENT_ASSET_SPACE_FOR_OPTIMAL_DSET_ALGORITHM_KEY,
               answersCatalogConfoguration.getOptParentAssetSpace().getValue());
   }

   @Override
   public void saveCommonConfigurations (CommonConfigurationViewInfo commonConfoguration) {
      getDriverDBConfiguration().saveConfigurationProperty(IDriverConfigurationService.DISPLAY_QUERY_STRING_KEY,
               commonConfoguration.getDisplayQueryString().getValue());
      getCoreDBConfiguration().saveConfigurationProperty(ICoreConfigurationService.SYSTEM_LEVEL_DEFAULT_STAT_KEY,
               commonConfoguration.getSystemLevelDefaultStat().getValue());
   }

   @Override
   public void saveReportAggregationConfigurations (
            ReportAggregationConfigurationViewInfo reportAggregationConfoguration) {
      getAggregationDBConfiguration().saveConfigurationProperty(
               IAggregationConfigurationService.DATA_BROWSER_MAX_RECORDS_KEY,
               reportAggregationConfoguration.getDataBrowserMaxRecords().getValue());
      getAggregationDBConfiguration().saveConfigurationProperty(
               IAggregationConfigurationService.DATA_BROWSER_MIN_RECORDS_KEY,
               reportAggregationConfoguration.getDataBrowserMinRecords().getValue());
      getAggregationDBConfiguration().saveConfigurationProperty(
               IAggregationConfigurationService.DETAIL_REPORTS_SELECTION_THRESHOLD_KEY,
               reportAggregationConfoguration.getDetailReportSelectionThreshold().getValue());
      getAggregationDBConfiguration().saveConfigurationProperty(
               IAggregationConfigurationService.DYNAMIC_RANGES_BAND_COUNT_KEY,
               reportAggregationConfoguration.getDynamicRangeBandCount().getValue());
      getAggregationDBConfiguration().saveConfigurationProperty(
               IAggregationConfigurationService.ENABLE_DETAIL_REPORTS_KEY,
               reportAggregationConfoguration.getEnableDetailReports().getValue());
      getAggregationDBConfiguration().saveConfigurationProperty(IAggregationConfigurationService.ENABLE_DYNAMIC_RANGES,
               reportAggregationConfoguration.getEnableDynamicRanges().getValue());
      getAggregationDBConfiguration().saveConfigurationProperty(
               IAggregationConfigurationService.REPORT_TITLE_MAX_ALLOWED_LENGTH_KEY,
               reportAggregationConfoguration.getReportTitleLength().getValue());
      getAggregationDBConfiguration().saveConfigurationProperty(IAggregationConfigurationService.SKIP_UNIVARIANTS,
               reportAggregationConfoguration.getSkipUniVariants().getValue());
   }

   @Override
   public void saveReportPresentationConfigurations (
            ReportPresentationConfigurationViewInfo reportPresentationConfoguration) {
      getRpDBConfiguration().saveConfigurationProperty(IPresentationConfigurationService.AXIS_LABEL_MAX_LENGTH,
               reportPresentationConfoguration.getAxisLabelMaxLength().getValue());
   }

   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   public IDriverConfigurationService getDriverConfigurationService () {
      return driverConfigurationService;
   }

   public void setDriverConfigurationService (IDriverConfigurationService driverConfigurationService) {
      this.driverConfigurationService = driverConfigurationService;
   }

   public IAggregationConfigurationService getAggregationConfigurationService () {
      return aggregationConfigurationService;
   }

   public void setAggregationConfigurationService (IAggregationConfigurationService aggregationConfigurationService) {
      this.aggregationConfigurationService = aggregationConfigurationService;
   }

   public IPresentationConfigurationService getPresentationConfigurationService () {
      return presentationConfigurationService;
   }

   public void setPresentationConfigurationService (IPresentationConfigurationService presentationConfigurationService) {
      this.presentationConfigurationService = presentationConfigurationService;
   }

   public IAnswersCatalogConfigurationService getAnswersCatalogConfigurationService () {
      return answersCatalogConfigurationService;
   }

   public void setAnswersCatalogConfigurationService (
            IAnswersCatalogConfigurationService answersCatalogConfigurationService) {
      this.answersCatalogConfigurationService = answersCatalogConfigurationService;
   }

   public IConfiguration getCoreDBConfiguration () {
      return coreDBConfiguration;
   }

   public void setCoreDBConfiguration (IConfiguration coreDBConfiguration) {
      this.coreDBConfiguration = coreDBConfiguration;
   }

   public IConfiguration getDriverDBConfiguration () {
      return driverDBConfiguration;
   }

   public void setDriverDBConfiguration (IConfiguration driverDBConfiguration) {
      this.driverDBConfiguration = driverDBConfiguration;
   }

   public IConfiguration getAnswersCatalogDBConfiguration () {
      return answersCatalogDBConfiguration;
   }

   public void setAnswersCatalogDBConfiguration (IConfiguration answersCatalogDBConfiguration) {
      this.answersCatalogDBConfiguration = answersCatalogDBConfiguration;
   }

   public IConfiguration getAggregationDBConfiguration () {
      return aggregationDBConfiguration;
   }

   public void setAggregationDBConfiguration (IConfiguration aggregationDBConfiguration) {
      this.aggregationDBConfiguration = aggregationDBConfiguration;
   }

   public IConfiguration getRpDBConfiguration () {
      return rpDBConfiguration;
   }

   public void setRpDBConfiguration (IConfiguration rpDBConfiguration) {
      this.rpDBConfiguration = rpDBConfiguration;
   }

}
