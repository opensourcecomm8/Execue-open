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


package com.execue.driver.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.AssetResult;
import com.execue.core.common.bean.ReportGroupResult;
import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.ReportGroup;
import com.execue.core.common.bean.entity.ReportType;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.qdata.QDataAggregatedQuery;
import com.execue.core.common.bean.qdata.QDataAggregatedReportType;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryGenerationInput;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;
import com.execue.core.common.bean.querygen.QueryRepresentation;
import com.execue.core.common.bean.querygen.QueryStructure;
import com.execue.core.common.bean.swi.PossibleAssetInfo;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.MessageStatusType;
import com.execue.core.common.type.SearchFilterType;
import com.execue.core.common.util.QueryFormatUtility;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.driver.configuration.IDriverConfigurationService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IQueryDataService;
import com.execue.querygen.service.QueryGenerationServiceFactory;
import com.execue.swi.service.ILookupService;
import com.ibm.icu.text.SimpleDateFormat;
import com.thoughtworks.xstream.XStream;

/**
 * @author ExeCue
 */
public class DriverHelper implements IDriverHelper {

   private static Logger                 logger = Logger.getLogger(DriverHelper.class);
   private IQueryDataService             queryDataService;
   private ILookupService                lookupService;
   private QueryGenerationServiceFactory queryGenerationServiceFactory;
   private IDriverConfigurationService   driverConfigurationService;

   // public AssetResult populateAssetResult (AssetResult assetResult, Long queryId) throws QueryDataException {
   // QDataAggregatedQuery qDataAQ = queryDataService.getAggregatedQuery(queryId, assetResult.getAssetId(), assetResult
   // .getBusinessQueryId(), AggregateQueryType.BUSINESS_SUMMARY);
   // // read the aggregated query structure and format the structure to prepare a formatted query string
   // String aggregatedQueryStructureXml = qDataAQ.getAggregatedQueryStructure();
   // QueryStructure aggregatedQueryStructure = (QueryStructure) new XStream().fromXML(aggregatedQueryStructureXml);
   // assetResult.setQuery(QueryFormatUtility.prepareFormattedQueryString(aggregatedQueryStructure));
   // assetResult.setRelevance(qDataAQ.getRelevance());
   // assetResult.setReportHeader(qDataAQ.getTitle());
   // assetResult.setPseudoStatement(qDataAQ.getEnglishQueryString());
   // if (CheckType.NO.equals(qDataAQ.isDataPresent())) {
   // assetResult.setDataPresent(false);
   // } else {
   // assetResult.setDataPresent(true);
   // }
   // Set<QDataAggregatedReportType> reportTypeSet = qDataAQ.getReportTypes();
   // List<Integer> reportTypes = new ArrayList<Integer>();
   // for (QDataAggregatedReportType dataAggregatedReportType : reportTypeSet) {
   // reportTypes.add(dataAggregatedReportType.getType().getValue());
   // }
   // assetResult.setReportGroupList(generateReportGroup(reportTypes));
   // return assetResult;
   // }

   // TODO: -JM- add a new service method to get the aggregate queries by asset id and BQ id
   private List<QDataAggregatedQuery> getAggregateQueriesByAssetIdBusinessId (Long queryId, AssetResult assetResult)
            throws QueryDataException {
      List<QDataAggregatedQuery> aggregateQueryList = queryDataService.getAllAggregateQueries(queryId);
      List<QDataAggregatedQuery> aggregateQueries = new ArrayList<QDataAggregatedQuery>();
      for (QDataAggregatedQuery qDataAQ : aggregateQueryList) {
         if (qDataAQ.getAssetId().longValue() == assetResult.getAssetId()
                  && qDataAQ.getBusinessQueryId().longValue() == assetResult.getBusinessQueryId().longValue()) {
            aggregateQueries.add(qDataAQ);
         }
      }
      return aggregateQueries;
   }

