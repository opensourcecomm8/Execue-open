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

public class HtmlGroupByGridStructureTxService extends BaseGridStructureTxService {

   private IPresentationConfigurationService presentationConfigurationService;

   public String transformStructure (UniversalReport universalReport) throws ReportStructureTxServiceException {
      super.transformStructure(universalReport);
      StringBuffer groupGridBuffer = new StringBuffer();
      Map<String, String> plotAsMap = new HashMap<String, String>();
      try {
         int groupNodeIndex = 0;
         int acrossNodeIndex = 0;
         List<String> groupByMemberList = new ArrayList<String>();
         List<String> acrossMemberList = new ArrayList<String>();
         List<Integer> columnScales = new ArrayList<Integer>();
         groupGridBuffer.append("<table class='textDataTable'>");
         groupGridBuffer
                  .append("<tr><th valign='middle' style='width:auto' align='center' class='gridTitle' colspan='100'>"
                           + universalReport.getHeader().getTITLE() + "</th></tr>");
         groupGridBuffer.append("<tr>");
         int tempCounter = 0;
         for (UniversalColumn columns : universalReport.getHeader().getColumns()) {
            if (null != columns.getGroupby()) {
               groupGridBuffer
                        .append("<th align='center' style='width:auto' valign='middle' width='104' height='24' class='textHeader' >"
                                 + columns.getDesc() + "</th>");
               for (UniversalMember members : columns.getMembers()) {
                  String memberDesc = members.getDesc().trim();
                  groupByMemberList.add(memberDesc);
               }
               groupNodeIndex = tempCounter;
            }
            tempCounter++;
         }
         tempCounter = 0;
         for (UniversalColumn universalColumn : universalReport.getHeader().getColumns()) {
            if (null != universalColumn.getAcross()) {
               groupGridBuffer
                        .append("<th align='center' style='width:auto' valign='middle' width='104' height='24' class='textHeader' >"
                                 + universalColumn.getDesc() + "</th>");
               for (UniversalMember members : universalColumn.getMembers()) {
                  String memberDesc = members.getDesc().trim();
                  acrossMemberList.add(memberDesc);
               }
               acrossNodeIndex = tempCounter;
            } else if (!(universalColumn.getCtype().equalsIgnoreCase("DIMENSION") || (universalColumn.getCtype()
                     .equalsIgnoreCase("ID") && "DIMENSION".equalsIgnoreCase(universalColumn.getPlotAs())))) {
               groupGridBuffer
                        .append("<th align='center' style='width:auto' valign='middle' width='104' height='24' class='textHeader' >"
                                 + universalColumn.getDesc() + "</th>");
               columnScales.add(universalColumn.getScale());
            }
            plotAsMap.put(universalColumn.getId(), universalColumn.getPlotAs());
            tempCounter++;
         }
         groupGridBuffer.append("</tr>");

         for (String groupByMember : groupByMemberList) {
            groupGridBuffer.append("<tr>");
            // put the 1st entry for groupby member
            String groupByMemberItem = groupByMember;
            if ("N/A".equalsIgnoreCase(groupByMemberItem) || "null".equalsIgnoreCase(groupByMemberItem))
               groupByMemberItem = "";
            groupGridBuffer.append("<td rowspan='" + acrossMemberList.size() + "' class='textData'>"
                     + groupByMemberItem + "</td>");
            for (String acrossMember : acrossMemberList) {
               for (UniversalValue value : universalReport.getData().getValues()) {
                  if (value.getColumndata().get(groupNodeIndex).getValue().trim().equals(groupByMember)
                           && value.getColumndata().get(acrossNodeIndex).getValue().trim().equals(acrossMember)) {
                     for (int colCntr = 0; colCntr < value.getColumndata().size(); colCntr++) {
                        String ctype = value.getColumndata().get(colCntr).getCtype();
                        String plotAsValue = plotAsMap.get(value.getColumndata().get(colCntr).getName());
                        if (colCntr == groupNodeIndex) {
                           colCntr++;
                           ctype = value.getColumndata().get(colCntr).getCtype();
                        }
                        int measureIndex = 0;
                        String alignValue = "left";
                        String cellValue = value.getColumndata().get(colCntr).getValue();
                        if ("N/A".equalsIgnoreCase(cellValue) || "null".equalsIgnoreCase(cellValue)) {
                           cellValue = "";
                        } else {
                           if ("MEASURE".equalsIgnoreCase(ctype)) {
                              cellValue = applyNumberFormat(cellValue, measureIndex, columnScales, true);
                              alignValue = "right";
                              measureIndex++;
                           } else if ("ID".equalsIgnoreCase(ctype) && "MEASURE".equalsIgnoreCase(plotAsValue)) {
                              cellValue = applyNumberFormat(cellValue, measureIndex, columnScales, false);
                              alignValue = "right";
                           }
                        }
                        groupGridBuffer.append("<td class='textDataItem' align='" + alignValue + "'>" + cellValue
                                 + "</td>");
                     }
                     groupGridBuffer.append("</tr>");
                  }
               }
            }
         }
         groupGridBuffer.append("<tr><td class='textSource' colspan=100>Source: "
                  + universalReport.getHeader().getSource() + "</td></tr>");
         groupGridBuffer.append("<tr><td class='textSource' colspan=100>www.semantifi.com</td></tr>");
         groupGridBuffer.append("</table>");
      } catch (Exception e) {
         e.printStackTrace();
      }
      return groupGridBuffer.toString();
   }

   private String applyNumberFormat (String cellValue, int measureIndex, List<Integer> columnScales, boolean isMeasure) {
      NumberFormat numberFormat = null;
      int maxScale;
      try {
         if (isMeasure) {
            maxScale = getPresentationConfigurationService().getGridLayoutObject().getGRIDPROPERTIES()
                     .getMaxscaleValueToFormat();
            numberFormat = getNumberFormatWithTwoDigitPrecision(maxScale);
            numberFormat = getNumberFormatWithTwoDigitPrecision(maxScale);
            if (columnScales.get(measureIndex) >= getPresentationConfigurationService().getGridLayoutObject()
                     .getGRIDPROPERTIES().getMinscaleValueToFormat()) {
               numberFormat.setMinimumFractionDigits(getPresentationConfigurationService().getGridLayoutObject()
                        .getGRIDPROPERTIES().getMinscaleValueToFormat());
            }
            cellValue = numberFormat.format(Double.parseDouble(cellValue));
         } else {
            numberFormat = new DecimalFormat("#,###");
            cellValue = numberFormat.format(Double.parseDouble(cellValue));
         }
      } catch (NumberFormatException e) {
         e.printStackTrace();
         // do nothing
         // value is of id type or the measure value can't be converted into number
      }
      return cellValue;
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
