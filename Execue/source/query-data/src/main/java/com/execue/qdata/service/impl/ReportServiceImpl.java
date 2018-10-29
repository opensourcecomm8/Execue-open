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


package com.execue.qdata.service.impl;

import java.util.List;

import com.execue.core.common.bean.entity.ReportComment;
import com.execue.core.exception.ExeCueException;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.qdata.dataaccess.IReportDataAccessManager;
import com.execue.qdata.exception.QueryDataExceptionCodes;
import com.execue.qdata.service.IReportService;

public class ReportServiceImpl implements IReportService {

   private IReportDataAccessManager reportDataAccessManager;

   public List<ReportComment> getReportComments (String queryHash, Long assetId) throws ExeCueException {
      try {
         return getReportDataAccessManager().getReportComments(queryHash, assetId);
      } catch (DataAccessException e) {
         e.printStackTrace();
         throw new ExeCueException(QueryDataExceptionCodes.REPORT_COMMENT_CREATION_FAILED, e);
      }
   }

   public List<ReportComment> getAllReportComments (String queryHash, Long assetId) throws ExeCueException {
      try {
         return getReportDataAccessManager().getAllReportComments(queryHash, assetId);
      } catch (DataAccessException e) {
         e.printStackTrace();
         throw new ExeCueException(QueryDataExceptionCodes.REPORT_COMMENT_CREATION_FAILED, e);
      }
   }

   public void saveReportComment (ReportComment reportComment) throws ExeCueException {
      try {
         getReportDataAccessManager().saveReportComment(reportComment);
      } catch (DataAccessException e) {
         e.printStackTrace();
         throw new ExeCueException(QueryDataExceptionCodes.REPORT_COMMENT_CREATION_FAILED, e);
      }

   }

   public void deleteReportCommentsByAssetId (Long assetId) throws ExeCueException {
      getReportDataAccessManager().deleteReportCommentsByAssetId(assetId);

   }

   /**
    * @return the reportDataAccessManager
    */
   public IReportDataAccessManager getReportDataAccessManager () {
      return reportDataAccessManager;
   }

   /**
    * @param reportDataAccessManager
    *           the reportDataAccessManager to set
    */
   public void setReportDataAccessManager (IReportDataAccessManager reportDataAccessManager) {
      this.reportDataAccessManager = reportDataAccessManager;
   }

}
