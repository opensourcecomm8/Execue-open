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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.unstructured.SemantifiedContent;
import com.execue.core.common.bean.entity.unstructured.SemantifiedContentFeatureInformation;
import com.execue.core.common.bean.uss.UnstructuredKeywordSearchInput;
import com.execue.core.common.bean.uss.UnstructuredSearchResultDataHeader;
import com.execue.core.common.bean.uss.UnstructuredSearchResultItem;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ProcessingFlagType;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueDAOUtil;
import com.execue.dataaccess.NativeQueryHolder;
import com.execue.dataaccess.callback.HibernateCallbackParameterInfo;
import com.execue.dataaccess.callback.HibernateCallbackParameterValueType;
import com.execue.dataaccess.callback.HibernateLimitCallback;
import com.execue.dataaccess.callback.HibernateStatementCallback;
import com.execue.dataaccess.callback.NativeStatementCallback;
import com.execue.dataaccess.configuration.USWHDataAccessConfigurationConstants;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.uswhda.dataaccess.dao.BaseUnstructuredWHDAO;
import com.execue.uswhda.dataaccess.dao.ISemantifiedContentDAO;

/**
 * @author vishay
 */
public class SemantifiedContentDAOImpl extends BaseUnstructuredWHDAO implements ISemantifiedContentDAO {

   private static Map<String, String> queriesMap = new HashMap<String, String>();

   private static final Logger        log        = Logger.getLogger(SemantifiedContentDAOImpl.class);

   static {
      queriesMap = ExecueDAOUtil
               .loadUSWHQueriesMap(USWHDataAccessConfigurationConstants.STATIC_SEMANTIFIED_CONTENT_DAO_QUERIES_KEY);
   }

   @SuppressWarnings ("unchecked")
   public Integer populateSemantifiedContentKeyWordMatchResultForUserQuery (
            UnstructuredKeywordSearchInput unstructuredKeywordSearchInput) throws DataAccessException {

      String queryTokens = unstructuredKeywordSearchInput.getUserQueryTokens();
      Long userQueryId = unstructuredKeywordSearchInput.getUserQueryId();
      Integer maxRecordCount = unstructuredKeywordSearchInput.getMaxRecordCount();
      Long contextId = unstructuredKeywordSearchInput.getApplicationId();
      List<String> userRequestedLocations = unstructuredKeywordSearchInput.getUserRequestedLocations();

      boolean isLocationBased = unstructuredKeywordSearchInput.isLocationBased();
      boolean useDbFunctionForMultipleLocationQuery = unstructuredKeywordSearchInput
               .isUseDbFunctionForMultipleLocationQuery();
      boolean usingLocationJoinQuery = isLocationBased && userRequestedLocations.size() > 1
               && !useDbFunctionForMultipleLocationQuery;

      String againstQueryTokens = getAgainstQueryTokens(queryTokens);

      // start
      String querySemantifiedContentKeyWordMatch = getUnstructuredWHQueryBuilderServiceFactory()
               .getQueryBuilderService().buildSemantifiedContentKeyWordMatchQuery(contextId,
                        unstructuredKeywordSearchInput, againstQueryTokens);
      // end

      List<HibernateCallbackParameterInfo> queryParams = new ArrayList<HibernateCallbackParameterInfo>();

      queryParams.add(new HibernateCallbackParameterInfo("executionDate", new Date(),
               HibernateCallbackParameterValueType.OBJECT));

      if (usingLocationJoinQuery) {
         queryParams.add(new HibernateCallbackParameterInfo("userQueryId", userQueryId,
                  HibernateCallbackParameterValueType.OBJECT));
      }
      queryParams.add(new HibernateCallbackParameterInfo("minResult", 0, HibernateCallbackParameterValueType.OBJECT));
      queryParams.add(new HibernateCallbackParameterInfo("maxResult", maxRecordCount,
               HibernateCallbackParameterValueType.OBJECT));
      NativeStatementCallback nativeStmtCallback = new NativeStatementCallback(querySemantifiedContentKeyWordMatch,
               queryParams);
      return (Integer) getHibernateTemplate(contextId).execute(nativeStmtCallback);
   }

   private String getAgainstQueryTokens (String queryTokens) {
      StringTokenizer st = new StringTokenizer(queryTokens);
      StringBuilder sb = new StringBuilder(1);
      while (st.hasMoreTokens()) {
         String nextToken = st.nextToken();
         sb.append("+\"").append(nextToken).append("\"");
      }
      return sb.toString();
   }

