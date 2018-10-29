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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.type.ProcessingFlagType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueDAOUtil;
import com.execue.dataaccess.callback.HibernateCallbackParameterInfo;
import com.execue.dataaccess.callback.HibernateCallbackParameterValueType;
import com.execue.dataaccess.callback.HibernateCallbackUtility;
import com.execue.dataaccess.callback.HibernateLimitCallback;
import com.execue.dataaccess.callback.HibernateStatementCallback;
import com.execue.dataaccess.callback.NativeStatementCallback;
import com.execue.dataaccess.configuration.USWHDataAccessConfigurationConstants;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.uswhda.dataaccess.dao.BaseUnstructuredWHDAO;
import com.execue.uswhda.dataaccess.dao.ISourceContentDAO;

/**
 * @author vishay
 */
public class SourceContentDAOImpl extends BaseUnstructuredWHDAO implements ISourceContentDAO {

   private static Map<String, String> queriesMap = new HashMap<String, String>();

   private static final Logger        log        = Logger.getLogger(SourceContentDAOImpl.class);

   static {
      queriesMap = ExecueDAOUtil
               .loadUSWHQueriesMap(USWHDataAccessConfigurationConstants.STATIC_SOURCE_CONTENT_DAO_QUERIES_KEY);
   }

   @Override
   public void updateSourceContentProcessedStateByBatchId (Long contextId, Long batchId,
            ProcessingFlagType existingProcessedState, ProcessingFlagType targetProcessedState)
            throws DataAccessException {
      try {
         String query = queriesMap.get("UPDATE_SOURCE_CONTENT_PROCESSING_STATE_BY_BATCH_ID");
         List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
         parameters.add(new HibernateCallbackParameterInfo("batchId", batchId,
                  HibernateCallbackParameterValueType.OBJECT));
         parameters.add(new HibernateCallbackParameterInfo("existingProcessedState", existingProcessedState,
                  HibernateCallbackParameterValueType.ENUMERATION));
         parameters.add(new HibernateCallbackParameterInfo("targetProcessedState", targetProcessedState,
                  HibernateCallbackParameterValueType.ENUMERATION));
         HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
         getHibernateTemplate(contextId).execute(callback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @Override
   public void updateSourceContentByProcessingState (Long contextId, Long batchId, Long startSourceContentId,
            Long endSourceContentId, ProcessingFlagType processingFlagType) throws DataAccessException {
      ProcessingFlagType notProcessed = ProcessingFlagType.NOT_PROCESSED;
      try {
         String query = queriesMap.get("UPDATE_SOURCE_CONTENT_BY_GIVEN_PROCESSING_STATE");
         List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
         parameters.add(new HibernateCallbackParameterInfo("processingFlagType", processingFlagType,
                  HibernateCallbackParameterValueType.ENUMERATION));
         parameters.add(new HibernateCallbackParameterInfo("batchId", batchId,
                  HibernateCallbackParameterValueType.OBJECT));
         parameters.add(new HibernateCallbackParameterInfo("startSourceContentId", startSourceContentId,
                  HibernateCallbackParameterValueType.OBJECT));
         parameters.add(new HibernateCallbackParameterInfo("endSourceContentId", endSourceContentId,
                  HibernateCallbackParameterValueType.OBJECT));
         parameters.add(new HibernateCallbackParameterInfo("notProcessed", notProcessed,
                  HibernateCallbackParameterValueType.ENUMERATION));
         HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
         getHibernateTemplate(contextId).execute(callback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @SuppressWarnings ("unchecked")
   @Override
   public List<Long> getLatestSourceContentIds (Long contextId, int batchSize) throws DataAccessException {
      List<Long> newsItemIds = null;
      ProcessingFlagType notProcessed = ProcessingFlagType.NOT_PROCESSED;
      try {
         List<HibernateCallbackParameterInfo> queryParams = HibernateCallbackUtility
                  .prepareListForParameters(new HibernateCallbackParameterInfo("notProcessed", notProcessed,
                           HibernateCallbackParameterValueType.ENUMERATION));
         HibernateLimitCallback limitCallback = new HibernateLimitCallback(queriesMap
                  .get("QUERY_GET_LATEST_SOURCE_CONTENT_IDS"), queryParams, batchSize);
         newsItemIds = (List<Long>) getHibernateTemplate(contextId).execute(limitCallback);
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
      return newsItemIds;
   }

   @SuppressWarnings ("unchecked")
   public Long getMaxSourceContentItemIdByContextAndSource (Long contextId, Long sourceNodeId)
            throws DataAccessException {
      try {
         List<Long> maxIds = getHibernateTemplate(contextId).findByNamedParam(
                  queriesMap.get("QUERY_GET_MAX_SOURCE_ITEM_ID_BY_CONTEXT_AND_SOURCE_NODE"),
                  new String[] { "contextId", "sourceNodeId" }, new Long[] { contextId, sourceNodeId });
         if (ExecueCoreUtil.isCollectionNotEmpty(maxIds)) {
            if (maxIds.get(0) != null) {
               return maxIds.get(0);
            }
         }
         return 0L;
      } catch (org.springframework.dao.DataAccessException dae) {
         throw new DataAccessException(DataAccessExceptionCodes.DEFAULT_EXCEPTION_CODE, dae);
      }
   }

   @Override
   public void deleteSourceContentByContentDate (Long contextId, Date contentDate) throws DataAccessException {
      String query = queriesMap.get("DELETE_SOURCE_CONTENT_BY_CONTENT_DATE");
      List<HibernateCallbackParameterInfo> parameters = new ArrayList<HibernateCallbackParameterInfo>();
      parameters.add(new HibernateCallbackParameterInfo("contentDate", contentDate,
               HibernateCallbackParameterValueType.OBJECT));
      HibernateStatementCallback callback = new HibernateStatementCallback(query, parameters);
      getHibernateTemplate(contextId).execute(callback);
   }

   @Override
   public void removeDuplicateFromSourceContentTempTable (Long contextId, QueryTable queryTable)
            throws DataAccessException {
      String query = getUnstructuredWHQueryBuilderServiceFactory().getQueryBuilderService()
               .buildDeleteDedupOnTargetTempTableQuery(queryTable);
      NativeStatementCallback stmtCallback = new NativeStatementCallback(query);
      getHibernateTemplate(contextId).execute(stmtCallback);
   }

   @Override
   public void populateSourceContentTable (Long contextId, QueryTable queryTable) throws DataAccessException {
      String query = getUnstructuredWHQueryBuilderServiceFactory().getQueryBuilderService()
               .buildInsertSourceContentFromTempTableQuery(queryTable);
      NativeStatementCallback stmtCallback = new NativeStatementCallback(query);
      getHibernateTemplate(contextId).execute(stmtCallback);

   }

}
