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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.execue.core.common.bean.ReportListWrapper;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.presentation.IPresentationHandler;
import com.execue.handler.reports.IReportController;
import com.execue.handler.reports.IScatterChartController;
import com.opensymphony.xwork2.ActionSupport;

public class ChartFxScatterChartAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

   private static Logger           log           = Logger.getLogger(ChartFxScatterChartAction.class);

   private HttpServletRequest      request;
   private HttpServletResponse     response;
   private IReportController       reportController;
   private IPresentationHandler    presentationHandler;
   private ReportListWrapper       reportWrapper = new ReportListWrapper();
   private String                  agQueryIdList;
   private IScatterChartController scatterChartController;

   //method

   public String showScatterChart () {
      List<Long> agQueryIds = prepareAgQueryIds();
      if (ExecueCoreUtil.isCollectionNotEmpty(agQueryIds)) {
         for (Long aggregateQueryId : agQueryIds) {
            String xmlReportData = null;
            try {
               xmlReportData = presentationHandler.getReportXMLData(aggregateQueryId);
               getReportController().selectReportHandlers(reportWrapper, request, response, xmlReportData);
            } catch (HandlerException e) {
               e.printStackTrace();
               return ERROR;
            }
         }
      }
      return SUCCESS;
   }

   public String showScatterChartSubIcons () {
      List<Long> agQueryIds = prepareAgQueryIds();
      if (ExecueCoreUtil.isCollectionNotEmpty(agQueryIds)) {
         try {
            getScatterChartController().preapreScatterChartSubIcons(reportWrapper, request, response, agQueryIds);
         } catch (HandlerException e) {
            e.printStackTrace();
         }
      }
      return SUCCESS;
   }

   private List<Long> prepareAgQueryIds () {
      List<Long> agQueryIds = new ArrayList<Long>();
      if (agQueryIdList != null) {
         String[] scatterChartAgQueryIdArray = agQueryIdList.split(",");
         for (int i = 0; i < scatterChartAgQueryIdArray.length; i++) {
            agQueryIds.add(new Long(scatterChartAgQueryIdArray[i]));
         }

      }
      return agQueryIds;
   }

   /**
    * @return the request
    */
   public HttpServletRequest getRequest () {
      return request;
   }

   /**
    * @return the response
    */
   public HttpServletResponse getResponse () {
      return response;
   }

   /**
    * @return the reportController
    */
   public IReportController getReportController () {
      return reportController;
   }

   /**
    * @param reportController the reportController to set
    */
   public void setReportController (IReportController reportController) {
      this.reportController = reportController;
   }

   @Override
   public void setServletRequest (HttpServletRequest request) {
      this.request = request;

   }

   @Override
   public void setServletResponse (HttpServletResponse response) {
      this.response = response;

   }

   /**
    * @return the reportWrapper
    */
   public ReportListWrapper getReportWrapper () {
      return reportWrapper;
   }

   /**
    * @param reportWrapper the reportWrapper to set
    */
   public void setReportWrapper (ReportListWrapper reportWrapper) {
      this.reportWrapper = reportWrapper;
   }

   /**
    * @return the agQueryIdList
    */
   public String getAgQueryIdList () {
      return agQueryIdList;
   }

   /**
    * @param agQueryIdList the agQueryIdList to set
    */
   public void setAgQueryIdList (String agQueryIdList) {
      this.agQueryIdList = agQueryIdList;
   }

   /**
    * @return the scatterChartController
    */
   public IScatterChartController getScatterChartController () {
      return scatterChartController;
   }

   /**
    * @param scatterChartController the scatterChartController to set
    */
   public void setScatterChartController (IScatterChartController scatterChartController) {
      this.scatterChartController = scatterChartController;
   }

   /**
    * @return the presentationHandler
    */
   public IPresentationHandler getPresentationHandler () {
      return presentationHandler;
   }

   /**
    * @param presentationHandler the presentationHandler to set
    */
   public void setPresentationHandler (IPresentationHandler presentationHandler) {
      this.presentationHandler = presentationHandler;
   }

}
