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
import com.execue.core.common.bean.reports.prsntion.UniversalReportData;
import com.execue.core.common.bean.reports.prsntion.UniversalValue;
import com.execue.core.common.bean.reports.view.ColumnData;
import com.execue.report.presentation.tx.structure.impl.ChartFxBaseChartStructureTxService;

public class ChartFxPieChartStructureTxService extends ChartFxBaseChartStructureTxService {

   @Override
   protected void transformColumnData (UniversalColumn column, UniversalReportData reportData, int dimensionIndex,
            List<Row> chartFxRowList) {

      for (UniversalMember members : column.getMembers()) {
         boolean isNoValueFound = false;
         Row chartFxRow = new Row();
         List<ColumnData> chartFxColumnDataList = new ArrayList<ColumnData>();
         for (UniversalValue values : reportData.getValues()) {
            if (members.getDesc().equalsIgnoreCase(values.getColumndata().get(dimensionIndex).getValue())) {
               for (UniversalColumnData columnData : values.getColumndata()) {
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

}
