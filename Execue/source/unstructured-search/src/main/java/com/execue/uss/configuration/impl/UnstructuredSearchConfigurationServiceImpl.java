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
package com.execue.uss.configuration.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.qdata.UniversalSearchResultFeatureHeader;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.DisplayableFeatureAlignmentType;
import com.execue.core.common.type.FeatureValueType;
import com.execue.core.common.type.OrderEntityType;
import com.execue.core.configuration.IConfiguration;
import com.execue.uss.configuration.IUnstructuredSearchConfigurationService;

/**
 * @author Nitesh
 */
public class UnstructuredSearchConfigurationServiceImpl implements IUnstructuredSearchConfigurationService {

   private static final String                      NAME                                                                 = "unstructuredSearchConfigurationService";
   private IConfiguration                           unstructuredSearchConfiguration;

   /* Configuration Constants */
   private static final String                      DISPLAY_CLOSE_MATCH_COUNT                                            = "unstructured-search.static-values.displayCloseMatchCount";
   private static final String                      PERFORM_COMPLETE_KEYWORD_BASED_SEARCH                                = "unstructured-search.static-values.performCompleteKeyWordBasedSearch";
   private static final String                      SPECIAL_CHAR_TRIM_PATTERN_FOR_TITLE                                  = "unstructured-search.static-values.special-char-trim-pattern-for-title";
   private static final String                      APPLY_KEY_WORD_SEARCH_FILTER                                         = "unstructured-search.static-values.apply-key-word-search-filter";
   private static final String                      APPLY_PARTIAL_MATCH_FILTER                                           = "unstructured-search.static-values.apply-partial-match-filter";
   private static final String                      UNSTRUCTURED_RESULT_NUMBER_OF_LINKS                                  = "unstructured-search.static-values.numberOfLinks";
   private static final String                      UNSTRUCTURED_RESULT_PAGE_SIZE                                        = "unstructured-search.static-values.pageSize";
   private static final String                      DEFAULT_VICINITY_DISTANCE_LIMIT                                      = "unstructured-search.static-values.defaultVicinityDistanceLimit";
   private static final String                      DEFAULT_VICINITY_DISTANCE_LIMITS                                     = "unstructured-search.static-values.defaultVicinityDistanceLimits";
   private static final String                      DEFAULT_USER_PREFERRED_ZIP_CODE                                      = "unstructured-search.static-values.defaultUserPreferredZipCode";
   private static final String                      DEFAULT_SEMANTIFIED_CONTENT_KEYWORD_MATCH_MAX_RECORD_COUNT           = "unstructured-search.static-values.defaultSemantifiedContentKeywordMatchMaxRecordCount";
   private static final String                      RESEMANTIFICATION_SEMANTIFIED_CONTENT_KEYWORD_MATCH_MAX_RECORD_COUNT = "unstructured-search.static-values.resemantificationSemantifiedContentKeywordMatchMaxRecordCount";

   private static final String                      RESULT_ORDER_RESULT_ORDER                                            = "unstructured-search.SearchResultOrder";
   private static final String                      RESULT_HEADERS                                                       = "unstructured-search.resultHeaders";
   private static final String                      RESEMANTIFICATION_CHECKBOX_VISIBILITY                                = "unstructured-search.static-values.resemantification-checkbox-visibility";
   private static final String                      UNSTRUCTURED_SEARCH_RESULT_HEADERS                                   = "unstructured-search.unstructuredSearchResultHeaders";
   private static final String                      UNSTRUCTURED_SUGGESTION_RETRIEVAL_LIMIT                              = "unstructured-search.static-values.suggestion-retrieval-limit";
   private static final String                      USE_DB_FUNCTION_FOR_MULTIPLE_LOCATION_QUERY                          = "unstructured-search.static-values.use-db-function-for-multiple-location-query";
   private static final String                      DB_FUNCTION_NAME_FOR_MULTIPLE_LOCATION_QUERY                         = "unstructured-search.static-values.db-function-name-for-multiple-location";
   /* Members */
   private List<String>                             searchResultPreOrder                                                 = new ArrayList<String>(
                                                                                                                                  1);
   private List<String>                             searchResultPostOrder                                                = new ArrayList<String>(
                                                                                                                                  1);
   private String                                   searchResultDistanceOrder                                            = "DISTANCE##ASC";

