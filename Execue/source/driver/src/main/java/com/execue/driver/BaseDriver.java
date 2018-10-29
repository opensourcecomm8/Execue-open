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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.AggregationMessage;
import com.execue.core.common.bean.AssetResult;
import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.PossibilityResult;
import com.execue.core.common.bean.QueryResult;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetDetail;
import com.execue.core.common.bean.governor.AssetQuery;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.pseudolang.NormalizedPseudoQuery;
import com.execue.core.common.bean.qdata.QDataAggregatedQuery;
import com.execue.core.common.bean.qdata.QDataBusinessQuery;
import com.execue.core.common.bean.qdata.QDataUserQuery;
import com.execue.core.common.bean.swi.PossibleAssetInfo;
import com.execue.core.common.bean.swi.UserQueryPossibility;
import com.execue.core.common.bean.swi.comparator.PossibleAssetInfoComparatorByPosId;
import com.execue.core.common.messages.ExeCueMessages;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.MessageStatusType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.driver.configuration.IDriverConfigurationService;
import com.execue.driver.helper.DriverHelper;
import com.execue.driver.qi.QueryInterfaceDriver;
import com.execue.governor.exception.GovernorException;
import com.execue.governor.exception.GovernorExceptionCodes;
import com.execue.governor.service.IGovernorService;
import com.execue.governor.service.IQueryFederationService;
import com.execue.message.IMessage;
import com.execue.message.bean.MessageInfo;
import com.execue.message.bean.MessageInput;
import com.execue.message.exception.MessageException;
import com.execue.platform.IUidService;
import com.execue.pseudolang.service.IPseudoLanguageService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.exception.QueryDataExceptionCodes;
import com.execue.qdata.service.IMessageService;
import com.execue.querycache.configuration.IQueryCacheConfiguration;
import com.execue.querycache.configuration.IQueryCacheService;
import com.execue.security.IUserContext;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IAssetDetailService;
import com.execue.swi.service.IUserQueryPossibilityService;
import com.execue.util.print.SemanticDriverPrintHelper;

public class BaseDriver implements IDriver {

   private static Logger                  logger = Logger.getLogger(QueryInterfaceDriver.class);

   protected IGovernorService             governorService;

   protected IMessage                     reportMessage;
   protected IMessageService              messageService;
   protected IUidService                  transactionIdGenerationService;
   protected IQueryCacheService           queryCacheService;
   protected IUserContext                 userContext;
   protected IPseudoLanguageService       pseudoLanguageService;
   protected DriverHelper                 driverHelper;
   protected IUserQueryPossibilityService userQueryPossibilityService;
   private IAssetDetailService            assetDetailService;
   private IQueryFederationService        queryFederationService;
   private ICoreConfigurationService      coreConfigurationService;
   private IQueryCacheConfiguration       queryCacheConfiguration;

   protected long getQueryId () {
      try {
         return getTransactionIdGenerationService().getNextId();
      } catch (Exception e) {
         return -1;
      }
   }

   protected QueryResult initializeResult () {
      QueryResult queryResult = new QueryResult();
      ArrayList<PossibilityResult> possibilites = new ArrayList<PossibilityResult>();
      long queryId = getQueryId();
      queryResult.setId(queryId);
      // set transaction id in log context
      // String tidValue = "txnId-" + queryResult.getId();
      // MDC.put("txnId", tidValue);
      queryResult.setPossibilites(possibilites);
      queryResult.setDisplaySqlQuery(getDriverConfigurationService().isDisplayQueryString());
      return queryResult;
   }

   protected QueryResult getQDataCachedQueryDataResult (Long aggregateQueryId) throws ExeCueException {
      QDataAggregatedQuery qDataAggregatedQuery = getDriverHelper().getQueryDataService().getAggregatedQueryById(
               aggregateQueryId);
      QDataUserQuery qDataUserQuery = qDataAggregatedQuery.getUserQuery();
      QueryResult queryResult = new QueryResult();
      queryResult.setId(qDataUserQuery.getId());
      List<PossibilityResult> posList = new ArrayList<PossibilityResult>();
      queryResult.setPossibilites(posList);
      PossibilityResult pResult = new PossibilityResult();
      List<AssetResult> assetList = new ArrayList<AssetResult>();
      pResult.setAssets(assetList);
      pResult.setId(1);
      posList.add(pResult);
      queryResult.setQueryName(qDataUserQuery.getNormalizedQueryString());
      AssetResult assetResult = new AssetResult();
      assetResult.setAssetId(qDataAggregatedQuery.getAssetId());
      assetResult.setBusinessQueryId(qDataAggregatedQuery.getBusinessQueryId());
      assetResult.setReportStatus(MessageStatusType.COMPLETED.getValue());
      driverHelper.populateAssetResult(assetResult, qDataUserQuery.getId(), false);
      assetList.add(assetResult);
      return queryResult;
   }

   protected QueryResult getQDataCachedQueryDataResult (Long userQueryId, Long businessQueryId, Long assetId)
            throws ExeCueException {
      QDataUserQuery qDataUserQuery = getDriverHelper().getQueryDataService().getUserQuery(userQueryId);
      QueryResult queryResult = new QueryResult();
      queryResult.setId(qDataUserQuery.getId());
      List<PossibilityResult> posList = new ArrayList<PossibilityResult>();
      queryResult.setPossibilites(posList);
      PossibilityResult pResult = new PossibilityResult();
      List<AssetResult> assetList = new ArrayList<AssetResult>();
      pResult.setAssets(assetList);
      pResult.setId(1);
      posList.add(pResult);
      queryResult.setQueryName(qDataUserQuery.getNormalizedQueryString());
      AssetResult assetResult = new AssetResult();
      assetResult.setAssetId(assetId);
      assetResult.setBusinessQueryId(businessQueryId);
      assetResult.setReportStatus(MessageStatusType.COMPLETED.getValue());
      driverHelper.populateAssetResult(assetResult, userQueryId, false);
      assetList.add(assetResult);
      return queryResult;
   }

