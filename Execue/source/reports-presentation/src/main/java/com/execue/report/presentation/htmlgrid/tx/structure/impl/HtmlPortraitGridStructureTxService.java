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
import com.execue.core.util.ExecueCoreUtil;
import com.execue.exception.ReportStructureTxServiceException;
import com.execue.report.presentation.tx.structure.impl.BaseGridStructureTxService;

public class HtmlPortraitGridStructureTxService extends BaseGridStructureTxService {

   private static int          columnSeperateCount = 16;
   private static int          maxMemberCount      = 3;
   private static final String DIMENSION           = "DIMENSION";

   @Override
   public String transformStructure (UniversalReport universalReport) throws ReportStructureTxServiceException {
      super.transformStructure(universalReport);
      StringBuffer acrossGridBuffer = new StringBuffer();
      try {
         int numOfMeasures = Integer.parseInt(universalReport.getHeader().getNumcolumns()) - 1;
         boolean columnsToBeSeperated = numOfMeasures >= columnSeperateCount ? true : false;
         List<String> columnMemberList = new ArrayList<String>();
         Map<String, String> measureList = new HashMap<String, String>();

         acrossGridBuffer.append("<table class='textDataTable'>");
         acrossGridBuffer.append("<tr><th valign='middle' align='center' class='gridTitle' colspan='100'>"
                  + universalReport.getHeader().getTITLE() + "</th></tr>");

         for (UniversalColumn column : universalReport.getHeader().getColumns()) {
            if (DIMENSION.equalsIgnoreCase(column.getCtype())) {
               acrossGridBuffer.append("<tr><th class='textHeader'>" + column.getDesc() + "</th>");
               columnsToBeSeperated = (column.getMembers().size() <= maxMemberCount) && columnsToBeSeperated ? true
                        : false;
               appendGridHeader(column, columnMemberList, acrossGridBuffer);
               if (columnsToBeSeperated) {
                  acrossGridBuffer.append("<th width='10px;'></th>");
                  acrossGridBuffer.append("<th class='textHeader'>" + column.getDesc() + "</th>");
                  appendGridHeader(column, columnMemberList, acrossGridBuffer);
               }
            } else
               measureList.put(column.getId(), column.getDesc());
         }
         acrossGridBuffer.append("</tr>");

         for (int measureCntr = 0; measureCntr <= measureList.size(); measureCntr++) {
            String measureDisplayName = measureList.get("c" + measureCntr);
            if (ExecueCoreUtil.isNotEmpty(measureDisplayName)) {
               acrossGridBuffer.append("<tr>");
               acrossGridBuffer.append("<td class='textData'>" + measureDisplayName + "</td>");
               appendGridData(universalReport, measureList, measureDisplayName, acrossGridBuffer);
               if (columnsToBeSeperated) {
                  measureCntr++;
                  if (measureCntr <= measureList.size()) {
                     measureDisplayName = measureList.get("c" + measureCntr);
                     acrossGridBuffer.append("<th width='10px;'></th>");
                     acrossGridBuffer.append("<td class='textData'>" + measureDisplayName + "</td>");
                     appendGridData(universalReport, measureList, measureDisplayName, acrossGridBuffer);
                  }
                  if ((measureCntr - 1 == measureList.size()) && !(measureList.size() % 2 == 0)) {
                     acrossGridBuffer.append("<th width='10px;'></th>");
                     for (int memberCntr = 0; memberCntr <= (columnMemberList.size() / 2); memberCntr++) {
                        acrossGridBuffer.append("<td class='textDataItem' align='right'></td>");
                     }
                  }
               }
               acrossGridBuffer.append("</tr>");
            }
         }
         acrossGridBuffer.append("<tr><td class='textSource' colspan=100>Source: "
                  + universalReport.getHeader().getSource() + "</td></tr>");
         acrossGridBuffer.append("<tr><td class='textSource' colspan=100>www.semantifi.com</td></tr>");
         acrossGridBuffer.append("</table>");
      } catch (Exception e) {
         e.printStackTrace();
      }
      return acrossGridBuffer.toString();
   }

   private void appendGridHeader (UniversalColumn column, List<String> acrossMemberList, StringBuffer acrossGridBuffer) {
      for (UniversalMember members : column.getMembers()) {
         acrossMemberList.add(members.getDesc());
         acrossGridBuffer.append("<th class='textHeader' align='center'>" + members.getDesc() + "</th>");
      }
   }

   private void appendGridData (UniversalReport universalReport, Map<String, String> measureList,
            String measureDisplayName, StringBuffer acrossGridBuffer) {
      for (UniversalColumn universalColumns : universalReport.getHeader().getColumns()) {
         if (DIMENSION.equalsIgnoreCase(universalColumns.getCtype())) {
            for (UniversalMember member : universalColumns.getMembers()) {
               for (UniversalValue value : universalReport.getData().getValues()) {
                  boolean isValueOfMember = false;
                  if (value.getColumndata().get(0).getName().equalsIgnoreCase(universalColumns.getId())
                           && value.getColumndata().get(0).getValue().equalsIgnoreCase(member.getDesc())) {
                     isValueOfMember = true;
                  }
                  for (int colCntr = 0; colCntr < value.getColumndata().size(); colCntr++) {
                     if (isValueOfMember) {

                        NumberFormat numberFormat = new DecimalFormat("#,###.##");
                        numberFormat.setMaximumFractionDigits(2);
                        String measureId = measureList.get(value.getColumndata().get(colCntr).getName());
                        boolean isSameMeasure = (ExecueCoreUtil.isNotEmpty(measureId) && measureId
                                 .equalsIgnoreCase(measureDisplayName)) ? true : false;

                        if (isSameMeasure) {
                           String dataItem = value.getColumndata().get(colCntr).getValue();
                           try {
                              acrossGridBuffer.append("<td class='textDataItem' align='right'>"
                                       + numberFormat.format(Double.parseDouble(dataItem)) + "</td>");
                           } catch (NumberFormatException e) {
                              if ("N/A".equalsIgnoreCase(dataItem))
                                 dataItem = "";
                              acrossGridBuffer.append("<td class='textDataItem' align='right'>" + dataItem + "</td>");
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
