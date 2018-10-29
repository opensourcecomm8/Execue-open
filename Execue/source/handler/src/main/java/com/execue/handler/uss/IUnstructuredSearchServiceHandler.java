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


package com.execue.handler.uss;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.execue.core.common.bean.ExecueFacet;
import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.UnstructuredQueryResult;
import com.execue.core.common.bean.entity.unstructured.UserLocationInfo;
import com.execue.core.common.bean.entity.unstructured.UserPreference;
import com.execue.core.common.bean.qdata.UniversalSearchResultFeatureHeader;
import com.execue.core.common.bean.sdata.location.LocationSuggestTerm;
import com.execue.core.common.bean.uss.UniversalUnstructuredSearchResult;
import com.execue.core.exception.HandlerException;
import com.execue.handler.IRequestResponseHandler;

/**
 * @author jitendra
 */
public interface IUnstructuredSearchServiceHandler extends IRequestResponseHandler {

   public Long performQuickResemantification (Long applicationId, Long transactionId) throws HandlerException;

   public UniversalUnstructuredSearchResult getUniversalSearchResult (Long applicationId, Long userQueryId,
            Integer userQueryFeatureCount, Integer userQueryRecordCount, List<UserLocationInfo> userLocationInfos,
            Page page, Integer distanceLimit, boolean imagePresent, UniversalSearchResultFeatureHeader sortingInfo,
            boolean fromPagination) throws HandlerException;

   public Long getUnstructuredResultPageSize ();

   public Long getUnstructuredResultNumberOfLinks ();

   public List<String> getdefaultVicinityDistanceLimits ();

   public Integer getDefaultVicinityDistanceLimit ();

   public String getUserRemoteLocationUrl ();

   public String getUserRemoteLocationReadTimeout ();

   public String getUserRemoteLocationConnectTimeout ();

   public String getDefaultUserPreferredZipCode ();

   public Long getQueryId ();

   public void saveUserQueryFeatureInformation (UnstructuredQueryResult queryResult, Long applicationId)
            throws HandlerException;

   public void saveUserQueryLocationInformation (UnstructuredQueryResult queryResult,
            LocationSuggestTerm locationSuggestTerm, UserPreference userPreference) throws HandlerException;

   public boolean isLocationBased (Long applicationId);

   public void populateSemantifiedContentKeywordMatch (UnstructuredQueryResult queryResult,
            List<UserLocationInfo> userLocationInfos, boolean isForResemantification) throws HandlerException;

   public List<LocationSuggestTerm> suggestLocationBySearchString (String search) throws HandlerException;

   public List<ExecueFacet> retrieveFacets (Long contextId, Long modelId, String selectedDefaultVicinityDistanceLimit,
            String imagePresent, Long userQueryId, List<UserLocationInfo> userLocationInfos) throws HandlerException;

   public void prepareAndPersistUserQueryFeatureInformationsFromFacets (UnstructuredQueryResult queryResult,
            List<ExecueFacet> facets) throws HandlerException;

   public void prepareAndPersistUserQueryLocationInformationsFromFacets (List<ExecueFacet> facets,
            UnstructuredQueryResult queryResult, LocationSuggestTerm locationSuggestTerm,
            UserPreference userPreference, Long modelId) throws HandlerException;

   public List<UserLocationInfo> populateLocationInfoFromLocationSuggestTerm (LocationSuggestTerm locationSuggestTerm);

   public String prepareLocationDisplayName (List<UserLocationInfo> userLocationInformations);

   public UserPreference populateUserPreference (HttpServletRequest httpServletRequest, Map httpSession)
            throws HandlerException;

}
