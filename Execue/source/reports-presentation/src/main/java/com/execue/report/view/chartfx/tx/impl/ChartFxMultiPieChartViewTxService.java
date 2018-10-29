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
import com.softwarefx.chartfx.server.ChartServer;
import com.softwarefx.chartfx.server.Pane;
import com.softwarefx.chartfx.server.SeriesAttributes;

public class ChartFxMultiPieChartViewTxService extends ChartFxPieCanvasViewTxService {

   private int numberOfMeasures;

   @Override
   protected int getChartHeight (int chartWidth) throws LocalizedViewTxServiceException {
      return getProportionalHeight(super.getChartHeight(chartWidth));
   }

   @Override
   protected int getChartWidth (ChartServer chartServer, String chartType) throws LocalizedViewTxServiceException {
      int width = super.getChartWidth(chartServer, chartType);

      // get the number of measures based on the unique series name
      Set<String> seriesName = new HashSet<String>();

      for (SeriesAttributes series : chartServer.getSeries()) {
         String measureName[] = series.getText().split("~");
         seriesName.add(measureName[0]);
      }
      numberOfMeasures = seriesName.size();
      return width;
   }

   @Override
   public void prepareViewData (Map<String, Object> domainObject) throws ReportViewTxServiceException {
      super.prepareViewData(domainObject);

      setChartSeriesColors((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
      setTitleProperties((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
      setLegendBoxProps((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
   }

   @Override
   public void applyViewRules (Map<String, Object> domainObject) throws ReportViewTxServiceException {
      super.applyViewRules(domainObject);
      setSeriesAxesAllocation((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
   }

   @Override
   protected void setChartSeriesColors (ChartServer chartServer) throws LocalizedViewTxServiceException {
      super.setChartSeriesColors(chartServer);
   }

   @Override
   protected void setLegendBoxProps (ChartServer chartServer) throws LocalizedViewTxServiceException {
      super.setLegendBoxProps(chartServer);
   }

   @Override
   protected void setSeriesAxesAllocation (ChartServer chartCurve) throws LocalizedViewTxServiceException {

      for (int alCntr = 0; alCntr < chartCurve.getData().getSeries(); alCntr++) {

         SeriesAttributes series = chartCurve.getSeries().get(alCntr);

         chartCurve.getAxisY().getTitle().setText(ReportingConstants.SPACE);
         series.setAxisY(chartCurve.getAxisY());
         series.bringToFront();

         Pane pane = new Pane();
         pane.setSeparation(20);

         if (alCntr == 0) {
            pane = chartCurve.getPanes().get(alCntr);
         } else {
            chartCurve.getPanes().add(pane);
         }
         pane.getAxisY().setVisible(true);
         series.setPane(pane);
      }

   }

   @Override
   protected void setTitleProperties (ChartServer chartServer) throws LocalizedViewTxServiceException {
      super.setTitleProperties(chartServer);
   }

   private int getProportionalHeight (int originalHeight) {
      if (numberOfMeasures > 3)
         return (int) (originalHeight * (numberOfMeasures * 0.45));
      else
         return originalHeight;
   }
}
