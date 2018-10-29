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

import com.execue.core.common.type.ColumnType;
import com.execue.repoting.aggregation.bean.ReportColumnInfo;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.exception.ReportMetadataException;
import com.execue.repoting.aggregation.helper.ReportAggregationHelper;
import com.execue.repoting.aggregation.service.IReportMetaProcessor;

/**
 * @author John Mallavalli
 */

public class PostProcessor implements IReportMetaProcessor {

   private ReportAggregationHelper reportAggregationHelper;

   private static final Logger     logger = Logger.getLogger(PostProcessor.class);

   /**
    * This method checks and sets the flags to check the paths that the current query can traverse
    */
   public boolean analyzeMetadata (ReportMetaInfo reportMetaInfo) throws ReportMetadataException {
      // TODO: -JM- test and remove the comment for the below rule
      // handleIDDescriptionColumnsForDetailReports(reportMetaInfo);

      // Rule: To select the required paths for a query [Business Summary & Detail Reports]
      // reportMetaInfo.setGenerateBusinessSummary(true);
      // reportMetaInfo.setGenerateDetailReport(true);
      // if (reportMetaInfo.isGenerateMultipleDetailReports()) {
      // reportMetaInfo.setGenerateBusinessSummary(false);
      // }

      return true;
   }

   // TODO: -JM- add rule for setting the upper-limit on the detail report path when ID/DESC columns are found
   // TODO: -JM- modify the rule such that when ID/DESC column is present, even if the row count is greater than the
   // threshold(600), detail report path have to be taken - probably data browser is the best fit
   private void handleIDDescriptionColumnsForDetailReports (ReportMetaInfo reportMetaInfo) {
      boolean idDescColumnPresent = false;
      for (ReportColumnInfo colInfo : reportMetaInfo.getReportColumns()) {
         if (ColumnType.ID.equals(colInfo.getColumnType())) {
            idDescColumnPresent = true;
            break;
         }
      }
      if (reportMetaInfo.isGenerateMultipleDetailReports()) {
         if (idDescColumnPresent) {
            reportMetaInfo.setGenerateMultipleDetailReports(true);
            reportMetaInfo.setGenerateOnlyDataBrowser(true);
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