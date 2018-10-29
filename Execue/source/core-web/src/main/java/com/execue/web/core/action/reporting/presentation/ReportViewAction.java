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


/***********************************************************************************************************************
 * <ExcCueLite - Reporting views> www.vbsoftindia.com FILENAME :ReportView.java DEPENDENCIES:struts.xml KNOWN ISSUES:
 * CREATED ON: 20 February,2009
 **********************************************************************************************************************/

package com.execue.web.core.action.reporting.presentation;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.qdata.QDataAggregatedQuery;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.common.type.ReportType;
import com.execue.handler.bean.presentation.FormBeans;
import com.execue.handler.presentation.IPresentationHandler;
import com.execue.qdata.service.IQueryDataService;
import com.execue.report.presentation.tx.IGrid;
import com.execue.reporting.presentation.bean.PresentationTransformData;
import com.execue.reporting.presentation.service.IConvertionForChart;
import com.execue.reporting.presentation.service.IPresentationTransformService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * ReportView Class
 * 
 * @author Ankur M. Bhalodia
 */
public class ReportViewAction extends ActionSupport {

   private Long                          assetId;
   private Long                          queryId;
   private Long                          businessQueryId;
   private String                        source;
   private List<Long>                    reportTypes;
   private IQueryDataService             queryDataService;
   private IConvertionForChart           convertionForChart;
   private IPresentationTransformService gridTransform;
   private IPresentationHandler          presentationHandler;
   private IGrid                         grid;
   private String                        chartTypeString;
   private String                        reportTitle;
   private FormBeans                     formBean;
   private String                        chartTypeForm;
   private String                        xmlReportData;
   private boolean                       hasDetailReport;
   private boolean                       printDetailReportMessage;
   private String                        reportType;
   private String                        agQueryIdList;

   /**
    * Creates a String
    * 
    * @throws java.lang.Exception
    */
   @Override
   public String execute () throws Exception {
      String filePath = "", filePath1 = "";
      String returnType = "";
      formBean = new FormBeans();

      PresentationTransformData reportData = gridTransform.getReport(queryId, assetId, businessQueryId,
               AggregateQueryType.BUSINESS_SUMMARY);
      xmlReportData = reportData.getXmlData();

      // call for getting reportTitle
      reportTitle = convertionForChart.getReportTitle(xmlReportData);
      // ---- call for get ReportType
      chartTypeString = convertionForChart.getReportType(xmlReportData);
      if (null != reportType) {
         chartTypeString = reportType + ",";
         if (!reportType.equalsIgnoreCase("0"))
            hasDetailReport = false;
      }

      setChartType(chartTypeString);
      String[] chartType = chartTypeString.split(",");
      PresentationTransformData transformedData = null;
      if (hasDetailReport) {
         int iRequestedPage = 1;
         int iPageCount = 100;
         try {
            List<Long> aggregatedQueryIds = new ArrayList<Long>();
            if (agQueryIdList.contains(",")) {
               String[] stringList = agQueryIdList.split(",");
               for (String item : stringList)
                  aggregatedQueryIds.add(Long.parseLong(item));
            } else {
               aggregatedQueryIds.add(Long.parseLong(agQueryIdList));
            }

            for (long aggQueryId : aggregatedQueryIds) {
               QDataAggregatedQuery aggregateQuery = queryDataService.getAggregatedQueryById(aggQueryId);
               if (aggregateQuery.getType().equals(AggregateQueryType.DETAILED_SUMMARY)) {
                  // lazzy loading of the data
                  presentationHandler.processReportRequest(aggQueryId);
               }
            }

            transformedData = gridTransform.getReport(iRequestedPage, iPageCount, queryId, assetId, businessQueryId,
                     AggregateQueryType.DETAILED_SUMMARY);
            if (transformedData.getPageCount() / iPageCount > 0) {
               printDetailReportMessage = true;
            } else {
               printDetailReportMessage = false;
            }
            String chartTypeString = convertionForChart.getReportType(transformedData.getXmlData());
            /*
             * System.out.println(" ReportType.GroupTable.getValue().toString() " +
             * ReportType.GroupTable.getValue().toString()); System.out.println("###################");
             * System.out.println(transformedData.getXmlData()); System.out.println("###################");
             */
            // call the HTML Group Grid if the DetailGroupTable is selected else the normal grid
            if (chartTypeString.contains(ReportType.DetailGroupTable.getValue().toString())) {
               // Getting all the records instead of the first 100 as in the case of Data browser
               transformedData = gridTransform.getReport(queryId, assetId, businessQueryId,
                        AggregateQueryType.DETAILED_SUMMARY);
               formBean.setDetailGroupReport(grid.getHtmlGroupGrid(source + "~" + reportTitle, transformedData
                        .getXmlData()));
               // System.out.println("#####Detail Group Table#####");
               // System.out.println(transformedData.getXmlData());
               // System.out.println("###################");
            } else {
               formBean.setDetailReport(grid.getHtmlGrid(source + "~" + reportTitle, transformedData.getXmlData()));
            }
         } catch (Exception e) {
            e.printStackTrace();
            hasDetailReport = false;
            printDetailReportMessage = false;
         }
      }

      for (int cntr = 0; cntr < chartType.length; cntr++) {
         returnType = "success";
         if (chartType[cntr].equals("7") || chartType[cntr].equals("8")) {
            // ----- XML convertion for Cross Line Chart
            String[] crossLine = convertionForChart.crossLineChartXMLConvertor(null, xmlReportData);
            filePath1 = convertionForChart.crossChartXMLConvertor(null, xmlReportData);
            formBean.setCrossLinePath(filePath1);
            formBean.setCrossLineChart(crossLine);
         } else if (chartType[cntr].equals("18") || chartType[cntr].equals("19")) {
            // ----- XML convertion for CM_Multi_Line Chart & CM_Multi_Bar chart
            filePath = convertionForChart.cmMultiChartXMLConvertor(null, xmlReportData);
            formBean.setCmMultiLinePath(filePath);
         } else if (chartType[cntr].equals("1")) {
            // ----- XML convertion for Simple Gride
            String htmlGrid = grid.getHtmlGrid(source + "~" + reportTitle, xmlReportData);
            formBean.setGridString(htmlGrid);
         } else if (chartType[cntr].equals("12") || chartType[cntr].equals("15") || chartType[cntr].equals("13")
                  || chartType[cntr].equals("16")) {
            // ----- XML convertion for Simple Gride
            String[] cMultiArry = convertionForChart.cMultiChart(xmlReportData);
            formBean.setCMultiArry(cMultiArry);
            filePath = convertionForChart.convertionForChart(null, xmlReportData);
            formBean.setPath(filePath);
         } else if (chartType[cntr].equals("5")) {
            // ----- XML convertion for Gride Gride
            String htmlGroupGrid = grid.getHtmlGroupGrid(source + "~" + reportTitle, xmlReportData);
            formBean.setGroupByGrid(htmlGroupGrid);
         } else if (chartType[cntr].equals("6")) {
            // ----- XML convertion for Across Gride
            String acrossGride = grid.getHtmlAcrossGrid(source + "~" + reportTitle, xmlReportData);
            formBean.setAcrossGrid(acrossGride);
         } else if (chartType[cntr].equals("4"))// Added by jaimin
         {
            String pivotTable = convertionForChart.getPivotXMLData(null, xmlReportData);
            formBean.setPivotTable(pivotTable);

         } else if (!chartType[cntr].equals("98") && !chartType[cntr].equals("99") && !chartType[cntr].equals("20")) {
            // ----- XML convertion for Chart
            filePath = convertionForChart.convertionForChart(null, xmlReportData);
            formBean.setPath(filePath);
         }
      }
      // }
      return returnType;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public Long getQueryId () {
      return queryId;
   }

   public void setQueryId (Long queryId) {
      this.queryId = queryId;
   }

   public List<Long> getReportTypes () {
      return reportTypes;
   }

   public void setReportTypes (List<Long> reportTypes) {
      this.reportTypes = reportTypes;
   }

   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
   }