   private List<UniversalSearchResultFeatureHeader> searchResultFeatureHeaders                                           = new ArrayList<UniversalSearchResultFeatureHeader>(
                                                                                                                                  1);
   private List<String>                             defaultVicinityDistanceLimits                                        = new ArrayList<String>();

   private List<UniversalSearchResultFeatureHeader> unstructuredSearchResultsHeaders                                     = new ArrayList<UniversalSearchResultFeatureHeader>();

   public List<String> getSearchResultOrder (String userRequestedSearchResultOrder) {
      List<String> searchOrderClause = new ArrayList<String>();
      searchOrderClause.addAll(searchResultPreOrder);
      if (StringUtils.isEmpty(userRequestedSearchResultOrder)) {
         searchOrderClause.add(searchResultDistanceOrder);
      } else {
         searchOrderClause.add(userRequestedSearchResultOrder);
      }
      searchOrderClause.addAll(searchResultPostOrder);
      if (!StringUtils.isEmpty(userRequestedSearchResultOrder)) {
         searchOrderClause.add(searchResultDistanceOrder);
      }
      return searchOrderClause;
   }

   public List<UniversalSearchResultFeatureHeader> getSearchResultFeatureHeaders () {
      return searchResultFeatureHeaders;
   }

   public List<UniversalSearchResultFeatureHeader> getUnstructuredSearchResultsHeaders () {
      return unstructuredSearchResultsHeaders;
   }

   public void loadDifaultDistanceLimits () {
      String distanceLimits = getUnstructuredSearchConfiguration().getProperty(DEFAULT_VICINITY_DISTANCE_LIMITS);
      if (distanceLimits != null) {
         StringTokenizer st = new StringTokenizer(distanceLimits, ",");
         while (st.hasMoreTokens()) {
            defaultVicinityDistanceLimits.add(st.nextToken());
         }
      }

   }

   public void loadSearchResultOrder () {
      Object prop = getUnstructuredSearchConfiguration().getPropertyObject(
               RESULT_ORDER_RESULT_ORDER + ".pre-order.ResultOrder.name");
      int size = 0;
      if (prop instanceof Collection) {
         size = ((Collection) prop).size();
      } else if (prop != null) {
         size = 1;
      }
      Map<Integer, String> searchResultOrderMap = new TreeMap<Integer, String>();
      for (int i = 0; i < size; i++) {
         Integer resultOrder = getUnstructuredSearchConfiguration().getInt(
                  RESULT_ORDER_RESULT_ORDER + ".pre-order.ResultOrder" + "(" + i + ")." + "priority");
         String resultColumn = getUnstructuredSearchConfiguration().getProperty(
                  RESULT_ORDER_RESULT_ORDER + ".pre-order.ResultOrder" + "(" + i + ")." + "name");
         String orderType = getUnstructuredSearchConfiguration().getProperty(
                  RESULT_ORDER_RESULT_ORDER + ".pre-order.ResultOrder" + "(" + i + ")." + "type");

         searchResultOrderMap.put(resultOrder, resultColumn + "##" + orderType);
      }
      searchResultPreOrder.addAll(searchResultOrderMap.values());

      prop = getUnstructuredSearchConfiguration().getPropertyObject(
               RESULT_ORDER_RESULT_ORDER + ".post-order.ResultOrder.name");
      size = 0;
      if (prop instanceof Collection) {
         size = ((Collection) prop).size();
      } else if (prop != null) {
         size = 1;
      }
      searchResultOrderMap = new TreeMap<Integer, String>();
      for (int i = 0; i < size; i++) {
         Integer resultOrder = getUnstructuredSearchConfiguration().getInt(
                  RESULT_ORDER_RESULT_ORDER + ".post-order.ResultOrder" + "(" + i + ")." + "priority");
         String resultColumn = getUnstructuredSearchConfiguration().getProperty(
                  RESULT_ORDER_RESULT_ORDER + ".post-order.ResultOrder" + "(" + i + ")." + "name");
         String orderType = getUnstructuredSearchConfiguration().getProperty(
                  RESULT_ORDER_RESULT_ORDER + ".post-order.ResultOrder" + "(" + i + ")." + "type");

         searchResultOrderMap.put(resultOrder, resultColumn + "##" + orderType);
      }
      searchResultPostOrder.addAll(searchResultOrderMap.values());

      String resultColumn = getUnstructuredSearchConfiguration().getProperty(
               RESULT_ORDER_RESULT_ORDER + ".distance-order.ResultOrder.name");
      String orderType = getUnstructuredSearchConfiguration().getProperty(
               RESULT_ORDER_RESULT_ORDER + ".distance-order.ResultOrder.type");

      searchResultDistanceOrder = resultColumn + "##" + orderType;
   }