   public void markSemantifiedContentForResemantification (Long contextId, Long userQueryId) throws DataAccessException {
      ProcessingFlagType processingState = ProcessingFlagType.PROCESSED;
      try {
         String query = NativeQueryHolder
                  .getUnstructuredWHNativeQuery("QUERY_MARK_SEMANTIFIED_CONTENT_FOR_RESEMANTIFICATION");
         List<HibernateCallbackParameterInfo> queryParams = new ArrayList<HibernateCallbackParameterInfo>();
         queryParams.add(new HibernateCallbackParameterInfo("contextId", contextId,
                  HibernateCallbackParameterValueType.OBJECT));
         queryParams.add(new HibernateCallbackParameterInfo("userQueryId", userQueryId,
                  HibernateCallbackParameterValueType.OBJECT));
         queryParams.add(new HibernateCallbackParameterInfo("scProcessingState", processingState,
                  HibernateCallbackParameterValueType.ENUMERATION));
         queryParams.add(new HibernateCallbackParameterInfo("scfiProcessingState", processingState,
                  HibernateCallbackParameterValueType.ENUMERATION));
         queryParams.add(new HibernateCallbackParameterInfo("imageURLProcessed", processingState,
                  HibernateCallbackParameterValueType.ENUMERATION));
         NativeStatementCallback nativeStmtCallback = new NativeStatementCallback(query, queryParams);
         getHibernateTemplate(contextId).execute(nativeStmtCallback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @Override
   public void updateSemantifiedContentByProcessedStateUserQueryId (Long contextId, Long userQueryId,
            ProcessingFlagType processingFlagType) throws DataAccessException {
      try {
         String query = NativeQueryHolder
                  .getUnstructuredWHNativeQuery("UPDATE_SEMANTIFIED_CONTENT_PROCESSING_STATE_BY_USER_QUERY_ID");
         List<HibernateCallbackParameterInfo> queryParams = new ArrayList<HibernateCallbackParameterInfo>();
         queryParams.add(new HibernateCallbackParameterInfo("userQueryId", userQueryId,
                  HibernateCallbackParameterValueType.OBJECT));
         queryParams.add(new HibernateCallbackParameterInfo("processingFlagType", processingFlagType,
                  HibernateCallbackParameterValueType.ENUMERATION));
         queryParams.add(new HibernateCallbackParameterInfo("contextId", contextId,
                  HibernateCallbackParameterValueType.OBJECT));
         NativeStatementCallback nativeStmtCallback = new NativeStatementCallback(query, queryParams);
         getHibernateTemplate(contextId).execute(nativeStmtCallback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   @Override
   public Long getSemantifiedConentKeywordMatchMaxExecutionDate (Long contextId) throws DataAccessException {
      Long maxSemantifiedContentKeyWordMatchExecutionDate = null;
      List<Timestamp> dates = getHibernateTemplate(contextId).find(
               queriesMap.get("QUERY_SEMANTIFIED_CONTENT_KEYWORD_MATCH_MAX_EXECUTION_DATE"));
      if (ExecueCoreUtil.isCollectionNotEmpty(dates)) {
         if (dates.get(0) != null) {
            maxSemantifiedContentKeyWordMatchExecutionDate = dates.get(0).getTime();
         }
      }
      return maxSemantifiedContentKeyWordMatchExecutionDate;
   }

   @Override
   public void deleteSemantifiedContentKeyWordMatchByExecutionDate (Long contextId, Date executionDate)
            throws DataAccessException {
      try {
         String query = queriesMap.get("DELETE_SEMANTIFIED_CONTENT_KEYWORD_MATCH_BY_EXECUTION_DATE");
         List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
         parameters.add(new HibernateCallbackParameterInfo("executionDate", executionDate,
                  HibernateCallbackParameterValueType.OBJECT));
         HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
         getHibernateTemplate(contextId).execute(callback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @Override
   public void deleteSemantifiedContentKeyword (Long contextId, Date contentDate) throws DataAccessException {
      String query = queriesMap.get("DELETE_SEMANTIFIED_CONTENT_KEYWORD_BY_CONTENT_DATE");
      List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
      parameters.add(new HibernateCallbackParameterInfo("contentDate", contentDate,
               HibernateCallbackParameterValueType.OBJECT));
      HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
      getHibernateTemplate(contextId).execute(callback);
   }

   @Override
   public void deleteSemantifiedContentFeatureInfo (Long contextId, Date contentDate) throws DataAccessException {
      String query = queriesMap.get("DELETE_SEMANTIFIED_CONTENT_FEATURE_INFO_BY_CONTENT_DATE");
      List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
      parameters.add(new HibernateCallbackParameterInfo("contentDate", contentDate,
               HibernateCallbackParameterValueType.OBJECT));
      HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
      getHibernateTemplate(contextId).execute(callback);
   }

   @Override
   public void deleteSemantifiedContentByContentDate (Long contextId, Date contentDate) throws DataAccessException {
      String query = queriesMap.get("DELETE_SEMANTIFIED_CONTENT_BY_CONTENT_DATE");
      List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
      parameters.add(new HibernateCallbackParameterInfo("contentDate", contentDate,
               HibernateCallbackParameterValueType.OBJECT));
      HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
      getHibernateTemplate(contextId).execute(callback);
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List<SemantifiedContentFeatureInformation> getSemantifiedContentFeatureInfoBySemantifiedContentId (
            Long contextId, Long semantifiedContentId) throws DataAccessException {
      try {
         return getHibernateTemplate(contextId).findByNamedParam(
                  queriesMap.get("QUERY_GET_SCFI_BY_SEMANTIFIED_CONTENT_ID"), new String[] { "semantifiedContentId" },
                  new Object[] { semantifiedContentId });
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @Override
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

   @Override
   public void deleteExistingSemantifiedContentFeatureInfo (Long contextId, Long semantifiedContentId)
            throws DataAccessException {
      try {
         String query = queriesMap.get("QUERY_DELETE_SEMANTIFIED_CONTENT_FEATURE_INFO");
         List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
         parameters.add(new HibernateCallbackParameterInfo("contextId", contextId,
                  HibernateCallbackParameterValueType.OBJECT));
         parameters.add(new HibernateCallbackParameterInfo("semantifiedContentId", semantifiedContentId,
                  HibernateCallbackParameterValueType.OBJECT));
         HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
         getHibernateTemplate(contextId).execute(callback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @Override
   public void deleteExistingSemantifiedContentKeywordInfo (Long contextId, Long semantifiedContentId)
            throws DataAccessException {
      try {
         String query = queriesMap.get("QUERY_DELETE_SEMANTIFIED_CONTENT_KEYWORD");
         List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
         parameters.add(new HibernateCallbackParameterInfo("contextId", contextId,
                  HibernateCallbackParameterValueType.OBJECT));
         parameters.add(new HibernateCallbackParameterInfo("semantifiedContentId", semantifiedContentId,
                  HibernateCallbackParameterValueType.OBJECT));
         HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
         getHibernateTemplate(contextId).execute(callback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List<Long> getSemantifiedContentIdsByUserQueryId (Long contextId, Long userQueryId)
            throws DataAccessException {
      List<Long> semantifiedContentIds = null;
      ProcessingFlagType processing = ProcessingFlagType.PROCESSING;
      try {
         String querySemantiedContentIdsByUserQueryId = queriesMap.get("QUERY_GET_SEMANTIFIED_CONTENT_IDS_BY_QUERY_ID");
         semantifiedContentIds = getHibernateTemplate(contextId).findByNamedParam(
                  querySemantiedContentIdsByUserQueryId, new String[] { "contextId", "userQueryId", "processing" },
                  new Object[] { contextId, userQueryId, processing });
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
      return semantifiedContentIds;
   }

   @Override
   public void updateSemantifiedContentImageURLProcessedStateByBatchId (Long contextId,
            ProcessingFlagType updatingProcessedState, Long batchId, ProcessingFlagType existingProcessedState)
            throws DataAccessException {
      try {
         String query = queriesMap.get("UPDATE_SC_IMAGEURL_PROCESSING_STATE_BY_BATCH_ID");
         List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
         parameters.add(new HibernateCallbackParameterInfo("updatingProcessedState", updatingProcessedState,
                  HibernateCallbackParameterValueType.ENUMERATION));
         parameters.add(new HibernateCallbackParameterInfo("batchId", batchId,
                  HibernateCallbackParameterValueType.OBJECT));
         parameters.add(new HibernateCallbackParameterInfo("existingProcessedState", existingProcessedState,
                  HibernateCallbackParameterValueType.ENUMERATION));
         HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
         getHibernateTemplate(contextId).execute(callback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List<Long> getSemantifiedContentIdsByImageUrlProcessedState (Long contextId, ProcessingFlagType processed,
            int batchSize) throws DataAccessException {
      List<Long> semantifiedContentIds = new ArrayList<Long>();
      ProcessingFlagType processing = ProcessingFlagType.PROCESSING;
      try {
         List<HibernateCallbackParameterInfo> queryParams = new ArrayList<HibernateCallbackParameterInfo>();
         queryParams.add(new HibernateCallbackParameterInfo("processed", processed,
                  HibernateCallbackParameterValueType.ENUMERATION));
         queryParams.add(new HibernateCallbackParameterInfo("processing", processing,
                  HibernateCallbackParameterValueType.ENUMERATION));
         HibernateLimitCallback limitCallback = new HibernateLimitCallback(queriesMap
                  .get("QUERY_SEMANTIFIED_CONTENT_IDS_BY_PROCESSED_STATE"), queryParams, batchSize);
         semantifiedContentIds = (List<Long>) getHibernateTemplate(contextId).execute(limitCallback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.NEWS_ITEMS_LOOKUP_FAILED, dae);
      }
      return semantifiedContentIds;
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List<Long> getSemantifiedContentIdsByBadImageUrlLikeDomain (Long contextId, String badImageURLDomainName)
            throws DataAccessException {
      List<Long> semantifiedContentIds = new ArrayList<Long>();
      try {
         String queryGetSemantifiedContentIdsByBadImageUrlLikeDomain = queriesMap
                  .get("QUERY_SC_IDS_BY_IMAGE_URL_LIKE_DOMAIN_NAME");
         semantifiedContentIds = getHibernateTemplate(contextId).findByNamedParam(
                  queryGetSemantifiedContentIdsByBadImageUrlLikeDomain,
                  new String[] { "contextId", "badImageURLDomainName" },
                  new Object[] { contextId, "%" + badImageURLDomainName + "%" });
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
      return semantifiedContentIds;
   }

   @Override
   public void setImagePresentInSemantifiedContentFeatureInfo (Long contextId, Long semantifiedContentId,
            CheckType imagePresent) throws DataAccessException {
      try {
         String query = queriesMap.get("UPDATE_SC_FEATURE_INFO_IMAGE_PRESENT_BY_SEMANTIFIED_CONTENT_ID");
         List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
         parameters.add(new HibernateCallbackParameterInfo("imagePresent", imagePresent,
                  HibernateCallbackParameterValueType.ENUMERATION));
         parameters.add(new HibernateCallbackParameterInfo("semantifiedContentId", semantifiedContentId,
                  HibernateCallbackParameterValueType.OBJECT));
         parameters.add(new HibernateCallbackParameterInfo("contextId", contextId,
                  HibernateCallbackParameterValueType.OBJECT));
         HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
         getHibernateTemplate(contextId).execute(callback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @Override
   public void setImageURLToNullInSemantifiedContent (Long contextId, Long semantifiedContentId)
            throws DataAccessException {
      try {
         String query = queriesMap.get("UPDATE_SC_IMAGE_URL_TO_NULL_IN_SEMANTIFIED_CONTENT_BY_ID");
         List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
         parameters
                  .add(new HibernateCallbackParameterInfo("imageUrl", null, HibernateCallbackParameterValueType.OBJECT));
         parameters.add(new HibernateCallbackParameterInfo("semantifiedContentId", semantifiedContentId,
                  HibernateCallbackParameterValueType.OBJECT));
         parameters.add(new HibernateCallbackParameterInfo("contextId", contextId,
                  HibernateCallbackParameterValueType.OBJECT));
         HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
         getHibernateTemplate(contextId).execute(callback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @Override
   public void updateSemantifiedContentImageURLProcessedState (Long contextId, ProcessingFlagType processedState,
            Long minSemantifiedContentId, Long maxSemantifiedContentId, Long batchId) throws DataAccessException {
      ProcessingFlagType notProcessed = ProcessingFlagType.NOT_PROCESSED;
      try {
         String query = queriesMap.get("UPDATE_SC_IMAGE_URL_PROCESSED_STATE");
         List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
         parameters.add(new HibernateCallbackParameterInfo("processedState", processedState,
                  HibernateCallbackParameterValueType.ENUMERATION));
         parameters.add(new HibernateCallbackParameterInfo("batchId", batchId,
                  HibernateCallbackParameterValueType.OBJECT));
         parameters.add(new HibernateCallbackParameterInfo("minSemantifiedContentId", minSemantifiedContentId,
                  HibernateCallbackParameterValueType.OBJECT));
         parameters.add(new HibernateCallbackParameterInfo("maxSemantifiedContentId", maxSemantifiedContentId,
                  HibernateCallbackParameterValueType.OBJECT));
         parameters.add(new HibernateCallbackParameterInfo("notProcessed", notProcessed,
                  HibernateCallbackParameterValueType.ENUMERATION));
         HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
         getHibernateTemplate(contextId).execute(callback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   /**
    * @param universalUnstructuredSearchResult
    * @param unstructuredSearchInput
    * @param page
    * @param contextId
    * @throws DataAccessException
    */

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

}
