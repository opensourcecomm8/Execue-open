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


package com.execue.web.core.action.reporting.presentation;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.execue.core.common.bean.RelatedAssetResult;
import com.execue.core.common.bean.ReportGroupResult;
import com.execue.core.common.bean.ReportListWrapper;
import com.execue.core.common.bean.entity.ReportGroup;
import com.execue.core.common.bean.entity.ReportType;
import com.execue.core.common.bean.qdata.QDataAggregatedQuery;
import com.execue.core.common.bean.qdata.QDataAggregatedReportType;
import com.execue.core.common.bean.qdata.QDataCachedReportResults;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.exception.PresentationExceptionCodes;
import com.execue.handler.presentation.IPresentationHandler;
import com.execue.handler.reports.IReportController;
import com.execue.handler.reports.IScatterChartController;
import com.execue.platform.querydata.IQueryDataPlatformRetrievalService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IQueryDataService;
import com.execue.swi.service.ILookupService;
import com.opensymphony.xwork2.ActionSupport;

public class ReportViewActionNew extends ActionSupport implements ServletRequestAware, ServletResponseAware {

   private static final long                  serialVersionUID = -7774693888012292221L;
   private static Logger                      log              = Logger.getLogger(ReportViewActionNew.class);

   private Long                               assetId;
   private Long                               queryId;
   private Long                               businessQueryId;
   private String                             source;
   private String                             agQueryIdList;
   private HttpServletRequest                 request;
   private HttpServletResponse                response;
   private ReportListWrapper                  reportListWrapper;
   private IReportController                  reportController;
   private IReportController                  detailReportController;
   private IReportController                  hierarchicalReportController;
   private IScatterChartController            scatterChartController;
   private IQueryDataService                  queryDataService;
   private IQueryDataPlatformRetrievalService queryDataPlatformRetrievalService;
   private ILookupService                     lookupService;
   private IPresentationHandler               presentationHandler;
   private boolean                            hasDetailReport;
   private String                             relatedUserQueryIds;
   private List<RelatedAssetResult>           relatedAssetResults;
   private Long                               applicationId;
   private Long                               verticalId;
   private String                             title;
   private String                             requestedString;
   private String                             type;
   private InputStream                        inputStream;
   private Long                               cachedReportId;

