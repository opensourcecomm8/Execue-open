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

import com.execue.exception.LocalizedViewTxServiceException;
import com.execue.exception.ReportViewTxServiceException;
import com.softwarefx.chartfx.server.ChartServer;

public class ChartFxStateMapViewTxService extends ChartFxMapCanvasViewTxService {

   @Override
   protected int getChartHeight (int chartWidth) throws LocalizedViewTxServiceException {
      return super.getChartHeight(chartWidth);
   }

   @Override
   protected int getChartWidth (ChartServer chartServer, String chartType) throws LocalizedViewTxServiceException {
      return super.getChartWidth(chartServer, chartType);
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
   }

   @Override
   public void prepareViewData (Map<String, Object> domainObject) throws ReportViewTxServiceException {
      super.prepareViewData(domainObject);
   }

   @Override
   public void applyViewRules (Map<String, Object> domainObject) throws ReportViewTxServiceException {
      super.applyViewRules(domainObject);
   }

}
