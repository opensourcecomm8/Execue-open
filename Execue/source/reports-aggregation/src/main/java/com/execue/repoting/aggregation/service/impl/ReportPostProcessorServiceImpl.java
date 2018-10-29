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


package com.execue.repoting.aggregation.service.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.ReportQueryData;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.bean.qdata.QDataUserQuery;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;
import com.execue.core.common.bean.querygen.QueryRepresentation;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IQueryDataService;
import com.execue.querygen.service.IStructuredQueryTransformerService;
import com.execue.querygen.service.QueryGenerationServiceFactory;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.exception.ReportException;
import com.execue.repoting.aggregation.helper.ReportAggregationHelper;
import com.execue.repoting.aggregation.service.IReportPostProcessorService;

/**
 * @author John Mallavalli
 */

public class ReportPostProcessorServiceImpl implements IReportPostProcessorService {

   private static final Logger                log = Logger.getLogger(ReportPostProcessorServiceImpl.class);

   private ReportAggregationHelper            reportAggregationHelper;
   private QueryGenerationServiceFactory      queryGenerationServiceFactory;
   private IStructuredQueryTransformerService structuredQueryTransformerService;
   private IQueryDataService                  queryDataService;

   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
   }

   public ReportQueryData processQueryData (ReportMetaInfo reportMetaInfo, ReportQueryData reportQueryData)
            throws ReportException {
      if (reportQueryData != null) {
         StructuredQuery structuredQuery = reportMetaInfo.getAssetQuery().getLogicalQuery();
         // modify the structured query if univariants are present
         if (reportMetaInfo.isUnivariants() && reportQueryData.getNumberOfRows() > 0) {
            // process the structured query
            reportAggregationHelper.processUnivariants(reportMetaInfo);

            if (log.isDebugEnabled()) {
               QueryGenerationOutput queryGenerationOutput = getStructuredQueryTransformerService()
                        .populateQueryGenerationOutput(structuredQuery, new HashMap<String, BusinessAssetTerm>());

               QueryRepresentation queryRepresentation = getQueryGenerationServiceFactory().getQueryGenerationService(
                        structuredQuery.getAsset()).extractQueryRepresentation(structuredQuery.getAsset(),
                        queryGenerationOutput.getResultQuery());

               log.debug("-----------------------------------------------------------------");
               log.debug("\nNew Query String : \n" + queryRepresentation.getQueryString());
               log.debug("-----------------------------------------------------------------");
            }
         }
         // generate the title
         String reportTitle = null;

         // Handling the report title in the case of profiles and lengthy titles
         // check if profile is involved
         List<BusinessAssetTerm> metrics = reportMetaInfo.getAssetQuery().getLogicalQuery().getMetrics();
         boolean isProfilePresent = false;
         for (BusinessAssetTerm metric : metrics) {
            if (metric.getBusinessTerm().getProfileDomainEntityDefinitionId() != null) {
               isProfilePresent = true;
               break;
            }
         }
         // check if the generated report title is lengthy
         if (isProfilePresent) {
            reportTitle = getUserQuery(reportMetaInfo);
         } else {
            reportTitle = reportAggregationHelper.prepareReportTitle(reportMetaInfo);
            if (reportTitle.length() > reportAggregationHelper.getMaximumReportTitleLength()) {
               // use the user query
               reportTitle = getUserQuery(reportMetaInfo);
            }
         }
         // this is to append the static text "report" to the title pulled from configuaration.
         reportTitle = reportAggregationHelper.alterReportTitle(reportTitle);

         if (log.isDebugEnabled()) {
            log.debug("REPORT TITLE : " + reportTitle);
         }
         reportQueryData.getQueryData().getQueryDataHeader().setTitle(reportTitle);
      }
      return reportQueryData;
   }

   private String getUserQuery (ReportMetaInfo reportMetaInfo) throws ReportException {
      QDataUserQuery userQuery = null;
      String strUserQuery = null;
      try {
         userQuery = queryDataService.getUserQuery(reportMetaInfo.getAssetQuery().getUserQueryId());
         if (userQuery != null) {
            strUserQuery = userQuery.getNormalizedQueryString();
         }
      } catch (QueryDataException e) {
         throw new ReportException(e.getCode(), e.getCause());
      }
      return strUserQuery;
   }

   public ReportAggregationHelper getReportAggregationHelper () {
      return reportAggregationHelper;
   }

   public void setReportAggregationHelper (ReportAggregationHelper reportAggregationHelper) {
      this.reportAggregationHelper = reportAggregationHelper;
   }

   public QueryGenerationServiceFactory getQueryGenerationServiceFactory () {
      return queryGenerationServiceFactory;
   }

   public void setQueryGenerationServiceFactory (QueryGenerationServiceFactory queryGenerationServiceFactory) {
      this.queryGenerationServiceFactory = queryGenerationServiceFactory;
   }

   public IStructuredQueryTransformerService getStructuredQueryTransformerService () {
      return structuredQueryTransformerService;
   }

   public void setStructuredQueryTransformerService (
            IStructuredQueryTransformerService structuredQueryTransformerService) {
      this.structuredQueryTransformerService = structuredQueryTransformerService;
   }
}