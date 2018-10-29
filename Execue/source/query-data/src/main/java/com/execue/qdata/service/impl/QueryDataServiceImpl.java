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


package com.execue.qdata.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.AggregateQuery;
import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.audittrail.UserSearchAuditInput;
import com.execue.core.common.bean.audittrail.UserSearchAuditOutput;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.qdata.NewsItem;
import com.execue.core.common.bean.qdata.OptimalDSetSWIInfo;
import com.execue.core.common.bean.qdata.QDataAggregatedQuery;
import com.execue.core.common.bean.qdata.QDataAggregatedQueryColumn;
import com.execue.core.common.bean.qdata.QDataAggregatedReportType;
import com.execue.core.common.bean.qdata.QDataBusinessQuery;
import com.execue.core.common.bean.qdata.QDataCachedReportResults;
import com.execue.core.common.bean.qdata.QDataDimensionCombinationInfo;
import com.execue.core.common.bean.qdata.QDataDimensionInfo;
import com.execue.core.common.bean.qdata.QDataDimensionInput;
import com.execue.core.common.bean.qdata.QDataReducedQuery;
import com.execue.core.common.bean.qdata.QDataReportData;
import com.execue.core.common.bean.qdata.QDataUserQuery;
import com.execue.core.common.bean.qdata.QueryCacheResultInfo;
import com.execue.core.common.bean.qdata.QueryHistoryBusinessEntityInfo;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.NewsCategory;
import com.execue.core.common.type.ReportType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.qdata.configuration.IQueryDataConfigurationService;
import com.execue.qdata.dataaccess.IQueryDataAccessManager;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.helper.QueryDataServiceHelper;
import com.execue.qdata.service.IQueryDataService;

