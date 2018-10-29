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


package com.execue.handler.reports.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import com.execue.core.common.bean.ReportListWrapper;
import com.execue.core.common.bean.qdata.QDataCachedReportResults;
import com.execue.core.common.bean.reports.ReportingConstants;
import com.execue.core.common.bean.reports.prsntion.ReportWrapper;
import com.execue.core.common.bean.reports.prsntion.UniversalColumn;
import com.execue.core.common.bean.reports.prsntion.UniversalMember;
import com.execue.core.common.bean.reports.prsntion.UniversalReport;
import com.execue.core.common.bean.reports.view.ChartFXReport;
import com.execue.core.common.type.ReportType;
import com.execue.core.common.util.ChartFXReportParser;
import com.execue.core.common.util.ExeCueXMLUtils;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.exception.PresentationExceptionCodes;
import com.execue.exception.ReportDataMassageServiceException;
import com.execue.exception.ReportDataTxServiceException;
import com.execue.exception.ReportStructureTxServiceException;
import com.execue.exception.ReportViewTxServiceException;
import com.execue.handler.reports.IReportHandler;
import com.execue.qdata.exception.QueryDataException;
import com.execue.report.configuration.service.IPresentationConfigurationService;
import com.execue.report.presentation.tx.structure.IChartFxReportStructureTxService;
import com.execue.report.view.chartfx.tx.IChartFxReportViewTxService;
import com.softwarefx.StringAlignment;
import com.softwarefx.chartfx.server.ChartServer;
import com.softwarefx.chartfx.server.DockArea;
import com.softwarefx.chartfx.server.FieldMap;
import com.softwarefx.chartfx.server.FieldUsage;
import com.softwarefx.chartfx.server.TitleDockable;
import com.softwarefx.chartfx.server.dataproviders.XmlDataProvider;

public abstract class ChartFxChartHandler extends ReportHandler implements IReportHandler {

   private IPresentationConfigurationService presentationConfigurationService;

   @Override
   public IPresentationConfigurationService getPresentationConfigurationService () {
      return presentationConfigurationService;
   }

   @Override
   public void setPresentationConfigurationService (IPresentationConfigurationService presentationConfigurationService) {
      this.presentationConfigurationService = presentationConfigurationService;
   }

