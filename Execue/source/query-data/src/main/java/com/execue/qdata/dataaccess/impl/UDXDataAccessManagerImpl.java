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


package com.execue.qdata.dataaccess.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.entity.Seed;
import com.execue.core.common.bean.qdata.AdditionalInfo;
import com.execue.core.common.bean.qdata.AppNewsPopularity;
import com.execue.core.common.bean.qdata.ContentReducedFormIndex;
import com.execue.core.common.bean.qdata.EntityAttribute;
import com.execue.core.common.bean.qdata.RIInstanceTripleDefinition;
import com.execue.core.common.bean.qdata.RIUniversalSearch;
import com.execue.core.common.bean.qdata.RIUserQuery;
import com.execue.core.common.bean.qdata.UDXKeyword;
import com.execue.core.common.bean.qdata.UnStructuredIndex;
import com.execue.core.common.bean.qdata.UniversalSearchResult;
import com.execue.core.common.bean.uss.UniversalUnstructuredSearchResult;
import com.execue.core.common.bean.uss.UnstructuredSearchResultItem;
import com.execue.core.common.type.ProcessingFlagType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.qdata.dao.IUDXDAO;
import com.execue.qdata.dataaccess.IUDXDataAccessManager;
import com.execue.qdata.exception.UDXException;
import com.execue.qdata.exception.UDXExceptionCodes;

public class UDXDataAccessManagerImpl implements IUDXDataAccessManager {

   private IUDXDAO udxDAO;

   /**
    * @return the udxDAO
    */
   public IUDXDAO getUdxDAO () {
      return udxDAO;
   }

   /**
    * @param udxDAO
    */
   public void setUdxDAO (IUDXDAO udxDAO) {
      this.udxDAO = udxDAO;
   }

