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


package com.execue.querycache.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.RelatedUserQuery;
import com.execue.core.common.bean.qdata.QDataReducedQuery;
import com.execue.core.common.bean.qdata.QDataUserQuery;
import com.execue.core.common.bean.qdata.QueryCacheResultInfo;
import com.execue.core.common.bean.qdata.UniversalSearchResult;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.querydata.IQueryDataPlatformRetrievalService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.exception.UDXException;
import com.execue.qdata.service.IQueryDataService;
import com.execue.qdata.service.IRFXService;
import com.execue.qdata.service.IUDXService;
import com.execue.querycache.configuration.IQueryCacheConfiguration;
import com.execue.querycache.configuration.IQueryCacheService;
import com.execue.querycache.configuration.QueryCacheConstants;
import com.execue.querycache.exception.QueryCacheException;
import com.execue.querycache.exception.QueryCacheExceptionCodes;

public class QueryCacheServiceImpl implements IQueryCacheService {

   private static Logger                      logger = Logger.getLogger(QueryCacheServiceImpl.class);
   private IQueryDataService                  queryDataService;
   private IQueryCacheConfiguration           queryCacheConfiguration;
   private IQueryDataPlatformRetrievalService queryDataPlatformRetrievalService;
   private IRFXService                        rfxService;
   private IUDXService                        udxService;

   public List<RelatedUserQuery> performUniversalSearchForRelatedQueries (long userQueryId,
            Map<Long, QDataReducedQuery> reducedQueryByAppId, String currentUserQuery) throws QueryCacheException {
      try {
         long startTime = System.currentTimeMillis();
         // Match the related user queries
         List<UniversalSearchResult> queryCacheMatchedResults = getUdxService().getUniversalSearchMatchForRelatedQuery(
                  userQueryId);
         // Process the related queries for the matched related query results
         List<RelatedUserQuery> relatedQueries = processRelatedQueries(queryCacheMatchedResults, reducedQueryByAppId,
                  currentUserQuery);
         long endTime = System.currentTimeMillis();
         if (logger.isInfoEnabled()) {
            logger.info("Time taken to match and process the Related Queries from Query Cache " + (endTime - startTime)
                     / 1000.0 + " seconds");
         }
         List<RelatedUserQuery> sortedRelatedQueries = sortAndFilterByTopRank(relatedQueries);
         return sortedRelatedQueries;
      } catch (UDXException e) {
         throw new QueryCacheException(e.getCode(), e);
      } catch (QueryDataException e) {
         throw new QueryCacheException(e.getCode(), e);
      }
   }

   private List<RelatedUserQuery> processRelatedQueries (List<UniversalSearchResult> queryCacheMatchedResults,
            Map<Long, QDataReducedQuery> reducedQueryByAppId, String currentUserQuery) throws QueryDataException {

      List<RelatedUserQuery> relatedQueries = new ArrayList<RelatedUserQuery>(1);
      if (CollectionUtils.isEmpty(queryCacheMatchedResults)) {
         return relatedQueries;
      }

      int queryCacheRelatedQueryWeightThreshold = getQueryCacheConfiguration().getConfiguration().getInt(
               QueryCacheConstants.QUERY_CACHE_RELATED_QUERY_WEIGHT_THRESHOLD);
      int queryCacheRelatedQueryLimit = getQueryCacheConfiguration().getConfiguration().getInt(
               QueryCacheConstants.QUERY_CACHE_RELATED_QUERY_MAX_LIMIT);
      int count = 0;
      Map<Long, Double> userQueryIdByRankMap = new TreeMap<Long, Double>();
      Map<Long, Long> userQueryIdByApplicationMap = new HashMap<Long, Long>(1);
      for (UniversalSearchResult universalSearchResult : queryCacheMatchedResults) {
         Long userQueryId = universalSearchResult.getUserQueryId(); // Get the user query id
         Long applicationId = universalSearchResult.getApplicationId(); // Get the application id
         QDataReducedQuery dataReducedQuery = reducedQueryByAppId.get(applicationId);
         if (dataReducedQuery == null) {
            continue;
         }
         Double userQueryMatchedQCWeight = (Double) universalSearchResult.getMatchWeight()
                  / dataReducedQuery.getMaxMatchWeight() * 100;

         // NK: Assumption here is matchedResults is already ordered by weight, so break at first weight
         // which is coming as lower than the threshold weight for the related query OR related query count
         // is equal to max allowed limit
         // No threshold on match weight for now
         if (/* userQueryMatchedQCWeight < queryCacheRelatedQueryWeightThreshold || */count >= queryCacheRelatedQueryLimit) {
            break;
         }
         userQueryIdByRankMap.put(userQueryId, userQueryMatchedQCWeight);
         userQueryIdByApplicationMap.put(userQueryId, applicationId);
         count++;
      }
      if (userQueryIdByRankMap.isEmpty()) {
         return relatedQueries;
      }

      List<QDataUserQuery> qdataUserQueries = getQueryDataService().getUserQueriesByIds(
               new ArrayList<Long>(userQueryIdByRankMap.keySet()));
      Set<String> uniqueRelatedQueries = new HashSet<String>(1);
      uniqueRelatedQueries.add(currentUserQuery.toLowerCase());
      for (QDataUserQuery dataUserQuery : qdataUserQueries) {
         String userQuery = dataUserQuery.getNormalizedQueryString();
         if (!uniqueRelatedQueries.contains(userQuery.toLowerCase())) {
            Long userQueryId = dataUserQuery.getId();
            RelatedUserQuery rq = new RelatedUserQuery();
            rq.setUserQueryId(userQueryId);
            rq.setUserQuery(userQuery);
            rq.setRank(userQueryIdByRankMap.get(userQueryId));
            rq.setApplicationId(userQueryIdByApplicationMap.get(userQueryId));
            relatedQueries.add(rq);
            uniqueRelatedQueries.add(userQuery.toLowerCase());
         }
      }
      return relatedQueries;
   }

