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


package com.execue.core.common.bean;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.reports.prsntion.ReportWrapper;

public class ReportListWrapper {

   private List<ReportWrapper> chartFxReports;
   private List<ReportWrapper> htmlReports;

   public ReportListWrapper () {
      chartFxReports = new ArrayList<ReportWrapper>();
      htmlReports = new ArrayList<ReportWrapper>();
   }

   public List<ReportWrapper> getChartFxReports () {
      return chartFxReports;
   }

   public List<ReportWrapper> getHtmlReports () {
      return htmlReports;
   }

}
