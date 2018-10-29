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


package com.execue.qdata.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.entity.Seed;
import com.execue.core.common.bean.qdata.AppNewsPopularity;
import com.execue.core.common.bean.qdata.RIInstanceTripleDefinition;
import com.execue.core.common.bean.qdata.RIUniversalSearch;
import com.execue.core.common.bean.qdata.RIUserQuery;
import com.execue.core.common.bean.qdata.ReducedFormIndex;
import com.execue.core.common.bean.qdata.UDXKeyword;
import com.execue.core.common.bean.qdata.UnStructuredIndex;
import com.execue.core.common.bean.qdata.UniversalSearchResult;
import com.execue.core.common.bean.uss.UniversalUnstructuredSearchResult;
import com.execue.core.common.type.ProcessingFlagType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.qdata.dataaccess.IRFXDataAccessManager;
import com.execue.qdata.dataaccess.IUDXDataAccessManager;
import com.execue.qdata.exception.UDXException;
import com.execue.qdata.service.IUDXService;

public class UDXServiceImpl implements IUDXService {

   IUDXDataAccessManager             udxDataAccessManager;
   IRFXDataAccessManager             rfxDataAccessManager;
   private ICoreConfigurationService coreConfigurationService;

   public IUDXDataAccessManager getUdxDataAccessManager () {
      return udxDataAccessManager;
   }

   public void setUdxDataAccessManager (IUDXDataAccessManager udxDataAccessManager) {
      this.udxDataAccessManager = udxDataAccessManager;
   }

   public UnStructuredIndex storeUDX (UnStructuredIndex udx) throws UDXException {
      return getUdxDataAccessManager().storeUDX(udx);
   }

   public void updatedUDX (UnStructuredIndex udx) throws UDXException {
      getUdxDataAccessManager().updateUDX(udx);
   }

   public void updateUnstructuredIndexes (List<UnStructuredIndex> unstructuredIndexes) throws UDXException {
      getUdxDataAccessManager().updateUnstructuredIndexes(unstructuredIndexes);
   }

   public void storeRIUDXEntries (List<RIUniversalSearch> riUdxEntries) throws UDXException {
      getUdxDataAccessManager().storeRIUDXEntries(riUdxEntries);
   }

   public List<Long> getUDXIdsByRFId (Long rfId) throws UDXException {
      try {
         return getUdxDataAccessManager().getUDXIdsByRFId(rfId);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }
   }

   public Map<Long, List<Long>> getUDXIdsMapByRFIds (List<Long> rfIds) throws UDXException {
      try {
         return getUdxDataAccessManager().getUDXIdsMapByRFIds(rfIds);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }
   }

   public Map<Long, List<UnStructuredIndex>> getUDXMapByRFIds (List<Long> rfIds) throws UDXException {
      try {
         return getUdxDataAccessManager().getUDXMapByRFIds(rfIds);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }
   }

   public List<UniversalSearchResult> getUniversalSearchMatchForQueryCache (Long userQueryId) throws UDXException {
      try {
         return getUdxDataAccessManager().getUniversalSearchMatchForQueryCache(userQueryId);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }
   }

   public List<UniversalSearchResult> getUniversalSearchMatchForRelatedQuery (Long userQueryId) throws UDXException {
      try {
         return getUdxDataAccessManager().getUniversalSearchMatchForRelatedQuery(userQueryId);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.qdata.IUDXService#getRIUnstructuredIndexByUserQueryId(java.lang.Long)
    */
   public UniversalUnstructuredSearchResult getRIUnstructuredResultsByUserQuery (Long userQueryId, Integer pageNumber,
            Integer maxResults) throws UDXException {
      try {
         return getUdxDataAccessManager().getRIUnstructuredResultsByUserQuery(userQueryId, pageNumber, maxResults);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }
   }

   public List<RIInstanceTripleDefinition> getRIInstanceTriplesForUserQuery (Long userQueryId) throws UDXException {
      try {
         List<RIInstanceTripleDefinition> riInstanceTriplesList = getUdxDataAccessManager()
                  .getRIInstanceTriplesForUserQuery(userQueryId);
         return riInstanceTriplesList;
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }
   }

   public void saveUserQueryReverseIndex (List<RIUserQuery> userQueryRIEntries) throws UDXException {
      try {
         getUdxDataAccessManager().saveUserQueryReverseIndex(userQueryRIEntries);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae.getMessage(), dae);
      }
   }

   public RIUniversalSearch getRIUnstructuredIndexById (Long id) throws UDXException {
      try {
         return getUdxDataAccessManager().getRIUnstructuredIndexById(id);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae.getMessage(), dae);
      }
   }

