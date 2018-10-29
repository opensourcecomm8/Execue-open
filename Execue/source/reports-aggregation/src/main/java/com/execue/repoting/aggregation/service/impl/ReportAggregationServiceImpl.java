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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.AggregateQuery;
import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.ReportQueryData;
import com.execue.core.common.bean.aggregation.QueryData;
import com.execue.core.common.bean.aggregation.QueryDataColumnData;
import com.execue.core.common.bean.aggregation.QueryDataHeader;
import com.execue.core.common.bean.aggregation.QueryDataHeaderColumnMeta;
import com.execue.core.common.bean.aggregation.QueryDataRowData;
import com.execue.core.common.bean.aggregation.QueryDataRows;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.governor.AssetQuery;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ReportType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.common.util.QueryDataXMLWriter;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.repoting.aggregation.bean.AggregateQueryInfo;
import com.execue.repoting.aggregation.bean.AggregationMetaInfo;
import com.execue.repoting.aggregation.bean.ReportMetaInfo;
import com.execue.repoting.aggregation.exception.AggregationException;
import com.execue.repoting.aggregation.exception.ReportException;
import com.execue.repoting.aggregation.exception.ReportMetadataException;
import com.execue.repoting.aggregation.exception.ReportQueryGenerationException;
import com.execue.repoting.aggregation.helper.ReportAggregationHelper;
import com.execue.repoting.aggregation.service.IReportAggregationService;
import com.execue.repoting.aggregation.service.IReportDataExtractionService;
import com.execue.repoting.aggregation.service.IReportMetadataAnalyzerService;
import com.execue.repoting.aggregation.service.IReportPostProcessorService;
import com.execue.repoting.aggregation.service.IReportQueryGenerationService;
import com.execue.repoting.aggregation.service.IReportSelectionService;

/**
 * @author kaliki
 * @since 4.0
 */

public class ReportAggregationServiceImpl implements IReportAggregationService {

   private static final Logger            logger = Logger.getLogger(ReportAggregationServiceImpl.class);

   private IReportMetadataAnalyzerService reportMetadataAnalyzerService;
   private IReportQueryGenerationService  reportQueryGenerationService;
   private IReportDataExtractionService   reportDataExtractionService;
   private IReportPostProcessorService    reportPostProcessorService;
   private IReportSelectionService        reportSelectionService;
   private ReportAggregationHelper        reportAggregationHelper;

