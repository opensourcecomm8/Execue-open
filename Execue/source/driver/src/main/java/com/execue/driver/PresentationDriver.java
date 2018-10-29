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


package com.execue.driver;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.ReportQueryData;
import com.execue.core.common.bean.qdata.QDataAggregatedQuery;
import com.execue.core.common.bean.qdata.QDataAggregatedReportType;
import com.execue.core.common.bean.qdata.QDataReportData;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ReportType;
import com.execue.core.common.util.QueryDataXMLWriter;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.qdata.exception.QueryDataException;
import com.execue.repoting.aggregation.service.IReportAggregationService;

/**
 * @author John Mallavalli
 */
public class PresentationDriver extends BaseDriver implements IPresentationDriver {

   private static final Logger       logger = Logger.getLogger(PresentationDriver.class);

   private IReportAggregationService aggregationService;

   public String processReportRequest (Long aggregateQueryId) throws ExeCueException {
      try {

         // Get the corresponding QDataAggregatedQuery from DB
         QDataAggregatedQuery qDataAQ = getDriverHelper().getQueryDataService()
                  .getAggregatedQueryById(aggregateQueryId);

         String xmlData = "";
         if (CheckType.NO.equals(qDataAQ.getDataExtracted())) {

            if (logger.isDebugEnabled()) {
               logger
                        .debug("Data for the requested report was not extracted...\nStarting the data extraction process...!!");

            }

            String finalQuery = qDataAQ.getAggregatedQueryString();
            String metaInfoStructure = qDataAQ.getReportMetaInfoStructure();

            // Get the report types
            List<ReportType> reportTypes = new ArrayList<ReportType>();
            for (QDataAggregatedReportType qReportType : qDataAQ.getReportTypes()) {
               reportTypes.add(qReportType.getType());
            }

            // Extract the report query data 
            ReportQueryData reportQueryData = getAggregationService().extractQueryDataForPresentationTimeReport(
                     finalQuery, metaInfoStructure);
            reportQueryData.setReportTypes(reportTypes);

            // Transform the report query data into the report xml format
            String payLoad = new QueryDataXMLWriter().toXML(reportQueryData);
            xmlData = payLoad;

            // Prepare and store the qdata report data
            QDataReportData qDataReportData = new QDataReportData();
            qDataReportData.setAggregatedQuery(qDataAQ);
            qDataReportData.setPayload(payLoad);

            // Store the report data (Universal XML)
            storeReportData(qDataReportData);

            // Update the extracted flag for the current aggregate query
            qDataAQ.setDataExtracted(CheckType.YES);
            if (reportQueryData.getNumberOfRows() > 0) {
               qDataAQ.setDataPresent(CheckType.YES);
            }

            // Set the time taken for executing the query
            qDataAQ.setQueryExecutionTime(reportQueryData.getQueryExecutionTime());
            updateFlagsForAggregateQuery(qDataAQ);
         } else {
            if (logger.isDebugEnabled()) {
               logger.debug("Data for the requested report is available...!!");
            }
            xmlData = qDataAQ.getReportDatas().getPayload();
         }
         return xmlData;
      } catch (QueryDataException queryDataException) {
         throw new ExeCueException(queryDataException.getCode(), queryDataException.getCause());
      }
   }

   public String processReportRequest (Long aggregateQueryId, Page pageDetail) throws ExeCueException {
      try {

         // Get the corresponding QDataAggregatedQuery from DB
         QDataAggregatedQuery qDataAQ = getDriverHelper().getQueryDataService()
                  .getAggregatedQueryById(aggregateQueryId);

         String xmlData = "";
         if (CheckType.YES.equals(qDataAQ.getDataExtracted())) {

            String finalQuery = qDataAQ.getAggregatedQueryString();
            String metaInfoStructure = qDataAQ.getReportMetaInfoStructure();

            // Extraction of the data begins with the new limit clauses
            ReportQueryData reportQueryData = getAggregationService().alterLimitsAndExtractQueryDataForDetailReport(
                     finalQuery, metaInfoStructure, pageDetail);

            List<ReportType> reportTypes = new ArrayList<ReportType>();
            for (QDataAggregatedReportType qReportType : qDataAQ.getReportTypes()) {
               reportTypes.add(qReportType.getType());
            }
            reportQueryData.setReportTypes(reportTypes);

            // Returning back the universal xml without storing it onto db
            xmlData = new QueryDataXMLWriter().toXML(reportQueryData);
         } else {
            // NOTE: We should never come here in the pagination scenario.  
            // Assumption here is data is already extracted once at least, hence throwing exception if its not the case  
            if (logger.isDebugEnabled()) {
               logger.debug("Data for requested report has not been extracted yet!!");
            }
            throw new ExeCueException(ExeCueExceptionCodes.ENTITY_RETRIEVAL_FAILED, "Data not extracted yet");
         }
         return xmlData;
      } catch (QueryDataException queryDataException) {
         throw new ExeCueException(queryDataException.getCode(), queryDataException.getCause());
      }
   }

   public String processDetailReportHeaderRequest (Long aggregateQueryId) throws ExeCueException {
      QDataAggregatedQuery aggregateQuery = getDriverHelper().getQueryDataService().getAggregatedQueryById(
               aggregateQueryId);
      if (aggregateQuery.getType().equals(AggregateQueryType.DETAILED_SUMMARY)) {
         // loading the detailed xml and getting the headers data.
         return processReportRequest(aggregateQueryId);
      }
      return null;
   }

   private void updateFlagsForAggregateQuery (QDataAggregatedQuery qDataAggregatedQuery) {
      try {
         getDriverHelper().getQueryDataService().updateFlagsForAggregateQuery(qDataAggregatedQuery);
      } catch (QueryDataException e) {
         e.printStackTrace();
      }
   }

   private void storeReportData (QDataReportData qDataReportData) {
      try {
         getDriverHelper().getQueryDataService().storeReportData(qDataReportData, qDataReportData.getAggregatedQuery());
      } catch (QueryDataException e) {
         e.printStackTrace();
         // TODO: -JVK- handle exception
      }
   }

   public IReportAggregationService getAggregationService () {
      return aggregationService;
   }

   public void setAggregationService (IReportAggregationService aggregationService) {
      this.aggregationService = aggregationService;
   }

}
