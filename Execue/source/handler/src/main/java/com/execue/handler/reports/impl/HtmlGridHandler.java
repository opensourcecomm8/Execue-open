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


package com.execue.handler.reports.impl;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;

import com.execue.core.common.bean.ReportListWrapper;
import com.execue.core.common.bean.qdata.QDataCachedReportResults;
import com.execue.core.common.bean.reports.prsntion.ReportWrapper;
import com.execue.core.common.bean.reports.prsntion.UniversalReport;
import com.execue.core.common.type.ReportType;
import com.execue.core.exception.HandlerException;
import com.execue.exception.PresentationExceptionCodes;
import com.execue.exception.ReportDataMassageServiceException;
import com.execue.exception.ReportDataTxServiceException;
import com.execue.exception.ReportStructureTxServiceException;
import com.execue.handler.reports.IReportHandler;
import com.execue.qdata.exception.QueryDataException;
import com.execue.report.presentation.tx.structure.IGridReportStructureTxService;

public class HtmlGridHandler extends ReportHandler implements IReportHandler {

   public void handleReport (UniversalReport universalReport, ReportListWrapper reportsList,
            HttpServletRequest request, HttpServletResponse response) throws HandlerException {

      try {
         int sizeOfReport = reportsList.getHtmlReports().size();
         long aggregateQueryId = 0;
         ReportWrapper htmlReport = reportsList.getHtmlReports().get(sizeOfReport - 1);

         ReportType reportType = ReportType.valueOf(htmlReport.getId());
         String agQueryIdList = request.getParameter("agQueryIdList");

         if (agQueryIdList.contains(",")) {
            String[] aggQueryIdList = agQueryIdList.split(",");
            aggregateQueryId = Long.parseLong(aggQueryIdList[0]);
         } else {
            aggregateQueryId = Long.parseLong(agQueryIdList);
         }

         boolean dbSavePath = getPresentationConfigurationService().isSaveReportsToDB();

         if (dbSavePath) {
            QDataCachedReportResults cachedReportResults = getQueryDataService().getCachedReportResultsById(
                     aggregateQueryId, reportType);
            if (cachedReportResults != null && cachedReportResults.getId() > 0) {
               int blobLength = (int) cachedReportResults.getReportData().length();
               htmlReport.setReport(new String(cachedReportResults.getReportData().getBytes(1, blobLength)));
            } else {
               cachedReportResults = new QDataCachedReportResults();
               getReportDataTxService().transformData(universalReport);
               getReportDataMassageService().massageData(universalReport);
               String grid = ((IGridReportStructureTxService) getReportStructureTxService())
                        .transformStructure(universalReport);

               cachedReportResults.setReportType(reportType);
               
               // cachedReportResults.setReportData(Hibernate.createBlob(grid.getBytes()));
               cachedReportResults.setReportData(new SerialBlob(grid.getBytes()));
               
               getQueryDataService().storeCachedReportResults(cachedReportResults, aggregateQueryId);
               htmlReport.setReport(grid);
            }
         } else {
            getReportDataTxService().transformData(universalReport);
            getReportDataMassageService().massageData(universalReport);
            String grid = ((IGridReportStructureTxService) getReportStructureTxService())
                     .transformStructure(universalReport);
            htmlReport.setReport(grid);
         }
      } catch (ReportDataTxServiceException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (ReportDataMassageServiceException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (ReportStructureTxServiceException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (QueryDataException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (SQLException e) {
         throw new HandlerException(PresentationExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }
}