   @Override
   public String execute () {
      String userQuery = null;

      // process the related search user query ids
      if (ExecueCoreUtil.isNotEmpty(request.getParameter("title"))) {
         userQuery = request.getParameter("title");
      }
      try {
         // parse the comma separated string to get the user query ids as a list
         List<Long> relatedUserQueryIdList = getRelatedUserQueryIdList(relatedUserQueryIds);

         // query to get the agg queries by the user query ids
         if (ExecueCoreUtil.isCollectionNotEmpty(relatedUserQueryIdList)) {

            List<QDataAggregatedQuery> aggregatedQueries = populateQDataAggregatedQuery(relatedUserQueryIdList);
            // group the aggregated queries by the asset id and the user query ids so that the detail report queries if
            // any get attached to the business summary queries
            List<List<QDataAggregatedQuery>> aggregatedQueriesComboList = getAggregatedQueriesComboList(aggregatedQueries);

            // create a map with the app name as the key and the list of aggregated query bunches
            Map<String, List<List<QDataAggregatedQuery>>> appNameMap = prepareAppNameAggregatedQueriesMap(aggregatedQueriesComboList);
            try {
               // -- Start of top cluster logic
               // TODO: -JM- revisit later to improvise

               // Limit the number of application entries to be displayed
               Map<String, List<List<QDataAggregatedQuery>>> filteredEntriesMap = getFilteredEntries(appNameMap);

               // run top cluster to retain only the relevant aggregate queries for each of the application
               List<List<QDataAggregatedQuery>> topClusterOfRelatedQueries;

               topClusterOfRelatedQueries = getTopClusterOfRelatedQueries(filteredEntriesMap);

               // prepare the AssetResult list from the top cluster loaded aggregate queries
               prepareRelatedAssetResults(topClusterOfRelatedQueries, userQuery);

               // -- End of top cluster logic
            } catch (QueryDataException e) {
               log.error("Exception occured while retrieving top cluster queries: " + e.getMessage());
               e.printStackTrace();
            }
         }
      } catch (QueryDataException e) {
         log.error("Exception occured: " + e.getMessage());
         e.printStackTrace();
      }

      List<Long> aggregatedQueryIds = prepareAggregatedQueryIdsFromCommaSepratedIdList();
      try {
         List<Long> scatterChartAgQueryIds = new ArrayList<Long>();
         List<QDataAggregatedQuery> aggregateQueries = filterScatterTypeAggregatedQueryIds(scatterChartAgQueryIds,
                  aggregatedQueryIds);
         if (ExecueCoreUtil.isCollectionNotEmpty(aggregateQueries)) {
            for (QDataAggregatedQuery aggregateQuery : aggregateQueries) {
               // setting the variables which will be used by the page3
               Long aggregateQueryId = aggregateQuery.getId();
               assetId = aggregateQuery.getAssetId();
               queryId = aggregateQuery.getUserQuery().getId();
               businessQueryId = aggregateQuery.getBusinessQueryId();

               Set<QDataAggregatedReportType> aggregateReportTypes = aggregateQuery.getReportTypes();
               List<Integer> reportTypes = new ArrayList<Integer>();

               if (isDataBrowserExist(aggregateReportTypes, reportTypes)) {
                  try {
                     // lazzy loading of the data
                     // presentationHandler.processReportRequest(aggQueryId);
                     getDetailReportController().selectReportHandlers(reportListWrapper, request, response,
                              aggregateQueryId);
                  } catch (HandlerException e) {
                     log.error("Exception occured while presenting detailReport: " + e.getMessage());
                     e.printStackTrace();
                  }
               } else if (AggregateQueryType.HIERARCHY_SUMMARY == aggregateQuery.getType()) {
                  try {
                     getHierarchicalReportController().selectReportHandlers(reportListWrapper, request, response,
                              aggregateQueryId);
                  } catch (HandlerException e) {
                     log.error("Exception occured while presenting detailReport: " + e.getMessage());
                     e.printStackTrace();
                  }
               } else {
                  String xmlReportData = queryDataService.getReportXMLData(aggregateQuery.getId());
                  try {
                     getReportController().selectReportHandlers(reportListWrapper, request, response, xmlReportData);
                  } catch (HandlerException e) {
                     if (PresentationExceptionCodes.REVERT_TO_DEFAULT_GRID == e.getCode()) {
                        log.warn(e.getMessage() + " Query: " + userQuery + " assetId: " + assetId);
                        List<Integer> defaultGridType = new ArrayList<Integer>();
                        defaultGridType.add(com.execue.core.common.type.ReportType.Grid.getValue());
                        xmlReportData = replaceReportTypes(xmlReportData, defaultGridType);
                        try {
                           getReportController().selectReportHandlers(reportListWrapper, request, response,
                                    xmlReportData);
                           if (getPresentationHandler().getConfigurationRevertToDefaultGridFlag())
                              addActionError(getText("error.reporting." + e.getCode()));
                        } catch (HandlerException exception) {
                           log.error("Exception occured while presenting businessSummary report: "
                                    + exception.getMessage());
                           if (exception.getCode() < PresentationExceptionCodes.PRESENTATION_EXCEPTION_RETURN_ERROR_LIMIT) {
                              exception.printStackTrace();
                              return ERROR;
                           }
                        }
                     } else {
                        log.error("Exception occured while presenting businessSummary report: " + e.getMessage());
                        if (e.getCode() < PresentationExceptionCodes.PRESENTATION_EXCEPTION_RETURN_ERROR_LIMIT) {
                           e.printStackTrace();
                           return ERROR;
                        }
                     }
                  }
               }

            }
            // Prepare scatter chart 
            if (ExecueCoreUtil.isCollectionNotEmpty(scatterChartAgQueryIds)) {
               try {
                  getScatterChartController().preapreScatterChartIcon(getReportListWrapper(), request, response,
                           scatterChartAgQueryIds);
               } catch (HandlerException e) {
                  log.error("Exception occured while presenting scatter chart: " + e.getMessage());
                  return ERROR;
               }
            }
         }
      } catch (QueryDataException e) {
         // log.error("Exception while retrieving the aggQueryId for " + aggQueryId + " :error: " + e.getMessage());
         e.printStackTrace();
         return ERROR;
      }

      return "newsuccess";
   }

