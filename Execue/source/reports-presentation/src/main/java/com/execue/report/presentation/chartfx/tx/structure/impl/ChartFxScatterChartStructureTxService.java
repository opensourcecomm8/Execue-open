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

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.execue.core.common.bean.reports.Row;
import com.execue.core.common.bean.reports.prsntion.UniversalColumn;
import com.execue.core.common.bean.reports.prsntion.UniversalColumnData;
import com.execue.core.common.bean.reports.prsntion.UniversalReport;
import com.execue.core.common.bean.reports.prsntion.UniversalReportData;
import com.execue.core.common.bean.reports.prsntion.UniversalReportHeader;
import com.execue.core.common.bean.reports.prsntion.UniversalValue;
import com.execue.core.common.bean.reports.view.ChartFXReport;
import com.execue.core.common.bean.reports.view.Column;
import com.execue.core.common.bean.reports.view.ColumnData;
import com.execue.core.common.bean.reports.view.Columns;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.exception.ReportStructureTxServiceException;
import com.execue.report.presentation.tx.structure.impl.ChartFxBaseChartStructureTxService;
import com.execue.util.MathUtil;

public class ChartFxScatterChartStructureTxService extends ChartFxBaseChartStructureTxService {

   private static final String   stringAttrValueTag = "String";
   private static final String   doubleAttrValueTag = "Double";
   protected static final String nullString         = "N/A";
   private static final String   measureTag         = "MEASURE";

   @Override
   public ChartFXReport transformStructure (UniversalReport universalReport) throws ReportStructureTxServiceException {
      ChartFXReport chartFXReport = new ChartFXReport();

      try {

         UniversalReportHeader reportHeader = universalReport.getHeader();
         UniversalReportData reportData = universalReport.getData();

         chartFXReport.setREPORTTYPES(reportHeader.getReporttypes());
         chartFXReport.setTITLE(reportHeader.getTITLE());
         chartFXReport.setSOURCE(reportHeader.getSource());

         List<Column> chartFxColumnList = new ArrayList<Column>();

         for (UniversalColumn column : reportHeader.getColumns()) {
            Column chartFxColumn = null;
            if (column.getCtype().equalsIgnoreCase(measureTag)) {
               chartFxColumn = new Column();
               chartFxColumn.setDESCRIPTION(column.getDesc());
               chartFxColumn.setNAME(column.getId());
               chartFxColumn.setTYPE(doubleAttrValueTag);
            }

            if (chartFxColumn != null) {
               chartFxColumnList.add(chartFxColumn);
            }
         }

         double[] xArray = new double[reportData.getValues().size()];
         double[] yArray = new double[reportData.getValues().size()];

         List<Row> chartFxRowList = new ArrayList<Row>();
         transformColumnData(reportData, chartFxRowList, xArray, yArray);

         if (xArray.length > 1 && yArray.length > 1) {
            double covarianceValue = MathUtil.getCovariance(xArray, yArray);
            double correlationValue = MathUtil.getCorrelation(xArray, yArray);

            String formattedCovarianceValue = ExecueCoreUtil.getFormattedNumberString(covarianceValue, 4);
            String formattedCorrelationValue = ExecueCoreUtil.getFormattedNumberString(correlationValue, 4);

            String covarianceString = "Covariance:  " + formattedCovarianceValue;
            String correlationString = "Correlation: " + formattedCorrelationValue;

            // TODO::NK:: Commented the showing of covariance as per vishy's request, shall uncomment it if needed
            String subTitle = /*covarianceString + " " +*/correlationString;
            chartFXReport.setSUBTITLE(subTitle);
         }

         // Get the min x and min y
         chartFXReport.setMinXAxisValue(getAdjustedMinValue(xArray));
         chartFXReport.setMinYAxisValue(getAdjustedMinValue(yArray));

         // Prepare the columns for the report
         Columns chartFxColumns = new Columns();
         chartFxColumns.setCOLUMN(chartFxColumnList);

         chartFXReport.setCOLUMNS(chartFxColumns);
         chartFXReport.setROW(chartFxRowList);

      } catch (Exception e) {
         e.printStackTrace();
      }
      return chartFXReport;
   }

   private double getAdjustedMinValue (double[] array) {
      DescriptiveStatistics stats = new DescriptiveStatistics(array);
      double standardDeviation = stats.getStandardDeviation();
      double minValue = stats.getMin();
      double adjustedMinValue = minValue - standardDeviation;
      return adjustedMinValue;
   }

   private void transformColumnData (UniversalReportData reportData, List<Row> chartFxRowList, double[] xArray,
            double[] yArray) {

      int arrayValueIndex = 0;
      for (UniversalValue values : reportData.getValues()) {
         List<ColumnData> chartFxColumnDataList = new ArrayList<ColumnData>();
         Row chartFxRow = new Row();
         boolean isNoValueFound = false;
         int valueIndex = 0;
         for (UniversalColumnData columnData : values.getColumndata()) {

            if (nullString.equalsIgnoreCase(columnData.getValue())) {
               isNoValueFound = true;
            } else if (columnData.getCtype().equalsIgnoreCase("MEASURE")) {
               ColumnData chartFxColumnData = new ColumnData();
               chartFxColumnData.setNAME(columnData.getName());
               String value = columnData.getValue();
               double doubleValue = Double.parseDouble(value);
               if (valueIndex == 0) {
                  // For scatters as y = f(x), hence we are putting first column value as y axis
                  yArray[arrayValueIndex] = doubleValue;
               } else {
                  xArray[arrayValueIndex] = doubleValue;
               }
               chartFxColumnData.setVALUE(value);
               chartFxColumnDataList.add(chartFxColumnData);
               valueIndex++;
            }
         }
         arrayValueIndex++;
         if (!isNoValueFound) {
            chartFxRow.setCOLUMNDATA(chartFxColumnDataList);
            chartFxRowList.add(chartFxRow);
         }
      }

   }
}