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


package com.execue.handler.uss.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.ExecueFacet;
import com.execue.core.common.bean.ExecueFacetDetail;
import com.execue.core.common.bean.FacetQueryInput;
import com.execue.core.common.bean.LocationDetail;
import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.UnstructuredQueryResult;
import com.execue.core.common.bean.UserInput;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.RISharedUserModelMapping;
import com.execue.core.common.bean.entity.unstructured.FeatureRange;
import com.execue.core.common.bean.entity.unstructured.FeatureValue;
import com.execue.core.common.bean.entity.unstructured.RIFeatureContent;
import com.execue.core.common.bean.entity.unstructured.SemantifiedContentFeatureInformation;
import com.execue.core.common.bean.entity.unstructured.UserLocationInfo;
import com.execue.core.common.bean.entity.unstructured.UserPreference;
import com.execue.core.common.bean.entity.unstructured.UserQueryFeatureInformation;
import com.execue.core.common.bean.entity.unstructured.UserQueryLocationInformation;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.qdata.UniversalSearchResultFeatureHeader;
import com.execue.core.common.bean.sdata.location.LocationPointInfo;
import com.execue.core.common.bean.sdata.location.LocationSuggestTerm;
import com.execue.core.common.bean.sdata.location.StateCity;
import com.execue.core.common.bean.security.UserRemoteLocationInfo;
import com.execue.core.common.bean.uss.UniversalUnstructuredSearchResult;
import com.execue.core.common.bean.uss.UnstructuredKeywordSearchInput;
import com.execue.core.common.bean.uss.UnstructuredSearchInput;
import com.execue.core.common.bean.uss.UnstructuredSearchResultItem;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ExecueFacetType;
import com.execue.core.common.type.FeatureValueType;
import com.execue.core.common.type.OrderEntityType;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.exception.HandlerResponseTransformException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.driver.helper.IDriverHelper;
import com.execue.driver.uss.IUnstructuredSearchDriver;
import com.execue.handler.BaseHandler;
import com.execue.handler.bean.Response;
import com.execue.handler.bean.UIUserInput;
import com.execue.handler.comparator.ExecueFacetDisplayOrderComparator;
import com.execue.handler.uss.IUnstructuredSearchServiceHandler;
import com.execue.handler.util.RemoteLocationRetrievalUtil;
import com.execue.message.IMessage;
import com.execue.message.bean.MessageInfo;
import com.execue.message.bean.MessageInput;
import com.execue.message.exception.MessageException;
import com.execue.platform.IUidService;
import com.execue.platform.configuration.IPlatformServicesConfigurationService;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.swi.shared.location.impl.LocationSharedModelServiceImpl;
import com.execue.platform.unstructured.IUnstructuredFacetRetrievalService;
import com.execue.sdata.exception.LocationException;
import com.execue.sdata.exception.SharedModelException;
import com.execue.sdata.service.ILocationRetrievalService;
import com.execue.semantification.batch.service.IGenericArticleResemantificationService;
import com.execue.semantification.bean.GenericArticleResemantificationMessage;
import com.execue.sus.helper.ReducedFormHelper;
import com.execue.sus.helper.SemanticUtil;
import com.execue.sus.helper.SemantificationHelper;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.uss.configuration.IUnstructuredSearchConfigurationService;
import com.execue.uss.helper.UnstructuredSearchHelper;
import com.execue.uswh.configuration.IUnstructuredWHConfigurationService;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.helper.UnstructuredWarehouseHelper;
import com.execue.uswh.service.IUnstructuredWHFeatureContentLookupService;
import com.execue.uswh.service.IUnstructuredWarehouseManagementService;
import com.execue.uswh.service.IUnstructuredWarehouseRetrievalService;

/**
 * @author jitendra
 */
public class UnstructuredSearchServiceHandlerImpl extends BaseHandler implements IUnstructuredSearchServiceHandler {

   private ICoreConfigurationService                  coreConfigurationService;
   private IUnstructuredSearchConfigurationService    unstructuredSearchConfigurationService;
   private IUnstructuredWHFeatureContentLookupService unstructuredWHFeatureContentLookupService;
   private IUnstructuredSearchDriver                  unstructuredSearchDriver;
   private IUnstructuredWarehouseRetrievalService     unstructuredWarehouseRetrievalService;
   private IUnstructuredWarehouseManagementService    unstructuredWarehouseManagementService;
   private ILocationRetrievalService                  locationRetrievalService;
   private IGenericArticleResemantificationService    genericArticleResemantificationService;
   private IUidService                                transactionIdGenerationService;
   private IMessage                                   genericArticleResemantificationThreadMessage;
   private IDriverHelper                              driverHelper;
   private SemantificationHelper                      semantificationHelper;
   private ReducedFormHelper                          reducedFormHelper;
   private IUnstructuredWHConfigurationService        unstructuredWHConfigurationService;
   private IUnstructuredFacetRetrievalService         unstructuredFacetRetrievalService;
   private IPlatformServicesConfigurationService      platformServicesConfigurationService;
   private UnstructuredWarehouseHelper                unstructuredWarehouseHelper;
   private IKDXRetrievalService                       kdxRetrievalService;
   private LocationSharedModelServiceImpl             locationSharedModelService;
   public static final String                         USER_PREFERENCE = "userPreference";

   public Long getQueryId () {
      try {
         return getTransactionIdGenerationService().getNextId();
      } catch (Exception e) {
         return -1l;
      }
   }

   @Override
   public Object process (Object inputBean) throws HandlerException {
      try {
         return getUnstructuredSearchDriver().process(inputBean);
      } catch (ExeCueException e) {
         throw new HandlerException(e.getCode(), e.getMessage(), e);
      }
   }

   @Override
   public Response processRequest (Object request) throws HandlerException {
      Response response = new Response();
      try {
         UIUserInput userRequest = (UIUserInput) request;
         UserInput userInput = generateUserInput(userRequest);
         Object outputBean = process(userInput);
         Object responseObject = getHandlerResponse().transformResponse(outputBean);
         response.setObject(responseObject);
      } catch (HandlerResponseTransformException rte) {
         throw new HandlerException(rte.getCode(), rte.getCause());
      }
      return response;
   }

   @Override
   public Long performQuickResemantification (Long applicationId, Long transactionId) throws HandlerException {
      GenericArticleResemantificationMessage genericArticleResemantificationMessage = new GenericArticleResemantificationMessage();
      genericArticleResemantificationMessage.setContextId(applicationId);
      genericArticleResemantificationMessage.setUserQueryId(transactionId);

      MessageInput minput = new MessageInput();
      minput.setTransactionId(transactionId);
      minput.setObject(genericArticleResemantificationMessage);

      MessageInfo messageInfo;
      try {
         messageInfo = getGenericArticleResemantificationThreadMessage().processMessage(minput);
      } catch (MessageException e) {
         throw new HandlerException(e.getCode(), e.getCause());
      }
      return messageInfo.getId();
   }

