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


package com.execue.qdata.dataaccess.impl;

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
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.qdata.dataaccess.IQueryDataAccessManager;
import com.execue.qdata.dataaccess.QDataDAOComponents;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.exception.QueryDataExceptionCodes;
import com.execue.qdata.exception.RFXExceptionCodes;

/**
 * This data access manager has the methods to work on QData schema
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public class QueryDataAccessManagerImpl extends QDataDAOComponents implements IQueryDataAccessManager {

   public String getDataXML (long requestID, long assetID, long businessQueryId, AggregateQueryType type)
            throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getDataXML(requestID, assetID, businessQueryId, type);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public String getDataXML (long aggregateQueryId) throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getDataXML(aggregateQueryId);
      } catch (DataAccessException e) {
         throw new QueryDataException(e.getCode(), e);
      }
   }

   public List<QDataAggregatedQuery> getQdataAggregateQueries (long requestID, AggregateQueryType aggregateQueryType)
            throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getQdataAggregateQueries(requestID, aggregateQueryType);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<QDataAggregatedQuery> getAllQdataAggregateQueries (Long requestID) throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getAllQdataAggregateQueries(requestID);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public QDataAggregatedQuery getQdataAggregatedQuery (long requestID, long assetID, long businessQueryId,
            AggregateQueryType type) throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getSpecificQDataAggregatedQuery(requestID, assetID, businessQueryId, type);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public QDataAggregatedQuery getQdataAggregatedQueryById (long aggregateQueryId) throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getQdataAggregatedQueryById(aggregateQueryId);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public QDataBusinessQuery getQdataBusinessQuery (long requestID) throws QueryDataException {

      try {
         return getQDataBusinessQueryDAO().getQdataBusinessQuery(requestID);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public List<Asset> getQdataMatchedAssets (long requestID) throws QueryDataException {
      // TODO Auto-generated method stub
      return null;
   }

   public List<ReportType> getQdataReportTypes (long requestID, long assetID, long businessQueryId)
            throws QueryDataException {
      // TODO Auto-generated method stub
      return null;
   }

   public List<ReportType> getQdataReportTypes (long aggregateQueryId) throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getQdataReportTypes(aggregateQueryId);
      } catch (DataAccessException dae) {
         throw new QueryDataException(dae.getCode(), dae);
      }
   }

   public ReportVO getReport (long requestID, long assetID, long businessQueryId) throws QueryDataException {
      // TODO Auto-generated method stub
      return null;
   }

   public List<ReportVO> getReports (long requestID) throws QueryDataException {
      // TODO Auto-generated method stub
      return null;
   }

   public void storeAggregateQuery (QDataAggregatedQuery qdataAggregatedQuery, long userQueryId)
            throws QueryDataException {
      try {
         QDataUserQuery userQuery = new QDataUserQuery();
         userQuery.setId(userQueryId);
         qdataAggregatedQuery.setUserQuery(userQuery);
         getQDataAggregatedQueryDAO().create(qdataAggregatedQuery);
      } catch (Exception e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void storeBusinessQuery (QDataBusinessQuery qdataBusinessQuery, long userQueryId) throws QueryDataException {
      try {
         QDataUserQuery userQuery = new QDataUserQuery();
         userQuery.setId(userQueryId);
         qdataBusinessQuery.setUserQuery(userQuery);
         getQDataBusinessQueryDAO().create(qdataBusinessQuery);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void storeReportData (QDataReportData qdataReportData, QDataAggregatedQuery qdataAggregatedQuery)
            throws QueryDataException {
      try {
         qdataReportData.setAggregatedQuery(qdataAggregatedQuery);
         getQDataAggregatedQueryDAO().storeReportData(qdataReportData, qdataAggregatedQuery);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void storeReportTypes (QDataAggregatedQuery qdataAggregatedQuery,
            List<QDataAggregatedReportType> dataReportTypes) throws QueryDataException {
      for (QDataAggregatedReportType qDataAggregatedReportType : dataReportTypes) {
         qDataAggregatedReportType.setAggregatedQuery(qdataAggregatedQuery);
      }
      try {
         getQDataAggregatedQueryDAO().storeReportTypes(qdataAggregatedQuery, dataReportTypes);
      } catch (DataAccessException e) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void storeUserQuery (QDataUserQuery dataUserQuery) throws QueryDataException {
      try {
         getQDataUserQueryDAO().create(dataUserQuery);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(QueryDataExceptionCodes.QD_USER_QUERY_EXCEPTION_CODE, dataAccessException
                  .getCause());
      }
   }

   public <DomainObject extends Serializable> DomainObject getById (Long id, Class<DomainObject> clazz)
            throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getById(id, clazz);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<QDataUserQuery> getUserQuerysByName (String userQuery) throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getMatchQDataUserQuery(userQuery);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public QDataDimensionInput getQDataDimensionInput (long assetId) throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getQDataDimensionInput(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<QDataDimensionCombinationInfo> getQDataDimensionCombinationInfo (long assetId) throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getQDataDimensionCombinationInfo(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<QDataDimensionInfo> getQDataDimensionInfo (long assetId) throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getQDataDimensionInfo(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void updateFlagsForAggregateQuery (QDataAggregatedQuery qDataAggregatedQuery) throws QueryDataException {
      try {
         getQDataAggregatedQueryDAO().update(qDataAggregatedQuery);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<QueryCacheResultInfo> getQueryCacheResults (Long userQueryId, Long applicationId)
            throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getQueryCacheResults(userQueryId, applicationId);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void updateQDataUserQuery (QDataUserQuery userQuery) throws QueryDataException {
      try {
         getQDataAggregatedQueryDAO().update(userQuery);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public QDataReducedQuery storeReducedQuery (QDataReducedQuery reducedQuery) throws QueryDataException {
      try {
         return getQDataUserQueryDAO().create(reducedQuery);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(QueryDataExceptionCodes.QD_REDUCED_QUERY_EXCEPTION_CODE, dataAccessException
                  .getCause());
      }
   }

   public QDataReducedQuery getQdataReducedQueryById (Long reducedQueryId) throws QueryDataException {
      try {
         return getQDataReducedQueryDAO().getQdataReducedQueryById(reducedQueryId);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(QueryDataExceptionCodes.QD_REDUCED_QUERY_EXCEPTION_CODE, dataAccessException
                  .getCause());
      }
   }

   public List<QDataUserQuery> getUserQueriesByReducedQueryIds (List<Long> reducedQueryIds) throws QueryDataException {
      try {
         return getQDataUserQueryDAO().getUserQueriesByReducedQueryIds(reducedQueryIds);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(QueryDataExceptionCodes.QD_REDUCED_QUERY_EXCEPTION_CODE, dataAccessException
                  .getCause());
      }
   }

   public void updateReducedQuery (QDataReducedQuery qdataReducedQuery) throws QueryDataException {
      try {
         getQDataReducedQueryDAO().update(qdataReducedQuery);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }

   }

   public List<NewsItem> getNewsItemsByCategory (NewsCategory category, Long size) throws QueryDataException {
      try {
         return getNewsItemDAO().getNewsItemsByCategory(category, size);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<QDataUserQuery> getUserQueriesByIds (List<Long> userQueryIds) throws QueryDataException {
      try {
         return getQDataUserQueryDAO().getUserQueriesByIds(userQueryIds);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(QueryDataExceptionCodes.QD_REDUCED_QUERY_EXCEPTION_CODE, dataAccessException
                  .getCause());
      }
   }

   public List<QDataAggregatedQuery> getAggregatedQueriesByUserQueryId (Long userQueryId) throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getAggregatedQueriesByUserQueryId(userQueryId);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(QueryDataExceptionCodes.QD_AGGREGATION_QUERY_EXCEPTION_CODE, dataAccessException
                  .getCause());
      }
   }

   public QDataAggregatedQueryColumn getMatchedAggregatedQueryColumnByConceptBedId (Long conceptBedId, Long aggQueryId)
            throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getMatchedAggregatedQueryColumnByConceptBedId(conceptBedId, aggQueryId);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(QueryDataExceptionCodes.QD_REDUCED_QUERY_EXCEPTION_CODE, dataAccessException
                  .getCause());
      }
   }

   public long storeCachedReportResults (QDataCachedReportResults cachedReportResults) throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().storeCachedReportResults(cachedReportResults);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(QueryDataExceptionCodes.QD_REDUCED_QUERY_EXCEPTION_CODE, dataAccessException
                  .getCause());
      }
   }

   public QDataCachedReportResults getCachedReportResultsById (Long aggregateQueryId, ReportType reportType)
            throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getCachedReportResultsById(aggregateQueryId, reportType);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(QueryDataExceptionCodes.QD_REDUCED_QUERY_EXCEPTION_CODE, dataAccessException
                  .getCause());
      }
   }

   public QDataCachedReportResults getCachedReportResultsById (Long cachedReportId) throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().getCachedReportResultsById(cachedReportId);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(QueryDataExceptionCodes.ENTITY_RETRIEVAL_FAILED, dataAccessException.getCause());
      }
   }

   @Override
   public Map<String, Double> getPastUsagePatternInfoMap (Long assetId, Date queryExecutionDate)
            throws QueryDataException {
      try {
         return getOptimalDSetSWIDAO().getPastUsagePatternInfoMap(assetId, queryExecutionDate);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(QueryDataExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException.getCause());
      }
   }

   @Override
   public void deleteOptimalDSetSwiInfoByAssetId (Long assetId) throws QueryDataException {
      try {
         getOptimalDSetSWIDAO().deleteOptimalDSetSwiInfoByAssetId(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(QueryDataExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException.getCause());
      }
   }

   @Override
   public void createAllOptimalDSetSwiInfos (List<OptimalDSetSWIInfo> opList) throws QueryDataException {
      try {
         getOptimalDSetSWIDAO().createAll(opList);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(QueryDataExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException.getCause());
      }
   }

   @Override
   public List<QueryHistoryBusinessEntityInfo> getPastUsageProminentBusinessEntities (Long assetId,
            Date queryExecutionDate, ColumnType columnType, Integer limit) throws QueryDataException {
      try {
         return getOptimalDSetSWIDAO().getPastUsageProminentBusinessEntities(assetId, queryExecutionDate, columnType,
                  limit);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(QueryDataExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException.getCause());
      }
   }

   @Override
   public List<UserSearchAuditOutput> populateUserSearchAudit (UserSearchAuditInput input) throws QueryDataException {
      try {
         return getQDataAggregatedQueryDAO().populateUserSearchAudit(input);
      } catch (DataAccessException dataAccessException) {
         throw new QueryDataException(QueryDataExceptionCodes.POPULATION_OF_USER_SEARCH_AUDIT_FAILED,
                  dataAccessException.getCause());
      }
   }
}
