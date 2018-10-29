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


package com.execue.uswhda.dataaccess.dao.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.unstructured.SemantifiedContent;
import com.execue.core.common.bean.entity.unstructured.UserQueryFeatureInformation;
import com.execue.core.common.bean.qdata.UniversalSearchResultFeature;
import com.execue.core.common.bean.qdata.UniversalSearchResultFeatureHeader;
import com.execue.core.common.bean.uss.UniversalSearchResultItemType;
import com.execue.core.common.bean.uss.UniversalUnstructuredSearchResult;
import com.execue.core.common.bean.uss.UnstructuredSearchInput;
import com.execue.core.common.bean.uss.UnstructuredSearchResultDataHeader;
import com.execue.core.common.bean.uss.UnstructuredSearchResultItem;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.FeatureValueType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueDAOUtil;
import com.execue.dataaccess.callback.HibernateCallbackParameterInfo;
import com.execue.dataaccess.callback.HibernateCallbackParameterValueType;
import com.execue.dataaccess.callback.HibernateCallbackUtility;
import com.execue.dataaccess.callback.HibernateStatementCallback;
import com.execue.dataaccess.callback.NativePaginationCallback;
import com.execue.dataaccess.callback.NativeQueryCallback;
import com.execue.dataaccess.configuration.USWHDataAccessConfigurationConstants;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.uswhda.dataaccess.dao.BaseUnstructuredWHDAO;
import com.execue.uswhda.dataaccess.dao.IUserQueryFeatureInformationDAO;

/**
 * @author vishay
 */
