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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.execue.core.common.bean.ReportListWrapper;
import com.execue.core.common.bean.reports.prsntion.UniversalReport;
import com.execue.core.exception.HandlerException;

public class ChartFxCountryMapHandler extends ChartFxChartHandler {

   @Override
   public void handleReport (UniversalReport universalReport, ReportListWrapper reportsList,
            HttpServletRequest request, HttpServletResponse response) throws HandlerException {
      super.handleReport(universalReport, reportsList, request, response);
   }
}
