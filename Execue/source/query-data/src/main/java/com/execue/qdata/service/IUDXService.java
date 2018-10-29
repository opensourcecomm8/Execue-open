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


package com.execue.qdata.service;

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
import com.execue.core.common.type.ProcessingFlagType;
import com.execue.qdata.exception.UDXException;

/**
 * Access Service for Un Structured Search related lookups
 * 
 * @author Raju Gottumukkala
 * @since 4.0
 */
public interface IUDXService {

   /**
    * @param udx
    * @throws UDXException
    */
   public UnStructuredIndex storeUDX (UnStructuredIndex udx) throws UDXException;

   /**
    * @param udx
    * @throws UDXException
    */
   public void updatedUDX (UnStructuredIndex udx) throws UDXException;

   /**
    * @param riUdxEntries
    * @throws UDXException
    */
   public void storeRIUDXEntries (List<RIUniversalSearch> riUdxEntries) throws UDXException;

   /**
    * Provides the list of UDX id's based on RF id
    * 
    * @param rfId
    * @return
    * @throws UDXException
    */
   public List<Long> getUDXIdsByRFId (Long rfId) throws UDXException;

   /**
    * Provides a map of rfId to the list of UDX ids
    * 
    * @param rfIds
    * @return
    * @throws UDXException
    */
   public Map<Long, List<Long>> getUDXIdsMapByRFIds (List<Long> rfIds) throws UDXException;

   /**
    * Provides a map of rfId to the list of UDX Objects
    * 
    * @param rfIds
    * @return
    * @throws UDXException
    */
   public Map<Long, List<UnStructuredIndex>> getUDXMapByRFIds (List<Long> rfIds) throws UDXException;

   /**
    * Provide the list of bean which contains Info Abouth RIUDxObject based on UserQuery
    * 
    * @param userQueryId
    * @return
    * @throws UDXException
    */
   public UniversalUnstructuredSearchResult getRIUnstructuredResultsByUserQuery (Long userQueryId, Integer pageNumber,
            Integer maxResults) throws UDXException;

   public void saveUserQueryReverseIndex (List<RIUserQuery> userQueryRIEntries) throws UDXException;

   public RIUniversalSearch getRIUnstructuredIndexById (Long id) throws UDXException;

   public UnStructuredIndex getUnstructuredIndexById (Long udxId) throws UDXException;

   public ReducedFormIndex getReducedFormIndexById (Long rfxId) throws UDXException;  

  

   public List<RIInstanceTripleDefinition> getRIInstanceTriplesForUserQuery (Long userQueryId) throws UDXException;

   public List<UniversalSearchResult> getUniversalSearchMatchForQueryCache (Long userQueryId) throws UDXException;

   public List<UniversalSearchResult> getUniversalSearchMatchForRelatedQuery (Long userQueryId) throws UDXException;

   public void createAppNewsPopularity (List<AppNewsPopularity> appNewsPopularityList) throws UDXException;

   public void cleanRIUserQueries () throws UDXException;

   public void deleteRIUnstructuredIndexesByContentDate (Date contentDate, String contentSourceType)
            throws UDXException;

   public void deleteUdxAttributesByContentDate (Date contentDate) throws UDXException;

   public void deleteUDXByContentDate (Date contentDate, String contentSourceType) throws UDXException;

   public List<UnStructuredIndex> getUnstructuredIndexesByContentSourceType (String contentSourceType, Long batchNum,
            int batchSize) throws UDXException;

   public List<UnStructuredIndex> getUnstructuredIndexesByProcessedState (ProcessingFlagType processed, int batchSize,
            String contentSourceType) throws UDXException;

   public void deleteRIUnstructuredIndexesByUdxIds (List<Long> udxIds) throws UDXException;

   public void deleteUdxAttributesByUdxIds (List<Long> udxIds) throws UDXException;

   public void deleteUDXById (List<Long> ids) throws UDXException;

   public void updateUDXImageURLProcessedState (ProcessingFlagType processedState, Long minUDXID, Long maxUDXID,
            Long batchId) throws UDXException;

   public UniversalUnstructuredSearchResult getUniversalSearchResultForQuery (Long userQueryId, Long pageNumber,
            Long maxResults, boolean applyKeyWordSearchFilter) throws UDXException;

   public UniversalUnstructuredSearchResult getNextUniversalSearchMatchForUdx (Long userQueryId, Long pageNumber,
            Long maxResults, int totalCount);

   public void cleanUdxResults () throws UDXException;

   public void createUDXKeywords (List<UDXKeyword> udxKeywords) throws UDXException;

   public Integer populateUdxKeyWordMatchResultForUserQuery (Long userQueryId, String queryTokens) throws UDXException;

   public void deleteUDXKeyWordByContentDate (Date contentDate) throws UDXException;

   public void updateUnstructuredIndexes (List<UnStructuredIndex> unstructuredIndexes) throws UDXException;

   public void cleanDuplicateArticles (List<Long> duplicateArticleIds) throws UDXException;

   public List<Long> getDuplicateUDXArticleIds () throws UDXException;

   public void updateUDXImageURLProcessedStateByBatchId (ProcessingFlagType updatingProcessedState, Long batchId,
            ProcessingFlagType existingProcessedState) throws UDXException;

   public Seed getSeedByNodeAndType (Long nodeId, String type) throws UDXException;

   public void updateSeed (Seed seed) throws UDXException;

}