   private List<RelatedUserQuery> sortAndFilterByTopRank (List<RelatedUserQuery> allRelatedUserQueries) {

      if (CollectionUtils.isEmpty(allRelatedUserQueries)) {
         return allRelatedUserQueries;
      }
      int queryCacheRelatedQueryLimit = getQueryCacheConfiguration().getConfiguration().getInt(
               QueryCacheConstants.QUERY_CACHE_RELATED_QUERY_MAX_LIMIT);

      Set<RelatedUserQuery> uniqueRelatedUserQueries = new HashSet<RelatedUserQuery>(allRelatedUserQueries);
      List<RelatedUserQuery> relatedUserQueries = new ArrayList<RelatedUserQuery>(uniqueRelatedUserQueries);

      // Sort the RelatedUserQuery across apps by best rank at top
      Collections.sort(relatedUserQueries, new Comparator<RelatedUserQuery>() {

         public int compare (RelatedUserQuery o1, RelatedUserQuery o2) {
            return (int) (o2.getRank() - o1.getRank());
         }
      });

      // If more than the query cache limit, then return only top queries within limit
      if (relatedUserQueries.size() > queryCacheRelatedQueryLimit) {
         return relatedUserQueries.subList(0, queryCacheRelatedQueryLimit);
      }
      return relatedUserQueries;
   }

   public QDataUserQuery getMatchQueryIdOnUserQuery (String userQuery, QueryForm form) throws QueryCacheException {
      if (logger.isDebugEnabled()) {
         logger.debug("In getMatchQueryIdOnUserQuery");
      }
      if (!queryCacheConfiguration.getConfiguration().getBoolean(QueryCacheConstants.CHECK_MATCH_FLAG)) {
         if (logger.isInfoEnabled()) {
            logger.info("Query Cache CheckMatch is disabled");
         }
         return null;
      }
      if (logger.isInfoEnabled()) {
         logger.info("Query Cache CheckMatch is enabled");
      }
      try {
         List<QDataUserQuery> userQueryList = queryDataPlatformRetrievalService.getMatchUseryQueries(userQuery);
         // TODO: -JVK- add logic to perform filtering and pick the most suitable QDataUserQuery
         if (ExecueCoreUtil.isCollectionNotEmpty(userQueryList)) {
            if (userQueryList.size() == 1) {
               return userQueryList.get(0);
            }
            return userQueryList.get(1);
         }
      } catch (QueryDataException queryDataException) {
         logger.error("Exception from QueryCache Service ", queryDataException);
         throw new QueryCacheException(QueryCacheExceptionCodes.QC_GET_USER_QUERY_MATCH_EXCEPTION_CODE,
                  queryDataException.getMessage(), queryDataException.getCause());
      }
      return null;
   }

   public List<QueryCacheResultInfo> getQueryCacheResults (Long userQueryId, Long applicationId)
            throws QueryCacheException {
      try {
         return queryDataService.getQueryCacheResults(userQueryId, applicationId);
      } catch (QueryDataException queryDataException) {
         throw new QueryCacheException(QueryCacheExceptionCodes.QC_GET_USER_QUERY_MATCH_EXCEPTION_CODE,
                  queryDataException.getMessage(), queryDataException.getCause());
      }
   }

   /**
    * @return the rfxService
    */
   public IRFXService getRfxService () {
      return rfxService;
   }

   /**
    * @param rfxService
    *           the rfxService to set
    */
   public void setRfxService (IRFXService rfxService) {
      this.rfxService = rfxService;
   }

   /**
    * @return the udxService
    */
   public IUDXService getUdxService () {
      return udxService;
   }

   /**
    * @param udxService
    *           the udxService to set
    */
   public void setUdxService (IUDXService udxService) {
      this.udxService = udxService;
   }

   /**
    * @return the queryDataService
    */
   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   /**
    * @param queryDataService
    *           the queryDataService to set
    */
   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
   }

   /**
    * @return the queryCacheConfiguration
    */
   public IQueryCacheConfiguration getQueryCacheConfiguration () {
      return queryCacheConfiguration;
   }

   /**
    * @param queryCacheConfiguration
    *           the queryCacheConfiguration to set
    */
   public void setQueryCacheConfiguration (IQueryCacheConfiguration queryCacheConfiguration) {
      this.queryCacheConfiguration = queryCacheConfiguration;
   }

   /**
    * @return the queryDataPlatformRetrievalService
    */
   public IQueryDataPlatformRetrievalService getQueryDataPlatformRetrievalService () {
      return queryDataPlatformRetrievalService;
   }

   /**
    * @param queryDataPlatformRetrievalService
    *           the queryDataPlatformRetrievalService to set
    */
   public void setQueryDataPlatformRetrievalService (
            IQueryDataPlatformRetrievalService queryDataPlatformRetrievalService) {
      this.queryDataPlatformRetrievalService = queryDataPlatformRetrievalService;
   }

}
