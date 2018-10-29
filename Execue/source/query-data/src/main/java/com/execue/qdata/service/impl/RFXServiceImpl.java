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
import java.util.Set;

import com.execue.core.common.bean.qdata.AppCategoryMapping;
import com.execue.core.common.bean.qdata.NewsItem;
import com.execue.core.common.bean.qdata.RFXValue;
import com.execue.core.common.bean.qdata.ReducedFormIndex;
import com.execue.core.common.type.ProcessingFlagType;
import com.execue.core.common.type.RFXVariationSubType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.qdata.dataaccess.IRFXDataAccessManager;
import com.execue.qdata.exception.RFXException;
import com.execue.qdata.service.IRFXService;

/**
 * @author John Mallavalli
 */
public class RFXServiceImpl implements IRFXService {

   private IRFXDataAccessManager     rfxDataAccessManager;
   private ICoreConfigurationService coreConfigurationService;

   public IRFXDataAccessManager getRfxDataAccessManager () {
      return rfxDataAccessManager;
   }

   public void setRfxDataAccessManager (IRFXDataAccessManager rfxDataAccessManager) {
      this.rfxDataAccessManager = rfxDataAccessManager;
   }

   public List<NewsItem> getLatestNewsItems () throws RFXException {
      return getRfxDataAccessManager().getLatestNewsItems();
   }

   public List<Long> getLatestNewsItemIds (int batchSize) throws RFXException {
      return getRfxDataAccessManager().getLatestNewsItemIds(batchSize);
   }

   public void updateNewsItem (NewsItem newsItem) throws RFXException {
      getRfxDataAccessManager().updateNewsItem(newsItem);
   }

   public void updateNewsItems (List<NewsItem> newsItems) throws RFXException {
      getRfxDataAccessManager().updateNewsItems(newsItems);
   }

   public void storeRFX (List<ReducedFormIndex> rfxList) throws RFXException {
      getRfxDataAccessManager().storeRFX(rfxList);
   }

   public void storeRFXValue (List<RFXValue> rfxValueList) throws RFXException {
      getRfxDataAccessManager().storeRFXValue(rfxValueList);
   }

   public List<Long> matchAllRFXValue (Set<RFXValue> rfxValueSet, Set<Long> rfIds) throws RFXException {
      StringBuilder querySelect = new StringBuilder(1);
      StringBuilder queryWhere = new StringBuilder(1);
      StringBuilder queryJoin = new StringBuilder(1);

      int i = 1;
      String currentAlias = "rfx" + i;
      String nextAlias = "rfx" + i;
      querySelect.append("select rfx1.rfxId from ContentRFXValue rfx1 ");
      for (RFXValue rfxValue : rfxValueSet) {
         currentAlias = nextAlias;
         queryWhere.append("( " + currentAlias + ".srcConceptBedId= ").append(rfxValue.getSrcConceptBedId()).append(
                  " and " + currentAlias + ".relationBedId=" + rfxValue.getRelationBedId()).append(
                  " and " + currentAlias + ".destConceptBedId=" + rfxValue.getDestConceptBedId()).append(
                  " and " + currentAlias + ".value " + rfxValue.getOperator() + " " + rfxValue.getValue() + ")");
         if (i < rfxValueSet.size()) {
            i++;
            nextAlias = "rfx" + i;
            queryJoin.append(i > 2 ? " AND " : "").append(currentAlias + ".rfxId=" + nextAlias + ".rfxId");
            currentAlias = "rfx" + (i + 1);
            queryWhere.append(" AND ");
            querySelect.append(", ContentRFXValue " + nextAlias);
         }
      }
      StringBuilder finalQuery = new StringBuilder(1);
      finalQuery.append(querySelect).append(" Where ").append("(").append(queryWhere);
      if (ExecueCoreUtil.isNotEmpty(queryJoin.toString())) {
         finalQuery.append(" AND ").append(queryJoin);
      }
      finalQuery.append(") AND ").append(currentAlias).append(".rfxId in (:rfIds)");
      return getRfxDataAccessManager().filterResultsByRFXValueSearch(finalQuery.toString(), rfIds);
   }

