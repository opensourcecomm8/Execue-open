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


package com.execue.core.common.bean.qdata;

import java.io.Serializable;
import java.sql.Blob;

import com.execue.core.common.type.ReportType;

public class QDataCachedReportResults implements Serializable {

   private Long                 id;
   private QDataAggregatedQuery aggregatedQuery;
   private String               metaInfo;
   private Blob                 reportData;
   private ReportType           reportType;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public QDataAggregatedQuery getAggregatedQuery () {
      return aggregatedQuery;
   }

   public void setAggregatedQuery (QDataAggregatedQuery aggregatedQuery) {
      this.aggregatedQuery = aggregatedQuery;
   }

   public String getMetaInfo () {
      return metaInfo;
   }

   public void setMetaInfo (String metaInfo) {
      this.metaInfo = metaInfo;
   }

   public Blob getReportData () {
      return reportData;
   }

   public void setReportData (Blob reportData) {
      this.reportData = reportData;
   }

   public ReportType getReportType () {
      return reportType;
   }

   public void setReportType (ReportType reportType) {
      this.reportType = reportType;
   }

}
