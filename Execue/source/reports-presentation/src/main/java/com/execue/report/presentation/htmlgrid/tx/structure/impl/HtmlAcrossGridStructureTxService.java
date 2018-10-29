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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.reports.prsntion.UniversalColumn;
import com.execue.core.common.bean.reports.prsntion.UniversalMember;
import com.execue.core.common.bean.reports.prsntion.UniversalReport;
import com.execue.core.common.bean.reports.prsntion.UniversalValue;
import com.execue.exception.ReportStructureTxServiceException;
import com.execue.report.configuration.service.IPresentationConfigurationService;
import com.execue.report.presentation.tx.structure.impl.BaseGridStructureTxService;

public class HtmlAcrossGridStructureTxService extends BaseGridStructureTxService {

   private IPresentationConfigurationService presentationConfigurationService;

   @Override
   public String transformStructure (UniversalReport universalReport) throws ReportStructureTxServiceException {
      StringBuilder gridHeader = new StringBuilder();
      StringBuilder acrossGridBody = new StringBuilder();
      StringBuilder gridFooter = new StringBuilder();
      Map<String, String> plotAsMap = new HashMap<String, String>();
      try {
         int numOfMeasures = Integer.parseInt(universalReport.getHeader().getNumcolumns()) - 2;
         List<String> acrossMemberList = new ArrayList<String>();
         List<String> groupByMemberList = new ArrayList<String>();
         List<String> measureList = new ArrayList<String>();
         List<Integer> columnScales = new ArrayList<Integer>();
         int acrossNodeIndex = 0;
         int groupNodeIndex = 0;
         int colSpan = 0;

         acrossGridBody.append("<tr><th style='white-space:nowrap;width:auto' class='textHeader'/>");
         int tempCounter = 0;
         for (UniversalColumn columns : universalReport.getHeader().getColumns()) {
            if (null != columns.getAcross()) {
               for (UniversalMember members : columns.getMembers()) {
                  String memberDesc = members.getDesc().trim();
                  if ("N/A".equalsIgnoreCase(memberDesc) || "null".equalsIgnoreCase(memberDesc)) {
                     memberDesc = "";
                  }
                  acrossMemberList.add(members.getDesc().trim());
                  acrossGridBody.append("<th style='white-space:nowrap;width:auto' colspan='" + numOfMeasures
                           + "' class='textHeader' align='center'>" + memberDesc + "</th>");
               }
               acrossNodeIndex = tempCounter;
               break;
            }
            tempCounter++;
         }
         acrossGridBody.append("</tr><tr>");
         tempCounter = 0;
         for (UniversalColumn universalColumn : universalReport.getHeader().getColumns()) {
            if (null != universalColumn.getGroupby()) {
               for (UniversalMember members : universalColumn.getMembers()) {
                  String memberDesc = members.getDesc().trim();
                  groupByMemberList.add(memberDesc);
               }
               acrossGridBody
                        .append("<th class='textHeader' align='center'><div style='white-space:nowrap;width:auto'>"
                                 + universalColumn.getDesc() + "</div></th>");
               groupNodeIndex = tempCounter;
            }
            if (universalColumn.getCtype().equalsIgnoreCase("MEASURE")
                     || universalColumn.getCtype().equalsIgnoreCase("ID")
                     && "MEASURE".equalsIgnoreCase(universalColumn.getPlotAs())) {
               measureList.add(universalColumn.getDesc());
               columnScales.add(universalColumn.getScale());
            }
            plotAsMap.put(universalColumn.getId(), universalColumn.getPlotAs());
            tempCounter++;
         }
         for (int crossCntr = 0; crossCntr < acrossMemberList.size(); crossCntr++) {
            for (int measureCntr = 0; measureCntr < measureList.size(); measureCntr++) {
               acrossGridBody
                        .append("<th class='textHeader' align='center'><div style='width:auto;white-space:nowrap;'>"
                                 + measureList.get(measureCntr) + "</div></th>");
            }
         }

         acrossGridBody.append("</tr>");

         // populating header info based on across members and measures list
         colSpan = acrossMemberList.size() * numOfMeasures + 1;
         gridHeader.append("<table class='textDataTable'>");
         gridHeader.append("<tr><th valign='middle' style='width:auto' align='center' class='gridTitle' colspan='"
                  + colSpan + "'>" + universalReport.getHeader().getTITLE() + "</th></tr>");
         // end of populating header info

         for (String groupByMember : groupByMemberList) {
            acrossGridBody.append("<tr>");
            if ("N/A".equalsIgnoreCase(groupByMember) || "null".equalsIgnoreCase(groupByMember)) {
               acrossGridBody.append("<td class='textData'></td>");
            } else {
               acrossGridBody.append("<td class='textData'>" + groupByMember + "</td>");
            }
            for (int crossCntr = 0; crossCntr < acrossMemberList.size(); crossCntr++) {
               for (UniversalValue value : universalReport.getData().getValues()) {
                  if (value.getColumndata().get(acrossNodeIndex).getValue().trim().equals(
                           acrossMemberList.get(crossCntr))
                           && value.getColumndata().get(groupNodeIndex).getValue().trim().equals(groupByMember)) {

                     for (int colCntr = 0; colCntr < value.getColumndata().size(); colCntr++) {
                        String columnIdentifier = value.getColumndata().get(colCntr).getName();
                        String ctype = value.getColumndata().get(colCntr).getCtype();
                        String alignValue = "left";
                        if (!"DIMENSION".equalsIgnoreCase(plotAsMap.get(columnIdentifier))) {
                           int measureIndex = 0;
                           String dataItem = value.getColumndata().get(colCntr).getValue();
                           if ("N/A".equalsIgnoreCase(dataItem) || "null".equalsIgnoreCase(dataItem)) {
                              dataItem = "";
                           } else {
                              if ("MEASURE".equalsIgnoreCase(ctype)) {
                                 alignValue = "right";
                                 dataItem = applyNumberFormat(dataItem, measureIndex, columnScales, true);
                              } else if ("ID".equalsIgnoreCase(ctype)
                                       && "MEASURE".equalsIgnoreCase(plotAsMap.get(columnIdentifier))) {
                                 dataItem = applyNumberFormat(dataItem, measureIndex, columnScales, false);
                                 alignValue = "right";
                              }
                           }
                           acrossGridBody.append("<td class='textDataItem' align='" + alignValue + "'>" + dataItem
                                    + "</td>");
                           measureIndex++;
                        }
                     }
                  }
               }
            }
            acrossGridBody.append("</tr>");
         }
         gridFooter.append("<tr><td class='textSource' colspan='" + colSpan + "'>Source: "
                  + universalReport.getHeader().getSource() + "</td></tr>");
         gridFooter.append("<tr><td class='textSource' colspan='" + colSpan + "'>www.semantifi.com</td></tr>");
         gridFooter.append("</table>");
      } catch (Exception e) {
         e.printStackTrace();
      }
      return gridHeader.toString() + acrossGridBody.toString() + gridFooter.toString();
   }