   private boolean isDataBrowserExist (Set<QDataAggregatedReportType> aggregateReportTypes, List<Integer> reportTypes) {
      boolean dataBrowserExist = false;
      for (QDataAggregatedReportType item : aggregateReportTypes) {
         reportTypes.add(item.getType().getValue());
         if (item.getType().getValue() == 20 || item.getType().getValue() == 98) {
            dataBrowserExist = true;
         }
      }
      return dataBrowserExist;
   }

   private List<Long> prepareAggregatedQueryIdsFromCommaSepratedIdList () {
      List<Long> aggregatedQueryIds = new ArrayList<Long>();
      if (agQueryIdList.contains(",")) {
         String[] stringList = agQueryIdList.split(",");
         for (String item : stringList)
            aggregatedQueryIds.add(Long.parseLong(item));
      } else {
         aggregatedQueryIds.add(Long.parseLong(agQueryIdList));
      }
      return aggregatedQueryIds;
   }

   private Map<String, List<List<QDataAggregatedQuery>>> prepareAppNameAggregatedQueriesMap (
            List<List<QDataAggregatedQuery>> aggregatedQueriesComboList) {
      Map<String, List<List<QDataAggregatedQuery>>> appNameMap = new HashMap<String, List<List<QDataAggregatedQuery>>>();
      for (List<QDataAggregatedQuery> list : aggregatedQueriesComboList) {
         List<List<QDataAggregatedQuery>> mapValue = new ArrayList<List<QDataAggregatedQuery>>();
         String appName = list.get(0).getAsset().getApplication().getName();
         if (appNameMap.containsKey(appName)) {
            // get that list and add the current list to the existing list
            mapValue = appNameMap.get(appName);
            mapValue.add(list);
         } else {
            // add a new entry
            mapValue.add(list);
            appNameMap.put(appName, mapValue);
         }
      }
      return appNameMap;
   }

   private List<QDataAggregatedQuery> populateQDataAggregatedQuery (List<Long> relatedUserQueryIdList)
            throws QueryDataException {
      List<QDataAggregatedQuery> aggregatedQueries = new ArrayList<QDataAggregatedQuery>();
      for (Long uqId : relatedUserQueryIdList) {
         List<QDataAggregatedQuery> aggregatedQueriesByUserQueryId = queryDataService
                  .getAggregatedQueriesByUserQueryId(uqId);
         // Fully load the objects
         for (QDataAggregatedQuery query : aggregatedQueriesByUserQueryId) {
            QDataAggregatedQuery loadedAggregatedQueryById = getQueryDataPlatformRetrievalService()
                     .getLoadedAggregatedQueryById(query.getId());
            aggregatedQueries.add(loadedAggregatedQueryById);
         }
      }
      return aggregatedQueries;
   }

   private List<QDataAggregatedQuery> filterScatterTypeAggregatedQueryIds (List<Long> scatterChartAgQueryIds,
            List<Long> aggregatedQueryIds) throws QueryDataException {
      List<QDataAggregatedQuery> aggregateQueries = new ArrayList<QDataAggregatedQuery>();
      for (Long aggQueryId : aggregatedQueryIds) {
         QDataAggregatedQuery aggregateQuery = queryDataService.getAggregatedQueryById(aggQueryId);
         if (AggregateQueryType.SCATTER.equals(aggregateQuery.getType())) {
            scatterChartAgQueryIds.add(aggregateQuery.getId());
         } else {
            aggregateQueries.add(aggregateQuery);
         }
      }
      return aggregateQueries;
   }

