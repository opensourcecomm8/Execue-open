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


package com.execue.controller.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.execue.core.common.bean.ReportListWrapper;
import com.execue.core.common.bean.reports.prsntion.ReportWrapper;
import com.execue.core.exception.HandlerException;
import com.execue.handler.reports.IScatterChartController;

/**
 * This object  prepares static image and div for scatter chart
 * @author Jitendra
 *
 */
public class ScatterChartController implements IScatterChartController {

   @Override
   public void preapreScatterChartIcon (ReportListWrapper outputReportsList, HttpServletRequest request,
            HttpServletResponse response, List<Long> aggregatedQueryIds) throws HandlerException {

      String thumbnail = "<img class=\"htmlThumbnail\" " + "src=\"" + request.getContextPath()
               + "/images/main/scatterplot.png\" onclick=\"showScatterChartSubIcons(" + aggregatedQueryIds
               + ");\" onmouseover=\"showScatterChartSubIcons(" + aggregatedQueryIds + ");\" />";
      // prepare scatter chart icon
      ReportWrapper scatterChartIcon = new ReportWrapper();
      scatterChartIcon.setId("fxScatterIcon");
      scatterChartIcon.setThumbnail(thumbnail);
      scatterChartIcon.setReport("");
      outputReportsList.getChartFxReports().add(scatterChartIcon);

      // prepare scatter chart div up-front to represent actual report 
      for (Long aggregatedQueryId : aggregatedQueryIds) {
         ReportWrapper scatterChartDiv = new ReportWrapper();
         scatterChartDiv.setId("ScatterChart" + aggregatedQueryId);
         scatterChartDiv.setReport("");
         outputReportsList.getChartFxReports().add(scatterChartDiv);
      }

   }

   @Override
   public void preapreScatterChartSubIcons (ReportListWrapper outputReportsList, HttpServletRequest request,
            HttpServletResponse response, List<Long> aggregatedQueryIds) throws HandlerException {
      // prepare scatter chart div up-front to represent actual report 
      for (Long aggregatedQueryId : aggregatedQueryIds) {
         String scatterChartDiv = "ScatterChart" + aggregatedQueryId;
         String thumbnail = "<img class=\"htmlThumbnail\" " + "src=\"" + request.getContextPath()
                  + "/images/main/scatterplot.png\" onclick=\"showScatterChart('" + scatterChartDiv + "',"
                  + aggregatedQueryId + ");\" onmouseover=\"showScatterChart('" + scatterChartDiv + "',"
                  + aggregatedQueryId + ");\" />";
         ReportWrapper scatterChartIcon = new ReportWrapper();
         scatterChartIcon.setId("ScatterChart" + aggregatedQueryId);
         scatterChartIcon.setThumbnail(thumbnail);
         scatterChartIcon.setReport("");
         outputReportsList.getChartFxReports().add(scatterChartIcon);
      }

   }

}
