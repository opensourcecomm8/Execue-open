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


package com.execue.uss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.unstructured.SemantifiedContentFeatureInformation;
import com.execue.core.common.bean.entity.unstructured.UserQueryFeatureInformation;
import com.execue.core.common.bean.entity.unstructured.UserQueryLocationInformation;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.qdata.UniversalSearchResultFeatureHeader;
import com.execue.core.common.bean.sdata.location.LocationPointInfo;
import com.execue.core.common.bean.security.UserRemoteLocationInfo;
import com.execue.core.common.bean.uss.UniversalUnstructuredSearchResult;
import com.execue.core.common.bean.uss.UnstructuredKeywordSearchInput;
import com.execue.core.common.bean.uss.UnstructuredSearchInput;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.NormalizedLocationType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.ExeCueException;
import com.execue.platform.configuration.IPlatformServicesConfigurationService;
import com.execue.sdata.exception.LocationException;
import com.execue.sdata.service.ILocationRetrievalService;
import com.execue.sus.helper.ReducedFormHelper;
import com.execue.sus.helper.SemanticUtil;
import com.execue.sus.helper.SemantificationHelper;
import com.execue.sus.service.IReducedFormOrganizationService;
import com.execue.uss.configuration.IUnstructuredSearchConfigurationService;
import com.execue.uss.helper.UnstructuredSearchHelper;
import com.execue.uss.service.IUnstructuredWarehouseSearchEngine;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.helper.UnstructuredWarehouseHelper;
import com.execue.uswh.service.IUnstructuredWHFeatureContentLookupService;
import com.execue.uswh.service.IUnstructuredWarehouseManagementService;
import com.execue.uswh.service.IUnstructuredWarehouseRetrievalService;

/**
 * @author Nitesh
 * @since April 26, 2011 : 11:45:11 AM
 */
public class UnstructuredWarehouseSearchEngineImpl implements IUnstructuredWarehouseSearchEngine {

   private Logger                                     logger = Logger
                                                                      .getLogger(UnstructuredWarehouseSearchEngineImpl.class);
   private ICoreConfigurationService                  coreConfigurationService;
   private IUnstructuredSearchConfigurationService    unstructuredSearchConfigurationService;
   private IUnstructuredWHFeatureContentLookupService unstructuredWHFeatureContentLookupService;
   private IReducedFormOrganizationService            reducedFormOrganizationService;
   private IUnstructuredWarehouseRetrievalService     unstructuredWarehouseRetrievalService;
   private IUnstructuredWarehouseManagementService    unstructuredWarehouseManagementService;
   private ReducedFormHelper                          reducedFormHelper;
   private SemantificationHelper                      semantificationHelper;
   private ILocationRetrievalService                  locationRetrievalService;
   private IPlatformServicesConfigurationService      platformServicesConfigurationService;
   private UnstructuredWarehouseHelper                unstructuredWarehouseHelper;

