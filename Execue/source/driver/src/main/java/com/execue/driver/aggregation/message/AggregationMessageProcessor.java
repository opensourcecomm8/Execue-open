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


package com.execue.driver.aggregation.message;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import com.execue.core.common.bean.AggregateQuery;
import com.execue.core.common.bean.AggregationMessage;
import com.execue.core.common.bean.governor.AssetQuery;
import com.execue.core.common.bean.pseudolang.NormalizedPseudoQuery;
import com.execue.core.common.bean.querygen.QueryRepresentation;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ReportType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.message.exception.MessageException;
import com.execue.pseudolang.service.IPseudoLanguageService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IQueryDataService;
import com.execue.repoting.aggregation.exception.AggregationException;
import com.execue.repoting.aggregation.service.IReportAggregationService;
import com.thoughtworks.xstream.XStream;

public class AggregationMessageProcessor {

   private static final Logger       log = Logger.getLogger(AggregationMessageProcessor.class);

   private IReportAggregationService aggregationService;
   private IQueryDataService         queryDataService;
   private IPseudoLanguageService    pseudoLanguageService;

   void processMessage (AggregationMessage aggregationMessage) throws MessageException {
      try {

         String tidValue = "txnId-" + aggregationMessage.getQueryId();
         MDC.put("txnId", tidValue);

         // Get the asset query and assign the query id to it
         AssetQuery assetQuery = aggregationMessage.getAssetQuery();
         assetQuery.setUserQueryId(aggregationMessage.getQueryId());

         if (CheckType.NO == assetQuery.getLogicalQuery().getAsset().getQueryExecutionAllowed()) {

            // By pass the aggregation
            processMessageBypassingAggregation(aggregationMessage, assetQuery);
         } else {

            // Generate the list of aggregated queries 
            List<AggregateQuery> aggregateQueryList = getAggregationService().generateReports(assetQuery);

            if (ExecueCoreUtil.isCollectionNotEmpty(aggregateQueryList)) {

               for (AggregateQuery aggregateQuery : aggregateQueryList) {

                  // Generate the pseudo query
                  NormalizedPseudoQuery pseudoQuery = getPseudoLanguageService().getPseudoQuery(
                           aggregateQuery.getAssetQuery().getLogicalQuery());
                  String pseudoStatement = getPseudoLanguageService().getPseudoLanguageStatement(pseudoQuery);
                  String pseudoTitle = getPseudoLanguageService().generateTitle(pseudoQuery);

                  // Transform the governor query structure
                  QueryRepresentation governorQueryRepresentation = aggregationMessage.getGovernorQueryRepresentation();

                  // NOTE: governor query representation is populated based on configuration, so could be
                  // null if in case the flag is turned off
                  String governorQueryStructure = "";
                  String governorQueryString = "";
                  if (governorQueryRepresentation != null) {
                     governorQueryStructure = new XStream().toXML(governorQueryRepresentation.getQueryStructure());
                     governorQueryString = governorQueryRepresentation.getQueryString();
                  }

                  // Transform the aggregated query structure
                  // NOTE: aggregated query representation could be null for example in case of hierarchy aggregated query
                  QueryRepresentation aggregatedQueryRepresentation = aggregateQuery.getQueryRepresentation();
                  String aggregatedQueryStructure = null;
                  String aggregatedQueryString = null;
                  if (aggregatedQueryRepresentation != null
                           && aggregatedQueryRepresentation.getQueryStructure() != null) {
                     aggregatedQueryStructure = new XStream().toXML(aggregatedQueryRepresentation.getQueryStructure());
                     aggregatedQueryString = aggregatedQueryRepresentation.getQueryString();
                  }

                  // Store the aggregated query in the qdata
                  getQueryDataService()
                           .storeAggregateQuery(aggregationMessage.getQueryId(),
                                    aggregationMessage.getBusinessQueryId(), aggregateQuery, pseudoStatement,
                                    pseudoTitle, governorQueryString, aggregatedQueryString, governorQueryStructure,
                                    aggregatedQueryStructure);
               }
            }
         }
      } catch (AggregationException aggregationException) {
         log.error(aggregationException, aggregationException);
         throw new MessageException(aggregationException.getCode(), aggregationException.getMessage(),
                  aggregationException.getCause());
      } catch (QueryDataException queryDataException) {
         log.error(queryDataException, queryDataException);
         throw new MessageException(queryDataException.getCode(), "unable to save to query data ", queryDataException);
      } finally {
         MDC.remove("txnId");
      }
   }

   /**
    * Create the flow by bypassing Aggregation. 
    * 
    * @param aggregationMessage
    * @param assetQuery
    * @throws QueryDataException
    */
   private void processMessageBypassingAggregation (AggregationMessage aggregationMessage, AssetQuery assetQuery)
            throws QueryDataException {

      AggregateQuery aggregateQuery = new AggregateQuery();
      aggregateQuery.setAssetQuery(assetQuery);
      aggregateQuery.setType(AggregateQueryType.NO_SUMMARY);
      aggregateQuery.setReportTypes(new ArrayList<ReportType>());
      aggregateQuery.setDataExtracted(CheckType.NO);
      aggregateQuery.setDataPresent(CheckType.NO);

      // Generate the pseudo query
      NormalizedPseudoQuery pseudoQuery = getPseudoLanguageService().getPseudoQuery(assetQuery.getLogicalQuery());
      String statement = getPseudoLanguageService().getPseudoLanguageStatement(pseudoQuery);
      String title = getPseudoLanguageService().generateTitle(pseudoQuery);

      String governorQueryStructure = "";
      String governorQueryString = "";

      QueryRepresentation governorQueryRepresentation = aggregationMessage.getGovernorQueryRepresentation();
      if (governorQueryRepresentation != null) {
         governorQueryStructure = new XStream().toXML(governorQueryRepresentation.getQueryStructure());
         governorQueryString = governorQueryRepresentation.getQueryString();
      }

      // Store the aggregated query in the qdata
      getQueryDataService().storeAggregateQuery(aggregationMessage.getQueryId(),
               aggregationMessage.getBusinessQueryId(), aggregateQuery, statement, title, governorQueryString,
               governorQueryString, governorQueryStructure, governorQueryStructure);
   }

   public IReportAggregationService getAggregationService () {
      return aggregationService;
   }

   public void setAggregationService (IReportAggregationService aggregationService) {
      this.aggregationService = aggregationService;
   }

   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
   }

   public IPseudoLanguageService getPseudoLanguageService () {
      return pseudoLanguageService;
   }

   public void setPseudoLanguageService (IPseudoLanguageService pseudoLanguageService) {
      this.pseudoLanguageService = pseudoLanguageService;
   }
}