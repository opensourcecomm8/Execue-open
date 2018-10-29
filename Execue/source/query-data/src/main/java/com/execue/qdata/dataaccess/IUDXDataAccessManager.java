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


package com.execue.qdata.dataaccess;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.entity.Seed;
import com.execue.core.common.bean.qdata.AdditionalInfo;
import com.execue.core.common.bean.qdata.AppNewsPopularity;
import com.execue.core.common.bean.qdata.EntityAttribute;
import com.execue.core.common.bean.qdata.RIInstanceTripleDefinition;
import com.execue.core.common.bean.qdata.RIUniversalSearch;
import com.execue.core.common.bean.qdata.RIUserQuery;
import com.execue.core.common.bean.qdata.ReducedFormIndex;
import com.execue.core.common.bean.qdata.UDXKeyword;
import com.execue.core.common.bean.qdata.UnStructuredIndex;
import com.execue.core.common.bean.qdata.UniversalSearchResult;
import com.execue.core.common.bean.uss.UniversalUnstructuredSearchResult;
import com.execue.core.common.bean.uss.UnstructuredSearchResultItem;
import com.execue.core.common.type.ProcessingFlagType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.qdata.exception.UDXException;

public interface IUDXDataAccessManager {

   public UnStructuredIndex storeUDX (UnStructuredIndex udx) throws UDXException;

   public void updateUDX (UnStructuredIndex udx) throws UDXException;

   public void storeRIUDXEntries (List<RIUniversalSearch> udxEntries) throws UDXException;

   public List<Long> getUDXIdsByRFId (Long rfId) throws DataAccessException;

   public Map<Long, List<Long>> getUDXIdsMapByRFIds (List<Long> rfIds) throws DataAccessException;

   public Map<Long, List<UnStructuredIndex>> getUDXMapByRFIds (List<Long> rfIds) throws DataAccessException;

   /**
    * Method to return the Bean created with the information RiUnstructuredIndex Object and RIUserQuery
    * 
    * @param userQueryId
    * @return
    * @throws DataAccessException
    */
   public UniversalUnstructuredSearchResult getRIUnstructuredResultsByUserQuery (Long userQueryId, Integer pageNumber,
            Integer maxResults) throws DataAccessException;

   public void saveUserQueryReverseIndex (RIUserQuery userQuery) throws DataAccessException;

   public void saveUserQueryReverseIndex (List<RIUserQuery> userQueryList) throws DataAccessException;

   public List<UnstructuredSearchResultItem> getRIUdxByUserQueryId (Long userQueryId, Integer pageNumber,
            Integer maxResults) throws DataAccessException;

   public RIUniversalSearch getRIUnstructuredIndexById (Long id) throws DataAccessException;

   public UnStructuredIndex getUnstructuredIndexById (Long udxId) throws DataAccessException;

   public ReducedFormIndex getReducedFormIndexById (Long rfxId) throws DataAccessException;

   public List<RIInstanceTripleDefinition> getRIInstanceTriplesForUserQuery (Long userQueryId)
            throws DataAccessException;   

   public List<UniversalSearchResult> getUniversalSearchMatchForQueryCache (Long userQueryId)
            throws DataAccessException;

   public List<UniversalSearchResult> getUniversalSearchMatchForRelatedQuery (Long userQueryId)
            throws DataAccessException;

   public Integer populateUniversalSearchResultForUserQuery (Long userQueryId, boolean applyKeyWordSearchFilter)
            throws DataAccessException;

   public void createAppNewsPopularity (List<AppNewsPopularity> appNewsPopularityList) throws DataAccessException;

   public Long getRIUserQueryMaxExecutionDate () throws DataAccessException;

   public void deleteRIUserQueriesByExecutionDate (Long executionDateTime) throws DataAccessException;

   public void deleteRIUnstructuredIndexesByContentDate (Date contentDate, String contentSourceType)
            throws DataAccessException;

   public void deleteUdxAttributesByContentDate (Date contentDate) throws DataAccessException;

   public void deleteUDXByContentDate (Date contentDate, String contentSourceType) throws DataAccessException;

   public List<UnStructuredIndex> getUnstructuredIndexesByContentSourceType (String contentSourceType, Long batchNum,
            int batchSize) throws DataAccessException;

   public List<UnStructuredIndex> getUnstructuredIndexesByProcessedState (ProcessingFlagType processed, int batchSize,
            String contentSourceType) throws DataAccessException;

   public void deleteRIUnstructuredIndexesByUdxIds (List<Long> udxIds) throws DataAccessException;

   public void deleteUDXById (List<Long> ids) throws DataAccessException;

   public void deleteUdxAttributesByUdxIds (List<Long> udxIds) throws DataAccessException;

   public Seed getSeedByNodeAndType (Long nodeId, String type) throws DataAccessException;

   public void updateSeed (Seed seed) throws DataAccessException;

   public void updateUDXImageURLProcessedState (ProcessingFlagType processedState, Long minUDXID, Long maxUDXID,
            Long batchId) throws DataAccessException;

   public void updateUdxResultWeightBasedOnRFXValues (Long userQueryId) throws DataAccessException;

   public UniversalUnstructuredSearchResult getUdxResultsInRange (Long userQueryId, Long pageNumber, Long maxResults,
            Integer totalCount);

   public void deleteUdxResultsByExecutionDate (long execuetionDate) throws DataAccessException;

   public void createUDXKeywords (List<UDXKeyword> udxKeywords) throws DataAccessException;

   public Integer populateUdxKeyWordMatchResultForUserQuery (Long userQueryId, String queryTokens)
            throws DataAccessException;

   public void deleteUDXKeyWordByContentDate (Date contentDate) throws DataAccessException;

   public void updateUnstructuredIndexes (List<UnStructuredIndex> unstructuredIndexes) throws UDXException;

   public void cleanUDXDuplicateArticles (List<Long> duplicateArticleIds) throws UDXException;

   public List<Long> getDuplicateUDXArticleIds () throws UDXException;

   public void updateUDXImageURLProcessedStateByBatchId (ProcessingFlagType updatingProcessedState, Long batchId,
            ProcessingFlagType existingProcessedState) throws DataAccessException;
}