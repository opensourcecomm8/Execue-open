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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.execue.core.common.bean.ReportListWrapper;
import com.execue.core.common.bean.reports.ReportingConstants;
import com.execue.core.common.bean.reports.prsntion.ReportWrapper;
import com.execue.core.common.bean.reports.prsntion.UniversalColumn;
import com.execue.core.common.bean.reports.prsntion.UniversalReport;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ReportType;
import com.execue.core.common.util.ExeCueXMLUtils;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.exception.PresentationExceptionCodes;
import com.execue.exception.UniversalDataTransformationException;
import com.execue.handler.reports.IReportController;
import com.execue.handler.reports.IReportHandler;
import com.execue.handler.reports.ReportHandlerFactory;
import com.execue.qdata.service.IQueryDataService;
import com.execue.reporting.presentation.helper.PresentationTransformHelper;

public class ReportController implements IReportController, ApplicationContextAware {

   private Logger             logger = Logger.getLogger(ReportController.class);

   private ApplicationContext springAppContext;
   private IQueryDataService  queryDataService;

   public void selectReportHandlers (ReportListWrapper outputReports, HttpServletRequest request,
            HttpServletResponse response, long aggregatedQueryId) throws HandlerException {
      throw new HandlerException(PresentationExceptionCodes.DEFAULT_EXCEPTION_METHOD_NOT_SUPPORTED, "Method Unused");
   }

   public void selectReportHandlers (ReportListWrapper outputReports, HttpServletRequest request,
            HttpServletResponse response, String xmlReportData) throws HandlerException {

      /*
       * try { xmlReportData = queryDataService.getReportXMLData(new Long(request.getAttribute("queryId").toString())
       * .longValue(), new Long(request.getAttribute("assetId").toString()).longValue(), new Long(request
       * .getAttribute("businessQueryId").toString()).longValue(), AggregateQueryType.BUSINESS_SUMMARY); } catch
       * (NumberFormatException e) { e.printStackTrace(); } catch (QueryDataException e) { log.error("Exception occured
       * while retrieving business summary report: " + e.getMessage()); }
       */

      if (ExecueCoreUtil.isNotEmpty(xmlReportData)) {
         String chartTypes = ExeCueXMLUtils.getSelectedReportTypesFromXML(xmlReportData);
         UniversalReport universalReport = prepareUniversalReport(request, xmlReportData);

         List<ReportType> selectedReportTypes = getSelectedReportTypes(chartTypes);

         // Custom logic to remove Pie Chart if the member count of the dimension exceeds the max_allowed_count
         if (selectedReportTypes.contains(ReportType.PieChart)) {
            // TODO: -JM- configure the max member count value
            int MAX_MEMBER_COUNT = 12;
            int memberCount = 0;
            for (UniversalColumn uColumn : universalReport.getHeader().getColumns()) {
               if (ColumnType.DIMENSION.getValue().equalsIgnoreCase(uColumn.getCtype())) {
                  memberCount = Integer.parseInt(uColumn.getNummembers());
                  break;
               }
            }
            if (memberCount > MAX_MEMBER_COUNT) {
               selectedReportTypes.remove(ReportType.PieChart);
            }
         }

         for (ReportType reportType : selectedReportTypes) {
            String reportTypeStr = reportType.toString();
            String reportTypeForThumbnail = reportTypeStr;

            // to check if the repeated reportTypes are coming or not
            // this done to avoid duplicate coming from detail path
            for (ReportWrapper chartReports : outputReports.getChartFxReports()) {
               if (ExecueCoreUtil.isNotEmpty(chartReports.getId())
                        && chartReports.getId().equalsIgnoreCase(reportTypeStr)) {
                  reportTypeForThumbnail = "detail_" + reportTypeStr;
                  break;
               }
            }
            for (ReportWrapper htmlReports : outputReports.getHtmlReports()) {
               if (ExecueCoreUtil.isNotEmpty(htmlReports.getId())
                        && htmlReports.getId().equalsIgnoreCase(reportTypeStr)) {
                  reportTypeForThumbnail = "detail_" + reportTypeStr;
                  break;
               }
            }

            IReportHandler reportHandler = ((ReportHandlerFactory) springAppContext
                     .getBean(ReportingConstants.REPORT_HANDLER_FACTORY)).getReportHandler(reportTypeStr);
            if (reportHandler != null) {

               // setting the chart types upfront so that it can be used by later classes in path.
               ReportWrapper reportWrapper = new ReportWrapper();
               if (reportTypeStr.toLowerCase().endsWith("chart")) {
                  outputReports.getChartFxReports().add(reportWrapper);
                  reportWrapper.setId(reportTypeStr);
               } else {
                  outputReports.getHtmlReports().add(reportWrapper);
                  reportWrapper.setId(reportTypeStr);
               }

               reportHandler.handleReport(universalReport, outputReports, request, response);
               String contextPath = request.getContextPath();
               int sizeOfReport = outputReports.getChartFxReports().size();

               if (reportTypeStr.toLowerCase().endsWith("chart")) { // prepare and set the chart thumbnails
                  if (sizeOfReport > 0) {
                     ReportWrapper chartFxReport = outputReports.getChartFxReports().get(sizeOfReport - 1);
                     chartFxReport.setId(reportTypeForThumbnail);

                     String thumbnailImageTag = "<img src=\"" + contextPath + "/" + chartFxReport.getThumbnail() + "\""
                              + " class=\"chartThumbnail\">" + "<img src=\"" + contextPath
                              + "/images/main/reporting/reportBorder.png\" onmouseover=\"chartView('"
                              + reportTypeForThumbnail + "');\" onclick=\"chartView('" + reportTypeForThumbnail
                              + "');\"" + " class=\"overlayBox\">";
                     chartFxReport.setThumbnail(thumbnailImageTag);
                  }
               } else { // prepare and set the HTML grid thumbnails
                  sizeOfReport = outputReports.getHtmlReports().size();
                  if (sizeOfReport > 0) {
                     ReportWrapper htmlReport = outputReports.getHtmlReports().get(sizeOfReport - 1);
                     htmlReport.setId(reportTypeForThumbnail);

                     String htmlThumbnail = "";
                     String imageSrc = "";
                     if (ReportType.Grid.toString().equals(reportTypeStr)) {
                        imageSrc = contextPath + "/images/main/reporting/ngrid.png";
                     } else if (ReportType.GroupTable.toString().equals(reportTypeStr)) {
                        imageSrc = contextPath + "/images/main/reporting/groupgrid.png";
                     } else if (ReportType.CrossTable.toString().equals(reportTypeStr)) {
                        imageSrc = contextPath + "/images/main/reporting/acrossgrid.png";
                     } else if (ReportType.PortraitTable.toString().equals(reportTypeStr)) {
                        imageSrc = contextPath + "/images/main/reporting/acrossgrid.png";
                     } else if (ReportType.ClusterColumnGrid.toString().equals(reportTypeStr)) {
                        imageSrc = contextPath + "/images/main/reporting/OBGgrid.png";
                     }

                     htmlThumbnail = "<img class=\"htmlThumbnail\" src=\"" + imageSrc + "\" onclick=\"chartView('"
                              + reportTypeForThumbnail + "');\" onmouseover=\"chartView('" + reportTypeForThumbnail
                              + "');\" />";

                     htmlReport.setThumbnail(htmlThumbnail);
                  }
               }
            }
         }
      }
   }