public class UserQueryFeatureInformationDAOImpl extends BaseUnstructuredWHDAO implements
         IUserQueryFeatureInformationDAO {

   private static Map<String, String> queriesMap = new HashMap<String, String>();

   private static final Logger        log        = Logger.getLogger(UserQueryFeatureInformationDAOImpl.class);

   static {
      queriesMap = ExecueDAOUtil
               .loadUSWHQueriesMap(USWHDataAccessConfigurationConstants.STATIC_USER_QUERY_FEATURE_INFORMATION_DAO_QUERIES_KEY);
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List<UserQueryFeatureInformation> getUserQueryFeatureInformation (Long contextId, Long userQueryId)
            throws DataAccessException {
      try {
         return getHibernateTemplate(contextId).findByNamedParam(
                  queriesMap.get("GET_USER_QUERY_FEATURE_INFO_BY_QUERY_ID"), "userQueryId", userQueryId);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   public UniversalUnstructuredSearchResult getUnstructuredSearchResult (Long contextId,
            UnstructuredSearchInput unstructuredSearchInput, Page page) throws DataAccessException {
      try {
         Long userQueryId = unstructuredSearchInput.getUserQueryId();

         boolean isLocationBased = unstructuredSearchInput.isLocationBased();
         boolean applyKeyWordSearchFilter = unstructuredSearchInput.isApplyKeyWordSearchFilter();
         boolean imagePresent = unstructuredSearchInput.isImagePresent();
         boolean applyPartialMatchFilter = unstructuredSearchInput.isApplyPartialMatchFilter();
         boolean displayCloseMatchCount = unstructuredSearchInput.isDisplayCloseMatchCount();
         boolean useDbFunctionForMultipleLocationQuery = unstructuredSearchInput
                  .isUseDbFunctionForMultipleLocationQuery();

         List<String> searchResultOrder = unstructuredSearchInput.getSearchResultOrder();
         List<String> userRequestedLocations = unstructuredSearchInput.getUserRequestedLocations();
         List<String> selectColumnNames = unstructuredSearchInput.getSelectColumnNames();
         List<UniversalSearchResultFeatureHeader> universalSearchResultFeatureHeaders = unstructuredSearchInput
                  .getUniversalSearchResultFeatureHeaders();

         boolean usingLocationJoinQuery = userRequestedLocations.size() > 1 && !useDbFunctionForMultipleLocationQuery;

         // Create a New Universal Search Result Object
         UniversalUnstructuredSearchResult searchResult = new UniversalUnstructuredSearchResult();
         // Create a list of Search Result Items (Rows)
         List<UnstructuredSearchResultItem> resultItems = new ArrayList<UnstructuredSearchResultItem>(1);
         // Start Filling out Search Result
         searchResult.setUnstructuredSearchResultItem(resultItems);
         searchResult.setUserQueryId(userQueryId);
         // If no entries are present for User Query, return key words based result
         if (CollectionUtils.isEmpty(getUserQueryFeatureInformation(contextId, userQueryId))) {
            populateCompleteKeyWordBasedResults(searchResult, unstructuredSearchInput, page, contextId);
            return searchResult;
         }
         // Add image present clause if boolean imagePresent is true in below populateWhereClause call
         Integer requiredInfoSum = 0; // This can be used later once we have mandatory feature functionality in place
         String orderByClause = getUnstructuredWHQueryBuilderServiceFactory().getQueryBuilderService()
                  .bulidOrderByClauseForCompleteKeywordBasedResult(searchResultOrder, isLocationBased,
                           applyKeyWordSearchFilter);
         String joinQueryResultAlias = "usr";
         String coreSearchQuery = getUnstructuredWHQueryBuilderServiceFactory().getQueryBuilderService()
                  .bulidSemantifiedContentSearchQuery(unstructuredSearchInput, requiredInfoSum, joinQueryResultAlias);
         String finalQuery = getUnstructuredWHQueryBuilderServiceFactory().getQueryBuilderService()
                  .bulidKeyWordSearchFilterQuery(contextId, orderByClause, coreSearchQuery, applyKeyWordSearchFilter,
                           selectColumnNames, isLocationBased);

         java.util.LinkedHashMap<String, org.hibernate.type.Type> scalars = new java.util.LinkedHashMap<String, Type>();

         scalars.put("semantified_content_id", StandardBasicTypes.LONG);
         populateResultHeaderInCallback(universalSearchResultFeatureHeaders, scalars, false);
         scalars.put("MATCHED_FACTOR", StandardBasicTypes.DOUBLE);
         if (applyKeyWordSearchFilter) {
            scalars.put("KEY_WORD_MATCH_SCORE", StandardBasicTypes.DOUBLE);
         }
         if (isLocationBased) {
            scalars.put("location_display_name", StandardBasicTypes.STRING);
         }
         HibernateCallbackParameterInfo parameter = new HibernateCallbackParameterInfo("userQueryId", userQueryId,
                  HibernateCallbackParameterValueType.OBJECT);

         NativePaginationCallback callback = new NativePaginationCallback(finalQuery, HibernateCallbackUtility
                  .prepareListForParameters(parameter), scalars, HibernateCallbackUtility.calculateStartIndex(page
                  .getRequestedPage(), page.getPageSize()), page.getPageSize().intValue());
         // Execute and populate the results in the UnstructuredSearchResultItem object
         List<Object[]> results = (List<Object[]>) getHibernateTemplate(contextId).execute(callback);
         if (CollectionUtils.isEmpty(results) && !imagePresent) {
            populateCompleteKeyWordBasedResults(searchResult, unstructuredSearchInput, page, contextId);
            return searchResult;
         }

         for (Object result[] : results) {
            UnstructuredSearchResultItem unstructuredSearchResultItem = getUnstructuredSearchResultItem(result,
                     isLocationBased, false, universalSearchResultFeatureHeaders);
            resultItems.add(unstructuredSearchResultItem);
         }

         // Update the result items with the semantified content information
         updateResultItems(contextId, resultItems);

         // Execute the count query
         if (page.getRecordCount() == null || page.getRecordCount().equals(0L)) {
            // results count query
            String totalCountQuery = getUnstructuredWHQueryBuilderServiceFactory().getQueryBuilderService()
                     .bulidTotalCountQuery(coreSearchQuery);

            HibernateCallbackParameterInfo countQueryParameter = new HibernateCallbackParameterInfo("userQueryId",
                     userQueryId, HibernateCallbackParameterValueType.OBJECT);
            java.util.LinkedHashMap<String, org.hibernate.type.Type> countQueryScalars = new java.util.LinkedHashMap<String, Type>();
            countQueryScalars.put("TOTAL_COUNT", StandardBasicTypes.LONG);

            NativeQueryCallback countQueryCallback = new NativeQueryCallback(totalCountQuery, HibernateCallbackUtility
                     .prepareListForParameters(countQueryParameter), scalars);

            List<Long> resultCounts = (List<Long>) getHibernateTemplate(contextId).execute(countQueryCallback);
            Long totalCount = 0L;
            if (!CollectionUtils.isEmpty(resultCounts)) {
               totalCount = resultCounts.get(0);
            }
            page.setRecordCount(totalCount);

            Long perfectMatchCount = 0L;
            Long migthMatchCount = 0L;

            String perfectMatchQuery = "";
            String mightMatchQuery = "";
            if (!applyPartialMatchFilter) {
               perfectMatchQuery = getUnstructuredWHQueryBuilderServiceFactory().getQueryBuilderService()
                        .bulidPerfectMatchQuery(coreSearchQuery, usingLocationJoinQuery, joinQueryResultAlias);
               mightMatchQuery = getUnstructuredWHQueryBuilderServiceFactory().getQueryBuilderService()
                        .bulidMightMatchQuery(coreSearchQuery, usingLocationJoinQuery, joinQueryResultAlias);
            } else {
               perfectMatchQuery = getUnstructuredWHQueryBuilderServiceFactory().getQueryBuilderService()
                        .bulidPerfectMatchQuery(coreSearchQuery, usingLocationJoinQuery, joinQueryResultAlias);
            }

            HibernateCallbackParameterInfo perfectMatchCountQueryParameter = new HibernateCallbackParameterInfo(
                     "userQueryId", userQueryId, HibernateCallbackParameterValueType.OBJECT);
            java.util.LinkedHashMap<String, org.hibernate.type.Type> perfectMatchCountQueryScalars = new java.util.LinkedHashMap<String, Type>();
            countQueryScalars.put("TOTAL_COUNT", StandardBasicTypes.LONG);

            NativeQueryCallback perfectMatchCountQueryCallback = new NativeQueryCallback(perfectMatchQuery,
                     HibernateCallbackUtility.prepareListForParameters(perfectMatchCountQueryParameter),
                     perfectMatchCountQueryScalars);

            resultCounts = (List<Long>) getHibernateTemplate(contextId).execute(perfectMatchCountQueryCallback);
            if (!CollectionUtils.isEmpty(resultCounts)) {
               perfectMatchCount = resultCounts.get(0);
            }

            // displayCloseMatchCount flag is true only if we want to show the close matches count on the UI,
            // else by default it is set to false.
            if (!applyPartialMatchFilter && displayCloseMatchCount) {
               HibernateCallbackParameterInfo mightMatchCountQueryParameter = new HibernateCallbackParameterInfo(
                        "userQueryId", userQueryId, HibernateCallbackParameterValueType.OBJECT);
               java.util.LinkedHashMap<String, org.hibernate.type.Type> mightMatchCountQueryScalars = new java.util.LinkedHashMap<String, Type>();
               countQueryScalars.put("TOTAL_COUNT", StandardBasicTypes.LONG);

               NativeQueryCallback mightMatchCountQueryCallback = new NativeQueryCallback(mightMatchQuery,
                        HibernateCallbackUtility.prepareListForParameters(mightMatchCountQueryParameter),
                        mightMatchCountQueryScalars);

               resultCounts = (List<Long>) getHibernateTemplate(contextId).execute(mightMatchCountQueryCallback);
               if (!CollectionUtils.isEmpty(resultCounts)) {
                  migthMatchCount = resultCounts.get(0);
               }
            }

            searchResult.setTotalCount(totalCount.intValue());
            searchResult.setPerfectMatchCount(perfectMatchCount.intValue());
            searchResult.setMightMatchCount(migthMatchCount.intValue());
            searchResult.setPartialMatchCount((int) (totalCount - perfectMatchCount - migthMatchCount));
         }
         return searchResult;
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   private void populateCompleteKeyWordBasedResults (
            UniversalUnstructuredSearchResult universalUnstructuredSearchResult,
            UnstructuredSearchInput unstructuredSearchInput, Page page, Long contextId) throws DataAccessException {

      List<String> selectColumnNames = unstructuredSearchInput.getSelectColumnNames();
      boolean isLocationBased = unstructuredSearchInput.isLocationBased();
      String userRequestedSortOrder = unstructuredSearchInput.getUserRequestedSortOrder();
      Long userQueryId = unstructuredSearchInput.getUserQueryId();
      List<UniversalSearchResultFeatureHeader> universalSearchResultFeatureHeaders = unstructuredSearchInput
               .getUniversalSearchResultFeatureHeaders();

      // Here we are preparing native sql's
      String queryCompleteKeywordBasedResults = getUnstructuredWHQueryBuilderServiceFactory().getQueryBuilderService()
               .bulidCompleteKeywordBasedResultsQuery(selectColumnNames, isLocationBased, userRequestedSortOrder);

      java.util.LinkedHashMap<String, org.hibernate.type.Type> scalars = new java.util.LinkedHashMap<String, Type>();

      scalars.put("semantified_content_id", StandardBasicTypes.LONG);
      populateResultHeaderInCallback(universalSearchResultFeatureHeaders, scalars, true);
      // callBack.addScalarValue("KEY_WORD_MATCH_SCORE", StandardBasicTypes.DOUBLE);
      if (isLocationBased) {
         scalars.put("location_display_name", StandardBasicTypes.STRING);
      }

      HibernateCallbackParameterInfo parameter = new HibernateCallbackParameterInfo("userQueryId", userQueryId,
               HibernateCallbackParameterValueType.OBJECT);

      NativePaginationCallback callback = new NativePaginationCallback(queryCompleteKeywordBasedResults,
               HibernateCallbackUtility.prepareListForParameters(parameter), scalars, HibernateCallbackUtility
                        .calculateStartIndex(page.getRequestedPage(), page.getPageSize()), page.getPageSize()
                        .intValue());

      // Execute and populate the results in the UnstructuredSearchResultItem object
      List<Object[]> results = (List<Object[]>) getHibernateTemplate(contextId).execute(callback);
      List<UnstructuredSearchResultItem> resultItems = universalUnstructuredSearchResult
               .getUnstructuredSearchResultItem();
      for (Object kwResult[] : results) {
         UnstructuredSearchResultItem unstructuredSearchResultItem = getUnstructuredSearchResultItem(kwResult,
                  isLocationBased, true, universalSearchResultFeatureHeaders);
         resultItems.add(unstructuredSearchResultItem);
      }

      // Update the result items with the semantified content information
      updateResultItems(contextId, resultItems);

      // Execute the count query for the first time
      if (page.getRecordCount() == null || page.getRecordCount().equals(0L)) {
         // Prepare the count query
         // Here we are preparing native sql's
         String countQuery = getUnstructuredWHQueryBuilderServiceFactory().getQueryBuilderService()
                  .bulidCountQueryForCompleteKeywordBasedResult(queryCompleteKeywordBasedResults);

         HibernateCallbackParameterInfo countQueryParameter = new HibernateCallbackParameterInfo("userQueryId",
                  userQueryId, HibernateCallbackParameterValueType.OBJECT);

         java.util.LinkedHashMap<String, org.hibernate.type.Type> countQueryScalars = new java.util.LinkedHashMap<String, Type>();
         scalars.put("FINAL_COUNT", StandardBasicTypes.LONG);

         NativeQueryCallback countQueryCallback = new NativeQueryCallback(countQuery, HibernateCallbackUtility
                  .prepareListForParameters(countQueryParameter), scalars);
         // Get the results
         List<Long> resultCounts = (List<Long>) getHibernateTemplate(contextId).execute(countQueryCallback);
         Long totalCount = 0L;
         if (!CollectionUtils.isEmpty(resultCounts)) {
            totalCount = resultCounts.get(0);
         }
         page.setRecordCount(totalCount);
         universalUnstructuredSearchResult.setTotalCount(totalCount.intValue());
      }
   }

   private void populateResultHeaderInCallback (
            List<UniversalSearchResultFeatureHeader> universalSearchResultFeatureHeaders,
            java.util.LinkedHashMap<String, org.hibernate.type.Type> scalars, boolean isCompleteKeyWordSearch) {
      for (UniversalSearchResultFeatureHeader unstructuredSearchResultItem : universalSearchResultFeatureHeaders) {
         if (isCompleteKeyWordSearch && unstructuredSearchResultItem.getColumnName().equalsIgnoreCase("SEMANTIC_SCORE")) {
            continue;
         }
         if (unstructuredSearchResultItem.getFeatureValueType() == FeatureValueType.VALUE_NUMBER) {
            scalars.put(unstructuredSearchResultItem.getColumnName(), StandardBasicTypes.DOUBLE);
         } else if (unstructuredSearchResultItem.getFeatureValueType() == FeatureValueType.VALUE_DATE) {
            scalars.put(unstructuredSearchResultItem.getColumnName(), StandardBasicTypes.DATE);
         } else {
            scalars.put(unstructuredSearchResultItem.getColumnName(), StandardBasicTypes.STRING);
         }
      }
   }

   private UnstructuredSearchResultItem getUnstructuredSearchResultItem (Object[] result, boolean isLocationBased,
            boolean isCompleteKeyWordSearch,
            List<UniversalSearchResultFeatureHeader> universalSearchResultFeatureHeaders) {

      List<UniversalSearchResultFeature> unstructuredSearchResultData = new ArrayList<UniversalSearchResultFeature>();
      UnstructuredSearchResultDataHeader unstructuredSearchResultDataHeader = new UnstructuredSearchResultDataHeader();
      List<Integer> indexListForDataHeaderFlag = getDataHeaderIndexList(universalSearchResultFeatureHeaders);
      // populate unstructuredSearchResultData from result set
      int headerSize = universalSearchResultFeatureHeaders.size();
      int requiredIteration = headerSize - 2;
      for (int i = 0; i < requiredIteration; i++) {
         UniversalSearchResultFeatureHeader universalSearchResultFeatureHeader = universalSearchResultFeatureHeaders
                  .get(i);
         if (FeatureValueType.VALUE_NUMBER.equals(universalSearchResultFeatureHeader.getFeatureValueType())) {
            Double value = (Double) result[i + 1];
            unstructuredSearchResultData.add(getNumiricUniversalSearchResultFeature(value));
         } else {
            String value = (String) result[i + 1];
            unstructuredSearchResultData.add(getStringUniversalSearchResultFeature(value));
         }

      }
      Date contentDate = (Date) result[headerSize - 1];
      unstructuredSearchResultData.add(getUniversalSearchResultFeature(contentDate));
      double semanticScore = 0d;
      double matchedFactor = -1d;
      if (!isCompleteKeyWordSearch) {
         semanticScore = (Double) result[headerSize];
         matchedFactor = (Double) result[headerSize + 1];
         unstructuredSearchResultData.add(getUniversalSearchResultFeature(semanticScore));
         if (isLocationBased) {
            String locationDisplayName = (String) result[headerSize + 3];
            // unstructuredSearchResultData.add(getUniversalSearchResultFeature(locationDisplayName));
            unstructuredSearchResultDataHeader.setLocation(locationDisplayName);
         }
      } else {
         if (isLocationBased) {
            String locationDisplayName = (String) result[headerSize];
            unstructuredSearchResultDataHeader.setLocation(locationDisplayName);
         }
         // in case of complete KeyWord Search we will to show semantic score as "NA"
         unstructuredSearchResultData.add(getUniversalSearchResultFeatureWithDummySemanticScore(-1d));
      }

      // populate unstructuredSearchResultDataHeader
      if (ExecueCoreUtil.isCollectionNotEmpty(indexListForDataHeaderFlag)) {
         List<UniversalSearchResultFeature> unstructuredSearchResultDataForHeader = new ArrayList<UniversalSearchResultFeature>();
         for (Integer index : indexListForDataHeaderFlag) {
            UniversalSearchResultFeature universalSearchResultFeature = unstructuredSearchResultData.get(index
                     .intValue());
            universalSearchResultFeature.setHeaderIndex(index);
            unstructuredSearchResultDataForHeader.add(universalSearchResultFeature);
         }
         unstructuredSearchResultDataHeader.setUnstructuredSearchResultData(unstructuredSearchResultDataForHeader);
      }

      // Populate the UnstructuredSearchResultItem
      UnstructuredSearchResultItem unstructuredResult = new UnstructuredSearchResultItem();
      unstructuredResult.setId((Long) result[0]);
      unstructuredResult.setWeight(semanticScore);
      unstructuredResult.setContentDate(contentDate);
      unstructuredResult.setResultItemType(getResultItemType(semanticScore, matchedFactor));

      unstructuredResult.setResultItemId((Long) result[0]);
      unstructuredResult.setUnstructuredSearchResultData(unstructuredSearchResultData);
      unstructuredResult.setUnstructuredSearchResultDataHeader(unstructuredSearchResultDataHeader);
      // Date contentDate = (Date) result[1];
      // Double semanticScore = 0d;
      // isCompleteKeyWordSearch true semantic score and matched factor wont come

      // Double matchedFactor = -1d;
      // This condition is for pure keyword search
      // if (result.length > 3) {
      // semanticScore = (Double) result[2];
      // matchedFactor = (Double) result[3];
      // if (isLocationBased) {
      // Double distance = (Double) result[4];
      // if (applyKeyWordSearchFilter) {
      // Double keyWordMatchScore = (Double) result[5];
      // }
      // } else if (applyKeyWordSearchFilter) {
      // Double keyWordMatchScore = (Double) result[4];
      // }
      // }

      // Map<String, UniversalSearchResultFeature> universalSearchResultFeatureMap = new LinkedHashMap<String,
      // UniversalSearchResultFeature>(
      // 1);
      // TODO: NK: Should later externalize the IDENTIFIER somehow instead of hard coding
      // universalSearchResultFeatureMap.put("MATCH", getUniversalSearchResultFeature(semanticScore));
      // universalSearchResultFeatureMap.put("DATE", getUniversalSearchResultFeature(contentDate));

      // unstructuredResult.setId(id);
      // unstructuredResult.setContentDate(contentDate);
      // unstructuredResult.setWeight(Double.parseDouble(String.format("%.2f", semanticScore)));
      // unstructuredResult.setResultItemType(getResultItemType(semanticScore, matchedFactor));
      // unstructuredResult.setResultFeatureMap(universalSearchResultFeatureMap);

      // TODO: NK: What should be the content source type
      // unstructuredResult.setContentSourceType(semantifiedContent.getContentSourceType());

      return unstructuredResult;
   }

   private void updateResultItems (Long contextId, List<UnstructuredSearchResultItem> resultItems)
            throws DataAccessException {
      if (CollectionUtils.isEmpty(resultItems)) {
         return;
      }
      List<Long> semantifiedContentIds = new ArrayList<Long>();
      for (UnstructuredSearchResultItem unstructuredSearchResultItem : resultItems) {
         semantifiedContentIds.add(unstructuredSearchResultItem.getResultItemId());
      }

      List<SemantifiedContent> semantifiedContents = getSemantfiedContentByIds(contextId, semantifiedContentIds);
      Map<Long, SemantifiedContent> semantifiedContentById = prepareSemantifiedContentByIdMap(semantifiedContents);
      for (UnstructuredSearchResultItem unstructuredSearchResultItem : resultItems) {
         updateResultItem(unstructuredSearchResultItem, semantifiedContentById.get(unstructuredSearchResultItem
                  .getResultItemId()));
      }
   }

   private List<Integer> getDataHeaderIndexList (
            List<UniversalSearchResultFeatureHeader> universalSearchResultFeatureHeaders) {
      int index = 0;
      List<Integer> dataHeaderIndexList = new ArrayList<Integer>();
      for (UniversalSearchResultFeatureHeader universalSearchResultFeatureHeader : universalSearchResultFeatureHeaders) {
         if (CheckType.YES.equals(universalSearchResultFeatureHeader.getDataHeader())) {
            dataHeaderIndexList.add(index);
         }
         index++;
      }
      return dataHeaderIndexList;
   }

   private UniversalSearchResultFeature getNumiricUniversalSearchResultFeature (Double value) {
      UniversalSearchResultFeature universalSearchResultFeature = new UniversalSearchResultFeature();
      universalSearchResultFeature.setValue(Double.toString(value));
      return universalSearchResultFeature;
   }

   private UniversalSearchResultFeature getStringUniversalSearchResultFeature (String value) {
      UniversalSearchResultFeature universalSearchResultFeature = new UniversalSearchResultFeature();
      universalSearchResultFeature.setValue(value);
      return universalSearchResultFeature;
   }

   private UniversalSearchResultFeature getUniversalSearchResultFeature (Double semanticScore) {
      UniversalSearchResultFeature universalSearchResultFeature = new UniversalSearchResultFeature();
      universalSearchResultFeature.setValue(String.format("%.2f", semanticScore));
      return universalSearchResultFeature;
   }

   private UniversalSearchResultFeature getUniversalSearchResultFeatureWithDummySemanticScore (Double semanticScore) {
      UniversalSearchResultFeature universalSearchResultFeature = new UniversalSearchResultFeature();
      universalSearchResultFeature.setValue("NA");
      return universalSearchResultFeature;
   }

   private UniversalSearchResultFeature getUniversalSearchResultFeature (String value) {
      UniversalSearchResultFeature universalSearchResultFeature = new UniversalSearchResultFeature();
      universalSearchResultFeature.setValue(value);
      return universalSearchResultFeature;

   }

   private UniversalSearchResultFeature getUniversalSearchResultFeature (Date contentDate) {
      UniversalSearchResultFeature universalSearchResultFeature = new UniversalSearchResultFeature();
      universalSearchResultFeature.setValue(getFormattedContentDate(contentDate));
      return universalSearchResultFeature;
   }

   private UniversalSearchResultItemType getResultItemType (Double semanticScore, Double featureMatchedCount) {
      if (semanticScore.equals(0d) && featureMatchedCount.equals(-1d)) {
         return UniversalSearchResultItemType.KEYWORD_MATCH;
      }
      if (semanticScore.equals(100d) && featureMatchedCount.equals(1d)) {
         return UniversalSearchResultItemType.PERFECT_MATCH;
      }
      if (semanticScore < 100 && featureMatchedCount.equals(1d)) {
         return UniversalSearchResultItemType.UNKNOWN_MATCH;
      }
      if (semanticScore < 100 && featureMatchedCount < 1) {
         return UniversalSearchResultItemType.PARTIAL_MATCH;
      }
      return UniversalSearchResultItemType.PERFECT_MATCH;
   }

   @SuppressWarnings ("unchecked")
   public List<SemantifiedContent> getSemantfiedContentByIds (Long contextId, List<Long> semantifiedContentIds)
            throws DataAccessException {
      try {
         return getHibernateTemplate(contextId).findByNamedParam(
                  queriesMap.get("QUERY_GET_SEMANTIFIED_CONTENT_BY_IDS"),
                  new String[] { "contextId", "semantifiedContentIds" },
                  new Object[] { contextId, semantifiedContentIds });
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   private Map<Long, SemantifiedContent> prepareSemantifiedContentByIdMap (List<SemantifiedContent> semantifiedContents) {
      Map<Long, SemantifiedContent> semantifiedContentById = new HashMap<Long, SemantifiedContent>();
      for (SemantifiedContent semantifiedContent : semantifiedContents) {
         semantifiedContentById.put(semantifiedContent.getId(), semantifiedContent);
      }
      return semantifiedContentById;
   }

   private UnstructuredSearchResultItem updateResultItem (UnstructuredSearchResultItem unstructuredResult,
            SemantifiedContent semantifiedContent) {

      unstructuredResult.setUrl(semantifiedContent.getUrl());
      unstructuredResult.setImageUrl(semantifiedContent.getImageUrl());
      unstructuredResult.setName(semantifiedContent.getShortDescription());
      unstructuredResult.setShortDescription(semantifiedContent.getShortDescription());
      unstructuredResult.setLongDescription(semantifiedContent.getLongDescription());
      unstructuredResult.setContentSource(semantifiedContent.getContentSource());

      UnstructuredSearchResultDataHeader unstructuredSearchResultDataHeader = unstructuredResult
               .getUnstructuredSearchResultDataHeader();
      unstructuredSearchResultDataHeader.setUrl(semantifiedContent.getUrl());
      unstructuredSearchResultDataHeader.setImageUrl(semantifiedContent.getImageUrl());
      // unstructuredSearchResultDataHeader.setName(semantifiedContent.getShortDescription());
      unstructuredSearchResultDataHeader.setShortDescription(semantifiedContent.getShortDescription());
      unstructuredSearchResultDataHeader.setLongDescription(semantifiedContent.getLongDescription());
      unstructuredSearchResultDataHeader.setContentSource(semantifiedContent.getContentSource());
      return unstructuredResult;
   }

   private String getFormattedContentDate (Date contentDate) {
      // TODO: Later should get the format from the configuration
      SimpleDateFormat cachedDateFormat = new SimpleDateFormat("MMM dd, yyyy");
      return cachedDateFormat.format(contentDate);
   }

   @Override
   public void deleteUserQuerFeatureInfoByExecutionDate (Long contextId, Date executionDate) throws DataAccessException {
      try {
         String query = queriesMap.get("DELETE_USER_QUERY_FEATURE_INFO_BY_EXECUTION_DATE");
         List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
         parameters.add(new HibernateCallbackParameterInfo("executionDate", executionDate,
                  HibernateCallbackParameterValueType.OBJECT));
         HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
         getHibernateTemplate(contextId).execute(callback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   @Override
   public Long getUserQueryFeatureInfoMaxExecutionDate (Long contextId) throws DataAccessException {
      Long maxUserQueryFeatureInfoExecutionDate = null;
      List<Timestamp> dates = getHibernateTemplate(contextId).find(
               queriesMap.get("QUERY_USER_QUERY_FEATURE_INFO_MAX_EXECUTION_DATE"));
      if (ExecueCoreUtil.isCollectionNotEmpty(dates)) {
         if (dates.get(0) != null) {
            maxUserQueryFeatureInfoExecutionDate = dates.get(0).getTime();
         }
      }
      return maxUserQueryFeatureInfoExecutionDate;
   }

   @SuppressWarnings ("unchecked")
   public void saveUserQueryFeatureInformation (Long contextId,
            List<UserQueryFeatureInformation> userQueryFeatureInformationList) throws DataAccessException {
      try {
         saveOrUpdateAll(contextId, userQueryFeatureInformationList);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

}
