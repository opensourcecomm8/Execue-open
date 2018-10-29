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


package com.execue.driver.qi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.AssetResult;
import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.PossibilityResult;
import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.QueryResult;
import com.execue.core.common.bean.UserInput;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.qdata.QDataUserQuery;
import com.execue.core.common.bean.swi.PossibleAssetInfo;
import com.execue.core.common.bean.swi.UserQueryPossibility;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.CheckType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.driver.BaseDriver;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qi.exception.QIException;
import com.execue.qi.service.IQueryInterfaceService;
import com.execue.querycache.exception.QueryCacheException;

public class QueryInterfaceDriver extends BaseDriver implements IQueryInterfaceDriver {

   private Logger                 logger = Logger.getLogger(QueryInterfaceDriver.class);
   private IQueryInterfaceService queryInterfaceService;

   public void setQueryInterfaceService (IQueryInterfaceService queryInterfaceService) {
      this.queryInterfaceService = queryInterfaceService;
   }

   public QueryResult process (Object userInputObject) throws ExeCueException {
      QueryResult queryResult = initializeResult();

      if (logger.isDebugEnabled()) {
         logger.debug("processing request");
      }
      try {
         /*
          * TODO: -KA- Check if this still needed List<QuerySuggestion> querySuggestions =
          * queryInterfaceService.validateQueryForm(requestForm); // After validation, found multiple possibilities if
          * (querySuggestions != null) { queryResult = new QueryResult();
          * queryResult.setQuerySuggestions(querySuggestions); return queryResult; }
          */
         UserInput userInput = (UserInput) userInputObject;
         QueryForm requestForm = userInput.getQueryForm();
         // generate user query statement
         String userQueryString = pseudoLanguageService.getPseudoLanguageStatement(pseudoLanguageService
                  .getPseudoQuery(requestForm));
         if (logger.isDebugEnabled()) {
            logger.debug("USER QUERY : " + userQueryString);
         }
         // call BusinessQuery Generation
         BusinessQuery businessQuery = queryInterfaceService.generateBusinessQuery(requestForm);
         businessQuery.setScoped(true);
         businessQuery.setAppSourceType(AppSourceType.STRUCTURED);
         businessQuery.setStandarizedApplicationWeight(100d);
         businessQuery.setStandarizedPossiblityWeight(100d);

         // String normalizedQuery = pseudoLanguageService.getPseudoLanguageStatement(pseudoLanguageService
         // .getPseudoQuery(businessQuery));
         QDataUserQuery qDataUserQuery = null;
         List<UserQueryPossibility> userQueryPossibilities = new ArrayList<UserQueryPossibility>(1);
         if (queryCacheService != null
                  && (qDataUserQuery = queryCacheService.getMatchQueryIdOnUserQuery(userQueryString, requestForm)) != null) {
            // get match id
            if (logger.isInfoEnabled()) {
               logger.info("Match From QueryCache ## " + qDataUserQuery.getId());
            }
            queryResult = getQueryDataResults(qDataUserQuery);
         } else {
            CheckType anonymous = CheckType.YES;
            Long userId = -999L;
            if (userInput.getUserId() != null && userInput.getUserId() != -1) {
               userId = userInput.getUserId();
               anonymous = CheckType.NO;
            }
            qDataUserQuery = getDriverHelper().getQueryDataService().storeUserQuery(queryResult.getId(), requestForm,
                     userQueryString, userId, anonymous);

            // get the set of business entity terms
            Set<BusinessEntityTerm> businessEntityTerms = governorService.populateBusinessEntityTerms(businessQuery);
            Set<BusinessEntityTerm> userRequestedBusinessEntityTerms = getUserRequestedBusinessEntityTerms(businessEntityTerms);
            Date userQueryExecutionDate = qDataUserQuery.getExecutionDate();
            userQueryPossibilities.addAll(getUserQueryPossibilties(userRequestedBusinessEntityTerms, businessQuery,
                     queryResult.getId(), -1L, userQueryExecutionDate));

            Long startST = System.currentTimeMillis();
            // Now populate the User_Query_Possibiliy table
            getUserQueryPossibilityService().createUserQueryPossibilities(userQueryPossibilities);
            Long endST = System.currentTimeMillis();
            if (logger.isInfoEnabled()) {
               logger.info("Time taken to create UQ Possibilities " + (endST - startST) / 1000.0 + " seconds");
            }

            // Get the possible asset info
            List<PossibleAssetInfo> possibleAssetsInfo = getUserQueryPossibilityService()
                     .getPossibleAssetsForUserRequest(queryResult.getId());

            // Return if no match found for possible asset info's
            if (ExecueCoreUtil.isCollectionEmpty(possibleAssetsInfo)) {
               return queryResult;
            }

            Map<Long, BusinessQuery> businessQueryMap = new HashMap<Long, BusinessQuery>(1);
            businessQueryMap.put(-1L, businessQuery);

            // Apply the asset level filer logic
            Map<Long, List<PossibleAssetInfo>> possibleAssetInfoMap = applyAssetLevelFilters(businessQueryMap,
                     possibleAssetsInfo);

            possibleAssetsInfo = possibleAssetInfoMap.get(-1L);

            setStandardizedTotalTypeBasedWeight(possibleAssetsInfo, 100d, businessQuery.getMaxPossibleBQWeight());

            setAssetRelevance(possibleAssetsInfo, 100d);

            queryResult = processBusinessQuery2(businessQuery, queryResult, possibleAssetsInfo, businessEntityTerms);
            // above call will give dummy asset result objects for a possibility due to pagination changes
            // set pagination info
            int pageSize;
            if (userInput.getPageSize() != null) {
               pageSize = Integer.parseInt(userInput.getPageSize());
               logger.info("page size from Drop Down QI " + pageSize);
            } else {
               pageSize = getCoreConfigurationService().getResultsPageSize();
               logger.info("page size from ConfigFile QI " + pageSize);
            }

            int pageNum = 1;
            if (userInput.getRequestedPage() != null) {
               pageNum = Integer.valueOf(userInput.getRequestedPage());
            }
            queryResult.setRequestedPage(pageNum);
            queryResult.setPageSize(pageSize);
            // get the total number of results for the current request
            int totalAssetResultsCount = 0;
            for (PossibilityResult possibility : queryResult.getPossibilites()) {
               totalAssetResultsCount += possibility.getAssets().size();
            }

            // calculate the page count
            int totalPages = totalAssetResultsCount / pageSize;
            int remainder = totalAssetResultsCount % pageSize;
            if (remainder > 0) {
               totalPages++;
            }

            // set the page count
            queryResult.setPageCount(totalAssetResultsCount); // setting total results count

            // TODO: NK: Should later use the same call for semantic and QI driver to get the results by page
            // Get the relevant page results
            List<PossibilityResult> pageResults = getResultsByPage(pageNum, pageSize, queryResult);
            queryResult.setPossibilites(pageResults);

            performAggregation(pageResults, queryResult.getId());
         }
         if (logger.isDebugEnabled()) {
            logger.debug("returning response " + queryResult.getQueryName());
         }
      } catch (QueryCacheException queryCacheException) {
         logger.error("QueryCacheException in QIDriver", queryCacheException);
         logger.error("Actual Error : [" + queryCacheException.getCode() + "] " + queryCacheException.getMessage());
         logger.error("Cause : " + queryCacheException.getCause());
         queryResult.setError(Integer.toString(queryCacheException.getCode()));
      } catch (QIException qiException) {
         logger.error("QIException in QIDriver", qiException);
         logger.error("Actual Error : [" + qiException.getCode() + "] " + qiException.getMessage());
         logger.error("Cause : " + qiException.getCause());
         queryResult.setError(Integer.toString(qiException.getCode()));
      } catch (QueryDataException queryDataException) {
         logger.error("QueryDataException in QIDriver", queryDataException);
         logger.error("Actual Error : [" + queryDataException.getCode() + "] " + queryDataException.getMessage());
         logger.error("Cause : " + queryDataException.getCause());
         queryResult.setError(Integer.toString(queryDataException.getCode()));
      } catch (ExeCueException exeCueException) {
         logger.error("ExeCueException in QIDriver", exeCueException);
         logger.error("Actual Error : " + exeCueException.getMessage());
         logger.error("Cause : " + exeCueException.getCause());
         queryResult.setError(Integer.toString(exeCueException.getCode()));
      } catch (Exception exception) {
         logger.error("Exception in QIDriver", exception);
         logger.error("Actual Error : " + exception.getMessage());
         logger.error("Cause : " + exception.getCause());
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, "Error in QueryInterfaceDriver",
                  exception.getCause());
      }
      return queryResult;
   }

   @SuppressWarnings ("unchecked")
   public List<PossibilityResult> getResultsByPage (int pageNum, int pageSize, QueryResult queryResult) {
      int toBeFilled = pageSize;
      List<PossibilityResult> source = queryResult.getPossibilites();
      List<PossibilityResult> results = new ArrayList<PossibilityResult>();

      int startIndex = (pageNum - 1) * pageSize + 1;
      if (logger.isDebugEnabled()) {
         logger.debug("Start Index : " + startIndex);
      }
      int endIndex = pageNum * pageSize;
      if (logger.isDebugEnabled()) {
         logger.debug("End Index : " + endIndex);
      }
      for (PossibilityResult result : source) {
         int checkIndex = checkPossibilityForPageResults(startIndex, queryResult, result);
         if (logger.isDebugEnabled()) {
            logger.debug("Check Index : " + checkIndex);
            logger.debug("queryResult.getCounter() " + queryResult.getCounter());
         }
         if (checkIndex > -1) {
            if (queryResult.getCounter() < endIndex) {
               if (checkIndex <= result.getAssets().size()) {
                  // List<AssetResult> finalAssets = result.getAssets().subList((result.getAssets().size() -
                  // checkIndex), result.getAssets().size());
                  List<AssetResult> finalAssets = getSubList((result.getAssets().size() - checkIndex), result
                           .getAssets().size(), result.getAssets());
                  result.setAssets(finalAssets);
                  results.add(result);
                  toBeFilled -= finalAssets.size();
               } else {
                  if (toBeFilled >= result.getAssets().size()) {
                     results.add(result);
                     toBeFilled -= result.getAssets().size();
                  } else {
                     int diff = checkIndex - result.getAssets().size();
                     // List<AssetResult> finalAssets = result.getAssets().subList(0, diff);
                     List<AssetResult> finalAssets = getSubList(0, diff, result.getAssets());
                     result.setAssets(finalAssets);
                     results.add(result);
                     toBeFilled -= finalAssets.size();
                  }
               }
            } else {
               // break condition
               int diff = queryResult.getCounter() - endIndex;
               int toIdx = result.getAssets().size() - diff;
               List<AssetResult> finalAssets = result.getAssets().subList(0, toIdx);
               if (finalAssets.size() > pageSize) {
                  // finalAssets = finalAssets.subList((finalAssets.size() - pageSize), finalAssets.size());
                  finalAssets = getSubList((finalAssets.size() - pageSize), finalAssets.size(), finalAssets);
               }
               result.setAssets(finalAssets);
               results.add(result);
               toBeFilled -= finalAssets.size();
               break;
            }
         }
      }
      return results;
   }

   private int checkPossibilityForPageResults (int startIndex, QueryResult queryResult, PossibilityResult result) {
      int count = queryResult.getCounter();
      count += result.getAssets().size();
      queryResult.setCounter(count);
      if (count < startIndex) {
         return -1;
      }
      return count - startIndex + 1;
   }

   @SuppressWarnings ("unchecked")
   public static List getSubList (int startIndex, int endIndex, List list) {
      List subList = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(list)) {
         if (startIndex < 0) {
            return null;
         }
         if (endIndex > list.size()) {
            endIndex = list.size();
         }
         subList = new ArrayList();
         for (int i = startIndex; i < endIndex; i++) {
            subList.add(list.get(i));
         }
      }
      return subList;
   }

   public AssetResult getPopulatedAssetResult (AssetResult assetResult, Long queryId, boolean isRelevancePopulated)
            throws ExeCueException {
      return driverHelper.populateAssetResult(assetResult, queryId, isRelevancePopulated);
   }

   public QueryResult getCachedQueryDataResult (Long userQueryId, Long businessQueryId, Long assetId)
            throws ExeCueException {
      return getQDataCachedQueryDataResult(userQueryId, businessQueryId, assetId);
   }

   public QueryResult getCachedQueryDataResult (Long aggregateQueryId) throws ExeCueException {
      return getQDataCachedQueryDataResult(aggregateQueryId);
   }

   public IQueryInterfaceService getQueryInterfaceService () {
      return queryInterfaceService;
   }
}