   private UniversalReport prepareUniversalReport (HttpServletRequest request, String xmlReportData)
            throws HandlerException {
      try {
         // transform the xml report data to universal report
         UniversalReport universalReport = PresentationTransformHelper
                  .getUniversalXMLTranformedToUniversalReport(xmlReportData);

         // obtain the source value from the request and set into the report header object
         universalReport.getHeader().setSource((String) request.getAttribute(ReportingConstants.CHART_SOURCE_KEY));
         return universalReport;
      } catch (UniversalDataTransformationException e) {
         logger.error("Error occured while universal transformation for AGGQueryID: "
                  + request.getParameter("agQueryIdList") + "\n");
         throw new HandlerException(e.getCode(), e);
      }
   }

   private List<ReportType> getSelectedReportTypes (String chartTypes) {
      List<ReportType> reportTypes = new ArrayList<ReportType>();
      List<String> strReportTypeValues = Arrays.asList(chartTypes.split(ReportingConstants.COMMA));
      for (String strReportTypeValue : strReportTypeValues) {
         reportTypes.add(ReportType.getType(new Integer(strReportTypeValue)));
      }
      return reportTypes;
   }

   public void setApplicationContext (ApplicationContext arg0) throws BeansException {
      springAppContext = arg0;
   }

   public ApplicationContext getSpringAppContext () {
      return springAppContext;
   }

   public void setSpringAppContext (ApplicationContext springAppContext) {
      this.springAppContext = springAppContext;
   }

   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
   }

}