   public void loadSearchResultFeatureHeaders () {
      Object headerValues = getUnstructuredSearchConfiguration().getPropertyObject(
               RESULT_HEADERS + "." + "resultHeader.name");
      int headerValuesSize = 0;
      if (headerValues instanceof Collection) {
         headerValuesSize = ((Collection) headerValues).size();
         for (int i = 0; i < headerValuesSize; i++) {
            String id = getUnstructuredSearchConfiguration().getProperty(
                     RESULT_HEADERS + "." + "resultHeader" + "(" + i + ")." + "id");
            String name = getUnstructuredSearchConfiguration().getProperty(
                     RESULT_HEADERS + "." + "resultHeader" + "(" + i + ")." + "name");
            String format = getUnstructuredSearchConfiguration().getProperty(
                     RESULT_HEADERS + "." + "resultHeader" + "(" + i + ")." + "format");
            searchResultFeatureHeaders.add(getSearchResultFeatureHeader(id, name, format));

         }
      }
   }

   public void loadUnstructuredSearchResultHeaders () {
      Object headerValues = getUnstructuredSearchConfiguration().getPropertyObject(
               UNSTRUCTURED_SEARCH_RESULT_HEADERS + "." + "resultHeader.name");
      int headerValuesSize = 0;
      if (headerValues instanceof Collection) {
         headerValuesSize = ((Collection) headerValues).size();
         for (int i = 0; i < headerValuesSize; i++) {
            UniversalSearchResultFeatureHeader universalSearchResultFeatureHeader = new UniversalSearchResultFeatureHeader();
            String id = getUnstructuredSearchConfiguration().getProperty(
                     UNSTRUCTURED_SEARCH_RESULT_HEADERS + "." + "resultHeader" + "(" + i + ")." + "id");
            String name = getUnstructuredSearchConfiguration().getProperty(
                     UNSTRUCTURED_SEARCH_RESULT_HEADERS + "." + "resultHeader" + "(" + i + ")." + "name");
            String format = getUnstructuredSearchConfiguration().getProperty(
                     UNSTRUCTURED_SEARCH_RESULT_HEADERS + "." + "resultHeader" + "(" + i + ")." + "format");
            String columnName = getUnstructuredSearchConfiguration().getProperty(
                     UNSTRUCTURED_SEARCH_RESULT_HEADERS + "." + "resultHeader" + "(" + i + ")." + "columnName");
            String featureValueType = getUnstructuredSearchConfiguration().getProperty(
                     UNSTRUCTURED_SEARCH_RESULT_HEADERS + "." + "resultHeader" + "(" + i + ")." + "featureValueType");
            String sortable = getUnstructuredSearchConfiguration().getProperty(
                     UNSTRUCTURED_SEARCH_RESULT_HEADERS + "." + "resultHeader" + "(" + i + ")." + "sortable");
            String defaultSortOrder = getUnstructuredSearchConfiguration().getProperty(
                     UNSTRUCTURED_SEARCH_RESULT_HEADERS + "." + "resultHeader" + "(" + i + ")." + "defaultSortOrder");
            String diplayableFeatureAlignmentType = getUnstructuredSearchConfiguration().getProperty(
                     UNSTRUCTURED_SEARCH_RESULT_HEADERS + "." + "resultHeader" + "(" + i + ")."
                              + "diplayableFeatureAlignmentType");

            universalSearchResultFeatureHeader.setId(id);
            universalSearchResultFeatureHeader.setName(name);
            universalSearchResultFeatureHeader.setFormat(format);
            universalSearchResultFeatureHeader.setColumnName(columnName);
            universalSearchResultFeatureHeader.setFeatureValueType(FeatureValueType.getType(featureValueType));
            universalSearchResultFeatureHeader.setSortable(CheckType.getType(sortable.charAt(0)));
            universalSearchResultFeatureHeader
                     .setDefaultSortOrder(OrderEntityType.getOrderEntityType(defaultSortOrder));
            universalSearchResultFeatureHeader.setDiplayableFeatureAlignmentType(DisplayableFeatureAlignmentType
                     .getType(diplayableFeatureAlignmentType));
            universalSearchResultFeatureHeader.setDataHeader(CheckType.getType(sortable.charAt(0)));
            unstructuredSearchResultsHeaders.add(universalSearchResultFeatureHeader);

         }
      }
   }

