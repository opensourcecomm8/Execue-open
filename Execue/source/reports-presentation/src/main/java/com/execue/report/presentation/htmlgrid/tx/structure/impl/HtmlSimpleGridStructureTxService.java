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


package com.execue.report.presentation.htmlgrid.tx.structure.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.execue.core.common.bean.reports.prsntion.UniversalColumn;
import com.execue.core.common.bean.reports.prsntion.UniversalColumnData;
import com.execue.core.common.bean.reports.prsntion.UniversalReport;
import com.execue.core.common.bean.reports.prsntion.UniversalValue;
import com.execue.exception.ReportStructureTxServiceException;
import com.execue.report.configuration.service.IPresentationConfigurationService;
import com.execue.report.presentation.tx.structure.impl.BaseGridStructureTxService;
import com.execue.reporting.presentation.helper.PresentationTransformHelper;

public class HtmlSimpleGridStructureTxService extends BaseGridStructureTxService {

   private IPresentationConfigurationService presentationConfigurationService;

   @Override
   public String transformStructure (UniversalReport universalReport) throws ReportStructureTxServiceException {
      super.transformStructure(universalReport);

      String htmlString = "";
      StringBuffer gridBuffer = new StringBuffer();
      int maxScale;

      NumberFormat numberFormat = new DecimalFormat("#,###.##");
      numberFormat.setMaximumFractionDigits(2);
      int numOfColumns = Integer.parseInt(universalReport.getHeader().getNumcolumns());
      try {
         gridBuffer.append("<table class='textDataTable'> ");
         gridBuffer.append("<tr><th valign='middle' align='center' style='width:auto' class='gridTitle' colspan='100'>"
                  + "<div style='width:auto'>" + universalReport.getHeader().getTITLE() + "</div></th></tr>");
         gridBuffer.append("<tr>");
         for (UniversalColumn columns : universalReport.getHeader().getColumns()) {
            gridBuffer.append("<th style='width:auto' align='center' valign='middle' class='textHeader' >"
                     + columns.getDesc() + "</th>");
         }
         gridBuffer.append("</tr>");

         for (UniversalValue values : universalReport.getData().getValues()) {
            gridBuffer.append("<tr>");
            for (UniversalColumn columns : universalReport.getHeader().getColumns()) {
               for (UniversalColumnData columnData : values.getColumndata()) {
                  if (columns.getId().equalsIgnoreCase(columnData.getName())) {
                     if (columns.getCtype().equalsIgnoreCase("dimension") || columns.getCtype().equalsIgnoreCase("id")) {
                        // -JM- 25-FEB-2011 : right align the ID column values which are numbers
                        String alignValue = "left";
                        String styleTag = "";
                        String dataItem = columnData.getValue().replaceAll("_", " ");
                        if (columns.getCtype().equalsIgnoreCase("id")
                                 && "measure".equalsIgnoreCase(columns.getPlotAs())) {
                           numberFormat = new DecimalFormat("#,###");
                           dataItem = numberFormat.format(Double.parseDouble(dataItem));
                           alignValue = "right";
                        }
                        if ("N/A".equalsIgnoreCase(dataItem)) {
                           dataItem = "";
                        }
                        if (numOfColumns <= getPresentationConfigurationService().getGridLayoutObject()
                                 .getGRIDPROPERTIES().getDimensionMinWidthForMinColumns()) {
                           int dataLength = dataItem.length();
                           if (dataLength <= 10) {
                              styleTag = "style='min-width: 100px;'";
                           } else if (dataLength <= 50) {
                              styleTag = "style='min-width: 150px;'";
                           } else if (dataLength <= 100) {
                              styleTag = "style='min-width: 450px;'";
                           }
                        }
                        gridBuffer.append("<td align='" + alignValue + "' " + styleTag + " class='textData'>"
                                 + dataItem + "</td>");
                     } else {
                        try {
                           String columnValue = "";
                           if (columns.getCtype().equalsIgnoreCase("measure")) {
                              maxScale = getPresentationConfigurationService().getGridLayoutObject()
                                       .getGRIDPROPERTIES().getMaxscaleValueToFormat();
                              NumberFormat numberFormater = PresentationTransformHelper
                                       .getNumberFormatWithTwoDigitPrecision(maxScale);
                              if (columns.getScale() >= getPresentationConfigurationService().getGridLayoutObject()
                                       .getGRIDPROPERTIES().getMinscaleValueToFormat()) {
                                 numberFormater.setMinimumFractionDigits(getPresentationConfigurationService()
                                          .getGridLayoutObject().getGRIDPROPERTIES().getMinscaleValueToFormat());
                              }
                              columnValue = numberFormater.format(Double.parseDouble(columnData.getValue()));
                           } else {
                              columnValue = columnData.getValue();
                           }
                           gridBuffer.append("<td class='textDataItem' align='right'>" + columnValue + "</td>");
                        } catch (NumberFormatException e) {
                           String dataItem = columnData.getValue().replaceAll("_", " ");
                           if ("N/A".equalsIgnoreCase(dataItem)) {
                              dataItem = "";
                           }
                           gridBuffer.append("<td class='textDataItem' align='right'>" + dataItem + "</td>");
                        }
                     }
                  }
               }
            }
            gridBuffer.append("</tr>");
         }
         gridBuffer.append("<tr><td class='textSource' colspan=100>Source: " + universalReport.getHeader().getSource()
                  + "</td></tr>");
         gridBuffer.append("<tr><td class='textSource' colspan=100>www.semantifi.com</td></tr>");
         gridBuffer.append("</table>");
      } catch (Exception e) {
         e.printStackTrace();
      }
      htmlString = gridBuffer.toString();
      return htmlString;
   }

   /**
    * @return the presentationConfigurationService
    */
   public IPresentationConfigurationService getPresentationConfigurationService () {
      return presentationConfigurationService;
   }

   /**
    * @param presentationConfigurationService the presentationConfigurationService to set
    */
   public void setPresentationConfigurationService (IPresentationConfigurationService presentationConfigurationService) {
      this.presentationConfigurationService = presentationConfigurationService;
   }

}
