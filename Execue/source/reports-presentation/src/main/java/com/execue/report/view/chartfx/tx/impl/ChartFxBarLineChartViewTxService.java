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


package com.execue.report.view.chartfx.tx.impl;

import java.util.Map;

import com.execue.core.common.bean.reports.ReportingConstants;
import com.execue.exception.LocalizedViewTxServiceException;
import com.execue.exception.ReportViewTxServiceException;
import com.execue.rules.bean.RuleParameters;
import com.execue.rules.exception.RuleException;
import com.softwarefx.chartfx.server.BarShape;
import com.softwarefx.chartfx.server.ChartServer;
import com.softwarefx.chartfx.server.Gallery;

public class ChartFxBarLineChartViewTxService extends ChartFxSingleCanvasViewTxService {

   @Override
   protected void setSeriesAxesAllocation (ChartServer chartCurve) throws LocalizedViewTxServiceException {
      // super.setSeriesAxesAllocation(chartCurve);
      chartCurve.getAllSeries().setGallery(Gallery.LINES);
      chartCurve.getSeries().get(0).setGallery(Gallery.BAR);
      chartCurve.getSeries().get(0).setBarShape(BarShape.RECTANGLE);
   }

   @Override
   public void applyViewRules (Map<String, Object> domainObject) throws ReportViewTxServiceException {
      super.applyViewRules(domainObject);
      setSeriesAxesAllocation((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
      RuleParameters ruleParams = new RuleParameters();
      ruleParams.addInput(domainObject.get(ReportingConstants.CHART_SERVER_KEY));
      try {
         getRuleService().executeRule(getPresentationConfigurationService().getFirstSeriesOnLeftAxis(), ruleParams);
         getRuleService().executeRule(getPresentationConfigurationService().getBarSize(), ruleParams);
         reduceBarSize((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
         // revisit the chart width based on the textual size of the series and adjust.
         adjustChartWidthAndHeightOnLegendTextSize((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
      } catch (RuleException e) {
         throw new ReportViewTxServiceException(e.getCode(), e);
      }
   }

   private void reduceBarSize (ChartServer chartServer) throws ReportViewTxServiceException {
      // reducing the size of the bar as the width of the charts have increased and
      // the bar size is proportional to it.
      for (int i = 0; i < chartServer.getSeries().size(); i++) {
         if (Gallery.BAR.equals(chartServer.getSeries().get(i).getGallery())) {
            short volumeSize = chartServer.getSeries().get(i).getVolume();
            volumeSize = (short) (volumeSize * 1 / 2);
            chartServer.getSeries().get(i).setVolume(volumeSize);
         }
      }
   }
}