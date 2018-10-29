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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.execue.core.common.type.AggregateQueryType;
import com.execue.reporting.presentation.service.IPresentationTransformService;
import com.opensymphony.xwork2.ActionSupport;

public class ShowCSVAction extends ActionSupport implements ServletResponseAware {

   private String                        queryId;
   private String                        assetId;
   private Long                          businessQueryId;
   private HttpServletResponse           response;
   private IPresentationTransformService csvTransform;
   private String                        type;
   private String                        agQueryIdList;

   public String showCSV () {
      try {
         AggregateQueryType queryType = AggregateQueryType.BUSINESS_SUMMARY;
         if (type != null && NumberUtils.isNumber(type)) {
            queryType = AggregateQueryType.getAggregateQueryType(new Integer(type));
         }
         response.setContentType("application/vnd.ms-excel");
         response.addHeader("Content-Disposition", "filename=\"ExcelReport.csv\"");
         ServletOutputStream output = response.getOutputStream();

         // String CSV_Data = csvTransform.getReport(new Long(queryId).longValue(), new Long(assetId).longValue(), businessQueryId, queryType).getXmlData();
         String CSV_Data = csvTransform.getReport(0, 0, Long.parseLong(agQueryIdList)).getXmlData();
         output.println(CSV_Data);
         output.flush();
         output.close();

      } catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
      /**
       * returning null helps to send NOOP code for this action hence preventing re-direction and avoiding IllegalStateException thrown on the response object.
       */
      return null;
   }

   public void setServletResponse (HttpServletResponse arg0) {
      response = arg0;

   }

   public void setCsvTransform (IPresentationTransformService csvTransform) {
      this.csvTransform = csvTransform;
   }

   public void setQueryId (String queryId) {
      this.queryId = queryId;
   }

   public void setAssetId (String assetId) {
      this.assetId = assetId;
   }

   public Long getBusinessQueryId () {
      return businessQueryId;
   }

   public void setBusinessQueryId (Long businessQueryId) {
      this.businessQueryId = businessQueryId;
   }

   public String getType () {
      return type;
   }

   public void setType (String type) {
      this.type = type;
   }

   public String getAgQueryIdList () {
      return agQueryIdList;
   }

   public void setAgQueryIdList (String agQueryIdList) {
      this.agQueryIdList = agQueryIdList;
   }

}