   // protected QueryResult getQueryDataResults (QDataUserQuery qDataUserQuery) throws ExeCueException {
   protected QueryResult getQueryDataResults (QDataUserQuery qDataUserQuery) {
      QueryResult queryResult = new QueryResult();
      queryResult.setId(qDataUserQuery.getId());
      List<PossibilityResult> posList = new ArrayList<PossibilityResult>();
      queryResult.setPossibilites(posList);
      PossibilityResult pResult = new PossibilityResult();
      List<AssetResult> assetList = new ArrayList<AssetResult>();
      pResult.setAssets(assetList);
      pResult.setId(1);
      posList.add(pResult);

      queryResult.setQueryName(qDataUserQuery.getNormalizedQueryString());
      Set<QDataAggregatedQuery> aggQDList = qDataUserQuery.getAggregatedQueries();
      boolean isAppNameSet = false;
      for (QDataAggregatedQuery dataAggregatedQuery : aggQDList) {
         if (!isAppNameSet) {
            isAppNameSet = true;
            pResult.setAppId(dataAggregatedQuery.getAsset().getApplication().getId());
            pResult.setAppName(dataAggregatedQuery.getAsset().getApplication().getName());
            pResult.setAppUrl(dataAggregatedQuery.getAsset().getApplication().getApplicationURL());
            pResult.setAppImageId(dataAggregatedQuery.getAsset().getApplication().getImageId());
         }
         AssetResult assetResult = new AssetResult();
         assetResult.setQuery(dataAggregatedQuery.getAggregatedQueryString());
         driverHelper.populateAssetInfo(assetResult, dataAggregatedQuery.getAsset());
         assetResult.setRelevance(dataAggregatedQuery.getRelevance());
         assetResult.setPseudoStatement(dataAggregatedQuery.getEnglishQueryString());
         boolean isDataPresent = false;
         if (CheckType.YES.equals(dataAggregatedQuery.isDataPresent())) {
            isDataPresent = true;
         }
         assetResult.setDataPresent(isDataPresent);
         // report related.
         driverHelper.populateReportType(dataAggregatedQuery, assetResult);
         assetList.add(assetResult);
      }

      // sort asset results based on data present or not.
      List<AssetResult> resultsDataPresent = new ArrayList<AssetResult>();
      for (AssetResult assetResult : assetList) {
         if (assetResult.isDataPresent()) {
            resultsDataPresent.add(assetResult);
         }
      }
      assetList.removeAll(resultsDataPresent);
      assetList.addAll(0, resultsDataPresent);
      // set the messages
      ArrayList<String> messages = new ArrayList<String>();
      if (aggQDList.size() > 1) {
         messages.add(ExeCueMessages.RESULTS_FROM_MULTIPLE_DATA_SOURCES_KEY);
      } else if (aggQDList.size() == 0) {
         messages.add(ExeCueMessages.RESULTS_FROM_NO_DATA_SOURCES_KEY);
      }
      queryResult.setMessages(messages);
      return queryResult;
   }

