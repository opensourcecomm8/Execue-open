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


package com.execue.qdata.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.execue.core.IService;
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
import com.execue.qdata.exception.QueryDataException;

/**
 * This service has the methods to work on QData schema
 * 
 * @author kaliki
 * @version 4.0
 * @since 25/06/09
 */
public interface IQueryDataService extends IService {

   public QDataAggregatedQuery getQdataAggregatedQueryById (long aggregateQueryId) throws QueryDataException;

   public List<QDataUserQuery> getUserQuerysByName (String userQuery) throws QueryDataException;

   /**
    * Retrieve Business query as stored in Query Data.
    * 
    * @param queryId
    * @return QDataBusinessQuery
    * @throws QueryDataException
    */
   public QDataBusinessQuery getBusinessQuery (Long queryId) throws QueryDataException;

   /**
    * Retrieve Business query with column values as stored in Query Data.
    * 
    * @param queryId
    * @return QDataBusinessQuery
    * @throws QueryDataException
    */
   public QDataBusinessQuery getBusinessQueryWithColumns (Long queryId) throws QueryDataException;

   /**
    * Retrieve list of Aggregated query stored along with report types.
    * 
    * @param queryId
    * @return List for QDataAggregatedQuery
    * @throws QueryDataException
    */
   public List<QDataAggregatedQuery> getAggregateQueries (Long queryId, AggregateQueryType aggregateQueryType)
            throws QueryDataException;

   /**
    * Retrieves the Aggregated query by id
    * 
    * @param aggregateQueryId
    * @return QDataAggregatedQuery
    * @throws QueryDataException
    */
   public QDataAggregatedQuery getAggregatedQueryById (Long aggregateQueryId) throws QueryDataException;

   /**
    * Retrieve list for Assets matched to the query Id.
    * 
    * @param queryId
    * @return List<Asset>
    * @throws QueryDataException
    */
   public List<Asset> getMatchedAssets (Long queryId) throws QueryDataException;

   /**
    * Retrieve Aggregated Query stored for queryId and assetId
    * 
    * @param queryId
    * @param assetId
    * @param type
    *           Business Summary or Total summary
    * @return
    * @throws QueryDataException
    */
   public QDataAggregatedQuery getAggregatedQuery (Long queryId, Long assetId, Long businessQueryId,
            AggregateQueryType type) throws QueryDataException;

   /**
    * @param queryId
    * @param assetId
    * @param type
    *           Business Summary or Total summary
    * @return
    * @throws QueryDataException
    * @Deprecated
    */
   public List<ReportType> getReportTypes (Long queryId, Long assetId, AggregateQueryType type)
            throws QueryDataException;

   /**
    * @param aggregateQueryId
    * @return list of report types
    * @throws QueryDataException
    */
   public List<ReportType> getReportTypes (Long aggregateQueryId) throws QueryDataException;

   /**
    * get XML data for the query ID. Business Summary assumed.
    * 
    * @param queryId
    * @param assetId
    * @return
    * @throws QueryDataException
    */
   public String getReportXMLData (Long queryId, Long assetId, Long businessQueryId, AggregateQueryType type)
            throws QueryDataException;

   /**
    * get XML data for a specific aggregate query ID.
    * 
    * @param aggregateQueryId
    * @return
    * @throws QueryDataException
    */
   public String getReportXMLData (Long aggregateQueryId) throws QueryDataException;

   /**
    * Store User Query method should take care of storing QueryForm into User_Query_Column
    * 
    * @param queryId
    * @param queryForm
    * @param normalizedQuery
    * @throws QueryDataException
    */
   public QDataUserQuery storeUserQuery (Long queryId, QueryForm queryForm, String normalizedQuery, Long userId,
            CheckType anonymous) throws QueryDataException;

   /**
    * Store Reduced Form method should take care of storing QueryForm into User_Query_Column
    * 
    * @param queryId
    * @param queryForm
    * @param normalizedQuery
    * @throws QueryDataException
    *            TODO: DONT IMPLEMENT . THIS WILL BE USED WHEN NLP IS INTEGRATED
    */
   // public void storeUserQuery (Long queryId, String userQuery) throws QueryDataException;
   // public void storeReducedQuery (Long queryId) throws QueryDataException;
   /**
    * Store Business Query method should take care of storing Business Query into Business_Query_Column
    * 
    * @param queryId
    * @param businessQuery
    * @param noramalizedQuery
    * @throws QueryDataException
    */
   public QDataBusinessQuery storeBusinessQuery (Long queryId, BusinessQuery businessQuery, String noramalizedQuery)
            throws QueryDataException;

