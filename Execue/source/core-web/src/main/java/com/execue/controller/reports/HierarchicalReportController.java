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


package com.execue.controller.reports;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.execue.core.common.bean.ReportListWrapper;
import com.execue.core.common.bean.reports.prsntion.ReportWrapper;
import com.execue.core.exception.HandlerException;
import com.execue.exception.PresentationExceptionCodes;
import com.execue.handler.reports.IReportController;

/**
 * 
 * @author Jitendra
 *
 */
public class HierarchicalReportController implements IReportController {

   @Override
   public void selectReportHandlers (ReportListWrapper reportListWrapper, HttpServletRequest request,
            HttpServletResponse response, String xmlReportData) throws HandlerException {
      throw new HandlerException(PresentationExceptionCodes.DEFAULT_EXCEPTION_METHOD_NOT_SUPPORTED, "Method Unused");
   }

   @Override
   public void selectReportHandlers (ReportListWrapper reportListWrapper, HttpServletRequest request,
            HttpServletResponse response, long aggregatedQueryId) throws HandlerException {

      String hierarchyReportGrid = "hierarchyReportGrid" + aggregatedQueryId;
      String thumbnail = "<img class=\"htmlThumbnail\" " + "src=\"" + request.getContextPath()
               + "/images/main/HIERARCHIES.png\" onclick=\"showHierarchyGrid('" + hierarchyReportGrid + "',"
               + aggregatedQueryId + ");\" onmouseover=\"showHierarchyGrid('" + hierarchyReportGrid + "',"
               + aggregatedQueryId + ");\" />";

      ReportWrapper hierarchyHtmlReport = new ReportWrapper();
      hierarchyHtmlReport.setId(hierarchyReportGrid);
      hierarchyHtmlReport.setThumbnail(thumbnail);
      hierarchyHtmlReport.setReport("");
      reportListWrapper.getHtmlReports().add(hierarchyHtmlReport);
   }
}
