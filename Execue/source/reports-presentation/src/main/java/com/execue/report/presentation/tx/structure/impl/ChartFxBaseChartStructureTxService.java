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


package com.execue.report.presentation.tx.structure.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.reports.Row;
import com.execue.core.common.bean.reports.prsntion.UniversalColumn;
import com.execue.core.common.bean.reports.prsntion.UniversalColumnData;
import com.execue.core.common.bean.reports.prsntion.UniversalMember;
import com.execue.core.common.bean.reports.prsntion.UniversalReport;
import com.execue.core.common.bean.reports.prsntion.UniversalReportData;
import com.execue.core.common.bean.reports.prsntion.UniversalReportHeader;
import com.execue.core.common.bean.reports.prsntion.UniversalValue;
import com.execue.core.common.bean.reports.view.ChartFXReport;
import com.execue.core.common.bean.reports.view.Column;
import com.execue.core.common.bean.reports.view.ColumnData;
import com.execue.core.common.bean.reports.view.Columns;
import com.execue.core.common.type.ColumnType;
import com.execue.exception.ReportStructureTxServiceException;
import com.execue.report.presentation.tx.structure.IChartFxReportStructureTxService;

public class ChartFxBaseChartStructureTxService implements IChartFxReportStructureTxService {

   private static final String   dimensionTag       = "DIMENSION";
   private static final String   stringAttrValueTag = "String";
   private static final String   doubleAttrValueTag = "Double";
   protected static final String nullString         = "N/A";
   private static final String   measureTag         = "MEASURE";
   private static final String   idTag              = "ID";

   public ChartFXReport transformStructure (UniversalReport universalReport) throws ReportStructureTxServiceException {
      ChartFXReport chartFXReport = new ChartFXReport();

      try {

         UniversalReportHeader reportHeader = universalReport.getHeader();
         UniversalReportData reportData = universalReport.getData();
         chartFXReport.setREPORTTYPES(reportHeader.getReporttypes());
         chartFXReport.setTITLE(reportHeader.getTITLE());
         chartFXReport.setSOURCE(reportHeader.getSource());

         Columns chartFxColumns = new Columns();
         List<Column> chartFxColumnList = new ArrayList<Column>();
         List<Row> chartFxRowList = new ArrayList<Row>();
         int dimensionIndex = 0;

         for (UniversalColumn column : reportHeader.getColumns()) {
            Column chartFxColumn = null;
            if (column.getCtype().equalsIgnoreCase(dimensionTag)) {
               chartFxColumn = new Column();
               chartFxColumn.setDESCRIPTION(column.getDesc());
               chartFxColumn.setNAME(column.getId());
               chartFxColumn.setTYPE(stringAttrValueTag);
            } else if (column.getCtype().equalsIgnoreCase(measureTag)) {
               chartFxColumn = new Column();
               chartFxColumn.setDESCRIPTION(column.getDesc());
               chartFxColumn.setNAME(column.getId());
               chartFxColumn.setTYPE(doubleAttrValueTag);
            } else if (column.getCtype().equalsIgnoreCase(idTag)) {
               chartFxColumn = getPlottableColumn(column);
            }
            if (chartFxColumn != null) {
               chartFxColumnList.add(chartFxColumn);
            }
            // if (column.getCtype().equalsIgnoreCase(dimensionTag)) {
            if (column.getCtype().equalsIgnoreCase(dimensionTag) || dimensionTag.equalsIgnoreCase(column.getPlotAs())) {
               transformColumnData(column, reportData, dimensionIndex, chartFxRowList);
            }
            dimensionIndex++;
         }
         chartFxColumns.setCOLUMN(chartFxColumnList);

         chartFXReport.setCOLUMNS(chartFxColumns);
         chartFXReport.setROW(chartFxRowList);

      } catch (Exception e) {
         e.printStackTrace();
      }
      return chartFXReport;
   }

   protected void transformColumnData (UniversalColumn column, UniversalReportData reportData, int dimensionIndex,
            List<Row> chartFxRowList) {

      for (UniversalMember members : column.getMembers()) {
         boolean isNoValueFound = false;
         Row chartFxRow = new Row();
         List<ColumnData> chartFxColumnDataList = new ArrayList<ColumnData>();
         for (UniversalValue values : reportData.getValues()) {
            if (members.getDesc().equalsIgnoreCase(values.getColumndata().get(dimensionIndex).getValue())) {
               for (UniversalColumnData columnData : values.getColumndata()) {
                  // if (!nullString.equalsIgnoreCase(columnData.getValue())
                  // && columnData.getCtype().equalsIgnoreCase(measureTag)) {
                  if (nullString.equalsIgnoreCase(columnData.getValue())) {
                     isNoValueFound = true;
                  } else {
                     ColumnData chartFxColumnData = new ColumnData();
                     chartFxColumnData.setNAME(columnData.getName());
                     chartFxColumnData.setVALUE(columnData.getValue());
                     chartFxColumnDataList.add(chartFxColumnData);
                  }
               }
            }
         }
         if (!isNoValueFound) {
            chartFxRow.setCOLUMNDATA(chartFxColumnDataList);
            chartFxRowList.add(chartFxRow);
         }
      }

   }

   // this value has to be populated as a tag value in the universal xml for each column
   // eg : in the column header tag, add another tag <plottable>true</plottable>
   private Column getPlottableColumn (UniversalColumn column) {
      Column chartFxColumn = null;
      if (ColumnType.ID.getValue().equals(column.getCtype())) {
         // this is for normal Business Summary path
         if (ColumnType.MEASURE.getValue().equals(column.getPlotAs())) {
            chartFxColumn = new Column();
            chartFxColumn.setDESCRIPTION(column.getDesc());
            chartFxColumn.setNAME(column.getId());
            chartFxColumn.setTYPE(doubleAttrValueTag);
         }
         // this is for detail path
         if (ColumnType.DIMENSION.getValue().equals(column.getPlotAs())) {
            chartFxColumn = new Column();
            chartFxColumn.setDESCRIPTION(column.getDesc());
            chartFxColumn.setNAME(column.getId());
            chartFxColumn.setTYPE(stringAttrValueTag);
         }
      }
      return chartFxColumn;
   }
}
