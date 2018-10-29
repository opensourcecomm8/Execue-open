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


package com.execue.core.common.bean;

import java.util.List;

import com.execue.core.common.bean.aggregation.QueryData;
import com.execue.core.common.type.ReportType;

/**
 * @author kaliki
 * @since 4.0
 */

public class ReportQueryData {

   private long             id;
   private int              numberOfRows;
   private List<ReportType> reportTypes;
   private QueryData        queryData;
   private Long             queryExecutionTime;

   public List<ReportType> getReportTypes () {
      return reportTypes;
   }

   public void setReportTypes (List<ReportType> reportTypes) {
      this.reportTypes = reportTypes;
   }

   public long getId () {
      return id;
   }

   public void setId (long id) {
      this.id = id;
   }

   public int getNumberOfRows () {
      return numberOfRows;
   }

   public void setNumberOfRows (int numberOfRows) {
      this.numberOfRows = numberOfRows;
   }

   public QueryData getQueryData () {
      return queryData;
   }

   public void setQueryData (QueryData queryData) {
      this.queryData = queryData;
   }

   public Long getQueryExecutionTime () {
      return queryExecutionTime;
   }

   public void setQueryExecutionTime (Long queryExecutionTime) {
      this.queryExecutionTime = queryExecutionTime;
   }
}
