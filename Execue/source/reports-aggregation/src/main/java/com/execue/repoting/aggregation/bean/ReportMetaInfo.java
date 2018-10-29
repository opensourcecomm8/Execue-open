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


package com.execue.repoting.aggregation.bean;

import java.util.List;

import com.execue.core.common.bean.governor.AssetQuery;
import com.execue.core.common.type.AggregateQueryType;

/**
 * This bean is the aggregated structure of the columns participating in the metrics section of the business query and
 * also has a reference to the asset query
 * 
 * @author kaliki
 * @since 4.0
 */

public class ReportMetaInfo {

   private AssetQuery                    assetQuery;
   private List<ReportColumnInfo>        reportColumns;
   private List<ReportMetaHierarchyInfo> reportMetaHierarchyInfo;
   private AggregateQueryType            summaryPathType;
   private boolean                       uniqueCounts;
   private boolean                       minMaxValues;
   private long                          totalCount;
   private boolean                       univariants;
   private boolean                       generateBusinessSummary = true;
   private boolean                       generateDetailReport    = true;
   private boolean                       generateHierarchyReport;
   private boolean                       generateScatterReport;
   private boolean                       generateOnlyDataBrowser;
   private boolean                       generateMultipleDetailReports;
   private boolean                       skipUnivariants;
   private String                        finalQuery;
   private boolean                       profilePresent;

   public boolean isProfilePresent () {
      return profilePresent;
   }

   public void setProfilePresent (boolean profilePresent) {
      this.profilePresent = profilePresent;
   }

   public boolean isUnivariants () {
      return univariants;
   }

   public void setUnivariants (boolean univariants) {
      this.univariants = univariants;
   }

   public ReportMetaInfo (AssetQuery assetQuery) {
      this.assetQuery = assetQuery;
   }

   public List<ReportColumnInfo> getReportColumns () {
      return reportColumns;
   }

   public void setReportColumns (List<ReportColumnInfo> reportColumns) {
      this.reportColumns = reportColumns;
   }

   public AssetQuery getAssetQuery () {
      return assetQuery;
   }

   public void setAssetQuery (AssetQuery assetQuery) {
      this.assetQuery = assetQuery;
   }

   public boolean hasUniqueCounts () {
      return uniqueCounts;
   }

   public void setUniqueCounts (boolean hasUniqueCounts) {
      this.uniqueCounts = hasUniqueCounts;
   }

   public boolean hasMinMaxValues () {
      return minMaxValues;
   }

   public void setMinMaxValues (boolean minMaxValues) {
      this.minMaxValues = minMaxValues;
   }

   public long getTotalCount () {
      return totalCount;
   }

   public void setTotalCount (long totalCount) {
      this.totalCount = totalCount;
   }

   public boolean isGenerateDetailReport () {
      return generateDetailReport;
   }

   public void setGenerateDetailReport (boolean generateDetailedReport) {
      this.generateDetailReport = generateDetailedReport;
   }

   public boolean isGenerateHierarchyReport () {
      return generateHierarchyReport;
   }

   public void setGenerateHierarchyReport (boolean generateHierarchyReport) {
      this.generateHierarchyReport = generateHierarchyReport;
   }

   public boolean isGenerateScatterReport () {
      return generateScatterReport;
   }

   public void setGenerateScatterReport (boolean generateScatterReport) {
      this.generateScatterReport = generateScatterReport;
   }

   public String getFinalQuery () {
      return finalQuery;
   }

   public void setFinalQuery (String finalQuery) {
      this.finalQuery = finalQuery;
   }

   public boolean isGenerateOnlyDataBrowser () {
      return generateOnlyDataBrowser;
   }

   public void setGenerateOnlyDataBrowser (boolean generateOnlyDataBrowser) {
      this.generateOnlyDataBrowser = generateOnlyDataBrowser;
   }

   public boolean isGenerateMultipleDetailReports () {
      return generateMultipleDetailReports;
   }

   public void setGenerateMultipleDetailReports (boolean generateMultipleDetailReports) {
      this.generateMultipleDetailReports = generateMultipleDetailReports;
   }

   public boolean isGenerateBusinessSummary () {
      return generateBusinessSummary;
   }

   public void setGenerateBusinessSummary (boolean generateBusinessSummary) {
      this.generateBusinessSummary = generateBusinessSummary;
   }

   /**
    * @return the reportMetaHierarchyInfo
    */
   public List<ReportMetaHierarchyInfo> getReportMetaHierarchyInfo () {
      return reportMetaHierarchyInfo;
   }

   /**
    * @param reportMetaHierarchyInfo the reportMetaHierarchyInfo to set
    */
   public void setReportMetaHierarchyInfo (List<ReportMetaHierarchyInfo> reportMetaHierarchyInfo) {
      this.reportMetaHierarchyInfo = reportMetaHierarchyInfo;
   }

   /**
    * @return the summaryPathType
    */
   public AggregateQueryType getSummaryPathType () {
      return summaryPathType;
   }

   /**
    * @param summaryPathType the summaryPathType to set
    */
   public void setSummaryPathType (AggregateQueryType summaryPathType) {
      this.summaryPathType = summaryPathType;
   }
}