   public List<AggregateQuery> generateReports (AssetQuery assetQuery) throws AggregationException {
      List<AggregateQuery> aggregateQueryList = new ArrayList<AggregateQuery>();
      try {
         if (logger.isDebugEnabled()) {
            logger.debug("\n============== Start of Aggregation ==============\n");
         }

         // Generate the Report Meta Info object
         ReportMetaInfo reportMetaInfo = getReportAggregationHelper().prepareReportMetaInfo(assetQuery);

         // Run the counts query to populate the respective columns with their counts
         getReportAggregationHelper().runUniqueCount(reportMetaInfo);

         // Execute the analyzer service(series of processors) for deducing the column types, stats and ranges
         // NOTE: In case of execue owned cubes we only execute statistics, hierarchy, post processor as of now. 
         // This logic is moved inside the call.
         reportMetaInfo = getReportMetadataAnalyzerService().analyze(reportMetaInfo);

         // If the asset is execue owned cube, then bye-pass the processors and the query generation service
         Asset asset = assetQuery.getLogicalQuery().getAsset();
         boolean isExecueOwnedCube = ExecueBeanUtil.isExecueOwnedCube(asset);
         if (isExecueOwnedCube) {

            // Check for hierarchy aggregate query processing, 
            // NOTE: We need to handle hierarchy case at this stage, otherwise report meta info is getting
            // altered for regular cube query. For hierarchy handling we need to work on the clean report meta info.
            if (reportMetaInfo.isGenerateHierarchyReport()) {
               // Generate the Hierarchy Aggregate Query Info's
               List<AggregateQueryInfo> hierarchyAggregateQueries = getReportQueryGenerationService()
                        .populateHierarchyAggregateQueryInfo(reportMetaInfo);
               for (AggregateQueryInfo aggregateQueryInfo : hierarchyAggregateQueries) {
                  aggregateQueryList.add(aggregateQueryInfo.getAggregateQuery());
               }
            }

            // Get the business aggregated query for cube
            AggregateQuery aggregateQuery = getReportAggregationHelper().getBusinessAggregateQuery(reportMetaInfo);

            // service to extract the data from the source and prepare the query data object
            ReportQueryData reportQueryData = getReportDataExtractionService().getQueryData(aggregateQuery,
                     reportMetaInfo);

            // service to do the post processing and update
            reportQueryData = getReportPostProcessorService().processQueryData(reportMetaInfo, reportQueryData);

            // service to select the appropriate report types
            List<ReportType> reportTypes = getReportSelectionService().getReportTypes(reportMetaInfo, aggregateQuery,
                     reportQueryData);
            reportQueryData.setReportTypes(reportTypes);
            aggregateQuery.setReportTypes(reportTypes);
            aggregateQuery.setXmlData(new QueryDataXMLWriter().toXML(reportQueryData));
            if (logger.isDebugEnabled()) {
               logger.debug("Number of rows returned from ASSET : " + reportQueryData.getNumberOfRows());
            }

            // Set the dataPresent flag based on the number of rows
            if (reportQueryData.getNumberOfRows() > 0) {
               aggregateQuery.setDataPresent(CheckType.YES);
            } else {
               aggregateQuery.setDataPresent(CheckType.NO);
            }
            aggregateQuery.setQueryExecutionTime(reportQueryData.getQueryExecutionTime());

            // Add the aggregate query to the list
            aggregateQueryList.add(aggregateQuery);

         } else {

            // Execute the Query generation service to generate the aggregate queries and their internal SQL queries
            List<AggregateQueryInfo> aggregateQueryInfoList = getReportQueryGenerationService().generateQueries(
                     reportMetaInfo);

            // Iterate through the list of the Aggregate Queries and execute the remaining services
            Map<AggregateQuery, ReportQueryData> queryDataByDetailedAggregateQuery = new HashMap<AggregateQuery, ReportQueryData>();
            Map<AggregateQuery, ReportMetaInfo> reportMetaInfoByDetailedAggregateQuery = new HashMap<AggregateQuery, ReportMetaInfo>();
            for (AggregateQueryInfo aggregateQueryInfo : aggregateQueryInfoList) {
               AggregateQuery aggregateQuery = aggregateQueryInfo.getAggregateQuery();
               ReportMetaInfo clonedReportMetaInfo = aggregateQueryInfo.getReportMetaInfo();

               if (AggregateQueryType.DETAILED_SUMMARY.equals(aggregateQuery.getType())) {
                  getReportAggregationHelper().resetColumnTypes(clonedReportMetaInfo);
               }

               if (!skipDataExtraction(aggregateQuery)) {
                  logger.debug("!!!!!!!!!!!! getting data for " + aggregateQuery.getType());
                  // service to extract the data from the source and prepare the query data object
                  ReportQueryData reportQueryData = getReportDataExtractionService().getQueryData(aggregateQuery,
                           clonedReportMetaInfo);

                  // service to do the post processing and update
                  reportQueryData = getReportPostProcessorService().processQueryData(clonedReportMetaInfo,
                           reportQueryData);

                  // service to select the appropriate report types
                  List<ReportType> reportTypes = getReportSelectionService().getReportTypes(clonedReportMetaInfo,
                           aggregateQuery, reportQueryData);
                  if (reportQueryData != null) {
                     reportQueryData.setReportTypes(reportTypes);
                     aggregateQuery.setXmlData(new QueryDataXMLWriter().toXML(reportQueryData));
                     if (logger.isDebugEnabled()) {
                        logger.debug("Number of rows returned from ASSET : " + reportQueryData.getNumberOfRows());
                     }
                     // Set the dataPresent flag based on the number of rows
                     if (reportQueryData.getNumberOfRows() > 0) {
                        aggregateQuery.setDataPresent(CheckType.YES);
                     }
                     aggregateQuery.setQueryExecutionTime(reportQueryData.getQueryExecutionTime());
                  }
                  aggregateQuery.setReportTypes(reportTypes);

                  if (isValidForScatter(reportMetaInfo)
                           && AggregateQueryType.DETAILED_SUMMARY == aggregateQuery.getType()) {
                     queryDataByDetailedAggregateQuery.put(aggregateQuery, reportQueryData);
                     reportMetaInfoByDetailedAggregateQuery.put(aggregateQuery, clonedReportMetaInfo);
                  }
               }

               aggregateQueryList.add(aggregateQuery);
            }

            // Deduce scatter for detailed summary and add it to the aggregate query list 
            if (reportMetaInfo.isGenerateScatterReport()
                     && ExecueCoreUtil.isMapNotEmpty(reportMetaInfoByDetailedAggregateQuery)) {
               List<AggregateQuery> scatterAggregateQueries = new ArrayList<AggregateQuery>();
               for (Entry<AggregateQuery, ReportMetaInfo> scatterReportMetaInfoByAggregateQueryEntry : reportMetaInfoByDetailedAggregateQuery
                        .entrySet()) {
                  AggregateQuery detailedAggregateQuery = scatterReportMetaInfoByAggregateQueryEntry.getKey();
                  ReportMetaInfo detailedReportMetaInfo = scatterReportMetaInfoByAggregateQueryEntry.getValue();

                  List<BusinessAssetTerm> metrics = detailedReportMetaInfo.getAssetQuery().getLogicalQuery()
                           .getMetrics();

                  List<List<BusinessAssetTerm>> combinationMetricsList = getReportAggregationHelper()
                           .getCombinationMetricsForScatter(metrics);

                  if (ExecueCoreUtil.isCollectionEmpty(combinationMetricsList)) {
                     break;
                  }

                  ReportQueryData reportQueryData = queryDataByDetailedAggregateQuery.get(detailedAggregateQuery);
                  for (List<BusinessAssetTerm> combinationMetrics : combinationMetricsList) {

                     ReportQueryData scatterReportQueryData = getScatterReportQueryData(reportQueryData,
                              combinationMetrics);

                     // Prepare the AggregateQuery for scatter by overriding just the query data in case of detailed summary  
                     AggregateQuery scatterAggregateQuery = getReportAggregationHelper().cloneAggregateQuery(
                              detailedAggregateQuery, true);
                     scatterAggregateQuery.setType(AggregateQueryType.SCATTER);
                     scatterAggregateQuery.setXmlData(new QueryDataXMLWriter().toXML(scatterReportQueryData));

                     // Add to the scatter list
                     scatterAggregateQueries.add(scatterAggregateQuery);
                  }
               }

               // Add scatter aggregate query info's to the list
               if (ExecueCoreUtil.isCollectionNotEmpty(scatterAggregateQueries)) {
                  aggregateQueryList.addAll(scatterAggregateQueries);
               }
            }
         }
         if (logger.isDebugEnabled()) {
            logger.debug("\n============== End of Aggregation ==============\n");
         }
      } catch (ReportException reportException) {
         throw new AggregationException(reportException.getCode(), reportException.getMessage(), reportException
                  .getCause());
      } catch (ReportMetadataException reportMetadataException) {
         throw new AggregationException(reportMetadataException.getCode(), reportMetadataException.getMessage(),
                  reportMetadataException.getCause());
      } catch (ReportQueryGenerationException reportQueryGenerationException) {
         throw new AggregationException(reportQueryGenerationException.getCode(), reportQueryGenerationException
                  .getMessage(), reportQueryGenerationException.getCause());
      }
      return aggregateQueryList;
   }

