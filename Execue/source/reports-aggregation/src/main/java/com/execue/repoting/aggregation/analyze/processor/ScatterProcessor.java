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

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.exception.ReportMetadataException;
import com.execue.repoting.aggregation.helper.ReportAggregationHelper;
import com.execue.repoting.aggregation.service.IReportMetaProcessor;

/**
 * @author Nitesh
 */

public class ScatterProcessor implements IReportMetaProcessor {

   private static final Logger logger = Logger.getLogger(ScatterProcessor.class);

   /**
    * This method checks and sets the flags to check the paths that the current query can traverse
    */
   public boolean analyzeMetadata (ReportMetaInfo reportMetaInfo) throws ReportMetadataException {

      // Analyze for scatter handling and set the flag accordingly
      StructuredQuery structuredQuery = reportMetaInfo.getAssetQuery().getLogicalQuery();
      Asset asset = structuredQuery.getAsset();
      boolean isExecueOwnedCube = ExecueBeanUtil.isExecueOwnedCube(asset);

      // Rules to set the generate scatter report flag
      if (reportMetaInfo.getAssetQuery().getLogicalQuery().getTopBottom() != null) {
         // Rule 1: Top Bottom queries can never produce Scatter Plots
         reportMetaInfo.setGenerateScatterReport(false);
      } else if (reportMetaInfo.getAssetQuery().getLogicalQuery().getCohort() != null) {
         // Rule 2: Cohort Queries can never produce Scatter Plots
         reportMetaInfo.setGenerateScatterReport(false);
      } else if (isExecueOwnedCube) {
         // Rule 3: Cube can never produce scatter plots
         reportMetaInfo.setGenerateScatterReport(false);
      } else if (reportMetaInfo.isGenerateBusinessSummary()
               && !ReportAggregationHelper.checkIfAllDimensionAreUnivariants(reportMetaInfo)) {
         // Rule 4: Business Summary yielding to more than one row can never produce Scatter Plots
         reportMetaInfo.setGenerateScatterReport(false);
      } else if (ReportAggregationHelper.getMeasureCount(structuredQuery.getMetrics()) < 2) {
         // Rule 5: Less than 2 measures can never produce Scatter Plots
         reportMetaInfo.setGenerateScatterReport(false);
      } else {
         // scatter plots are possible
         reportMetaInfo.setGenerateScatterReport(true);
      }
      return true;
   }
}