   public void handleReport (UniversalReport universalReport, ReportListWrapper reportsList,
            HttpServletRequest request, HttpServletResponse response) throws HandlerException {

      String chartXml = "";
      long aggregateQueryId = 0;
      int totalCharts = reportsList.getChartFxReports().size();
      XmlDataProvider cfxXMLCurve = new XmlDataProvider();

      ReportWrapper chartFXReport = reportsList.getChartFxReports().get(totalCharts - 1);
      ReportType reportType = ReportType.valueOf(chartFXReport.getId());
      String agQueryIdList = request.getParameter("agQueryIdList");

      if (agQueryIdList.contains(",")) {
         String[] aggQueryIdList = agQueryIdList.split(",");
         aggregateQueryId = Long.parseLong(aggQueryIdList[0]);
      } else {
         aggregateQueryId = Long.parseLong(agQueryIdList);
      }

      boolean dbSavePath = getPresentationConfigurationService().isSaveReportsToDB();
      QDataCachedReportResults cachedReportResults = null;
      if (dbSavePath) {
         try {
            cachedReportResults = getQueryDataService().getCachedReportResultsById(aggregateQueryId, reportType);
            if (cachedReportResults != null && cachedReportResults.getId() > 0) {
               String imageAction = getPresentationConfigurationService().getImageRetrievalAction();
               chartFXReport.setReport(cachedReportResults.getMetaInfo());
               chartFXReport.setThumbnail(imageAction + cachedReportResults.getId());
            }
         } catch (QueryDataException e) {
            throw new HandlerException(PresentationExceptionCodes.DATABASE_RETRIVAL_FAILED, e);
         }
      }

      try {
         // if code reaches here means
         // 1. either the db save path is false
         // 2. or the request is for the 1st time
         if (cachedReportResults == null) {
            getReportDataTxService().transformData(universalReport);
            getReportDataMassageService().massageData(universalReport);

            ChartFXReport chartFxXML = ((IChartFxReportStructureTxService) getReportStructureTxService())
                     .transformStructure(universalReport);

            chartXml = ChartFXReportParser.generateXML(chartFxXML);
            chartXml = ExeCueXMLUtils.denormalizeDataSectionForChartFx(chartXml);

            ChartServer chartServer = new ChartServer(request.getSession().getServletContext(), request, response);
            String realPathForChartFx = request.getSession().getServletContext().getRealPath("/chartfx70");

            // Prepare the input map for report view tx service
            Map<String, Object> container = new HashMap<String, Object>();
            container.put(ReportingConstants.CHART_TITLE_KEY, chartFxXML.getTITLE());
            container.put(ReportingConstants.CHART_SOURCE_KEY, chartFxXML.getSOURCE());
            container.put(ReportingConstants.CHART_REAL_PATH, realPathForChartFx);
            container.put(ReportingConstants.CHART_SERVER_KEY, chartServer);
            int dimensionColumnIndex = 0;
            if (reportType.equals(ReportType.ScatterChart)) {
               List<String> columnNames = new ArrayList<String>();
               List<String> columnIndex = new ArrayList<String>();
               boolean isDimensionAvailable = false;
               int columnHeaderIndex = 0;
               for (UniversalColumn column : universalReport.getHeader().getColumns()) {
                  if (column.getCtype().equalsIgnoreCase("DIMENSION")) {
                     isDimensionAvailable = true;
                     dimensionColumnIndex = columnHeaderIndex;
                  } else if (column.getCtype().equalsIgnoreCase("MEASURE")) {
                     columnNames.add(column.getDesc());
                     columnIndex.add(column.getId());
                  }
                  columnHeaderIndex++;
               }
               if (columnIndex.size() == 2) {
                  chartServer.getAxisX().getTitle().setText(columnNames.get(1));
                  chartServer.getAxisY().getTitle().setText(columnNames.get(0));

                  chartServer.getDataSourceSettings().getFields().add(
                           new FieldMap(columnIndex.get(1), FieldUsage.XVALUE));
                  chartServer.getDataSourceSettings().getFields().add(
                           new FieldMap(columnIndex.get(0), FieldUsage.VALUE));

                  // Prepare the header for the main title
                  String mainTitle = chartFxXML.getTITLE();
                  while (ExecueCoreUtil.isNotEmpty(mainTitle)) {

                     int maxLenghtForTitleLine = 80;
                     int titleEndIndex = mainTitle.length() >= maxLenghtForTitleLine ? (mainTitle.indexOf(" ",
                              maxLenghtForTitleLine) > -1 ? mainTitle.indexOf(" ", maxLenghtForTitleLine) : mainTitle
                              .length()) : mainTitle.length();
                     String mainTitleSubString = titleEndIndex >= maxLenghtForTitleLine ? mainTitle.substring(0,
                              titleEndIndex).trim() : mainTitle;

                     TitleDockable mainTitleHeader = getTitleDockable(mainTitleSubString, DockArea.TOP,
                              StringAlignment.CENTER);
                     chartServer.getTitles().add(mainTitleHeader);

                     mainTitle = titleEndIndex >= maxLenghtForTitleLine ? mainTitle.substring(titleEndIndex).trim()
                              : "";
                  }

                  // Prepare the header for the sub title
                  TitleDockable subTitleHeader = getTitleDockable(chartFxXML.getSUBTITLE(), DockArea.TOP,
                           StringAlignment.CENTER);

                  chartServer.getTitles().add(subTitleHeader);
               }
               if (isDimensionAvailable) {
                  // start of chart title footer
                  List<String> conceptDetails = new ArrayList<String>();

                  for (UniversalMember member : universalReport.getHeader().getColumns().get(dimensionColumnIndex)
                           .getMembers()) {
                     conceptDetails.add(member.getDisplay());
                  }

                  // Prepare the footer
                  String maintTitleFooter = "** The above points are derived based on range bands of "
                           + universalReport.getHeader().getColumns().get(dimensionColumnIndex).getDesc() + " **";
                  TitleDockable footer = getTitleDockable(maintTitleFooter, DockArea.BOTTOM, StringAlignment.CENTER);
                  chartServer.getTitles().add(footer);

                  // Prepare the footer having all the dimension concept details
                  String titleFotter = "";
                  for (int i = 1; i <= conceptDetails.size(); i++) {
                     titleFotter = titleFotter + "  [ " + conceptDetails.get(i - 1) + " ]";

                     if (titleFotter.length() >= 80) {
                        footer = getTitleDockable(titleFotter.trim(), DockArea.BOTTOM, StringAlignment.CENTER);
                        chartServer.getTitles().add(footer);
                        titleFotter = "";
                     }
                  }
                  if (ExecueCoreUtil.isNotEmpty(titleFotter)) {
                     footer = getTitleDockable(titleFotter.trim(), DockArea.BOTTOM, StringAlignment.CENTER);
                     chartServer.getTitles().add(footer);
                  }
                  // end of footer
               }
            }

            // Prepare the chart xml object and set it as the source of data in the chat server
            cfxXMLCurve.loadXML(chartXml);
            chartServer.getDataSourceSettings().setDataSource(cfxXMLCurve);

            if (reportType.equals(ReportType.ScatterChart)) {
               chartServer.getAxisX().setMin(chartFxXML.getMinXAxisValue());
               chartServer.getAxisY().setMin(chartFxXML.getMinYAxisValue());
            }

            // Prepare the view data of the chart server
            ((IChartFxReportViewTxService) getReportViewTxService()).prepareViewData(container);

            // Apply the view rules on the chart server properties
            ((IChartFxReportViewTxService) getReportViewTxService()).applyViewRules(container);

            // generate static images instead of chartfx default generated images.
            ByteArrayOutputStream imgMapStream = new ByteArrayOutputStream();
            OutputStreamWriter imgMap = new OutputStreamWriter(imgMapStream);
            ByteArrayOutputStream htmlTagStream = new ByteArrayOutputStream();
            OutputStreamWriter htmlTag = new OutputStreamWriter(htmlTagStream);
            OutputStream contentStream = null;
            String replaceSrcString = "";

            try {
               if (dbSavePath) {
                  cachedReportResults = new QDataCachedReportResults();
                  contentStream = new ByteArrayOutputStream();

                  // save the report in db
                  cachedReportResults.setReportType(reportType);
                  long storedReportId = getQueryDataService().storeCachedReportResults(cachedReportResults,
                           aggregateQueryId);

                  // Replace the Image Source (src="") with the name and id of the image from db
                  String imageAction = getPresentationConfigurationService().getImageRetrievalAction();
                  replaceSrcString = imageAction + storedReportId;

               } else {
                  // generate static images instead of chartfx default generated images.
                  String chartImagePath = getPresentationConfigurationService().getImageLocation() + aggregateQueryId
                           + "_" + totalCharts + ".jpg";
                  String chartImageFilePath = request.getSession().getServletContext().getRealPath("/") + "/"
                           + chartImagePath;
                  contentStream = new FileOutputStream(chartImageFilePath, false);
                  replaceSrcString = chartImagePath;
               }

               chartServer.renderToStream(contentStream, imgMap, htmlTag);
               contentStream.flush();
               imgMap.flush();
               htmlTag.flush();

               String sMap = imgMapStream.toString();
               String sTag = htmlTagStream.toString();

               String newImgTag = sTag.replaceAll("src=\"\"", "src=\"" + request.getContextPath() + "/"
                        + replaceSrcString + "\"");

               if (dbSavePath) {
                  // issue a subsequent update call to the same row.

                  // cachedReportResults.setReportData(Hibernate.createBlob(new
                  // ByteArrayInputStream(((ByteArrayOutputStream) contentStream).toByteArray())));
                  ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                           ((ByteArrayOutputStream) contentStream).toByteArray());
                  byte[] reportDataByteArray = new byte[byteArrayInputStream.available()];
                  byteArrayInputStream.read(reportDataByteArray);
                  Blob reportData = new SerialBlob(reportDataByteArray);
                  cachedReportResults.setReportData(reportData);

                  cachedReportResults.setMetaInfo(sMap + newImgTag);
                  getQueryDataService().updateCachedReportResults(cachedReportResults);
               }

               chartFXReport.setReport(sMap + newImgTag);
               chartFXReport.setThumbnail(replaceSrcString);
            } catch (FileNotFoundException foe) {
               foe.printStackTrace();
            } catch (SerialException se) {
               se.printStackTrace();
            } catch (SQLException sqle) {
               sqle.printStackTrace();
            } finally {
               contentStream.close();
               imgMap.close();
               htmlTag.close();
            }
         }
      } catch (IOException e) {
         throw new HandlerException(PresentationExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      } catch (QueryDataException e) {
         throw new HandlerException(PresentationExceptionCodes.DATABASE_RETRIVAL_FAILED, e);
      } catch (ReportViewTxServiceException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (ReportDataTxServiceException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (ReportDataMassageServiceException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (ReportStructureTxServiceException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (Exception e) {
         throw new HandlerException(PresentationExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   /**
    * @param title
    * @return
    */
   private TitleDockable getTitleDockable (String text, DockArea dockArea, StringAlignment alignment) {
      TitleDockable titleDockable = new TitleDockable();
      titleDockable.setText(text);
      titleDockable.setDock(dockArea);
      titleDockable.setAlignment(alignment);
      return titleDockable;
   }

   private String getNewLine () {
      return "\r\n";
   }
}