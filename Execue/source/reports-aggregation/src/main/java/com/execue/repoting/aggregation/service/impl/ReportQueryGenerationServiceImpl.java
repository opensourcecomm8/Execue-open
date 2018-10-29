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


package com.execue.repoting.aggregation.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.AggregateQuery;
import com.execue.core.common.bean.BusinessStat;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.common.type.StatType;
import com.execue.repoting.aggregation.bean.AggregateQueryInfo;
import com.execue.repoting.aggregation.bean.AggregationMetaInfo;
import com.execue.repoting.aggregation.bean.ReportColumnInfo;
import com.execue.repoting.aggregation.bean.ReportMetaHierarchyInfo;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.configuration.IAggregationConfigurationService;
import com.execue.repoting.aggregation.exception.ReportQueryGenerationException;
import com.execue.repoting.aggregation.helper.ReportAggregationHelper;
import com.execue.repoting.aggregation.service.IReportQueryGenerationService;
import com.execue.swi.exception.KDXException;

/**
 * @author kaliki
 * @since 4.0 The purpose of this service is to generate an aggregated query with - all statistics applied to the
 *        measures - summarized by grouping variables Deal with different data asset types (DW,Cubes, Marts, etc). Write
 *        the most common language at this level, specialize all other logic to other service classes derived from this
 *        service.
 */
public class ReportQueryGenerationServiceImpl implements IReportQueryGenerationService {

   private ReportAggregationHelper          reportAggregationHelper;
   private IAggregationConfigurationService aggregationConfigurationService;
   private static final Logger              logger = Logger.getLogger(ReportQueryGenerationServiceImpl.class);

   /**
    * <pre>
    * Method will modify the SQ in AQ based on following 
    * 1. new Dimensions: Change the Asset column type to Dimension. Columns analyzed as dimensions should be to added to group by and order by. 
    * 2. Default Stats: If stats are applied during analysis, corresponding changes should be made to SQ. Change the Asset column type to Measure 
    * 3. Ranges: apply ranges to select and group by
    * 4. Order By Logic based on rules.
    * </pre>
    */
   @SuppressWarnings ("unused")
   public List<AggregateQueryInfo> generateQueries (ReportMetaInfo reportMetaInfo)
            throws ReportQueryGenerationException {
      List<AggregateQueryInfo> aggregateQueryInfoList = new ArrayList<AggregateQueryInfo>();

      ReportMetaInfo businessSummaryReportMetaInfo = reportMetaInfo;

      // Generate the Aggregate Query for business summary path
      if (reportMetaInfo.isGenerateBusinessSummary()) {

         businessSummaryReportMetaInfo = getReportAggregationHelper().cloneReportMetaInfo(reportMetaInfo, false);
         businessSummaryReportMetaInfo.setSummaryPathType(AggregateQueryType.BUSINESS_SUMMARY);

         AggregateQuery businessAggregateQuery = getReportAggregationHelper().getBusinessAggregateQuery(
                  businessSummaryReportMetaInfo);

         //Prepare the business aggregated query info
         AggregateQueryInfo bizAggregateQueryInfo = new AggregateQueryInfo();
         bizAggregateQueryInfo.setAggregateQuery(businessAggregateQuery);
         bizAggregateQueryInfo.setReportMetaInfo(businessSummaryReportMetaInfo);

         aggregateQueryInfoList.add(bizAggregateQueryInfo);
      }

      // Generate the Aggregate Query for detailed summary path      
      if (reportMetaInfo.isGenerateDetailReport()) {

         // NOTE: We are sending businessSummaryReportMetaInfo here as this is also used when we are getting data browser for business summary
         // so that we do not loose what has been updated during business summary processing.
         AggregateQueryInfo detailedAggregateQueryInfo = populateDetailedAggregateQueryInfo(businessSummaryReportMetaInfo);
         aggregateQueryInfoList.add(detailedAggregateQueryInfo);

      }

      // Generate the Aggregate Query for hierarchy report path      
      if (reportMetaInfo.isGenerateHierarchyReport()) {
         List<AggregateQueryInfo> hierarchyAggregateQueries = populateHierarchyAggregateQueryInfo(reportMetaInfo);
         aggregateQueryInfoList.addAll(hierarchyAggregateQueries);
      }

      // Generate the Aggregate Query(s) for scatter report path
      if (reportMetaInfo.isGenerateBusinessSummary() && reportMetaInfo.isGenerateScatterReport()) {
         ReportMetaInfo scatterReportMetaInfo = getReportAggregationHelper().cloneReportMetaInfo(reportMetaInfo, false);

         overrideStatAsAverageInMetrics(scatterReportMetaInfo);

         List<AggregateQueryInfo> scatterAggregateQueries = populateScatterAggregateQueryInfo(scatterReportMetaInfo);
         aggregateQueryInfoList.addAll(scatterAggregateQueries);
      }

      return aggregateQueryInfoList;
   }

