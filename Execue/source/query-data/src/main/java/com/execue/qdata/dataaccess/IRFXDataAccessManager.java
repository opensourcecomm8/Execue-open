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
import com.execue.qdata.exception.RFXException;

/**
 * @author John Mallavalli
 */
public interface IRFXDataAccessManager {

   public void storeUQRFXEntries (List<UQRFX> uqRFXEntries) throws RFXException;

   public void storeRIUserQueryEntries (List<RIUserQuery> riUserQueryEntries) throws RFXException;

   public Long getNextRFXId () throws RFXException;

   public void storeRFX (List<ReducedFormIndex> rfxList) throws RFXException;

   public List<NewsItem> getLatestNewsItems () throws RFXException;

   public void updateNewsItem (NewsItem newsItem) throws RFXException;

   public void storeRFXValue (List<RFXValue> rfxValueList) throws RFXException;

   public List<Long> filterResultsByRFXValueSearch (String query, Set<Long> rfIds) throws RFXException;

   public Long getUserQueryRFXMaxExecutionDate () throws RFXException;

   public void deleteUserQueryRFXByExecutionDate (long executionDateTime) throws RFXException;

   public Map<RFXVariationSubType, Double> getRankingWeightsMapForContentVariationSubType (
            RFXVariationSubType rfxVariationSubType) throws RFXException;

   public List<Long> getLatestNewsItemIds (int batchSize) throws RFXException;

   public void updateNewsItems (List<NewsItem> newsItems) throws RFXException;

   public void deleteRFXValuesByContentDate (Date contentDate) throws RFXException;

   public void deleteRFXByContentDate (Date contentDate) throws RFXException;

   public void deleteRFXByUdxIds (List<Long> udxIds) throws RFXException;

   public void deleteRFXValuesByUdxIds (List<Long> udxIds) throws RFXException;

   public void updateNewsItemProcessedState (ProcessingFlagType processedState, Long minNewsItemId, Long maxNewsItemId,
            Long batchId) throws RFXException;

   public void updateNewsItemProcessedStateByByBatchId (ProcessingFlagType updatingProcessedState, Long batchId,
            ProcessingFlagType existingProcessedState) throws RFXException;

   public List<UserQueryRFXValue> getUserQueryRFXValuesByQueryId (Long userQueryId) throws RFXException;

   public void deleteUserQueryRFXValueByExecutionDate (long executionDateTime) throws RFXException;

   public List<AppCategoryMapping> getAllApplicationCategoryMappings () throws RFXException;

   public void storeApplicationCategoryMapping (AppCategoryMapping appCategoryMapping) throws RFXException;

   public NewsItem getNewsItemById (Long newsItemId) throws RFXException;

}