   @Override
   public UniversalUnstructuredSearchResult getUniversalSearchResult (Long applicationId, Long userQueryId,
            Integer userQueryFeatureCount, Integer userQueryRecordCount, List<UserLocationInfo> userLocationInfos,
            Page page, Integer distanceLimit, boolean imagePresent, UniversalSearchResultFeatureHeader sortingInfo,
            boolean fromPagination) throws HandlerException {

      UniversalUnstructuredSearchResult universalUnstructuredSearchResult = new UniversalUnstructuredSearchResult();
      boolean applyKeyWordSearchFilter = getUnstructuredSearchConfigurationService().getApplyKeyWordSearchFilter();
      boolean applyPartialMatchFilter = getUnstructuredSearchConfigurationService().getApplyPartialMatchFilter();

      boolean displayCloseMatchCount = getUnstructuredSearchConfigurationService().getDisplayClosedMatchCount();
      boolean isLocationBased = isLocationBased(applicationId);

      try {
         List<UniversalSearchResultFeatureHeader> uiversalSearchResultFeatureHeaders = SemanticUtil
                  .populateUiversalSearchResultFeatureHeader(getUnstructuredWHFeatureContentLookupService()
                           .getOrderedDisplayableRIFeatureContentByContextId(applicationId));
         // add semantic date and semaniic score
         uiversalSearchResultFeatureHeaders.addAll(getUnstructuredSearchConfigurationService()
                  .getUnstructuredSearchResultsHeaders());
         List<String> searchResultOrder = getUnstructuredSearchConfigurationService().getSearchResultOrder(
                  prepareSortingInfo(sortingInfo, uiversalSearchResultFeatureHeaders, fromPagination));
         String userRequestedSortOrder = getUserRequestedSortOrder(sortingInfo);

         List<String> selectColumnNameList = getUnstructuredWHFeatureContentLookupService()
                  .getOrderedDisplayableColumnsByContextId(applicationId);

         boolean useDbFunctionForMultipleLocationQuery = getUnstructuredSearchConfigurationService()
                  .getUseDbFunctionForMultipleLocationQuery();
         String dbFunctionNameForMultipleLocationQuery = getUnstructuredSearchConfigurationService()
                  .getDbFunctionNameForMultipleLocationQuery();

         UnstructuredSearchInput unstructuredSearchInput = UnstructuredSearchHelper.prepareUnstructuredSearchInput(
                  applicationId, userQueryId, userQueryFeatureCount, userQueryRecordCount, distanceLimit,
                  isLocationBased, imagePresent, applyKeyWordSearchFilter, applyPartialMatchFilter,
                  displayCloseMatchCount, searchResultOrder, getUserRequestedLocations(userLocationInfos),
                  selectColumnNameList, uiversalSearchResultFeatureHeaders, userRequestedSortOrder,
                  useDbFunctionForMultipleLocationQuery, dbFunctionNameForMultipleLocationQuery);

         universalUnstructuredSearchResult = getUnstructuredWarehouseRetrievalService().getUnstructuredSearchResult(
                  applicationId, unstructuredSearchInput, page);
         universalUnstructuredSearchResult.setUniversalSearchResultFeatureHeaders(uiversalSearchResultFeatureHeaders);

      } catch (UnstructuredWarehouseException e1) {
         throw new HandlerException(e1.getCode(), e1);
      }

      if (CollectionUtils.isEmpty(universalUnstructuredSearchResult.getUnstructuredSearchResultItem())) {
         return universalUnstructuredSearchResult;
      }

      for (UnstructuredSearchResultItem unstructuredSearchResultItem : universalUnstructuredSearchResult
               .getUnstructuredSearchResultItem()) {
         updateResultItem(unstructuredSearchResultItem);
      }
      return universalUnstructuredSearchResult;
   }