   private void overrideStatAsAverageInMetrics (ReportMetaInfo scatterReportMetaInfo)
            throws ReportQueryGenerationException {
      List<BusinessAssetTerm> metrics = scatterReportMetaInfo.getAssetQuery().getLogicalQuery().getMetrics();
      try {
         for (BusinessAssetTerm businessAssetTerm : metrics) {
            if (businessAssetTerm.getBusinessTerm().getBusinessEntityTerm().isMeasurableEntity()) {
               BusinessStat businessStat = businessAssetTerm.getBusinessTerm().getBusinessStat();
               if (businessStat == null || businessStat.getStat().getStatType() != StatType.AVERAGE) {
                  BusinessStat averageBusinessStat = getReportAggregationHelper().prepareBusinessStat(StatType.AVERAGE,
                           false);
                  businessAssetTerm.getBusinessTerm().setBusinessStat(averageBusinessStat);
               }
            }
         }
      } catch (KDXException e) {
      }
   }

   private List<AggregateQueryInfo> populateScatterAggregateQueryInfo (ReportMetaInfo scatterReportMetaInfo)
            throws ReportQueryGenerationException {
      List<AggregateQueryInfo> scatterAggregateQueryInfos = new ArrayList<AggregateQueryInfo>();

      List<BusinessAssetTerm> metrics = scatterReportMetaInfo.getAssetQuery().getLogicalQuery().getMetrics();
      List<List<BusinessAssetTerm>> combinationMetricsForScatter = getReportAggregationHelper()
               .getCombinationMetricsForScatter(metrics);

      Map<BusinessAssetTerm, ReportColumnInfo> reportColumnsByBAT = ReportAggregationHelper
               .getReportColumnInfoByBATMap(scatterReportMetaInfo, metrics);
      for (List<BusinessAssetTerm> combinationMetrics : combinationMetricsForScatter) {

         // Assumption here is independent metric is always present at index position 1
         BusinessAssetTerm independentMetric = combinationMetrics.get(1);

         if (!isValidCombinationForScatter(reportColumnsByBAT, independentMetric)) {
            continue;
         }

         // Clone the ReportMetaInfo object
         ReportMetaInfo clonedReportMetaInfo = getReportAggregationHelper().cloneReportMetaInfo(scatterReportMetaInfo,
                  false);

         // Update the related info for scatter report meta info 
         clonedReportMetaInfo.setSummaryPathType(AggregateQueryType.SCATTER);

         AggregateQuery scatterAggregateQuery = getReportAggregationHelper().getScatterAggregateQuery(
                  clonedReportMetaInfo, combinationMetrics);

         //Prepare the scatter aggregated query info
         AggregateQueryInfo scatterAggregateQueryInfo = new AggregateQueryInfo();
         scatterAggregateQueryInfo.setAggregateQuery(scatterAggregateQuery);
         scatterAggregateQueryInfo.setReportMetaInfo(clonedReportMetaInfo);

         scatterAggregateQueryInfos.add(scatterAggregateQueryInfo);
      }

      return scatterAggregateQueryInfos;
   }

   private boolean isValidCombinationForScatter (Map<BusinessAssetTerm, ReportColumnInfo> reportColumnsByBAT,
            BusinessAssetTerm independentMetric) {

      boolean validForScatter = true;

      ReportColumnInfo reportColumnInfo = reportColumnsByBAT.get(independentMetric);
      if (reportColumnInfo == null) {
         return validForScatter;
      }

      // Band count should not be greater than the total result count of the column, as dynamic ranges are not feasible,
      // we cannot generate the scatter chart for business summary case
      int bandCount = getAggregationConfigurationService().getDynamicRangesBandCount();
      if (bandCount >= reportColumnInfo.getCountSize()) {
         validForScatter = false;
      }

      return validForScatter;
   }

