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


package com.execue.qdata.dataaccess.impl;

import java.util.List;

import com.execue.core.common.bean.entity.ReportComment;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.qdata.dao.IReportDAO;
import com.execue.qdata.dataaccess.IReportDataAccessManager;

public class ReportDataAccessManagerImpl implements IReportDataAccessManager {

   private IReportDAO reportDAO;

   public List<ReportComment> getReportComments (String queryHash, Long assetId) throws DataAccessException {
      return getReportDAO().getReportComments(queryHash, assetId);
   }

   public List<ReportComment> getAllReportComments (String queryHash, Long assetId) throws DataAccessException {
      return getReportDAO().getAllReportComments(queryHash, assetId);
   }

   public void saveReportComment (ReportComment reportComment) throws DataAccessException {
      getReportDAO().create(reportComment);
   }

   public void deleteReportCommentsByAssetId (Long assetId) throws DataAccessException {
      getReportDAO().deleteReportCommentsByAssetId(assetId);

   }

   /**
    * @return the reportDAO
    */
   public IReportDAO getReportDAO () {
      return reportDAO;
   }

   /**
    * @param reportDAO
    *           the reportDAO to set
    */
   public void setReportDAO (IReportDAO reportDAO) {
      this.reportDAO = reportDAO;
   }

}
