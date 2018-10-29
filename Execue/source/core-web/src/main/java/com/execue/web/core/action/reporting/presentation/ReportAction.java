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

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.ReportComment;
import com.execue.core.exception.HandlerException;
import com.execue.handler.reports.IReportServiceHandler;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author JTiwari
 * @since 25/02/2010
 */
public class ReportAction extends ActionSupport {

   private static final long     serialVersionUID = 1L;
   private static Logger         logger           = Logger.getLogger(ReportAction.class);

   private ReportComment         reportComment;
   private List<ReportComment>   reportComments;
   private IReportServiceHandler reportServiceHandler;

   // Action methods

   public String input () {
      try {

         reportComments = getReportServiceHandler().getReportComments(reportComment.getQueryHash(),
                  reportComment.getAssetId());
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String getAllReportComments () {
      try {
         reportComments = getReportServiceHandler().getAllReportComments(reportComment.getQueryHash(),
                  reportComment.getAssetId());
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String saveComment () {
      try {
         if (reportComment != null) {
            if (logger.isDebugEnabled()) {
               logger.debug("QueryId:" + reportComment.getQueryId());
               logger.debug("QueryHash:" + reportComment.getQueryHash());
               logger.debug("QueryComment:" + reportComment.getComment());
               logger.debug("assetId:" + reportComment.getAssetId());
               logger.debug("UserName:" + reportComment.getUserName());
               logger.debug("public:" + reportComment.getIsPublic());
            }

         }
         getReportServiceHandler().saveReportComment(reportComment);
      } catch (HandlerException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return SUCCESS;
   }

   /**
    * @return the reportServiceHandler
    */
   public IReportServiceHandler getReportServiceHandler () {
      return reportServiceHandler;
   }

   /**
    * @param reportServiceHandler
    *           the reportServiceHandler to set
    */
   public void setReportServiceHandler (IReportServiceHandler reportServiceHandler) {
      this.reportServiceHandler = reportServiceHandler;
   }

   /**
    * @return the reportComment
    */
   public ReportComment getReportComment () {
      return reportComment;
   }

   /**
    * @param reportComment
    *           the reportComment to set
    */
   public void setReportComment (ReportComment reportComment) {
      this.reportComment = reportComment;
   }

   /**
    * @return the reportComments
    */
   public List<ReportComment> getReportComments () {
      return reportComments;
   }

   /**
    * @param reportComments
    *           the reportComments to set
    */
   public void setReportComments (List<ReportComment> reportComments) {
      this.reportComments = reportComments;
   }

}
