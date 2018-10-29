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


package com.execue.repoting.aggregation.bean;

import com.execue.core.common.bean.AggregateQuery;

/**
 * @author John Mallavalli
 */
public class AggregateQueryInfo {

   private AggregateQuery aggregateQuery;
   private ReportMetaInfo reportMetaInfo;

   public AggregateQuery getAggregateQuery () {
      return aggregateQuery;
   }

   public void setAggregateQuery (AggregateQuery aggregateQuery) {
      this.aggregateQuery = aggregateQuery;
   }

   public ReportMetaInfo getReportMetaInfo () {
      return reportMetaInfo;
   }

   public void setReportMetaInfo (ReportMetaInfo reportMetaInfo) {
      this.reportMetaInfo = reportMetaInfo;
   }
}