   private ReportQueryData getScatterReportQueryData (ReportQueryData reportQueryData,
            List<BusinessAssetTerm> combinationMetrics) {

      List<ReportType> reportTypes = new ArrayList<ReportType>();
      reportTypes.add(ReportType.ScatterChart);

      ReportQueryData scatterReportQueryData = new ReportQueryData();
      scatterReportQueryData.setNumberOfRows(reportQueryData.getNumberOfRows());
      scatterReportQueryData.setQueryData(getQueryDataForScatter(reportQueryData.getQueryData(), combinationMetrics,
               reportTypes));
      scatterReportQueryData.setQueryExecutionTime(reportQueryData.getQueryExecutionTime());
      scatterReportQueryData.setReportTypes(reportTypes);

      return scatterReportQueryData;
   }

   private QueryData getQueryDataForScatter (QueryData queryData, List<BusinessAssetTerm> combinationMetrics,
            List<ReportType> reportTypes) {

      // Prepare the scatter query data header column meta
      QueryDataHeader queryDataHeader = queryData.getQueryDataHeader();
      List<QueryDataHeaderColumnMeta> queryDataHeaderColumns = queryDataHeader.getQueryDataHeaderColumns();
      List<QueryDataHeaderColumnMeta> scatterQueryDataHeaderColumns = new ArrayList<QueryDataHeaderColumnMeta>();

      Map<String, QueryDataHeaderColumnMeta> queryDataHeaderColumnMetaByDescription = getColumnMetaByDescription(queryDataHeaderColumns);

      for (BusinessAssetTerm businessAssetTerm : combinationMetrics) {
         String reportColumnDescription = ReportAggregationHelper.getReportColumnDescription(businessAssetTerm);
         QueryDataHeaderColumnMeta queryDataHeaderColumnMeta = queryDataHeaderColumnMetaByDescription
                  .get(reportColumnDescription);
         scatterQueryDataHeaderColumns.add(queryDataHeaderColumnMeta);
      }

      // Prepare the scatter query data header
      QueryDataHeader scatterQueryDataHeader = new QueryDataHeader();
      scatterQueryDataHeader.setColumnCount(combinationMetrics.size());
      scatterQueryDataHeader.setQueryDataHeaderColumns(scatterQueryDataHeaderColumns);
      scatterQueryDataHeader.setQueryDataHeaderHierarchyMeta(null);
      scatterQueryDataHeader.setTitle(queryDataHeader.getTitle());

      // Prepare the scatter query data header rows
      QueryDataRows queryDataRows = queryData.getQueryDataRows();
      List<QueryDataRowData> queryDataRowsList = queryDataRows.getQueryDataRowsList();
      List<QueryDataRowData> scatterQueryDataRowsList = new ArrayList<QueryDataRowData>();
      for (QueryDataRowData queryDataRowData : queryDataRowsList) {
         List<QueryDataColumnData> queryDataColumns = queryDataRowData.getQueryDataColumns();
         List<QueryDataColumnData> scatterQueryDataColumns = getQueryDataColumnsForScatter(queryDataColumns,
                  scatterQueryDataHeaderColumns);

         // Prepare the query data row data for scatter 
         QueryDataRowData scatterQueryDataRowData = new QueryDataRowData();
         scatterQueryDataRowData.setQueryDataColumns(scatterQueryDataColumns);

         // Add to the list
         scatterQueryDataRowsList.add(scatterQueryDataRowData);
      }

      // Prepare the scatter query data rows
      QueryDataRows scatterQueryDataRows = new QueryDataRows();
      scatterQueryDataRows.setQueryDataRowsList(scatterQueryDataRowsList);

      // Prepare the scatter query data
      QueryData scatterQueryData = new QueryData();
      scatterQueryData.setQueryDataHeader(scatterQueryDataHeader);
      scatterQueryData.setQueryDataRows(scatterQueryDataRows);

      return scatterQueryData;
   }

