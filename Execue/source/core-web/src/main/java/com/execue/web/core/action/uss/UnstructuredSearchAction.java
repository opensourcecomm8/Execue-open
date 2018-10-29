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


package com.execue.web.core.action.uss;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.execue.core.common.bean.ExecueFacet;
import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.RelatedUserQuery;
import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.UnstructuredQueryResult;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.unstructured.UserLocationInfo;
import com.execue.core.common.bean.entity.unstructured.UserPreference;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.qdata.UniversalSearchResultFeatureHeader;
import com.execue.core.common.bean.sdata.location.LocationSuggestTerm;
import com.execue.core.common.bean.uss.UniversalUnstructuredSearchResult;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.SearchFilterType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.Response;
import com.execue.handler.bean.UIUserInput;
import com.execue.handler.uss.IUnstructuredSearchServiceHandler;
import com.execue.handler.uss.impl.UnstructuredSearchServiceHandlerImpl;

public class UnstructuredSearchAction extends UnstructuredSearchPaginationAction {

   private static final long                  serialVersionUID                     = 1L;
   private static final Logger                logger                               = Logger
                                                                                            .getLogger(UnstructuredSearchAction.class);

   private UniversalUnstructuredSearchResult  universalSearchResult;
   private Long                               applicationId;
   private Long                               modelId;
   private Long                               transactionId;
   private Long                               messageId;
   private Long                               userQueryId;
   private String                             userQuery;
   private UnstructuredQueryResult            result;
   private IUnstructuredSearchServiceHandler  unstructuredSearchServiceHandler;
   private Integer                            userQueryFeatureCount;
   private Integer                            userQueryRecordCount;
   private int                                recordCount;
   private int                                perfectMatchCount;
   private int                                mightMatchCount;
   private int                                partialMatchCount;
   private String                             selectedDefaultVicinityDistanceLimit = "100";
   private String                             imagePresent                         = "false";
   private UniversalSearchResultFeatureHeader sortingInfo;
   private CheckType                          keyWordBasedResults                  = CheckType.NO;
   private List<RelatedUserQuery>             relatedUserQueries                   = new ArrayList<RelatedUserQuery>();
   //  private List<String>                       userRequestZipCodes                  = new ArrayList<String>(1);
   private LocationSuggestTerm                locationSuggestTerm;
   private String                             applicationName;
   private boolean                            locationBased                        = false;
   private String                             fromResemantification                = "N";
   private boolean                            fromPagination                       = false;
   //suggest location terms
   private List<LocationSuggestTerm>          suggestLocationTerms;
   private String                             search;
   private List<ExecueFacet>                  facets;
   //This is dummy variable similar to search search action to avoid setter issue in case of single app redirection
   private String                             request;