   private String applyNumberFormat (String dataItem, int measureIndex, List<Integer> columnScales, boolean isMeasure) {
      NumberFormat numberFormat = null;
      int maxScale;
      try {
         if (isMeasure) {
            maxScale = getPresentationConfigurationService().getGridLayoutObject().getGRIDPROPERTIES()
                     .getMaxscaleValueToFormat();
            numberFormat = getNumberFormatWithTwoDigitPrecision(maxScale);
            if (columnScales.get(measureIndex) >= getPresentationConfigurationService().getGridLayoutObject()
                     .getGRIDPROPERTIES().getMinscaleValueToFormat()) {
               numberFormat.setMinimumFractionDigits(getPresentationConfigurationService().getGridLayoutObject()
                        .getGRIDPROPERTIES().getMinscaleValueToFormat());
            }
            dataItem = numberFormat.format(Double.parseDouble(dataItem));
         } else {
            numberFormat = new DecimalFormat("#,###");
            dataItem = numberFormat.format(Double.parseDouble(dataItem));
         }
      } catch (NumberFormatException e) {
         e.printStackTrace();
         // do nothing
         // value is of id type or the measure value can't be converted into number
      }
      return dataItem;
   }

   private static NumberFormat getNumberFormatWithTwoDigitPrecision (int maxScale) {
      NumberFormat numberFormat = new DecimalFormat("#,###.##");
      numberFormat.setMaximumFractionDigits(maxScale);
      return numberFormat;
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
