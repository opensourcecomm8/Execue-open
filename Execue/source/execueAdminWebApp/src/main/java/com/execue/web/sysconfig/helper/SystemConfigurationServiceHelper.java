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


package com.execue.web.sysconfig.helper;

import java.util.HashMap;
import java.util.Map;

import com.execue.ac.configuration.IAnswersCatalogConfigurationService;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.StatType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.driver.configuration.IDriverConfigurationService;
import com.execue.report.configuration.service.IPresentationConfigurationService;
import com.execue.repoting.aggregation.configuration.IAggregationConfigurationService;
import com.execue.web.sysconfig.bean.ConfigurationProperty;

public class SystemConfigurationServiceHelper {

   private static Map<String, ConfigurationProperty> configurationLabelNameMapping                                    = new HashMap<String, ConfigurationProperty>();

   public static final String                        COMMON_SHOW_REVIEW_QUERY_IDENTITY                                = "C01";
   public static final String                        COMMON_SYSTEM_LEVEL_STAT_IDENTITY                                = "C02";

   public static final String                        ANSWERS_CATALOG_CUBE_BE_BUILT_AT_SOURCE_IDENTITY                 = "AC01";
   public static final String                        ANSWERS_CATALOG_MART_BE_BUILT_AT_SOURCE_IDENTITY                 = "AC02";

   public static final String                        ANSWERS_CATALOG_SAMPLING_ERROR_RATE_IDENTITY                     = "AC03";
   public static final String                        ANSWERS_CATALOG_SAMPLING_CONFIDENCE_LEVEL_IDENTITY               = "AC04";
   public static final String                        ANSWERS_CATALOG_SAMPLING_MIN_SAMPLE_IDENTITY                     = "AC05";
   public static final String                        ANSWERS_CATALOG_SAMPLING_MAX_SAMPLE_IDENTITY                     = "AC06";

   public static final String                        ANSWERS_CATALOG_MART_MAX_DIMS_IDENTITY                           = "AC07";
   public static final String                        ANSWERS_CATALOG_MART_MAX_MEASURES_IDENTITY                       = "AC08";
   public static final String                        ANSWERS_CATALOG_MART_USE_BASIC_SAMPLING_ALGO_IDENTITY            = "AC09";

   public static final String                        ANSWERS_CATALOG_OPTD_CUBE_MIN_USAGE_IDENTITY                     = "AC10";
   public static final String                        ANSWERS_CATALOG_OPTD_CUBE_MAX_SPACE_IDENTITY                     = "AC11";
   public static final String                        ANSWERS_CATALOG_OPTD_APPLY_CONSTRAINTS_IDENTITY                  = "AC12";
   public static final String                        ANSWERS_CATALOG_OPTD_SPACE_AT_RUNTIME_IDENTITY                   = "AC13";
   public static final String                        ANSWERS_CATALOG_OPTD_PARENT_ASSET_SPACE_IDENTITY                 = "AC14";
   public static final String                        ANSWERS_CATALOG_OPTD_NUMBER_OF_MEASURES_IN_PARENT_ASSET_IDENTITY = "AC15";
   public static final String                        ANSWERS_CATALOG_OPTD_LOOKUP_COLUMN_LENGTH_IDENTITY               = "AC16";

   public static final String                        REPORT_AGGREGATION_ENABLE_DYNAMIC_RANGES_IDENTITY                = "RA01";
   public static final String                        REPORT_AGGREGATION_SKIP_UNI_VARIANTS_IDENTITY                    = "RA02";
   public static final String                        REPORT_AGGREGATION_DATA_BROWSER_MAX_RECORDS_IDENTITY             = "RA03";
   public static final String                        REPORT_AGGREGATION_DATA_BROWSER_MIN_RECORDS_IDENTITY             = "RA04";
   public static final String                        REPORT_AGGREGATION_DETAIL_REPORT_SELECTION_THRESHOLD_IDENTITY    = "RA05";
   public static final String                        REPORT_AGGREGATION_ENABLE_DETAIL_REPORTS_IDENTITY                = "RA06";
   public static final String                        REPORT_AGGREGATION_REPORT_TITLE_LENGTH_IDENTITY                  = "RA07";
   public static final String                        REPORT_AGGREGATION_DYNAMIC_RANGES_BAND_COUNT_IDENTITY            = "RA08";

