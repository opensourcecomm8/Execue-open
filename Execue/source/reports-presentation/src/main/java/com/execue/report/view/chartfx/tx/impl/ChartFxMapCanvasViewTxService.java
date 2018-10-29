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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.reports.ReportingConstants;
import com.execue.exception.LocalizedViewTxServiceException;
import com.execue.exception.ReportViewTxServiceException;
import com.softwarefx.chartfx.server.ChartServer;
import com.softwarefx.chartfx.server.PointAttributesCollection;

public class ChartFxMapCanvasViewTxService extends ChartFxChartViewTxService {

   @Override
   protected int getChartHeight (int chartWidth) throws LocalizedViewTxServiceException {
      return super.getChartHeight(chartWidth);
   }

   @Override
   protected int getChartWidth (ChartServer chartServer, String chartType) throws LocalizedViewTxServiceException {
      // return super.getChartWidth(chartServer, chartType);
      return 650;
   }

   @Override
   public void prepareViewData (Map<String, Object> domainObject) throws ReportViewTxServiceException {
      super.prepareViewData(domainObject);
      ChartServer localChartObj = (ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY);
      localChartObj.getDataGrid().setVisible(false);
      localChartObj.setZoom(false);
   }

   @Override
   public void applyViewRules (Map<String, Object> domainObject) throws ReportViewTxServiceException {
      super.applyViewRules(domainObject);
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
   }

   @Override
   protected void setSeriesAxesAllocation (ChartServer chartCurve) throws LocalizedViewTxServiceException {
      super.setSeriesAxesAllocation(chartCurve);
      int maxPoints = chartCurve.getData().getPoints();
      double maxValue = 0;
      double minValue = 0;
      List<Double> dataPoints = new ArrayList<Double>();
      for (int i = 0; i < maxPoints; i++) {
         double currentValue = chartCurve.getData().get(0, i);
         dataPoints.add(currentValue);
         // overriding the missing point problem.
         if (("" + currentValue).contains("E"))
            currentValue = maxValue;
         if (maxValue < currentValue)
            maxValue = currentValue;
         if (minValue > currentValue)
            minValue = currentValue;
      }
      Collections.sort(dataPoints);
      // MapRegion mapRegion = null;
      // mapRegion.
      PointAttributesCollection plAttribs = chartCurve.getPoints();
      for (int cnt = 0, i = dataPoints.size() - 1; i >= 0; cnt++, i--) {
         for (int j = 0; j < maxPoints; j++) {
            double currentValue = chartCurve.getData().get(0, j);
            if (currentValue == dataPoints.get(i)) {
               System.out.println(plAttribs.get(i).getText());
               plAttribs.get(i).setColor(new Color(cnt * 4, cnt * 4, 255));
               break;
            }
         }
      }
   }
}
