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


package com.execue.repoting.aggregation.configuration;

import java.util.List;

public interface IAggregationConfigurationService {

   public String ENABLE_DYNAMIC_RANGES                  = "aggregation.static-values.enable-dynamic-ranges";
   public String SKIP_UNIVARIANTS                       = "aggregation.static-values.skip-univariants";
   public String DATA_BROWSER_MAX_RECORDS_KEY           = "aggregation.static-values.data-browser-max-records";
   public String DATA_BROWSER_MIN_RECORDS_KEY           = "aggregation.static-values.data-browser-min-records";
   public String DETAIL_REPORTS_SELECTION_THRESHOLD_KEY = "aggregation.static-values.detail-reports-selection-threshold";
   public String ENABLE_DETAIL_REPORTS_KEY              = "aggregation.static-values.enable-detail-reports";
   public String REPORT_TITLE_MAX_ALLOWED_LENGTH_KEY    = "aggregation.static-values.report-title-length";
   // This key will retrieve the number of bands for dynamic deduction of ranges
   public String DYNAMIC_RANGES_BAND_COUNT_KEY          = "aggregation.static-values.dynamic-ranges-band-count";

   public String getCountStat ();

   public Integer getDynamicRangesBandCount ();

   public boolean enableDynamicRanges ();

   public Integer getDataBrowserMaxRecords ();

   public Integer getDataBrowserMinRecords ();

   public Integer getReportTitleMaxAllowedLength ();

   public String getReportTitleTextToAppend ();

   public boolean enableDetailReports ();

   public Long getDetailReportSelectionThreshold ();

   public List<String> getDimensionDataTypes ();

   public List<String> getMeasureDataTypes ();

   public String getDefaultColumnType ();

   public boolean skipUnivariants ();

   public String getReportSelectionRuleName ();

   public Integer getDataPoints ();

   public Integer getNumberOfEffectiveGroups ();

   public Integer getNumberOfEffectiveRecords ();

   public Integer getNumberOfGroups ();

   public Integer getNumberOfIdColumns ();

   public Integer getNumberOfMeasures ();

   public Integer getNumberOfRecords ();

   public Integer getUniqueCountPercent ();

}
