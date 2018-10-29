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


package com.execue.qdata.dataaccess;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.ReportVO;
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
import com.execue.core.common.bean.qdata.QueryHistoryBusinessEntityInfo;
import com.execue.core.common.bean.qdata.QDataReducedQuery;
import com.execue.core.common.bean.qdata.QDataReportData;
import com.execue.core.common.bean.qdata.QDataUserQuery;
import com.execue.core.common.bean.qdata.QueryCacheResultInfo;
import com.execue.core.common.type.AggregateQueryType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.NewsCategory;
import com.execue.core.common.type.ReportType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.qdata.exception.QueryDataException;

/**
 * This data access manager has the methods to work on QData schema
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public interface IQueryDataAccessManager {

   public <DomainObject extends Serializable> DomainObject getById (Long id, Class<DomainObject> clazz)
            throws QueryDataException;

   // for user request identification/short circuiting
   public QDataBusinessQuery getQdataBusinessQuery (long requestID) throws QueryDataException;

   public List<QDataAggregatedQuery> getQdataAggregateQueries (long requestID, AggregateQueryType aggregateQueryType)
            throws QueryDataException;

   public List<Asset> getQdataMatchedAssets (long requestID) throws QueryDataException;

   public QDataAggregatedQuery getQdataAggregatedQuery (long requestID, long assetID, long businessQueryId,
            AggregateQueryType type) throws QueryDataException;

   public QDataAggregatedQuery getQdataAggregatedQueryById (long aggregateQueryId) throws QueryDataException;

   // for reporting
   public List<ReportType> getQdataReportTypes (long requestID, long assetID, long businessQueryId)
            throws QueryDataException;

   public List<ReportType> getQdataReportTypes (long aggregateQueryId) throws QueryDataException;

   // query data for the asset
   public String getDataXML (long requestID, long assetID, long businessQueryId, AggregateQueryType type)
            throws QueryDataException;

   // retrieve report data xml for a specific Aggregate Query Id
   public String getDataXML (long aggregateQueryId) throws QueryDataException;

   // for an asset that answered the query
   public ReportVO getReport (long requestID, long assetID, long businessQueryId) throws QueryDataException;

   // for multiple assets for user query persistence
   public List<ReportVO> getReports (long requestID) throws QueryDataException;

   public void storeUserQuery (QDataUserQuery qDataUserQuery) throws QueryDataException;

   public void storeBusinessQuery (QDataBusinessQuery qdataBusinessQuery, long userQueryId) throws QueryDataException;

   public void storeAggregateQuery (QDataAggregatedQuery qdataAggregatedQuery, long userQueryId)
            throws QueryDataException;

   public void storeReportData (QDataReportData qdataReportData, QDataAggregatedQuery qdataAggregatedQuery)
            throws QueryDataException;

   public void storeReportTypes (QDataAggregatedQuery qdataAggregatedQuery,
            List<QDataAggregatedReportType> qDataReportTypes) throws QueryDataException;

   public void updateFlagsForAggregateQuery (QDataAggregatedQuery qDataAggregatedQuery) throws QueryDataException;

   public List<QDataUserQuery> getUserQuerysByName (String userQuery) throws QueryDataException;

   public List<QDataDimensionInfo> getQDataDimensionInfo (long assetId) throws QueryDataException;

   public List<QDataDimensionCombinationInfo> getQDataDimensionCombinationInfo (long assetId) throws QueryDataException;

   public List<QDataAggregatedQuery> getAllQdataAggregateQueries (Long queryId) throws QueryDataException;

   public List<QueryCacheResultInfo> getQueryCacheResults (Long userQueryId, Long applicationId)
            throws QueryDataException;

   public void updateQDataUserQuery (QDataUserQuery userQuery) throws QueryDataException;

   public QDataReducedQuery storeReducedQuery (QDataReducedQuery reducedQuery) throws QueryDataException;

   public QDataReducedQuery getQdataReducedQueryById (Long reducedQueryId) throws QueryDataException;

   public List<QDataUserQuery> getUserQueriesByReducedQueryIds (List<Long> reducedQueryIds) throws QueryDataException;

   public void updateReducedQuery (QDataReducedQuery qdataReducedQuery) throws QueryDataException;

   public List<NewsItem> getNewsItemsByCategory (NewsCategory category, Long size) throws QueryDataException;

   public List<QDataUserQuery> getUserQueriesByIds (List<Long> userQueryIds) throws QueryDataException;

   public List<QDataAggregatedQuery> getAggregatedQueriesByUserQueryId (Long userQueryId) throws QueryDataException;

   public QDataAggregatedQueryColumn getMatchedAggregatedQueryColumnByConceptBedId (Long conceptBedId, Long aggQueryId)
            throws QueryDataException;

   public long storeCachedReportResults (QDataCachedReportResults cachedReportResults) throws QueryDataException;

   public QDataCachedReportResults getCachedReportResultsById (Long aggregateQueryId, ReportType reportType)
            throws QueryDataException;

   public QDataCachedReportResults getCachedReportResultsById (Long cachedReportId) throws QueryDataException;

   public QDataDimensionInput getQDataDimensionInput (long assetId) throws QueryDataException;

   public Map<String, Double> getPastUsagePatternInfoMap (Long assetId, Date queryExecutionDate)
            throws QueryDataException;

   public void deleteOptimalDSetSwiInfoByAssetId (Long assetId) throws QueryDataException;

   public void createAllOptimalDSetSwiInfos (List<OptimalDSetSWIInfo> opList) throws QueryDataException;

   public List<QueryHistoryBusinessEntityInfo> getPastUsageProminentBusinessEntities (Long assetId,
            Date queryExecutionDate, ColumnType columnType, Integer limit) throws QueryDataException;

   public List<UserSearchAuditOutput> populateUserSearchAudit (UserSearchAuditInput input) throws QueryDataException;
}