   public IConvertionForChart getConvertionForChart () {
      return convertionForChart;
   }

   public void setConvertionForChart (IConvertionForChart convertionForChart) {
      this.convertionForChart = convertionForChart;
   }

   public IGrid getGrid () {
      return grid;
   }

   public void setGrid (IGrid grid) {
      this.grid = grid;
   }

   public String getChartTypeString () {
      return chartTypeString;
   }

   public void setChartTypeString (String chartTypeString) {
      this.chartTypeString = chartTypeString;
   }

   public FormBeans getFormBean () {
      return formBean;
   }

   public void setFormBean (FormBeans formBeans) {
      this.formBean = formBeans;
   }

   public void setChartType (String chartType) {
      this.chartTypeForm = chartType;
   }

   public String getChartType () {
      return chartTypeForm;
   }

   public IPresentationTransformService getGridTransform () {
      return gridTransform;
   }

   public void setGridTransform (IPresentationTransformService gridTransform) {
      this.gridTransform = gridTransform;
   }

   public String getXmlReportData () {
      return xmlReportData;
   }

   public void setXmlReportData (String xmlReportData) {
      this.xmlReportData = xmlReportData;
   }

   public String getReportTitle () {
      return reportTitle;
   }

   public void setReportTitle (String reportTitle) {
      this.reportTitle = reportTitle;
   }

   public Long getBusinessQueryId () {
      return businessQueryId;
   }

   public void setBusinessQueryId (Long businessQueryId) {
      this.businessQueryId = businessQueryId;
   }

   public String getSource () {
      return source;
   }

   public void setSource (String source) {
      this.source = source;
   }

   public IPresentationHandler getPresentationHandler () {
      return presentationHandler;
   }

   public void setPresentationHandler (IPresentationHandler presentationHandler) {
      this.presentationHandler = presentationHandler;
   }

   public boolean isHasDetailReport () {
      return hasDetailReport;
   }

   public void setHasDetailReport (boolean hasDetailReport) {
      this.hasDetailReport = hasDetailReport;
   }

   public boolean isPrintDetailReportMessage () {
      return printDetailReportMessage;
   }

   public void setPrintDetailReportMessage (boolean printDetailReportMessage) {
      this.printDetailReportMessage = printDetailReportMessage;
   }

   public void setReportType (String reportType) {
      this.reportType = reportType;
   }

   public String getReportType () {
      return reportType;
   }

   public String getAgQueryIdList () {
      return agQueryIdList;
   }

   public void setAgQueryIdList (String agQueryIdList) {
      this.agQueryIdList = agQueryIdList;
   }
}