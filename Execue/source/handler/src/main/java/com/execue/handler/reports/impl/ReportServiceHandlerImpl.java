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

import java.util.Date;
import java.util.List;

import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.execue.core.common.bean.entity.ReportComment;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.handler.reports.IReportServiceHandler;
import com.execue.qdata.service.IReportService;
import com.execue.security.UserContextService;

public class ReportServiceHandlerImpl extends UserContextService implements IReportServiceHandler {

   private IReportService  reportService;
   private PasswordEncoder md5StringEncoder;

   public List<ReportComment> getReportComments (String queryHash, Long assetId) throws HandlerException {
      try {
         // User user = getUserContext().getUser();
         queryHash = md5StringEncoder.encodePassword(queryHash, null);
         return getReportService().getReportComments(queryHash, assetId);

         // TODO-JT- Added user info as trying to implement report public check for report comment.
         /*
          * if (ExecueCoreUtil.isCollectionNotEmpty(allReportComments) && user != null) { if
          * ("guest".equalsIgnoreCase(user.getUsername())) { for (ReportComment reportComment : allReportComments) { if
          * (CheckType.YES.equals(reportComment.getIsPublic())) { reportComments.add(reportComment); } if
          * (reportComments.size() == 5) { break; } } } else { for (ReportComment reportComment : allReportComments) {
          * if (user.getUsername().equalsIgnoreCase(reportComment.getUserName())) { reportComments.add(reportComment); }
          * else if (CheckType.YES.equals(reportComment.getIsPublic())) { reportComments.add(reportComment); } if
          * (reportComments.size() == 5) { break; } } } }
          */

      } catch (ExeCueException e) {
         e.printStackTrace();
         throw new HandlerException(ExeCueExceptionCodes.REPORT_COMMENT_RETRIEVAL_FAILED, e);
      }
   }

   public List<ReportComment> getAllReportComments (String queryHash, Long assetId) throws HandlerException {
      try {
         queryHash = md5StringEncoder.encodePassword(queryHash, null);
         return getReportService().getReportComments(queryHash, assetId);
      } catch (ExeCueException e) {
         e.printStackTrace();
         throw new HandlerException(ExeCueExceptionCodes.REPORT_COMMENT_RETRIEVAL_FAILED, e);
      }
   }

   public void saveReportComment (ReportComment reportComment) throws HandlerException {
      try {
         if (reportComment != null) {
            if (reportComment.getQueryHash() != null) {
               reportComment.setQueryHash(md5StringEncoder.encodePassword(reportComment.getQueryHash(), null));
               reportComment.setCreatedDate(new Date());
            }
            if (ExecueCoreUtil.isNotEmpty(reportComment.getComment())) {
               reportComment.setComment(ExecueStringUtil.getTruncatedString(reportComment.getComment(), 6000));
            }
            getReportService().saveReportComment(reportComment);
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
         throw new HandlerException(ExeCueExceptionCodes.REPORT_COMMENT_CREATION_FAILED, e);
      }

   }

   public IReportService getReportService () {
      return reportService;
   }

   public void setReportService (IReportService reportService) {
      this.reportService = reportService;
   }

   public PasswordEncoder getMd5StringEncoder () {
      return md5StringEncoder;
   }

   public void setMd5StringEncoder (PasswordEncoder md5StringEncoder) {
      this.md5StringEncoder = md5StringEncoder;
   }

}
