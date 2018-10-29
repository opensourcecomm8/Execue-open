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


/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.execue.report.presentation.tx.massage.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.reports.prsntion.UniversalColumn;
import com.execue.core.common.bean.reports.prsntion.UniversalColumnData;
import com.execue.core.common.bean.reports.prsntion.UniversalMember;
import com.execue.core.common.bean.reports.prsntion.UniversalReport;
import com.execue.core.common.bean.reports.prsntion.UniversalValue;
import com.execue.core.common.type.ColumnType;
import com.execue.report.configuration.service.IPresentationConfigurationService;
import com.execue.report.presentation.tx.massage.IReportDataMassageService;

/**
 * @author pulaparty
 */
public class BaseChartDataMassageService implements IReportDataMassageService {

   private IPresentationConfigurationService presentationConfigurationService;

   public void massageData (UniversalReport universalReport) {
      List<String> dimColumns = new ArrayList<String>();
      // format long strings for dimension member values
      for (UniversalColumn headerColumn : universalReport.getHeader().getColumns()) {
         if ((ColumnType.DIMENSION.getValue().equalsIgnoreCase(headerColumn.getCtype()))
                  || (ColumnType.ID.getValue().equalsIgnoreCase(headerColumn.getCtype()) && ColumnType.DIMENSION
                           .getValue().equalsIgnoreCase(headerColumn.getPlotAs()))) {
            dimColumns.add(headerColumn.getId());
            for (UniversalMember headerMember : headerColumn.getMembers()) {
               headerMember.setDesc(trimText(headerMember.getDesc()));
               headerMember.setValue(trimText(headerMember.getValue()));
            }
         }
      }

      // format long strings for data values
      for (UniversalValue dataColumn : universalReport.getData().getValues()) {
         for (UniversalColumnData dataColumnValue : dataColumn.getColumndata()) {
            if (dimColumns.contains(dataColumnValue.getName())) {
               dataColumnValue.setValue(trimText(dataColumnValue.getValue()));
            }
         }
      }
   }

   private String trimText (String inputString) {
      String trimmedText = inputString;
      int maxLength = getPresentationConfigurationService().getAxisLabelMaxLength();
      if (maxLength < inputString.length()) {
         trimmedText = inputString.substring(0, maxLength);
         trimmedText += "...";
      }
      return trimmedText;
   }

   public IPresentationConfigurationService getPresentationConfigurationService () {
      return presentationConfigurationService;
   }

   public void setPresentationConfigurationService (IPresentationConfigurationService presentationConfigurationService) {
      this.presentationConfigurationService = presentationConfigurationService;
   }
}