   /**
    * Store Business Query method should take care of storing Aggregate Query into Aggretate_Query_Column. Should store
    * all report types should save xml data if Aggregate Query is of type Business Summary
    * 
    * @param queryId
    * @param aggregatedQuery
    * @param noramalizedQuery
    * @param title
    * @param governorSQL
    * @param aggregateSQL
    * @param aggregatedQueryStructure
    * @param governorQueryStructure
    * @throws QueryDataException
    */
   public void storeAggregateQuery (Long queryId, Long businessQueryId, AggregateQuery aggregatedQuery,
            String noramalizedQuery, String title, String governorSQL, String aggregateSQL,
            String governorQueryStructure, String aggregatedQueryStructure) throws QueryDataException;

   public void storeReportData (QDataReportData qdataReportData, QDataAggregatedQuery qdataAggregatedQuery)
            throws QueryDataException;

   public void updateFlagsForAggregateQuery (QDataAggregatedQuery qDataAggregatedQuery) throws QueryDataException;

   public QDataUserQuery getUserQuery (Long userQueryId) throws QueryDataException;

   public List<QDataDimensionInfo> getQDataDimensionInfo (long assetId) throws QueryDataException;

   public List<QDataDimensionCombinationInfo> getQDataDimensionCombinationInfo (long assetId) throws QueryDataException;

   public List<QDataAggregatedQuery> getAllAggregateQueries (Long queryId) throws QueryDataException;

   public List<QueryCacheResultInfo> getQueryCacheResults (Long userQueryId, Long applicationId)
            throws QueryDataException;

   public void updateQDataUserQuery (QDataUserQuery userQuery) throws QueryDataException;

   public QDataReducedQuery storeReducedQuery (QDataReducedQuery qDataReducedQuery) throws QueryDataException;

   public QDataReducedQuery getQdataReducedQueryById (Long reducedQueryId) throws QueryDataException;

   public List<QDataUserQuery> getUserQueriesByReducedQueryIds (List<Long> reducedQueryIds) throws QueryDataException;

   public void updateReducedQuery (QDataReducedQuery qdataReducedQuery) throws QueryDataException;

   public List<NewsItem> getNewsItemsByCategory (NewsCategory category, Long size) throws QueryDataException;

   public List<QDataUserQuery> getUserQueriesByIds (List<Long> userQueryIds) throws QueryDataException;

   public List<QDataAggregatedQuery> getAggregatedQueriesByUserQueryId (Long userQueryId) throws QueryDataException;

   public QDataAggregatedQueryColumn getMatchedAggregatedQueryColumnByConceptBedId (Long conceptBedId, Long aggQueryId)
            throws QueryDataException;

   public long storeCachedReportResults (QDataCachedReportResults cachedReportResults, Long aggregateQueryId)
            throws QueryDataException;

   public void updateCachedReportResults (QDataCachedReportResults cachedReportResults) throws QueryDataException;

   public QDataCachedReportResults getCachedReportResultsById (Long aggregateQueryId, ReportType reportType)
            throws QueryDataException;

   public QDataCachedReportResults getCachedReportResultsById (Long cachedReportId) throws QueryDataException;

   public QDataDimensionInput getQDataDimensionInput (long assetId) throws QueryDataException;

   public void deleteOptimalDSetSwiInfoByAssetId (Long assetId) throws QueryDataException;

   public void createAllOptimalDSetSwiInfos (List<OptimalDSetSWIInfo> opList) throws QueryDataException;

   public Map<String, Double> getPastUsagePatternInfoMap (Long assetId, Date queryExecutionDate)
            throws QueryDataException;

   public List<QueryHistoryBusinessEntityInfo> getPastUsageProminentBusinessEntities (Long assetId,
            Date queryExecutionDate, ColumnType columnType, Integer limit) throws QueryDataException;

   public List<UserSearchAuditOutput> populateUserSearchAudit (UserSearchAuditInput input) throws QueryDataException;
}
