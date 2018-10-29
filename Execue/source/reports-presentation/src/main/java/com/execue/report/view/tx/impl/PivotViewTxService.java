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
package com.execue.report.view.tx.impl;

import com.execue.exception.PresentationExceptionCodes;
import com.execue.exception.ReportViewTxServiceException;
import com.execue.report.view.chartfx.tx.IReportViewTxService;
import com.softwarefx.chartfx.server.ChartServer;

/**
 * @author Owner
 */
public class PivotViewTxService implements IReportViewTxService {

   public void prepareViewData (ChartServer chartServer) throws ReportViewTxServiceException {
      throw new ReportViewTxServiceException(PresentationExceptionCodes.DEFAULT_EXCEPTION_METHOD_NOT_SUPPORTED,
               "Not supported yet.");
   }

   public void applyViewRules (ChartServer chartServer) throws ReportViewTxServiceException {
      throw new ReportViewTxServiceException(PresentationExceptionCodes.DEFAULT_EXCEPTION_METHOD_NOT_SUPPORTED,
               "Not supported yet.");
   }
}
