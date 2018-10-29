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


/***********************************************************************************************************************************************************************************
 * <ExcCueLite - Reporting views> www.vbsoftindia.com FILENAME : this.java DEPENDENCIES : ReportView.java KNOWN ISSUES : CREATED ON : 4 March,2009
 **********************************************************************************************************************************************************************************/
package com.execue.handler.bean.presentation;

/**
 * FormBeans Class
 * 
 * @author Ankur M. Bhalodia
 */
public class FormBeans {

   private String fileForm;
   private String chartType, pathCall, pivotDefPath, pivotDataPath, gridStringForm, groupByGridForm, acrossGride,
            crossLinepathCall, cmMultiLinepathCall, pivotData, detailReport, detailGroupReport;
   public static String[] cMultiArryForm, crossLineChartForm;

   public String getMyFile () {
      return fileForm;
   }

   public void setMyFile (String myFile) {
      this.fileForm = myFile;
   }

   public String getFileType () {
      return chartType;
   }

   public void setFileType (String fileType) {
      this.chartType = fileType;
   }

   public void setPath (String path) {
      this.pathCall = path;
   }

   public String getPath () {
      return pathCall;
   }

   public void setCrossLinePath (String crossLinePath) {
      this.crossLinepathCall = crossLinePath;
   }

   public String getCrossLinePath () {
      return crossLinepathCall;
   }

   public void setCmMultiLinePath (String cmMultiLinePath) {
      this.cmMultiLinepathCall = cmMultiLinePath;
   }

   public String getCmMultiLinePath () {
      return cmMultiLinepathCall;
   }

   public void setPivotPathDef (String pivotPathDef) {
      this.pivotDefPath = pivotPathDef;
   }

   public String getPivotPathDef () {
      return pivotDefPath;
   }

   public void setPivotPathData (String pivotPathData) {
      this.pivotDataPath = pivotPathData;
   }

   public String getPivotPathData () {
      return pivotDataPath;
   }

   public void setGridString (String gridString) {
      this.gridStringForm = gridString;
   }

   public String getGridString () {
      return gridStringForm;
   }

   public void setGroupByGrid (String groupByGrid) {
      this.groupByGridForm = groupByGrid;
   }

   public String getGroupByGrid () {
      return groupByGridForm;
   }

   public void setAcrossGrid (String acrossGrid) {
      this.acrossGride = acrossGrid;
   }

   public String getAcrossGrid () {
      return acrossGride;
   }

   public void setCMultiArry (String[] cMultiArry) {
      FormBeans.cMultiArryForm = cMultiArry;
   }

   public String[] getCMultiArry () {
      return cMultiArryForm;
   }

   public void setCrossLineChart (String[] CrossLineChart) {
      this.crossLineChartForm = CrossLineChart;
   }

   public String[] getCrossLineChart () {
      return crossLineChartForm;
   }

   public void setPivotTable (String pivotData1) {
      this.pivotData = pivotData1;
   }

   public String getPivotTable () {
      return pivotData;
   }

   public String getDetailReport () {
      return detailReport;
   }

   public void setDetailReport (String detailReport) {
      this.detailReport = detailReport;
   }

   public String getDetailGroupReport () {
      return detailGroupReport;
   }

   public void setDetailGroupReport (String detailGroupReport) {
      this.detailGroupReport = detailGroupReport;
   }
}