   public AssetResult populateAssetResult (AssetResult assetResult, Long queryId, boolean isRelevancePopulated)
            throws QueryDataException {
      // TODO: -JVK- use different service call to get the correct set of Aggregate Queries
      List<QDataAggregatedQuery> aggregateQueryList = getAggregateQueriesByAssetIdBusinessId(queryId, assetResult);
      /*
       * List<QDataAggregatedQuery> aggregateQueryList = queryDataService.getAllAggregateQueries(queryId); List<QDataAggregatedQuery>aggregateQueries =
       * new ArrayList<QDataAggregatedQuery>(); for (QDataAggregatedQuery qDataAQ : aggregateQueryList) { if
       * (qDataAQ.getAssetId().longValue() == assetResult.getAssetId() && qDataAQ.getBusinessQueryId().longValue() ==
       * assetResult.getBusinessQueryId().longValue()) { aggregateQueries.add(qDataAQ); } }
       */

      if (aggregateQueryList.size() == 0) {
         int sleepInterval = driverConfigurationService.getAggregateQueryRetryInterval();
         int maxRetryCount = driverConfigurationService.getAggregateQueryRetryCount();
         int retryCount = 1;
         do {
            // TODO: -JVK- temp fix for the (NO DATA FOUND) condition where the pseudo statement is not displayed on
            // Page 2
            try {
               if (logger.isDebugEnabled()) {
                  logger.debug("Retrieval of Aggregate Query failed, retrying after " + sleepInterval + " msecs...");
               }
               Thread.sleep(sleepInterval);
               aggregateQueryList = getAggregateQueriesByAssetIdBusinessId(queryId, assetResult);
            } catch (InterruptedException e) {
               e.printStackTrace();
               throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_SYSTEM_EXCEPTION_CODE,
                        "Interruption while waiting to retry for retrieving the Aggregate Query", e);
            }
            retryCount++;
         } while (aggregateQueryList.size() == 0 && retryCount <= maxRetryCount);
      }

      // check from the list if the list has a normal AQ, if its not present then use the DAQ
      QDataAggregatedQuery normalQDataAQ = null;
      QDataAggregatedQuery detailQDataAQ = null;
      QDataAggregatedQuery hierarchyQDataAQ = null;
      QDataAggregatedQuery noSummaryQDataAQ = null;
      List<QDataAggregatedReportType> finalReportTypes = new ArrayList<QDataAggregatedReportType>();
      List<Long> hierarchySummaryIds = new ArrayList<Long>();
      List<Long> aggregatedQueryIdList = new ArrayList<Long>();
      boolean useNormalAQDetails = false;
      for (QDataAggregatedQuery qdAQ : aggregateQueryList) {
         aggregatedQueryIdList.add(qdAQ.getId());
         if (AggregateQueryType.DETAILED_SUMMARY.equals(qdAQ.getType())) {
            detailQDataAQ = qdAQ;
         } else if (AggregateQueryType.BUSINESS_SUMMARY.equals(qdAQ.getType())) {
            normalQDataAQ = qdAQ;
         } else if (AggregateQueryType.HIERARCHY_SUMMARY.equals(qdAQ.getType())) {
            hierarchyQDataAQ = qdAQ;
            hierarchySummaryIds.add(qdAQ.getId());
         } else if (AggregateQueryType.NO_SUMMARY.equals(qdAQ.getType())) {
            noSummaryQDataAQ = qdAQ;
         }
      }

      if (normalQDataAQ != null) {
         useNormalAQDetails = true;
         if (driverConfigurationService.isDisplayQueryString()) {
            // read the aggregated query structure and format the structure to prepare a formatted query string
            String aggregatedQueryStructureXml = normalQDataAQ.getAggregatedQueryStructure();
            QueryStructure aggregatedQueryStructure = (QueryStructure) new XStream()
                     .fromXML(aggregatedQueryStructureXml);
            assetResult.setQuery(QueryFormatUtility.prepareFormattedQueryString(aggregatedQueryStructure));
         }
         if (!isRelevancePopulated) {
            assetResult.setRelevance(normalQDataAQ.getRelevance());
         }
         assetResult.setReportHeader(normalQDataAQ.getTitle());
         assetResult.setPseudoStatement(normalQDataAQ.getEnglishQueryString());
         populateAssetInfo(assetResult, normalQDataAQ.getAsset());
         if (CheckType.NO.equals(normalQDataAQ.isDataPresent())) {
            assetResult.setDataPresent(false);
         } else {
            assetResult.setDataPresent(true);
         }
         Set<QDataAggregatedReportType> reportTypeSet = normalQDataAQ.getReportTypes();
         finalReportTypes.addAll(reportTypeSet);
         assetResult.setAggregateQueryType(normalQDataAQ.getType());
      }

