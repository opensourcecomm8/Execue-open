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
import com.execue.exception.PresentationExceptionCodes;
import com.execue.exception.ReportViewTxServiceException;
import com.softwarefx.chartfx.server.ChartServer;
import com.softwarefx.chartfx.server.Gallery;

public class ChartFxScatterChartViewTxService extends ChartFxSingleCanvasViewTxService {

   @Override
   protected void setSeriesAxesAllocation (ChartServer chartServer) throws LocalizedViewTxServiceException {

      chartServer.setGallery(Gallery.SCATTER);

      chartServer.getAxisX().getGrids().getMajor().setVisible(true);

      chartServer.setToolTipFormat("x:%x , y:%v");

      chartServer.getLegendBox().setVisible(false);

      // Linear Regression Related Changes
      // NOTE:: DV:: Uncomment below lines once we have the license for this chart
      // Statistics chartStatistics = new Statistics();
      // chartStatistics.getStudies().add(StudyGroup.XYCORRELATION);
      // chartStatistics.getStudies().add(Analysis.LINEAR_REGRESSION_B);
      // chartStatistics.getStudies().add(Analysis.LINEAR_REGRESSION_M);
      // chartStatistics.getLegendBox().setVisible(true);

      // Add it to the extension
      // chartCurve.getExtensions().add(chartStatistics);
   }

   @Override
   protected void setTitleProperties (ChartServer chartServer) throws LocalizedViewTxServiceException {
      try {
         super.setTitleProperties(chartServer);
         // Reset the main title text for scatters 
         String mainTitle = chartServer.getTitles().get(0).getText();

         int maxLenghtForTitleLine = 80;
         int titleEndIndex = mainTitle.length() >= maxLenghtForTitleLine ? (mainTitle.indexOf(" ",
                  maxLenghtForTitleLine) > -1 ? mainTitle.indexOf(" ", maxLenghtForTitleLine) : mainTitle.length())
                  : mainTitle.length();
         String mainTitleSubString = titleEndIndex >= maxLenghtForTitleLine ? mainTitle.substring(0, titleEndIndex)
                  .trim() : mainTitle;

         chartServer.getTitles().get(0).setText(mainTitleSubString);

      } catch (Exception e) {
         throw new LocalizedViewTxServiceException(PresentationExceptionCodes.DEFAULT_LOCALIZED_VIEWTX_EXCEPTION, e);
      }
   }

   @Override
   public void applyViewRules (Map<String, Object> domainObject) throws ReportViewTxServiceException {
      super.applyViewRules(domainObject);
      setSeriesAxesAllocation((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
      setTitleProperties((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
   }
}
