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

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.repoting.aggregation.analyze.processor.DetailReportProcessor;
import com.execue.repoting.aggregation.analyze.processor.HierarchyProcessor;
import com.execue.repoting.aggregation.analyze.processor.PostProcessor;
import com.execue.repoting.aggregation.analyze.processor.RangeProcessor;
import com.execue.repoting.aggregation.analyze.processor.ReductionProcessor;
import com.execue.repoting.aggregation.analyze.processor.ScatterProcessor;
import com.execue.repoting.aggregation.analyze.processor.StatisticsProcessor;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.exception.ReportMetadataException;
import com.execue.repoting.aggregation.service.IReportMetadataAnalyzerService;

/**
 * @author John Mallavalli
 * @since 4.0
 */

public class ReportMetadataAnalyzerServiceImpl implements IReportMetadataAnalyzerService {

   private DetailReportProcessor detailReportProcessor;
   private ReductionProcessor    reductionProcessor;
   private RangeProcessor        rangeProcessor;
   private StatisticsProcessor   statisticsProcessor;
   private HierarchyProcessor    hierarchyProcessor;
   private ScatterProcessor      scatterProcessor;
   private PostProcessor         postProcessor;

   public ReportMetaInfo analyze (ReportMetaInfo reportMetaInfo) throws ReportMetadataException {
      StructuredQuery logicalQuery = reportMetaInfo.getAssetQuery().getLogicalQuery();
      if (logicalQuery.getTopBottom() == null) {

         Asset asset = logicalQuery.getAsset();
         boolean isExecueOwnedCube = ExecueBeanUtil.isExecueOwnedCube(asset);

         if (!isExecueOwnedCube) {
            // analyze if we need to generate the detail reports, multiple detail reports, data browser, business summary with/without data browser 
            detailReportProcessor.analyzeMetadata(reportMetaInfo);

            // Deduce the report columns behavior based on several rules defined inside the processor
            reductionProcessor.analyzeMetadata(reportMetaInfo);

            // Apply the ranges if any measure is deduced as Dimension in the above processor call
            rangeProcessor.analyzeMetadata(reportMetaInfo);
         }

         // apply the stats based on the behavior and rules
         statisticsProcessor.analyzeMetadata(reportMetaInfo);

         // analyze the meta data for processing the hierarchies
         hierarchyProcessor.analyzeMetadata(reportMetaInfo);

         // analyze the meta data for processing the scatters
         scatterProcessor.analyzeMetadata(reportMetaInfo);

         // Nothing as of now
         postProcessor.analyzeMetadata(reportMetaInfo);
      } else {
         // TODO: -JM- confirm the below logic
         // turn-off detail report path and even data browser
         reportMetaInfo.setGenerateDetailReport(false);
         reportMetaInfo.setGenerateOnlyDataBrowser(false);
      }
      return reportMetaInfo;
   }

   public DetailReportProcessor getDetailReportProcessor () {
      return detailReportProcessor;
   }

   public void setDetailReportProcessor (DetailReportProcessor detailReportProcessor) {
      this.detailReportProcessor = detailReportProcessor;
   }

   public ReductionProcessor getReductionProcessor () {
      return reductionProcessor;
   }

   public void setReductionProcessor (ReductionProcessor reductionProcessor) {
      this.reductionProcessor = reductionProcessor;
   }

   public RangeProcessor getRangeProcessor () {
      return rangeProcessor;
   }

   public void setRangeProcessor (RangeProcessor dynamicRangeProcessor) {
      this.rangeProcessor = dynamicRangeProcessor;
   }

   public StatisticsProcessor getStatisticsProcessor () {
      return statisticsProcessor;
   }

   public void setStatisticsProcessor (StatisticsProcessor statisticsProcessor) {
      this.statisticsProcessor = statisticsProcessor;
   }

   public HierarchyProcessor getHierarchyProcessor () {
      return hierarchyProcessor;
   }

   public void setHierarchyProcessor (HierarchyProcessor hierarchyProcessor) {
      this.hierarchyProcessor = hierarchyProcessor;
   }

   public ScatterProcessor getScatterProcessor () {
      return scatterProcessor;
   }

   public void setScatterProcessor (ScatterProcessor scatterProcessor) {
      this.scatterProcessor = scatterProcessor;
   }

   public PostProcessor getPostProcessor () {
      return postProcessor;
   }

   public void setPostProcessor (PostProcessor postProcessor) {
      this.postProcessor = postProcessor;
   }
}