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


package com.execue.repoting.aggregation.configuration.impl;

import java.util.List;

import com.execue.core.configuration.IConfiguration;
import com.execue.repoting.aggregation.configuration.IAggregationConfigurationService;

/**
 * This class define the configuration constants and methods that will be further used by aggregation services.
 * 
 * @author Jitendra
 * @version 1.0
 * @since 13/10/11
 */
public class AggregationConfigurationServiceImpl implements IAggregationConfigurationService {

   private String         COUNT_STAT_KEY                  = "aggregation.static-values.count-stat";

   private String         DATA_POINTS_KEY                 = "aggregation.reportselection.dataPoints";
   private String         NUMBER_OF_EFFECTIVE_GROUPS_KEY  = "aggregation.reportselection.numberOfEffectiveGroups";
   private String         NUMBER_OF_EFFECTIVE_RECORDS_KEY = "aggregation.reportselection.numberOfEffectiveRecords";
   private String         NUMBER_OF_GROUPS_KEY            = "aggregation.reportselection.numberOfGroups";
   private String         NUMBER_OF_ID_COLUMNS_KEY        = "aggregation.reportselection.numberOfIdColumns";
   private String         NUMBER_OF_MEASURES_KEY          = "aggregation.reportselection.numberOfMeasures";
   private String         NUMBER_OF_RECORDS_KEY           = "aggregation.reportselection.numberOfRecords";
   private String         REPORT_SELECTION_RULE_NAME_KEY  = "aggregation.reportselection.rule-name";

   private String         REPORT_TITLE_APPEND_TEXT        = "aggregation.static-values.report-title-append-text";
   private String         UNIQUE_COUNT_PERCENT            = "aggregation.static-values.unique-count-percent";

   /**
    * This key will retrieve the different data types used for report column
    */
   private String         MEASURE_DATA_TYPES              = "aggregation.reportcolumn.measure-data-types";
   private String         DIMENSION_DATA_TYPES            = "aggregation.reportcolumn.dimension-data-types";
   private String         DEFAULT_COLUMN_TYPE             = "aggregation.reportcolumn.default-column-type";

   private IConfiguration aggregationConfiguration;
   private IConfiguration aggregationDBConfiguration;

   @Override
   public boolean enableDetailReports () {
      return getAggregationDBConfiguration().getBoolean(ENABLE_DETAIL_REPORTS_KEY);
   }

   @Override
   public boolean enableDynamicRanges () {
      return getAggregationDBConfiguration().getBoolean(ENABLE_DYNAMIC_RANGES);
   }

   @Override
   public String getCountStat () {
      return getAggregationConfiguration().getProperty(COUNT_STAT_KEY);
   }

   @Override
   public Integer getDataBrowserMaxRecords () {
      return getAggregationDBConfiguration().getInt(DATA_BROWSER_MAX_RECORDS_KEY);
   }

   @Override
   public Integer getDataPoints () {
      return getAggregationConfiguration().getInt(DATA_POINTS_KEY);
   }

   @Override
   public String getDefaultColumnType () {
      return getAggregationConfiguration().getProperty(DEFAULT_COLUMN_TYPE);
   }

   @Override
   public Long getDetailReportSelectionThreshold () {
      return getAggregationDBConfiguration().getLong(DETAIL_REPORTS_SELECTION_THRESHOLD_KEY);
   }

   @Override
   public List<String> getDimensionDataTypes () {
      return getAggregationConfiguration().getList(DIMENSION_DATA_TYPES);
   }

   @Override
   public Integer getDynamicRangesBandCount () {
      return getAggregationDBConfiguration().getInt(DYNAMIC_RANGES_BAND_COUNT_KEY);
   }

   @Override
   public List<String> getMeasureDataTypes () {
      return getAggregationConfiguration().getList(MEASURE_DATA_TYPES);
   }

   @Override
   public Integer getNumberOfEffectiveGroups () {
      return getAggregationConfiguration().getInt(NUMBER_OF_EFFECTIVE_GROUPS_KEY);
   }

   @Override
   public Integer getNumberOfEffectiveRecords () {
      return getAggregationConfiguration().getInt(NUMBER_OF_EFFECTIVE_RECORDS_KEY);
   }

   @Override
   public Integer getNumberOfGroups () {
      return getAggregationConfiguration().getInt(NUMBER_OF_GROUPS_KEY);
   }

   @Override
   public Integer getNumberOfIdColumns () {
      return getAggregationConfiguration().getInt(NUMBER_OF_ID_COLUMNS_KEY);
   }

   @Override
   public Integer getNumberOfMeasures () {
      return getAggregationConfiguration().getInt(NUMBER_OF_MEASURES_KEY);
   }

   @Override
   public Integer getNumberOfRecords () {
      return getAggregationConfiguration().getInt(NUMBER_OF_RECORDS_KEY);
   }

   @Override
   public String getReportSelectionRuleName () {
      return getAggregationConfiguration().getProperty(REPORT_SELECTION_RULE_NAME_KEY);
   }

   @Override
   public Integer getReportTitleMaxAllowedLength () {
      return getAggregationDBConfiguration().getInt(REPORT_TITLE_MAX_ALLOWED_LENGTH_KEY);
   }

   @Override
   public String getReportTitleTextToAppend () {
      return getAggregationConfiguration().getProperty(REPORT_TITLE_APPEND_TEXT);
   }

   @Override
   public boolean skipUnivariants () {
      return getAggregationDBConfiguration().getBoolean(SKIP_UNIVARIANTS);
   }

   @Override
   public Integer getDataBrowserMinRecords () {
      return getAggregationDBConfiguration().getInt(DATA_BROWSER_MIN_RECORDS_KEY);
   }

   @Override
   public Integer getUniqueCountPercent () {
      return getAggregationConfiguration().getInt(UNIQUE_COUNT_PERCENT);
   }

   /**
    * @return the aggregationConfiguration
    */
   public IConfiguration getAggregationConfiguration () {
      return aggregationConfiguration;
   }

   /**
    * @param aggregationConfiguration
    *           the aggregationConfiguration to set
    */
   public void setAggregationConfiguration (IConfiguration aggregationConfiguration) {
      this.aggregationConfiguration = aggregationConfiguration;
   }

   public IConfiguration getAggregationDBConfiguration () {
      return aggregationDBConfiguration;
   }

   public void setAggregationDBConfiguration (IConfiguration aggregationDBConfiguration) {
      this.aggregationDBConfiguration = aggregationDBConfiguration;
   }

}
