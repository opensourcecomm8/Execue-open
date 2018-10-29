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


package com.execue.repoting.aggregation.analyze.processor;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.type.ColumnType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.repoting.aggregation.bean.ReportColumnInfo;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.exception.AggregationExceptionCodes;
import com.execue.repoting.aggregation.exception.ReportMetadataException;
import com.execue.repoting.aggregation.helper.ReportAggregationHelper;
import com.execue.repoting.aggregation.service.IReportMetaProcessor;

/**
 * @author John Mallavalli
 */

public class DetailReportProcessor implements IReportMetaProcessor {

   private ReportAggregationHelper reportAggregationHelper;

   private static final Logger     logger = Logger.getLogger(DetailReportProcessor.class);

   /*
    * This method analyzes the counts to check if we need to just with the data browser or go for detail reports
    */
   public boolean analyzeMetadata (ReportMetaInfo reportMetaInfo) throws ReportMetadataException {
      // TODO: -JM- enhance the rules - move to a separate processor
      try {
         // Rule 1: if total count is less than 600, go for detail reports else data browser
         boolean enabled = reportAggregationHelper.isDetailReportPathEnabled();
         if (enabled) {
            long totalCount = reportMetaInfo.getTotalCount();
            if (logger.isDebugEnabled()) {
               logger.debug("Total records count : " + totalCount);
            }
            if (totalCount <= reportAggregationHelper.getDetailReportsSelectionThreshold()) {
               enableDetailReports(reportMetaInfo);
            } else {
               disableDetailReports(reportMetaInfo);
            }

            // Rule : If there are any user requested summarizations then do not use Detail Reports
            // -JM- 22 Apr 2011 Rule : If there are user-requested stats then use biz summary
            if (hasUserRequestedSummarizations(reportMetaInfo) || hasUserRequestedStats(reportMetaInfo)) {
               disableDetailReports(reportMetaInfo);
            }

            // Derive the column types
            // TODO: -JM- for now, only deduce if the column type is null, add rules later
            if (reportMetaInfo.isGenerateMultipleDetailReports()) {
               deduceColumnTypesForDetailReports(reportMetaInfo);
            }
         } else {
            reportMetaInfo.setGenerateOnlyDataBrowser(true);
         }
      } catch (Exception e) {
         throw new ReportMetadataException(AggregationExceptionCodes.DEFAULT_EXCEPTION_CODE,
                  "Exception in analysis for detail reports", e);
      }
      return true;
   }

   private boolean hasUserRequestedStats (ReportMetaInfo reportMetaInfo) {
      boolean userRequestedStats = false;
      if (ExecueCoreUtil.isCollectionNotEmpty(reportMetaInfo.getAssetQuery().getLogicalQuery().getMetrics())) {
         for (BusinessAssetTerm metric : reportMetaInfo.getAssetQuery().getLogicalQuery().getMetrics()) {
            if (metric.getBusinessTerm().getBusinessStat() != null
                     && metric.getBusinessTerm().getBusinessStat().isRequestedByUser()) {
               userRequestedStats = true;
               break;
            }
         }
      }
      return userRequestedStats;
   }

   private void enableDetailReports (ReportMetaInfo reportMetaInfo) {
      reportMetaInfo.setGenerateMultipleDetailReports(true);
      reportMetaInfo.setGenerateBusinessSummary(false);
      reportMetaInfo.setGenerateOnlyDataBrowser(false);
   }

   private void disableDetailReports (ReportMetaInfo reportMetaInfo) {
      reportMetaInfo.setGenerateMultipleDetailReports(false);
      reportMetaInfo.setGenerateBusinessSummary(true);
      reportMetaInfo.setGenerateOnlyDataBrowser(true);
   }

   private boolean hasUserRequestedSummarizations (ReportMetaInfo reportMetaInfo) {
      boolean userRequestedSummarizations = false;
      if (ExecueCoreUtil.isCollectionNotEmpty(reportMetaInfo.getAssetQuery().getLogicalQuery().getSummarizations())) {
         for (BusinessAssetTerm summarization : reportMetaInfo.getAssetQuery().getLogicalQuery().getSummarizations()) {
            if (summarization.getBusinessTerm().isRequestedByUser()) {
               userRequestedSummarizations = true;
               break;
            }
         }
      }
      return userRequestedSummarizations;
   }

   private void deduceColumnTypesForDetailReports (ReportMetaInfo reportMetaInfo) {
      for (ReportColumnInfo reportColumnInfo : reportMetaInfo.getReportColumns()) {
         ColumnType columnType = reportColumnInfo.getDetailReportColumnType();
         if (columnType == ColumnType.NULL || columnType == null) {
            // rules for deducing the column type
            // Modify the column type based on the data type of the column when there is no data type defined
            // in SWI
            reportAggregationHelper.deduceColumnType(reportColumnInfo);
         }
      }
   }

   public ReportAggregationHelper getReportAggregationHelper () {
      return reportAggregationHelper;
   }

   public void setReportAggregationHelper (ReportAggregationHelper reportAggregationHelper) {
      this.reportAggregationHelper = reportAggregationHelper;
   }
}