   public UnStructuredIndex getUnstructuredIndexById (Long udxId) throws UDXException {
      try {
         return getUdxDataAccessManager().getUnstructuredIndexById(udxId);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae.getMessage(), dae);
      }
   }

   public ReducedFormIndex getReducedFormIndexById (Long rfxId) throws UDXException {
      try {
         return getUdxDataAccessManager().getReducedFormIndexById(rfxId);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae.getMessage(), dae);
      }
   }

   public void createAppNewsPopularity (List<AppNewsPopularity> appNewsPopularityList) throws UDXException {
      try {
         getUdxDataAccessManager().createAppNewsPopularity(appNewsPopularityList);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae.getMessage(), dae);
      }
   }

   public void cleanRIUserQueries () throws UDXException {
      try {
         Long RIuserQueryMaxExecutionDate = getUdxDataAccessManager().getRIUserQueryMaxExecutionDate();
         if (RIuserQueryMaxExecutionDate != null) {
            Long maintainRIUserQueryExecutionTime = getCoreConfigurationService().getRIUserQueryExecutionTime();
            getUdxDataAccessManager().deleteRIUserQueriesByExecutionDate(
                     RIuserQueryMaxExecutionDate - maintainRIUserQueryExecutionTime);
         }
      } catch (DataAccessException dae) {
         dae.printStackTrace();
         throw new UDXException(dae.getCode(), dae);
      }
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.api.qdata.IUDXService#getUniversalSearchResultForQuery(java.lang.Long, java.lang.Long,
    *      java.lang.Long)
    */
   public UniversalUnstructuredSearchResult getUniversalSearchResultForQuery (Long userQueryId, Long pageNumber,
            Long maxResults, boolean applyKeyWordSearchFilter) throws UDXException {
      try {
         Integer totalCount = getUdxDataAccessManager().populateUniversalSearchResultForUserQuery(userQueryId,
                  applyKeyWordSearchFilter);
         getUdxDataAccessManager().updateUdxResultWeightBasedOnRFXValues(userQueryId);
         return getUdxDataAccessManager().getUdxResultsInRange(userQueryId, pageNumber, maxResults, totalCount);
      } catch (DataAccessException e) {
         throw new UDXException(e.getCode(), e);
      }
   }

   public UniversalUnstructuredSearchResult getNextUniversalSearchMatchForUdx (Long userQueryId, Long pageNumber,
            Long maxResults, int totalCount) {
      return getUdxDataAccessManager().getUdxResultsInRange(userQueryId, pageNumber, maxResults, totalCount);
   }

   public void deleteRIUnstructuredIndexesByContentDate (Date contentDate, String contentSourceType)
            throws UDXException {
      try {
         getUdxDataAccessManager().deleteRIUnstructuredIndexesByContentDate(contentDate, contentSourceType);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }

   }

   public void deleteUdxAttributesByContentDate (Date contentDate) throws UDXException {
      try {
         getUdxDataAccessManager().deleteUdxAttributesByContentDate(contentDate);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }
   }

   public void deleteUDXByContentDate (Date contentDate, String contentSourceType) throws UDXException {
      try {
         getUdxDataAccessManager().deleteUDXByContentDate(contentDate, contentSourceType);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }
   }

   public List<UnStructuredIndex> getUnstructuredIndexesByContentSourceType (String contentSourceType, Long batchNum,
            int batchSize) throws UDXException {
      try {
         return getUdxDataAccessManager().getUnstructuredIndexesByContentSourceType(contentSourceType, batchNum,
                  batchSize);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }
   }

   public List<UnStructuredIndex> getUnstructuredIndexesByProcessedState (ProcessingFlagType processed, int batchSize,
            String contentSourceType) throws UDXException {
      try {
         return getUdxDataAccessManager().getUnstructuredIndexesByProcessedState(processed, batchSize,
                  contentSourceType);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }
   }

   public void deleteRIUnstructuredIndexesByUdxIds (List<Long> udxIds) throws UDXException {
      try {
         getUdxDataAccessManager().deleteRIUnstructuredIndexesByUdxIds(udxIds);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }

   }

   public void deleteUDXById (List<Long> ids) throws UDXException {
      try {
         getUdxDataAccessManager().deleteUDXById(ids);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }

   }

   public void deleteUdxAttributesByUdxIds (List<Long> udxIds) throws UDXException {
      try {
         getUdxDataAccessManager().deleteUDXById(udxIds);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }

   }

   public Integer populateUdxKeyWordMatchResultForUserQuery (Long userQueryId, String queryTokens) throws UDXException {
      try {
         return getUdxDataAccessManager().populateUdxKeyWordMatchResultForUserQuery(userQueryId, queryTokens);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }
   }

   public void updateUDXImageURLProcessedState (ProcessingFlagType processedState, Long minUDXID, Long maxUDXID,
            Long batchId) throws UDXException {
      try {
         getUdxDataAccessManager().updateUDXImageURLProcessedState(processedState, minUDXID, maxUDXID, batchId);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }

   }

   public void cleanUdxResults () throws UDXException {
      try {
         Long RIuserQueryMaxExecutionDate = getUdxDataAccessManager().getRIUserQueryMaxExecutionDate();
         if (RIuserQueryMaxExecutionDate != null) {
            Long maintainRIUserQueryExecutionTime = getCoreConfigurationService().getRIUserQueryExecutionTime();
            getUdxDataAccessManager().deleteUdxResultsByExecutionDate(
                     RIuserQueryMaxExecutionDate - maintainRIUserQueryExecutionTime);
         }
      } catch (DataAccessException dae) {
         dae.printStackTrace();
         throw new UDXException(dae.getCode(), dae);
      }
   }

   public void createUDXKeywords (List<UDXKeyword> udxKeywords) throws UDXException {
      try {
         getUdxDataAccessManager().createUDXKeywords(udxKeywords);
      } catch (DataAccessException dae) {
         dae.printStackTrace();
         throw new UDXException(dae.getCode(), dae);
      }

   }

   /**
    * @return the rfxDataAccessManager
    */
   public IRFXDataAccessManager getRfxDataAccessManager () {
      return rfxDataAccessManager;
   }

   /**
    * @param rfxDataAccessManager
    *           the rfxDataAccessManager to set
    */
   public void setRfxDataAccessManager (IRFXDataAccessManager rfxDataAccessManager) {
      this.rfxDataAccessManager = rfxDataAccessManager;
   }

   public void deleteUDXKeyWordByContentDate (Date contentDate) throws UDXException {
      try {
         getUdxDataAccessManager().deleteUDXKeyWordByContentDate(contentDate);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }
   }

   public void cleanDuplicateArticles (List<Long> duplicateArticleIds) throws UDXException {
      getUdxDataAccessManager().cleanUDXDuplicateArticles(duplicateArticleIds);
   }

   public List<Long> getDuplicateUDXArticleIds () throws UDXException {
      return getUdxDataAccessManager().getDuplicateUDXArticleIds();
   }

   public void updateUDXImageURLProcessedStateByBatchId (ProcessingFlagType updatingProcessedState, Long batchId,
            ProcessingFlagType existingProcessedState) throws UDXException {
      try {
         getUdxDataAccessManager().updateUDXImageURLProcessedStateByBatchId(updatingProcessedState, batchId,
                  existingProcessedState);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }

   }

   public Seed getSeedByNodeAndType (Long nodeId, String type) throws UDXException {
      try {
         return getUdxDataAccessManager().getSeedByNodeAndType(nodeId, type);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }
   }

   public void updateSeed (Seed seed) throws UDXException {
      try {
         getUdxDataAccessManager().updateSeed(seed);
      } catch (DataAccessException dae) {
         throw new UDXException(dae.getCode(), dae);
      }
   }
}