   public UnStructuredIndex storeUDX (UnStructuredIndex udx) throws UDXException {
      try {
         return getUdxDAO().create(udx);
      } catch (DataAccessException e) {
         throw new UDXException(UDXExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updateUDX (UnStructuredIndex udx) throws UDXException {
      try {
         getUdxDAO().update(udx);
      } catch (DataAccessException e) {
         throw new UDXException(UDXExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void storeRIUDXEntries (List<RIUniversalSearch> udxEntries) throws UDXException {
      try {
         getUdxDAO().createAll(udxEntries);
      } catch (DataAccessException dataAccessException) {
         throw new UDXException(UDXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.qdata.dataaccess.IUDXDataAccessManager#getUDXIdsByRFId(java.lang.Long)
    */
   public List<Long> getUDXIdsByRFId (Long rfId) throws DataAccessException {
      return getUdxDAO().getUDXIdsByRFId(rfId);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.qdata.dataaccess.IUDXDataAccessManager#getUDXIdsMapByRFIds(java.util.List)
    */
   public Map<Long, List<Long>> getUDXIdsMapByRFIds (List<Long> rfIds) throws DataAccessException {
      Map<Long, List<Long>> udxIdsMap = new HashMap<Long, List<Long>>();
      List<Long> udxIds = null;
      List<UnStructuredIndex> udxs = getUdxDAO().getUDXByRFIds(rfIds);
      for (UnStructuredIndex unStructuredIndex : udxs) {
         if (udxIdsMap.get(unStructuredIndex.getRfId()) != null) {
            udxIds = udxIdsMap.get(unStructuredIndex.getRfId());
            udxIds.add(unStructuredIndex.getId());
         } else {
            udxIds = new ArrayList<Long>();
            udxIds.add(unStructuredIndex.getId());
            udxIdsMap.put(unStructuredIndex.getRfId(), udxIds);
         }
      }
      return udxIdsMap;
   }

   public Map<Long, List<UnStructuredIndex>> getUDXMapByRFIds (List<Long> rfIds) throws DataAccessException {
      Map<Long, List<UnStructuredIndex>> udxsMap = new HashMap<Long, List<UnStructuredIndex>>();
      List<UnStructuredIndex> udxsTemp = null;
      List<UnStructuredIndex> udxs = getUdxDAO().getUDXByRFIds(rfIds);
      for (UnStructuredIndex unStructuredIndex : udxs) {
         if (udxsMap.get(unStructuredIndex.getRfId()) != null) {
            udxsTemp = udxsMap.get(unStructuredIndex.getRfId());
            udxsTemp.add(unStructuredIndex);
         } else {
            udxsTemp = new ArrayList<UnStructuredIndex>();
            udxsTemp.add(unStructuredIndex);
            udxsMap.put(unStructuredIndex.getRfId(), udxsTemp);
         }
      }
      return udxsMap;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.qdata.dataaccess.IUDXDataAccessManager#getRIUnstructuredResultsByUserQuery(java.lang.Long)
    */
   public UniversalUnstructuredSearchResult getRIUnstructuredResultsByUserQuery (Long userQueryId, Integer pageNumber,
            Integer maxResults) throws DataAccessException {
      UniversalUnstructuredSearchResult universalUnstrucyuredSearchResult = new UniversalUnstructuredSearchResult();
      universalUnstrucyuredSearchResult.setTotalCount(getUdxDAO().getCountOfRIUdxByUserQueryId(userQueryId));
      universalUnstrucyuredSearchResult.setUnstructuredSearchResultItem(getRIUdxByUserQueryId(userQueryId, pageNumber,
               maxResults));
      universalUnstrucyuredSearchResult.setUserQueryId(userQueryId);
      return universalUnstrucyuredSearchResult;
   }

   public void saveUserQueryReverseIndex (RIUserQuery userQuery) throws DataAccessException {
      getUdxDAO().create(userQuery);
   }

   public void saveUserQueryReverseIndex (List<RIUserQuery> userQueryList) throws DataAccessException {
      getUdxDAO().createAll(userQueryList);
   }

   @SuppressWarnings ("unchecked")
   public List<UnstructuredSearchResultItem> getRIUdxByUserQueryId (Long userQueryId, Integer pageNumber,
            Integer maxResults) throws DataAccessException {
      List<Object[]> resultObjects = getUdxDAO().getRIUdxByUserQueryId(userQueryId, pageNumber, maxResults);
      List<UnstructuredSearchResultItem> unstructuredResultList = new ArrayList<UnstructuredSearchResultItem>();
      for (Object[] array : resultObjects) {
         Long udxId = (Long) array[0];
         // Double matchWeight = (Double) array[1];
         // Double maxWeight = (Double) array[2];
         // Double rank = matchWeight / maxWeight * 100;
         Double rank = (Double) array[1];
         Integer searchType = (Integer) array[2];
         Long applicationId = (Long) array[3];
         UnstructuredSearchResultItem unstructuredResult = new UnstructuredSearchResultItem();
         UnStructuredIndex unStructuredIndex = getUnstructuredIndexById(udxId);
         unstructuredResult.setContentSource(unStructuredIndex.getContentSource());
         unstructuredResult.setRfId(unStructuredIndex.getRfId());
         unstructuredResult.setContentSourceType(unStructuredIndex.getContentSourceType());
         unstructuredResult.setLongDescription(unStructuredIndex.getLongDescription());
         unstructuredResult.setShortDescription(unStructuredIndex.getShortDescription());
         unstructuredResult.setUrl(unStructuredIndex.getUrl());
         unstructuredResult.setWeight(rank);
         unstructuredResult.setSearchType(getSearchTypeString(searchType));
         // TODO: NK: Currently setting the name as udx short description
         // should remove below line once we have the rfx table correctly populated
         unstructuredResult.setName(unStructuredIndex.getShortDescription());
         unstructuredResult.setApplicationId(applicationId);

         unstructuredResultList.add(unstructuredResult);
      }
      // if (!CollectionUtils.isEmpty(unstructuredResultList)) {
      // Collections.sort(unstructuredResultList, new Comparator<UnstructuredSearchResultItem>() {
      //
      // public int compare (UnstructuredSearchResultItem o1, UnstructuredSearchResultItem o2) {
      // return (int) (o2.getWeight() - o1.getWeight());
      // }
      // });
      // }

      return unstructuredResultList;
   }

   private String getSearchTypeString (Integer searchType) {
      switch (searchType) {
         case 0:
            return "KeyWord Match";
         case 1:
            return "Full Match";
         case 2:
            return "Partial Match";
         case 3:
            return "Entity Match";
      }
      return "";
   }

   public RIUniversalSearch getRIUnstructuredIndexById (Long id) throws DataAccessException {
      return getUdxDAO().getById(id, RIUniversalSearch.class);
   }

   public UnStructuredIndex getUnstructuredIndexById (Long udxId) throws DataAccessException {
      return getUdxDAO().getById(udxId, UnStructuredIndex.class);
   }

   public ContentReducedFormIndex getReducedFormIndexById (Long rfxId) throws DataAccessException {
      return getUdxDAO().getById(rfxId, ContentReducedFormIndex.class);
   }

   public List<RIInstanceTripleDefinition> getRIInstanceTriplesForUserQuery (Long userQueryId)
            throws DataAccessException {
      return getUdxDAO().getRIInstanceTriplesForUserQuery(userQueryId);
   }  
   
   public List<UniversalSearchResult> getUniversalSearchMatchForQueryCache (Long userQueryId)
            throws DataAccessException {
      return getUdxDAO().getUniversalSearchMatchForQueryCache(userQueryId);
   }

   public List<UniversalSearchResult> getUniversalSearchMatchForRelatedQuery (Long userQueryId)
            throws DataAccessException {
      return getUdxDAO().getUniversalSearchMatchForRelatedQuery(userQueryId);
   }

   public void createAppNewsPopularity (List<AppNewsPopularity> appNewsPopularityList) throws DataAccessException {
      getUdxDAO().createAll(appNewsPopularityList);
   }

   public Long getRIUserQueryMaxExecutionDate () throws DataAccessException {
      return getUdxDAO().getRIUserQueryMaxExecutionDate();
   }

   public void deleteRIUserQueriesByExecutionDate (Long executionDateTime) throws DataAccessException {
      Date executionDate = new Date(executionDateTime);
      getUdxDAO().deleteRIUserQueriesByExecutionDate(executionDate);
   }

   public void deleteRIUnstructuredIndexesByContentDate (Date contentDate, String contentSourceType)
            throws DataAccessException {
      getUdxDAO().deleteRIUnstructuredIndexesByContentDate(contentDate, contentSourceType);

   }

   public void deleteUdxAttributesByContentDate (Date contentDate) throws DataAccessException {
      getUdxDAO().deleteUdxAttributesByContentDate(contentDate);
   }

   public void deleteUDXByContentDate (Date contentDate, String contentSourceType) throws DataAccessException {
      getUdxDAO().deleteUDXByContentDate(contentDate, contentSourceType);
   }

   public List<UnStructuredIndex> getUnstructuredIndexesByContentSourceType (String contentSourceType, Long batchNum,
            int batchSize) throws DataAccessException {
      return getUdxDAO().getUnstructuredIndexesByContentSourceType(contentSourceType, batchNum, batchSize);
   }

   public List<UnStructuredIndex> getUnstructuredIndexesByProcessedState (ProcessingFlagType processed, int batchSize,
            String contentSourceType) throws DataAccessException {
      return getUdxDAO().getUnstructuredIndexesByProcessedState(processed, batchSize, contentSourceType);
   }

   public void deleteRIUnstructuredIndexesByUdxIds (List<Long> udxIds) throws DataAccessException {
      getUdxDAO().deleteRIUnstructuredIndexesByUdxIds(udxIds);

   }

   public void deleteUDXById (List<Long> ids) throws DataAccessException {
      getUdxDAO().deleteUDXById(ids);

   }

   public void deleteUdxAttributesByUdxIds (List<Long> udxIds) throws DataAccessException {
      getUdxDAO().deleteUdxAttributesByUdxIds(udxIds);

   }

   public Seed getSeedByNodeAndType (Long nodeId, String type) throws DataAccessException {
      return getUdxDAO().getSeedByNodeAndType(nodeId, type);
   }

   public void updateSeed (Seed seed) throws DataAccessException {
      getUdxDAO().update(seed);
   }

   public Integer populateUniversalSearchResultForUserQuery (Long userQueryId, boolean applyKeyWordSearchFilter)
            throws DataAccessException {
      return getUdxDAO().populateUniversalSearchResult(userQueryId, applyKeyWordSearchFilter);
   }

   public Integer populateUdxKeyWordMatchResultForUserQuery (Long userQueryId, String queryTokens)
            throws DataAccessException {
      return getUdxDAO().populateUdxKeyWordMatchResultForUserQuery(userQueryId, queryTokens);
   }

   public void updateUDXImageURLProcessedState (ProcessingFlagType processedState, Long minUDXID, Long maxUDXID,
            Long batchId) throws DataAccessException {
      getUdxDAO().updateUDXImageURLProcessedState(processedState, minUDXID, maxUDXID, batchId);
   }

   public UniversalUnstructuredSearchResult getUdxResultsInRange (Long userQueryId, Long pageNumber, Long maxResults,
            Integer totalCount) {
      List<Object[]> resultObjects = getUdxDAO().getUdxResultsInRange(userQueryId, pageNumber, maxResults);
      List<UnstructuredSearchResultItem> unstructuredResultList = new ArrayList<UnstructuredSearchResultItem>();

      for (Object[] array : resultObjects) {
         Long id = (Long) array[0];
         Long rfId = (Long) array[1];
         String url = (String) array[2];
         String contentSource = (String) array[3];
         String contentSourceType = (String) array[4];
         String longDescription = (String) array[5];
         String shortDescription = (String) array[6];
         Date contentDate = (Date) array[7];
         Integer searchType = (Integer) array[9];
         Long applicationId = (Long) array[10];
         UnstructuredSearchResultItem unstructuredResult = new UnstructuredSearchResultItem();
         unstructuredResult.setId(id);
         unstructuredResult.setContentSource(contentSource);
         unstructuredResult.setRfId(rfId);
         unstructuredResult.setContentSourceType(contentSourceType);
         unstructuredResult.setLongDescription(longDescription);
         unstructuredResult.setShortDescription(shortDescription);
         unstructuredResult.setUrl(url);
         unstructuredResult.setWeight((Double) array[8]);
         unstructuredResult.setContentDate(contentDate);
         unstructuredResult.setSearchType(getSearchTypeString(searchType));
         // TODO: NK: Currently setting the name as udx short description
         // should remove below line once we have the rfx table correctly populated
         unstructuredResult.setName(shortDescription);
         unstructuredResult.setApplicationId(applicationId);

         unstructuredResultList.add(unstructuredResult);
      }
      UniversalUnstructuredSearchResult universalUnstrucyuredSearchResult = new UniversalUnstructuredSearchResult();
      // TODO Change The count
      universalUnstrucyuredSearchResult.setTotalCount(totalCount);
      universalUnstrucyuredSearchResult.setUnstructuredSearchResultItem(unstructuredResultList);
      universalUnstrucyuredSearchResult.setUserQueryId(userQueryId);
      return universalUnstrucyuredSearchResult;
   }

   public void updateUdxResultWeightBasedOnRFXValues (Long userQueryId) throws DataAccessException {
      getUdxDAO().updateUdxResultWeightBasedOnRFXValues(userQueryId);

   }

   public void deleteUdxResultsByExecutionDate (long executionDateTime) throws DataAccessException {
      Date executionDate = new Date(executionDateTime);
      getUdxDAO().deleteUdxResultsByExecutionDate(executionDate);
   }

   public void createUDXKeywords (List<UDXKeyword> udxKeywords) throws DataAccessException {
      getUdxDAO().createAll(udxKeywords);
   }

   public void deleteUDXKeyWordByContentDate (Date contentDate) throws DataAccessException {
      getUdxDAO().deleteUDXKeyWordByContentDate(contentDate);
   }

   public void updateUnstructuredIndexes (List<UnStructuredIndex> unstructuredIndexes) throws UDXException {
      try {
         getUdxDAO().updateUnstructuredIndexes(unstructuredIndexes);
      } catch (DataAccessException dataAccessException) {
         throw new UDXException(UDXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void cleanUDXDuplicateArticles (List<Long> duplicateArticleIds) throws UDXException {
      try {
         getUdxDAO().cleanDuplicateArticles(duplicateArticleIds);
      } catch (DataAccessException dataAccessException) {
         throw new UDXException(UDXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Long> getDuplicateUDXArticleIds () throws UDXException {
      try {
         return getUdxDAO().getDuplicateUDXArticleIds();
      } catch (DataAccessException dataAccessException) {
         throw new UDXException(UDXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void updateUDXImageURLProcessedStateByBatchId (ProcessingFlagType updatingProcessedState, Long batchId,
            ProcessingFlagType existingProcessedState) throws DataAccessException {
      getUdxDAO().updateUDXImageURLProcessedStateByBatchId(updatingProcessedState, batchId, existingProcessedState);

   }
}