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


package com.execue.web.sysconfig.bean;

import java.io.Serializable;
import java.util.List;

public class ReportAggregationConfigurationViewInfo implements Serializable {

   private List<ConfigurationProperty> readOnlyConfigurations;

   private ConfigurationProperty       enableDynamicRanges;
   private ConfigurationProperty       skipUniVariants;
   private ConfigurationProperty       enableDetailReports;
   private ConfigurationProperty       dynamicRangeBandCount;
   private ConfigurationProperty       detailReportSelectionThreshold;
   private ConfigurationProperty       dataBrowserMinRecords;
   private ConfigurationProperty       dataBrowserMaxRecords;
   private ConfigurationProperty       reportTitleLength;

   public List<ConfigurationProperty> getReadOnlyConfigurations () {
      return readOnlyConfigurations;
   }

   public void setReadOnlyConfigurations (List<ConfigurationProperty> readOnlyConfigurations) {
      this.readOnlyConfigurations = readOnlyConfigurations;
   }

   public ConfigurationProperty getEnableDynamicRanges () {
      return enableDynamicRanges;
   }

   public void setEnableDynamicRanges (ConfigurationProperty enableDynamicRanges) {
      this.enableDynamicRanges = enableDynamicRanges;
   }

   public ConfigurationProperty getSkipUniVariants () {
      return skipUniVariants;
   }

   public void setSkipUniVariants (ConfigurationProperty skipUniVariants) {
      this.skipUniVariants = skipUniVariants;
   }

   public ConfigurationProperty getEnableDetailReports () {
      return enableDetailReports;
   }

   public void setEnableDetailReports (ConfigurationProperty enableDetailReports) {
      this.enableDetailReports = enableDetailReports;
   }

   public ConfigurationProperty getDynamicRangeBandCount () {
      return dynamicRangeBandCount;
   }

   public void setDynamicRangeBandCount (ConfigurationProperty dynamicRangeBandCount) {
      this.dynamicRangeBandCount = dynamicRangeBandCount;
   }

   public ConfigurationProperty getDetailReportSelectionThreshold () {
      return detailReportSelectionThreshold;
   }

   public void setDetailReportSelectionThreshold (ConfigurationProperty detailReportSelectionThreshold) {
      this.detailReportSelectionThreshold = detailReportSelectionThreshold;
   }

   public ConfigurationProperty getDataBrowserMinRecords () {
      return dataBrowserMinRecords;
   }

   public void setDataBrowserMinRecords (ConfigurationProperty dataBrowserMinRecords) {
      this.dataBrowserMinRecords = dataBrowserMinRecords;
   }

   public ConfigurationProperty getDataBrowserMaxRecords () {
      return dataBrowserMaxRecords;
   }

   public void setDataBrowserMaxRecords (ConfigurationProperty dataBrowserMaxRecords) {
      this.dataBrowserMaxRecords = dataBrowserMaxRecords;
   }

   public ConfigurationProperty getReportTitleLength () {
      return reportTitleLength;
   }

   public void setReportTitleLength (ConfigurationProperty reportTitleLength) {
      this.reportTitleLength = reportTitleLength;
   }

}
