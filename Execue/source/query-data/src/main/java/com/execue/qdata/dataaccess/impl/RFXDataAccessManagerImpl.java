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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.qdata.AppCategoryMapping;
import com.execue.core.common.bean.qdata.NewsItem;
import com.execue.core.common.bean.qdata.RFXValue;
import com.execue.core.common.bean.qdata.RIUserQuery;
import com.execue.core.common.bean.qdata.ReducedFormIndex;
import com.execue.core.common.bean.qdata.UQRFX;
import com.execue.core.common.bean.qdata.UserQueryRFXValue;
import com.execue.core.common.type.ProcessingFlagType;
import com.execue.core.common.type.RFXVariationSubType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.qdata.dao.IApplicationCategoryMappingDAO;
import com.execue.dataaccess.qdata.dao.INewsItemDAO;
import com.execue.dataaccess.qdata.dao.IRFXDAO;
import com.execue.qdata.dataaccess.IRFXDataAccessManager;
import com.execue.qdata.dataaccess.QDataDAOComponents;
import com.execue.qdata.exception.RFXException;
import com.execue.qdata.exception.RFXExceptionCodes;

/**
 * @author John Mallavalli
 */
public class RFXDataAccessManagerImpl extends QDataDAOComponents implements IRFXDataAccessManager {

   IRFXDAO                                rfxDAO;
   INewsItemDAO                           newsItemDAO;
   private IApplicationCategoryMappingDAO applicationCategoryMappingDAO;

   /**
    * @return the newsItemDAO
    */
   @Override
   public INewsItemDAO getNewsItemDAO () {
      return newsItemDAO;
   }

   /**
    * @param newsItemDAO
    *           the newsItemDAO to set
    */
   @Override
   public void setNewsItemDAO (INewsItemDAO newsItemDAO) {
      this.newsItemDAO = newsItemDAO;
   }

   public void storeUQRFXEntries (List<UQRFX> uqRFXEntries) throws RFXException {
      try {
         getRfxDAO().createAll(uqRFXEntries);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.qdata.dataaccess.IRFXDataAccessManager#storeRIUserQueryEntries(java.util.List)
    */
   public void storeRIUserQueryEntries (List<RIUserQuery> riUserQueryEntries) throws RFXException {
      try {
         getRfxDAO().createAll(riUserQueryEntries);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.qdata.dataaccess.IRFXDataAccessManager#getNextRFXId()
    */
   public Long getNextRFXId () throws RFXException {
      try {
         return getRfxDAO().getNextRFXId();
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }

   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.qdata.dataaccess.IRFXDataAccessManager#storeRFX(java.util.List)
    */
   public void storeRFX (List<ReducedFormIndex> rfxList) throws RFXException {
      try {
         getRfxDAO().createAll(rfxList);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.qdata.dataaccess.IRFXDataAccessManager#storeRFXValue(java.util.List)
    */
   public void storeRFXValue (List<RFXValue> rfxValueList) throws RFXException {
      try {
         getRfxDAO().createAll(rfxValueList);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void updateNewsItem (NewsItem newsItem) throws RFXException {
      try {
         getRfxDAO().update(newsItem);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void updateNewsItems (List<NewsItem> newsItems) throws RFXException {
      try {
         getRfxDAO().updateAll(newsItems);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<NewsItem> getLatestNewsItems () throws RFXException {
      try {
         return getNewsItemDAO().getLatestNewsItems();
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<Long> getLatestNewsItemIds (int batchSize) throws RFXException {
      try {
         return getNewsItemDAO().getLatestNewsItemIds(batchSize);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<AppCategoryMapping> getAllApplicationCategoryMappings () throws RFXException {
      try {
         return getApplicationCategoryMappingDAO().getAllApplicationCategoryMappings();
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   /**
    * @return the rfxDAO
    */
   public IRFXDAO getRfxDAO () {
      return rfxDAO;
   }

   /**
    * @param rfxDAO
    *           the rfxDAO to set
    */
   public void setRfxDAO (IRFXDAO rfxDAO) {
      this.rfxDAO = rfxDAO;
   }

   public List<Long> filterResultsByRFXValueSearch (String query, Set<Long> rfIds) throws RFXException {
      try {
         return getRfxDAO().filterResultsByRFXValueSearch(query, rfIds);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void deleteUserQueryRFXByExecutionDate (long executionDateTime) throws RFXException {
      Date executionDate = new Date(executionDateTime);
      try {
         getRfxDAO().deleteUserQueryRFXByExecutionDate(executionDate);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public Long getUserQueryRFXMaxExecutionDate () throws RFXException {
      try {
         return getRfxDAO().getUserQueryRFXMaxExecutionDate();
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public Map<RFXVariationSubType, Double> getRankingWeightsMapForContentVariationSubType (
            RFXVariationSubType rfxVariationSubType) throws RFXException {
      try {
         return getRfxDAO().getRankingWeightsMapForContentVariationSubType(rfxVariationSubType);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void deleteRFXValuesByContentDate (Date contentDate) throws RFXException {
      try {
         getRfxDAO().deleteRFXValuesByContentDate(contentDate);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void deleteRFXByContentDate (Date contentDate) throws RFXException {
      try {
         getRfxDAO().deleteRFXByContentDate(contentDate);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void deleteRFXByUdxIds (List<Long> udxIds) throws RFXException {
      try {
         getRfxDAO().deleteRFXByUdxIds(udxIds);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void deleteRFXValuesByUdxIds (List<Long> udxIds) throws RFXException {
      try {
         getRfxDAO().deleteRFXValuesByUdxIds(udxIds);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void updateNewsItemProcessedState (ProcessingFlagType processedState, Long minNewsItemId, Long maxNewsItemId,
            Long batchId) throws RFXException {
      try {
         getNewsItemDAO().updateNewsItemProcessedState(processedState, minNewsItemId, maxNewsItemId, batchId);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void updateNewsItemProcessedStateByByBatchId (ProcessingFlagType updatingProcessedState, Long batchId,
            ProcessingFlagType existingProcessedState) throws RFXException {
      try {
         getNewsItemDAO().updateNewsItemProcessedStateByByBatchId(updatingProcessedState, batchId,
                  existingProcessedState);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public List<UserQueryRFXValue> getUserQueryRFXValuesByQueryId (Long userQueryId) throws RFXException {
      try {
         return getRfxDAO().getUserQueryRFXValuesByQueryId(userQueryId);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void deleteUserQueryRFXValueByExecutionDate (long executionDateTime) throws RFXException {
      Date executionDate = new Date(executionDateTime);
      try {
         getRfxDAO().deleteUserQueryRFXValueByExecutionDate(executionDate);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void storeApplicationCategoryMapping (AppCategoryMapping appCategoryMapping) throws RFXException {
      try {
         getNewsItemDAO().create(appCategoryMapping);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public NewsItem getNewsItemById (Long newsItemId) throws RFXException {
      try {
         return getNewsItemDAO().getById(newsItemId, NewsItem.class);
      } catch (DataAccessException dataAccessException) {
         throw new RFXException(RFXExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public IApplicationCategoryMappingDAO getApplicationCategoryMappingDAO () {
      return applicationCategoryMappingDAO;
   }

   public void setApplicationCategoryMappingDAO (IApplicationCategoryMappingDAO applicationCategoryMappingDAO) {
      this.applicationCategoryMappingDAO = applicationCategoryMappingDAO;
   }
}