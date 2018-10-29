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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.reports.ReportingConstants;
import com.execue.exception.LocalizedViewTxServiceException;
import com.execue.exception.ReportViewTxServiceException;
import com.execue.rules.bean.RuleParameters;
import com.execue.rules.exception.RuleException;
import com.softwarefx.StringAlignment;
import com.softwarefx.chartfx.server.ChartServer;
import com.softwarefx.chartfx.server.Gallery;
import com.softwarefx.chartfx.server.LegendItemAttributes;
import com.softwarefx.chartfx.server.Pane;
import com.softwarefx.chartfx.server.SeriesAttributes;
import com.softwarefx.chartfx.server.SeriesAttributesCollection;

public class ChartFxCrossBarViewTxService extends ChartFxMultiCanvasViewTxService {

   @Override
   public void applyViewRules (Map<String, Object> domainObject) throws ReportViewTxServiceException {
      super.applyViewRules(domainObject);
      setSeriesAxesAllocation((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
      RuleParameters ruleParams = new RuleParameters();
      ruleParams.addInput(domainObject.get(ReportingConstants.CHART_SERVER_KEY));
      try {
         getRuleService().executeRule(getPresentationConfigurationService().getSameColoredMeasure(), ruleParams);
         getRuleService().executeRule(getPresentationConfigurationService().getDistinctLegends(), ruleParams);
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
      // super.setSeriesAxesAllocation(chartCurve);

      SeriesAttributesCollection seriesCollection = chartCurve.getSeries();
      Set<String> acrossMemberList = new HashSet<String>();

      for (SeriesAttributes series : seriesCollection) {
         int acrossNameIndex = series.getText().indexOf("~");
         if (acrossNameIndex > -1) {
            acrossMemberList.add(series.getText().substring(acrossNameIndex + 1));
         }
      }

      chartCurve.setGallery(Gallery.BAR);

      int seriesCount = chartCurve.getData().getSeries();
      int numberOfMeasure = seriesCount / acrossMemberList.size();
      int acrossMemberCount = seriesCount / numberOfMeasure;

      for (int acrossMemberCntr = 0; acrossMemberCntr < numberOfMeasure; acrossMemberCntr++) {
         Pane pane = new Pane(acrossMemberCntr);
         pane.getAxes().get(0).getDataFormat().setCustomFormat("#,###.##");
         chartCurve.getPanes().add(pane);
      }

      for (int paneCntr = 0, seriesCntr = 0; paneCntr < numberOfMeasure; paneCntr++) {

         for (int acrossMemberCntr = 0; acrossMemberCntr < seriesCount; acrossMemberCntr++, seriesCntr++) {

            if (acrossMemberCntr == acrossMemberCount)
               break; // quit out of 1st loop after Nth iteration based on across members.

            SeriesAttributes series = chartCurve.getSeries().get(seriesCntr);
            series.setPane(chartCurve.getPanes().get(paneCntr));
            if (!(paneCntr % 2 == 0)) {
               series.getAxisY().getTitle().setSeparation(30);
            }
            series.getAxisY().getTitle().setAlignment(StringAlignment.FAR);
            int acrossNameIndex = series.getText().indexOf("~");
            if (acrossNameIndex > -1) {
               String seriesName = series.getText().substring(0, acrossNameIndex);
               series.getPane().getAxisY().getTitle().setText(seriesName);
               series.setText(series.getText().substring(acrossNameIndex + 1));
            }

            /*
             * int xAxisWidth = chartCurve.getData().getPoints(); if (xAxisWidth <= 9) { if (xAxisWidth >= 3)
             * series.setVolume((short) (getChartWidth(chartCurve, ReportingConstants.CHART_TYPE_CLUSTER) * 0.117));
             * else series .setVolume((short) (getChartWidth(chartCurve, ReportingConstants.CHART_TYPE_CLUSTER) *
             * 0.06425)); }
             */
         }
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
