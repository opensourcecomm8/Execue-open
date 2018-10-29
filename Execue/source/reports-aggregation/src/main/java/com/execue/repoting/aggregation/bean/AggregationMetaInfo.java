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

import com.execue.core.common.type.AggregateQueryType;

/**
 * @author John Mallavalli
 */
public class AggregationMetaInfo {

   // to populate ReportMetaInfo
   private boolean                      uniqueCounts;
   private boolean                      minMaxValues;
   private long                         totalCount;
   private boolean                      univariants;
   private boolean                      generateDetailedReport;
   private List<AggregationColumnInfo>  reportColumns;
   private AggregationMetaHierarchyInfo aggregationMetaHierarchyInfo;
   private AggregationStructuredQuery   structuredQuery;
   private boolean                      dataBrowser;
   private long                         userQueryId;
   private AggregateQueryType           summaryPathType;

   public long getUserQueryId () {
      return userQueryId;
   }

   public void setUserQueryId (long userQueryId) {
      this.userQueryId = userQueryId;
   }

   public boolean isUniqueCounts () {
      return uniqueCounts;
   }

   public void setUniqueCounts (boolean uniqueCounts) {
      this.uniqueCounts = uniqueCounts;
   }

   public boolean isMinMaxValues () {
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

   public boolean isUnivariants () {
      return univariants;
   }

   public void setUnivariants (boolean univariants) {
      this.univariants = univariants;
   }

   public boolean isGenerateDetailedReport () {
      return generateDetailedReport;
   }

   public void setGenerateDetailedReport (boolean generateDetailedReport) {
      this.generateDetailedReport = generateDetailedReport;
   }

   public List<AggregationColumnInfo> getReportColumns () {
      return reportColumns;
   }

   public void setReportColumns (List<AggregationColumnInfo> reportColumns) {
      this.reportColumns = reportColumns;
   }

   public AggregationStructuredQuery getStructuredQuery () {
      return structuredQuery;
   }

   public void setStructuredQuery (AggregationStructuredQuery structuredQuery) {
      this.structuredQuery = structuredQuery;
   }

   public boolean isDataBrowser () {
      return dataBrowser;
   }

   public void setDataBrowser (boolean dataBrowser) {
      this.dataBrowser = dataBrowser;
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

   /**
    * @return the aggregationMetaHierarchyInfo
    */
   public AggregationMetaHierarchyInfo getAggregationMetaHierarchyInfo () {
      return aggregationMetaHierarchyInfo;
   }

   /**
    * @param aggregationMetaHierarchyInfo the aggregationMetaHierarchyInfo to set
    */
   public void setAggregationMetaHierarchyInfo (AggregationMetaHierarchyInfo aggregationMetaHierarchyInfo) {
      this.aggregationMetaHierarchyInfo = aggregationMetaHierarchyInfo;
   }
}