      if (detailQDataAQ != null) {
         // use the DAQ for populating the AssetResult
         if (!useNormalAQDetails) {
            if (driverConfigurationService.isDisplayQueryString()) {
               String aggregatedQueryStructureXml = detailQDataAQ.getAggregatedQueryStructure();
               QueryStructure aggregatedQueryStructure = (QueryStructure) new XStream()
                        .fromXML(aggregatedQueryStructureXml);
               assetResult.setQuery(QueryFormatUtility.prepareFormattedQueryString(aggregatedQueryStructure));
            }
            if (!isRelevancePopulated) {
               assetResult.setRelevance(detailQDataAQ.getRelevance());
            }
            assetResult.setReportHeader(detailQDataAQ.getTitle());
            assetResult.setPseudoStatement(detailQDataAQ.getEnglishQueryString());
            populateAssetInfo(assetResult, detailQDataAQ.getAsset());
            if (CheckType.NO.equals(detailQDataAQ.isDataPresent())) {
               assetResult.setDataPresent(false);
            } else {
               assetResult.setDataPresent(true);
            }
            assetResult.setResultFromBusinessAggregateQuery(false);
         }
         Set<QDataAggregatedReportType> reportTypeSet = detailQDataAQ.getReportTypes();
         finalReportTypes.addAll(reportTypeSet);
         assetResult.setAggregateQueryType(detailQDataAQ.getType());
      }

      if (hierarchyQDataAQ != null) {
         Set<QDataAggregatedReportType> reportTypeSet = hierarchyQDataAQ.getReportTypes();
         finalReportTypes.addAll(reportTypeSet);
         assetResult.setAggregateQueryType(hierarchyQDataAQ.getType());
      }

      if (noSummaryQDataAQ != null) {
         if (driverConfigurationService.isDisplayQueryString()) {
            String governorQueryStructureXml = noSummaryQDataAQ.getGovernorQueryStructure();
            QueryStructure governorQueryStructure = (QueryStructure) new XStream().fromXML(governorQueryStructureXml);
            assetResult.setQuery(QueryFormatUtility.prepareFormattedQueryString(governorQueryStructure));
         }
         assetResult.setAggregateQueryType(noSummaryQDataAQ.getType());
         assetResult.setReportHeader(getDriverConfigurationService().getNoSummaryTitle());
         assetResult.setPseudoStatement(noSummaryQDataAQ.getEnglishQueryString());
      }