   /**
    * @param businessQuery
    * @param queryResult
    * @return the QueryResult
    * @throws ExeCueException
    */
   // TODO: NK: No more being used, should remove it later
   protected QueryResult processBusinessQuery (BusinessQuery businessQuery, QueryResult queryResult)
            throws ExeCueException {
      try {
         // String tidValue = "txnId-" + queryResult.getId();
         // MDC.put("txnId", tidValue);
         long startTime = System.currentTimeMillis();
         List<PossibilityResult> possibilites = queryResult.getPossibilites();
         PossibilityResult possibilityResult = new PossibilityResult();
         List<AssetResult> assets = new ArrayList<AssetResult>();

         NormalizedPseudoQuery pseudoQuery = getPseudoLanguageService().getPseudoQuery(businessQuery);
         String businessStatement = getPseudoLanguageService().getPseudoLanguageStatement(pseudoQuery);
         long endTime = System.currentTimeMillis();
         logger.info("time taken to generate pseudo statements " + (endTime - startTime) / 1000.0 + " seconds");
         // TODO :-VG- do we need to store business query
         startTime = System.currentTimeMillis();
         QDataBusinessQuery qDataBusinessQuery = getDriverHelper().getQueryDataService().storeBusinessQuery(
                  queryResult.getId(), businessQuery, businessStatement);
         endTime = System.currentTimeMillis();
         logger.info("time taken to store Business Query " + (endTime - startTime) / 1000.0 + " seconds");
         possibilityResult.setAssets(assets);
         possibilites.add(possibilityResult);
         queryResult.setPossibilites(possibilites);

         possibilityResult.setId(1); // default ID
         queryResult.setQueryName(businessStatement);

         // call Governor
         startTime = System.currentTimeMillis();
         List<AssetQuery> assetQueryList = governorService.extractDataAssetQueries(businessQuery);
         endTime = System.currentTimeMillis();
         logger.info("time taken By Governor to process business Query " + (endTime - startTime) / 1000.0 + " seconds");
         logger.info("Asset query list size : " + assetQueryList.size());
         // process Aggregation to prepare the asset result
         startTime = System.currentTimeMillis();
         for (AssetQuery assetQuery : assetQueryList) {
            // AssetResult assetResult = processAggregation(assetQuery, queryResult.getId(),
            // qDataBusinessQuery.getId());
            AssetResult assetResult = new AssetResult();
            assetResult.setBusinessQueryId(qDataBusinessQuery.getId());
            assetResult.setAssetQuery(assetQuery);
            assetResult.setAssetId(assetQuery.getLogicalQuery().getAsset().getId());
            driverHelper.populateAssetInfo(assetResult, assetQuery.getLogicalQuery().getAsset());
            assetResult.setRelevance(assetQuery.getLogicalQuery().getStructuredQueryWeight());
            assets.add(assetResult);
         }
         endTime = System.currentTimeMillis();
         logger.info("time taken by aggregation " + (endTime - startTime) / 1000.0 + " seconds");

         // set the application name and url into the Possibility Result object
         Asset tempAsset = possibilityResult.getAssets().get(0).getAssetQuery().getLogicalQuery().getAsset();
         possibilityResult.setAppId(tempAsset.getApplication().getId());
         possibilityResult.setAppName(tempAsset.getApplication().getName());
         possibilityResult.setAppUrl(tempAsset.getApplication().getApplicationURL());
         possibilityResult.setAppImageId(tempAsset.getApplication().getImageId());

         // sort asset results based on data present or not.
         List<AssetResult> resultsDataPresent = new ArrayList<AssetResult>();
         for (AssetResult assetResult : assets) {
            if (assetResult.isDataPresent()) {
               resultsDataPresent.add(assetResult);
            }
         }
         assets.removeAll(resultsDataPresent);
         assets.addAll(0, resultsDataPresent);
         ArrayList<String> messages = new ArrayList<String>();
         if (assetQueryList.size() > 1) {
            messages.add(ExeCueMessages.RESULTS_FROM_MULTIPLE_DATA_SOURCES_KEY);
         }
         queryResult.setMessages(messages);
         // TODO: generate pseudo language statement
      } catch (QueryDataException queryDataException) {
         logger.error("QueryDataException in BaseDriver", queryDataException);
         logger.error("Actual Error : [" + queryDataException.getCode() + "] " + queryDataException.getMessage());
         logger.error("Cause : " + queryDataException.getCause());
         // throw new ExeCueException(QueryDataExceptionCodes.QD_DEFAULT_EXCEPTION_CODE, queryDataException.getCause());
         queryResult.setError(Integer.toString(QueryDataExceptionCodes.QD_DEFAULT_EXCEPTION_CODE));
      } catch (GovernorException governorException) {
         logger.error("GovernorException in BaseDriver", governorException);
         logger.error("Actual Error : [" + governorException.getCode() + "] " + governorException.getMessage());
         logger.error("Cause : " + governorException.getCause());
         // throw new ExeCueException(GovernorExceptionCodes.GOV_DEFAULT_EXCEPTION_CODE, governorException.getCause());
         queryResult.setError(Integer.toString(GovernorExceptionCodes.GOV_DEFAULT_EXCEPTION_CODE));
      } catch (Exception exception) {
         logger.error("Exception in BaseDriver", exception);
         logger.error("Actual Error : " + exception.getMessage());
         logger.error("Cause : " + exception.getCause());
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, "Error in BaseDriver", exception
                  .getCause());
      } finally {
         // MDC.remove("txnId");
      }
      return queryResult;
   }

   protected Map<Long, List<PossibleAssetInfo>> applyAssetLevelFilters (Map<Long, BusinessQuery> businessQueryMap,
            List<PossibleAssetInfo> possibleAssetInfoList) throws SWIException {
      Collections.sort(possibleAssetInfoList, new PossibleAssetInfoComparatorByPosId());

      // Filtering asset by security settings
      Map<Long, Asset> filteredAssetsMap = filterAssetsBySecurity(possibleAssetInfoList);

      // TODO: -RG- Remove later - Added for testing
      if (logger.isInfoEnabled()) {
         logger.info("Possible Assets by Possibility after Security filter applied, ");
         SemanticDriverPrintHelper.printPossibleAssetInfo(possibleAssetInfoList);
      }

      // Get the map of possibility_id and list of PossibleAssetInfo as key value pair sorted by possibility id
      Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap = getPossibleAssetInfoMap(possibleAssetInfoList);

      // federate the possible asset info in the map
      getQueryFederationService().federatePossibleAssetInfo(possibleAssetForPossibilityMap, businessQueryMap,
               filteredAssetsMap);

      // TODO: -RG- Remove later - Added for testing
      if (logger.isInfoEnabled()) {
         logger.info("Possible Assets by Possibility after federation by Asset Info, ");
         SemanticDriverPrintHelper.printPossibleAssetInfo(possibleAssetForPossibilityMap);
      }

      // List<Double> assetWeights = new ArrayList<Double>();
      // for (Long possibilityId : possibleAssetForPossibilityMap.keySet()) {
      // List<PossibleAssetInfo> possibleAssetsInfo = possibleAssetForPossibilityMap.get(possibilityId);
      // for (PossibleAssetInfo possibleAssetInfo : possibleAssetsInfo) {
      // assetWeights.add(possibleAssetInfo.getTotalTypeBasedWeight());
      // }
      // }
      // List<Double> standardizedValues = MathUtil.getStandardizedValues(assetWeights);
      // int index = 0;
      // for (Long possibilityId : possibleAssetForPossibilityMap.keySet()) {
      // List<PossibleAssetInfo> possibleAssetsInfo = possibleAssetForPossibilityMap.get(possibilityId);
      // for (PossibleAssetInfo possibleAssetInfo : possibleAssetsInfo) {
      // possibleAssetInfo.setStandarizedTotalTypeBasedWeight(standardizedValues.get(index++));
      // }
      // }

      // federate by top cluster
      getQueryFederationService().federatePossibleAssetInfoByTopCluster(possibleAssetForPossibilityMap,
               businessQueryMap, filteredAssetsMap);

      // TODO: -RG- Remove later - Added for testing
      if (logger.isInfoEnabled()) {
         logger.info("Possible Assets by Possibility after federation by Asset Info, ");
         SemanticDriverPrintHelper.printPossibleAssetInfo(possibleAssetForPossibilityMap);
      }
      return possibleAssetForPossibilityMap;
   }

   public Map<Long, List<PossibleAssetInfo>> getPossibleAssetInfoMap (List<PossibleAssetInfo> possibleAssetInfos) {
      Map<Long, List<PossibleAssetInfo>> possibiltyMap = new LinkedHashMap<Long, List<PossibleAssetInfo>>();
      for (PossibleAssetInfo possibleAssetInfo : possibleAssetInfos) {
         long possibilityId = possibleAssetInfo.getPossibilityId();
         if (possibiltyMap.containsKey(possibilityId)) {
            List<PossibleAssetInfo> possibleAssetForPossibility = possibiltyMap.get(possibilityId);
            possibleAssetForPossibility.add(possibleAssetInfo);
         } else {
            List<PossibleAssetInfo> possibleAssetForPossibility = new ArrayList<PossibleAssetInfo>();
            possibleAssetForPossibility.add(possibleAssetInfo);
            possibiltyMap.put(possibilityId, possibleAssetForPossibility);
         }
      }
      return possibiltyMap;
   }

   /**
    * @param businessQuery
    * @param queryResult
    * @param possibleAssetsInfo
    * @param businessEntityTerms
    * @return the QueryResult
    * @throws ExeCueException
    */
   // TODO: NK: Needs further refactoring, and should rename it later once we remove the old processBusinessQuery method
   // implementation
   protected QueryResult processBusinessQuery2 (BusinessQuery businessQuery, QueryResult queryResult,
            List<PossibleAssetInfo> possibleAssetsInfo, Set<BusinessEntityTerm> businessEntityTerms)
            throws ExeCueException {
      try {
         long startTime = System.currentTimeMillis();
         List<PossibilityResult> possibilites = queryResult.getPossibilites();
         List<AssetResult> assets = new ArrayList<AssetResult>();

         PossibilityResult possibilityResult = new PossibilityResult();
         possibilityResult.setId(1); // default ID
         possibilityResult.setScoped(businessQuery.isScoped());
         possibilityResult.setAssets(assets);
         possibilityResult.setApplicationType(businessQuery.getAppSourceType());

         possibilites.add(possibilityResult);
         queryResult.setPossibilites(possibilites);

         NormalizedPseudoQuery pseudoQuery = getPseudoLanguageService().getPseudoQuery(businessQuery);
         String businessStatement = getPseudoLanguageService().getPseudoLanguageStatement(pseudoQuery);
         long endTime = System.currentTimeMillis();
         if (logger.isInfoEnabled()) {
            logger.info("Time taken to generate pseudo statements " + (endTime - startTime) / 1000.0 + " seconds");
         }
         // TODO :-VG- do we need to store business query
         startTime = System.currentTimeMillis();
         QDataBusinessQuery qDataBusinessQuery = getDriverHelper().getQueryDataService().storeBusinessQuery(
                  queryResult.getId(), businessQuery, businessStatement);
         endTime = System.currentTimeMillis();
         if (logger.isInfoEnabled()) {
            logger.info("Time taken to store Business Query " + (endTime - startTime) / 1000.0 + " seconds");
         }

         // This is required in case of Advanced Search to populate the search box on page 2. It will get
         // over ridden in free form search with the given user query
         queryResult.setQueryName(businessStatement);

         if (businessQuery.getAppSourceType() == AppSourceType.UNSTRUCTURED) {
            return queryResult;
         }

         startTime = System.currentTimeMillis();
         List<PossibleAssetInfo> cachePossibleAssetInfo = new ArrayList<PossibleAssetInfo>(1);
         Set<Asset> answersAsset = getUserQueryPossibilityService().loadAnswerableAssets(possibleAssetsInfo,
                  cachePossibleAssetInfo);
         endTime = System.currentTimeMillis();
         if (logger.isInfoEnabled()) {
            logger.info("Time taken to load assets: " + (endTime - startTime) / 1000.0 + " seconds");
         }

         populateLiveAssetResults(businessQuery, possibleAssetsInfo, businessEntityTerms, assets, qDataBusinessQuery,
                  answersAsset);

         populateCachedAssetResults(queryResult, assets, cachePossibleAssetInfo);

         // sort asset results based on data present or not.
         List<AssetResult> resultsDataPresent = new ArrayList<AssetResult>(1);
         for (AssetResult assetResult : assets) {
            if (assetResult.isDataPresent()) {
               resultsDataPresent.add(assetResult);
            }
         }
         assets.removeAll(resultsDataPresent);
         assets.addAll(0, resultsDataPresent);
         List<String> messages = queryResult.getMessages();
         if (assets.size() > 1 && !messages.contains(ExeCueMessages.RESULTS_FROM_MULTIPLE_DATA_SOURCES_KEY)) {
            messages.add(ExeCueMessages.RESULTS_FROM_MULTIPLE_DATA_SOURCES_KEY);
         }
         queryResult.setMessages(messages);
      } catch (QueryDataException queryDataException) {
         logger.error("QueryDataException in BaseDriver", queryDataException);
         logger.error("Actual Error : [" + queryDataException.getCode() + "] " + queryDataException.getMessage());
         logger.error("Cause : " + queryDataException.getCause());
         // throw new ExeCueException(QueryDataExceptionCodes.QD_DEFAULT_EXCEPTION_CODE, queryDataException.getCause());
         queryResult.setError(Integer.toString(QueryDataExceptionCodes.QD_DEFAULT_EXCEPTION_CODE));
      } catch (GovernorException governorException) {
         logger.error("GovernorException in BaseDriver", governorException);
         logger.error("Actual Error : [" + governorException.getCode() + "] " + governorException.getMessage());
         logger.error("Cause : " + governorException.getCause());
         // throw new ExeCueException(GovernorExceptionCodes.GOV_DEFAULT_EXCEPTION_CODE, governorException.getCause());
         queryResult.setError(Integer.toString(GovernorExceptionCodes.GOV_DEFAULT_EXCEPTION_CODE));
      } catch (Exception exception) {
         logger.error("Exception in BaseDriver", exception);
         logger.error("Actual Error : " + exception.getMessage());
         logger.error("Cause : " + exception.getCause());
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, "Error in BaseDriver", exception
                  .getCause());
      } finally {
         // MDC.remove("txnId");
      }
      return queryResult;
   }

   /**
    * This method filters the asset based on the security settings
    * 
    * @param possibleAssetsInfo
    * @return the Map<Long, Asset>
    * @throws SWIException
    */
   private Map<Long, Asset> filterAssetsBySecurity (List<PossibleAssetInfo> possibleAssetsInfo) throws SWIException {
      long startST = System.currentTimeMillis();
      Set<Asset> answerableAssets = getUserQueryPossibilityService().filterByAssetSecurity(possibleAssetsInfo);

      Map<Long, Asset> filteredAssetsMap = getAssetsMap(answerableAssets);
      // Remove the remaining PossibleAssetInfo from the list
      for (Iterator<PossibleAssetInfo> iterator = possibleAssetsInfo.iterator(); iterator.hasNext();) {
         PossibleAssetInfo possibleAssetInfo = iterator.next();
         if (!filteredAssetsMap.containsKey(possibleAssetInfo.getAssetId())) {
            iterator.remove();
         }
      }
      long endST = System.currentTimeMillis();
      if (logger.isInfoEnabled()) {
         logger.info("Time taken to get possible assets and to apply security filter " + (endST - startST) / 1000.0
                  + " seconds");
      }

      return filteredAssetsMap;
   }

   private Map<Long, Asset> getAssetsMap (Set<Asset> answerAssets) {
      Map<Long, Asset> assetsMap = new HashMap<Long, Asset>();
      if (CollectionUtils.isEmpty(answerAssets)) {
         return assetsMap;
      }
      for (Asset asset : answerAssets) {
         assetsMap.put(asset.getId(), asset);
      }
      return assetsMap;
   }

   /**
    * This method performs the aggregation for the given list of possibility results
    * 
    * @param possibilityResults
    * @param queryId
    * @throws SDXException
    */
   protected void performAggregation (List<PossibilityResult> possibilityResults, Long queryId) throws SDXException {
      for (PossibilityResult possibilityResult : possibilityResults) {

         // If its unstructured app, then skip aggregation
         if (possibilityResult.getApplicationType() == AppSourceType.UNSTRUCTURED) {
            continue;
         }
         List<AssetResult> loadedResults = new ArrayList<AssetResult>();
         for (AssetResult result : possibilityResult.getAssets()) {
            if (!result.isFromQueryCache()) {
               AssetQuery assetQuery = result.getAssetQuery();
               result = processAggregation(assetQuery, queryId, result.getBusinessQueryId());
               result.setRelevance(assetQuery.getLogicalQuery().getRelevance());
               driverHelper.populateAssetInfo(result, assetQuery.getLogicalQuery().getAsset());
            }
            loadedResults.add(result);
         }
         possibilityResult.setAssets(loadedResults);
         List<AssetResult> resultsDataPresent = new ArrayList<AssetResult>();
         List<AssetResult> assets = possibilityResult.getAssets();
         for (AssetResult assetResult : assets) {
            if (assetResult.isDataPresent()) {
               resultsDataPresent.add(assetResult);
            }
         }
         assets.removeAll(resultsDataPresent);
         assets.addAll(0, resultsDataPresent);

         // load the short note and extended asset detail id
         for (AssetResult assetResult : assets) {
            AssetDetail assetDetail = getAssetDetailService().getAssetDetailInfo(assetResult.getAssetId());
            assetResult.setAssetDetailId(assetDetail.getId());
            assetResult.setShortNote(assetDetail.getShortNote());
         }

         // set the application name and url into the Possibility Result object
         Asset tempAsset = possibilityResult.getAssets().get(0).getAssetQuery().getLogicalQuery().getAsset();
         possibilityResult.setAppName(tempAsset.getApplication().getName());
         possibilityResult.setAppUrl(tempAsset.getApplication().getApplicationURL());
         possibilityResult.setAppId(tempAsset.getApplication().getId());
         possibilityResult.setAppImageId(tempAsset.getApplication().getImageId());
         possibilityResult.setApplicationType(tempAsset.getApplication().getSourceType());
      }
   }

   /**
    * This method sets the standardized possible asset info
    * 
    * @param possibleAssetsInfo
    * @param possibilityWeight
    * @param maxPossibleBQWeight
    */
   protected void setStandardizedTotalTypeBasedWeight (List<PossibleAssetInfo> possibleAssetsInfo,
            double possibilityWeight, double maxPossibleBQWeight) {
      if (CollectionUtils.isEmpty(possibleAssetsInfo)) {
         return;
      }
      for (PossibleAssetInfo possibleAssetInfo : possibleAssetsInfo) {
         double standarizedTotalTypeBasedWeight = possibilityWeight * possibleAssetInfo.getTotalTypeBasedWeight()
                  / maxPossibleBQWeight;
         possibleAssetInfo.setStandarizedTotalTypeBasedWeight(standarizedTotalTypeBasedWeight);
      }
   }

   /**
    * This method sets the relevance for the possible asset info
    * 
    * @param possibleAssetsInfo
    * @param baseUserQueryWeight
    */
   protected void setAssetRelevance (List<PossibleAssetInfo> possibleAssetsInfo, double baseUserQueryWeight) {
      for (PossibleAssetInfo assetInfo : possibleAssetsInfo) {
         double relevance = assetInfo.getStandarizedTotalTypeBasedWeight() / baseUserQueryWeight * 100;
         assetInfo.setRelavance(relevance);
      }
   }

   /**
    * Method to get the user query possibilities for the given business query and its businessEntityTerms. It also
    * updates the max possible business query weight in the business query.
    * 
    * @param businessEntityTerms
    * @param businessQuery
    * @param userQueryId
    * @param possibilityId
    * @param userQueryExecueionDate
    * @return the List<UserQueryPossibility>
    */
   protected List<UserQueryPossibility> getUserQueryPossibilties (Set<BusinessEntityTerm> businessEntityTerms,
            BusinessQuery businessQuery, long userQueryId, long possibilityId, Date userQueryExecueionDate) {
      List<UserQueryPossibility> userQueryPossibilityList = new ArrayList<UserQueryPossibility>();

      if (CollectionUtils.isEmpty(businessEntityTerms)) {
         return userQueryPossibilityList;
      }
      Double maxPossibleBQWeight = 0D;
      for (BusinessEntityTerm businessEntityTerm : businessEntityTerms) {
         UserQueryPossibility userQueryPossibility = new UserQueryPossibility();
         userQueryPossibility.setModelId(businessQuery.getModelId());
         userQueryPossibility.setPossibilityId(possibilityId);
         userQueryPossibility.setEntityBedId(businessEntityTerm.getBusinessEntityDefinitionId());
         userQueryPossibility.setRecWeight(0.0); // TODO: NK: check with GA, if we need this?
         userQueryPossibility.setMaxPossibleWeight(0.0); // NK: check with GA, if we need this?
         userQueryPossibility.setEntityType(businessEntityTerm.getBusinessEntityType());
         if (businessEntityTerm.getBusinessEntityType() == BusinessEntityType.CONCEPT) {
            userQueryPossibility.setTypeBasedWeight(getConceptRecognitionWeight());
            maxPossibleBQWeight += getConceptRecognitionWeight();
         } else if (businessEntityTerm.getBusinessEntityType() == BusinessEntityType.CONCEPT_LOOKUP_INSTANCE) {
            userQueryPossibility.setTypeBasedWeight(getInstanceRecognitionWeight());
            maxPossibleBQWeight += getInstanceRecognitionWeight();
         } else if (businessEntityTerm.getBusinessEntityType() == BusinessEntityType.RELATION) {
            userQueryPossibility.setTypeBasedWeight(getRelationRecognitionWeight());
         }
         userQueryPossibility.setUserQueryId(userQueryId);
         userQueryPossibility.setExecutionDate(userQueryExecueionDate);
         userQueryPossibility.setMeasureGroupBy(businessEntityTerm.getMeasureGroupBy());
         userQueryPossibility.setMeasureConditionWithoutStat(businessEntityTerm.getMeasureConditionWithoutStat());
         userQueryPossibilityList.add(userQueryPossibility);
      }
      // Set the max possible weight
      businessQuery.setMaxPossibleBQWeight(maxPossibleBQWeight);
      return userQueryPossibilityList;
   }

   /**
    * @param allBusinessEntityTerms
    * @return the Set<BusinessEntityTerm>
    */
   protected Set<BusinessEntityTerm> getUserRequestedBusinessEntityTerms (Set<BusinessEntityTerm> allBusinessEntityTerms) {
      Set<BusinessEntityTerm> userRequestedBusinessEntityTerms = new HashSet<BusinessEntityTerm>();
      for (BusinessEntityTerm businessEntityTerm : allBusinessEntityTerms) {
         if (businessEntityTerm.isRequestedByUser()) {
            userRequestedBusinessEntityTerms.add(businessEntityTerm);
         }
      }
      return userRequestedBusinessEntityTerms;
   }

   private void populateCachedAssetResults (QueryResult queryResult, List<AssetResult> assets,
            List<PossibleAssetInfo> cachePossibleAssetInfo) throws QueryDataException {
      if (ExecueCoreUtil.isCollectionNotEmpty(cachePossibleAssetInfo)) {
         queryResult.setCacheResultPresent(true);
         for (PossibleAssetInfo possibleAssetInfo : cachePossibleAssetInfo) {
            AssetResult assetResult = new AssetResult();
            assetResult.setAssetId(possibleAssetInfo.getAssetId());
            assetResult.setBusinessQueryId(possibleAssetInfo.getBusinessQueryId());
            assetResult.setReportStatus(MessageStatusType.COMPLETED.getValue());
            assetResult.setFromQueryCache(possibleAssetInfo.isFromQueryCache());
            assetResult.setCachedDate(driverHelper.formatCachedDate(possibleAssetInfo.getCachedDate()));
            assetResult.setRelevance(Double.parseDouble(String.format("%.2f", possibleAssetInfo.getRelavance())));
            driverHelper.populateAssetResult(assetResult, possibleAssetInfo.getUserQueryId(), true);
            assets.add(assetResult);
         }
      }
   }

   private void populateLiveAssetResults (BusinessQuery businessQuery, List<PossibleAssetInfo> possibleAssetsInfo,
            Set<BusinessEntityTerm> businessEntityTerms, List<AssetResult> assets,
            QDataBusinessQuery qDataBusinessQuery, Set<Asset> answersAsset) throws GovernorException {
      long startTime;
      long endTime;
      if (ExecueCoreUtil.isCollectionNotEmpty(answersAsset)) {
         startTime = System.currentTimeMillis();
         // call Governor
         List<AssetQuery> assetQueryList = governorService.extractDataAssetQueries2(businessQuery, answersAsset,
                  businessEntityTerms);
         endTime = System.currentTimeMillis();
         if (logger.isInfoEnabled()) {
            logger.info("Time taken By Governor to process business Query " + (endTime - startTime) / 1000.0
                     + " seconds");
            logger.info("Asset query list size : " + assetQueryList.size());
         }
         Map<Long, PossibleAssetInfo> possibleAssetInfoMap = getPossibleAssetInfoByAssetIdMap(possibleAssetsInfo);
         for (AssetQuery assetQuery : assetQueryList) {
            Long assetId = assetQuery.getLogicalQuery().getAsset().getId();
            PossibleAssetInfo possibleAssetInfo = possibleAssetInfoMap.get(assetId);

            // Set the weights information
            assetQuery.getLogicalQuery().setAssetWeight(possibleAssetInfo.getTotalTypeBasedWeight());
            assetQuery.getLogicalQuery().setStandarizedAssetWeight(
                     possibleAssetInfo.getStandarizedTotalTypeBasedWeight());
            assetQuery.getLogicalQuery().setStandarizedApplicationWeight(
                     businessQuery.getStandarizedApplicationWeight());
            assetQuery.getLogicalQuery().setStandarizedPossiblityWeight(businessQuery.getStandarizedPossiblityWeight());
            assetQuery.getLogicalQuery().setRelevance(
                     Double.parseDouble(String.format("%.2f", possibleAssetInfo.getRelavance())));
         }

         // process Aggregation to prepare the asset result
         startTime = System.currentTimeMillis();
         for (AssetQuery assetQuery : assetQueryList) {
            // AssetResult assetResult = processAggregation(assetQuery, queryResult.getId(),
            // qDataBusinessQuery.getId());
            AssetResult assetResult = new AssetResult();
            assetResult.setBusinessQueryId(qDataBusinessQuery.getId());
            assetResult.setAssetQuery(assetQuery);
            assetResult.setAssetId(assetQuery.getLogicalQuery().getAsset().getId());
            driverHelper.populateAssetInfo(assetResult, assetQuery.getLogicalQuery().getAsset());
            assetResult.setRelevance(assetQuery.getLogicalQuery().getRelevance());
            assets.add(assetResult);
         }
         endTime = System.currentTimeMillis();
         if (logger.isInfoEnabled()) {
            logger.info("Time taken by aggregation " + (endTime - startTime) / 1000.0 + " seconds");
         }
      }
   }

   private Map<Long, PossibleAssetInfo> getPossibleAssetInfoByAssetIdMap (List<PossibleAssetInfo> possibleAssetsInfo) {
      Map<Long, PossibleAssetInfo> possibleAssetInfoMap = new HashMap<Long, PossibleAssetInfo>();
      for (PossibleAssetInfo possibleAssetInfo : possibleAssetsInfo) {
         possibleAssetInfoMap.put(possibleAssetInfo.getAssetId(), possibleAssetInfo);
      }
      return possibleAssetInfoMap;
   }

   private AssetResult processAggregation (AssetQuery assetQuery, long queryId, long businessQueryId) {
      long startTime = System.currentTimeMillis();

      AssetResult assetResult = new AssetResult();

      MessageInput minput = new MessageInput();
      minput.setTransactionId(queryId);
      AggregationMessage aMessage = new AggregationMessage();
      aMessage.setAssetQuery(assetQuery);
      aMessage.setQueryId(queryId);
      aMessage.setBusinessQueryId(businessQueryId);

      // Query representation is only required of the flag is set to true
      if (getDriverConfigurationService().isGovernorQueryRepresentationRequired()
               || CheckType.NO == assetQuery.getLogicalQuery().getAsset().getQueryExecutionAllowed()) {
         aMessage.setGovernorQueryRepresentation(driverHelper.getQueryRepresentation(assetQuery.getPhysicalQuery(),
                  assetQuery.getLogicalQuery().getAsset()));
      }

      minput.setObject(aMessage);
      MessageInfo minfo = null;
      try {
         if (logger.isDebugEnabled()) {
            logger.debug("Starting thread");
         }
         minfo = reportMessage.processMessage(minput);
         assetResult.setReportStatus(minfo.getStatus().getValue());
         assetResult.setMessageId(minfo.getId());
         assetResult.setAssetId(assetQuery.getLogicalQuery().getAsset().getId());
         assetResult.setBusinessQueryId(businessQueryId);
         assetResult.setAssetQuery(assetQuery);
         if (logger.isDebugEnabled()) {
            logger.debug("Created thread for asset id : " + assetResult.getAssetId() + "; BQ id : "
                     + assetResult.getBusinessQueryId());
         }
         if (MessageStatusType.COMPLETED.equals(minfo.getStatus())) {
            driverHelper.populateAssetResult(assetResult, queryId, false);
         } else if (MessageStatusType.ERROR.equals(minfo.getStatus())) {
            assetResult.setError(Integer.toString(QueryDataExceptionCodes.QD_AGGREGATION_QUERY_EXCEPTION_CODE));
         } else { // For displaying the relevant details till the report details are ready
            // generate the description using the SQ
            NormalizedPseudoQuery pseudoQuery = pseudoLanguageService.getPseudoQuery(assetQuery.getLogicalQuery());
            assetResult.setPseudoStatement(pseudoLanguageService.getPseudoLanguageStatement(pseudoQuery));
            assetResult.setReportHeader(pseudoLanguageService.generateTitle(pseudoQuery));
         }
      } catch (MessageException messageException) {
         messageException.printStackTrace();
         logger.error("MessageException in BaseDriver in processAggregation", messageException);
         logger.error("Actual Error : [" + messageException.getCode() + "] " + messageException.getMessage());
         logger.error("Cause : " + messageException.getCause());
         assetResult.setError(Integer.toString(messageException.getCode()));
      } catch (QueryDataException queryDataException) {
         queryDataException.printStackTrace();
         logger.error("QueryDataException in BaseDriver in processAggregation", queryDataException);
         logger.error("Actual Error : [" + queryDataException.getCode() + "] " + queryDataException.getMessage());
         logger.error("Cause : " + queryDataException.getCause());
         assetResult.setError(Integer.toString(QueryDataExceptionCodes.QD_AGGREGATION_QUERY_EXCEPTION_CODE));
      }
      long endTime = System.currentTimeMillis();
      logger.info("time taken by aggregation " + (endTime - startTime) / 1000.0 + " seconds");
      return assetResult;
   }

   private double getConceptRecognitionWeight () {
      return getCoreConfigurationService().getAssetBasedConceptDefaultRecognitionWeight();
   }

   private double getInstanceRecognitionWeight () {
      return getCoreConfigurationService().getAssetBasedInstanceDefaultRecognitionWeight();
   }

   private double getRelationRecognitionWeight () {
      return getCoreConfigurationService().getAssetBasedRelationDefaultRecognitionWeight();
   }

   protected IDriverConfigurationService getDriverConfigurationService () {
      return getDriverHelper().getDriverConfigurationService();
   }

   public DriverHelper getDriverHelper () {
      return driverHelper;
   }

   public void setDriverHelper (DriverHelper driverHelper) {
      this.driverHelper = driverHelper;
   }

   public IUserQueryPossibilityService getUserQueryPossibilityService () {
      return userQueryPossibilityService;
   }

   public void setUserQueryPossibilityService (IUserQueryPossibilityService userQueryPossibilityService) {
      this.userQueryPossibilityService = userQueryPossibilityService;
   }

   public IAssetDetailService getAssetDetailService () {
      return assetDetailService;
   }

   public void setAssetDetailService (IAssetDetailService assetDetailService) {
      this.assetDetailService = assetDetailService;
   }

   public IUserContext getUserContext () {
      return userContext;
   }

   public void setUserContext (IUserContext userContext) {
      this.userContext = userContext;
   }

   public void setGovernorService (IGovernorService governorService) {
      this.governorService = governorService;
   }

   public void setReportMessage (IMessage repotMessage) {
      this.reportMessage = repotMessage;
   }

   public void setMessageService (IMessageService messageService) {
      this.messageService = messageService;
   }

   public void setQueryCacheService (IQueryCacheService queryCacheService) {
      this.queryCacheService = queryCacheService;
   }

   public IPseudoLanguageService getPseudoLanguageService () {
      return pseudoLanguageService;
   }

   public void setPseudoLanguageService (IPseudoLanguageService pseudoLanguageService) {
      this.pseudoLanguageService = pseudoLanguageService;
   }

   /**
    * @return the queryFederationService
    */
   public IQueryFederationService getQueryFederationService () {
      return queryFederationService;
   }

   /**
    * @param queryFederationService
    *           the queryFederationService to set
    */
   public void setQueryFederationService (IQueryFederationService queryFederationService) {
      this.queryFederationService = queryFederationService;
   }

   public IQueryCacheConfiguration getQueryCacheConfiguration () {
      return queryCacheConfiguration;
   }

   public void setQueryCacheConfiguration (IQueryCacheConfiguration queryCacheConfiguration) {
      this.queryCacheConfiguration = queryCacheConfiguration;
   }

   public IUidService getTransactionIdGenerationService () {
      return transactionIdGenerationService;
   }

   public void setTransactionIdGenerationService (IUidService transactionIdGenerationService) {
      this.transactionIdGenerationService = transactionIdGenerationService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}