   // Action Method
   public String performUnstructuredSearchByUserQuery () {
      if (logger.isDebugEnabled()) {
         logger.debug("Unstructure Search By User Query");
      }
      try {
         if (result != null && result.isRedirectURLPresent()) {
            userQuery = result.getOriginalQuery();
            applicationId = result.getApplicationId();
         } else {
            Response response = processUserRequest();
            result = (UnstructuredQueryResult) response.getObject();
         }

         result.setApplicationId(getApplicationId());
         result.setUserQueryDistanceLimit(Integer.valueOf(getSelectedDefaultVicinityDistanceLimit()));
         result.setApplyImagePresentFilter(Boolean.valueOf(getImagePresent()));

         userQueryId = result.getId();
         relatedUserQueries = result.getRelatedUserQueries();

         // Populate the user query feature information
         getUnstructuredSearchServiceHandler().saveUserQueryFeatureInformation(result, applicationId);

         // Populate the user query location information

         if (getUnstructuredSearchServiceHandler().isLocationBased(applicationId)) {
            // Get the user remote location information
            UserPreference userPreference = getUnstructuredSearchServiceHandler().populateUserPreference(
                     ServletActionContext.getRequest(), getHttpSession());

            getUnstructuredSearchServiceHandler().saveUserQueryLocationInformation(result, getLocationSuggestTerm(),
                     userPreference);
         }

         // Populate the Semantified Content Key Word Match
         UserPreference userPreferenceFromSession = (UserPreference) getHttpSession().get(
                  UnstructuredSearchServiceHandlerImpl.USER_PREFERENCE);
         if (!result.isUserQueryInfoSaved()) {
            getUnstructuredSearchServiceHandler().populateSemantifiedContentKeywordMatch(result,
                     userPreferenceFromSession.getUserLocationInfos(), false);
         }

         // TODO: NK: Should first set these values in the SemanticDriver, then only can get it here in the redirect
         // flow
         userQueryFeatureCount = result.getUserQueryFeatureCount();
         userQueryRecordCount = result.getUserQueryRecordCount();
         // set Application Info
         setApplicationInfo(result.getSemanticPossibilities());
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String updateUnstructuredSearchOptions () {
      UserPreference userPreference = (UserPreference) getHttpSession().get(
               UnstructuredSearchServiceHandlerImpl.USER_PREFERENCE);
      if (locationSuggestTerm != null && locationSuggestTerm.getId() > 0) {
         List<UserLocationInfo> locationInfos = getUnstructuredSearchServiceHandler()
                  .populateLocationInfoFromLocationSuggestTerm(locationSuggestTerm);
         userPreference.setUserLocationInfos(locationInfos);
      }
      result = new UnstructuredQueryResult();
      if (userPreference.getUserLocationInfos().size() > 1) {
         result.setMultipleLocation(true);
      }
      result.setLocationDisplayName(getUnstructuredSearchServiceHandler().prepareLocationDisplayName(
               userPreference.getUserLocationInfos()));

      return SUCCESS;
   }

   @Override
   public String processPage () throws ExeCueException {
      if (logger.isDebugEnabled()) {
         logger.debug("Process Page");
      }
      Page page = getPageDetail();
      page.setPageSize(getUnstructuredSearchServiceHandler().getUnstructuredResultPageSize());
      page.setNumberOfLinks(getUnstructuredSearchServiceHandler().getUnstructuredResultNumberOfLinks());
      page.setRecordCount(Long.valueOf(recordCount));
      //
      UserPreference userPreference = (UserPreference) getHttpSession().get(
               UnstructuredSearchServiceHandlerImpl.USER_PREFERENCE);
      universalSearchResult = getUnstructuredSearchServiceHandler().getUniversalSearchResult(applicationId,
               userQueryId, getUserQueryFeatureCount(), getUserQueryRecordCount(),
               userPreference.getUserLocationInfos(), page, Integer.valueOf(getSelectedDefaultVicinityDistanceLimit()),
               Boolean.valueOf(getImagePresent()), sortingInfo, fromPagination);
      // Get counts from UI Cache
      if (universalSearchResult.getTotalCount() == 0) {
         universalSearchResult.setTotalCount(recordCount);
         universalSearchResult.setPerfectMatchCount(perfectMatchCount);
         universalSearchResult.setMightMatchCount(mightMatchCount);
         universalSearchResult.setPartialMatchCount(partialMatchCount);
      }
      getHttpRequest().put(PAGINATION, getPageDetail());
      return SUCCESS;
   }

   public String getExecueFacets () {
      if (logger.isDebugEnabled()) {
         logger.debug("Get facets");
      }
      try {
         UserPreference userPreference = (UserPreference) getHttpSession().get(
                  UnstructuredSearchServiceHandlerImpl.USER_PREFERENCE);
         facets = getUnstructuredSearchServiceHandler().retrieveFacets(applicationId, modelId,
                  selectedDefaultVicinityDistanceLimit, imagePresent, userQueryId,
                  userPreference.getUserLocationInfos());
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String performUnstructuredSearchByFacet () {
      if (logger.isDebugEnabled()) {
         logger.debug("Unstructure Search By Facet");
      }
      try {
         result = new UnstructuredQueryResult();
         result.setUserQueryFeatureCount(userQueryFeatureCount);
         result.setUserQueryRecordCount(userQueryRecordCount);
         result.setApplicationId(getApplicationId());
         result.setUserQueryDistanceLimit(Integer.valueOf(getSelectedDefaultVicinityDistanceLimit()));
         result.setApplyImagePresentFilter(Boolean.valueOf(getImagePresent()));
         userQueryId = getUnstructuredSearchServiceHandler().getQueryId();
         result.setId(userQueryId);
         getUnstructuredSearchServiceHandler().prepareAndPersistUserQueryFeatureInformationsFromFacets(result, facets);

         // Populate the user query location information
         if (getUnstructuredSearchServiceHandler().isLocationBased(applicationId)) {
            // Get the user remote location information
            UserPreference userPreference = getUnstructuredSearchServiceHandler().populateUserPreference(
                     ServletActionContext.getRequest(), getHttpSession());
            getUnstructuredSearchServiceHandler().prepareAndPersistUserQueryLocationInformationsFromFacets(facets,
                     result, locationSuggestTerm, userPreference, modelId);
         }

         userQueryFeatureCount = result.getUserQueryFeatureCount();
         userQueryRecordCount = result.getUserQueryRecordCount();
      } catch (HandlerException e) {
         e.printStackTrace();
      }

      return SUCCESS;
   }

   public List<String> getDefaultVicinityDistanceLimits () {
      return getUnstructuredSearchServiceHandler().getdefaultVicinityDistanceLimits();
   }

   public String suggestLocations () {
      try {
         setSuggestLocationTerms(getUnstructuredSearchServiceHandler().suggestLocationBySearchString(search));
      } catch (HandlerException handlerException) {
         logger.error(handlerException);
      }
      return SUCCESS;

   }

   public String performResemantification () {

      try {
         // STEP 1: Run the NLP
         Response response = processUserRequest();
         result = (UnstructuredQueryResult) response.getObject();
         result.setUserQueryDistanceLimit(Integer.valueOf(getSelectedDefaultVicinityDistanceLimit()));
         result.setApplyImagePresentFilter(Boolean.valueOf(getImagePresent()));
         result.setApplicationId(getApplicationId());

         // Populate the user query location information
         if (getUnstructuredSearchServiceHandler().isLocationBased(applicationId)) {
            // Get the user remote location information
            UserPreference userPreference = getUnstructuredSearchServiceHandler().populateUserPreference(
                     ServletActionContext.getRequest(), getHttpSession());
            getUnstructuredSearchServiceHandler().saveUserQueryLocationInformation(result, getLocationSuggestTerm(),
                     userPreference);
         }

         // STEP 2: Populate the Semantified Content Key Word Match and Mark the semantified content for
         // re-semantification
         UserPreference userPreferenceFromSession = (UserPreference) getHttpSession().get(
                  UnstructuredSearchServiceHandlerImpl.USER_PREFERENCE);
         getUnstructuredSearchServiceHandler().populateSemantifiedContentKeywordMatch(result,
                  userPreferenceFromSession.getUserLocationInfos(), true);

         // STEP 3: Perform the quick re-semantification
         transactionId = result.getId();
         messageId = getUnstructuredSearchServiceHandler().performQuickResemantification(applicationId, transactionId);

         // STEP 4: Populate the user query feature information and the location information
         // Again reset the query id and populate the Semantified Content Key Word Match
         result.setId(getUnstructuredSearchServiceHandler().getQueryId());
         userQueryId = result.getId();
         relatedUserQueries = result.getRelatedUserQueries();

         // Populate the user query feature information
         getUnstructuredSearchServiceHandler().saveUserQueryFeatureInformation(result, applicationId);

         // Populate the user query location information
         // TODO:NK: Check if we can avoid this save, as its already saved before re-semantification
         if (getUnstructuredSearchServiceHandler().isLocationBased(applicationId)) {
            // Get the user remote location information
            UserPreference userPreference = getUnstructuredSearchServiceHandler().populateUserPreference(
                     ServletActionContext.getRequest(), getHttpSession());

            getUnstructuredSearchServiceHandler().saveUserQueryLocationInformation(result, getLocationSuggestTerm(),
                     userPreference);
         }

         // Populate the Semantified Content Key Word Match
         getUnstructuredSearchServiceHandler().populateSemantifiedContentKeywordMatch(result,
                  userPreferenceFromSession.getUserLocationInfos(), false);

         userQueryFeatureCount = result.getUserQueryFeatureCount();
         userQueryRecordCount = result.getUserQueryRecordCount();
         // set Application Info
         setApplicationInfo(result.getSemanticPossibilities());

      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   private Response processUserRequest () throws HandlerException {
      SearchFilter searchFilter = new SearchFilter();
      if (applicationId != null && applicationId != -1) {
         searchFilter.setId(applicationId);
         searchFilter.setSearchFilterType(SearchFilterType.APP);
      }
      UIUserInput userInput = new UIUserInput();
      userInput.setSearchFilter(searchFilter);
      userInput.setRequest(userQuery);
      userInput.setUserPossibilities(userInput.getUserPossibilities());
      Response response = getUnstructuredSearchServiceHandler().processRequest(userInput);
      return response;
   }

   private void setApplicationInfo (Set<SemanticPossibility> semanticPossibilities) {
      if (CollectionUtils.isNotEmpty(semanticPossibilities)) {
         for (SemanticPossibility semanticPossibility : semanticPossibilities) {
            setApplicationId(semanticPossibility.getApplication().getId());
            setApplicationName(semanticPossibility.getApplication().getName());
            Model model = semanticPossibility.getModel();
            if (model != null) {
               setModelId(model.getId());
            }
         }
      }
   }

   /**
    * @return the universalSearchResult
    */
   public UniversalUnstructuredSearchResult getUniversalSearchResult () {
      return universalSearchResult;
   }

   /**
    * @param universalSearchResult
    *           the universalSearchResult to set
    */
   public void setUniversalSearchResult (UniversalUnstructuredSearchResult universalSearchResult) {
      this.universalSearchResult = universalSearchResult;
   }

   /**
    * @return the unstructuredSearchServiceHandler
    */
   public IUnstructuredSearchServiceHandler getUnstructuredSearchServiceHandler () {
      return unstructuredSearchServiceHandler;
   }

   /**
    * @param unstructuredSearchServiceHandler
    *           the unstructuredSearchServiceHandler to set
    */
   public void setUnstructuredSearchServiceHandler (IUnstructuredSearchServiceHandler unstructuredSearchServiceHandler) {
      this.unstructuredSearchServiceHandler = unstructuredSearchServiceHandler;
   }

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId
    *           the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   /**
    * @return the userQuery
    */
   public String getUserQuery () {
      return userQuery;
   }

   /**
    * @param userQuery
    *           the userQuery to set
    */
   public void setUserQuery (String userQuery) {
      this.userQuery = userQuery;
   }

   /**
    * @return the result
    */
   public UnstructuredQueryResult getResult () {
      return result;
   }

   /**
    * @param result
    *           the result to set
    */
   public void setResult (UnstructuredQueryResult result) {
      this.result = result;
   }

   public Long getUserQueryId () {
      return userQueryId;
   }

   public void setUserQueryId (Long userQueryId) {
      this.userQueryId = userQueryId;
   }

   public String getSelectedDefaultVicinityDistanceLimit () {
      return selectedDefaultVicinityDistanceLimit;
   }

   public void setSelectedDefaultVicinityDistanceLimit (String selectedDefaultVicinityDistanceLimit) {
      this.selectedDefaultVicinityDistanceLimit = selectedDefaultVicinityDistanceLimit;
   }

   public String getImagePresent () {
      return imagePresent;
   }

   public void setImagePresent (String imagePresent) {
      this.imagePresent = imagePresent;
   }

   /**
    * @return the keyWordBasedResults
    */
   public CheckType getKeyWordBasedResults () {
      return keyWordBasedResults;
   }

   /**
    * @param keyWordBasedResults
    *           the keyWordBasedResults to set
    */
   public void setKeyWordBasedResults (CheckType keyWordBasedResults) {
      this.keyWordBasedResults = keyWordBasedResults;
   }

   /**
    * @return the relatedUserQueries
    */
   public List<RelatedUserQuery> getRelatedUserQueries () {
      return relatedUserQueries;
   }

   /**
    * @param relatedUserQueries
    *           the relatedUserQueries to set
    */
   public void setRelatedUserQueries (List<RelatedUserQuery> relatedUserQueries) {
      this.relatedUserQueries = relatedUserQueries;
   }

   /**
    * @return the recordCount
    */
   public int getRecordCount () {
      return recordCount;
   }

   /**
    * @param recordCount
    *           the recordCount to set
    */
   public void setRecordCount (int recordCount) {
      this.recordCount = recordCount;
   }

   /**
    * @return the transactionId
    */
   public Long getTransactionId () {
      return transactionId;
   }

   /**
    * @param transactionId
    *           the transactionId to set
    */
   public void setTransactionId (Long transactionId) {
      this.transactionId = transactionId;
   }

   /**
    * @return the messageId
    */
   public Long getMessageId () {
      return messageId;
   }

   /**
    * @param messageId
    *           the messageId to set
    */
   public void setMessageId (Long messageId) {
      this.messageId = messageId;
   }

   public Long getModelId () {
      return modelId;
   }

   public void setModelId (Long modelId) {
      this.modelId = modelId;
   }

   public String getApplicationName () {
      return applicationName;
   }

   public void setApplicationName (String applicationName) {
      this.applicationName = applicationName;
   }

   /**
    * @return the isLocationBased
    */
   public boolean isLocationBased () {
      return locationBased;
   }

   /**
    * @param isLocationBased
    *           the isLocationBased to set
    */
   public void setLocationBased (boolean isLocationBased) {
      this.locationBased = isLocationBased;
   }

   public String getFromResemantification () {
      return fromResemantification;
   }

   public void setFromResemantification (String fromResemantification) {
      this.fromResemantification = fromResemantification;
   }

   /**
   * @return the perfectMatchCount
   */
   public int getPerfectMatchCount () {
      return perfectMatchCount;
   }

   /**
   * @param perfectMatchCount the perfectMatchCount to set
   */
   public void setPerfectMatchCount (int perfectMatchCount) {
      this.perfectMatchCount = perfectMatchCount;
   }

   /**
   * @return the mightMatchCount
   */
   public int getMightMatchCount () {
      return mightMatchCount;
   }

   /**
   * @param mightMatchCount the mightMatchCount to set
   */
   public void setMightMatchCount (int mightMatchCount) {
      this.mightMatchCount = mightMatchCount;
   }

   /**
   * @return the partialMatchCount
   */
   public int getPartialMatchCount () {
      return partialMatchCount;
   }

   /**
   * @param partialMatchCount the partialMatchCount to set
   */
   public void setPartialMatchCount (int partialMatchCount) {
      this.partialMatchCount = partialMatchCount;
   }

   /**
    * @return the sortingInfo
    */
   public UniversalSearchResultFeatureHeader getSortingInfo () {
      return sortingInfo;
   }

   /**
    * @param sortingInfo the sortingInfo to set
    */
   public void setSortingInfo (UniversalSearchResultFeatureHeader sortingInfo) {
      this.sortingInfo = sortingInfo;
   }

   /**
    * @return the fromPagination
    */
   public boolean isFromPagination () {
      return fromPagination;
   }

   /**
    * @param fromPagination the fromPagination to set
    */
   public void setFromPagination (boolean fromPagination) {
      this.fromPagination = fromPagination;
   }

   /**
    * @return the userQueryFeatureCount
    */
   public Integer getUserQueryFeatureCount () {
      return userQueryFeatureCount;
   }

   /**
    * @param userQueryFeatureCount the userQueryFeatureCount to set
    */
   public void setUserQueryFeatureCount (Integer userQueryFeatureCount) {
      this.userQueryFeatureCount = userQueryFeatureCount;
   }

   /**
    * @return the userQueryResultCount
    */
   public Integer getUserQueryRecordCount () {
      return userQueryRecordCount;
   }

   /**
    * @param userQueryRecordCount the userQueryResultCount to set
    */
   public void setUserQueryRecordCount (Integer userQueryRecordCount) {
      this.userQueryRecordCount = userQueryRecordCount;
   }

   /**
    * @return the search
    */
   public String getSearch () {
      return search;
   }

   /**
    * @param search the search to set
    */
   public void setSearch (String search) {
      this.search = search;
   }

   /**
    * @return the suggestLocationTerms
    */
   public List<LocationSuggestTerm> getSuggestLocationTerms () {
      return suggestLocationTerms;
   }

   /**
    * @param suggestLocationTerms the suggestLocationTerms to set
    */
   public void setSuggestLocationTerms (List<LocationSuggestTerm> suggestLocationTerms) {
      this.suggestLocationTerms = suggestLocationTerms;
   }

   /**
    * @return the locationSuggestTerm
    */
   public LocationSuggestTerm getLocationSuggestTerm () {
      return locationSuggestTerm;
   }

   /**
    * @param locationSuggestTerm the locationSuggestTerm to set
    */
   public void setLocationSuggestTerm (LocationSuggestTerm locationSuggestTerm) {
      this.locationSuggestTerm = locationSuggestTerm;
   }

   /**
    * @return the facets
    */
   public List<ExecueFacet> getFacets () {
      return facets;
   }

   /**
    * @param facets the facets to set
    */
   public void setFacets (List<ExecueFacet> facets) {
      this.facets = facets;
   }

   /**
    * @return the request
    */
   public String getRequest () {
      return request;
   }

   /**
    * @param request the request to set
    */
   public void setRequest (String request) {
      this.request = request;
   }

}