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


/**
 *
 */
package com.execue.driver.semantic.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import com.execue.core.common.bean.AssetResult;
import com.execue.core.common.bean.BusinessCondition;
import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.PossibilityResult;
import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.QueryResult;
import com.execue.core.common.bean.RelatedUserQuery;
import com.execue.core.common.bean.UnstructuredQueryResult;
import com.execue.core.common.bean.UserInput;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.VerticalEntityBasedRedirection;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.nlp.CandidateEntity;
import com.execue.core.common.bean.nlp.NLPInformation;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.nlp.UserQuery;
import com.execue.core.common.bean.qdata.QDataReducedQuery;
import com.execue.core.common.bean.qdata.QDataUserQuery;
import com.execue.core.common.bean.qdata.QueryCacheResultInfo;
import com.execue.core.common.bean.qdata.RFXValue;
import com.execue.core.common.bean.qdata.RIUniversalSearch;
import com.execue.core.common.bean.qdata.ReducedFormIndex;
import com.execue.core.common.bean.qdata.UnStructuredIndex;
import com.execue.core.common.bean.qdata.UniversalSearchResult;
import com.execue.core.common.bean.security.UserRemoteLocationInfo;
import com.execue.core.common.bean.swi.PossibleAssetInfo;
import com.execue.core.common.bean.swi.UserQueryPossibility;
import com.execue.core.common.bean.uss.UniversalUnstructuredSearchResult;
import com.execue.core.common.bean.uss.UnstructuredPossibleAssetInfo;
import com.execue.core.common.bean.uss.UnstructuredSearchResultItem;
import com.execue.core.common.messages.ExeCueMessages;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.MessageStatusType;
import com.execue.core.common.type.RFXVariationSubType;
import com.execue.core.common.type.SearchFilterType;
import com.execue.core.common.type.UniversalSearchType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.driver.BaseDriver;
import com.execue.driver.PossibleAssetComparator;
import com.execue.driver.semantic.ISemanticDriver;
import com.execue.driver.semantic.helper.SemanticDriverHelper;
import com.execue.governor.exception.GovernorExceptionCodes;
import com.execue.ks.exception.KnowledgeSearchException;
import com.execue.ks.service.IKnowledgeBaseSearchEngine;
import com.execue.nlp.engine.INLPEngine;
import com.execue.nlp.exception.NLPException;
import com.execue.nlp.generator.impl.ReducedFormBusinessQueryGenerator;
import com.execue.platform.IBusinessQueryOrganizationService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.exception.RFXException;
import com.execue.qdata.exception.UDXException;
import com.execue.qdata.service.IRFXService;
import com.execue.qdata.service.IUDXService;
import com.execue.querycache.configuration.IQueryCacheService;
import com.execue.querycache.configuration.QueryCacheConstants;
import com.execue.querycache.exception.QueryCacheException;
import com.execue.sus.helper.RFXServiceHelper;
import com.execue.sus.helper.SemantificationHelper;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.uss.beanfactory.UnstructuredWarehouseSearchEngineFactory;
import com.execue.uss.configuration.IUnstructuredSearchConfigurationService;
import com.execue.uss.exception.USSException;
import com.execue.uss.service.IEntitySearchEngine;
import com.execue.uss.service.IUnstructuredWarehouseSearchEngine;
import com.execue.util.print.SemanticDriverPrintHelper;

/**
 * @author Nihar
 */
public class SemanticDriver extends BaseDriver implements ISemanticDriver {

   private Logger                                  logger = Logger.getLogger(SemanticDriver.class);

   private ReducedFormBusinessQueryGenerator       reducedFormBQGenerator;
   private IBusinessQueryOrganizationService       businessQueryOrganizationService;
   private IKDXManagementService                   kdxManagementService;
   private IKDXRetrievalService                    kdxRetrievalService;
   private IQueryCacheService                      queryCacheService;
   private IKnowledgeBaseSearchEngine              knowledgeSearchEngine;
   private IEntitySearchEngine                     entitySearchEngine;
   private IUnstructuredSearchConfigurationService unstructuredSearchConfigurationService;
   private IUnstructuredWarehouseSearchEngine      unstructuredWarehouseSearchEngine;
   private INLPEngine                              nlpEngine;
   private SemanticDriverHelper                    semanticDriverHelper;
   private RFXServiceHelper                        rfxServiceHelper;
   private IUDXService                             udxService;
   private IRFXService                             rfxService;
   private IApplicationRetrievalService            applicationRetrievalService;

