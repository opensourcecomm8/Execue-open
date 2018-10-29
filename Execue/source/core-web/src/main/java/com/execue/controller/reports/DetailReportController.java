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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.execue.core.common.bean.ReportListWrapper;
import com.execue.core.common.bean.reports.prsntion.ReportWrapper;
import com.execue.core.exception.HandlerException;
import com.execue.exception.PresentationExceptionCodes;
import com.execue.handler.reports.IReportController;
import com.execue.reporting.presentation.service.IPresentationTransformService;

public class DetailReportController implements IReportController, ApplicationContextAware {

   private ApplicationContext            springAppContext;
   private IPresentationTransformService gridTransform;

   public void selectReportHandlers (ReportListWrapper reportListWrapper, HttpServletRequest request,
            HttpServletResponse response, String xmlReportData) throws HandlerException {
      throw new HandlerException(PresentationExceptionCodes.DEFAULT_EXCEPTION_METHOD_NOT_SUPPORTED, "Method Unused");
   }

   public void selectReportHandlers (ReportListWrapper reportListWrapper, HttpServletRequest request,
            HttpServletResponse response, long aggregatedQueryId) throws HandlerException {
      /*
       * int iRequestedPage = 1; int iPageCount = 100; String source = (String)
       * request.getAttribute(ReportingConstants.CHART_SOURCE_KEY);
       */
      try {

         /*
          * PresentationTransformData transformedData = gridTransform.getReport(iRequestedPage, iPageCount,
          * aggregatedQueryId); UniversalReport detailReport =
          * UniversalReportParser.preProcessReportData(PresentationTransformHelper
          * .normalizeDataSection(transformedData.getXmlData())); String divContent = ""; if
          * (transformedData.getPageCount() / iPageCount > 0) { divContent = "<b>First 100 records ... <a
          * href='showDetailReport.action?agQueryIdList=" + aggregatedQueryId + "&source=" + source + "&type=" +
          * request.getParameter("type") + "&requestedString=" + request.getParameter("requestedString") + "'>More</a></b>"; }
          * else { divContent = "<b>Showing records 1 - " + detailReport.getData().getValues().size() + "</b>"; }
          * detailReport.getHeader().setSource(source); String chartTypeString[] =
          * detailReport.getHeader().getReporttypes().split(","); // over riding the chartTypes here to re-use the html
          * grid and add the report title String detailReportType = ""; String thumbnail = ""; for (String chartType :
          * chartTypeString) { if (ReportType.DetailGrid.getValue().toString().equals(chartType)) { detailReportType =
          * ReportType.DetailGrid.toString(); thumbnail = "<img class=\"htmlThumbnail\" " +
          * "src=\"images/reporting/detailedgrid.png\" onclick=\"chartView('" + detailReportType + "');\"
          * onmouseover=\"chartView('" + detailReportType + "');\" />"; IReportHandler reportHandler = (IReportHandler)
          * ((ReportHandlerFactory) springAppContext
          * .getBean(ReportingConstants.REPORT_HANDLER_FACTORY)).getReportHandler(detailReportType); if (reportHandler !=
          * null) { reportHandler.handleReport(detailReport, reportListWrapper, request, response); int sizeOfReport =
          * reportListWrapper.getHtmlReports().size(); ReportWrapper detailHtmlReport =
          * reportListWrapper.getHtmlReports().get(sizeOfReport - 1); detailHtmlReport.setId(detailReportType);
          * detailHtmlReport.setThumbnail(thumbnail); detailHtmlReport.setReport(divContent +
          * detailHtmlReport.getReport()); } } }
          */

         String thumbnail = "<img class=\"htmlThumbnail\" "
                  + "src=\""
                  + request.getContextPath()
                  + "/images/main/reporting/detailedgrid.png\" onclick=\"chartView('DetailGrid');\" onmouseover=\"chartView('DetailGrid');\" />";

         ReportWrapper detailHtmlReport = new ReportWrapper();
         detailHtmlReport.setId("DetailGrid");
         detailHtmlReport.setThumbnail(thumbnail);
         detailHtmlReport.setReport("");
         if (reportListWrapper.getHtmlReports() != null)
            reportListWrapper.getHtmlReports().add(detailHtmlReport);
      } catch (NullPointerException e) {
         throw new HandlerException(PresentationExceptionCodes.DEFAULT_DATA_BROWSER_FAILED, e.getMessage());
      }
   }

   public IPresentationTransformService getGridTransform () {
      return gridTransform;
   }

   public void setGridTransform (IPresentationTransformService gridTransform) {
      this.gridTransform = gridTransform;
   }

   public void setApplicationContext (ApplicationContext applicationContext) throws BeansException {
      springAppContext = applicationContext;
   }

   public ApplicationContext getSpringAppContext () {
      return springAppContext;
   }

   public void setSpringAppContext (ApplicationContext springAppContext) {
      this.springAppContext = springAppContext;
   }

}