   public static final String                        REPORT_PRESENTATION_CHART_AXIS_LABEL_MAXIMUM_LENGTH_IDENTITY     = "RP01";

   static {
      // Common configuration properties
      configurationLabelNameMapping.put(COMMON_SHOW_REVIEW_QUERY_IDENTITY, prepareConfigurationProperty(
               COMMON_SHOW_REVIEW_QUERY_IDENTITY, "Show Review Query",
               IDriverConfigurationService.DISPLAY_QUERY_STRING_KEY));
      configurationLabelNameMapping.put(COMMON_SYSTEM_LEVEL_STAT_IDENTITY, prepareConfigurationProperty(
               COMMON_SYSTEM_LEVEL_STAT_IDENTITY, "Default Aggregation Function",
               ICoreConfigurationService.SYSTEM_LEVEL_DEFAULT_STAT_KEY));

      // Answers Catalog configuration properties
      configurationLabelNameMapping.put(ANSWERS_CATALOG_CUBE_BE_BUILT_AT_SOURCE_IDENTITY, prepareConfigurationProperty(
               ANSWERS_CATALOG_CUBE_BE_BUILT_AT_SOURCE_IDENTITY, "Create Cube at Source Schema",
               IAnswersCatalogConfigurationService.CUBE_TARGET_DATASOURCE_SAME_AS_SOURCE_DATASOURCE_KEY));
      configurationLabelNameMapping.put(ANSWERS_CATALOG_MART_BE_BUILT_AT_SOURCE_IDENTITY, prepareConfigurationProperty(
               ANSWERS_CATALOG_MART_BE_BUILT_AT_SOURCE_IDENTITY, "Create Mart at Source Schema",
               IAnswersCatalogConfigurationService.MART_TARGET_DATASOURCE_SAME_AS_SOURCE_DATASOURCE_KEY));

      
      configurationLabelNameMapping.put(ANSWERS_CATALOG_SAMPLING_CONFIDENCE_LEVEL_IDENTITY, prepareConfigurationProperty(
               ANSWERS_CATALOG_SAMPLING_CONFIDENCE_LEVEL_IDENTITY, "Mart Confidence Level",
               IAnswersCatalogConfigurationService.SAMPLING_ALGO_CONFIDENCE_LEVEL_PERCENTAGE_KEY));
      configurationLabelNameMapping.put(ANSWERS_CATALOG_SAMPLING_ERROR_RATE_IDENTITY, prepareConfigurationProperty(
               ANSWERS_CATALOG_SAMPLING_ERROR_RATE_IDENTITY, "Mart Error Rate",
               IAnswersCatalogConfigurationService.SAMPLING_ALGO_ERROR_RATE_PERCENTAGE_KEY));
      configurationLabelNameMapping.put(ANSWERS_CATALOG_SAMPLING_MIN_SAMPLE_IDENTITY, prepareConfigurationProperty(
               ANSWERS_CATALOG_SAMPLING_MIN_SAMPLE_IDENTITY, "Mart Minimum Population",
               IAnswersCatalogConfigurationService.SAMPLING_ALGO_MIN_SAMPLE_PERCENTAGE_OF_POPULATION_KEY));
      configurationLabelNameMapping.put(ANSWERS_CATALOG_SAMPLING_MAX_SAMPLE_IDENTITY, prepareConfigurationProperty(
               ANSWERS_CATALOG_SAMPLING_MAX_SAMPLE_IDENTITY, "Mart Maximum Population",
               IAnswersCatalogConfigurationService.SAMPLING_ALGO_MAX_SAMPLE_PERCENTAGE_OF_POPULATION_ALLOWED_KEY));
      
      configurationLabelNameMapping.put(ANSWERS_CATALOG_MART_MAX_DIMS_IDENTITY, prepareConfigurationProperty(
               ANSWERS_CATALOG_MART_MAX_DIMS_IDENTITY, "Maximum Lookups for Mart",
               IAnswersCatalogConfigurationService.MAX_DIMENSIONS_KEY));
      configurationLabelNameMapping.put(ANSWERS_CATALOG_MART_MAX_MEASURES_IDENTITY, prepareConfigurationProperty(
               ANSWERS_CATALOG_MART_MAX_MEASURES_IDENTITY, "Maximum Measures for Mart",
               IAnswersCatalogConfigurationService.MAX_MEASURES_KEY));
      configurationLabelNameMapping.put(ANSWERS_CATALOG_MART_USE_BASIC_SAMPLING_ALGO_IDENTITY,
               prepareConfigurationProperty(ANSWERS_CATALOG_MART_USE_BASIC_SAMPLING_ALGO_IDENTITY,
                        "Use Basic Algorithm for Mart",
                        IAnswersCatalogConfigurationService.MART_USE_BASIC_SAMPLING_ALGO_KEY));

      configurationLabelNameMapping.put(ANSWERS_CATALOG_OPTD_APPLY_CONSTRAINTS_IDENTITY, prepareConfigurationProperty(
               ANSWERS_CATALOG_OPTD_APPLY_CONSTRAINTS_IDENTITY, "Optimal D-Set Algorithm Apply Constraints",
               IAnswersCatalogConfigurationService.APPLY_CONSTRAINTS_IN_OPTIMAL_DSET_ALGORITHM_KEY));
      configurationLabelNameMapping.put(ANSWERS_CATALOG_OPTD_CUBE_MAX_SPACE_IDENTITY, prepareConfigurationProperty(
               ANSWERS_CATALOG_OPTD_CUBE_MAX_SPACE_IDENTITY, "Optimal D-Set Algorithm Maximum Space Constraint",
               IAnswersCatalogConfigurationService.MAX_SPACE_PERCENTAGE_KEY));
      configurationLabelNameMapping.put(ANSWERS_CATALOG_OPTD_CUBE_MIN_USAGE_IDENTITY, prepareConfigurationProperty(
               ANSWERS_CATALOG_OPTD_CUBE_MIN_USAGE_IDENTITY, "Optimal D-Set Algorithm Minimum Usage",
               IAnswersCatalogConfigurationService.MIN_USAGE_PERCENTAGE_KEY));
      
      configurationLabelNameMapping.put(ANSWERS_CATALOG_OPTD_SPACE_AT_RUNTIME_IDENTITY, prepareConfigurationProperty(
               ANSWERS_CATALOG_OPTD_SPACE_AT_RUNTIME_IDENTITY, "Optimal D-Set Algorithm Calculate Space at Runtime",
               IAnswersCatalogConfigurationService.DEDUCE_SPACE_AT_RUNTIME_IN_OPTIMAL_DSET_ALGORITHM_KEY));
      configurationLabelNameMapping.put(ANSWERS_CATALOG_OPTD_PARENT_ASSET_SPACE_IDENTITY, prepareConfigurationProperty(
               ANSWERS_CATALOG_OPTD_PARENT_ASSET_SPACE_IDENTITY, "Optimal D-Set Algorithm Parent Asset Space",
               IAnswersCatalogConfigurationService.CONFIGURED_PARENT_ASSET_SPACE_FOR_OPTIMAL_DSET_ALGORITHM_KEY));
      configurationLabelNameMapping.put(ANSWERS_CATALOG_OPTD_NUMBER_OF_MEASURES_IN_PARENT_ASSET_IDENTITY, prepareConfigurationProperty(
               ANSWERS_CATALOG_OPTD_NUMBER_OF_MEASURES_IN_PARENT_ASSET_IDENTITY, "Optimal D-Set Algorithm Number of Measures at Source",
               IAnswersCatalogConfigurationService.CONFIGURED_NUMBER_OF_MEASURES_IN_PARENT_ASSET));
      configurationLabelNameMapping.put(ANSWERS_CATALOG_OPTD_LOOKUP_COLUMN_LENGTH_IDENTITY, prepareConfigurationProperty(
               ANSWERS_CATALOG_OPTD_LOOKUP_COLUMN_LENGTH_IDENTITY, "Optimal D-Set Algorithm Lookup Column Length at Source",
               IAnswersCatalogConfigurationService.CONFIGURED_DIMENSION_LOOKUP_VALUE_COLUMN_SIZE));

      // Report Aggregation configuration properties
      configurationLabelNameMapping.put(REPORT_AGGREGATION_ENABLE_DYNAMIC_RANGES_IDENTITY,
               prepareConfigurationProperty(REPORT_AGGREGATION_ENABLE_DYNAMIC_RANGES_IDENTITY, "Enable Dynamic Ranges",
                        IAggregationConfigurationService.ENABLE_DYNAMIC_RANGES));

      configurationLabelNameMapping.put(REPORT_AGGREGATION_DYNAMIC_RANGES_BAND_COUNT_IDENTITY,
               prepareConfigurationProperty(REPORT_AGGREGATION_DYNAMIC_RANGES_BAND_COUNT_IDENTITY,
                        "Dynamic Ranges Band Count", IAggregationConfigurationService.DYNAMIC_RANGES_BAND_COUNT_KEY));

      configurationLabelNameMapping.put(REPORT_AGGREGATION_DATA_BROWSER_MAX_RECORDS_IDENTITY,
               prepareConfigurationProperty(REPORT_AGGREGATION_DATA_BROWSER_MAX_RECORDS_IDENTITY,
                        "Maximum Records in Data Browser",
                        IAggregationConfigurationService.DATA_BROWSER_MAX_RECORDS_KEY));

      configurationLabelNameMapping.put(REPORT_AGGREGATION_DATA_BROWSER_MIN_RECORDS_IDENTITY,
               prepareConfigurationProperty(REPORT_AGGREGATION_DATA_BROWSER_MIN_RECORDS_IDENTITY,
                        "Minimum Records in Data Browser",
                        IAggregationConfigurationService.DATA_BROWSER_MIN_RECORDS_KEY));

      configurationLabelNameMapping.put(REPORT_AGGREGATION_DETAIL_REPORT_SELECTION_THRESHOLD_IDENTITY,
               prepareConfigurationProperty(REPORT_AGGREGATION_DETAIL_REPORT_SELECTION_THRESHOLD_IDENTITY,
                        "Selection Threshold for Detail Report",
                        IAggregationConfigurationService.DETAIL_REPORTS_SELECTION_THRESHOLD_KEY));

      configurationLabelNameMapping.put(REPORT_AGGREGATION_ENABLE_DETAIL_REPORTS_IDENTITY,
               prepareConfigurationProperty(REPORT_AGGREGATION_ENABLE_DETAIL_REPORTS_IDENTITY, "Enable Detail Report",
                        IAggregationConfigurationService.ENABLE_DETAIL_REPORTS_KEY));

      configurationLabelNameMapping.put(REPORT_AGGREGATION_REPORT_TITLE_LENGTH_IDENTITY, prepareConfigurationProperty(
               REPORT_AGGREGATION_REPORT_TITLE_LENGTH_IDENTITY, "Length of Report Title",
               IAggregationConfigurationService.REPORT_TITLE_MAX_ALLOWED_LENGTH_KEY));

      configurationLabelNameMapping.put(REPORT_AGGREGATION_SKIP_UNI_VARIANTS_IDENTITY, prepareConfigurationProperty(
               REPORT_AGGREGATION_SKIP_UNI_VARIANTS_IDENTITY, "Skip Univariants",
               IAggregationConfigurationService.SKIP_UNIVARIANTS));

      // Report Presentation configuration properties
      configurationLabelNameMapping.put(REPORT_PRESENTATION_CHART_AXIS_LABEL_MAXIMUM_LENGTH_IDENTITY,
               prepareConfigurationProperty(REPORT_PRESENTATION_CHART_AXIS_LABEL_MAXIMUM_LENGTH_IDENTITY,
                        "Chart Axis Maximum Length", IPresentationConfigurationService.AXIS_LABEL_MAX_LENGTH));
   }

   private static ConfigurationProperty getConfigurationProperty (String identity) {
      return configurationLabelNameMapping.get(identity);
   }

   private static ConfigurationProperty prepareConfigurationProperty (String identity, String label, String name) {
      ConfigurationProperty configurationProperty = new ConfigurationProperty();
      configurationProperty.setIdentity(identity);
      configurationProperty.setLabel(label);
      configurationProperty.setName(name);
      return configurationProperty;
   }

   public static ConfigurationProperty prepareConfigurationProperty (String identity, Object value) {
      ConfigurationProperty configurationProperty = getConfigurationProperty(identity);
      configurationProperty.setValue(String.valueOf(value));
      configurationProperty.setDisplayValue(configurationProperty.getValue());
      return configurationProperty;
   }

   public static CheckType getCorrespondingCheckType (boolean booleanValue) {
      if (booleanValue) {
         return CheckType.YES;
      }
      return CheckType.NO;
   }

   public static StatType getCorrespondingStatType (String statTypeValue) {
      return StatType.getStatType(statTypeValue);
   }
}