   public String getReportData () {
      try {
         QDataCachedReportResults cachedReportResults = getPresentationHandler().getCachedReportResultsById(
                  getCachedReportId());
         setInputStream(cachedReportResults.getReportData().getBinaryStream());
      } catch (HandlerException e) {
         e.printStackTrace();
         return ERROR;
      } catch (SQLException e) {
         log.error("Exception occured while retriving the image for: " + getCachedReportId() + "\n" + e.getMessage());
         e.printStackTrace();
         return ERROR;
      }
      return SUCCESS;
   }

   private Map<String, List<List<QDataAggregatedQuery>>> getFilteredEntries (
            Map<String, List<List<QDataAggregatedQuery>>> appNameMap) {
      Map<String, List<List<QDataAggregatedQuery>>> filteredMap = new HashMap<String, List<List<QDataAggregatedQuery>>>();
      // TODO -JM- have a pre-defined number (N) of links to be displayed - hence take the first N entries of the map
      int limit = 10;
      int index = 0;
      for (String key : appNameMap.keySet()) {
         if (index++ < limit) {
            filteredMap.put(key, appNameMap.get(key));
         }
      }
      return filteredMap;
   }

   private List<List<QDataAggregatedQuery>> getAggregatedQueriesComboList (List<QDataAggregatedQuery> aggregatedQueries) {
      List<List<QDataAggregatedQuery>> comboList = new ArrayList<List<QDataAggregatedQuery>>();
      for (QDataAggregatedQuery query : aggregatedQueries) {
         List<QDataAggregatedQuery> combo = new ArrayList<QDataAggregatedQuery>();
         for (QDataAggregatedQuery checkQuery : aggregatedQueries) {
            // escape the current query
            if (checkQuery.getId() != query.getId()) {
               // check if the asset id and the user query id are equal and add to the list
               if ((checkQuery.getAssetId() == query.getAssetId())
                        && (checkQuery.getUserQuery().getId() == query.getUserQuery().getId())) {
                  if (!combo.contains(checkQuery)) {
                     combo.add(checkQuery);
                  }
               }
            }
         }
         if (!combo.contains(query)) {
            combo.add(query);
         }
         comboList.add(combo);
      }
      return comboList;
   }

   private void prepareRelatedAssetResults (List<List<QDataAggregatedQuery>> topClusterOfRelatedQueries,
            String userQuery) {
      relatedAssetResults = new ArrayList<RelatedAssetResult>();
      for (List<QDataAggregatedQuery> aggQueries : topClusterOfRelatedQueries) {
         QDataAggregatedQuery aggQuery = aggQueries.get(0);
         RelatedAssetResult relatedAssetResult = new RelatedAssetResult();
         relatedAssetResult.setApplicationName(aggQuery.getAsset().getApplication().getName());
         List<QDataAggregatedReportType> finalReportTypes = new ArrayList<QDataAggregatedReportType>();
         List<Long> aggQueryIds = new ArrayList<Long>();
         for (QDataAggregatedQuery query : aggQueries) {
            aggQueryIds.add(query.getId());
            finalReportTypes.addAll(query.getReportTypes());
         }
         relatedAssetResult.setAggregateQueryId(aggQueryIds);
         relatedAssetResult.setReportHeader(userQuery);
         relatedAssetResult.setBusinessQueryId(aggQuery.getBusinessQueryId());
         relatedAssetResult.setAssetId(aggQuery.getAssetId());
         relatedAssetResult.setDescription(aggQuery.getAsset().getDescription());
         List<Integer> reportTypes = new ArrayList<Integer>();
         for (QDataAggregatedReportType dataAggregatedReportType : finalReportTypes) {
            reportTypes.add(dataAggregatedReportType.getType().getValue());
         }
         relatedAssetResult.setReportGroupList(generateReportGroup(reportTypes));
         relatedAssetResults.add(relatedAssetResult);
      }
   }

