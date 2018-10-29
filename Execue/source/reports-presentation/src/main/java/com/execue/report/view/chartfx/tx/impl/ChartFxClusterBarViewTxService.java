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
import com.softwarefx.chartfx.server.LegendItemAttributes;
import com.softwarefx.chartfx.server.Pane;
import com.softwarefx.chartfx.server.SeriesAttributes;

public class ChartFxClusterBarViewTxService extends ChartFxMultiCanvasViewTxService {

   @Override
   public void applyViewRules (Map<String, Object> domainObject) throws ReportViewTxServiceException {
      super.applyViewRules(domainObject);
      setSeriesAxesAllocation((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
      RuleParameters ruleParams = new RuleParameters();
      ruleParams.addInput(domainObject.get(ReportingConstants.CHART_SERVER_KEY));
      try {
         getRuleService().executeRule(getPresentationConfigurationService().getBarSize(), ruleParams);
      } catch (RuleException e) {
         throw new ReportViewTxServiceException(e.getCode(), e);
      }
   }

   @Override
   public void prepareViewData (Map<String, Object> domainObject) throws ReportViewTxServiceException {
      super.prepareViewData(domainObject);

      setChartSeriesColors((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
      setTitleProperties((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
      setLegendBoxProps((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
   }

   @Override
   protected void setChartSeriesColors (ChartServer chartServer) throws LocalizedViewTxServiceException {
      super.setChartSeriesColors(chartServer);
   }

   @Override
   protected void setTitleProperties (ChartServer chartServer) throws LocalizedViewTxServiceException {
      super.setTitleProperties(chartServer);
   }

   @Override
   protected void setLegendBoxProps (ChartServer chartServer) throws LocalizedViewTxServiceException {
      super.setLegendBoxProps(chartServer);

      // enable the inverse property for legend display order
      LegendItemAttributes item = new LegendItemAttributes();
      item.setInverted(false);
      chartServer.getLegendBox().getItemAttributes().set(chartServer.getSeries(), item);
   }

   @Override
   protected void setSeriesAxesAllocation (ChartServer chartCurve) throws LocalizedViewTxServiceException {
      super.setSeriesAxesAllocation(chartCurve);

      for (int alCntr = 0; alCntr < chartCurve.getData().getSeries(); alCntr++) {

         SeriesAttributes series = chartCurve.getSeries().get(alCntr);

         series.setGallery(Gallery.BAR);
         series.setBarShape(BarShape.RECTANGLE);
         chartCurve.getAxisY().getTitle().setText(ReportingConstants.SPACE);
         series.setAxisY(chartCurve.getAxisY());
         series.bringToFront();
         /*
          * int xAxisWidth = chartCurve.getData().getPoints(); if (xAxisWidth <= 9) { if (xAxisWidth >= 3)
          * series.setVolume((short) (getChartWidth(chartCurve, ReportingConstants.CHART_TYPE_CLUSTER) * 0.117)); else
          * series.setVolume((short) (getChartWidth(chartCurve, ReportingConstants.CHART_TYPE_CLUSTER) * 0.06425)); }
          */

         Pane pane = new Pane();
         pane.setSeparation(20);

         if (alCntr == 0) {
            pane = chartCurve.getPanes().get(alCntr);
         } else {
            chartCurve.getPanes().add(pane);
         }
         pane.getAxisY().setVisible(true);
         pane.getAxes().get(0).getDataFormat().setCustomFormat("#,###.##");
         series.setPane(pane);
      }

   }

   @Override
   protected int getChartHeight (int chartWidth) throws LocalizedViewTxServiceException {
      return super.getChartHeight(chartWidth);
   }

   @Override
   protected int getChartWidth (ChartServer chartServer, String chartType) throws LocalizedViewTxServiceException {
      int width = super.getChartWidth(chartServer, chartType);
      return width;
   }

}