   @Override
   public void saveUserQueryFeatureInformation (UnstructuredQueryResult queryResult, Long applicationId)
            throws HandlerException {
      try {

         List<SemantifiedContentFeatureInformation> featuresInformation = new ArrayList<SemantifiedContentFeatureInformation>();
         if (!CollectionUtils.isEmpty(queryResult.getSemanticPossibilities())) {
            featuresInformation = getSemantificationHelper().getSemantifiedContentFeaturesInformation(
                     queryResult.getSemanticPossibilities().iterator().next(), false);
         }

         queryResult.setUserQueryFeatureCount(UnstructuredSearchHelper.getUserQueryFeatureCount(featuresInformation));
         queryResult.setUserQueryRecordCount(UnstructuredSearchHelper.getUserQueryRecordCount(featuresInformation));

         // Save the user query feature information
         if (!queryResult.isUserQueryInfoSaved()) {
            Long userQueryId = queryResult.getId();
            Set<UserQueryFeatureInformation> userQueryFeaturesInformation = getUnstructuredWarehouseHelper()
                     .populateUserQueryFeatureInformation(featuresInformation, userQueryId, applicationId);

            getUnstructuredWarehouseManagementService().saveUserQueryFeatureInformation(applicationId,
                     new ArrayList<UserQueryFeatureInformation>(userQueryFeaturesInformation));
         }

      } catch (UnstructuredWarehouseException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   @Override
   public void saveUserQueryLocationInformation (UnstructuredQueryResult queryResult,
            LocationSuggestTerm locationSuggestTerm, UserPreference userPreference) throws HandlerException {
      try {
         Long userQueryId = queryResult.getId();
         Long applicationId = queryResult.getApplicationId();
         Set<SemanticPossibility> semanticPossibilities = queryResult.getSemanticPossibilities();
         List<UserLocationInfo> userLocationInfos = populateLocationInfoFromUserQuery(semanticPossibilities);

         if (!CollectionUtils.isEmpty(userLocationInfos)) {
            //Do nothing
         } else if (locationSuggestTerm != null && locationSuggestTerm.getId() > 0) {
            userLocationInfos = populateLocationInfoFromLocationSuggestTerm(locationSuggestTerm);
         } else if (!CollectionUtils.isEmpty(userPreference.getUserLocationInfos())) {
            userLocationInfos = userPreference.getUserLocationInfos();
         } else if (userPreference.getUserRemoteLocationInfo() != null
                  && userPreference.getUserRemoteLocationInfo().getLocationId() != null) {
            userLocationInfos.add(userPreference.getUserRemoteLocationInfo());
         }

         // Get the default location info
         if (CollectionUtils.isEmpty(userLocationInfos)) {
            UserLocationInfo userLocationInfo = getUserLocationInformationsByZipCode(getDefaultUserPreferredZipCode());
            userLocationInfos.add(userLocationInfo);
         }
         if (userLocationInfos.size() > 1 && !queryResult.isUserQueryInfoSaved()) {
            List<UserQueryLocationInformation> userQueryLocationInformations = prepareUQLIFromUserLocationInfos(
                     applicationId, userQueryId, userLocationInfos);
            getUnstructuredWarehouseManagementService().saveUserQueryLocationInformation(applicationId,
                     userQueryLocationInformations);
            queryResult.setMultipleLocation(true);
         }
         queryResult.setLocationDisplayName(prepareLocationDisplayName(userLocationInfos));
         userPreference.setUserLocationInfos(userLocationInfos);
      } catch (UnstructuredWarehouseException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (LocationException le) {
         throw new HandlerException(le.getCode(), le);
      }
   }

   /**
    * @param queryResult
    * @param isForResemantification
    * @throws HandlerException
    */
   public void populateSemantifiedContentKeywordMatch (UnstructuredQueryResult queryResult,
            List<UserLocationInfo> userLocationInfos, boolean isForResemantification) throws HandlerException {
      boolean applyKeyWordSearchFilter = getUnstructuredSearchConfigurationService().getApplyKeyWordSearchFilter();
      // return if the key word match is not enabled and is not for re-semantification
      if (!applyKeyWordSearchFilter && !isForResemantification) {
         return;
      }

      String userQueryTokens = "";
      Set<SemanticPossibility> semanticPossibilities = queryResult.getSemanticPossibilities();
      String userQuery = queryResult.getOriginalQuery();
      if (CollectionUtils.isEmpty(semanticPossibilities)) {
         List<String> queryTokens = ExecueStringUtil.getAsList(userQuery);
         userQueryTokens = " " + StringUtils.join(queryTokens, " ");
      } else {
         for (SemanticPossibility semanticPossibility : semanticPossibilities) {

            // TODO: NK:: Need to check how to handle this in generic unstructured search
            Map<Long, Integer> conceptPriorityMap = new HashMap<Long, Integer>();

            userQueryTokens += getReducedFormHelper().getKeyWordMatchText(semanticPossibility, conceptPriorityMap);
            List<String> unrecognizedQueryWords = SemanticUtil.getUnrecognizedQueryWords(semanticPossibility);
            userQueryTokens += " " + StringUtils.join(unrecognizedQueryWords, " ");
         }
      }

      // Update the semantified content key word match table
      Integer maxRecordCount = getUnstructuredSearchConfigurationService()
               .getDefaultSemantifiedContentKeywordMatchMaxRecordCount();
      if (isForResemantification) {
         maxRecordCount = getUnstructuredSearchConfigurationService()
                  .getResemantificationSemantifiedContentKeywordMatchMaxRecordCount();
      }

      List<String> userRequestedLocations = getUserRequestedLocations(userLocationInfos);
      Long applicationId = queryResult.getApplicationId();
      Long userQueryId = queryResult.getId();
      Integer userQueryDistanceLimit = queryResult.getUserQueryDistanceLimit();
      boolean isLocationBased = isLocationBased(applicationId);
      boolean applyImagePresentFilter = queryResult.isApplyImagePresentFilter();
      boolean useDbFunctionForMultipleLocationQuery = getUnstructuredSearchConfigurationService()
               .getUseDbFunctionForMultipleLocationQuery();
      String dbFunctionNameForMultipleLocationQuery = getUnstructuredSearchConfigurationService()
               .getDbFunctionNameForMultipleLocationQuery();

      // Prepare the input for the keyword search
      UnstructuredKeywordSearchInput unstructuredKeywordSearchInput = UnstructuredSearchHelper
               .prepareKeywordSearchInput(userRequestedLocations, userQueryTokens, applicationId, userQueryId,
                        userQueryDistanceLimit, maxRecordCount, isLocationBased, applyImagePresentFilter,
                        useDbFunctionForMultipleLocationQuery, dbFunctionNameForMultipleLocationQuery);

      try {
         getUnstructuredWarehouseRetrievalService().populateSemantifiedContentKeyWordMatchResultForUserQuery(
                  unstructuredKeywordSearchInput);
         if (isForResemantification) {
            getUnstructuredWarehouseManagementService().markSemantifiedContentForResemantification(applicationId,
                     userQueryId);
         }
      } catch (UnstructuredWarehouseException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   @Override
   public List<LocationSuggestTerm> suggestLocationBySearchString (String search) throws HandlerException {
      try {
         return getLocationRetrievalService().suggestLocationBySearchString(search,
                  getUnstructuredSearchConfigurationService().getSuggestionRetrivalLimit());
      } catch (LocationException e) {
         e.printStackTrace();
         throw new HandlerException(e.getCode(), e);
      }

   }

   @Override
   public List<ExecueFacet> retrieveFacets (Long contextId, Long modelId, String selectedDefaultVicinityDistanceLimit,
            String imagePresent, Long userQueryId, List<UserLocationInfo> userLocationInfos) throws HandlerException {

      List<ExecueFacet> facets = new ArrayList<ExecueFacet>();
      try {
         // get userQueryFeatureInformations from DB
         List<UserQueryFeatureInformation> userQueryFeatureInformations = getUnstructuredWarehouseRetrievalService()
                  .getUserQueryFeatureInformation(contextId, userQueryId);
         List<UserQueryLocationInformation> userQueryLocationInformations = prepareUQLIFromUserLocationInfos(contextId,
                  userQueryId, userLocationInfos);
         FacetQueryInput facetQueryInput = buildFacetQueryInput(contextId, selectedDefaultVicinityDistanceLimit,
                  imagePresent, userQueryLocationInformations, userQueryFeatureInformations);
         facets = getUnstructuredFacetRetrievalService().retrieveFacets(contextId, facetQueryInput);
         if (ExecueCoreUtil.isCollectionNotEmpty(facets)) {
            Collections.sort(facets, new ExecueFacetDisplayOrderComparator());
            markExecueFacetsAsProminent(facets);
            markExecueFacetsAsSelected(contextId, modelId, facets, userQueryLocationInformations,
                     userQueryFeatureInformations);
         }
      } catch (PlatformException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (UnstructuredWarehouseException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
      return facets;

   }

   private List<UserQueryLocationInformation> prepareUQLIFromUserLocationInfos (Long contextId, Long userQueryId,
            List<UserLocationInfo> userLocationInfos) {
      List<UserQueryLocationInformation> userQueryLocationInformations = new ArrayList<UserQueryLocationInformation>();
      for (UserLocationInfo userLocationInfo : userLocationInfos) {
         UserQueryLocationInformation userQueryLocationInformation = new UserQueryLocationInformation();
         userQueryLocationInformation.setContextId(contextId);
         userQueryLocationInformation.setQueryId(userQueryId);
         userQueryLocationInformation.setLocationId(userLocationInfo.getLocationId());
         userQueryLocationInformation.setLocationBedId(userLocationInfo.getLocationBedId());
         userQueryLocationInformation.setLocationDisplayName(userLocationInfo.getLocationDisplayName());
         userQueryLocationInformation.setLatitude(userLocationInfo.getLatitude());
         userQueryLocationInformation.setLongitude(userLocationInfo.getLongitude());
         userQueryLocationInformation.setExecutionDate(new Date());
         userQueryLocationInformations.add(userQueryLocationInformation);
      }
      return userQueryLocationInformations;
   }

   @Override
   public void prepareAndPersistUserQueryFeatureInformationsFromFacets (UnstructuredQueryResult queryResult,
            List<ExecueFacet> facets) throws HandlerException {
      try {
         List<UserQueryFeatureInformation> finalUserQueryFeatureInformations = new ArrayList<UserQueryFeatureInformation>();
         Long applicationId = queryResult.getApplicationId();
         boolean otherFeatureFound = false;
         if (ExecueCoreUtil.isCollectionNotEmpty(facets)) {
            List<UserQueryFeatureInformation> userQueryFeatureInformations = null;
            for (ExecueFacet facet : facets) {
               RIFeatureContent featureContent = getRIFeatureContentByFeatureId(applicationId, Long.valueOf(facet
                        .getId()));

               // Skip for location based features
               if (featureContent.getLocationBased() == CheckType.YES) {
                  continue;
               }
               otherFeatureFound = true;
               if (featureContent.getFeatureValueType() == FeatureValueType.VALUE_NUMBER) {
                  userQueryFeatureInformations = getUserQueryFeatureInfoForNumberFacets(facet, queryResult
                           .getApplicationId(), queryResult.getId());
               } else {
                  userQueryFeatureInformations = getUserQueryFeatureInfoForStringFacets(facet, queryResult
                           .getApplicationId(), queryResult.getId());
               }

               if (ExecueCoreUtil.isCollectionNotEmpty(userQueryFeatureInformations)) {
                  // Also add the unknown(default) feature value information
                  try {
                     UserQueryFeatureInformation unknownUserQueryFeatureInformation = getUnstructuredWarehouseHelper()
                              .getUnknownUserQueryFeatureInformation(userQueryFeatureInformations.get(0),
                                       featureContent.getFeatureValueType());
                     userQueryFeatureInformations.add(unknownUserQueryFeatureInformation);
                  } catch (CloneNotSupportedException e) {
                     throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
                  }
                  finalUserQueryFeatureInformations.addAll(userQueryFeatureInformations);
               }
            }
         }
         // Add the dummy feature user query info
         UserQueryFeatureInformation dummyUserQueryFeatureInfo = getUnstructuredWarehouseHelper()
                  .getDummyUserQueryFeatureInfo(queryResult.getId(), applicationId);
         if (!otherFeatureFound) {
            dummyUserQueryFeatureInfo.setFeatureWeightFactor(getUnstructuredWHConfigurationService()
                     .getDefaultFeatureWeightFactor());
         }
         finalUserQueryFeatureInformations.add(dummyUserQueryFeatureInfo);
         if (ExecueCoreUtil.isCollectionNotEmpty(finalUserQueryFeatureInformations)) {
            getUnstructuredWarehouseManagementService().saveUserQueryFeatureInformation(applicationId,
                     finalUserQueryFeatureInformations);
         }
         queryResult.setUserQueryFeatureCount(getUserQueryFeatureCountFromFacets(applicationId, facets));
         queryResult.setUserQueryRecordCount(getUserQueryRecordCountFromFacets(applicationId, facets));
      } catch (UnstructuredWarehouseException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   @Override
   public void prepareAndPersistUserQueryLocationInformationsFromFacets (List<ExecueFacet> facets,
            UnstructuredQueryResult queryResult, LocationSuggestTerm locationSuggestTerm,
            UserPreference userPreference, Long modelId) throws HandlerException {

      if (ExecueCoreUtil.isCollectionEmpty(facets)) {
         return;
      }
      try {
         Long applicationId = queryResult.getApplicationId();
         Long userQueryId = queryResult.getId();
         List<UserLocationInfo> userLocationInformations = populateLocationInfoFromFacets(facets, applicationId,
                  userQueryId, modelId);
         if (!ExecueCoreUtil.isCollectionEmpty(userLocationInformations)) {
            // Do nothing
         } else if (locationSuggestTerm != null && locationSuggestTerm.getId() > 0) {
            userLocationInformations = populateLocationInfoFromLocationSuggestTerm(locationSuggestTerm);
         } else if (!CollectionUtils.isEmpty(userPreference.getUserLocationInfos())) {
            userLocationInformations = userPreference.getUserLocationInfos();
         } else if (userPreference.getUserRemoteLocationInfo() != null
                  && userPreference.getUserRemoteLocationInfo().getLocationId() != null) {
            userLocationInformations.add(userPreference.getUserRemoteLocationInfo());
         }

         // Get the default location info if its empty
         if (CollectionUtils.isEmpty(userLocationInformations)) {
            UserLocationInfo userLocationInfo = getUserLocationInformationsByZipCode(getDefaultUserPreferredZipCode());
            userLocationInformations.add(userLocationInfo);
         }
         // If we have more than one user location informations, then save it in db
         if (userLocationInformations.size() > 1 && !queryResult.isUserQueryInfoSaved()) {
            List<UserQueryLocationInformation> userQueryLocationInformations = prepareUQLIFromUserLocationInfos(
                     applicationId, userQueryId, userLocationInformations);
            getUnstructuredWarehouseManagementService().saveUserQueryLocationInformation(applicationId,
                     userQueryLocationInformations);
            queryResult.setMultipleLocation(true);
         }
         queryResult.setLocationDisplayName(prepareLocationDisplayName(userLocationInformations));
         userPreference.setUserLocationInfos(userLocationInformations);
      } catch (LocationException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (UnstructuredWarehouseException e) {
         throw new HandlerException(e.getCode(), e);
      } catch (NumberFormatException e) {
         throw new HandlerException(10, e);
      } catch (KDXException e) {
         throw new HandlerException(e.getCode(), e);
      }
   }

   @Override
   public UserPreference populateUserPreference (HttpServletRequest request, Map httpSession) throws HandlerException {
      UserPreference userPreference = (UserPreference) httpSession.get(USER_PREFERENCE);
      if (userPreference == null) {
         UserRemoteLocationInfo userRemoteLocationInfo = RemoteLocationRetrievalUtil.populateUserRemoteLocationInfo(
                  request, httpSession, getUserRemoteLocationUrl(), getUserRemoteLocationConnectTimeout(),
                  getUserRemoteLocationReadTimeout());

         userPreference = new UserPreference();
         httpSession.put(USER_PREFERENCE, userPreference);
         try {
            UserLocationInfo userLocationInfo = populateLocationInfoFromUserRemoteLocation(userRemoteLocationInfo);
            userPreference.setUserRemoteLocationInfo(userLocationInfo);
         } catch (SharedModelException e) {
            throw new HandlerException(e.getCode(), e);
         }
      }
      return userPreference;
   }

   /**
    * @param facets
    * @param long1
    * @param modelId 
    * @param modelId
    * @param finalUserQueryLocationInformations
    * @throws UnstructuredWarehouseException
    * @throws LocationException
    * @throws KDXException
    */
   private List<UserLocationInfo> populateLocationInfoFromFacets (List<ExecueFacet> facets, Long applicationId,
            Long userQueryId, Long modelId) throws UnstructuredWarehouseException, LocationException, KDXException {
      List<UserLocationInfo> finalUserLocationInformations = new ArrayList<UserLocationInfo>();
      if (CollectionUtils.isEmpty(facets)) {
         return finalUserLocationInformations;
      }

      for (ExecueFacet facet : facets) {
         RIFeatureContent featureContent = getRIFeatureContentByFeatureId(applicationId, Long.valueOf(facet.getId()));
         if (CheckType.YES.equals(featureContent.getLocationBased())) {
            List<UserLocationInfo> userQueryLocationInformations = prepareUserLocationInformationsFromFacets(modelId,
                     facet, facet.getFacetDetails(), userQueryId, applicationId);
            if (ExecueCoreUtil.isCollectionNotEmpty(userQueryLocationInformations)) {
               finalUserLocationInformations.addAll(userQueryLocationInformations);
            }
         }
      }
      return finalUserLocationInformations;
   }

   private double getSQRTBasedDistanceLimit (Integer distanceLimit) {
      double sqrtBasedDistanceLimit = Math.pow(distanceLimit / 69d, 2);
      return sqrtBasedDistanceLimit;
   }

   public Long getUnstructuredResultPageSize () {
      return getUnstructuredSearchConfigurationService().getUnstructuredResultPageSize();
   }

   public Long getUnstructuredResultNumberOfLinks () {
      return getUnstructuredSearchConfigurationService().getUnstructuredResultNumberOfLinks();
   }

   public Response fetchResponse (Long userQueryId, Long businessQueryId, Long assetId) throws HandlerException {
      return null;
   }

   public Response fetchResponse (Long aggregateQueryId) throws HandlerException {
      return null;
   }

   public IUnstructuredSearchDriver getUnstructuredSearchDriver () {
      return unstructuredSearchDriver;
   }

   public void setUnstructuredSearchDriver (IUnstructuredSearchDriver unstructuredSearchDriver) {
      this.unstructuredSearchDriver = unstructuredSearchDriver;
   }

   public IUnstructuredSearchConfigurationService getUnstructuredSearchConfigurationService () {
      return unstructuredSearchConfigurationService;
   }

   public void setUnstructuredSearchConfigurationService (
            IUnstructuredSearchConfigurationService unstructuredSearchConfigurationService) {
      this.unstructuredSearchConfigurationService = unstructuredSearchConfigurationService;
   }

   public IDriverHelper getDriverHelper () {
      return driverHelper;
   }

   public void setDriverHelper (IDriverHelper driverHelper) {
      this.driverHelper = driverHelper;
   }

   /**
    * @return the semantificationHelper
    */
   public SemantificationHelper getSemantificationHelper () {
      return semantificationHelper;
   }

   /**
    * @param semantificationHelper
    *           the semantificationHelper to set
    */
   public void setSemantificationHelper (SemantificationHelper semantificationHelper) {
      this.semantificationHelper = semantificationHelper;
   }

   /**
    * @return the reducedFormHelper
    */
   public ReducedFormHelper getReducedFormHelper () {
      return reducedFormHelper;
   }

   /**
    * @param reducedFormHelper
    *           the reducedFormHelper to set
    */
   public void setReducedFormHelper (ReducedFormHelper reducedFormHelper) {
      this.reducedFormHelper = reducedFormHelper;
   }

   /**
    * @return the locationRetrievalService
    */
   public ILocationRetrievalService getLocationRetrievalService () {
      return locationRetrievalService;
   }

   /**
    * @param locationRetrievalService
    *           the locationRetrievalService to set
    */
   public void setLocationRetrievalService (ILocationRetrievalService locationRetrievalService) {
      this.locationRetrievalService = locationRetrievalService;
   }

   @Override
   public Integer getDefaultVicinityDistanceLimit () {
      return getUnstructuredSearchConfigurationService().getDefaultVicinityDistanceLimit();
   }

   @Override
   public List<String> getdefaultVicinityDistanceLimits () {
      return getUnstructuredSearchConfigurationService().getDefaultVicinityDistanceLimits();
   }

   @Override
   public String getUserRemoteLocationConnectTimeout () {
      return getCoreConfigurationService().getUserRemoteLocationConnectTimeout();
   }

   @Override
   public String getUserRemoteLocationReadTimeout () {
      return getCoreConfigurationService().getUserRemoteLocationReadTimeout();
   }

   @Override
   public String getUserRemoteLocationUrl () {
      return getCoreConfigurationService().getUserRemoteLocationUrl();
   }

   @Override
   public String getDefaultUserPreferredZipCode () {
      return getUnstructuredSearchConfigurationService().getDefaultUserPreferredZipCode();
   }

   @Override
   public boolean isLocationBased (Long applicationId) {
      return getPlatformServicesConfigurationService().isApplicationHasLocationRealization(applicationId);
   }

   private Integer getUserQueryRecordCountFromFacets (Long contextId, List<ExecueFacet> facets) {
      int recordCount = 1;
      if (CollectionUtils.isEmpty(facets)) {
         return recordCount;
      }
      for (ExecueFacet execueFacet : facets) {
         RIFeatureContent featureContent = getRIFeatureContentByFeatureId(contextId, Long.valueOf(execueFacet.getId()));
         if (featureContent.getLocationBased() == CheckType.YES) {
            continue;
         }
         if (featureContent.getMultiValued() == CheckType.NO) {
            recordCount += 1;
            continue;
         }
         recordCount += execueFacet.getFacetDetails().size();
      }
      return recordCount;
   }

   private Integer getUserQueryFeatureCountFromFacets (Long contextId, List<ExecueFacet> facets) {
      int featureCount = 0;
      if (CollectionUtils.isEmpty(facets)) {
         return featureCount;
      }

      for (ExecueFacet execueFacet : facets) {
         RIFeatureContent featureContent = getRIFeatureContentByFeatureId(contextId, Long.valueOf(execueFacet.getId()));
         if (featureContent.getLocationBased() == CheckType.YES) {
            continue;
         }
         if (featureContent.getMultiValued() == CheckType.NO) {
            featureCount += 1;
            continue;
         } else if (featureContent.getMultiValuedGlobalPenalty() == CheckType.YES) {
            featureCount += execueFacet.getFacetDetails().size();
            continue;
         }
         featureCount += 1;
      }
      // If feature count is zero here, means its purely based on location, so will have to consider the feature count
      // as 1 for dummy feature match
      if (featureCount == 0) {
         featureCount = 1;
      }
      return featureCount;
   }

   private RIFeatureContent getRIFeatureContentByFeatureId (Long applicationId, Long featureId) {
      Map<Long, RIFeatureContent> facetRIFeatureContentMap = getUnstructuredWHFeatureContentLookupService()
               .getFacetRIFeatureContentMapByContextId(applicationId);
      return facetRIFeatureContentMap.get(featureId);
   }

   private List<UserLocationInfo> prepareUserLocationInformationsFromFacets (Long modelId, ExecueFacet facet,
            List<ExecueFacetDetail> facetDetails, Long userQueryId, Long applicationId) throws NumberFormatException,
            UnstructuredWarehouseException, LocationException, KDXException {
      List<UserLocationInfo> userQueryLocationInformations = new ArrayList<UserLocationInfo>();
      // get model Group Id
      Set<Long> modelGroupIds = getModelGroupIds(getKdxRetrievalService().getUserModelGroupsByModelId(modelId));

      for (ExecueFacetDetail facetDetail : facetDetails) {
         // Make a DB call to get FeatureValue by featureId and feature value
         FeatureValue featureValue = getUnstructuredWarehouseRetrievalService()
                  .getFeatureValueByFeatureIdAndFeatureValue(applicationId, Long.valueOf(facet.getId()),
                           facetDetail.getName());
         // RISharedUserModelMapping by
         RISharedUserModelMapping sharedUserModelMapping = getKdxRetrievalService()
                  .getRISharedUserModelMappingByAppInstanceBedId(featureValue.getFeatureValueBeId(), modelGroupIds);
         // get locationPointInfo from DB by location BedId
         LocationPointInfo locationPointInfo = getLocationRetrievalService().getLocationPointInfoByLocationBedId(
                  sharedUserModelMapping.getBaseInstanceBedId());
         // prepare UserQueryLocationInfo from LocationPointInfo
         UserLocationInfo userLocationInformation = prepareUserLocationInfoFromLocationPointInfo(locationPointInfo);
         userQueryLocationInformations.add(userLocationInformation);
      }
      return userQueryLocationInformations;
   }

   private Set<Long> getModelGroupIds (List<ModelGroup> userModelGroups) {
      Set<Long> userModelGroupIds = new HashSet<Long>();
      for (ModelGroup modelGroup : userModelGroups) {
         userModelGroupIds.add(modelGroup.getId());
      }
      return userModelGroupIds;
   }

   private UserQueryLocationInformation prepareUserQueryLocationInfoFromLocationPointInfo (
            LocationPointInfo locationPointInfo, Long applicationId, Long userQueryId) {
      UserQueryLocationInformation userQueryLocationInformation = new UserQueryLocationInformation();
      userQueryLocationInformation.setContextId(applicationId);
      userQueryLocationInformation.setQueryId(userQueryId);
      userQueryLocationInformation.setLocationId(locationPointInfo.getId());
      userQueryLocationInformation.setLocationBedId(locationPointInfo.getLocationBedId());
      userQueryLocationInformation.setLongitude(locationPointInfo.getLongitude());
      userQueryLocationInformation.setLatitude(locationPointInfo.getLatitude());
      userQueryLocationInformation.setExecutionDate(new Date());
      userQueryLocationInformation.setLocationDisplayName(locationPointInfo.getLocationDisplayName());
      return userQueryLocationInformation;
   }

   private UserLocationInfo prepareUserLocationInfoFromLocationPointInfo (LocationPointInfo locationPointInfo) {
      UserLocationInfo userLocationInformation = new UserLocationInfo();
      userLocationInformation.setLocationId(locationPointInfo.getId());
      userLocationInformation.setLocationBedId(locationPointInfo.getLocationBedId());
      userLocationInformation.setLongitude(locationPointInfo.getLongitude());
      userLocationInformation.setLatitude(locationPointInfo.getLatitude());
      userLocationInformation.setLocationDisplayName(locationPointInfo.getLocationDisplayName());
      return userLocationInformation;
   }

   private List<UserQueryFeatureInformation> getUserQueryFeatureInfoForNumberFacets (ExecueFacet facet,
            Long applicationId, Long userQueryId) throws UnstructuredWarehouseException {
      List<UserQueryFeatureInformation> userQueryFeatureInformations = new ArrayList<UserQueryFeatureInformation>();
      Long featureId = Long.valueOf(facet.getId());

      // Get Feature Range from DB call
      List<FeatureRange> featureRanges = getUnstructuredWarehouseRetrievalService().getFeatureRangesByFeatureId(
               applicationId, featureId);
      List<ExecueFacetDetail> facetDetails = facet.getFacetDetails();
      for (ExecueFacetDetail facetDetail : facetDetails) {
         UserQueryFeatureInformation userQueryFeatureInformation = new UserQueryFeatureInformation();
         userQueryFeatureInformation.setContextId(applicationId);
         userQueryFeatureInformation.setFeatureId(featureId);
         userQueryFeatureInformation.setQueryId(userQueryId);
         userQueryFeatureInformation.setExecutionDate(new Date());
         if (ExecueFacetType.NUMBER.equals(facet.getType())) {
            for (FeatureRange featureRange : featureRanges) {
               if (facetDetail.getName().equals(featureRange.getRangeName())) {
                  userQueryFeatureInformation.setStartValue(featureRange.getStartValue() + "");
                  userQueryFeatureInformation.setEndValue(featureRange.getEndValue() + "");
                  break;
               }
            }
         }
         userQueryFeatureInformations.add(userQueryFeatureInformation);
      }
      return userQueryFeatureInformations;
   }

   private List<UserQueryFeatureInformation> getUserQueryFeatureInfoForStringFacets (ExecueFacet facet,
            Long applicationId, Long userQueryId) throws UnstructuredWarehouseException {
      List<UserQueryFeatureInformation> userQueryFeatureInformations = new ArrayList<UserQueryFeatureInformation>();
      Long featureId = Long.valueOf(facet.getId());

      List<ExecueFacetDetail> facetDetails = facet.getFacetDetails();
      for (ExecueFacetDetail facetDetail : facetDetails) {
         UserQueryFeatureInformation userQueryFeatureInformation = new UserQueryFeatureInformation();
         userQueryFeatureInformation.setContextId(applicationId);
         userQueryFeatureInformation.setQueryId(userQueryId);
         userQueryFeatureInformation.setFeatureId(featureId);
         userQueryFeatureInformation.setExecutionDate(new Date());
         userQueryFeatureInformation.setStartValue(facetDetail.getName());
         userQueryFeatureInformation.setEndValue(facetDetail.getName());
         userQueryFeatureInformations.add(userQueryFeatureInformation);
      }
      return userQueryFeatureInformations;
   }

   private void markExecueFacetsAsSelected (Long contextId, Long modelId, List<ExecueFacet> facets,
            List<UserQueryLocationInformation> userQueryLocationInformations,
            List<UserQueryFeatureInformation> userQueryFeatureInformations) throws UnstructuredWarehouseException,
            KDXException {
      Map<Long, List<UserQueryFeatureInformation>> userQueryFeatureInformationMap = populateUserQueryFeatureInformationMap(userQueryFeatureInformations);
      for (ExecueFacet facet : facets) {
         Long facetId = Long.valueOf(facet.getId()).longValue();
         List<ExecueFacetDetail> facetDetails = facet.getFacetDetails();
         RIFeatureContent featureContent = getRIFeatureContentByFeatureId(contextId, facetId);
         List<ExecueFacetDetail> selectedFacetDetails = new ArrayList<ExecueFacetDetail>();
         if (CheckType.YES.equals(featureContent.getLocationBased())) {
            Set<Long> featureValueBedIds = getFeatureBedIdsFromUserQueryLocationInformations(
                     userQueryLocationInformations, modelId);
            // Db call to get feature values by featureValueBedIds
            List<FeatureValue> featureValues = getUnstructuredWarehouseRetrievalService().getFeatureValues(contextId,
                     featureValueBedIds);
            if (ExecueCoreUtil.isCollectionNotEmpty(featureValues)) {
               selectedFacetDetails = getSelectedExecueFacetDetailLocations(facetDetails, featureValues);
            }
         } else {
            List<UserQueryFeatureInformation> userQueryFeatureInformationsByFeatureId = userQueryFeatureInformationMap
                     .get(facetId);
            if (ExecueCoreUtil.isCollectionNotEmpty(userQueryFeatureInformationsByFeatureId)) {
               userQueryFeatureInformationsByFeatureId = getUnstructuredWarehouseHelper().filterDefaultFeatureValues(
                        userQueryFeatureInformationsByFeatureId, featureContent);
               if (ExecueFacetType.STRING.equals(facet.getType())) {
                  selectedFacetDetails = getSelectedFacetDetailFeatureValues(userQueryFeatureInformationsByFeatureId,
                           facetDetails);
               } else if (ExecueFacetType.NUMBER.equals(facet.getType())) {
                  selectedFacetDetails = getSelectedFacetDetailRanges(facetId, contextId, facetDetails,
                           userQueryFeatureInformationsByFeatureId, featureContent);
               }
            }
         }
         pushSelectedOnesToTop(facet, selectedFacetDetails);
      }
   }

   private void pushSelectedOnesToTop (ExecueFacet facet, List<ExecueFacetDetail> selectedFacetDetails) {
      if (ExecueCoreUtil.isCollectionNotEmpty(selectedFacetDetails)) {
         facet.getFacetDetails().removeAll(selectedFacetDetails);
         facet.getFacetDetails().addAll(0, selectedFacetDetails);
      }
   }

   private List<ExecueFacetDetail> getSelectedFacetDetailFeatureValues (
            List<UserQueryFeatureInformation> userQueryFeatureInformations, List<ExecueFacetDetail> facetDetails) {
      List<ExecueFacetDetail> selectedFacetDetails = new ArrayList<ExecueFacetDetail>();
      for (ExecueFacetDetail facetDetail : facetDetails) {
         for (UserQueryFeatureInformation userQueryFeatureInformation : userQueryFeatureInformations) {
            if (facetDetail.getName().equals(userQueryFeatureInformation.getStartValue())) {
               facetDetail.setSelected(CheckType.YES);
               selectedFacetDetails.add(facetDetail);
               break;
            }
         }
      }
      return selectedFacetDetails;
   }

   private List<ExecueFacetDetail> getSelectedFacetDetailRanges (Long facetId, Long contextId,
            List<ExecueFacetDetail> facetDetails, List<UserQueryFeatureInformation> userQueryFeatureInformations,
            RIFeatureContent featureContent) throws UnstructuredWarehouseException {
      List<FeatureRange> featureRanges = getUnstructuredWarehouseRetrievalService().getFeatureRangesByFeatureId(
               contextId, facetId);
      List<FeatureRange> selectedFeatureRanges = getUnstructuredWarehouseHelper().getMatchingFeatureRanges(
               featureRanges, userQueryFeatureInformations, featureContent);
      List<ExecueFacetDetail> selectedFacetDetails = new ArrayList<ExecueFacetDetail>();
      if (ExecueCoreUtil.isCollectionNotEmpty(selectedFeatureRanges)) {
         for (ExecueFacetDetail execueFacetDetail : facetDetails) {
            for (FeatureRange range : selectedFeatureRanges) {
               if (execueFacetDetail.getName().equals(range.getRangeName())) {
                  execueFacetDetail.setSelected(CheckType.YES);
                  selectedFacetDetails.add(execueFacetDetail);
                  break;
               }
            }
         }
      }
      return selectedFacetDetails;
   }

   private List<ExecueFacetDetail> getSelectedExecueFacetDetailLocations (List<ExecueFacetDetail> facetDetails,
            List<FeatureValue> featureValues) {
      List<ExecueFacetDetail> selectedFacetDetails = new ArrayList<ExecueFacetDetail>();
      for (ExecueFacetDetail facetDetail : facetDetails) {
         for (FeatureValue featureValue : featureValues) {
            if (facetDetail.getName().equals(featureValue.getFeatureValue())) {
               facetDetail.setSelected(CheckType.YES);
               selectedFacetDetails.add(facetDetail);
               break;
            }
         }
      }
      return selectedFacetDetails;
   }

   private Set<Long> getFeatureBedIdsFromUserQueryLocationInformations (
            List<UserQueryLocationInformation> userQueryLocationInformations, Long modelId) throws KDXException {
      List<Long> locationBedIds = new ArrayList<Long>();
      for (UserQueryLocationInformation userQueryLocationInformation : userQueryLocationInformations) {
         locationBedIds.add(userQueryLocationInformation.getLocationBedId());
      }
      // Get the model group ids
      Set<Long> modelGroupIds = getModelGroupIds(getKdxRetrievalService().getUserModelGroupsByModelId(modelId));

      List<RISharedUserModelMapping> sharedUserModelMappings = getKdxRetrievalService().getRISharedUserModelMappings(
               locationBedIds, modelGroupIds);
      Set<Long> featureValueBedIds = new HashSet<Long>();
      for (RISharedUserModelMapping sharedUserModelMapping : sharedUserModelMappings) {
         featureValueBedIds.add(sharedUserModelMapping.getInstanceBedId());
      }

      return featureValueBedIds;
   }

   private void markExecueFacetsAsProminent (List<ExecueFacet> facets) {
      Integer maxProminentFacet = getUnstructuredWHConfigurationService().getMaxProminentFacet();
      for (int index = 0; index < maxProminentFacet; index++) {
         facets.get(index).setProminent(CheckType.YES);
      }
   }

   private FacetQueryInput buildFacetQueryInput (Long contextId, String selectedDefaultVicinityDistanceLimit,
            String imagePresent, List<UserQueryLocationInformation> userQueryLocationInformations,
            List<UserQueryFeatureInformation> userQueryFeatureInformations) {
      FacetQueryInput facetQueryInput = new FacetQueryInput();
      facetQueryInput.setDistance(getSQRTBasedDistanceLimit(Integer.valueOf(selectedDefaultVicinityDistanceLimit)));
      facetQueryInput.setImagePresent(ExecueBeanUtil.getCorrespondingCheckTypeValue(ExecueBeanUtil
               .getCorrespondingBooleanValue(imagePresent)));
      facetQueryInput.setLocationDetails(getLocationDetails(userQueryLocationInformations));
      facetQueryInput.setQueryConditions(userQueryFeatureInformations);
      return facetQueryInput;
   }

   private List<LocationDetail> getLocationDetails (List<UserQueryLocationInformation> userQueryLocationInformations) {
      List<LocationDetail> locationDetails = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(userQueryLocationInformations)) {
         locationDetails = new ArrayList<LocationDetail>();
         for (UserQueryLocationInformation userQueryLocationInformation : userQueryLocationInformations) {
            LocationDetail locationDetail = new LocationDetail(userQueryLocationInformation.getLongitude(),
                     userQueryLocationInformation.getLatitude());
            locationDetails.add(locationDetail);
         }
      }
      return locationDetails;
   }

   private Map<Long, List<UserQueryFeatureInformation>> populateUserQueryFeatureInformationMap (
            List<UserQueryFeatureInformation> userQueryFeatureInformations) {
      Map<Long, List<UserQueryFeatureInformation>> userQueryFeatureInformationsMap = new HashMap<Long, List<UserQueryFeatureInformation>>();
      for (UserQueryFeatureInformation userQueryFeatureInformation : userQueryFeatureInformations) {
         List<UserQueryFeatureInformation> tempUserQueryFeatureInformations = userQueryFeatureInformationsMap
                  .get(userQueryFeatureInformation.getFeatureId());
         if (ExecueCoreUtil.isCollectionNotEmpty(tempUserQueryFeatureInformations)) {
            tempUserQueryFeatureInformations.add(userQueryFeatureInformation);
         } else {
            tempUserQueryFeatureInformations = new ArrayList<UserQueryFeatureInformation>();
            tempUserQueryFeatureInformations.add(userQueryFeatureInformation);
            userQueryFeatureInformationsMap.put(userQueryFeatureInformation.getFeatureId(),
                     tempUserQueryFeatureInformations);
         }
      }
      return userQueryFeatureInformationsMap;
   }

   private List<String> getUserRequestedLocations (List<UserLocationInfo> userLocationInfos) {
      List<String> userRequestedLocations = new ArrayList<String>();
      if (ExecueCoreUtil.isCollectionNotEmpty(userLocationInfos)) {
         for (UserLocationInfo userLocationInfo : userLocationInfos) {
            userRequestedLocations.add(userLocationInfo.getLongitude() + "~~" + userLocationInfo.getLatitude());
         }

      }
      return userRequestedLocations;
   }

   private UserInput generateUserInput (UIUserInput userRequest) {
      UserInput userInput = new UserInput();
      userInput.setRequest(userRequest.getRequest());
      userInput.setUserQueryId(userRequest.getUserQueryId());
      userInput.setSearchFilter(userRequest.getSearchFilter());
      userInput.setPageSize(userRequest.getPageSize());
      userInput.setRequestedPage(userRequest.getRequestedPage());
      return userInput;
   }

   private UnstructuredSearchResultItem updateResultItem (UnstructuredSearchResultItem unstructuredResult) {

      String regex = getUnstructuredSearchConfigurationService().getUnwantedCharRegex();
      String trimmedShortDescription = SemantificationHelper.removeUnwantedCharacterFromDescription(unstructuredResult
               .getShortDescription(), regex);
      unstructuredResult.setName(trimmedShortDescription);
      unstructuredResult.setShortDescription(trimmedShortDescription);
      if (StringUtils.isEmpty(unstructuredResult.getImageUrl())) {
         unstructuredResult.setImageUrl("images/main/noImageCraigsList.png");
      }
      unstructuredResult.setContentDateString(getDriverHelper().formatUnstructuredContentDate(
               unstructuredResult.getContentDate()));
      return unstructuredResult;
   }

   /**
    * @param semanticPossibilities
    * @param applicationId
    * @param userQueryId
    * @return the List<UserLocationInfo> 
    * @throws LocationException
    */
   private List<UserLocationInfo> populateLocationInfoFromUserQuery (Set<SemanticPossibility> semanticPossibilities)
            throws LocationException {
      List<UserLocationInfo> userQueryLocationInformations = new ArrayList<UserLocationInfo>();

      if (CollectionUtils.isEmpty(semanticPossibilities)) {
         return userQueryLocationInformations;
      }

      List<LocationPointInfo> userQueryLocationPointInformations = getSemantificationHelper()
               .getLocationPointInformations(semanticPossibilities.iterator().next(), false);
      if (ExecueCoreUtil.isCollectionNotEmpty(userQueryLocationPointInformations)) {
         for (LocationPointInfo locationPointInfo : userQueryLocationPointInformations) {
            UserLocationInfo userLocationInfo = prepareUserLocationInfoFromLocationPointInfo(locationPointInfo);
            userQueryLocationInformations.add(userLocationInfo);
         }
      }
      return userQueryLocationInformations;
   }

   /**
    * @param userRemoteLocationInfo
    * @return UserLocationInfo
    * @throws SharedModelException 
    */
   private UserLocationInfo populateLocationInfoFromUserRemoteLocation (UserRemoteLocationInfo userRemoteLocationInfo)
            throws SharedModelException {
      UserLocationInfo userLocationInformation = null;
      String cityCenterZipCode = userRemoteLocationInfo.getCityCenterZipCode();
      if (!ExecueCoreUtil.isEmpty(cityCenterZipCode)) {
         userLocationInformation = getUserLocationInformationsByZipCode(cityCenterZipCode);
      }
      if (userLocationInformation != null) {
         String cityName = userRemoteLocationInfo.getCityName();
         if (!ExecueCoreUtil.isEmpty(cityName)) {
            List<Long> cityIds = getLocationSharedModelService().findMatchingCityInstance(cityName);
            if (!CollectionUtils.isEmpty(cityIds)) {
               if (cityIds.size() > 1) {
                  String stateName = userRemoteLocationInfo.getStateName();
                  if (!ExecueCoreUtil.isEmpty(stateName)) {
                     List<Long> stateIds = getLocationSharedModelService().findMatchingStateInstance(stateName);
                     if (!CollectionUtils.isEmpty(stateIds)) {
                        List<StateCity> validStateCityCombinations = getLocationRetrievalService()
                                 .getValidStateCityCombinations(stateIds, cityIds);

                        if (!CollectionUtils.isEmpty(validStateCityCombinations)
                                 && validStateCityCombinations.size() == 1) {
                           userLocationInformation = prepareUserLocationInfo(validStateCityCombinations.get(0)
                                    .getCityId());
                        }
                     }
                  }
               } else {
                  userLocationInformation = prepareUserLocationInfo(cityIds.get(0));
               }
            }
         }
      }
      return userLocationInformation;
   }

   /**
    * @param userQueryId
    * @param applicationId
    * @param cityIds
    * @return
    * @throws LocationException
    */
   private UserQueryLocationInformation prepareUserQueryLocationInfo (Long userQueryId, Long applicationId, Long cityId)
            throws LocationException {
      LocationPointInfo locationPointInfo = getLocationRetrievalService().getLocationPointInfoByLocationBedId(cityId);
      UserQueryLocationInformation userQueryLocationInformation = prepareUserQueryLocationInfoFromLocationPointInfo(
               locationPointInfo, applicationId, userQueryId);
      return userQueryLocationInformation;
   }

   private UserLocationInfo prepareUserLocationInfo (Long cityId) throws LocationException {
      LocationPointInfo locationPointInfo = getLocationRetrievalService().getLocationPointInfoByLocationBedId(cityId);
      UserLocationInfo userLocationInfo = prepareUserLocationInfoFromLocationPointInfo(locationPointInfo);
      return userLocationInfo;
   }

   public String prepareLocationDisplayName (List<UserLocationInfo> userLocationInformations) {
      List<String> locationDisplayNameList = new ArrayList<String>();
      for (UserLocationInfo userQueryLocationInformation : userLocationInformations) {
         locationDisplayNameList.add(userQueryLocationInformation.getLocationDisplayName());
      }
      return ExecueCoreUtil.joinCollection(locationDisplayNameList, ",");
   }

   public List<UserLocationInfo> populateLocationInfoFromLocationSuggestTerm (LocationSuggestTerm locationSuggestTerm) {
      List<UserLocationInfo> userLocationInformations = new ArrayList<UserLocationInfo>();
      UserLocationInfo userLocationInformation = new UserLocationInfo();
      userLocationInformation.setLocationId(locationSuggestTerm.getId());
      userLocationInformation.setLocationBedId(locationSuggestTerm.getLocationBedId());
      userLocationInformation.setLocationDisplayName(locationSuggestTerm.getDisplayName());
      userLocationInformation.setLatitude(locationSuggestTerm.getLatitude());
      userLocationInformation.setLongitude(locationSuggestTerm.getLongitude());
      userLocationInformations.add(userLocationInformation);
      return userLocationInformations;
   }

   private UserLocationInfo getUserLocationInformationsByZipCode (String userRequestZipCode) throws LocationException {
      LocationPointInfo locationPointInfo = getLocationRetrievalService().getLocationPointInfoByZipCode(
               userRequestZipCode);
      return prepareUserLocationInfoFromLocationPointInfo(locationPointInfo);
   }

   private String prepareSortingInfo (UniversalSearchResultFeatureHeader sortingInfo,
            List<UniversalSearchResultFeatureHeader> uiversalSearchResultFeatureHeaders, boolean fromPagination) {
      if (sortingInfo != null && !ExecueCoreUtil.isEmpty(sortingInfo.getColumnName())) {
         alterSortOrder(sortingInfo, uiversalSearchResultFeatureHeaders, fromPagination);
         return getUserRequestedSortOrder(sortingInfo);
      }
      return null;
   }

   private String getUserRequestedSortOrder (UniversalSearchResultFeatureHeader sortingInfo) {
      if (sortingInfo != null && !ExecueCoreUtil.isEmpty(sortingInfo.getColumnName())) {
         return sortingInfo.getColumnName() + "##" + sortingInfo.getDefaultSortOrder().getValue();
      }
      return null;
   }

   private void alterSortOrder (UniversalSearchResultFeatureHeader sortingInfo,
            List<UniversalSearchResultFeatureHeader> uiversalSearchResultFeatureHeaders, boolean fromPagination) {
      if (!fromPagination) {
         if (OrderEntityType.ASCENDING.equals(sortingInfo.getDefaultSortOrder())) {
            sortingInfo.setDefaultSortOrder(OrderEntityType.DESCENDING);
         } else {
            sortingInfo.setDefaultSortOrder(OrderEntityType.ASCENDING);
         }
         for (UniversalSearchResultFeatureHeader universalSearchResultFeatureHeader : uiversalSearchResultFeatureHeaders) {
            if (universalSearchResultFeatureHeader.getColumnName().equals(sortingInfo.getColumnName())) {
               universalSearchResultFeatureHeader.setDefaultSortOrder(sortingInfo.getDefaultSortOrder());
               break;
            }
         }
      }
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService
    *           the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   /**
    * @return the transactionIdGenerationService
    */
   public IUidService getTransactionIdGenerationService () {
      return transactionIdGenerationService;
   }

   /**
    * @param transactionIdGenerationService
    *           the transactionIdGenerationService to set
    */
   public void setTransactionIdGenerationService (IUidService transactionIdGenerationService) {
      this.transactionIdGenerationService = transactionIdGenerationService;
   }

   /**
    * @return the genericArticleResemantificationService
    */
   public IGenericArticleResemantificationService getGenericArticleResemantificationService () {
      return genericArticleResemantificationService;
   }

   /**
    * @param genericArticleResemantificationService
    *           the genericArticleResemantificationService to set
    */
   public void setGenericArticleResemantificationService (
            IGenericArticleResemantificationService genericArticleResemantificationService) {
      this.genericArticleResemantificationService = genericArticleResemantificationService;
   }

   /**
    * @return the genericArticleResemantificationThreadMessage
    */
   public IMessage getGenericArticleResemantificationThreadMessage () {
      return genericArticleResemantificationThreadMessage;
   }

   /**
    * @param genericArticleResemantificationThreadMessage
    *           the genericArticleResemantificationThreadMessage to set
    */
   public void setGenericArticleResemantificationThreadMessage (IMessage genericArticleResemantificationThreadMessage) {
      this.genericArticleResemantificationThreadMessage = genericArticleResemantificationThreadMessage;
   }

   public IUnstructuredWHFeatureContentLookupService getUnstructuredWHFeatureContentLookupService () {
      return unstructuredWHFeatureContentLookupService;
   }

   public void setUnstructuredWHFeatureContentLookupService (
            IUnstructuredWHFeatureContentLookupService unstructuredWHFeatureContentLookupService) {
      this.unstructuredWHFeatureContentLookupService = unstructuredWHFeatureContentLookupService;
   }

   /**
    * @return the unstructuredWHConfigurationService
    */
   public IUnstructuredWHConfigurationService getUnstructuredWHConfigurationService () {
      return unstructuredWHConfigurationService;
   }

   /**
    * @param unstructuredWHConfigurationService
    *           the unstructuredWHConfigurationService to set
    */
   public void setUnstructuredWHConfigurationService (
            IUnstructuredWHConfigurationService unstructuredWHConfigurationService) {
      this.unstructuredWHConfigurationService = unstructuredWHConfigurationService;
   }

   /**
    * @return the unstructuredFacetRetrievalService
    */
   public IUnstructuredFacetRetrievalService getUnstructuredFacetRetrievalService () {
      return unstructuredFacetRetrievalService;
   }

   /**
    * @param unstructuredFacetRetrievalService
    *           the unstructuredFacetRetrievalService to set
    */
   public void setUnstructuredFacetRetrievalService (
            IUnstructuredFacetRetrievalService unstructuredFacetRetrievalService) {
      this.unstructuredFacetRetrievalService = unstructuredFacetRetrievalService;
   }

   /**
    * @return the unstructuredWarehouseRetrievalService
    */
   public IUnstructuredWarehouseRetrievalService getUnstructuredWarehouseRetrievalService () {
      return unstructuredWarehouseRetrievalService;
   }

   /**
    * @param unstructuredWarehouseRetrievalService
    *           the unstructuredWarehouseRetrievalService to set
    */
   public void setUnstructuredWarehouseRetrievalService (
            IUnstructuredWarehouseRetrievalService unstructuredWarehouseRetrievalService) {
      this.unstructuredWarehouseRetrievalService = unstructuredWarehouseRetrievalService;
   }

   /**
    * @return the unstructuredWarehouseManagementService
    */
   public IUnstructuredWarehouseManagementService getUnstructuredWarehouseManagementService () {
      return unstructuredWarehouseManagementService;
   }

   /**
    * @param unstructuredWarehouseManagementService
    *           the unstructuredWarehouseManagementService to set
    */
   public void setUnstructuredWarehouseManagementService (
            IUnstructuredWarehouseManagementService unstructuredWarehouseManagementService) {
      this.unstructuredWarehouseManagementService = unstructuredWarehouseManagementService;
   }

   /**
    * @return the platformServicesConfigurationService
    */
   public IPlatformServicesConfigurationService getPlatformServicesConfigurationService () {
      return platformServicesConfigurationService;
   }

   /**
    * @param platformServicesConfigurationService
    *           the platformServicesConfigurationService to set
    */
   public void setPlatformServicesConfigurationService (
            IPlatformServicesConfigurationService platformServicesConfigurationService) {
      this.platformServicesConfigurationService = platformServicesConfigurationService;
   }

   public UnstructuredWarehouseHelper getUnstructuredWarehouseHelper () {
      return unstructuredWarehouseHelper;
   }

   public void setUnstructuredWarehouseHelper (UnstructuredWarehouseHelper unstructuredWarehouseHelper) {
      this.unstructuredWarehouseHelper = unstructuredWarehouseHelper;
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
    * @return the locationSharedModelService
    */
   public LocationSharedModelServiceImpl getLocationSharedModelService () {
      return locationSharedModelService;
   }

   /**
    * @param locationSharedModelService the locationSharedModelService to set
    */
   public void setLocationSharedModelService (LocationSharedModelServiceImpl locationSharedModelService) {
      this.locationSharedModelService = locationSharedModelService;
   }

}