   public List<UniversalUnstructuredSearchResult> performWarehouseSearch (
            Set<SemanticPossibility> semanticPossibilities, UserRemoteLocationInfo userRemoteLocationInfo,
            Long userQueryId) {
      List<UniversalUnstructuredSearchResult> universalUnstructuredSearchResults = new ArrayList<UniversalUnstructuredSearchResult>(
               1);
      if (CollectionUtils.isEmpty(semanticPossibilities)) {
         return universalUnstructuredSearchResults;
      }

      try {
         Map<Long, SemanticPossibility> unstructuredSemanticPossibilityByAppId = new HashMap<Long, SemanticPossibility>(
                  1);
         for (SemanticPossibility possibility : semanticPossibilities) {
            Application application = possibility.getApplication();
            if (AppSourceType.UNSTRUCTURED == application.getSourceType()) {
               Long applicationId = application.getId();
               unstructuredSemanticPossibilityByAppId.put(applicationId, possibility);
            }
         }
         if (MapUtils.isEmpty(unstructuredSemanticPossibilityByAppId)) {
            return universalUnstructuredSearchResults;
         }

         // Get the feature information
         // TODO: NK: Should later perform the warehouse search using one db call instead for each app separately
         for (Entry<Long, SemanticPossibility> entry : unstructuredSemanticPossibilityByAppId.entrySet()) {
            Long applicationId = entry.getKey();
            SemanticPossibility semanticPossibility = entry.getValue();
            reducedFormOrganizationService.disintegrateInstanceProfiles(semanticPossibility);

            List<SemantifiedContentFeatureInformation> featureInformations = getSemantificationHelper()
                     .getSemantifiedContentFeaturesInformation(semanticPossibility, false);
            if (CollectionUtils.isEmpty(featureInformations)) {
               continue;
            }

            // Get and save the user query feature information
            Set<UserQueryFeatureInformation> userQueryFeatureInformations = getUnstructuredWarehouseHelper()
                     .populateUserQueryFeatureInformation(featureInformations, userQueryId, applicationId);

            if (!CollectionUtils.isEmpty(userQueryFeatureInformations)) {
               getUnstructuredWarehouseManagementService().saveUserQueryFeatureInformation(applicationId,
                        new ArrayList<UserQueryFeatureInformation>(userQueryFeatureInformations));
            }

            List<String> userRequestedLocations = new ArrayList<String>();
            boolean isLocationBased = getPlatformServicesConfigurationService().isApplicationHasLocationRealization(
                     applicationId);
            if (isLocationBased) {

               // Get the location point infos from the user query possibilities
               List<LocationPointInfo> locationPointInformations = getSemantificationHelper()
                        .getLocationPointInformations(semanticPossibility, false);

               // If empty, then get the default location point info
               if (CollectionUtils.isEmpty(locationPointInformations)) {
                  locationPointInformations = getDefaultUserLocationPointInfo();
               } else if (locationPointInformations.size() > 1) {
                  List<UserQueryLocationInformation> userQueryLocationInformations = UnstructuredWarehouseHelper
                           .populateUserQueryLocationInformations(locationPointInformations, applicationId, userQueryId);
                  getUnstructuredWarehouseManagementService().saveUserQueryLocationInformation(applicationId,
                           userQueryLocationInformations);
               }

               // Prepare the user requested location string
               userRequestedLocations = UnstructuredWarehouseHelper
                        .getDelimitedLocationStringFromLocationPointInfos(locationPointInformations);
            }

            if (applyKeyWordSearchFilter()) {
               // Populate the semantified content key word match table
               populateSemantifiedContentKeyWordMatch(semanticPossibility, userRequestedLocations, applicationId,
                        userQueryId);
            }

            // Match the user query to the udx featuer info and get the matched udx ids
            Page defaultPageDetail = getDefaultPageDetail();
            List<UniversalSearchResultFeatureHeader> uiversalSearchResultFeatureHeaders = SemanticUtil
                     .populateUiversalSearchResultFeatureHeader(getUnstructuredWHFeatureContentLookupService()
                              .getOrderedDisplayableRIFeatureContentByContextId(applicationId));
            // add semantic date and semaniic score
            uiversalSearchResultFeatureHeaders.addAll(getUnstructuredSearchConfigurationService()
                     .getUnstructuredSearchResultsHeaders());
            List<String> selectColumnNameList = getUnstructuredWHFeatureContentLookupService()
                     .getOrderedDisplayableColumnsByContextId(applicationId);
            List<String> searchResultOrder = getUnstructuredSearchConfigurationService().getSearchResultOrder(null);
            Integer userQueryFeatureCount = UnstructuredSearchHelper.getUserQueryFeatureCount(featureInformations);
            Integer userQueryRecordCount = UnstructuredSearchHelper.getUserQueryRecordCount(featureInformations);
            Integer distanceLimit = getUnstructuredSearchConfigurationService().getDefaultVicinityDistanceLimit();
            boolean imagePresent = false; // For page 2, set it to false for now
            boolean applyKeyWordSearchFilter = applyKeyWordSearchFilter();
            boolean applyPartialMatchFilter = applyPartialMatchFilter();
            boolean displayCloseMatchCount = getUnstructuredSearchConfigurationService().getDisplayClosedMatchCount();
            boolean useDbFunctionForMultipleLocationQuery = getUnstructuredSearchConfigurationService()
                     .getUseDbFunctionForMultipleLocationQuery();
            String dbFunctionNameForMultipleLocationQuery = getUnstructuredSearchConfigurationService()
                     .getDbFunctionNameForMultipleLocationQuery();

            // Prepare the input for the unstructured search
            UnstructuredSearchInput unstructuredSearchInput = UnstructuredSearchHelper.prepareUnstructuredSearchInput(
                     applicationId, userQueryId, userQueryFeatureCount, userQueryRecordCount, distanceLimit,
                     isLocationBased, imagePresent, applyKeyWordSearchFilter, applyPartialMatchFilter,
                     displayCloseMatchCount, searchResultOrder, userRequestedLocations, selectColumnNameList,
                     uiversalSearchResultFeatureHeaders, null, useDbFunctionForMultipleLocationQuery,
                     dbFunctionNameForMultipleLocationQuery);

            UniversalUnstructuredSearchResult universalUnstructuredSearchResult = getUnstructuredWarehouseRetrievalService()
                     .getUnstructuredSearchResult(applicationId, unstructuredSearchInput, defaultPageDetail);
            universalUnstructuredSearchResult.setApplicationId(applicationId);

            universalUnstructuredSearchResults.add(universalUnstructuredSearchResult);
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return universalUnstructuredSearchResults;
   }

   private List<LocationPointInfo> getDefaultUserLocationPointInfo () throws LocationException {
      String defaultUserPreferredZipCode = getUnstructuredSearchConfigurationService().getDefaultUserPreferredZipCode();
      List<String> tempUserRequestZipCodes = new ArrayList<String>();
      tempUserRequestZipCodes.add(defaultUserPreferredZipCode);
      List<LocationPointInfo> locationPointInfos = getLocationRetrievalService().getLocationPointsByZipCodes(
               tempUserRequestZipCodes, NormalizedLocationType.ZIPCODE);
      return locationPointInfos;
   }

   protected boolean applyKeyWordSearchFilter () {
      return getUnstructuredSearchConfigurationService().getApplyKeyWordSearchFilter();
   }

   protected boolean applyPartialMatchFilter () {
      return getUnstructuredSearchConfigurationService().getApplyPartialMatchFilter();
   }

   private void populateSemantifiedContentKeyWordMatch (SemanticPossibility semanticPossibility,
            List<String> userRequestedLocations, Long applicationId, Long userQueryId)
            throws UnstructuredWarehouseException {
      // TODO: NK: Later need to get the concept priority map for the given semantic possibility
      Map<Long, Integer> conceptPriorityMap = getConceptPriorityMap();
      String keyWordMatchText = getReducedFormHelper().getKeyWordMatchText(semanticPossibility, conceptPriorityMap);
      List<String> unrecognizedQueryWords = SemanticUtil.getUnrecognizedQueryWords(semanticPossibility);
      keyWordMatchText += " " + StringUtils.join(unrecognizedQueryWords, " ");
      Integer maxRecordCount = getUnstructuredSearchConfigurationService()
               .getDefaultSemantifiedContentKeywordMatchMaxRecordCount();
      Integer userQueryDistanceLimit = getUnstructuredSearchConfigurationService().getDefaultVicinityDistanceLimit();
      boolean imagePresent = false; // For page 2, set it to false for now
      boolean isLocationBased = getPlatformServicesConfigurationService().isApplicationHasLocationRealization(
               applicationId);

      boolean useDbFunctionForMultipleLocationQuery = getUnstructuredSearchConfigurationService()
               .getUseDbFunctionForMultipleLocationQuery();
      String dbFunctionNameForMultipleLocationQuery = getUnstructuredSearchConfigurationService()
               .getDbFunctionNameForMultipleLocationQuery();

      // Prepare the input for the keyword search
      UnstructuredKeywordSearchInput unstructuredKeywordSearchInput = UnstructuredSearchHelper
               .prepareKeywordSearchInput(userRequestedLocations, keyWordMatchText, applicationId, userQueryId,
                        userQueryDistanceLimit, maxRecordCount, isLocationBased, imagePresent,
                        useDbFunctionForMultipleLocationQuery, dbFunctionNameForMultipleLocationQuery);
      getUnstructuredWarehouseRetrievalService().populateSemantifiedContentKeyWordMatchResultForUserQuery(
               unstructuredKeywordSearchInput);
   }

   private Map<Long, Integer> getConceptPriorityMap () {
      Map<Long, Integer> conceptPriorityMap = new HashMap<Long, Integer>(1);
      // TODO: Later should pick up the priority map by the app or app category
      return conceptPriorityMap;
   }

   protected Page getDefaultPageDetail () {
      Page page = new Page();
      Long pageSize = Long.valueOf(getPageSizeFromConfiguration());
      page.setNumberOfLinks(pageSize);
      page.setPageSize(pageSize);
      page.setRequestedPage(1L);
      return page;
   }

   private int getPageSizeFromConfiguration () {
      return getCoreConfigurationService().getResultsPageSize();
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

   public SemantificationHelper getSemantificationHelper () {
      return semantificationHelper;
   }

   public void setSemantificationHelper (com.execue.sus.helper.SemantificationHelper semantificationHelper) {
      this.semantificationHelper = semantificationHelper;
   }

   public IReducedFormOrganizationService getReducedFormOrganizationService () {
      return reducedFormOrganizationService;
   }

   public void setReducedFormOrganizationService (IReducedFormOrganizationService reducedFormOrganizationService) {
      this.reducedFormOrganizationService = reducedFormOrganizationService;
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

   public IUnstructuredWHFeatureContentLookupService getUnstructuredWHFeatureContentLookupService () {
      return unstructuredWHFeatureContentLookupService;
   }

   public void setUnstructuredWHFeatureContentLookupService (
            IUnstructuredWHFeatureContentLookupService unstructuredWHFeatureContentLookupService) {
      this.unstructuredWHFeatureContentLookupService = unstructuredWHFeatureContentLookupService;
   }

   /**
    * @return the unstructuredWarehouseRetrievalService
    */
   public IUnstructuredWarehouseRetrievalService getUnstructuredWarehouseRetrievalService () {
      return unstructuredWarehouseRetrievalService;
   }

   /**
    * @param unstructuredWarehouseRetrievalService the unstructuredWarehouseRetrievalService to set
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
    * @param unstructuredWarehouseManagementService the unstructuredWarehouseManagementService to set
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
    * @param platformServicesConfigurationService the platformServicesConfigurationService to set
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

}
