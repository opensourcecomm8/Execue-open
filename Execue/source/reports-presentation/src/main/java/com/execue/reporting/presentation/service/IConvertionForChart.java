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


package com.execue.reporting.presentation.service;

public interface IConvertionForChart {

   public String convertionForChart (String path, String reportQueryDataXML);

   public String cmMultiChartXMLConvertor (String path, String reportQueryDataXML);

   public String crossChartXMLConvertor (String path, String reportQueryDataXML);

   public String[] crossLineChartXMLConvertor (String path, String reportQueryDataXML);

   public String getReportType (String path);

   public String getReportTitle (String path);

   public String[] cMultiChart (String path);

   public String getPivotXMLData (String path, String reportQueryDataXML);
   // public String[] cMultiChart(String path, String xmlReportData);
}
