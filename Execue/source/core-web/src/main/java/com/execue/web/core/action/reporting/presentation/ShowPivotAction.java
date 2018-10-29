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


package com.execue.web.core.action.reporting.presentation;

import com.execue.reporting.presentation.service.IPresentationTransformService;
import com.opensymphony.xwork2.ActionSupport;

public class ShowPivotAction extends ActionSupport {

   private String                        queryId;
   private String                        assetId;
   private Long                          businessQueryId;
   private String                        xmlReportData;
   private String                        title;
   private String                        source;
   private IPresentationTransformService pivotTransform;
   private String                        agQueryIdList;

   public String showPivot () {

      try {
         // xmlReportData = pivotTransform.getReport(new Long(queryId).longValue(), new Long(assetId).longValue(), businessQueryId,
         // AggregateQueryType.BUSINESS_SUMMARY).getXmlData();
         xmlReportData = pivotTransform.getReport(0, 0, Long.parseLong(agQueryIdList)).getXmlData();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return SUCCESS;
   }

   public String getQueryId () {
      return queryId;
   }

   public void setQueryId (String queryId) {
      this.queryId = queryId;
   }

   public String getAssetId () {
      return assetId;
   }

   public void setAssetId (String assetId) {
      this.assetId = assetId;
   }

   public String getXmlReportData () {
      return xmlReportData;
   }

   public void setXmlReportData (String xmlReportData) {
      this.xmlReportData = xmlReportData;
   }

   public String getTitle () {
      return title;
   }

   public void setTitle (String title) {
      this.title = title;
   }

   public IPresentationTransformService getPivotTransform () {
      return pivotTransform;
   }

   public void setPivotTransform (IPresentationTransformService pivotTransform) {
      this.pivotTransform = pivotTransform;
   }

   public String getSource () {
      return source;
   }

   public void setSource (String source) {
      this.source = source;
   }

   public Long getBusinessQueryId () {
      return businessQueryId;
   }

   public void setBusinessQueryId (Long businessQueryId) {
      this.businessQueryId = businessQueryId;
   }

   public String getAgQueryIdList () {
      return agQueryIdList;
   }

   public void setAgQueryIdList (String agQueryIdList) {
      this.agQueryIdList = agQueryIdList;
   }

}
