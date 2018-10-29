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

public class ChartFxCrossStateMapsStructureTxService extends ChartFxBaseChartStructureTxService {

   @Override
   public ChartFXReport transformStructure (UniversalReport universalReport) throws ReportStructureTxServiceException {
      ChartFXReport chartFXReport = super.transformStructure(universalReport);

      try {

         chartFXReport = new ChartFXReport();
         chartFXReport.setREPORTTYPES(universalReport.getHeader().getReporttypes());
         chartFXReport.setTITLE(universalReport.getHeader().getTITLE());
         chartFXReport.setSOURCE(universalReport.getHeader().getSource());
         Columns columns = new Columns();
         List<String> acrossMemberList = new ArrayList<String>();
         List<String> measureIdList = new ArrayList<String>();
         String acrossId = "";
         List<Column> columnList = new ArrayList<Column>();

         // generating the columns for chartfx report
         for (UniversalColumn universalColumns : universalReport.getHeader().getColumns()) {
            if (null != universalColumns.getGroupby()) {
               Column column = new Column();
               column.setTYPE("String");
               column.setNAME(universalColumns.getId());
               column.setDESCRIPTION(universalColumns.getDesc());
               columnList.add(column);
            }
            if (null != universalColumns.getAcross()) {
               for (UniversalMember member : universalColumns.getMembers()) {
                  acrossMemberList.add(member.getDesc());
               }
               acrossId = universalColumns.getId();
            }
            if (acrossMemberList.size() > 0 && universalColumns.getCtype().equalsIgnoreCase("MEASURE")) {
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
         }
         columns.setCOLUMN(columnList);

         // generating the rows for chartfx report
         List<Row> rowList = new ArrayList<Row>();
         int seriesCntr = 0;
         for (UniversalColumn universalColumns : universalReport.getHeader().getColumns()) {
            if (null != universalColumns.getGroupby()) {
               for (UniversalMember member : universalColumns.getMembers()) {
                  Row row = new Row();
                  List<ColumnData> columnDataList = new ArrayList<ColumnData>();
                  ColumnData columnData = new ColumnData();
                  columnData.setNAME(universalColumns.getId());
                  columnData.setVALUE(member.getDesc());
                  columnDataList.add(columnData);
                  for (int measureCntr = 0; measureCntr < measureIdList.size(); measureCntr++) {
                     for (UniversalValue value : universalReport.getData().getValues()) {
                        if (value.getColumndata().get(0).getValue().equalsIgnoreCase(member.getDesc())) {
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
                     seriesCntr = 0;
                  }
                  row.setCOLUMNDATA(columnDataList);
                  rowList.add(row);
               }
            }
         }
         chartFXReport.setCOLUMNS(columns);
         chartFXReport.setROW(rowList);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return chartFXReport;
   }
}
