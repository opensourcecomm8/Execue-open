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

import com.execue.qdata.service.IQueryDataService;
import com.execue.report.configuration.service.IPresentationConfigurationService;
import com.execue.report.presentation.tx.data.IReportDataTxService;
import com.execue.report.presentation.tx.massage.IReportDataMassageService;
import com.execue.report.presentation.tx.structure.IReportStructureTxService;
import com.execue.report.view.chartfx.tx.IReportViewTxService;

public abstract class ReportHandler {

   private IReportDataMassageService         reportDataMassageService;
   private IReportDataTxService              reportDataTxService;
   private IReportStructureTxService         reportStructureTxService;
   private IReportViewTxService              reportViewTxService;
   private IPresentationConfigurationService presentationConfigurationService;
   private IQueryDataService                 queryDataService;

   public final IReportViewTxService getReportViewTxService () {
      return reportViewTxService;
   }

   public final void setReportViewTxService (IReportViewTxService reportViewTxService) {
      this.reportViewTxService = reportViewTxService;
   }

   public final IReportDataMassageService getReportDataMassageService () {
      return reportDataMassageService;
   }

   public final void setReportDataMassageService (IReportDataMassageService reportDataMassageService) {
      this.reportDataMassageService = reportDataMassageService;
   }

   public final IReportDataTxService getReportDataTxService () {
      return reportDataTxService;
   }

   public final void setReportDataTxService (IReportDataTxService reportDataTxService) {
      this.reportDataTxService = reportDataTxService;
   }

   public final IReportStructureTxService getReportStructureTxService () {
      return reportStructureTxService;
   }

   public final void setReportStructureTxService (IReportStructureTxService reportStructureTxService) {
      this.reportStructureTxService = reportStructureTxService;
   }

   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
   }

   /**
    * @return the presentationConfigurationService
    */
   public IPresentationConfigurationService getPresentationConfigurationService () {
      return presentationConfigurationService;
   }

   /**
    * @param presentationConfigurationService
    *           the presentationConfigurationService to set
    */
   public void setPresentationConfigurationService (IPresentationConfigurationService presentationConfigurationService) {
      this.presentationConfigurationService = presentationConfigurationService;
   }

}
