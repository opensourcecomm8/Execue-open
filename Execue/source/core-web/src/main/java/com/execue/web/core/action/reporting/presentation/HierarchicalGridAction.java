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

import org.apache.log4j.Logger;

import com.execue.core.common.bean.reports.prsntion.HierarchicalReportInfo;
import com.execue.core.exception.HandlerException;
import com.execue.handler.presentation.IPresentationHandler;
import com.opensymphony.xwork2.ActionSupport;

public class HierarchicalGridAction extends ActionSupport {

   private Logger                 logger = Logger.getLogger(HierarchicalGridAction.class);

   private IPresentationHandler   presentationHandler;
   private HierarchicalReportInfo hierarchicalReportInfo;
   private String                 agQueryIdList;
   private Long                   hierarchySummaryId;

   public String showHierarchicalReport () {
      if (logger.isDebugEnabled()) {
         logger.debug("hierarchySummaryId: " + hierarchySummaryId);
      }
      Long agQueryId = null;
      if (hierarchySummaryId != null) {
         agQueryId = hierarchySummaryId;
      } else {
         agQueryId = 490L;
      }

      try {
         hierarchicalReportInfo = getPresentationHandler().getHierarchicalReportInfo(agQueryId);
      } catch (HandlerException e) {
         logger.error(e);
      }

      return SUCCESS;
   }

   public IPresentationHandler getPresentationHandler () {
      return presentationHandler;
   }

   public void setPresentationHandler (IPresentationHandler presentationHandler) {
      this.presentationHandler = presentationHandler;
   }

   /**
    * @return the hierarchicalReportInfo
    */
   public HierarchicalReportInfo getHierarchicalReportInfo () {
      return hierarchicalReportInfo;
   }

   /**
    * @param hierarchicalReportInfo the hierarchicalReportInfo to set
    */
   public void setHierarchicalReportInfo (HierarchicalReportInfo hierarchicalReportInfo) {
      this.hierarchicalReportInfo = hierarchicalReportInfo;
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
    * @return the hierarchySummaryId
    */
   public Long getHierarchySummaryId () {
      return hierarchySummaryId;
   }

   /**
    * @param hierarchySummaryId the hierarchySummaryId to set
    */
   public void setHierarchySummaryId (Long hierarchySummaryId) {
      this.hierarchySummaryId = hierarchySummaryId;
   }

}
