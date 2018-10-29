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

package com.execue.report.presentation.tx.data.impl;

import java.util.List;

import com.execue.core.common.bean.reports.prsntion.UniversalColumn;
import com.execue.core.common.bean.reports.prsntion.UniversalReport;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.exception.ReportDataTxServiceException;
import com.execue.report.presentation.tx.data.IReportDataTxService;
import com.execue.reporting.presentation.helper.PresentationTransformHelper;

/**
 * @author Owner
 */
public class BaseDataTxService implements IReportDataTxService {

   public void transformData (UniversalReport universalReport) throws ReportDataTxServiceException {
      // Do nothing as of now
      // Should only implement something if it is common across all the child classes
   }

   /**
    * This method will check if the count of dimensions is 2 and set the "GROUPBY" and "ACROSS" tags to true
    * based on the number of members each dimension has. The dimension with the higher number of members will
    * have the "GROUPBY" as true and other dimension will have "ACROSS" as true
    * 
    * @param universalReport
    */
   protected void processAcrossReportData (UniversalReport universalReport) {

      List<UniversalColumn> columns = universalReport.getHeader().getColumns();
      List<Integer> dimensionColumnIndices = PresentationTransformHelper.getDimensionColumnIndices(columns);

      if (ExecueCoreUtil.isCollectionNotEmpty(dimensionColumnIndices) && dimensionColumnIndices.size() == 2) {

         // Get the dimension column and member count of the first index
         int firstDimensionColumnIndex = dimensionColumnIndices.get(0).intValue();
         UniversalColumn firstDimensionColumn = columns.get(firstDimensionColumnIndex);
         String firstDimensionMemberCount = firstDimensionColumn.getNummembers();

         // Get the dimension column and member count of the second index
         int secondDimensionColumnIndex = dimensionColumnIndices.get(1).intValue();
         UniversalColumn secondDimensionColumn = columns.get(secondDimensionColumnIndex);
         String secondDimensionMemberCount = secondDimensionColumn.getNummembers();

         // Set the across element "true" to the dimension column where member count is less
         // Set the group by element "true" to the dimension column where member count is more
         if (Integer.parseInt(firstDimensionMemberCount) > Integer.parseInt(secondDimensionMemberCount)) {
            firstDimensionColumn.setGroupby("true");
            secondDimensionColumn.setAcross("true");
         } else {
            firstDimensionColumn.setAcross("true");
            secondDimensionColumn.setGroupby("true");
         }
      }
   }
}