   private List<QueryDataColumnData> getQueryDataColumnsForScatter (List<QueryDataColumnData> queryDataColumns,
            List<QueryDataHeaderColumnMeta> scatterQueryDataHeaderColumns) {

      List<QueryDataColumnData> scatterQueryDataColumns = new ArrayList<QueryDataColumnData>();
      Map<String, QueryDataColumnData> queryDataColumnsByName = getQueryDataColumnsByName(queryDataColumns);
      for (QueryDataHeaderColumnMeta queryDataHeaderColumnMeta : scatterQueryDataHeaderColumns) {
         QueryDataColumnData scatterQueryDataColumnData = queryDataColumnsByName.get(queryDataHeaderColumnMeta.getId());
         scatterQueryDataColumns.add(scatterQueryDataColumnData);
      }

      return scatterQueryDataColumns;
   }

   private Map<String, QueryDataColumnData> getQueryDataColumnsByName (List<QueryDataColumnData> queryDataColumns) {

      Map<String, QueryDataColumnData> queryDataColumnsByName = new HashMap<String, QueryDataColumnData>();

      for (QueryDataColumnData queryDataColumnData : queryDataColumns) {
         queryDataColumnsByName.put(queryDataColumnData.getColumnName(), queryDataColumnData);
      }

      return queryDataColumnsByName;
   }

