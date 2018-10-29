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


package com.execue.handler.bean.reports;

import java.util.List;

/**
 * @author kaliki
 * @since 4.0
 */

public class AssetReport {

   private String                  status;
   private List<AssetReportDetail> reportList;
   private String                  sqlQuery;
   private String                  assetHeader;

   public String getStatus () {
      return status;
   }

   public void setStatus (String status) {
      this.status = status;
   }

   public List<AssetReportDetail> getReportList () {
      return reportList;
   }

   public void setReportList (List<AssetReportDetail> reportList) {
      this.reportList = reportList;
   }

   public String getSqlQuery () {
      return sqlQuery;
   }

   public void setSqlQuery (String sqlQuery) {
      this.sqlQuery = sqlQuery;
   }

   public String getAssetHeader () {
      return assetHeader;
   }

   public void setAssetHeader (String assetHeader) {
      this.assetHeader = assetHeader;
   }

}
