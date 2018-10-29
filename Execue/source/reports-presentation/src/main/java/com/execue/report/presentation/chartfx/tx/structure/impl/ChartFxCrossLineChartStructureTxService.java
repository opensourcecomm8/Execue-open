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


package com.execue.report.presentation.chartfx.tx.structure.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.reports.Row;
import com.execue.core.common.bean.reports.prsntion.UniversalColumn;
import com.execue.core.common.bean.reports.prsntion.UniversalColumnData;
import com.execue.core.common.bean.reports.prsntion.UniversalMember;
import com.execue.core.common.bean.reports.prsntion.UniversalReport;
import com.execue.core.common.bean.reports.prsntion.UniversalValue;
import com.execue.core.common.bean.reports.view.ChartFXReport;
import com.execue.core.common.bean.reports.view.Column;
import com.execue.core.common.bean.reports.view.ColumnData;
import com.execue.core.common.bean.reports.view.Columns;
import com.execue.exception.ReportStructureTxServiceException;
import com.execue.report.presentation.tx.structure.impl.ChartFxBaseChartStructureTxService;

public class ChartFxCrossLineChartStructureTxService extends ChartFxBaseChartStructureTxService {

   @Override
   public ChartFXReport transformStructure (UniversalReport universalReport) throws ReportStructureTxServiceException {
      ChartFXReport chartFXReport = super.transformStructure(universalReport);

      try {
         List<String> acrossMemberList = new ArrayList<String>();
         int groupNodeIndex = 0;
         int acrossNodeIndex = 0;

         List<String> measureIdList = new ArrayList<String>();
         String acrossId = "";
         List<Column> columnList = new ArrayList<Column>();

         // generating the columns for chartfx report
         int tempCounter = 0;
         Column groupColumn = null;

         for (UniversalColumn universalColumns : universalReport.getHeader().getColumns()) {
            if (null != universalColumns.getGroupby()) {
            	groupColumn = new Column();
               groupColumn.setTYPE("String");
               groupColumn.setNAME(universalColumns.getId());
               groupColumn.setDESCRIPTION(universalColumns.getDesc());
               groupNodeIndex = tempCounter;
            }
            if (null != universalColumns.getAcross()) {
               for (UniversalMember member : universalColumns.getMembers()) {
                  acrossMemberList.add(member.getDesc());
               }
               acrossId = universalColumns.getId();
               acrossNodeIndex = tempCounter;
            }
            if (acrossMemberList.size() > 0
                     && (universalColumns.getCtype().equalsIgnoreCase("MEASURE") || universalColumns.getCtype()
                              .equalsIgnoreCase("ID")
                              && "MEASURE".equalsIgnoreCase(universalColumns.getPlotAs()))) {
               for (int columnCntr = 0; columnCntr < acrossMemberList.size(); columnCntr++) {
                  Column column = new Column();
                  column.setTYPE("Double");
                  String name = universalColumns.getId() + "_" + acrossId + "_" + columnCntr;
                  column.setNAME(name);
                  column.setDESCRIPTION(universalColumns.getDesc() + "~" + acrossMemberList.get(columnCntr));
                  columnList.add(column);
               }
               measureIdList.add(universalColumns.getId());
            }
            tempCounter++;
         }

         // Add the group column to the first always
         columnList.add(0, groupColumn);

         // generating the rows for chartfx report
         List<Row> rowList = new ArrayList<Row>();
         int seriesCntr = 0;

         for (UniversalColumn groupByColumn : universalReport.getHeader().getColumns()) {
            if (null != groupByColumn.getGroupby()) {
               for (UniversalMember groupBymember : groupByColumn.getMembers()) {
                  ColumnData columnData = new ColumnData();
                  columnData.setNAME(groupByColumn.getId());
                  columnData.setVALUE(groupBymember.getDesc());

                  List<ColumnData> columnDataList = new ArrayList<ColumnData>();
                  columnDataList.add(columnData);

                  for (int measureCntr = 0; measureCntr < measureIdList.size(); measureCntr++) {
                     for (int crossCntr = 0; crossCntr < acrossMemberList.size(); crossCntr++) {
                        for (UniversalValue value : universalReport.getData().getValues()) {

                           if (value.getColumndata().get(groupNodeIndex).getValue().equalsIgnoreCase(
                                    groupBymember.getDesc())
                                    && value.getColumndata().get(acrossNodeIndex).getValue().equalsIgnoreCase(
                                             acrossMemberList.get(crossCntr))) {

                              for (UniversalColumnData valueColumnData : value.getColumndata()) {
                                 if (valueColumnData.getName().equalsIgnoreCase(measureIdList.get(measureCntr))) {
                                    if (!"N/A".equalsIgnoreCase(valueColumnData.getValue())) {
                                       ColumnData valueColData = new ColumnData();
                                       String name = valueColumnData.getName() + "_" + acrossId + "_" + seriesCntr;
                                       valueColData.setNAME(name);
                                       valueColData.setVALUE(valueColumnData.getValue());
                                       columnDataList.add(valueColData);
                                    }
                                    seriesCntr++;
                                 }
                              }
                           }
                        }
                     }
                     seriesCntr = 0;
                  }

                  Row row = new Row();
                  row.setCOLUMNDATA(columnDataList);
                  rowList.add(row);
               }
            }
         }

         Columns columns = new Columns();
         columns.setCOLUMN(columnList);

         chartFXReport = new ChartFXReport();
         chartFXReport.setREPORTTYPES(universalReport.getHeader().getReporttypes());
         chartFXReport.setTITLE(universalReport.getHeader().getTITLE());
         chartFXReport.setSOURCE(universalReport.getHeader().getSource());
         chartFXReport.setCOLUMNS(columns);
         chartFXReport.setROW(rowList);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return chartFXReport;
   }
}