   private AggregateQueryInfo populateDetailedAggregateQueryInfo (ReportMetaInfo reportMetaInfo)
            throws ReportQueryGenerationException {
      if (reportMetaInfo.isGenerateOnlyDataBrowser()) {
         // Generate the Aggregate Query for data browser
         ReportMetaInfo dataBrowserReportMetaInfo = getReportAggregationHelper().cloneReportMetaInfo(reportMetaInfo,
                  true);

         // enable the Data browser flag so that the report types are selected for it in the ReportSelectionService
         dataBrowserReportMetaInfo.setSummaryPathType(AggregateQueryType.DETAILED_SUMMARY);

         AggregateQuery dataBrowserAggregateQuery = getReportAggregationHelper().getDetailedAggregateQuery(
                  dataBrowserReportMetaInfo);

         // convert the ReportMetaInfo object into AggregationMetaInfo object
         AggregationMetaInfo aggregationMetaInfo = getReportAggregationHelper().transformMetaInfo(
                  dataBrowserReportMetaInfo);

         // generate the AggregateMetaInfo xml
         String aggregateMetaInfoXML = getReportAggregationHelper().getXStreamForMetaInfoTransformation().toXML(
                  aggregationMetaInfo);
         dataBrowserAggregateQuery.setReportMetaInfoStructure(aggregateMetaInfoXML);

         // Prepare the Aggregate Query for data browser
         AggregateQueryInfo dataBrowserAggregateQueryInfo = new AggregateQueryInfo();
         dataBrowserAggregateQueryInfo.setAggregateQuery(dataBrowserAggregateQuery);
         dataBrowserAggregateQueryInfo.setReportMetaInfo(dataBrowserReportMetaInfo);
         return dataBrowserAggregateQueryInfo;
      } else {
         // Generate the Aggregate Query for detail path
         if (logger.isDebugEnabled()) {
            logger.debug("Generating Aggregate query for the detail path...");
         }
         reportMetaInfo.setSummaryPathType(AggregateQueryType.DETAILED_SUMMARY);
         AggregateQuery detailedAggregateQuery = getReportAggregationHelper().getDetailedAggregateQuery(reportMetaInfo);

         //Prepare the detailed aggregated query info
         AggregateQueryInfo detailedAggregateQueryInfo = new AggregateQueryInfo();
         detailedAggregateQueryInfo.setAggregateQuery(detailedAggregateQuery);
         detailedAggregateQueryInfo.setReportMetaInfo(reportMetaInfo);
         return detailedAggregateQueryInfo;
      }
   }

   /**
    * @param reportMetaInfo
    * @param aggregateQueryInfoList
    * @throws ReportQueryGenerationException
    */
   public List<AggregateQueryInfo> populateHierarchyAggregateQueryInfo (ReportMetaInfo reportMetaInfo)
            throws ReportQueryGenerationException {
      List<AggregateQueryInfo> aggregateQueryInfoList = new ArrayList<AggregateQueryInfo>();
      List<ReportMetaHierarchyInfo> reportMetaHierarchyInfos = reportMetaInfo.getReportMetaHierarchyInfo();
      for (ReportMetaHierarchyInfo reportMetaHierarchyInfo : reportMetaHierarchyInfos) {
         // clone the ReportMetaInfo object
         ReportMetaInfo clonedReportMetaInfo = getReportAggregationHelper().cloneReportMetaInfo(reportMetaInfo, false);

         // Update the related info for hierarchy report meta info 
         clonedReportMetaInfo.setSummaryPathType(AggregateQueryType.HIERARCHY_SUMMARY);
         clonedReportMetaInfo.getAssetQuery().getLogicalQuery().setRollupQuery(true);

         // NOTE: We are not cloning ReportMetaHierarchyInfo itself but just adding them in new list as later we are not modifying them
         // in any case. If any plans to modify it in future, then do clone this object here
         List<ReportMetaHierarchyInfo> clonedReportMetaHierarchyInfos = new ArrayList<ReportMetaHierarchyInfo>();
         clonedReportMetaHierarchyInfos.add(reportMetaHierarchyInfo);
         clonedReportMetaInfo.setReportMetaHierarchyInfo(clonedReportMetaHierarchyInfos);

         AggregateQuery hierarchyAggregateQuery = getReportAggregationHelper().getHierarchyAggregateQuery(
                  clonedReportMetaInfo);

         //Prepare the hierarchy aggregated query info
         AggregateQueryInfo hierarchyAggregateQueryInfo = new AggregateQueryInfo();
         hierarchyAggregateQueryInfo.setAggregateQuery(hierarchyAggregateQuery);
         hierarchyAggregateQueryInfo.setReportMetaInfo(clonedReportMetaInfo);

         aggregateQueryInfoList.add(hierarchyAggregateQueryInfo);
      }
      return aggregateQueryInfoList;
   }

   public ReportAggregationHelper getReportAggregationHelper () {
      return reportAggregationHelper;
   }

   public void setReportAggregationHelper (ReportAggregationHelper reportAggregationHelper) {
      this.reportAggregationHelper = reportAggregationHelper;
   }

   /**
    * @return the aggregationConfigurationService
    */
   public IAggregationConfigurationService getAggregationConfigurationService () {
      return aggregationConfigurationService;
   }

   /**
    * @param aggregationConfigurationService the aggregationConfigurationService to set
    */
   public void setAggregationConfigurationService (IAggregationConfigurationService aggregationConfigurationService) {
      this.aggregationConfigurationService = aggregationConfigurationService;
   }
}