      List<Integer> reportTypes = new ArrayList<Integer>();
      for (QDataAggregatedReportType dataAggregatedReportType : finalReportTypes) {
         reportTypes.add(dataAggregatedReportType.getType().getValue());
      }
      assetResult.setAggregateQueryId(aggregatedQueryIdList);
      assetResult.setHierarchySummaryIds(hierarchySummaryIds);
      assetResult.setReportGroupList(generateReportGroup(reportTypes));
      return assetResult;
   }

   public List<ReportGroupResult> generateReportGroup (List<Integer> reportTypes) {
      // TODO SS: to change the for loop so that the rGroup is repeated instead of just the report type.
      List<ReportGroupResult> reportGroupList = new ArrayList<ReportGroupResult>();
      for (ReportGroup rGroup : lookupService.getReportGroups()) {
         List<Integer> groupReportTypes = new ArrayList<Integer>();
         // get matching report types
         for (ReportType reportType : rGroup.getReportTypes()) {
            for (Integer integer : reportTypes) {
               if (reportType.getId() == integer) {
                  groupReportTypes.add(integer);
                  // break;
               }
            }
         }
         // for matched list create ReportGroupResults
         if (groupReportTypes.size() > 0) {
            ReportGroupResult result = new ReportGroupResult();
            result.setImageUrl(rGroup.getImageUrl());
            result.setLinkUrl(rGroup.getLinkUrl());
            result.setReportTypes(groupReportTypes);
            result.setGroupType(rGroup.getId());
            reportGroupList.add(result);
         }
      }
      return reportGroupList;
   }

   public void populateReportType (QDataAggregatedQuery dataAggregatedQuery, AssetResult assetResult) {
      assetResult.setReportHeader(dataAggregatedQuery.getTitle());
      assetResult.setReportStatus(MessageStatusType.COMPLETED.getValue());
      Set<QDataAggregatedReportType> reportDataSet = dataAggregatedQuery.getReportTypes();
      List<Integer> reportTypeList = new ArrayList<Integer>();
      if (reportDataSet == null) {
         return;
      }
      for (QDataAggregatedReportType dataAggregatedReportType : reportDataSet) {
         reportTypeList.add(dataAggregatedReportType.getType().getValue());
      }
      assetResult.setReportGroupList(generateReportGroup(reportTypeList));
   }

   public QueryRepresentation getQueryRepresentation (Query query, Asset asset) {
      try {
         QueryGenerationInput input = new QueryGenerationInput();
         input.setTargetAsset(asset);
         QueryGenerationOutput queryGenerationOutput = new QueryGenerationOutput();
         queryGenerationOutput.setQueryGenerationInput(input);
         queryGenerationOutput.setResultQuery(query);
         return queryGenerationServiceFactory.getQueryGenerationService(asset).extractQueryRepresentation(asset,
                  queryGenerationOutput.getResultQuery());
      } catch (Exception e) {
         return null;
      }
   }

   public void populateAssetInfo (AssetResult assetResult, Asset asset) {
      assetResult.setId(asset.getId());
      assetResult.setName(asset.getDisplayName());
      assetResult.setDescription(asset.getDescription());
      assetResult.setAssetType(asset.getType());
   }

   public String formatCachedDate (Date cachedDate) {
      String dateFormatPattern = driverConfigurationService.getCachedDateFormatPattern();
      SimpleDateFormat cachedDateFormat = new SimpleDateFormat(dateFormatPattern);
      String formattedDate = cachedDateFormat.format(cachedDate);
      String dateString = formattedDate.substring(0, formattedDate.length() - 2);
      String amPm = formattedDate.substring(formattedDate.length() - 2).toLowerCase();
      String finalFormattedDate = dateString + amPm;
      return "Cached@" + finalFormattedDate;
   }

   public String formatUnstructuredContentDate (Date contentDate) {
      String dateFormatPattern = driverConfigurationService.getContentDateFormatPattern();
      SimpleDateFormat cachedDateFormat = new SimpleDateFormat(dateFormatPattern);
      String formattedDate = cachedDateFormat.format(contentDate);
      return formattedDate;
   }

   public SearchFilter getGeneralSearchFilter () {
      SearchFilter outFilter = new SearchFilter();
      outFilter.setSearchFilterType(SearchFilterType.GENERAL);
      return outFilter;
   }

   public Set<SemanticPossibility> getScopedPossibilities (Set<SemanticPossibility> allPossibilities) {
      Set<SemanticPossibility> scopedPossibilities = new LinkedHashSet<SemanticPossibility>();
      for (SemanticPossibility possibility : allPossibilities) {
         if (possibility.isScoped()) {
            scopedPossibilities.add(possibility);
         }
      }
      return scopedPossibilities;
   }

   public void correctSearchScoping (Set<SemanticPossibility> possibilities, List<String> appNames) {
      for (SemanticPossibility possibility : possibilities) {
         if (appNames.contains(possibility.getApplication().getName())) {
            possibility.setScoped(true);
         }
      }
   }

   /**
    * Sort the map to have the scoped possibilities at the top and followed by non scoped possibilities in the same
    * order of their relevance with in scope and non scoped groups
    * 
    * @param possibleAssetForPossibilityMap
    * @return
    */
   public Map<Long, List<PossibleAssetInfo>> sortAssetInfoMapBasedOnSearchScope (
            Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap,
            Map<Long, SemanticPossibility> possibilityMap) {

      Map<Long, List<PossibleAssetInfo>> sortedBySearchScopePossibleAssetInfoMap = new LinkedHashMap<Long, List<PossibleAssetInfo>>();

      Map<Long, List<PossibleAssetInfo>> scopedPossibleAssetInfoMap = new LinkedHashMap<Long, List<PossibleAssetInfo>>();
      Map<Long, List<PossibleAssetInfo>> nonScopedPossibleAssetInfoMap = new LinkedHashMap<Long, List<PossibleAssetInfo>>();
      SemanticPossibility possibility = null;
      for (Entry<Long, List<PossibleAssetInfo>> entry : possibleAssetForPossibilityMap.entrySet()) {
         possibility = possibilityMap.get(entry.getKey());
         if (possibility.isScoped()) {
            scopedPossibleAssetInfoMap.put(entry.getKey(), entry.getValue());
         } else {
            nonScopedPossibleAssetInfoMap.put(entry.getKey(), entry.getValue());
         }
      }
      sortedBySearchScopePossibleAssetInfoMap.putAll(scopedPossibleAssetInfoMap);
      sortedBySearchScopePossibleAssetInfoMap.putAll(nonScopedPossibleAssetInfoMap);
      return sortedBySearchScopePossibleAssetInfoMap;
   }

   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
   }

   public ILookupService getLookupService () {
      return lookupService;
   }

   public void setLookupService (ILookupService lookupService) {
      this.lookupService = lookupService;
   }

   public QueryGenerationServiceFactory getQueryGenerationServiceFactory () {
      return queryGenerationServiceFactory;
   }

   public void setQueryGenerationServiceFactory (QueryGenerationServiceFactory queryGenerationServiceFactory) {
      this.queryGenerationServiceFactory = queryGenerationServiceFactory;
   }

   public IDriverConfigurationService getDriverConfigurationService () {
      return driverConfigurationService;
   }

   public void setDriverConfigurationService (IDriverConfigurationService driverConfigurationService) {
      this.driverConfigurationService = driverConfigurationService;
   }

}