   public List<Long> matchAnyRFXValue (Set<RFXValue> rfxValueSet, Set<Long> rfIds) throws RFXException {
      StringBuilder querySelect = new StringBuilder(1);

      querySelect.append("select rfx.rfxId, count(rfx.rfxId) from ContentRFXValue rfx where (");
      for (RFXValue rfxValue : rfxValueSet) {
         querySelect.append("(rfx.srcConceptBedId = ").append(rfxValue.getSrcConceptBedId()).append(
                  " and rfx.relationBedId = " + rfxValue.getRelationBedId()).append(
                  " and rfx.destConceptBedId=" + rfxValue.getDestConceptBedId()).append(
                  " and rfx.value " + rfxValue.getOperator() + " " + rfxValue.getValue() + ") OR ");
      }
      String finalQuery = querySelect.substring(0, querySelect.length() - 3);
      finalQuery = finalQuery + ") AND rfx.rfxId in (:rfIds) GROUP BY rfx.rfxId order by count(rfx.rfxId) desc";
      return getRfxDataAccessManager().filterResultsByRFXValueSearch(finalQuery, rfIds);
   }

   public void cleanUserQueryReducedFormIndexes () throws RFXException {
      Long userQueryRFXMaxExecutionDate = getRfxDataAccessManager().getUserQueryRFXMaxExecutionDate();
      if (userQueryRFXMaxExecutionDate != null) {
         Long maintainRIUserQueryExecutionTime = getCoreConfigurationService().getRIUserQueryExecutionTime();
         getRfxDataAccessManager().deleteUserQueryRFXByExecutionDate(
                  userQueryRFXMaxExecutionDate - maintainRIUserQueryExecutionTime);
      }
   }

   public Map<RFXVariationSubType, Double> getRankingWeightsMapForContentVariationSubType (
            RFXVariationSubType rfxVariationSubType) throws RFXException {
      return getRfxDataAccessManager().getRankingWeightsMapForContentVariationSubType(rfxVariationSubType);
   }

   public void deleteRFXValuesByContentDate (Date contentDate) throws RFXException {
      getRfxDataAccessManager().deleteRFXValuesByContentDate(contentDate);

   }

   public void deleteRFXByContentDate (Date contentDate) throws RFXException {
      getRfxDataAccessManager().deleteRFXByContentDate(contentDate);

   }

   public void deleteRFXByUdxIds (List<Long> udxIds) throws RFXException {
      getRfxDataAccessManager().deleteRFXByUdxIds(udxIds);
   }

   public void deleteRFXValuesByUdxIds (List<Long> udxIds) throws RFXException {
      getRfxDataAccessManager().deleteRFXValuesByUdxIds(udxIds);
   }

   public void updateNewsItemProcessedState (ProcessingFlagType processedState, Long minNewsItemId, Long maxNewsItemId,
            Long batchId) throws RFXException {
      getRfxDataAccessManager().updateNewsItemProcessedState(processedState, minNewsItemId, maxNewsItemId, batchId);
   }

   public void updateNewsItemProcessedStateByByBatchId (ProcessingFlagType updatingProcessedState, Long batchId,
            ProcessingFlagType existingProcessedState) throws RFXException {
      getRfxDataAccessManager().updateNewsItemProcessedStateByByBatchId(updatingProcessedState, batchId,
               existingProcessedState);
   }

   public void cleanUserQueryRFXValue () throws RFXException {
      Long userQueryRFXMaxExecutionDate = getRfxDataAccessManager().getUserQueryRFXMaxExecutionDate();
      if (userQueryRFXMaxExecutionDate != null) {
         Long maintainRIUserQueryExecutionTime = getCoreConfigurationService().getRIUserQueryExecutionTime();
         getRfxDataAccessManager().deleteUserQueryRFXValueByExecutionDate(
                  userQueryRFXMaxExecutionDate - maintainRIUserQueryExecutionTime);
      }

   }

   public List<AppCategoryMapping> getApplicationCategoryMappings () throws RFXException {
      return getRfxDataAccessManager().getAllApplicationCategoryMappings();

   }

   public void storeApplicationCategoryMapping (AppCategoryMapping appCategoryMapping) throws RFXException {
      getRfxDataAccessManager().storeApplicationCategoryMapping(appCategoryMapping);

   }

   public NewsItem getNewsItemById (Long newsItemId) throws RFXException {
      return getRfxDataAccessManager().getNewsItemById(newsItemId);
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

}