   private UniversalSearchResultFeatureHeader getSearchResultFeatureHeader (String id, String name, String format) {
      UniversalSearchResultFeatureHeader universalSearchResultFeatureHeader = new UniversalSearchResultFeatureHeader();
      universalSearchResultFeatureHeader.setId(id);
      universalSearchResultFeatureHeader.setName(name);
      universalSearchResultFeatureHeader.setFormat(format);
      return universalSearchResultFeatureHeader;
   }

   public Integer getDefaultVicinityDistanceLimit () {
      return getUnstructuredSearchConfiguration().getInt(DEFAULT_VICINITY_DISTANCE_LIMIT);
   }

   public boolean getDisplayClosedMatchCount () {
      return getUnstructuredSearchConfiguration().getBoolean(DISPLAY_CLOSE_MATCH_COUNT);
   }

   public boolean getUseDbFunctionForMultipleLocationQuery () {
      return getUnstructuredSearchConfiguration().getBoolean(USE_DB_FUNCTION_FOR_MULTIPLE_LOCATION_QUERY);
   }

   public String getDbFunctionNameForMultipleLocationQuery () {
      return getUnstructuredSearchConfiguration().getProperty(DB_FUNCTION_NAME_FOR_MULTIPLE_LOCATION_QUERY);
   }

   public String getUnwantedCharRegex () {
      return getUnstructuredSearchConfiguration().getProperty(SPECIAL_CHAR_TRIM_PATTERN_FOR_TITLE);
   }

   public boolean getApplyKeyWordSearchFilter () {
      return getUnstructuredSearchConfiguration().getBoolean(APPLY_KEY_WORD_SEARCH_FILTER);
   }

   public boolean getApplyPartialMatchFilter () {
      return getUnstructuredSearchConfiguration().getBoolean(APPLY_PARTIAL_MATCH_FILTER);
   }

   public Long getUnstructuredResultNumberOfLinks () {
      return getUnstructuredSearchConfiguration().getLong(UNSTRUCTURED_RESULT_NUMBER_OF_LINKS);
   }

   public Long getUnstructuredResultPageSize () {
      return getUnstructuredSearchConfiguration().getLong(UNSTRUCTURED_RESULT_PAGE_SIZE);
   }

   @Override
   public String getDefaultUserPreferredZipCode () {
      return getUnstructuredSearchConfiguration().getProperty(DEFAULT_USER_PREFERRED_ZIP_CODE);
   }

   @Override
   public Integer getDefaultSemantifiedContentKeywordMatchMaxRecordCount () {
      return getUnstructuredSearchConfiguration().getInt(DEFAULT_SEMANTIFIED_CONTENT_KEYWORD_MATCH_MAX_RECORD_COUNT);
   }

   @Override
   public Integer getResemantificationSemantifiedContentKeywordMatchMaxRecordCount () {
      return getUnstructuredSearchConfiguration().getInt(
               RESEMANTIFICATION_SEMANTIFIED_CONTENT_KEYWORD_MATCH_MAX_RECORD_COUNT);
   }

   @Override
   public Boolean isResemantificationCheckBoxVisible () {
      return getUnstructuredSearchConfiguration().getBoolean(RESEMANTIFICATION_CHECKBOX_VISIBILITY);
   }

   @Override
   public Integer getSuggestionRetrivalLimit () {
      return getUnstructuredSearchConfiguration().getInt(UNSTRUCTURED_SUGGESTION_RETRIEVAL_LIMIT);
   }

   public List<String> getDefaultVicinityDistanceLimits () {
      return defaultVicinityDistanceLimits;
   }

   public void setDefaultVicinityDistanceLimits (List<String> defaultVicinityDistanceLimits) {
      this.defaultVicinityDistanceLimits = defaultVicinityDistanceLimits;
   }

   /**
    * @return the unstructuredSearchConfiguration
    */
   public IConfiguration getUnstructuredSearchConfiguration () {
      return unstructuredSearchConfiguration;
   }

   /**
    * @param unstructuredSearchConfiguration the unstructuredSearchConfiguration to set
    */
   public void setUnstructuredSearchConfiguration (IConfiguration unstructuredSearchConfiguration) {
      this.unstructuredSearchConfiguration = unstructuredSearchConfiguration;
   }

}