   private Map<String, QueryDataHeaderColumnMeta> getColumnMetaByDescription (
            List<QueryDataHeaderColumnMeta> queryDataHeaderColumns) {

      Map<String, QueryDataHeaderColumnMeta> queryDataHeaderColumnMetaByDescription = new HashMap<String, QueryDataHeaderColumnMeta>();
      for (QueryDataHeaderColumnMeta queryDataHeaderColumnMeta : queryDataHeaderColumns) {
         queryDataHeaderColumnMetaByDescription.put(queryDataHeaderColumnMeta.getDescription(),
                  queryDataHeaderColumnMeta);
      }

      return queryDataHeaderColumnMetaByDescription;
   }

   private boolean isValidForScatter (ReportMetaInfo reportMetaInfo) {
      if (reportMetaInfo.isGenerateScatterReport() && !reportMetaInfo.isGenerateBusinessSummary()
               && !reportMetaInfo.isGenerateOnlyDataBrowser() && reportMetaInfo.isGenerateDetailReport()) {
         return true;
      }
      return false;
   }

   /**
    * @param aggregateQuery
    * @return boolean true/false
    */
   private boolean skipDataExtraction (AggregateQuery aggregateQuery) {
      return AggregateQueryType.HIERARCHY_SUMMARY == aggregateQuery.getType()
               || AggregateQueryType.SCATTER == aggregateQuery.getType();
   }

   public ReportQueryData extractQueryDataForPresentationTimeReport (String finalQuery,
            String aggregationMetaInfoAsString) throws AggregationException {
      try {

         // Transform the aggregation meta info string to the aggregation meta info object
         AggregationMetaInfo aggregationMetaInfo = (AggregationMetaInfo) getReportAggregationHelper()
                  .getXStreamForMetaInfoTransformation().fromXML(aggregationMetaInfoAsString);

         // Reconstruct the report meta info object from the aggregation meta info
         ReportMetaInfo reportMetaInfo = getReportAggregationHelper().constructReportMetaInfo(aggregationMetaInfo);
         reportMetaInfo.setFinalQuery(finalQuery);

         Map<String, BusinessAssetTerm> aliasBusinessAssetTerm = new HashMap<String, BusinessAssetTerm>();

         // Generate the report query data
         ReportQueryData reportQueryData = getReportDataExtractionService().generateReportQueryData(
                  reportMetaInfo, aliasBusinessAssetTerm);

         // Perform post processing on the report query data
         reportQueryData = getReportPostProcessorService().processQueryData(reportMetaInfo, reportQueryData);

         return reportQueryData;
      } catch (ReportException e) {
         throw new AggregationException(e.getCode(), e.getCause());
      } catch (ReportQueryGenerationException reportQueryGenerationException) {
         throw new AggregationException(reportQueryGenerationException.getCode(), reportQueryGenerationException
                  .getCause());
      }
   }