   private List<ReportGroupResult> generateReportGroup (List<Integer> reportTypes) {
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

   private List<List<QDataAggregatedQuery>> getTopClusterOfRelatedQueries (
            Map<String, List<List<QDataAggregatedQuery>>> filteredEntriesMap) throws QueryDataException {
      List<List<QDataAggregatedQuery>> finalList = new ArrayList<List<QDataAggregatedQuery>>();
      // for each of the app entries, retain only the entry with the highest asset weight
      for (String key : filteredEntriesMap.keySet()) {
         List<List<QDataAggregatedQuery>> value = filteredEntriesMap.get(key);
         List<QDataAggregatedQuery> selectedList = getTopCluster(value);
         finalList.add(selectedList);
      }
      return finalList;
   }

   private List<QDataAggregatedQuery> getTopCluster (List<List<QDataAggregatedQuery>> filteredList) {
      List<QDataAggregatedQuery> topCluster = new ArrayList<QDataAggregatedQuery>();
      List<Double> assetWts = new ArrayList<Double>();
      // Traverse through the list and pick the best list by asset weight
      for (List<QDataAggregatedQuery> list : filteredList) {
         Double assetWeight = 0.0;
         if (list.size() > 1) {
            boolean found = false;
            // Check in the list if there is a business summary aggregated query
            for (QDataAggregatedQuery query : list) {
               if (AggregateQueryType.BUSINESS_SUMMARY.equals(query.getType())) {
                  assetWeight = query.getAssetWeight();
                  found = true;
                  break;
               }
            }
            if (!found) {
               // take the first element's asset weight
               assetWeight = list.get(0).getAssetWeight();
            }
         } else {
            // there is only one item - get the asset weight
            assetWeight = list.get(0).getAssetWeight();
         }
         assetWts.add(assetWeight);
      }
      // from the asset weights list, pick the index whose asset weight is the highest, if multiple indices are found,
      // take the first
      Map<Double, Integer> map = new HashMap<Double, Integer>();
      int index = 0;
      for (Double wt : assetWts) {
         map.put(wt, new Integer(index));
         index++;
      }
      List<Double> keys = new ArrayList<Double>(map.keySet());
      Collections.sort(keys);
      // take the last element of the sorted list
      Double highest = keys.get(keys.size() - 1);
      Integer idx = map.get(highest);
      topCluster = filteredList.get(idx);
      return topCluster;
   }

   private List<Long> getRelatedUserQueryIdList (String relatedUserQueryIds) {
      // process the related search query ids
      List<Long> relatedUserQueryIdList = new ArrayList<Long>();
      if (ExecueCoreUtil.isNotEmpty(relatedUserQueryIds)) {
         if (relatedUserQueryIds.contains(",")) {
            String[] strRelUserQueryIds = relatedUserQueryIds.split(",");
            for (String strRelUserQueryId : strRelUserQueryIds) {
               relatedUserQueryIdList.add(Long.parseLong(strRelUserQueryId));
            }
         } else {
            relatedUserQueryIdList.add(Long.parseLong(relatedUserQueryIds));
         }
      }
      return relatedUserQueryIdList;
   }

   private String replaceReportTypes (String inputXml, List<Integer> reportTypes) {
      String xmlData = "";
      int beginIndex = inputXml.indexOf("<REPORTTYPES>") + 13;
      int endIndex = inputXml.indexOf("</REPORTTYPES>");
      String[] xmlParts = { inputXml.substring(0, beginIndex), inputXml.substring(endIndex) };
      for (Integer item : reportTypes) {
         xmlData += item + ",";
      }
      xmlData = xmlParts[0] + xmlData + xmlParts[1];
      return xmlData;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public Long getQueryId () {
      return queryId;
   }

   public void setQueryId (Long queryId) {
      this.queryId = queryId;
   }

   public void setServletRequest (HttpServletRequest servletRequest) {
      this.request = servletRequest;
   }

   public void setServletResponse (HttpServletResponse servletResponse) {
      this.response = servletResponse;
   }

   public void setReportController (IReportController reportController) {
      this.reportController = reportController;
   }

   public void setReportListWrapper (ReportListWrapper reportListWrapper) {
      this.reportListWrapper = reportListWrapper;
   }

   public ReportListWrapper getReportListWrapper () {
      return reportListWrapper;
   }

   public void setBusinessQueryId (Long businessQueryId) {
      this.businessQueryId = businessQueryId;
   }

   public Long getBusinessQueryId () {
      return businessQueryId;
   }

   public String getSource () {
      return source;
   }

   public void setSource (String source) {
      this.source = source;
   }

   public String getAgQueryIdList () {
      return agQueryIdList;
   }

   public void setAgQueryIdList (String agQueryIdList) {
      this.agQueryIdList = agQueryIdList;
   }

   public boolean isHasDetailReport () {
      return hasDetailReport;
   }

   public void setHasDetailReport (boolean hasDetailReport) {
      this.hasDetailReport = hasDetailReport;
   }

   public IReportController getDetailReportController () {
      return detailReportController;
   }

   public void setDetailReportController (IReportController detailReportController) {
      this.detailReportController = detailReportController;
   }

   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
   }

   public IPresentationHandler getPresentationHandler () {
      return presentationHandler;
   }

   public void setPresentationHandler (IPresentationHandler presentationHandler) {
      this.presentationHandler = presentationHandler;
   }

   public String getRelatedUserQueryIds () {
      return relatedUserQueryIds;
   }

   public void setRelatedUserQueryIds (String relatedUserQueryIds) {
      this.relatedUserQueryIds = relatedUserQueryIds;
   }

   public List<RelatedAssetResult> getRelatedAssetResults () {
      return relatedAssetResults;
   }

   public void setRelatedAssetResults (List<RelatedAssetResult> relatedAssetResults) {
      this.relatedAssetResults = relatedAssetResults;
   }

   public ILookupService getLookupService () {
      return lookupService;
   }

   public void setLookupService (ILookupService lookupService) {
      this.lookupService = lookupService;
   }

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public Long getVerticalId () {
      return verticalId;
   }

   public void setVerticalId (Long verticalId) {
      this.verticalId = verticalId;
   }

   public String getTitle () {
      return title;
   }

   public void setTitle (String title) {
      this.title = title;
   }

   public String getRequestedString () {
      return requestedString;
   }

   public void setRequestedString (String requestedString) {
      this.requestedString = requestedString;
   }

   public String getType () {
      return type;
   }

   public void setType (String type) {
      this.type = type;
   }

   public InputStream getInputStream () {
      return inputStream;
   }

   public void setInputStream (InputStream inputStream) {
      this.inputStream = inputStream;
   }

   public Long getCachedReportId () {
      return cachedReportId;
   }

   public void setCachedReportId (Long cachedReportId) {
      this.cachedReportId = cachedReportId;
   }

   public IQueryDataPlatformRetrievalService getQueryDataPlatformRetrievalService () {
      return queryDataPlatformRetrievalService;
   }

   public void setQueryDataPlatformRetrievalService (
            IQueryDataPlatformRetrievalService queryDataPlatformRetrievalService) {
      this.queryDataPlatformRetrievalService = queryDataPlatformRetrievalService;
   }

   /**
    * @return the reportController
    */
   public IReportController getReportController () {
      return reportController;
   }

   public void setHierarchicalReportController (IReportController hierarchicalReportController) {
      this.hierarchicalReportController = hierarchicalReportController;
   }

   /**
    * @return the hierarchicalReportController
    */
   public IReportController getHierarchicalReportController () {
      return hierarchicalReportController;
   }

   /**
    * @return the scatterChartController
    */
   public IScatterChartController getScatterChartController () {
      return scatterChartController;
   }

   /**
    * @param scatterChartController the scatterChartController to set
    */
   public void setScatterChartController (IScatterChartController scatterChartController) {
      this.scatterChartController = scatterChartController;
   }

}