/**
 * This service has the methods to work on QData schema
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public class QueryDataServiceImpl implements IQueryDataService {

   private IQueryDataConfigurationService queryDataConfigurationService;
   private IQueryDataAccessManager        queryDataAccessManager;

   public QDataAggregatedQuery getQdataAggregatedQueryById (long aggregateQueryId) throws QueryDataException {
      return getQueryDataAccessManager().getQdataAggregatedQueryById(aggregateQueryId);
   }

   public List<QDataUserQuery> getUserQuerysByName (String userQuery) throws QueryDataException {
      return getQueryDataAccessManager().getUserQuerysByName(userQuery);
   }

   public QDataAggregatedQuery getAggregatedQuery (Long queryId, Long assetId, Long businessQueryId,
            AggregateQueryType type) throws QueryDataException {
      QDataAggregatedQuery dataAggregatedQuery = queryDataAccessManager.getQdataAggregatedQuery(queryId, assetId,
               businessQueryId, type);

      Set<QDataAggregatedReportType> reportTypeSet = dataAggregatedQuery.getReportTypes();
      if (reportTypeSet != null) {
         for (QDataAggregatedReportType dataAggregatedReportType : reportTypeSet) {
            dataAggregatedReportType.getId();
         }
      }
      return dataAggregatedQuery;
   }

   public QDataAggregatedQuery getAggregatedQueryById (Long aggregateQueryId) throws QueryDataException {

      QDataAggregatedQuery dataAggregatedQuery = queryDataAccessManager.getQdataAggregatedQueryById(aggregateQueryId);

      Set<QDataAggregatedReportType> reportTypeSet = dataAggregatedQuery.getReportTypes();
      if (reportTypeSet != null) {
         for (QDataAggregatedReportType dataAggregatedReportType : reportTypeSet) {
            dataAggregatedReportType.getId();
         }
      }
      dataAggregatedQuery.getUserQuery().getNormalizedQueryString();

      return dataAggregatedQuery;
   }

   public List<QDataAggregatedQuery> getAggregateQueries (Long queryId, AggregateQueryType aggregateQueryType)
            throws QueryDataException {
      List<QDataAggregatedQuery> dataAggregatedQueryList = queryDataAccessManager.getQdataAggregateQueries(queryId,
               aggregateQueryType);
      for (QDataAggregatedQuery dataAggregatedQuery : dataAggregatedQueryList) {
         Set<QDataAggregatedReportType> reportTypeSet = dataAggregatedQuery.getReportTypes();
         if (reportTypeSet != null) {
            for (QDataAggregatedReportType dataAggregatedReportType : reportTypeSet) {
               dataAggregatedReportType.getId();
            }
         }
      }
      return dataAggregatedQueryList;
   }

   public List<QDataAggregatedQuery> getAllAggregateQueries (Long queryId) throws QueryDataException {
      List<QDataAggregatedQuery> dataAggregatedQueryList = queryDataAccessManager.getAllQdataAggregateQueries(queryId);
      for (QDataAggregatedQuery dataAggregatedQuery : dataAggregatedQueryList) {
         Set<QDataAggregatedReportType> reportTypeSet = dataAggregatedQuery.getReportTypes();
         if (reportTypeSet != null) {
            for (QDataAggregatedReportType dataAggregatedReportType : reportTypeSet) {
               dataAggregatedReportType.getId();
            }
         }
      }
      return dataAggregatedQueryList;
   }

   public QDataUserQuery getUserQuery (Long userQueryId) throws QueryDataException {
      return queryDataAccessManager.getById(userQueryId, QDataUserQuery.class);
   }

   public QDataBusinessQuery getBusinessQuery (Long queryId) throws QueryDataException {
      // TODO Auto-generated method stub
      return null;
   }

   public QDataBusinessQuery getBusinessQueryWithColumns (Long queryId) throws QueryDataException {
      // TODO Auto-generated method stub
      return null;
   }

   public List<Asset> getMatchedAssets (Long queryId) throws QueryDataException {
      // TODO Auto-generated method stub
      return null;
   }

   public List<ReportType> getReportTypes (Long queryId, Long assetId, AggregateQueryType type)
            throws QueryDataException {
      // TODO Auto-generated method stub
      return null;
   }

   public List<ReportType> getReportTypes (Long aggregateQueryId) throws QueryDataException {
      return queryDataAccessManager.getQdataReportTypes(aggregateQueryId);
   }

   public String getReportXMLData (Long queryId, Long assetId, Long businessQueryId, AggregateQueryType type)
            throws QueryDataException {
      return queryDataAccessManager.getDataXML(queryId, assetId, businessQueryId, type);
   }

   public String getReportXMLData (Long aggregateQueryId) throws QueryDataException {
      return queryDataAccessManager.getDataXML(aggregateQueryId);
   }

   public void storeAggregateQuery (Long queryId, Long businessQueryId, AggregateQuery aggregatedQuery,
            String noramalizedQuery, String title, String governorQueryString, String aggregatedQueryString,
            String governorQueryStructure, String aggregatedQueryStructure) throws QueryDataException {

      QDataBusinessQuery businessQuery = new QDataBusinessQuery();
      businessQuery.setId(businessQueryId);

      // prepare and store the aggregated query
      QDataAggregatedQuery qDataAggregatedQuery = new QDataAggregatedQuery();
      qDataAggregatedQuery.setAssetId(aggregatedQuery.getAssetQuery().getLogicalQuery().getAsset().getId());
      qDataAggregatedQuery.setBusinessQuery(businessQuery);
      qDataAggregatedQuery.setEnglishQueryString(noramalizedQuery);
      qDataAggregatedQuery.setGovernorQueryString(governorQueryString);
      qDataAggregatedQuery.setAggregatedQueryString(aggregatedQueryString);
      qDataAggregatedQuery.setGovernorQueryStructure(governorQueryStructure);
      qDataAggregatedQuery.setAggregatedQueryStructure(aggregatedQueryStructure);
      qDataAggregatedQuery.setReportMetaInfoStructure(aggregatedQuery.getReportMetaInfoStructure());
      qDataAggregatedQuery.setRelevance(aggregatedQuery.getAssetQuery().getLogicalQuery().getStructuredQueryWeight());
      qDataAggregatedQuery.setAssetWeight(aggregatedQuery.getAssetQuery().getLogicalQuery().getAssetWeight());
      qDataAggregatedQuery.setExecutionDate(new Date());
      qDataAggregatedQuery.setTitle(title);
      qDataAggregatedQuery.setQueryColumns(QueryDataServiceHelper.generateAggregatedQueryColumns(aggregatedQuery));
      qDataAggregatedQuery.setDataPresent(aggregatedQuery.isDataPresent());
      qDataAggregatedQuery.setType(aggregatedQuery.getType());
      qDataAggregatedQuery.setDataExtracted(aggregatedQuery.isDataExtracted());
      qDataAggregatedQuery.setQueryExecutionTime(aggregatedQuery.getQueryExecutionTime());

      queryDataAccessManager.storeAggregateQuery(qDataAggregatedQuery, queryId);

      // store the report data
      if (StringUtils.isNotBlank(aggregatedQuery.getXmlData())) {
         QDataReportData qDataReportData = new QDataReportData();
         qDataReportData.setPayload(aggregatedQuery.getXmlData());
         storeReportData(qDataReportData, qDataAggregatedQuery);
      }

      // store the report types
      if (ExecueCoreUtil.isCollectionNotEmpty(aggregatedQuery.getReportTypes())) {
         queryDataAccessManager.storeReportTypes(qDataAggregatedQuery, new ArrayList<QDataAggregatedReportType>(
                  QueryDataServiceHelper.genereateQDataReportTypes(aggregatedQuery.getReportTypes())));
      }
   }

   public void storeReportData (QDataReportData qdataReportData, QDataAggregatedQuery qdataAggregatedQuery)
            throws QueryDataException {
      queryDataAccessManager.storeReportData(qdataReportData, qdataAggregatedQuery);
   }

   public QDataBusinessQuery storeBusinessQuery (Long queryId, BusinessQuery businessQuery, String noramalizedQuery)
            throws QueryDataException {
      QDataBusinessQuery qDataBusinessQuery = new QDataBusinessQuery();
      qDataBusinessQuery.setModelId(businessQuery.getModelId());
      qDataBusinessQuery.setPsuedoLanguageQueryString(noramalizedQuery);
      qDataBusinessQuery.setQueryColumns(QueryDataServiceHelper.generateBusinessQueryColumns(businessQuery));
      qDataBusinessQuery.setExecutionDate(new Date());
      qDataBusinessQuery.setApplicationId(businessQuery.getApplicationId());
      qDataBusinessQuery.setApplicationName(businessQuery.getApplicationName());
      qDataBusinessQuery.setRequestRecognition(businessQuery.getRequestRecognition());
      queryDataAccessManager.storeBusinessQuery(qDataBusinessQuery, queryId);
      return qDataBusinessQuery;
   }

   public QDataUserQuery storeUserQuery (Long queryId, QueryForm queryForm, String normalizedQuery, Long userId,
            CheckType anonymous) throws QueryDataException {
      QDataUserQuery qDataUserQuery = new QDataUserQuery();
      qDataUserQuery.setId(queryId);
      qDataUserQuery.setExecutionDate(new Date());
      qDataUserQuery.setNormalizedQueryString(normalizedQuery);
      qDataUserQuery.setUserId(userId);
      qDataUserQuery.setAnonymousUser(anonymous);
      qDataUserQuery.setQueryColumns(QueryDataServiceHelper.generateUserQueryColumns(queryForm));
      queryDataAccessManager.storeUserQuery(qDataUserQuery);
      return qDataUserQuery;
   }

   public QDataReducedQuery storeReducedQuery (QDataReducedQuery qDataReducedQuery) throws QueryDataException {
      return queryDataAccessManager.storeReducedQuery(qDataReducedQuery);
   }

   public List<QDataDimensionCombinationInfo> getQDataDimensionCombinationInfo (long assetId) throws QueryDataException {
      return getQueryDataAccessManager().getQDataDimensionCombinationInfo(assetId);
   }

   public QDataDimensionInput getQDataDimensionInput (long assetId) throws QueryDataException {
      return getQueryDataAccessManager().getQDataDimensionInput(assetId);
   }

   public List<QDataDimensionInfo> getQDataDimensionInfo (long assetId) throws QueryDataException {
      return getQueryDataAccessManager().getQDataDimensionInfo(assetId);
   }

   public void updateFlagsForAggregateQuery (QDataAggregatedQuery qDataAggregatedQuery) throws QueryDataException {
      queryDataAccessManager.updateFlagsForAggregateQuery(qDataAggregatedQuery);
   }

   public List<QueryCacheResultInfo> getQueryCacheResults (Long userQueryId, Long applicationId)
            throws QueryDataException {
      return queryDataAccessManager.getQueryCacheResults(userQueryId, applicationId);
   }

   public void updateQDataUserQuery (QDataUserQuery userQuery) throws QueryDataException {
      queryDataAccessManager.updateQDataUserQuery(userQuery);
   }

   public QDataReducedQuery getQdataReducedQueryById (Long reducedQueryId) throws QueryDataException {

      return queryDataAccessManager.getQdataReducedQueryById(reducedQueryId);
   }

   public List<QDataUserQuery> getUserQueriesByReducedQueryIds (List<Long> reducedQueryIds) throws QueryDataException {
      return queryDataAccessManager.getUserQueriesByReducedQueryIds(reducedQueryIds);
   }

   public void updateReducedQuery (QDataReducedQuery qdataReducedQuery) throws QueryDataException {
      getQueryDataAccessManager().updateReducedQuery(qdataReducedQuery);
   }

   public List<NewsItem> getNewsItemsByCategory (NewsCategory category, Long size) throws QueryDataException {
      return getQueryDataAccessManager().getNewsItemsByCategory(category, size);
   }

   public List<QDataUserQuery> getUserQueriesByIds (List<Long> userQueryIds) throws QueryDataException {
      return getQueryDataAccessManager().getUserQueriesByIds(userQueryIds);
   }

   public List<QDataAggregatedQuery> getAggregatedQueriesByUserQueryId (Long userQueryId) throws QueryDataException {
      return getQueryDataAccessManager().getAggregatedQueriesByUserQueryId(userQueryId);
   }

   public QDataAggregatedQueryColumn getMatchedAggregatedQueryColumnByConceptBedId (Long conceptBedId, Long aggQueryId)
            throws QueryDataException {
      return getQueryDataAccessManager().getMatchedAggregatedQueryColumnByConceptBedId(conceptBedId, aggQueryId);
   }

   public long storeCachedReportResults (QDataCachedReportResults cachedReportResults, Long aggregateQueryId)
            throws QueryDataException {
      QDataAggregatedQuery qdataAggregatedQuery = getQueryDataAccessManager().getQdataAggregatedQueryById(
               aggregateQueryId);
      cachedReportResults.setAggregatedQuery(qdataAggregatedQuery);
      return getQueryDataAccessManager().storeCachedReportResults(cachedReportResults);
   }

   public void updateCachedReportResults (QDataCachedReportResults cachedReportResults) throws QueryDataException {
      getQueryDataAccessManager().storeCachedReportResults(cachedReportResults);
   }

   public QDataCachedReportResults getCachedReportResultsById (Long aggregateQueryId, ReportType reportType)
            throws QueryDataException {
      return getQueryDataAccessManager().getCachedReportResultsById(aggregateQueryId, reportType);
   }

   public QDataCachedReportResults getCachedReportResultsById (Long cachedReportId) throws QueryDataException {
      return getQueryDataAccessManager().getCachedReportResultsById(cachedReportId);
   }

   @Override
   public void createAllOptimalDSetSwiInfos (List<OptimalDSetSWIInfo> optimalDSetSwiInfos) throws QueryDataException {
      getQueryDataAccessManager().createAllOptimalDSetSwiInfos(optimalDSetSwiInfos);
   }

   @Override
   public void deleteOptimalDSetSwiInfoByAssetId (Long assetId) throws QueryDataException {
      getQueryDataAccessManager().deleteOptimalDSetSwiInfoByAssetId(assetId);
   }

   @Override
   public Map<String, Double> getPastUsagePatternInfoMap (Long assetId, Date queryExecutionDate)
            throws QueryDataException {
      Map<String, Double> patternInfos = getQueryDataAccessManager().getPastUsagePatternInfoMap(assetId,
               queryExecutionDate);
      rearrangePatternInfos(patternInfos);
      filterPatternInfos(patternInfos);
      return patternInfos;
   }

   private void filterPatternInfos (Map<String, Double> patternInfos) {
      // TODO :: NK:: TO BE FILLED

   }

   /**
    * This method will rearrange the map by updating the keys by first splitting the bed ids and sorting them. Also sums
    * up the value if the sorted key is already present in the existing map.
    * 
    * @param patternInfos
    */
   private void rearrangePatternInfos (Map<String, Double> patternInfos) {
      if (MapUtils.isEmpty(patternInfos)) {
         return;
      }
      Set<Entry<String, Double>> entrySet = new HashSet<Entry<String, Double>>(patternInfos.entrySet());
      for (Entry<String, Double> entry : entrySet) {
         String key = entry.getKey();
         String[] elements = key.split(getQueryDataConfigurationService().getSqlGroupConcatDefaultDelimeter());
         Collection<String> inputStrings = new TreeSet<String>();
         CollectionUtils.addAll(inputStrings, elements);
         String sortedKey = ExecueCoreUtil.joinCollection(inputStrings);
         if (!key.equalsIgnoreCase(sortedKey)) {
            Double removedValue = patternInfos.remove(key);
            Double value = patternInfos.get(sortedKey);
            if (value == null) {
               // Put the removed value back with the sorted key
               patternInfos.put(sortedKey, removedValue);
            } else {
               // Sum the removed value to the sorted key value and put it back
               value += removedValue;
               patternInfos.put(sortedKey, value);
            }
         }
      }
   }

   @Override
   public List<QueryHistoryBusinessEntityInfo> getPastUsageProminentBusinessEntities (Long assetId,
            Date queryExecutionDate, ColumnType columnType, Integer limit) throws QueryDataException {
      return getQueryDataAccessManager().getPastUsageProminentBusinessEntities(assetId, queryExecutionDate, columnType,
               limit);
   }

   @Override
   public List<UserSearchAuditOutput> populateUserSearchAudit (UserSearchAuditInput input) throws QueryDataException {
      return getQueryDataAccessManager().populateUserSearchAudit(input);
   }

   public void setQueryDataAccessManager (IQueryDataAccessManager queryDataAccessManager) {
      this.queryDataAccessManager = queryDataAccessManager;
   }

   public IQueryDataAccessManager getQueryDataAccessManager () {
      return queryDataAccessManager;
   }

   /**
    * @return the queryDataConfigurationService
    */
   public IQueryDataConfigurationService getQueryDataConfigurationService () {
      return queryDataConfigurationService;
   }

   /**
    * @param queryDataConfigurationService
    *           the queryDataConfigurationService to set
    */
   public void setQueryDataConfigurationService (IQueryDataConfigurationService queryDataConfigurationService) {
      this.queryDataConfigurationService = queryDataConfigurationService;
   }

}