   public QueryResult process (Object userInputObject) throws ExeCueException {
      QueryResult queryResult = initializeResult();
      UserInput userInput = (UserInput) userInputObject;
      String request = userInput.getRequest();
      if (ExecueCoreUtil.isEmpty(request)) {
         return queryResult;
      }
      try {
         // Set the query id as transaction id in the Mapped Diagnostic Context of log4j
         String tidValue = "txnId-" + queryResult.getId();
         MDC.put("txnId", tidValue);

         // Validate the length of the user request
         if (request.length() >= getCoreConfigurationService().getMaxUserQueryLength()) {
            logger.debug("MAX LIMIT EXCEEDED....throwing exception");
            throw new ExeCueException(ExeCueExceptionCodes.MAX_USER_QUERY_LENGTH_EXCEPTION_CODE,
                     "User Query has exceeded maximum limit");
         }

         // Update basic information in query result from the user input
         updateBasicInfoFromUserInput(queryResult, userInput);

         // Save user query in qdata
         long queryId = queryResult.getId();
         long startTime = System.currentTimeMillis();
         CheckType anonymous = CheckType.YES;
         Long userId = -999L;
         if (userInput.getUserId() != null && userInput.getUserId() != -1) {
            userId = userInput.getUserId();
            anonymous = CheckType.NO;
         }
         QDataUserQuery qdataUserQuery = getDriverHelper().getQueryDataService().storeUserQuery(queryId,
                  new QueryForm(), request, userId, anonymous);
         Date userQueryExecutionDate = qdataUserQuery.getExecutionDate();
         long endTime = System.currentTimeMillis();
         logger.info("Time taken to store User Query " + (endTime - startTime) / 1000.0 + " seconds");

         // Prepare the user query
         UserQuery userQuery = prepareUserQuery(userInput);

         // Obtain possibilities for the user query
         NLPInformation nlpInformation = getNlpEngine().processQuery(userQuery, null);
         Set<SemanticPossibility> possibilities = nlpInformation.getReducedForms().keySet();

         // TODO -KA- Temp fix to avoid Unstructured app visibility to users.
         boolean isUnstructuredAppVisibleToUser = getCoreConfigurationService().getUnstructuredAppUserVisibility();
         if (!isUnstructuredAppVisibleToUser) {
            List<SemanticPossibility> removeReducedForms = new ArrayList<SemanticPossibility>();
            for (SemanticPossibility reducedForm : possibilities) {
               if (reducedForm.getApplication().getSourceType() == AppSourceType.UNSTRUCTURED
                        && !getUserContext().getUser().getId().equals(reducedForm.getApplication().getOwner().getId())) {
                  removeReducedForms.add(reducedForm);
               }
            }
            for (SemanticPossibility semanticPossibility : removeReducedForms) {
               nlpInformation.getReducedForms().remove(semanticPossibility);
            }
         }

         // Reset the search filter
         userQuery.setSearchFilter(userInput.getSearchFilter());
         queryResult.setTokenCandedates(userQuery.getNlpInformation().getTokenCandidates());

         // Process the possibilities to get the asset results and populate the query result
         if (!CollectionUtils.isEmpty(possibilities)) {
            Map<Long, QDataReducedQuery> reducedQueryByAppId = new HashMap<Long, QDataReducedQuery>(1);
            Set<ReducedFormIndex> rfxList = new HashSet<ReducedFormIndex>(1);
            Set<RFXValue> rfxValues = new HashSet<RFXValue>(1);
            Map<Long, Set<ReducedFormIndex>> rfxByAppId = new HashMap<Long, Set<ReducedFormIndex>>(1);

            // Generate the RI USER QUERY entries
            getSemanticDriverHelper().generateRIUserQueries(possibilities, rfxList, rfxValues, reducedQueryByAppId,
                     rfxByAppId, new HashMap<Long, Integer>(), queryId,
                     userQuery.getBaseWeightInformation().getFinalWeight(), getDependentConceptByRequiredConcept());

            // Currently populating the rfx value only if the unstructured app exists, later should populate
            // for all if we start using it for related query, related news article, etc
            if (checkIfUnstructuredAppExists(possibilities) && !CollectionUtils.isEmpty(rfxValues)) {
               getRfxService().storeRFXValue(new ArrayList<RFXValue>(rfxValues));
            }

            // Perform the universal search for related queries
            List<RelatedUserQuery> relatedQueries = getQueryCacheService().performUniversalSearchForRelatedQueries(
                     queryId, reducedQueryByAppId, queryResult.getQueryName());
            queryResult.setRelatedUserQueries(relatedQueries);

            // REDIRECT IF ONLY ONE APP AND THAT APP HAS A SPECIFIC URL SPECIFIED IN THE CONFIGURATION
            String redirectURL = null;
            if (possibilities.size() == 1) {
               Application application = possibilities.iterator().next().getApplication();
               Long applicationId = application.getId();
               if (getDriverConfigurationService().getUniqueAppPossiblilityRedirectionMap(applicationId) != null) {
                  redirectURL = getDriverConfigurationService().getUniqueAppPossiblilityRedirectionMap(applicationId);
                  return getUnstructuredQueryResult(queryResult, possibilities, redirectURL, false);
               } else if (application.getSourceType() == AppSourceType.UNSTRUCTURED) {
                  redirectURL = getDriverConfigurationService().getGenericUnstructuredAppRedirectUrl();
                  return getUnstructuredQueryResult(queryResult, possibilities, redirectURL, false);
               }
            }

            // Perform the universal search for query cache
            List<UniversalSearchResult> queryCacheMatchedResults = performUniversalSearchForQueryCache(queryId,
                     reducedQueryByAppId, rfxByAppId, rfxList, userInput.isDisableQueryCache());

            Map<Long, List<UniversalSearchResult>> queryCacheMatchedResultsByAppId = getUniversalSearchMatchedResultsByAppId(
                     queryCacheMatchedResults, reducedQueryByAppId);

            // Perform the universal search for unstructured app
            // TODO: NK: Need to use UniversalSearchResult object for news search articles during performNewsSearch
            List<UniversalUnstructuredSearchResult> unstructuredSearchResults = performUniversalSearchForUnstructuredApp(
                     possibilities, queryResult, userInput.getUserRemoteLocationInfo());

            Map<Long, UniversalUnstructuredSearchResult> unstructuredSearchResultByAppIdMap = getUnstructuredSearchResultByAppIdMap(unstructuredSearchResults);

            processPossibilities(possibilities, queryCacheMatchedResultsByAppId, unstructuredSearchResultByAppIdMap,
                     reducedQueryByAppId, queryResult, userQueryExecutionDate, userInput.isDisableQueryCache(),
                     userQuery.getBaseWeightInformation().getFinalWeight(), userQuery.getOriginalQuery());

            // Perform news search if search filter is vertical and vertical domain is finance
            if (userInput.getSearchFilter() != null
                     && SearchFilterType.VERTICAL == userInput.getSearchFilter().getSearchFilterType()) {
               List<UniversalUnstructuredSearchResult> unstructuredSearchResult = null;
               // TODO : -RG- Considering all the possibilities is wrong here. We should have considered only the
               // ones which are scoped to the current page only needs to be considered
               if (getQueryCacheConfiguration().getConfiguration().getBoolean(
                        QueryCacheConstants.VERTICAL_BASED_SORTING_FLAG)) {
                  // Pass in only the scoped possibilities as NLP might have given results across all the models
                  unstructuredSearchResult = performNewsSearch(getDriverHelper().getScopedPossibilities(possibilities),
                           queryResult.getId());
               } else {
                  unstructuredSearchResult = performNewsSearch(possibilities, queryResult.getId());
               }
               queryResult.setUnstructuredSearchResult(unstructuredSearchResult);
            }
         }
         List<PossibilityResult> possibilityResults = queryResult.getPossibilites();

         // REDIRECT IF AFTER FEDERATION WE HAVE ONLY ONE APP IN THE PossibilityResult
         // AND THAT APP HAS A SPECIFIC URL SPECIFIED IN THE CONFIGURATION
         String redirectURL = null;
         if (queryResult.getRequestedPage() == 1
                  && possibilityResults.size() == 1
                  && (redirectURL = getDriverConfigurationService().getUniqueAppPossiblilityRedirectionMap(
                           possibilityResults.iterator().next().getAppId())) != null) {
            return getUnstructuredQueryResult(queryResult, possibilities, redirectURL, true);
         }

         // Re-adjust the requested page because of unstructured app results(if any)
         adjustRequestedPage(queryResult);

         // Invoke processAggregation for the AssetResult in each of the possibility
         performAggregation(possibilityResults, queryResult.getId());

         // Update the unstructured content information
         updateUnstructuredAppContent(possibilityResults);

         // Set the messages in the query result
         updateMessages(queryResult);

         // update the user query upon successful completion
         if (queryResult.getMessages().isEmpty() || queryResult.getMessages().size() == 1
                  && ExeCueMessages.RESULTS_FROM_MULTIPLE_DATA_SOURCES_KEY.equals(queryResult.getMessages().get(0))) {
            // set the user query request successful flag to 'Y'
            qdataUserQuery.setRequestSuccessful(CheckType.YES);
            getDriverHelper().getQueryDataService().updateQDataUserQuery(qdataUserQuery);
         }
      } catch (KDXException kdxException) {
         logger.error("KDXException in SemanticDriver");
         logger.error("Actual Error : " + kdxException.getMessage());
         logger.error("Cause : " + kdxException.getCause());
         queryResult.setError(Integer.toString(kdxException.getCode()));
         logger.error(kdxException, kdxException);
      } catch (NLPException nlpException) {
         logger.error("NLPException in SemanticDriver");
         logger.error("Actual Error : " + nlpException.getMessage());
         logger.error("Cause : " + nlpException.getCause());
         queryResult.setError(Integer.toString(nlpException.getCode()));
         logger.error(nlpException, nlpException);
      } catch (QueryDataException queryDataException) {
         logger.error("QueryDataException in SemanticDriver");
         logger.error("Actual Error : " + queryDataException.getMessage());
         logger.error("Cause : " + queryDataException.getCause());
         queryResult.setError(Integer.toString(queryDataException.getCode()));
         logger.error(queryDataException, queryDataException);
      } catch (ExeCueException exeCueException) {
         logger.error(exeCueException, exeCueException);
         throw exeCueException;
      } catch (Exception exception) {
         logger.error("Exception in SemanticDriver");
         logger.error("Actual Error : " + exception.getMessage());
         logger.error("Cause : " + exception.getCause());
         logger.error(exception, exception);
         List<String> messages = new ArrayList<String>();
         messages.add(ExeCueMessages.REQUEST_UNRECOGNIZED_KEY);
         queryResult.setMessages(messages);
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, "Error in SemanticDriver", exception
                  .getCause());
      } finally {
         MDC.remove("txnId");
      }
      queryResult.setQueryName(request);
      return queryResult;
   }

   private QueryResult getUnstructuredQueryResult (QueryResult queryResult, Set<SemanticPossibility> possibilities,
            String redirectURL, boolean userQueryInfoSaved) {
      UnstructuredQueryResult newQueryResult = new UnstructuredQueryResult();
      // NK:: Assumption here is we are getting only one possibility out since we are redirecting to that particular app
      List<SemanticPossibility> semList = new ArrayList<SemanticPossibility>(possibilities);
      SemanticPossibility possibility = semList.get(0);
      String colorCodedRequest = pseudoLanguageService.getColorCodedPseudoLanguageStatement(
               possibility.getQueryWords(), possibility.getWordRecognitionStates());
      newQueryResult.setId(queryResult.getId());
      newQueryResult.setApplicationId(possibility.getApplication().getId());
      newQueryResult.setOriginalQuery(queryResult.getOriginalQuery());
      newQueryResult.setColorCodedRequest(colorCodedRequest);
      newQueryResult.setRedirectURL(redirectURL);
      newQueryResult.setRedirectURLPresent(true);
      newQueryResult.setUserQueryInfoSaved(userQueryInfoSaved);
      newQueryResult.setRelatedUserQueries(getFilteredRelatedQueries(queryResult.getRelatedUserQueries()));
      newQueryResult.setSemanticPossibilities(getFilteredPossibilities(possibilities));
      newQueryResult.setApplyImagePresentFilter(false);
      return newQueryResult;
   }

   /**
    * @param possibility
    * @param queryResult
    * @throws KDXException
    */
   private void updateUnstructuredAppContent (List<PossibilityResult> possibilityResults) throws KDXException {
      if (CollectionUtils.isEmpty(possibilityResults)) {
         return;
      }
      for (PossibilityResult possibility : possibilityResults) {
         if (possibility.getApplicationType() != AppSourceType.UNSTRUCTURED) {
            continue;
         }
         Application app = getApplicationRetrievalService().getApplicationByName(possibility.getAppName());

         // TODO: HACK:: Check if it is craigslist auto app. Later should get rid of this code altogether once we have
         // craigslist
         // in the generic unstructured wh
         if (getCoreConfigurationService().getSkipDerivedUserQueryVariation().equals(possibility.getAppName())) {

            for (AssetResult result : possibility.getAssets()) {
               try {
                  UnStructuredIndex unStructuredIndex = getUdxService().getUnstructuredIndexById(result.getAssetId());
                  populateAssetResult(app, result, unStructuredIndex.getShortDescription(), unStructuredIndex.getUrl(),
                           unStructuredIndex.getImageUrl(), unStructuredIndex.getContentDate());
               } catch (UDXException e) {
                  // NK: There is a chance that warehouse is not in synch with qdata udx ids,
                  // As a result we might NOT get the unstructuredIndex from the qdata, so for now skipping the
                  // udx entry
                  e.printStackTrace();
               }
            }
         } else {
            for (AssetResult result : possibility.getAssets()) {
               result.setAppOwnerId(app.getOwner().getId());
               result.setAppOwnerName(app.getOwner().getFirstName());
            }
         }
      }
   }

   /**
    * @param app
    * @param result
    * @param unStructuredIndex
    */
   private void populateAssetResult (Application app, AssetResult result, String shortDescription, String url,
            String imageUrl, Date contentDate) {
      result.setReportHeader(getUpdatedShortDescription(shortDescription));
      result.setReportStatus("COMPLETED");
      result.setHeaderUrl(url);
      result.setImageUrl(imageUrl);
      result.setUnstructuredContentDate(driverHelper.formatUnstructuredContentDate(contentDate));
      result.setDataPresent(true);
      result.setAppOwnerId(app.getOwner().getId());
      result.setAppOwnerName(app.getOwner().getFirstName());
   }

   private Map<Long, UniversalUnstructuredSearchResult> getUnstructuredSearchResultByAppIdMap (
            List<UniversalUnstructuredSearchResult> unstructuredSearchResults) {
      Map<Long, UniversalUnstructuredSearchResult> unstructuredSearchResultByAppIdMap = new HashMap<Long, UniversalUnstructuredSearchResult>(
               1);
      if (CollectionUtils.isEmpty(unstructuredSearchResults)) {
         return unstructuredSearchResultByAppIdMap;
      }
      for (UniversalUnstructuredSearchResult universalUnstructuredSearchResult : unstructuredSearchResults) {
         unstructuredSearchResultByAppIdMap.put(universalUnstructuredSearchResult.getApplicationId(),
                  universalUnstructuredSearchResult);
      }
      return unstructuredSearchResultByAppIdMap;
   }

   /**
    * @param queryResult
    */
   private void adjustRequestedPage (QueryResult queryResult) {
      int pageSize = queryResult.getPageSize();
      int pageNum = queryResult.getRequestedPage();
      int totalAssetResultsCount = queryResult.getPageCount();
      int totalPages = totalAssetResultsCount / pageSize;
      int remainder = totalAssetResultsCount % pageSize;
      if (remainder > 0) {
         totalPages++;
      }
      if (queryResult.getRequestedPage() > totalPages) {
         queryResult.setRequestedPage(totalPages);
         pageNum = totalPages;
      }
      if (logger.isInfoEnabled()) {
         logger.info("Total result count : " + totalAssetResultsCount);
         logger.info("Total pages : " + totalPages);
         logger.info("Page size : " + pageSize);
         logger.info("Current page number : " + pageNum);
         logger.info("Current page result count : " + queryResult.getPossibilites().size());
      }
   }

   /**
    * @param queryResult
    */
   private void updateMessages (QueryResult queryResult) {
      // If queryResult possibility is empty, it means that BQ has not been generated.
      // case 1: there is no BQ (no semantic possibility found)
      // case 2: got a RF but unable to generate BQ from it
      if (ExecueCoreUtil.isCollectionEmpty(queryResult.getPossibilites())) {
         List<String> messages = new ArrayList<String>();
         if (!ExecueCoreUtil.isCollectionEmpty(queryResult.getTokenCandedates())) {
            messages.add(ExeCueMessages.UNABLE_TO_PROCESS_REQUEST);
         } else {
            messages.add(ExeCueMessages.REQUEST_UNRECOGNIZED_KEY);
         }
         queryResult.setMessages(messages);
      } else {
         // Possibilities for the query exist but there assets did not answer
         int answeredAssetsCount = 0;
         for (PossibilityResult possibility : queryResult.getPossibilites()) {
            answeredAssetsCount += possibility.getAssets().size();
         }
         if (answeredAssetsCount == 0) {
            // For all the possibilities, there are no assets which can answer - display apt message
            List<String> messages = null;
            if (ExecueCoreUtil.isCollectionEmpty(queryResult.getMessages())) {
               messages = new ArrayList<String>();
            } else {
               messages = queryResult.getMessages();
            }
            messages.add(ExeCueMessages.RESULTS_FROM_NO_DATA_SOURCES_KEY);
         } else {
            // TODO: -JVK- check for each possibility where there are no answerable assets
            // if a possibility is found with no answering assets - how should we handle this case ?
         }
      }
   }

   /**
    * @param userInput
    * @param request
    * @return the UserQuery object
    */
   private UserQuery prepareUserQuery (UserInput userInput) {
      UserQuery userQuery = new UserQuery();
      userQuery.setOriginalQuery(userInput.getRequest());

      // By pass search filter based on configuration (if vertical based sorting is needed on results)
      if (getQueryCacheConfiguration().getConfiguration().getBoolean(QueryCacheConstants.VERTICAL_BASED_SORTING_FLAG)) {
         userQuery.setSearchFilter(getDriverHelper().getGeneralSearchFilter());
      } else {
         userQuery.setSearchFilter(userInput.getSearchFilter());
      }

      // handle app scoping here. set the flag based on configuration
      userQuery.getSearchFilter().setAppScopingEnabled(getCoreConfigurationService().isAppScopeEnabled());
      return userQuery;
   }

   /**
    * @param userInput
    * @throws ExeCueException
    */
   private void udpateWeightForCandidateEntities (UserInput userInput) throws ExeCueException {
      // implementation of the over-ridden method - public Object process (String input, Object entities)
      if (!ExecueCoreUtil.isCollectionEmpty(userInput.getEntities())) {
         List<CandidateEntity> candidateEntities = userInput.getEntities();
         try {
            getKdxManagementService().updateWeightsForCandidateEntities(candidateEntities);
         } catch (KDXException kdxException) {
            logger.error("KDXException in SemanticDriver");
            logger.error("Actual Error : " + kdxException.getMessage());
            logger.error("Cause : " + kdxException.getCause());
            throw new ExeCueException(kdxException.getCode(), kdxException.getCause());
         }
      }
      // end of the overridden method implementation
   }

   /**
    * @param queryResult
    * @param userInput
    */
   private void updateBasicInfoFromUserInput (QueryResult queryResult, UserInput userInput) {
      if (userInput.getSearchFilter() != null) {
         queryResult.setSearchFilter(userInput.getSearchFilter());
         if (SearchFilterType.VERTICAL.equals(queryResult.getSearchFilter().getSearchFilterType())) {
            queryResult.setVerticalEntityBasedRedirection(getDriverConfigurationService()
                     .getVerticalEntityBasedRedirection(queryResult.getSearchFilter().getId()));
         }
      }
      queryResult.setQueryName(userInput.getRequest());
      queryResult.setOriginalQuery(userInput.getRequest());
      // set the pagination info
      int pageSize = 1;
      if (userInput.getPageSize() != null) {
         pageSize = Integer.parseInt(userInput.getPageSize());
         logger.info("Page Size from Drop Down: " + pageSize);
      } else {
         pageSize = getPageSizeFromConfiguration();
         logger.info("Page Size from Config File: " + pageSize);
      }

      int pageNum = 1;
      if (userInput.getRequestedPage() != null) {
         pageNum = Integer.valueOf(userInput.getRequestedPage());
      }
      logger.info("Requested Page Number: " + pageNum);
      queryResult.setRequestedPage(pageNum);
      queryResult.setPageSize(pageSize);
   }

   protected String getUpdatedShortDescription (String shorDescription) {
      String regex = getUnstructuredSearchConfigurationService().getUnwantedCharRegex();
      return SemantificationHelper.removeUnwantedCharacterFromDescription(shorDescription, regex);
   }

   private Set<SemanticPossibility> getFilteredPossibilities (Set<SemanticPossibility> possibilities) {
      Set<SemanticPossibility> filteredPossibilities = new HashSet<SemanticPossibility>(1);
      if (CollectionUtils.isEmpty(possibilities)) {
         return filteredPossibilities;
      }
      for (SemanticPossibility possibility : possibilities) {
         if (possibility.getApplication().getId() != null
                  && getDriverConfigurationService().getUniqueAppPossiblilityRedirectionMap(
                           possibility.getApplication().getId()) != null) {
            filteredPossibilities.add(possibility);
         }
      }
      return filteredPossibilities;
   }

   private List<RelatedUserQuery> getFilteredRelatedQueries (List<RelatedUserQuery> relatedUserQueries) {
      List<RelatedUserQuery> filteredRelatedUserQueries = new ArrayList<RelatedUserQuery>(1);
      if (CollectionUtils.isEmpty(relatedUserQueries)) {
         return filteredRelatedUserQueries;
      }
      for (RelatedUserQuery relatedUserQuery : relatedUserQueries) {
         if (relatedUserQuery.getApplicationId() != null
                  && getDriverConfigurationService().getUniqueAppPossiblilityRedirectionMap(
                           relatedUserQuery.getApplicationId()) != null) {
            filteredRelatedUserQueries.add(relatedUserQuery);
         }
      }
      return filteredRelatedUserQueries;
   }

   protected int getPageSizeFromConfiguration () {
      return getCoreConfigurationService().getResultsPageSize();
   }

   /**
    * @param userQueryId
    * @param reducedQueryByAppId
    * @param rfxByAppId
    * @param allRfxList
    * @param disableQueryCache
    * @return
    * @throws SWIException
    * @throws RFXException
    * @throws UDXException
    */
   private List<UniversalSearchResult> performUniversalSearchForQueryCache (Long userQueryId,
            Map<Long, QDataReducedQuery> reducedQueryByAppId, Map<Long, Set<ReducedFormIndex>> rfxByAppId,
            Set<ReducedFormIndex> allRfxList, boolean disableQueryCache) throws SWIException, RFXException,
            UDXException {
      // Then try to Match and get the results from the ri_query_cache
      Long universalSearchStartTime = System.currentTimeMillis();
      List<UniversalSearchResult> matchedResults = getUdxService().getUniversalSearchMatchForQueryCache(userQueryId);
      if (logger.isDebugEnabled()) {
         logger.debug("Universal Search For Query Cache Match Time: "
                  + (System.currentTimeMillis() - universalSearchStartTime));
      }

      // Store the query in cache,
      // 1. If no results match after the universal search or
      // 2. If the matched result weight is less than the minimum query cache weight threshold or
      // 3. If the user asks for fresh results(by clicking on the refresh button on UI) as was not happy with the
      // previous query cache results
      // In all such above cases we store the entries in the query cache
      if (CollectionUtils.isEmpty(matchedResults)) {
         storeRfxToQueryCache(allRfxList, reducedQueryByAppId, userQueryId);
      } else {
         // Process for caching the user query if results are not matching the threshold
         int queryCacheWeightThreshold = getQueryCacheConfiguration().getConfiguration().getInt(
                  QueryCacheConstants.QUERY_CACHE_MATCH_RESULT_WEIGHT);
         Map<Long, List<UniversalSearchResult>> universalSearchMatchedResultsByAppId = getUniversalSearchMatchedResultsByAppId(
                  matchedResults, reducedQueryByAppId);
         for (Entry<Long, QDataReducedQuery> entry : reducedQueryByAppId.entrySet()) {
            Long applicationId = entry.getKey();
            if (!universalSearchMatchedResultsByAppId.containsKey(applicationId)) {
               Set<ReducedFormIndex> rfxEntries = rfxByAppId.get(applicationId);
               storeRfxToQueryCache(rfxEntries, reducedQueryByAppId, userQueryId);
            } else {
               QDataReducedQuery matchedReducedQuery = entry.getValue();
               UniversalSearchResult matchedResult = universalSearchMatchedResultsByAppId.get(applicationId).get(0);
               double matchedReducedQueryMaxMatchWeight = matchedReducedQuery.getMaxMatchWeight();
               double matchedWeight = matchedResult.getMatchWeight() / matchedReducedQueryMaxMatchWeight * 100;

               if (matchedWeight < queryCacheWeightThreshold || disableQueryCache
                        || !matchedReducedQuery.getEntityCount().equals(matchedResult.getEntityCount())) {
                  Set<ReducedFormIndex> rfxEntries = rfxByAppId.get(applicationId);
                  storeRfxToQueryCache(rfxEntries, reducedQueryByAppId, userQueryId);
               }
            }

         }
      }
      return matchedResults;
   }

   /**
    * @param allRfxList
    * @param reducedQueryByAppId
    * @param userQueryId
    * @throws RFXException
    * @throws UDXException
    */
   private void storeRfxToQueryCache (Collection<ReducedFormIndex> allRfxList,
            Map<Long, QDataReducedQuery> reducedQueryByAppId, Long userQueryId) throws RFXException, UDXException {
      if (CollectionUtils.isEmpty(allRfxList)) {
         return;
      }
      Set<RIUniversalSearch> riUdxSet = new HashSet<RIUniversalSearch>(1);

      for (ReducedFormIndex reducedFormIndex : allRfxList) {
         Map<RFXVariationSubType, Double> rankingWeightsMap = getRfxService()
                  .getRankingWeightsMapForContentVariationSubType(reducedFormIndex.getRfxVariationSubType());
         QDataReducedQuery dataReducedQuery = reducedQueryByAppId.get(reducedFormIndex.getApplicationId());
         Double entityCount = 1D;
         if (dataReducedQuery != null) {
            entityCount = dataReducedQuery.getEntityCount();
         }
         riUdxSet.addAll(getRfxServiceHelper().getRIUniversalSearchEntries(reducedFormIndex, rankingWeightsMap,
                  UniversalSearchType.QUERY_CACHE_SEARCH, userQueryId, entityCount, null));
      }
      getUdxService().storeRIUDXEntries(new ArrayList<RIUniversalSearch>(riUdxSet));
   }

   protected boolean isUniversalSearchFromWarehouse () {
      return getCoreConfigurationService().getUniversalSearchFromWarehouse();
   }

   private List<UniversalUnstructuredSearchResult> performUniversalSearchForUnstructuredApp (
            Set<SemanticPossibility> possibilities, QueryResult queryResult,
            UserRemoteLocationInfo userRemoteLocationInfo) throws UDXException, USSException {

      List<UniversalUnstructuredSearchResult> unstructuredSearchResultItems = new ArrayList<UniversalUnstructuredSearchResult>(
               1);

      Set<SemanticPossibility> unstructuredAppPossibilities = getUnstructuredAppPossibilities(possibilities);
      if (CollectionUtils.isEmpty(unstructuredAppPossibilities)) {
         return unstructuredSearchResultItems;
      }
      Long universalSearchStartTime = System.currentTimeMillis();
      Long userQueryId = queryResult.getId();
      if (isUniversalSearchFromWarehouse()) {

         // NK: HACK: Later should use only one generic warehouse service call for all unstructured apps
         // Filter the craigslist unstructured possibility
         SemanticPossibility craigslistPossibility = getCraigslistPossibility(unstructuredAppPossibilities);
         if (craigslistPossibility != null) {
            Set<SemanticPossibility> possibilitySet = new HashSet<SemanticPossibility>(1);
            possibilitySet.add(craigslistPossibility);
            // Get the results from warehouse
            List<UniversalUnstructuredSearchResult> craigslistUnstructuredSearchResultItems = UnstructuredWarehouseSearchEngineFactory
                     .getInstance().getUnstructuredWarehouseSearchEngine(
                              craigslistPossibility.getApplication().getName()).performWarehouseSearch(possibilitySet,
                              userRemoteLocationInfo, userQueryId);
            unstructuredSearchResultItems.addAll(craigslistUnstructuredSearchResultItems);

            // Remove the craigslist possibility and call generic warehouse search for unstructured app
            unstructuredAppPossibilities.remove(craigslistPossibility);
         }
         // Perform the warehouse search for the remaining unstructured possibilities
         if (!CollectionUtils.isEmpty(unstructuredAppPossibilities)) {
            List<UniversalUnstructuredSearchResult> genericUnstructuredSearchResultItems = UnstructuredWarehouseSearchEngineFactory
                     .getInstance().getUnstructuredWarehouseSearchEngine("Default").performWarehouseSearch(
                              unstructuredAppPossibilities, userRemoteLocationInfo, userQueryId);
            unstructuredSearchResultItems.addAll(genericUnstructuredSearchResultItems);
         }
      } else {
         // Perform the universal search
         UniversalUnstructuredSearchResult universalSearchResult = getUdxService().getUniversalSearchResultForQuery(
                  userQueryId, 1L, (long) queryResult.getPageSize(), false);
         if (!CollectionUtils.isEmpty(universalSearchResult.getUnstructuredSearchResultItem())) {
            unstructuredSearchResultItems.add(universalSearchResult);
         }
      }
      if (logger.isDebugEnabled()) {
         logger.debug("Universal Search For Unstructured App Match Time: "
                  + (System.currentTimeMillis() - universalSearchStartTime));
      }
      return unstructuredSearchResultItems;
   }

   private SemanticPossibility getCraigslistPossibility (Set<SemanticPossibility> unstructuredAppPossibilities) {
      SemanticPossibility craigslistPossibility = null;
      for (SemanticPossibility semanticPossibility : unstructuredAppPossibilities) {

         if (semanticPossibility.getApplication().getSourceType() == AppSourceType.UNSTRUCTURED
                  && semanticPossibility.getApplication().getName().equalsIgnoreCase(
                           getCoreConfigurationService().getSkipDerivedUserQueryVariation())) {
            craigslistPossibility = semanticPossibility;
            break;
         }
      }
      return craigslistPossibility;
   }

   /**
    * @param possibilities
    * @return the Set<SemanticPossibility> of unstructured app possibilities
    */
   private Set<SemanticPossibility> getUnstructuredAppPossibilities (Set<SemanticPossibility> possibilities) {
      Set<SemanticPossibility> unstucturedPossibilities = new HashSet<SemanticPossibility>(1);
      for (SemanticPossibility semanticPossibility : possibilities) {
         if (semanticPossibility.getApplication().getSourceType() == AppSourceType.UNSTRUCTURED) {
            unstucturedPossibilities.add(semanticPossibility);
         }
      }
      return unstucturedPossibilities;
   }

   /**
    * @param possibilities
    * @return the boolean true/false
    */
   private boolean checkIfUnstructuredAppExists (Set<SemanticPossibility> possibilities) {
      boolean hasUnstructuredApp = false;
      for (SemanticPossibility semanticPossibility : possibilities) {
         if (semanticPossibility.getApplication().getSourceType() == AppSourceType.UNSTRUCTURED) {
            hasUnstructuredApp = true;
            break;
         }
      }
      return hasUnstructuredApp;
   }

   protected List<UniversalUnstructuredSearchResult> performUniversalSearchFromWarehouse (
            Set<SemanticPossibility> possibilities, UserRemoteLocationInfo userRemoteLocationInfo, Long userQueryId) {
      return new ArrayList<UniversalUnstructuredSearchResult>(1);
   }

   protected Integer getUniversalSearchMatchLimit () {
      return getCoreConfigurationService().getUniversalSearchMatchQueryLimit();
   }

   public List<UniversalUnstructuredSearchResult> performNewsSearch (Set<SemanticPossibility> possibilities,
            Long userQueryId) throws KnowledgeSearchException, USSException {
      Map<Long, Set<Long>> bedsFromKnowledgeSearchByAppId = getKnowledgeSearchEngine().searchRelatedInstances(
               possibilities);
      UniversalUnstructuredSearchResult universalSearchResult = getEntitySearchEngine().universalSearch(
               bedsFromKnowledgeSearchByAppId, userQueryId, 1);

      List<UniversalUnstructuredSearchResult> results = new ArrayList<UniversalUnstructuredSearchResult>(1);
      results.add(universalSearchResult);
      return results;
   }

   @SuppressWarnings ("unchecked")
   public List<PossibilityResult> getResultsByPage (int pageNum, int pageSize, QueryResult queryResult) {
      int toBeFilled = pageSize;
      List<PossibilityResult> source = queryResult.getPossibilites();
      List<PossibilityResult> results = new ArrayList<PossibilityResult>();

      int startIndex = (pageNum - 1) * pageSize + 1;
      int endIndex = pageNum * pageSize;

      for (PossibilityResult result : source) {
         int checkIndex = checkPossibilityForPageResults(startIndex, queryResult, result);
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
      if (!ExecueCoreUtil.isCollectionEmpty(list)) {
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

   private List<PossibleAssetInfo> getQueryCachePossibleAssetInfo (Long possiblityId, Long existingUserQueryId,
            Long modelId, Long applicationId, Double matchQCPercent) throws QueryCacheException {
      long startTime = System.currentTimeMillis();
      List<QueryCacheResultInfo> queryCacheResults = getQueryCacheService().getQueryCacheResults(existingUserQueryId,
               applicationId);
      List<PossibleAssetInfo> cachePossibleAssetInfos = new ArrayList<PossibleAssetInfo>();
      if (!ExecueCoreUtil.isCollectionEmpty(queryCacheResults)) {
         for (QueryCacheResultInfo queryCacheResultInfo : queryCacheResults) {
            PossibleAssetInfo possibleAssetInfo = new PossibleAssetInfo();
            possibleAssetInfo.setPossibilityId(possiblityId);
            possibleAssetInfo.setModelId(modelId);
            possibleAssetInfo.setFromQueryCache(true);
            possibleAssetInfo.setAssetId(queryCacheResultInfo.getAssetId());
            possibleAssetInfo.setBusinessQueryId(queryCacheResultInfo.getBusinessQueryId());
            possibleAssetInfo.setUserQueryId(queryCacheResultInfo.getUserQueryId());
            Double matchedAssetWeight = queryCacheResultInfo.getAssetWeight();
            Double relevantMatchedAssetWeight = matchedAssetWeight * matchQCPercent;
            possibleAssetInfo.setTotalTypeBasedWeight(relevantMatchedAssetWeight);
            possibleAssetInfo.setCachedDate(queryCacheResultInfo.getCachedDate());
            cachePossibleAssetInfos.add(possibleAssetInfo);
         }
      }
      long endTime = System.currentTimeMillis();
      if (logger.isInfoEnabled()) {
         logger.info("Time taken to get the PossibleAssetInfo From Query Cache " + (endTime - startTime) / 1000.0
                  + " seconds");
      }
      return cachePossibleAssetInfos;
   }

   private void processPossibilities (Set<SemanticPossibility> possibilities,
            Map<Long, List<UniversalSearchResult>> queryCacheMatchedResultsByAppId,
            Map<Long, UniversalUnstructuredSearchResult> unstructuredSearchResultByAppIdMap,
            Map<Long, QDataReducedQuery> reducedQueryByAppId, QueryResult queryResult, Date userQueryExecutionDate,
            boolean disableQueryCache, double baseUserQueryWeight, String userQueryString) throws KDXException,
            ExeCueException {

      Map<Long, SemanticPossibility> possibilityMap = new LinkedHashMap<Long, SemanticPossibility>(1);
      Map<Long, BusinessQuery> businessQueryMap = new LinkedHashMap<Long, BusinessQuery>(1);
      Map<Long, Set<BusinessEntityTerm>> businessEntityTermsMap = new HashMap<Long, Set<BusinessEntityTerm>>(1);
      Map<Long, List<PossibleAssetInfo>> queryCachePossibleAssetInfosByPossibilityId = new HashMap<Long, List<PossibleAssetInfo>>(
               1);
      List<UserQueryPossibility> userQueryPossibilities = new ArrayList<UserQueryPossibility>(1);
      List<SemanticPossibility> unstructuredSemanticPossibilities = new ArrayList<SemanticPossibility>(1);
      boolean queryCacheMatch = getQueryCacheConfiguration().getConfiguration().getBoolean(
               QueryCacheConstants.CHECK_MATCH_FLAG);

      long startST = System.currentTimeMillis();
      long endST = System.currentTimeMillis();
      // Get the user query possibility for the unique set of business entity terms of each possibility
      for (SemanticPossibility possibility : possibilities) {
         try {
            long startTime = System.currentTimeMillis();
            List<BusinessQuery> bqList = getReducedFormBQGenerator().generateBQs(possibility);
            if (bqList.size() <= 0) {
               continue;
            }
            // TODO as of now get the first from the list need to do it in a proper way
            BusinessQuery businessQuery = null;
            for (BusinessQuery bQuery : bqList) {
               if (bQuery.getMetrics() != null) {
                  businessQuery = bQuery;
                  break;
               }
            }

            // Update the maps
            possibilityMap.put(possibility.getId(), possibility);
            businessQueryMap.put(possibility.getId(), businessQuery);

            if (AppSourceType.UNSTRUCTURED == possibility.getApplication().getSourceType()) {
               unstructuredSemanticPossibilities.add(possibility);
               continue;
            }
            getBusinessQueryOrganizationService().organizeBusinessQuery(businessQuery);
            long endTime = System.currentTimeMillis();
            if (logger.isInfoEnabled()) {
               logger.info("Time taken to generate and organize Business Query " + (endTime - startTime) / 1000.0
                        + " seconds");
            }

            // get the set of business entity terms
            Set<BusinessEntityTerm> businessEntityTerms = governorService.populateBusinessEntityTerms(businessQuery);
            businessEntityTermsMap.put(possibility.getId(), businessEntityTerms);

            Set<BusinessEntityTerm> userRequestedBusinessEntityTerms = getUserRequestedBusinessEntityTerms(businessEntityTerms);

            userQueryPossibilities.addAll(getUserQueryPossibilties(userRequestedBusinessEntityTerms, businessQuery,
                     queryResult.getId(), possibility.getId(), userQueryExecutionDate));
            Long applicationId = possibility.getApplication().getId();

            if (queryResult.getVerticalEntityBasedRedirection() != null
                     && applicationId.equals(queryResult.getVerticalEntityBasedRedirection().getApplicationId())) {
               // Validate and populate configured vertical based redirection details
               populateVerticalBasedRedirectInformation(queryResult, userRequestedBusinessEntityTerms);
            }

            if (queryResult.getEntityBasedRedirection() == null) {
               List<VerticalEntityBasedRedirection> verticalEntityBasedRedirectionList = getDriverConfigurationService()
                        .getAppBusinessEntityIdMap(applicationId);
               if (!ExecueCoreUtil.isCollectionEmpty(verticalEntityBasedRedirectionList)) {
                  List<BusinessCondition> conditions = businessQuery.getConditions();
                  if (!ExecueCoreUtil.isCollectionEmpty(conditions)) {
                     queryResult.setEntityBasedRedirection(populateTkrName(conditions,
                              verticalEntityBasedRedirectionList));
                  }
               }
            }

            // Populate the query cached based information........
            List<PossibleAssetInfo> queryCachePossibleAssetInfos = getQueryCacheBasedPossibleAssetInfos(possibility,
                     queryCacheMatchedResultsByAppId, reducedQueryByAppId, queryCacheMatch, disableQueryCache);

            if (!CollectionUtils.isEmpty(queryCachePossibleAssetInfos)) {
               queryCachePossibleAssetInfosByPossibilityId.put(possibility.getId(), queryCachePossibleAssetInfos);
            }

         } catch (Exception exception) {
            handleProcessPossibilitiesExceptions(userQueryString, possibility, exception,
                     ExeCueExceptionCodes.DRIVER_PROCESS_POSSIBILITIES_PRIOR_GOVERNOR_UNHANDLED_EXCPETION);
            continue;
         }
      }

      endST = System.currentTimeMillis();
      if (logger.isInfoEnabled()) {
         logger.info("Time taken to prepare UQ Possibilities " + (endST - startST) / 1000.0 + " seconds");
      }

      startST = System.currentTimeMillis();
      // Now populate the User_Query_Possibiliy table
      getUserQueryPossibilityService().createUserQueryPossibilities(userQueryPossibilities);
      endST = System.currentTimeMillis();
      if (logger.isInfoEnabled()) {
         logger.info("Time taken to create UQ Possibilities " + (endST - startST) / 1000.0 + " seconds");
      }

      startST = System.currentTimeMillis();
      // Get the possible asset info
      List<PossibleAssetInfo> possibleAssetInfos = getUserQueryPossibilityService().getPossibleAssetsForUserRequest(
               queryResult.getId());

      // Get all the query cache possible asset infos
      List<PossibleAssetInfo> queryCachePossibleAssetInfoAcrossPossiblities = ExecueCoreUtil
               .mergeCollectionAsList(queryCachePossibleAssetInfosByPossibilityId.values());

      if (!ExecueCoreUtil.isCollectionEmpty(queryCachePossibleAssetInfoAcrossPossiblities)) {
         possibleAssetInfos.addAll(queryCachePossibleAssetInfoAcrossPossiblities);
      }

      Map<Long, List<PossibleAssetInfo>> possibleAssetInfoForPossibilityMap = new HashMap<Long, List<PossibleAssetInfo>>(
               1);
      // Don't Process governor if possible asset infos are empty.
      if (!CollectionUtils.isEmpty(possibleAssetInfos)) {
         possibleAssetInfoForPossibilityMap = applyAssetLevelFilters(businessQueryMap, possibleAssetInfos);

         populateStandardizedTotalTypeBasedWeight(possibilityMap, businessQueryMap, possibleAssetInfoForPossibilityMap);

      }

      // NOTE: Assumption here is adding the unstructured possible asset always at the end of above federations
      // Add the unstructured possible asset info to the possibleAssetForPossibilityMap
      // Get the possible asset info for unstructured app
      Map<Long, List<PossibleAssetInfo>> unstructuredAppPossibleAssetInfoMap = getPossibleAssetInfoMapForUnstructureApp(
               unstructuredSemanticPossibilities, unstructuredSearchResultByAppIdMap);

      addUnsturturedPossibleAssetInfo(possibleAssetInfoForPossibilityMap, unstructuredAppPossibleAssetInfoMap);

      // If empty, then notify the user
      if (CollectionUtils.isEmpty(possibleAssetInfoForPossibilityMap.values())) {
         if (logger.isDebugEnabled()) {
            logger.debug("Possible asset info list is empty, Please set the primary flag in the mappings table");
         }
         throw new ExeCueException(GovernorExceptionCodes.ANSWERABLE_ASSET_LIST_EMPTY,
                  "Possible asset info list is empty, Please set the primary flag in the mappings table");
      }

      // TODO: -RG- Remove later - Added for testing
      if (logger.isInfoEnabled()) {
         logger.info("Possible Assets by Possibility after federation by Top Cluster, ");
         SemanticDriverPrintHelper.printPossibleAssetInfo(possibleAssetInfoForPossibilityMap);
      }

      // assign relevance Weight To each possibleAssetInfo Object.
      assignRelevanceWeights(possibleAssetInfoForPossibilityMap, baseUserQueryWeight);

      // sort possibilities based on relevance
      possibleAssetInfoForPossibilityMap = sortAssetInfoMapBasedOnrelevance(possibleAssetInfoForPossibilityMap,
               possibilityMap);

      // Sorting of possibilities based on search type handling - Start
      if (getQueryCacheConfiguration().getConfiguration().getBoolean(QueryCacheConstants.VERTICAL_BASED_SORTING_FLAG)) {
         if (queryResult.getSearchFilter() != null
                  && !SearchFilterType.GENERAL.equals(queryResult.getSearchFilter().getSearchFilterType())) {
            List<String> scopedAppNames = new ArrayList<String>();
            if (SearchFilterType.VERTICAL.equals(queryResult.getSearchFilter().getSearchFilterType())) {
               scopedAppNames.addAll(getApplicationRetrievalService().getApplicationNamesForVertical(
                        getKdxRetrievalService().getVerticalById(queryResult.getSearchFilter().getId()).getName()));
            } else {
               scopedAppNames.add(getKdxRetrievalService().getVerticalById(queryResult.getSearchFilter().getId())
                        .getName());
            }
            getDriverHelper().correctSearchScoping(possibilities, scopedAppNames);
            // Search Filter based sorting of possibleAssetForPossibilityMap
            possibleAssetInfoForPossibilityMap = getDriverHelper().sortAssetInfoMapBasedOnSearchScope(
                     possibleAssetInfoForPossibilityMap, possibilityMap);
         }
      }
      // Sorting of possibilities based on search type handling - End

      // Scope the map to the page size requested
      Set<Long> showMoreLinkPossibilityIds = new HashSet<Long>(1);
      Map<Long, List<PossibleAssetInfo>> scopedAssetsPerPossibilityMap = getScopedAssetsPerPossibilityForRequestedPage(
               possibleAssetInfoForPossibilityMap, queryResult, showMoreLinkPossibilityIds);

      // TODO: -RG- Remove later - Added for testing
      if (logger.isInfoEnabled()) {
         logger.info("Possible Assets by Possibility after scoped to requested page, ");
         SemanticDriverPrintHelper.printPossibleAssetInfo(scopedAssetsPerPossibilityMap);
      }

      // Prepare the semantic possibility map against the possibility id for all the input possibilities
      endST = System.currentTimeMillis();
      if (logger.isInfoEnabled()) {
         logger.info("time taken to federate possible assets by weight per possibility " + (endST - startST) / 1000.0
                  + " seconds");
      }

      for (Entry<Long, List<PossibleAssetInfo>> entry : scopedAssetsPerPossibilityMap.entrySet()) {
         Long possibilityId = entry.getKey();
         List<PossibleAssetInfo> possibleAssetsInfo = entry.getValue();
         SemanticPossibility possibility = possibilityMap.get(possibilityId);
         BusinessQuery businessQuery = businessQueryMap.get(possibilityId);
         if (logger.isInfoEnabled()) {
            logger.info("\nReduced Form Being Processed (" + possibility.getId() + ")  ["
                     + possibility.getWeightInformationForExplicitEntities().getFinalWeight() + " - "
                     + businessQuery.getMaxPossibleBQWeight() + "],\n" + possibility.getDisplayString());
         }
         try {
            // Retrieve the color coded request
            String colorCodedRequest = pseudoLanguageService.getColorCodedPseudoLanguageStatement(possibility
                     .getQueryWords(), possibility.getWordRecognitionStates());

            Application application = possibility.getApplication();

            // Set the application related stuff
            businessQuery.setApplicationName(application.getName());
            businessQuery.setApplicationId(application.getId());
            businessQuery.setAppSourceType(application.getSourceType());
            businessQuery.setRequestRecognition(colorCodedRequest);

            // Set the semantic possiblity related stuff
            businessQuery.setScoped(possibility.isScoped());
            businessQuery.setStandarizedApplicationWeight(possibility.getStandarizedApplicationWeight());
            businessQuery.setStandarizedPossiblityWeight(possibility.getStandarizedPossiblityWeight());

            // call BQ processing
            long startTime = System.currentTimeMillis();
            processBusinessQuery2(businessQuery, queryResult, possibleAssetsInfo, businessEntityTermsMap
                     .get(possibilityId));
            long endTime = System.currentTimeMillis();
            if (logger.isInfoEnabled()) {
               logger.info("Total time taken to process businessQuery [" + possibility.getId() + "] "
                        + (endTime - startTime) / 1000.0 + " seconds");
            }

            // Update the unstructured app info in the possibility result
            if (application.getSourceType() == AppSourceType.UNSTRUCTURED) {
               // NOTE: NK: Assumption here is the possibility result got added in the last of the list in the
               // processBusinessQuery2 call
               PossibilityResult possibilityResult = queryResult.getPossibilites().get(
                        queryResult.getPossibilites().size() - 1);
               if (possibilityResult.getApplicationType() == AppSourceType.UNSTRUCTURED) {
                  updateUnstructuredPossibilityResult(unstructuredSearchResultByAppIdMap, possibleAssetsInfo,
                           application, possibilityResult);
               }
            }
            // add color coding by calling the PLService
            List<PossibilityResult> pResultList = queryResult.getPossibilites();
            PossibilityResult pResult = pResultList.get(pResultList.size() - 1);
            if (showMoreLinkPossibilityIds.contains(possibility.getId())) {
               pResult.setShowMore(CheckType.YES);
            }
            pResult.setColorCodedRequest(colorCodedRequest);
         } catch (Exception exception) {
            handleProcessPossibilitiesExceptions(userQueryString, possibility, exception,
                     ExeCueExceptionCodes.DRIVER_PROCESS_POSSIBILITIES_GOVERNOR_HANDLED_EXCPETION);
            continue;
         }
      }

      // If its first page, we are displaying the unstructured results on top irrespective of weight
      if (queryResult.getRequestedPage() == 1) {
         getUnstructuredResultsOnTop(queryResult);
      }
   }

   private List<PossibleAssetInfo> getQueryCacheBasedPossibleAssetInfos (SemanticPossibility possibility,
            Map<Long, List<UniversalSearchResult>> queryCacheMatchedResultsByAppId,
            Map<Long, QDataReducedQuery> reducedQueryByAppId, boolean queryCacheMatch, boolean disableQueryCache)
            throws QueryCacheException, QueryDataException {
      List<PossibleAssetInfo> queryCachePossibleAssetInfos = new ArrayList<PossibleAssetInfo>(1);
      // Return, if system query cache flag is set to false or if the user is disabling the query cache in the request
      // refresh
      if (!queryCacheMatch || disableQueryCache) {
         return queryCachePossibleAssetInfos;
      }

      // Process the query cache...
      // Get the user query matched results for the current application
      Long applicationId = possibility.getApplication().getId();
      List<UniversalSearchResult> matchedResults = queryCacheMatchedResultsByAppId.get(applicationId);
      boolean isQueryCachePossible = checkIfQueryCacheProcessingIsPossible(matchedResults);

      // If the current query can be retrieved from cache, then process query cache
      if (isQueryCachePossible) {
         queryCachePossibleAssetInfos = processQueryCache(possibility, matchedResults, reducedQueryByAppId
                  .get(applicationId));

      }

      return queryCachePossibleAssetInfos;
   }

   private void getUnstructuredResultsOnTop (QueryResult queryResult) {
      List<PossibilityResult> readjustedResults = new ArrayList<PossibilityResult>(1);
      List<PossibilityResult> structuredResults = new ArrayList<PossibilityResult>(1);
      List<PossibilityResult> possibilites = queryResult.getPossibilites();
      for (PossibilityResult possibilityResult : possibilites) {
         if (possibilityResult.getApplicationType() == AppSourceType.UNSTRUCTURED) {
            readjustedResults.add(possibilityResult);
         } else {
            structuredResults.add(possibilityResult);
         }
      }
      readjustedResults.addAll(structuredResults);
      queryResult.setPossibilites(readjustedResults);
   }

   /**
    * @param possibilityMap
    * @param businessQueryMap
    * @param possibleAssetForPossibilityMap
    */
   private void populateStandardizedTotalTypeBasedWeight (Map<Long, SemanticPossibility> possibilityMap,
            Map<Long, BusinessQuery> businessQueryMap, Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap) {
      if (MapUtils.isEmpty(possibleAssetForPossibilityMap)) {
         return;
      }
      // Calculate the adjusted weight for each asset
      for (Entry<Long, List<PossibleAssetInfo>> entry : possibleAssetForPossibilityMap.entrySet()) {
         Long possibilityId = entry.getKey();
         BusinessQuery businessQuery = businessQueryMap.get(possibilityId);
         SemanticPossibility semanticPossibility = possibilityMap.get(possibilityId);
         List<PossibleAssetInfo> possibleAssetsInfo = entry.getValue();
         double possibilityWeight = semanticPossibility.getWeightInformationForExplicitEntities().getFinalWeight();
         double maxPossibleBQWeight = businessQuery.getMaxPossibleBQWeight();
         setStandardizedTotalTypeBasedWeight(possibleAssetsInfo, possibilityWeight, maxPossibleBQWeight);
      }
   }

   /**
    * @param unstructuredSearchResultByAppIdMap
    * @param possibleAssetsInfo
    * @param application
    * @param possibilityResult
    */
   private void updateUnstructuredPossibilityResult (
            Map<Long, UniversalUnstructuredSearchResult> unstructuredSearchResultByAppIdMap,
            List<PossibleAssetInfo> possibleAssetsInfo, Application application, PossibilityResult possibilityResult) {
      // Set the asset result into the Possibility Result object
      populateUnstructuredAssetResults(possibilityResult, possibleAssetsInfo);

      // Set the application related info into the Possibility Result object
      possibilityResult.setAppId(application.getId());
      possibilityResult.setAppName(application.getName());
      possibilityResult.setAppUrl(application.getApplicationURL());
      possibilityResult.setAppImageId(application.getImageId());
      possibilityResult.setApplicationType(application.getSourceType());

      // Set the universal search related info into the Possibility Result object
      UniversalUnstructuredSearchResult universalUnstructuredSearchResult = unstructuredSearchResultByAppIdMap
               .get(application.getId());
      if (universalUnstructuredSearchResult != null) {
         possibilityResult.setTotalCount(universalUnstructuredSearchResult.getTotalCount());
         possibilityResult.setPerfectMatchCount(universalUnstructuredSearchResult.getPerfectMatchCount());
         possibilityResult.setMightMatchCount(universalUnstructuredSearchResult.getMightMatchCount());
         possibilityResult.setPartialMatchCount(universalUnstructuredSearchResult.getPartialMatchCount());
      }
   }

   private void populateUnstructuredAssetResults (PossibilityResult possibilityResult,
            List<PossibleAssetInfo> possibleAssetsInfo) {
      List<AssetResult> assets = new ArrayList<AssetResult>(1);
      possibilityResult.setAssets(assets);
      if (CollectionUtils.isEmpty(possibleAssetsInfo)) {
         return;
      }
      for (PossibleAssetInfo possibleAssetInfo : possibleAssetsInfo) {
         UnstructuredPossibleAssetInfo unstructuredPossibleAssetInfo = (UnstructuredPossibleAssetInfo) possibleAssetInfo;
         AssetResult assetResult = new AssetResult();
         assetResult.setAssetId(unstructuredPossibleAssetInfo.getAssetId());
         assetResult.setBusinessQueryId(unstructuredPossibleAssetInfo.getBusinessQueryId());
         assetResult.setReportStatus(MessageStatusType.COMPLETED.getValue());
         assetResult.setRelevance(Double.parseDouble(String.format("%.2f", possibleAssetInfo.getRelavance())));
         assetResult.setResultItemType(unstructuredPossibleAssetInfo.getResultItemType());
         assetResult.setReportHeader(getUpdatedShortDescription(unstructuredPossibleAssetInfo.getShortDescription()));
         assetResult.setHeaderUrl(unstructuredPossibleAssetInfo.getUrl());
         assetResult.setImageUrl(unstructuredPossibleAssetInfo.getImageUrl());
         assetResult.setUnstructuredContentDate(getDriverHelper().formatUnstructuredContentDate(
                  unstructuredPossibleAssetInfo.getContentDate()));
         assetResult.setDataPresent(true);
         assets.add(assetResult);
      }
   }

   private Map<Long, AppSourceType> getPossibilityTypeMap (
            Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap) {
      Map<Long, AppSourceType> possibilityType = new HashMap<Long, AppSourceType>(1);
      for (Entry<Long, List<PossibleAssetInfo>> entry : possibleAssetForPossibilityMap.entrySet()) {
         possibilityType.put(entry.getKey(), entry.getValue().get(0).getApplicationType());
      }
      return possibilityType;
   }

   private VerticalEntityBasedRedirection populateTkrName (List<BusinessCondition> businessConditions,
            List<VerticalEntityBasedRedirection> verticalEntityBasedRedirectionList) {
      VerticalEntityBasedRedirection entityBasedRedirection = null;
      for (BusinessCondition businessCondition : businessConditions) {
         entityBasedRedirection = getMatchingVerticalEntityBasedRedirection(businessCondition,
                  verticalEntityBasedRedirectionList);
         if (entityBasedRedirection != null) {
            List<BusinessTerm> rhsBusinessTerms = businessCondition.getRhsBusinessTerms();
            if (!ExecueCoreUtil.isCollectionEmpty(rhsBusinessTerms)) {
               BusinessTerm businessTerm = rhsBusinessTerms.get(0);
               Instance instance = (Instance) businessTerm.getBusinessEntityTerm().getBusinessEntity();
               entityBasedRedirection.setTkrName(instance.getAbbreviatedName());
            }
            if (entityBasedRedirection.getTkrName() != null) {
               break;
            }
         }
      }
      return entityBasedRedirection;
   }

   private VerticalEntityBasedRedirection getMatchingVerticalEntityBasedRedirection (
            BusinessCondition businessCondition, List<VerticalEntityBasedRedirection> verticalEntityBasedRedirectionList) {
      VerticalEntityBasedRedirection entityBasedRedirection = null;
      for (VerticalEntityBasedRedirection verticalEntityBasedRedirection : verticalEntityBasedRedirectionList) {
         if (businessCondition.getLhsBusinessTerm().getBusinessEntityTerm().getBusinessEntityDefinitionId().longValue() == verticalEntityBasedRedirection
                  .getBusinessEntityId().longValue()) {
            entityBasedRedirection = verticalEntityBasedRedirection;
            break;

         }
      }
      return entityBasedRedirection;
   }

   private Map<Long, List<PossibleAssetInfo>> getPossibleAssetInfoMapForUnstructureApp (
            List<SemanticPossibility> unstructuredSemanticPossibilities,
            Map<Long, UniversalUnstructuredSearchResult> unstructuredSearchResultByAppIdMap) {
      Map<Long, List<PossibleAssetInfo>> possibleAssetInfoMap = new HashMap<Long, List<PossibleAssetInfo>>(1);
      for (SemanticPossibility semanticPossibility : unstructuredSemanticPossibilities) {

         // Get the user query matched results for the current application
         Application application = semanticPossibility.getApplication();
         Long applicationId = application.getId();
         UniversalUnstructuredSearchResult matchedResults = unstructuredSearchResultByAppIdMap.get(applicationId);

         if (matchedResults == null || CollectionUtils.isEmpty(matchedResults.getUnstructuredSearchResultItem())) {
            continue;
         }

         List<PossibleAssetInfo> dummyPossibleAssetInfo = getPossibleAssetInfoForUnstructuredApp(semanticPossibility,
                  matchedResults.getUnstructuredSearchResultItem());
         possibleAssetInfoMap.put(semanticPossibility.getId(), dummyPossibleAssetInfo);
      }
      return possibleAssetInfoMap;
   }

   private List<PossibleAssetInfo> getPossibleAssetInfoForUnstructuredApp (SemanticPossibility semanticPossibility,
            List<UnstructuredSearchResultItem> unstructuredSearchResultItems) {
      List<PossibleAssetInfo> possibleAssetInfos = new ArrayList<PossibleAssetInfo>(1);

      for (UnstructuredSearchResultItem universalSearchResult : unstructuredSearchResultItems) {
         Double matchWeight = universalSearchResult.getWeight(); // universal search match weight. eg: 90 percent
         // match out of 100
         UnstructuredPossibleAssetInfo possibleAssetInfo = new UnstructuredPossibleAssetInfo();
         // Assumption here is No Assets for unstructured App, so storing the result item id i.e. semantified content id
         possibleAssetInfo.setAssetId(universalSearchResult.getId());
         possibleAssetInfo.setContentDate(universalSearchResult.getContentDate());
         possibleAssetInfo.setUrl(universalSearchResult.getUrl());
         possibleAssetInfo.setImageUrl(universalSearchResult.getImageUrl());
         possibleAssetInfo.setShortDescription(universalSearchResult.getShortDescription());
         possibleAssetInfo.setResultItemType(universalSearchResult.getResultItemType());
         possibleAssetInfo.setApplicationType(AppSourceType.UNSTRUCTURED);
         possibleAssetInfo.setModelId(semanticPossibility.getModel().getId());
         possibleAssetInfo.setPossibilityId(semanticPossibility.getId());
         // since unstructured possibility asset info setting the match weight
         possibleAssetInfo.setTotalTypeBasedWeight(matchWeight);
         // // since unstructured possibility setting the user query max weight
         // semanticPossibility.setMaxPossibleBQWeight(100d);
         possibleAssetInfo.setResultItemType(universalSearchResult.getResultItemType());

         // set the standardized weight for unstructured possibility asset info
         double standarizedTotalTypeBasedWeight = semanticPossibility.getWeightInformationForExplicitEntities()
                  .getFinalWeight()
                  * matchWeight / 100d;
         possibleAssetInfo.setStandarizedTotalTypeBasedWeight(standarizedTotalTypeBasedWeight);
         possibleAssetInfos.add(possibleAssetInfo);
      }
      return possibleAssetInfos;
   }

   private void addUnsturturedPossibleAssetInfo (Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap,
            Map<Long, List<PossibleAssetInfo>> unstructuredAppPossibleAssetInfoMap) {
      possibleAssetForPossibilityMap.putAll(unstructuredAppPossibleAssetInfoMap);

   }

   private Map<Long, List<UniversalSearchResult>> getUniversalSearchMatchedResultsByAppId (
            List<UniversalSearchResult> unstructuredSearchMatchedResults,
            Map<Long, QDataReducedQuery> reducedQueryByAppId) {
      Map<Long, List<UniversalSearchResult>> universalSearchResultsByAppId = new HashMap<Long, List<UniversalSearchResult>>(
               1);
      if (CollectionUtils.isEmpty(unstructuredSearchMatchedResults)) {
         return universalSearchResultsByAppId;
      }
      for (UniversalSearchResult matchedResult : unstructuredSearchMatchedResults) {
         Long applicationId = matchedResult.getApplicationId();
         if (isUniversalSearchFromWarehouse()) {
            matchedResult.setUserQueryMaxWeigth(100d);
         } else {
            QDataReducedQuery dataReducedQuery = reducedQueryByAppId.get(applicationId);
            // Also set the max weight for user query for the given app
            if (dataReducedQuery != null) {
               matchedResult.setUserQueryMaxWeigth(dataReducedQuery.getMaxMatchWeight());
            }
         }
         List<UniversalSearchResult> appMatchedResults = universalSearchResultsByAppId.get(applicationId);
         if (appMatchedResults == null) {
            appMatchedResults = new ArrayList<UniversalSearchResult>(1);
            universalSearchResultsByAppId.put(applicationId, appMatchedResults);
         }
         appMatchedResults.add(matchedResult);
      }
      return universalSearchResultsByAppId;
   }

   /**
    * @param possibility
    * @param matchedResults
    * @param qdataReducedQuery
    * @return
    * @throws QueryCacheException
    * @throws QueryDataException
    */
   private List<PossibleAssetInfo> processQueryCache (SemanticPossibility possibility,
            List<UniversalSearchResult> matchedResults, QDataReducedQuery qdataReducedQuery)
            throws QueryCacheException, QueryDataException {

      List<PossibleAssetInfo> queryCachePossibleAssetInfo = new ArrayList<PossibleAssetInfo>(1);
      if (CollectionUtils.isEmpty(matchedResults)) {
         return queryCachePossibleAssetInfo;
      }
      // TODO: NK: Once we have the correct weight coming in the matchedResults, then need to consider all the best
      // matches instead of only first??
      UniversalSearchResult firstMatchedResult = matchedResults.get(0);
      Long matchedUserQueryId = firstMatchedResult.getUserQueryId();
      Double matchedQCResultWeight = firstMatchedResult.getMatchWeight();
      Double maxQueryWeight = qdataReducedQuery.getMaxMatchWeight();
      Double matchedQCPercent = matchedQCResultWeight / maxQueryWeight;
      queryCachePossibleAssetInfo = getQueryCachePossibleAssetInfo(possibility.getId(), matchedUserQueryId, possibility
               .getModel().getId(), qdataReducedQuery.getApplicationId(), matchedQCPercent);

      if (logger.isInfoEnabled()) {
         logger.info("\nUser Query Id: " + matchedUserQueryId + "\nUser Query processed from query cache: \n"
                  + qdataReducedQuery.getReducedQueryString());
      }

      return queryCachePossibleAssetInfo;
   }

   /**
    * 1. Returns false, If system query cache flag is set to false. 2. Returns false, If the user is disabling the query
    * cache in the request refresh. Else if both are true, then we check if the matched result weight is atleast the
    * configured weight threshold
    * 
    * @param matchedResults
    * @param disableQueryCache
    * @return
    */
   private boolean checkIfQueryCacheProcessingIsPossible (List<UniversalSearchResult> matchedResults) {

      if (CollectionUtils.isEmpty(matchedResults)) {
         return false;
      }

      // As per universal search algorithm, considering the first one as best match and processing the
      // userQueryMatchedQCWeight of it to validate with the configured QC weight threshold.
      UniversalSearchResult universalSearchResult = matchedResults.get(0);
      Double userQueryMatchedQCWeight = universalSearchResult.getMatchWeight()
               / universalSearchResult.getUserQueryMaxWeigth() * 100;
      int queryCacheWeightThreshold = getQueryCacheConfiguration().getConfiguration().getInt(
               QueryCacheConstants.QUERY_CACHE_MATCH_RESULT_WEIGHT);

      return userQueryMatchedQCWeight >= queryCacheWeightThreshold;
   }

   private void populateVerticalBasedRedirectInformation (QueryResult queryResult,
            Set<BusinessEntityTerm> businessEntityTerms) {
      List<Long> verticalEntityBasedRedirectionBEDIds = new ArrayList<Long>();
      boolean onlyConfiguredEntitiesExists = true;
      for (BusinessEntityTerm bet : businessEntityTerms) {
         if (BusinessEntityType.CONCEPT.equals(bet.getBusinessEntityType())) {
            if (!queryResult.getVerticalEntityBasedRedirection().getBusinessEntityId().equals(
                     bet.getBusinessEntityDefinitionId())) {
               onlyConfiguredEntitiesExists = false;
               break;
            }
         }
      }
      for (BusinessEntityTerm businessEntityTerm : businessEntityTerms) {
         if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(businessEntityTerm.getBusinessEntityType())) {
            if (queryResult.getVerticalEntityBasedRedirection().getEntityId().equals(
                     ((Instance) businessEntityTerm.getBusinessEntity()).getParentConcept().getId())) {
               verticalEntityBasedRedirectionBEDIds.add(businessEntityTerm.getBusinessEntityDefinitionId());
            }
         }
      }
      if (!ExecueCoreUtil.isCollectionEmpty(verticalEntityBasedRedirectionBEDIds)) {
         queryResult.setVerticalEntityBasedRedirectionBEDIds(verticalEntityBasedRedirectionBEDIds);
      }
      queryResult.setOnlyConfiguredEntitiesExists(onlyConfiguredEntitiesExists);
   }

   /**
    * Sort the possibilityAssetInfo Map based on the Relevance weights. Possibility With highest relevance for a asset
    * will be displayed on the Top.
    * 
    * @param possibleAssetForPossibilityMap
    * @return
    */
   private Map<Long, List<PossibleAssetInfo>> sortAssetInfoMapBasedOnrelevance (
            Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap, Map<Long, SemanticPossibility> possMap) {
      PossibleAssetComparator possibleAssetComparator = new PossibleAssetComparator();
      possibleAssetComparator.setPossibilityMap(possMap);
      possibleAssetComparator.setPossibleAssetForPossibilityMap(possibleAssetForPossibilityMap);
      Map<Long, List<PossibleAssetInfo>> sortedPossibleAssetInfoMap = new TreeMap<Long, List<PossibleAssetInfo>>(
               possibleAssetComparator);
      sortedPossibleAssetInfoMap.putAll(possibleAssetForPossibilityMap);
      return sortedPossibleAssetInfoMap;

   }

   /**
    * Method to assign the relevance to each possibilityAssetInfo object based on the base user query weight.
    * 
    * @param possibleAssetForPossibilityMap
    * @param baseUserQueryWeight
    */
   private void assignRelevanceWeights (Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap,
            double baseUserQueryWeight) {
      for (Entry<Long, List<PossibleAssetInfo>> entry : possibleAssetForPossibilityMap.entrySet()) {
         setAssetRelevance(entry.getValue(), baseUserQueryWeight);
      }
   }

   private Map<Long, List<PossibleAssetInfo>> getScopedAssetsPerPossibilityForRequestedPage (
            Map<Long, List<PossibleAssetInfo>> possibleAssetForPossibilityMap, QueryResult queryResult,
            Set<Long> showMoreLinkPossibilityIds) {
      Map<Long, List<PossibleAssetInfo>> scopedAssetsPerPossibilityMap = new LinkedHashMap<Long, List<PossibleAssetInfo>>();
      Map<Long, AppSourceType> possibilityType = getPossibilityTypeMap(possibleAssetForPossibilityMap);
      Set<Long> possibilityIds = possibleAssetForPossibilityMap.keySet();
      final int unstructuredAppGroupingResultCount = getUnstructuredAppGroupingResultCount();
      int resultCount = 0;
      for (Long posId : possibilityIds) {
         int assetInfoSize = possibleAssetForPossibilityMap.get(posId).size();
         if (AppSourceType.UNSTRUCTURED == possibilityType.get(posId)) {
            if (assetInfoSize >= queryResult.getPageSize()) {
               showMoreLinkPossibilityIds.add(posId);
               // Using math.ceil to consider zero to unstructuredAppGroupingResultCount(i.e 3) as 1. Since both the
               // numerator and denominators being INTs was
               // becoming 0 instead of equivalent to showing 1 result
               resultCount += Math.ceil((double) queryResult.getPageSize() / unstructuredAppGroupingResultCount);
            } else {
               resultCount += Math.ceil((double) assetInfoSize / unstructuredAppGroupingResultCount);
            }
         } else {
            resultCount += assetInfoSize;
         }
      }
      queryResult.setPageCount(resultCount);

      int requestedPageNumber = queryResult.getRequestedPage();
      int pageSize = queryResult.getPageSize();

      int endIndex = requestedPageNumber * pageSize - 1;
      // TODO --AP-- review boundary conditions
      // Only one result makes result count = 1 and hence end-result = 0 hence we do not iterate
      if (endIndex > resultCount - 1 && resultCount != 1) {
         endIndex = resultCount - 1;
      }
      int startIndex = 0;
      if (requestedPageNumber > 1) {
         startIndex = (requestedPageNumber - 1) * pageSize;
      }

      List<PossibleAssetInfo> tempPossibleAssetInfos;
      List<PossibleAssetInfo> possibleAssetInfos;
      int tempPossibleAssetInfosSize = 0;
      int requiredIndex = startIndex;
      int currentPossibleAssetInfoIndex = 0;
      boolean pageFilled = false;
      if (logger.isDebugEnabled()) {
         logger.debug("Total Possibilities: " + possibilityIds.size());
         logger.debug("Total Result Page Count: " + resultCount);
         logger.debug("Start Index: " + startIndex);
         logger.debug("End Index: " + endIndex);
         logger.debug("Requested Page: " + requestedPageNumber);
         logger.debug("Page Size: " + pageSize);
      }
      for (Long possibilityId : possibilityIds) {
         tempPossibleAssetInfos = possibleAssetForPossibilityMap.get(possibilityId);
         boolean isUnstructuredPossibility = AppSourceType.UNSTRUCTURED == possibilityType.get(possibilityId);
         if (isUnstructuredPossibility) {
            tempPossibleAssetInfosSize += Math.ceil((double) tempPossibleAssetInfos.size()
                     / unstructuredAppGroupingResultCount);
         } else {
            tempPossibleAssetInfosSize += tempPossibleAssetInfos.size();
         }
         logger.debug("Possibliity Id: " + possibilityId + " Required Index: " + requiredIndex + " Size: "
                  + tempPossibleAssetInfosSize);
         if (requiredIndex < tempPossibleAssetInfosSize) {
            possibleAssetInfos = new ArrayList<PossibleAssetInfo>();
            int unstructuredIndex = 1;
            for (PossibleAssetInfo possibleAssetInfo : tempPossibleAssetInfos) {
               if (requiredIndex <= endIndex) {
                  // Skip until we reach the start index
                  if (currentPossibleAssetInfoIndex < startIndex) {
                     if (isUnstructuredPossibility && unstructuredIndex / unstructuredAppGroupingResultCount < 1) {
                        unstructuredIndex++;
                        continue;
                     }
                     unstructuredIndex = 1;
                     currentPossibleAssetInfoIndex++;
                     continue;
                  }
                  possibleAssetInfos.add(possibleAssetInfo);
                  if (isUnstructuredPossibility && unstructuredIndex / unstructuredAppGroupingResultCount < 1) {
                     unstructuredIndex++;
                     continue;
                  }
                  requiredIndex++;
                  unstructuredIndex = 1;
                  currentPossibleAssetInfoIndex++;
               } else {
                  logger.debug("Page filled at required Index: " + requiredIndex + " For Possibility Id: "
                           + possibilityId);
                  pageFilled = true;
                  break;
               }
            }
            if (isUnstructuredPossibility && unstructuredIndex > 1) {
               requiredIndex++;
            }
            if (possibleAssetInfos.size() > 0) {
               scopedAssetsPerPossibilityMap.put(possibilityId, possibleAssetInfos);
            }
            if (pageFilled || requiredIndex > endIndex) {
               logger
                        .debug("Page filled at required Index: " + requiredIndex + " For Possibility Id: "
                                 + possibilityId);

               break;
            }
         } else { // Increment the current index to the size
            if (isUnstructuredPossibility) {
               currentPossibleAssetInfoIndex += Math.ceil((double) tempPossibleAssetInfos.size()
                        / unstructuredAppGroupingResultCount);
            } else {
               currentPossibleAssetInfoIndex += tempPossibleAssetInfos.size();
            }
         }
      }
      return scopedAssetsPerPossibilityMap;
   }

   private int getUnstructuredAppGroupingResultCount () {
      return getCoreConfigurationService().getUnstructuredAppGroupingResultCount();
   }

   private void handleProcessPossibilitiesExceptions (String userQueryString, SemanticPossibility possibility,
            Exception exception, int exceptionCode) throws ExeCueException {
      boolean handleProcessPossibilitiesExceptions = getQueryCacheConfiguration().getConfiguration().getBoolean(
               QueryCacheConstants.HANDLE_PROCESS_POSSIBILITIES_ERRORS_FLAG);
      ExeCueException exeCueException = null;
      boolean knownException = false;
      if (exception instanceof ExeCueException) {
         exeCueException = (ExeCueException) exception;
         knownException = true;
      } else {
         exeCueException = new ExeCueException(exceptionCode, exception);
      }
      if (!handleProcessPossibilitiesExceptions) {
         throw exeCueException;
      }
      logger.error("EATEN EXCEPTION START <!--");
      if (knownException) {
         logger.error("EXCEPTION : " + exeCueException.getCode() + " - " + exeCueException.getMessage());
      } else {
         logger.error("EXCEPTION : " + exception.getMessage());
      }
      logger.error("User Query   : " + userQueryString);
      logger.error("Reduced Form : " + possibility.getDisplayString());
      logger.error("EATEN EXCEPTION END --> Exception trace follows ..");
      logger.error(exception, exception);
   }

   public QueryResult getCachedQueryDataResult (Long userQueryId, Long businessQueryId, Long assetId)
            throws ExeCueException {
      return getQDataCachedQueryDataResult(userQueryId, businessQueryId, assetId);
   }

   public QueryResult getCachedQueryDataResult (Long aggregateQueryId) throws ExeCueException {
      return getQDataCachedQueryDataResult(aggregateQueryId);
   }

   protected Map<String, String> getDependentConceptByRequiredConcept () {
      return new HashMap<String, String>(1);
   }

   /**
    * Method to return the boolean value match filter true/false
    * 
    * @return boolean
    */
   public boolean isValueMatchFilterEnabled () {
      return getCoreConfigurationService().isFilterUniversalSearchResultByValueMatch();
   }

   /**
    * @return the reducedFormBQGenerator
    */
   public ReducedFormBusinessQueryGenerator getReducedFormBQGenerator () {
      return reducedFormBQGenerator;
   }

   /**
    * @param reducedFormBQGenerator
    *           the reducedFormBQGenerator to set
    */
   public void setReducedFormBQGenerator (ReducedFormBusinessQueryGenerator reducedFormBQGenerator) {
      this.reducedFormBQGenerator = reducedFormBQGenerator;
   }

   /**
    * @return the businessQueryOrganizationService
    */
   public IBusinessQueryOrganizationService getBusinessQueryOrganizationService () {
      return businessQueryOrganizationService;
   }

   /**
    * @param businessQueryOrganizationService
    *           the businessQueryOrganizationService to set
    */
   public void setBusinessQueryOrganizationService (IBusinessQueryOrganizationService businessQueryOrganizationService) {
      this.businessQueryOrganizationService = businessQueryOrganizationService;
   }

   /**
    * @return the kdxManagementService
    */
   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   /**
    * @param kdxManagementService
    *           the kdxManagementService to set
    */
   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService
    *           the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the queryCacheService
    */
   public IQueryCacheService getQueryCacheService () {
      return queryCacheService;
   }

   /**
    * @param queryCacheService
    *           the queryCacheService to set
    */
   @Override
   public void setQueryCacheService (IQueryCacheService queryCacheService) {
      this.queryCacheService = queryCacheService;
   }

   /**
    * @return the knowledgeSearchEngine
    */
   public IKnowledgeBaseSearchEngine getKnowledgeSearchEngine () {
      return knowledgeSearchEngine;
   }

   /**
    * @param knowledgeSearchEngine
    *           the knowledgeSearchEngine to set
    */
   public void setKnowledgeSearchEngine (IKnowledgeBaseSearchEngine knowledgeSearchEngine) {
      this.knowledgeSearchEngine = knowledgeSearchEngine;
   }

   /**
    * @return the entitySearchEngine
    */
   public IEntitySearchEngine getEntitySearchEngine () {
      return entitySearchEngine;
   }

   /**
    * @param entitySearchEngine
    *           the entitySearchEngine to set
    */
   public void setEntitySearchEngine (IEntitySearchEngine entitySearchEngine) {
      this.entitySearchEngine = entitySearchEngine;
   }

   /**
    * @return the unstructuredWarehouseSearchEngine
    */
   public IUnstructuredWarehouseSearchEngine getUnstructuredWarehouseSearchEngine () {
      return unstructuredWarehouseSearchEngine;
   }

   /**
    * @param unstructuredWarehouseSearchEngine
    *           the unstructuredWarehouseSearchEngine to set
    */
   public void setUnstructuredWarehouseSearchEngine (
            IUnstructuredWarehouseSearchEngine unstructuredWarehouseSearchEngine) {
      this.unstructuredWarehouseSearchEngine = unstructuredWarehouseSearchEngine;
   }

   public RFXServiceHelper getRfxServiceHelper () {
      return rfxServiceHelper;
   }

   public void setRfxServiceHelper (RFXServiceHelper rfxServiceHelper) {
      this.rfxServiceHelper = rfxServiceHelper;
   }

   /**
    * @return the nlpEngine
    */
   public INLPEngine getNlpEngine () {
      return nlpEngine;
   }

   /**
    * @param nlpEngine
    *           the nlpEngine to set
    */
   public void setNlpEngine (INLPEngine nlpEngine) {
      this.nlpEngine = nlpEngine;
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
    * @return the semanticDriverHelper
    */
   public SemanticDriverHelper getSemanticDriverHelper () {
      return semanticDriverHelper;
   }

   /**
    * @param semanticDriverHelper
    *           the semanticDriverHelper to set
    */
   public void setSemanticDriverHelper (SemanticDriverHelper semanticDriverHelper) {
      this.semanticDriverHelper = semanticDriverHelper;
   }

   /**
    * @return the unstructuredSearchConfigurationService
    */
   public IUnstructuredSearchConfigurationService getUnstructuredSearchConfigurationService () {
      return unstructuredSearchConfigurationService;
   }

   /**
    * @param unstructuredSearchConfigurationService
    *           the unstructuredSearchConfigurationService to set
    */
   public void setUnstructuredSearchConfigurationService (
            IUnstructuredSearchConfigurationService unstructuredSearchConfigurationService) {
      this.unstructuredSearchConfigurationService = unstructuredSearchConfigurationService;
   }

   /**
    * @return the applicationRetrievalService
    */
   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   /**
    * @param applicationRetrievalService
    *           the applicationRetrievalService to set
    */
   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

}