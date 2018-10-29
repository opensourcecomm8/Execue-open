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


package com.execue.repoting.aggregation.service;

import java.util.List;

import com.execue.core.common.bean.AggregateQuery;
import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.ReportQueryData;
import com.execue.core.common.bean.governor.AssetQuery;
import com.execue.repoting.aggregation.exception.AggregationException;

public interface IReportAggregationService {

   public List<AggregateQuery> generateReports (AssetQuery assetQuery) throws AggregationException;

   public void populateReportData ();

   /**
    * Prepares the ReportQueryData for a detail report using the ReportMetaInfo string information that gets stored
    * 
    * @param aggregationMetaInfoAsString
    * @return reportQueryData
    */
   public ReportQueryData extractQueryDataForPresentationTimeReport (String finalQuery,
            String aggregationMetaInfoAsString) throws AggregationException;

   /**
    * Prepares the ReportQueryData for a detail report using the ReportMetaInfo string after altering the limit clauses
    * @param aggregationMetaInfoAsString
    * 
    * @return reportQueryData
    */
   public ReportQueryData alterLimitsAndExtractQueryDataForDetailReport (String finalQuery,
            String aggregationMetaInfoAsString, Page pageDetail)
            throws AggregationException;

}