   public ReportQueryData alterLimitsAndExtractQueryDataForDetailReport (String finalQuery,
            String aggregationMetaInfoAsString, Page pageDetail) throws AggregationException {
      try {
         // Transform the aggregation meta info string to the aggregation meta info object
         AggregationMetaInfo aggregationMetaInfo = (AggregationMetaInfo) getReportAggregationHelper()
                  .getXStreamForMetaInfoTransformation().fromXML(aggregationMetaInfoAsString);

         // Update the aggregation meta info with the page detail information
         long beginIndex = (pageDetail.getRequestedPage() - 1) * pageDetail.getPageSize() + 1;
         long endIndex = pageDetail.getRequestedPage() * pageDetail.getPageSize();
         aggregationMetaInfo.getStructuredQuery().getTopBottom().setStartValue((int) beginIndex);
         aggregationMetaInfo.getStructuredQuery().getTopBottom().setLimitValue((int) endIndex);

         // Set the total record count from aggregation meta info to the page detail 
         pageDetail.setRecordCount(aggregationMetaInfo.getTotalCount());

         // Reconstruct the report meta info object from the aggregation meta info
         ReportMetaInfo reportMetaInfo = getReportAggregationHelper().constructReportMetaInfo(aggregationMetaInfo);
         reportMetaInfo.setFinalQuery(finalQuery);

         Map<String, BusinessAssetTerm> aliasBusinessAssetTerm = new HashMap<String, BusinessAssetTerm>();

         // Generate the report query data
         ReportQueryData reportQueryData = getReportDataExtractionService().generateReportQueryData(
                  reportMetaInfo, aliasBusinessAssetTerm);

         // Perform post processing on the report query data
         reportQueryData = getReportPostProcessorService().processQueryData(reportMetaInfo, reportQueryData);

         return reportQueryData;
      } catch (ReportException e) {
         throw new AggregationException(e.getCode(), e.getCause());
      } catch (ReportQueryGenerationException reportQueryGenerationException) {
         throw new AggregationException(reportQueryGenerationException.getCode(), reportQueryGenerationException
                  .getCause());
      }
   }

   public void populateReportData () {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public IReportMetadataAnalyzerService getReportMetadataAnalyzerService () {
      return reportMetadataAnalyzerService;
   }

   public void setReportMetadataAnalyzerService (IReportMetadataAnalyzerService reportMetadataAnalyzerService) {
      this.reportMetadataAnalyzerService = reportMetadataAnalyzerService;
   }

   public IReportQueryGenerationService getReportQueryGenerationService () {
      return reportQueryGenerationService;
   }

   public void setReportQueryGenerationService (IReportQueryGenerationService reportQueryGenerationService) {
      this.reportQueryGenerationService = reportQueryGenerationService;
   }

   public IReportDataExtractionService getReportDataExtractionService () {
      return reportDataExtractionService;
   }

   public void setReportDataExtractionService (IReportDataExtractionService reportDataExtractionService) {
      this.reportDataExtractionService = reportDataExtractionService;
   }

   public IReportSelectionService getReportSelectionService () {
      return reportSelectionService;
   }

   public void setReportSelectionService (IReportSelectionService reportSelectionService) {
      this.reportSelectionService = reportSelectionService;
   }

   public ReportAggregationHelper getReportAggregationHelper () {
      return reportAggregationHelper;
   }

   public void setReportAggregationHelper (ReportAggregationHelper reportAggregationHelper) {
      this.reportAggregationHelper = reportAggregationHelper;
   }

   public IReportPostProcessorService getReportPostProcessorService () {
      return reportPostProcessorService;
   }

   public void setReportPostProcessorService (IReportPostProcessorService reportPostProcessorService) {
      this.reportPostProcessorService = reportPostProcessorService;
   }
}